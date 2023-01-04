package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCliamDetailsMode2Req {
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CreatedDate")
	private String createdDate;
	
	@JsonProperty("StatusofClaim")
	private String statusofClaim;
	
	@JsonProperty("DateofLoss")
	private String dateofLoss;
	
	@JsonProperty("ReportDate")
	private String reportDate;
	
	
	@JsonProperty("LossDetails")
	private String lossDetails;
	
	
	@JsonProperty("CauseofLoss")
	private String causeofLoss;
	
	@JsonProperty("Currecny")
	private String currecny;
	
	@JsonProperty("LossEstimateOrigCurr")
	private String lossEstimateOrigCurr;
	
	@JsonProperty("LossEstimateOurShareOrigCurr")
	private String lossEstimateOurShareOrigCurr;
	
	@JsonProperty("ExcRate")
	private String excRate;
	
	@JsonProperty("AdviceUW")
	private String adviceUW;
	
	@JsonProperty("AdviceMangement")
	private String adviceMangement;
	
	@JsonProperty("RiRecovery")
	private String riRecovery;


	
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	
	@JsonProperty("Location")
	private String location;
	
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("AdviceUwEmail")
	private String adviceUwEmail;
	
	@JsonProperty("AdviceManagementEmail")
	private String adviceManagementEmail;
	
	@JsonProperty("RiskCode")
	private String riskCode;
	
	@JsonProperty("AccumulationCode")
	private String accumulationCode;
	
	@JsonProperty("EventCode")
	private String eventCode;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	
	@JsonProperty("RecordFees")
	private String recordFees;
	
	
	@JsonProperty("SurveyorAdjesterPerOC")
	private String surveyorAdjesterPerOC;
	
	@JsonProperty("OtherProfessionalPerOc")
	private String otherProfessionalPerOc;
	
	@JsonProperty("IbnrPerOc")
	private String ibnrPerOc;
	
	@JsonProperty("RecordIbnr")
	private String recordIbnr;
	
	@JsonProperty("CedentClaimNo")
	private String cedentClaimNo;
	
	@JsonProperty("SurveyorAdjesterOurShareOC")
	private String surveyorAdjesterOurShareOC;
	
	@JsonProperty("ProfessionalOurShareOc")
	private String professionalOurShareOc;
	
	@JsonProperty("IbnrOurShareOc")
	private String ibnrOurShareOc;
	
	@JsonProperty("ReOpenDate")
	private String reOpenDate;
	
	
	@JsonProperty("GrossLossFGU")
	private String grossLossFGU;
	
	
	@JsonProperty("ClaimdepartId")
	private String claimdepartId;
	
	@JsonProperty("DepartmentClass")
	private String departmentClass;
	
	@JsonProperty("SubProfitId")
	private String subProfitId;
	
	@JsonProperty("ReservePositionDate")
	private String reservePositionDate;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("TotalReserveOSOC")
	private String totalReserveOSOC;
	
	@JsonProperty("OsAmt")
	private String osAmt;
	@JsonProperty("Basis")
	private String basis;
	
	@JsonProperty("From")
	private String from;
	
	@JsonProperty("To")
	private String to;
	
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
	
	@JsonProperty("OpstartDate")
	private String opstartDate;	
	
	@JsonProperty("OpendDate")
	private String opendDate;	
	
	@JsonProperty("OpenPeriodDate")
	private String openPeriodDate;
	
	@JsonProperty("SumInsOSDC")
	private String sumInsOSDC;
	
	@JsonProperty("SignedShare")
	private String signedShare;
	
	@JsonProperty("PreReOpenDate")
	private String preReOpenDate;
	
	@JsonProperty("ReputedDate")
	private String reputedDate;
	
	@JsonProperty("RecoveryFrom")
	private String recoveryFrom;
	
	
}
