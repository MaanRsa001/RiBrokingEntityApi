package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TmasTerritory;
import com.maan.insurance.model.entity.TtrnRiskRemarks;

public interface TmasTerritoryRepository extends JpaRepository<TmasTerritory,BigDecimal> , JpaSpecificationExecutor<TmasTerritory> {


	List<TmasTerritory> findByBranchCodeAndTerritoryIdIn(String branchCode, List<BigDecimal> territory1);

}
