package com.maan.insurance.model.req.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class GetAdjustmentPayRecListReq {
	@JsonProperty("Test")
	private String test;
	@JsonProperty("AmountType")
	private String amountType;
	@JsonProperty("Amount")
	private String amount;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("CedingId")
	private String 	cedingId;
	@JsonProperty("TransactionNo")
	private String 	transactionNo;
	@JsonProperty("AdjustType")
	private String 	adjustType;
	@JsonProperty("Mode")
	private String 	mode;
	@JsonProperty("TransactionNoListReq")
	private List<TransactionNoListReq> 	transactionNoListReq;
}
