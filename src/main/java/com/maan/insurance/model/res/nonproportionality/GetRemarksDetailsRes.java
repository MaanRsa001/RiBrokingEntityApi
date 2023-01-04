package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RemarksReq;
import com.maan.insurance.model.res.retro.GetRemarksDetailsRes1;

import lombok.Data;

@Data
public class GetRemarksDetailsRes {
	
	
	@JsonProperty("Result")
	private List<RemarksRes> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
