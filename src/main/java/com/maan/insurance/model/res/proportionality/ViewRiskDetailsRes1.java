package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.proportionality.MappingProposalRes;

import lombok.Data;

@Data
public class ViewRiskDetailsRes1 {
	
	@JsonProperty("DepartId")
	private String departId;
	
	@JsonProperty("DepartClass")
	private String departClass;
	
	@JsonProperty("ProposalType")
	private String proposalType;

	@JsonProperty("AccountingPeriod")
	private String accountingPeriod;
	
	@JsonProperty("ReceiptofPayment")
	private String receiptofPayment;
	
	@JsonProperty("ReceiptofStatements")
	private String receiptofStatements;
	
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;

	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("TreatyNametype")
	private String treatyNametype;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;

	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
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
	
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	
	@JsonProperty("RetroType")
	private String retroType;

	@JsonProperty("NoRetroCess")
	private String NoRetroCess;
	
	@JsonProperty("Pnoc")
	private String pnoc;
	
	@JsonProperty("RiskCovered")
	private String riskCovered;
	
	@JsonProperty("Territoryscope")
	private String territoryscope;
	
	@JsonProperty("Territory")
	private String territory;

	@JsonProperty("InwardType")
	private String inwardType;
	
	@JsonProperty("TreatyType")
	private String treatyType;
	
	@JsonProperty("TreatyName")
	private String treatyName;
	
	@JsonProperty("LOCIssued")
	private String lOCIssued;
	
	@JsonProperty("PerilCovered")
	private String perilCovered;

	@JsonProperty("TreatynoofLine")
	private String treatynoofLine;
	
	@JsonProperty("RetentionYN")
	private String retentionYN;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("RunoffYear")
	private String runoffYear;

	@JsonProperty("LocBankName")
	private String locBankName;
	
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
	
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;

	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
	
	@JsonProperty("CountryExcludedName")
	private String countryExcludedName;
	
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	
	@JsonProperty("FaclimitOrigCur")
	private String faclimitOrigCur;
	
	@JsonProperty("FacLimitOrigCurDc")
	private String facLimitOrigCurDc;

	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;
	
	@JsonProperty("LimitOrigCurDc")
	private String limitOrigCurDc;
	
	@JsonProperty("EpiorigCur")
	private String epiorigCur;
	
	@JsonProperty("EpiorigCurDc")
	private String epiorigCurDc;
	
	@JsonProperty("OurEstimate")
	private String ourEstimate;

	@JsonProperty("XlPremium")
	private String xlPremium;
	
	@JsonProperty("XlPremiumDc")
	private String xlPremiumDc;
	
	@JsonProperty("DeduchunPercent")
	private String deduchunPercent;
	
	@JsonProperty("EpiEstmate")
	private String epiEstmate;
	
	@JsonProperty("EpiEstmateDc")
	private String epiEstmateDc;

	@JsonProperty("XlCost")
	private String xlCost;
	
	@JsonProperty("XlCostDc")
	private String xlCostDc;
	
	@JsonProperty("CedReten")
	private String cedReten;
	
	@JsonProperty("MdPremium")
	private String mdPremium;
	
	@JsonProperty("MdpremiumDc")
	private String mdpremiumDc;

	@JsonProperty("AdjRate")
	private String adjRate;
	
	@JsonProperty("PortfoloCovered")
	private String portfoloCovered;
	
	@JsonProperty("SubPremium")
	private String subPremium;
	
	@JsonProperty("SubPremiumDc")
	private String subPremiumDc;
	
	@JsonProperty("MaxLimitProduct")
	private String maxLimitProduct;

	@JsonProperty("DeduchunPercentDc")
	private String deduchunPercentDc;
	
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	
	@JsonProperty("LimitOurShareDc")
	private String limitOurShareDc;
	
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	
	@JsonProperty("EpiAsPerOfferDc")
	private String epiAsPerOfferDc;
	
	@JsonProperty("EpiOurShareEs")
	private String epiOurShareEs;
	
	@JsonProperty("EpiOurShareEsDc")
	private String epiOurShareEsDc;

	@JsonProperty("XlcostOurShare")
	private String xlcostOurShare;
	
	@JsonProperty("XlcostOurShareDc")
	private String xlcostOurShareDc;
	
	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	
	@JsonProperty("MdpremiumourserviceDc")
	private String mdpremiumourserviceDc;
	
