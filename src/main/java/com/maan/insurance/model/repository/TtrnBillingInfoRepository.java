package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingInfoId;

public interface TtrnBillingInfoRepository extends JpaRepository<TtrnBillingInfo,TtrnBillingInfoId> , JpaSpecificationExecutor<TtrnBillingInfo> {

	TtrnBillingInfo findByBillingNoAndBranchCode(BigDecimal bigDecimal,String branchCode);

}
