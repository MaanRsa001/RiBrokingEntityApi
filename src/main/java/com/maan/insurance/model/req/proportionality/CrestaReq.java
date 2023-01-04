package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CrestaReq {
	@JsonProperty("TerritoryCode")
	private String territoryCode;
	
	@JsonProperty("CrestaId")
	private String crestaId;

	@JsonProperty("CrestaName")
	private String crestaName;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("AccRisk")
	private String accRisk;

	@JsonProperty("AccumulationDate")
	private String accumulationDate;
	
	@JsonProperty("ScaleSNo")
	private String scaleSNo;
	
	
}
