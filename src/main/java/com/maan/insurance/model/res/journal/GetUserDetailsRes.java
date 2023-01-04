package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.PremiumEditRes;
import com.maan.insurance.model.res.premium.PremiumEditRes1;

import lombok.Data;
@Data
public class GetUserDetailsRes {
	@JsonProperty("Result")
	private GetUserDetailsRes1 commonResponse;
	
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
