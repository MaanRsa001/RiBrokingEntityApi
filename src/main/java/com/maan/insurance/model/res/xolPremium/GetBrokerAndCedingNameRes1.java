package com.maan.insurance.model.res.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetBrokerAndCedingNameRes1 {
	@JsonProperty("Code")
	private String cutomerId;
	@JsonProperty("CodeDescription")
	private String companyName;
	@JsonProperty("CodeValue")
	private String address;
}
