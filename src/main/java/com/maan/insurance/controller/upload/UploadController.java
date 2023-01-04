package com.maan.insurance.controller.upload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.DoUploadReq;
import com.maan.insurance.model.req.home.GetOldProductIdReq;
import com.maan.insurance.model.req.upload.DoDeleteDocDetailsReq;
import com.maan.insurance.model.req.upload.GetDocListReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.upload.AllmoduleListRes;
import com.maan.insurance.model.res.upload.GetDocListRes;
import com.maan.insurance.model.res.upload.GetDocTypeRes;
import com.maan.insurance.service.home.HomeService;
import com.maan.insurance.service.upload.UploadService;
import com.maan.insurance.validation.home.HomeValidation;
import com.maan.insurance.validation.upload.UploadValidation;

@RestController
@RequestMapping("/Insurance/upload")
public class UploadController {
	Gson gson = new Gson(); 
	@Autowired
	private UploadService serv;
	@Autowired
	private UploadValidation val;
	
	@PostMapping("/getDocList")
	public GetDocListRes getOldProductId(@RequestBody GetDocListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getDocListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getDocList(req);
	}  
	@PostMapping("/doDeleteDocDetails")
	public CommonResponse doDeleteDocDetails(@RequestBody DoDeleteDocDetailsReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.doDeleteDocDetailsVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.doDeleteDocDetails(req);
	} 

	@GetMapping("/allmoduleList/{branchCode}")
	public AllmoduleListRes allmoduleList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.allmoduleList(branchCode);
		}  
	@GetMapping("/getDocType/{branchCode}/{productId}/{moduleType}")
		public GetDocTypeRes getDocType(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId,@PathVariable ("moduleType") String moduleType) throws CommonValidationException {
			return serv.getDocType(branchCode,productId,moduleType);
			} 
		@GetMapping("/moduleTypeList/{branchCode}")
		public GetCommonDropDownRes moduleTypeList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
			return serv.moduleTypeList(branchCode);
			}  
		
//		@PostMapping("/doUpload")
//			public CommonSaveRes doUpload(@RequestBody DoUploadReq req) throws CommonValidationException {
//				List<ErrorCheck> error= val.doUploadVali(req);
//				if(error!=null && error.size()>0) {
//					throw new CommonValidationException("error",error);
//				}
//				return serv.doUpload(req);
//			} 
		@PostMapping("/doUpload")
		public CommonSaveRes doUpload(@RequestParam("File") MultipartFile file, @RequestParam("Req") String jsonString) throws CommonValidationException, JsonMappingException, JsonProcessingException{
			DoUploadReq req =  new ObjectMapper().readValue(jsonString, DoUploadReq.class); 
			List<ErrorCheck> error= val.doUploadVali(req, file);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return serv.doUpload(req,file);
		}
}
