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

public class SmsConfigMasterRes implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("InsId")
	private String insId;

	@JsonProperty("SmsPartyUrl")
	private String smsPartyUrl;

	@JsonProperty("SmsUserName")
	private String smsUserName;

	@JsonProperty("SmsUserPass")
	private String smsUserPass;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("SecureYn")
	private String secureYn;

	@JsonProperty("Senderid")
	private String senderid;

	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("Remarks")
	private String remarks;

}
