package com.maan.insurance.model.res.authentication;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;

import lombok.Data;

@Data
public class AuthenticationListRes1 {
	@JsonProperty("Transaction")
	private String transaction;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LayerNo")
	private String layerNo;
	
}
