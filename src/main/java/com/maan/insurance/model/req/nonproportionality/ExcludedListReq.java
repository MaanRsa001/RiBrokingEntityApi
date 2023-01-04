package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExcludedListReq {
	@JsonProperty("Value1")
	private String value1;
	@JsonProperty("Value2")
	private String value2;
}
