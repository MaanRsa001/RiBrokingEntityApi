package com.maan.insurance.service.impl.billing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.mapper.TtrnAllocatedTransactionMapper;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.model.repository.TtrnBillingInfoRepository;
import com.maan.insurance.model.repository.TtrnBillingTransactionRepository;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.res.billing.GetBillingInfoListRes;
import com.maan.insurance.model.res.billing.GetBillingInfoListRes1;
import com.maan.insurance.model.res.billing.GetTransContractRes1Ri;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.billing.BillingService;
import com.maan.insurance.validation.Formatters;

@Service
public class BillingServiceImple implements  BillingService {
	private Logger log = LogManager.getLogger(BillingServiceImple.class);
	@Autowired
	private Formatters fm;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private TreasuryCustomRepository treasuryCustomRepository;
	
	@Autowired

	private BillingCustomRepository billingCustomRepository;

	@Autowired
	private TtrnBillingInfoRepository ttrnBillingInfoRepository;
	@Autowired
	private TtrnAllocatedTransactionMapper ttrnAllocatedTransactionMapper;
	@Autowired
	private TtrnBillingTransactionRepository ttrnBillingTransactionRepository;
	
	
	
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public CommonResponse insertBillingInfo(InsertBillingInfoReq req) {
		CommonResponse response = new CommonResponse();
		try {
			//TtrnBillingDetails entity = new TtrnBillingDetails();
			TtrnBillingInfo info = new TtrnBillingInfo();
			if(StringUtils.isBlank(req.getBillingNo())) {
				req.setBillingNo(getSequence("IB".equals(req.getTransType())?"InBillingNo":"OutBillingNo","","", req.getBranchCode(),"",req.getBillDate()));
			}
			
			info.setAmendId(BigDecimal.ZERO);
			info.setAmendmentDate(req.getAmendmentDate()==null?null:sdf.parse(req.getAmendmentDate()));
			info.setBillingNo(req.getBillingNo()==null?BigDecimal.ZERO:new BigDecimal(req.getBillingNo()));
			info.setBillDate(req.getBillDate()==null?null:sdf.parse(req.getBillDate()));
			info.setBranchCode(req.getBranchCode()==null?"":req.getBranchCode());
			info.setBrokerId(req.getBrokerId()==null?BigDecimal.ZERO:new BigDecimal(req.getBrokerId()));
			info.setCedingId(req.getCedingId()==null?BigDecimal.ZERO:new BigDecimal(req.getCedingId()));
			info.setLoginId(req.getLoginId()==null?"":req.getLoginId());
			info.setProductId(req.getProductId()==null?BigDecimal.ZERO:new BigDecimal(req.getProductId()));
			info.setCurrencyId(req.getCurrencyId()==null?BigDecimal.ZERO:new BigDecimal(req.getCurrencyId()));
			info.setRoundingAmt(req.getRoundingAmount()==null?BigDecimal.ZERO:new BigDecimal(req.getRoundingAmount()));
			info.setUtilizedTillDate(req.getUtilizedTillDate()==null?BigDecimal.ZERO:new BigDecimal(req.getUtilizedTillDate()));
			//info.setRemarks(req.getRemarks()==null?"":req.getRemarks());
			//info.setReversaldate(req.getReversaldate()==null?null:sdf.parse(req.getReversaldate()));
			//info.setReversaltransno(req.getReversaltransno()==null?BigDecimal.ZERO:new BigDecimal(req.getReversaltransno()));
			//info.setReverselloginid(req.getReverselloginid()==null?"":req.getReverselloginid());
			//info.setRevtransaltype(req.getRevtransaltype()==null?"":req.getRevtransaltype());
			info.setStatus("Y");
			info.setSysDate(new Date());
			//info.setTranscationtype(req.getTranscationtype()==null?"":req.getTranscationtype());
			info.setTransType(req.getTransType()==null?"":req.getTransType());
			ttrnBillingInfoRepository.saveAndFlush(info);
			if("IB".equals(req.getTransType())) {
				getAllocateTransaction(req);
			}else {
				getAllocateTransactionRi(req);
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
	public GetTransContractResRi getTransContract(GetTransContractReqRi req) {
		List<GetTransContractRes1Ri> finalList=new ArrayList<GetTransContractRes1Ri>();
		GetTransContractResRi response = new GetTransContractResRi();
        try {
           
        	List<Tuple> list = billingCustomRepository.getTranContDtls(req);
          
			for(int i=0 ,count=0; i<list.size(); i++,count++) {
				GetTransContractRes1Ri res = new GetTransContractRes1Ri();
				Tuple tempMap = list.get(i);
				res.setContractNo(tempMap.get(1)==null?"":tempMap.get(1).toString());
				res.setMode(tempMap.get(2)==null?"":tempMap.get(2).toString());
				res.setProductName(tempMap.get(3)==null?"":tempMap.get(3).toString());
				res.setTransactionNo(tempMap.get(0)==null?"":tempMap.get(0).toString());
				res.setInceptiobDate(tempMap.get(4)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(4)).toString());
				res.setNetDue(tempMap.get(5)==null?"":tempMap.get(5).toString());
				res.setPayAmount(tempMap.get(6)==null?"":tempMap.get(6).toString());
				res.setAccPremium(tempMap.get(7)==null?"":tempMap.get(7).toString());
				res.setAccClaim(tempMap.get(8)==null?"":tempMap.get(8).toString());
				res.setCheckYN(tempMap.get(9)==null?"":tempMap.get(9).toString());
				res.setCheckPC(tempMap.get(10)==null?"":tempMap.get(10).toString());
				res.setCedingCompanyName(tempMap.get(11)==null?"":tempMap.get(11).toString());
//				res.setAllocType(req.getAllocType());
//				if(!CollectionUtils.isEmpty(req.getTransContractListReq())) {
//					List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> res.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
//					if(!CollectionUtils.isEmpty(filterTrack)) {
//						res.setReceivePayAmounts(filterTrack.get(0).getReceivePayAmounts());
//						res.setPreviousValue(filterTrack.get(0).getReceivePayAmounts());
//						res.setPayAmounts(filterTrack.get(0).getReceivePayAmounts());
//						res.setChkBox("true");
//				
//					}
//					else {
//						res.setReceivePayAmounts("");
//						res.setPreviousValue("");
//						res.setPayAmounts("");
//						res.setChkBox("false");
//					}
				res.setTotalRecCount(String.valueOf(list.size()));
				res.setCount(String.valueOf(count));
				finalList.add(res);
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
	
	
	public CommonResponse getAllocateTransaction(InsertBillingInfoReq req) {
		CommonResponse response = new CommonResponse();
		try{
			GetTransContractReqRi request=new GetTransContractReqRi();
			request.setCurrencyId(req.getCurrencyId());
			request.setBranchCode(req.getBranchCode());
			request.setBrokerId(req.getBrokerId());
			request.setCedingId(req.getCedingId());
			request.setProductId(req.getProductId());
			GetTransContractResRi res= getTransContract(request);
			List<GetTransContractRes1Ri> payList = res.getCommonResponse();
			String billsnNo = "";
			Double a=0.0,b=0.0,c=0.0;
		
		//	billsnNo=getSequence("TreasuryARP","","", req.getBranchCode(),"",req.getBillDate());
			billsnNo = String.valueOf(billingCustomRepository.getNextRetDtlsNo());
			
			
			String [] args = null;
		
			for(int i=0;i<payList.size();i++) {
		
				GetTransContractRes1Ri form= payList.get(i);
		
			List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(filterTrack)) {
		
			 args=new String[17];
			 args[0]=billsnNo;	
			 args[1]=form.getContractNo();
			 args[2]=StringUtils.isBlank(form.getMode())?"0":form.getMode();
		
			 args[3]=form.getProductName();
			 args[4]=form.getTransactionNo();
			 args[5]=req.getBillDate();
			if("P".equalsIgnoreCase(form.getCheckPC())){
				args[6]= filterTrack.get(0).getReceivePayAmounts();
			 	args[7]="P";
			 	String[] updateArgs = new String[5];
		
			 	updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
			 	updateArgs[1] = req.getLoginId();
			 	updateArgs[2] = req.getBranchCode();
			 	updateArgs[3] = form.getContractNo();
			 	updateArgs[4] = form.getTransactionNo();
			 	treasuryCustomRepository.updatePremiumDetails(updateArgs);
			
			 	
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getLoginId();
				updateArgs[2] = req.getBranchCode();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				treasuryCustomRepository.updatepreSetStatus(updateArgs);
			
			 	a=a+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			else if("C".equalsIgnoreCase(form.getCheckPC())) {
				args[6] = filterTrack.get(0).getReceivePayAmounts();
				args[7]="C";
				
				String[] updateArgs = new String[5];
				updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginId();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
			
				treasuryCustomRepository.updateclaimPymtAlloDtls(updateArgs);
			
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginId();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				treasuryCustomRepository.updateclaimSetStatus(updateArgs);
			
				b = b + Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			args[8]="Y";
			args[9]="0";
			args[10]=req.getBillingNo();//Receipt No
			args[11]=req.getCurrencyId();//Currency ID
			args[12]="";
			args[13]=form.getSubClass();
			args[14]=req.getLoginId();
			args[15]=req.getBranchCode();
			
			TtrnBillingTransaction ttrnBillingTransaction = ttrnAllocatedTransactionMapper.toEntityBilling(args);
			ttrnBillingTransactionRepository.saveAndFlush(ttrnBillingTransaction);
			
			}
			}
		
			c=b-a;
			
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(c);
			updateArgs[1] = req.getLoginId();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getBillingNo();
			updateArgs[4] = req.getCurrencyId();
	
			billingCustomRepository.updateAlloTranDtls(updateArgs);

			treasuryCustomRepository.updateRskPremChkyn();
			
			response.setMessage(billsnNo);
			response.setMessage("Success");
			response.setIsError(false);
			}
			catch(Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
		}
	public CommonResponse getAllocateTransactionRi(InsertBillingInfoReq req) {
		CommonResponse response = new CommonResponse();
		try{
			GetTransContractReqRi request=new GetTransContractReqRi();
			request.setCurrencyId(req.getCurrencyId());
			request.setBranchCode(req.getBranchCode());
			request.setBrokerId(req.getBrokerId());
			request.setCedingId(req.getCedingId());
			request.setProductId(req.getProductId());
			GetTransContractResRi res= getTransContractRi(request);
			List<GetTransContractRes1Ri> payList = res.getCommonResponse();
			String billsnNo = "";
			Double a=0.0,b=0.0,c=0.0;
		
		//	billsnNo=getSequence("TreasuryARP","","", req.getBranchCode(),"",req.getBillDate());
			billsnNo = String.valueOf(billingCustomRepository.getNextRetDtlsNo());
			
			
			String [] args = null;
		
			for(int i=0;i<payList.size();i++) {
		
				GetTransContractRes1Ri form= payList.get(i);
		
			List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(filterTrack)) {
		
			 args=new String[17];
			 args[0]=billsnNo;	
			 args[1]=form.getContractNo();
			 args[2]=StringUtils.isBlank(form.getMode())?"0":form.getMode();
		
			 args[3]=form.getProductName();
			 args[4]=form.getTransactionNo();
			 args[5]=req.getBillDate();
			if("P".equalsIgnoreCase(form.getCheckPC())){
				args[6]= filterTrack.get(0).getReceivePayAmounts();
			 	args[7]="P";
			 	String[] updateArgs = new String[5];
		
			 	updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
			 	updateArgs[1] = req.getLoginId();
			 	updateArgs[2] = req.getBranchCode();
			 	updateArgs[3] = form.getContractNo();
			 	updateArgs[4] = form.getTransactionNo();
			 	billingCustomRepository.updatePremiumRiDetails(updateArgs);
			
			 	
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getLoginId();
				updateArgs[2] = req.getBranchCode();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				billingCustomRepository.updatepreRiSetStatus(updateArgs);
			
			 	a=a+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			else if("C".equalsIgnoreCase(form.getCheckPC())) {
				args[6] = filterTrack.get(0).getReceivePayAmounts();
				args[7]="C";
				
				String[] updateArgs = new String[5];
				updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginId();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
			
				billingCustomRepository.updateclaimRiPymtAlloDtls(updateArgs);
			
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginId();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				billingCustomRepository.updateclaimRiSetStatus(updateArgs);
			
				b = b + Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			args[8]="Y";
			args[9]="0";
			args[10]=req.getBillingNo();//Receipt No
			args[11]=req.getCurrencyId();//Currency ID
			args[12]="";
			args[13]=form.getSubClass();
			args[14]=req.getLoginId();
			args[15]=req.getBranchCode();
			
			TtrnBillingTransaction ttrnBillingTransaction = ttrnAllocatedTransactionMapper.toEntityBilling(args);
			ttrnBillingTransactionRepository.saveAndFlush(ttrnBillingTransaction);
			
			}
			}
		
			c=b-a;
			
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(c);
			updateArgs[1] = req.getLoginId();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getBillingNo();
			updateArgs[4] = req.getCurrencyId();
	
			billingCustomRepository.updateAlloTranDtls(updateArgs);

			billingCustomRepository.updateRskPremRiChkyn();
			
			response.setMessage(billsnNo);
			response.setMessage("Success");
			response.setIsError(false);
			}
			catch(Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
		}
	public synchronized String getSequence(String type,String productID,String departmentId,String branchCode, String proposalNo,String date){ 
		String seqName="";
		try{
			Long seqNo = treasuryCustomRepository.getseqno(new String[]{type,productID,departmentId,branchCode,proposalNo,date});
			seqName = (seqNo == null ? "": seqNo.toString());
			log.info("Result==> " + seqName);
			
		}catch(Exception e){
			log.debug("Exception @ {" + e + "}");
		}

		return seqName;
	}


	@Override
	public GetBillingInfoListRes getBillingInfoList(GetBillingInfoListReq req) {
		GetBillingInfoListRes response = new GetBillingInfoListRes();
		List<GetBillingInfoListRes1> resList = new ArrayList<GetBillingInfoListRes1>();
		try {
			
			List<Tuple> list = billingCustomRepository.getBillingInfoList(req);
			
			
			if(list.size()>0) {
				for(int i=0; i<list.size();i++) {
					GetBillingInfoListRes1 res = new GetBillingInfoListRes1();
					Tuple map = list.get(i);
					res.setBillDate(map.get("billDate")==null?null:sdf.format(map.get("billDate")));
					res.setBillingNo(map.get("billingNo")==null?"":map.get("billingNo").toString());
					res.setBrokerId(map.get("brokerId")==null?"":map.get("brokerId").toString());
					res.setBrokerName(map.get("brokerName")==null?"":map.get("brokerName").toString());
					res.setReinsurerName(map.get("reinsurerName")==null?"":map.get("reinsurerName").toString());
					res.setStatus(map.get("status")==null?"":map.get("status").toString());
					resList.add(res);					
			} 
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetTransContractResRi getTransContractRi(GetTransContractReqRi req) {
		List<GetTransContractRes1Ri> finalList=new ArrayList<GetTransContractRes1Ri>();
		GetTransContractResRi response = new GetTransContractResRi();
        try {

        	List<Tuple> list = billingCustomRepository.getTranContDtlsRi(req);
          
			for(int i=0 ,count=0; i<list.size(); i++,count++) {
				GetTransContractRes1Ri res = new GetTransContractRes1Ri();
				Tuple tempMap = list.get(i);
				res.setContractNo(tempMap.get(1)==null?"":tempMap.get(1).toString());
				res.setMode(tempMap.get(2)==null?"":tempMap.get(2).toString());
				res.setProductName(tempMap.get(3)==null?"":tempMap.get(3).toString());
				res.setTransactionNo(tempMap.get(0)==null?"":tempMap.get(0).toString());
				res.setInceptiobDate(tempMap.get(4)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(4)).toString());
				res.setNetDue(tempMap.get(5)==null?"":tempMap.get(5).toString());
				res.setPayAmount(tempMap.get(6)==null?"":tempMap.get(6).toString());
				res.setAccPremium(tempMap.get(7)==null?"":tempMap.get(7).toString());
				res.setAccClaim(tempMap.get(8)==null?"":tempMap.get(8).toString());
				res.setCheckYN(tempMap.get(9)==null?"":tempMap.get(9).toString());
				res.setCheckPC(tempMap.get(10)==null?"":tempMap.get(10).toString());
				res.setCedingCompanyName(tempMap.get(11)==null?"":tempMap.get(11).toString());
//				res.setAllocType(req.getAllocType());
//				if(!CollectionUtils.isEmpty(req.getTransContractListReq())) {
//					List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> res.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
//					if(!CollectionUtils.isEmpty(filterTrack)) {
//						res.setReceivePayAmounts(filterTrack.get(0).getReceivePayAmounts());
//						res.setPreviousValue(filterTrack.get(0).getReceivePayAmounts());
//						res.setPayAmounts(filterTrack.get(0).getReceivePayAmounts());
//						res.setChkBox("true");
//				
//					}
//					else {
//						res.setReceivePayAmounts("");
//						res.setPreviousValue("");
//						res.setPayAmounts("");
//						res.setChkBox("false");
//					}
				res.setTotalRecCount(String.valueOf(list.size()));
				res.setCount(String.valueOf(count));
				finalList.add(res);
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

	

}
