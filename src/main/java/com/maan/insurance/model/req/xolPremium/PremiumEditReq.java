package com.maan.insurance.model.req.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes1;

import lombok.Data;
@Data
public class PremiumEditReq {
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("TableType")
	private String tableType;
	@JsonProperty("RequestNo")
	private String requestNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;

}
