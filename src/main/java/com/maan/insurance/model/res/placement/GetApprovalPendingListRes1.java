package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetApprovalPendingListRes1 {
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("StatusNo")
	private String statusNo; 
	@JsonProperty("OfferNo")
	private String offerNo ;
	
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("CedingCompany")
	private String cedingCompany; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
	@JsonProperty("BrokerName")
	private String brokerName; 
	
	@JsonProperty("ProposalWrittenLine")
	private String proposalWrittenLine; 

	@JsonProperty("BrokeragePer")
	private String brokeragePer; 
	
	@JsonProperty("WrittenLine")
	private String writtenLine;  //sharewritten
	
	@JsonProperty("ConfirmedSignedLine")
	private String confirmedSignedLine; 
	@JsonProperty("ProposedSignedLine")
	private String proposedSignedLine; 
	@JsonProperty("ShareOffered")
	private String shareOffered  ;
	@JsonProperty("Status")
	private String status  ;


}
