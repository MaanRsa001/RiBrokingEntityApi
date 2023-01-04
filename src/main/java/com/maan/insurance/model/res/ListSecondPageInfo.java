package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ListSecondPageInfo {
	@JsonProperty("CedingCompanyValList")
	private String cedingCompanyValList;
	
	@JsonProperty("ExachangeValList")
	private String exachangeValList;
	@JsonProperty("PayamountValList")
	private String payamountValList;
	@JsonProperty("RowamountValList")
	private String rowamountValList;
	@JsonProperty("RecNoValList")
	private String recNoValList;
	@JsonProperty("SetExcRateValList")
	private String setExcRateValList;
	@JsonProperty("ConRecCurValList")
	private String conRecCurValList;

}
