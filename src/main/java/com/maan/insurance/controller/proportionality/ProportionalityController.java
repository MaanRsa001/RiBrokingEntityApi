package com.maan.insurance.controller.proportionality;

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
import com.maan.insurance.model.req.proportionality.BaseLayerStatusReq;
import com.maan.insurance.model.req.proportionality.BonusSaveReq;
import com.maan.insurance.model.req.proportionality.CedentSaveReq;
import com.maan.insurance.model.req.proportionality.ConvertPolicyReq;
import com.maan.insurance.model.req.proportionality.CrestaSaveReq;
import com.maan.insurance.model.req.proportionality.FirstpageSaveReq;
import com.maan.insurance.model.req.proportionality.GetCrestaDetailListReq;
import com.maan.insurance.model.req.proportionality.GetRetentionDetailsReq;
import com.maan.insurance.model.req.proportionality.GetSectionDuplicationCheckReq;
import com.maan.insurance.model.req.proportionality.GetcalculateSCReq;
import com.maan.insurance.model.req.proportionality.GetprofitCommissionEnableReq;
import com.maan.insurance.model.req.proportionality.InsertCrestaDetailsReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionListReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionSaveReq;
import com.maan.insurance.model.req.proportionality.RemarksSaveReq;
import com.maan.insurance.model.req.proportionality.RetroSaveReq;
import com.maan.insurance.model.req.proportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.proportionality.ScaleCommissionInsertReq;
import com.maan.insurance.model.req.proportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.proportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.proportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.proportionality.getScaleCommissionListReq;
import com.maan.insurance.model.req.proportionality.saveRiskDeatilsSecondFormReq;
import com.maan.insurance.model.req.proportionality.showSecondPageData1Req;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes;
import com.maan.insurance.model.res.proportionality.CancelProposalRes;
import com.maan.insurance.model.res.proportionality.CheckProductMatchRes;
import com.maan.insurance.model.res.proportionality.CommonSaveRes;
import com.maan.insurance.model.res.proportionality.ConvertPolicyRes;
import com.maan.insurance.model.res.proportionality.FirstpagesaveRes;
import com.maan.insurance.model.res.proportionality.FirstpageupdateRes;
import com.maan.insurance.model.res.proportionality.GetCommonValueRes;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes;
import com.maan.insurance.model.res.proportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetentionDetailsRes;
import com.maan.insurance.model.res.proportionality.GetScaleCommissionListRes;
import com.maan.insurance.model.res.proportionality.GetSectionEditModeRes;
import com.maan.insurance.model.res.proportionality.GetSlidingScaleMethodInfoRes;
import com.maan.insurance.model.res.proportionality.GetcalculateSCRes;
import com.maan.insurance.model.res.proportionality.GetprofitCommissionEnableRes;
import com.maan.insurance.model.res.proportionality.InsertCrestaDetailsRes;
import com.maan.insurance.model.res.proportionality.ProfitCommissionListRes;
import com.maan.insurance.model.res.proportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.proportionality.ScaleCommissionInsertRes;
import com.maan.insurance.model.res.proportionality.SecondpagesaveRes;
import com.maan.insurance.model.res.proportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.proportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.proportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.proportionality.UpdateOfferNoReq;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.proportionality.checkAvialabilityRes;
import com.maan.insurance.model.res.proportionality.getSlidingScaleMethodInfo;
import com.maan.insurance.model.res.proportionality.getprofitCommissionDeleteRes;
import com.maan.insurance.model.res.proportionality.getprofitCommissionEditRes;
import com.maan.insurance.model.res.proportionality.saveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.proportionality.showSecondPageData1Res;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.proportionality.ProportionalityService;
import com.maan.insurance.validation.proportionality.ProportionalityValidation;

@RestController
@RequestMapping("/Insurance/proportionality")

public class ProportionalityController {
	Gson gson = new Gson();
	
	@Autowired
	private ProportionalityService propService;
	@Autowired
	private ProportionalityValidation propValidation;
	
