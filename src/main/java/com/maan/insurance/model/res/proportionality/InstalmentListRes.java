package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalmentListRes {
	@JsonProperty("DateList")
	private String dateList;
	
	@JsonProperty("PremiumList")
	private String premiumList;
	@JsonProperty("Paymentdays")
	private String paymentdays;
}
