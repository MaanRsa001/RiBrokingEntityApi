package com.maan.insurance.jpa.repository.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.xolpremium.RskXLPremiumDetails;
import com.maan.insurance.jpa.entity.xolpremium.SequenceMaster;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.jpa.entity.xolpremium.TtrnRetroPrcl;
import com.maan.insurance.jpa.repository.xolpremium.XolPremiumCustomRepository;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;

@Repository
public class XolPremiumCustomRepositoryImpl implements XolPremiumCustomRepository {
	
	@Autowired
	EntityManager em;
	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	@Override
	public List<Tuple> selectPremiumedList1(GetPremiumedListReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<RskPremiumDetails> traRoot = cq.from(RskPremiumDetails.class);
		
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
				rkRoot.get("rskDeptid").alias("RSK_DEPTID"),
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
				cb.equal(endoSubRoot.get("rskContractNo"), beanObj.getContNo()),
				cb.equal(endoSubRoot.get("rskLayerNo"), beanObj.getLayerNo()));
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), beanObj.getContNo()),
				 cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				 cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
				 cb.equal(personalRoot.get("branchCode"), beanObj.getBranchCode()),
				 cb.equal(personalRoot.get("amendId"), amendSq),
				 cb.equal(piRoot.get("branchCode"), personalRoot.get("branchCode")),
				 cb.equal(piRoot.get("amendId"), piAmendSq),
				 cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
				 cb.equal(traRoot.get("contractNo"), rkRoot.get("rskContractNo")),
				 cb.equal(traRoot.get("layerNo"), rkRoot.get("rskLayerNo")),
				 cb.equal(rkRoot.get("rskLayerNo"), beanObj.getLayerNo()));
		
		cq.orderBy(cb.desc(traRoot.get("transactionNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectRetroPremiumedList1(GetPremiumedListReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<RskXLPremiumDetails> traRoot = cq.from(RskXLPremiumDetails.class);
		
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
				rkRoot.get("rskDeptid").alias("RSK_DEPTID"),
				traRoot.get("transactionNo").alias("TRANSACTION_NO"),
				traExpression.alias("INS_DETAIL"),
				cb.selectCase().when(cb.equal(rkRoot.<Integer>get("rskProductid"), 2), 
									 cb.concat(cb.concat(detailNameSq, " "), traRoot.get("accountPeriodYear")))
							   .otherwise(accPerExpression).alias("ACC_PER"),
			   traRoot.get("entryDateTime").alias("INS_DATE"),
			   cb.selectCase().when(cb.isNull(traRoot.get("allocatedTillDate")), 0)
			   				  .otherwise(traRoot.get("allocatedTillDate")).alias("ALLOC_AMT"),
			   traRoot.get("movementYn").alias("MOVEMENT_YN"),
			   traRoot.get("settlementStatus").alias("SETTLEMENT_STATUS"),
			   traRoot.get("transactionMonthYear").alias("TRANSACTION_DATE"),
			   traRoot.get("statementDate").alias("STATEMENT_DATE"),
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
				cb.equal(endoSubRoot.get("rskContractNo"), beanObj.getContNo()),
				cb.equal(endoSubRoot.get("rskLayerNo"), beanObj.getLayerNo()));
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), beanObj.getContNo()),
				 cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				 cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
				 cb.equal(personalRoot.get("branchCode"), beanObj.getBranchCode()),
				 cb.equal(personalRoot.get("amendId"), amendSq),
				 cb.equal(piRoot.get("branchCode"), personalRoot.get("branchCode")),
				 cb.equal(piRoot.get("amendId"), piAmendSq),
				 cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
				 cb.equal(traRoot.get("contractNo"), rkRoot.get("rskContractNo")),
				 cb.equal(traRoot.get("layerNo"), rkRoot.get("rskLayerNo")),
				 cb.equal(rkRoot.get("rskLayerNo"), beanObj.getLayerNo()));
		
		cq.orderBy(cb.desc(traRoot.get("transactionNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> xolPremiumListTemp(GetPremiumedListReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<RskPremiumDetailsTemp> traRoot = cq.from(RskPremiumDetailsTemp.class);
		
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
				rkRoot.get("rskDeptid").alias("RSK_DEPTID"),
				traRoot.get("transactionNo").alias("TRANSACTION_NO"),
				traExpression.alias("INS_DETAIL"),
				cb.selectCase().when(cb.equal(rkRoot.<Integer>get("rskProductid"), 2), 
									 cb.concat(cb.concat(detailNameSq, " "), traRoot.get("accountPeriodYear")))
							   .otherwise(accPerExpression).alias("ACC_PER"),
			   traRoot.get("entryDateTime").alias("INS_DATE"),
			   cb.selectCase().when(cb.isNull(traRoot.get("allocatedTillDate")), 0)
			   				  .otherwise(traRoot.get("allocatedTillDate")).alias("ALLOC_AMT"),
			   traRoot.get("movementYn").alias("MOVEMENT_YN"),
			   traRoot.get("settlementStatus").alias("SETTLEMENT_STATUS"),
			   traRoot.get("transactionMonthYear").alias("TRANSACTION_DATE"),
			   traRoot.get("statementDate").alias("STATEMENT_DATE"),
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
				cb.equal(endoSubRoot.get("rskContractNo"), beanObj.getContNo()),
				cb.equal(endoSubRoot.get("rskLayerNo"), beanObj.getLayerNo()));
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), beanObj.getContNo()),
				 cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				 cb.equal(rkRoot.get("rskBrokerid"), piRoot.get("customerId")),
				 cb.equal(personalRoot.get("branchCode"), beanObj.getBranchCode()),
				 cb.equal(personalRoot.get("amendId"), amendSq),
				 cb.equal(piRoot.get("branchCode"), personalRoot.get("branchCode")),
				 cb.equal(piRoot.get("amendId"), piAmendSq),
				 cb.equal(rkRoot.get("rskEndorsementNo"), endoSq),
				 cb.equal(traRoot.get("contractNo"), rkRoot.get("rskContractNo")),
				 cb.equal(traRoot.get("layerNo"), rkRoot.get("rskLayerNo")),
				 cb.equal(rkRoot.get("rskLayerNo"), beanObj.getLayerNo()),
				 cb.equal(traRoot.get("transStatus"), "P"));
		
		cq.orderBy(cb.desc(traRoot.get("transactionNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String selectAllocatedYN(String contractNo, String transactionNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);
		
		cq.multiselect(cb.selectCase().when(cb.equal(cb.count(root), 0), "Y").otherwise("N").alias("allocatedYN"))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("type"), "P"),
				cb.equal(root.get("status"), "Y"));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String getAllocationStatus(String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);
		
		cq.multiselect(cb.count(root).as(String.class).alias("COUNT"))
		.where(cb.equal(root.get("transactionNo"), transactionNo),
				cb.equal(root.get("status"), "Y"));
		return em.createQuery(cq).getSingleResult();
	}
	
	@Override
	public String getRetroStatus(String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRetroPrcl> root = cq.from(TtrnRetroPrcl.class);
		
		cq.multiselect(cb.count(root).as(String.class).alias("COUNT"))
		.where(cb.equal(root.get("transactionNo"), transactionNo));
		return em.createQuery(cq).getSingleResult();
	}
	
	@Override
	public String getRetroStatus1(String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRetroPrcl> root = cq.from(TtrnRetroPrcl.class);
		
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnRetroPrcl> subRoot = sq.from(TtrnRetroPrcl.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(root.get("transactionNo"), subRoot.get("transactionNo")));
		
		cq.multiselect(cb.count(root).as(String.class).alias("COUNT"))
		.where(cb.equal(root.get("transactionNo"), transactionNo),
				cb.isNotNull(root.get("processId")),
				cb.equal(root.get("amendId") ,sq));
		
		return em.createQuery(cq).getSingleResult();
	}


	@Override
	public String selectRskSubjPremiumOc(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRiskProposal> root = cq.from(TtrnRiskProposal.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);
		
		Subquery<Integer> amendSq = sq.subquery(Integer.class);
		Root<PositionMaster> aSubRoot = amendSq.from(PositionMaster.class);

		amendSq.select(cb.max(aSubRoot.get("amendId")))
				.where(cb.equal(aSubRoot.get("contractNo"), subRoot.get("contractNo")),
					   cb.equal(aSubRoot.get("layerNo"), subRoot.get("layerNo")),
					   cb.equal(aSubRoot.get("contractStatus"), subRoot.get("contractStatus")));

		sq.select(subRoot.get("proposalNo"))
		  .where(cb.equal(subRoot.get("contractNo"), contractNo),
				  cb.equal(subRoot.get("amendId"), amendSq),
				  cb.equal(cb.selectCase().when(cb.isNull(subRoot.get("contractStatus")), "A")
						  .otherwise(subRoot.get("contractStatus")), subRoot.get("contractStatus")),
				  cb.equal(subRoot.get("layerNo"), layerNo));
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnRiskProposal> endoSubRoot = endoSq.from(TtrnRiskProposal.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo")))
				.where(cb.equal(endoSubRoot.get("rskProposalNumber"), root.get("rskProposalNumber")));
		
		
//		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("rskSubjPremiumOc")), 0)
//									  .otherwise(root.get("rskSubjPremiumOc")))
		
		Expression<Object> rsksubOc = cb.selectCase().when(cb.isNull(root.get("rskSubjPremiumOc")), 0)
				  .otherwise(root.get("rskSubjPremiumOc"));
		
		cq.multiselect(rsksubOc.as(String.class).alias("RSK_SUBJ_PREMIUM_OC"))	  
				  
		.where(cb.equal(root.get("rskProposalNumber"), sq),
			   cb.equal(root.get("rskEndorsementNo"), endoSq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String selectPreviousPremium(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		Expression<Double> mDpremiumOcExp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("mDpremiumOc")),0.0)
				.otherwise(root.<Double>get("mDpremiumOc"));
			
		Expression<Double> adjustmentExp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("adjustmentPremiumOc")),0.0)
					.otherwise(root.<Double>get("adjustmentPremiumOc"));
				
		Expression<Double> added = cb.sum(cb.sum(mDpremiumOcExp, adjustmentExp));
			
			cq.multiselect(added.as(String.class).alias("SUM"))
			
		.where(cb.equal(root.get("contractNo"), contractNo));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getPremiumMndInsList(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnMndInstallments> root = cq.from(TtrnMndInstallments.class);
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnMndInstallments> endoSubRoot = endoSq.from(TtrnMndInstallments.class);

		endoSq.select(cb.max(endoSubRoot.get("endorsementNo")))
				.where(cb.equal(endoSubRoot.get("contractNo"), contractNo),
					   cb.equal(endoSubRoot.get("layerNo"), layerNo));
		
		Subquery<String> iNoSq = cq.subquery(String.class);
		Root<RskPremiumDetailsTemp> iNoSubRoot = iNoSq.from(RskPremiumDetailsTemp.class);

		iNoSq.select(iNoSubRoot.get("instalmentNumber"))
				.where(cb.isNull(iNoSubRoot.get("reverseTransactionNo")),
					   cb.equal(iNoSubRoot.get("contractNo"), contractNo),
					   cb.equal(iNoSubRoot.get("layerNo"), layerNo),
					   cb.isNotNull(iNoSubRoot.get("accountPeriodQtr")));
					   
		cq.multiselect(root.get("installmentNo").alias("INSTALLMENT_NO"),
					   root.get("installmentDate").alias("INSTALLMENT_DATE"))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("endorsementNo"), endoSq),
				cb.isNull(root.get("transactionNo")),
				cb.not(cb.in(root.get("installmentNo")).value(iNoSq)))
		
		.orderBy(cb.asc(root.get("installmentNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectmdInstalmentList(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnMndInstallments> root = cq.from(TtrnMndInstallments.class);
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnMndInstallments> endoSubRoot = endoSq.from(TtrnMndInstallments.class);

		endoSq.select(cb.max(endoSubRoot.get("endorsementNo")))
				.where(cb.equal(endoSubRoot.get("contractNo"), contractNo),
					   cb.equal(endoSubRoot.get("layerNo"), layerNo));
		
		cq.multiselect(root.get("installmentNo").alias("INSTALLMENT_NO"),
					   root.get("installmentDate").alias("INSTALLMENT_DATE"))
		.where(cb.equal(root.get("contractNo"), contractNo),
				cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("endorsementNo"), endoSq),
				cb.isNull(root.get("transactionNo")))
		
		.orderBy(cb.asc(root.get("installmentNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getGnpiCountPre(String contractNo, String layerNo, String endoType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRiskDetails> root = cq.from(TtrnRiskDetails.class);
		
		cq.multiselect(cb.count(root.get("rsEndorsementType")).as(String.class).alias("COUNT"))
		.where(cb.equal(root.get("rskContractNo"), contractNo),
				cb.equal(root.get("rskLayerNo"), layerNo),
				cb.equal(root.get("rsEndorsementType"), endoType));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectXolPreList(String contNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> aRoot = cq.from(PositionMaster.class);
		Root<TmasDepartmentMaster> bRoot = cq.from(TmasDepartmentMaster.class);
		Root<PersonalInfo> cRoot = cq.from(PersonalInfo.class);
		
		Subquery<String> fNameSq = cq.subquery(String.class);
		Root<PersonalInfo> fNameSubRoot = fNameSq.from(PersonalInfo.class);

		fNameSq.select(fNameSubRoot.get("firstName"))
			   .where( cb.equal(fNameSubRoot.get("customerId"), aRoot.get("brokerId")),
					   cb.equal(fNameSubRoot.get("branchCode"), aRoot.get("branchCode")));
		
		Subquery<Integer> pmAmendSq = cq.subquery(Integer.class);
		Root<PositionMaster> pmSubRoot = pmAmendSq.from(PositionMaster.class);

		pmAmendSq.select(cb.max(pmSubRoot.get("amendId")))
				.where(cb.equal(pmSubRoot.get("contractNo"), aRoot.get("contractNo")),
					   cb.equal(pmSubRoot.get("layerNo"), aRoot.get("layerNo")));
		
		Subquery<Integer> piAmendSq = cq.subquery(Integer.class);
		Root<PersonalInfo> piSubRoot = piAmendSq.from(PersonalInfo.class);

		piAmendSq.select(cb.max(piSubRoot.get("amendId")))
				.where(cb.equal(piSubRoot.get("customerId"), cRoot.get("customerId")),
					   cb.equal(piSubRoot.get("branchCode"), cRoot.get("branchCode")));
		
		cq.multiselect(aRoot.get("contractNo").alias("CONTRACT_NO"),
				bRoot.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
				aRoot.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
				aRoot.get("uwYear").alias("UW_YEAR"),
				cRoot.get("companyName").alias("COMPANY_NAME"),
				aRoot.get("layerNo").alias("LAYER_NO"),
				fNameSq.alias("First_name"))
		
		.where(cb.equal(aRoot.get("contractNo"), contNo),
				cb.equal(aRoot.get("layerNo"), layerNo),
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
	public String selectCeaseStatus(String contNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> pmAmendSq = cq.subquery(Integer.class);
		Root<PositionMaster> pmSubRoot = pmAmendSq.from(PositionMaster.class);

		pmAmendSq.select(cb.max(pmSubRoot.get("amendId")))
				.where(cb.equal(pmSubRoot.get("contractNo"), root.get("contractNo")),
					   cb.equal(pmSubRoot.get("layerNo"), root.get("layerNo")));
		
		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("ceaseStatus")), "N")
				.otherwise(root.get("ceaseStatus")).alias("CEASE_STATUS"))
				.where(cb.equal(root.get("contractNo"), contNo),
			   cb.equal(root.get("layerNo"), layerNo),
			   cb.equal(root.get("amendId"), pmAmendSq),
			   cb.equal(cb.selectCase().when(cb.isNull(root.get("contractStatus")), "A")
						  .otherwise(root.get("contractStatus")), root.get("contractStatus")));
				return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectDeptId(String contNo, String productId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Integer> pmAmendSq = cq.subquery(Integer.class);
		Root<PositionMaster> pmSubRoot = pmAmendSq.from(PositionMaster.class);

		pmAmendSq.select(cb.max(pmSubRoot.get("amendId")))
				.where(cb.equal(pmSubRoot.get("proposalNo"), root.get("proposalNo")));
		
		cq.multiselect(root.get("deptId").alias("DEPT_ID"),
					   root.get("proposalNo").alias("PROPOSAL_NO"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
			   cb.equal(root.get("productId"), productId),
			   cb.equal(root.get("amendId"), pmAmendSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String selectMndPremiumOC(String contNo, String layerNo, String instalmentno) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnMndInstallments> root = cq.from(TtrnMndInstallments.class);
		
		Subquery<Long> endoSq = cq.subquery(Long.class);
		Root<TtrnMndInstallments> endoSubRoot = endoSq.from(TtrnMndInstallments.class);

		endoSq.select(cb.max(endoSubRoot.get("endorsementNo")))
				.where(cb.equal(endoSubRoot.get("contractNo"), root.get("contractNo")),
					   cb.equal(endoSubRoot.get("layerNo"), root.get("layerNo")),
					   cb.equal(endoSubRoot.get("installmentNo"), root.get("installmentNo")));
		
		cq.multiselect(root.get("mndPremiumOc").as(String.class).alias("MND_PREMIUM_OC"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
			   cb.equal(root.get("layerNo"), layerNo),
			   cb.equal(root.get("installmentNo"), instalmentno),
			   cb.equal(root.get("endorsementNo"), endoSq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getBrokerCedingName(String contNo, String branchCode) {
		List<Tuple> finalList=new ArrayList<Tuple>();
		List<Tuple> resultList = ttrnBrokerCedingName(contNo, branchCode);
		List<Tuple> resultList1 =piBrokerCedingName(contNo, branchCode);
			if(!CollectionUtils.isEmpty(resultList))
				finalList.addAll(resultList);
			if(!CollectionUtils.isEmpty(resultList1))
				finalList.addAll(resultList1);
		return finalList;
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
				cb.concat(cb.concat(piRoot.get("address1"), " "), piRoot.get("address2")).alias("Address"))
		
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
				cb.concat(cb.concat(piRoot.get("address1"), " "), piRoot.get("address2")).alias("Address"))
		
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
	public List<Tuple> selectGetAlloTransaction(String contNo, String transactionNo) {
		List<Tuple> resultList = tprAllcoatedTransaction(contNo, transactionNo);
		if(Objects.nonNull(resultList))
			resultList.addAll(tatAllcoatedTransaction(contNo, transactionNo));
		else
			resultList = tatAllcoatedTransaction(contNo, transactionNo);
	return Objects.isNull(resultList) ? new ArrayList<>() : resultList;
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
		
		cq.multiselect(tatRoot.get("sno").alias("SNO"), 
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
		
		cq.multiselect(tatRoot.get("sno").alias("SNO"), 
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
	public List<Tuple> selectTreatyContDet1XolLayerNo2(ContractDetailsReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		//Root<TmasPfcMaster> pfcRoot = cq.from(TmasPfcMaster.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		//Root<TmasPolicyBranch> branchRoot = cq.from(TmasPolicyBranch.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cmRoot = cq.from(CurrencyMaster.class);
		
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
				cb.literal("RTRIM").alias("TMAS_PFC_NAME"),
				cb.literal("RTRIM").alias("TMAS_SPFC_NAME"), // need to be replaced
				personalRoot.get("companyName").alias("COMPANY"),
				rkRoot.get("rskTreatyid").alias("RSK_TREATYID"),
				nameExpression.alias("BROKER"),
				rkRoot.get("rskInceptionDate").alias("INS_DATE"),
				rkRoot.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
				rkRoot.get("rskExpiryDate").alias("EXP_DATE"),
				rkRoot.get("rskMonth").alias("MONTH"),
				rkRoot.get("rskCedingid").alias("CEDING_ID"),
				rkRoot.get("rskBrokerid").alias("BROKER_ID"),
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
				cb.equal(endoSubRoot.get("rskLayerNo"), req.getLayerno()));
		
		Subquery<Integer> cmAmendSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cmAmendSubRoot = cmAmendSq.from(CurrencyMaster.class);

		cmAmendSq.select(cb.max(cmAmendSubRoot.get("amendId"))).where(
				cb.equal(cmAmendSubRoot.get("currencyId"), cmRoot.get("currencyId")),
				cb.equal(cmAmendSubRoot.get("branchCode"), cmRoot.get("branchCode")));
		
		
		cq.where(cb.equal(rkRoot.get("rskContractNo"), req.getContNo()),
			   cb.equal(rkRoot.get("rskLayerNo"), req.getLayerno()),
			   //cb.equal(pfcRoot.get("tmasPfcId"), rkRoot.get("rskPfcid")),
			   //cb.equal(pfcRoot.get("branchCode"), req.getBranchCode()),
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
			   //cb.equal(rkRoot.get("rskPolbranch"), branchRoot.get("tmasPolBranchId")),
			   cb.equal(rkRoot.get("branchCode"), req.getBranchCode()),
			   cb.equal(cmRoot.get("currencyId"), rkRoot.get("rskOriginalCurr")),
			   cb.equal(cmRoot.get("branchCode"), piRoot.get("branchCode")),
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
				cb.literal("rskLossReserve").alias("RSK_LOSS_RESERVE"),
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
	public List<Tuple> selectTreatyXOLfacProposalDetails(String proposalNo) {
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
				cb.isNotNull(fEndoSubRoot.get("rskEndorsementNo")));
		
		cq.multiselect(fRoot.get("rskPremiumQuotaShare").alias("RSK_PREMIUM_QUOTA_SHARE"), 
				fRoot.get("rskPremiumSurpuls").alias("RSK_PREMIUM_SURPULS"),
				fRoot.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC"),
				fRoot.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
				fRoot.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),
				fRoot.get("rskMdPremOsOc").alias("RSK_MD_PREM_OS_OC"),
				exp.alias("ADJ_PRE"),
				dRoot.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"))
		
		.where(cb.equal(fRoot.get("rskProposalNumber"), proposalNo),
			cb.isNotNull(fRoot.get("rskEndorsementNo")),
			cb.equal(dRoot.get("rskProposalNumber"), fRoot.get("rskProposalNumber")),
			cb.equal(dRoot.get("rskEndorsementNo"), dEndoSq),
			cb.equal(fRoot.get("rskEndorsementNo"), fEndoSq));
		
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
	public List<Tuple> getXolPremiumEditRTemp(String contNo, String reqNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsTemp> root = cq.from(RskPremiumDetailsTemp.class);

		cq.multiselect(root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("status").alias("STATUS"),
				root.get("remarks").alias("REMARKS"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("commission").alias("COMMISSION"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("overrider").alias("OVERRIDER"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("subClass").alias("SUB_CLASS"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("scCommOc").alias("SC_Comm_OC"),
				root.get("scCommDc").alias("SC_Comm_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				root.get("lpcOc").alias("LPC_OC"),
				
				root.get("lpcDc").alias("LPC_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				cb.selectCase().when(cb.isNull(root.get("prdAllocatedTillDate")), 0)
							   .otherwise(root.get("prdAllocatedTillDate")).alias("PRD_ALLOCATED_TILL_DATE"),
			    cb.selectCase().when(cb.isNull(root.get("lrdAllocatedTillDate")), 0)
			    			   .otherwise(root.get("lrdAllocatedTillDate")).alias("LRD_ALLOCATED_TILL_DATE"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("osbyn").alias("OSBYN"),
				root.get("sectionName").alias("SECTION_NAME"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE")
				)
		
		.where(cb.equal(root.get("contractNo"), contNo),
				 cb.equal(root.get("requestNo"), reqNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectTreetyXOLPremiumEdit(String contNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);

		cq.multiselect(root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("status").alias("STATUS"),
				root.get("remarks").alias("REMARKS"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("commission").alias("COMMISSION"),
				//root.get("entryDateTime").alias("INS_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("overrider").alias("OVERRIDER"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("subClass").alias("SUB_CLASS"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("scCommOc").alias("SC_Comm_OC"),
				root.get("scCommDc").alias("SC_Comm_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				cb.selectCase().when(cb.isNull(root.get("prdAllocatedTillDate")), 0)
							   .otherwise(root.get("prdAllocatedTillDate")).alias("PRD_ALLOCATED_TILL_DATE"),
			    cb.selectCase().when(cb.isNull(root.get("lrdAllocatedTillDate")), 0)
			    			   .otherwise(root.get("lrdAllocatedTillDate")).alias("LRD_ALLOCATED_TILL_DATE"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("osbyn").alias("OSBYN"),
				root.get("sectionName").alias("SECTION_NAME"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		  .where(cb.equal(root.get("contractNo"), contNo),
				 cb.equal(root.get("transactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectRetrotreetyXOLPremiumEdit(String contNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskXLPremiumDetails> root = cq.from(RskXLPremiumDetails.class);

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
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
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
				root.get("transactionMonthYear").alias("due_date"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("subClass").alias("SUB_CLASS"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		  .where(cb.equal(root.get("contractNo"), contNo),
				 cb.equal(root.get("transactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Map<String, Object>> getCurrencyList(String branchCode) {
		List<String> columns = new ArrayList<String>(Arrays.asList("CURRENCY_ID", "SHORT_NAME"));

		List<Map<String, Object>> resultList = new ArrayList<>();

		String nativeQuery = "select CURRENCY_ID,SHORT_NAME from currency_master where"
				+ " BRANCH_CODE=? and CURRENCY_ID in (SELECT Distinct(REGEXP_SUBSTR(BANK_CURRENCY,"
				+ "'[^,]+',1,LEVEL)) lc_login FROM (SELECT DISTINCT BANK_CURRENCY FROM bank_master"
				+ " WHERE BRANCH_CODE=? AND STATUS='Y') B CONNECT BY LEVEL <= LENGTH(BANK_CURRENCY) "
				+ "- LENGTH(REPLACE(BANK_CURRENCY,',',''))+1 and BANK_CURRENCY is not null)";

		Query query = em.createNativeQuery(nativeQuery);
		query.setParameter(1, branchCode);
		query.setParameter(2, branchCode);

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
	public List<Double> getGnpiPremium(GetAdjPremiumReq bean) {
		List<Tuple> rskGetGnpiPremiumList = rskGetGnpiPremium(bean);
		List<Double> coverGetGnpiPremiumList = coverGetGnpiPremium(bean);
		List<Double> resultList = new ArrayList<>();
		
		if(rskGetGnpiPremiumList != null && coverGetGnpiPremiumList != null) {
			int maxSize = rskGetGnpiPremiumList.size() >= coverGetGnpiPremiumList.size() ? 
					rskGetGnpiPremiumList.size() : coverGetGnpiPremiumList.size();
			
			for(int i = 0; i < maxSize; i++) {
				if(i<rskGetGnpiPremiumList.size() && i<coverGetGnpiPremiumList.size())
					resultList.add(( coverGetGnpiPremiumList.get(i) * 
							(Double) rskGetGnpiPremiumList.get(i).get("RSK_SHARE_SIGNED") *
							(Double) rskGetGnpiPremiumList.get(i).get("RSK_ADJRATE")  / 100)/ 100);
			}
		}
		return resultList;
	}
	
	private List<Tuple> rskGetGnpiPremium(GetAdjPremiumReq bean){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskProposal> root = cq.from(TtrnRiskProposal.class);
		
		Subquery<String> rskSq = cq.subquery(String.class);
		Root<TtrnRiskDetails> rskSubRoot = rskSq.from(TtrnRiskDetails.class);
		
		Subquery<Integer> endoSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endoSubRoot = endoSq.from(TtrnRiskDetails.class);

		endoSq.select(cb.max(endoSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endoSubRoot.get("rskContractNo"), rskSubRoot.get("rskContractNo")),
				cb.equal(endoSubRoot.get("rskLayerNo"), rskSubRoot.get("rskLayerNo")),
				cb.equal(endoSubRoot.get("rskOriginalCurr"), rskSubRoot.get("rskOriginalCurr")));

		rskSq.select(rskSubRoot.get("rskProposalNumber"))
			.where(cb.equal(rskSubRoot.get("rskContractNo"), bean.getContNo()),
				   cb.equal(rskSubRoot.get("rskLayerNo"), bean.getLayerno()),
				   cb.equal(rskSubRoot.get("rskOriginalCurr"), bean.getCurrency()),
				   cb.equal(rskSubRoot.get("rskEndorsementNo"), endoSq));
		
		Subquery<Integer> endowSq = cq.subquery(Integer.class);
		Root<TtrnRiskDetails> endowSubRoot = endowSq.from(TtrnRiskDetails.class);

		endowSq.select(cb.max(endowSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(endowSubRoot.get("rskProposalNumber"), root.get("rskProposalNumber")),
				cb.equal(endowSubRoot.get("rskLayerNo"), root.get("rskLayerNo")),
				cb.equal(endowSubRoot.get("branchCode"), root.get("branchCode")));

		cq.multiselect(cb.selectCase().when(cb.isNull(root.<Double>get("rskShareSigned")),0.0)
				.otherwise(root.<Double>get("rskShareSigned")).alias("RSK_SHARE_SIGNED"),
				root.<Double>get("rskAdjrate").alias("RSK_ADJRATE"))
		
		  .where(cb.equal(root.get("rskProposalNumber"), rskSq),
				  cb.equal(root.get("rskLayerNo"), bean.getLayerno()),
				  cb.equal(root.get("branchCode"), bean.getBranchCode()),
				  cb.equal(root.get("rskEndorsementNo"), endowSq));
		
		return em.createQuery(cq).getResultList();
	}
	
	private List<Double> coverGetGnpiPremium(GetAdjPremiumReq bean){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Double> cq = cb.createQuery(Double.class);
		Root<TtrnRiskProposal> root = cq.from(TtrnRiskProposal.class);
		
		Expression<Double> exp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("rskGnpiAsOff")),0.0)
			.otherwise(root.<Double>get("rskGnpiAsOff"));
		
		cq.multiselect(cb.sum(exp).alias("RSK_GNPI_AS_OFF"))
		.where(cb.equal(root.get("rskContractNo"), bean.getContNo()),
				  cb.equal(root.get("rskLayerNo"), bean.getLayerno()),
				  cb.equal(root.get("branchCode"), bean.getBranchCode()),
				  cb.equal(root.get("rskCoverClass"), bean.getPredepartment()),
				  cb.equal(root.get("rskEndorsementNo"), bean.getGnpiDate().split("_")[0]));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Double> getDepositPremium(GetAdjPremiumReq bean) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Double> cq = cb.createQuery(Double.class);
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		Expression<Double> mDpremiumOcExp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("mDpremiumOc")),0.0)
			.otherwise(root.<Double>get("mDpremiumOc"));
		
		Expression<Double> adjustmentExp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("adjustmentPremiumOc")),0.0)
				.otherwise(root.<Double>get("adjustmentPremiumOc"));
		
		cq.multiselect(cb.sum(cb.sum(mDpremiumOcExp, adjustmentExp)).alias("MD_PREMIUM"))
		.where(cb.equal(root.get("contractNo"), bean.getContNo()),
				  cb.equal(root.get("layerNo"), bean.getLayerno()),
				  cb.equal(root.get("premiumClass"), bean.getPredepartment()),
				  cb.equal(root.get("currencyId"), bean.getCurrency()),
				  cb.equal(root.get("branchCode"), bean.getBranchCode()));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> xolPremiumViewDetailsTemp(String branchCode, String contNo, String productId, String requestNo) {
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
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
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
				root.get("entryDate").alias("ENTRY_DATE"),
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
				//root.get("transactionMonthYear").alias("due_date"),
				root.get("riCession").alias("RI_CESSION"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				pDeptSq.alias("PREMIUM_CLASS"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("requestNo"), requestNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> premiumSelectXolPremiumView(String branchCode, String contNo, String productId,
			String requestNo) {
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
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
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
				//root.get("accountPeriodQtr").alias("due_date"),
				root.get("riCession").alias("RI_CESSION"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				pDeptSq.alias("PREMIUM_CLASS"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("transactionNo"), requestNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> premiumSelectRetroXolPremiumView(String branchCode, String contNo, String productId,
			String requestNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskXLPremiumDetails> root = cq.from(RskXLPremiumDetails.class);
		
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
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
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
				root.get("amendentDate").alias("AMENDMENT_DATE"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("accountPeriodQtr").alias("due_date"),
				root.get("mdPremium").alias("MD_PREMIUM"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				pDeptSq.alias("PREMIUM_CLASS"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("transactionNo"), requestNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public String premiumSelectCurrency(String currencyId, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<CurrencyMaster> root = cq.from(CurrencyMaster.class);
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<CurrencyMaster> subRoot = sq.from(CurrencyMaster.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")),
				cb.equal(subRoot.get("countryId"), root.get("countryId")),
				cb.equal(subRoot.get("status"), root.get("status")),
				cb.equal(subRoot.get("currencyId"), root.get("currencyId")));
		
		cq.multiselect(root.get("shortName").alias("CURRENCY_NAME"))
		
		.where(cb.equal(root.get("currencyId"),currencyId),
				cb.equal(root.get("branchCode"),branchCode),
				cb.equal(root.get("status"),"Y"),
				cb.equal(root.get("amendId"),sq));
		
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
	
	
	@Transactional
	public Integer updateMndInstallment(PremiumInsertMethodReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnMndInstallments> update = cb.createCriteriaUpdate(TtrnMndInstallments.class);
		Root<TtrnMndInstallments> root = update.from(TtrnMndInstallments.class);
		
		Subquery<Integer> endoSq = update.subquery(Integer.class);
		Root<TtrnMndInstallments> endoSubRoot = endoSq.from(TtrnMndInstallments.class);

		endoSq.select(cb.max(endoSubRoot.get("endorsementNo"))).where(
				cb.equal(endoSubRoot.get("contractNo"), root.get("contractNo")),
				cb.equal(endoSubRoot.get("layerNo"), root.get("layerNo")),
				cb.equal(endoSubRoot.get("installmentNo"), root.get("installmentNo")));

		update.set(root.get("transactionNo"), new BigDecimal(beanObj.getTransactionNo())==null? "":new BigDecimal(beanObj.getTransactionNo()));
		update.where(cb.equal(root.get("contractNo"), beanObj.getContNo()),
					 cb.equal(root.get("layerNo"), new BigDecimal(beanObj.getLayerno())==null? "":new BigDecimal(beanObj.getLayerno())),
					 cb.equal(root.get("installmentNo"), beanObj.getInstlmentNo()),
					 cb.equal(root.get("endorsementNo"), endoSq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Transactional
	public Integer facTempStatusUpdate(PremiumInsertMethodReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsTemp> update = cb.createCriteriaUpdate(RskPremiumDetailsTemp.class);
		Root<RskPremiumDetailsTemp> root = update.from(RskPremiumDetailsTemp.class);

		update.set(root.get("transStatus"), "A")
			  .set(root.get("reviewerId"), beanObj.getLoginId())
			  .set(root.get("reviewDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
			  .set(root.get("transactionNo"), new BigDecimal(beanObj.getTransactionNo())==null?"":new BigDecimal(beanObj.getTransactionNo()));

		update.where(cb.equal(root.get("requestNo"), beanObj.getRequestNo()),
					 cb.equal(root.get("branchCode"), beanObj.getBranchCode()));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public Integer updateRevTransactionNo(String input1, String input2) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		update.set(root.get("reverseTransactionNo"), input1);

		update.where(cb.equal(root.get("transactionNo"), new BigDecimal(input2)));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public Integer updateRevTransactionNoRetro(String input1, String input2) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskXLPremiumDetails> update = cb.createCriteriaUpdate(RskXLPremiumDetails.class);
		Root<RskXLPremiumDetails> root = update.from(RskXLPremiumDetails.class);

		update.set(root.get("reverseTransactionNo"), input1);

		update.where(cb.equal(root.get("transactionNo"), input2));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public Integer updateMndInstal(String input1, String input2) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnMndInstallments> update = cb.createCriteriaUpdate(TtrnMndInstallments.class);
		Root<TtrnMndInstallments> root = update.from(TtrnMndInstallments.class);

		update.set(root.get("transactionNo"), "");

		update.where(cb.equal(root.get("proposalNo"), input1),
				     cb.equal(root.get("transactionNo"), input2));

		Query q = em.createQuery(update);
		return q.executeUpdate();
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

		cq.multiselect(expressionToGetPaddedId.as(String.class));
		return em.createQuery(cq).getSingleResult();
	}

	@Transactional
	public Integer xolPremiumUpdateUpdateTemp(String[] args) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsTemp> update = cb.createCriteriaUpdate(RskPremiumDetailsTemp.class);
		Root<RskPremiumDetailsTemp> root = update.from(RskPremiumDetailsTemp.class);
		

		update.set(root.get("transactionMonthYear"), present(args[0]))
		.set(root.get("currencyId"), Double.parseDouble(args[1]))
		.set(root.get("exchangeRate"), Double.parseDouble(args[2]))
		.set(root.get("brokerage"), Double.parseDouble(args[3]))
		.set(root.get("brokerageAmtOc"), Double.parseDouble(args[4]))
		.set(root.get("tax"), Double.parseDouble(args[5]))
		.set(root.get("taxAmtOc"), Double.parseDouble(args[6]))
		.set(root.get("entryDateTime"), present(args[7]))
		.set(root.get("commission"), Double.parseDouble(args[8]))
		.set(root.get("mDpremiumOc"), Double.parseDouble(args[9]))
		.set(root.get("adjustmentPremiumOc"), Double.parseDouble(args[10]))
		.set(root.get("recPremiumOc"), Double.parseDouble(args[11]))
		.set(root.get("netdueOc"), Double.parseDouble(args[12]))
		.set(root.get("enteringMode"), args[13])
		.set(root.get("receiptNo"), Integer.parseInt(args[14]))
		.set(root.get("otherCostOc"), Double.parseDouble(args[15]))
		.set(root.get("brokerageAmtDc"), Double.parseDouble(args[16]))
		.set(root.get("taxAmtDc"), Double.parseDouble(args[17]))
		.set(root.get("mDpremiumDc"), Double.parseDouble(args[18]))
		.set(root.get("adjustmentPremiumDc"), Double.parseDouble(args[19]))
		.set(root.get("recPremiumDc"), Double.parseDouble(args[20]))
		.set(root.get("netdueDc"), Double.parseDouble(args[21]))
		.set(root.get("otherCostDc"), Double.parseDouble(args[22]))
		.set(root.get("cedantReference"), args[23])
		.set(root.get("remarks"), args[24])
		.set(root.get("totalCrOc"), Double.parseDouble(args[25]))
		.set(root.get("totalCrDc"), Double.parseDouble(args[26]))
		.set(root.get("totalDrOc"), Double.parseDouble(args[27]))
		.set(root.get("totalDrDc"), Double.parseDouble(args[28]))
		.set(root.get("amendmentDate"), StringUtils.isEmpty(args[29]) ?null :sdf.parse(args[29]))
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("withHoldingTaxOc"), Double.parseDouble(args[30]))
		.set(root.get("withHoldingTaxDc"), Double.parseDouble(args[31]))
		.set(root.get("riCession"), args[32])
		.set(root.get("subClass"), Integer.parseInt(args[33]))
		.set(root.get("tdsOc"), Double.parseDouble(args[34]))
		.set(root.get("tdsDc"), Double.parseDouble(args[35]))
		.set(root.get("vatPremiumOc"), Double.parseDouble(args[36]))
		.set(root.get("vatPremiumDc"), Double.parseDouble(args[37]))
		.set(root.get("bonusOc"), Double.parseDouble(args[38]))
		.set(root.get("bonusDc"), Double.parseDouble(args[39]))
		.set(root.get("gnpiEndtNo"), args[40])
		.set(root.get("premiumClass"), args[41])
		.set(root.get("statementDate"), present(args[42]))
		.set(root.get("brokerageVatOc"), Double.parseDouble(args[43]))
		.set(root.get("brokerageVatDc"), Double.parseDouble(args[44]))
		.set(root.get("documentType"), args[45]);

		update.where(cb.equal(root.get("contractNo"), args[46]),
				     cb.equal(root.get("requestNo"), args[47]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
		
	}

	@Transactional
	public Integer premiumUpdateXolUpdatePre(String[] args) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		update.set(root.get("transactionMonthYear"), present(args[0]))
		.set(root.get("currencyId"), Double.parseDouble(args[1]))
		.set(root.get("exchangeRate"), Double.parseDouble(args[2]))
		.set(root.get("brokerage"), Double.parseDouble(args[3]))
		.set(root.get("brokerageAmtOc"), Double.parseDouble(args[4]))
		.set(root.get("tax"), Double.parseDouble(args[5]))
		.set(root.get("taxAmtOc"), Double.parseDouble(args[6]))
		.set(root.get("entryDateTime"), present(args[7]))
		.set(root.get("commission"), Double.parseDouble(args[8]))
		.set(root.get("mDpremiumOc"), Double.parseDouble(args[9]))
		.set(root.get("adjustmentPremiumOc"), Double.parseDouble(args[10]))
		.set(root.get("recPremiumOc"), Double.parseDouble(args[11]))
		.set(root.get("netdueOc"), Double.parseDouble(args[12]))
		.set(root.get("enteringMode"), args[13])
		.set(root.get("receiptNo"), Integer.parseInt(args[14]))
		.set(root.get("otherCostOc"), Double.parseDouble(args[15]))
		.set(root.get("brokerageAmtDc"), Double.parseDouble(args[16]))
		.set(root.get("taxAmtDc"), Double.parseDouble(args[17]))
		.set(root.get("mDpremiumDc"), Double.parseDouble(args[18]))
		.set(root.get("adjustmentPremiumDc"), Double.parseDouble(args[19]))
		.set(root.get("recPremiumDc"), Double.parseDouble(args[20]))
		.set(root.get("netdueDc"), Double.parseDouble(args[21]))
		.set(root.get("otherCostDc"), Double.parseDouble(args[22]))
		.set(root.get("cedantReference"), args[23])
		.set(root.get("remarks"), args[24])
		.set(root.get("totalCrOc"), Double.parseDouble(args[25]))
		.set(root.get("totalCrDc"), Double.parseDouble(args[26]))
		.set(root.get("totalDrOc"), Double.parseDouble(args[27]))
		.set(root.get("totalDrDc"), Double.parseDouble(args[28]))
		.set(root.get("amendmentDate"), StringUtils.isEmpty(args[29]) ?null :sdf.parse(args[29]))
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("withHoldingTaxOc"), Double.parseDouble(args[30]))
		.set(root.get("withHoldingTaxDc"), Double.parseDouble(args[31]))
		.set(root.get("riCession"), args[32])
		.set(root.get("subClass"), Integer.parseInt(args[33]))
		.set(root.get("tdsOc"), Double.parseDouble(args[34]))
		.set(root.get("tdsDc"), Double.parseDouble(args[35]))
		.set(root.get("vatPremiumOc"), Double.parseDouble(args[36]))
		.set(root.get("vatPremiumDc"), Double.parseDouble(args[37]))
		.set(root.get("bonusOc"), Double.parseDouble(args[38]))
		.set(root.get("bonusDc"), Double.parseDouble(args[39]))
		.set(root.get("gnpiEndtNo"), args[40])
		.set(root.get("premiumClass"), args[41])
		.set(root.get("statementDate"), present(args[42]))
		.set(root.get("brokerageVatOc"), Double.parseDouble(args[43]))
		.set(root.get("brokerageVatDc"), Double.parseDouble(args[44]))
		.set(root.get("documentType"), args[45]);

		update.where(cb.equal(root.get("contractNo"), args[46]),
				     cb.equal(root.get("requestNo"), args[47]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
		
	}

	@Override
	@Transactional
	public Integer premiumUpdateRetroxolUpdatePre(String[] args) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskXLPremiumDetails> update = cb.createCriteriaUpdate(RskXLPremiumDetails.class);
		Root<RskXLPremiumDetails> root = update.from(RskXLPremiumDetails.class);

		update.set(root.get("transactionMonthYear"), present(args[0]))
		.set(root.get("currencyId"), Double.parseDouble(args[1]))
		.set(root.get("exchangeRate"), Double.parseDouble(args[2]))
		.set(root.get("brokerage"), Double.parseDouble(args[3]))
		.set(root.get("brokerageAmtOc"), Double.parseDouble(args[4]))
		.set(root.get("tax"), Double.parseDouble(args[5]))
		.set(root.get("taxAmtOc"), Double.parseDouble(args[6]))
		.set(root.get("entryDateTime"), present(args[7]))
		.set(root.get("commission"), Double.parseDouble(args[8]))
		.set(root.get("mDpremiumOc"), Double.parseDouble(args[9]))
		.set(root.get("adjustmentPremiumOc"), Double.parseDouble(args[10]))
		.set(root.get("recPremiumOc"), Double.parseDouble(args[11]))
		.set(root.get("netdueOc"), Double.parseDouble(args[12]))
		.set(root.get("enteringMode"), args[13])
		.set(root.get("receiptNo"), Integer.parseInt(args[14]))
		.set(root.get("otherCostOc"), Double.parseDouble(args[15]))
		.set(root.get("brokerageAmtDc"), Double.parseDouble(args[16]))
		.set(root.get("taxAmtDc"), Double.parseDouble(args[17]))
		.set(root.get("mDpremiumDc"), Double.parseDouble(args[18]))
		.set(root.get("adjustmentPremiumDc"), Double.parseDouble(args[19]))
		.set(root.get("recPremiumDc"), Double.parseDouble(args[20]))
		.set(root.get("netdueDc"), Double.parseDouble(args[21]))
		.set(root.get("otherCostDc"), Double.parseDouble(args[22]))
		.set(root.get("cedantReference"), args[23])
		.set(root.get("remarks"), args[24])
		.set(root.get("totalCrOc"), Double.parseDouble(args[25]))
		.set(root.get("totalCrDc"), Double.parseDouble(args[26]))
		.set(root.get("totalDrOc"), Double.parseDouble(args[27]))
		.set(root.get("totalDrDc"), Double.parseDouble(args[28]))
		.set(root.get("amendmentDate"), StringUtils.isEmpty(args[29]) ?null :sdf.parse(args[29]))
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("withHoldingTaxOc"), Double.parseDouble(args[30]))
		.set(root.get("withHoldingTaxDc"), Double.parseDouble(args[31]))
		.set(root.get("riCession"), args[32])
		.set(root.get("subClass"), Integer.parseInt(args[33]))
		.set(root.get("tdsOc"), Double.parseDouble(args[34]))
		.set(root.get("tdsDc"), Double.parseDouble(args[35]))
		.set(root.get("vatPremiumOc"), Double.parseDouble(args[36]))
		.set(root.get("vatPremiumDc"), Double.parseDouble(args[37]))
		.set(root.get("bonusOc"), Double.parseDouble(args[38]))
		.set(root.get("bonusDc"), Double.parseDouble(args[39]))
		.set(root.get("gnpiEndtNo"), args[40])
		.set(root.get("premiumClass"), args[41])
		.set(root.get("statementDate"), present(args[42]))
		.set(root.get("brokerageVatOc"), Double.parseDouble(args[43]))
		.set(root.get("brokerageVatDc"), Double.parseDouble(args[44]))
		.set(root.get("documentType"), args[45]);

		update.where(cb.equal(root.get("contractNo"), args[46]),
				     cb.equal(root.get("requestNo"), args[47]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public void premiumDetailArchive(PremiumInsertMethodReq beanObj, String netdueOc) {
		StoredProcedureQuery sp = em.createStoredProcedureQuery("PREMIUM_DETAIL_ARCHIVE");

		// Assign parameters
		sp.registerStoredProcedureParameter("pnContractNo", String.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnLayerNo", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnTranNno", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnCurrency", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnExchange", Double.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnPrmAmount", Double.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnDeptId", Integer.class, ParameterMode.IN);
		sp.registerStoredProcedureParameter("pnProducttId", Integer.class, ParameterMode.IN);

		// Set parameters
		sp.setParameter("pnContractNo", beanObj.getContNo());
		sp.setParameter("pnLayerNo",
				Integer.parseInt(StringUtils.isBlank(beanObj.getLayerno()) ? "0" : beanObj.getLayerno()));
		sp.setParameter("pnTranNno", Integer.parseInt(beanObj.getTransactionNo()));
		sp.setParameter("pnCurrency", Integer.parseInt(beanObj.getCurrencyId()));
		sp.setParameter("pnExchange", Double.parseDouble(beanObj.getExchRate()));
		sp.setParameter("pnPrmAmount", Double.parseDouble(netdueOc));
		sp.setParameter("pnDeptId", Integer.parseInt(beanObj.getDepartmentId()));
		sp.setParameter("pnProducttId", Integer.parseInt(beanObj.getProductId()));
		
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

	@Override
	public RskPremiumDetailsTemp facPremiumTempToMain(String requestNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RskPremiumDetailsTemp> cq = cb.createQuery(RskPremiumDetailsTemp.class);
		Root<RskPremiumDetailsTemp> root = cq.from(RskPremiumDetailsTemp.class);
		
		cq.multiselect(root)
		.where(cb.equal(root.get("requestNo"), requestNo),
			   cb.equal(root.get("branchCode"), branchCode));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public void premiumSpRetroSplit(PremiumInsertMethodReq beanObj) {
		try {
			StoredProcedureQuery sp = em.createStoredProcedureQuery("RetroPremium_Split_claim");

			// Assign parameters
			sp.registerStoredProcedureParameter("pvContractNo", String.class, ParameterMode.IN);
			sp.registerStoredProcedureParameter("pnLayerNo", String.class, ParameterMode.IN);
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
			sp.setParameter("pvContractNo", beanObj.getContNo());
			sp.setParameter("pnLayerNo", StringUtils.isEmpty(beanObj.getLayerno())? "0" :beanObj.getLayerno());
			sp.setParameter("pnProductId", Integer.parseInt(beanObj.getProductId()));
			sp.setParameter("pnPremiumTranNo", Double.parseDouble(beanObj.getTransactionNo()));
			sp.setParameter("pdPremTranDate", formatDate(beanObj.getTransaction()));
			sp.setParameter("pnCurrencyId", Integer.parseInt("3".equalsIgnoreCase(beanObj.getProductId()) ? beanObj.getCurrencyId() : beanObj.getCurrency()));
			sp.setParameter("pnExchange", Double.parseDouble(beanObj.getExchRate()));
			sp.setParameter("pnBranchCode", beanObj.getBranchCode());

			sp.setParameter("pvtransactionType", "P");
			sp.setParameter("pdAmendDate", beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate());
			sp.setParameter("pnReference", "");
			sp.setParameter("pnTreatyName", "");
			sp.setParameter("pnRemarks", "");
			sp.setParameter("pnUwYear", "");
			sp.setParameter("pnSubClass", "");
			sp.setParameter("retroCession", beanObj.getRicession());
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Integer prclDelete(PremiumInsertMethodReq beanObj) {
		  CriteriaBuilder cb = em.getCriteriaBuilder();
		  CriteriaDelete<TtrnRetroPrcl> delete = cb.createCriteriaDelete(TtrnRetroPrcl.class);
		  Root<TtrnRetroPrcl> root = delete.from(TtrnRetroPrcl.class);

		  delete.where(cb.equal(root.get("contractNo"), beanObj.getContNo()),
				  cb.equal(root.get("layerNo"), StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno()),
				  cb.equal(root.get("productId"), beanObj.getProductId()));

		  return em.createQuery(delete).executeUpdate();
	}
	

	/*@Override
	public String selectRPPremiumOC(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		Expression<String> exp = cb.selectCase().when(cb.isNull(cb.sum(root.get("recPremiumOc"))), "0")
					   .otherwise(cb.sum(root.get("recPremiumOc"))).as(String.class);
		
		cq.multiselect(exp)
		.where(cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("layerNo"), layerNo),
			   cb.equal(root.get("instalmentNumber"), "RP"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String xolSelectRPPremiumOC(String contractNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		//Need to change Table
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		
		Expression<String> exp = cb.selectCase().when(cb.isNull(cb.sum(root.get("recPremiumOc"))), "0")
					   .otherwise(cb.sum(root.get("recPremiumOc"))).as(String.class);
		
		cq.multiselect(exp.alias("REC_PREMIUM_OC"))
		.where(cb.equal(root.get("contractNo"), contractNo),
			   cb.equal(root.get("layerNo"), layerNo),
			   cb.equal(root.get("instalmentNumber"), "RP"));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> selectGetTreatySPRetro(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskProposal> rpRoot = cq.from(TtrnRiskProposal.class);
		Root<TtrnRiskDetails> rdRoot = cq.from(TtrnRiskDetails.class);
		
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnRiskProposal> subRoot = sq.from(TtrnRiskProposal.class);

		sq.select(cb.max(subRoot.get("rskEndorsementNo"))).where(
				cb.equal(subRoot.get("rskProposalNumber"), rpRoot.get("rskProposalNumber")));
		
		Subquery<Long> rdSq = cq.subquery(Long.class);
		Root<TtrnRiskDetails> rdSubRoot = rdSq.from(TtrnRiskDetails.class);

		rdSq.select(cb.max(rdSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(rdSubRoot.get("rskContractNo"), rdRoot.get("rskContractNo")));
		
		cq.multiselect(rpRoot.get("rskSpRetro").alias("RSK_SP_RETRO"),
					   rpRoot.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),
					   rpRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"))
		
		.where(cb.equal(rpRoot.get("rskProposalNumber"), rdRoot.get("rskProposalNumber")),
			   cb.equal(rpRoot.get("rskEndorsementNo"), sq),
			   cb.equal(rdRoot.get("rskContractNo"), contractNo),
			   cb.equal(rdRoot.get("rskEndorsementNo"), rdSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> selectInsDetails(String proposalNo, Integer noOfRetro) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnInsurerDetails> root = cq.from(TtrnInsurerDetails.class);
		
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnInsurerDetails> subRoot = sq.from(TtrnInsurerDetails.class);

		sq.select(cb.max(subRoot.<Long>get("endorsementNo")))
		   .where(cb.equal(root.get("proposalNo"), subRoot.get("proposalNo")),
				  cb.equal(root.get("insurerNo"), subRoot.get("insurerNo")));
		
		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("retroPercentage").alias("RETRO_PERCENTAGE"),
				root.get("type").alias("TYPE"),
				root.get("uwYear").alias("UW_YEAR"),
				root.get("retroType").alias("RETRO_TYPE"))
		
		.where(cb.equal(root.get("endorsementNo"), sq),
				cb.equal(root.get("proposalNo"),proposalNo),
				cb.between(root.get("insurerNo"), cb.literal(0), cb.literal(noOfRetro)))
		
		.orderBy(cb.asc(root.get("insurerNo")));
		
		return em.createQuery(cq).getResultList();
		
	}

	@Override
	public String selectGetNoRetroCess(String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRiskDetails> root = cq.from(TtrnRiskDetails.class);
		
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnRiskDetails> subRoot = sq.from(TtrnRiskDetails.class);

		sq.select(cb.max(subRoot.<Long>get("rskEndorsementNo")))
		   .where(cb.equal(subRoot.get("rskContractNo"), root.get("rskContractNo")));
		
		cq.multiselect(root.get("retroCessionaries").alias("RETRO_CESSIONARIES"))
		
		.where(cb.equal(root.get("rskContractNo"), contractNo),
				cb.equal(root.get("rskEndorsementNo"),sq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String selectGetSumOfShareSign(String contractNo, Integer shareSign) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRetroCessionary> root = cq.from(TtrnRetroCessionary.class);
		
		Expression<String> exp = cb.selectCase().when(cb.isNull(cb.sum(root.get("shareSigned"))), "0")
				   .otherwise(cb.sum(root.get("shareSigned"))).as(String.class);
	
		Subquery<Long> sq = cq.subquery(Long.class);
		Root<TtrnRetroCessionary> subRoot = sq.from(TtrnRetroCessionary.class);

		sq.select(cb.max(subRoot.<Long>get("amendId")))
		   .where(cb.equal(root.get("contractNo"), subRoot.get("contractNo")),
				  cb.equal(root.get("proposalStatus"), subRoot.get("proposalStatus")),
				  cb.equal(root.get("sno"), subRoot.get("sno")));
		
		cq.multiselect(exp.alias("SHARE_SIGNED"))
		
		.where(cb.equal(root.get("amendId"), sq),
				cb.equal(root.get("contractNo"),contractNo),
				cb.equal(root.get("proposalStatus"),"A"),
				cb.between(root.get("sno"), cb.literal(0), cb.literal(shareSign)))
		
		.orderBy(cb.asc(root.get("sno")));
		
		return em.createQuery(cq).getSingleResult();
	}*/
	
	@SuppressWarnings("deprecation")
	public Timestamp present(String date) {
		String newDateString = null;
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date d = new java.sql.Date(Date.parse(date));
			((SimpleDateFormat) formatter).applyPattern("yyyy-MM-dd hh:mm:ss");
			newDateString = formatter.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Timestamp.valueOf(newDateString);
	}
	@Override
	public void premiumRiSplit(PremiumInsertMethodReq req) {
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
		
		// execute SP
		sp.execute();
	}
	public Date formatDate(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf1.parse(input);
		return new java.sql.Date(date.getTime());
	}

	@Override
	public List<Tuple> premiumSelectXolPremiumViewRi(String branchCode, String contNo, String productId,
			String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsRi> root = cq.from(RskPremiumDetailsRi.class);
		
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

		cq.multiselect(root.get("contractNo").alias("CONTRACT_NO"),
				root.get("ritransactionNo").alias("RI_TRANSACTION_NO"),
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
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
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
				//root.get("accountPeriodQtr").alias("due_date"),
				root.get("riCession").alias("RI_CESSION"),
				deptSq.alias("SUB_CLASS"),//sq
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				pDeptSq.alias("PREMIUM_CLASS"),//sq
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		.where(cb.equal(root.get("contractNo"), contNo),
				cb.equal(root.get("ritransactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();  
	}

	@Override
	public List<Tuple> selectTreetyXOLPremiumEditRi(String contNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsRi> root = cq.from(RskPremiumDetailsRi.class);

		cq.multiselect(root.get("transactionMonthYear").alias("TRANS_DATE"),
				root.get("accountingPeriodDate").alias("ACCOUNTING_PERIOD_DATE"),
				root.get("statementDate").alias("STATEMENT_DATE"),
				root.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),
				root.get("accountPeriodYear").alias("ACCOUNT_PERIOD_YEAR"),
				root.get("currencyId").alias("CURRENCY_ID"),
				root.get("exchangeRate").alias("EXCHANGE_RATE"),
				root.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),
				root.get("taxAmtOc").alias("TAX_AMT_OC"),
				root.get("entryDateTime").alias("ENTRY_DATE_TIME"),
				root.get("amendmentDate").alias("AMENDMENT_DATE"),
				root.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),
				root.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),
				root.get("premiumSurplusOc").alias("PREMIUM_SURPLUS_OC"),
				root.get("commissionSurplusOc").alias("COMMISSION_SURPLUS_OC"),
				root.get("premiumPortfolioinOc").alias("PREMIUM_PORTFOLIOIN_OC"),
				root.get("claimPortfolioinOc").alias("CLAIM_PORTFOLIOIN_OC"),
				root.get("premiumPortfoliooutOc").alias("PREMIUM_PORTFOLIOOUT_OC"),
				root.get("lossReserveReleasedOc").alias("LOSS_RESERVE_RELEASED_OC"),
				root.get("premiumreserveQuotashareOc").alias("PREMIUMRESERVE_QUOTASHARE_OC"),
				root.get("cashLossCreditOc").alias("CASH_LOSS_CREDIT_OC"),
				root.get("lossReserveretainedOc").alias("LOSS_RESERVERETAINED_OC"),
				root.get("profitCommissionOc").alias("PROFIT_COMMISSION_OC"),
				root.get("cashLosspaidOc").alias("CASH_LOSSPAID_OC"),
				root.get("status").alias("STATUS"),
				root.get("remarks").alias("REMARKS"),
				root.get("netdueOc").alias("NETDUE_OC"),
				root.get("enteringMode").alias("ENTERING_MODE"),
				root.get("receiptNo").alias("RECEIPT_NO"),
				root.get("claimsPaidOc").alias("CLAIMS_PAID_OC"),
				root.get("settlementStatus").alias("SETTLEMENT_STATUS"),
				root.get("mDpremiumOc").alias("M_DPREMIUM_OC"),
				root.get("adjustmentPremiumOc").alias("ADJUSTMENT_PREMIUM_OC"),
				root.get("recPremiumOc").alias("REC_PREMIUM_OC"),
				root.get("commission").alias("COMMISSION"),
				//root.get("entryDateTime").alias("INS_DATE"),
				root.get("xlCostOc").alias("XL_COST_OC"),
				root.get("claimPortfolioOutOc").alias("CLAIM_PORTFOLIO_OUT_OC"),
				root.get("premiumReserveRealsedOc").alias("PREMIUM_RESERVE_REALSED_OC"),
				root.get("otherCostOc").alias("OTHER_COST_OC"),
				root.get("cedantReference").alias("CEDANT_REFERENCE"),
				root.get("totalCrOc").alias("TOTAL_CR_OC"),
				root.get("totalDrOc").alias("TOTAL_DR_OC"),
				root.get("interestOc").alias("INTEREST_OC"),
				root.get("osclaimLossupdateOc").alias("OSCLAIM_LOSSUPDATE_OC"),
				root.get("overrider").alias("OVERRIDER"),
				root.get("overriderAmtOc").alias("OVERRIDER_AMT_OC"),
				root.get("overriderAmtDc").alias("OVERRIDER_AMT_DC"),
				root.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),
				root.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
				root.get("riCession").alias("RI_CESSION"),
				root.get("subClass").alias("SUB_CLASS"),
				root.get("tdsOc").alias("TDS_OC"),
				root.get("tdsDc").alias("TDS_DC"),
				//root.get("stOc").alias("ST_OC"),
				//root.get("stDc").alias("ST_DC"),
				root.get("scCommOc").alias("SC_Comm_OC"),
				root.get("scCommDc").alias("SC_Comm_DC"),
				root.get("bonusOc").alias("BONUS_OC"),
				root.get("bonusDc").alias("BONUS_DC"),
				root.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
				root.get("lpcOc").alias("LPC_OC"),
				root.get("lpcDc").alias("LPC_DC"),
				root.get("gnpiEndtNo").alias("GNPI_ENDT_NO"),
				cb.selectCase().when(cb.isNull(root.get("prdAllocatedTillDate")), 0)
							   .otherwise(root.get("prdAllocatedTillDate")).alias("PRD_ALLOCATED_TILL_DATE"),
			    cb.selectCase().when(cb.isNull(root.get("lrdAllocatedTillDate")), 0)
			    			   .otherwise(root.get("lrdAllocatedTillDate")).alias("LRD_ALLOCATED_TILL_DATE"),
				root.get("premiumClass").alias("PREMIUM_CLASS"),
				root.get("osbyn").alias("OSBYN"),
				root.get("sectionName").alias("SECTION_NAME"),
				root.get("reverselStatus").alias("REVERSEL_STATUS"),
				root.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
				root.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
				root.get("vatPremiumOc").alias("VAT_PREMIUM_OC"),
				root.get("vatPremiumDc").alias("VAT_PREMIUM_DC"),
				root.get("brokerageVatOc").alias("BROKERAGE_VAT_OC"),
				root.get("brokerageVatDc").alias("BROKERAGE_VAT_DC"),
				root.get("documentType").alias("DOCUMENT_TYPE"))
		
		  .where(cb.equal(root.get("contractNo"), contNo),
				 cb.equal(root.get("ritransactionNo"), transactionNo));
		
		return em.createQuery(cq).getResultList();
	}
	@Transactional
	@Override
	public int premiumUpdateXolUpdatePreRi(String[] args) throws ParseException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsRi> update = cb.createCriteriaUpdate(RskPremiumDetailsRi.class);
		Root<RskPremiumDetailsRi> root = update.from(RskPremiumDetailsRi.class);

		update.set(root.get("transactionMonthYear"), present(args[0]))
		.set(root.get("currencyId"), Double.parseDouble(args[1]))
		.set(root.get("exchangeRate"), Double.parseDouble(args[2]))
		.set(root.get("brokerage"), Double.parseDouble(args[3]))
		.set(root.get("brokerageAmtOc"), Double.parseDouble(args[4]))
		.set(root.get("tax"), Double.parseDouble(args[5]))
		.set(root.get("taxAmtOc"), Double.parseDouble(args[6]))
		.set(root.get("entryDateTime"), present(args[7]))
		.set(root.get("commission"), Double.parseDouble(args[8]))
		.set(root.get("mDpremiumOc"), Double.parseDouble(args[9]))
		.set(root.get("adjustmentPremiumOc"), Double.parseDouble(args[10]))
		.set(root.get("recPremiumOc"), Double.parseDouble(args[11]))
		.set(root.get("netdueOc"), Double.parseDouble(args[12]))
		.set(root.get("enteringMode"), args[13])
		.set(root.get("receiptNo"), Integer.parseInt(args[14]))
		.set(root.get("otherCostOc"), Double.parseDouble(args[15]))
		.set(root.get("brokerageAmtDc"), Double.parseDouble(args[16]))
		.set(root.get("taxAmtDc"), Double.parseDouble(args[17]))
		.set(root.get("mDpremiumDc"), Double.parseDouble(args[18]))
		.set(root.get("adjustmentPremiumDc"), Double.parseDouble(args[19]))
		.set(root.get("recPremiumDc"), Double.parseDouble(args[20]))
		.set(root.get("netdueDc"), Double.parseDouble(args[21]))
		.set(root.get("otherCostDc"), Double.parseDouble(args[22]))
		.set(root.get("cedantReference"), args[23])
		.set(root.get("remarks"), args[24])
		.set(root.get("totalCrOc"), Double.parseDouble(args[25]))
		.set(root.get("totalCrDc"), Double.parseDouble(args[26]))
		.set(root.get("totalDrOc"), Double.parseDouble(args[27]))
		.set(root.get("totalDrDc"), Double.parseDouble(args[28]))
		.set(root.get("amendmentDate"), StringUtils.isEmpty(args[29]) ?null :sdf.parse(args[29]))
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
		.set(root.get("withHoldingTaxOc"), Double.parseDouble(args[30]))
		.set(root.get("withHoldingTaxDc"), Double.parseDouble(args[31]))
		.set(root.get("riCession"), args[32])
		.set(root.get("subClass"), Integer.parseInt(args[33]))
		.set(root.get("tdsOc"), Double.parseDouble(args[34]))
		.set(root.get("tdsDc"), Double.parseDouble(args[35]))
		.set(root.get("vatPremiumOc"), Double.parseDouble(args[36]))
		.set(root.get("vatPremiumDc"), Double.parseDouble(args[37]))
		.set(root.get("bonusOc"), Double.parseDouble(args[38]))
		.set(root.get("bonusDc"), Double.parseDouble(args[39]))
		.set(root.get("gnpiEndtNo"), args[40])
		.set(root.get("premiumClass"), args[41])
		.set(root.get("statementDate"), present(args[42]))
		.set(root.get("brokerageVatOc"), Double.parseDouble(args[43]))
		.set(root.get("brokerageVatDc"), Double.parseDouble(args[44]))
		.set(root.get("documentType"), args[45]);

		update.where(cb.equal(root.get("contractNo"), args[46]),
				     cb.equal(root.get("ritransactionNo"), args[47]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
}
