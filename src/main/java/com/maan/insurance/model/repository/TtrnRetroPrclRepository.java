package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.jpa.entity.xolpremium.TtrnRetroPrcl;

public interface TtrnRetroPrclRepository extends JpaRepository<TtrnRetroPrcl,BigDecimal > , JpaSpecificationExecutor<BigDecimal> {

	int countByTransactionNo(Long long1);

}
