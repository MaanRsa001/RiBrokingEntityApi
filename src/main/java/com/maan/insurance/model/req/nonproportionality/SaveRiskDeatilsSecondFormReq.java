package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;

import lombok.Data;

@Data
public class SaveRiskDeatilsSecondFormReq {


	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	
	@JsonProperty("ExchRate")
	private String exchRate;
	
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	
	@JsonProperty("EpiAsPerShare")
	private String epiAsPerShare;
	
	@JsonProperty("PremiumQuotaShare")
	private String premiumQuotaShare;
	
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	
	@JsonProperty("CommissionQSAmt")
	private String commissionQSAmt;
	
	@JsonProperty("CommissionsurpAmt")
	private String commissionsurpAmt;
	
	@JsonProperty("Proposalno")
	private String proposalno;
	
	@JsonProperty("EndNo")
	private String endNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("Brokerage")
	private String brokerage;
	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	
	@JsonProperty("AcquisitionCost")
	private String acquisitionCost;
	
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
	
	@JsonProperty("Eventlimit")
	private String eventlimit;
	
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
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
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
	
	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("SharSign")
	private String sharSign;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BaseLayerYN")
	private String baseLayerYN;
	
	@JsonProperty("Lay")
	private String lay;
	
	@JsonProperty("Renewalcontractno")
	private String renewalcontractno;
	
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("SourceId")
	private String sourceId;
	
	@JsonProperty("ContractGendration")
	private String contractGendration;
	
	@JsonProperty("BaseLayer")
	private String baseLayer;
	
	@JsonProperty("EndorsementDate")
	private String endorsementDate;
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	
	@JsonProperty("AmendId")
	private String amendId;
	

}
