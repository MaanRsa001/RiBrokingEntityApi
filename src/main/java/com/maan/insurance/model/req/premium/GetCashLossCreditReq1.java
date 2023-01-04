package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.GetShortnameRes;

import lombok.Data;

@Data
public class GetCashLossCreditReq1 {
	@JsonProperty("MainclaimPaymentNos")
	private String mainclaimPaymentNos;
	@JsonProperty("MaincreditAmountCLClist")
	private String maincreditAmountCLClist;
	@JsonProperty("MaincreditAmountCLDlist")
	private String maincreditAmountCLDlist;
	@JsonProperty("MainCLCsettlementRatelist")
	private String mainCLCsettlementRatelist;
	@JsonProperty("CreditAmountCLDlist")
	private String creditAmountCLDlist;
	@JsonProperty("CreditAmountCLClist")
	private String creditAmountCLClist;
	@JsonProperty("CLCsettlementRatelist")
	private String cLCsettlementRatelist;
	@JsonProperty("Chkbox")
	private String chkbox;
}
