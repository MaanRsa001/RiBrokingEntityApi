package com.maan.insurance.model.res.premium;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetSPRetroListRes1 {
	
	@JsonProperty("SpRetro")
	private String spRetro;
	
	@JsonProperty("NoOfInsurers")
	private String noOfInsurers;
	
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
