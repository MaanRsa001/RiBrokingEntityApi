package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPendingListResponse {
	@JsonProperty("PendingList")
	private List<GetPendingListRes1> pendingList;
	@JsonProperty("Type")
	private String type;
}
