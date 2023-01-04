package com.maan.insurance.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimLoginResponse {
	
	@JsonProperty("Token")
    private String token;
	@JsonProperty("LoginId")
    private String loginId;
	@JsonProperty("Email")
    private String email;
	@JsonProperty("MobileNo")
    private String mobileNo;
	@JsonProperty("UserName")
    private String userName;

}
