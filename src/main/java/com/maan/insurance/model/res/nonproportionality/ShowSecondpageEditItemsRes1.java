package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.res.nonproportionality.NoInsurerRes;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsRes1 {


	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	
	@JsonProperty("LimitOSViewOC")
	private String limitOSViewOC;
	
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	
	@JsonProperty("EpiOSViewOC")
	private String epiOSViewOC;

	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	
	@JsonProperty("MandDpreViewOC")
	private String mandDpreViewOC;
	
	@JsonProperty("MandDpreViewDC")
	private String mandDpreViewDC;
	
	@JsonProperty("ReinstNo")
	private String reinstNo;
	
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	
	@JsonProperty("ReinstAdditionalPremium")
	private String reinstAdditionalPremium;
	
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;
	
	@JsonProperty("AcqBonus")
	private String acqBonus;
	
	@JsonProperty("CrestaStatus")
	private String crestaStatus;

	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;
	
	@JsonProperty("AnualAggregateDeduct")
	private String anualAggregateDeduct;
	
	@JsonProperty("BurningCost")
	private String burningCost;
	
	@JsonProperty("ReinstAditionalPremiumpercent")
	private String reinstAditionalPremiumpercent;
	
	@JsonProperty("ReInstatementPremium")
	private String reInstatementPremium;
//	@JsonProperty("EpiAsPerShare")
//	private String epiAsPerShare;
//	@JsonProperty("EpiOSOEViewOC")
//	private String epiOSOEViewOC;
//	
//	@JsonProperty("XlcostOurShare")
//	private String xlcostOurShare;
//	
//	@JsonProperty("XlCostViewOC")
//	private String xlCostViewOC;
	
	@JsonProperty("LimitOSViewDC")
	private String limitOSViewDC;
	
	@JsonProperty("EpiOSViewDC")
	private String epiOSViewDC;
//	@JsonProperty("EpiOSOEViewDC")
//	private String epiOSOEViewDC;
//	
//	@JsonProperty("XlCostViewDC")
//	private String xlCostViewDC;
//	
//	@JsonProperty("CommissionQS")
//	private String commissionQS;
//	
//	@JsonProperty("Commissionsurp")
//	private String commissionsurp;
//	
//	@JsonProperty("OverRidder")
//	private String overRidder;
	@JsonProperty("Brokerage")
	private String brokerage;
	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("AcquisitionCost")
	private String acquisitionCost;
	
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	
//	@JsonProperty("PremiumReserve")
//	private String premiumReserve;
//	@JsonProperty("Lossreserve")
//	private String lossreserve;
//	
//	@JsonProperty("Interest")
//	private String interest;
//	
//	@JsonProperty("CashLossLimit")
//	private String cashLossLimit;
//	
//	@JsonProperty("PortfolioinoutPremium")
//	private String portfolioinoutPremium;
//	
//	@JsonProperty("PortfolioinoutLoss")
//	private String portfolioinoutLoss;
//	@JsonProperty("LossAdvise")
//	private String lossAdvise;
	
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	
	@JsonProperty("Accounts")
	private String accounts;
	
//	@JsonProperty("CrestaStatus")
//	private String crestaStatus;
//	@JsonProperty("Eventlimit")
//	private String eventlimit;
//	
//	@JsonProperty("AggregateLimit")
//	private String aggregateLimit;
	
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
	
//	@JsonProperty("SlideScaleCommission")
//	private String slideScaleCommission;
//	
//	@JsonProperty("LossParticipants")
//	private String lossParticipants;
//	
//	@JsonProperty("CommissionSubClass")
//	private String commissionSubClass;
//	@JsonProperty("LeaderUnderwritercountry")
//	private String leaderUnderwritercountry;
//	
//	@JsonProperty("Orginalacqcost")
//	private String orginalacqcost;
//	
//	@JsonProperty("Ourassessmentorginalacqcost")
//	private String ourassessmentorginalacqcost;
//	
//	@JsonProperty("OuracqCost")
//	private String ouracqCost;
//	
//	@JsonProperty("ProfitCommission")
//	private String profitCommission;
//	@JsonProperty("LosscommissionSubClass")
//	private String losscommissionSubClass;
//	
//	@JsonProperty("SlidecommissionSubClass")
//	private String slidecommissionSubClass;
//	
//	@JsonProperty("CrestacommissionSubClass")
//	private String crestacommissionSubClass;
//	
//	@JsonProperty("ManagementExpenses")
//	private String managementExpenses;
//	
//	@JsonProperty("CommissionType")
//	private String commissionType;
//	@JsonProperty("SuperProfitCommission")
//	private String superProfitCommission;
//	@JsonProperty("Setup")
//	private String setup;
//	
//	@JsonProperty("LocRate")
//	private String locRate;
	
	@JsonProperty("Othercost")
	private String othercost;
	
//	@JsonProperty("AcqCostPer")
//	private String acqCostPer;
//	
//	@JsonProperty("PremiumQuotaShare")
//	private String premiumQuotaShare;
//	@JsonProperty("CommissionQSAmt")
//	private String commissionQSAmt;
//	
//	@JsonProperty("CommissionsurpAmt")
//	private String commissionsurpAmt;
//	
//	@JsonProperty("NoInsurer")
//	private String noInsurer;
//	
//
//	
//	@JsonProperty("Subpc")
//	private String subpc;
//	@JsonProperty("LossCarried")
//	private String lossCarried;
//	
//	@JsonProperty("Lossyear")
//	private String lossyear;
//	
//	@JsonProperty("ProfitCarried")
//	private String profitCarried;
//	
//	@JsonProperty("ProfitCarriedForYear")
//	private String profitCarriedForYear;
	@JsonProperty("NoInsurerRes")
	private List<NoInsurerRes> noInsurerRes;

	
	@JsonProperty("InstalmentListRes")
	private List<InstalmentListRes> instalmentList;
}
