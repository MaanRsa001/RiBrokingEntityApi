package com.maan.insurance.service.Dropdown;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.DropDown.DuplicateCountCheckReq;
import com.maan.insurance.model.req.DropDown.GetClaimDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetContractLayerNoReq;
import com.maan.insurance.model.req.DropDown.GetCopyQuoteReq;
import com.maan.insurance.model.req.DropDown.GetCurrencyIdReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentieModuleDropDownReq;
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
import com.maan.insurance.model.req.DropDown.GetYearToListValueReq;
import com.maan.insurance.model.req.DropDown.SavehtmltoPdfReq;
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
import com.maan.insurance.model.res.DropDown.GetSubStatusDropDownRes;
import com.maan.insurance.model.res.DropDown.GetYearToListValueRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;

@Service
public interface DropDownService {

	GetCommonValueRes EditModeStatus(String proposalNo);

	GetCommonValueRes getCoverLimitAmount(String proposalNo, String departmentId, String productId);

	GetOpenPeriodRes getOpenPeriod(String branchCode);

	GetCommonDropDownRes getCurrencyMasterDropDown(String branchCode, String countryId);

	GetCommonValueRes getDepartmentName(String branchCode, String productCode, String deptCode);

	GetCommonDropDownRes getClaimDepartmentDropDown(GetClaimDepartmentDropDownReq req);

	GetCommonDropDownRes getSubProfitCentreMulti(GetSubProfitCentreMultiReq req);

	GetCommonValueRes getCurrencyId(GetCurrencyIdReq req);

	GetCommonValueRes getProposalNo(GetProposalNoReq req);

	GetCommonDropDownRes getSectionList(GetSectionListReq req);

	GetCommonValueRes getContractLayerNo(GetContractLayerNoReq req);

	GetCommonDropDownRes getPreDepartmentDropDown(GetPreDepartmentDropDownReq req);

	GetCommonDropDownRes getSubProfitCentreMultiDropDown(GetSubProfitCentreMultiDropDownReq req);

	GetCommonValueRes getCashLossCount(String contractNo, String departmentId);

	int Validatethree(String branchCode, String accountDate);

	GetCommonDropDownRes getPersonalInfoDropDown(String branchCode, String customerType, String pid);

	GetCommonDropDownRes getBankMasterDropDown(String branchCode);

	GetCommonDropDownRes getConstantDropDown(String categoryId);

	GetCommonValueRes getDisableStatus(String contractNo, String layerNo);

	GetCommonValueRes getDisableStatus1(String contractNo, String layerNo);

	CommonResponse riskDetailsEndorsement(String proposalNo, String endtStatus, String branchCode);

	CommonResponse updateSubClass(String proposalNo, String type);

	//CommonResponse updateRenewalEditMode(String proposalNo, String status, String updateProposalNo);

	//CommonResponse updateEditMode(String proposalNo, String status, String updateProposalNo);

	CommonResponse updateSubEditMode(String proposalNo, String status, String updateProposalNo);
	//GetCommonValueRes editModeStatus(String proposalNo, String layerNo);

	GetCommonValueRes getBaseProposal(String proposalNo);

	GetCommonDropDownRes getCurrencyShortList(String branchCode, String countryId);

	GetCommonDropDownRes getCountryDropDown(String branchCode);

	GetCommonDropDownRes getTypeList(String branchCode);

	GetCommonDropDownRes getReinstatementOptionList();

	GetCommonDropDownRes getTerritoryRegionList(String branchCode);


	GetCommonDropDownRes getDocType(String branchCode, String productId, String moduleType);

	GetCommonDropDownRes getPolicyBranchDropDown(String branchCode);

	GetCommonDropDownRes getDepartmentDropDown(GetDepartmentDropDownReq req);

	GetCommonValueRes getCeaseaccountStatus(String ContractNo);

	GetCommonDropDownRes getSubProfitCentreDropDown(String deptid, String branchCode, String productCode);

	GetCommonDropDownRes getUnderwriterCountryList(String leaderUnderriter, String branchCode);

	GetCommonDropDownRes getProfitCentreDropDown(String branchCode);

	GetCommonDropDownRes RenewalDropDown(String branchCode, String productCode, String status);

	GetCommonDropDownRes getTerritoryDropDown(String branchCode);

	GetCommonDropDownRes getCrestaIDList(String branchCode, String crestaValue);

