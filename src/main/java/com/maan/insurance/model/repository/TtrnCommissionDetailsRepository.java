package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCommissionDetailsId;

@Transactional
public interface TtrnCommissionDetailsRepository extends JpaRepository<TtrnCommissionDetails,TtrnCommissionDetailsId> , JpaSpecificationExecutor<TtrnCommissionDetails> {

	void deleteByProposalNoAndBranchCodeAndEndorsementNo(BigDecimal bigDecimal, String branchCode,
			BigDecimal bigDecimal2);

	void deleteByProposalNoAndBranchCodeAndEndorsementNoAndContractNo(BigDecimal bigDecimal, String branchCode,
			BigDecimal bigDecimal2, BigDecimal bigDecimal3);

	TtrnCommissionDetails findByProposalNoAndBranchCodeAndEndorsementNo(BigDecimal bigDecimal, String branchCode,
			BigDecimal bigDecimal2);

	TtrnCommissionDetails findTop1ByBranchCodeOrderBySerialNoDesc(String branchCode);

	void deleteByReferenceNoAndBranchCodeAndEndorsementNoAndContractNo(BigDecimal bigDecimal, String branchCode,
			BigDecimal bigDecimal2, BigDecimal bigDecimal3);

}
