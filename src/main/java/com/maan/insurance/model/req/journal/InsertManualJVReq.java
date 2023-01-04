package com.maan.insurance.model.req.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertManualJVReq {
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("TranId")
	private String tranId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("ReversalDate")
	private String reversalDate;
	@JsonProperty("LedgerIdReq")
	private List<LedgerIdReq> LedgerIdReq;
	@JsonProperty("LedClass")
	private String ledClass;
	
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("Narration")
	private String narration;
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("ExdebitDC")
	private String exdebitDC;
	@JsonProperty("ExcreditDC")
	private String excreditDC;
	@JsonProperty("Shortname")
	private String shortname;
}
