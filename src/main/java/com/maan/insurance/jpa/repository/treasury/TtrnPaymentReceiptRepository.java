package com.maan.insurance.jpa.repository.treasury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;

@Repository
public interface TtrnPaymentReceiptRepository
		extends JpaRepository<TtrnPaymentReceipt, Long> {

}
