package com.maan.insurance.model.res.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.RetroDupListRes;

import lombok.Data;

@Data
public class GetPendingOffersListRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("UWYear")
	private String uWYear;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("AccDate")
	private String accDate;
	@JsonProperty("TmasSpfcName")
	private String tmasSpfcName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("ShareWritten")
	private String shareWritten;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("ProposalStatus")
	private String proposalStatus;
	@JsonProperty("UnderWritter")
	private String underWritter;
	@JsonProperty("BranchCode")
	private String branchCode;
}
