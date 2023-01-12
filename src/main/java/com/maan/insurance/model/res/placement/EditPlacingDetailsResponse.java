package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;

import lombok.Data;

@Data
public class EditPlacingDetailsResponse {
	@JsonProperty("PlacingDetails")
	private List<EditPlacingDetailsRes1> placingDetails;
	
	@JsonProperty("CurrentStatus")
	private String currentStatus;
}
