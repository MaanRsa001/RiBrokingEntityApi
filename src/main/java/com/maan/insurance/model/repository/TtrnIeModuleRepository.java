package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnIeModule;

public interface TtrnIeModuleRepository extends JpaRepository<TtrnIeModule,BigDecimal > , JpaSpecificationExecutor<TtrnIeModule> {

}
