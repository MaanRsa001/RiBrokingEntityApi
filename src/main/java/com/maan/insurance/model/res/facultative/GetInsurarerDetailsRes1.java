package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes1;

import lombok.Data;

@Data
public class GetInsurarerDetailsRes1 {
	@JsonProperty("RetroTypeValList")
	private String retroTypeValList;
	
	@JsonProperty("UwYearValList")
	private String uwYearValList;
	
	@JsonProperty("CedingCompanyValList")
	private String cedingCompanyValList;
	
	@JsonProperty("RetroPercentage")
	private String retroPercentage;
	
	@JsonProperty("Retper")
	private String retper;
	
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroDupType")
	private String retroDupType;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;

	@JsonProperty("UwList")
	private List<UwList> uwList;
	
	@JsonProperty("RetroDupList")
	private List<UwList> retroDupList;
	
	@JsonProperty("RetrolList")
	private List<UwList> retrolList;
	
	@JsonProperty("RetroDupMode")
	private String retroDupMode;
}
