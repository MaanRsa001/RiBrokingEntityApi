package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiReq;

import lombok.Data;

@Data
public class BaseLayerStatusRes1 {
	@JsonProperty("BaseLayerYN")
	private String baseLayerYN;
	
	@JsonProperty("BaseContractNo")
	private String baseContractNo;
	@JsonProperty("BaseContractNoStatus")
	private String baseContractNoStatus;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("ProdisableStatus")
	private String prodisableStatus;

}
