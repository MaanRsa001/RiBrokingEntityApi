package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdatePlacementListReq {
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("Reoffer")
	private String reoffer; 
	@JsonProperty("ShareOffered")
	private String shareOffered; 
	@JsonProperty("WrittenLine")
	private String writtenLine; 
	@JsonProperty("ProposedWL")
	private String proposedWL; 
	@JsonProperty("Writtenvaliditydate")
	private String writtenvaliditydate; 
	@JsonProperty("WrittenvalidityRemarks")
	private String writtenvalidityRemarks; 
	@JsonProperty("SignedLine")
	private String signedLine; 
	@JsonProperty("SignedLineValidity")
	private String signedLineValidity; 
	@JsonProperty("SignedLineRemarks")
	private String signedLineRemarks; 
	@JsonProperty("ProposedSL")
	private String proposedSL; 
	@JsonProperty("Brokerage")
	private String brokerage; 
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo;
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseproposalNo")
	private String baseproposalNo; 
	@JsonProperty("PsignedLine")
	private String psignedLine; 
	
}
