package com.maan.insurance.validation.adjustment;

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
import com.maan.insurance.model.req.adjustment.AdjustmentViewReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentDoneListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentPayRecListReq;
import com.maan.insurance.model.req.adjustment.InsertReverseReq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.adjustment.AdjustmentServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

@Service
public class AdjustmentValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(AdjustmentValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private AdjustmentServiceImple adjImple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public AdjustmentValidation() {
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

	public List<ErrorCheck> getAdjustmentDoneListVali(GetAdjustmentDoneListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getSearchBy())) {
		list.add(new ErrorCheck("Please Enter SearchBy", "SearchBy", "1"));
	}
	if (StringUtils.isBlank(req.getSearchValue())) {
		list.add(new ErrorCheck("Please Enter SearchValue", "SearchValue", "2"));
	}
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
	}
	return list;
	}

	public List<ErrorCheck> adjustmentViewVali(AdjustmentViewReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "1"));
		}
		if (StringUtils.isBlank(req.getSerialNo())) {
			list.add(new ErrorCheck("Please Enter SerialNo", "SerialNo", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "4"));
		}
		
		return list;
		}



	public List<ErrorCheck> validateAdjustList(GetAdjustmentListReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
		if(bean.getTest().equals("ALL")){
		if(bean.getBrokerId().equals("-1")){
			list.add(new ErrorCheck(prop.getProperty("error.blank.brokerId"),"brokerId","01"));
		}else if(bean.getBrokerId().equals("63")){
			if(bean.getCedingId().equals("-1")){
				list.add(new ErrorCheck(prop.getProperty("error.blank.cedingId"),"cedingId","01"));
			}
		}
		if(bean.getCurrencyId().equals("-1")){
			list.add(new ErrorCheck(prop.getProperty("error.blank.currencyId"),"currencyId","01"));
		}
		if(!bean.getAmountType().equals("-1")){
		if(StringUtils.isBlank(bean.getAmount())){
			list.add(new ErrorCheck(prop.getProperty("error.blank.amount"),"amount","01"));
		}
		}
		}else{
			if(StringUtils.isBlank(bean.getTransactionNo())){
				list.add(new ErrorCheck(prop.getProperty("error.blank.transactionNo"),"transactionNo","01"));
			}
		}
		if(bean.getTransactionType().equals("-1")){
			list.add(new ErrorCheck(prop.getProperty("error.blank.transactionNo"),"transactionNo","01"));
		}
		if(bean.getAdjustType().equals("-1")){
			list.add(new ErrorCheck(prop.getProperty("error.blank.adjustType"),"adjustType","01"));
		}
		} catch(Exception e) {
			e.printStackTrace();
			}
		return list;
	}

	public List<ErrorCheck> getAdjustmentPayRecListVali(GetAdjustmentPayRecListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "1"));
		}
		if (StringUtils.isBlank(req.getAdjustType())) {
			list.add(new ErrorCheck("Please Enter AdjustType", "AdjustType", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getAmount())) {
			list.add(new ErrorCheck("Please Enter Amount", "Amount", "4"));
		}
		if (StringUtils.isBlank(req.getAmountType())) {
			list.add(new ErrorCheck("Please Enter AmountType", "AmountType", "5"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getCurrencyId())) {
			list.add(new ErrorCheck("Please Enter CurrencyId", "CurrencyId", "8"));
		}
		if (StringUtils.isBlank(req.getTest())) {
			list.add(new ErrorCheck("Please Enter Test", "Test", "9"));
		}
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "10"));
		}
		
		return list;
		
	}

	public List<ErrorCheck> validateReverseInsert(InsertReverseReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val = new Validation();
		try {
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
		adjImple.allocatedDate(bean);
		
	if(StringUtils.isBlank(bean.getReverseDate())) {
		list.add(new ErrorCheck(prop.getProperty("errors.payment.reverseDateReq"),"reverseDateReq","01"));

	}
	else if(val.checkDate(bean.getReverseDate()).equalsIgnoreCase("INVALID")) {
		list.add(new ErrorCheck(prop.getProperty("errors.payment.reverseDateInv"),"reverseDateInv","01"));
	}
	
	else if(val.ValidateTwoDates(bean.getAllocateddate(),bean.getReverseDate()).equalsIgnoreCase("INVALID")) {
		list.add(new ErrorCheck(prop.getProperty("errors.payment.revDateGretAlloDate"),"revDateGretAlloDate","01"));
	}
	if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getReverseDate()).equalsIgnoreCase("") ){
		if(dropDowmImpl.Validatethree(bean.getBranchCode(),bean.getReverseDate())==0){
			list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.reversal")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
		}
	}
	} catch(Exception e) {
		e.printStackTrace();
		}
		return list;
	}

	public List<ErrorCheck> validteAdjustment(GetAdjustmentListReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
//		boolean check=true;
//		final Validation val=new Validation();
//		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
//		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
//		Double a=0.0,b=0.0,c=0.0;
//		List<GetAdjustmentListRes1> payList=new ArrayList<GetAdjustmentListRes1>();
//		if(StringUtils.isBlank(bean.getAdjustmentDate())){
//			list.add(new ErrorCheck(prop.getProperty("error.blank.adjustmentDate"),"","01"));
//		}
//		else if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getAdjustmentDate()).equalsIgnoreCase("")){
//			if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getAdjustmentDate())==0){
//				list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.adjustmentDate")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
//			}
//			}
//		String maxDate = adjImple.GetMaxDate(bean);
//		if(null!=maxDate){
//			if(val.ValidateTwoDates(maxDate, bean.getAdjustmentDate()).equalsIgnoreCase("Invalid")){
//				list.add(new ErrorCheck(prop.getProperty("errors.adjustmentDate.greater.maxDate")+maxDate,"adjustmentDate","01"));
//			}
//		}
//		List<String> transactionNos = bean.getTransactionNo();
//		List<String> adjustmentAmounts = bean.getAdjustmentAmounts();
//		List<String> chkbox = bean.getChkbox();
//		
//		Map<String,String> receiveAdjustAmountMap1 = new HashMap<String, String>();
//		if(bean.getTransactionNoListReq()!=null && bean.getTransactionNoListReq().size()>0) {
//			for(int i=0 ; i<bean.getTransactionNoListReq().size() ; i++) {
//				TransactionNoListReq	req = bean.getTransactionNoListReq().get(i);
//				if("true".equalsIgnoreCase(req.getCheck())) {
//					List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream()
//							.filter(o -> bean1.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
//							.collect(Collectors.toList());
//					if (!CollectionUtils.isEmpty(filterTrack)) {
//						filterTrack.get(i).setTransactionNo(req.getTransactionNo());
//						filterTrack.get(i).getAdjustAmount(req.getTransactionNo());
//						receiveAdjustAmountMap1.remove(transactionNos.get(i));
//						receiveAdjustAmountMap1.put(transactionNos.get(i), adjustmentAmounts.get(i).replace(",", ""));
//					}
//					else {
//						receiveAdjustAmountMap1.put(transactionNos.get(i), adjustmentAmounts.get(i).replace(",", ""));						
//					}
//				}
//				else if(receiveAdjustAmountMap1!=null && receiveAdjustAmountMap1.containsKey(transactionNos.get(i))) {
//					receiveAdjustAmountMap1.remove(transactionNos.get(i));
//				}
//			}
//		}
//		if(bean.getTransactionType().equals("PC")){
//			GetAdjustmentListRes res = adjImple.getAdjustmentList(bean);
//			payList = res.getCommonResponse().getAdjustmentList();
//		}else{
//			GetAdjustmentListRes res = adjImple.getAdjustmentList(bean);
//			payList = res.getCommonResponse().getAdjustmentList();
//		}
//		for(GetAdjustmentListRes1 Obj : payList)
//		{	
//			GetAdjustmentListRes1 form = Obj;
//			List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream()
//					.filter(o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
//					.collect(Collectors.toList());
//			if (!CollectionUtils.isEmpty(filterTrack)) {
//				check=false;
//					if(receiveAdjustAmountMap1.get(form.getTransactionNo())==null || "".equalsIgnoreCase(receiveAdjustAmountMap1.get(form.getTransactionNo())))
//					 {			 
//						 list.add(new ErrorCheck(prop.getProperty("error.adjustAmountReq")+form.getTransactionNo(),"","01"));
//					 }else if(val.numbervalid((receiveAdjustAmountMap1.get(form.getTransactionNo()))).equalsIgnoreCase("INVALID"))
//					{
//						 list.add(new ErrorCheck(prop.getProperty("error.adjustAmountInv")+form.getTransactionNo(),"","01"));
//					}else
//					{
//						String sign1;
//						String sign2;
//						if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))<0)
//							sign1="-";
//						else
//							sign1="+";
//						if(Double.parseDouble(form.getPendingAmount().replace(",", ""))<0)
//							sign2="-";
//						else
//							sign2="+";
//						 if(!sign1.equals(sign2))
//						 {
//							 list.add(new ErrorCheck(prop.getProperty("error.sign.AdjustAmount")+form.getTransactionNo(),"TransactionNo","01"));
//						 }else if("-".equals(sign2)&&"-".equals(sign1))
//							{
//								if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))*(-1)>Double.parseDouble(form.getPendingAmount().replace(",", ""))*(-1))
//								{
//									list.add(new ErrorCheck(prop.getProperty("error.adjustAmountGreater")+form.getTransactionNo(),"TransactionNo","01"));
//								}else{
//									a=a+Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()));
//								}
//							}else if("+".equals(sign2)&&"+".equals(sign1))
//							{
//								if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))>Double.parseDouble(form.getPendingAmount().replace(",", "")))
//								{
//									list.add(new ErrorCheck(prop.getProperty("error.adjustAmountGreater")+form.getTransactionNo(),"TransactionNo","01"));
//								}else{
//									a=a+Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()));
//								}
//							}
//					}
//					
//			}else
//			{
//						if(receiveAdjustAmountMap1.get(form.getTransactionNo())!=null && !"".equalsIgnoreCase(receiveAdjustAmountMap1.get(form.getTransactionNo())))
//						{
//							list.add(new ErrorCheck(prop.getProperty("error.check")+form.getTransactionNo(),"TransactionNo","01"));
//							if(val.numbervalid((receiveAdjustAmountMap1.get(form.getTransactionNo()))).equalsIgnoreCase("INVALID"))
//							{
//								 list.add(new ErrorCheck(prop.getProperty("error.adjustAmountInv")+form.getTransactionNo(),"adjustAmountInv","01"));
//							}else 
//							{
//								String sign1;
//								String sign2;
//								if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))<0)
//									sign1="-";
//								else
//									sign1="+";
//								if(Double.parseDouble(form.getPendingAmount().replace(",", ""))<0)
//									sign2="-";
//								else
//									sign2="+";
//								 if(!sign1.equals(sign2))
//								{
//									 //list.add(new ErrorCheck(prop.getProperty("error.premiumAmount",new String[] {form.getTransactionNo()}));
//								}else if("-".equals(sign2)&&"-".equals(sign1))
//								{
//									if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))*(-1)>Double.parseDouble(form.getPendingAmount().replace(",", ""))*(-1))
//									{
//										list.add(new ErrorCheck(prop.getProperty("error.adjustAmountGreater")+form.getTransactionNo(),"adjustAmountGreater","01"));
//									}
//								}else if("+".equals(sign2)&&"+".equals(sign1))
//								{
//									if(Double.parseDouble(receiveAdjustAmountMap1.get(form.getTransactionNo()))>Double.parseDouble(form.getPendingAmount().replace(",", "")))
//									{
//										list.add(new ErrorCheck(prop.getProperty("error.adjustAmountGreater")+form.getTransactionNo(),"adjustAmountGreater","01"));
//									}
//								}
//								 
//							}
//						}
//			}
//		}
	return list;
	}
}
