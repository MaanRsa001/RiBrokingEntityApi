package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumDetailsResponse {
	
	@JsonProperty("TreatyView")
	private List<GetPremiumDetailsRes1> treatyView;
	
	@JsonProperty("Sumofpaidpremium")
	private String sumofpaidpremium;

	@JsonProperty("CurrencyName")
	private String currencyName;
	
}
