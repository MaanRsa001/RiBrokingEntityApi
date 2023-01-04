package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubmitPremiumReservedRes1 {
	@JsonProperty("Value1")
	private String value1;
	@JsonProperty("Value2")
	private String value2;
	@JsonProperty("Value3")
	private String value3;
	@JsonProperty("Value4")
	private String value4;
	
}
