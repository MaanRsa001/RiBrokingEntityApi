package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllRetroTransContractRes {

	@JsonProperty("ContractNo")
    private String contractNo ;
	
	@JsonProperty("ProductName")
    private String productName ;
	
	@JsonProperty("AccountDate")
    private String accountDate ;

	@JsonProperty("TransactionNo")
    private String transactionNo ;

	@JsonProperty("Inceptiobdate")
    private String Inceptiobdate ;

	@JsonProperty("NetDue")
    private String netDue ;

	@JsonProperty("CheckPC")
    private String checkPC ;
	
	@JsonProperty("Mode")
    private String mode ;

	@JsonProperty("CedingCompanyName")
    private String cedingCompanyName ;

	
}
