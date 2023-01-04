package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractNoReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ContractNo")
	private String contractNo;
}
