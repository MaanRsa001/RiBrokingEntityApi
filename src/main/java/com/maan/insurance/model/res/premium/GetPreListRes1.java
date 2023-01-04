package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPreListRes1 {
	@JsonProperty("UwYear")
	private String uwYear;
	
//	@JsonProperty("ProposalNo")
//	private String proposalNo;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	
	@JsonProperty("DepartmentName")
	private String departmentName;
	
//	@JsonProperty("Layerno")
//	private String layerno;
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
//	@JsonProperty("ProductId")
//	private String productId;
//	
//	@JsonProperty("CeaseStatus")
//	private String ceaseStatus;
	
}
