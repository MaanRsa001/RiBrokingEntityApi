package com.maan.insurance.jpa.repository.impl;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.facultative.TmasRiskGrade;
import com.maan.insurance.jpa.entity.facultative.TtrnFacRiskProposal;
import com.maan.insurance.jpa.entity.facultative.TtrnFacSi;
import com.maan.insurance.jpa.entity.facultative.TtrnLossDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.jpa.repository.facultative.FacultativeCustomRepository;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;

@Repository
public class FacultativeCustomRepositoryImpl implements FacultativeCustomRepository {

	@Autowired
	EntityManager em;

	@Override
	public String getShortName(String branchcode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TmasBranchMaster> root = cq.from(TmasBranchMaster.class);

		// Currecncy Master SQ
		Subquery<Integer> cmSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cmSubRoot = cmSq.from(CurrencyMaster.class);

		// AMEND_ID SQ
		Subquery<Integer> aSq = cmSq.subquery(Integer.class);
		Root<CurrencyMaster> aSubRoot = aSq.from(CurrencyMaster.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("currencyId"), cmSubRoot.get("currencyId")),
				cb.equal(aSubRoot.get("branchCode"), cmSubRoot.get("branchCode")));

		cmSq.select(cmSubRoot.get("shortName")).where(cb.equal(cmSubRoot.get("currencyId"), root.get("baseCurrencyId")),
				cb.equal(cmSubRoot.get("branchCode"), root.get("branchCode")),
				cb.equal(cmSubRoot.get("amendId"), cb.selectCase().when(cb.isNull(aSq), 0).otherwise(aSq)));

		cq.multiselect(cmSq).where(cb.equal(root.get("branchCode"), branchcode), cb.equal(root.get("status"), "Y"));

