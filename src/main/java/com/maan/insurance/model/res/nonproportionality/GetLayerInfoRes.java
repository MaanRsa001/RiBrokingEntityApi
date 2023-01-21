package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;

import lombok.Data;

@Data
public class GetLayerInfoRes {
	@JsonProperty("Result")
	private List<GetLayerInfoRes1> commonResponse;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
