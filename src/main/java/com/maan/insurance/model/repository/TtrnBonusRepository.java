package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;

public interface TtrnBonusRepository extends JpaRepository<TtrnBonus,BigDecimal> , JpaSpecificationExecutor<TtrnBonus> {

	@Transactional
	void deleteByProposalNoAndBranchAndTypeAndLayerNo(BigDecimal bigDecimal, String branchCode, String type,
			String layerNo);

	void deleteByProposalNoAndEndorsementNoAndBranchAndTypeAndLayerNo(BigDecimal bigDecimal, BigDecimal bigDecimal2,
			String branchCode, String type, String layerNo);

	int countByProposalNoAndBranchAndTypeAndEndorsementNoAndLayerNoAndLcbFromNotNull(BigDecimal bigDecimal,
			String string, String string2, BigDecimal bigDecimal2, String string3);

	void deleteByReferenceNoAndBranchAndTypeAndLayerNo(BigDecimal bigDecimal, String string, String string2,
			String string3);

	TtrnBonus findTop1ByBranchOrderBySnoDesc(String branchCode);

}
