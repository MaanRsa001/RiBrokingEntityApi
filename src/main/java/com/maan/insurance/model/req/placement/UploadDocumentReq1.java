package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadDocumentReq1 {
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseproposalNo")
	private String baseproposalNo; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
}
