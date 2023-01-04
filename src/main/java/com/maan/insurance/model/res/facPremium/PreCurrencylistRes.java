package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facPremium.PreCurrencylist;

import lombok.Data;

@Data
public class PreCurrencylistRes {
	@JsonProperty("Paiddate")
	private String paiddate;
	@JsonProperty("Pretotal")
	private String pretotal;
	@JsonProperty("Claimout")
	private String claimout;
	@JsonProperty("Claimamt")
	private String claimamt;
	@JsonProperty("Premiumamt")
	private String premiumamt;
	@JsonProperty("Total")
	private String total;
}
