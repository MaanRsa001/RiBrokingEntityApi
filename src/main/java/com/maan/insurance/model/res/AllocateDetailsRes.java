package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocateDetailsRes {
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("AllocatedDate")
	private String allocatedDate;
	
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("AdjustmentType")
	private String adjustmentType;
	
	@JsonProperty("AllTillDate")
	private String allTillDate;
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	
	@JsonProperty("Broker")
	private String broker;

}
