package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RemarksReq;

import lombok.Data;

@Data
public class RemarksRes {

	@JsonProperty("RemarkSNo")
	private String remarkSNo;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Remark1")
	private String remark1;
	@JsonProperty("Remark2")
	private String remark2;
}
