package com.maan.insurance.service.impl.claim;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.req.claim.AllocListReq;
import com.maan.insurance.model.req.claim.AllocationListReq;
import com.maan.insurance.model.req.claim.ClaimListMode4Req;
import com.maan.insurance.model.req.claim.ClaimListReq;
import com.maan.insurance.model.req.claim.ClaimPaymentEditReq;
import com.maan.insurance.model.req.claim.ClaimPaymentListReq;
import com.maan.insurance.model.req.claim.ClaimTableListMode2Req;
import com.maan.insurance.model.req.claim.ClaimTableListReq;
import com.maan.insurance.model.req.claim.ContractDetailsModeReq;
import com.maan.insurance.model.req.claim.ContractDetailsReq;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;
import com.maan.insurance.model.req.claim.GetContractNoReq;
import com.maan.insurance.model.req.claim.GetReInsValueListReq;
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.res.ClaimPaymentListRes;
import com.maan.insurance.model.res.ClaimlistRes;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.claim.AllocListRes;
import com.maan.insurance.model.res.claim.AllocListRes1;
import com.maan.insurance.model.res.claim.AllocationListRes;
import com.maan.insurance.model.res.claim.ClaimListMode3Res;
import com.maan.insurance.model.res.claim.ClaimListMode3Response;
import com.maan.insurance.model.res.claim.ClaimListMode4Res;
import com.maan.insurance.model.res.claim.ClaimListMode4Response;
import com.maan.insurance.model.res.claim.ClaimListMode5Res;
import com.maan.insurance.model.res.claim.ClaimListMode5Response;
import com.maan.insurance.model.res.claim.ClaimListMode6Res;
import com.maan.insurance.model.res.claim.ClaimListMode6Response;
import com.maan.insurance.model.res.claim.ClaimListMode7Res1;
import com.maan.insurance.model.res.claim.ClaimListMode7ResList;
import com.maan.insurance.model.res.claim.ClaimListRes;
import com.maan.insurance.model.res.claim.ClaimPaymentEditRes;
import com.maan.insurance.model.res.claim.ClaimPaymentEditRes1;
import com.maan.insurance.model.res.claim.ClaimPaymentListRes1;
import com.maan.insurance.model.res.claim.ClaimRes;
import com.maan.insurance.model.res.claim.ClaimTableListMode1Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode2Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode2ResList;
import com.maan.insurance.model.res.claim.ClaimTableListMode7Res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode10res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode1res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode4res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode5res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode6res;
import com.maan.insurance.model.res.claim.ContractDetailsListMode7res;
import com.maan.insurance.model.res.claim.ContractDetailsMode10Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode1Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode4Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode5Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode6Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode7Res;
import com.maan.insurance.model.res.claim.ContractidetifierlistRes;
import com.maan.insurance.model.res.claim.ContractidetifierlistRes1;
import com.maan.insurance.model.res.claim.GetContractNoRes;
import com.maan.insurance.model.res.claim.GetContractNoRes1;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode12Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode2Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode3Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode8Res;
import com.maan.insurance.model.res.claim.ProductIdListRes;
import com.maan.insurance.model.res.claim.ProductIdListRes1;
import com.maan.insurance.model.res.claim.ProposalNoRes;
import com.maan.insurance.service.claim.ClaimService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.Validation;
import com.maan.insurance.validation.Claim.ValidationImple;


@Service
public class ClaimServiceImple implements ClaimService {
	private Logger log = LogManager.getLogger(ClaimServiceImple.class);
	
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	

	@Autowired
	private ValidationImple vi;
	
//Claim List Mode1
	@Override
	public ClaimTableListMode1Res claimTableListMode1(ClaimTableListReq req) {
		log.info("CliamBusinessImpl cliamTableList || Enter");
		List<ClaimlistRes> cliamlists = new ArrayList<ClaimlistRes>();
		
		String query = "";
		List<Map<String, Object>> list;
		ClaimTableListMode1Res res = new ClaimTableListMode1Res();
		try {
			/*if (StringUtils.isNotBlank(req.getClaimNo())) {
				query = "claim.select.sumPaidAmt1"; 
				list = queryImpl.selectList(query, new String[] { req.getClaimNo(), req.getPolicyContractNo() });

				if (!CollectionUtils.isEmpty(list)) {
					res.setSumOfPaidAmountOC(fm.formatter(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
							: list.get(0).get("PAID_AMOUNT_OC").toString()));
				}

			} */

			query = "claim.select.claimTableList";

			list = queryImpl.selectList(query,
					new String[] { req.getPolicyContractNo(), req.getLayerNo(), req.getDepartmentId() });
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
				
				ClaimlistRes cliam = new ClaimlistRes();
				cliam.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
				cliam.setDateOfLoss(tempMap.get("DATE_OF_LOSS") == null ? "" : tempMap.get("DATE_OF_LOSS").toString());
				cliam.setCreatedDate(tempMap.get("CREATED_DATE") == null ? "" : tempMap.get("CREATED_DATE").toString());
				cliam.setStatusOfClaim(tempMap.get("STATUS_OF_CLAIM") == null ? "" : tempMap.get("STATUS_OF_CLAIM").toString());
				cliam.setPolicyContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
				cliam.setEditMode(tempMap.get("EDITVIEW") == null ? "" : tempMap.get("EDITVIEW").toString());
				int count = Integer.valueOf(dropDowmImpl.Validatethree(req.getBranchCode(), cliam.getCreatedDate()));
				int claimpaymentcount = Integer
						.valueOf(getCliampaymnetCount(cliam.getClaimNo(), cliam.getPolicyContractNo()));
		if (count != 0 && claimpaymentcount == 0) {
				cliam.setDeleteStatus("Y");
				}
				cliamlists.add(cliam);
			}
			res.setCommonResponse(cliamlists);
		/*	if (StringUtils.isNotBlank(req.getProposalNo())) {
				query = "setCeaseStatus";
				list = queryImpl.selectList(query, new String[] { req.getProposalNo() });

				if (!CollectionUtils.isEmpty(list)) {
					res.setCeaseStatus(
							list.get(0).get("CEASE_STATUS") == null ? "" : list.get(0).get("CEASE_STATUS").toString());
				}

			} */
			res.setMessage("Success");
			res.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
	}

