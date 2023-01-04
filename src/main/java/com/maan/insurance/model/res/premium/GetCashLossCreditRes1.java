package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCashLossCreditRes1 {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("PaidDate")
	private String paidDate;
	
	@JsonProperty("ClaimNumber")
	private String claimNumber;
	
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	
	@JsonProperty("PayAmount")
	private String payAmount;
	
	@JsonProperty("ExcessRatePercent")
	private String excessRatePercent;
	
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("CurrencyValueName")
	private String currencyValueName;
	@JsonProperty("CurrencyIdName")
	private String currencyIdName;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("CreditAmountCLC")
	private String creditAmountCLC;
	@JsonProperty("CreditAmountCLD")
	private String creditAmountCLD;
	@JsonProperty("CLCsettlementRate")
	private String cLCsettlementRate;
	@JsonProperty("Check")
	private String check;
	@JsonProperty("CreditAmountCLCTemp")
	private String creditAmountCLCTemp;
	@JsonProperty("CreditAmountCLDTemp")
	private String creditAmountCLDTemp;
	@JsonProperty("CLCsettlementRateTemp")
	private String cLCsettlementRateTemp;

}
