package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FirstInsertReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("SaveFlag")
	private String saveFlag;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("DepartId")
	private String departId;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	@JsonProperty("PolBr")
	private String polBr;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("TreatyNametype")
	private String treatyNametype;
	@JsonProperty("Month")
	private String month;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("Underwriter")
	private String underwriter;
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("AccDate")
	private String accDate;
	@JsonProperty("OrginalCurrency")
	private String orginalCurrency;
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("Basis")
	private String basis;
	@JsonProperty("Pnoc")
	private String pnoc;
	@JsonProperty("RiskCovered")
	private String riskCovered;
	@JsonProperty("Territoryscope")
	private String territoryscope;
	@JsonProperty("Territory")
	private String territory;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("ProposalType")
	private String proposalType;
	@JsonProperty("AccountingPeriod")
	private String accountingPeriod;
	@JsonProperty("ReceiptofStatements")
	private String receiptofStatements;
	@JsonProperty("ReceiptofPayment")
	private String receiptofPayment;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("InwardType")
	private String inwardType;
	@JsonProperty("TreatyType")
	private String treatyType;
	@JsonProperty("BusinessType")
	private String businessType;
	@JsonProperty("ExchangeType")
	private String exchangeType;
	@JsonProperty("PerilCovered")
	private String perilCovered;
	@JsonProperty("LOCIssued")
	private String lOCIssued;
	@JsonProperty("UmbrellaXL")
	private String umbrellaXL;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;
	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
	@JsonProperty("TreatynoofLine")
	private String treatynoofLine;
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	@JsonProperty("RunoffYear")
	private String runoffYear;
	@JsonProperty("LocBankName")
	private String locBankName;
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
	@JsonProperty("CessionExgRate")
	private String cessionExgRate;
	@JsonProperty("FixedRate")
	private String fixedRate;
	@JsonProperty("RetentionYN")
	private String retentionYN;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("Renewalcontractno")
	private String renewalcontractno;
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
	@JsonProperty("FaclimitOrigCur")
	private String faclimitOrigCur;
	@JsonProperty("EpiorigCur")
	private String epiorigCur;
	@JsonProperty("OurEstimate")
	private String ourEstimate;
	@JsonProperty("XlCost")
	private String xlCost;
	@JsonProperty("CedReten")
	private String cedReten;
	@JsonProperty("Epi")
	private String epi;
	@JsonProperty("ShareWritt")
	private String shareWritt;
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("CedRetenType")
	private String cedRetenType;
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("MaxLimitProduct")
	private String maxLimitProduct;
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	@JsonProperty("EpiAsPerShare")
	private String epiAsPerShare;
	@JsonProperty("XlcostOurShare")
	private String xlcostOurShare;
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
	@JsonProperty("TreatyLimitsurplusOC")
	private String treatyLimitsurplusOC;
	@JsonProperty("TreatyLimitsurplusOurShare")
	private String treatyLimitsurplusOurShare;
	@JsonProperty("LimitOrigCurPml")
	private String limitOrigCurPml;
	@JsonProperty("FaclimitOrigCurPml")
	private String faclimitOrigCurPml;
	@JsonProperty("LimitOrigCurPmlOS")
	private String limitOrigCurPmlOS;
	@JsonProperty("TreatyLimitsurplusOCPml")
	private String treatyLimitsurplusOCPml;
	@JsonProperty("TreatyLimitsurplusOCPmlOS")
	private String treatyLimitsurplusOCPmlOS;
	@JsonProperty("Epipml")
	private String epipml;
	@JsonProperty("EpipmlOS")
	private String epipmlOS;
	@JsonProperty("PmlPercent")
	private String pmlPercent;
	@JsonProperty("Pml")
	private String pml;
	@JsonProperty("SubPremium")
	private String subPremium;
	@JsonProperty("XlPremium")
	private String xlPremium;
	@JsonProperty("PortfoloCovered")
	private String portfoloCovered;
	@JsonProperty("DeduchunPercent")
	private String deduchunPercent;
	@JsonProperty("MdPremium")
	private String mdPremium;
	@JsonProperty("AdjRate")
	private String adjRate;
	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	@JsonProperty("EgnpiOffer")
	private String egnpiOffer;
	@JsonProperty("OurAssessment")
	private String ourAssessment;
	@JsonProperty("Eventlimit")
	private String eventlimit;
	@JsonProperty("EventLimitOurShare")
	private String eventLimitOurShare;
	@JsonProperty("CoverLimitXL")
	private String coverLimitXL;
	@JsonProperty("CoverLimitXLOurShare")
	private String coverLimitXLOurShare;
	@JsonProperty("DeductLimitXL")
	private String deductLimitXL;
	@JsonProperty("DeductLimitXLOurShare")
	private String deductLimitXLOurShare;
	@JsonProperty("Egnpipml")
	private String egnpipml;
	@JsonProperty("EgnpipmlOurShare")
	private String egnpipmlOurShare;
	@JsonProperty("Premiumbasis")
	private String premiumbasis;
	@JsonProperty("MinimumRate")
	private String minimumRate;
	@JsonProperty("MaximumRate")
	private String maximumRate;
	@JsonProperty("BurningCostLF")
	private String burningCostLF;
	@JsonProperty("MinPremium")
	private String minPremium;
	@JsonProperty("MinPremiumOurShare")
	private String minPremiumOurShare;
	@JsonProperty("PaymentDuedays")
	private String paymentDuedays;
	@JsonProperty("DummyCon")
	private String dummyCon;
	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo;
	@JsonProperty("BaseLoginID")
	private String baseLoginID;
	@JsonProperty("EndorsmentNo")
	private String endorsmentNo;
}
