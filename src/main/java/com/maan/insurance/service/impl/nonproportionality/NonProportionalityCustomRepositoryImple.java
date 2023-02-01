package com.maan.insurance.service.impl.nonproportionality;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;

@Repository
public class NonProportionalityCustomRepositoryImple implements NonProportionalityCustomRepository {
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@PersistenceContext
	private EntityManager em;
	
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
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, new BigDecimal(args[2]));
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
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, new BigDecimal(args[1]));
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

}
