package com.maan.insurance.service.impl.billing;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingTransaction;
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
		
		//Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnBillingDetails") + Double.parseDouble(args[0]);
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
		Root<TtrnBillingTransaction> root = cq.from(TtrnBillingTransaction.class);

		cq.multiselect(cb.selectCase().when(cb.isNull(cb.max(root.get("billSno"))), 0)
				.otherwise(cb.sum(cb.max(root.get("billSno")), 1)));
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
		
		Subquery<Double> wht = cq.subquery(Double.class); 
		Root<RskPremiumDetailsRi> rirds = wht.from(RskPremiumDetailsRi.class);
		wht.select(cb.sum(rirds.<Double> get("premiumWhtOc")));
		wht.where(cb.equal( rirds.get("transactionNo"),rRoot.get("transactionNo")));
		
		
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		Expression<Double> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("netdueOc")), 0.0)
						.otherwise(rRoot.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate")));

		Expression<Double> exp2 = cb.diff(exp,wht);
		
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
				funct.alias("PRODUCT_NAME"), rRoot.get("entryDateTime"), exp2.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), rRoot.get("accPremium").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.selectCase().when(cb.isNull(rRoot.get("checkyn")), "N").as(String.class),
				cb.literal("P").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				pRoot.get("deptId").as(String.class),
				pRoot.get("proposalNo").as(String.class),exp.alias("AMOUNT"),cb.nullLiteral(Double.class).alias("premiumWhtOc")).distinct(true);

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

		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		
		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
				.otherwise(tcpRoot.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(tcpRoot.<Double>get("allocatedTillDate")));
		Expression<Double> exp2 = cb.diff(exp,cb.literal("0.0").as(Double.class));
		
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
				exp2.alias("PAID_AMOUNT"),
				cb.nullLiteral(Double.class).alias("ACC_PREMIUM"),
				tcpRoot.get("accClaim").as(String.class),
				cb.selectCase().when(cb.isNull(tcpRoot.get("checkyn")), "N").as(String.class),
				cb.literal("C").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"),
				pRoot.get("deptId"),
				pRoot.get("proposalNo"),exp.alias("AMOUNT"),cb.nullLiteral(Double.class).alias("premiumWhtOc")).distinct(true);
 		
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
		query.orderBy(cb.desc(bi.get("billingNo")));
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
		
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		
		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
				.otherwise(tcpRoot.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(tcpRoot.<Double>get("allocatedTillDate")));
		
		Expression<Double> exp2 = cb.diff(exp,cb.literal("0.0").as(Double.class));
		
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
				exp2.alias("PAID_AMOUNT"),
				cb.nullLiteral(Double.class).alias("ACC_PREMIUM"),
				tcpRoot.get("accClaim").as(String.class),
				cb.selectCase().when(cb.isNull(tcpRoot.get("checkyn")), "N").as(String.class),
				cb.literal("C").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"),
				pRoot.get("deptId"),
				pRoot.get("proposalNo"),exp.alias("AMOUNT"),cb.nullLiteral(Double.class).alias("premiumWhtOc")).distinct(true);
 		
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


		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		Expression<Double> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("netdueOc")), 0.0)
						.otherwise(rRoot.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate")));
		
		Expression<Double> exp2 = cb.diff(exp,cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("premiumWhtOc")), 0.0)
				.otherwise(rRoot.<Double>get("premiumWhtOc")));
		
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
				funct.alias("PRODUCT_NAME"), rRoot.get("entryDateTime"), exp2.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), rRoot.get("accPremium").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.selectCase().when(cb.isNull(rRoot.get("checkyn")), "N").as(String.class),
				cb.literal("P").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				pRoot.get("deptId").as(String.class),
				pRoot.get("proposalNo").as(String.class),exp.alias("AMOUNT"),rRoot.get("premiumWhtOc")).distinct(true);

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

		update.where(cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("riTransactionNo"), args[4]));

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

		update.where(cb.equal(exp, 0.0),cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("riTransactionNo"), args[4]));

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
			Root<TtrnClaimPaymentRi> root = cq.from(TtrnClaimPaymentRi.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("contractNo"), input1), cb.equal(root.get("ritransactionNo"), input2));
			list = em.createQuery(cq).getResultList();

		}else if (classType.equals("RskPremiumDetails")) {

			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("contractNo"), input1), cb.equal(root.get("transactionNo"), input2));
			list = em.createQuery(cq).getResultList();

		} else if (classType.equals("TtrnClaimPayment")) {

			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("contractNo"), input1), cb.equal(root.get("claimPaymentNo"), input2));
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

	@Override
	public List<Tuple> getAlloTransDtls(String receiptNo, String serialNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnBillingTransaction> root = cq.from(TtrnBillingTransaction.class);

		if (serialNo != null) {
			cq.multiselect(root.get("billSno"),
					root.get("inceptionDate"),
					root.get("reversalDate"),
					cb.selectCase().when(cb.greaterThan(root.get("reversalAmount"), 0), "C").otherwise("P"),
					cb.abs(root.get("reversalAmount")), 
					root.get("transactionNo").as(String.class), 
					root.get("contractNo").as(String.class),
					root.get("productName"),
					root.get("type"),
					cb.selectCase().when(cb.greaterThan(root.get("paidAmount"), 0), "C").otherwise("P"),
					root.get("paidAmount"),
					root.get("paidAmount"),
					root.get("currencyId"),
					root.get("status"),
					root.get("billNo"),
					cb.selectCase().when(cb.equal(root.get("adjustmentType"), "W"), "Write Off")
					.when(cb.equal(root.get("adjustmentType"), "R"), "Round Off").otherwise(""));
			
			cq.where(cb.equal(root.get("billNo"), receiptNo), cb.equal(root.get("billSno"), serialNo));
		}else {
			cq.multiselect(root.get("billSno"),
					root.get("inceptionDate"),
					root.get("reversalDate"),
					cb.selectCase().when(cb.greaterThan(root.get("reversalAmount"), 0), "C").otherwise("P"),
					cb.abs(root.get("reversalAmount")), 
					root.get("transactionNo").as(String.class), 
					root.get("contractNo").as(String.class),
					root.get("productName"),
					root.get("type"),
					cb.selectCase().when(cb.greaterThan(root.get("paidAmount"), 0), "C").otherwise("P"),
					root.get("paidAmount"),
					root.get("paidAmount"),
					root.get("currencyId"),
					root.get("status"));
			cq.where(cb.equal(root.get("billNo"), receiptNo));
		}

		cq.orderBy(cb.desc(root.get("billSno")));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getSelCurrency(String branchCode, String currencyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<CurrencyMaster> root = cq.from(CurrencyMaster.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<CurrencyMaster> subRoot = sq.from(CurrencyMaster.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("currencyId"), root.get("currencyId")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.select(root.get("shortName")).where(cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("currencyId"), currencyId), cb.equal(root.get("amendId"), sq));

		TypedQuery<String> q = em.createQuery(cq);
		String shortName = q.getSingleResult();
		return shortName;
	}

	@Transactional
	@Override
	public Integer updateAllocatedDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnBillingTransaction> update = cb.createCriteriaUpdate(TtrnBillingTransaction.class);
		Root<TtrnBillingTransaction> root = update.from(TtrnBillingTransaction.class);

		try {
			update.set(root.get("status"), args[0]).set(root.get("reversalDate"), parseDateLocal(args[1]))
					.set(root.get("reversalAmount"), new BigDecimal(args[2])).set(root.get("paidAmount"), new BigDecimal(args[3]))
					.set(root.get("loginId"), args[4]).set(root.get("branchCode"), args[5])
					.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		update.where(cb.equal(root.get("transactionNo"), args[6]), cb.equal(root.get("billSno"), args[7]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	public java.util.Date parseDateLocal(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("DD/MM/YYYY");
		return sdf1.parse(input);
	}

	@Override
	@Transactional
	public Integer updateRskPremDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		Double allocated = getAllocatedTillDate(args[4], args[5], "RskPremiumDetails") - Double.parseDouble(args[1]);
		update.set(root.get("settlementStatus"), args[0]).set(root.get("allocatedTillDate"), allocated)
				.set(root.get("loginId"), args[2]).set(root.get("branchCode"), args[3]);

		update.where(cb.equal(root.get("contractNo"), args[4]), cb.equal(root.get("transactionNo"), args[5]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getRskPremDtls(String contractNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetails> root = cq.from(RskPremiumDetails.class);
		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("netdueOc")), 0.0).otherwise(root.get("netdueOc")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("contractNo"), contractNo), cb.equal(root.get("transactionNo"), transactionNo));

		return em.createQuery(cq).getResultList();
	}

	@Override
	@Transactional
	public Integer updateClaimPymtDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);

		Double allocated = getAllocatedTillDate(args[4], args[5], "TtrnClaimPayment") - Double.parseDouble(args[1]);
		update.set(root.get("settlementStatus"), args[0]).set(root.get("allocatedTillDate"), allocated)
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[2]).set(root.get("loginId"), args[3]);

		update.where(cb.equal(root.get("contractNo"), args[4]), cb.equal(root.get("claimPaymentNo"), args[5]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getClaimPymtDtls(String contractNo, String claimPaymentNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPayment> root = cq.from(TtrnClaimPayment.class);
		cq.multiselect(
				cb.selectCase().when(cb.isNull(root.get("paidAmountOc")), 0.0).otherwise(root.get("paidAmountOc")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("contractNo"), contractNo), cb.equal(root.get("claimPaymentNo"), claimPaymentNo));

		return em.createQuery(cq).getResultList();
	}

	@Transactional
	@Override
	public Integer updatePymtRetDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnBillingInfo> update = cb.createCriteriaUpdate(TtrnBillingInfo.class);
		Root<TtrnBillingInfo> root = update.from(TtrnBillingInfo.class);

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnBillingInfo> subRoot = sq.from(TtrnBillingInfo.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("billingNo"), root.get("billingNo")));

	
		update.set(root.get("roundingAmt"), new BigDecimal(0))
				.set(root.get("utilizedTillDate"), new BigDecimal(0))
				.set(root.get("status"), "R")
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[2]).set(root.get("loginId"), args[1]);

		update.where(cb.equal(root.get("billingNo"), args[3]), cb.equal(root.get("currencyId"), args[4]),
				cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getPymtRetDtls(String receiptSlNo, String currencyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnBillingInfo> root = cq.from(TtrnBillingInfo.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnBillingInfo> subRoot = sq.from(TtrnBillingInfo.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("billingNo"), root.get("billingNo")));

		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("amount")), 0.0).otherwise(root.get("amount")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("receiptSlNo"), receiptSlNo), cb.equal(root.get("currencyId"), currencyId),
				cb.equal(root.get("amendId"), sq));

		return em.createQuery(cq).getResultList();
	}

	@Override
	@Transactional
	public Integer updateRskPremRiDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetailsRi> update = cb.createCriteriaUpdate(RskPremiumDetailsRi.class);
		Root<RskPremiumDetailsRi> root = update.from(RskPremiumDetailsRi.class);

		Double allocated = getAllocatedTillDate(args[4], args[5], "RskPremiumDetailsRi") - Double.parseDouble(args[1]);
		update.set(root.get("settlementStatus"), args[0]).set(root.get("allocatedTillDate"), allocated)
				.set(root.get("loginId"), args[2]).set(root.get("branchCode"), args[3]);

		update.where(cb.equal(root.get("contractNo"), args[4]), cb.equal(root.get("ritransactionNo"), args[5]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getRskPremRiDtls(String contractNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<RskPremiumDetailsRi> root = cq.from(RskPremiumDetailsRi.class);
		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("netdueOc")), 0.0).otherwise(root.get("netdueOc")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("contractNo"), contractNo), cb.equal(root.get("ritransactionNo"), transactionNo));

		return em.createQuery(cq).getResultList();
	}

	@Override
	@Transactional
	public Integer updateClaimPymtRiDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPaymentRi> update = cb.createCriteriaUpdate(TtrnClaimPaymentRi.class);
		Root<TtrnClaimPaymentRi> root = update.from(TtrnClaimPaymentRi.class);

		Double allocated = getAllocatedTillDate(args[4], args[5], "TtrnClaimPaymentRi") - Double.parseDouble(args[1]);
		update.set(root.get("settlementStatus"), args[0]).set(root.get("allocatedTillDate"), allocated)
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[2]).set(root.get("loginId"), args[3]);

		update.where(cb.equal(root.get("contractNo"), args[4]), cb.equal(root.get("riTransactionNo"), args[5]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getClaimPymtRiDtls(String contractNo, String claimPaymentNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnClaimPaymentRi> root = cq.from(TtrnClaimPaymentRi.class);
		cq.multiselect(
				cb.selectCase().when(cb.isNull(root.get("paidAmountOc")), 0.0).otherwise(root.get("paidAmountOc")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("contractNo"), contractNo), cb.equal(root.get("riTransactionNo"), claimPaymentNo));

		return em.createQuery(cq).getResultList();
	}

	
}
