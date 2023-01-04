package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode7ResList {
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
	
	@JsonProperty("Lero2a")
	private String lero2a;

	@JsonProperty("Lero2b")
	private String lero2b;

	@JsonProperty("Lero2c")
	private String lero2c;

	@JsonProperty("Saf3a")
	private String saf3a;

	@JsonProperty("Saf3b")
	private String saf3b;

	@JsonProperty("Saf3c")
	private String saf3c;

	@JsonProperty("Ofos4a")
	private String ofos4a;

	@JsonProperty("Ofos4b")
	private String ofos4b;

	@JsonProperty("Ofos4c")
	private String ofos4c;
	
	@JsonProperty("Totala")
	private String totala;

	@JsonProperty("Totalb")
	private String totalb;

	@JsonProperty("Totalc")
	private String totalc;

	@JsonProperty("ReInspremiumOS")
	private String reInspremiumOS;

	@JsonProperty("Cibnr100Oc")
	private String cibnr100Oc;

	@JsonProperty("ClaimNo")
	private String claimNo;
	
	
	
	
}
