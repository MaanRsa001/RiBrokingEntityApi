package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class LedgerdetailListRes {
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("LedClass")
	private String ledClass;
	@JsonProperty("Narration")
	private String narration;
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("ReversalStatus")
	private String reversalStatus;
	
	@JsonProperty("LedgerId")
	private String ledgerId;
	@JsonProperty("DebitOC")
	private String debitOC;
	@JsonProperty("CreditOC")
	private String creditOC;
	@JsonProperty("DebitDC")
	private String debitDC;
	@JsonProperty("CreditDC")
	private String creditDC; 

}
