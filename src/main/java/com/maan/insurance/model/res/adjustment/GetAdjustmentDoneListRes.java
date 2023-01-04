package com.maan.insurance.model.res.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.retro.GetEndDateReq;
import com.maan.insurance.model.res.retro.GetRemarksDetailsRes1;

import lombok.Data;

@Data
public class GetAdjustmentDoneListRes {
	@JsonProperty("Result")
	private List<GetAdjustmentDoneListRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
