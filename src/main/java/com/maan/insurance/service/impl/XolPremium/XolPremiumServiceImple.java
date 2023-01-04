package com.maan.insurance.service.impl.XolPremium;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.GetRetroContractsReq;
import com.maan.insurance.model.req.xolPremium.MdInstallmentDatesReq;
import com.maan.insurance.model.req.xolPremium.PremiumEditReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes1;
import com.maan.insurance.model.res.facPremium.GetAllocatedListResponse;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes1;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes1;
import com.maan.insurance.model.res.premium.GetRetroContractsRes;
import com.maan.insurance.model.res.xolPremium.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes1;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes1;
import com.maan.insurance.model.res.xolPremium.GetPreListRes;
import com.maan.insurance.model.res.xolPremium.GetPreListRes1;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes1;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes1;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes1;
import com.maan.insurance.model.res.xolPremium.PremiumEditResponse;
import com.maan.insurance.model.res.xolPremium.PremiumInsertRes;
import com.maan.insurance.model.res.xolPremium.premiumInsertMethodRes;
import com.maan.insurance.service.XolPremium.XolPremiumService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
@Service
public class XolPremiumServiceImple implements XolPremiumService{
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;
	

	public String getRPPremiumOC(String contractNo, String layerNo,String productId){
	List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
	String premiumOC ="";
	try {
		String query="";
		if("3".equalsIgnoreCase(productId)){
			query="premium.select.RPPremiumOC";
		}else{
		 query="XOL_PREMIUM_SELECT_RPPREMIUMOC";
		}
		 list = queryImpl.selectList(query, new String [] {contractNo,layerNo});
		 if (!CollectionUtils.isEmpty(list)) {
				premiumOC = list.get(0).get("REC_PREMIUM_OC") == null ? "": list.get(0).get("REC_PREMIUM_OC").toString();
			} 
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return premiumOC;
}

	public List<Map<String, Object>> getSPRetroList(String contNo){
     	List<Map<String, Object>> list= new ArrayList<Map<String,Object>>();
     	try{
					String query="premium.select.getTreatySPRetro";
					list=queryImpl.selectList(query,new String[] {contNo});	
				}catch(Exception e){
					e.printStackTrace();
				}
				return list;
	}
public List<Map<String, Object>> getRetroContracts(String proposalNo, String noOfRetro) {
	List<Map<String, Object>> list= new ArrayList<Map<String,Object>>();
	
	try{
		String query="premium.select.insDetails";
		String[] args = new String [2];
		args[0] = proposalNo;
		args[1] = noOfRetro;
		list=queryImpl.selectList(query,args);
	}catch(Exception e){
		e.printStackTrace();
	}
	return list;
}

public String getSumOfShareSign(String contractNo){
	List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
	int shareSign = 0;
	String sumOfShareSign = "";
	try {
		String query ="premium.select.getNoRetroCess";
		 list = queryImpl.selectList(query, new String [] {contractNo});
		 if (!CollectionUtils.isEmpty(list)) {
			 shareSign = Integer.valueOf(list.get(0).get("RETRO_CESSIONARIES") == null ? "": list.get(0).get("RETRO_CESSIONARIES").toString())-1;
			} 
		 query="premium.select.getSumOfShareSign";
		list = queryImpl.selectList(query, new String [] {contractNo,String.valueOf(shareSign)});
		 if (!CollectionUtils.isEmpty(list)) {
			 sumOfShareSign = list.get(0).get("SHARE_SIGNED") == null ? "": list.get(0).get("SHARE_SIGNED").toString();
			}
	} catch (Exception e) {
		e.printStackTrace();
	}
	 return sumOfShareSign;
}


@Override
public GetPremiumedListRes getPremiumedList(GetPremiumedListReq beanObj) {
	GetPremiumedListRes response = new GetPremiumedListRes();
	List<GetPremiumedListRes1> finalList = new ArrayList<GetPremiumedListRes1>();
	String query="";
	int allocationstatus=0;
	int retroPrclStatus=0;
    String[] args=null;
    try {
    args=new String[5];
	args[0]=beanObj.getContNo();
	args[1]=beanObj.getBranchCode();
	args[2]=beanObj.getContNo();
	args[3]=beanObj.getLayerNo();
	args[4]=beanObj.getLayerNo();
	if("Main".equalsIgnoreCase(beanObj.getType())){
    	if("3".equalsIgnoreCase(beanObj.getProductId())){
    	query= "premium.select.PremiumedList1_added";
    	}else{
    	query= "premium.select.retroPremiumedList1_added";
    	}
	}else{
	    	query= "XOL_PREMIUM_LIST_TEMP";
	}
	List<Map<String,Object>> list= queryImpl.selectList(query, args);
	if(list.size()>0 && list != null)
	for(int i=0 ; i<list.size() ; i++) {
		Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
		GetPremiumedListRes1 tempBean = new GetPremiumedListRes1();
		tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
		tempBean.setContNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
		tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
		tempBean.setBroker(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
		tempBean.setLayerno(tempMap.get("RSK_LAYER_NO")==null?"":tempMap.get("RSK_LAYER_NO").toString());
		tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
		tempBean.setAccountPeriod(tempMap.get("ACC_PER")==null?"":tempMap.get("ACC_PER").toString());
		tempBean.setAccountPeriod(tempMap.get("INS_DETAIL")==null?"":tempMap.get("INS_DETAIL").toString());
		tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
		tempBean.setRequestNo(tempMap.get("REQUEST_NO")==null?"":tempMap.get("REQUEST_NO").toString());
		if(i==0)
			tempBean.setEndtYN("No");
		else
			tempBean.setEndtYN("Yes");
		if(Double.parseDouble(tempMap.get("ALLOC_AMT").toString())!=0)
			tempBean.setEndtYN("Yes");
		tempBean.setInceptionDate(tempMap.get("INS_DATE")==null?"":tempMap.get("INS_DATE").toString());
		tempBean.setStatementDate(tempMap.get("STATEMENT_DATE")==null?"":tempMap.get("STATEMENT_DATE").toString());
		tempBean.setMovementYN(tempMap.get("MOVEMENT_YN")==null?"":tempMap.get("MOVEMENT_YN").toString());
		tempBean.setTransDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
		if((StringUtils.isNotBlank(beanObj.getOpstartDate()))&& (StringUtils.isNotBlank(beanObj.getOpendDate()))){
			if(dropDowmImpl.Validatethree(beanObj.getBranchCode(), tempBean.getTransDate())==0){
				tempBean.setTransOpenperiodStatus("N");
			}else
			{
				tempBean.setTransOpenperiodStatus("Y");
			}
			}
		List<Map<String,Object>> list1= queryImpl.selectList("premium.select.allocatedYN",new String[]{tempBean.getContNo(),tempBean.getTransactionNo(),tempBean.getLayerno()});
		if (!CollectionUtils.isEmpty(list1)) {
			tempBean.setAllocatedYN(list1.get(0).get("ALLOCATED_YN") == null ? "": list1.get(0).get("ALLOCATED_YN").toString());
		}
		tempBean.setProductId("3");
		int count=dropDowmImpl.Validatethree(beanObj.getBranchCode(), tempBean.getTransDate());
		String args2[]=new String[1];
		args2[0]=tempBean.getTransactionNo();
		query="allocation.status";
		list1= queryImpl.selectList(query,args2);
		if (!CollectionUtils.isEmpty(list1)) {
			allocationstatus = Integer.valueOf(list1.get(0).get("COUNT") == null ? "": list1.get(0).get("COUNT").toString());
		}
		query= "retro.status";
		list1= queryImpl.selectList(query,args2);
		if (!CollectionUtils.isEmpty(list1)) {
			retroPrclStatus = Integer.valueOf(list1.get(0).get("COUNT") == null ? "": list1.get(0).get("COUNT").toString());
		}
		
		int retroPrclStatus1=0;
		if(retroPrclStatus!=0){
		query= "retro.status1";
		list1= queryImpl.selectList(query,args2);
		if (!CollectionUtils.isEmpty(list1)) {
			retroPrclStatus1 = Integer.valueOf(list1.get(0).get("COUNT") == null ? "": list1.get(0).get("COUNT").toString());
		}
		}
		
		if(count!=0 && allocationstatus ==0 && retroPrclStatus1 ==0 ){
			tempBean.setDeleteStatus("Y");
		}
		finalList.add(tempBean);
	}
		response.setCommonResponse(finalList);
		response.setMessage("Success");
		response.setIsError(false);
    } catch (Exception e) {
    		e.printStackTrace();
    		response.setMessage("Failed");
    		response.setIsError(true);
}
return response;
}


@Override
public CommonSaveRes getContractPremium(String contractNo, String layerNo) {
	CommonSaveRes response = new CommonSaveRes();
	String premium="";
	try{
		String query="SELECT_RSK_SUBJ_PREMIUM_OC";
		List<Map<String,Object>> list= queryImpl.selectList(query,new String[]{contractNo,layerNo});
		if (!CollectionUtils.isEmpty(list)) {
			premium = list.get(0).get("RSK_SUBJ_PREMIUM_OC") == null ? "": list.get(0).get("RSK_SUBJ_PREMIUM_OC").toString();
		}
		response.setResponse(premium);
		 response.setMessage("Success");
			response.setIsError(false);
	} catch (Exception e) {
		e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	}
	 return response;
}


@Override
public CommonSaveRes getPreviousPremium(String contractNo) {
	CommonSaveRes response = new CommonSaveRes();
	String premium="";
	try{
		String query="select_Previous_premium";
		List<Map<String,Object>> list= queryImpl.selectList(query,new String[]{contractNo});
		if (!CollectionUtils.isEmpty(list)) {
			premium = list.get(0).get("SUM") == null ? "": list.get(0).get("SUM").toString();
		}
		response.setResponse(premium);
		 response.setMessage("Success");
			response.setIsError(false);
	} catch (Exception e) {
		e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	}
	 return response;
}


@Override
public MdInstallmentDatesRes mdInstallmentDates(MdInstallmentDatesReq req) {
	MdInstallmentDatesRes response = new MdInstallmentDatesRes();
	List<MdInstallmentDatesRes1> resList = new ArrayList<MdInstallmentDatesRes1>();
	String query="";
	 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    try{
    	 String[] args=null;
    	 if("3".equalsIgnoreCase(req.getProductId())){
    		args=new String[6];
    	 }else{
    		args=new String[4];
    	 }
		    args[0]=req.getContNo();
		    args[1]=req.getLayerno();
		    args[2]=req.getContNo();
		    args[3]=req.getLayerno();
	    if("3".equalsIgnoreCase(req.getProductId())){
	    	args[4]=req.getContNo();
	    	args[5]=req.getLayerno();
	    }
	    if("3".equalsIgnoreCase(req.getProductId())){
	    	query= "PREMIUM_MND_INS_LIST";
	    	 
	    }else{
	    	query= "premium.select.mdInstalmentList";
	    }
	    list= queryImpl.selectList(query, args);		 
	    Map<String,Object> tempMap1 = new HashMap<String, Object>();
	    Map<String,Object> tempMap2 = new HashMap<String, Object>();
	    Map<String,Object> tempMap3 = new HashMap<String, Object>();
	    Map<String,Object> tempMap4 = new HashMap<String, Object>();
	    if(list.size()==0)
	    {
	    String count="";
	    if("RI02".equalsIgnoreCase(req.getSourceId())){
	    query = "GET_GNPI_COUNT_PRE";
	     args=new String[3];
	     args[0]=req.getContNo();
	     args[1]=req.getLayerno();
	     args[2]="GNPI";
	     List<Map<String,Object>>   list1= queryImpl.selectList(query,args);
	     if (!CollectionUtils.isEmpty(list1)) {
	    	 count = list1.get(0).get("COUNT") == null ? "": list1.get(0).get("COUNT").toString();
			}
	    }
	    tempMap1.put("KEY1","RP");	    
	    tempMap1.put("VALUE","Reinstatement Premium");
	    list.add(tempMap1);	
	    if("RI02".equalsIgnoreCase(req.getSourceId()) && !"0".equalsIgnoreCase(count)){
		    tempMap2.put("KEY1","AP");	    
		    tempMap2.put("VALUE","Adjustment Premium");	
		    list.add(tempMap2);	
	    }else if("RI01".equalsIgnoreCase(req.getSourceId())){
	    	tempMap2.put("KEY1","AP");	    
		    tempMap2.put("VALUE","Adjustment Premium");	
		    list.add(tempMap2);	
	    }
	    tempMap3.put("KEY1","RTP");	    
	    tempMap3.put("VALUE","Return Premium");
	    list.add(tempMap3);
	    tempMap4.put("KEY1","RVP");	    
	    tempMap4.put("VALUE","Reversal Premium");
	    list.add(tempMap4);
	    }
	    else
	    {
	    tempMap1.put("KEY1","RP");	    
		tempMap1.put("VALUE","Reinstatement Premium");	
		 list.add(tempMap1);
		tempMap3.put("KEY1","RTP");	    
		tempMap3.put("VALUE","Return Premium");
		 list.add(tempMap3);
		tempMap4.put("KEY1","RVP");	    
		tempMap4.put("VALUE","Reversal Premium");
		 list.add(tempMap4);
	    }
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
public ContractDetailsRes contractDetails(ContractDetailsReq req) {
	ContractDetailsRes response = new ContractDetailsRes();
	ContractDetailsRes1 bean = new ContractDetailsRes1();
	 String query="";
	 String[] args=null;
	 List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	 try {
		 query="premium.select.treatyContDet1_xolLayerNo2";
		 args =new String[11];
		 args[0] = req.getProductId();
		 args[1] = req.getContNo();
		 args[2] = req.getLayerno();
		 args[3] = req.getBranchCode();
		 args[4] = req.getBranchCode();
		 args[5] = req.getProductId();
		 args[6] = req.getBranchCode();
		 args[7] = req.getBranchCode();
		 args[8] = req.getContNo();
		 args[9] = req.getLayerno();
		 args[10] = req.getBranchCode();
		 list = queryImpl.selectList(query, args);
			if(list!=null && list.size()>0) {
				Map<String,Object> contDet=(Map<String,Object>) list.get(0);
					bean.setContNo(contDet.get("RSK_CONTRACT_NO")==null?"":contDet.get("RSK_CONTRACT_NO").toString());
					bean.setAmendId(contDet.get("RSK_ENDORSEMENT_NO")==null?"":contDet.get("RSK_ENDORSEMENT_NO").toString());
					bean.setProfitCenter(contDet.get("TMAS_PFC_NAME")==null?"":contDet.get("TMAS_PFC_NAME").toString());
					bean.setSubProfitcenter(contDet.get("TMAS_SPFC_NAME")==null?"":contDet.get("TMAS_SPFC_NAME").toString());
					bean.setCedingCo(contDet.get("COMPANY")==null?"":contDet.get("COMPANY").toString());
					bean.setBroker(contDet.get("BROKER")==null?"":contDet.get("BROKER").toString());
					bean.setTreatyNametype(contDet.get("RSK_TREATYID")==null?"":contDet.get("RSK_TREATYID").toString());					
					bean.setProposalNo(contDet.get("RSK_PROPOSAL_NUMBER")==null?"":contDet.get("RSK_PROPOSAL_NUMBER").toString());
					bean.setUwYear(contDet.get("RSK_UWYEAR")==null?"":contDet.get("RSK_UWYEAR").toString());
					bean.setLayerno(contDet.get("RSK_LAYER_NO")==null?"":contDet.get("RSK_LAYER_NO").toString());
					bean.setInsDate(contDet.get("INS_DATE")==null?"":contDet.get("INS_DATE").toString());
					bean.setExpDate(contDet.get("EXP_DATE")==null?"":contDet.get("EXP_DATE").toString());
					bean.setMonth(contDet.get("MONTH")==null?"":contDet.get("MONTH").toString());
					bean.setBaseCurrencyId(contDet.get("RSK_ORIGINAL_CURR")==null?"":contDet.get("RSK_ORIGINAL_CURR").toString());
					bean.setBaseCurrencyName(contDet.get("CURRENCY_NAME")==null?"":contDet.get("CURRENCY_NAME").toString());
					bean.setPolicyBranch(contDet.get("TMAS_POL_BRANCH_NAME")==null?"":contDet.get("TMAS_POL_BRANCH_NAME").toString());
					bean.setAddress(contDet.get("ADDRESS")==null?"":contDet.get("ADDRESS").toString());
					bean.setDepartmentId(contDet.get("RSK_DEPTID")==null?"":contDet.get("RSK_DEPTID").toString());
					bean.setDepartmentName(contDet.get("TMAS_DEPARTMENT_NAME")==null?"":contDet.get("TMAS_DEPARTMENT_NAME").toString());
					bean.setAcceptenceDate(contDet.get("RSK_ACCOUNT_DATE")==null?"":contDet.get("RSK_ACCOUNT_DATE").toString());
				}
			if(list!=null && list.size()>0)
				bean.setSaveFlag("true");
				args=new String[2];
				args[0] = bean.getProposalNo();
				args[1] = bean.getProposalNo();
				query= "premium.select.commissionDetails";
				 list = queryImpl.selectList(query, args);
				 if(list!=null && list.size()>0) {
				 Map<String,Object> commission=(Map<String,Object>) list.get(0);
						bean.setCommissionview(commission.get("RSK_COMM_QUOTASHARE")==null?"":commission.get("RSK_COMM_QUOTASHARE").toString());
						bean.setPremiumReserveview(commission.get("RSK_PREMIUM_RESERVE")==null?"":commission.get("RSK_PREMIUM_RESERVE").toString());
						bean.setLossreserveview(commission.get("RSK_LOSS_RESERVE")==null?"":commission.get("RSK_LOSS_RESERVE").toString());
						bean.setProfitCommYN(commission.get("RSK_PROFIT_COMM")==null?"":commission.get("RSK_PROFIT_COMM").toString());
						bean.setCommissionSurbview(commission.get("RSK_COMM_SURPLUS")==null?"":commission.get("RSK_COMM_SURPLUS").toString());
						bean.setOverRiderview(commission.get("RSK_OVERRIDER_PERC")==null?"":commission.get("RSK_OVERRIDER_PERC").toString());
						bean.setBrokerageview(commission.get("RSK_BROKERAGE")==null?"":commission.get("RSK_BROKERAGE").toString());
						bean.setTaxview(commission.get("RSK_TAX")==null?"":fm.formatter(commission.get("RSK_TAX").toString()));
						bean.setOtherCostView(commission.get("RSK_OTHER_COST")==null?"":fm.formatter(commission.get("RSK_OTHER_COST").toString()));
				 }
				args[0] = bean.getProposalNo();
				args[1] = bean.getProposalNo();
				query= "premium.select.treatyXOLfacProposalDetails";
				 list = queryImpl.selectList(query, args);
				 if(list!=null && list.size()>0) {
					 Map<String,Object> proposalDetails=(Map<String,Object>) list.get(0);
						bean.setShareSigned(proposalDetails.get("RSK_SHARE_SIGNED")==null?"0":proposalDetails.get("RSK_SHARE_SIGNED").toString());
						String mnd = proposalDetails.get("RSK_MD_PREM_OS_OC")==null?"0":proposalDetails.get("RSK_MD_PREM_OS_OC").toString();
						String eps = (proposalDetails.get("RSK_EPI_OSOF_OC")==null?"0":proposalDetails.get("RSK_EPI_OSOF_OC").toString());
						bean.setRdsExchageRate(proposalDetails.get("RSK_EXCHANGE_RATE")==null?"":proposalDetails.get("RSK_EXCHANGE_RATE").toString());
						if(bean.getRdsExchageRate() != "0" && bean.getRdsExchageRate() != null) {
						double val= Double.parseDouble(eps)/Double.parseDouble(bean.getRdsExchageRate());
						double mndval= Double.parseDouble(mnd)/Double.parseDouble(bean.getRdsExchageRate());
						bean.setEPIourshareview(fm.formatter(Double.toString(val)));
						bean.setMdpremiumview(fm.formatter(Double.toString(mndval)));
						}
						bean.setAdjustmentpremiumtemp(proposalDetails.get("ADJ_PRE")==null?"":proposalDetails.get("ADJ_PRE").toString());
					}
	 query= "premium.select.currecy.name";
	 list = queryImpl.selectList(query, new String[]{req.getBranchCode()});
	 if(!CollectionUtils.isEmpty(list)) {
		 	bean.setCurrencyName(list.get(0).get("COUNTRY_SHORT_NAME") == null ? "": list.get(0).get("COUNTRY_SHORT_NAME").toString());
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
	public GetPreListRes getPreList(String contNo, String layerNo) {
	GetPreListRes response = new GetPreListRes();
	GetPreListRes1 bean = new GetPreListRes1();
	String query="";
	String[] args = null;
	args = new String[2];
	args[0] = contNo;
	args[1] = layerNo;
	try {
	query= "premium.select.xolPreList";
	 List<Map<String,Object>> list= queryImpl.selectList(query, args);
	 if(list!=null && list.size()>0) {
		 Map<String,Object> preList=(Map<String,Object>) list.get(0);
			bean.setContNo(preList.get("CONTRACT_NO")==null?"":preList.get("CONTRACT_NO").toString());
			bean.setDepartmentName(preList.get("TMAS_DEPARTMENT_NAME")==null?"":preList.get("TMAS_DEPARTMENT_NAME").toString());
			bean.setUwYear(preList.get("UW_YEAR")==null?"":preList.get("UW_YEAR").toString());
			bean.setCedingCompanyName(preList.get("COMPANY_NAME")==null?"":preList.get("COMPANY_NAME").toString());
			bean.setLayerno(preList.get("LAYER_NO")==null?"":preList.get("LAYER_NO").toString());
			bean.setBrokername(preList.get("FIRST_NAME")==null?"":preList.get("FIRST_NAME").toString());
		}
	 bean.setSaveFlag("true");
	if(StringUtils.isNotBlank(contNo)){
		 list = queryImpl.selectList("select_CEASE_STATUS",  new String[]{contNo,layerNo});
		 if(!CollectionUtils.isEmpty(list)) {
			 	bean.setCeaseStatus(list.get(0).get("CEASE_STATUS") == null ? "": list.get(0).get("CEASE_STATUS").toString());
			}
		}response.setCommonResponse(bean);
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
		PremiumEditResponse res1 = new PremiumEditResponse();
		 String query="";
		 List<PremiumEditRes1> resList = new ArrayList<PremiumEditRes1>();
		 List<Map<String,Object>> transList=new ArrayList<Map<String,Object>>();
		 try {
				String[] args = new String[2];
				args[0] = req.getContNo();
				if("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId())){
					query= "XOL_PREMIUM_EDIT_TEMP";
					args[1] = req.getRequestNo();
				}else{
					args[1] = req.getTransactionNo();
					if("3".equalsIgnoreCase(req.getProductId())){
					query="premium.select.treetyXOLPremiumEdit";
					}
					else{
						query= "premium.select.retrotreetyXOLPremiumEdit";
					}
				}
				if( "transEdit".equalsIgnoreCase(req.getMode())){
					args[0] = req.getContNo();
					args[1] = req.getTransDropDownVal();
				
					transList= queryImpl.selectList(query, args);
					 if(transList.size()>0)
					 {
						 for(int i=0;i<transList.size();i++){
							 PremiumEditRes1 bean = new PremiumEditRes1();
							 Map<String,Object>	 editPremium=transList.get(i);
							 bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
								bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
								if(null==editPremium.get("EXCHANGE_RATE")){
									GetCommonValueRes common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
									bean.setExchRate(common.getCommonResponse());
								}
								else{
								bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString()));
								}
								bean.setBrokerage((editPremium.get("BROKERAGE_AMT_OC")==null?"":editPremium.get("BROKERAGE_AMT_OC").toString()));
								bean.setBrokerage((getMultipleVal(bean.getBrokerage())));
								bean.setTax((editPremium.get("TAX_AMT_OC")==null?"":editPremium.get("TAX_AMT_OC").toString()));
								bean.setTax((getMultipleVal(bean.getTax())));
								bean.setPremiumQuotaShare(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString());
								bean.setPremiumQuotaShare((getMultipleVal(bean.getPremiumQuotaShare())));
								bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
								bean.setCommissionQuotaShare((getMultipleVal(bean.getCommissionQuotaShare())));
								bean.setPremiumSurplus(editPremium.get("PREMIUM_SURPLUS_OC")==null?"":editPremium.get("PREMIUM_SURPLUS_OC").toString());
								bean.setPremiumSurplus((getMultipleVal(bean.getPremiumSurplus())));
								bean.setCommissionSurplus(editPremium.get("COMMISSION_SURPLUS_OC")==null?"":editPremium.get("COMMISSION_SURPLUS_OC").toString());
								bean.setCommissionSurplus((getMultipleVal(bean.getCommissionSurplus())));
								bean.setPremiumportifolioIn(editPremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
								bean.setPremiumportifolioIn((getMultipleVal(bean.getPremiumportifolioIn())));
								bean.setCliamPortfolioin(editPremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editPremium.get("CLAIM_PORTFOLIOIN_OC").toString());
								bean.setCliamPortfolioin((getMultipleVal(bean.getCliamPortfolioin())));
								bean.setPremiumportifolioout(editPremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
								bean.setPremiumportifolioout((getMultipleVal(bean.getPremiumportifolioout())));
								bean.setLossReserveReleased(editPremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editPremium.get("LOSS_RESERVE_RELEASED_OC").toString());
								bean.setLossReserveReleased((getMultipleVal(bean.getLossReserveReleased())));
								bean.setPremiumReserveQuotaShare(editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
								bean.setPremiumReserveQuotaShare((getMultipleVal(bean.getPremiumReserveQuotaShare())));
								bean.setCashLossCredit(editPremium.get("CASH_LOSS_CREDIT_OC")==null?"":editPremium.get("CASH_LOSS_CREDIT_OC").toString());
								bean.setCashLossCredit((getMultipleVal(bean.getCashLossCredit())));
								bean.setLossReserveRetained(editPremium.get("LOSS_RESERVERETAINED_OC")==null?"":editPremium.get("LOSS_RESERVERETAINED_OC").toString());
								bean.setLossReserveRetained((getMultipleVal(bean.getLossReserveRetained())));
								bean.setProfitCommission(editPremium.get("PROFIT_COMMISSION_OC")==null?"":editPremium.get("PROFIT_COMMISSION_OC").toString());
								bean.setProfitCommission((getMultipleVal(bean.getProfitCommission())));
								bean.setCashLossPaid(editPremium.get("CASH_LOSSPAID_OC")==null?"":editPremium.get("CASH_LOSSPAID_OC").toString());
								bean.setCashLossPaid((getMultipleVal(bean.getCashLossPaid())));
								bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
								bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
								bean.setNetDue((getMultipleVal(bean.getNetDue())));
								bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());
								bean.setClaimspaid(editPremium.get("CLAIMS_PAID_OC")==null?"":editPremium.get("CLAIMS_PAID_OC").toString());	
								bean.setClaimspaid((getMultipleVal(bean.getClaimspaid())));
							    bean.setMdpremium(fm.formatter(editPremium.get("M_DPREMIUM_OC")==null?"":editPremium.get("M_DPREMIUM_OC").toString()));
							    bean.setMdpremium((getMultipleVal(bean.getMdpremium())));
							    bean.setAdjustmentpremium(editPremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editPremium.get("ADJUSTMENT_PREMIUM_OC").toString());
							    bean.setAdjustmentpremium((getMultipleVal(bean.getAdjustmentpremium())));
							    bean.setRecuirementpremium(editPremium.get("REC_PREMIUM_OC")==null?"":editPremium.get("REC_PREMIUM_OC").toString());
							    bean.setRecuirementpremium((getMultipleVal(bean.getRecuirementpremium())));
							    bean.setCommission(editPremium.get("COMMISSION")==null?"":editPremium.get("COMMISSION").toString());
							    bean.setCommission((getMultipleVal(bean.getCommission())));
							    bean.setXlCost(editPremium.get("XL_COST_OC")==null?"":editPremium.get("XL_COST_OC").toString());
							    bean.setXlCost((getMultipleVal(bean.getXlCost())));
							    bean.setCliamportfolioout(editPremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editPremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
							    bean.setCliamportfolioout((getMultipleVal(bean.getCliamportfolioout())));
							    bean.setPremiumReserveReleased(editPremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editPremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
							    bean.setPremiumReserveReleased((getMultipleVal(bean.getPremiumReserveReleased())));
							    bean.setOtherCost((editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString()));
							    bean.setOtherCost((getMultipleVal(bean.getOtherCost())));
								bean.setInterest((editPremium.get("INTEREST_OC")==null?"":editPremium.get("INTEREST_OC").toString()));
								bean.setInterest((getMultipleVal(bean.getInterest())));
								bean.setOsClaimsLossUpdateOC(editPremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editPremium.get("OSCLAIM_LOSSUPDATE_OC").toString());
								bean.setOsClaimsLossUpdateOC((getMultipleVal(bean.getOsClaimsLossUpdateOC())));
								bean.setOverrider(editPremium.get("OVERRIDER_AMT_OC")==null?"":editPremium.get("OVERRIDER_AMT_OC").toString());
								bean.setOverrider((getMultipleVal(bean.getOverrider())));
								bean.setOverriderUSD(editPremium.get("OVERRIDER_AMT_DC")==null?"":editPremium.get("OVERRIDER_AMT_DC").toString());	
								bean.setOverriderUSD((getMultipleVal(bean.getOverriderUSD())));
		                        bean.setWithHoldingTaxOC((editPremium.get("WITH_HOLDING_TAX_OC")==null?"":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
		                        bean.setWithHoldingTaxOC((getMultipleVal(bean.getWithHoldingTaxOC())));
		                        bean.setPredepartment(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
		                        bean.setDepartmentId(editPremium.get("SUB_CLASS")==null?"":editPremium.get("SUB_CLASS").toString());
		                        bean.setTaxDedectSource((editPremium.get("TDS_OC")==null?"":editPremium.get("TDS_OC").toString()));
		                        bean.setTaxDedectSource((getMultipleVal(bean.getTaxDedectSource())));
		                        bean.setServiceTax(fm.formatter(editPremium.get("ST_OC")==null?"":editPremium.get("ST_OC").toString()));
		                        bean.setServiceTax((getMultipleVal(bean.getCommissionSurplus())));
		                        bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"":editPremium.get("BONUS_OC").toString()));
		                        bean.setBonus((getMultipleVal(bean.getBonus())));
		                        bean.setTotalCredit((editPremium.get("TOTAL_CR_OC")==null?"":editPremium.get("TOTAL_CR_OC").toString()));
		                        bean.setTotalCredit((getMultipleVal(bean.getTotalCredit())));
		    					bean.setTotalDebit((editPremium.get("TOTAL_DR_OC")==null?"":editPremium.get("TOTAL_DR_OC").toString()));
		    					bean.setTotalDebit((getMultipleVal(bean.getTotalDebit())));
		    					resList.add(bean);
						}
						 res1.setPremiumEditRes1(resList);
					 }
				}
				else{
				if("3".equalsIgnoreCase(req.getProductId())){
					transList=  queryImpl.selectList(query, args);
					 if(transList.size()>0)
					 {
						 for(int i=0;i<transList.size();i++){
						 PremiumEditRes1 bean = new PremiumEditRes1();
						 Map<String,Object>	 editPremium=transList.get(i);
						bean.setTransaction(editPremium.get("TRANS_DATE")==null?"":editPremium.get("TRANS_DATE").toString()); 
						bean.setAccountPeriod(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString());
						bean.setAccountPeriodyear(editPremium.get("ACCOUNT_PERIOD_YEAR")==null?"":editPremium.get("ACCOUNT_PERIOD_YEAR").toString());
						bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						if(null==editPremium.get("EXCHANGE_RATE")){
							GetCommonValueRes common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
							bean.setExchRate(common.getCommonResponse());
						}
						else{
						bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"0":editPremium.get("EXCHANGE_RATE").toString()));
						}
						bean.setBrokerage(fm.formatter(editPremium.get("BROKERAGE_AMT_OC")==null?"0":editPremium.get("BROKERAGE_AMT_OC").toString()));
						bean.setTax(fm.formatter(editPremium.get("TAX_AMT_OC")==null?"0":editPremium.get("TAX_AMT_OC").toString()));
						bean.setPremiumQuotaShare(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString());
						bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
						bean.setPremiumSurplus(editPremium.get("PREMIUM_SURPLUS_OC")==null?"":editPremium.get("PREMIUM_SURPLUS_OC").toString());
						bean.setCommissionSurplus(editPremium.get("COMMISSION_SURPLUS_OC")==null?"":editPremium.get("COMMISSION_SURPLUS_OC").toString());
						bean.setPremiumportifolioIn(editPremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
						bean.setCliamPortfolioin(editPremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editPremium.get("CLAIM_PORTFOLIOIN_OC").toString());
						bean.setPremiumportifolioout(editPremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
						bean.setLossReserveReleased(editPremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editPremium.get("LOSS_RESERVE_RELEASED_OC").toString());
						bean.setPremiumReserveQuotaShare(editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
						bean.setCashLossCredit(editPremium.get("CASH_LOSS_CREDIT_OC")==null?"":editPremium.get("CASH_LOSS_CREDIT_OC").toString());
						bean.setLossReserveRetained(editPremium.get("LOSS_RESERVERETAINED_OC")==null?"":editPremium.get("LOSS_RESERVERETAINED_OC").toString());
						bean.setProfitCommission(editPremium.get("PROFIT_COMMISSION_OC")==null?"":editPremium.get("PROFIT_COMMISSION_OC").toString());
						bean.setCashLossPaid(editPremium.get("CASH_LOSSPAID_OC")==null?"":editPremium.get("CASH_LOSSPAID_OC").toString());
						bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
						bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
						bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
						bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());
						bean.setClaimspaid(editPremium.get("CLAIMS_PAID_OC")==null?"":editPremium.get("CLAIMS_PAID_OC").toString());				 
					    bean.setMdpremium(fm.formatter(editPremium.get("M_DPREMIUM_OC")==null?"":editPremium.get("M_DPREMIUM_OC").toString()));
					    bean.setAdjustmentpremium(editPremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editPremium.get("ADJUSTMENT_PREMIUM_OC").toString());
					    bean.setRecuirementpremium(editPremium.get("REC_PREMIUM_OC")==null?"":editPremium.get("REC_PREMIUM_OC").toString());
					    bean.setCommission(editPremium.get("COMMISSION")==null?"":editPremium.get("COMMISSION").toString());
					    bean.setInstlmentNo(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
				    	if("RP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())||"AP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
				    	{
				    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
				    	}else
				    	{
				    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")+"_"+editPremium.get("ACCOUNT_PERIOD_QTR"));
				    	}					
					    bean.setInceptionDate(editPremium.get("INS_DATE")==null?"":editPremium.get("INS_DATE").toString());
					    bean.setXlCost(editPremium.get("XL_COST_OC")==null?"":editPremium.get("XL_COST_OC").toString());
					    bean.setCliamportfolioout(editPremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editPremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
					    bean.setPremiumReserveReleased(editPremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editPremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
					    bean.setOtherCost(fm.formatter(editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString()));
					    bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
						bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
						bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
						bean.setInterest(fm.formatter(editPremium.get("INTEREST_OC")==null?"0":editPremium.get("INTEREST_OC").toString()));
						bean.setOsClaimsLossUpdateOC(editPremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editPremium.get("OSCLAIM_LOSSUPDATE_OC").toString());
						bean.setOverrider(editPremium.get("OVERRIDER_AMT_OC")==null?"":editPremium.get("OVERRIDER_AMT_OC").toString());
						bean.setOverriderUSD(editPremium.get("OVERRIDER_AMT_DC")==null?"":editPremium.get("OVERRIDER_AMT_DC").toString());	
						bean.setAmendmentDate(editPremium.get("AMENDMENT_DATE")==null?"":editPremium.get("AMENDMENT_DATE").toString());	
                       bean.setWithHoldingTaxOC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC")==null?"":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
                       bean.setWithHoldingTaxDC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_DC")==null?"":editPremium.get("WITH_HOLDING_TAX_DC").toString()));
                       bean.setRicession(editPremium.get("RI_CESSION")==null?"":editPremium.get("RI_CESSION").toString());
                       bean.setPredepartment(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
                       bean.setDepartmentId(editPremium.get("SUB_CLASS")==null?"":editPremium.get("SUB_CLASS").toString());
                       bean.setTaxDedectSource(fm.formatter(editPremium.get("TDS_OC")==null?"":editPremium.get("TDS_OC").toString()));
                       bean.setTaxDedectSourceDc(fm.formatter(editPremium.get("TDS_DC")==null?"":editPremium.get("TDS_DC").toString()));
                       bean.setServiceTax(fm.formatter(editPremium.get("ST_OC")==null?"":editPremium.get("ST_OC").toString()));
                       bean.setServiceTaxDc(fm.formatter(editPremium.get("ST_DC")==null?"":editPremium.get("ST_DC").toString()));
                       bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"":editPremium.get("BONUS_OC").toString()));
                       bean.setBonusDc(fm.formatter(editPremium.get("BONUS_DC")==null?"":editPremium.get("BONUS_DC").toString()));
                       bean.setGnpiDate((editPremium.get("GNPI_ENDT_NO")==null?"":editPremium.get("GNPI_ENDT_NO").toString()));
                       bean.setStatementDate(editPremium.get("STATEMENT_DATE")==null?"":editPremium.get("STATEMENT_DATE").toString());
                       bean.setChooseTransaction(editPremium.get("REVERSEL_STATUS")==null?"":editPremium.get("REVERSEL_STATUS").toString() );
       	            bean.setTransDropDownVal(editPremium.get("REVERSE_TRANSACTION_NO")==null?"":editPremium.get("REVERSE_TRANSACTION_NO").toString() );
					resList.add(bean);
						 }
						 res1.setPremiumEditRes1(resList);
					 }
				}
				else{
					transList= queryImpl.selectList(query, args);
					 if(transList.size()>0)
					 {
						 for(int i=0;i<transList.size();i++){
							 PremiumEditRes1 bean = new PremiumEditRes1();
							 Map<String,Object>	 xolView=transList.get(i);
					bean.setContNo(xolView.get("CONTRACT_NO")==null?"":xolView.get("CONTRACT_NO").toString());
					bean.setTransactionNo(xolView.get("TRANSACTION_NO")==null?"":xolView.get("TRANSACTION_NO").toString());
					bean.setTransaction(xolView.get("TRANS_DATE")==null?"":xolView.get("TRANS_DATE").toString()); 
					bean.setBrokerage(fm.formatter(xolView.get("BROKERAGE_AMT_OC")==null?"0":xolView.get("BROKERAGE_AMT_OC").toString()));
					bean.setTax(fm.formatter(xolView.get("TAX_AMT_OC")==null?"0":xolView.get("TAX_AMT_OC").toString()));
					bean.setMdpremium(fm.formatter(xolView.get("M_DPREMIUM_OC")==null?"0":xolView.get("M_DPREMIUM_OC").toString()));
					bean.setAdjustmentpremium(fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_OC")==null?"0":xolView.get("ADJUSTMENT_PREMIUM_OC").toString()));							
					bean.setRecuirementpremium(fm.formatter(xolView.get("REC_PREMIUM_OC")==null?"0":xolView.get("REC_PREMIUM_OC").toString()));
					bean.setNetDue(fm.formatter(xolView.get("NETDUE_OC")==null?"0":xolView.get("NETDUE_OC").toString()));
					bean.setLayerno(xolView.get("LAYER_NO")==null?"":xolView.get("LAYER_NO").toString());
					bean.setEnteringMode(xolView.get("ENTERING_MODE")==null?"":xolView.get("ENTERING_MODE").toString());
					bean.setAccountPeriod(xolView.get("INSTALMENT_NUMBER")+(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":("_"+xolView.get("ACCOUNT_PERIOD_QTR"))));
					bean.setCurrency(xolView.get("CURRENCY_ID")==null?"":xolView.get("CURRENCY_ID").toString());
					bean.setOtherCost(fm.formatter(xolView.get("OTHER_COST_OC")==null?"0":xolView.get("OTHER_COST_OC").toString()));
					bean.setBrokerageusd(fm.formatter(xolView.get("BROKERAGE_AMT_DC")==null?"0":xolView.get("BROKERAGE_AMT_DC").toString()));
					bean.setTaxusd(fm.formatter(xolView.get("TAX_AMT_DC")==null?"0":xolView.get("TAX_AMT_DC").toString()));
					bean.setMdpremiumusd(fm.formatter(xolView.get("M_DPREMIUM_DC")==null?"0":xolView.get("M_DPREMIUM_DC").toString()));
					bean.setAdjustmentpremiumusd(fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_DC")==null?"0":xolView.get("ADJUSTMENT_PREMIUM_DC").toString()));
					bean.setRecuirementpremiumusd(fm.formatter(xolView.get("REC_PREMIUM_DC")==null?"0":xolView.get("REC_PREMIUM_DC").toString()));
					bean.setNetdueusd(fm.formatter(xolView.get("NETDUE_DC")==null?"0":xolView.get("NETDUE_DC").toString()));
					bean.setOtherCostUSD(fm.formatter(xolView.get("OTHER_COST_DC")==null?"0":xolView.get("OTHER_COST_DC").toString()));
					bean.setInceptionDate(xolView.get("ENTRY_DATE")==null?"":xolView.get("ENTRY_DATE").toString());
					bean.setCedentRef(xolView.get("CEDANT_REFERENCE")==null?"":xolView.get("CEDANT_REFERENCE").toString());
					bean.setRemarks(xolView.get("REMARKS")==null?"":xolView.get("REMARKS").toString());
					bean.setTotalCredit(fm.formatter(xolView.get("TOTAL_CR_OC")==null?"":xolView.get("TOTAL_CR_OC").toString()));
					bean.setTotalCreditDC(fm.formatter(xolView.get("TOTAL_CR_DC")==null?"":xolView.get("TOTAL_CR_DC").toString()));
					bean.setTotalDebit(fm.formatter(xolView.get("TOTAL_DR_OC")==null?"":xolView.get("TOTAL_DR_OC").toString()));
					bean.setTotalDebitDC(fm.formatter(xolView.get("TOTAL_DR_DC")==null?"":xolView.get("TOTAL_DR_DC").toString()));
					bean.setAmendmentDate(xolView.get("AMENDMENT_DATE")==null?"":xolView.get("AMENDMENT_DATE").toString());
                   bean.setWithHoldingTaxOC(fm.formatter(xolView.get("WITH_HOLDING_TAX_OC")==null?"":xolView.get("WITH_HOLDING_TAX_OC").toString()));
                   bean.setWithHoldingTaxDC(fm.formatter(xolView.get("WITH_HOLDING_TAX_DC")==null?"":xolView.get("WITH_HOLDING_TAX_DC").toString()));
                   bean.setDueDate(xolView.get("DUE_DATE")==null?"":xolView.get("DUE_DATE").toString());
                   bean.setCreditsign(xolView.get("NETDUE_OC")==null?"":xolView.get("NETDUE_OC").toString());
                   bean.setRicession(xolView.get("RI_CESSION")==null?"":xolView.get("RI_CESSION").toString());
                   bean.setPredepartment(xolView.get("PREMIUM_CLASS")==null?"":xolView.get("PREMIUM_CLASS").toString());
                   bean.setDepartmentId(xolView.get("SUB_CLASS")==null?"":xolView.get("SUB_CLASS").toString());
                   bean.setTaxDedectSource(fm.formatter(xolView.get("TDS_OC")==null?"0":xolView.get("TDS_OC").toString()));
                   bean.setTaxDedectSourceDc(fm.formatter(xolView.get("TDS_DC")==null?"0":xolView.get("TDS_DC").toString()));
                   bean.setServiceTax(fm.formatter(xolView.get("ST_OC")==null?"0":xolView.get("ST_OC").toString()));
                   bean.setServiceTaxDc(fm.formatter(xolView.get("ST_DC")==null?"0":xolView.get("ST_DC").toString()));
                   bean.setBonus(fm.formatter(xolView.get("BONUS_OC")==null?"0":xolView.get("BONUS_OC").toString()));
                   bean.setBonusDc(fm.formatter(xolView.get("BONUS_DC")==null?"0":xolView.get("BONUS_DC").toString()));
                   bean.setExchRate(dropDowmImpl.exchRateFormat(xolView.get("EXCHANGE_RATE")==null?"0":xolView.get("EXCHANGE_RATE").toString()));
                   bean.setGnpiDate(xolView.get("GNPI_ENDT_NO")==null?"":xolView.get("GNPI_ENDT_NO").toString());
                   bean.setStatementDate(xolView.get("STATEMENT_DATE")==null?"":xolView.get("STATEMENT_DATE").toString());
                   bean.setChooseTransaction(xolView.get("REVERSEL_STATUS")==null?"":xolView.get("REVERSEL_STATUS").toString() );
                   bean.setTransDropDownVal(xolView.get("REVERSE_TRANSACTION_NO")==null?"":xolView.get("REVERSE_TRANSACTION_NO").toString() );
                   resList.add(bean);
						 }	
						 res1.setPremiumEditRes1(resList);
				 }
				}
				
				if(transList!=null && transList.size()>0)
				res1.setSaveFlag("true");
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
	private String getMultipleVal(String premiumQuotaShare) {
		String res="";double val =0;
		try{
			if(premiumQuotaShare==""){
				premiumQuotaShare="0";
			}
				 val = Double.parseDouble(premiumQuotaShare.replaceAll(",", ""))*-1;
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
	public premiumInsertMethodRes premiumInsertMethod(PremiumInsertMethodReq beanObj) {
		premiumInsertMethodRes response = new premiumInsertMethodRes();
		PremiumInsertRes res1 = new PremiumInsertRes();
		try {
				String query="";
				int result;
				String[] args = insertArguments(beanObj);
			 	String netDueOc="0";
			 	if("3".equalsIgnoreCase(beanObj.getProductId())){
			 		query= "PREMIUM_INSERT_XOLPREMIUM_TEMP";
			 		res1.setRequestNo(args[1]);
				}else{
					query= "premium.insert.retoxolpremium";
					res1.setTransactionNo(args[1]);
				}
		 		netDueOc=args[17];
			 	result= queryImpl.updateQuery(query, args);
			 	if("submit".equalsIgnoreCase(beanObj.getButtonStatus())){
			 		if("3".equalsIgnoreCase(beanObj.getProductId())){
			 			res1.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
						query = "FAC_TEMP_STATUS_UPDATE";
						args = new String[5];
				 		args[0] = "A";
				 		args[1] = beanObj.getLoginId();
				 		args[2] =res1.getTransactionNo()==null?"":res1.getTransactionNo();
				 		args[3]= res1.getRequestNo() ;
				 		args[4]= beanObj.getBranchCode() ;
				 		queryImpl.updateQuery(query,args);
				 		getTempToMainMove(beanObj,netDueOc,res1.getRequestNo(),res1.getTransactionNo());
			 		}
			 		
				if (result==1) {
					if(!("RP".equalsIgnoreCase(beanObj.getInstlmentNo()) || "RTP".equalsIgnoreCase(beanObj.getInstlmentNo()) || "RVP".equalsIgnoreCase(beanObj.getInstlmentNo()) || "AP".equalsIgnoreCase(beanObj.getInstlmentNo()))){
					query= "UPDATE_MND_INSTALLMENT";
					args = new String[4];
					args[0]=res1.getTransactionNo();
					args[1]=beanObj.getContNo();
					args[2]=StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno();
					args[3]=beanObj.getInstlmentNo();
					queryImpl.updateQuery(query,args);
					}
					
				}
				if("transEdit".equalsIgnoreCase(beanObj.getMode())){
					if("3".equalsIgnoreCase(beanObj.getProductId())){
					query= "UPDATE_REV_TRANSACTION_NO";
					}else{
					query= "UPDATE_REV_TRANSACTION_NO_RETRO";
					}
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
			 	response.setInsertRes(res1);;
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
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args=new String[58];
		}else{
			args=new String[56];
		}
		args[0]=beanObj.getContNo();
		
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args[1] = getRequestNo(beanObj.getBranchCode());
		}else{
			args[1] = fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction());
			beanObj.setTransactionNo(args[1]);
		}
		args[2]=beanObj.getTransaction();
		args[3]=beanObj.getAccountPeriod();
		args[4]=StringUtils.isBlank(beanObj.getAccountPeriodyear())?"":beanObj.getAccountPeriodyear();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
		args[5]=beanObj.getCurrencyId();
		}
		else{
			args[5]=beanObj.getCurrency();
		}
		args[6]=beanObj.getExchRate();
		args[7]=beanObj.getBrokerageview();
		args[8] = getModeOfTransaction(beanObj.getBrokerage().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
		args[9]=beanObj.getTaxview();
		args[10]=getModeOfTransaction(beanObj.getTax().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
		args[11]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
		args[12]=getModeOfTransaction(beanObj.getCommission().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[13]=getModeOfTransaction(beanObj.getMdpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
		args[14]=getModeOfTransaction(beanObj.getAdjustmentpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
		args[15]=getModeOfTransaction(beanObj.getRecuirementpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
		if("RP".equalsIgnoreCase(beanObj.getInstlmentNo()))
		{
			args[13]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
			args[14]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getRecuirementpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getRecuirementpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
				}
			}
		}else if("AP".equalsIgnoreCase(beanObj.getInstlmentNo()))
		{
			args[13]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
			args[15]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getAdjustmentpremium()))
			{
			double premiums=Double.parseDouble(beanObj.getAdjustmentpremium().replaceAll(",", ""));
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
				args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
			}
		}
		}else
		{
			args[14]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
			args[15]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getMdpremium()))
			{
			final double premium=Double.parseDouble(beanObj.getMdpremium());
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				final double brokerage=premium*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
				
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				final double tax=premium*(Double.parseDouble(beanObj.getTaxview())/100);
				args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
			}
		}
		}
		args[16]="Y";
		args[18]=beanObj.getLayerno();
		args[19]="2";
		args[20]=StringUtils.isBlank(beanObj.getReceiptno())?"":beanObj.getReceiptno();
		args[21]=beanObj.getInstlmentNo();
		args[22]=StringUtils.isBlank(beanObj.getSettlementstatus())?"":beanObj.getSettlementstatus();
		args[23]=getModeOfTransaction(beanObj.getOtherCost().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[30]=dropDowmImpl.GetDesginationCountry(args[23],beanObj.getExchRate());
		args[31]=beanObj.getCedentRef();
		args[32]=beanObj.getRemarks();
		args[33]=getModeOfTransaction(beanObj.getTotalCredit().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[34]=dropDowmImpl.GetDesginationCountry(args[33],beanObj.getExchRate());
		args[35]=getModeOfTransaction(beanObj.getTotalDebit().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[36]=dropDowmImpl.GetDesginationCountry(args[35],beanObj.getExchRate());
		args[37]=getModeOfTransaction(beanObj.getWithHoldingTaxOC().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());		
		args[38]=dropDowmImpl.GetDesginationCountry(args[37],beanObj.getExchRate());
		args[39]=beanObj.getRicession();
		args[40] = beanObj.getLoginId();
		args[41] = beanObj.getBranchCode();
		args[42]=beanObj.getDepartmentId();
		args[43] = getModeOfTransaction(beanObj.getTaxDedectSource()==null?"0":beanObj.getTaxDedectSource().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[44] = dropDowmImpl.GetDesginationCountry(args[43], beanObj.getExchRate());
		args[45] = getModeOfTransaction(beanObj.getServiceTax()==null?"0":beanObj.getServiceTax().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[46] = dropDowmImpl.GetDesginationCountry(args[45], beanObj.getExchRate());
		args[47] = getModeOfTransaction(beanObj.getBonus()==null?"0":beanObj.getBonus().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[48] = dropDowmImpl.GetDesginationCountry(args[47], beanObj.getExchRate());
		args[17] = getNetDueXol(args,beanObj.getProductId());
		args[29]=dropDowmImpl.GetDesginationCountry(args[17],beanObj.getExchRate());
		beanObj.setTransactionNo(args[1]);
		args[49] = StringUtils.isEmpty(beanObj.getGnpiDate()) ?"" :beanObj.getGnpiDate();
		args[50] ="D";
		args[51]=beanObj.getPredepartment();
		args[52]=beanObj.getStatementDate();
		args[53]=beanObj.getProposalNo();
		args[54]=beanObj.getProductId();
		args[55]=beanObj.getChooseTransaction()==null?"":beanObj.getChooseTransaction();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			if("submit".equalsIgnoreCase(beanObj.getButtonStatus())){
				args[56] = "A";
			}else{
				args[56] = "P";
			}
			args[57] = beanObj.getMode();
		}
		
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
			query="SELECT LPAD("+name+".nextval,6,0) REQ_NO FROM DUAL";
			list = queryImpl.selectSingle(query, new String[] {});
			if (!CollectionUtils.isEmpty(list)) {
				reqNo = list.get(0).get("REQ_NO") == null ? "" : list.get(0).get("REQ_NO").toString();
			}
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
	private static String getNetDueXol(final String[] args, String id) {
		final double Ant=StringUtils.isEmpty(args[13]) ? 0 :Double.parseDouble(args[13]) ;
		final double Bnt=StringUtils.isEmpty(args[14]) ? 0 :Double.parseDouble(args[14]) ;
		final double Cnt=StringUtils.isEmpty(args[15]) ? 0 :Double.parseDouble(args[15]) ;
		final double Dnt=StringUtils.isEmpty(args[8]) ? 0 :Double.parseDouble(args[8]) ;
		final double Ent=StringUtils.isEmpty(args[10]) ? 0 :Double.parseDouble(args[10]) ;
		final double Fnt=StringUtils.isEmpty(args[12]) ? 0 :Double.parseDouble(args[12]) ;
		final double Gnt=StringUtils.isEmpty(args[23]) ? 0 :Double.parseDouble(args[23]) ;
		final double Hnt=StringUtils.isEmpty(args[37]) ? 0 :Double.parseDouble(args[37]) ;
		final double Int=StringUtils.isEmpty(args[43]) ? 0 :Double.parseDouble(args[43]) ;
		final double Jnt=StringUtils.isEmpty(args[45]) ? 0 :Double.parseDouble(args[45]) ;
		 double Knt =0.00;
		 Knt=StringUtils.isEmpty(args[47]) ? 0 :Double.parseDouble(args[47]) ;
	    final double cnt=(Ant+Bnt+Cnt+Int+Jnt)-(Dnt+Ent+Fnt+Gnt+Hnt+Knt);
		return String.valueOf(cnt);
	}
	private void getTempToMainMove(PremiumInsertMethodReq beanObj, String netDueOc,String RequestNo,String TrancationNo) {
		try{
			String query="";
			String args[] = new String[2];
			if(!"Main".equalsIgnoreCase(beanObj.getTableType())){
				query = "FAC_PREMIUM_TEMP_TO_MAIN";
		 		args = new String[2];
		 		args[0] = RequestNo;
		 		args[1] = beanObj.getBranchCode();
		 		queryImpl.updateQuery(query,args);
			}
			if( "3".equalsIgnoreCase(beanObj.getProductId())){
			query= "premium.sp.retroSplit";
			args = new String[16];
			args[0]=beanObj.getContNo();
			args[1]=StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno();
			args[2]=beanObj.getProductId();
			args[3]=TrancationNo;
			args[4]=beanObj.getTransaction();
			if("3".equalsIgnoreCase(beanObj.getProductId())){
				args[5]=beanObj.getCurrencyId();
				}
				else{
					args[5]=beanObj.getCurrency();
				}
			args[6]=beanObj.getExchRate();
			args[7]=beanObj.getBranchCode();
			args[8]="P";
			args[9]=beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate();
			args[10]="";
			args[11]="";
			args[12]="";
			args[13]="";
			args[14]="";
			args[15]=beanObj.getRicession();
			int spresult= queryImpl.updateQuery(query,args);
			}
			else{
				 query= "PRCL_DELETE";
				String arg[]=new String[3];
				arg[0]=beanObj.getContNo();
				arg[1]=StringUtils.isEmpty(beanObj.getLayerno())?"0":beanObj.getLayerno();
				arg[2]=beanObj.getProductId();
				queryImpl.updateQuery(query,arg);
			}
		}
		catch (Exception exe) {
			exe.printStackTrace();
			}
		}
	public String getMovementReportMaxDate(String branchCode) {
		String maxDate = "";
		String query = "premium.mov.rep.max.date";
		try {
			List<Map<String,Object>> list = queryImpl.selectList(query, new String[] {branchCode});
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
			if("Temp".equalsIgnoreCase(beanObj.getTableType()) && "3".equalsIgnoreCase(beanObj.getProductId())){
				query= "XOL_PREMIUM_UPDATE_UPDATE_TEMP";
			}else{
				if("3".equalsIgnoreCase(beanObj.getProductId())){
						query= "premium.update.xolUpdatePre";
					}else{
						query= "premium.update.retroxolUpdatePre";
					}
			}
			netDueOc=args[12];	
			 queryImpl.updateQuery(query,args);
			if("Submit".equalsIgnoreCase(beanObj.getButtonStatus()) && "Temp".equalsIgnoreCase(beanObj.getTableType()) && "3".equalsIgnoreCase(beanObj.getProductId())){
				if("3".equalsIgnoreCase(beanObj.getProductId())){
				beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
				query = "FAC_TEMP_STATUS_UPDATE";
				args = new String[5];
		 		args[0] = "A";
		 		args[1] = beanObj.getLoginId();
		 		args[2] =beanObj.getTransactionNo()==null?"":beanObj.getTransactionNo();
		 		args[3]= beanObj.getRequestNo() ;
		 		args[4]= beanObj.getBranchCode() ;
		 		queryImpl.updateQuery(query,args);
				getTempToMainMove(beanObj,netDueOc,beanObj.getRequestNo(),beanObj.getTransactionNo());
				}
				int spresult= queryImpl.updateQuery("premium.detail.archive",new String[]{beanObj.getContNo(),(StringUtils.isBlank(beanObj.getLayerno())?"0":beanObj.getLayerno()),beanObj.getTransactionNo(),beanObj.getCurrencyId(),beanObj.getExchRate(),netDueOc,beanObj.getDepartmentId(),beanObj.getProductId()});
				int update= queryImpl.updateQuery(query,args);
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
		args=new String[45];
		args[0]=beanObj.getTransaction();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args[1]=beanObj.getCurrencyId();
			}
			else{
				args[1]=beanObj.getCurrency();
			}
		args[2]=beanObj.getExchRate();
		args[3]=beanObj.getBrokerageview();
		args[4]=getModeOfTransaction(beanObj.getBrokerage(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
		args[5]=beanObj.getTaxview();
		args[6]=getModeOfTransaction(beanObj.getTax(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
		args[7]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
		args[8]=getModeOfTransaction(beanObj.getCommission(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[9]=getModeOfTransaction(beanObj.getMdpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
		args[10]=getModeOfTransaction(beanObj.getAdjustmentpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
		args[11]=getModeOfTransaction(beanObj.getRecuirementpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
		args[13]="2";
		args[14]=StringUtils.isBlank(beanObj.getReceiptno())?"":beanObj.getReceiptno();
		
		if(beanObj.getInstlmentNo().equalsIgnoreCase("RP"))
		{
			args[9]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
			args[10]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getRecuirementpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getRecuirementpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
				}
			}
			
		}else if(beanObj.getInstlmentNo().equalsIgnoreCase("AP"))
		{
			args[9]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
			args[11]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getAdjustmentpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getAdjustmentpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
			}
			}
		}else
		{
			args[10]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
			args[11]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getMdpremium()))
			{
			double premium=Double.parseDouble(beanObj.getMdpremium());
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				double brokerage=premium*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				double tax=premium*(Double.parseDouble(beanObj.getTaxview())/100);
				args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
			}
		}
		}
		args[15]=getModeOfTransaction(beanObj.getOtherCost(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[30]=getModeOfTransaction(beanObj.getWithHoldingTaxOC(), beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[22]= dropDowmImpl.GetDesginationCountry(args[15], beanObj.getExchRate());
		args[23]=beanObj.getCedentRef();
		args[24]=beanObj.getRemarks();
		args[25]=getModeOfTransaction(beanObj.getTotalCredit(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[26]= dropDowmImpl.GetDesginationCountry(args[25],beanObj.getExchRate());
		args[27]=getModeOfTransaction(beanObj.getTotalDebit(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[28]= dropDowmImpl.GetDesginationCountry(args[27],beanObj.getExchRate());
		args[29]=beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate();
        args[31]= dropDowmImpl.GetDesginationCountry(args[30], beanObj.getExchRate());
        args[32]=beanObj.getRicession();
        args[33]=beanObj.getDepartmentId();
        args[34] = getModeOfTransaction(beanObj.getTaxDedectSource()==null?"0":beanObj.getTaxDedectSource(),beanObj.getEnteringMode(), beanObj.getShareSigned());
        args[35] =  dropDowmImpl.GetDesginationCountry(args[34], beanObj.getExchRate());
        args[36] = getModeOfTransaction(beanObj.getServiceTax()==null?"0":beanObj.getServiceTax(),beanObj.getEnteringMode(), beanObj.getShareSigned());
        args[37] =  dropDowmImpl.GetDesginationCountry(args[36], beanObj.getExchRate());
		args[38] = getModeOfTransaction(beanObj.getBonus(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[39] =  dropDowmImpl.GetDesginationCountry(args[38], beanObj.getExchRate());
		args[43]=beanObj.getContNo();
		if("Temp".equalsIgnoreCase(beanObj.getTableType()) &&"3".equalsIgnoreCase(beanObj.getProductId())){
			args[44]=beanObj.getRequestNo();
		}else{
			args[44]=beanObj.getTransactionNo();
		}
		args[12]=getNetDueXolUpdate(args,beanObj.getProductId());
		args[21]= dropDowmImpl.GetDesginationCountry(args[12], beanObj.getExchRate());
		args[40]=StringUtils.isEmpty(beanObj.getGnpiDate()) ?"" :beanObj.getGnpiDate();
		args[41]=beanObj.getPredepartment();
		args[42]= beanObj.getStatementDate();
		final String[] copiedArray = new String[args.length];
		System.arraycopy(args, 0, copiedArray, 0, args.length);
		return copiedArray;
	}
	private static String getNetDueXolUpdate(final String[] args, String id) {
		double Ant=StringUtils.isEmpty(args[9]) ? 0 :Double.parseDouble(args[9]) ;
		double Bnt=StringUtils.isEmpty(args[10]) ? 0 :Double.parseDouble(args[10]) ;
		double Cnt=StringUtils.isEmpty(args[11]) ? 0 :Double.parseDouble(args[11]) ;
		double Dnt=StringUtils.isEmpty(args[4]) ? 0 :Double.parseDouble(args[4]) ;
		double Ent=StringUtils.isEmpty(args[6]) ? 0 :Double.parseDouble(args[6]) ;
		double Fnt=StringUtils.isEmpty(args[15]) ? 0 :Double.parseDouble(args[15]) ;
		double Gnt=StringUtils.isEmpty(args[30]) ? 0 :Double.parseDouble(args[30]) ;
		double Int=StringUtils.isEmpty(args[34]) ? 0 :Double.parseDouble(args[34]) ;
		double Jnt=StringUtils.isEmpty(args[36]) ? 0 :Double.parseDouble(args[36]) ;
		double Knt =0.00;
			Knt =StringUtils.isEmpty(args[38]) ? 0 :Double.parseDouble(args[38]) ;
		double c=(Ant+Bnt+Cnt+Int+Jnt)-(Dnt+Ent+Fnt+Gnt+Knt);
		return String.valueOf(c);
	}
	@Override
	public GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();
		String query="";
		try{
	   		String[] args=new String[6];
	   	 	args[0]=req.getBranchCode();
		   	args[1]=req.getProductId();
		   	args[2]=req.getBranchCode();
		   	args[3]=req.getProductId();
		   	args[4]=req.getContNo();		  
			if("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId()) || "".equalsIgnoreCase(req.getTransactionNo())){
		   		args[5] = req.getRequestNo();
		   		query= "XOL_PREMIUM_VIEW_DETAILS_TEMP";
		   	}else{
		   		args[5]=req.getTransactionNo();
		   		if("3".equalsIgnoreCase(req.getProductId())){
				   	query= "premium.select.XolPremiumView";
				   	}else{
				   		query= "premium.select.retroXolPremiumView";
				   	}
		   	}
		    List<Map<String,Object>> list = queryImpl.selectList(query, args);
		    if(list!=null && list.size()>0) {
				Map<String,Object> xolView=(Map<String,Object>) list.get(0);
							bean.setContNo(xolView.get("CONTRACT_NO")==null?"":xolView.get("CONTRACT_NO").toString());
							bean.setTransactionNo(xolView.get("TRANSACTION_NO")==null?"":xolView.get("TRANSACTION_NO").toString());
							bean.setTransaction(xolView.get("TRANS_DATE")==null?"":xolView.get("TRANS_DATE").toString()); 
							bean.setBrokerage(xolView.get("BROKERAGE_AMT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_OC").toString()));
							bean.setTax(xolView.get("TAX_AMT_OC")==null?"":fm.formatter(xolView.get("TAX_AMT_OC").toString()));
							bean.setMdpremium(xolView.get("M_DPREMIUM_OC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_OC").toString()));
							bean.setAdjustmentpremium(xolView.get("ADJUSTMENT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_OC").toString()));							
							bean.setRecuirementpremium(xolView.get("REC_PREMIUM_OC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_OC").toString()));
							bean.setNetDue(xolView.get("NETDUE_OC")==null?"":fm.formatter(xolView.get("NETDUE_OC").toString()));
							bean.setLayerno(xolView.get("LAYER_NO")==null?"":xolView.get("LAYER_NO").toString());
							bean.setEnteringMode(xolView.get("ENTERING_MODE")==null?"":xolView.get("ENTERING_MODE").toString());
							bean.setAccountPeriod(xolView.get("INSTALMENT_NUMBER")+(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":("_"+xolView.get("ACCOUNT_PERIOD_QTR"))));
							bean.setCurrencyId(xolView.get("CURRENCY_ID")==null?"":xolView.get("CURRENCY_ID").toString());
							bean.setOtherCost(xolView.get("OTHER_COST_OC")==null?"":fm.formatter(xolView.get("OTHER_COST_OC").toString()));
							bean.setBrokerageusd(xolView.get("BROKERAGE_AMT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_DC").toString()));
							bean.setTaxusd(xolView.get("TAX_AMT_DC")==null?"":fm.formatter(xolView.get("TAX_AMT_DC").toString()));
							bean.setMdpremiumusd(xolView.get("M_DPREMIUM_DC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_DC").toString()));
							bean.setAdjustmentpremiumusd(xolView.get("ADJUSTMENT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_DC").toString()));
							bean.setRecuirementpremiumusd(xolView.get("REC_PREMIUM_DC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_DC").toString()));
							bean.setNetdueusd(xolView.get("NETDUE_DC")==null?"":fm.formatter(xolView.get("NETDUE_DC").toString()));
							bean.setOtherCostUSD(xolView.get("OTHER_COST_DC")==null?"":fm.formatter(xolView.get("OTHER_COST_DC").toString()));
							bean.setInceptionDate(xolView.get("ENTRY_DATE")==null?"":xolView.get("ENTRY_DATE").toString());
							bean.setCedentRef(xolView.get("CEDANT_REFERENCE")==null?"":xolView.get("CEDANT_REFERENCE").toString());
							bean.setRemarks(xolView.get("REMARKS")==null?"":xolView.get("REMARKS").toString());
							bean.setTotalCredit(xolView.get("TOTAL_CR_OC")==null?"":fm.formatter(xolView.get("TOTAL_CR_OC").toString()));
							bean.setTotalCreditDC(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_CR_DC").toString()));
							bean.setTotalDebit(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_OC").toString()));
							bean.setTotalDebitDC(xolView.get("TOTAL_DR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_DC").toString()));
							bean.setAmendmentDate(xolView.get("AMENDMENT_DATE")==null?"":xolView.get("AMENDMENT_DATE").toString());
                            bean.setWithHoldingTaxOC(xolView.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_OC").toString()));
                            bean.setWithHoldingTaxDC(xolView.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_DC").toString()));
                            bean.setDueDate(xolView.get("DUE_DATE")==null?"":xolView.get("DUE_DATE").toString());
                            bean.setCreditsign(xolView.get("NETDUE_OC")==null?"":xolView.get("NETDUE_OC").toString());
                            bean.setRicession(xolView.get("RI_CESSION")==null?"":xolView.get("RI_CESSION").toString());
                            bean.setPredepartment(xolView.get("PREMIUM_CLASS")==null?"":xolView.get("PREMIUM_CLASS").toString());
                            bean.setDepartmentId(xolView.get("SUB_CLASS")==null?"":xolView.get("SUB_CLASS").toString());
                            bean.setTaxDedectSource(xolView.get("TDS_OC")==null?"":fm.formatter(xolView.get("TDS_OC").toString()));
                            bean.setTaxDedectSourceDc(xolView.get("TDS_DC")==null?"":fm.formatter(xolView.get("TDS_DC").toString()));
                            bean.setServiceTax(xolView.get("ST_OC")==null?"":fm.formatter(xolView.get("ST_OC").toString()));
                            bean.setServiceTaxDc(xolView.get("ST_OC")==null?"":fm.formatter(xolView.get("ST_DC").toString()));
                            bean.setBonus(xolView.get("BONUS_OC")==null?"":fm.formatter(xolView.get("BONUS_OC").toString()));
                            bean.setBonusDc(xolView.get("BONUS_DC")==null?"":fm.formatter(xolView.get("BONUS_DC").toString()));
            				bean.setExchRate(dropDowmImpl.exchRateFormat(xolView.get("EXCHANGE_RATE")==null?"":xolView.get("EXCHANGE_RATE").toString()));
                            bean.setGnpiDate(xolView.get("GNPI_ENDT_NO")==null?"":xolView.get("GNPI_ENDT_NO").toString());
                            bean.setStatementDate(xolView.get("STATEMENT_DATE")==null?"":xolView.get("STATEMENT_DATE").toString());
                            bean.setChooseTransaction(xolView.get("REVERSEL_STATUS")==null?"":xolView.get("REVERSEL_STATUS").toString());
                            bean.setTransDropDownVal(xolView.get("REVERSE_TRANSACTION_NO")==null?"":xolView.get("REVERSE_TRANSACTION_NO").toString());
						}				
		   	if(StringUtils.isNotBlank(bean.getCurrencyId())){
				query= "premium.select.currency";
				list= queryImpl.selectList(query, new String[] {bean.getCurrencyId(),req.getBranchCode()});
				if (!CollectionUtils.isEmpty(list)) {
					bean.setCurrency(list.get(0).get("CURRENCY_NAME") == null ? "" : list.get(0).get("CURRENCY_NAME").toString());
				}
		   	}
		   	query= "premium.select.currecy.name";
		   	list=queryImpl.selectList(query, new String[]{req.getBranchCode()});
			if (!CollectionUtils.isEmpty(list)) {
				bean.setCurrencyName(list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
						: list.get(0).get("COUNTRY_SHORT_NAME").toString());
			}
		   	if("3".equalsIgnoreCase(req.getProductId())){
		   	query= "GETSETTLEMET_STATUS";
			List<Map<String,Object>> premlist = new ArrayList<Map<String,Object>>();
			premlist = queryImpl.selectList(query,new String[]{req.getContNo()});
			List<SettlementstatusRes> res1List = new ArrayList<SettlementstatusRes>();
			if(premlist.size()>0){
				for(int i=0;i<premlist.size();i++){
					Map<String,Object> map = premlist.get(i);
					SettlementstatusRes res1 = new SettlementstatusRes();
						String allocate = map.get("ALLOCATED_TILL_DATE")==null?"0":map.get("ALLOCATED_TILL_DATE").toString();
						String net = map.get("NETDUE_OC").toString();
						if("0".equalsIgnoreCase(allocate)){
							res1.setSettlementstatus("Pending");
						}else if(Double.parseDouble(allocate) == Double.parseDouble(net)){
							res1.setSettlementstatus("Allocated");
						}else{
							res1.setSettlementstatus("Partially Allocated");
						}
						res1List.add(res1);
				}
				bean.setSettlementstatusRes(res1List);
			}
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
	public CommonSaveRes getInstalmentAmount(String contNo, String layerNo,String instalmentno) {
		CommonSaveRes response = new CommonSaveRes();
		String  string=null;
		try{
			String query= "premium.select.mndPremiumOC";
			// final String[] Instalmentno = getAmount.split("_");
			List<Map<String,Object>> list=queryImpl.selectList(query, new String[] {contNo,layerNo,instalmentno});
			if (!CollectionUtils.isEmpty(list)) {
				string = list.get(0).get("MND_PREMIUM_OC") == null ? "" : list.get(0).get("MND_PREMIUM_OC").toString();
			}
			 response.setResponse(string);
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
	public GetBrokerAndCedingNameRes getBrokerAndCedingName(String contNo, String branchCode) {
		GetBrokerAndCedingNameRes response = new GetBrokerAndCedingNameRes();
		List<GetBrokerAndCedingNameRes1> resList = new ArrayList<GetBrokerAndCedingNameRes1>();
		try{
			String query="";
			String args[]=null;
			args=new String[4];
			args[0]=contNo;
			args[1]=branchCode;
			args[2]=contNo;
			args[3]=branchCode;
			query= "broker.ceding.name";
			List<Map<String,Object>> list=queryImpl.selectList(query, args);
			for(int i=0 ; i<list.size() ; i++) {
				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
				GetBrokerAndCedingNameRes1 temp = new GetBrokerAndCedingNameRes1();
				temp.setCutomerId(tempMap.get("CUSTOMER_ID")==null?"":tempMap.get("CUSTOMER_ID").toString());
				temp.setCompanyName(tempMap.get("BROKER")==null?"":tempMap.get("BROKER").toString());
				temp.setAddress(tempMap.get("ADDRESS")==null?"":tempMap.get("ADDRESS").toString());
				resList.add(temp);
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
	public CommonSaveRes getAdjPremium(GetAdjPremiumReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		String query="";
		String obj[]=null;
		List<Map<String,Object>>list=null;
		String premium="0";
		String dppremium="0";
		String adjpremium="0";
		try {
			String[] value=bean.getGnpiDate().split("_");
			query = "GET_GNPI_PREMIUM";
			obj=new String[10];
			obj[0]=bean.getContNo();
			obj[1]=bean.getLayerno();
			obj[2]=bean.getCurrency();
			obj[3]=bean.getLayerno();
			obj[4]=bean.getBranchCode();
			obj[5]=bean.getContNo();
			obj[6]=bean.getLayerno();
			obj[7]=bean.getBranchCode();
			obj[8]=bean.getPredepartment();
			obj[9]=value[0];
			list= queryImpl.selectList(query,obj);
			if(list!=null && list.size()>0){
				Map<String,Object>map=list.get(0);
				premium=map.get("RSK_GNPI_AS_OFF")==null?"0":map.get("RSK_GNPI_AS_OFF").toString();
			}
			query = "GET_DEPOSIT_PREMIUM";
			obj=new String[5];
			obj[0]=bean.getContNo();
			obj[1]=bean.getLayerno();
			obj[2]=bean.getPredepartment();
			obj[3]=bean.getCurrency();
			obj[4]=bean.getBranchCode();
			list= queryImpl.selectList(query,obj);
			if(list!=null && list.size()>0){
				Map<String,Object>map=list.get(0);
				dppremium=map.get("MD_PREMIUM")==null?"0":map.get("MD_PREMIUM").toString();
			}
			adjpremium=Double.toString((Double.parseDouble(premium))-(Double.parseDouble(dppremium)));
			response.setResponse(adjpremium);
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
	



