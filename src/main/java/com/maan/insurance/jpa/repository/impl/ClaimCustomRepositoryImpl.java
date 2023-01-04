package com.maan.insurance.jpa.repository.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.claim.TtrnClaimAcc;
import com.maan.insurance.jpa.entity.claim.TtrnClaimPaymentArchive;
import com.maan.insurance.jpa.entity.claim.TtrnClaimReview;
import com.maan.insurance.jpa.entity.claim.TtrnClaimUpdation;
import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.repository.claim.ClaimCustomRepository;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.entity.TtrnRiskDetails;
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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(cb.sum(root.get("paidAmountOc")).as(String.class).alias("PAID_AMOUNT_OC"))
		.where(cb.equal(root.get("claimNo"), claimNo),
				cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
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
	public List<Tuple> selectClaimClaimmaster(ClaimListReq req, String searchCriteria) {
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
				pmRoot.get("productId").alias("Product_id"),
				tpmRoot.get("tmasProductName").alias("TMAS_PRODUCT_NAME"),
				pmRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				pmRoot.get("expiryDate").alias("Expiry_date"),
				cnSq.alias("Customer_name"),
				bnSq.alias("Broker_name"),
				pmRoot.get("proposalNo").alias("Proposal_no"),
				pmRoot.get("layerNo").alias("layer_no"),
				pmRoot.get("deptId").alias("DEPT_ID")).distinct(true);
		
		Subquery<Integer> amSq = cq.subquery(Integer.class);
		Root<PositionMaster> amSubRoot = cq.from(PositionMaster.class);

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
		
		switch (searchCriteria) {
		case "CUSTOMER_NAME":
			predicates.add(cb.like(cb.upper(cnSq), "%" + req.getCompanyNameSearch().toUpperCase() + "%"));
			break;

		case "BROKER_NAME":
			predicates.add(cb.like(cb.upper(bnSq), "%" + req.getBrokerNameSearch().toUpperCase() + "%"));
			break;

		case "CONTRACT_NO":
			predicates.add(cb.like(tcdRoot.get("contractNo"), "%" + req.getContractNoSearch() + "%"));
			break;

		case "CLAIM_NO":
			predicates.add(cb.like(tcdRoot.get("claimNo"), "%" + req.getClaimNoSearch() + "%"));
			break;

		case "DATE_OF_LOSS":
			predicates.add(cb.like(tcdRoot.get("dateOfLoss"), "%" + req.getDateOfLossSearch() + "%"));
			break;

		case "STATUS_OF_CLAIM":
			predicates.add(cb.like(cb.upper(tcdRoot.get("statusOfClaim")), "%" + req.getClaimStatusSearch() + "%"));
			break;
		}
		
		cq.where(predicates.toArray(new Predicate[0]));
		
		cq.orderBy(cb.desc(tcdRoot.get("claimNo")));
		return em.createQuery(cq).getResultList();
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
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
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
	public Integer selectPaymentReqNo(String claimNo, String paymentRequestNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(cb.count(root)).where(cb.equal(root.get("claimNo"), claimNo),
											 cb.equal(root.get("paymentRequestNo"), paymentRequestNo));
		
		return em.createQuery(cq).getSingleResult();
	}
	
	@Override
	public Integer claimUpdatePayment(InsertCliamDetailsMode3Req req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);
		
		boolean condition = "3".equalsIgnoreCase(req.getProductId());
		
		update.set(root.get("inceptionDate"), req.getDate())
		.set(root.get("paymentReference"), req.getPaymentReference())
		.set(root.get("paymentRequestNo"), req.getPaymentRequestNo())
		.set(root.get("paidClaimOsOc"), req.getPaidClaimOs())
		.set(root.get("paidClaimOsDc"), GetDesginationCountry(req.getPaidClaimOs(),req.getExcRate()))
		.set(root.get("safOsOc"), req.getSurveyorfeeos())
		.set(root.get("safOsDc"), GetDesginationCountry(req.getSurveyorfeeos(),req.getExcRate()))
		.set(root.get("othFeeOsOc"), req.getOtherproffeeos())
		.set(root.get("othFeeOsDc"), GetDesginationCountry(req.getOtherproffeeos(),req.getExcRate()))
		.set(root.get("branchCode"), req.getBranchCode())
		.set(root.get("loginId"), req.getLoginId())
		.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("paidAmountOc"), req.getPaidAmountOrigcurr())
		.set(root.get("paidAmountDc"), GetDesginationCountry(req.getPaidAmountOrigcurr(),req.getExcRate()))
		.set(root.get("remarks"), req.getRemarks())
		
		.set(root.get("reinstatementType"), condition ? req.getReinstType() : "")
		.set(root.get("reinspremiumOurshareOc"),  condition ? req.getReinstPremiumOCOS() : "")
		.set(root.get("reinspremiumOurshareDc"), condition ? GetDesginationCountry(req.getReinstPremiumOCOS(),req.getExcRate()) : "")
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

		update.set(root.get("totalAmtPaidTillDate"), amt);
				
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
		
		StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("RetroPremium_Split_claim");
		
		// Assign parameters
		storedProcedure.registerStoredProcedureParameter("pvContractNo", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnLayerNo", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnProductId", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnPremiumTranNo", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pdPremTranDate", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnCurrencyId", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnExchange", Integer.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnBranchCode", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pvtransactionType", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pdAmendDate", Date.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnReference", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnTreatyName", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnRemarks", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnUwYear", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("pnSubClass", String.class, ParameterMode.IN);
		storedProcedure.registerStoredProcedureParameter("retroCession", String.class, ParameterMode.IN);
		
		// Set parameters
		storedProcedure.setParameter("pvContractNo", req.getPolicyContractNo());
		storedProcedure.setParameter("pnLayerNo", Integer.parseInt(StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo()));
		storedProcedure.setParameter("pnProductId", Integer.parseInt(req.getProductId()));
		storedProcedure.setParameter("pnPremiumTranNo", Integer.parseInt(req.getClaimPaymentNo()));
		storedProcedure.setParameter("pdPremTranDate", req.getDate());
		storedProcedure.setParameter("pnCurrencyId", Integer.parseInt(req.getCurrecny()));
		storedProcedure.setParameter("pnExchange", Integer.parseInt(req.getExcRate()));
		storedProcedure.setParameter("pnBranchCode", Integer.parseInt(req.getBranchCode()));
		storedProcedure.setParameter("pvtransactionType", "C");
		storedProcedure.setParameter("pdAmendDate", "");
		storedProcedure.setParameter("pnReference", "");
		storedProcedure.setParameter("pnTreatyName", "");
		storedProcedure.setParameter("pnRemarks", "");
		storedProcedure.setParameter("pnUwYear", "");
		storedProcedure.setParameter("pnSubClass", "");
		storedProcedure.setParameter("retroCession", req.getRiRecovery());
		// execute SP
		storedProcedure.execute();
		
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
	public TtrnClaimPayment clainArchInsert(InsertCliamDetailsMode3Req req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnClaimPayment> cq = cb.createQuery(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		
		cq.multiselect(root).where( cb.equal(root.get("contractNo"), req.getPolicyContractNo()),
									cb.equal(root.get("claimNo"), req.getClaimNo()),
									cb.equal(root.get("layerNo"), req.getLayerNo()),
									cb.equal(root.get("claimPaymentNo"), req.getClaimPaymentNo()));
		
		return em.createQuery(cq).getSingleResult();
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
	public String selectMaxNo(String claimNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnClaimUpdation> root = cq.from(TtrnClaimUpdation.class);
		
		cq.multiselect(cb.max(root.get("slNo")).as(String.class).alias("MAX_NO"))
		.where( cb.equal(root.get("claimNo"), claimNo),
				cb.equal(root.get("contractNo"), contractNo));
		
		return em.createQuery(cq).getSingleResult();
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
	public List<Map<String, Object>> selectFacGetCliamQuery(String proposalNo, String productId, String branchCode) {
		
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		String nativeQuery = "SELECT   (select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',') "
				+ " from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from "
				+ "table(SPLIT_TEXT_FN(replace(RK.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID ="
				+ " RK.RSK_PRODUCTID AND PERSONAL.BRANCH_CODE = SPFC.BRANCH_CODE)TMAS_SPFC_NAME,"
				+ "RK.RSK_CONTRACT_NO,RK.RSK_ORIGINAL_CURR,RK.RSK_ENDORSEMENT_NO,PERSONAL.COMPANY_NAME "
				+ "CEDING_COMPANY,RK.RSK_CEDINGID, RK.RSK_PROPOSAL_NUMBER, RP.SHARE_SIGNED, SUM_INSURED_OUR_SHARE_DC,"
				+ " SUM_INSURED_OUR_SHARE_OC, RP.RSK_CEDANT_RETENTION, TO_CHAR (RK.RSK_INCEPTION_DATE, 'DD/MM/YYYY') "
				+ "INCP_DATE,TO_CHAR(RK.RSK_ACCOUNT_DATE, 'DD/MM/YYYY') RSK_ACCOUNT_DATE, TO_CHAR (RK.RSK_EXPIRY_DATE, "
				+ "'DD/MM/YYYY') EXP_DATE, RK.RSK_TREATYID, PI.FIRST_NAME||' '||PI.LAST_NAME BROKER_NAME, RK.RSK_BROKERID,"
				+ "(select TMAS_DEPARTMENT_NAME from TMAS_DEPARTMENT_MASTER where TMAS_DEPARTMENT_ID=RK.RSK_DEPTID AND "
				+ "RK.RSK_PRODUCTID=TMAS_PRODUCT_ID and Branch_code=PERSONAL.BRANCH_CODE and TMAS_STATUS='Y') TMAS_DEPARTMENT_NAME,"
				+ " RK.RSK_INSURED_NAME, RK.RSK_PROPOSAL_NUMBER,RK.RSK_SPFCID,RK.RSK_DEPTID FROM   TTRN_RISK_DETAILS RK,"
				+ " PERSONAL_INFO PERSONAL, TTRN_FAC_RISK_PROPOSAL RP,PERSONAL_INFO PI WHERE RK.RSK_PROPOSAL_NUMBER = ? "
				+ "AND RK.RSK_PRODUCTID = ? AND RK.RSK_CEDINGID = PERSONAL.CUSTOMER_ID AND PERSONAL.BRANCH_CODE=? AND"
				+ " PERSONAL.CUSTOMER_TYPE='C' AND PERSONAL.AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO WHERE"
				+ " CUSTOMER_ID=PERSONAL.CUSTOMER_ID AND BRANCH_CODE=PERSONAL.BRANCH_CODE AND CUSTOMER_TYPE="
				+ "PERSONAL.CUSTOMER_TYPE) AND RK.RSK_BROKERID = PI.CUSTOMER_ID AND PI.BRANCH_CODE=? AND PI.CUSTOMER_TYPE="
				+ "'B' AND PI.AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO  WHERE CUSTOMER_ID=PI.CUSTOMER_ID AND"
				+ " BRANCH_CODE=PI.BRANCH_CODE AND CUSTOMER_TYPE=PI.CUSTOMER_TYPE) AND RP.RSK_PROPOSAL_NUMBER = "
				+ "RK.RSK_PROPOSAL_NUMBER AND RK.RSK_CEDINGID = PERSONAL.CUSTOMER_ID AND RK.RSK_ENDORSEMENT_NO = "
				+ "(SELECT   MAX (RSK_ENDORSEMENT_NO) FROM   TTRN_RISK_DETAILS WHERE   RSK_PROPOSAL_NUMBER =? ) "
				+ "AND RP.RSK_ENDORSEMENT_NO = (SELECT   MAX (RSK_ENDORSEMENT_NO) FROM   TTRN_FAC_RISK_PROPOSAL WHERE  "
				+ " RSK_PROPOSAL_NUMBER = RK.RSK_PROPOSAL_NUMBER)"; 
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,proposalNo);
	    query.setParameter(2,productId);
	    query.setParameter(3,branchCode);
	    query.setParameter(4,branchCode);
	    query.setParameter(5,proposalNo);

	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
	}

	@Override
	public List<Map<String, Object>> selectXolOrTeatyGetClimeQuery(String proposalNo, String productId,
			String branchCode) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		String nativeQuery = "SELECT   (select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')"
				+ "  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from "
				+ "table(SPLIT_TEXT_FN(replace(RK.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = "
				+ "RK.RSK_PRODUCTID AND PERSONAL.BRANCH_CODE = SPFC.BRANCH_CODE)TMAS_SPFC_NAME,"
				+ "RK.RSK_CONTRACT_NO,RK.RSK_ENDORSEMENT_NO,  PERSONAL.COMPANY_NAME CEDING_COMPANY, "
				+ " RK.RSK_CEDINGID,   RK.RSK_PROPOSAL_NUMBER, RP.RSK_SHARE_SIGNED,  RP.RSK_LIMIT_OC,"
				+ "  RP.RSK_LIMIT_DC,  RP.RSK_LIMIT_OS_OC,RP.RSK_LIMIT_OS_DC,  RP.RSK_CEDANT_RETENTION, "
				+ " TO_CHAR (RK.RSK_INCEPTION_DATE, 'DD/MM/YYYY') INCP_DATE,TO_CHAR(RK.RSK_ACCOUNT_DATE,"
				+ " 'DD/MM/YYYY') RSK_ACCOUNT_DATE,  TO_CHAR (RK.RSK_EXPIRY_DATE, 'DD/MM/YYYY') EXP_DATE,"
				+ "  RK.RSK_TREATYID,  RP.RSK_PF_COVERED,  RK.RSK_INSURED_NAME,  RK.RSK_RISK_COVERED,"
				+ "  PI.FIRST_NAME||' '||PI.LAST_NAME BROKER_NAME,  RK.RSK_BROKERID, RK.RSK_PROPOSAL_TYPE,"
				+ "RK.RSK_BASIS, RC.RSK_CASHLOSS_LMT_OC,RC.RSK_CASHLOSS_LMT_DC,(select TMAS_DEPARTMENT_NAME "
				+ "from TMAS_DEPARTMENT_MASTER where TMAS_DEPARTMENT_ID=RK.RSK_DEPTID AND RK.RSK_PRODUCTID="
				+ "TMAS_PRODUCT_ID and Branch_code=PERSONAL.BRANCH_CODE and TMAS_STATUS='Y')"
				+ " TMAS_DEPARTMENT_NAME,RK.RSK_SPFCID,RK.RSK_DEPTID,RK.RSK_UWYEAR ,RC.RSK_REINSTATEMENT_PREMIUM  "
				+ "FROM TTRN_RISK_DETAILS RK, PERSONAL_INFO PERSONAL, TTRN_RISK_PROPOSAL RP,PERSONAL_INFO PI"
				+ ",TTRN_RISK_COMMISSION RC  WHERE RK.RSK_PROPOSAL_NUMBER = ?  AND RK.RSK_PRODUCTID = ?"
				+ "  AND RK.RSK_CEDINGID = PERSONAL.CUSTOMER_ID  AND PERSONAL.BRANCH_CODE=? AND"
				+ " PERSONAL.CUSTOMER_TYPE='C' AND PERSONAL.AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO "
				+ "WHERE CUSTOMER_ID=PERSONAL.CUSTOMER_ID  AND BRANCH_CODE=PERSONAL.BRANCH_CODE AND CUSTOMER_TYPE"
				+ "=PERSONAL.CUSTOMER_TYPE)  AND RK.RSK_BROKERID = PI.CUSTOMER_ID  AND PI.BRANCH_CODE=? AND "
				+ "PI.CUSTOMER_TYPE='B' AND PI.AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO  WHERE CUSTOMER_ID"
				+ "=PI.CUSTOMER_ID  AND BRANCH_CODE=PI.BRANCH_CODE AND CUSTOMER_TYPE=PI.CUSTOMER_TYPE)  AND "
				+ "RP.RSK_PROPOSAL_NUMBER = RK.RSK_PROPOSAL_NUMBER  AND RK.RSK_CEDINGID = PERSONAL.CUSTOMER_ID "
				+ " AND RK.RSK_ENDORSEMENT_NO = (SELECT   MAX (RSK_ENDORSEMENT_NO)  FROM   TTRN_RISK_DETAILS "
				+ " WHERE   RSK_PROPOSAL_NUMBER =? )  AND RP.RSK_ENDORSEMENT_NO =  (SELECT   MAX (RSK_ENDORSEMENT_NO) "
				+ " FROM   TTRN_RISK_PROPOSAL  WHERE   RSK_PROPOSAL_NUMBER = RK.RSK_PROPOSAL_NUMBER) AND "
				+ "RC.RSK_PROPOSAL_NUMBER=RP.RSK_PROPOSAL_NUMBER AND RC.RSK_ENDORSEMENT_NO=(SELECT MAX(RSK_ENDORSEMENT_NO)"
				+ " FROM TTRN_RISK_COMMISSION WHERE RSK_PROPOSAL_NUMBER=RC.RSK_PROPOSAL_NUMBER)"; 
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,proposalNo);
	    query.setParameter(2,productId);
	    query.setParameter(3,branchCode);
	    query.setParameter(4,branchCode);
	    query.setParameter(5,proposalNo);
	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
		
	}

	@Override
	public List<Map<String, Object>> partialSelectGetpaymentlist(ClaimPaymentListReq req) {
	
		
		List<Map<String,Object>> resultList = new ArrayList<>();
		String searchQuery = "";
		String searchKey = null;
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				searchQuery= " AND UPPER(CUSTOMER_NAME) LIKE UPPER (?) ";
				searchKey = "%"+req.getCompanyNameSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				searchQuery= " AND UPPER(BROKER_NAME) LIKE UPPER (?) ";
				searchKey = "%"+req.getBrokerNameSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				searchQuery = " AND CONTRACT_NO LIKE ? ";
				searchKey = "%"+req.getContractNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				searchQuery = " AND CLAIM_NO LIKE ? ";
				searchKey = "%"+req.getClaimNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
				searchQuery = " AND CLAIM_PAYMENT_NO LIKE ? ";
				searchKey = "%"+req.getPaymentNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
				searchQuery = " AND INCEPTION_DT LIKE ? ";
				searchKey = "%"+req.getPaymentDateSearch()+"%";
			}
		}
		
		String nativeQuery = "select * from (SELECT   DISTINCT PAID_AMOUNT_OC,TCD.CLAIM_NO,TCD.CONTRACT_NO,"
				+ " PAYMENT_REQUEST_NO, LOSS_ESTIMATE_REVISED_OC,TO_CHAR (TCD.INCEPTION_DATE, 'DD/MM/YYYY')"
				+ " AS INCEPTION_DT,  CLAIM_NOTE_RECOMM, PAYMENT_REFERENCE, ADVICE_TREASURY, PAID_AMOUNT_DC,"
				+ " LOSS_ESTIMATE_REVISED_DC, CLAIM_PAYMENT_NO, RESERVE_ID,SETTLEMENT_STATUS,  pm.Product_id,"
				+ " TPM.TMAS_PRODUCT_NAME, TO_CHAR (PM.INCEPTION_DATE, 'DD/MM/YYYY') INCEPTION_DATE, TO_CHAR"
				+ " (PM.Expiry_date, 'DD/MM/YYYY') Expiry_date,  (SELECT   COMPANY_NAME FROM   personal_info Pi"
				+ " WHERE       CUSTOMER_TYPE \\= 'C'  AND pm.CEDING_COMPANY_ID \\= Pi.CUSTOMER_ID AND pi.branch_code"
				+ " \\= pm.branch_code AND amend_id \\= (SELECT   MAX (Amend_id)  FROM   personal_info p WHERE  "
				+ "     p.CUSTOMER_TYPE \\= pi.CUSTOMER_TYPE  AND p.customer_id \\= pi.customer_id AND p.branch_code "
				+ "\\= pm.branch_code))  Customer_name,(SELECT   FIRST_NAME  FROM   personal_info Pi  WHERE   "
				+ "    CUSTOMER_TYPE \\= 'B'    AND pm.Broker_id \\= Pi.CUSTOMER_ID  AND pi.branch_code \\="
				+ " pm.branch_code  AND amend_id \\=  (SELECT   MAX (Amend_id)   FROM   personal_info p WHERE "
				+ "      p.CUSTOMER_TYPE \\= pi.CUSTOMER_TYPE  AND p.customer_id \\= pi.customer_id   AND p.branch_code "
				+ "\\= pm.branch_code))   Broker_name,  Pm.Proposal_no, Pm.layer_no,PM.DEPT_ID  FROM  "
				+ " TTRN_CLAIM_PAYMENT TCD,   POSITION_MASTER PM, TMAS_PRODUCT_MASTER TPM WHERE      TCD.Contract_No "
				+ "\\= Pm.Contract_No   AND NVL (tcd.layer_no, 0) \\= NVL (pm.layer_no, 0)   AND Pm.branch_code \\= ? "
				+ " AND TPM.BRANCH_CODE \\= Pm.branch_code  AND TPM.Tmas_Product_id \\= pm.Product_id AND Pm.Amend_Id"
				+ " \\=  (SELECT   MAX (Amend_Id)  FROM   Position_Master P   WHERE   P.Contract_No \\= Pm.Contract_No "
				+ "AND NVL (P.layer_no, 0) \\= NVL (pm.layer_no, 0)) ORDER BY   CLAIM_NO DESC)where  rownum<\\=100 "; 
		
		nativeQuery+=searchQuery;
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,req.getBranchCode());
	    if(Objects.nonNull(searchKey))
	    	query.setParameter(2,searchKey);

	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
	}

	@Override
	public List<Map<String, Object>> selectGetpaymentlist(ClaimPaymentListReq req) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		String searchQuery = "";
		String searchKey = null;
		
		if("S".equalsIgnoreCase(req.getSearchType())){
			if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
				searchQuery= " WHERE UPPER(CUSTOMER_NAME) LIKE UPPER (?) ";
				searchKey = "%"+req.getCompanyNameSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
				searchQuery= " WHERE UPPER(BROKER_NAME) LIKE UPPER (?) ";
				searchKey = "%"+req.getBrokerNameSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getContractNoSearch())){
				searchQuery = " WHERE CONTRACT_NO LIKE ? ";
				searchKey = "%"+req.getContractNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getClaimNoSearch())){
				searchQuery = " WHERE CLAIM_NO LIKE ? ";
				searchKey = "%"+req.getClaimNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
				searchQuery = " WHERE CLAIM_PAYMENT_NO LIKE ? ";
				searchKey = "%"+req.getPaymentNoSearch()+"%";
			}
			if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
				searchQuery = " WHERE INCEPTION_DT LIKE ? ";
				searchKey = "%"+req.getPaymentDateSearch()+"%";
			}
		}
		
		String nativeQuery = "Select * from (SELECT   DISTINCT PAID_AMOUNT_OC,TCD.CLAIM_NO,TCD.CONTRACT_NO,"
				+ " PAYMENT_REQUEST_NO, LOSS_ESTIMATE_REVISED_OC,TO_CHAR (TCD.INCEPTION_DATE, 'DD/MM/YYYY')"
				+ " AS INCEPTION_DT,  CLAIM_NOTE_RECOMM, PAYMENT_REFERENCE, ADVICE_TREASURY, PAID_AMOUNT_DC,"
				+ " LOSS_ESTIMATE_REVISED_DC, CLAIM_PAYMENT_NO, RESERVE_ID,SETTLEMENT_STATUS,  pm.Product_id, "
				+ "TPM.TMAS_PRODUCT_NAME, TO_CHAR (PM.INCEPTION_DATE, 'DD/MM/YYYY') INCEPTION_DATE, TO_CHAR"
				+ " (PM.Expiry_date, 'DD/MM/YYYY') Expiry_date,  (SELECT   COMPANY_NAME FROM   personal_info Pi "
				+ "WHERE       CUSTOMER_TYPE \\= 'C'  AND pm.CEDING_COMPANY_ID \\= Pi.CUSTOMER_ID AND pi.branch_code "
				+ "\\= pm.branch_code AND amend_id \\= (SELECT   MAX (Amend_id)  FROM   personal_info p WHERE  "
				+ "     p.CUSTOMER_TYPE \\= pi.CUSTOMER_TYPE  AND p.customer_id \\= pi.customer_id AND p.branch_code"
				+ " \\= pm.branch_code))  Customer_name,(SELECT   FIRST_NAME  FROM   personal_info Pi  WHERE    "
				+ "   CUSTOMER_TYPE \\= 'B'    AND pm.Broker_id \\= Pi.CUSTOMER_ID  AND pi.branch_code \\= pm.branch_code"
				+ "  AND amend_id \\=  (SELECT   MAX (Amend_id)   FROM   personal_info p WHERE       p.CUSTOMER_TYPE"
				+ " \\= pi.CUSTOMER_TYPE  AND p.customer_id \\= pi.customer_id   AND p.branch_code \\= pm.branch_code))"
				+ "   Broker_name,  Pm.Proposal_no, Pm.layer_no,PM.DEPT_ID  FROM   TTRN_CLAIM_PAYMENT TCD,  "
				+ " POSITION_MASTER PM, TMAS_PRODUCT_MASTER TPM WHERE      TCD.Contract_No \\= Pm.Contract_No  "
				+ " AND NVL (tcd.layer_no, 0) \\= NVL (pm.layer_no, 0)   AND Pm.branch_code \\= ?  AND TPM.BRANCH_CODE"
				+ " \\= Pm.branch_code  AND TPM.Tmas_Product_id \\= pm.Product_id AND Pm.Amend_Id \\=  (SELECT  "
				+ " MAX (Amend_Id)  FROM   Position_Master P   WHERE   P.Contract_No \\= Pm.Contract_No AND NVL"
				+ " (P.layer_no, 0) \\= NVL (pm.layer_no, 0))) "; 
		
		nativeQuery+=searchQuery;
		nativeQuery+=" ORDER BY CLAIM_NO DESC ";
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,req.getBranchCode());
	    if(Objects.nonNull(searchKey))
	    	query.setParameter(2,searchKey);

	    query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    resultList =  query.getResultList();
		return resultList;
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
		
		cq.multiselect(cb.max(root.get("slNo")))
		.where(cb.equal(root.get("claimNo"), claimNo),
			   cb.equal(root.get("contractNo"), policyContractNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Map<String, Object>> contractIdentifierList(ContractidetifierlistReq req) {
		List<String> columns = new ArrayList<String>(Arrays.asList("PROPOSAL_NO", "DEPT_ID", "CONTRACT_NO",
				"CEDING_COMPANY_ID", "ACCOUNT_DATE", "TMAS_DEPARTMENT_NAME", "COMPANY_NAME", "BROKER_NAME",
				"INCEPTION_DATE", "EXPIRY_DATE", "LAYER_NO", "BASE_LAYER", "OLD_CONTRACTNO", "RENEWAL_STATUS",
				"RENWALDATE", "UW_YEAR", "UW_MONTH", "UNDERWRITTER", "HAS_CLAIM", "CLAIM_NO", "COUNT", "AMEND_ID"));

		List<Map<String, Object>> resultList = new ArrayList<>();
		String searchQuery = "";
		String searchKey = null;

		if (!"N".equalsIgnoreCase(req.getCedingCompanyCode()) && !"".equalsIgnoreCase(req.getCedingCompanyCode())) {
			searchQuery = " AND D.RSK_CEDINGID = ? ";
			searchKey = req.getCedingCompanyCode();

		}
		if (!"N".equalsIgnoreCase(req.getBrokerCode()) && !"".equalsIgnoreCase(req.getBrokerCode())) {
			searchQuery = "  AND D.RSK_BROKERID = ? ";
			searchKey = req.getBrokerCode();

		}
		if (!"N".equalsIgnoreCase(req.getUnderwritingYear()) && !"".equalsIgnoreCase(req.getUnderwritingYear())) {
			searchQuery = " AND D.RSK_UWYEAR = ?  ";
			searchKey = req.getUnderwritingYear();

		}
		if (!"N".equalsIgnoreCase(req.getDeptId()) && !"".equalsIgnoreCase(req.getDeptId())) {
			searchQuery = " AND D.RSK_DEPTID = ? ";
			searchKey = req.getDeptId();
		}

		String nativeQuery = "SELECT distinct A.PROPOSAL_NO,A.DEPT_ID,A.CONTRACT_NO,A.CEDING_COMPANY_ID,"
				+ "TO_CHAR (A.ACCOUNT_DATE, 'dd/mm/yyyy') AS ACCOUNT_DATE,B.TMAS_DEPARTMENT_NAME,C.COMPANY_NAME,"
				+ " C1.FIRST_NAME || ' '||C1.LAST_NAME BROKER_NAME,TO_CHAR (A.INCEPTION_DATE, 'dd/mm/yyyy')"
				+ " AS INCEPTION_DATE, TO_CHAR (A.EXPIRY_DATE, 'dd/mm/yyyy') AS EXPIRY_DATE,A.LAYER_NO,"
				+ "A.BASE_LAYER,A.OLD_CONTRACTNO AS OLD_CONTRACTNO, A.RENEWAL_STATUS AS RENEWAL_STATUS,"
				+ "(A.EXPIRY_DATE - SYSDATE) AS RENWALDATE,(CASE WHEN A.UW_YEAR != 0 THEN A.UW_YEAR ELSE NULL END)"
				+ " UW_YEAR, TO_CHAR (A.UW_MONTH, 'dd/mm/yyyy') UW_MONTH,E.UNDERWRITTER,DECODE(CD.CONTRACT_NO,"
				+ "NULL,'N','Y') HAS_CLAIM,CD.CLAIM_NO,CD.COUNT,A.AMEND_ID FROM   POSITION_MASTER A JOIN "
				+ "TMAS_DEPARTMENT_MASTER B ON A.DEPT_ID = B.TMAS_DEPARTMENT_ID JOIN PERSONAL_INFO C ON "
				+ " C.CUSTOMER_ID = A.CEDING_COMPANY_ID JOIN TTRN_RISK_DETAILS D ON A.PRODUCT_ID = D.RSK_PRODUCTID "
				+ "AND A.CONTRACT_NO = D.RSK_CONTRACT_NO AND A.LAYER_NO=D.RSK_LAYER_NO AND A.AMEND_ID ="
				+ " D.RSK_ENDORSEMENT_NO AND A.DEPT_ID = D.RSK_DEPTID JOIN UNDERWRITTER_MASTER E ON D.RSK_UNDERWRITTER"
				+ " = E.UWR_CODE JOIN PERSONAL_INFO C1 ON  C1.CUSTOMER_ID = A.BROKER_ID LEFT OUTER JOIN ( "
				+ "SELECT DISTINCT CONTRACT_NO,LAYER_NO,CLAIM_NO, COUNT(CLAIM_NO) COUNT FROM   TTRN_CLAIM_DETAILS "
				+ "group by CONTRACT_NO, LAYER_NO ,CLAIM_NO) CD ON A.CONTRACT_NO = CD.CONTRACT_NO AND A.LAYER_NO="
				+ "CD.LAYER_NO  WHERE  A.PRODUCT_ID = ?  AND A.CONTRACT_STATUS = 'A' AND A.CONTRACT_NO > 0 AND"
				+ " B.TMAS_PRODUCT_ID = ? AND B.BRANCH_CODE = ?   AND B.TMAS_STATUS='Y' AND C.BRANCH_CODE = ?"
				+ " AND C.CUSTOMER_TYPE = 'C' AND C.AMEND_ID = (SELECT   MAX (AMEND_ID) FROM   PERSONAL_INFO"
				+ " WHERE CUSTOMER_ID = C.CUSTOMER_ID AND BRANCH_CODE = C.BRANCH_CODE AND CUSTOMER_TYPE = "
				+ "C.CUSTOMER_TYPE) AND E.BRANCH_CODE = ? AND A.BRANCH_CODE = ? AND A.AMEND_ID IN (SELECT "
				+ "  MAX (AMEND_ID) FROM   POSITION_MASTER WHERE  PRODUCT_ID = ? AND A.CONTRACT_NO = CONTRACT_NO"
				+ " AND A.LAYER_NO=LAYER_NO AND A.BRANCH_CODE = BRANCH_CODE AND CONTRACT_STATUS = 'A') AND "
				+ "C1.BRANCH_CODE = ? AND C1.AMEND_ID = (SELECT   MAX (AMEND_ID) FROM   PERSONAL_INFO WHERE "
				+ "CUSTOMER_ID = C1.CUSTOMER_ID AND BRANCH_CODE = C1.BRANCH_CODE AND CUSTOMER_TYPE = "
				+ "C1.CUSTOMER_TYPE) and D.RSK_ENDORSEMENT_NO=(SELECT MAX(RSK_ENDORSEMENT_NO) FROM TTRN_RISK_DETAILS"
				+ " RD  WHERE RD.BRANCH_CODE=D.BRANCH_CODE AND RD.RSK_PROPOSAL_NUMBER=D.RSK_PROPOSAL_NUMBER)";

		nativeQuery += searchQuery;
		nativeQuery += " ORDER BY A.CONTRACT_NO DESC ";

		Query query = em.createNativeQuery(nativeQuery);
		query.setParameter(1, req.getProductId());
		query.setParameter(2, req.getProductId());
		query.setParameter(3, req.getBranchCode());
		query.setParameter(4, req.getBranchCode());
		query.setParameter(5, req.getBranchCode());
		query.setParameter(6, req.getBranchCode());
		query.setParameter(7, req.getProductId());
		query.setParameter(8, req.getBranchCode());
		if (Objects.nonNull(searchKey))
			query.setParameter(9, searchKey);

		List<Object[]> list = query.getResultList();
		for (Object[] objArr : list) {
			Map<String, Object> output = new HashMap<>();
			int index = 0;
			for (Object q1 : objArr) {
				output.put(columns.get(index), q1.toString());
				index++;
			}
			resultList.add(output);
		}

		return resultList;
	}

}
