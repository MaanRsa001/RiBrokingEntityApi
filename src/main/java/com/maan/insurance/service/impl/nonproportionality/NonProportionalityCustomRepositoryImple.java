package com.maan.insurance.service.impl.nonproportionality;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;

@Repository
public class NonProportionalityCustomRepositoryImple implements NonProportionalityCustomRepository {
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

}
