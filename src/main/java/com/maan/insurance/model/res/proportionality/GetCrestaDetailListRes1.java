package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class GetCrestaDetailListRes1 {
	@JsonProperty("TerritoryId")
	private String territoryId;
	
	@JsonProperty("CrestaId")
	private String crestaId;
	
	@JsonProperty("CrestaName")
	private String crestaName;

	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("AccRisk")
	private String accRisk;
	
	@JsonProperty("AccDate")
	private String accDate;
	@JsonProperty("ScaleSNo")
	private String scaleSNo;
	
	@JsonProperty("CrestaIDList")
	private List<CommonResDropDown> crestaIDList;
	
	@JsonProperty("CrestaNameList")
	private List<CommonResDropDown> crestaNameList;
}
