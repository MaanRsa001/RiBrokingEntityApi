package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetPremiumReservedReq {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("PrTransNo")
	private String prTransNo;

	@JsonProperty("Chkbox")
	private String chkbox;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CurrencyId")
	private String currencyId;
}
