package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetClaimAuthViewRes1 {
	@JsonProperty("PaymentType")
	private String paymentType;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("DateOfLoss")
	private String dateOfLoss;
	@JsonProperty("LossDetails")
	private String lossDetails;
	@JsonProperty("LossEstimateOc")
	private String lossEstimateOc;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("LossEstimateOsOc")
	private String lossEstimateOsOc;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("RskInsuredName")
	private String rskInsuredName;
	@JsonProperty("ReinspremiumOurshareOc")
	private String reinspremiumOurshareOc;
	@JsonProperty("PaidAmountOc")
	private String paidAmountOc;
	@JsonProperty("Oslr")
	private String oslr;
	@JsonProperty("Currency")
	private String currency;
}
