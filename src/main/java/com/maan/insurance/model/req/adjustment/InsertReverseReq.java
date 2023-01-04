package com.maan.insurance.model.req.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertReverseReq {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ReverseDate")
	private String reverseDate;
	
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("Allocateddate")
	private String allocateddate;
}
