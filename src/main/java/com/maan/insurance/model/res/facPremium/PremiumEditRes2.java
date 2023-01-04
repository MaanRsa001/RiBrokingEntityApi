package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facPremium.PremiumEditReq;

import lombok.Data;

@Data
public class PremiumEditRes2 {
	@JsonProperty("Sumofpaidpremium")
	private String sumofpaidpremium;
	@JsonProperty("Epibalance")
	private String epibalance;

	@JsonProperty("SaveFlag")
	private String saveFlag;
}
