package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CashLossmailTriggerReq {
	
	@JsonProperty("ContNo")
	private String contNo;

	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("Transaction")
	private String transaction;
	
	@JsonProperty("CurrencyId")
	private String currencyId;

}
