package com.maan.insurance.service.impl.Dropdown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiPlacementStatus;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.validation.Formatters;

@Repository
public class DropDownCustomRepositoryImple implements DropDownCustomRepository{
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private Formatters fm;

	@Override
	public List<Tuple> getExistingBouquet(String branchCode, String bouquetNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_EXISTING_BOUQUET
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			//deptName
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(coms.get("tmasDepartmentName"));
			Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rd.get("rskDeptid"));
			Predicate a2 = cb.equal( coms.get("tmasProductId"), pm.get("productId"));
			Predicate a3 = cb.equal( coms.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
			deptName.where(a1,a2,a3,a4);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Predicate d1 = cb.equal( pi.get("customerId"), rd.get("rskCedingid"));
			Predicate d2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("customerType"), "C");
			companyName.where(d1,d2,d3);
			
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate b1 = cb.equal(rds.get("categoryId"), cb.selectCase().when(cb.equal(pm.get("productId"),"2"), fm.formatBigDecimal("43")).otherwise(fm.formatBigDecimal("29")));
			Predicate b2 = cb.equal(rds.get("type"),cb.selectCase().when(cb.equal(pm.get("productId"),"2"), rd.get("treatytype")).otherwise(rd.get("rskBusinessType")));
			detailName.where(b1, b2);
			
			//bussinessType
			Subquery<String> bussinessType = query.subquery(String.class); 
			Root<TmasProductMaster> bm = bussinessType.from(TmasProductMaster.class);
			bussinessType.select(bm.get("tmasProductName"));
			Predicate e1 = cb.equal( bm.get("tmasProductId"), pm.get("productId"));
			Predicate e2 = cb.equal( bm.get("branchCode"), pm.get("branchCode"));
			bussinessType.where(e1,e2);
			
			query.multiselect(
					rd.get("rskInceptionDate").alias("INS_DATE"),
					rd.get("rskExpiryDate").alias("EXP_DATE"),
					companyName.alias("COMPANY_NAME"),
					pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					rd.get("treatytype").alias("TREATYTYPE"),
					pm.get("productId").alias("PRODUCT_ID"),
					bussinessType.alias("BUSINESS_TYPE"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					detailName.alias("TREATY_TYPE"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"),
					cb.selectCase().when(cb.equal(pm.get("oldContractno"), ""), "New").otherwise("Renew").alias("POLICY_STATUS"),
					cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_LAYER"),
					pm.get("sectionNo").alias("SECTION_NO"),
					pm.get("layerNo").alias("LAYER_NO"),
					deptName.alias("TMAS_DEPARTMENT_NAME"),
					pm.get("offerNo").alias("OFFER_NO")); 
	
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("offerNo")));
			orderList.add(cb.asc(pm.get("proposalNo")));
			
			Predicate n1 = cb.isNotNull(pm.get("bouquetNo"));
			Predicate n2 = cb.equal(pm.get("branchCode"), branchCode); 
			Predicate n4 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n3 = cb.equal(pm.get("bouquetNo"), bouquetNo);
			Predicate n5 = cb.equal(pm.get("contractStatus"), "P");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getExistingBaselayer(String branchCode, String baseProposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_EXISTING_BASELAYER
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			//deptName
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(coms.get("tmasDepartmentName"));
			Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rd.get("rskDeptid"));
			Predicate a2 = cb.equal( coms.get("tmasProductId"), pm.get("productId"));
			Predicate a3 = cb.equal( coms.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
			deptName.where(a1,a2,a3,a4);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Predicate d1 = cb.equal( pi.get("customerId"), rd.get("rskCedingid"));
			Predicate d2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("customerType"), "C");
			companyName.where(d1,d2,d3);
			
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate b1 = cb.equal(rds.get("categoryId"), cb.selectCase().when(cb.equal(pm.get("productId"),"2"), fm.formatBigDecimal("43")).otherwise(fm.formatBigDecimal("29")));
			Predicate b2 = cb.equal(rds.get("type"),cb.selectCase().when(cb.equal(pm.get("productId"),"2"), rd.get("treatytype")).otherwise(rd.get("rskBusinessType")));
			detailName.where(b1, b2);
			
