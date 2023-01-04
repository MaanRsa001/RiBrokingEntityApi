package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusdetailsRes1 {
	@JsonProperty("ExchRatePrem")
	private List<String> exchRatePrem;
	@JsonProperty("CurrencyShortName")
	private List<String> currencyShortName;
	@JsonProperty("PreCurrencylist")
	private List<String> preCurrencylist;
	@JsonProperty("BonusRes")
	private List<BonusRes> bonusRes;
	@JsonProperty("GridShow")
	private String gridShow;
}
