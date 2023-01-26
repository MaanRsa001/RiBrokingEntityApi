package com.maan.insurance.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.model.entity.TtrnBillingTransactionId;

public interface TtrnBillingTransactionRepository extends JpaRepository<TtrnBillingTransaction,TtrnBillingTransactionId > , JpaSpecificationExecutor<TtrnBillingTransaction> {


}
