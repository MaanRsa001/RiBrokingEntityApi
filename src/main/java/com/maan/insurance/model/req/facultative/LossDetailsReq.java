package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LossDetailsReq {
	
	@JsonProperty("LossYear")
	private String lossYear;
	
	@JsonProperty("LossNo")
	private String lossNo;
	
	@JsonProperty("LossinsuredName")
	private String lossinsuredName;
	
	@JsonProperty("LossInceptionDate")
	private String lossInceptionDate;
	
	@JsonProperty("LossExpiryDate")
	private String lossExpiryDate;
	
	@JsonProperty("LossDateOfLoss")
	private String lossDateOfLoss;
	
	@JsonProperty("LossCauseOfLoss")
	private String lossCauseOfLoss;
	
	@JsonProperty("LossInsuredClaim")
	private String lossInsuredClaim;
	
	@JsonProperty("LossPremium")
	private String lossPremium;
	
	@JsonProperty("LossRatio")
	private String lossRatio;
	
	@JsonProperty("LossLeader")
	private String lossLeader;
	
	@JsonProperty("LossITIReShare")
	private String lossITIReShare;
}
