package com.maan.insurance.validation.proportionality;

import java.io.InputStream;
import com.maan.insurance.model.req.proportionality.ProfitCommissionListReq;
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
import com.maan.insurance.model.req.proportionality.BaseLayerStatusReq;
import com.maan.insurance.model.req.proportionality.BonusSaveReq;
import com.maan.insurance.model.req.proportionality.CedentRetentReq;
import com.maan.insurance.model.req.proportionality.CedentSaveReq;
import com.maan.insurance.model.req.proportionality.ContractReq;
import com.maan.insurance.model.req.proportionality.CrestaReq;
import com.maan.insurance.model.req.proportionality.CrestaSaveReq;
import com.maan.insurance.model.req.proportionality.FirstpageSaveReq;
import com.maan.insurance.model.req.proportionality.GetBonusListCountReq;
import com.maan.insurance.model.req.proportionality.GetCrestaCountReq;
import com.maan.insurance.model.req.proportionality.GetCrestaDetailListReq;
import com.maan.insurance.model.req.proportionality.GetRetentionDetailsReq;
import com.maan.insurance.model.req.proportionality.GetprofitCommissionEnableReq;
import com.maan.insurance.model.req.proportionality.InsertCrestaDetailsReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionSaveReq;
import com.maan.insurance.model.req.proportionality.RemarksReq;
import com.maan.insurance.model.req.proportionality.RemarksSaveReq;
import com.maan.insurance.model.req.proportionality.RetroList;
import com.maan.insurance.model.req.proportionality.RetroListReq;
import com.maan.insurance.model.req.proportionality.RetroSaveReq;
import com.maan.insurance.model.req.proportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.proportionality.SecondpageSaveReq;
import com.maan.insurance.model.req.proportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.proportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.proportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.DropDown.GetContractValRes;
import com.maan.insurance.model.res.DropDown.GetContractValidationRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.proportionality.ProportionalityServiceImpl;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.Validation;
import com.maan.insurance.model.req.proportionality.ScaleCommissionInsertReq;
import com.maan.insurance.model.req.proportionality.saveRiskDeatilsSecondFormReq;
import com.maan.insurance.model.req.proportionality.showSecondPageData1Req;

	

