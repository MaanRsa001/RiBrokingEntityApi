package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.GetPlacementViewListReq;

import lombok.Data;

@Data
public class GetPlacementViewRes1 {
	@JsonProperty("CedentCorrespondent")
	private String cedentCorrespondent; 
	@JsonProperty("ReinsurerCorrespondent")
	private String reinsurerCorrespondent; 
	@JsonProperty("TqrCorrespondent")
	private String tqrCorrespondent; 
	
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	
	@JsonProperty("UpdateDate")
	private String updateDate;  
	@JsonProperty("EmailBy")
	private String emailBy;

}
