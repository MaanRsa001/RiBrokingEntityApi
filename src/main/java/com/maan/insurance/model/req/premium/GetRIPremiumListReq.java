package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetRIPremiumListReq {
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ProductId")
	private String productId;
}
