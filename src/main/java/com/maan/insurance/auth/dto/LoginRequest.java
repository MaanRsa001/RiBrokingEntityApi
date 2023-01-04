package com.maan.insurance.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginRequest {

	@JsonProperty("UserId")
	private String userId;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("RegionCode")
	private String regionCode;
	@JsonProperty("LoginType")
	private String loginType;
	@JsonProperty("InsuranceId")
	private String companyId;
	@JsonProperty("WhatsappYN")
	private String whatsappyn;
	
	//new Attibute
	@JsonProperty("UserType")
	private String userType;
	@JsonProperty("SubUserType")
	private String subUserType;
	
}
