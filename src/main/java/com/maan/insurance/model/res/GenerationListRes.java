package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GenerationListRes {
	@JsonProperty("MaxVal")
	private String maxVal;

}
