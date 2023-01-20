package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RiskDetailsEditModeReq;

import lombok.Data;

@Data
public class RiskDetailsEditModeRes1 {


	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
//	@JsonProperty("BranchCode")
//	private String branchCode;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	@JsonProperty("PolicyBranch")
	private String policyBranch;
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
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("AcceptanceDate")
	private String AcceptanceDate;
	@JsonProperty("OrginalCurrency")
	private String orginalCurrency;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("Basis")
	private String basis;
//	@JsonProperty("Pnoc")
//	private String pnoc;
//	@JsonProperty("RiskCovered")
//	private String riskCovered;
	@JsonProperty("Territoryscope")
	private String territoryscope;
	@JsonProperty("Territory")
	private String territory;
	@JsonProperty("ProStatus")
	private String proStatus;
//	@JsonProperty("ProposalType")
//	private String proposalType;
//	@JsonProperty("AccountingPeriod")
//	private String accountingPeriod;
//	@JsonProperty("ReceiptofStatements")
//	private String receiptofStatements;
//	@JsonProperty("ReceiptofPayment")
//	private String receiptofPayment;
	@JsonProperty("SubPremium")
	private String subPremium;
	@JsonProperty("XlPremium")
	private String xlPremium;
	@JsonProperty("DeduchunPercent")
	private String deduchunPercent;
	@JsonProperty("MdPremium")
	private String mdPremium;
	@JsonProperty("InwardType")
	private String inwardType;
//	@JsonProperty("TreatyType")
//	private String treatyType;
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
//	@JsonProperty("LoginId")
//	private String loginId;
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;
	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
//	@JsonProperty("TreatynoofLine")
//	private String treatynoofLine;
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	@JsonProperty("EndorsmentNo")
	private String endorsmentNo;
//	@JsonProperty("RunoffYear")
//	private String runoffYear;
	@JsonProperty("LocBankName")
	private String locBankName;
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
//	@JsonProperty("RetentionYN")
//	private String retentionYN;
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo;
//	@JsonProperty("AmendId")
//	private String amendId;
	
//	@JsonProperty("FaclimitOrigCur")
//	private String faclimitOrigCur;
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
//	@JsonProperty("EpiorigCur")
//	private String epiorigCur;
//	@JsonProperty("OurEstimate")
//	private String ourEstimate;
//	@JsonProperty("XlCost")
//	private String xlCost;
//	@JsonProperty("CedRetent")
//	private String cedRetent;
	@JsonProperty("Epi")
	private String epi;
	@JsonProperty("ShareWritten")
	private String shareWritten;
	@JsonProperty("SharSign")
	private String sharSign;
//	@JsonProperty("CedRetenType")
//	private String cedRetenType;
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("MaxLimitProduct")
	private String maxLimitProduct;
	@JsonProperty("AdjRate")
	private String adjRate;
	@JsonProperty("PortfoloCovered")
	private String portfoloCovered;
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
//	@JsonProperty("XlcostOurShare")
//	private String xlcostOurShare;
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
//	@JsonProperty("TreatyLimitsurplusOC")
//	private String treatyLimitsurplusOC;
//	@JsonProperty("TreatyLimitsurplusOurShare")
//	private String treatyLimitsurplusOurShare;
//	@JsonProperty("LimitOrigCurPml")
//	private String limitOrigCurPml;
//	@JsonProperty("LimitOrigCurPmlOS")
//	private String limitOrigCurPmlOS;
//	@JsonProperty("TreatyLimitsurplusOCPml")
//	private String treatyLimitsurplusOCPml;
//	@JsonProperty("TreatyLimitsurplusOCPmlOS")
//	private String treatyLimitsurplusOCPmlOS;
//	@JsonProperty("Epipml")
//	private String epipml;
//	@JsonProperty("EpipmlOS")
//	private String epipmlOS;
	@JsonProperty("Pml")
	private String pml;
	@JsonProperty("PmlPercent")
	private String pmlPercent;
	
	
	@JsonProperty("Eventlimit")
	private String eventlimit;
//	@JsonProperty("EventLimitOurShare")
//	private String eventLimitOurShare;
	@JsonProperty("CoverLimitXL")
	private String coverLimitXL;
//	@JsonProperty("CoverLimitXLOurShare")
//	private String coverLimitXLOurShare;
	@JsonProperty("DeductLimitXL")
	private String deductLimitXL;
//	@JsonProperty("DeductLimitXLOurShare")
//	private String deductLimitXLOurShare;
	@JsonProperty("Egnpipml")
	private String egnpipml;
//	@JsonProperty("EgnpipmlOurShare")
//	private String egnpipmlOurShare;
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
//	@JsonProperty("MinPremiumOurShare")
//	private String minPremiumOurShare;
	@JsonProperty("PaymentDuedays")
	private String paymentDuedays;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("BaseLayer")
	private String baseLayer;
//	@JsonProperty("LayerProposalNo")
//	private String layerProposalNo;
	@JsonProperty("BaseLoginId")
	private String baseLoginId;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("PrclFlag")
	private Boolean  prclFlag;
//	@JsonProperty("RemarksList")
//	private List<RemarksReq> remarksReq;
//	@JsonProperty("RemarksList")
//	private List<CedentRetentReq> cedentRetentReq;
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
	@JsonProperty("EgnpiOffer")
	private String egnpiOffer;
	@JsonProperty("EgnpiOfferDC")
	private String egnpiOfferDC;
	@JsonProperty("OurAssessment")
	private String ourAssessment;
	
	@JsonProperty("BouquetNo") //RI
	private String bouquetNo;
	
	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN; 
	
	@JsonProperty("OfferNo")
	private String offerNo; 
	
	@JsonProperty("UwYearTo")
	private String uwYearTo; 
	
	@JsonProperty("PaymentPartner")
	private String paymentPartner;
	@JsonProperty("RateOnLine")
	private String rateOnLine;
	@JsonProperty("RiskdetailYN")
	private String riskdetailYN;
	@JsonProperty("BrokerdetYN")
	private String brokerdetYN;
	
	@JsonProperty("PremiumdetailYN")
	private String premiumdetailYN;
	@JsonProperty("InstallYN")
	private String installYN;
	@JsonProperty("AcqdetailYN")
	private String acqdetailYN;
	@JsonProperty("ReinstdetailYN")
	private String reinstdetailYN;
	
}
