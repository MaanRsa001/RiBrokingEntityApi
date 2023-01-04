package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllocatedListRes2 {
	@JsonProperty("Result")
	private List<GetAllocatedListRes1> commonResponse;
	
	@JsonProperty("TotalAmount")
	private String totalAmount;
}
