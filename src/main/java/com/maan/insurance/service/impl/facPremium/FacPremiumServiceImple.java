package com.maan.insurance.service.impl.facPremium;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.req.facPremium.AddFieldValueReq;
import com.maan.insurance.model.req.facPremium.BonusdetailsReq;
import com.maan.insurance.model.req.facPremium.GetFieldValuesReq;
import com.maan.insurance.model.req.facPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumEditReq;
import com.maan.insurance.model.req.facPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.req.facPremium.PremiumedListreq;
import com.maan.insurance.model.res.facPremium.BonusRes;
import com.maan.insurance.model.res.facPremium.BonusdetailsRes;
import com.maan.insurance.model.res.facPremium.BonusdetailsRes1;
import com.maan.insurance.model.res.facPremium.CommonResponse;
import com.maan.insurance.model.res.facPremium.ContractDetailsRes;
import com.maan.insurance.model.res.facPremium.ContractDetailsRes1;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes1;
import com.maan.insurance.model.res.facPremium.GetAllocatedListResponse;
import com.maan.insurance.model.res.facPremium.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes1;
import com.maan.insurance.model.res.facPremium.GetFieldValuesRes;
import com.maan.insurance.model.res.facPremium.GetFieldValuesRes1;
import com.maan.insurance.model.res.facPremium.GetMandDInstallmentsRes;
import com.maan.insurance.model.res.facPremium.GetMandDInstallmentsRes1;
import com.maan.insurance.model.res.facPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.facPremium.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.facPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.facPremium.MdInstallmentDatesRes1;
import com.maan.insurance.model.res.facPremium.PreCurrencylistRes;
import com.maan.insurance.model.res.facPremium.PreListRes;
import com.maan.insurance.model.res.facPremium.PreListRes1;
import com.maan.insurance.model.res.facPremium.PreListRes2;
import com.maan.insurance.model.res.facPremium.PremListRes;
import com.maan.insurance.model.res.facPremium.PremiumContractDetailsRes;
import com.maan.insurance.model.res.facPremium.PremiumContractDetailsRes1;
import com.maan.insurance.model.res.facPremium.PremiumEditRes;
import com.maan.insurance.model.res.facPremium.PremiumEditRes1;
import com.maan.insurance.model.res.facPremium.PremiumEditRes2;
import com.maan.insurance.model.res.facPremium.PremiumEditResponse;
import com.maan.insurance.model.res.facPremium.PremiumListRes;
import com.maan.insurance.model.res.facPremium.PremiumTempRes;
import com.maan.insurance.model.res.facPremium.PremiumTempRes1;
import com.maan.insurance.model.res.facPremium.PremiumedListRes;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes1;
import com.maan.insurance.service.facPremium.FacPremiumService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
@Service
public class FacPremiumServiceImple implements FacPremiumService {
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;

