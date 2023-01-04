package com.maan.insurance.model.req.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumedListreq {
@JsonProperty("ContNo")
private String contNo;
@JsonProperty("BranchCode")
private String branchCode;
@JsonProperty("OpstartDate")
private String opstartDate;
@JsonProperty("OpendDate")
private String opendDate;



}
