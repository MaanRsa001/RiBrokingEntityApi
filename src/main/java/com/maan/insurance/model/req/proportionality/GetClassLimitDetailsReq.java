package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes1;

import lombok.Data;

@Data
public class GetClassLimitDetailsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("BusinessType")
	private String businessType;

	@JsonProperty("ReinstatementOption")
	private String reinstatementOption;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("BaseLayer")
	private String baseLayer;


}
