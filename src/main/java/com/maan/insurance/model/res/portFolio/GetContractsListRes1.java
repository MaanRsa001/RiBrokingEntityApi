package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractsListRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Ceddingcompanyid")
	private String ceddingcompanyid;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("InsuredName")
	private String insuredName; 
	@JsonProperty("CombinedClassCount")
	private String combinedClassCount;
//	@JsonProperty("ShareSign")
//	private String shareSign;
//	@JsonProperty("Contractno1")
//	private String contractno1;
//	@JsonProperty("ContractNo")
//	private String contractNo;
	@JsonProperty("BrokerName")
	private String brokerName;
//	@JsonProperty("BrokerId")
//	private String brokerId;
//	@JsonProperty("PremiumClass")
//	private String premiumClass; 
//	@JsonProperty("PremiumSubClass")
//	private String premiumSubClass;
//	@JsonProperty("SubClass")
//	private String subClass;
//	@JsonProperty("CurrencyShort")
//	private String currencyShort;
//	@JsonProperty("CurrencyId")
//	private String currencyId;
//	@JsonProperty("ExchangeRate")
//	private String exchangeRate; 
//	@JsonProperty("InstallmentDate")
//	private String installmentDate;
//	@JsonProperty("InstallmentNo")
//	private String installmentNo;
//	@JsonProperty("PremiumOC")
//	private String premiumOC;
//	@JsonProperty("Commission")
//	private String commission;
//	@JsonProperty("Brokerage")
//	private String brokerage; 
//	@JsonProperty("OtherCost")
//	private String otherCost;
//	@JsonProperty("Tax")
//	private String tax ;
//	@JsonProperty("TreatyName")
//	private String treatyName;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
//	@JsonProperty("Lay1")
//	private String lay1; 
	@JsonProperty("UwYear")
	private String uwYear ;
	@JsonProperty("UwMonth")
	private String uwMonth;
	@JsonProperty("Underwritter")
	private String underwritter;
	@JsonProperty("OldContract")
	private String oldContract; 
	@JsonProperty("ProposalId")
	private String proposalId;
	@JsonProperty("ProposalStatus")
	private String proposalStatus;
	@JsonProperty("ContractStatus")
	private String contractStatus;
	@JsonProperty("QuoteGendrateddate")
	private String quoteGendrateddate;
	@JsonProperty("OldStatus")
	private String oldStatus;
	@JsonProperty("ProductId")
	private String productId; 
	@JsonProperty("RenewalStatus")
	private String renewalStatus;
	@JsonProperty("RenewalPeriod")
	private String renewalPeriod;
	@JsonProperty("EditMode")
	private String editMode;
	@JsonProperty("DisableStatus")
	private String disableStatus;
	@JsonProperty("Renewaldisable")
	private String renewaldisable;
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus; 
	@JsonProperty("Premiumcount")
	private String premiumcount;
	@JsonProperty("PremiumStatus")
	private String premiumStatus;
	@JsonProperty("ClaimCount")
	private String claimCount;
	@JsonProperty("ClaimStatus")
	private String claimStatus;
	@JsonProperty("GnpiFlag")
	private String gnpiFlag;
	
	@JsonProperty("ButtonSelectionList")
	private List<ButtonSelectionListRes> ButtonSelectionList;
	
	@JsonProperty("OfferNo") 
	private String offerNo;
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("SectionNo")
	private String sectionNo;
}
