package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumEditResponse {
	@JsonProperty("EditPremium")
	private List<PremiumEditRes1> editPremium;
	@JsonProperty("PremiumEdit")
	private PremiumEditRes2 sum;
}
