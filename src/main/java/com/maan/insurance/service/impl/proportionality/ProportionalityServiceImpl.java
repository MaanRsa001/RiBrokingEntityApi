package com.maan.insurance.service.impl.proportionality;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TmasPolicyBranch;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCedentRet;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TtrnBonusRepository;
import com.maan.insurance.model.repository.TtrnCedentRetRepository;
import com.maan.insurance.model.repository.TtrnCommissionDetailsRepository;
import com.maan.insurance.model.repository.TtrnCrestazoneDetailsRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnMndInstallmentsRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;
import com.maan.insurance.model.repository.TtrnRiskRemarksRepository;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.proportionality.BaseLayerStatusReq;
import com.maan.insurance.model.req.proportionality.BonusSaveReq;
import com.maan.insurance.model.req.proportionality.CedentRetentReq;
import com.maan.insurance.model.req.proportionality.CedentSaveReq;
import com.maan.insurance.model.req.proportionality.ConvertPolicyReq;
import com.maan.insurance.model.req.proportionality.ConvertPolicyReq1;
import com.maan.insurance.model.req.proportionality.CrestaSaveReq;
import com.maan.insurance.model.req.proportionality.FirstpageSaveReq;
import com.maan.insurance.model.req.proportionality.GetBonusListCountReq;
import com.maan.insurance.model.req.proportionality.GetCrestaCountReq;
import com.maan.insurance.model.req.proportionality.GetCrestaDetailListReq;
import com.maan.insurance.model.req.proportionality.GetRetentionDetailsReq;
import com.maan.insurance.model.req.proportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.proportionality.GetSectionDuplicationCheckReq;
import com.maan.insurance.model.req.proportionality.GetcalculateSCReq;
import com.maan.insurance.model.req.proportionality.GetprofitCommissionEnableReq;
import com.maan.insurance.model.req.proportionality.InsertCrestaDetailsReq;
import com.maan.insurance.model.req.proportionality.InsertSectionValueReq;
import com.maan.insurance.model.req.proportionality.InsertSlidingScaleMentodInfoReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionListReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionSaveReq;
import com.maan.insurance.model.req.proportionality.RemarksReq;
import com.maan.insurance.model.req.proportionality.RemarksSaveReq;
import com.maan.insurance.model.req.proportionality.RetroDetailReq;
import com.maan.insurance.model.req.proportionality.RetroSaveReq;
import com.maan.insurance.model.req.proportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.proportionality.ScaleCommissionInsertReq;
import com.maan.insurance.model.req.proportionality.ScaleList;
import com.maan.insurance.model.req.proportionality.SecondpageSaveReq;
import com.maan.insurance.model.req.proportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.proportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.proportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.proportionality.saveRiskDeatilsSecondFormReq;
import com.maan.insurance.model.req.proportionality.showSecondPageData1Req;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;
import com.maan.insurance.model.res.proportionality.CancelProposalRes;
import com.maan.insurance.model.res.proportionality.CheckProductMatchRes;
import com.maan.insurance.model.res.proportionality.CommonSaveRes;
import com.maan.insurance.model.res.proportionality.ConvertPolicyRes;
import com.maan.insurance.model.res.proportionality.ConvertPolicyRes1;
import com.maan.insurance.model.res.proportionality.FirstpagesaveRes;
import com.maan.insurance.model.res.proportionality.FirstpageupdateRes;
import com.maan.insurance.model.res.proportionality.GetCommonValueRes;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes1;
import com.maan.insurance.model.res.proportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetDetailsRes1;
import com.maan.insurance.model.res.proportionality.GetRetentionDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetentionDetailsRes1;
import com.maan.insurance.model.res.proportionality.GetScaleCommissionListRes;
import com.maan.insurance.model.res.proportionality.GetScaleCommissionListRes1;
import com.maan.insurance.model.res.proportionality.GetSectionEditModeRes;
import com.maan.insurance.model.res.proportionality.GetSectionEditModeRes1;
import com.maan.insurance.model.res.proportionality.GetSlidingScaleMethodInfoRes;
import com.maan.insurance.model.res.proportionality.GetSlidingScaleMethodInfoRes1;
import com.maan.insurance.model.res.proportionality.GetcalculateSCRes;
import com.maan.insurance.model.res.proportionality.GetcalculateSCRes1;
import com.maan.insurance.model.res.proportionality.GetprofitCommissionEnableRes;
import com.maan.insurance.model.res.proportionality.InsertCrestaDetailsRes;
import com.maan.insurance.model.res.proportionality.InstalmentListRes;
import com.maan.insurance.model.res.proportionality.MappingProposalRes;
import com.maan.insurance.model.res.proportionality.NoInsurerRes;
import com.maan.insurance.model.res.proportionality.ProfitCommissionListRes;
import com.maan.insurance.model.res.proportionality.ProfitCommissionListRes1;
import com.maan.insurance.model.res.proportionality.RetListRes;
import com.maan.insurance.model.res.proportionality.RetroFinalListres;
import com.maan.insurance.model.res.proportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.proportionality.RiskDetailsEditModeRes1;
import com.maan.insurance.model.res.proportionality.ScaleCommissionInsertRes;
import com.maan.insurance.model.res.proportionality.SecondpagesaveRes;
import com.maan.insurance.model.res.proportionality.SecondpagesaveResp;
import com.maan.insurance.model.res.proportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.proportionality.ShowLayerBrokerageRes1;
import com.maan.insurance.model.res.proportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.proportionality.ShowRetroContractsRes1;
import com.maan.insurance.model.res.proportionality.ShowSecondPageData1Res1;
import com.maan.insurance.model.res.proportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.proportionality.ShowSecondPageDataRes1;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes1;
import com.maan.insurance.model.res.proportionality.UpdateOfferNoReq;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes1;
import com.maan.insurance.model.res.proportionality.checkAvialabilityRes;
import com.maan.insurance.model.res.proportionality.getprofitCommissionDeleteRes;
import com.maan.insurance.model.res.proportionality.getprofitCommissionEditRes;
import com.maan.insurance.model.res.proportionality.getprofitCommissionEditRes1;
import com.maan.insurance.model.res.proportionality.saveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.proportionality.saveRiskDeatilsSecondFormRes1;
import com.maan.insurance.model.res.proportionality.showSecondPageData1Res;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.placement.PlacementServiceImple;
import com.maan.insurance.service.proportionality.ProportionalityService;
import com.maan.insurance.validation.Formatters;

@Service
public class ProportionalityServiceImpl implements ProportionalityService {
	private Logger logger = LogManager.getLogger(ProportionalityServiceImpl.class);

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private PlacementServiceImple placeImple;
	

	@Autowired
	private ProportionalityCustomRepository proportionalityCustomRepository;
	@Autowired
	private TtrnRiskDetailsRepository ttrnRiskDetailsRepository;
	@Autowired
	private TtrnRiskProposalRepository ttrnRiskProposalRepository;
	@Autowired
	private PositionMasterRepository positionMasterRepository;
	@Autowired
	private TtrnRiskRemarksRepository ttrnRiskRemarksRepository;
	@Autowired
	private TtrnCedentRetRepository ttrnCedentRetRepository;
	@Autowired
	private TtrnRiskCommissionRepository ttrnRiskCommissionRepository;
	@Autowired
	private  TtrnInsurerDetailsRepository ttrnInsurerDetailsRepository;
	@Autowired
	private  TtrnCrestazoneDetailsRepository ttrnCrestazoneDetailsRepository;
	@Autowired
	private  TtrnBonusRepository ttrnBonusRepository;
	@Autowired
	private  TtrnCommissionDetailsRepository ttrnCommissionDetailsRepository;
	@Autowired
	private  TtrnMndInstallmentsRepository ttrnMndInstallmentsRepository;