@Service
public class ProportionalityValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropDownImple;
	
	@Autowired
	private ProportionalityServiceImpl propImple;
	
	@Autowired
	private CommonCalculation calcu;
	
	
	public ProportionalityValidation() {
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


	public List<ErrorCheck> validatefirstpageSave(FirstpageSaveReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		boolean flags = true;
		boolean cedCheck = true;
		final PropValidation val = new PropValidation();
		final String tear_nt = val.isNull(req.getTreatyNametype());
		final String brok = val.isSelect(req.getBroker());
		final String incDate = val.checkDate(StringUtils.isBlank(req.getInceptionDate())?"":req.getInceptionDate());
		final String expdate = val.checkDate(StringUtils.isBlank(req.getExpiryDate())?"":req.getExpiryDate());
		final String accDate = val.checkDate(StringUtils.isBlank(req.getAcceptanceDate())?"":req.getAcceptanceDate());
		final String exchRate = val.isNull(req.getExchangeRate());
		final String riskCover = val.isNull(req.getRiskCovered());
		final String terrtyscope = val.isNull(req.getTerritoryscope());
		final String limitPercent = val.isNull(req.getLimitOrigCur());
		final String ourEst = val.isNull(req.getOurEstimate());
		final String epiPercent = val.isNull(req.getEpiorigCur());
		final String Epi = val.isNull(req.getEpi());
		final String xlCost = val.isNull(req.getXlCost());
		final String cenRent = val.isNull(req.getCedRetent());
		final String proStatus = val.isSelect(req.getProStatus());
		final String shareWrit = val.isNull(req.getShareWritten());
		final String shareSign = val.isNull(req.getSharSign());
		final String orginalCurrency = val.isSelect(req.getOrginalCurrency());
		final String maxLimit_Product = val.isNull(req.getMaxLimitProduct());
		if(StringUtils.isNotBlank(req.getProStatus()) &&"A".equalsIgnoreCase(req.getProStatus())){
			
		//	validatenext();
				
					if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0){
					if(StringUtils.isBlank(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype", "01"));
					}
				}
				if (val.isSelect(req.getDepartmentId()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"DepartmentId", "01"));
				}
				if (val.isSelect(req.getSubProfitcenter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitcenter", "01"));
				}else{
					req.setSubProfitcenter((req.getSubProfitcenter()).replaceAll(" ", ""));
				}
				if (val.isSelect(req.getProfitCenter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.Profit_Center.required"),"ProfitCenter", "01"));
				}
				if (val.isNull(req.getInwardType()).equalsIgnoreCase("0")) {
//					if("RI01".equalsIgnoreCase(sourceId)){
//						list.add(new ErrorCheck(prop.getProperty("error.InwardType.Reqired"),"InwardType", "01"));
//					}
//					else
					{
						list.add(new ErrorCheck(prop.getProperty("error.InwardType.Reqired02"),"InwardType", "01"));
					}
				}
				if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("0")) {
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"Underwriter", "01"));
				}

				if (val.isNull(maxLimit_Product).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct", "01"));
					cedCheck = false;
				} else {
					req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
					if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct", "01"));
						cedCheck = false;
					} else {
						GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getProductId(), "0");
						String uwLimit=commonRes.getCommonResponse();
						uwLimit=uwLimit.replaceAll(",", "");
						if (Double.parseDouble(uwLimit) == 0) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct", "01"));
							cedCheck = false;
						} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"MaxLimitProduct","01"));
							cedCheck = false;
						}
					}
				}
				Map<String, Object> map = null;
				if(StringUtils.isNotBlank(req.getInceptionDate()) && StringUtils.isNotBlank(req.getContractNo())) {
				List<Map<String, Object>> list1 = propImple.getValidation(req.getInceptionDate(),req.getContractNo(),req.getDepartmentId());
				if (list1 != null && list1.size() > 0) {
					map = (Map<String, Object>) list1.get(0);
				}
				}
				if (val.isNull(req.getPolicyBranch()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch", "01"));
				}
				if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo", "01"));
				}
				if (val.isNull(brok).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker", "01"));
				}
				if (val.isNull(req.getTreatyType()).equalsIgnoreCase("0")) {
					list.add(new ErrorCheck(prop.getProperty("error.TreatyType.Reqired"),"TreatyType", "01"));
				}
				if (val.isNull(tear_nt).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.treatyName_type.required"),"treatyName_type", "01"));
				}

				if (val.isNull(req.getInceptionDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate", "01"));
				} else if (val.isNull(incDate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate", "01"));
				} else if (!"".equals(req.getRenewalcontractNo())&& !"0".equals(req.getRenewalcontractNo())&& map != null) {
					if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getInceptionDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"incepDate", "01"));
					}else {
					//	req.setRenewalFlag("NEWCONTNO");
					}
				}
				if (val.isNull(req.getExpiryDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate", "01"));
				} else if (val.isNull(expdate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate", "01"));
				}
				if (!val.isNull(req.getInceptionDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpiryDate()).equalsIgnoreCase("")) {
					if (Validation.ValidateTwo(req.getInceptionDate(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"expDate", "01"));
					}
				}

				if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear", "01"));
				} else if (!"".equals(req.getRenewalcontractNo())&& !"0".equals(req.getRenewalcontractNo())&& map != null	&& Integer.parseInt((String) map.get("UW_YEAR")) >= Integer	.parseInt(req.getUwYear())) {
					
				}
				if (val.isNull(req.getAcceptanceDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.required"),"AcceptanceDate", "01"));
				}
				else if (val.isNull(accDate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate", "01"));
				}
				if (val.isNull(orginalCurrency).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency", "01"));
				}
				if (val.isNull(exchRate).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate", "01"));
					cedCheck = false;
				} else if (val.isValidNo(exchRate.trim().toString()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate", "01"));
					cedCheck = false;
				}

				double maxlimit = 0.0;
				boolean spflag = true;

				if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro", "01"));
					spflag = false;
				}
				if (val.isNull(req.getNoInsurer()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.Required"),"NoInsurar", "01"));
				} else if (val.isValidNo(req.getNoInsurer()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.NumberFormat"),"NoInsurar", "01"));
				} else if (spflag && "Y".equals(req.getSpRetro())&& Integer.parseInt(req.getNoInsurer()) <= 0) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.gr0"),"NoInsurar", "01"));
				}
				if( "2".equals(req.getDepartmentId()) || "10".equals(req.getDepartmentId())){
					if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC", "01"));
					}else{
						req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC", "01"));
						}
					}
					if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC", "01"));
					}else{
						req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC", "01"));
						}
					}
				}
				if(StringUtils.isBlank(req.getTerritory())){
					list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode", "01"));
				}
				if (val.isNull(terrtyscope).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.terrtoryScope.required"),"terrtoryScope", "01"));
				}
				double amt = 0.0;

				if (val.isNull(riskCover).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.portfolio.Reqired"),"portfolio", "01"));
				}
				if (val.isNull(req.getProposalType()).equalsIgnoreCase("0")) {
					list.add(new ErrorCheck(prop.getProperty("error.cleancutoff.required"),"cleancutoff", "01"));
				}else if("R".equalsIgnoreCase(req.getProposalType()) || "H".equalsIgnoreCase(req.getProposalType())){
					if(StringUtils.isBlank(req.getRunoffYear())){
						list.add(new ErrorCheck(prop.getProperty("error.runoff.required"),"runoff", "01"));	
					}
				}

				if(StringUtils.isBlank(req.getLOCIssued())){
					list.add(new ErrorCheck(prop.getProperty("error.locissued.required"),"locissued", "01"));
				}else if("Y".equalsIgnoreCase(req.getLOCIssued())){
				if(StringUtils.isBlank(req.getLocBankName())){
					list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank", "01"));
				}
				if(StringUtils.isBlank(req.getLocCreditPrd())){
					list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd", "01"));
				}
				if(StringUtils.isBlank(req.getLocCreditAmt())){
					list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt", "01"));
				}
				else{
					req.setLocCreditAmt(req.getLocCreditAmt().replaceAll(",", ""));
				}
				if(StringUtils.isBlank(req.getLocBeneficerName())){
					list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName", "01"));
				}
				
				}
				
				if(StringUtils.isBlank(req.getPnoc())){
					list.add(new ErrorCheck(prop.getProperty("error.pnoc.required"),"pnoc", "01"));
				}
				else if (val.isNull(req.getPnoc()).equalsIgnoreCase("-1")) {
					list.add(new ErrorCheck(prop.getProperty("error.pnoc.required"),"pnoc", "01"));
				}
				if (val.isNull(req.getAccountingPeriod()).equalsIgnoreCase("0")) {
					list.add(new ErrorCheck(prop.getProperty("error.AccountionPeriod.reqired"),"AccountionPeriod", "01"));
				}
				if (val.isNull(req.getReceiptofStatements()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("Error.ResciptStatments.Required"),"ResciptStatments", "01"));
				} else if (val.isValidNo(req.getReceiptofStatements()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("Error.ReceiptofStatmenst.Error"),"ReceiptofStatmenst", "01"));
				}
				if (val.isNull(req.getReceiptofPayment()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.required"),"ReceiptofStatmenst", "01"));
				} else if (val.isValidNo(req.getReceiptofPayment()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.Error"),"ReceiptofStatmenst", "01"));
				}
				if(StringUtils.isBlank(req.getCountryIncludedList())){
					list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude", "01"));
				
				boolean cedflag = true;
				if (val.isNull(req.getCedRetenType()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required"),"cedRentType", "01"));
					cedflag = false;
				} else {
					if (val.isNull(cenRent).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.cedRent.required"),"cedRent", "01"));
						cedflag = false;
					} else {
						req.setCedRetent((req.getCedRetent()).replaceAll(",", ""));
						if ("A".equalsIgnoreCase(req.getCedRetenType())) {
							cedflag = false;
							if (val.isValidNo(req.getCedRetent()).trim().equalsIgnoreCase("Invalid")) {
								list.add(new ErrorCheck(prop.getProperty("error.cedRentAmt.required"),"cedRentAmt", "01"));
							}
						} else if ("P".equalsIgnoreCase(req.getCedRetenType())) {
							if (val.percentageValid(req.getCedRetent()).trim().equalsIgnoreCase("Invalid")|| val.percentageValid(req.getCedRetent().trim()).equalsIgnoreCase("less")|| val.percentageValid(req.getCedRetent().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.cedRentPer.required"),"cedRentPer", "01"));
								cedflag = false;
							}
						}
					}
				}
				if("3".equalsIgnoreCase(req.getTreatyType()) || "1".equalsIgnoreCase(req.getTreatyType()) ){
					if (val.isNull(limitPercent).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.limitOrigCurr.required"),"limitOrigCurr", "01"));
						cedCheck = false;
					} else {
						req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
						if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.limitOrigCurr.check"),"limitOrigCurr", "01"));
							cedCheck = false;
						} else {
							amt = Double.parseDouble(req.getLimitOrigCur());
						}
					}
					}
				if("3".equalsIgnoreCase(req.getTreatyType()) || "2".equalsIgnoreCase(req.getTreatyType()) ){
					if(StringUtils.isBlank(req.getTreatynoofLine())){
						list.add(new ErrorCheck(prop.getProperty("error.noonline.required"),"noonline", "01"));
					}
				}
				if("4".equalsIgnoreCase(req.getTreatyType()) || "5".equalsIgnoreCase(req.getTreatyType()) ){
					if(StringUtils.isBlank(req.getFaclimitOrigCur())){
						list.add(new ErrorCheck(prop.getProperty("error.fac.limit.currency"),"FaclimitOrigCur", "01"));
					}
					else {
						req.setFaclimitOrigCur((req.getFaclimitOrigCur()).replaceAll(",", ""));
						amt = Double.parseDouble(req.getFaclimitOrigCur());
					}
				}
				if(val.isNull(req.getTreatyType()).equalsIgnoreCase("3") || val.isNull(req.getTreatyType()).equalsIgnoreCase("2")){
					if(StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
						list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.required"),"TreatyLimitsurplusOC", "01"));
						cedCheck = false;
					} else {
						req.setTreatyLimitsurplusOC((req.getTreatyLimitsurplusOC()).replaceAll(",", ""));
						if (val.isValidNo(req.getTreatyLimitsurplusOC()).equalsIgnoreCase("invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.check"),"TreatyLimitsurplusOC", "01"));
							cedCheck = false;
						} else {
							amt = Double.parseDouble(req.getTreatyLimitsurplusOC());
						}
					}
					}

				if(StringUtils.isBlank(req.getPml())){
					list.add(new ErrorCheck(prop.getProperty("error.pml.required"),"pml", "01"));
				}
				else if("Y".equalsIgnoreCase(req.getPml())){
					if(StringUtils.isBlank(req.getPmlPercent())){
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.required"),"pmlpercentage", "01"));
					}
					else{
						double pmlper =Double.parseDouble(req.getPmlPercent());
						if(pmlper>100){
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.less.100.required"),"pmlpercentage", "01"));
						}
					}

			
				}
				if (val.isNull(epiPercent).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required1"),"epiperCent", "01"));
				} else {
					req.setEpiorigCur((req.getEpiorigCur()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpiorigCur().trim()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.check"),"epiperCent", "01"));
					}
				}
				if (val.isNull(ourEst).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.required"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.check"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.checkless"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.checkgreater"),"ourEstimate", "01"));
				}
				else{
					//String ans = calcu.calculatePTTY(req,"OurEstmt",0);
					String premiumRate=StringUtils.isBlank(req.getEpi())?"0":req.getEpi().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getEpiorigCur())?"0":req.getEpiorigCur().replaceAll(",", "");
					if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
					amt = (Double.parseDouble(premiumRate) * 100 / Double.parseDouble(coverlimit));
					}
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurEstimate().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"calcul", "01"));
					
					}else{
						req.setOurEstimate(ans);
					}
				}
				if (StringUtils.isBlank(Epi)) {
					list.add(new ErrorCheck(prop.getProperty("error.epi.required"),"epi", "01"));
				} else {
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epi.invalid"),"epi", "01"));
					}
				//	String ans = calcu.calculatePTTY(req,"EPI",0);
				//	if("EPI".equalsIgnoreCase(type)){
						
						String premiumRate=StringUtils.isBlank(req.getEpiorigCur())?"0":req.getEpiorigCur().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurEstimate())?"0":req.getOurEstimate().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
					
					
					
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"calcul", "01"));
						
					}else{
						req.setEpi(ans);
					}
				}
			
				double cedPer = 0.0;
				flags = true;
				if ("".equalsIgnoreCase(proStatus)) {
					list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus", "01"));
				}

				if (val.isNull(shareWrit).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"ShareWritten", "01"));
					flags = false;
					cedCheck = false;
				} else if (val.percentageNewValid(req.getShareWritten().trim()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"ShareWritten", "01"));
					flags = false;
					cedCheck = false;
				}
				if (val.isNull(proStatus).equalsIgnoreCase("A")) {
					if("INVALID".equalsIgnoreCase(val.percentageNewValid(req.getShareWritten().trim()))){
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shWt.percentages", "01"));
						flags=false;
						cedCheck=false;
					}
					if (val.isNull(shareSign).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.shareSign.required.pro"),"shareSign", "01"));
						flags = false;
						cedCheck = false;
					} else if (val.percentageNewValid(req.getSharSign().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign", "01"));
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getSharSign());
					}
					if (flags) {
						if (Double.parseDouble(val.isNull(req.getSharSign()).equalsIgnoreCase("") ? "0" : req.getSharSign()) > Double.parseDouble(val.isNull(req.getShareWritten()).equalsIgnoreCase("") ? "0" : req.getShareWritten())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign", "01"));
							cedCheck = false;
						}
					}
					if (cedCheck) {
						double amount = (amt * (cedPer / 100.0));
						amount = amount / Double.parseDouble(exchRate);
						maxlimit = Double.parseDouble(req.getMaxLimitProduct());
					
						if (amount > maxlimit) {
							list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt", "01"));
						}
					}
				}

				if (!val.isNull(req.getExpiryDate()).equalsIgnoreCase("") && !val.isNull(req.getAcceptanceDate()).equalsIgnoreCase("")) {
					if (Validation.ValidateTwo(req.getAcceptanceDate(), req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"AcceptanceDate", "01"));
					}
				}
				if (!val.isNull(req.getInceptionDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpiryDate()).equalsIgnoreCase("")) {
					if (Validation.ValidateTwo(req.getInceptionDate(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"AcceptanceDate", "01"));
					}
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(req.getAcceptanceDate()).equalsIgnoreCase("") && !req.getEdit().equalsIgnoreCase("endorsment")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getAcceptanceDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
				
//				if(StringUtils.isNotBlank(req.getRetentionYN()) && "Y".equalsIgnoreCase(req.getRetentionYN())){
//				validationCedentRetention();
//				}
//				validationContract();
//				validationRemarks();

		}}
		else{
			//validatesave();

		
				
					if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0){
					if(StringUtils.isBlank(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("end.type.error"),"type", "01"));
					}
				}
				if (val.isSelect(req.getDepartmentId()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"departId", "01"));
				}
				if (val.isSelect(req.getSubProfitcenter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"subProfit_center", "01"));
				}else{
					req.setSubProfitcenter((req.getSubProfitcenter()).replaceAll(" ", ""));
				}
				
				if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("0")) {
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter", "01"));
				}

				if (val.isNull(maxLimit_Product).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct", "01"));
					cedCheck = false;
				} else {
					req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
					if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct", "01"));
						cedCheck = false;
					} else {
						GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getProductId(), "0");
						String uwLimit=commonRes.getCommonResponse();
						uwLimit=uwLimit.replaceAll(",", "");
						if (Double.parseDouble(uwLimit) == 0) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct", "01"));
							cedCheck = false;
						} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"maxLimitproduct","01"));
							cedCheck = false;
						}
					}
				}
				Map<String, Object> map = null;
				if(StringUtils.isNotBlank(req.getInceptionDate()) && StringUtils.isNotBlank(req.getContractNo())) {
				
				List<Map<String, Object>> list1 = propImple.getValidation(req.getInceptionDate(),req.getContractNo(),req.getDepartmentId());
				if (list1 != null && list1.size() > 0) {
					map = (Map<String, Object>) list1.get(0);
				}
				}
				if (val.isNull(req.getPolicyBranch()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch", "01"));
				}
				if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo", "01"));
				}
				if (val.isNull(brok).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker", "01"));
				}
				if ("0".equalsIgnoreCase(req.getTreatyType())) {
					list.add(new ErrorCheck(prop.getProperty("error.TreatyType.Reqired"),"TreatyType", "01"));
				}
				if (val.isNull(req.getInceptionDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate", "01"));
				} else if (val.isNull(incDate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate", "01"));
				} else if (!"".equals(req.getRenewalcontractNo())&& !"0".equals(req.getRenewalcontractNo())&& map != null) {
					if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getInceptionDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"", "01"));
					}else {
						//req.setRenewalFlag("NEWCONTNO");
					}
				}
				if (val.isNull(req.getExpiryDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate", "01"));
				} else if (val.isNull(expdate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"expDate", "01"));
				}
				if(StringUtils.isNotBlank(req.getInceptionDate()) && StringUtils.isNotBlank(req.getExpiryDate())) {
					if (Validation.ValidateTwo(req.getInceptionDate(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"expDate", "01"));
					}
				}
				if(StringUtils.isNotBlank(req.getAcceptanceDate())){
				if (val.isNull(accDate).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"AcceptanceDate", "01"));
				}
				}
				if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"", "01"));
				} else if (!"".equals(req.getRenewalcontractNo())&& !"0".equals(req.getRenewalcontractNo())&& map != null	&& Integer.parseInt((String) map.get("UW_YEAR")) >= Integer	.parseInt(req.getUwYear())) {
					//list.add(new ErrorCheck(prop.getProperty("errors.year.invalid"),"", "01"));
				}
				
				if (val.isNull(orginalCurrency).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency", "01"));
				}
				if (val.isNull(exchRate).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate", "01"));
					cedCheck = false;
				} else if (val.isValidNo(val.isNull(exchRate).trim().toString()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate", "01"));
					cedCheck = false;
				}

				double maxlimit = 0.0;
				boolean spflag = true;

				if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro", "01"));
					spflag = false;
				}
				
				if( "2".equals(req.getDepartmentId()) || "10".equals(req.getDepartmentId())){
					if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC", "01"));
					}else{
						req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC", "01"));
						}
					}
					if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC", "01"));
					}else{
						req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC", "01"));
						}
					}
				}
				
				double amt = 0.0;

				if ("0".equalsIgnoreCase(req.getProposalType())) {
					list.add(new ErrorCheck(prop.getProperty("error.cleancutoff.required"),"cleancutoff", "01"));
				}

				if(StringUtils.isBlank(req.getLOCIssued())){
					list.add(new ErrorCheck(prop.getProperty("error.locissued.required"),"locissued", "01"));
				}
				if (val.isNull(req.getReceiptofPayment()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.required"),"ReceiptOfStatments", "01"));
				} else if (val.isValidNo(req.getReceiptofPayment()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.Error"),"ReceiptOfStatments", "01"));
				}
				
				boolean cedflag = true;
				if (val.isNull(req.getCedRetenType()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required"),"cedRentType", "01"));
					cedflag = false;
				} else {
					if (val.isNull(cenRent).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.cedRent.required"),"cedRent", "01"));
						cedflag = false;
					} else {
						req.setCedRetent((req.getCedRetent()).replaceAll(",", ""));
						if ("A".equalsIgnoreCase(req.getCedRetenType())) {
							cedflag = false;
						} 
					}
				}
				if("3".equalsIgnoreCase(req.getTreatyType()) || "1".equalsIgnoreCase(req.getTreatyType()) ){
					if (val.isNull(limitPercent).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.limitOrigCurr.required"),"limitOrigCurr", "01"));
						cedCheck = false;
					} else {
						req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
						if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.limitOrigCurr.check"),"limitOrigCurr", "01"));
							cedCheck = false;
						} else {
							amt = Double.parseDouble(req.getLimitOrigCur());
						}
					}
					}
				if("3".equalsIgnoreCase(req.getTreatyType()) || "2".equalsIgnoreCase(req.getTreatyType()) ){
					if(StringUtils.isBlank(req.getTreatynoofLine())){
						list.add(new ErrorCheck(prop.getProperty("error.noonline.required"),"noonline", "01"));
					}
				}
				if("4".equalsIgnoreCase(req.getTreatyType()) || "5".equalsIgnoreCase(req.getTreatyType()) ){
					if(StringUtils.isBlank(req.getFaclimitOrigCur())){
						list.add(new ErrorCheck(prop.getProperty("error.fac.limit.currency"),"FaclimitOrigCur", "01"));
					}
					else {
						req.setFaclimitOrigCur((req.getFaclimitOrigCur()).replaceAll(",", ""));
						amt = Double.parseDouble(req.getFaclimitOrigCur());
					}
				}
				if("3".equalsIgnoreCase(req.getTreatyType()) || "2".equalsIgnoreCase(req.getTreatyType())){
					if(StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
						list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.required"),"TreatyLimitsurplusOC", "01"));
						cedCheck = false;
					} else {
						req.setTreatyLimitsurplusOC((req.getTreatyLimitsurplusOC()).replaceAll(",", ""));
						if (val.isValidNo(req.getTreatyLimitsurplusOC()).equalsIgnoreCase("invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.check"),"TreatyLimitsurplusOC", "01"));
							cedCheck = false;
						} else {
							amt = Double.parseDouble(req.getTreatyLimitsurplusOC());
						}
					}
					}

				if(StringUtils.isBlank(req.getPml())){
					list.add(new ErrorCheck(prop.getProperty("error.pml.required"),"pml", "01"));
				}
				else if("Y".equalsIgnoreCase(req.getPml())){
					if(StringUtils.isBlank(req.getPmlPercent())){
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.required"),"pmlpercentage", "01"));
					}
					else{
						double pmlper =Double.parseDouble(req.getPmlPercent());
						if(pmlper>100){
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.less.100.required"),"pmlpercentage", "01"));
						}
					}

				
				}
				if (val.isNull(epiPercent).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required1"),"epiperCent", "01"));
				} else {
					req.setEpiorigCur((req.getEpiorigCur()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpiorigCur().trim()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.check"),"epiperCent", "01"));
					}
				}
				if (val.isNull(ourEst).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.required"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.check"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.checkless"),"ourEstimate", "01"));
				} else if (val.percentageValid(ourEst.trim()).equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("error.ourEstimate.checkgreater"),"ourEstimate", "01"));
				}else{
					//String ans = calcu.calculatePTTY(req,"OurEstmt",0);
					String premiumRate=StringUtils.isBlank(req.getEpi())?"0":req.getEpi().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getEpiorigCur())?"0":req.getEpiorigCur().replaceAll(",", "");
					if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
					amt = (Double.parseDouble(premiumRate) * 100 / Double.parseDouble(coverlimit));
					}
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurEstimate().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"calcul", "01"));
						
					}else{
						req.setOurEstimate(ans);
					}
				}
				if (StringUtils.isBlank(Epi)) {
					list.add(new ErrorCheck(prop.getProperty("error.epi.required"),"Epi", "01"));
				} else {
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epi.invalid"),"Epi", "01"));
					}
				//	String ans = calcu.calculatePTTY(req,"EPI",0);
					String premiumRate=StringUtils.isBlank(req.getEpiorigCur())?"0":req.getEpiorigCur().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getOurEstimate())?"0":req.getOurEstimate().replaceAll(",", "");
					if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
					amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
					}
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"calcul", "01"));
						
					}else{
						req.setEpi(ans);
					}
				}
				
				double cedPer = 0.0;
				flags = true;
				if ("".equalsIgnoreCase(proStatus)) {
					list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus", "01"));
				}

				if (val.isNull(shareWrit).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"shareWrit", "01"));
					flags = false;
					cedCheck = false;
				} else if (val.percentageNewValid(req.getShareWritten().trim()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"shareWrit", "01"));
					flags = false;
					cedCheck = false;
				}
				if (val.isNull(proStatus).equalsIgnoreCase("A")) {
					if("INVALID".equalsIgnoreCase(val.percentageNewValid(req.getShareWritten().trim()))){
						list.add(new ErrorCheck(prop.getProperty("errors.shWt.percentages"),"shareWrit", "01"));
						flags=false;
						cedCheck=false;
					}
					if (val.isNull(shareSign).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.shareSign.required.pro"),"shareSign", "01"));
						flags = false;
						cedCheck = false;
					} else if (val.percentageNewValid(req.getSharSign().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign", "01"));
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getSharSign());
					}
					if (flags) {
						if (Double.parseDouble(val.isNull(req.getSharSign()).equalsIgnoreCase("") ? "0" : req.getSharSign()) > Double.parseDouble(val.isNull(req.getShareWritten()).equalsIgnoreCase("") ? "0" : req.getShareWritten())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign", "01"));
							cedCheck = false;
						}
					}
					if (cedCheck) {
						double amount = (amt * (cedPer / 100.0));
						amount = amount / Double.parseDouble(exchRate);
						maxlimit = Double.parseDouble(req.getMaxLimitProduct());
						
						if (amount > maxlimit) {
							list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt", "01"));
						}
					}
				}


				if (!val.isNull(req.getExpiryDate()).equalsIgnoreCase("") && !val.isNull(req.getAcceptanceDate()).equalsIgnoreCase("") &&!val.isNull(accDate).equalsIgnoreCase("INVALID")) {
					if (Validation.ValidateTwo(req.getAcceptanceDate(), req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"accDate", "01"));
					}
				}
				if (!val.isNull(req.getInceptionDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpiryDate()).equalsIgnoreCase("") &&!val.isNull(incDate).equalsIgnoreCase("INVALID")) {
					if (Validation.ValidateTwo(req.getInceptionDate(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate", "01"));
					}
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !val.isNull(req.getAcceptanceDate()).equalsIgnoreCase("") && !req.getEdit().equalsIgnoreCase("endorsment") &&!val.isNull(accDate).equalsIgnoreCase("INVALID")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getAcceptanceDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
//				if(StringUtils.isNotBlank(req.getRetentionYN()) && "Y".equalsIgnoreCase(req.getRetentionYN())){
//					validationCedentRetention();
//					}
//				validationContract();
//				validationRemarks();
				
			} 
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public List<ErrorCheck> riskDetailsEditModeVali(RiskDetailsEditModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

//		if (StringUtils.isBlank(req.getContNo())) {
//			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "1"));
//		}

		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(String.valueOf(req.getContractMode()))) {
			list.add(new ErrorCheck("Please Enter ContractMode", "ContractMode", "3"));
		}
		return list;
	}


	public List<ErrorCheck> baseLayerStatusVali(BaseLayerStatusReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}

		if (StringUtils.isBlank(req.getProposalNo())) {			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(String.valueOf(req.getBranchCode()))) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		return list;
	}


	public List<ErrorCheck> showSecondpageEditItemsVali(ShowSecondpageEditItemsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "1"));
		}

		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "4"));
		}
		return list;
	}





	public List<ErrorCheck> validatesecondpageSave(SecondpageSaveReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double amt = 0;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		Validation validation = new Validation();
		 PropValidation val = new PropValidation();
		 
		if("A".equalsIgnoreCase(req.getProStatus())){
			//validateSecondPage();

				if("3".equalsIgnoreCase(req.getTreatyType()) ||"1".equalsIgnoreCase(req.getTreatyType())  ||"4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
					if (StringUtils.isNotBlank(req.getPremiumQuotaShare()) ){
						req.setPremiumQuotaShare((req.getPremiumQuotaShare()).replaceAll(",", ""));
						if (validation.isValidNo(req.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
							if("4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
								list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Obj.Invalid"), "PremiumQuotaShare", "01"));
							}
							else{
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Invalid"), "PremiumQuotaShare", "01"));
							}
						}
					}
					}
				if("3".equalsIgnoreCase(req.getTreatyType()) ||"2".equalsIgnoreCase(req.getTreatyType()) ){

				if (StringUtils.isBlank(req.getPremiumSurplus())) {
				list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.reqired"), "PremiumSurplus", "01"));
				} else {
					req.setPremiumSurplus((req.getPremiumSurplus()).replaceAll(",", ""));
					
					if (validation.isValidNo(req.getPremiumSurplus()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Invalid"), "PremiumSurplus", "01"));
					}
				//	String ans = calcu.calculatePTTY(req,"Surplus",0);
					String premiumRate=StringUtils.isBlank(req.getEpiOSViewOC())?"0":req.getEpiOSViewOC().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replaceAll(",", "");
					if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
					amt = (Double.parseDouble(premiumRate) -  Double.parseDouble(coverlimit));
					}
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getPremiumSurplus().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "PremiumSurplus", "01"));
						
					}else{
						req.setPremiumSurplus(ans);
					}
				}
				}

				if("2".equalsIgnoreCase(req.getInwardType())){
					if(StringUtils.isBlank(req.getOrginalacqcost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.cost.Invalid"), "Orginalacqcost", "01"));
					}
					else if("Y".equalsIgnoreCase(req.getOrginalacqcost())){
					if(StringUtils.isBlank(req.getOurassessmentorginalacqcost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.assessment.Invalid"), "Ourassessmentorginalacqcost", "01"));
					}
					else{
						req.setOurassessmentorginalacqcost(req.getOurassessmentorginalacqcost().replaceAll(",",""));
					}
					if(StringUtils.isBlank(req.getOuracqCost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.acqCost.Invalid"), "OuracqCost", "01"));
					}
					else{
						req.setOuracqCost(req.getOuracqCost().replaceAll(",",""));
					//	String ans = calcu.calculatePTTY(req,"OurAcuCost",0);
						String premiumRate=StringUtils.isBlank(req.getEpiAsPerOffer())?"0":req.getEpiAsPerOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurassessmentorginalacqcost())?"0":req.getOurassessmentorginalacqcost().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) *  Double.parseDouble(coverlimit))/100;
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getOuracqCost().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "OuracqCost", "01"));
							
						}else{
							req.setOuracqCost(ans);
						}
						
					}
					}
				}
				if(StringUtils.isBlank(req.getLocRate())){
					list.add(new ErrorCheck(prop.getProperty("label.rate.year.error"), "LocRate", "01"));
				}
				if("3".equalsIgnoreCase(req.getCommissionType()) ||"1".equalsIgnoreCase(req.getCommissionType())  ||"4".equalsIgnoreCase(req.getCommissionType()) ||"5".equalsIgnoreCase(req.getCommissionType())){
				if(StringUtils.isBlank(req.getCommissionQS())){
					if("4".equalsIgnoreCase(req.getCommissionType()) ||"5".equalsIgnoreCase(req.getCommissionType())){
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.Obj.second"), "CommissionType", "01"));
					}
					else{
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second"), "CommissionType", "01"));
					}
				} else {
					if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1"), "CommissionQS", "01"));
					} else if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1less"), "CommissionQS", "01"));
					} else if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1greater"), "CommissionQS", "01"));
					}
				}
				
			}
			if("3".equalsIgnoreCase(req.getCommissionType()) ||"2".equalsIgnoreCase(req.getCommissionType()) ){
				if(StringUtils.isBlank(req.getCommissionsurp())){
					list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second.req"), "CommissionType", "01"));
				} else {
					if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second"), "Commissionsurp", "01"));
					} else if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondless"), "Commissionsurp", "01"));
					}else if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondgreater"), "Commissionsurp", "01"));
					}
				
				}
				}
				if(StringUtils.isBlank(req.getOverRidder())){
					list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second.req"), "OverRidder", "01"));
				} else {
					if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second1"), "OverRidder", "01"));
					} else if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondless"), "OverRidder", "01"));
					} else if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondgreater"), "OverRidder", "01"));
					}
				}
			

				if (!req.getBroker().equalsIgnoreCase("")) {
					if (!req.getBroker().equalsIgnoreCase("Direct")) {
						if (validation.isNull(req.getBrokerage()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second"), "Brokerage", "01"));
						} else {
							if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second1"), "Brokerage", "01"));
							}

							else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("less")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondless"), "Brokerage", "01"));
							}

							else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondgreater"), "Brokerage", "01"));
							}
						}
					}
				}
				if (validation.isNull(req.getOthercost())
							.equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondinvalid"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondless"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondgreater"), "othercost", "01"));
					}
				if (validation.isNull(req.getTax()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second1"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondless"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondgreater"), "tax", "01"));
				}
				if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
				req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalNo())));
				if(StringUtils.isNotBlank(req.getAccDate()) && StringUtils.isNotBlank( req.getPreviousendoDate())) {
					req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
				}
				final String endorseDate=validation.checkDate(req.getEndorsementDate());
				if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"), "EndorsementDate", "01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.rectification.required"), "rectification", "01"));
					}
					
					
				} else if (val.isNull(endorseDate).equalsIgnoreCase("INVALID")) {
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"), "EndorsementDate", "01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.rectification.check"), "rectification", "01"));
					}
					
				} else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
					
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementDate","01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"rectificationDate","01"));
					}
					
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getEndorsementDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
				}
				}
				if (validation.isNull(req.getAcquisitionCost()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
				} else {
					req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
					if (validation.isValidNo(req.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"), "AcquisitionCost", "01"));
					}else{
					//	String ans = calcu.calculatePTTY(req,"AcqCost",0);
						
						
						double a=0,b=0,c=0;
						if("3".equalsIgnoreCase(req.getTreatyType())){
							a = (((Double.parseDouble(req.getPremiumQuotaShare().equalsIgnoreCase("")?"0":req.getPremiumQuotaShare().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", "")))/100 ;
							b = (((Double.parseDouble(req.getPremiumSurplus().equalsIgnoreCase("")?"0":req.getPremiumSurplus().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", "")))/100 ;
							c = (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *(Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}else if("1".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType())||"4".equalsIgnoreCase(req.getTreatyType())){
							c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}else if("2".equalsIgnoreCase(req.getTreatyType())){
							c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}
						amt = Double.parseDouble((req.getOuracqCost()==null||req.getOuracqCost().equalsIgnoreCase(""))?"0":req.getOuracqCost().replaceAll(",", "")) + a+b+c;
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "AcquisitionCost", "01"));
							
						}else{
							req.setAcquisitionCost(ans);
						}
					}
				}
				final int LoopCount = Integer.parseInt(req.getNoInsurer());
				double totPer = 0.0;
				boolean flag = true;
				if (LoopCount != 0) {
					if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"), "RetentionPercentage", "01"));
						flag = false;
					} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"), "RetentionPercentage", "01"));
						flag = false;
					} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"), "RetentionPercentage", "01"));
						flag = false;
					} else {
						totPer += Double.parseDouble(req.getRetentionPercentage());
					}
				}
				//RetroListReq retroReq = new RetroListReq();
				boolean dupCheck = true;
				for (int i = 0; i < LoopCount; i++) {
					RetroListReq retroReq =req.getRetroListReq().get(i);
					if ("".equals(retroReq.getRetroYear())) {
						list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"RetroYear","01"));
						dupCheck = false;
					}
					if(retroReq.getRetroCeding() ==null){
						list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
						dupCheck = false;
					}
					else if (StringUtils.isBlank(retroReq.getRetroCeding())) {
						list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
						dupCheck = false;
					}
			
					
					if (validation.isNull(retroReq.getPercentRetro()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.Required")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.invalid")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.greater")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else {
						totPer += Double.parseDouble(retroReq.getPercentRetro());
					}

					//RiskDetailsBean bean = new RiskDetailsBean();
					
					req.setProductId("4");
					req.setRetroType("TR");
				}
				if (dupCheck) {
					for (int i = 0; i < LoopCount - 1; i++) {
						for (int j = i + 1; j < LoopCount; j++) {
							RetroListReq retroReq =req.getRetroListReq().get(i);
							if (retroReq.getRetroCeding().equalsIgnoreCase(retroReq.getRetroCeding())) {
								list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(i + 1),"RetroContract","01"));
							}
						}
					}
				}
				if (LoopCount != 0) {
					if (flag) {
						DecimalFormat df = new DecimalFormat("#.##");
						totPer=Double.parseDouble(df.format(totPer));
						if (totPer != 100) {
							list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"), "totPercentage", "01"));
						}
					}
				}
				GetCrestaCountReq req1 =new GetCrestaCountReq();
				req1.setAmendId(req.getAmendId());
				req1.setBranchCode(req.getBranchCode());
				req1.setProposalNo(req.getProposalNo());
				
				if(req.getCrestaStatus().equalsIgnoreCase("Y")){
					if(StringUtils.isBlank(req.getCrestaPopUp())){
						list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"), "popup", "01"));
					}
					else if(propImple.getCrestaCount(req1)==0){
						list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"), "creasta", "01"));
					}
				}
				GetBonusListCountReq req2 = new GetBonusListCountReq();
				req2.setAmendId(req.getAmendId());
				req2.setBranchCode(req.getBranchCode());
				req2.setProposalNo(req.getProposalNo());
				req2.setLayerNo(req.getLayerNo());

				if(StringUtils.isBlank(req.getSlideScaleCommission())){
					list.add(new ErrorCheck(prop.getProperty("error.slidescale.commission"), "slidescale", "01"));
					}
					else if("Y".equalsIgnoreCase(req.getSlideScaleCommission())){
						if(StringUtils.isBlank(req.getSlidePopUp())){
							list.add(new ErrorCheck(prop.getProperty("error.slide.recheck"), "slide", "01"));
						}else{
						int count = propImple.getBonusListCount(req2,"scale");
						if(count<=0){
							list.add(new ErrorCheck(prop.getProperty("slide.error.lcb.table.empty"), "BonusListCount", "01"));
						}
						}
					}
				if(StringUtils.isBlank(req.getBaseLayer())){
					if(StringUtils.isBlank(req.getSlidecommissionSubClass())){
						list.add(new ErrorCheck(prop.getProperty("slide.profit.commission.sub.class"), "SlidecommissionSubClass", "01"));
					}
				}
				
					if(StringUtils.isBlank(req.getLossParticipants())){
						list.add(new ErrorCheck(prop.getProperty("error.losspart"), "LossParticipants", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getLossParticipants())){
							if(StringUtils.isBlank(req.getLossPopUp())){
								list.add(new ErrorCheck(prop.getProperty("error.loss.recheck"), "LossPopUp", "01"));
							}else{
							int count = propImple.getBonusListCount(req2,"lossparticipates");
							if(count<=0){
								list.add(new ErrorCheck(prop.getProperty("losspart.error.lcb.table.empty"), "BonusListCount", "01"));
							}
							}
						}
					if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getLosscommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("loss.profit.commission.sub.class"), "BaseLayer", "01"));
						}
					}
					if(StringUtils.isBlank(req.getShareProfitCommission())){
						list.add(new ErrorCheck(prop.getProperty("errors.share_Profit_Commission.empty"), "ShareProfitCommission", "01"));
					}
					if(StringUtils.isBlank(req.getBaseLayer())){
					if(StringUtils.isBlank(req.getCommissionSubClass())){
						list.add(new ErrorCheck(prop.getProperty("profit.commission.sub.class"), "ShareProfitCommission", "01"));
					}
					}

					if("1".equalsIgnoreCase(req.getShareProfitCommission())){
						if(StringUtils.isBlank(req.getManagementExpenses())){
							list.add(new ErrorCheck(prop.getProperty("man.exp.req"), "ManagementExpenses", "01"));
						}
						if(StringUtils.isBlank(req.getCommissionType())){
							list.add(new ErrorCheck(prop.getProperty("com.type.req"), "CommissionType", "01"));
											}
						else if("PC".equalsIgnoreCase(req.getCommissionType())){
						
						 if (Double.parseDouble(req.getProfitCommissionPer())>100){
							list.add(new ErrorCheck(prop.getProperty("profit.com.less.hundred"), "ProfitCommissionPer", "01"));
						}
						if(StringUtils.isBlank(req.getSuperProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("error.super.pro.com"), "SuperProfitCommission", "01"));
						}else{
							if("Y".equalsIgnoreCase(req.getSuperProfitCommission())){
								if(StringUtils.isBlank(req.getProfitPopUp())){
									list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"), "SuperProfitCommission", "01"));
								}else{
									int count = propImple.CommissionTypeCount(req.getProposalNo(),req.getBranchCode(),req.getCommissionType());
									if(count<=0){
										list.add(new ErrorCheck(prop.getProperty("error.commission.schedule"), "commission", "01"));
									}
							}
							}
						}
						}
						else if("PR".equalsIgnoreCase(req.getCommissionType()) || "LR".equalsIgnoreCase(req.getCommissionType()) ){
						if(StringUtils.isBlank(req.getSetup())){
							list.add(new ErrorCheck(prop.getProperty("error.setup.req"), "setup", "01"));
						}
						if(StringUtils.isBlank(req.getProfitPopUp())){
							list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"), "ProfitPopUp", "01"));
						}else{
						int count = propImple.CommissionTypeCount(req.getProposalNo(),req.getBranchCode(),req.getCommissionType());
						if(count<=0){
							list.add(new ErrorCheck(prop.getProperty("error.commission.setup.schedule"), "CommissionTypeCount", "01"));
						}
						}
						}

						if(StringUtils.isBlank(req.getLossCarried())){
							
						}else if(!"TE".equalsIgnoreCase(req.getLossCarried())){
							if(StringUtils.isBlank(req.getLossyear())){
								list.add(new ErrorCheck(prop.getProperty("error.loss.year"), "Lossyear", "01"));
							}else if(Integer.parseInt(req.getLossyear())>100){
								list.add(new ErrorCheck(prop.getProperty("loss.carried.yeas.less.hundred"), "Lossyear", "01"));
							}
						}
						
						else if(!"TE".equalsIgnoreCase(req.getProfitCarried())){
							if(StringUtils.isBlank(req.getProfitCarriedForYear())){
								list.add(new ErrorCheck(prop.getProperty("profit.carried.year.req"), "ProfitCarriedForYear", "01"));
							}else if(Integer.parseInt(req.getProfitCarriedForYear())>100){
								list.add(new ErrorCheck(prop.getProperty("profit.carried.yeas.less.hundred"), "ProfitCarriedForYear", "01"));
							}
						}
						if(StringUtils.isBlank(req.getSubSeqCalculation())){
							list.add(new ErrorCheck(prop.getProperty("error.sub.seq.cal.req"), "SubSeqCalculation", "01"));
						}if(StringUtils.isBlank(req.getProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("error.profit.commission.req"), "ProfitCommission", "01"));
						}
					}
				if(StringUtils.isBlank(req.getPremiumReserve())){
					list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second"), "PremiumReserve", "01"));
				} else {
					if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second1"), "PremiumReserve", "01"));
					} else if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondless"), "PremiumReserve", "01"));
					} else if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondgreater"), "PremiumReserve", "01"));
					}
				}
				if(StringUtils.isBlank(req.getLossreserve())){
					list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second"), "Lossreserve", "01"));
				} else {
					if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second1"), "Lossreserve", "01"));
					}else if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondless"), "Lossreserve", "01"));
					} else if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondgreater"), "Lossreserve", "01"));
					}
				}
				if(StringUtils.isBlank(req.getInterest())){
					list.add(new ErrorCheck(prop.getProperty("errors.interest.second"), "", "01"));
				} else {
					if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("INVALID")){
						list.add(new ErrorCheck(prop.getProperty("errors.interest.second1"), "interest", "01"));
					} else if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.interest.secondless"), "interest", "01"));
					} else if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.interest.secondgreater"), "interest", "01"));
					}
				}
				if(StringUtils.isBlank(req.getPortfolioinoutLoss())){
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second"), "PortfolioinoutLoss", "01"));
				} else {

					if (validation.percentageValid(req.getPortfolioinoutLoss()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second1"), "PortfolioinoutLoss", "01"));
					} else if (validation.percentageValid(req.getPortfolioinoutLoss()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.secondgreater"), "PortfolioinoutLoss", "01"));
					}
				}
				if(StringUtils.isBlank(req.getPortfolioinoutPremium())){
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.second"), "PortfolioinoutPremium", "01"));
				} else {
					if (validation.percentageValid(req.getPortfolioinoutPremium()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.second"), "PortfolioinoutPremium", "01"));
					} else if (validation.percentageValid(req.getPortfolioinoutPremium()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.greater"), "PortfolioinoutPremium", "01"));
					}
				}
				if (StringUtils.isBlank(req.getLossAdvise())) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second"), "LossAdvise", "01"));
				} else if (validation.isValidNo(req.getLossAdvise().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second1"), "LossAdvise", "01"));
				}
				else{
					req.setLossAdvise((req.getLossAdvise()).replaceAll(",", ""));
				}
				if(StringUtils.isBlank(req.getCashLossLimit())){

					list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.second"), "CashLossLimit", "01"));
				} else {
					req.setCashLossLimit((req.getCashLossLimit()).replaceAll(",", ""));
					if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
					if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
					} else if (!validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getLimitOrigCur())))
						list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
					}
					else if(StringUtils.isBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getTreatyLimitsurplusOC())))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
						}
					else if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
						int t=Double.compare(Double.parseDouble(req.getLimitOrigCur()), Double.parseDouble(req.getTreatyLimitsurplusOC()));
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!(validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")||validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID"))	&& (Double.parseDouble(req.getCashLossLimit()) > (Double.parseDouble(t>0?req.getLimitOrigCur():req.getTreatyLimitsurplusOC()))))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
						}
				}


				if(StringUtils.isBlank(req.getEventlimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.event_limit.second"), "Eventlimit", "01"));
				}else{
					req.setEventlimit(req.getEventlimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getEventlimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.event_limit.invalid"), "Eventlimit", "01"));
					}
				}
				if(StringUtils.isBlank(req.getAggregateLimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.second"), "AggregateLimit", "01"));
				}else{
					req.setAggregateLimit(req.getAggregateLimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getAggregateLimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.invalid"), "", "01"));
					}
				}
				if(StringUtils.isBlank(req.getOccurrentLimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.second"), "OccurrentLimit", "01"));
				}else{
					req.setOccurrentLimit(req.getOccurrentLimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getOccurrentLimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.invalid"), "OccurrentLimit", "01"));
					}
				}

		
				if (validation.isNull(req.getLeaderUnderwriter())
						.equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second"), "LeaderUnderwriter", "01"));
				}
				if("RI02".equalsIgnoreCase(req.getSourceId())){
				if(StringUtils.isBlank(req.getLeaderUnderwritercountry())){
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.country"), "LeaderUnderwritercountry", "01"));
				}
				}
				if(StringUtils.isBlank(req.getLeaderUnderwritershare())){
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.share"), "Underwritershare", "01"));
				}
				if (StringUtils.isNotBlank(req.getLeaderUnderwritershare())) {
					if (validation.percentageValid(	req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second"), "LeaderUnderwritershare", "01"));
					} else if (validation.percentageValid(req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.greater"), "LeaderUnderwritershare", "01"));
					}
				}if(StringUtils.isNotBlank(req.getLeaderUnderwriter()) &&    !"64".equalsIgnoreCase(req.getLeaderUnderwriter())){
					 if(propImple.GetShareValidation(req.getProposalNo(),req.getLeaderUnderwritershare())){
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.greater.signed"), "LeaderUnderwritershare", "01"));
					} 
				}else{
					if(dropDownImple.GetShareEqualValidation(req.getProductId(),req.getLeaderUnderwritershare(),req.getProposalNo())){
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.equals.signed"), "LeaderUnderwritershare", "01"));
					}
				}
				if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
					list.add(new ErrorCheck(prop.getProperty("doc.status"), "EndorsementStatus", "01"));
				}
//				if(StringUtils.isBlank(req.getRetroDupContract())){
//					list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
//				}
				//validationRemarks();

			}
			else{
				//validateSaveSecondPage();
					if("3".equalsIgnoreCase(req.getTreatyType()) ||"1".equalsIgnoreCase(req.getTreatyType())  ||"4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
					if (StringUtils.isNotBlank(req.getPremiumQuotaShare()) ){
						req.setPremiumQuotaShare((req.getPremiumQuotaShare()).replaceAll(",", ""));
						if (validation.isValidNo(req.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
							if("4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
								list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Obj.Invalid"), "TreatyType", "01"));
							}
							else{
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Invalid"), "PremiumQuotaShare", "01"));
							}
						}
					}
					}
					if("3".equalsIgnoreCase(req.getTreatyType()) ||"2".equalsIgnoreCase(req.getTreatyType()) ){

					if (StringUtils.isBlank(req.getPremiumSurplus())) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.reqired"), "PremiumQuotaShare", "01"));
					} else {
						req.setPremiumSurplus((req.getPremiumSurplus()).replaceAll(",", ""));
						
						if (validation.isValidNo(req.getPremiumSurplus()).equalsIgnoreCase("INVALID")) {
							
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Invalid"), "PremiumSurplus", "01"));
						}
					//	String ans = calcu.calculatePTTY(req,"Surplus",0);
						String premiumRate=StringUtils.isBlank(req.getEpiOSViewOC())?"0":req.getEpiOSViewOC().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) -  Double.parseDouble(coverlimit));
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getPremiumSurplus().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "PremiumSurplus", "01"));
							
						}else{
							req.setPremiumSurplus(ans);
						}
					}
					}

					if("2".equalsIgnoreCase(req.getInwardType())){
						if(StringUtils.isBlank(req.getOrginalacqcost())){
							list.add(new ErrorCheck(prop.getProperty("Errors.org.cost.Invalid"), "Orginalacqcost", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getOrginalacqcost())){
						if(StringUtils.isNotBlank(req.getOurassessmentorginalacqcost())){
							req.setOurassessmentorginalacqcost(req.getOurassessmentorginalacqcost().replaceAll(",",""));
						}
						if(StringUtils.isNotBlank(req.getOuracqCost())){
							req.setOuracqCost(req.getOuracqCost().replaceAll(",",""));
						}
						}
					}
					if(StringUtils.isBlank(req.getLocRate())){
						list.add(new ErrorCheck(prop.getProperty("label.rate.year.error"), "", "01"));
					}
					if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
					req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalNo())));
					if(StringUtils.isNotBlank(req.getAccDate()) && StringUtils.isNotBlank( req.getPreviousendoDate())) {
						req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
					}
					final String endorseDate=validation.checkDate(req.getEndorsementDate());
					if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"), "Endorsmenttype", "01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.rectification.required"), "rectification", "01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.required"), "gnpiDate", "01"));
						}
						
					} else if (val.isNull(endorseDate).equalsIgnoreCase("INVALID")) {
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"), "Endorsmenttype", "01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.rectification.check"), "rectification", "01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.check"), "gnpiDate", "01"));
						}
					}
					else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
						//list.add(new ErrorCheck(prop.getProperty("errors.endoDate.invalid"));
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementDate","01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementType","01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.gnpiDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementType","01"));
						}
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")){
						if(dropDownImple.Validatethree(req.getBranchCode(), req.getEndorsementDate())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo")+openPeriodRes.getOpenPeriodDate(),"EndorsementDate","01"));
						}
					}
					}
					if (validation.isNull(req.getAcquisitionCost())
							.equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
					} else {
						req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
						if (validation.isValidNo(req.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
						}else{
							//String ans = calcu.calculatePTTY(req,"AcqCost",0);
							double a=0,b=0,c=0;
							if("3".equalsIgnoreCase(req.getTreatyType())){
								a = (((Double.parseDouble(req.getPremiumQuotaShare().equalsIgnoreCase("")?"0":req.getPremiumQuotaShare().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", "")))/100 ;
								b = (((Double.parseDouble(req.getPremiumSurplus().equalsIgnoreCase("")?"0":req.getPremiumSurplus().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", "")))/100 ;
								c = (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *(Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}else if("1".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType())||"4".equalsIgnoreCase(req.getTreatyType())){
								c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}else if("2".equalsIgnoreCase(req.getTreatyType())){
								c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}
							amt = Double.parseDouble((req.getOuracqCost()==null||req.getOuracqCost().equalsIgnoreCase(""))?"0":req.getOuracqCost().replaceAll(",", "")) + a+b+c;
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
								if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "AcquisitionCost", "01"));
								
							}else{
								req.setAcquisitionCost(ans);
							}
						}
					}
					final int LoopCount = Integer.parseInt(req.getNoInsurer());
					double totPer = 0.0;
					boolean flag = true;
					if (LoopCount != 0) {
						if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"), "RetentionPercentage", "01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"), "RetentionPercentage", "01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"), "RetentionPercentage", "01"));
							flag = false;
						} else {
							totPer += Double.parseDouble(req.getRetentionPercentage());
						}
					}
					boolean dupCheck = true;
					for (int i = 0; i < LoopCount; i++) {
						RetroListReq retroReq =req.getRetroListReq().get(i);
						if ("".equals(retroReq.getRetroYear())) {
							list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"RetroYear","01"));
							dupCheck = false;
						}
						if(retroReq.getRetroCeding() ==null){
							list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
							dupCheck = false;
						}
						else if (StringUtils.isBlank(retroReq.getRetroCeding())) {
							list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
							dupCheck = false;
						}
				
						
						if (validation.isNull(retroReq.getPercentRetro()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.Required")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.invalid")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.greater")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else {
							totPer += Double.parseDouble(retroReq.getPercentRetro());
						}

						//RiskDetailsBean bean = new RiskDetailsBean();
						
						req.setProductId("4");
						req.setRetroType("TR");
					}
					if (dupCheck) {
						for (int i = 0; i < LoopCount - 1; i++) {
							for (int j = i + 1; j < LoopCount; j++) {
								RetroListReq retroReq =req.getRetroListReq().get(i);
								if (retroReq.getRetroCeding().equalsIgnoreCase(retroReq.getRetroCeding())) {
									list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(i + 1),"RetroContract","01"));
								}
							}
						}
					}
					if (LoopCount != 0) {
						if (flag) {
							DecimalFormat df = new DecimalFormat("#.##");
							totPer=Double.parseDouble(df.format(totPer));
							if (totPer != 100) {
								list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"), "totPercentage", "01"));
							}
						}
					}
					GetCrestaCountReq req1 =new GetCrestaCountReq();
					req1.setAmendId(req.getAmendId());
					req1.setBranchCode(req.getBranchCode());
					req1.setProposalNo(req.getProposalNo());
					if(req.getCrestaStatus().equalsIgnoreCase("Y")){
						if(StringUtils.isBlank(req.getCrestaPopUp())){
							list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"), "popup", "01"));
						}
						
						else if(propImple.getCrestaCount(req1)==0){
							list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"), "creasta", "01"));
						}
					}
					GetBonusListCountReq req2 = new GetBonusListCountReq();
					req2.setAmendId(req.getAmendId());
					req2.setBranchCode(req.getBranchCode());
					req2.setProposalNo(req.getProposalNo());
					req2.setLayerNo(req.getLayerNo());

					if(StringUtils.isBlank(req.getSlideScaleCommission())){
						list.add(new ErrorCheck(prop.getProperty("error.slidescale.commission"), "slidescale", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getSlideScaleCommission())){
							if(StringUtils.isBlank(req.getSlidePopUp())){
								list.add(new ErrorCheck(prop.getProperty("error.slide.recheck"), "SlidePopUp", "01"));
							}else{
							int count = propImple.getBonusListCount(req2,"scale");
							if(count<=0){
								list.add(new ErrorCheck(prop.getProperty("slide.error.lcb.table.empty"), "table.empty", "01"));
							}
							}
						}
					if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getSlidecommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("slide.profit.commission.sub.class"), "profit.commission", "01"));
						}
					}
						if(StringUtils.isBlank(req.getLossParticipants())){
							list.add(new ErrorCheck(prop.getProperty("error.losspart"), "LossParticipants", "01"));
							}
							else if("Y".equalsIgnoreCase(req.getLossParticipants())){
								if(StringUtils.isBlank(req.getLossPopUp())){
									list.add(new ErrorCheck(prop.getProperty("error.loss.recheck"), "LossPopUp", "01"));
								}else{
								int count = propImple.getBonusListCount(req2,"lossparticipates");
								if(count<=0){
									list.add(new ErrorCheck(prop.getProperty("losspart.error.lcb.table.empty"), "lossparticipates", "01"));
								}
								}
							}
						if(StringUtils.isBlank(req.getBaseLayer())){
							if(StringUtils.isBlank(req.getLosscommissionSubClass())){
								list.add(new ErrorCheck(prop.getProperty("loss.profit.commission.sub.class"), "LosscommissionSubClass", "01"));
							}
						}
						if(StringUtils.isBlank(req.getShareProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("errors.share_Profit_Commission.empty"), "ShareProfitCommission", "01"));
						}
						if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getCommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("profit.commission.sub.class"), "CommissionSubClass", "01"));
						}
						}

						
					if (StringUtils.isNotBlank(req.getLossAdvise())) {
						
						req.setLossAdvise((req.getLossAdvise()).replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getCashLossLimit())){
						req.setCashLossLimit((req.getCashLossLimit()).replaceAll(",", ""));
						if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getLimitOrigCur())))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "LimitOrigCur", "01"));
						}
						else if(StringUtils.isBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
							if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "GetShareEqualValidationRes", "01"));
							} else if (!validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getTreatyLimitsurplusOC())))
								list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "TreatyLimitsurplusOC", "01"));
							}
						else if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
							int t=Double.compare(Double.parseDouble(req.getLimitOrigCur()), Double.parseDouble(req.getTreatyLimitsurplusOC()));
							if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
							} else if (!(validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")||validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID"))	&& (Double.parseDouble(req.getCashLossLimit()) > (Double.parseDouble(t>0?req.getLimitOrigCur():req.getTreatyLimitsurplusOC()))))
								list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "LimitOrigCur", "01"));
							}
					}
					if(StringUtils.isNotBlank(req.getEventlimit())){
						req.setEventlimit(req.getEventlimit().replaceAll(",", ""));
						
					}
					if(StringUtils.isNotBlank(req.getAggregateLimit())){
						req.setAggregateLimit(req.getAggregateLimit().replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getOccurrentLimit())){
					
						req.setOccurrentLimit(req.getOccurrentLimit().replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
						list.add(new ErrorCheck(prop.getProperty("doc.status"), "EndorsementStatus", "01"));
					}
//					if(StringUtils.isBlank(req.getRetroDupContract())){
//						list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
//					}
				//	validationRemarks();
				
				
			
			}
		return list;
	}


	public List<ErrorCheck> validateinsertRetroContracts(RetroSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<ErrorCheck> validateinsertCrestaMaintable(CrestaSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<ErrorCheck> validateinsertBonusDetails(BonusSaveReq req) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<ErrorCheck> validateinsertProfitCommission(ProfitCommissionSaveReq req) {
		// TODO Auto-generated method stub
		return null;
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
					//error.add(getText("error.remarks2",new String[]{String.valueOf(i+1)}));
					list.add(new ErrorCheck(prop.getProperty("error.remarks2")+String.valueOf(i+1),"Remark2","01"));
				}
			}
			
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return list;
	}


	public List<ErrorCheck> validateinsertCedentRetention(CedentSaveReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		try{
			if(StringUtils.isNotBlank(req.getRetentionYN()) && "Y".equalsIgnoreCase(req.getRetentionYN())){
			for(int i=0;i<req.getCedentRetentReq().size();i++){
				CedentRetentReq req1 =req.getCedentRetentReq().get(i);
				if(StringUtils.isBlank(req1.getCoverdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.coverdept")+String.valueOf(i+1),"coverdept","01"));
				}
				if(StringUtils.isBlank(req1.getCoversubdepartId())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.coversubdept")+String.valueOf(i+1),"CoversubdepartId","01"));
				}
				if(StringUtils.isBlank(req1.getRetBusinessType())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retbusinessType")+String.valueOf(i+1),"RetBusinessType","01"));
				}
				if(StringUtils.isBlank(req1.getRetType())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retType")+String.valueOf(i+1),"RetType","01"));
				}
				if(StringUtils.isBlank(req1.getRetBasis())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retBasis")+String.valueOf(i+1),"RetBasis","01"));
				}
				if(StringUtils.isBlank(req1.getFirstretention())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.firstret")+String.valueOf(i+1),"Firstretention","01"));
				}
				if(StringUtils.isBlank(req1.getSecondretention())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.secobdret")+String.valueOf(i+1),"Secondretention","01"));
				}
				if(StringUtils.isBlank(req1.getRetTreatyFST())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retTreatyFST")+String.valueOf(i+1),"RetTreatyFST","01"));
				}
				if(StringUtils.isBlank(req1.getRetTreatySST())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retTreatySST")+String.valueOf(i+1),"RetTreatySST","01"));
				}
				if(StringUtils.isBlank(req1.getRetEventFST())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retEventFST")+String.valueOf(i+1),"RetEventFST","01"));
				}
				if(StringUtils.isBlank(req1.getRetEventSST())){
					list.add(new ErrorCheck(prop.getProperty("error.ret.retEventFST")+String.valueOf(i+1),"RetEventSST","01"));
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
	public List<ErrorCheck> validateContract(ContractReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try{
			if(StringUtils.isNotBlank(req.getCedingCo())&&StringUtils.isNotBlank(req.getIncepDate())&&StringUtils.isNotBlank(req.getExpDate())&&StringUtils.isNotBlank(req.getUwYear())&&StringUtils.isNotBlank(req.getOrginalCurrency())&&StringUtils.isNotBlank(req.getDepartId())&&StringUtils.isNotBlank(req.getTreatyType())&&StringUtils.isNotBlank(req.getProfitCenter())){
				GetContractValRes res=dropDownImple.getContractValidation(req);
				List<GetContractValidationRes> contractRes = res.getCommonResponse();
				if(contractRes.size()>0){
					if(StringUtils.isBlank(req.getContractListVal())){
						list.add(new ErrorCheck(prop.getProperty("error.contract.list"),"ContractListVal","01"));
					}
				}
			
			}	}catch(Exception e){
			
			e.printStackTrace();
		}
		return list;
	}


	public List<ErrorCheck> getprofitCommissionEnableVali(GetprofitCommissionEnableReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

//		if (StringUtils.isBlank(req.getBaseLayer())) {
//			list.add(new ErrorCheck("Please Enter BaseLayer", "BaseLayer", "1"));
//		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "4"));
		}
		return list;
	}


	public List<ErrorCheck> showSecondPageDataVali(ShowSecondPageDataReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "4"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "2"));
		}
		
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "3"));
		}
		
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "4"));
		}
