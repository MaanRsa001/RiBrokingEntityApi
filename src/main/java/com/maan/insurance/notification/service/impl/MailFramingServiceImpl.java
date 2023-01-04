package com.maan.insurance.notification.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.notification.controller.NotificationController;
import com.maan.insurance.notification.mail.dto.MailFramingReq;
import com.maan.insurance.notification.mail.dto.MailTriggerReq;
import com.maan.insurance.notification.service.MailFramingService;

@Service
public class MailFramingServiceImpl implements MailFramingService {
	


	@Autowired
	private NotificationController mailTrigger;
	

	
	private Logger log = LogManager.getLogger(MailThreadServiceImpl.class);
	
	@Override
	public String frameMail(MailFramingReq mReq , String mailBody, String mailSubject) {
		String res = "" ;
		try {
			List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();//oracle.getListFromQuery(queryKey, mReq.getKeys());	
			
			
			if(contents !=null && contents.size()>0 ) {
				
				Map<String, Object> content = contents.get(0);
				mailSubject = frameContentMail(mReq ,mailSubject , content) ; 	
				mailBody = frameContentMail(mReq ,mailBody , content) ; 		
				
				MailTriggerReq mTReq = new MailTriggerReq(); 
				mTReq.setInsuranceId(mReq.getInsId());
				mTReq.setMailBody(mailBody);
				mTReq.setMailSubject(mailSubject);
				mTReq.setMailCc(mReq.getMailCc() );
				mTReq.setMailTo(mReq.getMailTo());
				mTReq.setMailRegards(mReq.getMailRegards());
				
				// Trigger Mail
				mailTrigger.sendMails(mTReq);
				log.info("{ Mail Sent Successfully }");
				res = "Mail Sent Successfully";
			}
		return res;
		}catch(Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		
	}
	
	
	
	
	private String frameContentMail(MailFramingReq mReq  ,String body ,Map<String, Object> content) {
		try {
			
			//String body=;
			if(content!=null && !content.isEmpty()){
				for (Entry<String, Object> entry : content.entrySet()) {
					body=body.replaceAll("\\{"+entry.getKey()+"\\}", entry.getValue()==null?"":entry.getValue().toString()); //entry.getKey()
				}
			}
			
			return body;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private String frameMailTemplate(MailFramingReq mReq ,String mailContent , String subject, String insuranceId) {
	
	try {
		//InsuranceCompanyMaster insData =  insRepo.findByInsId(mReq.getInsId() );
		
		String companyLogo = ""; //insData.getCompanyLogo();
		String companyAddress = ""; //insData.getInsAddress();
		String companyRegards = "";//insData.getRegards();
		
	 //	<img src="https://alliance.co.tz/wp-content/themes/alliance/img/logo.png" width="150">
		StringBuilder html = new StringBuilder();			
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/email.html")));
		 
		String val;
		while ((val = br.readLine()) != null) {
			html.append(val);
		}
		String result = html.toString();
		result = result.replace("{MailLogo}", companyLogo);
		result = result.replace("{MailSub}", subject);
		result = result.replace("{MailBody}", mailContent);
		result = result.replace("{MailFooter}", companyRegards);
		result = result.replace("{MailAddress}", companyAddress);
		
		br.close();
		return result;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

}
