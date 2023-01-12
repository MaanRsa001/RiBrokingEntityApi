package com.maan.insurance.controller.premium;

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
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.premium.ClaimTableListReq;
import com.maan.insurance.model.req.premium.ContractDetailsReq;
import com.maan.insurance.model.req.premium.GetCassLossCreditReq;
import com.maan.insurance.model.req.premium.GetConstantPeriodDropDownReq;
import com.maan.insurance.model.req.premium.GetPreListReq;
import com.maan.insurance.model.req.premium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.premium.GetSPRetroListReq;
import com.maan.insurance.model.req.premium.GetVatInfoReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.premium.PremiumEditReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq;
import com.maan.insurance.model.req.premium.CashLossmailTriggerReq;
import com.maan.insurance.model.req.premium.InsertReverseCashLossCreditReq;
import com.maan.insurance.model.res.premium.ClaimTableListMode1Res;
import com.maan.insurance.model.res.premium.ContractDetailsRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes;
import com.maan.insurance.model.res.premium.GetAllocatedListRes;
import com.maan.insurance.model.res.premium.GetAllocatedTransListRes;
import com.maan.insurance.model.res.premium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes;
import com.maan.insurance.model.res.premium.GetClaimNosDropDownRes;
import com.maan.insurance.model.res.premium.GetConstantPeriodDropDownRes;
import com.maan.insurance.model.res.premium.GetContractPremiumRes;
import com.maan.insurance.model.res.premium.GetCountCleanCUTRes;
import com.maan.insurance.model.res.premium.GetDepartmentNoRes;
import com.maan.insurance.model.res.premium.GetDepositReleaseCountRes;
import com.maan.insurance.model.res.premium.GetOSBListRes;
import com.maan.insurance.model.res.premium.GetPreListRes;
import com.maan.insurance.model.res.premium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes1;
import com.maan.insurance.model.res.premium.GetPremiumedListRes;
import com.maan.insurance.model.res.premium.GetPreviousPremiumRes;
import com.maan.insurance.model.res.premium.GetRetroContractsRes;
import com.maan.insurance.model.res.premium.GetSPRetroListRes;
import com.maan.insurance.model.res.premium.GetSumOfShareSignRes;
import com.maan.insurance.model.res.premium.GetVatInfoRes;
import com.maan.insurance.model.res.premium.InsertPremiumRes;
import com.maan.insurance.model.res.premium.PremiumEditRes;
import com.maan.insurance.model.res.premium.SubmitPremiumReservedRes;
import com.maan.insurance.model.res.premium.premiumUpdateMethodRes;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIReq;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIRes;
import com.maan.insurance.model.res.premium.ViewRIPremiumListRes;
import com.maan.insurance.model.res.premium.getCurrencyShortNameRes;
import com.maan.insurance.model.res.premium.getReverseCassLossCreditRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.premium.PropPremiumService;
import com.maan.insurance.validation.premium.PropPremiumValidation;

@RestController
@RequestMapping("/Insurance")
public class PropPremiumController {
	Gson gson = new Gson();
	
	@Autowired
	private PropPremiumService premiumService;
	
	@Autowired
	private PropPremiumValidation premiumVali;
	
	@PostMapping("/proppremium/getPremiumedList")
	public GetPremiumedListRes getPremiumedList(@RequestBody GetPremiumedListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getPremiumedListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getPremiumedList(req);
		
	}
	
	@PostMapping("/Proppremium/getPreList")
	public GetPreListRes getPreList(@RequestBody GetPreListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getPreListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getPreList(req);
		
	}
	@PostMapping("/Proppremium/getConstantPeriodDropDown")
	public GetConstantPeriodDropDownRes getConstantPeriodDropDown(@RequestBody GetConstantPeriodDropDownReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getConstantPeriodDropDownVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getConstantPeriodDropDown(req);
		
	}

