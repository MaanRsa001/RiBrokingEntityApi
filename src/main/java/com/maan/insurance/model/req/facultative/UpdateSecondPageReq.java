package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateSecondPageReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;
	
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	
	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;
	
	@JsonProperty("Exclusion")
	private String exclusion;
	
	@JsonProperty("CrestaStatus")
	private String crestaStatus;
	
	@JsonProperty("DocStatus")
	private String docStatus;
	
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
}
