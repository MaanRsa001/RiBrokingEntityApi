package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditBillingInfoRes1 {
	
	
	@JsonProperty("CurrencyId")
	private String currencyId; 
	@JsonProperty("Remarks")
	private String remarks;
	//info
	@JsonProperty("BusinessType")
	private String productId;
	@JsonProperty("TransType")
	private String transType;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("CedingId")
	private String cedingId; 
	@JsonProperty("AmendmentDate")
	private String amendmentDate;
	@JsonProperty("BillDate")
	private String billDate; 
	@JsonProperty("RoundingAmount")
	private String roundingAmount;
	@JsonProperty("UtilizedTillDate")
	private String utilizedTillDate; 
}
