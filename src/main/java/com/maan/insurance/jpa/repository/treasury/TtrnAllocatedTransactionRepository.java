package com.maan.insurance.jpa.repository.treasury;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;

@Repository
public interface TtrnAllocatedTransactionRepository
		extends JpaRepository<TtrnAllocatedTransaction, Long> {

	TtrnAllocatedTransaction findByTransactionNoAndSno(BigDecimal bigDecimal, BigDecimal bigDecimal2);

	TtrnAllocatedTransaction findByReceiptNoAndSno(BigDecimal bigDecimal, BigDecimal bigDecimal2);

	List<TtrnAllocatedTransaction> findBySno(BigDecimal bigDecimal);

	int countByBranchCodeAndReceiptNoAndStatus(String string, BigDecimal bigDecimal2, String string3);

	int countByContractNoAndTransactionNoAndLayerNoAndTypeAndStatus(String contNo, BigDecimal bigDecimal, String string,
			String string2, String string3);

	int countByTransactionNoAndStatus(BigDecimal bigDecimal, String string);

}
