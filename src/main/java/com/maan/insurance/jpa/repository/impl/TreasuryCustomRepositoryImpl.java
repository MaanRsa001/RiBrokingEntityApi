package com.maan.insurance.jpa.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.treasury.BankMaster;
import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.jpa.entity.treasury.TtrnRetroSoa;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.JournelFormat;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.req.CurrecyAmountReq;
import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetReceiptAllocateReq;
import com.maan.insurance.model.req.GetReceiptGenerationReq;
import com.maan.insurance.model.req.GetReceiptReversalListReq;
import com.maan.insurance.model.req.GetRetroallocateTransactionReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.GetTreasuryJournalViewReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.ReceiptTreasuryReq;
import com.maan.insurance.model.req.ReceiptViewListReq;
import com.maan.insurance.model.req.ReciptListReq;
import com.maan.insurance.model.req.RetroTransReq;
import com.maan.insurance.model.req.ReverseViewReq;
import com.maan.insurance.model.req.SecondPageInfoReq;
import com.maan.insurance.service.impl.QueryImplemention;

@Repository
public class TreasuryCustomRepositoryImpl implements TreasuryCustomRepository {

	@Autowired
	EntityManager em;
	@Autowired
	private QueryImplemention queryImpl;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	// getSecondPageInfo(SecondPageInfoReq req) -- STARTS
	@Override
	public List<TtrnPaymentReceiptDetails> getSecondPageDtls(SecondPageInfoReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnPaymentReceiptDetails> cq = cb.createQuery(TtrnPaymentReceiptDetails.class);
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

		cq.multiselect(root.get("currencyId"), root.get("exchangeRate"), root.get("amount"), root.get("totAmt"),
				root.get("receiptNo"), root.get("settledExcrate"), root.get("convertedReccur"))
				.where(cb.equal(root.get("receiptSlNo"), req.getSerialno()), cb.equal(root.get("amendId"), sq));
		cq.orderBy(cb.asc(root.get("receiptNo")));

		TypedQuery<TtrnPaymentReceiptDetails> q = em.createQuery(cq);
		List<TtrnPaymentReceiptDetails> receiptDetails = q.getResultList();
		return receiptDetails;
	}

