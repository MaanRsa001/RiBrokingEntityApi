package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GenerationReq {
	
	@JsonProperty("CurrencyValList")
	private String currencyValList;
	@JsonProperty("PayamountValList")
	private String payamountValList;
	@JsonProperty("ExachangeValList")
	private String exachangeValList;
	@JsonProperty("RowamountValList")
	private String rowamountValList;
	@JsonProperty("SetExcRateValList")
	private String setExcRateValList;
	@JsonProperty("ConRecCurValList")
	private String conRecCurValList;
	@JsonProperty("RecNoValList")
	private String recNoValList;
	
	
	
	
	

}
