package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetLowClaimBonusListRes1;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes1;

import lombok.Data;

@Data
public class GetInsurarerDetailsRes {
	@JsonProperty("Result")
	private List<GetInsurarerDetailsRes1> commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
