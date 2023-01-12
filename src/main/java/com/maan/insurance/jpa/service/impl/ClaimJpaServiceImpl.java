package com.maan.insurance.jpa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.claim.TtrnClaimAcc;
import com.maan.insurance.jpa.entity.claim.TtrnClaimPaymentArchive;
import com.maan.insurance.jpa.entity.claim.TtrnClaimUpdation;
import com.maan.insurance.jpa.mapper.TtrnClaimAccMapper;
import com.maan.insurance.jpa.mapper.TtrnClaimDetailsMapper;
import com.maan.insurance.jpa.mapper.TtrnClaimPaymentArchiveMapper;
import com.maan.insurance.jpa.mapper.TtrnClaimPaymentMapper;
import com.maan.insurance.jpa.mapper.TtrnClaimUpdationMapper;
import com.maan.insurance.jpa.repository.claim.ClaimCustomRepository;
import com.maan.insurance.jpa.repository.claim.TtrnClaimPaymentArchiveRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnClaimUpdationRepository;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.repository.TtrnClaimAccRepository;
import com.maan.insurance.model.repository.TtrnClaimDetailsRepository;
import com.maan.insurance.model.repository.TtrnClaimPaymentRepository;
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
import com.maan.insurance.model.req.claim.claimNoListReq;
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
import com.maan.insurance.model.res.claim.claimNoListRes;
import com.maan.insurance.service.claim.ClaimService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.Validation;
import com.maan.insurance.validation.Claim.ValidationImple;

@Component
public class ClaimJpaServiceImpl implements ClaimService  {

	private Logger log = LogManager.getLogger(ClaimJpaServiceImpl.class);

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;

	@Autowired
	private ValidationImple vi;

	@Autowired
	private ClaimCustomRepository claimCustomRepository;
	
	@Autowired
	private TtrnClaimUpdationMapper ttrnClaimUpdationMapper;
	
	@Autowired
	private TtrnClaimPaymentMapper ttrnClaimPayemntMapper;
	
	@Autowired
	private TtrnClaimPaymentArchiveMapper ttrnClaimPaymentArchiveMapper;
	
	@Autowired
	private TtrnClaimUpdationRepository ttrnClaimUpdationRepository;
	
	@Autowired
	private TtrnClaimPaymentRepository ttrnClaimPaymentRepository;
	
	@Autowired
	private TtrnClaimPaymentArchiveRepository ttrnClaimPaymentArchiveRepository;
	
	@Autowired
	private TtrnClaimDetailsMapper ttrnClaimDetailsMapper;
	
	@Autowired
	private TtrnClaimDetailsRepository ttrnClaimDetailsRepository;
	
	@Autowired
	private TtrnClaimAccRepository ttrnClaimAccRepository;
	
