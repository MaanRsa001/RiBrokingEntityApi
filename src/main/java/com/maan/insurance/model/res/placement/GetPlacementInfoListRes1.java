package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetPlacementInfoListRes1 {
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("CedingCompanyId")
	private String cedingCompanyId; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
	@JsonProperty("BrokerName")
	private String brokerName; 
	@JsonProperty("ShareOffered")
	private String shareOffered; 
	@JsonProperty("ShareWritten")
	private String shareWritten; 
	@JsonProperty("ShareProposalWritten")
	private String shareProposalWritten; 
	@JsonProperty("ShareSigned")
	private String shareSigned; 
	@JsonProperty("BrokeragePer")
	private String brokeragePer; 
	@JsonProperty("Status")
	private String status; 
	@JsonProperty("WrittenLineValidity")
	private String writtenLineValidity; 
	@JsonProperty("WrittenLineRemarks")
	private String writtenLineRemarks; 
	@JsonProperty("ShareLineValidity")
	private String shareLineValidity; 
	@JsonProperty("ShareLineRemarks")
	private String shareLineRemarks; 
	@JsonProperty("ShareProposedSigned")
	private String shareProposedSigned; 
	@JsonProperty("MailStatus")
	private String mailStatus; 
	@JsonProperty("OfferNo")
	private String offerNo ;
	@JsonProperty("ApproverStatus")
	private String approverStatus; 
	@JsonProperty("ContractStatus")
	private String contractStatus; 

}
