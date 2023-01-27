package com.maan.insurance.service.impl.billing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.mapper.TtrnAllocatedTransactionMapper;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPaymentRi;
import com.maan.insurance.model.repository.TtrnBillingDetailsRepository;
import com.maan.insurance.model.repository.TtrnBillingInfoRepository;
import com.maan.insurance.model.repository.TtrnBillingTransactionRepository;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.billing.EditBillingInfoReq;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.res.billing.EditBillingInfoComRes;
import com.maan.insurance.model.res.billing.EditBillingInfoRes;
import com.maan.insurance.model.res.billing.EditBillingInfoRes1;
import com.maan.insurance.model.res.billing.EditBillingTransactionRes;
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
	private TtrnBillingDetailsRepository ttrnBillingDetailsRepository;

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

	private List<Tuple> getTranContDtlsRi(GetTransContractReqRi req) {
		List<Tuple> resultList = getTranContDtlsRiForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getCurrencyId(), req.getBranchCode());
		if(Objects.nonNull(resultList))
			resultList.addAll(getTranContDtlsRiForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode()));
		else
			resultList = getTranContDtlsRiForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode());
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}

	private List<Tuple>  getTranContDtlsRiForClaim(String brokerId, String cedingId, String alloccurrencyId,
			String branchCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		Root<TtrnClaimPaymentRi> tcpRoot = cq.from(TtrnClaimPaymentRi.class);
		String input = null;
		
//		Expression<String> funct = cb.function("FN_GET_NAME", String.class,
//     		   cb.literal("P"),
//     		  pRoot.get("productId"),
//     		 pRoot.get("branchCode"));
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		
		Expression<String> exp = cb.diff(cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("paidAmountOc")), 0.0)
				.otherwise(tcpRoot.<Double>get("paidAmountOc")),
			cb.<Double>selectCase().when(cb.isNull(tcpRoot.<Double>get("allocatedTillDate")), 0.0)
				.otherwise(tcpRoot.<Double>get("allocatedTillDate"))).as(String.class);
		
		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);
		
		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId")))
		  .where(cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName"))
		  .where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")),
				cb.equal(subRoot.get("amendId"), aSq));
		
		cq.multiselect(tcpRoot.get("claimPaymentNo").as(String.class),
				pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"),
				tcpRoot.get("inceptionDate"),
				cb.nullLiteral(Double.class).alias("NETDUE_OC"),
				exp.alias("PAID_AMOUNT"),
				cb.nullLiteral(Double.class).alias("ACC_PREMIUM"),
				tcpRoot.get("accClaim").as(String.class),
				cb.selectCase().when(cb.isNull(tcpRoot.get("checkyn")), "N").as(String.class),
				cb.literal("C").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"),
				pRoot.get("deptId"),
				pRoot.get("proposalNo")).distinct(true);
 		
		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);
		
	   Expression<Object> exp1 = cb.selectCase().when(
	    		cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
	    		pRoot.get("cedingCompanyId")).otherwise(pRoot.get("brokerId"));
	   
	   if("63".equalsIgnoreCase(brokerId)) {
		   input=cedingId;
		}
		else {        	
			input=brokerId;
		}
		
		pSq.select(cb.max(pSubRoot.get("amendId")))
		  .where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
			     cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
	    		 cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))), 
			     cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")),
			     cb.equal(tcdRoot.get("currency"), alloccurrencyId),
			     cb.equal(exp1, input));
		
		cq.where(cb.equal(pRoot.get("contractNo"), tcdRoot.get("contractNo")),
				 cb.equal(tcdRoot.get("contractNo"), tcpRoot.get("contractNo")),
				 cb.equal(tcdRoot.get("claimNo"), tcpRoot.get("claimNo")),
				 cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						 cb.selectCase().when(cb.isNull(tcdRoot.get("layerNo")), 0).otherwise(tcdRoot.get("layerNo"))),
				cb.equal(pRoot.get("sectionNo"), tcdRoot.get("subClass")),
				cb.equal(pRoot.get("branchCode"), branchCode),
				cb.notEqual(exp, 0),
				cb.equal(pRoot.get("amendId"), pSq));
		
		return em.createQuery(cq).getResultList();
	}

	private List<Tuple> getTranContDtlsRiForRsk(String brokerId, String cedingId, String alloccurrencyId,
			String branchCode) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<RskPremiumDetailsRi> rRoot = cq.from(RskPremiumDetailsRi.class);
		String input = null;

