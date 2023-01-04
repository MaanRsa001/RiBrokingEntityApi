package com.maan.insurance.validation.XolPremium;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.MdInstallmentDatesReq;
import com.maan.insurance.model.req.xolPremium.PremiumEditReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.XolPremium.XolPremiumServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

@Service
public class XolPremiumValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(XolPremiumValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private XolPremiumServiceImple xolPreImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public XolPremiumValidation() {
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

	public static List<ErrorCheck> getRetroContractsVali(com.maan.insurance.model.req.xolPremium.GetRetroContractsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		
		if (StringUtils.isBlank(req.getNoOfRetro())) {
			list.add(new ErrorCheck("Please Enter NoOfRetro", "NoOfRetro", "3"));
		}
		return list;
	}

	public List<ErrorCheck> getPremiumedListVali(GetPremiumedListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "3"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "4"));
		}
		if (StringUtils.isBlank(req.getOpendDate())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "5"));
		}
		if (StringUtils.isBlank(req.getOpstartDate())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "6"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "7"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "8"));
		}
		return list;
	}

	public List<ErrorCheck> mdInstallmentDatesVali(MdInstallmentDatesReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getLayerno())) {
			list.add(new ErrorCheck("Please Enter Layerno", "Layerno", "2"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getSourceId())) {
			list.add(new ErrorCheck("Please Enter SourceId", "SourceId", "5"));
		}
		return list;
	}
	public List<ErrorCheck> contractDetailsVali(ContractDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getLayerno())) {
			list.add(new ErrorCheck("Please Enter Layerno", "Layerno", "2"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "5"));
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
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "7"));
		}
		if (StringUtils.isBlank(req.getTableType())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "9"));
		}
		if (StringUtils.isBlank(req.getRequestNo())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "10"));
		}
