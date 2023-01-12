package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.GetReinsurerInfoReq;

import lombok.Data;

@Data
public class GetReinsurerInfoResponse {
	@JsonProperty("ReinsurerInfoList")
	private List<GetReinsurerInfoRes1> reinsurerInfoList;
	
	@JsonProperty("PlacementDisabled")
	private String placementDisabled;
	
	@JsonProperty("PlacementMode")
	private String placementMode;

}
