package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.SessionMaster;
import com.maan.insurance.model.entity.TtrnBonus;

public interface SessionMasterRepository extends JpaRepository< SessionMaster,String> , JpaSpecificationExecutor< SessionMaster> {

	List<SessionMaster> findByBranchCodeAndLogoutDtIsNullAndLoginIdNotIn(String branchCode, List<String> loginId);

}
