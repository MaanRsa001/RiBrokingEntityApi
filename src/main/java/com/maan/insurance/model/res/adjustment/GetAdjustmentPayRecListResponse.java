package com.maan.insurance.model.res.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAdjustmentPayRecListResponse {

	@JsonProperty("AdjustmentList")
	private List<GetAdjustmentPayRecListRes1> adjustmentList;
	
	@JsonProperty("UnUtilizedAmt")
	private String unUtilizedAmt;
}
