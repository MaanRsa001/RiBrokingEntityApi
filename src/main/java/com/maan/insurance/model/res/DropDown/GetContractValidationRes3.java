package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractValidationRes3 {
	@JsonProperty("ContractNo")
	private String ContractNo;
	
	@JsonProperty("EndoresmentNo")
	private String endoresmentNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("UnderWritter")
	private String underWritter;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("DedPer")
	private String dedPer;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("CoverPer")
	private String coverPer;
	
	@JsonProperty("Dedu")
	private String dedu;
	@JsonProperty("CoverLimit")
	private String coverLimit;
}
