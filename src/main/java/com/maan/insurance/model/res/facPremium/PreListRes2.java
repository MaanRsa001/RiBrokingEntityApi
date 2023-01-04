package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PreListRes2 {
	@JsonProperty("Result")
	private List<PreListRes> commonResponse;
	
	@JsonProperty("SaveFlag")
	private String saveFlag;
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
}
