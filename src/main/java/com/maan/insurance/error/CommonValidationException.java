package com.maan.insurance.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CommonValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Message")
	private String messageResult;
	@JsonProperty("IsError")
	private Boolean isError=Boolean.TRUE;
	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;
	
	public CommonValidationException(ErrorCheck error ) {		
			setErrors(new ArrayList<ErrorCheck>());
			this.getErrors().add(error);
			
	}
	
	public CommonValidationException(String messageResult,List<ErrorCheck> error) {
		this.messageResult=messageResult;	
		this.setErrors(error);			
	}

	public CommonValidationException(List<ErrorCheck> error) {
		this.setErrors(error);		
	}

	
	
}
