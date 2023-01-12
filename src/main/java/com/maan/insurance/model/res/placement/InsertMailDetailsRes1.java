package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class InsertMailDetailsRes1 {
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;  

	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;

	
}
