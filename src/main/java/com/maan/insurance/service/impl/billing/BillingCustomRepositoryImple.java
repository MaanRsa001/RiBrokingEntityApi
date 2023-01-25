package com.maan.insurance.service.impl.billing;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.jpa.entity.treasury.TtrnRetroSoa;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TtrnBillingDetails;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnClaimPayment;

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

		sq.select(cb.max(subRoot.get("amendId"))).where(cb.equal(subRoot.get("billingNo"), root.get("receiptNo")));
		
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
	

	

}