	public String getCliampaymnetCount(String claimNo, String contNo) {
		String result = "";

		try {
			String query1 = "getCliampaymnetCount";
			String[] args = new String[2];
			args[0] = claimNo;
			args[1] = contNo;
			List<Map<String, Object>> list = queryImpl.selectList(query1, args);

			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0).get("COUNT") == null ? "" : list.get(0).get("COUNT").toString();
			}

		} catch (Exception e) {
			log.debug("Exception @ {" + e + "}");
		}
		return result;
	}
	//Claim List Mode2
	@Override
	public ClaimTableListMode2Res claimTableListMode2(ClaimTableListMode2Req req) {
		ClaimTableListMode2Res response =new ClaimTableListMode2Res();
		List<ClaimTableListMode2ResList> cliamlists = new ArrayList<ClaimTableListMode2ResList>();
		
		String query;
		String args[]=null;
		List<Map<String, Object>> list;
		try{
			
		args=new String[2];
		args[0]=req.getClaimNo();
		args[1]=req.getContractNo();
		
		query = "claim.select.getClaimUpdateList";
		
	
		
		double a=0.0;
		double b=0.0;
		double c=0.0;
		double d=0.0;
		 list=queryImpl.selectList(query,args);
		if(list!=null && list.size()>0){
			for (int j = 0; j < list.size(); j++) {
				ClaimTableListMode2ResList bean=new ClaimTableListMode2ResList();
				Map<String,Object> tempMap = (Map<String, Object>) list.get(j);
				
				bean.setLossEstimateRevisedOrigCurr(tempMap.get("LOSS_ESTIMATE_REVISED_OC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
				bean.setLossEstimateRevisedUSD(tempMap.get("LOSS_ESTIMATE_REVISED_DC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_DC").toString()));
				bean.setUpdateReference(tempMap.get("UPDATE_REFERENCE")==null?"":tempMap.get("UPDATE_REFERENCE").toString());
				bean.setCliamupdateDate(tempMap.get("INCEPTION_DT")==null?"":tempMap.get("INCEPTION_DT").toString());
				bean.setSNo(tempMap.get("SL_NO")==null?"":tempMap.get("SL_NO").toString());
				bean.setPolicyContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				bean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
				cliamlists.add(bean);
				
			}
			response.setCommonResponse(cliamlists);
			//			response.setTotalORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(a)))));
//			response.setTotalSApaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(b)))));
//			response.setTotalOPpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(c)))));
//			response.setTotalTORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(d)))));
			
		}
		
//		query = "premium.select.currecy.name";
//		list = queryImpl.selectList(query, new String[] { req.getBranchCode() });
//
//		if (!CollectionUtils.isEmpty(list)) {
//			response.setCurrencyName((list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
//					: list.get(0).get("COUNTRY_SHORT_NAME").toString()));
//		}
//		
		
		

		response.setMessage("Success");
		response.setIsError(false);

	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
		
	}
	//Claim List Mode7
	@Override
	public ClaimTableListMode7Res claimTableListMode7(ClaimTableListMode2Req req) {
		ClaimTableListMode7Res response =new ClaimTableListMode7Res();
		List<ClaimListMode7ResList> cliamlists = new ArrayList<ClaimListMode7ResList>();
		ClaimListMode7Res1 res = new ClaimListMode7Res1();
		
		String query;
		String args[]=null;
		List<Map<String, Object>> list;
		try{
			
		args=new String[4];
		args[0]=req.getContractNo();
		args[1]=req.getClaimNo();
		args[2]=req.getContractNo();
		args[3]=req.getClaimNo();
		query = "clime.select.getClaimRESERVEList";
		double a=0.0;
		double b=0.0;
		double c=0.0;
		double d=0.0;
		 list=queryImpl.selectList(query,args);
		if(list!=null && list.size()>0){
			for (int j = 0; j < list.size(); j++) {
				ClaimListMode7ResList bean=new ClaimListMode7ResList();
				Map<String,Object> tempMap = (Map<String, Object>) list.get(j);
				
				bean.setLossEstimateRevisedOrigCurr(tempMap.get("LOSS_ESTIMATE_REVISED_OC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
				bean.setLossEstimateRevisedUSD(tempMap.get("LOSS_ESTIMATE_REVISED_DC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_DC").toString()));
				bean.setUpdateReference(tempMap.get("UPDATE_REFERENCE")==null?"":tempMap.get("UPDATE_REFERENCE").toString());
				bean.setCliamupdateDate(tempMap.get("INCEPTION_DT")==null?"":tempMap.get("INCEPTION_DT").toString());
				bean.setSNo(tempMap.get("SL_NO")==null?"":tempMap.get("SL_NO").toString());
				bean.setPolicyContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				bean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
				

				bean.setLero2a(tempMap.get("LOSS_ESTIMATE_REVISED_OC_2A")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC_2A").toString()));
				bean.setLero2b(tempMap.get("PAID_CLAIM_OS_OC_2B")==null?"":fm.formatter(tempMap.get("PAID_CLAIM_OS_OC_2B").toString()));
				bean.setLero2c(tempMap.get("OC_OS_AMOUNT_2C")==null?"":fm.formatter(tempMap.get("OC_OS_AMOUNT_2C").toString()));
				bean.setSaf3a(tempMap.get("SAF_OS_OC_3A")==null?"":fm.formatter(tempMap.get("SAF_OS_OC_3A").toString()));
				bean.setSaf3b(tempMap.get("SAF_OS_OC_3B")==null?"":fm.formatter(tempMap.get("SAF_OS_OC_3B").toString()));
				bean.setSaf3c(tempMap.get("SURVEYOR_OS_AMT_3C")==null?"":fm.formatter(tempMap.get("SURVEYOR_OS_AMT_3C").toString()));
				bean.setOfos4a(tempMap.get("OTH_FEE_OS_OC_4A")==null?"":fm.formatter(tempMap.get("OTH_FEE_OS_OC_4A").toString()));
				bean.setOfos4b(tempMap.get("OTH_FEE_OS_OC_4B")==null?"":fm.formatter(tempMap.get("OTH_FEE_OS_OC_4B").toString()));
				bean.setOfos4c(tempMap.get("OTHER_PROFESS_OS_AMT_4C")==null?"":fm.formatter(tempMap.get("OTHER_PROFESS_OS_AMT_4C").toString()));
				bean.setTotala(tempMap.get("TOTAL_A")==null?"":fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_A").toString())))));
				bean.setTotalb(tempMap.get("TOTAL_B")==null?"":fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_B").toString())))));
				bean.setTotalc(tempMap.get("TOTAL_C")==null?"":fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_C").toString())))));
				bean.setReInspremiumOS(tempMap.get("REINSPREMIUM_OURSHARE_OC")==null?"":fm.formatter(tempMap.get("REINSPREMIUM_OURSHARE_OC").toString()));
				bean.setCibnr100Oc(tempMap.get("C_IBNR_OS_OC")==null?"":fm.formatter(tempMap.get("C_IBNR_OS_OC").toString()));
				
				a=a+Double.parseDouble(tempMap.get("PAID_CLAIM_OS_OC_2B")==null?"":tempMap.get("PAID_CLAIM_OS_OC_2B").toString());
				b=b+Double.parseDouble(tempMap.get("SAF_OS_OC_3B")==null?"":tempMap.get("SAF_OS_OC_3B").toString());
				c=c+Double.parseDouble(tempMap.get("OTH_FEE_OS_OC_4B")==null?"":tempMap.get("OTH_FEE_OS_OC_4B").toString());
				d=d+Double.parseDouble(tempMap.get("TOTAL_B")==null?"":Double.toString(Double.parseDouble(tempMap.get("TOTAL_B").toString())));
				
				cliamlists.add(bean);
				
			}
			res.setCommonResponse(cliamlists);
			
			res.setTotalORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(a)))));
			res.setTotalSApaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(b)))));
			res.setTotalOPpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(c)))));
			res.setTotalTORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(d)))));
			
		}
		
		query = "premium.select.currecy.name";
		list = queryImpl.selectList(query, new String[] {req.getBranchCode()});

		if (!CollectionUtils.isEmpty(list)) {
			res.setCurrencyName((list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
					: list.get(0).get("COUNTRY_SHORT_NAME").toString()));
		}
		
	response.setCommonResponse(res);
	response.setMessage("Success");
	response.setIsError(false);

} catch (Exception e) {
	log.error(e);
	e.printStackTrace();
	response.setMessage("Failed");
	response.setIsError(true);
}
return response;
		
	}
	//Claim List Mode3

	@Override
	public ClaimListMode3Response claimListMode3(ClaimTableListMode2Req req) {
		 List<ClaimListMode3Res> response = new ArrayList<ClaimListMode3Res>();
		ClaimListMode3Response res = new ClaimListMode3Response();
		String query="";
		
		List<Map<String, Object>> list;
		try {
			query = "claim.select.getClaimList";
			
			list = queryImpl.selectList(query, new String[] {req.getClaimNo(),req.getContractNo(),req.getBranchCode()});
			if(list!=null && list.size()>0){
				for (int j = 0; j < list.size(); j++) {
					final ClaimListMode3Res bean=new ClaimListMode3Res();
					Map<String,Object> tempMap = (Map<String, Object>) list.get(j);
					
					bean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
					bean.setDateofLoss(tempMap.get("DATEOFLOSS")==null?"":tempMap.get("DATEOFLOSS").toString());
					bean.setLossEstimateOurshareOrigCurr(tempMap.get("LOSS_ESTIMATE_OS_OC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_OS_OC").toString()));
					bean.setCurrecny(tempMap.get("CURRENCY_NAME")==null?"":tempMap.get("CURRENCY_NAME").toString());
					bean.setDate(tempMap.get("CREATEDDATE")==null?"":tempMap.get("CREATEDDATE").toString());
					response.add(bean);
				}}
			res.setCommonResponse(response);
			res.setMessage("Success");
			res.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
		}
	//Claim List Mode4
	@Override
	public ClaimListMode4Response claimListMode4(ClaimListMode4Req req) {
		ClaimListMode4Response res = new ClaimListMode4Response();
       List<ClaimListMode4Res> response = new ArrayList<ClaimListMode4Res>();
      
		String query = "";
		int count = 0;
		String maxno="";
		List<Map<String, Object>> list;
		try {

		query = "claim.select.getClaimReserveList";


		List<Map<String,Object>> claimlist = new ArrayList<Map<String,Object>>();
		claimlist=queryImpl.selectList(query, new String[] {req.getClaimNo(),req.getContractNo()});
		String que="payment.select.maxno";
		
		list = queryImpl.selectList(que, new String[] {req.getClaimNo(),req.getContractNo()});

		if (!CollectionUtils.isEmpty(list)) {
			 maxno = list.get(0).get("MAX_NO") == null ? ""
					: list.get(0).get("MAX_NO").toString();
		}
		
		
	if(claimlist.size()>0){
			for(int i=0;i<claimlist.size();i++){
				Map<String,Object> result = claimlist.get(i);
				
				final ClaimListMode4Res bean=new ClaimListMode4Res();
				bean.setPaymentRequestNo(result.get("PAYMENT_REQUEST_NO")==null?"":result.get("PAYMENT_REQUEST_NO").toString());
				bean.setPaidAmountOrigCurr(result.get("PAID_AMOUNT_OC")==null?"":fm.formatter(result.get("PAID_AMOUNT_OC").toString()));
				//bean.setLossEstimateRevisedOrigCurr(result.get("LOSS_ESTIMATE_REVISED_OC")==null?"":fm.formatter(result.get("LOSS_ESTIMATE_REVISED_OC").toString()));
				bean.setDate(result.get("INCEPTION_DT")==null?"":result.get("INCEPTION_DT").toString());
				bean.setClaimPaymentNo(result.get("CLAIM_PAYMENT_NO")==null?"":result.get("CLAIM_PAYMENT_NO").toString());
				bean.setSNo(result.get("RESERVE_ID")==null?"":result.get("RESERVE_ID").toString());
//				bean.setSettlementStatus(result.get("SETTLEMENT_STATUS")==null?"":result.get("SETTLEMENT_STATUS").toString());
//				bean.setTransactionType(result.get("TRANS_TYPE")==null?"":result.get("TRANS_TYPE").toString());
//				bean.setTransactionNumber(result.get("TRANSACTION_NO")==null?"":result.get("TRANSACTION_NO").toString());
				
//				query = "payment.select.count.allocatedYN";
//				claimlist =  queryImpl.selectList(query, new String[]{req.getContractNo(),req.getClaimPaymentNo(),req.getLayerNo()});
//				if (!CollectionUtils.isEmpty(claimlist)) {
//					 count = Integer.valueOf(claimlist.get(0).get("ALLOCATEDYN") == null ? ""
//							: claimlist.get(0).get("ALLOCATEDYN").toString());
//				}
				
				
//				if(count==0){
//					bean.setAllocatedYN("Y");
//				}else if(count>=1){
//					bean.setAllocatedYN("N");
//				}
				
//				claimlist =  queryImpl.selectList("GET_STATUS_OF_CLAIM", new String[]{req.getContractNo(),req.getClaimNo(),req.getLayerNo()});
//				if (!CollectionUtils.isEmpty(claimlist)) {
//					bean.setStatusofClaim((claimlist.get(0).get("STATUS_OF_CLAIM") == null ? ""
//							: claimlist.get(0).get("STATUS_OF_CLAIM").toString()));
//				}
				
			
//				if(StringUtils.isNotBlank(bean.getDate())){
//					if(Validatethree(req.getBranchCode(), bean.getDate())==0 ){
//						bean.setTransOpenperiodStatus("N");
//					}else if(!result.get("RESERVE_ID").toString().equalsIgnoreCase(maxno)){
//						bean.setTransOpenperiodStatus("N");
//					}
//					else
//					{
//						bean.setTransOpenperiodStatus("Y");
//					}
//					}
				response.add(bean);
				
			}
		}
			res.setCommonResponse(response);	
			res.setMessage("Success");
			res.setIsError(false);
			
			} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
			}
			return res;
				} 
	//Claim List Mode5
	@Override
	public ClaimListMode5Response claimListMode5(String claimNo, String contractNo) {
		 List<ClaimListMode5Res> response = new ArrayList<ClaimListMode5Res>();
		 ClaimListMode5Response res = new ClaimListMode5Response();
			String query = "";
			
			List<Map<String, Object>> cliamlists;
	
		try {
	
		query= "claim.select.getClaimPaymentList";
		log.info("Select Query====>"+query);
		log.info("Arg[0]====>"+claimNo);
		log.info("Arg[1]====>"+contractNo);
		cliamlists = queryImpl.selectList(query, new String[] {claimNo, contractNo});
		if(cliamlists.size()>0){
			for(int i=0;i<cliamlists.size();i++){
				Map<String,Object> result = cliamlists.get(i);
				final ClaimListMode5Res bean=new ClaimListMode5Res();
				bean.setPaymentRequestNo(result.get("PAYMENT_REQUEST_NO")==null?"":result.get("PAYMENT_REQUEST_NO").toString());
				bean.setLossEstimateRevisedOrigCurr(result.get("LOSS_ESTIMATE_REVISED_OC")==null?"":result.get("LOSS_ESTIMATE_REVISED_OC").toString());
				bean.setPaidAmountOrigCurr(result.get("PAID_AMOUNT_OC")==null?"":result.get("PAID_AMOUNT_OC").toString());
				bean.setDate(result.get("INCEPTION_DT")==null?"":result.get("INCEPTION_DT").toString());
				bean.setClaimPaymentNo(result.get("CLAIM_PAYMENT_NO")==null?"":result.get("CLAIM_PAYMENT_NO").toString());
				bean.setSNo(result.get("RESERVE_ID")==null?"":result.get("RESERVE_ID").toString());
				response.add(bean);
			}
		}
	
		res.setCommonResponse(response);	
		res.setMessage("Success");
		res.setIsError(false);
		
		} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		res.setMessage("Failed");
		res.setIsError(true);
		}
		return res;
	}
	//Claim List Mode6
	@Override
	public ClaimListMode6Response claimListMode6(String contractNo, String claimNo) {
		 List<ClaimListMode6Res> response = new ArrayList<ClaimListMode6Res>();
		 ClaimListMode6Response res = new ClaimListMode6Response();
			String query = "";
			
			List<Map<String, Object>> cliamlists;
	
		try {
			query="claim.select.getClaimReviewList";	
			log.info("Select Query====>"+query);
		log.info("Arg[0]====>"+contractNo);
		log.info("Arg[1]====>"+claimNo);
		cliamlists=queryImpl.selectList(query, new String[] {contractNo,claimNo}); 
		if(cliamlists.size()>0){
			for(int i=0;i<cliamlists.size();i++){
				Map<String,Object> result = cliamlists.get(i);
				final ClaimListMode6Res bean=new ClaimListMode6Res();
				bean.setSNo(result.get("SNO")==null?"":result.get("SNO").toString());
				bean.setReviewDate(result.get("REVIEW_DATE")==null?"":result.get("REVIEW_DATE").toString());	
				bean.setReviewDoneBy(result.get("REVIEW_DONE_BY")==null?"":result.get("REVIEW_DONE_BY").toString());
				bean.setRemarks(result.get("REMARKS")==null?"":result.get("REMARKS").toString());
				response.add(bean);
			}
		}
		res.setCommonResponse(response);	
		res.setMessage("Success");
		res.setIsError(false);
		
		} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		res.setMessage("Failed");
		res.setIsError(true);
		}
		return res;				
	}

	@Override
	public GetContractNoRes1 getContractNo(GetContractNoReq req) {
		GetContractNoRes1 response = new GetContractNoRes1();
		GetContractNoRes res = new GetContractNoRes();
		String query="";
		
		String[] arg=null;
		try{
		query="GET_CONTRACT_NUMBER";
		arg = new String[] {req.getClaimNo()};
		if(StringUtils.isNotBlank(req.getContractNo())){
			query =	"GET_CONTRACT_NUMBER1";
			arg = new String[] {req.getClaimNo(), req.getContractNo()};
			
		}
		
		
		List<Map<String,Object>>list=queryImpl.selectList(query, arg);
		if(list!=null &&list.size()>0){
			Map<String,Object>map=list.get(0);
			res.setContractNo(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
			if(!"2".equalsIgnoreCase(req.getProductId())){
			res.setDepartmentId(map.get("CLAIM_CLASS")==null?"":map.get("CLAIM_CLASS").toString());
			}
		}
		response.setCommonResponse(res);
		response.setMessage("Success");
		response.setIsError(false);

	} catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public ClaimPaymentEditRes1 claimPaymentEdit(ClaimPaymentEditReq req) {
		ClaimPaymentEditRes1 res = new ClaimPaymentEditRes1();
		List<ClaimPaymentEditRes>  response = new ArrayList<ClaimPaymentEditRes>();
	
		try{
			List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
			String query = "GET_CLAIM_PAYMENT_DATA";
			String args[]=new String[4];
			args[0] = req.getClaimNo();
			args[1] = req.getContractNo();
			args[2] = req.getLayerNo();
			args[3] = req.getClaimPaymentNo();
			list = queryImpl.selectList(query,args);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					ClaimPaymentEditRes bean = new ClaimPaymentEditRes();
					Map<String,Object> map = list.get(i);
					bean.setDate(map.get("INCEPTION_DATE")==null?"":map.get("INCEPTION_DATE").toString());
					bean.setPaymentRequestNo(map.get("PAYMENT_REQUEST_NO")==null?"":map.get("PAYMENT_REQUEST_NO").toString());
					bean.setPaymentReference(map.get("PAYMENT_REFERENCE")==null?"":map.get("PAYMENT_REFERENCE").toString());
					bean.setPaidClaimOs(map.get("PAID_CLAIM_OS_OC")==null?"":fm.formatter(map.get("PAID_CLAIM_OS_OC").toString()));
					bean.setSurveyOrFeeOs(map.get("SAF_OS_OC")==null?"":fm.formatter(map.get("SAF_OS_OC").toString()));
					bean.setOtherprofFeeOs(map.get("OTH_FEE_OS_OC")==null?"":fm.formatter(map.get("OTH_FEE_OS_OC").toString()));
					bean.setPaidAmountOrigCurr(map.get("PAID_AMOUNT_OC")==null?"":fm.formatter(map.get("PAID_AMOUNT_OC").toString()));
					bean.setRemarks(map.get("REMARKS")==null?"":map.get("REMARKS").toString());
					bean.setClaimPaymentNo(map.get("CLAIM_PAYMENT_NO")==null?"":map.get("CLAIM_PAYMENT_NO").toString());
//					if("paymentView".equalsIgnoreCase(req.getDropDown())){
//					bean.setReinstType(map.get("REINSTATEMENT_TYPE_NAME")==null?"":map.get("REINSTATEMENT_TYPE_NAME").toString());
//					}else{
//						bean.setReinstType(map.get("REINSTATEMENT_TYPE")==null?"":map.get("REINSTATEMENT_TYPE").toString());
//						
//					}
					bean.setReinstPremiumOCOS(map.get("REINSPREMIUM_OURSHARE_OC")==null?"":map.get("REINSPREMIUM_OURSHARE_OC").toString());
					bean.setPaymentType(map.get("PAYMENT_TYPE")==null?"":map.get("PAYMENT_TYPE").toString());
					response.add(bean);
					
				}
			}
			res.setCommonResponse(response);
			res.setMessage("Success");
			res.setIsError(false);
			
			} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
			}
			return res;	
	}

	

	@Override
	public AllocListRes1 allocList(AllocListReq req) {
		String query="";
		List<AllocListRes>  allocists=new ArrayList<AllocListRes> ();
		AllocListRes1 res = new AllocListRes1();
		try{
		query = "claim_allocation_list";
		log.info("Select Query====>"+query);
		String arg[]=new String[4];
		  arg[0]=req.getContractNo();
		  arg[1]=req.getTransactionNo();
		  arg[2]=req.getContractNo();
		  arg[3]=req.getTransactionNo();
		  List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		 
		  list = queryImpl.selectList(query,arg);
		  if(list.size()>0){
				for(int i=0;i<list.size();i++){
					 final AllocListRes bean=new AllocListRes();
					Map<String,Object> map = list.get(i);
					bean.setDate(map.get("INCEPTION_DATE")==null?"":map.get("INCEPTION_DATE").toString());
					bean.setSNo(map.get("SNO")==null?"":map.get("SNO").toString());
					bean.setTransactionType(map.get("TRANS_TYPE")==null?"":map.get("TRANS_TYPE").toString());
					bean.setPaidAmountOrigcurr(map.get("PAID_AMOUNT")==null?"":map.get("PAID_AMOUNT").toString());
					bean.setStatusofclaim(map.get("STATUS")==null?"":map.get("STATUS").toString());
					bean.setProductName(map.get("PRODUCT_NAME")==null?"":map.get("PRODUCT_NAME").toString());
					bean.setType(map.get("TYPE")==null?"":map.get("TYPE").toString());
					bean.setSign(map.get("SIGN")==null?"":map.get("SIGN").toString());
					
				if(bean.getSign().equalsIgnoreCase("-1")){
					bean.setSign("-");
				}
				else if(bean.getSign().equalsIgnoreCase("1")){
					bean.setSign("+");
				}
				else{
					bean.setSign("");
				}
				bean.setReceiptNo(map.get("RECEIPT_NO")==null?"":map.get("RECEIPT_NO").toString());
				allocists.add(bean);
			}
		}
		  res.setCommonResponse(allocists);
			res.setMessage("Success");
			res.setIsError(false);
			
			} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
			}
			return res;	
		
	}

	@Override
	public ProductIdListRes1 productIdList(String branchCode) {
		ProductIdListRes1 res = new ProductIdListRes1();
		List<ProductIdListRes> response = new ArrayList<ProductIdListRes>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		log.info("Enter ino product List");
		try{
		String query="GET_PRODUC_ID_LIST";
		String[] args = new String[1];
		args[0]=branchCode;
		
		log.info("Select Query ==> " + query);
		 list = queryImpl.selectList(query,args);
		
		  if(list.size()>0){
				for(int i=0;i<list.size();i++){
					final ProductIdListRes bean = new ProductIdListRes();
					Map<String,Object> map = list.get(i);
					bean.setProductId(map.get("TMAS_PRODUCT_ID")==null?"":map.get("TMAS_PRODUCT_ID").toString());
					bean.setProductName(map.get("TMAS_PRODUCT_NAME")==null?"":map.get("TMAS_PRODUCT_NAME").toString());
					response.add(bean);
		}
				 res.setCommonResponse(response);
					res.setMessage("Success");
					res.setIsError(false);
					
					} }catch (Exception e) {
					log.error(e);
					e.printStackTrace();
					res.setMessage("Failed");
					res.setIsError(true);
					}
					return res;	
		
	}

	@Override
	public ContractidetifierlistRes1 contractidetifierlist(ContractidetifierlistReq req) {
		ContractidetifierlistRes1 res = new ContractidetifierlistRes1();
		String query="";
		String[] args=null;
		List<Map<String, Object>> allocists=new ArrayList<Map<String, Object>>();
		List<ContractidetifierlistRes> finalList = new ArrayList<ContractidetifierlistRes>();
		log.info("Enter into PremiumList");
		try{
		query="contract.identifier.list";	
		log.info("Select Query====>"+query);
		
			args = new String[] {req.getProductId(), req.getProductId(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getProductId(),req.getBranchCode()};
		
		if(!"N".equalsIgnoreCase(req.getCedingCompanyCode())&&!"".equalsIgnoreCase(req.getCedingCompanyCode())){
			query="contract.identifier.list1";	
			args = new String[] {req.getProductId(), req.getProductId(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getProductId(),req.getBranchCode(),req.getCedingCompanyCode()};
			
		}
		if(!"N".equalsIgnoreCase(req.getBrokerCode())&&!"".equalsIgnoreCase(req.getBrokerCode())){
			query="contract.identifier.list2";	
			args = new String[] {req.getProductId(), req.getProductId(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getProductId(),req.getBranchCode(),req.getBrokerCode()};
			
		}
		if(!"N".equalsIgnoreCase(req.getUnderwritingYear())&&!"".equalsIgnoreCase(req.getUnderwritingYear())){
			query="contract.identifier.list3";	
			args = new String[] {req.getProductId(), req.getProductId(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getProductId(),req.getBranchCode(),req.getUnderwritingYear()};
			
		}
		if(!"N".equalsIgnoreCase(req.getDeptId())&&!"".equalsIgnoreCase(req.getDeptId())){
			query="contract.identifier.list4";	
			args = new String[] {req.getProductId(), req.getProductId(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getBranchCode(),req.getProductId(),req.getBranchCode(),req.getDeptId()};
			
		}
		
		allocists = queryImpl.selectList(query, args);
		for(int i=0 ; i<allocists.size() ; i++) {
			Map<String,Object> tempMap = (Map<String,Object>) allocists.get(i);
			ContractidetifierlistRes tempBean=new ContractidetifierlistRes();
			tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
			tempBean.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
			tempBean.setCedingcompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
			tempBean.setTransactionNumber(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setProductId(req.getProductId());
			tempBean.setDeptId(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
			tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE")==null?"":tempMap.get("EXPIRY_DATE").toString());
			tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
			tempBean.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
			tempBean.setUnderwritingYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
			tempBean.setUnderwriter(tempMap.get("UNDERWRITTER")==null?"":tempMap.get("UNDERWRITTER").toString());
			tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO")==null?"":tempMap.get("OLD_CONTRACTNO").toString());
			tempBean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
			tempBean.setDepartmentId(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
			tempBean.setClaimCount(tempMap.get("COUNT")==null?"":tempMap.get("COUNT").toString());
			
			finalList.add(tempBean);
		}
		
		 res.setCommonResponse(finalList);
			res.setMessage("Success");
			res.setIsError(false);
			
			 }catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
			}
			return res;	
	}

	@Override
	public ClaimPaymentListRes1 claimPaymentList(ClaimPaymentListReq req) {
		ClaimPaymentListRes1 res = new ClaimPaymentListRes1();
		String query="";
		List<Map<String, Object>> allocists=new ArrayList<Map<String, Object>>();
		List<ClaimPaymentListRes> finalList = new ArrayList<ClaimPaymentListRes>();
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		int count=0;
		String[] args =null;
		try{
			if(req.getFlag().equalsIgnoreCase("claim")){
				query="partial.claim.select.getpaymentlist";
				args = new String[] {req.getBranchCode()};
				
				if("S".equalsIgnoreCase(req.getSearchType())){
					if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
						query = "partial.claim.select.getpaymentlist1";
						args = new String[] {req.getBranchCode(), "%"+req.getCompanyNameSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
						query = "partial.claim.select.getpaymentlist2";
						args = new String[] {req.getBranchCode(), "%"+req.getBrokerNameSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getContractNoSearch())){
						query = "partial.claim.select.getpaymentlist3";
						args = new String[] {req.getBranchCode(), "%"+req.getContractNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getClaimNoSearch())){
						query = "partial.claim.select.getpaymentlist4";
						args = new String[] {req.getBranchCode(), "%"+req.getClaimNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
						query = "partial.claim.select.getpaymentlist5";
						args = new String[] {req.getBranchCode(), "%"+req.getPaymentNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
						query = "partial.claim.select.getpaymentlist6";
						args = new String[] {req.getBranchCode(), "%"+req.getPaymentDateSearch()+"%"};
						
					}
					}
			}
			else{
				query="claim.select.getpaymentlist";
				args = new String[] {req.getBranchCode()};
				if("S".equalsIgnoreCase(req.getSearchType())){
					if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
						query="claim.select.getpaymentlist1";
						args = new String[] {req.getBranchCode(), "%"+req.getCompanyNameSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
						query="claim.select.getpaymentlist2";
						args = new String[] {req.getBranchCode(), "%"+req.getBrokerNameSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getContractNoSearch())){
						query="claim.select.getpaymentlist3";
						args = new String[] {req.getBranchCode(), "%"+req.getContractNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getClaimNoSearch())){
						query="claim.select.getpaymentlist4";
						args = new String[] {req.getBranchCode(), "%"+req.getClaimNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
						query="claim.select.getpaymentlist5";
						args = new String[] {req.getBranchCode(), "%"+req.getPaymentNoSearch()+"%"};
						
					}
					if(StringUtils.isNotBlank(req.getPaymentDateSearch())){
						query="claim.select.getpaymentlist6";
						args = new String[] {req.getBranchCode(), "%"+req.getPaymentDateSearch()+"%"};
						
					}
					}
			}
	
		allocists=queryImpl.selectList(query, args);
		
		for(int i=0 ; i<allocists.size() ; i++) {
			Map<String,Object> tempMap = (Map<String,Object>) allocists.get(i);
			ClaimPaymentListRes tempBean=new ClaimPaymentListRes();
			tempBean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
			tempBean.setPolicyContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
			tempBean.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
			tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
			tempBean.setCedingcompanyName(tempMap.get("CUSTOMER_NAME")==null?"":tempMap.get("CUSTOMER_NAME").toString());
			tempBean.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
			tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE")==null?"":tempMap.get("EXPIRY_DATE").toString());
			tempBean.setProductId(tempMap.get("PRODUCT_ID")==null?"":tempMap.get("PRODUCT_ID").toString());
			tempBean.setProductName(tempMap.get("TMAS_PRODUCT_NAME")==null?"":tempMap.get("TMAS_PRODUCT_NAME").toString());
			tempBean.setPaymentRequestNo(tempMap.get("PAYMENT_REQUEST_NO")==null?"":tempMap.get("PAYMENT_REQUEST_NO").toString());
			tempBean.setPaidAmountOrigcurr(fm.formatter(tempMap.get("PAID_AMOUNT_OC")==null?"":tempMap.get("PAID_AMOUNT_OC").toString()));
			tempBean.setLossEstimateRevisedOrigCurr(fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC")==null?"":tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
			tempBean.setInceptionDt(tempMap.get("INCEPTION_DT")==null?"":tempMap.get("INCEPTION_DT").toString());
			tempBean.setClaimPaymentNo(tempMap.get("CLAIM_PAYMENT_NO")==null?"":tempMap.get("CLAIM_PAYMENT_NO").toString());
			tempBean.setSNo(tempMap.get("RESERVE_ID")==null?"":tempMap.get("RESERVE_ID").toString());
			tempBean.setSettlementStatus(tempMap.get("SETTLEMENT_STATUS")==null?"":tempMap.get("SETTLEMENT_STATUS").toString());
			tempBean.setTransactionType(tempMap.get("TRANS_TYPE")==null?"":tempMap.get("TRANS_TYPE").toString());
			tempBean.setTransactionNumber(tempMap.get("RECEIPT_NO")==null?"":tempMap.get("RECEIPT_NO").toString());
			tempBean.setDepartmentId(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
			String maxno="";
			try{
				query = "payment.select.count.allocatedYN";
				list=queryImpl.selectList(query, new String[]{tempBean.getPolicyContractNo(),tempBean.getClaimNo(),tempBean.getLayerNo()});
				if (!CollectionUtils.isEmpty(list)) {
					count = Integer.valueOf((list.get(0).get("ALLOCATEDYN") == null ? ""
							: list.get(0).get("ALLOCATEDYN").toString()));
				}
				
			if(count==0){
				tempBean.setAllocatedYN("N");
			}else if(count>=1){	
				tempBean.setAllocatedYN("Y");
			}
			query="GET_STATUS_OF_CLAIM";
			list=queryImpl.selectList(query, new String[] {tempBean.getPolicyContractNo(),tempBean.getClaimNo(),tempBean.getLayerNo()});
			if (!CollectionUtils.isEmpty(list)) {
				tempBean.setStatusofclaim((list.get(0).get("STATUS_OF_CLAIM") == null ? ""
						: list.get(0).get("STATUS_OF_CLAIM").toString()));
			}
			
			String que="claim.select.maxno";
			list=queryImpl.selectList(que, new String[] {tempBean.getClaimNo(),tempBean.getPolicyContractNo()});
			if (!CollectionUtils.isEmpty(list)) {
				maxno = ((list.get(0).get("MAXNO") == null ? ""
						: list.get(0).get("MAXNO").toString()));
			}
			}catch(Exception e){
				e.printStackTrace();
				log.debug("Exception "+e);
			}
			if(StringUtils.isNotBlank(tempBean.getInceptionDt())){
				if(Integer.valueOf(dropDowmImpl.Validatethree(req.getBranchCode(), req.getDate()))==0 ){
					tempBean.setTransOpenperiodStatus("N");
				}else if(!tempMap.get("RESERVE_ID").toString().equalsIgnoreCase(maxno)){
					tempBean.setTransOpenperiodStatus("N");
				}
				else
				{
					tempBean.setTransOpenperiodStatus("Y");
				}
				}
			finalList.add(tempBean);
			}
		
		res.setCommonResponse(finalList);
		res.setMessage("Success");
		res.setIsError(false);
	
		 }catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		res.setMessage("Failed");
		res.setIsError(true);
		}
		return res;	
	}

	@Override
	public String getReInsValue(GetReInsValueReq req) {
		String query="";
		String args[]=null;
		double result=0;
		String finalVal="";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<GetReInsValueListReq> req1 = new ArrayList<GetReInsValueListReq>();
	
		try{
			for(int i=0;i<req1.size();i++){
				if("This Trxn".equalsIgnoreCase(req1.get(i).getClaimPaymentNoList())){
					query="GET_RIP_VALUES";
					args= new String[5];
					args[0] = req.getProposalNo();
					args[1] = req.getPolicyContractNo();
					args[2] = req.getLayerNo();
					args[3] = req.getBranchCode();
					args[4] = req1.get(i).getPaymentCoverPlus(); //Reinstno
					list= queryImpl.selectList(query,args);
					if(list.size()>0){
						for(int j=0;j<list.size();j++){
							Map<String,Object> map = list.get(j);
							String type = map.get("REINST_TYPE")==null?"0":map.get("REINST_TYPE").toString().replaceAll(",", ""); 
							
							String totalNoCol = StringUtils.isBlank(req.getTotalBookedPremium())?"0":req.getTotalBookedPremium().replaceAll(",","");
							String amtPer = map.get("AMOUNT_PERCENT")==null?"0":map.get("AMOUNT_PERCENT").toString().replaceAll(",", "");
							String minAmt = map.get("MIN_AMOUNT_PERCENT")==null?"0":map.get("MIN_AMOUNT_PERCENT").toString().replaceAll(",", "");
							String time = map.get("MIN_TIME_PERCENT")==null?"0":map.get("MIN_TIME_PERCENT").toString().replaceAll(",", "");
							double AmountPer = Double.parseDouble(amtPer)/100;
							String val = getSumInsuredVal(req);
							String total = Double.toString(Double.parseDouble(req1.get(i).getClaimPaidOC().replaceAll(",", ""))/Double.parseDouble(val.replaceAll(",", "")));
							
							
							if("PRA".equalsIgnoreCase(type)){
								minAmt = Double.toString(Double.parseDouble(minAmt)/100);
								double amount= maxValue(Double.parseDouble(minAmt), Double.parseDouble(total));
								double ans = Double.parseDouble(StringUtils.isBlank(req.getTotalBookedPremium())?"0":req.getTotalBookedPremium().replaceAll(",",""))*amount ; 
								ans = ans*AmountPer;
								result +=ans;
							}
							else if("PRT".equalsIgnoreCase(type)){
								double dateCom = getDateDetails(req);
								double minTime = Double.parseDouble(time)/100;
								double amount= maxValue(dateCom, minTime);
								double ans = Double.parseDouble(totalNoCol)*Double.parseDouble(total)*amount ; 
								ans = ans*AmountPer;
								result +=ans;
							}
							else if("PRAT".equalsIgnoreCase(type)){
								minAmt = Double.toString(Double.parseDouble(minAmt)/100);
								double amount= maxValue(Double.parseDouble(minAmt), Double.parseDouble(total));
								 
								double ans = 0; 
								
								double dateCom = getDateDetails(req);
								double minTime = Double.parseDouble(time)/100;
								double amount1= maxValue(dateCom, minTime);
								double ans1 = Double.parseDouble(totalNoCol)*amount1 *amount; 
								ans = ans1*AmountPer;
								result +=ans;
							}
							else if("FREE".equalsIgnoreCase(type)){
								result+=AmountPer;
							}
						}
					}
					
				}
			}
			finalVal = fm.formatter(Double.toString(result));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return finalVal;
		
	}
	private String getSumInsuredVal(GetReInsValueReq req) {
		String val="";
		String query="";
		String args[]=null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			query= "COVER_SUM_INSURED_VAL";
			args = new String[7];
			args[0] = req.getProposalNo();
			args[1] = req.getLayerNo();
			args[2] = req.getBranchCode();
			args[3] = req.getProposalNo();
			args[4] = req.getLayerNo();
			args[5] = req.getPolicyContractNo();
			args[6] = req.getBranchCode();
			list= queryImpl.selectList(query,args);
			if (!CollectionUtils.isEmpty(list)) {
				 val = list.get(0).get("SUM_INSURED_VAL") == null ? ""
						: list.get(0).get("SUM_INSURED_VAL").toString();
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	private double maxValue(double minAmt, double rdsAmt) {
		try{
			if(minAmt<rdsAmt){
				minAmt = rdsAmt;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return minAmt;
	}
	private double getDateDetails(GetReInsValueReq req) {
		String query="";
		String[] args= null;
		String inceptionDate ="";
		double ans =0;
		String ExpDate="";
		String dateOfLoss="";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			query= "GET_DATE_OF_LOSS";
			args= new String[4];
			args[0] = req.getClaimNo();
			args[1] = req.getPolicyContractNo();
			args[2] = req.getLayerNo();
			args[3] = req.getBranchCode();
			list= queryImpl.selectList(query,args);
			if (!CollectionUtils.isEmpty(list)) {
				 dateOfLoss = list.get(0).get("DATE_OF_LOSS") == null ? ""
						: list.get(0).get("DATE_OF_LOSS").toString();
			}
			
			query="GET_RDS_DATE";
			args = new String[4];
			args[0] = req.getProposalNo();
			args[1] = req.getPolicyContractNo();
			args[2] = req.getLayerNo();
			args[3] = req.getBranchCode();
			list= queryImpl.selectList(query,args);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> map =list.get(i);
					inceptionDate = map.get("RSK_INCEPTION_DATE")==null?"":map.get("RSK_INCEPTION_DATE").toString();
						ExpDate =	 map.get("RSK_EXPIRY_DATE")==null?"":map.get("RSK_EXPIRY_DATE").toString();
				}
			}
			String format = "dd/MM/yyyy"; 
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			java.util.Date incep = sdf.parse(inceptionDate);
			java.util.Date exp = sdf.parse(ExpDate);
			java.util.Date lossDate = sdf.parse(dateOfLoss);
			double diffInDays = (double)( (exp.getTime() - incep.getTime()) / (1000 * 60 * 60 * 24) );
			diffInDays = diffInDays+1;
			double diff = (double)( (exp.getTime() - lossDate.getTime()) / (1000 * 60 * 60 * 24) );
			
			 ans =diff/diffInDays;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}





	@Override
	public ProposalNoRes savepaymentReciept(ProposalNoReq req) {
		
		ProposalNoRes response = new ProposalNoRes();
		String query="";
		String proposalNo="";
		String[] obj=null;
		try {
		if(StringUtils.isNotBlank(req.getProductId()) &&"1".equals(req.getProductId())){
			
			obj=new String[1];
			obj[0]=req.getContarctno();
			query="GET_FAC_PROPOSAL_NO";
		}else if("2".equals(req.getProductId())){
			
			obj=new String[2];
			obj[0]=req.getContarctno();
			obj[1]=req.getDepartmentId();
			query="GET_PRO_PROPOSAL_NO";
		}
		else if("3".equals(req.getProductId())){
			
			obj=new String[2];
			obj[0]=req.getContarctno();
			obj[1]=req.getLayerNo();
			query="GET_XOL_PROPOSAL_NO";
		}
		List<Map<String,Object>>list=queryImpl.selectList(query,obj);
		if(list!=null &&list.size()>0){
			Map<String,Object>map=list.get(0);
			proposalNo=map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
			if(!"2".equalsIgnoreCase(req.getProductId())){
			req.setDepartmentId(map.get("DEPT_ID")==null?"":map.get("DEPT_ID").toString());
			
			}
			
		}
		response.setCommonResponse(proposalNo);
	
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}

	@Override
	public ContractDetailsMode1Res saveContractDetailsMode1(ContractDetailsReq req) {
		ContractDetailsMode1Res response = new ContractDetailsMode1Res();
		List<ContractDetailsListMode1res> finalList = new ArrayList<ContractDetailsListMode1res>();
	    ContractDetailsListMode1res res = new ContractDetailsListMode1res();
		
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		
		try {

				args=new String[5];
				args[0]=req.getProposalNo();
				args[1]=req.getProductId();
				args[2]=req.getBranchCode();
				args[3]=req.getBranchCode();
				args[4]=req.getProposalNo();
				
				
				if("1".equalsIgnoreCase(req.getProductId())){
				
					query = "claim.select.facGetCliamQuery";
							}
				else{
					
					query = "claim.select.xolOrTeatyGetClimeQuery";
					
				}
				
				list = queryImpl.selectList(query, args);
				if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> contractDetails=(Map<String,Object>) list.get(i);
					res.setPolicyContractNo(contractDetails.get("RSK_CONTRACT_NO")==null?"":contractDetails.get("RSK_CONTRACT_NO").toString());
					String DepartmentId = (contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());

					if("1".equalsIgnoreCase(req.getProductId())){
						res.setSignedShare(contractDetails.get("SHARE_SIGNED")==null?"":contractDetails.get("SHARE_SIGNED").toString());
						res.setSumInsOSOC(contractDetails.get("SUM_INSURED_OUR_SHARE_OC")==null?"":fm.formatter(contractDetails.get("SUM_INSURED_OUR_SHARE_OC").toString()));
						res.setSumInsOSDC(contractDetails.get("SUM_INSURED_OUR_SHARE_DC")==null?"":fm.formatter(contractDetails.get("SUM_INSURED_OUR_SHARE_DC").toString()));						
						list =queryImpl.selectList("claim.select.tmasDeptName",new String[]{DepartmentId,req.getProductId(),req.getBranchCode()});
						if (!CollectionUtils.isEmpty(list)) {
							res.setDepartmentName(fm.formatter(list.get(0).get("TMAS_NAME") == null ? ""
									: list.get(0).get("TMAS_NAME").toString()));
						}
					}else
					{
						res.setSignedShare(contractDetails.get("RSK_SHARE_SIGNED")==null?"":contractDetails.get("RSK_SHARE_SIGNED").toString());
						res.setSumInsOSOC(contractDetails.get("RSK_LIMIT_OS_OC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OS_OC").toString()));
						res.setSumInsOSDC(contractDetails.get("RSK_LIMIT_OS_DC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OS_DC").toString()));
						res.setDepartmentName(contractDetails.get("TMAS_DEPARTMENT_NAME")==null?"":contractDetails.get("TMAS_DEPARTMENT_NAME").toString());
					}
					res.setFrom(contractDetails.get("INCP_DATE")==null?"":contractDetails.get("INCP_DATE").toString());
					res.setTo(contractDetails.get("EXP_DATE")==null?"":contractDetails.get("EXP_DATE").toString());
					res.setAcceptenceDate(contractDetails.get("RSK_ACCOUNT_DATE")==null?"":contractDetails.get("RSK_ACCOUNT_DATE").toString());	
					
					}
				finalList.add(res);
				response.setCommonResponse(finalList);
				response.setMessage("Success");
				response.setIsError(false);	
				}
				
				
		}
				catch (Exception e) {
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
	
	
		
	}
		return response;

}

	private String getCombinedClass(String branchCode, String productId, String departmentId) {
		String count="";
		try {
			//String query="SELECT CORE_COMPANY_CODE FROM TMAS_DEPARTMENT_MASTER WHERE BRANCH_CODE =? AND TMAS_PRODUCT_ID=? AND  TMAS_STATUS='Y' AND CORE_COMPANY_CODE IS NOT NULL AND TMAS_DEPARTMENT_ID IN(?) ";
			//log.info("Select Query==> " + query);
			List<Map<String,Object>> list=queryImpl.selectList("claim.select.coreCompanyName",  new String[]{branchCode,productId,departmentId});
			log.info("Select Query==> " + list);
			if(list!=null && list.size()>0){
			Map<String,Object>map=list.get(0);
			count=map.get("CORE_COMPANY_CODE")==null?"":map.get("CORE_COMPANY_CODE").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}




	@Override
	public ContractDetailsMode4Res saveContractDetailsMode4(ContractDetailsModeReq req) {
		ContractDetailsMode4Res response = new ContractDetailsMode4Res();
		List<ContractDetailsListMode4res> finalList = new ArrayList<ContractDetailsListMode4res>();
	    ContractDetailsListMode4res res = new ContractDetailsListMode4res();
	    String ClaimNo ="";
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean dataFlag = false;
		try {
//			if(StringUtils.isNotBlank(req.getClaimNo()))
//			{
//				list =queryImpl.selectList("claim.select.sumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
//			
//			
//			if (!CollectionUtils.isEmpty(list)) {
//				res.setSumOfPaidAmountOC(fm.formatter(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
//						: list.get(0).get("PAID_AMOUNT_OC").toString()));
//			}
//	
//		
//		list =queryImpl.selectList("claim.select.revSumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
//		if (!CollectionUtils.isEmpty(list)) {
//			res.setRevSumOfPaidAmt(fm.formatter(list.get(0).get("PAID_AMT") == null ? ""
//					: list.get(0).get("PAID_AMT").toString()));
//		}
//
//		
//		
//		
//	}
			
  			list=queryImpl.selectList("claim.select.claimEdit", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
  			
  			
  			for(int i=0; i<list.size();i++) {
  				
  				Map<String,Object> data=(Map<String,Object>) list.get(i);
					 ClaimNo = (data.get("CLAIM_NO")== null?"":data.get("CLAIM_NO").toString());
					res.setClaimNo(ClaimNo);
					res.setStatusofclaim(data.get("STATUS_OF_CLAIM")== null?"":data.get("STATUS_OF_CLAIM").toString());
					res.setDepartmentClass(data.get("COVER_LIMIT_DEPTID")== null?"":data.get("COVER_LIMIT_DEPTID").toString());
					res.setDateofLoss(data.get("LOSS_DATE")== null?"":data.get("LOSS_DATE").toString());
					res.setReportDate(data.get("REP_DATE")== null?"":data.get("REP_DATE").toString());
					res.setLossDetails(data.get("LOSS_DETAILS")== null?"":data.get("LOSS_DETAILS").toString());
					res.setCauseofLoss(data.get("CAUSE_OF_LOSS")== null?"":data.get("CAUSE_OF_LOSS").toString());
					res.setLocation(list.get(i).get("LOCATION")== null?"":list.get(i).get("LOCATION").toString());
					res.setLossEstimateOrigCurr(fm.formatter(data.get("LOSS_ESTIMATE_OC")== null?"":data.get("LOSS_ESTIMATE_OC").toString()));
					res.setLossEstimateOurshareOrigCurr(fm.formatter(data.get("LOSS_ESTIMATE_OS_OC")== null?"":data.get("LOSS_ESTIMATE_OS_OC").toString()));
					res.setExcRate(exchRateFormat(data.get("EXCHANGE_RATE")== null?"":data.get("EXCHANGE_RATE").toString()));
//					res.setLossEstimateOurShareUSD(data.get("LOSS_ESTIMATE_OS_DC")== null?"":data.get("LOSS_ESTIMATE_OS_DC").toString());
//					res.setAdviceUW(data.get("ADVICE_UW")== null?"":data.get("ADVICE_UW").toString());
//					res.setAdviceMangement(data.get("ADVICE_MANAGEMENT")== null?"":data.get("ADVICE_MANAGEMENT").toString());
					res.setRiRecovery(data.get("RI_RECOVERY")== null?"":data.get("RI_RECOVERY").toString());
//					res.setRiRecoveryAmountUSD(data.get("RI_RECOVERY_AMOUNT_DC")== null?"":data.get("RI_RECOVERY_AMOUNT_DC").toString());
//					res.setRecoveryfrom(data.get("RECOVERY_FROM")== null?"":data.get("RECOVERY_FROM").toString());
					res.setCreatedby(data.get("CREATED_BY")== null?"":data.get("CREATED_BY").toString());
					res.setCreatedDate(data.get("CREATED_DT")== null?"":data.get("CREATED_DT").toString());
//					res.setModifiedby(data.get("MODIFIED_BY")== null?"":data.get("MODIFIED_BY").toString());
//					res.setModifiedDate(data.get("MODIFIED_DT")== null?"":data.get("MODIFIED_DT").toString());
//		    		res.setUpdatedby(data.get("UPDATED_BY")== null?"":data.get("UPDATED_BY").toString());
//		    		res.setUpdatedDate(data.get("UPDATED_DT")== null?"":data.get("UPDATED_DT").toString());
		    		res.setCurrency(data.get("CURRENCY")== null?"":data.get("CURRENCY").toString());
		    		res.setCurrencyName(data.get("CURRENCY_NAME")== null?"":data.get("CURRENCY_NAME").toString());
		    		res.setRemarks(data.get("REMARKS")== null?"":data.get("REMARKS").toString());
//		    		res.setAdviceuwemail(data.get("ADVICE_UW_EMAILID")== null?"":data.get("ADVICE_UW_EMAILID").toString());
//		    		res.setAdvicemanagementemail(data.get("ADVICE_MGT_EMAILID")== null?"":data.get("ADVICE_MGT_EMAILID").toString());
		    		res.setRiskCode(data.get("RISK_CODE")== null?"":data.get("RISK_CODE").toString());
		    		res.setAccumulationCode(data.get("ACCUMULATION_CODE")== null?"":data.get("ACCUMULATION_CODE").toString());
		    		res.setEventCode(data.get("EVENT_CODE")== null?"":data.get("EVENT_CODE").toString());
		    		//res.setLayerNo(data.get("LAYER_NO")== null?"":data.get("LAYER_NO").toString());
		    		res.setInsuredName(data.get("INSURED_NAME")== null?"":data.get("INSURED_NAME").toString());
		    		res.setRecordFees(data.get("RECORD_FEES_CRE_RESERVE")== null?"":data.get("RECORD_FEES_CRE_RESERVE").toString());
		    		res.setSurveyorAdjesterPerOC(fm.formatter(data.get("SAF_100_OC")== null?"":data.get("SAF_100_OC").toString()));
		    		res.setSurveyorAdjesterOurShareOC(fm.formatter(data.get("SAF_OS_OC")== null?"":data.get("SAF_OS_OC").toString()));
		    		res.setOtherProfessionalPerOc(fm.formatter(data.get("OTH_FEE_100_OC")== null?"":data.get("OTH_FEE_100_OC").toString()));
		    		res.setProfessionalOurShareOc(fm.formatter(data.get("OTH_FEE_OS_OC")== null?"":data.get("OTH_FEE_OS_OC").toString()));
		    		res.setIbnrPerOc(fm.formatter(data.get("C_IBNR_100_OC")== null?"":data.get("C_IBNR_100_OC").toString()));
		    		res.setIbnrOurShareOc(fm.formatter(data.get("C_IBNR_OS_OC")== null?"":data.get("C_IBNR_OS_OC").toString()));
		    		res.setGrossLossFGU(fm.formatter(data.get("GROSSLOSS_FGU_OC")== null?"":data.get("GROSSLOSS_FGU_OC").toString()));
		    		res.setRecordIbnr(data.get("RECORD_IBNR")== null?"":data.get("RECORD_IBNR").toString());
		    		res.setCedentClaimNo(data.get("CEDENT_CLAIM_NO")== null?"":data.get("CEDENT_CLAIM_NO").toString());
		    		res.setReservePositionDate(data.get("RES_POS_DATE")== null?"":data.get("RES_POS_DATE").toString());
		    		res.setClaimdepartId(data.get("CLAIM_CLASS")== null?"":data.get("CLAIM_CLASS").toString());
		    		res.setSubProfitId(data.get("CLAIM_SUBCLASS")== null?"":data.get("CLAIM_SUBCLASS").toString());
		    		res.setReopenDate(data.get("REOPENED_DATE")== null?"":data.get("REOPENED_DATE").toString());
		    		res.setReputedDate(data.get("REPUDATE_DATE")== null?"":data.get("REPUDATE_DATE").toString());
		    		
				}
			
		
	     	list=queryImpl.selectList("GET_EDIT_ACC_DATA",new String[]{ClaimNo,req.getPolicyContractNo()});
	    
	     	for(int i=0; i<list.size();i++) {
	     		Map<String,Object> data=(Map<String,Object>) list.get(i);
					res.setRiskCode(data.get("RISK_CODE")==null?"":(data.get("RISK_CODE").toString()));
		    		res.setAccumulationCode(data.get("AGGREGATE_CODE")==null?"":(data.get("AGGREGATE_CODE").toString()));
		    		res.setEventCode(data.get("EVENT_CODE")==null?"":(data.get("EVENT_CODE").toString()));
	     	}
		    		
				
				finalList.add(res);
				response.setCommonResponse(finalList);
				response.setMessage("Success");
				response.setIsError(false);
		
			}catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
  		
	}
		return response;
	}
	private String exchRateFormat(String value) {
 		String output="0.00";
		if(StringUtils.isNotBlank(value))
		{
			System.out.println(value);
			double doublevalue=Double.parseDouble(value);
			DecimalFormat myFormatter = new DecimalFormat("#####.##########");
			output = myFormatter.format(doublevalue);
		}
		return output;
	}

	@Override
	public ContractDetailsMode5Res saveContractDetailsMode5(ContractDetailsModeReq req) {
		ContractDetailsMode5Res response = new ContractDetailsMode5Res();
		List<ContractDetailsListMode5res> finalList = new ArrayList<ContractDetailsListMode5res>();
	    ContractDetailsListMode5res res = new ContractDetailsListMode5res();
		
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean dataFlag = false;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				list =queryImpl.selectList("claim.select.sumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
			
			log.info("Select Query==>"+list);
			if (!CollectionUtils.isEmpty(list)) {
				res.setSumOfPaidAmountOC(fm.formatter(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
						: list.get(0).get("PAID_AMOUNT_OC").toString()));
			}
	
		
		list =queryImpl.selectList("claim.select.revSumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			res.setRevSumOfPaidAmt(fm.formatter(list.get(0).get("PAID_AMT") == null ? ""
					: list.get(0).get("PAID_AMT").toString()));
		}

		log.info("Select Query==>"+list);
		
		
	}

  			list=queryImpl.selectList("claim.select.getClaimReserveList", new String[] {req.getClaimNo(),req.getPolicyContractNo()});
  			log.info("Select Query=>"+list);
//  			
  			for(int i=0; i<list.size();i++) {
	     		Map<String,Object> data=(Map<String,Object>) list.get(i);
  			        res.setPaidAmountOrigcurr(data.get("PAID_AMOUNT_OC")==null?"":data.get("PAID_AMOUNT_OC").toString());
					res.setPaymentRequestNo(data.get("PAYMENT_REQUEST_NO")==null?"":data.get("PAYMENT_REQUEST_NO").toString());
					res.setLossEstimateRevisedOrigCurr(data.get("LOSS_ESTIMATE_REVISED_OC")==null?"":data.get("LOSS_ESTIMATE_REVISED_OC").toString());
					res.setClaimNoterecommendations(data.get("CLAIM_NOTE_RECOMM")==null?"":data.get("CLAIM_NOTE_RECOMM").toString());
					res.setPaymentReference(data.get("PAYMENT_REFERENCE")==null?"":data.get("PAYMENT_REFERENCE").toString());
					res.setAdviceTreasury(data.get("ADVICE_TREASURY")==null?"":data.get("ADVICE_TREASURY").toString());
					res.setDate(data.get("INCEPTION_DT")==null?"":data.get("INCEPTION_DT").toString());
					res.setPaidAmountUSD(data.get("PAID_AMOUNT_DC")==null?"":data.get("PAID_AMOUNT_DC").toString());
					res.setLossEstimateRevisedUSD(data.get("LOSS_ESTIMATE_REVISED_DC")==null?"":data.get("LOSS_ESTIMATE_REVISED_DC").toString());
				
				}
		
  		finalList.add(res);
		response.setCommonResponse(finalList);
	}catch (Exception e) {
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
		return response;
}

	@Override
	public ContractDetailsMode6Res saveContractDetailsMode6(ContractDetailsModeReq req) {
		ContractDetailsMode6Res response = new ContractDetailsMode6Res();
		List<ContractDetailsListMode6res> finalList = new ArrayList<ContractDetailsListMode6res>();
	    ContractDetailsListMode6res res = new ContractDetailsListMode6res();
		
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean dataFlag = false;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				list =queryImpl.selectList("claim.select.sumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
			
			log.info("Select Query==>"+list);
			if (!CollectionUtils.isEmpty(list)) {
				res.setSumOfPaidAmountOC(fm.formatter(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
						: list.get(0).get("PAID_AMOUNT_OC").toString()));
			}
	
		
		list =queryImpl.selectList("claim.select.revSumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			res.setRevSumOfPaidAmt(fm.formatter(list.get(0).get("PAID_AMT") == null ? ""
					: list.get(0).get("PAID_AMT").toString()));
		}

		log.info("Select Query==>"+list);
		
		
	}

  			//query=getQuery(DBConstants.CLAIM_SELECT_GETCLAIMUPDATELIST);
			list = queryImpl.selectList("claim.select.getClaimUpdateList", new String[] {req.getClaimNo(),req.getPolicyContractNo()});
  			log.info("Select Query====>"+list);
  			for(int i=0; i<list.size();i++) {
	     		Map<String,Object> data=(Map<String,Object>) list.get(i);
		 		res.setLossEstimateRevisedOrigCurr(data.get("LOSS_ESTIMATE_REVISED_OC")==null?"":data.get("LOSS_ESTIMATE_REVISED_OC").toString());
				res.setLossEstimateRevisedUSD(data.get("LOSS_ESTIMATE_REVISED_DC")==null?"":data.get("LOSS_ESTIMATE_REVISED_DC").toString());
				res.setUpdateReference(data.get("UPDATE_REFERENCE")==null?"":data.get("UPDATE_REFERENCE").toString());
				res.setCliamupdateDate(data.get("INCEPTION_DT")==null?"":data.get("INCEPTION_DT").toString());
				
				}
  			finalList.add(res);
  			response.setCommonResponse(finalList);
  		}catch (Exception e) {
  			log.error(e);
  			e.printStackTrace();
  			response.setMessage("Failed");
  			response.setIsError(true);
  		}
  			return response;
	  		
	}

	@Override
	public ContractDetailsMode7Res saveContractDetailsMode7(ContractDetailsModeReq req) {
		ContractDetailsMode7Res response = new ContractDetailsMode7Res();
		List<ContractDetailsListMode7res> finalList = new ArrayList<ContractDetailsListMode7res>();
	    ContractDetailsListMode7res res = new ContractDetailsListMode7res();
		
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean dataFlag = false;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				list =queryImpl.selectList("claim.select.sumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
			
			log.info("Select Query==>"+list);
			if (!CollectionUtils.isEmpty(list)) {
				res.setSumOfPaidAmountOC(fm.formatter(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
						: list.get(0).get("PAID_AMOUNT_OC").toString()));
			}
	
		
		list =queryImpl.selectList("claim.select.revSumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			res.setRevSumOfPaidAmt(fm.formatter(list.get(0).get("PAID_AMT") == null ? ""
					: list.get(0).get("PAID_AMT").toString()));
		}

		log.info("Select Query==>"+list);
		
	
	}
			list = queryImpl.selectList("claim.select.getClaimReviewQuery", new String[] {req.getClaimNo(),req.getPolicyContractNo()});
			for(int i=0; i<list.size();i++) {
	     		Map<String,Object> data=(Map<String,Object>) list.get(i);
  			
				res.setReviewDate(data.get("REVIEW_DT")==null?"":data.get("REVIEW_DT").toString());
				res.setReviewDoneBy(data.get("REVIEW_DONE_BY")==null?"":data.get("REVIEW_DT").toString());
				
				}
			
			finalList.add(res);
  			response.setCommonResponse(finalList);
  		}catch (Exception e) {
  			log.error(e);
  			e.printStackTrace();
  			response.setMessage("Failed");
  			response.setIsError(true);
  		}
  			return response;
	}

	@Override
	public ContractDetailsMode10Res saveContractDetailsMode10(ContractDetailsModeReq req) {
		ContractDetailsMode10Res response = new ContractDetailsMode10Res();
		List<ContractDetailsListMode10res> finalList = new ArrayList<ContractDetailsListMode10res>();
	    ContractDetailsListMode10res res = new ContractDetailsListMode10res();
		
		String query="";
		String[] args =null;
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		boolean dataFlag = false;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				list =queryImpl.selectList("claim.select.sumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
			
			log.info("Select Query==>"+list);
			if (!CollectionUtils.isEmpty(list)) {
				res.setSumOfPaidAmountOC(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
						: fm.formatter(list.get(0).get("PAID_AMOUNT_OC").toString()));
			}
	
		
		list =queryImpl.selectList("claim.select.revSumPaidAmt", new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			res.setRevSumOfPaidAmt(list.get(0).get("PAID_AMT") == null ? ""
					: fm.formatter(list.get(0).get("PAID_AMT").toString()));
		}
	}

			if(StringUtils.isNotBlank(req.getClaimNo())){
				list = queryImpl.selectList("claim.select.lossEstimateRevisedOc", new String[]{req.getClaimNo(),req.getPolicyContractNo(),req.getClaimNo(),req.getPolicyContractNo()});
				if (!CollectionUtils.isEmpty(list)) {
					res.setLossEstimateRevisedOrigCurr(list.get(0).get("LOSS_ESTIMATE_REVISED_OC") == null ? ""
							: fm.formatter(list.get(0).get("LOSS_ESTIMATE_REVISED_OC").toString()));
				}
			}
			
		
	
			finalList.add(res);
  			response.setCommonResponse(finalList);
  		}catch (Exception e) {
  			log.error(e);
  			e.printStackTrace();
  			response.setMessage("Failed");
  			response.setIsError(true);
  		}
  			return response;
	}

	@Override
	public AllocationListRes saveAllocationList(AllocationListReq req) {
		AllocationListRes response = new AllocationListRes();
		
		
		
		List<Map<String,Object>> date=new ArrayList<Map<String,Object>>();
		Double a=0.0;
		try{
			date = queryImpl.selectList("claim_allocation_list", new String[]{req.getContractNo(),req.getTransactionNumber(),req.getContractNo(),req.getTransactionNumber()});

				if (date.size()>0) {
					for (int i = 0; i < date.size(); i++) {
						Map<String,Object> resMap = date.get(i);
						a=a+Double.parseDouble(resMap.get("PAID_AMOUNT")==null?"":resMap.get("PAID_AMOUNT").toString());
					}
				}
				if(a>0){
					response.setCommonResponse(fm.formatter(Double.toString(a)));
					}
					else{
						response.setCommonResponse("");	//TotalAmount
					}
			
	  			

		}catch(Exception e){
			log.error(e);
  			e.printStackTrace();
  			response.setMessage("Failed");
  			response.setIsError(true);
		}


		return response;
	
	}

	@Override
	public ClaimListRes saveclaimlist(ClaimListReq req) {
		ClaimListRes response = new ClaimListRes();
		List<ClaimRes> finalList = new ArrayList<ClaimRes>();
		ClaimRes res = new ClaimRes();

		List<Map<String, Object>> allocists=new ArrayList<Map<String, Object>>();
		//List<ClaimBean> cliamlists=new ArrayList<ClaimBean>();
		String query="";
		try{
			if(req.getFlag().equalsIgnoreCase("claim")){
				query = "partial.claim.select.claim.claimmaster";
				
				//query=getQuery(DBConstants.PARTIAL_CLAIM_SELECT_GETCLAIMMASTERLIST);
			}
			else{
				//query=getQuery(DBConstants.CLAIM_SELECT_CLAIMTABLELIST_CLAIMMASTER);
				query =" claim.select.claim.claimmaster";
			}
			if("S".equalsIgnoreCase(req.getSearchType())){
				if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
					query +=" AND UPPER(CUSTOMER_NAME) LIKE UPPER('%"+req.getCompanyNameSearch()+"%')";
				}
				if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
					query +=" AND UPPER(BROKER_NAME) LIKE UPPER('%"+req.getBrokerNameSearch()+"%')";
				}
				if(StringUtils.isNotBlank(req.getContractNoSearch())){
					query +=" AND CONTRACT_NO LIKE ('%"+req.getContractNoSearch()+"%')";
				}
				if(StringUtils.isNotBlank(req.getClaimNoSearch())){
					query +=" AND CLAIM_NO LIKE ('%"+req.getClaimNoSearch()+"%')";
				}
				if(StringUtils.isNotBlank(req.getDateOfLossSearch())){
					query +=" AND DATE_OF_LOSS LIKE ('%"+req.getDateOfLossSearch()+"%')";
				}
				if(StringUtils.isNotBlank(req.getClaimStatusSearch())){
					query +=" AND UPPER(STATUS_OF_CLAIM) LIKE UPPER('%"+req.getClaimStatusSearch()+"%')";
				}
				}else{
					req.setCompanyNameSearch("");
					req.setBrokerNameSearch("");
					req.setDateOfLossSearch("");
					req.setClaimStatusSearch("");
					req.setContractNoSearch("");
					req.setClaimNoSearch("");
				}
		
		query="claim.select.date";
			log.info("Select Query====>"+query);
		String arg[]=new String[1];
		  arg[0]=req.getBranchCode();
		allocists=queryImpl.selectList(query,arg);
		for(int i=0 ; i<allocists.size() ; i++) {
			Map<String,Object> tempMap = (Map<String,Object>) allocists.get(i);
			ClaimRes tempBean=new ClaimRes();
			tempBean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
			tempBean.setDateofLoss(tempMap.get("DATE_OF_LOSS")==null?"":tempMap.get("DATE_OF_LOSS").toString());
			tempBean.setCreatedDate(tempMap.get("CREATED_DATE")==null?"":tempMap.get("CREATED_DATE").toString());
			tempBean.setStatusofclaim(tempMap.get("STATUS_OF_CLAIM")==null?"":tempMap.get("STATUS_OF_CLAIM").toString());
			tempBean.setPolicyContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
			tempBean.setEditMode(tempMap.get("editview")==null?"":tempMap.get("editview").toString());
			tempBean.setLayerNo(tempMap.get("layer_no")==null?"":tempMap.get("layer_no").toString());
			tempBean.setProposalNo(tempMap.get("Proposal_no")==null?"":tempMap.get("Proposal_no").toString());
			tempBean.setCedingcompanyName(tempMap.get("Customer_name")==null?"":tempMap.get("Customer_name").toString());
			tempBean.setBrokerName(tempMap.get("Broker_name")==null?"":tempMap.get("Broker_name").toString());
			tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
			tempBean.setExpiryDate(tempMap.get("Expiry_date")==null?"":tempMap.get("Expiry_date").toString());
			tempBean.setProductId(tempMap.get("Product_id")==null?"":tempMap.get("Product_id").toString());
			tempBean.setProductName(tempMap.get("TMAS_PRODUCT_NAME")==null?"":tempMap.get("TMAS_PRODUCT_NAME").toString());
			tempBean.setDepartmentId(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
			int count= Integer.valueOf(dropDowmImpl.Validatethree(req.getBranchCode(), tempBean.getCreatedDate()));
			int claimpaymentcount= Integer.valueOf(getCliampaymnetCount(tempBean.getClaimNo(),tempBean.getPolicyContractNo()));
			
			if(count!=0 && claimpaymentcount ==0 ){
				tempBean.setDeleteStatus("Y");
			}
			finalList.add(tempBean);
		}
		
	}catch(Exception e){
		log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	
	}
	
	return response;
	
	}
	@Override
	public GetShortnameRes getShortname(String branchcode) {
		GetShortnameRes response = new GetShortnameRes();
		String Short="";
		try {
		  List<Map<String, Object>> list = queryImpl.selectList("GET_SHORT_NAME", new String[] {branchcode});
		  if (!CollectionUtils.isEmpty(list)) {
				Short = ((list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
						: list.get(0).get("COUNTRY_SHORT_NAME").toString()));
			}
		  response.setCommonResponse(Short);		
	}catch(Exception e){
		log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	
	}
	
	return response;
	}
  //Mode2
	@Override
	public InsertCliamDetailsMode2Res insertCliamDetailsMode2(InsertCliamDetailsMode2Req req) {
		InsertCliamDetailsMode2Res response = new InsertCliamDetailsMode2Res();
		
		String query="";
		String[] arg =null;
		String[] args =null;
		try {
			 arg = new String[56];
			 arg[0]=req.getStatusofClaim();
			 arg[1]=req.getDateofLoss();
			 arg[2]=req.getReportDate();
			 arg[3]=req.getLossDetails();
			 arg[4]=req.getCauseofLoss();
			 arg[5]=req.getCurrecny();
			 arg[6]=req.getLossEstimateOrigCurr();
			 arg[7]=req.getLossEstimateOurShareOrigCurr();
			 arg[8]=req.getExcRate();
			 arg[9]=GetDesginationCountry(req.getLossEstimateOurShareOrigCurr(), req.getExcRate());
			 arg[10]=req.getAdviceUW()==null?"":req.getAdviceUW();
			 arg[11]=req.getAdviceMangement()==null?"":req.getAdviceMangement();
			 arg[12]=req.getRiRecovery();
			 arg[13]="0";
			 arg[14]=req.getRecoveryFrom();
			 arg[15]=req.getCreatedBy();
			 arg[16]=req.getCreatedDate();			
			 arg[17]=req.getLocation();
			 arg[18]=req.getRemarks();
			 arg[19]=req.getAdviceUwEmail()==null?"":req.getAdviceUwEmail();
			 arg[20]=req.getAdviceManagementEmail()==null?"":req.getAdviceManagementEmail();
			 arg[21]=req.getRiskCode()==null?"":req.getRiskCode();
			 arg[22]=req.getAccumulationCode()==null?"":req.getAccumulationCode();
			 arg[23]=req.getEventCode()==null?"":req.getEventCode();
			
			
			 arg[25]=req.getDepartmentId();
			 arg[24]=req.getInsuredName(); 
			
			
			 arg[26]=req.getBranchCode();
			 arg[27]=req.getLoginId();
			 arg[28]=req.getRecordFees()==null?"":req.getRecordFees();
			 arg[29]=req.getSurveyorAdjesterPerOC()==null?"0":req.getSurveyorAdjesterPerOC();
			 arg[30]=StringUtils.isEmpty(req.getSurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterPerOC(),req.getExcRate());
			 arg[31]=req.getOtherProfessionalPerOc()==null?"0":req.getOtherProfessionalPerOc();
			 arg[32]=StringUtils.isEmpty(req.getOtherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getOtherProfessionalPerOc(),req.getExcRate());
			 arg[33]=req.getIbnrPerOc()==null?"0":req.getIbnrPerOc();
			 arg[34]=StringUtils.isEmpty(req.getIbnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrPerOc(),req.getExcRate());
			 arg[35]=req.getRecordIbnr();
			 arg[36]=req.getCedentClaimNo();
			 arg[37]=req.getSurveyorAdjesterOurShareOC()==null?"0":req.getSurveyorAdjesterOurShareOC();
			 arg[38]=req.getProfessionalOurShareOc()==null?"0":req.getProfessionalOurShareOc();
			 arg[39]=req.getIbnrOurShareOc()==null?"0":req.getIbnrOurShareOc();
			 arg[40]=StringUtils.isEmpty(req.getSurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterOurShareOC(),req.getExcRate());
			 arg[41]=StringUtils.isEmpty(req.getProfessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getProfessionalOurShareOc(),req.getExcRate());
			 arg[42]=StringUtils.isEmpty(req.getIbnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrOurShareOc(),req.getExcRate());

			 arg[43]=StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate());
			 arg[44]=req.getReOpenDate()==null?"":req.getReOpenDate();
			 arg[45]=req.getGrossLossFGU()==null?"":req.getGrossLossFGU();
			 if("2".equalsIgnoreCase(req.getProductId())){
				 arg[46]=req.getClaimdepartId();
			 }else if("1".equalsIgnoreCase(req.getProductId())){
				 arg[46]=req.getDepartmentId();
			 }else if("3".equalsIgnoreCase(req.getProductId())){
				 arg[46]=req.getDepartmentClass(); //
			 }
			 arg[47]=req.getSubProfitId()==null?"D":req.getSubProfitId();
			 arg[48]=req.getReservePositionDate();
			 arg[49] = req.getDepartmentClass();
			 arg[50] = req.getProposalNo(); //
			 arg[51] = req.getProductId();
			 arg[52]=req.getReOpenDate()==null?"":req.getReOpenDate();
			 arg[53]=req.getPolicyContractNo().trim();
			 arg[54]=req.getClaimNo();
			 arg[55]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			if(StringUtils.isEmpty(req.getClaimNo()))
			{
			req.setClaimNo(getSequence("ClaimBooking",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getCreatedDate()));
			arg[54]=req.getClaimNo();
			
				query="claim.insert.cliamDetails";
				
				queryImpl.updateQuery(query, arg); //CliamDetailsArugumentsmode2 arg
				
				//Mode 7 arg
				  args=new String[31];
				  args[0] = GetSl_no(req.getClaimNo(),req.getPolicyContractNo());
				  args[1]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();		
				  args[2]=req.getLossEstimateOurShareOrigCurr();
				  args[3]=GetDesginationCountry(req.getLossEstimateOurShareOrigCurr(),req.getExcRate());
				  args[4]="Inserted at the time of Claim Registration";
				  args[5]=req.getCreatedDate();
				  args[6]=req.getClaimNo();           
				  args[7]=req.getPolicyContractNo();
				  args[8]="Inserted at the time of Claim Registration";
				 
				 args[9]=req.getBranchCode();
				 args[10]=req.getLoginId();
				 args[11]=req.getSurveyorAdjesterPerOC()==null?"0":req.getSurveyorAdjesterPerOC();
				 args[12]=StringUtils.isEmpty(req.getSurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterPerOC(),req.getExcRate());
				 args[13]=req.getOtherProfessionalPerOc()==null?"0":req.getOtherProfessionalPerOc();
				 args[14]=StringUtils.isEmpty(req.getOtherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getOtherProfessionalPerOc(),req.getExcRate());
				 args[15]=req.getIbnrPerOc()==null?"0":req.getIbnrPerOc();
				 args[16]=StringUtils.isEmpty(req.getIbnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrPerOc(),req.getExcRate());
				 args[17]=req.getSurveyorAdjesterOurShareOC()==null?"0":req.getSurveyorAdjesterOurShareOC();
				 args[18]=req.getProfessionalOurShareOc()==null?"0":req.getProfessionalOurShareOc();
				 args[19]=req.getIbnrOurShareOc()==null?"0":req.getIbnrOurShareOc();
				args[20]=StringUtils.isEmpty(req.getSurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterOurShareOC(),req.getExcRate());
					args[21]=StringUtils.isEmpty(req.getProfessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getProfessionalOurShareOc(),req.getExcRate());
					args[22]=StringUtils.isEmpty(req.getIbnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrOurShareOc(),req.getExcRate());

				args[23]=req.getLossEstimateOrigCurr();
				 args[24]=StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate());
				 args[25]=req.getRecordFees();
				 args[26]=req.getRecordIbnr();
				args[27]=req.getExcRate();
				if(StringUtils.isBlank(req.getTotalReserveOSOC())){
					double total = Double.parseDouble(StringUtils.isBlank(req.getSurveyorAdjesterOurShareOC())?"0":req.getSurveyorAdjesterOurShareOC().replaceAll(",", ""))+
					Double.parseDouble(StringUtils.isBlank(req.getProfessionalOurShareOc())?"0":req.getProfessionalOurShareOc().replaceAll(",", ""))+
					Double.parseDouble(StringUtils.isBlank(req.getLossEstimateOurShareOrigCurr())?"0":req.getLossEstimateOurShareOrigCurr().replaceAll(",", ""));
					req.setTotalReserveOSOC(Double.toString(total));
				}
				 args[28]=req.getTotalReserveOSOC();
				 args[29]=StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate());
				 args[30]=req.getReservePositionDate();
			
				
				query = "claim.insert.getUpdationQuery";
				queryImpl.updateQuery(query, args); 
				
				
				
				insertAggregate(req);
				}
				else 
				{
					
					query="claim.update.cliamDetailsUpdate";
					
					
					queryImpl.updateQuery(query, arg); 
					
					insertAggregate(req);
				}
				response.setMessage("Success");
				response.setIsError(false);
				response.setClaimNo(req.getClaimNo());	
				} catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
		return response;
	}
	public synchronized String getSequence(String type,String productID,String departmentId,String branchCode, String proposalNo,String date){ 
	
		String seqName="";
		try{
			
			String query="dropdowm.getseqno";
			
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{type,productID,departmentId,branchCode,proposalNo,date});
			if (!CollectionUtils.isEmpty(list)) {
				seqName = ((list.get(0).get("SEQNO") == null ? ""
						: list.get(0).get("SEQNO").toString()));
			}
		
			log.info("Result==> " + seqName);
			
		}catch(Exception e){
			e.printStackTrace();
			log.debug("Exception @ {" + e + "}");
		}

		return seqName;
	}

	public static String GetDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String valu="0";
		if (StringUtils.isNotBlank(limitOrigCur)&& Double.parseDouble(limitOrigCur) != 0) {
			double originalCountry = Double.parseDouble(limitOrigCur)/ Double.parseDouble(ExchangeRate);
			DecimalFormat myFormatter = new DecimalFormat("###0.00");
			final double dround = Math.round(originalCountry * 100.0) / 100.0;
			valu = myFormatter.format(dround);
		}
		return valu;
	}
	private String GetSl_no(final String claimNo,final String policyContractNo) {
		String result="";
		
	
		String query = "claim.select.maxSnoDTB3";
		List<Map<String, Object>> list = queryImpl.selectList(query, new String[]{claimNo,policyContractNo});

		if (!CollectionUtils.isEmpty(list)) {
			result = list.get(0).get("SL_NO") == null ? "" : list.get(0).get("SL_NO").toString();
		}
		
	
		return result;
	}
	public int insertAggregate(final InsertCliamDetailsMode2Req req) {
		int status = 0;
		
		String query="";
		String[] args =null;
		int spresult=0;
		try {
			query="INSERT_ACCUMULATION_DATA";
			
			args = new String[13];
			args[0]=req.getClaimNo();
			args[1]=req.getPolicyContractNo();
			args[2]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			args[3]=req.getDepartmentId();
			args[4]=req.getRiskCode();
			args[5]=req.getAccumulationCode();
			args[6]=req.getEventCode();
			args[7]=req.getClaimNo();
			args[8]=req.getPolicyContractNo();
			args[9]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			args[10]=req.getDepartmentId();
			args[11]=req.getBranchCode();
			args[12]=req.getLoginId();
			queryImpl.updateQuery(query, args); 
			
			
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	public String getDuplicateCedentClaim(InsertCliamDetailsMode2Req req) {
		List<Map<String,Object>>list=null;
		String claimno="";
		try{
			String query="GET_DUPLICATE_CEDENT_NO_LIST";
			String args[] = null;
			args = new String[] {req.getBranchCode(), req.getProposalNo(),req.getCedentClaimNo() };
			
			if(StringUtils.isNotBlank(req.getClaimNo())){
				query="GET_DUPLICATE_CEDENT_NO_LIST1";
				args = new String[] {req.getBranchCode(), req.getProposalNo(),req.getCedentClaimNo(),req.getClaimNo()};
				
				
			}
			list=queryImpl.selectList(query, args);
			if(list!=null && list.size()>0) {
				claimno=list.get(0).get("CLAIM_NO")==null?"":list.get(0).get("CLAIM_NO").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return claimno;
	}
	public boolean BusinessValidation(InsertCliamDetailsMode2Req req, int mode){
		boolean businesFlag=false;
		InsertCliamDetailsMode8Req req1 = new InsertCliamDetailsMode8Req();
		req1.setPolicyContractNo(req.getPolicyContractNo());
		if(mode==3)
		{
			
			if(Validation.ValidateTwo(getClaimDate(req1,3),req.getDateofLoss()).equalsIgnoreCase("Invalid"))
			{
				businesFlag=true;
			}
			
		}
		return businesFlag;
}
	public boolean BusinessValidation(InsertCliamDetailsMode3Req formObj, int mode){
		boolean businesFlag=false;
		double Amount=0;
		 if(mode==7)
			{
				Amount=businessValidaion(formObj,mode);
				if(Double.parseDouble(formObj.getPaidClaimOs())>Amount)
				{
					businesFlag=true;
				}
			}else if(mode==8)
			{
				Amount=businessValidaion(formObj,mode);
				if(Double.parseDouble(formObj.getSurveyorfeeos())>Amount)
				{
					businesFlag=true;
				}
			}else if(mode==9)
			{
				Amount=businessValidaion(formObj,mode);
				if(Double.parseDouble(formObj.getOtherproffeeos())>Amount)
				{
					businesFlag=true;
				}
			}
	
		return businesFlag;
}
	public double businessValidaion(final InsertCliamDetailsMode3Req formObj,final int mode) {
		double amount=0.0;
		String query="";
		List<Map<String,Object>>list=null;
		try {
		if(mode==7){
	
				if("edit".equalsIgnoreCase(formObj.getPaymentFlag())){
					query="CLAIM_LOSS_ESTIMATE_PAID_DIFFERENCE_EDIT";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getClaimPaymentNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("LOSS_ESTIMATE_DIFF") == null ? ""
								: list.get(0).get("LOSS_ESTIMATE_DIFF").toString());
					}
					

				}else{
					query="claim.loss.estimate.paid.difference";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("LOSS_ESTIMATE_DIFF") == null ? ""
								: list.get(0).get("LOSS_ESTIMATE_DIFF").toString());
					}
					
				}
				
			} 
		
		
		else if(mode==8){
				if("edit".equalsIgnoreCase(formObj.getPaymentFlag())){
					query="CLAIM_SAF_OS_SUM_DIFFERENCE_EDIT";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getClaimPaymentNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("SAF_OS_DIFF") == null ? ""
								: list.get(0).get("SAF_OS_DIFF").toString());
					}
					
					

				}else{
					query="claim.saf.os.sum.difference";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("SAF_OS_DIFF") == null ? ""
								: list.get(0).get("SAF_OS_DIFF").toString());
					}
					
					}
				}
		else if(mode==9){
		
				if("edit".equalsIgnoreCase(formObj.getPaymentFlag())){
					query="CLAIM_OTHER_FEE_OS_SUM_EDIT";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getClaimPaymentNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("SAF_OS_DIFF") == null ? ""
								: list.get(0).get("SAF_OS_DIFF").toString());
					}
				

				}else{
					query="claim.other.fee.os.sum.difference";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("SAF_OS_DIFF") == null ? ""
								: list.get(0).get("SAF_OS_DIFF").toString());
					}
					
					}
		}} catch(Exception e) {
			e.printStackTrace();
		}
		
		return amount;
	}

	
	public String getClaimDate(InsertCliamDetailsMode8Req req,int mode)
	{
		List<Map<String,Object>>list=null;
		String date="";
		String query = "";
		try{
			if(mode==1)
			{
				 query="claim.select.getInsDate";
				list=queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getClaimNo()});
				if(list!=null && list.size()>0) {
					date=list.get(0).get("INS_DATE")==null?"":list.get(0).get("INS_DATE").toString();
				}
				
			}else if(mode==2)
			{
				 query="claim.select.getLossDate";
				 list=queryImpl.selectList(query, new String[]{req.getClaimNo()});
					if(list!=null && list.size()>0) {
						date=list.get(0).get("LOSS_DATE")==null?"":list.get(0).get("LOSS_DATE").toString();
					}
				
			}else if(mode==3)
			{
				 query="claim.select.getContInsDate";
				list=queryImpl.selectList(query, new String[]{req.getPolicyContractNo(),req.getPolicyContractNo()});
				if(list!=null && list.size()>0) {
					date=list.get(0).get("INS_DATE")==null?"":list.get(0).get("INS_DATE").toString();
				}else if(mode==4)
				{
				 query="claim.select.getLastPayDt";
				 list=queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getPolicyContractNo()});
					if(list!=null && list.size()>0) {
						date=list.get(0).get("PAY_DT")==null?"":list.get(0).get("PAY_DT").toString();
					}
				
			}
			else if(mode==5)
			{
				 query="claim.lost.reserve.updateDate";
				 list=queryImpl.selectList(query, new String[]{req.getPolicyContractNo(),req.getClaimNo(),req.getBranchCode()});
					if(list!=null && list.size()>0) {
						date=list.get(0).get("INCEPTION_DATE")==null?"":list.get(0).get("INCEPTION_DATE").toString();
					}
			}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return date;
	}

	@Override
	public InsertCliamDetailsMode8Res insertCliamDetailsMode8(InsertCliamDetailsMode8Req req) {
		InsertCliamDetailsMode8Res response = new InsertCliamDetailsMode8Res();
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		
		String query="";
		String[] arg =null;
		try {
			
			
			
		if("Yes".equalsIgnoreCase(req.getReverseClaimYN()))
		{
			
			req.setExcRate(getExcRateForCliam(req.getClaimNo(),req.getPolicyContractNo()));

			  arg=new String[31]; //CliamDetailsArugumentsmode8
			  arg[1]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			  arg[2]=req.getUpdateRivisedoriginalCur();
			  arg[3]=GetDesginationCountry(req.getUpdateRivisedoriginalCur(),req.getExcRate());
			  arg[4]=req.getUpdateReference();
			  arg[5]=req.getCliamupdateDate();
			  arg[6]=req.getClaimNo();           
			  arg[7]=req.getPolicyContractNo();
			  arg[8]=req.getRemarks();
			  arg[9]=req.getBranchCode();
			  arg[10]=req.getLoginId();
			  arg[11]=req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC();
			  arg[12]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate());
			  arg[13]=req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc();
			  arg[14]=StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate());
			  arg[15]=req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc();
			  arg[16]=StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate());
			  arg[17]=req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC();
			  arg[18]=req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc();
			  arg[19]=req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc();
			  arg[20]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate());
			  arg[21]=StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate());
			  arg[22]=StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate());
			  arg[23]=req.getLossEstimateOrigCurr();
			  arg[24]=StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate());
			  arg[25]=req.getUpdaterecordFees();
			  arg[26]=req.getUpdaterecordIbnr();
			  arg[27]=req.getExcRate();
			  arg[28]=req.getTotalReserveOSOC();
			  arg[29]=StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate());
			  arg[30]=req.getReservePositionDate();
			
			
			arg[0] = GetSl_no(req.getClaimNo(),req.getPolicyContractNo());
			query="claim.insert.getUpdationQuery";
			queryImpl.updateQuery(query, arg); 
			
			//AfterInsert()
			
			
					
		}else
		{
			req.setExcRate(getExcRateForCliam(req.getClaimNo(),req.getPolicyContractNo()));
			
			//CliamDetailsArugumentsmode12
			 arg=new String[31];
			  arg[1]="0";
			  arg[2]="0";
			  arg[3]=GetDesginationCountry("0",req.getExcRate());
			  arg[4]=req.getUpdateReference()==null?"":req.getUpdateReference();
			  arg[5]=req.getCliamupdateDate();
			  arg[6]=req.getClaimNo();           
			  arg[7]=req.getPolicyContractNo();
			  arg[8]=req.getRemarks();
			  arg[9]=req.getBranchCode();
			  arg[10]=req.getLoginId();
			  arg[11]=req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC();
			  arg[12]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate());
			  arg[13]=req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc();
			  arg[14]=StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate());
			  arg[15]=req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc();
			  arg[16]=StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate());
			  arg[17]=req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC();
			  arg[18]=req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc();
			  arg[19]=req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc();
			  arg[20]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate());
			  arg[21]=StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate());
			  arg[22]=StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate());
			  arg[23]=req.getLossEstimateOrigCurr();
			  arg[24]=StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate());
			  arg[25]=req.getUpdaterecordFees()==null?"No":req.getUpdaterecordFees();
			  arg[26]=req.getUpdaterecordIbnr()==null?"No":req.getUpdaterecordIbnr();
			  arg[27]=req.getExcRate();
			  arg[28]=req.getTotalReserveOSOC()==null?"0":req.getTotalReserveOSOC();
			  arg[29]=StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate());
			  arg[30]=req.getReservePositionDate();
			  
			  
			arg[4]="Inserted at the time of Claim Closure";
			arg[5] =req.getClaimclosedDate();
			arg[30] =req.getClaimclosedDate();
			arg[0]=GetSl_no(req.getClaimNo(),req.getPolicyContractNo());
			
			query="claim.insert.getUpdationQuery";
			queryImpl.updateQuery(query, arg); 				
		
			query="claim.update.closeClaim";
			queryImpl.updateQuery(query, new String[]{"Closed",req.getClaimclosedDate(),req.getPolicyContractNo(),req.getClaimNo()}); 	
	
		}
