package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class InsertPlacingRes1 {
	@JsonProperty("PlacementamendId")
	private String placementamendId;  
	
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	
	@JsonProperty("BrokerId")
	private String brokerId; 
}
