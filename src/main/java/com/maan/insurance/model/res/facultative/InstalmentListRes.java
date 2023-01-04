package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalmentListRes {
	@JsonProperty("InstalmentDateList")
	private String InstalmentDateList;
	@JsonProperty("PaymentDueDays")
	private String PaymentDueDays;
	@JsonProperty("TransactionList")
	private String TransactionList;
}