	@JsonProperty("SpRetro")
	private String spRetro;

	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("CedRetenType")
	private String cedRetenType;
	
	@JsonProperty("TreatyLimitsurplusOC")
	private String treatyLimitsurplusOC;
	
	@JsonProperty("TreatyLimitsurplusDC")
	private String treatyLimitsurplusDC;
	
	@JsonProperty("TreatyLimitsurplusOurShare")
	private String treatyLimitsurplusOurShare;
	
	@JsonProperty("TreatyLimitsurplusOurShareDC")
	private String treatyLimitsurplusOurShareDC;
	
	@JsonProperty("Pml")
	private String pml;

	@JsonProperty("PmlPercent")
	private String pmlPercent;
	
	@JsonProperty("LimitOrigCurPml")
	private String limitOrigCurPml;
	
	@JsonProperty("LimitOrigCurPmlDC")
	private String limitOrigCurPmlDC;
	
	@JsonProperty("LimitOrigCurPmlOS")
	private String limitOrigCurPmlOS;
	
	@JsonProperty("LimitOrigCurPmlOSDC")
	private String limitOrigCurPmlOSDC;

	@JsonProperty("TreatyLimitsurplusOCPml")
	private String treatyLimitsurplusOCPml;
	
	@JsonProperty("TreatyLimitsurplusOCPmlDC")
	private String treatyLimitsurplusOCPmlDC;
	
	@JsonProperty("TreatyLimitsurplusOCPmlOS")
	private String treatyLimitsurplusOCPmlOS;
	
	@JsonProperty("TreatyLimitsurplusOCPmlOSDC")
	private String treatyLimitsurplusOCPmlOSDC;
	
//	@JsonProperty("InstalmentDateList")
//	private String instalmentDateList;
//	
//	@JsonProperty("InstallmentPremium")
//	private String installmentPremium;
	
	@JsonProperty("Brokerage")
	private String brokerage;

	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	
	@JsonProperty("PremiumQuotaShareDC")
	private String premiumQuotaShareDC;
	
	@JsonProperty("PremiumSurplusDC")
	private String premiumSurplusDC;

	@JsonProperty("PremiumQuotaShareOSDC")
	private String premiumQuotaShareOSDC;
	
	@JsonProperty("PremiumSurplusOSDC")
	private String premiumSurplusOSDC;
	
	@JsonProperty("AcquisitionCost")
	private String acquisitionCost;
	
	@JsonProperty("AcquisitionCostDc")
	private String acquisitionCostDc;
	
	@JsonProperty("LossAdvise")
	private String lossAdvise;
	
	@JsonProperty("LossAdviseDc")
	private String lossAdviseDc;
	
	@JsonProperty("CashLossLimit")
	private String cashLossLimit;

	@JsonProperty("CashLossLimitDc")
	private String cashLossLimitDc;
	
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	
	@JsonProperty("AnualAggregateLiabilityDc")
	private String anualAggregateLiabilityDc;
	
	@JsonProperty("ReinstNo")
	private String reinstNo;
	
	@JsonProperty("ReinstAditionalPremiumpercent")
	private String reinstAditionalPremiumpercent;

	@JsonProperty("ReinstAditionalPremiumpercentDc")
	private String reinstAditionalPremiumpercentDc;
	
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	
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

	@JsonProperty("Decision")
	private String decision;
	
	@JsonProperty("ReinstAdditionalPremium")
	private String reinstAdditionalPremium;
	
	@JsonProperty("BurningCost")
	private String burningCost;
	
	@JsonProperty("CommissionQSAmt")
	private String commissionQSAmt;
	
	@JsonProperty("CommissionsurpAmt")
	private String commissionsurpAmt;

	@JsonProperty("CommissionQSAmtDC")
	private String commissionQSAmtDC;
	
	@JsonProperty("CommissionsurpAmtDC")
	private String commissionsurpAmtDC;
	
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	
	@JsonProperty("LimitPerVesselDC")
	private String limitPerVesselDC;
	
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
	
	@JsonProperty("LimitPerLocationDC")
	private String limitPerLocationDC;
	
	@JsonProperty("EndorsementDate")
	private String endorsementDate;

	@JsonProperty("Eventlimit")
	private String eventlimit;
	
	@JsonProperty("EventlimitDC")
	private String eventlimitDC;
	
