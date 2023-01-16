package com.maan.insurance.validation.premium;

import java.io.InputStream;
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
import com.maan.insurance.jpa.service.impl.PropPremiumJpaServiceImpl;
import com.maan.insurance.model.req.premium.CashLossmailTriggerReq;
import com.maan.insurance.model.req.premium.ClaimTableListReq;
import com.maan.insurance.model.req.premium.ContractDetailsReq;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq;
import com.maan.insurance.model.req.premium.GetCassLossCreditReq;
import com.maan.insurance.model.req.premium.GetConstantPeriodDropDownReq;
import com.maan.insurance.model.req.premium.GetPreListReq;
import com.maan.insurance.model.req.premium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.premium.GetSPRetroListReq;
import com.maan.insurance.model.req.premium.GetVatInfoReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.premium.InsertReverseCashLossCreditReq;
import com.maan.insurance.model.req.premium.PremiumEditReq;
import com.maan.insurance.model.req.premium.PremiumUpdateMethodReq;
import com.maan.insurance.model.req.premium.PremiumUpdateMethodRiReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq1;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.premium.GetClaimNosDropDownRes;
import com.maan.insurance.model.res.premium.GetDepartmentNoRes;
import com.maan.insurance.model.res.premium.GetOSBListRes;
import com.maan.insurance.model.res.premium.GetRetroContractsRes;
import com.maan.insurance.model.res.premium.GetSPRetroListRes;
import com.maan.insurance.model.res.premium.GetSPRetroListRes1;
import com.maan.insurance.model.res.premium.GetSumOfShareSignRes;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.Validation;


@Service
public class PropPremiumValidation {
	private Logger log = LogManager.getLogger(PropPremiumValidation.class);
	private Properties prop = new Properties();

	@Autowired
	private PropPremiumJpaServiceImpl premiumImpl;
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropDownImple;
	
	@Autowired
	private CommonCalculation calcu;
	
 public PropPremiumValidation() {
		
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

	public List<ErrorCheck> getPremiumedListVali(GetPremiumedListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
		if(StringUtils.isBlank(req.getSectionNo())) {
			list.add(new ErrorCheck("Please Enter SectionNo","DepartmentId", "03"));
			}
		if(StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type","Type", "04"));
			}
		return list;
	}

	public List<ErrorCheck> getPreListVali(GetPreListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId","DepartmentId", "01"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
		return list;
	}

	public List<ErrorCheck> getConstantPeriodDropDownVali(GetConstantPeriodDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getCategoryId())) {
			list.add(new ErrorCheck("Please Enter CategoryId","CategoryId", "01"));
			}
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
			/*
			 * if(StringUtils.isBlank(req.getDepartmentId())) { list.add(new
			 * ErrorCheck("Please Enter DepartmentId","DepartmentId", "03")); }
			 */
		if(StringUtils.isBlank(req.getSectionNo())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter SectionNo"),"DepartmentId", "03"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo","ProposalNo", "04"));
			}
		return list;
	}



	public List<ErrorCheck> contractDetailsVali(ContractDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
		if(StringUtils.isBlank(req.getSectionNo())) {
			list.add(new ErrorCheck("Please Enter SectionNo","SectionNo", "03"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			//list.add(new ErrorCheck("Please Enter ProposalNo","ProposalNo", "04"));
			}
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId","ProductId", "05"));
			}
		return list;
	}

	public List<ErrorCheck> claimTableListMode1Vali(ClaimTableListReq req) {

		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck("Please Enter PolicyContractNo", "PolicyContractNo", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "4"));
		}
		return list;
	
	}

	public List<ErrorCheck> getSPRetroListVali(GetSPRetroListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter PolicyContractNo", "PolicyContractNo", "1"));
		}
