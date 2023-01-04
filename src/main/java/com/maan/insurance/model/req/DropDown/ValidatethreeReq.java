package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ValidatethreeReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("AccountDate")
	private String accountDate;
}
