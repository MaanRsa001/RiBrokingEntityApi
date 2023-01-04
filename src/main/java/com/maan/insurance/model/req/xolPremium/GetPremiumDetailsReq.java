package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumDetailsReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("TableType")
	private String tableType;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("RequestNo")
	private String requestNo;
	
//	@JsonProperty("RdsExchageRate")
//	private String rdsExchageRate;
//	@JsonProperty("GwpiOS")
//	private String gwpiOS;




}
