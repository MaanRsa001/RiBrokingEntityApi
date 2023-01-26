package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetTransContractReqRi {

	
	@JsonProperty("CurrencyId")
	private String currencyId;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("CedingId")
	private String cedingId;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
	private String productId;
}
