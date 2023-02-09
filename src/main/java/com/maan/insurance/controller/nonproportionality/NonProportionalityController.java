package com.maan.insurance.controller.nonproportionality;

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
import com.maan.insurance.model.req.nonproportionality.CrestaSaveReq;
import com.maan.insurance.model.req.nonproportionality.GetLayerInfoReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertBonusDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertIEModuleReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;
import com.maan.insurance.model.req.nonproportionality.MoveReinstatementMainReq;
import com.maan.insurance.model.req.nonproportionality.ReInstatementMainInsertReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.SaveSecondPageReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.nonproportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageData1Req;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.nonproportionality.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.nonproportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.nonproportionality.getReInstatementDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.insertClassLimitReq;
import com.maan.insurance.model.req.nonproportionality.insertProportionalTreatyReq;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;
import com.maan.insurance.model.res.nonproportionality.CheckAvialabilityRes;
import com.maan.insurance.model.res.nonproportionality.CommonResponse;
import com.maan.insurance.model.res.nonproportionality.CommonSaveRes;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetCommonValueRes;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes;
import com.maan.insurance.model.res.nonproportionality.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.nonproportionality.GetReInstatementDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.nonproportionality.UpdateProportionalTreatyRes;
import com.maan.insurance.model.res.nonproportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.nonproportionality.insertProportionalTreatyRes;
import com.maan.insurance.service.nonproportionality.NonProportionalityService;
import com.maan.insurance.validation.nonproportionality.NonProportionalityValidation;

@RestController
@RequestMapping("/Insurance/Nonproportionality")
public class NonProportionalityController {
	Gson gson = new Gson();
	
	@Autowired
	private NonProportionalityService nonPropService;
	@Autowired
	private NonProportionalityValidation nonPropValidation;
	
	@PostMapping("/showSecondPageData1")
	public ShowSecondPageData1Res showSecondPageData1(@RequestBody ShowSecondPageData1Req req) throws CommonValidationException {
		List<ErrorCheck> error=nonPropValidation.showSecondPageData1Vali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return nonPropService.showSecondPageData1(req);
	}
	@GetMapping("/getShortname/{branchCode}")
	public GetCommonValueRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
					return nonPropService.getShortname(branchCode);
		}
	@GetMapping("/GetRemarksDetails/{proposalNo}/{layerNo}")
	public GetRemarksDetailsRes GetRemarksDetails(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return nonPropService.GetRemarksDetails( proposalNo,layerNo);
		}
	@GetMapping("/getSumOfCover/{proposalNo}/{branchCode}/{productId}/{layerNo}")
	public GetCommonValueRes getSumOfCover(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
		return nonPropService.getSumOfCover( proposalNo,branchCode,productId,layerNo);
		}
	@GetMapping("/showLayerBrokerage/{layerProposalNo}")
	public ShowLayerBrokerageRes showLayerBrokerage(@PathVariable ("layerProposalNo") String layerProposalNo) throws CommonValidationException {
					return nonPropService.showLayerBrokerage(layerProposalNo);
		}
	@PostMapping("/showRetroContracts")
	public ShowRetroContractsRes showRetroContracts(@RequestBody ShowRetroContractsReq req) throws CommonValidationException {
		List<ErrorCheck> error=nonPropValidation.showRetroContractsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return nonPropService.showRetroContracts(req);
	}
	@PostMapping("/getRetroContractDetails")
	public GetCommonValueRes getRetroContractDetails(@RequestBody GetRetroContractDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error=nonPropValidation.getRetroContractDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return nonPropService.getRetroContractDetails(req);
	}
	@GetMapping("/checkAvialability/{proposalNo}/{productId}")
	public CheckAvialabilityRes checkAvialability(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("productId") String productId) throws CommonValidationException {
					return nonPropService.checkAvialability(proposalNo,productId);
		}
	@PostMapping("/showRetroCess1")
		public ShowRetroCess1Res showRetroCess1(@RequestBody ShowRetroCess1Req req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.showRetroCess1Vali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.showRetroCess1(req);
		}
		@PostMapping("/updateProportionalTreaty")
		public UpdateProportionalTreatyRes updateProportionalTreaty(@RequestBody insertProportionalTreatyReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.insertProportionalTreatyvali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.updateProportionalTreaty(req);
		}

		/*
		 * @PostMapping("/insertClassLimit") public CommonResponse
		 * insertClassLimit(@RequestBody insertClassLimitReq req) throws
		 * CommonValidationException { List<ErrorCheck>
		 * error=nonPropValidation.insertClassLimitVali(req); if(error!=null &&
		 * error.size()>0) { throw new CommonValidationException("error",error); }
		 * return nonPropService.insertClassLimit(req); }
		 */
		@PostMapping("/viewRiskDetails")
		public ViewRiskDetailsRes viewRiskDetails(@RequestBody ViewRiskDetailsReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.viewRiskDetailsVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.viewRiskDetails(req);
		}
		@PostMapping("/riskDetailsEditMode")
		public RiskDetailsEditModeRes riskDetailsEditMode(@RequestBody RiskDetailsEditModeReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.riskDetailsEditModeVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.riskDetailsEditMode(req);
		}
		@PostMapping("/showSecondPageData")
		public ShowSecondPageDataRes showSecondPageData(@RequestBody ShowSecondPageDataReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.showSecondPageDataVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.showSecondPageData(req);
		}
		@PostMapping("/getRetroContractDetailsList")
		public GetRetroContractDetailsListRes getRetroContractDetailsList(@RequestBody GetRetroContractDetailsListReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.getRetroContractDetailsListVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.getRetroContractDetailsList(req, req.getFlag(), req.getUwYear());
		} 
