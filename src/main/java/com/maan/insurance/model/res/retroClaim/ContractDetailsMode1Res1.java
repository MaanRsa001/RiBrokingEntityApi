package com.maan.insurance.model.res.retroClaim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;

import lombok.Data;

@Data
public class ContractDetailsMode1Res1 {
	@JsonProperty("SumOfPaidAmountOC")
	private String sumOfPaidAmountOC;

	@JsonProperty("RevSumOfPaidAmt")
	private String revSumOfPaidAmt;
	
	@JsonProperty("ContractDetails")
	private List<ContractDetailsMode1Res2> contractDetails;
}