	@PostMapping("/insertProportionalTreaty")
	public FirstpagesaveRes firstpageSave(@RequestBody FirstpageSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validatefirstpageSave(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertProportionalTreaty(req,req.getFlagStatus(), req.getAmendStatus());
	}
	@PostMapping("/updateProportionalTreaty")
	public FirstpageupdateRes firstpageupdate(@RequestBody FirstpageSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validatefirstpageSave(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.updateProportionalTreaty(req);
	}
	@GetMapping("/GetRemarksDetails/{proposalNo}/{layerNo}")
	public GetRemarksDetailsRes GetRemarksDetails(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return propService.GetRemarksDetails( proposalNo,layerNo);
		}
	@PostMapping("/saveSecondPage")
	public SecondpagesaveRes saveSecondPage(@RequestBody FirstpageSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validatefirstpageSave(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.saveSecondPage(req);
	}
	@PostMapping("/insertRetroContracts")
	public CommonSaveRes insertRetroContracts(@RequestBody RetroSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertRetroContracts(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertRetroContracts(req);
	}
	@PostMapping("/insertCrestaMaintable")
	public CommonSaveRes insertCrestaMaintable(@RequestBody CrestaSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertCrestaMaintable(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertCrestaMaintable(req);
	}
	@PostMapping("/insertBonusDetails/scale")
	public CommonSaveRes insertBonusScaleDetails(@RequestBody BonusSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertBonusDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertBonusDetails(req,"scale");
	}
	@PostMapping("/insertBonusDetails/lossparticipate")
	public CommonSaveRes insertBonusLossDetails(@RequestBody BonusSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertBonusDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertBonusDetails(req,"lossparticipate");
	}
	@PostMapping("/insertProfitCommission")
	public CommonSaveRes insertProfitCommission(@RequestBody ProfitCommissionSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertProfitCommission(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertProfitCommission(req);
	}
	@PostMapping("/insertRemarkDetails")
	public CommonSaveRes insertRemarkDetails(@RequestBody RemarksSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertRemarkDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertRemarkDetails(req);
	}
	@PostMapping("/insertCedentRetention")
	public CommonSaveRes insertCedentRetention(@RequestBody CedentSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.validateinsertCedentRetention(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.insertCedentRetention(req);
	}
	@GetMapping("/getShortname/{branchCode}")
	public GetCommonValueRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
					return propService.getShortname(branchCode);
		}
	@GetMapping("/getEditMode/{proposalNo}")
	public GetCommonValueRes getEditMode(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
					return propService.getEditMode(proposalNo);
		}
	@PostMapping("/riskDetailsEditMode")
	public RiskDetailsEditModeRes riskDetailsEditMode(@RequestBody RiskDetailsEditModeReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.riskDetailsEditModeVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.riskDetailsEditMode(req);
	}
	@GetMapping("/getGetRetDetails/{proposalNo}/{branchCode}/{productId}")
	public GetRetDetailsRes getGetRetDetails(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
					return propService.getGetRetDetails(proposalNo,branchCode,productId);
		}
	@PostMapping("/BaseLayerStatus")
	public BaseLayerStatusRes BaseLayerStatus(@RequestBody BaseLayerStatusReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.baseLayerStatusVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.BaseLayerStatus(req);
	}
	@PostMapping("/showSecondpageEditItems")
	public ShowSecondpageEditItemsRes showSecondpageEditItems(@RequestBody ShowSecondpageEditItemsReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.showSecondpageEditItemsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.showSecondpageEditItems(req);
		}
	@PostMapping("/getprofitCommissionEnable")
	public GetprofitCommissionEnableRes getprofitCommissionEnable(@RequestBody GetprofitCommissionEnableReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.getprofitCommissionEnableVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.getprofitCommissionEnable(req);
	}
	@PostMapping("/showSecondPageData")
	public ShowSecondPageDataRes showSecondPageData(@RequestBody ShowSecondPageDataReq req) throws CommonValidationException {
		List<ErrorCheck> error=propValidation.showSecondPageDataVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return propService.showSecondPageData(req);
	}
	@GetMapping("/showLayerBrokerage/{layerProposalNo}")
	public ShowLayerBrokerageRes showLayerBrokerage(@PathVariable ("layerProposalNo") String layerProposalNo) throws CommonValidationException {
					return propService.showLayerBrokerage(layerProposalNo);
		}
	@GetMapping("/checkProductMatch/{proposalNo}/{contractMode}/{productId}")
		public CheckProductMatchRes checkProductMatch(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("contractMode") boolean contractMode,@PathVariable ("productId") String productId) throws CommonValidationException {
						return propService.checkProductMatch(proposalNo,contractMode,productId);
			}
		@PostMapping("/showRetroContracts")
			public ShowRetroContractsRes showRetroContracts(@RequestBody ShowRetroContractsReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.showRetroContractsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.showRetroContracts(req);
			}
			@PostMapping("/getCrestaDetailList")
			public GetCrestaDetailListRes getCrestaDetailList(@RequestBody GetCrestaDetailListReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.getCrestaDetailListVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.getCrestaDetailList(req);
			}
			@PostMapping("/insertCrestaDetails")
			public InsertCrestaDetailsRes insertCrestaDetails(@RequestBody InsertCrestaDetailsReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.insertCrestaDetailsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.insertCrestaDetails(req);
			}
			@GetMapping("/CancelProposal/{proposalNo}/{newProposal}/{ProposalReference}")
			public CancelProposalRes cancelProposal(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("newProposal") String newProposal,@PathVariable ("ProposalReference") String ProposalReference) throws CommonValidationException {
							return propService.cancelProposal(proposalNo,newProposal,ProposalReference);
				}
			@PostMapping("/getRetentionDetails")
			public GetRetentionDetailsRes getRetentionDetails(@RequestBody GetRetentionDetailsReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.getRetentionDetailsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.getRetentionDetails(req);
			}
			@PostMapping("/getScaleCommissionList")
			public GetScaleCommissionListRes getScaleCommissionList(@RequestBody getScaleCommissionListReq req) throws CommonValidationException {
							return propService.getScaleCommissionList(req);
				}
		
			@PostMapping("/viewRiskDetails")
			public ViewRiskDetailsRes viewRiskDetails(@RequestBody ViewRiskDetailsReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.viewRiskDetailsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.viewRiskDetails(req);
			}
			@GetMapping("/getprofitCommissionDelete/{Proposalno}/{BranchCode}/{ProfitSno}")
			public getprofitCommissionDeleteRes getprofitCommissionDelete(@PathVariable ("Proposalno") String Proposalno,@PathVariable ("BranchCode") String BranchCode,@PathVariable ("ProfitSno") String ProfitSno) throws CommonValidationException{
				List<ErrorCheck> error=propValidation.getprofitCommissionDeletevali(Proposalno,BranchCode,ProfitSno);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.getprofitCommissionDelete(Proposalno,BranchCode,ProfitSno);
			}
			
			@PostMapping("/showSecondPageData1")
			public showSecondPageData1Res showSecondPageData1(@RequestBody showSecondPageData1Req req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.showSecondPageData1vali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.showSecondPageData1(req);
			}
			
			@GetMapping("/getprofitCommissionEdit/{Proposalno}/{BranchCode}/{ProfitSno}")
			public getprofitCommissionEditRes getprofitCommissionEdit(@PathVariable ("Proposalno") String Proposalno,@PathVariable ("BranchCode") String BranchCode,@PathVariable ("ProfitSno") String ProfitSno) throws CommonValidationException{
				return propService.getprofitCommissionEdit(Proposalno,BranchCode,ProfitSno);
			}
			
			@PostMapping("/ProfitCommissionList")
			public ProfitCommissionListRes ProfitCommissionList(@RequestBody ProfitCommissionListReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.ProfitCommissionListvali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.ProfitCommissionList(req);
			}
			
			@PostMapping("/ScaleCommissionInsert")
			public ScaleCommissionInsertRes ScaleCommissionInsert(@RequestBody ScaleCommissionInsertReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.ScaleCommissionInsertvali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.ScaleCommissionInsert(req);
			}
			
			@PostMapping("/saveRiskDeatilsSecondForm")
			public saveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(@RequestBody saveRiskDeatilsSecondFormReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.saveRiskDeatilsSecondFormvali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.saveRiskDeatilsSecondForm(req);
			}
			@GetMapping("/checkAvialability/{proposalno}/{pid}")
			public checkAvialabilityRes checkAvialability (@PathVariable ("proposalno")String proposalno,@PathVariable ("pid") String pid) throws CommonValidationException {
				return propService.checkAvialability(proposalno,pid);
			} 
			
			@PostMapping("/updateOfferNo")
			public CommonSaveRes updateOfferNo(@RequestBody UpdateOfferNoReq req) throws CommonValidationException {
				List<ErrorCheck> error=propValidation.updateOfferNoVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return propService.updateOfferNo(req);
			}
			@PostMapping("/getSlidingScaleMethodInfo")
			public GetSlidingScaleMethodInfoRes getSlidingScaleMethodInfo(@RequestBody getSlidingScaleMethodInfo req) throws CommonValidationException {
							return propService.getSlidingScaleMethodInfo(req);
				} 
			@PostMapping("/insertSlidingScaleMentodInfo")
				public CommonResponse insertSlidingScaleMentodInfo(@RequestBody ScaleCommissionInsertReq req) throws CommonValidationException {
					List<ErrorCheck> error=propValidation.insertSlidingScaleMentodInfoVali(req);
					if(error!=null && error.size()>0) {
						throw new CommonValidationException("error",error);
					}
					return propService.insertSlidingScaleMentodInfo(req);
				} 
			@GetMapping("/getSectionEditMode/{proposalNo}")
				public GetSectionEditModeRes getSectionEditMode (@PathVariable ("proposalNo")String proposalNo) throws CommonValidationException {
					return propService.getSectionEditMode(proposalNo);
				}  
				@PostMapping("/getSectionDuplicationChseck")
				public CommonSaveRes getSectionDuplicationCheck(@RequestBody GetSectionDuplicationCheckReq req) throws CommonValidationException {
					List<ErrorCheck> error=propValidation.getSectionDuplicationCheckVali(req);
					if(error!=null && error.size()>0) {
						throw new CommonValidationException("error",error);
					}
					return propService.getSectionDuplicationCheck(req);
				}  
				@PostMapping("/convertPolicy")
				public ConvertPolicyRes convertPolicy(@RequestBody ConvertPolicyReq req) throws CommonValidationException {
					List<ErrorCheck> error=propValidation.convertPolicyVali(req);
					if(error!=null && error.size()>0) {
						throw new CommonValidationException("error",error);
					}
					return propService.convertPolicy(req);
				}
				
				@PostMapping("/getcalculateSC")
				public GetcalculateSCRes getcalculateSC(@RequestBody GetcalculateSCReq req) throws CommonValidationException {
					List<ErrorCheck> error=propValidation.getcalculateSCVali(req);
				if(error!=null&& error.size()>0) {
					throw new CommonValidationException("error", error);
				}
				return propService.getcalculateSC(req);
				}
				@GetMapping("/ttrnRipDelete/{referenceNo}")
				public CommonResponse ttrnRipDelete (@PathVariable ("referenceNo")String referenceNo) throws CommonValidationException {
					return propService.ttrnRipDelete(referenceNo);
				}  
				@GetMapping("/ttrnBonusDelete/{proposalNo}")
				public CommonResponse ttrnBonusDelete (@PathVariable ("referenceNo")String referenceNo) throws CommonValidationException {
					return propService.ttrnBonusDelete(referenceNo);
				}  
				
}



 