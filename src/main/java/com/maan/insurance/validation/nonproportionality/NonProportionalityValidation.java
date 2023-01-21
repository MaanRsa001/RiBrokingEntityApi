package com.maan.insurance.validation.nonproportionality;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmount;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmountreq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitOCReq;
import com.maan.insurance.model.req.nonproportionality.CrestaSaveReq;
import com.maan.insurance.model.req.nonproportionality.GetLayerInfoReq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmount;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmountreq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitOCReq;
import com.maan.insurance.model.req.nonproportionality.CoverList;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertBonusDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertIEModuleReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;
import com.maan.insurance.model.req.nonproportionality.MoveReinstatementMainReq;
import com.maan.insurance.model.req.nonproportionality.NoRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.ReInstatementMainInsertReq;
import com.maan.insurance.model.req.nonproportionality.ReinstatementNoList;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.SaveSecondPageReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.nonproportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageData1Req;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.nonproportionality.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.nonproportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.nonproportionality.coverLimitOC;
import com.maan.insurance.model.req.nonproportionality.insertClassLimitReq;
import com.maan.insurance.model.req.nonproportionality.insertProportionalTreatyReq;
import com.maan.insurance.model.req.proportionality.ContractReq;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;
import com.maan.insurance.model.req.proportionality.RetroListReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.DropDown.GetContractValRes;
import com.maan.insurance.model.res.DropDown.GetContractValidationRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes1;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.nonproportionality.NonProportionalityServiceImpl;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Validation;
@Service
public class NonProportionalityValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropDownImple;
	
	@Autowired
	private NonProportionalityServiceImpl nonPropImple;
	
	@Autowired
	private CommonCalculation calcu;
	public NonProportionalityValidation() {
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
	public List<ErrorCheck> showSecondPageData1Vali(ShowSecondPageData1Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter Proposal", "Proposal", "3"));
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
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
	public List<ErrorCheck> getRetroContractDetailsVali(GetRetroContractDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "3"));
		}
		
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "4"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "2"));
		}
		return list;
	}
	public List<ErrorCheck> showRetroCess1Vali(ShowRetroCess1Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "2"));
		}
		
		if (StringUtils.isBlank(req.getNoRetroCess())) {
			list.add(new ErrorCheck("Please Enter NoRetroCess", "NoRetroCess", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		if (req.getMode()==null) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "5"));
		}
		if (CollectionUtils.isEmpty(req.getRetroCessReq())) {
			list.add(new ErrorCheck("Please Enter RetroCessList", "RetroCessList", "6"));
		}
		return list;
	}
	public List<ErrorCheck> updateProportionalTreatyVali(UpdateProportionalTreatyReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		boolean flags = true;
		boolean cedCheck = true;
		boolean cedflag = true;
		final Validation val = new Validation();
		double amt = 0;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		try {
		if("A".equalsIgnoreCase(req.getProStatus())|| "5".equalsIgnoreCase(req.getProductId())){
			//validatenext();
			
			if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0 && "3".equalsIgnoreCase(req.getProductId())){
				if(StringUtils.isBlank(req.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
				}
			}
			if (val.isSelect(req.getDepartmentId()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"DepartmentId","01"));
			}
			if (val.isSelect(req.getSubProfitCenter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitCenter","01"));
			}else{
				req.setSubProfitCenter((req.getSubProfitCenter()).replaceAll(" ", ""));
			}
			if (val.isSelect(req.getProfitCenter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.Profit_Center.required"),"ProfitCenter","01"));
			}
			if("3".equalsIgnoreCase(req.getProductId())){
			if(StringUtils.isBlank(req.getInwardType())){
				if("RI01".equalsIgnoreCase(req.getSourceId())){
					list.add(new ErrorCheck(prop.getProperty("error.InwardType.required"),"InwardType","01"));
				}
				else{
					list.add(new ErrorCheck(prop.getProperty("error.InwardType.required02"),"InwardType","01"));
				}
			}
			}
			if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
			}
			if("3".equalsIgnoreCase(req.getProductId())){
			if (StringUtils.isBlank(req.getMaxLimitProduct())) {
				list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct","01"));
				cedCheck = false;
			} else {
				req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
				if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct","01"));
					cedCheck = false;
				} else {
					GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getProductId(), "0");
					String uwLimit=commonRes.getCommonResponse();
					uwLimit=uwLimit.replaceAll(",", "");
					if (Double.parseDouble(uwLimit) == 0) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct","01"));
						cedCheck = false;
					} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"MaxLimitProduct","01"));
						cedCheck = false;
					}
				}
			}
			}
			if (val.isNull(req.getPolicyBranch()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch","01"));
			}
			if(StringUtils.isBlank(req.getBusinessType())){
				list.add(new ErrorCheck(prop.getProperty("error.BusinessType.required"),"BusinessType","01"));
			}
			if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
			}
			if (StringUtils.isBlank(req.getBroker())) {
				list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker","01"));
			}
			if("3".equalsIgnoreCase(req.getProductId()))
			if (StringUtils.isBlank(req.getTreatyNameType())) {
				list.add(new ErrorCheck(prop.getProperty("error.treatyName_type.required"),"TreatyNameType","01"));
			}
			else if("5".equalsIgnoreCase(req.getProductId())){
				list.add(new ErrorCheck(prop.getProperty("error.retroTreatyName.required"),"TreatyNameType","01"));
			}
			if (val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"layerNo","01"));
			} else if (val.isValidNo(req.getLayerNo()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"layerNo","01"));
			}
			Map<String, Object> map = null;
			List<Map<String, Object>> list1 = nonPropImple.getValidation(req.getIncepDate(),req.getRenewalcontractNo());
			if (list1 != null && list1.size() > 0) {
				map = (Map<String, Object>) list1.get(0);
			}
			if (val.isNull(req.getIncepDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
			} else if (val.checkDate(req.getIncepDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
			} else if (StringUtils.isNotBlank((req.getRenewalcontractNo()))&& !"0".equals(req.getRenewalcontractNo())&& map != null) {
				if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getIncepDate()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"incepDate","01"));
				}else {
					req.setRenewalFlag("NEWCONTNO");
				}
			}
			if (val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate","01"));
			} else if (val.checkDate(req.getExpDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
			}
			if (!val.isNull(req.getIncepDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
				if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"expDate","01"));
				}
			}
			if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
			} else if (!"".equals(req.getRenewalcontractNo()) && !"0".equals(req.getRenewalcontractNo()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(req.getUwYear())) {
				
			}
			if(StringUtils.isNotBlank(req.getProStatus()) &&"A".equalsIgnoreCase(req.getProStatus())){
			if (StringUtils.isBlank(req.getAccDate())) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.required"),"accDate","01"));
			}
			}
			 if (!val.isNull(req.getAccDate()).equalsIgnoreCase("") &&val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
			}
			if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !"endorsment".equalsIgnoreCase(req.getEdit())){
				if(dropDownImple.Validatethree(req.getBranchCode(), req.getAccDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
			}
			if (!val.isNull(req.getExpDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
				if (Validation.ValidateTwo(req.getAccDate(), req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"accDate","01"));
				}
			}
			if (StringUtils.isNotBlank(req.getIncepDate())&& StringUtils.isNotBlank(req.getExpDate())) {
				if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
				}
			}
			if (StringUtils.isBlank(val.isSelect(req.getOrginalCurrency()))) {
				list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
			}
			
			if (StringUtils.isBlank(req.getExchRate())) {
				list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate","01"));
				cedCheck = false;
			} else if (val.isValidNo(req.getExchRate().trim().toString()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate","01"));
				cedCheck = false;
			}
			double maxlimit = 0.0;
			boolean spflag = true;
			if ("3".equals(req.getProductId())) {
				
				if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
					spflag = false;
				}
				if (val.isNull(req.getNoInsurer()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.Required"),"NoInsurer","01"));
				} else if (val.isValidNo(req.getNoInsurer()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.NumberFormat"),"NoInsurer","01"));
				} else if (spflag && "Y".equals(req.getSpRetro()) && Integer.parseInt(req.getNoInsurer()) <= 0) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.gr0"),"NoInsurer","01"));
				}
				if("4".equals(req.getDepartmentId())|| "2".equals(req.getDepartmentId()) || "10".equals(req.getDepartmentId())){
					if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC","01"));
					}else{
						req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC","01"));
						}
					}
					if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC","01"));
					}else{
						req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC","01"));
						}
					}
				}
			}
				if(StringUtils.isBlank(req.getTerritory())){
					list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode","01"));
				}if(StringUtils.isBlank(req.getCountryIncludedList())){
					list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude","01"));
				}
			
			if (StringUtils.isBlank(req.getTerritoryscope())) {
				list.add(new ErrorCheck(prop.getProperty("error.terrtoryScope.required"),"terrtoryScope","01"));
			}
			if (StringUtils.isBlank(req.getPortfoloCovered())) {
				list.add(new ErrorCheck(prop.getProperty("error.portfoloCovered.required"),"portfoloCovered","01"));
			}
			if (StringUtils.isBlank(req.getBasis())) {
				list.add(new ErrorCheck(prop.getProperty("error.basic.required"),"basic","01"));
			}
			if("3".equals(req.getProductId()) || "5".equals(req.getProductId())){
				
				if(StringUtils.isBlank(req.getLOCIssued())){
					list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
				}
				else if("Y".equalsIgnoreCase(req.getLOCIssued())){
					if(StringUtils.isBlank(req.getLocBankName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank","01"));
					}
					if(StringUtils.isBlank(req.getLocCreditPrd())){
						list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd","01"));
					}
					if(StringUtils.isBlank(req.getLocCreditAmt())){
						list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt","01"));
					}
					else{
						req.setLocCreditAmt(req.getLocCreditAmt().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(req.getLocBeneficerName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName","01"));
					}
					
					}
				if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"5".equalsIgnoreCase(req.getBusinessType()))){
					for(int i=0;i<req.getCoverLimitOCReq().size();i++){
						CoverLimitOCReq req2 = req.getCoverLimitOCReq().get(i);
						if(StringUtils.isBlank(req2.getCoverdepartId())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
						}
						if(StringUtils.isBlank(req2.getCoverLimitOC())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
						}
						if(StringUtils.isBlank(req2.getDeductableLimitOC())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
						}
						if(StringUtils.isBlank(req2.getEgnpiAsPerOff()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
						}
						if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							if(StringUtils.isBlank(req2.getGnpiAsPO()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));
							}
						}
					}
//					if(DropDownControllor.findDuplicates(req.getCoverdepartId()).size()>0){
//						list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"","01"));
//					}
//					if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//						if(req.getCoverdepartId().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//						}
//					}
				}
				if(StringUtils.isNotBlank(req.getBusinessType()) &&("5".equalsIgnoreCase(req.getBusinessType()))){
					for(int i=0;i<req.getCoverLimitAmountReq().size();i++){
						CoverLimitAmountreq req3 = req.getCoverLimitAmountReq().get(i);
						if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitAmountreq()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"CoverLimitAmount","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmountreq())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"CoverLimittPercent","01"));
						}
						if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"DeductableLimitAmount","01"));
						}
						if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"DeductableLimitPercent","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitAmountreq()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"","01"));
						}
						if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOffSlide","01"));
						}
						if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(i + 1),"GnpiAsPOSlide","01"));
							}
						}
						
					}
