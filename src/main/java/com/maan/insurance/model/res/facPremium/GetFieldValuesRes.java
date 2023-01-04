package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facPremium.GetBonusValueReq;

import lombok.Data;

@Data
public class GetFieldValuesRes {
	@JsonProperty("Result")
	private GetFieldValuesRes1 commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
