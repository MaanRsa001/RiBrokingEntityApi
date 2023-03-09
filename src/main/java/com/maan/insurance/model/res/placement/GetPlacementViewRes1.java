package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetPlacementViewRes1 {
	@JsonProperty("CedentCorrespondent")
	private String cedentCorrespondent; 
	@JsonProperty("ReinsurerCorrespondent")
	private String reinsurerCorrespondent; 
	@JsonProperty("TqrCorrespondent")
	private String tqrCorrespondent; 
	
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	@JsonProperty("NewStatus")
	private String newStatus; 
	@JsonProperty("UpdateDate")
	private String updateDate;  
	@JsonProperty("EmailBy")
	private String emailBy;

}
