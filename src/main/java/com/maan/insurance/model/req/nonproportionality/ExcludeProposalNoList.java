package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExcludeProposalNoList {
	@JsonProperty("Value1")
	private String value1;
}
