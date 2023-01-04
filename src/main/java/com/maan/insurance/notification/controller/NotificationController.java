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

import com.maan.insurance.model.res.SuccessRes;
import com.maan.insurance.notification.mail.dto.MailTriggerReq;
import com.maan.insurance.notification.mail.dto.ReadMailReq;
import com.maan.insurance.notification.mail.dto.ReadMailRes;
import com.maan.insurance.notification.req.ReplayMailTriggerReq;
import com.maan.insurance.notification.req.SmsGetReq;
import com.maan.insurance.notification.res.CommonCrmRes;
import com.maan.insurance.notification.res.SmsGetRes;
import com.maan.insurance.notification.service.NotificationService;
import com.maan.insurance.notification.sms.dto.SmsReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "NOTIFIACTION : Notifiaction ", description = "API's")
@RequestMapping("/post/notification")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	


	@PostMapping("/readReceivedMail")
	@ApiOperation(value = "This method is to Read Email")
	public ResponseEntity<CommonCrmRes> readMails(@RequestBody ReadMailReq req) {


		
		CommonCrmRes data = new CommonCrmRes();
		
		List<ReadMailRes> res = notificationService.ReadMail(req);

		if (res.size()!=0) {
			
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
		} else {
			
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("No Recieved Mail for last 10 Days");
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
		}
		
	}

	@PostMapping("/sendMail")
	@ApiOperation(value = "This method is to Send Email")
	public ResponseEntity<CommonCrmRes> sendMails(@RequestBody MailTriggerReq req) {
		
		
		CommonCrmRes data = new CommonCrmRes();
		
		SuccessRes res = notificationService.SendMail(req);

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
	
	
	@PostMapping("/readSendMail")
	@ApiOperation(value = "This method is to Read Send Email")
	public ResponseEntity<CommonCrmRes> readSentMail(@RequestBody ReadMailReq req) {


		
		CommonCrmRes data = new CommonCrmRes();
		
		List<ReadMailRes> res = notificationService.ReadSentMail(req);

		if (res.size()!=0) {
			
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
		} else {
			
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("No Recieved Mail for last 10 Days");
			return new ResponseEntity<CommonCrmRes>(data, HttpStatus.CREATED);
		}
		
	}
	
	
	@PostMapping("/sendSms")
	@ApiOperation(value = "This method is to Send Sms")
	public ResponseEntity<CommonCrmRes> sendSms(@RequestBody SmsReq req) {
		
		
		CommonCrmRes data = new CommonCrmRes();
		
		SuccessRes res = notificationService.SaveAndSendSms(req);

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

	@PostMapping("/replayMail")
	@ApiOperation(value = "This method is to Send Replay Mail")
	public ResponseEntity<CommonCrmRes> replayMail(@RequestBody ReplayMailTriggerReq req) {
		
		
		CommonCrmRes data = new CommonCrmRes();
		
		SuccessRes res = notificationService.replayMail(req);

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


	@PostMapping("/getSms")
	@ApiOperation(value = "This method is to Get Sms")
	public ResponseEntity<CommonCrmRes> getSms(@RequestBody SmsGetReq req) {
		
		
		CommonCrmRes data = new CommonCrmRes();
		
		List<SmsGetRes> res = notificationService.getSms(req);

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
