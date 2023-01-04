package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes1;

import lombok.Data;

@Data
public class GetReinsurerInfoReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	
//	@JsonProperty("PlacementMode")
//	private String placementMode;
	
	
}
