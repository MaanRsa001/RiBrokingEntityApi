package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractValidationReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("Year")
	private String year;
	@JsonProperty("DepartId")
	private String departId;
	@JsonProperty("OriginalCurrency")
	private String originalCurrency;
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("ContNo")
	private String contno;
	@JsonProperty("Surplus")
	private String surplus;
	@JsonProperty("CoverPer")
	private String coverPer;
	@JsonProperty("DedPer")
	private String dedPer;

	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("BranchCode")
	private String branchCode;

}
