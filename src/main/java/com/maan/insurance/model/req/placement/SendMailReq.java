package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SendMailReq {
	@JsonProperty("MailSubject")
	private String mailSubject; 
	@JsonProperty("MailTo")
	private String mailTo; 
	@JsonProperty("MailCC")
	private String mailCC; 
	@JsonProperty("MailBody")
	private String mailBody; 
	@JsonProperty("MailRemarks")
	private String mailRemarks; 
	@JsonProperty("MailRegards")
	private String mailRegards; 
	@JsonProperty("MailType")
	private String mailType; 
	
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("CorresId")
	private String corresId; 
}
