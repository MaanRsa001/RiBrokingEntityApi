package com.maan.insurance.service.impl.adjustment;

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

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.validation.Formatters;

@Repository
public class AdjustmentCustomRepositoryImple implements AdjustmentCustomRepository{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Tuple> adjutmentList(String[] obj, GetAdjustmentListReq bean) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjutment.list
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> a = query.from(PositionMaster.class);
			Root<RskPremiumDetails> b = query.from(RskPremiumDetails.class);
			
			//productName
			Subquery<String> productName = query.subquery(String.class); 
			Root<TmasProductMaster> pis = productName.from(TmasProductMaster.class);
			productName.select(pis.get("tmasProductName"));
			Predicate b1 = cb.equal( pis.get("tmasProductId"), a.get("productId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), a.get("branchCode"));
			productName.where(b1,b2);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingCompanyId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingCompanyId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), b.get("currencyId"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					b.get("transactionNo").alias("TRANSACTION_NO"),
					a.get("contractNo").alias("CONTRACT_NO"),
					a.get("layerNo").alias("LAYER_NO"),
					productName.alias("PRODUCT_NAME"),
					b.get("transactionMonthYear").alias("ADATE"),
					cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					b.get("accPremium").alias("ACC_PREMIUM"),
					cb.literal("null").alias("ACC_CLAIM"),
					cb.coalesce(b.get("checkyn"), "N").alias("CHECKYN"),
					cb.literal("Premium").alias("BUSINESS_TYPE"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					b.get("currencyId").alias("CURRENCY_ID"),
					a.get("proposalNo").alias("PROPOSAL_NO"),
					a.get("deptId").alias("DEPT_ID")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("contractNo"), a.get("contractNo")));
			predict.add(cb.equal(cb.coalesce(a.get("layerNo"), 0) , cb.coalesce(pms.get("layerNo"), 0)));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(a.get("contractNo")));
			orderList.add(cb.asc(b.get("transactionNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.isNull(b.get("receiptNo")));
			predicates.add(cb.equal(a.get("contractNo"), b.get("contractNo")));
			predicates.add(cb.equal(cb.coalesce(a.get("layerNo"), 0), cb.coalesce(b.get("layerNo"), 0)));
			predicates.add(cb.equal(a.get("deptId"), b.get("subClass")));
			predicates.add(cb.equal(a.get("branchCode"), obj[0]));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), 0));
			predicates.add(cb.equal(a.get("amendId"), amend));
		
			predicates.add(cb.equal( b.get("currencyId"), obj[1]));
			if("63".equalsIgnoreCase(obj[2]))
				predicates.add(cb.equal( a.get("cedingCompanyId"), obj[3]));
			else
				predicates.add(cb.equal( a.get("brokerId"), obj[3]));
			
		
			
