package com.maan.insurance.model.req.journal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LedgerIdReq {
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
