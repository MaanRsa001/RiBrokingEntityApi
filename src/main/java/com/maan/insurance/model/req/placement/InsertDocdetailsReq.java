package com.maan.insurance.model.req.placement;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.placement.InsertDocdetailsRes;

import lombok.Data;

@Data
public class InsertDocdetailsReq {
	@JsonProperty("UploadFileName")
	private String uploadFileName; 
	@JsonProperty("DocTypeId")
	private String docTypeId;
	@JsonProperty("Upload")
	private List<File> upload; 
	@JsonProperty("DocDesc")
	private String docDesc; 
}