//		query = "claim.select.Ri_recovery";
//		
//		list = queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getPolicyContractNo()});
//		if (!CollectionUtils.isEmpty(list)) {
//			req.setRiRecovery(list.get(0).get("RI_RECOVERY") == null ? ""
//					: list.get(0).get("RI_RECOVERY").toString());
//		
//		}
		response.setMessage("Success");
		response.setIsError(false);
		}
		
		 catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
				return response;
			}

private String getExcRateForCliam(final String claimNo,final String policyContractNo) {
	List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
	String excRate="";
	String query="claim.select.excRate";
	result=queryImpl.selectList(query,new String[]{claimNo, policyContractNo});
	if (!CollectionUtils.isEmpty(result)) {
		excRate =(result.get(0).get("EXCHANGE_RATE") == null ? ""
				: result.get(0).get("EXCHANGE_RATE").toString());
	
	}
	
	return  excRate;
}
public boolean BusinessValidation(InsertCliamDetailsMode8Req req, int mode){
	boolean businesFlag=false;
	String status="";
	if(mode==6){
		 status=getClaimStatus(req);		
		if("Closed".equalsIgnoreCase(status)){
			businesFlag=true;
		}
		
		}
	else if(mode==10){
		 status=getClaimStatus(req);		
		if("Closed".equalsIgnoreCase(status)){
			businesFlag=true;
		}
		}
	
	return businesFlag;
}
public String getClaimStatus(InsertCliamDetailsMode8Req req) {
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	
	String status="";
	try{
	String query="claim.select.claimstatus";
	list = queryImpl.selectList(query, new String[] {req.getPolicyContractNo(),req.getClaimNo()});
	if (!CollectionUtils.isEmpty(list)) {
		status =(list.get(0).get("STATUS_OF_CLAIM") == null ? ""
				: list.get(0).get("STATUS_OF_CLAIM").toString());
	
	}
	
	
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return  status;
}

@Override
public InsertCliamDetailsMode3Res insertCliamDetailsMode3(InsertCliamDetailsMode3Req req) {
	InsertCliamDetailsMode3Res response = new InsertCliamDetailsMode3Res();
	boolean checking = true;
	String [] arg=null;
	String [] args=null;
	String query="";
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	try {
	if("new".equalsIgnoreCase(req.getPaymentFlag())){
	 checking=ModeValidation(req.getPaymentRequestNo(),req.getClaimNo());
	}
	if(checking)
	{
		if("new".equalsIgnoreCase(req.getPaymentFlag())){
		
			req.setClaimPaymentNo(fm.getSequence("ClaimPayment",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getDate()));
		
		//CliamDetailsAruguments(req,mode);
		 
			arg=new String[30];
		
		arg[1]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
		arg[2]=req.getPaymentRequestNo();
		
		arg[3]=req.getPaidAmountOrigcurr();
		arg[4]=GetDesginationCountry(req.getPaidAmountOrigcurr(),req.getExcRate());
		
		arg[5]=req.getLossEstimateRevisedOrigCurr();
		arg[6]=GetDesginationCountry(req.getLossEstimateRevisedOrigCurr(),req.getExcRate());
	    arg[7]=req.getClaimNoteRecommendations()==null?"":req.getClaimNoteRecommendations();
	    arg[8]=req.getPaymentReference();
	    if(StringUtils.isNotBlank(req.getAdviceTreasury())){
	    arg[9]=req.getAdviceTreasury();
	    }
	    else{
	    	arg[9]="";	
	    }
        arg[10]=req.getDate();
        arg[11]=req.getClaimNo();
		arg[12]=req.getPolicyContractNo();
		arg[13]=req.getClaimPaymentNo();
		arg[14]=req.getRemarks();
		arg[15]=req.getAdviceTreasuryEmail()==null?"":req.getAdviceTreasuryEmail();
		arg[16]="1";
		if("3".equalsIgnoreCase(req.getProductId())){
			arg[17]=req.getReinstType();
			arg[18]=req.getReinstPremiumOCOS();
			arg[19]=GetDesginationCountry(req.getReinstPremiumOCOS(),req.getExcRate());
			
		}else{
		arg[17]="";
		arg[18]="";
		arg[19]="";
		
		}
		arg[20]=req.getPaidClaimOs();
		arg[21]=GetDesginationCountry(req.getPaidClaimOs(),req.getExcRate());
		arg[22]=req.getSurveyorfeeos();
		arg[23]=GetDesginationCountry(req.getSurveyorfeeos(),req.getExcRate());
		arg[24]=req.getOtherproffeeos();
		arg[25]=GetDesginationCountry(req.getOtherproffeeos(),req.getExcRate());
		arg[26]=req.getBranchCode();
		arg[27]=req.getLoginId();
		arg[28]=req.getExcRate();
		arg[29]= req.getPaymentType();
	
		
		String GetSl_no   = GetSl_no(req.getClaimNo(),req.getPolicyContractNo());
	arg[0] = GetSl_no ;
		query="claim.select.maxResvId";
		list = queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			arg[16] =(list.get(0).get("MAXID") == null ? ""
					: list.get(0).get("MAXID").toString());
		
		}
		
		query="claim.insert.payment";
		
		queryImpl.updateQuery(query,arg);
		
		}else{
			
		query="CLAIN_ARCH_INSERT";
		 arg=new String[8];
		arg[0] =req.getPolicyContractNo();
		arg[1] = req.getClaimNo();
		arg[2] = req.getLayerNo();
		arg[3] = req.getClaimPaymentNo();
		arg[4] =req.getPolicyContractNo();
		arg[5] = req.getClaimNo();
		arg[6] = req.getLayerNo();
		arg[7] = req.getClaimPaymentNo();
		queryImpl.updateQuery(query,arg);
		
		
		query="CLAIM_UPDATE_PAYMENT";
		 arg=new String[22];
		arg[0]=req.getDate();
		arg[1] =req.getPaymentReference();
		arg[2] = req.getPaymentRequestNo();
		arg[3] = req.getPaidClaimOs();
		arg[4]=GetDesginationCountry(req.getPaidClaimOs(),req.getExcRate());
		arg[5]=req.getSurveyorfeeos();
		arg[6]=GetDesginationCountry(req.getSurveyorfeeos(),req.getExcRate());
		arg[7]=req.getOtherproffeeos();
		arg[8]=GetDesginationCountry(req.getOtherproffeeos(),req.getExcRate());
		arg[9]=req.getBranchCode();
		arg[10]=req.getLoginId();
		arg[11]=req.getPaidAmountOrigcurr();
		arg[12]=GetDesginationCountry(req.getPaidAmountOrigcurr(),req.getExcRate());
		arg[13] =req.getRemarks();
		if("3".equalsIgnoreCase(req.getProductId())){
			arg[14]=req.getReinstType();
			arg[15]=req.getReinstPremiumOCOS();
			arg[16]=GetDesginationCountry(req.getReinstPremiumOCOS(),req.getExcRate());
		}else{
		arg[14]="";
		arg[15]="";
		arg[16]="";
		}
		arg[17]=req.getPaymentType();
		arg[18] =req.getPolicyContractNo();
		arg[19] = req.getClaimNo();
		arg[20] = req.getLayerNo();
		arg[21] = req.getClaimPaymentNo();
		queryImpl.updateQuery(query,arg);
		
	}
		args = new String[1];
	
		query="claim.select.sumPaidAmt";
		list = queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			args[0] =(list.get(0).get("PAID_AMOUNT_OC") == null ? ""
					: list.get(0).get("PAID_AMOUNT_OC").toString());
		
		}
	
		
		query="claim.update.totalAmtPaidTillDate";
		queryImpl.updateQuery(query,new String[]{args[0],req.getClaimNo(),req.getPolicyContractNo()});
		query = "claim.select.Ri_recovery";
		list = queryImpl.selectList(query, new String[]{req.getClaimNo(),req.getPolicyContractNo()});
		if (!CollectionUtils.isEmpty(list)) {
			req.setRiRecovery(list.get(0).get("RI_RECOVERY") == null ? ""
					: list.get(0).get("RI_RECOVERY").toString());
		
		}
	
		
	
			query="premium.sp.retroSplit";
		
			args = new String[16];
			args[0]=req.getPolicyContractNo();
			args[1]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			args[2]=req.getProductId();
			args[3]=req.getClaimPaymentNo();
			args[4]=req.getDate();
			args[5]=req.getCurrecny();
			args[6]=req.getExcRate();
			args[7]=req.getBranchCode();
			args[8]="C";
			args[9]="";
			args[10]="";
			args[11]="";
			args[12]="";
			args[13]="";
			args[14]="";
			args[15]=req.getRiRecovery();
			queryImpl.updateQuery(query,args);
	} 
	response.setMessage("Success");
	response.setIsError(false);
	}
	
	 catch(Exception e){
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
			return response;
}

