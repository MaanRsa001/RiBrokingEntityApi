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
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmount;
import com.maan.insurance.model.req.nonproportionality.CoverList;
import com.maan.insurance.model.req.nonproportionality.CrestaSaveReq;
import com.maan.insurance.model.req.nonproportionality.GetLayerInfoReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertBonusDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertIEModuleReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;
import com.maan.insurance.model.req.nonproportionality.MoveReinstatementMainReq;
import com.maan.insurance.model.req.nonproportionality.ReInstatementMainInsertReq;
import com.maan.insurance.model.req.nonproportionality.ReinstatementNoList;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
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
import com.maan.insurance.model.req.proportionality.GetTreatyNameDuplicationCheckReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetContractValRes;
import com.maan.insurance.model.res.DropDown.GetContractValidationRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.nonproportionality.NonProportionalityCustomRepository;
import com.maan.insurance.service.impl.nonproportionality.NonProportionalityServiceImpl;
import com.maan.insurance.service.impl.proportionality.ProportionalityServiceImpl;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Validation;
@Service
public class NonProportionalityValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(NonProportionalityValidation.class);
	private Properties prop = new Properties();
	CommonCalculation calcu = new CommonCalculation();
	
	@Autowired
	private DropDownServiceImple dropDownImple;
	
	@Autowired
	private NonProportionalityServiceImpl nonPropImple;

	@Autowired
	private RskPremiumDetailsRepository rskPremiumDetailsRepository;
	
	@Autowired
	private NonProportionalityCustomRepository nonProportionalityCustomRepository;
	
	@Autowired
	private Formatters fm;

	
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
		
		try {
		final Validation val = new Validation();
		Map<String, Object> map = null;
		List<Map<String, Object>> relist = nonPropImple.getValidation(req.getIncepDate(),req.getRenewalcontractNo());
		if (relist != null && relist.size() > 0) {
			map = (Map<String, Object>) relist.get(0);
		}
		if(StringUtils.isBlank(req.getBouquetModeYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.bouquetModeYn.required"),"BouquetModeYN","01"));
		}
		
		if (val.isSelect(req.getCedingCo()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"CedingCo","02"));
		}
		if (val.isNull(req.getIncepDate()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"IncepDate","03"));
		} else if (val.checkDate(req.getIncepDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"IncepDate","03"));
		} else if (StringUtils.isNotBlank((req.getRenewalcontractNo()))&& !"0".equals(req.getRenewalcontractNo())&& map != null) {
			if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), req.getIncepDate()))) {
				list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"DateValidate","03"));
			}else {
				req.setRenewalFlag("NEWCONTNO");
			}
		}
		if (val.isNull(req.getExpDate()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"ExpDate", "04"));
			
		} else if (val.checkDate(req.getExpDate()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpDate", "05"));
		}
		if (!req.getIncepDate().equalsIgnoreCase("")&& !req.getExpDate().equalsIgnoreCase("")) {
			if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"IncepDate", "06"));
			}
		}
		if (val.isSelect(req.getUwYear()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"UWYear", "07"));
		}if (val.isSelect(req.getUwYearTo()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.uwYearTo.UwYearTo"),"UwYearTo", "08"));
		}
		if (StringUtils.isNotBlank(req.getIncepDate())&& StringUtils.isNotBlank(req.getExpDate())) {
			if (Validation.ValidateTwo(req.getIncepDate(),req.getExpDate()).equalsIgnoreCase("Invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"IncepDate", "08"));
			}
		}
		if (val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
			list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"LayerNo", "09"));
		} else if (val.isValidNo(req.getLayerNo()).equalsIgnoreCase("INVALID")) {
			list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"LayerNo", "10"));
		}
		if (!val.isNull(req.getLayerNo()).equalsIgnoreCase("")) {
			if (nonPropImple.getLayerDuplicationCheck(req.getProposalNo(),req.getLayerNo(),req.getBaseLayer()).getResponse().equals("true")) {
				list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"Layer", "11"));
			}
		}
		if(StringUtils.isNotBlank(req.getTreatyNameType())) {
			GetTreatyNameDuplicationCheckReq req1=new GetTreatyNameDuplicationCheckReq();
			req1.setProposalNo(req.getProposalNo());
			req1.setBaseLayer(req.getBaseLayer());
			req1.setTreatyNameType(req.getTreatyNameType());
			if (nonPropImple.getTreatYNameDuplicationCheck(req1).getResponse().equalsIgnoreCase("true")) {
				list.add(new ErrorCheck("Treaty / Section Name Already Exists", "TreatySectionDuplicate", "11"));
			}
		}
		if("Y".equals(req.getBouquetModeYN()) && StringUtils.isNotBlank(req.getBouquetNo())) {
			if (dropDownImple.getBouquetCedentBrokercheck(req.getBouquetNo(),req.getCedingCo(),req.getBroker()).getResponse().equals("true")) {
				list.add(new ErrorCheck(prop.getProperty("error.brokercedent.duplicate"),"Broker", "12"));
			}
		}
		if(StringUtils.isBlank(req.getRiskdetailYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"ALLDetails", "08"));
		}
		if(StringUtils.isBlank(req.getBrokerdetYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"IncepDate", "08"));
		}
		if(StringUtils.isBlank(req.getPremiumdetailYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"IncepDate", "08"));
		}
		if(StringUtils.isBlank(req.getInstallYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.InstallYN.required"),"IncepDate", "08"));
		}
		if(StringUtils.isBlank(req.getAcqdetailYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"IncepDate", "08"));
		}
		if(StringUtils.isBlank(req.getReinstdetailYN())) {
			list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"IncepDate", "08"));
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ErrorCheck> insertClassLimitVali(insertClassLimitReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if("Y".equals(req.getContractMode())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}

		if (StringUtils.isBlank(req.getBusinessType())) {
		list.add(new ErrorCheck("Please Enter BusinessType", "BusinessType", "3"));
		}
		
		if(!"5".equalsIgnoreCase(req.getBusinessType())){
			for(int i=0;i<req.getCoverList().size();i++) {
			CoverList req2 = req.getCoverList().get(i);
			if(StringUtils.isBlank(req2.getCoverLimitOC())) {
				list.add(new ErrorCheck("Please Enter CoverLimitOC","CoverLimitOC", "5" ));
			}
			if(StringUtils.isBlank(req2.getCoverdepartId())) {
				list.add(new ErrorCheck("Please Enter CoverdepartId","CoverdepartId", "6" ));
			}
			if(StringUtils.isBlank(req2.getDeductableLimitOC())) {
				list.add(new ErrorCheck("Please Enter DeductableLimitOC","DeductableLimitOC", "7" ));
			}
			if(StringUtils.isBlank(req2.getEgnpiAsPerOff())) {
				list.add(new ErrorCheck("Please Enter EgnpiAsPerOff","EgnpiAsPerOff", "8" ));
			}
			if(StringUtils.isBlank(req2.getGnpiAsPO())) {
				list.add(new ErrorCheck("Please Enter GnpiAsPO","GnpiAsPO", "9" ));
			}
			if(StringUtils.isBlank(req2.getNetMaxRetentPer())) {
				list.add(new ErrorCheck("Please Enter NetMaxRetentPer","NetMaxRetentPer", "10" ));
			}
			}
		}else {
			for(int i=0;i<req.getCoverLimitAmount().size();i++) {
			CoverLimitAmount req2 = req.getCoverLimitAmount().get(i);
			if(StringUtils.isBlank(req2.getCoverLimitAmount())) {
				list.add(new ErrorCheck("Please Enter CoverLimitAmount","CoverLimitAmount", "11" ));
			}
			if(StringUtils.isBlank(req2.getCoverLimitPercent())) {
				list.add(new ErrorCheck("Please Enter CoverLimitPercent","CoverLimitPercent", "12" ));
			}
			if(StringUtils.isBlank(req2.getCoverdepartIdS())) {
				list.add(new ErrorCheck("Please Enter CoverdepartIdS","CoverdepartIdS", "13" ));
			}
			if(StringUtils.isBlank(req2.getDeductableLimitAmount())) {
				list.add(new ErrorCheck("Please Enter DeductableLimitAmount","DeductableLimitAmount", "14" ));
			}
			if(StringUtils.isBlank(req2.getDeductableLimitPercent())) {
				list.add(new ErrorCheck("Please Enter DeductableLimitPercent","DeductableLimitPercent", "15" ));
			}
			if(StringUtils.isBlank(req2.getEgnpiAsPerOffSlide())) {
				list.add(new ErrorCheck("Please Enter EgnpiAsPerOffSlide","EgnpiAsPerOffSlide", "16" ));
			}
			if(StringUtils.isBlank(req2.getGnpiAsPOSlide())) {
				list.add(new ErrorCheck("Please Enter GnpiAsPOSlide","GnpiAsPOSlide", "17" ));
			}
		}
		}
		
		
		if (StringUtils.isBlank(req.getLayerNo())) {
		list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "18"));
		}
		if (StringUtils.isBlank(req.getProductid())) {
			list.add(new ErrorCheck("Please Enter Productid", "Productid", "19"));
			}
		if (StringUtils.isBlank(req.getProposalno())) {
			list.add(new ErrorCheck("Please Enter Proposalno", "Proposalno", "20"));
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
	
	
	public List<ErrorCheck> insertProportionalTreatyvali(insertProportionalTreatyReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
			  if("Y".equals(req.getContractMode())) { 
				  list =  validateNext(req); 
			}else {
				list =   validateOffer(req);
				}
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	private List<ErrorCheck> validateOffer(insertProportionalTreatyReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
			final Validation val = new Validation();
			Map<String, Object> map = null;
			List<Map<String, Object>> list1 = nonPropImple.getValidation(bean.getIncepDate(), bean.getRenewalcontractno());
			if (list1 != null && list1.size() > 0) {
				map = (Map<String, Object>) list1.get(0);
			}
			if(StringUtils.isBlank(bean.getBouquetModeYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.bouquetModeYn.required"),"bouquetModeYn","01"));
			}
			
			if (val.isSelect(bean.getCedingCo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
			}
			if (val.isNull(bean.getIncepDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
			} else if (val.checkDate(bean.getIncepDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
			} else if (StringUtils.isNotBlank((bean.getRenewalcontractno()))&& !"0".equals(bean.getRenewalcontractno())&& map != null) {
				if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), bean.getIncepDate()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
				}else {
					bean.setRenewalFlag("NEWCONTNO");
				}
			}
			if (val.isNull(bean.getExpDate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"ExpiryDate","01"));
			} else if (val.checkDate(bean.getExpDate()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
			}
			if (StringUtils.isNotBlank(bean.getIncepDate()) && StringUtils.isNotBlank(bean.getExpDate())) {
				if (Validation.ValidateTwo(bean.getIncepDate(),bean.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
				}
			}
			if (val.isSelect(bean.getUwYear()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
			}if (val.isSelect(bean.getUwYearTo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYearTo.required"),"uwYearTo","01"));
			}
			if (StringUtils.isNotBlank(bean.getIncepDate())&& StringUtils.isNotBlank(bean.getExpDate())) {
				if (Validation.ValidateTwo(bean.getIncepDate(),bean.getExpDate()).equalsIgnoreCase("Invalid")) {
					list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
				}
			}
			if (val.isNull(bean.getNewLayerNo()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.required"),"NewLayerNo","01"));
			} else if (val.isValidNo(bean.getNewLayerNo()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.layerNo.error"),"NewLayerNo","01"));
			}
			if (!val.isNull(bean.getNewLayerNo()).equalsIgnoreCase("")) {
				if (nonPropImple.getLayerDuplicationCheck(bean.getProposalNo(),bean.getNewLayerNo(),bean.getBaseLayer()).getResponse().equalsIgnoreCase("true")) {
				
					list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"layer","01"));
				}
			}
			if(StringUtils.isNotBlank(bean.getTreatyNametype())) {
				GetTreatyNameDuplicationCheckReq req1=new GetTreatyNameDuplicationCheckReq();
				req1.setProposalNo(bean.getProposalNo());
				req1.setBaseLayer(bean.getBaseLayer());
				req1.setTreatyNameType(bean.getTreatyNametype());
				if (nonPropImple.getTreatYNameDuplicationCheck(req1).getResponse().equalsIgnoreCase("true")) {
					list.add(new ErrorCheck("Treaty / Section Name Already Exists", "TreatySectionDuplicate", "11"));
				}
			}
			if("Y".equals(bean.getBouquetModeYN()) && StringUtils.isNotBlank(bean.getBouquetNo())) {
				if (dropDownImple.getBouquetCedentBrokercheck(bean.getBouquetNo(),bean.getCedingCo(),bean.getBroker()).getResponse().equalsIgnoreCase("true")) {
					list.add(new ErrorCheck(prop.getProperty("error.brokercedent.duplicate"),"brokercedent","01"));
				}
			}
			if(StringUtils.isBlank(bean.getRiskdetailYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
			}
			if(StringUtils.isBlank(bean.getBrokerdetYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
			}
			if(StringUtils.isBlank(bean.getPremiumdetailYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
			}
			if(StringUtils.isBlank(bean.getInstallYN())) {
					list.add(new ErrorCheck(prop.getProperty("error.InstallYN.required"),"InstallYN","01"));
			}
			if(StringUtils.isBlank(bean.getAcqdetailYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
			}
			if(StringUtils.isBlank(bean.getReinstdetailYN())) {
				list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private List<ErrorCheck> validateNext(insertProportionalTreatyReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
			//validateNext
			
				boolean flags = true;
				boolean cedCheck = true;
				boolean cedflag = true;
				final Validation val = new Validation();
				Map<String, Object> map = null;
				List<Map<String, Object>> list1 = nonPropImple.getValidation(bean.getIncepDate(), bean.getRenewalcontractno());
				if (list1 != null && list1.size() > 0) {
					map = (Map<String, Object>) list1.get(0);
				}
				if(StringUtils.isNotBlank(bean.getAmendId())&& Integer.parseInt(bean.getAmendId())>0 && bean.getProductId().equalsIgnoreCase("3")){
					if(StringUtils.isBlank(bean.getEndorsmenttype())){
						list.add(new ErrorCheck(prop.getProperty("end.type.error"),"type","01"));
					}
				}
				if(StringUtils.isBlank(bean.getBouquetModeYN())) {
					list.add(new ErrorCheck(prop.getProperty("error.bouquetModeYn.required"),"bouquetModeYn","01"));
				}
				
				if (val.isSelect(bean.getCedingCo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedingCo.required"),"cedingCo","01"));
				}
				if (val.isNull(bean.getIncepDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
				} else if (val.checkDate(bean.getIncepDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
				} else if (StringUtils.isNotBlank((bean.getRenewalcontractno()))&& !"0".equals(bean.getRenewalcontractno())&& map != null) {
					if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), bean.getIncepDate()))) {
						list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"incepDate","01"));
					}else {
						bean.setRenewalFlag("NEWCONTNO");
					}
				}
				if (val.isNull(bean.getExpDate()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"ExpiryDate","01"));
				} else if (val.checkDate(bean.getExpDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.ExpiryDate.Error"),"ExpiryDate","01"));
				}
				if (StringUtils.isNotBlank(bean.getIncepDate()) && StringUtils.isNotBlank(bean.getExpDate())) {
					if (Validation.ValidateTwo(bean.getIncepDate(),bean.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.expDate.check"),"ExpiryDate","01"));
					}
				}
				if (val.isSelect(bean.getUwYear()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"uwYear","01"));
				}if (val.isSelect(bean.getUwYearTo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.uwYearTo.required"),"uwYearTo","01"));
				}
				if (StringUtils.isNotBlank(bean.getIncepDate())&& StringUtils.isNotBlank(bean.getExpDate())) {
					if (Validation.ValidateTwo(bean.getIncepDate(),bean.getExpDate()).equalsIgnoreCase("Invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.accDate.check1"),"accDate","01"));
					}
				}
				if (val.isNull(bean.getNewLayerNo()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.NewLayerNo.required"),"layerNo","01"));
				} else if (val.isValidNo(bean.getNewLayerNo()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("error.NewLayerNo.error"),"NewLayerNo","01"));
				}
				if (!val.isNull(bean.getNewLayerNo()).equalsIgnoreCase("")) {
					if (nonPropImple.getLayerDuplicationCheck(bean.getProposalNo(),bean.getNewLayerNo(),bean.getBaseLayer()).getResponse().equalsIgnoreCase("true")) {
					
						list.add(new ErrorCheck(prop.getProperty("error.layer.duplicate"),"layer","01"));
					}
				}
				
				if(StringUtils.isBlank(bean.getRiskdetailYN())) {
					list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
				}else if("Y".equals(bean.getRiskdetailYN())) {
					if (StringUtils.isBlank(val.isSelect(bean.getOrginalCurrency()))) {
						list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
					}
					/*if(StringUtils.isBlank(bean.getExchangeType())){
						list.add(new ErrorCheck(prop.getProperty("error.ExchangeType.required"),"","01"));
					}*/
					if (StringUtils.isBlank(bean.getExchangeRate())) {
						list.add(new ErrorCheck(prop.getProperty("error.exchRate.required"),"exchRate","01"));
						cedCheck = false;
					} else if (val.isValidNo(bean.getExchangeRate().trim().toString()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.exchRate.check"),"exchRate","01"));
						cedCheck = false;
					}
					if (StringUtils.isBlank(bean.getTreatyNametype())) {
						if("3".equalsIgnoreCase(bean.getProductId())) {
							list.add(new ErrorCheck(prop.getProperty("error.treatyName_type.required"),"treatyName_type","01"));
						}
						else if("5".equalsIgnoreCase(bean.getProductId())){
							list.add(new ErrorCheck(prop.getProperty("error.retroTreatyName.required"),"retroTreatyName","01"));
						}
					}else {
						if(StringUtils.isNotBlank(bean.getTreatyNametype())) {
							GetTreatyNameDuplicationCheckReq req1=new GetTreatyNameDuplicationCheckReq();
							req1.setProposalNo(bean.getProposalNo());
							req1.setBaseLayer(bean.getBaseLayer());
							req1.setTreatyNameType(bean.getTreatyNametype());
							if (nonPropImple.getTreatYNameDuplicationCheck(req1).getResponse().equalsIgnoreCase("true")) {
								list.add(new ErrorCheck("Treaty / Section Name Already Exists", "TreatySectionDuplicate", "11"));
							}
						}
					}
					if(StringUtils.isBlank(bean.getBusinessType())){
						list.add(new ErrorCheck(prop.getProperty("error.BusinessType.required"),"BusinessType","01"));
					}
					if (val.isSelect(bean.getDepartmentId()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"departId","01"));
					}
					if (StringUtils.isBlank(bean.getBasis())) {
						list.add(new ErrorCheck(prop.getProperty("error.basic.required"),"basic","01"));
					}
					List<String> deptId = new ArrayList<>();
					if(StringUtils.isNotBlank(bean.getBusinessType()) &&(!"5".equalsIgnoreCase(bean.getBusinessType()))){
						for(int i=0;i<bean.getCoverList().size();i++){
							CoverList req = bean.getCoverList().get(i);
							if(StringUtils.isBlank(req.getCoverdepartId())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+String.valueOf(i + 1),"CoverdepartId","01"));
								deptId.add(req.getCoverdepartId());
								}
							if(StringUtils.isBlank(req.getCoverLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitOC")+String.valueOf(i + 1),"CoverLimitOC","01"));
							}
							if(StringUtils.isBlank(req.getDeductableLimitOC())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitOC")+String.valueOf(i + 1),"DeductableLimitOC","01"));
							}
							if(StringUtils.isBlank(req.getEgnpiAsPerOff()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+String.valueOf(i + 1),"egnpi","01"));
							}
							if(StringUtils.isNotBlank(bean.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(bean.getEndorsmenttype())){
								if(StringUtils.isBlank(req.getGnpiAsPO()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"gnpi","01"));
								}
							}
						}
						if(dropDownImple.findDuplicates(deptId).size()>0){
							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"CoverdepartId","01"));
						}
						if("17".equalsIgnoreCase(bean.getDepartmentId()) || "18".equalsIgnoreCase(bean.getDepartmentId()) || "19".equalsIgnoreCase(bean.getDepartmentId())){
							if(deptId.contains(bean.getDepartmentId()) && deptId.size()>1){
								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"CoverdepartId","01"));
							}
						}
					}
					if(StringUtils.isNotBlank(bean.getBusinessType()) &&("5".equalsIgnoreCase(bean.getBusinessType()))){
						for(int i=0;i<bean.getCoverLimitAmount().size();i++){
							CoverLimitAmount req = bean.getCoverLimitAmount().get(i);
							if(StringUtils.isBlank(req.getCoverdepartIdS()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverdepartId")+String.valueOf(i + 1),"CoverdepartId","01"));
							}
							if(StringUtils.isBlank(req.getCoverLimitAmount()) && StringUtils.isNotBlank(req.getCoverLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+String.valueOf(i + 1),"CoverLimitAmount","01"));
							}
							if(StringUtils.isBlank(req.getCoverLimitPercent())&& StringUtils.isNotBlank(req.getCoverLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+String.valueOf(i + 1),"CoverLimittPercent","01"));
							}
							if(StringUtils.isBlank(req.getDeductableLimitAmount()) &&StringUtils.isNotBlank(req.getDeductableLimitPercent())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+String.valueOf(i + 1),"DeductableLimitAmount","01"));
							}
							if(StringUtils.isBlank(req.getDeductableLimitPercent()) && StringUtils.isNotBlank(req.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+String.valueOf(i + 1),"DeductableLimitPercent","01"));
							}
							if(StringUtils.isBlank(req.getCoverLimitAmount()) && StringUtils.isBlank(req.getCoverLimitPercent()) && StringUtils.isBlank(req.getDeductableLimitPercent()) && StringUtils.isBlank(req.getDeductableLimitAmount())){
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimitAmount")+String.valueOf(i + 1),"CoverLimitAmount","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.CoverLimittPercent")+String.valueOf(i + 1),"CoverLimittPercent","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitAmount")+String.valueOf(i + 1),"DeductableLimitPercent","01"));
								list.add(new ErrorCheck(prop.getProperty("error.enter.DeductableLimitPercent")+String.valueOf(i + 1),"DeductableLimitPercent","01"));
							}
							if(StringUtils.isBlank(req.getEgnpiAsPerOffSlide()) ){
								list.add(new ErrorCheck(prop.getProperty("error.enter.egnpi.as.per.off")+String.valueOf(i + 1),"egnpi","01"));
							}
							if(StringUtils.isNotBlank(bean.getEndorsmenttype()) &&"GNPI".equalsIgnoreCase(bean.getEndorsmenttype())){
								if(StringUtils.isBlank(req.getGnpiAsPOSlide()) ){
									list.add(new ErrorCheck(prop.getProperty("error.enter.gnpi.as.per.off")+String.valueOf(i + 1),"gnpi","01"));
								}
							}
							
						}
						if(dropDownImple.findDuplicates(deptId).size()>0){
							list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.duplicate"),"CoverdepartId","01"));
						}
						if("17".equalsIgnoreCase(bean.getDepartmentId()) || "18".equalsIgnoreCase(bean.getDepartmentId()) || "19".equalsIgnoreCase(bean.getDepartmentId())){
							if(deptId.contains(bean.getDepartmentId()) && deptId.size()>1){
								list.add(new ErrorCheck(prop.getProperty("error.CoverdepartId.groped"),"CoverdepartId","01"));
							}
						}
					}
					
					if(StringUtils.isNotBlank(bean.getBusinessType()) &&(!"4".equalsIgnoreCase(bean.getBusinessType()) && !"5".equalsIgnoreCase(bean.getBusinessType()))){
						
					if(!"5".equalsIgnoreCase(bean.getBusinessType())){
						/*if(StringUtils.isBlank(bean.getEvent_limit())){
							list.add(new ErrorCheck(prop.getProperty("error.eventLimit.required"),"","01"));
							}else {
								bean.setEvent_limit((bean.getEvent_limit()).replaceAll(",", ""),"","01"));
								if (val.isValidNo(bean.getEvent_limit().trim()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("error.eventLimit.required"),"","01"));
								}
							}*/
					}
					if(StringUtils.isNotBlank(bean.getUmbrellaXL()) && "Y".equalsIgnoreCase(bean.getUmbrellaXL())){
					
					if(StringUtils.isBlank(bean.getCoverLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required"),"CoverLimitXL","01"));
						}else {
							bean.setCoverLimitXL((bean.getCoverLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(bean.getCoverLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.CoverLimitXL.required.format"),"CoverLimitXL","01"));
							}
						}
					if(StringUtils.isBlank(bean.getDeductLimitXL())){
						list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required"),"DeductLimitXL","01"));
						}else {
							bean.setDeductLimitXL((bean.getDeductLimitXL()).replaceAll(",", ""));
							if (val.isValidNo(bean.getDeductLimitXL().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.DeductLimitXL.required.format"),"DeductLimitXL","01"));
							}
						}
					
					}
				}
				}

				if(StringUtils.isBlank(bean.getPremiumdetailYN())) {
					list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
				}else if("Y".equals(bean.getPremiumdetailYN())) {
					if (StringUtils.isBlank(bean.getSubPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.subPremium.required"),"subPremium","01"));
					} else {
						bean.setSubPremium((bean.getSubPremium()).replaceAll(",", ""));
						if (val.isValidNo(bean.getSubPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.subPremium.required.format"),"subPremium","01"));
						}
						
					}
					if(StringUtils.isBlank(bean.getPremiumbasis())){
						list.add(new ErrorCheck(prop.getProperty("error.Premiumbasis.required"),"Premiumbasis","01"));
					}
					else if("1".equalsIgnoreCase(bean.getPremiumbasis())){
						if (StringUtils.isBlank(bean.getAdjRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required"),"adjRate","01"));
						} else if (val.isValidNo(bean.getAdjRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjRate.required.format"),"adjRate","01"));
						} else if (val.percentageValid(bean.getAdjRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.adjrate.checkgreater"),"adjRate","01"));
						}
						if(StringUtils.isNotBlank(bean.getBusinessType()) && "5".equalsIgnoreCase(bean.getBusinessType())) {
							if (StringUtils.isBlank(bean.getMinimumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(bean.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required.format"),"MinimumRate","01"));
							} else if (val.percentageValid(bean.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
							}
							if (StringUtils.isBlank(bean.getMaximumRate())) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MinimumRate","01"));
							} else if (val.isValidNo(bean.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required.format"),"MinimumRate","01"));
							} else if (val.percentageValid(bean.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MinimumRate","01"));
							}
							double min=Double.parseDouble(bean.getMinimumRate());
							double max=Double.parseDouble(bean.getMaximumRate());
							if(min>max){
								list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
							}
						}
					}
					else if("2".equalsIgnoreCase(bean.getPremiumbasis())){
						if (StringUtils.isBlank(bean.getMinimumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.isValidNo(bean.getMinimumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.required"),"MinimumRate","01"));
						} else if (val.percentageValid(bean.getMinimumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MinimumRate.checkgreater"),"MinimumRate","01"));
						}
						if (StringUtils.isBlank(bean.getMaximumRate())) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
						} else if (val.isValidNo(bean.getMaximumRate().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.required"),"MaximumRate","01"));
						} else if (val.percentageValid(bean.getMaximumRate().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.MaximumRate.checkgreater"),"MaximumRate","01"));
						}
						double min=Double.parseDouble(bean.getMinimumRate());
						double max=Double.parseDouble(bean.getMaximumRate());
						if(min>max){
							list.add(new ErrorCheck(prop.getProperty("min.rate.less"),"rate","01"));
						}
						if(StringUtils.isBlank(bean.getBurningCostLF())){
							list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF.required"),"BurningCostLF","01"));
						}
						if (val.percentageValid(bean.getBurningCostLF().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.BurningCostLF().checkgreater"),"BurningCostLF","01"));
						}
						
					}
					if (StringUtils.isBlank(bean.getEpi())) {
						list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required"),"epiperCent","01"));
					} else {
						bean.setEpi((bean.getEpi()).replaceAll(",", ""));
						if (val.isValidNo(bean.getEpi().trim()).equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.epiperCent.required.format"),"epiperCent","01"));
						}else{
							if(!"3".equalsIgnoreCase(bean.getPremiumbasis())){
							//	String ans = calcu.calculateXOL(bean,"EPI",0,bean.getSourceId());

								double a=0;
								double amt=0.0;
								if("1".equalsIgnoreCase(bean.getPremiumbasis())){
								a =  Double.parseDouble((bean.getAdjRate()==null||bean.getAdjRate().equalsIgnoreCase(""))?"0":bean.getAdjRate().replaceAll(",", ""));	
								}else if("2".equalsIgnoreCase(bean.getPremiumbasis())){
								a =  Double.parseDouble((bean.getMinimumRate()==null||bean.getMinimumRate().equalsIgnoreCase(""))?"0":bean.getMinimumRate().replaceAll(",", ""));	
								}
								String premiumRate=StringUtils.isBlank(bean.getSubPremium())?"0":bean.getSubPremium().replaceAll(",", "");
								amt = (Double.parseDouble(premiumRate) * a)/100;
								String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
							
								
								if(Double.parseDouble(ans)!=Double.parseDouble(bean.getEpi().replaceAll(",",""))){
									//list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"));
									
								}else{
									bean.setEpi(ans);
								}
							}
						}
					}
					if("3".equalsIgnoreCase(bean.getPremiumbasis())){
						bean.setMinimumRate(bean.getEpi());
					}
					if(StringUtils.isBlank(bean.getMinPremium())){
						 list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required"),"MinPremium","01"));
						}else {
							bean.setMinPremium((bean.getMinPremium()).replaceAll(",", ""));
							if (val.isValidNo(bean.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("error.MinPremium.required.format"),"MinPremium","01"));
							}else if("2".equalsIgnoreCase(bean.getPremiumbasis())){
								double amt = 0.0;
								//String ans = calcu.calculateXOL(bean,"MinPremium",0,bean.getSourceId());
								String premiumRate=StringUtils.isBlank(bean.getSubPremium())?"0":bean.getSubPremium().replaceAll(",", "");
								String coverlimit=StringUtils.isBlank(bean.getMinimumRate())?"0":bean.getMinimumRate().replaceAll(",", "");
								amt = (Double.parseDouble(premiumRate) *Double.parseDouble(coverlimit))/100;
								String ans =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
								
								if(Double.parseDouble(ans)!=Double.parseDouble(bean.getMinPremium().replaceAll(",",""))){
									//list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"","01"));
								}else{
									bean.setMinPremium(ans);
								}
							}
						}
					if (StringUtils.isBlank(bean.getMinimumpremiumPercent())) {
						list.add(new ErrorCheck(prop.getProperty("error.MinimumpremiumPercent.required1"),"MinimumpremiumPercent","01"));
					}else if(Double.valueOf(bean.getMinimumpremiumPercent())>999){
						list.add(new ErrorCheck(prop.getProperty("error.MinimumpremiumPercent.less"),"MinimumpremiumPercent","01"));
					}
					if (StringUtils.isBlank(bean.getGnpiCapPercent())) {
						list.add(new ErrorCheck(prop.getProperty("error.GnpiCapPercent.required1"),"GnpiCapPercent","01"));
					}else if(Double.valueOf(bean.getGnpiCapPercent())>999){
						list.add(new ErrorCheck(prop.getProperty("error.GnpiCapPercent.less"),"GnpiCapPercent","01"));
					}
					
					
					if (StringUtils.isBlank(bean.getMdPremium())) {
						list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required1"),"m_dPremium","01"));
					} else {
						bean.setMdPremium((bean.getMdPremium()).replaceAll(",", ""));
						if (val.isValidNo(bean.getMdPremium().trim()).equalsIgnoreCase("INVALID")) {
							list.add(new ErrorCheck(prop.getProperty("error.m_dPremium.required.format"),"m_dPremium","01"));
						}
					}
					if (StringUtils.isNotBlank(bean.getMdPremium()) && StringUtils.isNotBlank(bean.getEpi())) {
						bean.setMdPremium((bean.getMdPremium()).replaceAll(",", ""));
						bean.setEpi((bean.getEpi()).replaceAll(",", ""));
						if (!val.isValidNo(bean.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(bean.getEpi().trim()).equalsIgnoreCase("INVALID")) {

							final float mdpremium = Float.parseFloat(bean.getMdPremium());
							final float Pepi = Float.parseFloat(bean.getEpi());

							if (mdpremium > Pepi) {
								list.add(new ErrorCheck(prop.getProperty("error.mdandpremium.difference.invalid1"),"mdandpremium","01"));
							}
						}
					}
				
				if (StringUtils.isNotBlank(bean.getMdPremium()) && StringUtils.isNotBlank(bean.getMinPremium())) {
					bean.setMdPremium((bean.getMdPremium()).replaceAll(",", ""));
					bean.setMinPremium((bean.getMinPremium()).replaceAll(",", ""));
					if (!val.isValidNo(bean.getMdPremium().trim())	.equalsIgnoreCase("INVALID")&& !val.isValidNo(bean.getMinPremium().trim()).equalsIgnoreCase("INVALID")) {

						final float mdpremium = Float.parseFloat(bean.getMdPremium());
						final float minp = Float.parseFloat(bean.getMinPremium());

						if (mdpremium > minp) {
							list.add(new ErrorCheck(prop.getProperty("error.mdandminpremium.difference.invalid"),"mdandpremium","01"));
						}
					}
				}
					if (val.isNull(bean.getMdInstalmentNumber()).equalsIgnoreCase("")) {
						list.add(new ErrorCheck(prop.getProperty("error.Instalment.error"),"Instalment","01"));
						
					} else if (val.isValidNo(bean.getMdInstalmentNumber()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("error.Instalment.Required"),"Instalment","01"));
					}
					if(StringUtils.isBlank(bean.getRateOnLine())) {
						list.add(new ErrorCheck(prop.getProperty("error.rateonline.Required"),"rateonline","01"));
					}
				}
				//-------->instalMentPremiumVali
				if(StringUtils.isBlank(bean.getInstallYN())) {
					list.add(new ErrorCheck(prop.getProperty("error.InstallYN.required"),"InstallYN","01"));
				}else if("Y".equalsIgnoreCase(bean.getInstallYN()) && StringUtils.isNotBlank(bean.getMdInstalmentNumber())) {
				if ("3".equals(bean.getProductId()) || "5".equals(bean.getProductId())) {
					int instalmentperiod = Integer.parseInt(bean.getMdInstalmentNumber());
					double totalInstPremium=0.0;
					boolean tata = false;
					double mndPremiumOC=Double.parseDouble(bean.getMdPremium().replaceAll(",", ""));
					double minPremiumOC=Double.parseDouble(bean.getMinPremium().replaceAll(",", ""));// 
					
					for (int i = 0; i < instalmentperiod; i++) {
						InstalmentperiodReq req = bean.getInstalmentperiodReq().get(i);
						if(StringUtils.isBlank(req.getInstalmentDateList())) {
							list.add(new ErrorCheck("Please Select Installment Date for Row " + String.valueOf(i + 1),"Intallment Date","01"));
						}else if (!val.isNull(req.getInstalmentDateList()).equalsIgnoreCase("")) {
							if (val.ValidateINstallDates(bean.getIncepDate(),bean.getInstalmentperiodReq().get(0).getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
								tata = true;
							}
						}else if (!val.isNull(req.getInstalmentDateList()).equalsIgnoreCase("")) {
							if (val.ValidateTwoDates(req.getInstalmentDateList(),bean.getExpDate()).equalsIgnoreCase("Invalid")) {
								list.add(new ErrorCheck(prop.getProperty("Error.Select.Expirydate")+String.valueOf(i + 1),"Expirydate","01"));
							}
						}
						
						if (!val.isNull(req.getInstallmentPremium()).equalsIgnoreCase("")) {
							
							try{
								if(Double.parseDouble(req.getInstallmentPremium().replaceAll(",", ""))<0) {
									list.add(new ErrorCheck(prop.getProperty("Error.installment.Premium")+String.valueOf(i + 1),"installment","01"));
								}else {
									totalInstPremium+=Double.parseDouble(req.getInstallmentPremium().replaceAll(",", "")); 
								}
				            }catch (Exception e) {
				            	list.add(new ErrorCheck(prop.getProperty("Error.installment.Premium")+String.valueOf(i + 1),"installment","01"));
							}
						}
						if (i != 0) {
							if (!val.isNull(req.getInstalmentDateList()).equalsIgnoreCase("")) {
								if (val.ValidateTwoDates( bean.getInstalmentperiodReq().get(i-1).getInstalmentDateList(),	req.getInstalmentDateList()).equalsIgnoreCase("Invalid")) {
									list.add(new ErrorCheck(prop.getProperty("Error.required.InstalDate")+ String.valueOf(i + 1),"InstalDate","01"));
								}
							}
						}
						
					}
					BigDecimal bd = new BigDecimal(totalInstPremium).setScale(2, RoundingMode.HALF_EVEN);
					totalInstPremium = bd.doubleValue();
					if((totalInstPremium)!=mndPremiumOC){
						list.add(new ErrorCheck(prop.getProperty("Error.total.installment.premium")+" Deposit Premium - Our Share - OC","installment","01"));
				    }
					if (tata == true) {
						list.add(new ErrorCheck(prop.getProperty("Error.Select.AfterInceptionDate"),"AfterInceptionDate","01"));
					}
					totalInstPremium = 1000;
				if((totalInstPremium)!=mndPremiumOC && (totalInstPremium)!=minPremiumOC)	{
					list.add(new ErrorCheck(prop.getProperty("Error.Select.totalInstPremium"),"totalInstPremium","01"));
					}
				if(StringUtils.isNotBlank(bean.getMdInstalmentNumber())){
					int count=dropDownImple.getCountOfInstallmentBooked(bean.getContNo(), bean.getLayerNo());
					if(Double.parseDouble(bean.getMdInstalmentNumber())<count){
						list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumbooked.xol"),"premiumbooked","01"));
					}
					int instaNoList = nonProportionalityCustomRepository.getCountOfInstallmentNumber(bean.getProposalNo());
					
					if(Integer.valueOf(bean.getMdInstalmentNumber())<instaNoList) {
						list.add(new ErrorCheck(prop.getProperty("error.no.installment.premiumNumber.xol"),"premiumNumber","01"));	
					}
					
				}
			}
				}

				//----------------savesecondpage
						if(StringUtils.isBlank(bean.getBrokerdetYN())) {
							list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
						}else if("Y".equals(bean.getBrokerdetYN())) {
							if (StringUtils.isBlank(bean.getBroker())) {
								list.add(new ErrorCheck(prop.getProperty("error.broker.required"),"broker","01"));
							}
							if(StringUtils.isBlank(bean.getPaymentPartner())) {
								list.add(new ErrorCheck(prop.getProperty("error.PaymentPartner.required"),"PaymentPartner","01"));
							}
							if (val.isNull(bean.getLeaderUnderwriter()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second"),"leader_Underwriter","01"));
							}
							if("RI02".equalsIgnoreCase(bean.getSourceId()) && "3".equalsIgnoreCase(bean.getProductId())){
								if(StringUtils.isBlank(bean.getLeaderUnderwritercountry())){
									list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter.second.country"),"leader_Underwriter","01"));
								}
							}
							if (val.isNull(bean.getUnderwriterRecommendations()).equalsIgnoreCase("")) {
								//list.add(new ErrorCheck(prop.getProperty("errors.underwriter_Recommendations.second"),"","01"));
							}
							if (val.isNull(bean.getLeaderUnderwritershare()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second"),"LeaderUnderwritershare","01"));
							} else if (val.percentageValid(bean.getLeaderUnderwritershare()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.second1"),"LeaderUnderwritershare","01"));
							} else if (val.percentageValid(bean.getLeaderUnderwritershare()).trim().equalsIgnoreCase("less")) {
								list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.secondless"),"LeaderUnderwritershareless","01"));
							} else if (val.percentageValid(bean.getLeaderUnderwritershare()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.leader_Underwriter_share.secondgreater"),"LeaderUnderwritersharegreater","01"));
							}
							
						}
						if(StringUtils.isBlank(bean.getAcqdetailYN())) {
							list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
						}else if("Y".equals(bean.getAcqdetailYN())) {
							if (val.isNull(bean.getBrokerage()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second"),"brokerage","01"));
							} else if (val.percentageValid(bean.getBrokerage()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second1"),"brokerage","01"));
							} else if (val.percentageValid(bean.getBrokerage()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondgreater"),"brokerage","01"));
							}
							if (val.isNull(bean.getTax()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.tax.second"),"tax","01"));
							} else if (val.percentageValid(bean.getTax()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.tax.second1"),"tax","01"));
							} else if (val.percentageValid(bean.getTax()).trim().equalsIgnoreCase("less")) {
								list.add(new ErrorCheck(prop.getProperty("errors.tax.secondless"),"tax","01"));
							} else if (val.percentageValid(bean.getTax()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.tax.secondgreater"),"tax","01"));
							}
							if (val.isNull(bean.getOthercost()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"),"othercost","01"));
							} else if (val.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("INVALID")) {
								list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondinvalid"),"othercost","01"));
							} else if (val.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("less")) {
								list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondless"),"othercost","01"));
							} else if (val.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("greater")) {
								list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondgreater"),"othercost","01"));
							}
							if ((!"4".equalsIgnoreCase(bean.getProductId())) && (!"5".equalsIgnoreCase(bean.getProductId()))) {
								
								if(StringUtils.isBlank(bean.getAcqBonus())){
									//list.add(new ErrorCheck(prop.getProperty("bonus.error.bonus"));
								}
								else{
									if("LCB".equalsIgnoreCase(bean.getAcqBonus())){
										/*if(StringUtils.isBlank(bean.getBonusPopUp())){
						                    list.add(new ErrorCheck(prop.getProperty("bonus.popup.recheck"),"popup","01"));
						                }else{*/
										int count = nonPropImple.getBonusListCount(bean);
										if(count<=0){
											list.add(new ErrorCheck(prop.getProperty("bonus.error.lcb.table.empty"),"lcb","01"));
										}
										/* } */
									}
									else if("NCB".equalsIgnoreCase(bean.getAcqBonus())){
									   if(StringUtils.isBlank(bean.getAcqBonusPercentage())){
										   list.add(new ErrorCheck(prop.getProperty("bonus.error.noclaimbonu.per"),"noclaimbonu","01"));
										}
									   else if(100<Double.parseDouble(bean.getAcqBonusPercentage())){
										list.add(new ErrorCheck(prop.getProperty("bonus.error.low.claim.bonus.range"),"lowclaim","01"));
										}
									}
									
								}
							}
							if (val.isNull(bean.getAcquisitionCost()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second"),"AcquisitionCost","01"));
							} else {
								bean.setAcquisitionCost((bean.getAcquisitionCost()).replaceAll(",", ""));
								if (val.isValidNo(bean.getAcquisitionCost()).trim().equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("errors.acquisition_Cost.second1"),"AcquisitionCost","01"));
								}
							}
						}
						if(StringUtils.isBlank(bean.getReinstdetailYN())) {
							list.add(new ErrorCheck(prop.getProperty("error.alldetails.required"),"alldetails","01"));
						}else if("Y".equals(bean.getReinstdetailYN())) {
							if(StringUtils.isBlank(bean.getReInstatementPremium())){
								list.add(new ErrorCheck(prop.getProperty("error.ReinstatementPremium.required"),"Reinstatement Premium","01"));
							}
							else if("Y".equalsIgnoreCase(bean.getReInstatementPremium())){
								/*if(StringUtils.isBlank(bean.getReinsPopUp())){
				                    list.add(new ErrorCheck(prop.getProperty("reins.popup.recheck"),"popup","01"));
				                }else{*/
								int count=nonPropImple.getReInstatementCount(bean.getAmendId(),bean.getProposalNo(),bean.getBranchCode(),bean.getReferenceNo());
								if(count<=0){
									list.add(new ErrorCheck(prop.getProperty("errors.reinstatement.schedule"),"reinstatementschedule","01"));
								}
								/* } */
							}
							if (val.isNull(bean.getAnualAggregateLiability()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateLiability.number"),"AnualAggregateLiability","01"));
							} else {
								bean.setAnualAggregateLiability((bean.getAnualAggregateLiability()).replaceAll(",", ""));
								if (val.isValidNo(bean.getAnualAggregateLiability()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateLiability.number.format"),"AnualAggregateLiability","01"));
								}
							}
							
							if (val.isNull(bean.getAnualAggregateDeduct()).equalsIgnoreCase("")) {
								list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateDeduct.number"),"AnualAggregateDeduct","01"));
							} else {
								bean.setAnualAggregateDeduct((bean.getAnualAggregateDeduct()).replaceAll(",", ""));
								if (val.isValidNo(bean.getAnualAggregateDeduct()).equalsIgnoreCase("INVALID")) {
									list.add(new ErrorCheck(prop.getProperty("errors.AnualAggregateDeduct.number.format"),"AnualAggregateDeduct","01"));
								}
							}
						}
						if(dropDownImple.getBaseContractValid(bean.getBranchCode(),bean.getProposalNo(), bean.getProductId())) {
						//	list.add(new ErrorCheck(prop.getProperty("errors.base.contract")+bean.getBaseLayerYN(),"","01"));
						}
				
				if("3".equalsIgnoreCase(bean.getProductId())){
				//	validationContract();
					}
				/*validationRemarks();
				validateRiDetail();*/
			
		}catch(Exception e) {
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
						if(to<from){
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
					//list.add(new ErrorCheck(prop.getProperty("error.remarks1")+String.valueOf(i+1),"Remark1","01"));
				}
				if(StringUtils.isBlank(req1.getRemark2())){
					//error.add(getText("error.remarks2",new String[]{String.valueOf(i+1)}));
					//list.add(new ErrorCheck(prop.getProperty("error.remarks2")+String.valueOf(i+1),"Remark2","01"));
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
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "4"));
		}
		return list;
	}
}
