package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TmasLedgerMaster;
import com.maan.insurance.model.entity.TmasOpenPeriod;

public interface TmasLedgerMasterRepository extends JpaRepository<TmasLedgerMaster,BigDecimal > , JpaSpecificationExecutor<BigDecimal> {

	TmasLedgerMaster findByBranchCodeAndStatusAndCoaIdOrderByCoaId(String branchCode, String string,
			BigDecimal bigDecimal);

}
