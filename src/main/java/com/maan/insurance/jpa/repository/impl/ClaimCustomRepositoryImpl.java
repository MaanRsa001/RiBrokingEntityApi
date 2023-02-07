package com.maan.insurance.jpa.repository.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.claim.TtrnClaimAcc;
import com.maan.insurance.jpa.entity.claim.TtrnClaimPaymentArchive;
import com.maan.insurance.jpa.entity.claim.TtrnClaimReview;
import com.maan.insurance.jpa.entity.claim.TtrnClaimUpdation;
import com.maan.insurance.jpa.entity.facultative.TtrnFacRiskProposal;
import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.repository.claim.ClaimCustomRepository;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.entity.TtrnClaimPaymentRi;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.UnderwritterMaster;
import com.maan.insurance.model.req.claim.ClaimListReq;
import com.maan.insurance.model.req.claim.ClaimPaymentEditReq;
import com.maan.insurance.model.req.claim.ClaimPaymentListReq;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;

@Repository
public class ClaimCustomRepositoryImpl implements ClaimCustomRepository{
	
	@Autowired
	EntityManager em;

	@Override
	public List<Tuple> getClaimAllocationList(String contractNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		//before union all
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> tatRoot = cq.from(TtrnAllocatedTransaction.class);
		Root<TtrnPaymentReceipt> tprRoot = cq.from(TtrnPaymentReceipt.class);
		
		cq.multiselect(tatRoot.get("sno").alias("SNO"), 
				tatRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				tatRoot.get("transactionNo").alias("TRANSACTION_NO"),
				tatRoot.get("productName").alias("PRODUCT_NAME"),
				cb.selectCase().when(cb.equal(tatRoot.get("type"), "C"), "CLAIM").alias("TYPE"),
				tatRoot.get("paidAmount").alias("PAID_AMOUNT"),
				tatRoot.get("currencyId").alias("CURRENCY_ID"),
				cb.selectCase().when(cb.greaterThanOrEqualTo(tatRoot.<Double>get("paidAmount"), 0.0), +1).otherwise(-1).alias("SIGN"),
				cb.selectCase().when(cb.equal(tatRoot.get("status"), "R"), "Reverted")
							   .when(cb.equal(tatRoot.get("status"), "Y"), "Allocated").alias("STATUS"),
			    tatRoot.get("receiptNo").alias("RECEIPT_NO"),
			    cb.selectCase().when(cb.equal(tprRoot.get("transType"), "PT"), "PAYMENT")
				   .when(cb.equal(tprRoot.get("transType"), "RT"), "RECEIPT").alias("TRANS_TYPE"));
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), tprRoot.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), tprRoot.get("branchCode")));
		
		cq.where(cb.equal(tatRoot.get("receiptNo"), tprRoot.get("paymentReceiptNo")),
				 cb.equal(tatRoot.get("contractNo"), contractNo),
				 cb.equal(tatRoot.get("transactionNo"), transactionNo),
				 cb.equal(tatRoot.get("type"), "C"),
				 cb.equal(tatRoot.get("status"), "Y"),
				 cb.equal(tprRoot.get("amendId"), sq));
				
		List<Tuple> list = em.createQuery(cq).getResultList();
		
		//After union all
		CriteriaQuery<Tuple> ucq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> utatRoot = ucq.from(TtrnAllocatedTransaction.class);
		
		cq.multiselect(utatRoot.get("sno").alias("SNO"), 
				utatRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				utatRoot.get("transactionNo").alias("TRANSACTION_NO"),
				utatRoot.get("productName").alias("PRODUCT_NAME"),
				cb.selectCase().when(cb.equal(utatRoot.get("type"), "C"), "CLAIM").alias("TYPE"),
				utatRoot.get("paidAmount").alias("PAID_AMOUNT"),
				utatRoot.get("currencyId").alias("CURRENCY_ID"),
				cb.selectCase().when(cb.greaterThanOrEqualTo(utatRoot.<Double>get("paidAmount"), 0.0), +1).otherwise(-1).alias("SIGN"),
				cb.literal("Adjusted").alias("STATUS"),
				utatRoot.get("receiptNo").alias("RECEIPT_NO"),
			    cb.selectCase().when(cb.equal(utatRoot.get("adjustmentType"), "W"), "Write Off")
				   .when(cb.equal(utatRoot.get("adjustmentType"), "R"), "Round Off").alias("TRANS_TYPE"));
		
		Subquery<Integer> usq = ucq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> usubRoot = usq.from(TtrnPaymentReceipt.class);

		usq.select(usubRoot.get("paymentReceiptNo")).where(
				cb.equal(utatRoot.get("receiptNo"), usubRoot.get("paymentReceiptNo")));
		
		ucq.where(cb.equal(utatRoot.get("contractNo"), contractNo),
				 cb.equal(utatRoot.get("transactionNo"), transactionNo),
				 cb.equal(utatRoot.get("type"), "C"),
				 cb.equal(utatRoot.get("status"), "Y"),
				 cb.not(cb.in(utatRoot.get("receiptNo")).value(usq)));
				
		List<Tuple> ulist = em.createQuery(cq).getResultList();
		list.addAll(ulist);
		return list;
	}

	@Override
	public String selectSumPaidAmt(String claimNo, String contractNo) {
		String paidamount="";
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(cb.sum(root.get("paidAmountOc")).alias("PAID_AMOUNT_OC"))
		.where(cb.equal(root.get("claimNo"), claimNo),
				cb.equal(root.get("contractNo"), contractNo));
		List<Tuple> ulist = em.createQuery(cq).getResultList();
		if(CollectionUtils.isEmpty(ulist)) {
			paidamount=ulist.get(0).get("PAID_AMOUNT_OC")==null?"":ulist.get(0).get("PAID_AMOUNT_OC").toString();
		}
		return paidamount;
	}

	@Override
	public String selectRevSumPaidAmt(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnClaimUpdation> subRoot = sq.from(TtrnClaimUpdation.class);

		sq.select(cb.<Integer>selectCase().when(cb.isNull(cb.max(subRoot.get("slNo"))), 1)
				.otherwise(cb.max(subRoot.get("slNo"))))
				.where( cb.equal(subRoot.get("claimNo"), root.get("claimNo")),
						cb.equal(subRoot.get("contractNo"), root.get("contractNo")));
		
		cq.multiselect(cb.selectCase().when(cb.isNull(cb.sum(root.get("paidAmountOc"))), 0.0)
				.otherwise(cb.sum(root.get("paidAmountOc")))
				.as(String.class).alias("PAID_AMOUNT_OC"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("reserveId"), sq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String selectLossEstimateRevisedOc(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnClaimUpdation> subRoot = sq.from(TtrnClaimUpdation.class);

		sq.select(cb.max(subRoot.get("slNo")))
				.where( cb.equal(subRoot.get("claimNo"), claimNo),
						cb.equal(subRoot.get("contractNo"), contractNo));
		
		cq.multiselect(root.get("lossEstimateRevisedOc")
				.as(String.class).alias("LOSS_ESTIMATE_REVISED_OC"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("slNo"), sq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectGetClaimUpdateList(String claimNo, String policyContractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		Subquery<Integer> ocSq = cq.subquery(Integer.class);
		Root<TtrnClaimPayment> subRoot = ocSq.from(TtrnClaimPayment.class);

		ocSq.select(cb.sum(subRoot.get("paidAmountOc")))
				.where(cb.equal(subRoot.get("reserveId"), root.get("slNo")),
						cb.equal(subRoot.get("contractNo"), root.get("contractNo")),
						cb.equal(subRoot.get("claimNo"), root.get("claimNo")));
		
		
		cq.multiselect(root.get("lossEstimateRevisedOc").as(String.class).alias("LOSS_ESTIMATE_REVISED_OC"),
					   root.get("lossEstimateRevisedDc").as(String.class).alias("LOSS_ESTIMATE_REVISED_DC"),
					   root.get("inceptionDate").alias("INCEPTION_DT"),
					   root.get("slNo").alias("SL_NO"),
					   root.get("claimNo").alias("CLAIM_NO"),
					   root.get("contractNo").alias("CONTRACT_NO"),
					   root.get("updateReference").alias("UPDATE_REFERENCE"),
					   cb.selectCase().when(cb.isNull(ocSq), 0.0).otherwise(ocSq).alias("PAIDAMTOC"),
					   cb.selectCase().when(cb.isNull(ocSq), 0.0).otherwise(ocSq).alias("PAIDAMTUSD"));
		
		Subquery<Integer> maxSq = cq.subquery(Integer.class);
		Root<TtrnClaimUpdation> maxSubRoot = maxSq.from(TtrnClaimUpdation.class);

		maxSq.select(cb.max(maxSubRoot.get("slNo")))
				.where( cb.equal(maxSubRoot.get("claimNo"), root.get("claimNo")),
						cb.equal(maxSubRoot.get("contractNo"), root.get("contractNo")));
		
		cq.where(cb.equal(root.get("claimNo"), claimNo),
				 cb.equal(root.get("contractNo"), policyContractNo),
				 cb.equal(root.get("slNo"), maxSq));
		
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public List<Tuple> selectGetClaimReviewQuery(String claimNo, String policyContractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("reviewDate").alias("REVIEW_DT"),
				root.get("reviewDoneBy").alias("REVIEW_DONE_BY"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), policyContractNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectClaimClaimmaster(ClaimListReq req) {
		List<Tuple>list=null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		Root<PositionMaster> pmRoot = cq.from(PositionMaster.class);
		Root<TmasProductMaster> tpmRoot = cq.from(TmasProductMaster.class);
		
		//Company name sub query
		Subquery<String> cnSq = cq.subquery(String.class);
		Root<PersonalInfo> cnSubRoot = cnSq.from(PersonalInfo.class);
		
		Subquery<Integer> aIdSq = cnSq.subquery(Integer.class);
		Root<PersonalInfo> aIdSubRoot = aIdSq.from(PersonalInfo.class);

		aIdSq.select(cb.max(aIdSubRoot.get("amendId")))
				.where( cb.equal(aIdSubRoot.get("customerType"), cnSubRoot.get("customerType")),
						cb.equal(aIdSubRoot.get("customerId"), cnSubRoot.get("customerId")),
						cb.equal(aIdSubRoot.get("branchCode"), pmRoot.get("branchCode")));

		cnSq.select(cnSubRoot.get("companyName"))
				.where( cb.equal(cnSubRoot.get("customerType"), "C"),
						cb.equal(pmRoot.get("cedingCompanyId"), cnSubRoot.get("customerId")),
						cb.equal(cnSubRoot.get("branchCode"), pmRoot.get("branchCode")),
						cb.equal(cnSubRoot.get("amendId"), aIdSq));
		
		//Broker name sub query
		Subquery<String> bnSq = cq.subquery(String.class);
		Root<PersonalInfo> bnSubRoot = bnSq.from(PersonalInfo.class);
		
		Subquery<Integer> aSq = bnSq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
				.where( cb.equal(aSubRoot.get("customerType"), bnSubRoot.get("customerType")),
						cb.equal(aSubRoot.get("customerId"), bnSubRoot.get("customerId")),
						cb.equal(aSubRoot.get("branchCode"), pmRoot.get("branchCode")));

		bnSq.select(bnSubRoot.get("firstName"))
				.where( cb.equal(bnSubRoot.get("customerType"), "B"),
						cb.equal(pmRoot.get("brokerId"), bnSubRoot.get("customerId")),
						cb.equal(bnSubRoot.get("branchCode"), pmRoot.get("branchCode")),
						cb.equal(bnSubRoot.get("amendId"), aSq));
		
		cq.multiselect(tcdRoot.get("claimNo").alias("CLAIM_NO"),
				tcdRoot.get("dateOfLoss").alias("DATE_OF_LOSS"),
				tcdRoot.get("createdDate").alias("CREATED_DATE"),
				tcdRoot.get("statusOfClaim").alias("STATUS_OF_CLAIM"),
				tcdRoot.get("contractNo").alias("CONTRACT_NO"),
				cb.diff(tcdRoot.<BigDecimal>get("totalAmtPaidTillDate"), tcdRoot.<BigDecimal>get("lossEstimateOsOc")).alias("EDITVIEW"),
				pmRoot.get("productId").alias("PRODUCT_ID"),
				tpmRoot.get("tmasProductName").alias("TMAS_PRODUCT_NAME"),
				pmRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				pmRoot.get("expiryDate").alias("EXPIRY_DATE"),
				cnSq.alias("CUSTOMER_NAME"),
				bnSq.alias("BROKER_NAME"),
				pmRoot.get("proposalNo").alias("PROPOSAL_NO"),
				pmRoot.get("layerNo").alias("LAYER_NO"),
				pmRoot.get("deptId").alias("DEPT_ID")).distinct(true);
		
		Subquery<Integer> amSq = cq.subquery(Integer.class);
		Root<PositionMaster> amSubRoot = amSq.from(PositionMaster.class);

		amSq.select(cb.max(amSubRoot.get("amendId")))
				.where( cb.equal(amSubRoot.get("contractNo"), pmRoot.get("contractNo")),
						cb.equal(cb.selectCase().when(cb.isNull(amSubRoot.get("layerNo")), 0).otherwise(amSubRoot.get("layerNo")),
								 cb.selectCase().when(cb.isNull(pmRoot.get("layerNo")), 0).otherwise(pmRoot.get("layerNo"))));
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(tcdRoot.get("contractNo"), pmRoot.get("contractNo")));
		predicates.add(cb.equal(cb.selectCase().when(cb.isNull(tcdRoot.get("layerNo")), 0).otherwise(tcdRoot.get("layerNo")),
				  cb.selectCase().when(cb.isNull(pmRoot.get("layerNo")), 0).otherwise(pmRoot.get("layerNo"))));
		predicates.add( cb.equal(pmRoot.get("branchCode"), req.getBranchCode()));
		predicates.add(cb.equal(tpmRoot.get("branchCode"), pmRoot.get("branchCode")));
		predicates.add(cb.equal(tpmRoot.get("tmasProductId"), pmRoot.get("productId")));
		predicates.add(cb.equal(pmRoot.get("amendId"), amSq));
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				predicates.add(cb.like(cb.upper(cnSq), "%" + req.getCompanyNameSearch().toUpperCase() + "%"));
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				predicates.add(cb.like(cb.upper(bnSq), "%" + req.getBrokerNameSearch().toUpperCase() + "%"));
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				predicates.add(cb.like(tcdRoot.get("contractNo"), "%" + req.getContractNoSearch() + "%"));
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				predicates.add(cb.like(tcdRoot.get("claimNo"), "%" + req.getClaimNoSearch() + "%"));
			}
			if(StringUtils.isNotBlank(req.getDateOfLossSearch())){
				predicates.add(cb.like(tcdRoot.get("dateOfLoss"), "%" + req.getDateOfLossSearch() + "%"));
			}
			if(StringUtils.isNotBlank(req.getClaimStatusSearch())){
				predicates.add(cb.like(cb.upper(tcdRoot.get("statusOfClaim")), "%" + req.getClaimStatusSearch() + "%"));
			}
		}
		
		cq.where(predicates.toArray(new Predicate[0]));
		
		cq.orderBy(cb.desc(tcdRoot.get("claimNo")));
		
		TypedQuery<Tuple> result = em.createQuery(cq);
		if(req.getFlag().equalsIgnoreCase("claim")){
			result.setFirstResult(0 * 100);
			result.setMaxResults(100);
		}
		list = result.getResultList();
		return list;
	}

	@Override
	public List<Tuple> selectDate(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("claimNo").alias("CLAIM_NO"),
				root.get("dateOfLoss").alias("DATE_OF_LOSS"),
				root.get("createdDate").alias("CREATED_DATE"),
				root.get("layerNo").alias("LAYER_NO"),
				root.get("statusOfClaim").alias("STATUS_OF_CLAIM"),
				root.get("contractNo").alias("CONTRACT_NO"),
				cb.diff(root.<BigDecimal>get("totalAmtPaidTillDate"), root.<BigDecimal>get("lossEstimateOsOc")).alias("EDITVIEW"));
		
		cq.orderBy(cb.desc(root.get("claimNo")));
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public Integer getCliampaymnetCount(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(cb.count(root).as(Integer.class)).where(cb.equal(root.get("claimNo"), claimNo),
											 cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectGetClaimPaymentList(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(root.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				root.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				root.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				root.get("claimNoteRecomm").alias("CLAIM_NOTE_RECOMM"),
				root.get("paymentReference").alias("PAYMENT_REFERENCE"),
				root.get("adviceTreasury").alias("ADVICE_TREASURY"),
				root.get("inceptionDate").alias("INCEPTION_DATE"),
				root.get("adviceTreasury").alias("PAID_AMOUNT_DC"),
				root.get("paidAmountDc").alias("ADVICE_TREASURY"),
				root.get("lossEstimateRevisedDc").alias("LOSS_ESTIMATE_REVISED_DC"),
				root.get("reserveId").alias("RESERVE_ID"),
				root.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo))
		
		.orderBy(cb.desc(root.get("claimPaymentNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectGetClaimReviewList(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimReview> root = cq.from(TtrnClaimReview.class);
		
		cq.multiselect(root.get("sNo").alias("SNO"),
				root.get("reviewDate").alias("REVIEW_DATE"),
				root.get("reviewDoneBy").alias("REVIEW_DONE_BY"),
				root.get("remarks").alias("REMARKS"))
		.where(cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("claimNo"), claimNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectGetClaimList(String claimNo, String contractNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> cdRoot = cq.from(TtrnClaimDetails.class);
		Root<CurrencyMaster> cmRoot = cq.from(CurrencyMaster.class);
		
		Subquery<Integer> aSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> aSubRoot = aSq.from(CurrencyMaster.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
				.where( cb.equal(aSubRoot.get("branchCode"), cmRoot.get("branchCode")),
						cb.equal(aSubRoot.get("currencyId"), cmRoot.get("currencyId")));

		
		cq.multiselect(cdRoot.get("claimNo").alias("CLAIM_NO"), 
				cdRoot.get("dateOfLoss").alias("DATE_OF_LOSS"),
				cdRoot.get("lossEstimateOsOc").alias("LOSS_ESTIMATE_OS_OC"),
				cdRoot.get("currency").alias("CURRENCY"),
				cdRoot.get("createdDate").alias("CREATED_DATE"),
				cmRoot.get("shortName").alias("CURRENCY_NAME"))
		
		.where(cb.equal(cdRoot.get("claimNo"), claimNo),
				cb.equal(cdRoot.get("contractNo"), contractNo),
				cb.equal(cdRoot.get("branchCode"), branchCode),
				cb.equal(cmRoot.get("currencyId"), cdRoot.get("currency")),
				cb.equal(cmRoot.get("amendId"), aSq));
		
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public List<Tuple> selectClaimTableList(String policyContractNo, String layerNo, String departmentId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("claimNo").alias("CLAIM_NO"), 
				root.get("dateOfLoss").alias("DATE_OF_LOSS"),
				root.get("createdDate").alias("CREATED_DATE"),
				root.get("statusOfClaim").alias("STATUS_OF_CLAIM"),
				root.get("contractNo").alias("CONTRACT_NO"),
				cb.diff(root.<BigDecimal>get("totalAmtPaidTillDate"), 
						root.<BigDecimal>get("lossEstimateOsOc")).alias("EDITVIEW"))
		
		.where(cb.equal(root.get("contractNo"), policyContractNo),
			   cb.equal(cb.coalesce(root.get("layerNo"), 0),StringUtils.isBlank(layerNo)?0:layerNo),
			   cb.equal(root.get("subClass"), departmentId))
		
		.orderBy(cb.desc(root.get("claimNo")));
		return em.createQuery(cq).getResultList();
				
	}

	@Override
	public List<Tuple> getClaimPaymentData(ClaimPaymentEditReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("reinstatementType"), "MDP"), "On M&D Premium")
					   .when(cb.equal(root.get("reinstatementType"), "ADP"), "On Adjusted Premium")	
					   .when(cb.equal(root.get("reinstatementType"), "FDP"), "On Final Adjusted Premium")
					   .when(cb.equal(root.get("reinstatementType"), "NA"), "Not Applicable").otherwise(cb.literal(""));
		
		cq.multiselect(root.get("reinspremiumOurshareOc").alias("REINSPREMIUM_OURSHARE_OC"),
				root.get("reinstatementType").alias("REINSTATEMENT_TYPE"),
				exp.alias("REINSTATEMENT_TYPE_NAME"),
				root.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				root.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				root.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				root.get("paidClaimOsOc").alias("PAID_CLAIM_OS_OC"),
				root.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				root.get("paymentReference").alias("PAYMENT_REFERENCE"),
				root.get("inceptionDate").alias("INCEPTION_DATE"),
				root.get("remarks").alias("REMARKS"),
				root.get("paidClaimOsDc").alias("PAID_CLAIM_OS_DC"),
				root.get("safOsOc").alias("SAF_OS_OC"),
				root.get("othFeeOsOc").alias("OTH_FEE_OS_OC"),
				root.get("paymentType").alias("PAYMENT_TYPE"))
		
		.where(cb.equal(root.get("claimNo"), req.getClaimNo()),
				cb.equal(root.get("contractNo"), req.getContractNo()),
				cb.equal(root.get("layerNo"), req.getLayerNo()),
				cb.equal(root.get("claimPaymentNo"), req.getClaimPaymentNo()));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getShortName(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TmasBranchMaster> root = cq.from(TmasBranchMaster.class);
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<CurrencyMaster> subRoot = sq.from(CurrencyMaster.class);
		
		Subquery<Integer> aSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> aSubRoot = aSq.from(CurrencyMaster.class);

		aSq.select(cb.max(
				cb.<Integer>selectCase()
				.when(cb.isNull(aSubRoot.<Integer>get("amendId")), 0)
				.otherwise(aSubRoot.<Integer>get("amendId"))))
				.where( cb.equal(aSubRoot.get("currencyId"), subRoot.get("currencyId")),
						cb.equal(aSubRoot.get("branchCode"), subRoot.get("branchCode")));


		sq.select(subRoot.get("shortName"))
		  .where(cb.equal(subRoot.get("currencyId"), root.get("baseCurrencyId")),
				 cb.equal(subRoot.get("branchCode"), root.get("branchCode")),
				 cb.equal(subRoot.get("amendId"), aSq));
		
		cq.multiselect(sq.alias("COUNTRY_SHORT_NAME"))
		.where(cb.equal(root.get("branchCode"), branchCode),
			   cb.equal(root.get("status"), "Y"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getContractNumber(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
					   root.get("claimClass").alias("CLAIM_CLASS"));
		
		if(contractNo == null)
			cq.where(cb.equal(root.get("claimNo"), claimNo));
		else
			cq.where(cb.equal(root.get("claimNo"), claimNo), 
					 cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getFacProposalNo(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);

		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("contractNo"), root.get("contractNo")));
		
		cq.multiselect(root.get("proposalNo").alias("PROPOSAL_NO"),
				root.get("deptId").alias("DEPT_ID"))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("amendId"), sq));
		
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public List<Tuple> getProProposalNo(String contractNo, String deptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);

		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("contractNo"), root.get("contractNo")),
					   cb.equal(subRoot.get("deptId"), root.get("deptId")));
		
		cq.multiselect(root.get("proposalNo").alias("PROPOSAL_NO"),
				root.get("deptId").alias("DEPT_ID"))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("deptId"), deptId),
				cb.equal(root.get("amendId"), sq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getXolProposalNo(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("contractNo"), root.get("contractNo")),
				cb.equal(subRoot.get("layerNo"), root.get("layerNo")));

		cq.multiselect(root.get("proposalNo").alias("PROPOSAL_NO"), root.get("deptId").alias("DEPT_ID")).where(
				cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("layerNo"),
						cb.selectCase().when(cb.isNull(cb.literal(layerNo)), 0).otherwise(layerNo)),
				cb.equal(root.get("amendId"), sq));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getProductIdList(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TmasProductMaster> root = cq.from(TmasProductMaster.class);
		
		cq.multiselect(root.get("tmasProductId").alias("TMAS_PRODUCT_ID"),
					   root.get("tmasProductName").alias("TMAS_PRODUCT_NAME"))
		
		.where(cb.equal(root.get("branchCode"), branchCode),
			   root.get("tmasProductId").in(Arrays.asList(new Integer[]{1,2,3})));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Map<String, Object>> getRipValues(GetReInsValueReq req, String paymentCoverPlus) {
			List<Map<String,Object>> resultList = new ArrayList<>();
			
			String nativeQuery = "SELECT REINST_TYPE,AMOUNT_PERCENT,MIN_AMOUNT_PERCENT,MIN_TIME_PERCENT,"
					+ "count(*)  OVER(PARTITION By PROPOSAL_NO) COUNT from TTRN_RIP P "
					+ "where P.PROPOSAL_NO\\=? and P.CONTRACT_NO\\=? AND P.LAYER_NO\\=? AND P.BRANCH_CODE\\=? "
					+ "and P.SECTION_TYPE\\='A' AND REINST_NO\\=? AND P.AMEND_ID\\=(SELECT MAX(AMEND_ID) "
					+ "FROM  TTRN_RIP T WHERE T.PROPOSAL_NO\\=P.PROPOSAL_NO and T.CONTRACT_NO\\=P.CONTRACT_NO"
					+ " AND T.LAYER_NO\\=P.LAYER_NO AND T.BRANCH_CODE\\=P.BRANCH_CODE)"; 
			
		    Query query = em.createNativeQuery(nativeQuery);
		    query.setParameter(1,req.getProposalNo());
		    query.setParameter(2,req.getPolicyContractNo());
		    query.setParameter(3,req.getLayerNo());
		    query.setParameter(4,req.getBranchCode());
		    query.setParameter(5,paymentCoverPlus);

		    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		    resultList =  query.getResultList();
			return resultList;
		
	}

	@Override
	public String selectExcRate(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("exchangeRate").as(String.class).alias("EXCHANGE_RATE"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String selectMaxSnoDTB3(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		Expression<Integer> exp = cb.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("slNo"))),0)
		.otherwise(cb.max(root.<Integer>get("slNo"))), 1);
		
		cq.multiselect(exp.as(String.class).alias("SL_NO"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Integer updateCloseClaim(String status, java.util.Date date, String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimDetails> update = cb.createCriteriaUpdate(TtrnClaimDetails.class);
		Root<TtrnClaimDetails> root = update.from(TtrnClaimDetails.class);

		update.set(root.get("statusOfClaim"), status)
			  .set(root.get("claimClosedDate"), date);
				
		update.where(cb.equal(root.get("claimNo"), claimNo),
				     cb.equal(root.get("contractNo"), contractNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Long selectPaymentReqNo(String claimNo, String paymentRequestNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(cb.count(root)).where(cb.equal(root.get("claimNo"), claimNo),
											 cb.equal(root.get("paymentRequestNo"), paymentRequestNo));
		
		return em.createQuery(cq).getSingleResult();
	}
	@Transactional
	@Override
	public Integer claimUpdatePayment(InsertCliamDetailsMode3Req req) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
		boolean condition = "3".equalsIgnoreCase(req.getProductId());
		
		java.util.Date date = sdf.parse(req.getDate());
		update.set(root.get("inceptionDate"), date)
		.set(root.get("paymentReference"), req.getPaymentReference())
		.set(root.get("paymentRequestNo"), req.getPaymentRequestNo())
		.set(root.get("paidClaimOsOc"), formatBigDecimal(req.getPaidClaimOs()))
		.set(root.get("paidClaimOsDc"), formatBigDecimal(GetDesginationCountry(req.getPaidClaimOs(),req.getExcRate())))
		.set(root.get("safOsOc"), formatBigDecimal(req.getSurveyorfeeos()))
		.set(root.get("safOsDc"), formatBigDecimal(GetDesginationCountry(req.getSurveyorfeeos(),req.getExcRate())))
		.set(root.get("othFeeOsOc"), formatBigDecimal(req.getOtherproffeeos()))
		.set(root.get("othFeeOsDc"), formatBigDecimal(GetDesginationCountry(req.getOtherproffeeos(),req.getExcRate())))
		.set(root.get("branchCode"), req.getBranchCode())
		.set(root.get("loginId"), req.getLoginId())
		.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("paidAmountOc"), formatBigDecimal(req.getPaidAmountOrigcurr()))
		.set(root.get("paidAmountDc"), formatBigDecimal(GetDesginationCountry(req.getPaidAmountOrigcurr(),req.getExcRate())))
		.set(root.get("remarks"), req.getRemarks())
		
		.set(root.get("reinstatementType"), condition ? req.getReinstType() : "")
		.set(root.get("reinspremiumOurshareOc"),  formatBigDecimal(condition ? req.getReinstPremiumOCOS() : ""))
		.set(root.get("reinspremiumOurshareDc"), formatBigDecimal(condition ? GetDesginationCountry(req.getReinstPremiumOCOS(),req.getExcRate()) : ""))
		.set(root.get("paymentType"), req.getPaymentType());
				
		update.where(cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
				     cb.equal(root.get("claimNo"), req.getClaimNo()),
				     cb.equal(root.get("layerNo"), req.getLayerNo()),
				     cb.equal(root.get("claimPaymentNo"), req.getClaimPaymentNo()));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public Integer updateTotalAmtPaidTillDate(String amt, String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimDetails> update = cb.createCriteriaUpdate(TtrnClaimDetails.class);
		Root<TtrnClaimDetails> root = update.from(TtrnClaimDetails.class);

		update.set(root.get("totalAmtPaidTillDate"), formatBigDecimal(amt));
				
		update.where(cb.equal(root.get("claimNo"), claimNo),
				     cb.equal(root.get("contractNo"), contractNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public String selectRiRecovery(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("riRecovery")).where(cb.equal(root.get("claimNo"), claimNo),
											 cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public void getPremiumSpRetroSplit(InsertCliamDetailsMode3Req req) {
		
		try {
			StoredProcedureQuery sp = em.createStoredProcedureQuery("RetroPremium_Split_claim");
			
			// Assign parameters
			sp.registerStoredProcedureParameter("pvContractNo", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnLayerNo", Integer.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnProductId", Integer.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnPremiumTranNo", Double.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pdPremTranDate", Date.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnCurrencyId", Integer.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnExchange", Double.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnBranchCode", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pvtransactionType", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pdAmendDate", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnReference", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnTreatyName", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnRemarks", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnUwYear", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnSubClass", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("retroCession", String.class, ParameterMode.IN);
			
			// Set parameters
			sp.setParameter("pvContractNo", req.getPolicyContractNo());
			sp.setParameter("pnLayerNo", Integer.parseInt(StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo()));
			sp.setParameter("pnProductId", Integer.parseInt(req.getProductId()));
			sp.setParameter("pnPremiumTranNo", Double.parseDouble(req.getClaimPaymentNo()));
			sp.setParameter("pdPremTranDate", formatDate(req.getDate()));
			sp.setParameter("pnCurrencyId", Integer.parseInt(req.getCurrecny()));
			sp.setParameter("pnExchange", Double.parseDouble(req.getExcRate()));
			sp.setParameter("pnBranchCode", req.getBranchCode());
			sp.setParameter("pvtransactionType", "C");
			sp.setParameter("pdAmendDate", "");
			sp.setParameter("pnReference", "");
			sp.setParameter("pnTreatyName", "");
			sp.setParameter("pnRemarks", "");
			sp.setParameter("pnUwYear", "");
			sp.setParameter("pnSubClass", "");
			sp.setParameter("retroCession", req.getRiRecovery());
			// execute SP
			sp.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String GetDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String valu="0";
		if (StringUtils.isNotBlank(limitOrigCur)&& Double.parseDouble(limitOrigCur) != 0) {
			double originalCountry = Double.parseDouble(limitOrigCur)/ Double.parseDouble(ExchangeRate);
			DecimalFormat myFormatter = new DecimalFormat("###0.00");
			final double dround = Math.round(originalCountry * 100.0) / 100.0;
			valu = myFormatter.format(dround);
		}
		return valu;
	}

	@Override
	public String selectMaxResvId(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		Expression<Integer> exp = cb.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("slNo"))),0)
		.otherwise(cb.max(root.<Integer>get("slNo"))), 1);
		
		cq.multiselect(exp.as(String.class).alias("SL_NO"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}
	@Override
	public String selectMaxReservevId(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		Expression<Integer> exp = cb.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("slNo"))),0)
		.otherwise(cb.max(root.<Integer>get("slNo"))), 0);
		
		cq.multiselect(exp.as(String.class).alias("SL_NO"))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
	}
	@Override
	public List<TtrnClaimPayment> clainArchInsert(InsertCliamDetailsMode3Req req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnClaimPayment> cq = cb.createQuery(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(root).where( cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
									cb.equal(root.get("claimNo"), req.getClaimNo()),
									cb.equal(root.get("layerNo"), req.getLayerNo()),
									cb.equal(root.get("claimPaymentNo"), req.getClaimPaymentNo()));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String selectMaxIdArchive(InsertCliamDetailsMode3Req req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimPaymentArchive> root = cq.from(TtrnClaimPaymentArchive.class);
		
		Expression<Integer> exp = cb.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("slNo"))),0)
		.otherwise(cb.max(root.<Integer>get("slNo"))), 1);
		
		cq.multiselect(exp.as(String.class).alias("SL_NO"))
		.where( cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
				cb.equal(root.get("claimNo"), req.getClaimNo()),
				cb.equal(root.get("layerNo"), req.getLayerNo()),
				cb.equal(root.get("claimPaymentNo"), req.getClaimPaymentNo()));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectClaimEdit(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<CurrencyMaster> subRoot = sq.from(CurrencyMaster.class);
		
		Subquery<Integer> aSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> aSubRoot = aSq.from(CurrencyMaster.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
				.where( cb.equal(aSubRoot.get("currencyId"), subRoot.get("currencyId")),
						cb.equal(aSubRoot.get("branchCode"), subRoot.get("branchCode")),
						cb.equal(aSubRoot.get("status"), "Y"));

		sq.select(subRoot.get("shortName"))
		  .where(cb.equal(subRoot.get("currencyId"), root.get("currency")),
				 cb.equal(subRoot.get("branchCode"), root.get("branchCode")),
				 cb.equal(subRoot.get("amendId"), aSq));
		
		cq.multiselect(root.get("claimNo").alias("CLAIM_NO"), 
					   root.get("statusOfClaim").alias("STATUS_OF_CLAIM"), 
					   root.get("insuredName").alias("INSURED_NAME"),
					   root.get("dateOfLoss").alias("LOSS_DATE"), 
					   root.get("resPosDate").alias("RES_POS_DATE"), 
					   root.get("reportDate").alias("REP_DATE"), 
					   root.get("lossDetails").alias("LOSS_DETAILS"),
					   root.get("causeOfLoss").alias("CAUSE_OF_LOSS"),
					   root.get("location").alias("LOCATION"),
					   root.get("lossEstimateOc").alias("LOSS_ESTIMATE_OC"),
					   root.get("lossEstimateOsOc").alias("LOSS_ESTIMATE_OS_OC"),
					   root.get("exchangeRate").alias("EXCHANGE_RATE"),
					   root.get("lossEstimateOsDc").alias("LOSS_ESTIMATE_OS_DC"),
					   root.get("adviceUw").alias("ADVICE_UW"),
					   root.get("adviceManagement").alias("ADVICE_MANAGEMENT"),
					   root.get("riRecovery").alias("RI_RECOVERY"),
					   root.get("riRecoveryAmountDc").alias("RI_RECOVERY_AMOUNT_DC"),
					   root.get("recoveryFrom").alias("RECOVERY_FROM"),
					   root.get("createdBy").alias("CREATED_BY"),
					   root.get("createdDate").alias("CREATED_DT"),
					   root.get("modifiedBy").alias("MODIFIED_BY"),
					   root.get("modifiedDate").alias("MODIFIED_DT"),
					   root.get("updatedBy").alias("UPDATED_BY"),
					   root.get("updatedDate").alias("UPDATED_DT"),
					   root.get("reopenedDate").alias("REOPENED_DATE"),
					   root.get("currency").alias("CURRENCY"),
					   sq.alias("CURRENCY_NAME"),
					   root.get("remarks").alias("REMARKS"),
					   root.get("adviceUwEmailid").alias("ADVICE_UW_EMAILID"),
					   root.get("adviceMgtEmailid").alias("ADVICE_MGT_EMAILID"),
					   root.get("riskCode").alias("RISK_CODE"),
					   root.get("accumulationCode").alias("ACCUMULATION_CODE"),
					   root.get("eventCode").alias("EVENT_CODE"),
					   root.get("layerNo").alias("LAYER_NO"),
					   root.get("recordFeesCreReserve").alias("RECORD_FEES_CRE_RESERVE"),
					   root.get("saf100Oc").alias("SAF_100_OC"),
					   root.get("saf100Dc").alias("SAF_100_DC"),
					   root.get("othFee100Oc").alias("OTH_FEE_100_OC"),
					   root.get("othFee100Dc").alias("OTH_FEE_100_DC"),
					   root.get("cIbnr100Oc").alias("C_IBNR_100_OC"),
					   root.get("cIbnr100Dc").alias("C_IBNR_100_DC"),
					   root.get("recordIbnr").alias("RECORD_IBNR"),
					   root.get("cedentClaimNo").alias("CEDENT_CLAIM_NO"),
					   root.get("safOsDc").alias("SAF_OS_DC"),
					   root.get("othFeeOsDc").alias("OTH_FEE_OS_DC"),
					   root.get("cIbnrOsDc").alias("C_IBNR_OS_DC"),
					   root.get("safOsOc").alias("SAF_OS_OC"),
					   root.get("othFeeOsOc").alias("OTH_FEE_OS_OC"),
					   root.get("cIbnrOsOc").alias("C_IBNR_OS_OC"),
					   root.get("grosslossFguOc").alias("GROSSLOSS_FGU_OC"),
					   root.get("claimClass").alias("CLAIM_CLASS"),
					   root.get("claimSubclass").alias("CLAIM_SUBCLASS"),
					   root.get("repudateDate").alias("REPUDATE_DATE"),
					   root.get("coverLimitDeptid").alias("COVER_LIMIT_DEPTID"))
		.where( cb.equal(root.get("claimNo"), claimNo),
				cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getEditAccData(String claimNo, String policyContractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimAcc> root = cq.from(TtrnClaimAcc.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnClaimAcc> subRoot = sq.from(TtrnClaimAcc.class);

		sq.select(cb.max(subRoot.get("amendId")))
				.where( cb.equal(subRoot.get("claimNo"), root.get("claimNo")),
						cb.equal(subRoot.get("contractNo"), root.get("contractNo")));

		cq.multiselect( root.get("riskCode").alias("RISK_CODE"),
						root.get("aggregateCode").alias("AGGREGATE_CODE"),
						root.get("eventCode").alias("EVENT_CODE"))
		
		.where( cb.equal(root.get("claimNo"), claimNo),
				cb.equal(root.get("contractNo"), policyContractNo),
				cb.equal(root.get("amendId"), sq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Map<String, Object>> selectGetClaimReserveList(String claimNo, String contractNo) {
	
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		String nativeQuery = "SELECT  TC.LOSS_ESTIMATE_REVISED_OC, TC.LOSS_ESTIMATE_REVISED_DC,"
				+ " TC.UPDATE_REFERENCE, TO_CHAR (TC.INCEPTION_DATE, 'DD/MM/YYYY') AS INCEPTION_DT,"
				+ "  TC.SL_NO, TC.CLAIM_NO,TC.CONTRACT_NO,TC.C_IBNR_100_OC,TC.C_IBNR_OS_OC,"
				+ " NVL ((SELECT SUM (REINSPREMIUM_OURSHARE_OC) FROM TTRN_CLAIM_PAYMENT WHERE "
				+ "RESERVE_ID = TC.SL_NO AND CONTRACT_NO = TC.CONTRACT_NO AND CLAIM_NO = TC.CLAIM_NO),'0') "
				+ "REINSPREMIUM_OURSHARE_OC,NVL((SELECT SUM(PAID_AMOUNT_OC) FROM TTRN_CLAIM_PAYMENT WHERE"
				+ " RESERVE_ID=TC.SL_NO AND CONTRACT_NO =TC.CONTRACT_NO AND CLAIM_NO = TC.CLAIM_NO ),'0') "
				+ "PAIDAMTOC, NVL((SELECT SUM(PAID_AMOUNT_DC) FROM TTRN_CLAIM_PAYMENT WHERE"
				+ " RESERVE_ID=TC.SL_NO AND CONTRACT_NO =TC.CONTRACT_NO AND CLAIM_NO = TC.CLAIM_NO ),'0')"
				+ " PAIDAMTUSD  , NVL(loss_Estimate_Revised_OC,0) loss_Estimate_Revised_OC_2A ,"
				+ " NVL(TP.PAID_CLAIM_OS_OC,0) PAID_CLAIM_OS_OC_2B , NVL(loss_Estimate_Revised_OC,0)-"
				+ "NVL(TP.PAID_CLAIM_OS_OC,0) OC_OS_Amount_2C, NVL(tc.SAF_OS_OC,0) SAF_OS_OC_3A, "
				+ "NVL(TP.SAF_OS_OC,0) SAF_OS_OC_3B, NVL(tc.SAF_OS_OC,0)-NVL(TP.SAF_OS_OC,0) "
				+ "Surveyor_OS_AMT_3C, NVL(tc.OTH_FEE_OS_OC,0) OTH_FEE_OS_OC_4A, NVL(TP.OTH_FEE_OS_OC,0) "
				+ "OTH_FEE_OS_OC_4B, NVL(tc.OTH_FEE_OS_OC,0)-NVL(TP.OTH_FEE_OS_OC,0) Other_Profess_OS_AMT_4C,"
				+ " NVL(loss_Estimate_Revised_OC,0)+NVL(tc.SAF_OS_OC,0)+NVL(tc.OTH_FEE_OS_OC,0) Total_A ,"
				+ " NVL(TP.PAID_CLAIM_OS_OC,0)+NVL(TP.SAF_OS_OC,0) + NVL(TP.OTH_FEE_OS_OC,0) Total_b, "
				+ "(NVL(loss_Estimate_Revised_OC,0)-NVL(TP.PAID_CLAIM_OS_OC,0))+(NVL(tc.SAF_OS_OC,0)-"
				+ "NVL(TP.SAF_OS_OC,0))+(NVL(tc.OTH_FEE_OS_OC,0)-NVL(TP.OTH_FEE_OS_OC,0)) TOTAL_C"
				+ " FROM TTRN_CLAIM_UPDATION tc left outer join (SELECT   reserve_id,NVL (SUM (NVL "
				+ "(PAID_CLAIM_OS_OC, 0)), 0) PAID_CLAIM_OS_OC, NVL (SUM (NVL (SAF_OS_OC, 0)), 0)"
				+ " SAF_OS_OC, NVL (SUM (NVL (OTH_FEE_OS_OC, 0)), 0) OTH_FEE_OS_OC FROM   ttrn_claim_payment "
				+ "Tcp WHERE   CONTRACT_NO = ? AND CLAIM_NO = ? group by reserve_id) TP on ( tp.reserve_id="
				+ "tc.SL_NO)  WHERE   TC.CONTRACT_NO = ? AND TC.CLAIM_NO = ? order by SL_NO"; 
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,contractNo);
	    query.setParameter(2,claimNo);
	    query.setParameter(3,contractNo);
	    query.setParameter(4,claimNo);

	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
	}

	@Override
	public String selectCurrecyName(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TmasBranchMaster> root = cq.from(TmasBranchMaster.class);
		
		cq.multiselect(root.get("countryShortName").alias("COUNTRY_SHORT_NAME"))
		.where( cb.equal(root.get("branchCode"), branchCode));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Map<String, Object>> selectGetClaimReserveListModeFour(String claimNo, String contractNo) {
		
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		String nativeQuery = "SELECT SL_NO,PAID_AMOUNT_OC,PAYMENT_REQUEST_NO,LOSS_ESTIMATE_REVISED_OC ,"
				+ "CLAIM_NOTE_RECOMM,PAYMENT_REFERENCE,ADVICE_TREASURY,TO_CHAR(p.INCEPTION_DATE,'DD/MM/YYYY') "
				+ "AS INCEPTION_DT,PAID_AMOUNT_DC, LOSS_ESTIMATE_REVISED_DC,CLAIM_PAYMENT_NO,RESERVE_ID,"
				+ "SETTLEMENT_STATUS,t.RECEIPT_NO TRANSACTION_NO,Decode(r.TRANS_TYPE,'PT','PAYMENT','RT',"
				+ "'RECEIPT','ADJUSTMENT') TRANS_TYPE FROM TTRN_CLAIM_PAYMENT p left join"
				+ " TTRN_ALLOCATED_TRANSACTION t on t.CONTRACT_NO \\= p.CONTRACT_NO AND t.PAID_AMOUNT"
				+ " \\= p.PAID_AMOUNT_OC AND t.TYPE \\= 'C' AND T.TRANSACTION_NO\\=P.CLAIM_PAYMENT_NO"
				+ " left join TTRN_PAYMENT_RECEIPT r on t.RECEIPT_NO \\= r.PAYMENT_RECEIPT_NO WHERE "
				+ "p.CLAIM_NO\\=? AND p.CONTRACT_NO\\=?   and  decode(r.AMEND_ID,'',1,r.AMEND_ID) \\= "
				+ "(case when r.AMEND_ID is not null then  (select max(AMEND_ID) from  TTRN_PAYMENT_RECEIPT "
				+ "pr where pr.PAYMENT_RECEIPT_NO\\= r.PAYMENT_RECEIPT_NO)  else 1 end) ORDER BY "
				+ "CLAIM_PAYMENT_NO DESC"; 
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,claimNo);
	    query.setParameter(2,contractNo);
	    //query.setParameter(3,contractNo);
	    //query.setParameter(4,claimNo);

	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
	}
	@Override
	public List<Map<String, Object>> coverSumInsuredVal(GetReInsValueReq req) {
			
			List<Map<String,Object>> resultList = new ArrayList<>();
			
			String nativeQuery = "WITH E1 AS (SELECT NVL(RSK_SHARE_SIGNED,0) RSK_SHARE_SIGNED FROM "
					+ "TTRN_RISK_PROPOSAL R WHERE RSK_PROPOSAL_NUMBER\\=? AND RSK_LAYER_NO\\=? AND"
					+ " BRANCH_CODE\\=?  AND RSK_ENDORSEMENT_NO\\=(SELECT MAX(RSK_ENDORSEMENT_NO) FROM"
					+ " TTRN_RISK_PROPOSAL P WHERE  P.RSK_PROPOSAL_NUMBER\\=R.RSK_PROPOSAL_NUMBER  AND"
					+ " P.RSK_LAYER_NO\\=R.RSK_LAYER_NO AND P.BRANCH_CODE\\=R.BRANCH_CODE)),E2 AS(SELECT "
					+ "SUM(NVL(RSK_COVER_LIMT,0)) COVER_LIMIT  FROM TTRN_RSK_CLASS_LIMITS RC WHERE "
					+ " RC.RSK_PROPOSAL_NUMBER\\=?  AND RC.RSK_LAYER_NO\\=? AND RC.RSK_CONTRACT_NO\\=? AND "
					+ "RC.BRANCH_CODE\\=? AND RC.RSK_ENDORSEMENT_NO\\=(SELECT MAX(RSK_ENDORSEMENT_NO) FROM  "
					+ "TTRN_RSK_CLASS_LIMITS L WHERE L.RSK_PROPOSAL_NUMBER\\=RC.RSK_PROPOSAL_NUMBER))SELECT"
					+ " (COVER_LIMIT*RSK_SHARE_SIGNED)/100 SUM_INSURED_VAL FROM E1,E2"; 
			
		    Query query = em.createNativeQuery(nativeQuery);
		    query.setParameter(1,req.getProposalNo());
		    query.setParameter(2,req.getLayerNo());
		    query.setParameter(3,req.getBranchCode());
		    query.setParameter(4,req.getProposalNo());
		    query.setParameter(5,req.getLayerNo());
		    query.setParameter(6,req.getPolicyContractNo());
		    query.setParameter(7,req.getBranchCode());

		    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		    resultList =  query.getResultList();
			return resultList;
	}

	@Override
	public String getDateOfLoss(GetReInsValueReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("dateOfLoss").as(String.class).alias("DATE_OF_LOSS"))
		.where( cb.equal(root.get("claimNo"), req.getClaimNo()),
				cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
				cb.equal(root.get("layerNo"), req.getLayerNo()),
				cb.equal(root.get("branchCode"), req.getBranchCode()));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getRdsDate(GetReInsValueReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> root = cq.from(TtrnRiskDetails.class);
		
		cq.multiselect(root.get("rskInceptionDate").as(String.class).alias("RSK_INCEPTION_DATE"), 
					   root.get("rskExpiryDate").as(String.class).alias("RSK_EXPIRY_DATE"))
		.where( cb.equal(root.get("claimNo"), req.getClaimNo()),
				cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
				cb.equal(root.get("layerNo"), req.getLayerNo()),
				cb.equal(root.get("branchCode"), req.getBranchCode()));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectFacGetCliamQuery(String proposalNo, String productId, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try { // select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME, pending
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> rk = query1.from(TtrnRiskDetails.class);
			Root<PersonalInfo> personal = query1.from(PersonalInfo.class);
			Root<TtrnFacRiskProposal> rp = query1.from(TtrnFacRiskProposal.class);
			Root<PersonalInfo> pi = query1.from(PersonalInfo.class);
		
			Expression<String> e0 = cb.concat(pi.get("firstName"), " ");
			
			query1.multiselect(rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
					rk.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),
					rk.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					personal.get("companyName").alias("CEDING_COMPANY"),
					rk.get("rskCedingid").alias("RSK_CEDINGID"),
					rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
					rp.get("shareSigned").alias("SHARE_SIGNED"),
					rp.get("sumInsuredOurShareDc").alias("SUM_INSURED_OUR_SHARE_DC"),
					rp.get("sumInsuredOurShareOc").alias("SUM_INSURED_OUR_SHARE_OC"),
					rp.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),
					rk.get("rskInceptionDate").alias("INCP_DATE"),
					rk.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
					rk.get("rskExpiryDate").alias("EXP_DATE"),
					rk.get("rskTreatyid").alias("RSK_TREATYID"),
					cb.concat(e0, pi.get("lastName")).alias("BROKER_NAME"),
					rk.get("rskBrokerid").alias("RSK_BROKERID"),
					rk.get("rskInsuredName").alias("RSK_INSURED_NAME"),
					rk.get("rskSpfcid").alias("RSK_SPFCID"),
					rk.get("rskDeptid").alias("RSK_DEPTID")					); 
			//amend
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PersonalInfo> pis = amend.from(PersonalInfo.class);
			amend.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal( pis.get("customerId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( pis.get("customerType"), personal.get("customerType"));
			amend.where(b1,b2,b3);
			
			//amend
			Subquery<Long> amendPi = query1.subquery(Long.class); 
			Root<PersonalInfo> pi1 = amendPi.from(PersonalInfo.class);
			amendPi.select(cb.max(pi1.get("amendId")));
			Predicate c1 = cb.equal( pi1.get("customerId"), pi.get("customerId"));
			Predicate c2 = cb.equal( pi1.get("branchCode"), pi.get("branchCode"));
			Predicate c3 = cb.equal( pi1.get("customerType"), pi.get("customerType"));
			amendPi.where(c1,c2,c3);
			
			//end
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rks.get("rskEndorsementNo")));
			Predicate d1 = cb.equal( rks.get("rskProposalNumber"), proposalNo);
			end.where(d1);
			
			//endRp
			Subquery<Long> endRp = query1.subquery(Long.class); 
			Root<TtrnFacRiskProposal> rps = endRp.from(TtrnFacRiskProposal.class);
			endRp.select(cb.max(rps.get("rskEndorsementNo")));
			Predicate e1 = cb.equal( rps.get("rskProposalNumber"),  rk.get("rskProposalNumber"));
			endRp.where(e1);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rk.get("rskProposalNumber"),proposalNo));
			predicates.add(cb.equal(rk.get("rskProductid"),productId));
			predicates.add(cb.equal(rk.get("rskCedingid"),personal.get("customerId")));
			predicates.add(cb.equal(personal.get("branchCode"),branchCode));
			predicates.add(cb.equal(personal.get("customerType"),"C"));
			predicates.add(cb.equal(personal.get("amendId"),amend));
			predicates.add(cb.equal(rk.get("rskBrokerid"),pi.get("customerId")));
			predicates.add(cb.equal(pi.get("branchCode"),branchCode));
			predicates.add(cb.equal(pi.get("customerType"),"B"));
			predicates.add(cb.equal(pi.get("amendId"),amendPi));
			predicates.add(cb.equal(rp.get("rskProposalNumber"),rk.get("rskProposalNumber")));
			predicates.add(cb.equal(personal.get("customerId"),rk.get("rskCedingid")));
			predicates.add(cb.equal(rk.get("rskEndorsementNo"),end));
			predicates.add(cb.equal(rp.get("rskEndorsementNo"),endRp));
			
			query1.where(predicates.toArray(new Predicate[0]));
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> selectXolOrTeatyGetClimeQuery(String proposalNo, String productId,
			String branchCode) { //// select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME, pending
		List<Tuple> list = new ArrayList<>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> rk = query1.from(TtrnRiskDetails.class);
			Root<PersonalInfo> personal = query1.from(PersonalInfo.class);
			Root<TtrnRiskProposal> rp = query1.from(TtrnRiskProposal.class);
			Root<PersonalInfo> pi = query1.from(PersonalInfo.class);
			Root<TtrnRiskCommission> rc = query1.from(TtrnRiskCommission.class);
		
			//deptName
			Subquery<String> deptName = query1.subquery(String.class); 
			Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(coms.get("tmasDepartmentName"));
			Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rk.get("rskDeptid"));
			Predicate a2 = cb.equal( coms.get("tmasProductId"), rk.get("rskProductid"));
			Predicate a3 = cb.equal( coms.get("branchCode"), personal.get("branchCode"));
			Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
			deptName.where(a1,a2,a3,a4);
			
			Expression<String> e0 = cb.concat(pi.get("firstName"), " ");
			
			query1.multiselect(rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
					rk.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					personal.get("companyName").alias("CEDING_COMPANY"),
					rk.get("rskCedingid").alias("RSK_CEDINGID"),
					rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
					rp.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
					rp.get("rskLimitOc").alias("RSK_LIMIT_OC"),
					rp.get("rskLimitDc").alias("RSK_LIMIT_DC"),
					rp.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),
					rp.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
					rp.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),
					rp.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),
					rp.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),
					rk.get("rskInceptionDate").alias("INCP_DATE"),
					rk.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
					rk.get("rskExpiryDate").alias("EXP_DATE"),
					rk.get("rskTreatyid").alias("RSK_TREATYID"),
					rp.get("rskPfCovered").alias("RSK_PF_COVERED"),
					rk.get("rskInsuredName").alias("RSK_INSURED_NAME"),
					rk.get("rskRiskCovered").alias("RSK_RISK_COVERED"),
					cb.concat(e0, pi.get("lastName")).alias("BROKER_NAME"),
					rk.get("rskBrokerid").alias("RSK_BROKERID"),
					rk.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
					rk.get("rskBasis").alias("RSK_BASIS"),
					rc.get("rskCashlossLmtOc").alias("RSK_CASHLOSS_LMT_OC"),
					rc.get("rskCashlossLmtDc").alias("RSK_CASHLOSS_LMT_DC"),
					deptName.alias("TMAS_DEPARTMENT_NAME"),
					rk.get("rskSpfcid").alias("RSK_SPFCID"),
					rk.get("rskDeptid").alias("RSK_DEPTID"),
					rk.get("rskUwyear").alias("RSK_UWYEAR"),
					rc.get("rskReinstatementPremium").alias("RSK_REINSTATEMENT_PREMIUM")
					); 
			//amend
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PersonalInfo> pis = amend.from(PersonalInfo.class);
			amend.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal( pis.get("customerId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( pis.get("customerType"), personal.get("customerType"));
			amend.where(b1,b2,b3);
			
			//amend
			Subquery<Long> amendPi = query1.subquery(Long.class); 
			Root<PersonalInfo> pi1 = amendPi.from(PersonalInfo.class);
			amendPi.select(cb.max(pi1.get("amendId")));
			Predicate c1 = cb.equal( pi1.get("customerId"), pi.get("customerId"));
			Predicate c2 = cb.equal( pi1.get("branchCode"), pi.get("branchCode"));
			Predicate c3 = cb.equal( pi1.get("customerType"), pi.get("customerType"));
			amendPi.where(c1,c2,c3);
			
			//end
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rks.get("rskEndorsementNo")));
			Predicate d1 = cb.equal( rks.get("rskProposalNumber"), proposalNo);
			end.where(d1);
			
			//endRp
			Subquery<Long> endRp = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> rps = endRp.from(TtrnRiskProposal.class);
			endRp.select(cb.max(rps.get("rskEndorsementNo")));
			Predicate e1 = cb.equal( rps.get("rskProposalNumber"),  rk.get("rskProposalNumber"));
			endRp.where(e1);
			
			//endRc
			Subquery<Long> endRc = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> rcs = endRc.from(TtrnRiskCommission.class);
			endRc.select(cb.max(rcs.get("rskEndorsementNo")));
			Predicate f1 = cb.equal( rcs.get("rskProposalNumber"),  rc.get("rskProposalNumber"));
			endRc.where(f1);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(rk.get("rskProposalNumber"),proposalNo));
			predicates.add(cb.equal(rk.get("rskProductid"),productId));
			predicates.add(cb.equal(rk.get("rskCedingid"),personal.get("customerId")));
			predicates.add(cb.equal(personal.get("branchCode"),branchCode));
			predicates.add(cb.equal(personal.get("customerType"),"C"));
			predicates.add(cb.equal(personal.get("amendId"),amend));
			predicates.add(cb.equal(rk.get("rskBrokerid"),pi.get("customerId")));
			predicates.add(cb.equal(pi.get("branchCode"),branchCode));
			predicates.add(cb.equal(pi.get("customerType"),"B"));
			predicates.add(cb.equal(pi.get("amendId"),amendPi));
			predicates.add(cb.equal(rp.get("rskProposalNumber"),rk.get("rskProposalNumber")));
			predicates.add(cb.equal(personal.get("customerId"),rk.get("rskCedingid")));
			predicates.add(cb.equal(rk.get("rskEndorsementNo"),end));
			predicates.add(cb.equal(rp.get("rskEndorsementNo"),endRp));
			predicates.add(cb.equal(rc.get("rskProposalNumber"),rp.get("rskProposalNumber")));
			predicates.add(cb.equal(rc.get("rskEndorsementNo"),endRc));
		
			query1.where(predicates.toArray(new Predicate[0]));
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> partialSelectGetpaymentlist(ClaimPaymentListReq req) {
		List<Tuple> list = new ArrayList<>();
		try {
		
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnClaimPayment> tcd = query.from(TtrnClaimPayment.class);
		Root<PositionMaster> pm = query.from(PositionMaster.class);
		Root<TmasProductMaster> tpm = query.from(TmasProductMaster.class);
		
		// Customer_name 
		Subquery<String> companyName = query.subquery(String.class); 
		Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
		companyName.select(pi.get("companyName"));
		//maxamend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate c1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
		Predicate c2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate c3 = cb.equal( pi.get("customerType"), pis.get("customerType"));
		maxAmend.where(c1,c2,c3);
		Predicate b1 = cb.equal( pm.get("branchCode"), pi.get("branchCode"));
		Predicate b2 = cb.equal( pm.get("cedingCompanyId"), pi.get("customerId"));
		Predicate b3 = cb.equal( pi.get("amendId"), maxAmend);
		Predicate b4 = cb.equal( pi.get("customerType"), "C");
		companyName.where(b1,b2,b3,b4);
		
		// Broker_name
		Subquery<String> brokerName = query.subquery(String.class); 
		Root<PersonalInfo> b = brokerName.from(PersonalInfo.class);
		brokerName.select(b.get("firstName"));
		//maxamend
		Subquery<Long> amendPI = query.subquery(Long.class); 
		Root<PersonalInfo> bs = amendPI.from(PersonalInfo.class);
		amendPI.select(cb.max(bs.get("amendId")));
		Predicate y1 = cb.equal( b.get("customerId"), bs.get("customerId"));
		Predicate y2 = cb.equal( pm.get("branchCode"), bs.get("branchCode"));
		Predicate y3 = cb.equal( b.get("customerType"), bs.get("customerType"));
		amendPI.where(y1,y2,y3);
		Predicate x1 = cb.equal( b.get("branchCode"), pm.get("branchCode"));
		Predicate x2 = cb.equal( b.get("customerId"), pm.get("brokerId"));
		Predicate x3 = cb.equal( b.get("amendId"), amendPI);
		Predicate x4 = cb.equal( b.get("customerType"), "B");
		brokerName.where(x1,x2,x3,x4);

		query.multiselect(tcd.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				tcd.get("claimNo").alias("CLAIM_NO"),
				tcd.get("contractNo").alias("CONTRACT_NO"),
				tcd.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				tcd.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				tcd.get("inceptionDate").alias("INCEPTION_DT"),
				tcd.get("claimNoteRecomm").alias("CLAIM_NOTE_RECOMM"),
				tcd.get("paymentReference").alias("PAYMENT_REFERENCE"),
				tcd.get("adviceTreasury").alias("ADVICE_TREASURY"),
				tcd.get("paidAmountDc").alias("PAID_AMOUNT_DC"),
				tcd.get("lossEstimateRevisedDc").alias("LOSS_ESTIMATE_REVISED_DC"),
				tcd.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				tcd.get("reserveId").alias("RESERVE_ID"),
				tcd.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				pm.get("productId").alias("PRODUCT_ID"),
				tpm.get("tmasProductName").alias("TMAS_PRODUCT_NAME"),
				pm.get("inceptionDate").alias("INCEPTION_DATE"),
				pm.get("expiryDate").alias("EXPIRY_DATE"),
				companyName.alias("CUSTOMER_NAME"),
				brokerName.alias("BROKER_NAME"),
				pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("layerNo").alias("LAYER_NO"),
				pm.get("deptId").alias("DEPT_ID")
				).distinct(true); 

		Subquery<Long> amend = query.subquery(Long.class); 
		Root<PositionMaster> rds = amend.from(PositionMaster.class);
		amend.select(cb.max(rds.get("amendId")));
		Predicate a1 = cb.equal( rds.get("contractNo"), pm.get("contractNo"));
		Predicate a2 = cb.equal(cb.coalesce(rds.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0"));
		amend.where(a1,a2);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(tcd.get("claimNo")));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(tcd.get("layerNo"),pm.get("contractNo")));
		predicates.add(cb.equal(cb.coalesce(tcd.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0")));
		predicates.add(cb.equal(pm.get("branchCode"),req.getBranchCode()));
		predicates.add(cb.equal(tpm.get("branchCode"),pm.get("branchCode")));
		predicates.add(cb.equal(tpm.get("tmasProductId"),pm.get("productId")));
		predicates.add(cb.equal(pm.get("amendId"), amend));
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				predicates.add(cb.like(cb.upper(companyName), "%" +req.getCompanyNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				predicates.add(cb.like(cb.upper(brokerName), "%" +req.getBrokerNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				predicates.add(cb.equal(tcd.get("contractNo"), "%" +req.getContractNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				predicates.add(cb.equal(tcd.get("claimNo"), "%" +req.getClaimNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
				predicates.add(cb.equal(tcd.get("claimPaymentNo"), "%" +req.getPaymentNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
				predicates.add(cb.equal(tcd.get("inceptionDate"), "%" +req.getPaymentDateSearch()+"%"));
			}
		}
	
		query.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Tuple> result = em.createQuery(query);
		//Pagination
		result.setFirstResult(0 * 100);
		result.setMaxResults(100);
		list = result.getResultList();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> selectGetpaymentlist(ClaimPaymentListReq req) {
		List<Tuple> list = new ArrayList<>();
		try {
		
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnClaimPayment> tcd = query.from(TtrnClaimPayment.class);
		Root<PositionMaster> pm = query.from(PositionMaster.class);
		Root<TmasProductMaster> tpm = query.from(TmasProductMaster.class);
		
		// Customer_name 
		Subquery<String> companyName = query.subquery(String.class); 
		Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
		companyName.select(pi.get("companyName"));
		//maxamend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate c1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
		Predicate c2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate c3 = cb.equal( pi.get("customerType"), pis.get("customerType"));
		maxAmend.where(c1,c2,c3);
		Predicate b1 = cb.equal( pm.get("branchCode"), pi.get("branchCode"));
		Predicate b2 = cb.equal( pm.get("cedingCompanyId"), pi.get("customerId"));
		Predicate b3 = cb.equal( pi.get("amendId"), maxAmend);
		Predicate b4 = cb.equal( pi.get("customerType"), "C");
		companyName.where(b1,b2,b3,b4);
		
		// Broker_name
		Subquery<String> brokerName = query.subquery(String.class); 
		Root<PersonalInfo> b = brokerName.from(PersonalInfo.class);
		brokerName.select(b.get("firstName"));
		//maxamend
		Subquery<Long> amendPI = query.subquery(Long.class); 
		Root<PersonalInfo> bs = amendPI.from(PersonalInfo.class);
		amendPI.select(cb.max(bs.get("amendId")));
		Predicate y1 = cb.equal( b.get("customerId"), bs.get("customerId"));
		Predicate y2 = cb.equal( pm.get("branchCode"), bs.get("branchCode"));
		Predicate y3 = cb.equal( b.get("customerType"), bs.get("customerType"));
		amendPI.where(y1,y2,y3);
		Predicate x1 = cb.equal( b.get("branchCode"), pm.get("branchCode"));
		Predicate x2 = cb.equal( b.get("customerId"), pm.get("brokerId"));
		Predicate x3 = cb.equal( b.get("amendId"), amendPI);
		Predicate x4 = cb.equal( b.get("customerType"), "B");
		brokerName.where(x1,x2,x3,x4);

		query.multiselect(tcd.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				tcd.get("claimNo").alias("CLAIM_NO"),
				tcd.get("contractNo").alias("CONTRACT_NO"),
				tcd.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				tcd.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				tcd.get("inceptionDate").alias("INCEPTION_DT"),
				tcd.get("claimNoteRecomm").alias("CLAIM_NOTE_RECOMM"),
				tcd.get("paymentReference").alias("PAYMENT_REFERENCE"),
				tcd.get("adviceTreasury").alias("ADVICE_TREASURY"),
				tcd.get("paidAmountDc").alias("PAID_AMOUNT_DC"),
				tcd.get("lossEstimateRevisedDc").alias("LOSS_ESTIMATE_REVISED_DC"),
				tcd.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				tcd.get("reserveId").alias("RESERVE_ID"),
				tcd.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				pm.get("productId").alias("PRODUCT_ID"),
				tpm.get("tmasProductName").alias("TMAS_PRODUCT_NAME"),
				pm.get("inceptionDate").alias("INCEPTION_DATE"),
				pm.get("expiryDate").alias("EXPIRY_DATE"),
				companyName.alias("CUSTOMER_NAME"),
				brokerName.alias("BROKER_NAME"),
				pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("layerNo").alias("LAYER_NO"),
				pm.get("deptId").alias("DEPT_ID")
				).distinct(true); 

		Subquery<Long> amend = query.subquery(Long.class); 
		Root<PositionMaster> rds = amend.from(PositionMaster.class);
		amend.select(cb.max(rds.get("amendId")));
		Predicate a1 = cb.equal( rds.get("contractNo"), pm.get("contractNo"));
		Predicate a2 = cb.equal(cb.coalesce(rds.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0"));
		amend.where(a1,a2);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(tcd.get("claimNo")));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(tcd.get("layerNo"),pm.get("contractNo")));
		predicates.add(cb.equal(cb.coalesce(tcd.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0")));
		predicates.add(cb.equal(pm.get("branchCode"),req.getBranchCode()));
		predicates.add(cb.equal(tpm.get("branchCode"),pm.get("branchCode")));
		predicates.add(cb.equal(tpm.get("tmasProductId"),pm.get("productId")));
		predicates.add(cb.equal(pm.get("amendId"), amend));
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				predicates.add(cb.like(cb.upper(companyName), "%" +req.getCompanyNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				predicates.add(cb.like(cb.upper(brokerName), "%" +req.getBrokerNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				predicates.add(cb.equal(tcd.get("contractNo"), "%" +req.getContractNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				predicates.add(cb.equal(tcd.get("claimNo"), "%" +req.getClaimNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
				predicates.add(cb.equal(tcd.get("claimPaymentNo"), "%" +req.getPaymentNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
				predicates.add(cb.equal(tcd.get("inceptionDate"), "%" +req.getPaymentDateSearch()+"%"));
			}
		}
	
		query.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String selectCountAllocatedYN(String policyContractNo, String claimNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);
		
		cq.multiselect(cb.count(root).as(String.class))
		.where(cb.equal(root.get("contractNo"), policyContractNo),
			   cb.equal(root.get("transactionNo"), claimNo),
			   cb.equal(root.get("layerNo"), layerNo),
			   cb.equal(root.get("status"), "Y"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String getStatusOfClaim(String policyContractNo, String claimNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		cq.multiselect(root.get("statusOfClaim"))
		.where(cb.equal(root.get("contractNo"), policyContractNo),
			   cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("layerNo"), layerNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String selectMaxno(String claimNo, String policyContractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		cq.multiselect(cb.max(root.get("slNo")).as(String.class))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), policyContractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> contractIdentifierList(ContractidetifierlistReq bean) {
		 List<Tuple> list = new ArrayList<>();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
		try {
			//contract.identifier.list
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> a = query.from(PositionMaster.class);
			Root<TmasDepartmentMaster> b = query.from(TmasDepartmentMaster.class);
			Root<PersonalInfo> c = query.from(PersonalInfo.class);
			Root<TtrnRiskDetails> d = query.from(TtrnRiskDetails.class);
			Root<UnderwritterMaster> e = query.from(UnderwritterMaster.class);
			Root<PersonalInfo> c1 = query.from(PersonalInfo.class);
			
			Expression<String> e0 = cb.concat(c1.get("firstName"), " ")	;	

			query.multiselect(a.get("proposalNo").alias("PROPOSAL_NO"),a.get("deptId").alias("DEPT_ID"),
					a.get("contractNo").alias("CONTRACT_NO"),b.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
					a.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),a.get("accountDate").alias("ACCOUNT_DATE"),
					c.get("companyName").alias("COMPANY_NAME"),	cb.concat(e0, c1.get("lastName")).alias("BROKER_NAME"),					
					a.get("inceptionDate").alias("INCEPTION_DATE"),a.get("expiryDate").alias("EXPIRY_DATE"),
					a.get("layerNo").alias("LAYER_NO"),a.get("baseLayer").alias("BASE_LAYER"),a.get("oldContractno").alias("OLD_CONTRACTNO"),
					a.get("renewalStatus").alias("RENEWAL_STATUS"), //cb.diff(a.get("expiryDate"), new Date()).alias("RENWALDATE"),
					cb.selectCase().when(cb.notEqual(a.get("uwYear"),"0"), a.get("uwYear")) .otherwise("").alias("UW_YEAR"),
					a.get("uwMonth").alias("UW_MONTH"),e.get("underwritter").alias("UNDERWRITTER"),a.get("amendId").alias("AMEND_ID"));
					
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfo> pi = amend.from(PersonalInfo.class);
			amend.select(cb.max(pi.get("amendId")));
			Predicate a1 = cb.equal( pi.get("customerId"), c.get("customerId"));
			Predicate a2 = cb.equal( pi.get("branchCode"), c.get("branchCode"));
			Predicate a3 = cb.equal( pi.get("customerType"), c.get("customerType"));
			amend.where(a1,a2,a3);
			//amenId
			Subquery<Long> amenId = query.subquery(Long.class); 
			Root<PositionMaster> pm = amenId.from(PositionMaster.class);
			amenId.select(cb.max(pm.get("amendId")));
			Predicate b1 = cb.equal( pm.get("productId"), bean.getProductId());
			Predicate b2 = cb.equal( pm.get("contractNo"), a.get("contractNo"));
			Predicate b3 = cb.equal( pm.get("layerNo"), a.get("layerNo"));
			Predicate b4 = cb.equal( pm.get("branchCode"), a.get("branchCode"));
			Predicate b5 = cb.equal( pm.get("contractStatus"), "A");
			amenId.where(b1,b2,b3,b4,b5);
			//amendC1
			Subquery<Long> amendC1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis = amendC1.from(PersonalInfo.class);
			amendC1.select(cb.max(pis.get("amendId")));
			Predicate d1 = cb.equal( pis.get("customerId"), c1.get("customerId"));
			Predicate d2 = cb.equal( pis.get("branchCode"), c1.get("branchCode"));
			Predicate d3 = cb.equal( pis.get("customerType"), c1.get("customerType"));
			amendC1.where(d1,d2,d3);
			//end
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rd = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rd.get("rskEndorsementNo")));
			Predicate f1 = cb.equal( rd.get("rskProposalNumber"), d.get("rskProposalNumber"));
			Predicate f2 = cb.equal( rd.get("branchCode"), d.get("branchCode"));
			end.where(f1,f2);
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(a.get("contractNo")));

			Predicate n1 = cb.equal(a.get("deptId"), b.get("tmasDepartmentId"));
			Predicate n2 = cb.equal(c.get("customerId"), a.get("cedingCompanyId"));
			Predicate n3 = cb.equal(a.get("productId"), d.get("rskProductid"));
			Predicate n4 = cb.equal(a.get("contractNo"), d.get("rskContractNo"));
			Predicate n5 = cb.equal(a.get("layerNo"), d.get("rskLayerNo"));
			Predicate n6 = cb.equal(a.get("amendId"), d.get("rskEndorsementNo"));
			Predicate n7 = cb.equal(a.get("deptId"), d.get("rskDeptid"));
			Predicate n8 = cb.equal(d.get("rskUnderwritter"), e.get("uwrCode"));
			Predicate n9 = cb.equal(c1.get("customerId"), a.get("brokerId"));
			Predicate n10 = cb.equal(a.get("productId"), bean.getProductId());
			Predicate n11 = cb.equal(a.get("contractStatus"), "A");
			Predicate n12 = cb.greaterThan(a.get("contractNo"), 0);
			Predicate n13 = cb.equal(b.get("tmasProductId"), bean.getProductId());
			Predicate n14 = cb.equal(b.get("branchCode"), bean.getBranchCode());
			Predicate n15 = cb.equal(b.get("tmasStatus"), "Y");
			Predicate n16 = cb.equal(c.get("branchCode"), bean.getBranchCode());
			Predicate n17 = cb.equal(c.get("customerType"), "C");
			Predicate n18 = cb.equal(c.get("amendId"), amend);
			Predicate n19 = cb.equal(e.get("branchCode"), bean.getBranchCode());
			Predicate n20 = cb.equal(a.get("branchCode"), bean.getBranchCode());
			//in
			Expression<String> e1= a.get("amendId");
			Predicate n21 = e1.in(amenId==null?null:amenId);
			Predicate n22 = cb.equal(c1.get("branchCode"), bean.getBranchCode());
			Predicate n23 = cb.equal(c1.get("amendId"), amendC1);
			Predicate n24 = cb.equal(d.get("rskEndorsementNo"), end);
			Predicate n25 = null;
			if(!"N".equalsIgnoreCase(bean.getCedingCompanyCode())&&!"".equalsIgnoreCase(bean.getCedingCompanyCode())){
				 n25 = cb.equal(d.get("rskCedingid"), bean.getCedingCompanyCode());
				 query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24,n25).orderBy(orderList);
			}
			if(!"N".equalsIgnoreCase(bean.getBrokerCode())&&!"".equalsIgnoreCase(bean.getBrokerCode())){
				 n25 = cb.equal(d.get("rskBrokerId"), bean.getBrokerCode());
				 query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24,n25).orderBy(orderList);
			}
			if(!"N".equalsIgnoreCase(bean.getUnderwritingYear())&&!"".equalsIgnoreCase(bean.getUnderwritingYear())){
				 n25 = cb.equal(d.get("rskUwyear"), bean.getUnderwritingYear());
				 query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24,n25).orderBy(orderList);
			}
			if(!"N".equalsIgnoreCase(bean.getDeptId())&&!"".equalsIgnoreCase(bean.getDeptId())){
				 n25 = cb.equal(d.get("rskDeptid"), bean.getDeptId());
				 query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24,n25).orderBy(orderList);
			}
			query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24).orderBy(orderList);	
		

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
			}
		return list;
	}
	public Date formatDate(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf1.parse(input);
		return new java.sql.Date(date.getTime());
	}

	@Override
	public List<Tuple> getClaimPaymentDataRi(ClaimPaymentEditReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPaymentRi> root = cq.from(TtrnClaimPaymentRi.class);
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("reinstatementType"), "MDP"), "On M&D Premium")
					   .when(cb.equal(root.get("reinstatementType"), "ADP"), "On Adjusted Premium")	
					   .when(cb.equal(root.get("reinstatementType"), "FDP"), "On Final Adjusted Premium")
					   .when(cb.equal(root.get("reinstatementType"), "NA"), "Not Applicable").otherwise(cb.literal(""));
		
		cq.multiselect(root.get("reinspremiumOurshareOc").alias("REINSPREMIUM_OURSHARE_OC"),
				root.get("reinstatementType").alias("REINSTATEMENT_TYPE"),
				exp.alias("REINSTATEMENT_TYPE_NAME"),
				root.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				root.get("riTransactionNo").alias("CLAIM_PAYMENTRI_NO"),
				root.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				root.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				root.get("paidClaimOsOc").alias("PAID_CLAIM_OS_OC"),
				root.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				root.get("paymentReference").alias("PAYMENT_REFERENCE"),
				root.get("inceptionDate").alias("INCEPTION_DATE"),
				root.get("remarks").alias("REMARKS"),
				root.get("paidClaimOsDc").alias("PAID_CLAIM_OS_DC"),
				root.get("safOsOc").alias("SAF_OS_OC"),
				root.get("othFeeOsOc").alias("OTH_FEE_OS_OC"),
				root.get("paymentType").alias("PAYMENT_TYPE"))
		
		.where(cb.equal(root.get("claimNo"), req.getClaimNo()),
				cb.equal(root.get("contractNo"), req.getContractNo()),
				cb.equal(root.get("layerNo"), req.getLayerNo()),
				cb.equal(root.get("riTransactionNo"), req.getClaimPaymentNo()));
		
		return em.createQuery(cq).getResultList();
	}
	@Transactional
	@Override
	public int claimUpdatePaymentRi(InsertCliamDetailsMode3Req req) throws ParseException {
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPaymentRi> update = cb.createCriteriaUpdate(TtrnClaimPaymentRi.class);
		Root<TtrnClaimPaymentRi> root = update.from(TtrnClaimPaymentRi.class);
		
		boolean condition = "3".equalsIgnoreCase(req.getProductId());
		java.util.Date date = sdf.parse(req.getDate());
		update.set(root.get("inceptionDate"), date)
		.set(root.get("paymentReference"), req.getPaymentReference())
		.set(root.get("paymentRequestNo"), req.getPaymentRequestNo())
		.set(root.get("paidClaimOsOc"), formatBigDecimal(req.getPaidClaimOs()))
		.set(root.get("paidClaimOsDc"), formatBigDecimal(GetDesginationCountry(req.getPaidClaimOs(),req.getExcRate())))
		.set(root.get("safOsOc"), formatBigDecimal(req.getSurveyorfeeos()))
		.set(root.get("safOsDc"), formatBigDecimal(GetDesginationCountry(req.getSurveyorfeeos(),req.getExcRate())))
		.set(root.get("othFeeOsOc"), formatBigDecimal(req.getOtherproffeeos()))
		.set(root.get("othFeeOsDc"), formatBigDecimal(GetDesginationCountry(req.getOtherproffeeos(),req.getExcRate())))
		.set(root.get("branchCode"), req.getBranchCode())
		.set(root.get("loginId"), req.getLoginId())
		.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("paidAmountOc"), formatBigDecimal(req.getPaidAmountOrigcurr()))
		.set(root.get("paidAmountDc"), formatBigDecimal(GetDesginationCountry(req.getPaidAmountOrigcurr(),req.getExcRate())))
		.set(root.get("remarks"), req.getRemarks())
		
		.set(root.get("reinstatementType"), condition ? req.getReinstType() : "")
		.set(root.get("reinspremiumOurshareOc"),  formatBigDecimal(condition ? req.getReinstPremiumOCOS() : ""))
		.set(root.get("reinspremiumOurshareDc"), formatBigDecimal(condition ? GetDesginationCountry(req.getReinstPremiumOCOS(),req.getExcRate()) : ""))
		.set(root.get("paymentType"), req.getPaymentType());
				
		update.where(cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
				     cb.equal(root.get("claimNo"), req.getClaimNo()),
				     cb.equal(root.get("layerNo"), req.getLayerNo()),
				     cb.equal(root.get("riTransactionNo"), req.getClaimPaymentNo()));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	public void getPremiumRiSplit(InsertCliamDetailsMode3Req req) {
		StoredProcedureQuery sp = em.createStoredProcedureQuery("RI_SPLIT_INSERT");

		// Assign parameters
		sp.registerStoredProcedureParameter("V_PROPOSAL_NO", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_LAYER_NO", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_PRODUCT_ID", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_TRANSACTION_NO", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_BRANCH_CODE", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_TYPE", String.class, ParameterMode.IN);
		
		// Set parameters
		sp.setParameter("V_PROPOSAL_NO", req.getProposalNo());
		sp.setParameter("V_LAYER_NO", Integer.parseInt(StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo()));
		sp.setParameter("V_PRODUCT_ID", req.getProductId());
		sp.setParameter("V_TRANSACTION_NO", req.getClaimPaymentNo());
		sp.setParameter("V_BRANCH_CODE", req.getBranchCode());
		sp.setParameter("V_TYPE", "C");
		
		System.out.println("V_PROPOSAL_NO: "+sp.getParameterValue("V_PROPOSAL_NO"));
		System.out.println("V_LAYER_NO: "+sp.getParameterValue("V_LAYER_NO"));
		System.out.println("V_PRODUCT_ID: "+sp.getParameterValue("V_PRODUCT_ID"));
		System.out.println("V_TRANSACTION_NO: "+sp.getParameterValue("V_TRANSACTION_NO"));
		System.out.println("V_BRANCH_CODE: "+sp.getParameterValue("V_BRANCH_CODE"));
		sp.execute();
	}
	public BigDecimal formatBigDecimal(String input) {
		if(StringUtils.isBlank(input))
			return new BigDecimal("0.0");
		return new BigDecimal(input);
	}

	@Override
	public List<Tuple> partialSelectGetpaymentRilist(ClaimPaymentListReq req) {
		List<Tuple> list = new ArrayList<>();
		try {
		
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnClaimPayment> tcd = query.from(TtrnClaimPayment.class);
		Root<PositionMaster> pm = query.from(PositionMaster.class);
		Root<TmasProductMaster> tpm = query.from(TmasProductMaster.class);
		
		// Customer_name 
		Subquery<String> companyName = query.subquery(String.class); 
		Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
		companyName.select(pi.get("companyName"));
		//maxamend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate c1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
		Predicate c2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate c3 = cb.equal( pi.get("customerType"), pis.get("customerType"));
		maxAmend.where(c1,c2,c3);
		Predicate b1 = cb.equal( pm.get("branchCode"), pi.get("branchCode"));
		Predicate b2 = cb.equal( pm.get("cedingCompanyId"), pi.get("customerId"));
		Predicate b3 = cb.equal( pi.get("amendId"), maxAmend);
		Predicate b4 = cb.equal( pi.get("customerType"), "C");
		companyName.where(b1,b2,b3,b4);
		
		// Broker_name
		Subquery<String> brokerName = query.subquery(String.class); 
		Root<PersonalInfo> b = brokerName.from(PersonalInfo.class);
		brokerName.select(b.get("firstName"));
		//maxamend
		Subquery<Long> amendPI = query.subquery(Long.class); 
		Root<PersonalInfo> bs = amendPI.from(PersonalInfo.class);
		amendPI.select(cb.max(bs.get("amendId")));
		Predicate y1 = cb.equal( b.get("customerId"), bs.get("customerId"));
		Predicate y2 = cb.equal( pm.get("branchCode"), bs.get("branchCode"));
		Predicate y3 = cb.equal( b.get("customerType"), bs.get("customerType"));
		amendPI.where(y1,y2,y3);
		Predicate x1 = cb.equal( b.get("branchCode"), pm.get("branchCode"));
		Predicate x2 = cb.equal( b.get("customerId"), pm.get("brokerId"));
		Predicate x3 = cb.equal( b.get("amendId"), amendPI);
		Predicate x4 = cb.equal( b.get("customerType"), "B");
		brokerName.where(x1,x2,x3,x4);

		query.multiselect(tcd.get("paidAmountOc").alias("PAID_AMOUNT_OC"),
				tcd.get("claimNo").alias("CLAIM_NO"),
				tcd.get("contractNo").alias("CONTRACT_NO"),
				tcd.get("paymentRequestNo").alias("PAYMENT_REQUEST_NO"),
				tcd.get("lossEstimateRevisedOc").alias("LOSS_ESTIMATE_REVISED_OC"),
				tcd.get("inceptionDate").alias("INCEPTION_DT"),
				tcd.get("claimNoteRecomm").alias("CLAIM_NOTE_RECOMM"),
				tcd.get("paymentReference").alias("PAYMENT_REFERENCE"),
				tcd.get("adviceTreasury").alias("ADVICE_TREASURY"),
				tcd.get("paidAmountDc").alias("PAID_AMOUNT_DC"),
				tcd.get("lossEstimateRevisedDc").alias("LOSS_ESTIMATE_REVISED_DC"),
				tcd.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				tcd.get("reserveId").alias("RESERVE_ID"),
				tcd.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				pm.get("productId").alias("PRODUCT_ID"),
				tpm.get("tmasProductName").alias("TMAS_PRODUCT_NAME"),
				pm.get("inceptionDate").alias("INCEPTION_DATE"),
				pm.get("expiryDate").alias("EXPIRY_DATE"),
				companyName.alias("CUSTOMER_NAME"),
				brokerName.alias("BROKER_NAME"),
				pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("layerNo").alias("LAYER_NO"),
				pm.get("deptId").alias("DEPT_ID")
				).distinct(true); 

		Subquery<Long> amend = query.subquery(Long.class); 
		Root<PositionMaster> rds = amend.from(PositionMaster.class);
		amend.select(cb.max(rds.get("amendId")));
		Predicate a1 = cb.equal( rds.get("contractNo"), pm.get("contractNo"));
		Predicate a2 = cb.equal(cb.coalesce(rds.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0"));
		amend.where(a1,a2);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(tcd.get("claimNo")));
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(tcd.get("layerNo"),pm.get("contractNo")));
		predicates.add(cb.equal(cb.coalesce(tcd.get("layerNo"), "0"),cb.coalesce(pm.get("layerNo"), "0")));
		predicates.add(cb.equal(pm.get("branchCode"),req.getBranchCode()));
		predicates.add(cb.equal(tpm.get("branchCode"),pm.get("branchCode")));
		predicates.add(cb.equal(tpm.get("tmasProductId"),pm.get("productId")));
		predicates.add(cb.equal(pm.get("amendId"), amend));
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				predicates.add(cb.like(cb.upper(companyName), "%" +req.getCompanyNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				predicates.add(cb.like(cb.upper(brokerName), "%" +req.getBrokerNameSearch().toUpperCase()+"%"));
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				predicates.add(cb.equal(tcd.get("contractNo"), "%" +req.getContractNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				predicates.add(cb.equal(tcd.get("claimNo"), "%" +req.getClaimNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
				predicates.add(cb.equal(tcd.get("claimPaymentNo"), "%" +req.getPaymentNoSearch()+"%"));
			}
			if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
				predicates.add(cb.equal(tcd.get("inceptionDate"), "%" +req.getPaymentDateSearch()+"%"));
			}
		}
	
		query.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Tuple> result = em.createQuery(query);
		//Pagination
		result.setFirstResult(0 * 100);
		result.setMaxResults(100);
		list = result.getResultList();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
