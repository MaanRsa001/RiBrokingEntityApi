package com.maan.insurance.service.impl.billing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.jpa.entity.treasury.TtrnRetroSoa;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnBillingDetails;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.entity.TtrnClaimPaymentRi;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;

@Repository
public class BillingCustomRepositoryImple implements BillingCustomRepository {

	@Autowired
	EntityManager em;

	@Override
	@Transactional
	public int updateAlloTranDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnBillingInfo> update = cb.createCriteriaUpdate(TtrnBillingInfo.class);
		Root<TtrnBillingInfo> root = update.from(TtrnBillingInfo.class);

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnBillingInfo> subRoot = sq.from(TtrnBillingInfo.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("billingNo"), root.get("billingNo")));
		
	//	Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnBillingDetails") + Double.parseDouble(args[0]);
		update.set(root.get("loginId"), args[1])
			  .set(root.get("branchCode"), args[2])
			  .set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));

		update.where(cb.equal(root.get("billingNo"), args[3]),
					 cb.equal(root.get("currencyId"), args[4]),
					 cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
		
	}
	
	@Override
	public Integer getNextRetDtlsNo() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

		cq.multiselect(cb.selectCase().when(cb.isNull(cb.max(root.get("receiptNo"))), 0)
				.otherwise(cb.sum(cb.max(root.get("receiptNo")), 1)));
		TypedQuery<Integer> q = em.createQuery(cq);
		List<Integer> receiptList = q.getResultList();
		return receiptList != null && !receiptList.isEmpty() ? receiptList.get(0) : null;
	}
	public List<Tuple> getTranContDtls(GetTransContractReqRi req) {
		List<Tuple> resultList = getTranContDtlsForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getCurrencyId(), req.getBranchCode(),req.getProductId());
		if(Objects.nonNull(resultList))
			resultList.addAll(getTranContDtlsForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode(),req.getProductId()));
		else
			resultList = getTranContDtlsForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode(),req.getProductId());
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}
	private List<Tuple> getTranContDtlsForRsk(String brokerId, String cedingId, String alloccurrencyId, String branchCode,String productId) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<RskPremiumDetails> rRoot = cq.from(RskPremiumDetails.class);
		String input = null;

