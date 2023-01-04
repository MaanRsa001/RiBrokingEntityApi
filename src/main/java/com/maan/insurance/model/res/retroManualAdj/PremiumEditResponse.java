package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditRes1;

import lombok.Data;

@Data
public class PremiumEditResponse {
	@JsonProperty("PremiumEditRes1")
	private List<PremiumEditRes1> premiumEditRes1;
	@JsonProperty("SaveFlag")
	private String saveFlag;
}
