package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes1;

import lombok.Data;

@Data
public class GetRetroContractDetailsListReq {
	@JsonProperty("Productid")
	private String productid;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("UwYear")
	private String  uwYear;
	
}
