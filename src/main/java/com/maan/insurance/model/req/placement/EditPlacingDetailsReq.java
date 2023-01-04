package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes1;

import lombok.Data;

@Data
public class EditPlacingDetailsReq {
	@JsonProperty("SearchType")
	private String searchType; 
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("SearchReinsurerId")
	private String searchReinsurerId; 
	@JsonProperty("SearchBrokerId")
	private String searchBrokerId; 
	@JsonProperty("SearchStatus")
	private String searchStatus; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;  
	@JsonProperty("NewStatus")
	private String newStatus;
}
