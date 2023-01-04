package com.maan.insurance.model.res.menu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes1;

import lombok.Data;

@Data
public class GetAdminMenuRes {
	@JsonProperty("Result")
	private List<GetAdminMenuRes1> commonResponse;
	
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