//		if (StringUtils.isBlank(req.getProductId())) {
//			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
//		}
//		if (StringUtils.isBlank(req.getLayerNo())) {
//			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
//		}
		return list;
	}

	public List<ErrorCheck> insertPremiumVali(InsertPremiumReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		GetClaimNosDropDownRes ClaimNos= premiumImpl.getClaimNosDropDown(req.getContNo());
		GetClaimNosDropDownRes response = new GetClaimNosDropDownRes();
	
		GetSPRetroListReq req2 = new GetSPRetroListReq();
		final Validation val=new Validation();
		boolean flag=false;
		boolean cashLossCrFlag=false;
		String spRetro="";
		String noOfRetro="";
		String proposalNo="";
		String retroContractNo="";
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
//		if(StringUtils.isBlank(req.getRiCession())){
//			req.setRiCession("Yes");
//		}
		try {
		if(StringUtils.isBlank(req.getRiCession())){
			list.add(new ErrorCheck(prop.getProperty("errors.ricession.required"),"RiCession","01"));
		}
		else if("Yes".equalsIgnoreCase(req.getRiCession())){
		GetSPRetroListRes retroList=premiumImpl.getSPRetroList(req2);//req.getContNo(), req.getProductId(), req.getLayerno()
			 if(retroList!=null&&retroList.getCommonResponse().size()>0)
			 {
				 Map map=(Map) retroList.getCommonResponse().get(0);				 
			//	 req.setProductId(req.getProductId());
				 spRetro= (map.get("RSK_SP_RETRO")==null?"N":map.get("RSK_SP_RETRO").toString());
				 noOfRetro= (map.get("RSK_NO_OF_INSURERS")==null?"0":map.get("RSK_NO_OF_INSURERS").toString());
				 proposalNo= (map.get("RSK_PROPOSAL_NUMBER")==null?"0":map.get("RSK_PROPOSAL_NUMBER").toString());
				 if("Y".equalsIgnoreCase(spRetro))
				 {
					 GetRetroContractsRes insList=premiumImpl.getRetroContracts(proposalNo, noOfRetro);
					 if(insList!=null && insList.getCommonResponse().size()>0){
						 for(int i=0;i<insList.getCommonResponse().size();i++){
							 Map insMap=(Map)insList.getCommonResponse().get(i);
							 if("C".equals(insMap.get("TYPE")))
							 {
								 retroContractNo= (insMap.get("CONTRACT_NO").toString());
								 GetSumOfShareSignRes string= premiumImpl.getSumOfShareSign(retroContractNo);
								 if(Double.parseDouble(string.getCommonResponse())!=100){
									
									 list.add(new ErrorCheck(prop.getProperty("errors.retroNotCompleted")+retroContractNo,"retroContractNo","02"));
								 }
							 }							
						 }
					 }
				 }
			 }
		}
			 GetDepartmentNoRes deptId = premiumImpl.getDepartmentNo(retroContractNo);
			 if(!deptId.getCommonResponse().equalsIgnoreCase(req.getDepartmentId())){
				
			 }
			 		 req.setOtherCost((req.getOtherCost()).replaceAll(",",""));
			 		 if(val.isNull(req.getEnteringMode()).equalsIgnoreCase(""))
					 {
					 flag=true;
					list.add(new ErrorCheck(prop.getProperty("errors.mode.reqired"),"EnteringMode","03")); 
					 }	 
				 	 if(val.isNull(req.getCurrency()).equalsIgnoreCase("0"))
					 {						 
				 		list.add(new ErrorCheck(prop.getProperty("errors.currency.requireds"),"Currency","04"));							
					 }
					 if(val.isNull(req.getExchRate()).equalsIgnoreCase(""))
					 {
						 list.add(new ErrorCheck(prop.getProperty("errors.exchRate.required"),"ExchRate","05"));
					 }
					 if(StringUtils.isBlank(req.getRiCession())){
						 list.add(new ErrorCheck(prop.getProperty("errors.ricession.required"),"RiCession","06"));
						}
					boolean dateflag=true; 
					boolean statDate=true;
					if(val.isNull(req.getInceptionDate()).equalsIgnoreCase(""))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.statRecDate.required"),"InceptionDate","07"));
							dateflag=false;
							
					}
					else if(val.checkDate(req.getInceptionDate()).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.statRecDate.invalid"),"InceptionDate","07"));
							dateflag=false;
							
					}
					
					
					if(val.isNull(req.getStatementDate()).equalsIgnoreCase(""))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.statDate.required"),"StatementDate","08"));
							dateflag=false;
							statDate=false;
					}
					else if(val.checkDate(req.getStatementDate()).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.statDate.invalid"),"StatementDate","08"));
							dateflag=false;
							statDate=false;
					}else if(Validation.ValidateTwo(req.getInsDate(),req.getStatementDate()).equalsIgnoreCase("invalid"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.statDate"),"InsDate,StatementDate","09"));
						
					}else if(Validation.ValidateTwo(req.getStatementDate(),req.getInceptionDate()).equalsIgnoreCase("invalid"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.statRecDate"),"StatementDate,InceptionDate","10"));
						
					}
					if("edit".equalsIgnoreCase(req.getMode()) && !"Temp".equalsIgnoreCase(req.getTableType())){
					if(val.isNull(req.getAmendmentDate()).equalsIgnoreCase(""))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.amendmentDate.required"),"AmendmentDate","11"));
							dateflag=false;
							statDate=false;
					}
					else if(val.checkDate(req.getAmendmentDate()).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.amendmentDate.invalid"),"AmendmentDate","11"));
							dateflag=false;
							statDate=false;
					}else if(StringUtils.isNotBlank(req.getMaxDate()) && Validation.ValidateTwo(req.getMaxDate(),req.getAmendmentDate()).equalsIgnoreCase("invalid"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.amendDate"),"MaxDate,AmendmentDate","12"));
						
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAmendmentDate()).equalsIgnoreCase("")){
						if(dropDownImple.Validatethree(req.getBranchCode(), req.getAmendmentDate())==0){
							 list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans.amend")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
					}
					}
					if(StringUtils.isBlank(req.getSectionType())){
						 list.add(new ErrorCheck(prop.getProperty("error.enter.sectionType"),"SectionType","01"));	
					}else if(!"2".equals(req.getSectionType())){
						 list.add(new ErrorCheck(prop.getProperty("error.enter.sectionType.invalid"),"SectionType","01"));	
					}
					if(StringUtils.isNotBlank(req.getSectionName()) && "-1".equals(req.getSectionName())){
						 list.add(new ErrorCheck(prop.getProperty("error.enter.sectionName"),"sectionName","01"));	
					}
					if(StringUtils.isBlank(req.getPredepartment())){
						 list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"Predepartment","01"));
					}
					if(StringUtils.isBlank(req.getSubProfitId())){
						 list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitId","01"));
					}else{
						req.setSubProfitId((req.getSubProfitId()).replaceAll(" ", ""));
					}
					if(val.isNull(req.getTransaction()).equalsIgnoreCase(""))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.transaction.required"),"transaction","01"));			
					}
					else if(val.checkDate(req.getTransaction()).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.transaction.invalid"),"transaction","01"));
					}else if(dateflag && Validation.ValidateTwo(req.getInceptionDate(),req.getTransaction()).equalsIgnoreCase("invalid"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.transaction"),"InceptionDate,transaction","01"));
					}/*else if(dateflag && Validation.ValidateTwo(req.getAcceptenceDate(),req.getTransaction()).equalsIgnoreCase("invalid"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.premium.acDate")+req.getAcceptenceDate(),"AcceptenceDate,transaction","01"));
					}*/
							 
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(req.getTransaction()).equalsIgnoreCase("") && !"edit".equalsIgnoreCase(req.getMode())){
						if(dropDownImple.Validatethree(req.getBranchCode(), req.getTransaction())==0){
							 list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.trans")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
						}
					  if("0".equals(req.getAccountPeriodyear())||"0".equals(req.getAccountPeriod()))
					 {
						   list.add(new ErrorCheck(prop.getProperty("errors.account_Period_year.required"),"AccountPeriodyear","01"));
					 }else if(statDate)
					 {
						
						 
						if(req.getAccountPeriodDate().length()>0)
						{
						
							if(Validation.ValidateTwo(req.getAccountPeriodDate(),req.getStatementDate()).equalsIgnoreCase("invalid")){
								 list.add(new ErrorCheck(prop.getProperty("errors.premium.statRecDateGrAccPer"),"StatementDate","01"));
							}
						}
						
					 }
					 if(!val.isNull(req.getReceiptno()).equalsIgnoreCase(""))
					 {
						 flag=true;
					  
						 if(val.numbervalid(req.getReceiptno()).equalsIgnoreCase("INVALID"))
						 {
							  list.add(new ErrorCheck(prop.getProperty("errors.receipt_no.Check"),"receipt_no","01"));			 
						 }
								
					 }			  
					 if(!val.isNull(req.getSettlementstatus()).equalsIgnoreCase(""))
					 {
						
								
					 }			
					 if(!val.isNull(req.getClaimspaid()).equalsIgnoreCase(""))
					 {
						 flag=true;
					
						 req.setClaimspaid((req.getClaimspaid()).replaceAll(",",""));
						 if(val.numbervalid(req.getClaimspaid().toString()).equalsIgnoreCase("INVALID"))
						 {
							  list.add(new ErrorCheck(prop.getProperty("errors.claims_paid.Check"),"claims_paid","01"));			 
						 }
								
					 }
					 
				 if(val.isNull(req.getPremiumQuotaShare()).equalsIgnoreCase("") && val.isNull(req.getPremiumSurplus()).equalsIgnoreCase("") && val.isNull(req.getPremiumportifolioIn()).equalsIgnoreCase("") && val.isNull(req.getPremiumportifolioout()).equalsIgnoreCase("") && val.isNull(req.getPremiumReserveReleased()).equalsIgnoreCase("") && val.isNull(req.getLossReserveReleased()).equalsIgnoreCase(""))	 
				 {
					  list.add(new ErrorCheck(prop.getProperty("enter.anyone.value"),"PremiumQuotaShare","01"));
				 }
			
				 if(!val.isNull(req.getPremiumQuotaShare()).equalsIgnoreCase(""))
				 {
					 flag=true;
				
					 req.setPremiumQuotaShare((req.getPremiumQuotaShare()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumQuotaShare()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.premium.Error"),"PremiumQuotaShare","01")); 
					 }
					
				 }	
			 	 if(!val.isNull(req.getCommissionQuotaShare()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setCommissionQuotaShare((req.getCommissionQuotaShare()).replaceAll(",",""));
					 if(val.numbervalid(req.getCommissionQuotaShare()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.commission.Check"),"CommissionQuotaShare","01"));
					 }
					  
				 }
			 	 if(!val.isNull(req.getBrokerage()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setBrokerage((req.getBrokerage()).replaceAll(",",""));
					 if(val.numbervalid(req.getBrokerage().toString()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.brokerage.check"),"brokerage","01"));
					 }
				 }
			 if(!val.isNull(req.getTax()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setTax((req.getTax()).replaceAll(",",""));
					 if(val.numbervalid(req.getTax().toString()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.tax.check"),"tax","01"));
					 }
				 }
            if(!val.isNull(req.getWithHoldingTaxOC()).equalsIgnoreCase(""))
            {
                flag=true;
                req.setWithHoldingTaxOC((req.getWithHoldingTaxOC()).replaceAll(",", ""));
                if(val.numbervalid(req.getWithHoldingTaxOC()).equalsIgnoreCase("INVALID"))
                {
                     list.add(new ErrorCheck(prop.getProperty("errors.whtax.check"),"WithHoldingTaxOC","01"));
                }
            }
            if("RI02".equalsIgnoreCase(req.getSourceId())){
            if(StringUtils.isBlank(req.getTaxDedectSource())){
				  list.add(new ErrorCheck(prop.getProperty("taxdedct.source.invalid"),"TaxDedectSource","01"));
   			 }
   			 else{
   				 req.setTaxDedectSource(req.getTaxDedectSource().replaceAll(",", ""));
   				if(val.numbervalid(req.getTaxDedectSource()).equalsIgnoreCase("INVALID"))
				 {
   					list.add(new ErrorCheck(prop.getProperty("error.taxdedect.number"),"TaxDedectSource","01"));
				 }
   			 }
            }
			 if(!val.isNull(req.getOverrider()).equalsIgnoreCase(""))
			 {
				 flag=true;
				 req.setOverrider((req.getOverrider()).replaceAll(",",""));
				 if(val.numbervalid(req.getOverrider().toString()).equalsIgnoreCase("INVALID"))
				 {
					  list.add(new ErrorCheck(prop.getProperty("errors.overrider.check"),"Overrider","01"));
				 }
			 }
				 
			 if(!val.isNull(req.getPremiumSurplus()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setPremiumSurplus((req.getPremiumSurplus()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumSurplus()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.premiumsurplus.Error"),"PremiumSurplus","01")); 
					 }
				 }
				 		 
				 if(!val.isNull(req.getCommissionSurplus()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setCommissionSurplus((req.getCommissionSurplus()).replaceAll(",",""));
					 if(val.numbervalid(req.getCommissionSurplus()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.CommissionSurplus.Error"),"CommissionSurplus","01")); 
					 }
				 }
				 if(!val.isNull(req.getPremiumportifolioIn()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setPremiumportifolioIn((req.getPremiumportifolioIn()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumportifolioIn()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.PremiumPortfolioIn.Error"),"PremiumportifolioIn","01")); 
					 }
					
				 }
				if(!val.isNull(req.getSlideScaleCom()).equalsIgnoreCase(""))
				{
					flag=true;
					req.setSlideScaleCom((req.getSlideScaleCom()).replaceAll(",",""));
					if(val.numbervalid(req.getSlideScaleCom()).equalsIgnoreCase("INVALID"))
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.slidescale.Error"),"slidescale","01"));
					}
				
				}
				 if(!val.isNull(req.getCliamPortfolioin()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setCliamPortfolioin((req.getCliamPortfolioin()).replaceAll(",",""));
					 if(val.numbervalid(req.getCliamPortfolioin()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.ClaimPortfolioIn.Error"),"ClaimPortfolioIn","01")); 
					 }
				
				 }
			 	 if(!val.isNull(req.getPremiumportifolioout()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setPremiumportifolioout((req.getPremiumportifolioout()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumportifolioout()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.PremiumPortfolioOut.Error"),"PremiumPortfolioOut","01")); 
					 }
				 }
			 	 if(!val.isNull(req.getLossReserveReleased()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setLossReserveReleased((req.getLossReserveReleased()).replaceAll(",",""));
					 if(val.numbervalid(req.getLossReserveReleased()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.LossReserveReleased.Error"),"LossReserveReleased","01")); 
					 }
					 
				 }
			 	 if(!val.isNull(req.getInterest()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setInterest((req.getInterest()).replaceAll(",",""));
					 if(val.numbervalid(req.getInterest()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.Premium.intrest.invalid"),"intrest","01"));
					 }
			 	 }
			 	 if(!val.isNull(req.getPremiumReserveQuotaShare()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setPremiumReserveQuotaShare((req.getPremiumReserveQuotaShare()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumReserveQuotaShare()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.PremiumReserveQuotaShare1.Error"),"PremiumReserveQuotaShare","01"));
					 }else if(Double.parseDouble(req.getPremiumReserveQuotaShare())<0){
						  //list.add(new ErrorCheck(prop.getProperty("errors.PremiumReserveQuotaShare1.less"),"PremiumReserveQuotaShare1","01"));
						 
					 }
			 	 }
				 if(!val.isNull(req.getCashLossCredit()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 cashLossCrFlag=true;
				System.out.println("Cash");;
					 req.setCashLossCredit((req.getCashLossCredit()).replaceAll(",",""));
					 if(val.numbervalid(req.getCashLossCredit()).equalsIgnoreCase("INVALID"))
					 {
						 cashLossCrFlag=false;
						  list.add(new ErrorCheck(prop.getProperty("errors.CashLossCredit.Error"),"CashLossCredit","01")); 
					 }else if(Double.parseDouble(req.getCashLossCredit())>0 && (CollectionUtils.isEmpty(ClaimNos.getCommonResponse()))){
						 cashLossCrFlag=false;
						  list.add(new ErrorCheck(prop.getProperty("errors.CashLossCredit.invalid"),"CashLossCredit","01"));
					 }
				 }
				 if("RI02".equalsIgnoreCase(req.getSourceId())){
					 if(StringUtils.isBlank(req.getVatPremium())){
						 list.add(new ErrorCheck(prop.getProperty("vatpremium.empty"),"servicetax","01")); 
					 }
					 else{
						 req.setVatPremium(req.getVatPremium().replaceAll(",", ""));
						 if(val.numbervalid(req.getVatPremium()).equalsIgnoreCase("INVALID"))
						 {
							  list.add(new ErrorCheck(prop.getProperty("error.vatpremium.number"),"servicetax","01"));
						 }
					 }
					 
				
					 if(StringUtils.isBlank(req.getBrokerageVat())){
						 list.add(new ErrorCheck(prop.getProperty("brokeragevat.empty"),"servicetax","01")); 
					 }
					 else{
						 req.setBrokerageVat(req.getBrokerageVat().replaceAll(",", ""));
						 if(val.numbervalid(req.getBrokerageVat()).equalsIgnoreCase("INVALID"))
						 {
							  list.add(new ErrorCheck(prop.getProperty("error.brokeragevat.number"),"servicetax","01"));
						 }
					 }
					}
					 if(StringUtils.isBlank(req.getLossParticipation())){
							 list.add(new ErrorCheck(prop.getProperty("lossParticipation.empty"),"lossParticipation","01"));
						 }
						 else{
							 req.setLossParticipation(req.getLossParticipation().replaceAll(",", ""));
							 if(val.numbervalid(req.getLossParticipation()).equalsIgnoreCase("INVALID"))
							 {
								  list.add(new ErrorCheck(prop.getProperty("error.lossParticipation.number"),"lossParticipation","01"));
							 }
						 }
				 
			 	 if(!val.isNull(req.getLossReserveRetained()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setLossReserveRetained((req.getLossReserveRetained()).replaceAll(",",""));
					 if(val.numbervalid(req.getLossReserveRetained()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.LossReserveRetained.Error"),"LossReserveRetained","01")); 
					 }else if(Double.parseDouble(req.getLossReserveRetained())<0){
						  //list.add(new ErrorCheck(prop.getProperty("errors.LossReserveRetained.Error.less"),"LossReserveRetained","01"));
					 }
					 
				 }
				  
				 if(!val.isNull(req.getProfitCommission()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setProfitCommission((req.getProfitCommission()).replaceAll(",",""));
					 if(val.numbervalid(req.getProfitCommission()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.ProfitCommission.Error"),"ProfitCommission","01")); 
					 }
				 }
			 if(!val.isNull(req.getCashLossPaid()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setCashLossPaid((req.getCashLossPaid()).replaceAll(",",""));
					 if(val.numbervalid(req.getCashLossPaid()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.CashLossPaid1.Error"),"CashLossPaid","01"));
					 }
				 }
				 
			   if(! val.isNull(req.getXlCost()).equalsIgnoreCase(""))
				 {
					 req.setXlCost((req.getXlCost()).replaceAll(",",""));
					 if(val.numbervalid(req.getXlCost()).equalsIgnoreCase("Invalid"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("Premium.Xlcost.Invalid"),"Xlcost","01"));
					 }
				 }
				 
			   if(! val.isNull(req.getCliamportfolioout()).equalsIgnoreCase(""))
				 {
					 req.setCliamportfolioout((req.getCliamportfolioout()).replaceAll(",",""));
					 if(val.numbervalid(req.getCliamportfolioout()).equalsIgnoreCase("Invalid"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("Premium.CliamPortpoliIn.Invalid"),"CliamPortpoliIn","01"));
					 }
				 }
				 if(! val.isNull(req.getPremiumReserveReleased()).equalsIgnoreCase(""))
				 {
					 req.setPremiumReserveReleased((req.getPremiumReserveReleased()).replaceAll(",",""));
					 if(val.numbervalid(req.getPremiumReserveReleased()).equalsIgnoreCase("Invalid"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("PremiumReserveRealsed.Required.error"),"PremiumReserveRealsed","01"));
					 }
				 }
				 if(!val.isNull(req.getOtherCost()).equalsIgnoreCase(""))
				 {
					 flag=true;
					 req.setOtherCost((req.getOtherCost()).replaceAll(",",""));
					 if(val.numbervalid(req.getOtherCost()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"),"othercost","01")); 
					 }
				 }
				 if(!val.isNull(req.getOsClaimsLossUpdateOC()).equalsIgnoreCase(""))
				 {
					 flag=true;
					
					 req.setOsClaimsLossUpdateOC((req.getOsClaimsLossUpdateOC()).replaceAll(",",""));
					 if(val.numbervalid(req.getOsClaimsLossUpdateOC()).equalsIgnoreCase("INVALID"))
					 {
						  list.add(new ErrorCheck(prop.getProperty("errors.OSClaimLossUpdate.invalid"),"OSClaimLossUpdate","01")); 
					 }
				 }
				 if(StringUtils.isNotBlank(req.getAccountPeriod()) &&"51".equalsIgnoreCase(req.getAccountPeriod()) || "52".equalsIgnoreCase(req.getAccountPeriod()) ||"53".equalsIgnoreCase(req.getAccountPeriod()) || "54".equalsIgnoreCase(req.getAccountPeriod()) ||"55".equalsIgnoreCase(req.getAccountPeriod()) || "56".equalsIgnoreCase(req.getAccountPeriod()) ||  "57".equalsIgnoreCase(req.getAccountPeriod())){

					 if(premiumImpl.getCountAccountPeriod(req)==0){
					
					 }
				 }
				
				 if(StringUtils.isNotBlank(req.getAccountPeriod()) && "6".equals(req.getAccountPeriod())){
						/*
						 * GetOSBListRes totalOSB =
						 * premiumImpl.getOSBList(req.getTransaction(),req.getContNo(),req.getBranchCode
						 * ()); if(Double.parseDouble(totalOSB.getCommonResponse())>0){ list.add(new
						 * ErrorCheck(prop.getProperty("errors.osbtotal.invalid"),"","")); }
						 */
				 }
				
					req.setTotalCredit((req.getTotalCredit()).replaceAll(",",""));
					req.setTotalDebit((req.getTotalDebit()).replaceAll(",",""));
					if(flag==false)
					{
						 list.add(new ErrorCheck(prop.getProperty("errors.currency.select"),"",""));	 
					}
					if(StringUtils.isBlank(req.getDocumentType())) {
						list.add(new ErrorCheck(prop.getProperty("errors.documenttype.select"),"documenttype","01"));
					}
					if("transEdit".equalsIgnoreCase(req.getMode())){
						if(StringUtils.isBlank(req.getTransDropDownVal()) && "Yes".equalsIgnoreCase(req.getChooseTransaction())){
							list.add(new ErrorCheck(prop.getProperty("resersel.trans"),"TransDropDownVal","01")); 
						}
					}
					
					
		} catch(Exception e) {
			e.printStackTrace();
		}		
		return list;
	}

	public List<ErrorCheck> getPremiumDetailsVali(GetPremiumDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
	
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "03"));
			}
		if(StringUtils.isBlank(req.getRequestNo())) {
			//list.add(new ErrorCheck("Please Enter RequestNo"),"RequestNo", "04"));
			}
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId","ProductId", "05"));
			}
		if(StringUtils.isBlank(req.getTableType())) {
			//list.add(new ErrorCheck("Please Enter TableType","TableType", "06"));
			}
		if(StringUtils.isBlank(req.getTransactionNo())) {
			//list.add(new ErrorCheck("Please Enter TransactionNo"),"TransactionNo", "07"));
			}
		return list;
	}

//	public List<ErrorCheck> premiumEditVali(PremiumEditReq req) {
//		
//		return null;
//	}

	public List<ErrorCheck> getPremiumReservedVali(GetPremiumReservedReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
			if(StringUtils.isBlank(req.getChkbox())) {
			//list.add(new ErrorCheck("Please Enter Chkbox","Chkbox", "02"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo","ContNo", "03"));
			}
		if(StringUtils.isBlank(req.getCurrencyId())) {
			list.add(new ErrorCheck("Please Enter CurrencyId","CurrencyId", "04"));
			}
		if(StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId","DepartmentId", "05"));
			}
		if(StringUtils.isBlank(req.getTransaction())) {
				//list.add(new ErrorCheck("Please Enter Transaction","Transaction", "06"));
			}
			if (StringUtils.isBlank(req.getType())) {
				list.add(new ErrorCheck("Please Enter Type", "Type", "07"));
			}
			
		return list;
	}

//	public List<ErrorCheck> getCashLossCreditVali(InsertPremiumReq req) {
//		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
//		if(StringUtils.isBlank(req.getBranchCode())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter BranchCode"),"BranchCode", "01"));
//			}
//			if(StringUtils.isBlank(req.getChkbox())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter Chkbox"),"Chkbox", "02"));
//			}
//		if(StringUtils.isBlank(req.getContNo())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter ContNo"),"ContNo", "03"));
//			}
//		if(StringUtils.isBlank(req.getCurrencyId())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter CurrencyId"),"CurrencyId", "04"));
//			}
//		if(StringUtils.isBlank(req.getDepartmentId())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter DepartmentId"),"DepartmentId", "05"));
//			}
//		if(StringUtils.isBlank(req.getBrokerage())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter Brokerage"),"Brokerage", "06"));
//			}
//			if (StringUtils.isBlank(req.getClaimPayNo())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter ClaimPayNo"), "ClaimPayNo", "07"));
//			}
//			if(StringUtils.isBlank(req.getCLCsettlementRatelist())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter CLCsettlementRatelist"),"CLCsettlementRatelist", "01"));
//				}
//				if(StringUtils.isBlank(req.getCreditAmountCLDlist())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter CreditAmountCLDlist"),"CreditAmountCLDlist", "02"));
//				}
//			if(StringUtils.isBlank(req.getCreditAmountCLClist())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter CreditAmountCLClist"),"CreditAmountCLClist", "03"));
//				}
//			if(StringUtils.isBlank(req.getCreditAmountCLDlist())) {
//				list.add(new ErrorCheck(prop.getProperty("Please Enter CreditAmountCLDlist"),"CreditAmountCLDlist", "04"));
//				}
//		
//			if(StringUtils.isBlank(req.getMainclaimPaymentNos())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter MainclaimPaymentNos"),"MainclaimPaymentNos", "06"));
//				}
//				if (StringUtils.isBlank(req.getMainCLCsettlementRatelist())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter MainCLCsettlementRatelist"), "MainCLCsettlementRatelist", "07"));
//				}
//				
//				if(StringUtils.isBlank(req.getMaincreditAmountCLClist())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter MaincreditAmountCLClist"),"MaincreditAmountCLClist", "01"));
//					}
//					if(StringUtils.isBlank(req.getMaincreditAmountCLDlist())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter MaincreditAmountCLDlist"),"MaincreditAmountCLDlist", "02"));
//					}
//				if(StringUtils.isBlank(req.getMode())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter Mode"),"Mode", "03"));
//					}
//				if(StringUtils.isBlank(req.getTax())) {
//					list.add(new ErrorCheck(prop.getProperty("Please Enter Tax"),"Tax", "04"));
//					}
//			
//		return list;
//	}

	public List<ErrorCheck> validatePremimReserved(SubmitPremiumReservedReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		boolean check=true;
		final Validation val=new Validation();
		
		
		if(req.getReqList()!=null && req.getReqList().size()>0) {
			for(int i=0 ; i<req.getReqList().size(); i++) {
				SubmitPremiumReservedReq1 req1 = req.getReqList().get(i);
				if("true".equalsIgnoreCase(req1.getChkbox())) {
					check=false;
					if(StringUtils.isBlank(req1.getCreditAmountCLC().replace(",", ""))){
						list.add(new ErrorCheck(prop.getProperty("error.pramount.req")+String.valueOf(i + 1),"CreditAmountCLC","01"));
					}else{
						String ans = calcu.calculatePTTYPopUp(req,"PremiRes",i);
						if(Double.parseDouble(ans)!=Double.parseDouble(req1.getCreditAmountCLC().replace(",", ""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"CreditAmountCLC","01"));
							
						}else{
							
							req1.setCreditAmountCLC(ans);
							
						}
						
						if(val.numbervalid(req1.getCreditAmountCLC().replace(",", "")).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("error.pramount.invalid")+String.valueOf(i + 1),"CreditAmountCLC","01"));
					}
					}
					if(StringUtils.isBlank(req1.getCreditAmountCLD().replace(",", ""))){
						list.add(new ErrorCheck(prop.getProperty("error.preamount.req")+String.valueOf(i + 1),"CreditAmountCLD","01"));
					}
					else if(val.numbervalid(req1.getCreditAmountCLD().replace(",", "")).equalsIgnoreCase("INVALID")){
						list.add(new ErrorCheck(prop.getProperty("error.preamount.invalid")+String.valueOf(i + 1),"",""));
					}else if(Double.parseDouble(req1.getCreditAmountCLD().replace(",", ""))>Double.parseDouble(req1.getPayAmountList().replace(",", ""))){
						list.add(new ErrorCheck(prop.getProperty("error.riamount.less.rt")+String.valueOf(i + 1),"",""));
					}else if(Double.parseDouble(req1.getCreditAmountCLD().replace(",", ""))+Double.parseDouble(req1.getPrAllocatedList().replace(",", ""))<0){
						list.add(new ErrorCheck(prop.getProperty("error.riamount.less.rt")+String.valueOf(i + 1),"CreditAmountCLD","01"));
					}
					if(StringUtils.isBlank(req1.getCLCsettlementRate().replace(",", ""))){
						list.add(new ErrorCheck(prop.getProperty("error.preRate.req")+String.valueOf(i + 1),"CLCsettlementRate","01"));
					}else if(val.numbervalid(req1.getCLCsettlementRate().replace(",", "")).equalsIgnoreCase("INVALID")){
						list.add(new ErrorCheck(prop.getProperty("error.preRate.invalid")+String.valueOf(i + 1),"CLCsettlementRate","01"));
					}
				}
				
				}if(check) {
					list.add(new ErrorCheck(prop.getProperty("error.one.check1"),"check","01"));
				}
	}
	return list;
	}
	public List<ErrorCheck> premiumEditVali(PremiumEditReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "03"));
			}
		if(StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId","CountryId", "01"));
			}
	
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "03"));
			}
		if(StringUtils.isBlank(req.getRequestNo())) {
			//list.add(new ErrorCheck(prop.getProperty("Please Enter BranchCode"),"BranchCode", "01"));
			}
	
		if(StringUtils.isBlank(req.getTableType())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter ContractNo"),"ContractNo", "03"));
			}
//		if(StringUtils.isBlank(req.getTransDropDownVal())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter ContractNo"),"ContractNo", "03"));
//			}
//		if(StringUtils.isBlank(req.getTransactionNo())) {
//			list.add(new ErrorCheck(prop.getProperty("Please Enter ContractNo"),"ContractNo", "03"));
//			}
		return list;
	}

	public List<ErrorCheck> getCassLossCreditVali(GetCassLossCreditReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
		if(StringUtils.isBlank(req.getClaimPayNo())) {
			list.add(new ErrorCheck("Please Enter ClaimPayNo","ClaimPayNo", "03"));
			}
	
		if(StringUtils.isBlank(req.getCurrencyId())) {
			list.add(new ErrorCheck("Please Enter CurrencyId","CurrencyId", "04"));
			}
		if(StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter DepartmentId"),"DepartmentId", "05"));
			}
		if(StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter Mode"),"Mode", "06"));
			}
		if(CollectionUtils.isEmpty(req.getGetCashLossCreditReq1())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter GetCashLossCredit"),"GetCashLossCredit", "07"));
			}
		return list;
	}
	public List<ErrorCheck> viewPremiumDetailsRIVali(ViewPremiumDetailsRIReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo","TransactionNo", "01"));
			}
		return list;
	}

	public List<ErrorCheck> getVatInfoVali(GetVatInfoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode","BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getPremiumAmount())) {
			list.add(new ErrorCheck("Please Enter PremiumAmount","PremiumAmount", "03"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo","ProposalNo", "01"));
			}
		return list;
	}

	public List<ErrorCheck> getRIPremiumListVali(GetRIPremiumListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			//list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		return list;
	}

	public List<ErrorCheck> InsertCashLossCreditVali(InsertPremiumReq req) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ErrorCheck> InsertReverseCashLossCreditVali(InsertReverseCashLossCreditReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getCashlosstranId())) {
			list.add(new ErrorCheck("Please Enter CashlosstranId","CashlosstranId", "01"));
			}
	
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo","ContractNo", "02"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo","ProposalNo", "03"));
			}
	
		if(StringUtils.isBlank(req.getCashlossType())) {
			list.add(new ErrorCheck("Please Enter CashlossType","CashlossType", "04"));
			}
		return list;
	}

	public List<ErrorCheck> CashLossmailTriggerVali(CashLossmailTriggerReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo","ContNo", "01"));
			}
		if(StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId","DepartmentId", "02"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo","ProposalNo", "03"));
			}
		if(StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo","TransactionNo", "04"));
			}
		return list;
	}

}
