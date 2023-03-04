package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class PremiumListRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Layerno")
	private String layerno;

	@JsonProperty("TransactionNo")
	private String transactionNo; 
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("TransactionTypeName")
	private String transactionTypeName; 
	@JsonProperty("InsDate")
	private String insDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("InceptionDate")
	private String inceptionDate; 
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("TransOpenperiodStatus")
	private String transOpenperiodStatus; 
	@JsonProperty("AllocatedYN")
	private String allocatedYN;
	@JsonProperty("DeleteStatus")
	private String deleteStatus; 
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	@JsonProperty("RiTransactionNo")
	private String ritransactionNo; 
	@JsonProperty("ReinsuranceName")
	private String reinsuranceName;
	@JsonProperty("RiBroker")
	private String riBroker;
	
	@JsonProperty("NewLayerno")
	private String newLayerno;
	
}
