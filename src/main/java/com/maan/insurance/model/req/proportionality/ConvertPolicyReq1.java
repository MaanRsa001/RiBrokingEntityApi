package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;

import lombok.Data;

@Data
public class ConvertPolicyReq1 {
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	@JsonProperty("NewStatus")
	private String newStatus; 
	@JsonProperty("Snos")
	private String snos; 
	@JsonProperty("BouquetNos")
	private String bouquetNos; 
	@JsonProperty("BaseproposalNos")
	private String baseproposalNos; 
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo;
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	
	@JsonProperty("ReinsurerIds")
	private String reinsurerIds; 
	@JsonProperty("ShareOffered")
	private String shareOffered; 
	@JsonProperty("WrittenLine")
	private String writtenLine; 
	@JsonProperty("ProposedWL")
	private String proposedWL; 
	@JsonProperty("BrokerIds")
	private String brokerIds; 
	@JsonProperty("Brokerages")
	private String brokerages; 
	@JsonProperty("SignedLine")
	private String signedLine; 
	@JsonProperty("ProposedSL")
	private String proposedSL;  
	@JsonProperty("StatusNo")
	private String statusNo; 
	
	
}
