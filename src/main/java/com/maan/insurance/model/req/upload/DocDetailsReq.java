package com.maan.insurance.model.req.upload;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.DoUploadReq;

import lombok.Data;

@Data
public class DocDetailsReq {
	@JsonProperty("DocId")
	private String docId;
	@JsonProperty("DocTypeId")
	private String docTypeId; 
	@JsonProperty("Upload")
	private File upload;  
	@JsonProperty("UploadFileName")
	private String uploadFileName;
	@JsonProperty("DocDesc")
	private String docDesc; 
}
