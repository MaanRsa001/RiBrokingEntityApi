package com.maan.insurance.model.req.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class TransactionNoListReq {
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("AdjustAmount")
	private String adjustAmount;
	@JsonProperty("AdjustType")
	private String adjustType;
	@JsonProperty("Check")
	private String check;
}
