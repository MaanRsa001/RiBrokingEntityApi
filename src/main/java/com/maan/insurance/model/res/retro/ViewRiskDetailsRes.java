package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.InstalmentListRes1;
import com.maan.insurance.model.res.nonproportionality.MappingProposalRes;
import com.maan.insurance.model.res.retro.ViewRiskDetailsRes1;

import lombok.Data;

@Data
public class ViewRiskDetailsRes {
	@JsonProperty("Result")
	private ViewRiskDetailsRes1 commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
