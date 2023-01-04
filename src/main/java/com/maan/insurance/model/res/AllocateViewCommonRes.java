package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocateViewCommonRes {
	@JsonProperty("Alllist")
	private List<AllocateViewRes> alllist;
	@JsonProperty("AllocatedList")
	private List<AllocateViewRes> allocatedList;
	@JsonProperty("RevertedList")
	private List<AllocateViewRes> revertedList;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("AllTillDate")
	private String allTillDate;
}
