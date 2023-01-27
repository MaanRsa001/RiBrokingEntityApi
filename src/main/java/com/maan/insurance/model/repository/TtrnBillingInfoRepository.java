package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingInfoId;

public interface TtrnBillingInfoRepository extends JpaRepository<TtrnBillingInfo,TtrnBillingInfoId> , JpaSpecificationExecutor<TtrnBillingInfo> {

	TtrnBillingInfo findByBillingNo(BigDecimal bigDecimal);

}
