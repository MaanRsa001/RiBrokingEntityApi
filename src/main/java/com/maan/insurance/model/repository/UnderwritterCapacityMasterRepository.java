package com.maan.insurance.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.UnderwritterCapacityMaster;

public interface UnderwritterCapacityMasterRepository extends JpaRepository<UnderwritterCapacityMaster,String > , JpaSpecificationExecutor<UnderwritterCapacityMaster> {

	UnderwritterCapacityMaster findByUnderwriteridAndProductIdAndDepartmentidAndStatus(String uwName, String productId,
			String deptId, String string);

}
