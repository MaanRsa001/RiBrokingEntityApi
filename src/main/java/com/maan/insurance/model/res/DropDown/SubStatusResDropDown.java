package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class SubStatusResDropDown 
{
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescription")
	private String codeDescription;

	@JsonProperty("CodeValue")
	private String codeValue;
	
	@JsonProperty("ApproverYN")
	private String approverYN;
	
	@JsonProperty("MailYN")
	private String mailYN;


}
