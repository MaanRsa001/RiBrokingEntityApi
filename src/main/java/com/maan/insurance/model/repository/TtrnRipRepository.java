package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRip;

public interface TtrnRipRepository extends JpaRepository<TtrnRip,String> , JpaSpecificationExecutor<TtrnRip> {

	void deleteByProposalNoAndBranchCode(String proposalNo, String branchCode);

	void deleteByReferenceNoAndBranchCode(String referenceNo, String branchCode);

	void deleteByProposalNoAndAmendIdAndBranchCode(String proposalNo, BigDecimal bigDecimal, String branchCode);

}
