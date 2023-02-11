package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdatePlacementRes {
	@JsonProperty("CorrespondentId")
	private String correspondentId; 
	@JsonProperty("StatusNo")
	private String statusNo; 
	
}
