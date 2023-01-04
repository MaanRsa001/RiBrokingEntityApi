package com.maan.insurance.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.StatusMaster;
import com.maan.insurance.model.entity.StatusMasterId;

public interface StatusMasterRepository extends JpaRepository<StatusMaster,StatusMasterId > , JpaSpecificationExecutor<StatusMasterId> {

	List<StatusMaster> findByBranchCodeAndStatus(String branchCode, String string);

}
