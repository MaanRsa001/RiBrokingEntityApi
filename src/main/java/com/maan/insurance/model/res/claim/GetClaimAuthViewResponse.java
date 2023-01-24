package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.GetClaimAuthViewReq;

import lombok.Data;

@Data
public class GetClaimAuthViewResponse {
	@JsonProperty("LossEstimateOrigCurr")
	private String lossEstimateOrigCurr;
	@JsonProperty("LossEstimateOurShareOrigCurr")
	private String lossEstimateOurShareOrigCurr;
	
	@JsonProperty("MultiPaymentNoList")
	private List<MultiPaymentNoListRes> multiPaymentNoList;
	@JsonProperty("GetClaimAuthView")
	private List<GetClaimAuthViewRes1>  getClaimAuthView;

}