		List<String> result = em.createQuery(cq).getResultList();
		return result != null && !result.isEmpty() ? result.get(0) : null;
	}

	@Override
	public Integer updateUWShare(String shSd, String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRiskCommission> update = cb.createCriteriaUpdate(TtrnRiskCommission.class);
		Root<TtrnRiskCommission> root = update.from(TtrnRiskCommission.class);

		Subquery<Double> sq = update.subquery(Double.class);
		Root<TtrnRiskCommission> subRoot = sq.from(TtrnRiskCommission.class);

		sq.select(cb.max(subRoot.get("rskEndorsementNo")))
				.where(cb.equal(subRoot.get("rskProposalNumber"), proposalNo));

		update.set(root.get("rskLeadUwShare"), shSd).where(cb.equal(root.get("rskProposalNumber"), proposalNo),
				cb.equal(root.get("transactionNo"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<TtrnBonus> bonusMainSelect(String proposalNo, String branchCode, String acqBonus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnBonus> cq = cb.createQuery(TtrnBonus.class);
		Root<TtrnBonus> root = cq.from(TtrnBonus.class);

		// ENDORSEMENT_NO SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<TtrnBonus> subRoot = sq.from(TtrnBonus.class);

		sq.select(cb.max(subRoot.get("endorsementNo")))
				.where(cb.equal(root.get("proposalNo") , subRoot.get("proposalNo")),
						cb.equal(root.get("branch") , subRoot.get("branch")),
						cb.equal(root.get("type") , subRoot.get("type")));

		cq.multiselect(root)
		
		.where(cb.equal(root.get("proposalNo"), proposalNo),
			   cb.equal(root.get("branch"), branchCode),
			   cb.equal(root.get("type"), acqBonus))
		
		.orderBy(cb.asc(root.get("lcbId")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectUwYear(String productId, Date incepDate, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		// AmendID SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);

		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("productId") , root.get("productId")),
						cb.equal(subRoot.get("contractNo") , root.get("contractNo")),
						cb.equal(subRoot.get("retroType") , root.get("retroType")),
						cb.lessThan(cb.literal(incepDate) , subRoot.get("expiryDate")),
						cb.equal(subRoot.get("branchCode") , root.get("branchCode")),
						cb.equal(root.get("contractStatus") , subRoot.get("contractStatus")));

		cq.multiselect(root.get("uwYear").alias("CONTDET1"),
						root.get("uwYear").alias("CONTDET2"))
		
		.where(cb.equal(root.get("productId") , productId),
				cb.equal(root.get("contractStatus") , "A"),
				cb.isNotNull(root.get("contractNo")),
				cb.notEqual(root.get("contractNo"), cb.literal(0)),
				cb.equal(root.get("retroType") , "TR"),
				cb.lessThan(cb.literal(incepDate) , root.get("expiryDate")),
				cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("amendId") , sq))
		
		.orderBy(cb.asc(root.get("uwYear")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getRemarksDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskRemarks> root = cq.from(TtrnRiskRemarks.class);

		// AmendID SQ
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnRiskRemarks> subRoot = sq.from(TtrnRiskRemarks.class);

		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("proposalNo") , root.get("proposalNo")));

		cq.multiselect(root.get("rskSno").alias("RSK_SNO"),
						root.get("rskDescription").alias("RSK_DESCRIPTION"),
						root.get("rskRemark1").alias("RSK_REMARK1"),
						root.get("rskRemark2").alias("RSK_REMARK2"))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("layerNo") , layerNo),
				cb.equal(root.get("amendId") , sq))
		
		.orderBy(cb.asc(root.get("rskSno")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getLossDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//Need to change to TtrnLossDetails
		Root<TtrnRiskRemarks> root = cq.from(TtrnRiskRemarks.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		//Need to change to TtrnLossDetails
		Root<TtrnRiskRemarks> subRoot = sq.from(TtrnRiskRemarks.class);

		sq.select(cb.max(subRoot.get("endorsementNo")))
				.where(cb.equal(subRoot.get("proposalNo") , root.get("proposalNo")),
						cb.equal(subRoot.get("layerNo") , root.get("layerNo")));

		cq.multiselect(root.get("proposalNo").alias("PROPOSAL_NO"),
						root.get("contractNo").alias("CONTRACT_NO"),
						root.get("endorsementNo").alias("ENDORSEMENT_NO"),
						root.get("productId").alias("PRODUCT_ID"),
						root.get("branch").alias("BRANCH"),
						root.get("sysDate").alias("SYS_DATE"),
						root.get("layerNo").alias("LAYER_NO"),
						root.get("year").alias("YEAR"),
						root.get("lossNo").alias("LOSS_NO"),
						root.get("insuredName").alias("INSURED_NAME"),
						root.get("inceptionDate").alias("INCEPTION_DATE"),
						root.get("expirydate").alias("EXPIRYDATE"),
						root.get("dateOfLoss").alias("DATE_OF_LOSS"),
						root.get("causeOfLoss").alias("CAUSE_OF_LOSS"),
						root.get("insuredClaim").alias("INSURED_CLAIM"),
						root.get("premium").alias("PREMIUM"),
						root.get("lossRatio").alias("LOSS_RATIO"),
						root.get("leader").alias("LEADER"),
						root.get("itiReShare").alias("ITI_RE_SHARE"))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("layerNo") , layerNo),
				cb.equal(root.get("endorsementNo") , sq))
		
		.orderBy(cb.asc(root.get("year")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String bonusPreviousTypeCheck(String proposalNo, String branch, String layerno) {
		String result = null;
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnBonus> root = cq.from(TtrnBonus.class);

		// AmendID SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<TtrnBonus> subRoot = sq.from(TtrnBonus.class);

		sq.select(cb.max(subRoot.get("endorsementNo")))
		.where(cb.equal(subRoot.get("proposalNo") , root.get("proposalNo")),
				cb.equal(subRoot.get("branch") , root.get("branch")));

		cq.multiselect(root.get("type").alias("type")).distinct(true)
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("branch") , branch),
				cb.equal(root.get("layerno") , layerno),
				cb.equal(root.get("endorsementNo") , sq));
		
		result = em.createQuery(cq).getSingleResult();
		return result == null ? "NA" : result; 
	}

	@Override
	public Integer bonusMainDelete(String proposalNo, String branchCode, String acqBonus, String layerno) {
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		  CriteriaDelete<TtrnBonus> delete = cb.createCriteriaDelete(TtrnBonus.class);
		  Root<TtrnBonus> root = delete.from(TtrnBonus.class);

		  delete.where(cb.equal(root.get("proposalNo"), proposalNo),
				  cb.equal(root.get("branch"), branchCode),
				  cb.equal(root.get("type"), acqBonus),
				  cb.equal(root.get("layerno"), layerno));

		  return em.createQuery(delete).executeUpdate();
	}

	@Override
	public Integer bonusMainDelete2(String proposalNo, String endoNo, String branchCode, String acqBonus,
			String layerno) {
		 CriteriaBuilder cb = em.getCriteriaBuilder();
		  CriteriaDelete<TtrnBonus> delete = cb.createCriteriaDelete(TtrnBonus.class);
		  Root<TtrnBonus> root = delete.from(TtrnBonus.class);

		  delete.where(cb.equal(root.get("proposalNo"), proposalNo),
				  cb.equal(root.get("endorsementNo"), endoNo),
				  cb.equal(root.get("branch"), branchCode),
				  cb.equal(root.get("type"), acqBonus),
				  cb.equal(root.get("layerno"), layerno));

		  return em.createQuery(delete).executeUpdate();
	}

	@Override
	public Integer riskComUpdate(UpdateSecondPageReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRiskCommission> update = cb.createCriteriaUpdate(TtrnRiskCommission.class);
		Root<TtrnRiskCommission> root = update.from(TtrnRiskCommission.class);

		update.set(root.get("rskLeadUw"), beanObj.getLeaderUnderwriter())
				.set(root.get("rskLeadUwShare"), beanObj.getLeaderUnderwritershare())
				.set(root.get("rskLeadUnderwriterCountry"), beanObj.getLeaderUnderwritercountry())
				.set(root.get("rskExclusion"), beanObj.getExclusion())
				.set(root.get("rskCreastaStatus"), beanObj.getCrestaStatus()).set(root.get("rskDocStatus"),
						StringUtils.isEmpty(beanObj.getDocStatus()) ? "" : beanObj.getDocStatus());

		update.where(cb.equal(root.get("rskProposalNumber"), beanObj.getProposalNo()),
				cb.equal(root.get("rskEndorsementNo"),
						StringUtils.isEmpty(beanObj.getEndorsmentno()) ? "0" : beanObj.getEndorsmentno()));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Integer bonusCountMain(String proposalNo, String branchCode, String acqBonus, String endorsmentno, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnBonus> root = cq.from(TtrnBonus.class);

		cq.multiselect(cb.count(root.get("lcbFrom")))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("branch") , branchCode),
				cb.equal(root.get("type") , acqBonus),
				cb.equal(root.get("endorsementNo") , endorsmentno),
				cb.equal(root.get("layerno") , layerNo));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public String getSignShareProduct1(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnFacRiskProposal> root = cq.from(TtrnFacRiskProposal.class);
		
		Expression<Double> exp = cb.<Double>selectCase().when(cb.isNull(root.<Double>get("shareSigned")), 0.0)
						.otherwise(root.<Double>get("shareSigned"));
		
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<TtrnFacRiskProposal> subRoot = sq.from(TtrnFacRiskProposal.class);

		sq.select(cb.max(subRoot.get("rskEndorsementNo")))
		.where(cb.equal(subRoot.get("rskProposalNumber") , root.get("rskProposalNumber")));
		
		cq.multiselect(exp.as(String.class).alias("SHARE_SIGNED"))
		.where(cb.equal(root.get("rskProposalNumber") , proposalNo),
				cb.equal(root.get("rskEndorsementNo") , sq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> getXolcoverDeductableDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//Need to change to TtrnFacSI.class
		Root<TtrnFacRiskProposal> root = cq.from(TtrnFacRiskProposal.class);
		
		// AMEND_ID SQ
		Subquery<Double> aSq = cq.subquery(Double.class);
		//Need to change to TtrnFacSI.class
		Root<TtrnFacRiskProposal> aSubRoot = aSq.from(TtrnFacRiskProposal.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")),
				cb.equal(aSubRoot.get("layerNo"), root.get("layerNo")),
				cb.equal(aSubRoot.get("rskBusinessType"), root.get("rskBusinessType")));
		
		cq.multiselect(root.get("pmlPercentage").alias("PML_PERCENTAGE"),
				root.get("rskSno").alias("RSK_SNO"),
				root.get("rskClass").alias("RSK_CLASS"),
				root.get("rskSubclass").alias("RSK_SUBCLASS"),
				root.get("rskType").alias("RSK_TYPE"),
				root.get("rskCoverlimitOc").alias("RSK_COVERLIMIT_OC"),
				root.get("rskDeductablelimitOc").alias("RSK_DEDUCTABLELIMIT_OC"),
				root.get("rskCoverageDays").alias("RSK_COVERAGE_DAYS"),
				root.get("rskDeductableDays").alias("RSK_DEDUCTABLE_DAYS"),
				root.get("rskPremiumRate").alias("RSK_PREMIUM_RATE"),
				root.get("rskGwpiOc").alias("RSK_GWPI_OC"),
				root.get("rskCoverRemarks").alias("RSK_COVER_REMARKS"),
				root.get("pmlHunPerOc").alias("PML_HUN_PER_OC"))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("layerNo") , layerNo),
				cb.equal(root.get("rskBusinessType") , "2"),
				cb.equal(root.get("amendId") , aSq))
		
		.orderBy(cb.asc(root.get("rskSno")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getCoverDeductableDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//Need to change to TtrnFacSI.class
		Root<TtrnFacSi> root = cq.from(TtrnFacSi.class);
		
		// AMEND_ID SQ
		Subquery<Double> aSq = cq.subquery(Double.class);
		//Need to change to TtrnFacSI.class
		Root<TtrnFacSi> aSubRoot = aSq.from(TtrnFacSi.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")),
				cb.equal(aSubRoot.get("layerNo"), root.get("layerNo")),
				cb.equal(aSubRoot.get("rskBusinessType"), root.get("rskBusinessType")));
		
		cq.multiselect(root.get("pmlPercentage").alias("PML_PERCENTAGE"),
				root.get("rskSno").alias("RSK_SNO"),
				root.get("rskClass").alias("RSK_CLASS"),
				root.get("rskSubclass").alias("RSK_SUBCLASS"),
				root.get("rskType").alias("RSK_TYPE"),
				root.get("rskCoverlimitOc").alias("RSK_COVERLIMIT_OC"),
				root.get("rskDeductablelimitOc").alias("RSK_DEDUCTABLELIMIT_OC"),
				root.get("rskCoverageDays").alias("RSK_COVERAGE_DAYS"),
				root.get("rskDeductableDays").alias("RSK_DEDUCTABLE_DAYS"),
				root.get("rskPremiumRate").alias("RSK_PREMIUM_RATE"),
				root.get("rskGwpiOc").alias("RSK_GWPI_OC"),
				root.get("rskCoverRemarks").alias("RSK_COVER_REMARKS"),
				root.get("pmlHunPerOc").alias("PML_HUN_PER_OC"))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("layerNo") , layerNo),
				cb.equal(root.get("rskBusinessType") , "1"),
				cb.equal(root.get("amendId") , aSq))
		
		.orderBy(cb.asc(root.get("rskSno")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public Integer deleteRemarksDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<TtrnRiskRemarks> delete = cb.createCriteriaDelete(TtrnRiskRemarks.class);
		Root<TtrnRiskRemarks> root = delete.from(TtrnRiskRemarks.class);

		// AMEND_ID SQ
		Subquery<Double> aSq = delete.subquery(Double.class);
		Root<TtrnRiskRemarks> aSubRoot = aSq.from(TtrnRiskRemarks.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")));

		delete.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("amendId"), aSq));

		return em.createQuery(delete).executeUpdate();
	}

	@Override
	public String ttrnRiskRemarksAmendId(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnRiskRemarks> root = cq.from(TtrnRiskRemarks.class);

		Expression<Integer> exp = cb
				.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("amendId"))), 0)
						.otherwise(cb.max(root.<Integer>get("amendId"))), 1);

		cq.multiselect(exp.as(String.class).alias("AMEND_ID")).where(cb.equal(root.get("proposalNo"), proposalNo));
		return em.createQuery(cq).getSingleResult();

	}

	@Override
	public Integer deleteCoverdeductableDetails(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<TtrnFacSi> delete = cb.createCriteriaDelete(TtrnFacSi.class);
		Root<TtrnFacSi> root = delete.from(TtrnFacSi.class);

		// AMEND_ID SQ
		Subquery<Double> aSq = delete.subquery(Double.class);
		Root<TtrnFacSi> aSubRoot = aSq.from(TtrnFacSi.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")));

		delete.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("amendId"), aSq));

		return em.createQuery(delete).executeUpdate();
	}

	@Override
	public String ttrnFacSiAmendId(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnFacSi> root = cq.from(TtrnFacSi.class);

		Expression<Integer> exp = cb
				.sum(cb.<Integer>selectCase().when(cb.isNull(cb.max(root.<Integer>get("amendId"))), 0)
						.otherwise(cb.max(root.<Integer>get("amendId"))), 1);

		cq.multiselect(exp.as(String.class).alias("AMEND_ID")).where(cb.equal(root.get("proposalNo"), proposalNo));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> facSelectRetroContDetTR(String productId, String year, Date incepDate, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		// AmendID SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);
		
		Expression<String> typeExp = cb.<String>selectCase().when(cb.isNull(root.<String>get("retroType")), "N").otherwise(root.<String>get("retroType"));
		
		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("productId") , root.get("productId")),
						cb.equal(subRoot.get("contractNo") , root.get("contractNo")),
						typeExp.in(Arrays.asList(new String[]{"TR", "FO", "TO"})),
						cb.equal(subRoot.get("retroType") , root.get("retroType")),
						cb.equal(root.get("uwYear") , year),
						cb.lessThan(cb.literal(incepDate) , subRoot.get("expiryDate")),
						cb.equal(subRoot.get("branchCode") , root.get("branchCode")),
						cb.equal(root.get("rskDummyContract") , "N"));
		
		Expression<String> contExp = cb.concat(cb.concat(root.get("contractNo").as(String.class), "-"),
				root.get("layerNo").as(String.class));
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("productId"), 4), root.get("contractNo"))
					   .when(cb.equal(root.get("productId"), 5), contExp)
					   .otherwise(null);
					   
		cq.multiselect(exp.alias("CONTDET1"), exp.alias("CONTDET2"))
		
		.where(cb.equal(root.get("productId") , productId),
				cb.equal(root.get("contractStatus") , "A"),
				cb.isNotNull(root.get("contractNo")),
				cb.notEqual(root.get("contractNo"), cb.literal(0)),
				typeExp.in(Arrays.asList(new String[]{"TR", "FO", "TO"})),
				cb.equal(root.get("uwYear") , year),
				cb.lessThan(cb.literal(incepDate) , root.get("expiryDate")),
				cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("rskDummyContract"), "N"),
				cb.equal(root.get("amendId") , sq))
		
		.orderBy(cb.asc(root.get("contractNo")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectRetroContDet(String productId, String type, String year, Date incepDate,
			String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		// AmendID SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);
		
		Expression<String> typeExp = cb.<String>selectCase().when(cb.isNull(root.<String>get("retroType")), "N").otherwise(root.<String>get("retroType"));
		Expression<String> typeExpCheck = cb.<String>selectCase().when(cb.equal(root.<String>get("productId"), "4"), cb.literal(type)).otherwise(cb.literal("N"));
		
		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("productId") , root.get("productId")),
						cb.equal(subRoot.get("contractNo") , root.get("contractNo")),
						cb.equal(typeExp, typeExpCheck),
						cb.equal(subRoot.get("retroType") , root.get("retroType")),
						cb.equal(root.get("uwYear") , year),
						cb.lessThan(cb.literal(incepDate) , subRoot.get("expiryDate")),
						cb.equal(subRoot.get("branchCode") , root.get("branchCode")),
						cb.equal(root.get("rskDummyContract") , "N"));
		
		Expression<String> contExp = cb.concat(cb.concat(root.get("contractNo").as(String.class), "-"),
				root.get("layerNo").as(String.class));
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("productId"), 4), root.get("contractNo"))
					   .when(cb.equal(root.get("productId"), 5), contExp)
					   .otherwise(null);
					   
		cq.multiselect(exp.alias("CONTDET1"), exp.alias("CONTDET2"))
		
		.where(cb.equal(root.get("productId") , productId),
				cb.equal(root.get("contractStatus") , "A"),
				cb.isNotNull(root.get("contractNo")),
				cb.notEqual(root.get("contractNo"), cb.literal(0)),
				cb.equal(typeExp, typeExpCheck),
				cb.equal(root.get("uwYear") , year),
				cb.lessThan(cb.literal(incepDate) , root.get("expiryDate")),
				cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("rskDummyContract"), "N"),
				cb.equal(root.get("amendId") , sq))
		
		.orderBy(cb.asc(root.get("contractNo")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectRetroDupContract(String productId, String type, String year, Date incepDate,
			String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		// AmendID SQ
		Subquery<Double> sq = cq.subquery(Double.class);
		Root<PositionMaster> subRoot = sq.from(PositionMaster.class);
		
		Expression<String> typeExp = cb.<String>selectCase().when(cb.isNull(root.<String>get("retroType")), "N").otherwise(root.<String>get("retroType"));
		Expression<String> typeExpCheck = cb.<String>selectCase().when(cb.equal(root.<String>get("productId"), "4"), cb.literal(type)).otherwise(cb.literal("N"));
		
		sq.select(cb.max(subRoot.get("amendId")))
				.where(cb.equal(subRoot.get("productId") , root.get("productId")),
						cb.equal(subRoot.get("contractNo") , root.get("contractNo")),
						cb.equal(typeExp, typeExpCheck),
						cb.equal(subRoot.get("retroType") , root.get("retroType")),
						cb.equal(root.get("uwYear") , year),
						cb.lessThan(cb.literal(incepDate) , subRoot.get("expiryDate")),
						cb.equal(subRoot.get("branchCode") , root.get("branchCode")),
						cb.equal(root.get("rskDummyContract") , "D"));
		
		Expression<String> contExp = cb.concat(cb.concat(root.get("contractNo").as(String.class), "-"),
				root.get("layerNo").as(String.class));
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("productId"), 4), root.get("contractNo"))
					   .when(cb.equal(root.get("productId"), 5), contExp)
					   .otherwise(null);
					   
		cq.multiselect(exp.alias("CONTDET1"), exp.alias("CONTDET2"))
		
		.where(cb.equal(root.get("productId") , productId),
				cb.equal(root.get("contractStatus") , "A"),
				cb.isNotNull(root.get("contractNo")),
				cb.notEqual(root.get("contractNo"), cb.literal(0)),
				cb.equal(typeExp, typeExpCheck),
				cb.equal(root.get("uwYear") , year),
				cb.lessThan(cb.literal(incepDate) , root.get("expiryDate")),
				cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("rskDummyContract"), "D"),
				cb.equal(root.get("amendId") , sq))
		
		.orderBy(cb.asc(root.get("contractNo")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getCrestaDetailCount(String proposalNo, String amendId, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnCrestazoneDetails> root = cq.from(TtrnCrestazoneDetails.class);

		cq.multiselect(cb.count(root).as(String.class).alias("COUNT"))
		
		.where(cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("amendId") , amendId),
				cb.equal(root.get("branchCode") , branchCode));

		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Integer creataContractUpdate(String contractNo, String proposalNo, String amendId, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnCrestazoneDetails> update = cb.createCriteriaUpdate(TtrnCrestazoneDetails.class);
		Root<TtrnCrestazoneDetails> root = update.from(TtrnCrestazoneDetails.class);

		update.set(root.get("contractNo"), contractNo)

		.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("amendId"), amendId),
						cb.equal(root.get("branchCode"), branchCode));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Integer updateContractDetails(InsertBonusDetailsReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnBonus> update = cb.createCriteriaUpdate(TtrnBonus.class);
		Root<TtrnBonus> root = update.from(TtrnBonus.class);

		update.set(root.get("contractNo"), beanObj.getContractNo())

		.where(cb.equal(root.get("proposalNo"), beanObj.getProposalNo()),
				cb.equal(root.get("branch"), beanObj.getBranchCode()),
				cb.equal(root.get("type"), beanObj.getAcqBonus()),
				cb.equal(root.get("endorsementNo"), beanObj.getEndorsmentno()),
				cb.equal(root.get("layerNo"), "0"));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public String selectAmendId(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<PositionMaster> root = cq.from(PositionMaster.class);

		cq.multiselect(cb.max(root.get("amendId")).as(String.class))

		.where(cb.equal(root.get("proposalNo"), proposalNo));

		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Integer deleteFaculData(String proposalNo, String endorsmentno) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<TtrnInsurerDetails> delete = cb.createCriteriaDelete(TtrnInsurerDetails.class);
		Root<TtrnInsurerDetails> root = delete.from(TtrnInsurerDetails.class);

		delete.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("endorsementNo"), endorsmentno));

		return em.createQuery(delete).executeUpdate();
	}

	@Override
	public Integer facUpdateFacContractNo(String facContractNo, String contractNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
		Root<PositionMaster> root = update.from(PositionMaster.class);

		// AMEND_ID SQ
		Subquery<Double> aSq = update.subquery(Double.class);
		Root<PositionMaster> aSubRoot = aSq.from(PositionMaster.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(cb.equal(aSubRoot.get("contractNo"), root.get("contractNo")));

		update.set(root.get("facContractNo"), facContractNo)

				.where(cb.equal(root.get("contractNo"), contractNo), cb.equal(root.get("amendId"), aSq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Integer deleteTtrnMndInstakkments(String proposalNo, String endorsmentno) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<TtrnMndInstallments> delete = cb.createCriteriaDelete(TtrnMndInstallments.class);
		Root<TtrnMndInstallments> root = delete.from(TtrnMndInstallments.class);

		delete.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("endorsementNo"), endorsmentno));

		return em.createQuery(delete).executeUpdate();
	}

	@Override
	public Integer lossDelete(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<TtrnLossDetails> delete = cb.createCriteriaDelete(TtrnLossDetails.class);
		Root<TtrnLossDetails> root = delete.from(TtrnLossDetails.class);

		Subquery<Double> aSq = delete.subquery(Double.class);
		Root<TtrnLossDetails> aSubRoot = aSq.from(TtrnLossDetails.class);

		aSq.select(cb.max(aSubRoot.get("endorsementNo"))).where(
				cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")),
				cb.equal(aSubRoot.get("layerNo"), root.get("layerNo")));

		delete.where(cb.equal(root.get("proposalNo"), proposalNo), cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("endorsementNo"), aSq));

		return em.createQuery(delete).executeUpdate();
	}

	@Override
	public List<Tuple> facSelectViewInsDetails(String endorsmentno, String proposalNo, Double noOfInsurer) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnInsurerDetails> root = cq.from(TtrnInsurerDetails.class);
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("contractNo"), "0"), "")
								.otherwise(root.get("contractNo"));
		
		cq.multiselect(exp.alias("CONTRACT_NO"),
						root.get("retroPercentage").alias("RETRO_PER"),
						root.get("type").alias("TYPE"),
						root.get("uwYear").alias("UW_YEAR"),
						root.get("retroType").alias("RETRO_TYPE"))
		
		.where(cb.equal(root.get("endorsementNo"), endorsmentno),
				cb.equal(root.get("proposalNo") , proposalNo),
				cb.between(root.<Double>get("insurerNo"), 0.0, noOfInsurer))
		
		.orderBy(cb.asc(root.get("insurerNo")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectInsDetails(String proposalNo, Double noOfInsurer) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnInsurerDetails> root = cq.from(TtrnInsurerDetails.class);
		
		Subquery<Double> aSq = cq.subquery(Double.class);
		Root<TtrnInsurerDetails> aSubRoot = aSq.from(TtrnInsurerDetails.class);

		aSq.select(cb.max(aSubRoot.get("endorsementNo"))).where(
				cb.equal(root.get("proposalNo"), aSubRoot.get("proposalNo")),
				cb.equal(root.get("insurerNo"), aSubRoot.get("insurerNo")));
		
		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("contractNo"), "0"), "")
								.otherwise(root.get("contractNo"));
		
		cq.multiselect(exp.alias("CONTRACT_NO"),
						root.get("retroPercentage").alias("RETRO_PER"),
						root.get("type").alias("TYPE"),
						root.get("uwYear").alias("UW_YEAR"),
						root.get("retroType").alias("RETRO_TYPE"))
		
		.where(cb.equal(root.get("endorsementNo"), aSq),
				cb.equal(root.get("proposalNo") , proposalNo),
				cb.between(root.<Double>get("insurerNo"), 0.0, noOfInsurer))
		
		.orderBy(cb.asc(root.get("insurerNo")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectSecondPageDet(String proposalNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnFacRiskProposal> facRoot = cq.from(TtrnFacRiskProposal.class);
		Root<TtrnRiskCommission> comRoot = cq.from(TtrnRiskCommission.class);
		//Root<TmasRiskGrade> rgRoot = cq.from(TmasRiskGrade.class);
		
		// Grade DESC SQ
		Subquery<Long> gsq = cq.subquery(Long.class);
		Root<TmasRiskGrade> gSubRoot = gsq.from(TmasRiskGrade.class);

		gsq.select(gSubRoot.get("gradeDesc"))
				.where(cb.equal(facRoot.get("riskGrade"), gSubRoot.get("gradeId")),
						cb.equal(gSubRoot.get("status") , "Y"),
						cb.equal(gSubRoot.get("branchCode"), branchCode));

		Expression<Double> sum = cb.sum(cb.sum(cb.sum(comRoot.<Double>get("RSK_COMM"), 
				comRoot.<Double>get("RSK_BROKERAGE")), comRoot.<Double>get("RSK_TAX")),
				comRoot.<Double>get("RSK_OTHER_COST"));
		
		// FAC ENDORSEMENT_NO SQ
		Subquery<Double> facSq = cq.subquery(Double.class);
		Root<TtrnFacRiskProposal> subRoot = facSq.from(TtrnFacRiskProposal.class);

		facSq.select(cb.max(subRoot.get("rskEndorsementNo")))
				.where( cb.isNotNull(subRoot.get("rskEndorsementNo")),
						cb.equal(subRoot.get("rskProposalNumber") , proposalNo));
		
		// COM ENDORSEMENT_NO SQ
		Subquery<Double> comSq = cq.subquery(Double.class);
		Root<TtrnRiskCommission> comSubRoot = comSq.from(TtrnRiskCommission.class);

		comSq.select(cb.max(comSubRoot.get("rskEndorsementNo")))
				.where( cb.isNotNull(comSubRoot.get("rskEndorsementNo")),
						cb.equal(comSubRoot.get("rskProposalNumber") , proposalNo));

		cq.multiselect(facRoot.get("riskGrade").alias("RISK_GRADE"),
				gsq.alias("GRADE_DESC"),
				sum.alias("ACC_Percentage"),
				facRoot.get("occupationCode").alias("OCCUPATION_CODE"),
				facRoot.get("sumInsuredOurShareOc").alias("SUM_INSURED_OUR_SHARE_OC"),
				facRoot.get("gwpiOurShareOc").alias("GWPI_OUR_SHARE_OC"),
				facRoot.get("pmlOurShareOc").alias("PML_OUR_SHARE_OC"),
				facRoot.get("tplOurShareOc").alias("TPL_OUR_SHARE_OC"),
				facRoot.get("riskDetails").alias("RISK_DETAILS"),
				facRoot.get("firePort").alias("FIRE_PORT"),
				facRoot.get("scope").alias("SCOPE"),
				facRoot.get("mbInd").alias("MB_IND"),
				facRoot.get("categoryZone").alias("CATEGORY_ZONE"),
				facRoot.get("earthquakeWsInd").alias("EARTHQUAKE_WS_IND"),
				facRoot.get("wsThreatInd").alias("WS_THREAT_IND"),
				facRoot.get("eqThreat").alias("EQ_THREAT"),
				comRoot.get("rskComm").alias("RSK_COMM"),
				comRoot.get("rskBrokerage").alias("RSK_BROKERAGE"),
				comRoot.get("rskTax").alias("RSK_TAX"),
				comRoot.get("rskLossRecord").alias("RSK_LOSS_RECORD"),
				comRoot.get("rskDgmApproval").alias("RSK_DGM_APPROVAL"),
				comRoot.get("rskUnderwritterCode").alias("RSK_UNDERWRITTER_CODE"),
				comRoot.get("rskUwRecommendation").alias("RSK_UW_RECOMMENDATION"),
				comRoot.get("rskRemarks").alias("RSK_REMARKS"),
				comRoot.get("rskOthAccep").alias("RSK_OTH_ACCEP"),
				comRoot.get("rskRefToHo").alias("RSK_REF_TO_HO"),
				comRoot.get("rskAcquistionCostOc").alias("RSK_ACQUISTION_COST_OC"),
				comRoot.get("rskNoclaimbonusPercent").alias("RSK_NOCLAIMBONUS_PRCENT"),
				comRoot.get("rskBonusId").alias("RSK_BONUS_ID"),
				comRoot.get("rskAcquistionCostDc").alias("RSK_ACQUISTION_COST_DC"),
				facRoot.get("cu").alias("CU"),
				facRoot.get("cuRsn").alias("CU_RSN"),
				comRoot.get("rskOtherCost").alias("RSK_OTHER_COST"),
				facRoot.get("mLop").alias("M_LOP"),
				facRoot.get("aLop").alias("A_LOP"),
				facRoot.get("mndInstallments").alias("MND_INSTALLMENTS"),
				comRoot.get("rskLeadUnderwritterCountry").alias("RSK_LEAD_UNDERWRITER_COUNTRY"),
				comRoot.get("rskLeadUw").alias("RSK_LEAD_UW"),
				comRoot.get("rskLeadUwShare").alias("RSK_LEAD_UW_SHARE"),
				comRoot.get("rskExclusion").alias("RSK_EXCLUSION"),
				comRoot.get("rskCreastaStatus").alias("RSK_CREASTA_STATUS"))
		
		.where(cb.equal(facRoot.get("rskProposalNumber"), proposalNo),
			   cb.equal(facRoot.get("rskProposalNumber"), comRoot.get("rskProposalNumber")),
			   cb.isNotNull(facRoot.get("rskEndorsementNo")),
			   cb.isNotNull(comRoot.get("rskEndorsementNo")),
			   cb.equal(facRoot.get("rskEndorsementNo"), facSq),
			   cb.equal(comRoot.get("rskEndorsementNo"), comSq));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public String premiumSelectCeaseStatus(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<PositionMaster> root = cq.from(PositionMaster.class);
		
		Subquery<Double> aSq = cq.subquery(Double.class);
		Root<PositionMaster> aSubRoot = aSq.from(PositionMaster.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("proposalNo"), root.get("proposalNo")));
		
		cq.multiselect(root.get("ceaseStatus").alias("CEASE_STATUS"))
		
		.where( cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("amendId") , aSq));
		
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Tuple> riskSelectGetInstalmentData(String proposalNo, String layerNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnMndInstallments> root = cq.from(TtrnMndInstallments.class);
		
		Subquery<Double> aSq = cq.subquery(Double.class);
		Root<TtrnMndInstallments> aSubRoot = aSq.from(TtrnMndInstallments.class);

		aSq.select(cb.max(aSubRoot.get("endorsementNo"))).where(
				cb.equal(aSubRoot.get("proposalNo"), proposalNo),
				cb.equal(aSubRoot.get("layerNo"), layerNo),
				cb.isNotNull(aSubRoot.get("endorsementNo")));
		
		cq.multiselect(root.get("installmentNo").alias("INSTALLMENT_NO"),
				root.get("installmentDate").alias("INSTALLMENT_DATE"),
				root.get("mndPremiumOc").alias("MND_PREMIUM_OC"),
				root.get("paymentDueDay").alias("PAYEMENT_DUE_DAY"),
				root.get("transactionNo").alias("TRANSACTION_NO"))
		
		.where( cb.equal(root.get("proposalNo") , proposalNo),
				cb.equal(root.get("layerNo"), layerNo),
				cb.equal(root.get("endorsementNo") , aSq),
				cb.isNotNull(root.get("endorsementNo")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> facSelectShowSecondData(String proposalNo, String productId, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRiskDetails> rkRoot = cq.from(TtrnRiskDetails.class);
		Root<TmasPfcMaster> pfcRoot = cq.from(TmasPfcMaster.class);
		Root<PersonalInfo> personalRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> piRoot = cq.from(PersonalInfo.class);
		Root<TmasDepartmentMaster> tdmRoot = cq.from(TmasDepartmentMaster.class);
		
		Expression<String> nameExpression = cb.concat(cb.concat(piRoot.get("firstName"), " "),
				piRoot.get("lastName"));
		
		//Constant details sq
		Subquery<String> cSq = cq.subquery(String.class);
		Root<ConstantDetail> cSubRoot = cSq.from(ConstantDetail.class);

		cSq.select(cSubRoot.get("detailName")).where(
				cb.equal(rkRoot.get("rsEndorsementType"), cSubRoot.get("type")));
		
		//Personal Amend id sq
		Subquery<Double> personalSq = cq.subquery(Double.class);
		Root<PersonalInfo> aSubRoot = personalSq.from(PersonalInfo.class);

		personalSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("customerId"), personalRoot.get("customerId")),
				cb.equal(aSubRoot.get("customerType"), personalRoot.get("customerType")),
				cb.equal(aSubRoot.get("branchCode"), personalRoot.get("branchCode")));
		
		//Pi Amend id sq
		Subquery<Double> piSq = cq.subquery(Double.class);
		Root<PersonalInfo> piSubRoot = piSq.from(PersonalInfo.class);

		piSq.select(cb.max(piSubRoot.get("amendId"))).where(
				cb.equal(piSubRoot.get("customerId"), piRoot.get("customerId")),
				cb.equal(piSubRoot.get("customerType"), piRoot.get("customerType")),
				cb.equal(piSubRoot.get("branchCode"), piRoot.get("branchCode")));
		
		//RSK endorsement no sq
		Subquery<Double> rskSq = cq.subquery(Double.class);
		Root<TtrnRiskDetails> rskSubRoot = rskSq.from(TtrnRiskDetails.class);

		rskSq.select(cb.max(rskSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(rskSubRoot.get("rskProposalNumber"), proposalNo));
		
		cq.multiselect(rkRoot.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
				pfcRoot.get("tmasPfcName").alias("TMAS_PFC_NAME"),
				cb.literal("").alias("TMAS_SPFC_NAME"),
				personalRoot.get("companyName").alias("COMPANY"),
				nameExpression.alias("BROKER"),
				rkRoot.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				rkRoot.get("rskUwyear").alias("RSK_UWYEAR"),
				rkRoot.get("rskInsuredName").alias("RSK_INSURED_NAME"),
				tdmRoot.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
				cSq.alias("DETAIL_NAME"))
		
		.where( cb.equal(rkRoot.get("rskProposalNumber"), proposalNo),
				cb.equal(pfcRoot.get("tmasPfcId"), rkRoot.get("rskPfcid")),
				cb.equal(tdmRoot.get("tmasProductId") , productId),
				cb.equal(tdmRoot.get("tmasStatus") , "Y"),
				cb.equal(rkRoot.get("rskCedingid"), personalRoot.get("customerId")),
				cb.equal(pfcRoot.get("branchCode") , piRoot.get("branchCode")),
				cb.equal(piRoot.get("branchCode"), branchCode),
				cb.equal(piRoot.get("branchCode") , personalRoot.get("branchCode")),
				cb.equal(personalRoot.get("branchCode") , pfcRoot.get("branchCode")),
				cb.equal(rkRoot.get("rskBrokerId") , piRoot.get("customerId")),
				cb.equal(personalRoot.get("amendId") , personalSq),
				cb.equal(piRoot.get("amendId") , piSq),
				cb.equal(rkRoot.get("rskEndorsementNo") , rskSq),
				cb.equal(rkRoot.get("branchCode") , tdmRoot.get("branchCode")),
				cb.equal(rkRoot.get("rskDeptid") , tdmRoot.get("tmasDepartmentId")));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public Integer updateRiskDetails(FirstPageInsertReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRiskDetails> update = cb.createCriteriaUpdate(TtrnRiskDetails.class);
		Root<TtrnRiskDetails> root = update.from(TtrnRiskDetails.class);

		update.set(root.get("inwardBusType"), StringUtils.isEmpty(beanObj.getInwardType())? "" :beanObj.getInwardType())
		.set(root.get("rskReceiptPayment"), StringUtils.isEmpty(beanObj.getReceiptofPayment())? "" :beanObj.getReceiptofPayment())
		.set(root.get("rskLocIssued"), StringUtils.isEmpty(beanObj.getLocIssued())? "" :beanObj.getLocIssued())
		.set(root.get("rskLatitude"), StringUtils.isEmpty(beanObj.getLatitude())? "" :beanObj.getLatitude())
		.set(root.get("rskLongitude"), StringUtils.isEmpty(beanObj.getLongitude())? "" :beanObj.getLongitude())
		.set(root.get("rskVessalTonnage"), StringUtils.isEmpty(beanObj.getVessaletonnage())? "" :beanObj.getVessaletonnage())
		.set(root.get("rsEndorsementType"), StringUtils.isEmpty(beanObj.getEndorsmenttype())? "" :beanObj.getEndorsmenttype())
		.set(root.get("rskLocBnkName"), StringUtils.isEmpty(beanObj.getLocBankName())? "" :beanObj.getLocBankName())
		.set(root.get("rskLocCrdtPrd"), StringUtils.isEmpty(beanObj.getLocCreditPrd())? "" :beanObj.getLocCreditPrd())
		.set(root.get("rskLocCrdtAmt"), StringUtils.isEmpty(beanObj.getLocCreditAmt())? "" :beanObj.getLocCreditAmt().replaceAll(",", ""))
		.set(root.get("rskLocBenfcreName"), StringUtils.isEmpty(beanObj.getLocBeneficerName())? "" :beanObj.getLocBeneficerName())
		
		.where(cb.equal(root.get("rskProposalNumber"), beanObj.getProposalNo()),
				cb.equal(root.get("rskEndorsementNo"), StringUtils.isEmpty(beanObj.getEndorsmentno())?"0":beanObj.getEndorsmentno()));
		
		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Integer updateFacRiskProposal(FirstPageInsertReq beanObj) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnFacRiskProposal> update = cb.createCriteriaUpdate(TtrnFacRiskProposal.class);
		Root<TtrnFacRiskProposal> root = update.from(TtrnFacRiskProposal.class);
		
		update.set(root.get("coverLimitPmlOc"), StringUtils.isEmpty(beanObj.getDeductibleFacXolPml())? "" :beanObj.getDeductibleFacXolPml())
		.set(root.get("coverLimitPmlDc"), StringUtils.isEmpty(beanObj.getDeductibleFacXolPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getDeductibleFacXolPml(), beanObj.getUsCurrencyRate()))
		.set(root.get("coverLimitPmlOsOc"), StringUtils.isEmpty(beanObj.getDeductibleFacXolPmlOurShare())? "" :beanObj.getDeductibleFacXolPmlOurShare())
		.set(root.get("coverLimitPmlOsDc"), StringUtils.isEmpty(beanObj.getDeductibleFacXolPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getDeductibleFacXolPmlOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("gwpiPmlOc"), StringUtils.isEmpty(beanObj.getGwpiPml())? "" :beanObj.getGwpiPml())
		.set(root.get("gwpiPmlDc"), StringUtils.isEmpty(beanObj.getGwpiPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getGwpiPml(), beanObj.getUsCurrencyRate()))
		.set(root.get("gwpiPmlOsOc"), StringUtils.isEmpty(beanObj.getGwpiPmlOurShare())? "" :beanObj.getGwpiPmlOurShare())
		.set(root.get("gwpiPmlOsDc"), StringUtils.isEmpty(beanObj.getGwpiPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getGwpiPmlOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("pslOc"), StringUtils.isEmpty(beanObj.getPslOC())? "0" :beanObj.getPslOC())
		.set(root.get("pslDc"), StringUtils.isEmpty(beanObj.getPslOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPslOC(), beanObj.getUsCurrencyRate()))
		.set(root.get("pslOsOc"), StringUtils.isEmpty(beanObj.getPslOurShare())? "0" :beanObj.getPslOurShare())
		.set(root.get("pslOsDc"), StringUtils.isEmpty(beanObj.getPslOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPslOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("pllOc"), StringUtils.isEmpty(beanObj.getPllOC())? "0" :beanObj.getPllOC())
		.set(root.get("pllDc"), StringUtils.isEmpty(beanObj.getPllOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPllOC(), beanObj.getUsCurrencyRate()))
		.set(root.get("pllOsOc"), StringUtils.isEmpty(beanObj.getPllOurShare())? "0" :beanObj.getPllOurShare())
		.set(root.get("pllOsDc"), StringUtils.isEmpty(beanObj.getPllOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPllOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("pblOc"), StringUtils.isEmpty(beanObj.getPblOC())? "0" :beanObj.getPblOC())
		.set(root.get("pblDc"), StringUtils.isEmpty(beanObj.getPblOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPblOC(), beanObj.getUsCurrencyRate()))
		.set(root.get("pblOsOc"), StringUtils.isEmpty(beanObj.getPblOurShare())? "0" :beanObj.getPblOurShare())
		.set(root.get("pblOsDc"), StringUtils.isEmpty(beanObj.getPblOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPblOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("pml100Oc"), StringUtils.isEmpty(beanObj.getPmll())? "" :beanObj.getPmll())
		.set(root.get("pml100Dc"), StringUtils.isEmpty(beanObj.getPmll())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPmll(), beanObj.getUsCurrencyRate()))
		.set(root.get("pmlOsOc"), StringUtils.isEmpty(beanObj.getPmlOCOurShare())? "" :beanObj.getPmlOCOurShare())
		.set(root.get("pmlOsDc"), StringUtils.isEmpty(beanObj.getPmlOCOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPmlOCOurShare(), beanObj.getUsCurrencyRate()))
		.set(root.get("sumInsPmlOc"), StringUtils.isEmpty(beanObj.getSumInsuredPml())? "" :beanObj.getSumInsuredPml())
		.set(root.get("sumInsPmlDc"), StringUtils.isEmpty(beanObj.getSumInsuredPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getSumInsuredPml(), beanObj.getUsCurrencyRate()))
		.set(root.get("sumInsPmlOsOc"), StringUtils.isEmpty(beanObj.getSumInsuredPmlOurShare())? "" :beanObj.getSumInsuredPmlOurShare())
		.set(root.get("sumInsPmlOsDc"), StringUtils.isEmpty(beanObj.getSumInsuredPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getSumInsuredPmlOurShare(), beanObj.getUsCurrencyRate()))
		
		.where(cb.equal(root.get("rskProposalNumber"), beanObj.getProposalNo()),
				cb.equal(root.get("rskEndorsementNo"), StringUtils.isEmpty(beanObj.getEndorsmentno())?"0":beanObj.getEndorsmentno()));
		
		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	private String getosvalue(final String limitOrigCur,final String ExchangeRate) {
		double output=0.0;
		String result = "";
		try{
			double origCountryVal=0.0;
			if(limitOrigCur!=null){
				String val = limitOrigCur.replaceAll(",", "");
				if (!("".equalsIgnoreCase(val))&& Double.parseDouble(val) != 0) {
					origCountryVal = Double.parseDouble(val) / Double.parseDouble(ExchangeRate);
					final DecimalFormat myFormatter = new DecimalFormat("###.##");
					output = Double.parseDouble(myFormatter.format(origCountryVal));
					result = String.valueOf(output);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer facSpFacultativepage1(String[] args) {
		try {
			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("FACULTATIVE_PAGE1_ORG");

			// Assign parameters
			storedProcedure.registerStoredProcedureParameter("PROPOSAL_NUM", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ENDORSEMENT_NO", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_CONTRACT_NO", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("XCHANGE", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PFCID", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SPFCID", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("MONTH_YR", Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("UNDERWRITTER", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("DEPTID", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CEDINGID", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("BROKERID", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_INCEPTION_DATE", Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_EXPIRY_DATE", Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_ACCOUNT_DATE", Date.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ORIGINAL_CURR", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TERRITORY", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("INSURED_NAME", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("LOCA", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CITY", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CEDANT_RET", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("NR1", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("MAX_LIMIT", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("DEDUCTIBLE", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("INTREST", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SP_RETRO1", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PML1", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SI_PML", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SUM_INS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("GWPI", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PML_SI", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TPL", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SH_WT", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SH_SD", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_PRODUCTID", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("YEAR_UW", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("STATUS", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PROPOSAL_TYPE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("log_id", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_NO_OF_INSURERS", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_OLD_CONTRACTNO", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_RENEWAL_STATUS", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_PREMIUM_RATE", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_BRANCH", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_POLBRANCH", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_RSK_CEDRET_TYPE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SUM_INS_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("GWPI_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PML_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TPL_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_UPDATELOGIN", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_DEDUCTIBLE_FACXOL_OC", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_XOL_OC", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_XOL_OURSHARE_OC", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_NO_INSTALMENT", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_MODE_TRANSPORT_ID", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_VESSEL_NAME", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_VESSEL_AGE", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_LIMIT_PER_VESSEL_OC", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_LIMIT_PER_LOCATION_OC", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_Type", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_COUNTRIES_INCLUDE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_COUNTRIES_EXCLUDE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_DATA_MAP_CONT_NO", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("P_XOL_LAYER_NO", String.class, ParameterMode.IN);

			storedProcedure.setParameter("PROPOSAL_NUM", args[0]);
			storedProcedure.setParameter("ENDORSEMENT_NO", Long.parseLong(args[1]));
			storedProcedure.setParameter("p_CONTRACT_NO", args[2]);
			storedProcedure.setParameter("XCHANGE", Double.parseDouble(args[3]));
			storedProcedure.setParameter("PFCID", args[4]);
			storedProcedure.setParameter("SPFCID", args[5]);
			storedProcedure.setParameter("MONTH_YR", args[6]);
			storedProcedure.setParameter("UNDERWRITTER", args[7]);
			storedProcedure.setParameter("DEPTID", Long.parseLong(args[8]));
			storedProcedure.setParameter("CEDINGID", Long.parseLong(args[9]));
			storedProcedure.setParameter("BROKERID", Long.parseLong(args[10]));
			storedProcedure.setParameter("p_INCEPTION_DATE", args[11]);
			storedProcedure.setParameter("p_EXPIRY_DATE", args[12]);
			storedProcedure.setParameter("p_ACCOUNT_DATE", args[13]);
			storedProcedure.setParameter("ORIGINAL_CURR", args[14]);
			storedProcedure.setParameter("TERRITORY", args[15]);
			storedProcedure.setParameter("INSURED_NAME", args[16]);
			storedProcedure.setParameter("LOCA", args[17]);
			storedProcedure.setParameter("CITY", args[18]);
			storedProcedure.setParameter("CEDANT_RET", Double.parseDouble(args[19]));
			storedProcedure.setParameter("NR1", Double.parseDouble(args[20]));
			storedProcedure.setParameter("MAX_LIMIT", Double.parseDouble(args[21]));
			storedProcedure.setParameter("DEDUCTIBLE", Double.parseDouble(args[22]));
			storedProcedure.setParameter("INTREST", Double.parseDouble(args[23]));
			storedProcedure.setParameter("SP_RETRO1", Double.parseDouble(args[24]));
			storedProcedure.setParameter("PML1", Double.parseDouble(args[25]));
			storedProcedure.setParameter("SI_PML", Double.parseDouble(args[26]));
			storedProcedure.setParameter("SUM_INS", Double.parseDouble(args[27]));
			storedProcedure.setParameter("GWPI", Double.parseDouble(args[28]));
			storedProcedure.setParameter("PML_SI", Double.parseDouble(args[29]));
			storedProcedure.setParameter("TPL", Double.parseDouble(args[30]));
			storedProcedure.setParameter("SH_WT", Double.parseDouble(args[31]));
			storedProcedure.setParameter("SH_SD", Double.parseDouble(args[32]));
			storedProcedure.setParameter("p_PRODUCTID", Long.parseLong(args[33]));
			storedProcedure.setParameter("YEAR_UW", Long.parseLong(args[34]));
			storedProcedure.setParameter("STATUS", args[35]);
			storedProcedure.setParameter("PROPOSAL_TYPE", args[36]);
			storedProcedure.setParameter("log_id", Long.parseLong(args[37]));
			storedProcedure.setParameter("P_NO_OF_INSURERS", Long.parseLong(args[38]));
			storedProcedure.setParameter("P_OLD_CONTRACTNO", Long.parseLong(args[39]));
			storedProcedure.setParameter("P_RENEWAL_STATUS", args[40]);
			storedProcedure.setParameter("P_PREMIUM_RATE", Double.parseDouble(args[41]));
			storedProcedure.setParameter("P_BRANCH", Long.parseLong(args[42]));
			storedProcedure.setParameter("P_POLBRANCH", Long.parseLong(args[43]));
			storedProcedure.setParameter("P_RSK_CEDRET_TYPE", args[44]);
			storedProcedure.setParameter("SUM_INS_OS", Double.parseDouble(args[45]));
			storedProcedure.setParameter("GWPI_OS", Double.parseDouble(args[46]));
			storedProcedure.setParameter("PML_OS", Double.parseDouble(args[47]));
			storedProcedure.setParameter("TPL_OS", Double.parseDouble(args[48]));
			storedProcedure.setParameter("P_UPDATELOGIN", Long.parseLong(args[49]));
			storedProcedure.setParameter("P_DEDUCTIBLE_FACXOL_OC", Double.parseDouble(args[50]));
			storedProcedure.setParameter("P_XOL_OC", Double.parseDouble(args[51]));
			storedProcedure.setParameter("P_XOL_OURSHARE_OC", Double.parseDouble(args[52]));
			storedProcedure.setParameter("P_NO_INSTALMENT", Long.parseLong(args[53]));
			storedProcedure.setParameter("P_MODE_TRANSPORT_ID", Long.parseLong(args[54]));
			storedProcedure.setParameter("P_VESSEL_NAME", args[55]);
			storedProcedure.setParameter("P_VESSEL_AGE", Double.parseDouble(args[56]));
			storedProcedure.setParameter("P_LIMIT_PER_VESSEL_OC", Double.parseDouble(args[57]));
			storedProcedure.setParameter("P_LIMIT_PER_LOCATION_OC", Double.parseDouble(args[58]));
			storedProcedure.setParameter("p_Type", args[59]);
			storedProcedure.setParameter("P_COUNTRIES_INCLUDE", args[60]);
			storedProcedure.setParameter("p_COUNTRIES_EXCLUDE", args[61]);
			storedProcedure.setParameter("p_DATA_MAP_CONT_NO", Long.parseLong(args[62]));
			storedProcedure.setParameter("P_XOL_LAYER_NO", args[63]);

			// execute SP
			storedProcedure.execute();
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Integer facSpFacultativepage2(String[] arg) {
		try {
			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("FACULTATIVE_PAGE2");

			// Assign parameters
			storedProcedure.registerStoredProcedureParameter("PROPOSAL_NO", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CONTRACT_NO", Long.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SUM_INS_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("GWPI_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PML_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TPL_OS", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("RSK_GRADE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("OCC_CODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("RSK_DETAIL", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("FIRE_PT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("SCOP", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("MD_IND1", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CATEGORY_ZONE1", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("EQ_WS_IND", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("WS_THREAT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("EQ_TRT", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("COMM", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("BROKERGE", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("TAX", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("LOSS_REC", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("DGM_APP", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("UW_CODE", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("UW_RECOMM", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("REMARKS1", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("OTH_APP", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("REF_TO_HQ", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("ACC_COST", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CU1", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("CU_RSN1", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("p_OTHER_COST", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PM_LOP", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("PA_LOP", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("NOCLAIMBONUS_PRCENT", Double.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("BONUS_ID", String.class, ParameterMode.IN);

			storedProcedure.setParameter("PROPOSAL_NO", Long.parseLong(arg[0]));
			storedProcedure.setParameter("CONTRACT_NO", Long.parseLong(arg[1]));
			storedProcedure.setParameter("SUM_INS_OS", Double.parseDouble(arg[2]));
			storedProcedure.setParameter("GWPI_OS", Double.parseDouble(arg[3]));
			storedProcedure.setParameter("PML_OS", Double.parseDouble(arg[4]));
			storedProcedure.setParameter("TPL_OS", Double.parseDouble(arg[5]));
			storedProcedure.setParameter("RSK_GRADE",arg[6]);
			storedProcedure.setParameter("OCC_CODE",arg[7]);
			storedProcedure.setParameter("RSK_DETAIL",arg[8]);
			storedProcedure.setParameter("FIRE_PT",arg[9]);
			storedProcedure.setParameter("SCOP",arg[10]);
			storedProcedure.setParameter("MD_IND1",arg[11]);
			storedProcedure.setParameter("CATEGORY_ZONE1",arg[12]);
			storedProcedure.setParameter("EQ_WS_IND",arg[13]);
			storedProcedure.setParameter("WS_THREAT",arg[14]);
			storedProcedure.setParameter("EQ_TRT",arg[15]);
			storedProcedure.setParameter("COMM", Double.parseDouble(arg[16]));
			storedProcedure.setParameter("BROKERGE", Double.parseDouble(arg[17]));
			storedProcedure.setParameter("TAX", Double.parseDouble(arg[18]));
			storedProcedure.setParameter("LOSS_REC",arg[19]);
			storedProcedure.setParameter("DGM_APP",arg[20]);
			storedProcedure.setParameter("UW_CODE",arg[21]);
			storedProcedure.setParameter("UW_RECOMM",arg[22]);
			storedProcedure.setParameter("REMARKS1",arg[23]);
			storedProcedure.setParameter("OTH_APP",arg[24]);
			storedProcedure.setParameter("REF_TO_HQ",arg[25]);
			storedProcedure.setParameter("ACC_COST", Double.parseDouble(arg[26]));
			storedProcedure.setParameter("CU1", Double.parseDouble(arg[27]));
			storedProcedure.setParameter("CU_RSN1", Double.parseDouble(arg[28]));
			storedProcedure.setParameter("p_OTHER_COST", Double.parseDouble(arg[29]));
			storedProcedure.setParameter("PM_LOP", Double.parseDouble(arg[30]));
			storedProcedure.setParameter("PA_LOP", Double.parseDouble(arg[31]));
			storedProcedure.setParameter("NOCLAIMBONUS_PRCENT", Double.parseDouble(arg[32]));
			storedProcedure.setParameter("BONUS_ID",arg[33]);

			// execute SP
			storedProcedure.execute();
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<Tuple> facSelectContGen(String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnFacRiskProposal> aRoot = cq.from(TtrnFacRiskProposal.class);
		Root<TtrnRiskDetails> bRoot = cq.from(TtrnRiskDetails.class);
		
		//aRoot sq
		Subquery<Double> aSq = cq.subquery(Double.class);
		Root<TtrnFacRiskProposal> aSubRoot = aSq.from(TtrnFacRiskProposal.class);

		aSq.select(cb.max(aSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(aSubRoot.get("rskProposalNumber"), proposalNo));
		
		//bRoot sq
		Subquery<Double> bSq = cq.subquery(Double.class);
		Root<TtrnRiskDetails> bSubRoot = bSq.from(TtrnRiskDetails.class);

		bSq.select(cb.max(bSubRoot.get("rskEndorsementNo"))).where(
				cb.equal(bSubRoot.get("rskProposalNumber"), proposalNo));
		
		cq.multiselect(bRoot.get("rskStatus").alias("RSK_STATUS"),
				aRoot.get("shareSigned").alias("SHARE_SIGNED"),
				bRoot.get("rskContractNo").alias("RSK_CONTRACT_NO"))
		
		.where( cb.equal(aRoot.get("rskProposalNumber") , bRoot.get("rskProposalNumber")),
				cb.equal(aRoot.get("rskEndorsementNo"), aSq),
				cb.equal(bRoot.get("rskEndorsementNo"), bSq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public Integer commonUpdateRiskDetContNo(String contractNo, String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRiskDetails> update = cb.createCriteriaUpdate(TtrnRiskDetails.class);
		Root<TtrnRiskDetails> root = update.from(TtrnRiskDetails.class);

		update.set(root.get("rskContractNo"), contractNo).where(cb.equal(root.get("rskProposalNumber"), proposalNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public Integer commonUpdatePosMasDetContNo(String contractNo, String proposalNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
		Root<PositionMaster> root = update.from(PositionMaster.class);

		update.set(root.get("contractNo"), contractNo).set(root.get("contractStatus"), "A")
				.where(cb.equal(root.get("proposalNo"), proposalNo));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

}