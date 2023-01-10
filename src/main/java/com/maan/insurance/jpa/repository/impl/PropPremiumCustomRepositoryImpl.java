package com.maan.insurance.jpa.repository.impl;

import java.math.BigDecimal;
import java.sql.Date;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.propPremium.TtrnCashLossCredit;
import com.maan.insurance.jpa.entity.propPremium.TtrnCashLossCreditUpdate;
import com.maan.insurance.jpa.entity.propPremium.TtrnDepositRelease;
import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnRetroCessionary;
import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.xolpremium.SequenceMaster;
import com.maan.insurance.jpa.repository.propPremium.PropPremiumCustomRepository;
import com.maan.insurance.jpa.repository.propPremium.TtrnDepositReleaseRepository;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TmasPolicyBranch;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.entity.TtrnPttySection;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes;

@Repository
public class PropPremiumCustomRepositoryImpl implements PropPremiumCustomRepository {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	TtrnDepositReleaseRepository ttrnrepos;

	@Override
	public List<Tuple> premiumSelectPremiumedList(GetPremiumedListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<RskPremiumDetails> traRoot = cq.from(RskPremiumDetails.class);
		Root<PositionMaster> pmroot = cq.from(PositionMaster.class);
		
		Expression<String> nameExpression = cb.concat(cb.concat(piRoot.get("firstName"), " "),
				piRoot.get("lastName"));
		
		Expression<String> traExpression = cb.concat(cb.concat(traRoot.get("instalmentNumber"), " "),
				traRoot.get("accountPeriodQtr"));
		
		Expression<String> accPerExpression = cb.concat(cb.concat(traRoot.get("accountPeriodQtr"), " "),
				traRoot.get("accountPeriodYear"));
		
		Subquery<String> detailNameSq = cq.subquery(String.class);
		Root<ConstantDetail> dSubRoot = detailNameSq.from(ConstantDetail.class);

		detailNameSq.select(dSubRoot.get("detailName")).distinct(true)
				   .where(cb.equal(dSubRoot.get("categoryId"), 49),
						   cb.equal(dSubRoot.get("type"), traRoot.get("accountPeriodQtr")),
						   cb.equal(dSubRoot.get("status"), "Y"));
		
		cq.multiselect(rkRoot.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				personalRoot.get("companyName").alias("COMPANY_NAME"),
				nameExpression.alias("BROKER_NAME"),
				rkRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
				rkRoot.get("rskLayerNo").alias("RSK_LAYER_NO"),
				pmroot.get("sectionNo").alias("SECTION_NO"),
				traRoot.get("transactionNo").alias("TRANSACTION_NO"),
				traExpression.alias("INS_DETAIL"),
				cb.selectCase().when(cb.equal(rkRoot.<Integer>get("rskProductid"), 2), 
									 cb.concat(cb.concat(detailNameSq, " "), traRoot.get("accountPeriodYear")))
							   .otherwise(accPerExpression).alias("ACC_PER"),
			   traRoot.get("entryDateTime").alias("INS_DATE"),
			   traRoot.get("statementDate").alias("STATEMENT_DATE"),
			   traRoot.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
			   cb.selectCase().when(cb.isNull(traRoot.get("allocatedTillDate")), 0)
			   				  .otherwise(traRoot.get("allocatedTillDate")).alias("ALLOC_AMT"),
			   traRoot.get("movementYn").alias("MOVEMENT_YN"),
			   traRoot.get("settlementStatus").alias("SETTLEMENT_STATUS"),
			   traRoot.get("transactionMonthYear").alias("TRANSACTION_DATE"),
			   traRoot.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"));
		
		Subquery<Integer> amendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> amendSubRoort = amendSq.from(PersonalInfo.class);

		amendSq.select(cb.max(amendSubRoort.get("amendId"))).where(
				cb.equal(amendSubRoort.get("customerId"), personalRoot.get("customerId")),
				cb.equal(amendSubRoort.get("branchCode"), personalRoot.get("branchCode")));
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piAmendSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piAmendSubRoot.get("amendId"))).where(
				cb.equal(piAmendSubRoot.get("customerId"), piRoot.get("customerId")),
				cb.equal(piAmendSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskContractNo"), req.getContNo()),
				cb.equal(endoSubRoot.get("rskDeptid"), traRoot.get("subClass")));
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), req.getContNo()),
				 cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				 cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
				 cb.equal(personalRoot.get("branchCode"), req.getBranchCode()),
				 cb.equal(personalRoot.get("amendId"), amendSq),
				 cb.equal(piRoot.get("branchCode"), personalRoot.get("branchCode")),
				 cb.equal(piRoot.get("amendId"), piAmendSq),
				 cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
				 cb.equal(traRoot.get("contractNo"), rkRoot.get("rskContractNo")),
				 cb.equal(rkRoot.get("rskProposalNumber"), pmroot.get("proposalNo")),
				 cb.equal(pmroot.get("sectionNo"), req.getSectionNo()),
				 cb.equal(traRoot.get("subClass"), rkRoot.get("rskDeptid")));
		
		cq.orderBy(cb.desc(traRoot.get("transactionNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> pityPremiumListTemp(GetPremiumedListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<RskPremiumDetailsTemp> traRoot = cq.from(RskPremiumDetailsTemp.class);
		Root<PositionMaster> pmroot = cq.from(PositionMaster.class);
		Expression<String> nameExpression = cb.concat(cb.concat(piRoot.get("firstName"), " "),
				piRoot.get("lastName"));
		
		Expression<String> traExpression = cb.concat(cb.concat(traRoot.get("instalmentNumber"), " "),
				traRoot.get("accountPeriodQtr"));
		
		Expression<String> accPerExpression = cb.concat(cb.concat(traRoot.get("accountPeriodQtr"), " "),
				traRoot.get("accountPeriodYear"));
		
		Subquery<String> detailNameSq = cq.subquery(String.class);
		Root<ConstantDetail> dSubRoot = detailNameSq.from(ConstantDetail.class);

		detailNameSq.select(dSubRoot.get("detailName")).distinct(true)
				   .where(cb.equal(dSubRoot.get("categoryId"), 49),
						   cb.equal(dSubRoot.get("type"), traRoot.get("accountPeriodQtr")),
						   cb.equal(dSubRoot.get("status"), "Y"));
		
		cq.multiselect(traRoot.get("requestNo").alias("REQUEST_NO"),
				rkRoot.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				personalRoot.get("companyName").alias("COMPANY_NAME"),
				nameExpression.alias("BROKER_NAME"),
				rkRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
				rkRoot.get("rskLayerNo").alias("RSK_LAYER_NO"),
				pmroot.get("sectionNo").alias("SECTION_NO"),
				traRoot.get("transactionNo").alias("TRANSACTION_NO"),
				traExpression.alias("INS_DETAIL"),
				cb.selectCase().when(cb.equal(rkRoot.<Integer>get("rskProductid"), 2), 
									 cb.concat(cb.concat(detailNameSq, " "), traRoot.get("accountPeriodYear")))
							   .otherwise(accPerExpression).alias("ACC_PER"),
			   traRoot.get("entryDateTime").alias("INS_DATE"),
			   traRoot.get("statementDate").alias("STATEMENT_DATE"),
			   traRoot.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
			   cb.selectCase().when(cb.isNull(traRoot.get("allocatedTillDate")), 0)
			   				  .otherwise(traRoot.get("allocatedTillDate")).alias("ALLOC_AMT"),
			   traRoot.get("movementYn").alias("MOVEMENT_YN"),
			   traRoot.get("settlementStatus").alias("SETTLEMENT_STATUS"),
			   traRoot.get("transactionMonthYear").alias("TRANSACTION_DATE"),
			   traRoot.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"));
		
		Subquery<Integer> amendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> amendSubRoort = amendSq.from(PersonalInfo.class);

		amendSq.select(cb.max(amendSubRoort.get("amendId"))).where(
				cb.equal(amendSubRoort.get("customerId"), personalRoot.get("customerId")),
				cb.equal(amendSubRoort.get("branchCode"), personalRoot.get("branchCode")));
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piAmendSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piAmendSubRoot.get("amendId"))).where(
				cb.equal(piAmendSubRoot.get("customerId"), piRoot.get("customerId")),
				cb.equal(piAmendSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskContractNo"), req.getContNo()),
				cb.equal(endoSubRoot.get("rskDeptid"), traRoot.get("subClass")));
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), req.getContNo()),
				 cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				 cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
				 cb.equal(personalRoot.get("branchCode"), req.getBranchCode()),
				 cb.equal(personalRoot.get("amendId"), amendSq),
				 cb.equal(piRoot.get("branchCode"), personalRoot.get("branchCode")),
				 cb.equal(piRoot.get("amendId"), piAmendSq),
				 cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
				 cb.equal(traRoot.get("contractNo"), rkRoot.get("rskContractNo")),
				 cb.equal(rkRoot.get("rskProposalNumber"), pmroot.get("proposalNo")),
				 cb.equal(pmroot.get("sectionNo"), req.getSectionNo()),
				 cb.equal(traRoot.get("subClass"), rkRoot.get("rskDeptid")),
				 cb.equal(traRoot.get("transStatus"), "P"));
		
		cq.orderBy(cb.desc(traRoot.get("transactionNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> premiumSelectFacTreatyPreList(String contNo, String deptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> aRoot = cq.from(PositionMaster.class);
		Root<TmasDepartmentMaster> bRoot = cq.from(TmasDepartmentMaster.class);
		Root<PersonalInfo> cRoot = cq.from(PersonalInfo.class);
		
		Subquery<String> fNameSq = cq.subquery(String.class);
		Root<PersonalInfo> fNameSubRoot = fNameSq.from(PersonalInfo.class);

		fNameSq.select(fNameSubRoot.get("firstName"))
			   .where( cb.equal(fNameSubRoot.get("customerId"), aRoot.get("brokerId")),
					   cb.equal(fNameSubRoot.get("customerType"), "B"),
					   cb.equal(fNameSubRoot.get("branchCode"), aRoot.get("branchCode")));
					   //cb.equal(fNameSubRoot.get("rownum"), 1)); //need to check
		
		Subquery<Integer> pmAmendSq = cq.subquery(Integer.class);
		Root<PositionMaster> pmSubRoot = pmAmendSq.from(PositionMaster.class);

		pmAmendSq.select(cb.max(pmSubRoot.get("amendId")))
				.where(cb.equal(pmSubRoot.get("contractNo"), aRoot.get("contractNo")),
					   cb.equal(pmSubRoot.get("deptId"), aRoot.get("deptId")));
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piSubRoot.get("amendId")))
				.where(cb.equal(piSubRoot.get("customerId"), cRoot.get("customerId")),
					   cb.equal(piSubRoot.get("branchCode"), cRoot.get("branchCode")));
		
		cq.multiselect(aRoot.get("contractNo").alias("CONTRACT_NO"),
				aRoot.get("proposalNo").alias("PROPOSAL_NO"),
				bRoot.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
				aRoot.get("productId").alias("PRODUCT_ID"),
				aRoot.get("uwYear").alias("UW_YEAR"),
				cRoot.get("companyName").alias("COMPANY_NAME"),
				aRoot.get("layerNo").alias("LAYER_NO"),
				aRoot.get("deptId").alias("DEPT_ID"),
				fNameSq.alias("BROKER_NAME"))
		
		.where(cb.equal(aRoot.get("contractNo"), contNo),
				cb.equal(aRoot.get("deptId"), deptId),
				cb.equal(aRoot.get("amendId"), pmAmendSq),
				
				cb.equal(aRoot.get("deptId"), bRoot.get("tmasDepartmentId")),
				cb.equal(bRoot.get("branchCode"), aRoot.get("branchCode")),
				cb.equal(bRoot.get("tmasProductId"), aRoot.get("productId")),
				cb.equal(cRoot.get("customerId"), aRoot.get("cedingCompanyId")),
				cb.equal(cRoot.get("branchCode"), aRoot.get("branchCode")),
				cb.equal(cRoot.get("customerType"), "C"),
				cb.equal(cRoot.get("amendId"), piAmendSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getAccPeriod(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRiskDetails> root = cq.from(TtrnRiskDetails.class);

		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.get("rskProposalNumber")))
				.where(cb.equal(endoSubRoot.get("rskProposalNumber"), root.get("rskProposalNumber")));

		cq.select(root.get("rskAccountingPeriod").as(String.class).alias("RSK_ACCOUNTING_PERIOD")).where(
				cb.equal(root.get("rskProposalNumber"), proposalNo), cb.equal(root.get("rskProposalNumber"), endoSq));
		
		return em.createQuery(cq).getSingleResult();
	
	}

	@Override
	public List<Tuple> commonSelectGetconstdetPity(String categoryId, String status, String accPeriod) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<ConstantDetail> root = cq.from(ConstantDetail.class);

		cq.multiselect(root.get("type").alias("TYPE"), 
				root.get("detailName").alias("DETAIL_NAME")).distinct(true);  
		
		if("2".equalsIgnoreCase(accPeriod)){
			cq.where(cb.equal(root.get("categoryId"), categoryId),
					   cb.equal(root.get("status"), status),
					   root.get("remarks").in(Arrays.asList(new String[]{"P","Q"})));
			cq.orderBy(cb.asc(root.get("detailName")));
			
		}else if("3".equalsIgnoreCase(accPeriod)){
			cq.where(cb.equal(root.get("categoryId"), categoryId),
					   cb.equal(root.get("status"), status),
					   root.get("remarks").in(Arrays.asList(new String[]{"P","H"})));
			cq.orderBy(cb.asc(root.get("detailName")));
			
		}else if("4".equalsIgnoreCase(accPeriod)){
			cq.where(cb.equal(root.get("categoryId"), categoryId),
					   cb.equal(root.get("status"), status),
					   root.get("remarks").in(Arrays.asList(new String[]{"P","Y"})));
			cq.orderBy(cb.asc(root.get("detailName")));
			
		}else {
			cq.where(cb.equal(root.get("categoryId"), categoryId),
					   cb.equal(root.get("status"), status));
		}
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getBaseLayer(String contractNo, String deptId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> amSq = cq.subquery(Integer.class);
		Root<PositionMaster> amSubRoot = amSq.from(PositionMaster.class);

		amSq.select(cb.max(amSubRoot.get("amendId")))
				.where( cb.equal(amSubRoot.get("contractNo"), root.get("contractNo")),
						cb.equal(amSubRoot.get("deptId"), root.get("deptId")),
						cb.equal(amSubRoot.get("proposalNo"), root.get("proposalNo")));
		
		
		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("baseLayer")), "0")
				.otherwise(root.get("baseLayer")).alias("BASE_LAYER"),
				root.get("proposalNo").alias("PROPOSAL_NO"))
		
		.where(cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("sectionNo"), deptId),
			   cb.equal(root.get("amendId"), amSq));
		
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public List<Tuple> getSlideCommValue(String proposalNo, String subClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskCommission> root = cq.from(TtrnRiskCommission.class);
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskCommission> endoSubRoot = endoSq.from(TtrnRiskCommission.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskProposalNumber"), root.get("rskProposalNumber")),
				cb.equal(endoSubRoot.get("subClass"), root.get("subClass")));
		
		
		cq.multiselect( root.get("rskSlideCombinSubClass").alias("RSK_SLIDE_COMBIN_SUB_CLASS"),
				root.get("rskSladscaleComm").alias("RSK_SLADSCALE_COMM"),
				root.get("rskProfitComm").alias("RSK_PROFIT_COMM"),
				root.get("rskCombinSubClass").alias("RSK_COMBIN_SUB_CLASS"),
				root.get("rskLossPartCarridor").alias("RSK_LOSS_PART_CARRIDOR"),
				root.get("rskLossCombinSubClass").alias("RSK_LOSS_COMBIN_SUB_CLASS"))
		
		.where(cb.equal(root.get("rskProposalNumber"), proposalNo),
			   cb.equal(root.get("subClass"), subClass),
			   cb.equal(root.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getDeptId(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> amSq = cq.subquery(Integer.class);
		Root<PositionMaster> amSubRoot = amSq.from(PositionMaster.class);

		amSq.select(cb.max(amSubRoot.get("amendId")))
				.where( cb.equal(amSubRoot.get("proposalNo"), root.get("proposalNo")));
		
		
		cq.multiselect( root.get("deptId").as(String.class).alias("DEPT_ID"))
		
		.where(cb.equal(root.get("proposalNo"), StringUtils.isBlank(proposalNo)?BigDecimal.ZERO:new BigDecimal(proposalNo)),
			   cb.equal(root.get("amendId"), amSq));
		
		return em.createQuery(cq).getSingleResult();
		
	}

	@Override
	public List<Tuple> getSlideCommValue2(String proposalNo, String subClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskCommission> root = cq.from(TtrnRiskCommission.class);
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskCommission> endoSubRoot = endoSq.from(TtrnRiskCommission.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskProposalNumber"), root.get("rskProposalNumber")),
				cb.equal(endoSubRoot.get("subClass"), root.get("subClass")));
		
		
		cq.multiselect( root.get("rskSladscaleComm").alias("RSK_SLADSCALE_COMM"),
				root.get("rskProfitComm").alias("RSK_PROFIT_COMM"))
		
		.where(cb.equal(root.get("rskProposalNumber"), proposalNo),
			   cb.equal(root.get("subClass"), subClass),
			   cb.equal(root.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<BigDecimal> selectPremiumSum(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class); 
		Root<RskPremiumDetails> tr = query.from(RskPremiumDetails.class); 
		query.select(cb.sum(tr.get("premiumQuotashareOc"), tr.get("premiumSurplusOc")) ); 
		Predicate n1 = cb.equal(tr.get("contractNo"), contractNo);
		query.where(n1); 

		return em.createQuery(query).getResultList();
	}

	@Override
	public List<Tuple> getContPrem(String contractNo, String departmentId, String branchCode) {
		CriteriaBuilder cb= em.getCriteriaBuilder();	
		CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class);
		
		//Find All
		Root<TtrnRiskProposal> tr = query1.from(TtrnRiskProposal.class);
		
		//Select
		query1.multiselect(tr.get("rskEpiOsoeOc").alias("rskEpiOsoeOc"));	
		
		// Proposal No
		Subquery<Long> proposalNo = query1.subquery(Long.class);		//
		Root<PositionMaster> pm = proposalNo.from(PositionMaster.class);	
		
		Subquery<Long> maxAmendId = query1.subquery(Long.class);
		Root<PositionMaster> amend = maxAmendId.from(PositionMaster.class);
		
		maxAmendId.select(cb.max(amend.get("amendId")));	
		Predicate b1 = cb.equal(amend.get("contractNo"), pm.get("contractNo"));		
		Predicate b2 = cb.equal(amend.get("deptId"),pm.get("deptId") );
		maxAmendId.where(b1,b2);	
		
		proposalNo.select(pm.get("proposalNo"));	
		
		Predicate a1 = cb.equal(pm.get("contractNo"),contractNo);		
		Predicate a2 = cb.equal(pm.get("deptId"),departmentId);
		Predicate a3 = cb.equal(pm.get("amendId"), maxAmendId);
		proposalNo.where(a1,a2,a3);	
		
		// Endorsment Type
		Subquery<Long> maxEndorsment = query1.subquery(Long.class);
		Root<TtrnRiskProposal> maxEndorsmentId = maxEndorsment.from(TtrnRiskProposal.class);
		
		maxEndorsment.select(cb.max(maxEndorsmentId.get("rskEndorsementNo")));	
		Predicate e1 = cb.equal(maxEndorsmentId.get("rskProposalNumber"), tr.get("rskProposalNumber"));		
		Predicate e2 = cb.equal(maxEndorsmentId.get("branchCode"),tr.get("branchCode") );
		maxEndorsment.where(e1,e2);	
		
		//Where
		Predicate n1 = cb.equal(tr.get("rskProposalNumber"), proposalNo);		
		Predicate n2 = cb.equal(tr.get("rskEndorsementNo"), maxEndorsment);	//max
		Predicate n3 = cb.equal(tr.get("branchCode"), branchCode);		
		query1.where(n1,n2,n3);
		
		//Get Result
		TypedQuery<Tuple> result = em.createQuery(query1); 
		return result.getResultList();	
	}

	@Override
	public List<Tuple> claimSelectClaimTableList(BigDecimal contractNo, String layerNo, String subClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimDetails> root = cq.from(TtrnClaimDetails.class);
		
		Expression<Double> exp = cb.diff(root.<Double>get("totalAmtPaidTillDate"), root.<Double>get("lossEstimateOsOc"));
		
		cq.multiselect( root.get("claimNo").alias("CLAIM_NO"),
				root.get("dateOfLoss").alias("DATE_OF_LOSS"),
				root.get("createdDate").alias("CREATED_DATE"),
				root.get("statusOfClaim").alias("STATUS_OF_CLAIM"),
				root.get("contractNo").alias("CONTRACT_NO"),
				exp.alias("EDITVIEW"))
		
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(cb.selectCase().when(cb.isNull(root.get("layerNo")), "0").otherwise(root.get("layerNo")),
						cb.selectCase().when(cb.isNull(cb.literal(layerNo)), "0").otherwise(cb.literal(layerNo))),
			   cb.equal(root.get("subClass"), subClass))
		
		.orderBy(cb.desc(root.get("claimNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getCountCashlossPrem(String contractNo, String branchCode, String tableMoveStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnCashLossCredit> root = cq.from(TtrnCashLossCredit.class);
		
		cq.multiselect(cb.count(root).as(String.class))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("branchCode"), contractNo),
				cb.equal(root.get("tableMoveStatus"), tableMoveStatus));
		
		return em.createQuery(cq).getSingleResult();
		
	}

	@Override
	public String getCountDepositPrem(String contractNo, String branchCode, String tableMoveStatus,
			String releaseType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnDepositRelease> root = cq.from(TtrnDepositRelease.class);
		
		cq.multiselect(cb.count(root).as(String.class))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("tableMoveStatus"), tableMoveStatus),
				cb.equal(root.get("releaseType"), releaseType));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getPremiumReservedDetails(GetPremiumReservedReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		try {
			Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);

			Expression<Double> exp = cb.diff(
					cb.<Double>selectCase().when(cb.isNull(root.<Double>get("premiumreserveQuotashareOc")), 0.0)
							.otherwise(root.<Double>get("premiumreserveQuotashareOc")),
					cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
							.otherwise(root.<Double>get("prdAllocatedTillDate")));
//root.get("rownum").alias("sno"),
			cq.multiselect( root.get("contractNo").alias("CONTRACT_NO"),
					root.get("transactionNo").alias("TRANSACTION_NO"),
					root.get("transactionMonthYear").alias("TRANSACTION_MONTH_YEAR"),
					root.get("currencyId").alias("CURRENCY_ID"), exp.alias("PREMIUMRESERVE_QUOTASHARE_OC"),
					cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
							.otherwise(root.<Double>get("prdAllocatedTillDate")).alias("ALLOCATE_TILLDATE"));
			if (StringUtils.isNotBlank(req.getPrTransNo())) {
				cq.where(
						cb.notEqual(
								cb.<Double>selectCase().when(cb.isNull(root.<Double>get("premiumreserveQuotashareOc")), 0.0)
										.otherwise(root.<Double>get("premiumreserveQuotashareOc")),
								cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
										.otherwise(root.<Double>get("prdAllocatedTillDate"))),
						cb.equal(root.get("contractNo"), req.getContNo()),
						cb.equal(root.get("subClass"), req.getDepartmentId()),
						cb.lessThanOrEqualTo(root.get("transactionMonthYear"), parseDateLocal(req.getTransaction())),
						cb.equal(root.get("transactionNo"), req.getPrTransNo()));
			} else {
				cq.where(
						cb.notEqual(
								cb.<Double>selectCase().when(cb.isNull(root.<Double>get("premiumreserveQuotashareOc")), 0.0)
										.otherwise(root.<Double>get("premiumreserveQuotashareOc")),
								cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
										.otherwise(root.<Double>get("prdAllocatedTillDate"))),
						cb.equal(root.get("contractNo"), req.getContNo()),
						cb.equal(root.get("subClass"), req.getDepartmentId()),
						cb.lessThanOrEqualTo(root.get("transactionMonthYear"), parseDateLocal(req.getTransaction())));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getLossReservedDetails(GetPremiumReservedReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);

		Expression<Double> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lossReserveretainedOc")), 0.0)
						.otherwise(root.<Double>get("lossReserveretainedOc")),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lrdAllocatedTillDate")), 0.0)
						.otherwise(root.<Double>get("lrdAllocatedTillDate")));

		cq.multiselect(root.get("rownum").alias("sno"), root.get("contractNo").alias("CONTRACT_NO"),
				root.get("transactionNo").alias("TRANSACTION_NO"),
				root.get("transactionMonthYear").alias("TRANSACTION_MONTH_YEAR"),
				root.get("currencyId").alias("CURRENCY_ID"), exp.alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
						.otherwise(root.<Double>get("lrdAllocatedTillDate")).alias("ALLOCATE_TILLDATE"));

		if (StringUtils.isNotBlank(req.getPrTransNo())) {
			cq.where(
					cb.notEqual(
							cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lossReserveretainedOc")), 0.0)
									.otherwise(root.<Double>get("lossReserveretainedOc")),
							cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lrdAllocatedTillDate")), 0.0)
									.otherwise(root.<Double>get("lrdAllocatedTillDate"))),
					cb.equal(root.get("contractNo"), req.getContNo()),
					cb.equal(root.get("subClass"), req.getDepartmentId()),
					cb.lessThanOrEqualTo(root.get("transactionMonthYear"), req.getTransaction()),
					cb.equal(root.get("transactionNo"), req.getPrTransNo()));
		} else {
			cq.where(
					cb.notEqual(
							cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lossReserveretainedOc")), 0.0)
									.otherwise(root.<Double>get("lossReserveretainedOc")),
							cb.<Double>selectCase().when(cb.isNull(root.<Double>get("lrdAllocatedTillDate")), 0.0)
									.otherwise(root.<Double>get("lrdAllocatedTillDate"))),
					cb.equal(root.get("contractNo"), req.getContNo()),
					cb.equal(root.get("subClass"), req.getDepartmentId()),
					cb.lessThanOrEqualTo(root.get("transactionMonthYear"), req.getTransaction()));
		}

		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getCurrencyName(String branchCode, String currencyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<CurrencyMaster> root = cq.from(CurrencyMaster.class);
		
		Subquery<Integer> amendSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> amendSubRoort = amendSq.from(CurrencyMaster.class);

		amendSq.select(cb.max(amendSubRoort.get("amendId"))).where(
				cb.equal(amendSubRoort.get("branchCode"), root.get("branchCode")),
				cb.equal(amendSubRoort.get("currencyId"), root.get("currencyId")),
				cb.equal(amendSubRoort.get("status"), "Y"));
		
		
		cq.multiselect(root.get("shortName").alias("SHORT_NAME"))
		.where(cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("currencyId"), currencyId),
				cb.equal(root.get("status"), "Y"),
				cb.equal(root.get("amendId"), amendSq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getAllocatedCasLoss(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnCashLossCredit> root = cq.from(TtrnCashLossCredit.class);
		
		cq.multiselect(root.get("credittrxnno").alias("CREDITTRXNNO"),
				root.get("contractNo").alias("CONTRACT_NO"),
				root.get("claimNo").alias("CLAIM_NO"),
				root.get("claimpaymentNo").alias("CLAIMPAYMENT_NO"),
				root.get("creditdate").alias("CREDITDATE"),
				root.get("cldcurrencyId").alias("CLDCURRENCY_ID"),
				root.get("cldAmount").alias("CLD_AMOUNT"),
				root.get("clccurrencyId").alias("CLCCURRENCY_ID"),
				root.get("creditamountclc").alias("CREDITAMOUNTCLC"),
				root.get("creditamountcld").alias("CREDITAMOUNTCLD"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"))
		.where(cb.equal(root.get("proposalNo"), proposalNo),
				cb.equal(root.get("status"), "A"));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getAllocatedTransList(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnCashLossCredit> root = cq.from(TtrnCashLossCredit.class);
		
		cq.multiselect(root.get("credittrxnno").alias("CREDITTRXNNO"),
				root.get("contractNo").alias("CONTRACT_NO"))
		.where(cb.equal(root.get("proposalNo"), proposalNo),
				cb.equal(root.get("status"), "A"));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getCashLossCreadt(String contNo, String departmentId, String claimPayNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPayment> tcpRoot = cq.from(TtrnClaimPayment.class);
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		
		Expression<Double> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
						.otherwise(tcpRoot.<Double>get("paidAmountOc")),
				cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("cashLossSettledTilldate")), 0.0)
						.otherwise(tcpRoot.<Double>get("cashLossSettledTilldate")));
		
		cq.multiselect(//root.get("rownum").alias("sno"), // Need to check
				tcpRoot.get("contractNo").alias("CONTRACT_NO"),
				tcpRoot.get("claimNo").alias("CLAIM_NO"),
				tcpRoot.get("claimPaymentNo").alias("CLAIM_PAYMENT_NO"),
				tcpRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				exp.alias("PAID_AMOUNT_OC"),
				tcdRoot.get("currency").alias("CURRENCY_ID"))
		.where(cb.greaterThan(exp, cb.literal(0.0)),
			   cb.equal(tcpRoot.get("contractNo"), contNo),
			   cb.equal(tcdRoot.get("subClass"), departmentId),
			   cb.equal(tcdRoot.get("contractNo"), tcpRoot.get("contractNo")),
			   cb.equal(tcdRoot.get("claimNo"), tcpRoot.get("claimNo")),
			   cb.equal(tcpRoot.get("claimPaymentNo"), claimPayNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getExcessRatePercent() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<ConstantDetail> root = cq.from(ConstantDetail.class);
		
		cq.multiselect(root.get("percent").as(String.class).alias("PERCENT"))
		.where(cb.equal(root.get("categoryId"), "50"),
				cb.equal(root.get("categoryDetailId"), "1"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Map<String, Object>> currencyList(String branchCode) {
		List<String> columns = new ArrayList<String>(Arrays.asList("CURRENCY_ID","SHORT_NAME"));
			
			List<Map<String,Object>> resultList = new ArrayList<>();
			
			String nativeQuery = "select CURRENCY_ID,SHORT_NAME from currency_master where"
					+ " BRANCH_CODE=? and CURRENCY_ID in (SELECT Distinct(REGEXP_SUBSTR"
					+ "(BANK_CURRENCY,'[^,]+',1,LEVEL)) lc_login FROM (SELECT DISTINCT"
					+ " BANK_CURRENCY FROM bank_master WHERE BRANCH_CODE=? AND STATUS='Y') B "
					+ "CONNECT BY LEVEL <= LENGTH(BANK_CURRENCY) - LENGTH(REPLACE(BANK_CURRENCY,"
					+ "',',''))+1 and BANK_CURRENCY is not null)"; 
			
		    Query query = em.createNativeQuery(nativeQuery);
		    query.setParameter(1,branchCode);
		    query.setParameter(2,branchCode);

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

	@Override
	public List<Tuple> premiumSelectGetAlloTransaction(String contNo, String transactionNo) {
		List<Tuple> resultList = tprAllcoatedTransaction(contNo, transactionNo);
		if(Objects.nonNull(resultList))
			resultList.addAll(tatAllcoatedTransaction(contNo, transactionNo));
		else
			resultList = tatAllcoatedTransaction(contNo, transactionNo);
	return Objects.isNull(resultList) ? new ArrayList<>() : resultList;
	//Need order by -- in xol too
	}
	
	private List<Tuple> tprAllcoatedTransaction(String contNo, String transactionNo){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> tatRoot = cq.from(TtrnAllocatedTransaction.class);
		Root<TtrnPaymentReceipt> tprRoot = cq.from(TtrnPaymentReceipt.class);
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> piSubRoot = piAmendSq.from(TtrnPaymentReceipt.class);

		piAmendSq.select(cb.max(piSubRoot.get("amendId")))
				.where(cb.equal(piSubRoot.get("paymentReceiptNo"), tprRoot.get("paymentReceiptNo")),
					   cb.equal(piSubRoot.get("branchCode"), tprRoot.get("branchCode")));
		
		cq.multiselect(//tatRoot.get("sNo").alias("SNO"), 
				tatRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				tatRoot.get("transactionNo").alias("TRANSACTION_NO"),
				tatRoot.get("productName").alias("PRODUCT_NAME"),
				tatRoot.get("type").alias("TYPE"),
				tatRoot.get("paidAmount").alias("PAID_AMOUNT"),
				tatRoot.get("currencyId").alias("CURRENCY_ID"),
				cb.selectCase().when(cb.equal(tatRoot.get("status"), "R"), "Reverted")
							   .when(cb.equal(tatRoot.get("status"), "Y"), "Allocated").alias("STATUS"),
			    tatRoot.get("receiptNo").alias("RECEIPT_NO"),
			    cb.selectCase().when(cb.equal(tprRoot.get("transType"), "PT"), "PAYMENT")
				   .when(cb.equal(tprRoot.get("transType"), "RT"), "RECEIPT").alias("TRANS_TYPE"),
			   tprRoot.get("transType").alias("ALLOCATE_TYPE"))
		
		.where(cb.equal(tatRoot.get("receiptNo"), tprRoot.get("paymentReceiptNo")),
			   cb.equal(tatRoot.get("contractNo"), contNo),
			   cb.equal(tatRoot.get("transactionNo"), transactionNo),
			   cb.equal(tatRoot.get("type"), "P"),
			   cb.equal(tatRoot.get("status"), "Y"),
			   cb.equal(tprRoot.get("amendId"), piAmendSq));
		
		return em.createQuery(cq).getResultList();
	}
	
	private List<Tuple> tatAllcoatedTransaction(String contNo, String transactionNo){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> tatRoot = cq.from(TtrnAllocatedTransaction.class);
		
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("paymentReceiptNo")))
				.where(cb.equal(tatRoot.get("receiptNo"), subRoot.get("paymentReceiptNo")));
		
		cq.multiselect(//tatRoot.get("sNo").alias("SNO"), 
				tatRoot.get("inceptionDate").alias("INCEPTION_DATE"),
				tatRoot.get("transactionNo").alias("TRANSACTION_NO"),
				tatRoot.get("productName").alias("PRODUCT_NAME"),
				tatRoot.get("type").alias("TYPE"),
				tatRoot.get("paidAmount").alias("PAID_AMOUNT"),
				tatRoot.get("currencyId").alias("CURRENCY_ID"),
				cb.literal("Adjusted").alias("STATUS"),
			    tatRoot.get("receiptNo").alias("RECEIPT_NO"),
			    cb.selectCase().when(cb.equal(tatRoot.get("adjustmentType"), "W"), "Write Off")
				   .when(cb.equal(tatRoot.get("adjustmentType"), "R"), "Round Off").alias("TRANS_TYPE"),
				cb.literal("").alias("ALLOCATE_TYPE"))
		
		.where(cb.equal(tatRoot.get("contractNo"), contNo),
			   cb.equal(tatRoot.get("transactionNo"), transactionNo),
			   cb.equal(tatRoot.get("type"), "P"),
			   cb.equal(tatRoot.get("status"), "Y"),
			   cb.not(cb.in(tatRoot.get("receiptNo")).value(sq)));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> brokerCedingName(String contNo, String branchCode) {
		List<Tuple> resultList = ttrnBrokerCedingName(contNo, branchCode);
			if(Objects.nonNull(resultList))
				resultList.addAll(piBrokerCedingName(contNo, branchCode));
			else
				resultList = piBrokerCedingName(contNo, branchCode);
		return Objects.isNull(resultList) ? new ArrayList<>() : resultList;
	}
	
	private List<Tuple> ttrnBrokerCedingName(String contNo, String branchCode){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piSubRoot.get("amendId")))
				.where(cb.equal(piSubRoot.get("customerId"), piRoot.get("customerId")),
					   cb.equal(piSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.<Long>get("rskEndorsementNo")))
				.where(cb.equal(rkRoot.get("rskContractNo"), endoSubRoot.get("rskContractNo")));
		
		cq.multiselect(piRoot.get("customerId").alias("CUSTOMER_ID"), 
				piRoot.get("companyName").alias("BROKER"),
				cb.concat(cb.concat(piRoot.get("address1"), " "), piRoot.get("address2")).alias("ADDRESS"))
		
		.where(cb.equal(rkRoot.get("rskContractNo"), contNo),
			   cb.equal(rkRoot.get("rskCedingid"), piRoot.get("customerId")),
			   cb.equal(piRoot.get("branchCode"), branchCode),
			   cb.equal(piRoot.get("customerType"), "C"),
			   cb.equal(piRoot.get("amendId"), piAmendSq),
			   cb.equal(rkRoot.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getResultList();
	}
	
	private List<Tuple> piBrokerCedingName(String contNo, String branchCode){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piSubRoot.get("amendId")))
				.where(cb.equal(piSubRoot.get("customerId"), piRoot.get("customerId")),
					   cb.equal(piSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.<Long>get("rskEndorsementNo")))
				.where(cb.equal(rkRoot.get("rskContractNo"), endoSubRoot.get("rskContractNo")));
		
		cq.multiselect(piRoot.get("customerId").alias("CUSTOMER_ID"), 
				cb.concat(cb.concat(piRoot.get("firstName"), " "), piRoot.get("lastName")).alias("BROKER"),
				cb.concat(cb.concat(piRoot.get("address1"), " "), piRoot.get("address2")).alias("ADDRESS"))
		
		.where(cb.equal(rkRoot.get("rskContractNo"), contNo),
			   cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
			   cb.equal(piRoot.get("branchCode"), branchCode),
			   cb.equal(piRoot.get("customerType"), "B"),
			   cb.notEqual(piRoot.get("customerId"), 63),
			   cb.equal(piRoot.get("amendId"), piAmendSq),
			   cb.equal(rkRoot.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getDepartmentNo(String contractNo) {
		CriteriaBuilder cb= em.getCriteriaBuilder();	
		CriteriaQuery<String> query1 = cb.createQuery(String.class);
		
		//Find All
		Root<TtrnRiskDetails> rd = query1.from(TtrnRiskDetails.class); 
		
		//Select
		query1.multiselect(rd.get("rskDeptid").as(String.class));
		
		//Max EndorsementNo
		Subquery<Long> maxEndorsment = query1.subquery(Long.class);
		Root<TtrnRiskDetails> maxEndorsmentId = maxEndorsment.from(TtrnRiskDetails.class);
		
		maxEndorsment.select(cb.max(maxEndorsmentId.get("rskEndorsementNo")));	
		Predicate e1 = cb.equal(maxEndorsmentId.get("rskContractNo"), rd.get("rskContractNo"));		
		Predicate e2 = cb.equal(maxEndorsmentId.get("rskProposalNumber"),rd.get("rskProposalNumber") );
		maxEndorsment.where(e1,e2);	
		
		//Where
		Predicate n1 = cb.equal(rd.get("rskContractNo"), contractNo);	
		Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), maxEndorsment);	
		query1.where(n1,n2); //just pass
		
		//Get Result
		return em.createQuery(query1).getSingleResult(); 
	}

	@Override
	public String getSumOfShareSign(String retroContractNo) {
		int noOfRetroCess=0;
		
		//Criteria
		CriteriaBuilder cb= em.getCriteriaBuilder();	
		CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class); 
		
		//Find All
		Root<TtrnRiskDetails> tr = query.from(TtrnRiskDetails.class); 
		
		//Select
		query.select(tr.get("retroCessionaries"));	
		
		// EndorsmentNoRD
		Subquery<Long> maxEndorsmentRD = query.subquery(Long.class);
		Root<TtrnRiskDetails> maxEndorsmentIdRD = maxEndorsmentRD.from(TtrnRiskDetails.class);
		
		maxEndorsmentRD.select(cb.max(maxEndorsmentIdRD.get("rskEndorsementNo")));	
		Predicate p1 = cb.equal(maxEndorsmentIdRD.get("rskContractNo"), tr.get("rskContractNo"));		
		maxEndorsmentRD.where(p1);										
		
		//Where
		Predicate n1 = cb.equal(tr.get("rskContractNo"), retroContractNo);	
		Predicate n2 = cb.equal(tr.get("rskEndorsementNo"), maxEndorsmentRD);	
		query.where(n1,n2); 
		
		//Get Result
		TypedQuery<BigDecimal> result = em.createQuery(query); 
		List<BigDecimal> list1 = result.getResultList();
		noOfRetroCess = Integer.valueOf(list1.get(0) == null ? "" : String.valueOf(list1.get(0)));
		//Criteria
		CriteriaQuery<String> query1 = cb.createQuery(String.class); 
		
		//Find All
		Root<TtrnRetroCessionary> rc = query1.from(TtrnRetroCessionary.class); 
		
		//Select
		query1.multiselect(rc.get("shareSigned").as(String.class));	
		
		// maxAmend
		Subquery<Long> maxAmend = query1.subquery(Long.class);
		Root<TtrnRetroCessionary> amend = maxAmend.from(TtrnRetroCessionary.class);
		
		maxAmend.select(cb.max(amend.get("amendId")));	
		Predicate a1 = cb.equal(amend.get("contractNo"), rc.get("contractNo"));		
		Predicate a2 = cb.equal(amend.get("proposalStatus"), rc.get("proposalStatus"));		
		Predicate a3 = cb.equal(amend.get("sno"), rc.get("sno"));		
		maxAmend.where(a1,a2,a3);										
		
		//Where
		Predicate m1 = cb.equal(rc.get("amendId"), maxAmend);	
		Predicate m2 = cb.equal(rc.get("contractNo"), retroContractNo);	
		Predicate m3 = cb.equal(rc.get("proposalStatus"), "A");	
		Predicate m4 = cb.greaterThanOrEqualTo(rc.get("sno"), "0");	
		Predicate m5 = cb.lessThanOrEqualTo(rc.get("sno"), noOfRetroCess);	
		
		//orderBy
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(rc.get("sno")));	
		query1.where(m1,m2,m3,m4,m5).orderBy(orderList);

		
		//Get Result
		return em.createQuery(query1).getSingleResult(); 
	}

	@Override
	public List<Tuple> premiumSelectInsDetails(String proposalNo, String noOfRetro) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnInsurerDetails> root = cq.from(TtrnInsurerDetails.class);
		

		//Select
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("retroPercentage").alias("RETRO_PERCENTAGE"),
				root.get("type").alias("TYPE"),
				root.get("uwYear").alias("UW_YEAR"),
				root.get("retroType").alias("RETRO_TYPE"));	
		
		// EndorsmentNoRD
		Subquery<Long> maxEndorsmentNo = cq.subquery(Long.class);
		Root<TtrnInsurerDetails> maxEndorsmentId = maxEndorsmentNo.from(TtrnInsurerDetails.class);
		
		maxEndorsmentNo.select(cb.max(maxEndorsmentId.get("endorsementNo")));	
		Predicate p1 = cb.equal(maxEndorsmentId.get("proposalNo"), root.get("proposalNo"));	
		Predicate p2 = cb.equal(maxEndorsmentId.get("insurerNo"), root.get("insurerNo"));
		maxEndorsmentNo.where(p1,p2);	
		
		//Where
		Predicate n1 = cb.equal(root.get("endorsementNo"), maxEndorsmentNo);	//max
		Predicate n2 = cb.equal(root.get("proposalNo"), proposalNo);	
		Predicate n3 = cb.greaterThanOrEqualTo(root.get("insurerNo"),"0");
		Predicate n4 = cb.lessThanOrEqualTo(root.get("insurerNo"),noOfRetro);
		//Predicate date =  cb.between(root.get("date"), dateBefore, dateAfter);
		
		//orderBy
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(root));	
		cq.where(n1,n2,n3,n4).orderBy(orderList);
		
		//Get Result
		return em.createQuery(cq).getResultList(); 
	}

	@Override
	public Integer getCleanCutContCount(String contractNo) {
		Integer count = null;

		// Criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query1 = cb.createQuery(Long.class);

		// Find All
		Root<TtrnRiskDetails> rd = query1.from(TtrnRiskDetails.class);

		// Select
		query1.multiselect(rd.get("rskContractNo").alias("rskContractNo"));

		// EndorsmentNoRD
		Subquery<Long> maxEndorsmentRD = query1.subquery(Long.class);
		Root<TtrnRiskDetails> maxEndorsmentIdRD = maxEndorsmentRD.from(TtrnRiskDetails.class);

		maxEndorsmentRD.select(cb.max(maxEndorsmentIdRD.get("rskEndorsementNo")));
		Predicate p1 = cb.equal(maxEndorsmentIdRD.get("rskProposalType"), "C");
		Predicate p2 = cb.equal(maxEndorsmentIdRD.get("rskContractNo"), rd.get("rskContractNo"));
		maxEndorsmentRD.where(p1, p2);

		// Where
		Predicate n1 = cb.equal(rd.get("rskProposalType"), "C");
		Predicate n2 = cb.equal(rd.get("rskContractNo"), contractNo);
		Predicate n3 = cb.equal(rd.get("rskEndorsementNo"), maxEndorsmentRD); // max
		query1.where(n1, n2, n3);

		// Get Result
		TypedQuery<Long> result = em.createQuery(query1); // res class datatype must match with entity
		count = result.getResultList().size();

		if (count > 0) {
			// Criteria
			CriteriaQuery<Tuple> query2 = cb.createQuery(Tuple.class);

			// Find All
			Root<RskPremiumDetails> rpd = query2.from(RskPremiumDetails.class);

			// Select
			query2.multiselect(rpd);

			// contract No rdd
			Subquery<Long> contractNord = query2.subquery(Long.class);
			Root<TtrnRiskDetails> cn = contractNord.from(TtrnRiskDetails.class);

			// maxEndNoRD
			Subquery<Long> maxEndNoRD = query2.subquery(Long.class);
			Root<TtrnRiskDetails> maxEndNoIdRD = maxEndNoRD.from(TtrnRiskDetails.class);

			maxEndNoRD.select(cb.max(maxEndNoIdRD.get("rskEndorsementNo")));
			Predicate x1 = cb.equal(maxEndNoIdRD.get("rskProposalType"), "C");
			Predicate x2 = cb.equal(maxEndNoIdRD.get("rskContractNo"), cn.get("rskContractNo"));
			maxEndNoRD.where(x1, x2);

			contractNord.select(cn.get("rskContractNo"));
			Predicate b1 = cb.equal(cn.get("rskProposalType"), "C");
			Predicate b2 = cb.equal(cn.get("rskContractNo"), contractNo);
			Predicate b3 = cb.equal(cn.get("rskEndorsementNo"), maxEndNoRD);
			contractNord.where(b1, b2, b3);

			// Where
			Predicate r1 = cb.equal(rpd.get("accountPeriodQtr"), "5");
			// In
			Expression<String> e0 = rpd.get("contractNo");
			Predicate r2 = e0.in(contractNord);
			query2.where(r1, r2);

			// Get Result
			TypedQuery<Tuple> result2 = em.createQuery(query2); // res class datatype must match with entity
			count = result2.getResultList().size();
		}
		return count;
	}

	@Override
	public List<Map<String, Object>> getOSBList(String transaction, String contractNo, String branchCode) {
		List<String> columns = new ArrayList<String>(Arrays.asList("CONTRACT_NO","LAYER_NO", 
				"RSK_INSURED_NAME DEPTID", "TRANSACTION_NO", "ACCOUNT_PERIOD_QTR" ,"STATEMENT_DATE",
				"SUB_CLASS", "CURRENCY_ID", "SECTION_NAME", "OSCLAIM_LOSSUPDATE_OC",
				"TRANSACTION_DATE", "ACCOUNTING_PERIOD_DATE"));
		
		List<Map<String,Object>> resultList = new ArrayList<>();
		
		String nativeQuery = "SELECT CONTRACT_NO,LAYER_NO, RSK_INSURED_NAME DEPTID,"
				+ "TRANSACTION_NO,ACCOUNT_PERIOD_QTR ,STATEMENT_DATE STATEMENT_DATE ,"
				+ "RSK_SPFCID SUB_CLASS,COMPANY_CODE CURRENCY_ID,BROKER_CODE SECTION_NAME,"
				+ "to_number(AMOUNT_OC) OSCLAIM_LOSSUPDATE_OC,TRANSACTION_DATE,"
				+ "ACCOUNTING_PERIOD_DATE FROM TABLE(FN_RENEWAL_STATISTICS(? ,'','','',?,?,'OSLR') ) "
				+ "ORDER BY CURRENCY"; 
		
	    Query query = em.createNativeQuery(nativeQuery);
	    query.setParameter(1,transaction);
	    query.setParameter(2,contractNo);
	    query.setParameter(3,branchCode);

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

	@Override
	public List<Tuple> premiumSelectGetTreatySPRetro(String contNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskProposal> rpRoot = cq.from(TtrnRiskProposal.class);
		Root<TtrnRiskDetails> rdRoot = cq.from(TtrnRiskDetails.class);
		
		Subquery<Integer> rpEndoSq = cq.subquery(Integer.class);
		Root<TtrnRiskProposal> rpEndoSubRoot = rpEndoSq.from(TtrnRiskProposal.class);

		rpEndoSq.select(cb.max(rpEndoSubRoot.get("rskEndorsementNo")))
				.where(cb.equal(rpEndoSubRoot.get("rskProposalNumber"), rpRoot.get("rskProposalNumber")));
		
		Subquery<Integer> rdEndoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> rdEndoSubRoot = rdEndoSq.from(TtrnRiskDetails.class);

		rdEndoSq.select(cb.max(rdEndoSubRoot.get("rskEndorsementNo")))
				.where(cb.equal(rdEndoSubRoot.get("rskContractNo"), rdRoot.get("rskContractNo")));

		
		cq.multiselect(rpRoot.get("rskSpRetro").alias("RSK_SP_RETRO"),
				rpRoot.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),
				rpRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"))
		
		.where(cb.equal(rpRoot.get("rskProposalNumber"), rdRoot.get("rskProposalNumber")),
				cb.equal(rpRoot.get("rskEndorsementNo"), rpEndoSq),
				cb.equal(rdRoot.get("rskContractNo"), contNo),
				cb.equal(rdRoot.get("rskEndorsementNo"), rdEndoSq));
		
		return em.createQuery(cq).getResultList(); 
		
	}

	@Override
	public String premiumSelectRLNO(String branchCode) {
		
		int val = 0;
		try{
		List<TtrnDepositRelease> list = ttrnrepos.findTop1ByBranchCodeOrderByRlNoDesc(branchCode);
		if(!CollectionUtils.isEmpty(list)) {
		 val = (list.get(0).getRlNo()==null?5001: Integer.valueOf(list.get(0).getRlNo().toString()))+1;
		}else {
			val=5001;
		}
		}catch(Exception e){
		e.printStackTrace();
		}

		return String.valueOf(val);	
	}
	@Transactional
	@Override
	public Integer updateLossReserve(String contractNo ,String requestNo, String transactionNo, String tableMoveStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);
		
		Subquery<Double> sq = update.subquery(Double.class);
		Root<TtrnDepositRelease> subRoot = sq.from(TtrnDepositRelease.class);

		sq.select(cb.<Double>selectCase().when(cb.isNull(cb.sum(subRoot.get("rlAmountInRtCurr"))), 0.0)
				.otherwise(cb.sum(subRoot.get("rlAmountInRtCurr"))))
		
		.where(cb.equal(subRoot.get("contractNo"), contractNo), 
				cb.equal(subRoot.get("tempRequestNo"), requestNo), 
				cb.equal(subRoot.get("tableMoveStatus"), tableMoveStatus),
				cb.equal(subRoot.get("releaseType"), "LRR"));
		
		Expression<Double> exp = cb.sum(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
				.otherwise(root.<Double>get("prdAllocatedTillDate")), sq);
		
		update.set(root.<Double>get("lrdAllocatedTillDate"), exp);
		update.where(cb.equal(root.get("contractNo"), contractNo),
				     cb.equal(root.get("transactionNo"), transactionNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	@Transactional
	@Override
	public Integer updatePremiumReserve(String contractNo ,String requestNo, String transactionNo, String tableMoveStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);
		
		Subquery<Double> sq = update.subquery(Double.class);
		Root<TtrnDepositRelease> subRoot = sq.from(TtrnDepositRelease.class);

		sq.select(cb.<Double>selectCase().when(cb.isNull(cb.sum(subRoot.get("rlAmountInRtCurr"))), 0.0)
				.otherwise(cb.sum(subRoot.get("rlAmountInRtCurr"))))
		
		.where(cb.equal(subRoot.get("contractNo"), contractNo), 
				cb.equal(subRoot.get("tempRequestNo"), requestNo), 
				cb.equal(subRoot.get("tableMoveStatus"), tableMoveStatus),
				cb.equal(subRoot.get("releaseType"), "PRR"));
		
		Expression<Double> exp = cb.sum(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("prdAllocatedTillDate")), 0.0)
				.otherwise(root.<Double>get("prdAllocatedTillDate")), sq);
				
		update.set(root.<Double>get("lrdAllocatedTillDate"), exp);
		update.where(cb.equal(root.get("contractNo"), contractNo),
				     cb.equal(root.get("transactionNo"), transactionNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public void premiumSpRetroSplit(InsertPremiumReq input) {
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
			sp.setParameter("pvContractNo", input.getContNo());
			sp.setParameter("pnLayerNo", StringUtils.isEmpty(input.getLayerno())? 0 :input.getLayerno());
			sp.setParameter("pnProductId", Integer.parseInt(input.getProductId()));
			sp.setParameter("pnPremiumTranNo", Double.parseDouble(input.getTransactionNo()));
			sp.setParameter("pdPremTranDate", formatDate(input.getTransaction()));
			sp.setParameter("pnCurrencyId", Integer.parseInt("3".equalsIgnoreCase(input.getCurrencyId()) ? input.getCurrencyId() : input.getCurrency()));
			sp.setParameter("pnExchange", Double.parseDouble(input.getExchRate()));
			sp.setParameter("pnBranchCode", input.getBranchCode());

			sp.setParameter("pvtransactionType", "P");
			sp.setParameter("pdAmendDate", input.getAmendmentDate()==null?"":input.getAmendmentDate());
			sp.setParameter("pnReference", "");
			sp.setParameter("pnTreatyName", "");
			sp.setParameter("pnRemarks", "");
			sp.setParameter("pnUwYear", "");
			sp.setParameter("pnSubClass", "");
			sp.setParameter("retroCession", input.getRiCession());
			// execute SP
			System.out.println("pvContractNo: "+sp.getParameterValue("pvContractNo"));
			System.out.println("pnLayerNo: "+sp.getParameterValue("pnLayerNo"));
			System.out.println("pnProductId: "+sp.getParameterValue("pnProductId"));
			System.out.println("pnPremiumTranNo: "+sp.getParameterValue("pnPremiumTranNo"));
			System.out.println("pdPremTranDate: "+sp.getParameterValue("pdPremTranDate"));
			System.out.println("pnCurrencyId: "+sp.getParameterValue("pnCurrencyId"));
			System.out.println("pnExchange: "+sp.getParameterValue("pnExchange"));
			System.out.println("pnBranchCode: "+sp.getParameterValue("pnBranchCode"));
			System.out.println("pvtransactionType: "+sp.getParameterValue("pvtransactionType"));
			System.out.println("pdAmendDate: "+sp.getParameterValue("pdAmendDate"));
			System.out.println("pnReference: "+sp.getParameterValue("pnReference"));
			System.out.println("pnTreatyName: "+sp.getParameterValue("pnTreatyName"));
			System.out.println("pnRemarks: "+sp.getParameterValue("pnRemarks"));
			System.out.println("pnUwYear: "+sp.getParameterValue("pnUwYear"));
			System.out.println("pnSubClass: "+sp.getParameterValue("pnSubClass"));
			System.out.println("retroCession: "+sp.getParameterValue("retroCession"));
			sp.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public RskPremiumDetailsTemp facPremiumTempToMain(String requestNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RskPremiumDetailsTemp> cq = cb.createQuery(RskPremiumDetailsTemp.class);
		Root<RskPremiumDetailsTemp> root = cq.from(RskPremiumDetailsTemp.class);
		
		cq.multiselect((root).alias("RskPremiumDetailsTemp"))
		.where(cb.equal(root.get("requestNo"), requestNo),
			   cb.equal(root.get("branchCode"), branchCode));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String getSeqName(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<SequenceMaster> root = cq.from(SequenceMaster.class);
		
		cq.multiselect(root.get("seqName").alias("SEQ_NAME"))
		.where(cb.equal(root.get("branchCode"), branchCode),
			   cb.equal(root.get("seqId"), "20"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String getLpad(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);

		Expression<String> expressionToGetPaddedId = cb.function("LPAD", String.class, cb.literal(name + ".nextval"),
				cb.literal(6), cb.literal(0));

		cq.multiselect(expressionToGetPaddedId);
		return em.createQuery(cq).getSingleResult();
	}
	@Transactional
	@Override
	public Integer facTempStatusUpdate(InsertPremiumReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsTemp> update = cb.createCriteriaUpdate(RskPremiumDetailsTemp.class);
		Root<RskPremiumDetailsTemp> root = update.from(RskPremiumDetailsTemp.class);

		update.set(root.get("transStatus"), "A")
			  .set(root.get("reviewerId"), req.getLoginId())
			  .set(root.get("reviewDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
			  .set(root.get("transactionNo"), new BigDecimal(req.getTransactionNo())==null?"": new BigDecimal(req.getTransactionNo()));

		update.where(cb.equal(root.get("requestNo"), req.getRequestNo()),
					 cb.equal(root.get("branchCode"), req.getBranchCode()));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public List<Tuple> premiumSelectTreatyContDet(com.maan.insurance.model.req.premium.ContractDetailsReq req) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<TmasPfcMaster> pfcRoot = cq.from(TmasPfcMaster.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		//Root<TmasPolicyBranch> branchRoot = cq.from(TmasPolicyBranch.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cmRoot = cq.from(CurrencyMaster.class);
		Root<PositionMaster> pmroot = cq.from(PositionMaster.class);
		Expression<String> nameExpression = cb.concat(cb.concat(piRoot.get("firstName"), " "),
				piRoot.get("lastName"));
		
		Expression<String> addressExpression = cb.concat(cb.concat(personalRoot.get("address1"), " "),
				personalRoot.get("address2"));
		
		Subquery<String> tmasDeptSq = cq.subquery(String.class);
		Root<TmasDepartmentMaster> tmasSubRoot = tmasDeptSq.from(TmasDepartmentMaster.class);

		tmasDeptSq.select(tmasSubRoot.get("tmasDepartmentName"))
				.where(cb.equal(tmasSubRoot.get("tmasDepartmentId"), rkRoot.get("rskDeptid")),
					   cb.equal(tmasSubRoot.get("branchCode"), rkRoot.get("branchCode")),
					   cb.equal(tmasSubRoot.get("tmasProductId"), req.getProductId()),
					   cb.equal(tmasSubRoot.get("tmasStatus"), "Y"));
		
		
		cq.multiselect(rkRoot.get("treatytype").alias("TREATYTYPE"), 
				rkRoot.get("rskDeptid").alias("RSK_DEPTID"),
				rkRoot.get("rskSpfcid").alias("RSK_SPFCID"),
				rkRoot.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				rkRoot.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
				pfcRoot.get("tmasPfcName").alias("TMAS_PFC_NAME"),
				cb.literal("RTRIM").alias("TMAS_SPFC_NAME"), // need to be replaced
				personalRoot.get("companyName").alias("COMPANY"),
				rkRoot.get("rskTreatyid").alias("RSK_TREATYID"),
				nameExpression.alias("BROKER"),
				rkRoot.get("rskInceptionDate").alias("INS_DATE"),
				rkRoot.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
				rkRoot.get("rskExpiryDate").alias("EXP_DATE"),
				rkRoot.get("rskMonth").alias("MONTH"),
				//branchRoot.get("tmasPolBranchName").alias("TMAS_POL_BRANCH_NAME"),	
				rkRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
				rkRoot.get("rskUwyear").alias("RSK_UWYEAR"),
				rkRoot.get("rskLayerNo").alias("RSK_LAYER_NO"),
				rkRoot.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),
				cmRoot.get("shortName").alias("CURRENCY_NAME"),
				addressExpression.alias("Address"),
				rkRoot.get("inwardBusType").alias("INWARD_BUS_TYPE"),
				tmasDeptSq.alias("TMAS_DEPARTMENT_NAME"));
		
		Subquery<Integer> amendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> amendSubRoort = amendSq.from(PersonalInfo.class);

		amendSq.select(cb.max(amendSubRoort.get("amendId"))).where(
				cb.equal(amendSubRoort.get("customerId"), personalRoot.get("customerId")),
				cb.equal(amendSubRoort.get("branchCode"), personalRoot.get("branchCode")));
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piAmendSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piAmendSubRoot.get("amendId"))).where(
				cb.equal(piAmendSubRoot.get("customerId"), piRoot.get("customerId")),
				cb.equal(piAmendSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskContractNo"), req.getContNo()),
				cb.equal(endoSubRoot.get("rskDeptid"), rkRoot.get("rskDeptid")));
		
		Subquery<Integer> cmAmendSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cmAmendSubRoot = cmAmendSq.from(CurrencyMaster.class);

		cmAmendSq.select(cb.max(cmAmendSubRoot.get("amendId"))).where(
				cb.equal(cmAmendSubRoot.get("currencyId"), cmRoot.get("currencyId")),
				cb.equal(cmAmendSubRoot.get("branchCode"), cmRoot.get("branchCode")));
		
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), req.getContNo()),
			   //cb.equal(rkRoot.get("rskDeptid"), req.getDepartmentId()),
			   cb.equal(pfcRoot.get("tmasPfcId"), rkRoot.get("rskPfcid")),
			   cb.equal(pfcRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(rkRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(rkRoot.get("rskProductid"), req.getProductId()),
			   cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
			   cb.equal(personalRoot.get("customerType"), "C"),
			   cb.equal(personalRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(personalRoot.get("amendId"), amendSq),
			   cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
			   cb.equal(piRoot.get("customerType"), "B"),
			   cb.equal(piRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(piRoot.get("amendId"), piAmendSq),
			   cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
			  // cb.equal(rkRoot.get("rskPolbranch"), branchRoot.get("tmasPolBranchId")),
			   cb.equal(rkRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(cmRoot.get("currencyId"), rkRoot.get("rskOriginalCurr")),
			   cb.equal(cmRoot.get("branchCode"), piRoot.get("branchCode")),
			   cb.equal(rkRoot.get("rskProposalNumber"), pmroot.get("proposalNo")),
			   cb.equal(pmroot.get("sectionNo"), req.getSectionNo()),
			   cb.equal(cmRoot.get("amendId"), cmAmendSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectCommissionDetails(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskCommission> root = cq.from(TtrnRiskCommission.class);
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskCommission> endoSubRoot = endoSq.from(TtrnRiskCommission.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskProposalNumber"), proposalNo));
		
		cq.multiselect(root.get("rskOurAssAcqCost").alias("RSK_OUR_ASS_ACQ_COST"), 
				root.get("rskComm").alias("RSK_COMM"),
				root.get("rskCommQuotashare").alias("RSK_COMM_QUOTASHARE"),
				root.get("rskCommSurplus").alias("RSK_COMM_SURPLUS"),
				root.get("rskOverriderPerc").alias("RSK_OVERRIDER_PERC"),
				root.get("rskBrokerage").alias("RSK_BROKERAGE"),
				root.get("rskTax").alias("RSK_TAX"),
				root.get("rskLossReserve").alias("RSK_LOSS_RESERVE"),
				root.get("rskOtherCost").alias("RSK_OTHER_COST"),
				root.get("rskAcquistionCostOc").alias("RSK_ACQUISTION_COST_OC"),
				root.get("rskManagementExpenses").alias("RSK_MANAGEMENT_EXPENSES"),
				root.get("rskProfitComm").alias("RSK_PROFIT_COMM"),
				cb.selectCase().when(cb.isNull(root.get("rskPremiumReserve")), 0)
							   .otherwise(root.get("rskPremiumReserve")).alias("RSK_PREMIUM_RESERVE"))
		
		.where(cb.equal(root.get("rskProposalNumber"), proposalNo),
			   cb.equal(root.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> premiumSelectTreatyProposalDetails(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskProposal> fRoot = cq.from(TtrnRiskProposal.class);
		Root<TtrnRiskDetails> dRoot = cq.from(TtrnRiskDetails.class);
		
		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(fRoot.<Double>get("rskEpiOsofOc")), 0.0)
							.otherwise(fRoot.<Double>get("rskEpiOsofOc")), 
			    cb.<Double>selectCase().when(cb.isNull(fRoot.<Double>get("rskEpiOsofOc")), 0.0)
			   				.otherwise(fRoot.<Double>get("rskEpiOsofOc")));
		
		Subquery<Integer> dEndoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> dEndoSubRoot = dEndoSq.from(TtrnRiskDetails.class);

		dEndoSq.select(cb.max(dEndoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(dEndoSubRoot.get("rskProposalNumber"), dRoot.get("rskProposalNumber")));
		
		Subquery<Integer> fEndoSq = cq.subquery(Integer.class);
		Root<TtrnRiskProposal> fEndoSubRoot = fEndoSq.from(TtrnRiskProposal.class);

		fEndoSq.select(cb.max(fEndoSubRoot.get("rskEndorsementNo")))
		.where( cb.equal(fEndoSubRoot.get("rskProposalNumber"), proposalNo),
				cb.isNotNull(fEndoSubRoot.get("rskPremiumQuotaShare")));
		
		cq.multiselect(fRoot.get("rskPremiumQuotaShare").alias("RSK_PREMIUM_QUOTA_SHARE"), 
				fRoot.get("rskPremiumSurpuls").alias("RSK_PREMIUM_SURPULS"),
				fRoot.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC"),
				fRoot.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
				fRoot.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),
				fRoot.get("rskMdPremOsOc").alias("RSK_MD_PREM_OS_OC"),
				exp.alias("ADJ_PRE"),
				fRoot.get("rskEpiOsoeOc").alias("RSK_EPI_OSOE_OC"),
				dRoot.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"))
		
		.where(cb.equal(fRoot.get("rskProposalNumber"), proposalNo),
			cb.isNotNull(fRoot.get("rskEndorsementNo")),
			cb.equal(dRoot.get("rskProposalNumber"), fRoot.get("rskProposalNumber")),
			cb.equal(dRoot.get("rskEndorsementNo"), dEndoSq),
			cb.equal(fRoot.get("rskEndorsementNo"), fEndoSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> premiumSelectCashLossCreditUpdate(String contNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnCashLossCreditUpdate> luRoot = cq.from(TtrnCashLossCreditUpdate.class);
		
		Subquery<Integer> amendSq = cq.subquery(Integer.class);
		Root<TtrnCashLossCreditUpdate> subRoot = amendSq.from(TtrnCashLossCreditUpdate.class);

		amendSq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("contractNo"), luRoot.get("contractNo")),
				cb.equal(subRoot.get("transactionNo"), luRoot.get("transactionNo")),
				cb.equal(subRoot.get("claimNo"), luRoot.get("claimNo")));
		
		cq.multiselect(luRoot.get("claimNo").alias("CLAIM_NO"), 
				luRoot.get("currencyId").alias("CURRENCY_ID"),
				luRoot.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				luRoot.get("cashLossCreditDc").alias("CASH_LOSS_CREDIT_DC"))
		
		.where(cb.equal(luRoot.get("contractNo"), contNo),
			cb.equal(luRoot.get("transactionNo"), transactionNo),
			cb.equal(luRoot.get("amendId"), amendSq))
		
		.orderBy(cb.asc(luRoot.get("claimNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String selectCurrecyName(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TmasBranchMaster> root = cq.from(TmasBranchMaster.class);

		cq.multiselect(root.get("countryShortName").alias("COUNTRY_SHORT_NAME"))
				.where(cb.equal(root.get("branchCode"), branchCode));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String premiumSelectSumOfPaidPremium(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);

		Expression<Object> exp = cb.selectCase().when(cb.isNull(cb.sum(root.get("premiumQuotashareOc"))), 0)
				.otherwise(cb.sum(root.get("premiumQuotashareOc")));

		cq.multiselect(exp.as(String.class).alias("PREMIUM_QUOTASHARE_OC"))
				.where(cb.equal(root.get("contractNo"), contractNo));

		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getSettlementStatus(String contNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		cq.multiselect(root.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"),
					   root.get("netdueOc").alias("NETDUE_OC"))
		.where(cb.equal(root.get("contractNo"), contNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getPremiumTempView(String branchCode,String productId, String contractNo, String requestNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsTemp> root = cq.from(RskPremiumDetailsTemp.class);
		
		Subquery<String> deptSq = cq.subquery(String.class);
		Root<TmasDepartmentMaster> deptSubRoot = deptSq.from(TmasDepartmentMaster.class);

		deptSq.select(deptSubRoot.get("tmasDepartmentName")).where(
				cb.equal(deptSubRoot.get("branchCode"), branchCode),
				cb.equal(deptSubRoot.get("tmasProductId"), productId),
				cb.equal(deptSubRoot.get("tmasStatus"), "Y"),
				cb.equal(deptSubRoot.get("tmasDepartmentId"), root.get("subClass")));
		
		Subquery<String> pDeptSq = cq.subquery(String.class);
		Root<TmasDepartmentMaster> pDeptSubRoot = pDeptSq.from(TmasDepartmentMaster.class);

		pDeptSq.select(pDeptSubRoot.get("tmasDepartmentName")).where(
				cb.equal(pDeptSubRoot.get("branchCode"), branchCode),
				cb.equal(pDeptSubRoot.get("tmasProductId"), productId),
				cb.equal(pDeptSubRoot.get("tmasStatus"), "Y"),
				cb.equal(pDeptSubRoot.get("tmasDepartmentId"), root.get("premiumClass")));
		
		Subquery<String> sectionSq = cq.subquery(String.class);
		Root<TtrnPttySection> seSubRoot = sectionSq.from(TtrnPttySection.class);
		
		Subquery<BigDecimal> psectionSq = cq.subquery(BigDecimal.class);
		Root<TtrnPttySection> pseSubRoot = psectionSq.from(TtrnPttySection.class);

		psectionSq.select(cb.max(pseSubRoot.get("amendId"))).where(
				cb.equal(seSubRoot.get("contractNo"), pseSubRoot.get("contractNo")),
				cb.equal(seSubRoot.get("deptId"), pseSubRoot.get("deptId")));
		
		sectionSq.select(seSubRoot.get("sectionName")).where(
				cb.equal(seSubRoot.get("contractNo"), root.get("contractNo")),
				cb.equal(seSubRoot.get("deptId"), root.get("subClass")),
				cb.equal(seSubRoot.get("sectionNo"), root.get("sectionName")),
				cb.equal(seSubRoot.get("amendId"), psectionSq));
		
		//ACCOUNT_PERIOD_QTR
				Subquery<String> act = cq.subquery(String.class); 
				Root<ConstantDetail> name = act.from(ConstantDetail.class);
				act.select(name.get("detailName"));
				Predicate j1 = cb.equal( name.get("categoryId"), "49");
				Predicate j2 = cb.equal( name.get("type"), root.get("accountPeriodQtr")); //
				act.where(j1,j2);
				
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("transactionNo").alias("TRANSACTION_NO"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerage").alias("BROKERAGE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("tax").alias("TAX"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("commission").alias("COMMISSION"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("layerNo").alias("LAYER_NO"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				act.alias("ACCOUNT_PERIOD_QTR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("brokerageAmtDc").alias("BROKERAGE_AMT_DC"),
				root.get("taxAmtDc").alias("TAX_AMT_DC"),
				root.get("mDpremiumDc").alias("M_DPREMIUM_DC"),
				root.get("adjustmentPremiumDc").alias("ADJUSTMENT_PREMIUM_DC"),
				root.get("recPremiumDc").alias("REC_PREMIUM_DC"),
				root.get("netdueDc").alias("NETDUE_DC"),
				root.get("otherCostDc").alias("OTHER_COST_DC"),
				root.get("entryDateTime").alias("ENTRY_DATE"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("remarks").alias("REMARKS"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalCrDc").alias("TOTAL_CR_DC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("totalDrDc").alias("TOTAL_DR_DC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				pDeptSq.alias("TMAS_DEPARTMENT_NAME"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("premiumQuotashareDc").alias("PREMIUM_QUOTASHARE_DC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("commissionQuotashareDc").alias("COMMISSION_QUOTASHARE_DC"),
				
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("premiumSurplusDc").alias("PREMIUM_SURPLUS_DC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("commissionSurplusDc").alias("COMMISSION_SURPLUS_DC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("premiumPortfolioinDc").alias("PREMIUM_PORTFOLIOIN_DC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinDc").alias("CLAIM_PORTFOLIOIN_DC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("premiumPortfoliooutDc").alias("PREMIUM_PORTFOLIOOUT_DC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("lossReserveReleasedDc").alias("LOSS_RESERVE_RELEASED_DC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("premiumreserveQuotashareDc").alias("PREMIUMRESERVE_QUOTASHARE_DC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("cashLossCreditDc").alias("CASH_LOSS_CREDIT_DC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("lossReserveretainedDc").alias("LOSS_RESERVERETAINED_DC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("profitCommissionDc").alias("PROFIT_COMMISSION_DC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("cashLosspaidDc").alias("CASH_LOSSPAID_DC"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("claimsPaidDc").alias("CLAIMS_PAID_DC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("claimPortfolioOutDc").alias("CLAIM_PORTFOLIO_OUT_DC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("premiumReserveRealsedDc").alias("PREMIUM_RESERVE_REALSED_DC"),
				root.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("xlCostDc").alias("XL_COST_DC"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("interestDc").alias("INTEREST_DC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("osclaimLossupdateDc").alias("OSCLAIM_LOSSUPDATE_DC"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("osbyn").alias("OSBYN"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				sectionSq.alias("SECTION_NAME"),
				root.get("scCommOc").alias("SC_COMM_OC"),
				root.get("scCommDc").alias("SC_COMM_DC"))
				
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("requestNo"), requestNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> getPremiumView(String branchCode,String productId, String contractNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		Subquery<String> deptSq = cq.subquery(String.class);
		Root<TmasDepartmentMaster> deptSubRoot = deptSq.from(TmasDepartmentMaster.class);

		deptSq.select(deptSubRoot.get("tmasDepartmentName")).where(
				cb.equal(deptSubRoot.get("branchCode"), branchCode),
				cb.equal(deptSubRoot.get("tmasProductId"), productId),
				cb.equal(deptSubRoot.get("tmasStatus"), "Y"),
				cb.equal(deptSubRoot.get("tmasDepartmentId"), root.get("subClass")));
		
		Subquery<String> pDeptSq = cq.subquery(String.class);
		Root<TmasDepartmentMaster> pDeptSubRoot = pDeptSq.from(TmasDepartmentMaster.class);

		pDeptSq.select(pDeptSubRoot.get("tmasDepartmentName")).where(
				cb.equal(pDeptSubRoot.get("branchCode"), branchCode),
				cb.equal(pDeptSubRoot.get("tmasProductId"), productId),
				cb.equal(pDeptSubRoot.get("tmasStatus"), "Y"),
				cb.equal(pDeptSubRoot.get("tmasDepartmentId"), root.get("premiumClass")));
		Subquery<String> sectionSq = cq.subquery(String.class);
		Root<TtrnPttySection> seSubRoot = sectionSq.from(TtrnPttySection.class);
		
		Subquery<BigDecimal> psectionSq = cq.subquery(BigDecimal.class);
		Root<TtrnPttySection> pseSubRoot = psectionSq.from(TtrnPttySection.class);

		psectionSq.select(cb.max(pseSubRoot.get("amendId"))).where(
				cb.equal(seSubRoot.get("contractNo"), pseSubRoot.get("contractNo")),
				cb.equal(seSubRoot.get("deptId"), pseSubRoot.get("deptId")));
		
		sectionSq.select(seSubRoot.get("sectionName")).where(
				cb.equal(seSubRoot.get("contractNo"), root.get("contractNo")),
				cb.equal(seSubRoot.get("deptId"), root.get("subClass")),
				cb.equal(seSubRoot.get("sectionNo"), root.get("sectionName")),
				cb.equal(seSubRoot.get("amendId"), psectionSq));
		
		
		
		//ACCOUNT_PERIOD_QTR
		Subquery<String> act = cq.subquery(String.class); 
		Root<ConstantDetail> name = act.from(ConstantDetail.class);
		act.select(name.get("detailName"));
		Predicate j1 = cb.equal( name.get("categoryId"), "49");
		Predicate j2 = cb.equal( name.get("type"), root.get("accountPeriodQtr")); //
		act.where(j1,j2);
		
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("transactionNo").alias("TRANSACTION_NO"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerage").alias("BROKERAGE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("tax").alias("TAX"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("commission").alias("COMMISSION"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("layerNo").alias("LAYER_NO"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				act.alias("ACCOUNT_PERIOD_QTR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("brokerageAmtDc").alias("BROKERAGE_AMT_DC"),
				root.get("taxAmtDc").alias("TAX_AMT_DC"),
				root.get("mDpremiumDc").alias("M_DPREMIUM_DC"),
				root.get("adjustmentPremiumDc").alias("ADJUSTMENT_PREMIUM_DC"),
				root.get("recPremiumDc").alias("REC_PREMIUM_DC"),
				root.get("netdueDc").alias("NETDUE_DC"),
				root.get("otherCostDc").alias("OTHER_COST_DC"),
				root.get("entryDateTime").alias("ENTRY_DATE"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("remarks").alias("REMARKS"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalCrDc").alias("TOTAL_CR_DC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("totalDrDc").alias("TOTAL_DR_DC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				pDeptSq.alias("TMAS_DEPARTMENT_NAME"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("premiumQuotashareDc").alias("PREMIUM_QUOTASHARE_DC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("commissionQuotashareDc").alias("COMMISSION_QUOTASHARE_DC"),
				
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("premiumSurplusDc").alias("PREMIUM_SURPLUS_DC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("commissionSurplusDc").alias("COMMISSION_SURPLUS_DC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("premiumPortfolioinDc").alias("PREMIUM_PORTFOLIOIN_DC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinDc").alias("CLAIM_PORTFOLIOIN_DC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("premiumPortfoliooutDc").alias("PREMIUM_PORTFOLIOOUT_DC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("lossReserveReleasedDc").alias("LOSS_RESERVE_RELEASED_DC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("premiumreserveQuotashareDc").alias("PREMIUMRESERVE_QUOTASHARE_DC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("cashLossCreditDc").alias("CASH_LOSS_CREDIT_DC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("lossReserveretainedDc").alias("LOSS_RESERVERETAINED_DC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("profitCommissionDc").alias("PROFIT_COMMISSION_DC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("cashLosspaidDc").alias("CASH_LOSSPAID_DC"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("claimsPaidDc").alias("CLAIMS_PAID_DC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("claimPortfolioOutDc").alias("CLAIM_PORTFOLIO_OUT_DC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("premiumReserveRealsedDc").alias("PREMIUM_RESERVE_REALSED_DC"),
				root.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("xlCostDc").alias("XL_COST_DC"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("interestDc").alias("INTEREST_DC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("osclaimLossupdateDc").alias("OSCLAIM_LOSSUPDATE_DC"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("osbyn").alias("OSBYN"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				sectionSq.alias("SECTION_NAME"),
				root.get("scCommOc").alias("SC_COMM_OC"),
				root.get("scCommDc").alias("SC_COMM_DC")
				
				
				)
				
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("transactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> getPremiumTempEdit(String contNo, String requestNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsTemp> root = cq.from(RskPremiumDetailsTemp.class);
		
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("transactionNo").alias("TRANSACTION_NO"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerage").alias("BROKERAGE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("tax").alias("TAX"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("commission").alias("COMMISSION"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("layerNo").alias("LAYER_NO"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				root.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("brokerageAmtDc").alias("BROKERAGE_AMT_DC"),
				root.get("taxAmtDc").alias("TAX_AMT_DC"),
				root.get("mDpremiumDc").alias("M_DPREMIUM_DC"),
				root.get("adjustmentPremiumDc").alias("ADJUSTMENT_PREMIUM_DC"),
				root.get("recPremiumDc").alias("REC_PREMIUM_DC"),
				root.get("netdueDc").alias("NETDUE_DC"),
				root.get("otherCostDc").alias("OTHER_COST_DC"),
				root.get("entryDateTime").alias("ENTRY_DATE"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("remarks").alias("REMARKS"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalCrDc").alias("TOTAL_CR_DC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("totalDrDc").alias("TOTAL_DR_DC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("premiumQuotashareDc").alias("PREMIUM_QUOTASHARE_DC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("commissionQuotashareDc").alias("COMMISSION_QUOTASHARE_DC"),
				
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("premiumSurplusDc").alias("PREMIUM_SURPLUS_DC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("commissionSurplusDc").alias("COMMISSION_SURPLUS_DC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("premiumPortfolioinDc").alias("PREMIUM_PORTFOLIOIN_DC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinDc").alias("CLAIM_PORTFOLIOIN_DC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("premiumPortfoliooutDc").alias("PREMIUM_PORTFOLIOOUT_DC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("lossReserveReleasedDc").alias("LOSS_RESERVE_RELEASED_DC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("premiumreserveQuotashareDc").alias("PREMIUMRESERVE_QUOTASHARE_DC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("cashLossCreditDc").alias("CASH_LOSS_CREDIT_DC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("lossReserveretainedDc").alias("LOSS_RESERVERETAINED_DC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("profitCommissionDc").alias("PROFIT_COMMISSION_DC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("cashLosspaidDc").alias("CASH_LOSSPAID_DC"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("claimsPaidDc").alias("CLAIMS_PAID_DC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("claimPortfolioOutDc").alias("CLAIM_PORTFOLIO_OUT_DC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("premiumReserveRealsedDc").alias("PREMIUM_RESERVE_REALSED_DC"),
				root.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("xlCostDc").alias("XL_COST_DC"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("interestDc").alias("INTEREST_DC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("osclaimLossupdateDc").alias("OSCLAIM_LOSSUPDATE_DC"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("osbyn").alias("OSBYN"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("scCommOc").alias("SC_COMM_OC"),
				root.get("scCommDc").alias("SC_COMM_DC"),
				root.get("status").alias("STATUS"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("sectionName").alias("SECTION_NAME"),
				root.get("prdAllocatedTillDate").alias("PRD_ALLOCATED_TILL_DATE"),
				root.get("lrdAllocatedTillDate").alias("LRD_ALLOCATED_TILL_DATE")
				
				
				)
				
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("requestNo"), requestNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> getPremiumEdit(String contNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("transactionNo").alias("TRANSACTION_NO"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerage").alias("BROKERAGE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("tax").alias("TAX"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("commission").alias("COMMISSION"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("layerNo").alias("LAYER_NO"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				root.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("brokerageAmtDc").alias("BROKERAGE_AMT_DC"),
				root.get("taxAmtDc").alias("TAX_AMT_DC"),
				root.get("mDpremiumDc").alias("M_DPREMIUM_DC"),
				root.get("adjustmentPremiumDc").alias("ADJUSTMENT_PREMIUM_DC"),
				root.get("recPremiumDc").alias("REC_PREMIUM_DC"),
				root.get("netdueDc").alias("NETDUE_DC"),
				root.get("otherCostDc").alias("OTHER_COST_DC"),
				root.get("entryDateTime").alias("ENTRY_DATE"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("remarks").alias("REMARKS"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalCrDc").alias("TOTAL_CR_DC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("totalDrDc").alias("TOTAL_DR_DC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("premiumQuotashareDc").alias("PREMIUM_QUOTASHARE_DC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("commissionQuotashareDc").alias("COMMISSION_QUOTASHARE_DC"),
				
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("premiumSurplusDc").alias("PREMIUM_SURPLUS_DC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("commissionSurplusDc").alias("COMMISSION_SURPLUS_DC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("premiumPortfolioinDc").alias("PREMIUM_PORTFOLIOIN_DC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinDc").alias("CLAIM_PORTFOLIOIN_DC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("premiumPortfoliooutDc").alias("PREMIUM_PORTFOLIOOUT_DC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("lossReserveReleasedDc").alias("LOSS_RESERVE_RELEASED_DC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("premiumreserveQuotashareDc").alias("PREMIUMRESERVE_QUOTASHARE_DC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("cashLossCreditDc").alias("CASH_LOSS_CREDIT_DC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("lossReserveretainedDc").alias("LOSS_RESERVERETAINED_DC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("profitCommissionDc").alias("PROFIT_COMMISSION_DC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("cashLosspaidDc").alias("CASH_LOSSPAID_DC"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("claimsPaidDc").alias("CLAIMS_PAID_DC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("claimPortfolioOutDc").alias("CLAIM_PORTFOLIO_OUT_DC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("premiumReserveRealsedDc").alias("PREMIUM_RESERVE_REALSED_DC"),
				root.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("xlCostDc").alias("XL_COST_DC"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("interestDc").alias("INTEREST_DC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("osclaimLossupdateDc").alias("OSCLAIM_LOSSUPDATE_DC"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("osbyn").alias("OSBYN"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("scCommOc").alias("SC_COMM_OC"),
				root.get("scCommDc").alias("SC_COMM_DC"),
				root.get("status").alias("STATUS"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("sectionName").alias("SECTION_NAME"),
				root.get("prdAllocatedTillDate").alias("PRD_ALLOCATED_TILL_DATE"),
				root.get("lrdAllocatedTillDate").alias("LRD_ALLOCATED_TILL_DATE")
				
				
				)
				
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("transactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();  
	}
	@Transactional
	@Override
	public Integer updateDepositRelease(String contractNo, String requestNo, String transactionNo, String tableMoveStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnDepositRelease> update = cb.createCriteriaUpdate(TtrnDepositRelease.class);
		Root<TtrnDepositRelease> root = update.from(TtrnDepositRelease.class);
		
		
		update.set(root.get("rlTransactionNo"), transactionNo)
		 		.set(root.get("tableMoveStatus"),tableMoveStatus);
		update.where(cb.equal(root.get("contractNo"), contractNo),
				     cb.equal(root.get("tempRequestNo"), requestNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	@Transactional
	@Override
	public Integer updateCashLossStatus(String contractNo, String requestNo, String transactionNo, String tableMoveStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnCashLossCredit> update = cb.createCriteriaUpdate(TtrnCashLossCredit.class);
		Root<TtrnCashLossCredit> root = update.from(TtrnCashLossCredit.class);
		
		
		update.set(root.get("credittrxnno"), transactionNo)
		 		.set(root.get("tableMoveStatus"),tableMoveStatus);
		update.where(cb.equal(root.get("contractNo"), contractNo),
				     cb.equal(root.get("tempRequestNo"), requestNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	@Transactional
	@Override
	public Integer updateClaimPayment(String contNo, String branchCode, String requestNo, String tableMoveStatus,
			String claimNumber, String claimPaymentNo, String contNo2, String claimNumber2, String claimPaymentNo2) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);
		
		Subquery<BigDecimal> sq = update.subquery(BigDecimal.class);
		Root<TtrnCashLossCredit> subRoot = sq.from(TtrnCashLossCredit.class);

		sq.select(cb.<BigDecimal>selectCase().when(cb.isNull(cb.sum(root.get("creditamountcld"))), BigDecimal.ZERO)
				.otherwise(cb.sum(root.get("creditamountcld"))))
		
		.where(cb.equal(subRoot.get("contractNo"), contNo), 
				cb.equal(subRoot.get("tempRequestNo"), requestNo), 
				cb.equal(subRoot.get("branchCode"), branchCode),
				cb.equal(subRoot.get("tableMoveStatus"), tableMoveStatus),
				cb.equal(subRoot.get("claimNo"), claimNumber),
				cb.equal(subRoot.get("claimpaymentNo"), claimPaymentNo));
		
		Expression<BigDecimal> exp = cb.sum(cb.<BigDecimal>selectCase().when(cb.isNull(root.<BigDecimal>get("prdAllocatedTillDate")), BigDecimal.ZERO)
				.otherwise(root.<BigDecimal>get("prdAllocatedTillDate")), sq);
				
		update.set(root.<BigDecimal>get("cashLossSettledTilldate"), exp);
		update.where(cb.equal(root.get("contractNo"), contNo2),
				cb.equal(subRoot.get("claimNo"), claimNumber2),
				cb.equal(subRoot.get("claimpaymentNo"), claimPaymentNo2));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public void premiumarchive(String contNo, String layerNo, String transactionNo, String currencyId, String exchRate,
			String netDueOc, String departmentId, String productId) {
		StoredProcedureQuery sp = em.createStoredProcedureQuery("PREMIUM_DETAIL_ARCHIVE");

		// Assign parameters
		sp.registerStoredProcedureParameter("pnContractNo", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnLayerNo", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnTranNno", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnCurrency", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnExchange", Double.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnPrmAmount", Double.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnDeptId", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnProducttId", String.class, ParameterMode.IN);
		// Set parameters
		sp.setParameter("pnContractNo", contNo);
		sp.setParameter("pnLayerNo", Integer.parseInt(layerNo));
		sp.setParameter("pnTranNno", Integer.parseInt(transactionNo));
		sp.setParameter("pnCurrency", currencyId);
		sp.setParameter("pnExchange", Double.parseDouble(exchRate));
		sp.setParameter("pnPrmAmount", Double.parseDouble(netDueOc));

		sp.setParameter("pnDeptId", departmentId);
		sp.setParameter("pnProducttId", productId);
		
		System.out.println("pnContractNo: "+sp.getParameterValue("pnContractNo"));
		System.out.println("pnLayerNo: "+sp.getParameterValue("pnLayerNo"));
		System.out.println("pnTranNno: "+sp.getParameterValue("pnTranNno"));
		System.out.println("pnCurrency: "+sp.getParameterValue("pnCurrency"));
		System.out.println("pnExchange: "+sp.getParameterValue("pnExchange"));
		System.out.println("pnPrmAmount: "+sp.getParameterValue("pnPrmAmount"));
		System.out.println("pnDeptId: "+sp.getParameterValue("pnDeptId"));
		System.out.println("pnProducttId: "+sp.getParameterValue("pnProducttId"));
		// execute SP
		sp.execute();
		
	}
	public java.util.Date parseDateLocal(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("DD/MM/YYYY");
		return sdf1.parse(input);
	}

	@Override
	public void premiumRiSplit(InsertPremiumReq req) {
		StoredProcedureQuery sp = em.createStoredProcedureQuery("RI_SPLIT_INSERT");

		// Assign parameters
		sp.registerStoredProcedureParameter("V_CONTRACT_NO", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_LAYER_NO", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_PRODUCT_ID", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_TRANSACTION_NO", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("V_BRANCH_CODE", String.class, ParameterMode.IN);
	
		// Set parameters
		sp.setParameter("V_CONTRACT_NO", req.getContNo());
		sp.setParameter("V_LAYER_NO", StringUtils.isBlank(req.getLayerno())?"0":req.getLayerno());
		sp.setParameter("V_PRODUCT_ID", req.getProductId());
		sp.setParameter("V_TRANSACTION_NO", req.getTransactionNo());
		sp.setParameter("V_BRANCH_CODE", req.getBranchCode());
		
		System.out.println("V_CONTRACT_NO: "+sp.getParameterValue("V_CONTRACT_NO"));
		System.out.println("V_LAYER_NO: "+sp.getParameterValue("V_LAYER_NO"));
		System.out.println("V_PRODUCT_ID: "+sp.getParameterValue("V_PRODUCT_ID"));
		System.out.println("V_TRANSACTION_NO: "+sp.getParameterValue("V_TRANSACTION_NO"));
		System.out.println("V_BRANCH_CODE: "+sp.getParameterValue("V_BRANCH_CODE"));
		sp.execute();
	}
	public Date formatDate(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf1.parse(input);
		return new java.sql.Date(date.getTime());
	}
}
