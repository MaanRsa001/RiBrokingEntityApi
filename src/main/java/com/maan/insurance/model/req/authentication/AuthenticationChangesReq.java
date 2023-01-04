package com.maan.insurance.model.req.authentication;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq1;

import lombok.Data;
@Data
public class AuthenticationChangesReq {
	@JsonProperty("UploadStatus")
	private String uploadStatus;
	@JsonProperty("CheckItem")
	private String checkItem;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("ApproveStatus")
	private String approveStatus;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo; 
	@JsonProperty("InstlmentNo")
	private String instlmentNo;
	
	//getCassLossCredit
	@JsonProperty("GetCashLossCreditReq1")
	private List<GetCashLossCreditReq1> GetCashLossCreditReq1;
	@JsonProperty("ClaimPayNo")
	private String claimPayNo;
	@JsonProperty("CurrencyId")
	private String currencyId; 
	@JsonProperty("Mode")
	private String mode;
	
	@JsonProperty("ExchRate")
	private String exchRate;
	@JsonProperty("AmendmentDate")
	private String AmendmentDate;
	@JsonProperty("RiCession")
	private String riCession;
	
}
