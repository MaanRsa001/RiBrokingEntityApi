package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRiPlacementStatus;
import com.maan.insurance.model.entity.TtrnRiPlacementStatusId;

public interface TtrnRiPlacementStatusRepository extends JpaRepository<TtrnRiPlacementStatus,TtrnRiPlacementStatusId > , JpaSpecificationExecutor<TtrnRiPlacementStatus> {

	TtrnRiPlacementStatus findTop1ByProposalNoAndReinsurerIdAndBrokerIdOrderByAmendIdDesc(BigDecimal bigDecimal,
			String reinsurerId, String brokerId);

}
