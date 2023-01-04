package com.maan.insurance.model.req.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetPreListRes;
import com.maan.insurance.model.res.premium.GetPreListRes1;

import lombok.Data;

@Data
public class GetContractLayerNoReq {
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ContractNo")
	private String contractNo;
}