//					if(DropDownControllor.findDuplicates(req.getCoverdepartIdS()).size()>0){
//						list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"","01"));
//					}
//					if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//						if(req.getCoverdepartIdS().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//						}
//					}
				}
				
				if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"4".equalsIgnoreCase(req.getBusinessType()) && !"5".equalsIgnoreCase(req.getBusinessType()))){
					
				if(!"5".equalsIgnoreCase(req.getBusinessType())){
					
				}
				if(StringUtils.isNotBlank(req.getUmbrellaXL()) && "Y".equalsIgnoreCase(req.getUmbrellaXL())){
				
				if(StringUtils.isBlank(req.getCoverLimitXL())){
					list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required"),"CoverLimitXL","01"));
					}else {
						req.setCoverLimitXL((req.getCoverLimitXL()).replaceAll(",", ""));
						if (val.isValidNo(req.getCoverLimitXL().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required.format"),"CoverLimitXL","01"));
						}
					}
				if(StringUtils.isBlank(req.getDeductLimitXL())){
					list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required"),"DeductLimitXL","01"));
					}else {
						req.setDeductLimitXL((req.getDeductLimitXL()).replaceAll(",", ""));
						if (val.isValidNo(req.getDeductLimitXL().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required.format"),"DeductLimitXL","01"));
						}
					}
				
				}
			}
				
				if("5".equals(req.getProductId())){
				if(StringUtils.isBlank(req.getEgnpiOffer())){
					list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
				}else {
					req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
					if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.egnpi.required.format"),"EgnpiOffer","01"));
					}
				}
				}
				if("3".equals(req.getProductId())){
					if(StringUtils.isBlank(req.getEgnpiOffer())){
						list.add(new ErrorCheck(prop.getProperty("error.egnpias.required"),"EgnpiOffer","01"));
					}else {
						req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
						if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
						}else{
							//calculateXOL
						
							//String ans = calcu.calculateXOL(req,"EGNPIAsper",0,req.getSourceId());
//							if("5".equalsIgnoreCase(req.getBusinessType())){
//								if(req.getEgnpiAsPerOffSlide()!=null){
//									for(int j=0;j<req.getEgnpiAsPerOffSlide().size();j++){
//										amt = amt+Double.parseDouble(bean.getEgnpiAsPerOffSlide().get(i)==null||bean.getEgnpiAsPerOffSlide().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOffSlide().get(i).replaceAll(",", ""));
//									}
//								}
//							}else{
//								if(bean.getEgnpiAsPerOffSlide()!=null){
//									for(int j=0;j<bean.getEgnpiAsPerOff().size();j++){
//										amt = amt+Double.parseDouble(bean.getEgnpiAsPerOff().get(i)==null||bean.getEgnpiAsPerOff().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOff().get(i).replaceAll(",", ""));
//									}
//								}
//							}
//							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
//							
//							if(Double.parseDouble(ans)!=Double.parseDouble(req.getEgnpiOffer().replaceAll(",",""))){
//								
//							}else{
//								req.setEgnpiOffer(ans);
//							}
						}
					}
				if(StringUtils.isBlank(req.getOurAssessment())){
					list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required"),"ourassessment","01"));
				}else {
					if (val.isValidNo(req.getOurAssessment().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required.format"),"ourassessment","01"));
					}
					else{
						//String ans = calcu.calculateXOL(req,"OverAss",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * 100)/Double.parseDouble(coverlimit);
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurAssessment().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"ourassessment","01"));
						
						}else{
							req.setOurAssessment(ans);
						}
					}
				}
				if (StringUtils.isBlank(req.getSubPremium())) {
					list.add(new ErrorCheck(prop.getProperty("error.subPremium.required"),"subPremium","01"));
				} else {
					req.setSubPremium((req.getSubPremium()).replaceAll(",", ""));
					if (val.isValidNo(req.getSubPremium().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.subPremium.required.format"),"subPremium","01"));
					}
					else{
						//String ans = calcu.calculateXOL(req,"EGNPI",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurAssessment())?"0":req.getOurAssessment().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
						}
						String ans  =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getSubPremium().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"subPremium","01"));
						
						}else{
							req.setSubPremium(ans);
						}
					}
				}
				}
				if(StringUtils.isNotBlank(req.getPml()) && "Y".equalsIgnoreCase(req.getPml())){
					if(StringUtils.isBlank(req.getPmlPercent())){
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required"),"PmlPercent","01"));
					} else if (val.isValidNo(req.getPmlPercent().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required.format"),"PmlPercent","01"));
					} else if (val.percentageValid(req.getPmlPercent().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.checkgreater"),"PmlPercent","01"));
					}
					
				}
				if(StringUtils.isBlank(req.getPremiumbasis())){
					list.add(new ErrorCheck(prop.getProperty("error.Premiumbasis.required"),"Premiumbasis","01"));
				}
				else if("1".equalsIgnoreCase(req.getPremiumbasis())){
					if (StringUtils.isBlank(req.getAdjRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"AdjRate","01"));
					} else if (val.isValidNo(req.getAdjRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.adjRate.required.format"),"AdjRate","01"));
					} else if (val.percentageValid(req.getAdjRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.adjrate.checkgreater"),"AdjRate","01"));
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) && "5".equalsIgnoreCase(req.getBusinessType())) {
						if (StringUtils.isBlank(req.getMinimumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required.format"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
						}
						if (StringUtils.isBlank(req.getMaximumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required.format"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
						}
						double min=Double.parseDouble(req.getMinimumRate());
						double max=Double.parseDouble(req.getMaximumRate());
						if(min>max){
							list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"Rate","01"));
						}
					}
				}
				else if("2".equalsIgnoreCase(req.getPremiumbasis())){
					if (StringUtils.isBlank(req.getMinimumRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
					} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
					} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
					}
					if (StringUtils.isBlank(req.getMaximumRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
					} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
					} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MaximumRate","01"));
					}
					double min=Double.parseDouble(req.getMinimumRate());
					double max=Double.parseDouble(req.getMaximumRate());
					if(min>max){
						list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
					}
					if(StringUtils.isBlank(req.getBurningCostLF())){
						list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF.required"),"BurningCostLF","01"));
					}
					if (val.percentageValid(req.getBurningCostLF().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF().checkgreater"),"BurningCostLF","01"));
					}
					
				}
				if (StringUtils.isBlank(req.getEpi())) {
					list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required"),"epiperCent","01"));
				} else {
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required.format"),"epiperCent","01"));
					}else{
						if(!"3".equalsIgnoreCase(req.getPremiumbasis())){
						//	String ans = calcu.calculateXOL(req,"EPI",0,req.getSourceId());

							double a=0;
							if("1".equalsIgnoreCase(req.getPremiumbasis())){
							a =  Double.parseDouble((req.getAdjRate()==null||val.isNull(req.getAdjRate()).equalsIgnoreCase(""))?"0":req.getAdjRate().replaceAll(",", ""));	
							}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
							a =  Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate().replaceAll(",", ""));	
							}
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							if(!"0".equals(premiumRate) ) {
							amt = (Double.parseDouble(premiumRate) * a)/100;
							}
							String ans  =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
						
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
								
							}else{
								req.setEpi(ans);
							}
						}
					}
				}
				if("3".equalsIgnoreCase(req.getPremiumbasis())){
					req.setMinimumRate(req.getEpi());
				}
				if(StringUtils.isBlank(req.getMinPremium())){
					 list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required"),"MinPremium","01"));
					}else {
						req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
						}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
							//String ans = calcu.calculateXOL(req,"MinPremium",0,req.getSourceId());
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							String coverlimit=StringUtils.isBlank(req.getMinimumRate())?"0":req.getMinimumRate().replaceAll(",", "");
							if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
							amt = (Double.parseDouble(premiumRate) *Double.parseDouble(coverlimit))/100;
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getMinPremium().replaceAll(",",""))){
							}else{
								req.setMinPremium(ans);
							}
						}
					}
				if (StringUtils.isBlank(req.getMdPremium())) {
					list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required1"),"MdPremium","01"));
				} else {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					if (val.isValidNo(req.getMdPremium().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required.format"),"MdPremium","01"));
					}
				}
				}
				if (val.isNull(req.getMdInstalmentNumber()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.Instalment.error"),"Instalment","01"));
					
				} else if (val.isValidNo(req.getMdInstalmentNumber()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.Instalment.Required"),"Instalment","01"));
				}
				
				if(StringUtils.isBlank(req.getPaymentDuedays())){
					list.add(new ErrorCheck(prop.getProperty("error.PaymentDuedays.Required"),"PaymentDuedays","01"));
				}
				flags = true;
				double cedPer = 0.0;
				if (StringUtils.isBlank(req.getProStatus())) {
					list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
				}
				if("5".equalsIgnoreCase(req.getProductId())){
					if(StringUtils.isBlank(req.getNoRetroCess())){
						list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession"),"NoRetroCess","01"));
					}
					else if("0".equalsIgnoreCase(req.getNoRetroCess())){
						list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession.greater.than.zero"),"NoRetroCess","01"));
					}
				}
				if (StringUtils.isBlank(req.getShareWritten())) {
					if ("3".equals(req.getProductId())) {
						list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"ShareWritten","01"));
					}		
					flags = false;
					cedCheck = false;
				} else if ("A".equalsIgnoreCase(req.getProStatus()) && val.percentageNewValid(req.getShareWritten().trim()).equalsIgnoreCase("Invalid")) {
					if ("3".equals(req.getProductId())) {
						list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"ShareWritten","01"));
					} 
					flags = false;
					cedCheck = false;
				} else {
					cedPer = Double.parseDouble(req.getShareWritten());
					if ("2".equals(req.getProductId())	&& cedflag	&& Double.parseDouble(req.getShareWritten())	+ Double.parseDouble(req.getCedReten()) > 100) {
						list.add(new ErrorCheck(prop.getProperty("error.SWCedPer.invalid"),"SWCedPer","01"));
					}
				}
				if ("A".equalsIgnoreCase(req.getProStatus())) {
				
					if (StringUtils.isBlank(req.getSharSign())) {
						if ( "3".equals(req.getProductId())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required.pro"),"shareSign","01"));
						} 
						flags = false;
						cedCheck = false;
					} else if (val.percentageNewValid(req.getSharSign().trim()).equalsIgnoreCase("Invalid")) {
						if ("3".equals(req.getProductId())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign","01"));
						} 
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getSharSign());
					}
					if (flags) {
						if (Double.parseDouble(req.getSharSign().equalsIgnoreCase("") ? "0" : req.getSharSign()) > Double.parseDouble(req.getShareWritten().equalsIgnoreCase("") ? "0" : req.getShareWritten())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign","01"));
							cedCheck = false;
						}
					}
					
					if ("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())) {
						if (StringUtils.isBlank(req.getLimitOrigCur())) {
							cedCheck = false;
						} else {
							req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
							if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
								list.add(new ErrorCheck(prop.getProperty("error.limit.check"),"LimitOrigCur","01"));
								cedCheck = false;
							} else {
								amt = Double.parseDouble(req.getLimitOrigCur());
							}
						}
						if (StringUtils.isBlank(req.getDeduchunPercent())) {
							list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.required"),"DeduchunPercent","01"));
						} else {
							req.setDeduchunPercent((req.getDeduchunPercent()).replaceAll(",", ""));
							if (val.isValidNo(req.getDeduchunPercent().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.check"),"DeduchunPercent","01"));
							}
						}
					}
					if ("3".equals(req.getProductId())){
						if (cedCheck) {
							double amount = (amt * (cedPer / 100.0));
							amount = amount / Double.parseDouble(req.getExchRate());
							maxlimit = Double.parseDouble(req.getMaxLimitProduct());
							
							if (amount > maxlimit) {
								list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
							}
						}
					}
					if(StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getSharSign())){
						final  DecimalFormat twoDigit = new DecimalFormat("###0.00");
						final double dvalue = (Double.parseDouble(req.getMdPremium())* (Double.parseDouble(req.getSharSign())) / 100);
						
						final double dround = Math.round(dvalue * 100.0) / 100.0;
						final double valu = Double.parseDouble(twoDigit.format(dround));//A
						if(StringUtils.isNotBlank(req.getContractNo())){
						double sumInst=dropDownImple.getSumOfInstallmentBooked(req.getContractNo(), req.getLayerNo());//B
							if(valu<sumInst){
								list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked.xol"),"installment","01"));
							}
						}
					}if(StringUtils.isNotBlank(req.getMdInstalmentNumber())){
						int count=dropDownImple.getCountOfInstallmentBooked(req.getContractNo(), req.getLayerNo());
						if(Double.parseDouble(req.getMdInstalmentNumber())<count){
							list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked.xol"),"installment","01"));
						}
					}
				}
	
				if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getEpi())) {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("INVALID")) {

						final float mdpremium = Float.parseFloat(req.getMdPremium());
						final float Pepi = Float.parseFloat(req.getEpi());

						if (mdpremium > Pepi) {
							list.add(new ErrorCheck(prop.getProperty("error.mdandpremium.difference.invalid1"),"","01"));
						}
					}
				}
			
			if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getMinPremium())) {
				req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
				req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
				if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {

					final float mdpremium = Float.parseFloat(req.getMdPremium());
					final float minp = Float.parseFloat(req.getMinPremium());
					if (mdpremium > minp) {
						list.add(new ErrorCheck(prop.getProperty("error.mdandminpremium.difference.invalid"),"","01"));
					}
				}
			}
			if (!val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
				if (nonPropImple.getLayerDuplicationCheck(req.getBaseLayer(),req.getLayerNo(),req.getLayerMode()).getResponse().equalsIgnoreCase("true")) {
				
					list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"","01"));
				}
			}
			

//			if("3".equalsIgnoreCase(req.getProductId())){
//				validationContract();
//				}
//			validationRemarks();
		}
		else{
			
				if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0 && ("3".equalsIgnoreCase(req.getProductId()) ||  "5".equalsIgnoreCase(req.getProductId()))){
					if(StringUtils.isBlank(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
					}
				}
				if (val.isSelect(req.getDepartmentId()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"departId","01"));
				}
				if (val.isSelect(req.getSubProfitCenter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitCenter","01"));
				}else{
					req.setSubProfitCenter((req.getSubProfitCenter()).replaceAll(" ", ""));
				}
				
				
				if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
				}
				if("3".equalsIgnoreCase(req.getProductId())){
				if (StringUtils.isBlank(req.getMaxLimitProduct())) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct","01"));
					cedCheck = false;
				} else {
					req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
					if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct","01"));
						cedCheck = false;
					} else {
						GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getProductId(), "0");
						String uwLimit=commonRes.getCommonResponse();
						uwLimit=uwLimit.replaceAll(",", "");
						if (Double.parseDouble(uwLimit) == 0) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct","01"));
							cedCheck = false;
						} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"maxLimitproduct","01"));
							cedCheck = false;
						}
					}
				}
				}
				if (val.isNull(req.getPolicyBranch()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch","01"));
				}
				if(StringUtils.isBlank(req.getBusinessType())){
					list.add(new ErrorCheck(prop.getProperty("error.BusinessType.required"),"BusinessType","01"));
				}
				if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
				}
				if (StringUtils.isBlank(req.getBroker())) {
					list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker","01"));
				}
				
				if (val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"layerNo","01"));
				} else if (val.isValidNo(req.getLayerNo()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"layerNo","01"));
				}
				Map<String, Object> map = null;
				List<Map<String, Object>> list1 = nonPropImple.getValidation(req.getIncepDate(),req.getRenewalcontractNo());
				if (list1 != null && list1.size() > 0) {
					map = (Map<String, Object>) list1.get(0);
				}
				if (val.isNull(req.getIncepDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
				} else if (val.checkDate(req.getIncepDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
				} else if (StringUtils.isNotBlank((req.getRenewalcontractNo()))&& !"0".equals(req.getRenewalcontractNo())&& map != null) {
					if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getIncepDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
					}else {
						req.setRenewalFlag("NEWCONTNO");
					}
				}
				if (val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"ExpiryDate","01"));
				} else if (val.checkDate(req.getExpDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
				}
				if (!val.isNull(req.getIncepDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
					if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
					}
				}
				 if (StringUtils.isNotBlank(req.getAccDate())&&val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
				}
				if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
				} else if (!"".equals(req.getRenewalcontractNo()) && !"0".equals(req.getRenewalcontractNo()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(req.getUwYear())) {
				}
				
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !req.getEdit().equalsIgnoreCase("endorsment") && !val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getAccDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
				}
				if (!val.isNull(req.getExpDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") &&!val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
					if (Validation.ValidateTwo(req.getAccDate(), req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"accDate","01"));
					}
				}
				if (StringUtils.isNotBlank(req.getIncepDate())&& StringUtils.isNotBlank(req.getExpDate())) {
					if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
					}
				}
				if (StringUtils.isBlank(val.isSelect(req.getOrginalCurrency()))) {
					list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
				}
				
				if (StringUtils.isBlank(req.getExchRate())) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate","01"));
					cedCheck = false;
				} else if (val.isValidNo(req.getExchRate().trim().toString()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate","01"));
					cedCheck = false;
				}
				double maxlimit = 0.0;
				boolean spflag = true;
				if ("3".equals(req.getProductId())) {
					
					if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
						spflag = false;
					}
					
					if("4".equals(req.getDepartmentId())|| "2".equals(req.getDepartmentId()) || "10".equals(req.getDepartmentId())){
						if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC","01"));
						}else{
							req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
							if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC","01"));
							}
						}
						if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC","01"));
						}else{
							req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
							if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC","01"));
							}
						}
					}
				}
				if("3".equals(req.getProductId()) || "5".equals(req.getProductId())){
					
					if(StringUtils.isBlank(req.getLOCIssued())){
						list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"5".equalsIgnoreCase(req.getBusinessType()))){
						
						for(int i=0;i<req.getCoverLimitOCReq().size();i++){
							CoverLimitOCReq req2 = req.getCoverLimitOCReq().get(i);
							if(StringUtils.isBlank(req2.getCoverdepartId())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
							}
							if(StringUtils.isBlank(req2.getCoverLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
							}
							if(StringUtils.isBlank(req2.getDeductableLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
							}
							if(StringUtils.isBlank(req2.getEgnpiAsPerOff()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
							}
							if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								if(StringUtils.isBlank(req2.getGnpiAsPO()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));
								}
							}
						}
						
//						if(DropDownControllor.findDuplicates(req.getCoverdepartId()).size()>0){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"));
//						}
//						if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//							if(req.getCoverdepartId().contains(req.getDepartId()) && req.getCoverdepartId().size()>1){
//								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"));
//							}
//						}
						
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) &&("5".equalsIgnoreCase(req.getBusinessType()))){
						for(int i=0;i<req.getCoverLimitAmountReq().size();i++){
							CoverLimitAmountreq req3 = req.getCoverLimitAmountReq().get(i);
							if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitAmountreq()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"CoverLimitAmount","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmountreq())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"CoverLimittPercent","01"));
							}
							if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"DeductableLimitAmount","01"));
							}
							if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"DeductableLimitPercent","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitAmountreq()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"","01"));
							}
							if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOffSlide","01"));
							}
							if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(i + 1),"GnpiAsPOSlide","01"));
								}
							}
							
						}
