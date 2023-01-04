package com.maan.insurance.model.req.facPremium;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AddFieldValueReq {
	@JsonProperty("BonusExchangeRate")
	private String bonusExchangeRate;
	
	@JsonProperty("PremiumFinallist2")
	private List<Map<String, Object>> premiumFinallist2;
	@JsonProperty("CurrencyShortName")
	private List<CurrencyShortName> currencyShortName;
	@JsonProperty("Transaction")
	private String transaction;
}
