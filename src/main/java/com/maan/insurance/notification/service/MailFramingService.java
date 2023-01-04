package com.maan.insurance.notification.service;

import com.maan.insurance.notification.mail.dto.MailFramingReq;

public interface MailFramingService {

	String frameMail(MailFramingReq mReq, String mailBody, String mailSubject);


}
