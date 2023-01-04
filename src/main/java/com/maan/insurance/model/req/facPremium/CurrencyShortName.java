package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyShortName {
	@JsonProperty("CurrencyShortName")
	private String currencyShortName;
}
