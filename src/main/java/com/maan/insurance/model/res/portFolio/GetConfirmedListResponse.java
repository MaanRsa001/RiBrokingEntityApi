package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.portFolio.GetConfirmedListReq;

import lombok.Data;
@Data
public class GetConfirmedListResponse {
	@JsonProperty("ConfirmedList")
	private List<GetConfirmedListRes1> confirmedList;
	@JsonProperty("Type")
	private String type;
}
