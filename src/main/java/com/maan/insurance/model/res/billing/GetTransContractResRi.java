package com.maan.insurance.model.res.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.GetTransContractRes;

import lombok.Data;

@Data
public class GetTransContractResRi {
	@JsonProperty("Result")
	private List<GetTransContractRes1Ri> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
