package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAutoPendingListResponse {
	@JsonProperty("AutoPendingList")
	private List<GetAutoPendingListRes1> autoPendingList;
	@JsonProperty("Mode")
	private String mode;
}
