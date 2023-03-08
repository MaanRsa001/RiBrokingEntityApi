package com.maan.insurance.service.impl.nonproportionality;

import java.math.BigDecimal;
import com.maan.insurance.model.entity.TtrnRip;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnRetroCessionary;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnIeModule;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.entity.TtrnRskClassLimits;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;
import com.maan.insurance.validation.Formatters;

@Repository
public class NonProportionalityCustomRepositoryImple implements NonProportionalityCustomRepository {
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private TtrnRiskDetailsRepository ttrnRiskDetailsRepository;
	@Autowired
	private Formatters fm;
	@Autowired
	private TtrnRiskProposalRepository ttrnRiskProposalRepository;
	@Autowired
	private PositionMasterRepository positionMasterRepository;
	@Autowired
	private TtrnRiskCommissionRepository ttrnRiskCommissionRepository;
	@Autowired
	private TtrnInsurerDetailsRepository ttrnInsurerDetailsRepository;
	
	@Override
	public String getShortName(String branchCode) { 
		String Short="";
		try {
			//GET_SHORT_NAME
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<TmasBranchMaster> tbm = query.from(TmasBranchMaster.class);
			//	shortName
			Subquery<String> shortName = query.subquery(String.class); 
			Root<CurrencyMaster> cm = shortName.from(CurrencyMaster.class);
			shortName.select(cm.get("shortName"));
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<CurrencyMaster> cms = amend.from(CurrencyMaster.class);
			amend.select(cb.max(cb.<Long>selectCase().when(cb.isNull(cms.get("amendId")), 0l)
					.otherwise(cms.get("amendId"))));
			Predicate b3 = cb.equal( cms.get("currencyId"), cm.get("currencyId"));
			Predicate b4 = cb.equal( cms.get("branchCode"), cm.get("branchCode"));
			amend.where(b3,b4);
			
			Predicate a1 = cb.equal( tbm.get("baseCurrencyId"), cm.get("currencyId"));
			Predicate a2 = cb.equal( tbm.get("branchCode"), cm.get("branchCode"));
			Predicate a3 = cb.equal( cm.get("amendId"), amend);
			shortName.where(a1,a2,a3);
			
			query.multiselect(shortName.alias("COUNTRY_SHORT_NAME")); 

			Predicate n1 = cb.equal(tbm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(tbm.get("status"), "Y");
			query.where(n1,n2);
			
			TypedQuery<String> reslt = em.createQuery(query);
			List<String> list = reslt.getResultList();
			if (!CollectionUtils.isEmpty(list)) {
				Short = list.get(0) == null ? ""
						: list.get(0);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Short;
	}

	@Override
	public List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId) {
		//risk.select.getSecPageData //TMAS_SPFC_NAME pending
		List<Tuple> list = new ArrayList<>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> rk = query1.from(TtrnRiskDetails.class);
			Root<PersonalInfo> personal = query1.from(PersonalInfo.class);
			Root<TtrnRiskProposal> rp = query1.from(TtrnRiskProposal.class);
			Root<PersonalInfo> pi = query1.from(PersonalInfo.class);
			Root<TtrnRiskCommission> rc = query1.from(TtrnRiskCommission.class);
			Root<TmasDepartmentMaster> tdm = query1.from(TmasDepartmentMaster.class);
		
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
			
			query1.multiselect(
					rk.get("rskSpfcid").alias("RSK_SPFCID"),  
					tdm.get("branchCode").alias("BRANCH_CODE"),
					tdm.get("tmasProductId").alias("TMAS_PRODUCT_ID"),
					rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
					rk.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					personal.get("companyName").alias("CEDING_COMPANY"),
					rk.get("rskCedingid").alias("RSK_CEDINGID"),
					rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
					rp.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
					rp.get("rskLimitOc").alias("RSK_LIMIT_OC"),
					rp.get("rskLimitDc").alias("RSK_LIMIT_DC"),
					rp.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),
					rp.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
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
			
			//endRp
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
			predicates.add(cb.equal(rc.get("rskEndorsementNo"), endRc));
			predicates.add(cb.equal(tdm.get("branchCode"), branchCode));
			predicates.add(cb.equal(tdm.get("tmasProductId"), productId));
		
			query1.where(predicates.toArray(new Predicate[0]));
		
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getRemarksDetails(String proposalNo, String layerNo) {
		List<Tuple> list = new ArrayList<>();
		try {
		//GET_REMARKS_DETAILS
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<TtrnRiskRemarks> rd = query.from(TtrnRiskRemarks.class);

		query.multiselect(rd.get("rskSNo").alias("RSK_SNO"),rd.get("rskDescription").alias("RSK_DESCRIPTION"),
				rd.get("rskRemark1").alias("RSK_REMARK1"),rd.get("rskRemark2").alias("RSK_REMARK2")); 

		Subquery<Long> amend = query.subquery(Long.class); 
		Root<TtrnRiskRemarks> rds = amend.from(TtrnRiskRemarks.class);
		amend.select(cb.max(rds.get("amendId")));
		Predicate a1 = cb.equal( rds.get("proposalNo"), rd.get("proposalNo"));
		amend.where(a1);

		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(rd.get("rskSNo")));
		
		Predicate n1 = cb.equal(rd.get("proposalNo"), proposalNo);
		Predicate n2 = cb.equal(rd.get("layerNo"), layerNo);
		Predicate n4 = cb.equal(rd.get("amendId"), amend);
		query.where(n1,n2,n4).orderBy(orderList);

		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> facSelectViewInsDetails(String[] args) {
		List<Tuple> list = new ArrayList<>();
		try {
			//fac.select.viewInsDetails
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnInsurerDetails> rd = query1.from(TtrnInsurerDetails.class);
			query1.multiselect(rd.get("retroPercentage").alias("RETRO_PER"),rd.get("type").alias("TYPE"),
					rd.get("uwYear").alias("UW_YEAR"),rd.get("retroType").alias("RETRO_TYPE"),
					cb.selectCase().when(cb.equal(rd.get("contractNo"),"0"), "").otherwise(rd.get("contractNo")).alias("CONTRACTNO")); 
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("insurerNo")));
			
			Predicate n1 = cb.equal(rd.get("endorsementNo"),args[0]);
			Predicate n2 = cb.equal(rd.get("proposalNo"), args[1]); 
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, fm.formatBigDecimal(args[2]));
			query1.where(n1,n2,n3).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> facSelectInsDetails(String[] args) {
		List<Tuple> list = new ArrayList<>();
		try {
			//fac.select.viewInsDetails
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnInsurerDetails> rd = query1.from(TtrnInsurerDetails.class);
			query1.multiselect(rd.get("retroPercentage").alias("RETRO_PER"),rd.get("type").alias("TYPE"),
					rd.get("uwYear").alias("UW_YEAR"),rd.get("retroType").alias("RETRO_TYPE"),
					cb.selectCase().when(cb.equal(rd.get("contractNo"),"0"), "").otherwise(rd.get("contractNo")).alias("CONTRACTNO")); 
			//end
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnInsurerDetails> coms = end.from(TtrnInsurerDetails.class);
			end.select(cb.max(coms.get("endorsementNo")));
			Predicate b1 = cb.equal( coms.get("proposalNo"), rd.get("proposalNo"));
			Predicate b2 = cb.equal( coms.get("insurerNo"), rd.get("insurerNo"));
			end.where(b1,b2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("insurerNo")));
			
			Predicate n1 = cb.equal(rd.get("endorsementNo"), end);
			Predicate n2 = cb.equal(rd.get("proposalNo"), args[0]); 
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, fm.formatBigDecimal(args[1]));
			query1.where(n1,n2,n3).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> facSelectRetrocontdet23(String productId, String uwYear, String incepDate, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try {
			//FAC_SELECT_RETROCONTDET_23
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");	
			 
			  query.multiselect(
					 cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
					.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET1")); 
			
			  //amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> p = amend.from(PositionMaster.class);
				amend.select(cb.max(p.get("amendId")));
				Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
				Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
				Predicate a3 = cb.notEqual(cb.coalesce(pm.get("retroType"),"N"),"SR");
				Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
				Predicate a5 = cb.equal(pm.get("uwYear"),uwYear);
				Date incep = sdf.parse(incepDate);
				Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate a7 = cb.equal(pm.get("rskDummyContract"),"N");
				amend.where(a1,a2,a3,a4,a5,a6,a7);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("contractNo")));
				
				Predicate n1 = cb.equal(pm.get("productId"), productId);
				Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n3 = cb.isNotNull(pm.get("contractNo"));
				Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
				Predicate n5 = cb.notEqual(cb.coalesce(pm.get("retroType"),"N"),"SR");
				Predicate n9 = cb.equal(pm.get("uwYear"),uwYear);

				Predicate n6 = cb.greaterThan(p.get("expiryDate"),incep);
				Predicate n7 = cb.equal(pm.get("branchCode"), branchCode);
				Predicate n10 = cb.equal(pm.get("rskDummyContract"),"N");
				Predicate n8 = cb.equal(pm.get("amendId"),amend);
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> riskSelectGetRskProIdByProNo(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getRskProIdByProNo
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> rd = query1.from(TtrnRiskDetails.class);
			query1.multiselect(rd.get("rskProductid").alias("RSK_PRODUCTID")); 
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);

			TypedQuery<Tuple> res = em.createQuery(query1);
			list = res.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;	
	}

	@Override
	public TtrnRiskDetails ttrnRiskDetailsUpdate(String[] args) {
		TtrnRiskDetails ttrnRiskDetails = null;
		//risk.update.rskDtls
		//args--getFirstPageEditSaveModeAruguments
		try {
		if(args != null) {
			ttrnRiskDetails = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNoAndRskLayerNo(
					args[52],fm.formatBigDecimal(args[53]),fm.formatBigDecimal(args[54]));
			if(ttrnRiskDetails!=null) {
			ttrnRiskDetails.setSysDate(new Date());
			ttrnRiskDetails.setRskDeptid(fm.formatBigDecimal(args[0]));
			ttrnRiskDetails.setRskPfcid(args[1]);
			ttrnRiskDetails.setRskSpfcid(args[2]);
			ttrnRiskDetails.setRskPolbranch(fm.formatBigDecimal(args[3]));
			ttrnRiskDetails.setRskCedingid(fm.formatBigDecimal(args[4]));
			ttrnRiskDetails.setRskBrokerid(fm.formatBigDecimal(args[5]));
			ttrnRiskDetails.setRskTreatyid(args[6]);
			ttrnRiskDetails.setRskMonth(StringUtils.isBlank(args[7])?null:sdf.parse(args[7]));
			ttrnRiskDetails.setRskUwyear(fm.formatBigDecimal(args[8]));
			ttrnRiskDetails.setRskUnderwritter(args[9]);
			ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[10]));
			ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[11]));
			ttrnRiskDetails.setRskAccountDate(StringUtils.isBlank(args[12])?null:sdf.parse(args[12]));
			ttrnRiskDetails.setRskOriginalCurr(args[13]);
			ttrnRiskDetails.setRskExchangeRate(fm.formatBigDecimal(args[14]));
			ttrnRiskDetails.setRskBasis(args[15]);
			ttrnRiskDetails.setRskPeriodOfNotice(args[16]);
			ttrnRiskDetails.setRskRiskCovered(args[17]);
			ttrnRiskDetails.setRskTerritoryScope(args[18]);
			ttrnRiskDetails.setRskTerritory(args[19]);
			ttrnRiskDetails.setRskStatus(args[20]);
			ttrnRiskDetails.setRskProposalType(args[21]);
			ttrnRiskDetails.setRskAccountingPeriod(fm.formatBigDecimal(args[22]));
			ttrnRiskDetails.setRskReceiptStatement(fm.formatBigDecimal(args[23]));
			ttrnRiskDetails.setRskReceiptPayement(fm.formatBigDecimal(args[24]));
			ttrnRiskDetails.setMndInstallments(fm.formatBigDecimal(args[25]));
			ttrnRiskDetails.setRetroCessionaries(fm.formatBigDecimal(args[26]));
			ttrnRiskDetails.setRskRetroType(args[27]);
			ttrnRiskDetails.setRskInsuredName(args[28]);
			ttrnRiskDetails.setInwardBusType(args[29]);
			ttrnRiskDetails.setTreatytype(args[30]);
			ttrnRiskDetails.setRskBusinessType(args[31]);
			ttrnRiskDetails.setRskExchangeType(args[32]);
			ttrnRiskDetails.setRskPerilsCovered(args[33]);
			ttrnRiskDetails.setRskLocIssued(args[34]);
			ttrnRiskDetails.setRskUmbrellaXl(args[35]);
			ttrnRiskDetails.setLoginId(args[36]);
			ttrnRiskDetails.setBranchCode(args[37]);
			ttrnRiskDetails.setCountriesInclude(args[38]);
			ttrnRiskDetails.setCountriesExclude(args[39]);
			ttrnRiskDetails.setRskNoOfLine(args[40]);
			ttrnRiskDetails.setRsEndorsementType(args[41]);
			ttrnRiskDetails.setRskRunOffYear(fm.formatBigDecimal(args[42]));
			ttrnRiskDetails.setRskLocBnkName(args[43]);
			ttrnRiskDetails.setRskLocCrdtPrd(fm.formatBigDecimal(args[44]));
			ttrnRiskDetails.setRskLocCrdtAmt(fm.formatBigDecimal(args[45]));
			ttrnRiskDetails.setRskLocBenfcreName(args[46]);
			ttrnRiskDetails.setRskCessionExgRate(args[47]);
			if(StringUtils.isNotEmpty(args[48]))
			ttrnRiskDetails.setRskFixedRate(fm.formatBigDecimal(args[48]));
			ttrnRiskDetails.setRetentionyn(args[49]);
			ttrnRiskDetails.setRskAccountPeriodNotice(args[50]);	
			ttrnRiskDetails.setRskStatementConfirm(args[51]);
			
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnRiskDetails;
	}

	@Override
	public TtrnRiskDetails ttrnRiskDetailsInsert(String[] args) {
		TtrnRiskDetails ttrnRiskDetails = null;
		//risk.insert.isAmendIDProTreaty
		//args--getFirstPageInsertAruguments
		try {
			if(args != null) {
				ttrnRiskDetails = new TtrnRiskDetails();
				ttrnRiskDetails.setRskProposalNumber(args[0]);
				ttrnRiskDetails.setRskEndorsementNo(fm.formatBigDecimal(args[1]));
				ttrnRiskDetails.setRskLayerNo(fm.formatBigDecimal(args[2]));
				ttrnRiskDetails.setRskProductid(fm.formatBigDecimal(args[3]));
				ttrnRiskDetails.setRskDeptid(fm.formatBigDecimal(args[4]));
				ttrnRiskDetails.setRskPfcid(args[5]);
				ttrnRiskDetails.setRskSpfcid(args[6]);
				ttrnRiskDetails.setRskPolbranch(fm.formatBigDecimal(args[7]));
				ttrnRiskDetails.setRskCedingid(fm.formatBigDecimal(args[8]));
				ttrnRiskDetails.setRskBrokerid(fm.formatBigDecimal(args[9]));
				ttrnRiskDetails.setRskTreatyid(args[10]);
				ttrnRiskDetails.setRskMonth(StringUtils.isBlank(args[11])?null:sdf.parse(args[11]));
				ttrnRiskDetails.setRskUwyear(fm.formatBigDecimal(args[12]));
				ttrnRiskDetails.setRskUnderwritter(args[13]);
				ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[14]));
				ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[15]));
				ttrnRiskDetails.setRskAccountDate(StringUtils.isBlank(args[16])?null:sdf.parse(args[16]));
				ttrnRiskDetails.setRskOriginalCurr(args[17]);
				ttrnRiskDetails.setRskExchangeRate(fm.formatBigDecimal(args[18]));
				ttrnRiskDetails.setRskBasis(args[19]);
				ttrnRiskDetails.setRskPeriodOfNotice(args[20]);
				ttrnRiskDetails.setRskRiskCovered(args[21]);
				ttrnRiskDetails.setRskTerritoryScope(args[22]);
				ttrnRiskDetails.setRskTerritory(args[23]);
				ttrnRiskDetails.setRskEntryDate(new Date());
				ttrnRiskDetails.setRskEndDate(new Date());
				ttrnRiskDetails.setRskStatus(args[24]);
				ttrnRiskDetails.setRskRemarks(args[25]);
				ttrnRiskDetails.setRskContractNo(args[26]);
				ttrnRiskDetails.setRskProposalType(args[27]);
				ttrnRiskDetails.setRskAccountingPeriod(fm.formatBigDecimal(args[28]));
				ttrnRiskDetails.setRskReceiptStatement(fm.formatBigDecimal(args[29]));
				ttrnRiskDetails.setRskReceiptPayement(fm.formatBigDecimal(args[30]));
				ttrnRiskDetails.setMndInstallments(fm.formatBigDecimal(args[31]));
				ttrnRiskDetails.setRetroCessionaries(fm.formatBigDecimal(args[32]));
				ttrnRiskDetails.setRskRetroType(args[33]);
				ttrnRiskDetails.setRskInsuredName(args[34]);
				ttrnRiskDetails.setOldContractno(args[35]);
				ttrnRiskDetails.setInwardBusType(args[36]);
				ttrnRiskDetails.setTreatytype(args[37]);
				ttrnRiskDetails.setRskBusinessType(args[38]);
				ttrnRiskDetails.setRskExchangeType(args[39]);
				ttrnRiskDetails.setRskPerilsCovered(args[40]);
				ttrnRiskDetails.setRskLocIssued(args[41]);
				ttrnRiskDetails.setRskUmbrellaXl(args[42]);
				ttrnRiskDetails.setLoginId(args[43]);
				ttrnRiskDetails.setBranchCode(args[44]);
				ttrnRiskDetails.setSysDate(new Date());
				ttrnRiskDetails.setCountriesInclude(args[45]);
				ttrnRiskDetails.setCountriesExclude(args[46]);
				ttrnRiskDetails.setRskNoOfLine(args[47]);
				ttrnRiskDetails.setRsEndorsementType(args[48]);
				ttrnRiskDetails.setRskRunOffYear(fm.formatBigDecimal(args[49]));
				ttrnRiskDetails.setRskLocBnkName(args[50]);
				ttrnRiskDetails.setRskLocCrdtPrd(fm.formatBigDecimal(args[51]));
				ttrnRiskDetails.setRskLocCrdtAmt(fm.formatBigDecimal(args[52]));
				ttrnRiskDetails.setRskLocBenfcreName(args[53]);
				ttrnRiskDetails.setRskCessionExgRate(args[54]);
				if(StringUtils.isNotEmpty(args[55]))
				ttrnRiskDetails.setRskFixedRate(fm.formatBigDecimal(args[55]));
				ttrnRiskDetails.setRetentionyn(args[56]);
				ttrnRiskDetails.setRskAccountPeriodNotice(args[57]);
				ttrnRiskDetails.setRskStatementConfirm(args[58]);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRiskDetails;
	}

