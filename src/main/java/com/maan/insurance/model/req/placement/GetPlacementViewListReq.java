package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;

import lombok.Data;

@Data
public class GetPlacementViewListReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
	@JsonProperty("BrokerId")
	private String brokerId;
}
