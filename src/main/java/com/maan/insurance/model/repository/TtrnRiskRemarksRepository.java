package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.entity.UnderwritterMaster;

public interface TtrnRiskRemarksRepository extends JpaRepository<TtrnRiskRemarks,BigDecimal> , JpaSpecificationExecutor<TtrnRiskRemarks> {




	List<TtrnRiskRemarks> findTop1ByProposalNoOrderByAmendIdDesc(BigDecimal bigDecimal);

}