	@Autowired
	private TtrnClaimAccMapper ttrnClaimAccMapper;
	// allocList(AllocListReq req) -- STARTS
	public AllocListRes1 allocList(AllocListReq req) {
		List<AllocListRes> allocists = new ArrayList<AllocListRes>();
		AllocListRes1 res = new AllocListRes1();
		try {

			// query -- "claim_allocation_list";
			List<Tuple> list = claimCustomRepository.getClaimAllocationList(req.getContractNo(),
					req.getTransactionNo());

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					final AllocListRes bean = new AllocListRes();
					Tuple map = list.get(i);
					bean.setDate(map.get("INCEPTION_DATE") == null ? "" : map.get("INCEPTION_DATE").toString());
					bean.setSNo(map.get("SNO") == null ? "" : map.get("SNO").toString());
					bean.setTransactionType(map.get("TRANS_TYPE") == null ? "" : map.get("TRANS_TYPE").toString());
					bean.setPaidAmountOrigcurr(map.get("PAID_AMOUNT") == null ? "" : map.get("PAID_AMOUNT").toString());
					bean.setStatusofclaim(map.get("STATUS") == null ? "" : map.get("STATUS").toString());
					bean.setProductName(map.get("PRODUCT_NAME") == null ? "" : map.get("PRODUCT_NAME").toString());
					bean.setType(map.get("TYPE") == null ? "" : map.get("TYPE").toString());
					bean.setSign(map.get("SIGN") == null ? "" : map.get("SIGN").toString());

					if (bean.getSign().equalsIgnoreCase("-1")) {
						bean.setSign("-");
					} else if (bean.getSign().equalsIgnoreCase("1")) {
						bean.setSign("+");
					} else {
						bean.setSign("");
					}
					bean.setReceiptNo(map.get("RECEIPT_NO") == null ? "" : map.get("RECEIPT_NO").toString());
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
	// allocList(AllocListReq req) -- ENDS

	// saveAllocationList -- STARTS
	public AllocationListRes saveAllocationList(AllocationListReq req) {
		AllocationListRes response = new AllocationListRes();
		List<Tuple> date = new ArrayList<>();
		Double a = 0.0;
		try {
			// query -- claim_allocation_list
			date = claimCustomRepository.getClaimAllocationList(req.getContractNo(), req.getTransactionNumber());

			if (date.size() > 0) {
				for (int i = 0; i < date.size(); i++) {
					Tuple resMap = date.get(i);
					a = a + Double
							.parseDouble(resMap.get("PAID_AMOUNT") == null ? "" : resMap.get("PAID_AMOUNT").toString());
				}
			}
			if (a > 0)
				response.setCommonResponse(fm.formatter(Double.toString(a)));
			else
				response.setCommonResponse(""); // TotalAmount

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	// saveAllocationList -- ENDS

	// saveContractDetailsMode10(ContractDetailsModeReq req) --STARTS
	public ContractDetailsMode10Res saveContractDetailsMode10(ContractDetailsModeReq req) {
		ContractDetailsMode10Res response = new ContractDetailsMode10Res();
		List<ContractDetailsListMode10res> finalList = new ArrayList<ContractDetailsListMode10res>();
		ContractDetailsListMode10res res = new ContractDetailsListMode10res();
		String output = null;
		try {
			if (StringUtils.isNotBlank(req.getClaimNo())) {
				// query -- claim.select.sumPaidAmt
				output = claimCustomRepository.selectSumPaidAmt(req.getClaimNo(), req.getPolicyContractNo());
				res.setSumOfPaidAmountOC(output == null ? "" : fm.formatter(output));
			}

			// query -- claim.select.revSumPaidAmt
			output = claimCustomRepository.selectRevSumPaidAmt(req.getClaimNo(), req.getPolicyContractNo());
			res.setRevSumOfPaidAmt(output == null ? "" : fm.formatter(output));

			if (StringUtils.isNotBlank(req.getClaimNo())) {
				// query -- claim.select.lossEstimateRevisedOc
				output = claimCustomRepository.selectLossEstimateRevisedOc(req.getClaimNo(), req.getPolicyContractNo());
				res.setLossEstimateRevisedOrigCurr(output == null ? "" : fm.formatter(output));
			}

			finalList.add(res);
			response.setCommonResponse(finalList);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	// saveContractDetailsMode10(ContractDetailsModeReq req) --ENDS

	// saveContractDetailsMode5(ContractDetailsModeReq req) -- STARTS
	public ContractDetailsMode5Res saveContractDetailsMode5(ContractDetailsModeReq req) {
		ContractDetailsMode5Res response = new ContractDetailsMode5Res();
		List<ContractDetailsListMode5res> finalList = new ArrayList<ContractDetailsListMode5res>();
		ContractDetailsListMode5res res = new ContractDetailsListMode5res();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String output = null;
		try {
			if (StringUtils.isNotBlank(req.getClaimNo())) {
				// Query -- claim.select.sumPaidAmt
				output = claimCustomRepository.selectSumPaidAmt(req.getClaimNo(), req.getPolicyContractNo());
				res.setSumOfPaidAmountOC(fm.formatter(output == null ? "" : output));

				// Query -- claim.select.revSumPaidAmt
				output = claimCustomRepository.selectRevSumPaidAmt(req.getClaimNo(), req.getPolicyContractNo());
				res.setRevSumOfPaidAmt(fm.formatter(output == null ? "" : output));

			}
			// Pending
			list = queryImpl.selectList("claim.select.getClaimReserveList",
					new String[] { req.getClaimNo(), req.getPolicyContractNo() });
			log.info("Select Query=>" + list);
//  			
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> data = (Map<String, Object>) list.get(i);
				res.setPaidAmountOrigcurr(
						data.get("PAID_AMOUNT_OC") == null ? "" : data.get("PAID_AMOUNT_OC").toString());
				res.setPaymentRequestNo(
						data.get("PAYMENT_REQUEST_NO") == null ? "" : data.get("PAYMENT_REQUEST_NO").toString());
				res.setLossEstimateRevisedOrigCurr(data.get("LOSS_ESTIMATE_REVISED_OC") == null ? ""
						: data.get("LOSS_ESTIMATE_REVISED_OC").toString());
				res.setClaimNoterecommendations(
						data.get("CLAIM_NOTE_RECOMM") == null ? "" : data.get("CLAIM_NOTE_RECOMM").toString());
				res.setPaymentReference(
						data.get("PAYMENT_REFERENCE") == null ? "" : data.get("PAYMENT_REFERENCE").toString());
				res.setAdviceTreasury(
						data.get("ADVICE_TREASURY") == null ? "" : data.get("ADVICE_TREASURY").toString());
				res.setDate(data.get("INCEPTION_DT") == null ? "" : data.get("INCEPTION_DT").toString());
				res.setPaidAmountUSD(data.get("PAID_AMOUNT_DC") == null ? "" : data.get("PAID_AMOUNT_DC").toString());
				res.setLossEstimateRevisedUSD(data.get("LOSS_ESTIMATE_REVISED_DC") == null ? ""
						: data.get("LOSS_ESTIMATE_REVISED_DC").toString());

			}

			finalList.add(res);
			response.setCommonResponse(finalList);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	//saveContractDetailsMode6(ContractDetailsModeReq req) -- STARTS -- check date
	public ContractDetailsMode6Res saveContractDetailsMode6(ContractDetailsModeReq req) {
		ContractDetailsMode6Res response = new ContractDetailsMode6Res();
		List<ContractDetailsListMode6res> finalList = new ArrayList<ContractDetailsListMode6res>();
	    ContractDetailsListMode6res res = new ContractDetailsListMode6res();
		
		List<Tuple> list= new ArrayList<>();
		String output = null;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				//Query -- claim.select.sumPaidAmt
				output = claimCustomRepository.selectSumPaidAmt(req.getClaimNo(),req.getPolicyContractNo());
				res.setSumOfPaidAmountOC(fm.formatter(output == null ? "" : output));
	
				//Query -- claim.select.revSumPaidAmt
				output = claimCustomRepository.selectRevSumPaidAmt(req.getClaimNo(),req.getPolicyContractNo());
				res.setRevSumOfPaidAmt(fm.formatter(output == null ? ""	: output));
			}
			
			//Query -- claim.select.getClaimUpdateList
			list = claimCustomRepository.selectGetClaimUpdateList(req.getClaimNo(),req.getPolicyContractNo());
  			log.info("Select Query====>"+list);
  			for(int i=0; i<list.size();i++) {
	     		Tuple data= list.get(i);
		 		res.setLossEstimateRevisedOrigCurr(data.get("LOSS_ESTIMATE_REVISED_OC")==null?"":data.get("LOSS_ESTIMATE_REVISED_OC").toString());
				res.setLossEstimateRevisedUSD(data.get("LOSS_ESTIMATE_REVISED_DC")==null?"":data.get("LOSS_ESTIMATE_REVISED_DC").toString());
				res.setUpdateReference(data.get("UPDATE_REFERENCE")==null?"":data.get("UPDATE_REFERENCE").toString());
				res.setCliamupdateDate(data.get("INCEPTION_DT")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(data.get("INCEPTION_DT")).toString());
				
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
	//saveContractDetailsMode6(ContractDetailsModeReq req) -- ENDS
	
	//saveContractDetailsMode7(ContractDetailsModeReq req) -- STARTS
	public ContractDetailsMode7Res saveContractDetailsMode7(ContractDetailsModeReq req) {
		ContractDetailsMode7Res response = new ContractDetailsMode7Res();
		List<ContractDetailsListMode7res> finalList = new ArrayList<ContractDetailsListMode7res>();
	    ContractDetailsListMode7res res = new ContractDetailsListMode7res();
		
		List<Tuple> list= new ArrayList<>();
		String output = null;
		try {
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
				//Query -- claim.select.sumPaidAmt
				output = claimCustomRepository.selectSumPaidAmt(req.getClaimNo(),req.getPolicyContractNo());
				res.setSumOfPaidAmountOC(fm.formatter(output == null ? ""
						: output.toString()));
				
				//Query -- claim.select.revSumPaidAmt
				output = claimCustomRepository.selectRevSumPaidAmt(req.getClaimNo(),req.getPolicyContractNo());
				res.setRevSumOfPaidAmt(fm.formatter(output == null ? ""
					: output.toString()));
	
			}
			
			//Query -- claim.select.getClaimReviewQuery
			list = claimCustomRepository.selectGetClaimReviewQuery(req.getClaimNo(),req.getPolicyContractNo());
			for(int i=0; i<list.size();i++) {
	     		Tuple data= list.get(i);
  			
				res.setReviewDate(data.get("REVIEW_DT")==null?"":new SimpleDateFormat("dd/MM/yyyy")
								.format(data.get("REVIEW_DT")).toString());
				res.setReviewDoneBy(data.get("REVIEW_DONE_BY")==null?"":data.get("REVIEW_DONE_BY").toString());
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
	//saveContractDetailsMode7(ContractDetailsModeReq req) -- ENDS
	
	//saveclaimlist(ClaimListReq req) -- STARTS
	public ClaimListRes saveclaimlist(ClaimListReq req) {
		ClaimListRes response = new ClaimListRes();
		List<ClaimRes> finalList = new ArrayList<ClaimRes>();

		List<Tuple> allocists=new ArrayList<>();
		try{
			if(req.getFlag().equalsIgnoreCase("claim")){
				//query -- "partial.claim.select.claim.claimmaster"; not found
			}
			else{
				//Query -- claim.select.claim.claimmaster
				allocists = claimCustomRepository.selectClaimClaimmaster(req, null);
			}
			if("S".equalsIgnoreCase(req.getSearchType())){
				if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
					//query +=" AND UPPER(CUSTOMER_NAME) LIKE UPPER('%"+req.getCompanyNameSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "CUSTOMER_NAME");
				}
				if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
					//query +=" AND UPPER(BROKER_NAME) LIKE UPPER('%"+req.getBrokerNameSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "BROKER_NAME");
				}
				if(StringUtils.isNotBlank(req.getContractNoSearch())){
					//query +=" AND CONTRACT_NO LIKE ('%"+req.getContractNoSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "CONTRACT_NO");
				}
				if(StringUtils.isNotBlank(req.getClaimNoSearch())){
					//query +=" AND CLAIM_NO LIKE ('%"+req.getClaimNoSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "CLAIM_NO");
				}
				if(StringUtils.isNotBlank(req.getDateOfLossSearch())){
					//query +=" AND DATE_OF_LOSS LIKE ('%"+req.getDateOfLossSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "DATE_OF_LOSS");
				}
				if(StringUtils.isNotBlank(req.getClaimStatusSearch())){
					//query +=" AND UPPER(STATUS_OF_CLAIM) LIKE UPPER('%"+req.getClaimStatusSearch()+"%')";
					//Query -- claim.select.claim.claimmaster
					allocists = claimCustomRepository.selectClaimClaimmaster(req, "STATUS_OF_CLAIM");
				}
				}else{
					req.setCompanyNameSearch("");
					req.setBrokerNameSearch("");
					req.setDateOfLossSearch("");
					req.setClaimStatusSearch("");
					req.setContractNoSearch("");
					req.setClaimNoSearch("");
				}
		
		//query -- claim.select.date
		allocists = claimCustomRepository.selectDate(req.getBranchCode());
		
		for(int i=0 ; i<allocists.size() ; i++) {
			Tuple tempMap = allocists.get(i);
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
	
	public String getCliampaymnetCount(String claimNo, String contNo) {
		String result = "";
		try {
			//query -- getCliampaymnetCount;
			Integer count = claimCustomRepository.getCliampaymnetCount(claimNo, contNo);
			result = count == null ? "" : count.toString();
		} catch (Exception e) {
			log.debug("Exception @ {" + e + "}");
		}
		return result;
	}
	//saveclaimlist(ClaimListReq req) -- ENDS
	
	//claimListMode5(String claimNo, String contractNo) -- STARTS
	public ClaimListMode5Response claimListMode5(String claimNo, String contractNo) {
		 List<ClaimListMode5Res> response = new ArrayList<ClaimListMode5Res>();
		 ClaimListMode5Response res = new ClaimListMode5Response();
		 List<Tuple> cliamlists;
	
		try {
	
		//query -- claim.select.getClaimPaymentList;
		cliamlists = claimCustomRepository.selectGetClaimPaymentList(claimNo, contractNo);
		if(cliamlists.size()>0){
			for(int i=0;i<cliamlists.size();i++){
				Tuple result = cliamlists.get(i);
				final ClaimListMode5Res bean=new ClaimListMode5Res();
				bean.setPaymentRequestNo(result.get("PAYMENT_REQUEST_NO")==null?"":result.get("PAYMENT_REQUEST_NO").toString());
				bean.setLossEstimateRevisedOrigCurr(result.get("LOSS_ESTIMATE_REVISED_OC")==null?"":result.get("LOSS_ESTIMATE_REVISED_OC").toString());
				bean.setPaidAmountOrigCurr(result.get("PAID_AMOUNT_OC")==null?"":result.get("PAID_AMOUNT_OC").toString());
				bean.setDate(result.get("INCEPTION_DATE")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(result.get("INCEPTION_DATE")).toString());
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
	//claimListMode5(String claimNo, String contractNo) -- ENDS
	
	//claimListMode6(String contractNo, String claimNo) -- STARTS
	public ClaimListMode6Response claimListMode6(String contractNo, String claimNo) {
		List<ClaimListMode6Res> response = new ArrayList<ClaimListMode6Res>();
		ClaimListMode6Response res = new ClaimListMode6Response();
		List<Tuple> cliamlists;

		try {
			// query -- claim.select.getClaimReviewList
			cliamlists = claimCustomRepository.selectGetClaimReviewList(claimNo, contractNo);
			if (cliamlists.size() > 0) {
				for (int i = 0; i < cliamlists.size(); i++) {
					Tuple result = cliamlists.get(i);
					final ClaimListMode6Res bean = new ClaimListMode6Res();
					bean.setSNo(result.get("SNO") == null ? "" : result.get("SNO").toString());
					bean.setReviewDate(result.get("REVIEW_DATE") == null ? ""
							: new SimpleDateFormat("dd/MM/yyyy").format(result.get("REVIEW_DATE")).toString());
					bean.setReviewDoneBy(
							result.get("REVIEW_DONE_BY") == null ? "" : result.get("REVIEW_DONE_BY").toString());
					bean.setRemarks(result.get("REMARKS") == null ? "" : result.get("REMARKS").toString());
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
	//claimListMode6(String contractNo, String claimNo) -- ENDS
	
	//claimListMode3(ClaimTableListMode2Req req) -- STARTS
	public ClaimListMode3Response claimListMode3(ClaimTableListMode2Req req) {
		List<ClaimListMode3Res> response = new ArrayList<ClaimListMode3Res>();
		ClaimListMode3Response res = new ClaimListMode3Response();

		List<Tuple> list;
		try {
			// query -- claim.select.getClaimList
			list = claimCustomRepository.selectGetClaimList(req.getClaimNo(), req.getContractNo(), req.getBranchCode());
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					final ClaimListMode3Res bean = new ClaimListMode3Res();
					Tuple tempMap = list.get(j);
					bean.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
					bean.setDateofLoss(tempMap.get("DATE_OF_LOSS") == null ? ""
							: new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("DATE_OF_LOSS")).toString());
					bean.setLossEstimateOurshareOrigCurr(tempMap.get("LOSS_ESTIMATE_OS_OC") == null ? ""
							: fm.formatter(tempMap.get("LOSS_ESTIMATE_OS_OC").toString()));
					bean.setCurrecny(
							tempMap.get("CURRENCY_NAME") == null ? "" : tempMap.get("CURRENCY_NAME").toString());
					bean.setDate(tempMap.get("CREATED_DATE") == null ? ""
							: new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("CREATED_DATE")).toString());
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
	//claimListMode3(ClaimTableListMode2Req req) -- ENDS
	
	//claimTableListMode1(ClaimTableListReq req) -- STARTS
	public ClaimTableListMode1Res claimTableListMode1(ClaimTableListReq req) {
		log.info("CliamBusinessImpl cliamTableList || Enter");
		List<ClaimlistRes> cliamlists = new ArrayList<ClaimlistRes>();
		
		List<Tuple> list;
		ClaimTableListMode1Res res = new ClaimTableListMode1Res();
		try {
			//query -- claim.select.claimTableList
			list = claimCustomRepository.selectClaimTableList(req.getPolicyContractNo(), 
					req.getLayerNo(), req.getDepartmentId());
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				
				ClaimlistRes cliam = new ClaimlistRes();
				cliam.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
				cliam.setDateOfLoss(tempMap.get("DATE_OF_LOSS") == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("DATE_OF_LOSS")).toString());
				cliam.setCreatedDate(tempMap.get("CREATED_DATE") == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("CREATED_DATE")).toString());
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
	
	public ClaimTableListMode2Res claimTableListMode2(ClaimTableListMode2Req req) {
		ClaimTableListMode2Res response =new ClaimTableListMode2Res();
		List<ClaimTableListMode2ResList> cliamlists = new ArrayList<ClaimTableListMode2ResList>();
		
		List<Tuple> list;
		try{

			//query -- claim.select.getClaimUpdateList
			list = claimCustomRepository.selectGetClaimUpdateList(req.getClaimNo(), req.getContractNo());
		
		if(list!=null && list.size()>0){
			for (int j = 0; j < list.size(); j++) {
				ClaimTableListMode2ResList bean=new ClaimTableListMode2ResList();
				Tuple tempMap = list.get(j);
				
				bean.setLossEstimateRevisedOrigCurr(tempMap.get("LOSS_ESTIMATE_REVISED_OC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
				bean.setLossEstimateRevisedUSD(tempMap.get("LOSS_ESTIMATE_REVISED_DC")==null?"":fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_DC").toString()));
				bean.setUpdateReference(tempMap.get("UPDATE_REFERENCE")==null?"":tempMap.get("UPDATE_REFERENCE").toString());
				bean.setCliamupdateDate(tempMap.get("INCEPTION_DT")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("INCEPTION_DT")).toString());
				bean.setSNo(tempMap.get("SL_NO")==null?"":tempMap.get("SL_NO").toString());
				bean.setPolicyContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				bean.setClaimNo(tempMap.get("CLAIM_NO")==null?"":tempMap.get("CLAIM_NO").toString());
				cliamlists.add(bean);
				
			}
			response.setCommonResponse(cliamlists);
			
		}
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
	
	//claimPaymentEdit(ClaimPaymentEditReq req) -- STARTS
	public ClaimPaymentEditRes1 claimPaymentEdit(ClaimPaymentEditReq req) {
		ClaimPaymentEditRes1 res = new ClaimPaymentEditRes1();
		List<ClaimPaymentEditRes>  response = new ArrayList<ClaimPaymentEditRes>();
	
		try{
			List<Tuple> list =new ArrayList<>();
			//query -- GET_CLAIM_PAYMENT_DATA
			
			list = claimCustomRepository.getClaimPaymentData(req);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					ClaimPaymentEditRes bean = new ClaimPaymentEditRes();
					Tuple map = list.get(i);
					bean.setDate(map.get("INCEPTION_DATE")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(map.get("INCEPTION_DATE")).toString());
					bean.setPaymentRequestNo(map.get("PAYMENT_REQUEST_NO")==null?"":map.get("PAYMENT_REQUEST_NO").toString());
					bean.setPaymentReference(map.get("PAYMENT_REFERENCE")==null?"":map.get("PAYMENT_REFERENCE").toString());
					bean.setPaidClaimOs(map.get("PAID_CLAIM_OS_OC")==null?"":fm.formatter(map.get("PAID_CLAIM_OS_OC").toString()));
					bean.setSurveyOrFeeOs(map.get("SAF_OS_OC")==null?"":fm.formatter(map.get("SAF_OS_OC").toString()));
					bean.setOtherprofFeeOs(map.get("OTH_FEE_OS_OC")==null?"":fm.formatter(map.get("OTH_FEE_OS_OC").toString()));
					bean.setPaidAmountOrigCurr(map.get("PAID_AMOUNT_OC")==null?"":fm.formatter(map.get("PAID_AMOUNT_OC").toString()));
					bean.setRemarks(map.get("REMARKS")==null?"":map.get("REMARKS").toString());
					bean.setClaimPaymentNo(map.get("CLAIM_PAYMENT_NO")==null?"":map.get("CLAIM_PAYMENT_NO").toString());
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
	//claimPaymentEdit(ClaimPaymentEditReq req) -- ENDS
	
	// getShortname(String branchcode) -- STARTS
	public GetShortnameRes getShortname(String branchcode) {
		GetShortnameRes response = new GetShortnameRes();
		String Short = "";
		try {
			// query -- GET_SHORT_NAME

			String output = claimCustomRepository.getShortName(branchcode);
			Short = output == null ? "" : output;
			response.setCommonResponse(Short);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}
	// getShortname(String branchcode) -- ENDS
	
	// getContractNo(GetContractNoReq req) -- STARTS
	public GetContractNoRes1 getContractNo(GetContractNoReq req) {
		GetContractNoRes1 response = new GetContractNoRes1();
		GetContractNoRes res = new GetContractNoRes();
		List<Tuple> list = new ArrayList<>();
		try {

			if (StringUtils.isNotBlank(req.getContractNo()))
				// query -- GET_CONTRACT_NUMBER1
				list = claimCustomRepository.getContractNumber(req.getClaimNo(), req.getContractNo());

			else
				// query -- GET_CONTRACT_NUMBER
				list = claimCustomRepository.getContractNumber(req.getClaimNo(), null);

			if (list != null && list.size() > 0) {
				Tuple map = list.get(0);
				res.setContractNo(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
				if (!"2".equalsIgnoreCase(req.getProductId())) {
					res.setDepartmentId(map.get("CLAIM_CLASS") == null ? "" : map.get("CLAIM_CLASS").toString());
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
	// getContractNo(GetContractNoReq req) -- ENDS
	
	// ProposalNoRes savepaymentReciept(ProposalNoReq req) -- STARTS
	public ProposalNoRes savepaymentReciept(ProposalNoReq req) {

		ProposalNoRes response = new ProposalNoRes();
		String proposalNo = "";
		List<Tuple> list = new ArrayList<>();
		try {
			if (StringUtils.isNotBlank(req.getProductId()) && "1".equals(req.getProductId()))
				// query -- GET_FAC_PROPOSAL_NO
				list = claimCustomRepository.getFacProposalNo(req.getContarctno());

			else if ("2".equals(req.getProductId()))
				// query -- GET_PRO_PROPOSAL_NO
				list = claimCustomRepository.getProProposalNo(req.getContarctno(), req.getDepartmentId());
			
			else if ("3".equals(req.getProductId()))
				// query -- GET_XOL_PROPOSAL_NO
				list = claimCustomRepository.getXolProposalNo(req.getContarctno(), req.getLayerNo());

			if (list != null && list.size() > 0) {
				Tuple map = list.get(0);
				proposalNo = map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString();
				if (!"2".equalsIgnoreCase(req.getProductId())) {
					req.setDepartmentId(map.get("DEPT_ID") == null ? "" : map.get("DEPT_ID").toString());
				}
			}
			response.setCommonResponse(proposalNo);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}
	// ProposalNoRes savepaymentReciept(ProposalNoReq req) -- ENDS
	
	// productIdList(String branchCode) -- STARTS
	public ProductIdListRes1 productIdList(String branchCode) {
		ProductIdListRes1 res = new ProductIdListRes1();
		List<ProductIdListRes> response = new ArrayList<ProductIdListRes>();
		List<Tuple> list = new ArrayList<>();
		log.info("Enter ino product List");
		try {
			// query -- GET_PRODUC_ID_LIST
			list = claimCustomRepository.getProductIdList(branchCode);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					final ProductIdListRes bean = new ProductIdListRes();
					Tuple map = list.get(i);
					bean.setProductId(map.get("TMAS_PRODUCT_ID") == null ? "" : map.get("TMAS_PRODUCT_ID").toString());
					bean.setProductName(
							map.get("TMAS_PRODUCT_NAME") == null ? "" : map.get("TMAS_PRODUCT_NAME").toString());
					response.add(bean);
				}
				res.setCommonResponse(response);
				res.setMessage("Success");
				res.setIsError(false);

			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;

	}
	// productIdList(String branchCode) -- ENDS
	
	//insertCliamDetailsMode12(InsertCliamDetailsMode12Req req) -- STARTS
	public InsertCliamDetailsMode12Res insertCliamDetailsMode12(InsertCliamDetailsMode12Req req) {
		InsertCliamDetailsMode12Res response = new InsertCliamDetailsMode12Res();
		try {
			req.setExcRate(getExcRateForCliam(req.getClaimNo(), req.getPolicyContractNo()));
			TtrnClaimUpdation ttrnClaimUpdation = ttrnClaimUpdationMapper.toEntity(req);
			ttrnClaimUpdation
					.setSlNo(ttrnClaimUpdationMapper.format(GetSl_no(req.getClaimNo(), req.getPolicyContractNo())));

			// query -- claim.insert.getUpdationQuery
			ttrnClaimUpdationRepository.save(ttrnClaimUpdation);

			// query -- claim.update.closeClaim
			claimCustomRepository.updateCloseClaim("Closed", ttrnClaimUpdationMapper.parseDateLocal(req.getDate()),
					req.getClaimNo(), req.getPolicyContractNo());
			response.setMessage("Success");
			response.setIsError(false);
		}

		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;

	}
	
	private String getExcRateForCliam(final String claimNo,final String policyContractNo) {
		String excRate="";
		//query -- claim.select.excRate
		excRate = claimCustomRepository.selectExcRate(claimNo, policyContractNo);
		return excRate == null ? "" : excRate;
	}
	
	private String GetSl_no(final String claimNo,final String policyContractNo) {
		String result="";
		//query -- claim.select.maxSnoDTB3
		result = claimCustomRepository.selectMaxSnoDTB3(claimNo, policyContractNo);
		return result == null ? "" : result;
	}
	//insertCliamDetailsMode12(InsertCliamDetailsMode12Req req) -- ENDS
	
	//insertCliamDetailsMode8(InsertCliamDetailsMode8Req req) -- STARTS
	public InsertCliamDetailsMode8Res insertCliamDetailsMode8(InsertCliamDetailsMode8Req req) {
		InsertCliamDetailsMode8Res response = new InsertCliamDetailsMode8Res();
		try {

			if ("Yes".equalsIgnoreCase(req.getReverseClaimYN())) {

				req.setExcRate(getExcRateForCliam(req.getClaimNo(), req.getPolicyContractNo()));
				TtrnClaimUpdation ttrnClaimUpdation = ttrnClaimUpdationMapper.insertCliamDetailsMode8(req);
				ttrnClaimUpdation
						.setSlNo(ttrnClaimUpdationMapper.format(GetSl_no(req.getClaimNo(), req.getPolicyContractNo())));

				// query -- claim.insert.getUpdationQuery
				ttrnClaimUpdationRepository.save(ttrnClaimUpdation);
				// AfterInsert()
			} else {
				req.setExcRate(getExcRateForCliam(req.getClaimNo(), req.getPolicyContractNo()));

				// CliamDetailsArugumentsmode12
				// query -- claim.insert.getUpdationQuery
				TtrnClaimUpdation ttrnClaimUpdation = ttrnClaimUpdationMapper.insertCliamDetailsMode8(req);
				ttrnClaimUpdation
						.setSlNo(ttrnClaimUpdationMapper.format(GetSl_no(req.getClaimNo(), req.getPolicyContractNo())));

				// query -- claim.update.closeClaim
				claimCustomRepository.updateCloseClaim("Closed",
						ttrnClaimUpdationMapper.parseDateLocal(req.getClaimclosedDate()), req.getClaimNo(),
						req.getPolicyContractNo());

			}
			response.setMessage("Success");
			response.setIsError(false);
		}

		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	//insertCliamDetailsMode8(InsertCliamDetailsMode8Req req) -- ENDS
	
	// insertCliamDetailsMode3(InsertCliamDetailsMode3Req req) -- STARTS
	public InsertCliamDetailsMode3Res insertCliamDetailsMode3(InsertCliamDetailsMode3Req req) {
		InsertCliamDetailsMode3Res response = new InsertCliamDetailsMode3Res();
		boolean checking = true;
		try {
			if ("new".equalsIgnoreCase(req.getPaymentFlag())) {
				checking = ModeValidation(req.getPaymentRequestNo(), req.getClaimNo());
			}
			if (checking) {
				if ("new".equalsIgnoreCase(req.getPaymentFlag())) {

					req.setClaimPaymentNo(fm.getSequence("ClaimPayment", req.getProductId(), req.getDepartmentId(),
							req.getBranchCode(), "", req.getDate()));

					// CliamDetailsAruguments(req,mode);
					TtrnClaimPayment ttrnClaimPayment = ttrnClaimPayemntMapper.toEntity(req);
					ttrnClaimPayment.setSlNo(new BigDecimal(
							ttrnClaimPayemntMapper.format(GetSl_no(req.getClaimNo(), req.getPolicyContractNo()))));

					// query -- claim.select.maxResvId
					String maxId = claimCustomRepository.selectMaxResvId(req.getClaimNo(), req.getPolicyContractNo());
					maxId = maxId == null ? "" : maxId;

					ttrnClaimPayment.setClaimPaymentNo(new BigDecimal(maxId));
					// query -- claim.insert.payment
					ttrnClaimPaymentRepository.save(ttrnClaimPayment);

				} else {

					// query -- CLAIN_ARCH_INSERT
					TtrnClaimPayment payment = claimCustomRepository.clainArchInsert(req);
					TtrnClaimPaymentArchive ttrnClaimPaymentArchive = ttrnClaimPaymentArchiveMapper.toEntity(payment);
					ttrnClaimPaymentArchive.setAmendId(
							ttrnClaimPaymentArchiveMapper.formatLong(claimCustomRepository.selectMaxIdArchive(req)));
					;
					ttrnClaimPaymentArchiveRepository.save(ttrnClaimPaymentArchive);

					// query -- CLAIM_UPDATE_PAYMENT
					claimCustomRepository.claimUpdatePayment(req);

				}
				String amt = null;

				// query -- claim.select.sumPaidAmt
				String output = claimCustomRepository.selectSumPaidAmt(req.getClaimNo(), req.getPolicyContractNo());
				amt = output == null ? "" : output;
				
				if(StringUtils.isNotBlank(amt)) {
					claimCustomRepository.updateTotalAmtPaidTillDate(amt, req.getClaimNo(), req.getPolicyContractNo());
				}
				// query -- claim.select.Ri_recovery
				String ri = claimCustomRepository.selectRiRecovery(req.getClaimNo(), req.getPolicyContractNo());
				req.setRiRecovery(ri == null ? "" : ri);

				// query -- premium.sp.retroSplit
				claimCustomRepository.getPremiumSpRetroSplit(req);

			}
			response.setMessage("Success");
			response.setIsError(false);
		}

		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	private boolean ModeValidation(final String paymentRequestNo, final String claimNo) {
		boolean modes = false;
		Long count = 0L;
		// query -- claim.select.paymentReqNo
		count = claimCustomRepository.selectPaymentReqNo(claimNo, paymentRequestNo);

		if (count == 0) {
			modes = true;
		}
		return modes;
	}
	// insertCliamDetailsMode3(InsertCliamDetailsMode3Req req) -- ENDS
	
	//saveContractDetailsMode4(ContractDetailsModeReq req) -- STARTS
	public ContractDetailsMode4Res saveContractDetailsMode4(ContractDetailsModeReq req) {
		ContractDetailsMode4Res response = new ContractDetailsMode4Res();
		List<ContractDetailsListMode4res> finalList = new ArrayList<ContractDetailsListMode4res>();
		ContractDetailsListMode4res res = new ContractDetailsListMode4res();
		String ClaimNo = "";
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- claim.select.claimEdit
			list = claimCustomRepository.selectClaimEdit(req.getClaimNo(), req.getPolicyContractNo());

			for (int i = 0; i < list.size(); i++) {

				Tuple data = list.get(i);
				ClaimNo = (data.get("CLAIM_NO") == null ? "" : data.get("CLAIM_NO").toString());
				res.setClaimNo(ClaimNo);
				res.setStatusofclaim(data.get("STATUS_OF_CLAIM") == null ? "" : data.get("STATUS_OF_CLAIM").toString());
				res.setDepartmentClass(
						data.get("COVER_LIMIT_DEPTID") == null ? "" : data.get("COVER_LIMIT_DEPTID").toString());
				res.setDateofLoss(data.get("LOSS_DATE") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("LOSS_DATE")).toString());
				res.setReportDate(data.get("REP_DATE") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("REP_DATE")).toString());
				res.setLossDetails(data.get("LOSS_DETAILS") == null ? "" : data.get("LOSS_DETAILS").toString());
				res.setCauseofLoss(data.get("CAUSE_OF_LOSS") == null ? "" : data.get("CAUSE_OF_LOSS").toString());
				res.setLocation(list.get(i).get("LOCATION") == null ? "" : list.get(i).get("LOCATION").toString());
				res.setLossEstimateOrigCurr(fm.formatter(
						data.get("LOSS_ESTIMATE_OC") == null ? "" : data.get("LOSS_ESTIMATE_OC").toString()));
				res.setLossEstimateOurshareOrigCurr(fm.formatter(
						data.get("LOSS_ESTIMATE_OS_OC") == null ? "" : data.get("LOSS_ESTIMATE_OS_OC").toString()));
				res.setExcRate(
						exchRateFormat(data.get("EXCHANGE_RATE") == null ? "" : data.get("EXCHANGE_RATE").toString()));
				res.setRiRecovery(data.get("RI_RECOVERY") == null ? "" : data.get("RI_RECOVERY").toString());
				res.setCreatedby(data.get("CREATED_BY") == null ? "" : data.get("CREATED_BY").toString());
				res.setCreatedDate(data.get("CREATED_DT") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("CREATED_DT")).toString());
				res.setCurrency(data.get("CURRENCY") == null ? "" : data.get("CURRENCY").toString());
				res.setCurrencyName(data.get("CURRENCY_NAME") == null ? "" : data.get("CURRENCY_NAME").toString());
				res.setRemarks(data.get("REMARKS") == null ? "" : data.get("REMARKS").toString());
				res.setRiskCode(data.get("RISK_CODE") == null ? "" : data.get("RISK_CODE").toString());
				res.setAccumulationCode(
						data.get("ACCUMULATION_CODE") == null ? "" : data.get("ACCUMULATION_CODE").toString());
				res.setEventCode(data.get("EVENT_CODE") == null ? "" : data.get("EVENT_CODE").toString());
				res.setInsuredName(data.get("INSURED_NAME") == null ? "" : data.get("INSURED_NAME").toString());
				res.setRecordFees(data.get("RECORD_FEES_CRE_RESERVE") == null ? ""
						: data.get("RECORD_FEES_CRE_RESERVE").toString());
				res.setSurveyorAdjesterPerOC(
						fm.formatter(data.get("SAF_100_OC") == null ? "" : data.get("SAF_100_OC").toString()));
				res.setSurveyorAdjesterOurShareOC(
						fm.formatter(data.get("SAF_OS_OC") == null ? "" : data.get("SAF_OS_OC").toString()));
				res.setOtherProfessionalPerOc(
						fm.formatter(data.get("OTH_FEE_100_OC") == null ? "" : data.get("OTH_FEE_100_OC").toString()));
				res.setProfessionalOurShareOc(
						fm.formatter(data.get("OTH_FEE_OS_OC") == null ? "" : data.get("OTH_FEE_OS_OC").toString()));
				res.setIbnrPerOc(
						fm.formatter(data.get("C_IBNR_100_OC") == null ? "" : data.get("C_IBNR_100_OC").toString()));
				res.setIbnrOurShareOc(
						fm.formatter(data.get("C_IBNR_OS_OC") == null ? "" : data.get("C_IBNR_OS_OC").toString()));
				res.setGrossLossFGU(fm.formatter(
						data.get("GROSSLOSS_FGU_OC") == null ? "" : data.get("GROSSLOSS_FGU_OC").toString()));
				res.setRecordIbnr(data.get("RECORD_IBNR") == null ? "" : data.get("RECORD_IBNR").toString());
				res.setCedentClaimNo(data.get("CEDENT_CLAIM_NO") == null ? "" : data.get("CEDENT_CLAIM_NO").toString());
				res.setReservePositionDate(data.get("RES_POS_DATE") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("RES_POS_DATE")).toString());
				res.setClaimdepartId(data.get("CLAIM_CLASS") == null ? "" : data.get("CLAIM_CLASS").toString());
				res.setSubProfitId(data.get("CLAIM_SUBCLASS") == null ? "" : data.get("CLAIM_SUBCLASS").toString());
				res.setReopenDate(data.get("REOPENED_DATE") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("REOPENED_DATE")).toString());
				res.setReputedDate(data.get("REPUDATE_DATE") == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(data.get("REPUDATE_DATE")).toString());
			}

			// query -- GET_EDIT_ACC_DATA
			list = claimCustomRepository.getEditAccData(ClaimNo, req.getPolicyContractNo());

			for (int i = 0; i < list.size(); i++) {
				Tuple data = list.get(i);
				res.setRiskCode(data.get("RISK_CODE") == null ? "" : (data.get("RISK_CODE").toString()));
				res.setAccumulationCode(
						data.get("AGGREGATE_CODE") == null ? "" : (data.get("AGGREGATE_CODE").toString()));
				res.setEventCode(data.get("EVENT_CODE") == null ? "" : (data.get("EVENT_CODE").toString()));
			}

			finalList.add(res);
			response.setCommonResponse(finalList);
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

	private String exchRateFormat(String value) {
		String output = "0.00";
		if (StringUtils.isNotBlank(value)) {
			System.out.println(value);
			double doublevalue = Double.parseDouble(value);
			DecimalFormat myFormatter = new DecimalFormat("#####.##########");
			output = myFormatter.format(doublevalue);
		}
		return output;
	}
	//saveContractDetailsMode4(ContractDetailsModeReq req) -- ENDS
	
	// claimTableListMode7(ClaimTableListMode2Req req) -- STARTS
	public ClaimTableListMode7Res claimTableListMode7(ClaimTableListMode2Req req) {
		ClaimTableListMode7Res response = new ClaimTableListMode7Res();
		List<ClaimListMode7ResList> cliamlists = new ArrayList<ClaimListMode7ResList>();
		ClaimListMode7Res1 res = new ClaimListMode7Res1();

		List<Map<String, Object>> list;
		try {

			// query -- clime.select.getClaimRESERVEList
			list = claimCustomRepository.selectGetClaimReserveList(req.getClaimNo(), req.getContractNo());
			double a = 0.0;
			double b = 0.0;
			double c = 0.0;
			double d = 0.0;
			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					ClaimListMode7ResList bean = new ClaimListMode7ResList();
					Map<String, Object> tempMap = (Map<String, Object>) list.get(j);

					bean.setLossEstimateRevisedOrigCurr(tempMap.get("LOSS_ESTIMATE_REVISED_OC") == null ? ""
							: fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
					bean.setLossEstimateRevisedUSD(tempMap.get("LOSS_ESTIMATE_REVISED_DC") == null ? ""
							: fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_DC").toString()));
					bean.setUpdateReference(
							tempMap.get("UPDATE_REFERENCE") == null ? "" : tempMap.get("UPDATE_REFERENCE").toString());
					bean.setCliamupdateDate(
							tempMap.get("INCEPTION_DT") == null ? "" : tempMap.get("INCEPTION_DT").toString());
					bean.setSNo(tempMap.get("SL_NO") == null ? "" : tempMap.get("SL_NO").toString());
					bean.setPolicyContractNo(
							tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
					bean.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());

					bean.setLero2a(tempMap.get("LOSS_ESTIMATE_REVISED_OC_2A") == null ? ""
							: fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC_2A").toString()));
					bean.setLero2b(tempMap.get("PAID_CLAIM_OS_OC_2B") == null ? ""
							: fm.formatter(tempMap.get("PAID_CLAIM_OS_OC_2B").toString()));
					bean.setLero2c(tempMap.get("OC_OS_AMOUNT_2C") == null ? ""
							: fm.formatter(tempMap.get("OC_OS_AMOUNT_2C").toString()));
					bean.setSaf3a(tempMap.get("SAF_OS_OC_3A") == null ? ""
							: fm.formatter(tempMap.get("SAF_OS_OC_3A").toString()));
					bean.setSaf3b(tempMap.get("SAF_OS_OC_3B") == null ? ""
							: fm.formatter(tempMap.get("SAF_OS_OC_3B").toString()));
					bean.setSaf3c(tempMap.get("SURVEYOR_OS_AMT_3C") == null ? ""
							: fm.formatter(tempMap.get("SURVEYOR_OS_AMT_3C").toString()));
					bean.setOfos4a(tempMap.get("OTH_FEE_OS_OC_4A") == null ? ""
							: fm.formatter(tempMap.get("OTH_FEE_OS_OC_4A").toString()));
					bean.setOfos4b(tempMap.get("OTH_FEE_OS_OC_4B") == null ? ""
							: fm.formatter(tempMap.get("OTH_FEE_OS_OC_4B").toString()));
					bean.setOfos4c(tempMap.get("OTHER_PROFESS_OS_AMT_4C") == null ? ""
							: fm.formatter(tempMap.get("OTHER_PROFESS_OS_AMT_4C").toString()));
					bean.setTotala(tempMap.get("TOTAL_A") == null ? ""
							: fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_A").toString())))));
					bean.setTotalb(tempMap.get("TOTAL_B") == null ? ""
							: fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_B").toString())))));
					bean.setTotalc(tempMap.get("TOTAL_C") == null ? ""
							: fm.formatter(Double.toString((Double.parseDouble(tempMap.get("TOTAL_C").toString())))));
					bean.setReInspremiumOS(tempMap.get("REINSPREMIUM_OURSHARE_OC") == null ? ""
							: fm.formatter(tempMap.get("REINSPREMIUM_OURSHARE_OC").toString()));
					bean.setCibnr100Oc(tempMap.get("C_IBNR_OS_OC") == null ? ""
							: fm.formatter(tempMap.get("C_IBNR_OS_OC").toString()));

					a = a + Double.parseDouble(tempMap.get("PAID_CLAIM_OS_OC_2B") == null ? ""
							: tempMap.get("PAID_CLAIM_OS_OC_2B").toString());
					b = b + Double.parseDouble(
							tempMap.get("SAF_OS_OC_3B") == null ? "" : tempMap.get("SAF_OS_OC_3B").toString());
					c = c + Double.parseDouble(
							tempMap.get("OTH_FEE_OS_OC_4B") == null ? "" : tempMap.get("OTH_FEE_OS_OC_4B").toString());
					d = d + Double.parseDouble(tempMap.get("TOTAL_B") == null ? ""
							: Double.toString(Double.parseDouble(tempMap.get("TOTAL_B").toString())));

					cliamlists.add(bean);

				}
				res.setCommonResponse(cliamlists);

				res.setTotalORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(a)))));
				res.setTotalSApaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(b)))));
				res.setTotalOPpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(c)))));
				res.setTotalTORpaidAmount(fm.formatter(Double.toString(Double.parseDouble(String.valueOf(d)))));

			}

			// query -- premium.select.currecy.name
			String name = claimCustomRepository.selectCurrecyName(req.getBranchCode());
			res.setCurrencyName(name == null ? "" : name);

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
	// claimTableListMode7(ClaimTableListMode2Req req) -- ENDS
	
	// claimListMode4(ClaimListMode4Req req) -- STARTS
	public ClaimListMode4Response claimListMode4(ClaimListMode4Req req) {
		ClaimListMode4Response res = new ClaimListMode4Response();
		List<ClaimListMode4Res> response = new ArrayList<ClaimListMode4Res>();

		String maxno = "";
		try {
			List<Map<String, Object>> claimlist = new ArrayList<Map<String, Object>>();
			// query = "claim.select.getClaimReserveList";
			claimlist = claimCustomRepository.selectGetClaimReserveListModeFour(req.getClaimNo(), req.getContractNo());

			// query -- payment.select.maxno
			maxno = claimCustomRepository.selectMaxNo(req.getClaimNo(), req.getContractNo());
			maxno = maxno == null ? "" : maxno;

			if (claimlist.size() > 0) {
				for (int i = 0; i < claimlist.size(); i++) {
					Map<String, Object> result = claimlist.get(i);

					final ClaimListMode4Res bean = new ClaimListMode4Res();
					bean.setPaymentRequestNo(result.get("PAYMENT_REQUEST_NO") == null ? ""
							: result.get("PAYMENT_REQUEST_NO").toString());
					bean.setPaidAmountOrigCurr(result.get("PAID_AMOUNT_OC") == null ? ""
							: fm.formatter(result.get("PAID_AMOUNT_OC").toString()));
					bean.setDate(result.get("INCEPTION_DT") == null ? "" : result.get("INCEPTION_DT").toString());
					bean.setClaimPaymentNo(
							result.get("CLAIM_PAYMENT_NO") == null ? "" : result.get("CLAIM_PAYMENT_NO").toString());
					bean.setSNo(result.get("RESERVE_ID") == null ? "" : result.get("RESERVE_ID").toString());
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
	// claimListMode4(ClaimListMode4Req req) -- ENDS
	
	// getReInsValue(GetReInsValueReq req -- STARTS
	public String getReInsValue(GetReInsValueReq req) {
		double result = 0;
		String finalVal = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<GetReInsValueListReq> req1 = new ArrayList<GetReInsValueListReq>();

		try {
			for (int i = 0; i < req1.size(); i++) {
				if ("This Trxn".equalsIgnoreCase(req1.get(i).getClaimPaymentNoList())) {
					// query -- GET_RIP_VALUES
					list = claimCustomRepository.getRipValues(req, req1.get(i).getPaymentCoverPlus());
					if (list.size() > 0) {
						for (int j = 0; j < list.size(); j++) {
							Map<String, Object> map = list.get(j);
							String type = map.get("REINST_TYPE") == null ? "0"
									: map.get("REINST_TYPE").toString().replaceAll(",", "");

							String totalNoCol = StringUtils.isBlank(req.getTotalBookedPremium()) ? "0"
									: req.getTotalBookedPremium().replaceAll(",", "");
							String amtPer = map.get("AMOUNT_PERCENT") == null ? "0"
									: map.get("AMOUNT_PERCENT").toString().replaceAll(",", "");
							String minAmt = map.get("MIN_AMOUNT_PERCENT") == null ? "0"
									: map.get("MIN_AMOUNT_PERCENT").toString().replaceAll(",", "");
							String time = map.get("MIN_TIME_PERCENT") == null ? "0"
									: map.get("MIN_TIME_PERCENT").toString().replaceAll(",", "");
							double AmountPer = Double.parseDouble(amtPer) / 100;
							String val = getSumInsuredVal(req);
							String total = Double
									.toString(Double.parseDouble(req1.get(i).getClaimPaidOC().replaceAll(",", ""))
											/ Double.parseDouble(val.replaceAll(",", "")));

							if ("PRA".equalsIgnoreCase(type)) {
								minAmt = Double.toString(Double.parseDouble(minAmt) / 100);
								double amount = maxValue(Double.parseDouble(minAmt), Double.parseDouble(total));
								double ans = Double.parseDouble(StringUtils.isBlank(req.getTotalBookedPremium()) ? "0"
										: req.getTotalBookedPremium().replaceAll(",", "")) * amount;
								ans = ans * AmountPer;
								result += ans;
							} else if ("PRT".equalsIgnoreCase(type)) {
								double dateCom = getDateDetails(req);
								double minTime = Double.parseDouble(time) / 100;
								double amount = maxValue(dateCom, minTime);
								double ans = Double.parseDouble(totalNoCol) * Double.parseDouble(total) * amount;
								ans = ans * AmountPer;
								result += ans;
							} else if ("PRAT".equalsIgnoreCase(type)) {
								minAmt = Double.toString(Double.parseDouble(minAmt) / 100);
								double amount = maxValue(Double.parseDouble(minAmt), Double.parseDouble(total));

								double ans = 0;

								double dateCom = getDateDetails(req);
								double minTime = Double.parseDouble(time) / 100;
								double amount1 = maxValue(dateCom, minTime);
								double ans1 = Double.parseDouble(totalNoCol) * amount1 * amount;
								ans = ans1 * AmountPer;
								result += ans;
							} else if ("FREE".equalsIgnoreCase(type)) {
								result += AmountPer;
							}
						}
					}

				}
			}
			finalVal = fm.formatter(Double.toString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalVal;

	}

	private String getSumInsuredVal(GetReInsValueReq req) {
		String val = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			// query -- COVER_SUM_INSURED_VAL
			list = claimCustomRepository.coverSumInsuredVal(req);
			if (!CollectionUtils.isEmpty(list)) {
				val = list.get(0).get("SUM_INSURED_VAL") == null ? "" : list.get(0).get("SUM_INSURED_VAL").toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	private double maxValue(double minAmt, double rdsAmt) {
		try {
			if (minAmt < rdsAmt) {
				minAmt = rdsAmt;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return minAmt;
	}

	private double getDateDetails(GetReInsValueReq req) {
		String inceptionDate = "";
		double ans = 0;
		String ExpDate = "";
		String dateOfLoss = "";
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- GET_DATE_OF_LOSS
			dateOfLoss = claimCustomRepository.getDateOfLoss(req);
			dateOfLoss = dateOfLoss == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(dateOfLoss);

			// query -- GET_RDS_DATE
			list = claimCustomRepository.getRdsDate(req);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Tuple map = list.get(i);
					inceptionDate = map.get("RSK_INCEPTION_DATE") == null ? ""
							: map.get("RSK_INCEPTION_DATE").toString();
					ExpDate = map.get("RSK_EXPIRY_DATE") == null ? "" : map.get("RSK_EXPIRY_DATE").toString();
				}
			}
			String format = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			java.util.Date incep = sdf.parse(inceptionDate);
			java.util.Date exp = sdf.parse(ExpDate);
			java.util.Date lossDate = sdf.parse(dateOfLoss);
			double diffInDays = (double) ((exp.getTime() - incep.getTime()) / (1000 * 60 * 60 * 24));
			diffInDays = diffInDays + 1;
			double diff = (double) ((exp.getTime() - lossDate.getTime()) / (1000 * 60 * 60 * 24));

			ans = diff / diffInDays;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	// getReInsValue(GetReInsValueReq req -- ENDS
	
	// saveContractDetailsMode1(ContractDetailsReq req) -- STARTS
	public ContractDetailsMode1Res saveContractDetailsMode1(ContractDetailsReq req) {
		ContractDetailsMode1Res response = new ContractDetailsMode1Res();
		List<ContractDetailsListMode1res> finalList = new ArrayList<ContractDetailsListMode1res>();
		ContractDetailsListMode1res res = new ContractDetailsListMode1res();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			if ("1".equalsIgnoreCase(req.getProductId()))
				// query -- claim.select.facGetCliamQuery
				list=claimCustomRepository.selectFacGetCliamQuery(req.getProposalNo(), req.getProductId(),
						req.getBranchCode());
			else
				// query -- claim.select.xolOrTeatyGetClimeQuery
				list=claimCustomRepository.selectXolOrTeatyGetClimeQuery(req.getProposalNo(), req.getProductId(),
						req.getBranchCode());

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String,Object> contractDetails=(Map<String,Object>) list.get(i);
					res.setPolicyContractNo(contractDetails.get("RSK_CONTRACT_NO")==null?"":contractDetails.get("RSK_CONTRACT_NO").toString());
					res.setAmendId(contractDetails.get("RSK_ENDORSEMENT_NO")==null?"":contractDetails.get("RSK_ENDORSEMENT_NO").toString());
					res.setCedingcompanyName(contractDetails.get("CEDING_COMPANY")==null?"":contractDetails.get("CEDING_COMPANY").toString());
					res.setCedingCompanyCode(contractDetails.get("RSK_CEDINGID")==null?"":contractDetails.get("RSK_CEDINGID").toString());
					res.setProposalNo(contractDetails.get("RSK_PROPOSAL_NUMBER")==null?"":contractDetails.get("RSK_PROPOSAL_NUMBER").toString());
					res.setDepartmentId(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
					res.setDepartmentClass(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
					res.setUwYear(contractDetails.get("RSK_UWYEAR")==null?"":contractDetails.get("RSK_UWYEAR").toString());
					res.setCurrecny(contractDetails.get("RSK_ORIGINAL_CURR")==null?"":contractDetails.get("RSK_ORIGINAL_CURR").toString());
					if("1".equalsIgnoreCase(req.getProductId())){
					res.setSignedShare(contractDetails.get("SHARE_SIGNED")==null?"":contractDetails.get("SHARE_SIGNED").toString());
					res.setLimitOurshareUSD(contractDetails.get("SUM_INSURED_OUR_SHARE_DC")==null?"":fm.formatter(contractDetails.get("SUM_INSURED_OUR_SHARE_DC").toString()));
					res.setSumInsOSOC(contractDetails.get("SUM_INSURED_OUR_SHARE_OC")==null?"":fm.formatter(contractDetails.get("SUM_INSURED_OUR_SHARE_OC").toString()));
					res.setSumInsOSDC(contractDetails.get("SUM_INSURED_OUR_SHARE_DC")==null?"":fm.formatter(contractDetails.get("SUM_INSURED_OUR_SHARE_DC").toString()));
					list =queryImpl.selectList("claim.select.tmasDeptName",new String[]{res.getDepartmentId(),req.getProductId(),req.getBranchCode()});
					if (!CollectionUtils.isEmpty(list)) {
						res.setDepartmentName(list.get(0).get("TMAS_NAME") == null ? ""
								: list.get(0).get("TMAS_NAME").toString());
					}

					}else
					{
					res.setSignedShare(contractDetails.get("RSK_SHARE_SIGNED")==null?"":contractDetails.get("RSK_SHARE_SIGNED").toString());
					res.setLimitOrigCurr(contractDetails.get("RSK_LIMIT_OC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OC").toString()));
					res.setLimitOurshareUSD(contractDetails.get("RSK_LIMIT_DC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_DC").toString()));
					res.setSumInsOSOC(contractDetails.get("RSK_LIMIT_OS_OC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OS_OC").toString()));
					res.setSumInsOSDC(contractDetails.get("RSK_LIMIT_OS_DC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OS_DC").toString()));
					res.setDepartmentName(contractDetails.get("TMAS_DEPARTMENT_NAME")==null?"":contractDetails.get("TMAS_DEPARTMENT_NAME").toString());
					}
					res.setSubProfitCenter(contractDetails.get("TMAS_SPFC_NAME")==null?"":contractDetails.get("TMAS_SPFC_NAME").toString());
					res.setRetention(contractDetails.get("RSK_CEDANT_RETENTION")==null?"":fm.formatter(contractDetails.get("RSK_CEDANT_RETENTION").toString()));
					res.setFrom(contractDetails.get("INCP_DATE")==null?"":contractDetails.get("INCP_DATE").toString());
					res.setTo(contractDetails.get("EXP_DATE")==null?"":contractDetails.get("EXP_DATE").toString());
					res.setTreatyName(contractDetails.get("RSK_TREATYID")==null?"":contractDetails.get("RSK_TREATYID").toString());
					res.setBrokercode(contractDetails.get("RSK_BROKERID")==null?"":contractDetails.get("RSK_BROKERID").toString());
					res.setBrokerName(contractDetails.get("BROKER_NAME")==null?"":contractDetails.get("BROKER_NAME").toString());
					res.setAcceptenceDate(contractDetails.get("RSK_ACCOUNT_DATE")==null?"":contractDetails.get("RSK_ACCOUNT_DATE").toString());
					String count="";
					if("2".equals(req.getProductId())){
					count= dropDowmImpl.getCombinedClass(req.getBranchCode(),req.getProductId(),res.getDepartmentId());
					}
					res.setClaimdepartId(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
					res.setConsubProfitId(contractDetails.get("RSK_SPFCID")==null?"":contractDetails.get("RSK_SPFCID").toString());
					res.setInsuredName(contractDetails.get("RSK_INSURED_NAME")==null?"":contractDetails.get("RSK_INSURED_NAME").toString());
					res.setProposalType(contractDetails.get("RSK_PROPOSAL_TYPE")==null?"":contractDetails.get("RSK_PROPOSAL_TYPE").toString());
					res.setBasis(contractDetails.get("RSK_BASIS")==null?"":contractDetails.get("RSK_BASIS").toString());

					if("3".equalsIgnoreCase(req.getProductId()))
					{
					res.setNatureofCoverage(contractDetails.get("RSK_PF_COVERED")==null?"":contractDetails.get("RSK_PF_COVERED").toString());
					res.setReinstatementPremium(contractDetails.get("RSK_REINSTATEMENT_PREMIUM")==null?"":contractDetails.get("RSK_REINSTATEMENT_PREMIUM").toString());
					}
					if("2".equalsIgnoreCase(req.getProductId()))
					{
					res.setNatureofCoverage(contractDetails.get("RSK_RISK_COVERED")==null?"":contractDetails.get("RSK_RISK_COVERED").toString());
					res.setCashLossOSOC(fm.formatter(((Double.parseDouble(contractDetails.get("RSK_CASHLOSS_LMT_OC").toString()==null?"0":contractDetails.get("RSK_CASHLOSS_LMT_OC").toString())*Double.parseDouble(contractDetails.get("RSK_SHARE_SIGNED")==null?"0":contractDetails.get("RSK_SHARE_SIGNED").toString()))/100.0)+""));
					res.setCashLossOSDC(fm.formatter(((Double.parseDouble(contractDetails.get("RSK_CASHLOSS_LMT_DC").toString()==null?"0":contractDetails.get("RSK_CASHLOSS_LMT_DC").toString())*Double.parseDouble(contractDetails.get("RSK_SHARE_SIGNED").toString()==null?"0":contractDetails.get("RSK_SHARE_SIGNED").toString()))/100.0)+""));
					}

					}
				finalList.add(res);
				response.setCommonResponse(finalList);
				response.setMessage("Success");
				response.setIsError(false);
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);

		}
		return response;

	}
	// saveContractDetailsMode1(ContractDetailsReq req) -- ENDS
	
	//claimPaymentList(ClaimPaymentListReq req) -- STARTS
	public ClaimPaymentListRes1 claimPaymentList(ClaimPaymentListReq req) {
		ClaimPaymentListRes1 res = new ClaimPaymentListRes1();
		List<Map<String, Object>> allocists = new ArrayList<Map<String, Object>>();
		List<ClaimPaymentListRes> finalList = new ArrayList<ClaimPaymentListRes>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int count = 0;
		try {
			if (req.getFlag().equalsIgnoreCase("claim")) {
				// query -- partial.claim.select.getpaymentlist
				allocists = claimCustomRepository.partialSelectGetpaymentlist(req);
			} else {
				// query -- claim.select.getpaymentlist
				allocists = claimCustomRepository.selectGetpaymentlist(req);
			}

			for (int i = 0; i < allocists.size(); i++) {
				Map<String, Object> tempMap = (Map<String, Object>) allocists.get(i);
				ClaimPaymentListRes tempBean = new ClaimPaymentListRes();
				tempBean.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
				tempBean.setPolicyContractNo(
						tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
				tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
				tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
				tempBean.setCedingcompanyName(
						tempMap.get("CUSTOMER_NAME") == null ? "" : tempMap.get("CUSTOMER_NAME").toString());
				tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
				tempBean.setInceptionDate(
						tempMap.get("INCEPTION_DATE") == null ? "" : tempMap.get("INCEPTION_DATE").toString());
				tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : tempMap.get("EXPIRY_DATE").toString());
				tempBean.setProductId(tempMap.get("PRODUCT_ID") == null ? "" : tempMap.get("PRODUCT_ID").toString());
				tempBean.setProductName(
						tempMap.get("TMAS_PRODUCT_NAME") == null ? "" : tempMap.get("TMAS_PRODUCT_NAME").toString());
				tempBean.setPaymentRequestNo(
						tempMap.get("PAYMENT_REQUEST_NO") == null ? "" : tempMap.get("PAYMENT_REQUEST_NO").toString());
				tempBean.setPaidAmountOrigcurr(fm.formatter(
						tempMap.get("PAID_AMOUNT_OC") == null ? "" : tempMap.get("PAID_AMOUNT_OC").toString()));
				tempBean.setLossEstimateRevisedOrigCurr(
						fm.formatter(tempMap.get("LOSS_ESTIMATE_REVISED_OC") == null ? ""
								: tempMap.get("LOSS_ESTIMATE_REVISED_OC").toString()));
				tempBean.setInceptionDt(
						tempMap.get("INCEPTION_DT") == null ? "" : tempMap.get("INCEPTION_DT").toString());
				tempBean.setClaimPaymentNo(
						tempMap.get("CLAIM_PAYMENT_NO") == null ? "" : tempMap.get("CLAIM_PAYMENT_NO").toString());
				tempBean.setSNo(tempMap.get("RESERVE_ID") == null ? "" : tempMap.get("RESERVE_ID").toString());
				tempBean.setSettlementStatus(
						tempMap.get("SETTLEMENT_STATUS") == null ? "" : tempMap.get("SETTLEMENT_STATUS").toString());
				tempBean.setTransactionType(
						tempMap.get("TRANS_TYPE") == null ? "" : tempMap.get("TRANS_TYPE").toString());
				tempBean.setTransactionNumber(
						tempMap.get("RECEIPT_NO") == null ? "" : tempMap.get("RECEIPT_NO").toString());
				tempBean.setDepartmentId(tempMap.get("DEPT_ID") == null ? "" : tempMap.get("DEPT_ID").toString());
				String maxno = "";
				String output = "";
				try {
					// query -- payment.select.count.allocatedYN
					output = claimCustomRepository.selectCountAllocatedYN(tempBean.getPolicyContractNo(),
							tempBean.getClaimNo(), tempBean.getLayerNo());
					count = Integer.valueOf(output == null ? "" : list.get(0).get("ALLOCATEDYN").toString());

					if (count == 0) {
						tempBean.setAllocatedYN("N");
					} else if (count >= 1) {
						tempBean.setAllocatedYN("Y");
					}

					// query -- GET_STATUS_OF_CLAIM
					output = claimCustomRepository.getStatusOfClaim(tempBean.getPolicyContractNo(),
							tempBean.getClaimNo(), tempBean.getLayerNo());
					tempBean.setStatusofclaim(output == null ? "" : output);

					// query -- claim.select.maxno
					output = claimCustomRepository.selectMaxno(tempBean.getClaimNo(), tempBean.getPolicyContractNo());
					maxno = output == null ? "" : output;
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("Exception " + e);
				}
				if (StringUtils.isNotBlank(tempBean.getInceptionDt())) {
					if (Integer.valueOf(dropDowmImpl.Validatethree(req.getBranchCode(), req.getDate())) == 0) {
						tempBean.setTransOpenperiodStatus("N");
					} else if (!tempMap.get("RESERVE_ID").toString().equalsIgnoreCase(maxno)) {
						tempBean.setTransOpenperiodStatus("N");
					} else {
						tempBean.setTransOpenperiodStatus("Y");
					}
				}
				finalList.add(tempBean);
			}

			res.setCommonResponse(finalList);
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
	//claimPaymentList(ClaimPaymentListReq req) -- ENDS
	
	//contractidetifierlist(ContractidetifierlistReq req) -- STARTS
	public ContractidetifierlistRes1 contractidetifierlist(ContractidetifierlistReq req) {
		ContractidetifierlistRes1 res = new ContractidetifierlistRes1();
		List<ContractidetifierlistRes> finalList = new ArrayList<ContractidetifierlistRes>();
		log.info("Enter into PremiumList");
		try{
		//query="contract.identifier.list";	
			List<Tuple>	allocists = claimCustomRepository.contractIdentifierList(req);
		for(int i=0 ; i<allocists.size() ; i++) {
			Tuple tempMap = allocists.get(i);
			ContractidetifierlistRes tempBean=new ContractidetifierlistRes();
			tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
			tempBean.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
			tempBean.setCedingcompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
		//	tempBean.setTransactionNumber(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setProductId(req.getProductId());
			tempBean.setDeptId(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
			tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE")==null?"":tempMap.get("EXPIRY_DATE").toString());
			tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
		//	tempBean.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
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
	//contractidetifierlist(ContractidetifierlistReq req) -- ENDS

	public InsertCliamDetailsMode2Res insertCliamDetailsMode2(InsertCliamDetailsMode2Req req) {
			InsertCliamDetailsMode2Res response = new InsertCliamDetailsMode2Res();
				try {
					
					if(StringUtils.isEmpty(req.getClaimNo()))
					{
					req.setClaimNo(fm.getSequence("ClaimBooking",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getCreatedDate()));
				
						TtrnClaimDetails ttrnClaimDetails = ttrnClaimDetailsMapper.toEntity(req);
						
						req.setClaimNo(fm.getSequence("ClaimBooking",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getCreatedDate()));
						ttrnClaimDetails.setClaimNo(new BigDecimal(req.getClaimNo()));
						ttrnClaimDetailsRepository.saveAndFlush(ttrnClaimDetails); //queryImpl.updateQuery(query, arg); CliamDetailsArugumentsmode2 arg
						
						//claim.insert.getUpdationQuery
						
						TtrnClaimUpdation ttrnClaimUpdation = ttrnClaimUpdationMapper.toEntity(req);
						ttrnClaimUpdation.setSlNo(ttrnClaimUpdationMapper.format(GetSl_no(req.getClaimNo(), req.getPolicyContractNo())));

						ttrnClaimUpdationRepository.save(ttrnClaimUpdation);
					
						insertAggregate(req);
						}
						else 
						{
							//claim.update.cliamDetailsUpdate
							TtrnClaimDetails ttrnClaimDetails = ttrnClaimDetailsRepository.findByContractNoAndClaimNoAndLayerNo(req.getPolicyContractNo(),new BigDecimal(req.getClaimNo()),new BigDecimal(req.getLayerNo()));
							ttrnClaimDetails = ttrnClaimDetailsMapper.toEntity(req);
							req.setClaimNo(fm.getSequence("ClaimBooking",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getCreatedDate()));
							ttrnClaimDetails.setClaimNo(new BigDecimal(req.getClaimNo()));
							ttrnClaimDetailsRepository.saveAndFlush(ttrnClaimDetails); 
							
							insertAggregate(req);
						}
					response.setClaimNo(req.getClaimNo());
					response.setClaimNo(req.getClaimNo());
					response.setMessage("Success");
					response.setIsError(false);
					} catch(Exception e){
						log.error(e);
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
				return response;
			}
			public int insertAggregate(final InsertCliamDetailsMode2Req req) {
				int status = 0;
				
				String query="";
				String[] args =null;
				int spresult=0;
				try {
					
					//INSERT_ACCUMULATION_DATA
					TtrnClaimAcc ttrnClaimAcc = ttrnClaimAccMapper.toEntity(req);
				
					//SELECT NVL(MAX(AMEND_ID)+1,0) FROM TTRN_CLAIM_ACC WHERE CLAIM_NO=? AND CONTRACT_NO=?AND LAYER_NO=? AND SUB_CLASS=?)
					TtrnClaimAcc list = ttrnClaimAccRepository.findTop1ByClaimNoAndContractNoAndLayerNoAndSubClass(
							new BigDecimal(req.getClaimNo()),req.getPolicyContractNo(),StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()),new BigDecimal(req.getDepartmentId()));
					if(list!=null) {
						ttrnClaimAcc.setAmendId(list.getAmendId()==null?BigDecimal.ZERO:new BigDecimal(list.getAmendId().intValue()+1));
						}else {
							ttrnClaimAcc.setAmendId(BigDecimal.ZERO);
					}
					ttrnClaimAccRepository.saveAndFlush(ttrnClaimAcc);
				
				} catch (DataAccessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return status;
			}
	protected String GetDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String valu="0";
		if (StringUtils.isNotBlank(limitOrigCur)&& Double.parseDouble(limitOrigCur) != 0) {
			double originalCountry = Double.parseDouble(limitOrigCur)/ Double.parseDouble(ExchangeRate);
			DecimalFormat myFormatter = new DecimalFormat("###0.00");
			final double dround = Math.round(originalCountry * 100.0) / 100.0;
			valu = myFormatter.format(dround);
		}
		return valu;
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
						amount =Double.parseDouble(list.get(0).get("OTH_FEE_DIFF") == null ? ""
								: list.get(0).get("OTH_FEE_DIFF").toString());
					}
				

				}else{
					query="claim.other.fee.os.sum.difference";
					list=queryImpl.selectList(query, new String[]{formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo(),formObj.getPolicyContractNo(),formObj.getClaimNo()});
					if (!CollectionUtils.isEmpty(list)) {
						amount =Double.parseDouble(list.get(0).get("OTH_FEE_DIFF") == null ? ""
								: list.get(0).get("OTH_FEE_DIFF").toString());
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

	public claimNoListRes claimNoList(claimNoListReq req) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		claimNoListRes response = new claimNoListRes();
		
			try {
				String query = "GET_CEDENT_NO_LIST";
				String args[] = new String[4];
				args[0] = req.getCedentClaimNo();
				args[1] = req.getDateofLoss();
				args[2] = req.getCedingCompanyCode();
				args[3] = req.getBranchCode();
				if (StringUtils.isNotBlank(req.getClaimNo())) {
					query += " AND C.CLAIM_NO !=" + req.getClaimNo();
				}
				list = queryImpl.selectList(query, args);
				
				response.setResponse(list);
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
