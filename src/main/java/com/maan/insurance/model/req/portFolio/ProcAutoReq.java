package com.maan.insurance.model.req.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProcAutoReq {
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("PortfolioListReq")
	private  List<PortfolioListReq> PortfolioListReq;
	@JsonProperty("CountryId")
	private String countryId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SettlementStatus")
	private String settlementStatus;
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("TransactionError")
	private String transactionError;
}
