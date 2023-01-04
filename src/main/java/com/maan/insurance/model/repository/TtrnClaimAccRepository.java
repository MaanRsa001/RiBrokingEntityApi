package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.jpa.entity.claim.TtrnClaimAcc;

public interface TtrnClaimAccRepository  extends JpaRepository<TtrnClaimAcc,BigDecimal > , JpaSpecificationExecutor<TtrnClaimAcc> {

	TtrnClaimAcc findTop1ByClaimNoAndContractNoAndLayerNoAndSubClass(BigDecimal bigDecimal, String policyContractNo,
			Object object, BigDecimal bigDecimal2);

}
