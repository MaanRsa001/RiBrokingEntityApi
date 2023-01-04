/*
 *  Copyright (c) 2019. All right reserved
 * Created on 2021-11-19 ( Date ISO 2021-11-19 - Time 13:16:53 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2021-11-19 ( 13:16:53 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */

package com.maan.insurance.notification.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Domain class for entity "SmsConfigMaster"
 *
 * @author Telosys Tools Generator
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SmsGetRes {

@JsonProperty("Sno")
private Integer sNo;

@JsonProperty("SmsRefNo")
private String smsRefNo;

@JsonProperty("CustName")
private String custName;

@JsonProperty("MobileNo")
private String mobileNo;


@JsonProperty("PolicyNo")
private String policyNo;


@JsonProperty("SmsType")
private String smsType;


@JsonProperty("SmsContent")
private String smsContent;

@JsonFormat(pattern="dd/MM/yyyy")
@JsonProperty("ReqTime")
private Date reqTime;

@JsonFormat(pattern="dd/MM/yyyy")
@JsonProperty("ResTime")
private Date resTime;

@JsonProperty("ResStatus")
private String resStatus;

@JsonProperty("ResSuccess")
private String resSuccess;


@JsonProperty("ResCode")
private String resCode;


@JsonProperty("ResMessage")
private String resMessage;


@JsonProperty("ResData")
private String resData;

@JsonProperty("Remarks")
private String remarks;

@JsonProperty("ProductId")
private String productId;

@JsonProperty("InsuranceId")
private String insId;


@JsonProperty("LoginId")
private String createdBy;

}
