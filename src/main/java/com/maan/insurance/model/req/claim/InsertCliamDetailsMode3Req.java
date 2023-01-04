package com.maan.insurance.model.req.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class InsertCliamDetailsMode3Req {
	@JsonProperty("PaymentFlag")
	private String paymentFlag;
	
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("Date")
	private String date;
	
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("PaidAmountOrigcurr")
	private String paidAmountOrigcurr;
	
	@JsonProperty("ExcRate")
	private String excRate;
	
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	
	@JsonProperty("ClaimNoteRecommendations")
	private String claimNoteRecommendations;
	
	@JsonProperty("PaymentReference")
	private String paymentReference;
	
	@JsonProperty("AdviceTreasury")
	private String adviceTreasury;
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("AdviceTreasuryEmail")
	private String adviceTreasuryEmail;
	
	@JsonProperty("ReinstType")
	private String reinstType;
	@JsonProperty("ReinstPremiumOCOS")
	private String reinstPremiumOCOS;
	
	@JsonProperty("PaidClaimOs")
	private String paidClaimOs;

	
	@JsonProperty("Surveyorfeeos")
	private String surveyorfeeos;
	
	@JsonProperty("Otherproffeeos")
	private String otherproffeeos;

	@JsonProperty("RiRecovery")
	private String riRecovery;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("PaymentType")
	private String paymentType;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("Currecny")
	private String currecny;
	
	@JsonProperty("BusinessMode")
	private String businessMode;

}
