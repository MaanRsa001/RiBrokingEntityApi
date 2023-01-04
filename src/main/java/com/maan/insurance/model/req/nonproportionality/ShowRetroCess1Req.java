package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;

import lombok.Data;

@Data
public class ShowRetroCess1Req {
	@JsonProperty("Mode")
	private Integer mode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
	
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("RetroCessListReq")
	private List<RetroCessListReq> retroCessReq;
	
	

}
