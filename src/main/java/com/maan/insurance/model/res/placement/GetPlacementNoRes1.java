package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPlacementNoRes1 {
	@JsonProperty("StatusNo")
	private String statusNo;
	
	@JsonProperty("PlacementNo")
	private String placementNo;
}
