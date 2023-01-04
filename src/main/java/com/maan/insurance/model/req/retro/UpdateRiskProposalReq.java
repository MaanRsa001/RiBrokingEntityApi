package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateRiskProposalReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
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
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("OurAssessment")
	private String ourAssessment;
	@JsonProperty("ProductId")
	private String productId;
}
