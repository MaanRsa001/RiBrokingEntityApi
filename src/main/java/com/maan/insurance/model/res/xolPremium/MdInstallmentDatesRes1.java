package com.maan.insurance.model.res.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MdInstallmentDatesRes1 {
	@JsonProperty("Key1")
	private String key1;
	@JsonProperty("Value")
	private String value;
	@JsonProperty("InstallmentNo")
	private String installmentNo;
}
