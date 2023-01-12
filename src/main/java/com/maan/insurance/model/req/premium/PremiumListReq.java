package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.premium.PremiumListRes;
import com.maan.insurance.model.res.premium.PremiumListRes1;

import lombok.Data;

@Data
public class PremiumListReq {
	@JsonProperty("TableType")
	private String tableType; 
	@JsonProperty("Type")
	private String type;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch; 
	@JsonProperty("ContractNoSearch")
	private String contractNoSearch;
	@JsonProperty("TransactionNoSearch")
	private String transactionNoSearch;
	@JsonProperty("TransactionDateSearch")
	private String transactionDateSearch;
	@JsonProperty("CompanyNameSearchTemp")
	private String companyNameSearchTemp;
	@JsonProperty("BrokerNameSearchTemp")
	private String brokerNameSearchTemp; 
	@JsonProperty("ContractNoSearchTemp")
	private String contractNoSearchTemp;
	@JsonProperty("TransactionNoSearchTemp")
	private String transactionNoSearchTemp;
	@JsonProperty("TransactionDateSearchTemp")
	private String transactionDateSearchTemp; 
	@JsonProperty("OpstartDate")
	private String opstartDate; 
	@JsonProperty("OpendDate")
	private String opendDate;

}
