package com.maan.insurance.model.res.nonproportionality;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalmentListRes1 {
	@JsonProperty("DateList")
	private String dateList;
	@JsonProperty("PremiumList")
	private String premiumList;
	@JsonProperty("Paymentdays")
	private String paymentdays;
}
