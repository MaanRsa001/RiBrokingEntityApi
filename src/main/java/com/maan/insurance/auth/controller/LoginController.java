package com.maan.insurance.auth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.insurance.auth.dto.CommonLoginResponse;
import com.maan.insurance.auth.dto.LoginRequest;
import com.maan.insurance.auth.service.AuthendicationService;
import com.maan.insurance.auth.service.LoginValidatedService;
import com.maan.insurance.error.Error;

@RestController
@RequestMapping("/authentication")
public class LoginController {

	private Logger log = LogManager.getLogger(LoginController.class);
	
	@Autowired
	private AuthendicationService authservice;
	@Autowired
	private LoginValidatedService loginValidationComponent;


	@PostMapping("/login")
	public CommonLoginResponse getloginToken(@RequestBody LoginRequest mslogin, HttpServletRequest http)  {
		CommonLoginResponse res = new CommonLoginResponse();
		List<Error> validation =loginValidationComponent.loginInputValidation(mslogin); 
		if(!CollectionUtils.isEmpty(validation)) {
			res.setErrors(validation);
			return res;

		}
		return authservice.checkUserLogin(mslogin,http);
	}
    	
    
    
   
    
}
