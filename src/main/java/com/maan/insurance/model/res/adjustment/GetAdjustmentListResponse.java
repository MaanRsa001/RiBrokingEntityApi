package com.maan.insurance.model.res.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAdjustmentListResponse {
	@JsonProperty("AdjustmentList")
	private List<GetAdjustmentListRes1> adjustmentList;
	
	@JsonProperty("UnUtilizedAmt")
	private String unUtilizedAmt;
}
