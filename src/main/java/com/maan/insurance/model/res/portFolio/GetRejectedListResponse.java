package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRejectedListResponse {
	@JsonProperty("PendingList")
	private List<GetRejectedListRes1> pendingList;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Mode")
	private String mode;
}
