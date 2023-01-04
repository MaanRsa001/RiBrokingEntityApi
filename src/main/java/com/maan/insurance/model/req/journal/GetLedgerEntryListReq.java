package com.maan.insurance.model.req.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes1;

import lombok.Data;

@Data
public class GetLedgerEntryListReq {
	@JsonProperty("OpendDate")
	private String opendDate;
	@JsonProperty("OpstartDate")
	private String opstartDate;
	@JsonProperty("BranchCode")
	private String branchCode;
}
