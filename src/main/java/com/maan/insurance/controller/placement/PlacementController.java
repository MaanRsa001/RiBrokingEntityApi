package com.maan.insurance.controller.placement;

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
import com.maan.insurance.model.req.placement.AttachFileReq;
import com.maan.insurance.model.req.placement.DeleteFileReq;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;
import com.maan.insurance.model.req.placement.GetExistingAttachListReq;
import com.maan.insurance.model.req.placement.GetExistingReinsurerListReq;
import com.maan.insurance.model.req.placement.GetMailTemplateReq;
import com.maan.insurance.model.req.placement.GetMailToListReq;
import com.maan.insurance.model.req.placement.GetPlacementInfoListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewReq;
import com.maan.insurance.model.req.placement.GetPlacingInfoReq;
import com.maan.insurance.model.req.placement.GetReinsurerInfoReq;
import com.maan.insurance.model.req.placement.InsertMailDetailsReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdateMailDetailsReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq;
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.placement.AttachFileRes;
import com.maan.insurance.model.res.placement.CommonSaveResList;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes;
import com.maan.insurance.model.res.placement.GetExistingAttachListRes;
import com.maan.insurance.model.res.placement.GetMailTemplateRes;
import com.maan.insurance.model.res.placement.GetPlacementInfoListRes;
import com.maan.insurance.model.res.placement.GetPlacementNoRes;
import com.maan.insurance.model.res.placement.GetPlacementViewListRes;
import com.maan.insurance.model.res.placement.GetPlacementViewRes;
import com.maan.insurance.model.res.placement.GetPlacingInfoRes;
import com.maan.insurance.model.res.placement.GetReinsurerInfoRes;
import com.maan.insurance.model.res.placement.InsertMailDetailsRes;
import com.maan.insurance.model.res.placement.InsertPlacingRes;
import com.maan.insurance.model.res.placement.PlacementSummaryRes;
import com.maan.insurance.model.res.placement.ProposalInfoRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.placement.PlacementService;
import com.maan.insurance.validation.placement.PlacementValidation;

@RestController
@RequestMapping("/Insurance/placement")
public class PlacementController {
Gson gson = new Gson();
	
	@Autowired
	private PlacementService serv;
	@Autowired
	private PlacementValidation val;
	
	@PostMapping("/getMailToList")
	public GetCommonDropDownRes getMailToList(@RequestBody GetMailToListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getMailToListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getMailToList(req);
	} 

	@PostMapping("/getExistingReinsurerList")
	public GetCommonDropDownRes getExistingReinsurerList(@RequestBody GetExistingReinsurerListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getExistingReinsurerListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getExistingReinsurerList(req);
	} 
	@PostMapping("/getExistingBrokerList")
	public GetCommonDropDownRes getExistingBrokerList(@RequestBody GetExistingReinsurerListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getExistingReinsurerListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getExistingBrokerList(req);
	} 
	@PostMapping("/getExistingAttachList")
	public GetExistingAttachListRes getExistingAttachList(@RequestBody GetExistingAttachListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getExistingAttachListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getExistingAttachList(req);
	}  
	@GetMapping("/proposalInfo/{branchCode}/{proposalNo}/{eProposalNo}")
	public ProposalInfoRes proposalInfo(@PathVariable ("branchCode") String branchCode,@PathVariable ("proposalNo") String proposalNo,@PathVariable ("eProposalNo") String eProposalNo) throws CommonValidationException {
		return serv.proposalInfo(branchCode,proposalNo,eProposalNo);
		}  
	@PostMapping("/getReinsurerInfo")
	public GetReinsurerInfoRes getReinsurerInfo(@RequestBody GetReinsurerInfoReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReinsurerInfoVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getReinsurerInfo(req);
		}   
	@PostMapping("/getPlacementInfoList")
	public GetPlacementInfoListRes getPlacementInfoList(@RequestBody GetPlacementInfoListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPlacementInfoListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPlacementInfoList(req);
		} 
	@PostMapping("/savePlacing")
	public CommonSaveResList savePlacing(@RequestBody SavePlacingReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validatePlacing(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
		}
			return serv.savePlacing(req);
		}  
	@PostMapping("/getPlacementNo")
		public GetPlacementNoRes getPlacementNo(@RequestBody SavePlacingReq req) throws CommonValidationException {
			List<ErrorCheck> error= val.validatePlacing(req);
			if(error!=null && error.size()>0) {
				 throw new CommonValidationException("error",error);
		}
			return serv.getPlacementNo(req);
	} 
	@PostMapping("/insertPlacing")
	public InsertPlacingRes insertPlacing(@RequestBody SavePlacingReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validatePlacing(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.insertPlacing(req);
	} 	
	@PostMapping("/getPlacingInfo")
	public GetPlacingInfoRes getPlacingInfo(@RequestBody GetPlacingInfoReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPlacingInfoVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.getPlacingInfo(req);
	} 
	@PostMapping("/editPlacingDetails")
	public EditPlacingDetailsRes editPlacingDetails(@RequestBody EditPlacingDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.editPlacingDetailsVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.editPlacingDetails(req);
	} 
	@PostMapping("/updatePlacement")
	public CommonResponse updatePlacement(@RequestBody UpdatePlacementReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validationStatus(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.updatePlacement(req);
	} 
	@PostMapping("/uploadDocument")
	public UploadDocumentRes uploadDocument(@RequestBody UploadDocumentReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.uploadDocumentVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.uploadDocument(req);
	} 
	@PostMapping("/attachFile")
	public AttachFileRes attachFile(@RequestBody AttachFileReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.attachFileVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.attachFile(req);
	}  
	@PostMapping("/sendMail")
	public CommonResponse sendMail(@RequestBody SendMailReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.sendMailVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.sendMail(req);
	} 
	@PostMapping("/updateStatus")
	public CommonResponse updateStatus(@RequestBody UpdatePlacementReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validationStatus(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.updateStatus(req);
	}  
	@PostMapping("/insertMailDetails")
	public InsertMailDetailsRes insertMailDetails(@RequestBody InsertMailDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.insertMailDetailsVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.insertMailDetails(req);
	} 
	@PostMapping("/updateMailDetails")
	public CommonResponse updateMailDetails(@RequestBody UpdateMailDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.updateMailDetailsVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.updateMailDetails(req);  
	}   
	@PostMapping("/deleteFile")
	public CommonSaveRes deleteFile(@RequestBody DeleteFileReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.deleteFileVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.deleteFile(req);  
	}   
	@PostMapping("/downloadFile")
	public CommonSaveRes downloadFile(@RequestBody DeleteFileReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.deleteFileVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.downloadFile(req);  
	} 
	@PostMapping("/getPlacementViewList")
	public GetPlacementViewListRes getPlacementViewList(@RequestBody GetPlacementViewListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPlacementViewListVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.getPlacementViewList(req);  
	} 
	@PostMapping("/getPlacementView")
	public GetPlacementViewRes getPlacementView(@RequestBody GetPlacementViewReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPlacementViewVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.getPlacementView(req);  
	}  
	@PostMapping("/placementSummary")
	public PlacementSummaryRes placementSummary(@RequestBody PlacementSummaryReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.placementSummaryVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.placementSummary(req);  
	}
	@PostMapping("/getMailTemplate")
	public GetMailTemplateRes getMailTemplate(@RequestBody GetMailTemplateReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getMailTemplateVali(req);
		if(error!=null && error.size()>0) {
			 throw new CommonValidationException("error",error);
	}
		return serv.getMailTemplate(req);  
	}
}