	GetCommonDropDownRes getCrestaNameList(String branchCode, String crestaValue);

	GetCommonDropDownRes getUnderWritterDropDown(String branchCode, String attachedUW);

	GetCommonValueRes getCrestaName(String branchCode, String crestaValue);


	GetCommonValueRes getCopyQuote(GetCopyQuoteReq req);

	GetCommonValueRes getAllocationDisableStatus(String contractNo, String layerNo);

	CommonResponse updatepositionMasterEndtStatus(String proposalNo, String endtDate,
			String ceaseStatus);

	GetCommonDropDownRes getYearListValue(String inceptionDate);

	CommonResponse updateInstallmentTransaction(String proposalNo);

	GetCommonDropDownRes getBonusList();

	GetCommonDropDownRes getConstantDropDownET(String categoryId, String contractNo);

	GetCommonDropDownRes getCoverDEpartmentDropDown(String branchCode, String pid, String departId);

	GetCommonDropDownRes getProductieModuleDropDown(GetProductModuleDropDownReq req);


	GetCommonValueRes getUnderWriterLimmit(String uwName, String processId, String ProductId, String deptId);

	GetCommonDropDownRes getInwardBusinessTypeDropDown(GetInwardBusinessTypeDropDownReq req);

	GetCommonDropDownRes getTreatyTypeDropDown(GetTreatyTypeDropDownReq req);

	GetCommonDropDownRes getProfitCentreieModuleDropDown(GetProfitCentreieModuleDropDownReq req);

	GetCommonDropDownRes getDepartmentieModuleDropDown(GetDepartmentieModuleDropDownReq req);

	GetCommonDropDownRes getProposalStatus(String excludeProposalNo);

	GetCommonValueRes getExcludeProposalNo(String branchCode, String proposalNo, String transactionNo);

	GetCommonDropDownRes getConstantDropDownBusinessType(String categoryId, String deptId);

	GetCommonDropDownRes getConstantDropDownBasis(String categoryId, String deptId);

	int DuplicateCountCheck(DuplicateCountCheckReq req);

	GetCommonDropDownRes getCountryDate(String branchCode, String DEC_FILE);

	GetCommonValueRes GetExchangeRate(String currency, String date, String countryid, String branchCode);

	GetContractValRes getContractValidation(ContractReq req);

	GetBouquetExistingListRes getBouquetExistingList(String branchCode, String bouquetNo, String bouquetYN);

	GetCommonDropDownRes getStatusDropDown(String branchCode);

	GetSubStatusDropDownRes getSubStatusDropDown(String branchCode, String statusCode);

	CommonResponse updateBqEditMode(updateBqEditModeReq req);

	GetCommonDropDownRes getPlacedProposalList(GetPlacedProposalListReq req);

	GetNotPlacedProposalListRes getNotPlacedProposalList(GetPlacedProposalListReq req);

	CommonResponse updateRenewalEditMode(updateSubEditModeReq req);

	CommonResponse updateEditMode(updateSubEditModeReq req);

	CommonResponse updateSubEditMode(updateSubEditModeReq req);

	GetCommonDropDownRes getDeptName(String branchCode);

	GetCommonDropDownRes getYearToListValue(GetYearToListValueReq req);

	CommonResponse updateBaseLayer(String baseLayer, String proposalNo);

	GetCommonDropDownRes getPaymentPartnerlist(String branchCode, String cedingId, String brokerId);

	CommonResponse updateProposalno(String referenceNo, String proposalNo);

	GetBouquetListRes getBouquetList(String branchCode);

	GetBouquetExistingListRes getBaseLayerExistingList(String branchCode, String baseProposalNo);

	GetBouquetCedentBrokerInfoRes getBouquetCedentBrokerInfo(String bouquetNo);

	CommonSaveRes getBouquetCedentBrokercheck(String bouquetNo, String cedingCo, String broker);

	CommonSaveRes gePltDisableStatus(String proposalNo);

	CommonSaveRes getUWFromTocheck(String bouquetNo, String uwYear, String uwYearTo);

	GetNewContractInfoRes getNewContractInfo(String branchCode, String proposalNo);

	GetPlacementInfoListRes getPlacementInfoList(String branchCode, String layerProposalNo);

	GetCompanyInfoRes getCompanyInfo(String branchCode);

	GetCommonDropDownRes getProductDropDown(String branchCode);

	CommonResponse Savehtmltopdf(SavehtmltoPdfReq req);
}
