package com.maan.insurance.validation.journal;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.journal.GetEndDateStatusReq;
import com.maan.insurance.model.req.journal.GetLedgerEntryListReq;
import com.maan.insurance.model.req.journal.GetQuaterEndValidationReq;
import com.maan.insurance.model.req.journal.GetSpcCurrencyListReq;
import com.maan.insurance.model.req.journal.GetStartDateStatusReq;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.journal.InsertManualJVReq;
import com.maan.insurance.model.req.journal.InsertRetroProcessReq;
import com.maan.insurance.model.req.journal.LedgerIdReq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.journal.JournalServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@Service
public class JournalValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(JournalValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private JournalServiceImple imple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public JournalValidation() {
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("application_field_names.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			log.info(e);
		}
	}

	public List<ErrorCheck> insertInActiveOpenPeriodVali(InsertInActiveOpenPeriodReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getJournalname())) {
			list.add(new ErrorCheck("Please Enter Journalname", "Journalname", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		return list;	
	}

	public List<ErrorCheck> getSpcCurrencyListVali(GetSpcCurrencyListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getJournalID())) {
			list.add(new ErrorCheck("Please Enter JournalID", "JournalID", "3"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		return list;	
	}

	public List<ErrorCheck> getQuaterEndValidationVali(GetQuaterEndValidationReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getSampledate())) {
			list.add(new ErrorCheck("Please Enter Sampledate", "Sampledate", "2"));
		}
		return list;	
	}

	public List<ErrorCheck> insertRetroProcessVali(InsertRetroProcessReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "3"));
		}
		return list;	
	}
	public List<ErrorCheck> getLedgerEntryListVali(GetLedgerEntryListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getOpendDate())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "2"));
		}
		if (StringUtils.isBlank(req.getOpstartDate())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "3"));
		}
		return list;	
	}

	public List<ErrorCheck> validationManual(InsertManualJVReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double totDebitOC=0;
		double totCreditOC=0;
		double totDebitDC=0;
		double totCreditDC=0;
		double exchangediff=0;
		final Validation val = new Validation();
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
		if(StringUtils.isNotBlank(bean.getTranId()) && "edit".equals(bean.getMode())){
			if(StringUtils.isBlank(bean.getAmendmentDate())){
				list.add(new ErrorCheck(prop.getProperty("error.camend.date"),"","01"));
			}else  if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getAmendmentDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.amend.date.invalid"),"","01"));
			}if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(bean.getAmendmentDate()).equalsIgnoreCase("")){
				if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getAmendmentDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans.amend")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
				}
		}
		if("edit".equals(bean.getMode()) || "new".equals(bean.getMode())){
		if(StringUtils.isBlank(bean.getTransactionDate())){
			list.add(new ErrorCheck(prop.getProperty("error.transactio.date"),"","01"));
		}else  if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getTransactionDate()))) {
			list.add(new ErrorCheck(prop.getProperty("error.transactio.date.invalid"),"","01"));
		}if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(bean.getTransactionDate()).equalsIgnoreCase("")){
			if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getTransactionDate())==0){
				list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
			}
			}
		}else{
			if(StringUtils.isBlank(bean.getReversalDate())){
				list.add(new ErrorCheck(prop.getProperty("error.reversal.date"),"ReversalDate","01"));
			}else  if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getReversalDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.reversal.date.invalid"),"ReversalDate","01"));
			}if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(bean.getReversalDate()).equalsIgnoreCase("")){
				if(dropDowmImpl.Validatethree(bean.getBranchCode(),bean.getReversalDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.reversal")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
				}
		}
		if(StringUtils.isBlank(bean.getCurrency())){
			list.add(new ErrorCheck(prop.getProperty("error.currency"),"currency","01"));
		}
		if(StringUtils.isBlank(bean.getExchRate())){
			list.add(new ErrorCheck(prop.getProperty("error.exchange.rate"),"exchange","01"));
		}
		if(StringUtils.isBlank(bean.getLedClass())){
			list.add(new ErrorCheck(prop.getProperty("error.class"),"LedClass","01"));
		}
		for(int i=0;i<bean.getLedgerIdReq().size();i++){
			LedgerIdReq req = bean.getLedgerIdReq().get(i);
			if(i==0 || StringUtils.isNotBlank(req.getLedgerId()) || StringUtils.isNotBlank(req.getDebitOC()) || StringUtils.isNotBlank(req.getCreditOC()) || StringUtils.isNotBlank(req.getDebitDC()) || StringUtils.isNotBlank(req.getCreditDC())){
			if(StringUtils.isBlank(req.getLedgerId()) ){
				list.add(new ErrorCheck(prop.getProperty("error.ledgerid")+String.valueOf(i + 1),"ledgerid","01" ));
			}
			if(StringUtils.isBlank(req.getDebitOC()) &&StringUtils.isBlank(req.getCreditOC())){
				if(StringUtils.isBlank(req.getDebitOC()) ){
					list.add(new ErrorCheck(prop.getProperty("error.debitOC")+String.valueOf(i + 1),"debitOC","01" ));
				}if(StringUtils.isBlank(req.getCreditOC()) ){
					list.add(new ErrorCheck(prop.getProperty("error.creditOC")+String.valueOf(i + 1),"creditOC","01"));
				}
			}
			if(StringUtils.isBlank(req.getDebitDC()) &&StringUtils.isBlank(req.getCreditDC())  ){
				if(StringUtils.isBlank(req.getDebitDC()) ){
					list.add(new ErrorCheck(prop.getProperty(("error.debitDC")+ bean.getShortname(), String.valueOf(i + 1)),"debitDC","01"));
				}if(StringUtils.isBlank(req.getCreditDC()) ){
					list.add(new ErrorCheck(prop.getProperty(("error.creditDC")+bean.getShortname(), String.valueOf(i + 1)),"creditDC","01"));
				}
			} 
			if(StringUtils.isNotBlank(req.getCreditDC()) ){
				String ans = calcu.calculateManualJournal(bean,"CreditDC",i);
				if(Double.parseDouble(ans)!=Double.parseDouble(req.getCreditDC().replace(",", ""))){
				list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"CreditDC","01"));
				
				}else{
				req.setCreditDC(ans);
					//req.getCreditDC().set(i,ans);
				}
			}
			if(StringUtils.isNotBlank(req.getDebitDC()) ){
				String ans = calcu.calculateManualJournal(bean,"DebitDC",i);
				if(Double.parseDouble(ans)!=Double.parseDouble(req.getDebitDC().replace(",", ""))){
					list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"DebitDC","01"));
					
				}else{
					req.setDebitDC(ans);
				//	req.getDebitDC().set(i,ans);
				}
			}
			totDebitOC +=Double.parseDouble(StringUtils.isBlank(req.getDebitOC())?"0":req.getDebitOC().replaceAll(",", ""));
			totCreditOC +=Double.parseDouble(StringUtils.isBlank(req.getCreditOC())?"0":req.getCreditOC().replaceAll(",", ""));
			totDebitDC +=Double.parseDouble(StringUtils.isBlank(req.getDebitDC())?"0":req.getDebitDC().replaceAll(",", ""));
			totCreditDC +=Double.parseDouble(StringUtils.isBlank(req.getCreditDC())?"0":req.getCreditDC().replaceAll(",", ""));
		}
		}
		exchangediff+=totDebitDC-totCreditDC;
		if(exchangediff<0){
			totDebitDC=totDebitDC+Math.abs(exchangediff);
		}else if (exchangediff>0){
			totCreditDC=totCreditDC+Math.abs(exchangediff);
		}
		if(totDebitOC!=totCreditOC){
			list.add(new ErrorCheck(prop.getProperty("error.totaloc"),"totaloc","01"));
		}
		if(totDebitDC!=totCreditDC){
			list.add(new ErrorCheck(prop.getProperty("error.totaldc")+bean.getShortname(),"totaldc","01"));
		}
		if(StringUtils.isBlank(bean.getNarration())){
			list.add(new ErrorCheck(prop.getProperty("error.narration"),"narration","01"));
		}
		return list;	
	}

	public List<ErrorCheck> getStartDateStatusVali(GetStartDateStatusReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "2"));
		}
		return list;	
	}

	public List<ErrorCheck> getEndDateStatusVali(GetEndDateStatusReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		return list;	
	}

}
