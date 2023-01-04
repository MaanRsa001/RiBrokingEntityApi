package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrecyAmountReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("CurrValu")
	private String currValu;

}