//		@PostMapping("/saveSecondPage")
//		public SaveSecondPageRes saveSecondPage(@RequestBody SaveSecondPageReq req) throws CommonValidationException {
//			List<ErrorCheck> error=nonPropValidation.saveSecondPageVali(req);
//			if(error!=null && error.size()>0) {
//				throw new CommonValidationException("error",error);
//			}
//			return nonPropService.saveSecondPage(req);
//		}
	@PostMapping("/insertProportionalTreaty")
	public insertProportionalTreatyRes insertProportionalTreaty(@RequestBody insertProportionalTreatyReq req) throws CommonValidationException {
		List<ErrorCheck> error=nonPropValidation.insertProportionalTreatyvali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return nonPropService.insertProportionalTreaty(req);
	}
//	@PostMapping("/getReInstatementDetailsList")
//	public GetReInstatementDetailsListRes getReInstatementDetailsList(@RequestBody GetReInstatementDetailsListReq req) throws CommonValidationException {
//		List<ErrorCheck> error=nonPropValidation.getReInstatementDetailsListVali(req);
//		if(error!=null && error.size()>0) {
//			throw new CommonValidationException("error",error);
//		}
//		return nonPropService.getReInstatementDetailsList(req);
//	}
	@PostMapping("/getReInstatementDetailsList")
	public GetReInstatementDetailsListRes getReInstatementDetailsList(@RequestBody getReInstatementDetailsListReq req) throws CommonValidationException {
					return nonPropService.getReInstatementDetailsList(req);
		}
	@PostMapping("/moveReinstatementMain")
	public CommonSaveRes moveReinstatementMain(@RequestBody MoveReinstatementMainReq req) throws CommonValidationException {
		List<ErrorCheck> error=nonPropValidation.moveReinstatementMainVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return nonPropService.moveReinstatementMain(req);
	}
	@GetMapping("/deleteMainTable/{proposalNo}/{amendId}/{branchCode}/{referenceNo}")
	public CommonResponse deleteMainTable(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("amendId") String amendId,@PathVariable ("branchCode") String branchCode,@PathVariable ("referenceNo") String referenceNo) throws CommonValidationException {
					return nonPropService.deleteMainTable(proposalNo,amendId,branchCode,referenceNo);
		}
	@GetMapping("/getLowClaimBonusList/{proposalNo}/{branchCode}/{acqBonus}/{referenceNo}")
	public GetLowClaimBonusListRes getLowClaimBonusList(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("acqBonus") String acqBonus,@PathVariable ("referenceNo") String referenceNo) throws CommonValidationException {
					return nonPropService.getLowClaimBonusList(proposalNo,branchCode,acqBonus,referenceNo);
		}
	@PostMapping("/LowClaimBonusInser")
		public CommonResponse lowClaimBonusInser(@RequestBody LowClaimBonusInserReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.lowClaimBonusInserVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.lowClaimBonusInser(req);
		}
		@GetMapping("/getInclusionExList/{proposalNo}/{branchCode}")
		public GetInclusionExListRes getInclusionExList(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
						return nonPropService.getInclusionExList(proposalNo,branchCode);
			}
		@PostMapping("/insertIEModule")
			public CommonResponse insertIEModule(@RequestBody InsertIEModuleReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.insertIEModuleVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.insertIEModule(req);
			}
		@PostMapping("/showSecondpageEditItems")
		public ShowSecondpageEditItemsRes showSecondpageEditItems(@RequestBody ShowSecondpageEditItemsReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.showSecondpageEditItemsVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.showSecondpageEditItems(req);
		}
		@PostMapping("/insertRemarkDetails")
		public CommonSaveRes insertRemarkDetails(@RequestBody RemarksSaveReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.validateinsertRemarkDetails(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.insertRemarkDetails(req);
		}
		@PostMapping("/getClassLimitDetails")
		public GetClassLimitDetailsRes getClassLimitDetails(@RequestBody GetClassLimitDetailsReq req) throws CommonValidationException {
			List<ErrorCheck> error=nonPropValidation.getClassLimitDetailsVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return nonPropService.getClassLimitDetails(req);
		}
//		@PostMapping("/saveRiskDeatilsSecondForm")
//		public SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(@RequestBody SaveSecondPageReq req) throws CommonValidationException {
//			List<ErrorCheck> error=nonPropValidation.saveSecondPageVali(req);
//			if(error!=null && error.size()>0) {
//				throw new CommonValidationException("error",error);
//			}
//			return nonPropService.saveRiskDeatilsSecondForm(req);
//		}
//		@GetMapping("/insertRetroCess/{proposalNo}/{noRetroCess}")
//		public CommonResponse insertRetroCess(@PathVariable ("proposalNo") String proposalNo, @PathVariable ("noRetroCess") String noRetroCess) throws CommonValidationException {
//						return nonPropService.insertRetroCess(proposalNo, noRetroCess);
//			}
		@PostMapping("/insertRetroCess")
			public CommonResponse insertRetroCess(@RequestBody InsertRetroCessReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.insertRetroCessVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.insertRetroCess(req);
			}
//			@PostMapping("/instalMentPremium")
//			public CommonResponse instalMentPremium(@RequestBody InstalMentPremiumReq req) throws CommonValidationException {
//				List<ErrorCheck> error=nonPropValidation.instalMentPremiumVali(req);
//				if(error!=null && error.size()>0) {
//					throw new CommonValidationException("error",error);
//				}
//				return nonPropService.instalMentPremium(req);
//			}
			@PostMapping("/insertRetroContracts")
			public CommonResponse insertRetroContracts(@RequestBody InsertRetroContractsReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.insertRetroContractsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.insertRetroContracts(req);
			}
			@PostMapping("/reInstatementMainInsert")
			public CommonResponse reInstatementMainInsert(@RequestBody ReInstatementMainInsertReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.reInstatementMainInsertVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.reInstatementMainInsert(req);
			}
			@PostMapping("/insertCrestaMaintable")
			public CommonResponse insertCrestaMaintable(@RequestBody CrestaSaveReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.validateinsertCrestaMaintable(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.insertCrestaMaintable(req);
			}
			@PostMapping("/insertBonusDetails")
			public CommonResponse insertBonusDetails(@RequestBody InsertBonusDetailsReq req) throws CommonValidationException {
				List<ErrorCheck> error=nonPropValidation.insertBonusDetailsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return nonPropService.insertBonusDetails(req);
			} 
			@GetMapping("/getLayerDuplicationCheck/{proposalNo}/{layerNo}/{baseLayer}")
			public CommonSaveRes getLayerDuplicationCheck(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("layerNo") String layerNo,@PathVariable ("baseLayer") String baseLayer) throws CommonValidationException {
							return nonPropService.getLayerDuplicationCheck(proposalNo,layerNo,baseLayer);
				} 
			@PostMapping("/getLayerInfo")
				public GetLayerInfoRes getLayerInfo(@RequestBody GetLayerInfoReq req) throws CommonValidationException {
					List<ErrorCheck> error=nonPropValidation.getLayerInfoVali(req);
					if(error!=null && error.size()>0) {
						throw new CommonValidationException("error",error);
					}
					return nonPropService.getLayerInfo(req);
				} 
			@GetMapping("/CancelProposal/{proposalNo}/{proposalReference}/{newProposal}")
			public CommonResponse cancelProposal(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("proposalReference") String proposalReference,@PathVariable ("newProposal") String newProposal) throws CommonValidationException {
							return nonPropService.cancelProposal(proposalNo,proposalReference,newProposal);
				} 
}
