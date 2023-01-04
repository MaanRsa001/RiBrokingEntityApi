package com.maan.insurance.controller.portFolio;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.controller.XolPremium.Xolpremiumcontroller;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.portFolio.GetAutoPendingListReq;
import com.maan.insurance.model.req.portFolio.GetContractsListReq;
import com.maan.insurance.model.req.portFolio.GetHistoryListReq;
import com.maan.insurance.model.req.portFolio.GetPendingListReq;
import com.maan.insurance.model.req.portFolio.ProcAutoReq;
import com.maan.insurance.model.res.portFolio.GetAutoPendingListRes;
import com.maan.insurance.model.res.portFolio.GetContractsListRes;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes;
import com.maan.insurance.model.res.portFolio.GetPendingListRes;
import com.maan.insurance.model.res.portFolio.GetRejectedListRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.portFolio.PortFolioService;
import com.maan.insurance.validation.portFolio.PortFolioValidation;

@RestController
@RequestMapping("/Insurance/PortFolio")
public class PortFolioController {
	Gson gson = new Gson();
	private Logger log = LogManager.getLogger(Xolpremiumcontroller.class);
	
	@Autowired
	private PortFolioService portServ;
	@Autowired
	private PortFolioValidation portVali;
	
	@PostMapping("/getPendingList")
	public GetPendingListRes getPendingList(@RequestBody GetPendingListReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.getPendingListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.getPendingList(req);
	}
//	@GetMapping("/GetContractPremium/{contractNo}/{layerNo}")
//	public CommonSaveRes getContractPremium(@PathVariable ("contractNo") String contractNo,@PathVariable ("layerNo") String layerNo) throws CommonValidationException {
//		return xps.getContractPremium(contractNo,layerNo);
//		}  
	
	@PostMapping("/getRejectedList")
	public GetRejectedListRes getRejectedList(@RequestBody GetPendingListReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.getPendingListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.getRejectedList(req);
	}
	@PostMapping("/getAutoPendingList")
	public GetAutoPendingListRes getAutoPendingList(@RequestBody GetAutoPendingListReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.getAutoPendingListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.getAutoPendingList(req);
	}
	@PostMapping("/getContractsList")
	public GetContractsListRes getContractsList(@RequestBody GetContractsListReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.getContractsListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.getContractsList(req);
	}  
	@PostMapping("/getHistoryList")
	public GetHistoryListRes getHistoryList(@RequestBody GetHistoryListReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.getHistoryListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.getHistoryList(req);
	}
	@PostMapping("/procAuto")
	public CommonResponse procAuto(@RequestBody ProcAutoReq req) throws CommonValidationException {
		List<ErrorCheck> error= portVali.procAutoVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return portServ.procAuto(req);
	}
}
