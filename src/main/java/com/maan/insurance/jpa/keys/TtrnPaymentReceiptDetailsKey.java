package com.maan.insurance.jpa.keys;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TtrnPaymentReceiptDetailsKey implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7729422246615599555L;
	
	private BigDecimal receiptNo;
	
	private BigDecimal receiptSlNo;
	
	private BigDecimal amendId;

}
