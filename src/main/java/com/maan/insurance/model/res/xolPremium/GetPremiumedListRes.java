package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes1;
import com.maan.insurance.model.res.premium.GetRetroContractsRes1;

import lombok.Data;

@Data
public class GetPremiumedListRes {
	@JsonProperty("Result")
	private List<GetPremiumedListRes1> commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
