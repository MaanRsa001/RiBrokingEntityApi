package com.maan.insurance.controller.facultative;

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
import com.maan.insurance.controller.claim.ClaimController;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.req.facultative.DeleteMaintableReq;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.GetInsurarerDetailsReq;
import com.maan.insurance.model.req.facultative.GetLowClaimBonusListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.facultative.InserLossRecordReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.facultative.InsertInsurarerTableInsertReq;
import com.maan.insurance.model.req.facultative.InsertXolCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InstalMentPremiumReq;
import com.maan.insurance.model.req.facultative.MoveBonusReq;
import com.maan.insurance.model.req.facultative.SecondPageInsertReq;
import com.maan.insurance.model.req.facultative.ShowSecondPagedataReq;
import com.maan.insurance.model.req.facultative.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;
import com.maan.insurance.model.req.facultative.ViewModeReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.claim.ProposalNoRes;
import com.maan.insurance.model.res.facultative.CommonResponse;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes;
import com.maan.insurance.model.res.facultative.GetCommonValueRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsRes;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.facultative.ViewModeRes;
import com.maan.insurance.model.res.nonproportionality.CommonSaveRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.service.claim.ClaimService;
import com.maan.insurance.service.facultative.FacultativeService;
import com.maan.insurance.validation.Claim.ClaimValidation;
import com.maan.insurance.validation.facultative.FacultativeValidation;

@RestController
@RequestMapping("/Insurance/Facultative")
public class FacultativeController {
	Gson gson = new Gson();
	private Logger log = LogManager.getLogger(FacultativeController.class);
	
	@Autowired
	private FacultativeService facService;
	@Autowired
	private FacultativeValidation facValidation;
	
