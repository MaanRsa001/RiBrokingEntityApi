package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPreListRes1 {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Brokername")
	private String brokername;
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	@JsonProperty("SaveFlag")
	private String saveFlag; 
}
