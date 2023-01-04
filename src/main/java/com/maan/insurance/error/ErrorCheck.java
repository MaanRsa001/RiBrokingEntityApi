package com.maan.insurance.error;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCheck {  
	
	@JsonProperty("Code")
    private String code;
	@JsonProperty("TimeStamp")
	 private Date timestamp=new Date();
	 @JsonProperty("ErrorDescription")
	 private String errorDescription;
	 @JsonProperty("Field")
	 private String field;
	 @JsonProperty("ErrorCode")
	 private String errorCode;
	 
	 public ErrorCheck(String errorDescription, String field, String errorCode) {
		super();
		this.errorDescription = errorDescription;
		this.field = field;
		this.errorCode = errorCode;
	}
}