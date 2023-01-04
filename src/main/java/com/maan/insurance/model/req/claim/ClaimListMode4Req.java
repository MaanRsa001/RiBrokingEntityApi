package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode4Req {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ContractNo")
	private String contractNo;
//	@JsonProperty("ClaimPaymentNo")
//	private String claimPaymentNo;
//	@JsonProperty("LayerNo")
//	private String layerNo;
}
