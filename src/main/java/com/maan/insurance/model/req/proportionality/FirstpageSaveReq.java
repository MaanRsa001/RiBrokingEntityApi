package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FirstpageSaveReq {
	@JsonProperty("ContractMode")
	private String contractMode;

	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
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
	@JsonProperty("RetentionYN")
	private String retentionYN;
	@JsonProperty("RenewalcontractNo")
	private String renewalcontractNo;
	@JsonProperty("AmendId")
	private String amendId;
	
	@JsonProperty("FaclimitOrigCur")
	private String faclimitOrigCur;
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
	@JsonProperty("EpiorigCur")
	private String epiorigCur;
	@JsonProperty("OurEstimate")
	private String ourEstimate;
	@JsonProperty("XlCost")
	private String xlCost;
	@JsonProperty("CedRetent")
	private String cedRetent;
	@JsonProperty("Epi")
	private String epi;
	@JsonProperty("ShareWritten")
	private String shareWritten;
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
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo;
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
	@JsonProperty("DocdetailYN")
	private String docdetailYN;
	@JsonProperty("DepositdetailYN")
	private String depositdetailYN;
	@JsonProperty("LossdetailYN")
	private String lossdetailYN;
	
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
	
	
	@JsonProperty("SectionNo")
	private String sectionNo; 
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
	
	@JsonProperty("OfferNo")
	private String offerNo; 
	@JsonProperty("RequestNumber")
	private String requestNumber;
//	-------savesecondpage


	
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	@JsonProperty("CommissionQSAmt")
	private String commissionQSAmt;
	@JsonProperty("CommissionsurpAmt")
	private String commissionsurpAmt;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	
	@JsonProperty("acquisitionCost")
	private String AcquisitionCost;
	@JsonProperty("CommissionQS")
	private String commissionQS;
	@JsonProperty("Commissionsurp")
	private String commissionsurp;
	@JsonProperty("OverRidder")
	private String overRidder;
	@JsonProperty("PremiumReserve")
	private String premiumReserve;
	@JsonProperty("Lossreserve")
	private String lossreserve;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("PortfolioinoutPremium")
	private String portfolioinoutPremium;
	@JsonProperty("PortfolioinoutLoss")
	private String portfolioinoutLoss;
	@JsonProperty("LossAdvise")
	private String lossAdvise;
	@JsonProperty("CashLossLimit")
	private String cashLossLimit;
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	@JsonProperty("Accounts")
	private String accounts;
	@JsonProperty("Exclusion")
	private String exclusion;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("UnderwriterRecommendations")
	private String underwriterRecommendations;
	@JsonProperty("GmsApproval")
	private String gmsApproval;
	@JsonProperty("Othercost")
	private String othercost;
	@JsonProperty("CrestaStatus")
	private String crestaStatus;

	@JsonProperty("AggregateLimit")
	private String aggregateLimit;
	@JsonProperty("OccurrentLimit")
	private String occurrentLimit;
	@JsonProperty("SlideScaleCommission")
	private String slideScaleCommission;
	@JsonProperty("LossParticipants")
	private String lossParticipants;
	@JsonProperty("CommissionSubClass")
	private String commissionSubClass;
	
	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;
	@JsonProperty("Orginalacqcost")
	private String orginalacqcost;
	@JsonProperty("Ourassessmentorginalacqcost")
	private String ourassessmentorginalacqcost;
	@JsonProperty("OuracqCost")
	private String ouracqCost;
	@JsonProperty("LosscommissionSubClass")
	private String losscommissionSubClass;
	@JsonProperty("SlidecommissionSubClass")
	private String slidecommissionSubClass;
	@JsonProperty("CrestacommissionSubClass")
	private String crestacommissionSubClass;
	@JsonProperty("ManagementExpenses")
	private String managementExpenses;
	@JsonProperty("CommissionType")
	private String commissionType;
	@JsonProperty("ProfitCommissionPer")
	private String profitCommissionPer;
	@JsonProperty("Setup")
	private String setup;
	@JsonProperty("SuperProfitCommission")
	private String superProfitCommission;
	@JsonProperty("LossCarried")
	private String lossCarried;
	@JsonProperty("Lossyear")
	private String lossyear;
	@JsonProperty("ProfitCarried")
	private String profitCarried;
	@JsonProperty("ProfitCarriedForYear")
	private String profitCarriedForYear;
	@JsonProperty("Fistpc")
	private String fistpc;
	@JsonProperty("ProfitMont")
	private String profitMont;
	@JsonProperty("SubProfitMonth")
	private String subProfitMonth;
	@JsonProperty("Subpc")
	private String subpc;
	@JsonProperty("SubSeqCalculation")
	private String subSeqCalculation;
	@JsonProperty("ProfitCommission")
	private String profitCommission;
	@JsonProperty("DocStatus")
	private String docStatus;
	@JsonProperty("LocRate")
	private String locRate;

	@JsonProperty("EndorsementStatus")
	private String endorsementStatus;
	@JsonProperty("AccDate")
	private String accDate;
	
	@JsonProperty("PreviousendoDate")
	private String previousendoDate;
	@JsonProperty("EndorsementDate")
	private String endorsementDate;

	@JsonProperty("MaxDate")
	private String maxDate;
	@JsonProperty("NoInsurer")
	private String NoInsurer;
	
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;



	@JsonProperty("CrestaPopUp")
	private String crestaPopUp;
	
	@JsonProperty("SlidePopUp")
	private String slidePopUp;

	@JsonProperty("LossPopUp")
	private String lossPopUp;
	@JsonProperty("ProfitPopUp")
	private String profitPopUp;
	
	@JsonProperty("EpiOSViewOC")
	private String epiOSViewOC;
	@JsonProperty("ShareValue")
	private String shareValue;
	
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("RetroDupContract")
	private String retroDupContract; 

	@JsonProperty("RetroListReq")
	private List<RetroListReq> retroListReq; 
	
	@JsonProperty("PremiumResType") //Ri
	private String premiumResType;
	@JsonProperty("PcfpcType")
	private String pcfpcType;
	@JsonProperty("PcfixedDate")
	private String pcfixedDate;
	@JsonProperty("PortfolioType")
	private String portfolioType;


}
