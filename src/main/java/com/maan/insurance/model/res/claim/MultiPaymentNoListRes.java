package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MultiPaymentNoListRes {

	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("Reference")
	private String reference;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("Class")
	private String clas; 
	@JsonProperty("LossDetails")
	private String lossDetails;
	@JsonProperty("DateOfLoss")
	private String dateOfLoss;
	@JsonProperty("LossEstimateOc")
	private String lossEstimateOc;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("LossEstimateOsOc")
	private String lossEstimateOsOc;
	@JsonProperty("PaymentType")
	private String paymentType;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
}
