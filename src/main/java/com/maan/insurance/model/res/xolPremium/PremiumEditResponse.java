package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumEditResponse {
	@JsonProperty("PremiumEditRes1")
	private List<PremiumEditRes1> premiumEditRes1;
	@JsonProperty("SaveFlag")
	private String saveFlag;
}
