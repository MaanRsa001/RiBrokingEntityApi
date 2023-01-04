package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsRes1;

import lombok.Data;

@Data
public class GetPremiumDetailsRes {
	@JsonProperty("Result")
	private GetPremiumDetailsResponse commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