	@GetMapping("/Proppremium/getPreviousPremium/{contractNo}")
	public GetPreviousPremiumRes getPreviousPremium(@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		
		
			return premiumService.getPreviousPremium(contractNo);
		
	}
	@GetMapping("/Proppremium/GetContractPremium/{contractNo}/{departmentId}/{branchCode}")
	public GetContractPremiumRes GetContractPremium(@PathVariable ("contractNo") String contractNo,@PathVariable ("departmentId") String departmentId,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {

	return premiumService.getContractPremium(contractNo,departmentId,branchCode);
	}
	
	@GetMapping("/Proppremium/getClaimNosDropDown/{contractNo}")
	public GetClaimNosDropDownRes getClaimNosDropDown(@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		
		
			//return premiumJpaService.getClaimNosDropDown(contractNo);
		return premiumService.getClaimNosDropDown(contractNo);
		
	}
	@PostMapping("/Proppremium/contractDetails")
	public ContractDetailsRes contractDetails(@RequestBody ContractDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.contractDetailsVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.contractDetails(req);
		
	}
	@PostMapping("/Proppremium/claimTableList/mode1")
	public ClaimTableListMode1Res claimTableListMode1(@RequestBody ClaimTableListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.claimTableListMode1Vali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		//return premiumJpaService.claimTableListMode1(req);
		return premiumService.claimTableListMode1(req);
		
	}
	@GetMapping("/Proppremium/getCountCleanCUT/{contractNo}")
	public GetCountCleanCUTRes getCountCleanCUT(@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		//return premiumService.getCountCleanCUT(contractNo);
		return premiumService.getCountCleanCUT(contractNo);
		
	}
	@PostMapping("/Proppremium/insertPremium")
	public InsertPremiumRes insertPremium(@RequestBody InsertPremiumReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.insertPremiumVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.insertPremium(req);
		
	}
	@PostMapping("/Proppremium/getSPRetroList")
	public GetSPRetroListRes getSPRetroList(@RequestBody GetSPRetroListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getSPRetroListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getSPRetroList(req);
		
	}
	@GetMapping("/Proppremium/getRetroContracts/{proposalNo}/{noOfRetro}")
	public GetRetroContractsRes getRetroContracts(@PathVariable ("proposalNo") String proposalNo, @PathVariable ("noOfRetro") String noOfRetro) throws CommonValidationException {
		return premiumService.getRetroContracts(proposalNo, noOfRetro);
		
	}
	@GetMapping("/Proppremium/getSumOfShareSign/{retroContractNo}")
	public GetSumOfShareSignRes getSumOfShareSign(@PathVariable ("retroContractNo") String retroContractNo) throws CommonValidationException {
		return premiumService.getSumOfShareSign(retroContractNo);
		
	}
	@GetMapping("/Proppremium/getDepartmentNo/{contractNo}")
	public GetDepartmentNoRes getDepartmentNo(@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		//return premiumService.getDepartmentNo(contractNo);
		return premiumService.getDepartmentNo(contractNo);
		
	}
	@GetMapping("/Proppremium/getOSBList/{transaction}/{contractNo}/{branchCode}")
	public GetOSBListRes getOSBList(@PathVariable ("transaction") String transaction, @PathVariable ("contractNo") String contractNo, @PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return premiumService.getOSBList(transaction, contractNo, branchCode);
		
	}
	@PostMapping("/Proppremium/GetPremiumDetails")
	public GetPremiumDetailsRes getPremiumDetails(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getPremiumDetailsVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getPremiumDetails(req);	
	}
	@PostMapping("/Proppremium/GetPremiumDetailsRi")
	public GetPremiumDetailsRes getPremiumDetailsRi(@RequestBody GetPremiumDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getPremiumDetailsVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getPremiumDetailsRi(req);	
	}
	@PostMapping("/Proppremium/premiumEdit")
	public PremiumEditRes premiumEdit(@RequestBody PremiumEditReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.premiumEditVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.premiumEdit(req);	
	}
	@PostMapping("/Proppremium/premiumEditRi")
	public PremiumEditRes premiumEditRi(@RequestBody PremiumEditReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.premiumEditVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.premiumEditRi(req);	
	}
	@GetMapping("/Proppremium/getBrokerAndCedingName/{ContNo}/{branchCode}")
	public GetBrokerAndCedingNameRes getBrokerAndCedingName(@PathVariable ("ContNo") String ContNo, @PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		//return premiumService.getBrokerAndCedingName(ContNo, branchCode);
		return premiumService.getBrokerAndCedingName(ContNo, branchCode);
		
	}
	@GetMapping("/Proppremium/getAllocatedList/{ContNo}/{transactionNo}")
	public GetAllocatedListRes getAllocatedList(@PathVariable ("ContNo") String ContNo, @PathVariable ("transactionNo") String transactionNo) throws CommonValidationException {
		//return premiumService.getAllocatedList(ContNo, transactionNo);
		return premiumService.getAllocatedList(ContNo, transactionNo);
	}
	@GetMapping("/Proppremium/currencyList/{branchCode}")
	public CurrencyListRes currencyList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		//return premiumService.currencyList(branchCode);
		return premiumService.currencyList(branchCode);
	}
	@PostMapping("/Proppremium/getPremiumReserved")
	public GetPremiumReservedRes1 getPremiumReserved(@RequestBody GetPremiumReservedReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getPremiumReservedVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getPremiumReserved(req);	
	}
	@PostMapping("/Proppremium/getCashLossCredit")
	public GetCashLossCreditRes getCassLossCredit(@RequestBody GetCassLossCreditReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getCassLossCreditVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getCassLossCredit(req);	
	}
	@GetMapping("/Proppremium/getAllocatedTransList/{proposalNo}")
	public GetAllocatedTransListRes getAllocatedTransList(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
		//return premiumService.getAllocatedTransList(proposalNo);
		return premiumService.getAllocatedTransList(proposalNo);
	}
	@GetMapping("/Proppremium/getAllocatedCassLossCredit/{proposalNo}/{branchCode}")
	public GetAllocatedCassLossCreditRes getAllocatedCassLossCredit(@PathVariable ("proposalNo") String proposalNo, @PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		//return premiumService.getAllocatedCassLossCredit(proposalNo, branchCode);
		return premiumService.getAllocatedCassLossCredit(proposalNo, branchCode);
	}
	@PostMapping("/Proppremium/submitPremiumReserved")
	public SubmitPremiumReservedRes submitPremiumReserved(@RequestBody SubmitPremiumReservedReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.validatePremimReserved(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.submitPremiumReserved(req);	
	}
	@GetMapping("/Proppremium/getDepositReleaseCount/{dropDown}/{contractNo}/{branchCode}/{type}")
	public GetDepositReleaseCountRes getDepositReleaseCount(@PathVariable ("dropDown") String dropDown,@PathVariable ("contractNo") String contractNo, @PathVariable ("branchCode") String branchCode,@PathVariable ("type") String type) throws CommonValidationException {
		return premiumService.getDepositReleaseCount(dropDown, contractNo,branchCode,type);
	} 
	@PostMapping("/Proppremium/premiumUpdateMethod")
	public premiumUpdateMethodRes premiumUpdateMethod(@RequestBody InsertPremiumReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.insertPremiumVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.premiumUpdateMethod(req);	
	}
	@PostMapping("/Proppremium/getVatInfo")
	public GetVatInfoRes getVatInfo(@RequestBody GetVatInfoReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getVatInfoVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.getVatInfo(req);	
	}
	@PostMapping("/Proppremium/viewPremiumDetailsRI")
	public ViewPremiumDetailsRIRes viewPremiumDetailsRI(@RequestBody ViewPremiumDetailsRIReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.viewPremiumDetailsRIVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.viewPremiumDetailsRI(req);	
	}
	@PostMapping("/Proppremium/viewRIPremiumList")
	public ViewRIPremiumListRes viewRIPremiumList(@RequestBody GetRIPremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getRIPremiumListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.viewRIPremiumList(req);	
	} 
	@PostMapping("/Proppremium/updateRIStatus")
	public CommonResponse updateRIStatus(@RequestBody GetRIPremiumListReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.getRIPremiumListVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.updateRIStatus(req);	
	}
	
	@PostMapping("/Proppremium/insertCashLossCredit")
	public CommonResponse InsertCashLossCredit(@RequestBody InsertPremiumReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.InsertCashLossCreditVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.InsertCashLossCredit(req);	
	}
	
	@PostMapping("/Proppremium/insertReverseCashLossCredit")
	public CommonResponse InsertReverseCashLossCredit(@RequestBody InsertReverseCashLossCreditReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.InsertReverseCashLossCreditVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.InsertReverseCashLossCredit(req);	
	}
	
	@PostMapping("/Proppremium/cashLossmailTrigger")
	public CommonResponse CashLossmailTrigger(@RequestBody CashLossmailTriggerReq req) throws CommonValidationException {
		List<ErrorCheck> error = premiumVali.CashLossmailTriggerVali(req);
		if(error!= null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return premiumService.CashLossmailTrigger(req);	
	}
	
	@GetMapping("/Proppremium/getReverseCassLossCredit/{proposalNo}/{cashlosstranId}")
	public getReverseCassLossCreditRes getReverseCassLossCredit(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("cashlosstranId") String cashlosstranId) throws CommonValidationException {
			return premiumService.getReverseCassLossCredit(proposalNo,cashlosstranId);
		
	}
	
	@GetMapping("/Proppremium/getCurrencyShortName/{currencyId}/{branchCode}")
	public getCurrencyShortNameRes getCurrencyShortName(@PathVariable ("currencyId") String currencyId,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
			return premiumService.getCurrencyShortName(currencyId,branchCode);
		
	}
}
