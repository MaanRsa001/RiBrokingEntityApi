package com.maan.insurance.controller.retro;

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
import com.maan.insurance.controller.XolPremium.Xolpremiumcontroller;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.retro.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.retro.InsertProfitCommissionMainReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.retro.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.retro.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.retro.UpdateRiskProposalReq;
import com.maan.insurance.model.req.retro.ShowSecondPageData1Req;
import com.maan.insurance.model.req.retro.ShowSecondPageDataReq;
import com.maan.insurance.model.req.retro.FirstInsertReq;
import com.maan.insurance.model.req.retro.GetEndDateReq;
import com.maan.insurance.model.req.retro.InsertBonusDetailsReq;
import com.maan.insurance.model.req.retro.InsertRemarkDetailsReq;
import com.maan.insurance.model.req.retro.InsertRetroDetailsReq;
import com.maan.insurance.model.req.retro.SaveSecondPageReq;
import com.maan.insurance.model.req.retro.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.retro.ViewRiskDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.retro.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.nonproportionality.UpdateProportionalTreatyRes;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res;
import com.maan.insurance.model.res.retro.ShowSecondPageDataRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.retro.SaveSecondPageRes;
import com.maan.insurance.model.res.retro.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.retro.ViewRiskDetailsRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes;
import com.maan.insurance.service.XolPremium.XolPremiumService;
import com.maan.insurance.service.retro.RetroService;
import com.maan.insurance.validation.XolPremium.XolPremiumValidation;
import com.maan.insurance.validation.retro.RetroValidation;

@RestController
@RequestMapping("/Insurance/Retro")
public class RetroController {
	Gson gson = new Gson();
	private Logger log = LogManager.getLogger(Xolpremiumcontroller.class);
	
	@Autowired
	private RetroService retroServ;
	@Autowired
	private RetroValidation retroVali;
	
	@PostMapping("/insertProportionalTreaty")
	public FirstInsertRes firstInsert(@RequestBody FirstInsertReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.firstInsertVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.firstInsert(req);
	}
	@GetMapping("/getShortname/{branchCode}")
	public CommonSaveRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return retroServ.getShortname(branchCode);
		} 
	@PostMapping("/showSecondpageEditItems")
	public ShowSecondpageEditItemsRes showSecondpageEditItems(@RequestBody ShowSecondpageEditItemsReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.showSecondpageEditItemsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.showSecondpageEditItems(req);
	}
	@PostMapping("/InsertRemarkDetails")
	public CommonResponse insertRemarkDetails(@RequestBody InsertRemarkDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.validationRemarks(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.insertRemarkDetails(req);
	}
	@GetMapping("/checkAvialability/{proposalNo}/{productId}")
	public CommonSaveRes checkAvialability(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("productId") String productId) throws CommonValidationException {
		return retroServ.checkAvialability(proposalNo, productId);
		} 
	@PostMapping("/viewRiskDetails")
	public ViewRiskDetailsRes viewRiskDetails(@RequestBody ViewRiskDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.viewRiskDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.viewRiskDetails(req);
	}
	@GetMapping("/getRemarksDetails/{proposalNo}")
	public GetRemarksDetailsRes getRemarksDetails(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
		return retroServ.getRemarksDetails( proposalNo);
		} 

	@PostMapping("/showRetroCess")
	public ShowRetroCess1Res showRetroCess(@RequestBody ShowRetroCess1Req req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.showRetroCessVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.showRetroCess(req);
	}
	@PostMapping("/showSecondPageData1")
	public ShowSecondPageData1Res showSecondPageData1(@RequestBody ShowSecondPageData1Req req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.showSecondPageData1Vali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.showSecondPageData1(req);
	}
	@PostMapping("/showSecondPageData")
	public ShowSecondPageDataRes showSecondPageData(@RequestBody ShowSecondPageDataReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.showSecondPageData1Vali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.showSecondPageData(req);
	} 
	@GetMapping("/getEditMode/{proposalNo}")
	public CommonSaveRes getEditMode(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
		return retroServ.getEditMode( proposalNo);
		}  
	@PostMapping("/saveSecondPage")
		public SaveSecondPageRes saveSecondPage(@RequestBody SaveSecondPageReq req) throws CommonValidationException {
			List<ErrorCheck> error=retroVali.validateSecondPage(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return retroServ.saveSecondPage(req);
		} 
	@PostMapping("/insertRetroCess")
	public CommonResponse insertRetroCess(@RequestBody InsertRetroCessReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.insertRetroCessVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.insertRetroCess(req);
	}
	@PostMapping("/insertCrestaMaintable")
	public CommonResponse insertCrestaMaintable(@RequestBody InsertCrestaMaintableReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.insertCrestaMaintableVali(req);
			if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
			}
				return retroServ.insertCrestaMaintable(req);
	} 
	@PostMapping("/insertBonusDetails")
	public CommonResponse insertBonusDetails(@RequestBody InsertBonusDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.insertBonusDetailsVali(req);
			if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
			}
				return retroServ.insertBonusDetails(req);
	} 
	@PostMapping("/insertProfitCommissionMain")
	public CommonResponse insertProfitCommissionMain(@RequestBody InsertProfitCommissionMainReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.insertProfitCommissionMainVali(req);
			if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
			}
				return retroServ.insertProfitCommissionMain(req);
	}
	@PostMapping("/instalMentPremium")
	public CommonResponse instalMentPremium(@RequestBody InstalMentPremiumReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.instalMentPremiumVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.instalMentPremium(req);
	}
	@PostMapping("/saveRiskDeatilsSecondForm")
	public SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(@RequestBody SaveSecondPageReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.validateSecondPage(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.saveRiskDeatilsSecondForm(req);
	}
	@GetMapping("/showLayerBrokerage/{layerProposalNo}")
	public ShowLayerBrokerageRes showLayerBrokerage(@PathVariable ("layerProposalNo") String layerProposalNo) throws CommonValidationException {
					return retroServ.showLayerBrokerage(layerProposalNo);
		}
	@PostMapping("/updateProportionalTreaty")
	public CommonSaveRes updateProportionalTreaty(@RequestBody UpdateProportionalTreatyReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.updateProportionalTreatyVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.updateProportionalTreaty(req);
	} 
	@PostMapping("/updateRiskProposal")
	public CommonSaveRes updateRiskProposal(@RequestBody FirstInsertReq req) throws CommonValidationException {
		List<ErrorCheck> error=retroVali.firstInsertVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.updateRiskProposal(req);
	} 
	@GetMapping("/previouRetroTypeChect/{proposalNo}/{branchCode}")
	public CommonSaveRes previouRetroTypeChect(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
					return retroServ.previouRetroTypeChect(proposalNo,branchCode);
		}
	@PostMapping("/riskDetailsEditMode")
	public RiskDetailsEditModeRes riskDetailsEditMode(@RequestBody RiskDetailsEditModeReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.riskDetailsEditModeVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.riskDetailsEditMode(req);
	} 
	@PostMapping("/getEndDate")
	public CommonSaveRes getEndDate(@RequestBody GetEndDateReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.getEndDateVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.getEndDate(req);
	}
	@PostMapping("/insertRetroDetails")
	public CommonResponse insertRetroDetails(@RequestBody InsertRetroDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= retroVali.insertRetroDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return retroServ.insertRetroDetails(req);
	}
}
