package com.maan.insurance.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.Error;

import lombok.Data;

@Data
public class CommonLoginResponse {

	@JsonProperty("LoginResponse")
    private ClaimLoginResponse loginResponse;
	@JsonProperty("Errors")
    private List<Error> errors;
	
}
