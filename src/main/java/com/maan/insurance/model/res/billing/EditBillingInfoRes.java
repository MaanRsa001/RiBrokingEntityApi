package com.maan.insurance.model.res.billing;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;

import lombok.Data;

@Data
public class EditBillingInfoRes {
	@JsonProperty("Result")
	private EditBillingInfoComRes commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
