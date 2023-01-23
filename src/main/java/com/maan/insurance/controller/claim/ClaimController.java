package com.maan.insurance.controller.claim;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
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
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.req.claim.claimNoListReq;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.claim.AllocListRes1;
import com.maan.insurance.model.res.claim.AllocationListRes;
import com.maan.insurance.model.res.claim.ClaimListMode3Response;
import com.maan.insurance.model.res.claim.ClaimListMode4Response;
import com.maan.insurance.model.res.claim.ClaimListMode5Response;
import com.maan.insurance.model.res.claim.ClaimListMode6Response;
import com.maan.insurance.model.res.claim.ClaimListRes;
import com.maan.insurance.model.res.claim.ClaimPaymentEditRes1;
import com.maan.insurance.model.res.claim.ClaimPaymentListRes1;
import com.maan.insurance.model.res.claim.ClaimTableListMode1Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode2Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode7Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode10Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode1Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode4Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode5Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode6Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode7Res;
import com.maan.insurance.model.res.claim.ContractidetifierlistRes1;
import com.maan.insurance.model.res.claim.GetContractNoRes1;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode12Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode2Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode3Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode8Res;
import com.maan.insurance.model.res.claim.ProductIdListRes1;
import com.maan.insurance.model.res.claim.ProposalNoRes;
import com.maan.insurance.model.res.claim.claimNoListRes;
import com.maan.insurance.service.claim.ClaimService;
import com.maan.insurance.validation.Claim.ClaimValidation;

@RestController
@RequestMapping("/Insurance")

public class ClaimController {
	Gson gson = new Gson();
	
	@Autowired
	private ClaimService claimService;
	@Autowired
	private ClaimValidation claimValidation;
	
