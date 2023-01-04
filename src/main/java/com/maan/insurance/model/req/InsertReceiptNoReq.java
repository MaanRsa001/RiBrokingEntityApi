package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertReceiptNoReq {
	
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("ProId")
	private String proId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("CancelType")
	private String cancelType;
	
	
}