private boolean ModeValidation(final String paymentRequestNo,final String claimNo)
{
	List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
	boolean modes=false;
	int result=0;
	String query="claim.select.paymentReqNo";
	list = queryImpl.selectList(query, new String[] {claimNo,paymentRequestNo});
	if (!CollectionUtils.isEmpty(list)) {
		result =Integer.valueOf((list.get(0).get("COUNT") == null ? ""
				: list.get(0).get("COUNT").toString()));
	
	}
	
	
	if(result==0){
		modes=true;
	}
	
	return modes;
}

@Override
public InsertCliamDetailsMode12Res insertCliamDetailsMode12(InsertCliamDetailsMode12Req req) {
	InsertCliamDetailsMode12Res response = new InsertCliamDetailsMode12Res();
	String [] arg=null;
	String query="";
	try {
	req.setExcRate(getExcRateForCliam(req.getClaimNo(),req.getPolicyContractNo()));
	
	//CliamDetailsArugumentsmode12
	 arg=new String[31];
	  arg[1]="0";
	  arg[2]="0";
	  arg[3]=GetDesginationCountry("0",req.getExcRate());
	  arg[4]=req.getUpdateReference()==null?"":req.getUpdateReference();
	  arg[5]=req.getCliamupdateDate();
	  arg[6]=req.getClaimNo();           
	  arg[7]=req.getPolicyContractNo();
	  arg[8]=req.getRemarks();
	  arg[9]=req.getBranchCode();
	  arg[10]=req.getLoginId();
	  arg[11]=req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC();
	  arg[12]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate());
	  arg[13]=req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc();
	  arg[14]=StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate());
	  arg[15]=req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc();
	  arg[16]=StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate());
	  arg[17]=req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC();
	  arg[18]=req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc();
	  arg[19]=req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc();
	  arg[20]=StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate());
	  arg[21]=StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate());
	  arg[22]=StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate());
	  arg[23]=req.getLossEstimateOrigCurr();
	  arg[24]=StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate());
	  arg[25]=req.getUpdaterecordFees()==null?"No":req.getUpdaterecordFees();
	  arg[26]=req.getUpdaterecordIbnr()==null?"No":req.getUpdaterecordIbnr();
	  arg[27]=req.getExcRate();
	  arg[28]=req.getTotalReserveOSOC()==null?"0":req.getTotalReserveOSOC();
	  arg[29]=StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate());
	  arg[30]=req.getReservePositionDate();
	  
		if(req.getPaymentType().equals("Final")){
			arg[4]="Inserted at the time of Claim Closure";
		}
		arg[5] = req.getDate();
		arg[30] = req.getDate();
		arg[0]=GetSl_no(req.getClaimNo(),req.getPolicyContractNo());
	query="claim.insert.getUpdationQuery";
	
	queryImpl.updateQuery(query,arg);
	

	
	query="claim.update.closeClaim";
	queryImpl.updateQuery(query,new String[]{"Closed",req.getDate(),req.getPolicyContractNo(),req.getClaimNo()});
	

	
	response.setMessage("Success");
	response.setIsError(false);
	}
	
	 catch(Exception e){
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
			return response;

}
}
	
