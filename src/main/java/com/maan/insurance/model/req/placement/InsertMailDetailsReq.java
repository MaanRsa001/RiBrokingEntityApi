package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class InsertMailDetailsReq {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;  
	@JsonProperty("PlacementMode")
	private String placementMode;  
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	
	@JsonProperty("MailSubject")
	private String mailSubject;
	@JsonProperty("MailTo")
	private String mailTo; 
	@JsonProperty("MailCC")
	private String mailCC; 
	@JsonProperty("MailBody")
	private String mailBody; 
	@JsonProperty("MailType")
	private String mailType; 
	@JsonProperty("UserId")
	private String userId; 
}
