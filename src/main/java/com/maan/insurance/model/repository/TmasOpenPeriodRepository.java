package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TmasPfcMasterId;

public interface TmasOpenPeriodRepository extends JpaRepository<TmasOpenPeriod,BigDecimal > , JpaSpecificationExecutor<BigDecimal> {



	List<TmasOpenPeriod> findByBranchCodeOrderBySnoDesc(String branchCode);



	List<TmasOpenPeriod> findByStartDateAndEndDateAndBranchCode(Date parse, Date parse2, String branchCode);






	int countBySnoLessThanAndBranchCodeAndStatus(BigDecimal bigDecimal, String branchCode, String string);



	List<TmasOpenPeriod> findByStartDateAndBranchCode(Date parse, String branchCode);



	List<TmasOpenPeriod> findByEndDateAndBranchCode(Date parse, String branchCode);

}
