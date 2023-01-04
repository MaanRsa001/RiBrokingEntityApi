package com.maan.insurance.model.res.statistics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCurrencyNameRes1 {
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("SlideCurrency")
	private String slideCurrency;
	@JsonProperty("ProfitCurrency")
	private String profitCurrency;
	@JsonProperty("LossCurrency")
	private String lossCurrency;
	@JsonProperty("BonusCurrency")
	private String bonusCurrency;
	
}
