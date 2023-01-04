package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocatedStatusRes {
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("Allocated")
	private String allocated;
	
	@JsonProperty("Utilized")
	private String utilized;
	
	@JsonProperty("NotUtilized")
	private String notUtilized;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("PaymentDate")
	private String paymentDate;
	
	@JsonProperty("Bank")
	private String bank;
	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;

}
