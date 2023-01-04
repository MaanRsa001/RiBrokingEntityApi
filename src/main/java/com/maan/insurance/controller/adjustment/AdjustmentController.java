package com.maan.insurance.controller.adjustment;

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
import com.maan.insurance.model.req.adjustment.AdjustmentViewReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentDoneListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentPayRecListReq;
import com.maan.insurance.model.req.adjustment.InsertReverseReq;
import com.maan.insurance.model.req.retro.FirstInsertReq;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentDoneListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentPayRecListRes;
import com.maan.insurance.model.res.adjustment.InsertReverseRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.adjustment.AdjustmentService;
import com.maan.insurance.service.retro.RetroService;
import com.maan.insurance.validation.adjustment.AdjustmentValidation;
import com.maan.insurance.validation.retro.RetroValidation;

@RestController
@RequestMapping("/Insurance/adjustment")
public class AdjustmentController {
	Gson gson = new Gson();
	
	@Autowired
	private AdjustmentService adjServ;
	@Autowired
	private AdjustmentValidation adjServVali;
	
	@PostMapping("/getAdjustmentDoneList")
	public GetAdjustmentDoneListRes getAdjustmentDoneList(@RequestBody GetAdjustmentDoneListReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.getAdjustmentDoneListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.getAdjustmentDoneList(req);
	}
//	@GetMapping("/getShortname/{branchCode}")
//	public CommonSaveRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
//		return retroServ.getShortname(branchCode);
//		}  
	@PostMapping("/adjustmentView")
	public AdjustmentViewRes adjustmentView(@RequestBody AdjustmentViewReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.adjustmentViewVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.adjustmentView(req);
	} 
	@PostMapping("/getAdjustmentList")
	public GetAdjustmentListRes getAdjustmentList(@RequestBody GetAdjustmentListReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.validateAdjustList(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.getAdjustmentList(req);
	} 
	@PostMapping("/getAdjustmentPayRecList")
	public GetAdjustmentPayRecListRes getAdjustmentPayRecList(@RequestBody GetAdjustmentListReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.validateAdjustList(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.getAdjustmentPayRecList(req);
	}  
	@PostMapping("/addAdjustment")
	public CommonResponse addAdjustment(@RequestBody GetAdjustmentListReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.validteAdjustment(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.addAdjustment(req);
	}  
	@PostMapping("/addAdjustmentPayRec")
	public CommonResponse addAdjustmentPayRec(@RequestBody GetAdjustmentListReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.validteAdjustment(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.addAdjustmentPayRec(req);
	} 
	@PostMapping("/insertReverse")
	public InsertReverseRes insertReverse(@RequestBody InsertReverseReq req) throws CommonValidationException {
		List<ErrorCheck> error= adjServVali.validateReverseInsert(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return adjServ.insertReverse(req);
	}
}