//						if(DropDownControllor.findDuplicates(req.getCoverdepartIdS()).size()>0){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"));
//						}
//						if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//							if(req.getCoverdepartIdS().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//							}
//						}
					}
					
					if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"4".equalsIgnoreCase(req.getBusinessType()) && !"5".equalsIgnoreCase(req.getBusinessType()))){
						
					
					if((StringUtils.isNotBlank(req.getUmbrellaXL()) && "Y".equalsIgnoreCase(req.getUmbrellaXL())) || (StringUtils.isNotBlank(req.getBusinessType()) && "6".equalsIgnoreCase(req.getBusinessType())) ){
					
					if(StringUtils.isBlank(req.getCoverLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required"),"CoverLimitXL","01"));
						}else {
							req.setCoverLimitXL((req.getCoverLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(req.getCoverLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required.format"),"CoverLimitXL","01"));
							}
						}
					if(StringUtils.isBlank(req.getDeductLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required"),"DeductLimitXL","01"));
						}else {
							req.setDeductLimitXL((req.getDeductLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(req.getDeductLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required.format"),"DeductLimitXL","01"));
							}
						}
					
					}
				}
					
					if("5".equals(req.getProductId())){
					if(StringUtils.isBlank(req.getEgnpiOffer())){
						list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
					}else {
						req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
						if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
						}
					}
					}
					if("3".equals(req.getProductId())){
						if(StringUtils.isBlank(req.getEgnpiOffer())){
							list.add(new ErrorCheck(prop.getProperty("error.egnpias.required"),"EgnpiOffer","01"));
						}else {
							req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
							if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
							}else {
								req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
								if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
								}else{
//								//	String ans = calcu.calculateXOL(req,"EGNPIAsper",0,req.getSourceId());
//									if("5".equalsIgnoreCase(bean.getBusinessType())){
//										if(bean.getEgnpiAsPerOffSlide()!=null){
//											for(int j=0;j<bean.getEgnpiAsPerOffSlide().size();j++){
//												amt = amt+Double.parseDouble(bean.getEgnpiAsPerOffSlide().get(i)==null||bean.getEgnpiAsPerOffSlide().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOffSlide().get(i).replaceAll(",", ""));
//											}
//										}
//									}else{
//										if(bean.getEgnpiAsPerOffSlide()!=null){
//											for(int j=0;j<bean.getEgnpiAsPerOff().size();j++){
//												amt = amt+Double.parseDouble(bean.getEgnpiAsPerOff().get(i)==null||bean.getEgnpiAsPerOff().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOff().get(i).replaceAll(",", ""));
//											}
//										}
//									}
//									String ans =  DropDownControllor.formatter(Double.toString(amt)).replaceAll(",", "");
//									if(Double.parseDouble(ans)!=Double.parseDouble(req.getEgnpiOffer().replaceAll(",",""))){
//										
//									}else{
//										req.setEgnpiOffer(ans);
//									}
								}
							}
						}
					if(StringUtils.isBlank(req.getOurAssessment())){
						list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required"),"ourassessment","01"));
					}else {
						if (val.isValidNo(req.getOurAssessment().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required.format"),"ourassessment","01"));
						}else{
							//String ans = calcu.calculateXOL(req,"OverAss",0,req.getSourceId());
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							String coverlimit=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
							if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
							amt = (Double.parseDouble(premiumRate) * 100)/Double.parseDouble(coverlimit);
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurAssessment().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"OurAssessment","01"));
								
							}else{
								req.setOurAssessment(ans);
							}
						}
					}
					if (StringUtils.isBlank(req.getSubPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.subPremium.required"),"subPremium","01"));
					} else {
						req.setSubPremium((req.getSubPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getSubPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.subPremium.required.format"),"subPremium","01"));
						}
					//	String ans = calcu.calculateXOL(req,"EGNPI",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurAssessment())?"0":req.getOurAssessment().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getSubPremium().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"SubPremium","01"));
							
						}else{
							req.setSubPremium(ans);
						}
					}
					}
					if(StringUtils.isNotBlank(req.getPml()) && "Y".equalsIgnoreCase(req.getPml())){
						if(StringUtils.isBlank(req.getPmlPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required"),"pmlpercent","01"));
						} else if (val.isValidNo(req.getPmlPercent().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required.format"),"pmlpercent","01"));
						} else if (val.percentageValid(req.getPmlPercent().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.checkgreater"),"pmlpercent","01"));
						}
						
					}
					if(StringUtils.isBlank(req.getPremiumbasis())){
						list.add(new ErrorCheck(prop.getProperty("error.Premiumbasis.required"),"Premiumbasis","01"));
					}
					else if("1".equalsIgnoreCase(req.getPremiumbasis())){
						if (StringUtils.isBlank(req.getAdjRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"adjRate","01"));
						} else if (val.isValidNo(req.getAdjRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"adjRate","01"));
						} else if (val.percentageValid(req.getAdjRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjrate.checkgreater"),"adjRate","01"));
						}
						if(StringUtils.isNotBlank(req.getBusinessType()) && "5".equalsIgnoreCase(req.getBusinessType())) {
							if (StringUtils.isBlank(req.getMinimumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
							} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
							}
							if (StringUtils.isBlank(req.getMaximumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
							} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
							}
							double min=Double.parseDouble(req.getMinimumRate());
							double max=Double.parseDouble(req.getMaximumRate());
							if(min>max){
								list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
							}
						}
					}
					else if("2".equalsIgnoreCase(req.getPremiumbasis())){
						if (StringUtils.isBlank(req.getMinimumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
						}
						if (StringUtils.isBlank(req.getMaximumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
						}
						double min=Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate());
						double max=Double.parseDouble((req.getMaximumRate()==null||val.isNull(req.getMaximumRate()).equalsIgnoreCase(""))?"0":req.getMaximumRate());
						if(min>max){
							list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
						}
						
						if (val.percentageValid(req.getBurningCostLF().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF().checkgreater"),"BurningCostLF","01"));
						}
						
					}
					if (StringUtils.isBlank(req.getEpi())) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required"),"epiperCent","01"));
					} else {
						req.setEpi((req.getEpi()).replaceAll(",", ""));
						if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required.format"),"epiperCent","01"));
						}else{
							if(!"3".equalsIgnoreCase(req.getPremiumbasis())){
							//	String ans = calcu.calculateXOL(req,"EPI",0,req.getSourceId());
								double a=0;
								if("1".equalsIgnoreCase(req.getPremiumbasis())){
								a =  Double.parseDouble((req.getAdjRate()==null||val.isNull(req.getAdjRate()).equalsIgnoreCase(""))?"0":req.getAdjRate().replaceAll(",", ""));	
								}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
								a =  Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate().replaceAll(",", ""));	
								}
								String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
								if(!"0".equals(premiumRate)) {
								amt = (Double.parseDouble(premiumRate) * a)/100;
								}
								String ans = fm.formatter(Double.toString(amt)).replaceAll(",", "");
								if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
									
								}else{
									req.setEpi(ans);
								}
							}
						}
					}
					if("3".equalsIgnoreCase(req.getPremiumbasis())){
						req.setMinimumRate(req.getEpi());
					}
					if(StringUtils.isBlank(req.getMinPremium())){
						 list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required"),"MinPremium","01"));
						}else {
							req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
							if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
							}else {
									req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
									if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
										list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
									}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
									//	String ans = calcu.calculateXOL(req,"MinPremium",0,req.getSourceId());
										String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
										String coverlimit=StringUtils.isBlank(req.getMinimumRate())?"0":req.getMinimumRate().replaceAll(",", "");
										if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
										amt = (Double.parseDouble(premiumRate) *Double.parseDouble(coverlimit))/100;
										}
										String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
										if(Double.parseDouble(ans)!=Double.parseDouble(req.getMinPremium().replaceAll(",",""))){
											
										}else{
											req.setMinPremium(ans);
										}
									}
								}
						}
					if (StringUtils.isBlank(req.getMdPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.depositPremium.required"),"depositPremium","01"));
					} else {
						req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getMdPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.depositPremium.required"),"depositPremium","01"));
						}
					}
					}
					if (val.isNull(req.getMdInstalmentNumber()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.no.of.Instalment.Required"),"MdInstalmentNumber","01"));
					} else if (val.isValidNo(req.getMdInstalmentNumber()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.no.of.Instalment.Required"),"MdInstalmentNumber","01"));
					}
					
					if(StringUtils.isBlank(req.getPaymentDuedays())){
						list.add(new ErrorCheck(prop.getProperty("error.PaymentDuedays.Required"),"PaymentDuedays","01"));
					}
					flags = true;
					double cedPer = 0.0;
					if (StringUtils.isBlank(req.getProStatus())) {
						list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
					}
					if("5".equalsIgnoreCase(req.getProductId())){
						if(StringUtils.isBlank(req.getNoRetroCess())){
							list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession"),"NoRetroCess","01"));
						}
						else if("0".equalsIgnoreCase(req.getNoRetroCess())){
							list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession.greater.than.zero"),"NoRetroCess","01"));
						}
					}
					if (StringUtils.isBlank(req.getShareWritten())) {
						if ("3".equals(req.getProductId())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"ShareWritten","01"));
						}		
						flags = false;
						cedCheck = false;
					} else if ("A".equalsIgnoreCase(req.getProStatus()) && val.percentageNewValid(req.getShareWritten().trim()).equalsIgnoreCase("Invalid")) {
						if ("3".equals(req.getProductId())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"ShareWritten","01"));
						} 
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getShareWritten());
						if ("2".equals(req.getProductId())	&& cedflag	&& Double.parseDouble(req.getShareWritten())	+ Double.parseDouble(req.getCedReten()) > 100) {
							list.add(new ErrorCheck(prop.getProperty("error.SWCedPer.invalid"),"ShareWritten","01"));
						}
					}
					
						
						if ("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())) {
							if (StringUtils.isBlank(req.getLimitOrigCur())) {
								cedCheck = false;
							} else {
								req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
								if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
									list.add(new ErrorCheck(prop.getProperty("error.limit.check"),"LimitOrigCur","01"));
									cedCheck = false;
								} else {
									amt = Double.parseDouble(req.getLimitOrigCur());
								}
							}
							if (StringUtils.isBlank(req.getDeduchunPercent())) {
								list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.required"),"DeduchunPercent","01"));
							} else {
								req.setDeduchunPercent((req.getDeduchunPercent()).replaceAll(",", ""));
								if (val.isValidNo(req.getDeduchunPercent().trim()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.check"),"DeduchunPercent","01"));
								}
							}
						}
						if ("3".equals(req.getProductId())){
							if (cedCheck) {
								double amount = (amt * (cedPer / 100.0));
								amount = amount / Double.parseDouble(req.getExchRate());
								maxlimit = Double.parseDouble(req.getMaxLimitProduct());
							
								if (amount > maxlimit) {
									list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
								}
							}
						}
						if(StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getSharSign())){
							final  DecimalFormat twoDigit = new DecimalFormat("###0.00");
							final double dvalue = (Double.parseDouble(req.getMdPremium())* (Double.parseDouble(req.getSharSign())) / 100);
							final double dround = Math.round(dvalue * 100.0) / 100.0;
							final double valu = Double.parseDouble(twoDigit.format(dround));//A
							if(StringUtils.isNotBlank(req.getContractNo())){
							double sumInst=dropDownImple.getSumOfInstallmentBooked(req.getContractNo(), req.getLayerNo());//B
								if(valu<sumInst){
									list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked.xol"),"premiumbooked","01"));
								}
							}
						}if(StringUtils.isNotBlank(req.getMdInstalmentNumber())){
							int count=dropDownImple.getCountOfInstallmentBooked(req.getContractNo(), req.getLayerNo());
							if(Double.parseDouble(req.getMdInstalmentNumber())<count){
								list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked.xol"),"premiumbooked","01"));
							}
						}
						
						
					
				
					if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getEpi())) {
						req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
						req.setEpi((req.getEpi()).replaceAll(",", ""));
						if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("INVALID")) {

							final float mdpremium = Float.parseFloat(req.getMdPremium());
							final float Pepi = Float.parseFloat(req.getEpi());

							if (mdpremium > Pepi) {
								list.add(new ErrorCheck(prop.getProperty("error.mdandpremium.difference.invalid"),"mdandpremium","01"));
							}
						}
					}
				
				if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getMinPremium())) {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
					if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {

						final float mdpremium = Float.parseFloat(req.getMdPremium());
						final float minp = Float.parseFloat(req.getMinPremium());

						if (mdpremium > minp) {
							list.add(new ErrorCheck(prop.getProperty("error.mdandminpremium.difference.invalid"),"mdandminpremium","01"));
						}
					}
				}
				if (!val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
					if (nonPropImple.getLayerDuplicationCheck(req.getBaseLayer(),req.getLayerNo(),req.getLayerMode()).getResponse().equalsIgnoreCase("true")) {
						list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"LayerDuplicationCheck","01"));
					}
				}
				if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
					list.add(new ErrorCheck(prop.getProperty("doc.status"),"EndorsementStatus","01"));
				}
//				if("3".equalsIgnoreCase(req.getProductId())){
//				validationContract();
//				}
//				validationRemarks();
			
		
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<ErrorCheck> insertClassLimitVali(insertClassLimitReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}

		if (StringUtils.isBlank(req.getBusinessType())) {
		list.add(new ErrorCheck("Please Enter BusinessType", "BusinessType", "3"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
		list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "5"));
		}
		if (CollectionUtils.isEmpty(req.getCoverLimitAmount())) {
			list.add(new ErrorCheck("Please Enter CoverLimitAmount", "CoverLimitAmount", "6"));
		}
		if (CollectionUtils.isEmpty(req.getCoverLimitAmount())) {
			list.add(new ErrorCheck("Please Enter CoverLimitOC", "CoverLimitOC", "6"));
		list.add(new ErrorCheck("Please Enter CoverLimitAmount", "CoverLimitAmount", "6"));
		}
		if (CollectionUtils.isEmpty(req.getCoverList())) {
		list.add(new ErrorCheck("Please Enter CoverLimitOC", "CoverLimitOC", "6"));
		}
		if (StringUtils.isBlank(req.getProductid())) {
		list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getProposalno())) {
		list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "5"));
		}
		
		if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"5".equalsIgnoreCase(req.getBusinessType()))){
		for(int i=0;i<req.getCoverLimitAmount().size();i++){
			CoverLimitAmount req2 = req.getCoverLimitAmount().get(i);
			if(StringUtils.isBlank(req2.getCoverdepartIdS())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
			}
			if(StringUtils.isBlank(req2.getCoverLimitAmount())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
			}
			if(StringUtils.isBlank(req2.getDeductableLimitAmount())){
				list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
			}
			if(StringUtils.isBlank(req2.getEgnpiAsPerOffSlide()) ){
				list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
			}
			
				if(StringUtils.isBlank(req2.getGnpiAsPOSlide()) ){
					list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));
			
			}
			for(int j=0;j<req.getCoverLimitAmount().size();j++){
				CoverLimitAmount req3 = req.getCoverLimitAmount().get(j);
				if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
					list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(j + 1),"CoverdepartId","01"));
				}
				if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
					list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(j + 1),"CoverLimitAmount","01"));
				}
				if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmount())){
					list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(j + 1),"CoverLimittPercent","01"));
				}
				if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
					list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(j + 1),"DeductableLimitAmount","01"));
				}
				if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
					list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(j + 1),"DeductableLimitPercent","01"));
				}
				if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
					list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(j + 1),"","01"));
					list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(j + 1),"","01"));
					list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(j + 1),"","01"));
					list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(j + 1),"","01"));
				}
				if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
					list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(j + 1),"EgnpiAsPerOffSlide","01"));
				}
				
					if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
						list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(j + 1),"GnpiAsPOSlide","01"));
					
				}
				
			} }
			
		for(int i=0;i<req.getCoverList().size();i++){
		CoverList req2 = req.getCoverList().get(i);
		if(StringUtils.isBlank(req2.getCoverdepartId())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
		}
		if(StringUtils.isBlank(req2.getCoverLimitOC())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
		}
		if(StringUtils.isBlank(req2.getDeductableLimitOC())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
		}
		if(StringUtils.isBlank(req2.getEgnpiAsPerOff()) ){
		list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
		}
		if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
		if(StringUtils.isBlank(req2.getGnpiAsPO()) ){
		list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));

		}
	}
		}
}
		else {
		for(int j=0;j<req.getCoverLimitAmount().size();j++){
		CoverLimitAmount req3 = req.getCoverLimitAmount().get(j);
		if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(j + 1),"CoverdepartId","01"));
		}
		if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(j + 1),"CoverLimitAmount","01"));
		}
		if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmount())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(j + 1),"CoverLimittPercent","01"));
		}
		if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(j + 1),"DeductableLimitAmount","01"));
		}
		if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(j + 1),"DeductableLimitPercent","01"));
		}
		if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(j + 1),"","01"));
		list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(j + 1),"","01"));
		list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(j + 1),"","01"));
		list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(j + 1),"","01"));
		}
		if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
		list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(j + 1),"EgnpiAsPerOffSlide","01"));
		}

		if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
		list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(j + 1),"GnpiAsPOSlide","01"));
		}
	}
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
	public List<ErrorCheck> riskDetailsEditModeVali(RiskDetailsEditModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
//		if (StringUtils.isBlank(req.getContractNo())) {
//			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "1"));
//		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(String.valueOf(req.getContractMode()))) {
			list.add(new ErrorCheck("Please Enter ContractMode", "ContractMode", "3"));
		}
