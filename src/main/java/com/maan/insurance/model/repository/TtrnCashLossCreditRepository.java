package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.jpa.entity.propPremium.TtrnCashLossCredit;

public interface TtrnCashLossCreditRepository extends JpaRepository<TtrnCashLossCredit,BigDecimal > , JpaSpecificationExecutor<TtrnCashLossCredit> {

	TtrnCashLossCredit findByContractNoAndTempRequestNo(String contractNo, BigDecimal bigDecimal);

}
