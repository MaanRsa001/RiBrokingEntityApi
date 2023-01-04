package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsId;
import com.maan.insurance.model.entity.TtrnRetroClaimPayment;

public interface TtrnRetroClaimPaymentRepository extends JpaRepository<TtrnRetroClaimPayment,BigDecimal > , JpaSpecificationExecutor<BigDecimal> {

	List<TtrnRetroClaimPayment> findByClaimNoAndContractNo(String claimNo, String policyContractNo);

}
