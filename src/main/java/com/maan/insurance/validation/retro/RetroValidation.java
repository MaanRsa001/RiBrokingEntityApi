package com.maan.insurance.validation.retro;

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
import com.maan.insurance.model.req.DropDown.DuplicateCountCheckReq;
import com.maan.insurance.model.req.retro.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.retro.InsertProfitCommissionMainReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.NoRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.retro.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.retro.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.retro.UpdateRiskProposalReq;
import com.maan.insurance.model.req.retro.FirstInsertReq;
import com.maan.insurance.model.req.retro.GetEndDateReq;
import com.maan.insurance.model.req.retro.InsertBonusDetailsReq;
import com.maan.insurance.model.req.retro.InsertRemarkDetailsReq;
import com.maan.insurance.model.req.retro.InsertRetroDetailsReq;
import com.maan.insurance.model.req.retro.SaveSecondPageReq;
import com.maan.insurance.model.req.retro.ShowSecondPageData1Req;
import com.maan.insurance.model.req.retro.ShowSecondPageDataReq;
import com.maan.insurance.model.req.retro.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.retro.ViewRiskDetailsReq;
import com.maan.insurance.model.res.retro.FirstInsertRes1;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.retro.RetroServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
@Service
public class RetroValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(RetroValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private RetroServiceImple retroImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public RetroValidation() {
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
	public List<ErrorCheck> firstInsertVali(FirstInsertReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		FirstInsertRes1 res1 = new FirstInsertRes1();
		//ValidateNext()
		try {
			boolean flags = true;
			final Validation val = new Validation();
			final String tear_nt = val.isNull(bean.getTreatyNametype());
			final String brok = val.isSelect(bean.getBroker());
			final String incDate = val.checkDate(bean.getIncepDate());
			final String expdate = val.checkDate(bean.getExpDate());
			final String accDate = val.checkDate(bean.getAccDate());
			final String exchRate = val.isNull(bean.getExchRate());
			final String riskCover = val.isNull(bean.getRiskCovered());
			final String terrtyscope = val.isNull(bean.getTerritoryscope());
			final String limitPercent = val.isNull(bean.getLimitOrigCur());
			final String epiPercent = val.isNull(bean.getEpiorigCur());
			final String Epi = val.isNull(bean.getEpi());
			final String xlCost = val.isNull(bean.getXlCost());
			final String proStatus = val.isSelect(bean.getProStatus());
			final String shareWrit = val.isNull(bean.getShareWritt());
			final String shareSign = val.isNull(bean.getSharSign());
			final String orginalCurrency = val.isSelect(bean.getOrginalCurrency());
			if(StringUtils.isNotBlank(bean.getAmendId())&& Integer.parseInt(bean.getAmendId())>0){
				if(StringUtils.isBlank(bean.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
				}
			}
			if (StringUtils.isBlank(val.isSelect(bean.getDepartId()))) {
				list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"DepartId","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getSubProfitcenter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"subProfitcenter","01"));
			}else{
				bean.setSubProfitcenter((bean.getSubProfitcenter()).replaceAll(" ", ""));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getProfitCenter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.Profit_Center.required"),"ProfitCenter","01"));
			}
			Map<String, Object> map = null;
			List<Map<String, Object>> list1 = retroImpl.getValidation(bean.getIncepDate(),bean.getRenewalcontractno());
			if (list1 != null && list1.size() > 0) {
				map = (Map<String, Object>) list1.get(0);
			}
			
			if (StringUtils.isBlank(val.isNull(bean.getUnderwriter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
			}
			if (StringUtils.isBlank(val.isNull(bean.getPolBr()))) {
				list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"polBr","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getCedingCo()))) {
				list.add(new ErrorCheck(prop.getProperty("error.leadRetrocessionaire.required"),"leadRetrocessionaire","01"));
			}
			if (StringUtils.isBlank(brok)) {
				list.add(new ErrorCheck(prop.getProperty("error.leadRetroBroker.required"),"leadRetroBroker","01"));
			}
			if (StringUtils.isBlank(bean.getDummyCon())) {
				list.add(new ErrorCheck(prop.getProperty("error.dummy.required"),"dummy","01"));
			}
			if ("4".equals(bean.getProductId()) && StringUtils.isBlank((bean.getRetroType()))) {
				list.add(new ErrorCheck(prop.getProperty("error.retroType.required.retro"),"retroType","01"));
			}
			if ("4".equals(bean.getProductId()) && "SR".equalsIgnoreCase(bean.getRetroType()) && StringUtils.isBlank(bean.getInsuredName())) {
				list.add(new ErrorCheck(prop.getProperty("errors.insuredName.required"),"insuredName","01"));
			}
			if("TR".equalsIgnoreCase(bean.getRetroType())){
				if (StringUtils.isBlank(val.isSelect(bean.getTreatyType()))) {
					list.add(new ErrorCheck(prop.getProperty("error.retroTreatyType.Reqired"),"retroTreatyType","01"));
				}
			}
			if("TR".equalsIgnoreCase(bean.getRetroType()) || "FO".equalsIgnoreCase(bean.getRetroType())){
				if (StringUtils.isBlank(tear_nt)) {
					list.add(new ErrorCheck(prop.getProperty("error.retroTreatyName.required"),"retroTreatyType","01"));
				}
			}
			if (StringUtils.isBlank(val.isNull(bean.getIncepDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
			} else if (incDate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
			} else if (!"".equals(bean.getRenewalcontractno())&& !"0".equals(bean.getRenewalcontractno())&& map != null) {
				if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), bean.getIncepDate()))) {
					list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
				}else {
					res1.setRenewalFlag("NEWCONTNO");
				}
			}
			if (StringUtils.isBlank(val.isNull(bean.getExpDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate","01"));
			} else if (expdate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.expdate.check"),"expDate","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getUwYear()))) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"","01"));
			} else if (!"".equals(bean.getRenewalcontractno()) && !"0".equals(bean.getRenewalcontractno()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(bean.getUwYear())) {
				//list.add(new ErrorCheck(prop.getProperty("errors.year.invalid"));
			}
			if("D".equalsIgnoreCase(bean.getDummyCon())){
				DuplicateCountCheckReq req = new DuplicateCountCheckReq();
				req.setBranchCode(bean.getBranchCode());
				req.setPid(bean.getProductId());
				req.setProposalNo(bean.getProposalNo());
				req.setType("D");
				req.setUwYear(bean.getUwYear());
				int count = dropDowmImpl.DuplicateCountCheck(req);
				if(count>0){
					list.add(new ErrorCheck(prop.getProperty("error.dup.uwyewar"),"DuplicateCount","01"));
				}
			}
			if (StringUtils.isNotBlank(bean.getProStatus()) && "A".equalsIgnoreCase(bean.getProStatus())&&StringUtils.isBlank(bean.getAccDate())) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.required"),"accDate","01"));
			}else if (StringUtils.isNotBlank(bean.getAccDate()) &&accDate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
			}
			if (StringUtils.isBlank(orginalCurrency)) {
				list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
			}
			if (StringUtils.isBlank(bean.getExchRate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.exchange.required"),"exchange","01"));
			}
			if (StringUtils.isBlank(bean.getCessionExgRate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.cession.exchange.required"),"CessionExgRate","01"));
			}
			else{
				if("F".equalsIgnoreCase(bean.getCessionExgRate()) && StringUtils.isBlank(bean.getFixedRate())){
					list.add(new ErrorCheck(prop.getProperty("errors.fixedrate.required"),"fixedrate","01"));
				}
				else{
					bean.setFixedRate(bean.getFixedRate().replace(",", ""));
				}
			}
			if (StringUtils.isBlank(val.isNull(bean.getNoRetroCess()))) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.required"),"noOfRetroCess","01"));
			} else if (val.isValidNo(bean.getNoRetroCess()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.invalid"),"noOfRetroCess","01"));
			} else if (Integer.parseInt(bean.getNoRetroCess()) < 1) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.gte1"),"noOfRetroCess","01"));
			}

			if (StringUtils.isBlank(bean.getTerritoryscope())) {
				list.add(new ErrorCheck(prop.getProperty("error.terrtoryScope.required"),"terrtoryScope","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getProposalType()))) {
				list.add(new ErrorCheck(prop.getProperty("error.cleancutoff.required"),"cleancutoff","01"));
			}
		else if("R".equalsIgnoreCase(bean.getProposalType()) || "H".equalsIgnoreCase(bean.getProposalType())){
				if(StringUtils.isBlank(bean.getRunoffYear())){
					list.add(new ErrorCheck(prop.getProperty("error.runoff.required"),"runoff","01"));	
				}
			}
			if (StringUtils.isBlank(riskCover)) {
				list.add(new ErrorCheck(prop.getProperty("error.portfolio.Reqired"),"portfolio","01"));
			}
			if (StringUtils.isBlank(bean.getLOCIssued())) {
				list.add(new ErrorCheck(prop.getProperty("error.locissued.required"),"locissued","01"));
			}
			else if("Y".equalsIgnoreCase(bean.getLOCIssued())){
					if(StringUtils.isBlank(bean.getLocBankName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank","01"));
					}
					if(StringUtils.isBlank(bean.getLocCreditPrd())){
						list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd","01"));
					}
					if(StringUtils.isBlank(bean.getLocCreditAmt())){
						list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt","01"));
					}
					else{
						bean.setLocCreditAmt(bean.getLocCreditAmt().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(bean.getLocBeneficerName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName","01"));
					}
				}
				
			if (bean.getPnoc().equalsIgnoreCase("-1")) {
				list.add(new ErrorCheck(prop.getProperty("error.pnoc.required"),"pnoc","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getAccountingPeriod())) || bean.getAccountingPeriod().equalsIgnoreCase("-1")) {
				list.add(new ErrorCheck(prop.getProperty("error.AccountionPeriod.reqired"),"AccountionPeriod","01"));
			}
			if (StringUtils.isBlank(bean.getReceiptofStatements())) {
				list.add(new ErrorCheck(prop.getProperty("Error.ResciptStatments.Required"),"ResciptStatments","01"));
			} else if (val.isValidNo(bean.getReceiptofStatements()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("Error.ReceiptofStatmenst.Error"),"ResciptStatments","01"));
			}
			if (StringUtils.isBlank(bean.getReceiptofPayment())) {
				list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.required"),"ResciptStatments","01"));
			} else if (val.isValidNo(bean.getReceiptofPayment()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.Error"),"ResciptStatments","01"));
			}

			if (StringUtils.isBlank(bean.getTerritory())) {
				list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode","01"));
			}
			if (StringUtils.isBlank(bean.getCountryIncludedList())) {
				list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude","01"));
			}
			boolean cedflag = true;
			double amt = 0.0;
			boolean cedCheck = true;
			final String cenRent = val.isNull(bean.getCedReten());
			if (val.isNull(bean.getCedRetenType()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required.our"),"cedRentType","01"));
				cedflag = false;
			} else {
				if (cenRent.equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.cedRent.required.our"),"cedRent","01"));
					cedflag = false;
				} else {
					bean.setCedReten((bean.getCedReten()).replaceAll(",", ""));
					if ("A".equalsIgnoreCase(bean.getCedRetenType())) {
						cedflag = false;
						if (val.isValidNo(bean.getCedReten()).trim().equalsIgnoreCase("Invalid")) {
							list.add(new ErrorCheck(prop.getProperty("error.cedRentAmt.required.our"),"cedRentAmt","01"));
						}
					} else if ("P".equalsIgnoreCase(bean.getCedRetenType())) {
						if (val.percentageValid(bean.getCedReten()).trim().equalsIgnoreCase("Invalid") || val.percentageValid(bean.getCedReten().trim()).equalsIgnoreCase("less") || val.percentageValid(bean.getCedReten().trim()).equalsIgnoreCase("greater")) {
							list.add(new ErrorCheck(prop.getProperty("error.cedRentPer.required.our"),"cedRentPer","01"));
							cedflag = false;
						}
					}
				}
			}
			if(StringUtils.isNotBlank(bean.getRetroType()) && "TR".equalsIgnoreCase(bean.getRetroType())){
			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("1")) {
				if (limitPercent.equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.required.qs"),"limitOrigCurr","01"));
					cedCheck = false;
				} else {
					bean.setLimitOrigCur((bean.getLimitOrigCur()).replaceAll(",", ""));
					if (val.isValidNo(bean.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.check.qs"),"limitOrigCurr","01"));
						cedCheck = false;
					} else {
						amt = Double.parseDouble(bean.getLimitOrigCur());
					}
				}
			}
		
			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("2")) {
				if (StringUtils.isBlank(bean.getTreatynoofLine())) {
					list.add(new ErrorCheck(prop.getProperty("error.noonline.required"),"noonline","01"));
				}
			}
			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("2")) {
				if (StringUtils.isBlank(bean.getTreatyLimitsurplusOC())) {
					list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.required"),"TreatyLimitsurplusOC","01"));
					cedCheck = false;
				} else {
					bean.setTreatyLimitsurplusOC((bean.getTreatyLimitsurplusOC()).replaceAll(",", ""));
					if (val.isValidNo(bean.getTreatyLimitsurplusOC()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.check"),"TreatyLimitsurplusOC","01"));
						cedCheck = false;
					} else {
						amt = Double.parseDouble(bean.getTreatyLimitsurplusOC());
					}
				}
			}
			}
			else{
				if (StringUtils.isBlank(bean.getFaclimitOrigCur())) {
					list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.required"),"limitOrigCurr","01"));
					cedCheck = false;
				} else {
					bean.setFaclimitOrigCur((bean.getFaclimitOrigCur()).replaceAll(",", ""));
					if (val.isValidNo(bean.getFaclimitOrigCur()).equalsIgnoreCase("invalid")) {
						list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.check"),"limitOrigCurr","01"));
						cedCheck = false;
					} else {
						amt = Double.parseDouble(bean.getFaclimitOrigCur());
					}
				}
			}
			if (StringUtils.isBlank(bean.getPml())) {
				list.add(new ErrorCheck(prop.getProperty("error.pml.required"),"pml","01"));
			} else if ("Y".equalsIgnoreCase(bean.getPml())) {
				if (StringUtils.isBlank(bean.getPmlPercent())) {
					list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.required"),"pmlpercentage","01"));
				} else {
					double pmlper = Double.parseDouble(bean.getPmlPercent());
					if (pmlper > 100) {
						list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.less.100.required"),"pmlpercentage","01"));
					}
				}
			
			if (StringUtils.isBlank(bean.getEpi())) {
				list.add(new ErrorCheck(prop.getProperty("error.epiRetro.required"),"epiRetro","01"));
			}

			if ("".equalsIgnoreCase(proStatus)) {
				list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
			}
			//validationRemarks();
		} }catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<ErrorCheck> showSecondpageEditItemsVali(ShowSecondpageEditItemsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		return list;
	}
	public List<ErrorCheck> validationRemarks(InsertRemarkDetailsReq req) {
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
	public List<ErrorCheck> showRetroCessVali(ShowRetroCess1Req req) {

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
	public List<ErrorCheck> showSecondPageData1Vali(ShowSecondPageData1Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getProposal())) {
			list.add(new ErrorCheck("Please Enter Proposal", "Proposal", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		return list;	
	}
	public List<ErrorCheck> showSecondPageData1Vali(ShowSecondPageDataReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getProposal())) {
			list.add(new ErrorCheck("Please Enter Proposal", "Proposal", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getReMode())) {
			list.add(new ErrorCheck("Please Enter ReMode", "ReMode", "5"));
		}
		return list;	
	}
	public List<ErrorCheck> validateSecondPage(SaveSecondPageReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
			double amt = 0.0;
			double totShAcc = 0.0;
			double totShsg = 0.0;
			Validation validation = new Validation();
			if ("3".equalsIgnoreCase(bean.getTreatyType()) || "2".equalsIgnoreCase(bean.getTreatyType())) {
				if (StringUtils.isBlank(bean.getPremiumSurplus())) {
					list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Required"),"PremiumSurplus","01"));
				} else if (StringUtils.isNotBlank(bean.getPremiumSurplus())) {
					bean.setPremiumSurplus((bean.getPremiumSurplus()).replaceAll(",", ""));
					if (validation.isValidNo(bean.getPremiumSurplus()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Invalid"),"PremiumSurplus","01"));
					}else{
					//	String ans = calcu.calculateXOL(bean,"PremiumSur",0,bean.getSourceId());
						String premiumRate=StringUtils.isBlank(bean.getEpi())?"0":bean.getEpi().replaceAll(",", "");
						String coverlimit=StringUtils.isBlank(bean.getPremiumQuotaShare())?"0":bean.getPremiumQuotaShare().replaceAll(",", "");
						amt = (Double.parseDouble(premiumRate) - Double.parseDouble(coverlimit));
						String ans = fm.formatter(Double.toString(amt)).replaceAll(",", "");
						
						if(Double.parseDouble(ans)!=Double.parseDouble(bean.getPremiumSurplus().replaceAll(",",""))){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"PremiumSurplus","01"));
						}else{
							bean.setPremiumSurplus(ans);
						}
					}
				}
				if(Double.parseDouble(bean.getPremiumSurplus())<0){
					list.add(new ErrorCheck(prop.getProperty("Errors.PremiumSurplus.Isnotless"),"PremiumSurplus","01"));
				}
			}
			if ("3".equalsIgnoreCase(bean.getTreatyType()) || "1".equalsIgnoreCase(bean.getTreatyType())) {
				if (StringUtils.isBlank(bean.getPremiumQuotaShare())) {
					list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuotaShare.Required"),"PremiumQuotaShare","01"));
				} else if (StringUtils.isNotBlank(bean.getPremiumQuotaShare())) {
					bean.setPremiumQuotaShare((bean.getPremiumQuotaShare()).replaceAll(",", ""));
					if (validation.isValidNo(bean.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Errors.PremiumQuota.Invalid"),"PremiumQuota","01"));
					}
				}
			}
			if(StringUtils.isBlank(bean.getTreatyType())){
				if (StringUtils.isBlank(bean.getPremiumQuotaShare())) {
					list.add(new ErrorCheck(prop.getProperty("Errors.Premium100OC.Required"),"Premium100OC","01"));
				} else if (StringUtils.isNotBlank(bean.getPremiumQuotaShare())) {
					bean.setPremiumQuotaShare((bean.getPremiumQuotaShare()).replaceAll(",", ""));
					if (validation.isValidNo(bean.getPremiumQuotaShare()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("Errors.Premium100OC.Invalid"),"Premium100OC","01"));
					}
				}
			}
			if(StringUtils.isBlank(bean.getLocRate())){
				list.add(new ErrorCheck(prop.getProperty("label.rate.year.error"),"rate","01"));
			}
			if(StringUtils.isBlank(bean.getRetroCommissionType())){
				list.add(new ErrorCheck(prop.getProperty("label.com.type.error"),"rate","01"));
			}
			if ("3".equalsIgnoreCase(bean.getTreatyType()) || "1".equalsIgnoreCase(bean.getTreatyType())) {
				if (StringUtils.isBlank(bean.getCommissionQS())) {
					list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second"),"CommissionQS","01"));
				} else if (!bean.getCommissionQS().equalsIgnoreCase("")) {
					if (validation.percentageValid(bean.getCommissionQS().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1"),"CommissionQS","01"));
					} else if (validation.percentageValid(bean.getCommissionQS().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1less"),"CommissionQS","01"));
					} else if (validation.percentageValid(bean.getCommissionQS().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commissionQ_S.second1greater"),"CommissionQS","01"));
					}
					bean.setCommissionQSAmt(validation.isNull(bean.getCommissionQSAmt()).replaceAll(",", ""));
				}
			}
			if ("3".equalsIgnoreCase(bean.getTreatyType()) || "2".equalsIgnoreCase(bean.getTreatyType())) {
				if (StringUtils.isBlank(bean.getCommissionsurp())) {
					list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second.req"),"Commissionsurp","01"));
				} else if (!bean.getCommissionsurp().equalsIgnoreCase("")) {
					if (validation.percentageValid(bean.getCommissionsurp().trim()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.second1"),"Commissionsurp","01"));
					} else if (validation.percentageValid(bean.getCommissionsurp().trim()).equalsIgnoreCase("less")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondless"),"Commissionsurpsecondless","01"));
					} else if (validation.percentageValid(bean.getCommissionsurp().trim()).equalsIgnoreCase("greater")) {
						list.add(new ErrorCheck(prop.getProperty("errors.commission_surp.secondgreater"),"Commissionsurpsecondgreater","01"));
					}
					bean.setCommissionsurpAmt(validation.isNull(bean.getCommissionsurpAmt()).replaceAll(",", ""));
				}
			}

			if (StringUtils.isBlank(bean.getOverRidder())) {
				list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second.req"),"overRidder","01"));
			} else if (StringUtils.isNotBlank(bean.getOverRidder())) {
				if (validation.percentageValid(bean.getOverRidder().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.overRidder.second1"),"overRidder","01"));
				} else if (validation.percentageValid(bean.getOverRidder().trim()).equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondless"),"overRidder","01"));
				} else if (validation.percentageValid(bean.getOverRidder().trim()).equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.overRidder.secondgreater"),"overRidder","01"));
				}
			}

			if (StringUtils.isBlank(bean.getBrokerage())) {
				list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second"),"brokerage","01"));
			} else if (StringUtils.isNotBlank(bean.getBrokerage())) {
				if (validation.percentageValid(bean.getBrokerage()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.second1"),"brokerage","01"));
				} else if (validation.percentageValid(bean.getBrokerage()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondless"),"brokerage","01"));
				} else if (validation.percentageValid(bean.getBrokerage()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.brokerage.secondgreater"),"brokerage","01"));
				}
			}

			if (StringUtils.isBlank(bean.getTax())) {
				list.add(new ErrorCheck(prop.getProperty("errors.tax.second"),"tax","01"));
			} else if (StringUtils.isNotBlank(bean.getTax())) {
				if (validation.percentageValid(bean.getTax()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.second1"),"tax","01"));
				} else if (validation.percentageValid(bean.getTax()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondless"),"tax","01"));
				} else if (validation.percentageValid(bean.getTax()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.tax.secondgreater"),"tax","01"));
				}
			}

			if (StringUtils.isBlank(bean.getOthercost())) {
				list.add(new ErrorCheck(prop.getProperty("errors.othercost.second"),"othercost","01"));
			} else if (!validation.isNull(bean.getOthercost()).equalsIgnoreCase("")) {
				if (validation.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondinvalid"),"othercost","01"));
				} else if (validation.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondless"),"othercost","01"));
				} else if (validation.percentageValid(bean.getOthercost()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.othercost.secondgreater"),"othercost","01"));
				}
			}
			if (StringUtils.isBlank(bean.getPremiumReserve())) {
				list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second"),"PremiumReserve","01"));
			} else {
				if (validation.percentageValid(bean.getPremiumReserve()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.second1"),"PremiumReserve","01"));
				} else if (validation.percentageValid(bean.getPremiumReserve()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondless"),"PremiumReserve","01"));
				} else if (validation.percentageValid(bean.getPremiumReserve()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.premium_Reserve.secondgreater"),"PremiumReserve","01"));
				}
			}
			if (StringUtils.isBlank(bean.getLossreserve())) {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second"),"Lossreserve","01"));
			} else {
				if (validation.percentageValid(bean.getLossreserve()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.second1"),"Lossreserve","01"));
				} else if (validation.percentageValid(bean.getLossreserve()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondless"),"Lossreservesecondless","01"));
				} else if (validation.percentageValid(bean.getLossreserve()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_reserve.secondgreater"),"Lossreservesecondless","01"));
				}
			}

			if (StringUtils.isBlank(bean.getInterest())) {
				list.add(new ErrorCheck(prop.getProperty("errors.interest.second"),"interest","01"));
			} else {
				if (validation.percentageValid(bean.getInterest()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.interest.second1"),"interest","01"));
				} else if (validation.percentageValid(bean.getInterest()).trim().equalsIgnoreCase("less")) {
					list.add(new ErrorCheck(prop.getProperty("errors.interest.secondless"),"interestsecondless","01"));
				} else if (validation.percentageValid(bean.getInterest()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.interest.secondgreater"),"interestsecondgreater","01"));
				}
			}
			if (StringUtils.isBlank(bean.getPortfolioinoutLoss())) {
				list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second"),"PortfolioinoutLoss","01"));
			} else {

				if (validation.percentageValid(bean.getPortfolioinoutLoss()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.second"),"PortfolioinoutLosssecond","01"));
				} else if (validation.percentageValid(bean.getPortfolioinoutLoss()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Loss.secondgreater"),"PortfolioinoutLosssecondgreater","01"));
				}
			}
			if (StringUtils.isBlank(bean.getPortfolioinoutPremium())) {
				list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.second"),"PortfolioinoutLosssecond","01"));
			} else {
				if (validation.percentageValid(bean.getPortfolioinoutPremium()).trim().equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.premiumNum"),"PortfolioinoutpremiumNum","01"));
				} else if (validation.percentageValid(bean.getPortfolioinoutPremium()).trim().equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.portfolio_inout_Premium.greater"),"Portfolioinoutpremiumgreater","01"));
				}
			}

			if ("Y".equalsIgnoreCase(bean.getCrestaStatus())) {
				if(StringUtils.isBlank(bean.getCrestaPopUp())){
					list.add(new ErrorCheck(prop.getProperty("cresta.popup.check"),"popup","01"));
				}
				else if (retroImpl.getCrestaCount(bean.getProposalNo(),bean.getAmendId(),bean.getBranchCode()) == 0) {
					list.add(new ErrorCheck(prop.getProperty("error.creasta.invalid"),"creasta","01"));
				}
			}

			if (StringUtils.isBlank(bean.getSlideScaleCommission())) {
				list.add(new ErrorCheck(prop.getProperty("error.slidescale.commission"),"slidescale","01"));
			} else if ("Y".equalsIgnoreCase(bean.getSlideScaleCommission())) {
				if(StringUtils.isBlank(bean.getSlidePopUp())){
					list.add(new ErrorCheck(prop.getProperty("error.slide.recheck"),"slide","01"));
				}else{
				int count = retroImpl.getBonusListCount(bean, "scale");
				count = 1;
				if (count <= 0) {
					list.add(new ErrorCheck(prop.getProperty("slide.error.lcb.table.empty"),"lcb","01"));
				}
				}
			}
			if (StringUtils.isBlank(bean.getLossParticipants())) {
				list.add(new ErrorCheck(prop.getProperty("error.losspart"),"losspart","01"));
			} else if ("Y".equalsIgnoreCase(bean.getLossParticipants())) {
				if(StringUtils.isBlank(bean.getLossPopUp())){
					list.add(new ErrorCheck(prop.getProperty("error.loss.recheck"),"loss","01"));
				}else{
				int count = retroImpl.getBonusListCount(bean, "lossparticipates");
				count = 1;
				if (count <= 0) {
					list.add(new ErrorCheck(prop.getProperty("losspart.error.lcb.table.empty"),"lcb","01"));
				}
				}
			}
			if (StringUtils.isBlank(bean.getShareProfitCommission())) {
				list.add(new ErrorCheck(prop.getProperty("errors.share_Profit_Commission.empty"),"ShareProfitCommission","01"));
			}

			if ("1".equalsIgnoreCase(bean.getShareProfitCommission())) {
				if (StringUtils.isBlank(bean.getManagementExpenses())) {
					list.add(new ErrorCheck(prop.getProperty("man.exp.req"),"ManagementExpenses","01"));
				}
				if (StringUtils.isBlank(bean.getCommissionType())) {
					list.add(new ErrorCheck(prop.getProperty("com.type.req"),"CommissionType","01"));
				} else if ("PC".equalsIgnoreCase(bean.getCommissionType())) {
					if (StringUtils.isBlank(bean.getProfitCommissionPer())) {
						list.add(new ErrorCheck(prop.getProperty("pro.com.per.req"),"ProfitCommissionPer","01"));
					} else if (Double.parseDouble(bean.getProfitCommissionPer()) > 100) {
						list.add(new ErrorCheck(prop.getProperty("profit.com.less.hundred"),"ProfitCommissionPer","01"));
					}
					if (StringUtils.isBlank(bean.getSuperProfitCommission())) {
						list.add(new ErrorCheck(prop.getProperty("error.super.pro.com"),"SuperProfitCommission","01"));
					} else {
						if ("Y".equalsIgnoreCase(bean.getSuperProfitCommission())) {
							if(StringUtils.isBlank(bean.getProfitPopUp())){
								list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"),"SuperProfitCommission","01"));
							}else{
							int count = retroImpl.CommissionTypeCount(bean.getProposalNo(),bean.getBranchCode(),bean.getCommissionType());
							if (count <= 0) {
								list.add(new ErrorCheck(prop.getProperty("error.commission.schedule"),"CommissionTypeCount","01"));
							}
						}
						}
					}
				} else if ("PR".equalsIgnoreCase(bean.getCommissionType()) || "LR".equalsIgnoreCase(bean.getCommissionType())) {
					if (StringUtils.isBlank(bean.getSetup())) {
						list.add(new ErrorCheck(prop.getProperty("error.setup.req"),"setup","01"));
					}
					if(StringUtils.isBlank(bean.getProfitPopUp())){
						list.add(new ErrorCheck(prop.getProperty("error.profit.recheck"),"profit","01"));
					}else{
					int count = retroImpl.CommissionTypeCount(bean.getProposalNo(),bean.getBranchCode(),bean.getCommissionType());
					if (count <= 0) {
						list.add(new ErrorCheck(prop.getProperty("error.commission.schedule"),"commission","01"));
					}
					}
				}

				if (StringUtils.isBlank(bean.getLossCarried())) {
					list.add(new ErrorCheck(prop.getProperty("loss.carried.req"),"LossCarried","01"));
				} else if (!"TE".equalsIgnoreCase(bean.getLossCarried())) {
					if (StringUtils.isBlank(bean.getLossyear())) {
						list.add(new ErrorCheck(prop.getProperty("error.loss.year"),"Lossyear","01"));
					} else if (Integer.parseInt(bean.getLossyear()) > 100) {
						list.add(new ErrorCheck(prop.getProperty("loss.carried.yeas.less.hundred"),"Lossyear","01"));
					}
				}
				if (StringUtils.isBlank(bean.getProfitCarried())) {
					list.add(new ErrorCheck(prop.getProperty("error.profit.carried"),"ProfitCarried","01"));
				} else if (!"TE".equalsIgnoreCase(bean.getProfitCarried())) {
					if (StringUtils.isBlank(bean.getProfitCarriedForYear())) {
						list.add(new ErrorCheck(prop.getProperty("profit.carried.year.req"),"ProfitCarried","01"));
					} else if (Integer.parseInt(bean.getProfitCarriedForYear()) > 100) {
						list.add(new ErrorCheck(prop.getProperty("profit.carried.yeas.less.hundred"),"ProfitCarriedForYear","01"));
					}
				}
				if (StringUtils.isBlank(bean.getFistpc())) {
					list.add(new ErrorCheck(prop.getProperty("req.first.profit.comm"),"Fistpc","01"));
				}
				if (StringUtils.isBlank(bean.getProfitMont())) {
					list.add(new ErrorCheck(prop.getProperty("error.profit.month"),"ProfitMont","01"));
				}
				if (StringUtils.isBlank(bean.getSubpc())) {
					list.add(new ErrorCheck(prop.getProperty("error.sub.profit.com"),"Subpc","01"));
				}
				if (StringUtils.isBlank(bean.getSubProfitMonth())) {
					list.add(new ErrorCheck(prop.getProperty("error.sub.profit.month"),"SubProfitMonth","01"));
				}
				if (StringUtils.isBlank(bean.getSubSeqCalculation())) {
					list.add(new ErrorCheck(prop.getProperty("error.sub.seq.cal.req"),"SubSeqCalculation","01"));
				}
				if (StringUtils.isBlank(bean.getProfitCommission())) {
					list.add(new ErrorCheck(prop.getProperty("error.profit.commission.req"),"ProfitCommission","01"));
				}
			}

			if (StringUtils.isBlank(bean.getLossAdvise())) {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second"),"LossAdvise","01"));
			} else if (validation.isValidNo(bean.getLossAdvise().trim()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Advise.second1"),"LossAdvise","01"));
			} else {
				bean.setLossAdvise((bean.getLossAdvise()).replaceAll(",", ""));
			}
			if (StringUtils.isBlank(bean.getCashLossLimit())) {

				list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.second"),"CashLossLimit","01"));
			} else {
				bean.setCashLossLimit((bean.getCashLossLimit()).replaceAll(",", ""));
				if (StringUtils.isNotBlank(bean.getLimitOrigCur()) && StringUtils.isBlank(bean.getTreatyLimitsurplusOC())) {
					if (validation.isValidNo(bean.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"TreatyLimitsurplusOC","01"));
					} else if (!validation.isValidNo(bean.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID") && (Double.parseDouble(bean.getCashLossLimit()) > Double.parseDouble(bean.getLimitOrigCur())))
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"LimitOrigCur","01"));
				} else if (StringUtils.isBlank(bean.getLimitOrigCur()) && StringUtils.isNotBlank(bean.getTreatyLimitsurplusOC())) {
					if (validation.isValidNo(bean.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"LimitOrigCur","01"));
					} else if (!validation.isValidNo(bean.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID") && (Double.parseDouble(bean.getCashLossLimit()) > Double.parseDouble(bean.getTreatyLimitsurplusOC())))
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"TreatyLimitsurplusOC","01"));
				} else if (StringUtils.isNotBlank(bean.getLimitOrigCur()) && StringUtils.isNotBlank(bean.getTreatyLimitsurplusOC())) {
					int t = Double.compare(Double.parseDouble(bean.getLimitOrigCur()), Double.parseDouble(bean.getTreatyLimitsurplusOC()));
					if (validation.isValidNo(bean.getCashLossLimit()).trim().equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"CashLossLimit","01"));
					} else if (!(validation.isValidNo(bean.getLimitOrigCur()).trim().equalsIgnoreCase("INVALID") || validation.isValidNo(bean.getTreatyLimitsurplusOC()).trim().equalsIgnoreCase("INVALID")) && (Double.parseDouble(bean.getCashLossLimit()) > (Double.parseDouble(t > 0 ? bean.getLimitOrigCur() : bean.getTreatyLimitsurplusOC()))))
						list.add(new ErrorCheck(prop.getProperty("errors.cash_Loss_Limit.invalid.retro"),"LimitOrigCur","01"));
				}
			}


			if (StringUtils.isBlank(bean.getEventlimit())) {
				list.add(new ErrorCheck(prop.getProperty("errors.event_limit.second"),"Eventlimit","01"));
			} else {
				bean.setEventlimit(bean.getEventlimit().replaceAll(",", ""));
				if (validation.isValidNo(bean.getEventlimit().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.event_limit.invalid"),"Eventlimit","01"));
				}
			}
			if (StringUtils.isBlank(bean.getAggregateLimit())) {
				list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.second"),"AggregateLimit","01"));
			} else {
				bean.setAggregateLimit(bean.getAggregateLimit().replaceAll(",", ""));
				if (validation.isValidNo(bean.getAggregateLimit().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.aggregate_Limit.invalid"),"AggregateLimit","01"));
				}
			}
			if (StringUtils.isBlank(bean.getOccurrentLimit())) {
				list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.second"),"OccurrentLimit","01"));
			} else {
				bean.setOccurrentLimit(bean.getOccurrentLimit().replaceAll(",", ""));
				if (validation.isValidNo(bean.getOccurrentLimit().trim()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.occurrent_Limit.invalid"),"OccurrentLimit","01"));
				}
			}

			int NoRetroCess = Integer.parseInt(bean.getNoRetroCess()== null ? "0" : bean.getNoRetroCess());
			for (int i = 0; i < NoRetroCess; i++) {
					NoRetroCessReq req2 = bean.getNoRetroCessReq().get(i);
					String cedComp = req2.getCedingCompany() == null ? "0" : req2.getCedingCompany();
					String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
					String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
					String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
					String comm = req2.getCommission()== null ? "" : req2.getCommission();
					String proStatus = req2.getProposalStatus() == null ? "0" : req2.getProposalStatus();
				if (StringUtils.isBlank(validation.isSelect(cedComp))) {
					list.add(new ErrorCheck(prop.getProperty("errors.reinsurersName.required.retro")+String.valueOf(i + 1),"reinsurersName","01"));
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
				if (StringUtils.isBlank(shSign) && "A".equalsIgnoreCase(proStatus)) {
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
				if(StringUtils.isNotBlank(proStatus)&&"A".equalsIgnoreCase(proStatus)){
				if (StringUtils.isBlank(comm)) {
					list.add(new ErrorCheck(prop.getProperty("errors.commPer.required")+String.valueOf(i + 1),"commPer","01"));
				} else if (validation.percentageValid(comm).equalsIgnoreCase("INVALID")	|| validation.percentageValid(comm)	.equalsIgnoreCase("less")|| validation.percentageValid(comm).equalsIgnoreCase("greater")) {
					list.add(new ErrorCheck(prop.getProperty("errors.commPer.invalid")+ String.valueOf(i + 1),"commPer","01"));
				}
				}

			}
			if (CollectionUtils.isEmpty(list)) {
				boolean shareFlag = false;
				
				for (int i = 0; i < NoRetroCess; i++) {
					NoRetroCessReq req2 = bean.getNoRetroCessReq().get(i);
					String cedComp = req2.getCedingCompany()== null ? "0" : req2.getCedingCompany();
					String broker = req2.getRetroBroker() == null ? "0" : req2.getRetroBroker();
					String shAccep = req2.getShareAccepted()== null ? "" : req2.getShareAccepted();
					String shSign = req2.getShareSigned()== null ? "" : req2.getShareSigned();
					if ("A".equalsIgnoreCase(req2.getProposalStatus())) {
						totShAcc += Double.parseDouble(shAccep);
						totShsg += Double.parseDouble(shSign);
						shareFlag =true;
					}
					
					for (int j = i + 1; j < NoRetroCess; j++) {		
						NoRetroCessReq req3 = bean.getNoRetroCessReq().get(j);
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
				
				BigDecimal bd = new BigDecimal(totShsg).setScale(2, RoundingMode.HALF_EVEN);
				totShsg = bd.doubleValue();
				if (totShsg != 100 && shareFlag)
					list.add(new ErrorCheck(prop.getProperty("errors.shSign.invalid"),"shSign","01"));
				BigDecimal bigDecimal = new BigDecimal(totShAcc).setScale(2, RoundingMode.HALF_EVEN);
				totShAcc = bigDecimal.doubleValue();
				if (totShAcc > 100)
					list.add(new ErrorCheck(prop.getProperty("errors.shAcc.invalid"),"shAcc","01"));
			}

			if(StringUtils.isNotBlank(bean.getEndorsementStatus())&& "Y".equalsIgnoreCase(bean.getEndorsementStatus()) && StringUtils.isBlank(bean.getDocStatus())) {
				list.add(new ErrorCheck(prop.getProperty("doc.status"),"EndorsementStatus","01"));
			}
			if(StringUtils.isNotBlank(bean.getEndorsementStatus())&& "Y".equalsIgnoreCase(bean.getEndorsementStatus()) ) {
				final String endorseDate=validation.checkDate(bean.getEndorsementDate());
				if (validation.isNull(bean.getEndorsementDate()).equalsIgnoreCase("")) {
				if("Endorsement".equalsIgnoreCase(bean.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("error.endoDate.required"),"endoDate","01"));
				}
				else if("Rectification".equalsIgnoreCase(bean.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("error.rectification.required"),"rectification","01"));
				}
				
				} else if (endorseDate.equalsIgnoreCase("INVALID")) {
				if("Endorsement".equalsIgnoreCase(bean.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("error.endoDate.check"),"endoDate","01"));
				}
				else if("Rectification".equalsIgnoreCase(bean.getEndorsmenttype())){
					list.add(new ErrorCheck(prop.getProperty("error.rectification.check"),"rectification","01"));
				}
				
				}
			}
		//	validationRemarks();
		} catch (Exception e) {
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
	public List<ErrorCheck> insertCrestaMaintableVali(InsertCrestaMaintableReq req) {
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
//		if (StringUtils.isBlank(req.getEndorsmentno())) {
//			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "9"));
//		}

		return list;
	}
	public List<ErrorCheck> insertProfitCommissionMainVali(InsertProfitCommissionMainReq req) {
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
//		if (StringUtils.isBlank(req.getContractNo())) {
//			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "5"));
//		}
		if (StringUtils.isBlank(req.getShareProfitCommission())) {
			list.add(new ErrorCheck("Please Enter ShareProfitCommission", "ShareProfitCommission", "6"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "7"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "8"));
		}
		if (StringUtils.isBlank(req.getEndorsmentno())) {
			list.add(new ErrorCheck("Please Enter Endorsmentno", "Endorsmentno", "9"));
		}
		if (StringUtils.isBlank(req.getCommissionType())) {
			list.add(new ErrorCheck("Please Enter CommissionType", "CommissionType", "10"));
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
		if (CollectionUtils.isEmpty(req.getInstalmentperiodReq())) {
			list.add(new ErrorCheck("Please Enter Instalmentperiod", "Instalmentperiod", "8"));
		}
		return list;
	}
	public List<ErrorCheck> updateProportionalTreatyVali(UpdateProportionalTreatyReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		FirstInsertRes1 res1 = new FirstInsertRes1();
		//ValidateNext()
		try {
			boolean flags = true;
			final Validation val = new Validation();
			final String tear_nt = val.isNull(bean.getTreatyNameType());
			final String brok = val.isSelect(bean.getBroker());
			final String incDate = val.checkDate(bean.getIncepDate());
			final String expdate = val.checkDate(bean.getExpDate());
			final String accDate = val.checkDate(bean.getAccDate());
			final String exchRate = val.isNull(bean.getExchRate());
			final String riskCover = val.isNull(bean.getRiskCovered());
			final String terrtyscope = val.isNull(bean.getTerritoryscope());
//			final String limitPercent = val.isNull(bean.getLimitOrigCur());
//			final String epiPercent = val.isNull(bean.getEpiorigCur());
//			final String Epi = val.isNull(bean.getEpi());
//			final String xlCost = val.isNull(bean.getXlCost());
			final String proStatus = val.isSelect(bean.getProStatus());
//			final String shareWrit = val.isNull(bean.getShareWritt());
//			final String shareSign = val.isNull(bean.getSharSign());
			final String orginalCurrency = val.isSelect(bean.getOrginalCurrency());
//			if(StringUtils.isNotBlank(bean.getAmendId())&& Integer.parseInt(bean.getAmendId())>0){
//				if(StringUtils.isBlank(bean.getEndorsmenttype())){
//					list.add(new ErrorCheck(prop.getProperty("end.type.error"),"Endorsmenttype","01"));
//				}
//			}
			if (StringUtils.isBlank(val.isSelect(bean.getDepartmentId()))) {
				list.add(new ErrorCheck(prop.getProperty("error.departId.required"),"DepartId","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getSubProfitCenter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"),"subProfitcenter","01"));
			}else{
				bean.setSubProfitCenter((bean.getSubProfitCenter()).replaceAll(" ", ""));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getProfitCenter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.Profit_Center.required"),"ProfitCenter","01"));
			}
//			Map<String, Object> map = null;
//			List<Map<String, Object>> list1 = retroImpl.getValidation(bean.getIncepDate(),bean.getRenewalcontractno());
//			if (list1 != null && list1.size() > 0) {
//				map = (Map<String, Object>) list1.get(0);
//			}
			
			if (StringUtils.isBlank(val.isNull(bean.getUnderwriter()))) {
				list.add(new ErrorCheck(prop.getProperty("error.underwriter.required"),"underwriter","01"));
			}
			if (StringUtils.isBlank(val.isNull(bean.getPolicyBranch()))) {
				list.add(new ErrorCheck(prop.getProperty("error.polBr.required"),"polBr","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getCedingCo()))) {
				list.add(new ErrorCheck(prop.getProperty("error.leadRetrocessionaire.required"),"leadRetrocessionaire","01"));
			}
			if (StringUtils.isBlank(brok)) {
				list.add(new ErrorCheck(prop.getProperty("error.leadRetroBroker.required"),"leadRetroBroker","01"));
			}
//			if (StringUtils.isBlank(bean.getDummyCon())) {
//				list.add(new ErrorCheck(prop.getProperty("error.dummy.required"),"dummy","01"));
//			}
			if ("4".equals(bean.getProductId()) && StringUtils.isBlank((bean.getRetroType()))) {
				list.add(new ErrorCheck(prop.getProperty("error.retroType.required.retro"),"retroType","01"));
			}
			if ("4".equals(bean.getProductId()) && "SR".equalsIgnoreCase(bean.getRetroType()) && StringUtils.isBlank(bean.getInsuredName())) {
				list.add(new ErrorCheck(prop.getProperty("errors.insuredName.required"),"insuredName","01"));
			}
			if("TR".equalsIgnoreCase(bean.getRetroType())){
				if (StringUtils.isBlank(val.isSelect(bean.getTreatyType()))) {
					list.add(new ErrorCheck(prop.getProperty("error.retroTreatyType.Reqired"),"retroTreatyType","01"));
				}
			}
			if("TR".equalsIgnoreCase(bean.getRetroType()) || "FO".equalsIgnoreCase(bean.getRetroType())){
				if (StringUtils.isBlank(tear_nt)) {
					list.add(new ErrorCheck(prop.getProperty("error.retroTreatyName.required"),"retroTreatyType","01"));
				}
			}
			if (StringUtils.isBlank(val.isNull(bean.getIncepDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.required"),"incepDate","01"));
			} else if (incDate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.incepDate.check"),"incepDate","01"));
			} 
//			else if (!"".equals(bean.getRenewalContractNo())&& !"0".equals(bean.getRenewalcontractno())&& map != null) {
//				if ("Invalid".equalsIgnoreCase(val.getDateValidate((String) map.get("EXPIRY_DATE"), bean.getIncepDate()))) {
//					list.add(new ErrorCheck(prop.getProperty("errors.InceptionDate.invalid"),"InceptionDate","01"));
//				}else {
//					res1.setRenewalFlag("NEWCONTNO");
//				}
//			}
			if (StringUtils.isBlank(val.isNull(bean.getExpDate()))) {
				list.add(new ErrorCheck(prop.getProperty("error.expDate.required"),"expDate","01"));
			} else if (expdate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.expdate.check"),"expDate","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getUwYear()))) {
				list.add(new ErrorCheck(prop.getProperty("error.uwYear.required"),"","01"));
			} 
//			else if (!"".equals(bean.getRenewalcontractno()) && !"0".equals(bean.getRenewalcontractno()) && map != null && Integer.parseInt((String) map.get("UW_YEAR")) >= Integer.parseInt(bean.getUwYear())) {
//				//list.add(new ErrorCheck(prop.getProperty("errors.year.invalid"));
//			}
//			if("D".equalsIgnoreCase(bean.getDummyCon())){
//				DuplicateCountCheckReq req = new DuplicateCountCheckReq();
//				req.setBranchCode(bean.getBranchCode());
//				req.setPid(bean.getProductId());
//				req.setProposalNo(bean.getProposalNo());
//				req.setType("D");
//				req.setUwYear(bean.getUwYear());
//				int count = dropDowmImpl.DuplicateCountCheck(req);
//				if(count>0){
//					list.add(new ErrorCheck(prop.getProperty("error.dup.uwyewar"),"DuplicateCount","01"));
//				}
//			}
			if (StringUtils.isNotBlank(bean.getProStatus()) && "A".equalsIgnoreCase(bean.getProStatus())&&StringUtils.isBlank(bean.getAccDate())) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.required"),"accDate","01"));
			}else if (StringUtils.isNotBlank(bean.getAccDate()) &&accDate.equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.accDate.checkerror"),"accDate","01"));
			}
			if (StringUtils.isBlank(orginalCurrency)) {
				list.add(new ErrorCheck(prop.getProperty("error.orginalCurrency.required"),"orginalCurrency","01"));
			}
			if (StringUtils.isBlank(bean.getExchRate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.exchange.required"),"exchange","01"));
			}
			if (StringUtils.isBlank(bean.getCessionExgRate())) {
				list.add(new ErrorCheck(prop.getProperty("errors.cession.exchange.required"),"CessionExgRate","01"));
			}
			else{
				if("F".equalsIgnoreCase(bean.getCessionExgRate()) && StringUtils.isBlank(bean.getFixedRate())){
					list.add(new ErrorCheck(prop.getProperty("errors.fixedrate.required"),"fixedrate","01"));
				}
				else{
					bean.setFixedRate(bean.getFixedRate().replace(",", ""));
				}
			}
			if (StringUtils.isBlank(val.isNull(bean.getNoRetroCess()))) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.required"),"noOfRetroCess","01"));
			} else if (val.isValidNo(bean.getNoRetroCess()).equalsIgnoreCase("INVALID")) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.invalid"),"noOfRetroCess","01"));
			} else if (Integer.parseInt(bean.getNoRetroCess()) < 1) {
				list.add(new ErrorCheck(prop.getProperty("error.noOfRetroCess.gte1"),"noOfRetroCess","01"));
			}

			if (StringUtils.isBlank(bean.getTerritoryscope())) {
				list.add(new ErrorCheck(prop.getProperty("error.terrtoryScope.required"),"terrtoryScope","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getProposalType()))) {
				list.add(new ErrorCheck(prop.getProperty("error.cleancutoff.required"),"cleancutoff","01"));
			}
		else if("R".equalsIgnoreCase(bean.getProposalType()) || "H".equalsIgnoreCase(bean.getProposalType())){
				if(StringUtils.isBlank(bean.getRunoffYear())){
					list.add(new ErrorCheck(prop.getProperty("error.runoff.required"),"runoff","01"));	
				}
			}
			if (StringUtils.isBlank(riskCover)) {
				list.add(new ErrorCheck(prop.getProperty("error.portfolio.Reqired"),"portfolio","01"));
			}
			if (StringUtils.isBlank(bean.getLOCIssued())) {
				list.add(new ErrorCheck(prop.getProperty("error.locissued.required"),"locissued","01"));
			}
			else if("Y".equalsIgnoreCase(bean.getLOCIssued())){
					if(StringUtils.isBlank(bean.getLocBankName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbank.required"),"locbank","01"));
					}
					if(StringUtils.isBlank(bean.getLocCreditPrd())){
						list.add(new ErrorCheck(prop.getProperty("error.loccrditPerd.required"),"loccrditPerd","01"));
					}
					if(StringUtils.isBlank(bean.getLocCreditAmt())){
						list.add(new ErrorCheck(prop.getProperty("error.loccreditAmt.required"),"loccreditAmt","01"));
					}
					else{
						bean.setLocCreditAmt(bean.getLocCreditAmt().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(bean.getLocBeneficerName())){
						list.add(new ErrorCheck(prop.getProperty("error.locbenifName.required"),"locbenifName","01"));
					}
				}
				
			if (bean.getPnoc().equalsIgnoreCase("-1")) {
				list.add(new ErrorCheck(prop.getProperty("error.pnoc.required"),"pnoc","01"));
			}
			if (StringUtils.isBlank(val.isSelect(bean.getAccountingPeriod())) || bean.getAccountingPeriod().equalsIgnoreCase("-1")) {
				list.add(new ErrorCheck(prop.getProperty("error.AccountionPeriod.reqired"),"AccountionPeriod","01"));
			}
			if (StringUtils.isBlank(bean.getReceiptofStatements())) {
				list.add(new ErrorCheck(prop.getProperty("Error.ResciptStatments.Required"),"ResciptStatments","01"));
			} else if (val.isValidNo(bean.getReceiptofStatements()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("Error.ReceiptofStatmenst.Error"),"ResciptStatments","01"));
			}
			if (StringUtils.isBlank(bean.getReceiptofPayment())) {
				list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.required"),"ResciptStatments","01"));
			} else if (val.isValidNo(bean.getReceiptofPayment()).equalsIgnoreCase("invalid")) {
				list.add(new ErrorCheck(prop.getProperty("error.ReceiptOfStatments.Error"),"ResciptStatments","01"));
			}

			if (StringUtils.isBlank(bean.getTerritory())) {
				list.add(new ErrorCheck(prop.getProperty("errors.territoryCode.required"),"territoryCode","01"));
			}
			if (StringUtils.isBlank(bean.getCountryIncludedList())) {
				list.add(new ErrorCheck(prop.getProperty("errors.CountryInclude.required"),"CountryInclude","01"));
			}
			boolean cedflag = true;
			double amt = 0.0;
			boolean cedCheck = true;
//			final String cenRent = val.isNull(bean.getCedReten());
//			if (val.isNull(bean.getCedRetenType()).equalsIgnoreCase("")) {
//				list.add(new ErrorCheck(prop.getProperty("error.cedRentType.required.our"),"cedRentType","01"));
//				cedflag = false;
//			} else {
//				if (cenRent.equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("error.cedRent.required.our"),"cedRent","01"));
//					cedflag = false;
//				} else {
//					bean.setCedReten((bean.getCedReten()).replaceAll(",", ""));
//					if ("A".equalsIgnoreCase(bean.getCedRetenType())) {
//						cedflag = false;
//						if (val.isValidNo(bean.getCedReten()).trim().equalsIgnoreCase("Invalid")) {
//							list.add(new ErrorCheck(prop.getProperty("error.cedRentAmt.required.our"),"cedRentAmt","01"));
//						}
//					} else if ("P".equalsIgnoreCase(bean.getCedRetenType())) {
//						if (val.percentageValid(bean.getCedReten()).trim().equalsIgnoreCase("Invalid") || val.percentageValid(bean.getCedReten().trim()).equalsIgnoreCase("less") || val.percentageValid(bean.getCedReten().trim()).equalsIgnoreCase("greater")) {
//							list.add(new ErrorCheck(prop.getProperty("error.cedRentPer.required.our"),"cedRentPer","01"));
//							cedflag = false;
//						}
//					}
//				}
//			}
			if(StringUtils.isNotBlank(bean.getRetroType()) && "TR".equalsIgnoreCase(bean.getRetroType())){
//			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("1")) {
//				if (limitPercent.equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.required.qs"),"limitOrigCurr","01"));
//					cedCheck = false;
//				} else {
//					bean.setLimitOrigCur((bean.getLimitOrigCur()).replaceAll(",", ""));
//					if (val.isValidNo(bean.getLimitOrigCur()).equalsIgnoreCase("invalid")) {
//						list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.check.qs"),"limitOrigCurr","01"));
//						cedCheck = false;
//					} else {
//						amt = Double.parseDouble(bean.getLimitOrigCur());
//					}
//				}
//			}
		
			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("2")) {
				if (StringUtils.isBlank(bean.getTreatynoofLine())) {
					list.add(new ErrorCheck(prop.getProperty("error.noonline.required"),"noonline","01"));
				}
			}
//			if (bean.getTreatyType().equalsIgnoreCase("3") || bean.getTreatyType().equalsIgnoreCase("2")) {
//				if (StringUtils.isBlank(bean.getTreatyLimitsurplusOC())) {
//					list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.required"),"TreatyLimitsurplusOC","01"));
//					cedCheck = false;
//				} else {
//					bean.setTreatyLimitsurplusOC((bean.getTreatyLimitsurplusOC()).replaceAll(",", ""));
//					if (val.isValidNo(bean.getTreatyLimitsurplusOC()).equalsIgnoreCase("invalid")) {
//						list.add(new ErrorCheck(prop.getProperty("error.TreatyLimitsurplusOC.check"),"TreatyLimitsurplusOC","01"));
//						cedCheck = false;
//					} else {
//						amt = Double.parseDouble(bean.getTreatyLimitsurplusOC());
//					}
//				}
//			}
			}
//			else{
//				if (StringUtils.isBlank(bean.getFaclimitOrigCur())) {
//					list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.required"),"limitOrigCurr","01"));
//					cedCheck = false;
//				} else {
//					bean.setFaclimitOrigCur((bean.getFaclimitOrigCur()).replaceAll(",", ""));
//					if (val.isValidNo(bean.getFaclimitOrigCur()).equalsIgnoreCase("invalid")) {
//						list.add(new ErrorCheck(prop.getProperty("error.fac.limitOrigCurr.check"),"limitOrigCurr","01"));
//						cedCheck = false;
//					} else {
//						amt = Double.parseDouble(bean.getFaclimitOrigCur());
//					}
//				}
//			}
//			if (StringUtils.isBlank(bean.getPml())) {
//				list.add(new ErrorCheck(prop.getProperty("error.pml.required"),"pml","01"));
//			} else if ("Y".equalsIgnoreCase(bean.getPml())) {
//				if (StringUtils.isBlank(bean.getPmlPercent())) {
//					list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.required"),"pmlpercentage","01"));
//				} else {
//					double pmlper = Double.parseDouble(bean.getPmlPercent());
//					if (pmlper > 100) {
//						list.add(new ErrorCheck(prop.getProperty("error.pmlpercentage.less.100.required"),"pmlpercentage","01"));
//					}
//				}
//			
//			if (StringUtils.isBlank(bean.getEpi())) {
//				list.add(new ErrorCheck(prop.getProperty("error.epiRetro.required"),"epiRetro","01"));
//			}
//
//			if ("".equalsIgnoreCase(proStatus)) {
//				list.add(new ErrorCheck(prop.getProperty("error.proStatus.required"),"proStatus","01"));
//			}
//			//validationRemarks();
//		}
			}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ErrorCheck> riskDetailsEditModeVali(RiskDetailsEditModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContNo", "ContNo", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(String.valueOf(req.getContractMode()))) {
			list.add(new ErrorCheck("Please Enter ContractMode", "ContractMode", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "2"));
		}
		return list;
	}
	public List<ErrorCheck> getEndDateVali(GetEndDateReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "1"));
		}
		return list;
	}
	public List<ErrorCheck> insertRetroDetailsVali(InsertRetroDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "1"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		return list;
	}

}