//		if (StringUtils.isBlank(req.getLayerNo())) {
//			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
//		}
//
//		if (StringUtils.isBlank(req.getProductId())) {
//			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "2"));
//		}
//		if (StringUtils.isBlank(req.getProposalReference())) {
//			list.add(new ErrorCheck("Please Enter ProposalReference", "ProposalReference", "2"));
//		}
		return list;
	}
	public List<ErrorCheck> showSecondPageDataVali(ShowSecondPageDataReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		return list;
	}
	public List<ErrorCheck> getRetroContractDetailsListVali(GetRetroContractDetailsListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}
		if (StringUtils.isBlank(req.getProductid())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getRetroType())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
		}

		return list;
	}
	public List<ErrorCheck> saveSecondPageVali(SaveSecondPageReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		SaveSecondPageRes1 res = new SaveSecondPageRes1();
		double amt = 0;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		try {
			if("A".equalsIgnoreCase(req.getProStatus())|| "5".equalsIgnoreCase(req.getProductId())){
			//	validateSecondPage();
				Validation validation = new Validation();
	            
				String limitOurShare = validation.isNull(req.getLimitOurShare());
				if ("3".equals(req.getProductId()) || "5".equals(req.getProductId())) {
					int instalmentperiod = Integer.parseInt(req.getMdInstalmentNumber());
					double mndPremiumOC=Double.parseDouble(req.getMdpremiumourservice().replaceAll(",", ""));
					double totalInstPremium=0.0;
					boolean tata = false;
					for (int i = 0; i < instalmentperiod; i++) {
						if (validation.isNull(req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.required.InstalDate")+ String.valueOf(i + 1),"InstalDate","01"));
						} else if (validation.checkDate(req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("Error.Error.InstalDate")+ String.valueOf(i + 1),"InstalDate","01"));
						}
						if (!validation.isNull(req.getInstalmentperiod().get(0).getInstalmentDateList()).equalsIgnoreCase("")) {
							if (validation.ValidateINstallDates(req.getIncepDate(),req.getInstalmentperiod().get(0).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
								tata = true;
							}
						}
						if (!validation.isNull(	req.getInstalmentperiod().get(0).getInstalmentDateList()).equalsIgnoreCase("")) {
							if (validation.ValidateTwoDates(req.getInstalmentperiod().get(i).getInstalmentDateList(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
								list.add(new ErrorCheck(prop.getProperty("Error.Select.Expirydate")+ String.valueOf(i + 1),"Expirydate","01"));
							}
						}
						if (validation.isNull(req.getInstalmentperiod().get(i).getInstallmentPremium())	.equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.required.InstallPremium")+ String.valueOf(i + 1),"InstallPremium","01"));
						} else if (validation.isValidNo(req.getInstalmentperiod().get(i).getInstallmentPremium().replaceAll(",", "")).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("Error.error.InstallPremium")+ String.valueOf(i + 1),"InstallPremium","01"));
						}else{
							try{
				            	totalInstPremium+=Double.parseDouble(req.getInstalmentperiod().get(i).getInstallmentPremium().replaceAll(",", ""));                	
				            }catch (Exception e) {
				            	list.add(new ErrorCheck(prop.getProperty("Error.installment.Premium")+ String.valueOf(i + 1),"InstallPremium","01"));
							}
						}
						if (i != 0) {
							if (!validation.isNull(req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("")) {
								if (validation.ValidateTwoDates(req.getInstalmentperiod().get(i-1).getInstalmentDateList(),	req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
									list.add(new ErrorCheck(prop.getProperty("Error.required.InstalDate")+ String.valueOf(i + 1),"InstalDate","01"));
								}
							}
						}
						if(StringUtils.isBlank(req.getInstalmentperiod().get(i).getPaymentDueDays())){
			            	list.add(new ErrorCheck(prop.getProperty("Error.payement.due.day")+ String.valueOf(i + 1),"PaymentDueDays","01"));
						}
					}
					BigDecimal bd = new BigDecimal(totalInstPremium).setScale(2, RoundingMode.HALF_EVEN);
					totalInstPremium = bd.doubleValue();
					if((totalInstPremium)!=mndPremiumOC){
						list.add(new ErrorCheck(prop.getProperty("Error.total.installment.premium")+" Deposit Premium - Our Share - OC","mndPremiumOC","01"));
				    }
					if (tata == true) {
						list.add(new ErrorCheck(prop.getProperty("Error.Select.AfterInceptionDate"),"AfterInceptionDate","01"));
					}
				}
				if ("3".equals(req.getProductId())) {
					String epiAsPerOffer = validation.isNull(req.getEpiAsPerOffer());
					if (validation.isNull(epiAsPerOffer).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.epiAsPerOffer.second.val"),"epiAsPerOffer","01"));
					} else {
						if (validation.isValidNo(req.getEpiAsPerOffer().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.epiAsPerOffer.second1.val"),"epiAsPerOffer","01"));
						}
					}
					if (validation.isNull(req.getMdpremiumourservice()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.mdpremiumourservice.number.val"),"mdpremiumourservice","01"));
					} else {
						if (validation.isValidNo(req.getMdpremiumourservice()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.mdpremiumourservice.number.format"),"mdpremiumourservice","01"));
						}
					}
					
					
					if (validation.isNull(req.getAnualAggregateLiability()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateLiability.number"),"AnualAggregateLiability","01"));
					} else {
						req.setAnualAggregateLiability((req.getAnualAggregateLiability()).replaceAll(",", ""));
						if (validation.isValidNo(req.getAnualAggregateLiability()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateLiability.number.format"),"AnualAggregateLiability","01"));
						}
					}
					
					if (validation.isNull(req.getAnualAggregateDeduct()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateDeduct.number"),"AnualAggregateDeduct","01"));
					} else {
						req.setAnualAggregateDeduct((req.getAnualAggregateDeduct()).replaceAll(",", ""));
						if (validation.isValidNo(req.getAnualAggregateDeduct()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateDeduct.number.format"),"AnualAggregateDeduct","01"));
						}
					}
					if("2".endsWith(req.getBusinessType()) ||"3".endsWith(req.getBusinessType()) ||  "7".equalsIgnoreCase(req.getBusinessType())||  "8".equalsIgnoreCase(req.getBusinessType())){
					if (validation.isNull(req.getOccurrentLimit()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.occlimit.number.req"),"occlimit","01"));
					} else {
						req.setOccurrentLimit((req.getOccurrentLimit()).replaceAll(",", ""));
						if (validation.isValidNo(req.getOccurrentLimit()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.occlimit.number"),"occlimit","01"));
						}
					}
					}
				
				}
				if(StringUtils.isBlank(req.getReInstatementPremium())){
					list.add(new ErrorCheck(prop.getProperty("Please Select Reinstatement Premium"),"Reinstatement","01"));
				}
				else if("Y".equalsIgnoreCase(req.getReInstatementPremium())){
					if(StringUtils.isBlank(req.getReinsPopUp())){
	                    list.add(new ErrorCheck(prop.getProperty("reins.popup.recheck"),"popup","01"));
	                }else{
					int count= nonPropImple.getReInstatementCount(req.getAmendId(),req.getProposalNo(),req.getBranchCode(),req.getReferenceNo());
					if(count<=0){
						list.add(new ErrorCheck(prop.getProperty("errors.reinstatement.schedule"),"reinstatement","01"));
					}
	                }
				}
				if (validation.isNull(req.getBrokerage()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second"),"brokerage","01"));
				} else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second1"),"brokerage","01"));
				} else if (validation.percentageValid(req.getBrokerage()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondgreater"),"brokerage","01"));
				}
				if (validation.isNull(req.getTax()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second"),"tax","01"));
				} else if (validation.percentageValid(req.getTax()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second1"),"tax","01"));
				} else if (validation.percentageValid(req.getTax()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondless"),"tax","01"));
				} else if (validation.percentageValid(req.getTax()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondgreater"),"tax","01"));
				}
				if (validation.isNull(req.getOthercost()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"),"othercost","01"));
				} else if (validation.percentageValid(req.getOthercost()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondinvalid"),"othercost","01"));
				} else if (validation.percentageValid(req.getOthercost()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondless"),"othercost","01"));
				} else if (validation.percentageValid(req.getOthercost()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondgreater"),"othercost","01"));
				}
				if ((!"4".equalsIgnoreCase(req.getProductId())) && (!"5".equalsIgnoreCase(req.getProductId()))) {
					if (validation.isNull(limitOurShare).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.limitOurShare.second"),"limitOurShare","01"));	
					} else if (validation.isValidNo(req.getLimitOurShare().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.limitOurShare.second1"),"limitOurShare","01"));
					}
					
					if ("3".equalsIgnoreCase(req.getProductId())) {
					
					}
					if ((!"4".equalsIgnoreCase(req.getProductId())) && (!"5".equalsIgnoreCase(req.getProductId()))) {
						
						if(StringUtils.isBlank(req.getAcqBonus())){
							}
							else{
							if("LCB".equalsIgnoreCase(req.getAcqBonus())){
								if(StringUtils.isBlank(req.getBonusPopUp())){
				                    list.add(new ErrorCheck(prop.getProperty("bonus.popup.recheck"),"popup","01"));
				                }else{
								int count = nonPropImple.getBonusListCount(req);
								if(count<=0){
									list.add(new ErrorCheck(prop.getProperty("bonus.error.lcb.table.empty"),"BonusListCount","01"));
								}
				                }
							}
							else if("NCB".equalsIgnoreCase(req.getAcqBonus())){
							   if(StringUtils.isBlank(req.getAcqBonusPercentage())){
								   list.add(new ErrorCheck(prop.getProperty("bonus.error.noclaimbonu.per"),"AcqBonus","01"));
									}
							   else if(100<Double.parseDouble(req.getAcqBonusPercentage())){
								list.add(new ErrorCheck(prop.getProperty("bonus.error.low.claim.bonus.range"),"AcqBonusPercentage","01"));
								}
								}
							
							}
					}
					
					if (validation.isNull(req.getAcquisitionCost()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"),"AcquisitionCost","01"));
					} else {
						req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
						if (validation.isValidNo(req.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"),"AcquisitionCost","01"));
						}
						else{
							//String ans = calcu.calculateXOL(req,"AcqCost",0,req.getSourceId());
							
							double a = 0;
							if("RI01".equalsIgnoreCase(req.getSourceId())){
								a = Double.parseDouble(StringUtils.isBlank(req.getMinPremiumOurShare())?"0":req.getMinPremiumOurShare().replaceAll(",", ""));
							}else{
								a = Double.parseDouble(StringUtils.isBlank(req.getEpiAsPerOffer())?"0":req.getEpiAsPerOffer().replaceAll(",", ""));
							}
							double b=Double.parseDouble(StringUtils.isBlank(req.getOthercost())?"0":req.getOthercost().replaceAll(",", ""));
							double c=Double.parseDouble(StringUtils.isBlank(req.getBrokerage())?"0":req.getBrokerage().replaceAll(",", ""));
							double d=Double.parseDouble(StringUtils.isBlank(req.getTax())?"0":req.getTax().replaceAll(",", ""));
							if(!"0".equals(a) ) {
							amt = ((b+c+d)*a)/100;
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"AcquisitionCost","01"));
							}else{
								req.setAcquisitionCost(ans);
							}

						}
					}
				
					
					if (validation.isNull(req.getLeaderUnderwriter()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second"),"LeaderUnderwriter","01"));
					}
					if("RI02".equalsIgnoreCase(req.getSourceId()) && "3".equalsIgnoreCase(req.getProductId())){
						if(StringUtils.isBlank(req.getLeaderUnderwritercountry())){
							list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second.country"),"LeaderUnderwriter","01"));
						}
					}
					if (validation.isNull(req.getUnderwriterRecommendations()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.underwriter_Recommendations.second"),"UnderwriterRecommendations","01"));
					}
					if (validation.isNull(req.getLeaderUnderwritershare()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second"),"LeaderUnderwritershare","01"));
					} else if (validation.percentageValid(req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second1"),"LeaderUnderwritershare","01"));
					} else if (validation.percentageValid(req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.secondless"),"LeaderUnderwritershare","01"));
					} else if (validation.percentageValid(req.getLeaderUnderwritershare()).trim().equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.secondgreater"),"LeaderUnderwritershare","01"));
					}
					if(StringUtils.isNotBlank(req.getLeaderUnderwritershare()) && !"64".equalsIgnoreCase(req.getLeaderUnderwriter())){
						if(nonPropImple.GetShareValidation(req.getProposalNo(),req.getLeaderUnderwritershare())){
						list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.greater.signed"),"LeaderUnderwritershare","01"));
					}
					}else{
						if(dropDownImple.GetShareEqualValidation(req.getProductId(),req.getLeaderUnderwritershare(),req.getProposalNo())){
							list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.equals.signed"),"LeaderUnderwritershare","01"));
						} 
					}
				}
				if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
					req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalNo())));
					req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
					final String endorseDate=validation.checkDate(req.getEndorsementDate());
						if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
							if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"),"Endorsmenttype","01"));
							}
							else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.rectification.required"),"rectification","01"));
							}
							else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.required"),"gnpiDate","01"));
							}
						} else if (endorseDate.equalsIgnoreCase("INVALID")) {
							if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"),"endoDate","01"));
							}
							else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.rectification.check"),"rectification","01"));
							}
							else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.check"),"gnpiDate","01"));
							}
						} else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
								if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
									list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(),req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"endoDate","01"));
								}
								else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
									list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"Endorsmenttype","01"));
								}
								else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
									list.add(new ErrorCheck(prop.getProperty(("errors.gnpiDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"Endorsmenttype","01"));
								}
						}
					
					if(!validation.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")){
						if(dropDownImple.Validatethree(req.getBranchCode(), req.getEndorsementDate())==0){
							list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.endo")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
						}
					}
					}
					if("Y".equalsIgnoreCase(req.getCrestaStatus())){
						if(StringUtils.isBlank(req.getCrestaPopUp())){
		                    list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"),"popup","01"));
		                }
						else if(nonPropImple.getCrestaCount(req.getAmendId(),req.getProposalNo(),req.getBranchCode())==0){
							list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"),"creasta","01"));
						}
					}
				if ("3".equalsIgnoreCase(req.getProductId())) {
					
				}
				if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
					list.add(new ErrorCheck(prop.getProperty("doc.status"),"EndorsementStatus","01"));
				}
				if ("3".equalsIgnoreCase(req.getProductId())) {
					final int LoopCount = Integer.parseInt(req.getNoInsurer());
					double totPer = 0.0;
					boolean flag = true;
					if (LoopCount != 0) {
						if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"),"RetentionPercentage","01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"),"RetentionPercentage","01"));
							flag = false;
						} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"),"RetentionPercentage","01"));
							flag = false;
						} else {
							totPer += Double.parseDouble(req.getRetentionPercentage());
						}
					}
					boolean dupCheck = true;
					for (int i = 0; i < LoopCount; i++) {
						RetroListReq retroReq =req.getRetroListReq().get(i);
						if ("".equals(retroReq.getRetroYear())) {
							list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"uwYear","01"));
							dupCheck = false;
						}
						if(retroReq.getRetroCeding() ==null){
							list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
							dupCheck = false;
						}
						if (StringUtils.isBlank(retroReq.getRetroCeding())) {
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

					//	RiskDetailsreq req = new RiskDetailsreq();
						if ("2".equals(req.getProductId())) {
							req.setProductId("4");
							req.setRetroType("TR");
						} else if ("3".equals(req.getProductId())) {
							req.setProductId("4");
							req.setRetroType("TR");
						}
					}
					if (dupCheck) {
						for (int i = 0; i < LoopCount - 1; i++) {
							for (int j = i + 1; j < LoopCount; j++) {
								if ((req.getRetroListReq().get(i).getRetroCeding()).equalsIgnoreCase((req.getRetroListReq().get(j).getRetroCeding()))) {
									list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(j + 1),"RetroContract","01"));
								}
							}
						}
					}
					if (LoopCount != 0) {
						if (flag) {
							DecimalFormat df = new DecimalFormat("#.##");
							totPer=Double.parseDouble(df.format(totPer));
							if (totPer != 100) {
								list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"),"totPercentage","01"));
							}
						}
					}
				}
				if("5".equals(req.getProductId())){
					int NoRetroCess = Integer.parseInt(req.getNoRetroCess()== null ? "0" : req.getNoRetroCess());
					for (int i = 0; i < NoRetroCess; i++) {
						NoRetroCessReq req2 = req.getNoRetroCessReq().get(i);
						String cedComp = req2.getCedingCompany() == null ? "0" : req2.getCedingCompany();
						String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
						String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
						String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
						String proStatus = req2.getProposalStatus() == null ? "0" : req2.getProposalStatus();
						if (StringUtils.isBlank(validation.isSelect(cedComp))) {
							list.add(new ErrorCheck(prop.getProperty("errors.reinsurersName.required")+String.valueOf(i + 1),"reinsurersName","01"));
						}
						if (StringUtils.isBlank(validation.isSelect(broker))) {
							list.add(new ErrorCheck(prop.getProperty("errors.brokerRetro.required")+String.valueOf(i + 1),"brokerRetro","01"));
						}
						boolean shAccSign = true;
						if (StringUtils.isBlank(shAccep)) {
							list.add(new ErrorCheck(prop.getProperty("errors.shAccepPer.required")+ String.valueOf(i + 1),"shAccepPer","01"));
							shAccSign = false;
						} else if (validation.percentageValid(shAccep).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.shAccepPer.invalid")+ String.valueOf(i + 1),"shAccepPer","01"));
							shAccSign = false;
						}
						if (StringUtils.isBlank(validation.isSelect(proStatus))) {
							list.add(new ErrorCheck(prop.getProperty("errors.proStatus.required")+ String.valueOf(i + 1),"proStatus","01"));
						}
						if (StringUtils.isBlank(shSign) && proStatus.equalsIgnoreCase("A")) {
							list.add(new ErrorCheck(prop.getProperty("errors.shSignPer.required")+ String.valueOf(i + 1),"shSignPer","01"));
							shAccSign = false;
						} else if (validation.percentageValid(shSign).equalsIgnoreCase("INVALID") && !"A".equalsIgnoreCase(proStatus)) {
							list.add(new ErrorCheck(prop.getProperty("errors.shSignPer.invalid")+String.valueOf(i + 1),"shSignPer","01"));
							shAccSign = false;
						}
						if (shAccSign  && "A".equalsIgnoreCase(proStatus) ) {
							double shac = Double.parseDouble(shAccep);
							double shsign = Double.parseDouble(shSign);
							if (shac < shsign) {
								list.add(new ErrorCheck(prop.getProperty("errors.shAccepLessShSign.invalid")+ String.valueOf(i + 1),"shAccepLessShSign","01"));
							}

						}
											}
					if (CollectionUtils.isEmpty(list)) {
						double totShAcc = 0.0;
						double totShsg = 0.0;
						for (int i = 0; i < NoRetroCess; i++) {
							NoRetroCessReq req2 = req.getNoRetroCessReq().get(i);
							String cedComp = req2.getCedingCompany()== null ? "0" : req2.getCedingCompany();
							String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
							String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
							String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
							totShAcc += Double.parseDouble(shAccep);
							totShsg += Double.parseDouble(shSign);
							for (int j = i + 1; j < NoRetroCess; j++) {		
								NoRetroCessReq req3 = req.getNoRetroCessReq().get(j);
								String cedComp1 = req3.getCedingCompany() == null ? "0" : req3.getCedingCompany();
								String broker1 = req3.getRetroBroker() == null ? "0" : req3.getRetroBroker();
								String shAccep1 = req3.getShareAccepted()== null ? "" : req3.getShareAccepted();
								String shSign1 = req3.getShareSigned()== null ? "" : req3.getShareSigned();
								if (cedComp.equals(cedComp1)&& broker.equals(broker1))
									list.add(new ErrorCheck(prop.getProperty("errors.cedCompBroker.invalid")+ String.valueOf(j + 1),"shAccepLessShSign","01"));
								if (((i + 1) == NoRetroCess) && (j == NoRetroCess)) {
									totShAcc += Double.parseDouble(shAccep1);
									totShsg += Double.parseDouble(shSign1);
								}
							}
						}
						if(StringUtils.isBlank(req.getRetroDupContract()) &&"3".equalsIgnoreCase(req.getProductId())){
							list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
						}
						if (totShsg != 100)
							list.add(new ErrorCheck(prop.getProperty("errors.shSign.invalid"),"shSign","01"));
					}
				}
				if ("5".equalsIgnoreCase(req.getProductId())) {
					
				}
			//	validationRemarks();

			 
				}
				else{
				//	validateSecodnPageSaveMethod();	

					Validation validation = new Validation();
		            
					String limitOurShare = validation.isNull(req.getLimitOurShare());
					if ("3".equals(req.getProductId()) || "5".equals(req.getProductId())) {
						int instalmentperiod = Integer.parseInt(req.getMdInstalmentNumber());
						double mndPremiumOC=Double.parseDouble(req.getMdpremiumourservice().replaceAll(",", ""));
						double totalInstPremium=0.0;
						boolean tata = false;
						for (int i = 0; i < instalmentperiod; i++) {
							
							if (!validation.isNull(req.getInstalmentperiod().get(0).getInstalmentDateList()).equalsIgnoreCase("")) {
								if (validation.ValidateINstallDates(req.getIncepDate(),req.getInstalmentperiod().get(0).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
									tata = true;
								}
							}
							if (!validation.isNull(	req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("")) {
								if (validation.ValidateTwoDates(req.getInstalmentperiod().get(i).getInstalmentDateList(),req.getExpiryDate()).equalsIgnoreCase("Invalid")) {
									list.add(new ErrorCheck(prop.getProperty("Error.Select.Expirydate")+String.valueOf(i + 1),"Expirydate","01"));
								}
							}
							if (!validation.isNull(req.getInstalmentperiod().get(i).getInstallmentPremium()).equalsIgnoreCase("")) {
								try{
					            	totalInstPremium+=Double.parseDouble(req.getInstalmentperiod().get(i).getInstallmentPremium().replaceAll(",", ""));                	
					            }catch (Exception e) {
								}
							}
							if (i != 0) {
								if (!validation.isNull(	req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("")) {
									if (validation.ValidateTwoDates(req.getInstalmentperiod().get(i-1).getInstalmentDateList(),	req.getInstalmentperiod().get(i).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
										list.add(new ErrorCheck(prop.getProperty("Error.required.InstalDate")+ String.valueOf(i + 1),"InstalDate","01"));
									}
								}
							}
							
						}
						BigDecimal bd = new BigDecimal(totalInstPremium).setScale(2, RoundingMode.HALF_EVEN);
						totalInstPremium = bd.doubleValue();
						if((totalInstPremium)!=mndPremiumOC){
					    }
						if (tata == true) {
							list.add(new ErrorCheck(prop.getProperty("Error.Select.AfterInceptionDate"),"AfterInceptionDate","01"));
						}
					}
					if ("3".equals(req.getProductId())) {
						String epiAsPerOffer = validation.isNull(req.getEpiAsPerOffer());
						if (validation.isNull(epiAsPerOffer).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.epiAsPerOffer.second.val"),"epiAsPerOffer","01"));
						} else {
							if (validation.isValidNo(req.getEpiAsPerOffer().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.epiAsPerOffer.second1.val"),"epiAsPerOffer","01"));
							}
						}
						if (validation.isNull(req.getMdpremiumourservice()).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.mdpremiumourservice.number.val"),"mdpremiumourservice","01"));
						} else {
							if (validation.isValidNo(req.getMdpremiumourservice()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.mdpremiumourservice.number.format"),"mdpremiumourservice","01"));
							}
						}
						
						if(StringUtils.isBlank(req.getReInstatementPremium())){
							list.add(new ErrorCheck(prop.getProperty("errors.reinstatement.blank"),"reinstatement","01"));
						}
						else if("Y".equalsIgnoreCase(req.getReInstatementPremium())){
							if(StringUtils.isBlank(req.getReinsPopUp())){
			                    list.add(new ErrorCheck(prop.getProperty("reins.popup.recheck"),"popup","01"));
			                }else{
							int count=nonPropImple.getReInstatementCount(req.getAmendId(),req.getProposalNo(),req.getBranchCode(), req.getReferenceNo());
							if(count<=0){
								list.add(new ErrorCheck(prop.getProperty("errors.reinstatement.schedule"),"reinstatement","01"));
							}
			                }
						}
						if (!validation.isNull(req.getAnualAggregateLiability()).equalsIgnoreCase("")) {
							req.setAnualAggregateLiability((req.getAnualAggregateLiability()).replaceAll(",", ""));
							
						}
						
						if (!validation.isNull(req.getAnualAggregateDeduct()).equalsIgnoreCase("")) {
							req.setAnualAggregateDeduct((req.getAnualAggregateDeduct()).replaceAll(",", ""));
							
						}
						if("2".endsWith(req.getBusinessType()) ||"3".endsWith(req.getBusinessType()) || "7".equalsIgnoreCase(req.getBusinessType()) ||  "8".equalsIgnoreCase(req.getBusinessType())){
						if (!validation.isNull(req.getOccurrentLimit()).equalsIgnoreCase("")) {
							req.setOccurrentLimit((req.getOccurrentLimit()).replaceAll(",", ""));
						}
						}
						
					}
					if ((!"4".equalsIgnoreCase(req.getProductId())) && (!"5".equalsIgnoreCase(req.getProductId()))) {
						if (validation.isNull(limitOurShare).equalsIgnoreCase("")) {
							list.add(new ErrorCheck(prop.getProperty("errors.limitOurShare.second"),"limitOurShare","01"));	
						} else if (validation.isValidNo(req.getLimitOurShare().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("errors.limitOurShare.second1"),"limitOurShare","01"));
						}
					
						if (!validation.isNull(req.getAcquisitionCost()).equalsIgnoreCase("")) {
							req.setAcquisitionCost((req.getAcquisitionCost()).replaceAll(",", ""));
							//	String ans = calcu.calculateXOL(req,"AcqCost",0,req.getSourceId());
							double a = 0;
							if("RI01".equalsIgnoreCase(req.getSourceId())){
								a = Double.parseDouble(StringUtils.isBlank(req.getMinPremiumOurShare ())?"0":req.getMinPremiumOurShare().replaceAll(",", ""));
							}else{
								a = Double.parseDouble(StringUtils.isBlank(req.getEpiAsPerOffer())?"0":req.getEpiAsPerOffer().replaceAll(",", ""));
							}
							double b=Double.parseDouble(StringUtils.isBlank(req.getOthercost())?"0":req.getOthercost().replaceAll(",", ""));
							double c=Double.parseDouble(StringUtils.isBlank(req.getBrokerage())?"0":req.getBrokerage().replaceAll(",", ""));
							double d=Double.parseDouble(StringUtils.isBlank(req.getTax())?"0":req.getTax().replaceAll(",", ""));
							if(!"0".equals(a) ) {
							amt = ((b+c+d)*a)/100;
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
								if(Double.parseDouble(ans)!=Double.parseDouble(req.getAcquisitionCost().replaceAll(",",""))){
									list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"AcquisitionCost","01"));
								}else{
									req.setAcquisitionCost(ans);
								}
						}
						if("Y".equalsIgnoreCase(req.getEndorsementStatus())) {
						req.setAccDate((dropDownImple.getAcceptanceDate(req.getProposalNo())));
						req.setMaxDate(Validation.getMaxDateValidate(req.getAccDate(), req.getPreviousendoDate()));
						final String endorseDate=validation.checkDate(req.getEndorsementDate());
						if (validation.isNull(req.getEndorsementDate()).equalsIgnoreCase("")) {
							if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"),"endoDate","01"));
							}
							else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.rectification.required"),"rectification","01"));
							}
							else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.required"),"gnpiDate","01"));
							}
						} else if (endorseDate.equalsIgnoreCase("INVALID")) {
							if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"),"endoDate","01"));
							}
							else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.rectification.check"),"rectification","01"));
							}
							else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty("error.gnpiDate.check"),"gnpiDate","01"));
							}
						} else  if ("Invalid".equalsIgnoreCase(Validation.ValidateTwo(req.getMaxDate(), req.getEndorsementDate()))) {
							if("Endorsement".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty(("errors.endoDate.invalid")+req.getAccDate(),req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"endoDate","01"));
							}
							else if("Rectification".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty(("errors.rectificationDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"Endorsmenttype","01"));
							}
							else if("GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								list.add(new ErrorCheck(prop.getProperty(("errors.gnpiDate.invalid")+req.getAccDate(), req.getPreviousendoDate()==null?"":req.getPreviousendoDate()),"Endorsmenttype","01"));
								}
						}
						}
						if("Y".equalsIgnoreCase(req.getCrestaStatus())){
							if(StringUtils.isBlank(req.getCrestaPopUp())){
			                    list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"),"popup","01"));
			                }
							
							else if(nonPropImple.getCrestaCount(req.getAmendId(),req.getProposalNo(),req.getBranchCode())==0){
								list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"),"creasta","01"));
							}
						}
						
					}
					if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
						list.add(new ErrorCheck(prop.getProperty("doc.status"),"EndorsementStatus","01"));
					}
					if ("3".equalsIgnoreCase(req.getProductId())) {
						final int LoopCount = Integer.parseInt(req.getNoInsurer());
						double totPer = 0.0;
						boolean flag = true;
						if (LoopCount != 0) {
							if (validation.isNull(req.getRetentionPercentage()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.Required"),"RetentionPercentage","01"));
								flag = false;
							} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.invalid"),"RetentionPercentage","01"));
								flag = false;
							} else if (validation.percentageValid(req.getRetentionPercentage()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("Error.RetentionPercentage.greater"),"RetentionPercentage","01"));
								flag = false;
							} else {
								totPer += Double.parseDouble(req.getRetentionPercentage());
							}
						}
						boolean dupCheck = true;
						for (int i = 0; i < LoopCount; i++) {
							RetroListReq retroReq =req.getRetroListReq().get(i);
							if ("".equals(retroReq.getRetroYear())) {
								list.add(new ErrorCheck(prop.getProperty("error.uwYear.Required")+String.valueOf(i + 1),"uwYear","01"));
								dupCheck = false;
							}
							if(retroReq.getRetroCeding() ==null){
								list.add(new ErrorCheck(prop.getProperty("Error.CeddingCompany.Required")+String.valueOf(i + 1),"CeddingCompany","01"));
								dupCheck = false;
							}
							if (StringUtils.isBlank(retroReq.getRetroCeding())) {
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

						//	RiskDetailsreq req = new RiskDetailsreq();
							if ("2".equals(req.getProductId())) {
								req.setProductId("4");
								req.setRetroType("TR");
							} else if ("3".equals(req.getProductId())) {
								req.setProductId("4");
								req.setRetroType("TR");
							}
						}
						if (dupCheck) {
							for (int i = 0; i < LoopCount - 1; i++) {
								for (int j = i + 1; j < LoopCount; j++) {
									if ((req.getRetroListReq().get(i).getRetroCeding()).equalsIgnoreCase((req.getRetroListReq().get(j).getRetroCeding()))) {
										list.add(new ErrorCheck(prop.getProperty("error.RetroContract.Repeat")+String.valueOf(j + 1),"RetroContract","01"));
									}
								}
							}
						}
						if (LoopCount != 0) {
							if (flag) {
								DecimalFormat df = new DecimalFormat("#.##");
								totPer=Double.parseDouble(df.format(totPer));
								if (totPer != 100) {
									list.add(new ErrorCheck(prop.getProperty("error.totPercentage.invalid"),"totPercentage","01"));
								}
							}
						}
					}
					if("5".equals(req.getProductId())){
						int NoRetroCess = Integer.parseInt(req.getNoRetroCess()== null ? "0" : req.getNoRetroCess());
						for (int i = 0; i < NoRetroCess; i++) {
							NoRetroCessReq req2 = req.getNoRetroCessReq().get(i);
							String cedComp = req2.getCedingCompany()== null ? "0" : req2.getCedingCompany();
							String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
							String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
							String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
							String proStatus = req2.getProposalStatus() == null ? "0" : req2.getProposalStatus();
							if (StringUtils.isBlank(validation.isSelect(cedComp))) {
								list.add(new ErrorCheck(prop.getProperty("errors.reinsurersName.required")+ String.valueOf(i + 1),"reinsurersName","01"));
							}
							if (StringUtils.isBlank(validation.isSelect(broker))) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerRetro.required")+ String.valueOf(i + 1),"brokerRetro","01"));
							}
							boolean shAccSign = true;
							if (StringUtils.isBlank(shAccep)) {
								list.add(new ErrorCheck(prop.getProperty("errors.shAccepPer.required")+ String.valueOf(i + 1),"shAccepPer","01"));
								shAccSign = false;
							} else if (validation.percentageValid(shAccep).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.shAccepPer.invalid")+ String.valueOf(i + 1),"shAccepPer","01"));
								shAccSign = false;
							}
							if (StringUtils.isBlank(validation.isSelect(proStatus))) {
								list.add(new ErrorCheck(prop.getProperty("errors.proStatus.required")+ String.valueOf(i + 1),"proStatus","01"));
							}
							if (StringUtils.isBlank(shSign) && "A".equalsIgnoreCase(proStatus)) {
								list.add(new ErrorCheck(prop.getProperty("errors.shSignPer.required")+ String.valueOf(i + 1),"shSignPer","01"));
								shAccSign = false;
							} else if (validation.percentageValid(shSign).equalsIgnoreCase("INVALID") && !"A".equalsIgnoreCase(proStatus)) {
								list.add(new ErrorCheck(prop.getProperty("errors.shSignPer.invalid")+ String.valueOf(i + 1),"shSignPer","01"));
								shAccSign = false;
							}
							if (shAccSign  && "A".equalsIgnoreCase(proStatus) ) {
								double shac = Double.parseDouble(shAccep);
								double shsign = Double.parseDouble(shSign);
								if (shac < shsign) {
									list.add(new ErrorCheck(prop.getProperty("errors.shAccepLessShSign.invalid")+ String.valueOf(i + 1),"shAccepLessShSign","01"));
								}

							}
						}
						if (CollectionUtils.isEmpty(list)) {
							double totShAcc = 0.0;
							double totShsg = 0.0;
							for (int i = 0; i < NoRetroCess; i++) {
								NoRetroCessReq req2 = req.getNoRetroCessReq().get(i);
								String cedComp = req2.getCedingCompany()== null ? "0" : req2.getCedingCompany();
								String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
								String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
								String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
								totShAcc += Double.parseDouble(shAccep);
								totShsg += Double.parseDouble(shSign);
								for (int j = i + 1; j < NoRetroCess; j++) {							
									
									String cedComp1 = req2.getCedingCompany()== null ? "0" : req2.getCedingCompany();
									String broker1 = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
									String shAccep1 = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
									String shSign1 = req2.getShareSigned()== null ? "" : req2.getShareSigned();
									if (cedComp.equals(cedComp1)&& broker.equals(broker1))
										list.add(new ErrorCheck(prop.getProperty("errors.cedCompBroker.invalid")+String.valueOf(j + 1),"cedCompBroker","01"));
									if (((i + 1) == NoRetroCess) && (j == NoRetroCess)) {
										totShAcc += Double.parseDouble(shAccep1);
										totShsg += Double.parseDouble(shSign1);
									}
								}
							}
							if (totShsg != 100)
								list.add(new ErrorCheck(prop.getProperty("errors.shSign.invalid"),"shSign","01"));
						}
					}
					if(StringUtils.isBlank(req.getRetroDupContract())&&"3".equalsIgnoreCase(req.getProductId())){
						list.add(new ErrorCheck(prop.getProperty("errors.dummy.contract")+req.getUwYear(),"RetroDupContract","01"));
					}
					
					//validationRemarks();
				 
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ErrorCheck> insertProportionalTreatyvali(insertProportionalTreatyReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		boolean flags = true;
		boolean cedCheck = true;
		boolean cedflag = true;
		final Validation val = new Validation();
		double amt = 0;
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		try {
		if("A".equalsIgnoreCase(req.getProStatus())|| "5".equalsIgnoreCase(req.getPid())){
			//validatenext();
			
			if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0 && "3".equalsIgnoreCase(req.getPid())){
				if(StringUtils.isBlank(req.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
				}
			}
			if (val.isSelect(req.getDepartId()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"DepartmentId","01"));
			}
			if (val.isSelect(req.getSubProfitcenter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitCenter","01"));
			}else{
				req.setSubProfitcenter((req.getSubProfitcenter()).replaceAll(" ", ""));
			}
			if (val.isSelect(req.getProfitCenter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.Profit_Center.required"),"ProfitCenter","01"));
			}
			if("3".equalsIgnoreCase(req.getPid())){
			if(StringUtils.isBlank(req.getInwardType())){
				if("RI01".equalsIgnoreCase(req.getSourceId())){
					list.add(new ErrorCheck(prop.getProperty("error.InwardType.required"),"InwardType","01"));
				}
				else{
					list.add(new ErrorCheck(prop.getProperty("error.InwardType.required02"),"InwardType","01"));
				}
			}
			}
			if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
			}
			if("3".equalsIgnoreCase(req.getPid())){
			if (StringUtils.isBlank(req.getMaxLimitProduct())) {
				list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct","01"));
				cedCheck = false;
			} else {
				req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
				if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct","01"));
					cedCheck = false;
				} else {
					GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getPid(), "0");
					String uwLimit=commonRes.getCommonResponse();
					uwLimit=uwLimit.replaceAll(",", "");
					if (Double.parseDouble(uwLimit) == 0) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct","01"));
						cedCheck = false;
					} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"MaxLimitProduct","01"));
						cedCheck = false;
					}
				}
			}
			}
			if (val.isNull(req.getPolBr()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch","01"));
			}
			if(StringUtils.isBlank(req.getBusinessType())){
				list.add(new ErrorCheck(prop.getProperty("error.BusinessType.required"),"BusinessType","01"));
			}
			if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
			}
			if (StringUtils.isBlank(req.getBroker())) {
				list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker","01"));
			}
			if("3".equalsIgnoreCase(req.getPid()))
			if (StringUtils.isBlank(req.getTreatyNametype())) {
				list.add(new ErrorCheck(prop.getProperty("error.treatyName_type.required"),"TreatyNameType","01"));
			}
			else if("5".equalsIgnoreCase(req.getPid())){
				list.add(new ErrorCheck(prop.getProperty("error.retroTreatyName.required"),"TreatyNameType","01"));
			}
			if (val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"layerNo","01"));
			} else if (val.isValidNo(req.getLayerNo()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"layerNo","01"));
			}
			Map<String, Object> map = null;
			List<Map<String, Object>> list1 = nonPropImple.getValidation(req.getIncepDate(),req.getRenewalcontractno());
			if (list1 != null && list1.size() > 0) {
				map = (Map<String, Object>) list1.get(0);
			}
			if (val.isNull(req.getIncepDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
			} else if (val.checkDate(req.getIncepDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
			} else if (StringUtils.isNotBlank((req.getRenewalcontractno()))&& !"0".equals(req.getRenewalcontractno())&& map != null) {
				if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getIncepDate()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"incepDate","01"));
				}else {
					req.setRenewalFlag("NEWCONTNO");
				}
			}
			if (val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate","01"));
			} else if (val.checkDate(req.getExpDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
			}
			if (!val.isNull(req.getIncepDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
				if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"expDate","01"));
				}
			}
			if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
			} else if (!"".equals(req.getRenewalcontractno()) && !"0".equals(req.getRenewalcontractno()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(req.getUwYear())) {
				
			}
			if(StringUtils.isNotBlank(req.getProStatus()) &&"A".equalsIgnoreCase(req.getProStatus())){
			if (StringUtils.isBlank(req.getAccDate())) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.required"),"accDate","01"));
			}
			}
			 if (!val.isNull(req.getAccDate()).equalsIgnoreCase("") &&val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
			}
			if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !"endorsment".equalsIgnoreCase(req.getEdit())){
				if(dropDownImple.Validatethree(req.getBranchCode(), req.getAccDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}
			}
			if (!val.isNull(req.getExpDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
				if (Validation.ValidateTwo(req.getAccDate(), req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"accDate","01"));
				}
			}
			if (StringUtils.isNotBlank(req.getIncepDate())&& StringUtils.isNotBlank(req.getExpDate())) {
				if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
				}
			}
			if (StringUtils.isBlank(val.isSelect(req.getOrginalCurrency()))) {
				list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
			}
			
			if (StringUtils.isBlank(req.getExchRate())) {
				list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate","01"));
				cedCheck = false;
			} else if (val.isValidNo(req.getExchRate().trim().toString()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate","01"));
				cedCheck = false;
			}
			double maxlimit = 0.0;
			boolean spflag = true;
			if ("3".equals(req.getPid())) {
				
				if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
					spflag = false;
				}
				if (val.isNull(req.getNoInsurer()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.Required"),"NoInsurer","01"));
				} else if (val.isValidNo(req.getNoInsurer()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.NumberFormat"),"NoInsurer","01"));
				} else if (spflag && "Y".equals(req.getSpRetro()) && Integer.parseInt(req.getNoInsurer()) <= 0) {
					list.add(new ErrorCheck(prop.getProperty("Errors.No_Insurar.gr0"),"NoInsurer","01"));
				}
				if("4".equals(req.getDepartId())|| "2".equals(req.getDepartId()) || "10".equals(req.getDepartId())){
					if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC","01"));
					}else{
						req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC","01"));
						}
					}
					if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
						list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC","01"));
					}else{
						req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
						if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC","01"));
						}
					}
				}
			}
				if(StringUtils.isBlank(req.getTerritory())){
					list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode","01"));
				}if(StringUtils.isBlank(req.getCountryIncludedList())){
					list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude","01"));
				}
			
			if (StringUtils.isBlank(req.getTerritoryscope())) {
				list.add(new ErrorCheck(prop.getProperty("error.terrtoryScope.required"),"terrtoryScope","01"));
			}
			if (StringUtils.isBlank(req.getPortfoloCovered())) {
				list.add(new ErrorCheck(prop.getProperty("error.portfoloCovered.required"),"portfoloCovered","01"));
			}
			if (StringUtils.isBlank(req.getBasis())) {
				list.add(new ErrorCheck(prop.getProperty("error.basic.required"),"basic","01"));
			}
			if("3".equals(req.getPid()) || "5".equals(req.getPid())){
				
				if(StringUtils.isBlank(req.getLOCIssued())){
					list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
				}
				else if("Y".equalsIgnoreCase(req.getLOCIssued())){
					if(StringUtils.isBlank(req.getLocBankName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank","01"));
					}
					if(StringUtils.isBlank(req.getLocCreditPrd())){
						list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd","01"));
					}
					if(StringUtils.isBlank(req.getLocCreditAmt())){
						list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt","01"));
					}
					else{
						req.setLocCreditAmt(req.getLocCreditAmt().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(req.getLocBeneficerName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName","01"));
					}
					
					}
				if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"5".equalsIgnoreCase(req.getBusinessType()))){
					for(int i=0;i<req.getCoverLimitOC().size();i++){
						coverLimitOC req2 = req.getCoverLimitOC().get(i);
						if(StringUtils.isBlank(req2.getCoverdepartId())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
						}
						if(StringUtils.isBlank(req2.getCoverLimitOC())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
						}
						if(StringUtils.isBlank(req2.getDeductableLimitOC())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
						}
						if(StringUtils.isBlank(req2.getEgnpiAsPerOff()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
						}
						if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							if(StringUtils.isBlank(req2.getGnpiAsPO()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));
							}
						}
					}
//					if(DropDownControllor.findDuplicates(req.getCoverdepartId()).size()>0){
//						list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"","01"));
//					}
//					if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//						if(req.getCoverdepartId().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//						}
//					}
				}
				if(StringUtils.isNotBlank(req.getBusinessType()) &&("5".equalsIgnoreCase(req.getBusinessType()))){
					for(int i=0;i<req.getCoverLimitAmount().size();i++){
						CoverLimitAmount req3 = req.getCoverLimitAmount().get(i);
						if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"CoverLimitAmount","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmount())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"CoverLimittPercent","01"));
						}
						if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"DeductableLimitAmount","01"));
						}
						if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"DeductableLimitPercent","01"));
						}
						if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"","01"));
							list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"","01"));
						}
						if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
							list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOffSlide","01"));
						}
						if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
							if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(i + 1),"GnpiAsPOSlide","01"));
							}
						}
						
					}