	@Override
	public PremiumTempRes1 getpremiumTempList(String contNo, String branchCode) {
		PremiumTempRes1 response = new PremiumTempRes1();
		List<PremiumTempRes> finalList = new ArrayList<PremiumTempRes>();
		String query="";
	    String[] args=null;
	    try {
	      	args=new String[3];
	    	args[0]=contNo;
	    	args[1]=branchCode;
	    	args[2]=contNo;
		query="FAC_PREMIUM_SELECT_PREMIUMEDLIST_TEMP";
		List<Map<String,Object>> list=queryImpl.selectList(query, args);
		for(int i=0 ; i<list.size() ; i++) {
			Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
			PremiumTempRes tempBean = new PremiumTempRes();
			tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
			tempBean.setContNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
			tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBroker(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerno(tempMap.get("RSK_LAYER_NO")==null?"":tempMap.get("RSK_LAYER_NO").toString());
			//tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setRequestNo(tempMap.get("REQUEST_NO")==null?"":tempMap.get("REQUEST_NO").toString());
			tempBean.setAccountPeriod(tempMap.get("INS_DETAIL")==null?"":tempMap.get("INS_DETAIL").toString());
			tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
			if(i==0)
				tempBean.setEndtYN("No");
			else
				tempBean.setEndtYN("Yes");
			if(Double.parseDouble(tempMap.get("ALLOC_AMT").toString())!=0)
				tempBean.setEndtYN("Yes");	
			tempBean.setInceptionDate(tempMap.get("INS_DATE")==null?"":tempMap.get("INS_DATE").toString());
			tempBean.setStatementDate(tempMap.get("STATEMENT_DATE")==null?"":tempMap.get("STATEMENT_DATE").toString());
			tempBean.setMovementYN(tempMap.get("MOVEMENT_YN")==null?"":tempMap.get("MOVEMENT_YN").toString());
			//tempBean.setSettlement_Status(tempMap.get("SETTLEMENT_STATUS")==null?"":tempMap.get("SETTLEMENT_STATUS").toString());
			tempBean.setTransDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
			//if((StringUtils.isNotBlank(req.getOpstartDate()))&& (StringUtils.isNotBlank(req.getOpendDate()))){
				if(dropDowmImpl.Validatethree(branchCode, tempBean.getTransDate())==0){
					tempBean.setTransOpenperiodStatus("N");
				}else
				{
					tempBean.setTransOpenperiodStatus("Y");
				}
			tempBean.setProductId("1");
			finalList.add(tempBean);
		}
		response.setCommonResponse(finalList);
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
	public PremiumedListRes getPremiumedList(PremiumedListreq req) {
		PremiumedListRes response = new PremiumedListRes();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		String query="";
		int retroPrclStatus = 0;
		int allocationstatus = 0;
		try {
	    String[] args=null;
	      	args=new String[3];
	    	args[0]=req.getContNo();
	    	args[1]=req.getBranchCode();
	    	args[2]=req.getContNo();
		query = "premium.select.PremiumedList3";
		list=queryImpl.selectList(query, args);
		List<PremiumListRes> resList = new ArrayList<PremiumListRes>();
		for(int i=0 ; i<list.size() ; i++) {
			Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
			PremiumListRes tempBean = new PremiumListRes();
			tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
			tempBean.setContNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
			tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBroker(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerno(tempMap.get("RSK_LAYER_NO")==null?"":tempMap.get("RSK_LAYER_NO").toString());
			tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setAccountPeriod(tempMap.get("INS_DETAIL")==null?"":tempMap.get("INS_DETAIL").toString());
			tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
			if(i==0)
				tempBean.setEndtYN("No");
			else
				tempBean.setEndtYN("Yes");
			if(Double.parseDouble(tempMap.get("ALLOC_AMT").toString())!=0)
				tempBean.setEndtYN("Yes");	
			//tempBean.setInception_Date(tempMap.get("INS_DATE")==null?"":tempMap.get("INS_DATE").toString());
			tempBean.setStatementDate(tempMap.get("STATEMENT_DATE")==null?"":tempMap.get("STATEMENT_DATE").toString());
			tempBean.setMovementYN(tempMap.get("MOVEMENT_YN")==null?"":tempMap.get("MOVEMENT_YN").toString());
			//tempBean.setSettlement_Status(tempMap.get("SETTLEMENT_STATUS")==null?"":tempMap.get("SETTLEMENT_STATUS").toString());
			tempBean.setTransDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
			if((StringUtils.isNotBlank(req.getOpstartDate()))&& (StringUtils.isNotBlank(req.getOpendDate()))){
				if(dropDowmImpl.Validatethree(req.getBranchCode(), tempBean.getTransDate())==0){
					tempBean.setTransOpenperiodStatus("N");
				}else
				{
					tempBean.setTransOpenperiodStatus("Y");
				}
				}
			tempBean.setProductId("1");
			List<Map<String,Object>> list1 = queryImpl.selectList("premium.select.count",new String[]{tempBean.getContNo(),tempBean.getTransactionNo(),tempBean.getLayerno()});
			if (!CollectionUtils.isEmpty(list1)) {
				tempBean.setAllocatedYN(fm.formatter(list1.get(0).get("ALLOCATEDYN") == null ? ""
						: list1.get(0).get("ALLOCATEDYN").toString()));
			}
			
			int count=dropDowmImpl.Validatethree(req.getBranchCode(), tempBean.getTransDate());
			String args2[]=new String[1];
			args2[0]=tempBean.getTransactionNo();
			query="allocation.status";
			list1= queryImpl.selectList(query,args2);
			if (!CollectionUtils.isEmpty(list1)) {
				 allocationstatus = Integer.valueOf(list1.get(0).get("COUNT") == null ? ""
						: list1.get(0).get("COUNT").toString());
			}
			
			query="retro.status";
			list1= queryImpl.selectList(query,args2);
			if (!CollectionUtils.isEmpty(list1)) {
				 retroPrclStatus = Integer.valueOf(list1.get(0).get("COUNT") == null ? ""
						: list1.get(0).get("COUNT").toString());
			}
			int retroPrclStatus1=0;
			if(retroPrclStatus!=0){
			query="retro.status1";
			list1= queryImpl.selectList(query,args2);
			if (!CollectionUtils.isEmpty(list1)) {
				retroPrclStatus1 = Integer.valueOf(list1.get(0).get("COUNT") == null ? ""
						: list1.get(0).get("COUNT").toString());
			}
			}
			if(count!=0 && allocationstatus ==0 && retroPrclStatus1 ==0 ){
				tempBean.setDeleteStatus("Y");
			}
			resList.add(tempBean);
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
	public PreListRes1 getPreList(String contNo,String deptId) {
		PreListRes1 response = new PreListRes1();
		PreListRes2 res2 = new PreListRes2();
		List<PreListRes> resList = new ArrayList<PreListRes>();
		String query="";
		res2.setSaveFlag("false");
		String[] args = null;
		args = new String[2];
		try {
		args[0] = contNo;
		args[1]= deptId;
		query="premium.select.facTreatyPreList";	
		List<Map<String,Object>> list=queryImpl.selectList(query, args);
		if(list.size()>0 && list != null) {
		for(int i=0; i<list.size();i++) {
			PreListRes res = new PreListRes();
     		Map<String,Object> preList=(Map<String,Object>) list.get(i);
				res.setContNo(preList.get("CONTRACT_NO")==null?"":preList.get("CONTRACT_NO").toString());
				res.setDepartmentName(preList.get("TMAS_DEPARTMENT_NAME")==null?"":preList.get("TMAS_DEPARTMENT_NAME").toString());
				res.setUwYear(preList.get("UW_YEAR")==null?"":preList.get("UW_YEAR").toString());
				res.setCedingCompanyName(preList.get("COMPANY_NAME")==null?"":preList.get("COMPANY_NAME").toString());
				res.setLayerno(preList.get("LAYER_NO")==null?"":preList.get("LAYER_NO").toString());
				res.setBrokername(preList.get("BROKER_NAME")==null?"":preList.get("BROKER_NAME").toString());
				resList.add(res);
			} }
		res2.setCommonResponse(resList);
		if(list!=null && list.size()>0)
			res2.setSaveFlag("true");
		if (StringUtils.isNotBlank(contNo)) {
			list = queryImpl.selectList("premium.select.ceasestatus", new String[] {contNo});
			if (!CollectionUtils.isEmpty(list)) {
				res2.setCeaseStatus(fm.formatter(
						list.get(0).get("STATUS") == null ? "" : list.get(0).get("STATUS").toString()));
			}
		}
		response.setCommonResponse(res2);
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
	public GetCommonValueRes getPreviousPremium(String contNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String premium="";
		try{
			String query="select_PREMIUM_QUOTASHARE_OC";
			List<Map<String,Object>> list=queryImpl.selectList(query, new String[] {contNo});
			if (!CollectionUtils.isEmpty(list)) {
				premium = list.get(0).get("PREMIUM_QUOTASHARE_OC") == null ? "" : list.get(0).get("PREMIUM_QUOTASHARE_OC").toString();
			}
			response.setCommonResponse(premium);
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
	public GetCommonValueRes getContractPremium(String contNo, String branchCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		String premium="";
		try{
			String query="select_GWPI_OUR_SHARE_OC";
			List<Map<String,Object>> list=queryImpl.selectList(query, new String[] {contNo, branchCode});
			if (!CollectionUtils.isEmpty(list)) {
				premium = list.get(0).get("GWPI_OUR_SHARE_OC") == null ? "" : list.get(0).get("GWPI_OUR_SHARE_OC").toString();
			}
			response.setCommonResponse(premium);
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
	public ContractDetailsRes contractDetails(String contNo, String branchCode, String productId) {
		ContractDetailsRes response = new ContractDetailsRes();
		ContractDetailsRes1 bean = new ContractDetailsRes1();
		 String query="";
		 String[] args=null;
		 try {
				query= "premium.select.facContDet";
				args =new String[7];
				args[0] = contNo;
				args[1] = branchCode;
				args[2] = branchCode;
				args[3] = productId;
				args[4] = branchCode;
				args[5] = branchCode;
				args[6] = contNo;			
				List<Map<String,Object>> list=queryImpl.selectList(query, args);
				if(list!=null && list.size()>0) {
					Map<String,Object> contDet=(Map<String,Object>) list.get(0);
						bean.setContNo(contDet.get("RSK_CONTRACT_NO")==null ? "" : contDet.get("RSK_CONTRACT_NO").toString());
						bean.setAmendId(contDet.get("RSK_ENDORSEMENT_NO") ==null ? "" : contDet.get("RSK_ENDORSEMENT_NO").toString());
						bean.setProfitCenter(contDet.get("TMAS_PFC_NAME") ==null ? "" : contDet.get("TMAS_PFC_NAME").toString());
						bean.setSubProfitcenter(contDet.get("RSK_SPFCID") ==null ? "" : contDet.get("RSK_SPFCID").toString());
						if(!"ALL".equalsIgnoreCase(bean.getSubProfitcenter())){
						bean.setSubProfitcenter(contDet.get("TMAS_SPFC_NAME") ==null ? "" : contDet.get("TMAS_SPFC_NAME").toString());
						}
						bean.setCedingCo(contDet.get("COMPANY") ==null ? "" : contDet.get("COMPANY").toString());
						bean.setBroker(contDet.get("BROKER") ==null ? "" : contDet.get("BROKER").toString());
						bean.setTreatyNametype(contDet.get("RSK_TREATYID") ==null ? "" : contDet.get("RSK_TREATYID").toString());					
						bean.setProposalNo(contDet.get("RSK_PROPOSAL_NUMBER") ==null ? "" : contDet.get("RSK_PROPOSAL_NUMBER").toString());
						bean.setUwYear(contDet.get("RSK_UWYEAR") ==null ? "" : contDet.get("RSK_UWYEAR").toString());
						bean.setLayerno(contDet.get("RSK_LAYER_NO") ==null ? "" : contDet.get("RSK_LAYER_NO").toString());
						bean.setInsDate(contDet.get("INS_DATE") ==null ? "" : contDet.get("INS_DATE").toString());
						bean.setExpDate(contDet.get("EXP_DATE") ==null ? "" : contDet.get("EXP_DATE").toString());
						bean.setMonth(contDet.get("MONTH") ==null ? "" : contDet.get("MONTH").toString());
						bean.setBaseCurrencyId(contDet.get("RSK_ORIGINAL_CURR") ==null ? "" : contDet.get("RSK_ORIGINAL_CURR").toString());
						bean.setBaseCurrencyName(contDet.get("SHORT_NAME") ==null ? "" : contDet.get("SHORT_NAME").toString());
						bean.setInsuredname(contDet.get("RSK_INSURED_NAME") ==null ? "" : contDet.get("RSK_INSURED_NAME").toString());
						bean.setAddress(contDet.get("ADDRESS") ==null ? "" : contDet.get("ADDRESS").toString());
						bean.setTdsRate(contDet.get("SPECIAL_RATE") ==null ? "" : contDet.get("SPECIAL_RATE").toString());
						bean.setDepartmentId(contDet.get("RSK_DEPTID") ==null ? "" : contDet.get("RSK_DEPTID").toString());
						bean.setPredepartment(contDet.get("RSK_DEPTID") ==null ? "" : contDet.get("RSK_DEPTID").toString());
						bean.setConsubProfitId(contDet.get("RSK_SPFCID") ==null ? "" : contDet.get("RSK_SPFCID").toString());
						bean.setSubProfitId("ALL");
						bean.setAcceptenceDate(contDet.get("RSK_ACCOUNT_DATE") ==null ? "" : contDet.get("RSK_ACCOUNT_DATE").toString());
					}
					args=new String[2];
					args[0] = bean.getProposalNo();
					args[1] = bean.getProposalNo();
					query= "premium.select.commissionDetails";
					list=queryImpl.selectList(query, args);
					if(list!=null && list.size()>0) {
					Map<String,Object> commission=(Map<String,Object>) list.get(0);
							bean.setCommissionview(commission.get("RSK_COMM")==null ? "" : commission.get("RSK_COMM").toString());
							bean.setCommissionSurbview(commission.get("RSK_COMM_SURPLUS")==null ? "" : commission.get("RSK_COMM_SURPLUS").toString());
							bean.setOverRiderview(commission.get("RSK_OVERRIDER_PERC")==null ? "" : commission.get("RSK_OVERRIDER_PERC").toString());
							bean.setBrokerageview(commission.get("RSK_BROKERAGE")==null ? "" : commission.get("RSK_BROKERAGE").toString());
							bean.setTaxview(commission.get("RSK_TAX")==null ? "" : commission.get("RSK_TAX").toString());
							bean.setOtherCostView(commission.get("RSK_OTHER_COST")==null ? "" : commission.get("RSK_OTHER_COST").toString());
						}
					args[0] = bean.getProposalNo();
					args[1] = bean.getProposalNo();
					query = "premium.select.facProposalDetails";				
					list=queryImpl.selectList(query, args);
					if(list!=null && list.size()>0) {
					Map<String,Object> proposalDetails=(Map<String,Object>) list.get(0);
							bean.setShareSigned(proposalDetails.get("SHARE_SIGNED") ==null ? "" : proposalDetails.get("SHARE_SIGNED").toString());
							bean.setGwpiOS(proposalDetails.get("GWPI_OC") ==null ? "" : proposalDetails.get("GWPI_OC").toString());
						    bean.setRdsExchageRate(proposalDetails.get("SHARE_SIGNED") ==null ? "" : proposalDetails.get("SHARE_SIGNED").toString());
					}
						query= "premium.select.sumOfPaidPremium";
						list=queryImpl.selectList(query, new String[] {contNo});
						if (!CollectionUtils.isEmpty(list)) {
							bean.setSumofpaidpremium(list.get(0).get("PREMIUM_QUOTASHARE_OC") == null ? ""
									: list.get(0).get("PREMIUM_QUOTASHARE_OC").toString());
						}
						double sumofPaidPre= bean.getSumofpaidpremium()==null ? 0.0 : Double.parseDouble(bean.getSumofpaidpremium());
						if(bean.getRdsExchageRate() != null) {
						double sumpaidpremium =(sumofPaidPre/Double.parseDouble(bean.getRdsExchageRate()));
						bean.setSumofpaidpremium(Double.toString(sumpaidpremium));
						}
						sumofPaidPre=Double.parseDouble(bean.getSumofpaidpremium());
						double gwpiOSFor=bean.getGwpiOS()==null ? 0.0 : Double.parseDouble(bean.getGwpiOS());
						double preBooked=gwpiOSFor-sumofPaidPre;
						bean.setEpibalance(String.valueOf(preBooked));
						query= "premium.select.currecy.name";
						list=queryImpl.selectList(query, new String[]{branchCode});
						if (!CollectionUtils.isEmpty(list)) {
							bean.setCurrencyName(list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
									: list.get(0).get("COUNTRY_SHORT_NAME").toString());
						}
						bean.setSaveFlag("true");
						response.setCommonResponse(bean);
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
	public MdInstallmentDatesRes mdInstallmentDates(String contNo, String layerNo) {
		MdInstallmentDatesRes response = new MdInstallmentDatesRes();
		
		List<MdInstallmentDatesRes1> resList = new ArrayList<MdInstallmentDatesRes1>();
		String query="";
		 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    try{
		    String[] args=new String[6];
		    args[0]=contNo;
		    args[1]=layerNo;
		    args[2]=contNo;
		    args[3]=layerNo;
		    args[4] = contNo;
		    args[5]=layerNo;
		    query="PREMIUM_MND_INS_LIST";
		    list= queryImpl.selectList(query, args);		 
		    Map<String,Object> tempMap1 = new HashMap<String, Object>();
		    tempMap1.put("KEY1","EP");	    
		    tempMap1.put("VALUE","Endorsement Premium");
		    list.add(tempMap1);	
		    Map<String,Object> tempMap2 = new HashMap<String, Object>();
		    tempMap2.put("KEY1","RTP");	    
		    tempMap2.put("VALUE","Return Premium");
		    list.add(tempMap2);
		    Map<String,Object> tempMap3 = new HashMap<String, Object>();
		    tempMap3.put("KEY1","RVP");	    
		    tempMap3.put("VALUE","Reversal Premium");
		    list.add(tempMap3);
		    if(list.size()>0)
		    for(int i =0; i<list.size(); i++) {
		    	MdInstallmentDatesRes1 res = new MdInstallmentDatesRes1();
		    	Map<String,Object> map=(Map<String,Object>) list.get(i);
		    	res.setKey1(map.get("KEY1")==null ? "" : map.get("KEY1").toString());
		    	res.setValue(map.get("VALUE")==null ? "" : map.get("VALUE").toString());
		    	res.setInstallmentNo(map.get("INSTALLMENT_NO")==null ? "" : map.get("INSTALLMENT_NO").toString());
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
	public GetDepartmentIdRes getDepartmentId(String contNo, String productId) {
		GetDepartmentIdRes response = new GetDepartmentIdRes();
		GetDepartmentIdRes1 res = new GetDepartmentIdRes1();
		try{
			String query = "SELECT_DEPT_ID";
			String args[] = new String[2];
			args[0] = contNo;
			args[1] = productId;
			List<Map<String,Object>>list= queryImpl.selectList(query,args);
			if(list!=null &&list.size()>0){
				Map<String,Object>map=list.get(0);
				res.setDepartmentId(map.get("DEPT_ID")==null?"":map.get("DEPT_ID").toString());
				res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
			}
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

	@Override
	public PremiumEditRes premiumEdit(PremiumEditReq req) {
		PremiumEditRes response = new PremiumEditRes();
		PremiumEditResponse common = new PremiumEditResponse();
		 List<PremiumEditRes1> resList = new ArrayList<PremiumEditRes1>();
		String query="";
		boolean saveFlag=false;
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String[] args = new String[2];
		try {
		if( "transEdit".equalsIgnoreCase(req.getMode())){
		args[0] = req.getContNo();
		args[1] = req.getTransDropDownVal();
		query= "premium.select.facPremiumEdit";		
		 list= queryImpl.selectList(query, args);
		 if(list.size()>0)
		 {
			 for(int i=0;i<list.size();i++){
				 PremiumEditRes1 bean = new PremiumEditRes1();
				 Map<String,Object>	 editPremium=list.get(i);
					bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
					bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
					if((editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString())==""){
						com.maan.insurance.model.res.DropDown.GetCommonValueRes dropDown = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),req.getTransaction(),req.getCountryId(),req.getBranchCode());
						bean.setExchRate(dropDown.getCommonResponse());
					}
					else{
					bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString()));
					}
					bean.setPremiumQuotaShare((editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString()));
					bean.setPremiumQuotaShare(getMultipleVal(bean.getPremiumQuotaShare()));
					bean.setCommissionview((editPremium.get("COMMISSION") ==null?"":editPremium.get("COMMISSION").toString()));
					bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
					bean.setCommissionQuotaShare((getMultipleVal(bean.getCommissionQuotaShare())));
					bean.setBrokerageview(editPremium.get("BROKERAGE")==null?"":editPremium.get("BROKERAGE").toString());
					bean.setBrokerage(editPremium.get("BROKERAGE_AMT_OC")==null?"":editPremium.get("BROKERAGE_AMT_OC").toString());
					bean.setBrokerage((getMultipleVal(bean.getBrokerage())));
					bean.setTaxview(editPremium.get("TAX")==null?"":editPremium.get("TAX").toString());
					bean.setTax(editPremium.get("TAX_AMT_OC")==null?"":editPremium.get("TAX_AMT_OC").toString());
					bean.setTax((getMultipleVal(bean.getTax())));
	     			bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
					bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());		
					bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
					bean.setOtherCost(editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString());
					bean.setOtherCost((getMultipleVal(bean.getOtherCost())));
					bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
					bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
					bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
					bean.setNetDue((getMultipleVal(bean.getNetDue())));
					bean.setWithHoldingTaxOC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC")==null?"0":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
					bean.setWithHoldingTaxOC((getMultipleVal(bean.getWithHoldingTaxOC())));
					bean.setTaxDedectSource(fm.formatter(editPremium.get("TDS_OC")==null?"0":editPremium.get("TDS_OC").toString()));
					bean.setTaxDedectSource((getMultipleVal(bean.getTaxDedectSource())));
					bean.setServiceTax((editPremium.get("ST_OC")==null?"0":editPremium.get("ST_OC").toString()));
					bean.setServiceTax(getMultipleVal(bean.getServiceTax()));
					bean.setBonus((editPremium.get("BONUS_OC")==null?"0":editPremium.get("BONUS_OC").toString()));
					bean.setBonus(getMultipleVal(bean.getBonus()));
					bean.setTotalCredit(editPremium.get("TOTAL_CR_OC")==null?"":editPremium.get("TOTAL_CR_OC").toString());						
					bean.setTotalCredit((getMultipleVal(bean.getTotalCredit())));
					bean.setTotalDebit(editPremium.get("TOTAL_DR_OC")==null?"":editPremium.get("TOTAL_DR_OC").toString());
					bean.setTotalDebit((getMultipleVal(bean.getTotalDebit())));
					resList.add(bean);
			 }
			 common.setEditPremium(resList);
		 }
		}else{
			args[0] = req.getContNo();
			
			if("Temp".equalsIgnoreCase(req.getTableType())){
				query= "PREMIUM_SELECT_FACPREMIUMEDIT_TEMP";
				args[1] = req.getRequestNo();
			}else{
				query= "premium.select.facPremiumEdit";
				args[1] = req.getTransactionNo();
			}
			 list= queryImpl.selectList(query, args);
			 if(list.size()>0)
			 {
				 for(int i=0;i<list.size();i++){
					 Map<String,Object>	 editPremium=list.get(i);
					 PremiumEditRes1 bean = new PremiumEditRes1();							
					bean.setTransaction(editPremium.get("TRANS_DATE")==null?"":editPremium.get("TRANS_DATE").toString()); 
					if("EP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else if("RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else if("RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else
			    	{
			    		bean.setAccountPeriod((editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())+"_"+(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString()));
			    	}
						bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						if((editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString())==""){
							com.maan.insurance.model.res.DropDown.GetCommonValueRes dropDown = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),req.getTransaction(),req.getCountryId(),req.getBranchCode());
							bean.setExchRate(dropDown.getCommonResponse());
						}
						else{
						bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString()));
						}
						bean.setPremiumQuotaShare(fm.formatter(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString()));
						bean.setCommissionview(dropDowmImpl.formatterpercentage(editPremium.get("COMMISSION") ==null?"":editPremium.get("COMMISSION").toString()));
						bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
						bean.setBrokerageview(editPremium.get("BROKERAGE")==null?"":editPremium.get("BROKERAGE").toString());
						bean.setBrokerage(editPremium.get("BROKERAGE_AMT_OC")==null?"":editPremium.get("BROKERAGE_AMT_OC").toString());
						bean.setTaxview(editPremium.get("TAX")==null?"":editPremium.get("TAX").toString());
						bean.setTax(editPremium.get("TAX_AMT_OC")==null?"":editPremium.get("TAX_AMT_OC").toString());
		     			bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
						bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());		
						bean.setInceptionDate(editPremium.get("ENTRY_DATE")==null?"":editPremium.get("ENTRY_DATE").toString());
						bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
						bean.setOtherCost(editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString());
						bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
						bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
						bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
						bean.setWithHoldingTaxOC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC")==null?"0":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
						bean.setTaxDedectSource(fm.formatter(editPremium.get("TDS_OC")==null?"0":editPremium.get("TDS_OC").toString()));
			            bean.setServiceTax(fm.formatter(editPremium.get("ST_OC")==null?"0":editPremium.get("ST_OC").toString()));
			            bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"0":editPremium.get("BONUS_OC").toString()));
						bean.setTotalCredit(editPremium.get("TOTAL_CR_OC")==null?"":editPremium.get("TOTAL_CR_OC").toString());						
						bean.setTotalDebit(editPremium.get("TOTAL_DR_OC")==null?"":editPremium.get("TOTAL_DR_OC").toString());
						bean.setAmendmentDate(editPremium.get("AMENDMENT_DATE")==null?"":editPremium.get("AMENDMENT_DATE").toString());
						bean.setRicession(editPremium.get("RI_CESSION")==null?"":editPremium.get("RI_CESSION").toString());
						bean.setStatementDate(editPremium.get("STATEMENT_DATE")==null?"":editPremium.get("STATEMENT_DATE").toString());
						bean.setChooseTransaction(editPremium.get("REVERSEL_STATUS")==null?"":editPremium.get("REVERSEL_STATUS").toString() );
			            bean.setTransDropDownVal(editPremium.get("REVERSE_TRANSACTION_NO")==null?"":editPremium.get("REVERSE_TRANSACTION_NO").toString() );
			            resList.add(bean);
				 } 
				 common.setEditPremium(resList);
			 }
			
	} 
		String[] arg=null;
		PremiumEditRes2 res2 = new PremiumEditRes2();
		double sum= 0.0;
			if(list!=null && list.size()>0)
				saveFlag = true;
			if("1".equalsIgnoreCase(req.getProductId()))
			{
				arg=new String[1];
				arg[0] = req.getContNo();
			   	query= "premium.select.sumOfPaidPremium";
			   	if( "transEdit".equalsIgnoreCase(req.getMode())){
			   		query = "premium.select.sumOfPaidPremium1";
			   		arg=new String[2];
					arg[0] = req.getContNo();
					arg[1] = req.getTransDropDownVal();
			   	}
				
				double premium=Double.parseDouble(StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replaceAll(",", ""));
				list = queryImpl.selectList(query,arg);
				if (!CollectionUtils.isEmpty(list)) {
					sum = Double.parseDouble(list.get(0).get("PREMIUM_QUOTASHARE_OC") == null ? ""
							: list.get(0).get("PREMIUM_QUOTASHARE_OC").toString());
				}
			
				sum =sum-premium;
				double sumpaidpremium =(sum/Double.parseDouble(req.getRdsExchageRate()));
				res2.setSumofpaidpremium(Double.toString(sumpaidpremium));
				sum=Double.parseDouble(res2.getSumofpaidpremium());
				double gwpiOSFor=Double.parseDouble(req.getGwpiOS());
				double preBooked=gwpiOSFor-sum;
				res2.setEpibalance(String.valueOf(preBooked));
				res2.setSaveFlag(String.valueOf(saveFlag));
				common.setSum(res2);
				
	}
			response.setCommonResponse(common);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
		
	}
	private String getMultipleVal(String premiumQuotaShare) {
		String res="";double val =0;
		try{
			if(premiumQuotaShare==""){
				premiumQuotaShare="0";
			}
			 val = premiumQuotaShare==null?0.0:Double.parseDouble(premiumQuotaShare.replaceAll(",", ""))*-1;
			 if(val==-0){
				 val = 0; 
			 }
			res = fm.formatter(Double.toString(val));
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public CommonResponse premiumInsertMethod(PremiumInsertMethodReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
				String query="";
				int result;
				String[] args = insertArguments(beanObj);
			 	String netDueOc="0";
			 	query= "premium.insert.facpremium";
			 	netDueOc=args[17];
			 	result= queryImpl.updateQuery(query, args);
			 	if("submit".equalsIgnoreCase(beanObj.getButtonStatus())){
			 		beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
					query = "FAC_TEMP_STATUS_UPDATE";
					args = new String[5];
			 		args[0] = "A";
			 		args[1] = beanObj.getLoginId();
			 		args[2] =beanObj.getTransactionNo()==null?"":beanObj.getTransactionNo();
			 		args[3]= beanObj.getRequestNo() ;
			 		args[4]= beanObj.getBranchCode() ;
			 		queryImpl.updateQuery(query,args);
			 		getTempToMainMove(beanObj,netDueOc);
			 	if (result==1) {
					if(!("EP".equalsIgnoreCase(beanObj.getInstlmentNo()) || "RTP".equalsIgnoreCase(beanObj.getInstlmentNo()) || "RVP".equalsIgnoreCase(beanObj.getInstlmentNo()))){
					query= "UPDATE_MND_INSTALLMENT";
					args = new String[4];
					args[0]=beanObj.getTransactionNo();
					args[1]=beanObj.getContNo();
					args[2]=StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno();
					args[3]=beanObj.getInstlmentNo();
					queryImpl.updateQuery(query,args);
					}
				}
				if("transEdit".equalsIgnoreCase(beanObj.getMode())){
					query= "UPDATE_REV_TRANSACTION_NO";
					String  query1= "UPDATE_REV_TRANSACTION_NO_RETRO";
					args=new String[2];
					args[0] = beanObj.getTransactionNo();
					args[1] = beanObj.getTransDropDownVal();
					queryImpl.updateQuery(query,args);
					queryImpl.updateQuery(query1,args);
					args[1] = beanObj.getTransactionNo();
					args[0] = beanObj.getTransDropDownVal();
					queryImpl.updateQuery(query,args);
					queryImpl.updateQuery(query1,args);
					query= "UPDATE_MND_INSTAL";
					args=new String[3];
					args[0] = "";
					args[1] = beanObj.getProposalNo();
					args[2] = beanObj.getTransDropDownVal();
					queryImpl.updateQuery(query,args);
				}
		}
			 //	response.setCommonResponse(beanObj.getStatus());
				response.setMessage("Success");
				response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	private String[] insertArguments(final PremiumInsertMethodReq beanObj)
	{
		String[] args=null;
		args=new String[53];
		args[0]=beanObj.getReceiptno();
	    args[1]=beanObj.getContNo();
	    args[2] = getRequestNo(beanObj.getBranchCode());
		args[3]=beanObj.getTransaction();
		args[4]=StringUtils.isEmpty(beanObj.getInstalmentdate())?"":beanObj.getInstalmentdate();
		args[5]=StringUtils.isEmpty(beanObj.getInstlmentNo())?"":beanObj.getInstlmentNo();
		args[6]=beanObj.getCurrencyId();
		args[7]=beanObj.getExchRate();
		args[8]=getModeOfTransaction(beanObj.getPremiumQuotaShare(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[21]=dropDowmImpl.GetDesginationCountry(args[8], beanObj.getExchRate());
		args[9]=beanObj.getCommissionview();
		args[10]=getModeOfTransaction(beanObj.getCommissionQuotaShare(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[22]=dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
		args[11]=beanObj.getBrokerageview();
		args[12]=getModeOfTransaction(beanObj.getBrokerage(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[23]=dropDowmImpl.GetDesginationCountry(args[12], beanObj.getExchRate());
		args[13]=beanObj.getTaxview();
		args[14]=getModeOfTransaction(beanObj.getTax(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[24]=dropDowmImpl.GetDesginationCountry(args[14], beanObj.getExchRate());
	    args[33]=getModeOfTransaction(beanObj.getWithHoldingTaxOC(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[34]=dropDowmImpl.GetDesginationCountry(args[33], beanObj.getExchRate());
	    args[35]=beanObj.getRicession();
	    args[36]=beanObj.getLoginId();
	    args[37]=beanObj.getBranchCode();
	    args[38]=beanObj.getDepartmentId();
	    args[39] = getModeOfTransaction(beanObj.getTaxDedectSource(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[40] = dropDowmImpl.GetDesginationCountry(args[39],beanObj.getExchRate());
	    args[41] = getModeOfTransaction(beanObj.getServiceTax(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[42] = dropDowmImpl.GetDesginationCountry(args[41],beanObj.getExchRate());
	    args[43] = getModeOfTransaction(beanObj.getBonus(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[44] = dropDowmImpl.GetDesginationCountry(args[43],beanObj.getExchRate());
	    args[45]=beanObj.getPredepartment();
	    args[46]=beanObj.getSubProfitId().replace(" ", "");
			if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare()))
			{
				double premiums=Double.parseDouble(beanObj.getPremiumQuotaShare());
				if(StringUtils.isEmpty(beanObj.getCommissionQuotaShare()))
				{
					final double commission=premiums*(Double.parseDouble(beanObj.getCommissionview())/100);
					args[10]=getModeOfTransaction(commission+" ",beanObj.getEnteringMode(),beanObj.getShareSigned()); 
					args[22]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
				}
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[12]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(),beanObj.getShareSigned());
					args[23]=dropDowmImpl.GetDesginationCountry(args[12],beanObj.getExchRate());
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[14]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(),beanObj.getShareSigned());
					args[24]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
				}
			}
			args[15]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
			args[16]="Y";
			args[18]="2";
			args[19]=beanObj.getSettlementstatus();
			args[20]=getModeOfTransaction(beanObj.getOtherCost().replaceAll(",",""),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[26]=dropDowmImpl.GetDesginationCountry(args[20],beanObj.getExchRate());
			args[17]=getNetDueFac(args,1);
			args[25]=dropDowmImpl.GetDesginationCountry(args[17],beanObj.getExchRate());
			args[27]=beanObj.getCedentRef();
			args[28]=beanObj.getRemarks();
			args[29]=getModeOfTransaction(beanObj.getTotalCredit(),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[30]=dropDowmImpl.GetDesginationCountry(args[29],beanObj.getExchRate());
			args[31]=getModeOfTransaction(beanObj.getTotalDebit(),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[32]=dropDowmImpl.GetDesginationCountry(args[31],beanObj.getExchRate());
			args[47]=StringUtils.isEmpty(beanObj.getStatementDate()) ?"" :beanObj.getStatementDate();
			args[48]=beanObj.getProposalNo();
			args[49]=beanObj.getProductId();
			args[50]= beanObj.getChooseTransaction()==null?"":beanObj.getChooseTransaction();
			if("submit".equalsIgnoreCase(beanObj.getButtonStatus())){
				args[51] = "A";
			}else{
				args[51] = "P";
			}
			args[52] = beanObj.getMode();
			beanObj.setRequestNo(args[2]);
				
		final String[] copiedArray = new String[args.length];
		System.arraycopy(args, 0, copiedArray, 0, args.length);
		
		return copiedArray;
	}
	private String getRequestNo(String branchCode) {
		String reqNo = "";
		String name= "";
		try{
			String query= "GET_SEQ_NAME";
			List<Map<String,Object>> list = queryImpl.selectList(query, new String[] {branchCode});
			if (!CollectionUtils.isEmpty(list)) {
				name = list.get(0).get("SEQ_NAME") == null ? "" : list.get(0).get("SEQ_NAME").toString();
			}
			query="SELECT LPAD("+name+".nextval,6,0) FROM DUAL";
			list = queryImpl.selectSingle(query, new String[] {});
			if (!CollectionUtils.isEmpty(list)) {
				reqNo = list.get(0).get("REQ_NO") == null ? "" : list.get(0).get("REQ_NO").toString();
			}
			reqNo ="92"+reqNo;
		}catch(Exception e){
			e.printStackTrace();
		}
		return reqNo;
	}
	private static String getModeOfTransaction(final String Value,final String enteringMode, final String shareSigned) {
		String result="0";
		double shareSign=0.0;
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		if(enteringMode!=null)
		{
			if("1".equalsIgnoreCase(enteringMode))
			{
				shareSign=Double.valueOf(shareSigned);
			}
			else if("2".equalsIgnoreCase(enteringMode))
			{
				shareSign=100;
			}
			if(!"".equalsIgnoreCase(Value))
			{
					double finalValue=Double.parseDouble(Value) *shareSign/100;
					result=String.valueOf(Double.valueOf(twoDForm.format(finalValue)));
			} 
		}
		return result;
		}
	private static String getNetDueFac(final String[] args,final int flag) {
		Double Net = 0.0;
		if (flag == 1) {
			if (StringUtils.isNotEmpty(args[8]))
			{
				Net += Double.parseDouble(args[8]);
			}
			if (StringUtils.isNotEmpty(args[39]))
			{
				Net += Double.parseDouble(args[39]);
			}
			if (StringUtils.isNotEmpty(args[41]))
			{
				Net += Double.parseDouble(args[41]);
			}
			if (StringUtils.isNotEmpty(args[10]))
			{
				Net -= Double.parseDouble(args[10]);
			}
			if (StringUtils.isNotEmpty(args[12]))
			{
				Net -= Double.parseDouble(args[12]);
			}
			if (StringUtils.isNotEmpty(args[14]))
			{
				Net -= Double.parseDouble(args[14]);
			}
			if (StringUtils.isNotEmpty(args[20]))
			{
				Net -= Double.parseDouble(args[20]);
			}
			if (StringUtils.isNotEmpty(args[33]))
			{
				Net -= Double.parseDouble(args[33]);
			}
			if (StringUtils.isNotEmpty(args[43]))
			{
				Net -= Double.parseDouble(args[43]);
			}
		} else if (flag == 2) {
			if (StringUtils.isNotEmpty(args[5]))
			{
				Net += Double.parseDouble(args[5]);
			}
			if (StringUtils.isNotEmpty(args[36]))
			{
				Net += Double.parseDouble(args[36]);
			}
			if (StringUtils.isNotEmpty(args[38]))
			{
				Net += Double.parseDouble(args[38]);
			}
			if (StringUtils.isNotEmpty(args[7]))
			{
				Net -= Double.parseDouble(args[7]);
			}
			if (StringUtils.isNotEmpty(args[9]))
			{
				Net -= Double.parseDouble(args[9]);
			}
			if (StringUtils.isNotEmpty(args[11]))
			{
				Net -= Double.parseDouble(args[11]);
			}
			if (StringUtils.isNotEmpty(args[17]))
			{
				Net -= Double.parseDouble(args[17]);
			}
			if (StringUtils.isNotEmpty(args[31]))
	        {
	            Net -= Double.parseDouble(args[31]);
	        }
			if (StringUtils.isNotEmpty(args[40]))
	        {
	            Net -= Double.parseDouble(args[40]);
	        }
		}
		return String.valueOf(Net);
	}
	private void getTempToMainMove(PremiumInsertMethodReq beanObj, String netDueOc) {
		try{
			String query="";
			String[] args = null;
			if(!"Main".equalsIgnoreCase(beanObj.getTableType())){
				query = "FAC_PREMIUM_TEMP_TO_MAIN";
		 		args = new String[2];
		 		args[0] = beanObj.getRequestNo();
		 		args[1] = beanObj.getBranchCode();
		 		queryImpl.updateQuery(query,args);
			}
		query= "premium.sp.retroSplit";
		args = new String[16];
		args[0]=beanObj.getContNo();
		args[1]=StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno();
		args[2]=beanObj.getProductId();
		args[3]=beanObj.getTransactionNo();
		args[4]=beanObj.getTransaction();
		args[5]=beanObj.getCurrencyId();
		args[6]=beanObj.getExchRate();
		args[7]=netDueOc;
		args[7]=beanObj.getBranchCode();
		args[8]="P";
		args[9]=beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate();
		args[10]="";
		args[11]="";
		args[12]="";
		args[13]="";
		args[14]="";
		args[15]= beanObj.getRicession();
		int spresult=queryImpl.updateQuery(query,args);		
		//beanObj.setStatus("Premium Saved Sucussfully , And Your Transaction Id :"+ beanObj.getTransactionNo());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<Map<String,Object>> getSPRetroList(String contNo,String productId, String layerNo)
	{
		List<Map<String,Object>> list=null;
		try{
			String query="";
			String args[]=null;
			if("1".equals(productId)){
				args=new String[1];
				args[0]=contNo;
				query= "premium.select.getFacSPRetro";
			}else if("2".equals(productId))
			{
				args=new String[1];
				args[0]=contNo;
				query= "premium.select.getTreatySPRetro";
			}else if("3".equals(productId)){
				args=new String[2];
				args[0]=contNo;
				args[1]=layerNo;
				query= "premium.select.geXOLSPRetro";	
			}
			 list= queryImpl.selectList(query, args);
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		
		return list;
}
	public List<Map<String,Object>> getRetroContracts(String proposalNo, String noOfRetro) {
		List<Map<String,Object>> list=null;
		try{
			String query="";
			String args[]=null;
			args=new String[2];
			args[0]= proposalNo;
			args[1]= noOfRetro;
			query= "premium.select.insDetails";
			
			list= queryImpl.selectList(query, args);
		}catch(Exception e)
		{
			e.printStackTrace();	
		}
		return list;
	}
	public String getSumOfShareSign(String contNo, String retroContNo)
	{
		String query="";
		String  string="0";
		String noOfRetroCess="";
		String args[]=null;
		try{		
				query= "premium.select.getNoRetroCess";
				args=new String[1];
				args[0]= retroContNo;
				List<Map<String,Object>> list=queryImpl.selectList(query, args);
				if (!CollectionUtils.isEmpty(list)) {
					noOfRetroCess = list.get(0).get("RETRO_CESSIONARIES") == null ? "" : list.get(0).get("RETRO_CESSIONARIES").toString();
				}
				query= "premium.select.getSumOfShareSign";
				args=new String[2];
				args[0]= retroContNo;
				args[1]=String.valueOf(Integer.valueOf(noOfRetroCess)-1);
				list=queryImpl.selectList(query, args);
				if (!CollectionUtils.isEmpty(list)) {
					string = list.get(0).get("SHARE_SIGNED") == null ? "" : list.get(0).get("SHARE_SIGNED").toString();
				}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return string;
}
	public String getMovementReportMaxDate(String branchCode) {
		String maxDate = "";
		String query = "premium.mov.rep.max.date";
		try {
			List<Map<String,Object>> list= queryImpl.selectList(query, new String[]{branchCode});
			if (!CollectionUtils.isEmpty(list)) {
				maxDate = list.get(0).get("MAX_DATE") == null ? "" : list.get(0).get("MAX_DATE").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxDate;
	}

	@Override
	public CommonResponse premiumUpdateMethod(PremiumInsertMethodReq beanObj) {
		CommonResponse response = new CommonResponse();
		String query="";
		try {
			String[] args = updateAruguments(beanObj);
			String netDueOc="0";
			if("Temp".equalsIgnoreCase(beanObj.getTableType())){
				query= "PREMIUM_UPDATE_FACUPATEPRE_TEMP";
			}else{
				query= "premium.update.facUpdatePre";
			}
			netDueOc=args[14];	
			int update=  queryImpl.updateQuery(query,args);
			if("Submit".equalsIgnoreCase(beanObj.getButtonStatus()) && "Temp".equalsIgnoreCase(beanObj.getTableType())){
				beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
				query = "FAC_TEMP_STATUS_UPDATE";
				args = new String[5];
		 		args[0] = "A";	
		 		args[1] = beanObj.getLoginId();
		 		args[2] =beanObj.getTransactionNo()==null?"":beanObj.getTransactionNo();
		 		args[3]= beanObj.getRequestNo() ;
		 		args[4]= beanObj.getBranchCode() ;
		 		queryImpl.updateQuery(query,args);
				getTempToMainMove(beanObj,netDueOc);
				int spresult= queryImpl.updateQuery("premium.detail.archive",new String[]{beanObj.getContNo(),(StringUtils.isBlank(beanObj.getLayerno())?"0":beanObj.getLayerno()),beanObj.getTransactionNo(),beanObj.getCurrencyId(),beanObj.getExchRate(),netDueOc,beanObj.getDepartmentId(),beanObj.getProductId()});
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
	public  String[] updateAruguments(final PremiumInsertMethodReq beanObj) {
		String[] args=null;
		args=new String[48];
		args[0]=StringUtils.isEmpty(beanObj.getInstalmentdate())?"":beanObj.getInstalmentdate();
		args[1]=StringUtils.isEmpty(beanObj.getInstlmentNo())?"":beanObj.getInstlmentNo();
		args[2]=beanObj.getTransaction();
		args[3]=beanObj.getCurrencyId();
		args[4]=beanObj.getExchRate();
		args[5]=getModeOfTransaction(beanObj.getPremiumQuotaShare(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[18]=dropDowmImpl.GetDesginationCountry(args[5], beanObj.getExchRate());
		args[6]=beanObj.getCommissionview();
		args[7]=getModeOfTransaction(beanObj.getCommissionQuotaShare(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[19]=dropDowmImpl.GetDesginationCountry(args[7], beanObj.getExchRate());
		args[8]=beanObj.getBrokerageview();
		args[9]=getModeOfTransaction(beanObj.getBrokerage(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[20]=dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
		args[10]=beanObj.getTaxview();
		args[11]=getModeOfTransaction(beanObj.getTax(), beanObj.getEnteringMode(),beanObj.getShareSigned());
		args[21]=dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
	    args[31]=getModeOfTransaction(beanObj.getWithHoldingTaxOC(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[32]=dropDowmImpl.GetDesginationCountry(args[31], beanObj.getExchRate());
	    args[33]=beanObj.getRicession();
	    args[34]=beanObj.getLoginId();
	    args[35]=beanObj.getBranchCode();
	    args[36] = getModeOfTransaction(beanObj.getTaxDedectSource(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[37] = dropDowmImpl.GetDesginationCountry(args[36],beanObj.getExchRate());
	    args[38] = getModeOfTransaction(beanObj.getServiceTax(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[39] = dropDowmImpl.GetDesginationCountry(args[38],beanObj.getExchRate());
	    args[40] = getModeOfTransaction(beanObj.getBonus(), beanObj.getEnteringMode(),beanObj.getShareSigned());
	    args[41] = dropDowmImpl.GetDesginationCountry(args[40],beanObj.getExchRate());
	    args[42]=beanObj.getPredepartment();
	    args[43]=beanObj.getSubProfitId();
	    args[44]=beanObj.getStatementDate();
	    args[45] = beanObj.getMode();
	    args[46]=beanObj.getContNo();
	    if(StringUtils.isBlank(beanObj.getTransactionNo())){
	    	args[47]=beanObj.getRequestNo();
	    }else{
	    	args[47]=beanObj.getTransactionNo();
	    
	    }
			if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare()))
			{
				double premiums=Double.parseDouble(beanObj.getPremiumQuotaShare());
				if(StringUtils.isEmpty(beanObj.getCommissionQuotaShare()))
				{
					final double commission=premiums*(Double.parseDouble(beanObj.getCommissionview())/100);
					args[7]=getModeOfTransaction(commission+" ",beanObj.getEnteringMode(),beanObj.getShareSigned()); 
					args[19]=dropDowmImpl.GetDesginationCountry(args[7],beanObj.getExchRate());
				}
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[9]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(),beanObj.getShareSigned());
					args[20]=dropDowmImpl.GetDesginationCountry(args[9],beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[11]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(),beanObj.getShareSigned());
					args[21]=dropDowmImpl.GetDesginationCountry(args[11],beanObj.getExchRate());
				}
				
			}
			args[12]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
			args[13]="Y";
			args[15]="2";
			args[16]=beanObj.getReceiptno();
			args[17]=getModeOfTransaction(beanObj.getOtherCost(),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[23]=dropDowmImpl.GetDesginationCountry(args[17],beanObj.getExchRate());
			args[14]=getNetDueFac(args,2);
			args[22]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
			args[24]=beanObj.getCedentRef();
			args[25]=beanObj.getRemarks();
			args[26]=getModeOfTransaction(beanObj.getTotalCredit(),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[27]=dropDowmImpl.GetDesginationCountry(args[26],beanObj.getExchRate());
			args[28]=getModeOfTransaction(beanObj.getTotalDebit(),beanObj.getEnteringMode(),beanObj.getShareSigned());
			args[29]=dropDowmImpl.GetDesginationCountry(args[28],beanObj.getExchRate());
			args[30]=beanObj.getAmendmentDate();
		final String[] copiedArray = new String[args.length];
		System.arraycopy(args, 0, copiedArray, 0, args.length);
		return copiedArray;
	}

	@Override
	public GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();
		String query="";
		try{
				String[] args=new String[4];
				args[0] = req.getProductId();
				args[1] = req.getProductId();
			   	args[2]=req.getContNo();
			   	if("Temp".equalsIgnoreCase(req.getTableType()) || "".equalsIgnoreCase(req.getTransactionNo())){
			   		args[3] = req.getRequestNo();
			   		query= "PREMIUM_SELECT_FACPREMIVIEW_TEMP";
			   	}else{
			   		args[3]=req.getTransactionNo();
			   		query= "premium.select.facPremiumView";
			   	}
			   	List<Map<String,Object>>list= queryImpl.selectList(query,args);
				if(list!=null &&list.size()>0){
					for(int i= 0; i<list.size(); i++) {
								Map<String,Object> facView=(Map<String,Object>) list.get(0);
		   			bean.setTransaction(facView.get("TRANS_DATE") == null ? "" : facView.get("TRANS_DATE").toString());			
		   			bean.setAccountPeriod(facView.get("INSTALMENT_NUMBER")==null?"0":facView.get("INSTALMENT_NUMBER").toString()+(facView.get("ACCOUNT_PERIOD_QTR")==null?"0":("_"+facView.get("ACCOUNT_PERIOD_QTR").toString())));
					bean.setCurrencyId(facView.get("CURRENCY_ID")==null?"":facView.get("CURRENCY_ID").toString());
					bean.setExchRate(dropDowmImpl.exchRateFormat(facView.get("EXCHANGE_RATE")==null?"":facView.get("EXCHANGE_RATE").toString()));
					bean.setPremiumQuotaShare(facView.get("PREMIUM_QUOTASHARE_OC")==null?"":fm.formatter(facView.get("PREMIUM_QUOTASHARE_OC").toString()));
					bean.setCommissionview(facView.get("COMMISSION")==null?"":facView.get("COMMISSION").toString());
					bean.setCommissionQuotaShare(facView.get("COMMISSION_QUOTASHARE_OC")==null?"":fm.formatter(facView.get("COMMISSION_QUOTASHARE_OC").toString()));
					bean.setBrokerageview(facView.get("BROKERAGE")==null?"":facView.get("BROKERAGE").toString());
					bean.setBrokerage(facView.get("BROKERAGE_AMT_OC")==null?"":fm.formatter(facView.get("BROKERAGE_AMT_OC").toString()));
					bean.setTaxview(facView.get("TAX")==null?"":facView.get("TAX").toString());
					bean.setTax(facView.get("TAX_AMT_OC")==null?"":fm.formatter(facView.get("TAX_AMT_OC").toString()));
	     			bean.setStatus(facView.get("STATUS")==null?"":facView.get("STATUS").toString());
					bean.setReceiptno(facView.get("RECEIPT_NO")==null?"":facView.get("RECEIPT_NO").toString());
					bean.setInceptionDate(facView.get("ENTRY_DATE")==null?"":facView.get("ENTRY_DATE").toString());
					bean.setNetDue(facView.get("NETDUE_OC")==null?"":fm.formatter(facView.get("NETDUE_OC").toString()));
					bean.setTransactionNo(facView.get("TRANSACTION_NO")==null?"":facView.get("TRANSACTION_NO").toString());
					bean.setOtherCost(facView.get("OTHER_COST_OC")==null?"":fm.formatter(facView.get("OTHER_COST_OC").toString()));
					bean.setPremiumQuotaShareusd(facView.get("PREMIUM_QUOTASHARE_DC")==null?"":fm.formatter(facView.get("PREMIUM_QUOTASHARE_DC").toString()));
					bean.setCommsissionQuotaShareusd(facView.get("COMMISSION_QUOTASHARE_DC")==null?"":fm.formatter(facView.get("COMMISSION_QUOTASHARE_DC").toString()));
					bean.setBrokerageusd(facView.get("BROKERAGE_AMT_DC")==null?"":fm.formatter(facView.get("BROKERAGE_AMT_DC").toString()));
					bean.setTaxusd(facView.get("TAX_AMT_DC")==null?"":fm.formatter(facView.get("TAX_AMT_DC").toString()));
					bean.setNetdueusd(facView.get("NETDUE_DC")==null?"":fm.formatter(facView.get("NETDUE_DC").toString()));
					bean.setOtherCostUSD(facView.get("OTHER_COST_DC")==null?"":fm.formatter(facView.get("OTHER_COST_DC").toString()));
					bean.setCedentRef(facView.get("CEDANT_REFERENCE")==null?"":facView.get("CEDANT_REFERENCE").toString());
					bean.setRemarks(facView.get("REMARKS")==null?"":facView.get("REMARKS").toString());
					bean.setTotalCredit(facView.get("TOTAL_CR_OC")==null?"":fm.formatter(facView.get("TOTAL_CR_OC").toString()));
					bean.setTotalCreditDC(facView.get("INSTALMENT_NUMBER")==null?"":fm.formatter(facView.get("TOTAL_CR_DC").toString()));
					bean.setTotalDebit(facView.get("TOTAL_CR_DC")==null?"":fm.formatter(facView.get("TOTAL_DR_OC").toString()));
					bean.setTotalDebitDC(facView.get("TOTAL_DR_DC")==null?"":fm.formatter(facView.get("TOTAL_DR_DC").toString()));
					bean.setSettlementstatus(facView.get("SETTLEMENT_STATUS")==null?"":facView.get("SETTLEMENT_STATUS").toString());
					bean.setAmendmentDate(facView.get("AMENDMENT_DATE")==null?"":facView.get("AMENDMENT_DATE").toString());
					bean.setWithHoldingTaxOC(facView.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(facView.get("WITH_HOLDING_TAX_OC").toString()));
	                bean.setWithHoldingTaxDC(facView.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(facView.get("WITH_HOLDING_TAX_DC").toString()));
	                bean.setDueDate(facView.get("DUE_DATE")==null?"":facView.get("DUE_DATE").toString());
	                bean.setCreditsign(facView.get("NETDUE_OC")==null?"":facView.get("NETDUE_OC").toString());
	                bean.setRicession(facView.get("RI_CESSION")==null?"":facView.get("RI_CESSION").toString());
	                bean.setTaxDedectSource(facView.get("TDS_OC")==null?"":fm.formatter(facView.get("TDS_OC").toString()));
	                bean.setTaxDedectSourceDc(facView.get("TDS_DC")==null?"":fm.formatter(facView.get("TDS_DC").toString()));
	                bean.setServiceTax(facView.get("ST_OC")==null?"":fm.formatter(facView.get("ST_OC").toString()));
	                bean.setServiceTaxDc(facView.get("ST_DC")==null?"":fm.formatter(facView.get("ST_DC").toString()));
	                bean.setBonus(facView.get("BONUS_OC")==null?"":fm.formatter(facView.get("BONUS_OC").toString()));
	                bean.setBonusDc(facView.get("BONUS_DC")==null?"":fm.formatter(facView.get("BONUS_DC").toString()));
	                bean.setPremiumClass(facView.get("TMAS_DEPARTMENT_NAME")==null?"":facView.get("TMAS_DEPARTMENT_NAME").toString());
	                bean.setPremiumSubClass(facView.get("PREMIUM_SUBCLASS")==null?"":facView.get("PREMIUM_SUBCLASS").toString());
	                bean.setChooseTransaction(facView.get("REVERSEL_STATUS")==null?"":facView.get("REVERSEL_STATUS").toString());
	                bean.setTransDropDownVal(facView.get("REVERSE_TRANSACTION_NO")==null?"":facView.get("REVERSE_TRANSACTION_NO").toString());
	                if(!"ALL".equalsIgnoreCase(bean.getPremiumSubClass())){
	                	bean.setPremiumSubClass(facView.get("PREMIUM_SUBCLASS_VAL")==null?"":facView.get("PREMIUM_SUBCLASS_VAL").toString());
	                }
	                bean.setStatementDate(facView.get("STATEMENT_DATE")==null?"":facView.get("STATEMENT_DATE").toString());
					}
				}
			 	query= "premium.select.sumOfPaidPremium";
			 	list=queryImpl.selectList(query, new String[] {req.getContNo()});
				if (!CollectionUtils.isEmpty(list)) {
					bean.setSumofpaidpremium(list.get(0).get("PREMIUM_QUOTASHARE_OC") == null ? "" : list.get(0).get("PREMIUM_QUOTASHARE_OC").toString());
				}
				double sumofPaidPre=Double.parseDouble(bean.getSumofpaidpremium());
				if(req.getRdsExchageRate()!=null && req.getRdsExchageRate() != "0") {
				double sumpaidpremium =(sumofPaidPre/Double.parseDouble(req.getRdsExchageRate()));
				bean.setSumofpaidpremium(Double.toString(sumpaidpremium));
				sumofPaidPre=Double.parseDouble(bean.getSumofpaidpremium());
				double gwpiOSFor=Double.parseDouble(req.getGwpiOS());
				double preBooked=gwpiOSFor-sumofPaidPre;
				bean.setEpibalance(String.valueOf(preBooked));
				}
		   
		   	if(StringUtils.isNotBlank(bean.getCurrencyId())){
				query= "premium.select.currency";
				list= queryImpl.selectList(query, new String[] {bean.getCurrencyId(),req.getBranchCode()});
				if (!CollectionUtils.isEmpty(list)) {
					bean.setCurrency(list.get(0).get("CURRENCY_NAME") == null ? "" : list.get(0).get("CURRENCY_NAME").toString());
				}
		   	}
		   	query= "premium.select.currecy.name";
		   	list=queryImpl.selectList(query, new String[] {req.getBranchCode()});
			if (!CollectionUtils.isEmpty(list)) {
				bean.setCurrencyName(list.get(0).get("COUNTRY_SHORT_NAME") == null ? "" : list.get(0).get("COUNTRY_SHORT_NAME").toString());
			}
		   	query= "GETSETTLEMET_STATUS";
			List<Map<String,Object>> premlist = new ArrayList<Map<String,Object>>();
			premlist = queryImpl.selectList(query,new String[]{req.getContNo()});
			List<SettlementstatusRes> sResList = new ArrayList<SettlementstatusRes>();
			if(premlist.size()>0){
				for(int i=0;i<premlist.size();i++){
					Map<String,Object> map = premlist.get(i);
					SettlementstatusRes sRes = new SettlementstatusRes();
						String allocate = map.get("ALLOCATED_TILL_DATE")==null?"0":map.get("ALLOCATED_TILL_DATE").toString();
						String net = map.get("NETDUE_OC")==null?"0":map.get("NETDUE_OC").toString();
						if("0".equalsIgnoreCase(allocate)){
							sRes.setSettlementstatus("Pending");
						}else if(Double.parseDouble(allocate) == Double.parseDouble(net)){
							sRes.setSettlementstatus("Allocated");
						}else{
							sRes.setSettlementstatus("Partially Allocated");
						}
						sResList.add(sRes);
				}
				bean.setSettlementstatusRes(sResList);
			}
			response.setCommonResponse(bean);
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
	public GetCommonValueRes getInstalmentAmount(String contNo, String instalmentno) {
		GetCommonValueRes response = new GetCommonValueRes();
		String  string=null;
		try{
			String query= "premium.select.mndPremiumOC";
			// final String[] Instalmentno=getAmount.split("_");
			 List<Map<String,Object>> list=queryImpl.selectList(query, new String[] {contNo,"0",instalmentno});
				if (!CollectionUtils.isEmpty(list)) {
					string = list.get(0).get("MND_PREMIUM_OC") == null ? "" : list.get(0).get("MND_PREMIUM_OC").toString();
				}
			 response.setCommonResponse(string);
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
	public GetAllocatedListRes getAllocatedList(String contNo, String transactionNo) {
		GetAllocatedListRes response = new GetAllocatedListRes();
		GetAllocatedListResponse res1 = new GetAllocatedListResponse();
		double a=0;
		List<GetAllocatedListRes1> resList = new ArrayList<GetAllocatedListRes1>();
		try{		
			String[] args = new String[4];
			args[0] = contNo;
			args[1] = transactionNo;
			args[2] = contNo;
			args[3] = transactionNo;
			String selectQry = "payment.select.getAlloTransaction";
			List<Map<String,Object>> list = queryImpl.selectList(selectQry,args);
			if (list.size()>0) {			
				for (int i = 0; i < list.size(); i++) {				
					Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
					GetAllocatedListRes1 tempBean = new GetAllocatedListRes1();
					tempBean.setSerialno(tempMap.get("SNO")==null?"":tempMap.get("SNO").toString());
					tempBean.setAllocateddate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
					tempBean.setProductname(tempMap.get("PRODUCT_NAME")==null?"":tempMap.get("PRODUCT_NAME").toString());
					tempBean.setType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());
					tempBean.setPayamount(tempMap.get("PAID_AMOUNT")==null?"":tempMap.get("PAID_AMOUNT").toString());
					tempBean.setCurrencyValue(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					tempBean.setAlloccurrencyid(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					tempBean.setStatus(("R".equals(tempMap.get("STATUS")==null?"":tempMap.get("STATUS").toString())?"Reverted":"Allocated"));	
					tempBean.setPayrecno(tempMap.get("RECEIPT_NO")==null?"":tempMap.get("RECEIPT_NO").toString());
					tempBean.setSettlementType(tempMap.get("TRANS_TYPE")==null?"":tempMap.get("TRANS_TYPE").toString());
					if(tempBean.getSettlementType().equalsIgnoreCase("PAYMENT") || tempBean.getSettlementType().equalsIgnoreCase("RECEIPT")){
						tempBean.setAllocateType(tempMap.get("ALLOCATE_TYPE")==null?"":tempMap.get("ALLOCATE_TYPE").toString());
					}
					resList.add(tempBean);	
					a=a+Double.parseDouble(tempMap.get("PAID_AMOUNT")==null?"":tempMap.get("PAID_AMOUNT").toString());
				}
				res1.setAllocatedList(resList);
			}
			if(a>0){
				res1.setTotalAmount(fm.formatter(Double.toString(a)));
			}
			else{
				res1.setTotalAmount("");
			}
			response.setCommonResponse(res1);
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
	public CurrencyListRes currencyList(String branchCode) {
		CurrencyListRes response = new CurrencyListRes();
		List<CurrencyListRes1> resList = new ArrayList<CurrencyListRes1>();
		List<Map<String,Object>> list=null;
		try{
			String query="";
			String args[]=null;
			args=new String[2];
			args[0]=branchCode;
			args[1]=branchCode;
			query="currency.list";
			list= queryImpl.selectList(query,args);
			if (list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
					CurrencyListRes1 res = new CurrencyListRes1();
					res.setCurrencyId(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					res.setShortName(tempMap.get("SHORT_NAME")==null?"":tempMap.get("SHORT_NAME").toString());
					resList.add(res);
				}	
					response.setCommonResponse(resList);
					response.setMessage("Success");
					response.setIsError(false);
			}  }catch (Exception e) {
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
				    return response;
	}
	@Override
	public PremiumContractDetailsRes premiumContractDetails(String contNo, String branchCode, String layerNo) {
		PremiumContractDetailsRes response = new PremiumContractDetailsRes();
		 String query="";
	     String args[] = null;
	     List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	     try {
			 query = "FAC_PREMIUM_CONTRACT_DETAILS";
			 args = new String[6];
			 args[0] = contNo;
			 args[1] = layerNo;
			 args[2] = branchCode;
			 args[3] = branchCode;
			 args[4] = contNo;
			 args[5] = layerNo;
			 list = queryImpl.selectList(query, args);
			 List<PremiumContractDetailsRes1> resList = new ArrayList<PremiumContractDetailsRes1>();
			 if (list.size() > 0) {
				 for (int i = 0; i < list.size(); i++) {
					 Map<String, Object> map = list.get(i);
					 PremiumContractDetailsRes1 bean = new PremiumContractDetailsRes1();
					    bean.setContNo(map.get("RSK_CONTRACT_NO")== null ? "" : map.get("RSK_CONTRACT_NO").toString());
					    bean.setAmendId(map.get("RSK_ENDORSEMENT_NO")== null ? "" : map.get("RSK_ENDORSEMENT_NO").toString());
					    bean.setSubProfitcenter(map.get("TMAS_DEPARTMENT_NAME")== null ? "" : map.get("TMAS_DEPARTMENT_NAME").toString());
					    bean.setCedingCo(map.get("COMPANY")== null ? "" : map.get("COMPANY").toString());
					    bean.setTreatyNametype(map.get("RSK_TREATYID")== null ? "" : map.get("RSK_TREATYID").toString());
					    bean.setProposalNo(map.get("RSK_PROPOSAL_NUMBER")== null ? "" : map.get("RSK_PROPOSAL_NUMBER").toString());
					    bean.setUwYear(map.get("RSK_UWYEAR")== null ? "" : map.get("RSK_UWYEAR").toString());
					    bean.setLayerno(map.get("RSK_LAYER_NO")== null ? "" : map.get("RSK_LAYER_NO").toString());
					    bean.setInsDate(map.get("INS_DATE")== null ? "" : map.get("INS_DATE").toString());
					    bean.setExpDate(map.get("EXP_DATE")== null ? "" : map.get("EXP_DATE").toString());
					    bean.setBroker(map.get("BROKER")== null ? "" : map.get("BROKER").toString());
					    bean.setDepartmentId(map.get("RSK_DEPTID")== null ? "" : map.get("RSK_DEPTID").toString());
					    bean.setInsuredname(map.get("RSK_INSURED_NAME")== null ? "" : map.get("RSK_INSURED_NAME").toString());
					    bean.setBonusId(map.get("BONUS_TYPE")== null ? "" : map.get("BONUS_TYPE").toString());
					    if("LCB".equalsIgnoreCase(bean.getBonusId())){
					    	bean.setAcqBonusName("Low Claim Bonus");
						}
						else if("NCB".equalsIgnoreCase(bean.getBonusId())){
							bean.setAcqBonusName("No Claim Bonus");
						}
					    resList.add(bean);
				 }
			 }
			 response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
	     	}catch (Exception e) {
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			    return response;
			    }


	public String getBonusValue(GetFieldValuesReq bean, double ans) {
		String slideres ="0.00";
		List<Map<String,Object>> list;
		try{
			if(StringUtils.isNotBlank(bean.getBonusId()) && "LCB".equals(bean.getBonusId())){
			String slidequery = "GET_SILDE_VALUE_DEPT" ;
			String slideArgs[] = new String[7];
			slideArgs[0] = bean.getContNo();
			slideArgs[1] = bean.getProductId();
			slideArgs[2] = bean.getBranchCode();
			slideArgs[3] = String.valueOf(ans);
			slideArgs[4] = bean.getBonusId();
			slideArgs[5] = bean.getDepartmentId();
			slideArgs[6] = bean.getProposalNo();
			 list= queryImpl.selectList(slidequery, slideArgs);
			if (!CollectionUtils.isEmpty(list)) {
				slideres = list.get(0).get("LCB_PERCENTAGE") == null ? "" : list.get(0).get("LCB_PERCENTAGE").toString();
			}
			}else if(StringUtils.isNotBlank(bean.getBonusId()) && "NCB".equals(bean.getBonusId()) && ans==0.00){
				String slidequery = "GET_BONUS_VALUE" ;
				String slideArgs[] = new String[2];
				slideArgs[0] = bean.getContNo();
				slideArgs[1] = bean.getLayerno();
				list=queryImpl.selectList(slidequery, slideArgs);
				if (!CollectionUtils.isEmpty(list)) {
					slideres = list.get(0).get("RSK_NOCLAIMBONUS_PRCENT") == null ? "" : list.get(0).get("RSK_NOCLAIMBONUS_PRCENT").toString();
				}
			}
			
     	}catch (Exception e) {
				e.printStackTrace();
				
			}
		    return slideres;
		    }

	@Override
	public GetFieldValuesRes getFieldValues(GetFieldValuesReq req) {
		GetFieldValuesRes response = new GetFieldValuesRes();
		GetFieldValuesRes1 res = new GetFieldValuesRes1();
		String args[]=null;
		String prargs[]=null;
		String orargs[]=null;
		List<Map<String,Object>> comList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> premList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> OSList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>>finalTest=new ArrayList<Map<String,Object>>();
		
		List<String> claimout =new ArrayList<String>();
		List<String> claimRatio =new ArrayList<String>();
		List<String> total = new ArrayList<String>();
		List<String> pretotal = new ArrayList<String>();
		List<String> paiddate = new ArrayList<String>();
		List<String> bonusOC = new ArrayList<String>();
		List<String> bonusAdj = new ArrayList<String>();
		List<String> claimamt =new ArrayList<String>();
		List<String> premiumamt =new ArrayList<String>();
		
		double val =0;
		double curval =0;
		double bonus =0;
		double paid =0;
		double premiumPaid=0;
		double commission=0;
		String paidamount = "";
		String premAmt ="";
		String curId=""   ;
		String precurId="";  
		String query ="";
		String prquery ="";
		String quotaShare = "";
		String mdpremium ="";
		String adjustmentPremium = "";
		String portfolioin ="";
		String portfolioout = "";
		String comqs = "";
		String comsu = "";
		String claimoutSt = "";
		String bonusres="";
		String OSQuery="";
		DecimalFormat myFormatter = new DecimalFormat("#####.##");
		List<PreCurrencylistRes> preResList = new ArrayList<PreCurrencylistRes>();
		try{
			if (req.getPreCurrencylist().size() > 0) {
				 for (int j = 0; j < req.getPreCurrencylist().size(); j++) {
						total.add("0.00") ;
						 paiddate.add("0.00");
					 	 pretotal.add("0.00");
						 claimout.add("0.00");
						 claimamt.add("0.00");
						 premiumamt.add("0.00");
					 }
				
				 }
				query = "GET_BONUS_PAID_AMOUT";
				args=new String[5];
				args[0] = req.getContNo();
				args[1] = req.getLayerno();
				args[2] = req.getBranchCode();
				args[3] = req.getTransaction();
				args[4] = req.getProductId();
				prquery = "GET_BONUS_PREMIUM_DETAILS";
				prargs=new String[7];
				prargs[0] = req.getContNo();
				prargs[1] = req.getLayerno();
				prargs[2] = req.getBranchCode();
				prargs[3] = req.getContNo();
				prargs[4] = req.getLayerno();
				prargs[5] = req.getTransaction();
				prargs[6] = req.getProductId();
				OSQuery = "GET_BONUS_RESERVED_CLAIM_PAID";
				orargs=new String[6];
				orargs[0] = req.getContNo();
				orargs[1] = req.getLayerno();
				orargs[2] = req.getTransaction();
				orargs[3] = req.getContNo();
				orargs[4] = req.getLayerno();
				orargs[5] = req.getTransaction();
				if(StringUtils.isNotBlank(req.getTransactionNo())){
				prquery="SELECT * FROM ("+prquery+")where TRANSACTION_NO not in('"+req.getTransactionNo()+"')" ;
				}
			comList = queryImpl.selectList(query,args);
			premList = queryImpl.selectList(prquery,prargs);
			OSList = queryImpl.selectList(OSQuery,orargs);
			
			curval+=Double.parseDouble(StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare().replace(",", ""))+
			Double.parseDouble(StringUtils.isBlank(req.getMdpremium())?"0":req.getMdpremium().replace(",", ""))+
			Double.parseDouble(StringUtils.isBlank(req.getAdjustmentpremium())?"0":req.getAdjustmentpremium().replace(",", ""));
			List<PremListRes> premResList = new ArrayList<PremListRes>();
			if(premList.size()>0){
				for (int i = 0; i < premList.size(); i++) {
						Map<String, Object> map = premList.get(i);
						PremListRes premRes = new PremListRes();
						finalTest.add(premList.get(i));
						quotaShare = map.get("PREMIUM_QUOTASHARE_OC") == null ? "0.00" : map.get("PREMIUM_QUOTASHARE_OC").toString();
						mdpremium = map.get("M_DPREMIUM_OC") == null ? "0.00" : map.get("M_DPREMIUM_OC").toString();
						adjustmentPremium = map.get("ADJUSTMENT_PREMIUM_OC") == null ? "0.00" : map.get("ADJUSTMENT_PREMIUM_OC").toString();
						comqs=map.get("BONUS_OC")==null?"0.00":map.get("BONUS_OC").toString();
						quotaShare = quotaShare.replaceAll(",", "");
						portfolioin = portfolioin.replaceAll(",", "");
						portfolioout = portfolioout.replaceAll(",", "");
						comqs = comqs.replaceAll(",", "");
						comsu = comsu.replaceAll(",", "");
						curId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
						if (precurId!=null && precurId.equals(curId)) {
							val += Double.parseDouble(quotaShare) + Double.parseDouble(mdpremium) + Double.parseDouble(adjustmentPremium) ;
							bonus += Double.parseDouble(comqs);
						}
						else{
							val=0;
							bonus=0;
							val += Double.parseDouble(quotaShare) + Double.parseDouble(mdpremium) + Double.parseDouble(adjustmentPremium) ;
							bonus += Double.parseDouble(comqs);
						}
						if (req.getPreCurrencylist().size() > 0) {
						 for (int j = 0; j < req.getPreCurrencylist().size(); j++) {
							 	precurId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
								if (precurId.equals(req.getPreCurrencylist().get(j))) {
									
									paiddate.set(j,fm.formatter(Double.toString(bonus)));
									 pretotal.set(j,fm.formatter(Double.toString(val+curval)));
									 								}
							}
						}
					}
				}else{
					if ( req.getPreCurrencylist().size() > 0) {
						for (int j = 0; j <  req.getPreCurrencylist().size(); j++) {
							 pretotal.set(j,fm.formatter(Double.toString(val+curval)));
						}
					}
				}
			if(OSList.size()>0){
				for (int i = 0; i < OSList.size(); i++) {
						Map<String, Object> map = OSList.get(i);
						claimoutSt = map.get("OSLR").toString() == null ? "0.00" : map.get("OSLR").toString();
						curId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
						claimoutSt = claimoutSt.replaceAll(",", "");
						if (precurId!=null && precurId.equals(curId)) {
								commission += Double.parseDouble(claimoutSt) ;
						}
						else{
							commission=0;
							commission += Double.parseDouble(claimoutSt);
						}
						if (req.getPreCurrencylist().size() > 0) {
							 for (int j = 0; j < req.getPreCurrencylist().size(); j++) {
								 	precurId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
									if (precurId.equals(req.getPreCurrencylist().get(j))) {
										claimout.set(j,fm.formatter(Double.toString(commission)));
									}
								}
							}
				}
			}
			if(comList.size()>0) {
				for (int i = 0; i < comList.size(); i++) {
					
					Map<String, Object> map = comList.get(i);
					finalTest.add(comList.get(i));
					curId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
					
					if (precurId!=null && precurId.equals(curId)) {
						paidamount = map.get("PAID_AMOUNT_OC").toString() == null ? "0.00" : map.get("PAID_AMOUNT_OC").toString();
						paid += Double.parseDouble(paidamount.replaceAll(",",""));
					}else{
						paid =0;
						paidamount = map.get("PAID_AMOUNT_OC").toString() == null ? "0.00" : map.get("PAID_AMOUNT_OC").toString();
						paid += Double.parseDouble(paidamount.replaceAll(",",""));
					}
					precurId = curId;
					if (req.getPreCurrencylist().size() > 0) {
						for (int j = 0; j < req.getPreCurrencylist().size(); j++) {
							curId = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
							if (curId.equals(req.getPreCurrencylist().get(j))) {
								paid = paid+Double.parseDouble(premiumamt.get(j).replaceAll(",",""));
								total.set(j,fm.formatter(Double.toString(paid)));
							}
						}
					}
				}
			}
			else{
				if ( req.getPreCurrencylist().size() > 0) {
					for (int j = 0; j <  req.getPreCurrencylist().size(); j++) {
						total.set(j,fm.formatter(premiumamt.get(j).replaceAll(",","")));
					}
				}
			}
			
				for (int i = 0; i < req.getPreCurrencylist().size(); i++) {
					double ans = 0.00;
					double slideresult = 0.00;
					String prtotal = pretotal.get(i).replaceAll(",", "");
					String claimou = claimout.get(i).replaceAll(",", "");
					String tot = total.get(i).replaceAll(",", "");
					if (Double.parseDouble(prtotal) > 0) {
						ans = (Double.parseDouble(tot) + Double.parseDouble(claimou)) / Double.parseDouble(prtotal) * 100;
					}
					claimRatio.add(fm.formatter(Double.toString(ans)));

					bonusres = getBonusValue(req, ans);
					bonusres = bonusres.replaceAll(",", "");
					if (Double.parseDouble(prtotal) > 0) {
						slideresult = Double.parseDouble(prtotal) * Double.parseDouble(bonusres) / 100;
					}

					bonusOC.add(fm.formatter(Double.toString(slideresult)));
					String sliderest = bonusOC.get(i).replaceAll(",", "");
					String paiddat = paiddate.get(i).replaceAll(",", "");
					double slideans = Double.parseDouble(sliderest) - Double.parseDouble(paiddat);
					bonusAdj.add(fm.formatter(Double.toString(slideans)));
				}
				res.setPremiumOC(pretotal);
				res.setClaimPaidOC(total);
				res.setClaimRatioOC(claimRatio);
				res.setClaimOutStandingOC(claimout);
				res.setBonusOC(bonusOC);
				res.setBonusPaidOCTillDate(paiddate);
				res.setBonusAdjOC(bonusAdj);
			//	res.setPremiumFinallist2(finalTest);
			    if(StringUtils.isBlank(req.getFlag())){
			    	res.setManualPremiumOC(pretotal);
			    	res.setManualclaimPaidOC(total);
			    	res.setManualclaimRatioOC(claimRatio);
			    	res.setManualclaimOutStandingOC(claimout);
			    }
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

	@Override
	public BonusdetailsRes bonusdetails(BonusdetailsReq bean) {
		BonusdetailsRes response = new BonusdetailsRes();
		BonusdetailsRes1 res = new BonusdetailsRes1();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
	    String cur = "";
	    List<String> exhRate = new ArrayList<String>();
	    List<String> total = new ArrayList<String>();
	    List<String> curId = new ArrayList<String>();
			List<String> curName = new ArrayList<String>();
	    String currency ="";
	    String currenyName= "";
	        try {
				String args[] = null;
				String query = "";
					query = "GET_BONUS_CURRENY_DET";
					args = new String[9];
					args[0] = bean.getContNo();
					args[1] = bean.getLayerno();
					args[2] = bean.getTransaction();
					args[3] = bean.getBranchCode();
					args[4] = bean.getContNo();
					args[5] = bean.getLayerno();
					args[6] = bean.getTransaction();
					args[7] = bean.getBranchCode();
					args[8] = bean.getBranchCode();
					list = queryImpl.selectList(query, args);
				
				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map = list.get(i);
						currency = map.get("SHORT_NAME").toString() == null ? "" : map.get("SHORT_NAME").toString();
						cur = map.get("CURRENCY_ID").toString() == null ? "" : map.get("CURRENCY_ID").toString();
						if (StringUtils.isNotBlank(bean.getMode()) && "bonus".equalsIgnoreCase(bean.getMode())) {
							com.maan.insurance.model.res.DropDown.GetCommonValueRes common = dropDowmImpl.GetExchangeRate(cur, bean.getTransaction(), bean.getCountryId(), bean.getBranchCode());
							String rate = common.getCommonResponse();
							 common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(), bean.getTransaction(), bean.getCountryId(), bean.getBranchCode());
							String gridexchange= common.getCommonResponse();
							if(rate != null && rate != "0")
							{
							double exchange = Double.parseDouble(gridexchange) / Double.parseDouble(rate);
							exhRate.add(fm.formattereight(Double.toString(exchange)));
							}
						}
						curId.add(cur);
						curName.add(currency);
					}
				}else if(list.size()==0 && (StringUtils.isNotBlank(bean.getType())&&bean.getType().equals("Y"))){
					curId.add(bean.getCurrencyId());
					query= "GET_CURRENCY_NAME";
					List<Map<String,Object>> list2 =	queryImpl.selectList(query, new String[]{bean.getBranchCode(),bean.getCurrencyId()});
					if (!CollectionUtils.isEmpty(list2)) {
						currenyName= list2.get(0).get("SHORT_NAME") == null ? ""
								: list2.get(0).get("SHORT_NAME").toString();
					}
					curName.add(currenyName);
					exhRate.add(fm.formattereight(bean.getExchRate()));
					
					 Map<String,Object> doubleMap = new HashMap<String,Object>();
					doubleMap.put("SHORT_NAME",currenyName);
					list1.add(doubleMap);
				}
				if (StringUtils.isNotBlank(bean.getMode()) && ("bonus".equalsIgnoreCase(bean.getMode()) ||  "bonusView".equalsIgnoreCase(bean.getMode()))  && bean.getPremiumOC() == null) {
					res.setExchRatePrem(exhRate);
				if (list.size() == 1) {
					if (bean.getCurrencyName().equalsIgnoreCase(currency.trim())) {
						res.setGridShow("N");
					} else {
						res.setGridShow("Y");
					}
				} else if (list.size() > 1) {
					res.setGridShow("Y");
				}else if(list1.size() == 1){
					list=list1;
					res.setGridShow("N");
				}
			}else if(list.size()==0){
				list=list1;
			}
				res.setCurrencyShortName(curName);
				res.setPreCurrencylist(curId);
				List<BonusRes> res1List = new ArrayList<BonusRes>();
				if(list.size()>0) {
					for(int i =0; i<list.size(); i++) {
						 Map<String,Object>	 map=list.get(i);
						 BonusRes res1 = new BonusRes();							
						 res1.setShortName(map.get("SHORT_NAME")==null?"":map.get("SHORT_NAME").toString());
						 res1.setCurrencyId(map.get("CURRENCY_ID")==null?"":map.get("CURRENCY_ID").toString());
						 res1List.add(res1);		
						 }
				}
				res.setBonusRes(res1List);
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

	@Override
	public CommonResponse addFieldValue(AddFieldValueReq bean) {
		CommonResponse response = new CommonResponse();
		String name="";
		String premium="";
		String cliam="";
		String cliamAMT="";
		String paid ="";
		String acqCost="";
		String claimout="";
		String bonusTill="";
		String total="";
		String proComm="";
		String val1[];
		boolean flag=true;
		String[] val=bean.getBonusExchangeRate().split(",");
		List<Map<String,Object>> test= new ArrayList<Map<String,Object>>();
	try{
			Map<String,Object> list = new HashMap<String,Object>();
				if (bean.getPremiumFinallist2().size() > 0) {
					for (int i = 0; i < bean.getPremiumFinallist2().size(); i++) {
						Map<String, Object> map = bean.getPremiumFinallist2().get(i);
						flag = true;
						name = map.get("SHORT_NAME") == null ? "" : map.get("SHORT_NAME").toString();
						premium = map.get("PremiumOC")== null ? "0.00" : map.get("PremiumOC").toString();
						cliam = map.get("PAID_AMOUNT_OC")== null ? "0.00" : map.get("PAID_AMOUNT_OC").toString();
						claimout = map.get("OSLR") == null ? "0.00" : map.get("OSLR").toString();
						bonusTill = map.get("BONUS_OC") == null ? "0.00" : map.get("BONUS_OC").toString();
						
						total = fm.formatter(cliam);
						if (bean.getCurrencyShortName().size() > 0) {
							for (int j = 0; j < bean.getCurrencyShortName().size(); j++) {
								if(name.equalsIgnoreCase(bean.getCurrencyShortName().get(j).getCurrencyShortName())){
									
									val1 = val[j].split("~");
									if(val1[0].equalsIgnoreCase(name) && flag){
										map.put("EXCHG_RATE",fm.formattereight(val1[1]));
										premium =fm.formatter(Double.toString(Double.parseDouble(premium) * Double.parseDouble(val1[1])));
										cliam =fm.formatter(Double.toString(Double.parseDouble(cliam) * Double.parseDouble(val1[1])));
										claimout =fm.formatter(Double.toString(Double.parseDouble(claimout) * Double.parseDouble(val1[1])));
										bonusTill=fm.formatter(Double.toString(Double.parseDouble(bonusTill) * Double.parseDouble(val1[1])));
										map.put("PREMIUM_DC",premium);
										map.put("CLAIM_DC",cliam);
										map.put("CLAIMOUT_DC",claimout);
										map.put("BONUS_DC",bonusTill);
										map.put("CLAIM_PREMIUM_VAL",total);
										flag = false;
									}

								}
							}
							}
						test.add(map);
			}
		}
		bean.setPremiumFinallist2(test);
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
	public GetMandDInstallmentsRes getMandDInstallments(String contNo, String layerNo) {
		GetMandDInstallmentsRes response = new GetMandDInstallmentsRes();
		 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		 List<GetMandDInstallmentsRes1> resList = new ArrayList<GetMandDInstallmentsRes1>();
		try{
			String query= "premium.select.mdInstallments";
			 list=  queryImpl.selectList(query, new String[]{contNo,layerNo});
			 if(list!=null &&list.size()>0){
				 for(int i=0; i<list.size(); i++) {
					 GetMandDInstallmentsRes1 res = new GetMandDInstallmentsRes1();
					Map<String,Object>map=list.get(i);
					res.setInstallmentNo(map.get("INSTALMENT_NUMBER")==null?"":map.get("INSTALMENT_NUMBER").toString());
					resList.add(res);
					}}
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
}
