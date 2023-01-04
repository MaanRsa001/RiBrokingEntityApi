package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes1;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditResponse;

import lombok.Data;
@Data
public class PremiumEditRes {
	@JsonProperty("Result")
	private PremiumEditResponse commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
