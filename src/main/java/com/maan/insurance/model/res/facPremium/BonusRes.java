package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusRes {
	@JsonProperty("ShortName")
	private String shortName;
	@JsonProperty("CurrencyId")
	private String currencyId;
}
