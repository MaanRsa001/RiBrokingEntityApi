package com.maan.insurance.model.req.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.retro.GetEndDateReq;

import lombok.Data;

@Data
public class GetAdjustmentDoneListReq {
	@JsonProperty("SearchBy")
	private String searchBy;
	@JsonProperty("SearchValue")
	private String searchValue;
	@JsonProperty("BranchCode")
	private String branchCode;
}
