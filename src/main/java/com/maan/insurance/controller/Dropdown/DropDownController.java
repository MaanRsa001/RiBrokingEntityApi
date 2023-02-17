package com.maan.insurance.controller.Dropdown;

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
import com.maan.insurance.controller.TreasuryController;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.DropDown.DuplicateCountCheckReq;
import com.maan.insurance.model.req.DropDown.GetClaimDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetContractLayerNoReq;
import com.maan.insurance.model.req.DropDown.GetCopyQuoteReq;
import com.maan.insurance.model.req.DropDown.GetCurrencyIdReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetExchangeRateReq;
import com.maan.insurance.model.req.DropDown.GetInwardBusinessTypeDropDownReq;
import com.maan.insurance.model.req.DropDown.GetPlacedProposalListReq;
import com.maan.insurance.model.req.DropDown.GetPreDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProductModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProfitCentreieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProposalNoReq;
import com.maan.insurance.model.req.DropDown.GetSectionListReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiDropDownReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiReq;
import com.maan.insurance.model.req.DropDown.GetTreatyTypeDropDownReq;
import com.maan.insurance.model.req.DropDown.GetYearListValueReq;
import com.maan.insurance.model.req.DropDown.GetYearToListValueReq;
import com.maan.insurance.model.req.DropDown.SavehtmltoPdfReq;
import com.maan.insurance.model.req.DropDown.UpdatepositionMasterEndtStatusReq;
import com.maan.insurance.model.req.DropDown.ValidatethreeReq;
import com.maan.insurance.model.req.DropDown.endorsementModeReq;
import com.maan.insurance.model.req.DropDown.updateBqEditModeReq;
import com.maan.insurance.model.req.DropDown.updateSubEditModeReq;
import com.maan.insurance.model.req.proportionality.ContractReq;
import com.maan.insurance.model.res.DropDown.CommonResponse;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetListRes;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.DropDown.GetCompanyInfoRes;
import com.maan.insurance.model.res.DropDown.GetContractValRes;
import com.maan.insurance.model.res.DropDown.GetNewContractInfoRes;
import com.maan.insurance.model.res.DropDown.GetNotPlacedProposalListRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.DropDown.GetPlacementInfoListRes;
import com.maan.insurance.model.res.DropDown.GetYearToListValueRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;

import com.maan.insurance.service.Dropdown.DropDownService;
import com.maan.insurance.validation.Dropdown.DropDownValidation;

@RestController
@RequestMapping("/Insurance/DropDown")
public class DropDownController {
	Gson gson = new Gson();
	private Logger log = LogManager.getLogger(TreasuryController.class);

	@Autowired
	private DropDownService dropDownservice;
	@Autowired
	private DropDownValidation dropDownVali;

	@GetMapping("/EditModeStatus/{proposalNo}")
	public GetCommonValueRes EditModeStatus(@PathVariable("proposalNo") String proposalNo)
			throws CommonValidationException {

		return dropDownservice.EditModeStatus(proposalNo);

	}

	@GetMapping("/getCoverLimitAmount/{proposalNo}/{departmentId}/{productId}")
	public GetCommonValueRes getCoverLimitAmount(@PathVariable("proposalNo") String proposalNo,
			@PathVariable("departmentId") String departmentId, @PathVariable("productId") String productId)
			throws CommonValidationException {

		return dropDownservice.getCoverLimitAmount(proposalNo, departmentId, productId);

	}

	@GetMapping("/getOpenPeriod/{branchCode}")
	public GetOpenPeriodRes getOpenPeriod(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {

		return dropDownservice.getOpenPeriod(branchCode);

	}

	@GetMapping("/getCurrencyMasterDropDown/{branchCode}/{countryId}")
	public GetCommonDropDownRes getCurrencyMasterDropDown(@PathVariable("branchCode") String branchCode,
			@PathVariable("countryId") String countryId) throws CommonValidationException {

		return dropDownservice.getCurrencyMasterDropDown(branchCode, countryId);

	}

	@GetMapping("/getDepartmentName/{branchCode}/{productCode}/{deptCode}")
	public GetCommonValueRes getDepartmentName(@PathVariable("branchCode") String branchCode,
			@PathVariable("productCode") String productCode, @PathVariable("deptCode") String deptCode)
			throws CommonValidationException {
		return dropDownservice.getDepartmentName(branchCode, productCode, deptCode);
	}

	@PostMapping("/getClaimDepartmentDropDown")
	public GetCommonDropDownRes getClaimDepartmentDropDown(@RequestBody GetClaimDepartmentDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getClaimDepartmentDropDownVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getClaimDepartmentDropDown(req);

	}

	@PostMapping("/getSubProfitCentreMulti")
	public GetCommonDropDownRes getSubProfitCentreMulti(@RequestBody GetSubProfitCentreMultiReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getSubProfitCentreMultiVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getSubProfitCentreMulti(req);

	}

	@PostMapping("/getCurrencyId")
	public GetCommonValueRes getCurrencyId(@RequestBody GetCurrencyIdReq req) throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getCurrencyIdVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getCurrencyId(req);

	}