//		Expression<String> funct = cb.function("FN_GET_NAME", String.class, cb.literal("P"), pRoot.get("productId"),
//				pRoot.get("branchCode"));
		
		Subquery<String> funct = cq.subquery(String.class); 
		Root<TmasProductMaster> rds = funct.from(TmasProductMaster.class);
		funct.select(rds.get("tmasProductName"));
		Predicate a1 = cb.equal( rds.get("tmasProductId"),pRoot.get("productId"));
		Predicate a2 = cb.equal( rds.get("branchCode"), pRoot.get("branchCode"));
		funct.where(a1,a2);

		Expression<String> exp = cb.diff(
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("netdueOc")), 0.0)
						.otherwise(rRoot.<Double>get("netdueOc")),
				cb.<Double>selectCase().when(cb.isNull(rRoot.<Double>get("allocatedTillDate")), 0.0)
						.otherwise(rRoot.<Double>get("allocatedTillDate"))).as(String.class);

		Subquery<String> sq = cq.subquery(String.class);
		Root<PersonalInfo> subRoot = sq.from(PersonalInfo.class);

		Subquery<Integer> aSq = sq.subquery(Integer.class);
		Root<PersonalInfo> aSubRoot = aSq.from(PersonalInfo.class);

		aSq.select(cb.max(aSubRoot.get("amendId"))).where(
				cb.equal(aSubRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(aSubRoot.get("branchCode"), pRoot.get("branchCode")));

		sq.select(subRoot.get("companyName")).where(cb.equal(subRoot.get("customerId"), pRoot.get("cedingCompanyId")),
				cb.equal(subRoot.get("branchCode"), pRoot.get("branchCode")), cb.equal(subRoot.get("amendId"), aSq));

		cq.multiselect(rRoot.get("transactionNo").as(String.class), pRoot.get("contractNo").as(String.class),
				pRoot.get("layerNo").as(String.class),
				funct.alias("PRODUCT_NAME"), rRoot.get("entryDateTime"), exp.alias("NETDUE"),
				cb.nullLiteral(Double.class).alias("PAID_AMOUNT_OC"), rRoot.get("accPremium").as(String.class),
				cb.nullLiteral(Double.class).alias("ACC_CLAIM"),
				cb.selectCase().when(cb.isNull(rRoot.get("checkyn")), "N").as(String.class),
				cb.literal("P").alias("BUSINESS_TYPE"),
				sq.alias("CEDING_COMPANY_NAME"), 
				pRoot.get("deptId").as(String.class),
				pRoot.get("proposalNo").as(String.class)).distinct(true);

		Subquery<Integer> pSq = cq.subquery(Integer.class);
		Root<PositionMaster> pSubRoot = pSq.from(PositionMaster.class);

		Expression<Object> exp1 = cb.selectCase()
				.when(cb.equal(cb.literal(63), cb.literal(Integer.parseInt(brokerId))),
						pRoot.get("cedingCompanyId"))
				.otherwise(pRoot.get("brokerId"));

		if("63".equalsIgnoreCase(brokerId))
			input=cedingId;
		else        	
			input=brokerId;
		
		pSq.select(cb.max(pSubRoot.get("amendId"))).where(cb.equal(pSubRoot.get("contractNo"), pRoot.get("contractNo")),
				cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						cb.selectCase().when(cb.isNull(pSubRoot.get("layerNo")), 0).otherwise(pSubRoot.get("layerNo"))),
				cb.equal(pRoot.get("deptId"), pSubRoot.get("deptId")), cb.equal(rRoot.get("currencyId"), alloccurrencyId),
				cb.equal(exp1, input));
				//cb.like(pRoot.get("contractNo"), ""), cb.like(pRoot.get("productId"), "")); // check

		cq.where(cb.or(cb.isNull(rRoot.get("receiptNo")),cb.equal(rRoot.get("receiptNo"), "0")), cb.equal(rRoot.get("contractNo"), pRoot.get("contractNo")),
				cb.equal(cb.selectCase().when(cb.isNull(pRoot.get("layerNo")), 0).otherwise(pRoot.get("layerNo")),
						cb.selectCase().when(cb.isNull(rRoot.get("layerNo")), 0).otherwise(rRoot.get("layerNo"))),
				cb.equal(pRoot.get("deptId"), rRoot.get("subClass")),
				cb.equal(pRoot.get("branchCode"), branchCode),
				cb.notEqual(exp, 0),
				cb.equal(pRoot.get("amendId"), pSq));

		list =  em.createQuery(cq).getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public EditBillingInfoRes editBillingInfo(EditBillingInfoReq req) {
		EditBillingInfoRes response = new EditBillingInfoRes();
		EditBillingInfoComRes com =new EditBillingInfoComRes();
		try {
			TtrnBillingInfo data = ttrnBillingInfoRepository.findByBillingNo(new BigDecimal(req.getBillingNo()));
			if(data!=null) {
					EditBillingInfoRes1 res = new EditBillingInfoRes1();
					res.setAmedId(data.getAmendId()==null?"":data.getAmendId().toString());		
					res.setAmendmentDate(data.getAmendmentDate()==null?"":sdf.format(data.getAmendmentDate()));;
					res.setBillDate(data.getBillDate()==null?"":sdf.format(data.getBillDate()));
					res.setBranchCode(data.getBranchCode()==null?"":data.getBranchCode().toString());	
					res.setBrokerId(data.getBrokerId()==null?"":data.getBrokerId().toString());		
					res.setCedingId(data.getCedingId()==null?"":data.getCedingId().toString());					
					res.setCurrencyId(data.getCurrencyId()==null?"":data.getCurrencyId().toString());
					res.setLoginId(data.getLoginId()==null?"":data.getLoginId().toString());
					res.setProductId(data.getProductId()==null?"":data.getProductId().toString());
					res.setRemarks(data.getRemarks()==null?"":data.getRemarks().toString());
					res.setReversalDate(data.getReversaldate()==null?"":sdf.format(data.getReversaldate()));
					res.setReversalTransno(data.getReversaltransno()==null?"":data.getReversaltransno().toString());					
					res.setReverselLoginid(data.getLoginId()==null?"":data.getLoginId().toString());
					res.setRevTransalType(data.getTransType()==null?"":data.getTransType().toString());;
					res.setRoundingAmount(data.getRoundingAmt()==null?"":data.getRoundingAmt().toString());
					res.setStatus(data.getStatus()==null?"":data.getStatus().toString());
					res.setTranscationType(data.getTranscationtype()==null?"":data.getTranscationtype().toString());
					res.setTransType(data.getTransType()==null?"":data.getTransType().toString());
					res.setUtilizedTillDate(data.getUtilizedTillDate()==null?"":sdf.format(data.getUtilizedTillDate()));	
					com.setBillingInfo(res);
					}
			
			List<EditBillingTransactionRes>  list = editBillingTransaction(req);
			com.setBillingTransaction(list);
			
				response.setCommonResponse(com);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}

	private List<EditBillingTransactionRes> editBillingTransaction(EditBillingInfoReq req) {
		List<EditBillingTransactionRes> response  = new ArrayList<EditBillingTransactionRes>();
		try {
			List<TtrnBillingTransaction> list = ttrnBillingTransactionRepository.findByBillNo(new BigDecimal(req.getBillingNo()));
			if(list.size()>0) {
				for(TtrnBillingTransaction data: list) {
					EditBillingTransactionRes res = new EditBillingTransactionRes();
					res.setAdjustmentType(data.getAdjustmentType()==null?"":data.getAdjustmentType().toString());		
					res.setAmedId(data.getAmendId()==null?"":data.getAmendId().toString());
					res.setBillSno(data.getBillSno()==null?"":data.getBillSno().toString());
					res.setBranchCode(data.getBranchCode()==null?"":data.getBranchCode().toString());
					res.setContractNo(data.getContractNo()==null?"":data.getContractNo().toString());
					res.setCurrencyId(data.getCurrencyId()==null?"":data.getCurrencyId().toString());
					res.setInceptionDate(data.getInceptionDate()==null?"":sdf.format(data.getInceptionDate()));
					res.setLayerNo(data.getLayerNo()==null?"":data.getLayerNo().toString());
					res.setLoginId(data.getLoginId()==null?"":data.getLoginId().toString());
					res.setPaidAmount(data.getPaidAmount()==null?"":data.getPaidAmount().toString());
					res.setProcessType(data.getProcessType()==null?"":data.getProcessType().toString());
					res.setProductName(data.getProductName()==null?"":data.getProductName().toString());
					res.setProposalNo(data.getProposalNo()==null?"":data.getProposalNo().toString());
					res.setRemarks(data.getRemarks()==null?"":data.getRemarks().toString());
					res.setReversalAmount(data.getReversalAmount()==null?"":data.getReversalAmount().toString());
					res.setReversalDate(data.getReversalDate()==null?"":sdf.format(data.getReversalDate()));
					res.setStatus(data.getStatus()==null?"":data.getStatus().toString());
					res.setTransactionNo(data.getTransactionNo()==null?"":data.getTransactionNo().toString());
					res.setType(data.getType()==null?"":data.getType().toString());
					response.add(res);
					}
			}
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
			return response;
	}

}
