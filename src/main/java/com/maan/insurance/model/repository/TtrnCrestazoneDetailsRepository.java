package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskRemarks;

public interface TtrnCrestazoneDetailsRepository extends JpaRepository<TtrnCrestazoneDetails,BigDecimal> , JpaSpecificationExecutor<TtrnCrestazoneDetails> {

	int countByProposalNoAndAmendIdAndBranchCode(BigDecimal bigDecimal, String amend, String branchCode);

	TtrnCrestazoneDetails findByProposalNoAndAmendIdAndBranchCode(BigDecimal bigDecimal, String amendId,
			String branchCode);

	List<TtrnCrestazoneDetails> findByProposalNoAndAmendIdAndBranchCodeOrderBySno(BigDecimal bigDecimal, String amendId,
			String branchCode);

	void deleteByProposalNoAndAmendIdAndBranchCode(BigDecimal bigDecimal, String amendId, String branchCode);

//	List<TtrnCrestazoneDetails> findByProposalNoAndAmendIdAndBranchCodeOrderBySNo(BigDecimal bigDecimal, String amendId,
//			String branchCode);

}
