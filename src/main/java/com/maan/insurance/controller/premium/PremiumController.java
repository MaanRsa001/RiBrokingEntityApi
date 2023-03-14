package com.maan.insurance.controller.premium;

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
import com.maan.insurance.model.req.premium.ContractidetifierlistReq;
import com.maan.insurance.model.req.premium.PremiumListReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.premium.ContractidetifierlistRes;
import com.maan.insurance.model.res.premium.PremiumListRes;
import com.maan.insurance.service.premium.PremiumService;
import com.maan.insurance.validation.premium.PremiumValidation;

@RestController
@RequestMapping("/Insurance/premium")
public class PremiumController {
	Gson gson = new Gson();
	
	@Autowired
	private PremiumService serv;
	@Autowired
	private PremiumValidation val;
	
	@PostMapping("/PremiumList")
	public PremiumListRes premiumList(@RequestBody PremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.premiumListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.PremiumList(req);
	}
	@PostMapping("/PendingPremiumList")
	public PremiumListRes PendingPremiumList(@RequestBody PremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.premiumListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.PendingPremiumList(req);
	}
	@GetMapping("/getOpenPeriod/{branchCode}")
	public GetOpenPeriodRes getOpenPeriod(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getOpenPeriod(branchCode);
		} 
	@GetMapping("/productIdList/{branchCode}")
	public GetCommonDropDownRes productIdList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
	return serv.productIdList(branchCode);
	} 
	@PostMapping("/contractidetifierlist")
	public ContractidetifierlistRes contractidetifierlist(@RequestBody ContractidetifierlistReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.contractidetifierlistVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.contractidetifierlist(req);
	} 
//	@PostMapping("/copyDatatoDeleteTable")
//	public CommonResponse copyDatatoDeleteTable(@RequestBody CopyDatatoDeleteTableReq req) throws CommonValidationException {
//		List<ErrorCheck> error= val.copyDatatoDeleteTableVali(req);
//		if(error!=null && error.size()>0) {
//			throw new CommonValidationException("error",error);
//		}
//		return serv.copyDatatoDeleteTable(req);
//	}
	@PostMapping("/PremiumRiList")
	public PremiumListRes PremiumRiList(@RequestBody PremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.premiumListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.PremiumRiList(req);
	}
}
