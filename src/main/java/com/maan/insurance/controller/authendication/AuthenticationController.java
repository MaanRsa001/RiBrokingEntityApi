package com.maan.insurance.controller.authendication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.authentication.AuthenticationChangesReq;
import com.maan.insurance.model.req.authentication.AuthenticationListReq;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.authentication.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.res.authentication.AuthenticationListRes;
import com.maan.insurance.model.res.journal.GetjounalListRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.authentication.AuthenticationService;
import com.maan.insurance.service.journal.JournalService;
import com.maan.insurance.validation.authentication.AuthenticationValidation;
import com.maan.insurance.validation.journal.JournalValidation;

@RestController
@RequestMapping("/Insurance/authentication")
public class AuthenticationController {
Gson gson = new Gson(); 
	
	@Autowired
	private AuthenticationService serv;
	@Autowired
	private AuthenticationValidation val;
	
	@PostMapping("/authenticationList")
	public AuthenticationListRes insertInActiveOpenPeriod(@RequestBody AuthenticationListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.authenticationListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.authenticationList(req);
	}  
	
	@PostMapping("/authenticationChanges")
	public CommonResponse authenticationChanges(@RequestBody AuthenticationChangesReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validationApproval(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.authenticationChanges(req);
	}  
	@PostMapping("/getPremiumDetails")
	public GetPremiumDetailsRes getPremiumDetails(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPremiumDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPremiumDetails(req);
	}  
}
