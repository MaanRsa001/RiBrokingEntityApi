package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnRip;
import com.maan.insurance.model.entity.TtrnRipId;

public interface TtrnRipRepository extends JpaRepository<TtrnRip,TtrnRipId> , JpaSpecificationExecutor<TtrnRip> {

	void deleteByProposalNoAndBranchCode(String proposalNo, String branchCode);

	void deleteByReferenceNoAndBranchCode(String referenceNo, String branchCode);

	void deleteByProposalNoAndAmendIdAndBranchCode(String proposalNo, BigDecimal bigDecimal, String branchCode);

	TtrnRip findTop1ByBranchCodeOrderBySnoDesc(String branchCode);

	int countByProposalNo(String proposalNo);

}
