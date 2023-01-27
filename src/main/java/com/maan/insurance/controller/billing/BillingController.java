package com.maan.insurance.controller.billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;

import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;

import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.billing.EditBillingInfoReq;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.res.GetTransContractRes1;
import com.maan.insurance.model.res.billing.EditBillingInfoRes;

import com.maan.insurance.model.res.billing.GetBillingInfoListRes;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.billing.BillingService;
import com.maan.insurance.validation.billing.BillingValidation;

@RestController
@RequestMapping("/Insurance/billing")
public class BillingController {

	@Autowired
	private BillingService serv;
	@Autowired
	private BillingValidation val;
	
	@PostMapping("/insertBillingInfo")
	public CommonResponse insertBillingInfo(@RequestBody InsertBillingInfoReq req) throws CommonValidationException {
		List<ErrorCheck> error = val.insertBillingInfoVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.insertBillingInfo(req);
	}
	
	@PostMapping("/getBillingInfoList")
	public GetBillingInfoListRes getBillingInfoList(@RequestBody GetBillingInfoListReq req) throws CommonValidationException {
		List<ErrorCheck> error = val.getBillingInfoListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getBillingInfoList(req);
	}
	@PostMapping("/getTransContract")
	public GetTransContractResRi getTransContract(@RequestBody GetTransContractReqRi req) throws CommonValidationException {
		List<ErrorCheck> error = val.getTransContractVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTransContract(req);
		} 
	@PostMapping("/getTransContractRi")
	public GetTransContractResRi getTransContractRi(@RequestBody GetTransContractReqRi req) throws CommonValidationException {
		List<ErrorCheck> error = val.getTransContractRiVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTransContractRi(req);
		}
	@PostMapping("/editBillingInfo")
	public EditBillingInfoRes editBillingInfo(@RequestBody EditBillingInfoReq req) throws CommonValidationException {
		List<ErrorCheck> error = val.editBillingInfo(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.editBillingInfo(req);
	}
}
