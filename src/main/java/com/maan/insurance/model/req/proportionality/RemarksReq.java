package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RemarksReq {

	@JsonProperty("RemarkSNo")
	private String remarkSNo;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Remark1")
	private String remark1;
	@JsonProperty("Remark2")
	private String remark2;
	
	
	
	
	
	

}
