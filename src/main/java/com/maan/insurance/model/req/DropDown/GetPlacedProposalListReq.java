package com.maan.insurance.model.req.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;

import lombok.Data;

@Data
public class GetPlacedProposalListReq {
	
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	
	@JsonProperty("PlacementMode")
	private String placementMode; 
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
}
