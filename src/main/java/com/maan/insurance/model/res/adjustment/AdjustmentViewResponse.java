package com.maan.insurance.model.res.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AdjustmentViewResponse {
	@JsonProperty("AllocatedList")
	private List<AdjustmentViewRes1> allocatedList;
	@JsonProperty("UnUtilizedAmt")
	private String unUtilizedAmt;
}
