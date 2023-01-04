package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertReverseCashLossCreditReq {
	
	@JsonProperty("CashlosstranId")
	private String cashlosstranId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("CashlossType")
	private String cashlossType;
	
	@JsonProperty("ContNo")
	private String contNo;
	

}
