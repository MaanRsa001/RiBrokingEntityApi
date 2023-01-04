package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReciptViewRes {
	@JsonProperty("Payres")
	private String payres;
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("Exrate")
	private String exrate;
	@JsonProperty("Inceptiobdate")
	private String inceptiobdate;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("TotAmount")
	private String totAmount;
	@JsonProperty("SetExcRate")
	private String setExcRate;
	@JsonProperty("ConRecCur")
	private String conRecCur;
	@JsonProperty("HideRowCnt")
	private String hideRowCnt;

}
