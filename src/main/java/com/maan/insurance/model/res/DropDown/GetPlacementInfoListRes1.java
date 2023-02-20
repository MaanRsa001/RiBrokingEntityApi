package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPlacementInfoListRes1 {
	@JsonProperty("Snos")
	private String snos;
	@JsonProperty("BaseproposalNos")
	private String baseproposalNos;
	@JsonProperty("BouquetNos")
	private String bouquetNos;
	@JsonProperty("ReinsurerIds")
	private String reinsurerIds;
	@JsonProperty("BrokerIds")
	private String brokerIds;
	@JsonProperty("ShareOffered")
	private String shareOffered;
	@JsonProperty("WrittenLine")
	private String writtenLine;
	@JsonProperty("Brokerages")
	private String brokerages;
	@JsonProperty("ProposedWL")
	private String proposedWL;
	
	@JsonProperty("SignedLine")
	private String signedLine;
	@JsonProperty("ProposedSL")
	private String proposedSL;
	@JsonProperty("CurrentStatus")
	private String currentStatus;
	@JsonProperty("NewStatus")
	private String newStatus;
	@JsonProperty("StatusNo")
	private String statusNo;
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo; 
}