	@PostMapping("/getProposalNo")
	public GetCommonValueRes getProposalNo(@RequestBody GetProposalNoReq req) throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getProposalNoVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getProposalNo(req);

	}

	@PostMapping("/premium/getSectionList")
	public GetCommonDropDownRes getSectionList(@RequestBody GetSectionListReq req) throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getSectionListVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getSectionList(req);

	}

	@PostMapping("/premium/getContractLayerNo")
	public GetCommonValueRes getContractLayerNo(@RequestBody GetContractLayerNoReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getContractLayerNoVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getContractLayerNo(req);

	}

	@PostMapping("/premium/getPreDepartmentDropDown")
	public GetCommonDropDownRes getPreDepartmentDropDown(@RequestBody GetPreDepartmentDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getPreDepartmentDropDownVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getPreDepartmentDropDown(req);

	}

	@PostMapping("/getSubProfitCentreMultiDropDown")
	public GetCommonDropDownRes getSubProfitCentreMultiDropDown(@RequestBody GetSubProfitCentreMultiDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getSubProfitCentreMultiDropDownVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getSubProfitCentreMultiDropDown(req);

	}

	@GetMapping("/getCashLossCount/{contractNo}/{departmentId}")
	public GetCommonValueRes getCashLossCount(@PathVariable("contractNo") String contractNo,
			@PathVariable("departmentId") String departmentId) throws CommonValidationException {

		return dropDownservice.getCashLossCount(contractNo, departmentId);
	}

	@PostMapping("/Validatethree")
	public int validatethree(@RequestBody ValidatethreeReq req) throws CommonValidationException {
		return dropDownservice.Validatethree(req.getBranchCode(), req.getAccountDate());
	}

	@GetMapping("/getPersonalInfoDropDown/{branchCode}/{customerType}/{pid}")
	public GetCommonDropDownRes getPersonalInfoDropDown(@PathVariable("branchCode") String branchCode,
			@PathVariable("customerType") String customerType, @PathVariable("pid") String pid)
			throws CommonValidationException {
		return dropDownservice.getPersonalInfoDropDown(branchCode, customerType, pid);
	}

	@GetMapping("/getBankMasterDropDown/{branchCode}")
	public GetCommonDropDownRes getBankMasterDropDown(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getBankMasterDropDown(branchCode);
	}

	@GetMapping("/getConstantDropDown/{categoryId}")
	public GetCommonDropDownRes getConstantDropDown(@PathVariable("categoryId") String categoryId)
			throws CommonValidationException {
		return dropDownservice.getConstantDropDown(categoryId);
	}

	@GetMapping("/getDisableStatus/{contractNo}/{layerNo}")
	public GetCommonValueRes getDisableStatus(@PathVariable("contractNo") String contractNo,
			@PathVariable("layerNo") String layerNo) throws CommonValidationException {
		return dropDownservice.getDisableStatus(contractNo, layerNo);
	}

	@GetMapping("/getDisableStatus1/{contractNo}/{layerNo}")
	public GetCommonValueRes getDisableStatus1(@PathVariable("contractNo") String contractNo,
			@PathVariable("layerNo") String layerNo) throws CommonValidationException {
		return dropDownservice.getDisableStatus1(contractNo, layerNo);
	}

