package com.maan.insurance.validation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.AllocateDetailsReq;
import com.maan.insurance.model.req.AllocateViewReq;
import com.maan.insurance.model.req.AllocatedStatusReq;

import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetReceiptAllocateReq;
import com.maan.insurance.model.req.GetReceiptGenerationReq;
import com.maan.insurance.model.req.GetReceiptReversalListReq;
import com.maan.insurance.model.req.GetRetroallocateTransactionReq;
import com.maan.insurance.model.req.GetReversalInfoReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.GetTreasuryJournalViewReq;

import com.maan.insurance.model.req.AllocateTransactionReq;
import com.maan.insurance.model.req.CurrecyAmountReq;
import com.maan.insurance.model.req.GenerationReq;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.ReceiptTreasuryReq;
import com.maan.insurance.model.req.ReceiptViewListReq;
import com.maan.insurance.model.req.RetroTransReq;
import com.maan.insurance.model.req.ReciptListReq;
import com.maan.insurance.model.req.ReverseInsertReq;

import com.maan.insurance.model.req.ReverseViewReq;

import com.maan.insurance.model.req.SecondPageInfoReq;

import com.maan.insurance.model.res.GetTransContractRes;
import com.maan.insurance.model.res.TransContractRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.TreasuryServiceImpl;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Claim.Validation;

@Service
public class TreasuryValidation {
	private Logger log = LogManager.getLogger(TreasuryValidation.class);
	private Properties prop = new Properties();

	@Autowired
	private TreasuryServiceImpl impl;
	@Autowired
	private DropDownServiceImple dropImpl;
	@Autowired
	private CommonCalculation calcu;
	

