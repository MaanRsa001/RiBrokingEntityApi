package com.maan.insurance.model.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SecondPageInfoReq {
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("BranchCode")
	private String branchCode;

	
	
}
