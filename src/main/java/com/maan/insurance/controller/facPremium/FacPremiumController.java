package com.maan.insurance.controller.facPremium;

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

import com.google.gson.Gson;
import com.maan.insurance.controller.facultative.FacultativeController;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.facPremium.AddFieldValueReq;
import com.maan.insurance.model.req.facPremium.BonusdetailsReq;
import com.maan.insurance.model.req.facPremium.GetBonusValueReq;
import com.maan.insurance.model.req.facPremium.GetFieldValuesReq;
import com.maan.insurance.model.req.facPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumContractDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumEditReq;
import com.maan.insurance.model.req.facPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.req.facPremium.PremiumedListreq;
import com.maan.insurance.model.res.facPremium.BonusdetailsRes;
import com.maan.insurance.model.res.facPremium.CommonResponse;
import com.maan.insurance.model.res.facPremium.ContractDetailsRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetFieldValuesRes;
import com.maan.insurance.model.res.facPremium.GetMandDInstallmentsRes;
import com.maan.insurance.model.res.facPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.facPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.facPremium.PreListRes1;
import com.maan.insurance.model.res.facPremium.PremiumContractDetailsRes;
import com.maan.insurance.model.res.facPremium.PremiumEditRes;
import com.maan.insurance.model.res.facPremium.PremiumTempRes1;
import com.maan.insurance.model.res.facPremium.PremiumedListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.service.facPremium.FacPremiumService;
import com.maan.insurance.validation.facPremium.FacPremiumValidation;
@RestController
@RequestMapping("/Insurance/FacPremium")
public class FacPremiumController {
	Gson gson = new Gson();
	private Logger log = LogManager.getLogger(FacultativeController.class);
	
	@Autowired
	private FacPremiumService facPremiumService;
	@Autowired
	private FacPremiumValidation facPreValidation;
	

	@GetMapping("/getPremiumTempList/{contNo}/{branchCode}")
	public PremiumTempRes1 getpremiumTempList(@PathVariable ("contNo") String contNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return facPremiumService.getpremiumTempList(contNo,branchCode);
		}
	@PostMapping("/getPremiumedList")
	public PremiumedListRes getPremiumedList(@RequestBody PremiumedListreq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.premiumedListvalidate(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.getPremiumedList(req);
	}
	@GetMapping("/GetPreList/{contNo}/{deptId}")
	public PreListRes1 getPreList(@PathVariable  ("contNo") String contNo, @PathVariable ("deptId") String deptId) throws CommonValidationException {
		return facPremiumService.getPreList(contNo,deptId);
	}
	@GetMapping("/GetPreviousPremium/{contNo}")
	public GetCommonValueRes getPreviousPremium(@PathVariable  ("contNo") String contNo) throws CommonValidationException {
		return facPremiumService.getPreviousPremium(contNo);
	}
	@GetMapping("/GetContractPremium/{contNo}/{branchCode}")
	public GetCommonValueRes getContractPremium(@PathVariable ("contNo") String contNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return facPremiumService.getContractPremium(contNo,branchCode);
	} 
	@GetMapping("/contractDetails/{contNo}/{branchCode}/{productId}")
	public ContractDetailsRes contractDetails(@PathVariable ("contNo") String contNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
		return facPremiumService.contractDetails(contNo,branchCode,productId);
	} 	
	@GetMapping("/mdInstallmentDates/{contNo}/{layerNo}")
		public MdInstallmentDatesRes mdInstallmentDates(@PathVariable ("contNo") String contNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return facPremiumService.mdInstallmentDates(contNo,layerNo);
	}
	@GetMapping("/getDepartmentId/{contNo}/{productId}")
		public GetDepartmentIdRes getDepartmentId(@PathVariable  ("contNo") String contNo, @PathVariable ("productId") String productId) throws CommonValidationException {
		return facPremiumService.getDepartmentId(contNo,productId);
	}
	@PostMapping("/PremiumEdit")
	public PremiumEditRes premiumEdit(@RequestBody PremiumEditReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.premiumEditVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.premiumEdit(req);
	}	 
	@PostMapping("/premiumInsertMethod")
	public CommonResponse premiumInsertMethod(@RequestBody PremiumInsertMethodReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.validateFaculPremium(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.premiumInsertMethod(req);
	} 
	@PostMapping("/premiumUpdateMethod")
	public CommonResponse premiumUpdateMethod(@RequestBody PremiumInsertMethodReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.validateFaculPremium(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.premiumUpdateMethod(req);
	} 
	@PostMapping("/getPremiumDetails")
	public GetPremiumDetailsRes getPremiumDetails(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.getPremiumDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.getPremiumDetails(req);
	}
	@GetMapping("/getInstalmentAmount/{contNo}/{instalmentno}")
	public GetCommonValueRes getInstalmentAmount(@PathVariable ("contNo") String contNo,@PathVariable ("instalmentno") String instalmentno) throws CommonValidationException {
	return facPremiumService.getInstalmentAmount(contNo,instalmentno);
	}
	@GetMapping("/getAllocatedList/{contNo}/{transactionNo}")
	public GetAllocatedListRes getAllocatedList(@PathVariable ("contNo") String contNo,@PathVariable ("transactionNo") String transactionNo) throws CommonValidationException {
	return facPremiumService.getAllocatedList(contNo,transactionNo);
	}
	@GetMapping("/currencyList/{branchCode}")
	public CurrencyListRes currencyList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return facPremiumService.currencyList(branchCode);	
	} 
	@GetMapping("/PremiumContractDetails/{contNo}/{branchCode}/{layerNo}")
	public PremiumContractDetailsRes premiumContractDetails(@PathVariable ("contNo") String contNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return facPremiumService.premiumContractDetails(contNo,branchCode,layerNo);
	} 
//	@PostMapping("/getBonusValue")
//	public GetCommonValueRes getBonusValue(@RequestBody GetFieldValuesReq req) throws CommonValidationException {
//		List<ErrorCheck> error=facPreValidation.getBonusValueVali(req);
//		if(error!=null && error.size()>0) {
//			throw new CommonValidationException("error",error);
//		}
//		return facPremiumService.getBonusValue(req, 0);
//	} 
	@PostMapping("/GetFieldValues")
	public GetFieldValuesRes getFieldValues(@RequestBody GetFieldValuesReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.GetFieldValuesVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.getFieldValues(req);
	} 
	@PostMapping("/bonusdetails")
	public BonusdetailsRes bonusdetails(@RequestBody BonusdetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.bonusdetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.bonusdetails(req);
	} 
	@PostMapping("/addFieldValue")
	public CommonResponse addFieldValue(@RequestBody AddFieldValueReq req) throws CommonValidationException {
		List<ErrorCheck> error=facPreValidation.addFieldValueVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facPremiumService.addFieldValue(req);
	} 
	@GetMapping("/getMandDInstallments/{contNo}/{layerNo}")
	public GetMandDInstallmentsRes getMandDInstallments(@PathVariable ("contNo") String contNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
	return facPremiumService.getMandDInstallments(contNo,layerNo);
	} 
	
}
