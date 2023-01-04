package com.maan.insurance.req.XolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroContractsReq {
	@JsonProperty("ProposalNo")
	public String proposalNo;
	
	@JsonProperty("NoOfRetro")
	public String noOfRetro;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("ProductId")
	public String productId;
	
	@JsonProperty("IncepDate")
	public String incepDate;
	
	@JsonProperty("UwYear")
	public String uwYear;
	

}
