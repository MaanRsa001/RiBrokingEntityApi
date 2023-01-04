package com.maan.insurance.model.res.claim;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;

import lombok.Data;
@Data
public class InsertCliamDetailsMode8Res {
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<Error> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
