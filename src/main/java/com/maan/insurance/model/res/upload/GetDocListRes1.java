package com.maan.insurance.model.res.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDocListRes1 {
	@JsonProperty("DocId")
	private String docId;
	
	@JsonProperty("DocDesc")
	private String docDesc;

	
	@JsonProperty("DocName")
	private String docName;
	@JsonProperty("OrgFileName")
	private String orgFileName;
	@JsonProperty("OurFileName")
	private String ourFileName;
	
	
	@JsonProperty("Location")
	private String location;
	
	@JsonProperty("EffDate")
	private String effDate;
	
	@JsonProperty("CategoryDetailId")
	private String categoryDetailId;
	
	@JsonProperty("DetailName")
	private String detailName;
	

}
