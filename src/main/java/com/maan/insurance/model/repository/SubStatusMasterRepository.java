package com.maan.insurance.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.SubStatusMaster;
import com.maan.insurance.model.entity.SubStatusMasterId;

public interface SubStatusMasterRepository extends JpaRepository<SubStatusMaster,SubStatusMasterId > , JpaSpecificationExecutor<SubStatusMasterId> {

	List<SubStatusMaster> findByBranchCodeAndStatusAndStatusCode(String branchCode, String string, String statusCode);

	List<SubStatusMaster> findByApprovelYN(String string);

}
