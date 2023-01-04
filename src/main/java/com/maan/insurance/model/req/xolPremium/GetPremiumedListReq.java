package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetRetroContractsRes1;

import lombok.Data;

@Data
public class GetPremiumedListReq {
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("OpstartDate")
	private String opstartDate;
	@JsonProperty("OpendDate")
	private String opendDate;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("Type")
	private String type; 
	@JsonProperty("ProductId")
	private String productId;
	
//	@JsonProperty("DepartmentId")
//	private String departmentId;
	

	

}
