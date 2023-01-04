package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumReservedRes {
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("PaidDate")
	private String paidDate;
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("PrallocatedTillDate")
	private String prallocatedTillDate;
	@JsonProperty("CurrencyValueName")
	private String currencyValueName;
	@JsonProperty("currencyIdName")
	private String  currencyIdName;
	@JsonProperty("Status")
	private String  status;
	
	@JsonProperty("CreditAmountCLC")
	private String creditAmountCLC;
	@JsonProperty("CreditAmountCLD")
	private String creditAmountCLD;
	@JsonProperty("CLCsettlementRate")
	private String  cLCsettlementRate;
	@JsonProperty("Check")
	private String  check;
}
