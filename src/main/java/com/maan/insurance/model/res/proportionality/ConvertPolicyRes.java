package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetSectionDuplicationCheckReq;

import lombok.Data;

@Data
public class ConvertPolicyRes {
	@JsonProperty("Result")
	private ConvertPolicyRes1 commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