//		if (StringUtils.isBlank(req.getTransactionNo())) {
//			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "12"));
//		}
//		if (StringUtils.isBlank(req.getTransDropDownVal())) {
//			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "13"));
//		}
		return list;
	}

	public List<ErrorCheck> validateXolPremium(PremiumInsertMethodReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val=new Validation();
		boolean flag=false;
		boolean cashLossCrFlag=false;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());	
		try {
		List<Map<String, Object>> retroList = xolPreImpl.getSPRetroList(bean.getContNo());
			 if(retroList!=null&&retroList.size()>0)
			 {
				 Map<String,Object>	 map=retroList.get(0);			
				 bean.setProductId(bean.getProductId());						
				 if("Y".equalsIgnoreCase(bean.getSpRetro()))
				 {
					 List<Map<String, Object>> insList=xolPreImpl.getRetroContracts(bean.getProposalNo(),bean.getNoOfRetro());
					 if(insList!=null && insList.size()>0){
						 for(int i=0;i<insList.size();i++){
							 Map<String,Object> insMap=insList.get(i);
							 if("C".equals(insMap.get("TYPE")))
							 {
								 bean.setRetroContractNo(insMap.get("CONTRACT_NO").toString());
								 String string= xolPreImpl.getSumOfShareSign(bean.getContNo());
								 if(Double.parseDouble(string)!=100){
									list.add(new ErrorCheck(prop.getProperty("errors.retroNotCompleted")+bean.getRetroContractNo(),"retroNotCompleted","01"));
								 }
							 }							
						 }
					 }
				 }
			 }
			 		 bean.setOtherCost((bean.getOtherCost()).replaceAll(",",""));
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
					if("3".equalsIgnoreCase(bean.getProductId())){
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
						list.add(new ErrorCheck(prop.getProperty("errors.premium.statDate"),"premium","01")); 
						
					}
					if("edit".equalsIgnoreCase(bean.getMode()) && !"Temp".equalsIgnoreCase(bean.getTableType())){
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
						list.add(new ErrorCheck(prop.getProperty("errors.premium.amendDate"),"premium","01")); 
						
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
						list.add(new ErrorCheck(prop.getProperty("errors.transaction.invalid"),"","01")); 
					}else if("3".equalsIgnoreCase(bean.getProductId())&&dateflag && Validation.ValidateTwo(bean.getInceptionDate(),bean.getTransaction()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.transaction"),"","01")); 
					}else if("5".equalsIgnoreCase(bean.getProductId())&&dateflag && Validation.ValidateTwo(bean.getStatementDate(),bean.getTransaction()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.transaction.statement"),"transaction","01")); 
					}
					else if(dateflag && Validation.ValidateTwo(bean.getAcceptenceDate(),bean.getTransaction()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.premium.acDate")+bean.getAcceptenceDate(),"premium","01"));
					}
					else if(!"superuser".equalsIgnoreCase(bean.getUserType()))
					{
						String morRepMaxDate = xolPreImpl.getMovementReportMaxDate(bean.getBranchCode());
						if(StringUtils.isBlank(bean.getTransactionNo())&&StringUtils.isNotBlank(morRepMaxDate)&&Validation.ValidateTwo(morRepMaxDate,bean.getTransaction()).equalsIgnoreCase("invalid")){
							list.add(new ErrorCheck(prop.getProperty("errors.tranDate.invalid"),"tranDate","01")); 
						}
					}					 
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getTransaction()).equalsIgnoreCase("") && !"edit".equalsIgnoreCase(bean.getMode())){
						if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getTransaction())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
						}

					 if(val.isSelect(bean.getAccountPeriod()).equalsIgnoreCase(""))
					 {
						list.add(new ErrorCheck(prop.getProperty("errors.account_Period.required"),"AccountPeriod","01")); 
					 }else 
					 {
						 String[] ins=bean.getAccountPeriod().split("_");
						 if((!"AP".equalsIgnoreCase(ins[0]))&&(!"RP".equalsIgnoreCase(ins[0])) &&(!"RTP".equalsIgnoreCase(ins[0]))&& (!"RVP".equalsIgnoreCase(ins[0]))){
							
							 if(!bean.getCurrency().equals(bean.getBaseCurrencyId())){
								list.add(new ErrorCheck(prop.getProperty("errors.MandD.currencyInvalid")+bean.getBaseCurrencyName(),"BaseCurrencyName","01"));
							 }
							 if(!"2".equals(bean.getEnteringMode())){
								list.add(new ErrorCheck(prop.getProperty("errors.MandD.enteringModeInvalid")+bean.getShareSigned(),"EnteringMode","01"));
							 }
							 }								 	
						 	else if("RP".equalsIgnoreCase(ins[0]))
							 {
						 		if(bean.getRecuirementpremium().equals("0")|| bean.getRecuirementpremium().equalsIgnoreCase(""))
								 {
									list.add(new ErrorCheck(prop.getProperty("errors.Recuirement_premium.Zero"),"Recuirementpremium","01")); 
								 }
						 		else if( ! val.isNull(bean.getRecuirementpremium()).equalsIgnoreCase("") )
								 {
									 flag=true;
									 bean.setRecuirementpremium((bean.getRecuirementpremium()).replaceAll(",",""));
									 if(val.numbervalid(bean.getRecuirementpremium()).equalsIgnoreCase("INVALID"))
									 {
										list.add(new ErrorCheck(prop.getProperty("errors.Recuirement_premium.check"),"Recuirement_premium","01"));   
									 }else{
										 String rpval = xolPreImpl.getRPPremiumOC(bean.getContNo(),bean.getLayerno(),bean.getProductId());
										 if(Double.parseDouble(bean.getRecuirementpremium())<0){
										 if(Double.parseDouble(rpval)<(Double.parseDouble(bean.getRecuirementpremium())*(-1))){
											list.add(new ErrorCheck(prop.getProperty("errors.rppreimium.error"),"rppreimium","01")); 
										 }
									 }
								 	 }
								 }
							 }
					 }
					if(StringUtils.isNotBlank(bean.getAccountPeriod()) && "AP".equalsIgnoreCase(bean.getAccountPeriod()) && "RI02".equalsIgnoreCase(bean.getSourceId()))	{
						if(StringUtils.isBlank(bean.getGnpiDate())){
							list.add(new ErrorCheck(prop.getProperty("error.gnpi.value"),"gnpi","01")); 
						}
					}
					 if("3".equalsIgnoreCase(bean.getProductId())){
					 if(!val.isNull(bean.getCommission()).equalsIgnoreCase(""))
					 {
						 flag=true;
					 	 if(val.numbervalid(bean.getCommission()).equalsIgnoreCase("INVALID"))
						 {
					 		list.add(new ErrorCheck(prop.getProperty("errors.commission.Check"),"commission","01")); 
						 }				  
					 }		
					 
					 if(!val.isNull(bean.getBrokerage()).equalsIgnoreCase(""))
					 {
						 flag=true;
						 bean.setBrokerage((bean.getBrokerage()).replaceAll(",",""));
					 	 if(val.numbervalid(bean.getBrokerage().toString()).equalsIgnoreCase("INVALID"))
						 {
					 		list.add(new ErrorCheck(prop.getProperty("errors.brokerage.check"),"brokerage","01")); 
						 }
					 } 
					 }
					 if(!val.isNull(bean.getTax()).equalsIgnoreCase(""))
					 {
						 flag=true;
						 bean.setTax((bean.getTax()).replaceAll(",",""));
						 if(val.numbervalid(bean.getTax().toString()).equalsIgnoreCase("INVALID"))
						 {
							list.add(new ErrorCheck(prop.getProperty("errors.tax.check"),"tax","01")); 
						 }
					 }
					 if("3".equalsIgnoreCase(bean.getProductId())){
				        if (!val.isNull(bean.getWithHoldingTaxOC()).equalsIgnoreCase("")) {
												 flag=true;
				            bean.setWithHoldingTaxOC((bean.getWithHoldingTaxOC()).replaceAll(",", ""));
				            if (val.numbervalid(bean.getWithHoldingTaxOC()).equalsIgnoreCase("INVALID")) {
				               list.add(new ErrorCheck(prop.getProperty("errors.whtax.check"),"whtax","01")); 
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
				        if(StringUtils.isBlank(bean.getTaxDedectSource())){
							list.add(new ErrorCheck(prop.getProperty("taxdedct.source.invalid"),"TaxDedectSource","01")); 
						 }
						 else{
							 bean.setTaxDedectSource(bean.getTaxDedectSource().replaceAll(",", ""));
							 if(val.numbervalid(bean.getTaxDedectSource()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("error.taxdedect.number"),"taxdedect","01"));
							 }
						 }
					 }
					 }
				        if (!val.isNull(bean.getMdpremium()).equalsIgnoreCase("")) {
				            flag = true;
						 bean.setMdpremium((bean.getMdpremium()).replaceAll(",",""));
						 if(val.numbervalid(bean.getMdpremium()).equalsIgnoreCase("INVALID"))
						 {
							list.add(new ErrorCheck(prop.getProperty("errors.mdpremium.check"),"mdpremium","01")); 
						 }
					 }
				
					 if(! val.isNull(bean.getAdjustmentpremium()).equalsIgnoreCase(""))
					 {
						 flag=true;
						 bean.setAdjustmentpremium((bean.getAdjustmentpremium()).replaceAll(",",""));
						 if(val.numbervalid(bean.getAdjustmentpremium()).equalsIgnoreCase("INVALID"))
						 {
							list.add(new ErrorCheck(prop.getProperty("errors.adjustpremium.check"),"adjustpremium","01")); 
						 }else if(! val.isNull(bean.getAdjustmentpremiumtemp()).equalsIgnoreCase("")){
							 if(Double.parseDouble(bean.getAdjustmentpremium())>Double.parseDouble(bean.getAdjustmentpremiumtemp()))
								 {
								 }		
						 }
					}
					 if (StringUtils.isNotBlank(bean.getMdpremium()) && StringUtils.isNotBlank(bean.getEPIourshareview())) {
							bean.setMdpremium((bean.getMdpremium()).replaceAll(",", ""));
							bean.setAdjustmentpremium((StringUtils.isBlank(bean.getAdjustmentpremium())?"":bean.getAdjustmentpremium()).replaceAll(",", ""));
							bean.setEPIourshareview((bean.getEPIourshareview()).replaceAll(",", ""));
							if (!val.isValidNo(bean.getMdpremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(bean.getAdjustmentpremium().trim()).equalsIgnoreCase("INVALID") && !val.isValidNo(bean.getEPIourshareview().trim()).equalsIgnoreCase("INVALID")) {
								if(StringUtils.isBlank(bean.getAdjustmentpremium())){
									bean.setAdjustmentpremium("0");
								}
					 final String Mdpremium = val.isNull(bean.getMdpremium());
					 final String AdjPremium = val.isNull(bean.getAdjustmentpremium());
					 final Double sumMdadjust=Double.parseDouble(Mdpremium)+Double.parseDouble(AdjPremium);
					 
							}
					 }
					bean.setTotalCredit((bean.getTotalCredit()).replaceAll(",",""));
					bean.setTotalDebit((bean.getTotalDebit()).replaceAll(",",""));
					if(flag==false)
					{
						list.add(new ErrorCheck(prop.getProperty("errors.currency.select"),"currency","01"));  
					}	
					if("transEdit".equalsIgnoreCase(bean.getMode())){
						if(StringUtils.isBlank(bean.getTransDropDownVal()) && "Yes".equalsIgnoreCase(bean.getChooseTransaction())){
							list.add(new ErrorCheck(prop.getProperty("resersel.trans"),"TransDropDownVal","01")); 
						}
					}}catch(Exception e) {
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
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
//		if (StringUtils.isBlank(req.getRequestNo())) {
//			list.add(new ErrorCheck("Please Enter RequestNo", "RequestNo", "6"));
//		}
//		if (StringUtils.isBlank(req.getTableType())) {
//			list.add(new ErrorCheck("Please Enter TableType", "TableType", "7"));
//		}
		return list;
	
	}

	public List<ErrorCheck> getAdjPremiumVali(GetAdjPremiumReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
