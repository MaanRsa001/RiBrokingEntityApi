package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadDocumentReq {

	@JsonProperty("Upload")
	private String upload; 
	@JsonProperty("FilePath")
	private String filePath; 
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
	@JsonProperty("InsertDocdetailsReq")
	private List<InsertDocdetailsReq> insertDocdetailsReq; 
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("UserId")
	private String userId; 
	@JsonProperty("CorresId")
	private String corresId; 
	@JsonProperty("UploadDocumentReq1")
	private List<UploadDocumentReq1> uploadDocumentReq1; 
}
