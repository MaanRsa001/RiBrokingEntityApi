package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimTableListMode2ResList {
	
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	
	@JsonProperty("LossEstimateRevisedUSD")
	private String lossEstimateRevisedUSD;
	
	@JsonProperty("UpdateReference")
	private String updateReference;
	
	@JsonProperty("CliamupdateDate")
	private String cliamupdateDate;
	
	@JsonProperty("SNo")
	private String sNo;
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("claimNo")
	private String claimNo;
	
	@JsonProperty("ClaimPaidOC")
	private String claimPaidOC;
	
	@JsonProperty("ClaimPaidDC")
	private String claimPaidDC;
	
	@JsonProperty("OsAmtOC")
	private String osAmtOC;
	
	@JsonProperty("OsAmtDC")
	private String osAmtDC;

}
