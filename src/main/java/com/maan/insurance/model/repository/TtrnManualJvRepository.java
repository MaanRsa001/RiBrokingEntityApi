package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.ConstantDetailId;
import com.maan.insurance.model.entity.TtrnManualJv;

public interface TtrnManualJvRepository  extends JpaRepository<TtrnManualJv,BigDecimal > , JpaSpecificationExecutor<TtrnManualJv> {

	String findTop1ByBranchCodeAndTransactionNoOrderByAmendIdDesc(String branchCode, String tranId);

}
