package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumDetailsReq {
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("TableType")
	private String tableType;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CountryId")
	private String countryId;
}
