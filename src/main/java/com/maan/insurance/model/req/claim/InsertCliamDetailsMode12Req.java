package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCliamDetailsMode12Req {
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("Date")
	private String date;
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("ExcRate")
	private String excRate;
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
	@JsonProperty("PaymentType")
	private String paymentType;
}
