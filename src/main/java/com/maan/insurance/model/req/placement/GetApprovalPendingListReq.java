package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetApprovalPendingListReq {

	@JsonProperty("SearchBouquetNo")
	private String searchBouquetNo; 
	@JsonProperty("SearchBaseProposalNo")
	private String searchBaseProposalNo;
	@JsonProperty("SearchProposalNo")
	private String searchProposalNo; 
	@JsonProperty("SearchType")
	private String searchType; 
	@JsonProperty("SearchStatusNo") 
	private String searchStatusNo; 
	@JsonProperty("SearchOfferNo") 
	private String searchOfferNo; 
	@JsonProperty("SearchCedingCompany") 
	private String searchCedingCompany; 
	@JsonProperty("SearchReinsurerName") 
	private String searchReinsurerName; 
	@JsonProperty("SearchBrokerName") 
	private String searchBrokerName; 
	@JsonProperty("SearchIncludeProposalNos") 
	private String searchIncludeProposalNos; 
	@JsonProperty("SearchExcludeProposalNos") 
	private String searchExcludeProposalNos; 
	
}
