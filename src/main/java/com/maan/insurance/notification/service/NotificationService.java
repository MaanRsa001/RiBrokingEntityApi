package com.maan.insurance.notification.service;

import java.util.List;

import com.maan.insurance.model.res.SuccessRes;
import com.maan.insurance.notification.mail.dto.GetMailTemplateReq;
import com.maan.insurance.notification.mail.dto.MailTriggerReq;
import com.maan.insurance.notification.mail.dto.NotifTemplateRes;
import com.maan.insurance.notification.mail.dto.ReadMailReq;
import com.maan.insurance.notification.mail.dto.ReadMailRes;
import com.maan.insurance.notification.req.ReplayMailTriggerReq;
import com.maan.insurance.notification.req.SmsGetReq;
import com.maan.insurance.notification.res.SmsGetRes;
import com.maan.insurance.notification.sms.dto.SmsReq;

public interface NotificationService {

	public SuccessRes SendMail(MailTriggerReq req);

	public NotifTemplateRes getGetMailTemplate(GetMailTemplateReq req);

	public List<ReadMailRes> ReadMail(ReadMailReq req);

	public SuccessRes SendSms(SmsReq req);

	public List<ReadMailRes> ReadSentMail(ReadMailReq req);

	public SuccessRes replayMail(ReplayMailTriggerReq req);

//	public SuccessRes SendAndSaveMail(MailTriggerReq req);

	public SuccessRes SaveAndSendSms(SmsReq req);

	public List<SmsGetRes> getSms(SmsGetReq req);


}