			if (bean.getAmountType().equals("1")) {
				predicates.add(cb.equal(cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), bean.getAmount()));
			} else if (bean.getAmountType().equals("2")) {
				predicates.add(cb.lessThanOrEqualTo(cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			} else if (bean.getAmountType().equals("3")) {
				predicates.add(cb.greaterThanOrEqualTo(cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			}
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> adjutmentListUnion(String[] obj, GetAdjustmentListReq bean) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjutment.list
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> a = query.from(PositionMaster.class);
			Root<TtrnClaimDetails> c = query.from(TtrnClaimDetails.class);
			Root<TtrnClaimPayment> cp = query.from(TtrnClaimPayment.class);
			
			//productName
			Subquery<String> productName = query.subquery(String.class); 
			Root<TmasProductMaster> pis = productName.from(TmasProductMaster.class);
			productName.select(pis.get("tmasProductName"));
			Predicate b1 = cb.equal( pis.get("tmasProductId"), a.get("productId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), a.get("branchCode"));
			productName.where(b1,b2);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingCompanyId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingCompanyId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), c.get("currency"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					cp.get("claimPaymentNo").alias("TRANSACTION_NO"),
					a.get("contractNo").alias("CONTRACT_NO"),
					a.get("layerNo").alias("LAYER_NO"),
					productName.alias("PRODUCT_NAME"),
					cp.get("inceptionDate").alias("ADATE"),
					cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					cb.literal("null").alias("ACC_PREMIUM"),
					cp.get("accClaim").alias("ACC_CLAIM"),
					cb.coalesce(cp.get("checkyn"), "N").alias("CHECKYN"),
					cb.literal("Claim").alias("BUSINESS_TYPE"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					c.get("currency").alias("CURRENCY_ID"),
					a.get("proposalNo").alias("PROPOSAL_NO"),
					a.get("deptId").alias("DEPT_ID")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("contractNo"), a.get("contractNo")));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(a.get("contractNo")));
			orderList.add(cb.asc(cp.get("claimPaymentNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(a.get("contractNo"), c.get("contractNo")));
			predicates.add(cb.equal(cp.get("contractNo"), c.get("contractNo")));
			predicates.add(cb.equal(cp.get("claimNo"), c.get("claimNo")));
			predicates.add(cb.equal(cb.coalesce(a.get("layerNo"), 0), cb.coalesce(c.get("layerNo"), 0)));
			predicates.add(cb.equal(a.get("deptId"), c.get("subClass")));
			predicates.add(cb.equal(a.get("branchCode"), obj[0]));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)), 0));
			predicates.add(cb.equal(a.get("amendId"), amend));
			predicates.add(cb.equal( c.get("currency"), obj[1]));
		
			if("63".equalsIgnoreCase(obj[2]))
				predicates.add(cb.equal( a.get("cedingCompanyId"), obj[3]));
			else
				predicates.add(cb.equal( a.get("brokerId"), obj[3]));
			
			if (bean.getAmountType().equals("1")) {
				predicates.add(cb.equal(cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)), bean.getAmount()));
			} else if (bean.getAmountType().equals("2")) {
				predicates.add(cb.lessThanOrEqualTo(cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			} else if (bean.getAmountType().equals("3")) {
				predicates.add(cb.greaterThanOrEqualTo(cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			}
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> adjustmentListIndTransactionCP(String branchCode, String transactionNo) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjustment.list.ind.transactionCP
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> a = query.from(PositionMaster.class);
			Root<RskPremiumDetails> b = query.from(RskPremiumDetails.class);
			
			//productName
			Subquery<String> productName = query.subquery(String.class); 
			Root<TmasProductMaster> pis = productName.from(TmasProductMaster.class);
			productName.select(pis.get("tmasProductName"));
			Predicate b1 = cb.equal( pis.get("tmasProductId"), a.get("productId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), a.get("branchCode"));
			productName.where(b1,b2);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingCompanyId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingCompanyId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), b.get("currencyId"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					b.get("transactionNo").alias("TRANSACTION_NO"),
					a.get("contractNo").alias("CONTRACT_NO"),
					a.get("layerNo").alias("LAYER_NO"),
					productName.alias("PRODUCT_NAME"),
					b.get("entryDateTime").alias("ADATE"),
					cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					b.get("accPremium").alias("ACC_PREMIUM"),
					cb.literal("null").alias("ACC_CLAIM"),
					cb.coalesce(b.get("checkyn"), "N").alias("CHECKYN"),
					cb.literal("Premium").alias("BUSINESS_TYPE"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					b.get("currencyId").alias("CURRENCY_ID"),
					a.get("proposalNo").alias("PROPOSAL_NO"),
					a.get("deptId").alias("DEPT_ID")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("contractNo"), a.get("contractNo")));
			predict.add(cb.equal(cb.coalesce(a.get("layerNo"), 0) , cb.coalesce(pms.get("layerNo"), 0)));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(a.get("contractNo")));
			orderList.add(cb.asc(b.get("transactionNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.isNull(b.get("receiptNo")));
			predicates.add(cb.equal(a.get("contractNo"), b.get("contractNo")));
			predicates.add(cb.equal(cb.coalesce(a.get("layerNo"), 0), cb.coalesce(b.get("layerNo"), 0)));
		//	predicates.add(cb.equal(a.get("deptId"), b.get("subClass")));
			predicates.add(cb.equal(a.get("branchCode"), branchCode));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(b.get("netdueOc"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), 0));
			predicates.add(cb.equal(a.get("amendId"), amend));
			predicates.add(b.get("transactionNo").in(transactionNo));
		
			
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> adjustmentListIndTransactionCPUnion(String branchCode, String transactionNo) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjustment.list.ind.transactionCP
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> a = query.from(PositionMaster.class);
			Root<TtrnClaimDetails> c = query.from(TtrnClaimDetails.class);
			Root<TtrnClaimPayment> cp = query.from(TtrnClaimPayment.class);
			
			//productName
			Subquery<String> productName = query.subquery(String.class); 
			Root<TmasProductMaster> pis = productName.from(TmasProductMaster.class);
			productName.select(pis.get("tmasProductName"));
			Predicate b1 = cb.equal( pis.get("tmasProductId"), a.get("productId"));
			Predicate b2 = cb.equal( pis.get("branchCode"), a.get("branchCode"));
			productName.where(b1,b2);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingCompanyId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingCompanyId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), c.get("currency"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					cp.get("claimPaymentNo").alias("TRANSACTION_NO"),
					a.get("contractNo").alias("CONTRACT_NO"),
					a.get("layerNo").alias("LAYER_NO"),
					productName.alias("PRODUCT_NAME"),
					cp.get("inceptionDate").alias("ADATE"),
					cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					cb.literal("null").alias("ACC_PREMIUM"),
					cp.get("accClaim").alias("ACC_CLAIM"),
					cb.coalesce(cp.get("checkyn"), "N").alias("CHECKYN"),
					cb.literal("Claim").alias("BUSINESS_TYPE"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					c.get("currency").alias("CURRENCY_ID"),
					a.get("proposalNo").alias("PROPOSAL_NO"),
					a.get("deptId").alias("DEPT_ID")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("contractNo"), a.get("contractNo")));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(a.get("contractNo")));
			orderList.add(cb.asc(cp.get("claimPaymentNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(a.get("contractNo"), c.get("contractNo")));
			predicates.add(cb.equal(cp.get("contractNo"), c.get("contractNo")));
			predicates.add(cb.equal(cp.get("claimNo"), c.get("claimNo")));
			predicates.add(cb.equal(cb.coalesce(a.get("layerNo"), 0), cb.coalesce(c.get("layerNo"), 0)));
		//	predicates.add(cb.equal(a.get("deptId"), c.get("subClass")));
			predicates.add(cb.equal(a.get("branchCode"),branchCode));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(cp.get("paidAmountOc"), 0), cb.coalesce(cp.get("allocatedTillDate"), 0)), 0));
			predicates.add(cb.equal(a.get("amendId"), amend));
			predicates.add( cp.get("claimPaymentNo").in(transactionNo));
		
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> adjustmentPayrecList(String[] obj,GetAdjustmentListReq bean) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjustment.payrec.list
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnPaymentReceipt> a = query.from(TtrnPaymentReceipt.class);
			Root<TtrnPaymentReceiptDetails> b = query.from(TtrnPaymentReceiptDetails.class);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), b.get("currencyId"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					b.get("receiptSlNo").alias("TRANSACTION_NO"),
					b.get("transDate").alias("ADATE"),
					cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					b.get("currencyId").alias("CURRENCY_ID"),
					cb.selectCase().when(cb.equal(a.get("transType"), "RT"), "Receipt").otherwise("Payment").alias("BUSINESS_TYPE")
					).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnPaymentReceiptDetails> pms = amend.from(TtrnPaymentReceiptDetails.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("receiptSlNo"), b.get("receiptSlNo")));
			predict.add(cb.equal( pms.get("receiptNo"), b.get("receiptNo")));
			predict.add(cb.equal( pms.get("currencyId"), b.get("currencyId")));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("receiptSlNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(a.get("paymentReceiptNo"), b.get("receiptSlNo")));
			predicates.add(cb.equal(a.get("branchCode"), obj[0]));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), 0));
			predicates.add(cb.equal(b.get("currencyId"), obj[1]));
			if("63".equalsIgnoreCase(obj[2]))
				predicates.add(cb.equal( a.get("cedingId"), obj[3]));
			else
				predicates.add(cb.equal( a.get("brokerId"), obj[3]));
			
			predicates.add(cb.equal(a.get("amendId"), amend));
			if (bean.getAmountType().equals("1")) {
				predicates.add(cb.equal(cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), bean.getAmount()));
			} else if (bean.getAmountType().equals("2")) {	
				predicates.add(cb.lessThanOrEqualTo(cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			} else if (bean.getAmountType().equals("3")) {
				predicates.add(cb.greaterThanOrEqualTo(cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), Integer.valueOf(bean.getAmount())));
			}
		
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> adjustmentListIndTransactionRP(String branchCode, String transactionNo) {
		List<Tuple> list=new ArrayList<>();
		try {
			//adjustment.list.ind.transactionRP
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnPaymentReceipt> a = query.from(TtrnPaymentReceipt.class);
			Root<TtrnPaymentReceiptDetails> b = query.from(TtrnPaymentReceiptDetails.class);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> i = amen.from(PersonalInfo.class);
			amen.select(cb.max(i.get("amendId")));
			Predicate q1 = cb.equal( i.get("customerId"), a.get("cedingId"));
			Predicate q2 = cb.equal( i.get("branchCode"), a.get("branchCode"));
			amen.where(q1,q2);
			Predicate d1 = cb.equal( pi.get("customerId"), a.get("cedingId"));
			Predicate d2 = cb.equal( pi.get("branchCode"), a.get("branchCode"));
			Predicate d3 = cb.equal( pi.get("amendId"), amen);
			companyName.where(d1,d2,d3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"),  a.get("brokerId"));
			Predicate s2 = cb.equal( pibs.get("branchCode"),  a.get("branchCode"));
			amenb.where(s1,s2);
			Predicate a1 = cb.equal( pib.get("customerId"), a.get("brokerId"));
			Predicate a2 = cb.equal( pib.get("branchCode"), a.get("branchCode"));
			Predicate a3 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(a1,a2,a3);
			
			//currencyName
			Subquery<String> currencyName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = currencyName.from(CurrencyMaster.class);
      		currencyName.select(cm.get("currencyName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate c1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate c2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(c1,c2);
      		Predicate x1 = cb.equal(cm.get("currencyId"), b.get("currencyId"));
      		Predicate x2 = cb.equal( cm.get("branchCode"), a.get("branchCode"));
      		Predicate x3 = cb.equal( cm.get("amendId"), amendA);
      		currencyName.where(x3,x1,x2);

			query.multiselect(
					b.get("receiptSlNo").alias("TRANSACTION_NO"),
					b.get("transDate").alias("ADATE"),
					cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)).alias("PENDING_AMOUNT"),
					companyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					currencyName.alias("CURRENCY_NAME"),
					b.get("currencyId").alias("CURRENCY_ID"),
					cb.selectCase().when(cb.equal(a.get("transType"), "RT"), "Receipt").otherwise("Payment").alias("BUSINESS_TYPE")
					).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnPaymentReceiptDetails> pms = amend.from(TtrnPaymentReceiptDetails.class);
			amend.select(cb.max(pms.get("amendId")));
			List<Predicate> predict = new ArrayList<Predicate>();
			predict.add(cb.equal( pms.get("receiptSlNo"), b.get("receiptSlNo")));
			predict.add(cb.equal( pms.get("receiptNo"), b.get("receiptNo")));
			predict.add(cb.equal( pms.get("currencyId"), b.get("currencyId")));
			amend.where(predict.toArray(new Predicate[0]));
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(b.get("receiptSlNo")));
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(a.get("paymentReceiptNo"), b.get("receiptSlNo")));
			predicates.add(cb.equal(a.get("branchCode"), branchCode));
			predicates.add(cb.notEqual(cb.diff(cb.coalesce(b.get("amount"), 0), cb.coalesce(b.get("allocatedTillDate"), 0)), 0));
			predicates.add(b.get("receiptSlNo").in(transactionNo));
			predicates.add(cb.notEqual(b.get("currencyId"), 0));
			predicates.add(cb.equal(b.get("amendId"), amend));
		
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
