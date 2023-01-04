package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyListRes1 {
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ShortName")
	private String shortName;
}
