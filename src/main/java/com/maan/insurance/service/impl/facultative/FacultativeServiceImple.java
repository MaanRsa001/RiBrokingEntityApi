package com.maan.insurance.service.impl.facultative;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.facultative.CommonResponse;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes1;
import com.maan.insurance.model.res.facultative.GetCommonValueRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes1;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes1;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes1;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes1;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes1;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes1;
import com.maan.insurance.model.res.facultative.InstalmentListRes;
import com.maan.insurance.model.res.facultative.MappingRes;
import com.maan.insurance.model.res.facultative.RetroDupListRes;
import com.maan.insurance.model.res.facultative.RetroListRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes1;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes1;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes1;
import com.maan.insurance.model.res.facultative.UwList;
import com.maan.insurance.model.res.facultative.ViewModeRes;
import com.maan.insurance.model.res.facultative.ViewModeRes1;
import com.maan.insurance.model.res.nonproportionality.BonusRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;
import com.maan.insurance.model.res.retro.GetRemarksDetailsRes1;
import com.maan.insurance.service.facultative.FacultativeService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
import com.maan.insurance.validation.facultative.Validation;

@Service
public class FacultativeServiceImple implements FacultativeService {
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;

	@Override
	public GetCommonValueRes getShortname(String branchCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String Short="";
		String query ="";
		try {
			query = "GET_SHORT_NAME";
			list= queryImpl.selectList(query,new String[] {branchCode});
			if (!CollectionUtils.isEmpty(list)) {
				Short = list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
						: list.get(0).get("COUNTRY_SHORT_NAME").toString();
			}
		response.setCommonResponse(Short);
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;	}

	@Override
	public FirstPageInsertRes firstPageInsert(FirstPageInsertReq beanObj) {
		FirstPageInsertRes response = new FirstPageInsertRes();
		FirstPageInsertRes1 res = new FirstPageInsertRes1();
		try{
			String query = "fac.sp.facultativepage1";
			String[] args=firstPageInsertAruguments(beanObj, Boolean.valueOf(beanObj.getFlag()),Boolean.valueOf(beanObj.getContract()));
			int result=queryImpl.updateQuery(query,args);
			updatependingFields(beanObj);
			if(result==1)
				res.setSaveFlag("true");
			else
				res.setSaveFlag("false");
			beanObj.setProposalNo(args[0].toString());
			if(StringUtils.isNotBlank(beanObj.getContractNo())) {
				res.setBackmode("Con");
				res.setStatus("Your Proposal is saved in Endorsement with Proposal No : "+beanObj.getProposalNo() +".");
			} else if("0".equalsIgnoreCase(beanObj.getProStatus()) ||"P".equalsIgnoreCase(beanObj.getProStatus()) || "A".equalsIgnoreCase(beanObj.getProStatus())){
				res.setBackmode("Pro");
				res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : "+beanObj.getProposalNo()+".");
			}else if("N".equalsIgnoreCase(beanObj.getProStatus())){
				res.setBackmode("NTU");
				res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+beanObj.getProposalNo()+".");
			}else if("R".equalsIgnoreCase(beanObj.getProStatus())){
				res.setBackmode("Reje");
				res.setStatus("Your Proposal is saved in Rejected Stage with Proposal No : "+beanObj.getProposalNo() +".");
			} 
//			if(saveFlag){
//				InsertRemarkDetails(beanObj);
//				UpadateUWShare(beanObj);
//				if(StringUtils.isNotBlank(beanObj.getType())&&"1".equalsIgnoreCase(beanObj.getType()) ){
//				InsertCoverDeductableDetails(beanObj);
//				}else{
//					InsertXolCoverDeductableDetails(beanObj);
//				}
//			}
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
			}
		catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public String[] firstPageInsertAruguments(FirstPageInsertReq beanObj,final boolean flag,final boolean contract) throws ParseException {
		final Validation val=new Validation();
		String[] args=new String[64];
		if(beanObj.getProposalNo().equalsIgnoreCase("")){
				args[0]= fm.getSequence("Proposal", beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),"");
		}
		else
		args[0]=beanObj.getProposalNo();	
		beanObj.setProposalNo(args[0].toString());
		args[1]="0";
		args[2]=beanObj.getContractNo()==null?"":beanObj.getContractNo();
		args[3]=StringUtils.isEmpty(beanObj.getUsCurrencyRate())? "0": beanObj.getUsCurrencyRate();
		args[4]=StringUtils.isEmpty(beanObj.getProfitCenterCode())? "0": beanObj.getProfitCenterCode();
		args[5]=StringUtils.isEmpty(beanObj.getSubProfitCenter())? "0": beanObj.getSubProfitCenter();
		args[6]=StringUtils.isEmpty(beanObj.getMonth())? "": val.GetProcedureDate(beanObj.getMonth());
		args[7]=StringUtils.isEmpty(beanObj.getUnderwriter())? "": beanObj.getUnderwriter();
		args[8]=StringUtils.isEmpty(beanObj.getFacultativeDepartment())? "0": beanObj.getFacultativeDepartment();
		args[9]=StringUtils.isEmpty(beanObj.getCedingCompany())? "0":beanObj.getCedingCompany();
		args[10]=StringUtils.isEmpty(beanObj.getBroker())? "0":beanObj.getBroker();
		args[11]=StringUtils.isEmpty(beanObj.getInceptionDate())? "": val.GetProcedureDate(beanObj.getInceptionDate());
		args[12]=StringUtils.isEmpty(beanObj.getExpiryDate())? "":val.GetProcedureDate(beanObj.getExpiryDate());
		args[13]=StringUtils.isEmpty(beanObj.getAccountDate())?"":val.GetProcedureDate(beanObj.getAccountDate());
		args[14]=StringUtils.isEmpty(beanObj.getOriginalCurrency())? "0":beanObj.getOriginalCurrency();
		args[15]=StringUtils.isEmpty(beanObj.getTerritory())? "0":beanObj.getTerritory();
		args[16]=StringUtils.isEmpty(beanObj.getInsuredName())? "":beanObj.getInsuredName();
		args[17]=StringUtils.isEmpty(beanObj.getLocation())? "":beanObj.getLocation();
		args[18]=StringUtils.isEmpty(beanObj.getCity())? "":beanObj.getCity();
		args[19]=StringUtils.isEmpty(beanObj.getCedantsRet())? "0":beanObj.getCedantsRet();
		args[20]=StringUtils.isEmpty(beanObj.getNr())? "0":beanObj.getNr();
		args[21]=StringUtils.isEmpty(beanObj.getMaxiumlimit())? "0":beanObj.getMaxiumlimit();
		args[22]=StringUtils.isEmpty(beanObj.getDeductible())? "0":beanObj.getDeductible();
		args[23]=StringUtils.isEmpty(beanObj.getInterest())? "":beanObj.getInterest();
		args[24]=StringUtils.isEmpty(beanObj.getSpRetro())? "0":beanObj.getSpRetro();
		args[25]=StringUtils.isEmpty(beanObj.getPml())? "0":beanObj.getPml();
		args[26]=StringUtils.isEmpty(beanObj.getSipml())? "0":beanObj.getSipml();
		args[27]=StringUtils.isEmpty(beanObj.getSumInsured())? "0":beanObj.getSumInsured();
		args[28]=StringUtils.isEmpty(beanObj.getGwpi())? "0": beanObj.getGwpi();
		args[29]=StringUtils.isEmpty(beanObj.getPmll())? "0":beanObj.getPmll();
		args[30]=StringUtils.isEmpty(beanObj.getTpl())? "0":beanObj.getTpl();  
		args[31]=StringUtils.isEmpty(beanObj.getShWt())? "0": beanObj.getShWt();
		if(!beanObj.getProStatus().equalsIgnoreCase("A")){
			args[32]="0";
		}
		else if(beanObj.getProStatus().equalsIgnoreCase("A")){
			args[32]=StringUtils.isEmpty(beanObj.getShSd())? "0": beanObj.getShSd();
		}
		args[33]=StringUtils.isEmpty(beanObj.getProductId()) ? "0":   beanObj.getProductId();
		args[34]=StringUtils.isEmpty(beanObj.getYear())? "0":   beanObj.getYear();
		if(flag){
			args[35]="P";
			args[36]="P";
		}
		else {
			if(beanObj.getProStatus().equalsIgnoreCase("0") ||  beanObj.getProStatus().equalsIgnoreCase("P") ){
				args[35]="P";
				args[36]="P";
			}else{
				args[35]=beanObj.getProStatus();
				args[36]=beanObj.getProStatus();
			}	
		}
		args[37]=StringUtils.isNotBlank(beanObj.getBaseLoginID())?beanObj.getBaseLoginID():beanObj.getLoginid();
		args[38]=StringUtils.isEmpty(beanObj.getNoInsurer())?"0":beanObj.getNoInsurer();
		args[39]=StringUtils.isEmpty(beanObj.getRenewalContractno())?"":beanObj.getRenewalContractno();
		args[40]=beanObj.getRenewalStatus();
		args[41]=StringUtils.isEmpty(beanObj.getPremiumrate())?"0":beanObj.getPremiumrate();
		args[42]=beanObj.getBranchCode();
		args[43]=beanObj.getPolicyBranch();
		args[44]=StringUtils.isEmpty(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		args[45]=StringUtils.isEmpty(beanObj.getSumInsuredOurShare())?"0": beanObj.getSumInsuredOurShare();
		args[46]=StringUtils.isEmpty(beanObj.getGwpiOurShare())? "0": beanObj.getGwpiOurShare();
		args[47]=StringUtils.isEmpty(beanObj.getPmlOurShare())? "0": beanObj.getPmlOurShare();
		args[48]=StringUtils.isEmpty(beanObj.getTplOurShare())? "0": beanObj.getTplOurShare();
		args[49]=beanObj.getLoginid();
		args[50]=StringUtils.isEmpty(beanObj.getDeductibleFacXol())?"0": beanObj.getDeductibleFacXol();
		args[51]=StringUtils.isEmpty(beanObj.getXolOC())?"0": beanObj.getXolOC();
		args[52]=StringUtils.isEmpty(beanObj.getXolOSOC())?"0": beanObj.getXolOSOC();
		args[53]=StringUtils.isEmpty(beanObj.getNoOfInst())?"0": beanObj.getNoOfInst();
		args[54]=StringUtils.isEmpty(beanObj.getModeOfTransport())?"0": beanObj.getModeOfTransport();
		args[55]=StringUtils.isEmpty(beanObj.getVesselName())?"": beanObj.getVesselName();
		args[56]=StringUtils.isEmpty(beanObj.getVesselAge())?"0": beanObj.getVesselAge();
		args[57]=StringUtils.isEmpty(beanObj.getLimitPerVesselOC())?"0": beanObj.getLimitPerVesselOC();
		args[58]=StringUtils.isEmpty(beanObj.getLimitPerLocationOC())?"0": beanObj.getLimitPerLocationOC();
		args[59]=StringUtils.isEmpty(beanObj.getType())?"": beanObj.getType();
		args[60]=StringUtils.isEmpty(beanObj.getCountryIncludedList())?"": beanObj.getCountryIncludedList();
		args[61]=StringUtils.isEmpty(beanObj.getCountryExcludedList())?"": beanObj.getCountryExcludedList();
		args[62]=StringUtils.isEmpty(beanObj.getContractListVal())?"": beanObj.getContractListVal();
		args[63]=StringUtils.isEmpty(beanObj.getXollayerNo())?"": beanObj.getXollayerNo();
		return args;
	}
	private void updatependingFields(FirstPageInsertReq beanObj) {
		String query="";
		String args[]=null;
		try{
			 query= "UPDATE_RISK_DETAILS";
			 args=new String[13];
			 args[0] = StringUtils.isEmpty(beanObj.getInwardType())? "" :beanObj.getInwardType();
			 args[1] = StringUtils.isEmpty(beanObj.getReceiptofPayment())? "" :beanObj.getReceiptofPayment();
			 args[2] = StringUtils.isEmpty(beanObj.getLocIssued())? "" :beanObj.getLocIssued();
			 args[3] = StringUtils.isEmpty(beanObj.getLatitude())? "" :beanObj.getLatitude();
			 args[4] = StringUtils.isEmpty(beanObj.getLongitude())? "" :beanObj.getLongitude();
			 args[5] = StringUtils.isEmpty(beanObj.getVessaletonnage())? "" :beanObj.getVessaletonnage();
			 args[6] = StringUtils.isEmpty(beanObj.getEndorsmenttype())? "" :beanObj.getEndorsmenttype();
			 args[7] = StringUtils.isEmpty(beanObj.getLocBankName())? "" :beanObj.getLocBankName();
			 args[8] = StringUtils.isEmpty(beanObj.getLocCreditPrd())? "" :beanObj.getLocCreditPrd();
			 args[9] = StringUtils.isEmpty(beanObj.getLocCreditAmt())? "" :beanObj.getLocCreditAmt().replaceAll(",", "");
			 args[10] = StringUtils.isEmpty(beanObj.getLocBeneficerName())? "" :beanObj.getLocBeneficerName();
			 args[11] = beanObj.getProposalNo();
			 args[12] = StringUtils.isEmpty(beanObj.getEndorsmentno())?"0":beanObj.getEndorsmentno();
			 queryImpl.updateQuery(query,args);
			 query = "UPDATE_FAC_RISK_PROPOSAL";
			 args = new String[30];
			 args[0] =StringUtils.isEmpty(beanObj.getDeductibleFacXolPml())? "" :beanObj.getDeductibleFacXolPml();
			 args[1] = StringUtils.isEmpty(beanObj.getDeductibleFacXolPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getDeductibleFacXolPml(), beanObj.getUsCurrencyRate());
			 args[2] =StringUtils.isEmpty(beanObj.getDeductibleFacXolPmlOurShare())? "" :beanObj.getDeductibleFacXolPmlOurShare();
			 args[3] = StringUtils.isEmpty(beanObj.getDeductibleFacXolPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getDeductibleFacXolPmlOurShare(), beanObj.getUsCurrencyRate());
			 args[4] =StringUtils.isEmpty(beanObj.getGwpiPml())? "" :beanObj.getGwpiPml();
			 args[5] = StringUtils.isEmpty(beanObj.getGwpiPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getGwpiPml(), beanObj.getUsCurrencyRate());
			 args[6] =StringUtils.isEmpty(beanObj.getGwpiPmlOurShare())? "" :beanObj.getGwpiPmlOurShare();
			 args[7] = StringUtils.isEmpty(beanObj.getGwpiPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getGwpiPmlOurShare(), beanObj.getUsCurrencyRate());
			 args[8] =StringUtils.isEmpty(beanObj.getPslOC())? "0" :beanObj.getPslOC();
			 args[9] = StringUtils.isEmpty(beanObj.getPslOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPslOC(), beanObj.getUsCurrencyRate());
			 args[10] =StringUtils.isEmpty(beanObj.getPslOurShare())? "0" :beanObj.getPslOurShare();
			 args[11] = StringUtils.isEmpty(beanObj.getPslOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPslOurShare(), beanObj.getUsCurrencyRate());
			 args[12] =StringUtils.isEmpty(beanObj.getPllOC())? "0" :beanObj.getPllOC();
			 args[13] = StringUtils.isEmpty(beanObj.getPllOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPllOC(), beanObj.getUsCurrencyRate());
			 args[14] =StringUtils.isEmpty(beanObj.getPllOurShare())? "0" :beanObj.getPllOurShare();
			 args[15] = StringUtils.isEmpty(beanObj.getPllOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPllOurShare(), beanObj.getUsCurrencyRate());
			 args[16] =StringUtils.isEmpty(beanObj.getPblOC())? "0" :beanObj.getPblOC();
			 args[17] = StringUtils.isEmpty(beanObj.getPblOC())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPblOC(), beanObj.getUsCurrencyRate());
			 args[18] =StringUtils.isEmpty(beanObj.getPblOurShare())? "0" :beanObj.getPblOurShare();
			 args[19] = StringUtils.isEmpty(beanObj.getPblOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPblOurShare(), beanObj.getUsCurrencyRate());
			 args[20] =StringUtils.isEmpty(beanObj.getPmll())? "" :beanObj.getPmll();
			 args[21] = StringUtils.isEmpty(beanObj.getPmll())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPmll(), beanObj.getUsCurrencyRate());
			 args[22] =StringUtils.isEmpty(beanObj.getPmlOCOurShare())? "" :beanObj.getPmlOCOurShare();
			 args[23] = StringUtils.isEmpty(beanObj.getPmlOCOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getPmlOCOurShare(), beanObj.getUsCurrencyRate());
			 args[24] =StringUtils.isEmpty(beanObj.getSumInsuredPml())? "" :beanObj.getSumInsuredPml();
			 args[25] = StringUtils.isEmpty(beanObj.getSumInsuredPml())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getSumInsuredPml(), beanObj.getUsCurrencyRate());
			 args[26] =StringUtils.isEmpty(beanObj.getSumInsuredPmlOurShare())? "" :beanObj.getSumInsuredPmlOurShare();
			 args[27] = StringUtils.isEmpty(beanObj.getSumInsuredPmlOurShare())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": getosvalue(beanObj.getSumInsuredPmlOurShare(), beanObj.getUsCurrencyRate());
			 args[28] = beanObj.getProposalNo();
			 args[29] = StringUtils.isEmpty(beanObj.getEndorsmentno())?"0":beanObj.getEndorsmentno();
			 queryImpl.updateQuery(query,args);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public String getosvalue(final String limitOrigCur,final String ExchangeRate) {
		double output=0.0;
		String result = "";
		try{
			double origCountryVal=0.0;
			if(limitOrigCur!=null){
				String val = limitOrigCur.replaceAll(",", "");
				if (!("".equalsIgnoreCase(val))&& Double.parseDouble(val) != 0) {
					origCountryVal = Double.parseDouble(val) / Double.parseDouble(ExchangeRate);
					final DecimalFormat myFormatter = new DecimalFormat("###.##");
					output = Double.parseDouble(myFormatter.format(origCountryVal));
					result = String.valueOf(output);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public List<Map<String, Object>> getValidation(FirstPageInsertReq formObj,int mode)  {
		String query="";
		List<Map<String,Object>> list=null;
		try{
			if(mode==1){
				query = "fac.select.getRenewalValidation";
				list = queryImpl.selectList(query, new String[] {formObj.getInceptionDate(),formObj.getRenewalContractno()});
			}else{
				query = "fac.select.getSPRetroValidation";
				list = queryImpl.selectList(query, new String[] {formObj.getContractNo()});
			}
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public CommonResponse insertRemarkDetails(RemarksSaveReq beanObj) {
		CommonResponse resp=new CommonResponse();
		String amendId="";
		try {
			String deleteQuery = "DELETE_REMARKS_DETAILS";
			String[] dobj= new String[2];
			dobj[0]=beanObj.getProposalNo();
			dobj[1]="0";
			queryImpl.updateQuery(deleteQuery, dobj);
			List<Map<String, Object>> list = queryImpl.selectList("TTRN_RISK_REMARKS_AMEND_ID", new String[]{beanObj.getProposalNo()});
			if(!CollectionUtils.isEmpty(list)) {
				amendId=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			if(!CollectionUtils.isEmpty(beanObj.getRemarksReq())) {
				String query="INSERT_REMARKS_DETAILS";
			for(int i=0;i<beanObj.getRemarksReq().size();i++){
				RemarksReq req=beanObj.getRemarksReq().get(i);
				String[] obj= new String[12];
				obj[0]=beanObj.getProposalNo();
				obj[1]=beanObj.getContractNo();
				obj[2]=beanObj.getLayerNo();
				obj[3]=beanObj.getDepartmentId();
				obj[4]=beanObj.getProductid();
				obj[5]=amendId;
				obj[6]=String.valueOf(i+1);
				obj[7]=req.getDescription();
				obj[8]=req.getRemark1();
				obj[9]=req.getRemark2();
				obj[10]=beanObj.getLoginId();
				obj[11]=beanObj.getBranchCode();
				queryImpl.updateQuery(query, obj);
				resp.setMessage("Success");
				resp.setErroCode(0);
				resp.setIsError(false);
			}
		} } catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}

	@Override
	public CommonResponse UpadateUWShare(String shSd, String proposalNo) {
		CommonResponse response = new CommonResponse();
		String query="";
		try {
			query= "UPDATE_UW_SHARE";
			queryImpl.updateQuery(query,new String[]{shSd,proposalNo, proposalNo});
			response.setMessage("Success");
			response.setIsError(false);
			}
		catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
		}

	@Override
	public CommonResponse insertCoverDeductableDetails(InsertCoverDeductableDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		String amendId= "";
		try {
			String  deleteQuery = "DELETE_COVERDEDUCTABLE_DETAILS";
			String[] dobj= new String[2];
			dobj[0]=beanObj.getProposalNo();
			dobj[1]="0";
			queryImpl.updateQuery(deleteQuery, dobj);
			String query= "INSERT_COVERDEDUCTABLE_DETAILS";
			List<Map<String, Object>> list = queryImpl.selectList("TTRN_FAC_SI_AMEND_ID", new String[]{beanObj.getProposalNo()});
			if(!CollectionUtils.isEmpty(list)) {
				amendId=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			if(StringUtils.isNotBlank(beanObj.getType()) && "1".equalsIgnoreCase(beanObj.getType())){
			for(int i=0;i<beanObj.getCoverSNoReq().size();i++){
			CoverSNoReq req = beanObj.getCoverSNoReq().get(i);
				String[] obj= new String[22];
				obj[0]=beanObj.getProposalNo();
				obj[1]=beanObj.getContractNo();
				obj[2]="0";
				obj[3]=beanObj.getDepartmentId();
				obj[4]=beanObj.getProductId();
				obj[5]=amendId;
				obj[6]=String.valueOf(i+1);
				obj[7]=req.getCoverdepartId();
				obj[8]=req.getCoversubdepartId();
				obj[9]=req.getCoverTypeId();
				obj[10]=req.getCoverLimitOC().replace(",", "");
				obj[11]=req.getDeductableLimitOC().replace(",", "");
				obj[12]=req.getCoverageDays().replace(",", "");
				obj[13]=req.getDeductableDays();
				obj[14]=req.getPremiumRateList();
				obj[15]=req.getEgnpiAsPerOff().replace(",", "");
				obj[16]=req.getCoverRemark();
				obj[17]=beanObj.getLoginid();
				obj[18]=beanObj.getBranchCode();
				obj[19]=StringUtils.isEmpty(req.getPmlPerList())? "" :req.getPmlPerList().replace(",", "");
				obj[20]=StringUtils.isEmpty(req.getPmlHundredPer())? "" :req.getPmlHundredPer().replace(",", "");
				obj[21]=beanObj.getType();
				queryImpl.updateQuery(query, obj);
			}
			}
			response.setMessage("Success");
			response.setIsError(false);
			}
		catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse insertXolCoverDeductableDetails(InsertXolCoverDeductableDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		String amendId="";
		try {
			String  deleteQuery = "DELETE_COVERDEDUCTABLE_DETAILS";
			String[] dobj= new String[2];
			dobj[0]=beanObj.getProposalNo();
			dobj[1]="0";
			queryImpl.updateQuery(deleteQuery, dobj);
			String query= "INSERT_XOLCOVERDEDUCTABLE_DETAILS";
			List<Map<String, Object>> list = queryImpl.selectList("TTRN_FAC_SI_AMEND_ID", new String[]{beanObj.getProposalNo()});
			if(!CollectionUtils.isEmpty(list)) {
				amendId=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			if(StringUtils.isNotBlank(beanObj.getType()) ){
			for(int i=0;i<beanObj.getXolcoverSNoReq().size();i++){
				XolcoverSNoReq req = beanObj.getXolcoverSNoReq().get(i);
				String[] obj= new String[16];
				obj[0]=beanObj.getProposalNo();
				obj[1]=beanObj.getContractNo();
				obj[2]="0";
				obj[3]=beanObj.getDepartmentId();
				obj[4]=beanObj.getProductId();
				obj[5]=amendId;
				obj[6]=String.valueOf(i+1);
				obj[7]=req.getXolcoverdepartId();
				obj[8]=req.getXolcoversubdepartId();
				obj[9]=req.getXolcoverLimitOC().replace(",", "");
				obj[10]=req.getXoldeductableLimitOC().replace(",", "");
				obj[11]=req.getXolpremiumRateList();
				obj[12]=req.getXolgwpiOC().replace(",", "");
				obj[13]=beanObj.getLoginid();
				obj[14]=beanObj.getBranchCode();
				obj[15]=beanObj.getType();
				queryImpl.updateQuery(query, obj);
			}
			}
			response.setMessage("Success");
			response.setIsError(false);
			}
		catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Override
	public GetLowClaimBonusListRes getLowClaimBonusList(String proposalNo, String branchCode, String acqBonus) {
		GetLowClaimBonusListRes response = new GetLowClaimBonusListRes();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		String query="";
         String args[]=null;
         GetLowClaimBonusListRes1 res = new GetLowClaimBonusListRes1();
         List<GetLowClaimBonusListRes1> resList= new ArrayList<GetLowClaimBonusListRes1>();
		try{
				args = new String[3];
				args[0] = proposalNo;
				args[1] = branchCode;
				args[2] = acqBonus;
					query = "BONUS_MAIN_SELECT";
					result = queryImpl.selectList(query,args);
					List<BonusRes> bonusResList = new ArrayList<BonusRes>();
					for(int i=0;i<result.size();i++){
					BonusRes bonusRes = new BonusRes();
		               Map<String,Object> tempMap = result.get(i);
		               res.setBonusTypeId(tempMap.get("LCB_TYPE")==null?"":tempMap.get("LCB_TYPE").toString());
		               bonusRes.setBonusSNo(tempMap.get("LCB_ID")==null?"":tempMap.get("LCB_ID").toString());
		               bonusRes.setBonusFrom(tempMap.get("LCB_FROM")==null?"":fm.formatter(tempMap.get("LCB_FROM").toString()));	  
		               bonusRes.setBonusTo(tempMap.get("LCB_TO")==null?"":fm.formatter(tempMap.get("LCB_TO").toString()));
		               bonusRes.setBonusLowClaim(tempMap.get("LCB_PERCENTAGE")==null?"":fm.formatter(tempMap.get("LCB_PERCENTAGE").toString()));		  
		               bonusResList.add(bonusRes);
		               res.setBonusRes(bonusResList);
		               resList.add(res);
		           }
//	               if("RD".equalsIgnoreCase(bean.getFlag()) && count<=0){
//	               LowClaimBonusInser(bean);
//	               }
					response.setCommonResponse(resList);
					response.setMessage("Success");
					response.setIsError(false);
					}catch(Exception e){
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
				return response;
	}

	@Override
	public ShowSecondPagedataRes showSecondPagedata(ShowSecondPagedataReq beanObj) {
		ShowSecondPagedataRes response = new ShowSecondPagedataRes();
		ShowSecondPagedataRes1 formObj = new ShowSecondPagedataRes1();
		try{
			List<String> days=new ArrayList<String>();
			String[] args=new String[4];
			args[0]=beanObj.getProposalNo();
			args[1]=beanObj.getProductId();
			args[2]=beanObj.getBranchCode();
			args[3]=beanObj.getProposalNo();
			String query= "fac.select.showSecondData";
			List<Map<String,Object>> list =  queryImpl.selectList(query,args);
			if(list!=null && list.size()>0) {
				Map<String,Object> tempMap = list.get(0);
				formObj.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
				formObj.setSubProfitCenter(tempMap.get("TMAS_SPFC_NAME")==null?"":tempMap.get("TMAS_SPFC_NAME").toString()); 
				formObj.setCedingCompany(tempMap.get("COMPANY")==null?"":tempMap.get("COMPANY").toString());
				formObj.setBroker(tempMap.get("BROKER")==null?"":tempMap.get("BROKER").toString());
				formObj.setUnderwriter(tempMap.get("RSK_UWYEAR")==null?"":tempMap.get("RSK_UWYEAR").toString());
				formObj.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
				formObj.setDepartClass(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
				formObj.setEndttypename(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
			}
			if(StringUtils.isNotBlank(beanObj.getNoOfInst()) && Integer.parseInt(beanObj.getNoOfInst())>0){
				List<String> instalList=new ArrayList<String>();
				for(int i=0;i<Integer.parseInt(beanObj.getNoOfInst());i++){
					instalList.add(String.valueOf(i));
				}
				formObj.setInstalList(instalList);
			}
			if(formObj.getInstalList().size()>0){
				for(int i=0;i<formObj.getInstalList().size();i++){
				if(null == beanObj.getPaymentDueDays() || beanObj.getPaymentDueDays().size()<=i){
					days.add(beanObj.getReceiptofPayment().get(i));
				}
				else{
					days.add(beanObj.getPaymentDueDays().get(i));
				}
				}
				formObj.setPaymentDueDays(days);
			}else{
				List<String> paymentdays = new ArrayList<String>();
				for (int k = 0; k < Integer.parseInt(beanObj.getNoOfInst()); k++) {
						paymentdays.add(beanObj.getReceiptofPayment().get(k));
				}
				formObj.setPaymentDueDays(paymentdays);
			}
			if(StringUtils.isNotBlank(beanObj.getNoInsurer()) && Integer.parseInt(beanObj.getNoInsurer())>0 && (beanObj.getRetrolList()==null || beanObj.getRetrolList().size()==0)){
				GetRetroContractDetailsListReq req = new GetRetroContractDetailsListReq();
				req.setBranchCode(beanObj.getBranchCode());
				req.setIncepDate(beanObj.getIncepDate());
				req.setRetroType(beanObj.getRetroType());
				req.setYear(beanObj.getYear());
				req.setProductId(beanObj.getProductId());
				
				
				List<RetroListRes> retroList1 = new ArrayList<RetroListRes>();
				List<RetroDupListRes> retroDupList1 = new ArrayList<RetroDupListRes>();
				for(int i=1;i<Integer.parseInt(beanObj.getNoInsurer());i++){
					RetroListRes retroListRes = new RetroListRes();
					GetRetroContractDetailsListRes retroList = getRetroContractDetailsList(req,"2");
					if(!(retroList.getCommonResponse()==null)) {
					retroListRes.setCONTDET1(retroList.getCommonResponse().get(i).getCONTDET1());
					retroListRes.setCONTDET2(retroList.getCommonResponse().get(i).getCONTDET2());
					retroList1.add(retroListRes);
					}
					
				}
				formObj.setRetroListRes(retroList1);
				GetRetroContractDetailsListRes retroDupList = getRetroContractDetailsList(req,"3");
				RetroDupListRes  retroDup = new RetroDupListRes();
				if(!(retroDupList.getCommonResponse()==null)) {
				retroDup.setCONTDET1(retroDupList.getCommonResponse().get(0).getCONTDET1());
				retroDup.setCONTDET2(retroDupList.getCommonResponse().get(0).getCONTDET2());
				retroDupList1.add(retroDup);
				}
				formObj.setRetroDupList(retroDupList1);
				}
			
			
			response.setCommonResponse(formObj);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
		}
	@Override
	public GetRetroContractDetailsListRes getRetroContractDetailsList(GetRetroContractDetailsListReq beanObj, String flag) {
		GetRetroContractDetailsListRes response = new GetRetroContractDetailsListRes();
		List<GetRetroContractDetailsListRes1> resList = new ArrayList<GetRetroContractDetailsListRes1>();
		String query="";
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		int flag1 = Integer.valueOf(flag);
		try{
			if(StringUtils.isEmpty(beanObj.getYear())&& flag1==1){
				query = "fac.select.uwYear";
				list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),beanObj.getIncepDate(),beanObj.getBranchCode(),beanObj.getIncepDate()});	
			}else if(StringUtils.isNotEmpty(beanObj.getYear())&&flag1==2){
				if(StringUtils.isNotBlank(beanObj.getRetroType()) && "TR".equals(beanObj.getRetroType()) && "4".equals(beanObj.getProductId())){
					query = "fac.select.retroContDetTR";
					list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),beanObj.getYear(),beanObj.getIncepDate(),beanObj.getBranchCode(),beanObj.getYear(),beanObj.getIncepDate()});
				//	beanObj.setCedingCompanyList(list);	
				}else{
				query = "fac.select.retroContDet";
				list =  queryImpl.selectList(query, new String[] {beanObj.getProductId(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getIncepDate(),beanObj.getBranchCode(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getIncepDate()});
				}
			}
			else if(StringUtils.isNotEmpty(beanObj.getYear())&&flag1==3){
				query = "FAC_SELECT_RETRO_DUP_CONTRACT";
				list =  queryImpl.selectList(query, new String[] {beanObj.getProductId(),"TR",beanObj.getYear(),beanObj.getIncepDate(),beanObj.getBranchCode(),"TR",beanObj.getYear(),beanObj.getIncepDate()});
			}
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					GetRetroContractDetailsListRes1 res = new GetRetroContractDetailsListRes1();
					Map<String, Object> insMap = (Map<String, Object>)list.get(i);
					res.setCONTDET1(insMap.get("CONTDET1")==null?"":insMap.get("CONTDET1").toString());
					res.setCONTDET2(insMap.get("CONTDET2")==null?"":insMap.get("CONTDET2").toString());
					resList.add(res);					
					}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}	}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public ShowSecondpageEditItemsRes ShowSecondPageEditItems(ShowSecondpageEditItemsReq req) {
		ShowSecondpageEditItemsRes response = new ShowSecondpageEditItemsRes();
		ShowSecondpageEditItemsRes1 formObj = new ShowSecondpageEditItemsRes1();
		try{
			String[] args=new String[4];
			args[1]=req.getProposalNo();
			args[2]=req.getProposalNo();
			args[3]=req.getProposalNo();
			args[0]=req.getBranchCode();
			String query = "fac.select.secondPageDet";
			List<Map<String,Object>> list = queryImpl.selectList(query,args);
			if(list!=null && list.size()>0){
				Map<String,Object> tempMap = list.get(0);
				formObj.setRiskGrade(tempMap.get("RISK_GRADE")==null?"":tempMap.get("RISK_GRADE").toString());
				formObj.setOccCode(tempMap.get("OCCUPATION_CODE")==null?"":tempMap.get("OCCUPATION_CODE").toString());
				formObj.setRiskDetail(tempMap.get("RISK_DETAILS")==null?"":tempMap.get("RISK_DETAILS").toString());
				formObj.setFireProt(StringUtils.isBlank(tempMap.get("FIRE_PORT")==null?"":tempMap.get("FIRE_PORT").toString())?"N":tempMap.get("FIRE_PORT").toString());
				formObj.setScope(tempMap.get("SCOPE")==null?"":tempMap.get("SCOPE").toString());
				formObj.setMbind(StringUtils.isBlank(tempMap.get("MB_IND")==null?"":tempMap.get("MB_IND").toString())?"N":tempMap.get("MB_IND").toString());
				formObj.setCategoryZone(tempMap.get("CATEGORY_ZONE")==null?"":tempMap.get("CATEGORY_ZONE").toString());
				formObj.setEqwsInd(tempMap.get("EARTHQUAKE_WS_IND")==null?"":tempMap.get("EARTHQUAKE_WS_IND").toString());
				formObj.setWsThreat(tempMap.get("WS_THREAT_IND")==null?"":tempMap.get("WS_THREAT_IND").toString());
				formObj.setEqThreat(tempMap.get("EQ_THREAT")==null?"":tempMap.get("EQ_THREAT").toString());
				formObj.setCommn(StringUtils.isBlank(req.getContractNo()==null?"":req.getContractNo().toString())&&(StringUtils.isBlank(tempMap.get("RSK_COMM")==null?"":tempMap.get("RSK_COMM").toString()))?"":tempMap.get("RSK_COMM").toString());
				formObj.setBrokerage(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_BROKERAGE")==null?"":tempMap.get("RSK_BROKERAGE").toString()))?"":tempMap.get("RSK_BROKERAGE").toString());
				formObj.setAcqBonus(tempMap.get("RSK_BONUS_ID")==null?"":tempMap.get("RSK_BONUS_ID").toString());
				formObj.setAcqBonusPercentage(dropDowmImpl.exchRateFormat(tempMap.get("RSK_NOCLAIMBONUS_PRCENT")==null?"":tempMap.get("RSK_NOCLAIMBONUS_PRCENT").toString()));
				formObj.setTax(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_TAX")==null?"":tempMap.get("RSK_TAX").toString()))?"":tempMap.get("RSK_TAX").toString());
				formObj.setLossRecord(StringUtils.isBlank(tempMap.get("RSK_LOSS_RECORD")==null?"":tempMap.get("RSK_LOSS_RECORD").toString())?"N":tempMap.get("RSK_LOSS_RECORD").toString());
				formObj.setDgmsApproval(tempMap.get("RSK_DGM_APPROVAL")==null?"":tempMap.get("RSK_DGM_APPROVAL").toString());
				formObj.setUnderwriterCode(tempMap.get("RSK_UNDERWRITTER_CODE")==null?"":tempMap.get("RSK_UNDERWRITTER_CODE").toString());
				formObj.setUwRecommendation(tempMap.get("RSK_UW_RECOMMENDATION")==null?"":tempMap.get("RSK_UW_RECOMMENDATION").toString());
				formObj.setRemarks(tempMap.get("RSK_REMARKS")==null?"":tempMap.get("RSK_REMARKS").toString());
				formObj.setOthAccep(tempMap.get("RSK_OTH_ACCEP")==null?"":tempMap.get("RSK_OTH_ACCEP").toString());
				formObj.setReftoHO(StringUtils.isBlank(tempMap.get("RSK_REF_TO_HO")==null?"":tempMap.get("RSK_REF_TO_HO").toString())?"N":tempMap.get("RSK_REF_TO_HO").toString());
				formObj.setAcqCost(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_ACQUISTION_COST_OC")==null?"":tempMap.get("RSK_ACQUISTION_COST_OC").toString()))?"":tempMap.get("RSK_ACQUISTION_COST_OC").toString());
				formObj.setAccusd(tempMap.get("RSK_ACQUISTION_COST_DC")==null?"":tempMap.get("RSK_ACQUISTION_COST_DC").toString());
				formObj.setCuRsn(tempMap.get("CU_RSN")==null?"":tempMap.get("CU_RSN").toString());
				formObj.setOthercost(tempMap.get("RSK_OTHER_COST")==null?"":tempMap.get("RSK_OTHER_COST").toString());
				formObj.setAcqCostPer(dropDowmImpl.GetACC(Double.parseDouble(StringUtils.isBlank(formObj.getCommn())?"0":formObj.getCommn())+Double.parseDouble(StringUtils.isBlank(formObj.getBrokerage())?"0":formObj.getBrokerage())+Double.parseDouble(StringUtils.isBlank(formObj.getTax())?"0":formObj.getTax())+Double.parseDouble(StringUtils.isBlank(formObj.getOthercost())?"0":formObj.getOthercost()))+"");
				String mLop = tempMap.get("M_LOP")==null?"":tempMap.get("M_LOP").toString();
				formObj.setMlopYN(StringUtils.isBlank(mLop)?"N":mLop);
				String aLop = tempMap.get("A_LOP")==null?"":tempMap.get("A_LOP").toString();
				formObj.setMlopYN(StringUtils.isBlank(aLop)?"N":aLop);
				formObj.setAlopYN(tempMap.get("A_LOP")==null?"N":tempMap.get("A_LOP").toString());
				formObj.setLeaderUnderwritercountry(tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
				formObj.setLeaderUnderwriter(tempMap.get("RSK_LEAD_UW")==null ? "0" : tempMap.get("RSK_LEAD_UW").toString());
				formObj.setLeaderUnderwritershare(tempMap.get("RSK_LEAD_UW_SHARE")==null ? "0" : tempMap.get("RSK_LEAD_UW_SHARE").toString());
				formObj.setExclusion(tempMap.get("RSK_EXCLUSION")==null?"":tempMap.get("RSK_EXCLUSION").toString());
				formObj.setCrestaStatus(tempMap.get("RSK_CREASTA_STATUS")==null?"":tempMap.get("RSK_CREASTA_STATUS").toString());
				list = queryImpl.selectList("premium.select.CEASE_STATUS", new String[]{req.getProposalNo()});
				if(!CollectionUtils.isEmpty(list)) {
					formObj.setCeaseStatus(list.get(0).get("CEASE_STATUS")==null?"":list.get(0).get("CEASE_STATUS").toString());
				}
			}
			args = new String[4];
			args[0] = req.getProposalNo();
			args[1] = "0";
			args[2] = req.getProposalNo();
			args[3] = "0";
			query = "risk.select.getInstalmentData";
			List<Map<String,Object>> instalmentList = queryImpl.selectList(query,args);
			
			List<InstalmentListRes> instalResList = new ArrayList<InstalmentListRes>();
			if (instalmentList != null) {
				
				for (int k = 0; k < instalmentList.size(); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					Map<String,Object> insMap = (Map<String,Object>)instalmentList.get(k);
					instalRes.setInstalmentDateList(insMap.get("INSTALLMENT_DATE")==null?"":insMap.get("INSTALLMENT_DATE").toString());
					instalRes.setPaymentDueDays(insMap.get("PAYEMENT_DUE_DAY")==null?"":insMap.get("PAYEMENT_DUE_DAY").toString());
					instalRes.setTransactionList(insMap.get("TRANSACTION_NO")==null?"":insMap.get("TRANSACTION_NO").toString());
					instalResList.add(instalRes);
				}
				
				for (int k = 0; k < Integer.parseInt(req.getNoOfInst()); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					instalRes.setPaymentDueDays(req.getReceiptofPayment()); 
					instalResList.add(instalRes);
				}
				formObj.setInstalmentList(instalResList);
			}else{
				for (int k = 0; k < Integer.parseInt(req.getNoOfInst()); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					instalRes.setPaymentDueDays(req.getReceiptofPayment());
					instalResList.add(instalRes);

				}
				formObj.setInstalmentList(instalResList);
			}
			
//			getInsurarerDetails(formObj,false);
//			GetRemarksDetails(formObj);
//			
//			if("Y".equalsIgnoreCase(formObj.getLossRecord())){
//				getLossDEtails(formObj);
//			}
			response.setCommonResponse(formObj);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetRemarksDetailsRes getRemarksDetails(String proposalNo) {
		GetRemarksDetailsRes response = new GetRemarksDetailsRes();
		GetRemarksDetailsRes1 res1 = new GetRemarksDetailsRes1();
		List<RemarksRes> remarksres=new ArrayList<RemarksRes>();
		try {
			List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
			String query= "GET_REMARKS_DETAILS";
			String[] obj= new String[2];
			obj[0]= proposalNo;
			obj[1]="0";
			result= queryImpl.selectList(query,obj);	
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					RemarksRes res=new RemarksRes();
					Map<String, Object> insMap = (Map<String, Object>)result.get(i);
					res.setDescription(insMap.get("RSK_DESCRIPTION")==null?"Remarks":insMap.get("RSK_DESCRIPTION").toString());
					res.setRemark1(insMap.get("RSK_REMARK1")==null?" ":insMap.get("RSK_REMARK1").toString());
					res.setRemark2(insMap.get("RSK_REMARK2")==null?"":insMap.get("RSK_REMARK2").toString());
					res.setRemarkSNo(Integer.toString(result.size()));
					remarksres.add(res);
				}
				
			}
//			else{
//				remarksres.add(res);
//			}
			res1.setRemarkCount(String.valueOf(result.size()));
			res1.setRemarksRes(remarksres);	
			response.setCommonResponse(remarksres);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetInsurarerDetailsRes getInsurarerDetails(GetInsurarerDetailsReq formObj) {
		GetInsurarerDetailsRes response = new GetInsurarerDetailsRes();
		List<GetInsurarerDetailsRes1> resList = new ArrayList<GetInsurarerDetailsRes1>();
		int noofInsurar =0;
		List<UwList> listUw = new ArrayList<UwList>();
		try{
//			List<String> retroTypeValList = new ArrayList<String>();
//			List<String> uwYearValList = new ArrayList<String>();
//			List<String> cedingCompanyValList = new ArrayList<String>();
//			List<String> retroPercentage = new ArrayList<String>();
	//		List<List<Map<String,Object>>> retroDupList=new ArrayList<List<Map<String,Object>>>();
	//		List<List<Map<String,Object>>> uwlList = new ArrayList<List<Map<String,Object>>>();
	//		List<List<Map<String,Object>>> retrolList = new ArrayList<List<Map<String,Object>>>();
			List<Map<String,Object>> retroCedList = new ArrayList<Map<String,Object>>();
			if(StringUtils.isNotBlank(formObj.getNoInsurer())){
				 noofInsurar = Integer.parseInt(formObj.getNoInsurer());
				 noofInsurar = noofInsurar+1;
			}
			String[] insargs=null;
			String query="";
			if("true".equalsIgnoreCase(formObj.getView())){
				insargs=new String[3];
				insargs[0]=formObj.getAmendId();
				insargs[1]=formObj.getProposalNo();
				insargs[2]=String.valueOf(noofInsurar);
				query= "fac.select.viewInsDetails";
			}else{
				query= "fac.select.insDetails";
				insargs=new String[2];
				insargs[0]=formObj.getProposalNo();
				insargs[1]=String.valueOf(noofInsurar);
			}
			List<Map<String,Object>> insDetailsList= queryImpl.selectList(query,insargs);
			if (insDetailsList!=null&&insDetailsList.size()>0){
				for(int j=0;j<insDetailsList.size();j++){
					GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
					Map<String,Object> insDetailsMap=(Map<String,Object>)insDetailsList.get(j);
					if("R".equalsIgnoreCase(insDetailsMap.get("TYPE").toString())){
						res.setRetper(insDetailsMap.get("RETRO_PER")==null?"":insDetailsMap.get("RETRO_PER").toString());
						if(StringUtils.isNotBlank(formObj.getNoInsurer())) {
						if("0".equalsIgnoreCase(res.getRetper())) {
							for(int z=0; z<Integer.valueOf(formObj.getNoInsurer()) ; z++) {
								res.setRetroTypeValList("");
								res.setUwYearValList("");
								res.setCedingCompanyValList("");
								res.setRetroPercentage("");
								resList.add(res);
								
//								uwlList.add(new ArrayList<Map<String,Object>>());
//								retrolList.add(new ArrayList<Map<String,Object>>());
							} }
					}
					}
					else{
						if(j<=noofInsurar){
							if(1==j){
								String uwYear ="";
								query = "FAC_SELECT_RETRO_DUP_CONTRACT";
								if(formObj.getYear().equalsIgnoreCase(insDetailsMap.get("UW_YEAR").toString())){
								res.setRetroDupYerar(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
								  uwYear = insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString();
								}else{
									res.setRetroDupYerar(formObj.getYear());
								  uwYear = formObj.getYear();
								}
								res.setRetroDupType(insDetailsMap.get("RETRO_TYPE")==null?"":insDetailsMap.get("RETRO_TYPE").toString());
								String retroType = insDetailsMap.get("RETRO_TYPE")==null?"":insDetailsMap.get("RETRO_TYPE").toString();
								List<Map<String,Object>> list =   queryImpl.selectList(query, new String[] {"4",(StringUtils.isBlank(retroType)?"":retroType),uwYear,formObj.getInceptionDate(),formObj.getBranchCode(),(StringUtils.isBlank(retroType)?"":retroType),uwYear,formObj.getInceptionDate()});
								for(int k=0;k<list.size();k++){
									Map<String,Object> map=list.get(k);
									res.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
								}
								}
							else {
						res.setRetroTypeValList(insDetailsMap.get("RETRO_TYPE")==null?"":insDetailsMap.get("RETRO_TYPE").toString());
						res.setUwYearValList(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
						res.setCedingCompanyValList(insDetailsMap.get("CONTRACTNO")==null?"":insDetailsMap.get("CONTRACTNO").toString());
						res.setRetroPercentage(insDetailsMap.get("RETRO_PER")==null?"":insDetailsMap.get("RETRO_PER").toString());
						query = "fac.select.uwYear";
						List<Map<String,Object>> uwList = queryImpl.selectList(query, new String[] {"4",formObj.getInceptionDate(),formObj.getBranchCode(),formObj.getInceptionDate()});
						if(uwList.size()>0 && uwList != null){
							for(int i=0; i<uwList.size(); i++) {
								UwList uw = new UwList();
								Map<String,Object> uwList1=(Map<String,Object>)uwList.get(i);
								uw.setContdet1(uwList1.get("CONTDET1")==null?"":uwList1.get("CONTDET1").toString());
								uw.setContdet2(uwList1.get("CONTDET2")==null?"":uwList1.get("CONTDET2").toString());
								listUw.add(uw);
							}		
							res.setUwList(listUw);
							}
						String retroType = insDetailsMap.get("RETRO_TYPE")==null?"":insDetailsMap.get("RETRO_TYPE").toString();
						
						String uwYear = insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString();
						
						if(StringUtils.isNotBlank(retroType) && "TR".equals(retroType)){
							query = "fac.select.retroContDetTR";
							retroCedList = queryImpl.selectList(query, new String[] {"4",uwYear,formObj.getInceptionDate(),formObj.getBranchCode(),uwYear,formObj.getInceptionDate()});
						}else{
							query = "fac.select.retroContDet";
							retroCedList = queryImpl.selectList(query, new String[] {"4",(StringUtils.isBlank(retroType)?"":retroType),uwYear,formObj.getInceptionDate(),formObj.getBranchCode(),(StringUtils.isBlank(retroType)?"":retroType),uwYear,formObj.getInceptionDate()});
						}
						}
						if(1==j){
							
							if(retroCedList.size()>0 && retroCedList != null){
								for(int i=0; i<retroCedList.size(); i++) {
									UwList uw = new UwList();
									Map<String,Object> retroCedList1=(Map<String,Object>)retroCedList.get(i);
									uw.setContdet1(retroCedList1.get("CONTDET1")==null?"":retroCedList1.get("CONTDET1").toString());
									uw.setContdet2(retroCedList1.get("CONTDET2")==null?"":retroCedList1.get("CONTDET2").toString());
									listUw.add(uw);
								}		
								res.setRetroDupList(listUw);
								}
						}
						else{
							if(retroCedList.size()>0 && retroCedList != null){
								for(int i=0; i<retroCedList.size(); i++) {
									UwList uw = new UwList();
									Map<String,Object> retroCedList1=(Map<String,Object>)retroCedList.get(i);;
									uw.setContdet1(retroCedList1.get("CONTDET1")==null?"":retroCedList1.get("CONTDET1").toString());
									uw.setContdet2(retroCedList1.get("CONTDET2")==null?"":retroCedList1.get("CONTDET2").toString());
									listUw.add(uw);
								}		
								res.setRetrolList(listUw);
						}
					}
					}
				}
				if(StringUtils.isNotBlank(formObj.getNoInsurer())) {
					int num = noofInsurar - (insDetailsList.size()-1);
					for(int z=0; z<num ; z++) {
						res.setUwYearValList("");
						res.setCedingCompanyValList("");
						res.setRetroPercentage("");
						
//						uwlList.add(new ArrayList<Map<String,Object>>());
//						retrolList.add(new ArrayList<Map<String,Object>>());
//						retroDupList.add(new ArrayList<Map<String,Object>>());
					}
				}
//				formObj.setUwlList(uwlList);
//				formObj.setRetrolList(retrolList);
//				formObj.setRetroDupList(retroDupList);
//				formObj.setRetroTypeValList(retroTypeValList);
//				formObj.setUwYearValList(uwYearValList);
//				formObj.setCedingCompanyValList(cedingCompanyValList);
//				formObj.setRetroPercentage(retroPercentage);
				resList.add(res);
				} }
				else if(StringUtils.isNotBlank(formObj.getNoInsurer())) {
				for(int z=0; z<Integer.valueOf(formObj.getNoInsurer())+1 ; z++) {
					GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
					res.setRetroTypeValList("");
					if(0==z){
						query = "fac.select.uwYear";
						List<Map<String,Object>> uwList = queryImpl.selectList(query, new String[] {"4",formObj.getInceptionDate(),formObj.getBranchCode(),formObj.getInceptionDate()});
						if(uwList.size()>0 && uwList != null){
							for(int i=0; i<uwList.size(); i++) {
								UwList uw = new UwList();
								Map<String,Object> uwList1=(Map<String,Object>)uwList.get(i);
								uw.setContdet1(uwList1.get("CONTDET1")==null?"":uwList1.get("CONTDET1").toString());
								uw.setContdet2(uwList1.get("CONTDET2")==null?"":uwList1.get("CONTDET2").toString());
								listUw.add(uw);
							}		
							res.setUwList(listUw);
							}
						res.setRetroDupYerar(formObj.getYear());
						res.setRetroDupType("TR");
						String query1 = "FAC_SELECT_RETRO_DUP_CONTRACT";
						List<Map<String,Object>> list =  queryImpl.selectList(query1, new String[] {"4","TR",formObj.getYear(),formObj.getInceptionDate(),formObj.getBranchCode(),"TR",formObj.getYear(),formObj.getInceptionDate()});
						for(int k=0;k<list.size();k++){
							Map<String,Object> map=list.get(k);
							res.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
						}
						res.setRetroDupMode("Duplicate");
					}
					else{
						//uwlList.add(new ArrayList<Map<String,Object>>());
						res.setRetroPercentage("");
						res.setUwYearValList("");
					}
					res.setCedingCompanyValList("");
					//retrolList.add(new ArrayList<Map<String,Object>>());
					resList.add(res);
				}
//				formObj.setUwlList(uwlList);
//				formObj.setRetrolList(retrolList);
//				formObj.setRetroTypeValList(retroTypeValList);
//				formObj.setUwYearValList(uwYearValList);
//				formObj.setCedingCompanyValList(cedingCompanyValList);
//				formObj.setRetroPercentage(retroPercentage);
			}
			if(StringUtils.isNotBlank(formObj.getNoInsurer()) && "0".equalsIgnoreCase(formObj.getNoInsurer())){
				GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
				res.setRetroDupYerar(formObj.getYear());
				res.setRetroDupType("TR");
				String query1 = "FAC_SELECT_RETRO_DUP_CONTRACT";
				List<Map<String,Object>> list =  queryImpl.selectList(query1, new String[] {"4","TR",formObj.getYear(),formObj.getInceptionDate(),formObj.getBranchCode(),"TR",formObj.getYear(),formObj.getInceptionDate()});
				for(int k=0;k<list.size();k++){
					GetInsurarerDetailsRes1 res1 = new GetInsurarerDetailsRes1();
					Map<String,Object> map=list.get(k);
					res1.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
					resList.add(res1);
				}
				resList.add(res);
			}
		response.setCommonResponse(resList);
		response.setMessage("Success");
		response.setIsError(false);
			}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Override
	public GetLossDEtailsRes getLossDEtails(String proposalNo) {
		GetLossDEtailsRes response = new GetLossDEtailsRes();
		
		List<GetLossDEtailsRes1> resList = new ArrayList<GetLossDEtailsRes1>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			String query= "GET_LOSS_DETIALS";
			String args[] = new String[2];
			args[0] = proposalNo;
			args[1] ="0";
			list =  queryImpl.selectList(query,args);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					GetLossDEtailsRes1 res = new GetLossDEtailsRes1();
					Map<String,Object> insMap = list.get(i);
					res.setLossYear(insMap.get("YEAR")==null?"":insMap.get("YEAR").toString());
					res.setLossNo(insMap.get("LOSS_NO")==null?"":insMap.get("LOSS_NO").toString());
					res.setLossinsuredName(insMap.get("INSURED_NAME")==null?"":insMap.get("INSURED_NAME").toString());
					res.setLossInceptionDate(insMap.get("INCEPTION_DATE")==null?"":insMap.get("INCEPTION_DATE").toString());
					res.setLossExpiryDate(insMap.get("EXPIRYDATE")==null?"":insMap.get("EXPIRYDATE").toString());
					res.setLossDateOfLoss(insMap.get("DATE_OF_LOSS")==null?"":insMap.get("DATE_OF_LOSS").toString());
					res.setLossCauseOfLoss(insMap.get("CAUSE_OF_LOSS")==null?"":insMap.get("CAUSE_OF_LOSS").toString());
					res.setLossInsuredClaim(insMap.get("INSURED_CLAIM")==null?"":fm.formatter(insMap.get("INSURED_CLAIM").toString()));
					res.setLossPremium(insMap.get("PREMIUM")==null?"":dropDowmImpl.formatterpercentage(insMap.get("PREMIUM").toString()));
					res.setLossRatio(insMap.get("LOSS_RATIO")==null?"":fm.formatter(insMap.get("LOSS_RATIO").toString()));
					res.setLossLeader(insMap.get("LEADER")==null?"":insMap.get("LEADER").toString());
					res.setLossITIReShare(insMap.get("ITI_RE_SHARE")==null?"":fm.formatter(insMap.get("ITI_RE_SHARE").toString()));
					res.setLossCount(Integer.toString(list.size()));
					resList.add(res);
				}
			}
//			if(list.size()<=0){
//			for(int i=0;i<5;i++){
//				Map<String,Object> string = new HashMap<String,Object>();
//				string.put("1","1");
//				list.add(string);
//				
//				}
//			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse moveBonus(MoveBonusReq bean) {
		CommonResponse response = new CommonResponse();
		try{
			if(StringUtils.isBlank(bean.getEndorsmentno())){
				bean.setEndorsmentno("0");
			}
	  //      deleteMaintable(bean);
	        String query = "BONUS_MAIN_INSERT";
			String args[]=new String[14];
			for(int i=0;i<bean.getBonusReq().size();i++){
				BonusReq req = bean.getBonusReq().get(i);
				if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) &&StringUtils.isNotBlank(req.getBonusLowClaimBonus()) ){
			           args[0] =bean.getProposalNo();
			           args[1] = bean.getContractNo();
			           args[2] = bean.getProductId();
			           args[3] = bean.getBonusTypeId();
			           args[4] = req.getBonusFrom().replace(",", "");
			           args[5] = req.getBonusTo().replace(",", "");
			           args[6] = req.getBonusLowClaimBonus().replace(",", "");
			           args[7] = bean.getLoginid();
			           args[8] = bean.getBranchCode();
			           args[9] = req.getBonusSNo().replace(",", "");
			           args[10] =bean.getAcqBonus();
			           args[11] = bean.getEndorsmentno();
			           args[12] =bean.getDepartmentId();
			           args[13] ="0";
			           queryImpl.updateQuery(query,args);
					}	}
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse deleteMaintable(DeleteMaintableReq bean) {
		CommonResponse response = new CommonResponse();
		String query1="";
		String arg[]=null;
		String acqBonus = bean.getAcqBonus();
		String type="";
		try{
			query1 = "BONUS_PREVIOUS_TYPE_CHECK";
			 arg = new String[3];
			 arg[0] = bean.getProposalNo();
			 arg[1] = bean.getBranchCode();
			 arg[2] = "0";
			 List<Map<String,Object>> list = queryImpl.selectList(query1, arg);
			 if(!CollectionUtils.isEmpty(list)) {
				 type=list.get(0).get("TYPE")==null?"":list.get(0).get("TYPE").toString();
				}
			 if(!type.equalsIgnoreCase(acqBonus)){
				 acqBonus=type;
			 }
			if("".equalsIgnoreCase(bean.getEndorsmentno())){
				query1 = "BONUS_MAIN_DELETE";
				 arg = new String[4];
				 arg[0] = bean.getProposalNo();
				 arg[1] = bean.getBranchCode();
				 arg[2] = acqBonus;
				 arg[3] = "0";
			}
			else{
			 query1 = "BONUS_MAIN_DELETE2";
			 arg = new String[5];
			 arg[0] = bean.getProposalNo();
			 arg[1] = bean.getEndorsmentno();
			 arg[2] = bean.getBranchCode();
			 arg[3] = acqBonus;
			 arg[4] = "0";
			}
			 queryImpl.updateQuery(query1,arg);
			 response.setMessage("Success");
				response.setIsError(false);
					}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}

	@Override
	public SecondPageInsertRes secondPageInsert(SecondPageInsertReq beanObj) {
		SecondPageInsertRes response = new SecondPageInsertRes();
		SecondPageInsertRes1 res = new SecondPageInsertRes1();
		boolean saveFlag=false;
		try{
			final String[] args = secondPageInsertArg(beanObj);
			String query= "fac.sp.facultativepage2";
			int result= queryImpl.updateQuery(query,args);
		//	updateSecondPage(beanObj);
			if(result==1)
				saveFlag=true;
			else
				saveFlag=false;
			if("".equalsIgnoreCase(beanObj.getContractNo())||"0".equalsIgnoreCase(beanObj.getContractNo())){
				String[] conargs=new String[3];
				conargs[0]=beanObj.getProposalNo();
				conargs[1]=beanObj.getProposalNo();
				conargs[2]=beanObj.getProposalNo();
				query = "fac.select.contGen";
				List<Map<String,Object>>  list= queryImpl.selectList(query, conargs);
				if(list!=null&&list.size()>0){	
					Map<String, Object> contractMap = (Map<String, Object>)list.get(0);
					res.setProStatus(contractMap.get("RSK_STATUS")==null?"":contractMap.get("RSK_STATUS").toString());
					res.setContractNo(contractMap.get("RSK_CONTRACT_NO")==null?"":contractMap.get("RSK_CONTRACT_NO").toString());
					if("true".equalsIgnoreCase(beanObj.getFlag())){
						if("A".equalsIgnoreCase(res.getProStatus()) && ! "0".equalsIgnoreCase(beanObj.getShSd())){
							if(res.getContractNo()==null||"0".equalsIgnoreCase(res.getContractNo())||"".equalsIgnoreCase(res.getContractNo())){
								if(!"".equals(beanObj.getRenewalContractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									res.setContractNo(beanObj.getRenewalContractno());
								}else {
										res.setContractNo(fm.getSequence("Contract",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getYear()));
								}
								query = "common.update.riskDetContNo";
								int ress= queryImpl.updateQuery(query,new String[]{res.getContractNo(),beanObj.getProposalNo()});
								query= "common.update.posMasDetContNo";
								ress= queryImpl.updateQuery(query,new String[]{res.getContractNo(),beanObj.getProposalNo()});
								if(!"".equals(beanObj.getRenewalContractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									res.setStatus("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No: "+beanObj.getContractNo()+" and New Contract No : "+beanObj.getContractNo()+".");
								}else{
									res.setStatus("Your Proposal is converted to Contract with Proposal No : "+beanObj.getProposalNo() +" and Contract No : "+beanObj.getContractNo()+".");
								}
								res.setBackmode("Con");
							}
							else{
								res.setStatus("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+beanObj.getContractNo()+".");
								res.setBackmode("Con");
							}
						}else if("0".equalsIgnoreCase(res.getProStatus())||"P".equalsIgnoreCase(res.getProStatus())||"A".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("Pro");
						}else if("N".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("NTU");
						}	
					}else{
						if("0".equalsIgnoreCase(res.getProStatus())||"P".equalsIgnoreCase(res.getProStatus())||"A".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("Pro");
						}else if("N".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("NTU");
						}
					}
				}else {
					res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : ==>"+beanObj.getProposalNo());
					res.setBackmode("Pro");
				}		
			}else{
				res.setStatus("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+beanObj.getContractNo()+".");
				res.setBackmode("Con");
			}
//			if("Y".equalsIgnoreCase(beanObj.getLossRecord())){
//				inserLossRecord(beanObj);
//			}
//			instalMentPremium(beanObj);
//			insertInsurarerTableInsert(beanObj);
//			insertBonusDetails(beanObj);
//			insertCrestaMaintable(beanObj);
//			InsertRemarkDetails(beanObj);
			response.setCommonResponse(res);
			response.setMessage("Success");
				response.setIsError(false);
					}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	public String[] secondPageInsertArg(final SecondPageInsertReq beanObj) {
		String[] args=new String[34];
		args[0]=beanObj.getProposalNo()==null?"":beanObj.getProposalNo();
		args[1]=beanObj.getContractNo()==null?"":beanObj.getContractNo();
		args[2]=StringUtils.isEmpty(beanObj.getSumInsuredOurShare())? "0": beanObj.getSumInsuredOurShare();
		args[3]=StringUtils.isEmpty(beanObj.getGwpiOurShare())? "0": beanObj.getGwpiOurShare();
		args[4]=StringUtils.isEmpty(beanObj.getPmlOurShare())? "0": beanObj.getPmlOurShare();
		args[5]=StringUtils.isEmpty(beanObj.getTplOurShare())? "0": beanObj.getTplOurShare();
		args[6]=StringUtils.isEmpty(beanObj.getRiskGrade())? "": beanObj.getRiskGrade();
		args[7]=StringUtils.isEmpty(beanObj.getOccCode())? "": beanObj.getOccCode();
		args[8]=StringUtils.isEmpty(beanObj.getRiskDetail())? "": beanObj.getRiskDetail();
		args[9]=StringUtils.isEmpty(beanObj.getFireProt())? "": beanObj.getFireProt();
		args[10]=StringUtils.isEmpty(beanObj.getScope())? "": beanObj.getScope();
		args[11]=StringUtils.isEmpty(beanObj.getMbind())? "": beanObj.getMbind();
		args[12]=StringUtils.isEmpty(beanObj.getCategoryZone())? "": beanObj.getCategoryZone();
		args[13]=StringUtils.isEmpty(beanObj.getEqwsInd())? "0": beanObj.getEqwsInd();
		args[14]=StringUtils.isEmpty(beanObj.getWsThreat())? "0": beanObj.getWsThreat();
		args[15]=StringUtils.isEmpty(beanObj.getEqThreat())? "0": beanObj.getEqThreat();
		args[16]=StringUtils.isEmpty(beanObj.getCommn())? "0": beanObj.getCommn();
		args[17]=StringUtils.isEmpty(beanObj.getBrokerage())? "0": beanObj.getBrokerage();
		args[18]=StringUtils.isEmpty(beanObj.getTax())? "0": beanObj.getTax();
		args[19]=StringUtils.isEmpty(beanObj.getLossRecord())? "": beanObj.getLossRecord();
		args[20]=StringUtils.isEmpty(beanObj.getDgmsApproval())? "": beanObj.getDgmsApproval();
		args[21]=StringUtils.isEmpty(beanObj.getUnderwriterCode())? "": beanObj.getUnderwriterCode();
		args[22]=StringUtils.isEmpty(beanObj.getUwRecommendation())? "": beanObj.getUwRecommendation();
		args[23]=StringUtils.isEmpty(beanObj.getRemarks())? "": beanObj.getRemarks();
		args[24]=StringUtils.isEmpty(beanObj.getOthAccep())? "": beanObj.getOthAccep();
		args[25]=StringUtils.isEmpty(beanObj.getReftoHO())? "0": beanObj.getReftoHO();
		args[26]=StringUtils.isEmpty(beanObj.getAcqCost()) ? "0": beanObj.getAcqCost();
		args[27]=StringUtils.isEmpty(beanObj.getCu())? "0": beanObj.getCu();
		args[28]=StringUtils.isEmpty(beanObj.getCuRsn())? "": beanObj.getCuRsn();
		args[29]=StringUtils.isEmpty(beanObj.getOthercost())? "": beanObj.getOthercost();
		args[30]=StringUtils.isEmpty(beanObj.getMlopYN())? "": beanObj.getMlopYN();
		args[31]=StringUtils.isEmpty(beanObj.getAlopYN())? "": beanObj.getAlopYN();
		if("NCB".equalsIgnoreCase(beanObj.getAcqBonus())){
		args[32]=StringUtils.isEmpty(beanObj.getAcqBonusPercentage())? "": beanObj.getAcqBonusPercentage();
		}
		else{
		args[32]="";
		}
		args[33]=StringUtils.isEmpty(beanObj.getAcqBonus())? "": beanObj.getAcqBonus();
		return args;
	}

	@Override
	public CommonResponse updateSecondPage(UpdateSecondPageReq beanObj) {
		CommonResponse response = new CommonResponse();
		String query="";
		try{
			query = "RISK_COM_UPDATE";
			String args[] = new String[8];
			args[0] = beanObj.getLeaderUnderwriter();
			args[1] = beanObj.getLeaderUnderwritershare();
			args[2] = beanObj.getLeaderUnderwritercountry();
			args[3] = beanObj.getExclusion();
			args[4] = beanObj.getCrestaStatus();  //
			args[5] = StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
			args[6] = beanObj.getProposalNo();
			args[7] = StringUtils.isEmpty(beanObj.getEndorsmentno())?"0":beanObj.getEndorsmentno();;
			queryImpl.updateQuery(query,args);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse inserLossRecord(InserLossRecordReq beanObj) {
		CommonResponse response = new CommonResponse();
		String query="";
		String args[];
		String endtNo = "";
		try{
			query = "LOSS_DELETE";
			args= new String[2];
			args[0] = beanObj.getProposalNo();
			args[1] = "0";
			queryImpl.updateQuery(query,args);
			query = "SELECT_AMEND_ID";
			args = new String[1];
			args[0] = beanObj.getProposalNo();
			List<Map<String, Object>> list =queryImpl.selectList(query, args);
			if(!CollectionUtils.isEmpty(list)) {
				endtNo=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			query = "INSET_LOSS_REC";
			args = new String[18];
			for(int i=0;i<beanObj.getLossDetails().size();i++){
				LossDetailsReq req = beanObj.getLossDetails().get(i);
				if(StringUtils.isNotBlank(req.getLossYear())){
					args[0] = beanObj.getProposalNo();
					args[1] =StringUtils.isBlank(beanObj.getContractNo())?"":beanObj.getContractNo();
					args[2] =endtNo;
					args[3] =beanObj.getProductId();
					args[4] =beanObj.getBranchCode();
					args[5] ="0";
					args[6] =StringUtils.isBlank(req.getLossYear())?"0":req.getLossYear();
					args[7] =StringUtils.isBlank(req.getLossNo())?"0":req.getLossNo();
					args[8] =StringUtils.isBlank(req.getLossinsuredName())?"0":req.getLossinsuredName();
					args[9] =StringUtils.isBlank(req.getLossInceptionDate())?"":req.getLossInceptionDate();
					args[10] =StringUtils.isBlank(req.getLossExpiryDate())?"":req.getLossExpiryDate();
					args[11] =StringUtils.isBlank(req.getLossDateOfLoss())?"0":req.getLossDateOfLoss();
					args[12] =StringUtils.isBlank(req.getLossCauseOfLoss())?"0":req.getLossCauseOfLoss().replaceAll(",", "");
					args[13] =StringUtils.isBlank(req.getLossInsuredClaim())?"0":req.getLossInsuredClaim().replaceAll(",", "");
					args[14] =StringUtils.isBlank(req.getLossPremium())?"0":req.getLossPremium().replaceAll(",", "");
					args[15] =StringUtils.isBlank(req.getLossRatio())?"0":req.getLossRatio().replaceAll(",", "");
					args[16] =StringUtils.isBlank(req.getLossLeader())?"0":req.getLossLeader();
					args[17] =StringUtils.isBlank(req.getLossITIReShare())?"0":req.getLossITIReShare().replaceAll(",", "");
					queryImpl.updateQuery(query,args);
				}
			}
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse instalMentPremium(InstalMentPremiumReq beanObj) {
		CommonResponse response = new CommonResponse();
		String endtNo = "";
		try{
			String query = "SELECT_AMEND_ID";
			String[] args = new String[1];
			args[0] = beanObj.getProposalNo();
			List<Map<String, Object>> list =queryImpl.selectList(query, args);
			if(!CollectionUtils.isEmpty(list)) {
				endtNo=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			query = "delete.TTRN_MND_INSTALLMENTS";
			args = new String[2];
			args[0] = beanObj.getProposalNo();
			args[1] = endtNo;
			queryImpl.updateQuery(query,args);
			String insertQry = "risk.insert.instalPrem";
			for (int i = 0; i < Integer.parseInt(beanObj.getNoOfInst()); i++) {
				InstalmentperiodReq req = beanObj.getInstalmentDetails().get(i);
				String[] obj = new String[11];
				int res=0;
				obj[0] = String.valueOf(i + 1);
				obj[1] = beanObj.getProposalNo();
				obj[2] = StringUtils.isEmpty(beanObj.getContractNo()) ? "0"	: beanObj.getContractNo();
				obj[3] = "0";
				obj[4] = endtNo;
				obj[5] = StringUtils.isEmpty(req.getInstalmentDateList()) ? "" : req.getInstalmentDateList();
				obj[6] = StringUtils.isEmpty(req.getInstallmentPremium()) ? "" : req.getInstallmentPremium().replaceAll(",", "");
				obj[7] = StringUtils.isEmpty(req.getInstallmentPremium())|| StringUtils.isEmpty(beanObj.getUsCurrencyRate()) ? "0": String.valueOf(Double.parseDouble(req.getInstallmentPremium().replaceAll(",", ""))/Double.parseDouble(beanObj.getUsCurrencyRate()));
				obj[8] =(req.getPaymentDueDays()==null)?"":StringUtils.isEmpty(req.getPaymentDueDays()) ? "" : req.getPaymentDueDays();
				obj[9] = beanObj.getLoginid();
				obj[10] = beanObj.getBranchCode();
				res= queryImpl.updateQuery(insertQry,obj);
			}
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
   		return response;
	}

	@Override
	public CommonResponse insertInsurarerTableInsert(InsertInsurarerTableInsertReq beanObj) {
		CommonResponse response = new CommonResponse();
		String endtNo = "";
		try{
			final int LoopCount = beanObj.getNoInsurer()=="" ?0:Integer.parseInt(beanObj.getNoInsurer());
			String[] obj = new String[12];
			String query = "SELECT_AMEND_ID";
			String[] args = new String[1];
			args[0] = beanObj.getProposalNo();
			List<Map<String, Object>> list =queryImpl.selectList(query, args);
			if(!CollectionUtils.isEmpty(list)) {
				endtNo=list.get(0).get("AMEND_ID")==null?"":list.get(0).get("AMEND_ID").toString();
			}
			query = "delete.facul.data";
			args = new String[2];
			args[0] = beanObj.getProposalNo();
			args[1] = endtNo;
			queryImpl.updateQuery(query,args);
			if(LoopCount==0){
				beanObj.setRetper("100");
			}
			query= "fac.insert.insDetails";
			obj[0] = "0";
			obj[1] = beanObj.getProposalNo();
			obj[2] = "";
			obj[3] = endtNo;
			obj[4] = "R";
			obj[5] = StringUtils.isEmpty(beanObj.getRetper())? "0" :beanObj.getRetper();
			obj[6] = "Y";
			obj[7] = "";
			obj[8] = "";
			obj[9] = "";
			obj[10] = beanObj.getLoginid();
			obj[11] = beanObj.getBranchCode();
			int res= queryImpl.updateQuery(query, obj);
			obj = new String[12];
			obj[0] = "1";
			obj[1] = beanObj.getProposalNo();
			obj[2] = StringUtils.isEmpty(beanObj.getRetroDupContract()) ? "0" :beanObj.getRetroDupContract();
			obj[3] = endtNo;
			obj[4] = "C";
			obj[5] = StringUtils.isEmpty(beanObj.getRetper())? "0" :beanObj.getRetper();
			obj[6] = "Y";
			obj[7] = StringUtils.isEmpty(beanObj.getRetroDupYerar())? "0" :beanObj.getRetroDupYerar();
			obj[8] ="TR";
			obj[9] = "";
			obj[10] = beanObj.getLoginid();
			obj[11] = beanObj.getBranchCode();
			res= queryImpl.updateQuery(query, obj);
			int j=2;
			for (int i = 0; i < LoopCount; i++) {
				RetroDetails req = beanObj.getRetroDetails().get(i);
				obj = new String[12];
				obj[0] = String.valueOf(j);
				obj[1] = beanObj.getProposalNo();
				obj[2] = StringUtils.isEmpty(req.getCedingCompanyValList()) ? "0" :req.getCedingCompanyValList();
				obj[3] = endtNo;
				obj[4] = "C";
				obj[5] = StringUtils.isEmpty(req.getRetroPercentage())? "0" :req.getRetroPercentage();
				obj[6] = "Y";
				obj[7] = StringUtils.isEmpty(req.getUwYearValList())? "0" :req.getUwYearValList();
				obj[8] = StringUtils.isEmpty(req.getRetroTypeValList())? "" :req.getRetroTypeValList();
				obj[9] = "";
				obj[10] = beanObj.getLoginid();
				obj[11] = beanObj.getBranchCode();
				res= queryImpl.updateQuery(query, obj);
				if(i>0){
				if(StringUtils.isNotBlank(beanObj.getContractNo()) && !"0".equals(beanObj.getContractNo())&&"SR".equalsIgnoreCase(req.getRetroTypeValList())){
					String updateQry= "fac.update.fac.contractNo";
					int result=queryImpl.updateQuery(updateQry, new String[]{beanObj.getContractNo(),req.getCedingCompanyValList()});
				}
				}
			}
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
   		return response;
	}

	@Override
	public CommonResponse insertBonusDetails(InsertBonusDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
			if(!"LCB".equalsIgnoreCase(beanObj.getAcqBonus())){
				insetNOClaimBonusMainTable(beanObj);
			}
		String query = "UPDATE_CONTRACT_DETAILS";
		String args[]=new String[6];
		args[1] =beanObj.getProposalNo();
		args[0] = beanObj.getContractNo();
		args[2] = beanObj.getBranchCode();
		args[3] = beanObj.getAcqBonus();
		args[4] =  beanObj.getEndorsmentno();
		args[5] =  "0";
		queryImpl.updateQuery(query,args);
		response.setMessage("Success");
		response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}
		return response;
		}
	public void insetNOClaimBonusMainTable(InsertBonusDetailsReq bean) {
		try{
			if(StringUtils.isBlank(bean.getEndorsmentno())){
				bean.setEndorsmentno("0");
			}
			DeleteMaintableReq req = new DeleteMaintableReq();
			req.setAcqBonus(bean.getAcqBonus());
			req.setBranchCode(bean.getBranchCode());
			req.setEndorsmentno(bean.getEndorsmentno());
			req.setProposalNo(bean.getProposalNo());
			deleteMaintable(req);
			String query = "BONUS_MAIN_INSERT";
			String args[]=new String[14];
		           args[0] = bean.getProposalNo();
		           args[1] = bean.getContractNo();
		           args[2] = bean.getProductId();
		           args[3] = "";
		           args[4] = "";
		           args[5] = "";
		           args[6] = "";
		           args[7] = bean.getLoginid();
		           args[8] = bean.getBranchCode();
		           args[9] = "1";
		           args[10] = bean.getAcqBonus();
		           args[11] = bean.getEndorsmentno();
		           args[12] = bean.getDepartmentId();
		           args[13] = "0";
		           queryImpl.updateQuery(query,args);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public CommonResponse insertCrestaMaintable(InsertCrestaMaintableReq bean) {
		CommonResponse response = new CommonResponse();
		String query ="";
		String obj[] =null;
		try {
			int count = getCrestaCount(bean.getEndorsmentno(), bean.getProposalNo(), bean.getBranchCode());
			if(count<=0){
				    query= "MOVE_TO_CRESTA_MAIN_TABLE";
				    obj = new String[12];
					obj[0]=bean.getContractNo();
					obj[1]=bean.getProposalNo();
					obj[2]=StringUtils.isEmpty(bean.getEndorsmentno())?"0":bean.getEndorsmentno();;
					obj[3]=bean.getDepartmentId();
					obj[4]="";
					obj[5]="";
					obj[6]="";
					obj[7]="";
					obj[8]="";
					obj[9]=bean.getBranchCode();
					obj[10]="";
					obj[11]= "";
				 queryImpl.updateQuery(query,obj);
			}
			 query = "CREATA_CONTRACT_UPDATE";
			 obj = new String[4];
			 obj[0]=bean.getContractNo();
			 obj[1]=bean.getProposalNo();
			 obj[2]=StringUtils.isEmpty(bean.getEndorsmentno())?"0":bean.getEndorsmentno();;
			 obj[3]=bean.getBranchCode();
			 queryImpl.updateQuery(query,obj);
			 response.setMessage("Success");
				response.setIsError(false);
					}catch(Exception e){
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
				}
				return response;
	}
	public int getCrestaCount(String endorsmentno, String proposalNo,String branchCode) {
		int count=0;
		try {
			String query= "GET_CRESTA_DETAIL_COUNT";
			String[] obj=new String[3];
			obj[0]= proposalNo;
			obj[1]=StringUtils.isEmpty(endorsmentno)?"0":endorsmentno;;
			obj[2]= branchCode;
			List<Map<String, Object>> list  = queryImpl.selectList(query,obj);
			if(!CollectionUtils.isEmpty(list)) {
				count=list.get(0).get("RSK_ENDORSEMENT_NO")==null?0:Integer.parseInt(list.get(0).get("RSK_ENDORSEMENT_NO").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public ViewModeRes viewMode(ViewModeReq req) {
		ViewModeRes response = new ViewModeRes();
		ViewModeRes1 formObj = new ViewModeRes1();
		List<Map<String,Object>> list1;
		try
		{
			String[] args=new String[13];
			args[0]=req.getBranchCode();
			args[1]=req.getBranchCode();
			args[2]=req.getBranchCode();
			args[3]=req.getBranchCode();
			args[4]=req.getBranchCode();
			args[5]=req.getBranchCode();
			args[6]=req.getBranchCode();
			args[7]=req.getBranchCode();
			args[8]=req.getBranchCode();
			args[9]=req.getBranchCode();
			args[10]=req.getProposalNo();
			args[11]=req.getAmendId();
			args[12]=req.getAmendId();
			String query = "fac.select.viewfirstPage";
			List<Map<String,Object>> list = queryImpl.selectList(query,args);
			if(list!=null && list.size()>0){
				Map<String,Object> tempMap = list.get(0);
				formObj.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
				formObj.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
				formObj.setProductId(tempMap.get("TMAS_PRODUCT_NAME")==null?"":tempMap.get("TMAS_PRODUCT_NAME").toString());
				formObj.setDepartmentId(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
				formObj.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
				formObj.setProfitCenterCode(tempMap.get("TMAS_PFC_NAME")==null?"":tempMap.get("TMAS_PFC_NAME").toString());
				formObj.setSubProfitCenter(tempMap.get("RSK_SPFCID")==null?"":tempMap.get("RSK_SPFCID").toString());
				if(!"ALL".equalsIgnoreCase(formObj.getSubProfitCenter())){
					formObj.setSubProfitCenter(tempMap.get("TMAS_SPFC_NAME")==null?"":tempMap.get("TMAS_SPFC_NAME").toString().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));	
				}
				formObj.setCedingCompany(tempMap.get("COMAPNY")==null?"":tempMap.get("COMAPNY").toString());
				formObj.setBroker(tempMap.get("BROKER")==null?"":tempMap.get("BROKER").toString());
				formObj.setMonth(tempMap.get("MONTH")==null?"":tempMap.get("MONTH").toString());
				formObj.setYear(tempMap.get("RSK_UWYEAR")==null?"":tempMap.get("RSK_UWYEAR").toString());
				formObj.setUnderwriter(tempMap.get("UNDERWRITTER")==null?"":tempMap.get("UNDERWRITTER").toString());
				formObj.setInceptionDate(tempMap.get("INSDATE")==null?"":tempMap.get("INSDATE").toString());
				formObj.setExpiryDate(tempMap.get("EXPDATE")==null?"":tempMap.get("EXPDATE").toString());
				formObj.setAccountDate(tempMap.get("ACDATE")==null?"":tempMap.get("ACDATE").toString());
				formObj.setOriginalCurrency(tempMap.get("SHORT_NAME")==null?"":tempMap.get("SHORT_NAME").toString());
				formObj.setUsCurrencyRate(tempMap.get("RSK_EXCHANGE_RATE")==null?"":tempMap.get("RSK_EXCHANGE_RATE").toString());
				formObj.setTerritoryName(tempMap.get("TERRITORY_DESC")==null?"":tempMap.get("TERRITORY_DESC").toString());
				formObj.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
				formObj.setLocation(tempMap.get("RSK_LOCATION")==null?"":tempMap.get("RSK_LOCATION").toString());
				formObj.setCity(tempMap.get("RSK_CITY")==null?"":tempMap.get("RSK_CITY").toString());
				formObj.setNr(tempMap.get("NR")==null?"":tempMap.get("NR").toString());
				formObj.setCedantsRet(fm.formatter(tempMap.get("RSK_CEDANT_RETENTION")==null?"":tempMap.get("RSK_CEDANT_RETENTION").toString()));
				formObj.setMaxiumlimit(fm.formatter(tempMap.get("MAXIMUM_LIMIT_OC")==null?"":tempMap.get("MAXIMUM_LIMIT_OC").toString()));
				formObj.setDeductible(fm.formatter(tempMap.get("DEDUCTIBLE_OC")==null?"":tempMap.get("DEDUCTIBLE_OC").toString()));
				formObj.setDeductibleDC(fm.formatter(tempMap.get("DEDUCTIBLE_DC")==null?"":tempMap.get("DEDUCTIBLE_DC").toString()));
				formObj.setDeductibleFacXol(fm.formatter(tempMap.get("DEDUCTIBLE_FACXOL_OC")==null?"":tempMap.get("DEDUCTIBLE_FACXOL_OC").toString()));
				formObj.setDeductibleFacXolDC(fm.formatter(tempMap.get("DEDUCTIBLE_FACXOL_DC")==null?"":tempMap.get("DEDUCTIBLE_FACXOL_DC").toString()));
				formObj.setSpRetro(tempMap.get("SP_RETRO")==null?"":tempMap.get("SP_RETRO").toString());
				formObj.setPml(tempMap.get("PML")==null?"":tempMap.get("PML").toString());
				formObj.setSipml(tempMap.get("SI_PML_OC")==null?"":tempMap.get("SI_PML_OC").toString());
				formObj.setCu(tempMap.get("CU")==null?"":dropDowmImpl.exchRateFormat(tempMap.get("CU").toString()));
				formObj.setCuRsn(tempMap.get("CU_RSN")==null?"":tempMap.get("CU_RSN").toString());
				formObj.setSumInsured(fm.formatter(tempMap.get("SUM_INSURED_OC")==null?"":tempMap.get("SUM_INSURED_OC").toString()));
				formObj.setGwpi(fm.formatter(tempMap.get("GWPI_OC")==null?"":tempMap.get("GWPI_OC").toString()));
				formObj.setPmll(fm.formatter(tempMap.get("PML_100_OC")==null?"":tempMap.get("PML_100_OC").toString()));
				formObj.setTpl(fm.formatter(tempMap.get("TPL_OC")==null?"":tempMap.get("TPL_OC").toString()));
				formObj.setShWt(tempMap.get("SHARE_WRITTEN")==null?"":tempMap.get("SHARE_WRITTEN").toString());
				formObj.setShSd(tempMap.get("SHARE_SIGNED")==null?"":tempMap.get("SHARE_SIGNED").toString());
				if (StringUtils.isNotBlank(tempMap.get("RSK_PROPOSAL_TYPE").toString())) {
					if ("P".equalsIgnoreCase(tempMap.get("RSK_PROPOSAL_TYPE").toString())||"0".equalsIgnoreCase(tempMap.get("RSK_PROPOSAL_TYPE").toString())) {
						formObj.setStatus("Pending");
					}else if ("N".equalsIgnoreCase(tempMap.get("RSK_PROPOSAL_TYPE").toString())) {
						formObj.setStatus("Not Taken Up");
					} else if ("A".equalsIgnoreCase(tempMap.get("RSK_PROPOSAL_TYPE").toString())) {
						formObj.setStatus("Accepted");
					} else if ("R".equalsIgnoreCase(tempMap.get("RSK_PROPOSAL_TYPE").toString())) {
						formObj.setStatus("Rejected");
					} 
				}
				formObj.setInterest(tempMap.get("RSK_INTEREST")==null?"":tempMap.get("RSK_INTEREST").toString());
				formObj.setSumusd(fm.formatter(tempMap.get("SUM_INSURED_DC")==null?"":tempMap.get("SUM_INSURED_DC").toString()));
				formObj.setGwpiUsd(fm.formatter(tempMap.get("GWPI_DC")==null?"":tempMap.get("GWPI_DC").toString()));
				formObj.setPmlusd(fm.formatter(tempMap.get("PML_100_DC")==null?"":tempMap.get("PML_100_DC").toString()));
				formObj.setTplusd(fm.formatter(tempMap.get("TPL_DC")==null?"":tempMap.get("TPL_DC").toString()));
				formObj.setSumOrginalUsd(fm.formatter(tempMap.get("SUM_INSURED_OUR_SHARE_DC")==null?"":tempMap.get("SUM_INSURED_OUR_SHARE_DC").toString()));
				formObj.setGwpiOurShareusd(fm.formatter(tempMap.get("GWPI_OUR_SHARE_DC")==null?"":tempMap.get("GWPI_OUR_SHARE_DC").toString()));
				formObj.setPmlOurShareusd(fm.formatter(tempMap.get("PML_OS_DC")==null?"":tempMap.get("PML_OS_DC").toString()));
				formObj.setTplOurshareusd(fm.formatter(tempMap.get("TPL_OUR_SHARE_DC")==null?"":tempMap.get("TPL_OUR_SHARE_DC").toString()));
				formObj.setDeductibleOurShareusd(fm.formatter(tempMap.get("DEDUCTIBLE_OURSHARE_DC")==null?"":tempMap.get("DEDUCTIBLE_OURSHARE_DC").toString()));
				formObj.setCoverlimitOurShareusd(fm.formatter(tempMap.get("DEDUCTIBLE_FACXOL_OURSHARE_DC")==null?"":tempMap.get("DEDUCTIBLE_FACXOL_OURSHARE_DC").toString()));
				formObj.setNoInsurer(tempMap.get("NO_OF_INSURERS")==null?"":tempMap.get("NO_OF_INSURERS").toString());
				formObj.setPolicyBranch(tempMap.get("TMAS_POL_BRANCH_NAME")==null?"":tempMap.get("TMAS_POL_BRANCH_NAME").toString());
				formObj.setScope(tempMap.get("SCOPE")==null?"":tempMap.get("SCOPE").toString());
				formObj.setSumInsuredOurShare(fm.formatter(tempMap.get("SUM_INSURED_OUR_SHARE_OC")==null?"":tempMap.get("SUM_INSURED_OUR_SHARE_OC").toString()));
				formObj.setGwpiOurShare(fm.formatter(tempMap.get("GWPI_OUR_SHARE_OC")==null?"":tempMap.get("GWPI_OUR_SHARE_OC").toString()));
				formObj.setPmlOCOurShare(fm.formatter(tempMap.get("PML_OS_OC")==null?"":tempMap.get("PML_OS_OC").toString()));
				formObj.setTplOurShare(fm.formatter(tempMap.get("TPL_OUR_SHARE_OC")==null?"":tempMap.get("TPL_OUR_SHARE_OC").toString()));
				formObj.setDeductibleOurShare(fm.formatter(tempMap.get("DEDUCTIBLE_OURSHARE_OC")==null?"":tempMap.get("DEDUCTIBLE_OURSHARE_OC").toString()));
				formObj.setCoverlimitOurShare(fm.formatter(tempMap.get("DEDUCTIBLE_FACXOL_OURSHARE_OC")==null?"":tempMap.get("DEDUCTIBLE_FACXOL_OURSHARE_OC").toString()));
				formObj.setPremiumrate(tempMap.get("RSK_PREMIUM_RATE")==null?"":tempMap.get("RSK_PREMIUM_RATE").toString());
				formObj.setCedRetenType(tempMap.get("RSK_CEDRET_TYPE")==null?"":tempMap.get("RSK_CEDRET_TYPE").toString());
				formObj.setModeOfTransport(tempMap.get("TRANSPORT_DESCRIPTION")==null?"":tempMap.get("TRANSPORT_DESCRIPTION").toString());
				formObj.setVesselName(tempMap.get("VESSEL_NAME")==null?"":tempMap.get("VESSEL_NAME").toString());
				formObj.setVesselAge(tempMap.get("VESSEL_AGE")==null?"":tempMap.get("VESSEL_AGE").toString());
				formObj.setXolOC(fm.formatter(tempMap.get("XOL_OC")==null?"":tempMap.get("XOL_OC").toString()));
				formObj.setXolDC(fm.formatter(tempMap.get("XOL_DC")==null?"":tempMap.get("XOL_DC").toString()));
				formObj.setXolOSOC(fm.formatter(tempMap.get("XOL_OURSHARE_OC")==null?"":tempMap.get("XOL_OURSHARE_OC").toString()));
				formObj.setXolOSDC(fm.formatter(tempMap.get("XOL_OURSHARE_DC")==null?"":tempMap.get("XOL_OURSHARE_DC").toString()));
				formObj.setNoOfInst(StringUtils.isBlank(tempMap.get("MND_INSTALLMENTS")==null?"":tempMap.get("MND_INSTALLMENTS").toString())?"0":tempMap.get("MND_INSTALLMENTS").toString());
				formObj.setType(tempMap.get("TYPE_NAME")==null?"":tempMap.get("TYPE_NAME").toString());

				String lpvOC = tempMap.get("LIMIT_PER_VESSEL_OC")==null?"":tempMap.get("LIMIT_PER_VESSEL_OC").toString();
				String lpvDC = tempMap.get("LIMIT_PER_VESSEL_DC")==null?"":tempMap.get("LIMIT_PER_VESSEL_DC").toString();
				String lplOC = tempMap.get("LIMIT_PER_LOCATION_OC")==null?"":tempMap.get("LIMIT_PER_LOCATION_OC").toString();
				String lplDC = tempMap.get("LIMIT_PER_LOCATION_DC")==null?"":tempMap.get("LIMIT_PER_LOCATION_DC").toString();

				formObj.setLimitPerVesselOC(fm.formatter(StringUtils.isBlank(formObj.getContractNo())&&(StringUtils.isBlank(lpvOC)||"0".equals(lpvOC))?"":lpvOC));
				formObj.setLimitPerVesselDC(fm.formatter(StringUtils.isBlank(formObj.getContractNo())&&(StringUtils.isBlank(lpvDC)||"0".equals(lpvDC))?"":lpvDC));
				formObj.setLimitPerLocationOC(fm.formatter(StringUtils.isBlank(formObj.getContractNo())&&(StringUtils.isBlank(lplOC)||"0".equals(lplOC))?"":lplOC));
				formObj.setLimitPerLocationDC(fm.formatter(StringUtils.isBlank(formObj.getContractNo())&&(StringUtils.isBlank(lplDC)||"0".equals(lplDC))?"":lplDC));
				formObj.setInwardType(tempMap.get("INWARD_BUS_TYPE")==null?"":tempMap.get("INWARD_BUS_TYPE").toString());
				formObj.setLocIssued(tempMap.get("RSK_LOC_ISSUED")==null?"":tempMap.get("RSK_LOC_ISSUED").toString());
				formObj.setLatitude(tempMap.get("RSK_LATITUDE")==null?"":tempMap.get("RSK_LATITUDE").toString());
				formObj.setLongitude(tempMap.get("RSK_LONGITUDE")==null?"":tempMap.get("RSK_LONGITUDE").toString());
				formObj.setVessaletonnage(tempMap.get("RSK_VESSAL_TONNAGE")==null?"":tempMap.get("RSK_VESSAL_TONNAGE").toString());
				formObj.setPslOC(tempMap.get("PSL_OC")==null?"0.00":fm.formatter(tempMap.get("PSL_OC").toString()));
				formObj.setPslusd(tempMap.get("PSL_DC")==null?"0.00":fm.formatter(tempMap.get("PSL_DC").toString()));
				formObj.setPslOurShare(tempMap.get("PSL_OS_OC")==null?"0.00":fm.formatter(tempMap.get("PSL_OS_OC").toString()));
				formObj.setPslOurShareusd(tempMap.get("PSL_OS_DC")==null?"0.00":fm.formatter(tempMap.get("PSL_OS_DC").toString()));
				formObj.setPllOC(tempMap.get("PLL_OC")==null?"0.00":fm.formatter(tempMap.get("PLL_OC").toString()));
				formObj.setPllusd(tempMap.get("PLL_DC")==null?"0.00":fm.formatter(tempMap.get("PLL_DC").toString()));
				formObj.setPllOurShare(tempMap.get("PLL_OS_OC")==null?"0.00":fm.formatter(tempMap.get("PLL_OS_OC").toString()));
				formObj.setPllOurShareusd(tempMap.get("PLL_OS_DC")==null?"0.00":fm.formatter(tempMap.get("PLL_OS_DC").toString()));
				formObj.setPblOC(tempMap.get("PBL_OC")==null?"0.00":fm.formatter(tempMap.get("PBL_OC").toString()));
				formObj.setPblusd(tempMap.get("PBL_DC")==null?"0.00":fm.formatter(tempMap.get("PBL_DC").toString()));
				formObj.setPblOurShare(tempMap.get("PBL_OS_OC")==null?"0.00":fm.formatter(tempMap.get("PBL_OS_OC").toString()));
				formObj.setPblOurShareusd(tempMap.get("PBL_OS_DC")==null?"0.00":fm.formatter(tempMap.get("PBL_OS_DC").toString()));
				formObj.setReceiptofPayment(tempMap.get("RSK_RECEIPT_PAYEMENT")==null?"":tempMap.get("RSK_RECEIPT_PAYEMENT").toString());
				formObj.setLocBankName(tempMap.get("RSK_LOC_BNK_NAME")==null?"":tempMap.get("RSK_LOC_BNK_NAME").toString());
				formObj.setLocCreditPrd(tempMap.get("RSK_LOC_CRDT_PRD")==null?"":tempMap.get("RSK_LOC_CRDT_PRD").toString());
				formObj.setLocCreditAmt(tempMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(tempMap.get("RSK_LOC_CRDT_AMT").toString()));
				formObj.setLocBeneficerName(tempMap.get("RSK_LOC_BENFCRE_NAME")==null?"":tempMap.get("RSK_LOC_BENFCRE_NAME").toString());
				formObj.setTerritory(tempMap.get("RSK_TERRITORY")==null?"":tempMap.get("RSK_TERRITORY").toString());
				formObj.setEndorsmenttype(tempMap.get("RS_ENDORSEMENT_TYPE")==null?"":tempMap.get("RS_ENDORSEMENT_TYPE").toString());
				formObj.setXollayerNo(tempMap.get("Xol_LAYER_NO")==null?"":tempMap.get("Xol_LAYER_NO").toString());
				String qry = "SELECT_TERRITORY_NAME";
				String arg[]=new String[2];
				if(StringUtils.isNotBlank(formObj.getTerritory())){
				arg[0] = formObj.getTerritory();
				arg[1] = req.getBranchCode();
				list1 = queryImpl.selectList(qry,arg);
				if(!CollectionUtils.isEmpty(list1)) {
					formObj.setTerritoryName(list1.get(0).get("TERRITORY_NAME")==null?"":list1.get(0).get("TERRITORY_NAME").toString());
				}
				}
				formObj.setCountryIncludedList(tempMap.get("COUNTRIES_INCLUDE")==null?"":tempMap.get("COUNTRIES_INCLUDE").toString());
				if(StringUtils.isNotBlank(formObj.getCountryIncludedList())){
				qry ="SELECT_COUNTRY_NAME";
				arg[0] = formObj.getCountryIncludedList();
				list1 = queryImpl.selectList(qry,arg);
				if(!CollectionUtils.isEmpty(list1)) {
					formObj.setCountryIncludedName(list1.get(0).get("COUNTRY_NAME")==null?"":list1.get(0).get("COUNTRY_NAME").toString());
				}
				formObj.setCountryIncludedName(formObj.getCountryIncludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
				}
				formObj.setCountryExcludedList(tempMap.get("COUNTRIES_EXCLUDE")==null?"":tempMap.get("COUNTRIES_EXCLUDE").toString());
				if(StringUtils.isNotBlank(formObj.getCountryExcludedList())){
				qry ="SELECT_COUNTRY_NAME";
				arg[0] = formObj.getCountryExcludedList();
				list1 = queryImpl.selectList(qry,arg);
				if(!CollectionUtils.isEmpty(list1)) {
					formObj.setCountryExcludedName(list1.get(0).get("COUNTRY_NAME")==null?"":list1.get(0).get("COUNTRY_NAME").toString());
				}
				formObj.setCountryExcludedName(formObj.getCountryExcludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
				}
			}
			args=new String[4];
			args[2]=req.getProposalNo();
			args[3]=req.getAmendId();
			args[0]=req.getBranchCode();
			args[1]=req.getBranchCode();
			query= "fac.select.viewsecondPage";
			list1 = queryImpl.selectList(query,args);
			if(list1!=null && list1.size()>0){
				Map<String,Object> tempMap = list1.get(0);
				formObj.setCu(tempMap.get("CU")==null?"":dropDowmImpl.exchRateFormat(tempMap.get("CU").toString()));
				formObj.setRiskGrade(tempMap.get("GRADE_DESC")==null?"":tempMap.get("GRADE_DESC").toString());
				formObj.setOccCode(tempMap.get("OCCUPATION_CODE")==null?"":tempMap.get("OCCUPATION_CODE").toString());
				formObj.setRiskDetail(tempMap.get("RISK_DETAILS")==null?"":tempMap.get("RISK_DETAILS").toString());
				formObj.setFireProt(StringUtils.isBlank(tempMap.get("FIRE_PORT")==null?"":tempMap.get("FIRE_PORT").toString())?"N":tempMap.get("FIRE_PORT").toString());
				formObj.setScope(tempMap.get("SCOPE")==null?"":tempMap.get("SCOPE").toString());
				formObj.setMbind(StringUtils.isBlank(tempMap.get("MB_IND")==null?"":tempMap.get("MB_IND").toString())?"N":tempMap.get("MB_IND").toString());
				formObj.setCategoryZone(tempMap.get("ZONE_DESC")==null?"":tempMap.get("ZONE_DESC").toString());
				formObj.setEqwsInd(tempMap.get("EARTHQUAKE_WS_IND_CON")==null?"":tempMap.get("EARTHQUAKE_WS_IND_CON").toString());
				formObj.setWsThreat(tempMap.get("WS_THREAT_IND")==null?"":tempMap.get("WS_THREAT_IND").toString());
				formObj.setEqThreat(tempMap.get("EQ_THREAT")==null?"":tempMap.get("EQ_THREAT").toString());
				
				formObj.setAcqBonus(tempMap.get("RSK_BONUS_ID")==null?"":tempMap.get("RSK_BONUS_ID").toString());
				formObj.setAcqBonusPercentage(dropDowmImpl.exchRateFormat(tempMap.get("RSK_NOCLAIMBONUS_PRCENT")==null?"":tempMap.get("RSK_NOCLAIMBONUS_PRCENT").toString()));
				formObj.setCommn(tempMap.get("RSK_COMM")==null?"":tempMap.get("RSK_COMM").toString());
				formObj.setBrokerage(tempMap.get("RSK_BROKERAGE")==null?"":tempMap.get("RSK_BROKERAGE").toString());
				formObj.setTax(tempMap.get("RSK_TAX")==null?"":tempMap.get("RSK_TAX").toString());
				formObj.setLossRecord(StringUtils.isBlank(tempMap.get("RSK_LOSS_RECORD")==null?"":tempMap.get("RSK_LOSS_RECORD").toString())?"N":tempMap.get("RSK_LOSS_RECORD").toString());
				formObj.setDgmsApproval(tempMap.get("RSK_DGM_APPROVAL")==null?"":tempMap.get("RSK_DGM_APPROVAL").toString());
				formObj.setUnderwriterCode(tempMap.get("RSK_UNDERWRITTER_CODE")==null?"":tempMap.get("RSK_UNDERWRITTER_CODE").toString());
				formObj.setUwRecommendation(tempMap.get("RSK_UW_RECOMMENDATION")==null?"":tempMap.get("RSK_UW_RECOMMENDATION").toString());
				formObj.setRemarks(tempMap.get("RSK_REMARKS")==null?"":tempMap.get("RSK_REMARKS").toString());
				formObj.setOthAccep(tempMap.get("RSK_OTH_ACCEP")==null?"":tempMap.get("RSK_OTH_ACCEP").toString());
				formObj.setReftoHO(StringUtils.isBlank(tempMap.get("RSK_REF_TO_HO")==null?"":tempMap.get("RSK_REF_TO_HO").toString())?"N":tempMap.get("RSK_REF_TO_HO").toString());
				formObj.setCuRsn(tempMap.get("CU_RSN")==null?"":tempMap.get("CU_RSN").toString());
				formObj.setAcqCost(fm.formatter(tempMap.get("RSK_ACQUISTION_COST_OC")==null?"":tempMap.get("RSK_ACQUISTION_COST_OC").toString()));
				formObj.setAccusd(fm.formatter(tempMap.get("RSK_ACQUISTION_COST_DC")==null?"":tempMap.get("RSK_ACQUISTION_COST_DC").toString()));
				
				formObj.setOthercost(tempMap.get("RSK_OTHER_COST")==null?"":tempMap.get("RSK_OTHER_COST").toString());
				formObj.setAcqCostPer(tempMap.get("ACC_PERCENTAGE")==null?"":tempMap.get("ACC_PERCENTAGE").toString());
				formObj.setMlopYN(StringUtils.isBlank(tempMap.get("M_LOP")==null?"":tempMap.get("M_LOP").toString())?"N":tempMap.get("M_LOP").toString());
				formObj.setAlopYN(StringUtils.isBlank(tempMap.get("A_LOP")==null?"":tempMap.get("A_LOP").toString())?"N":tempMap.get("A_LOP").toString());
				formObj.setEndorsementDate(tempMap.get("ENDORSEMENT_DATE")==null?"":tempMap.get("ENDORSEMENT_DATE").toString());
				formObj.setLeaderUnderwritercountry(tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
				formObj.setLeaderUnderwriter(tempMap.get("RSK_LEAD_UW")==null ? "" : tempMap.get("RSK_LEAD_UW").toString());
				formObj.setLeaderUnderwritershare(tempMap.get("RSK_LEAD_UW_SHARE")==null ? "" : tempMap.get("RSK_LEAD_UW_SHARE").toString());
				formObj.setCrestaStatus(tempMap.get("RSK_CREASTA_STATUS")==null?"":tempMap.get("RSK_CREASTA_STATUS").toString());
				formObj.setExclusion(tempMap.get("RSK_EXCLUSION")==null?"":tempMap.get("RSK_EXCLUSION").toString());
				
			}
			if("LCB".equalsIgnoreCase(formObj.getAcqBonus())){
				formObj.setAcqBonusName("Low Claim Bonus");
			}
			else if("NCB".equalsIgnoreCase(formObj.getAcqBonus())){
				formObj.setAcqBonusName("No Claim Bonus");
			}

			if(StringUtils.isNotBlank(formObj.getNoOfInst()) && Integer.parseInt(formObj.getNoOfInst())>0){
				List<InstalListRes> instalList=new ArrayList<InstalListRes>();
				for(int i=0;i<Integer.parseInt(formObj.getNoOfInst());i++){
					InstalListRes instal = new InstalListRes();
					instal.setInstall(String.valueOf(i));	
					instalList.add(instal);
				}
				formObj.setInstalList(instalList);
				}
			args = new String[3];
			args[0] = req.getProposalNo();
			args[1] = "0";
			args[2] = req.getAmendId();
			query = "risk.select.viewInstalmentData";
			List<Map<String,Object>> instalmentList = queryImpl.selectList(query,args);
			if (instalmentList != null) {
		
				List<InstalmentperiodReq> instalList = new ArrayList<InstalmentperiodReq>();
				for (int number = 0; number < instalmentList.size(); number++) {
					InstalmentperiodReq  instalment = new InstalmentperiodReq();
					Map<String,Object> insMap = (Map<String,Object>)instalmentList.get(number);
					instalment.setInstalmentDateList(insMap.get("INSTALLMENT_DATE")==null?"":insMap.get("INSTALLMENT_DATE").toString());
					instalment.setInstallmentPremium((insMap.get("MND_PREMIUM_OC")==null?"":fm.formatter(insMap.get("MND_PREMIUM_OC").toString())));
					instalment.setPaymentDueDays((insMap.get("PAYEMENT_DUE_DAY")==null?"":insMap.get("PAYEMENT_DUE_DAY").toString()));
					instalList.add(instalment);
				}
				formObj.setInstalmentperiod(instalList);
			}else{
				List<InstalmentperiodReq> instalList = new ArrayList<InstalmentperiodReq>();
				for (int k = 0; k < Integer.parseInt(formObj.getNoOfInst()); k++) {
					InstalmentperiodReq  instalment = new InstalmentperiodReq();
					instalment.setPaymentDueDays(formObj.getReceiptofPayment());
					instalList.add(instalment);
				}
				formObj.setInstalmentperiod(instalList);
			}
			String qry = "GET_POSITION_MASTER_CON_MAP";
			args = new String[2];
			args[0] = req.getProposalNo();
			args[1] = req.getAmendId();
			list1 = queryImpl.selectList(qry,args);
			if(!CollectionUtils.isEmpty(list1)) {
				formObj.setContractListVal(list1.get(0).get("DATA_MAP_CONT_NO")==null?"":list1.get(0).get("DATA_MAP_CONT_NO").toString());
			}
			if(StringUtils.isNotBlank(formObj.getContractListVal()) && !"None".equalsIgnoreCase(formObj.getContractListVal())) {
				String qrey = "GET_MAPPING_PROPOSAL_NO";
				args = new String[4];
				args[0] = formObj.getContractListVal();
				args[1] = "0";
				args[2] = formObj.getDepartmentId();
				args[3] = formObj.getYear();
				List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
				list2 = queryImpl.selectList(qrey, args);
				List<MappingRes> mapResList  = new ArrayList<MappingRes>();
				if(list2.size()>0){
					for(int i=0;i<list2.size();i++){
						Map<String,Object> map =list2.get(i);
						MappingRes mapRes = new MappingRes();
						mapRes.setMappingProposal(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
						mapRes.setMapingAmendId(map.get("AMEND_ID")==null?"":map.get("AMEND_ID").toString());
						mapResList.add(mapRes)	;
						}
					formObj.setMappingRes(mapResList);;
				}
			}
		//	getInsurarerDetails(formObj,true);
		//	GetRemarksDetails(formObj);
		//	GetCoverDeductableDetails(formObj);
		//	GetXolCoverDeductableDetails(formObj);
			if("Y".equalsIgnoreCase(formObj.getLossRecord())){
				getLossDEtails(req.getProposalNo());
			}
			response.setCommonResponse(formObj);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetCoverDeductableDetailsRes getCoverDeductableDetails(String proposalNo,String branchCode, String productId) {
		GetCoverDeductableDetailsRes response = new GetCoverDeductableDetailsRes();
		
		List<GetCoverDeductableDetailsRes1> resList = new ArrayList<GetCoverDeductableDetailsRes1>();
		try {
			List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
			String query= "GET_COVER_DEDUCTABLE_DETAILS";
			String[] obj= new String[2];
			obj[0]= proposalNo;
			obj[1]="0";
			result= queryImpl.selectList(query,obj);	
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					GetCoverDeductableDetailsRes1 res = new GetCoverDeductableDetailsRes1();
					Map<String, Object> insMap = (Map<String, Object>)result.get(i);
					res.setCoverdepartId(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString());
					res.setCoverageDays(insMap.get("RSK_COVERAGE_DAYS")==null?"":insMap.get("RSK_COVERAGE_DAYS").toString());
					res.setCoverLimitOC(insMap.get("RSK_COVERLIMIT_OC")==null?"":fm.formatter(insMap.get("RSK_COVERLIMIT_OC").toString()));	
					res.setCoverRemark(insMap.get("RSK_COVER_REMARKS")==null?"":insMap.get("RSK_COVER_REMARKS").toString());
					res.setCoversubdepartId(insMap.get("RSK_SUBCLASS")==null?"":insMap.get("RSK_SUBCLASS").toString());;
					res.setCoverTypeId(insMap.get("RSK_TYPE")==null?"":insMap.get("RSK_TYPE").toString());
					res.setDeductableDays(insMap.get("RSK_DEDUCTABLE_DAYS")==null?"":insMap.get("RSK_DEDUCTABLE_DAYS").toString());
					res.setDeductableLimitOC(insMap.get("RSK_DEDUCTABLELIMIT_OC")==null?"":fm.formatter(insMap.get("RSK_DEDUCTABLELIMIT_OC").toString()));;
					res.setEgnpiAsPerOff(insMap.get("RSK_GWPI_OC")==null?"":fm.formatter(insMap.get("RSK_GWPI_OC").toString()));
					res.setPmlHundredPer(fm.formatter(insMap.get("PML_HUN_PER_OC")==null?"0":insMap.get("PML_HUN_PER_OC").toString()));
					res.setPmlPerList(fm.formatter(insMap.get("PML_PERCENTAGE")==null?"0":insMap.get("PML_PERCENTAGE").toString()));
					res.setPremiumRateList(insMap.get("RSK_PREMIUM_RATE")==null?"":fm.formatter(insMap.get("RSK_PREMIUM_RATE").toString()));
					GetCommonDropDownRes dropDownRes= dropDowmImpl.getSubProfitCentreDropDown(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString(),branchCode,productId);
					res.setCoversubdeptList(dropDownRes.getCommonResponse());
					res.setLoopcount(Integer.toString(result.size()));;
					resList.add(res);
					}
			}
//			else{
//				Map<String,Object> doubleMap = new HashMap<String,Object>();
//				 doubleMap.put("one",new Double(1.0));
//				 result.add(doubleMap);
//				formObj.setCoverdeductableList(result);
//			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetXolCoverDeductableDetailsRes GetXolCoverDeductableDetails(String proposalNo, String branchCode) {
		GetXolCoverDeductableDetailsRes response = new GetXolCoverDeductableDetailsRes();
		
		List<GetXolCoverDeductableDetailsRes1> resList = new ArrayList<GetXolCoverDeductableDetailsRes1>();
		try {
			List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
			String query= "GET_XOLCOVER_DEDUCTABLE_DETAILS";
			String[] obj= new String[2];
			obj[0]= proposalNo;
			obj[1]="0";
			result= queryImpl.selectList(query,obj);	
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					GetXolCoverDeductableDetailsRes1 res = new GetXolCoverDeductableDetailsRes1();
					Map<String, Object> insMap = (Map<String, Object>)result.get(i);
					res.setXolcoverdepartId(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString());
					GetCommonDropDownRes dropDownRes= dropDowmImpl.getSubProfitCentreDropDown(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString(),branchCode,"1");
					res.setCoversubdeptList(dropDownRes.getCommonResponse());					
					res.setXolcoverLimitOC(insMap.get("RSK_COVERLIMIT_OC")==null?"":fm.formatter(insMap.get("RSK_COVERLIMIT_OC").toString()));;
					res.setXolcoversubdepartId(insMap.get("RSK_SUBCLASS")==null?"":insMap.get("RSK_SUBCLASS").toString());;
					res.setXoldeductableLimitOC(insMap.get("RSK_DEDUCTABLELIMIT_OC")==null?"":fm.formatter(insMap.get("RSK_DEDUCTABLELIMIT_OC").toString()));;
					res.setXolgwpiOC(insMap.get("RSK_GWPI_OC")==null?"":fm.formatter(insMap.get("RSK_GWPI_OC").toString()));	
					res.setXolpremiumRateList(insMap.get("RSK_PREMIUM_RATE")==null?"":fm.formatter(insMap.get("RSK_PREMIUM_RATE").toString()));
					res.setXolLoopcount(Integer.toString(result.size()));					
					resList.add(res);
				}
			}
//			else{
//				Map<String,Object> doubleMap = new HashMap<String,Object>();
//				 doubleMap.put("one",new Double(1.0));
//				 result.add(doubleMap);
//				formObj.setXolCoverdeductableList(result);
//			}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
					}catch(Exception e){
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
				}
				return response;
		}

	@Override
	public GetCommonValueRes getRetroContractDetails(GetRetroContractDetailsReq beanObj) {
		GetCommonValueRes response = new GetCommonValueRes();
		String  Cedingco="";
		String query="";
		try{
			List<Map<String,Object>> list =null;
			if("uwYear".equalsIgnoreCase(beanObj.getDropDown())) {
				query = "fac.select.uwYear";
				list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),beanObj.getInceptionDate(),beanObj.getBranchCode(),beanObj.getInceptionDate()});
				//beanObj.setUwYearList(list);
			}
			else if(StringUtils.isNotEmpty(beanObj.getYear())&&"Duplicate".equalsIgnoreCase(beanObj.getRetroDupMode())){
				query = "FAC_SELECT_RETRO_DUP_CONTRACT";
				list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getInceptionDate(),beanObj.getBranchCode(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getInceptionDate()});
				//beanObj.setCedingCompanyList(list);
			}
			else {
				if(StringUtils.isNotBlank(beanObj.getRetroType()) && "TR".equals(beanObj.getRetroType()) && "4".equals(beanObj.getProductId())){
					query = "fac.select.retroContDetTR";
					list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),beanObj.getYear(),beanObj.getInceptionDate(),beanObj.getBranchCode(),beanObj.getYear(),beanObj.getInceptionDate()});
					//beanObj.setCedingCompanyList(list);	
				}else{
				query = "fac.select.retroContDet";
				list = queryImpl.selectList(query, new String[] {beanObj.getProductId(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getInceptionDate(),beanObj.getBranchCode(),(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType()),beanObj.getYear(),beanObj.getInceptionDate()});
				//beanObj.setCedingCompanyList(list);
				}
			}
			if(list!=null && list.size()>0){
				Map<String,Object> resMap;
				for(int i=0;i<list.size();i++){
					resMap = (Map<String,Object>)list.get(i); 
					if(i==(list.size()-1)){
						Cedingco+=resMap.get("CONTDET1").toString()+"~"+resMap.get("CONTDET2").toString();
					}else{
						Cedingco+=resMap.get("CONTDET1").toString()+"~"+resMap.get("CONTDET2").toString()+"~";	
					}
				}
			}
			response.setCommonResponse(Cedingco);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetCommonValueRes getShareValidation(String proposalNo, String leaderUWShare) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result="false";
		String out = "";
		try {
			String query= "GET_SIGN_SHARE_PRODUCT1";
			List<Map<String,Object>> list= queryImpl.selectList(query, new String[]{proposalNo});
			if(!CollectionUtils.isEmpty(list)) {
				out = list.get(0).get("SHARE_SIGNED")==null?"":list.get(0).get("SHARE_SIGNED").toString();
			}
			if(Double.parseDouble(out)+Double.parseDouble(leaderUWShare)>100){
				result="true";
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetCommonValueRes getBonusListCount(String proposalNo, String branchCode, String acqBonus,
			String endorsmentno) {
		GetCommonValueRes response = new GetCommonValueRes();
		String query ="";
		String args[]=null;
		String result="";
		try{
			if(StringUtils.isBlank(endorsmentno)){
				endorsmentno = "0";
			}
			query = "BONUS_COUNT_MAIN";
			args = new String[5];
			args[0] = proposalNo;
			args[1] = branchCode;
			args[2] = acqBonus;
			args[3] = endorsmentno;
			args[4] ="0";
			List<Map<String,Object>> list = queryImpl.selectList(query,args);
			if(!CollectionUtils.isEmpty(list)) {
				result = list.get(0).get("COUNT")==null?"":list.get(0).get("COUNT").toString();
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

}