	@Override
	public FirstpagesaveRes insertProportionalTreaty(FirstpageSaveReq req, boolean saveFlag, final boolean amendId) {
		FirstpagesaveRes res=new FirstpagesaveRes();
		boolean savFlg = false,ChkSavFlg = false;
		try {
			String[] args=null;
			if (saveFlag) {
				ChkSavFlg = checkEditSaveModeMethod(req);
				if (ChkSavFlg){
					args = getFirstPageEditSaveModeAruguments(req,getMaxAmednId(req.getProposalNo()));
					
					//risk.update.rskDtls
					TtrnRiskDetails	 update = proportionalityCustomRepository.ttrnRiskDetailsUpdate(args);
					if(update!=null) {
					ttrnRiskDetailsRepository.saveAndFlush(update);	
					}
					args[1]=(Integer.parseInt((String)args[51])+1)+"";
				} else {
					args = getFirstPageInsertAruguments(req, amendId);
					//risk.insert.isAmendIDProTreaty
					TtrnRiskDetails	 insert = proportionalityCustomRepository.ttrnRiskDetailsInsert(args);
					ttrnRiskDetailsRepository.saveAndFlush(insert);	
					
					res.setContractGendration("Your Proposal Number :"+ req.getProposalNo());
				}
			} else {
				String maxAmendID = getMaxAmednId(req.getProposalNo());
				if(maxAmendID.equalsIgnoreCase(req.getAmendId())){
					args = getFirstPageInsertAruguments(req, amendId);
					//risk.insert.isAmendIDProTreaty
					TtrnRiskDetails	 insert = proportionalityCustomRepository.ttrnRiskDetailsInsert(args);
					ttrnRiskDetailsRepository.saveAndFlush(insert);	
				}
				else {
					args = getFirstPageEditSaveModeAruguments(req,maxAmendID);
					//risk.update.rskDtls
					TtrnRiskDetails	 update = proportionalityCustomRepository.ttrnRiskDetailsUpdate(args);
					if(update!=null) {
						ttrnRiskDetailsRepository.saveAndFlush(update);	
						}
					args[1]=(Integer.parseInt((String)args[51])+1)+"";
				}
			}
			res = insertRiskProposal(req,saveFlag,ChkSavFlg,amendId,(String)args[1]);
			res.setContractGendration("Your Proposal Number :"+ req.getProposalNo());
			//savFlg = true;
		} catch (Exception e) {
			e.printStackTrace();
			saveFlag = false;
		}
		return res;
	}
	private boolean checkEditSaveModeMethod(final FirstpageSaveReq req) {
		boolean editSaveMode = false;
		try {
			Object[] args = new Object[1];
			args[0] = req.getProposalNo();
			//risk.select.getRskProNO
			List<TtrnRiskDetails> list = ttrnRiskDetailsRepository.findByRskProposalNumber(req.getProposalNo());
			if (list.size() == 0) {
				editSaveMode = false;
			} else {
				editSaveMode = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
		return editSaveMode;
	}
	public String[] getFirstPageEditSaveModeAruguments(final FirstpageSaveReq beanObj,String endNo) {
		String[] args=null;
		try {
		args = new String[54]; //ri
		args[0] = StringUtils.isEmpty(beanObj.getDepartmentId()) ? "0" : beanObj.getDepartmentId();
		args[1] = StringUtils.isEmpty(beanObj.getProfitCenter()) ? "0"	: beanObj.getProfitCenter();
		args[2] = StringUtils.isEmpty(beanObj.getSubProfitcenter()) ? "0": beanObj.getSubProfitcenter();
		args[3] = StringUtils.isEmpty(beanObj.getPolicyBranch()) ? "0" : beanObj.getPolicyBranch();
		args[4] = StringUtils.isEmpty(beanObj.getCedingCo()) ? "0" : beanObj.getCedingCo();
		args[5] = StringUtils.isEmpty(beanObj.getBroker()) ? "63" : beanObj.getBroker(); //ri
		args[6] = StringUtils.isEmpty(beanObj.getTreatyNametype()) ? "": beanObj.getTreatyNametype();
		args[7] = StringUtils.isEmpty(beanObj.getMonth()) ? "" : beanObj.getMonth();
		args[8] = StringUtils.isEmpty(beanObj.getUwYear()) ? "0" : beanObj.getUwYear();
		args[9] = StringUtils.isEmpty(beanObj.getUnderwriter()) ? "" : beanObj.getUnderwriter();
		args[10] = StringUtils.isEmpty(beanObj.getInceptionDate()) ? "" : beanObj.getInceptionDate();
		args[11] = StringUtils.isEmpty(beanObj.getExpiryDate()) ? "" : beanObj.getExpiryDate();
		args[12] = StringUtils.isEmpty(beanObj.getAcceptanceDate()) ? "" : beanObj.getAcceptanceDate();
		args[13] = StringUtils.isEmpty(beanObj.getOrginalCurrency()) ? "0": beanObj.getOrginalCurrency();
		args[14] = StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : beanObj.getExchangeRate();
		args[15] = StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis();
		args[16] = StringUtils.isEmpty(beanObj.getPnoc()) ? "" : beanObj.getPnoc();
		args[17] = StringUtils.isEmpty(beanObj.getRiskCovered()) ? "" : beanObj.getRiskCovered();
		args[18] = StringUtils.isEmpty(beanObj.getTerritoryscope()) ? "": beanObj.getTerritoryscope();
		args[19] = StringUtils.isEmpty(beanObj.getTerritory()) ? "" : beanObj.getTerritory();
		args[20] = StringUtils.isEmpty(beanObj.getProStatus()) ? ""	: beanObj.getProStatus();
		args[21] = StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType();
		args[22] = StringUtils.isEmpty(beanObj.getAccountingPeriod()) ? "0"	: beanObj.getAccountingPeriod();
		args[23] = StringUtils.isEmpty(beanObj.getReceiptofStatements()) ? "0": beanObj.getReceiptofStatements();
		args[24] = StringUtils.isEmpty(beanObj.getReceiptofPayment()) ? "0"	: beanObj.getReceiptofPayment();
		args[25] = StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? "0"	: beanObj.getMdInstalmentNumber();
		args[26] = StringUtils.isEmpty(beanObj.getNoRetroCess()) ? "0": beanObj.getNoRetroCess();
		args[27] = StringUtils.isEmpty(beanObj.getRetroType()) ? "0": beanObj.getRetroType();
		args[28] = StringUtils.isEmpty(beanObj.getInsuredName()) ? "": beanObj.getInsuredName();
		args[29]=StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType();
		args[30]=StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType();
		args[31]=StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType();
		args[32]=StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType();
		args[33]=StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered();
		args[34]=StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued();
		args[35]=StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL();
		args[36] = beanObj.getLoginId();
		args[37] = beanObj.getBranchCode();
		args[38] = beanObj.getCountryIncludedList();
		args[39] = beanObj.getCountryExcludedList();
		args[40] =StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", "");
		args[41] =StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype();
		args[42] =StringUtils.isEmpty(beanObj.getRunoffYear()) ? "0"	:beanObj.getRunoffYear();
		args[43] =StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName();
		args[44] =StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? ""	:beanObj.getLocCreditPrd();
		args[45] =StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? ""	:beanObj.getLocCreditAmt().replaceAll(",", "");
		args[46] =StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName();
		args[47] = "";
		args[48]="";
		args[49]=StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN();
		args[50] = beanObj.getAccountingPeriodNotes();
		args[51] =beanObj.getStatementConfirm();
		args[52] = beanObj.getProposalNo();
		args[53]=endNo;
		} catch (Exception e) {
			e.printStackTrace();
			}
		return args;
	}
	public String[] getFirstPageInsertAruguments(final FirstpageSaveReq beanObj, final boolean amendId) {
		String[] args= new String[59]; //ri
		if (StringUtils.isBlank(beanObj.getProposalNo())) {
			args[26] = "";
			beanObj.setProposalNo(getMaxProposanlno(beanObj.getProductId(),beanObj.getBranchCode(),beanObj.getDepartmentId()));
			args[0] = beanObj.getProposalNo();
			args[1] = "0";
		}else{
			args[0] = beanObj.getProposalNo();
			args[1] =(Integer.parseInt(getMaxAmednId(beanObj.getProposalNo()))+1)+"";
			args[26] = beanObj.getContractNo();
				}
		args[2] = "0";
		args[27] = StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType();
		args[28] = StringUtils.isEmpty(beanObj.getAccountingPeriod()) ? "0"	: beanObj.getAccountingPeriod();
		args[29] = StringUtils.isEmpty(beanObj.getReceiptofStatements()) ? "0": beanObj.getReceiptofStatements();
		args[30] = StringUtils.isEmpty(beanObj.getReceiptofPayment()) ? "0": beanObj.getReceiptofPayment();
		args[3] = beanObj.getProductId();
		args[4] = StringUtils.isEmpty(beanObj.getDepartmentId()) ? "0" : beanObj.getDepartmentId();
		args[5] = StringUtils.isEmpty(beanObj.getProfitCenter()) ? "0": beanObj.getProfitCenter();
		args[6] = StringUtils.isEmpty(beanObj.getSubProfitcenter()) ? "0": beanObj.getSubProfitcenter();
		args[7] = StringUtils.isEmpty(beanObj.getPolicyBranch()) ? "0" : beanObj.getPolicyBranch();
		args[8] = StringUtils.isEmpty(beanObj.getCedingCo()) ? "0" : beanObj.getCedingCo();
		args[9] = StringUtils.isEmpty(beanObj.getBroker()) ? "63" : beanObj.getBroker(); //ri
		args[10] = StringUtils.isEmpty(beanObj.getTreatyNametype()) ? "": beanObj.getTreatyNametype();
		args[11] = StringUtils.isEmpty(beanObj.getMonth()) ? "" : beanObj.getMonth();
		args[12] = StringUtils.isEmpty(beanObj.getUwYear()) ? "0" : beanObj.getUwYear();
		args[13] = StringUtils.isEmpty(beanObj.getUnderwriter()) ? "" : beanObj.getUnderwriter();
		args[14] = StringUtils.isEmpty(beanObj.getInceptionDate()) ? "" : beanObj.getInceptionDate();
		args[15] = StringUtils.isEmpty(beanObj.getExpiryDate()) ? "" : beanObj.getExpiryDate();
		args[16] = StringUtils.isEmpty(beanObj.getAcceptanceDate()) ? "" : beanObj.getAcceptanceDate();
		args[17] = StringUtils.isEmpty(beanObj.getOrginalCurrency()) ? "0": beanObj.getOrginalCurrency();
		args[18] = StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : beanObj.getExchangeRate();
		args[19] = StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis();
		args[20] = StringUtils.isEmpty(beanObj.getPnoc()) ? "" : beanObj.getPnoc();
		args[21] = StringUtils.isEmpty(beanObj.getRiskCovered()) ? "0": beanObj.getRiskCovered();
		args[22] = StringUtils.isEmpty(beanObj.getTerritoryscope()) ? "": beanObj.getTerritoryscope();
		args[23] = StringUtils.isBlank(beanObj.getTerritory())?"":beanObj.getTerritory();
		args[24] = StringUtils.isEmpty(beanObj.getProStatus()) ? "0" : beanObj.getProStatus();
		args[25] = "";
		args[31] = StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? "0": beanObj.getMdInstalmentNumber();
		args[32] = StringUtils.isEmpty(beanObj.getNoRetroCess()) ? "0": beanObj.getNoRetroCess();
		args[33] = StringUtils.isEmpty(beanObj.getRetroType()) ? "0": beanObj.getRetroType();
		args[34] = StringUtils.isEmpty(beanObj.getInsuredName()) ? "": beanObj.getInsuredName();
		args[35] = StringUtils.isEmpty(beanObj.getRenewalcontractNo()) ? "": beanObj.getRenewalcontractNo();
		args[36]=StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType();
		args[37]=StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType();
		args[38]=StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType();
		args[39]=StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType();
		args[40]=StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered();
		args[41]=StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued();
		args[42]=StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL();
		args[43] = beanObj.getLoginId();
		args[44] = beanObj.getBranchCode();
		args[45] = beanObj.getCountryIncludedList();
		args[46] = beanObj.getCountryExcludedList();
		args[47] =StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", "");
		args[48] =StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype();
		args[49] =StringUtils.isEmpty(beanObj.getRunoffYear()) ? "0"	:beanObj.getRunoffYear();
		args[50] =StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName();
		args[51] =StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? ""	:beanObj.getLocCreditPrd();
		args[52] =StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? ""	:beanObj.getLocCreditAmt().replaceAll(",", "");
		args[53] =StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName();
		args[54]="";
		args[55]="";
		args[56]=StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN();
		args[57] = beanObj.getAccountingPeriodNotes();
		args[58] = beanObj.getStatementConfirm();
		return args;
	}
	private String getMaxAmednIdPro(final String proposalNo) {
		String result ="";
		try{
			//GET_MAX_AMEND_RISK_PROPOSAL
			TtrnRiskProposal list = ttrnRiskProposalRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if(list!=null) {
				result=list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	private String getMaxAmednId(final String proposalNo) {
		String result ="";
		try{ 
			//risk.select.getMaxEndorseNo
			TtrnRiskDetails list = ttrnRiskDetailsRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if(list!=null) {
				result=list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}	
	public String getMaxProposanlno(String pid,String branchCode, String deptId) {
		String result="";
		try{
				result=queryImpl.getSequenceNo("Proposal",pid,"0", branchCode,"",""); //RI: .getSequence("Proposal",pid,deptId, branchCode,"","")
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	private FirstpagesaveRes insertRiskProposal(final FirstpageSaveReq beanObj,final boolean saveFlag,final boolean ChekmodeFlag,boolean amendId,final String amednIdvalue) {
		FirstpagesaveRes res=new FirstpagesaveRes();
		try {
			String[] obj=null;
			String maxAmendId="0";
			if(!"0".endsWith(amednIdvalue))
				maxAmendId=(Integer.parseInt(amednIdvalue)-1)+"";
			if (saveFlag) {
				if (ChekmodeFlag) {
					int count=0;
					String endtNo=getMaxAmednIdPro(beanObj.getProposalNo());
					obj = getProposalSaveEditModeQuery(beanObj,endtNo);
					//risk.update.pro24FirPageRskPro
					TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalUpdate(obj);
					if(update!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update);	
					 count=1;
					}
					
					if (count>0) {
						if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
							res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
							res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
							res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
						}
					}
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					obj = updateHomePositionMasterAruguments(beanObj,"0");
					//risk.update.positionMaster
					PositionMaster update1 = proportionalityCustomRepository.positionMasterUpdate(obj);
					if(update1!=null) {
						positionMasterRepository.saveAndFlush(update1);	
						count=1;
					}
					
					if (count > 0) {
						if(StringUtils.isNotBlank(beanObj.getContractNo())) {
							res.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ beanObj.getProposalNo());
						}
						else if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
							res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
							res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
							res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
						} 
					}
				} else {
					obj = getFirstPageSecondTableAruguments(beanObj, amednIdvalue, amendId);
					//risk.insert.pro24RskProposal
					TtrnRiskProposal insert = proportionalityCustomRepository.ttrnRiskProposalInsert(obj);
					if(insert!=null) {
						ttrnRiskProposalRepository.saveAndFlush(insert);	
					}
				
					String renewalStatus = getRenewalStatus(beanObj.getProposalNo(),beanObj.getContractNo());
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					obj = insertHomePositionMasterAruguments(beanObj,amednIdvalue, amendId,renewalStatus);
					//risk.insert.positionMaster
					PositionMaster insert1 = proportionalityCustomRepository.positionMasterInsert(obj);
					if(insert1!=null) {
						positionMasterRepository.saveAndFlush(insert1);	
					}
					if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
						res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
					}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
						res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
					}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
						res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
					}
				}
			} else {
				if (!ChekmodeFlag) {
					int count=0;
					if(maxAmendId.equalsIgnoreCase(beanObj.getAmendId())){
						//risk.insert.pro24RskProposal
						obj = getFirstPageSecondTableInsertAruguments(beanObj, amednIdvalue, amendId);
						TtrnRiskProposal insert = proportionalityCustomRepository.ttrnRiskProposalInsert(obj);
						if(insert!=null) {
							ttrnRiskProposalRepository.saveAndFlush(insert);	
							 count=1;
						}
						
					}else{
						obj = getProposalSaveEditModeQuery(beanObj,maxAmendId);
						//risk.update.pro24FirPageRskPro
						TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalUpdate(obj);
						if(update!=null) {
						ttrnRiskProposalRepository.saveAndFlush(update);	
						 count=1;
						}
						
					}
					if (count > 0) {
						//InsertFlag = true;
					}
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					int insertCount = 0;
					if(maxAmendId.equalsIgnoreCase(beanObj.getAmendId())){
						String renewalStatus = getRenewalStatus(beanObj.getProposalNo(),beanObj.getContractNo());
						obj = insertHomePositionMasterAruguments(beanObj,amednIdvalue, amendId,renewalStatus);
					//risk.insert.positionMaster
						PositionMaster insert1 = proportionalityCustomRepository.positionMasterInsert(obj);
						if(insert1!=null) {
							positionMasterRepository.saveAndFlush(insert1);	
							insertCount=1;
						}
						
					}else{
						//risk.update.positionMaster
						obj = updateHomePositionMasterAruguments(beanObj,maxAmendId);
						PositionMaster update1 = proportionalityCustomRepository.positionMasterUpdate(obj);
						if(update1!=null) {
							positionMasterRepository.saveAndFlush(update1);	
							insertCount=1;
						}
						
					}
					if (insertCount > 0){
						//InsertFlag = true;
					}
					if (beanObj.getProStatus().equalsIgnoreCase("R")) {
						res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
					}
					String proposalno="";
					if (StringUtils.isNotEmpty(beanObj.getLayerProposalNo())) {
						proposalno = beanObj.getLayerProposalNo();
					} else {
						proposalno = beanObj.getProposalNo();
					}
					//this.showSecondpageEditItems(beanObj, pid, proposalno);
				}
			}
			res.setProposalNo(beanObj.getProposalNo());
			res.setMessage("Success");
			res.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
	}
	public String[] getProposalSaveEditModeQuery(final FirstpageSaveReq beanObj,String endNo) {
		String[] obj=null;
		obj = new String[51];
		if( beanObj.getTreatyType().equalsIgnoreCase("4") ||  beanObj.getTreatyType().equalsIgnoreCase("5") ){
			obj[0] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ? "0" : beanObj.getFaclimitOrigCur();
			obj[1] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchangeRate());
		}
		else{
			obj[0] = StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? "0": beanObj.getLimitOrigCur();
			obj[1] = StringUtils.isEmpty(beanObj.getLimitOrigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchangeRate());
		}
		obj[2] = StringUtils.isEmpty(beanObj.getEpiorigCur()) ? "0": beanObj.getEpiorigCur();
		obj[3] = StringUtils.isEmpty(beanObj.getEpiorigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchangeRate());
		obj[4] = StringUtils.isEmpty(beanObj.getOurEstimate()) ? "0": beanObj.getOurEstimate();
		obj[7] = StringUtils.isEmpty(beanObj.getXlCost()) ? "0" : beanObj.getXlCost();
		obj[8] = StringUtils.isEmpty(beanObj.getXlCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlCost(), beanObj.getExchangeRate());
		obj[9] = StringUtils.isEmpty(beanObj.getCedRetent()) ? "0" : beanObj.getCedRetent();
		obj[5] = StringUtils.isEmpty(beanObj.getEpi()) ? "0" : beanObj.getEpi().replaceAll(",", "");
		obj[6] = StringUtils.isEmpty(beanObj.getEpi())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[10] = StringUtils.isEmpty(beanObj.getShareWritten()) ? "0": beanObj.getShareWritten();
		if (beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[11] = "0";
		} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
			obj[11] = StringUtils.isEmpty(beanObj.getSharSign()) ? "0": beanObj.getSharSign();
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[11] = "0";
		} else {
			obj[11] = "0";
		}
		obj[12] =StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		obj[13] = StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro();
		obj[14] = StringUtils.isEmpty(beanObj.getNoInsurer()) ? "0" : beanObj.getNoInsurer();
		obj[15] = StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? "0" : beanObj.getMaxLimitProduct();
		obj[16] =StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[17] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[18] =StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[19] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[20] =StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? "0": beanObj.getEpiAsPerShare();
		obj[21] = StringUtils.isEmpty(beanObj.getEpiAsPerShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate());
		obj[22] =StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? "0": beanObj.getXlcostOurShare();
		obj[23] = StringUtils.isEmpty(beanObj.getXlcostOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate());
		obj[24] =StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? "0": beanObj.getLimitPerVesselOC();
		obj[25] = StringUtils.isEmpty(beanObj.getLimitPerVesselOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchangeRate());
		obj[26] =StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? "0": beanObj.getLimitPerLocationOC();
		obj[27] = StringUtils.isEmpty(beanObj.getLimitPerLocationOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchangeRate());
		obj[28] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? "0": beanObj.getTreatyLimitsurplusOC();
		obj[29] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchangeRate());
		obj[30] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? "0": beanObj.getTreatyLimitsurplusOurShare();
		obj[31] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchangeRate());
		obj[32] =StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml();
		obj[33] = StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchangeRate());
		obj[34] =StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0": beanObj.getLimitOrigCurPmlOS();
		obj[35] = StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchangeRate());
		obj[36] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0": beanObj.getTreatyLimitsurplusOCPml();
		obj[37] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchangeRate());
		obj[38] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0": beanObj.getTreatyLimitsurplusOCPmlOS();
		obj[39] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchangeRate());
		obj[40] =StringUtils.isEmpty(beanObj.getEpipml()) ? "0": beanObj.getEpipml();
		obj[41] = StringUtils.isEmpty(beanObj.getEpipml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipml(), beanObj.getExchangeRate());
		obj[42] =StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0": beanObj.getEpipmlOS();
		obj[43] = StringUtils.isEmpty(beanObj.getEpipmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchangeRate());
		obj[44]=beanObj.getDepartmentId();
		obj[45]=beanObj.getLoginId();
		obj[46]=beanObj.getBranchCode();
		obj[47]=StringUtils.isEmpty(beanObj.getPml()) ? "": beanObj.getPml();
		obj[48]=StringUtils.isEmpty(beanObj.getPmlPercent()) ? "0.00": beanObj.getPmlPercent();
		obj[49] = beanObj.getProposalNo();
		obj[50]=endNo;
		return obj;
	}
	public String getDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String output="0.0";
		try{
			double origCountryVal=0.0;
			if(limitOrigCur!=null){
				String val = limitOrigCur.replaceAll(",", "");
				if (!("".equalsIgnoreCase(val))&& Double.parseDouble(val) != 0) {
					origCountryVal = Double.parseDouble(val) / Double.parseDouble(ExchangeRate);
					output = fm.formatter(Double.toString(origCountryVal).toString()).replaceAll(",", "");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return output;
	}
	public boolean updateFirstPageFields(final FirstpageSaveReq beanObj,String endNo){
		boolean updateStatus = true;
		int res=0;
		
		String[] obj= new String[55]; //RI
		try {
			obj[0] = StringUtils.isEmpty(beanObj.getEventlimit()) ? "": beanObj.getEventlimit();
			obj[1] = StringUtils.isEmpty(beanObj.getEventlimit())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getEventlimit(), beanObj.getExchangeRate());
			obj[2] = StringUtils.isEmpty(beanObj.getEventLimitOurShare()) ? "0" : beanObj.getEventLimitOurShare();
			obj[3] = StringUtils.isEmpty(beanObj.getEventLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : getDesginationCountry(beanObj.getEventLimitOurShare(), beanObj.getExchangeRate());
			
			obj[4] = StringUtils.isEmpty(beanObj.getCoverLimitXL()) ? "": beanObj.getCoverLimitXL();
			obj[5] = StringUtils.isEmpty(beanObj.getCoverLimitXL())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getCoverLimitXL(), beanObj.getExchangeRate());
			obj[6] = StringUtils.isEmpty(beanObj.getCoverLimitXLOurShare()) ? "0" : beanObj.getCoverLimitXLOurShare();
			obj[7] = StringUtils.isEmpty(beanObj.getCoverLimitXLOurShare()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : getDesginationCountry(beanObj.getCoverLimitXLOurShare(), beanObj.getExchangeRate());
			
			obj[8] = StringUtils.isEmpty(beanObj.getDeductLimitXL()) ? "": beanObj.getDeductLimitXL();
			obj[9] = StringUtils.isEmpty(beanObj.getDeductLimitXL())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getDeductLimitXL(), beanObj.getExchangeRate());
			obj[10] = StringUtils.isEmpty(beanObj.getDeductLimitXLOurShare()) ? "0" : beanObj.getDeductLimitXLOurShare();
			obj[11] = StringUtils.isEmpty(beanObj.getDeductLimitXLOurShare()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : getDesginationCountry(beanObj.getDeductLimitXLOurShare(), beanObj.getExchangeRate());
			
			obj[12] = StringUtils.isEmpty(beanObj.getPml()) ? "" : beanObj.getPml();
			if("Y".equalsIgnoreCase(beanObj.getPml())){
			obj[13] = StringUtils.isEmpty(beanObj.getPmlPercent()) ? "0" : beanObj.getPmlPercent();
			
			obj[14] = StringUtils.isEmpty(beanObj.getEgnpipml()) ? "": beanObj.getEgnpipml();
			obj[15] = StringUtils.isEmpty(beanObj.getEgnpipml())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getEgnpipml(), beanObj.getExchangeRate());
			obj[16] = StringUtils.isEmpty(beanObj.getEgnpipmlOurShare()) ? "0" : beanObj.getEgnpipmlOurShare();
			}
			else{
				obj[13]="";
				obj[14]="";
				obj[15]="";
				obj[16]="";
			}
			obj[17] = StringUtils.isEmpty(beanObj.getEgnpipmlOurShare()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : getDesginationCountry(beanObj.getEgnpipmlOurShare(), beanObj.getExchangeRate());
			
			obj[18] = StringUtils.isEmpty(beanObj.getPremiumbasis()) ? "" : beanObj.getPremiumbasis();
			obj[19] = StringUtils.isEmpty(beanObj.getMinimumRate()) ? "0" : beanObj.getMinimumRate();
			obj[20] = StringUtils.isEmpty(beanObj.getMaximumRate()) ? "0" : beanObj.getMaximumRate();
			obj[21] = StringUtils.isEmpty(beanObj.getBurningCostLF()) ? "0" : beanObj.getBurningCostLF();
			
			obj[22] = StringUtils.isEmpty(beanObj.getMinPremium()) ? "": beanObj.getMinPremium();
			obj[23] = StringUtils.isEmpty(beanObj.getMinPremium())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getMinPremium(), beanObj.getExchangeRate());
			obj[24] = StringUtils.isEmpty(beanObj.getMinPremiumOurShare()) ? "0" : beanObj.getMinPremiumOurShare();
			obj[25] = StringUtils.isEmpty(beanObj.getMinPremiumOurShare()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0" : getDesginationCountry(beanObj.getMinPremiumOurShare(), beanObj.getExchangeRate());
			obj[26] = StringUtils.isEmpty(beanObj.getPaymentDuedays()) ? "0" : beanObj.getPaymentDuedays();
			
			obj[27] =StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml();
			obj[28] = StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchangeRate());
			obj[29] =StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0": beanObj.getLimitOrigCurPmlOS();
			obj[30] = StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchangeRate());
			obj[31] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0": beanObj.getTreatyLimitsurplusOCPml();
			obj[32] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchangeRate());
			obj[33] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0": beanObj.getTreatyLimitsurplusOCPmlOS();
			obj[34] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchangeRate());
			obj[35] =StringUtils.isEmpty(beanObj.getEpipml()) ? "0": beanObj.getEpipml();
			obj[36] = StringUtils.isEmpty(beanObj.getEpipml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipml(), beanObj.getExchangeRate());
			obj[37] =StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0": beanObj.getEpipmlOS();
			obj[38] = StringUtils.isEmpty(beanObj.getEpipmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchangeRate());
			obj[39]=beanObj.getRiskdetailYN(); //RI
			obj[40]=beanObj.getBrokerdetYN();
			obj[41]=beanObj.getCoverdetYN();
			obj[42]=beanObj.getPremiumdetailYN();
			obj[43]=beanObj.getAcqdetailYN();
			obj[44]=beanObj.getCommissiondetailYN();
			obj[45]=beanObj.getDepositdetailYN();
			obj[46]=beanObj.getLossdetailYN();
			obj[47]=beanObj.getDocdetailYN();
			obj[48] = beanObj.getPaymentPartner();
			obj[49] = beanObj.getInstallYN();
			obj[50] = beanObj.getReinstdetailYN();
			obj[51] = beanObj.getRateOnLine();
			obj[52] =StringUtils.isEmpty(beanObj.getQuotesharePercent()) ? "0": beanObj.getQuotesharePercent();
			obj[53] = beanObj.getProposalNo();
			obj[54]=endNo;
			
			//UPDATE_RISK_PROPOSAL_DETAILS
			TtrnRiskProposal update = proportionalityCustomRepository.updateFirstPageFields(obj);
			if(update!=null) {
			ttrnRiskProposalRepository.saveAndFlush(update);	
			res=1;
			}
			if (res> 0) {
				updateStatus = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateStatus;
	}
	public String[] updateHomePositionMasterAruguments(final FirstpageSaveReq beanObj,  final String maxAmdId) {
		String[] obj = new String[23];
		obj[0] = StringUtils.isEmpty(beanObj.getLayerNo()) ? "0" : beanObj.getLayerNo();
		obj[1] = "";
		obj[2] = beanObj.getProductId();
		obj[3] = beanObj.getDepartmentId();
		obj[4] = beanObj.getCedingCo();
		obj[5] = beanObj.getUwYear();
		obj[6] = beanObj.getMonth();
		obj[7] = beanObj.getAcceptanceDate();
		obj[8] = beanObj.getInceptionDate();
		obj[9] = beanObj.getExpiryDate();
		obj[10] = beanObj.getProStatus();
		if(beanObj.getContractNo()!=null&&!"0".equals(beanObj.getContractNo())&&!"".equals(beanObj.getContractNo()))
			obj[11] = "A";
		else if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[11] = "P";
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[11] = "R";
		} else if("N".equalsIgnoreCase(beanObj.getProStatus())) {
			obj[11] = "N";
		}else{
			obj[11] = "P";
		}
		obj[12] = StringUtils.isBlank(beanObj.getBroker())?"63":beanObj.getBroker();
		obj[13] = StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType();
		obj[14] = beanObj.getLoginId();
		obj[15] = "";
		obj[16] =  StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal();
		obj[17] = beanObj.getBouquetModeYN();
		obj[18] = beanObj.getBouquetNo();
		obj[19] = beanObj.getUwYearTo();
		obj[20] = beanObj.getSectionNo();
		obj[21] = beanObj.getProposalNo();
		obj[22] = maxAmdId;
		return obj;
	}
	public String[] getFirstPageSecondTableAruguments(final FirstpageSaveReq beanObj, final String args2, final boolean amendId) {
		String[] obj=null;
		obj = new String[38];
		if (amendId) {
			obj[0] = beanObj.getProposalNo();
			obj[1] = args2;
		} else {
			obj[0] = beanObj.getProposalNo();
			obj[1] = "0";
		}
		if( beanObj.getTreatyType().equalsIgnoreCase("4") ||  beanObj.getTreatyType().equalsIgnoreCase("5") ){
			obj[3] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ? "0" : beanObj.getFaclimitOrigCur();
			obj[4] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchangeRate());
		}
		else{
		obj[3] = StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? "0" : beanObj.getLimitOrigCur();
		obj[4] = StringUtils.isEmpty(beanObj.getLimitOrigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchangeRate());
		}
		obj[2] = "0";
		obj[5] = StringUtils.isEmpty(beanObj.getEpiorigCur()) ? "0": beanObj.getEpiorigCur();
		obj[6] = StringUtils.isEmpty(beanObj.getEpiorigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchangeRate());
		obj[7] = StringUtils.isEmpty(beanObj.getOurEstimate()) ? "0": beanObj.getOurEstimate();
		obj[10] = StringUtils.isEmpty(beanObj.getXlCost()) ? "" : beanObj.getXlCost();
		obj[11] = StringUtils.isEmpty(beanObj.getXlCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlCost(), beanObj.getExchangeRate());
		obj[12] = StringUtils.isEmpty(beanObj.getCedRetent()) ? "0": beanObj.getCedRetent();
		obj[8] = StringUtils.isEmpty(beanObj.getEpi()) ? "0" : beanObj.getEpi();
		obj[9] = StringUtils.isEmpty(beanObj.getEpi())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[13] = StringUtils.isEmpty(beanObj.getShareWritten()) ? "0": beanObj.getShareWritten();
		if (beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[14] = "0";
		} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
			obj[14] = StringUtils.isEmpty(beanObj.getSharSign()) ? "0": beanObj.getSharSign();
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[14] = "0";
		} else {
			obj[14] = "0";
		}
		obj[15] = StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		obj[16] = StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro();
		obj[17] = StringUtils.isEmpty(beanObj.getNoInsurer()) ? "0" : beanObj.getNoInsurer();
		obj[18] = StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? "0" : beanObj.getMaxLimitProduct();
		obj[19] =StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[20] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[21] =StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[22] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[23] =StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? "0": beanObj.getEpiAsPerShare();
		obj[24] = StringUtils.isEmpty(beanObj.getEpiAsPerShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate());
		obj[25] =StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? "0"	: beanObj.getXlcostOurShare();
		obj[26] = StringUtils.isEmpty(beanObj.getXlcostOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate());
		obj[27] =StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? "0": beanObj.getLimitPerVesselOC();
		obj[28] = StringUtils.isEmpty(beanObj.getLimitPerVesselOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchangeRate());
		obj[29] =StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? "0": beanObj.getLimitPerLocationOC();
		obj[30] = StringUtils.isEmpty(beanObj.getLimitPerLocationOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchangeRate());
		obj[31] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? "0": beanObj.getTreatyLimitsurplusOC();
		obj[32] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchangeRate());
		obj[33] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? "0": beanObj.getTreatyLimitsurplusOurShare();
		obj[34] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchangeRate());
		obj[35]=beanObj.getDepartmentId();
		obj[36]=beanObj.getLoginId();
		obj[37]=beanObj.getBranchCode();
		return obj;
	}
	private String getRenewalStatus(String proposalNo,String contractNo) {
		String result="";
		try{
			if(StringUtils.isNotBlank(contractNo)){
				//risk.select.getRenewalStatus
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				query.multiselect(cb.selectCase().when(cb.isNull(pm.get("renewalStatus")), "0")
						.otherwise(pm.get("renewalStatus"))); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2);
				
				TypedQuery<String> res1 = em.createQuery(query);
				List<String> list = res1.getResultList();
				 
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0)==null?"":list.get(0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}
	public String[] insertHomePositionMasterAruguments(final FirstpageSaveReq beanObj, final String args2, final boolean amendId,String renewalStatus) {
		String bouquetno="",offerNo="";
		if(StringUtils.isBlank(beanObj.getBouquetNo()) && "Y".equals(beanObj.getBouquetModeYN())) {
			bouquetno=fm.getSequence("Bouquet","9","0", beanObj.getBranchCode(),"","");
			//String query=getQuery("GET_BOUQUET_NO_SEQ");
			//bouquetno=this.mytemplate.queryForObject(query, String.class);
			beanObj.setBouquetNo(bouquetno);
		}
		if(StringUtils.isBlank(beanObj.getOfferNo())) {
			offerNo=fm.getSequence("Offer",beanObj.getProductId(),"0", beanObj.getBranchCode(),"","");
			beanObj.setOfferNo(offerNo);
		} //Ri
		
		
		String[] obj = new String[31];
		if (amendId) {
			obj[1] = beanObj.getContractNo();
			obj[2] = args2;
			obj[16] = beanObj.getBaseLayer();
		} else {
			obj[1] = "0";
			obj[2] = "0";
			obj[16] = StringUtils.isBlank(beanObj.getBaseLayer())?beanObj.getLayerProposalNo():beanObj.getBaseLayer();
		}
		obj[0] = beanObj.getProposalNo();
		obj[3] = StringUtils.isEmpty(beanObj.getLayerNo()) ? "0" : beanObj.getLayerNo();
		obj[4] = "";
		obj[5] = beanObj.getProductId();
		obj[6] = beanObj.getDepartmentId();
		obj[7] = beanObj.getCedingCo();
		obj[8] = beanObj.getUwYear();
		obj[9] = beanObj.getMonth();
		obj[10] = beanObj.getAcceptanceDate();
		obj[11] = beanObj.getInceptionDate();
		obj[12] = beanObj.getExpiryDate();
		obj[13] = beanObj.getProStatus();
		if (amendId) {
			obj[14] = "A";
			obj[18] = renewalStatus;
		} else {
			obj[14] = "P";
			obj[18] = StringUtils.isEmpty(beanObj.getLayerProposalNo()) ? "1" : "0";
		}
		obj[15] = StringUtils.isNotBlank(beanObj.getBaseLoginId())?beanObj.getBaseLoginId():beanObj.getLoginId();
		obj[17] = StringUtils.isEmpty(beanObj.getLayerProposalNo()) ?StringUtils.isEmpty(beanObj.getRenewalcontractNo()) ? "" : beanObj.getRenewalcontractNo():"";
		obj[19] = StringUtils.isBlank(beanObj.getBroker())?"63":beanObj.getBroker();
		obj[20] = beanObj.getBranchCode();
		obj[21] = StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType();
		obj[22] = beanObj.getLoginId();
		obj[23] = "";
		obj[24] = "";
		obj[25] = StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal();
		obj[26] = beanObj.getBouquetModeYN();
		obj[27] = beanObj.getBouquetNo();
		obj[28] = beanObj.getUwYearTo();
		obj[29] = beanObj.getSectionNo();
		obj[30] = beanObj.getOfferNo();
		return obj;
	}
	public String[] getFirstPageSecondTableInsertAruguments(final FirstpageSaveReq beanObj,final String args2, final boolean amendId) {
		String[] obj=null;
		obj = new String[38];
		if (amendId) {
			obj[1] = args2;
		} else {
			obj[1] = "0";
		}
		obj[0] = beanObj.getProposalNo();
		if( beanObj.getTreatyType().equalsIgnoreCase("4") ||  beanObj.getTreatyType().equalsIgnoreCase("5") ){
			obj[3] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ? "0" : beanObj.getFaclimitOrigCur();
			obj[4] = StringUtils.isEmpty(beanObj.getFaclimitOrigCur())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchangeRate());
		}
		else{
		obj[3] = beanObj.getLimitOrigCur();
		obj[4] = getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchangeRate());
		}
		obj[2] = "0";
		obj[5] = beanObj.getEpiorigCur();
		obj[6] = getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchangeRate());
		obj[7] =StringUtils.isBlank(beanObj.getOurEstimate())?"0": beanObj.getOurEstimate();
		obj[10] = StringUtils.isBlank(beanObj.getXlCost())?"":beanObj.getXlCost();
		obj[11] = getDesginationCountry(StringUtils.isBlank(beanObj.getXlCost())?"0":beanObj.getXlCost(), beanObj.getExchangeRate());
		obj[8] = StringUtils.isBlank(beanObj.getEpi())?"0": beanObj.getEpi();
		obj[9] = getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[12] = StringUtils.isBlank(beanObj.getCedRetent())?"0":beanObj.getCedRetent();
		obj[13] = beanObj.getShareWritten();
		if (beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[14] = "0";
		} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
			obj[14] = beanObj.getSharSign();
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[14] = "0";
		} else {
			obj[14] = "0";
		}
		obj[15]=StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		obj[16] = StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro();
		obj[17] = StringUtils.isEmpty(beanObj.getNoInsurer()) ? "0" : beanObj.getNoInsurer();
		obj[18] = StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? "0" : beanObj.getMaxLimitProduct();
		obj[19] =StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[20] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[21] =StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[22] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[23] =StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? "0": beanObj.getEpiAsPerShare();
		obj[24] = StringUtils.isEmpty(beanObj.getEpiAsPerShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate());
		obj[25] =StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? "0": beanObj.getXlcostOurShare();
		obj[26] = StringUtils.isEmpty(beanObj.getXlcostOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate());
		obj[27] =StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? "0": beanObj.getLimitPerVesselOC();
		obj[28] = StringUtils.isEmpty(beanObj.getLimitPerVesselOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchangeRate());
		obj[29] =StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? "0": beanObj.getLimitPerLocationOC();
		obj[30] = StringUtils.isEmpty(beanObj.getLimitPerLocationOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchangeRate());
		obj[31] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? "0": beanObj.getTreatyLimitsurplusOC();
		obj[32] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchangeRate());
		obj[33] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? "0": beanObj.getTreatyLimitsurplusOurShare();
		obj[34] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchangeRate());
		obj[35] =beanObj.getDepartmentId();
		obj[36]=beanObj.getLoginId();
		obj[37]=beanObj.getBranchCode();
		return obj;
	}
	@Transactional
	@Override
	public CommonSaveRes insertRemarkDetails(RemarksSaveReq beanObj) {
		CommonSaveRes resp=new CommonSaveRes();
		int amendId=0;
		try {
			//DELETE_REMARKS_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnRiskRemarks> delete = cb.createCriteriaDelete(TtrnRiskRemarks.class);
			
			Root<TtrnRiskRemarks> rp = delete.from(TtrnRiskRemarks.class);
			
			// MAXAmend ID
			Subquery<Long> amend = delete.subquery(Long.class); 
			Root<TtrnRiskRemarks> rrs = amend.from(TtrnRiskRemarks.class);
			amend.select(cb.max(rrs.get("amendId")));
			Predicate a1 = cb.equal( rrs.get("proposalNo"), rp.get("proposalNo"));
			amend.where(a1);
			
			//Where
			Predicate n1 = cb.equal(rp.get("proposalNo"), beanObj.getProposalNo());
			Predicate n2 = cb.equal(rp.get("layerNo"), "0");
			Predicate n3 = cb.equal(rp.get("amendId"), amend);
			delete.where(n1,n2,n3);
			em.createQuery(delete).executeUpdate();
			
			//GET_AMEND_REMARKS
			List<TtrnRiskRemarks> list = ttrnRiskRemarksRepository.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(beanObj.getProposalNo()));
			
			if(!CollectionUtils.isEmpty(list)) {
				amendId=list.get(0).getAmendId()==null?0:Integer.valueOf(list.get(0).getAmendId()+1);
			}
			if(!CollectionUtils.isEmpty(beanObj.getRemarksReq())) {
				//INSERT_REMARKS_DETAILS
			for(int i=0;i<beanObj.getRemarksReq().size();i++){
				RemarksReq req=beanObj.getRemarksReq().get(i);
			
				String[] obj= new String[12];
				obj[0]=beanObj.getProposalNo();
				obj[1]=beanObj.getContractNo();
				obj[2]="0";
				obj[3]=beanObj.getDepartmentId();
				obj[4]=beanObj.getProductid();
				obj[5]=String.valueOf(amendId);
				obj[6]=String.valueOf(i+1);
				obj[7]=req.getDescription();
				obj[8]=req.getRemark1();
				obj[9]=req.getRemark2();
				obj[10]=beanObj.getLoginId();
				obj[11]=beanObj.getBranchCode();
				TtrnRiskRemarks insert = proportionalityCustomRepository.ttrnRiskRemarksInsert(obj);
				if(insert!=null) {
					ttrnRiskRemarksRepository.saveAndFlush(insert);	
				}
			}
			resp.setResponse("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
		
	}
	@Transactional
	public CommonSaveRes insertCedentRetention(CedentSaveReq beanObj) {
		CommonSaveRes resp=new CommonSaveRes();
		int amendId=0;
		try {
			//DELETE_RET_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnCedentRet> delete = cb.createCriteriaDelete(TtrnCedentRet.class);
			
			Root<TtrnCedentRet> rp = delete.from(TtrnCedentRet.class);
			
			// MAXAmend ID
			Subquery<Long> amend = delete.subquery(Long.class); 
			Root<TtrnRiskRemarks> rrs = amend.from(TtrnRiskRemarks.class);
			amend.select(cb.max(rrs.get("amendId")));
			Predicate a1 = cb.equal( rrs.get("proposalNo"), rp.get("proposalNo"));
			amend.where(a1);
			
			//Where
			Predicate n1 = cb.equal(rp.get("proposalNo"), beanObj.getProposalNo());
			Predicate n2 = cb.equal(rp.get("layerNo"), "0");
			Predicate n3 = cb.equal(rp.get("amendId"), amend);
			delete.where(n1,n2,n3);
			em.createQuery(delete).executeUpdate();
			
			//GET_AMEND_RETENT
			TtrnCedentRet list = ttrnCedentRetRepository.findTop1ByProposalNoOrderByAmendIdDesc(beanObj.getProposalNo());
			if(list!=null) {
				amendId=list.getAmendId()==null?0:Integer.valueOf(list.getAmendId()+1);
			}
			//INSERT_RET_DETAILS
			if(StringUtils.isNotBlank(beanObj.getRetentionYN()) && "Y".equalsIgnoreCase(beanObj.getRetentionYN())){
			if(beanObj.getCedentRetentReq()!=null) {
				for(int i=0;i<beanObj.getCedentRetentReq().size();i++){
					CedentRetentReq req=beanObj.getCedentRetentReq().get(i);
					String[] obj= new String[20];
					obj[0]=beanObj.getProposalNo();
					obj[1]=beanObj.getContractNo();
					obj[2]="0";
					obj[3]=beanObj.getDepartmentId();
					obj[4]=beanObj.getProductid();
					obj[5]=String.valueOf(amendId);
					obj[6]=String.valueOf(i+1);
					obj[7]=req.getCoverdepartId();
					obj[8]=req.getCoversubdepartId();
					obj[9]=req.getRetBusinessType();
					obj[10]=req.getRetType();
					obj[11]=req.getRetBasis();
					obj[12]=req.getFirstretention().replace(",", "");
					obj[13]=req.getSecondretention().replace(",", "");
					obj[14]=req.getRetTreatyFST().replace(",", "");
					obj[15]=req.getRetTreatySST().replace(",", "");
					obj[16]=req.getRetEventFST().replace(",", "");
					obj[17]=req.getRetEventSST().replace(",", "");
					obj[18]=beanObj.getLoginId();
					obj[19]=beanObj.getBranchCode();
					TtrnCedentRet insert = proportionalityCustomRepository.ttrnCedentRetInsert(obj);
					if(insert!=null) {
						ttrnCedentRetRepository.saveAndFlush(insert);	
					}
				}
			}
			resp.setResponse("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}
	
	@Override
	public FirstpageupdateRes updateProportionalTreaty(FirstpageSaveReq req) {
		FirstpageupdateRes res=new FirstpageupdateRes();
		boolean savFlg = false;
		try {
			String[] args = getFirstPageSaveModeAruguments(req,getMaxAmednId(req.getProposalNo()));
			//risk.update.rskDtls
			TtrnRiskDetails	 update = proportionalityCustomRepository.ttrnRiskDetailsUpdate(args);
			ttrnRiskDetailsRepository.saveAndFlush(update);	
			int updateCount = 1;
			
			if (updateCount > 0) {
				updateRiskProposal(req);
				savFlg = true;
			}
			res.setStatus(savFlg);
			res.setIsError(false);
			res.setMessage("Success");
		} catch (Exception e) {
			logger.debug("Exception @ {" + e + "}");
			res.setIsError(true);
			res.setMessage("Failed");

		}
		return res;
	}
	public String[] getFirstPageSaveModeAruguments(final FirstpageSaveReq beanObj,String endNo) {
		String[] args=null;
		args = new String[54]; //Ri
		args[0] = beanObj.getDepartmentId();
		args[1] = beanObj.getProfitCenter();
		args[2] = beanObj.getSubProfitcenter();
		args[3] = beanObj.getPolicyBranch();
		args[4] = beanObj.getCedingCo();
		args[5] =  StringUtils.isBlank(beanObj.getBroker())?"63":beanObj.getBroker();
		args[6] = beanObj.getTreatyNametype();
		args[7] = beanObj.getMonth();
		args[8] = beanObj.getUwYear();
		args[9] = beanObj.getUnderwriter();
		args[10] = beanObj.getInceptionDate();
		args[11] = beanObj.getExpiryDate();
		args[12] = beanObj.getAcceptanceDate();
		args[13] = beanObj.getOrginalCurrency();
		args[14] = beanObj.getExchangeRate();
		args[15] = "0";
		args[16] = beanObj.getPnoc();
		args[17] = beanObj.getRiskCovered();
		args[18] = beanObj.getTerritoryscope();
		args[19] = StringUtils.isBlank(beanObj.getTerritory())?"":beanObj.getTerritory();
		args[20] = beanObj.getProStatus();
		args[21] = beanObj.getProposalType();
		args[22] = beanObj.getAccountingPeriod();
		args[23] = beanObj.getReceiptofStatements();
		args[24] = beanObj.getReceiptofPayment();
		args[25] = StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? "0"	: beanObj.getMdInstalmentNumber();
		args[26] = StringUtils.isEmpty(beanObj.getNoRetroCess()) ? "0": beanObj.getNoRetroCess();
		args[27] = StringUtils.isEmpty(beanObj.getRetroType()) ? "0": beanObj.getRetroType();
		args[28] = StringUtils.isEmpty(beanObj.getInsuredName()) ? "": beanObj.getInsuredName();
		args[29]=StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType();
		args[30]=StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType();
		args[31]=StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType();
		args[32]=StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType();
		args[33]=StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered();
		args[34]=StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued();
		args[35]=StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL();
		args[36] = beanObj.getLoginId();
		args[37] = beanObj.getBranchCode();
		args[38] = beanObj.getCountryIncludedList();
		args[39] = beanObj.getCountryExcludedList();
		args[40] = StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", "");
		args[41] =StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype();
		args[42] =StringUtils.isEmpty(beanObj.getRunoffYear()) ? "0"	:beanObj.getRunoffYear();
		args[43] =StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName();
		args[44] =StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? ""	:beanObj.getLocCreditPrd();
		args[45] =StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? ""	:beanObj.getLocCreditAmt().replaceAll(",", "");
		args[46] =StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName();
		args[47] = "";
		args[48]="";
		args[49]=StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN();
		args[50] = beanObj.getAccountingPeriodNotes();
		args[51] =beanObj.getStatementConfirm();
		args[52] = beanObj.getProposalNo();
		args[53	]=endNo;
//		args[53]=beanObj.getProductId();
		return args;
	}
	private boolean updateRiskProposal(final FirstpageSaveReq beanObj) {
		boolean saveFlag = false;
		try {
			int res=0;
			String[] obj=null;
			obj = updateRiskProposalArgs(beanObj,getMaxAmednIdPro(beanObj.getProposalNo()));
			//risk.update.pro24FirPageRskPro
			TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalUpdate(obj);
			if(update!=null) {
			ttrnRiskProposalRepository.saveAndFlush(update);	
			res=1;
			}
		
			if (res> 0) {
				saveFlag = true;
			}
			updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
			obj = updateHomePostion(beanObj,true);
			//risk.update.positionMaster
			PositionMaster update1 = proportionalityCustomRepository.positionMasterUpdate(obj);
			if(update1!=null) {
				positionMasterRepository.saveAndFlush(update1);	
				res=1;
			}
			if (res > 0) {
				saveFlag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
		return saveFlag;
	}
	public String[] updateRiskProposalArgs(final FirstpageSaveReq beanObj,String endNo) {
		String[] obj=null;
		obj = new String[51];
		if( beanObj.getTreatyType().equalsIgnoreCase("4") ||  beanObj.getTreatyType().equalsIgnoreCase("5") ){
			obj[0] = beanObj.getFaclimitOrigCur();
			obj[1] = getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchangeRate());
		
		}
		else{
			obj[0] = beanObj.getLimitOrigCur();
			obj[1] = getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchangeRate());
		}
		obj[2] = beanObj.getEpiorigCur();
		obj[3] = getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchangeRate());
		obj[4] = StringUtils.isBlank(beanObj.getOurEstimate())?"0":beanObj.getOurEstimate();
		obj[5] = StringUtils.isBlank(beanObj.getEpi())?"0":beanObj.getEpi();
		obj[6] = getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[7] = StringUtils.isBlank(beanObj.getXlCost())?"":beanObj.getXlCost();
		obj[8] = getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[9] =StringUtils.isEmpty(beanObj.getCedRetent())?"":beanObj.getCedRetent();
		obj[10] = beanObj.getShareWritten();
		if (beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[11] = "0";
		} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
			obj[11] = beanObj.getSharSign();
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[11] = "0";
		} else {
			obj[11] = "0";
		}
		obj[12] = StringUtils.isEmpty(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		obj[13] = StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro();
		obj[14] = StringUtils.isEmpty(beanObj.getNoInsurer()) ? "0" : beanObj.getNoInsurer();
		obj[15] = StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? "0" : beanObj.getMaxLimitProduct();
		obj[16] =StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[17] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[18] =StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[19] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[20] =StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? "0": beanObj.getEpiAsPerShare();
		obj[21] = StringUtils.isEmpty(beanObj.getEpiAsPerShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate());
		obj[22] =StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? "0": beanObj.getXlcostOurShare();
		obj[23] = StringUtils.isEmpty(beanObj.getXlcostOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate());
		obj[24] =StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? "0": beanObj.getLimitPerVesselOC();
		obj[25] = StringUtils.isEmpty(beanObj.getLimitPerVesselOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchangeRate());
		obj[26] =StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? "0"	: beanObj.getLimitPerLocationOC();
		obj[27] = StringUtils.isEmpty(beanObj.getLimitPerLocationOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchangeRate());
		obj[28] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? "0": beanObj.getTreatyLimitsurplusOC();
		obj[29] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchangeRate());
		obj[30] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? "0": beanObj.getTreatyLimitsurplusOurShare();
		obj[31] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchangeRate());
		obj[32] =StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml();
		obj[33] = StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchangeRate());
		obj[34] =StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0": beanObj.getLimitOrigCurPmlOS();
		obj[35] = StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchangeRate());
		obj[36] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0": beanObj.getTreatyLimitsurplusOCPml();
		obj[37] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchangeRate());
		obj[38] =StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0": beanObj.getTreatyLimitsurplusOCPmlOS();
		obj[39] = StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchangeRate());
		obj[40] =StringUtils.isEmpty(beanObj.getEpipml()) ? "0": beanObj.getEpipml();
		obj[41] = StringUtils.isEmpty(beanObj.getEpipml())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipml(), beanObj.getExchangeRate());
		obj[42] =StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0": beanObj.getEpipmlOS();
		obj[43] = StringUtils.isEmpty(beanObj.getEpipmlOS())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchangeRate());
		obj[44]=beanObj.getDepartmentId();
		obj[45]=beanObj.getLoginId();
		obj[46]=beanObj.getBranchCode();
		obj[47]=StringUtils.isEmpty(beanObj.getPml()) ? "": beanObj.getPml();
		obj[48]=StringUtils.isEmpty(beanObj.getPmlPercent()) ? "0.00": beanObj.getPmlPercent();
		obj[49] = beanObj.getProposalNo();
		obj[50]=endNo;
		return obj;
	}
	public GetRemarksDetailsRes GetRemarksDetails(String proposalNo,String layerNo) {
		GetRemarksDetailsRes resp=new GetRemarksDetailsRes();
		try {
			
			List<RemarksReq> remarksres=new ArrayList<RemarksReq>();
			//GET_REMARKS_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskRemarks> rd = query.from(TtrnRiskRemarks.class);

			query.multiselect(rd.get("rskSNo").alias("RSK_SNO"),rd.get("rskDescription").alias("RSK_DESCRIPTION"),
					rd.get("rskRemark1").alias("RSK_REMARK1"),rd.get("rskRemark2").alias("RSK_REMARK2")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiskRemarks> rds = amend.from(TtrnRiskRemarks.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal( rds.get("proposalNo"), rd.get("proposalNo"));
			amend.where(a1);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("rskSNo")));
			
			Predicate n1 = cb.equal(rd.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rd.get("layerNo"), layerNo);
			Predicate n4 = cb.equal(rd.get("amendId"), amend);
			query.where(n1,n2,n4).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			
			if(list!=null && list.size()>0){
				RemarksReq res=new RemarksReq();
				for (int i = 0; i < list.size(); i++) {
					Tuple insMap = list.get(i);
					res.setDescription(insMap.get("RSK_DESCRIPTION")==null?"Remarks":insMap.get("RSK_DESCRIPTION").toString());
					res.setRemark1(insMap.get("RSK_REMARK1")==null?" ":insMap.get("RSK_REMARK1").toString());
					res.setRemark2(insMap.get("RSK_REMARK2")==null?"":insMap.get("RSK_REMARK2").toString());
					res.setRemarkSNo(insMap.get("RSK_SNO")==null?"":insMap.get("RSK_SNO").toString());
					remarksres.add(res);
				}
			}else{
				RemarksReq res=new RemarksReq();
				remarksres.add(res);
			}
			resp.setRemarksReq(remarksres);
			resp.setIsError(false);
			resp.setMessage("Success");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setIsError(true);
			resp.setMessage("Failed");
		}
		return resp;
		
	}
	public String[] updateHomePostion(final FirstpageSaveReq beanObj, final boolean bool) {

		String[] obj = new String[23]; //Ri
		obj[0] = StringUtils.isEmpty(beanObj.getLayerNo()) ? "0" : beanObj.getLayerNo();
		obj[1] = "";
		obj[2] = beanObj.getProductId();
		obj[3] = beanObj.getDepartmentId();
		obj[4] = beanObj.getCedingCo();
		obj[5] = beanObj.getUwYear();
		obj[6] = beanObj.getMonth();
		obj[7] = beanObj.getAcceptanceDate();
		obj[8] = beanObj.getInceptionDate();
		obj[9] = beanObj.getExpiryDate();
		obj[10] = beanObj.getProStatus().trim();
		if(beanObj.getContractNo()==null || beanObj.getContractNo().equalsIgnoreCase("") ){
			/*if(beanObj.getProStatus().equalsIgnoreCase("P") || beanObj.getProStatus().equalsIgnoreCase("A")){
				obj[11] ="P"; 
			}*/
			if(beanObj.getProStatus().equalsIgnoreCase("P"))
			{
				obj[11] ="P"; 
			}
			else if(beanObj.getProStatus().equalsIgnoreCase("A"))
			{
				obj[11] ="P";
			}
			else if(beanObj.getProStatus().equalsIgnoreCase("R")){
				obj[11] ="R";
			}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
				obj[11] ="N";
			}
		}
		else{
			obj[11] =beanObj.getProStatus().trim();
		}
		obj[12] =StringUtils.isBlank(beanObj.getBroker())?"63":beanObj.getBroker();
		obj[13] = StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType();
		obj[14] = beanObj.getLoginId();
		obj[15]="";
		
		obj[16] =  StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal();
		obj[17] = beanObj.getBouquetModeYN(); //Ri
		obj[18] = beanObj.getBouquetNo();
		obj[19] = beanObj.getUwYearTo();
		obj[20] = beanObj.getSectionNo();
		obj[21] = beanObj.getProposalNo();
		obj[22] =beanObj.getAmendId();
		return obj;
	}
	@Override
	public SecondpagesaveRes saveSecondPage(SecondpageSaveReq req) {
		SecondpagesaveRes resp=new SecondpagesaveRes();
		SecondpagesaveResp response=new SecondpagesaveResp();
		try{
			String GetProposalStatus = null;
			int ChkSecPagMod = checkSecondPageMode(req.getProposalNo());
			String[] obj=null,obj1=null;
			if (ChkSecPagMod == 1) {
			//if (ChkSecPagMod == 2) {
				obj = saveUpdateRiskDetailsSecondForm(req,getMaxAmednId(req.getProposalNo()));
				//risk.update.pro24RskProposal
				TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalSecondPageUpdate(obj);
				if(update!=null) {
				ttrnRiskProposalRepository.saveAndFlush(update);	
				}
				//risk.select.maxRskStatus
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);

				query.multiselect(rd.get("rskStatus").alias("RSK_STATUS")); 

				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRiskDetails> rds = end.from(TtrnRiskDetails.class);
				end.select(cb.max(rds.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
				end.where(a1);

				Predicate n1 = cb.equal(rd.get("rskProposalNumber"), req.getProposalNo());
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
				query.where(n1,n2);

				TypedQuery<String> result = em.createQuery(query);
				List<String> list = result.getResultList();
				if(!CollectionUtils.isEmpty(list)) {
					GetProposalStatus=list.get(0)==null?"":list.get(0);
				}
				
				//beanObj.setProStatus(GetProposalStatus);
				if(StringUtils.isNotBlank(req.getContractNo())) {
					response.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ req.getProposalNo());
				}
				else if ("A".equalsIgnoreCase(GetProposalStatus)||"P".equalsIgnoreCase(GetProposalStatus)) {
					response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ obj[16]);
				}else if ("N".equalsIgnoreCase(GetProposalStatus)) {
					response.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ obj[16]);
				}
				obj1 = savemodeUpdateRiskDetailsSecondFormSecondTable(req, getMaxAmednId(req.getProposalNo()));
				//risk.update.pro2SecComm
				TtrnRiskCommission update1 = proportionalityCustomRepository.ttrnRiskCommissionSecondPageUpdate(obj1);
				if(update1!=null) {
					ttrnRiskCommissionRepository.saveAndFlush(update1);	
				}
				
			}else {
				obj = secondPageFirstTableSaveAruguments(req,getMaxAmednId(req.getProposalNo()));
				//risk.update.pro24RskProposal
				TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalSecondPageUpdate(obj);
				if(update!=null) {
				ttrnRiskProposalRepository.saveAndFlush(update);	
				}
				obj1 = secondPageCommissionSaveAruguments(req);
				//risk.insert.pro2SecComm
				TtrnRiskCommission insert = proportionalityCustomRepository.ttrnRiskCommissionSecondPageInsert(obj1);
				if(insert!=null) {
					ttrnRiskCommissionRepository.saveAndFlush(insert);	
				}
				
				//risk.select.chechProposalStatus
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnRiskProposal> rd = query.from(TtrnRiskProposal.class);
				Root<TtrnRiskDetails> b = query.from(TtrnRiskDetails.class);

				query.multiselect(b.get("rskStatus").alias("RSK_STATUS"),rd.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO")); 

				Subquery<Long> endA = query.subquery(Long.class); 
				Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
				endA.select(cb.max(rds.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rds.get("rskProposalNumber"), req.getProposalNo());
				endA.where(a1);
				
				Subquery<Long> endB = query.subquery(Long.class); 
				Root<TtrnRiskProposal> bs = endB.from(TtrnRiskProposal.class);
				endB.select(cb.max(bs.get("rskEndorsementNo")));
				Predicate b1 = cb.equal( bs.get("rskProposalNumber"), req.getProposalNo());
				endB.where(b1);

				Predicate n1 = cb.equal(rd.get("rskProposalNumber"), req.getProposalNo());
				Predicate n3 = cb.equal(rd.get("rskProposalNumber"), b.get("rskProposalNumber"));
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
				Predicate n4 = cb.equal(b.get("rskEndorsementNo"), endB);
				query.where(n1,n2,n3,n4);

				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				
				if(!CollectionUtils.isEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						Tuple resMap= list.get(0);
						response.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
						response.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
						response.setContractNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
					}
				}
				
				if ("P".equals(response.getProStatus())) {
					response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo());
				} else if ("A".equals(response.getProStatus())) {
					response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo() );
				} else if ("R".equals(response.getProStatus())) {
					response.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalNo());
				}else if ("N".equals(response.getProStatus())) {
					response.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalNo() );
				} else if ("0".equals(response.getProStatus())) {
					response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo() );
				}
			}
			resp.setResp(response);
			resp.setMessage("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}
	public int checkSecondPageMode(String proposalNo) {
		int mode=0;
		try{
			//risk.select.getRiskCommCount
			int  count = ttrnRiskCommissionRepository.countByRskProposalNumber(proposalNo);
			if (count == 0) {
				mode = 1;
			} else if (count != 0) {
				mode = 2;
			}		}
		catch(Exception e){		
			e.printStackTrace();	
		}
		return mode;
	}
	public String[] saveUpdateRiskDetailsSecondForm(final SecondpageSaveReq beanObj, final String endNo) {
		String[] obj=null;
		obj = new String[18];
		obj[0] = beanObj.getLimitOurShare();
		obj[1] = getDesginationCountry(beanObj.getLimitOurShare(), beanObj
				.getExchangeRate());
		obj[2] = beanObj.getEpiAsPerOffer();
		obj[3] = getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
				.getExchangeRate());
		obj[4] =StringUtils.isBlank(beanObj.getEpiAsPerShare())?"0": beanObj.getEpiAsPerShare();
		obj[5] = getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj
				.getExchangeRate());
		obj[6] ="";
		obj[7] = "";
		obj[8] = StringUtils.isBlank(beanObj.getPremiumQuotaShare())?"0":beanObj.getPremiumQuotaShare();
		obj[9] = StringUtils.isBlank(beanObj.getPremiumSurplus())?"0":beanObj.getPremiumSurplus();
		obj[10]= StringUtils.isEmpty(beanObj.getPremiumQuotaShare())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj
						.getExchangeRate());
		obj[11]= StringUtils.isEmpty(beanObj.getPremiumSurplus())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getPremiumSurplus(), beanObj
						.getExchangeRate());
		obj[12]=StringUtils.isBlank(beanObj.getCommissionQSAmt())?"0":beanObj.getCommissionQSAmt();
		obj[13]=StringUtils.isBlank(beanObj.getCommissionsurpAmt())?"0":beanObj.getCommissionsurpAmt();
		obj[14]= StringUtils.isEmpty(beanObj.getCommissionQSAmt())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj
						.getExchangeRate());
		obj[15]= StringUtils.isEmpty(beanObj.getCommissionsurpAmt())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj
						.getExchangeRate());
		obj[16] = beanObj.getProposalNo();
		obj[17] = endNo;
		return obj;
	}
	public String[] savemodeUpdateRiskDetailsSecondFormSecondTable(final SecondpageSaveReq beanObj, final String endNo) {
		String[] obj=new String[0];
		obj = new String[67];
		obj[0] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0": beanObj.getBrokerage();
		obj[1] = StringUtils.isEmpty(beanObj.getTax()) ? "0" : beanObj.getTax();
		obj[2] = StringUtils.isEmpty(beanObj.getShareProfitCommission()) ? "0": beanObj.getShareProfitCommission();
		obj[3] = StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? "0": beanObj.getAcquisitionCost();
		obj[4] = StringUtils.isEmpty(beanObj.getAcquisitionCost())	|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getAcquisitionCost(),beanObj.getExchangeRate());
		obj[5] = StringUtils.isEmpty(beanObj.getCommissionQS()) ? "0": beanObj.getCommissionQS();
		obj[6] = StringUtils.isEmpty(beanObj.getCommissionsurp()) ? "0": beanObj.getCommissionsurp();
		obj[7] = StringUtils.isEmpty(beanObj.getOverRidder()) ? "0"	: beanObj.getOverRidder();
		//obj[8] = StringUtils.isEmpty(beanObj.getManagement_Expenses()) ? "0": beanObj.getManagement_Expenses();
		//obj[9] = StringUtils.isEmpty(beanObj.getLossC_F()) ? "0" : beanObj.getLossC_F();		
		obj[8] = StringUtils.isEmpty(beanObj.getPremiumReserve()) ? "0": beanObj.getPremiumReserve();
		obj[9] = StringUtils.isEmpty(beanObj.getLossreserve()) ? "0": beanObj.getLossreserve();
		obj[10] = StringUtils.isEmpty(beanObj.getInterest()) ? "0": beanObj.getInterest();
		obj[11] = StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ? "0": beanObj.getPortfolioinoutPremium();
		obj[12] = StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? "0": beanObj.getPortfolioinoutLoss();
		obj[13] = StringUtils.isEmpty(beanObj.getLossAdvise()) ? "0": beanObj.getLossAdvise();
		obj[14] = StringUtils.isEmpty(beanObj.getCashLossLimit()) ? "0": beanObj.getCashLossLimit();
		obj[15] = StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getCashLossLimit(),beanObj.getExchangeRate());
		obj[16] = StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "": beanObj.getLeaderUnderwriter();
		obj[17] = StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0": beanObj.getLeaderUnderwritershare();
		obj[18] = beanObj.getAccounts();
		obj[19] = beanObj.getExclusion();
		obj[20] = StringUtils.isEmpty(beanObj.getRemarks())?"":beanObj.getRemarks();
		obj[21] = beanObj.getUnderwriterRecommendations();
		obj[22] = beanObj.getGmsApproval();
		//obj[25] = StringUtils.isEmpty(beanObj.getProfit_commission()) ? "0": beanObj.getProfit_commission();
		obj[23] = StringUtils.isEmpty(beanObj.getOthercost()) ? "0": beanObj.getOthercost();
		obj[24] = beanObj.getCrestaStatus();
		obj[25] = beanObj.getEventlimit();
		obj[26] =getDesginationCountry(beanObj.getEventlimit(),beanObj.getExchangeRate());
		obj[27] = beanObj.getAggregateLimit();
		obj[28] =getDesginationCountry(beanObj.getAggregateLimit(),beanObj.getExchangeRate());
		obj[29] = beanObj.getOccurrentLimit();
		obj[30] =getDesginationCountry(beanObj.getOccurrentLimit(),beanObj.getExchangeRate());
		obj[31] = beanObj.getSlideScaleCommission();
		obj[32] = beanObj.getLossParticipants();
		obj[33] = StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "": beanObj.getCommissionSubClass();
		obj[34] = beanObj.getDepartmentId();
		obj[35] = beanObj.getLoginId();
		obj[36] = beanObj.getBranchCode();
		obj[37] = StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry())?"":beanObj.getLeaderUnderwritercountry();
		obj[38] =StringUtils.isEmpty(beanObj.getOrginalacqcost())?"":beanObj.getOrginalacqcost();
		obj[39] = StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost())?"":beanObj.getOurassessmentorginalacqcost();
		obj[40] = StringUtils.isEmpty(beanObj.getOuracqCost())?"":beanObj.getOuracqCost();
		obj[41] = StringUtils.isEmpty(beanObj.getLosscommissionSubClass())?"":beanObj.getLosscommissionSubClass();
		obj[42] = StringUtils.isEmpty(beanObj.getSlidecommissionSubClass())?"":beanObj.getSlidecommissionSubClass();
		obj[43] = StringUtils.isEmpty(beanObj.getCrestacommissionSubClass())?"":beanObj.getCrestacommissionSubClass();
		if("1".equalsIgnoreCase(beanObj.getShareProfitCommission())){
			obj[44] = StringUtils.isEmpty(beanObj.getManagementExpenses())?"":beanObj.getManagementExpenses();
			obj[45] = StringUtils.isEmpty(beanObj.getCommissionType())?"":beanObj.getCommissionType();
			obj[46] = StringUtils.isEmpty(beanObj.getProfitCommissionPer())?"":beanObj.getProfitCommissionPer();
			obj[47] = StringUtils.isEmpty(beanObj.getSetup())?"":beanObj.getSetup();
			obj[48] = StringUtils.isEmpty(beanObj.getSuperProfitCommission())?"":beanObj.getSuperProfitCommission();
			obj[49] = StringUtils.isEmpty(beanObj.getLossCarried())?"":beanObj.getLossCarried();
			obj[50] = StringUtils.isEmpty(beanObj.getLossyear())?"":beanObj.getLossyear();
			obj[51] = StringUtils.isEmpty(beanObj.getProfitCarried())?"":beanObj.getProfitCarried();
			obj[52] = StringUtils.isEmpty(beanObj.getProfitCarriedForYear())?"":beanObj.getProfitCarriedForYear();
			obj[53] = StringUtils.isEmpty(beanObj.getFistpc())?"":beanObj.getFistpc();
			obj[54] = StringUtils.isEmpty(beanObj.getProfitMont())?"":beanObj.getProfitMont();
			obj[55] = StringUtils.isEmpty(beanObj.getSubProfitMonth())?"":beanObj.getSubProfitMonth();
			obj[56] = StringUtils.isEmpty(beanObj.getSubpc())?"":beanObj.getSubpc();
			obj[57] = StringUtils.isEmpty(beanObj.getSubSeqCalculation())?"":beanObj.getSubSeqCalculation();
			obj[58] = StringUtils.isEmpty(beanObj.getProfitCommission())?"":beanObj.getProfitCommission();
			}
			else{
				obj[44] = "";
				obj[45] =  "";
				obj[46] =  "";
				obj[47] =  "";
				obj[48] =  "";
				obj[49] =  "";
				obj[50] =  "";
				obj[51] =  "";
				obj[52] =  "";
				obj[53] =  "";
				obj[54] =  "";
				obj[55] =  "";
				obj[56] =  "";
				obj[57] =  "";
				obj[58] =  "";
			}
		obj[59] = StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
		obj[60] = StringUtils.isEmpty(beanObj.getLocRate())? "" :beanObj.getLocRate();
		obj[61] =StringUtils.isEmpty(beanObj.getPremiumResType())? "" :beanObj.getPremiumResType();
		obj[62] = StringUtils.isEmpty(beanObj.getPcfpcType())?"":beanObj.getPcfpcType();
		obj[63] = StringUtils.isEmpty(beanObj.getPcfixedDate())?"":beanObj.getPcfixedDate();
		obj[64] = StringUtils.isEmpty(beanObj.getPortfolioType())?"":beanObj.getPortfolioType();
		obj[65] = beanObj.getProposalNo();
		obj[66] = endNo;
		return obj;
	}
	public String[] secondPageFirstTableSaveAruguments(final SecondpageSaveReq beanObj, final String endNo) {
		String[] obj=null;
		obj = new String[18];
		obj[0] = beanObj.getLimitOurShare();
		obj[1] = getDesginationCountry(beanObj.getLimitOurShare(), beanObj
				.getExchangeRate());
		obj[2] = beanObj.getEpiAsPerOffer();
		obj[3] = getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
				.getExchangeRate());
		obj[4] =StringUtils.isBlank(beanObj.getEpiAsPerShare())?"0": beanObj.getEpiAsPerShare();
		obj[5] = getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj
				.getExchangeRate());
		obj[6] = "";
		obj[7] = "";
		obj[8] = StringUtils.isBlank(beanObj.getPremiumQuotaShare())?"0":beanObj.getPremiumQuotaShare();
		obj[9] = StringUtils.isBlank(beanObj.getPremiumSurplus())?"0":beanObj.getPremiumSurplus();
		obj[10]= StringUtils.isEmpty(beanObj.getPremiumQuotaShare())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj
						.getExchangeRate());
		obj[11]= StringUtils.isEmpty(beanObj.getPremiumSurplus())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getPremiumSurplus(), beanObj
						.getExchangeRate());
		obj[12]=StringUtils.isBlank(beanObj.getCommissionQSAmt())?"0":beanObj.getCommissionQSAmt();
		obj[13]=StringUtils.isBlank(beanObj.getCommissionsurpAmt())?"0":beanObj.getCommissionsurpAmt();
		obj[14]= StringUtils.isEmpty(beanObj.getCommissionQSAmt())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj
						.getExchangeRate());
		obj[15]= StringUtils.isEmpty(beanObj.getCommissionsurpAmt())
		|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
				: getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj
						.getExchangeRate());
		obj[16] = beanObj.getProposalNo();
		obj[17] = endNo;
		return obj;
	}
	public String[] secondPageCommissionSaveAruguments(final SecondpageSaveReq beanObj) {
		String[] obj=null;
		obj = new String[71]; //Ri
		obj[0] = beanObj.getProposalNo();
		obj[1] = "0";
		obj[2] = "0";
		obj[3] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0": beanObj.getBrokerage();
		obj[4] = StringUtils.isEmpty(beanObj.getTax()) ? "0" : beanObj.getTax();
		obj[5] = StringUtils.isEmpty(beanObj.getShareProfitCommission()) ? "0": beanObj.getShareProfitCommission();
		obj[6] = "0";
		obj[7] = StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? "0": beanObj.getAcquisitionCost();
		obj[8] = StringUtils.isEmpty(beanObj.getAcquisitionCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAcquisitionCost(),beanObj.getExchangeRate());
		obj[9] = StringUtils.isEmpty(beanObj.getCommissionQS()) ? "0": beanObj.getCommissionQS();
		obj[10] = StringUtils.isEmpty(beanObj.getCommissionsurp()) ? "0": beanObj.getCommissionsurp();
		obj[11] = StringUtils.isEmpty(beanObj.getOverRidder()) ? "0": beanObj.getOverRidder();
		//obj[12] = StringUtils.isEmpty(beanObj.getManagement_Expenses()) ? "0": beanObj.getManagement_Expenses();
		//obj[13] = StringUtils.isEmpty(beanObj.getLossC_F()) ? "0" : beanObj.getLossC_F();

		obj[12] = StringUtils.isEmpty(beanObj.getPremiumReserve()) ? "0": beanObj.getPremiumReserve();
		obj[13] = StringUtils.isEmpty(beanObj.getLossreserve()) ? "0": beanObj.getLossreserve();
		obj[14] = StringUtils.isEmpty(beanObj.getInterest()) ? "0": beanObj.getInterest();
		obj[15] = StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ? "0": beanObj.getPortfolioinoutPremium();
		obj[16] = StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? "0": beanObj.getPortfolioinoutLoss();
		obj[17] = StringUtils.isEmpty(beanObj.getLossAdvise()) ? "0": beanObj.getLossAdvise();
		obj[18] = StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": beanObj.getCashLossLimit();
		obj[19] = StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getCashLossLimit(),beanObj.getExchangeRate());
		obj[20] = StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "0": beanObj.getLeaderUnderwriter();
		obj[21] = StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0": beanObj.getLeaderUnderwritershare();
		obj[22] = StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts();
		obj[23] = StringUtils.isEmpty(beanObj.getExclusion()) ? "": beanObj.getExclusion();
		obj[24] = StringUtils.isEmpty(beanObj.getRemarks()) ? "" : beanObj.getRemarks();
		obj[25] = StringUtils.isEmpty(beanObj.getUnderwriterRecommendations()) ? "" : beanObj.getUnderwriterRecommendations();
		obj[26] = StringUtils.isEmpty(beanObj.getGmsApproval()) ? "": beanObj.getGmsApproval();
		obj[27] = "";
		obj[28] = "";
		//obj[31] = StringUtils.isEmpty(beanObj.getProfit_commission()) ? "0": beanObj.getProfit_commission();
		obj[29] = StringUtils.isEmpty(beanObj.getOthercost()) ? "0": beanObj.getOthercost();
		obj[30] = beanObj.getCrestaStatus();
		obj[31] = beanObj.getEventlimit();
		obj[32] =getDesginationCountry(beanObj.getEventlimit(),beanObj.getExchangeRate());
		obj[33] = beanObj.getAggregateLimit();
		obj[34] =getDesginationCountry(beanObj.getAggregateLimit(),beanObj.getExchangeRate());
		obj[35] = beanObj.getOccurrentLimit();
		obj[36] =getDesginationCountry(beanObj.getOccurrentLimit(),beanObj.getExchangeRate());
		obj[37] = beanObj.getSlideScaleCommission();
		obj[38] = beanObj.getLossParticipants();
		obj[39] = StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "": beanObj.getCommissionSubClass();
		obj[40]=beanObj.getDepartmentId();
		obj[41] = beanObj.getLoginId();
		obj[42] = beanObj.getBranchCode();
		obj[43] = StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry())?"":beanObj.getLeaderUnderwritercountry();
		obj[44] =StringUtils.isEmpty(beanObj.getOrginalacqcost())?"":beanObj.getOrginalacqcost();
		obj[45] = StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost())?"":beanObj.getOurassessmentorginalacqcost();
		obj[46] = StringUtils.isEmpty(beanObj.getOuracqCost())?"":beanObj.getOuracqCost();

		obj[47] = StringUtils.isEmpty(beanObj.getLosscommissionSubClass())?"":beanObj.getLosscommissionSubClass();
		obj[48] = StringUtils.isEmpty(beanObj.getSlidecommissionSubClass())?"":beanObj.getSlidecommissionSubClass();
		obj[49] = StringUtils.isEmpty(beanObj.getCrestacommissionSubClass())?"":beanObj.getCrestacommissionSubClass();
		if("1".equalsIgnoreCase(beanObj.getShareProfitCommission())){
			obj[50] = StringUtils.isEmpty(beanObj.getManagementExpenses())?"":beanObj.getManagementExpenses();
			obj[51] = StringUtils.isEmpty(beanObj.getCommissionType())?"":beanObj.getCommissionType();
			obj[52] = StringUtils.isEmpty(beanObj.getProfitCommissionPer())?"":beanObj.getProfitCommissionPer();
			obj[53] = StringUtils.isEmpty(beanObj.getSetup())?"":beanObj.getSetup();
			obj[54] = StringUtils.isEmpty(beanObj.getSuperProfitCommission())?"":beanObj.getSuperProfitCommission();
			obj[55] = StringUtils.isEmpty(beanObj.getLossCarried())?"":beanObj.getLossCarried();
			obj[56] = StringUtils.isEmpty(beanObj.getLossyear())?"":beanObj.getLossyear();
			obj[57] = StringUtils.isEmpty(beanObj.getProfitCarried())?"":beanObj.getProfitCarried();
			obj[58] = StringUtils.isEmpty(beanObj.getProfitCarriedForYear())?"":beanObj.getProfitCarriedForYear();
			obj[59] = StringUtils.isEmpty(beanObj.getFistpc())?"":beanObj.getFistpc();
			obj[60] = StringUtils.isEmpty(beanObj.getProfitMont())?"":beanObj.getProfitMont();
			obj[61] = StringUtils.isEmpty(beanObj.getSubProfitMonth())?"":beanObj.getSubProfitMonth();
			obj[62] = StringUtils.isEmpty(beanObj.getSubpc())?"":beanObj.getSubpc();
			obj[63] = StringUtils.isEmpty(beanObj.getSubSeqCalculation())?"":beanObj.getSubSeqCalculation();
			obj[64] = StringUtils.isEmpty(beanObj.getProfitCommission())?"":beanObj.getProfitCommission();
			}
			else{
				obj[50] = "";
				obj[51] = "";
				obj[52] = "";
				obj[53] = "";
				obj[54] = "";
				obj[55] = "";
				obj[56] = "";
				obj[57] = "";
				obj[58] = "";
				obj[59] = "";
				obj[60] = "";
				obj[61] = "";
				obj[62] = "";
				obj[63] = "";
				obj[64] = "";
			}
		obj[65] =StringUtils.isEmpty(beanObj.getDocStatus())?"":beanObj.getDocStatus();
		obj[66] =StringUtils.isEmpty(beanObj.getLocRate())? "" :beanObj.getLocRate();
		obj[67] =StringUtils.isEmpty(beanObj.getPremiumResType())? "" :beanObj.getPremiumResType();
		obj[68] = StringUtils.isEmpty(beanObj.getPcfpcType())?"":beanObj.getPcfpcType();
		obj[69] = StringUtils.isEmpty(beanObj.getPcfixedDate())?"":beanObj.getPcfixedDate();
		obj[70] = StringUtils.isEmpty(beanObj.getPortfolioType())?"":beanObj.getPortfolioType();
		return obj;
	}
	@Transactional
	public CommonSaveRes insertRetroContracts(RetroSaveReq beanObj) {
		CommonSaveRes resp=new CommonSaveRes();
		int	res=0;
		try{
			final int LoopCount = Integer.parseInt(beanObj.getNoInsurer());
			String[] obj = new String[12];
			String endtNo=dropDowmImpl.getRiskComMaxAmendId(beanObj.getProposalNo());
		
			//delete.facul.data
			 ttrnInsurerDetailsRepository.deleteByProposalNoAndEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endtNo));
			
			if(LoopCount==0){
				beanObj.setRetentionPercentage("100");
			}
			if(Integer.parseInt(endtNo)>0){
				//fac.update.insDetails
				res= proportionalityCustomRepository.facUpdateInsDetails(beanObj.getProposalNo(),"0");
			}
			obj[0] = "0";
			obj[1] = beanObj.getProposalNo();
			obj[2] = "";
			obj[3] = endtNo;
			obj[4] = "R";
			obj[5] = StringUtils.isEmpty(beanObj.getRetentionPercentage())? "0" :beanObj.getRetentionPercentage();
			obj[6] = "Y";
			obj[7] = "";
			obj[8] = "";
			obj[9] = beanObj.getDepartmentId();
			obj[10] = beanObj.getLoginId();
			obj[11] = beanObj.getBranchCode();
			//fac.insert.insDetails
			TtrnInsurerDetails insert = proportionalityCustomRepository.facInsertInsDetails(obj);
			if(insert!=null) {
			ttrnInsurerDetailsRepository.saveAndFlush(insert);	
			res=1;
			}
			obj = new String[12];
			obj[0] = "1";
			obj[1] = beanObj.getProposalNo();
			obj[2] =  StringUtils.isEmpty(beanObj.getRetroDupContract()) ? "0" :beanObj.getRetroDupContract();
			obj[3] = endtNo;
			obj[4] = "C";
			obj[5] = StringUtils.isEmpty(beanObj.getRetentionPercentage())? "0" :beanObj.getRetentionPercentage();
			obj[6] = "Y";
			obj[7] = StringUtils.isEmpty(beanObj.getRetroDupYerar())? "0" :beanObj.getRetroDupYerar();
			obj[8] = StringUtils.isEmpty(beanObj.getRetroType())? "" :beanObj.getRetroType();
			obj[9] = beanObj.getDepartmentId();
			obj[10] = beanObj.getLoginId();
			obj[11] = beanObj.getBranchCode();
			//fac.insert.insDetails
			TtrnInsurerDetails insert1 = proportionalityCustomRepository.facInsertInsDetails(obj);
			if(insert1!=null) {
			ttrnInsurerDetailsRepository.saveAndFlush(insert1);	
			res=1;
			}
			int j=2;
			if(beanObj.getRetroDetailReq()!=null && beanObj.getRetroDetailReq().size()>0) {
			for(int i=0;i<beanObj.getRetroDetailReq().size();i++){
				RetroDetailReq req=beanObj.getRetroDetailReq().get(i);
				obj[0] = String.valueOf(j);
				obj[1] = beanObj.getProposalNo();
				obj[2] = StringUtils.isEmpty(req.getRetroCeding()) ? "0" :req.getRetroCeding();
				obj[3] = endtNo;
				obj[4] = "C";
				obj[5] = StringUtils.isEmpty(req.getPercentRetro())? "0" :req.getPercentRetro();
				obj[6] = "Y";
				obj[7] = StringUtils.isEmpty(req.getRetroYear())? "0" :req.getRetroYear();
				obj[8] = StringUtils.isEmpty(beanObj.getRetroType())? "" :beanObj.getRetroType();
				obj[9] = beanObj.getDepartmentId();
				obj[10] = beanObj.getLoginId();
				obj[11] = beanObj.getBranchCode();
				//fac.insert.insDetails
				TtrnInsurerDetails insert2 = proportionalityCustomRepository.facInsertInsDetails(obj);
				if(insert2!=null) {
				ttrnInsurerDetailsRepository.saveAndFlush(insert2);	
				res=1;
				}
				j++;
			}
		}
			resp.setResponse("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			resp.setResponse("Success");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}
private void deleteByProposalNoAndEndorsementNo(String proposalNo, BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		
	}
//	public String getRiskComMaxAmendId(final String proposalNo) {
//		String result="";
//		try{
//			List<Map<String, Object>> list  = queryImpl.selectList("risk.select.getRiskComMaxAmendId",new String[] {proposalNo});
//			if(!CollectionUtils.isEmpty(list)) {
//				result=list.get(0).get("RSK_ENDORSEMENT_NO")==null?"":list.get(0).get("RSK_ENDORSEMENT_NO").toString();
//			}
//		}catch(Exception e)
//		{
//			logger.debug("Exception @ {" + e + "}");
//		}
//
//		return result;
//	}
	public CommonSaveRes insertCrestaMaintable(CrestaSaveReq bean) {
		CommonSaveRes resp=new CommonSaveRes();
		String obj[] =null;
		try {
			if(StringUtils.isBlank(bean.getAmendId())){
				bean.setAmendId("0");
			}
			int count=getCrestaCount(bean);
			if(count<=0){
				    obj = new String[12];
					obj[0]=bean.getContractNo();
					obj[1]=bean.getProposalNo();
					obj[2]=bean.getAmendId();
					obj[3]=bean.getDepartmentId();
					obj[4]="";
					obj[5]="";
					obj[6]="";
					obj[7]="";
					obj[8]="";
					obj[9]=bean.getBranchCode();
					obj[10]="";
					obj[11]= "";
					//MOVE_TO_CRESTA_MAIN_TABLE
					TtrnCrestazoneDetails insert = proportionalityCustomRepository.moveToCrestaMainTable(obj);
					if(insert!=null) {
						ttrnCrestazoneDetailsRepository.saveAndFlush(insert);
						}
			}
			 obj = new String[4];
			 obj[0]=bean.getContractNo();
			 obj[1]=bean.getProposalNo();
			 obj[2]=bean.getAmendId();
			 obj[3]=bean.getBranchCode();
			 //CREATA_CONTRACT_UPDATE
			 TtrnCrestazoneDetails list = ttrnCrestazoneDetailsRepository.findByProposalNoAndAmendIdAndBranchCode(new BigDecimal(bean.getProposalNo()),bean.getAmendId(),bean.getBranchCode());
			 if(list!= null) {
				 list.setContractNo(new BigDecimal(bean.getContractNo()));	
				 ttrnCrestazoneDetailsRepository.saveAndFlush(list);
				 }
			 resp.setResponse("Success");
			 resp.setErroCode(0);
			 resp.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
		
	}
	public int getCrestaCount(CrestaSaveReq bean) {
		int count=0;
		try {
			//GET_CRESTA_DETAIL_COUNT
			count = ttrnCrestazoneDetailsRepository.countByProposalNoAndAmendIdAndBranchCode(new BigDecimal(bean.getProposalNo()), bean.getAmendId(), bean.getBranchCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Transactional
	@Override
	public CommonSaveRes insertBonusDetails(BonusSaveReq req, String type) {
		CommonSaveRes resp=new CommonSaveRes();
		try {
			if("scale".equalsIgnoreCase(type) && "Y".equalsIgnoreCase(req.getSlideScaleCommission()) ){
				updateContractDetails(req,"scale");
			}
			else if("Y".equalsIgnoreCase(req.getLossParticipants()) && "lossparticipate".equalsIgnoreCase(type)){
				updateContractDetails(req,"lossparticipate");
			}
			else{
				moveBonusEmptyData(req,type);
			}
			 resp.setResponse("Success");
			 resp.setErroCode(0);
			 resp.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
		
	}

	private void updateContractDetails(BonusSaveReq bean, String string) {
		try{
		    //CONTRACT_UPDATE
		    CriteriaBuilder cb = this.em.getCriteriaBuilder();
			 CriteriaUpdate<TtrnBonus> update = cb.createCriteriaUpdate(TtrnBonus.class);
			 Root<TtrnBonus> m = update.from(TtrnBonus.class);
			 update.set("contractNo", new BigDecimal(bean.getContractNo()));
			 //end
				Subquery<Long> end = update.subquery(Long.class);
				Root<TtrnBonus> rds = end.from(TtrnBonus.class);
				end.select(cb.max(rds.get("endorsementNo")));
				Predicate a1 = cb.equal(rds.get("proposalNo"), m.get("proposalNo"));
				Predicate a2 = cb.equal(rds.get("branch"), m.get("branch"));
				end.where(a1, a2);
			 Predicate n1 = cb.equal(m.get("proposalNo"), bean.getProposalNo());
			 Predicate n2 = cb.equal(m.get("branch"), bean.getBranchCode());
			 Predicate n3 = cb.equal(m.get("endorsementNo"), end==null?null:end);
			 update.where(n1,n2,n3 );
			 em.createQuery(update).executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void moveBonusEmptyData(BonusSaveReq bean,String type) {
		try{
			if(StringUtils.isBlank(bean.getAmendId())){
				bean.setAmendId("0");
			}
			deleteMaintable(bean,type);
			//BONUS_MAIN_INSERT_PTTY
			String args[]=new String[22]; //ri
					args[0] =bean.getProposalNo();
					args[1] = bean.getContractNo();
					args[2] = bean.getProductid();
		           args[3] = "";
		           args[4] = "";
		           args[5] = "";
		           args[6] = "";
		           args[7] = bean.getLoginId();
		           args[8] = bean.getBranchCode();
		           args[9] = "";
		           if("scale".equalsIgnoreCase(type)){
		        	   args[10] ="SSC";
		           }
		           else{
		        	   args[10]="LPC";
		           }
		           args[11] = bean.getAmendId();
		           args[12] = bean.getDepartmentId();
		           args[13] = StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
		           args[14] ="";
		           args[15] ="";
		           args[16] ="";
		           args[17] ="";
		           args[18] ="";
		           args[19] ="";
		           args[20] ="";
		           args[21]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();
		           TtrnBonus insert = proportionalityCustomRepository.bonusMainInsertPtty(args);
		           if(insert!=null) {
		        	   ttrnBonusRepository.saveAndFlush(insert);
		        	   }
	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Transactional
	private void deleteMaintable(BonusSaveReq bean,String type) {
		String arg[]=null;
		try{
			if("".equalsIgnoreCase(bean.getEndorsmentno())){
				//BONUS_MAIN_DELETE
				 arg = new String[4];
				 arg[0] = bean.getProposalNo();
				 arg[1] = bean.getBranchCode();
				 if("scale".equalsIgnoreCase(type)){
		        	   arg[2] ="SSC";
		           }
		           else{
		        	   arg[2]="LPC";
		           }
				  arg[3]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
				  ttrnBonusRepository.deleteByProposalNoAndBranchAndTypeAndLayerNo(new BigDecimal( arg[0]), arg[1], arg[2],arg[3]);
			}else if(StringUtils.isBlank(bean.getProposalNo())) {
				//BONUS_MAIN_DELETE3
				 arg = new String[4];
				 arg[0] = bean.getReferenceNo();
				 arg[1] = bean.getBranchCode();
				 if("scale".equalsIgnoreCase(bean.getPageFor())){
		        	   arg[2] ="SSC";
		           }
		           else{
		        	   arg[2]="LPC";
		           }
				  arg[3]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
				  ttrnBonusRepository.deleteByReferenceNoAndBranchAndTypeAndLayerNo(new BigDecimal(arg[0]), arg[1], arg[2],arg[3]);
			}	else{
			 //BONUS_MAIN_DELETE2
			 arg = new String[5];
			 arg[0] = bean.getProposalNo();
			 arg[1] = bean.getAmendId();
			 arg[2] = bean.getBranchCode();
			 if("scale".equalsIgnoreCase(type)){
	        	   arg[3] ="SSC";
	           }
	           else{
	        	   arg[3]="LPC";
	           }
			  arg[4]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
			  ttrnBonusRepository.deleteByProposalNoAndEndorsementNoAndBranchAndTypeAndLayerNo(new BigDecimal(arg[0]), new BigDecimal(arg[1]), arg[2],arg[3], arg[4]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public CommonSaveRes insertProfitCommission(ProfitCommissionSaveReq req) {
		CommonSaveRes resp=new CommonSaveRes();
		try {
			if(!"1".equalsIgnoreCase(req.getShareProfitCommission())){
				mainDelete(req);
				profitMainEmptyInsert(req);
			}
			profitUpdate(req);
			resp.setResponse("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}

	private void profitUpdate(ProfitCommissionSaveReq bean) {
		try{
			//COMMISSION_STATUS_UPDATE
			TtrnCommissionDetails list =ttrnCommissionDetailsRepository.findByProposalNoAndBranchCodeAndEndorsementNo(new BigDecimal(bean.getProposalNo()), bean.getBranchCode(), new BigDecimal(bean.getAmendId()))	;
			if(list!=null) {
				list.setContractNo(new BigDecimal(bean.getContractNo()));	
				list.setProfitComStatus(bean.getShareProfitCommission());
				ttrnCommissionDetailsRepository.saveAndFlush(list);
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void profitMainEmptyInsert(ProfitCommissionSaveReq bean) {
		try{
			//COMMISSION_INSERT
			String args[]=new String[12];
				args[0]="";
				args[1]="";
				args[2]="";
				args[3]="";
				args[4]=bean.getProposalNo();
				args[5]=bean.getContractNo();
				args[6]=bean.getAmendId();
				args[7]=bean.getProductid();
				args[8]=bean.getBranchCode();
				args[9]=bean.getDepartmentId();
				args[10]=bean.getCommissionType();
				args[11]=bean.getLoginId();
				TtrnCommissionDetails insert = proportionalityCustomRepository.commissionInsert(args);
						if(insert!=null) {
							ttrnCommissionDetailsRepository.saveAndFlush(insert);
							}
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	private void mainDelete(ProfitCommissionSaveReq bean) {
		try{
		if(StringUtils.isNotBlank(bean.getContractNo())){
			//COMMIOSSION_DELETE_CONTRACT
			 ttrnCommissionDetailsRepository.deleteByProposalNoAndBranchCodeAndEndorsementNoAndContractNo(
					 new BigDecimal(bean.getProposalNo()), bean.getBranchCode(), new BigDecimal(bean.getAmendId()), new BigDecimal(bean.getContractNo()));
		}
		else{
			//COMMIOSSION_DELETE
			 ttrnCommissionDetailsRepository.deleteByProposalNoAndBranchCodeAndEndorsementNo(
					 new BigDecimal(bean.getProposalNo()), bean.getBranchCode(), new BigDecimal(bean.getAmendId()));
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public GetCommonValueRes getShortname(String branchCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		String Short="";
		try {
			//GET_SHORT_NAME
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<TmasBranchMaster> tbm = query.from(TmasBranchMaster.class);
			//	shortName
			Subquery<String> shortName = query.subquery(String.class); 
			Root<CurrencyMaster> cm = shortName.from(CurrencyMaster.class);
			shortName.select(cm.get("shortName"));
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<CurrencyMaster> cms = amend.from(CurrencyMaster.class);
			amend.select(cb.max(cb.<Long>selectCase().when(cb.isNull(cms.get("amendId")), 0l)
					.otherwise(cms.get("amendId"))));
			Predicate b3 = cb.equal( cms.get("currencyId"), cm.get("currencyId"));
			Predicate b4 = cb.equal( cms.get("branchCode"), cm.get("branchCode"));
			amend.where(b3,b4);
			
			Predicate a1 = cb.equal( tbm.get("baseCurrencyId"), cm.get("currencyId"));
			Predicate a2 = cb.equal( tbm.get("branchCode"), cm.get("branchCode"));
			Predicate a3 = cb.equal( cm.get("amendId"), amend);
			shortName.where(a1,a2,a3);
			
			query.multiselect(shortName.alias("COUNTRY_SHORT_NAME")); 

			Predicate n1 = cb.equal(tbm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(tbm.get("status"), "Y");
			query.where(n1,n2);
			
			TypedQuery<String> reslt = em.createQuery(query);
			List<String> list = reslt.getResultList();
			if (!CollectionUtils.isEmpty(list)) {
				Short = list.get(0) == null ? ""
						: list.get(0);
			}
		response.setCommonResponse(Short);
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Override
	public GetCommonValueRes getEditMode(String proposalNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		int mode=0;
		String	string ="";
		try {
		
			//RISK_SELECT_GETRSKCONTRACTNO
			TtrnRiskDetails list = ttrnRiskDetailsRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if (list!= null) {
				string = list.getRskEndorsementNo()==null ? ""
						: list.getRskEndorsementNo().toString();
			}
			if ("0".equalsIgnoreCase(string)) {
				mode = 1;
			} else {
				mode = 2;
			}
			response.setCommonResponse(String.valueOf(mode));
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public RiskDetailsEditModeRes riskDetailsEditMode(RiskDetailsEditModeReq req) {
		RiskDetailsEditModeRes response = new RiskDetailsEditModeRes();
		List<Tuple> list = new ArrayList<>();
		try {
			String[] args = new String[3];
			if (req.getContractMode()) {
				args[0] = req.getContNo();
				args[1] = req.getContNo();
				args[2] = req.getContNo();
				//risk.select.getEditModeData1
				list = proportionalityCustomRepository.riskSelectGetEditModeData1(args);
			} else {
				args[0] = req.getProposalNo();
				args[1] = req.getProposalNo();
				args[2] = req.getProposalNo();
				//risk.select.getEditModeData2
				list = proportionalityCustomRepository.riskSelectGetEditModeData2(args);
			}
			RiskDetailsEditModeRes1 beanObj = new RiskDetailsEditModeRes1(); 
			Tuple resMap = null;
			if(list!=null && list.size()>0)
				resMap = list.get(0);
			if (resMap!=null) { //RI
				beanObj.setCedingCo(resMap.get("RSK_CEDINGID")==null?"":resMap.get("RSK_CEDINGID").toString());
				beanObj.setInceptionDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				beanObj.setExpiryDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				beanObj.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				beanObj.setUwYearTo(resMap.get("UW_YEAR_TO")==null?"":resMap.get("UW_YEAR_TO").toString());
				beanObj.setBouquetModeYN(resMap.get("BOUQUET_MODE_YN")==null?"N":resMap.get("BOUQUET_MODE_YN").toString());
				beanObj.setBouquetNo(resMap.get("BOUQUET_NO")==null?"":resMap.get("BOUQUET_NO").toString());
				beanObj.setOfferNo(resMap.get("OFFER_NO")==null?"":resMap.get("OFFER_NO").toString());
				if(StringUtils.isBlank(req.getSectionMode())) {
				beanObj.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());	
				
				beanObj.setContractListVal(resMap.get("DATA_MAP_CONT_NO")==null?"":resMap.get("DATA_MAP_CONT_NO").toString());
				beanObj.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
				if(StringUtils.isBlank(beanObj.getBaseLayer()))
				beanObj.setBaseLayer(resMap.get("BASE_LAYER")==null?"":resMap.get("BASE_LAYER").toString());
				beanObj.setEndorsmentNo(resMap.get("RSK_ENDORSEMENT_NO")==null?"":resMap.get("RSK_ENDORSEMENT_NO").toString());
				beanObj.setContractNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				beanObj.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
				beanObj.setProductId(resMap.get("RSK_PRODUCTID")==null?"":resMap.get("RSK_PRODUCTID").toString());
				beanObj.setDepartmentId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				beanObj.setProfitCenter(resMap.get("RSK_PFCID")==null?"":resMap.get("RSK_PFCID").toString());
				beanObj.setSubProfitcenter(resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString());
				beanObj.setPolicyBranch(resMap.get("RSK_POLBRANCH")==null?"":resMap.get("RSK_POLBRANCH").toString());
				beanObj.setCedingCo(resMap.get("RSK_CEDINGID")==null?"":resMap.get("RSK_CEDINGID").toString());
				beanObj.setBroker(resMap.get("RSK_BROKERID")==null?"":resMap.get("RSK_BROKERID").toString());
				beanObj.setTreatyNametype(resMap.get("RSK_TREATYID")==null?"":resMap.get("RSK_TREATYID").toString());
				beanObj.setMonth(resMap.get("RSK_MONTH")==null?"":resMap.get("RSK_MONTH").toString());
				beanObj.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				beanObj.setUnderwriter(resMap.get("RSK_UNDERWRITTER")==null?"":resMap.get("RSK_UNDERWRITTER").toString());
				beanObj.setInceptionDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				beanObj.setExpiryDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				beanObj.setAcceptanceDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":resMap.get("RSK_ACCOUNT_DATE").toString());
				beanObj.setOrginalCurrency(resMap.get("RSK_ORIGINAL_CURR")==null?"":resMap.get("RSK_ORIGINAL_CURR").toString());
				beanObj.setExchangeRate(resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString().equalsIgnoreCase("0") ? "0"	: resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString());
				beanObj.setBasis(resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString());
				beanObj.setPnoc(resMap.get("RSK_PERIOD_OF_NOTICE")==null?"":resMap.get("RSK_PERIOD_OF_NOTICE").toString());
				beanObj.setRiskCovered(resMap.get("RSK_RISK_COVERED")==null?"":resMap.get("RSK_RISK_COVERED").toString());
				beanObj.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
				beanObj.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString()); //24
				beanObj.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
				beanObj.setEpiorigCur(resMap.get("RSK_EPI_OFFER_OC")==null?"":resMap.get("RSK_EPI_OFFER_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OFFER_OC").toString()==null?"":resMap.get("RSK_EPI_OFFER_OC").toString());
				beanObj.setPerilCovered(resMap.get("RSK_PERILS_COVERED")==null ? "" : resMap.get("RSK_PERILS_COVERED").toString());
				if(beanObj.getProductId().equalsIgnoreCase("2")){
					beanObj.setOurEstimate(resMap.get("RSK_EPI_ESTIMATE")==null?"":resMap.get("RSK_EPI_ESTIMATE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_ESTIMATE").toString()==null?"":resMap.get("RSK_EPI_ESTIMATE").toString());
				}
				if(beanObj.getProductId().equalsIgnoreCase("2")){
					beanObj.setEpi(resMap.get("RSK_EPI_EST_OC")==null?"":resMap.get("RSK_EPI_EST_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_EPI_EST_OC").toString()==null?"":resMap.get("RSK_EPI_EST_OC").toString());
				}
				beanObj.setXlCost(resMap.get("RSK_XLCOST_OC")==null?"":resMap.get("RSK_XLCOST_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_XLCOST_OC").toString()==null?"":resMap.get("RSK_XLCOST_OC").toString());
				if(beanObj.getProductId().equalsIgnoreCase("2")){
					beanObj.setCedRetent(resMap.get("RSK_CEDANT_RETENTION")==null?"":resMap.get("RSK_CEDANT_RETENTION").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_CEDANT_RETENTION").toString()==null?"":resMap.get("RSK_CEDANT_RETENTION").toString());
				}
				beanObj.setShareWritten(resMap.get("RSK_SHARE_WRITTEN")==null?"":resMap.get("RSK_SHARE_WRITTEN").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_SHARE_WRITTEN").toString()==null?"":resMap.get("RSK_SHARE_WRITTEN").toString());
				beanObj.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString().equalsIgnoreCase("0") ? "": resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
				beanObj.setProposalType(resMap.get("RSK_PROPOSAL_TYPE")==null?"":resMap.get("RSK_PROPOSAL_TYPE").toString());
				beanObj.setAccountingPeriod(resMap.get("RSK_ACCOUNTING_PERIOD")==null?"":resMap.get("RSK_ACCOUNTING_PERIOD").toString());
				beanObj.setReceiptofStatements(resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString());
				beanObj.setReceiptofPayment(resMap.get("RSK_RECEIPT_PAYEMENT")==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_RECEIPT_PAYEMENT").toString()==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString());
				if("2".equalsIgnoreCase(beanObj.getProductId())){
					beanObj.setCedRetenType(resMap.get("RSK_CEDRET_TYPE")==null?"":resMap.get("RSK_CEDRET_TYPE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_CEDRET_TYPE")==null?"":resMap.get("RSK_CEDRET_TYPE").toString());
					beanObj.setSpRetro(resMap.get("RSK_SP_RETRO")==null?"":resMap.get("RSK_SP_RETRO").toString()==null ? "0" : resMap.get("RSK_SP_RETRO").toString()==null?"":resMap.get("RSK_SP_RETRO").toString());
					beanObj.setNoInsurer(resMap.get("RSK_NO_OF_INSURERS")==null?"":resMap.get("RSK_NO_OF_INSURERS").toString());
					beanObj.setLimitPerVesselOC(resMap.get("LIMIT_PER_VESSEL_OC")==null||"0".equals(resMap.get("LIMIT_PER_VESSEL_OC"))?"":resMap.get("LIMIT_PER_VESSEL_OC").toString());
					beanObj.setLimitPerLocationOC((resMap.get("LIMIT_PER_LOCATION_OC")==null||"0".equals(resMap.get("LIMIT_PER_LOCATION_OC"))?"":resMap.get("LIMIT_PER_LOCATION_OC").toString()));
					beanObj.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE")==null?"":resMap.get("COUNTRIES_INCLUDE").toString());
					beanObj.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE")==null?"":resMap.get("COUNTRIES_EXCLUDE").toString());
					beanObj.setTreatynoofLine(resMap.get("RSK_NO_OF_LINE")==null?"0":resMap.get("RSK_NO_OF_LINE").toString());
					beanObj.setLimitOrigCurPml(resMap.get("RSK_TRTY_LMT_PML_OC")==null?"0":resMap.get("RSK_TRTY_LMT_PML_OC").toString());
					beanObj.setTreatyLimitsurplusOCPml(resMap.get("RSK_TRTY_LMT_SUR_PML_OC")==null?"":resMap.get("RSK_TRTY_LMT_SUR_PML_OC").toString());
					beanObj.setEpipml(resMap.get("RSK_TRTY_LMT_OURASS_PML_OC")==null?"":resMap.get("RSK_TRTY_LMT_OURASS_PML_OC").toString());
				}
				beanObj.setEndorsmenttype(resMap.get("RS_ENDORSEMENT_TYPE")==null?"":resMap.get("RS_ENDORSEMENT_TYPE").toString());
				beanObj.setPml(resMap.get("RSK_PML")==null ? "" : resMap.get("RSK_PML").toString());
				beanObj.setPmlPercent(resMap.get("RSK_PML_PERCENT")==null ? "" : resMap.get("RSK_PML_PERCENT").toString());
				beanObj.setMaxLimitProduct(resMap.get("RSK_MAX_LMT_COVER")==null?"":resMap.get("RSK_MAX_LMT_COVER").toString()==null ? "0" : resMap.get("RSK_MAX_LMT_COVER").toString()==null?"":resMap.get("RSK_MAX_LMT_COVER").toString());
				beanObj.setRenewalcontractNo(resMap.get("OLD_CONTRACTNO")==null?"":resMap.get("OLD_CONTRACTNO").toString());
				beanObj.setBaseLoginId(resMap.get("LOGIN_ID")==null?"":resMap.get("LOGIN_ID").toString());
				beanObj.setTreatyLimitsurplusOC(resMap.get("RSK_TREATY_SURP_LIMIT_OC")==null?"":resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString()==null?"":resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString());
				beanObj.setInwardType(resMap.get("INWARD_BUS_TYPE")==null?"":resMap.get("INWARD_BUS_TYPE").toString());
				beanObj.setTreatyType(resMap.get("TREATYTYPE")==null?"":resMap.get("TREATYTYPE").toString());
				beanObj.setLOCIssued(resMap.get("RSK_LOC_ISSUED")==null?"":resMap.get("RSK_LOC_ISSUED").toString());
				beanObj.setRunoffYear(resMap.get("RSK_RUN_OFF_YEAR")==null?"":resMap.get("RSK_RUN_OFF_YEAR").toString());
				beanObj.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
				beanObj.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
				beanObj.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
				beanObj.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
				beanObj.setRetentionYN(resMap.get("RETENTIONYN")==null?"":resMap.get("RETENTIONYN").toString());
				if( beanObj.getTreatyType().equalsIgnoreCase("4") ||  beanObj.getTreatyType().equalsIgnoreCase("5") ){
					beanObj.setFaclimitOrigCur(fm.formatter(resMap.get("RSK_LIMIT_OC")==null?"0":resMap.get("RSK_LIMIT_OC").toString()));
				}
				else{
				beanObj.setLimitOrigCur(resMap.get("RSK_LIMIT_OC")==null?"0":resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OC").toString()==null?"":resMap.get("RSK_LIMIT_OC").toString());
				}
			}
			if(StringUtils.isNotBlank(beanObj.getContractNo())&&!"0".equals(beanObj.getContractNo())){
				beanObj.setPrclFlag(dropDowmImpl.getPLCLCountStatus(beanObj.getContractNo(), "0"));
			}else{
				beanObj.setPrclFlag(false);
			}
			beanObj.setRiskdetailYN(resMap.get("RISK_DET_YN")==null?"N":resMap.get("RISK_DET_YN").toString());
			beanObj.setBrokerdetYN(resMap.get("BROKER_DET_YN")==null?"N":resMap.get("BROKER_DET_YN").toString());
			beanObj.setCoverdetYN(resMap.get("COVER_DET_YN")==null?"N":resMap.get("COVER_DET_YN").toString());
			beanObj.setPremiumdetailYN(resMap.get("PREMIUM_DET_YN")==null?"N":resMap.get("PREMIUM_DET_YN").toString());
			beanObj.setAcqdetailYN(resMap.get("ACQCOST_DET_YN")==null?"N":resMap.get("ACQCOST_DET_YN").toString());
			beanObj.setCommissiondetailYN(resMap.get("COMM_DET_YN")==null?"N":resMap.get("COMM_DET_YN").toString());
			beanObj.setDepositdetailYN(resMap.get("DEPOSIT_DET_YN")==null?"N":resMap.get("DEPOSIT_DET_YN").toString());
			beanObj.setLossdetailYN(resMap.get("LOSS_DET_YN")==null?"N":resMap.get("LOSS_DET_YN").toString());
			beanObj.setDocdetailYN(resMap.get("DOC_DET_YN")==null?"N":resMap.get("DOC_DET_YN").toString());
			beanObj.setPaymentPartner(resMap.get("PAYMENT_PARTNER")==null?"":resMap.get("PAYMENT_PARTNER").toString());
			beanObj.setSectionNo(resMap.get("SECTION_NO")==null?"":resMap.get("SECTION_NO").toString());
			
			beanObj.setQuotesharePercent(resMap.get("QUOTESHARE_PERCENT")==null?"":dropDowmImpl.formattereight(resMap.get("QUOTESHARE_PERCENT").toString()));
			beanObj.setAccountingPeriodNotes(resMap.get("RSK_ACCOUNT_PERIOD_NOTICE")==null?"":resMap.get("RSK_ACCOUNT_PERIOD_NOTICE").toString());
			beanObj.setStatementConfirm(resMap.get("RSK_STATEMENT_CONFIRM")==null?"":resMap.get("RSK_STATEMENT_CONFIRM").toString());
//			GetRemarksDetails(req.getProposalNo());
//			getRetDetails(req.getProposalNo());
			beanObj.setAmendId(dropDowmImpl.getRiskComMaxAmendId(beanObj.getProposalNo()));
			} 
			else {
				beanObj.setDepartId("");
				beanObj.setContractListVal("");
				beanObj.setProfitCenter("");
				beanObj.setSubProfitcenter("");
				beanObj.setPolicyBranch("");
				beanObj.setBroker("");
				beanObj.setTreatyNametype("");
				beanObj.setMonth("");
				beanObj.setUnderwriter("");
				beanObj.setAcceptanceDate("");
				beanObj.setOrginalCurrency("");
				beanObj.setExchangeRate("");
				beanObj.setBasis("");
				beanObj.setPnoc("");
				beanObj.setRiskCovered("");
				beanObj.setTerritoryscope("");
				beanObj.setTerritory(""); //24
				beanObj.setProStatus("");
				
				beanObj.setEpiorigCur("");
				beanObj.setPerilCovered("");
				if("2".equalsIgnoreCase(beanObj.getProductId())){  //error
					beanObj.setOurEstimate("");
				}
				if("2".equalsIgnoreCase(beanObj.getProductId())){
					beanObj.setEpi("");
				}
				beanObj.setXlCost("");
				if("2".equalsIgnoreCase(beanObj.getProductId())){
					beanObj.setCedRetent("");
				}
				beanObj.setShareWritten("");
				beanObj.setSharSign("");
				beanObj.setProposalType("");
				beanObj.setAccountingPeriod("");
				beanObj.setReceiptofStatements("");
				beanObj.setReceiptofPayment("");
				if("2".equalsIgnoreCase(beanObj.getProductId())){
					beanObj.setCedRetenType("");
					beanObj.setSpRetro("");
					beanObj.setNoInsurer("");
					beanObj.setLimitPerVesselOC("");
					beanObj.setLimitPerLocationOC("");
					beanObj.setCountryIncludedList("");
					beanObj.setCountryExcludedList("");
					beanObj.setTreatynoofLine("");
					beanObj.setLimitOrigCurPml("");
					beanObj.setTreatyLimitsurplusOCPml("");
					beanObj.setEpipml("");
				}
				
				beanObj.setEndorsmenttype("");
				beanObj.setPml("");
				beanObj.setPmlPercent("");
				beanObj.setMaxLimitProduct("");
				beanObj.setRenewalcontractNo("");
				beanObj.setBaseLoginId("");
				beanObj.setTreatyLimitsurplusOC("");
				beanObj.setInwardType("");
				beanObj.setTreatyType("");
				beanObj.setLOCIssued("");
				beanObj.setRunoffYear("");
				beanObj.setLocBankName("");
				beanObj.setLocCreditPrd("");
				beanObj.setLocCreditAmt("");
				beanObj.setLocBeneficerName("");
				beanObj.setRetentionYN("");
				beanObj.setFaclimitOrigCur("");
				beanObj.setLimitOrigCur("");
				beanObj.setPrclFlag(false);
			
			beanObj.setRiskdetailYN("N");
			beanObj.setBrokerdetYN("N");
			beanObj.setCoverdetYN("N");
			beanObj.setPremiumdetailYN("N");
			beanObj.setAcqdetailYN("N");
			beanObj.setCommissiondetailYN("N");
			beanObj.setDepositdetailYN("N");
			beanObj.setLossdetailYN("N");
			beanObj.setDocdetailYN("N");
			beanObj.setPaymentPartner("");
			beanObj.setSectionNo("");
			beanObj.setQuotesharePercent("");
			beanObj.setAccountingPeriodNotes("");
			beanObj.setStatementConfirm("");
//			List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
//			Map<String,Object> doubleMap = new HashMap<String,Object>();
//			 doubleMap.put("one",new Double(1.0));
//			 result.add(doubleMap);
//			 beanObj.setRemarkList(result);
//			
			}
			String proposalno="";
			if (StringUtils.isNotEmpty(req.getLayerProposalNo())) {
				proposalno = req.getLayerProposalNo();
			} else {
				proposalno = beanObj.getProposalNo();
			}
	//		this.showSecondpageEditItems(beanObj, beanObj.getProductId(), proposalno);
			if("copy".equals(req.getFlag())) {
				String sectionNo="";
				if("2".equals(beanObj.getProductId())) {
					//if(StringUtils.isBlank(beanObj.getSectionNo())) {
					//GET_MAX_SECTION_NO_DET
					sectionNo =	proportionalityCustomRepository.getMaxSectionNoDet(beanObj.getProposalNo());
						
						beanObj.setSectionNo(sectionNo);
					//}
				}
			}
			response.setCommonResponse(beanObj);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}


//	public boolean getPLCLCountStatus(String contractNo,String layerNo) {
//		boolean  status=false;
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		int plclCount=0;
//		try{
//			String query="common.select.getPRCLCount";
//			list= queryImpl.selectList(query,new String[] {contractNo,layerNo,contractNo,layerNo});
//			if (!CollectionUtils.isEmpty(list)) {
//				plclCount = Integer.valueOf(list.get(0).get("COUNT") == null ? ""
//						: list.get(0).get("COUNT").toString());
//			}
//			
//			
//			if(plclCount>0)
//				status=true;
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//
//		return status;
//	}
	
	@Override
	public GetRetDetailsRes getGetRetDetails(String proposalNo,  String branchCode, String productId) {
		GetRetDetailsRes response = new GetRetDetailsRes();
		GetRetDetailsRes1 res1 = new GetRetDetailsRes1();
		List<GetRetDetailsRes1> resList = new ArrayList<GetRetDetailsRes1>();
		List<RetListRes> retResList = new ArrayList<RetListRes>();
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_RET_DETAILS
			String[] args= new String[2];
			args[0]=proposalNo;
			args[1]="0";
			list = proportionalityCustomRepository.getRetDetails(args);	
			if(list!=null && list.size()>0){

				for (int i = 0; i < list.size(); i++) {
					RetListRes retRes = new RetListRes();
					Tuple insMap = list.get(i);
					retRes.setCoverdepartId(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString());
					retRes.setCoversubdepartId(insMap.get("RSK_SUBCLASS")==null?"":insMap.get("RSK_SUBCLASS").toString());
					retRes.setRetBusinessType(insMap.get("RSK_TYPE")==null?"":insMap.get("RSK_TYPE").toString());
					retRes.setRetType(insMap.get("RSK_RETTYPE")==null?"":insMap.get("RSK_RETTYPE").toString());
					retRes.setRetBasis(insMap.get("RSK_BASISTYPE")==null?"":insMap.get("RSK_BASISTYPE").toString());
					retRes.setFirstretention(insMap.get("RSK_FIRST_RET_OC")==null?"":fm.formatter(insMap.get("RSK_FIRST_RET_OC").toString()));
					retRes.setSecondretention(insMap.get("RSK_SECOND_RET_OC")==null?"":fm.formatter(insMap.get("RSK_SECOND_RET_OC").toString()));
					retRes.setRetTreatyFST(insMap.get("RSK_RET_TL_FST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_TL_FST_OC").toString()));
					retRes.setRetTreatySST(insMap.get("RSK_RET_TL_SST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_TL_SST_OC").toString()));
					retRes.setRetEventFST(insMap.get("RSK_RET_EL_FST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_EL_FST_OC").toString()));
					retRes.setRetEventSST(insMap.get("RSK_RET_EL_SST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_EL_SST_OC").toString()));
					GetCommonDropDownRes deptListRes= (dropDowmImpl.getSubProfitCentreDropDown(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString(),branchCode,productId));
					retRes.setCoversubdeptList(deptListRes.getCommonResponse());
					retResList.add(retRes);
				}
				
				// bean.setRetList(result);
				res1.setRetList(retResList);
				 res1.setLoopcount(Integer.toString(list.size()));
				 resList.add(res1);
				 response.setCommonResponse(resList);
				 response.setMessage("Success");
					response.setIsError(false);
				}}catch(Exception e){
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
				return response;
	}

	@Override
	public BaseLayerStatusRes BaseLayerStatus(BaseLayerStatusReq req) {
		BaseLayerStatusRes response = new BaseLayerStatusRes();
		BaseLayerStatusRes1 res = new BaseLayerStatusRes1();
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_BASE_LAYER_DETAILS
			list = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getProposalNo());	
			Tuple resMap1 = null;
			if(list!=null && list.size()>0)
				resMap1 = list.get(0);
			if (resMap1 != null) {
					res.setBaseLayerYN(resMap1.get("BASE_LAYER")==null?"":resMap1.get("BASE_LAYER").toString());
			}
			if(StringUtils.isNotBlank(res.getBaseLayerYN())){
				//GET_BASE_LAYER_DETAILS
				list = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),res.getBaseLayerYN());	
				
				resMap1 = null;
				if(list!=null && list.size()>0)
					resMap1 = list.get(0);
				if (resMap1 != null) {
						res.setBaseContractNo(resMap1.get("CONTRACT_NO")==null?"":resMap1.get("CONTRACT_NO").toString());
						res.setBaseContractNoStatus(resMap1.get("CONTRACT_STATUS")==null?"":resMap1.get("CONTRACT_STATUS").toString());
				}
				if(("0".equalsIgnoreCase(res.getBaseContractNo()) || StringUtils.isBlank(res.getBaseContractNo())) && "P".equalsIgnoreCase(res.getBaseContractNoStatus().trim())){
					res.setProStatus("P");
					res.setProdisableStatus("Y");
				}
			}
			response.setCommonResponse(res);
			 response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}

	@Override
	public ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req) {
		ShowSecondpageEditItemsRes response = new ShowSecondpageEditItemsRes();
		ShowSecondpageEditItemsRes1 res = new ShowSecondpageEditItemsRes1();
		List<Tuple> list = new ArrayList<>();
		try{
			//risk.select.getEditModeSecPageData
			if(StringUtils.isBlank(req.getSectionMode())) { //RI
			list = proportionalityCustomRepository.riskSelectGetEditModeSecPageData(req.getProposalNo());
			Tuple resMap = null;
			if(res!=null && list.size()>0)
				resMap =  list.get(0);
			if (resMap != null) {
				if (resMap.get("RSK_LIMIT_OS_OC") != null) {
					res.setLimitOurShare(resMap.get("RSK_LIMIT_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OS_OC").toString());
					res.setLimitOSViewOC(fm.formatter(resMap.get("RSK_LIMIT_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OS_OC").toString()));
				}
				if (resMap.get("RSK_EPI_OSOF_OC") != null) {
					res.setEpiAsPerOffer(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOF_OC").toString());
					res.setEpiOSViewOC(fm.formatter(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOF_OC").toString()));
				}
				if (resMap.get("RSK_EPI_OSOE_OC") != null) {
					res.setEpiAsPerShare(resMap.get("RSK_EPI_OSOE_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOE_OC").toString());
					res.setEpiOSOEViewOC(fm.formatter(resMap.get("RSK_EPI_OSOE_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOE_OC").toString()));
				}
				if (resMap.get("RSK_XLCOST_OS_OC") != null) {
					res.setXlcostOurShare(resMap.get("RSK_XLCOST_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_XLCOST_OS_OC").toString());
					res.setXlCostViewOC(fm.formatter(resMap.get("RSK_XLCOST_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_XLCOST_OS_OC").toString()));
				}
				if (resMap.get("RSK_LIMIT_OS_DC") != null) {
					res.setLimitOSViewDC(fm.formatter(resMap.get("RSK_LIMIT_OS_DC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OS_DC").toString()));
				}
				if (resMap.get("RSK_EPI_OSOF_DC") != null) {
					res.setEpiOSViewDC(fm.formatter(resMap.get("RSK_EPI_OSOF_DC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOF_DC").toString()));
				}
				if (resMap.get("RSK_EPI_OSOE_DC") != null) {
					res.setEpiOSOEViewDC(fm.formatter(resMap.get("RSK_EPI_OSOE_DC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOE_DC").toString()));
				}
				if (resMap.get("RSK_XLCOST_OS_DC") != null) {
					res.setXlCostViewDC(fm.formatter(resMap.get("RSK_XLCOST_OS_DC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_XLCOST_OS_DC").toString()));
				}
				if (resMap.get("RSK_COMM_QUOTASHARE") != null) {
					res.setCommissionQS(resMap.get("RSK_COMM_QUOTASHARE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_COMM_QUOTASHARE").toString());
				}
				if (resMap.get("RSK_COMM_SURPLUS") != null) {
					res.setCommissionsurp(resMap.get("RSK_COMM_SURPLUS").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_COMM_SURPLUS").toString());
				}
				if (resMap.get("RSK_OVERRIDER_PERC") != null) {
					res.setOverRidder(resMap.get("RSK_OVERRIDER_PERC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_OVERRIDER_PERC").toString());
				}
				if (resMap.get("RSK_BROKERAGE") != null) {
					res.setBrokerage(resMap.get("RSK_BROKERAGE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_BROKERAGE").toString());
				}
				if (resMap.get("RSK_TAX") != null) {
					res.setTax(resMap.get("RSK_TAX").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_TAX").toString());
				}
				if (resMap.get("RSK_ACQUISTION_COST_OC") != null) {
					res.setAcquisitionCost(resMap.get("RSK_ACQUISTION_COST_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_ACQUISTION_COST_OC").toString());
				}
				if (resMap.get("RSK_PROFIT_COMM") != null) {
					res.setShareProfitCommission(resMap.get("RSK_PROFIT_COMM").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PROFIT_COMM").toString());
				}
				
				if (resMap.get("RSK_PREMIUM_RESERVE") != null) {
					res.setPremiumReserve(resMap.get("RSK_PREMIUM_RESERVE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PREMIUM_RESERVE").toString());
				}
				if (resMap.get("RSK_LOSS_RESERVE") != null) {
					res.setLossreserve(resMap.get("RSK_LOSS_RESERVE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LOSS_RESERVE").toString());
				}
				if (resMap.get("RSK_INTEREST") != null) {
					res.setInterest(resMap.get("RSK_INTEREST").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_INTEREST").toString());
				}
				if (resMap.get("RSK_CASHLOSS_LMT_OC") != null) {
					res.setCashLossLimit(resMap.get("RSK_CASHLOSS_LMT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_CASHLOSS_LMT_OC").toString());
				}
				if (resMap.get("RSK_PF_INOUT_PREM") != null) {
					res.setPortfolioinoutPremium(resMap.get("RSK_PF_INOUT_PREM").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PF_INOUT_PREM").toString());
				}
				if (resMap.get("RSK_PF_INOUT_LOSS") != null) {
					res.setPortfolioinoutLoss(resMap.get("RSK_PF_INOUT_LOSS").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PF_INOUT_LOSS").toString());
				}
				if (resMap.get("RSK_LOSSADVICE") != null) {
					res.setLossAdvise(resMap.get("RSK_LOSSADVICE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LOSSADVICE").toString());
				}
				if (resMap.get("RSK_LEAD_UW") != null) {
					res.setLeaderUnderwriter(resMap.get("RSK_LEAD_UW").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LEAD_UW").toString());
				}
				if (resMap.get("RSK_LEAD_UW_SHARE") != null) {
					res.setLeaderUnderwritershare(resMap.get("RSK_LEAD_UW_SHARE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LEAD_UW_SHARE").toString());
				}
				res.setAccounts(resMap.get("RSK_ACCOUNTS")==null?"":resMap.get("RSK_ACCOUNTS").toString());
				res.setCrestaStatus(resMap.get("RSK_CREASTA_STATUS")==null?"":resMap.get("RSK_CREASTA_STATUS").toString());
				res.setEventlimit(resMap.get("RSK_EVENT_LIMIT_OC")==null?"0":resMap.get("RSK_EVENT_LIMIT_OC").toString());
				res.setAggregateLimit(resMap.get("RSK_AGGREGATE_LIMIT_OC")==null?"0":resMap.get("RSK_AGGREGATE_LIMIT_OC").toString());
				res.setOccurrentLimit(resMap.get("RSK_OCCURRENT_LIMIT_OC")==null?"0":resMap.get("RSK_OCCURRENT_LIMIT_OC").toString());
				res.setExclusion(resMap.get("RSK_EXCLUSION")==null?"":resMap.get("RSK_EXCLUSION").toString());
				res.setRemarks(resMap.get("RSK_REMARKS")==null?"":resMap.get("RSK_REMARKS").toString());
				res.setUnderwriterRecommendations(resMap.get("RSK_UW_RECOMM")==null?"":resMap.get("RSK_UW_RECOMM").toString());
				res.setGmsApproval(resMap.get("RSK_GM_APPROVAL")==null?"":resMap.get("RSK_GM_APPROVAL").toString());
			
				res.setSlideScaleCommission(resMap.get("RSK_SLADSCALE_COMM")==null?"":resMap.get("RSK_SLADSCALE_COMM").toString());
				res.setLossParticipants(resMap.get("RSK_LOSS_PART_CARRIDOR")==null?"":resMap.get("RSK_LOSS_PART_CARRIDOR").toString());
				res.setCommissionSubClass(resMap.get("RSK_COMBIN_SUB_CLASS")==null?"":resMap.get("RSK_COMBIN_SUB_CLASS").toString());
				res.setLeaderUnderwritercountry(resMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":resMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
				res.setOrginalacqcost(resMap.get("RSK_INCLUDE_ACQ_COST")==null?"":resMap.get("RSK_INCLUDE_ACQ_COST").toString());
				res.setOurassessmentorginalacqcost(resMap.get("RSK_OUR_ASS_ACQ_COST")==null?"":resMap.get("RSK_OUR_ASS_ACQ_COST").toString());
				res.setOuracqCost(resMap.get("RSK_OUR_ACQ_OUR_SHARE_OC")==null?"":resMap.get("RSK_OUR_ACQ_OUR_SHARE_OC").toString());
				res.setProfitCommission(resMap.get("RSK_PRO_NOTES")==null?"":resMap.get("RSK_PRO_NOTES").toString());
				res.setLosscommissionSubClass(resMap.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":resMap.get("RSK_LOSS_COMBIN_SUB_CLASS").toString());
				res.setSlidecommissionSubClass(resMap.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"":resMap.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString());
				res.setCrestacommissionSubClass(resMap.get("RSK_CRESTA_COMBIN_SUB_CLASS")==null?"":resMap.get("RSK_CRESTA_COMBIN_SUB_CLASS").toString());
				res.setManagementExpenses(resMap.get("RSK_PRO_MANAGEMENT_EXP")==null?"":resMap.get("RSK_PRO_MANAGEMENT_EXP").toString());
				res.setCommissionType(resMap.get("RSK_PRO_COMM_TYPE")==null?"":resMap.get("RSK_PRO_COMM_TYPE").toString());
				res.setProfitCommissionPer(resMap.get("RSK_PRO_COMM_PER")==null?"":dropDowmImpl.formatterpercentage(resMap.get("RSK_PRO_COMM_PER").toString()));
				res.setSetup(resMap.get("RSK_PRO_SET_UP")==null?"":resMap.get("RSK_PRO_SET_UP").toString());
				res.setSuperProfitCommission(resMap.get("RSK_PRO_SUP_PRO_COM")==null?"":resMap.get("RSK_PRO_SUP_PRO_COM").toString());
				res.setLossCarried(resMap.get("RSK_PRO_LOSS_CARY_TYPE")==null?"":resMap.get("RSK_PRO_LOSS_CARY_TYPE").toString());
				res.setLossyear(resMap.get("RSK_PRO_LOSS_CARY_YEAR")==null?"":resMap.get("RSK_PRO_LOSS_CARY_YEAR").toString());
				res.setProfitCarried(resMap.get("RSK_PRO_PROFIT_CARY_TYPE")==null?"":resMap.get("RSK_PRO_PROFIT_CARY_TYPE").toString());
				res.setProfitCarriedForYear(resMap.get("RSK_PRO_PROFIT_CARY_YEAR")==null?"":resMap.get("RSK_PRO_PROFIT_CARY_YEAR").toString());
				res.setFistpc(resMap.get("RSK_PRO_FIRST_PFO_COM")==null?"":resMap.get("RSK_PRO_FIRST_PFO_COM").toString());
				res.setProfitMont(resMap.get("RSK_PRO_FIRST_PFO_COM_PRD")==null?"":resMap.get("RSK_PRO_FIRST_PFO_COM_PRD").toString());
				res.setSubProfitMonth(resMap.get("RSK_PRO_SUB_PFO_COM_PRD")==null?"":resMap.get("RSK_PRO_SUB_PFO_COM_PRD").toString());
				res.setSubpc(resMap.get("RSK_PRO_SUB_PFO_COM")==null?"":resMap.get("RSK_PRO_SUB_PFO_COM").toString());
				res.setSubSeqCalculation(resMap.get("RSK_PRO_SUB_SEQ_CAL")==null?"":resMap.get("RSK_PRO_SUB_SEQ_CAL").toString());
				res.setLocRate(resMap.get("RSK_RATE")==null?"":resMap.get("RSK_RATE").toString());

				res.setPremiumResType(resMap.get("RSK_PREMIUM_RES_TYPE")==null?"":resMap.get("RSK_PREMIUM_RES_TYPE").toString());
				res.setPortfolioType(resMap.get("RSK_PORTFOLIO_TYPE")==null?"":resMap.get("RSK_PORTFOLIO_TYPE").toString());
				res.setPcfpcType(resMap.get("FPC_TYPE")==null?"":resMap.get("FPC_TYPE").toString());
				res.setPcfixedDate(resMap.get("FPC_FIXED_DATE")==null?"":resMap.get("FPC_FIXED_DATE").toString()); //Ri
				
				if (resMap.get("RSK_OTHER_COST") != null) {
					res.setOthercost(resMap.get("RSK_OTHER_COST").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_OTHER_COST").toString());
				}else{
					res.setOthercost("0");
				}
				res.setAcqCostPer((Double.parseDouble(res.getCommissionQS()==null?"0":res.getCommissionQS())+Double.parseDouble(res.getCommissionsurp()==null?"0":res.getCommissionsurp())
				+Double.parseDouble(res.getOverRidder()==null?"0":res.getOverRidder())+Double.parseDouble(res.getBrokerage()==null?"0":res.getBrokerage())+Double.parseDouble(res.getTax()==null?"0":res.getTax())+Double.parseDouble(res.getOthercost()==null?"0":res.getOthercost()))+"");
				
			}
			
			//risk.select.getQuotaShare
			list = proportionalityCustomRepository.riskSelectGetQuotaShare(req.getProposalNo());
			Tuple res1Map = null;
			if(list!=null && list.size()>0) {
				res1Map = list.get(0);
				if(res1Map!=null) {
					res.setPremiumQuotaShare(res1Map.get("RSK_PREMIUM_QUOTA_SHARE")==null?"":res1Map.get("RSK_PREMIUM_QUOTA_SHARE").toString());
					res.setPremiumSurplus(res1Map.get("RSK_PREMIUM_SURPULS")==null?"":res1Map.get("RSK_PREMIUM_SURPULS").toString());
				}
			}
			
			//RISK_SELECT_COMM_GETQUOTASHARE
			list = proportionalityCustomRepository.riskSelectCommGetquotashare(req.getProposalNo());
			
			if(list!=null && list.size()>0) {
				res1Map = list.get(0);
				if(res1Map!=null) {
					res.setCommissionQSAmt(res1Map.get("COMM_QS_AMT")==null?"":res1Map.get("COMM_QS_AMT").toString());
					res.setCommissionsurpAmt(res1Map.get("COMM_SURPLUS_AMT")==null?"":res1Map.get("COMM_SURPLUS_AMT").toString());
				}
			}
		
			List<CommonResDropDown> dropResList = new ArrayList<CommonResDropDown>();
			NoInsurerRes insurerRes = new NoInsurerRes();
			List<NoInsurerRes> insurerResList = new ArrayList<NoInsurerRes>();
			for(int i=0;i<Integer.parseInt(req.getNoInsurer());i++){
			
				insurerRes.setProductid("4");	
				insurerRes.setRetroType("TR");
				insurerRes.setBranchCode(req.getBranchCode());
				insurerRes.setIncepDate(req.getIncepDate());
				
				GetRetroContractDetailsListReq req1 = new GetRetroContractDetailsListReq();
				req1.setBranchCode(req.getBranchCode());
				req1.setIncepDate(req.getIncepDate());
				req1.setProductid(insurerRes.getProductid());
				
				GetCommonDropDownRes commonRes=dropDowmImpl.getRetroContractDetailsList(req1,1,"");
				if(commonRes.getCommonResponse()!=null) {	
					for(int j=0;j<commonRes.getCommonResponse().size();j++) {
						CommonResDropDown dropRes = new CommonResDropDown();
						dropRes.setCode(commonRes.getCommonResponse().get(j).getCode());
						dropRes.setCodeDescription(commonRes.getCommonResponse().get(j).getCodeDescription());
						dropResList.add(dropRes);
					}
				}
				
				insurerRes.setRetroUwyear(dropResList);
				insurerResList.add(insurerRes);
			} 
			res.setNoInsurerList(insurerResList);	
			} else {
				res.setLimitOurShare("");
				res.setLimitOSViewOC("");
				res.setEpiAsPerOffer("");
				res.setEpiOSViewOC("");
				res.setEpiAsPerShare("");
				res.setEpiOSOEViewOC("");
				res.setXlcostOurShare("");
				res.setXlCostViewOC("");
				res.setLimitOSViewDC("");
				res.setEpiOSViewDC("");
				res.setEpiOSOEViewDC("");
				res.setXlCostViewDC("");
				res.setCommissionQS("");
				res.setCommissionsurp("");
				res.setOverRidder("");
				res.setBrokerage("");
				res.setTax("");
				res.setAcquisitionCost("");
				res.setShareProfitCommission("2");
				res.setPremiumReserve("");
				res.setLossreserve("");
				res.setInterest("");
				res.setCashLossLimit("");
				res.setPortfolioinoutPremium("");
				res.setPortfolioinoutLoss("");
				res.setLossAdvise("");
				res.setLeaderUnderwriter("");
				res.setLeaderUnderwritershare("");
				res.setAccounts("");
				res.setCrestaStatus("");
				res.setEventlimit("");
				res.setAggregateLimit("");
				res.setOccurrentLimit("");
				res.setExclusion("");
				res.setRemarks("");
				res.setUnderwriterRecommendations("");
				res.setGmsApproval("");
				res.setSlideScaleCommission("");
				res.setLossParticipants("");
				res.setCommissionSubClass("");
				res.setLeaderUnderwritercountry("");
				res.setOrginalacqcost("");
				res.setOurassessmentorginalacqcost("");
				res.setOuracqCost("");
				res.setProfitCommission("");
				res.setLosscommissionSubClass("");
				res.setSlidecommissionSubClass("");
				res.setCrestacommissionSubClass("");
				res.setManagementExpenses("");
				res.setCommissionType("");
				res.setProfitCommissionPer("");
				res.setSetup("");
				res.setSuperProfitCommission("");
				res.setLossCarried("");
				res.setLossyear("");
				res.setProfitCarried("");
				res.setProfitCarriedForYear("");
				res.setFistpc("");
				res.setProfitMont("");
				res.setSubProfitMonth("");
				res.setSubpc("");
				res.setSubSeqCalculation("");
				res.setLocRate("");
				res.setPremiumResType("");
				res.setPortfolioType("");
				res.setPcfpcType("");
				res.setPcfixedDate("");
				res.setOthercost("0");
				res.setAcqCostPer("");
				res.setPremiumQuotaShare("");
				res.setPremiumSurplus("");
				res.setCommissionQSAmt("");
				res.setCommissionsurpAmt("");
			}
			
			response.setCommonResponse(res);
			 response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}

	public List<Map<String, Object>>  getValidation(String icepDate, String contId, String deptId){
		List<Map<String, Object>>  list = new ArrayList<>();
		try{
			//pro.select.getRenewalValidation
			list = proportionalityCustomRepository.riskSelectCommGetquotashare(icepDate,contId,deptId);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public GetprofitCommissionEnableRes getprofitCommissionEnable(GetprofitCommissionEnableReq req) {
		GetprofitCommissionEnableRes response = new GetprofitCommissionEnableRes();
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		List<Tuple> list  = new ArrayList<>();
		String profitComm="";
		String subclass="";
		String status="Y";
		String propNo="";
		try{
			if(StringUtils.isBlank(req.getBaseLayer())){
				//GET_BASE_PROPOSAL_NO
				propNo = proportionalityCustomRepository.getBaseProposalNo(req.getProposalNo());
				req.setBaseLayer(propNo);
			}
			if("profit".equalsIgnoreCase(req.getType())){
				//PROFIT_COMMISSION_ENABLE
				list  = proportionalityCustomRepository.profitCommissionEnable(req.getBaseLayer());
			for(int i=0;i<list.size();i++){
	              Tuple tempMap = list.get(i);
	               profitComm=(tempMap.get("RSK_PROFIT_COMM")==null?"":tempMap.get("RSK_PROFIT_COMM").toString());
	               subclass=(tempMap.get("RSK_COMBIN_SUB_CLASS")==null?"":tempMap.get("RSK_COMBIN_SUB_CLASS").toString());
			}
			}
			if("loss".equalsIgnoreCase(req.getType())){
				//PROFIT_COMMISSION_ENABLE_LOSS
				list  = proportionalityCustomRepository.profitCommissionEnableLoss(req.getBaseLayer());
				
				for(int i=0;i<list.size();i++){
					Tuple tempMap = list.get(i);
		               profitComm=(tempMap.get("RSK_LOSS_PART_CARRIDOR")==null?"":tempMap.get("RSK_LOSS_PART_CARRIDOR").toString());
		               subclass=(tempMap.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":tempMap.get("RSK_LOSS_COMBIN_SUB_CLASS").toString());
				}
				
				}
			if("cresta".equalsIgnoreCase(req.getType())){
				//PROFIT_COMMISSION_ENABLE_CRESTA
				list  = proportionalityCustomRepository.profitCommissionEnableCresta(req.getBaseLayer());
				for(int i=0;i<list.size();i++){
		              Tuple tempMap = list.get(i);
		               profitComm=(tempMap.get("RSK_CREASTA_STATUS")==null?"":tempMap.get("RSK_CREASTA_STATUS").toString());
		               subclass=(tempMap.get("RSK_CRESTA_COMBIN_SUB_CLASS")==null?"":tempMap.get("RSK_CRESTA_COMBIN_SUB_CLASS").toString());
				}
				}
			if("slide".equalsIgnoreCase(req.getType())){
				//PROFIT_COMMISSION_ENABLE_SLIDE
				list  = proportionalityCustomRepository.profitCommissionEnableSlide(req.getBaseLayer());
				for(int i=0;i<list.size();i++){
		              Tuple tempMap = list.get(i);
		               profitComm=(tempMap.get("RSK_SLADSCALE_COMM")==null?"":tempMap.get("RSK_SLADSCALE_COMM").toString());
		               subclass=(tempMap.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"":tempMap.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString());
				}
				}
			if("2".equalsIgnoreCase(subclass) || result.size()<=0){
				status="N";	
			}
				response.setStatus(status);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	public int getCrestaCount(GetCrestaCountReq req) {
		int count=0;
		try {
			//GET_CRESTA_DETAIL_COUNT
			count = ttrnCrestazoneDetailsRepository.countByProposalNoAndAmendIdAndBranchCode(new BigDecimal(req.getProposalNo()), req.getAmendId(), req.getBranchCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public int getBonusListCount(GetBonusListCountReq req, String type) {
		String args[]=null;
		int result=0;
		try{
			//BONUS_COUNT_MAIN
			args = new String[5];
			args[0] = req.getProposalNo();
			args[1] = req.getBranchCode();
			 if("scale".equalsIgnoreCase(type)){
		           args[2] ="SSC";
		           }
		      else{
		        	   args[2]="LPC";
		         }
			args[3] = req.getAmendId();
			args[4] = StringUtils.isEmpty(req.getLayerNo()) ? "0" : req.getLayerNo();
			result = ttrnBonusRepository.countByProposalNoAndBranchAndTypeAndEndorsementNoAndLayerNoAndLcbFromNotNull(
					new BigDecimal(args[0]),args[1],args[2],new BigDecimal(args[3]),args[4]);
	}
	catch(Exception e){
		e.printStackTrace();
	}
		return result;
	}
	public int CommissionTypeCount(String proposalNo, String branchCode, String CommissionType) {
		int count=0;
		try{
			//COMMISSION_TYPE_COUNT
			String args[]=new String[3];
			args[0]=proposalNo;
			args[1]=branchCode;
			args[2]= CommissionType;
			count =	proportionalityCustomRepository.commissionTypeCount(args);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	public boolean GetShareValidation(String proposalNo, String leaderUnderwritershare) {
		boolean result=false;
		String out="";
		try {
			//GET_SIGN_SHARE_PRODUCT23
			out =	proportionalityCustomRepository.getSignShareProduct23(proposalNo);
			if(Double.parseDouble(out)+Double.parseDouble(leaderUnderwritershare==null?"0":leaderUnderwritershare)>100){
				result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req) {
		ShowSecondPageDataRes response = new ShowSecondPageDataRes();
		ShowSecondPageDataRes1 res = new ShowSecondPageDataRes1();
		try {
			//risk.select.getSecPageData //select RTRIM(XMLAGG(XMLELEMEN pending
			List<Tuple> list =	proportionalityCustomRepository.riskSelectGetSecPageData(req.getProposalNo(),req.getBranchCode(),req.getProductId());
			Tuple resMap = null;
			if(list!=null && list.size()>0)
				resMap = list.get(0);
			if(resMap!=null){
				res.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
				res.setSubProfitcenter(resMap.get("TMAS_SPFC_NAME")==null?"":resMap.get("TMAS_SPFC_NAME").toString()); 
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
				res.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				res.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
				res.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
				String query="risk.select.CEASE_STATUS";
				List<Map<String, Object>> list1 = queryImpl.selectList(query,new String[]{req.getProposalNo()});
				if(!CollectionUtils.isEmpty(list1)) {
					res.setCeaseStatus(list1.get(0).get("RSK_ENDORSEMENT_NO")==null?"":list1.get(0).get("RSK_ENDORSEMENT_NO").toString());
				}
				res.setEndttypename(resMap.get("DETAIL_NAME")==null?"":resMap.get("DETAIL_NAME").toString());
			}
			List<RetroFinalListres>  finalList = new ArrayList<RetroFinalListres>();
			if(StringUtils.isNotBlank(req.getNoInsurer()) && Integer.parseInt(req.getNoInsurer())>0 && (req.getRetroFinalListReq()==null || req.getRetroFinalListReq().size()==0)){
				if(StringUtils.isNotBlank(req.getNoInsurer()) && Integer.parseInt(req.getNoInsurer())>0){
//					List<List<Map<String,Object>>> retroFinalList=new ArrayList<List<Map<String,Object>>>();
//					List<String> retroPercentage=new ArrayList<String>();
//					List<String> UWYear=new ArrayList<String>();
					for(int i=0;i<Integer.parseInt(req.getNoInsurer());i++){
						RetroFinalListres retro = new RetroFinalListres();
						GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
						req2.setBranchCode(req.getBranchCode());
						req2.setIncepDate(req.getIncepDate());
						req2.setProductid(req.getProductId());
						req2.setRetroType(req.getRetroType());
						//req2.setUwYear(req.getRetroType());
						GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,2,"");
						if (!CollectionUtils.isEmpty(dropDownRes.getCommonResponse())) {
						
							
						CommonResDropDown res2= dropDownRes.getCommonResponse().get(i);
						
						retro.setCONTDET1(res2.getCode());
						retro.setCONTDET2(res2.getCodeDescription());
						finalList.add(retro);
							}
						
					}
					if(StringUtils.isNotBlank(req.getNoInsurer())){
						for (int z=0;z<Integer.parseInt(req.getNoInsurer());z++){
							RetroFinalListres retro = new RetroFinalListres();
							retro.setRetroDupYerar(req.getUwYear());
							
							GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
							req2.setBranchCode(req.getBranchCode());
							req2.setIncepDate(req.getIncepDate());
							req2.setProductid(req.getProductId());
							req2.setRetroType(req.getRetroType());
							//req2.setUwYear(req.getUwYear());
							GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,3,"");
							if (!CollectionUtils.isEmpty(dropDownRes.getCommonResponse())) {
							List<CommonResDropDown> list1= dropDownRes.getCommonResponse();
							
						//	List<Map<String,Object>> list =dropDowmImpl.getRetroContractDetailsList(formobj,3,"");
							
							for (int i=0;i<list1.size();i++){
								CommonResDropDown map =  list1.get(i);
								//retro.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
								retro.setRetroDupContract(map.getCode());
								finalList.add(retro);
							}
							
						}}
					}
					res.setRetroFinalListRes(finalList);
					//formobj.setPercentRetro(retroPercentage);
				//	formobj.setRetroYear(UWYear);
					//formobj.setRetroFinalList(retroFinalList);
					
				}
				if(req.getNoInsurer()!=null && Integer.parseInt(req.getNoInsurer())==0){
					
					res.setRetroDupYerar(req.getUwYear());
					
					GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
					req2.setBranchCode(req.getBranchCode());
					req2.setIncepDate(req.getIncepDate());
					req2.setProductid(req.getProductId());
					req2.setRetroType(req.getRetroType());
					//req2.setUwYear(req.getRetroType());
					GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,3,"");
					List<CommonResDropDown> list1= dropDownRes.getCommonResponse();
				//	List<Map<String,Object>> list = getRetroContractDetailsList(formobj,3,"");
					
					for (int i=0;i<list1.size();i++){
						RetroFinalListres retro = new RetroFinalListres();
						//Map<String,Object> map = (Map<String, Object>) list1.get(i);
						//retro.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
						CommonResDropDown map =  list1.get(i);
						retro.setRetroDupContract(map.getCode());
						finalList.add(retro);
					}
				}
			}
			res.setRetroFinalListRes(finalList);
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	@Override
	public ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo) {
		ShowLayerBrokerageRes response = new ShowLayerBrokerageRes();
		ShowLayerBrokerageRes1 res = new ShowLayerBrokerageRes1();
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getBrokerage
			if(StringUtils.isNotBlank(layerProposalNo)){
				list =	proportionalityCustomRepository.riskSelectGetBrokerage(layerProposalNo);
				Tuple resMap = null;
				if(res!=null && list.size()>0)
					resMap = list.get(0);
				if(resMap!=null){
					res.setBrokerage(resMap.get("RSK_BROKERAGE")==null?"":resMap.get("RSK_BROKERAGE").toString());
					res.setTax(resMap.get("RSK_TAX")==null?"":resMap.get("RSK_TAX").toString());
				}
			}
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
	}
	
	@Override
	public CheckProductMatchRes checkProductMatch(String proposalNo, boolean contractMode,String productId) {
		CheckProductMatchRes response = new CheckProductMatchRes();
		boolean saveFlag = false;
		try{
			String result = "";
			if (contractMode) {
				//risk.select.getRskProIdByContNo
				List<TtrnRiskDetails> list = ttrnRiskDetailsRepository.findByRskContractNo(proposalNo);
				if (!CollectionUtils.isEmpty(list)) {
					result = (list.get(0).getRskProductid() == null ? ""
							: list.get(0).getRskProductid().toString());
				}
			} else {
				//risk.select.getRskProIdByProNo
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
				Root<TtrnRiskDetails> rd = query1.from(TtrnRiskDetails.class);
				query1.multiselect(rd.get("rskProductid").alias("RSK_PRODUCTID")); 
				//amendId
				Subquery<Long> end = query1.subquery(Long.class); 
				Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
				end.select(cb.max(p.get("rskEndorsementNo")));
				Predicate d1 = cb.equal(p.get("rskProposalNumber"), rd.get("rskProposalNumber"));
				end.where(d1);
				
				Predicate n1 = cb.equal(rd.get("rskProposalNumber"),proposalNo);
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end); 
				query1.where(n1,n2);

				TypedQuery<Tuple> res = em.createQuery(query1);
				List<Tuple> list = res.getResultList();
				if (!CollectionUtils.isEmpty(list)) {
					result = list.get(0).get("RSK_PRODUCTID")== null ? "": list.get(0).get("RSK_PRODUCTID").toString();
				}
			}
			if (productId.equalsIgnoreCase(result)) {
				saveFlag = true;
			} else {
				saveFlag = false;
			}
				response.setSaveFlag(saveFlag);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
	}
	@Override
	public ShowRetroContractsRes showRetroContracts(ShowRetroContractsReq req) {
		ShowRetroContractsRes response = new ShowRetroContractsRes();
		List<ShowRetroContractsRes1> resList = new ArrayList<ShowRetroContractsRes1>();
		
		GetRetroContractDetailsListReq req1 = new GetRetroContractDetailsListReq();
		req1.setBranchCode(req.getBranchCode());
		req1.setIncepDate(req.getIncepDate());
		req1.setProductid(req.getProductId());
		req1.setRetroType(req.getRetroType());
		req1.setUwYear(req.getUwYear());
		
		List<Tuple> insDetailsList = new ArrayList<>();
		try{
			String[] args=null;
			int noofInsurar = 0;
			if(StringUtils.isNotBlank(req.getNoInsurer())){
				 noofInsurar = Integer.parseInt(req.getNoInsurer());
				 noofInsurar = noofInsurar+1;
			}
			if(req.getView()){
				args=new String[3];
				args[0]=req.getAmendId();
				args[1]=req.getProposalNo();
				args[2]=Integer.toString(noofInsurar);
				//fac.select.viewInsDetails
				insDetailsList = proportionalityCustomRepository.facSelectViewInsDetails(args);
			}else{
				//fac.select.insDetails
				args=new String[2];
				args[0]=req.getProposalNo();
				args[1]=Integer.toString(noofInsurar);
				insDetailsList = proportionalityCustomRepository.facSelectInsDetails(args);
			}
			if(insDetailsList!=null&&insDetailsList.size()>0){
				List<String> retroList=new ArrayList<String>();
				List<String> retroPercentage=new ArrayList<String>();
				List<String> UWYear=new ArrayList<String>();
				//List<List<Map<String,Object>>> retroFinalList=new ArrayList<List<Map<String,Object>>>();
				List<CommonResDropDown>  retroFinalList = new ArrayList<CommonResDropDown>();
				List<String> cedingCompany=new ArrayList<String>();
				for(int j=0;j<insDetailsList.size();j++){
					ShowRetroContractsRes1 beanObj = new  ShowRetroContractsRes1();
					Tuple insDetailsMap= insDetailsList.get(j);
					if("R".equalsIgnoreCase((String)insDetailsMap.get("TYPE")))	{
						beanObj.setRetentionPercentage(insDetailsMap.get("RETRO_PER")==null?"":insDetailsMap.get("RETRO_PER").toString());
					}else{
						
						if(j==1){
							if(req.getUwYear().equalsIgnoreCase(insDetailsMap.get("UW_YEAR").toString())){
								beanObj.setRetroDupYerar(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
								}else{
									beanObj.setRetroDupYerar(req.getUwYear());
								}
							if(insDetailsMap.get("CONTRACTNO")!=null){
							beanObj.setRetroDupContract(insDetailsMap.get("CONTRACTNO")==null?"":insDetailsMap.get("CONTRACTNO").toString());
							}
						}
						else if(j>1){
							UWYear.add(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
							cedingCompany.add(insDetailsMap.get("CONTRACTNO")==null?"":insDetailsMap.get("CONTRACTNO").toString());
							retroPercentage.add(insDetailsMap.get("RETRO_PER")==null?"0":insDetailsMap.get("RETRO_PER").toString());
							GetCommonDropDownRes ds=dropDowmImpl.getRetroContractDetailsList(req1,2,insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
						if(ds!=null) {
							for(int k=0;k<ds.getCommonResponse().size();k++) {
								retroFinalList.add(ds.getCommonResponse().get(k));
							}	
							}
							retroList.add(String.valueOf(j));
						}
						
						if("2".equals(req.getProductId())){
							beanObj.setProductId("4");	
							beanObj.setRetroType("TR");
						}else if("3".equals(req.getProductId())){
							beanObj.setProductId("4");	
							beanObj.setRetroType("TR");	
						}
						
						beanObj.setBranchCode(beanObj.getBranchCode());
						beanObj.setIncepDate(beanObj.getIncepDate());
					}}}//--------------------------------------doubt below code need to update
//						beanObj.setRetroUwyear(getRetroContractDetailsList(bean,1,""));
//						beanObj.setUwYear(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
//					}
//				}
//				beanObj.setPercentRetro(retroPercentage);
//				beanObj.setRetroCeding(cedingCompany);
//				beanObj.setRetroYear(UWYear);
//				if(UWYear.size()==0){
//					beanObj.setRetroUwyear(getRetroContractDetailsList(beanObj,1,""));
//				}
//				if(retroFinalList.size()!=Integer.parseInt(req.getNoInsurer())){
//					for(int i=0;i<Integer.parseInt(req.getNoInsurer())-retroList.size();i++){
//						retroFinalList.add(new ArrayList<Map<String,Object>>());
//					}
//				}
//				
//				beanObj.setRetroFinalList(retroFinalList);
//			}else{
//				List<List<Map<String,Object>>> retroFinalList=new ArrayList<List<Map<String,Object>>>();
//				for(int i=0;i<Integer.parseInt(StringUtils.isBlank(req.getNoInsurer())?"0":req.getNoInsurer());i++){
//					ShowRetroContractsRes1 beanObj = new  ShowRetroContractsRes1();
//					if("2".equals(req.getProductId())){
//						beanObj.setProductId("4");	
//						beanObj.setRetroType("TR");
//					}else if("3".equals(req.getProductId())){
//						beanObj.setProductId("4");	
//						beanObj.setRetroType("TR");
//					}
//					beanObj.setBranchCode(beanObj.getBranchCode());
//					beanObj.setIncepDate(beanObj.getIncepDate());
//					retroFinalList.add(new ArrayList<Map<String,Object>>());
//					beanObj.setRetroUwyear(getRetroContractDetailsList(bean,1,""));
//				}
//			}
//			if(req.getNoInsurer()!=null &&Integer.parseInt(req.getNoInsurer())==0){
//				beanObj.setRetroDupYerar(req.getUwYear());
//				List<Map<String,Object>> list = getRetroContractDetailsList(beanObj,3,"");
//				for (int i=0;i<list.size();i++){
//					Map<String,Object> map = list.get(i);
//					beanObj.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
//				}
//			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
		}
	@Override
	public GetCrestaDetailListRes getCrestaDetailList(GetCrestaDetailListReq req) {
		GetCrestaDetailListRes response = new GetCrestaDetailListRes();
		List<GetCrestaDetailListRes1> crestaDetailList = new ArrayList<GetCrestaDetailListRes1>();
		try {
			//GET_CRESTA_DETAIL_LIST
			if(StringUtils.isBlank(req.getAmendId())){
				req.setAmendId("0");
			}
			
			List<TtrnCrestazoneDetails> result = ttrnCrestazoneDetailsRepository.findByProposalNoAndAmendIdAndBranchCodeOrderBySno(new BigDecimal(req.getProposalno()),req.getAmendId(),req.getBranchCode());
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					TtrnCrestazoneDetails insMap = result.get(i);
					GetCrestaDetailListRes1 res = new GetCrestaDetailListRes1();
					res.setTerritoryId(insMap.getTerritoryCode()==null?"":insMap.getTerritoryCode().toString());;
					res.setCrestaId(insMap.getCrestaId()==null?"":insMap.getCrestaId().toString());;
					res.setCrestaName(insMap.getCrestaName()==null?"":insMap.getCrestaName().toString());
					res.setCurrencyId(insMap.getCurrency()==null?"":insMap.getCurrency().toString());;
					res.setAccRisk(insMap.getAccRisk()==null?"":insMap.getAccRisk().toString());
					res.setAccDate(insMap.getAccumDate()==null?"":insMap.getAccumDate().toString());
					res.setScaleSNo(insMap.getSno()==null?"":insMap.getSno().toString());
					
					GetCommonDropDownRes commonRes=dropDowmImpl.getCrestaIDList(req.getBranchCode(),insMap.getTerritoryCode()==null?"":insMap.getTerritoryCode().toString());
					res.setCrestaIDList(commonRes.getCommonResponse());
					
					GetCommonDropDownRes commonRes1=dropDowmImpl.getCrestaNameList(req.getBranchCode(),insMap.getCrestaId()==null?"":insMap.getCrestaId().toString());
					res.setCrestaNameList(commonRes1.getCommonResponse());					
					crestaDetailList.add(res);
				}
			}
			response.setCommonResponse(crestaDetailList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
	}
	@Transactional
	@Override
	public InsertCrestaDetailsRes insertCrestaDetails(InsertCrestaDetailsReq req) {
		InsertCrestaDetailsRes response = new InsertCrestaDetailsRes();
		try {
			if(StringUtils.isBlank(req.getAmendId())){
				req.setAmendId("0");
			}
			String[] obj=null;
				//DELETE_CRESTA_MAIN_DETAILS
			
				ttrnCrestazoneDetailsRepository.deleteByProposalNoAndAmendIdAndBranchCode(new BigDecimal(req.getProposalNo()),req.getAmendId(),req.getBranchCode());
			obj = new String[12];
			for(int i=0;i<req.getCrestaReq().size();i++){
				if(StringUtils.isNotBlank(req.getCrestaReq().get(i).getTerritoryCode())){
				obj[0]=req.getContractNo();
				obj[1]=req.getProposalNo();
				obj[2]=req.getAmendId();
				obj[3]=req.getDepartmentId();
				obj[5]=req.getCrestaReq().get(i).getCrestaName();
				obj[4]=req.getCrestaReq().get(i).getCrestaId();
				obj[6]=req.getCrestaReq().get(i).getCurrencyId();
				obj[7]=req.getCrestaReq().get(i).getAccRisk();
				obj[8]=req.getCrestaReq().get(i).getAccumulationDate();
				obj[9]=req.getBranchCode();
				obj[10]=req.getCrestaReq().get(i).getTerritoryCode();
				obj[11]= req.getCrestaReq().get(i).getScaleSNo();
				//MOVE_TO_CRESTA_MAIN_TABLE
				TtrnCrestazoneDetails insert = proportionalityCustomRepository.moveToCrestaMainTable(obj);
				if(insert!=null) {
					ttrnCrestazoneDetailsRepository.saveAndFlush(insert);
					}
				}
				}
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
	}
	@Transactional
	@Override
	public CancelProposalRes cancelProposal(String proposalNo, String newProposal,String proposalReference) {
		CancelProposalRes response = new CancelProposalRes();
		
		try{
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			if(!"Layer".equalsIgnoreCase(proposalReference)){
			//CANCEL_OLD_PROPOSAL
				
				update.set("renewalStatus", "1");
				//amend
				Subquery<Long> amend = update.subquery(Long.class); 
				Root<PositionMaster> p = amend.from(PositionMaster.class);
				amend.select(cb.max(p.get("amendId")));
				Predicate d1 = cb.equal(p.get("proposalNo"), m.get("proposalNo"));
				amend.where(d1);
				
				Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(m.get("amendId"), amend);
				update.where(n1,n2);
				em.createQuery(update).executeUpdate();
			}
			//CANCEL_PROPOSAL
		
			update.set("contractStatus", "C");
			update.set("editMode", "N");
			//amend
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<PositionMaster> p = amend.from(PositionMaster.class);
			amend.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), m.get("proposalNo"));
			amend.where(d1);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), newProposal);
			Predicate n2 = cb.equal(m.get("amendId"), amend);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();

			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
	}
	@Override
	public GetRetentionDetailsRes getRetentionDetails(GetRetentionDetailsReq req) {
		GetRetentionDetailsRes response = new GetRetentionDetailsRes();
		List<Map<String,Object>>result=new ArrayList<Map<String,Object>>();
		List<GetRetentionDetailsRes1> resList = new ArrayList<GetRetentionDetailsRes1>();
		try {
			String query="GET_RET_BASE_DETAILS"; // SELECT RT.* ,DENSE_RANK()  OVER ( ORDER BY PROPOSAL_NO DESC) RN  FROM  TTRN_CEDENT_RET RT  WHERE  EXISTS 
			String[] obj= new String[4];
			obj[0]=req.getBranchCode();
			obj[1]=req.getCedingCo();
			obj[2]=req.getUwYear();
			obj[3]=req.getProductId();
			result=queryImpl.selectList(query,obj);	
			if(result!=null && result.size()>0){
				
				for (int i = 0; i < result.size(); i++) {
					Map<String, Object> insMap = (Map<String, Object>)result.get(i);
					GetRetentionDetailsRes1 res = new GetRetentionDetailsRes1();
					res.setCoverdepartId(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString());;
					res.setCoversubdepartId(insMap.get("RSK_SUBCLASS")==null?"":insMap.get("RSK_SUBCLASS").toString());;
					res.setRetBusinessType(insMap.get("RSK_TYPE")==null?"":insMap.get("RSK_TYPE").toString());
					res.setRetType(insMap.get("RSK_RETTYPE")==null?"":insMap.get("RSK_RETTYPE").toString());;
					res.setRetBasis(insMap.get("RSK_BASISTYPE")==null?"":insMap.get("RSK_BASISTYPE").toString());;
					res.setFirstretention(insMap.get("RSK_FIRST_RET_OC")==null?"":fm.formatter(insMap.get("RSK_FIRST_RET_OC").toString()));
					res.setSecondretention(insMap.get("RSK_SECOND_RET_OC")==null?"":fm.formatter(insMap.get("RSK_SECOND_RET_OC").toString()));;
					res.setRetTreatyFST(insMap.get("RSK_RET_TL_FST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_TL_FST_OC").toString()));;
					res.setRetTreatySST(insMap.get("RSK_RET_TL_SST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_TL_SST_OC").toString()));;
					res.setRetEventFST(insMap.get("RSK_RET_EL_FST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_EL_FST_OC").toString()));
					res.setRetEventSST(insMap.get("RSK_RET_EL_SST_OC")==null?"":fm.formatter(insMap.get("RSK_RET_EL_SST_OC").toString()));;
					GetCommonDropDownRes commonRes=dropDowmImpl.getSubProfitCentreDropDown(insMap.get("RSK_CLASS")==null?"":insMap.get("RSK_CLASS").toString(),req.getBranchCode(),req.getProductId());
					res.setCoversubdeptList(commonRes.getCommonResponse());
					resList.add(res);
					}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
					}
				return response;
	}
	@Override
	public GetScaleCommissionListRes getScaleCommissionList(String proposalNo, String branchCode, String pageFor,String referenceNo) {
		GetScaleCommissionListRes response= new GetScaleCommissionListRes();
		List<GetScaleCommissionListRes1> resList = new ArrayList<GetScaleCommissionListRes1>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	//	List<BonusDetailsRes> bonusResList = new ArrayList<BonusDetailsRes>();
		try{
			 if("scale".equalsIgnoreCase(pageFor)){
					//BONUS_MAIN_SELECT
//				 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
//					CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
//					Root<TtrnBonus> pm = query.from(TtrnBonus.class);
//					query.multiselect(pm.get("lcbId").alias("LCB_ID"),pm.get("lcbFrom").alias("LCB_FROM"),
//							pm.get("lcbTo").alias("LCB_TO"),pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
//							pm.get("lcbType").alias("LCB_TYPE"),pm.get("quotaShare").alias("QUOTA_SHARE"),
//							pm.get("remarks").alias("REMARKS"),pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
//							pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),pm.get("subProfitComm").alias("SUB__PROFIT_COMM"),
//							pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),pm.get("subSecCal").alias("SUB_SEC_CAL"),
//				 			pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),pm.get("fpcType").alias("FPC_TYPE"),
//							pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
//				
//					//end
//					Subquery<Long> end = query.subquery(Long.class);
//					Root<TtrnBonus> rds = end.from(TtrnBonus.class);
//					end.select(cb.max(rds.get("endorsementNo")));
//					Predicate a1 = cb.equal(rds.get("proposalNo"), pm.get("proposalNo"));
//					Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
//					Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
//					end.where(a1,a2,a3);
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(pm.get("lcbId")));
//
//					Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
//					Predicate n2 = cb.equal(pm.get("branch"), branchCode);
//					Predicate n3 = cb.equal(pm.get("type"), "SSC");
//					Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
//					Predicate n5 = cb.equal(pm.get("lcbType"), "SSC2");
//					query.where(n1,n2,n3,n4,n5).orderBy(orderList);
//					
//					TypedQuery<Tuple> res1 = em.createQuery(query);
//					List<Tuple> result = res1.getResultList();
				
				 list= queryImpl.selectList("BONUS_MAIN_SELECT",new String[] {proposalNo,branchCode,"SSC","SSC2"});
					
					if(CollectionUtils.isEmpty(list)) {
						list= queryImpl.selectList("BONUS_MAIN_SELECT_REFERENCE",new String[] {referenceNo});
					}
				 }else {
					
					 list= queryImpl.selectList("BONUS_MAIN_SELECT_LPC",new String[] {proposalNo,branchCode,"LPC"});
						if(CollectionUtils.isEmpty(list)) {
							 list= queryImpl.selectList("BONUS_MAIN_SELECT_REFERENCE_LPC",new String[] {referenceNo});
						}
				 }
					
				for(int i=0;i<list.size();i++){
					 Map<String,Object> tempMap = list.get(i);
		               GetScaleCommissionListRes1 res = new GetScaleCommissionListRes1();
		               res.setBonusSno(tempMap.get("LCB_ID")==null?"":tempMap.get("LCB_ID").toString());
		               res.setBonusFrom(tempMap.get("LCB_FROM")==null?"":fm.formattereight(tempMap.get("LCB_FROM").toString()));
		               res.setBonusTo(tempMap.get("LCB_TO")==null?"":fm.formatter(tempMap.get("LCB_TO").toString()));
		               res.setBonusLowClaimBonus(tempMap.get("LCB_PERCENTAGE")==null?"":fm.formatter(tempMap.get("LCB_PERCENTAGE").toString()));
			           res.setScaleMaxPartPercent(tempMap.get("SCALE_MAX_PART_PERCENT")==null?"":dropDowmImpl.formatter(tempMap.get("SCALE_MAX_PART_PERCENT").toString()));
		               if(!"scale".equalsIgnoreCase(pageFor)){
		               res.setBonusTypeId(tempMap.get("LCB_TYPE")==null?"":tempMap.get("LCB_TYPE").toString());
		               }
		               res.setQuotaShare(tempMap.get("QUOTA_SHARE")==null?"":tempMap.get("QUOTA_SHARE").toString());
		               res.setBonusremarks(tempMap.get("REMARKS")==null?"":tempMap.get("REMARKS").toString());
		               res.setScFistpc(tempMap.get("FIRST_PROFIT_COMM")==null?"":tempMap.get("FIRST_PROFIT_COMM").toString());
		               res.setScProfitMont(tempMap.get("FPC_DURATION_TYPE")==null?"":tempMap.get("FPC_DURATION_TYPE").toString());
		               res.setScSubpc(tempMap.get("SUB__PROFIT_COMM")==null?"":tempMap.get("SUB__PROFIT_COMM").toString());
		               res.setScSubProfitMonth(tempMap.get("SPC_DURATION_TYPE")==null?"":tempMap.get("SPC_DURATION_TYPE").toString());
		               res.setScSubSeqCalculation(tempMap.get("SUB_SEC_CAL")==null?"":tempMap.get("SUB_SEC_CAL").toString());
		               res.setFpcType(tempMap.get("FPC_TYPE")==null?"":tempMap.get("FPC_TYPE").toString());
		               res.setFpcfixedDate(tempMap.get("FPC_FIXED_DATE")==null?"":tempMap.get("FPC_FIXED_DATE").toString());
		               resList.add(res);    
		               }
		//		GetSlidingScaleMethodInfo(bean);
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
					}
				return response;
	}
	@Override
	public ViewRiskDetailsRes viewRiskDetails(ViewRiskDetailsReq req) {
		ViewRiskDetailsRes response = new ViewRiskDetailsRes();
		ViewRiskDetailsRes1 res = new ViewRiskDetailsRes1();
		 //List<ViewRiskDetailsRes1> resList = new ArrayList<ViewRiskDetailsRes1>();
		List<Tuple> list2 = new ArrayList<>();
		try {
			String[] args=new String[10];
			args[0] = req.getBranchCode();
			args[1] = req.getBranchCode();
			args[2] = req.getBranchCode();
			args[3] = req.getBranchCode();
			args[4] = req.getBranchCode();
			args[5] = req.getBranchCode();
			args[6] = req.getBranchCode();
			args[7] = req.getBranchCode();
			args[8] = req.getProposalNo();
			args[9] = req.getAmendId();
			String selectQry="RISK_SELECT_GETCOMMONDATA_PTTY";//LEFT OUTER JOIN
			List<Map<String, Object>> list = queryImpl.selectList(selectQry,args); 
			Map<String, Object> resMap = null;
			if(list!=null && list.size()>0)
				resMap = (Map<String, Object>)list.get(0);
			if (resMap != null) {
				
				res.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				res.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
				res.setProposalType(resMap.get("PROPOSAL_TYPE")==null?"":resMap.get("PROPOSAL_TYPE").toString());
				res.setAccountingPeriod(resMap.get("ACCOUNTING_PERIOD")==null?"":resMap.get("ACCOUNTING_PERIOD").toString());
				res.setReceiptofPayment(resMap.get("RSK_RECEIPT_PAYEMENT")==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString());
				res.setReceiptofStatements(resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString());
				res.setProfitCenter(resMap.get("TMAS_PFC_NAME")==null?"":resMap.get("TMAS_PFC_NAME").toString());
				res.setSubProfitCenter(resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString());
				if(!"ALL".equalsIgnoreCase(req.getSubProfitCenter())){
				res.setSubProfitCenter(resMap.get("TMAS_SPFC_NAME")==null?"":resMap.get("TMAS_SPFC_NAME").toString().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
				
				}
				
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setTreatyNametype(resMap.get("RSK_TREATYID")==null?"":resMap.get("RSK_TREATYID").toString());
				res.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
				res.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
				res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				res.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				res.setUnderwriter(resMap.get("UNDERWRITTER")==null?"":resMap.get("UNDERWRITTER").toString());
				res.setIncepDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				res.setExpDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				res.setAccDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":resMap.get("RSK_ACCOUNT_DATE").toString());
				res.setOrginalCurrency(resMap.get("SHORT_NAME")==null?"":resMap.get("SHORT_NAME").toString());
				res.setExchRate(resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString());
				res.setMdInstalmentNumber(resMap.get("MND_INSTALLMENTS")==null?"0":resMap.get("MND_INSTALLMENTS").toString());
				res.setRetroType(resMap.get("RSK_RETRO_TYPE")==null?"0":resMap.get("RSK_RETRO_TYPE").toString());
				res.setNoRetroCess(resMap.get("RETRO_CESSIONARIES")==null?"0":resMap.get("RETRO_CESSIONARIES").toString());
				if (resMap.get("RSK_BASIS") != null && !"0".equals(resMap.get("RSK_BASIS"))) {
					List<Map<String, Object>> result = queryImpl.selectList("risk.select.getDtlName",new String[]{resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString()});
					if(!CollectionUtils.isEmpty(result)) {
						res.setBasis(result.get(0).get("DETAIL_NAME")==null?"":result.get(0).get("DETAIL_NAME").toString());
					}
				}
				res.setPnoc(resMap.get("RSK_PERIOD_OF_NOTICE")==null?"":resMap.get("RSK_PERIOD_OF_NOTICE").toString());
				res.setRiskCovered(resMap.get("RSK_RISK_COVERED")==null?"":resMap.get("RSK_RISK_COVERED").toString());
				res.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
				res.setTerritory(resMap.get("TERRITORY_DESCR")==null?"":resMap.get("TERRITORY_DESCR").toString());
				res.setInwardType(resMap.get("INWARD_BUS_TYPE")==null?"":resMap.get("INWARD_BUS_TYPE").toString());
				res.setTreatyType(resMap.get("TREATYTYPE")==null?"":resMap.get("TREATYTYPE").toString());
				res.setTreatyName(resMap.get("TREATYTYPE_NAME")==null?"":resMap.get("TREATYTYPE_NAME").toString());
				res.setLOCIssued(resMap.get("RSK_LOC_ISSUED")==null?"":resMap.get("RSK_LOC_ISSUED").toString());
				res.setPerilCovered(resMap.get("RSK_PERILS_COVERED")==null ? "" : resMap.get("RSK_PERILS_COVERED").toString());
				if("0".equalsIgnoreCase(res.getPerilCovered())){
					res.setPerilCovered("None");
				}else{
				res.setPerilCovered(resMap.get("RSK_PERILS_COVERED_Con")==null ? "" : resMap.get("RSK_PERILS_COVERED_Con").toString());
				}
				res.setTreatynoofLine(resMap.get("RSK_NO_OF_LINE")==null?"0":resMap.get("RSK_NO_OF_LINE").toString());
				res.setRetentionYN(resMap.get("RETENTIONYN")==null?"":resMap.get("RETENTIONYN").toString());
				if (resMap.get("RSK_STATUS") != null) {
					if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("P")||"0".equalsIgnoreCase(resMap.get("RSK_STATUS").toString())) {
						res.setStatus("Pending");
					}else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("N")) {
						res.setStatus("Not Taken Up");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("A")) {
						res.setStatus("Accepted");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("R")) {
						res.setStatus("Rejected");
					} else {
						res.setStatus("Pending");
					}
				}
				res.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
				res.setRunoffYear(resMap.get("RSK_RUN_OFF_YEAR")==null?"":resMap.get("RSK_RUN_OFF_YEAR").toString());
				res.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
				res.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
				res.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
				res.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
				res.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString());
				res.setEndorsmenttype(resMap.get("RS_ENDORSEMENT_TYPE")==null?"":resMap.get("RS_ENDORSEMENT_TYPE").toString());
				
				//String qry = "SELECT   RTRIM(XMLAGG(XMLELEMENT(E,TERRITORY_NAME,',')).EXTRACT('//text()'),',') TERRITORY_NAME FROM   TMAS_TERRITORY  WHERE  TERRITORY_ID in("+res.getTerritory()+") and BRANCH_CODE="+req.getBranchCode();
				String qry ="select_TERRITORY_NAME";
				if(StringUtils.isNotBlank(res.getTerritory())){
					List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getTerritory(),req.getBranchCode()});
					if(!CollectionUtils.isEmpty(result)) {
						res.setTerritoryName(result.get(0).get("TERRITORY_NAME")==null?"":result.get(0).get("TERRITORY_NAME").toString());
					}
			
				}
				res.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE")==null?"":resMap.get("COUNTRIES_INCLUDE").toString());
//				if(StringUtils.isNotBlank(res.getCountryIncludedList())){
//			//	qry ="SELECT   RTRIM(XMLAGG(XMLELEMENT(E,COUNTRY_NAME,',')).EXTRACT('//text()'),',') COUNTRY_NAME FROM   country_master  WHERE  COUNTRY_ID in("+res.getCountryIncludedList()+") and BRANCH_CODE="+req.getBranchCode();
//				qry="select_COUNTRY_NAME";
//				List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getCountryIncludedList(),req.getBranchCode()});
//				if(!CollectionUtils.isEmpty(result)) {
//					res.setCountryIncludedName(result.get(0).get("COUNTRY_NAME")==null?"":result.get(0).get("COUNTRY_NAME").toString());
//				}
//				
//				res.setCountryIncludedName(res.getCountryIncludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
//				
//				}
				res.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE")==null?"":resMap.get("COUNTRIES_EXCLUDE").toString());
//				if(StringUtils.isNotBlank(res.getCountryExcludedList())){
//				//qry ="SELECT   RTRIM(XMLAGG(XMLELEMENT(E,COUNTRY_NAME,',')).EXTRACT('//text()'),',')  FROM   country_master  WHERE  COUNTRY_ID in("+beanObj.getCountryExcludedList()+") and BRANCH_CODE="+beanObj.getBranchCode();
//					qry ="select_COUNTRY_NAME1";
//				List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getCountryExcludedList(),req.getBranchCode()});
//				if(!CollectionUtils.isEmpty(result)) {
//					res.setCountryExcludedName(result.get(0).get("COUNTRY_NAME")==null?"":result.get(0).get("COUNTRY_NAME").toString());
//				}
//				
//				res.setCountryExcludedName(res.getCountryExcludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
//				}
			}

			
			//risk.select.getSecondViewData
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> pm = query.from(TtrnRiskProposal.class);
			//detailName
			Subquery<String> detailName = query.subquery(String.class);
			Root<ConstantDetail> rds = detailName.from(ConstantDetail.class);
			detailName.select(rds.get("detailName"));
			Predicate a1 = cb.equal(rds.get("categoryId"), "31");
			Predicate a2 = cb.equal(rds.get("categoryDetailId"), pm.get("rskPremiumBasis"));
			detailName.where(a1, a2);
			
			query.multiselect(pm.get("rskMdInstallmentNos").alias("RSK_MD_INSTALLMENT_NOS"),
					cb.selectCase().when(cb.equal(pm.get("rskCedretType"),"P"),"Percentage").otherwise("Amount").alias("CEDRET_TYPE"),
					pm.get("rskLimitOc").alias("RSK_LIMIT_OC"),pm.get("rskLimitDc").alias("RSK_LIMIT_DC"),
					pm.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pm.get("rskEpiOfferDc").alias("RSK_EPI_OFFER_DC"),
					pm.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),pm.get("rskXlpremOc").alias("RSK_XLPREM_OC"),
					pm.get("rskXlpremDc").alias("RSK_XLPREM_DC"),pm.get("rskDeducOc").alias("RSK_DEDUC_OC"),
					pm.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pm.get("rskEpiEstDc").alias("RSK_EPI_EST_DC"),
					pm.get("rskXlcostOc").alias("RSK_XLCOST_OC"),pm.get("rskXlcostDc").alias("RSK_XLCOST_DC"),
					pm.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pm.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
					pm.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),pm.get("rskMdPremOc").alias("RSK_MD_PREM_OC"),
					pm.get("rskMdPremDc").alias("RSK_MD_PREM_DC"),pm.get("rskAdjrate").alias("RSK_ADJRATE"),
					pm.get("rskPfCovered").alias("RSK_PF_COVERED"),pm.get("rskSubjPremiumOc").alias("RSK_SUBJ_PREMIUM_OC"),
					pm.get("rskSubjPremiumDc").alias("RSK_SUBJ_PREMIUM_DC"),pm.get("rskDeducDc").alias("RSK_DEDUC_DC"),
					pm.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),pm.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
					pm.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),pm.get("rskEpiOsofDc").alias("RSK_EPI_OSOF_DC"),
					pm.get("rskEpiOsoeOc").alias("RSK_EPI_OSOE_OC"),pm.get("rskEpiOsoeDc").alias("RSK_EPI_OSOE_DC"),
					pm.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC"),pm.get("rskXlcostOsDc").alias("RSK_XLCOST_OS_DC"),
					pm.get("rskMdPremOsOc").alias("RSK_MD_PREM_OS_OC"),pm.get("rskMdPremOsDc").alias("RSK_MD_PREM_OS_DC"),
					cb.selectCase().when(cb.equal(pm.get("rskSpRetro"),"Y"),"Yes").otherwise("No").alias("RSK_SP_RETRO"),
					pm.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pm.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
					pm.get("egpniAsOffer").alias("EGPNI_AS_OFFER"),pm.get("egpniAsOfferDc").alias("EGPNI_AS_OFFER_DC"),
					pm.get("ourassessment").alias("OURASSESSMENT"),pm.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),
					pm.get("rskTreatySurpLimitOsOc").alias("RSK_TREATY_SURP_LIMIT_OS_OC"),pm.get("rskTreatySurpLimitOsDc").alias("RSK_TREATY_SURP_LIMIT_OS_DC"),
					pm.get("rskEventLimitOc").alias("RSK_EVENT_LIMIT_OC"),pm.get("rskEventLimitDc").alias("RSK_EVENT_LIMIT_DC"),
					pm.get("rskEventLimitOsOc").alias("RSK_EVENT_LIMIT_OS_OC"),pm.get("rskEventLimitOsDc").alias("RSK_EVENT_LIMIT_OS_DC"),
					pm.get("rskCoverLimitUxlOsDc").alias("RSK_COVER_LIMIT_UXL_OS_DC"),pm.get("rskDeductableUxlOc").alias("RSK_DEDUCTABLE_UXL_OC"),
					pm.get("rskDeductableUxlDc").alias("RSK_DEDUCTABLE_UXL_DC"),pm.get("rskDeductableUxlOsOc").alias("RSK_DEDUCTABLE_UXL_OS_OC"),
					pm.get("rskDeductableUxlOsDc").alias("RSK_DEDUCTABLE_UXL_OS_DC"),
					cb.selectCase().when(cb.equal(pm.get("rskPml"),"Y"),"Yes").otherwise("No").alias("RSK_PML"),
					pm.get("rskPmlPercent").alias("RSK_PML_PERCENT"),	pm.get("rskEgnpiPmlOc").alias("RSK_EGNPI_PML_OC"),
					pm.get("rskEgnpiPmlDc").alias("RSK_EGNPI_PML_DC"),pm.get("rskEgnpiPmlOsOc").alias("RSK_EGNPI_PML_OS_OC"),
					pm.get("rskEgnpiPmlOsDc").alias("RSK_EGNPI_PML_OS_DC"),pm.get("rskPremiumBasis").alias("RSK_PREMIUM_BASIS"),
					pm.get("rskMinimumRate").alias("RSK_MINIMUM_RATE"),pm.get("rskMaxiimumRate").alias("RSK_MAXIIMUM_RATE"),
					pm.get("rskBurningCostLf").alias("RSK_BURNING_COST_LF"),pm.get("rskPaymentDueDays").alias("RSK_PAYMENT_DUE_DAYS"),
					detailName.alias("RSK_PREMIUM_BASIS_Con"),pm.get("rskTrtyLmtPmlOsOc").alias("RSK_TRTY_LMT_PML_OS_OC"),
					pm.get("rskTrtyLmtPmlOsDc").alias("RSK_TRTY_LMT_PML_OS_DC"),pm.get("rskTrtyLmtSurPmlDc").alias("RSK_TRTY_LMT_SUR_PML_DC"),
					pm.get("rskTrtyLmtSurPmlOsOc").alias("RSK_TRTY_LMT_SUR_PML_OS_OC"),pm.get("rskTrtyLmtSurPmlOsDc").alias("RSK_TRTY_LMT_SUR_PML_OS_DC"),
					pm.get("rskMinimumPremiumOc").alias("RSK_MINIMUM_PREMIUM_OC"),pm.get("rskMinimumPremiumDc").alias("RSK_MINIMUM_PREMIUM_DC"),
					pm.get("rskMinimumPremiumOsOc").alias("RSK_MINIMUM_PREMIUM_OS_OC"),pm.get("rskMinimumPremiumOsDc").alias("RSK_MINIMUM_PREMIUM_OS_DC"),
					pm.get("rskCoverLimitUxlOc").alias("RSK_COVER_LIMIT_UXL_OC"),pm.get("rskCoverLimitUxlDc").alias("RSK_COVER_LIMIT_UXL_DC"),
					pm.get("rskCoverLimitUxlOsOc").alias("RSK_COVER_LIMIT_UXL_OS_OC"),pm.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
					pm.get("rskTrtyLmtPmlDc").alias("RSK_TRTY_LMT_PML_DC"),pm.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),
					pm.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"));

			Predicate n1 = cb.equal(pm.get("rskProposalNumber"), req.getProposalNo());
			Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), req.getAmendId());
			query.where(n1,n2);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> result1 = res1.getResultList();
			
			Tuple secViewDataMap = null;
			if(result1!=null && result1.size()>0)
				secViewDataMap = result1.get(0);
			if (secViewDataMap != null) {
				if( res.getTreatyType().equalsIgnoreCase("4") ||  res.getTreatyType().equalsIgnoreCase("5") ){
					res.setFaclimitOrigCur(secViewDataMap.get("RSK_LIMIT_OC")==null?"0":fm.formatter(secViewDataMap.get("RSK_LIMIT_OC").toString()));
					res.setFacLimitOrigCurDc(secViewDataMap.get("RSK_LIMIT_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_DC").toString()));
					res.setLimitOrigCur("0.00");
					res.setLimitOrigCurDc("0.00");
				}
				else{
					res.setFaclimitOrigCur("0.00");
					res.setFacLimitOrigCurDc("0.00");
					res.setLimitOrigCur(secViewDataMap.get("RSK_LIMIT_OC")==null?"0":fm.formatter(secViewDataMap.get("RSK_LIMIT_OC").toString()));
					res.setLimitOrigCurDc(secViewDataMap.get("RSK_LIMIT_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_DC").toString()));
				}
				res.setEpiorigCur(secViewDataMap.get("RSK_EPI_OFFER_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OFFER_OC").toString()));
				res.setEpiorigCurDc(secViewDataMap.get("RSK_EPI_OFFER_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OFFER_DC").toString()));
				res.setOurEstimate(secViewDataMap.get("RSK_EPI_ESTIMATE")==null?"":secViewDataMap.get("RSK_EPI_ESTIMATE").toString());
				res.setXlPremium(secViewDataMap.get("RSK_XLPREM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLPREM_OC").toString()));
				res.setXlPremiumDc(secViewDataMap.get("RSK_XLPREM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLPREM_DC").toString()));
				res.setDeduchunPercent(secViewDataMap.get("RSK_DEDUC_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUC_OC").toString()));
				res.setEpiEstmate(secViewDataMap.get("RSK_EPI_EST_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_EST_OC").toString()));
				res.setEpiEstmateDc(secViewDataMap.get("RSK_EPI_EST_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_EST_DC").toString()));
				res.setXlCost(secViewDataMap.get("RSK_XLCOST_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OC").toString()));
				res.setXlCostDc(secViewDataMap.get("RSK_XLCOST_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_DC").toString()));
				res.setCedReten(secViewDataMap.get("RSK_CEDANT_RETENTION")==null?"":fm.formatter(secViewDataMap.get("RSK_CEDANT_RETENTION").toString()));
				
				res.setShareWritt(secViewDataMap.get("RSK_SHARE_WRITTEN")==null?"":dropDowmImpl.formatterpercentage(secViewDataMap.get("RSK_SHARE_WRITTEN").toString()));
				res.setSharSign(secViewDataMap.get("RSK_SHARE_SIGNED")==null?"":dropDowmImpl.formatterpercentage(secViewDataMap.get("RSK_SHARE_SIGNED").toString()));
				res.setMdPremium(secViewDataMap.get("RSK_MD_PREM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OC").toString()));
				res.setMdpremiumDc(secViewDataMap.get("RSK_MD_PREM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_DC").toString()));
				res.setAdjRate(secViewDataMap.get("RSK_ADJRATE")==null?"":fm.formattereight(secViewDataMap.get("RSK_ADJRATE").toString())); //Ri
				res.setPortfoloCovered(secViewDataMap.get("RSK_PF_COVERED")==null?"":secViewDataMap.get("RSK_PF_COVERED").toString());
				res.setSubPremium(secViewDataMap.get("RSK_SUBJ_PREMIUM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_SUBJ_PREMIUM_OC").toString()));
				res.setSubPremiumDc(secViewDataMap.get("RSK_SUBJ_PREMIUM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_SUBJ_PREMIUM_DC").toString()));
				res.setMaxLimitProduct(secViewDataMap.get("RSK_MAX_LMT_COVER")==null?"":fm.formatter(secViewDataMap.get("RSK_MAX_LMT_COVER").toString()));
				res.setDeduchunPercentDc(secViewDataMap.get("RSK_DEDUC_DC")==null?"":secViewDataMap.get("RSK_DEDUC_DC").toString());
				res.setLimitOurShare(secViewDataMap.get("RSK_LIMIT_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_OS_OC").toString()));
				res.setLimitOurShareDc(secViewDataMap.get("RSK_LIMIT_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_OS_DC").toString()));
				res.setEpiAsPerOffer(secViewDataMap.get("RSK_EPI_OSOF_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOF_OC").toString()));
				res.setEpiAsPerOfferDc(secViewDataMap.get("RSK_EPI_OSOF_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOF_DC").toString()));
				
				res.setEpiOurShareEs(secViewDataMap.get("RSK_EPI_OSOE_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOE_OC").toString()));
				res.setEpiOurShareEsDc(secViewDataMap.get("RSK_EPI_OSOE_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOE_DC").toString()));
				res.setXlcostOurShare(secViewDataMap.get("RSK_XLCOST_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OS_OC").toString()));
				res.setXlcostOurShareDc(secViewDataMap.get("RSK_XLCOST_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OS_DC").toString()));
				res.setMdpremiumourservice(secViewDataMap.get("RSK_MD_PREM_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OS_OC").toString()));
				res.setMdpremiumourserviceDc(secViewDataMap.get("RSK_MD_PREM_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OS_DC").toString()));
				res.setSpRetro(secViewDataMap.get("RSK_SP_RETRO")==null?"0":secViewDataMap.get("RSK_SP_RETRO").toString());
				res.setNoInsurer(secViewDataMap.get("RSK_NO_OF_INSURERS")==null?"0":secViewDataMap.get("RSK_NO_OF_INSURERS").toString());
				res.setMaxLimitProduct(secViewDataMap.get("RSK_MAX_LMT_COVER")==null?"0":fm.formatter(secViewDataMap.get("RSK_MAX_LMT_COVER").toString()));
				res.setCedRetenType(secViewDataMap.get("CEDRET_TYPE")==null?"0":secViewDataMap.get("CEDRET_TYPE").toString());
				res.setTreatyLimitsurplusOC(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OC").toString()));
				res.setTreatyLimitsurplusDC(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_DC").toString()));
				res.setTreatyLimitsurplusOurShare(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OS_OC").toString()));
				res.setTreatyLimitsurplusOurShareDC(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TREATY_SURP_LIMIT_OS_DC").toString()));
				res.setPml(secViewDataMap.get("RSK_PML")==null ? "" : secViewDataMap.get("RSK_PML").toString());
				res.setPmlPercent(secViewDataMap.get("RSK_PML_PERCENT")==null ? "" : secViewDataMap.get("RSK_PML_PERCENT").toString());
				res.setLimitOrigCurPml(secViewDataMap.get("RSK_TRTY_LMT_PML_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_PML_OC").toString()));
				res.setLimitOrigCurPmlDC(secViewDataMap.get("RSK_TRTY_LMT_PML_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_PML_DC").toString()));
				res.setLimitOrigCurPmlOS(secViewDataMap.get("RSK_TRTY_LMT_PML_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_PML_OS_OC").toString()));
				res.setLimitOrigCurPmlOSDC(secViewDataMap.get("RSK_TRTY_LMT_PML_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_PML_OS_DC").toString()));
				res.setTreatyLimitsurplusOCPml(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OC").toString()));
				res.setTreatyLimitsurplusOCPmlDC(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_DC").toString()));
				res.setTreatyLimitsurplusOCPmlOS(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OS_OC").toString()));
				res.setTreatyLimitsurplusOCPmlOSDC(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_TRTY_LMT_SUR_PML_OS_DC").toString()));
			}
			args = new String[3];
			args[0] = req.getProposalNo();
			args[1] = req.getLayerNo();
			args[2] = req.getAmendId();
			int number = 0;
			//risk.select.viewInstalmentData
			
			List<TtrnMndInstallments> instalmentList = ttrnMndInstallmentsRepository.findByProposalNoAndLayerNoAndEndorsementNoAndEndorsementNoNotNullOrderByInstallmentNo(
					req.getProposalNo(),new BigDecimal(req.getLayerNo()),new BigDecimal(req.getAmendId()));
			List<InstalmentListRes> instalmentResList = new ArrayList<InstalmentListRes>();
			if (instalmentList != null) {
//				List<String> dateList = new ArrayList<String>();
//				List<String> premiumList = new ArrayList<String>();
				
				for (number = 0; number < instalmentList.size(); number++) {
					TtrnMndInstallments insMap = instalmentList.get(number);
					InstalmentListRes instalmentRes = new InstalmentListRes();
					instalmentRes.setDateList(insMap.getInstallmentDate()==null?"":insMap.getInstallmentDate().toString());
					instalmentRes.setPremiumList(insMap.getMndPremiumOc()==null?"":fm.formatter(insMap.getMndPremiumOc().toString()));	
					instalmentResList.add(instalmentRes);
				}
							res.setInstalmentList(instalmentResList);
				}
			args = new String[5];
			args[0] = req.getBranchCode();
			args[1] = req.getProposalNo();
			args[2] = req.getAmendId();
			args[3] = req.getProposalNo();
			args[4] = req.getAmendId();
			selectQry = "risk.select.getThirdPageData"; // LEFT OUTER JOIN 
			
			List<Map<String, Object>> res3 = queryImpl.selectList(selectQry,args);
			
			Map<String, Object> thirdViewDataMap = null;
			if(res3!=null && res3.size()>0)
				thirdViewDataMap = (Map<String, Object>)res3.get(0);
			if (thirdViewDataMap != null) {
				for (int k = 0; k < res3.size(); k++) {
					res.setBrokerage(thirdViewDataMap.get("RSK_BROKERAGE")==null?"":thirdViewDataMap.get("RSK_BROKERAGE").toString());
					res.setTax(thirdViewDataMap.get("RSK_TAX")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_TAX").toString())); //Ri
					res.setShareProfitCommission(thirdViewDataMap.get("RSK_PROFIT_COMM")==null?"":thirdViewDataMap.get("RSK_PROFIT_COMM").toString());
					
					res.setPremiumQuotaShare(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE").toString()));
					res.setPremiumSurplus(thirdViewDataMap.get("RSK_PREMIUM_SURPULS")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_SURPULS").toString()));
					res.setPremiumQuotaShareDC(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE_DC").toString()));
					res.setPremiumSurplusDC(thirdViewDataMap.get("RSK_PREMIUM_SURPLUS_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_SURPLUS_DC").toString()));
					res.setPremiumQuotaShareOSOC(getShareVal(res.getPremiumQuotaShare().replaceAll(",", ""),req.getSharSign(),"share"));
					res.setPremiumSurplusOSOC(getShareVal(res.getPremiumSurplus().replaceAll(",", ""),req.getSharSign(),"share"));
					res.setPremiumQuotaShareOSDC(fm.formatter(getDesginationCountry(res.getPremiumQuotaShareOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setPremiumSurplusOSDC(fm.formatter(getDesginationCountry(res.getPremiumSurplusOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					
					
					res.setAcquisitionCost(thirdViewDataMap.get("RSK_ACQUISTION_COST_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_ACQUISTION_COST_OC").toString()));
					res.setAcquisitionCostDc(thirdViewDataMap.get("RSK_ACQUISTION_COST_DC")==null?"":thirdViewDataMap.get("RSK_ACQUISTION_COST_DC").toString());
					res.setAcquisitionCostOSOC(getShareVal(res.getAcquisitionCost().replaceAll(",", ""),req.getSharSign(),"share"));
					res.setAcquisitionCostOSDC(fm.formatter(getDesginationCountry(res.getAcquisitionCostOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setCommissionQS(thirdViewDataMap.get("RSK_COMM_QUOTASHARE")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_COMM_QUOTASHARE").toString()));
					res.setCommissionsurp(thirdViewDataMap.get("RSK_COMM_SURPLUS")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_COMM_SURPLUS").toString()));
					res.setOverRidder(thirdViewDataMap.get("RSK_OVERRIDER_PERC")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_OVERRIDER_PERC").toString()));
					
					res.setPremiumReserve(thirdViewDataMap.get("RSK_PREMIUM_RESERVE")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_PREMIUM_RESERVE").toString()));
					res.setLossreserve(thirdViewDataMap.get("RSK_LOSS_RESERVE")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_LOSS_RESERVE").toString()));
					res.setInterest(thirdViewDataMap.get("RSK_INTEREST")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_INTEREST").toString()));
					res.setPortfolioinoutPremium(thirdViewDataMap.get("RSK_PF_INOUT_PREM")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_PF_INOUT_PREM").toString()));
					res.setPortfolioinoutLoss(thirdViewDataMap.get("RSK_PF_INOUT_LOSS")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_PF_INOUT_LOSS").toString()));
					res.setLossAdvise(thirdViewDataMap.get("RSK_LOSSADVICE")==null?"":fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE").toString()));
					res.setLossAdviseDc(thirdViewDataMap.get("RSK_LOSSADVICE_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE_DC").toString()));
					res.setLossAdviseOSOC(getShareVal(res.getLossAdvise().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLossAdviseOSDC(fm.formatter(getDesginationCountry(res.getLossAdviseOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setCashLossLimit(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC").toString()));
					res.setCashLossLimitDc(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC").toString()));
					res.setCashLossLimitOSOC(getShareVal(res.getCashLossLimit().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setCashLossLimitOSDC(fm.formatter(getDesginationCountry(res.getCashLossLimitOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setAnualAggregateLiability(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC").toString()));
					res.setAnualAggregateLiabilityDc(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC").toString()));
					
					res.setReinstNo(thirdViewDataMap.get("RSK_REINSTATE_NO")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_NO").toString()));
					res.setReinstAditionalPremiumpercent(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString()));
					res.setReinstAditionalPremiumpercentDc(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC").toString()));
					res.setLeaderUnderwriter(thirdViewDataMap.get("RSK_LEAD_UWID")==null?"":thirdViewDataMap.get("RSK_LEAD_UWID").toString());
					if(!"64".equalsIgnoreCase(res.getLeaderUnderwriter())){
					res.setLeaderUnderwriter(thirdViewDataMap.get("RSK_LEAD_UW")==null?"":thirdViewDataMap.get("RSK_LEAD_UW").toString());
					}else if("64".equalsIgnoreCase(res.getLeaderUnderwriter())){
						
						res.setLeaderUnderwriter(getUGUWName(req.getBranchCode(),res.getLeaderUnderwriter()));
					}
					res.setLeaderUnderwritershare(thirdViewDataMap.get("RSK_LEAD_UW_SHARE")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_LEAD_UW_SHARE").toString()));
					res.setAccounts(thirdViewDataMap.get("RSK_ACCOUNTS")==null?"":thirdViewDataMap.get("RSK_ACCOUNTS").toString());
					res.setExclusion(thirdViewDataMap.get("RSK_EXCLUSION")==null?"":thirdViewDataMap.get("RSK_EXCLUSION").toString());
					res.setRemarks(thirdViewDataMap.get("RSK_REMARKS")==null?"":thirdViewDataMap.get("RSK_REMARKS").toString());
					res.setUnderwriterRecommendations(thirdViewDataMap.get("RSK_UW_RECOMM")==null?"":thirdViewDataMap.get("RSK_UW_RECOMM").toString());
					res.setGmsApproval(thirdViewDataMap.get("RSK_GM_APPROVAL")==null?"":thirdViewDataMap.get("RSK_GM_APPROVAL").toString());
					res.setDecision(thirdViewDataMap.get("RSK_DECISION")==null?"":thirdViewDataMap.get("RSK_DECISION").toString()); 
					res.setOthercost(thirdViewDataMap.get("RSK_OTHER_COST")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_OTHER_COST").toString()));
					res.setReinstAdditionalPremium(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString()));
					res.setBurningCost(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					res.setCommissionQSAmt(thirdViewDataMap.get("COMM_QS_AMT")==null?"":fm.formatter(thirdViewDataMap.get("COMM_QS_AMT").toString()));
					res.setCommissionsurpAmt(thirdViewDataMap.get("COMM_SURPLUS_AMT")==null?"":fm.formatter(thirdViewDataMap.get("COMM_SURPLUS_AMT").toString()));
					res.setCommissionQSAmtDC(thirdViewDataMap.get("COMM_QS_AMT_DC")==null?"":fm.formatter(thirdViewDataMap.get("COMM_QS_AMT_DC").toString()));
					res.setCommissionsurpAmtDC(thirdViewDataMap.get("COMM_SURPLUS_AMT_DC")==null?"":fm.formatter(thirdViewDataMap.get("COMM_SURPLUS_AMT_DC").toString()));
				if(StringUtils.isNotBlank(res.getCommissionQS()) &&StringUtils.isNotBlank(res.getCommissionsurp())&& StringUtils.isNotBlank(res.getOverRidder())&&StringUtils.isNotBlank(res.getBrokerage())&&StringUtils.isNotBlank(res.getTax())&&StringUtils.isNotBlank(res.getOthercost())){
					res.setAcqCostPer(fm.formatter((Double.parseDouble(res.getCommissionQS().replaceAll(",",""))+Double.parseDouble(res.getCommissionsurp().replaceAll(",",""))+Double.parseDouble(res.getOverRidder().replaceAll(",",""))+Double.parseDouble(res.getBrokerage().replaceAll(",",""))+Double.parseDouble(res.getTax().replaceAll(",",""))+Double.parseDouble(res.getOthercost().replaceAll(",","")))+""));
					res.setAcqCostPerDC(fm.formatter(getDesginationCountry(StringUtils.isBlank(res.getAcqCostPer())?"0":res.getAcqCostPer().replaceAll(",", ""),res.getExchRate()).toString()));
				}
					res.setReinstAditionalPremiumpercentDc(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString()));
					res.setBurningCost(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					res.setBrokerage(thirdViewDataMap.get("RSK_BROKERAGE")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_BROKERAGE").toString()));
					res.setLimitPerVesselOC(thirdViewDataMap.get("LIMIT_PER_VESSEL_OC")==null?"0":fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_OC").toString()));
					res.setLimitPerVesselDC(thirdViewDataMap.get("LIMIT_PER_VESSEL_DC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_DC").toString()));
					res.setLimitPerVesselOSOC(getShareVal(res.getLimitPerVesselOC().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLimitPerVesselOSDC(fm.formatter(getDesginationCountry(res.getLimitPerVesselOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					
					res.setLimitPerLocationOC(thirdViewDataMap.get("LIMIT_PER_LOCATION_OC")==null?"0":fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_OC").toString()));
					res.setLimitPerLocationDC(thirdViewDataMap.get("LIMIT_PER_LOCATION_DC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_DC").toString()));
					res.setLimitPerLocationOSOC(getShareVal(res.getLimitPerLocationOC().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLimitPerLocationOSDC(fm.formatter(getDesginationCountry(res.getLimitPerLocationOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setEndorsementDate(thirdViewDataMap.get("ENDT_DATE")==null?"":thirdViewDataMap.get("ENDT_DATE").toString());
					res.setEventlimit(thirdViewDataMap.get("RSK_EVENT_LIMIT_OC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_EVENT_LIMIT_OC").toString()));
					res.setEventlimitDC(thirdViewDataMap.get("RSK_EVENT_LIMIT_DC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_EVENT_LIMIT_DC").toString()));
					res.setEventlimitOSOC(getShareVal(res.getEventlimit().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setEventlimitOSDC(fm.formatter(getDesginationCountry(res.getEventlimitOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setAggregateLimit(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_OC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_OC").toString()));
					res.setAggregateLimitDC(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_DC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_DC").toString()));
					res.setAggregateLimitOSOC(getShareVal(res.getAggregateLimit().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setAggregateLimitOSDC(fm.formatter(getDesginationCountry(res.getAggregateLimitOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setOccurrentLimit(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC").toString()));
					res.setOccurrentLimitDC(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC")==null?"0":fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC").toString()));
					res.setOccurrentLimitOSOC(getShareVal(res.getOccurrentLimit().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setOccurrentLimitOSDC(fm.formatter(getDesginationCountry(res.getOccurrentLimitOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					
					res.setSlideScaleCommission(thirdViewDataMap.get("RSK_SLADSCALE_COMM")==null?"":thirdViewDataMap.get("RSK_SLADSCALE_COMM").toString());
					res.setLossParticipants(thirdViewDataMap.get("RSK_LOSS_PART_CARRIDOR")==null?"":thirdViewDataMap.get("RSK_LOSS_PART_CARRIDOR").toString());
					res.setCommissionSubClass(thirdViewDataMap.get("RSK_COMBIN_SUB_CLASS")==null?"":thirdViewDataMap.get("RSK_COMBIN_SUB_CLASS").toString());
					res.setLeaderUnderwritercountry(thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
					res.setOrginalacqcost(thirdViewDataMap.get("RSK_INCLUDE_ACQ_COST")==null?"":thirdViewDataMap.get("RSK_INCLUDE_ACQ_COST").toString());
					res.setOuracqCost(thirdViewDataMap.get("RSK_OUR_ACQ_OUR_SHARE_OC")==null?"":thirdViewDataMap.get("RSK_OUR_ACQ_OUR_SHARE_OC").toString());
					res.setOurassessmentorginalacqcost(thirdViewDataMap.get("RSK_OUR_ASS_ACQ_COST")==null?"":thirdViewDataMap.get("RSK_OUR_ASS_ACQ_COST").toString());
					res.setProfitCommission(thirdViewDataMap.get("RSK_PRO_NOTES")==null?"":thirdViewDataMap.get("RSK_PRO_NOTES").toString());
					res.setLosscommissionSubClass(thirdViewDataMap.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":thirdViewDataMap.get("RSK_LOSS_COMBIN_SUB_CLASS").toString());
					res.setSlidecommissionSubClass(thirdViewDataMap.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"":thirdViewDataMap.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString());
					res.setCrestacommissionSubClass(thirdViewDataMap.get("RSK_CRESTA_COMBIN_SUB_CLASS")==null?"":thirdViewDataMap.get("RSK_CRESTA_COMBIN_SUB_CLASS").toString());
					res.setManagementExpenses(thirdViewDataMap.get("RSK_PRO_MANAGEMENT_EXP")==null?"":thirdViewDataMap.get("RSK_PRO_MANAGEMENT_EXP").toString());
					res.setCommissionType(thirdViewDataMap.get("RSK_PRO_COMM_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_COMM_TYPE").toString());
					res.setProfitCommissionPer(thirdViewDataMap.get("RSK_PRO_COMM_PER")==null?"":dropDowmImpl.formattereight(thirdViewDataMap.get("RSK_PRO_COMM_PER").toString()));
					res.setSetup(thirdViewDataMap.get("RSK_PRO_SET_UP")==null?"":thirdViewDataMap.get("RSK_PRO_SET_UP").toString());
					res.setSuperProfitCommission(thirdViewDataMap.get("RSK_PRO_SUP_PRO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_SUP_PRO_COM").toString());
					res.setLossCarried(thirdViewDataMap.get("RSK_PRO_LOSS_CARY_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_LOSS_CARY_TYPE").toString());
					res.setLossyear(thirdViewDataMap.get("RSK_PRO_LOSS_CARY_YEAR")==null?"":thirdViewDataMap.get("RSK_PRO_LOSS_CARY_YEAR").toString());
					res.setProfitCarried(thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_TYPE").toString());
					res.setProfitCarriedForYear(thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_YEAR")==null?"":thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_YEAR").toString());
					res.setFistpc(thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM").toString());
					res.setProfitMont(thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM_PRD")==null?"":thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM_PRD").toString());
					res.setSubProfitMonth(thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM_PRD")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM_PRD").toString());
					res.setSubpc(thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM").toString());
					res.setSubSeqCalculation(thirdViewDataMap.get("RSK_PRO_SUB_SEQ_CAL")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_SEQ_CAL").toString());
					res.setCrestaStatus(thirdViewDataMap.get("RSK_CREASTA_STATUS")==null?"":thirdViewDataMap.get("RSK_CREASTA_STATUS").toString());
					res.setLocRate(thirdViewDataMap.get("RSK_RATE")==null?"":thirdViewDataMap.get("RSK_RATE").toString());
					
					
					if("NR".equalsIgnoreCase(res.getLocRate())){
						res.setLocRate("On Net Rate");
					}
					else if("GR".equalsIgnoreCase(res.getLocRate())){
						res.setLocRate("On Gross Rate");
					}
				}
			}
			//GET_POSITION_MASTER_CON_MAP
			PositionMaster result = positionMasterRepository.findByProposalNoAndAmendId(new BigDecimal(req.getProposalNo()),new BigDecimal(req.getProposalNo()));
			if(result!=null) {
				res.setContractListVal(result.getDataMapContNo()==null?"":result.getDataMapContNo().toString());
			}
			
			if(StringUtils.isNotBlank(res.getContractListVal()) && !"None".equalsIgnoreCase(res.getContractListVal())){
				//GET_MAPPING_PROPOSAL_NO
				CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
				Root<PositionMaster> m = query1.from(PositionMaster.class);
				query1.multiselect(m.get("proposalNo").alias("PROPOSAL_NO"),m.get("amendId").alias("AMEND_ID"));
				
				//amend
				Subquery<Long> amend = query1.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("rskEndorsementNo")));
				Predicate b1 = cb.equal(m.get("proposalNo"), pms.get("proposalNo"));
				Predicate b2 = cb.equal(m.get("contractNo"), pms.get("contractNo"));
				Predicate b3 = cb.equal(m.get("layerNo"), pms.get("layerNo"));
				amend.where(b1,b2,b3);

				Predicate m1 = cb.equal(m.get("contractNo"), res.getContractListVal());
				Predicate m2 = cb.equal(m.get("layerNo"), res.getLayerNo());
				Predicate m3 = cb.equal(m.get("deptId"), res.getDepartId());
				Predicate m4 = cb.equal(m.get("uwYear"), res.getUwYear());
				Predicate m5 = cb.equal(m.get("amendId"), amend);
				query1.where(m1,m2,m3,m4,m5);
				
				TypedQuery<Tuple> res2 = em.createQuery(query1);
				List<Tuple> list1 = res2.getResultList();
			
				List<MappingProposalRes> mapResList = new ArrayList<MappingProposalRes>();
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						Tuple map =list1.get(i);
						MappingProposalRes mapRes = new MappingProposalRes();
						mapRes.setMappingProposal(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
						mapRes.setMapingAmendId(map.get("AMEND_ID")==null?"":map.get("AMEND_ID").toString());
						mapResList.add(mapRes);
						}
					res.setMappingProposal(mapResList);
				}
				}
			
			//GET_BASE_LAYER_DETAILS
			list2 = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getProposalNo());	
			
			Tuple resMap1 = null;
			if(list2!=null && list2.size()>0)
				resMap1 = list2.get(0);
			if (resMap1 != null) {
					res.setBaseLayer(resMap1.get("BASE_LAYER")==null?"":resMap1.get("BASE_LAYER").toString());
			}
		//	resList.add(res);
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
				}
			return response;
				}
	
	private String getShareVal(String lossAdvise, String signedShare, String type) {
		String res="";
		try{
			if(StringUtils.isBlank(lossAdvise)){
				lossAdvise="0";
			}
			if(StringUtils.isBlank(signedShare)){
				signedShare="0";
			}
			if("share".equalsIgnoreCase(type)){
			res=Double.toString((Double.parseDouble(lossAdvise)*Double.parseDouble(signedShare))/100);
			}
			res=fm.formatter(res);
		}
		catch (Exception e) {
			logger.debug("Exception @ {" + e + "}");

		}
		return res;
	}
	public String getUGUWName(String branchCode,String ugcode) {
		String code="";
		try{
			//GET_UG_UNDERWRITER
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<PersonalInfo> pm = query.from(PersonalInfo.class);
			
			Expression<String> e0 = cb.concat(pm.get("firstName")," ");
			query.multiselect(cb.concat(e0, pm.get("lastName")).alias("RSK_LEAD_UW_UG"));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfo> pms = amend.from(PersonalInfo.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal(pm.get("customerType"), pms.get("branchCode"));
			Predicate a3 = cb.equal(pm.get("status"), pms.get("branchCode"));
			Predicate a4 = cb.equal(pm.get("customerId"), pms.get("customerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("customerId"), ugcode);
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			Predicate n4 = cb.equal(pm.get("status"), "Y");
			query.where(n1,n2,n3,n4);
			
			TypedQuery<String> res1 = em.createQuery(query);
			List<String> list = res1.getResultList();
			 
			
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
				String map = list.get(i);
				code=map==null?"":map.toString();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return code;
	}
	public getprofitCommissionDeleteRes getprofitCommissionDelete(String proposalno, String branchCode,
			String profitSno) {
		getprofitCommissionDeleteRes response = new getprofitCommissionDeleteRes();
		try {
			String query="PROFIT_COMMISSION_DELETE";
			String [] args = new String[3];
			args[0] = proposalno;
			args[1] = branchCode;
			args[2] = profitSno;
			queryImpl.updateQuery(query, args);
			
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		
		return response;
	}
	@Override
	public showSecondPageData1Res showSecondPageData1(showSecondPageData1Req req) {
		showSecondPageData1Res response = new showSecondPageData1Res();
		ShowSecondPageData1Res1  resset= new ShowSecondPageData1Res1();
		List<ShowSecondPageData1Res1> ressetList = new ArrayList<ShowSecondPageData1Res1>();
		try {
			
//			(select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  
//					from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in( select * from table(SPLIT_TEXT_FN(replace(RK.RSK_SPFCID,' ', '')))) AND SPFC.TMAS_PRODUCT_ID 
//					= TDM.TMAS_PRODUCT_ID AND TDM.BRANCH_CODE = SPFC.BRANCH_CODE)TMAS_SPFC_NAME,
			
			//risk.select.getSecPageData
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PersonalInfo> personal = query.from(PersonalInfo.class);
			Root<TtrnRiskDetails> rk = query.from(TtrnRiskDetails.class);
			Root<TmasPfcMaster> pfc = query.from(TmasPfcMaster.class);
			Root<PersonalInfo> pi = query.from(PersonalInfo.class);
			Root<TmasPolicyBranch> bran = query.from(TmasPolicyBranch.class);
			Root<TmasDepartmentMaster> tdm = query.from(TmasDepartmentMaster.class); 
				
			Expression<String> e0 = cb.concat(pi.get("firstName")," ");
			query.multiselect(rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),    //TMAS_SPFC_NAME pending
					pfc.get("tmasPfcName").alias("TMAS_PFC_NAME"), personal.get("companyName").alias("COMPANY_NAME"),
					cb.concat(e0, pi.get("lastName")).alias("BROKER"),
					rk.get("rskMonth").alias("MONTH"),bran.get("tmasPolBranchName").alias("TMAS_POL_BRANCH_NAME"),
					rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),rk.get("rskUwyear").alias("RSK_UWYEAR"),
					tdm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),rk.get("rsEndorsementType").alias("DETAIL_NAME"));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfo> pms = amend.from(PersonalInfo.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pms.get("customerId"), personal.get("customerId"));
			Predicate a2 = cb.equal(personal.get("customerType"), pms.get("customerType"));
			Predicate a3 = cb.equal(personal.get("branchCode"), pms.get("branchCode"));
			amend.where(a1,a2,a3);
			//amendPi
			Subquery<Long> amendPi = query.subquery(Long.class); 
			Root<PersonalInfo> pis = amendPi.from(PersonalInfo.class);
			amendPi.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal(pis.get("customerId"), pi.get("customerId"));
			Predicate b2 = cb.equal(pi.get("customerType"), pis.get("customerType"));
			Predicate b3 = cb.equal(pi.get("branchCode"), pis.get("branchCode"));
			amendPi.where(b1,b2,b3);
			//end
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rks.get("rskEndorsementNo")));
			Predicate c1 = cb.equal(rks.get("rskProposalNumber"), rk.get("rskProposalNumber"));
			end.where(c1);
			
			Predicate n1 = cb.equal(rk.get("rskProposalNumber"), req.getProposal());
			Predicate n2 = cb.equal(pfc.get("tmasPfcId"), rk.get("rskPfcid"));
			Predicate n3 = cb.equal(pfc.get("branchCode"), req.getBranchCode());
			Predicate n4 = cb.equal(tdm.get("branchCode"), req.getBranchCode());
			Predicate n5 = cb.equal(tdm.get("tmasProductId"), req.getProductId());
			Predicate n6 = cb.equal(tdm.get("tmasDepartmentId"), rk.get("rskDeptid"));
			Predicate n7 = cb.equal(rk.get("rskCedingid"), personal.get("customerId"));
			Predicate n8 = cb.equal(personal.get("customerType"), "C");
			Predicate n9 = cb.equal(personal.get("branchCode"), req.getBranchCode());
			Predicate n10 = cb.equal(personal.get("amendId"), amend);
			Predicate n11 = cb.equal(rk.get("rskBrokerid"), pi.get("customerId"));
			Predicate n12 = cb.equal(pi.get("customerType"), "B");
			Predicate n13 = cb.equal(pi.get("branchCode"), req.getBranchCode());
			Predicate n14 = cb.equal(pi.get("amendId"), amendPi);
			Predicate n15 = cb.equal(rk.get("rskEndorsementNo"), end);
			Predicate n16 = cb.equal(rk.get("rskPolbranch"), bran.get("tmasPolBranchId"));
			Predicate n17 = cb.equal(bran.get("branchCode"), req.getBranchCode());
			query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			Tuple resMap = null;
			if(list!=null && list.size()>0) {
				resMap = list.get(0);
				if(resMap!=null) {
					resset.setProposalno(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
					resset.setSubProfitcenter(resMap.get("TMAS_SPFC_NAME")==null?"":resMap.get("TMAS_SPFC_NAME").toString());
					resset.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
					resset.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
					resset.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
					resset.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
					resset.setPolBr(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
					resset.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
					ressetList.add(resset);
				}
				}
				if(StringUtils.isNotBlank(req.getNoInsurer()) && Integer.parseInt(req.getNoInsurer())>0 ){
				if(req.getRetroFinalList().equalsIgnoreCase("No")) {
					List<RetroFinalListres>  finalList = new ArrayList<RetroFinalListres>();
					for(int i=0;i<Integer.parseInt(req.getNoInsurer());i++){
						RetroFinalListres retro = new RetroFinalListres();
						GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
						req2.setBranchCode(req.getBranchCode());
						req2.setIncepDate(req.getIncepDate());
						req2.setProductid(req.getProductId());
						req2.setRetroType(req.getRetroType());
						req2.setUwYear(req.getUwYear());
						GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,2,"");
					if (!CollectionUtils.isEmpty(dropDownRes.getCommonResponse())) {
					CommonResDropDown res2= dropDownRes.getCommonResponse().get(i);
					retro.setCONTDET1(res2.getCode());
					retro.setCONTDET2(res2.getCodeDescription());
					finalList.add(retro);
						}
				}
				}
				if(StringUtils.isNotBlank(req.getNoInsurer())){
					List<RetroFinalListres>  finalList = new ArrayList<RetroFinalListres>();
					for (int z=0;z<Integer.parseInt(req.getNoInsurer());z++){
						RetroFinalListres retro = new RetroFinalListres();
						retro.setRetroDupYerar(req.getUwYear());
						GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
						req2.setBranchCode(req.getBranchCode());
						req2.setIncepDate(req.getIncepDate());
						req2.setProductid(req.getProductId());
						req2.setRetroType(req.getRetroType());
						req2.setUwYear(req.getUwYear());
						GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,3,req2.getUwYear());
						if (!CollectionUtils.isEmpty(dropDownRes.getCommonResponse())) {
							List<CommonResDropDown> list1= dropDownRes.getCommonResponse();
							for (int i=0;i<list1.size();i++){
								RetroFinalListres retro1 = new RetroFinalListres();
									CommonResDropDown map = list1.get(i);
									retro1.setRetroDupContract(map.getCode());
									finalList.add(retro1);
								}
						resset.setRetroFinalListres(finalList);
						}
				}
				}
				if(req.getNoInsurer()!=null &&Integer.parseInt(req.getNoInsurer())==0){
					List<RetroFinalListres>  finalList = new ArrayList<RetroFinalListres>();
					RetroFinalListres retro = new RetroFinalListres();
					retro.setRetroDupYerar(req.getUwYear());
					
					GetRetroContractDetailsListReq req2 = new GetRetroContractDetailsListReq();
					req2.setBranchCode(req.getBranchCode());
					req2.setIncepDate(req.getIncepDate());
					req2.setProductid(req.getProductId());
					req2.setRetroType(req.getRetroType());
					req2.setUwYear(req.getRetroType());
					GetCommonDropDownRes dropDownRes =dropDowmImpl.getRetroContractDetailsList(req2,3,"");
					List<CommonResDropDown> list1= dropDownRes.getCommonResponse();
					
					for (int i=0;i<list1.size();i++){
						CommonResDropDown map = list1.get(i);
						retro.setRetroDupContract(map.getCode());
						finalList.add(retro);
					}
					
					}
				}
				response.setShowSecondPageData1Res1(ressetList);
				response.setMessage("Success");
				response.setIsError(false);
			}
			
			catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
			}
			return response;
	}
	@Override
	public getprofitCommissionEditRes getprofitCommissionEdit(String proposalno, String branchCode, String profitSno) {
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		getprofitCommissionEditRes response = new getprofitCommissionEditRes();
		getprofitCommissionEditRes1 resset = new getprofitCommissionEditRes1();
		List<getprofitCommissionEditRes1> ressetList = new ArrayList<getprofitCommissionEditRes1>();
		try {
			//PROFIT_COMMISSION_EDIT
//			CriteriaBuilder cb = em.getCriteriaBuilder(); 
//			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
//			Root<TtrnProfitCommissionArch> pm = query.from(TtrnFacRiskProposal.class);
//			query.multiselect(cb.count(pm)); 
//
//			Subquery<Long> end = query.subquery(Long.class); 
//			Root<TtrnFacRiskProposal> pms = end.from(TtrnFacRiskProposal.class);
//			end.select(cb.max(pms.get("rskEndorsementNo")));
//			Predicate a1 = cb.equal(pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
//			end.where(a1);
//
//			Predicate n1 = cb.equal(pm.get("shareSigned"), leaderUnderwriterShare);
//			Predicate n2 = cb.equal(pm.get("rskProposalNumber"), proposalNo);
//			Predicate n3 = cb.equal(pm.get("rskEndorsementNo"), end);
//			query.where(n1,n2,n3);
//			
//			TypedQuery<Tuple> res1 = em.createQuery(query);
//			List<Tuple> list = res1.getResultList();
			 String query = "PROFIT_COMMISSION_EDIT";
			String [] args = new String [3];
			args[0] = proposalno;
			args[1] = branchCode;
			args[2] = profitSno;
			result = queryImpl.selectList(query,args);
				for(int i=0;i<result.size();i++){
	               Map<String,Object> tempMap = result.get(i);
	               resset.setProfitSno(tempMap.get("S_NO")==null?"":tempMap.get("S_NO").toString());
	               resset.setManagementExpenses(tempMap.get("MANAGEMENT_EXPENSES")==null?"":tempMap.get("MANAGEMENT_EXPENSES").toString());
	               resset.setLossCF(tempMap.get("LOSS")==null?"":tempMap.get("LOSS").toString());
	               resset.setFistpc(tempMap.get("FIRST_PROFIT_COM_AFTER")==null?"":tempMap.get("FIRST_PROFIT_COM_AFTER").toString());
	               resset.setProfitMont(tempMap.get("PROFIT_MONTHS")==null?"":tempMap.get("PROFIT_MONTHS").toString());
	               resset.setProfitQuarters(tempMap.get("PROFIT_QUARTERS")==null?"":tempMap.get("PROFIT_QUARTERS").toString());
	               resset.setProfitYear(tempMap.get("PROFIT_YEAR")==null?"":tempMap.get("PROFIT_YEAR").toString());
	               resset.setProfitCommission(tempMap.get("PROFIT_COMMISSION")==null?"":tempMap.get("PROFIT_COMMISSION").toString());
	               ressetList.add(resset);			
				}
				response.setGetprofitCommissionEditRes1(ressetList);
				response.setMessage("Success");
				response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public ProfitCommissionListRes ProfitCommissionList(ProfitCommissionListReq req) {
		ProfitCommissionListRes response = new ProfitCommissionListRes();
		ProfitCommissionListRes1 resset = new ProfitCommissionListRes1();		
		List<ProfitCommissionListRes1> relist = new ArrayList<ProfitCommissionListRes1>();
		 try {
				//COMMISSION_TYPE_LIST
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnCommissionDetails> c = query.from(TtrnCommissionDetails.class);
				query.multiselect(c.get("sNo").alias("SNO"),c.get("commFrom").alias("COMM_FROM"),
						c.get("commTo").alias("COMM_TO"),
						c.get("profitComm").alias("PROFIT_COMM"));
						
				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnCommissionDetails> pms = end.from(TtrnCommissionDetails.class);
				end.select(cb.max(pms.get("endorsementNo")));
				Predicate a1 = cb.equal(c.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(c.get("branchCode"), pms.get("branchCode"));
				end.where(a1,a2);
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(c.get("sNo")));
	
				Predicate n1 = cb.equal(c.get("proposalNo"), req.getProposalno());
				Predicate n2 = cb.equal(c.get("branchCode"), req.getBranchCode());
				Predicate n3 = cb.equal(c.get("commissionType"), req.getCommissionType());
				Predicate n4 = cb.equal(c.get("endorsementNo"), end);
				
				
			 if(StringUtils.isNotBlank(req.getContractNo())){
				//COMMISSION_TYPE_LIST1
					Predicate n5 = cb.equal(c.get("contractNo"), req.getContractNo());
					query.where(n1,n2,n3,n4,n5).orderBy(orderList);
					}
			 else{
				 query.where(n1,n2,n3,n4).orderBy(orderList);
			 }
			 TypedQuery<Tuple> res1 = em.createQuery(query);
			 List<Tuple> result = res1.getResultList();

			for(int i=0;i<result.size();i++) {
				Tuple tempMap = result.get(i);	
				resset.setProfitSno(tempMap.get("SNO")==null?"":tempMap.get("SNO").toString());
				resset.setFrom(tempMap.get("COMM_FROM")==null?"":tempMap.get("COMM_FROM").toString());
				resset.setTo(tempMap.get("COMM_TO")==null?"":tempMap.get("COMM_TO").toString());
				resset.setCom(tempMap.get("PROFIT_COMM")==null?"":tempMap.get("PROFIT_COMM").toString());
				relist.add(resset);
			}
			 response.setProfitCommissionListRes1(relist);
				response.setMessage("Success");
				response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	private void MoveBonus(ScaleCommissionInsertReq req) throws ParseException {
	//	List<ScaleList> req2 = new ArrayList<ScaleList>();
		if(StringUtils.isBlank(req.getAmendId())){
			req.setAmendId("0");
		}
		
		BonusSaveReq req1 = new BonusSaveReq();
		req1.setProposalNo(req.getProposalNo());
		req1.setBranchCode(req.getBranchCode());
		req1.setLayerNo(req.getLayerNo());
		req1.setAmendId(req.getAmendId());
		deleteMaintable(req1,req.getType());
		
		  if(StringUtils.isBlank(req.getProposalNo()) && StringUtils.isBlank(req.getReferenceNo())) { //Ri
	        	String referenceNo="";
	        	String query="GET_REFERENCE_NO_SEQ";
	        	List<Map<String, Object>> list  = queryImpl.selectSingle(query,new String[]{}); 
	        	if (!CollectionUtils.isEmpty(list)) {
	        		referenceNo = list.get(0).get("REFERENCENO") == null ? ""
							: list.get(0).get("REFERENCENO").toString();
				}
	        	req.setReferenceNo(referenceNo);
	        }
		
		//BONUS_MAIN_INSERT_PTTY
		String [] args = new String [25];
		for(int i=0;i<req.getScaleList().size();i++) {
			ScaleList req3 = req.getScaleList().get(i);			
			if("MB".equals(req.getScalementhod()) ||StringUtils.isNotBlank(req3.getScaleFrom()) && StringUtils.isNotBlank(req3.getScaleTo()) && StringUtils.isNotBlank(req3.getScaleLowClaimBonus())) {
				args[0] = req.getProposalNo();
				args[1] = req.getContractNo();
				args[2] = req.getProductid();
				if("scale".equalsIgnoreCase(req.getPageFor())){
					args[3] = "SSC2";
				}
				else {
					args[3] = req.getBonusTypeId();				
					}
				if(!"MB".equals(req.getScalementhod())) {
					args[9] = req3.getScaleSNo().replace(",", "");		
					args[4] = req3.getScaleFrom().replace(",", "");
					args[5] = req3.getScaleTo().replace(",", "");
					args[6] = req3.getScaleLowClaimBonus().replace(",", "");
				 }else {
		        	   args[9] = "";
			           args[4] = "";
			           args[5] = "";
			           args[6] = "";
		           }
					args[7] = req.getLoginId();
					args[8] = req.getBranchCode();
			
			if("scale".equalsIgnoreCase(req.getPageFor())){
	        	   args[10] ="SSC";
	           } 
			else{
	        	   args[10]="LPC";
	           }
			args[11] = req.getAmendId();
			args[12] = req.getDepartmentId();
			args[13] = StringUtils.isEmpty(req.getLayerNo()) ? "0" : req.getLayerNo();
			if("scale".equalsIgnoreCase(req.getPageFor())){
	        	   args[14] =req.getQuotaShare();
					}
			 else{
	        	   args[14]="";
	           }
		   args[15] =StringUtils.isEmpty(req.getBonusremarks()) ? "" :req.getBonusremarks();
           args[16] =StringUtils.isEmpty(req.getScFistpc()) ? "" :req.getScFistpc();
           args[17] =StringUtils.isEmpty(req.getScProfitMont()) ? "" :req.getScProfitMont();
           args[18] =StringUtils.isEmpty(req.getScSubpc()) ? "" :req.getScSubpc();
           args[19] =StringUtils.isEmpty(req.getScSubProfitMonth()) ? "" :req.getScSubProfitMonth();
           args[20] =StringUtils.isEmpty(req.getScSubSeqCalculation()) ? "" :req.getScSubSeqCalculation();
           args[21]=StringUtils.isBlank(req.getReferenceNo())?"":req.getReferenceNo();
           if("scale".equalsIgnoreCase(req.getPageFor())){
        	   args[22]="";
           }
           else{
        	   args[22] =req3.getScaleMaxPartPercent(); 
           }
           args[23] =req.getFpcType();
           args[24] =req.getFpcfixedDate();
		          
		TtrnBonus insert = proportionalityCustomRepository.bonusMainInsertPtty(args);
        if(insert!=null) {
     	   ttrnBonusRepository.saveAndFlush(insert);
     	   }
        if("MB".equals(req.getScalementhod())) {
			   break;
		   }
			}
		} if("scale".equalsIgnoreCase(req.getPageFor())){
			//  InsertSlidingScaleMentodInfo(req);
		  }
				}
	
	@Override
	public ScaleCommissionInsertRes ScaleCommissionInsert(ScaleCommissionInsertReq req) {
		ScaleCommissionInsertRes response = new ScaleCommissionInsertRes();		
		try {
			MoveBonus(req);
				response.setMessage("Success");
				response.setIsError(false);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public saveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(saveRiskDeatilsSecondFormReq req) {
		saveRiskDeatilsSecondFormRes response = new saveRiskDeatilsSecondFormRes();
		saveRiskDeatilsSecondFormRes1 res = new saveRiskDeatilsSecondFormRes1();
		String query = "";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Tuple> list2 = new ArrayList<>();
		try {
			String[] args=null;
			int out=0;
			int chkSecPageMode = checkSecondPageMode(req.getProposalno());
			int ContractEditMode = contractEditMode(req.getProposalno());
			if (ContractEditMode == 1) {
				if (chkSecPageMode == 1) {

					args = secondPageFirstTableAruguments(req, req.getProductId(),getMaxAmednId(req.getProposalno()));
					//risk.update.pro24RskProposal
					TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalSecondPageUpdate(args);
					if(update!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update);	
					}
					args = secondPageCommissionAruguments(req,req.getProductId());
					//risk.insert.pro2SecComm
					TtrnRiskCommission insert = proportionalityCustomRepository.ttrnRiskCommissionSecondPageInsert(args);
					if(insert!=null) {
						ttrnRiskCommissionRepository.saveAndFlush(insert);	
					}
					
					args = new String[3];
					args[0] = req.getProposalno();
					args[1] = req.getProposalno();
					args[2] = req.getProposalno();
					//risk.select.chechProposalStatus
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
					Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

					query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),rd.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO")); 

					Subquery<Long> endA = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
					endA.select(cb.max(rds.get("rskEndorsementNo")));
					Predicate a1 = cb.equal( rds.get("rskProposalNumber"), req.getProposalno());
					endA.where(a1);
					
					Subquery<Long> endB = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> bs = endB.from(TtrnRiskProposal.class);
					endB.select(cb.max(bs.get("rskEndorsementNo")));
					Predicate b1 = cb.equal( bs.get("rskProposalNumber"), req.getProposalno());
					endB.where(b1);

					Predicate n1 = cb.equal(rd.get("rskProposalNumber"), req.getProposalno());
					Predicate n3 = cb.equal(rd.get("rskProposalNumber"), b.get("rskProposalNumber"));
					Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
					Predicate n4 = cb.equal(b.get("rskEndorsementNo"), endB);
					query1.where(n1,n2,n3,n4);

					TypedQuery<Tuple> result = em.createQuery(query1);
					List<Tuple> list1 = result.getResultList();
					Tuple resMap = null;
					if(list1!=null && list1.size()>0)
						resMap = list1.get(0);
					if(resMap!=null){
						res.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
						res.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
						res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
						if (res.getProStatus().matches("A")	&& !res.getSharSign().matches("0")) {
							String maxContarctNo = null;
							
							//GET_BASE_LAYER_DETAILS
							list2 = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getProposalno());	
							Tuple resMap1 = null;
							if(list2!=null && list2.size()>0)
								resMap1 = list2.get(0);
							if (resMap1 != null) {
									res.setBaseLayerYN(resMap1.get("BASE_LAYER")==null?"":resMap1.get("BASE_LAYER").toString());
							}
							String prodid=req.getProductId();
							if ("layer".equalsIgnoreCase(req.getLay())) { //RI
								maxContarctNo = req.getContNo();
							}
							else if(StringUtils.isNotBlank(req.getBaseLayerYN())){
								//GET_BASE_LAYER_DETAILS
							
								list2 = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getBaseLayerYN());	

								resMap1 = null;
								if(list2!=null && list2.size()>0)
									resMap1 = list2.get(0);
								if (resMap1 != null) {
										res.setContNo(resMap1.get("CONTRACT_NO")==null?"":resMap1.get("CONTRACT_NO").toString());
								}
								maxContarctNo=req.getContNo();
							}
							
							else {
								if(!"".equals(req.getRenewalcontractno())&&!"0".equals(req.getRenewalcontractno())&&"OLDCONTNO".equals(req.getRenewalFlag())){
									maxContarctNo=req.getRenewalcontractno();
								}else{
									//if("06".equalsIgnoreCase(beanObj.getBranchCode())){
										maxContarctNo=fm.getSequence("Contract",prodid,req.getDepartmentId(), req.getBranchCode(),req.getProposalno(),req.getUwYear());
									/*}else
									maxContarctNo=new DropDownControllor().getPolicyNo("2",prodid,beanObj.getBranchCode());*/
									if("RI01".equalsIgnoreCase(req.getSourceId())){
										InsertSectionValueReq req1 = new InsertSectionValueReq();
										req1.setBranchCode(req.getBranchCode());
										req1.setDepartmentId(req.getDepartmentId());
										req1.setLoginId(req.getLoginId());									
										insertSectionValue(req1,maxContarctNo);
									}
								}
							}
							
							//risk.update.contNo
							List<TtrnRiskDetails> update1 = ttrnRiskDetailsRepository.findByRskProposalNumber(req.getProposalno());	
							if(update1.size()>0) {
								TtrnRiskDetails u =update1.get(0);		
								u.setRskContractNo(maxContarctNo);				
								ttrnRiskDetailsRepository.saveAndFlush(u);
							}							
							//risk.update.homeContNo
							PositionMaster update2 = positionMasterRepository.findByProposalNo(new BigDecimal(req.getProposalno()));
							if(update2!=null) {
								update2.setContractNo(new BigDecimal(maxContarctNo));
								update2.setProposalStatus("A");
								update2.setContractStatus("A");	
								positionMasterRepository.saveAndFlush(update2)	;
								}
							
							res.setContNo((String)args[0]);
												
							res.setContNo(maxContarctNo);
							if("".equals(req.getRenewalcontractno())||"0".equals(req.getRenewalcontractno())||"NEWCONTNO".equals(req.getRenewalFlag())){
								res.setContractGendration("Your Proposal is converted to Contract with Proposal No : "+req.getProposalno() +" and Contract No : "+maxContarctNo+".");
							}else{
								res.setContractGendration("Your Proposal is Renewaled with Proposal No : "+req.getProposalno() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
							}
						} else {
							args = new String[4];
							args[0] = req.getContNo();
							args[1] = getproposalStatus(req.getProposalno());
							args[2] = args[1];
							if (args != null) {

								if (((String) args[1]).equalsIgnoreCase("P")) {
									res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ req.getProposalno());
								}	if (((String) args[1]).equalsIgnoreCase("N")) {
									res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalno());
								}  else if (((String) args[1]).equalsIgnoreCase("A")) {
									res.setContractGendration("Your Contract is updated with Proposal No : "+req.getProposalno()+" and Contract No : "+req.getContNo()+".");
								} else if (((String) args[1]).equalsIgnoreCase("R")) {
									res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalno());
								}
							}
							args[3] = req.getProposalno();
							query = "risk.update.homeContNo";
							int k=0;
							for(Object str:args)
								out = queryImpl.updateQuery(query,args);
						}
					}
				}

				else if (chkSecPageMode == 2) {
					args =updateRiskDetailsSecondForm(req, req.getProductId(),getMaxAmednId(req.getProposalno()));
					//risk.update.pro24RskProposal
					TtrnRiskProposal update = proportionalityCustomRepository.ttrnRiskProposalSecondPageUpdate(args);
					if(update!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update);	
					}
					args = updateRiskDetailsSecondFormSecondTable(req, req.getProductId(),getMaxAmednId(req.getProposalno()));
					//risk.update.pro2SecComm
					TtrnRiskCommission update1 = proportionalityCustomRepository.ttrnRiskCommissionSecondPageUpdate(args);
					if(update1!=null) {
						ttrnRiskCommissionRepository.saveAndFlush(update1);	
					}
					
					args = new String[3];
					args[0] = req.getProposalno();
					args[1] = req.getProposalno();
					args[2] = req.getProposalno();
					//risk.select.chechProposalStatus
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
					Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

					query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),rd.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO")); 

					Subquery<Long> endA = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
					endA.select(cb.max(rds.get("rskEndorsementNo")));
					Predicate a1 = cb.equal( rds.get("rskProposalNumber"), req.getProposalno());
					endA.where(a1);
					
					Subquery<Long> endB = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> bs = endB.from(TtrnRiskProposal.class);
					endB.select(cb.max(bs.get("rskEndorsementNo")));
					Predicate b1 = cb.equal( bs.get("rskProposalNumber"), req.getProposalno());
					endB.where(b1);

					Predicate n1 = cb.equal(rd.get("rskProposalNumber"), req.getProposalno());
					Predicate n3 = cb.equal(rd.get("rskProposalNumber"), b.get("rskProposalNumber"));
					Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
					Predicate n4 = cb.equal(b.get("rskEndorsementNo"), endB);
					query1.where(n1,n2,n3,n4);

					TypedQuery<Tuple> result = em.createQuery(query1);
					List<Tuple> list1 = result.getResultList();
					Tuple resMap = null;
					if(list1!=null && list1.size()>0)
						resMap = list1.get(0);
					if (resMap != null) {
						for (int i = 0; i < list1.size(); i++) {
							res.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
							res.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
							res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
						}
					}

					if (res.getProStatus().matches("A")	&& !res.getSharSign().matches("0")) {
						String prodid=req.getProductId();
						String maxContarctNo=""; 
						//GET_BASE_LAYER_DETAILS
						list2 = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getProposalno());	
						Tuple resMap1 = null;
						if(list2!=null && list2.size()>0)
							resMap1 = list2.get(0);
						if (resMap1 != null) {
								res.setBaseLayerYN(resMap1.get("BASE_LAYER")==null?"":resMap1.get("BASE_LAYER").toString());
						}
						if(StringUtils.isNotBlank(req.getBaseLayerYN())){
							//GET_BASE_LAYER_DETAILS
							list2 = proportionalityCustomRepository.getBaseLayerDetails(req.getProductId(),req.getBranchCode(),req.getBaseLayerYN());
							resMap1 = null;
							if(list2!=null && list2.size()>0)
								resMap1 =  list2.get(0);
							if (resMap1 != null) {
									res.setContNo(resMap1.get("CONTRACT_NO")==null?"":resMap1.get("CONTRACT_NO").toString());
							}
							maxContarctNo=req.getContNo();
							if("RI01".equalsIgnoreCase(req.getSourceId())){
								InsertSectionValueReq req1 = new InsertSectionValueReq();
								req1.setBranchCode(req.getBranchCode());
								req1.setDepartmentId(req.getDepartmentId());
								req1.setLoginId(req.getLoginId());					
								insertSectionValue(req1,maxContarctNo); 
							}
						}else{
						if(!"".equals(req.getRenewalcontractno())&&!"0".equals(req.getRenewalcontractno())&&"OLDCONTNO".equals(req.getRenewalFlag())){
							maxContarctNo=req.getRenewalcontractno(); 
							
						}else{
							//if("06".equalsIgnoreCase(beanObj.getBranchCode())){
								maxContarctNo=fm.getSequence("Contract",prodid,req.getDepartmentId(), req.getBranchCode(),req.getProposalno(),req.getUwYear());
							/*}else
							maxContarctNo=new DropDownControllor().getPolicyNo("2",prodid,beanObj.getBranchCode());*/
						}
						if("RI01".equalsIgnoreCase(req.getSourceId())){
							InsertSectionValueReq req1 = new InsertSectionValueReq();
							req1.setBranchCode(req.getBranchCode());
							req1.setDepartmentId(req.getDepartmentId());
							req1.setLoginId(req.getLoginId());					
							insertSectionValue(req1,maxContarctNo);
						}
						}
						//if(StringUtils.isNotBlank(maxContarctNo) && !"0".equalsIgnoreCase(maxContarctNo)){
						//risk.update.contNo
						List<TtrnRiskDetails> update2 = ttrnRiskDetailsRepository.findByRskProposalNumber(req.getProposalno());	
						if(update2.size()>0) {
							TtrnRiskDetails u =update2.get(0);		
							u.setRskContractNo(maxContarctNo);				
							ttrnRiskDetailsRepository.saveAndFlush(u);
						}		
						args = new String[4];
						args[0] = maxContarctNo;
						args[1] = getMaxproposalStatus(req.getProposalno());
						args[2] = args[1];
						args[3] = req.getProposalno();
						query = "risk.update.homeContNo";
						out = queryImpl.updateQuery(query,args);
						if(StringUtils.isNotBlank(req.getBaseLayer())){
							res.setContractGendration("A new proposal "+req.getProposalno()+" has been created under contract number "+req.getContNo()+".");
						}
						else if("".equals(req.getRenewalcontractno())||"0".equals(req.getRenewalcontractno())||"NEWCONTNO".equals(req.getRenewalFlag())){
							res.setContractGendration("Your Contract is updated with Proposal No : "+req.getProposalno()+" and Contract No : "+maxContarctNo+".");
						}else{
							res.setContractGendration("Your Proposal is Renewaled with Proposal No : "+req.getProposalno() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
						}
						query = "risk.update.mndInstallments";
						args = new String [2];
						args[0] = maxContarctNo;
						args[1] = req.getProposalno();
						out = queryImpl.updateQuery(query, args);				
						res.setContNo(maxContarctNo);
					//}else{
						//beanObj.setContractGendration("Your  Proposal No : "+ beanObj.getProposal_no()+"not converting into contract because base proposal of this proposal "+beanObj.getBaseLayerYN()+"is not converted as contract");
					//}
					} else if (req.getProStatus().matches("P")) {
						res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ req.getProposalno());
					}else if (req.getProStatus().matches("N")) {
						if (req.getProductId().equalsIgnoreCase("2")||req.getProductId().equalsIgnoreCase("4")) {
							res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalno());
						}
					}else if (req.getProStatus().matches("R")) {
						res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalno()+" and Layer No.:"+req.getLayerNo()+".");
					}

				}
			}else if (ContractEditMode == 2) {
				query = "risk.select.endo";
				String endtNo = null;
				args = new String [1];
				args[0] = req.getProposalno();
				list = queryImpl.selectList(query, args);
				if(!CollectionUtils.isEmpty(list)) {
					endtNo=list.get(0).get("RSK_ENDORSEMENT_NO")==null?"":list.get(0).get("RSK_ENDORSEMENT_NO").toString();
				}
				
				args = updateContractRiskDetailsSecondForm(req, req.getProductId(), endtNo);
				query = "risk.update.pro24ContSecPage";
				out = queryImpl.updateQuery(query, args);
					res.setContractGendration("Your Contract is updated with Proposal No : "+req.getProposalno()+", Contract No : "+req.getContNo()+".");
				/*insertQry = getQuery(DBConstants.RISK_INSERT_PRO2SECCOMM);
				logger.info("InsertQry=>" + insertQry);
				obj1 = secondContarctPageCommissionAruguments(beanObj,productId);
				out=this.mytemplate.update(insertQry, obj1);*/

				args = updateRiskDetailsSecondFormSecondTable(req, req.getProductId(), getMaxAmednId(req.getProposalno()));
				//risk.update.pro2SecComm
				TtrnRiskCommission update1 = proportionalityCustomRepository.ttrnRiskCommissionSecondPageUpdate(args);
				if(update1!=null) {
					ttrnRiskCommissionRepository.saveAndFlush(update1);	
				}
				res.setProStatus("A");
			}
//			RetroSaveReq sub1 = new RetroSaveReq();
//			CrestaSaveReq sub2 = new CrestaSaveReq();
//			BonusSaveReq sub3 = new BonusSaveReq();
//			RemarksSaveReq sub4 = new RemarksSaveReq();
//			
//			insertRetroContracts(sub1);
//			insertCrestaMaintable(sub2);
//			res.setProductId(req.getProductId());
//			insertBonusDetails(sub3,"scale");
//			insertBonusDetails(sub3,"lossparticipate");
//			insertProfitCommissionMain(req,"main");
//			insertRemarkDetails(sub4);
//			updateRetentionContractNo(req);
//			dropDowmImpl.updatepositionMasterEndtStatus(req.getProposalno(),req.getEndorsementDate(),req.getCeaseStatus());
//			if(StringUtils.isNotBlank(req.getContNo())){
//			dropDowmImpl.getSOATableInsert(req.getProposalno(), req.getContNo(),req.getBranchCode());
//			}
			response.setSaveRiskDeatilsSecondFormRes1(res);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	
	public int contractEditMode(String proposalNo) {
		int mode=0;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String query;
		String result = null;
		try {
			query = "risk.select.getRskContractNo";
			String [] args = new String [1];
			args [0] = proposalNo;
			list = queryImpl.selectList(query, args);
			if(!CollectionUtils.isEmpty(list)) {
				result=list.get(0).get("RSK_CONTRACT_NO")==null?"":list.get(0).get("RSK_CONTRACT_NO").toString();
			}
			if ("0".equalsIgnoreCase(result)) {
				mode = 1;
			} else {
				mode = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mode;
	}
	
	public String[] secondPageFirstTableAruguments(saveRiskDeatilsSecondFormReq req,String productId,String endNo) {
		String[] args=null;
		args = new String[18];
		args[0] =  StringUtils.isBlank(req.getLimitOurShare())?"":req.getLimitOurShare().replaceAll(",", "");
		args[1] = getDesginationCountry(req.getLimitOurShare(), req.getExchRate());
		args[2] = StringUtils.isBlank(req.getEpiAsPerOffer())?"0":req.getEpiAsPerOffer().replaceAll(",", "");
		args[3] = getDesginationCountry(req.getEpiAsPerOffer(), req.getExchRate());
		args[4] =StringUtils.isBlank(req.getEpiAsPerShare())?"0": req.getEpiAsPerShare();
		args[5] = getDesginationCountry(req.getEpiAsPerShare(), req.getExchRate());
		args[6] = "";
		args[7] = "";
		args[8] = StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare();
		args[9] = StringUtils.isBlank(req.getPremiumSurplus())?"0":req.getPremiumSurplus();
		args[10]= StringUtils.isEmpty(req.getPremiumQuotaShare())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumQuotaShare(), req.getExchRate());
		args[11]= StringUtils.isEmpty(req.getPremiumSurplus())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumSurplus(), req.getExchRate());
		args[12]=StringUtils.isBlank(req.getCommissionQSAmt())?"0":req.getCommissionQSAmt().replaceAll(",", "");
		args[13]=StringUtils.isBlank(req.getCommissionsurpAmt())?"0":req.getCommissionsurpAmt().replaceAll(",", "");
		args[14]= StringUtils.isEmpty(req.getCommissionQSAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionQSAmt(), req.getExchRate());
		args[15]= StringUtils.isEmpty(req.getCommissionsurpAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionsurpAmt(), req.getExchRate());
		args[16] = req.getProposalno();
		args[17] = endNo;
		return args;
	}
	
	public String[] secondPageCommissionAruguments(saveRiskDeatilsSecondFormReq req, String productId) {
		String[] args=null;
		args = new String[71]; //Ri
		args[0] = req.getProposalno();
		args[1] = "0";
		args[2] = req.getLayerNo();
		args[3] = StringUtils.isEmpty(req.getBrokerage()) ? "0": req.getBrokerage();
		args[4] = req.getTax();
		args[5] = req.getShareProfitCommission().replaceAll(",", "");
		args[6] = "0";
		args[7] = req.getAcquisitionCost().replaceAll(",", "");
		args[8] = getDesginationCountry(req.getAcquisitionCost().replaceAll(",",""),req.getExchRate());
		args[9] = StringUtils.isBlank(req.getCommissionQS())?"0":req.getCommissionQS().replaceAll(",", "");
		args[10] = StringUtils.isBlank(req.getCommissionsurp())?"0":req.getCommissionsurp().replaceAll(",", "");
		args[11] = req.getOverRidder().replaceAll(",", "");
		args[12] = req.getPremiumReserve();
		args[13] = req.getLossreserve();
		args[14] = req.getInterest();
		args[15] = StringUtils.isEmpty(req.getPortfolioinoutPremium()) ? "0": req.getPortfolioinoutPremium();
		args[16] = StringUtils.isEmpty(req.getPortfolioinoutLoss()) ? "0": req.getPortfolioinoutLoss();
		args[17] = req.getLossAdvise().replaceAll(",", "");
		args[18] = req.getCashLossLimit().replaceAll(",", "");
		args[19] = getDesginationCountry(req.getCashLossLimit(),req.getExchRate());
		args[20] = req.getLeaderUnderwriter();
		args[21] = req.getLeaderUnderwritershare();
		args[22] = req.getAccounts();
		args[23] = req.getExclusion();
		args[24] =StringUtils.isEmpty(req.getRemarks())?"":req.getRemarks();
		args[25] = req.getUnderwriterRecommendations();
		args[26] = req.getGmsApproval();
		args[27] = "";
		args[28] = "";
		args[29] = StringUtils.isEmpty(req.getOthercost()) ? "0"	: req.getOthercost().replaceAll(",", "");
		args[30] = req.getCrestaStatus();
		args[31] = req.getEventlimit();
		args[32] =getDesginationCountry(req.getEventlimit(),req.getExchRate());
		args[33] = req.getAggregateLimit();
		args[34] =getDesginationCountry(req.getAggregateLimit(),req.getExchRate());
		args[35] = req.getOccurrentLimit();
		args[36] =getDesginationCountry(req.getOccurrentLimit(),req.getExchRate());
		args[37] = req.getSlideScaleCommission();
		args[38] = req.getLossParticipants();
		args[39] =StringUtils.isEmpty(req.getCommissionSubClass()) ? "": req.getCommissionSubClass();
		args[40]=req.getDepartmentId();
		args[41] = req.getLoginId();
		args[42] = req.getBranchCode();
		args[43] = req.getLeaderUnderwritercountry();
		args[44] =StringUtils.isEmpty(req.getOrginalacqcost())?"":req.getOrginalacqcost();
		args[45] = StringUtils.isEmpty(req.getOurassessmentorginalacqcost())?"":req.getOurassessmentorginalacqcost();
		args[46] = StringUtils.isEmpty(req.getOuracqCost())?"":req.getOuracqCost();
		
		args[47] = StringUtils.isEmpty(req.getLosscommissionSubClass())?"":req.getLosscommissionSubClass();
		args[48] = StringUtils.isEmpty(req.getSlidecommissionSubClass())?"":req.getSlidecommissionSubClass();
		args[49] = StringUtils.isEmpty(req.getCrestacommissionSubClass())?"":req.getCrestacommissionSubClass();
		if("1".equalsIgnoreCase(req.getShareProfitCommission())){
		args[50] = StringUtils.isEmpty(req.getManagementExpenses())?"":req.getManagementExpenses();
		args[51] = StringUtils.isEmpty(req.getCommissionType())?"":req.getCommissionType();
		args[52] = StringUtils.isEmpty(req.getProfitCommissionPer())?"":req.getProfitCommissionPer();
		args[53] = StringUtils.isEmpty(req.getSetup())?"":req.getSetup();
		args[54] = StringUtils.isEmpty(req.getSuperProfitCommission())?"":req.getSuperProfitCommission();
		args[55] = StringUtils.isEmpty(req.getLossCarried())?"":req.getLossCarried();
		args[56] = StringUtils.isEmpty(req.getLossyear())?"":req.getLossyear();
		args[57] = StringUtils.isEmpty(req.getProfitCarried())?"":req.getProfitCarried();
		args[58] = StringUtils.isEmpty(req.getProfitCarriedForYear())?"":req.getProfitCarriedForYear();
		args[59] = StringUtils.isEmpty(req.getFistpc())?"":req.getFistpc();
		args[60] = StringUtils.isEmpty(req.getProfitMont())?"":req.getProfitMont();
		args[61] = StringUtils.isEmpty(req.getSubProfitMonth())?"":req.getSubProfitMonth();
		args[62] = StringUtils.isEmpty(req.getSubpc())?"":req.getSubpc();
		args[63] = StringUtils.isEmpty(req.getSubSeqCalculation())?"":req.getSubSeqCalculation();
		args[64] = StringUtils.isEmpty(req.getProfitCommission())?"":req.getProfitCommission();
		}
		else{
			args[50] = "";
			args[51] = "";
			args[52] = "";
			args[53] = "";
			args[54] = "";
			args[55] = "";
			args[56] = "";
			args[57] = "";
			args[58] = "";
			args[59] = "";
			args[60] = "";
			args[61] = "";
			args[62] = "";
			args[63] = "";
			args[64] = "";
		}
		args[65] =StringUtils.isEmpty(req.getDocStatus())?"":req.getDocStatus();
		args[66] =StringUtils.isEmpty(req.getLocRate())? "" :req.getLocRate();
		args[67] =StringUtils.isEmpty(req.getPremiumResType())? "" :req.getPremiumResType();
		args[68] = StringUtils.isEmpty(req.getPcfpcType())?"":req.getPcfpcType();
		args[69] = StringUtils.isEmpty(req.getPcfixedDate())?"":req.getPcfixedDate();
		args[70] = StringUtils.isEmpty(req.getPortfolioType())?"":req.getPortfolioType();
		return args;
	}
	
	private void insertSectionValue(InsertSectionValueReq req, String maxContarctNo) {
		try{
			String query = "";
			query = "INSERT_SECTION_DETAILS";
			String args[]= new String[6];
			args[0] ="1";
			args[1] = maxContarctNo;
			args[2] = req.getDepartmentId();
			args[3] = "Main Section";
			args[4] = req.getBranchCode();
			args[5] = req.getLoginId();
			queryImpl.updateQuery(query, args);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private String getproposalStatus(String proposalNo) {
		String result="";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			String query;
			String [] args = null;
			query = "risk.select.getRskStatus";
				args = new String [1];
				args [1] = proposalNo;
				list = queryImpl.selectList(query, args);
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0).get("RSK_PROPOSAL_NUMBER")==null?"":list.get(0).get("RSK_PROPOSAL_NUMBER").toString();
				}
		}catch(Exception e){
		}
		return result;
	}
	
	
	public String[] updateRiskDetailsSecondForm(saveRiskDeatilsSecondFormReq req, String productId, String endNo) {
		String[] args=null;
		args = new String[18];
		args[0] = req.getLimitOurShare();
		args[1] = getDesginationCountry(req.getLimitOurShare(), req.getExchRate());
		args[2] = req.getEpiAsPerOffer();
		args[3] = getDesginationCountry(req.getEpiAsPerOffer(), req.getExchRate());
		args[4] =StringUtils.isBlank(req.getEpiAsPerShare())?"0": req.getEpiAsPerShare();
		args[5] = getDesginationCountry(req.getEpiAsPerShare(), req.getExchRate());
		args[6] = "";
		args[7] = "";
		args[8] = StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare();
		args[9] = StringUtils.isBlank(req.getPremiumSurplus())?"0":req.getPremiumSurplus();
		args[10]= StringUtils.isEmpty(req.getPremiumQuotaShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumQuotaShare(), req.getExchRate());
		args[11]= StringUtils.isEmpty(req.getPremiumSurplus())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumSurplus(), req.getExchRate());
		args[12]=StringUtils.isBlank(req.getCommissionQSAmt())?"0":req.getCommissionQSAmt();
		args[13]=StringUtils.isBlank(req.getCommissionsurpAmt())?"0":req.getCommissionsurpAmt();
		args[14]= StringUtils.isEmpty(req.getCommissionQSAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionQSAmt(), req.getExchRate());
		args[15]= StringUtils.isEmpty(req.getCommissionsurpAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionsurpAmt(), req.getExchRate());
		args[16] = req.getProposalno();
		args[17] = endNo;
		return args;
	}
	
	public String[] updateRiskDetailsSecondFormSecondTable(saveRiskDeatilsSecondFormReq req,String productId,String endNo) {
		String[] args=null;
		if (productId.equalsIgnoreCase("2")) {
			args = new String[67];
			args[0] = StringUtils.isEmpty(req.getBrokerage()) ? "0": req.getBrokerage();
			args[1] = req.getTax();
			args[2] = req.getShareProfitCommission();
			args[3] = req.getAcquisitionCost();
			args[4] = getDesginationCountry(req.getAcquisitionCost().replaceAll(",",""),req.getExchRate());
			args[5] =StringUtils.isEmpty(req.getCommissionQS()) ? "0": req.getCommissionQS();
			args[6] = StringUtils.isEmpty(req.getCommissionsurp()) ? "0":req.getCommissionsurp();
			args[7] = StringUtils.isEmpty(req.getOverRidder())?"0":req.getOverRidder();
			//args[8] = StringUtils.isEmpty(req.getManagement_Expenses()) ? "0": req.getManagement_Expenses();
			//args[9] = StringUtils.isEmpty(req.getLossC_F()) ? "0" : req.getLossC_F();
			args[8] = req.getPremiumReserve();
			args[9] = req.getLossreserve();
			args[10] = req.getInterest();
			args[11] = StringUtils.isEmpty(req.getPortfolioinoutPremium()) ? "0": req.getPortfolioinoutPremium();
			args[12] = StringUtils.isEmpty(req.getPortfolioinoutLoss()) ? "0": req.getPortfolioinoutLoss();
			args[13] = req.getLossAdvise();
			args[14] = req.getCashLossLimit();
			args[15] = getDesginationCountry(req.getCashLossLimit(),req.getExchRate());
			args[16] = req.getLeaderUnderwriter();
			args[17] = req.getLeaderUnderwritershare();
			args[18] = req.getAccounts();
			args[19] = req.getExclusion();
			args[20] = StringUtils.isEmpty(req.getRemarks())?"":req.getRemarks();
			args[21] = req.getUnderwriterRecommendations();
			args[22] = req.getGmsApproval();
			//args[25] = StringUtils.isEmpty(req.getProfit_commission()) ? "0"	: req.getProfit_commission();
			args[23] = StringUtils.isEmpty(req.getOthercost()) ? "0"	: req.getOthercost();
			args[24] =req.getCrestaStatus();
			args[25] = req.getEventlimit();
			args[26] =getDesginationCountry(req.getEventlimit(),req.getExchRate());
			args[27] = req.getAggregateLimit();
			args[28] =getDesginationCountry(req.getAggregateLimit(),req.getExchRate());
			args[29] = req.getOccurrentLimit();
			args[30] =getDesginationCountry(req.getOccurrentLimit(),req.getExchRate());
			args[31] = req.getSlideScaleCommission();
			args[32] = req.getLossParticipants();
			args[33] = StringUtils.isEmpty(req.getCommissionSubClass()) ? "": req.getCommissionSubClass();
			args[34] =  req.getDepartmentId();
			args[35] = req.getLoginId();
			args[36] = req.getBranchCode();
			args[37] = StringUtils.isEmpty(req.getLeaderUnderwritercountry())?"":req.getLeaderUnderwritercountry();
			args[38] =StringUtils.isEmpty(req.getOrginalacqcost())?"":req.getOrginalacqcost();
			args[39] = StringUtils.isEmpty(req.getOurassessmentorginalacqcost())?"":req.getOurassessmentorginalacqcost();
			args[40] = StringUtils.isEmpty(req.getOuracqCost())?"":req.getOuracqCost();
			args[41] = StringUtils.isEmpty(req.getLosscommissionSubClass())?"":req.getLosscommissionSubClass();
			args[42] = StringUtils.isEmpty(req.getSlidecommissionSubClass())?"":req.getSlidecommissionSubClass();
			args[43] = StringUtils.isEmpty(req.getCrestacommissionSubClass())?"":req.getCrestacommissionSubClass();
			if("1".equalsIgnoreCase(req.getShareProfitCommission())){
			args[44] = StringUtils.isEmpty(req.getManagementExpenses())?"":req.getManagementExpenses();
			args[45] = StringUtils.isEmpty(req.getCommissionType())?"":req.getCommissionType();
			args[46] = StringUtils.isEmpty(req.getProfitCommissionPer())?"":req.getProfitCommissionPer();
			args[47] = StringUtils.isEmpty(req.getSetup())?"":req.getSetup();
			args[48] = StringUtils.isEmpty(req.getSuperProfitCommission())?"":req.getSuperProfitCommission();
			args[49] = StringUtils.isEmpty(req.getLossCarried())?"":req.getLossCarried();
			args[50] = StringUtils.isEmpty(req.getLossyear())?"":req.getLossyear();
			args[51] = StringUtils.isEmpty(req.getProfitCarried())?"":req.getProfitCarried();
			args[52] = StringUtils.isEmpty(req.getProfitCarriedForYear())?"":req.getProfitCarriedForYear();
			args[53] = StringUtils.isEmpty(req.getFistpc())?"":req.getFistpc();
			args[54] = StringUtils.isEmpty(req.getProfitMont())?"":req.getProfitMont();
			args[55] = StringUtils.isEmpty(req.getSubProfitMonth())?"":req.getSubProfitMonth();
			args[56] = StringUtils.isEmpty(req.getSubpc())?"":req.getSubpc();
			args[57] = StringUtils.isEmpty(req.getSubSeqCalculation())?"":req.getSubSeqCalculation();
			args[58] = StringUtils.isEmpty(req.getProfitCommission())?"":req.getProfitCommission();
			}
			else{
				args[44] = "";
				args[45] =  "";
				args[46] =  "";
				args[47] =  "";
				args[48] =  "";
				args[49] =  "";
				args[50] =  "";
				args[51] =  "";
				args[52] =  "";
				args[53] =  "";
				args[54] =  "";
				args[55] =  "";
				args[56] =  "";
				args[57] =  "";
				args[58] =  "";
			}
			args[59] = StringUtils.isEmpty(req.getDocStatus())? "" :req.getDocStatus();
			args[60] = StringUtils.isEmpty(req.getLocRate())? "" :req.getLocRate();
			args[61] =StringUtils.isEmpty(req.getPremiumResType())? "" :req.getPremiumResType();
			args[62] = StringUtils.isEmpty(req.getPcfpcType())?"":req.getPcfpcType();
			args[63] = StringUtils.isEmpty(req.getPcfixedDate())?"":req.getPcfixedDate();
			args[64] = StringUtils.isEmpty(req.getPortfolioType())?"":req.getPortfolioType();
			args[65] = req.getProposalno();
			args[66] = endNo;
		} 
		return args;
	}
	
	private String getMaxproposalStatus(String proposalNo) {
		String result="";
		try{
			 //risk.select.maxRskStatus
			 CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);

				query.multiselect(rd.get("rskStatus").alias("RSK_STATUS")); 

				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRiskDetails> rds = end.from(TtrnRiskDetails.class);
				end.select(cb.max(rds.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
				end.where(a1);

				Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
				query.where(n1,n2);

				TypedQuery<String> res = em.createQuery(query);
				List<String> list = res.getResultList();
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0)==null?"":list.get(0);
				}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}
	
	
	public String[] updateContractRiskDetailsSecondForm(saveRiskDeatilsSecondFormReq req, String productId, String endNo) {
		String[] args=null;
		args = new String[18];
		args[0] = req.getLimitOurShare();
		args[1] = getDesginationCountry(req.getLimitOurShare(), req.getExchRate());
		args[2] = req.getEpiAsPerOffer();
		args[3] = getDesginationCountry(req.getEpiAsPerOffer(), req.getExchRate());
		args[4] =StringUtils.isBlank(req.getEpiAsPerShare())?"0": req.getEpiAsPerShare();
		args[5] = getDesginationCountry(req.getEpiAsPerShare(), req.getExchRate());
		args[6] = "";
		args[7] = "";
		args[8] = StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare();
		args[9] = StringUtils.isBlank(req.getPremiumSurplus())?"0":req.getPremiumSurplus();
		args[10]= StringUtils.isEmpty(req.getPremiumQuotaShare())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumQuotaShare(), req.getExchRate());
		args[11]= StringUtils.isEmpty(req.getPremiumSurplus())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumSurplus(), req.getExchRate());
		args[12]=StringUtils.isBlank(req.getCommissionQSAmt())?"0":req.getCommissionQSAmt();
		args[13]=StringUtils.isBlank(req.getCommissionsurpAmt())?"0":req.getCommissionsurpAmt();
		args[14]= StringUtils.isEmpty(req.getCommissionQSAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionQSAmt(), req.getExchRate());
		args[15]= StringUtils.isEmpty(req.getCommissionsurpAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionsurpAmt(), req.getExchRate());
		args[16]=endNo;
		args[17] = req.getProposalno();
		return args;
	}
	
	private void insertProfitCommissionMain(saveRiskDeatilsSecondFormReq req,String type) {
		ProfitCommissionSaveReq bean = new ProfitCommissionSaveReq();
		if(!"1".equalsIgnoreCase(req.getShareProfitCommission())){
				mainDelete(bean);
				profitMainEmptyInsert(bean);
			}
		profitUpdate(bean);
	}
	
	public void updateRetentionContractNo(String contNo, String productId, String proposalNo, String departmentId){
		try{
			String query="GET_COUNT_RETENTION";
			String args[] = new String[2];
			args[0] = proposalNo;
			args[1] = productId;
			int count = queryImpl.updateQuery(query,args);
			if(count>=1){
				query= "UPDATE_RETEN_CONTNO";
				 	args = new String[4];
				 	args[0] = contNo;
				 	args[1] = departmentId;
					args[2] = proposalNo;
					args[3] = productId;
					queryImpl.updateQuery(query,args);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public checkAvialabilityRes checkAvialability(String proposalno,String pid) {
		checkAvialabilityRes response = new checkAvialabilityRes();
		boolean saveFlag = false;
		String result = "";
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			String query = "risk.select.getRskProIdByProNo";
			list = queryImpl.selectList(query, new String [] {proposalno});
			if(!CollectionUtils.isEmpty(list)) {
				 result = list.get(0).get("RSK_PRODUCTID")==null?"":list.get(0).get("RSK_PRODUCTID").toString();
				}
				if (result.equals(pid)) {
					saveFlag = true;
				} else {
					saveFlag = false;
				}
			response.setResponse(String.valueOf(saveFlag));
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public CommonSaveRes updateOfferNo(UpdateOfferNoReq req) { //RI
		CommonSaveRes response = new CommonSaveRes();
		try {
			String offerNo="";
			
			if(StringUtils.isBlank(req.getOfferNo())) {
				offerNo= fm.getSequence("Offer",req.getProductId(),"0",req.getBranchCode(),"","");
				response.setResponse("OfferNo: "+offerNo);			
				String query="UPDATE POSITION_MASTER SET OFFER_NO=? WHERE PROPOSAL_NO=?";
				queryImpl.updateQuery(query, new String[] {req.getOfferNo(),req.getProposalNo()});
			}
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetSlidingScaleMethodInfoRes getSlidingScaleMethodInfo(String proposalNo, String branchCode,
			String referenceNo) {
		GetSlidingScaleMethodInfoRes response = new GetSlidingScaleMethodInfoRes();
		List<GetSlidingScaleMethodInfoRes1> resList = new ArrayList<GetSlidingScaleMethodInfoRes1>();
		String args[]=null;
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		String query="";
	try {
		args = new String[4];
		args[0] = proposalNo;
		args[1] = branchCode;
		args[2] ="SSC";
		args[3] ="SSC1";
		
			query ="SELECT_SLIDING_SCALE_METHOD_INFO";
			result= queryImpl.selectList(query,args);
			if(CollectionUtils.isEmpty(result)) {
				args[0] = referenceNo;
				query ="SELECT_SLIDING_SCALE_METHOD_INFO_REF";
				result= queryImpl.selectList(query,args);
			}
			//}
		for(int i=0;i<result.size();i++){
		       Map<String,Object> tempMap = result.get(i);
		       GetSlidingScaleMethodInfoRes1 bean = new GetSlidingScaleMethodInfoRes1();
		       bean.setProvisionCom(tempMap.get("PROVISIONAL_COMMISIION")==null?"":tempMap.get("PROVISIONAL_COMMISIION").toString());
               bean.setScalementhod(tempMap.get("SC_METHOD_TYPE")==null?"":tempMap.get("SC_METHOD_TYPE").toString());
               bean.setScaleminRatio(tempMap.get("SC_MIN_LOSS_RATIO")==null?"":tempMap.get("SC_MIN_LOSS_RATIO").toString());
               bean.setScalemaxRatio(tempMap.get("SC_MAX_LOSS_RATIO")==null?"":tempMap.get("SC_MAX_LOSS_RATIO").toString());
               bean.setScalecombine(tempMap.get("SC_COMBINE_LOSS_RATIO")==null?"":tempMap.get("SC_COMBINE_LOSS_RATIO").toString());
               bean.setScalebanding(tempMap.get("SC_BANDING_STEP")==null?"":tempMap.get("SC_BANDING_STEP").toString());
               bean.setScaledigit(tempMap.get("SC_NO_OF_DIGIT")==null?"":tempMap.get("SC_NO_OF_DIGIT").toString());
               bean.setScalelossratioFrom(tempMap.get("LCB_FROM")==null?"":tempMap.get("LCB_FROM").toString());
               bean.setScalelossratioTo(tempMap.get("LCB_TO")==null?"":tempMap.get("LCB_TO").toString());
               bean.setScaledeltalossratio(tempMap.get("DELTA_LOSS_RATIO")==null?"":tempMap.get("DELTA_LOSS_RATIO").toString());
               bean.setScaledeltacommission(tempMap.get("LCB_PERCENTAGE")==null?"":tempMap.get("LCB_PERCENTAGE").toString());
               resList.add(bean);
               }
		response.setCommonResponse(resList);
		response.setMessage("Success");
		response.setIsError(false);
	} catch (Exception e) {
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}
	@Override
	public CommonResponse insertSlidingScaleMentodInfo(InsertSlidingScaleMentodInfoReq bean) {
		CommonResponse response = new CommonResponse();
		try {
			 String query ="INSERT_SC_METHOD_INFO";
			 String args[]=new String[23];
			   args[0] =bean.getProposalNo();
	           args[1] = bean.getContractNo();
	           args[2] = bean.getProductId();
	           args[3] = "SSC1";  
	           args[4] = bean.getProvisionCom();
	           args[5] = bean.getScalementhod();
	           args[6] = bean.getScaleminRatio();
	           args[7] = bean.getScalemaxRatio();
	           args[8] = bean.getScalecombine();
	           args[9] = bean.getScalebanding();
	           args[10] = bean.getScaledigit();
	           args[11] = bean.getLoginId();
	           args[12] = bean.getBranchCode();
	           args[13] ="SSC";
	           args[14] = bean.getAmendId();
	           args[15] = bean.getDepartmentId();
	           args[16] = StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
	           args[17]="";
	           args[18]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();
	           if("MB".equals(bean.getScalementhod())) {
	        	   args[19]=bean.getScalelossratioFrom();
	        	   args[20]=bean.getScalelossratioTo();
	        	   args[21]=bean.getScaledeltalossratio();
	        	   args[22]=bean.getScaledeltacommission();
	        	   
	           }else {
	        	   args[19]="";
	        	   args[20]="";
	        	   args[21]="";
	        	   args[22]="";
	           }
	           queryImpl.updateQuery(query, args);
	           response.setMessage("Success");
	   		response.setIsError(false);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		response.setMessage("Failed");
	   		response.setIsError(true);
	   	}
	   	return response;
	}
	@Override
	public GetcalculateSCRes getcalculateSC(GetcalculateSCReq bean) {
		GetcalculateSCRes response = new GetcalculateSCRes();
		List<GetcalculateSCRes1> resList = new ArrayList<GetcalculateSCRes1>();
		  List<String> bonusFrom = new ArrayList<String>();
	        List<String> bonusTo = new ArrayList<String>();
	        List<String> bonusLowClaimBonus = new ArrayList<String>();
	        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			try {
				int j=0;
				double bandToprevious=0;
				String bandToNext="";
			
					String bandFrom="0",bandTo="",slidingScaleCom="";
					int a=Integer.parseInt(bean.getScaleminRatio());int b=Integer.parseInt(bean.getScalemaxRatio());int c=Integer.parseInt(bean.getScalebanding()),d=Integer.parseInt(bean.getScaledigit()),e=Integer.parseInt(bean.getScalecombine()),f=999;
					bonusFrom.add(bandFrom);
					bonusTo.add(bean.getScaleminRatio());
					bonusLowClaimBonus.add(String.valueOf((e-(a+0))));
					Map<String,Object> string = new HashMap<String,Object>();
					string.put("1","1");
					list.add(string);
					j++;
				for(int i=a;i<=b;i+=c) {
					if(j==1) {
						bandToprevious=a;
						bandToNext=String.valueOf(((i+c)));	
						
					}else {
						bandToprevious=Double.parseDouble(bandTo);
						bandToNext=String.valueOf(((Double.parseDouble(bandTo)+c)));	
					}
					
					if(bandTo.equals(fm.formattereight(String.valueOf(f)))) {
						bandFrom="";
					}else {
						bandFrom=String.valueOf((bandToprevious)+(1/Math.pow(10, d)));
						System.out.println(j+"A==>"+fm.formattereight(bandFrom));
					}
					
					
					if(((Double.parseDouble(bandToNext)))>b) {
						bandTo=fm.formattereight(String.valueOf(f));
						
					}else {
						bandTo=fm.formattereight(bandToNext);
					}
					System.out.println(j+"B==>"+bandTo);
					if(e<(i+c)) {
						slidingScaleCom=String.valueOf((e-b));
					}else {
						slidingScaleCom=String.valueOf((e-((i+c))));
					}
					System.out.println(j+"C==>"+slidingScaleCom);
					bonusFrom.add(bandFrom);
					bonusTo.add(bandTo);
					bonusLowClaimBonus.add(slidingScaleCom);
					string = new HashMap<String,Object>();
					string.put("1","1");
					list.add(string);
					j++;
				}
				for(int i= 0; i<bonusFrom.size();i++){
					GetcalculateSCRes1 res = new GetcalculateSCRes1();
					res.setScaleFrom(bonusFrom.get(i));
					res.setScaleTo(bonusTo.get(i));
					res.setScaleLowClaimBonus(bonusLowClaimBonus.get(i));
					resList.add(res);
					}
				
//				bean.setScaleFrom(bonusFrom);
//				bean.setScaleTo(bonusTo);
//				bean.setScaleLowClaimBonus(bonusLowClaimBonus);
			//	bean.setScaleCommissionList(list);
					response.setCommonResponse(resList);
					response.setMessage("Success");
			   		response.setIsError(false);
			   	} catch (Exception e) {
			   		e.printStackTrace();
			   		response.setMessage("Failed");
			   		response.setIsError(true);
			   	}
			   	return response;
	}
	@Override
	public GetSectionEditModeRes getSectionEditMode(String proposalNo) {
		GetSectionEditModeRes response = new GetSectionEditModeRes();
		GetSectionEditModeRes1 beanObj = new GetSectionEditModeRes1();
		try {
			List<Map<String, Object>> res = queryImpl.selectList(GetRiskDetailsEditQuery(false),new String[] {proposalNo,proposalNo,proposalNo});
		
			Map<String, Object> resMap = null;
			if(res!=null && res.size()>0)
				resMap = (Map<String, Object>)res.get(0);
			if (resMap!=null) {
				beanObj.setCedingCo(resMap.get("RSK_CEDINGID")==null?"":resMap.get("RSK_CEDINGID").toString());
				beanObj.setIncepDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				beanObj.setExpDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				beanObj.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				beanObj.setUwYearTo(resMap.get("UW_YEAR_TO")==null?"":resMap.get("UW_YEAR_TO").toString());
				beanObj.setBouquetModeYN(resMap.get("BOUQUET_MODE_YN")==null?"N":resMap.get("BOUQUET_MODE_YN").toString());
				beanObj.setBouquetNo(resMap.get("BOUQUET_NO")==null?"":resMap.get("BOUQUET_NO").toString());
				beanObj.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
				beanObj.setBaseLayer(resMap.get("BASE_LAYER")==null?"":resMap.get("BASE_LAYER").toString());
				beanObj.setEndorsmentno(resMap.get("RSK_ENDORSEMENT_NO")==null?"":resMap.get("RSK_ENDORSEMENT_NO").toString());
				beanObj.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				beanObj.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
				beanObj.setProductId(resMap.get("RSK_PRODUCTID")==null?"":resMap.get("RSK_PRODUCTID").toString());
				beanObj.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				response.setCommonResponse(beanObj);
			}
			response.setMessage("Success");
	   		response.setIsError(false);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		response.setMessage("Failed");
	   		response.setIsError(true);
	   	}
	   	return response;
	}
	public String GetRiskDetailsEditQuery(boolean contractMode) {

		String query = "";
		query = "risk.select.getEditModeData";
		if(contractMode){
			query = "risk.select.getEditModeData1";
		}
		else {
			query = "risk.select.getEditModeData2";
		}
		return query.toString();
	}
	@Override
	public CommonSaveRes getSectionDuplicationCheck(GetSectionDuplicationCheckReq formObj) {
		CommonSaveRes response = new CommonSaveRes();
		boolean result=false;
		String query="";
		List<Map<String, Object>> list=null;
		try{
			if (StringUtils.isNotBlank(formObj.getProposalNo()) && StringUtils.isNotBlank(formObj.getSectionNo()) ) {
				query= "risk.select.getSectionDupcheckByProNo";
				
				list=  queryImpl.selectList(query,new String[]{formObj.getSectionNo(),formObj.getProposalNo(),StringUtils.isBlank(formObj.getBaseLayer())?formObj.getProposalNo():formObj.getBaseLayer()});
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> map=(Map<String, Object>)list.get(i);
						String res=map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString();
						if(res.equalsIgnoreCase(formObj.getSectionNo())){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(formObj.getBaseLayer()) && StringUtils.isNotBlank(formObj.getSectionNo()) ){
				query= "risk.select.getSectionDupcheckByBaseLayer";
				
				list= queryImpl.selectList(query,new String[]{formObj.getSectionNo(),formObj.getBaseLayer()});
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> map=(Map<String, Object>)list.get(i);
						String res=map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString();
						if(res.equalsIgnoreCase(formObj.getSectionNo())){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(formObj.getProposalNo()) && StringUtils.isNotBlank(formObj.getSectionNo())){
				query= "risk.select.getSectionDupcheckByBaseLayer";
				
				list= queryImpl.selectList(query,new String[]{formObj.getSectionNo(),formObj.getProposalNo()});
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Map<String, Object> map=(Map<String, Object>)list.get(i);
						String res=map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString();
						if(res.equalsIgnoreCase(formObj.getSectionNo())){
							result=true;
						}
					}
				}
			}
			response.setResponse(String.valueOf(result));
			response.setMessage("Success");
	   		response.setIsError(false);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		response.setMessage("Failed");
	   		response.setIsError(true);
	   	}
	   	return response;
	}
	@Override
	public ConvertPolicyRes convertPolicy(ConvertPolicyReq beanObj) {
		ConvertPolicyRes response = new ConvertPolicyRes();
		ConvertPolicyRes1 res = new ConvertPolicyRes1();
		try {
			try {
				String updateQry = "",insertQry = "",selectQry="",endom="";
				String[] args=null;
				String[] obj=null,obj1=null;
				int out=0;
				int chkSecPageMode = checkSecondPageMode(beanObj.getProposalNo()); //commission table count 0 mode=1 else 2
				int ContractEditMode = contractEditMode(beanObj.getProposalNo()); // get contract no from risk details if empty mode=1 else 2
				if (ContractEditMode == 1) {
					if (chkSecPageMode == 2) {

						selectQry = "risk.select.chechProposalStatus";
						List<Map<String, Object>> result = queryImpl.selectList(selectQry,new String[] {beanObj.getProposalNo(),beanObj.getProposalNo(),beanObj.getProposalNo()});
						
						Map<String, Object> resMap = null;
						if(result!=null &&result.size()>0)
							resMap = (Map<String, Object>)result.get(0);
						if(resMap!=null){
							res.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
							res.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
							res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
							if (res.getProStatus().matches("A") /* && !beanObj.getSharSign().matches("0") */) {
								String maxContarctNo = null;
								
								String query="GET_BASE_LAYER_DETAILS";
								List<Map<String, Object>> res1 =  queryImpl.selectList(query,new String[]{beanObj.getProductId(),beanObj.getBranchCode(),beanObj.getProposalNo()});
							
								Map<String, Object> resMap1 = null;
								if(res1!=null && res1.size()>0)
									resMap1 = (Map<String, Object>)res1.get(0);
								if (resMap1 != null) {
										res.setBaseLayerYN(resMap1.get("BASE_LAYER")==null?"":resMap1.get("BASE_LAYER").toString());
								}
								
								
								String prodid=beanObj.getProductId();
								if ("layer".equalsIgnoreCase(beanObj.getLay())) {
								
									maxContarctNo = beanObj.getContractno();
								}
								else if(StringUtils.isNotBlank(beanObj.getBaseLayerYN())){
									query="GET_BASE_LAYER_DETAILS";
								
									res1 = queryImpl.selectList(query,new String[]{beanObj.getProductId(),beanObj.getBranchCode(),beanObj.getBaseLayerYN()});
									
									resMap1 = null;
									if(res1!=null && res1.size()>0)
										resMap1 = (Map<String, Object>)res1.get(0);
									if (resMap1 != null) {
											res.setContNo(resMap1.get("CONTRACT_NO")==null?"":resMap1.get("CONTRACT_NO").toString());
									}
									maxContarctNo=res.getContNo();
								}
								
								else {
									
									if(!"".equals(beanObj.getRenewalContractNo())&&!"0".equals(beanObj.getRenewalContractNo())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
										maxContarctNo=beanObj.getRenewalContractNo();
									}else{
										
											maxContarctNo=fm.getSequence("Contract",prodid,beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear());
									
										if("RI01".equalsIgnoreCase(beanObj.getSourceId())){
											InsertSectionValueReq req1 = new InsertSectionValueReq();
											req1.setBranchCode(beanObj.getBranchCode());
											req1.setDepartmentId(beanObj.getDepartmentId());
											req1.setLoginId(beanObj.getLoginId());					
											insertSectionValue(req1,maxContarctNo);
										}
									}
								}
							
								args = new String[2];
								args[0] = maxContarctNo;
								args[1] = beanObj.getProposalNo();
								updateQry = "risk.update.contNo";
								
								out=queryImpl.updateQuery(updateQry,args);
							
								updateQry="risk.update.homeContNo";
								args = new String[4];
								args[0] = maxContarctNo;
								res.setContNo((String)args[0]);
								args[1] = "A";
								args[2] = "A";
								args[3] = beanObj.getProposalNo();
								
								out=queryImpl.updateQuery(updateQry,args);
						
								res.setContNo(maxContarctNo);
								if("".equals(beanObj.getRenewalContractNo())||"0".equals(beanObj.getRenewalContractNo())||"NEWCONTNO".equals(beanObj.getRenewalFlag())){
									res.setContractGendration("Your Proposal is converted to Contract with Proposal No : "+beanObj.getProposalNo() +" and Contract No : "+maxContarctNo+".");
								}else{
									res.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
								}
								updateQry ="risk.update.mndInstallments";
								
								out=queryImpl.updateQuery(updateQry,new String[]{maxContarctNo,beanObj.getProposalNo()});	
							} else {
								args = new String[4];
								args[0] = res.getContNo();
								args[1] = getproposalStatus(beanObj
										.getProposalNo());
								args[2] = args[1];
								if (args != null) {

									if (((String) args[1]).equalsIgnoreCase("P")) {
										res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
									}	if (((String) args[1]).equalsIgnoreCase("N")) {
										res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
									}  else if (((String) args[1]).equalsIgnoreCase("A")) {
										res.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+res.getContNo()+".");
									} else if (((String) args[1]).equalsIgnoreCase("R")) {
										res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
									}
								}
								args[3] = beanObj.getProposalNo();
								updateQry="risk.update.homeContNo";
								
								int k=0;
								for(Object str:args)
								out=queryImpl.updateQuery("risk.update.homeContNo",args);
							}
						}
					}
				}else if (ContractEditMode == 2) {
					String endtNo= "";
					endom="risk.select.endo";
					List<Map<String, Object>> list = queryImpl.selectList(endom, new String[]{beanObj.getProposalNo()});
					if(!CollectionUtils.isEmpty(list)) {
						endtNo=list.get(0).get("RSK_ENDORSEMENT_NO")==null?"":list.get(0).get("RSK_ENDORSEMENT_NO").toString();
					}

					obj = updateContractRiskDetailsSecondForm(beanObj,beanObj.getProductId(),endtNo);
					updateQry = "risk.update.pro24ContSecPage";
					out=queryImpl.updateQuery(updateQry, obj);
						res.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+res.getContNo()+".");

					obj1 = updateRiskDetailsSecondFormSecondTable(beanObj, beanObj.getProductId(), getMaxAmednId(beanObj.getProposalNo()));
					updateQry = "risk.update.pro2SecComm";
					out=queryImpl.updateQuery(updateQry, obj1);

					res.setProStatus("A");
				}
				InsertPlacement(beanObj);
				insertRiDetails(beanObj);
				updateRiContractStatus(beanObj);
				
				res.setProductId(beanObj.getProductId());
				
				updateRetentionContractNo(beanObj.getContNo(),beanObj.getProductId(),beanObj.getProposalNo(),beanObj.getDepartmentId());						
						
				updateShareSign(beanObj.getProposalNo());
				dropDowmImpl.updatepositionMasterEndtStatus(beanObj.getProposalNo(),beanObj.getEndorsementDate(),beanObj.getCeaseStatus());
				if(StringUtils.isNotBlank(res.getContNo())){
					dropDowmImpl.getSOATableInsert(beanObj.getProposalNo(), res.getContNo(),beanObj.getBranchCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setCommonResponse(res);
			response.setMessage("Success");
	   		response.setIsError(false);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   		response.setMessage("Failed");
	   		response.setIsError(true);
	   	}
	   	return response;
	}

	public String[] updateContractRiskDetailsSecondForm(ConvertPolicyReq req, String productId, String endNo) {
		String[] args=null;
		args = new String[18];
		args[0] = req.getLimitOurShare();
		args[1] = getDesginationCountry(req.getLimitOurShare(), req.getExchRate());
		args[2] = req.getEpiAsPerOffer();
		args[3] = getDesginationCountry(req.getEpiAsPerOffer(), req.getExchRate());
		args[4] =StringUtils.isBlank(req.getEpiAsPerShare())?"0": req.getEpiAsPerShare();
		args[5] = getDesginationCountry(req.getEpiAsPerShare(), req.getExchRate());
		args[6] = "";
		args[7] = "";
		args[8] = StringUtils.isBlank(req.getPremiumQuotaShare())?"0":req.getPremiumQuotaShare();
		args[9] = StringUtils.isBlank(req.getPremiumSurplus())?"0":req.getPremiumSurplus();
		args[10]= StringUtils.isEmpty(req.getPremiumQuotaShare())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumQuotaShare(), req.getExchRate());
		args[11]= StringUtils.isEmpty(req.getPremiumSurplus())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getPremiumSurplus(), req.getExchRate());
		args[12]=StringUtils.isBlank(req.getCommissionQSAmt())?"0":req.getCommissionQSAmt();
		args[13]=StringUtils.isBlank(req.getCommissionsurpAmt())?"0":req.getCommissionsurpAmt();
		args[14]= StringUtils.isEmpty(req.getCommissionQSAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionQSAmt(), req.getExchRate());
		args[15]= StringUtils.isEmpty(req.getCommissionsurpAmt())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getCommissionsurpAmt(), req.getExchRate());
		args[16]=endNo;
		args[17] = req.getProposalNo();
		return args;
	}
	public String[] updateRiskDetailsSecondFormSecondTable(ConvertPolicyReq req,String productId,String endNo) {
		String[] args=null;
		if (productId.equalsIgnoreCase("2")) {
			args = new String[67];
			args[0] = StringUtils.isEmpty(req.getBrokerage()) ? "0": req.getBrokerage();
			args[1] = req.getTax();
			args[2] = req.getShareProfitCommission();
			args[3] = req.getAcquisitionCost();
			args[4] = getDesginationCountry(req.getAcquisitionCost().replaceAll(",",""),req.getExchRate());
			args[5] =StringUtils.isEmpty(req.getCommissionQS()) ? "0": req.getCommissionQS();
			args[6] = StringUtils.isEmpty(req.getCommissionsurp()) ? "0":req.getCommissionsurp();
			args[7] = StringUtils.isEmpty(req.getOverRidder())?"0":req.getOverRidder();
			//args[8] = StringUtils.isEmpty(req.getManagement_Expenses()) ? "0": req.getManagement_Expenses();
			//args[9] = StringUtils.isEmpty(req.getLossC_F()) ? "0" : req.getLossC_F();
			args[8] = req.getPremiumReserve();
			args[9] = req.getLossreserve();
			args[10] = req.getInterest();
			args[11] = StringUtils.isEmpty(req.getPortfolioinoutPremium()) ? "0": req.getPortfolioinoutPremium();
			args[12] = StringUtils.isEmpty(req.getPortfolioinoutLoss()) ? "0": req.getPortfolioinoutLoss();
			args[13] = req.getLossAdvise();
			args[14] = req.getCashLossLimit();
			args[15] = getDesginationCountry(req.getCashLossLimit(),req.getExchRate());
			args[16] = req.getLeaderUnderwriter();
			args[17] = req.getLeaderUnderwritershare();
			args[18] = req.getAccounts();
			args[19] = req.getExclusion();
			args[20] = StringUtils.isEmpty(req.getRemarks())?"":req.getRemarks();
			args[21] = req.getUnderwriterRecommendations();
			args[22] = req.getGmsApproval();
			//args[25] = StringUtils.isEmpty(req.getProfit_commission()) ? "0"	: req.getProfit_commission();
			args[23] = StringUtils.isEmpty(req.getOthercost()) ? "0"	: req.getOthercost();
			args[24] =req.getCrestaStatus();
			args[25] = req.getEventlimit();
			args[26] =getDesginationCountry(req.getEventlimit(),req.getExchRate());
			args[27] = req.getAggregateLimit();
			args[28] =getDesginationCountry(req.getAggregateLimit(),req.getExchRate());
			args[29] = req.getOccurrentLimit();
			args[30] =getDesginationCountry(req.getOccurrentLimit(),req.getExchRate());
			args[31] = req.getSlideScaleCommission();
			args[32] = req.getLossParticipants();
			args[33] = StringUtils.isEmpty(req.getCommissionSubClass()) ? "": req.getCommissionSubClass();
			args[34] =  req.getDepartmentId();
			args[35] = req.getLoginId();
			args[36] = req.getBranchCode();
			args[37] = StringUtils.isEmpty(req.getLeaderUnderwritercountry())?"":req.getLeaderUnderwritercountry();
			args[38] =StringUtils.isEmpty(req.getOrginalacqcost())?"":req.getOrginalacqcost();
			args[39] = StringUtils.isEmpty(req.getOurassessmentorginalacqcost())?"":req.getOurassessmentorginalacqcost();
			args[40] = StringUtils.isEmpty(req.getOuracqCost())?"":req.getOuracqCost();
			args[41] = StringUtils.isEmpty(req.getLosscommissionSubClass())?"":req.getLosscommissionSubClass();
			args[42] = StringUtils.isEmpty(req.getSlidecommissionSubClass())?"":req.getSlidecommissionSubClass();
			args[43] = StringUtils.isEmpty(req.getCrestacommissionSubClass())?"":req.getCrestacommissionSubClass();
			if("1".equalsIgnoreCase(req.getShareProfitCommission())){
			args[44] = StringUtils.isEmpty(req.getManagementExpenses())?"":req.getManagementExpenses();
			args[45] = StringUtils.isEmpty(req.getCommissionType())?"":req.getCommissionType();
			args[46] = StringUtils.isEmpty(req.getProfitCommissionPer())?"":req.getProfitCommissionPer();
			args[47] = StringUtils.isEmpty(req.getSetup())?"":req.getSetup();
			args[48] = StringUtils.isEmpty(req.getSuperProfitCommission())?"":req.getSuperProfitCommission();
			args[49] = StringUtils.isEmpty(req.getLossCarried())?"":req.getLossCarried();
			args[50] = StringUtils.isEmpty(req.getLossyear())?"":req.getLossyear();
			args[51] = StringUtils.isEmpty(req.getProfitCarried())?"":req.getProfitCarried();
			args[52] = StringUtils.isEmpty(req.getProfitCarriedForYear())?"":req.getProfitCarriedForYear();
			args[53] = StringUtils.isEmpty(req.getFistpc())?"":req.getFistpc();
			args[54] = StringUtils.isEmpty(req.getProfitMont())?"":req.getProfitMont();
			args[55] = StringUtils.isEmpty(req.getSubProfitMonth())?"":req.getSubProfitMonth();
			args[56] = StringUtils.isEmpty(req.getSubpc())?"":req.getSubpc();
			args[57] = StringUtils.isEmpty(req.getSubSeqCalculation())?"":req.getSubSeqCalculation();
			args[58] = StringUtils.isEmpty(req.getProfitCommission())?"":req.getProfitCommission();
			}
			else{
				args[44] = "";
				args[45] =  "";
				args[46] =  "";
				args[47] =  "";
				args[48] =  "";
				args[49] =  "";
				args[50] =  "";
				args[51] =  "";
				args[52] =  "";
				args[53] =  "";
				args[54] =  "";
				args[55] =  "";
				args[56] =  "";
				args[57] =  "";
				args[58] =  "";
			}
			args[59] = StringUtils.isEmpty(req.getDocStatus())? "" :req.getDocStatus();
			args[60] = StringUtils.isEmpty(req.getLocRate())? "" :req.getLocRate();
			args[61] =StringUtils.isEmpty(req.getPremiumResType())? "" :req.getPremiumResType();
			args[62] = StringUtils.isEmpty(req.getPcfpcType())?"":req.getPcfpcType();
			args[63] = StringUtils.isEmpty(req.getPcfixedDate())?"":req.getPcfixedDate();
			args[64] = StringUtils.isEmpty(req.getPortfolioType())?"":req.getPortfolioType();
			args[65] = req.getProposalNo();
			args[66] = endNo;
		} 
		return args;
	}
	private void InsertPlacement(ConvertPolicyReq beanObj) {
		UpdatePlacementReq bean = new UpdatePlacementReq();
		List<UpdatePlacementListReq> reqList = new ArrayList<UpdatePlacementListReq>();
		try {
			String statusNo=fm.getSequence("StatusNo","0","0", beanObj.getBranchCode(),"","");
			
			for(int i=0;i<beanObj.getConvertPolicyReq1().size();i++) {
				ConvertPolicyReq1 req1 = beanObj.getConvertPolicyReq1().get(i);
				UpdatePlacementListReq req = new UpdatePlacementListReq();
				if("CSL".equalsIgnoreCase(req1.getCurrentStatus()) || "CSL".equalsIgnoreCase(req1.getNewStatus())) {
					req.setSno(req1.getSnos());	
					req.setBaseproposalNo(req1.getBaseproposalNos());
					req.setBouquetNo(req1.getBouquetNos());
					req.setReinsurerId(req1.getReinsurerIds());
					req.setBrokerId(req1.getBrokerIds());
					req.setProposalNo(req1.getProposalNo());
					req.setShareOffered(req1.getShareOffered());
					req.setWrittenLine(req1.getWrittenLine());
					req.setBrokerage(req1.getBrokerages());
					req.setWrittenvaliditydate("");
					req.setWrittenvalidityRemarks("");
					req.setProposedWL(req1.getProposedWL());	
					req.setSignedLine(req1.getSignedLine());
					req.setSignedLineValidity("");
					req.setSignedLineRemarks("");
					req.setProposedSL(req1.getProposedSL());	
					req.setPsignedLine("");			
					reqList.add(req);
				}
			//	statusNos.add(statusNo);
			}
			bean.setPlacementListReq(reqList);
			
			
//			bean.setEmailStatus(emailStatus);
			bean.setBranchCode(beanObj.getBranchCode());
			bean.setNewStatus("C");
			bean.setCurrentStatus("CSL");
			bean.setUserId(beanObj.getLoginId());
			beanObj.setStatusNo(statusNo); //doubt
			bean.setStatusNo(statusNo);
			placeImple.updatePlacement(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void insertRiDetails(ConvertPolicyReq bean) {
		String query="";
		try {
			query="INSERT_RI_DETAILS";
			for(int i=0;i<bean.getConvertPolicyReq1().size();i++) {
				ConvertPolicyReq1 req = bean.getConvertPolicyReq1().get(i);
				if("CSL".equalsIgnoreCase(req.getCurrentStatus()) || "CSL".equalsIgnoreCase(req.getNewStatus())) {
					bean.setSubcontractNo(bean.getContNo()+(StringUtils.isBlank(bean.getLayerNo())?bean.getSectionNo():bean.getLayerNo())+req.getSnos());
					String obj[]=new String[23];
					obj[0]=req.getStatusNo();
					obj[1]=req.getSnos();
					obj[2]=req.getBouquetNos();
					obj[3]=req.getBaseproposalNos();
					obj[4]=bean.getProposalNo();
					obj[5]=bean.getContNo();
					obj[6]=bean.getSubcontractNo();
					obj[7]=bean.getLayerNo();
					obj[8]=bean.getSectionNo();
					obj[9]=bean.getAmendId();
					obj[10]=req.getReinsurerIds();
					obj[11]=req.getBrokerIds();
					obj[12]=req.getShareOffered();
					obj[13]=req.getWrittenLine();
					obj[14]=req.getProposedWL();
					obj[15]=req.getSignedLine();
					obj[16]=req.getProposedSL();
					obj[17]=req.getBrokerages();
					obj[18]="CSL";
					obj[19]="C";
					obj[20]="Y";
					obj[21]=bean.getLoginId();
					obj[22]=bean.getBranchCode();
					queryImpl.updateQuery(query,obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateRiContractStatus(ConvertPolicyReq bean) {
		String query="";
		try {
			for(int i=0;i<bean.getConvertPolicyReq1().size();i++) {
				ConvertPolicyReq1 req = bean.getConvertPolicyReq1().get(i);
				if("CSL".equalsIgnoreCase(req.getCurrentStatus())|| "CSL".equalsIgnoreCase(req.getNewStatus())) {
					String obj[]=new String[7];
					obj[0]=bean.getContNo();
					obj[1]=bean.getAmendId();
					obj[2]=bean.getProposalNo();
					obj[3]=req.getReinsurerIds();
					obj[4]=req.getBrokerIds();
					obj[5]=bean.getBranchCode();
					obj[6]=req.getStatusNo();
					query="UPDATE_RI_CONTRACT";
				
					queryImpl.updateQuery(query,obj);
					obj=new String[6];
					obj[0]=bean.getContNo();
					obj[1]=bean.getProposalNo();
					obj[2]=req.getReinsurerIds();
					obj[3]=req.getBrokerIds();
					obj[4]=bean.getBranchCode();
					obj[5]=req.getStatusNo();
					query="UPDATE_MAIL_CONTRACT";
				
					queryImpl.updateQuery(query,obj);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateShareSign(String prroposalNo) {
		String query="";
		try {
			query="UPDATE_SHARE_SHIGN";
			queryImpl.updateQuery(query,new String[] {prroposalNo});			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



	
	





	
