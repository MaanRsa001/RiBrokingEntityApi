package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.RskPremiumDetailsRiId;

@Repository
public interface RskPremiumDetailsRIRepository extends  JpaRepository<RskPremiumDetailsRi,RskPremiumDetailsRiId > , JpaSpecificationExecutor<RskPremiumDetailsRi> {

	List<RskPremiumDetailsRi> findByTransactionNo(BigDecimal bigDecimal);

	RskPremiumDetailsRi findByTransactionNoAndContractNoAndReinsurerIdAndBranchCode(BigDecimal bigDecimal,
			BigDecimal bigDecimal2, String reinsurerId, String branchCode);

	List<RskPremiumDetailsRi> findByContractNo(BigDecimal bigDecimal);



	List<RskPremiumDetailsRi> findByContractNoAndTransactionNoAndBranchCode(BigDecimal bigDecimal,
			BigDecimal bigDecimal2, String branchCode);

}
