package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.nonproportionality.RetroListRes;

import lombok.Data;

@Data
public class GetRetroContractDetailsReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("BranchCode")
	private String branchCode;
}
