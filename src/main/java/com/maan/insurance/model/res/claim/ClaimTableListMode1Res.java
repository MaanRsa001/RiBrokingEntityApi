package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.ClaimlistRes;

import lombok.Data;

@Data
public class ClaimTableListMode1Res {

	
//	@JsonProperty("SumOfPaidAmountOC")
//	private String sumOfPaidAmountOC;
	
	@JsonProperty("Result")
	private List<ClaimlistRes> commonResponse;
	
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
//	@JsonProperty("CeaseStatus")
//	private String ceaseStatus;
}
