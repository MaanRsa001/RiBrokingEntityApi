/*
 * Java domain class for entity "TmasHomepageMaster" 
 * Created on 2022-09-15 ( Date ISO 2022-09-15 - Time 15:42:29 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */
 
 /*
 * Created on 2022-09-15 ( 15:42:29 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TmasHomepageMaster;
import com.maan.insurance.model.entity.TmasHomepageMasterId;
/**
 * <h2>TmasHomepageMasterRepository</h2>
 *
 * createdAt : 2022-09-15 - Time 15:42:29
 * <p>
 * Description: "TmasHomepageMaster" Repository
 */
 
 
 
public interface TmasHomepageMasterRepository  extends JpaRepository<TmasHomepageMaster,TmasHomepageMasterId > , JpaSpecificationExecutor<TmasHomepageMaster> {

	List<TmasHomepageMaster> findByStatusAndProductIdAndDeptIdAndBranchCodeOrderByOrderNoAsc(String string,
			BigDecimal bigDecimal, BigDecimal bigDecimal2, String branchCode);

	List<TmasHomepageMaster> findByProductIdAndDeptIdAndProcessIdAndBranchCodeAndStatus(BigDecimal bigDecimal,
			BigDecimal bigDecimal2, BigDecimal bigDecimal3, String branchCode, String string);

}
