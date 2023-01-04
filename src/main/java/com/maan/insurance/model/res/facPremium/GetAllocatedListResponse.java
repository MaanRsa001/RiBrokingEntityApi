package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllocatedListResponse {
	@JsonProperty("AllocatedList")
	private List<GetAllocatedListRes1> allocatedList;
	@JsonProperty("TotalAmount")
	private String totalAmount;
}
