package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class InstalMentPremiumReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("EndorsementNo")
	private String endorsementNo;
	
	@JsonProperty("InstalmentperiodReq")
	private List<InstalmentperiodReq> instalmentperiodReq;
	
	
}