	@Override
	public Integer getDiffAmt(Long paymentReceiptNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnPaymentReceipt> root = cq.from(TtrnPaymentReceipt.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("diffAmt")), 0).otherwise(root.get("diffAmt"))).where(
				cb.equal(root.get("paymentReceiptNo"), paymentReceiptNo), cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("amendId"), sq));

		TypedQuery<Integer> q = em.createQuery(cq);
		List<Integer> receiptList = q.getResultList();
		return receiptList != null && !receiptList.isEmpty() ? receiptList.get(0) : null;
	}
	// getSecondPageInfo(SecondPageInfoReq req) -- ENDS

	@Override
	public List<Tuple> currencyAmt(CurrecyAmountReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceiptDetails> tRoot = cq.from(TtrnPaymentReceiptDetails.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);

		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> tSubRoot = tSq.from(TtrnPaymentReceiptDetails.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(cb.equal(tSubRoot.get("receiptNo"), tRoot.get("receiptNo")),
				cb.equal(tSubRoot.get("receiptSlNo"), tRoot.get("receiptSlNo")));

		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		Expression<Object> exp = cb.selectCase().when(cb.isNull(tRoot.get("allocatedTillDate")), 0)
				.otherwise(tRoot.get("allocatedTillDate"));
		cq.multiselect(tRoot.get("amount"), exp, cRoot.get("shortName")).where(
				cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
				cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(tRoot.get("amendId"), tSq),
				cb.equal(cRoot.get("amendId"), cSq), cb.equal(tRoot.get("receiptSlNo"), req.getSerialNo()),
				cb.equal(tRoot.get("currencyId"), req.getCurrValu()));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;

	}

	// Validatethree(String branchCode, String accountDate) -- STARTS
	@Override
	public List<TmasOpenPeriod> getOpenPeriodDate(String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TmasOpenPeriod> cq = cb.createQuery(TmasOpenPeriod.class);
		Root<TmasOpenPeriod> root = cq.from(TmasOpenPeriod.class);

		cq.multiselect(root.get("startDate"), root.get("endDate")).where(cb.equal(root.get("status"), "Y"),
				cb.equal(root.get("branchCode"), branchCode));

		TypedQuery<TmasOpenPeriod> q = em.createQuery(cq);
		List<TmasOpenPeriod> receiptList = q.getResultList();
		return receiptList;
	}
	// Validatethree(String branchCode, String accountDate) -- ENDS

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

	// Method to get sub query value for payment.update.payRetDtls
	@Override
	public Integer getMaxAmendIdSq(Integer receiptNo, Integer receiptSlNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);
		cq.select(cb.max(root.get("amendId"))).where(cb.equal(root.get("receiptNo"), receiptNo),
				cb.equal(root.get("receiptSlNo"), receiptSlNo));

		TypedQuery<Integer> q = em.createQuery(cq);
		int receiptList = q.getFirstResult();
		return receiptList + 1;
	}

	@Override
	@Transactional
	public Integer updateDiffAmt(PaymentRecieptReq req) {
		double diffAmt = Double
				.parseDouble(StringUtils.isEmpty(req.getTxtDiffPer()) ? "0" : req.getTxtDiffPer().replaceAll(",", ""));
		double diffAmt1 = Double.parseDouble(StringUtils.isEmpty(req.getConvertedDiffAmount()) ? "0"
				: req.getConvertedDiffAmount().replaceAll(",", ""));

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnPaymentReceipt> update = cb.createCriteriaUpdate(TtrnPaymentReceipt.class);
		Root<TtrnPaymentReceipt> root = update.from(TtrnPaymentReceipt.class);

		update.set(root.get("diffAmt"), diffAmt).set(root.get("convertedDiffAmt"), diffAmt1)
				.set(root.get("loginId"), req.getLoginId()).set(root.get("branchCode"), req.getBranchCode())
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		update.where(cb.equal(root.get("paymentReceiptNo"), req.getSerialno()),
				cb.equal(root.get("branchCode"), req.getBranchCode()), cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateReversalPayment(PaymentRecieptReq req, String type) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnPaymentReceipt> update = cb.createCriteriaUpdate(TtrnPaymentReceipt.class);
		Root<TtrnPaymentReceipt> root = update.from(TtrnPaymentReceipt.class);
		update.set(root.get("status"), "R").set(root.get("revtransalType"), type)
				.set(root.get("reversalLoginId"), req.getLoginId()).set(root.get("loginId"), req.getLoginId())
				.set(root.get("branchCode"), req.getBranchCode())
				.set(root.get("reversalDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));

		if (type.equals("RT"))
			update.set(root.get("reversalTransNo"), Long.parseLong(req.getRevPayReceiptNo()));
		else
			update.set(root.get("reversalTransNo"), Long.parseLong(req.getSerialno()));

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		if (type.equals("RT"))
			update.where(cb.equal(root.get("paymentReceiptNo"), Long.parseLong(req.getSerialno())),
					cb.equal(root.get("branchCode"), req.getBranchCode()), cb.equal(root.get("amendId"), sq));
		else
			update.where(cb.equal(root.get("paymentReceiptNo"), Long.parseLong(req.getRevPayReceiptNo())),
					cb.equal(root.get("branchCode"), req.getBranchCode()), cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateReversalPaymentDetails(PaymentRecieptReq req, boolean isSerial) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnPaymentReceiptDetails> update = cb.createCriteriaUpdate(TtrnPaymentReceiptDetails.class);
		Root<TtrnPaymentReceiptDetails> root = update.from(TtrnPaymentReceiptDetails.class);
		update.set(root.get("status"), "R")
				// .set(root.get("ALLOCATED_TILL_DATE"), root.get("AMOUNT"))
				.set(root.get("loginId"), req.getLoginId()).set(root.get("branchCode"), req.getBranchCode())
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

		if (isSerial)
			update.where(cb.equal(root.get("receiptSlNo"), req.getSerialno()), cb.equal(root.get("amendId"), sq));
		else
			update.where(cb.equal(root.get("receiptSlNo"), req.getRevPayReceiptNo()),
					cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	// getAllocatedStatus(AllocatedStatusReq req) -- STARTS
	// payment.select.getPymtRetStatus
	@Override
	public List<Tuple> getPymtRetStatus(String branchCode, String payRecNo, String currencyId) {
		List<Tuple> list =null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnPaymentReceiptDetails> a = query.from(TtrnPaymentReceiptDetails.class);
			Root<CurrencyMaster> b = query.from(CurrencyMaster.class);
			Root<TtrnPaymentReceipt> tat = query.from(TtrnPaymentReceipt.class);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			Predicate a1 = cb.equal( pi.get("customerId"), tat.get("cedingId"));
			Predicate a2 = cb.equal( pi.get("branchCode"), tat.get("branchCode"));
			companyName.where(a1,a2);
			//bankName
			Subquery<String> bankName = query.subquery(String.class); 
			Root<BankMaster> bm = bankName.from(BankMaster.class);
			bankName.select(bm.get("bankName"));
			Predicate b1 = cb.equal( bm.get("bankId"), tat.get("receiptBank"));
			Predicate b2 = cb.equal( bm.get("branchCode"), tat.get("branchCode"));
			bankName.where(b1,b2);
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
			brokerName.select(pi1.get("firstName"));
			Predicate c1 = cb.equal( pi1.get("customerId"), tat.get("brokerId"));
			Predicate c2 = cb.equal( pi1.get("branchCode"), tat.get("branchCode"));
			brokerName.where(c1,c2);
		
			query.multiselect(b.get("shortName").alias("CURRENCY_NAME"),
					a.get("amount").alias("ALLOCATED"),
					cb.coalesce(a.get("allocatedTillDate"), BigDecimal.ZERO).alias("UTILIZED"),	
					cb.diff(a.get("amount"), cb.coalesce(a.get("allocatedTillDate"), BigDecimal.ZERO)).alias("NOTUTILIZED"),
				
					cb.selectCase().when(cb.equal(cb.diff(a.get("amount"), cb.coalesce(a.get("allocatedTillDate"), BigDecimal.ZERO)), BigDecimal.ZERO),"Fully Allocated")
					.otherwise(cb.selectCase().when(cb.greaterThan(cb.coalesce(a.get("allocatedTillDate"), BigDecimal.ZERO),BigDecimal.ZERO),"Partially Allocated")
					.otherwise(cb.selectCase().when(cb.equal(cb.coalesce(a.get("allocatedTillDate"), BigDecimal.ZERO),BigDecimal.ZERO),"Pending")
					.otherwise(""))).alias("STATUS"),
					
					tat.get("transDate").alias("PAYMANT_DATE"),
					companyName.alias("Ceding_company"),
					bankName.alias("Bank_name"),
					brokerName.alias("broker_name"));
	
			Subquery<Long> amendA = query.subquery(Long.class); 
			Root<TtrnPaymentReceiptDetails> rds = amendA.from(TtrnPaymentReceiptDetails.class);
			amendA.select(cb.max(rds.get("amendId")));
			Predicate d1 = cb.equal( rds.get("receiptNo"), a.get("receiptNo"));
			Predicate d2 = cb.equal( rds.get("receiptSlNo"), a.get("receiptSlNo"));
			amendA.where(d1,d2);
			
			Subquery<Long> amendB = query.subquery(Long.class); 
			Root<CurrencyMaster> cm = amendB.from(CurrencyMaster.class);
			amendB.select(cb.max(cm.get("amendId")));
			Predicate e1 = cb.equal( cm.get("currencyId"), b.get("currencyId"));
			Predicate e2 = cb.equal( cm.get("branchCode"), b.get("branchCode"));
			amendB.where(e1,e2);
			
			Subquery<Long> amendTat = query.subquery(Long.class); 
			Root<TtrnPaymentReceipt> pr = amendTat.from(TtrnPaymentReceipt.class);
			amendTat.select(cb.max(pr.get("amendId")));
			Predicate f1 = cb.equal( pr.get("paymentReceiptNo"), a.get("receiptSlNo"));
			amendTat.where(f1);
	
			Predicate n1 = cb.equal(b.get("currencyId"), a.get("currencyId"));
			Predicate n2 = cb.equal(b.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(a.get("receiptSlNo"), tat.get("paymentReceiptNo"));
			Predicate n4 = cb.equal(a.get("amendId"), amendA);
			Predicate n5 = cb.equal(b.get("amendId"), amendB);
			Predicate n6 = cb.equal(tat.get("amendId"), amendTat);
			Predicate n7 = cb.equal(a.get("receiptSlNo"), payRecNo);
			
			if(StringUtils.isNotBlank(currencyId)){
				Predicate n8 = cb.equal(a.get("currencyId"), currencyId);
				query.where(n1,n2,n3,n4,n5,n6,n7,n8);
			}else {
			query.where(n1,n2,n3,n4,n5,n6,n7);
			}
			
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
		
		}catch(Exception e) {
			e.printStackTrace();
			}
		return list;

	}

	// common.select.getCompName
	@Override
	public String getCompName(String branchCode, String brokerId, String customerType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<PersonalInfo> root = cq.from(PersonalInfo.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("branchCode"), root.get("branchCode")),
				cb.equal(subRoot.get("customerId"), root.get("customerId")),
				cb.equal(subRoot.get("customerType"), root.get("customerType")));

		cq.multiselect(cb.selectCase().when(cb.equal(root.get("customerType"), "C"), root.get("companyName"))
				.otherwise(cb.concat(cb.concat(root.get("firstName"), " "), root.get("lastName"))));

		cq.where(cb.equal(root.get("branchCode"), branchCode), cb.equal(root.get("customerId"), brokerId),
				cb.equal(root.get("customerType"), customerType), cb.equal(root.get("amendId"), sq));

		TypedQuery<String> q = em.createQuery(cq);
		List<String> receiptList = q.getResultList();
		return receiptList != null && !receiptList.isEmpty() ? receiptList.get(0) : null;
	}
	// getAllocatedStatus(AllocatedStatusReq req) -- ENDS

	// getReceiptEdit(String paymentReceiptNo, String branchCode) -- STARTS
	@Override
	public List<TtrnPaymentReceipt> getRetDtls(String paymentReceiptNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnPaymentReceipt> cq = cb.createQuery(TtrnPaymentReceipt.class);
		Root<TtrnPaymentReceipt> root = cq.from(TtrnPaymentReceipt.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.select(root).where(cb.equal(root.get("paymentReceiptNo"), paymentReceiptNo),
				cb.equal(root.get("branchCode"), branchCode), cb.equal(root.get("amendId"), sq));

		TypedQuery<TtrnPaymentReceipt> q = em.createQuery(cq);
		List<TtrnPaymentReceipt> receiptList = q.getResultList();
		return receiptList;

	}
	// getReceiptEdit(String paymentReceiptNo, String branchCode) -- ENDS

	// reverseView(ReverseViewReq req) -- STARTS
	@Override
	public List<TtrnAllocatedTransaction> getAllTranDtls(ReverseViewReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TtrnAllocatedTransaction> cq = cb.createQuery(TtrnAllocatedTransaction.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		cq.select(root).where(cb.equal(root.get("receiptNo"), req.getSerialNo())).orderBy(cb.desc(root.get("sNo")));

		TypedQuery<TtrnAllocatedTransaction> q = em.createQuery(cq);
		List<TtrnAllocatedTransaction> list = q.getResultList();
		return list;
	}

	@Override
	public String getSelCurrency(String branchCode, String currecnyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<CurrencyMaster> root = cq.from(CurrencyMaster.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<CurrencyMaster> subRoot = sq.from(CurrencyMaster.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("currencyId"), root.get("currencyId")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.select(root.get("shortName")).where(cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("currencyId"), currecnyId), cb.equal(root.get("amendId"), sq));

		TypedQuery<String> q = em.createQuery(cq);
		String shortName = q.getSingleResult();
		return shortName;
	}

	@Override
	public Integer getAlloAmt(Integer receiptSlNo, String currencyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

		Expression<Object> allocatedExpression = cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0)
				.otherwise(root.get("allocatedTillDate"));

		cq.multiselect(root.get("amount"), allocatedExpression).where(cb.equal(root.get("receiptSlNo"), receiptSlNo),
				cb.equal(root.get("currencyId"), currencyId), cb.equal(root.get("amendId"), sq));

		List<Tuple> tupleResult = em.createQuery(cq).getResultList();
		return tupleResult != null && !tupleResult.isEmpty()
				? ((Integer) tupleResult.get(0).get(0) - (Integer) tupleResult.get(0).get(1))
				: null;
	}

	// reverseView(ReverseViewReq req) -- ENDS

	// Inprogress
	@Override
	public List<Tuple> getReversaltLists(GetReceiptReversalListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// before union all
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> rRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> personalRoot1 = cq.from(PersonalInfo.class);
		Root<PersonalInfo> personalRoot2 = cq.from(PersonalInfo.class);

		Subquery<Integer> rSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> rSubRoot = rSq.from(TtrnPaymentReceipt.class);

		rSq.select(cb.max(rSubRoot.get("amendId"))).where(
				cb.equal(rSubRoot.get("paymentReceiptNo"), rRoot.get("paymentReceiptNo")),
				cb.equal(rSubRoot.get("branchCode"), rRoot.get("branchCode")));

		Subquery<Integer> pSq1 = cq.subquery(Integer.class);
		Root<PersonalInfo> pSubRoot1 = pSq1.from(PersonalInfo.class);

		pSq1.select(cb.max(pSubRoot1.get("amendId"))).where(
				cb.equal(pSubRoot1.get("customerId"), personalRoot1.get("customerId")),
				cb.equal(pSubRoot1.get("branchCode"), personalRoot1.get("branchCode")),
				cb.equal(pSubRoot1.get("customerType"), personalRoot1.get("customerType")));

		Subquery<Integer> pSq2 = cq.subquery(Integer.class);
		Root<PersonalInfo> pSubRoot2 = pSq2.from(PersonalInfo.class);

		pSq2.select(cb.max(pSubRoot2.get("amendId"))).where(
				cb.equal(pSubRoot2.get("customerId"), personalRoot2.get("customerId")),
				cb.equal(pSubRoot2.get("branchCode"), personalRoot2.get("branchCode")),
				cb.equal(pSubRoot2.get("customerType"), personalRoot2.get("customerType")));

		Expression<String> nameExpression = cb.concat(cb.concat(personalRoot2.get("firstName"), " "),
				personalRoot2.get("lastName"));

		cq.multiselect(rRoot.get("paymentReceiptNo"), personalRoot1.get("companyName"), nameExpression.alias("BROKER"),
				rRoot.get("paymentResponse"), rRoot.get("paidAmt"), rRoot.get("brokerId"), rRoot.get("reversalTransNo"),
				rRoot.get("branchCode")).

				where(cb.equal(personalRoot1.get("customerId"), rRoot.get("cedingId")),
						cb.equal(personalRoot1.get("branchCode"), req.getBranchCode()),
						cb.equal(personalRoot1.get("customerType"), "C"), cb.equal(rRoot.get("amendId"), rSq),
						cb.equal(personalRoot1.get("amendId"), pSq1),
						cb.equal(personalRoot2.get("customerId"), rRoot.get("brokerId")),
						cb.equal(personalRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(personalRoot2.get("customerType"), "B"), cb.equal(personalRoot2.get("amendId"), pSq2),
						cb.equal(rRoot.get("brokerId"), "63"), cb.equal(rRoot.get("transType"), req.getTranstype()),
						cb.equal(rRoot.get("branchCode"), req.getBranchCode()), cb.equal(rRoot.get("status"), "R")

				);
		List<Tuple> tupleResult = em.createQuery(cq).getResultList();

		// after union all
		CriteriaQuery<Tuple> uCq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> urRoot = uCq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> uPersonalRoot2 = uCq.from(PersonalInfo.class);

		Subquery<Integer> urSq = uCq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> urSubRoot = urSq.from(TtrnPaymentReceipt.class);

		urSq.select(cb.max(urSubRoot.get("amendId"))).where(
				cb.equal(urSubRoot.get("paymentReceiptNo"), urRoot.get("paymentReceiptNo")),
				cb.equal(urSubRoot.get("branchCode"), urRoot.get("branchCode")));

		Subquery<Integer> upSq2 = uCq.subquery(Integer.class);
		Root<PersonalInfo> upSubRoot2 = upSq2.from(PersonalInfo.class);

		upSq2.select(cb.max(upSubRoot2.get("amendId"))).where(
				cb.equal(upSubRoot2.get("customerId"), uPersonalRoot2.get("customerId")),
				cb.equal(upSubRoot2.get("branchCode"), uPersonalRoot2.get("branchCode")),
				cb.equal(upSubRoot2.get("customerType"), uPersonalRoot2.get("customerType")));

		Expression<String> uNameExpression = cb.concat(cb.concat(uPersonalRoot2.get("firstName"), " "),
				uPersonalRoot2.get("lastName"));

		uCq.multiselect(urRoot.get("paymentReceiptNo"), cb.literal(""), uNameExpression.alias("broker"),
				urRoot.get("paymentResponse"), urRoot.get("paidAmt"), urRoot.get("brokerId"),
				urRoot.get("reversalTransNo"), urRoot.get("branchCode")).

				where(cb.equal(uPersonalRoot2.get("customerId"), urRoot.get("brokerId")),
						cb.equal(uPersonalRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(uPersonalRoot2.get("customerType"), "B"), cb.equal(urRoot.get("amendId"), urSq),
						cb.equal(uPersonalRoot2.get("amendId"), upSq2), cb.notEqual(urRoot.get("brokerId"), "63"),
						cb.equal(urRoot.get("transType"), req.getTranstype()),
						cb.equal(urRoot.get("branchCode"), req.getBranchCode()), cb.equal(urRoot.get("status"), "R"));

		List<Tuple> uTupleResult = em.createQuery(uCq).getResultList();
		tupleResult.addAll(uTupleResult);
		return tupleResult;
	}

	@Override
	public List<Long> getTotCount(String receiptNo, String status) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		cq.multiselect(cb.count(root.get("sno"))).where(cb.equal(root.get("receiptNo"), receiptNo),
				cb.equal(root.get("status"), status));
		return em.createQuery(cq).getResultList();
	}

	// getDirectCeding(String branchId) -- STARTS -- OK
	@Override
	public List<Tuple> cedingSelectDirect(String branchId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PersonalInfo> root = cq.from(PersonalInfo.class);

		Expression<String> nameExpression = cb.concat(cb.concat(root.get("firstName"), " "), root.get("lastName"));

		Expression<Object> exp = cb.selectCase().when(cb.equal(root.get("customerType"), "C"), root.get("companyName"))
				.otherwise(nameExpression);

		cq.multiselect(root.get("customerId"), exp.alias("NAME")).where(cb.equal(root.get("branchCode"), branchId),
				cb.notEqual(root.get("customerId"), 63), cb.equal(root.get("customerType"), "C")).orderBy(cb.asc(exp));

		return em.createQuery(cq).getResultList();
	}
	// getDirectCeding(String branchId) -- ENDS

	// getShortname(String branchcode) -- STARTS -- OK
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
	// getShortname(String branchcode) -- ENDS

	// getReceiptViewList(ReceiptViewListReq req) -- STARTS
	@Override
	public List<Tuple> getRetViewDtls(ReceiptViewListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceiptDetails> tRoot = cq.from(TtrnPaymentReceiptDetails.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);

		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> tSubRoot = tSq.from(TtrnPaymentReceiptDetails.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(cb.equal(tSubRoot.get("receiptNo"), tRoot.get("receiptNo")),
				cb.equal(tSubRoot.get("receiptSlNo"), tRoot.get("receiptSlNo")));

		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		cq.multiselect(tRoot.get("receiptNo"), tRoot.get("receiptSlNo"), tRoot.get("amount"), tRoot.get("exchangeRate"),
				tRoot.get("transDate"), cRoot.get("shortName"), tRoot.get("totAmt"), tRoot.get("settledExcrate"),
				tRoot.get("convertedReccur"))
				.where(cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
						cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(tRoot.get("amendId"), tSq),
						cb.equal(cRoot.get("amendId"), cSq), cb.equal(tRoot.get("receiptSlNo"), req.getSerialno()))
				.orderBy(cb.asc(tRoot.get("receiptNo")));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;
	}
	// getReceiptViewList(ReceiptViewListReq req) -- ENDS

	// getRetroallocateTransaction(GetRetroallocateTransactionReq req) -- STARTS
	@Override
	public Integer updateRetroDetails(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRetroSoa> update = cb.createCriteriaUpdate(TtrnRetroSoa.class);
		Root<TtrnRetroSoa> root = update.from(TtrnRetroSoa.class);

		Double allocated = getAllocatedTillDate(args[1], args[2], "TtrnRetroSoa") + Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated)
		.where(cb.equal(root.get("retroContractNumber"), args[1]), cb.equal(root.get("transactionNo"), args[2]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public Integer updateRetroStatus(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRetroSoa> update = cb.createCriteriaUpdate(TtrnRetroSoa.class);
		Root<TtrnRetroSoa> root = update.from(TtrnRetroSoa.class);

		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("netDueRcp")), 0.0)
					.otherwise(root.<Double>get("netDueRcp")),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
		
		update.set(root.get("allocationStatus"), args[0])
		.where(cb.equal(exp, 0.0),
			   cb.equal(root.get("retroContractNumber"), args[1]),
			   cb.equal(root.get("transactionNo"), args[2]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	@Override
	public List<Tuple> getDirBroDtlsRetro(GetRetroallocateTransactionReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRetroSoa> root = cq.from(TtrnRetroSoa.class);
		String input = null;
		
		if("63".equalsIgnoreCase(req.getBrokerId()))
			input=req.getCedingId();
		else        	
			input=req.getBrokerId();
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		sq.select(subRoot.get("companyName"))
		  .where(cb.equal(subRoot.get("customerId"), root.get("retrocessionaire")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));
		
		Expression<Double> netDueExp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("netDueRcp")), 0.0)
				.otherwise(root.<Double>get("netDueRcp")),
			cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(root.<Double>get("allocatedTillDate")));

			cq.multiselect(root.get("retroContractNumber"),
					root.get("transactionNo").as(String.class),
					root.get("retrocessionaire"),
					root.get("transactionDate"),
					cb.literal("RE").alias("TYPE"),
					cb.abs(netDueExp),
					cb.selectCase().when(cb.greaterThan(netDueExp, 0.0), "P").otherwise("C"),
					sq.alias("CEDING_COMPANY_NAME"));
			
		cq.where(cb.equal(root.get("branchCode"), req.getBranchCode()),
				cb.equal(root.get("retroCurrencyId"), req.getAllOccurencyId()),
				cb.equal(cb.selectCase()
						.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(input))),
								root.get("retrocessionaire"))
						.otherwise(root.get("retroBroker")), input),
				cb.notEqual(netDueExp, 0.0));
		
		Order order1 = cb.asc(cb.selectCase().when(cb.isNotNull(root.get("retroContractNumber")), root.get("retroContractNumber"))
											 .otherwise(root.get("retroContractNumber")));
		Order order2 = cb.asc(cb.selectCase().when(cb.isNotNull(root.get("transactionNo")), root.get("transactionNo"))
				 .otherwise(root.get("transactionNo")));
		Order order3 = cb.asc(cb.selectCase().when(cb.isNotNull(root.get("netDueRcp")), root.get("netDueRcp"))
				 .otherwise(root.get("netDueRcp")));

		cq.orderBy(order1).orderBy(order2).orderBy(order3);

		return em.createQuery(cq).getResultList();
	}
	
	@Override
	public Long getseqno(String[] args) {
		/*
		 * CriteriaBuilder cb = em.getCriteriaBuilder(); CriteriaQuery<Long> query =
		 * cb.createQuery(Long.class); Root<Dual> root = query.from(Dual.class);
		 * query.multiselect( cb.function("Fn_get_SeqNo", Long.class,
		 * cb.literal(args[0]), cb.literal(args[1]), cb.literal(args[2]),
		 * cb.literal(args[3]), cb.literal(args[4])) ); return
		 * em.createQuery(query).getSingleResult();
		 */
		String seqno=queryImpl.getSequenceNo(args[0], args[1], args[2],args[3],args[4],args[5]);
		return Long.parseLong(seqno);
	}
	
	@Override
	@Transactional
	public Integer updateAlloTranDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnPaymentReceiptDetails> update = cb.createCriteriaUpdate(TtrnPaymentReceiptDetails.class);
		Root<TtrnPaymentReceiptDetails> root = update.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));
		
		Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnPaymentReceiptDetails") + Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated)
			  .set(root.get("loginId"), args[1])
			  .set(root.get("branchCode"), args[2])
			  .set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));

		update.where(cb.equal(root.get("receiptSlNo"), args[3]),
					 cb.equal(root.get("currencyId"), args[4]),
					 cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	
	// getRetroallocateTransaction(GetRetroallocateTransactionReq req) -- ENDS

	// getReceiptTreasuryGeneration(ReceiptTreasuryReq req) -- STARTS
	@Override
	public List<Tuple> getDirBroDtls(ReceiptTreasuryReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceipt> tRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> pRoot1 = cq.from(PersonalInfo.class);
		Root<PersonalInfo> pRoot2 = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);
		Root<BankMaster> bRoot = cq.from(BankMaster.class);

		// TTRN_PAYMENT_RECEIPT sq
		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> tSubRoot = tSq.from(TtrnPaymentReceipt.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(
				cb.equal(tSubRoot.get("paymentReceiptNo"), tRoot.get("paymentReceiptNo")),
				cb.equal(tSubRoot.get("branchCode"), tRoot.get("branchCode")));

		// PersonalInfo 1 sq
		Subquery<Integer> p1Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p1SubRoot = p1Sq.from(PersonalInfo.class);

		p1Sq.select(cb.max(p1SubRoot.get("amendId"))).where(
				cb.equal(p1SubRoot.get("customerId"), pRoot1.get("customerId")),
				cb.equal(p1SubRoot.get("branchCode"), pRoot1.get("branchCode")));

		// PersonalInfo 2 sq
		Subquery<Integer> p2Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p2SubRoot = p2Sq.from(PersonalInfo.class);

		p2Sq.select(cb.max(p2SubRoot.get("amendId"))).where(
				cb.equal(p2SubRoot.get("customerId"), pRoot2.get("customerId")),
				cb.equal(p2SubRoot.get("branchCode"), pRoot2.get("branchCode")));

		// Currency Master sq
		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		// Expressions
		Expression<String> nameExpression = cb.concat(cb.concat(pRoot2.get("firstName"), " "), pRoot2.get("lastName"));
		Expression<Number> round = cb.quot(tRoot.get("paidAmt"), tRoot.get("exchangeRate"));

		cq.multiselect(pRoot1.get("companyName"), nameExpression.alias("BROKER"), cRoot.get("currencyName"),
				tRoot.get("paidAmt"), tRoot.get("transDate"), tRoot.get("paymentReceiptNo"), tRoot.get("exchangeRate"),
				round.alias("TOT_EXC_AMT"), tRoot.get("brokerId"), tRoot.get("diffAmt"), tRoot.get("transactionType"),
				tRoot.get("currencyId"), tRoot.get("reversalTransNo"), tRoot.get("bankCharges"), tRoot.get("netAmt"),
				tRoot.get("convertedDiffAmt"), bRoot.get("bankName"), tRoot.get("whTax"), tRoot.get("remarks"),
				tRoot.get("premiumLavy")).

				where(cb.equal(tRoot.get("cedingId"), pRoot1.get("customerId")),
						cb.equal(pRoot1.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot1.get("customerType"), "C"), cb.equal(tRoot.get("amendId"), tSq),
						cb.equal(pRoot1.get("amendId"), p1Sq),
						cb.equal(pRoot2.get("customerId"), tRoot.get("brokerId")),
						cb.equal(pRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot2.get("customerType"), "B"), cb.equal(pRoot2.get("amendId"), p2Sq),
						cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
						cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(cRoot.get("amendId"), cSq),
						cb.equal(bRoot.get("bankId"), tRoot.get("receiptBank")),
						cb.equal(bRoot.get("branchCode"), tRoot.get("branchCode")), cb.equal(bRoot.get("status"), "Y"),
						cb.equal(tRoot.get("paymentReceiptNo"), req.getPayrecno()),
						cb.equal(tRoot.get("branchCode"), req.getBranchCode()));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;
	}

	@Override
	public List<Tuple> getBroDtls(ReceiptTreasuryReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceipt> tRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> pRoot2 = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);
		Root<BankMaster> bRoot = cq.from(BankMaster.class);

		// TTRN_PAYMENT_RECEIPT sq
		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> tSubRoot = tSq.from(TtrnPaymentReceipt.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(
				cb.equal(tSubRoot.get("paymentReceiptNo"), tRoot.get("paymentReceiptNo")),
				cb.equal(tSubRoot.get("branchCode"), tRoot.get("branchCode")));

		// PersonalInfo 2 sq
		Subquery<Integer> p2Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p2SubRoot = p2Sq.from(PersonalInfo.class);

		p2Sq.select(cb.max(p2SubRoot.get("amendId"))).where(
				cb.equal(p2SubRoot.get("customerId"), pRoot2.get("customerId")),
				cb.equal(p2SubRoot.get("branchCode"), pRoot2.get("branchCode")));

		// Currency Master sq
		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		// Expressions
		Expression<String> nameExpression = cb.concat(cb.concat(pRoot2.get("firstName"), " "), pRoot2.get("lastName"));
		Expression<Number> round = cb.quot(tRoot.get("paidAmt"), tRoot.get("exchangeRate"));

		cq.multiselect(cb.literal("").alias("COMPANY_NAME"), nameExpression.alias("BROKER"),
				cRoot.get("shortName").alias("CURRENCY_NAME"), tRoot.get("paidAmt"), tRoot.get("transDate"),
				tRoot.get("paymentReceiptNo"), tRoot.get("exchangeRate"), round.alias("TOT_EXC_AMT"),
				tRoot.get("brokerId"), tRoot.get("diffAmt"), tRoot.get("transactionType"), tRoot.get("currencyId"),
				tRoot.get("reversalTransNo"), tRoot.get("bankCharges"), tRoot.get("netAmt"),
				tRoot.get("convertedDiffAmt"), bRoot.get("bankName"), tRoot.get("whTax"), tRoot.get("remarks"),
				tRoot.get("premiumLavy")).

				where(cb.equal(pRoot2.get("customerId"), tRoot.get("brokerId")),
						cb.equal(pRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot2.get("customerType"), "C"), cb.equal(tRoot.get("amendId"), tSq),
						cb.equal(pRoot2.get("amendId"), p2Sq),
						cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
						cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(cRoot.get("amendId"), cSq),
						cb.equal(bRoot.get("bankId"), tRoot.get("receiptBank")),
						cb.equal(bRoot.get("branchCode"), tRoot.get("branchCode")), cb.equal(bRoot.get("status"), "Y"),
						cb.equal(tRoot.get("paymentReceiptNo"), req.getPayrecno()),
						cb.equal(tRoot.get("branchCode"), req.getBranchCode()));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;
	}
	// getReceiptTreasuryGeneration(ReceiptTreasuryReq req) -- ENDS

	// savereverseInsert(ReverseInsertReq req) -- STARTS
	@Override
	public List<Tuple> getAlloTransDtls(String receiptNo, String serialNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		if (serialNo != null) {
			cq.multiselect(root.get("sno"),
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
					root.get("receiptNo"),
					cb.selectCase().when(cb.equal(root.get("adjustmentType"), "W"), "Write Off")
					.when(cb.equal(root.get("adjustmentType"), "R"), "Round Off").otherwise(""));
			
			cq.where(cb.equal(root.get("receiptNo"), receiptNo), cb.equal(root.get("sno"), serialNo));
		}else {
			cq.multiselect(root.get("sno"),
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
					root.get("receiptNo"));
			cq.where(cb.equal(root.get("receiptNo"), receiptNo));
		}

		cq.orderBy(cb.desc(root.get("sno")));

		return em.createQuery(cq).getResultList();
	}
	@Transactional
	@Override
	public Integer updateAllocatedDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnAllocatedTransaction> update = cb.createCriteriaUpdate(TtrnAllocatedTransaction.class);
		Root<TtrnAllocatedTransaction> root = update.from(TtrnAllocatedTransaction.class);

		try {
			update.set(root.get("status"), args[0]).set(root.get("reversalDate"), parseDateLocal(args[1]))
					.set(root.get("reversalAmount"), new BigDecimal(args[2])).set(root.get("paidAmount"), new BigDecimal(args[3]))
					.set(root.get("loginId"), args[4]).set(root.get("branchCode"), args[5])
					.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		update.where(cb.equal(root.get("transactionNo"), args[6]), cb.equal(root.get("sno"), args[7]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
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

	@Override
	public Integer updateRetroPayment(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRetroSoa> update = cb.createCriteriaUpdate(TtrnRetroSoa.class);
		Root<TtrnRetroSoa> root = update.from(TtrnRetroSoa.class);

		Double allocated = getAllocatedTillDate(args[1], args[2], "TtrnClaimPayment") - Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated).set(root.get("allocationStatus"), "Pending");

		update.where(cb.equal(root.get("retroContractNumber"), args[1]), cb.equal(root.get("transactionNo"), args[2]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getRetroPayment(String contractNo, String transactionNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRetroSoa> root = cq.from(TtrnRetroSoa.class);
		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("netDueRcp")), 0.0).otherwise(root.get("netDueRcp")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("retroContractNumber"), contractNo),
				cb.equal(root.get("transactionNo"), transactionNo));

		return em.createQuery(cq).getResultList();
	}
	@Transactional
	@Override
	public Integer updatePymtRetDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnPaymentReceiptDetails> update = cb.createCriteriaUpdate(TtrnPaymentReceiptDetails.class);
		Root<TtrnPaymentReceiptDetails> root = update.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = update.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

		Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnPaymentReceiptDetails")
				- Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated)
				.set(root.get("sysDate"), new java.util.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[2]).set(root.get("loginId"), args[1]);

		update.where(cb.equal(root.get("receiptSlNo"), args[3]), cb.equal(root.get("currencyId"), args[4]),
				cb.equal(root.get("amendId"), sq));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getPymtRetDtls(String receiptSlNo, String currencyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

		cq.multiselect(cb.selectCase().when(cb.isNull(root.get("amount")), 0.0).otherwise(root.get("amount")),
				cb.selectCase().when(cb.isNull(root.get("allocatedTillDate")), 0.0)
						.otherwise(root.get("allocatedTillDate")));
		cq.where(cb.equal(root.get("receiptSlNo"), receiptSlNo), cb.equal(root.get("currencyId"), currencyId),
				cb.equal(root.get("amendId"), sq));

		return em.createQuery(cq).getResultList();
	}

	// Private method to get allocated till date
	private Double getAllocatedTillDate(String input1, String input2, String classType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		List<Double> list = null;
		if (classType.equals("RskPremiumDetails")) {

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

		} else if (classType.equals("TtrnRetroSoa")) {

			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<TtrnRetroSoa> root = cq.from(TtrnRetroSoa.class);
			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("retroContractNumber"), input1), cb.equal(root.get("transactionNo"), input2));
			list = em.createQuery(cq).getResultList();

		} else {
			//doubt
			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

			Subquery<Integer> sq = cq.subquery(Integer.class);
			Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

			sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
					cb.equal(subRoot.get("receiptSlNo"), root.get("receiptSlNo")));

			cq.multiselect(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
			cq.where(cb.equal(root.get("receiptSlNo"), input1), cb.equal(root.get("currencyId"), input2),
					cb.equal(root.get("amendId"), sq));
			list = em.createQuery(cq).getResultList();
		}

		return list != null && !list.isEmpty() ? list.get(0) : 0.0;

	}

	// savereverseInsert(ReverseInsertReq req) -- ENDS
	
	
	// allocateDetails(AllocateDetailsReq req) -- STARTS
	@Override
	public List<Tuple> getAllocatedTls(String payRecNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		cq.multiselect(root.get("sno"), 
					   root.get("currencyId"),
					   root.get("inceptionDate"),
					   root.get("adjustmentType"))
		.where(cb.equal(root.get("receiptNo"), payRecNo), 
			   cb.equal(root.get("status"), "Y")).distinct(true).orderBy(cb.desc(root.get("sno")));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public String getAllcoationType(String serialNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		cq.select(root.get("type"))
		.where(cb.equal(root.get("sNo"), serialNo), 
			   cb.equal(root.get("status"), "Y"));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Double getRetAmtDtls1(String serialNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Double> cq = cb.createQuery(Double.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);

		cq.multiselect(cb.selectCase().when(cb.isNull(cb.sum(root.get("paidAmount"))),0.0)
				.otherwise(cb.sum(root.get("paidAmount"))))
		.where(cb.equal(root.get("sNo"), serialNo), 
			   cb.equal(root.get("type"), "RE"));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Double getRetAmtDtls(String serialNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Double> cq1 = cb.createQuery(Double.class);
		Root<TtrnAllocatedTransaction> root1 = cq1.from(TtrnAllocatedTransaction.class);

		cq1.multiselect(cb.selectCase().when(cb.isNull(cb.sum(root1.get("paidAmount"))),0.0)
				.otherwise(cb.sum(root1.get("paidAmount"))))
		.where(cb.equal(root1.get("sno"), serialNo), 
			   cb.or(cb.equal(root1.get("type"), "P"), cb.equal(root1.get("type"), "PT")));
		Double result1 =  em.createQuery(cq1).getSingleResult();
		
		CriteriaQuery<Double> cq2 = cb.createQuery(Double.class);
		Root<TtrnAllocatedTransaction> root2 = cq2.from(TtrnAllocatedTransaction.class);

		cq2.multiselect(cb.selectCase().when(cb.isNull(cb.sum(root2.get("paidAmount"))),0.0)
				.otherwise(cb.sum(root2.get("paidAmount"))))
		.where(cb.equal(root2.get("sno"), serialNo), 
			   cb.or(cb.equal(root2.get("type"), "C"), cb.equal(root2.get("type"), "RT")));
		Double result2 =  em.createQuery(cq2).getSingleResult();
		
		return result1 - result2;
	}

	// allocateDetails(AllocateDetailsReq req) -- ENDS
	
	// allocateView(AllocateViewReq req) -- STARTS
	@Override
	public List<Tuple> getBroCedingIds(String payRecNo, String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceipt> root = cq.from(TtrnPaymentReceipt.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.multiselect(root.get("brokerId"), root.get("cedingId"))
		.where(cb.equal(root.get("paymentReceiptNo"), payRecNo), 
			   cb.equal(root.get("branchCode"), branchCode),
			   cb.equal(root.get("amendId"), sq));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	public BigDecimal getExchRate(String serialNo, String currecncyValue) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
		Root<TtrnPaymentReceiptDetails> root = cq.from(TtrnPaymentReceiptDetails.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), root.get("receiptNo")),
				cb.equal(subRoot.get("currencyId"), root.get("currencyId")));

		cq.select(root.get("exchangeRate"))
		.where(cb.equal(root.get("receiptSlNo"), serialNo), 
			   cb.equal(root.get("currencyId"), currecncyValue),
			   cb.equal(root.get("amendId"), sq)).distinct(true);
		
		return em.createQuery(cq).getSingleResult();
	}
	// allocateView(AllocateDetailsReq req) -- ENDS

	
	// getReceiptGeneration(GetReceiptGenerationReq req) --STARTS
	@Override
	public List<Tuple> getDirBroDtls(GetReceiptGenerationReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceipt> tRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> pRoot1 = cq.from(PersonalInfo.class);
		Root<PersonalInfo> pRoot2 = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);
		Root<BankMaster> bRoot = cq.from(BankMaster.class);

		// TTRN_PAYMENT_RECEIPT sq
		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> tSubRoot = tSq.from(TtrnPaymentReceipt.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(
				cb.equal(tSubRoot.get("paymentReceiptNo"), tRoot.get("paymentReceiptNo")),
				cb.equal(tSubRoot.get("branchCode"), tRoot.get("branchCode")));

		// PersonalInfo 1 sq
		Subquery<Integer> p1Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p1SubRoot = p1Sq.from(PersonalInfo.class);

		p1Sq.select(cb.max(p1SubRoot.get("amendId"))).where(
				cb.equal(p1SubRoot.get("customerId"), pRoot1.get("customerId")),
				cb.equal(p1SubRoot.get("branchCode"), pRoot1.get("branchCode")));

		// PersonalInfo 2 sq
		Subquery<Integer> p2Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p2SubRoot = p2Sq.from(PersonalInfo.class);

		p2Sq.select(cb.max(p2SubRoot.get("amendId"))).where(
				cb.equal(p2SubRoot.get("customerId"), pRoot2.get("customerId")),
				cb.equal(p2SubRoot.get("branchCode"), pRoot2.get("branchCode")));

		// Currency Master sq
		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		// Expressions
		Expression<String> nameExpression = cb.concat(cb.concat(pRoot2.get("firstName"), " "), pRoot2.get("lastName"));
		Expression<Number> round = cb.quot(tRoot.get("paidAmt"), tRoot.get("exchangeRate"));

		cq.multiselect(pRoot1.get("companyName"), nameExpression.alias("BROKER"), cRoot.get("currencyName"),
				tRoot.get("paidAmt"), tRoot.get("transDate"), tRoot.get("paymentReceiptNo"), tRoot.get("exchangeRate"),
				round.alias("TOT_EXC_AMT"), tRoot.get("brokerId"), tRoot.get("diffAmt"), tRoot.get("transactionType"),
				tRoot.get("currencyId"), tRoot.get("reversalTransNo"), tRoot.get("bankCharges"), tRoot.get("netAmt"),
				tRoot.get("convertedDiffAmt"), bRoot.get("bankName"), tRoot.get("whTax"), tRoot.get("remarks"),
				tRoot.get("premiumLavy")).

				where(cb.equal(tRoot.get("cedingId"), pRoot1.get("customerId")),
						cb.equal(pRoot1.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot1.get("customerType"), "C"), cb.equal(tRoot.get("amendId"), tSq),
						cb.equal(pRoot1.get("amendId"), p1Sq),
						cb.equal(pRoot2.get("customerId"), tRoot.get("brokerId")),
						cb.equal(pRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot2.get("customerType"), "B"), cb.equal(pRoot2.get("amendId"), p2Sq),
						cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
						cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(cRoot.get("amendId"), cSq),
						cb.equal(bRoot.get("bankId"), tRoot.get("receiptBank")),
						cb.equal(bRoot.get("branchCode"), tRoot.get("branchCode")), cb.equal(bRoot.get("status"), "Y"),
						cb.equal(tRoot.get("paymentReceiptNo"), req.getSerialNo()),
						cb.equal(tRoot.get("branchCode"), req.getBranchCode()));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;
	}

	@Override
	public List<Tuple> getBroDtls(GetReceiptGenerationReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnPaymentReceipt> tRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> pRoot2 = cq.from(PersonalInfo.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);
		Root<BankMaster> bRoot = cq.from(BankMaster.class);

		// TTRN_PAYMENT_RECEIPT sq
		Subquery<Integer> tSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> tSubRoot = tSq.from(TtrnPaymentReceipt.class);

		tSq.select(cb.max(tSubRoot.get("amendId"))).where(
				cb.equal(tSubRoot.get("paymentReceiptNo"), tRoot.get("paymentReceiptNo")),
				cb.equal(tSubRoot.get("branchCode"), tRoot.get("branchCode")));

		// PersonalInfo 2 sq
		Subquery<Integer> p2Sq = cq.subquery(Integer.class);
		Root<PersonalInfo> p2SubRoot = p2Sq.from(PersonalInfo.class);

		p2Sq.select(cb.max(p2SubRoot.get("amendId"))).where(
				cb.equal(p2SubRoot.get("customerId"), pRoot2.get("customerId")),
				cb.equal(p2SubRoot.get("branchCode"), pRoot2.get("branchCode")));

		// Currency Master sq
		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		// Expressions
		Expression<String> nameExpression = cb.concat(cb.concat(pRoot2.get("firstName"), " "), pRoot2.get("lastName"));
		Expression<Number> round = cb.quot(tRoot.get("paidAmt"), tRoot.get("exchangeRate"));

		cq.multiselect(cb.literal("").alias("COMPANY_NAME"),
				nameExpression.alias("BROKER"),
				cRoot.get("shortName").alias("CURRENCY_NAME"), 
				tRoot.get("paidAmt"), 
				tRoot.get("transDate"),
				tRoot.get("paymentReceiptNo"),
				tRoot.get("exchangeRate"),
				round.alias("TOT_EXC_AMT"),
				tRoot.get("brokerId"),
				tRoot.get("diffAmt"),
				tRoot.get("transactionType"),
				tRoot.get("currencyId"),
				tRoot.get("reversalTransNo"),
				tRoot.get("bankCharges"),
				tRoot.get("netAmt"),
				tRoot.get("convertedDiffAmt"),
				bRoot.get("bankName"),
				tRoot.get("whTax"),
				tRoot.get("remarks"),
				tRoot.get("premiumLavy")).

				where(cb.equal(pRoot2.get("customerId"), tRoot.get("brokerId")),
						cb.equal(pRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(pRoot2.get("customerType"), "B"), 
						cb.equal(tRoot.get("amendId"), tSq),
						cb.equal(pRoot2.get("amendId"), p2Sq),
						cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
						cb.equal(cRoot.get("branchCode"), req.getBranchCode()), cb.equal(cRoot.get("amendId"), cSq),
						cb.equal(bRoot.get("bankId"), tRoot.get("receiptBank")),
						cb.equal(bRoot.get("branchCode"), tRoot.get("branchCode")), cb.equal(bRoot.get("status"), "Y"),
						cb.equal(tRoot.get("paymentReceiptNo"), req.getSerialNo()),
						cb.equal(tRoot.get("branchCode"), req.getBranchCode()));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList;
	}

	// getReceiptGeneration(GetReceiptGenerationReq req) --ENDS
	
	//getTransContract(GetTransContractReq req) -- STARTS
	@Override
	public List<Tuple> getTranContDtls(GetTransContractReq req) {
		List<Tuple> resultList = getTranContDtlsForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getAlloccurrencyId(), req.getBranchCode());
		List<Tuple> resultList1 = getTranContDtlsForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getAlloccurrencyId(), req.getBranchCode());
		List<Tuple> resultList2 = getTranContDtlsForBill(req.getBrokerId(), req.getCedingId(), 
				req.getAlloccurrencyId(), req.getBranchCode());
		if(!CollectionUtils.isEmpty(resultList1)) {
			resultList.addAll(resultList1);
		}
		if(!CollectionUtils.isEmpty(resultList2)) {
			resultList.addAll(resultList2);
		}
		
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}
	
	private List<Tuple> getTranContDtlsForBill(String brokerId, String cedingId, String alloccurrencyId,
			String branchCode) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		//Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<TtrnBillingInfo> rRoot = cq.from(TtrnBillingInfo.class);
		String input = null;


		Expression<String> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("utilizedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("utilizedTillDate")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate"))).as(String.class);

		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("customerId"), rRoot.get("cedingId")),
				cb.equal(aSubRoot.get("branchCode"), rRoot.get("branchCode")));

		sq.select(subRoot.get("companyName")).where(cb.equal(subRoot.get("customerId"), rRoot.get("cedingId")),
				cb.equal(subRoot.get("branchCode"), rRoot.get("branchCode")), cb.equal(subRoot.get("amendId"), aSq));

		cq.multiselect(rRoot.get("transactionNo").as(String.class),cb.literal("RTRIM").as(String.class),
				cb.literal("RTRIM").as(String.class),
				cb.literal("RTRIM"), rRoot.get("sysDate"), exp.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), cb.literal("RTRIM").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.literal("RTRIM").as(String.class),
				cb.literal("B").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				cb.literal("RTRIM").as(String.class),
				cb.literal("RTRIM").as(String.class)).distinct(true);

		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<TtrnBillingInfo> pSubRoot = pSq.from(TtrnBillingInfo.class);

		Expression<Object> exp1 = cb.selectCase()
				.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
						rRoot.get("cedingId"))
				.otherwise(rRoot.get("brokerId"));

		if("63".equalsIgnoreCase(brokerId))
			input=cedingId;
		else        	
			input=brokerId;
		
		pSq.select(cb.max(pSubRoot.get("amendId"))).where(cb.equal(pSubRoot.get("billingNo"), rRoot.get("billingNo")),
				 cb.equal(rRoot.get("currencyId"), alloccurrencyId),
				cb.equal(exp1, input));
				//cb.like(pRoot.get("contractNo"), ""), cb.like(pRoot.get("productId"), "")); // check

		cq.where( cb.equal(rRoot.get("billingNo"), rRoot.get("billingNo")),
				cb.equal(rRoot.get("branchCode"), branchCode),
				cb.notEqual(exp, 0),
				cb.equal(rRoot.get("amendId"), pSq));

		list =  em.createQuery(cq).getResultList();
		}catch(Exception e) {
			e.printStackTrace();;
		}
		return list;
	}

	//GetTransContractReq req -- params
	private List<Tuple> getTranContDtlsForRsk(String brokerId, String cedingId, String alloccurrencyId, String branchCode) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<RskPremiumDetails> rRoot = cq.from(RskPremiumDetails.class);
		String input = null;

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

		cq.where(cb.or(cb.isNull(rRoot.get("receiptNo")),cb.equal(rRoot.get("receiptNo"), "0")) , cb.equal(rRoot.get("contractNo"), pRoot.get("contractNo")),
				cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						cb.selectCase().when(cb.isNull(rRoot.get("layerNo")), 0).otherwise(rRoot.get("layerNo"))),
				cb.equal(pRoot.get("deptId"), rRoot.get("subClass")),
				cb.equal(pRoot.get("branchCode"), branchCode),
				cb.notEqual(exp, 0),
				cb.equal(pRoot.get("amendId"), pSq));

		list =  em.createQuery(cq).getResultList();
		}catch(Exception e) {
			e.printStackTrace();;
		}
		return list;
	}
	
	
	private List<Tuple> getTranContDtlsForClaim(String brokerId, String cedingId, String alloccurrencyId, String branchCode){
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
		
		cq.where(cb.equal(pRoot.get("contractNo"), tcdRoot.get("contractNo")),
				 cb.equal(tcdRoot.get("contractNo"), tcpRoot.get("contractNo")),
				 cb.equal(tcdRoot.get("claimNo"), tcpRoot.get("claimNo")),
				 cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						 cb.selectCase().when(cb.isNull(tcdRoot.get("layerNo")), 0).otherwise(tcdRoot.get("layerNo"))),
				 cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("sectionNo")), 0).otherwise(pRoot.get("sectionNo")),
						 cb.selectCase().when(cb.isNull(tcdRoot.get("subClass")), 0).otherwise(tcdRoot.get("subClass"))),
				//cb.equal(pRoot.get("sectionNo"), tcdRoot.get("subClass")),
				cb.equal(pRoot.get("branchCode"), branchCode),
				cb.notEqual(exp, 0),
				cb.equal(pRoot.get("amendId"), pSq));
		
		return em.createQuery(cq).getResultList();
	}
	//getTransContract(GetTransContractReq req) -- ENDS

	// getRetroTransContract(RetroTransReq req) -- STARTS
	@Override
	public List<Tuple> getDirBroDtlsRetro(RetroTransReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<TtrnRetroSoa> root = cq.from(TtrnRetroSoa.class);
		String input = null;

		if ("63".equalsIgnoreCase(req.getBrokerid()))
			input = req.getCedingid();
		else
			input = req.getBrokerid();

		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		sq.select(subRoot.get("companyName")).where(cb.equal(subRoot.get("customerId"), root.get("retrocessionaire")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		Expression<Double> netDueExp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("netDueRcp")), 0.0)
						.otherwise(root.<Double>get("netDueRcp")),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(root.<Double>get("allocatedTillDate")));

		cq.multiselect(root.get("retroContractNumber"), root.get("transactionNo").as(String.class),
				root.get("retrocessionaire"), root.get("transactionDate"), cb.literal("RE").alias("TYPE"),
				cb.abs(netDueExp), cb.selectCase().when(cb.greaterThan(netDueExp, 0.0), "P").otherwise("C"),
				sq.alias("CEDING_COMPANY_NAME"));

		cq.where(cb.equal(root.get("branchCode"), req.getBranchcode()),
				cb.equal(root.get("retroCurrencyId"), req.getAlloccurrencyid()),
				cb.equal(cb.selectCase()
						.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(input))),
								root.get("retrocessionaire"))
						.otherwise(root.get("retroBroker")), input),
				cb.notEqual(netDueExp, 0.0));

		Order order1 = cb.asc(
				cb.selectCase().when(cb.isNotNull(root.get("retroContractNumber")), root.get("retroContractNumber"))
						.otherwise(root.get("retroContractNumber")));
		Order order2 = cb.asc(cb.selectCase().when(cb.isNotNull(root.get("transactionNo")), root.get("transactionNo"))
				.otherwise(root.get("transactionNo")));
		Order order3 = cb.asc(cb.selectCase().when(cb.isNotNull(root.get("netDueRcp")), root.get("netDueRcp"))
				.otherwise(root.get("netDueRcp")));

		cq.orderBy(order1).orderBy(order2).orderBy(order3);

		return em.createQuery(cq).getResultList();
	}
	// getRetroTransContract(RetroTransReq req) -- ENDS

	//getTreasuryJournalView(GetTreasuryJournalViewReq req) -- STARTS
	@Override
	public List<Tuple> treasuryJournalView(GetTreasuryJournalViewReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<JournelFormat> root = cq.from(JournelFormat.class);
		String input = "";
		List<Tuple> result = null;
		
		if(req.getType().equalsIgnoreCase(""))
			input=req.getSerialNo();
		else
			input = req.getAllocationNo();
		
		cq.multiselect(root.get("startDate").as(String.class).alias("STARTDATE"), 
				root.get("reference").alias("REFERENCE"), 
				root.get("ledger").alias("LEDGER"), 
				root.get("uwy").alias("UWY"), 
				root.get("spc").alias("SPC"), 
				root.get("currency").alias("CURRENCY"), 
				root.get("debitOc").as(BigDecimal.class).alias("DEBITOC"), 
				root.get("creditOc").as(BigDecimal.class).alias("CREDITOC"), 
				root.get("debitUgx").as(BigDecimal.class).alias("DEBITUGX"), 
				root.get("creditUgx").as(BigDecimal.class).alias("CREDITUGX"), 
				root.get("narration").alias("NARRATION"), 
				root.get("productId").as(String.class).alias("PRODUCT_ID"), 
				root.get("endDate").as(String.class).alias("END_DATE")).where(cb.equal(root.get("transactionNo"), input));
		
		result = em.createQuery(cq).getResultList();
		
		CriteriaQuery<Tuple> cq2 = cb.createTupleQuery();
		Root<JournelFormat> root2 = cq2.from(JournelFormat.class);
		
		cq2.multiselect(cb.literal("").alias("STARTDATE"), 
				cb.literal("").alias("REFERENCE"), 
				cb.literal("Total").alias("LEDGER"), 
				cb.literal("").alias("UWY"), 
				cb.literal("").alias("SPC"), 
				cb.literal("").alias("CURRENCY"), 
				cb.sum(root2.get("debitOc")).alias("DEBITOC"), 
				cb.sum(root2.get("creditOc")).alias("CREDITOC"), 
				cb.sum(root2.get("debitUgx")).alias("DEBITUGX"), 
				cb.sum(root2.get("creditUgx")).alias("CREDITUGX"), 
				cb.literal("").alias("NARRATION"), 
				cb.literal("").alias("PRODUCT_ID"), 
				cb.literal("").alias("END_DATE")).where(cb.equal(root2.get("transactionNo"), input));
		
		List<Tuple> result2 = em.createQuery(cq2).getResultList();
		if(Objects.nonNull(result) && Objects.nonNull(result2))
			result.addAll(result2);
		
		return result;
	}
	//getTreasuryJournalView(GetTreasuryJournalViewReq req) -- ENDS

	//getReciptList(ReciptListReq req) -- STARTS
	@Override
	public List<Tuple> getRetLists(ReciptListReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// before union all
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> rRoot = cq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> personalRoot1 = cq.from(PersonalInfo.class);
		Root<PersonalInfo> personalRoot2 = cq.from(PersonalInfo.class);

		Subquery<Integer> rSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> rSubRoot = rSq.from(TtrnPaymentReceipt.class);

		rSq.select(cb.max(rSubRoot.get("amendId"))).where(
				cb.equal(rSubRoot.get("paymentReceiptNo"), rRoot.get("paymentReceiptNo")),
				cb.equal(rSubRoot.get("branchCode"), rRoot.get("branchCode")));

		Subquery<Integer> pSq1 = cq.subquery(Integer.class);
		Root<PersonalInfo> pSubRoot1 = pSq1.from(PersonalInfo.class);

		pSq1.select(cb.max(pSubRoot1.get("amendId"))).where(
				cb.equal(pSubRoot1.get("customerId"), personalRoot1.get("customerId")),
				cb.equal(pSubRoot1.get("branchCode"), personalRoot1.get("branchCode")),
				cb.equal(pSubRoot1.get("customerType"), personalRoot1.get("customerType")));

		Subquery<Integer> pSq2 = cq.subquery(Integer.class);
		Root<PersonalInfo> pSubRoot2 = pSq2.from(PersonalInfo.class);

		pSq2.select(cb.max(pSubRoot2.get("amendId"))).where(
				cb.equal(pSubRoot2.get("customerId"), personalRoot2.get("customerId")),
				cb.equal(pSubRoot2.get("branchCode"), personalRoot2.get("branchCode")),
				cb.equal(pSubRoot2.get("customerType"), personalRoot2.get("customerType")));

		Expression<String> nameExpression = cb.concat(cb.concat(personalRoot2.get("firstName"), " "),
				personalRoot2.get("lastName"));
		
		cq.multiselect(rRoot.get("paymentReceiptNo").alias("PAYMENT_RECEIPT_NO"),
					   personalRoot1.get("companyName").alias("COMPANY_NAME"), 
					   nameExpression.alias("BROKER"),
					   rRoot.get("paymentResponse").alias("PAYMENT_RESPONSE"),
					   rRoot.get("paidAmt").alias("PAID_AMT"),
					   rRoot.get("brokerId").alias("BROKER_ID"),
					   rRoot.get("reversalTransNo").alias("REVERSALTRANSNO"),
					   rRoot.get("remarks").alias("REMARKS"),
					   rRoot.get("transactionType").alias("TRANSCATIONTYPE"),
					   rRoot.get("transDate").alias("TRANS_DATE"),
					   rRoot.get("branchCode").alias("BRANCH_CODE")).

				where(cb.equal(personalRoot1.get("customerId"), rRoot.get("cedingId")),
						cb.equal(personalRoot1.get("branchCode"), req.getBranchCode()),
						cb.equal(personalRoot1.get("customerType"), "C"), cb.equal(rRoot.get("amendId"), rSq),
						cb.equal(personalRoot1.get("amendId"), pSq1),
						cb.equal(personalRoot2.get("customerId"), rRoot.get("brokerId")),
						cb.equal(personalRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(personalRoot2.get("customerType"), "B"), cb.equal(personalRoot2.get("amendId"), pSq2),
						cb.equal(rRoot.get("brokerId"), "63"), cb.equal(rRoot.get("transType"), req.getTransType()),
						cb.equal(rRoot.get("branchCode"), req.getBranchCode()));

		List<Tuple> tupleResult = em.createQuery(cq).getResultList();

		// after union all
		CriteriaQuery<Tuple> uCq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> urRoot = uCq.from(TtrnPaymentReceipt.class);
		Root<PersonalInfo> uPersonalRoot2 = uCq.from(PersonalInfo.class);

		Subquery<Integer> urSq = uCq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> urSubRoot = urSq.from(TtrnPaymentReceipt.class);

		urSq.select(cb.max(urSubRoot.get("amendId"))).where(
				cb.equal(urSubRoot.get("paymentReceiptNo"), urRoot.get("paymentReceiptNo")),
				cb.equal(urSubRoot.get("branchCode"), urRoot.get("branchCode")));

		Subquery<Integer> upSq2 = uCq.subquery(Integer.class);
		Root<PersonalInfo> upSubRoot2 = upSq2.from(PersonalInfo.class);

		upSq2.select(cb.max(upSubRoot2.get("amendId"))).where(
				cb.equal(upSubRoot2.get("customerId"), uPersonalRoot2.get("customerId")),
				cb.equal(upSubRoot2.get("branchCode"), uPersonalRoot2.get("branchCode")),
				cb.equal(upSubRoot2.get("customerType"), uPersonalRoot2.get("customerType")));

		Expression<String> uNameExpression = cb.concat(cb.concat(uPersonalRoot2.get("firstName"), " "),
				uPersonalRoot2.get("lastName"));

		uCq.multiselect(urRoot.get("paymentReceiptNo").alias("PAYMENT_RECEIPT_NO"),
						cb.literal("").alias("COMPANY_NAME"),
						uNameExpression.alias("BROKER"),
						urRoot.get("paymentResponse").alias("PAYMENT_RESPONSE"),
						urRoot.get("paidAmt").alias("PAID_AMT"),
						urRoot.get("brokerId").alias("BROKER_ID"),
						urRoot.get("reversalTransNo").alias("REVERSALTRANSNO"),
						urRoot.get("remarks").alias("REMARKS"),
						urRoot.get("transactionType").alias("TRANSCATIONTYPE"), 
						urRoot.get("transDate").alias("TRANS_DATE"),
						urRoot.get("branchCode").alias("BRANCH_CODE")).

				where(cb.equal(uPersonalRoot2.get("customerId"), urRoot.get("brokerId")),
						cb.equal(uPersonalRoot2.get("branchCode"), req.getBranchCode()),
						cb.equal(uPersonalRoot2.get("customerType"), "B"), cb.equal(urRoot.get("amendId"), urSq),
						cb.equal(uPersonalRoot2.get("amendId"), upSq2), cb.notEqual(urRoot.get("brokerId"), "63"),
						cb.equal(urRoot.get("transType"), req.getTransType()),
						cb.equal(urRoot.get("branchCode"), req.getBranchCode()));

		List<Tuple> uTupleResult = em.createQuery(uCq).getResultList();
		tupleResult.addAll(uTupleResult);
		return tupleResult;
	}

	@Override
	public List<Tuple> paymentAmountDetails(String branchCode, String payrecno) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceiptDetails> tRoot = cq.from(TtrnPaymentReceiptDetails.class);
		Root<CurrencyMaster> cRoot = cq.from(CurrencyMaster.class);
		
		cq.multiselect(cb.diff(tRoot.<Double>get("amount"),
							   cb.<Double>selectCase().when(cb.isNull(tRoot.<Double>get("allocatedTillDate")), 0.0)
							   .otherwise(tRoot.<Double>get("allocatedTillDate"))).alias("AMOUNT"));
		
		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> subRoot = sq.from(TtrnPaymentReceiptDetails.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("receiptNo"), tRoot.get("receiptNo")),
				cb.equal(subRoot.get("receiptSlNo"), tRoot.get("receiptSlNo")));
		
		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<CurrencyMaster> cSubRoot = cSq.from(CurrencyMaster.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("currencyId"), cRoot.get("currencyId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));
		
		cq.where(cb.equal(cRoot.get("currencyId"), tRoot.get("currencyId")),
				 cb.equal(cRoot.get("branchCode"), branchCode),
				 cb.equal(tRoot.get("amendId"), sq),
				 cb.equal(cRoot.get("amendId"), cSq),
				 cb.equal(tRoot.get("receiptSlNo"), payrecno));
		
		return em.createQuery(cq).getResultList();
	}

	@Override
	@Transactional
	public Integer updateRskPremChkyn() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		update.set(root.get("checkyn"), "").set(root.get("accPremium"), "");
		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateClaimPymtChkyn() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);

		update.set(root.get("checkyn"), "").set(root.get("accClaim"), "");
		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	public List<Tuple> getRetAlloDtls(GetReceiptAllocateReq req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// before union all
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> aRoot = cq.from(TtrnPaymentReceipt.class);
		Root<CurrencyMaster> bRoot = cq.from(CurrencyMaster.class);
		Root<PersonalInfo> cRoot = cq.from(PersonalInfo.class);
		Root<PersonalInfo> dRoot = cq.from(PersonalInfo.class);

		Subquery<Integer> aSq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> aSubRoot = aSq.from(TtrnPaymentReceipt.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("paymentReceiptNo"), aRoot.get("paymentReceiptNo")),
				cb.equal(aSubRoot.get("branchCode"), aRoot.get("branchCode")));

		Subquery<Integer> cSq = cq.subquery(Integer.class);
		Root<PersonalInfo> cSubRoot = cSq.from(PersonalInfo.class);

		cSq.select(cb.max(cSubRoot.get("amendId"))).where(cb.equal(cSubRoot.get("customerId"), cRoot.get("customerId")),
				cb.equal(cSubRoot.get("branchCode"), cRoot.get("branchCode")));

		Subquery<Integer> dSq = cq.subquery(Integer.class);
		Root<PersonalInfo> dSubRoot = dSq.from(PersonalInfo.class);

		dSq.select(cb.max(dSubRoot.get("amendId"))).where(cb.equal(dSubRoot.get("customerId"), dRoot.get("customerId")),
				cb.equal(dSubRoot.get("branchCode"), dRoot.get("branchCode")));

		Expression<String> nameExpression = cb.concat(cb.concat(dRoot.get("firstName"), " "), dRoot.get("lastName"));

		cq.multiselect(aRoot.get("paymentReceiptNo").alias("PAYMENT_RECEIPT_NO"),
				aRoot.get("paidAmt").alias("PAID_AMT"), bRoot.get("shortName").alias("CURRENCY_NAME"),
				cRoot.get("companyName").alias("COMPANY_NAME"), nameExpression.alias("BROKER"),
				cRoot.get("customerId").alias("CEDDINGID"), dRoot.get("customerId").alias("BROKERID"),
				aRoot.get("remarks").alias("REMARKS"), aRoot.get("branchCode").alias("BRANCH_CODE")).distinct(true).

				where(cb.equal(aRoot.get("currencyId"), bRoot.get("currencyId")),
						cb.equal(aRoot.get("cedingId"), cRoot.get("customerId")),
						cb.equal(aRoot.get("brokerId"), dRoot.get("customerId")), cb.equal(aRoot.get("brokerId"), "63"),
						cb.equal(aRoot.get("branchCode"), req.getBranchCode()),
						cb.equal(aRoot.get("branchCode"), bRoot.get("branchCode")),
						cb.equal(aRoot.get("branchCode"), cRoot.get("branchCode")),
						cb.equal(aRoot.get("branchCode"), dRoot.get("branchCode")), cb.equal(aRoot.get("amendId"), aSq),
						cb.equal(cRoot.get("amendId"), cSq), cb.equal(dRoot.get("amendId"), dSq),
						cb.equal(aRoot.get("status"), "Y"), cb.equal(aRoot.get("transType"), req.getTransType()));

		List<Tuple> tupleResult = em.createQuery(cq).getResultList();

		// after union all
		tupleResult.addAll(getRetAlloDtlsUnion(req));
		return tupleResult;
	}
	
	private List<Tuple> getRetAlloDtlsUnion(GetReceiptAllocateReq req){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> ucq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> aRoot1 = ucq.from(TtrnPaymentReceipt.class);
		Root<CurrencyMaster> bRoot1 = ucq.from(CurrencyMaster.class);
		Root<PersonalInfo> dRoot1 = ucq.from(PersonalInfo.class);

		Subquery<Integer> aSq1 = ucq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> aSubRoot1 = aSq1.from(TtrnPaymentReceipt.class);

		aSq1.select(cb.max(aSubRoot1.get("amendId"))).where(
				cb.equal(aSubRoot1.get("paymentReceiptNo"), aRoot1.get("paymentReceiptNo")),
				cb.equal(aSubRoot1.get("branchCode"), aRoot1.get("branchCode")));

		Subquery<Integer> dSq1 = ucq.subquery(Integer.class);
		Root<PersonalInfo> dSubRoot1 = dSq1.from(PersonalInfo.class);

		dSq1.select(cb.max(dSubRoot1.get("amendId"))).where(
				cb.equal(dSubRoot1.get("customerId"), dRoot1.get("customerId")),
				cb.equal(dSubRoot1.get("branchCode"), dRoot1.get("branchCode")));

		Expression<String> nameExpression1 = cb.concat(cb.concat(dRoot1.get("firstName"), " "), dRoot1.get("lastName"));

		Subquery<Integer> aSq = ucq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> aSubRoot = aSq.from(TtrnPaymentReceipt.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("paymentReceiptNo"), aRoot1.get("paymentReceiptNo")),
				cb.equal(aSubRoot.get("branchCode"), aRoot1.get("branchCode")));

		Subquery<Integer> dSq = ucq.subquery(Integer.class);
		Root<PersonalInfo> dSubRoot = dSq.from(PersonalInfo.class);

		dSq.select(cb.max(dSubRoot.get("amendId"))).where(cb.equal(dSubRoot.get("customerId"), dRoot1.get("customerId")),
				cb.equal(dSubRoot.get("branchCode"), dRoot1.get("branchCode")));

		
		ucq.multiselect(aRoot1.get("paymentReceiptNo").alias("PAYMENT_RECEIPT_NO"),
				aRoot1.get("paidAmt").alias("PAID_AMT"), bRoot1.get("shortName").alias("CURRENCY_NAME"),
				cb.literal("").alias("COMPANY_NAME").alias("COMPANY_NAME"), nameExpression1.alias("BROKER"),
				cb.literal(0).alias("CEDDINGID"), dRoot1.get("customerId").alias("BROKERID"),
				aRoot1.get("remarks").alias("REMARKS"), aRoot1.get("branchCode").alias("BRANCH_CODE")).distinct(true).

				where(cb.equal(aRoot1.get("currencyId"), bRoot1.get("currencyId")),
						cb.equal(aRoot1.get("brokerId"), dRoot1.get("customerId")),
						cb.notEqual(aRoot1.get("brokerId"), "63"),
						cb.equal(aRoot1.get("branchCode"), req.getBranchCode()),
						cb.equal(aRoot1.get("branchCode"), bRoot1.get("branchCode")),
						cb.equal(aRoot1.get("branchCode"), dRoot1.get("branchCode")),
						cb.equal(aRoot1.get("amendId"), aSq), cb.equal(dRoot1.get("amendId"), dSq),
						cb.equal(aRoot1.get("status"), "Y"), cb.equal(aRoot1.get("transType"), req.getTransType()));

		return em.createQuery(ucq).getResultList();
		
	}

	@Override
	public List<Tuple> getPymtRetCurrDtls(String branchCode, String receiptSlNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> ucq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceiptDetails> aRoot = ucq.from(TtrnPaymentReceiptDetails.class);
		Root<CurrencyMaster> bRoot = ucq.from(CurrencyMaster.class);

		Subquery<Integer> aSq = ucq.subquery(Integer.class);
		Root<TtrnPaymentReceiptDetails> aSubRoot = aSq.from(TtrnPaymentReceiptDetails.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("receiptNo"), aRoot.get("receiptNo")),
				cb.equal(aSubRoot.get("receiptSlNo"), aRoot.get("receiptSlNo")));

		Subquery<Integer> bSq = ucq.subquery(Integer.class);
		Root<CurrencyMaster> bSubRoot = bSq.from(CurrencyMaster.class);

		bSq.select(cb.max(bSubRoot.get("amendId"))).where(
				cb.equal(bSubRoot.get("currencyId"), bRoot.get("currencyId")),
				cb.equal(bSubRoot.get("branchCode"), bRoot.get("branchCode")));

		Expression<Double> exp = cb.diff(aRoot.<Double>get("amount"), 
				cb.<Double>selectCase().when(cb.isNull(aRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(aRoot.<Double>get("allocatedTillDate")));
		
		ucq.multiselect(bRoot.get("currencyId").alias("CURRENCY_ID"),
						bRoot.get("shortName").alias("CURRENCY_NAME"),
						exp.alias("AMOUNT")).
		where(cb.equal(bRoot.get("currencyId"), aRoot.get("currencyId")),
			  cb.equal(bRoot.get("branchCode"), branchCode),
			  cb.equal(aRoot.get("amendId"), aSq),
			  cb.equal(bRoot.get("amendId"), bSq),
			  cb.equal(aRoot.get("receiptSlNo"), receiptSlNo));

		List<Tuple> uTupleResult = em.createQuery(ucq).getResultList();
		return uTupleResult;
	}

	@Override
	public List<Tuple> getTranContDtls(GetAllTransContractReq req) {
		List<Tuple> resultList = getTranContDtlsForRsk(req.getBrokerid(), req.getCedingid(), 
				req.getAlloccurrencyid(), req.getBranchCode());
		if(Objects.nonNull(resultList))
			resultList.addAll(getTranContDtlsForClaim(req.getBrokerid(), req.getCedingid(), 
					req.getAlloccurrencyid(), req.getBranchCode()));
		else
			resultList = getTranContDtlsForClaim(req.getBrokerid(), req.getCedingid(), 
					req.getAlloccurrencyid(), req.getBranchCode());
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}

	@Override
	public List<Map<String, Object>> getReversalInfo(String[] args) {
		List<Map<String,Object>> list;
		//LEFT OUTER JOIN
		Query q = em.createNativeQuery("SELECT   DECODE (PI.COMPANY_NAME, 'DIRECT', NULL, PI.COMPANY_NAME)COMPANY_NAME,"
				+ " PIB.FIRST_NAME || ' ' || PIB.LAST_NAME broker, AB.BROKER_ID,"
				+ " CM.SHORT_NAME CURRENCY_NAME,AB.EXCHANGE_RATE,AB.PAYMENT_RECEIPT_NO,"
				+ " AB.PAID_AMT, Ab.CEDING_ID, AB.STATUS From  (Select A.Amend_id,A.STATUS,A.BRANCH_CODE,"
				+ "A.EXCHANGE_RATE, A.PAYMENT_RECEIPT_NO,A.CEDING_ID,A.BROKER_ID,A.PAID_AMT,A.TRANS_TYPE,"
				+ "A.CURRENCY_ID from TTRN_PAYMENT_RECEIPT A LEFT OUTER JOIN(  SELECT   RECEIPT_SL_NO,"
				+ " NVL (SUM (ALLOCATED_TILL_DATE), '0') ALLOCATED_TILL_DATE FROM   TTRN_PAYMENT_RECEIPT_DETAILS "
				+ "GROUP BY   RECEIPT_SL_NO) B ON A.PAYMENT_RECEIPT_NO = b.RECEIPT_SL_NO AND B.ALLOCATED_TILL_DATE = 0"
				+ " Where  A.AMEND_ID =(SELECT   MAX (AMEND_ID) FROM   TTRN_PAYMENT_RECEIPT WHERE "
				+ "  PAYMENT_RECEIPT_NO = A.PAYMENT_RECEIPT_NO AND BRANCH_CODE = A.BRANCH_CODE)) AB JOIN PERSONAL_INFO"
				+ " PI ON PI.CUSTOMER_ID = CASE WHEN AB.BROKER_ID = 63 THEN AB.CEDING_ID ELSE 63 END "
				+ "AND PI.AMEND_ID = (SELECT   MAX (AMEND_ID) FROM   PERSONAL_INFO WHERE   CUSTOMER_ID = PI.CUSTOMER_ID"
				+ " AND BRANCH_CODE = PI.BRANCH_CODE) JOIN PERSONAL_INFO PIB ON PIB.CUSTOMER_ID = AB.BROKER_ID AND"
				+ " PIB.AMEND_ID =(SELECT   MAX (AMEND_ID) FROM   PERSONAL_INFO WHERE   CUSTOMER_ID = PIB.CUSTOMER_ID"
				+ " AND BRANCH_CODE = PIB.BRANCH_CODE) JOIN CURRENCY_MASTER CM ON CM.CURRENCY_ID = AB.CURRENCY_ID "
				+ "WHERE AB.TRANS_TYPE = ? AND AB.PAID_AMT = ? AND AB.CURRENCY_ID = ? AND AB.BRANCH_CODE = ? AND "
				+ "PI.BRANCH_CODE = ? AND PIB.BRANCH_CODE = ? AND CM.BRANCH_CODE = ?  AND CM.STATUS = 'Y' AND "
				+ "TRUNC (CM.EFFECTIVE_DATE) <= TRUNC (SYSDATE) AND CM.AMEND_ID =(SELECT   MAX (AMEND_ID) FROM"
				+ "   CURRENCY_MASTER WHERE CURRENCY_ID = CM.CURRENCY_ID AND BRANCH_CODE = CM.BRANCH_CODE AND "
				+ "STATUS = 'Y' AND TRUNC (EFFECTIVE_DATE) <= TRUNC (SYSDATE)) AND AB.PAYMENT_RECEIPT_NO =? AND "
				+ "AB.STATUS =?\r\n");
		
		
		for(int i=1; i<=9;i++)
			q.setParameter(i, args[i-1]);
		q.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		list =  q.getResultList();
		return list;
	}

	@Override
	public List<Map<String, Object>> getReversalInfoTreasury(String[] args) {
		List<Map<String, Object>> list;
		//LEFT OUTER JOIN
		Query q = em.createNativeQuery("SELECT   DECODE (PI.COMPANY_NAME, 'DIRECT', NULL, PI.COMPANY_NAME)COMPANY_NAME,"
				+ " PIB.FIRST_NAME || ' ' || PIB.LAST_NAME broker, AB.BROKER_ID, CM.SHORT_NAME CURRENCY_NAME,"
				+ "AB.EXCHANGE_RATE,AB.PAYMENT_RECEIPT_NO, AB.PAID_AMT, Ab.CEDING_ID, AB.STATUS From  "
				+ "(Select A.Amend_id,A.STATUS,A.BRANCH_CODE,A.EXCHANGE_RATE, A.PAYMENT_RECEIPT_NO,A.CEDING_ID,"
				+ "A.BROKER_ID,A.PAID_AMT,A.TRANS_TYPE,A.CURRENCY_ID from TTRN_PAYMENT_RECEIPT A LEFT OUTER"
				+ " JOIN(  SELECT   RECEIPT_SL_NO, NVL (SUM (ALLOCATED_TILL_DATE), '0') ALLOCATED_TILL_DATE FROM   "
				+ "TTRN_PAYMENT_RECEIPT_DETAILS GROUP BY   RECEIPT_SL_NO) B ON A.PAYMENT_RECEIPT_NO = b.RECEIPT_SL_NO"
				+ " AND B.ALLOCATED_TILL_DATE = 0 Where  A.AMEND_ID =(SELECT   MAX (AMEND_ID) FROM "
				+ "  TTRN_PAYMENT_RECEIPT WHERE   PAYMENT_RECEIPT_NO = A.PAYMENT_RECEIPT_NO AND"
				+ " BRANCH_CODE = A.BRANCH_CODE)) AB JOIN PERSONAL_INFO PI ON PI.CUSTOMER_ID = CASE WHEN"
				+ " AB.BROKER_ID = 63 THEN AB.CEDING_ID ELSE 63 END AND PI.AMEND_ID = (SELECT   MAX (AMEND_ID) FROM "
				+ "  PERSONAL_INFO WHERE   CUSTOMER_ID = PI.CUSTOMER_ID AND BRANCH_CODE = PI.BRANCH_CODE) JOIN "
				+ "PERSONAL_INFO PIB ON PIB.CUSTOMER_ID = AB.BROKER_ID AND PIB.AMEND_ID =(SELECT   MAX (AMEND_ID) FROM "
				+ "  PERSONAL_INFO WHERE   CUSTOMER_ID = PIB.CUSTOMER_ID AND BRANCH_CODE = PIB.BRANCH_CODE) JOIN"
				+ " CURRENCY_MASTER CM ON CM.CURRENCY_ID = AB.CURRENCY_ID WHERE AB.TRANS_TYPE = ? AND AB.PAID_AMT = ? "
				+ "AND AB.CURRENCY_ID = ? AND AB.BRANCH_CODE = ? AND PI.BRANCH_CODE = ? AND PIB.BRANCH_CODE = ? AND"
				+ " CM.BRANCH_CODE = ? AND AB.BROKER_ID = ? And Decode(?,'-1','A', AB.CEDING_ID)=Decode(?,'-1','A',?)"
				+ " AND CM.STATUS = 'Y' AND TRUNC (CM.EFFECTIVE_DATE) <= TRUNC (SYSDATE) AND CM.AMEND_ID =(SELECT  "
				+ " MAX (AMEND_ID) FROM   CURRENCY_MASTER WHERE CURRENCY_ID = CM.CURRENCY_ID AND BRANCH_CODE = "
				+ "CM.BRANCH_CODE AND STATUS = 'Y' AND TRUNC (EFFECTIVE_DATE) <= TRUNC (SYSDATE)) AND AB.STATUS =?");
		
		for (int i = 1; i <= args.length; i++)
			q.setParameter(i, args[i - 1]);
		q.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		list = q.getResultList();
		return list;
	}

	@Override
	public Integer getAllocationStatus(String branchCode, String paymentReceiptNo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
		Root<TtrnAllocatedTransaction> root = cq.from(TtrnAllocatedTransaction.class);
		
		cq.multiselect(cb.count(root.get("sno"))).where(cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("receiptNo"), paymentReceiptNo), cb.equal(root.get("status"), "Y"));
		
		return em.createQuery(cq).getFirstResult();
		
	}
	
	@Override
	@Transactional
	public Integer updatePremiumDetails(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);


		Double allocated = getAllocatedTillDate(args[3], args[4], "RskPremiumDetails") + Double.parseDouble(args[0]);
		update.set(root.get("allocatedTillDate"), allocated).set(root.get("loginId"), args[1]).set(root.get("branchCode"), args[2])
		.set(root.get("entryDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		
		update.where(cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("transactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updatepreSetStatus(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		Root<RskPremiumDetails> root = update.from(RskPremiumDetails.class);

		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("netdueOc")), 0.0)
					.otherwise(root.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
					.otherwise(root.<Double>get("allocatedTillDate")));
		
		update.set(root.get("settlementStatus"), args[0])
		.where(cb.equal(exp, 0.0),
			   cb.equal(root.get("contractNo"), args[3]),  cb.equal(root.get("branchCode"), args[2]),  cb.equal(root.get("loginId"), args[1]),
			   cb.equal(root.get("transactionNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateclaimPymtAlloDtls(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);

		Double allocated = getAllocatedTillDate(args[3], args[4], "TtrnClaimPayment") + Double.parseDouble(args[0]);
		
		update.set(root.get("allocatedTillDate"), allocated)
				.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[1]).set(root.get("loginId"), args[2]);

		update.where(cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("claimPaymentNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}

	@Override
	@Transactional
	public Integer updateclaimSetStatus(String[] args) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<TtrnClaimPayment> update = cb.createCriteriaUpdate(TtrnClaimPayment.class);
		Root<TtrnClaimPayment> root = update.from(TtrnClaimPayment.class);

		Expression<Double> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(root.<Double>get("paidAmountOc")), 0.0)
				.otherwise(root.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(root.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(root.<Double>get("allocatedTillDate")));
		
		update.set(root.get("settlementStatus"),  args[0])
				.set(root.get("sysDate"), new java.sql.Date(Calendar.getInstance().getTime().getTime()))
				.set(root.get("branchCode"), args[1]).set(root.get("loginId"), args[2]);

		update.where(cb.equal(exp, 0.0),cb.equal(root.get("contractNo"), args[3]), cb.equal(root.get("claimPaymentNo"), args[4]));

		Query q = em.createQuery(update);
		return q.executeUpdate();
	}
	public java.util.Date parseDateLocal(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("DD/MM/YYYY");
		return sdf1.parse(input);
	}

	@Override
	public String getAmend(String paymentReceiptNo,String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> root = cq.from(TtrnPaymentReceipt.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.multiselect(root.get("amendmentDate").alias("amendmentDate")).where(
				cb.equal(root.get("paymentReceiptNo"), paymentReceiptNo), cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("amendId"), sq));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList != null && receiptList.get(0).get("amendmentDate")!=null ? sdf.format(receiptList.get(0).get("amendmentDate")) : null;
	}

	@Override
	public String getTrans(String paymentReceiptNo,String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TtrnPaymentReceipt> root = cq.from(TtrnPaymentReceipt.class);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<TtrnPaymentReceipt> subRoot = sq.from(TtrnPaymentReceipt.class);

		sq.select(cb.max(subRoot.get("amendId"))).where(
				cb.equal(subRoot.get("paymentReceiptNo"), root.get("paymentReceiptNo")),
				cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

		cq.multiselect(root.get("transDate").alias("transDate")).where(
				cb.equal(root.get("paymentReceiptNo"), paymentReceiptNo), cb.equal(root.get("branchCode"), branchCode),
				cb.equal(root.get("amendId"), sq));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> receiptList = q.getResultList();
		return receiptList != null && receiptList.get(0).get("transDate")!=null ? sdf.format(receiptList.get(0).get("transDate")) : null;
	}
}
