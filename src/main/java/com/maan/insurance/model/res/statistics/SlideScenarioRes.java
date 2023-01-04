package com.maan.insurance.model.res.statistics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class SlideScenarioRes {
	@JsonProperty("Result")
	private SlideScenarioRes1 commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
