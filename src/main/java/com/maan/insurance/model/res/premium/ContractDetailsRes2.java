package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsRes2 {
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("Cashoc")
	private String cashoc;
	
	@JsonProperty("Cashdc")
	private String cashdc; 
}
