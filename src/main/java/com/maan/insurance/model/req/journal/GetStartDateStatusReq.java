package com.maan.insurance.model.req.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.journal.GetViewLedgerDetailsRes1;
import com.maan.insurance.model.res.journal.LedgerdetailListRes;

import lombok.Data;

@Data
public class GetStartDateStatusReq {
	@JsonProperty("StartDate")
	private String startDate;
	@JsonProperty("BranchCode")
	private String branchCode;
}