//		Expression<String> funct = cb.function("FN_GET_NAME", String.class, cb.literal("P"), pRoot.get("productId"),
//				pRoot.get("branchCode"));
		
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		Expression<String> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("netdueOc")), 0.0)
						.otherwise(rRoot.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate"))).as(String.class);

		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName")).where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")), cb.equal(subRoot.get("amendId"), aSq));

		cq.multiselect(rRoot.get("transactionNo").as(String.class), pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"), rRoot.get("entryDateTime"), exp.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), rRoot.get("accPremium").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.selectCase().when(cb.isNull(rRoot.get("checkyn")), "N").as(String.class),
				cb.literal("P").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				pRoot.get("deptId").as(String.class),
				pRoot.get("proposalNo").as(String.class)).distinct(true);

		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);

		Expression<Object> exp1 = cb.selectCase()
				.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
						pRoot.get("cedingCompanyId"))
				.otherwise(pRoot.get("brokerId"));

		if("63".equalsIgnoreCase(brokerId))
			input=cedingId;
		else        	
			input=brokerId;
		
		pSq.select(cb.max(pSubRoot.get("amendId"))).where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
				cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))),
				cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")), cb.equal(rRoot.get("currencyId"), alloccurrencyId),
				cb.equal(exp1, input));
				//cb.like(pRoot.get("contractNo"), ""), cb.like(pRoot.get("productId"), "")); // check

		List<Predicate>predicates=new ArrayList<Predicate>();
		predicates.add(cb.or(cb.isNull(rRoot.get("receiptNo")),cb.equal(rRoot.get("receiptNo"), "0")));
		predicates.add(cb.equal(rRoot.get("contractNo"), pRoot.get("contractNo")));
		predicates.add(cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
				cb.selectCase().when(cb.isNull(rRoot.get("layerNo")), 0).otherwise(rRoot.get("layerNo"))));
		predicates.add(cb.equal(pRoot.get("deptId"), rRoot.get("subClass")));
		predicates.add(cb.equal(pRoot.get("branchCode"), branchCode));
		predicates.add(cb.notEqual(exp, 0));
		predicates.add(cb.equal(pRoot.get("amendId"), pSq));
		 if(!"0".equalsIgnoreCase(productId)) {
			 predicates.add(cb.equal(pRoot.get("productId"), productId));
		 }
		 cq.where(predicates.toArray(new Predicate[0]));

		list =  em.createQuery(cq).getResultList();
		}catch(Exception e) {
			e.printStackTrace();;
		}
		return list;
	}
	private List<Tuple> getTranContDtlsForClaim(String brokerId, String cedingId, String alloccurrencyId, String branchCode,String productId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		Root<TtrnClaimPayment> tcpRoot = cq.from(TtrnClaimPayment.class);
		String input = null;
		
//		Expression<String> funct = cb.function("FN_GET_NAME", String.class,
//     		   cb.literal("P"),
//     		  pRoot.get("productId"),
//     		 pRoot.get("branchCode"));
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		
		Expression<String> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
				.otherwise(tcpRoot.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(tcpRoot.<Double>get("allocatedTillDate"))).as(String.class);
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);
		
		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
		  .where(cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName"))
		  .where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")),
				cb.equal(subRoot.get("amendId"), aSq));
		
		cq.multiselect(tcpRoot.get("claimPaymentNo").as(String.class),
				pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"),
				tcpRoot.get("inceptionDate"),
				cb.nullLiteral(Double.class).alias("NETDUE_OC"),
				exp.alias("PAID_AMOUNT"),
				cb.nullLiteral(Double.class).alias("ACC_PREMIUM"),
				tcpRoot.get("accClaim").as(String.class),
				cb.selectCase().when(cb.isNull(tcpRoot.get("checkyn")), "N").as(String.class),
				cb.literal("C").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"),
				pRoot.get("deptId"),
				pRoot.get("proposalNo")).distinct(true);
 		
		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);
		
	   Expression<Object> exp1 = cb.selectCase().when(
	    		cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
	    		pRoot.get("cedingCompanyId")).otherwise(pRoot.get("brokerId"));
	   
	   if("63".equalsIgnoreCase(brokerId)) {
		   input=cedingId;
		}
		else {        	
			input=brokerId;
		}
		
		pSq.select(cb.max(pSubRoot.get("amendId")))
		  .where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
			     cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
	    		 cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))), 
			     cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")),
			     cb.equal(tcdRoot.get("currency"), alloccurrencyId),
			     cb.equal(exp1, input));
		
		List<Predicate>predicates=new ArrayList<Predicate>();
		 predicates.add(cb.equal(pRoot.get("contractNo"), tcdRoot.get("contractNo")));
		 predicates.add(cb.equal(tcdRoot.get("contractNo"), tcpRoot.get("contractNo")));
		 predicates.add(cb.equal(tcdRoot.get("claimNo"), tcpRoot.get("claimNo")));
		 predicates.add(cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
		 cb.selectCase().when(cb.isNull(tcdRoot.get("layerNo")), 0).otherwise(tcdRoot.get("layerNo"))));
		 predicates.add(cb.equal(pRoot.get("sectionNo"), tcdRoot.get("subClass")));
		 predicates.add(cb.equal(pRoot.get("branchCode"), branchCode));
		 predicates.add(cb.notEqual(exp, 0));
		 predicates.add(cb.equal(pRoot.get("amendId"), pSq));
		 if(!"0".equalsIgnoreCase(productId)) {
			 predicates.add(cb.equal(pRoot.get("productId"), productId));
		 }
		 cq.where(predicates.toArray(new Predicate[0]));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Tuple> getBillingInfoList(GetBillingInfoListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnBillingInfo> bi = query.from(TtrnBillingInfo.class);
		
		//brokerName
		Subquery<String> brokerName = query.subquery(String.class); 
		Root<PersonalInfo> pi = brokerName.from(PersonalInfo.class);
	
		Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
		brokerName.select(cb.concat(firstName1, pi.get("lastName")));
		//maxAmend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate b1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
		maxAmend.where(b1);
		
		Predicate a1 = cb.equal( pi.get("customerType"), "B");
		Predicate a2 = cb.equal( pi.get("customerId"), bi.get("brokerId"));
		Predicate a3 = cb.equal( pi.get("branchCode"), bi.get("branchCode"));
		Predicate a4 = cb.equal( pi.get("amendId"), maxAmend);
		brokerName.where(a1,a2,a3,a4);
		
		//reinsurerName
		Subquery<String> reinsurerName = query.subquery(String.class); 
		Root<PersonalInfo> pi1 = reinsurerName.from(PersonalInfo.class);
	
		Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
		reinsurerName.select(cb.concat(firstName, pi1.get("lastName")));
		//maxAmend
		Subquery<Long> amend = query.subquery(Long.class); 
		Root<PersonalInfo> pis1 = amend.from(PersonalInfo.class);
		amend.select(cb.max(pis1.get("amendId")));
		Predicate c1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
		amend.where(c1);
		
		Predicate d1 = cb.equal( pi1.get("customerType"), cb.selectCase().when(cb.equal(bi.get("transType"), "IB"), "C").otherwise("R")); 
		Predicate d2 = cb.equal( pi1.get("customerId"), bi.get("cedingId"));
		Predicate d3 = cb.equal( pi1.get("branchCode"), bi.get("branchCode"));
		Predicate d4 = cb.equal( pi1.get("amendId"), amend);
		reinsurerName.where(d1,d2,d3,d4);
		
		query.multiselect(bi.get("billingNo").alias("billingNo"),
				bi.get("brokerId").alias("brokerId"),
				brokerName.alias("brokerName"),
				cb.selectCase().when(cb.equal(bi.get("status"), "Y"), "Allocated").otherwise("Reverted").alias("status"),
			//	bi.get("status").alias("status"),
				bi.get("billDate").alias("billDate"),
				reinsurerName.alias("reinsurerName"));

		Predicate n1 = cb.equal(bi.get("branchCode"), req.getBranchCode());
		Predicate n2 = cb.equal(bi.get("transType"), req.getTransType());
		query.where(n1,n2);
		TypedQuery<Tuple> res1 = em.createQuery(query);
		return res1.getResultList();
	}
	public List<Tuple> getTranContDtlsRi(GetTransContractReqRi req) {
		List<Tuple> resultList = getTranContDtlsRiForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getCurrencyId(), req.getBranchCode(),req.getProductId());
		if(Objects.nonNull(resultList))
			resultList.addAll(getTranContDtlsRiForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode(),req.getProductId()));
		else
			resultList = getTranContDtlsRiForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode(),req.getProductId());
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}

	private List<Tuple>  getTranContDtlsRiForClaim(String brokerId, String cedingId, String alloccurrencyId,
			String branchCode,String productId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		Root<TtrnClaimPaymentRi> tcpRoot = cq.from(TtrnClaimPaymentRi.class);
		String input = null;
		
//		Expression<String> funct = cb.function("FN_GET_NAME", String.class,
//     		   cb.literal("P"),
//     		  pRoot.get("productId"),
//     		 pRoot.get("branchCode"));
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		
		Expression<String> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
				.otherwise(tcpRoot.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(tcpRoot.<Double>get("allocatedTillDate"))).as(String.class);
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);
		
		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
		  .where(cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName"))
		  .where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")),
				cb.equal(subRoot.get("amendId"), aSq));
		
		cq.multiselect(tcpRoot.get("riTransactionNo").as(String.class),
				pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"),
				tcpRoot.get("inceptionDate"),
				cb.nullLiteral(Double.class).alias("NETDUE_OC"),
				exp.alias("PAID_AMOUNT"),
				cb.nullLiteral(Double.class).alias("ACC_PREMIUM"),
				tcpRoot.get("accClaim").as(String.class),
				cb.selectCase().when(cb.isNull(tcpRoot.get("checkyn")), "N").as(String.class),
				cb.literal("C").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"),
				pRoot.get("deptId"),
				pRoot.get("proposalNo")).distinct(true);
 		
		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);
		
	   Expression<Object> exp1 = cb.selectCase().when(
	    		cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
	    		tcpRoot.get("reinsurerId")).otherwise(tcpRoot.get("brokerId"));
	   
	   if("63".equalsIgnoreCase(brokerId)) {
		   input=cedingId;
		}
		else {        	
			input=brokerId;
		}
		
		pSq.select(cb.max(pSubRoot.get("amendId")))
		  .where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
			     cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
	    		 cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))), 
			     cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")),
			     cb.equal(tcdRoot.get("currency"), alloccurrencyId),
			     cb.equal(exp1, input));
		
		List<Predicate>predicates=new ArrayList<Predicate>();
		 predicates.add(cb.equal(pRoot.get("contractNo"), tcdRoot.get("contractNo")));
		 predicates.add(cb.equal(tcdRoot.get("contractNo"), tcpRoot.get("contractNo")));
		 predicates.add(cb.equal(tcdRoot.get("claimNo"), tcpRoot.get("claimNo")));
		 predicates.add(cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
		 cb.selectCase().when(cb.isNull(tcdRoot.get("layerNo")), 0).otherwise(tcdRoot.get("layerNo"))));
		 predicates.add(cb.equal(pRoot.get("sectionNo"), tcdRoot.get("subClass")));
		 predicates.add(cb.equal(pRoot.get("branchCode"), branchCode));
		 predicates.add(cb.notEqual(exp, 0));
		 predicates.add(cb.equal(pRoot.get("amendId"), pSq));
		 if(!"0".equalsIgnoreCase(productId)) {
			 predicates.add(cb.equal(pRoot.get("productId"), productId));
		 }
		 cq.where(predicates.toArray(new Predicate[0]));
		
		return em.createQuery(cq).getResultList();
	}

	private List<Tuple> getTranContDtlsRiForRsk(String brokerId, String cedingId, String alloccurrencyId,
			String branchCode,String productId) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<RskPremiumDetailsRi> rRoot = cq.from(RskPremiumDetailsRi.class);
		String input = null;