//					if(DropDownControllor.findDuplicates(req.getCoverdepartIdS()).size()>0){
//						list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"","01"));
//					}
//					if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//						if(req.getCoverdepartIdS().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//						}
//					}
				}
				
				if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"4".equalsIgnoreCase(req.getBusinessType()) && !"5".equalsIgnoreCase(req.getBusinessType()))){
					
				if(!"5".equalsIgnoreCase(req.getBusinessType())){
					
				}
				if(StringUtils.isNotBlank(req.getUmbrellaXL()) && "Y".equalsIgnoreCase(req.getUmbrellaXL())){
				
				if(StringUtils.isBlank(req.getCoverLimitXL())){
					list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required"),"CoverLimitXL","01"));
					}else {
						req.setCoverLimitXL((req.getCoverLimitXL()).replaceAll(",", ""));
						if (val.isValidNo(req.getCoverLimitXL().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required.format"),"CoverLimitXL","01"));
						}
					}
				if(StringUtils.isBlank(req.getDeductLimitXL())){
					list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required"),"DeductLimitXL","01"));
					}else {
						req.setDeductLimitXL((req.getDeductLimitXL()).replaceAll(",", ""));
						if (val.isValidNo(req.getDeductLimitXL().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required.format"),"DeductLimitXL","01"));
						}
					}
				
				}
			}
				
				if("5".equals(req.getPid())){
				if(StringUtils.isBlank(req.getEgnpiOffer())){
					list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
				}else {
					req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
					if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.egnpi.required.format"),"EgnpiOffer","01"));
					}
				}
				}
				if("3".equals(req.getPid())){
					if(StringUtils.isBlank(req.getEgnpiOffer())){
						list.add(new ErrorCheck(prop.getProperty("error.egnpias.required"),"EgnpiOffer","01"));
					}else {
						req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
						if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
						}else{
							//calculateXOL
						
							//String ans = calcu.calculateXOL(req,"EGNPIAsper",0,req.getSourceId());
//							if("5".equalsIgnoreCase(req.getBusinessType())){
//								if(req.getEgnpiAsPerOffSlide()!=null){
//									for(int j=0;j<req.getEgnpiAsPerOffSlide().size();j++){
//										amt = amt+Double.parseDouble(bean.getEgnpiAsPerOffSlide().get(i)==null||bean.getEgnpiAsPerOffSlide().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOffSlide().get(i).replaceAll(",", ""));
//									}
//								}
//							}else{
//								if(bean.getEgnpiAsPerOffSlide()!=null){
//									for(int j=0;j<bean.getEgnpiAsPerOff().size();j++){
//										amt = amt+Double.parseDouble(bean.getEgnpiAsPerOff().get(i)==null||bean.getEgnpiAsPerOff().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOff().get(i).replaceAll(",", ""));
//									}
//								}
//							}
//							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
//							
//							if(Double.parseDouble(ans)!=Double.parseDouble(req.getEgnpiOffer().replaceAll(",",""))){
//								
//							}else{
//								req.setEgnpiOffer(ans);
//							}
						}
					}
				if(StringUtils.isBlank(req.getOurAssessment())){
					list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required"),"ourassessment","01"));
				}else {
					if (val.isValidNo(req.getOurAssessment().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required.format"),"ourassessment","01"));
					}
					else{
						//String ans = calcu.calculateXOL(req,"OverAss",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * 100)/Double.parseDouble(coverlimit);
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurAssessment().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"ourassessment","01"));
						
						}else{
							req.setOurAssessment(ans);
						}
					}
				}
				if (StringUtils.isBlank(req.getSubPremium())) {
					list.add(new ErrorCheck(prop.getProperty("error.subPremium.required"),"subPremium","01"));
				} else {
					req.setSubPremium((req.getSubPremium()).replaceAll(",", ""));
					if (val.isValidNo(req.getSubPremium().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.subPremium.required.format"),"subPremium","01"));
					}
					else{
						//String ans = calcu.calculateXOL(req,"EGNPI",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurAssessment())?"0":req.getOurAssessment().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
						}
						String ans  =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getSubPremium().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"subPremium","01"));
						
						}else{
							req.setSubPremium(ans);
						}
					}
				}
				}
				if(StringUtils.isNotBlank(req.getPml()) && "Y".equalsIgnoreCase(req.getPml())){
					if(StringUtils.isBlank(req.getPmlPercent())){
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required"),"PmlPercent","01"));
					} else if (val.isValidNo(req.getPmlPercent().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required.format"),"PmlPercent","01"));
					} else if (val.percentageValid(req.getPmlPercent().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.checkgreater"),"PmlPercent","01"));
					}
					
				}
				if(StringUtils.isBlank(req.getPremiumbasis())){
					list.add(new ErrorCheck(prop.getProperty("error.Premiumbasis.required"),"Premiumbasis","01"));
				}
				else if("1".equalsIgnoreCase(req.getPremiumbasis())){
					if (StringUtils.isBlank(req.getAdjRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"AdjRate","01"));
					} else if (val.isValidNo(req.getAdjRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.adjRate.required.format"),"AdjRate","01"));
					} else if (val.percentageValid(req.getAdjRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.adjrate.checkgreater"),"AdjRate","01"));
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) && "5".equalsIgnoreCase(req.getBusinessType())) {
						if (StringUtils.isBlank(req.getMinimumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required.format"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
						}
						if (StringUtils.isBlank(req.getMaximumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required.format"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
						}
						double min=Double.parseDouble(req.getMinimumRate());
						double max=Double.parseDouble(req.getMaximumRate());
						if(min>max){
							list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"Rate","01"));
						}
					}
				}
				else if("2".equalsIgnoreCase(req.getPremiumbasis())){
					if (StringUtils.isBlank(req.getMinimumRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
					} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
					} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
					}
					if (StringUtils.isBlank(req.getMaximumRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
					} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
					} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MaximumRate","01"));
					}
					double min=Double.parseDouble(req.getMinimumRate());
					double max=Double.parseDouble(req.getMaximumRate());
					if(min>max){
						list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
					}
					if(StringUtils.isBlank(req.getBurningCostLF())){
						list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF.required"),"BurningCostLF","01"));
					}
					if (val.percentageValid(req.getBurningCostLF().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF().checkgreater"),"BurningCostLF","01"));
					}
					
				}
				if (StringUtils.isBlank(req.getEpi())) {
					list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required"),"epiperCent","01"));
				} else {
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required.format"),"epiperCent","01"));
					}else{
						if(!"3".equalsIgnoreCase(req.getPremiumbasis())){
						//	String ans = calcu.calculateXOL(req,"EPI",0,req.getSourceId());

							double a=0;
							if("1".equalsIgnoreCase(req.getPremiumbasis())){
							a =  Double.parseDouble((req.getAdjRate()==null||val.isNull(req.getAdjRate()).equalsIgnoreCase(""))?"0":req.getAdjRate().replaceAll(",", ""));	
							}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
							a =  Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate().replaceAll(",", ""));	
							}
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							if(!"0".equals(premiumRate) ) {
							amt = (Double.parseDouble(premiumRate) * a)/100;
							}
							String ans  =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
						
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
								
							}else{
								req.setEpi(ans);
							}
						}
					}
				}
				if("3".equalsIgnoreCase(req.getPremiumbasis())){
					req.setMinimumRate(req.getEpi());
				}
				if(StringUtils.isBlank(req.getMinPremium())){
					 list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required"),"MinPremium","01"));
					}else {
						req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
						}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
							//String ans = calcu.calculateXOL(req,"MinPremium",0,req.getSourceId());
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							String coverlimit=StringUtils.isBlank(req.getMinimumRate())?"0":req.getMinimumRate().replaceAll(",", "");
							if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
							amt = (Double.parseDouble(premiumRate) *Double.parseDouble(coverlimit))/100;
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getMinPremium().replaceAll(",",""))){
							}else{
								req.setMinPremium(ans);
							}
						}
					}
				if (StringUtils.isBlank(req.getMdPremium())) {
					list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required1"),"MdPremium","01"));
				} else {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					if (val.isValidNo(req.getMdPremium().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required.format"),"MdPremium","01"));
					}
				}
				}
				if (val.isNull(req.getMdInstalmentNumber()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.Instalment.error"),"Instalment","01"));
					
				} else if (val.isValidNo(req.getMdInstalmentNumber()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.Instalment.Required"),"Instalment","01"));
				}
				
				if(StringUtils.isBlank(req.getPaymentDuedays())){
					list.add(new ErrorCheck(prop.getProperty("error.PaymentDuedays.Required"),"PaymentDuedays","01"));
				}
				flags = true;
				double cedPer = 0.0;
				if (StringUtils.isBlank(req.getProStatus())) {
					list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
				}
				if("5".equalsIgnoreCase(req.getPid())){
					if(StringUtils.isBlank(req.getNoRetroCess())){
						list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession"),"NoRetroCess","01"));
					}
					else if("0".equalsIgnoreCase(req.getNoRetroCess())){
						list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession.greater.than.zero"),"NoRetroCess","01"));
					}
				}
				if (StringUtils.isBlank(req.getShareWritt())) {
					if ("3".equals(req.getPid())) {
						list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"ShareWritten","01"));
					}		
					flags = false;
					cedCheck = false;
				} else if ("A".equalsIgnoreCase(req.getProStatus()) && val.percentageNewValid(req.getShareWritt().trim()).equalsIgnoreCase("Invalid")) {
					if ("3".equals(req.getPid())) {
						list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"ShareWritten","01"));
					} 
					flags = false;
					cedCheck = false;
				} else {
					cedPer = Double.parseDouble(req.getShareWritt());
					if ("2".equals(req.getPid())	&& cedflag	&& Double.parseDouble(req.getShareWritt())	+ Double.parseDouble(req.getCedReten()) > 100) {
						list.add(new ErrorCheck(prop.getProperty("error.SWCedPer.invalid"),"SWCedPer","01"));
					}
				}
				if ("A".equalsIgnoreCase(req.getProStatus())) {
				
					if (StringUtils.isBlank(req.getSharSign())) {
						if ( "3".equals(req.getPid())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required.pro"),"shareSign","01"));
						} 
						flags = false;
						cedCheck = false;
					} else if (val.percentageNewValid(req.getSharSign().trim()).equalsIgnoreCase("Invalid")) {
						if ("3".equals(req.getPid())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.required"),"shareSign","01"));
						} 
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getSharSign());
					}
					if (flags) {
						if (Double.parseDouble(req.getSharSign().equalsIgnoreCase("") ? "0" : req.getSharSign()) > Double.parseDouble(req.getShareWritt().equalsIgnoreCase("") ? "0" : req.getShareWritt())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareSign.invalid"),"shareSign","01"));
							cedCheck = false;
						}
					}
					
					if ("3".equalsIgnoreCase(req.getPid()) || "5".equalsIgnoreCase(req.getPid())) {
						if (StringUtils.isBlank(req.getLimitOrigCur())) {
							cedCheck = false;
						} else {
							req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
							if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
								list.add(new ErrorCheck(prop.getProperty("error.limit.check"),"LimitOrigCur","01"));
								cedCheck = false;
							} else {
								amt = Double.parseDouble(req.getLimitOrigCur());
							}
						}
						if (StringUtils.isBlank(req.getDeduchunPercent())) {
							list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.required"),"DeduchunPercent","01"));
						} else {
							req.setDeduchunPercent((req.getDeduchunPercent()).replaceAll(",", ""));
							if (val.isValidNo(req.getDeduchunPercent().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.check"),"DeduchunPercent","01"));
							}
						}
					}
					if ("3".equals(req.getPid())){
						if (cedCheck) {
							double amount = (amt * (cedPer / 100.0));
							amount = amount / Double.parseDouble(req.getExchRate());
							maxlimit = Double.parseDouble(req.getMaxLimitProduct());
							
							if (amount > maxlimit) {
								list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
							}
						}
					}
					if(StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getSharSign())){
						final  DecimalFormat twoDigit = new DecimalFormat("###0.00");
						final double dvalue = (Double.parseDouble(req.getMdPremium())* (Double.parseDouble(req.getSharSign())) / 100);
						
						final double dround = Math.round(dvalue * 100.0) / 100.0;
						final double valu = Double.parseDouble(twoDigit.format(dround));//A
						if(StringUtils.isNotBlank(req.getContNo())){
						double sumInst=dropDownImple.getSumOfInstallmentBooked(req.getContNo(), req.getLayerNo());//B
							if(valu<sumInst){
								list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked.xol"),"installment","01"));
							}
						}
					}if(StringUtils.isNotBlank(req.getMdInstalmentNumber())){
						int count=dropDownImple.getCountOfInstallmentBooked(req.getContNo(), req.getLayerNo());
						if(Double.parseDouble(req.getMdInstalmentNumber())<count){
							list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked.xol"),"installment","01"));
						}
					}
				}
	
				if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getEpi())) {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					req.setEpi((req.getEpi()).replaceAll(",", ""));
					if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("INVALID")) {

						final float mdpremium = Float.parseFloat(req.getMdPremium());
						final float Pepi = Float.parseFloat(req.getEpi());

						if (mdpremium > Pepi) {
							list.add(new ErrorCheck(prop.getProperty("error.mdandpremium.difference.invalid1"),"","01"));
						}
					}
				}
			
			if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getMinPremium())) {
				req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
				req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
				if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {

					final float mdpremium = Float.parseFloat(req.getMdPremium());
					final float minp = Float.parseFloat(req.getMinPremium());
					if (mdpremium > minp) {
						list.add(new ErrorCheck(prop.getProperty("error.mdandminpremium.difference.invalid"),"","01"));
					}
				}
			}
			if (!val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
				if (nonPropImple.getLayerDuplicationCheck(req.getBaseLayer(),req.getLayerNo(),req.getLayerMode()).getResponse().equalsIgnoreCase("true")) {
				
					list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"","01"));
				}
			}
			

