package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TmasCrestaMaster;

public interface TmasCrestaMasterRepository extends JpaRepository<TmasCrestaMaster,BigDecimal > , JpaSpecificationExecutor<TmasCrestaMaster> {

	List<TmasCrestaMaster> findByBranchCodeAndStatusAndTerritoryCode(String branchCode, String string, Object object);

	List<TmasCrestaMaster> findByBranchCodeAndStatusAndCrestaId(String branchCode, String string, Object object);

}
