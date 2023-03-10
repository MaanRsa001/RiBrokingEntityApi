package com.maan.insurance.controller.billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;


import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.req.billing.ReverseInsertReq;

import com.maan.insurance.model.res.billing.EditBillingInfoRes;
import com.maan.insurance.model.res.billing.GetBillingInfoListRes;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.billing.ReverseInsertRes;
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
	@GetMapping("/editBillingInfo/{billingNo}/{branchCode}")
	public EditBillingInfoRes editBillingInfo(@PathVariable("billingNo") String billingNo,@PathVariable("branchCode") String branchCode) throws CommonValidationException {
		List<ErrorCheck> error = val.editBillingInfo(billingNo,branchCode);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.editBillingInfo(billingNo,branchCode);
	}
	@PostMapping("/delete")
	public ReverseInsertRes savereverseInsert(@RequestBody ReverseInsertReq req) throws CommonValidationException {
		List<ErrorCheck> error=val.reverseInsertvalidate(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.savereverseInsert(req);
	}
}
