package com.maan.insurance.controller.XolPremium;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.maan.insurance.model.res.xolPremium.CommonResponse;
import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetInstallmentAmountReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.MdInstallmentDatesReq;
import com.maan.insurance.model.req.xolPremium.PremiumEditReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.xolPremium.GetPreListRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes;
import com.maan.insurance.model.res.xolPremium.premiumInsertMethodRes;
import com.maan.insurance.service.XolPremium.XolPremiumService;
import com.maan.insurance.validation.XolPremium.XolPremiumValidation;

@RestController
@RequestMapping("/Insurance/XolPremium")
public class Xolpremiumcontroller {
	Gson gson = new Gson();
	
	@Autowired
	private XolPremiumService xps;
	@Autowired
	private XolPremiumValidation XolPremVali;

	@PostMapping("/getPremiumedList")
	public GetPremiumedListRes getPremiumedList(@RequestBody GetPremiumedListReq req) throws CommonValidationException {
		List<ErrorCheck> error= XolPremVali.getPremiumedListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return xps.getPremiumedList(req);
	}
	@GetMapping("/GetContractPremium/{contractNo}/{layerNo}")
	public CommonSaveRes getContractPremium(@PathVariable ("contractNo") String contractNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return xps.getContractPremium(contractNo,layerNo);
		} 
	@GetMapping("/GetPreviousPremium/{contractNo}")
		public CommonSaveRes getPreviousPremium(@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		return xps.getPreviousPremium(contractNo);
		}
	@PostMapping("/mdInstallmentDates")
	public MdInstallmentDatesRes mdInstallmentDates(@RequestBody MdInstallmentDatesReq req) throws CommonValidationException {
		List<ErrorCheck> error= XolPremVali.mdInstallmentDatesVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return xps.mdInstallmentDates(req);
	}
	@PostMapping("/contractDetails")
	public ContractDetailsRes contractDetails(@RequestBody ContractDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= XolPremVali.contractDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return xps.contractDetails(req);
	}
	@GetMapping("/getPreList/{contNo}/{layerNo}")
	public GetPreListRes getPreList(@PathVariable ("contNo") String contNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return xps.getPreList(contNo,layerNo);
	}
	@GetMapping("/getDepartmentId/{contNo}/{productId}")
	public GetDepartmentIdRes getDepartmentId(@PathVariable  ("contNo") String contNo, @PathVariable ("productId") String productId) throws CommonValidationException {
	return xps.getDepartmentId(contNo,productId);
	}  
	@PostMapping("/PremiumEdit")
	public PremiumEditRes premiumEdit(@RequestBody PremiumEditReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.premiumEditVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.premiumEdit(req);
	}
	@PostMapping("/PremiumEditRi")
	public PremiumEditRes premiumEditRi(@RequestBody PremiumEditReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.premiumEditVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.premiumEditRi(req);
	}
	@PostMapping("/premiumInsertMethod")
	public premiumInsertMethodRes premiumInsertMethod(@RequestBody PremiumInsertMethodReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.validateXolPremium(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.premiumInsertMethod(req); 
	}
	@PostMapping("/premiumUpdateMethod")
	public premiumInsertMethodRes premiumUpdateMethod(@RequestBody PremiumInsertMethodReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.validateXolPremium(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.premiumUpdateMethod(req); 
	} 
	@PostMapping("/premiumUpdateMethodRi")
	public CommonResponse premiumUpdateMethodRi(@RequestBody PremiumInsertMethodReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.validateXolPremium(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.premiumUpdateMethodRi(req); 
	} 
	@PostMapping("/getPremiumDetails")
	public GetPremiumDetailsRes getPremiumDetails(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.getPremiumDetailsVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.getPremiumDetails(req); 
	} 
	@PostMapping("/getPremiumDetailsRi")
	public GetPremiumDetailsRes getPremiumDetailsRi(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.getPremiumDetailsVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.getPremiumDetailsRi(req); 
	} 
	@PostMapping("/getInstalmentAmount")
	public CommonSaveRes getInstalmentAmount(@RequestBody GetInstallmentAmountReq req) throws CommonValidationException {
	return xps.getInstalmentAmount(req.getContNo(),req.getLayerno(),req.getInstalmentno());
	}
	@GetMapping("/getBrokerAndCedingName/{contNo}/{branchCode}")
	public GetBrokerAndCedingNameRes getBrokerAndCedingName(@PathVariable ("contNo") String contNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return xps.getBrokerAndCedingName(contNo,branchCode);
	}
	@GetMapping("/currencyList/{branchCode}")
	public CurrencyListRes currencyList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return xps.currencyList(branchCode);	
	} 
	@GetMapping("/getAllocatedList/{contNo}/{transactionNo}")
	public GetAllocatedListRes getAllocatedList(@PathVariable ("contNo") String contNo,@PathVariable ("transactionNo") String transactionNo) throws CommonValidationException {
	return xps.getAllocatedList(contNo,transactionNo);
	} 
	@PostMapping("/GetAdjPremium")
	public CommonSaveRes getAdjPremium(@RequestBody GetAdjPremiumReq req) throws CommonValidationException {
	List<ErrorCheck> error= XolPremVali.getAdjPremiumVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
	 	return xps.getAdjPremium(req); 
	} 
	@PostMapping("/getripremiumlist")
	public GetRIPremiumListRes getRIPremiumList(@RequestBody GetRIPremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error = XolPremVali.getRIPremiumListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return xps.getRIPremiumList(req);	
	}
}
