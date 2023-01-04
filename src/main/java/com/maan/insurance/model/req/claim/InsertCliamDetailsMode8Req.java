package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCliamDetailsMode8Req {
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("ReverseClaimYN")
	private String reverseClaimYN;
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("ExcRate")
	private String excRate;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("UpdateRivisedoriginalCur")
	private String updateRivisedoriginalCur;
	
	@JsonProperty("UpdateReference")
	private String updateReference;
	
	@JsonProperty("CliamupdateDate")
	private String cliamupdateDate;
	
	@JsonProperty("Remarks")
	private String Remarks;
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("UpdatesurveyorAdjesterPerOC")
	private String updatesurveyorAdjesterPerOC;
	
	@JsonProperty("UpdateotherProfessionalPerOc")
	private String updateotherProfessionalPerOc;
	
	@JsonProperty("UpdateibnrPerOc")
	private String updateibnrPerOc;
	
	@JsonProperty("UpdatesurveyorAdjesterOurShareOC")
	private String updatesurveyorAdjesterOurShareOC;
	
	@JsonProperty("UpdateprofessionalOurShareOc")
	private String updateprofessionalOurShareOc;
	
	@JsonProperty("UpdateibnrOurShareOc")
	private String updateibnrOurShareOc;
	
	@JsonProperty("LossEstimateOrigCurr")
	private String lossEstimateOrigCurr;
	@JsonProperty("UpdaterecordFees")
	private String updaterecordFees;
	
	@JsonProperty("UpdaterecordIbnr")
	private String updaterecordIbnr;
	
	@JsonProperty("TotalReserveOSOC")
	private String totalReserveOSOC;
	
	@JsonProperty("ReservePositionDate")
	private String reservePositionDate;
	
	@JsonProperty("ClaimclosedDate")
	private String claimclosedDate;

//	@JsonProperty("RiRecovery")
//	private String riRecovery;
	
	@JsonProperty("CloseClaimYN")
	private String closeClaimYN;
	
	@JsonProperty("UpdateRivisedpercentage")
	private String updateRivisedpercentage;
	
	@JsonProperty("DateofLoss")
	private String dateofLoss;
	
	@JsonProperty("SignedShare")
	private String signedShare;
	
	@JsonProperty("BusinessMode")
	private String businessMode;

}