	@GetMapping("/getShortname/{branchCode}")
	public GetCommonValueRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
					return facService.getShortname(branchCode);
		}
	@PostMapping("/firstPageInsert")
	public FirstPageInsertRes firstPageInsert(@RequestBody FirstPageInsertReq req) throws CommonValidationException {
		List<ErrorCheck> error=facValidation.firstPageInsertVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facService.firstPageInsert(req);
	}
	@PostMapping("/insertRemarkDetails")
	public CommonResponse insertRemarkDetails(@RequestBody RemarksSaveReq req) throws CommonValidationException {
		List<ErrorCheck> error=facValidation.validateinsertRemarkDetails(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return facService.insertRemarkDetails(req);
	}
	@GetMapping("/UpadateUWShare/{shSd}/{proposalNo}")
	public CommonResponse UpadateUWShare(@PathVariable ("shSd") String shSd, @PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
					return facService.UpadateUWShare(shSd,proposalNo);
		}
	@PostMapping("/InsertCoverDeductableDetails")
		public CommonResponse insertCoverDeductableDetails(@RequestBody InsertCoverDeductableDetailsReq req) throws CommonValidationException {
			List<ErrorCheck> error=facValidation.insertCoverDeductableDetailsVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return facService.insertCoverDeductableDetails(req);
		}
		@PostMapping("/InsertXolCoverDeductableDetails")
		public CommonResponse insertXolCoverDeductableDetails(@RequestBody InsertXolCoverDeductableDetailsReq req) throws CommonValidationException {
			List<ErrorCheck> error=facValidation.insertXolCoverDeductableDetailsVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return facService.insertXolCoverDeductableDetails(req);
		}
//		@PostMapping("/getLowClaimBonusList")
//		public GetLowClaimBonusListRes getLowClaimBonusList(@RequestBody GetLowClaimBonusListReq req) throws CommonValidationException {
//			List<ErrorCheck> error=facValidation.getLowClaimBonusListVali(req);
//			if(error!=null && error.size()>0) {
//				throw new CommonValidationException("error",error);
//			}
//			return facService.getLowClaimBonusList(req);
//		}
		@GetMapping("/getLowClaimBonusList/{proposalNo}/{branchCode}/{acqBonus}")
		public GetLowClaimBonusListRes getLowClaimBonusList(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("acqBonus") String acqBonus) throws CommonValidationException {
						return facService.getLowClaimBonusList(proposalNo,branchCode,acqBonus);
			}
		@PostMapping("/showSecondPagedata")
			public ShowSecondPagedataRes showSecondPagedata(@RequestBody ShowSecondPagedataReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.showSecondPagedataVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return facService.showSecondPagedata(req);
			}
			@PostMapping("/getRetroContractDetailsList")
			public GetRetroContractDetailsListRes getRetroContractDetailsList(@RequestBody GetRetroContractDetailsListReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.getRetroContractDetailsListVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return facService.getRetroContractDetailsList(req,req.getFlag());
			}
			@PostMapping("/ShowSecondPageEditItems")
			public ShowSecondpageEditItemsRes ShowSecondPageEditItems(@RequestBody ShowSecondpageEditItemsReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.ShowSecondPageEditItemsVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return facService.ShowSecondPageEditItems(req);
			}
			@GetMapping("/GetRemarksDetails/{proposalNo}")
			public GetRemarksDetailsRes getRemarksDetails(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
				return facService.getRemarksDetails( proposalNo);
				} 
			@PostMapping("/getInsurarerDetails")
				public GetInsurarerDetailsRes getInsurarerDetails(@RequestBody GetInsurarerDetailsReq req) throws CommonValidationException {
					List<ErrorCheck> error=facValidation.getInsurarerDetailsVali(req);
					if(error!=null && error.size()>0) {
						throw new CommonValidationException("error",error);
					}
					return facService.getInsurarerDetails(req);
				}
			@GetMapping("/getLossDEtails/{proposalNo}")
			public GetLossDEtailsRes getLossDEtails(@PathVariable ("proposalNo") String proposalNo) throws CommonValidationException {
				return facService.getLossDEtails( proposalNo);
				} 
			@PostMapping("/MoveBonus")
			public CommonResponse moveBonus(@RequestBody MoveBonusReq req) throws CommonValidationException {
			List<ErrorCheck> error=facValidation.moveBonusVali(req);
			if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
			}
			return facService.moveBonus(req);
			}			
			@PostMapping("/deleteMaintable")
			public CommonResponse deleteMaintable(@RequestBody DeleteMaintableReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.deleteMaintableVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.deleteMaintable(req);
			} 
			@PostMapping("/secondPageInsert")
			public SecondPageInsertRes secondPageInsert(@RequestBody SecondPageInsertReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.secondPageInsertVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.secondPageInsert(req);
			}
			@PostMapping("/updateSecondPage")
			public CommonResponse updateSecondPage(@RequestBody UpdateSecondPageReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.updateSecondPageVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.updateSecondPage(req);
			} 
			@PostMapping("/inserLossRecord")
			public CommonResponse inserLossRecord(@RequestBody InserLossRecordReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.inserLossRecordVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.inserLossRecord(req);
			}
			@PostMapping("/instalMentPremium")
			public CommonResponse instalMentPremium(@RequestBody InstalMentPremiumReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.instalMentPremiumVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.instalMentPremium(req);
			}
			@PostMapping("/insertInsurarerTableInsert")
			public CommonResponse insertInsurarerTableInsert(@RequestBody InsertInsurarerTableInsertReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.insertInsurarerTableInsertVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.insertInsurarerTableInsert(req);
			} 
			@PostMapping("/insertBonusDetails")
			public CommonResponse insertBonusDetails(@RequestBody InsertBonusDetailsReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.insertBonusDetailsVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.insertBonusDetails(req);
			}
			@PostMapping("/insertCrestaMaintable")
			public CommonResponse insertCrestaMaintable(@RequestBody InsertCrestaMaintableReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.insertCrestaMaintableVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.insertCrestaMaintable(req);
			} 
			@PostMapping("/viewMode")
			public ViewModeRes viewMode(@RequestBody ViewModeReq req) throws CommonValidationException {
				List<ErrorCheck> error=facValidation.viewModeVali(req);
					if(error!=null && error.size()>0) {
							throw new CommonValidationException("error",error);
					}
						return facService.viewMode(req);
			} 
			@GetMapping("/GetCoverDeductableDetails/{proposalNo}/{branchCode}/{productId}")
			public GetCoverDeductableDetailsRes getCoverDeductableDetails(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
				return facService.getCoverDeductableDetails(proposalNo,branchCode,productId);
				}  
			@GetMapping("/GetXolCoverDeductableDetails/{proposalNo}/{branchCode}")
				public GetXolCoverDeductableDetailsRes GetXolCoverDeductableDetails(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
					return facService.GetXolCoverDeductableDetails(proposalNo,branchCode);
					} 
				@PostMapping("/getRetroContractDetails")
					public GetCommonValueRes getRetroContractDetails(@RequestBody GetRetroContractDetailsReq req) throws CommonValidationException {
						List<ErrorCheck> error=facValidation.getRetroContractDetailsVali(req);
							if(error!=null && error.size()>0) {
									throw new CommonValidationException("error",error);
							}
								return facService.getRetroContractDetails(req);
			}
				@GetMapping("/GetShareValidation/{proposalNo}/{leaderUWShare}")
				public GetCommonValueRes getShareValidation(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("leaderUWShare") String leaderUWShare) throws CommonValidationException {
					return facService.getShareValidation(proposalNo,leaderUWShare);
					} 
				@GetMapping("/getBonusListCount/{proposalNo}/{branchCode}/{acqBonus}/{endorsmentno}")
					public GetCommonValueRes getBonusListCount(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("branchCode") String branchCode,@PathVariable ("acqBonus") String acqBonus,@PathVariable ("endorsmentno") String endorsmentno) throws CommonValidationException {
						return facService.getBonusListCount(proposalNo,branchCode,acqBonus,endorsmentno);
						}
}