//		if (StringUtils.isBlank(req.getRetroType())) {
//			list.add(new ErrorCheck("Please Enter RetroType", "RetroType", "4"));
//		}
		return list;
	
	}


	public List<ErrorCheck> showRetroContractsVali(ShowRetroContractsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "4"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "2"));
		}
		
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "3"));
		}
		
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "4"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "4"));
		}
		if ((req.getView().equals(""))) {
			list.add(new ErrorCheck("Please Enter View", "View", "4"));
		}
		return list;
	}


	public List<ErrorCheck> getCrestaDetailListVali(GetCrestaDetailListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "4"));
		}
//		if (StringUtils.isBlank(req.getAmendId())) {
//			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "4"));
//		}
		if (StringUtils.isBlank(req.getProposalno())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		return list;
	}


	public List<ErrorCheck> insertCrestaDetailsVali(InsertCrestaDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		int j=0;
		boolean status=true;
		List<String> error = new ArrayList<String>();
		List<String> lists = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();
		List<String> accRisk=new ArrayList<String>();
		for(int i=0;i<req.getCrestaReq().size();i++){
			CrestaReq req1=req.getCrestaReq().get(i);
			if(StringUtils.isBlank(req1.getTerritoryCode())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.territory")+String.valueOf(i + 1),"territory","1"));
			}
			if(StringUtils.isBlank(req1.getCrestaId())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.crestaid")+String.valueOf(i + 1),"crestaid","2"));
			}
			if(StringUtils.isBlank(req1.getCrestaName())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.crestaname")+String.valueOf(i + 1),"crestaname","3"));
			}
			if(StringUtils.isBlank(req1.getCurrencyId())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.currency")+String.valueOf(i + 1),"currency","4"));
			}
			if(StringUtils.isBlank(req1.getAccumulationDate())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.accumdate")+String.valueOf(i + 1),"accumdate","5"));
			}
			if(StringUtils.isBlank(req1.getAccRisk())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.accrisk")+String.valueOf(i + 1),"accrisk","6"));
				accRisk.add(req1.getAccRisk());
			}
//			else{
//				if(!hasActionErrors()){
//					accRisk.add(req1.getAccRisk().replace(",", ""));
//
//				}else{
//					accRisk.add(req1.getAccRisk());
//				}
//
//			}
//			if(lists.size()==6){
//				lists = new ArrayList<String>();
//				j++;
//			}
//			else if(lists.size()==5 && StringUtils.isNotBlank(req1.getCurrencyId())){
//				lists = new ArrayList<String>();
//				j++;
//			}
//			else{
//				list1 = lists;
//				lists = new ArrayList<String>();
//			}
//		}
//		
//		for(int k=0;k<list1.size();k++){
//			addActionError(list1.get(k));
//		}
//		if(j==req.getCrestaReq().size()){
//			list.add(new ErrorCheck(prop.getProperty("error.cresta.val"),"cresta","1"));
//			status = false;
//		}
//		bean.setAccRisk(accRisk);
//		return status;
		}
		return list;
	}


	public List<ErrorCheck> getRetentionDetailsVali(GetRetentionDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getCedingCo())) {
			list.add(new ErrorCheck("Please Enter CedingCo", "CedingCo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "2"));
		}
		return list;
	}


	public List<ErrorCheck> viewRiskDetailsVali(ViewRiskDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
//		if (StringUtils.isBlank(req.getCountryExcludedName())) {
//			list.add(new ErrorCheck("Please Enter CountryExcludedName", "CountryExcludedName", "2"));
//		}
//		if (StringUtils.isBlank(req.getCountryIncludedName())) {
//			list.add(new ErrorCheck("Please Enter CountryIncludedName", "CountryIncludedName", "2"));
//		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getSharSign())) {
			list.add(new ErrorCheck("Please Enter SharSign", "SharSign", "2"));
		}
		if (StringUtils.isBlank(req.getSubProfitCenter())) {
			list.add(new ErrorCheck("Please Enter SubProfitCenter", "SubProfitCenter", "2"));
		}
		return list;
	}
	public List<ErrorCheck> showSecondPageData1vali(showSecondPageData1Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getProposal())) {
			list.add(new ErrorCheck("Please Enter Proposalno", "Proposalno" ,"2"));
		}
		
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode" ,"3"));
		}
		
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductID", "ProductID" ,"4"));
		}
		return list;
	}
	public List<ErrorCheck> getprofitCommissionDeletevali(String proposalno, String branchCode, String profitSno) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		if(StringUtils.isBlank(proposalno)) {
			list.add(new ErrorCheck("Please Enter Proposalno", "Proposalno" ,"2"));
		}
		
		if(StringUtils.isBlank(branchCode)) {
			list.add(new ErrorCheck("Please Enter BranchCode", "branchCode" ,"3"));
		}
		
		if(StringUtils.isBlank(profitSno)) {
			list.add(new ErrorCheck("Please Enter ProfitSno", "profitSno" ,"4"));
		}
		
		return list;
	}
	public List<ErrorCheck> ProfitCommissionListvali(ProfitCommissionListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		if(StringUtils.isBlank(req.getProposalno())) {
			list.add(new ErrorCheck("Please Enter Proposalno", "Proposalno" ,"2"));
		}
		
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode" ,"3"));
		}
		
		if(StringUtils.isBlank(req.getCommissionType())) {
			list.add(new ErrorCheck("Please Enter CommissionType", "CommissionType" ,"4"));
		}
		
		return list;
	}
	public List<ErrorCheck> ScaleCommissionInsertvali(ScaleCommissionInsertReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter Proposalno", "Proposalno" ,"1"));
		}
		
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "contractNo" ,"2"));
		}
		
		if(StringUtils.isBlank(req.getProductid())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId" ,"3"));
		}
		
		if(StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type" ,"4"));
		}
		
		if(StringUtils.isBlank(req.getBonusTypeId())) {
			list.add(new ErrorCheck("Please Enter BonusTypeId", "Type" ,"5"));
		}
		
		if(StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId" ,"6"));
		}
		
		if(StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo" ,"7"));
		}
		
		if(StringUtils.isBlank(req.getBonusremarks())) {
			list.add(new ErrorCheck("Please Enter Bonusremarks", "Bonusremarks" ,"8"));
		}
		
		return list;
	}


	public List<ErrorCheck> saveRiskDeatilsSecondFormvali(saveRiskDeatilsSecondFormReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		double amt = 0;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		Validation validation = new Validation();
		 PropValidation val = new PropValidation();
		 
		if("A".equalsIgnoreCase(req.getProStatus())){
			//validateSecondPage();

				if("3".equalsIgnoreCase(req.getTreatyType()) ||"1".equalsIgnoreCase(req.getTreatyType())  ||"4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
					if (StringUtils.isNotBlank(req.getPremiumQuotaShare()) ){
						req.setPremiumQuotaShare((req.getPremiumQuotaShare()).replaceAll(",", ""));
						if (validation.isValidNo(req.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
							if("4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
								list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Obj.Invalid"), "PremiumQuotaShare", "01"));
							}
							else{
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Invalid"), "PremiumQuotaShare", "01"));
							}
						}
					}
					}
				if("3".equalsIgnoreCase(req.getTreatyType()) ||"2".equalsIgnoreCase(req.getTreatyType()) ){

				if (StringUtils.isBlank(req.getPremiumSurplus())) {
				list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.reqired"), "PremiumSurplus", "01"));
				} else {
					req.setPremiumSurplus((req.getPremiumSurplus()).replaceAll(",", ""));
					
					if (validation.isValidNo(req.getPremiumSurplus()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Invalid"), "PremiumSurplus", "01"));
					}
				//	String ans = calcu.calculatePTTY(req,"Surplus",0);
					String premiumRate=StringUtils.isBlank(req.getEpiOSViewOC())?"0":req.getEpiOSViewOC().replaceAll(",", "");
					String coverlimit=StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replaceAll(",", "");
					if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
					amt = (Double.parseDouble(premiumRate) -  Double.parseDouble(coverlimit));
					}
					String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getPremiumSurplus().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "PremiumSurplus", "01"));
						
					}else{
						req.setPremiumSurplus(ans);
					}
				}
				}

				if("2".equalsIgnoreCase(req.getInwardType())){
					if(StringUtils.isBlank(req.getOrginalacqcost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.cost.Invalid"), "Orginalacqcost", "01"));
					}
					else if("Y".equalsIgnoreCase(req.getOrginalacqcost())){
					if(StringUtils.isBlank(req.getOurassessmentorginalacqcost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.assessment.Invalid"), "Ourassessmentorginalacqcost", "01"));
					}
					else{
						req.setOurassessmentorginalacqcost(req.getOurassessmentorginalacqcost().replaceAll(",",""));
					}
					if(StringUtils.isBlank(req.getOuracqCost())){
						list.add(new ErrorCheck(prop.getProperty("Errors.org.acqCost.Invalid"), "OuracqCost", "01"));
					}
					else{
						req.setOuracqCost(req.getOuracqCost().replaceAll(",",""));
					//	String ans = calcu.calculatePTTY(req,"OurAcuCost",0);
						String premiumRate=StringUtils.isBlank(req.getEpiAsPerOffer())?"0":req.getEpiAsPerOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurassessmentorginalacqcost())?"0":req.getOurassessmentorginalacqcost().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) *  Double.parseDouble(coverlimit))/100;
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getOuracqCost().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "OuracqCost", "01"));
							
						}else{
							req.setOuracqCost(ans);
						}
						
					}
					}
				}
				if(StringUtils.isBlank(req.getLocRate())){
					list.add(new ErrorCheck(prop.getProperty("label.rate.year.error"), "LocRate", "01"));
				}
				if("3".equalsIgnoreCase(req.getCommissionType()) ||"1".equalsIgnoreCase(req.getCommissionType())  ||"4".equalsIgnoreCase(req.getCommissionType()) ||"5".equalsIgnoreCase(req.getCommissionType())){
				if(StringUtils.isBlank(req.getCommissionQS())){
					if("4".equalsIgnoreCase(req.getCommissionType()) ||"5".equalsIgnoreCase(req.getCommissionType())){
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.Obj.second"), "CommissionType", "01"));
					}
					else{
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second"), "CommissionType", "01"));
					}
				} else {
					if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1"), "CommissionQS", "01"));
					} else if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1less"), "CommissionQS", "01"));
					} else if (validation.percentageValid(req.getCommissionQS().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1greater"), "CommissionQS", "01"));
					}
				}
				
			}
			if("3".equalsIgnoreCase(req.getCommissionType()) ||"2".equalsIgnoreCase(req.getCommissionType()) ){
				if(StringUtils.isBlank(req.getCommissionsurp())){
					list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second.req"), "CommissionType", "01"));
				} else {
					if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second"), "Commissionsurp", "01"));
					} else if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondless"), "Commissionsurp", "01"));
					}else if (validation.percentageValid(req.getCommissionsurp().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondgreater"), "Commissionsurp", "01"));
					}
				
				}
				}
				if(StringUtils.isBlank(req.getOverRidder())){
					list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second.req"), "OverRidder", "01"));
				} else {
					if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second1"), "OverRidder", "01"));
					} else if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondless"), "OverRidder", "01"));
					} else if (validation.percentageValid(req.getOverRidder().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondgreater"), "OverRidder", "01"));
					}
				}
			

				if (!req.getBroker().equalsIgnoreCase("")) {
					if (!req.getBroker().equalsIgnoreCase("Direct")) {
						if (validation.isNull(req.getBrokerage()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second"), "Brokerage", "01"));
						} else {
							if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second1"), "Brokerage", "01"));
							}

							else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("less")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondless"), "Brokerage", "01"));
							}

							else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondgreater"), "Brokerage", "01"));
							}
						}
					}
				}
				if (validation.isNull(req.getOthercost())
							.equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondinvalid"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondless"), "othercost", "01"));
					} else if (validation.percentageValid(
							req.getOthercost()).trim().equalsIgnoreCase(
							"greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondgreater"), "othercost", "01"));
					}
				if (validation.isNull(req.getTax()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second1"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondless"), "tax", "01"));
				} else if (validation.percentageValid(req.getTax()).trim()
						.equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondgreater"), "tax", "01"));
				}
				if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
				req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalno())));
				if(StringUtils.isNotBlank(req.getAccDate()) && StringUtils.isNotBlank( req.getPreviousendoDate())) {
					req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
				}
				final String endorseDate=validation.checkDate(req.getEndorsementDate());
				if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"), "EndorsementDate", "01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.rectification.required"), "rectification", "01"));
					}
					
					
				} else if (val.isNull(endorseDate).equalsIgnoreCase("INVALID")) {
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"), "EndorsementDate", "01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("error.rectification.check"), "rectification", "01"));
					}
					
				} else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
					
					if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementDate","01"));
					}
					else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"rectificationDate","01"));
					}
					
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("")  && !validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getEndorsementDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
				}
				}
				if (validation.isNull(req.getAcquisitionCost()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
				} else {
					req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
					if (validation.isValidNo(req.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"), "AcquisitionCost", "01"));
					}else{
					//	String ans = calcu.calculatePTTY(req,"AcqCost",0);
						
						
						double a=0,b=0,c=0;
						if("3".equalsIgnoreCase(req.getTreatyType())){
							a = (((Double.parseDouble(req.getPremiumQuotaShare().equalsIgnoreCase("")?"0":req.getPremiumQuotaShare().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", "")))/100 ;
							b = (((Double.parseDouble(req.getPremiumSurplus().equalsIgnoreCase("")?"0":req.getPremiumSurplus().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", "")))/100 ;
							c = (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *(Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}else if("1".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType())||"4".equalsIgnoreCase(req.getTreatyType())){
							c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}else if("2".equalsIgnoreCase(req.getTreatyType())){
							c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
						}
						amt = Double.parseDouble((req.getOuracqCost()==null||req.getOuracqCost().equalsIgnoreCase(""))?"0":req.getOuracqCost().replaceAll(",", "")) + a+b+c;
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "AcquisitionCost", "01"));
							
						}else{
							req.setAcquisitionCost(ans);
						}
					}
				}
				final int LoopCount = Integer.parseInt(req.getNoInsurer());
				double totPer = 0.0;
				boolean flag = true;
				if (LoopCount != 0) {
					if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"), "RetentionPercentage", "01"));
						flag = false;
					} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"), "RetentionPercentage", "01"));
						flag = false;
					} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"), "RetentionPercentage", "01"));
						flag = false;
					} else {
						totPer += Double.parseDouble(req.getRetentionPercentage());
					}
				}
				//RetroListReq retroReq = new RetroListReq();
				boolean dupCheck = true;
				for (int i = 0; i < LoopCount; i++) {
					RetroList retroReq =req.getRetroList().get(i);
					if ("".equals(retroReq.getRetroYear())) {
						list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"RetroYear","01"));
						dupCheck = false;
					}
					if(retroReq.getRetroCeding() ==null){
						list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
						dupCheck = false;
					}
					else if (StringUtils.isBlank(retroReq.getRetroCeding())) {
						list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
						dupCheck = false;
					}
			
					
					if (validation.isNull(retroReq.getPercentRetro()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.Required")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.invalid")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.greater")+String.valueOf(i + 1),"RetroPercentahge","01"));
						flag = false;
					} else {
						totPer += Double.parseDouble(retroReq.getPercentRetro());
					}

					//RiskDetailsBean bean = new RiskDetailsBean();
					
					req.setProductId("4");
					req.setRetroType("TR");
				}
				if (dupCheck) {
					for (int i = 0; i < LoopCount - 1; i++) {
						for (int j = i + 1; j < LoopCount; j++) {
							RetroList retroReq =req.getRetroList().get(i);
							if (retroReq.getRetroCeding().equalsIgnoreCase(retroReq.getRetroCeding())) {
								list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(i + 1),"RetroContract","01"));
							}
						}
					}
				}
				if (LoopCount != 0) {
					if (flag) {
						DecimalFormat df = new DecimalFormat("#.##");
						totPer=Double.parseDouble(df.format(totPer));
						if (totPer != 100) {
							list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"), "totPercentage", "01"));
						}
					}
				}
				GetCrestaCountReq req1 =new GetCrestaCountReq();
				req1.setAmendId(req.getAmendId());
				req1.setBranchCode(req.getBranchCode());
				req1.setProposalNo(req.getProposalno());
				
				if(req.getCrestaStatus().equalsIgnoreCase("Y")){
					if(StringUtils.isBlank(req.getCrestaPopUp())){
						list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"), "popup", "01"));
					}
					else if(propImple.getCrestaCount(req1)==0){
						list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"), "creasta", "01"));
					}
				}
				GetBonusListCountReq req2 = new GetBonusListCountReq();
				req2.setAmendId(req.getAmendId());
				req2.setBranchCode(req.getBranchCode());
				req2.setProposalNo(req.getProposalno());
				req2.setLayerNo(req.getLayerNo());

				if(StringUtils.isBlank(req.getSlideScaleCommission())){
					list.add(new ErrorCheck(prop.getProperty("error.slidescale.commission"), "slidescale", "01"));
					}
					else if("Y".equalsIgnoreCase(req.getSlideScaleCommission())){
						if(StringUtils.isBlank(req.getSlidePopUp())){
							list.add(new ErrorCheck(prop.getProperty("error.slide.recheck"), "slide", "01"));
						}else{
						int count = propImple.getBonusListCount(req2,"scale");
						if(count<=0){
							list.add(new ErrorCheck(prop.getProperty("slide.error.lcb.table.empty"), "BonusListCount", "01"));
						}
						}
					}
				if(StringUtils.isBlank(req.getBaseLayer())){
					if(StringUtils.isBlank(req.getSlidecommissionSubClass())){
						list.add(new ErrorCheck(prop.getProperty("slide.profit.commission.sub.class"), "SlidecommissionSubClass", "01"));
					}
				}
				
					if(StringUtils.isBlank(req.getLossParticipants())){
						list.add(new ErrorCheck(prop.getProperty("error.losspart"), "LossParticipants", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getLossParticipants())){
							if(StringUtils.isBlank(req.getLossPopUp())){
								list.add(new ErrorCheck(prop.getProperty("error.loss.recheck"), "LossPopUp", "01"));
							}else{
							int count = propImple.getBonusListCount(req2,"lossparticipates");
							if(count<=0){
								list.add(new ErrorCheck(prop.getProperty("losspart.error.lcb.table.empty"), "BonusListCount", "01"));
							}
							}
						}
					if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getLosscommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("loss.profit.commission.sub.class"), "BaseLayer", "01"));
						}
					}
					if(StringUtils.isBlank(req.getShareProfitCommission())){
						list.add(new ErrorCheck(prop.getProperty("errors.share_Profit_Commission.empty"), "ShareProfitCommission", "01"));
					}
					if(StringUtils.isBlank(req.getBaseLayer())){
					if(StringUtils.isBlank(req.getCommissionSubClass())){
						list.add(new ErrorCheck(prop.getProperty("profit.commission.sub.class"), "ShareProfitCommission", "01"));
					}
					}

					if("1".equalsIgnoreCase(req.getShareProfitCommission())){
						if(StringUtils.isBlank(req.getManagementExpenses())){
							list.add(new ErrorCheck(prop.getProperty("man.exp.req"), "ManagementExpenses", "01"));
						}
						if(StringUtils.isBlank(req.getCommissionType())){
							list.add(new ErrorCheck(prop.getProperty("com.type.req"), "CommissionType", "01"));
											}
						else if("PC".equalsIgnoreCase(req.getCommissionType())){
						
						 if (Double.parseDouble(req.getProfitCommissionPer())>100){
							list.add(new ErrorCheck(prop.getProperty("profit.com.less.hundred"), "ProfitCommissionPer", "01"));
						}
						if(StringUtils.isBlank(req.getSuperProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("error.super.pro.com"), "SuperProfitCommission", "01"));
						}else{
							if("Y".equalsIgnoreCase(req.getSuperProfitCommission())){
								if(StringUtils.isBlank(req.getProfitPopUp())){
									list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"), "SuperProfitCommission", "01"));
								}else{
									int count = propImple.CommissionTypeCount(req.getProposalno(),req.getBranchCode(),req.getCommissionType());
									if(count<=0){
										list.add(new ErrorCheck(prop.getProperty("error.commission.schedule"), "commission", "01"));
									}
							}
							}
						}
						}
						else if("PR".equalsIgnoreCase(req.getCommissionType()) || "LR".equalsIgnoreCase(req.getCommissionType()) ){
						if(StringUtils.isBlank(req.getSetup())){
							list.add(new ErrorCheck(prop.getProperty("error.setup.req"), "setup", "01"));
						}
						if(StringUtils.isBlank(req.getProfitPopUp())){
							list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"), "ProfitPopUp", "01"));
						}else{
						int count = propImple.CommissionTypeCount(req.getProposalno(),req.getBranchCode(),req.getCommissionType());
						if(count<=0){
							list.add(new ErrorCheck(prop.getProperty("error.commission.setup.schedule"), "CommissionTypeCount", "01"));
						}
						}
						}

						if(StringUtils.isBlank(req.getLossCarried())){
							
						}else if(!"TE".equalsIgnoreCase(req.getLossCarried())){
							if(StringUtils.isBlank(req.getLossyear())){
								list.add(new ErrorCheck(prop.getProperty("error.loss.year"), "Lossyear", "01"));
							}else if(Integer.parseInt(req.getLossyear())>100){
								list.add(new ErrorCheck(prop.getProperty("loss.carried.yeas.less.hundred"), "Lossyear", "01"));
							}
						}
						
						else if(!"TE".equalsIgnoreCase(req.getProfitCarried())){
							if(StringUtils.isBlank(req.getProfitCarriedForYear())){
								list.add(new ErrorCheck(prop.getProperty("profit.carried.year.req"), "ProfitCarriedForYear", "01"));
							}else if(Integer.parseInt(req.getProfitCarriedForYear())>100){
								list.add(new ErrorCheck(prop.getProperty("profit.carried.yeas.less.hundred"), "ProfitCarriedForYear", "01"));
							}
						}
						if(StringUtils.isBlank(req.getSubSeqCalculation())){
							list.add(new ErrorCheck(prop.getProperty("error.sub.seq.cal.req"), "SubSeqCalculation", "01"));
						}if(StringUtils.isBlank(req.getProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("error.profit.commission.req"), "ProfitCommission", "01"));
						}
					}
				if(StringUtils.isBlank(req.getPremiumReserve())){
					list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second"), "PremiumReserve", "01"));
				} else {
					if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second1"), "PremiumReserve", "01"));
					} else if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondless"), "PremiumReserve", "01"));
					} else if (validation.percentageValid(req.getPremiumReserve()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondgreater"), "PremiumReserve", "01"));
					}
				}
				if(StringUtils.isBlank(req.getLossreserve())){
					list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second"), "Lossreserve", "01"));
				} else {
					if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second1"), "Lossreserve", "01"));
					}else if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondless"), "Lossreserve", "01"));
					} else if (validation.percentageValid(req.getLossreserve()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondgreater"), "Lossreserve", "01"));
					}
				}
				if(StringUtils.isBlank(req.getInterest())){
					list.add(new ErrorCheck(prop.getProperty("errors.interest.second"), "", "01"));
				} else {
					if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("INVALID")){
						list.add(new ErrorCheck(prop.getProperty("errors.interest.second1"), "interest", "01"));
					} else if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.interest.secondless"), "interest", "01"));
					} else if (validation.percentageValid(req.getInterest()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.interest.secondgreater"), "interest", "01"));
					}
				}
				if(StringUtils.isBlank(req.getPortfolioinoutLoss())){
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second"), "PortfolioinoutLoss", "01"));
				} else {

					if (validation.percentageValid(req.getPortfolioinoutLoss()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second1"), "PortfolioinoutLoss", "01"));
					} else if (validation.percentageValid(req.getPortfolioinoutLoss()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.secondgreater"), "PortfolioinoutLoss", "01"));
					}
				}
				if(StringUtils.isBlank(req.getPortfolioinoutPremium())){
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.second"), "PortfolioinoutPremium", "01"));
				} else {
					if (validation.percentageValid(req.getPortfolioinoutPremium()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.second"), "PortfolioinoutPremium", "01"));
					} else if (validation.percentageValid(req.getPortfolioinoutPremium()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.greater"), "PortfolioinoutPremium", "01"));
					}
				}
				if (StringUtils.isBlank(req.getLossAdvise())) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second"), "LossAdvise", "01"));
				} else if (validation.isValidNo(req.getLossAdvise().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second1"), "LossAdvise", "01"));
				}
				else{
					req.setLossAdvise((req.getLossAdvise()).replaceAll(",", ""));
				}
				if(StringUtils.isBlank(req.getCashLossLimit())){

					list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.second"), "CashLossLimit", "01"));
				} else {
					req.setCashLossLimit((req.getCashLossLimit()).replaceAll(",", ""));
					if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
					if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
					} else if (!validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getLimitOrigCur())))
						list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
					}
					else if(StringUtils.isBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getTreatyLimitsurplusOC())))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
						}
					else if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
						int t=Double.compare(Double.parseDouble(req.getLimitOrigCur()), Double.parseDouble(req.getTreatyLimitsurplusOC()));
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!(validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")||validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID"))	&& (Double.parseDouble(req.getCashLossLimit()) > (Double.parseDouble(t>0?req.getLimitOrigCur():req.getTreatyLimitsurplusOC()))))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "CashLossLimit", "01"));
						}
				}


				if(StringUtils.isBlank(req.getEventlimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.event_limit.second"), "Eventlimit", "01"));
				}else{
					req.setEventlimit(req.getEventlimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getEventlimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.event_limit.invalid"), "Eventlimit", "01"));
					}
				}
				if(StringUtils.isBlank(req.getAggregateLimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.second"), "AggregateLimit", "01"));
				}else{
					req.setAggregateLimit(req.getAggregateLimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getAggregateLimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.invalid"), "", "01"));
					}
				}
				if(StringUtils.isBlank(req.getOccurrentLimit())){
					list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.second"), "OccurrentLimit", "01"));
				}else{
					req.setOccurrentLimit(req.getOccurrentLimit().replaceAll(",", ""));
					if (validation.isValidNo(req.getOccurrentLimit().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.invalid"), "OccurrentLimit", "01"));
					}
				}

		
				if (validation.isNull(req.getLeaderUnderwriter())
						.equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second"), "LeaderUnderwriter", "01"));
				}
				if("RI02".equalsIgnoreCase(req.getSourceId())){
				if(StringUtils.isBlank(req.getLeaderUnderwritercountry())){
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.country"), "LeaderUnderwritercountry", "01"));
				}
				}
				if(StringUtils.isBlank(req.getLeaderUnderwritershare())){
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.share"), "Underwritershare", "01"));
				}
				if (StringUtils.isNotBlank(req.getLeaderUnderwritershare())) {
					if (validation.percentageValid(	req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second"), "LeaderUnderwritershare", "01"));
					} else if (validation.percentageValid(req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.greater"), "LeaderUnderwritershare", "01"));
					}
				}if(StringUtils.isNotBlank(req.getLeaderUnderwriter()) &&    !"64".equalsIgnoreCase(req.getLeaderUnderwriter())){
					 if(propImple.GetShareValidation(req.getProposalno(),req.getLeaderUnderwritershare())){
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.greater.signed"), "LeaderUnderwritershare", "01"));
					} 
				}else{
					if(dropDownImple.GetShareEqualValidation(req.getProductId(),req.getLeaderUnderwritershare(),req.getProposalno())){
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.equals.signed"), "LeaderUnderwritershare", "01"));
					}
				}
				if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
					list.add(new ErrorCheck(prop.getProperty("doc.status"), "EndorsementStatus", "01"));
				}
//				if(StringUtils.isBlank(req.getRetroDupContract())){
//					list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
//				}
				//validationRemarks();

			}
			else{
				//validateSaveSecondPage();
					if("3".equalsIgnoreCase(req.getTreatyType()) ||"1".equalsIgnoreCase(req.getTreatyType())  ||"4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
					if (StringUtils.isNotBlank(req.getPremiumQuotaShare()) ){
						req.setPremiumQuotaShare((req.getPremiumQuotaShare()).replaceAll(",", ""));
						if (validation.isValidNo(req.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
							if("4".equalsIgnoreCase(req.getTreatyType()) ||"5".equalsIgnoreCase(req.getTreatyType())){
								list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Obj.Invalid"), "TreatyType", "01"));
							}
							else{
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Invalid"), "PremiumQuotaShare", "01"));
							}
						}
					}
					}
					if("3".equalsIgnoreCase(req.getTreatyType()) ||"2".equalsIgnoreCase(req.getTreatyType()) ){

					if (StringUtils.isBlank(req.getPremiumSurplus())) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.reqired"), "PremiumQuotaShare", "01"));
					} else {
						req.setPremiumSurplus((req.getPremiumSurplus()).replaceAll(",", ""));
						
						if (validation.isValidNo(req.getPremiumSurplus()).equalsIgnoreCase("INVALID")) {
							
							list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Invalid"), "PremiumSurplus", "01"));
						}
					//	String ans = calcu.calculatePTTY(req,"Surplus",0);
						String premiumRate=StringUtils.isBlank(req.getEpiOSViewOC())?"0":req.getEpiOSViewOC().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) -  Double.parseDouble(coverlimit));
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getPremiumSurplus().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "PremiumSurplus", "01"));
							
						}else{
							req.setPremiumSurplus(ans);
						}
					}
					}

					if("2".equalsIgnoreCase(req.getInwardType())){
						if(StringUtils.isBlank(req.getOrginalacqcost())){
							list.add(new ErrorCheck(prop.getProperty("Errors.org.cost.Invalid"), "Orginalacqcost", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getOrginalacqcost())){
						if(StringUtils.isNotBlank(req.getOurassessmentorginalacqcost())){
							req.setOurassessmentorginalacqcost(req.getOurassessmentorginalacqcost().replaceAll(",",""));
						}
						if(StringUtils.isNotBlank(req.getOuracqCost())){
							req.setOuracqCost(req.getOuracqCost().replaceAll(",",""));
						}
						}
					}
					if(StringUtils.isBlank(req.getLocRate())){
						list.add(new ErrorCheck(prop.getProperty("label.rate.year.error"), "", "01"));
					}
					if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
					req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalno())));
					if(StringUtils.isNotBlank(req.getAccDate()) && StringUtils.isNotBlank( req.getPreviousendoDate())) {
						req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
					}
					final String endorseDate=validation.checkDate(req.getEndorsementDate());
					if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"), "Endorsmenttype", "01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.rectification.required"), "rectification", "01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.required"), "gnpiDate", "01"));
						}
						
					} else if (val.isNull(endorseDate).equalsIgnoreCase("INVALID")) {
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"), "Endorsmenttype", "01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.rectification.check"), "rectification", "01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.check"), "gnpiDate", "01"));
						}
					}
					else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
						//list.add(new ErrorCheck(prop.getProperty("errors.endoDate.invalid"));
						if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementDate","01"));
						}
						else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementType","01"));
						}
						else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							list.add(new ErrorCheck(prop.getProperty(("errors.gnpiDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"EndorsementType","01"));
						}
					}
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")){
						if(dropDownImple.Validatethree(req.getBranchCode(), req.getEndorsementDate())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo")+openPeriodRes.getOpenPeriodDate(),"EndorsementDate","01"));
						}
					}
					}
					if (validation.isNull(req.getAcquisitionCost())
							.equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
					} else {
						req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
						if (validation.isValidNo(req.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"), "AcquisitionCost", "01"));
						}else{
							//String ans = calcu.calculatePTTY(req,"AcqCost",0);
							double a=0,b=0,c=0;
							if("3".equalsIgnoreCase(req.getTreatyType())){
								a = (((Double.parseDouble(req.getPremiumQuotaShare().equalsIgnoreCase("")?"0":req.getPremiumQuotaShare().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", "")))/100 ;
								b = (((Double.parseDouble(req.getPremiumSurplus().equalsIgnoreCase("")?"0":req.getPremiumSurplus().replaceAll(",", "")) *Double.parseDouble(req.getShareValue().replaceAll(",", "")))/100)*Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", "")))/100 ;
								c = (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *(Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}else if("1".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType())||"4".equalsIgnoreCase(req.getTreatyType())){
								c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionQS().equalsIgnoreCase("")?"0":req.getCommissionQS().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}else if("2".equalsIgnoreCase(req.getTreatyType())){
								c= (Double.parseDouble(req.getEpiOSViewOC().equalsIgnoreCase("")?"0":req.getEpiOSViewOC().replaceAll(",", "")) *( Double.parseDouble(req.getCommissionsurp().equalsIgnoreCase("")?"0":req.getCommissionsurp().replaceAll(",", ""))+Double.parseDouble(req.getOverRidder().equalsIgnoreCase("")?"0":req.getOverRidder().replaceAll(",", "")) +Double.parseDouble(req.getBrokerage().equalsIgnoreCase("")?"0":req.getBrokerage().replaceAll(",", "")) +Double.parseDouble(req.getTax().equalsIgnoreCase("")?"0":req.getTax().replaceAll(",", "")) + Double.parseDouble(req.getOthercost().equalsIgnoreCase("")?"0":req.getOthercost().replaceAll(",", ""))))/100;
							}
							amt = Double.parseDouble((req.getOuracqCost()==null||req.getOuracqCost().equalsIgnoreCase(""))?"0":req.getOuracqCost().replaceAll(",", "")) + a+b+c;
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
								if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "AcquisitionCost", "01"));
								
							}else{
								req.setAcquisitionCost(ans);
							}
						}
					}
					final int LoopCount = Integer.parseInt(req.getNoInsurer());
					double totPer = 0.0;
					boolean flag = true;
					if (LoopCount != 0) {
						if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"), "RetentionPercentage", "01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"), "RetentionPercentage", "01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"), "RetentionPercentage", "01"));
							flag = false;
						} else {
							totPer += Double.parseDouble(req.getRetentionPercentage());
						}
					}
					boolean dupCheck = true;
					for (int i = 0; i < LoopCount; i++) {
						RetroList retroReq =req.getRetroList().get(i);
						if ("".equals(retroReq.getRetroYear())) {
							list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"RetroYear","01"));
							dupCheck = false;
						}
						if(retroReq.getRetroCeding() ==null){
							list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
							dupCheck = false;
						}
						else if (StringUtils.isBlank(retroReq.getRetroCeding())) {
							list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
							dupCheck = false;
						}
				
						
						if (validation.isNull(retroReq.getPercentRetro()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.Required")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.invalid")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else if (validation.percentageValid(retroReq.getPercentRetro()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetroPercentahge.greater")+String.valueOf(i + 1),"RetroPercentahge","01"));
							flag = false;
						} else {
							totPer += Double.parseDouble(retroReq.getPercentRetro());
						}

						//RiskDetailsBean bean = new RiskDetailsBean();
						
						req.setProductId("4");
						req.setRetroType("TR");
					}
					if (dupCheck) {
						for (int i = 0; i < LoopCount - 1; i++) {
							for (int j = i + 1; j < LoopCount; j++) {
								RetroList retroReq =req.getRetroList().get(i);
								if (retroReq.getRetroCeding().equalsIgnoreCase(retroReq.getRetroCeding())) {
									list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(i + 1),"RetroContract","01"));
								}
							}
						}
					}
					if (LoopCount != 0) {
						if (flag) {
							DecimalFormat df = new DecimalFormat("#.##");
							totPer=Double.parseDouble(df.format(totPer));
							if (totPer != 100) {
								list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"), "totPercentage", "01"));
							}
						}
					}
					GetCrestaCountReq req1 =new GetCrestaCountReq();
					req1.setAmendId(req.getAmendId());
					req1.setBranchCode(req.getBranchCode());
					req1.setProposalNo(req.getProposalno());
					if(req.getCrestaStatus().equalsIgnoreCase("Y")){
						if(StringUtils.isBlank(req.getCrestaPopUp())){
							list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"), "popup", "01"));
						}
						
						else if(propImple.getCrestaCount(req1)==0){
							list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"), "creasta", "01"));
						}
					}
					GetBonusListCountReq req2 = new GetBonusListCountReq();
					req2.setAmendId(req.getAmendId());
					req2.setBranchCode(req.getBranchCode());
					req2.setProposalNo(req.getProposalno());
					req2.setLayerNo(req.getLayerNo());

					if(StringUtils.isBlank(req.getSlideScaleCommission())){
						list.add(new ErrorCheck(prop.getProperty("error.slidescale.commission"), "slidescale", "01"));
						}
						else if("Y".equalsIgnoreCase(req.getSlideScaleCommission())){
							if(StringUtils.isBlank(req.getSlidePopUp())){
								list.add(new ErrorCheck(prop.getProperty("error.slide.recheck"), "SlidePopUp", "01"));
							}else{
							int count = propImple.getBonusListCount(req2,"scale");
							if(count<=0){
								list.add(new ErrorCheck(prop.getProperty("slide.error.lcb.table.empty"), "table.empty", "01"));
							}
							}
						}
					if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getSlidecommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("slide.profit.commission.sub.class"), "profit.commission", "01"));
						}
					}
						if(StringUtils.isBlank(req.getLossParticipants())){
							list.add(new ErrorCheck(prop.getProperty("error.losspart"), "LossParticipants", "01"));
							}
							else if("Y".equalsIgnoreCase(req.getLossParticipants())){
								if(StringUtils.isBlank(req.getLossPopUp())){
									list.add(new ErrorCheck(prop.getProperty("error.loss.recheck"), "LossPopUp", "01"));
								}else{
								int count = propImple.getBonusListCount(req2,"lossparticipates");
								if(count<=0){
									list.add(new ErrorCheck(prop.getProperty("losspart.error.lcb.table.empty"), "lossparticipates", "01"));
								}
								}
							}
						if(StringUtils.isBlank(req.getBaseLayer())){
							if(StringUtils.isBlank(req.getLosscommissionSubClass())){
								list.add(new ErrorCheck(prop.getProperty("loss.profit.commission.sub.class"), "LosscommissionSubClass", "01"));
							}
						}
						if(StringUtils.isBlank(req.getShareProfitCommission())){
							list.add(new ErrorCheck(prop.getProperty("errors.share_Profit_Commission.empty"), "ShareProfitCommission", "01"));
						}
						if(StringUtils.isBlank(req.getBaseLayer())){
						if(StringUtils.isBlank(req.getCommissionSubClass())){
							list.add(new ErrorCheck(prop.getProperty("profit.commission.sub.class"), "CommissionSubClass", "01"));
						}
						}

						
					if (StringUtils.isNotBlank(req.getLossAdvise())) {
						
						req.setLossAdvise((req.getLossAdvise()).replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getCashLossLimit())){
						req.setCashLossLimit((req.getCashLossLimit()).replaceAll(",", ""));
						if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isBlank(req.getTreatyLimitsurplusOC())){
						if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
						} else if (!validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getLimitOrigCur())))
							list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "LimitOrigCur", "01"));
						}
						else if(StringUtils.isBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
							if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "GetShareEqualValidationRes", "01"));
							} else if (!validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID")	&& (Double.parseDouble(req.getCashLossLimit()) > Double.parseDouble(req.getTreatyLimitsurplusOC())))
								list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "TreatyLimitsurplusOC", "01"));
							}
						else if(StringUtils.isNotBlank(req.getLimitOrigCur()) && StringUtils.isNotBlank(req.getTreatyLimitsurplusOC())){
							int t=Double.compare(Double.parseDouble(req.getLimitOrigCur()), Double.parseDouble(req.getTreatyLimitsurplusOC()));
							if (validation.isValidNo(req.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid"), "CashLossLimit", "01"));
							} else if (!(validation.isValidNo(req.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID")||validation.isValidNo(req.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID"))	&& (Double.parseDouble(req.getCashLossLimit()) > (Double.parseDouble(t>0?req.getLimitOrigCur():req.getTreatyLimitsurplusOC()))))
								list.add(new ErrorCheck(prop.getProperty("errors.cashLimitGrTreatyLimit"), "LimitOrigCur", "01"));
							}
					}
					if(StringUtils.isNotBlank(req.getEventlimit())){
						req.setEventlimit(req.getEventlimit().replaceAll(",", ""));
						
					}
					if(StringUtils.isNotBlank(req.getAggregateLimit())){
						req.setAggregateLimit(req.getAggregateLimit().replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getOccurrentLimit())){
					
						req.setOccurrentLimit(req.getOccurrentLimit().replaceAll(",", ""));
					}
					if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
						list.add(new ErrorCheck(prop.getProperty("doc.status"), "EndorsementStatus", "01"));
					}
//					if(StringUtils.isBlank(req.getRetroDupContract())){
//						list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
//					}
				//	validationRemarks();
				
				
			
			}
		return list;
	}
}