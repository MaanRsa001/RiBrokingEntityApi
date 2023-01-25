package com.maan.insurance.model.res.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.res.portFolio.GetConfirmedListResponse;

import lombok.Data;

@Data
public class GetBillingInfoListRes {
	@JsonProperty("Result")
	private List<GetBillingInfoListRes1> commonResponse;
		
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;
	
	@JsonProperty("ErroCode")
	private int erroCode;
}
