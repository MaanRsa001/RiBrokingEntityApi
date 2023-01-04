package com.maan.insurance.model.res.journal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.journal.InsertRetroProcessReq;

import lombok.Data;

@Data
public class InsertManualJVRes1 {
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("TranId")
	private String tranId;
	@JsonProperty("ReversalNo")
	private String reversalNo;
	@JsonProperty("TransOpenperiodStatus")
	private String transOpenperiodStatus;
	@JsonProperty("ReversalStatus")
	private String reversalStatus;
}
