package com.maan.insurance.validation.facultative;

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
import com.maan.insurance.model.req.facultative.CoverLimitOCReq;
import com.maan.insurance.model.req.facultative.CoverSNoReq;
import com.maan.insurance.model.req.facultative.DeleteMaintableReq;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.GetInsurarerDetailsReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.facultative.InserLossRecordReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.facultative.InsertInsurarerTableInsertReq;
import com.maan.insurance.model.req.facultative.InsertXolCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InstalMentPremiumReq;
import com.maan.insurance.model.req.facultative.LossDetailsReq;
import com.maan.insurance.model.req.facultative.MoveBonusReq;
import com.maan.insurance.model.req.facultative.RetroDetails;
import com.maan.insurance.model.req.facultative.SecondPageInsertReq;
import com.maan.insurance.model.req.facultative.ShowSecondPagedataReq;
import com.maan.insurance.model.req.facultative.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;
import com.maan.insurance.model.req.facultative.ViewModeReq;
import com.maan.insurance.model.req.facultative.XolcoverSNoReq;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes1;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.facultative.FacultativeServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
@Service
public class FacultativeValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(FacultativeValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private FacultativeServiceImple facImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public FacultativeValidation() {
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

	public List<ErrorCheck> firstPageInsertVali(FirstPageInsertReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		FirstPageInsertRes1 res = new FirstPageInsertRes1();
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDowmImpl.getOpenPeriod(bean.getBranchCode());
		double amt = 0;
		try {
			//getFirstPageSubmitError
		 if(StringUtils.isNotBlank(bean.getProStatus()) &&"A".equalsIgnoreCase(bean.getProStatus())){
					if(StringUtils.isNotBlank(bean.getType()) && "1".equalsIgnoreCase(bean.getType()) ){
						if(StringUtils.isNotBlank(bean.getTotalCoverage())){
							//String ans = calcu.calculateFacultative(bean,"TotCoverage",0);
							if(bean.getCoverLimitOCReq()!=null){
								for(int j=0;j<bean.getCoverLimitOCReq().size();j++){
									CoverLimitOCReq req = bean.getCoverLimitOCReq().get(0);
									String premiumRate=StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",", "");
									amt = amt+Double.parseDouble(premiumRate);
								}
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							if(Double.parseDouble(ans)!=Double.parseDouble(bean.getTotalCoverage().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalCoverage","01"));
							}else{
								bean.setDeductibleFacXol(bean.getTotalCoverage().replace(",", ""));
								bean.setSumInsured(bean.getTotalCoverage().replace(",", ""));
							}
						}
						if(StringUtils.isNotBlank(bean.getTotalGWPI())){
						//	String ans = calcu.calculateFacultative(bean,"TotGwpi",0);
							if(bean.getCoverLimitOCReq()!=null){
								for(int j=0;j<bean.getCoverLimitOCReq().size();j++){
									String premiumRate=StringUtils.isBlank(bean.getCoverLimitOCReq().get(0).getEgnpiAsPerOff())?"0":bean.getCoverLimitOCReq().get(0).getEgnpiAsPerOff().replaceAll(",", "");
									amt = amt+Double.parseDouble(premiumRate);
								}
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							if(Double.parseDouble(ans)!=Double.parseDouble(bean.getTotalGWPI().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalGWPI","01"));
							}else{
								bean.setTotalGWPI(ans);
								bean.setGwpi(bean.getTotalGWPI().replace(",", ""));
							}
						}
					}
					else if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType()) ){
						bean.setGwpi(bean.getXoltotalGWPI().replace(",", ""));	
					}
					final Validation val=new Validation();
					boolean flaging=true;
					if(StringUtils.isNotBlank(bean.getEndorsmentno())&& Integer.parseInt(bean.getEndorsmentno())>0){
						if(StringUtils.isBlank(bean.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
						}
					}
					if(val.isSelect(bean.getSubProfitCenter()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.subProfitCenter.required"),"subProfitCenter","01"));
					}else{
						bean.setSubProfitCenter((bean.getSubProfitCenter()).replaceAll(" ", ""));
					}
					if(val.isSelect(bean.getProfitCenterCode()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.profitCenterCode.required"),"profitCenterCode","01"));
					}
					if(StringUtils.isBlank(bean.getInwardType())||"0".equalsIgnoreCase(bean.getInwardType()) ){
						if("RI01".equalsIgnoreCase(bean.getSourceId())){
							list.add(new ErrorCheck(prop.getProperty("error.inwardtype"),"inwardtype","01"));
						}else{
							list.add(new ErrorCheck(prop.getProperty("error.inwardtype02"),"inwardtype02","01"));
						}
					}
					if(val.isNull(bean.getUnderwriter()).equalsIgnoreCase("0")){
						list.add(new ErrorCheck(prop.getProperty("errors.underwriter.required"),"underwriter","01"));
					}
					boolean cedCheck=true;
					if(val.isNull(bean.getMaxiumlimit()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.ourmaxiumlimit.required"),"ourmaxiumlimit","01"));
						cedCheck=false;
					}else {
						bean.setMaxiumlimit((bean.getMaxiumlimit().replaceAll(",","")));
						if(val.isValidNo(bean.getMaxiumlimit()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.ourmaxiumlimit.invalid"),"ourmaxiumlimit","01"));		
							cedCheck=false;
						}else {
							GetCommonValueRes commonRes=dropDowmImpl.getUnderWriterLimmit(bean.getUnderwriter(),bean.getProcessId(), "1", bean.getDepartmentId());
							String uwLimit=commonRes.getCommonResponse();
							uwLimit=uwLimit.replaceAll(",","");
							if(Double.parseDouble(uwLimit)==0){
								list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config")+uwLimit,"maxLimitproduct","01"));
								cedCheck=false;
							}else if(Double.parseDouble(bean.getMaxiumlimit())>Double.parseDouble(uwLimit)){
								list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"maxLimitproduct","01"));
								cedCheck=false;
							}
						}
					}
					Map<String,Object> map=null;
					List<Map<String, Object>> list1=facImpl.getValidation(bean,1);
					if(list1!=null&&list1.size()>0){
						map=(Map<String,Object>)list1.get(0);
					}
					if(val.isSelect(bean.getPolicyBranch()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.polBranch.required"),"polBranch","01"));
					}
					if(val.isSelect(bean.getType()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.Type.required"),"Type","01"));
					}
					if("-1".equalsIgnoreCase(bean.getCedingCompany())){
						list.add(new ErrorCheck(prop.getProperty("errors.cedingCompany.required"),"cedingCompany","01"));
					}
					if(val.isSelect(bean.getBroker()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.broker.required"),"broker","01"));
					}
					if(val.isNull(bean.getInceptionDate()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.required"),"InceptionDate","01"));			
					}
					else if("INVALID".equalsIgnoreCase(val.checkDate(bean.getInceptionDate()))){
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.Error"),"InceptionDate","01"));
					}else if(!"".equals(bean.getRenewalContractno())&&map!=null){
						if("Invalid".equalsIgnoreCase(val.getDateValidate((String)map.get("EXPIRY_DATE"),bean.getInceptionDate()))){
							list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
						}else{
							res.setRenewalFlag("NEWCONTNO");
						}
					}
					if(val.isNull(bean.getExpiryDate()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.required"),"ExpiryDate","01"));			
					}
					else if("INVALID".equalsIgnoreCase(val.checkDate(bean.getExpiryDate()))){
						list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
					}
					if (!"".equalsIgnoreCase(bean.getInceptionDate())&& !"".equalsIgnoreCase(bean.getExpiryDate())) {
						if (Validation.ValidateTwo(bean.getInceptionDate(),bean.getExpiryDate()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
						}
					}
					if(val.isSelect(bean.getYear()).equalsIgnoreCase("")){			
						list.add(new ErrorCheck(prop.getProperty("errors.year.required"),"year","01"));
					}else if(!"".equals(bean.getRenewalContractno())&& map!=null && Integer.parseInt((String)map.get("UW_YEAR"))>=Integer.parseInt(bean.getYear())){
					}
					boolean excFlag=true;
					if((StringUtils.isNotBlank(bean.getProStatus()) &&"A".equalsIgnoreCase(bean.getProStatus())) || !"".equalsIgnoreCase(bean.getAccountDate())){
					if(val.isNull(bean.getAccountDate()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.AccountDate.required"),"AccountDate","01"));
						excFlag=false;
					}
					else if("INVALID".equalsIgnoreCase(val.checkDate(bean.getAccountDate()))){
						list.add(new ErrorCheck(prop.getProperty("errors.AccountDate.Error"),"AccountDate","01"));
						excFlag=false;
					}
					if (!"".equalsIgnoreCase(bean.getExpiryDate())&& !"".equalsIgnoreCase(bean.getAccountDate())) {
						if (Validation.ValidateTwo(bean.getAccountDate(),bean.getExpiryDate()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"AccountDate","01"));
						}
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(bean.getAccountDate()).equalsIgnoreCase("") && !bean.getEdit().equalsIgnoreCase("endorsment")){
					if(dropDowmImpl.Validatethree(bean.getBranchCode(), bean.getAccountDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
					if(val.isSelect(bean.getOriginalCurrency()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.originalCurrency.required"),"originalCurrency","01"));
						excFlag=false;
					}
					if(excFlag && StringUtils.isBlank(bean.getUsCurrencyRate())){
						GetCommonValueRes commonRes= dropDowmImpl.GetExchangeRate(bean.getOriginalCurrency(),bean.getAccountDate(),bean.getCountryID(),bean.getBranchCode());
						bean.setUsCurrencyRate(commonRes.getCommonResponse());
					}
					}
					if(val.isNull(bean.getUsCurrencyRate()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.usCurrencyRate.required"),"usCurrencyRate","01"));
						cedCheck=false;
					}
					boolean spflag=true;
					if(val.isNull(bean.getSpRetro()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
						spflag=false;
					}
					if(val.isNull(bean.getNoInsurer()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.Required"),"NoInsurer","01"));
					}else if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getNoInsurer()))){
						list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.NumberFormat"),"NoInsurer","01"));
					}else if(spflag && "Y".equals(bean.getSpRetro()) && Integer.parseInt(bean.getNoInsurer())<=0){
						list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.gr0"),"NoInsurer","01"));
					}

					if(StringUtils.isBlank(bean.getTerritory())){
						list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode","01"));
					}if(StringUtils.isBlank(bean.getCountryIncludedList())){
						list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude","01"));
					}
					if(val.isNull(bean.getInsuredName()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.insuredName.required"),"insuredName","01"));
					}
					if(val.isNull(bean.getInterest()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.interestCoverage.required"),"interestCoverage","01"));
					}
					if(val.isNull(bean.getCity()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.city.required"),"city","01"));
					}
					if(val.isNull(bean.getLocation()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.location.required"),"location","01"));
					}
					if(StringUtils.isBlank(bean.getLatitude())){
					}
					if(StringUtils.isBlank(bean.getLongitude())){
					}
					if(StringUtils.isBlank(bean.getLocIssued())){
						list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
					}
					else if("Y".equalsIgnoreCase(bean.getLocIssued())){
						if(StringUtils.isBlank(bean.getLocBankName())){
							list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank","01"));
						}
						if(StringUtils.isBlank(bean.getLocCreditPrd())){
							list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd","01"));
						}
						if(StringUtils.isBlank(bean.getLocCreditAmt())){
							list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt","01"));
						}else{
							bean.setLocCreditAmt(bean.getLocCreditAmt().replaceAll(",", ""));
						}
						if(StringUtils.isBlank(bean.getLocBeneficerName())){
							list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName","01"));
						}
						
						}
					if(val.isNull(bean.getNr()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.nr.required"),"nr","01"));
					}
					if("4".equals(bean.getDepartmentId())){
						if(val.isSelect(bean.getModeOfTransport()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.modeofTransport.required"),"modeofTransport","01"));
						}
						if(val.isNull(bean.getVesselName()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.vesselName.required"),"vesselName","01"));
						}
						if(val.isNull(bean.getVesselAge()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.vesselAge.required"),"vesselAge","01"));
						}
						else if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getVesselAge()))){
							list.add(new ErrorCheck(prop.getProperty("errors.vesselAge.invalid"),"vesselAge","01"));
						}else if(Integer.parseInt(bean.getVesselAge())>100){
							list.add(new ErrorCheck(prop.getProperty("errors.vesselAge.lessEqualHundred"),"vesselAge","01"));
						}
							if(StringUtils.isBlank(bean.getVessaletonnage())){
								list.add(new ErrorCheck(prop.getProperty("empty.vessal.age"),"vesselAge","01"));
							}
						if(val.isNull(bean.getLimitPerVesselOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC","01"));
						}else{
							bean.setLimitPerVesselOC((bean.getLimitPerVesselOC()).replaceAll(",",""));
							if(val.isValidNo(bean.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC","01"));
							}
						}
						if(val.isNull(bean.getLimitPerLocationOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC","01"));
						}else{
							bean.setLimitPerLocationOC((bean.getLimitPerLocationOC()).replaceAll(",",""));
							if(val.isValidNo(bean.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC","01"));
							}
						}
					}
					boolean gwpiFlag=true;
					if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType())){
						if(val.isNull(bean.getDeductible()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.deductible.required"),"deductible","01"));
						}else{
							bean.setDeductible((bean.getDeductible()).replaceAll(",",""));
							if(val.isValidNo(bean.getDeductible()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.deductible.Error"),"deductible","01"));
							}
						}
						if(!val.isNull(bean.getDeductibleFacXol()).equalsIgnoreCase("")){
							bean.setDeductibleFacXol((bean.getDeductibleFacXol()).replaceAll(",",""));
							if(val.isValidNo(bean.getDeductibleFacXol()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.deductibleFacXOL.Invalid"),"deductibleFacXOL","01"));
								gwpiFlag=false;
							}
						}
						}else{
							
						}
					boolean cedflag=true;
					if(StringUtils.isNotBlank(bean.getType()) && "1".equalsIgnoreCase(bean.getType())){
					if (val.isNull(bean.getCedRetenType()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required"),"cedRentType","01"));
						cedflag=false;
					}else{
						if(val.isNull(bean.getCedantsRet()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.cedantsRet.required"),"cedantsRet","01"));
							cedflag=false;
						}else{
							bean.setCedantsRet((bean.getCedantsRet().replaceAll(",","")));
							if("A".equalsIgnoreCase(bean.getCedRetenType())){						
								cedflag=false;
								if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getCedantsRet()))){
									list.add(new ErrorCheck(prop.getProperty("error.cedRentAmt.required"),"cedRentAmt","01"));
								}
							}else if("P".equalsIgnoreCase(bean.getCedRetenType())){
								if (val.percentageValid(bean.getCedantsRet()).trim().equalsIgnoreCase("Invalid")||val.percentageValid(bean.getCedantsRet().trim()).equalsIgnoreCase("less")||val.percentageValid(bean.getCedantsRet().trim()).equalsIgnoreCase("greater")) {
									list.add(new ErrorCheck(prop.getProperty("error.cedRentPer.required"),"cedRentPer","01"));
									cedflag=false;
								}
							}
						}
					}
					}
					if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType())){
							if(val.isNull(bean.getPremiumrate()).equalsIgnoreCase("")){
								list.add(new ErrorCheck(prop.getProperty("errors.premium1.required"),"Premiumrate","01"));
								gwpiFlag=false;
							}else if(val.percentageValid(bean.getPremiumrate().trim()).equalsIgnoreCase("less")){
								list.add(new ErrorCheck(prop.getProperty("errors.premium1.percentagesless"),"Premiumrate","01"));
								gwpiFlag=false;
							}else if(val.percentageValid(bean.getPremiumrate().trim()).equalsIgnoreCase("greater")){
								list.add(new ErrorCheck(prop.getProperty("errors.premium1.percentagesgreater"),"Premiumrate","01"));
								gwpiFlag=false;
							}
					}
					if("RI02".equalsIgnoreCase(bean.getSourceId()) && "4".equalsIgnoreCase(bean.getDepartmentId()) && StringUtils.isNotBlank(bean.getType()) && "1".equalsIgnoreCase(bean.getType())){
						if(StringUtils.isBlank(bean.getPslOC())){
							list.add(new ErrorCheck(prop.getProperty("psl.error"),"PslOC","01"));
						}
						else{
							bean.setPslOC(bean.getPslOC().replaceAll(",",""));
						}
						if(StringUtils.isBlank(bean.getPllOC())){
							list.add(new ErrorCheck(prop.getProperty("pll.error"),"PllOC","01"));
						}
						else{
							bean.setPllOC(bean.getPllOC().replaceAll(",",""));
						}
						if(StringUtils.isBlank(bean.getPblOC())){
							list.add(new ErrorCheck(prop.getProperty("pbl.error"),"PblOC","01"));
						}
						else{
							bean.setPblOC(bean.getPblOC().replaceAll(",",""));
						}
						if(val.isNull(bean.getPmll()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.pmll.required"),"Pmll","01"));
							cedCheck=false;
						}else{
							bean.setPmll(bean.getPmll().replaceAll(",",""));
							if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getPmll().trim()))){
								list.add(new ErrorCheck(prop.getProperty("errors.pmll.percentages"),"Pmll","01"));
								cedCheck=false;
							}else{
								amt=Double.parseDouble(bean.getPmll());
							}
						}
					}
					if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType())){//need to check
					if(gwpiFlag&&("".equalsIgnoreCase(bean.getContractNo())||"0".equalsIgnoreCase(bean.getContractNo()))){
						double si="1".equalsIgnoreCase(bean.getType())?Double.parseDouble(bean.getSumInsured()):Double.parseDouble(bean.getDeductibleFacXol());
						double rate=Double.parseDouble(bean.getPremiumrate());
						if(rate!=0){
							double preAmt=(si*rate)/100.0;
							double pre10Per=(((si*rate)/100.0)*10.0)/100.0;
							double gwpi=Double.parseDouble(bean.getGwpi());
							if(!(gwpi>=(preAmt-pre10Per)&&gwpi<=(preAmt+pre10Per))){
								list.add(new ErrorCheck(prop.getProperty("errors.gwpi.exceedPremiumLimit"),"gwpi","01"));
						}
					}
				}
					}
					if("RI01".equalsIgnoreCase(bean.getSourceId())){
						if(val.isNull(bean.getTpl()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.tpl.required"),"tpl","01"));
						}else{
							bean.setTpl((bean.getTpl()).replaceAll(",",""));
							if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getTpl().trim()))){
								list.add(new ErrorCheck(prop.getProperty("errors.tpl.percentages"),"tpl","01"));
							}
						}
					}
					if(val.isNull(bean.getNoOfInst()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.required"),"noOfInstallments","01"));
					}
					else if("INVALID".equalsIgnoreCase(val.isValidNo(bean.getNoOfInst()))){
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.invalid"),"noOfInstallments","01"));
					}else if(Integer.parseInt(bean.getNoOfInst())<=0){
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.grZero"),"noOfInstallments","01"));
					}
					if(StringUtils.isBlank(bean.getReceiptofPayment())){
						list.add(new ErrorCheck(prop.getProperty("payment.days.required"),"ReceiptofPayment","01"));
					}
					final String proStatus=val.isSelect(bean.getProStatus());

					if(val.isNull(proStatus).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.Status.percentages"),"Status","01"));
					}
					flaging=true;
					double cedPer=0.0;
					if(val.isNull(bean.getShWt()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.required"),"shWt","01"));
						flaging=false;
						cedCheck=false;
					}else if("INVALID".equalsIgnoreCase(val.percentageValid(bean.getShWt().trim()))){
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shWt","01"));
						flaging=false;
						cedCheck=false;
					}else if(val.percentageValid(bean.getShWt().trim()).equalsIgnoreCase("greater")){
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.greater"),"shWt","01"));
						flaging=false;
						cedCheck=false;
					}else{
						cedPer=Double.parseDouble(bean.getShWt());
						if("P".equalsIgnoreCase(bean.getCedRetenType())){
						if(cedflag && Double.parseDouble(bean.getShWt())+Double.parseDouble(StringUtils.isEmpty(bean.getCedantsRet())? "0":bean.getCedantsRet())>100){
							list.add(new ErrorCheck(prop.getProperty("error.SWCedPer.invalid"),"SWCedPer","01"));
						}}
					}
					if("A".equalsIgnoreCase(proStatus)){
						if("INVALID".equalsIgnoreCase(val.percentageNewValid(bean.getShWt().trim()))){
							list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shWt","01"));
							flaging=false;
							cedCheck=false;
						}
						if(val.isNull(bean.getShSd()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign","01"));
							flaging=false;
							cedCheck=false;
						}else if("INVALID".equalsIgnoreCase(val.percentageValid(bean.getShSd().trim()))){
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.per"),"shareSign","01"));
							flaging=false;
							cedCheck=false;
						}else if(val.percentageValid(bean.getShSd().trim()).equalsIgnoreCase("greater")){
							list.add(new ErrorCheck(prop.getProperty("errors.shSd.greater"),"shSd","01"));
							flaging=false;
							cedCheck=false;
						}else{
							cedPer=Double.parseDouble(bean.getShSd());
						}
						if(flaging==true){
							if(Double.parseDouble("".equalsIgnoreCase(bean.getShSd())?"0":bean.getShSd())>Double.parseDouble("".equalsIgnoreCase(bean.getShWt())?"0":bean.getShWt())){
								list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign","01"));
								cedCheck=false;
							}
						}
						if(cedCheck){
							double amount=((amt*(cedPer/100.0))/Double.parseDouble(bean.getUsCurrencyRate()));
							double maxlimit=Double.parseDouble(bean.getMaxiumlimit());
							if(amount>maxlimit){
								list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
							}
						}
					}
					if(StringUtils.isNotBlank(bean.getGwpi()) && StringUtils.isNotBlank(bean.getShSd())){
						final  DecimalFormat twoDigit = new DecimalFormat("###0.00");
						final double dvalue = (Double.parseDouble(bean.getGwpi())* (Double.parseDouble(bean.getShSd())) / 100);
						final double dround = Math.round(dvalue * 100.0) / 100.0;
						final double valu = Double.parseDouble(twoDigit.format(dround));//A
						if(StringUtils.isNotBlank(bean.getContractNo())){
						double sumInst=dropDowmImpl.getSumOfInstallmentBooked(bean.getContractNo(), "0");//B
						if(valu<sumInst){
							list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked"),"installment","01"));
						}
						}
					}if(StringUtils.isNotBlank(bean.getNoOfInst())){
						int count= dropDowmImpl.getCountOfInstallmentBooked(bean.getContractNo(), "0");
						if(Double.parseDouble(bean.getNoOfInst())<count){
							list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked"),"NoOfInst","01"));
						}
					}
//					validationContract();
//					validationRemarks();
//					if(StringUtils.isNotBlank(bean.getType())&&"1".equalsIgnoreCase(bean.getType()) ){
//					validationCoverDeductable();
//					}else{
//						validationXolCoverDeductable();
//					}
			}
			else{
			//	getFirstPageSaveError();
					 if(StringUtils.isNotBlank(bean.getType()) && "1".equalsIgnoreCase(bean.getType()) ){
						 if(StringUtils.isNotBlank(bean.getTotalDeductible())){
								//String ans = calcu.calculateFacultative(bean,"TotDetuctible",0);
								if(bean.getCoverLimitOCReq()!=null){
									for(int j=0;j<bean.getCoverLimitOCReq().size();j++){
										String premiumRate=StringUtils.isBlank(bean.getCoverLimitOCReq().get(0).getDeductableLimitOC())?"0":bean.getCoverLimitOCReq().get(0).getDeductableLimitOC().replaceAll(",", "");
										amt = amt+Double.parseDouble(premiumRate);
									}
								}
								String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
								if(Double.parseDouble(ans)!=Double.parseDouble(bean.getTotalDeductible().replaceAll(",",""))){
									list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalDeductible","01"));
								}else{
									bean.setTotalDeductible(ans);
									bean.setDeductible(bean.getTotalDeductible().replace(",", ""));
								}
						}
						if(StringUtils.isNotBlank(bean.getTotalCoverage())){
								//String ans = calcu.calculateFacultative(bean,"TotCoverage",0);
								if(bean.getCoverLimitOCReq()!=null){
									for(int j=0;j<bean.getCoverLimitOCReq().size();j++){
										CoverLimitOCReq req = bean.getCoverLimitOCReq().get(0);
										String premiumRate=StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",", "");
										amt = amt+Double.parseDouble(premiumRate);
									}
								}
								String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
								if(Double.parseDouble(ans)!=Double.parseDouble(bean.getTotalCoverage().replaceAll(",",""))){
									list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalDeductible","01"));
								}else{
									bean.setTotalCoverage(ans);
									bean.setDeductibleFacXol(bean.getTotalCoverage().replace(",", ""));
									bean.setSumInsured(bean.getTotalCoverage().replace(",", ""));
								}
						}
						if(StringUtils.isNotBlank(bean.getTotalGWPI())){
							//String ans = calcu.calculateFacultative(bean,"TotGwpi",0);
							if(bean.getCoverLimitOCReq()!=null){
								for(int j=0;j<bean.getCoverLimitOCReq().size();j++){
									String premiumRate=StringUtils.isBlank(bean.getCoverLimitOCReq().get(0).getEgnpiAsPerOff())?"0":bean.getCoverLimitOCReq().get(0).getEgnpiAsPerOff().replaceAll(",", "");
									amt = amt+Double.parseDouble(premiumRate);
								}
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							if(Double.parseDouble(ans)!=Double.parseDouble(bean.getTotalGWPI().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalGWPI","01"));
							}else{
								bean.setTotalGWPI(ans);
								bean.setGwpi(bean.getTotalGWPI().replace(",", ""));
							}
						}
					}
					else if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType()) ){
						
						bean.setGwpi(bean.getXoltotalGWPI().replace(",", ""));	
					}
					final Validation val = new Validation();
					boolean flaging = true;
					if (StringUtils.isNotBlank(bean.getEndorsmentno()) && Integer.parseInt(bean.getEndorsmentno()) > 0) {
						if (StringUtils.isBlank(bean.getEndorsmenttype())) {
							list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
						}
					}
					if (val.isSelect(bean.getSubProfitCenter()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.subProfitCenter.required"),"subProfitCenter","01"));
					}else{
						bean.setSubProfitCenter((bean.getSubProfitCenter()).replaceAll(" ", ""));
					}

					if (val.isNull(bean.getUnderwriter()).equalsIgnoreCase("0")) {
						list.add(new ErrorCheck(prop.getProperty("errors.underwriter.required"),"underwriter","01"));
					}
					boolean cedCheck = true;
					if (val.isNull(bean.getMaxiumlimit()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.ourmaxiumlimit.required"),"ourmaxiumlimit","01"));
						cedCheck = false;
					} else {
						bean.setMaxiumlimit((bean.getMaxiumlimit().replaceAll(",", "")));
						if (val.isValidNo(bean.getMaxiumlimit()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.ourmaxiumlimit.invalid"),"ourmaxiumlimit","01"));
							cedCheck = false;
						} else {
							GetCommonValueRes commonRes= dropDowmImpl.getUnderWriterLimmit(bean.getUnderwriter(), bean.getProcessId(), "1", bean.getDepartmentId());
							String uwLimit=commonRes.getCommonResponse();
							uwLimit = uwLimit.replaceAll(",", "");
							if (Double.parseDouble(uwLimit) == 0) {
								list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config")+uwLimit,"maxLimitproduct","01"));
								cedCheck = false;
							} else if (Double.parseDouble(bean.getMaxiumlimit()) > Double.parseDouble(uwLimit)) {
								list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"maxLimitproduct","01"));
								cedCheck = false;
							}
						}
					}
					Map<String, Object> map = null;
					List<Map<String, Object>> list2 = facImpl.getValidation(bean, 1);
					if (list2 != null && list2.size() > 0) {
						map = (Map<String, Object>) list2.get(0);
					}

					if (val.isSelect(bean.getPolicyBranch()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.polBranch.required"),"polBranch","01"));
					}
					if (val.isSelect(bean.getType()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.Type.required"),"Type","01"));
					}
					if ("-1".equalsIgnoreCase(bean.getCedingCompany())) {
						list.add(new ErrorCheck(prop.getProperty("errors.cedingCompany.required"),"cedingCompany","01"));
					}
					if (val.isSelect(bean.getBroker()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.broker.required"),"broker","01"));
					}

					if (val.isNull(bean.getInceptionDate()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.required"),"InceptionDate","01"));
					} else if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getInceptionDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.Error"),"InceptionDate","01"));
					} else if (!"".equals(bean.getRenewalContractno()) && map != null) {
						if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), bean.getInceptionDate()))) {
							list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
						} else {
							res.setRenewalFlag("NEWCONTNO");
						}
					}
					if (val.isNull(bean.getExpiryDate()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.required"),"ExpiryDate","01"));
					} else if ("INVALID".equalsIgnoreCase(val.checkDate(bean
							.getExpiryDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
					}
					if (!"".equalsIgnoreCase(bean.getInceptionDate())&& !"".equalsIgnoreCase(bean.getExpiryDate())) {
						if (Validation.ValidateTwo(bean.getInceptionDate(),
								bean.getExpiryDate()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
						}
					}
					if( !"".equalsIgnoreCase(bean.getAccountDate())){
						if("INVALID".equalsIgnoreCase(val.checkDate(bean.getAccountDate()))){
							list.add(new ErrorCheck(prop.getProperty("errors.AccountDate.Error"),"AccountDate","01"));
						}
						if (!"".equalsIgnoreCase(bean.getExpiryDate())&& !"".equalsIgnoreCase(bean.getAccountDate())) {
							if (Validation.ValidateTwo(bean.getAccountDate(),bean.getExpiryDate()).equalsIgnoreCase("Invalid")) {
								list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"AccountDate","01"));
							}
						}
					}
					if (val.isSelect(bean.getYear()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.year.required"),"year","01"));
					} else if (!"".equals(bean.getRenewalContractno())
							&& map != null
							&& Integer.parseInt((String) map.get("UW_YEAR")) >= Integer
									.parseInt(bean.getYear())) {
					}
					if (val.isNull(bean.getUsCurrencyRate()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.usCurrencyRate.required"),"usCurrencyRate","01"));
						cedCheck = false;
					}
					boolean spflag = true;
					if(val.isSelect(bean.getOriginalCurrency()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.originalCurrency.required"),"originalCurrency","01"));
					}
					if (val.isNull(bean.getSpRetro()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
						spflag = false;
					}

					if (StringUtils.isBlank(bean.getLocIssued())) {
						list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
					}
					if (val.isNull(bean.getNr()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.nr.required"),"nr","01"));
					}
					boolean gwpiFlag = true;
					if (StringUtils.isNotBlank(bean.getType())&& "2".equalsIgnoreCase(bean.getType())) {
						if (!val.isNull(bean.getDeductibleFacXol()).equalsIgnoreCase("")) {
							bean.setDeductibleFacXol((bean.getDeductibleFacXol()).replaceAll(",", ""));
							if (val.isValidNo(bean.getDeductibleFacXol()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.deductibleFacXOL.Invalid"),"deductibleFacXOL","01"));
								gwpiFlag = false;
							}
						}
					}
					boolean cedflag = true;
					if (StringUtils.isNotBlank(bean.getType())&& "1".equalsIgnoreCase(bean.getType())) {
						if (val.isNull(bean.getCedRetenType()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required"),"cedRentType","01"));
							cedflag = false;
						}
					}
					if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType())){
						if (val.isNull(bean.getPremiumrate()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.premium1.required"),"Premiumrate","01"));
							gwpiFlag = false;
						} else if (val.percentageValid(bean.getPremiumrate().trim())
								.equalsIgnoreCase("less")) {
							list.add(new ErrorCheck(prop.getProperty("errors.premium1.percentagesless"),"Premiumrate","01"));
							gwpiFlag = false;
						} else if (val.percentageValid(bean.getPremiumrate().trim())
								.equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("errors.premium1.percentagesgreater"),"percentagesgreater","01"));
							gwpiFlag = false;
						}
					}
					if (StringUtils.isNotBlank(bean.getType())&& "1".equalsIgnoreCase(bean.getType())) {
					
					if(StringUtils.isNotBlank(bean.getType()) && "2".equalsIgnoreCase(bean.getType())){
					if (gwpiFlag && ("".equalsIgnoreCase(bean.getContractNo()) || "0".equalsIgnoreCase(bean.getContractNo()))) {
						double si = "1".equalsIgnoreCase(bean.getType()) ? Double.parseDouble(bean.getSumInsured()) : Double.parseDouble(bean.getDeductibleFacXol());
						double rate = Double.parseDouble(bean.getPremiumrate());
						if (rate != 0) {
							double preAmt = (si * rate) / 100.0;
							double pre10Per = (((si * rate) / 100.0) * 10.0) / 100.0;
							double gwpi = Double.parseDouble(bean.getGwpi());
							if (!(gwpi >= (preAmt - pre10Per) && gwpi <= (preAmt + pre10Per))) {
								list.add(new ErrorCheck(prop.getProperty("errors.gwpi.exceedPremiumLimit"),"gwpi","01"));
							}
						}
					}
				}
					if (val.isNull(bean.getNoOfInst()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.required"),"noOfInstallments","01"));
					} else if ("INVALID".equalsIgnoreCase(val.isValidNo(bean.getNoOfInst()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.invalid"),"noOfInstallments","01"));
					} else if (Integer.parseInt(bean.getNoOfInst()) <= 0) {
						list.add(new ErrorCheck(prop.getProperty("errors.noOfInstallments.grZero"),"noOfInstallments","01"));
					}
					if (StringUtils.isBlank(bean.getReceiptofPayment())) {
						list.add(new ErrorCheck(prop.getProperty("payment.days.required"),"ReceiptofPayment","01"));
					}
					final String proStatus = val.isSelect(bean.getProStatus());

					if (val.isNull(proStatus).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.Status.percentages"),"Status","01"));
					}
					flaging = true;
					double cedPer = 0.0;
					if (val.isNull(bean.getShWt()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.required"),"shWt","01"));
						flaging = false;
						cedCheck = false;
					} else if ("INVALID".equalsIgnoreCase(val.percentageValid(bean.getShWt().trim()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shWt","01"));
						flaging = false;
						cedCheck = false;
					} else if (val.percentageValid(bean.getShWt().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.greater"),"shWt","01"));
						flaging = false;
						cedCheck = false;
					} 
					if ("A".equalsIgnoreCase(proStatus)) {
						if ("INVALID".equalsIgnoreCase(val.percentageNewValid(bean.getShWt().trim()))) {
							list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shWt","01"));
							flaging = false;
							cedCheck = false;
						}
						if (val.isNull(bean.getShSd()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign","01"));
							flaging = false;
							cedCheck = false;
						} else if ("INVALID".equalsIgnoreCase(val.percentageValid(bean.getShSd().trim()))) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.per"),"shareSign","01"));
							flaging = false;
							cedCheck = false;
						} else if (val.percentageValid(bean.getShSd().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("errors.shSd.greater"),"shSd","01"));
							flaging = false;
							cedCheck = false;
						} else {
							cedPer = Double.parseDouble(bean.getShSd());
						}
						if (flaging == true) {
							if (Double.parseDouble(bean.getShSd().equalsIgnoreCase("") ? "0": bean.getShSd()) > Double.parseDouble(bean.getShWt().equalsIgnoreCase("") ? "0" : bean.getShWt())) {
								list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign","01"));
								cedCheck = false;
							}
						}
						if (cedCheck) {
							double amount = ((amt * (cedPer / 100.0)) / Double.parseDouble(bean.getUsCurrencyRate()));
							double maxlimit = Double.parseDouble(bean.getMaxiumlimit());
							if (amount > maxlimit) {
								list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
							}
						}
					}
					if (StringUtils.isNotBlank(bean.getGwpi())
							&& StringUtils.isNotBlank(bean.getShSd())) {
						final DecimalFormat twoDigit = new DecimalFormat("###0.00");
						final double dvalue = (Double.parseDouble(bean.getGwpi())* (Double.parseDouble(bean.getShSd())) / 100);
						final double dround = Math.round(dvalue * 100.0) / 100.0;
						final double valu = Double.parseDouble(twoDigit.format(dround));// 
						if (StringUtils.isNotBlank(bean.getContractNo())) {
							double sumInst = dropDowmImpl.getSumOfInstallmentBooked(bean.getContractNo(),"0");
							if (valu < sumInst) {
								list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked"),"SumOfInstallmentBooked","01"));
							}
						}
					}
					if (StringUtils.isNotBlank(bean.getNoOfInst())) {
						int count = dropDowmImpl.getCountOfInstallmentBooked(bean.getContractNo(), "0");
						if (Double.parseDouble(bean.getNoOfInst()) < count) {
							list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked"),"NoOfInst","01"));
						}
					}
//					validationContract();
//					validationRemarks();
//					if(StringUtils.isNotBlank(bean.getType())&&"1".equalsIgnoreCase(bean.getType()) ){
//					validationCoverDeductable();
//					}else{
//						validationXolCoverDeductable();
//				} 
			}}}catch(Exception e) {
				e.printStackTrace();	
				}
				return list;
			}

	public List<ErrorCheck> validateinsertRemarkDetails(RemarksSaveReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try{
			for(int i=0;i<req.getRemarksReq().size();i++){
				RemarksReq req1= req.getRemarksReq().get(i);
				if(StringUtils.isBlank(req1.getDescription())){
					list.add(new ErrorCheck(prop.getProperty("error.description")+String.valueOf(i+1),"Description","01"));
				}
				else if("".equalsIgnoreCase(req1.getRemark1())){
					list.add(new ErrorCheck(prop.getProperty("error.remarks1")+String.valueOf(i+1),"Remark1","01"));
				}
				if(StringUtils.isBlank(req1.getRemark2())){
					//list.add(new ErrorCheck(prop.getProperty("error.remarks2")+String.valueOf(i+1),"","01"));
					list.add(new ErrorCheck(prop.getProperty("error.remarks2")+String.valueOf(i+1),"Remark2","01"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public List<ErrorCheck> insertCoverDeductableDetailsVali(InsertCoverDeductableDetailsReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double amt = 0;
		try{
			for(int i=0;i<bean.getCoverSNoReq().size();i++){
				CoverSNoReq req = bean.getCoverSNoReq().get(i);
				if(StringUtils.isBlank(req.getCoverdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.cover.dept")+String.valueOf(i+1),"CoverdepartId","01"));
				}
				if(StringUtils.isBlank(req.getCoversubdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.cover.subdept")+String.valueOf(i+1),"CoverdepartId","01"));
				}
				if(StringUtils.isBlank(req.getCoverTypeId())){
					list.add(new ErrorCheck(prop.getProperty("error.CoverType")+String.valueOf(i+1),"CoverTypeId","01"));
				}
				else if("2".equalsIgnoreCase(req.getCoverTypeId())){
					if(StringUtils.isBlank(req.getPmlPerList())){
					list.add(new ErrorCheck(prop.getProperty("error.pmlper")+String.valueOf(i+1),"PmlPerList","01"));
				}else{
					//String ans = calcu.calculateFacultative(bean,"Pmlper",i);
					String premiumRate=StringUtils.isBlank(req.getPmlHundredPer())?"0":req.getPmlHundredPer().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",", "");
					amt = ((Double.parseDouble(premiumRate) *100)/ Double.parseDouble(coverlimit));
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getPmlPerList().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"PmlPerList","01"));
					}else{
						req.setPmlPerList(ans);
					}
				}
				if(StringUtils.isBlank(req.getPmlHundredPer())){
					list.add(new ErrorCheck(prop.getProperty("error.pmlhundredper")+String.valueOf(i+1),"pmlhundredper","01"));
				}
				}
				if(StringUtils.isBlank(req.getCoverLimitOC()) && StringUtils.isNotBlank(req.getDeductableLimitOC())){
				if(StringUtils.isBlank(req.getCoverLimitOC())){
					list.add(new ErrorCheck(prop.getProperty("error.Cover.limit")+String.valueOf(i+1),"CoverLimitOC","01"));
				}
				}
				if(StringUtils.isNotBlank(req.getCoverLimitOC()) && StringUtils.isBlank(req.getDeductableLimitOC())){
					if(StringUtils.isBlank(req.getDeductableLimitOC())){
						list.add(new ErrorCheck(prop.getProperty("error.Cover.deductable")+String.valueOf(i+1),"DeductableLimitOC","01"));
					}
				}
				if(StringUtils.isBlank(req.getCoverageDays()) && StringUtils.isNotBlank(req.getDeductableDays())){
				if(StringUtils.isBlank(req.getCoverageDays())){
					list.add(new ErrorCheck(prop.getProperty("error.Coverage.days.deductable")+String.valueOf(i+1),"CoverageDays","01"));
				}
				}
				if(StringUtils.isNotBlank(req.getCoverageDays()) && StringUtils.isBlank(req.getDeductableDays())){
					if(StringUtils.isBlank(req.getDeductableDays())){
						list.add(new ErrorCheck(prop.getProperty("error.ded.days.deductable")+String.valueOf(i+1),"DeductableDays","01"));
					}
				}
				if(StringUtils.isBlank(req.getEgnpiAsPerOff())){
					list.add(new ErrorCheck(prop.getProperty("error.egnpi.days.deductable")+String.valueOf(i+1),"EgnpiAsPerOff","01"));
				}
				else if(Double.parseDouble(req.getEgnpiAsPerOff().replaceAll(",","")) > ((1.1* (Double.parseDouble(StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",",""))) * Double.parseDouble(req.getPremiumRateList().replaceAll(",",""))))/100) {
					list.add(new ErrorCheck(prop.getProperty("gwpi.calculated.wrongly")+String.valueOf(i+1),"","01"));
				}
				else if(Double.parseDouble(req.getEgnpiAsPerOff().replaceAll(",","")) < ((0.9*(Double.parseDouble(StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",",""))) * Double.parseDouble(req.getPremiumRateList().replaceAll(",",""))))/100) {
					list.add(new ErrorCheck(prop.getProperty("gwpi.calculated.wrongly")+String.valueOf(i+1),"","01"));
				}else{
					//String ans = calcu.calculateFacultative(bean,"Egnpi",i);
					String premiumRate=StringUtils.isBlank(req.getPremiumRateList())?"0":req.getPremiumRateList().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getCoverLimitOC())?"0":req.getCoverLimitOC().replaceAll(",", "");
					amt = ((Double.parseDouble(premiumRate) /100)* Double.parseDouble(coverlimit));
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					double changeans=Double.parseDouble(ans)*10/100;
					if((Double.parseDouble(ans)-changeans)>Double.parseDouble(req.getEgnpiAsPerOff().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"EgnpiAsPerOff","01"));
					}else if((Double.parseDouble(ans)+changeans)<Double.parseDouble(req.getEgnpiAsPerOff().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"EgnpiAsPerOff","01"));
					}else{
						req.setEgnpiAsPerOff(req.getEgnpiAsPerOff());
					}
				}
			}
//			for(int k=0;k<error.size();k++){
//				addActionError(error.get(k));
//			}
//			error=new ArrayList<String>();
		}catch(Exception e){
			e.printStackTrace();
		}
	return list;
	}

	public List<ErrorCheck> insertXolCoverDeductableDetailsVali(InsertXolCoverDeductableDetailsReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double amt = 0;
		try{
			for(int i=0;i<bean.getXolcoverSNoReq().size();i++){
				XolcoverSNoReq req = bean.getXolcoverSNoReq().get(i);
				if(StringUtils.isBlank(req.getXolcoverdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.cover.dept")+String.valueOf(i+1),"XolcoverdepartId","01"));
				}
				if(StringUtils.isBlank(req.getXolcoversubdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.cover.subdept")+String.valueOf(i+1),"XolcoversubdepartId","01"));
				}
				if(StringUtils.isBlank(req.getXolcoverLimitOC()) && StringUtils.isNotBlank(req.getXoldeductableLimitOC())){
				if(StringUtils.isBlank(req.getXolcoverLimitOC())){
					list.add(new ErrorCheck(prop.getProperty("error.Cover.limit.xol")+String.valueOf(i+1),"XolcoverLimitOC","01"));
				}
				}
				if(StringUtils.isNotBlank(req.getXolcoverLimitOC()) && StringUtils.isBlank(req.getXoldeductableLimitOC())){
					if(StringUtils.isBlank(req.getXoldeductableLimitOC())){
						list.add(new ErrorCheck(prop.getProperty("error.Cover.deductable")+String.valueOf(i+1),"XolcoverLimitOC","01"));
					}
				}
				if(StringUtils.isBlank(req.getXolgwpiOC())){
					list.add(new ErrorCheck(prop.getProperty("error.egnpi.days.deductable")+String.valueOf(i+1),"XolgwpiOC","01"));
				}else{
				//	String ans = calcu.calculateFacultative(bean,"Gwpi",i);
					String premiumRate=StringUtils.isBlank(req.getXolpremiumRateList())?"0":req.getXolpremiumRateList().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getXolcoverLimitOC())?"0":req.getXolcoverLimitOC().replaceAll(",", "");
					amt = ((Double.parseDouble(premiumRate) /100)* Double.parseDouble(coverlimit));
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getXolgwpiOC().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"XolgwpiOC","01"));
					}else{
						req.setXolgwpiOC(ans);
					}
				}
			}
//			for(int k=0;k<error.size();k++){
//				addActionError(error.get(k));
//			}
//			error=new ArrayList<String>();
		}catch(Exception e){
			e.printStackTrace();
		}
			return list;
	}


	public List<ErrorCheck> showSecondPagedataVali(ShowSecondPagedataReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getYear())) {
			list.add(new ErrorCheck("Please Enter Year", "Year", "2"));
		}
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getRetroType())) {
			list.add(new ErrorCheck("Please Enter RetroType", "RetroType", "5"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "6"));
		}
		if (StringUtils.isBlank(req.getNoOfInst())) {
			list.add(new ErrorCheck("Please Enter NoOfInst", "NoOfInst", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
		}
		if (CollectionUtils.isEmpty(req.getPaymentDueDays())) {
			list.add(new ErrorCheck("Please Enter PaymentDueDays", "PaymentDueDays", "9"));
		}
		if (CollectionUtils.isEmpty(req.getReceiptofPayment())) {
			list.add(new ErrorCheck("Please Enter ReceiptofPayment", "ReceiptofPayment", "10"));
		}
//		if (CollectionUtils.isEmpty(req.getRetrolList())) {
//			list.add(new ErrorCheck("Please Enter RetrolList", "RetrolList", "11"));
//		}
		
				return list;
	}

	public List<ErrorCheck> getRetroContractDetailsListVali(GetRetroContractDetailsListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
//		if (StringUtils.isBlank(req.getYear())) {
//			list.add(new ErrorCheck("Please Enter Year", "Year", "3"));
//		}
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "4"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "5"));
		}
		if (StringUtils.isBlank(req.getRetroType())) {
			list.add(new ErrorCheck("Please Enter RetroType", "RetroType", "6"));
		}
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "7"));
		}
				return list;
	}

	public List<ErrorCheck> ShowSecondPageEditItemsVali(ShowSecondpageEditItemsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getNoOfInst())) {
			list.add(new ErrorCheck("Please Enter NoOfInst", "NoOfInst", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getReceiptofPayment())) {
			list.add(new ErrorCheck("Please Enter ReceiptofPayment", "ReceiptofPayment", "5"));
		}
				return list;
	}

	public List<ErrorCheck> getInsurarerDetailsVali(GetInsurarerDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "2"));
		}
		if (StringUtils.isBlank(req.getInceptionDate())) {
			list.add(new ErrorCheck("Please Enter InceptionDate", "InceptionDate", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "5"));
		}
		if (StringUtils.isBlank(req.getView())) {
			list.add(new ErrorCheck("Please Enter View", "View", "6"));
		}
		if (StringUtils.isBlank(req.getYear())) {
			list.add(new ErrorCheck("Please Enter Year", "Year", "7"));
		}
				return list;
	}

	public List<ErrorCheck> moveBonusVali(MoveBonusReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getAcqBonus())) {
			list.add(new ErrorCheck("Please Enter AcqBonus", "AcqBonus", "2"));
		}
		if (StringUtils.isBlank(req.getBonusTypeId())) {
			list.add(new ErrorCheck("Please Enter BonusTypeId", "BonusTypeId", "3"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "4"));
		}
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "5"));
		}
		if (StringUtils.isBlank(req.getLoginid())) {
			list.add(new ErrorCheck("Please Enter Loginid", "Loginid", "7"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "8"));
		}
		if (CollectionUtils.isEmpty(req.getBonusReq())) {
			list.add(new ErrorCheck("Please Enter BonusDetails", "BonusDetails", "9"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "10"));
		}
		return list;
	}

	public List<ErrorCheck> deleteMaintableVali(DeleteMaintableReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getAcqBonus())) {
			list.add(new ErrorCheck("Please Enter AcqBonus", "AcqBonus", "2"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		return list;
	}

	public List<ErrorCheck> secondPageInsertVali(SecondPageInsertReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try{
			final Validation validation=new Validation();
			if(validation.isNull(bean.getGwpiOurShare()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.gwpiOurShare.required"),"gwpiOurShare","01"));
			} else if("INVALID".equalsIgnoreCase(validation.isValidNo(bean.getGwpiOurShare()))) {
				list.add(new ErrorCheck(prop.getProperty("errors.gwpiOurShare.invalid"),"gwpiOurShare","01"));
			}
//			boolean tata = false;
//			double totalInstPremium=0.0;	
//			if (!validation.isNull(bean.getInstalmentDateList().get(0)).equalsIgnoreCase("")) {
//				if (validation.ValidateINstallDates(bean.getInceptionDate(),bean.getInstalmentDateList().get(0)).equalsIgnoreCase("Invalid")) {
//					tata = true;
//				}
//			}
//			BigDecimal bd = new BigDecimal(totalInstPremium).setScale(2, RoundingMode.HALF_EVEN);
//			totalInstPremium = bd.doubleValue();
//		   if (tata == true) {
//				list.add(new ErrorCheck(prop.getProperty("Error.Select.AfterInceptionDate"),"InceptionDate","01"));
//			}
//		   if("Y".equalsIgnoreCase(bean.getEndorsementStatus())) {
//		   bean.setAccountDate((new DropDownControllor().getAcceptanceDate(bean.getProposalNo())));
//		   bean.setMaxDate(Validation.getMaxDateValidate(bean.getAccountDate(), bean.getPreviousendoDate()));
//			final String endorseDate=validation.checkDate(bean.getEndorsementDate());
//			if (validation.isNull(bean.getEndorsementDate()).equalsIgnoreCase("")) {
//				if("Endorsement".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"),"endoDate","01"));
//				}
//				else if("Rectification".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("error.rectification.required"),"rectification","01"));
//				}
//				
//			} else if (endorseDate.equalsIgnoreCase("INVALID")) {
//				if("Endorsement".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"),"endoDate","01"));
//				}
//				else if("Rectification".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("error.rectification.check"),"rectification","01"));
//				}
//				
//			} else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(bean.getMaxDate(), bean.getEndorsementDate()))) {
//				if("Endorsement".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("errors.endoDate.invalid",new String[] {bean.getAccountDate(), bean.getPreviousendoDate()==null?"":bean.getPreviousendoDate()}));
//				}
//				else if("Rectification".equalsIgnoreCase(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("errors.rectificationDate.invalid",new String[] {bean.getAccountDate(), bean.getPreviousendoDate()==null?"":bean.getPreviousendoDate()}));
//				}
//				
//			}
//		   if(!validation.isNull(bean.getOpstartDate()).equalsIgnoreCase("")&& !validation.isNull(bean.getOpendDate()).equalsIgnoreCase("") && !validation.isNull(bean.getEndorsementDate()).equalsIgnoreCase("")){
//				if(new DropDownControllor().Validatethree(branchCode, bean.getEndorsementDate())==0){
//					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo",new String[] {bean.getOpenPeriodDate()}));
//				}
//			}
//		   }
		   if("7".equals(bean.getDepartmentId())||"9".equals(bean.getDepartmentId())){
			if(validation.isNull(bean.getFireProt()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("errors.fireProt.required"),"fireProt","01"));
			}
			
			if(validation.isNull(bean.getMbind()).equalsIgnoreCase("")){			
				list.add(new ErrorCheck(prop.getProperty("errors.mbind.required"),"mbind","01"));
			}
			if(validation.isNull(bean.getMlopYN()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.mlop.required"),"mlop","01"));
			}
			if(validation.isNull(bean.getAlopYN()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.alop.required"),"AlopYN","01"));
			}
			
			boolean wseq=true;
			if(validation.isNull(bean.getEqwsInd()).equalsIgnoreCase("-1")) {
				list.add(new ErrorCheck(prop.getProperty("errors.eqwsInd.required"),"eqwsInd","01"));
				wseq=false;
			}
			if(wseq){
				if(validation.isNull(bean.getWsThreat()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.wsThreat.required"),"wsThreat","01"));
				}
				if(validation.isNull(bean.getEqThreat()).equalsIgnoreCase("")){
					list.add(new ErrorCheck(prop.getProperty("errors.eqThreat.required"),"eqThreat","01"));
				}
			}
		   }
//			if(bean.getCrestaStatus().equalsIgnoreCase("Y")){
//				if(StringUtils.isBlank(bean.getCrestaPopUp())){
//					list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"),"popup","01"));
//				}
//				 
//				else if(service.getCrestaCount(bean)==0){
//					list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"),"creasta","01"));
//				}
//			}
			
			if(validation.isNull(bean.getLossRecord()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("errros.lossRecord.required"),"lossRecord","01"));
			}
//			else if("Y".equalsIgnoreCase(bean.getLossRecord())){
//				validationLossRecord();
//			}
			if(validation.isNull(bean.getCu()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("errors.cu.required"),"cu","01"));
			}else if("INVALID".equalsIgnoreCase(validation.percentageValid(bean.getCu()))){
				list.add(new ErrorCheck(prop.getProperty("errors.cu.percentages"),"cu","01"));
			}else if(validation.percentageValid(bean.getCu()).equalsIgnoreCase("greater")){
				list.add(new ErrorCheck(prop.getProperty("errors.cu.greater"),"cu","01"));
			} 
			if(validation.isNull(bean.getCuRsn()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("errors.cuRsn.required"),"cuRsn","01"));
			}
		
			if(validation.isNull(bean.getAcqCost()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"),"AcqCost","01"));
			}else{
				bean.setAcqCost((bean.getAcqCost()).replaceAll(",",""));
				if("INVALID".equalsIgnoreCase(validation.isValidNo(bean.getAcqCost()))){
					list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"),"AcqCost","01"));
				}
			}
//			bean.setAcqCost((bean.getAcqCost()).replaceAll(",",""));
//			if(StringUtils.isNotBlank(bean.getEndorsementStatus())&& "Y".equalsIgnoreCase(bean.getEndorsementStatus()) && StringUtils.isBlank(bean.getDocStatus())) {
//				list.add(new ErrorCheck(prop.getProperty("doc.status"),"status","01"));
//			}
//			if(StringUtils.isBlank(bean.getRetroDupContract())){
//				list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+bean.getYear(),"RetroDupContract","01"));
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public List<ErrorCheck> updateSecondPageVali(UpdateSecondPageReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getCrestaStatus())) {
			list.add(new ErrorCheck("Please Enter CrestaStatus", "CrestaStatus", "1"));
		}
		if (StringUtils.isBlank(req.getDocStatus())) {
			list.add(new ErrorCheck("Please Enter DocStatus", "DocStatus", "2"));
		}
		if (StringUtils.isBlank(req.getEndorsmentno())) {
			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "3"));
		}
		if (StringUtils.isBlank(req.getExclusion())) {
			list.add(new ErrorCheck("Please Enter Exclusion", "Exclusion", "4"));
		}
		if (StringUtils.isBlank(req.getLeaderUnderwriter())) {
			list.add(new ErrorCheck("Please Enter LeaderUnderwriter", "LeaderUnderwriter", "5"));
		}
		if (StringUtils.isBlank(req.getLeaderUnderwritercountry())) {
			list.add(new ErrorCheck("Please Enter LeaderUnderwritercountry", "LeaderUnderwritercountry", "6"));
		}
		if (StringUtils.isBlank(req.getLeaderUnderwritershare())) {
			list.add(new ErrorCheck("Please Enter LeaderUnderwritershare", "LeaderUnderwritershare", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
		}
				return list;
	}

	public List<ErrorCheck> inserLossRecordVali(InserLossRecordReq req) { //validationLossRecord
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double amt = 0;
		try{
			final Validation val = new Validation();
			Validation validation=new Validation();
			List<String> error=new ArrayList<String>();
			for(int i=0;i<req.getLossDetails().size();i++){
				LossDetailsReq bean = req.getLossDetails().get(i);
				if(StringUtils.isBlank(bean.getLossYear())){
					list.add(new ErrorCheck(prop.getProperty("err.year")+String.valueOf(i+1),"year","01"));
				}
				if(StringUtils.isBlank(bean.getLossInceptionDate())){
					list.add(new ErrorCheck(prop.getProperty("err.inception")+String.valueOf(i+1),"inception","01"));
				}else if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getLossInceptionDate()))) {
					list.add(new ErrorCheck(prop.getProperty("err.inception.format")+String.valueOf(i+1),"inception","01"));
				}
				if(StringUtils.isBlank(bean.getLossExpiryDate())){
					list.add(new ErrorCheck(prop.getProperty("err.expiry")+String.valueOf(i+1),"expiry","01"));
				}else if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getLossExpiryDate()))) {
					list.add(new ErrorCheck(prop.getProperty("err.expiry.format")+String.valueOf(i+1),"expiry","01"));
				}
				if(StringUtils.isBlank(bean.getLossDateOfLoss())){
					list.add(new ErrorCheck(prop.getProperty("err.dateofloass")+String.valueOf(i+1),"dateofloass","01"));
				}else if ("INVALID".equalsIgnoreCase(val.checkDate(bean.getLossDateOfLoss()))) {
					list.add(new ErrorCheck(prop.getProperty("err.dateofloass.format")+String.valueOf(i+1),"dateofloass","01"));
				}
				
				if(StringUtils.isBlank(bean.getLossInsuredClaim())){
					list.add(new ErrorCheck(prop.getProperty("err.incurredClaim")+String.valueOf(i+1),"incurredClaim","01"));
					}
				if(StringUtils.isBlank(bean.getLossPremium())){
					list.add(new ErrorCheck(prop.getProperty("err.lossPremium")+String.valueOf(i+1),"lossPremium","01"));
				}
				if(StringUtils.isBlank(bean.getLossRatio())){
					list.add(new ErrorCheck(prop.getProperty("err.ratio")+String.valueOf(i+1),"ratio","01"));
				}else{
				//	String ans = calcu.calculateFacultative(bean,"LossRatio",i);
					String premiumRate=StringUtils.isBlank(bean.getLossInsuredClaim())?"0":bean.getLossInsuredClaim().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(bean.getLossPremium())?"0":bean.getLossPremium().replaceAll(",", "");
					amt = (Double.parseDouble(premiumRate) / Double.parseDouble(coverlimit))*100;
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
					if(Double.parseDouble(ans)!=Double.parseDouble(bean.getLossRatio().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"ratio","01"));
					}else{
						//bean.getLossRatio().set(i,ans);
						bean.setLossRatio(ans);
					}
				}
				if(error.size()==7){
					error=new ArrayList<String>();
				}else{
					if(StringUtils.isNotBlank(bean.getLossInceptionDate()) && StringUtils.isNotBlank(bean.getLossExpiryDate()) && !"INVALID".equalsIgnoreCase(val.checkDate(bean.getLossInceptionDate())) && !"INVALID".equalsIgnoreCase(val.checkDate(bean.getLossExpiryDate()))){
						if (validation.ValidateINstallDates(bean.getLossInceptionDate(),bean.getLossExpiryDate()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("err.expiry.greater")+String.valueOf(i+1),"expiry","01"));
						}
					}
					if(StringUtils.isNotBlank(bean.getLossInceptionDate()) && StringUtils.isNotBlank(bean.getLossDateOfLoss()) && !"INVALID".equalsIgnoreCase(val.checkDate(bean.getLossInceptionDate())) && !"INVALID".equalsIgnoreCase(val.checkDate(bean.getLossDateOfLoss()))){
						if (validation.ValidateINstallDates(bean.getLossInceptionDate(),bean.getLossDateOfLoss()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("err.dateofloss.greater")+String.valueOf(i+1),"dateofloss","01"));
						}
					}
//					for(int k=0;k<error.size();k++){
//						addActionError(error.get(k));
//					}
//					error=new ArrayList<String>();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public List<ErrorCheck> instalMentPremiumVali(InstalMentPremiumReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getNoOfInst())) {
			list.add(new ErrorCheck("Please Enter NoOfInst", "NoOfInst", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getLoginid())) {
			list.add(new ErrorCheck("Please Enter Loginid", "Loginid", "5"));
		}
		if (StringUtils.isBlank(req.getUsCurrencyRate())) {
			list.add(new ErrorCheck("Please Enter UsCurrencyRate", "UsCurrencyRate", "6"));
		}
		if (CollectionUtils.isEmpty(req.getInstalmentDetails())) {
			list.add(new ErrorCheck("Please Enter InstalmentDetails", "InstalmentDetails", "7"));
		}
		
				return list;
	}

	public List<ErrorCheck> insertInsurarerTableInsertVali(InsertInsurarerTableInsertReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
		final Validation validation=new Validation();
		final int LoopCount= bean.getNoInsurer()=="" ?0:Integer.parseInt(bean.getNoInsurer());
		double totPer=0.0;
		boolean flag=true;
		if(LoopCount!=0){
			if(validation.isNull(bean.getRetper()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"),"RetentionPercentage","01"));
				flag=false;
			}else if("INVALID".equalsIgnoreCase(validation.percentageValid(bean.getRetper()))){
				list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"),"RetentionPercentage","01"));
				flag=false;
			}else if(validation.percentageValid(bean.getRetper()).equalsIgnoreCase("greater")){		
				list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"),"RetentionPercentage","01"));
				flag=false;
			}else{
				totPer+=Double.parseDouble(bean.getRetper());
			}
		}
		boolean dupCheck=true;
		for(int i=0;i<LoopCount;i++){
			RetroDetails req = bean.getRetroDetails().get(i);
//			List<String> list = new ArrayList<String>();
//			bean.setRetroDupVal(list);
			if(validation.isNull(req.getRetroPercentage()==null?"":req.getRetroPercentage()).equalsIgnoreCase("")){
				list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.Required")+String.valueOf(i+1),"RetroPercentahge","01"));
				flag=false;
			}else if("INVALID".equalsIgnoreCase(validation.percentageValid(req.getRetroPercentage()))){
				list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.invalid")+String.valueOf(i+1),"RetroPercentahge","01"));
				flag=false;
			}else if(validation.percentageValid(req.getRetroPercentage()).equalsIgnoreCase("greater")){
				list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.greater")+String.valueOf(i+1),"RetroPercentahge","01"));
				flag=false;
			}else{
				totPer+=Double.parseDouble(req.getRetroPercentage());
			}
			
		}
		
		if (dupCheck) {
			for (int i = 0; i < LoopCount - 1; i++) {
				RetroDetails req = bean.getRetroDetails().get(i);
				for (int j = i + 1; j < LoopCount; j++) {
					if (req.getCedingCompanyValList().equalsIgnoreCase(req.getCedingCompanyValList())) {
						list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(j+1),"RetroContract","01"));
					}
				}
			}
		}
		if(LoopCount!=0){	
			if(flag){	
				DecimalFormat df = new DecimalFormat("#.##");
				totPer=Double.parseDouble(df.format(totPer));
				if(totPer!=100){
					list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"),"totPercentage","01"));
				}
			}
		}}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<ErrorCheck> insertBonusDetailsVali(InsertBonusDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getAcqBonus())) {
			list.add(new ErrorCheck("Please Enter AcqBonus", "AcqBonus", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getEndorsmentno())) {
			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getLoginid())) {
			list.add(new ErrorCheck("Please Enter Loginid", "Loginid", "7"));
		}
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "8"));
		}
		return list;
	}

	public List<ErrorCheck> insertCrestaMaintableVali(InsertCrestaMaintableReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getEndorsmentno())) {
			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "5"));
		}
			return list;
	}

	public List<ErrorCheck> viewModeVali(ViewModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}		
		return list;
	}

	public List<ErrorCheck> getRetroContractDetailsVali(GetRetroContractDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getDropDown())) {
			list.add(new ErrorCheck("Please Enter DropDown", "DropDown", "2"));
		}
		if (StringUtils.isBlank(req.getInceptionDate())) {
			list.add(new ErrorCheck("Please Enter InceptionDate", "InceptionDate", "3"));
		}
		if (StringUtils.isBlank(req.getRetroDupMode())) {
			list.add(new ErrorCheck("Please Enter RetroDupMode", "RetroDupMode", "4"));
		}
		if (StringUtils.isBlank(req.getRetroType())) {
			list.add(new ErrorCheck("Please Enter RetroType", "RetroType", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getYear())) {
			list.add(new ErrorCheck("Please Enter Year", "Year", "7"));
		}
		return list;
	}
	}
