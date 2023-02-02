package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.jpa.keys.BankMasterKey;
import com.maan.insurance.model.entity.TtrnRskClassLimits;

public interface TtrnRskClassLimitsRepository  extends JpaRepository<TtrnRskClassLimits,BigDecimal > , JpaSpecificationExecutor<TtrnRskClassLimits> {

}
