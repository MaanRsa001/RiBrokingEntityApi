package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetViewLedgerDetailsRes1 {
	
	@JsonProperty("LedgerdetailList")
	private List<LedgerdetailListRes> ledgerdetailList;
	
	@JsonProperty("TotaldebitOC")
	private String totaldebitOC;
	@JsonProperty("TotalcreditOC")
	private String totalcreditOC;
	@JsonProperty("TotaldebitDC")
	private String totaldebitDC;
	@JsonProperty("TotalcreditDC")
	private String totalcreditDC;
	@JsonProperty("ExdebitDC")
	private String exdebitDC;
	@JsonProperty("ExcreditDC")
	private String excreditDC;
	@JsonProperty("Loopcount")
	private String loopcount;
	@JsonProperty("Narration")
	private String narration;
}