//			if("3".equalsIgnoreCase(req.getProductId())){
//				validationContract();
//				}
//			validationRemarks();
		}
		else{
			
				if(StringUtils.isNotBlank(req.getAmendId())&& Integer.parseInt(req.getAmendId())>0 && ("3".equalsIgnoreCase(req.getPid()) ||  "5".equalsIgnoreCase(req.getPid()))){
					if(StringUtils.isBlank(req.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
					}
				}
				if (val.isSelect(req.getDepartId()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"departId","01"));
				}
				if (val.isSelect(req.getSubProfitcenter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"SubProfitCenter","01"));
				}else{
					req.setSubProfitcenter((req.getSubProfitcenter()).replaceAll(" ", ""));
				}
				
				
				if (val.isNull(req.getUnderwriter()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
				}
				if("3".equalsIgnoreCase(req.getPid())){
				if (StringUtils.isBlank(req.getMaxLimitProduct())) {
					list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.required"),"maxLimitproduct","01"));
					cedCheck = false;
				} else {
					req.setMaxLimitProduct((req.getMaxLimitProduct()).replaceAll(",", ""));
					if (val.isValidNo(req.getMaxLimitProduct().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.maxLimitproduct.invalid"),"maxLimitproduct","01"));
						cedCheck = false;
					} else {
						GetCommonValueRes commonRes = dropDownImple.getUnderWriterLimmit(req.getUnderwriter(),req.getProcessId(), req.getPid(), "0");
						String uwLimit=commonRes.getCommonResponse();
						uwLimit=uwLimit.replaceAll(",", "");
						if (Double.parseDouble(uwLimit) == 0) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.config"),"maxLimitproduct","01"));
							cedCheck = false;
						} else if (Double.parseDouble(req.getMaxLimitProduct()) > Double.parseDouble(uwLimit)) {
							list.add(new ErrorCheck(prop.getProperty("error.maxLimitProduct.exceedLimit")+uwLimit,"maxLimitproduct","01"));
							cedCheck = false;
						}
					}
				}
				}
				if (val.isNull(req.getPolBr()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"PolicyBranch","01"));
				}
				if(StringUtils.isBlank(req.getBusinessType())){
					list.add(new ErrorCheck(prop.getProperty("error.BusinessType.required"),"BusinessType","01"));
				}
				if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
				}
				if (StringUtils.isBlank(req.getBroker())) {
					list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker","01"));
				}
				
				if (val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"layerNo","01"));
				} else if (val.isValidNo(req.getLayerNo()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"layerNo","01"));
				}
				Map<String, Object> map = null;
				List<Map<String, Object>> list1 = nonPropImple.getValidation(req.getIncepDate(),req.getRenewalcontractno());
				if (list1 != null && list1.size() > 0) {
					map = (Map<String, Object>) list1.get(0);
				}
				if (val.isNull(req.getIncepDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
				} else if (val.checkDate(req.getIncepDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
				} else if (StringUtils.isNotBlank((req.getRenewalcontractno()))&& !"0".equals(req.getRenewalcontractno())&& map != null) {
					if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getIncepDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
					}else {
						req.setRenewalFlag("NEWCONTNO");
					}
				}
				if (val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"ExpiryDate","01"));
				} else if (val.checkDate(req.getExpDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
				}
				if (!val.isNull(req.getIncepDate()).equalsIgnoreCase("")&& !val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
					if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
					}
				}
				 if (StringUtils.isNotBlank(req.getAccDate())&&val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
				}
				if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
				} else if (!"".equals(req.getRenewalcontractno()) && !"0".equals(req.getRenewalcontractno()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(req.getUwYear())) {
				}
				
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") && !req.getEdit().equalsIgnoreCase("endorsment") && !val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")){
					if(dropDownImple.Validatethree(req.getBranchCode(), req.getAccDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
				}
				if (!val.isNull(req.getExpDate()).equalsIgnoreCase("") && !val.isNull(req.getAccDate()).equalsIgnoreCase("") &&!val.checkDate(req.getAccDate()).equalsIgnoreCase("INVALID")) {
					if (Validation.ValidateTwo(req.getAccDate(), req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check"),"accDate","01"));
					}
				}
				if (StringUtils.isNotBlank(req.getIncepDate())&& StringUtils.isNotBlank(req.getExpDate())) {
					if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
					}
				}
				if (StringUtils.isBlank(val.isSelect(req.getOrginalCurrency()))) {
					list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
				}
				
				if (StringUtils.isBlank(req.getExchRate())) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate","01"));
					cedCheck = false;
				} else if (val.isValidNo(req.getExchRate().trim().toString()).equalsIgnoreCase("invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate","01"));
					cedCheck = false;
				}
				double maxlimit = 0.0;
				boolean spflag = true;
				if ("3".equals(req.getPid())) {
					
					if (val.isNull(req.getSpRetro()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("errors.SpRetro.error"),"SpRetro","01"));
						spflag = false;
					}
					
					if("4".equals(req.getDepartId())|| "2".equals(req.getDepartId()) || "10".equals(req.getDepartId())){
						if(val.isNull(req.getLimitPerVesselOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.required"),"LimitPerVesselOC","01"));
						}else{
							req.setLimitPerVesselOC((req.getLimitPerVesselOC()).replaceAll(",",""));
							if(val.isValidNo(req.getLimitPerVesselOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerVesselOC.invalid"),"LimitPerVesselOC","01"));
							}
						}
						if(val.isNull(req.getLimitPerLocationOC()).equalsIgnoreCase("")){
							list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.required"),"LimitPerLocationOC","01"));
						}else{
							req.setLimitPerLocationOC((req.getLimitPerLocationOC()).replaceAll(",",""));
							if(val.isValidNo(req.getLimitPerLocationOC().trim()).equalsIgnoreCase("INVALID")){
								list.add(new ErrorCheck(prop.getProperty("errors.LimitPerLocationOC.invalid"),"LimitPerLocationOC","01"));
							}
						}
					}
				}
				if("3".equals(req.getPid()) || "5".equals(req.getPid())){
					
					if(StringUtils.isBlank(req.getLOCIssued())){
						list.add(new ErrorCheck(prop.getProperty("locissued.error"),"locissued","01"));
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"5".equalsIgnoreCase(req.getBusinessType()))){
						
						for(int i=0;i<req.getCoverLimitOC().size();i++){
							coverLimitOC req2 = req.getCoverLimitOC().get(i);
							if(StringUtils.isBlank(req2.getCoverdepartId())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
							}
							if(StringUtils.isBlank(req2.getCoverLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+ String.valueOf(i + 1),"CoverLimitOC","01"));
							}
							if(StringUtils.isBlank(req2.getDeductableLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+ String.valueOf(i + 1),"DeductableLimitOC","01"));
							}
							if(StringUtils.isBlank(req2.getEgnpiAsPerOff()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOff","01"));
							}
							if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								if(StringUtils.isBlank(req2.getGnpiAsPO()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"GnpiAsPO","01"));
								}
							}
						}
						
