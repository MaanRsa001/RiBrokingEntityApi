package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadDocumentReq {

	
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("UserId")
	private String userId; 
	@JsonProperty("CorrespondentId")
	private String correspondentId; 
	@JsonProperty("InsertDocdetailsReq")
	private List<InsertDocdetailsReq> insertDocdetailsReq; 
	@JsonProperty("UploadDocumentReq1")
	private List<UploadDocumentReq1> uploadDocumentReq1; 
}
