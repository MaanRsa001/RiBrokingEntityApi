package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.res.retro.RiskDetailsEditModeRes1;

import lombok.Data;

@Data
public class RiskDetailsEditModeRes {

	@JsonProperty("Result")
	private RiskDetailsEditModeRes1 commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;

}
