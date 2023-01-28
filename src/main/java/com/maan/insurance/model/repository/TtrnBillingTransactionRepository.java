package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.model.entity.TtrnBillingTransactionId;

public interface TtrnBillingTransactionRepository extends JpaRepository<TtrnBillingTransaction,TtrnBillingTransactionId > , JpaSpecificationExecutor<TtrnBillingTransaction> {


	List<TtrnBillingTransaction> findByBillNo(BigDecimal bigDecimal);

	List<TtrnBillingTransaction> findByBillNoAndBranchCode(BigDecimal bigDecimal, String branchCode);

	//TtrnBillingTransaction findTop1OrderByBillSnoDesc();


}
