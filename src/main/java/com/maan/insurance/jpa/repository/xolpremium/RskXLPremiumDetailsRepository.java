package com.maan.insurance.jpa.repository.xolpremium;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.xolpremium.RskXLPremiumDetails;

@Repository
public interface RskXLPremiumDetailsRepository
		extends JpaRepository<RskXLPremiumDetails, Long> {

}
