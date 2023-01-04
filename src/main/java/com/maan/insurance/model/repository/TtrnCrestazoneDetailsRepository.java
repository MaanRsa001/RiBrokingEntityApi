package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskRemarks;

public interface TtrnCrestazoneDetailsRepository extends JpaRepository<TtrnCrestazoneDetails,BigDecimal> , JpaSpecificationExecutor<TtrnCrestazoneDetails> {

	int countByProposalNoAndAmendIdAndBranchCode(BigDecimal bigDecimal, String amend, String branchCode);

	TtrnCrestazoneDetails findByProposalNoAndAmendIdAndBranchCode(BigDecimal bigDecimal, String amendId,
			String branchCode);

}
