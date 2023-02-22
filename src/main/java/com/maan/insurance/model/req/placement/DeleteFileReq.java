package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeleteFileReq {
	@JsonProperty("DocId")
	private String docId;
	@JsonProperty("FileName")
	private String fileName;
}
