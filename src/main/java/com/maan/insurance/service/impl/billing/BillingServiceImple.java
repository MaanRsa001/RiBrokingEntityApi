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

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.mapper.TtrnAllocatedTransactionMapper;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.jpa.service.impl.TreasuryJpaServiceImpl;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnBillingInfo;
import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.repository.TtrnBillingInfoRepository;

import com.maan.insurance.model.repository.TtrnBillingTransactionRepository;
import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.res.GetAllTransContractRes;
import com.maan.insurance.model.res.GetAllTransContractRes1;
import com.maan.insurance.model.res.GetTransContractRes;
import com.maan.insurance.model.res.GetTransContractRes1;
import com.maan.insurance.model.res.DropDown.CommonResponse;

import com.maan.insurance.model.res.billing.GetTransContractRes1Ri;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.billing.InsertBillingInfoRes;
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

	private TtrnBillingInfoRepository ttrnBillingInfoRepository;
	@Autowired
	private TtrnAllocatedTransactionMapper ttrnAllocatedTransactionMapper;
	@Autowired
	private TtrnBillingTransactionRepository ttrnBillingTransactionRepository;
	
	
	
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public InsertBillingInfoRes insertBillingInfo(InsertBillingInfoReq req) {
		try {
			//TtrnBillingDetails entity = new TtrnBillingDetails();
			TtrnBillingInfo info = new TtrnBillingInfo();
			if(StringUtils.isBlank(req.getBillingNo())) {
				req.setBillingNo(getSequence("TreasuryARP","","", req.getBranchCode(),"",req.getBillDate()));
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
			info.setCurrencyId(req.getProductId()==null?BigDecimal.ZERO:new BigDecimal(req.getProductId()));
			
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
			
			getAllocateTransaction(req);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public GetTransContractResRi getTransContract(GetTransContractReqRi req) {
		List<GetTransContractRes1Ri> finalList=new ArrayList<GetTransContractRes1Ri>();
		GetTransContractResRi response = new GetTransContractResRi();
        try {
           
        	List<Tuple> list = getTranContDtls(req);
          
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
	public List<Tuple> getTranContDtls(GetTransContractReqRi req) {
		List<Tuple> resultList = getTranContDtlsForRsk(req.getBrokerId(), req.getCedingId(), 
				req.getCurrencyId(), req.getBranchCode());
		if(Objects.nonNull(resultList))
			resultList.addAll(getTranContDtlsForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode()));
		else
			resultList = getTranContDtlsForClaim(req.getBrokerId(), req.getCedingId(), 
					req.getCurrencyId(), req.getBranchCode());
		return Objects.nonNull(resultList) ? resultList : new ArrayList<>();
	}
	private List<Tuple> getTranContDtlsForRsk(String brokerId, String cedingId, String alloccurrencyId, String branchCode) {
		List<Tuple> list = null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<RskPremiumDetails> rRoot = cq.from(RskPremiumDetails.class);
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
			e.printStackTrace();;
		}
		return list;
	}
	private List<Tuple> getTranContDtlsForClaim(String brokerId, String cedingId, String alloccurrencyId, String branchCode){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<PositionMaster> pRoot = cq.from(PositionMaster.class);
		Root<TtrnClaimDetails> tcdRoot = cq.from(TtrnClaimDetails.class);
		Root<TtrnClaimPayment> tcpRoot = cq.from(TtrnClaimPayment.class);
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
	
	public CommonResponse getAllocateTransaction(InsertBillingInfoReq req) {
		CommonResponse response = new CommonResponse();
		try{
			GetTransContractReqRi request=new GetTransContractReqRi();
			request.setCurrencyId(req.getCurrencyId());
			request.setBranchCode(req.getBranchCode());
			request.setBrokerId(req.getBrokerId());
			request.setCedingId(req.getCedingId());
			GetTransContractResRi res= getTransContract(request);
			List<GetTransContractRes1Ri> payList = res.getCommonResponse();
			String billsnNo;
			Double a=0.0,b=0.0,c=0.0;
		
			billsnNo=getSequence("TreasuryARP","","", req.getBranchCode(),"",req.getBillDate());
		
			
			String [] args = null;
		
			for(int i=0;i<payList.size();i++) {
		
				GetTransContractRes1Ri form= payList.get(i);
		
			List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(filterTrack)) {
		
			//if(receivePayAmountMap.containsKey(form.getTransactionNo())) {	 
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
			 	//treasuryCustomRepository.updatePremiumDetails(updateArgs);
			
			 	
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getLoginId();
				updateArgs[2] = req.getBranchCode();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				//treasuryCustomRepository.updatepreSetStatus(updateArgs);
			
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
			
				//treasuryCustomRepository.updateclaimPymtAlloDtls(updateArgs);
			
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginId();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionNo();
				//treasuryCustomRepository.updateclaimSetStatus(updateArgs);
			
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
			args[16]=form.getProposalNo();
//			TtrnAllocatedTransaction ttrnAllocatedTransaction = ttrnAllocatedTransactionMapper.toEntity(args);
//			ttrnAllocatedTransactionRepository.save(ttrnAllocatedTransaction);
			
			TtrnBillingTransaction ttrnBillingTransaction = ttrnAllocatedTransactionMapper.toEntityBilling(args);
			ttrnBillingTransactionRepository.saveAndFlush(ttrnBillingTransaction);
			
			}
			}
		
			if("RT".equalsIgnoreCase(req.getTransType()))
			{
			c=a-b;
			//c=a+b;
			}
			else if("PT".equalsIgnoreCase(req.getTransType()))
			{
			c=b-a;
			//c=b+a;
			}
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(c);
			updateArgs[1] = req.getLoginId();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getBillingNo();
			updateArgs[4] = req.getCurrencyId();
			//queryImpl.updateQuery("payment.update.AlloTranDtls", updateArgs);
			//treasuryCustomRepository.updateAlloTranDtls(updateArgs);
		
			//queryImpl.updateQuery("payment.update.rskPremChkyn");
			treasuryCustomRepository.updateRskPremChkyn();
			
			response.setMessage(billsnNo);
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
}
