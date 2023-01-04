package com.maan.insurance.jpa.repository.treasury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;

@Repository
public interface TtrnPaymentReceiptDetailsRepository
		extends JpaRepository<TtrnPaymentReceiptDetails, Long> {

}