//	@GetMapping("/EditModeStatus/{proposalNo}/{layerNo}")
//	public GetCommonValueRes editModeStatus(@PathVariable ("proposalNo") String proposalNo, @PathVariable ("layerNo") String layerNo) throws CommonValidationException {
//				return dropDownservice.editModeStatus(proposalNo, layerNo);
//		}
	@PostMapping("/riskDetailsEndorsement")
	public CommonResponse riskDetailsEndorsement(@RequestBody endorsementModeReq req) throws CommonValidationException {
		return dropDownservice.riskDetailsEndorsement(req.getProposalNo(), req.getEndtstatus(),req.getBranchCode());
	}

	@GetMapping("/updateSubClass/{proposalNo}/{type}")
	public CommonResponse updateSubClass(@PathVariable("proposalNo") String proposalNo,
			@PathVariable("type") String type) throws CommonValidationException {
		return dropDownservice.updateSubClass(proposalNo, type);
	}

	@GetMapping("/updateRenewalEditMode")
	public CommonResponse updateRenewalEditMode(@RequestBody updateSubEditModeReq req)
			throws CommonValidationException {
		return dropDownservice.updateRenewalEditMode(req);
	}

	@GetMapping("/updateEditMode")
	public CommonResponse updateEditMode(@RequestBody updateSubEditModeReq req) throws CommonValidationException {
		return dropDownservice.updateEditMode(req);
	}

	@PostMapping("/updateSubEditMode")
	public CommonResponse updateSubEditMode(@RequestBody updateSubEditModeReq req) throws CommonValidationException {
		return dropDownservice.updateSubEditMode(req);
	}

	@GetMapping("/getBaseProposal/{proposalNo}")
	public GetCommonValueRes getBaseProposal(@PathVariable("proposalNo") String proposalNo)
			throws CommonValidationException {
		return dropDownservice.getBaseProposal(proposalNo);
	}

	@GetMapping("/getEndosTypelist")
	public GetCommonDropDownRes getEndosTypelist() throws CommonValidationException {
		return dropDownservice.getConstantDropDown("37");
	}

	@GetMapping("/getCurrencyShortList/{branchCode}/{countryId}")
	public GetCommonDropDownRes getCurrencyShortList(@PathVariable("branchCode") String branchCode,
			@PathVariable("countryId") String countryId) throws CommonValidationException {
		return dropDownservice.getCurrencyShortList(branchCode, countryId);
	}

	@GetMapping("/getCountryDropDown/{branchCode}")
	public GetCommonDropDownRes getCountryDropDown(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getCountryDropDown(branchCode);

	}

	@GetMapping("/getTypeList/{type}")
	public GetCommonDropDownRes getTypeList(@PathVariable("type") String type) throws CommonValidationException {
		return dropDownservice.getTypeList(type);
	}

	@PostMapping("/getReinstatementOptionList")
	public GetCommonDropDownRes getReinstatementOptionList() throws CommonValidationException {

		return dropDownservice.getReinstatementOptionList();

	}

	@GetMapping("/getTerritoryRegionList/{branchCode}")
	public GetCommonDropDownRes getTerritoryRegionList(@PathVariable("branchCode") String branchcode)
			throws CommonValidationException {
		return dropDownservice.getTerritoryRegionList(branchcode);
	}

	@GetMapping("/getDocType/{branchCode}/{productId}/{moduleType}")
	public GetCommonDropDownRes getDocType(@PathVariable("branchCode") String branchCode,
			@PathVariable("productId") String productId, @PathVariable("moduleType") String moduleType)
			throws CommonValidationException {

		return dropDownservice.getDocType(branchCode, productId, moduleType);

	}

	@GetMapping("/Proposallist")
	public GetCommonDropDownRes Proposallist() throws CommonValidationException {
		return dropDownservice.getConstantDropDown("1");
	}

	@GetMapping("/getPolicyBranchDropDown/{branchCode}")
	public GetCommonDropDownRes getPolicyBranchDropDown(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getPolicyBranchDropDown(branchCode);
	}

	@PostMapping("/getDepartmentDropDown")
	public GetCommonDropDownRes getDepartmentDropDown(@RequestBody GetDepartmentDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getDepartmentDropDownVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getDepartmentDropDown(req);

	}

	@GetMapping("/getSubProfitCentreDropDown/{deptid}/{branchCode}/{productCode}")
	public GetCommonDropDownRes getSubProfitCentreDropDown(@PathVariable("deptid") String deptid,
			@PathVariable("branchCode") String branchCode, @PathVariable("productCode") String productCode)
			throws CommonValidationException {

		return dropDownservice.getSubProfitCentreDropDown(deptid, branchCode, productCode);
	}

	@GetMapping("/getUnderwriterCountryList/{leaderUnderriter}/{branchCode}")
	public GetCommonDropDownRes getUnderwriterCountryList(@PathVariable("leaderUnderriter") String leaderUnderriter,
			@PathVariable("branchCode") String branchCode) throws CommonValidationException {

		return dropDownservice.getUnderwriterCountryList(leaderUnderriter, branchCode);

	}

	@GetMapping("/getUnderWriterLimit/{uwName}/{processId}/{productId}/{deptId}")
	public GetCommonValueRes getUnderWriterLimmit(@PathVariable("uwName") String uwName,
			@PathVariable("processId") String processId, @PathVariable("productId") String pid,
			@PathVariable("deptId") String deptId) throws CommonValidationException {

		return dropDownservice.getUnderWriterLimmit(uwName, processId, pid, deptId);

	}

	@GetMapping("/getProfitCentreDropDown/{branchCode}")
	public GetCommonDropDownRes getProfitCentreDropDown(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getProfitCentreDropDown(branchCode);
	}

	@GetMapping("/getCeaseaccountStatus/{ContractNo}")
	public GetCommonValueRes getCeaseaccountStatus(@PathVariable("ContractNo") String ContractNo)
			throws CommonValidationException {
		return dropDownservice.getCeaseaccountStatus(ContractNo);
	}

	@GetMapping("/RenewalDropDown/{branchCode}/{productCode}/{status}")
	public GetCommonDropDownRes RenewalDropDown(@PathVariable("branchCode") String branchCode,
			@PathVariable("productCode") String productCode, @PathVariable("status") String status)
			throws CommonValidationException {
		return dropDownservice.RenewalDropDown(branchCode, productCode, status);
	}

	@GetMapping("/getTerritoryDropDown/{branchCode}")
	public GetCommonDropDownRes getTerritoryDropDown(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getTerritoryDropDown(branchCode);

	}

	@GetMapping("/getCrestaIDList/{branchCode}/{crestaValue}")
	public GetCommonDropDownRes getCrestaIDList(@PathVariable("branchCode") String branchCode,
			@PathVariable("crestaValue") String crestaValue) throws CommonValidationException {

		return dropDownservice.getCrestaIDList(branchCode, crestaValue);

	}

	@GetMapping("/getCrestaNameList/{branchCode}/{crestaValue}")
	public GetCommonDropDownRes getCrestaNameList(@PathVariable("branchCode") String branchCode,
			@PathVariable("crestaValue") String crestaValue) throws CommonValidationException {

		return dropDownservice.getCrestaNameList(branchCode, crestaValue);

	}

	@GetMapping("/getUnderWritterDropDown/{branchCode}/{attachedUW}")
	public GetCommonDropDownRes getUnderWritterDropDown(@PathVariable("branchCode") String branchCode,
			@PathVariable("attachedUW") String attachedUW) throws CommonValidationException {

		return dropDownservice.getUnderWritterDropDown(branchCode, attachedUW);

	}

	@GetMapping("/getCrestaName/{branchCode}/{crestaValue}")
	public GetCommonValueRes getCrestaName(@PathVariable("branchCode") String branchCode,
			@PathVariable("crestaValue") String crestaValue) throws CommonValidationException {

		return dropDownservice.getCrestaName(branchCode, crestaValue);

	}

	@PostMapping("/getCopyQuote")
	public GetCommonValueRes getCopyQuote(@RequestBody GetCopyQuoteReq req) throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getCopyQuoteVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getCopyQuote(req);
	}

	@GetMapping("/getAllocationDisableStatus/{contractNo}/{layerNo}")
	public GetCommonValueRes getAllocationDisableStatus(@PathVariable("contractNo") String contractNo,
			@PathVariable("layerNo") String layerNo) throws CommonValidationException {

		return dropDownservice.getAllocationDisableStatus(contractNo, layerNo);

	}

	@PostMapping("/updatepositionMasterEndtStatus")
	public CommonResponse updatepositionMasterEndtStatus(@RequestBody UpdatepositionMasterEndtStatusReq req)
			throws CommonValidationException {
		return dropDownservice.updatepositionMasterEndtStatus(req.getProposalNo(), req.getEndtDate(),
				req.getCeaseStatus());
	}

	@PostMapping("/getYearListValue")
	public GetCommonDropDownRes getYearListValue(@RequestBody GetYearListValueReq req)
			throws CommonValidationException {

		return dropDownservice.getYearListValue(req.getInceptionDate());

	}

	@GetMapping("/UpdateInstallmentTransaction/{proposalNo}")
	public CommonResponse updateInstallmentTransaction(@PathVariable("proposalNo") String proposalNo)
			throws CommonValidationException {
		return dropDownservice.updateInstallmentTransaction(proposalNo);
	}

	@GetMapping("/getBonusList")
	public GetCommonDropDownRes getBonusList() throws CommonValidationException {

		return dropDownservice.getBonusList();

	}

	@GetMapping("/getConstantDropDownET/{categoryId}/{contractNo}")
	public GetCommonDropDownRes getConstantDropDownET(@PathVariable("categoryId") String categoryId,
			@PathVariable("contractNo") String contractNo) throws CommonValidationException {

		return dropDownservice.getConstantDropDownET(categoryId, contractNo);

	}

	@GetMapping("/getCoverDEpartmentDropDown/{branchCode}/{pid}/{departId}")
	public GetCommonDropDownRes getCoverDEpartmentDropDown(@PathVariable("branchCode") String branchCode,
			@PathVariable("pid") String pid, @PathVariable("departId") String departId)
			throws CommonValidationException {

		return dropDownservice.getCoverDEpartmentDropDown(branchCode, pid, departId);

	}

	@PostMapping("/getProductieModuleDropDown")
	public GetCommonDropDownRes getProductieModuleDropDown(@RequestBody GetProductModuleDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getProductieModuleDropDown(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getProductieModuleDropDown(req);
	}
	@GetMapping("/getProductDropDown/{branchCode}")
	public GetCommonDropDownRes getProductDropDown(@PathVariable("branchCode") String branchCode)throws CommonValidationException {
		return dropDownservice.getProductDropDown(branchCode);
	}

	@PostMapping("/getInwardBusinessTypeDropDown")
	public GetCommonDropDownRes getInwardBusinessTypeDropDown(@RequestBody GetInwardBusinessTypeDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getInwardBusinessTypeDropDown(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getInwardBusinessTypeDropDown(req);
	}

	@PostMapping("/getTreatyTypeDropDown")
	public GetCommonDropDownRes getTreatyTypeDropDown(@RequestBody GetTreatyTypeDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getTreatyTypeDropDownVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getTreatyTypeDropDown(req);
	}

	@PostMapping("/getProfitCentreieModuleDropDown")
	public GetCommonDropDownRes getProfitCentreieModuleDropDown(@RequestBody GetProfitCentreieModuleDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getProfitCentreieModuleDropDown(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getProfitCentreieModuleDropDown(req);
	}

	@PostMapping("/getDepartmentieModuleDropDown")
	public GetCommonDropDownRes getDepartmentieModuleDropDown(@RequestBody GetDepartmentieModuleDropDownReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getDepartmentieModuleDropDown(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getDepartmentieModuleDropDown(req);
	}

	@GetMapping("/getProposalStatus/{excludeProposalNo}")
	public GetCommonDropDownRes getProposalStatus(@PathVariable("excludeProposalNo") String excludeProposalNo)
			throws CommonValidationException {

		return dropDownservice.getProposalStatus(excludeProposalNo);

	}

	@GetMapping("/getExcludeProposalNo/{branchCode}/{proposalNo}/{transactionNo}")
	public GetCommonValueRes getExcludeProposalNo(@PathVariable("branchCode") String branchCode,
			@PathVariable("proposalNo") String proposalNo, @PathVariable("transactionNo") String transactionNo)
			throws CommonValidationException {
		return dropDownservice.getExcludeProposalNo(branchCode, proposalNo, transactionNo);
	}

	@GetMapping("/getConstantDropDownBusinessType/{categoryId}/{deptId}")
	public GetCommonDropDownRes getConstantDropDownBusinessType(@PathVariable("categoryId") String categoryId,
			@PathVariable("deptId") String deptId) throws CommonValidationException {

		return dropDownservice.getConstantDropDownBusinessType(categoryId, deptId);

	}

	@GetMapping("/getConstantDropDownBasis/{categoryId}/{deptId}")
	public GetCommonDropDownRes getConstantDropDownBasis(@PathVariable("categoryId") String categoryId,
			@PathVariable("deptId") String deptId) throws CommonValidationException {

		return dropDownservice.getConstantDropDownBasis(categoryId, deptId);

	}

	@PostMapping("/DuplicateCountCheck")
	public int DuplicateCountCheck(@RequestBody DuplicateCountCheckReq req) throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.DuplicateCountCheck(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.DuplicateCountCheck(req);
	}

	@GetMapping("/getCountryDate/{branchCode}/{DEC_FILE}")
	public GetCommonDropDownRes getCountryDate(@PathVariable("branchCode") String branchCode,
			@PathVariable("DEC_FILE") String DEC_FILE) throws CommonValidationException {

		return dropDownservice.getCountryDate(branchCode, DEC_FILE);

	}

	@PostMapping("/GetExchangeRate")
	public GetCommonValueRes GetExchangeRate(@RequestBody GetExchangeRateReq req) throws CommonValidationException {
		/*
		 * List<ErrorCheck> error =
		 * dropDownVali.GetExchangeRateVali(req.getIncepDate(),req.getAccDate(),req.
		 * getDate()); if (error != null && error.size() > 0) { throw new
		 * CommonValidationException("error", error); }
		 */

		return dropDownservice.GetExchangeRate(req.getCurrency(), req.getDate(), req.getCountryid(),
				req.getBranchCode());

	}

	@PostMapping("/getContractValidation")
	public GetContractValRes getContractValidation(@RequestBody ContractReq req) throws CommonValidationException {

		return dropDownservice.getContractValidation(req);

	}

	@GetMapping("/updatepositionMasterEndtStatus/{endtDate}/{ceaseStatus}")
	public CommonResponse updatepositionMasterEndtStatus(@PathVariable("endtDate") String endtDate,
			@PathVariable("ceaseStatus") String ceaseStatus, @PathVariable("proposalNo") String proposalNo)
			throws CommonValidationException {

		return dropDownservice.updatepositionMasterEndtStatus(endtDate, ceaseStatus, proposalNo);

	}

	@GetMapping("/getDeptName/{branchCode}")
	public GetCommonDropDownRes getDeptName(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getDeptName(branchCode);
	}

	@PostMapping("/getYearToListValue")
	public GetCommonDropDownRes getYearToListValue(@RequestBody GetYearToListValueReq req)
			throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getYearToListValueVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getYearToListValue(req);
	}

	@GetMapping("/updateBaseLayer/{baseLayer}/{proposalNo}")
	public CommonResponse updateBaseLayer(@PathVariable("baseLayer") String baseLayer,
			@PathVariable("proposalNo") String proposalNo) throws CommonValidationException {
		return dropDownservice.updateBaseLayer(baseLayer, proposalNo);
	}

	@GetMapping("/getPaymentPartnerlist/{branchCode}/{cedingId}/{brokerId}")
	public GetCommonDropDownRes getPaymentPartnerlist(@PathVariable("branchCode") String branchCode,
			@PathVariable("cedingId") String cedingId, @PathVariable("brokerId") String brokerId)
			throws CommonValidationException {
		return dropDownservice.getPaymentPartnerlist(branchCode, cedingId, brokerId);
	}

	@GetMapping("/updateProposalno/{referenceNo}/{proposalNo}")
	public CommonResponse updateProposalno(@PathVariable("referenceNo") String referenceNo,
			@PathVariable("proposalNo") String proposalNo) throws CommonValidationException {
		return dropDownservice.updateProposalno(referenceNo, proposalNo);
	} 

	@GetMapping("/getBouquetList/{branchCode}")
	public GetBouquetListRes getBouquetList(@PathVariable("branchCode") String branchCode)
			throws CommonValidationException {
		return dropDownservice.getBouquetList(branchCode);
	}
	@GetMapping("/getBouquetExistingList/{branchCode}/{bouquetNo}/{bouquetYN}")
	public GetBouquetExistingListRes getBouquetExistingList(@PathVariable("branchCode") String branchCode,@PathVariable("bouquetNo") String bouquetNo, @PathVariable("bouquetYN") String bouquetYN)
			throws CommonValidationException {
		return dropDownservice.getBouquetExistingList(branchCode, bouquetNo, bouquetYN);
	} 
	@GetMapping("/getStatusDropDown/{branchCode}")
	public GetCommonDropDownRes getStatusDropDown(@PathVariable("branchCode") String branchCode)throws CommonValidationException {
		return dropDownservice.getStatusDropDown(branchCode);
	} 
	@GetMapping("/getSubStatusDropDown/{branchCode}/{statusCode}")
	public GetCommonDropDownRes getSubStatusDropDown(@PathVariable("branchCode") String branchCode,@PathVariable("statusCode") String statusCode)throws CommonValidationException {
		return dropDownservice.getSubStatusDropDown(branchCode,statusCode);
	} 
	@PostMapping("/updateBqEditMode")
	public CommonResponse updateBqEditMode(@RequestBody updateBqEditModeReq req)throws CommonValidationException {
		return dropDownservice.updateBqEditMode(req);
	} 
	@PostMapping("/getPlacedProposalList")
	public GetCommonDropDownRes getPlacedProposalList(@RequestBody GetPlacedProposalListReq req)throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getPlacedProposalListVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getPlacedProposalList(req);
	} 
	@PostMapping("/getNotPlacedProposalList")
	public GetNotPlacedProposalListRes getNotPlacedProposalList(@RequestBody GetPlacedProposalListReq req)throws CommonValidationException {
		List<ErrorCheck> error = dropDownVali.getPlacedProposalListVali(req);
		if (error != null && error.size() > 0) {
			throw new CommonValidationException("error", error);
		}
		return dropDownservice.getNotPlacedProposalList(req);
	} 
	@GetMapping("/getBaseLayerExistingList/{branchCode}/{baseProposalNo}")
	public GetBouquetExistingListRes getBaseLayerExistingList(@PathVariable("branchCode") String branchCode,@PathVariable("baseProposalNo") String baseProposalNo)throws CommonValidationException {
		return dropDownservice.getBaseLayerExistingList(branchCode,baseProposalNo);
	}  
	@GetMapping("/getBouquetCedentBrokerInfo/{bouquetNo}")
	public GetBouquetCedentBrokerInfoRes getBouquetCedentBrokerInfo(@PathVariable("bouquetNo") String bouquetNo)throws CommonValidationException {
		return dropDownservice.getBouquetCedentBrokerInfo(bouquetNo);
	} 
	@GetMapping("/getBouquetCedentBrokercheck/{bouquetNo}/{cedingCo}/{broker}")
	public CommonSaveRes getBouquetCedentBrokercheck(@PathVariable("bouquetNo") String bouquetNo,@PathVariable("cedingCo") String cedingCo, @PathVariable("broker") String broker)throws CommonValidationException {
		return dropDownservice.getBouquetCedentBrokercheck(bouquetNo,cedingCo,broker);
	}  
	@GetMapping("/gePltDisableStatus/{proposalNo}")
	public CommonSaveRes gePltDisableStatus(@PathVariable("proposalNo") String proposalNo)throws CommonValidationException {
		return dropDownservice.gePltDisableStatus(proposalNo);
	} 
	@GetMapping("/getUWFromTocheck/{bouquetNo}/{uwYear}/{uwYearTo}")
	public CommonSaveRes getUWFromTocheck(@PathVariable("bouquetNo") String bouquetNo,@PathVariable("uwYear") String uwYear, @PathVariable("uwYearTo") String uwYearTo)throws CommonValidationException {
		return dropDownservice.getUWFromTocheck(bouquetNo,uwYear,uwYearTo);
	}   
	@GetMapping("/getNewContractInfo/{branchCode}/{proposalNo}")
	public GetNewContractInfoRes getNewContractInfo(@PathVariable("branchCode") String branchCode, @PathVariable("proposalNo") String proposalNo)throws CommonValidationException {
		return dropDownservice.getNewContractInfo(branchCode,proposalNo);
	} 
	@GetMapping("/getPlacementInfoList/{branchCode}/{layerProposalNo}")
	public GetPlacementInfoListRes getPlacementInfoList(@PathVariable("branchCode") String branchCode, @PathVariable("layerProposalNo") String layerProposalNo)throws CommonValidationException {
		return dropDownservice.getPlacementInfoList(branchCode,layerProposalNo);
	} 
	@GetMapping("/getCompanyInfo/{branchCode}")
	public GetCompanyInfoRes getCompanyInfo(@PathVariable("branchCode") String branchCode)throws CommonValidationException {
		return dropDownservice.getCompanyInfo(branchCode);
	}
	@PostMapping("/Savehtmltopdf")
	public CommonResponse Savehtmltopdf(@RequestBody SavehtmltoPdfReq req)throws CommonValidationException {
		return dropDownservice.Savehtmltopdf(req);
	} 
	
}