	@PostMapping("/getProposal")
	public ProposalNoRes getProposalNo(@RequestBody ProposalNoReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.PaymentRecieptvalidate(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.savepaymentReciept(req);
		
	}
	@PostMapping("/claim/getcontractDetailsMode1")
	public ContractDetailsMode1Res contractDetailsMode1(@RequestBody ContractDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode1ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode1(req);
	}
	@PostMapping("/claim/getcontractDetailsMode4")
	public ContractDetailsMode4Res contractDetailsMode4(@RequestBody ContractDetailsModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode4ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode4(req);
	}
	@PostMapping("/claim/getcontractDetailsMode5")
	public ContractDetailsMode5Res contractDetailsMode5(@RequestBody ContractDetailsModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode5ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode5(req);
	}
	@PostMapping("/claim/getcontractDetailsMode6")
	public ContractDetailsMode6Res contractDetailsMode6(@RequestBody ContractDetailsModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode6ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode6(req);
	}
	@PostMapping("/claim/getcontractDetailsMode7")
	public ContractDetailsMode7Res contractDetailsMode7(@RequestBody ContractDetailsModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode7ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode7(req);
	}
	@PostMapping("/claim/getcontractDetailsMode10")
	public ContractDetailsMode10Res contractDetailsMode10(@RequestBody ContractDetailsModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateMode10ContractDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveContractDetailsMode10(req);
	}
	@PostMapping("/claim/allocationList")
	public AllocationListRes allocationList(@RequestBody AllocationListReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateAllocationList(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveAllocationList(req);
	}
	@PostMapping("/claimlist")
	public ClaimListRes claimlist(@RequestBody ClaimListReq req) throws CommonValidationException {
		List<ErrorCheck> error=claimValidation.validateclaimlist(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.saveclaimlist(req);
	}
	
	
	@PostMapping("/claimlist/modeone")
	public ClaimTableListMode1Res claimTableListMode1(@RequestBody ClaimTableListReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimTableListVali(req);
		if(error != null && error.size() > 0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.claimTableListMode1(req);
	}

	@PostMapping("/claimlist/modetwo")
	public ClaimTableListMode2Res claimTableListMode2(@RequestBody ClaimTableListMode2Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimTableListMode2Vali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimTableListMode2(req);
	}
	@PostMapping("/claimlist/modeseven")
	public ClaimTableListMode7Res claimListMode7(@RequestBody ClaimTableListMode2Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimTableListMode2Vali(req);
		if(error != null && error.size() > 0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.claimTableListMode7(req);
	}
	
	@PostMapping("/claimlist/mode/three")
	public ClaimListMode3Response claimListMode3(@RequestBody ClaimTableListMode2Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimTableListMode2Vali(req);
		if(error != null && error.size() > 0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.claimListMode3(req);
	}
	
	@PostMapping("/claimlist/mode/four")
	public ClaimListMode4Response claimListMode4(@RequestBody ClaimListMode4Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimListMode4Vali(req);
		if(error != null && error.size() > 0) {
			throw new CommonValidationException("error",error);
		}
		return claimService.claimListMode4(req);
	}
	
	@GetMapping("/claimlist/mode/five/{claimNo}/{contractNo}")
	public ClaimListMode5Response claimListMode5(@PathVariable ("claimNo") String claimNo,@ PathVariable ("contractNo") String contractNo ) throws CommonValidationException {
		
			return claimService.claimListMode5( claimNo, contractNo);
		
	}
	
	@GetMapping("/claimlist/mode/six/{contractNo}/{claimNo}")
	public ClaimListMode6Response claimListMode6(@ PathVariable ("contractNo") String contractNo, @PathVariable ("claimNo") String claimNo) throws CommonValidationException {
		
		
			return claimService.claimListMode6( contractNo, claimNo);
		
	}
	
	@PostMapping("/getContractno")
	public GetContractNoRes1 getContractNo(@RequestBody GetContractNoReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.getContractNoVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error", error);
		}
		return claimService.getContractNo(req);
	}
	
	@PostMapping("/claimPaymentEdit")
	public ClaimPaymentEditRes1 claimPaymentEdit(@RequestBody ClaimPaymentEditReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimPaymentEditVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimPaymentEdit(req);
	}
	@PostMapping("/claimPaymentEditRi")
	public ClaimPaymentEditRes1 claimPaymentEditRi(@RequestBody ClaimPaymentEditReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimPaymentEditVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimPaymentEditRi(req);
	}
	
	@PostMapping("/alloclist")
	public AllocListRes1 allocList(@RequestBody AllocListReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.allocListVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.allocList(req);
	}
	
	@GetMapping("/productIdList/{branchCode}")
	public ProductIdListRes1 productIdList(@ PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		
		
			return claimService.productIdList(branchCode);
		
	}
	@PostMapping("/contractidetifierlist")
	public ContractidetifierlistRes1 contractidetifierlist(@RequestBody ContractidetifierlistReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.contractidetifierlistVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.contractidetifierlist(req);
	}
	
	@PostMapping("/claimPaymentList")
	public ClaimPaymentListRes1 claimPaymentList(@RequestBody ClaimPaymentListReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimPaymentListVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimPaymentList(req);
	}
	
	@PostMapping("/getReInsValue")
	public String getReInsValue(@RequestBody GetReInsValueReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.getReInsValueVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error", error);
		}
		return claimService.getReInsValue(req);
	}
	
	@GetMapping("/get/shortname/{branchcode}")
	public GetShortnameRes getShortname(@PathVariable ("branchcode") String branchcode ) throws CommonValidationException {
		
	
			return claimService.getShortname(branchcode);
		
	}
	//InitClaim
	
	@PostMapping("/insertCliamDetailsMode2")
	public InsertCliamDetailsMode2Res insertCliamDetailsMode2(@RequestBody InsertCliamDetailsMode2Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimsInsertValidation(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.insertCliamDetailsMode2(req);
	}
	
	@PostMapping("/insertCliamDetailsMode8")
	public InsertCliamDetailsMode8Res insertCliamDetailsMode8(@RequestBody InsertCliamDetailsMode8Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.updationValidation8(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.insertCliamDetailsMode8(req);
	}
	@PostMapping("/insertCliamDetailsMode3")
	public InsertCliamDetailsMode3Res insertCliamDetailsMode3(@RequestBody InsertCliamDetailsMode3Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.insertCliamDetailsMode3Vali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.insertCliamDetailsMode3(req);
	}

	@PostMapping("/insertCliamDetailsMode12")
	public InsertCliamDetailsMode12Res insertCliamDetailsMode12(@RequestBody InsertCliamDetailsMode12Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.insertCliamDetailsMode12Vali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.insertCliamDetailsMode12(req);
	}
	@PostMapping("/claimNoList")
	public claimNoListRes claimNoList(@RequestBody claimNoListReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimNoListVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimNoList(req);
	}
	@PostMapping("/claimUpdatePaymentRi")
	public InsertCliamDetailsMode3Res claimUpdatePaymentRi(@RequestBody InsertCliamDetailsMode3Req req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.insertCliamDetailsMode3Vali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimUpdatePaymentRi(req);
	}
	@PostMapping("/claimPaymentRiList")
	public ClaimPaymentListRes1 claimPaymentRiList(@RequestBody ClaimPaymentListReq req) throws CommonValidationException {
		List<ErrorCheck> error = claimValidation.claimPaymentListVali(req);
		if(error != null && error.size() > 0) {
			
			throw new CommonValidationException("error",error);
		}
		return claimService.claimPaymentRiList(req);
	}
}
