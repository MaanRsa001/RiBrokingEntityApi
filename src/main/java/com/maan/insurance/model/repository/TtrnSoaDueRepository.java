package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnSoaDue;
import com.maan.insurance.model.entity.TtrnSoaDueId;

public interface TtrnSoaDueRepository extends JpaRepository<TtrnSoaDue,TtrnSoaDueId> , JpaSpecificationExecutor<TtrnSoaDue> {

	void deleteByProposalNoAndContractNoAndBranchCode(BigDecimal bigDecimal, BigDecimal bigDecimal2, String branchCode);

}
