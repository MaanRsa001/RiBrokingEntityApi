package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimPaymentListReq {
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch;
	@JsonProperty("ContractNoSearch")
	private String contractNoSearch;
	@JsonProperty("ClaimNoSearch")
	private String claimNoSearch;
	@JsonProperty("PaymentNoSearch")
	private String paymentNoSearch;
	@JsonProperty("PaymentDateSearch")
	private String paymentDateSearch;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Date")
	private String date;
}
