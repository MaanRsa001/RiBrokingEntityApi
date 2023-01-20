package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMailTemplateReq {
	@JsonProperty("MailType")
	private String mailType;
	@JsonProperty("TranasctionNo")
	private String tranasctionNo; 
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("NewStatus")
	private String newStatus;
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("PlacementMode")
	private String placementMode; 
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo; 
	@JsonProperty("SearchReinsurerId")
	private String searchReinsurerId;
	@JsonProperty("SearchBrokerId")
	private String searchBrokerId; 
          	
	/*
	 * @JsonProperty("MailTo") private String mailTo;
	 * 
	 * @JsonProperty("MailCC") private String mailCC;
	 */
}