//						if(DropDownControllor.findDuplicates(req.getCoverdepartId()).size()>0){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"));
//						}
//						if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//							if(req.getCoverdepartId().contains(req.getDepartId()) && req.getCoverdepartId().size()>1){
//								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"));
//							}
//						}
						
					}
					if(StringUtils.isNotBlank(req.getBusinessType()) &&("5".equalsIgnoreCase(req.getBusinessType()))){
						for(int i=0;i<req.getCoverLimitAmount().size();i++){
							CoverLimitAmount req3 = req.getCoverLimitAmount().get(i);
							if(StringUtils.isBlank(req3.getCoverdepartIdS()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+ String.valueOf(i + 1),"CoverdepartId","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isNotBlank(req3.getCoverLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"CoverLimitAmount","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitPercent())&& StringUtils.isNotBlank(req3.getCoverLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"CoverLimittPercent","01"));
							}
							if(StringUtils.isBlank(req3.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req3.getDeductableLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"DeductableLimitAmount","01"));
							}
							if(StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isNotBlank(req3.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"DeductableLimitPercent","01"));
							}
							if(StringUtils.isBlank(req3.getCoverLimitAmount()) && StringUtils.isBlank(req3.getCoverLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitPercent()) && StringUtils.isBlank(req3.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+ String.valueOf(i + 1),"","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+ String.valueOf(i + 1),"","01"));
							}
							if(StringUtils.isBlank(req3.getEgnpiAsPerOffSlide()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+ String.valueOf(i + 1),"EgnpiAsPerOffSlide","01"));
							}
							if(StringUtils.isNotBlank(req.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(req.getEndorsmenttype())){
								if(StringUtils.isBlank(req3.getGnpiAsPOSlide()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+ String.valueOf(i + 1),"GnpiAsPOSlide","01"));
								}
							}
							
						}