			//bussinessType
			Subquery<String> bussinessType = query.subquery(String.class); 
			Root<TmasProductMaster> bm = bussinessType.from(TmasProductMaster.class);
			bussinessType.select(bm.get("tmasProductName"));
			Predicate e1 = cb.equal( bm.get("tmasProductId"), pm.get("productId"));
			Predicate e2 = cb.equal( bm.get("branchCode"), pm.get("branchCode"));
			bussinessType.where(e1,e2);
			
			query.multiselect(
					rd.get("rskInceptionDate").alias("INS_DATE"),
					rd.get("rskExpiryDate").alias("EXP_DATE"),
					companyName.alias("COMPANY_NAME"),
					pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					rd.get("treatytype").alias("TREATYTYPE"),
					pm.get("productId").alias("PRODUCT_ID"),
					bussinessType.alias("BUSINESS_TYPE"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					detailName.alias("TREATY_TYPE"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"),
					cb.selectCase().when(cb.equal(pm.get("oldContractno"), ""), "New").otherwise("Renew").alias("POLICY_STATUS"),
					cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_LAYER"),
					pm.get("sectionNo").alias("SECTION_NO"),
					pm.get("layerNo").alias("LAYER_NO"),
					deptName.alias("TMAS_DEPARTMENT_NAME"),
					pm.get("offerNo").alias("OFFER_NO")); 
	
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(pm.get("baseLayer")));
			orderList.add(cb.asc(pm.get("proposalNo")));
			
			Predicate n2 = cb.equal(pm.get("branchCode"), branchCode); 
			Predicate n4 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), baseProposalNo);
			Predicate n5 = cb.equal(pm.get("contractStatus"), "P");
			query.where(n2,n3,n4,n5).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> newContractSummary(String branchCode, String proposalNo) {
		List<Tuple> list = new ArrayList<>(); //need to check query single row
		try {
			//NEW_CONTRACT_SUMMARY
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> trd = query.from(TtrnRiskDetails.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiPlacement> trp = query.from(TtrnRiPlacement.class);
			Root<TtrnRiPlacementStatus> trps = query.from(TtrnRiPlacementStatus.class);
			Root<TtrnRiskProposal> tr = query.from(TtrnRiskProposal.class);
		
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(pi.get("firstName"), " ");
			Expression<String> e1 = cb.concat(e0, pi.get("lastName"));
		//	Expression<Object> e2 = cb.selectCase().when(cb.equal(pi.get("customerType"), "C"), pi.get("companyName")).otherwise(e1);
			reinsurerName.select(e1);
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> pis = amen.from(PersonalInfo.class);
			amen.select(cb.max(pis.get("amendId")));
			Predicate q1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
			amen.where(q1);
			Predicate a1 = cb.equal( pi.get("customerType"), "R");
			Predicate a2 = cb.equal( pi.get("customerId"), trp.get("reinsurerId"));
			Predicate a3 = cb.equal( pi.get("branchCode"), trp.get("branchCode"));
			Predicate a4 = cb.equal( pi.get("amendId"), amen);
			reinsurerName.where(a1,a2,a3,a4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"), pib.get("customerId"));
			amenb.where(s1);
			Predicate d1 = cb.equal( pib.get("customerType"), "B");
			Predicate d2 = cb.equal( pib.get("customerId"), trp.get("brokerId"));
			Predicate d3 = cb.equal( pib.get("branchCode"), trp.get("branchCode"));
			Predicate d4 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(d1,d2,d3,d4);
			
			//shortName
      		Subquery<String> shortName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = shortName.from(CurrencyMaster.class);
      		shortName.select(cm.get("shortName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate b1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate b2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(b1,b2);
      		Predicate x1 = cb.equal(trd.get("rskOriginalCurr"), cm.get("currencyId"));
      		Predicate x2 = cb.equal( pm.get("branchCode"), cm.get("branchCode"));
      		Predicate x3 =cm.get("amendId").in(amendA==null?null:amendA);
      		shortName.where(x3,x1,x2);
			
			
			query.multiselect( //Math.round
							pm.get("offerNo").alias("OFFER_NO"),
							cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_PROPOSAL"),		
							pm.get("proposalNo").alias("PROPOSAL_NO"),
							trd.get("rskTreatyid").alias("TREATY_NAME"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), pm.get("sectionNo")).otherwise(pm.get("layerNo")).alias("LAYER_SECTION"),
							trp.get("sno").alias("SNO"),
							reinsurerName.alias("REINSURER_NAME"),
							brokerName.alias("BROKER_NAME"),
							shortName.alias("CURRENCY"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), tr.get("rskEpiEstOc")).otherwise(cb.selectCase().when(cb.equal(pm.get("productId"), "3"), tr.get("rskSubjPremiumOc"))).alias("EPI_100_OC"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), tr.get("rskEpiEstDc")).otherwise(cb.selectCase().when(cb.equal(pm.get("productId"), "3"), tr.get("rskSubjPremiumDc"))).alias("EPI_100_DC"),
							cb.coalesce(trps.get("newStatus"), trp.get("status")).alias("PLACING_STATUS"),
							cb.coalesce(trp.get("shareSigned"), "0").alias("SHARE_SIGNED"),
							cb.coalesce(trp.get("brokerage"), "0").alias("BROKERAGE") //(EPI_100_DC)*(BROKERAGE/100)*(SHARE_SIGNED/100) = BROKERAGE_AMT
							).distinct(true); 
	
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(trp.get("sno")));
			orderList.add(cb.asc(pm.get("proposalNo")));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
			amend.select(cb.max(pm1.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), pm1.get("proposalNo"));
			Predicate r2 = cb.equal( pm1.get("branchCode"),  pm.get("branchCode"));
			amend.where(r1,r2);
			//status
			Subquery<Long> status = query.subquery(Long.class); 
			Root<TtrnRiPlacement> trp1 = status.from(TtrnRiPlacement.class);
			status.select(cb.max(trp1.get("statusNo")));
			Predicate p1 = cb.equal( trp.get("proposalNo"), trp1.get("proposalNo"));
			Predicate p2 = cb.equal( trp1.get("branchCode"),  trp.get("branchCode"));
			Predicate p3 = cb.equal( trp1.get("sno"),  trp.get("sno"));
			status.where(p1,p2,p3);
			
			List<Predicate> predicList = new ArrayList<Predicate>();
			predicList.add(pm.get("amendId").in(amend==null?null:amend));
			predicList.add(cb.equal(pm.get("branchCode"), trp.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), trp.get("proposalNo")));
			predicList.add(trp.get("statusNo").in(status==null?null:status));
			predicList.add(cb.equal(pm.get("proposalNo"), trps.get("proposalNo")));
			predicList.add(cb.equal( trps.get("sno"),  trp.get("sno")));
			predicList.add(cb.equal( trps.get("statusNo"),  trp.get("statusNo")));
			predicList.add(cb.equal(pm.get("branchCode"), trd.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")));
			predicList.add(cb.equal(pm.get("amendId"), trd.get("rskEndorsementNo")));
			predicList.add(cb.equal(pm.get("branchCode"), tr.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), tr.get("rskProposalNumber")));
			predicList.add(cb.equal(pm.get("amendId"), tr.get("rskEndorsementNo")));
			predicList.add(cb.equal(pm.get("branchCode"), branchCode));
			predicList.add(cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), proposalNo));
			predicList.add(cb.equal(cb.coalesce(trps.get("newStatus"), trp.get("status")), "CSL"));
			
			query.where(predicList.toArray(new Predicate[0])).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
		}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getReinsurerInfo(String branchCode, String layerProposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_REINSURER_INFO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> trd = query.from(TtrnRiskDetails.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			Root<TtrnRiPlacementStatus> ps = query.from(TtrnRiPlacementStatus.class);
			Root<TtrnRiskProposal> trp = query.from(TtrnRiskProposal.class);
			
			query.multiselect(
					p.get("sno").alias("SNO"),
					p.get("bouquetNo").alias("BOUQUET_NO"),
					p.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					p.get("proposalNo").alias("PROPOSAL_NO"),
					p.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					p.get("reinsurerId").alias("REINSURER_ID"),
					p.get("brokerId").alias("BROKER_ID"),
					p.get("shareOffered").alias("SHARE_OFFERED"),
					p.get("shareWritten").alias("SHARE_WRITTEN"),
					p.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),
					p.get("shareSigned").alias("SHARE_SIGNED"),
					p.get("brokerage").alias("BROKERAGE_PER"),
					p.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),
					p.get("placementAmendId").alias("PLACEMENT_AMEND_ID"),
					cb.coalesce(ps.get("currentStatus"), p.get("status")).alias("CURRENT_STATUS"),
					ps.get("newStatus").alias("NEW_STATUS"),
					p.get("statusNo").alias("STATUS_NO")); 
	
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
			orderList.add(cb.asc(p.get("proposalNo")));
			
			//status
			Subquery<Long> status = query.subquery(Long.class); 
			Root<TtrnRiPlacement> p1 = status.from(TtrnRiPlacement.class);
			status.select(cb.max(p1.get("statusNo")));
			Predicate x1 = cb.equal( p1.get("proposalNo"), p.get("proposalNo"));
			Predicate x2 = cb.equal( p1.get("branchCode"),  p.get("branchCode"));
			Predicate x3 = cb.equal( p1.get("sno"),  p.get("sno"));
			status.where(x1,x2,x3);
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
			amend.select(cb.max(pm1.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), pm1.get("proposalNo"));
			Predicate r2 = cb.equal( pm1.get("branchCode"),  pm.get("branchCode"));
			amend.where(r1,r2);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("proposalNo"), ps.get("proposalNo")));
			predicates.add(cb.equal(p.get("branchCode"), ps.get("branchCode")));
			predicates.add(cb.equal(p.get("statusNo"), ps.get("statusNo")));
			predicates.add(p.get("statusNo").in(status==null?null:status));
			predicates.add(cb.equal(p.get("branchCode"), pm.get("branchCode")));
			predicates.add(cb.equal(p.get("proposalNo"), pm.get("proposalNo")));
			predicates.add(pm.get("amendId").in(amend==null?null:amend));
			predicates.add(cb.equal(pm.get("branchCode"), trd.get("branchCode")));
			predicates.add(cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")));
			predicates.add(cb.equal(pm.get("amendId"), trd.get("rskEndorsementNo")));
			predicates.add(cb.equal(pm.get("branchCode"), trp.get("branchCode")));
			predicates.add(cb.equal(pm.get("proposalNo"), trp.get("rskProposalNumber")));
			predicates.add(cb.equal(pm.get("amendId"), trp.get("rskEndorsementNo")));
			predicates.add(cb.equal(p.get("branchCode"), branchCode));
			predicates.add(cb.equal(p.get("proposalNo"), layerProposalNo));
			predicates.add(cb.equal(pm.get("amendId"), p.get("amendId")));
			
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public String getBaseProposalNo(String proposalNo) {
		String proposal = "";
		try{
			//GET_BASE_PROPOSAL_NO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(cb.selectCase().when(cb.isNull(pm.get("proposalNo")), 0l)
					.otherwise(pm.get("proposalNo"))).distinct(true); 
			
			//contractNo
			Subquery<Long> cont = query.subquery(Long.class); 
			Root<PositionMaster> cs = cont.from(PositionMaster.class);
			cont.select(cs.get("oldContractno")).distinct(true);
			//amendId
			Subquery<Long> amendId = query.subquery(Long.class); 
			Root<PositionMaster> cs1 = amendId.from(PositionMaster.class);
			amendId.select(cb.max(cs1.get("amendId")));
			Predicate c1 = cb.equal( pm.get("proposalNo"), cs1.get("proposalNo"));
			amendId.where(c1);
			
			Predicate b1 = cb.equal( cs.get("proposalNo"), proposalNo);
			Predicate b2 = cb.equal( cs.get("amendId"), amendId);
			cont.where(b1,b2);

			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);

			// Where
			Predicate n1 = cb.isNull(pm.get("baseLayer"));
			Predicate n2 = cb.equal(pm.get("contractNo"), cont);
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n2);
			
			TypedQuery<Long> result = em.createQuery(query);
			List<Long> list = result.getResultList();
			
			proposal = list.size()==0?"0":list.get(0).toString();
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return proposal;
	}

}