//		Expression<String> funct = cb.function("FN_GET_NAME", String.class, cb.literal("P"), pRoot.get("productId"),
//				pRoot.get("branchCode"));
		
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		Expression<String> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("netdueOc")), 0.0)
						.otherwise(rRoot.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate"))).as(String.class);

		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName")).where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")), cb.equal(subRoot.get("amendId"), aSq));

		cq.multiselect(rRoot.get("ritransactionNo").as(String.class), pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"), rRoot.get("entryDateTime"), exp.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), rRoot.get("accPremium").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.selectCase().when(cb.isNull(rRoot.get("checkyn")), "N").as(String.class),
				cb.literal("P").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				pRoot.get("deptId").as(String.class),
				pRoot.get("proposalNo").as(String.class)).distinct(true);

		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);

		Expression<Object> exp1 = cb.selectCase()
				.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
						rRoot.get("reinsurerId"))
				.otherwise(rRoot.get("brokerId"));

		if("63".equalsIgnoreCase(brokerId))
			input=cedingId;
		else        	
			input=brokerId;
		
		pSq.select(cb.max(pSubRoot.get("amendId"))).where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
				cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))),
				cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")), cb.equal(rRoot.get("currencyId"), alloccurrencyId),
				cb.equal(exp1, input));
				//cb.like(pRoot.get("contractNo"), ""), cb.like(pRoot.get("productId"), "")); // check

		List<Predicate>predicates=new ArrayList<Predicate>();
		predicates.add(cb.or(cb.isNull(rRoot.get("receiptNo")),cb.equal(rRoot.get("receiptNo"), "0")));
		predicates.add(cb.equal(rRoot.get("contractNo"), pRoot.get("contractNo")));
		predicates.add(cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
				cb.selectCase().when(cb.isNull(rRoot.get("layerNo")), 0).otherwise(rRoot.get("layerNo"))));
		predicates.add(cb.equal(pRoot.get("deptId"), rRoot.get("subClass")));
		predicates.add(cb.equal(pRoot.get("branchCode"), branchCode));
		predicates.add(cb.notEqual(exp, 0));
		predicates.add(cb.equal(pRoot.get("amendId"), pSq));
		 if(!"0".equalsIgnoreCase(productId)) {
			 predicates.add(cb.equal(pRoot.get("productId"), productId));
		 }
		 cq.where(predicates.toArray(new Predicate[0]));

		list =  em.createQuery(cq).getResultList();
		}catch(Exception e) {
			e.printStackTrace();;
		}
		return list;
	}
	@Override
	@Transactional
	public Integer updatePremiumRiDetails(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsRi> update = cb.createCriteriaUpdate(RskPremiumDetailsRi.class);
		Root<RskPremiumDetailsRi> root = update.from(RskPremiumDetailsRi.class);


		Double allocated = getAllocatedTillDate(args[3], args[4], "RskPremiumDetails") + Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated).set(root.get("loginId"), args[1]).set(root.get("branchCode"), args[2])
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		
		update.where(cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("ritransactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updatepreRiSetStatus(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsRi> update = cb.createCriteriaUpdate(RskPremiumDetailsRi.class);
		Root<RskPremiumDetailsRi> root = update.from(RskPremiumDetailsRi.class);

		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("netdueOc")), 0.0)
					.otherwise(root.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
		
		update.set(root.get("settlementStatus"), args[0])
		.where(cb.equal(exp, 0.0),
			   cb.equal(root.get("contractNo"), args[3]),  cb.equal(root.get("branchCode"), args[2]),  cb.equal(root.get("loginId"), args[1]),
			   cb.equal(root.get("ritransactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateclaimRiPymtAlloDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPaymentRi> update = cb.createCriteriaUpdate(TtrnClaimPaymentRi.class);
		Root<TtrnClaimPaymentRi> root = update.from(TtrnClaimPaymentRi.class);

		Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnClaimPayment") + Double.parseDouble(args[0]);
		
		update.set(root.get("allocatedTillDate"), allocated)
				.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[1]).set(root.get("loginId"), args[2]);

		update.where(cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("ritransactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateclaimRiSetStatus(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPaymentRi> update = cb.createCriteriaUpdate(TtrnClaimPaymentRi.class);
		Root<TtrnClaimPaymentRi> root = update.from(TtrnClaimPaymentRi.class);

		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("paidAmountOc")), 0.0)
				.otherwise(root.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(root.<Double>get("allocatedTillDate")));
		
		update.set(root.get("settlementStatus"),  args[0])
				.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[1]).set(root.get("loginId"), args[2]);

		update.where(cb.equal(exp, 0.0),cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("ritransactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	private Double getAllocatedTillDate(String input1, String input2, String classType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		List<Double> list = null;
		if (classType.equals("RskPremiumDetailsRi")) {

			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<RskPremiumDetailsRi> root = cq.from(RskPremiumDetailsRi.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("contractNo"), input1), cb.equal(root.get("ritransactionNo"), input2));
			list = em.createQuery(cq).getResultList();

		} else if (classType.equals("TtrnClaimPaymentRi")) {

			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("contractNo"), input1), cb.equal(root.get("ritransactionNo"), input2));
			list = em.createQuery(cq).getResultList();

		}
		return list != null && !list.isEmpty() ? list.get(0) : 0.0;

	}

	@Override
	@Transactional
	public Integer updateRskPremRiChkyn() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		update.set(root.get("checkyn"), "").set(root.get("accPremium"), "");
		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
}
