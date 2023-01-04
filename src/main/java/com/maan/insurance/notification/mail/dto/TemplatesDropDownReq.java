package com.maan.insurance.notification.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TemplatesDropDownReq {

	@JsonProperty("InsuranceId")
	private String insId ;
	@JsonProperty("NotifApplicable")
	private String notifApplicable ;
	
}
