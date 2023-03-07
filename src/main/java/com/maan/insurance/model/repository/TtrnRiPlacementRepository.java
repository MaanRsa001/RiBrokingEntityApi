package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiPlacementId;

public interface TtrnRiPlacementRepository  extends JpaRepository<TtrnRiPlacement,TtrnRiPlacementId > , JpaSpecificationExecutor<TtrnRiPlacement> {

	int countByProposalNo(BigDecimal bigDecimal);


	int countByBranchCodeAndProposalNoAndStatusNot(String branchCode, BigDecimal bigDecimal, String string);



	int countByBranchCodeAndBouquetNoAndStatusNot(String branchCode, BigDecimal bigDecimal, String string);


	TtrnRiPlacement findDistinctByBouquetNo(BigDecimal bigDecimal);


	TtrnRiPlacement findDistinctByBaseProposalNo(BigDecimal bigDecimal);


	TtrnRiPlacement findDistinctByProposalNo(BigDecimal bigDecimal);


	TtrnRiPlacement findTop1ByBranchCodeAndProposalNoAndReinsurerIdAndBrokerIdOrderByPlacementAmendIdDesc(
			String branchCode, BigDecimal bigDecimal, String reinsurerId, String brokerId);


	TtrnRiPlacement findByProposalNoAndReinsurerIdAndBrokerIdAndBranchCodeAndStatusNo(BigDecimal bigDecimal,
			String reinsurerIds, String brokerIds, String branchCode, BigDecimal bigDecimal2);




	List<TtrnRiPlacement> findByBouquetNoAndContractNoIsNotNullAndContractNoNot(BigDecimal bigDecimal, BigDecimal zero);


	List<TtrnRiPlacement> findBybaseProposalNoAndContractNoIsNotNullAndContractNoNot(BigDecimal bigDecimal,
			BigDecimal zero);


	List<TtrnRiPlacement> findByProposalNoAndContractNoIsNotNullAndContractNoNot(BigDecimal bigDecimal,
			BigDecimal zero);


	List<TtrnRiPlacement> findByProposalNo(BigDecimal bigDecimal);

}
