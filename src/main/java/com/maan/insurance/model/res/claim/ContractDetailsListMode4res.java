package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode4res {
//	
//	@JsonProperty("PolicyContractNo")
//	private String policyContractNo;
//	
//	@JsonProperty("SumOfPaidAmountOC")
//	private String sumOfPaidAmountOC;
//	@JsonProperty("RevSumOfPaidAmt")
//	private String revSumOfPaidAmt;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("Statusofclaim")
	private String statusofclaim;
	
	@JsonProperty("DepartmentClass")
	private String departmentClass;
	
	@JsonProperty("DateofLoss")
	private String dateofLoss;
	
	@JsonProperty("ReportDate")
	private String reportDate;
	
	@JsonProperty("LossDetails")
	private String lossDetails;
	
	@JsonProperty("CauseofLoss")
	private String causeofLoss;
	
	@JsonProperty("Location")
	private String location;
	
	@JsonProperty("LossEstimateOrigCurr")
	private String lossEstimateOrigCurr;
	
	@JsonProperty("LossEstimateOurshareOrigCurr")
	private String lossEstimateOurshareOrigCurr;
	
	@JsonProperty("ExcRate")
	private String excRate;
	
//	@JsonProperty("LossEstimateOurShareUSD")
//	private String lossEstimateOurShareUSD;
//	@JsonProperty("AdviceUW")
//	private String adviceUW;
//	@JsonProperty("AdviceMangement")
//	private String adviceMangement;
	@JsonProperty("RiRecovery")
	private String riRecovery;
//	@JsonProperty("RiRecoveryAmountUSD")
//	private String riRecoveryAmountUSD;
//	@JsonProperty("Recoveryfrom")
//	private String recoveryfrom;
	@JsonProperty("Createdby")
	private String createdby;
	
	@JsonProperty("CreatedDate")
	private String createdDate;
//	@JsonProperty("Modifiedby")
//	private String modifiedby;
//	@JsonProperty("ModifiedDate")
//	private String modifiedDate;
//	@JsonProperty("Updatedby")
//	private String updatedby;
//	@JsonProperty("UpdatedDate")
//	private String updatedDate;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("CurrencyName")
	private String currencyName;
//	@JsonProperty("Adviceuwemail")
//	private String adviceuwemail;
//	@JsonProperty("Advicemanagementemail")
//	private String advicemanagementemail;
	@JsonProperty("RiskCode")
	private String riskCode;
	
	@JsonProperty("AccumulationCode")
	private String accumulationCode;
	
	@JsonProperty("EventCode")
	private String eventCode;
//	@JsonProperty("LayerNo")
//	private String layerNo;
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("RecordFees")
	private String recordFees;
	
	@JsonProperty("SurveyorAdjesterPerOC")
	private String surveyorAdjesterPerOC;
	
	@JsonProperty("SurveyorAdjesterOurShareOC")
	private String surveyorAdjesterOurShareOC;
	
	@JsonProperty("OtherProfessionalPerOc")
	private String otherProfessionalPerOc;
	
	@JsonProperty("ProfessionalOurShareOc")
	private String professionalOurShareOc;
	
	@JsonProperty("IbnrPerOc")
	private String ibnrPerOc;
	
	@JsonProperty("IbnrOurShareOc")
	private String ibnrOurShareOc;
	@JsonProperty("GrossLossFGU")
	private String grossLossFGU;
	@JsonProperty("RecordIbnr")
	private String recordIbnr;
	@JsonProperty("CedentClaimNo")
	private String cedentClaimNo;
	
	@JsonProperty("ReservePositionDate")
	private String reservePositionDate;
	@JsonProperty("ClaimdepartId")
	private String claimdepartId;
	@JsonProperty("SubProfitId")
	private String subProfitId;
	
	@JsonProperty("ReopenDate")
	private String reopenDate;
	
	@JsonProperty("ReputedDate")
	private String reputedDate;
	
	@JsonProperty("Remarks")
	private String remarks;
	
}
