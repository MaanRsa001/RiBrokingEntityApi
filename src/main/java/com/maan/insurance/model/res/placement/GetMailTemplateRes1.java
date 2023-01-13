package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetMailTemplateRes1 {
	@JsonProperty("MailSubject")
	private String mailSubject; 
	@JsonProperty("MailBody")
	private String mailBody; 
	@JsonProperty("MailTo")
	private String mailTo;
	@JsonProperty("MailCC")
	private String mailCC; 
	@JsonProperty("MailRegards")
	private String mailRegards;  
	
}
