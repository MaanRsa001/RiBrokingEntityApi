package com.maan.insurance.model.res.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetDocTypeRes1 {
	@JsonProperty("DocName")
	private String docName;
	@JsonProperty("DocType")
	private String docType;
}
