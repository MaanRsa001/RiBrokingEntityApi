package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@ Data
public class GetDirectCedingReq {

	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("BranchId")
	private String branchId;
}
