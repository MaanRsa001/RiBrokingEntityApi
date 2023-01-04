package com.maan.insurance.validation.facPremium;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.facPremium.AddFieldValueReq;
import com.maan.insurance.model.req.facPremium.BonusdetailsReq;
import com.maan.insurance.model.req.facPremium.GetBonusValueReq;
import com.maan.insurance.model.req.facPremium.GetFieldValuesReq;
import com.maan.insurance.model.req.facPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumContractDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumEditReq;
import com.maan.insurance.model.req.facPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.req.facPremium.PremiumedListreq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.facPremium.FacPremiumServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.facultative.FacultativeValidation;

@Service
public class FacPremiumValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(FacultativeValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private FacPremiumServiceImple facPreImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public FacPremiumValidation() {
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


	public List<ErrorCheck> premiumedListvalidate(PremiumedListreq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getOpendDate())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "3"));
		}
		if (StringUtils.isBlank(req.getOpstartDate())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "4"));
		}
		return list;
	}

	public List<ErrorCheck> premiumEditVali(PremiumEditReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "3"));
		}
		if (StringUtils.isBlank(req.getGwpiOS())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "4"));
		}
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "5"));
		}
		if (StringUtils.isBlank(req.getPremiumQuotaShare())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "6"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "7"));
		}
		if (StringUtils.isBlank(req.getRdsExchageRate())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "8"));
		}
		if (StringUtils.isBlank(req.getTableType())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "9"));
		}
		if (StringUtils.isBlank(req.getRequestNo())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "10"));
		}
		if (StringUtils.isBlank(req.getTransaction())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "11"));
		}
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "12"));
		}
		if (StringUtils.isBlank(req.getTransDropDownVal())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "13"));
		}
		return list;
	}


	public List<ErrorCheck> validateFaculPremium(PremiumInsertMethodReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val=new Validation();
		boolean flag=false;
		boolean cashLossCrFlag=false;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
		try {
			List<Map<String,Object>> retroList= facPreImpl.getSPRetroList(bean.getContNo(), bean.getProductId(), bean.getLayerno());
			 if(retroList!=null&&retroList.size()>0)
			 {
				 Map<String,Object>	 map=retroList.get(0);				 
				 bean.setProductId(bean.getProductId());
				 bean.setSpRetro(map.get("SP_RETRO")==null?"N":map.get("SP_RETRO").toString());
				 bean.setNoOfRetro(map.get("NO_OF_INSURERS")==null?"0":map.get("NO_OF_INSURERS").toString());
				 bean.setProposalNo(map.get("RSK_PROPOSAL_NUMBER")==null?"0":map.get("RSK_PROPOSAL_NUMBER").toString());
				 if("Y".equalsIgnoreCase(bean.getSpRetro()))
				 {
					 List<Map<String,Object>> insList=facPreImpl.getRetroContracts(bean.getProposalNo(),bean.getNoOfRetro());
					 if(insList!=null && insList.size()>0){
						 for(int i=0;i<insList.size();i++){
							 Map<String,Object>	 insMap=insList.get(i);		
							 if("C".equals(insMap.get("TYPE")))
							 {
								 bean.setRetroContractNo(insMap.get("CONTRACT_NO").toString());
								 String string= facPreImpl.getSumOfShareSign(bean.getContNo(),bean.getRetroContractNo());
								 if(Double.parseDouble(string)!=100){
									 list.add(new ErrorCheck(prop.getProperty("errors.retroNotCompleted")+bean.getRetroContractNo(),"retroNotCompleted","01"));
								 }
							 }							
						 }
					 }
				 }
			 }
			 if(!(bean.getPremiumQuotaShare()).equalsIgnoreCase(""))
					    {
					   	  bean.setPremiumQuotaShare((bean.getPremiumQuotaShare()).replaceAll(",",""));
					   	  if(val.numbervalid(bean.getPremiumQuotaShare()).equalsIgnoreCase("INVALID"))
					   	  {
					   		list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuota.Invalid"),"PremiumQuota","01"));
					   	  }else
					   	  {
					   		  double gwpi=Double.parseDouble(bean.getGwpiOS());
					   		  double sumPaidPre=Double.parseDouble(bean.getSumofpaidpremium());
					   		  double premium=Double.parseDouble(bean.getPremiumQuotaShare());					   		 
					   		  if("1".equals(bean.getEnteringMode()))
					   		  {
					   			premium=(premium*Double.parseDouble(bean.getShareSigned())/100.0);
					   		  }
					   		if(!"edit".equalsIgnoreCase(bean.getMode())){
					   		  DecimalFormat formatter = new  DecimalFormat("#.##");
					   		  double preBal=Double.parseDouble(formatter.format((gwpi-sumPaidPre)));                             
					   		  if(preBal<premium)
					   		  {
					   		  }
					   		}
					   	  }
					    }	
			 if("RI02".equalsIgnoreCase(bean.getSourceId())){
			 if(StringUtils.isBlank(bean.getTaxDedectSource())){
				 list.add(new ErrorCheck(prop.getProperty("taxdedct.source.invalid"),"TaxDedectSource","01"));
			 }
			 else{
				 bean.setTaxDedectSource(bean.getTaxDedectSource().replaceAll(",", ""));
				 if(val.numbervalid(bean.getTaxDedectSource()).equalsIgnoreCase("INVALID"))
				 {
					 list.add(new ErrorCheck(prop.getProperty("error.taxdedect.number"),"TaxDedectSource","01"));
				 }
			 }
			 }
					 if(val.isNull(bean.getEnteringMode()).equalsIgnoreCase(""))
					 {
					 flag=true;
					 list.add(new ErrorCheck(prop.getProperty("errors.mode.reqired"),"mode","01")); 
					 }	 
				 	 if(val.isNull(bean.getCurrency()).equalsIgnoreCase("0"))
					 {						 
				 		list.add(new ErrorCheck(prop.getProperty("errors.currency.requireds"),"currency","01"));							
					 }
					 if(val.isNull(bean.getExchRate()).equalsIgnoreCase(""))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.exchRate.required"),"exchRate","01"));
					 }
					if(StringUtils.isBlank(bean.getRicession())){
						list.add(new ErrorCheck(prop.getProperty("errors.ricession.required"),"ricession","01"));
					}
					boolean dateflag=true; 
					boolean statDate=true;
					if(val.isNull(bean.getInceptionDate()).equalsIgnoreCase(""))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.statRecDate.required"),"statRecDate","01"));
							dateflag=false;
							statDate=false;
					}
					else if(val.checkDate(bean.getInceptionDate()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.statRecDate.invalid"),"statRecDate","01"));
							dateflag=false;
							statDate=false;
					}else if(Validation.ValidateTwo(bean.getStatementDate(),bean.getInceptionDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.statRecDate"),"premium","01"));
						
					}
					if(val.isNull(bean.getStatementDate()).equalsIgnoreCase(""))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.statDate.required"),"statDate","01"));
							dateflag=false;
							statDate=false;
					}
					else if(val.checkDate(bean.getStatementDate()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.statDate.invalid"),"statDate","01"));
							dateflag=false;
							statDate=false;
					}else if(Validation.ValidateTwo(bean.getInsDate(),bean.getStatementDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.statDate"),"statDate","01"));
						
					}
					if("edit".equalsIgnoreCase(bean.getMode()) && !"Temp".equalsIgnoreCase(bean.getTableType())){
						bean.setMaxDate(Validation.getMaxDateValidate(bean.getTransaction(), bean.getPreamendmentDate()));
					if(val.isNull(bean.getAmendmentDate()).equalsIgnoreCase(""))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.amendmentDate.required"),"amendmentDate","01"));
							dateflag=false;
							statDate=false;
					}
					else if(val.checkDate(bean.getAmendmentDate()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.amendmentDate.invalid"),"amendmentDate","01"));
							dateflag=false;
							statDate=false;
					}else if(Validation.ValidateTwo(bean.getMaxDate(),bean.getAmendmentDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.amendDate"),"amendDate","01"));
						
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getAmendmentDate()).equalsIgnoreCase("")){
						if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getAmendmentDate())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans.amend")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
					}
					}
					if(val.isNull(bean.getTransaction()).equalsIgnoreCase(""))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.transaction.required"),"transaction","01"));			
					}
					else if(val.checkDate(bean.getTransaction()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.transaction.invalid"),"transaction","01"));

					}else if(dateflag && Validation.ValidateTwo(bean.getInceptionDate(),bean.getTransaction()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.transaction"),"transaction","01"));
					}else if(dateflag && Validation.ValidateTwo(bean.getAcceptenceDate(),bean.getTransaction()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.acDate")+bean.getAcceptenceDate(),"acDate","01"));
					}
					else if(!"superuser".equalsIgnoreCase(bean.getUserType()))
					{
						String morRepMaxDate = facPreImpl.getMovementReportMaxDate(bean.getBranchCode());
						if(StringUtils.isBlank(bean.getTransactionNo())&&StringUtils.isNotBlank(morRepMaxDate)&&Validation.ValidateTwo(morRepMaxDate,bean.getTransaction()).equalsIgnoreCase("invalid")){
							list.add(new ErrorCheck(prop.getProperty("errors.tranDate.invalid"),"tranDate","01"));
						}
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getTransaction()).equalsIgnoreCase("") && !"edit".equalsIgnoreCase(bean.getMode())){
						if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getTransaction())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
						}
					if(!val.isNull(bean.getReceiptno()).equalsIgnoreCase(""))
						 {
							 flag=true;						   
							 if(val.numbervalid(bean.getReceiptno()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("errors.receipt_no.Check"),"Receiptno","01"));			 
							 }
									
						 }	
						 if(val.isSelect(bean.getAccountPeriod()).equalsIgnoreCase(""))
						 {
							 flag=true;
							 list.add(new ErrorCheck(prop.getProperty("errors.account_Period.required"),"AccountPeriod","01"));
						 }else 
						 {
							 String[] ins=bean.getAccountPeriod().split("_");
							 if((!"EP".equalsIgnoreCase(ins[0]) && !"RTP".equalsIgnoreCase(ins[0]) && !"RVP".equalsIgnoreCase(ins[0]))){
							
							 if(!"2".equals(bean.getEnteringMode())){
								 list.add(new ErrorCheck(prop.getProperty("errors.MandD.enteringModeInvalid")+bean.getShareSigned(),"EnteringMode","01"));
							 }
						  }
						  else
						  {
							  if(Double.parseDouble(bean.getPremiumQuotaShare())==0|| bean.getPremiumQuotaShare().equalsIgnoreCase(""))
								 {
									 list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuota.Zero"),"PremiumQuota","01"));
								 }
						  }
						 } 
						
						 if(!val.isNull(bean.getCommissionQuotaShare()).equalsIgnoreCase(""))
						 {
							 flag=true;						
							 bean.setCommissionQuotaShare((bean.getCommissionQuotaShare()).replaceAll(",",""));
							 if(val.numbervalid(bean.getCommissionQuotaShare()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("errors.commission.Check"),"commission","01")); 
							 } 
						 }						  
						 
						 if(!val.isNull(bean.getTax()).equalsIgnoreCase(""))
						 {
							 flag=true;						
							 bean.setTax((bean.getTax()).replaceAll(",",""));
							 if(val.numbervalid(bean.getTax()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("errors.tax.check"),"tax","01"));
							 }
						 }	
						 if(StringUtils.isBlank(bean.getWithHoldingTaxOC())){
								list.add(new ErrorCheck(prop.getProperty("with.tax.empty"),"tax","01")); 
							 }
							 else{
								 bean.setWithHoldingTaxOC(bean.getWithHoldingTaxOC().replaceAll(",", ""));
								 if(val.numbervalid(bean.getWithHoldingTaxOC()).equalsIgnoreCase("INVALID"))
								 {
									 list.add(new ErrorCheck(prop.getProperty("error.withtax.number"),"withtax","01"));
								 }	
							 }
						 if(StringUtils.isBlank(bean.getBonus())){
								list.add(new ErrorCheck(prop.getProperty("with.bonus.empty"),"bonus","01")); 
							 }
							 else{
								 bean.setBonus(bean.getBonus().replaceAll(",", ""));
								 if(val.numbervalid(bean.getBonus()).equalsIgnoreCase("INVALID"))
								 {
									 list.add(new ErrorCheck(prop.getProperty("error.bonus.number"),"bonus","01"));
								 }	
							 }
						 if("RI02".equalsIgnoreCase(bean.getSourceId())){
							 if(StringUtils.isBlank(bean.getServiceTax())){
								list.add(new ErrorCheck(prop.getProperty("servicetax.empty"),"servicetax","01")); 
							 }
							 else{
								 bean.setServiceTax(bean.getServiceTax().replaceAll(",", ""));
								 if(val.numbervalid(bean.getServiceTax()).equalsIgnoreCase("INVALID"))
								 {
									 list.add(new ErrorCheck(prop.getProperty("error.servicetax.number"),"servicetax","01"));
								 }	
							 }
						 }
						 if(!val.isNull(bean.getBrokerage()).equalsIgnoreCase(""))
						 {
							 flag=true;							
							 bean.setBrokerage((bean.getBrokerage()).replaceAll(",",""));
							 if(val.numbervalid(bean.getBrokerage()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("errors.brokerage.check"),"brokerage","01"));
							 }
						 }						 
						 if(val.isNull(bean.getBrokerage()).equalsIgnoreCase(""))
						 {
							 flag=true;
						 }										
					bean.setOtherCost((bean.getOtherCost()).replaceAll(",",""));
					bean.setTotalCredit((bean.getTotalCredit()).replaceAll(",",""));
					bean.setTotalDebit((bean.getTotalDebit()).replaceAll(",",""));
					if(!flag)
					{
						list.add(new ErrorCheck(prop.getProperty("errors.currency.select"),"currency","01"));	 
					}	
					if("transEdit".equalsIgnoreCase(bean.getMode())){
						if(StringUtils.isBlank(bean.getTransDropDownVal()) && "Yes".equalsIgnoreCase(bean.getChooseTransaction())){
							list.add(new ErrorCheck(prop.getProperty("resersel.trans"),"TransDropDownVal","01"));
						}
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
					return list;	
	}

	public List<ErrorCheck> getPremiumDetailsVali(GetPremiumDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getGwpiOS())) {
			list.add(new ErrorCheck("Please Enter GwpiOS", "GwpiOS", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getRdsExchageRate())) {
			list.add(new ErrorCheck("Please Enter RdsExchageRate", "RdsExchageRate", "5"));
		}
		if (StringUtils.isBlank(req.getRequestNo())) {
			list.add(new ErrorCheck("Please Enter RequestNo", "RequestNo", "6"));
		}
		if (StringUtils.isBlank(req.getTableType())) {
			list.add(new ErrorCheck("Please Enter TableType", "TableType", "7"));
		}
		return list;
	}



	public List<ErrorCheck> GetFieldValuesVali(GetFieldValuesReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getAdjustmentpremium())) {
			list.add(new ErrorCheck("Please Enter Adjustmentpremium", "Adjustmentpremium", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getBonusId())) {
			list.add(new ErrorCheck("Please Enter BonusId", "BonusId", "5"));
		}
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "6"));
		}
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "7"));
		}
		if (StringUtils.isBlank(req.getLayerno())) {
			list.add(new ErrorCheck("Please Enter Layerno", "Layerno", "8"));
		}
		if (StringUtils.isBlank(req.getMdpremium())) {
			list.add(new ErrorCheck("Please Enter Mdpremium", "Mdpremium", "9"));
		}
		if (StringUtils.isBlank(req.getPremiumQuotaShare())) {
			list.add(new ErrorCheck("Please Enter PremiumQuotaShare", "PremiumQuotaShare", "10"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "11"));
		}
		if (StringUtils.isBlank(req.getTransaction())) {
			list.add(new ErrorCheck("Please Enter Transaction", "Transaction", "12"));
		}
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "13"));
		}
		return list;
	}


	public List<ErrorCheck> bonusdetailsVali(BonusdetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "3"));
		}
		if (StringUtils.isBlank(req.getCurrencyName())) {
			list.add(new ErrorCheck("Please Enter CurrencyName", "CurrencyName", "4"));
		}
		if (StringUtils.isBlank(req.getCurrencyId())) {
			list.add(new ErrorCheck("Please Enter CurrencyId", "CurrencyId", "5"));
		}
		if (StringUtils.isBlank(req.getExchRate())) {
			list.add(new ErrorCheck("Please Enter ExchRate", "ExchRate", "6"));
		}
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "7"));
		}
		if (StringUtils.isBlank(req.getLayerno())) {
			list.add(new ErrorCheck("Please Enter Layerno", "Layerno", "8"));
		}
		if (StringUtils.isBlank(req.getPremiumOC())) {
			list.add(new ErrorCheck("Please Enter PremiumOC", "PremiumOC", "9"));
		}
		if (StringUtils.isBlank(req.getTransaction())) {
			list.add(new ErrorCheck("Please Enter Transaction", "Transaction", "10"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "11"));
		}
		
		return list;
	}


	public List<ErrorCheck> addFieldValueVali(AddFieldValueReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBonusExchangeRate())) {
			list.add(new ErrorCheck("Please Enter BonusExchangeRate", "BonusExchangeRate", "1"));
		}
		if (StringUtils.isBlank(req.getTransaction())) {
			list.add(new ErrorCheck("Please Enter Transaction", "Transaction", "2"));
		}
		if (CollectionUtils.isEmpty(req.getCurrencyShortName())) {
			list.add(new ErrorCheck("Please Enter CurrencyShortName", "CurrencyShortName", "3"));
		}
		if (CollectionUtils.isEmpty(req.getPremiumFinallist2())) {
			list.add(new ErrorCheck("Please Enter PremiumFinallist2", "PremiumFinallist2", "4"));
		}
		return list;
	}


}
