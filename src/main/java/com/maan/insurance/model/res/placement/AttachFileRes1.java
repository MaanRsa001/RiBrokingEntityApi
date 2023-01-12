package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AttachFileRes1 {
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;  
	@JsonProperty("Sno")
	private String sno;  
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
}
