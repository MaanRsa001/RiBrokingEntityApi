package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllocatedCassLossCreditRes1 {
	@JsonProperty("CreditTrxnNo")
	private String creditTrxnNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	@JsonProperty("CreditDate")
	private String creditDate;
	@JsonProperty("CldAmount")
	private String cldAmount;
	@JsonProperty("CldCurrencyId")
	private String cldCurrencyId;
	@JsonProperty("ClcCurrencyId")
	private String ClcCurrencyId;
	@JsonProperty("CreditAmountCld")
	private String creditAmountCld;
	@JsonProperty("CreditAmountClc")
	private String creditAmountClc;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
}
