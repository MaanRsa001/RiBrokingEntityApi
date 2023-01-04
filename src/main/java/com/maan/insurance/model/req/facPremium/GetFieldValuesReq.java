package com.maan.insurance.model.req.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetFieldValuesReq {
	@JsonProperty("PreCurrencylist")
	private List<PreCurrencylist> preCurrencylist;
	
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	@JsonProperty("Mdpremium")
	private String mdpremium;
	@JsonProperty("Adjustmentpremium")
	private String adjustmentpremium;
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("BonusId")
	private String bonusId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	
}
