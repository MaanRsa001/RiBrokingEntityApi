package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;

import lombok.Data;
@Data
public class InstalMentPremiumReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;

	
	@JsonProperty("InstalmentDetails")
	private List<InstalmentperiodReq> instalmentDetails;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("NoOfInst")
	private String noOfInst;
	
	@JsonProperty("Loginid")
	private String loginid;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("UsCurrencyRate")
	private String usCurrencyRate;
}