	private GetTransContractListReq[] RetroTransReq;

	
	public TreasuryValidation() {
		
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

	public List<ErrorCheck> PaymentRecieptvalidate(PaymentRecieptReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val = new Validation();
		GetOpenPeriodRes openPeriodRes = dropImpl.getOpenPeriod(req.getBranchCode());
		try {
			if (StringUtils.isBlank(req.getBroker())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.broker"), "Broker", "1"));
			}else if("63".equals(req.getBroker())) {
				if("0".equalsIgnoreCase(req.getCedingCompany())) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.cedingCo"), "CedingCompany", "2"));

				}
			}
			if("0".equalsIgnoreCase(req.getReceiptBankId())) {
				if("PT".equalsIgnoreCase(req.getTransType())){
					list.add(new ErrorCheck(prop.getProperty("errors.payment.bank"), "ReceiptBankId", "3"));
				}
				else {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.receiptbank"), "ReceiptBankId", "4"));
				}
			}
			if(StringUtils.isBlank(req.getTransactionDate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.tr_date"), "TransactionDate", "1"));

			}
			else if(val.checkDate(req.getTransactionDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.tr_dateInv"), "TransactionDate", "1"));

			}
			if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getTransactionDate()).equalsIgnoreCase("") && StringUtils.isNotBlank(req.getSerialno())){
				if(dropImpl.Validatethree(req.getBranchCode(), req.getTransactionDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
				}
			if("0".equalsIgnoreCase(req.getCurrency()))
			{
				if("PT".equalsIgnoreCase(req.getTransType())) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.paycurrency"), "ReversalDate", "1"));
				}
				else {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.currency"), "ReversalDate", "1"));
				}
			}
			if(StringUtils.isBlank(req.getExchangeRate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.exrate"), "ReversalDate", "1"));		 
			}
			
			if(StringUtils.isBlank(req.getPaymentAmount())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.amount"), "ReversalDate", "1"));
			}else {
				req.setPaymentAmount(req.getPaymentAmount().replaceAll(",",""));
				if(val.numbervalid(req.getPaymentAmount()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.amount1"), "ReversalDate", "1"));
				}
			}
			
			if(StringUtils.isBlank(req.getBankCharges())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.bankChargesReq"), "ReversalDate", "1"));
			}else {
				req.setBankCharges(req.getBankCharges().replaceAll(",",""));
				if("INVALID".equalsIgnoreCase(val.numbervalid(req.getBankCharges()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.bankChargesInv"), "ReversalDate", "1"));
				}
			}
			if(StringUtils.isBlank(req.getPremiumLavy())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.premiumLavyReq"), "ReversalDate", "1"));
			}else {
				req.setPremiumLavy(req.getPremiumLavy().replaceAll(",",""));
				if("INVALID".equalsIgnoreCase(val.numbervalid(req.getPremiumLavy()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.premiumLavyInv"), "ReversalDate", "1"));
				}
			}
            
            if (StringUtils.isBlank(req.getWithHoldingTaxOC())) {
                req.setWithHoldingTaxOC("0");
            }else {
            	req.setWithHoldingTaxOC(req.getWithHoldingTaxOC().replaceAll(",", ""));
            	if("INVALID".equalsIgnoreCase(val.numbervalid(req.getWithHoldingTaxOC()))) {
                	if("PT".equalsIgnoreCase(req.getTransType())) {
                		list.add(new ErrorCheck(prop.getProperty("errors.whtax.check.PT"), "ReversalDate", "1"));
                	}else{
                		list.add(new ErrorCheck(prop.getProperty("errors.whtax.check"), "ReversalDate", "1"));
                	}
                }
            }
			if(StringUtils.isBlank(req.getBaseCurrencyAmount())){
				list.add(new ErrorCheck(prop.getProperty("errors.payment.baseCurrency"), "ReversalDate", "1"));
			}else{
				String ans = calcu.calculateTreasuryPayment(req,"baseCur",0);
				if(Double.parseDouble(ans)!=Double.parseDouble(req.getBaseCurrencyAmount().replaceAll(",", ""))){
					list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "ReversalDate", "1"));
				}else{
					req.setBaseCurrencyAmount(ans);
				}
			}
			
			if(StringUtils.isBlank(req.getNetAmount())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.netAmtReq"), "ReversalDate", "1"));
			}else {
				req.setNetAmount(req.getNetAmount().replaceAll(",",""));
				if("INVALID".equalsIgnoreCase(val.numbervalid(req.getNetAmount()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment.netAmtInv"), "ReversalDate", "1"));
				}else{
					String ans = calcu.calculateTreasuryPayment(req,"netAmt",0);
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getNetAmount())){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "ReversalDate", "1"));
					}else{
						req.setNetAmount(ans);
					}
				}
			}
			if(StringUtils.isNotBlank(req.getSerialno())){
			if(StringUtils.isBlank(req.getAmendDate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.amend_date"), "ReversalDate", "1"));
			}
			else if(val.checkDate(req.getAmendDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.payment.amend_dateInv"), "ReversalDate", "1"));
	            }
			if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAmendDate()).equalsIgnoreCase("") ){
				if(dropImpl.Validatethree(req.getBranchCode(), req.getAmendDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans.amend")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
				}
		}
	        } catch (Exception e) {
	        	log.error(e);
	            list.add(new ErrorCheck(prop.getProperty("error.validate.data"), "ReversalDate", "1"));
	        }
		return list;
	}

	public List<ErrorCheck> reverseInsertvalidate(ReverseInsertReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation validation = new Validation();

		if (StringUtils.isBlank(req.getReversalDate())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.reverseDateReq"), "ReversalDate", "1"));
		}

		else if (validation.checkDate(req.getReversalDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.reverseDateInv"), "ReversalDate", "2"));
		} else if (validation.ValidateTwoDates(req.getAllocatedDate(), req.getReversalDate())
				.equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.revDateGretAlloDate"), "ReversalDate", "3"));
		}
		if (impl.Validatethree(req.getBranchCode(), req.getAllocatedDate()) != 0) {
			if (!req.getAllocatedDate().equalsIgnoreCase(req.getReversalDate())) {
				list.add(new ErrorCheck(prop.getProperty("period.not.closed"), "AllocatedDate", "2"));
			}
		} else {
			
			/*  if(!validation.isNull(bean1.getOpstartDate()).equalsIgnoreCase("")&&
			 !validation.isNull(bean1.getOpendDate()).equalsIgnoreCase("") &&
			  !validation.isNull(req.getReversalDate()).equalsIgnoreCase("") ){
			  if(impl.Validatethree(req.getBranchCode(), req.getReversalDate())==0){
			  list.add(new
			  ErrorCheck(prop.getProperty("errors.open.period.date.reversal"),new String[]
			 {bean1.getOpenPeriodDate()})); }*/
			 
		}
		return list;
	}
	public List<ErrorCheck> getRetroallocateTransactionVali(GetRetroallocateTransactionReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val=new Validation();
		Double a=0.0,b=0.0,c=0.0;
		boolean check=true;
		req.setAmendDate(impl.getAmend(req));
		if(StringUtils.isBlank(req.getAmendDate())) {
			req.setAmendDate(impl.getTrans(req));
		}
		if(StringUtils.isBlank(req.getAccountDate())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.allocationDateReq"),"AccountDate", "01"));

		}
		else if(val.checkDate(req.getAccountDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("error.allocationDate"),"AccountDate", "01"));
		}
		else if(val.ValidateTwoDates(req.getAmendDate(),req.getAccountDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.allocDateGretAlloDate"),"AccountDate", "01"));
		}
		/*if(!val.isNull(bean1.getOpstartDate()).equalsIgnoreCase("")&& !val.isNull(bean1.getOpendDate()).equalsIgnoreCase("") && !val.isNull(bean.getAccountDate()).equalsIgnoreCase("") ){
			if(new DropDownControllor().Validatethree(branchCode, bean.getAccountDate())==0){
				list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.allocate",new String[] {bean1.getOpenPeriodDate()}));
			}*/
		return list;
			}
	

	public List<ErrorCheck> getReceiptReversalListVali(GetReceiptReversalListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getTranstype())) {
			list.add(new ErrorCheck("Please Enter Transtype", "Transtype", "6"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "8"));
		}
		return list;
	}
	public List<ErrorCheck> Reciptgetvalidate(ReciptListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.getReceipt.BranchCode"), "BranchCode","1"));
			} 
		if(StringUtils.isBlank(req.getTransType())) {
			list.add(new ErrorCheck(prop.getProperty("errors.getReceipt.TransType"), "TransType","2"));
			} 
		return list;
	}
	
	public List<ErrorCheck> RetroTransvalidate(RetroTransReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		GetTransContractListReq listreq = new GetTransContractListReq();
		if(StringUtils.isBlank(req.getBranchcode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.BranchCode"), "BranchCode","1"));
			}
		if(StringUtils.isBlank(req.getTransType())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.TransType"), "TransType","2"));
			}
		
		if(StringUtils.isBlank(req.getAlloccurrencyid())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.Alloccurrencyid"), "Alloccurrencyid","3"));
			} 
		 
		if(StringUtils.isBlank(req.getBrokerid())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.Brokerid"), "Brokerid","4"));
			} 
		if(StringUtils.isBlank(req.getCedingid())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.cedingid"), "cedingid","5"));
			} 
		for(GetTransContractListReq r : req.getTransContractListReq()) {
		if(StringUtils.isBlank(r.getReceivePayAmounts())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.ReceivePayAmounts"), "ReceivePayAmounts","6"));
			} 
		if(StringUtils.isBlank(r.getTransactionNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.RetroTrans.TransactionNo"), "TransactionNo","7"));
			}
		}
		
		
		
		return list;
	}
	public List<ErrorCheck> ReciptTreasuryvalidate(ReceiptTreasuryReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.ReceiptTreasury.BranchCode"), "BranchCode","1"));
			}
		if(StringUtils.isBlank(req.getBroker())) {
			list.add(new ErrorCheck(prop.getProperty("errors.ReceiptTreasury.Broker"), "Broker","2"));
			}
		if(StringUtils.isBlank(req.getPayrecno())) {
			list.add(new ErrorCheck(prop.getProperty("errors.ReceiptTreasury.Payrecno"), "Payrecno","3"));
			}
		return list;
	}
	
	public List<ErrorCheck> validateCurrencyAmount(CurrecyAmountReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.CurrencyAmount.BranchCode"), "BranchCode","1"));
			}
		if(StringUtils.isBlank(req.getSerialNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.CurrencyAmount.Serialno"), "Serialno","2"));
			}
		if(StringUtils.isBlank(req.getCurrValu())) {
			list.add(new ErrorCheck(prop.getProperty("errors.CurrencyAmount.CurrValu"), "CurrValu","3"));
			}
		return list;
	}
	
	public List<ErrorCheck> validateSecondPageInfo(SecondPageInfoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.SecondPageInfo.BranchCode"), "BranchCode","1"));
			}
		if(StringUtils.isBlank(req.getSerialno())) {
			list.add(new ErrorCheck(prop.getProperty("errors.SecondPageInfo.Serialno"), "Serialno","2"));
			}
		return list;
	}

	public List<ErrorCheck> validateTransCont(GetTransContractReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val=new Validation();
		Double a=0.0,b=0.0,c=0.0;
		boolean check=true;
		req.setAmendDate(impl.getAmend(req));
		if(StringUtils.isBlank(req.getAmendDate())) {
			req.setAmendDate(impl.getTrans(req));
		}
		if(StringUtils.isBlank(req.getAccountDate())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.allocationDateReq"),"allocationDate","1"));

		}
		else if(val.checkDate(req.getAccountDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("error.allocationDate"),"allocationDate","2"));
		}
		else if(val.ValidateTwoDates(req.getAmendDate(),req.getAccountDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.allocDateGretAlloDate"),"allocDateGretAlloDate","3"));
		}
//		if(!val.isNull(bean1.getOpstartDate()).equalsIgnoreCase("")&& !val.isNull(bean1.getOpendDate()).equalsIgnoreCase("") && !val.isNull(bean.getAccountDate()).equalsIgnoreCase("") ){
//			if(new DropDownControllor().Validatethree(req.getBranchCode(), req.getAccountDate())==0){
//				addActionError(getText("errors.open.period.date.allocate",new String[] {bean1.getOpenPeriodDate()}));
//			}
//			}
		/*if(val.checkDate(bean.getAccountDate()).equalsIgnoreCase("Invalid"))
		{
			this.addActionError(getText("error.allocationDate"));
		}*/
		
		List<TransContractRes> payList = impl.getAllTransContract(req);
		
		for(TransContractRes Obj : payList)//int i=0;i<payList.size();i++
		{	
			
			TransContractRes form = Obj;
			//if("Y".equalsIgnoreCase(form.getCheckYN()))
			List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionno().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(filterTrack)) {
			
				check=false;
				if("P".equalsIgnoreCase(form.getCheckPC())){
					if(filterTrack.get(0).getTransactionNo()==null || "".equalsIgnoreCase(filterTrack.get(0).getTransactionNo()))
					 {			 
						list.add(new ErrorCheck(prop.getProperty("payment.accPremiumReq"),"accPremiumReq","4"));
					 }else if(val.numbervalid((filterTrack.get(0).getTransactionNo())).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("payment.accPremiumInv"),"accPremiumInv","5"));
					}else
					{
						String sign1;
						String sign2;
						if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())<0)
							sign1="-";
						else
							sign1="+";
						if(Double.parseDouble(form.getNetdue())<0)
							sign2="-";
						else
							sign2="+";
						 if(!sign1.equals(sign2))
						 {
							 list.add(new ErrorCheck(prop.getProperty("error.premiumAmount"),"premiumAmount","6"));
						 }else if("-".equals(sign2)&&"-".equals(sign1))
							{
								if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())*(-1)>Double.parseDouble(form.getNetdue())*(-1))
								{
									list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","7"));
								}else{
									a=a+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
								}
							}else if("+".equals(sign2)&&"+".equals(sign1))
							{
								if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())>Double.parseDouble(form.getNetdue()))
								{
									list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","8"));
								}else{
									a=a+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
								}
							}
					}
					}else if("C".equalsIgnoreCase(form.getCheckPC()))
					{
					log.info("accClaim==>"+filterTrack.get(0).getTransactionNo());
					log.info("payment==>"+form.getPayamount());
					if(filterTrack.get(0).getTransactionNo()==null || "".equalsIgnoreCase(filterTrack.get(0).getTransactionNo()))
					 {			 
						list.add(new ErrorCheck(prop.getProperty("payment.accClaimReq"),"accClaimReq","9"));
					 }else if(val.numbervalid((filterTrack.get(0).getTransactionNo())).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("payment.accClaimInv"),"accClaimInv","10"));
					}else
					{
						String sign1;
						String sign2;
						if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())<0)
							sign1="-";
						else
							sign1="+";
						if(Double.parseDouble(form.getPayamount())<0)
							sign2="-";
						else
							sign2="+";
						if(!sign1.equals(sign2))
						{
							list.add(new ErrorCheck(prop.getProperty("error.claimAmount"),"claimAmount","11"));
						}else if("-".equals(sign2)&&"-".equals(sign1))
						{
							if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())*(-1)>Double.parseDouble(form.getPayamount())*(-1))
								{
								list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","12"));
								}else{
									b=b+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());	
								}
						}else if("+".equals(sign2)&&"+".equals(sign1))
						{
							if(Double.parseDouble(filterTrack.get(0).getReceivePayAmounts())>Double.parseDouble(form.getPayamount()))
							{
								list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","13"));
							}else{
								b=b+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());	
							}
							
						}
					}
					
				}
				
			} /*else
			{
					if("P".equalsIgnoreCase(form.getCheckPC())){
						log.info("Acc Premium===>"+filterTrack.get(0).getTransactionNo());
						if(filterTrack.get(0).getTransactionNo()!=null && !"".equalsIgnoreCase(filterTrack.get(0).getTransactionNo()))
						{
							list.add(new ErrorCheck(prop.getProperty("error.check"),"check","14"));
							if(val.numbervalid((filterTrack.get(0).getTransactionNo())).equalsIgnoreCase("INVALID"))
							{
								list.add(new ErrorCheck(prop.getProperty("payment.accPremiumInv"),"accPremiumInv","15"));
							}else 
							{
								String sign1;
								String sign2;
								if(Double.parseDouble(filterTrack.get(0).getTransactionNo())<0)
									sign1="-";
								else
									sign1="+";
								if(Double.parseDouble(form.getNetdue())<0)
									sign2="-";
								else
									sign2="+";
								 if(!sign1.equals(sign2))
								{
									 list.add(new ErrorCheck(prop.getProperty("error.premiumAmount"),"premiumAmount","16"));
								}else if("-".equals(sign2)&&"-".equals(sign1))
								{
									if(Double.parseDouble(filterTrack.get(0).getTransactionNo())*(-1)>Double.parseDouble(form.getNetdue())*(-1))
									{
										list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","17"));
									}
								}else if("+".equals(sign2)&&"+".equals(sign1))
								{
									if(Double.parseDouble(filterTrack.get(0).getTransactionNo())>Double.parseDouble(form.getNetdue()))
									{
										list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","18"));
									}
								}
								 
							}
						}
					}else if("C".equalsIgnoreCase(form.getCheckPC()))
					{
						log.info("Acc Claim===>"+filterTrack.get(0).getTransactionNo());
						if(filterTrack.get(0).getTransactionNo()!=null && !"".equalsIgnoreCase(filterTrack.get(0).getTransactionNo()))
						{
							list.add(new ErrorCheck(prop.getProperty("error.check"),"check","19"));
							if(val.numbervalid((filterTrack.get(0).getTransactionNo())).equalsIgnoreCase("INVALID"))
							{
								list.add(new ErrorCheck(prop.getProperty("payment.accClaimInv"),"accClaimInv","19"));
							}else
							{
									String sign1;
									String sign2;
									if (Double.parseDouble(filterTrack.get(0).getTransactionNo()) < 0)
										sign1 = "-";
									else
										sign1 = "+";
									if (Double.parseDouble(form.getPayamount()) < 0)
										sign2 = "-";
									else
										sign2 = "+";
									if(!sign1.equals(sign1))
									{
										list.add(new ErrorCheck(prop.getProperty("error.claimAmount"),"claimAmount","20"));
									}else if("-".equals(sign2)&&"-".equals(sign1))
									{
										if(Double.parseDouble(filterTrack.get(0).getTransactionNo())*(-1)>Double.parseDouble(form.getPayamount())*(-1))
											{
											list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","21"));
											}
									}else if("+".equals(sign2)&&"+".equals(sign1))
									{
										if(Double.parseDouble(filterTrack.get(0).getTransactionNo())>Double.parseDouble(form.getPayamount()))
										{
											list.add(new ErrorCheck(prop.getProperty("error.claimAmountGreater"),"claimAmountGreater","22"));
										}										
									}
								}
						}
					
				}
				
			}*/
		}
		if(check) {
			list.add(new ErrorCheck(prop.getProperty("error.one.check"),"check","23"));
		}
		if(Double.parseDouble(req.getCurrencyValue())>=0){//condition added for -amount allocation for claim by sathish on 10/09/2012
			if(req.getTransType().equalsIgnoreCase("RT"))
			{
			//c=a-b;
			c=Math.round((a-b) * 100.0) / 100.0;
			//c=a+b;
			log.info("Sum of Accepted Premium A==>"+a);
			log.info("Sum of Accepted Premium B==>"+b);
			log.info("Sum of Accepted Premium A-B==>"+c);
			//if(b<0 || c<0 )
			if(c<0)
			{
				list.add(new ErrorCheck(prop.getProperty("error.sumAmountInvalidPremium"),"sumAmountInvalidPremium","24"));
			}
			}else if(req.getTransType().equalsIgnoreCase("PT"))
			{
				//c=b-a;
				c=Math.round((b-a) * 100.0) / 100.0;
				//c=b+a;
				log.info("Sum of Accepted Premium A==>"+a);
				log.info("Sum of Accepted Premium B==>"+b);
				log.info("Sum of Accepted Premium A-B==>"+c);
				//if(a<0 || c<0)
				if(c<0)
				{
					list.add(new ErrorCheck(prop.getProperty("error.sumAmountInvalidCliam"),"sumAmountInvalidCliam","25"));
				}
				
			}
			log.info("Recipt Amount ==>"+req.getCurrencyValue());
			if(c>Double.parseDouble(req.getCurrencyValue()))
			{
				list.add(new ErrorCheck(prop.getProperty("error.receiptAmount"),"receiptAmount","26"));
			}
		}
		return list;
			
	/*	if("0".equalsIgnoreCase(request.getParameter("trans")))
		{
			this.addActionError("currency",getText("errors.payment.checkboxs"));
		}*/
		
	}
	public List<ErrorCheck> ReciptviewListvalidate(ReceiptViewListReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ErrorCheck> getReceiptGenerationVali(GetReceiptGenerationReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getBroker())) {
			//list.add(new ErrorCheck("Please Enter Broker", "Broker", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}

		if (StringUtils.isBlank(req.getSerialNo())) {
			list.add(new ErrorCheck("Please Enter SerialNo", "SerialNo", "3"));
		}
		return list;
	}

	public List<ErrorCheck> getTreasuryJournalViewVali(GetTreasuryJournalViewReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();


		if (StringUtils.isBlank(req.getAllocationNo())) {
			list.add(new ErrorCheck("Please Enter AllocationNo", "AllocationNo", "2"));
		}

		if (StringUtils.isBlank(req.getSerialNo())) {
			list.add(new ErrorCheck("Please Enter SerialNo", "SerialNo", "3"));
		}
		return list;
	}

	public List<ErrorCheck> reverseViewallocateViewValidate(ReverseViewReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getSerialNo())) {
			list.add(new ErrorCheck("Please Enter SerialNo", "SerialNo", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		return list;
	}

	public List<ErrorCheck> getReversalInfoVali(GetReversalInfoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		

		if (StringUtils.isBlank(req.getBroker())) {
			list.add(new ErrorCheck("Please Enter Broker", "Broker", "2"));
		}

		/*
		 * if (StringUtils.isBlank(req.getCedingCo())) { list.add(new
		 * ErrorCheck("Please Enter CedingCo", "CedingCo", "3")); }
		 */
		if (StringUtils.isBlank(req.getTransType())) {
			list.add(new ErrorCheck("Please Enter TransType", "TransType", "4"));
		}

		if (StringUtils.isBlank(req.getPaymentAmount())) {
			list.add(new ErrorCheck("Please Enter PaymentAmount", "PaymentAmount", "5"));
		}

		if (StringUtils.isBlank(req.getCurrency())) {
			list.add(new ErrorCheck("Please Enter Currency", "Currency", "6"));
		}
		if (StringUtils.isBlank(req.getCurrencyValue())) {
			//list.add(new ErrorCheck("Please Enter CurrencyValue", "CurrencyValue", "7"));
		}

	/*	if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "8"));
		} */

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "9"));
		}
		return list; 
	}

	public List<ErrorCheck> allocateDetailsVali(AllocateDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getPayRecNo())) {
			list.add(new ErrorCheck("Please Enter PayRecNo", "PayRecNo", "1"));
		}
		/*
		 * if (StringUtils.isBlank(req.getBroker())) { list.add(new
		 * ErrorCheck("Please Enter Broker", "Broker", "2")); } if
		 * (StringUtils.isBlank(req.getCedingCo())) { list.add(new
		 * ErrorCheck("Please Enter CedingCo", "CedingCo", "3")); } if
		 * (StringUtils.isBlank(req.getSerialNo())) { list.add(new
		 * ErrorCheck("Please Enter SerialNo", "SerialNo", "4")); }
		 */
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "5"));
		}
		return list;
	}

	public List<ErrorCheck> getAllocatedStatusVali(AllocatedStatusReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAlloccurrencyId())) {
			//list.add(new ErrorCheck("Please Enter AlloccurrencyId", "AlloccurrencyId", "1"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			//list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
		}
		if (StringUtils.isBlank(req.getPayRecNo())) {
			list.add(new ErrorCheck("Please Enter PayRecNo", "PayRecNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "5"));
		}
		return list;
	}

	public List<ErrorCheck> getTransContractVali(GetTransContractReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAlloccurrencyId())) {
			list.add(new ErrorCheck("Please Enter AlloccurrencyId", "AlloccurrencyId", "1"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "3"));
		}
		/*
		 * if (StringUtils.isBlank(req.getTransContractListReq().get(0).
		 * getReceivePayAmounts())) { list.add(new
		 * ErrorCheck("Please Enter ReceivePayAmounts", "ReceivePayAmounts", "4")); } if
		 * (StringUtils.isBlank(req.getTransContractListReq().get(0).getTransactionNo())
		 * ) { list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo",
		 * "5")); }
		 */
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "6"));
		}
		return list;
	}

	public List<ErrorCheck> allocateViewVali(AllocateViewReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAlloccurrencyId())) {
			//list.add(new ErrorCheck("Please Enter AlloccurrencyId", "AlloccurrencyId", "1"));
		}
		if (StringUtils.isBlank(req.getCurrecncyValue())) {
			//list.add(new ErrorCheck("Please Enter CurrecncyValue", "CurrecncyValue", "3"));
		}
		if (StringUtils.isBlank(req.getPayRecNo()) ){
			//list.add(new ErrorCheck("Please Enter PayRecNo", "PayRecNo", "4"));
		}
		if (StringUtils.isBlank(req.getSerialNo())) {
			//list.add(new ErrorCheck("Please Enter SerialNo", "SerialNo", "5"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "6"));
		}
		return list;
	}

	public List<ErrorCheck> getReceiptAllocateVali(GetReceiptAllocateReq req) {
		
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getTransType())) {
			list.add(new ErrorCheck("Please Enter TransType", "TransType", "2"));
		}
		return list;
	}

	public List<ErrorCheck> getAllTransContractVali(GetAllTransContractReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAlloccurrencyid())) {
			list.add(new ErrorCheck("Please Enter AlloccurrencyId", "AlloccurrencyId", "1"));
		}
		if (StringUtils.isBlank(req.getBrokerid())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
		}
		if (StringUtils.isBlank(req.getCedingid())) {
			list.add(new ErrorCheck("Please Enter Cedingid", "Cedingid", "3"));
		}
		if (StringUtils.isBlank(req.getBranchCode()) ){
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "4"));
		}
		return list;
	}

	

}
