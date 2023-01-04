package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetExchangeRateReq {
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("Countryid")
	private String countryid;
	@JsonProperty("BranchCode")
	private String branchCode;
	/*
	 * @JsonProperty("IncepDate") private String incepDate;
	 * 
	 * @JsonProperty("AccDate") private String accDate;
	 */
}
