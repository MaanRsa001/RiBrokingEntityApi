package com.maan.insurance.model.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
@Repository
public interface RskPremiumDetailsTempRepository
		extends JpaRepository<RskPremiumDetailsTemp, Long> {

	RskPremiumDetailsTemp findByRequestNoAndBranchCode(BigDecimal bigDecimal,
			String branchCode);

	List<RskPremiumDetailsTemp> findByRequestNo(BigDecimal bigDecimal);

	List<RskPremiumDetailsTemp> findByRequestNoAndBranchCodeOrderByEntryDateTimeDesc(BigDecimal bigDecimal,
			String branchCode);

	RskPremiumDetailsTemp findByRequestNoAndBranchCode(String requestNo, String branchCode);

	RskPremiumDetailsTemp findByContractNoAndRequestNo(BigDecimal bigDecimal, BigDecimal bigDecimal2);

}
