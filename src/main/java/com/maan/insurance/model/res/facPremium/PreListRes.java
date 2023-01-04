package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PreListRes {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("Brokername")
	private String brokername;
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	

	
	
	

}
