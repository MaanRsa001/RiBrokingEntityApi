package com.maan.insurance.validation.retroManualAdj;

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
import com.maan.insurance.model.req.retroManualAdj.InsertPremiumReq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.journal.JournalServiceImple;
import com.maan.insurance.service.impl.retroManualAdj.RetroManualAdjServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@Service
public class RetroManualAdjValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(RetroManualAdjValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private RetroManualAdjServiceImple imple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public RetroManualAdjValidation() {
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

	public List<ErrorCheck> validateProportionPremium(InsertPremiumReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val=new Validation();
		boolean flag=false;
		boolean cashLossCrFlag=false;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
		try {
			 		 bean.setOtherCost((bean.getOtherCost()).replaceAll(",",""));
			 		  
				 	 if(val.isNull(bean.getCurrency()).equalsIgnoreCase("0"))
					 {						 
				 		list.add(new ErrorCheck(prop.getProperty("errors.currency.requireds"),"currency","01"));							
					 }
					 if(val.isNull(bean.getExchRate()).equalsIgnoreCase(""))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.exchRate.required"),"exchRate","01"));
					 }
					boolean dateflag=true; 
					boolean statDate=true;
					
					
				
					if(StringUtils.isNotBlank(bean.getTransactionNo())){
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
					}
						if(StringUtils.isNotBlank(bean.getMaxDate()) && !(null ==bean.getMaxDate())){
						 if(Validation.ValidateTwo(bean.getMaxDate(),bean.getAmendmentDate()).equalsIgnoreCase("invalid"))
						{
							list.add(new ErrorCheck(prop.getProperty("errors.premium.amendDate"),"amendmentDate","01"));
							
						}
							}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getAmendmentDate()).equalsIgnoreCase("")){
						if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getAmendmentDate())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans.amend")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
					}
					}
					if(StringUtils.isBlank(bean.getPredepartment())){
						list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"departId","01"));
					}
					if(StringUtils.isBlank(bean.getSubProfitId())){
						list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitId","01"));
					}else{
						bean.setSubProfitId((bean.getSubProfitId()).replaceAll(" ", ""));
					}
					if(val.isNull(bean.getTransaction()).equalsIgnoreCase(""))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.transaction.required"),"transaction","01"));			
					}
					else if(val.checkDate(bean.getTransaction()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.transaction.invalid"),"transaction","01"));
					}if(StringUtils.isBlank(bean.getBusinessType())){
						list.add(new ErrorCheck(prop.getProperty("Please Enter Business Type"),"BusinessType","01"));
					}
								 
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getTransaction()).equalsIgnoreCase("") && !"edit".equalsIgnoreCase(bean.getMode())){
						if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getTransaction())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
						}
					 if(!val.isNull(bean.getClaimspaid()).equalsIgnoreCase(""))
					 {
						 flag=true;
						 bean.setClaimspaid((bean.getClaimspaid()).replaceAll(",",""));
						 if(val.numbervalid(bean.getClaimspaid().toString()).equalsIgnoreCase("INVALID"))
						 {
							 list.add(new ErrorCheck(prop.getProperty("errors.claims_paid.Check"),"claims_paid","01"));			 
						 }
								
					 }
				
				 if(!val.isNull(bean.getPremiumQuotaShare()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setPremiumQuotaShare((bean.getPremiumQuotaShare()).replaceAll(",",""));
					 if(val.numbervalid(bean.getPremiumQuotaShare()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.Error"),"premium","01")); 
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
			 	 if(!val.isNull(bean.getBrokerage()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setBrokerage((bean.getBrokerage()).replaceAll(",",""));
					 if(val.numbervalid(bean.getBrokerage().toString()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.brokerage.check"),"brokerage","01"));
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
            if(!val.isNull(bean.getWithHoldingTaxOC()).equalsIgnoreCase(""))
            {
                flag=true;
                bean.setWithHoldingTaxOC((bean.getWithHoldingTaxOC()).replaceAll(",", ""));
                if(val.numbervalid(bean.getWithHoldingTaxOC()).equalsIgnoreCase("INVALID"))
                {
                    list.add(new ErrorCheck(prop.getProperty("errors.whtax.check"),"whtax","01"));
                }
            }
            if("RI02".equalsIgnoreCase(bean.getSourceId())){
            if(StringUtils.isBlank(bean.getTaxDedectSource())){
				 list.add(new ErrorCheck(prop.getProperty("taxdedct.source.invalid"),"source","01"));
   			 }
   			 else{
   				 bean.setTaxDedectSource(bean.getTaxDedectSource().replaceAll(",", ""));
   				if(val.numbervalid(bean.getTaxDedectSource()).equalsIgnoreCase("INVALID"))
				 {
					 list.add(new ErrorCheck(prop.getProperty("error.taxdedect.number"),"taxdedect","01"));
				 }
   			 }
            }
			 if(!val.isNull(bean.getOverrider()).equalsIgnoreCase(""))
			 {
				 flag=true;
				 bean.setOverrider((bean.getOverrider()).replaceAll(",",""));
				 if(val.numbervalid(bean.getOverrider().toString()).equalsIgnoreCase("INVALID"))
				 {
					 list.add(new ErrorCheck(prop.getProperty("errors.overrider.check"),"overrider","01"));
				 }
			 }
				 if(!val.isNull(bean.getPremiumportifolioIn()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setPremiumportifolioIn((bean.getPremiumportifolioIn()).replaceAll(",",""));
					 if(val.numbervalid(bean.getPremiumportifolioIn()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.PremiumPortfolioIn.Error"),"PremiumPortfolioIn","01")); 
					 }
				 }
				if(!val.isNull(bean.getSlideScaleCom()).equalsIgnoreCase(""))
				{
					flag=true;
					bean.setSlideScaleCom((bean.getSlideScaleCom()).replaceAll(",",""));
					if(val.numbervalid(bean.getSlideScaleCom()).equalsIgnoreCase("INVALID"))
					{
						list.add(new ErrorCheck(prop.getProperty("errors.slidescale.Error"),"slidescale","01"));
					}
				}
				 if(!val.isNull(bean.getCliamPortfolioin()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setCliamPortfolioin((bean.getCliamPortfolioin()).replaceAll(",",""));
					 if(val.numbervalid(bean.getCliamPortfolioin()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.ClaimPortfolioIn.Error"),"ClaimPortfolioIn","01")); 
					 }
				 }
			 	 if(!val.isNull(bean.getPremiumportifolioout()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setPremiumportifolioout((bean.getPremiumportifolioout()).replaceAll(",",""));
					 if(val.numbervalid(bean.getPremiumportifolioout()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.PremiumPortfolioOut.Error"),"PremiumPortfolioOut","01")); 
					 }
				 }
			 	 if(!val.isNull(bean.getLossReserveReleased()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setLossReserveReleased((bean.getLossReserveReleased()).replaceAll(",",""));
					 if(val.numbervalid(bean.getLossReserveReleased()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.LossReserveReleased.Error"),"LossReserveReleased","01")); 
					 }
					 
				 }
			 	 if(!val.isNull(bean.getInterest()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setInterest((bean.getInterest()).replaceAll(",",""));
					 if(val.numbervalid(bean.getInterest()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.Premium.intrest.invalid"),"intrest","01"));
					 }
			 	 }
			 	 if(!val.isNull(bean.getPremiumReserveQuotaShare()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setPremiumReserveQuotaShare((bean.getPremiumReserveQuotaShare()).replaceAll(",",""));
					 if(val.numbervalid(bean.getPremiumReserveQuotaShare()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.PremiumReserveQuotaShare1.Error"),"PremiumReserveQuotaShare1","01"));
					 }else if(Double.parseDouble(bean.getPremiumReserveQuotaShare())<0){
						 list.add(new ErrorCheck(prop.getProperty("errors.PremiumReserveQuotaShare1.less"),"PremiumReserveQuotaShare1","01"));
					 }
			 	 }
			 	 
				
				 if(StringUtils.isBlank(bean.getCashLossCredit())){
					 list.add(new ErrorCheck(prop.getProperty("errors.CashLossCredit.reqired"),"CashLossCredit","01"));
				 }else {
					 bean.setCashLossCredit(bean.getCashLossCredit().replaceAll(",", ""));
					 if(val.numbervalid(bean.getCashLossCredit()).equalsIgnoreCase("INVALID"))
						 {
							 list.add(new ErrorCheck(prop.getProperty("errors.CashLossCredit.Error"),"CashLossCredit","01")); 
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
					 if(StringUtils.isBlank(bean.getLossParticipation())){
							list.add(new ErrorCheck(prop.getProperty("lossParticipation.empty"),"lossParticipation","01"));
						 }
						 else{
							 bean.setLossParticipation(bean.getLossParticipation().replaceAll(",", ""));
							 if(val.numbervalid(bean.getLossParticipation()).equalsIgnoreCase("INVALID"))
							 {
								 list.add(new ErrorCheck(prop.getProperty("error.lossParticipation.number"),"lossParticipation","01"));
							 }
						 }
					
			 	 if(!val.isNull(bean.getLossReserveRetained()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setLossReserveRetained((bean.getLossReserveRetained()).replaceAll(",",""));
					 if(val.numbervalid(bean.getLossReserveRetained()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.LossReserveRetained.Error"),"LossReserveRetained","01")); 
					 }else if(Double.parseDouble(bean.getLossReserveRetained())<0){
						 list.add(new ErrorCheck(prop.getProperty("errors.LossReserveRetained.Error.less"),"LossReserveRetained","01"));
					 }
					 
				 }
				  
				 if(!val.isNull(bean.getProfitCommission()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setProfitCommission((bean.getProfitCommission()).replaceAll(",",""));
					 if(val.numbervalid(bean.getProfitCommission()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.ProfitCommission.Error"),"ProfitCommission","01")); 
					 }
				 }
			 if(!val.isNull(bean.getCashLossPaid()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setCashLossPaid((bean.getCashLossPaid()).replaceAll(",",""));
					 if(val.numbervalid(bean.getCashLossPaid()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.CashLossPaid1.Error"),"CashLossPaid1","01"));
					 }
					
				 }
			   if(! val.isNull(bean.getXlCost()).equalsIgnoreCase(""))
				 {
					 bean.setXlCost((bean.getXlCost()).replaceAll(",",""));
					 if(val.numbervalid(bean.getXlCost()).equalsIgnoreCase("Invalid"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("Premium.Xlcost.Invalid"),"Xlcost","01"));
					 }
				 }
				 
			   if(! val.isNull(bean.getCliamportfolioout()).equalsIgnoreCase(""))
				 {
					 bean.setCliamportfolioout((bean.getCliamportfolioout()).replaceAll(",",""));
					 if(val.numbervalid(bean.getCliamportfolioout()).equalsIgnoreCase("Invalid"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("Premium.CliamPortpoliIn.Invalid"),"CliamPortpoliIn","01"));
					 }
				 }
				 
				 if(! val.isNull(bean.getPremiumReserveReleased()).equalsIgnoreCase(""))
				 {
					 bean.setPremiumReserveReleased((bean.getPremiumReserveReleased()).replaceAll(",",""));
					 if(val.numbervalid(bean.getPremiumReserveReleased()).equalsIgnoreCase("Invalid"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("PremiumReserveRealsed.Required.error"),"PremiumReserveRealsed","01"));
					 }
				 }
				 if(!val.isNull(bean.getOtherCost()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setOtherCost((bean.getOtherCost()).replaceAll(",",""));
					 if(val.numbervalid(bean.getOtherCost()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"),"othercost","01")); 
					 }
				 }
				 if(!val.isNull(bean.getOsClaimsLossUpdateOC()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 bean.setOsClaimsLossUpdateOC((bean.getOsClaimsLossUpdateOC()).replaceAll(",",""));
					 if(val.numbervalid(bean.getOsClaimsLossUpdateOC()).equalsIgnoreCase("INVALID"))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.OSClaimLossUpdate.invalid"),"OSClaimLossUpdate","01")); 
					 }
				 }
					bean.setTotalCredit((bean.getTotalCredit()).replaceAll(",",""));
					bean.setTotalDebit((bean.getTotalDebit()).replaceAll(",",""));
					if(flag==false)
					{
						list.add(new ErrorCheck(prop.getProperty("errors.currency.select"),"currency","01"));	 
					}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
