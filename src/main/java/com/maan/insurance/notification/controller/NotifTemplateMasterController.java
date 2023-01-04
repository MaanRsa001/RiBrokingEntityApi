package com.maan.insurance.notification.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.insurance.notification.mail.dto.GetMailTemplateReq;
import com.maan.insurance.notification.mail.dto.NotifTemplateRes;
import com.maan.insurance.notification.res.CommonCrmRes;
import com.maan.insurance.notification.service.NotificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "NOTIFIACTION TEMPLATES : Notifiaction Templates", description = "API's")
@RequestMapping("/notification")
public class NotifTemplateMasterController {
	


	@Autowired
	private NotificationService mailservice;
		
	@PostMapping("/getGetMailTemplate")
	@ApiOperation(value = "This method is to Get Email Templates")
	public ResponseEntity<CommonCrmRes> getGetMailTemplate(@RequestBody GetMailTemplateReq req) {

		CommonCrmRes data = new CommonCrmRes();

		// Save
		NotifTemplateRes res = mailservice.getGetMailTemplate(req);
		
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(Collections.emptyList());
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
}