	@JsonProperty("AggregateLimit")
	private String aggregateLimit;
	
	@JsonProperty("AggregateLimitDC")
	private String aggregateLimitDC;
	
	@JsonProperty("OccurrentLimit")
	private String occurrentLimit;

	@JsonProperty("OccurrentLimitDC")
	private String occurrentLimitDC;
	
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
	
	@JsonProperty("OuracqCost")
	private String ouracqCost;

	@JsonProperty("Ourassessmentorginalacqcost")
	private String ourassessmentorginalacqcost;
	
	@JsonProperty("ProfitCommission")
	private String profitCommission;
	
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
	
	@JsonProperty("CrestaStatus")
	private String crestaStatus;

	@JsonProperty("LocRate")
	private String locRate;
	
	@JsonProperty("ContractListVal")
	private String contractListVal;
	

	
	@JsonProperty("BaseLayer")
	private String baseLayer;

	@JsonProperty("Basis")
	private String basis;
	
	@JsonProperty("TerritoryName")
	private String territoryName;
	@JsonProperty("CountryIncludedName")
	private String  countryIncludedName;
	@JsonProperty("ShareWritt")
	private String shareWritt;
	@JsonProperty("SharSign")
	private String  sharSign;
	@JsonProperty("Tax")
	private String  tax;
	
	@JsonProperty("PremiumQuotaShareOSOC")
	private String  premiumQuotaShareOSOC;
	
	@JsonProperty("PremiumSurplusOSOC")
	private String 	premiumSurplusOSOC;
	@JsonProperty("AcquisitionCostOSOC")
	private String 	acquisitionCostOSOC;
	@JsonProperty("AcquisitionCostOSDC")
	private String 	acquisitionCostOSDC;
	@JsonProperty("CommissionQS")
	private String 	commissionQS;
	@JsonProperty("Commissionsurp")
	private String 	commissionsurp;
	@JsonProperty("OverRidder")
	private String overRidder;
	@JsonProperty("InstalmentList")
	private List<InstalmentListRes> instalmentList;
	@JsonProperty("MappingProposal")
	private List<MappingProposalRes> mappingProposal;
	
	@JsonProperty("PremiumReserve")
	private String  premiumReserve;
	
	@JsonProperty("Lossreserve")
	private String  lossreserve;
	
	@JsonProperty("Interest")
	private String 	interest;
	@JsonProperty("PortfolioinoutPremium")
	private String 	portfolioinoutPremium;
	@JsonProperty("PortfolioinoutLoss")
	private String 	portfolioinoutLoss;
	@JsonProperty("LossAdviseOSOC")
	private String 	lossAdviseOSOC;
	@JsonProperty("LossAdviseOSDC")
	private String 	lossAdviseOSDC;
	@JsonProperty("CashLossLimitOSOC")
	private String cashLossLimitOSOC;
	@JsonProperty("CashLossLimitOSDC")
	private String cashLossLimitOSDC;
	@JsonProperty("LeaderUnderwritershare")
	private String 	leaderUnderwritershare;
	@JsonProperty("Othercost")
	private String 	othercost;
	@JsonProperty("AcqCostPer")
	private String 	acqCostPer;
	@JsonProperty("AcqCostPerDC")
	private String 	acqCostPerDC;
	@JsonProperty("LimitPerVesselOSOC")
	private String 	limitPerVesselOSOC;
	@JsonProperty("LimitPerVesselOSDC")
	private String limitPerVesselOSDC;
	@JsonProperty("LimitPerLocationOSOC")
	private String limitPerLocationOSOC;
	@JsonProperty("LimitPerLocationOSDC")
	private String 	limitPerLocationOSDC;
	@JsonProperty("EventlimitOSOC")
	private String 	eventlimitOSOC;
	@JsonProperty("EventlimitOSDC")
	private String eventlimitOSDC;
	@JsonProperty("AggregateLimitOSOC")
	private String 	aggregateLimitOSOC;
	@JsonProperty("AggregateLimitOSDC")
	private String aggregateLimitOSDC;
	@JsonProperty("OccurrentLimitOSOC")
	private String occurrentLimitOSOC;
	@JsonProperty("OccurrentLimitOSDC")
	private String occurrentLimitOSDC;
	@JsonProperty("ProfitCommissionPer")
	private String profitCommissionPer;
}
