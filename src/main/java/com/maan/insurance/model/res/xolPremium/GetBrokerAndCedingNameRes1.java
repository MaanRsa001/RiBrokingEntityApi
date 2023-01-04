package com.maan.insurance.model.res.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetBrokerAndCedingNameRes1 {
	@JsonProperty("CutomerId")
	private String cutomerId;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("Address")
	private String address;
}
