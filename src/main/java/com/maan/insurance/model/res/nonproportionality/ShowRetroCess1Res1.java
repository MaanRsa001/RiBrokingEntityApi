package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;

import lombok.Data;

@Data
public class ShowRetroCess1Res1 {
//	@JsonProperty("BaseLayerYN")
//	private String baseLayerYN;
//	
//	@JsonProperty("BaseContractNo")
//	private String baseContractNo;
//	
//	@JsonProperty("BaseContractNoStatus")
//	private String baseContractNoStatus;
//	
//	@JsonProperty("ProStatus")
//	private String proStatus;
//	
//	@JsonProperty("ProdisableStatus")
//	private String prodisableStatus;
	
	@JsonProperty("ProdisableStatus")
	private List<RetroCessListRes> RetroCessList;
}