	@Override
	public List<Tuple> riskSelectGetRetroCessDtls(String[] selectArgs) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getRetroCessDtls
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRetroCessionary> rd = query1.from(TtrnRetroCessionary.class);
			query1.multiselect(rd.get("sno").alias("SNO"),
					rd.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					rd.get("brokerId").alias("BROKER_ID"),
					rd.get("shareAccepted").alias("SHARE_ACCEPTED"),
					rd.get("shareSigned").alias("SHARE_SIGNED"),
					rd.get("comission").alias("COMISSION"),
					rd.get("proposalStatus").alias("PROPOSAL_STATUS")
					); 
			//amendId
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnRetroCessionary> p = amend.from(TtrnRetroCessionary.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), rd.get("proposalNo"));
			Predicate d2 = cb.equal(p.get("sno"), rd.get("sno"));
			amend.where(d1,d2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("sno")));
			
			Predicate n1 = cb.equal(rd.get("amendId"),amend);
			Predicate n2 = cb.equal(rd.get("proposalNo"), selectArgs[0]); 
			Predicate n3 = cb.greaterThan(rd.get("sno"), "0");
			Predicate n4 = cb.lessThan(rd.get("sno"), selectArgs[1]);
			query1.where(n1,n2,n3,n4).orderBy(orderList);

			TypedQuery<Tuple> res = em.createQuery(query1);
			list = res.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> riskSelectViewRetroCessDtls(String[] selectArgs) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getRetroCessDtls
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRetroCessionary> rd = query1.from(TtrnRetroCessionary.class);
			query1.multiselect(rd.get("sno").alias("SNO"),
					rd.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					rd.get("brokerId").alias("BROKER_ID"),
					rd.get("shareAccepted").alias("SHARE_ACCEPTED"),
					rd.get("shareSigned").alias("SHARE_SIGNED"),
					rd.get("comission").alias("COMISSION"),
					rd.get("proposalStatus").alias("PROPOSAL_STATUS")
					); 
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("sno")));
			
			Predicate n1 = cb.equal(rd.get("amendId"),selectArgs[0]);
			Predicate n2 = cb.equal(rd.get("proposalNo"), selectArgs[1]); 
			Predicate n3 = cb.greaterThan(rd.get("sno"), "0");
			Predicate n4 = cb.lessThan(rd.get("sno"), selectArgs[2]);
			query1.where(n1,n2,n3,n4).orderBy(orderList);

			TypedQuery<Tuple> res = em.createQuery(query1);
			list = res.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
		}
	@Override
	public int riskUpdatePro35FirPageRskPro(String[] args) {
		TtrnRiskProposal ttrnRiskProposal =null;
		int count = 0;
		//risk.update.pro35FirPageRskPro
		try {
			if(args != null) {
				ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(args[34],fm.formatBigDecimal(args[35]));
				if(ttrnRiskProposal != null) {
					ttrnRiskProposal.setRskLimitOc(fm.formatBigDecimal(args[0]));	
					ttrnRiskProposal.setRskLimitDc(fm.formatBigDecimal(args[1]));	
					ttrnRiskProposal.setRskEpiEstOc(fm.formatBigDecimal(args[2]));		
					ttrnRiskProposal.setRskEpiEstDc(fm.formatBigDecimal(args[3]));	
					ttrnRiskProposal.setRskShareWritten(fm.formatBigDecimal(args[4]));
					ttrnRiskProposal.setRskShareSigned(fm.formatBigDecimal(args[5]));
					ttrnRiskProposal.setRskMaxLmtCover(fm.formatBigDecimal(args[6]));
					ttrnRiskProposal.setRskSubjPremiumOc(fm.formatBigDecimal(args[7]));
					ttrnRiskProposal.setRskSubjPremiumDc(fm.formatBigDecimal(args[8]));
					ttrnRiskProposal.setRskXlpremOc(fm.formatBigDecimal(args[9]));	
					ttrnRiskProposal.setRskXlpremDc(fm.formatBigDecimal(args[10]));	
					ttrnRiskProposal.setRskPfCovered(args[11]);
					ttrnRiskProposal.setRskDeducOc(fm.formatBigDecimal(args[12]));	
					ttrnRiskProposal.setRskDeducDc(fm.formatBigDecimal(args[13]));		
					ttrnRiskProposal.setRskMdPremOc(fm.formatBigDecimal(args[14]));			
					ttrnRiskProposal.setRskMdPremDc(fm.formatBigDecimal(args[15]));	
					ttrnRiskProposal.setRskAdjrate(fm.formatBigDecimal(args[16]));
					ttrnRiskProposal.setRskSpRetro(args[17]);
					ttrnRiskProposal.setRskNoOfInsurers(fm.formatBigDecimal(args[18]));
					ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(args[19]));		
					ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[20]));		
					ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(args[21]));
					ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[22]));
					ttrnRiskProposal.setRskMdPremOsOc(fm.formatBigDecimal(args[23]));	
					ttrnRiskProposal.setRskMdPremOsDc(fm.formatBigDecimal(args[24]));	
					ttrnRiskProposal.setLimitPerVesselOc(fm.formatBigDecimal(args[25]));
					ttrnRiskProposal.setLimitPerVesselDc(fm.formatBigDecimal(args[26]));
					ttrnRiskProposal.setLimitPerLocationOc(fm.formatBigDecimal(args[27]));	
					ttrnRiskProposal.setLimitPerLocationDc(fm.formatBigDecimal(args[28]));		
					ttrnRiskProposal.setEgpniAsOffer(fm.formatBigDecimal(args[29]));	
					ttrnRiskProposal.setOurassessment(fm.formatBigDecimal(args[30]));	
					ttrnRiskProposal.setEgpniAsOfferDc(fm.formatBigDecimal(args[31]));
					ttrnRiskProposal.setLoginId(args[32]);
					ttrnRiskProposal.setBranchCode(args[33]);
					ttrnRiskProposal.setSysDate(new Date());	
					ttrnRiskProposal.setRskMinimumpremiumPercent(new BigDecimal(fm.formatterfourNoComma(args[36])));
					ttrnRiskProposal.setRskGnpiCapPercent(new BigDecimal(fm.formatterfourNoComma(args[37])));	
					
				ttrnRiskProposalRepository.saveAndFlush(ttrnRiskProposal);
				count = 1;
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return count;
	}
	@Override
	public PositionMaster positionMasterUpdate(String[] input) {
		PositionMaster positionMaster = null;
		//risk.update.positionMaster
		//args--updateHomePositionMasterAruguments
		try {
		if(input != null) {
			positionMaster = positionMasterRepository.findByProposalNoAndAmendId(fm.formatBigDecimal(input[21]),fm.formatBigDecimal(input[22]));
			if(positionMaster!=null) {
				positionMaster.setLayerNo(fm.formatBigDecimal(input[0]));
				positionMaster.setReinsuranceId(input[1]);
				positionMaster.setProductId(fm.formatBigDecimal(input[2]));			
				positionMaster.setDeptId(input[3]);
				positionMaster.setCedingCompanyId(input[4]);
				positionMaster.setUwYear(input[5]);			
				positionMaster.setUwMonth(StringUtils.isBlank(input[6])? null : sdf.parse(input[6]));				
				positionMaster.setAccountDate(StringUtils.isBlank(input[7])? null : sdf.parse(input[7]));
				positionMaster.setInceptionDate(sdf.parse(input[8]));				
				positionMaster.setExpiryDate(sdf.parse(input[9]));
				positionMaster.setProposalStatus(input[10]);
				positionMaster.setEntryDate(new Date());
				positionMaster.setContractStatus(input[11]);
				positionMaster.setBrokerId(input[12]);
				positionMaster.setRetroType(input[13]);
				positionMaster.setUpdateLoginId(input[14]);
				positionMaster.setRskDummyContract(input[15]);
				positionMaster.setDataMapContNo(input[16]);
				positionMaster.setBouquetModeYn(input[17]); //Ri
				positionMaster.setBouquetNo(StringUtils.isBlank(input[18])? null : fm.formatBigDecimal(input[18]));
				positionMaster.setUwYearTo(fm.formatBigDecimal(input[19]));
				positionMaster.setSectionNo(fm.formatBigDecimal(input[20]));	
				positionMaster.setNewLayerNo(input[23]);				
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return positionMaster;
	}
	@Override
	@Transactional
	public void deleteClassLimit(String proposalno) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnRskClassLimits> delete = cb.createCriteriaDelete(TtrnRskClassLimits.class);
			
			Root<TtrnRskClassLimits> rp = delete.from(TtrnRskClassLimits.class);
			
			Subquery<Long> endNo = delete.subquery(Long.class); 
			Root<TtrnRskClassLimits> coms = endNo.from(TtrnRskClassLimits.class);
			endNo.select(cb.max(coms.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( coms.get("rskProposalNumber"), rp.get("rskProposalNumber"));
			endNo.where(a1);
			
			Predicate n1 = cb.equal(rp.get("rskProposalNumber"), proposalno);
			Predicate n2 = cb.equal(rp.get("rskEndorsementNo"), endNo);
			delete.where(n1,n2);
			em.createQuery(delete).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TtrnRskClassLimits insertClassLimit(String[] args) {
		TtrnRskClassLimits ttrnRskClassLimits = null;
		//INSERT_CLASS_LIMIT
		try {
			if(args != null) {
				ttrnRskClassLimits = new TtrnRskClassLimits();
				ttrnRskClassLimits.setRskProposalNumber(fm.formatBigDecimal(args[0]));
				ttrnRskClassLimits.setRskEndorsementNo(fm.formatBigDecimal(args[1]));
				ttrnRskClassLimits.setRskContractNo(fm.formatBigDecimal(args[2]));
				ttrnRskClassLimits.setRskLayerNo(fm.formatBigDecimal(args[3]));
				ttrnRskClassLimits.setRskProductid(fm.formatBigDecimal(args[4]));
				ttrnRskClassLimits.setRskCoverClass(args[5]);
				ttrnRskClassLimits.setRskCoverLimt(fm.formatBigDecimal(args[6]));
				ttrnRskClassLimits.setRskCoverLimtPercentage(fm.formatBigDecimal(args[7]));
				ttrnRskClassLimits.setRskDeductableLimt(fm.formatBigDecimal(args[8]));
				ttrnRskClassLimits.setRskDeductablePercentage(fm.formatBigDecimal(args[9]));
				ttrnRskClassLimits.setEntryDate(new Date());
				ttrnRskClassLimits.setBranchCode(args[10]);
				ttrnRskClassLimits.setStatus("Y");
				ttrnRskClassLimits.setRskSno(fm.formatBigDecimal(args[11]));
				ttrnRskClassLimits.setRskEgnpiAsOff(fm.formatBigDecimal(args[12]));
				ttrnRskClassLimits.setRskGnpiAsOff(args[13]);		
				ttrnRskClassLimits.setRskNetMaxRetentPercent(fm.formatBigDecimal(args[14]));
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRskClassLimits;
	}

	@Override
	public TtrnRiskProposal updateFirstPageFields(String[] args) {
		TtrnRiskProposal ttrnRiskProposal = null;
		//UPDATE_RISK_PROPOSAL_DETAILS
		try {
		if(args != null) {
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[53],fm.formatBigDecimal(args[54]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskEventLimitOc(StringUtils.isBlank(args[0])? null : fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskEventLimitDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEventLimitOsOc(fm.formatBigDecimal(args[2]));
				ttrnRiskProposal.setRskEventLimitOsDc(fm.formatBigDecimal(args[3]));
				ttrnRiskProposal.setRskCoverLimitUxlOc(StringUtils.isBlank(args[4])? null:fm.formatBigDecimal(args[4]));
				ttrnRiskProposal.setRskCoverLimitUxlDc(fm.formatBigDecimal(args[5]));
				ttrnRiskProposal.setRskCoverLimitUxlOsOc(fm.formatBigDecimal(args[6]));
				ttrnRiskProposal.setRskCoverLimitUxlOsDc(fm.formatBigDecimal(args[7]));
				ttrnRiskProposal.setRskDeductableUxlOc(StringUtils.isBlank(args[8])? null:fm.formatBigDecimal(args[8]));
				ttrnRiskProposal.setRskDeductableUxlDc(fm.formatBigDecimal(args[9]));
				ttrnRiskProposal.setRskDeductableUxlOsOc(fm.formatBigDecimal(args[10]));
				ttrnRiskProposal.setRskDeductableUxlOsDc(fm.formatBigDecimal(args[11]));
				ttrnRiskProposal.setRskPml(args[12]);
				if(args[13]!="")
				ttrnRiskProposal.setRskPmlPercent(fm.formatBigDecimal(args[13]));
				if(args[14]!="")
				ttrnRiskProposal.setRskEgnpiPmlOc(fm.formatBigDecimal(args[14]));
				if(args[15]!="")
				ttrnRiskProposal.setRskEgnpiPmlDc(fm.formatBigDecimal(args[15]));
				if(args[16]!="")
				ttrnRiskProposal.setRskEgnpiPmlOsOc(fm.formatBigDecimal(args[16]));
				ttrnRiskProposal.setRskEgnpiPmlOsDc(fm.formatBigDecimal(args[17]));
				ttrnRiskProposal.setRskPremiumBasis(args[18]);
				ttrnRiskProposal.setRskMinimumRate(fm.formatBigDecimal(args[19]));
				ttrnRiskProposal.setRskMaxiimumRate(fm.formatBigDecimal(args[20]));	
				ttrnRiskProposal.setRskBurningCostLf(args[21]);
				ttrnRiskProposal.setRskMinimumPremiumOc(StringUtils.isBlank(args[22])? null:fm.formatBigDecimal(args[22]));
				ttrnRiskProposal.setRskMinimumPremiumDc(fm.formatBigDecimal(args[23]));
				ttrnRiskProposal.setRskMinimumPremiumOsOc(fm.formatBigDecimal(args[24]));
				ttrnRiskProposal.setRskMinimumPremiumOsDc(fm.formatBigDecimal(args[25]));
				ttrnRiskProposal.setRskPaymentDueDays(args[26]);
				ttrnRiskProposal.setRskTrtyLmtPmlOc(args[27]);
				ttrnRiskProposal.setRskTrtyLmtPmlDc(args[28]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsOc(args[29]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsDc(args[30]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOc(args[31]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlDc(args[32]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsOc(args[33]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsDc(args[34]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOc(args[35]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlDc(args[36]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOsOc(args[37]);
				ttrnRiskProposal.setRskTrtyLmtourAssPmlOsDc(args[38]);
				ttrnRiskProposal.setRiskDetYn(args[39]);;
				ttrnRiskProposal.setBrokerDetYn(args[40]);
				ttrnRiskProposal.setCoverDetYn(args[41]);
				ttrnRiskProposal.setPremiumDetYn(args[42]);
				ttrnRiskProposal.setAcqcostDetYn(args[43]);
				ttrnRiskProposal.setCommDetYn(args[44]);
				ttrnRiskProposal.setDepositDetYn(args[45]);
				ttrnRiskProposal.setLossDetYn(args[46]);
				ttrnRiskProposal.setDocDetYn(args[47]);
				ttrnRiskProposal.setPaymentPartner(args[48]);
				ttrnRiskProposal.setIntallDetYn(args[49]);
				ttrnRiskProposal.setReinstDetYn(args[50]);
				ttrnRiskProposal.setRateOnLine(StringUtils.isBlank(args[51])? null:fm.formatBigDecimal(args[51]));
				ttrnRiskProposal.setQuotesharePercent(fm.formatBigDecimal(args[52]));
				ttrnRiskProposal.setRskMinimumpremiumPercent(new BigDecimal(fm.formatterfourNoComma(args[55])));
				ttrnRiskProposal.setRskGnpiCapPercent(new BigDecimal(fm.formatterfourNoComma(args[56])));	
				
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}	
	@Override
	public String riskSelectGetRenewalStatus(String proposalNo) {
		String result="";
		try{
				//risk.select.getRenewalStatus
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				query.multiselect(cb.selectCase().when(cb.isNull(pm.get("renewalStatus")), "0")
						.otherwise(pm.get("renewalStatus"))); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2);
				
				TypedQuery<String> res1 = em.createQuery(query);
				List<String> list = res1.getResultList();
				 
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0)==null?"":list.get(0);
				}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}

	@Override
	public List<Tuple> riskSelectGetLayerDupcheckByBaseLayer(String layerNo, String baseLayer) {
		List<Tuple> list =new ArrayList<>();
		try{
				//risk.select.getLayerDupcheckByBaseLayer
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
			
				query.multiselect(pm.get("newLayerNo").alias("NEW_LAYER_NO")); 

				Predicate n1 = cb.equal(pm.get("newLayerNo"), layerNo);
				Predicate n2 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), baseLayer);
				Predicate n3 = cb.equal(pm.get("contractStatus"), "P");
				query.where(n1,n2,n3);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
		}catch(Exception e){
			e.printStackTrace();
			}
		return list;
	}

	@Override
	public List<Tuple> riskSelectGetLayerDupcheckByProNo(String layerNo, String proposalNo, String proposalOrBase) {
		List<Tuple> list =new ArrayList<>();
		try{
				//risk.select.getLayerDupcheckByProNo
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
			
				query.multiselect(pm.get("newLayerNo").alias("NEW_LAYER_NO")); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				Predicate n1 = cb.equal(pm.get("newLayerNo"), layerNo);
				Predicate n2 = cb.notEqual(pm.get("proposalNo"), proposalNo);
				Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), proposalOrBase);
				Predicate n4 = cb.equal(pm.get("contractStatus"), "P");
				Predicate n5 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2,n3,n4,n5);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
		}catch(Exception e){
			e.printStackTrace();
			}
		return list;
	}	
	@Override
	public List<Map<String, Object>>  facSelectGetRenewalValidation(String incepDate, String renewalContractNo) {
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		Map<String,Object>  values = new HashMap<String,Object>();
		try{ 
			//fac.select.getRenewalValidation
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dbf = new SimpleDateFormat("yyyy-MM-dd"); //DB format
			
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);

			query.multiselect(pm.get("uwYear").alias("uwYear"),pm.get("expiryDate").alias("expiryDate")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("renewalStatus"), pms.get("renewalStatus"));
			amend.where(a1,a2);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(pm.get("uwYear")));

			Predicate n1 = cb.equal(pm.get("contractNo"), renewalContractNo);
			Predicate n2 = cb.equal(pm.get("renewalStatus"), "0");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3).orderBy(orderList);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();

			for(Tuple data :  list ) {
			values.put("UW_YEAR" , data.get("uwYear"));
			values.put("EXPIRY_DATE" , dbf.format(data.get("expiryDate")));
			LocalDate d1 = LocalDate.parse(dbf.format( sdf.parse( incepDate)), DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate d2 = LocalDate.parse(  dbf.format(data.get("expiryDate")), DateTimeFormatter.ISO_LOCAL_DATE);
			Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
			long diffDays = diff.toDays();	
			values.put("DIFF" , diffDays);
			response.add(values);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return response;	
	}

	@Override
	public List<Tuple> riskSelectGetSecondViewData(String proposalNo, String amendId) {
		List<Tuple>  list = new ArrayList<>();
		try {
			//risk.select.getSecondViewData
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> pm = query.from(TtrnRiskProposal.class);
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate a1 = cb.equal(rds.get("categoryId"), "31");
			Predicate a2 = cb.equal(rds.get("categoryDetailId"), pm.get("rskPremiumBasis"));
			detailName.where(a1, a2);
			
			query.multiselect(pm.get("rskMdInstallmentNos").alias("RSK_MD_INSTALLMENT_NOS"),
					cb.selectCase().when(cb.equal(pm.get("rskCedretType"),"P"),"Percentage").otherwise("Amount").alias("CEDRET_TYPE"),
					pm.get("rskLimitOc").alias("RSK_LIMIT_OC"),pm.get("rskLimitDc").alias("RSK_LIMIT_DC"),
					pm.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pm.get("rskEpiOfferDc").alias("RSK_EPI_OFFER_DC"),
					pm.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),pm.get("rskXlpremOc").alias("RSK_XLPREM_OC"),
					pm.get("rskXlpremDc").alias("RSK_XLPREM_DC"),pm.get("rskDeducOc").alias("RSK_DEDUC_OC"),
					pm.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pm.get("rskEpiEstDc").alias("RSK_EPI_EST_DC"),
					pm.get("rskXlcostOc").alias("RSK_XLCOST_OC"),pm.get("rskXlcostDc").alias("RSK_XLCOST_DC"),
					pm.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pm.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
					pm.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),pm.get("rskMdPremOc").alias("RSK_MD_PREM_OC"),
					pm.get("rskMdPremDc").alias("RSK_MD_PREM_DC"),pm.get("rskAdjrate").alias("RSK_ADJRATE"),
					pm.get("rskPfCovered").alias("RSK_PF_COVERED"),pm.get("rskSubjPremiumOc").alias("RSK_SUBJ_PREMIUM_OC"),
					pm.get("rskSubjPremiumDc").alias("RSK_SUBJ_PREMIUM_DC"),pm.get("rskDeducDc").alias("RSK_DEDUC_DC"),
					pm.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),pm.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
					pm.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),pm.get("rskEpiOsofDc").alias("RSK_EPI_OSOF_DC"),
					pm.get("rskEpiOsoeOc").alias("RSK_EPI_OSOE_OC"),pm.get("rskEpiOsoeDc").alias("RSK_EPI_OSOE_DC"),
					pm.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC"),pm.get("rskXlcostOsDc").alias("RSK_XLCOST_OS_DC"),
					pm.get("rskMdPremOsOc").alias("RSK_MD_PREM_OS_OC"),pm.get("rskMdPremOsDc").alias("RSK_MD_PREM_OS_DC"),
					cb.selectCase().when(cb.equal(pm.get("rskSpRetro"),"Y"),"Yes").otherwise("No").alias("RSK_SP_RETRO"),
					pm.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pm.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
					pm.get("egpniAsOffer").alias("EGPNI_AS_OFFER"),pm.get("egpniAsOfferDc").alias("EGPNI_AS_OFFER_DC"),
					pm.get("ourassessment").alias("OURASSESSMENT"),pm.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),
					pm.get("rskTreatySurpLimitOsOc").alias("RSK_TREATY_SURP_LIMIT_OS_OC"),pm.get("rskTreatySurpLimitOsDc").alias("RSK_TREATY_SURP_LIMIT_OS_DC"),
					pm.get("rskEventLimitOc").alias("RSK_EVENT_LIMIT_OC"),pm.get("rskEventLimitDc").alias("RSK_EVENT_LIMIT_DC"),
					pm.get("rskEventLimitOsOc").alias("RSK_EVENT_LIMIT_OS_OC"),pm.get("rskEventLimitOsDc").alias("RSK_EVENT_LIMIT_OS_DC"),
					pm.get("rskCoverLimitUxlOsDc").alias("RSK_COVER_LIMIT_UXL_OS_DC"),pm.get("rskDeductableUxlOc").alias("RSK_DEDUCTABLE_UXL_OC"),
					pm.get("rskDeductableUxlDc").alias("RSK_DEDUCTABLE_UXL_DC"),pm.get("rskDeductableUxlOsOc").alias("RSK_DEDUCTABLE_UXL_OS_OC"),
					pm.get("rskDeductableUxlOsDc").alias("RSK_DEDUCTABLE_UXL_OS_DC"),
					cb.selectCase().when(cb.equal(pm.get("rskPml"),"Y"),"Yes").otherwise("No").alias("RSK_PML"),
					pm.get("rskPmlPercent").alias("RSK_PML_PERCENT"),	pm.get("rskEgnpiPmlOc").alias("RSK_EGNPI_PML_OC"),
					pm.get("rskEgnpiPmlDc").alias("RSK_EGNPI_PML_DC"),pm.get("rskEgnpiPmlOsOc").alias("RSK_EGNPI_PML_OS_OC"),
					pm.get("rskEgnpiPmlOsDc").alias("RSK_EGNPI_PML_OS_DC"),pm.get("rskPremiumBasis").alias("RSK_PREMIUM_BASIS"),
					pm.get("rskMinimumRate").alias("RSK_MINIMUM_RATE"),pm.get("rskMaxiimumRate").alias("RSK_MAXIIMUM_RATE"),
					pm.get("rskBurningCostLf").alias("RSK_BURNING_COST_LF"),pm.get("rskPaymentDueDays").alias("RSK_PAYMENT_DUE_DAYS"),
					detailName.alias("RSK_PREMIUM_BASIS_Con"),pm.get("rskTrtyLmtPmlOsOc").alias("RSK_TRTY_LMT_PML_OS_OC"),
					pm.get("rskTrtyLmtPmlOsDc").alias("RSK_TRTY_LMT_PML_OS_DC"),pm.get("rskTrtyLmtSurPmlDc").alias("RSK_TRTY_LMT_SUR_PML_DC"),
					pm.get("rskTrtyLmtSurPmlOsOc").alias("RSK_TRTY_LMT_SUR_PML_OS_OC"),pm.get("rskTrtyLmtSurPmlOsDc").alias("RSK_TRTY_LMT_SUR_PML_OS_DC"),
					pm.get("rskMinimumPremiumOc").alias("RSK_MINIMUM_PREMIUM_OC"),pm.get("rskMinimumPremiumDc").alias("RSK_MINIMUM_PREMIUM_DC"),
					pm.get("rskMinimumPremiumOsOc").alias("RSK_MINIMUM_PREMIUM_OS_OC"),pm.get("rskMinimumPremiumOsDc").alias("RSK_MINIMUM_PREMIUM_OS_DC"),
					pm.get("rskCoverLimitUxlOc").alias("RSK_COVER_LIMIT_UXL_OC"),pm.get("rskCoverLimitUxlDc").alias("RSK_COVER_LIMIT_UXL_DC"),
					pm.get("rskCoverLimitUxlOsOc").alias("RSK_COVER_LIMIT_UXL_OS_OC"),pm.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
					pm.get("rskTrtyLmtPmlDc").alias("RSK_TRTY_LMT_PML_DC"),pm.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),
					pm.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"));

			Predicate n1 = cb.equal(pm.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), amendId);
			query.where(n1,n2);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<Tuple> getMappingProposalNo(String contractListVal, String layerNo, String departId, String uwYear) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_MAPPING_PROPOSAL_NO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<PositionMaster> m = query1.from(PositionMaster.class);
			query1.multiselect(m.get("proposalNo").alias("PROPOSAL_NO"),m.get("amendId").alias("AMEND_ID"));
			
			//amend
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("rskEndorsementNo")));
			Predicate b1 = cb.equal(m.get("proposalNo"), pms.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("contractNo"), pms.get("contractNo"));
			Predicate b3 = cb.equal(m.get("layerNo"), pms.get("layerNo"));
			amend.where(b1,b2,b3);

			Predicate m1 = cb.equal(m.get("contractNo"), contractListVal);
			Predicate m2 = cb.equal(m.get("layerNo"), layerNo);
			Predicate m3 = cb.equal(m.get("deptId"), departId);
			Predicate m4 = cb.equal(m.get("uwYear"), uwYear);
			Predicate m5 = cb.equal(m.get("amendId"), amend);
			query1.where(m1,m2,m3,m4,m5);
			
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			list = res2.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> riskSelectGetEditModeDataPro3ContCond1(String contractNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getEditModeDataPro3ContCond1
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			query.multiselect(
					pm.get("newLayerNo").alias("NEW_LAYER_NO"),
					pr.get("rskMinimumpremiumPercent").alias("RSK_MINIMUMPREMIUM_PERCENT"),
					pr.get("rskGnpiCapPercent").alias("RSK_GNPI_CAP_PERCENT"),
					de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					de.get("rskContractNo").alias("RSK_CONTRACT_NO"),de.get("rskLayerNo").alias("RSK_LAYER_NO"),
					de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
					de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
					de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
					de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
					de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
					de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
					de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
					de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
					de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
					de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
					de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
					de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),
					de.get("rskRemarks").alias("RSK_REMARKS"),pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),
					pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),
					pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),
					pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
					pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
					de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),
					de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
					pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
					pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
					de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("rskInsuredName").alias("RSK_INSURED_NAME"),
					de.get("oldContractno").alias("OLD_CONTRACTNO"),pm.get("loginId").alias("LOGIN_ID"),
					pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
					pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
					de.get("inwardBusType").alias("INWARD_BUS_TYPE"),de.get("treatytype").alias("TREATYTYPE"),
					pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
					pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
					pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),
					de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),
					pr.get("rskPml").alias("RSK_PML"),pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),
					de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),de.get("rskLocIssued").alias("RSK_LOC_ISSUED"),
					de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskRunOffYear").alias("RSK_RUN_OFF_YEAR"),
					de.get("rskLocBnkName").alias("RSK_LOC_BNK_NAME"),de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),
					de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),
					pm.get("baseLayer").alias("BASE_LAYER"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
					de.get("rskFixedRate").alias("RSK_FIXED_RATE"),pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),
					de.get("retentionyn").alias("RETENTIONYN"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"), //RI
					pm.get("bouquetNo").alias("BOUQUET_NO"),	pm.get("offerNo").alias("OFFER_NO"),
					pr.get("riskDetYn").alias("RISK_DET_YN"),	pr.get("brokerDetYn").alias("BROKER_DET_YN"), 
					pr.get("coverDetYn").alias("COVER_DET_YN"),	pr.get("premiumDetYn").alias("PREMIUM_DET_YN"), 
					pr.get("acqcostDetYn").alias("ACQCOST_DET_YN"),	pr.get("commDetYn").alias("COMM_DET_YN"), 
					pr.get("depositDetYn").alias("DEPOSIT_DET_YN"),	pr.get("lossDetYn").alias("LOSS_DET_YN"), 
					pr.get("docDetYn").alias("DOC_DET_YN"),	pr.get("paymentPartner").alias("PAYMENT_PARTNER"),
					pm.get("sectionNo").alias("SECTION_NO"),	pr.get("quotesharePercent").alias("QUOTESHARE_PERCENT"), 
					de.get("rskAccountPeriodNotice").alias("RSK_ACCOUNT_PERIOD_NOTICE"),de.get("rskStatementConfirm").alias("RSK_STATEMENT_CONFIRM")
					); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);
			//endDe
			Subquery<Long> endDe = query.subquery(Long.class); 
			Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
			endDe.select(cb.max(des.get("rskEndorsementNo")));
			Predicate b1 = cb.equal( des.get("rskContractNo"),contractNo);
			endDe.where(b1);
			//endPr
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskDetails> a = endPr.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(b.get("rskEndorsementNo")));
			Predicate c1 = cb.equal( a.get("rskContractNo"),contractNo);
			Predicate c2 = cb.equal( a.get("rskProposalNumber"), b.get("rskProposalNumber"));
			endPr.where(c1,c2);

			Predicate n1 = cb.equal(de.get("rskContractNo"), contractNo );
			Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
			Predicate n5 = cb.equal(de.get("rskEndorsementNo"), endDe);
			Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n4 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
			Predicate n6 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n4,n2,n5,n6);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
		
	}catch(Exception e) {
		e.printStackTrace();
	}
		return list;
	
	}

	@Override
	public List<Tuple> riskSelectGetEditModeDataPro3ProCond1(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getEditModeDataPro3ProCond1
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
				Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				
				query.multiselect(
						pr.get("rskMinimumpremiumPercent").alias("RSK_MINIMUMPREMIUM_PERCENT"),
						pr.get("rskGnpiCapPercent").alias("RSK_GNPI_CAP_PERCENT"),
						de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
						de.get("rskContractNo").alias("RSK_CONTRACT_NO"),de.get("rskLayerNo").alias("RSK_LAYER_NO"),
						pm.get("newLayerNo").alias("NEW_LAYER_NO"),
						de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
						de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
						de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
						de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
						de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
						de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
						de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
						de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
						de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
						de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
						de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
						de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),
						de.get("rskRemarks").alias("RSK_REMARKS"),pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),
						pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),
						pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),
						pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
						pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
						de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),
						de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
						pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
						pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
						de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("rskInsuredName").alias("RSK_INSURED_NAME"),
						de.get("oldContractno").alias("OLD_CONTRACTNO"),pm.get("loginId").alias("LOGIN_ID"),
						pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
						pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
						de.get("inwardBusType").alias("INWARD_BUS_TYPE"),de.get("treatytype").alias("TREATYTYPE"),
						pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
						pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
						pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),
						de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),
						pr.get("rskPml").alias("RSK_PML"),pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),
						de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),de.get("rskLocIssued").alias("RSK_LOC_ISSUED"),
						de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskRunOffYear").alias("RSK_RUN_OFF_YEAR"),
						de.get("rskLocBnkName").alias("RSK_LOC_BNK_NAME"),de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),
						de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),
						pm.get("baseLayer").alias("BASE_LAYER"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
						de.get("rskFixedRate").alias("RSK_FIXED_RATE"),pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),
						de.get("retentionyn").alias("RETENTIONYN"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"),
						pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"), //RI
						pm.get("bouquetNo").alias("BOUQUET_NO"),	pm.get("offerNo").alias("OFFER_NO"),
						pr.get("riskDetYn").alias("RISK_DET_YN"),	pr.get("brokerDetYn").alias("BROKER_DET_YN"), 
						pr.get("coverDetYn").alias("COVER_DET_YN"),	pr.get("premiumDetYn").alias("PREMIUM_DET_YN"), 
						pr.get("acqcostDetYn").alias("ACQCOST_DET_YN"),	pr.get("commDetYn").alias("COMM_DET_YN"), 
						pr.get("depositDetYn").alias("DEPOSIT_DET_YN"),	pr.get("lossDetYn").alias("LOSS_DET_YN"), 
						pr.get("docDetYn").alias("DOC_DET_YN"),	pr.get("paymentPartner").alias("PAYMENT_PARTNER"),
						pm.get("sectionNo").alias("SECTION_NO"),	pr.get("quotesharePercent").alias("QUOTESHARE_PERCENT"), 
						de.get("rskAccountPeriodNotice").alias("RSK_ACCOUNT_PERIOD_NOTICE"),de.get("rskStatementConfirm").alias("RSK_STATEMENT_CONFIRM"),
						pr.get("rskSubjPremiumOc").alias("RSK_SUBJ_PREMIUM_OC"),pr.get("rskXlpremOc").alias("RSK_XLPREM_OC"),
						pr.get("rskDeducOc").alias("RSK_DEDUC_OC"),pr.get("rskMdPremOc").alias("RSK_MD_PREM_OC"),pr.get("rskAdjrate").alias("RSK_ADJRATE"),
						pr.get("rskPfCovered").alias("RSK_PF_COVERED"),de.get("mndInstallments").alias("MND_INSTALLMENTS"),pr.get("egpniAsOffer").alias("EGPNI_AS_OFFER"),
						pr.get("egpniAsOfferDc").alias("EGPNI_AS_OFFER_DC"),pr.get("ourassessment").alias("OURASSESSMENT"),
						pr.get("rskTreatySurpLimitOsOc").alias("RSK_TREATY_SURP_LIMIT_OS_OC"),pr.get("rskTreatySurpLimitOsDc").alias("RSK_TREATY_SURP_LIMIT_OS_DC"),
						pr.get("rskEventLimitOc").alias("RSK_EVENT_LIMIT_OC"),pr.get("rskEventLimitDc").alias("RSK_EVENT_LIMIT_DC"),
						pr.get("rskEventLimitOsOc").alias("RSK_EVENT_LIMIT_OS_OC"),pr.get("rskEventLimitOsDc").alias("RSK_EVENT_LIMIT_OS_DC"),
						pr.get("rskCoverLimitUxlOsDc").alias("RSK_COVER_LIMIT_UXL_OS_DC"),pr.get("rskDeductableUxlOc").alias("RSK_DEDUCTABLE_UXL_OC"),
						pr.get("rskDeductableUxlDc").alias("RSK_DEDUCTABLE_UXL_DC"),pr.get("rskDeductableUxlOsOc").alias("RSK_DEDUCTABLE_UXL_OS_OC"),
						pr.get("rskDeductableUxlOsDc").alias("RSK_DEDUCTABLE_UXL_OS_DC"),de.get("rskBusinessType").alias("RSK_BUSINESS_TYPE"),
						de.get("rskExchangeType").alias("RSK_EXCHANGE_TYPE"),de.get("rskUmbrellaXl").alias("RSK_UMBRELLA_XL"),
						pr.get("rskCoverLimitUxlOc").alias("RSK_COVER_LIMIT_UXL_OC"),pr.get("rskEgnpiPmlOc").alias("RSK_EGNPI_PML_OC"),
						pr.get("rskPremiumBasis").alias("RSK_PREMIUM_BASIS"),pr.get("rskMinimumRate").alias("RSK_MINIMUM_RATE"),pr.get("rskMaxiimumRate").alias("RSK_MAXIIMUM_RATE"),
						pr.get("rskBurningCostLf").alias("RSK_BURNING_COST_LF"),pr.get("rskPaymentDueDays").alias("RSK_PAYMENT_DUE_DAYS"),
						pr.get("rskMinimumPremiumOc").alias("RSK_MINIMUM_PREMIUM_OC"),pr.get("rskMinimumPremiumDc").alias("RSK_MINIMUM_PREMIUM_DC"),
						pr.get("rskMinimumPremiumOsOc").alias("RSK_MINIMUM_PREMIUM_OS_OC"),pr.get("rskMinimumPremiumOsDc").alias("RSK_MINIMUM_PREMIUM_OS_DC"),
						pr.get("rskCoverLimitUxlDc").alias("RSK_COVER_LIMIT_UXL_DC"),pr.get("rateOnLine").alias("RATE_ON_LINE"),pr.get("reinstDetYn").alias("REINST_DET_YN"),
						pr.get("rskCoverLimitUxlOsOc").alias("RSK_COVER_LIMIT_UXL_OS_OC"),pr.get("intallDetYn").alias("INTALL_DET_YN"), 
						pr.get("rskTrtyLmtPmlDc").alias("RSK_TRTY_LMT_PML_DC"),pm.get("branchCode").alias("BRANCH_CODE")
						); 
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);
				//endDe
				Subquery<Long> endDe = query.subquery(Long.class); 
				Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
				endDe.select(cb.max(des.get("rskEndorsementNo")));
				Predicate b1 = cb.equal( des.get("rskProposalNumber"),proposalNo);
				endDe.where(b1);
				//endPr
				Subquery<Long> endPr = query.subquery(Long.class); 
				Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
				endPr.select(cb.max(b.get("rskEndorsementNo")));
				Predicate c1 = cb.equal( b.get("rskProposalNumber"),proposalNo);
				endPr.where(c1);

				Predicate n1 = cb.equal(de.get("rskProposalNumber"), proposalNo);
				Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
				Predicate n5 = cb.equal(de.get("rskEndorsementNo"), endDe);
				Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
				Predicate n4 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
				Predicate n6 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n3,n4,n2,n5,n6);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
	}

	@Override
	public String riskSelectCeaseStatus(String proposalNo) {
		String ceaseStatus = "";
		try {
			//risk.select.CEASE_STATUS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query1 = cb.createQuery(String.class); 
			Root<PositionMaster> pm = query1.from(PositionMaster.class);
			
			query1.select(pm.get("ceaseStatus")); 
			
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PositionMaster> rcs = amend.from(PositionMaster.class);
			amend.select(cb.max(rcs.get("amendId")));
			Predicate f1 = cb.equal( rcs.get("proposalNo"),  pm.get("proposalNo"));
			amend.where(f1);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"),proposalNo);
			Predicate n2 = cb.equal(pm.get("amendId"), amend); 
			query1.where(n1,n2);
	
			TypedQuery<String> result = em.createQuery(query1);
			List<String> list = result.getResultList();
			
			if(list!=null) {
				ceaseStatus = list.get(0)==null?"":list.get(0);
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ceaseStatus;
	}	
	@Override
	public List<Tuple> facSelectUwYear(String productid, String incepDate, String branchCode) {
		List<Tuple> list= new ArrayList<>();
		try {
			//fac.select.uwYear
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			query.multiselect(pm.get("uwYear").alias("CONTDET1")).distinct(true); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> p = amend.from(PositionMaster.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
			Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
			Predicate a3 = cb.equal(pm.get("retroType"), p.get("retroType"));
			Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate a5 = cb.equal(pm.get("contractStatus"), p.get("contractStatus"));
			Date incep = sdf.parse(incepDate);
			Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
			amend.where(a1,a2,a3,a4,a5,a6);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("uwYear")));
			
			Predicate n1 = cb.equal(pm.get("productId"),productid);
			Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
			Predicate n3 = cb.isNotNull(pm.get("contractNo"));
			Predicate n4 = cb.notEqual(pm.get("contractNo"), 0);
			Predicate n5 = cb.equal(pm.get("retroType"),"TR");
			Predicate n6 = cb.greaterThan( pm.get("expiryDate"),incep);
			Predicate n7 = cb.equal(pm.get("branchCode"),branchCode);
			Predicate n8 = cb.equal(pm.get("amendId"),amend);	
			query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> riskSelectUwYear(String productid, String incepDate, String branchCode) {
		List<Tuple> list= new ArrayList<>();
		try {
			//risk.select.uwYear
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			query.multiselect(pm.get("uwYear").alias("CONTDET1")).distinct(true); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> p = amend.from(PositionMaster.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
			Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
			Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate a5 = cb.equal(pm.get("contractStatus"), p.get("contractStatus"));
			Date incep = sdf.parse(incepDate);
			Predicate a6 = cb.lessThanOrEqualTo(p.get("expiryDate") ,incep);
			amend.where(a1,a2,a4,a5,a6);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("uwYear")));
			
			Predicate n1 = cb.equal(pm.get("productId"), productid);
			Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
			Predicate n3 = cb.isNotNull(pm.get("contractNo"));
			Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
			Predicate n6 = cb.lessThanOrEqualTo(pm.get("expiryDate") ,incep);
			Predicate n7 = cb.equal(pm.get("branchCode"),branchCode);
			Predicate n8 = cb.equal(pm.get("amendId"),amend);
			query.where(n1,n2,n3,n4,n6,n7,n8).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> facSelectRetroContDetTR(String productid, String UWYear, String incepDate, String branchCode) {
		List<Tuple> list= new ArrayList<>();
		try {
			//fac.select.retroContDetTR
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");	
			  query.multiselect(pm.get("contractNo").alias("CONTRACT_NO"),
					  cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
						.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET1")); 
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> p = amend.from(PositionMaster.class);
				amend.select(cb.max(p.get("amendId")));
				Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
				Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
				
				String s = "TR,FO,TO";
				List<String> retro =new ArrayList<String>(Arrays.asList(s.split(","))) ;
				Expression<String> e1= cb.coalesce(pm.get("retroType"),"N");
				Predicate a3 = e1.in(retro);
				
				Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
				Predicate a5 = cb.equal(pm.get("uwYear"),UWYear);
				Date incep = sdf.parse(incepDate);
				Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate a7 = cb.equal(pm.get("rskDummyContract"),"N");
				amend.where(a1,a2,a3,a4,a5,a6,a7);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("contractNo")));
				
				Predicate n1 = cb.equal(pm.get("productId"), productid);
				Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n3 = cb.isNotNull(pm.get("contractNo"));
				Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
				Expression<String> e2= cb.coalesce(pm.get("retroType"),"N");
				Predicate n5 = e2.in(retro);
				Predicate n9 = cb.equal(pm.get("uwYear"),UWYear);
				Predicate n6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate n7 = cb.equal(pm.get("branchCode"),branchCode);
				Predicate n10 = cb.equal(pm.get("rskDummyContract"),"N");
				Predicate n8 = cb.equal(pm.get("amendId"),amend);
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
				
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> facSelectRetroContDet(String productid, String retroType, String UWYear, String incepDate,String branchCode) {
		List<Tuple> list= new ArrayList<>();
		try {
			 //fac.select.retroContDet
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");	
			  query.multiselect(pm.get("contractNo").alias("CONTRACT_NO"),
					  cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
						.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET1")); 
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> p = amend.from(PositionMaster.class);
				amend.select(cb.max(p.get("amendId")));
				Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
				Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
				Predicate a3 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(p.get("productId"),"4"), StringUtils.isBlank(retroType)?"":retroType).otherwise("N"));
				Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
				Predicate a5 = cb.equal(pm.get("uwYear"),UWYear);
				Date incep = sdf.parse(incepDate);
				Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate a7 = cb.equal(pm.get("rskDummyContract"),"N");
				amend.where(a1,a2,a3,a4,a5,a6,a7);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("contractNo")));
				
				Predicate n1 = cb.equal(pm.get("productId"), productid);
				Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n3 = cb.isNotNull(pm.get("contractNo"));
				Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
				Predicate n5 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(pm.get("productId"),"4"), StringUtils.isBlank(retroType)?"":retroType).otherwise("N"));
				Predicate n9 = cb.equal(pm.get("uwYear"),UWYear);
				Predicate n6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate n7 = cb.equal(pm.get("branchCode"),branchCode);
				Predicate n10 = cb.equal(pm.get("rskDummyContract"),"N");
				Predicate n8 = cb.equal(pm.get("amendId"),amend);
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> facSelectRetroDupContract(String UWYear, String incepDate,	String branchCode) {
		List<Tuple> list= new ArrayList<>();
		try {
			//FAC_SELECT_RETRO_DUP_CONTRACT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");		
			 query.multiselect(pm.get("contractNo").alias("CONTRACT_NO"),
					  cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
						.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET1")); 
			
			  //amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> p = amend.from(PositionMaster.class);
				amend.select(cb.max(p.get("amendId")));
				Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
				Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
				Predicate a3 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(p.get("productId"),"4"), "TR").otherwise("N"));
				Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
				Predicate a5 = cb.equal(pm.get("uwYear"),UWYear);
				Date incep = sdf.parse(incepDate);
				Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate a7 = cb.equal(pm.get("rskDummyContract"),"D");
				amend.where(a1,a2,a3,a4,a5,a6,a7);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("contractNo")));
				
				Predicate n1 = cb.equal(pm.get("productId"), "4");
				Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n3 = cb.isNotNull(pm.get("contractNo"));
				Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
				Predicate n5 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(pm.get("productId"),"4"), "TR").otherwise("N"));
				Predicate n9 = cb.equal(pm.get("uwYear"),UWYear);
				Predicate n6 = cb.greaterThan(p.get("expiryDate") ,incep);
				Predicate n7 = cb.equal(pm.get("branchCode"),branchCode);
				Predicate n10 = cb.equal(pm.get("rskDummyContract"),"D");
				Predicate n8 = cb.equal(pm.get("amendId"),amend);
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}	
	@Override
	public TtrnRiskProposal riskUpdatePro35RskProposal(String[] args) {
		TtrnRiskProposal ttrnRiskProposal =null;
		//risk.update.pro35RskProposal
		try {
			if(args != null) {
				ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskLayerNoAndRskEndorsementNo(args[6],fm.formatBigDecimal(args[7]),fm.formatBigDecimal(args[8]));
				if(ttrnRiskProposal != null) {
					ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(args[0]));		
					ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[1]));		
					ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(args[2]));
					ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[3]));
					ttrnRiskProposal.setRskMdPremOsOc(fm.formatBigDecimal(args[4]));	
					ttrnRiskProposal.setRskMdPremOsDc(fm.formatBigDecimal(args[5]));	
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRiskProposal;
	}	
	@Override
	public String riskSelectMaxRskStatus(String proposalNo) {
		String result="";
		try{
			 //risk.select.maxRskStatus
			 CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);

				query.multiselect(rd.get("rskStatus").alias("RSK_STATUS")); 

				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRiskDetails> rds = end.from(TtrnRiskDetails.class);
				end.select(cb.max(rds.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
				end.where(a1);

				Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
				query.where(n1,n2);

				TypedQuery<String> res = em.createQuery(query);
				List<String> list = res.getResultList();
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0)==null?"":list.get(0);
				}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}

	@Override
	public TtrnRiskCommission riskUpdatePro3SecComm(String[] args) {
		TtrnRiskCommission ttrnRiskCommission =null;
		//risk.update.pro3SecComm
		try {
			if(args != null) {
				ttrnRiskCommission = ttrnRiskCommissionRepository.findByRskProposalNumberAndRskEndorsementNo(args[33],fm.formatBigDecimal(args[34]));
				if(ttrnRiskCommission != null) {
					ttrnRiskCommission.setRskBrokerage(fm.formatBigDecimal(args[0]));
					ttrnRiskCommission.setRskTax(fm.formatBigDecimal(args[1]));
					ttrnRiskCommission.setRskProfitComm(fm.formatBigDecimal(args[2]));
					ttrnRiskCommission.setRskAcquistionCostOc(fm.formatBigDecimal(args[3]));
					ttrnRiskCommission.setRskAcquistionCostDc(fm.formatBigDecimal(args[4]));
					ttrnRiskCommission.setRskAggregateLiabOc(fm.formatBigDecimal(args[5]));
					ttrnRiskCommission.setRskAggregateLiabDc(fm.formatBigDecimal(args[6]));
					ttrnRiskCommission.setRskReinstateNo(fm.formatBigDecimal(args[7]));
					ttrnRiskCommission.setRskReinstateAddlPremOc(fm.formatBigDecimal(args[8]));
					ttrnRiskCommission.setRskReinstateAddlPremDc(fm.formatBigDecimal(args[9]));
					ttrnRiskCommission.setRskLeadUw(args[10]);
					ttrnRiskCommission.setRskLeadUwShare(fm.formatBigDecimal(args[11]));
					ttrnRiskCommission.setRskAccounts(args[12]);
					ttrnRiskCommission.setRskExclusion(args[13]);
					ttrnRiskCommission.setRskRemarks(args[14]);
					ttrnRiskCommission.setRskUwRecomm(args[15]);
					ttrnRiskCommission.setRskGmApproval(args[16]);
					ttrnRiskCommission.setRskOtherCost(fm.formatBigDecimal(args[17]));
					ttrnRiskCommission.setRskReinstateAddlPremPct(fm.formatBigDecimal(args[18]));
					ttrnRiskCommission.setRskBurningCostPct(fm.formatBigDecimal(args[19]));
					ttrnRiskCommission.setRskAggregateDeductOc(fm.formatBigDecimal(args[20]));
					ttrnRiskCommission.setRskAggregateDeductDc(fm.formatBigDecimal(args[21]));
					ttrnRiskCommission.setRskOccurrentLimitOc(fm.formatBigDecimal(args[22]));
					ttrnRiskCommission.setRskOccurrentLimitDc(fm.formatBigDecimal(args[23]));
					ttrnRiskCommission.setRskReinstatementPremium(args[24]);
					ttrnRiskCommission.setRskCreastaStatus(args[25]);
					ttrnRiskCommission.setRskLeadUnderwriterCountry(args[26]);
					ttrnRiskCommission.setLoginId(args[27]);
					ttrnRiskCommission.setBranchCode(args[28]);
					ttrnRiskCommission.setSysDate(new Date());
					ttrnRiskCommission.setRskDocStatus(args[29]);
					ttrnRiskCommission.setRskNoclaimbonusPrcent(fm.formatBigDecimal(args[30]));
					ttrnRiskCommission.setRskBonusId(args[31]);
					ttrnRiskCommission.setRskLayerNo(fm.formatBigDecimal(args[32]));					
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRiskCommission;
	}


	@Override
	public TtrnRiskCommission riskInsertPro3SecComm(String[] args) {
		TtrnRiskCommission ttrnRiskCommission =null;
		//risk.insert.pro3SecComm
		try {
			if(args != null) {
				ttrnRiskCommission = new TtrnRiskCommission();
				if(ttrnRiskCommission != null) {
					ttrnRiskCommission.setRskProposalNumber(args[0]);
					ttrnRiskCommission.setRskEndorsementNo(fm.formatBigDecimal(args[1]));
					ttrnRiskCommission.setRskLayerNo(fm.formatBigDecimal(args[2]));
					ttrnRiskCommission.setRskBrokerage(fm.formatBigDecimal(args[3]));
					ttrnRiskCommission.setRskTax(fm.formatBigDecimal(args[4]));
					ttrnRiskCommission.setRskProfitComm(fm.formatBigDecimal(args[5]));
					ttrnRiskCommission.setRskReserveOnLoss(fm.formatBigDecimal(args[6]));
					ttrnRiskCommission.setRskAcquistionCostOc(fm.formatBigDecimal(args[7]));
					ttrnRiskCommission.setRskAcquistionCostDc(fm.formatBigDecimal(args[8]));
					ttrnRiskCommission.setRskAggregateLiabOc(fm.formatBigDecimal(args[9]));
					ttrnRiskCommission.setRskAggregateLiabDc(fm.formatBigDecimal(args[10]));
					ttrnRiskCommission.setRskReinstateNo(fm.formatBigDecimal(args[11]));
					ttrnRiskCommission.setRskReinstateAddlPremOc(fm.formatBigDecimal(args[12]));
					ttrnRiskCommission.setRskReinstateAddlPremDc(fm.formatBigDecimal(args[13]));
					ttrnRiskCommission.setRskLeadUw(args[14]);
					ttrnRiskCommission.setRskLeadUwShare(fm.formatBigDecimal(args[15]));
					ttrnRiskCommission.setRskAccounts(args[16]);
					ttrnRiskCommission.setRskExclusion(args[17]);
					ttrnRiskCommission.setRskRemarks(args[18]);
					ttrnRiskCommission.setRskUwRecomm(args[19]);
					ttrnRiskCommission.setRskGmApproval(args[20]);
					ttrnRiskCommission.setRskDecision(args[21]);
					ttrnRiskCommission.setRskEntryDate(new Date());		
					ttrnRiskCommission.setRskEndDate(new Date());
					ttrnRiskCommission.setRskStatus(args[22]);					
					ttrnRiskCommission.setRskOtherCost(fm.formatBigDecimal(args[23]));
					ttrnRiskCommission.setRskReinstateAddlPremPct(fm.formatBigDecimal(args[24]));
					ttrnRiskCommission.setRskBurningCostPct(fm.formatBigDecimal(args[25]));
					ttrnRiskCommission.setRskAggregateDeductOc(fm.formatBigDecimal(args[26]));
					ttrnRiskCommission.setRskAggregateDeductDc(fm.formatBigDecimal(args[27]));
					ttrnRiskCommission.setRskOccurrentLimitOc(fm.formatBigDecimal(args[28]));
					ttrnRiskCommission.setRskOccurrentLimitDc(fm.formatBigDecimal(args[29]));
					ttrnRiskCommission.setRskReinstatementPremium(args[30]);
					ttrnRiskCommission.setRskCreastaStatus(args[31]);
					ttrnRiskCommission.setRskLeadUnderwriterCountry(args[32]);
					ttrnRiskCommission.setLoginId(args[33]);
					ttrnRiskCommission.setBranchCode(args[34]);
					ttrnRiskCommission.setSysDate(new Date());
					ttrnRiskCommission.setRskDocStatus(args[35]);
					ttrnRiskCommission.setRskNoclaimbonusPrcent(fm.formatBigDecimal(args[36]));
					ttrnRiskCommission.setRskBonusId(args[37]);
							
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRiskCommission;
	}
	@Override
	public List<Tuple> riskSelectChechProposalStatus(String proposalNo) {
		List<Tuple> list =new ArrayList<>();
		try{
			//risk.select.chechProposalStatus
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);
			Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

			query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),rd.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO")); 

			Subquery<Long> endA = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
			endA.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( rds.get("rskProposalNumber"), proposalNo);
			endA.where(a1);
			
			Subquery<Long> endB = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> bs = endB.from(TtrnRiskProposal.class);
			endB.select(cb.max(bs.get("rskEndorsementNo")));
			Predicate b1 = cb.equal( bs.get("rskProposalNumber"), proposalNo);
			endB.where(b1);

			Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			Predicate n3 = cb.equal(rd.get("rskProposalNumber"), b.get("rskProposalNumber"));
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
			Predicate n4 = cb.equal(b.get("rskEndorsementNo"), endB);
			query1.where(n1,n2,n3,n4);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
				
		}catch(Exception e){
			e.printStackTrace();
			}
		return list;
	}
	@Override
	public TtrnRiskProposal ttrnRiskProposalInsert(String[] input) {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.insert.pro35RskProposal
		//args--getFirstPageSecondTableAruguments,getFirstPageSecondTableInsertAruguments
		try {
		if(input != null) {
			ttrnRiskProposal = new TtrnRiskProposal();
				ttrnRiskProposal.setRskProposalNumber(input[0]);
				ttrnRiskProposal.setRskEndorsementNo(fm.formatBigDecimal(input[1]));
				ttrnRiskProposal.setRskLayerNo(fm.formatBigDecimal(input[2]));
				ttrnRiskProposal.setRskLimitOc(fm.formatBigDecimal(input[3]));
				ttrnRiskProposal.setRskLimitDc(fm.formatBigDecimal(input[4]));
				ttrnRiskProposal.setRskXlpremOc(fm.formatBigDecimal(input[5]));
				ttrnRiskProposal.setRskXlpremDc(fm.formatBigDecimal(input[6]));
				ttrnRiskProposal.setRskDeducOc(fm.formatBigDecimal(input[7]));
				ttrnRiskProposal.setRskEpiEstOc(fm.formatBigDecimal(input[8]));
				ttrnRiskProposal.setRskEpiEstDc(fm.formatBigDecimal(input[9]));
				ttrnRiskProposal.setRskMdPremOc(fm.formatBigDecimal(input[10]));
				ttrnRiskProposal.setRskMdPremDc(fm.formatBigDecimal(input[11]));
				ttrnRiskProposal.setRskShareWritten(fm.formatBigDecimal(input[12]));
				ttrnRiskProposal.setRskShareSigned(fm.formatBigDecimal(input[3]));
				ttrnRiskProposal.setRskAdjrate(fm.formatBigDecimal(input[14]));
				ttrnRiskProposal.setRskPfCovered(input[15]);
				ttrnRiskProposal.setRskSubjPremiumOc(fm.formatBigDecimal(input[16]));
				ttrnRiskProposal.setRskSubjPremiumDc(fm.formatBigDecimal(input[17]));
				ttrnRiskProposal.setRskMaxLmtCover(fm.formatBigDecimal(input[18]));
				ttrnRiskProposal.setRskDeducDc(fm.formatBigDecimal(input[19]));
				ttrnRiskProposal.setRskSpRetro(input[20]);
				ttrnRiskProposal.setRskNoOfInsurers(fm.formatBigDecimal(input[21]));
				ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(input[22]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(input[23]));
				ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(input[24]));		
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(input[25]));	
				ttrnRiskProposal.setRskMdPremOsOc(fm.formatBigDecimal(input[26]));
				ttrnRiskProposal.setRskMdPremOsDc(fm.formatBigDecimal(input[27]));
				ttrnRiskProposal.setLimitPerVesselOc(fm.formatBigDecimal(input[28]));
				ttrnRiskProposal.setLimitPerVesselDc(fm.formatBigDecimal(input[29]));
				ttrnRiskProposal.setLimitPerLocationOc(fm.formatBigDecimal(input[30]));
				ttrnRiskProposal.setLimitPerLocationDc(fm.formatBigDecimal(input[31]));
				ttrnRiskProposal.setEgpniAsOffer(fm.formatBigDecimal(input[32]));
				ttrnRiskProposal.setOurassessment(fm.formatBigDecimal(input[33]));
				ttrnRiskProposal.setEgpniAsOfferDc(fm.formatBigDecimal(input[34]));
				ttrnRiskProposal.setLoginId(input[35]);
				ttrnRiskProposal.setBranchCode(input[36]);
				ttrnRiskProposal.setSysDate(new Date());
				ttrnRiskProposal.setRskMinimumpremiumPercent(new BigDecimal(fm.formatterfour(input[37])));	
				ttrnRiskProposal.setRskGnpiCapPercent(new BigDecimal(fm.formatterfour(input[38])));
				}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}
	@Override
	public PositionMaster positionMasterInsert(String[] input) {
		PositionMaster positionMaster = null;
		//risk.insert.positionMaster
		//args--insertHomePositionMasterAruguments
		try {
		if(input != null) {
			positionMaster = new PositionMaster();
				positionMaster.setProposalNo(fm.formatBigDecimal(input[0]));
				positionMaster.setContractNo(fm.formatBigDecimal(input[1]));
				positionMaster.setAmendId(fm.formatBigDecimal(input[2]));
				positionMaster.setLayerNo(fm.formatBigDecimal(input[3]));
				positionMaster.setReinsuranceId(input[4]);
				positionMaster.setProductId(fm.formatBigDecimal(input[5]));				
				positionMaster.setDeptId(input[6]);
				positionMaster.setCedingCompanyId(input[7]);
				positionMaster.setUwYear(input[8]);
				positionMaster.setUwMonth(StringUtils.isBlank(input[9])?null :sdf.parse(input[9]));
				positionMaster.setAccountDate(StringUtils.isBlank(input[10])?null :sdf.parse(input[10]));
				positionMaster.setInceptionDate(sdf.parse(input[11]));
				positionMaster.setExpiryDate(sdf.parse(input[12]));
				positionMaster.setProposalStatus(input[13]);
				positionMaster.setEntryDate(new Date());
				positionMaster.setContractStatus(input[14]);
				positionMaster.setLoginId(input[15]);
				positionMaster.setBaseLayer(input[16]);
				positionMaster.setOldContractno(input[17]);
				positionMaster.setRenewalStatus(input[18]);
				positionMaster.setBrokerId(input[19]);
				positionMaster.setBranchCode(input[20]);
				positionMaster.setRetroType(input[21]);
				positionMaster.setUpdateLoginId(input[22]);
				positionMaster.setEndtStatus(input[23]);
				positionMaster.setRskDummyContract(input[24]);
				positionMaster.setDataMapContNo(input[25]);	
				positionMaster.setBouquetModeYn(input[26]);
				positionMaster.setBouquetNo(StringUtils.isBlank(input[27])? null : fm.formatBigDecimal(input[27]));
				positionMaster.setUwYearTo(fm.formatBigDecimal(input[28]));
				positionMaster.setSectionNo(fm.formatBigDecimal(input[29]));
				positionMaster.setOfferNo(input[30]);	
				positionMaster.setNewLayerNo(input[31]);
				
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return positionMaster;
}
	@Override
	public int reinstatementCountMain(String proposalNo, String branchCode, String amendId) {
		int count = 0;
		try {
			//REINSTATEMENT_COUNT_MAIN
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<TtrnRip> pm = query.from(TtrnRip.class);
		
			query.multiselect(cb.count(pm.get("reinstNo"))); 

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n3 = cb.equal(pm.get("amendId"), amendId);
			query.where(n1,n2,n3);
			
			TypedQuery<Long> res1 = em.createQuery(query);
			List<Long> list = res1.getResultList();
			if(list!=null) {
				count = list.get(0)==null?0:list.get(0).intValue();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int reinstatementCountMainReference(String referenceNo, String branchCode, String amendId) {
		int count = 0;
		try {
			//REINSTATEMENT_COUNT_MAIN
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<TtrnRip> pm = query.from(TtrnRip.class);
		
			query.multiselect(cb.count(pm.get("reinstNo"))); 

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("referenceNo"), referenceNo);
			Predicate n3 = cb.equal(pm.get("amendId"), amendId);
			query.where(n1,n2,n3);
			
			TypedQuery<Long> res1 = em.createQuery(query);
			List<Long> list = res1.getResultList();
			if(list!=null) {
				count = list.get(0)==null?0:list.get(0).intValue();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public String getSignShareProduct23(String proposalNo) {
		String val = "0";
		try {
			//COMMISSION_TYPE_COUNT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<BigDecimal> query1 = cb.createQuery(BigDecimal.class); 
			Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);

			query1.select(rd.get("rskShareSigned")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> p = end.from(TtrnRiskProposal.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);

			TypedQuery<BigDecimal> result = em.createQuery(query1);
			List<BigDecimal>list = result.getResultList();
			if (!CollectionUtils.isEmpty(list)) {
				val = list.get(0) == null ?"0": list.get(0).toString();
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return val;
	}
	@Override
	public List<Tuple> reinstatementMainSelectA(String proposalNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try {
			//REINSTATEMENT_MAIN_SELECT_A
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRip> rd = query1.from(TtrnRip.class);

			query1.multiselect(rd.get("reinstatement").alias("REINSTATEMENT"),
					rd.get("reinstNo").alias("REINST_NO"),
					rd.get("reinstType").alias("REINST_TYPE"),
					rd.get("amountPercent").alias("AMOUNT_PERCENT"),
					rd.get("minAmountPercent").alias("MIN_AMOUNT_PERCENT"),
					rd.get("minTimePercent").alias("MIN_TIME_PERCENT")); 
			
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnRip> p = amend.from(TtrnRip.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), rd.get("proposalNo"));
			Predicate d2 = cb.equal(p.get("branchCode"), rd.get("branchCode"));
			amend.where(d1,d2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("reinstNo")));
			
			Predicate n1 = cb.equal(rd.get("proposalNo"),proposalNo);
			Predicate n2 = cb.equal(rd.get("branchCode"), branchCode); 
			Predicate n3 = cb.equal(rd.get("sectionType"), "A"); 
			Predicate n4 = cb.equal(rd.get("amendId"), amend); 
			query1.where(n1,n2,n3,n4).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> reinstatementMainSelectAReference(String referenceNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try {
			//REINSTATEMENT_MAIN_SELECT_A
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRip> rd = query1.from(TtrnRip.class);

			query1.multiselect(rd.get("reinstatement").alias("REINSTATEMENT"),
					rd.get("reinstNo").alias("REINST_NO"),
					rd.get("reinstType").alias("REINST_TYPE"),
					rd.get("amountPercent").alias("AMOUNT_PERCENT"),
					rd.get("minAmountPercent").alias("MIN_AMOUNT_PERCENT"),
					rd.get("minTimePercent").alias("MIN_TIME_PERCENT")); 
			
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnRip> p = amend.from(TtrnRip.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("referenceNo"), rd.get("referenceNo"));
			Predicate d2 = cb.equal(p.get("branchCode"), rd.get("branchCode"));
			amend.where(d1,d2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("reinstNo")));
			
			Predicate n1 = cb.equal(rd.get("referenceNo"),referenceNo);
			Predicate n2 = cb.equal(rd.get("branchCode"), branchCode); 
			Predicate n3 = cb.equal(rd.get("sectionType"), "A"); 
			Predicate n4 = cb.equal(rd.get("amendId"), amend); 
			query1.where(n1,n2,n3,n4).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public TtrnRip insertReinstatementMain(String[] args) {
		TtrnRip ttrnRip = null;
		//INSERT_REINSTATEMENT_MAIN
		try {
		if(args != null) {
			ttrnRip = new TtrnRip();
			ttrnRip.setReinstNo(fm.formatBigDecimal(args[0]));
			ttrnRip.setReinstType(args[1]);
			ttrnRip.setAmountPercent(args[2]);
			ttrnRip.setMinAmountPercent(args[3]);
			ttrnRip.setMinTimePercent(args[4]);
			ttrnRip.setProposalNo(args[5]);
			ttrnRip.setBranchCode(args[6]);
			ttrnRip.setAmendId(fm.formatBigDecimal(args[7]));
			ttrnRip.setSubClass(fm.formatBigDecimal(args[8]));
			ttrnRip.setContractNo(args[9]);
			ttrnRip.setPoductId(args[10]);
			ttrnRip.setLayerNo(fm.formatBigDecimal(args[11]));
			ttrnRip.setReinstatement(args[12]);
			ttrnRip.setSysDate(new Date());
			ttrnRip.setSectionType(args[13]);	
			ttrnRip.setReferenceNo(fm.formatBigDecimal(args[14]));
			ttrnRip.setSno(fm.formatBigDecimal(args[15]));
			}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRip;
}

	@Override
	public TtrnRip insertReinstatementMainB(String[] args) {
		TtrnRip ttrnRip = null;
		//INSERT_REINSTATEMENT_MAIN
		try {
		if(args != null) {
			ttrnRip = new TtrnRip();
			ttrnRip.setDepartmentClass(args[0]);
			ttrnRip.setAnnualAggreLaible(fm.formatBigDecimal(args[1]));
			ttrnRip.setProposalNo(args[2]);
			ttrnRip.setBranchCode(args[3]);
			ttrnRip.setAmendId(fm.formatBigDecimal(args[4]));
			ttrnRip.setSubClass(fm.formatBigDecimal(args[5]));
			ttrnRip.setContractNo(args[6]);
			ttrnRip.setPoductId(args[7]);
			ttrnRip.setLayerNo(fm.formatBigDecimal(args[8]));
			ttrnRip.setSysDate(new Date());
			ttrnRip.setSectionType(args[9]);		
			ttrnRip.setReferenceNo(fm.formatBigDecimal(args[10]));
			}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRip;
}

	@Override
	public List<Tuple> bonusMainSelect(String proposalNo, String branchCode, String acqBonus) {
		List<Tuple> list = new ArrayList<>();
		try{
			//BONUS_MAIN_SELECT
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("lcbId").alias("LCB_ID"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("quotaShare").alias("QUOTA_SHARE"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
					pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
					pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
					pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
					pm.get("subSecCal").alias("SUB_SEC_CAL"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
		 			pm.get("fpcType").alias("FPC_TYPE"),
					pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
					
		
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), pm.get("proposalNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			Predicate a4 = cb.equal(rds.get("lcbType"), pm.get("lcbType"));
			end.where(a1,a2,a3,a4);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), acqBonus);
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n5 = cb.equal(pm.get("lcbType"), "LR");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> bonusMainSelectReference(String referenceNo, String branchCode, String acqBonus) {
		List<Tuple> list = new ArrayList<>();
		try{
			//BONUS_MAIN_SELECT_REFERENCE
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("lcbId").alias("LCB_ID"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("quotaShare").alias("QUOTA_SHARE"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
					pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
					pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
					pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
					pm.get("subSecCal").alias("SUB_SEC_CAL"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
		 			pm.get("fpcType").alias("FPC_TYPE"),
					pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
					
		
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("referenceNo"), pm.get("referenceNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			Predicate a4 = cb.equal(rds.get("lcbType"), pm.get("lcbType"));
			end.where(a1,a2,a3,a4);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("referenceNo"), referenceNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), acqBonus);
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n5 = cb.equal(pm.get("lcbType"), "LR");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}
	@Override
	public TtrnBonus bonusMainInsert(String[] args) {
			TtrnBonus ttrnBonus = null;
		    //BONUS_MAIN_INSERT
			try {
			if(args != null) {
				ttrnBonus = new TtrnBonus();
				ttrnBonus.setProposalNo(fm.formatBigDecimal(args[0]));
				ttrnBonus.setContractNo(fm.formatBigDecimal(args[1]));
				ttrnBonus.setProductId(args[2]);
				ttrnBonus.setLcbType(args[3]);
				ttrnBonus.setLcbFrom(args[4]);
				ttrnBonus.setLcbTo(args[5]);
				ttrnBonus.setLcbPercentage(args[6]);
				ttrnBonus.setUserId(args[7]);
				ttrnBonus.setBranch(args[8]);
				ttrnBonus.setLcbId(args[9]);
				ttrnBonus.setType(args[10]);
				ttrnBonus.setEndorsementNo(fm.formatBigDecimal(args[11]));
				ttrnBonus.setSubClass(args[12]);
				ttrnBonus.setSysDate(new Date());
				ttrnBonus.setLayerNo(args[13]);
				ttrnBonus.setReferenceNo(fm.formatBigDecimal(args[14]));
				ttrnBonus.setSno(fm.formatBigDecimal(args[15]));				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnBonus;
}
	@Override
	public List<Tuple> getInclusionexList(String proposalNo, String branchCode) {
		 List<Tuple>  list = new ArrayList<>();
		try {
			//GET_INCLUSIONEX_LIST
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnIeModule> pm = query.from(TtrnIeModule.class);
		
			query.multiselect(pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("layerNo").alias("LAYER_NO"),
					pm.get("transactionNo").alias("TRANSACTION_NO"),
					pm.get("eefectiveDate").alias("EEFECTIVE_DATE")).distinct(true);
					
			//end
			Subquery<Long> amend = query.subquery(Long.class);
			Root<TtrnIeModule> rds = amend.from(TtrnIeModule.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), proposalNo);
			Predicate a2 = cb.equal(rds.get("branchCode"), pm.get("branchCode"));
			amend.where(a1,a2);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n4);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
		}

	@Override
	public TtrnIeModule inclusionExclusionInsert(String[] args) {
		TtrnIeModule ttrnIeModule = null;
		//INCLUSION_EXCLUSION_INSERT
		try {
		if(args != null) {
			ttrnIeModule = new TtrnIeModule();
			ttrnIeModule.setProposalNo(fm.formatBigDecimal(args[0]));
			ttrnIeModule.setContractNo(args[1]);
			ttrnIeModule.setLayerNo(args[2]);
			ttrnIeModule.setTransactionNo(fm.formatBigDecimal(args[3]));
			ttrnIeModule.setItemNo(fm.formatBigDecimal(args[4]));
			ttrnIeModule.setItemType(args[5]);
			ttrnIeModule.setItemInclusionExclusion(args[6]);
			ttrnIeModule.setAmendId(fm.formatBigDecimal(args[7]));
			ttrnIeModule.setBranchCode(args[8]);
			ttrnIeModule.setEefectiveDate(sdf.parse(args[9]));
			ttrnIeModule.setSysDate(new Date());
			ttrnIeModule.setLoginId(args[10]);
			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnIeModule;
}
	@Override
	public List<Tuple> riskSelectGetEditModeSecPagePro3Data(String proposalNo, String layerNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getEditModeSecPagePro3Data
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<TtrnRiskCommission> com = query.from(TtrnRiskCommission.class);
			
			query.multiselect(
					pr.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),
					pr.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
					pr.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),
					pr.get("rskEpiOsofDc").alias("RSK_EPI_OSOF_DC"),
					com.get("rskBrokerage").alias("RSK_BROKERAGE"),
					com.get("rskTax").alias("RSK_TAX"),
					com.get("rskAcquistionCostOc").alias("RSK_ACQUISTION_COST_OC"),
					com.get("rskProfitComm").alias("RSK_PROFIT_COMM"),
					pr.get("rskMdPremOsOc").alias("RSK_MD_PREM_OS_OC"),
					pr.get("rskMdPremOsDc").alias("RSK_MD_PREM_OS_DC"),
					com.get("rskAggregateLiabOc").alias("RSK_AGGREGATE_LIAB_OC"),
					com.get("rskReinstateNo").alias("RSK_REINSTATE_NO"),
					com.get("rskReinstateAddlPremOc").alias("RSK_REINSTATE_ADDL_PREM_OC"),
					com.get("rskLeadUw").alias("RSK_LEAD_UW"),
					com.get("rskLeadUwShare").alias("RSK_LEAD_UW_SHARE"),
					com.get("rskAccounts").alias("RSK_ACCOUNTS"),
					com.get("rskExclusion").alias("RSK_EXCLUSION"),
					com.get("rskRemarks").alias("RSK_REMARKS"),
					com.get("rskUwRecomm").alias("RSK_UW_RECOMM"),
					com.get("rskGmApproval").alias("RSK_GM_APPROVAL"),
					com.get("rskOtherCost").alias("RSK_OTHER_COST"),
					com.get("rskReinstateAddlPremPct").alias("RSK_REINSTATE_ADDL_PREM_PCT"),
					com.get("rskBurningCostPct").alias("RSK_BURNING_COST_PCT"),
					com.get("rskAggregateDeductOc").alias("RSK_AGGREGATE_DEDUCT_OC"),
					com.get("rskReinstatementPremium").alias("RSK_REINSTATEMENT_PREMIUM"),
					com.get("rskCreastaStatus").alias("RSK_CREASTA_STATUS"),
					com.get("rskLeadUnderwriterCountry").alias("RSK_LEAD_UNDERWRITER_COUNTRY"),
					com.get("rskBonusId").alias("RSK_BONUS_ID"),
					com.get("rskNoclaimbonusPrcent").alias("RSK_NOCLAIMBONUS_PRCENT"),
					com.get("rskOccurrentLimitOc").alias("RSK_OCCURRENT_LIMIT_OC"),
					com.get("rskOccurrentLimitDc").alias("RSK_OCCURRENT_LIMIT_DC"));
					
			
			//endPr
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskProposal> prs = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(prs.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( prs.get("rskProposalNumber"), proposalNo);
			endPr.where(a1);
			//endCom
			Subquery<Long> endCom = query.subquery(Long.class); 
			Root<TtrnRiskProposal> coms = endCom.from(TtrnRiskProposal.class);
			endCom.select(cb.max(coms.get("rskEndorsementNo")));
			Predicate b1 = cb.equal( coms.get("rskProposalNumber"), proposalNo);
			endCom.where(b1);

			Predicate n1 = cb.equal(pr.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(pr.get("rskProposalNumber"), com.get("rskProposalNumber"));
			Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n4 = cb.equal(com.get("rskEndorsementNo"), endCom);
			Predicate n5 = cb.equal(com.get("rskLayerNo"), layerNo);
			query.where(n1,n2,n3,n4,n5);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
		}	
	@Override
	public int getInstalmentCount(String proposalNo, String layerNo) {
		int count = 0;
		try{
				//GET_INSTALMENT_COUNT
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Long> query = cb.createQuery(Long.class); 
				Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);
			
				query.multiselect(cb.count(pm)); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnMndInstallments> pms = amend.from(TtrnMndInstallments.class);
				amend.select(cb.max(pms.get("endorsementNo")));
				Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(pm.get("layerNo"), pms.get("layerNo"));
				Predicate a3 = cb.isNotNull(pm.get("endorsementNo"));
				amend.where(a1,a2,a3);

				Predicate n1 = cb.equal(pm.get("layerNo"), layerNo);
				Predicate n2 = cb.equal(pm.get("proposalNo"), proposalNo);
				Predicate n3 = cb.equal(pm.get("endorsementNo"), amend);
				Predicate n4 = cb.isNotNull(pm.get("endorsementNo"));
				query.where(n1,n2,n3,n4);
				
				TypedQuery<Long> res1 = em.createQuery(query);
				List<Long> list = res1.getResultList();
				if(list!=null) {
					count = list.get(0)==null?0:list.get(0).intValue();
				}
				
		}catch(Exception e){
			e.printStackTrace();
			}
		return count;
	}
	@Override
	public List<Tuple> riskSelectGetInstalmentData(String[] args) {
		 List<Tuple>  list = new ArrayList<>();
		try {
			//risk.select.getInstalmentData
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);
		
			query.multiselect(
					pm.get("payementDueDay").alias("PAYEMENT_DUE_DAY"),
					pm.get("installmentDate").alias("INSTALLMENT_DATE"),pm.get("mndPremiumOc").alias("MND_PREMIUM_OC"));
					
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnMndInstallments> rds = end.from(TtrnMndInstallments.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), args[2]);
			Predicate a2 = cb.equal(rds.get("layerNo"), args[3]);
			Predicate a3 = cb.isNotNull(pm.get("endorsementNo"));
			end.where(a1,a2,a3);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("installmentNo")));
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), args[0]);
			Predicate n2 = cb.equal(pm.get("layerNo"), args[1]);
			Predicate n3 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n4 = cb.isNotNull(pm.get("endorsementNo"));
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
		}
	@Override
	public void deleteRemarksDetails(String proposalNo, String layerNo) {
		try {
			//DELETE_REMARKS_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnRiskRemarks> delete = cb.createCriteriaDelete(TtrnRiskRemarks.class);
			
			Root<TtrnRiskRemarks> rp = delete.from(TtrnRiskRemarks.class);
			
			// MAXAmend ID
			Subquery<Long> amend = delete.subquery(Long.class); 
			Root<TtrnRiskRemarks> rrs = amend.from(TtrnRiskRemarks.class);
			amend.select(cb.max(rrs.get("amendId")));
			Predicate a1 = cb.equal( rrs.get("proposalNo"), rp.get("proposalNo"));
			amend.where(a1);
			
			//Where
			Predicate n1 = cb.equal(rp.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rp.get("layerNo"), layerNo);
			Predicate n3 = cb.equal(rp.get("amendId"), amend);
			delete.where(n1,n2,n3);
			em.createQuery(delete).executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TtrnRiskRemarks ttrnRiskRemarksInsert(String[] input) {
		TtrnRiskRemarks ttrnRiskRemarks = null;
		//INSERT_REMARKS_DETAILS
		try {
		if(input != null) {
			ttrnRiskRemarks = new TtrnRiskRemarks();
				ttrnRiskRemarks.setProposalNo(fm.formatBigDecimal(input[0]));
				ttrnRiskRemarks.setContractNo(fm.formatBigDecimal(input[1]));
				ttrnRiskRemarks.setLayerNo(fm.formatBigDecimal(input[2]));
				ttrnRiskRemarks.setDeptId(input[3]);
				ttrnRiskRemarks.setProductId(input[4]);
				ttrnRiskRemarks.setAmendId(input[5]);
				ttrnRiskRemarks.setRskSNo(input[6]);
				ttrnRiskRemarks.setRskDescription(input[7]);
				ttrnRiskRemarks.setRskRemark1(input[8]);
				ttrnRiskRemarks.setRskRemark2(input[9]);
				ttrnRiskRemarks.setLoginId(input[10]);
				ttrnRiskRemarks.setBranchCode(input[11]);
				ttrnRiskRemarks.setSysDate(new Date());;
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskRemarks;
	}
	@Override
	public List<Tuple> getClassLimitDetails(String proposalNo, String layerNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_CLASS_LIMIT_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRskClassLimits> pm = query.from(TtrnRskClassLimits.class);
		
			query.multiselect(
					pm.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
					pm.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					pm.get("rskContractNo").alias("RSK_CONTRACT_NO"),
					pm.get("rskLayerNo").alias("RSK_LAYER_NO"),
					pm.get("rskProductid").alias("RSK_PRODUCTID"),
					pm.get("rskCoverClass").alias("RSK_COVER_CLASS"),
					pm.get("rskCoverLimt").alias("RSK_COVER_LIMT"),
					pm.get("rskCoverLimtPercentage").alias("RSK_COVER_LIMT_PERCENTAGE"),
					pm.get("rskDeductableLimt").alias("RSK_DEDUCTABLE_LIMT"),
					pm.get("rskDeductablePercentage").alias("RSK_DEDUCTABLE_PERCENTAGE"),
					pm.get("entryDate").alias("ENTRY_DATE"),
					pm.get("branchCode").alias("BRANCH_CODE"),
					pm.get("status").alias("STATUS"),
					pm.get("rskEgnpiAsOff").alias("RSK_EGNPI_AS_OFF"),
					pm.get("rskGnpiAsOff").alias("RSK_GNPI_AS_OFF"),
					pm.get("rskNetMaxRetentPercent").alias("RSK_NET_MAX_RETENT_PERCENT"));  //Ri
					
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnRskClassLimits> rds = end.from(TtrnRskClassLimits.class);
			end.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal(rds.get("rskProposalNumber"), pm.get("rskProposalNumber"));
			end.where(a1);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("rskSno")));
			
			Predicate n1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(pm.get("rskLayerNo"), layerNo);
			Predicate n3 = cb.equal(pm.get("rskEndorsementNo"), end);
			query.where(n1,n2,n3).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getMaxLayerNo(String proposalNo) {
		int count = 0;
		try {
			//GET_CLASS_LIMIT_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(cb.max(pm.get("layerNo")).alias("MAXLAYER"));  
			Predicate n1 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), proposalNo);
			query.where(n1);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			if(list!=null) {
				count = list.get(0).get("MAXLAYER")==null?0:Integer.valueOf(list.get(0).get("MAXLAYER").toString());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public void getRetroConUpdate(String maxContarctNo, String proposalNo, String layerNo) {
		try {
			//GET_RETRO_CON_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnRetroCessionary> update = cb.createCriteriaUpdate(TtrnRetroCessionary.class);
			Root<TtrnRetroCessionary> m = update.from(TtrnRetroCessionary.class);
		
			update.set("contractNo", maxContarctNo);
			//end
			Subquery<Long> amend = update.subquery(Long.class);
			Root<TtrnRetroCessionary> rds = amend.from(TtrnRetroCessionary.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), m.get("proposalNo"));
			Predicate a2 = cb.equal(rds.get("layerNo"), m.get("layerNo"));
			amend.where(a1,a2);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(m.get("layerNo"), layerNo);
			Predicate n3 = cb.equal(m.get("amendId"), amend);
			update.where(n1,n2,n3);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public TtrnRiskProposal riskUpdatePro35ContSecPage(String[] args) {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.update.pro35ContSecPage
		try {
		if(args != null) {
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskEndorsementNoAndRskProposalNumberAndRskLayerNo(
					fm.formatBigDecimal(args[6]),args[7],fm.formatBigDecimal(args[8]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(args[2]));
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[3]));
				ttrnRiskProposal.setRskMdPremOsOc(fm.formatBigDecimal(args[4]));
				ttrnRiskProposal.setRskMdPremOsDc(fm.formatBigDecimal(args[5]));				
					}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}
	@Override
	public void riskUpdateHomeContNo(String[] args) {
		try {
			//GET_RETRO_CON_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
		
			update.set("contractNo", fm.formatBigDecimal(args[0]));
			update.set("proposalStatus", args[1]);
			update.set("contractStatus", args[2]);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), args[3]);
			update.where(n1);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public String riskSelectGetRskContractNo(String proposalNo) {
		String contNo = "";
		try {
			//risk.select.getRskContractNo
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query1 = cb.createQuery(String.class); 
			Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);
			
			query1.select(cb.coalesce(pm.get("rskContractNo"), "0")); 
			
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> rcs = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rcs.get("rskEndorsementNo")));
			Predicate f1 = cb.equal( rcs.get("rskProposalNumber"),  pm.get("rskProposalNumber"));
			end.where(f1);
			
			Predicate n1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);
	
			TypedQuery<String> result = em.createQuery(query1);
			List<String> list = result.getResultList();
			
			if(list!=null) {
				contNo = list.get(0)==null?"":list.get(0);
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return contNo;
	}
	@Override
	public void deleteTtrnRetroCessionary(String proposalNo, String endtNo) {
		try {
			////delete_TTRN_RETRO_CESSIONARY
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnRetroCessionary> delete = cb.createCriteriaDelete(TtrnRetroCessionary.class);
			Root<TtrnRetroCessionary> rp = delete.from(TtrnRetroCessionary.class);
			Predicate n1 = cb.equal(rp.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rp.get("amendId"), endtNo);
			delete.where(n1,n2);
			em.createQuery(delete).executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	@Override
	public TtrnRetroCessionary riskInsertRetroCessDtls(String[] input) {
		TtrnRetroCessionary ttrnRetroCessionary = null;
		//risk.insert.retroCessDtls
		try {
		if(input != null) {
			ttrnRetroCessionary = new TtrnRetroCessionary();
			ttrnRetroCessionary.setSno(fm.formatBigDecimal(input[0]));
			ttrnRetroCessionary.setProposalNo(input[1]);
			ttrnRetroCessionary.setContractNo(input[2]);
			ttrnRetroCessionary.setCedingCompanyId(fm.formatBigDecimal(input[3]));
			ttrnRetroCessionary.setBrokerId(fm.formatBigDecimal(input[4]));
			ttrnRetroCessionary.setShareAccepted(fm.formatBigDecimal(input[5]));
			ttrnRetroCessionary.setShareSigned(fm.formatBigDecimal(input[6]));
			ttrnRetroCessionary.setComission(fm.formatBigDecimal(input[7]));
			ttrnRetroCessionary.setProposalStatus(input[8]);
			ttrnRetroCessionary.setAmendId(fm.formatBigDecimal(input[9]));
			ttrnRetroCessionary.setLayerNo(fm.formatBigDecimal(input[10]));
			ttrnRetroCessionary.setEntryDate(new Date());		
			ttrnRetroCessionary.setLoginId(input[11]);		
			ttrnRetroCessionary.setBranchCode(input[12]);
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRetroCessionary;
	}
	@Transactional
	@Override
	public void deleteTtrnMndInstallments(String proposalNo, String endtNo) {
		try {
			//delete.TTRN_MND_INSTALLMENTS
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnMndInstallments> delete = cb.createCriteriaDelete(TtrnMndInstallments.class);
			Root<TtrnMndInstallments> rp = delete.from(TtrnMndInstallments.class);
			Predicate n1 = cb.equal(rp.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rp.get("endorsementNo"), endtNo);
			delete.where(n1,n2);
			em.createQuery(delete).executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public TtrnMndInstallments riskInsertInstalPrem(String[] input) {
		TtrnMndInstallments ttrnMndInstallments = null;
		//risk.insert.instalPrem
		try {
		if(input != null) {
			ttrnMndInstallments = new TtrnMndInstallments();
			ttrnMndInstallments.setInstallmentNo(fm.formatBigDecimal(input[0]));
			ttrnMndInstallments.setProposalNo(input[1]);
			ttrnMndInstallments.setContractNo(input[2]);
			ttrnMndInstallments.setLayerNo(fm.formatBigDecimal(input[3]));
			ttrnMndInstallments.setEndorsementNo(fm.formatBigDecimal(input[4]));	
			ttrnMndInstallments.setInstallmentDate(StringUtils.isBlank(input[5])?null:sdf.parse(input[5]));
			ttrnMndInstallments.setMndPremiumOc(fm.formatBigDecimal(input[6]));
			ttrnMndInstallments.setMndPremiumDc(fm.formatBigDecimal(input[7]));			
			ttrnMndInstallments.setStatus("Y");
			ttrnMndInstallments.setEntryDate(new Date());
			ttrnMndInstallments.setPayementDueDay(input[8]);
			ttrnMndInstallments.setLoginId(input[9]);
			ttrnMndInstallments.setBranchCode(input[10]);			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnMndInstallments;
	}
	@Override
	public TtrnInsurerDetails facUpddateUpdDetails(String[] input) {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		//fac.upddate.updDetails
		try {
		if(input != null) {
			ttrnInsurerDetails = ttrnInsurerDetailsRepository.findByProposalNoAndInsurerNoAndEndorsementNo(input[8],fm.formatBigDecimal(input[9]),fm.formatBigDecimal(input[10]));
			if(ttrnInsurerDetails!=null) {
				ttrnInsurerDetails.setContractNo(input[0]);		
				ttrnInsurerDetails.setType(input[1]);
				ttrnInsurerDetails.setRetroPercentage(fm.formatBigDecimal(input[2]));
				ttrnInsurerDetails.setStatus(input[3]);
				ttrnInsurerDetails.setUwYear(input[4]);
				ttrnInsurerDetails.setRetroType(input[5]);
				ttrnInsurerDetails.setLoginId(input[6]);
				ttrnInsurerDetails.setBranchCode(input[7]);				
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnInsurerDetails;
	}
	@Override
	public void facUpdateInsDetails(String proposalNo, String insurerNo) {
		try {
			//fac.update.insDetails
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnInsurerDetails> update = cb.createCriteriaUpdate(TtrnInsurerDetails.class);
			Root<TtrnInsurerDetails> m = update.from(TtrnInsurerDetails.class);
			update.set("endDate", new Date());
			//end
			Subquery<Long> end = update.subquery(Long.class);
			Root<TtrnInsurerDetails> rds = end.from(TtrnInsurerDetails.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), m.get("proposalNo"));
			Predicate a2 = cb.equal(rds.get("insurerNo"), m.get("insurerNo"));
			end.where(a1,a2);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(m.get("insurerNo"), insurerNo);
			Predicate n3 = cb.equal(m.get("endorsementNo"), end);
			update.where(n1,n2,n3);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@Transactional
	@Override
	public void retroDeleteQuery(String proposalNo, String endtNo) {
		try {
			//RETRO_DELETE_QUERY
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnInsurerDetails> delete = cb.createCriteriaDelete(TtrnInsurerDetails.class);
			Root<TtrnInsurerDetails> rp = delete.from(TtrnInsurerDetails.class);
			Predicate n1 = cb.equal(rp.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rp.get("endorsementNo"), endtNo);
			delete.where(n1,n2);
			em.createQuery(delete).executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public TtrnInsurerDetails facInsertInsDetails(String[] input) {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		//risk.insert.instalPrem
		try {
		if(input != null) {
			ttrnInsurerDetails = new TtrnInsurerDetails();
			ttrnInsurerDetails.setInsurerNo(fm.formatBigDecimal(input[0]));
			ttrnInsurerDetails.setProposalNo(input[1]);
			ttrnInsurerDetails.setContractNo(input[2]);		
			ttrnInsurerDetails.setEndorsementNo(fm.formatBigDecimal(input[3]));
			ttrnInsurerDetails.setType(input[4]);
			ttrnInsurerDetails.setRetroPercentage(fm.formatBigDecimal(input[5]));
			ttrnInsurerDetails.setStatus(input[6]);
			ttrnInsurerDetails.setUwYear(input[7]);
			ttrnInsurerDetails.setRetroType(input[8]);
			ttrnInsurerDetails.setEntryDate(new Date());
			ttrnInsurerDetails.setSubClass(fm.formatBigDecimal(input[9]));
			ttrnInsurerDetails.setLoginId(input[10]);
			ttrnInsurerDetails.setBranchCode(input[11]);			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnInsurerDetails;
	}
	@Transactional
	@Override
	public void updateReinstatementTotalNo(String proposalNo, String valueOf) {
		try {
			//UPDATE_REINSTATEMENT_TOTAL_NO
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnRiskCommission> update = cb.createCriteriaUpdate(TtrnRiskCommission.class);
			Root<TtrnRiskCommission> m = update.from(TtrnRiskCommission.class);
			update.set("rskTotalNoReinstatements", valueOf);
			//end
			Subquery<Long> end = update.subquery(Long.class);
			Root<TtrnRiskCommission> rds = end.from(TtrnRiskCommission.class);
			end.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal(rds.get("rskProposalNumber"), m.get("rskProposalNumber"));
			end.where(a1);
			
			Predicate n1 = cb.equal(m.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(m.get("rskEndorsementNo"), end);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	@Override
	public void reInstatementUpdate(String[] args) {
		try {
			//RE_INSTATEMENT_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnRip> update = cb.createCriteriaUpdate(TtrnRip.class);
			Root<TtrnRip> m = update.from(TtrnRip.class);
			update.set("contractNo", args[0]);
			update.set("layerNo",  fm.formatBigDecimal(args[1]));
			
			Predicate n1 = cb.equal(m.get("proposalNo"), args[2]);
			Predicate n2 = cb.equal(m.get("branchCode"), args[3]);
			Predicate n3 = cb.equal(m.get("amendId"), args[4]);
			update.where(n1,n2,n3);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public TtrnCrestazoneDetails moveToCrestaMainTable(String[] input){
		TtrnCrestazoneDetails ttrnCrestazoneDetails = null;
		//MOVE_TO_CRESTA_MAIN_TABLE
		try {
		if(input != null) {
			ttrnCrestazoneDetails = new TtrnCrestazoneDetails();
			ttrnCrestazoneDetails.setContractNo(fm.formatBigDecimal(input[0]));
			ttrnCrestazoneDetails.setProposalNo(fm.formatBigDecimal(input[1]));
			ttrnCrestazoneDetails.setAmendId(input[2]);
			ttrnCrestazoneDetails.setSubClass(input[3]);
			ttrnCrestazoneDetails.setCrestaId(input[4]);
			ttrnCrestazoneDetails.setCrestaName(input[5]);
			ttrnCrestazoneDetails.setCurrency(input[6]);
			ttrnCrestazoneDetails.setAccRisk(input[7]);
			if(StringUtils.isNotEmpty(input[8]))
			ttrnCrestazoneDetails.setAccumDate(sdf.parse(input[8]));
			ttrnCrestazoneDetails.setEntryDate(new Date());		
			ttrnCrestazoneDetails.setStatus("Y");
			ttrnCrestazoneDetails.setBranchCode(input[9]);
			ttrnCrestazoneDetails.setTerritoryCode(input[10]);
			ttrnCrestazoneDetails.setSno(input[11]);			
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnCrestazoneDetails;
		}
	@Transactional
	@Override
	public void updateContractDetails(String[] args) {
		try {
			//UPDATE_CONTRACT_DETAILS
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnBonus> update = cb.createCriteriaUpdate(TtrnBonus.class);
			Root<TtrnBonus> m = update.from(TtrnBonus.class);
			update.set("contractNo", fm.formatBigDecimal(args[0]));
			
			Predicate n1 = cb.equal(m.get("proposalNo"), args[1]);
			Predicate n2 = cb.equal(m.get("branch"), args[2]);
			Predicate n3 = cb.equal(m.get("type"), args[3]);
			Predicate n4 = cb.equal(m.get("endorsementNo"), args[4]);
			Predicate n5 = cb.equal(m.get("layerNo"), args[5]);
			update.where(n1,n2,n3,n4,n5);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<Tuple> getLayerInfo(String[] obj) {
		List<Tuple> result=new ArrayList<>();
		try {
			//GET_LAYER_INFO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);
			
			//deptName
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(coms.get("tmasDepartmentName"));
			Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rd.get("rskDeptid"));
			Predicate a2 = cb.equal( coms.get("tmasProductId"), pm.get("productId"));
			Predicate a3 = cb.equal( coms.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
			deptName.where(a1,a2,a3,a4);
			
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate b1 = cb.equal(rds.get("categoryId"), cb.selectCase().when(cb.equal(pm.get("productId"),"2"), fm.formatBigDecimal("43")).otherwise(fm.formatBigDecimal("29")));
			Predicate b2 = cb.equal(rds.get("type"),cb.selectCase().when(cb.equal(pm.get("productId"),"2"), rd.get("treatytype")).otherwise(rd.get("rskBusinessType")));
			detailName.where(b1, b2);
			
			query.multiselect(
					pm.get("offerNo").alias("OFFER_NO"),
					pm.get("layerNo").alias("LAYER_NO"),
					pm.get("newLayerNo").alias("NEW_LAYER_NO"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_LAYER"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("deptId").alias("DEPT_ID"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("sectionNo").alias("SECTION_NO"),
					deptName.alias("TMAS_DEPARTMENT_NAME"),
					detailName.alias("TREATY_TYPE"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"));  
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo"))));
			orderList.add(cb.asc(pm.get("proposalNo")));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pi1 = amend.from(PositionMaster.class);
			amend.select(cb.max(pi1.get("amendId")));
			Predicate c1 = cb.equal( pi1.get("proposalNo"), pm.get("proposalNo"));
			Predicate c2 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			amend.where(c1,c2);
			
			//end
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rks.get("rskEndorsementNo")));
			Predicate d1 = cb.equal( rks.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			Predicate d2 = cb.equal( rks.get("branchCode"), rd.get("branchCode"));
			end.where(d1,d2);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n2 = cb.equal(pm.get("proposalNo"), obj[0]);
			Predicate n3 = cb.equal(pm.get("baseLayer"), obj[1]);
			Predicate n4 = cb.or(n2,n3);
			Expression<String> e0 = pm.get("contractStatus");
			Predicate n5 = e0.in("P");
			Predicate n6 = cb.equal(pm.get("amendId"), amend);
			Predicate n7 = cb.equal(rd.get("rskEndorsementNo"), end);
			query.where(n1,n4,n5,n6,n7).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			result = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Tuple> getLayerContractInfo(String[] obj) {
		List<Tuple> result=new ArrayList<>();
		try {
			//GET_LAYER_CONTRACT_INFO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);
			
			//deptName
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(coms.get("tmasDepartmentName"));
			Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rd.get("rskDeptid"));
			Predicate a2 = cb.equal( coms.get("tmasProductId"), pm.get("productId"));
			Predicate a3 = cb.equal( coms.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
			deptName.where(a1,a2,a3,a4);
			
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate b1 = cb.equal(rds.get("categoryId"), cb.selectCase().when(cb.equal(pm.get("productId"),"2"), fm.formatBigDecimal("43")).otherwise(fm.formatBigDecimal("29")));
			Predicate b2 = cb.equal(rds.get("type"),cb.selectCase().when(cb.equal(pm.get("productId"),"2"), rd.get("treatytype")).otherwise(rd.get("rskBusinessType")));
			detailName.where(b1, b2);
			
			query.multiselect(
					pm.get("offerNo").alias("OFFER_NO"),
					pm.get("layerNo").alias("LAYER_NO"),
					pm.get("newLayerNo").alias("NEW_LAYER_NO"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_LAYER"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("deptId").alias("DEPT_ID"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("sectionNo").alias("SECTION_NO"),
					deptName.alias("TMAS_DEPARTMENT_NAME"),
					detailName.alias("TREATY_TYPE"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"));  
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo"))));
			orderList.add(cb.asc(pm.get("proposalNo")));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pi1 = amend.from(PositionMaster.class);
			amend.select(cb.max(pi1.get("amendId")));
			Predicate c1 = cb.equal( pi1.get("proposalNo"), pm.get("proposalNo"));
			Predicate c2 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			amend.where(c1,c2);
			
			//end
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rks.get("rskEndorsementNo")));
			Predicate d1 = cb.equal( rks.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			Predicate d2 = cb.equal( rks.get("branchCode"), rd.get("branchCode"));
			end.where(d1,d2);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n2 = cb.equal(pm.get("proposalNo"), obj[0]);
			Predicate n3 = cb.equal(pm.get("baseLayer"), obj[1]);
			Predicate n4 = cb.or(n2,n3);
			Expression<String> e0 = pm.get("contractStatus");
			Predicate n5 = e0.in("A");
			Predicate n6 = cb.equal(pm.get("amendId"), amend);
			Predicate n7 = cb.equal(rd.get("rskEndorsementNo"), end);
			query.where(n1,n4,n5,n6,n7).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			result = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@Transactional
	@Override
	public void cancelOldProposal(String proposalNo) {
		try {
			//CANCEL_OLD_PROPOSAL
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			
			update.set("renewalStatus", "1");
			//amend
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<PositionMaster> p = amend.from(PositionMaster.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), m.get("proposalNo"));
			amend.where(d1);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(m.get("amendId"), amend);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Transactional
	@Override
	public void cancelProposal(String newProposal) {
		try {
			//CANCEL_PROPOSAL
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			
			update.set("contractStatus", "C");
			update.set("editMode", "N");
			//amend
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<PositionMaster> p = amend.from(PositionMaster.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), m.get("proposalNo"));
			amend.where(d1);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), newProposal);
			Predicate n2 = cb.equal(m.get("amendId"), amend);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCountOfInstallmentNumber(String proposalNo) {
		int count =0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<RskPremiumDetails> pm = query.from(RskPremiumDetails.class);
			
			query.multiselect(pm.get("instalmentNumber").alias("instalmentNumber")).distinct(true); 
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
		
			query.where(n1);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			if(list!=null) {
				for(Tuple data: list) {
					
					
					if("RP".equalsIgnoreCase(data.get("instalmentNumber").toString())  || "RTP".equalsIgnoreCase(data.get("instalmentNumber").toString()) || "AP".equalsIgnoreCase(data.get("instalmentNumber").toString())) {}
					else{
						count = count +1;
					}
				}	
				}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public BigDecimal getRipSno() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<TtrnRip> root = cq.from(TtrnRip.class);
        
        cq.select(cb.coalesce(cb.max(root.get("sno")), 0).as(BigDecimal.class).alias("SNO"));        
        return em.createQuery(cq).getSingleResult();
	}

	}
 
