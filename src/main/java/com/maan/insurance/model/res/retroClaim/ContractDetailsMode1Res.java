package com.maan.insurance.model.res.retroClaim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;
import com.maan.insurance.model.res.retro.ShowSecondpageEditItemsRes1;

import lombok.Data;

@Data
public class ContractDetailsMode1Res {
	@JsonProperty("Result")
	private ContractDetailsMode1Res1 commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
