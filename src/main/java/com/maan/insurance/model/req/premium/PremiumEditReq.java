package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumEditReq {
	
	
@JsonProperty("TableType")
private String tableType;

@JsonProperty("ContNo")
private String contNo;

@JsonProperty("RequestNo")
private String requestNo;

@JsonProperty("TransactionNo")
private String transactionNo;

@JsonProperty("CurrencyId")
private String currencyId;

@JsonProperty("Transaction")
private String transaction;

@JsonProperty("CountryId")
private String countryId;

@JsonProperty("BranchCode")
private String branchCode;

@JsonProperty("ProductId")
private String productId;

@JsonProperty("TransDropDownVal")
private String transDropDownVal;

@JsonProperty("Mode")
private String mode;

}
