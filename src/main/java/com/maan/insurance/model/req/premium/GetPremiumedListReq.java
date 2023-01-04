package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumedListReq {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("Type")
	private String type; 
	
	@JsonProperty("OpstartDate")
	private String opstartDate;
	
	@JsonProperty("OpendDate")
	private String opendDate;
	
}
