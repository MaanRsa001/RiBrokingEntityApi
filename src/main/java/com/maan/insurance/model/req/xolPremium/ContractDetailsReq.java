package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ContractDetailsReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
}
