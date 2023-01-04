package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.retro.FirstInsertReq;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsRes1 {
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	@JsonProperty("XlcostOurShare")
	private String xlcostOurShare;
	@JsonProperty("LimitOSViewOC")
	private String limitOSViewOC;
	@JsonProperty("EpiOSViewOC")
	private String epiOSViewOC;
	@JsonProperty("EpiOSOEViewOC")
	private String epiOSOEViewOC;
	@JsonProperty("XlCostViewOC")
	private String xlCostViewOC;
	@JsonProperty("LimitOSViewDC")
	private String limitOSViewDC;
	@JsonProperty("EpiOSViewDC")
	private String epiOSViewDC;
	@JsonProperty("EpiOSOEViewDC")
	private String epiOSOEViewDC;
	@JsonProperty("XlCostViewDC")
	private String xlCostViewDC;
	@JsonProperty("CommissionQS")
	private String commissionQS;
	@JsonProperty("Commissionsurp")
	private String commissionsurp;
	@JsonProperty("OverRidder")
	private String overRidder;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("AcquisitionCost")
	private String acquisitionCost;
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	@JsonProperty("ManagementExpenses")
	private String managementExpenses;
	@JsonProperty("LossCF")
	private String lossCF;
	@JsonProperty("PremiumReserve")
	private String premiumReserve;
	@JsonProperty("Lossreserve")
	private String lossreserve;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("CashLossLimit")
	private String cashLossLimit;
	@JsonProperty("PortfolioinoutPremium")
	private String portfolioinoutPremium;
	@JsonProperty("PortfolioinoutLoss")
	private String portfolioinoutLoss;
	@JsonProperty("LossAdvise")
	private String lossAdvise;
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	@JsonProperty("Accounts")
	private String accounts;
	@JsonProperty("CrestaStatus")
	private String crestaStatus;
	@JsonProperty("AggregateLimit")
	private String aggregateLimit;
	@JsonProperty("OccurrentLimit")
	private String occurrentLimit;
	@JsonProperty("Exclusion")
	private String exclusion;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("UnderwriterRecommendations")
	private String underwriterRecommendations;
	@JsonProperty("GmsApproval")
	private String gmsApproval;
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
	@JsonProperty("CommissionQSYN")
	private String commissionQSYN;
	@JsonProperty("CommissionsurpYN")
	private String commissionsurpYN;
	@JsonProperty("OverRidderYN")
	private String overRidderYN;
	@JsonProperty("BrokerageYN")
	private String brokerageYN;
	@JsonProperty("TaxYN")
	private String taxYN;
	@JsonProperty("OthercostYN")
	private String othercostYN;
	@JsonProperty("CeedODIYN")
	private String ceedODIYN;
	@JsonProperty("LocRate")
	private String locRate;
	@JsonProperty("RetroCommissionType")
	private String retroCommissionType;
	@JsonProperty("Othercost")
	private String othercost;
	@JsonProperty("EpiAsPerShare")
	private String epiAsPerShare;
	@JsonProperty("AcqCostPer")
	private String acqCostPer;
	
	
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
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	@JsonProperty("MandDpreViewOC")
	private String mandDpreViewOC;
	@JsonProperty("MandDpreViewDC")
	private String mandDpreViewDC;
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	@JsonProperty("ReinstNo")
	private String reinstNo;
	@JsonProperty("ReinstAdditionalPremium")
	private String reinstAdditionalPremium;
	@JsonProperty("ReinstAditionalPremiumpercent")
	private String reinstAditionalPremiumpercent;
	@JsonProperty("BurningCost")
	private String burningCost;
	@JsonProperty("InstalmentDateList")
	private List<String> instalmentDateList;
	
}