//						if(DropDownControllor.findDuplicates(req.getCoverdepartIdS()).size()>0){
//							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"));
//						}
//						if("17".equalsIgnoreCase(req.getDepartId()) || "18".equalsIgnoreCase(req.getDepartId()) || "19".equalsIgnoreCase(req.getDepartId())){
//							if(req.getCoverdepartIdS().contains(req.getDepartId()) && req.getCoverdepartIdS().size()>1){
//								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"","01"));
//							}
//						}
					}
					
					if(StringUtils.isNotBlank(req.getBusinessType()) &&(!"4".equalsIgnoreCase(req.getBusinessType()) && !"5".equalsIgnoreCase(req.getBusinessType()))){
						
					
					if((StringUtils.isNotBlank(req.getUmbrellaXL()) && "Y".equalsIgnoreCase(req.getUmbrellaXL())) || (StringUtils.isNotBlank(req.getBusinessType()) && "6".equalsIgnoreCase(req.getBusinessType())) ){
					
					if(StringUtils.isBlank(req.getCoverLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required"),"CoverLimitXL","01"));
						}else {
							req.setCoverLimitXL((req.getCoverLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(req.getCoverLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required.format"),"CoverLimitXL","01"));
							}
						}
					if(StringUtils.isBlank(req.getDeductLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required"),"DeductLimitXL","01"));
						}else {
							req.setDeductLimitXL((req.getDeductLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(req.getDeductLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required.format"),"DeductLimitXL","01"));
							}
						}
					
					}
				}
					
					if("5".equals(req.getPid())){
					if(StringUtils.isBlank(req.getEgnpiOffer())){
						list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
					}else {
						req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
						if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.egnpi.required"),"EgnpiOffer","01"));
						}
					}
					}
					if("3".equals(req.getPid())){
						if(StringUtils.isBlank(req.getEgnpiOffer())){
							list.add(new ErrorCheck(prop.getProperty("error.egnpias.required"),"EgnpiOffer","01"));
						}else {
							req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
							if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
							}else {
								req.setEgnpiOffer((req.getEgnpiOffer()).replaceAll(",", ""));
								if (val.isValidNo(req.getEgnpiOffer().trim()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("error.egnpias.required.format"),"EgnpiOffer","01"));
								}else{
//								//	String ans = calcu.calculateXOL(req,"EGNPIAsper",0,req.getSourceId());
//									if("5".equalsIgnoreCase(bean.getBusinessType())){
//										if(bean.getEgnpiAsPerOffSlide()!=null){
//											for(int j=0;j<bean.getEgnpiAsPerOffSlide().size();j++){
//												amt = amt+Double.parseDouble(bean.getEgnpiAsPerOffSlide().get(i)==null||bean.getEgnpiAsPerOffSlide().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOffSlide().get(i).replaceAll(",", ""));
//											}
//										}
//									}else{
//										if(bean.getEgnpiAsPerOffSlide()!=null){
//											for(int j=0;j<bean.getEgnpiAsPerOff().size();j++){
//												amt = amt+Double.parseDouble(bean.getEgnpiAsPerOff().get(i)==null||bean.getEgnpiAsPerOff().get(i).equalsIgnoreCase("")?"0":bean.getEgnpiAsPerOff().get(i).replaceAll(",", ""));
//											}
//										}
//									}
//									String ans =  DropDownControllor.formatter(Double.toString(amt)).replaceAll(",", "");
//									if(Double.parseDouble(ans)!=Double.parseDouble(req.getEgnpiOffer().replaceAll(",",""))){
//										
//									}else{
//										req.setEgnpiOffer(ans);
//									}
								}
							}
						}
					if(StringUtils.isBlank(req.getOurAssessment())){
						list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required"),"ourassessment","01"));
					}else {
						if (val.isValidNo(req.getOurAssessment().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.ourassessment.required.format"),"ourassessment","01"));
						}else{
							//String ans = calcu.calculateXOL(req,"OverAss",0,req.getSourceId());
							String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
							String coverlimit=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
							if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
							amt = (Double.parseDouble(premiumRate) * 100)/Double.parseDouble(coverlimit);
							}
							String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getOurAssessment().replaceAll(",",""))){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"OurAssessment","01"));
								
							}else{
								req.setOurAssessment(ans);
							}
						}
					}
					if (StringUtils.isBlank(req.getSubPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.subPremium.required"),"subPremium","01"));
					} else {
						req.setSubPremium((req.getSubPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getSubPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.subPremium.required.format"),"subPremium","01"));
						}
					//	String ans = calcu.calculateXOL(req,"EGNPI",0,req.getSourceId());
						String premiumRate=StringUtils.isBlank(req.getEgnpiOffer())?"0":req.getEgnpiOffer().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(req.getOurAssessment())?"0":req.getOurAssessment().replaceAll(",", "");
						if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
						amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
						}
						String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getSubPremium().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"SubPremium","01"));
							
						}else{
							req.setSubPremium(ans);
						}
					}
					}
					if(StringUtils.isNotBlank(req.getPml()) && "Y".equalsIgnoreCase(req.getPml())){
						if(StringUtils.isBlank(req.getPmlPercent())){
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required"),"pmlpercent","01"));
						} else if (val.isValidNo(req.getPmlPercent().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.required.format"),"pmlpercent","01"));
						} else if (val.percentageValid(req.getPmlPercent().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.pmlpercent.checkgreater"),"pmlpercent","01"));
						}
						
					}
					if(StringUtils.isBlank(req.getPremiumbasis())){
						list.add(new ErrorCheck(prop.getProperty("error.Premiumbasis.required"),"Premiumbasis","01"));
					}
					else if("1".equalsIgnoreCase(req.getPremiumbasis())){
						if (StringUtils.isBlank(req.getAdjRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"adjRate","01"));
						} else if (val.isValidNo(req.getAdjRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"adjRate","01"));
						} else if (val.percentageValid(req.getAdjRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjrate.checkgreater"),"adjRate","01"));
						}
						if(StringUtils.isNotBlank(req.getBusinessType()) && "5".equalsIgnoreCase(req.getBusinessType())) {
							if (StringUtils.isBlank(req.getMinimumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
							} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
							}
							if (StringUtils.isBlank(req.getMaximumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
							} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
							}
							double min=Double.parseDouble(req.getMinimumRate());
							double max=Double.parseDouble(req.getMaximumRate());
							if(min>max){
								list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
							}
						}
					}
					else if("2".equalsIgnoreCase(req.getPremiumbasis())){
						if (StringUtils.isBlank(req.getMinimumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
						}
						if (StringUtils.isBlank(req.getMaximumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(req.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
						} else if (val.percentageValid(req.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
						}
						double min=Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate());
						double max=Double.parseDouble((req.getMaximumRate()==null||val.isNull(req.getMaximumRate()).equalsIgnoreCase(""))?"0":req.getMaximumRate());
						if(min>max){
							list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
						}
						
						if (val.percentageValid(req.getBurningCostLF().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF().checkgreater"),"BurningCostLF","01"));
						}
						
					}
					if (StringUtils.isBlank(req.getEpi())) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required"),"epiperCent","01"));
					} else {
						req.setEpi((req.getEpi()).replaceAll(",", ""));
						if (val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required.format"),"epiperCent","01"));
						}else{
							if(!"3".equalsIgnoreCase(req.getPremiumbasis())){
							//	String ans = calcu.calculateXOL(req,"EPI",0,req.getSourceId());
								double a=0;
								if("1".equalsIgnoreCase(req.getPremiumbasis())){
								a =  Double.parseDouble((req.getAdjRate()==null||val.isNull(req.getAdjRate()).equalsIgnoreCase(""))?"0":req.getAdjRate().replaceAll(",", ""));	
								}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
								a =  Double.parseDouble((req.getMinimumRate()==null||val.isNull(req.getMinimumRate()).equalsIgnoreCase(""))?"0":req.getMinimumRate().replaceAll(",", ""));	
								}
								String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
								if(!"0".equals(premiumRate)) {
								amt = (Double.parseDouble(premiumRate) * a)/100;
								}
								String ans = fm.formatter(Double.toString(amt)).replaceAll(",", "");
								if(Double.parseDouble(ans)!=Double.parseDouble(req.getEpi().replaceAll(",",""))){
									
								}else{
									req.setEpi(ans);
								}
							}
						}
					}
					if("3".equalsIgnoreCase(req.getPremiumbasis())){
						req.setMinimumRate(req.getEpi());
					}
					if(StringUtils.isBlank(req.getMinPremium())){
						 list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required"),"MinPremium","01"));
						}else {
							req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
							if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
							}else {
									req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
									if (val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
										list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
									}else if("2".equalsIgnoreCase(req.getPremiumbasis())){
									//	String ans = calcu.calculateXOL(req,"MinPremium",0,req.getSourceId());
										String premiumRate=StringUtils.isBlank(req.getSubPremium())?"0":req.getSubPremium().replaceAll(",", "");
										String coverlimit=StringUtils.isBlank(req.getMinimumRate())?"0":req.getMinimumRate().replaceAll(",", "");
										if(!"0".equals(premiumRate) && !"0".equals(coverlimit)) {
										amt = (Double.parseDouble(premiumRate) *Double.parseDouble(coverlimit))/100;
										}
										String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
										if(Double.parseDouble(ans)!=Double.parseDouble(req.getMinPremium().replaceAll(",",""))){
											
										}else{
											req.setMinPremium(ans);
										}
									}
								}
						}
					if (StringUtils.isBlank(req.getMdPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.depositPremium.required"),"depositPremium","01"));
					} else {
						req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
						if (val.isValidNo(req.getMdPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.depositPremium.required"),"depositPremium","01"));
						}
					}
					}
					if (val.isNull(req.getMdInstalmentNumber()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.no.of.Instalment.Required"),"MdInstalmentNumber","01"));
					} else if (val.isValidNo(req.getMdInstalmentNumber()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.no.of.Instalment.Required"),"MdInstalmentNumber","01"));
					}
					
					if(StringUtils.isBlank(req.getPaymentDuedays())){
						list.add(new ErrorCheck(prop.getProperty("error.PaymentDuedays.Required"),"PaymentDuedays","01"));
					}
					flags = true;
					double cedPer = 0.0;
					if (StringUtils.isBlank(req.getProStatus())) {
						list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
					}
					if("5".equalsIgnoreCase(req.getPid())){
						if(StringUtils.isBlank(req.getNoRetroCess())){
							list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession"),"NoRetroCess","01"));
						}
						else if("0".equalsIgnoreCase(req.getNoRetroCess())){
							list.add(new ErrorCheck(prop.getProperty("error.no.of.retro.cession.greater.than.zero"),"NoRetroCess","01"));
						}
					}
					if (StringUtils.isBlank(req.getShareWritt())) {
						if ("3".equals(req.getPid())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareWrit.required"),"ShareWritten","01"));
						}		
						flags = false;
						cedCheck = false;
					} else if ("A".equalsIgnoreCase(req.getProStatus()) && val.percentageNewValid(req.getShareWritt().trim()).equalsIgnoreCase("Invalid")) {
						if ("3".equals(req.getPid())) {
							list.add(new ErrorCheck(prop.getProperty("error.shareWrit.invalid"),"ShareWritten","01"));
						} 
						flags = false;
						cedCheck = false;
					} else {
						cedPer = Double.parseDouble(req.getShareWritt());
						if ("2".equals(req.getPid())	&& cedflag	&& Double.parseDouble(req.getShareWritt())	+ Double.parseDouble(req.getCedReten()) > 100) {
							list.add(new ErrorCheck(prop.getProperty("error.SWCedPer.invalid"),"ShareWritten","01"));
						}
					}
					
						
						if ("3".equalsIgnoreCase(req.getPid()) || "5".equalsIgnoreCase(req.getPid())) {
							if (StringUtils.isBlank(req.getLimitOrigCur())) {
								cedCheck = false;
							} else {
								req.setLimitOrigCur((req.getLimitOrigCur()).replaceAll(",", ""));
								if (val.isValidNo(req.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
									list.add(new ErrorCheck(prop.getProperty("error.limit.check"),"LimitOrigCur","01"));
									cedCheck = false;
								} else {
									amt = Double.parseDouble(req.getLimitOrigCur());
								}
							}
							if (StringUtils.isBlank(req.getDeduchunPercent())) {
								list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.required"),"DeduchunPercent","01"));
							} else {
								req.setDeduchunPercent((req.getDeduchunPercent()).replaceAll(",", ""));
								if (val.isValidNo(req.getDeduchunPercent().trim()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("error.deduc_hunPercent.check"),"DeduchunPercent","01"));
								}
							}
						}
						if ("3".equals(req.getPid())){
							if (cedCheck) {
								double amount = (amt * (cedPer / 100.0));
								amount = amount / Double.parseDouble(req.getExchRate());
								maxlimit = Double.parseDouble(req.getMaxLimitProduct());
							
								if (amount > maxlimit) {
									list.add(new ErrorCheck(prop.getProperty("error.accAmtlessUWAmt"),"accAmtlessUWAmt","01"));
								}
							}
						}
						if(StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getSharSign())){
							final  DecimalFormat twoDigit = new DecimalFormat("###0.00");
							final double dvalue = (Double.parseDouble(req.getMdPremium())* (Double.parseDouble(req.getSharSign())) / 100);
							final double dround = Math.round(dvalue * 100.0) / 100.0;
							final double valu = Double.parseDouble(twoDigit.format(dround));//A
							if(StringUtils.isNotBlank(req.getContNo())){
							double sumInst=dropDownImple.getSumOfInstallmentBooked(req.getContNo(), req.getLayerNo());//B
								if(valu<sumInst){
									list.add(new ErrorCheck(prop.getProperty("error.installment.premiumbooked.xol"),"premiumbooked","01"));
								}
							}
						}if(StringUtils.isNotBlank(req.getMdInstalmentNumber())){
							int count=dropDownImple.getCountOfInstallmentBooked(req.getContNo(), req.getLayerNo());
							if(Double.parseDouble(req.getMdInstalmentNumber())<count){
								list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked.xol"),"premiumbooked","01"));
							}
						}
						
						
					
				
					if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getEpi())) {
						req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
						req.setEpi((req.getEpi()).replaceAll(",", ""));
						if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getEpi().trim()).equalsIgnoreCase("INVALID")) {

							final float mdpremium = Float.parseFloat(req.getMdPremium());
							final float Pepi = Float.parseFloat(req.getEpi());

							if (mdpremium > Pepi) {
								list.add(new ErrorCheck(prop.getProperty("error.mdandpremium.difference.invalid"),"mdandpremium","01"));
							}
						}
					}
				
				if (StringUtils.isNotBlank(req.getMdPremium()) && StringUtils.isNotBlank(req.getMinPremium())) {
					req.setMdPremium((req.getMdPremium()).replaceAll(",", ""));
					req.setMinPremium((req.getMinPremium()).replaceAll(",", ""));
					if (!val.isValidNo(req.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(req.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {

						final float mdpremium = Float.parseFloat(req.getMdPremium());
						final float minp = Float.parseFloat(req.getMinPremium());

						if (mdpremium > minp) {
							list.add(new ErrorCheck(prop.getProperty("error.mdandminpremium.difference.invalid"),"mdandminpremium","01"));
						}
					}
				}
				if (!val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
					if (nonPropImple.getLayerDuplicationCheck(req.getBaseLayer(),req.getLayerNo(),req.getLayerMode()).getResponse().equalsIgnoreCase("true")) {
						list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"LayerDuplicationCheck","01"));
					}
				}
				if(StringUtils.isNotBlank(req.getEndorsementStatus())&& "Y".equalsIgnoreCase(req.getEndorsementStatus()) && StringUtils.isBlank(req.getDocStatus())) {
					list.add(new ErrorCheck(prop.getProperty("doc.status"),"EndorsementStatus","01"));
				}
//				if("3".equalsIgnoreCase(req.getProductId())){
//				validationContract();
//				}
//				validationRemarks();
			
		
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ErrorCheck> moveReinstatementMainVali(MoveReinstatementMainReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
			int j=1;
			Double val=0.00;
				for(int i=0;i<req.getReinstatementNo().size();i++){
					ReinstatementNoList req1 = req.getReinstatementNo().get(i);
					if(StringUtils.isBlank(req1.getReinstatementTypeId())){
							list.add(new ErrorCheck(prop.getProperty("Scale.error.typeId.val")+ String.valueOf(i + 1),"ReinstatementTypeId","01"));					
					}
					
					if(StringUtils.isBlank(req1.getReinstatementAmount())){
						list.add(new ErrorCheck(prop.getProperty("Scale.error.amount.val")+String.valueOf(i + 1),"ReinstatementAmount","01"));					
					}
//					if(StringUtils.isNotBlank(req1.getReinstatementTypeId())){
//							if(list.size()==2){
//								list=new ArrayList<String>();
//							}
//					}
//					if(list.size()!=2){
//						for(int k=0;k<list.size();k++){
//							list1.add(list.get(k));
//							}
//							list=new ArrayList<String>();
//						}
//				j++;
				}
				if(StringUtils.isBlank(req.getReinstatementOption())){
					list.add(new ErrorCheck(prop.getProperty("Scale.error.option.val"),"ReinstatementOption","01"));									
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
			return list;
	}
	public List<ErrorCheck> lowClaimBonusInserVali(LowClaimBonusInserReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		int j=1;
		int k=2;
		if(StringUtils.isBlank(bean.getBonusTypeId())){
			list.add(new ErrorCheck(prop.getProperty("bonus.error.type"),"BonusTypeId","01"));
			}
			else{
			for(int i=0;i<bean.getBonusReq().size();i++){
				BonusReq req = bean.getBonusReq().get(i);
				if(StringUtils.isBlank(req.getBonusFrom()) || StringUtils.isBlank(req.getBonusTo()) || StringUtils.isBlank(req.getBonusLowClaimBonus())){
					if(StringUtils.isBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) && StringUtils.isNotBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.from.val")+String.valueOf(i + 1),"BonusFrom","01"));
					}
					if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isBlank(req.getBonusTo()) && StringUtils.isNotBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.to.val")+String.valueOf(i + 1),"BonusTo","01"));
					}
					if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) && StringUtils.isBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.lcb.val")+String.valueOf(i + 1),"BonusLowClaimBonus","01"));
					}
					if(StringUtils.isBlank(req.getBonusFrom()) && StringUtils.isBlank(req.getBonusTo()) && StringUtils.isNotBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.from.val")+String.valueOf(i + 1),"BonusFrom","01"));
						list.add(new ErrorCheck(prop.getProperty("bonus.error.to.val")+String.valueOf(i + 1),"BonusTo","01"));
					}
					if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isBlank(req.getBonusTo()) && StringUtils.isBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.to.val")+String.valueOf(i + 1),"BonusFrom","01"));
						list.add(new ErrorCheck(prop.getProperty("bonus.error.lcb.val")+String.valueOf(i + 1),"BonusLowClaimBonus","01"));
					}
					if(StringUtils.isBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) && StringUtils.isBlank(req.getBonusLowClaimBonus())){
						list.add(new ErrorCheck(prop.getProperty("bonus.error.from.val")+String.valueOf(i + 1),"BonusFrom","01"));
						list.add(new ErrorCheck(prop.getProperty("bonus.error.lcb.val")+String.valueOf(i + 1),"BonusLowClaimBonus","01"));
					}
				}
				if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) && StringUtils.isNotBlank(req.getBonusLowClaimBonus())){
					if(Double.parseDouble(req.getBonusFrom().replace(",", ""))>Double.parseDouble(req.getBonusTo().replace(",", ""))){
						list.add(new ErrorCheck(prop.getProperty("bonus.loss.error.from.to.ratio.limit")+String.valueOf(i + 1),"BonusFrom","01"));
					}
					if("LR".equals(bean.getBonusTypeId())){
						if(Double.parseDouble(req.getBonusFrom().replace(",", ""))>1000){
							list.add(new ErrorCheck(prop.getProperty("bonus.loss.from.ratio.limit")+String.valueOf(i + 1),"BonusFrom","01"));
						}
						if(Double.parseDouble(req.getBonusTo().replace(",", ""))>1000){
							list.add(new ErrorCheck(prop.getProperty("bonus.loss.to.ratio.limit")+String.valueOf(i + 1),"BonusTo","01"));
						}
					}
					if(Double.parseDouble(req.getBonusLowClaimBonus().replace(",", ""))>100){
						list.add(new ErrorCheck(prop.getProperty("bonus.loss.lcb.ratio.limit")+String.valueOf(i + 1),"BonusLowClaimBonus","01"));
					}
					if(bean.getBonusReq().size()>j){
						if(StringUtils.isNotBlank(req.getBonusFrom()) ){
						double from =Double.parseDouble(req.getBonusFrom().replace(",", ""));
						double to = Double.parseDouble(req.getBonusTo().replace(",", ""));
						if(to>from){
							list.add(new ErrorCheck(prop.getProperty(("bonus.from.to.limit")+String.valueOf(i + 1),String.valueOf(i + 2)),"BonusTo","01"));
						}
						}
					}
					}
					j++;
					k++;
					}
			}
	return list;
	}
	public List<ErrorCheck> insertIEModuleVali(InsertIEModuleReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try{
			if(!(CollectionUtils.isEmpty(bean.getExcludedList()))){
				String errorProposal ="";
				GetCommonDropDownRes dropDownRes = dropDownImple.getProposalStatus(bean.getExcludeProposalNo());
				List<CommonResDropDown> receiveAdjustAmountMap	= dropDownRes.getCommonResponse();
				//HashMap<String,String> receiveAdjustAmountMap = dropDownImple.getProposalStatus(bean.getExcludeProposalNo());
				for(int i=0;i<bean.getExcludedList().size();i++){
				if(StringUtils.isNotBlank(receiveAdjustAmountMap.get(i).getCode())) {

					String status = receiveAdjustAmountMap.get(i).getCode();
					if("P".equalsIgnoreCase(status.trim())){
						errorProposal += receiveAdjustAmountMap.get(i).getCode()+",";
					}
				}else{
						errorProposal += receiveAdjustAmountMap.get(i).getCode()+",";
					}
				}
				if(StringUtils.isNotBlank(errorProposal)){
					errorProposal = errorProposal.substring(0, errorProposal.length() - 1);
					list.add(new ErrorCheck(prop.getProperty("error.in.proposal.no")+String.valueOf(errorProposal),"proposal","01"));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public List<ErrorCheck> showSecondpageEditItemsVali(ShowSecondpageEditItemsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getIncepDate())) {
			list.add(new ErrorCheck("Please Enter IncepDate", "IncepDate", "3"));
		}
//		if (StringUtils.isBlank(req.getLayerLayerNo())) {
//			list.add(new ErrorCheck("Please Enter LayerLayerNo", "LayerLayerNo", "4"));
//		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "5"));
		}
//		if (StringUtils.isBlank(req.getPaymentDuedays())) {
//			list.add(new ErrorCheck("Please Enter PaymentDuedays", "PaymentDuedays", "6"));
//		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
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
					//error.add(getText("error.remarks2",new String[]{String.valueOf(i+1)}));
					list.add(new ErrorCheck(prop.getProperty("error.remarks2")+String.valueOf(i+1),"Remark2","01"));
				}
			}
			
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return list;
	}	
	public List<ErrorCheck> getClassLimitDetailsVali(GetClassLimitDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
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
	public List<ErrorCheck> insertRetroCessVali(InsertRetroCessReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getNoRetroCess())) {
			list.add(new ErrorCheck("Please Enter NoRetroCess", "NoRetroCess", "5"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "6"));
		}
		return list;
	}
	public List<ErrorCheck> instalMentPremiumVali(InstalMentPremiumReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getContNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getExchangeRate())) {
			list.add(new ErrorCheck("Please Enter ExchangeRate", "ExchangeRate", "5"));
		}
		if (StringUtils.isBlank(req.getMdInstalmentNumber())) {
			list.add(new ErrorCheck("Please Enter MdInstalmentNumber", "MdInstalmentNumber", "6"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "7"));
		}
//		if (CollectionUtils.isEmpty(req.getInstalmentperiodReq())) {
//			list.add(new ErrorCheck("Please Enter Instalmentperiod", "Instalmentperiod", "8"));
//		}
		return list;
	}
	public List<ErrorCheck> insertRetroContractsVali(InsertRetroContractsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getNoInsurer())) {
			list.add(new ErrorCheck("Please Enter NoInsurer", "NoInsurer", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getRetentionPercentage())) {
			list.add(new ErrorCheck("Please Enter RetentionPercentage", "RetentionPercentage", "5"));
		}
		if (StringUtils.isBlank(req.getRetroDupContract())) {
			list.add(new ErrorCheck("Please Enter RetroDupContract", "RetroDupContract", "6"));
		}
		if (StringUtils.isBlank(req.getRetroDupYerar())) {
			list.add(new ErrorCheck("Please Enter RetroDupYerar", "RetroDupYerar", "7"));
		}
		if (StringUtils.isBlank(req.getRetroType())) {
			list.add(new ErrorCheck("Please Enter RetroType", "RetroType", "7"));
		}
		if (CollectionUtils.isEmpty(req.getRetroDetailReq())) {
			list.add(new ErrorCheck("Please Enter RetroDetail", "RetroDetail", "8"));
		}
		return list;
	}
	public List<ErrorCheck> reInstatementMainInsertVali(ReInstatementMainInsertReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "5"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "6"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "7"));
		}
		if (StringUtils.isBlank(req.getReInstatementPremium())) {
			list.add(new ErrorCheck("Please Enter ReInstatementPremium", "ReInstatementPremium", "8"));
		}
		return list;
	}
	public List<ErrorCheck> validateinsertCrestaMaintable(CrestaSaveReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "3"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "5"));
		}
			return list;
	}
	public List<ErrorCheck> insertBonusDetailsVali(InsertBonusDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getAmendId())) {
			list.add(new ErrorCheck("Please Enter AmendId", "AmendId", "3"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "4"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "5"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "6"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
		}
		if (StringUtils.isBlank(req.getAcqBonus())) {
			list.add(new ErrorCheck("Please Enter AcqBonus", "AcqBonus", "9"));
		}
		if (StringUtils.isBlank(req.getEndorsmentno())) {
			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "9"));
		}
		return list;
	}
	public List<ErrorCheck> getLayerInfoVali(GetLayerInfoReq req) {
		// TODO Auto-generated method stub
		return null;
	}
}
