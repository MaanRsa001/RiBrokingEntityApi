package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes1;

import lombok.Data;

@Data
public class GetExistingAttachListRes1 {
	@JsonProperty("DocId")
	private String docId; 
	@JsonProperty("DocType")
	private String docType; 
	@JsonProperty("DocDescription")
	private String docDescription; 
	@JsonProperty("OrgFileName")
	private String orgFileName; 
	@JsonProperty("OurFileName")
	private String ourFileName; 
	
}
