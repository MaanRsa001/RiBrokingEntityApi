package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes1;
import com.maan.insurance.model.res.facultative.UwList;

import lombok.Data;

@Data
public class RetroDetails {
	
	@JsonProperty("RetroTypeValList")
	private String retroTypeValList;
	
	@JsonProperty("UwYearValList")
	private String uwYearValList;
	
	@JsonProperty("CedingCompanyValList")
	private String cedingCompanyValList;
	
	@JsonProperty("RetroPercentage")
	private String retroPercentage;

}
