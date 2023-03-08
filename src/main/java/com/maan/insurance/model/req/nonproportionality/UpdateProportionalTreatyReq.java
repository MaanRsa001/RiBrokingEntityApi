package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateProportionalTreatyReq {
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("NewLayerNo")
	private String newlayerNo;
	@JsonProperty("LayerMode")
	private String layerMode;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("TreatyNameType")
	private String treatyNameType;
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
	@JsonProperty("Territoryscope")
	private String territoryscope;
	@JsonProperty("Territory")
	private String territory;
	@JsonProperty("ProStatus")
	private String proStatus;
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
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	@JsonProperty("LocBankName")
	private String locBankName;
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
	@JsonProperty("RetentionYN")
	private String retentionYN;
	@JsonProperty("EndNo")
	private String endNo;
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo;
	
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
	@JsonProperty("Epi")
	private String epi;
	@JsonProperty("ShareWritten")
	private String shareWritten;
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("MaxLimitProduct")
	private String maxLimitProduct;
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
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
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

	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("BaseLayer")
	private String baseLayer;

	@JsonProperty("BaseLoginId")
	private String baseLoginId;
	@JsonProperty("FlagStatus")
	private Boolean flagStatus;
	@JsonProperty("AmendStatus")
	private Boolean AmendStatus;
	@JsonProperty("Edit")
	private String edit;
	@JsonProperty("ProcessId")
	private String processId;

	@JsonProperty("LimitOrigCurPml")
	private String limitOrigCurPml;
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
	@JsonProperty("Pml")
	private String pml;
	@JsonProperty("PmlPercent")
	private String pmlPercent;
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("CedReten")
	private String cedReten;
	@JsonProperty("CoverLimitOCReq")
	private List<CoverLimitOCReq> coverLimitOCReq;
	@JsonProperty("CoverLimitAmountReq")
	private List<CoverLimitAmountreq> coverLimitAmountReq;
	@JsonProperty("EgnpiAsPerOffSlide")
	private String egnpiAsPerOffSlide;
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	@JsonProperty("EndorsementStatus")
	private String endorsementStatus;
	@JsonProperty("DocStatus")
	private String docStatus;
	@JsonProperty("OfferNo")
	private String offerNo;
	
	@JsonProperty("BouquetModeYN") //Ri
	private String bouquetModeYN;
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("UwYearTo")
	private String uwYearTo;
	@JsonProperty("AccountingPeriodNotes")
	private String accountingPeriodNotes;
	@JsonProperty("StatementConfirm")
	private String statementConfirm; 
	
	@JsonProperty("RiskdetailYN") 
	private String riskdetailYN;
	@JsonProperty("BrokerdetYN")
	private String brokerdetYN;
	@JsonProperty("CoverdetYN")
	private String coverdetYN;
	@JsonProperty("PremiumdetailYN")
	private String premiumdetailYN;
	@JsonProperty("AcqdetailYN")
	private String acqdetailYN; 
	@JsonProperty("CommissiondetailYN") 
	private String commissiondetailYN;
	@JsonProperty("DepositdetailYN")
	private String depositdetailYN;
	@JsonProperty("LossdetailYN")
	private String lossdetailYN;
	@JsonProperty("DocdetailYN")
	private String docdetailYN;
	@JsonProperty("PaymentPartner")
	private String paymentPartner; 
	@JsonProperty("InstallYN")
	private String installYN;
	@JsonProperty("ReinstdetailYN")
	private String reinstdetailYN;
	@JsonProperty("RateOnLine")
	private String rateOnLine; 
	@JsonProperty("QuotesharePercent")
	private String quotesharePercent;
	
	@JsonProperty("MinimumpremiumPercent")
	private String minimumpremiumPercent; 
	@JsonProperty("GnpiCapPercent")
	private String gnpiCapPercent;
}
