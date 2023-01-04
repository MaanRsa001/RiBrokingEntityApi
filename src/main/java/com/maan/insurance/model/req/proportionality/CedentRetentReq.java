package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CedentRetentReq {

	@JsonProperty("RetentSNo")
	private String retentSNo;
	@JsonProperty("CoverdepartId")
	private String coverdepartId;
	@JsonProperty("CoversubdepartId")
	private String coversubdepartId;
	@JsonProperty("RetBusinessType")
	private String retBusinessType;
	@JsonProperty("RetType")
	private String retType;
	@JsonProperty("RetBasis")
	private String retBasis;
	@JsonProperty("Firstretention")
	private String firstretention;
	@JsonProperty("Secondretention")
	private String secondretention;
	@JsonProperty("RetTreatyFST")
	private String retTreatyFST;
	@JsonProperty("RetTreatySST")
	private String retTreatySST;
	@JsonProperty("RetEventFST")
	private String retEventFST;
	@JsonProperty("RetEventSST")
	private String retEventSST;
	
	
	
	
	
	
	

}
