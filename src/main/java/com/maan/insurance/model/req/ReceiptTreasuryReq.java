package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiptTreasuryReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Payrecno")
	private String payrecno;
	
}
