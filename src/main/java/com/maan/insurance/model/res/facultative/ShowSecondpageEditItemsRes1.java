package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.res.proportionality.NoInsurerRes;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsRes1 {
	@JsonProperty("RiskGrade")
	private String riskGrade;
	
	@JsonProperty("OccCode")
	private String occCode;
	
	@JsonProperty("RiskDetail")
	private String riskDetail;
	
	@JsonProperty("FireProt")
	private String fireProt;
	
	@JsonProperty("Scope")
	private String scope;
	
	@JsonProperty("Mbind")
	private String mbind;
	
	@JsonProperty("CategoryZone")
	private String categoryZone;
	
	@JsonProperty("EqwsInd")
	private String eqwsInd;
	
	@JsonProperty("WsThreat")
	private String wsThreat;
	
	@JsonProperty("EqThreat")
	private String eqThreat;
	
	@JsonProperty("Commn")
	private String commn;
	
	@JsonProperty("Brokerage")
	private String brokerage;
	
	@JsonProperty("AcqBonus")
	private String acqBonus;
	
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;
	
	@JsonProperty("LossRecord")
	private String lossRecord;
	
	@JsonProperty("DgmsApproval")
	private String dgmsApproval;
	
	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("UnderwriterCode")
	private String underwriterCode;
	
	@JsonProperty("UwRecommendation")
	private String uwRecommendation;
	
	@JsonProperty("OthAccep")
	private String othAccep;
	
	@JsonProperty("ReftoHO")
	private String reftoHO;
	
	@JsonProperty("AcqCost")
	private String acqCost;
	
	@JsonProperty("Accusd")
	private String accusd;
	
	@JsonProperty("CuRsn")
	private String cuRsn;
	
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
	@JsonProperty("Eventlimit")
	private String eventlimit;
	
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
	
	@JsonProperty("Ourassessmentorginalacqcost")
	private String ourassessmentorginalacqcost;
	
	@JsonProperty("OuracqCost")
	private String ouracqCost;
	
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
	@JsonProperty("SuperProfitCommission")
	private String superProfitCommission;
	@JsonProperty("Setup")
	private String setup;
	
	@JsonProperty("LocRate")
	private String locRate;
	
	@JsonProperty("Othercost")
	private String othercost;
	
	@JsonProperty("AcqCostPer")
	private String acqCostPer;
	
	@JsonProperty("MlopYN")
	private String mlopYN;
	
	@JsonProperty("AlopYN")
	private String alopYN;
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	
	@JsonProperty("NoInsurer")
	private String noInsurer;
	

	
	@JsonProperty("Subpc")
	private String subpc;
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
	
	@JsonProperty("SubSeqCalculation")
	private String subSeqCalculation;
	@JsonProperty("ProfitCommissionPer")
	private String profitCommissionPer;
	
	@JsonProperty("InstalmentList")
	private List<InstalmentListRes> instalmentList;
}
