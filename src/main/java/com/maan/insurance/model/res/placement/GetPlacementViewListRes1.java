package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;

import lombok.Data;

@Data
public class GetPlacementViewListRes1 {
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;  
	@JsonProperty("Sno")
	private String sno;  
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
	@JsonProperty("BrokerName")
	private String brokerName; 
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	@JsonProperty("NewStatus")
	private String newStatus;  
	@JsonProperty("UpdateDate")
	private String updateDate;  
	@JsonProperty("EmailBy")
	private String emailBy;
	
	@JsonProperty("Status")
	private String status;  
	@JsonProperty("CorrespondentId")
	private String correspondentId;
}
