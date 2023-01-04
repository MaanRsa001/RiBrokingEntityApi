package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.TtrnRetroClaimDetails;
import com.maan.insurance.model.entity.TtrnRetroClaimDetailsId;

@Repository
public interface TtrnRetroClaimDetailsRepository  extends JpaRepository<TtrnRetroClaimDetails,TtrnRetroClaimDetailsId > , JpaSpecificationExecutor<TtrnRetroClaimDetails> {

	TtrnRetroClaimDetails findByContractNoAndClaimNo(String contractNo, BigDecimal bigDecimal);

	TtrnRetroClaimDetails findByContractNoAndClaimNoAndLayerNo(String contractNo, BigDecimal bigDecimal,
			BigDecimal bigDecimal2);

}
