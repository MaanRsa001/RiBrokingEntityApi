package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetSPRetroListRes1;

import lombok.Data;

@Data
public class GetSPRetroListReq {
	
	@JsonProperty("ContNo")
	private String contNo;
//	@JsonProperty("ProductId")
//	private String productId;
//	@JsonProperty("LayerNo")
//	private String layerNo;
}
