package com.maan.insurance.service.impl.retro;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.propPremium.TtrnRetroCessionary;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CountryMaster;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TmasPolicyBranch;
import com.maan.insurance.model.entity.TmasTerritory;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.repository.ConstantDetailRepository;
import com.maan.insurance.model.repository.CountryMasterRepository;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TmasTerritoryRepository;
import com.maan.insurance.model.repository.TtrnBonusRepository;
import com.maan.insurance.model.repository.TtrnCommissionDetailsRepository;
import com.maan.insurance.model.repository.TtrnCrestazoneDetailsRepository;
import com.maan.insurance.model.repository.TtrnMndInstallmentsRepository;
import com.maan.insurance.model.repository.TtrnRetroCessionaryRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;
import com.maan.insurance.model.repository.TtrnRiskRemarksRepository;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq1;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RetroCessListReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.retro.FirstInsertReq;
import com.maan.insurance.model.req.retro.GetEndDateReq;
import com.maan.insurance.model.req.retro.InsertBonusDetailsReq;
import com.maan.insurance.model.req.retro.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.retro.InsertProfitCommissionMainReq;
import com.maan.insurance.model.req.retro.InsertRemarkDetailsReq;
import com.maan.insurance.model.req.retro.InsertRetroDetailsReq;
import com.maan.insurance.model.req.retro.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.retro.SaveSecondPageReq;
import com.maan.insurance.model.req.retro.ShowSecondPageData1Req;
import com.maan.insurance.model.req.retro.ShowSecondPageDataReq;
import com.maan.insurance.model.req.retro.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.retro.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.retro.ViewRiskDetailsReq;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.InstalmentListRes1;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;
import com.maan.insurance.model.res.nonproportionality.RetroCessListRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes1;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes1;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes1;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.retro.FirstInsertRes1;
import com.maan.insurance.model.res.retro.GetRemarksDetailsRes1;
import com.maan.insurance.model.res.retro.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.retro.RiskDetailsEditModeRes1;
import com.maan.insurance.model.res.retro.SaveSecondPageRes;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res1;
import com.maan.insurance.model.res.retro.ShowSecondPageDataRes;
import com.maan.insurance.model.res.retro.ShowSecondPageDataRes1;
import com.maan.insurance.model.res.retro.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.retro.ShowSecondpageEditItemsRes1;
import com.maan.insurance.model.res.retro.ViewRiskDetailsRes;
import com.maan.insurance.model.res.retro.ViewRiskDetailsRes1;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.retro.RetroService;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;

@Service
public class RetroServiceImple implements RetroService {
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;
	
	@Autowired
	private TtrnRiskDetailsRepository rdRepo;
	
	@Autowired
	private TtrnRiskProposalRepository rpRepo;
	@Autowired
	private PositionMasterRepository pmRepo;
	@Autowired
	private TtrnRiskRemarksRepository rrRepo;
	@Autowired
	private ConstantDetailRepository cdRepo;
	@Autowired
	private TmasTerritoryRepository ttRepo;
	@Autowired
	private 	CountryMasterRepository cmRepo;
	@Autowired
	private 	TtrnMndInstallmentsRepository mndRepo;
	@Autowired
	private TtrnRetroCessionaryRepository cessRepo;
	@Autowired
	private TtrnRiskCommissionRepository rcRepo;
	@Autowired
	private TtrnCrestazoneDetailsRepository cresRepo;
	@Autowired
	private TtrnBonusRepository  bonusRepo;
	@Autowired
	private TtrnCommissionDetailsRepository commRepo;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public FirstInsertRes firstInsert(FirstInsertReq req) {
		FirstInsertRes response = new FirstInsertRes();
		FirstInsertRes1 res1 = new FirstInsertRes1();
		boolean savFlg = false,ChkSavFlg = false;
		String arg1 = "";
		try {
			if ("true".equalsIgnoreCase(req.getSaveFlag())) {
				ChkSavFlg = checkEditSaveModeMethod(req);
				if (ChkSavFlg){
					 Map<String,Object>  values = getFirstPageEditSaveModeAruguments(req, req.getProductId(),getMaxAmednId(req.getProposalNo()));
					 int updateCount =   values.get("UpdateCount")==null?0:(int) values.get("UpdateCount");
					 String arg30 = values.get("Arg30")==null?"":values.get("Arg30").toString();
					 arg1 = ((Integer.parseInt((String)arg30)+1)+"");
					if (updateCount > 0) {
						savFlg = true;
						res1.setSaveFlag(String.valueOf(savFlg));
					} else {
						savFlg = false;
						res1.setSaveFlag(String.valueOf(savFlg));
						response.setCommonResponse(res1);
						response.setMessage("Success");
						response.setIsError(false);
						return response;
					}
				} else {
					 Map<String,Object>  values = getFirstPageInsertAruguments(req, req.getProductId(), Boolean.valueOf(req.getAmendId()));
					int res = values.get("res")==null?0:(int) values.get("res");
					arg1 = values.get("Arg1")==null?"":values.get("Arg1").toString();
					res1.setContractGendration("Your Proposal Number :"+ req.getProposalNo());
				}
			} else {
				String maxAmendID = getMaxAmednId(req.getProposalNo());
				if(maxAmendID.equalsIgnoreCase(req.getAmendId())){
					 Map<String,Object>  values = getFirstPageInsertAruguments(req, req.getProductId(), Boolean.valueOf(req.getAmendId()));
					int res = values.get("res")==null?0:(int) values.get("res");
					arg1 = values.get("Arg1")==null?"":values.get("Arg1").toString();
				}else{
					 Map<String,Object>  values = getFirstPageEditSaveModeAruguments(req, req.getProductId(),maxAmendID);
					int updateCount = values.get("UpdateCount")==null?0:(int)  values.get("UpdateCount");
					 String arg30 = values.get("Arg30")==null?"":values.get("Arg30").toString();
					 arg1 =  ((Integer.parseInt((String)arg30)+1)+"");					
				}
			}
			 Map<String,Object>  values = insertRiskProposal(req,Boolean.valueOf(req.getSaveFlag()),req.getProductId(),ChkSavFlg,Boolean.valueOf(req.getAmendId()),(String)arg1);
			savFlg = (boolean) values.get("InsertFlag");
			res1.setContractGendration(String.valueOf(values.get("res1")));
			//InsertRemarkDetails(beanObj);
			savFlg = true;
			res1.setSaveFlag(String.valueOf(savFlg));
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	private boolean checkEditSaveModeMethod(final FirstInsertReq beanObj) {
		boolean editSaveMode = false;
		try {
			String[] args = new String[1];
			args[0] = beanObj.getProposalNo();
			TtrnRiskDetails list = rdRepo.findByRskProposalNumber(beanObj.getProposalNo());
			if ( list == null) {
				editSaveMode = false;
			} else {
				editSaveMode = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return editSaveMode;
	}
	public  Map<String,Object> getFirstPageEditSaveModeAruguments(final FirstInsertReq beanObj, final String pid,String endNo) throws ParseException {
		int updateCount = 0;
		 Map<String,Object>  values = new HashMap<String,Object>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		TtrnRiskDetails entity = rdRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		entity.setRskDeptid(StringUtils.isEmpty(beanObj.getDepartId()) ? BigDecimal.ZERO :new BigDecimal( beanObj.getDepartId()));
		entity.setRskPfcid(StringUtils.isEmpty(beanObj.getProfitCenter()) ? "0" : beanObj.getProfitCenter());
		entity.setRskSpfcid(StringUtils.isEmpty(beanObj.getSubProfitcenter()) ? "0" : beanObj.getSubProfitcenter());
		entity.setRskPolbranch(StringUtils.isEmpty(beanObj.getPolBr()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPolBr()));
		entity.setRskCedingid(StringUtils.isEmpty(beanObj.getCedingCo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCedingCo()));
		entity.setRskBrokerid(StringUtils.isEmpty(beanObj.getBroker()) ?  BigDecimal.ZERO :new BigDecimal(beanObj.getBroker()));
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyNametype()) ? "" : beanObj.getTreatyNametype());
		entity.setRskMonth(StringUtils.isEmpty(beanObj.getMonth()) ? null : sdf.parse(beanObj.getMonth()));
		entity.setRskUwyear(StringUtils.isEmpty(beanObj.getUwYear()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getUwYear()));
		entity.setRskUnderwritter(StringUtils.isEmpty(beanObj.getUnderwriter()) ? "" : beanObj.getUnderwriter());
		entity.setRskInceptionDate(StringUtils.isEmpty(beanObj.getIncepDate()) ?  null :  sdf.parse(beanObj.getIncepDate()));
		entity.setRskExpiryDate(StringUtils.isEmpty(beanObj.getExpDate()) ?null :  sdf.parse(beanObj.getExpDate()));
		entity.setRskAccountDate( StringUtils.isEmpty(beanObj.getAccDate()) ?  null :  sdf.parse(beanObj.getAccDate()));
		entity.setRskOriginalCurr(StringUtils.isEmpty(beanObj.getOrginalCurrency()) ? "0" : beanObj.getOrginalCurrency());
		entity.setRskExchangeRate(StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getExchRate()));
		entity.setRskBasis(StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis());
		entity.setRskPeriodOfNotice(StringUtils.isEmpty(beanObj.getPnoc()) ? "" : beanObj.getPnoc());
		entity.setRskRiskCovered(StringUtils.isEmpty(beanObj.getRiskCovered()) ? "" : beanObj.getRiskCovered());
		entity.setRskTerritoryScope(StringUtils.isEmpty(beanObj.getTerritoryscope()) ? "" : beanObj.getTerritoryscope());
		entity.setRskTerritory(StringUtils.isEmpty(beanObj.getTerritory()) ? "" : beanObj.getTerritory());
		if ("4".equalsIgnoreCase(pid)) {
			entity.setRskStatus(StringUtils.isEmpty(beanObj.getProStatus()) ? "" : beanObj.getProStatus());
			entity.setRskProposalType(StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType());
			entity.setRskAccountingPeriod(StringUtils.isEmpty(beanObj.getAccountingPeriod()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getAccountingPeriod()));
			entity.setRskReceiptStatement(StringUtils.isEmpty(beanObj.getReceiptofStatements()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReceiptofStatements()));
			entity.setRskReceiptPayement(StringUtils.isEmpty(beanObj.getReceiptofPayment()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReceiptofPayment()));
		} else if ("5".equalsIgnoreCase(pid)||"3".equalsIgnoreCase(pid)) {
			entity.setRskStatus(StringUtils.isEmpty(beanObj.getProStatus()) ? ""	: beanObj.getProStatus());
			entity.setRskProposalType(StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType());
			entity.setRskAccountingPeriod(BigDecimal.ZERO);
			entity.setRskReceiptStatement(BigDecimal.ZERO);
			entity.setRskReceiptPayement(BigDecimal.ZERO);
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
		}
		entity.setMndInstallments(StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getMdInstalmentNumber()));
		entity.setRetroCessionaries(StringUtils.isEmpty(beanObj.getNoRetroCess()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getNoRetroCess()));
		entity.setRskRetroType(StringUtils.isEmpty(beanObj.getRetroType()) ? "0" : beanObj.getRetroType());
		entity.setRskInsuredName(StringUtils.isEmpty(beanObj.getInsuredName()) ? "" : beanObj.getInsuredName());
		entity.setInwardBusType(StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType());
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType());
		entity.setRskBusinessType(StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType());
		entity.setRskExchangeType(StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType());
		entity.setRskPerilsCovered(StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered());
		entity.setRskLocIssued(StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued());
		entity.setRskUmbrellaXl(StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL());
		entity.setLoginId(beanObj.getLoginId());
		entity.setBranchCode(beanObj.getBranchCode());
		entity.setCountriesInclude(StringUtils.isEmpty(beanObj.getCountryIncludedList()) ? ""	:beanObj.getCountryIncludedList());
		entity.setCountriesExclude(StringUtils.isEmpty(beanObj.getCountryExcludedList()) ? ""	: beanObj.getCountryExcludedList());
		entity.setRskNoOfLine(StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", ""));
		entity.setRsEndorsementType(StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype());
		entity.setRskRunOffYear(StringUtils.isEmpty(beanObj.getRunoffYear()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getRunoffYear()));
		entity.setRskLocBnkName(StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName());
		entity.setRskLocCrdtAmt(StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditAmt().replaceAll(",", "")));
		entity.setRskLocCrdtPrd(StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditPrd()));
		entity.setRskLocBenfcreName(StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName());
		entity.setRskCessionExgRate(StringUtils.isEmpty(beanObj.getCessionExgRate()) ? ""	:beanObj.getCessionExgRate());
		entity.setRskFixedRate(StringUtils.isEmpty(beanObj.getFixedRate()) ? new BigDecimal("")	:new BigDecimal(beanObj.getFixedRate()));
		entity.setRetentionyn(StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN());
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));
		rdRepo.save(entity);
		 updateCount = 1;
			values.put("UpdateCount" , updateCount);
			values.put("Arg30" , entity.getTreatytype() );
		return values;
	}
	private String getMaxAmednId(final String proposalNo) {
		String result ="";
		try{
			TtrnRiskProposal list =	rpRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			 if (list != null) {
				 result =String.valueOf(list.getRskEndorsementNo());
				} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public  Map<String,Object> getFirstPageInsertAruguments(final FirstInsertReq beanObj, final String pid, final boolean amendId) throws ParseException {
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		 Map<String,Object>  values = new HashMap<String,Object>();
		int res=0;
		TtrnRiskDetails entity = new TtrnRiskDetails();
		if (amendId) {
			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskEndorsementNo(new BigDecimal((Integer.parseInt(getMaxAmednId(beanObj.getProposalNo()))+1)+""));
			entity.setRskContractNo(beanObj.getContNo());
		}else{
			entity.setRskContractNo("");
			beanObj.setProposalNo(getMaxProposanlno(pid,beanObj.getBranchCode(),beanObj.getRetroType(),beanObj.getDepartmentId()));
			entity.setRskProposalNumber( beanObj.getProposalNo());
			entity.setRskEndorsementNo(BigDecimal.ZERO);
		}
		if (pid.equalsIgnoreCase("4")) {
			entity.setRskLayerNo(BigDecimal.ZERO);
			entity.setRskProposalType(StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType());
			entity.setRskAccountingPeriod(StringUtils.isEmpty(beanObj.getAccountingPeriod()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getAccountingPeriod()));
			entity.setRskReceiptStatement(StringUtils.isEmpty(beanObj.getReceiptofStatements()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getReceiptofStatements()));
			entity.setRskReceiptPayement(StringUtils.isEmpty(beanObj.getReceiptofPayment()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getReceiptofPayment()));
	
		}else if ("5".equalsIgnoreCase(pid)) {
			entity.setRskLayerNo( new BigDecimal(beanObj.getLayerNo()));
			entity.setRskProposalType("");
			entity.setRskAccountingPeriod(BigDecimal.ZERO);
			entity.setRskReceiptStatement(BigDecimal.ZERO);
			entity.setRskReceiptPayement(BigDecimal.ZERO);
			entity.setRskProposalType("");
			entity.setRskAccountingPeriod(BigDecimal.ZERO);
			entity.setRskReceiptStatement(BigDecimal.ZERO);
			entity.setRskReceiptPayement(BigDecimal.ZERO);
		
		}
		entity.setRskLayerNo( new BigDecimal(pid));
		entity.setRskDeptid(StringUtils.isEmpty(beanObj.getDepartId()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getDepartId()));
		entity.setRskPfcid(StringUtils.isEmpty(beanObj.getProfitCenter()) ? "0" : beanObj.getProfitCenter());
		entity.setRskSpfcid(StringUtils.isEmpty(beanObj.getSubProfitcenter()) ? "0" : beanObj.getSubProfitcenter());
		entity.setRskPolbranch(StringUtils.isEmpty(beanObj.getPolBr()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getPolBr()));
		entity.setRskCedingid(StringUtils.isEmpty(beanObj.getCedingCo()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getCedingCo()));
		entity.setRskBrokerid(StringUtils.isEmpty(beanObj.getBroker()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getBroker()));
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyNametype()) ? "" : beanObj.getTreatyNametype());
		entity.setRskMonth(StringUtils.isEmpty(beanObj.getMonth()) ? null : sdf.parse(beanObj.getMonth()));
		entity.setRskUwyear(StringUtils.isEmpty(beanObj.getUwYear()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getUwYear()));
		entity.setRskUwyear(StringUtils.isEmpty(beanObj.getUnderwriter()) ?  new BigDecimal("") :  new BigDecimal(beanObj.getUnderwriter()));
		entity.setRskInceptionDate(StringUtils.isEmpty(beanObj.getIncepDate()) ? null : sdf.parse(beanObj.getIncepDate()));
		entity.setRskExpiryDate(StringUtils.isEmpty(beanObj.getExpDate()) ? null : sdf.parse(beanObj.getExpDate()));
		entity.setRskAccountDate(StringUtils.isEmpty(beanObj.getAccDate()) ?null : sdf.parse(beanObj.getAccDate()));
		entity.setRskOriginalCurr(StringUtils.isEmpty(beanObj.getOrginalCurrency()) ? "0" : beanObj.getOrginalCurrency());
		entity.setRskExchangeRate(StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getExchRate()));
		entity.setRskBasis(StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis());
		entity.setRskPeriodOfNotice(StringUtils.isEmpty(beanObj.getPnoc()) ? "" : beanObj.getPnoc());
		entity.setRskRiskCovered(StringUtils.isEmpty(beanObj.getRiskCovered()) ? "0"	: beanObj.getRiskCovered());
		entity.setRskTerritoryScope(StringUtils.isEmpty(beanObj.getTerritoryscope()) ? "" : beanObj.getTerritoryscope());
		entity.setRskTerritory(StringUtils.isBlank(beanObj.getTerritory())?"" :beanObj.getTerritory());
		entity.setRskStatus(StringUtils.isEmpty(beanObj.getProStatus()) ? "0" : beanObj.getProStatus());
		entity.setRskRemarks("");
		entity.setMndInstallments(StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdInstalmentNumber()));
		entity.setRetroCessionaries(StringUtils.isEmpty(beanObj.getNoRetroCess()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getNoRetroCess()));
		entity.setRskRetroType(StringUtils.isEmpty(beanObj.getRetroType()) ? "0" : beanObj.getRetroType());
		entity.setRskInsuredName(StringUtils.isEmpty(beanObj.getInsuredName()) ? "" : beanObj.getInsuredName());
		entity.setOldContractno(StringUtils.isEmpty(beanObj.getRenewalcontractno()) ? "": beanObj.getRenewalcontractno());
		entity.setInwardBusType(StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType());
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType());
		entity.setRskBusinessType(StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType());
		entity.setRskExchangeType(StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType());
		entity.setRskPerilsCovered(StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered());
		entity.setRskLocIssued(StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued());
		entity.setRskUmbrellaXl(StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL());
		entity.setLoginId( beanObj.getLoginId());
		entity.setBranchCode(beanObj.getBranchCode());
		entity.setCountriesInclude(StringUtils.isEmpty(beanObj.getCountryIncludedList()) ? ""	:beanObj.getCountryIncludedList());
		entity.setCountriesExclude(StringUtils.isEmpty(beanObj.getCountryExcludedList()) ? ""	: beanObj.getCountryExcludedList());
		entity.setRskNoOfLine(StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", ""));
		entity.setRsEndorsementType(StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype());
		entity.setRskRunOffYear(StringUtils.isEmpty(beanObj.getRunoffYear()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getRunoffYear()));
		entity.setRskLocBnkName(StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName());
		entity.setRskLocCrdtPrd(StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditPrd()));
		entity.setRskLocCrdtAmt(StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditAmt().replaceAll(",", "")));
		entity.setRskLocBenfcreName(StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName());
		entity.setRskCessionExgRate(StringUtils.isEmpty(beanObj.getCessionExgRate()) ? ""	:beanObj.getCessionExgRate());
		entity.setRskFixedRate(StringUtils.isEmpty(beanObj.getFixedRate()) ? new BigDecimal("")	:new BigDecimal(beanObj.getFixedRate()));
		entity.setRetentionyn(StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN());
		entity.setRskEntryDate(new Date());
		entity.setSysDate(new Date());
		rdRepo.save(entity);
		 res=1;
		 values.put("res" , res);
		values.put("Arg1" , entity.getRskEndorsementNo() );
		return values;
	}
	public String getMaxProposanlno(String pid,String branchCode,String retroType, String deptId) {
		String result="";
		try{
			result = fm.getSequence("Proposal", "SR".equalsIgnoreCase(retroType) ? "5" : "4", deptId, branchCode,"","");
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	private   Map<String,Object> insertRiskProposal(final FirstInsertReq beanObj,final boolean saveFlag, final String pid,final boolean ChekmodeFlag,boolean amendId,final String amednIdvalue) {
		boolean InsertFlag = false;
		FirstInsertRes1 res1 = new FirstInsertRes1();
		Map<String,Object>  values = new HashMap<String,Object>();
		try {
			String maxAmendId="0";
			String endtNo="";
			int count = 0;
			if(!"0".endsWith(amednIdvalue))
				maxAmendId=(Integer.parseInt(amednIdvalue)-1)+"";
			if (saveFlag) {
				if (ChekmodeFlag) {
					TtrnRiskProposal list =	rpRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(beanObj.getProposalNo());
					 if (list != null) {
						 endtNo =String.valueOf(list.getRskEndorsementNo());
						} 
					 count = getProposalSaveEditModeQuery(beanObj, pid,endtNo);
					if (count > 0) {
						if(StringUtils.isNotBlank(beanObj.getContNo())) {
							res1.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ beanObj.getProposalNo());
						}
						else if (beanObj.getProStatus().equalsIgnoreCase("A") || beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
							res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
							res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
							res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
						}
					}
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					count = updateHomePositionMasterAruguments(beanObj, pid,"0");
					if (count > 0) {
						if (pid.equalsIgnoreCase("4")) {
							if(StringUtils.isNotBlank(beanObj.getContNo())) {
								res1.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ beanObj.getProposalNo());
							}
							else if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
								res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
							}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
								res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
							}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
								res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
							}
						}else if (pid.equalsIgnoreCase("5")) {
							if(StringUtils.isNotBlank(beanObj.getContNo())) {
								res1.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ beanObj.getProposalNo());
							}
							if (beanObj.getProStatus().equalsIgnoreCase("A") || beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
								res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No : "+beanObj.getLayerNo());
							}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
								res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No : "+beanObj.getLayerNo());
							}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
								res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo() +" and Layer No : "+beanObj.getLayerNo());
							}
						}
					}
				} else
				{
					int res = getFirstPageSecondTableAruguments(beanObj, pid, amednIdvalue, amendId);
					String renewalStatus = getRenewalStatus(beanObj);
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					res = insertHomePositionMasterAruguments(beanObj, pid,amednIdvalue, amendId,renewalStatus);
					if (pid.equalsIgnoreCase("4")) {
						if (beanObj.getProStatus().equalsIgnoreCase("A")|| beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
							res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
							res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
							res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
						}
					}else if (pid.equalsIgnoreCase("5")) {
						if (beanObj.getProStatus().equalsIgnoreCase("A") || beanObj.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(beanObj.getProStatus())) {
							res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No : "+beanObj.getLayerNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("N")){
							res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No : "+beanObj.getLayerNo());
						}else if(beanObj.getProStatus().equalsIgnoreCase("R")){
							res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo() +" and Layer No : "+beanObj.getLayerNo());
						}
					}
				}
			} else {
				if (!ChekmodeFlag) {
					if (maxAmendId.equalsIgnoreCase(beanObj.getAmendId())){
						count = getFirstPageSecondTableInsertAruguments(beanObj,pid, amednIdvalue, amendId);
					}else{
						 count = getProposalSaveEditModeQuery(beanObj, pid,maxAmendId);
					}
					if (count > 0) {
						InsertFlag = true;
					}
					updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
					if(maxAmendId.equalsIgnoreCase(beanObj.getAmendId())){
						String renewalStatus = getRenewalStatus(beanObj);
						count = insertHomePositionMasterAruguments(beanObj, pid,amednIdvalue, amendId,renewalStatus);
					}else{
						count = updateHomePositionMasterAruguments(beanObj, pid,maxAmendId);
					}
					if (count > 0){
						InsertFlag = true;
					}
					if (beanObj.getProStatus().equalsIgnoreCase("R")) {
						res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
					}
					String proposalno="";
					if (StringUtils.isNotEmpty(beanObj.getLayerProposalNo())) {
						proposalno = beanObj.getLayerProposalNo();
					} else {
						proposalno = beanObj.getProposalNo();
					}
				//	this.showSecondpageEditItems(beanObj, pid, proposalno);
				}
			}
			values.put("InsertFlag" , InsertFlag);
			values.put("res1" , res1);
		} catch (Exception e) {
			InsertFlag = false;
			e.printStackTrace();
		}
		return values;
	}
	public int getProposalSaveEditModeQuery(final FirstInsertReq beanObj, final String pid,String endNo) {
		TtrnRiskProposal rpEntity = rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		if (pid.equalsIgnoreCase("4")) {
			if("TR".equalsIgnoreCase(beanObj.getRetroType())){
			rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?  BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			}
			else{
				rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getFaclimitOrigCur()));
				rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchRate())));
				}
			rpEntity.setRskEpiOfferOc(StringUtils.isEmpty(beanObj.getEpiorigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiorigCur()));
			rpEntity.setRskEpiOfferDc(StringUtils.isEmpty(beanObj.getEpiorigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchRate())));
			rpEntity.setRskEpiEstimate(StringUtils.isEmpty(beanObj.getOurEstimate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurEstimate()));
			rpEntity.setRskXlcostOc(StringUtils.isEmpty(beanObj.getXlCost()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlCost()));
			rpEntity.setRskXlcostDc(StringUtils.isEmpty(beanObj.getXlCost()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlCost(), beanObj.getExchRate())));
			rpEntity.setRskCedantRetention(StringUtils.isEmpty(beanObj.getCedReten()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getCedReten()));
			rpEntity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			rpEntity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			rpEntity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				rpEntity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			}
			rpEntity.setRskCedretType(StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType());
			rpEntity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			rpEntity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			rpEntity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			rpEntity.setRskEventLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			rpEntity.setRskEventLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerOffer()));
			rpEntity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsoeOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerShare()));
			rpEntity.setRskEpiOsoeDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));
			rpEntity.setRskXlcostOsOc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlcostOurShare()));
			rpEntity.setRskXlcostOsDc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchRate())));
			rpEntity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			rpEntity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			rpEntity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			rpEntity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			rpEntity.setRskTreatySurpLimitOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOC()));
			rpEntity.setRskTreatySurpLimitDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchRate())));
			rpEntity.setRskTreatySurpLimitOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOurShare()));
			rpEntity.setRskTreatySurpLimitOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchRate())));
			if("TR".equalsIgnoreCase(beanObj.getRetroType())){
				rpEntity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml());
				rpEntity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchRate()));
			}
			else{
				rpEntity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml()) ? "0": beanObj.getFaclimitOrigCurPml());
				rpEntity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCurPml(), beanObj.getExchRate()));
				}
			rpEntity.setRskTrtyLmtPmlOsOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0": beanObj.getLimitOrigCurPmlOS());
			rpEntity.setRskTrtyLmtPmlOsDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtSurPmlOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0": beanObj.getTreatyLimitsurplusOCPml());
			rpEntity.setRskTrtyLmtSurPmlDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtSurPmlOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0": beanObj.getTreatyLimitsurplusOCPmlOS());		
			rpEntity.setRskTrtyLmtSurPmlOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtOurassPmlOc(StringUtils.isEmpty(beanObj.getEpipml()) ? "0": beanObj.getEpipml());
			rpEntity.setRskTrtyLmtOurassPmlDc(StringUtils.isEmpty(beanObj.getEpipml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getEpipml(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtOurassPmlOsOc	(StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0": beanObj.getEpipmlOS());
			rpEntity.setRskTrtyLmtourAssPmlOsDc(StringUtils.isEmpty(beanObj.getEpipmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchRate()));
			rpEntity.setSubClass(new BigDecimal(beanObj.getDepartId()));
			rpEntity.setLoginId(beanObj.getLoginId());
			rpEntity.setBranchCode(beanObj.getBranchCode());
			rpEntity.setRskPml(StringUtils.isEmpty(beanObj.getPml()) ? "" : beanObj.getPml());
			rpEntity.setRskPmlPercent(StringUtils.isEmpty(beanObj.getPmlPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getPmlPercent()));
//			rpEntity.setRskProposalNumber(beanObj.getProposalNo());
//			rpEntity.setRskEndorsementNo(new BigDecimal(endNo));
			rpRepo.save(rpEntity);
			count=1;
		}
		if ("5".equalsIgnoreCase(pid)) {
			rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur())	|| StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			rpEntity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			rpEntity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			rpEntity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			rpEntity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			rpEntity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getMaxLimitProduct()));
			rpEntity.setRskSubjPremiumOc(StringUtils.isEmpty(beanObj.getSubPremium()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getSubPremium()));
			rpEntity.setRskSubjPremiumDc(StringUtils.isEmpty(beanObj.getSubPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getSubPremium(), beanObj.getExchRate())));
			rpEntity.setRskXlpremOc(StringUtils.isEmpty(beanObj.getXlPremium()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getXlPremium()));
			rpEntity.setRskXlpremDc(StringUtils.isEmpty(beanObj.getXlPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlPremium(), beanObj.getExchRate())));
			rpEntity.setRskPfCovered(StringUtils.isEmpty(beanObj.getPortfoloCovered()) ? "": beanObj.getPortfoloCovered());
			rpEntity.setRskDeducOc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getDeduchunPercent()));
			rpEntity.setRskDeducDc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeduchunPercent(),beanObj.getExchRate())));
			rpEntity.setRskMdPremOc(StringUtils.isEmpty(beanObj.getMdPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdPremium()));
			rpEntity.setRskMdPremDc(StringUtils.isEmpty(beanObj.getMdPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMdPremium(), beanObj.getExchRate())));
			rpEntity.setRskAdjrate(StringUtils.isEmpty(beanObj.getAdjRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getAdjRate()));
			rpEntity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			rpEntity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			rpEntity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			rpEntity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerOffer()));
			rpEntity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			rpEntity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdpremiumourservice()));
			rpEntity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchRate())));
			rpEntity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getLimitPerVesselOC()));
			rpEntity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			rpEntity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			rpEntity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			rpEntity.setEgpniAsOffer(StringUtils.isEmpty(beanObj.getEgnpiOffer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpiOffer()));
			rpEntity.setOurassessment(StringUtils.isEmpty(beanObj.getOurAssessment()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurAssessment()));
			rpEntity.setEgpniAsOfferDc(StringUtils.isEmpty(beanObj.getEgnpiOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEgnpiOffer(), beanObj.getExchRate())));
			rpEntity.setLoginId(beanObj.getLoginId());
			rpEntity.setBranchCode(beanObj.getBranchCode());
//			rpEntity.setRskProposalNumber(beanObj.getProposalNo());
//			rpEntity.setRskEndorsementNo(new BigDecimal(endNo==null?"0":endNo));
			rpRepo.save(rpEntity);
			count=1;
		}
		return count;
	}
	public String getDesginationCountry(final String limitOrigCur, final String ExchRate) {
		double output=0.0;
		try{
			double origCountryVal=0.0;
			if(limitOrigCur!=null){
				if (!("".equalsIgnoreCase(limitOrigCur)) && Double.parseDouble(limitOrigCur) != 0) {
					origCountryVal = Double.parseDouble(limitOrigCur) / Double.parseDouble(ExchRate);
					final DecimalFormat myFormatter = new DecimalFormat("###.##");
					output = Double.parseDouble(myFormatter.format(origCountryVal));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return String.valueOf(output);
	}
	private String getMaxAmednIdPro(final String proposalNo) {
		String result = "";
		try {
			TtrnRiskProposal list =	rpRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			 if (list != null) {
				 result =String.valueOf(list.getRskEndorsementNo());
				} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public boolean updateFirstPageFields(final FirstInsertReq beanObj, String endNo) {
		boolean updateStatus = true;
		int res = 0;
		TtrnRiskProposal entity = rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		try {
			entity.setRskEventLimitOc(StringUtils.isEmpty(beanObj.getEventlimit()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getEventlimit()));
			entity.setRskEventLimitDc(StringUtils.isEmpty(beanObj.getEventlimit()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEventlimit(), beanObj.getExchRate())));
			entity.setRskEventLimitOsOc(StringUtils.isEmpty(beanObj.getEventLimitOurShare()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getEventLimitOurShare()));
			entity.setRskEventLimitOsDc(StringUtils.isEmpty(beanObj.getEventLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEventLimitOurShare(), beanObj.getExchRate())));
			entity.setRskCoverLimitUxlOc(StringUtils.isEmpty(beanObj.getCoverLimitXL()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getCoverLimitXL()));
			entity.setRskCoverLimitUxlDc(StringUtils.isEmpty(beanObj.getCoverLimitXL()) || StringUtils.isEmpty(beanObj.getExchRate()) ?  BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getCoverLimitXL(), beanObj.getExchRate())));
			entity.setRskCoverLimitUxlOsOc(StringUtils.isEmpty(beanObj.getCoverLimitXLOurShare()) ?  BigDecimal.ZERO : new BigDecimal( beanObj.getCoverLimitXLOurShare()));		
			entity.setRskCoverLimitUxlOsDc(StringUtils.isEmpty(beanObj.getCoverLimitXLOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getCoverLimitXLOurShare(), beanObj.getExchRate())));
			entity.setRskDeductableUxlOc(StringUtils.isEmpty(beanObj.getDeductLimitXL()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getDeductLimitXL()));		
			entity.setRskDeductableUxlDc(StringUtils.isEmpty(beanObj.getDeductLimitXL()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeductLimitXL(), beanObj.getExchRate())));	
			entity.setRskDeductableUxlOsOc(StringUtils.isEmpty(beanObj.getDeductLimitXLOurShare()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getDeductLimitXLOurShare()));
			entity.setRskDeductableUxlOsDc(StringUtils.isEmpty(beanObj.getDeductLimitXLOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeductLimitXLOurShare(), beanObj.getExchRate())));
			entity.setRskPml(StringUtils.isEmpty(beanObj.getPml()) ? "" : beanObj.getPml());
	
			if ("Y".equalsIgnoreCase(beanObj.getPml())) {
				entity.setRskPmlPercent(StringUtils.isEmpty(beanObj.getPmlPercent()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getPmlPercent()));
				entity.setRskEgnpiPmlOc(StringUtils.isEmpty(beanObj.getEgnpipml()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpipml()));
				entity.setRskEgnpiPmlDc(StringUtils.isEmpty(beanObj.getEgnpipml()) || StringUtils.isEmpty(beanObj.getExchRate()) ?  BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEgnpipml(), beanObj.getExchRate())));
				entity.setRskEgnpiPmlOsOc(StringUtils.isEmpty(beanObj.getEgnpipmlOurShare()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpipmlOurShare()));
			
			} else {
				entity.setRskPmlPercent(BigDecimal.ZERO );
				entity.setRskEgnpiPmlOc(BigDecimal.ZERO);
				entity.setRskEgnpiPmlDc(BigDecimal.ZERO );
				entity.setRskEgnpiPmlOsOc(BigDecimal.ZERO );
			}
			entity.setRskEgnpiPmlOsDc(StringUtils.isEmpty(beanObj.getEgnpipmlOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEgnpipmlOurShare(), beanObj.getExchRate())));
			entity.setRskPremiumBasis(StringUtils.isEmpty(beanObj.getPremiumbasis()) ? "" : beanObj.getPremiumbasis());
			entity.setRskMinimumRate(StringUtils.isEmpty(beanObj.getMinimumRate()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getMinimumRate()));
			entity.setRskMinimumRate(StringUtils.isEmpty(beanObj.getMaximumRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaximumRate()));
			entity.setRskBurningCostLf(StringUtils.isEmpty(beanObj.getBurningCostLF()) ? "0" : beanObj.getBurningCostLF());
			entity.setRskMinimumPremiumOc(StringUtils.isEmpty(beanObj.getMinPremium()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getMinPremium()));
			entity.setRskMinimumPremiumOc(StringUtils.isEmpty(beanObj.getMinPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMinPremium(), beanObj.getExchRate())));
			entity.setRskMinimumPremiumOsOc(StringUtils.isEmpty(beanObj.getMinPremiumOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMinPremiumOurShare()));
			entity.setRskMinimumPremiumOsOc(StringUtils.isEmpty(beanObj.getMinPremiumOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMinPremiumOurShare(), beanObj.getExchRate())));
			entity.setRskPaymentDueDays(StringUtils.isEmpty(beanObj.getPaymentDuedays()) ? "0" : beanObj.getPaymentDuedays());
			if("TR".equalsIgnoreCase(beanObj.getRetroType())){
				entity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml());
				entity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchRate()));
				}
				else{
					entity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml()) ? "0": beanObj.getFaclimitOrigCurPml());
					entity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCurPml(), beanObj.getExchRate()));
					
					}
			entity.setRskTrtyLmtPmlOsOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0" : beanObj.getLimitOrigCurPmlOS());
			entity.setRskTrtyLmtPmlOsDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) || StringUtils.isEmpty(beanObj.getExchRate()) ? "0" : getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchRate()));
			entity.setRskTrtyLmtSurPmlOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0" : beanObj.getTreatyLimitsurplusOCPml());
			entity.setRskTrtyLmtSurPmlDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) || StringUtils.isEmpty(beanObj.getExchRate()) ? "0" : getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchRate()));
			entity.setRskTrtyLmtSurPmlOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0" : beanObj.getTreatyLimitsurplusOCPmlOS());	
			entity.setRskTrtyLmtSurPmlOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) || StringUtils.isEmpty(beanObj.getExchRate()) ? "0" : getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchRate()));
			entity.setRskTrtyLmtOurassPmlOc(StringUtils.isEmpty(beanObj.getEpipml()) ? "0" : beanObj.getEpipml());	
			entity.setRskTrtyLmtOurassPmlDc(StringUtils.isEmpty(beanObj.getEpipml()) || StringUtils.isEmpty(beanObj.getExchRate()) ? "0" : getDesginationCountry(beanObj.getEpipml(), beanObj.getExchRate()));
			entity.setRskTrtyLmtOurassPmlOsOc(StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0" : beanObj.getEpipmlOS());	
			entity.setRskTrtyLmtourAssPmlOsDc(StringUtils.isEmpty(beanObj.getEpipmlOS()) || StringUtils.isEmpty(beanObj.getExchRate()) ? "0" : getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchRate()));
//			entity.setRskProposalNumber(beanObj.getProposalNo());
//			entity.setRskEndorsementNo(new BigDecimal(endNo));	
			rpRepo.save(entity);
			res = 1;
			if (res > 0) {
				updateStatus = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateStatus;

	}
	public int updateHomePositionMasterAruguments(final FirstInsertReq beanObj, final String pid, final String maxAmdId) throws ParseException {
		int count = 0;
		PositionMaster entity = pmRepo.findByProposalNoAndAmendId(new BigDecimal(beanObj.getProposalNo()),new BigDecimal(maxAmdId));
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		entity.setLayerNo(StringUtils.isEmpty(beanObj.getLayerNo()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLayerNo()));
		entity.setReinsuranceId("");
		entity.setProductId( new BigDecimal(pid));
		entity.setDeptId(beanObj.getDepartId());
		entity.setCedingCompanyId(beanObj.getCedingCo());
		entity.setUwYear(beanObj.getUwYear());
		entity.setUwMonth(sdf.parse(beanObj.getMonth()));
		entity.setAccountDate(sdf.parse(beanObj.getAccDate()));
		entity.setInceptionDate(sdf.parse(beanObj.getIncepDate()));
		entity.setExpiryDate(sdf.parse(beanObj.getExpDate()));
		entity.setProposalStatus(beanObj.getProStatus());		
		if (beanObj.getContNo() != null && !"0".equals(beanObj.getContNo().replaceAll(",", "")) && !"".equals(beanObj.getContNo().replaceAll(",", ""))) {
			entity.setContractStatus("A");
		}else if (beanObj.getProStatus().equalsIgnoreCase("A") || beanObj.getProStatus().equalsIgnoreCase("P")) {
			entity.setContractStatus("P");
		} else if ("R".equalsIgnoreCase(beanObj.getProStatus())) {
			entity.setContractStatus("R");
		} else if("N".equalsIgnoreCase(beanObj.getProStatus())) {
			entity.setContractStatus("N");
		}else{
			entity.setContractStatus("P");
		}
		entity.setBrokerId(beanObj.getBroker());
		entity.setRetroType(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType());
		entity.setLoginId(beanObj.getLoginId());
		entity.setRskDummyContract(StringUtils.isBlank(beanObj.getDummyCon())?"":beanObj.getDummyCon());
		entity.setDataMapContNo(StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal());
//		entity.setProposalNo(new BigDecimal(beanObj.getProposalNo()));
//		entity.setAmendId(new BigDecimal(maxAmdId));
		pmRepo.save(entity);
		count = 1;
		return count;
	}
	public int getFirstPageSecondTableAruguments(final FirstInsertReq beanObj, final String pid, final String args2, final boolean amendId) {
		int count = 0;
		TtrnRiskProposal entity = new TtrnRiskProposal();
		if (amendId) {
			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskEndorsementNo(new BigDecimal(args2));
		} else {
			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskEndorsementNo(BigDecimal.ZERO);
		}
		if("TR".equalsIgnoreCase(beanObj.getRetroType())){
			entity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			entity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			}
			else{
				entity.setRskLimitOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getFaclimitOrigCur()));
				entity.setRskLimitDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchRate())));
				}
		if (pid.equalsIgnoreCase("4")) {
			entity.setRskLayerNo(BigDecimal.ZERO);		
			entity.setRskEpiOfferOc(StringUtils.isEmpty(beanObj.getEpiorigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiorigCur()));		
			entity.setRskEpiOfferDc(StringUtils.isEmpty(beanObj.getEpiorigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchRate())));	
			entity.setRskEpiEstimate(StringUtils.isEmpty(beanObj.getOurEstimate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurEstimate()));
			entity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			entity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			entity.setRskXlcostOc(StringUtils.isEmpty(beanObj.getXlCost()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlCost()));	
			entity.setRskXlcostDc(StringUtils.isEmpty(beanObj.getXlCost()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlCost(), beanObj.getExchRate())));
			entity.setRskCedantRetention(StringUtils.isEmpty(beanObj.getCedReten()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getCedReten()));
			entity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				entity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ? BigDecimal.ZERO: new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				entity.setRskShareSigned(BigDecimal.ZERO);
			}
			entity.setRskCedretType(StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType());
			entity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			entity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			entity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			entity.setRskTrtyLmtPmlOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "" : beanObj.getLimitOurShare());
			entity.setRskTrtyLmtPmlOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ?"" : getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate()));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			entity.setRskEpiOsoeOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerShare()));	
			entity.setRskEpiOsoeDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));	
			entity.setRskXlcostOsOc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlcostOurShare()));	
			entity.setRskXlcostOsDc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchRate())));
			entity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			entity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			entity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			entity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			entity.setRskTreatySurpLimitOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOC()));
			entity.setRskTreatySurpLimitDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchRate())));
			entity.setRskTreatySurpLimitOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ?BigDecimal.ZERO : new BigDecimal( beanObj.getTreatyLimitsurplusOurShare()));
			entity.setRskTreatySurpLimitOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchRate())));
			entity.setSubClass(BigDecimal.ZERO);
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			rpRepo.save(entity);
			count = 1;
			}

		if ("5".equalsIgnoreCase(pid)) {
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
			entity.setRskXlpremOc(StringUtils.isEmpty(beanObj.getXlPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlPremium()));	
			entity.setRskXlpremDc(StringUtils.isEmpty(beanObj.getXlPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlPremium(), beanObj.getExchRate())));
			entity.setRskDeducOc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getDeduchunPercent()));		
			entity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));		
			entity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));		
			entity.setRskMdPremOc(StringUtils.isEmpty(beanObj.getMdPremium()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getMdPremium()));			
			entity.setRskMdPremDc(StringUtils.isEmpty(beanObj.getMdPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getMdPremium(), beanObj.getExchRate())));
			entity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			entity.setRskAdjrate(StringUtils.isEmpty(beanObj.getAdjRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getAdjRate()));
			entity.setRskPfCovered( StringUtils.isEmpty(beanObj.getPortfoloCovered()) ?"" : beanObj.getPortfoloCovered());
			entity.setRskSubjPremiumOc(StringUtils.isEmpty(beanObj.getSubPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getSubPremium()));
			entity.setRskSubjPremiumDc(StringUtils.isEmpty(beanObj.getSubPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getSubPremium(), beanObj.getExchRate())));
			entity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			entity.setRskDeducDc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeduchunPercent(), beanObj.getExchRate())));
			entity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			entity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				entity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				entity.setRskShareSigned(BigDecimal.ZERO);
			}
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerShare()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdpremiumourservice()));			
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchRate())));
			entity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			entity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			entity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			entity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			entity.setEgpniAsOffer(StringUtils.isEmpty(beanObj.getEgnpiOffer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpiOffer()));
			entity.setOurassessment(StringUtils.isEmpty(beanObj.getOurAssessment()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurAssessment()));		
			entity.setEgpniAsOfferDc(StringUtils.isEmpty(beanObj.getEgnpiOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEgnpiOffer(), beanObj.getExchRate())));
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			rpRepo.save(entity);
		}
			count = 1;
			return count;
	}
	private String getRenewalStatus(final FirstInsertReq beanObj) {
		String result="";
		try{
			if(StringUtils.isNotBlank(beanObj.getContNo())){
				CriteriaBuilder cb = em.getCriteriaBuilder(); //gn
				CriteriaQuery<String> query = cb.createQuery(String.class); //based on return value table name(full data)/data type(single)/Tuple(some)
				
				// Find All, like table name
				Root<PositionMaster> pm = query.from(PositionMaster.class);

				// Select
				query.select(pm.get("renewalStatus")); //Alias name except single data or entity

				// MAXAmend ID
				Subquery<Long> amend = query.subquery(Long.class); //based on return value table name/data type
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				// Where
				Predicate n1 = cb.equal(pm.get("proposalNo"), beanObj.getProposalNo());
				Predicate n2 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2);

				// Get Result
				TypedQuery<String> res = em.createQuery(query);
				List<String> list = res.getResultList();
				
				if (list.size()>0) {
					 result = list.get(0);
					} 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public int insertHomePositionMasterAruguments(final FirstInsertReq beanObj, final String pid,	final String args2, final boolean amendId,String renewalStatus) throws ParseException {
		PositionMaster entity = new PositionMaster();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		int count =0;
		if (amendId) {
			entity.setContractNo(new BigDecimal(beanObj.getContNo()));
			entity.setAmendId(new BigDecimal(args2));
			entity.setBaseLayer( beanObj.getBaseLayer());
		} else {
			entity.setContractNo(new BigDecimal(beanObj.getContNo()));
			entity.setAmendId(new BigDecimal(args2));
			entity.setBaseLayer(beanObj.getLayerProposalNo());
		}
		entity.setProposalNo(new BigDecimal(beanObj.getProposalNo()));
		entity.setLayerNo(StringUtils.isEmpty(beanObj.getLayerNo()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLayerNo()));
		entity.setReinsuranceId("");
		entity.setProductId(new BigDecimal(pid));
		entity.setDeptId(beanObj.getDepartId());
		entity.setCedingCompanyId(beanObj.getCedingCo());
		entity.setUwYear(beanObj.getUwYear());
		entity.setUwMonth(sdf.parse(beanObj.getMonth()));
		entity.setAccountDate(sdf.parse(beanObj.getAccDate()));
		entity.setInceptionDate(sdf.parse(beanObj.getIncepDate()));
		entity.setExpiryDate(sdf.parse(beanObj.getExpDate()));
		entity.setProposalStatus(beanObj.getProStatus());
		if (amendId) {
			entity.setContractStatus("A");
			entity.setRenewalStatus(renewalStatus);
		} else {
			entity.setContractStatus("P");
			entity.setRenewalStatus("1");
		}
		entity.setLoginId(StringUtils.isNotBlank(beanObj.getBaseLoginID())?beanObj.getBaseLoginID():beanObj.getLoginId());
		entity.setOldContractno(StringUtils.isEmpty(beanObj.getRenewalcontractno()) ? "": beanObj.getRenewalcontractno());
		entity.setBrokerId(beanObj.getBroker());
		entity.setBranchCode(beanObj.getBranchCode());
		entity.setRetroType(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType());
		entity.setUpdateLoginId(beanObj.getLoginId());
		entity.setEndtStatus("");
		entity.setRskDummyContract(StringUtils.isBlank(beanObj.getDummyCon())?"":beanObj.getDummyCon());
		entity.setDataMapContNo(StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal());
		entity.setEntryDate(new Date());
		pmRepo.save(entity);
		count = 1;
		return count;
	}
	public int getFirstPageSecondTableInsertAruguments(final FirstInsertReq beanObj, final String pid,final String args2, final boolean amendId) {
		int count = 0;
		TtrnRiskProposal entity = new TtrnRiskProposal();
		if (amendId) {
			entity.setRskEndorsementNo(new BigDecimal(args2));
		} else {
			entity.setRskEndorsementNo(BigDecimal.ZERO);
		}
		entity.setRskProposalNumber(beanObj.getProposalNo());
		if("TR".equalsIgnoreCase(beanObj.getRetroType())){
			entity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			entity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			}
			else{
				entity.setRskLimitOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getFaclimitOrigCur()));
				entity.setRskLimitDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchRate())));
				}
		if (pid.equalsIgnoreCase("4")) {
			entity.setRskLayerNo(BigDecimal.ZERO);		
			entity.setRskEpiOfferOc(StringUtils.isEmpty(beanObj.getEpiorigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiorigCur()));		
			entity.setRskEpiOfferDc(StringUtils.isEmpty(beanObj.getEpiorigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchRate())));	
			entity.setRskEpiEstimate(StringUtils.isEmpty(beanObj.getOurEstimate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurEstimate()));
			entity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			entity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			entity.setRskXlcostOc(StringUtils.isEmpty(beanObj.getXlCost()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlCost()));	
			entity.setRskXlcostDc(StringUtils.isEmpty(beanObj.getXlCost()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlCost(), beanObj.getExchRate())));
			entity.setRskCedantRetention(StringUtils.isEmpty(beanObj.getCedReten()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getCedReten()));
			entity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				entity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ? BigDecimal.ZERO: new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				entity.setRskShareSigned(BigDecimal.ZERO);
			}
			entity.setRskCedretType(StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType());
			entity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			entity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			entity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			entity.setRskTrtyLmtPmlOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "" : beanObj.getLimitOurShare());
			entity.setRskTrtyLmtPmlOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ?"" : getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate()));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			entity.setRskEpiOsoeOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerShare()));	
			entity.setRskEpiOsoeDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));	
			entity.setRskXlcostOsOc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlcostOurShare()));	
			entity.setRskXlcostOsDc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchRate())));
			entity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			entity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			entity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			entity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			entity.setRskTreatySurpLimitOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOC()));
			entity.setRskTreatySurpLimitDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchRate())));
			entity.setRskTreatySurpLimitOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ?BigDecimal.ZERO : new BigDecimal( beanObj.getTreatyLimitsurplusOurShare()));
			entity.setRskTreatySurpLimitOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchRate())));
			entity.setSubClass(BigDecimal.ZERO);
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskEntryDate(new Date());		
			entity.setSysDate(new Date());		
			rpRepo.save(entity);
			count = 1;
			}

		if ("5".equalsIgnoreCase(pid)) {
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
			entity.setRskXlpremOc(StringUtils.isEmpty(beanObj.getXlPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlPremium()));	
			entity.setRskXlpremDc(StringUtils.isEmpty(beanObj.getXlPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlPremium(), beanObj.getExchRate())));
			entity.setRskDeducOc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getDeduchunPercent()));		
			entity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));		
			entity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));		
			entity.setRskMdPremOc(StringUtils.isEmpty(beanObj.getMdPremium()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getMdPremium()));			
			entity.setRskMdPremDc(StringUtils.isEmpty(beanObj.getMdPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getMdPremium(), beanObj.getExchRate())));
			entity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			entity.setRskAdjrate(StringUtils.isEmpty(beanObj.getAdjRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getAdjRate()));
			entity.setRskPfCovered( StringUtils.isEmpty(beanObj.getPortfoloCovered()) ?"" : beanObj.getPortfoloCovered());
			entity.setRskSubjPremiumOc(StringUtils.isEmpty(beanObj.getSubPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getSubPremium()));
			entity.setRskSubjPremiumDc(StringUtils.isEmpty(beanObj.getSubPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getSubPremium(), beanObj.getExchRate())));
			entity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			entity.setRskDeducDc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeduchunPercent(), beanObj.getExchRate())));
			entity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			entity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				entity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				entity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				entity.setRskShareSigned(BigDecimal.ZERO);
			}
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerShare()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdpremiumourservice()));			
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchRate())));
			entity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			entity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			entity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			entity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			entity.setEgpniAsOffer(StringUtils.isEmpty(beanObj.getEgnpiOffer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpiOffer()));
			entity.setOurassessment(StringUtils.isEmpty(beanObj.getOurAssessment()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurAssessment()));		
			entity.setEgpniAsOfferDc(StringUtils.isEmpty(beanObj.getEgnpiOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEgnpiOffer(), beanObj.getExchRate())));
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskEntryDate(new Date());		
			entity.setSysDate(new Date());	
			rpRepo.save(entity);
			count = 1;
		}
			return count;
	}
	public List<Map<String, Object>> getValidation(String incepDate, String renewalContNo)  {
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		 Map<String,Object>  values = new HashMap<String,Object>();
		try{ 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dbf = new SimpleDateFormat("yyyy-MM-dd"); //DB format
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			// like table name
			Root<PositionMaster> pm = query.from(PositionMaster.class);

			// Select
			query.multiselect(pm.get("uwYear").alias("uwYear"),pm.get("expiryDate").alias("expiryDate")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("renewalStatus"), pms.get("renewalStatus"));
			amend.where(a1,a2);

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(pm.get("uwYear")));

			// Where
			Predicate n1 = cb.equal(pm.get("contractNo"), renewalContNo);
			Predicate n2 = cb.equal(pm.get("renewalStatus"), "0");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();

			for(Tuple data :  list ) {
			values.put("UWYear" , data.get("uwYear"));
			values.put("expiryDate" , dbf.format(data.get("expiryDate")));
			LocalDate d1 = LocalDate.parse(dbf.format( sdf.parse( incepDate)), DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate d2 = LocalDate.parse(  dbf.format(data.get("expiryDate")), DateTimeFormatter.ISO_LOCAL_DATE);
			Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
			long diffDays = diff.toDays();	
			values.put("Diff" , diffDays);
			response.add(values);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req) {
		ShowSecondpageEditItemsRes response = new ShowSecondpageEditItemsRes();
		ShowSecondpageEditItemsRes1 beanObj =new ShowSecondpageEditItemsRes1();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		boolean savFlg = false;
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		try{
			if ("4".equalsIgnoreCase(req.getProductId())) {
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnRiskProposal> rp = query.from(TtrnRiskProposal.class);
				Root<TtrnRiskCommission> comm = query.from(TtrnRiskCommission.class);

				// Select
				query.multiselect(rp.alias("rp"),comm.alias("comm")); 

				// maxEndRP 
				Subquery<Long> endRP = query.subquery(Long.class); 
				Root<TtrnRiskProposal> rps = endRP.from(TtrnRiskProposal.class);
				endRP.select(cb.max(rps.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rps.get("rskProposalNumber"),req.getProposalNo());
				endRP.where(a1);
				
				// maxEndComm
				Subquery<Long> endComm = query.subquery(Long.class); 
				Root<TtrnRiskCommission> comms = endComm.from(TtrnRiskCommission.class);
				endComm.select(cb.max(comms.get("rskEndorsementNo")));
				Predicate b1 = cb.equal( comms.get("rskProposalNumber"),req.getProposalNo());
				endComm.where(b1);

				// Where
				Predicate n1 = cb.equal(rp.get("rskProposalNumber"), req.getProposalNo());
				Predicate n2 = cb.equal(rp.get("rskProposalNumber"), comm.get("rskProposalNumber"));
				Predicate n3 = cb.equal(rp.get("rskEndorsementNo"), endRP);
				Predicate n4 = cb.equal(comm.get("rskEndorsementNo"), endComm);
				query.where(n1,n2,n3,n4);
				
				// Get Result
				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> res = result.getResultList();
				
				TtrnRiskProposal data1 = (TtrnRiskProposal) res.get(0).get("rp");
				TtrnRiskCommission data2 = (TtrnRiskCommission) res.get(0).get("comm");

				if(res!=null && res.size()>0) {
				if (data1 != null &&  data2 != null){
					if (data1.getRskLimitOsOc() != null) {
						beanObj.setLimitOurShare(data1.getRskLimitOsOc().toString().equalsIgnoreCase("0")?"0" : data1.getRskLimitOsOc().toString());
						beanObj.setLimitOSViewOC(fm.formatter(data1.getRskLimitOsOc().toString().equalsIgnoreCase("0")?"0" : data1.getRskLimitOsOc().toString()));
					}
					if (data1.getRskEpiOsofOc() != null) { 
						beanObj.setEpiAsPerOffer(data1.getRskEpiOsofOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofOc().toString());
						beanObj.setEpiOSViewOC(fm.formatter(data1.getRskEpiOsofOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofOc().toString()));
					}
					if (data1.getRskEpiOsoeOc() != null) { 
						beanObj.setEpiAsPerShare(data1.getRskEpiOsoeOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsoeOc().toString());
						beanObj.setEpiOSOEViewOC(fm.formatter(data1.getRskEpiOsoeOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsoeOc().toString()));
					}
					if (data1.getRskXlcostOsOc() != null) { 
						beanObj.setXlcostOurShare(data1.getRskXlcostOsOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskXlcostOsOc().toString());
						beanObj.setXlCostViewOC(fm.formatter(data1.getRskXlcostOsOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskXlcostOsOc().toString()));
					}
					if (data1.getRskLimitOsDc() != null) {
						beanObj.setLimitOSViewDC(fm.formatter(data1.getRskLimitOsDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskLimitOsDc().toString()));
					}
					if (data1.getRskEpiOsofDc() != null) {
						beanObj.setEpiOSViewDC(fm.formatter(data1.getRskEpiOsofDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofDc().toString()));
					}
					if (data1.getRskEpiOsoeDc() != null) { 
						beanObj.setEpiOSOEViewDC(fm.formatter(data1.getRskEpiOsoeDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsoeDc().toString()));
					}
					if (data1.getRskXlcostOsDc() != null) { 
						beanObj.setXlCostViewDC(fm.formatter(data1.getRskXlcostOsDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskXlcostOsDc().toString()));
					}
					if (data2.getRskCommQuotashare() != null) { 
						beanObj.setCommissionQS(data2.getRskCommQuotashare().toString().equalsIgnoreCase("0") ? "0" : data2.getRskCommQuotashare().toString());
					}
					if (data2.getRskCommSurplus() != null) {
						beanObj.setCommissionsurp(data2.getRskCommSurplus().toString().equalsIgnoreCase("0") ? "0" : data2.getRskCommSurplus().toString());
					}
					if (data2.getRskOverriderPerc() != null) { 
						beanObj.setOverRidder(data2.getRskOverriderPerc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskOverriderPerc().toString());
					}
					if (data2.getRskBrokerage() != null) { 
						beanObj.setBrokerage(data2.getRskBrokerage().toString().equalsIgnoreCase("0") ? "0" : data2.getRskBrokerage().toString());
					}
					if (data2.getRskTax() != null) {
						beanObj.setTax(data2.getRskTax().toString().equalsIgnoreCase("0") ? "0"	: data2.getRskTax().toString());
					}
					if (data2.getRskAcquistionCostOc() != null) {
						beanObj.setAcquisitionCost(data2.getRskAcquistionCostOc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskAcquistionCostOc().toString());
					}
					if (data2.getRskProfitComm() != null) {
						beanObj.setShareProfitCommission(data2.getRskProfitComm().toString().equalsIgnoreCase("0") ? "0" : data2.getRskProfitComm().toString());
					}
					if (data2.getRskManagementExpenses() != null) {
						beanObj.setManagementExpenses(data2.getRskManagementExpenses().toString().equalsIgnoreCase("0") ? "0" : data2.getRskManagementExpenses().toString());
					}
					if (data2.getRskLossCarryforward() != null) {
						beanObj.setLossCF(data2.getRskLossCarryforward().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLossCarryforward().toString());
					}
					if (data2.getRskPremiumReserve() != null) {
						beanObj.setPremiumReserve(data2.getRskPremiumReserve().toString().equalsIgnoreCase("0") ? "0" : data2.getRskPremiumReserve().toString());
					}
					if (data2.getRskLossReserve() != null) {
						beanObj.setLossreserve(data2.getRskLossReserve().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLossReserve().toString());
					}
					if (data2.getRskInterest() != null) {
						beanObj.setInterest(data2.getRskInterest().toString().equalsIgnoreCase("0") ? "0" : data2.getRskInterest().toString());
					}
					if (data2.getRskCashlossLmtOc() != null) {
						beanObj.setCashLossLimit(data2.getRskCashlossLmtOc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskCashlossLmtOc().toString());
					}
					if (data2.getRskPfInoutPrem() != null) {
						beanObj.setPortfolioinoutPremium(data2.getRskPfInoutPrem().toString().equalsIgnoreCase("0") ? "0" : data2.getRskPfInoutPrem().toString());
					}
					if (data2.getRskPfInoutLoss() != null) {
						beanObj.setPortfolioinoutLoss(data2.getRskPfInoutLoss().toString().equalsIgnoreCase("0") ? "0" : data2.getRskPfInoutLoss().toString());
					}
					if (data2.getRskLossadvice() != null) {
						beanObj.setLossAdvise(data2.getRskLossadvice().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLossadvice().toString());
					}
					if (data2.getRskLeadUw() != null) {
						beanObj.setLeaderUnderwriter(data2.getRskLeadUw().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLeadUw().toString());
					}
					if (data2.getRskLeadUwShare() != null) {
						beanObj.setLeaderUnderwritershare(data2.getRskLeadUwShare().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLeadUwShare().toString());
					}
					
					beanObj.setAccounts(data2.getRskAccounts()==null?"":data2.getRskAccounts().toString());
					beanObj.setCrestaStatus(data2.getRskCreastaStatus() == null ? "" : data2.getRskCreastaStatus().toString());
					beanObj.setEventlimit(data2.getRskEventLimitOc() == null ? "0" : data2.getRskEventLimitOc().toString());
					beanObj.setAggregateLimit(data2.getRskAggregateLimitOc() == null ? "0" : 	data2.getRskAggregateLimitOc().toString());
					beanObj.setOccurrentLimit(data2.getRskOccurrentLimitOc() == null ? "0" : data2.getRskOccurrentLimitOc().toString());
					beanObj.setExclusion(data2.getRskExclusion()==null?"":data2.getRskExclusion().toString());
					beanObj.setRemarks(data2.getRskRemarks()==null?"":data2.getRskRemarks().toString());
					beanObj.setUnderwriterRecommendations(data2.getRskUwRecomm()==null?"":	data2.getRskUwRecomm().toString());
					beanObj.setGmsApproval(data2.getRskGmApproval()==null?"":data2.getRskGmApproval().toString());
					beanObj.setSlideScaleCommission(data2.getRskSladscaleComm() == null ? "" : data2.getRskSladscaleComm().toString());
					beanObj.setLossParticipants(data2.getRskLossPartCarridor() == null ? "" : data2.getRskLossPartCarridor().toString());
					beanObj.setCommissionSubClass(data2.getRskCombinSubClass() == null ? "" : data2.getRskCombinSubClass().toString());
					beanObj.setLeaderUnderwritercountry(data2.getRskLeadUnderwriterCountry() == null ? "" : data2.getRskLeadUnderwriterCountry().toString());
					beanObj.setOrginalacqcost(data2.getRskIncludeAcqCost() == null ? "" : data2.getRskIncludeAcqCost().toString());
					beanObj.setOuracqCost(data2.getRskOurAssAcqCost() == null ? "" : data2.getRskOurAssAcqCost().toString());
					beanObj.setOurassessmentorginalacqcost(data2.getRskOurAcqOurShareOc() == null ? "" : data2.getRskOurAcqOurShareOc().toString());
					beanObj.setProfitCommission(data2.getRskProNotes() == null ? "" : data2.getRskProNotes().toString());
					beanObj.setLosscommissionSubClass(data2.getRskLossCombinSubClass() == null ? "" : data2.getRskLossCombinSubClass().toString());
					beanObj.setSlidecommissionSubClass(data2.getRskSlideCombinSubClass() == null ? "" : data2.getRskSlideCombinSubClass().toString());
					beanObj.setCrestacommissionSubClass(data2.getRskCrestaCombinSubClass() == null ? "" : data2.getRskCrestaCombinSubClass().toString());
					beanObj.setManagementExpenses(data2.getRskProManagementExp() == null ? "" : data2.getRskProManagementExp().toString());
					beanObj.setCommissionType(data2.getRskProCommType() == null ? "" : data2.getRskProCommType().toString());
					beanObj.setProfitCommissionPer(data2.getRskProCommPer() == null ? "" : data2.getRskProCommPer().toString());
					beanObj.setSetup(data2.getRskProSetUp() == null ? "" : data2.getRskProSetUp().toString());
					beanObj.setSuperProfitCommission(data2.getRskProSubPfoCom() == null ? "" : data2.getRskProSubPfoCom().toString());
					beanObj.setLossCarried(data2.getRskProLossCaryType() == null ? "" : data2.getRskProLossCaryType().toString());
					beanObj.setLossyear(data2.getRskProLossCaryYear() == null ? "" : data2.getRskProLossCaryYear().toString());
					beanObj.setProfitCarried(data2.getRskProProfitCaryType() == null ? "" : data2.getRskProProfitCaryType().toString());
					beanObj.setProfitCarriedForYear(data2.getRskProProfitCaryYear() == null ? "" : data2.getRskProProfitCaryYear().toString());
					beanObj.setFistpc(data2.getRskProFirstPfoCom() == null ? "" : data2.getRskProFirstPfoCom().toString());
					beanObj.setProfitMont(data2.getRskProFirstPfoComPrd() == null ? "" : data2.getRskProFirstPfoComPrd().toString());
					beanObj.setSubProfitMonth(data2.getRskProSubPfoComPrd() == null ? "" : data2.getRskProSubPfoComPrd().toString());
					beanObj.setSubpc(data2.getRskProSubPfoCom() == null ? "" : data2.getRskProSubPfoCom().toString());
					beanObj.setSubSeqCalculation(data2.getRskProSubSeqCal() == null ? "" : data2.getRskProSubSeqCal().toString());
					beanObj.setCommissionQSYN(data2.getRskCommissionQsYn() == null ? "" : data2.getRskCommissionQsYn().toString());
					beanObj.setCommissionsurpYN(data2.getRskCommissionSurYn() == null ? "" : data2.getRskCommissionSurYn().toString());
					beanObj.setOverRidderYN(data2.getRskOverrideYn() == null ? "" : data2.getRskOverrideYn().toString());
					beanObj.setBrokerageYN(data2.getRskBrokarageYn() == null ? "" : data2.getRskBrokarageYn().toString());
					beanObj.setTaxYN(data2.getRskTaxYn() == null ? "" : data2.getRskTaxYn().toString());
					beanObj.setOthercostYN(data2.getRskOtherCostYn() == null ? "" : data2.getRskOtherCostYn().toString());
					beanObj.setCeedODIYN(data2.getRskCeedOdiYn() == null ? "" : data2.getRskCeedOdiYn().toString());
					beanObj.setLocRate(data2.getRskRate()==null?"":data2.getRskRate().toString());
					beanObj.setRetroCommissionType(data2.getRskCommissionType()==null?"":data2.getRskCommissionType().toString());
					if (data2.getRskOtherCost() != null) {
						beanObj.setOthercost(data2.getRskOtherCost().toString().equalsIgnoreCase("0") ? "0" : data2.getRskOtherCost().toString());
					}else{
						beanObj.setOthercost("0");
					}
					if ("2".equalsIgnoreCase(req.getProductId()))
						beanObj.setAcqCostPer((Double.parseDouble(beanObj.getCommissionQS())+Double.parseDouble(beanObj.getCommissionsurp())+Double.parseDouble(beanObj.getOverRidder())+Double.parseDouble(beanObj.getBrokerage())+Double.parseDouble(beanObj.getTax())+Double.parseDouble(beanObj.getOthercost()))+"");
					savFlg = true;
				} }
				CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
				Root<TtrnRiskProposal> rp1 = query1.from(TtrnRiskProposal.class);

				// Select
				query1.multiselect(rp1.get("rskPremiumQuotaShare").alias("rskPremiumQuotaShare"), rp1.get("rskPremiumSurpuls").alias("rskPremiumSurpuls"));
				
				// maxEndRP 
				Subquery<Long> endRP1 = query1.subquery(Long.class); 
				Root<TtrnRiskProposal> rps1 = endRP1.from(TtrnRiskProposal.class);
				endRP1.select(cb.max(rps1.get("rskEndorsementNo")));
				Predicate k1 = cb.equal( rps1.get("rskProposalNumber"),rp1.get("rskProposalNumber"));
				Predicate k2 = cb.isNotNull(rps1.get("rskPremiumQuotaShare"));
				endRP1.where(k1,k2);
		
				// Where
				Predicate r1 = cb.equal(rp1.get("rskProposalNumber"), req.getProposalNo());
				Predicate r2 = cb.isNotNull(rp1.get("rskPremiumQuotaShare"));
				Predicate r3 = cb.equal(rp1.get("rskEndorsementNo"), endRP1);
				query1.where(r1,r2,r3);
				
				// Get Result
				TypedQuery<Tuple> result1 = em.createQuery(query1);
				List<Tuple> res1 = result1.getResultList();
				if(res1!=null && res1.size()>0)
					beanObj.setPremiumQuotaShare(res1.get(0).get("rskPremiumQuotaShare").toString());
					beanObj.setPremiumSurplus(res1.get(0).get("rskPremiumSurpuls").toString());

			}
			if ("5".equalsIgnoreCase(req.getProductId())) {
				CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
				Root<TtrnRiskProposal> rp = query1.from(TtrnRiskProposal.class);
				Root<TtrnRiskCommission> comm = query1.from(TtrnRiskCommission.class);

				// Select
				query1.multiselect(rp.alias("rp"),comm.alias("comm")); 

				// maxEndRP 
				Subquery<Long> endRP = query1.subquery(Long.class); 
				Root<TtrnRiskProposal> rps = endRP.from(TtrnRiskProposal.class);
				endRP.select(cb.max(rps.get("rskEndorsementNo")));
				Predicate x1 = cb.equal( rps.get("rskProposalNumber"),req.getProposalNo());
				endRP.where(x1);
				
				// maxEndComm
				Subquery<Long> endComm = query1.subquery(Long.class); 
				Root<TtrnRiskCommission> comms = endComm.from(TtrnRiskCommission.class);
				endComm.select(cb.max(comms.get("rskEndorsementNo")));
				Predicate b1 = cb.equal( comms.get("rskProposalNumber"),req.getProposalNo());
				endComm.where(b1);

				// Where
				Predicate v1 = cb.equal(rp.get("rskProposalNumber"), req.getProposalNo());
				Predicate v2 = cb.equal(rp.get("rskProposalNumber"), comm.get("rskProposalNumber"));
				Predicate v3 = cb.equal(rp.get("rskEndorsementNo"), endRP);
				Predicate v4 = cb.equal(comm.get("rskEndorsementNo"), endComm);
				Predicate v5 = cb.equal(comm.get("rskLayerNo"), req.getLayerNo());
				query1.where(v1,v2,v3,v4,v5);
				
				// Get Result
				TypedQuery<Tuple> result = em.createQuery(query1);
				List<Tuple> resList = result.getResultList();
				
				TtrnRiskProposal data1 = (TtrnRiskProposal) resList.get(0).get("rp");
				TtrnRiskCommission data2 = (TtrnRiskCommission) resList.get(0).get("comm");
				
				if(resList!=null && resList.size()>0)
				if(data1!=null && data2 != null){
					for (int i = 0; i < resList.size(); i++) {
						if (data1.getRskLimitOsOc() != null) {
							beanObj.setLimitOurShare(data1.getRskLimitOsOc().toString().equalsIgnoreCase("0")?"0" : data1.getRskLimitOsOc().toString());
							beanObj.setLimitOSViewOC(fm.formatter(data1.getRskLimitOsOc().toString().equalsIgnoreCase("0")?"0" : data1.getRskLimitOsOc().toString()));
						}
						if (data1.getRskLimitOsDc() != null) {
							beanObj.setLimitOSViewDC(fm.formatter(data1.getRskLimitOsDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskLimitOsDc().toString()));
						}
						if (data1.getRskEpiOsofOc() != null) { 
							beanObj.setEpiAsPerOffer(data1.getRskEpiOsofOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofOc().toString());
							beanObj.setEpiOSViewOC(fm.formatter(data1.getRskEpiOsofOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofOc().toString()));
						}
						if (data1.getRskEpiOsofDc() != null) { 
							beanObj.setEpiOSViewDC(fm.formatter(data1.getRskEpiOsofDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskEpiOsofDc().toString()));
						}
						if (data2.getRskBrokerage() != null) {
							beanObj.setBrokerage(data2.getRskBrokerage().toString().equalsIgnoreCase("0") ? "0" : data2.getRskBrokerage().toString());
						}
						if (data2.getRskTax() != null) {
							beanObj.setTax(data2.getRskTax().toString().equalsIgnoreCase("0") ? "0"	: data2.getRskTax().toString());
						}
						if (data2.getRskAcquistionCostOc() != null) { 
							beanObj.setAcquisitionCost(data2.getRskAcquistionCostOc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskAcquistionCostOc().toString());
						}
						if (data2.getRskProfitComm() != null) {
							beanObj.setShareProfitCommission(data2.getRskProfitComm().toString().equalsIgnoreCase("0") ? "0" : data2.getRskProfitComm().toString());
						}
						if (data1.getRskMdPremOsOc() != null) {
							beanObj.setMdpremiumourservice(data1.getRskMdPremOsOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskMdPremOsOc().toString());
							beanObj.setMandDpreViewOC(fm.formatter(data1.getRskMdPremOsOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskMdPremOsOc().toString()));
						}
						if (data1.getRskMdPremOsDc() != null) {
							beanObj.setMandDpreViewDC(fm.formatter(data1.getRskMdPremOsDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskMdPremOsDc().toString()));
						}
						if (data2.getRskAggregateLiabOc() != null) {
							beanObj.setAnualAggregateLiability(data2.getRskAggregateLiabOc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskAggregateLiabOc().toString());
						}
						if (data2.getRskReinstateNo() != null) {
							beanObj.setReinstNo(data2.getRskReinstateNo().toString().equalsIgnoreCase("0") ? "" : data2.getRskReinstateNo().toString());
						}
						if (data2.getRskReinstateAddlPremOc() != null) {
							beanObj.setReinstAdditionalPremium(data2.getRskReinstateAddlPremOc().toString().equalsIgnoreCase("0") ? "0" : data2.getRskReinstateAddlPremOc().toString());
						}
						if (data2.getRskLeadUw() != null) {
							beanObj.setLeaderUnderwriter(data2.getRskLeadUw().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLeadUw().toString());
						}
						if (data2.getRskLeadUwShare() != null) {
							beanObj.setLeaderUnderwritershare(data2.getRskLeadUwShare().toString().equalsIgnoreCase("0") ? "0" : data2.getRskLeadUwShare().toString());
						}
						if (data2.getRskAccounts() != null) {
							beanObj.setAccounts(data2.getRskAccounts().toString().equalsIgnoreCase("0") ? "0" : data2.getRskAccounts().toString());
						}
						if (data2.getRskExclusion() != null) {
							beanObj.setExclusion(data2.getRskExclusion().toString().equalsIgnoreCase("0") ? "0" : data2.getRskExclusion().toString());
						}
					
						beanObj.setRemarks(data2.getRskRemarks()==null?"":data2.getRskRemarks().toString());
						beanObj.setUnderwriterRecommendations(	data2.getRskUwRecomm()==null?"":	data2.getRskUwRecomm().toString());
						beanObj.setGmsApproval(data2.getRskGmApproval()==null?"":data2.getRskGmApproval().toString());
						if (data2.getRskOtherCost() != null) {
							beanObj.setOthercost(data2.getRskOtherCost().toString().equalsIgnoreCase("0") ? "0" : data2.getRskOtherCost().toString());
						}
						if (data2.getRskReinstateAddlPremPct() != null) {
							beanObj.setReinstAditionalPremiumpercent(data2.getRskReinstateAddlPremPct().toString().equalsIgnoreCase("0") ? "0" : data2.getRskReinstateAddlPremPct().toString());
						}
						if (data2.getRskBurningCostPct() != null) {
							beanObj.setBurningCost(data2.getRskBurningCostPct().toString().equalsIgnoreCase("0") ? "0" : data2.getRskBurningCostPct().toString());
						}
						savFlg = true;
					}
				}
				CriteriaQuery<Date> query = cb.createQuery(Date.class); 
				
				Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);

				// Select
				query.select(pm.get("installmentDate")); 

				// maxEndNo
				Subquery<Long> maxEndNo = query.subquery(Long.class); 
				Root<TtrnMndInstallments> pms = maxEndNo.from(TtrnMndInstallments.class);
				maxEndNo.select(cb.max(pms.get("endorsementNo")));
				Predicate a1 = cb.equal( pms.get("proposalNo"), req.getProposalNo());
				Predicate a2 = cb.equal( pms.get("layerNo"), req.getLayerNo());
				Predicate a3 = cb.isNotNull(pms.get("endorsementNo"));
				maxEndNo.where(a1,a2,a3);

				// Order By
				List<Order> orderList = new ArrayList<>();
				orderList.add(cb.asc(pm.get("installmentNo")));

				// Where
				Predicate n1 = cb.equal(pm.get("proposalNo"), req.getProposalNo());
				Predicate n2 = cb.equal(pm.get("layerNo"), req.getLayerNo());
				Predicate n3 = cb.equal(pm.get("endorsementNo"), maxEndNo);
				Predicate n4 = cb.isNotNull(pm.get("endorsementNo"));
				query.where(n1,n2,n3,n4).orderBy(orderList);

				// Get Result
				TypedQuery<Date> res = em.createQuery(query);
				List<Date> list = res.getResultList();
				
				List<String> instalmentDateList = new ArrayList<String>();
				for(Date date: list) {
					String today = sdf.format(date);
					instalmentDateList.add(today);
				}
				beanObj.setInstalmentDateList(instalmentDateList);
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
	
	@Override
	@Transactional
	public CommonResponse insertRemarkDetails(InsertRemarkDetailsReq req) {
		CommonResponse response = new CommonResponse();
		TtrnRiskRemarks entity = new TtrnRiskRemarks();
		try {
			//Delete data
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
			Predicate n1 = cb.equal(rp.get("proposalNo"), req.getProposalNo());
			Predicate n2 = cb.equal(rp.get("layerNo"), "0");
			Predicate n3 = cb.equal(rp.get("amendId"), amend);
			delete.where(n1,n2,n3);
			em.createQuery(delete).executeUpdate();
			
			List<TtrnRiskRemarks>  res= rrRepo.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(req.getProposalNo()));
			int amen = 0;
			if(res.size()>0) {
			 amen = 	res.get(0).getAmendId()==null?0:Integer.valueOf(res.get(0).getAmendId())+1;
			}
			for(int i=0;i<req.getRemarksReq().size();i++){
				RemarksReq req1 = req.getRemarksReq().get(i);
				entity.setProposalNo(new BigDecimal(req.getProposalNo()));
				entity.setContractNo(new BigDecimal(req.getContractNo()));
				entity.setLayerNo(BigDecimal.ZERO);
				entity.setDeptId(req.getDepartmentId());
				entity.setProductId(req.getProductId());
				entity.setAmendId(String.valueOf(amen));		
				entity.setRskSNo(String.valueOf(i+1));
				entity.setRskDescription(req1.getDescription());
				entity.setRskRemark1(req1.getRemark1());
				entity.setRskRemark2(req1.getRemark2());
				entity.setLoginId(req.getLoginId());
				entity.setBranchCode(req.getBranchCode());
				rrRepo.save(entity);
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
	@Override
	public CommonSaveRes getShortname(String branchCode) {
		CommonSaveRes response = new CommonSaveRes();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			
			Root<TmasBranchMaster> pm = query.from(TmasBranchMaster.class);

			// GET_SHORT_NAME
			Subquery<String> name = query.subquery(String.class); 
			Root<CurrencyMaster> cm = name.from(CurrencyMaster.class);
			name.select(cm.get("shortName"));
			// MAXAmend ID
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<CurrencyMaster> cms = maxAmend.from(CurrencyMaster.class);
			maxAmend.select(cb.max(cms.get("amendId")));
			Predicate b1 = cb.equal( cms.get("currencyId"), cm.get("currencyId"));
			Predicate b2 = cb.equal( cms.get("branchCode"), cm.get("branchCode"));
			maxAmend.where(b1,b2);
			
			Predicate a1 = cb.equal( cm.get("currencyId"), pm.get("baseCurrencyId"));
			Predicate a2 = cb.equal( cm.get("branchCode"), pm.get("branchCode"));
			Predicate a3 = cb.equal( cm.get("amendId"), maxAmend);
			name.where(a1,a2,a3);
			
			// Select
			query.select(name); 

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("status"), "Y");
			query.where(n1,n2);
			
			// Get Result
			TypedQuery<String> res = em.createQuery(query);
			List<String> list = res.getResultList();
			String shortName = "";
			if(list.size()>0) {
				shortName = list.get(0);
			}
		response.setResponse(shortName);	
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
	public CommonSaveRes checkAvialability(String proposalNo, String productId) {
		CommonSaveRes response = new CommonSaveRes();
		boolean saveFlag = false;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class); 
			
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);

			// Select
			query.select(rd.get("rskProductid")); 

			// maxEnd
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rds = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			end.where(a1);

			// Where
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
			query.where(n1,n2);

			// Get Result
			TypedQuery<BigDecimal> res = em.createQuery(query);
			List<BigDecimal> list = res.getResultList();
			BigDecimal pid = BigDecimal.ZERO;
			if(list.size()>0) {
				pid = res.getResultList().get(0);
			}
			if (productId.equalsIgnoreCase(String.valueOf(pid))) {
				saveFlag = true;	
			} else {
				saveFlag = false;
			}
			String result = "SaveFlag: " + String.valueOf(saveFlag);
			response.setResponse(result);	
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
		ViewRiskDetailsRes1 beanObj = new ViewRiskDetailsRes1();
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
			String selectQry = "risk.select.getCommonData";
			List<Map<String, Object>> res = queryImpl.selectList(selectQry,args);
			Map<String, Object> resMap = null;
			if(res!=null && res.size()>0)
				resMap = (Map<String, Object>)res.get(0);
			if (resMap != null) {
				beanObj.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				beanObj.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
				beanObj.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				beanObj.setProposalType(resMap.get("PROPOSAL_TYPE")==null?"":resMap.get("PROPOSAL_TYPE").toString());
				beanObj.setAccountingPeriod(resMap.get("ACCOUNTING_PERIOD")==null?"":resMap.get("ACCOUNTING_PERIOD").toString());
				beanObj.setReceiptofPayment(resMap.get("RSK_RECEIPT_PAYEMENT")==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString());
				beanObj.setReceiptofStatements(resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString());
				beanObj.setProfitCenter(resMap.get("TMAS_PFC_NAME")==null?"":resMap.get("TMAS_PFC_NAME").toString());
				beanObj.setSubProfitCenter(resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString());
				if(!"ALL".equalsIgnoreCase(beanObj.getSubProfitCenter())){
				beanObj.setSubProfitCenter(resMap.get("TMAS_SPFC_NAME")==null?"":resMap.get("TMAS_SPFC_NAME").toString().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
				}
				beanObj.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				beanObj.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				beanObj.setTreatyNametype(resMap.get("RSK_TREATYID")==null?"":resMap.get("RSK_TREATYID").toString());
				beanObj.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
				beanObj.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
				beanObj.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				beanObj.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				beanObj.setUnderwriter(resMap.get("UNDERWRITTER")==null?"":resMap.get("UNDERWRITTER").toString());
				beanObj.setIncepDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				beanObj.setExpDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				beanObj.setAccDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":resMap.get("RSK_ACCOUNT_DATE").toString());
				beanObj.setOrginalCurrency(resMap.get("SHORT_NAME")==null?"":resMap.get("SHORT_NAME").toString());
				beanObj.setExchRate(resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString());
				beanObj.setMdInstalmentNumber(resMap.get("MND_INSTALLMENTS")==null?"0":resMap.get("MND_INSTALLMENTS").toString());
				beanObj.setRetroType(resMap.get("RSK_RETRO_TYPE")==null?"0":resMap.get("RSK_RETRO_TYPE").toString());
				beanObj.setNoRetroCess(resMap.get("RETRO_CESSIONARIES")==null?"0":resMap.get("RETRO_CESSIONARIES").toString());
				beanObj.setExchRate(resMap.get("RSK_CESSION_EXG_RATE")==null?"":resMap.get("RSK_CESSION_EXG_RATE").toString());
				beanObj.setFixedRate(resMap.get("RSK_FIXED_RATE")==null?"":resMap.get("RSK_FIXED_RATE").toString());
				beanObj.setDummyCon(resMap.get("RSK_DUMMY_CONTRACT")==null?"":resMap.get("RSK_DUMMY_CONTRACT").toString());
				String categoryDetailId = resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString();
				if (resMap.get("RSK_BASIS") != null && !"0".equals(resMap.get("RSK_BASIS"))) {
					List<ConstantDetail> list =	cdRepo.findByCategoryIdAndStatusAndCategoryDetailId(new BigDecimal(6),"Y",new BigDecimal(categoryDetailId));
				if(list.size()>0) { 
					beanObj.setBasis(list.get(0).getDetailName());
					}
				}
				beanObj.setPnoc(resMap.get("RSK_PERIOD_OF_NOTICE")==null?"":resMap.get("RSK_PERIOD_OF_NOTICE").toString());
				beanObj.setRiskCovered(resMap.get("RSK_RISK_COVERED")==null?"":resMap.get("RSK_RISK_COVERED").toString());
				beanObj.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
				beanObj.setTerritory(resMap.get("TERRITORY_DESC")==null?"":resMap.get("TERRITORY_DESC").toString());
				beanObj.setTreatyName(resMap.get("TREATYTYPE") == null ? "" : resMap.get("TREATYTYPE").toString());
				beanObj.setTreatyType(resMap.get("TREATYTYPEID") == null ? "" : resMap.get("TREATYTYPEID").toString());
				beanObj.setLOCIssued(resMap.get("RSK_LOC_ISSUED") == null ? "" : resMap.get("RSK_LOC_ISSUED").toString());
				beanObj.setPerilCovered(resMap.get("RSK_PERILS_COVERED") == null ? "" : resMap.get("RSK_PERILS_COVERED").toString());
				if("0".equalsIgnoreCase(beanObj.getPerilCovered())){
					beanObj.setPerilCovered("None");
				}else{
				beanObj.setPerilCovered(resMap.get("RSK_PERILS_COVERED_Con")==null ? "" : resMap.get("RSK_PERILS_COVERED_Con").toString());
				}
				beanObj.setTreatynoofLine(resMap.get("RSK_NO_OF_LINE") == null ? "0" : resMap.get("RSK_NO_OF_LINE").toString());
				
				if (resMap.get("RSK_STATUS") != null) {
					if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("P")||"0".equalsIgnoreCase(resMap.get("RSK_STATUS").toString())) {
						beanObj.setStatus("Pending");
					}else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("N")) {
						beanObj.setStatus("Not Taken Up");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("A")) {
						beanObj.setStatus("Accepted");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("R")) {
						beanObj.setStatus("Rejected");
					} else {
						beanObj.setStatus("Pending");
					}
				}
				beanObj.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
				beanObj.setRunoffYear(resMap.get("RSK_RUN_OFF_YEAR")==null?"":resMap.get("RSK_RUN_OFF_YEAR").toString());
				beanObj.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
				beanObj.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
				beanObj.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
				beanObj.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
				beanObj.setInsuredName(resMap.get("RSK_INSURED_NAME")==null?"":resMap.get("RSK_INSURED_NAME").toString());
				beanObj.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString());
				if(StringUtils.isNotBlank(beanObj.getTerritory())){
				List<String> territory =new ArrayList<String>(Arrays.asList(beanObj.getTerritory().split(","))) ;
				List<BigDecimal> territory1 = territory.stream().map(BigDecimal::new).collect(Collectors.toList());
				List<TmasTerritory> list = ttRepo.findByBranchCodeAndTerritoryIdIn(req.getBranchCode(),territory1);
				if(list != null) {
					String list1 = "";
					for(int i =0 ;i<list.size();i++) {
					 list1 =	list.get(i).getTerritoryName() + ", " + list1;
					}
					beanObj.setTerritoryName(list.get(0).getTerritoryName());
				}
				}
			
				beanObj.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE")==null?"":resMap.get("COUNTRIES_INCLUDE").toString());
				if(StringUtils.isNotBlank(beanObj.getCountryIncludedList())){
					List<String> countryIncludedList =new ArrayList<String>(Arrays.asList(beanObj.getCountryIncludedList().split(","))) ;
					List<BigDecimal> countryIncludedList1 = countryIncludedList.stream().map(BigDecimal::new).collect(Collectors.toList());
					List<CountryMaster> list = cmRepo.findByBranchCodeAndCountryIdIn(req.getBranchCode(),countryIncludedList1);
					if(list != null) {
						String list1 = "";
						for(int i =0 ;i<list.size();i++) {
						 list1 =	list.get(i).getCountryName() + ", " + list1;
						}
						beanObj.setCountryIncludedName(list1);
						beanObj.setCountryIncludedName(beanObj.getCountryIncludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
					}
				}
				beanObj.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE")==null?"":resMap.get("COUNTRIES_EXCLUDE").toString());
				if(StringUtils.isNotBlank(beanObj.getCountryExcludedList())){
					List<String> countryExcludedList =new ArrayList<String>(Arrays.asList(beanObj.getCountryExcludedList().split(","))) ;
					List<BigDecimal> countryExcludedList1 = countryExcludedList.stream().map(BigDecimal::new).collect(Collectors.toList());
					List<CountryMaster> list = cmRepo.findByBranchCodeAndCountryIdIn(req.getBranchCode(),countryExcludedList1);
					if(list != null) {
						String list1 = "";
						for(int i =0 ;i<list.size();i++) {
						 list1 =	list.get(i).getCountryName() + ", " + list1;
						}
						beanObj.setCountryExcludedName(list1);
						beanObj.setCountryExcludedName(beanObj.getCountryExcludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
					}
				}
			} 
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskProposal> rp = query.from(TtrnRiskProposal.class);

			// RSK_PREMIUM_BASIS_Con
			Subquery<Long> basis = query.subquery(Long.class); 
			Root<ConstantDetail> cd = basis.from(ConstantDetail.class);
			basis.select(cd.get("detailName"));
			Predicate a1 = cb.equal( cd.get("categoryId"), "31");
			Predicate a2 = cb.equal( cd.get("categoryDetailId"), rp.get("rskPremiumBasis"));
			basis.where(a1,a2);
			
			// Select
			query.multiselect(rp.alias("TtrnRiskProposal"),basis.alias("rskPremiumBasisCon")); 

			// Where
			Predicate n1 = cb.equal(rp.get("rskProposalNumber"), req.getProposalNo());
			Predicate n2 = cb.equal(rp.get("rskEndorsementNo"), req.getAmendId());
			query.where(n1,n2);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> res1 = result.getResultList();
			
			TtrnRiskProposal data1 = (TtrnRiskProposal) res1.get(0).get("TtrnRiskProposal");	
		//	Long preBasis = (Long) res1.get(0).get("rskPremiumBasisCon");
			
		
			if(res1!=null && res1.size()>0)
			if (data1 != null) { 
				beanObj.setSharSign(fm.formatter(data1.getRskShareSigned()==null?"":data1.getRskShareSigned().toString()));
				if("TR".equalsIgnoreCase(beanObj.getRetroType())){data1.getRskLimitOc();
					beanObj.setLimitOrigCur(fm.formatter(data1.getRskLimitOc()==null?"":data1.getRskLimitOc().toString()));
					beanObj.setLimitOrigCurDc(fm.formatter(data1.getRskLimitDc()==null?"":data1.getRskLimitDc().toString()));
					beanObj.setLimitOrigCurOSOC(getShareVal(beanObj.getLimitOrigCur().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setLimitOrigCurOSDC(fm.formatter(getDesginationCountry(beanObj.getLimitOrigCurOSOC().replaceAll(",",""),beanObj.getExchRate().toString())));
				}
				else{
					beanObj.setFaclimitOrigCur(fm.formatter(data1.getRskLimitOc()==null?"":data1.getRskLimitOc().toString()));
					beanObj.setFacLimitOrigCurDc(fm.formatter(data1.getRskLimitDc()==null?"":data1.getRskLimitDc().toString()));
					beanObj.setFaclimitOrigCurOSOC(getShareVal(beanObj.getFaclimitOrigCur().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setFaclimitOrigCurOSDC(fm.formatter(getDesginationCountry(beanObj.getFaclimitOrigCurOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
				}
				
				beanObj.setEpiorigCur(fm.formatter(data1.getRskEpiOfferOc()==null?"":data1.getRskEpiOfferOc().toString()));
				beanObj.setEpiorigCurDc(fm.formatter(data1.getRskEpiOfferDc()==null?"":data1.getRskEpiOfferDc().toString()));
				beanObj.setOurEstimate(data1.getRskEpiEstimate()==null?"":data1.getRskEpiEstimate().toString());
				beanObj.setXlPremium(fm.formatter(data1.getRskXlpremOc()==null?"":data1.getRskXlpremOc().toString()));
				beanObj.setXlPremiumDc(fm.formatter(data1.getRskXlpremDc()==null?"":data1.getRskXlpremDc().toString()));
				beanObj.setDeduchunPercent(fm.formatter(data1.getRskDeducOc()==null?"":data1.getRskDeducOc().toString()));
				beanObj.setEpiEstmate(fm.formatter(data1.getRskEpiEstOc()==null?"":data1.getRskEpiEstOc().toString()));
				beanObj.setEpiEstmateDc(fm.formatter(data1.getRskEpiEstDc()==null?"":data1.getRskEpiEstDc().toString()));
				beanObj.setEpiEstmateOSOC(getShareVal(beanObj.getEpiEstmate().replaceAll(",", ""),beanObj.getSharSign(),"share"));
				beanObj.setEpiEstmateOSDC(fm.formatter(getDesginationCountry(beanObj.getEpiEstmateOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
				beanObj.setXlCost(fm.formatter(data1.getRskXlcostOc()==null?"":data1.getRskXlcostOc().toString()));
				beanObj.setXlCostDc(fm.formatter(data1.getRskXlcostDc()==null?"":data1.getRskXlcostDc().toString()));
				beanObj.setCedReten(fm.formatter(data1.getRskCedantRetention()==null?"":data1.getRskCedantRetention().toString()));
				beanObj.setShareWritt(fm.formatter(data1.getRskShareWritten()==null?"":data1.getRskShareWritten().toString()));
				beanObj.setMdPremium(fm.formatter(	data1.getRskMdPremOc()==null?"":	data1.getRskMdPremOc().toString()));
				beanObj.setMdpremiumDc(fm.formatter(data1.getRskMdPremDc()==null?"":data1.getRskMdPremDc().toString()));
				beanObj.setAdjRate(fm.formatter(data1.getRskAdjrate()==null?"":data1.getRskAdjrate().toString()));
				beanObj.setPortfoloCovered(data1.getRskPfCovered()==null?"":data1.getRskPfCovered().toString());
				beanObj.setSubPremium(fm.formatter(data1.getRskSubjPremiumOc()==null?"":data1.getRskSubjPremiumOc().toString()));
				beanObj.setSubPremiumDc(fm.formatter(data1.getRskSubjPremiumDc()==null?"":data1.getRskSubjPremiumDc().toString()));
				beanObj.setMaxLimitProduct(fm.formatter(data1.getRskMaxLmtCover()==null?"":data1.getRskMaxLmtCover().toString()));
				beanObj.setDeduchunPercentDc(data1.getRskDeducDc()==null?"":data1.getRskDeducDc().toString());
				beanObj.setLimitOurShare(fm.formatter(	data1.getRskLimitOsOc()==null?"":	data1.getRskLimitOsOc().toString()));
				beanObj.setLimitOurShareDc(fm.formatter(data1.getRskLimitOsDc()==null?"":data1.getRskLimitOsDc().toString()));
				beanObj.setEpiAsPerOffer(fm.formatter(data1.getRskEventLimitOsDc()==null?"":data1.getRskEventLimitOsDc().toString()));
				beanObj.setEpiAsPerOfferDc(fm.formatter(data1.getRskEpiOsofOc()==null?"":data1.getRskEpiOsofOc().toString()));
				beanObj.setEpiOurShareEs(fm.formatter(data1.getRskEpiOsofDc()==null?"":data1.getRskEpiOsofDc().toString()));
				beanObj.setEpiOurShareEsDc(fm.formatter(data1.getRskEpiOsoeDc()==null?"":data1.getRskEpiOsoeDc().toString()));
				beanObj.setXlcostOurShare(fm.formatter(data1.getRskXlcostOsOc()==null?"":data1.getRskXlcostOsOc().toString()));
				beanObj.setXlcostOurShareDc(fm.formatter(data1.getRskXlcostOsDc()==null?"":data1.getRskXlcostOsDc().toString()));
				beanObj.setMdpremiumourservice(fm.formatter(data1.getRskMdPremOsOc()==null?"":data1.getRskMdPremOsOc().toString()));
				beanObj.setMdpremiumourserviceDc(fm.formatter(data1.getRskMdPremOsDc()==null?"":data1.getRskMdPremOsDc().toString()));
				beanObj.setSpRetro(data1.getRskSpRetro()==null?"0":data1.getRskSpRetro().toString());
				if(beanObj.getSpRetro() != null)
				beanObj.setSpRetro(beanObj.getSpRetro() == "Y" ? "Yes" : "No");
				beanObj.setNoInsurer(data1.getRskNoOfInsurers()==null?"0":data1.getRskNoOfInsurers().toString());
				beanObj.setMaxLimitProduct(fm.formatter(data1.getRskMaxLmtCover()==null?"0":data1.getRskMaxLmtCover().toString()));
				
				beanObj.setCedRetenType(data1.getRskCedretType()==null?"0":data1.getRskCedretType().toString());
				if(beanObj.getCedRetenType() != null)
				beanObj.setCedRetenType(beanObj.getCedRetenType()=="P" ? "Percentage": "Amount");
				beanObj.setPml(data1.getRskPml() == null ? "" : data1.getRskPml().toString());
				if(beanObj.getPml() != null)
					beanObj.setPml(beanObj.getPml() == "Y" ? "Yes" : "No");
				beanObj.setPmlPercent(data1.getRskPmlPercent() == null ? "" : data1.getRskPmlPercent().toString());
				if("TR".equalsIgnoreCase(beanObj.getRetroType())){
					beanObj.setLimitOrigCurPml(fm.formatter(data1.getRskTrtyLmtPmlOc() == null ? "0" : data1.getRskTrtyLmtPmlOc().toString()));
					beanObj.setLimitOrigCurPmlDC(fm.formatter(data1.getRskTrtyLmtPmlDc() == null ? "0" : data1.getRskTrtyLmtPmlDc().toString()));
					}
					else{
						beanObj.setFaclimitOrigCurPml(fm.formatter(data1.getRskTrtyLmtPmlOc() == null ? "0" : data1.getRskTrtyLmtPmlOc().toString()));
						beanObj.setFaclimitOrigCurPmlDC(fm.formatter(data1.getRskTrtyLmtPmlDc() == null ? "0" : data1.getRskTrtyLmtPmlDc().toString()));
					}
					beanObj.setTreatyLimitsurplusOCPml(fm.formatter(data1.getRskTrtyLmtSurPmlOc() == null ? "" : data1.getRskTrtyLmtSurPmlOc().toString()));
					beanObj.setTreatyLimitsurplusOCPmlDC(fm.formatter(data1.getRskTrtyLmtSurPmlDc() == null ? "" : data1.getRskTrtyLmtSurPmlDc().toString()));
					beanObj.setTreatyLimitsurplusOC(fm.formatter(data1.getRskTreatySurpLimitOc() == null ? "" : data1.getRskTreatySurpLimitOc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskTreatySurpLimitOc().toString() == null ? "" : data1.getRskTreatySurpLimitOc().toString()));
					beanObj.setTreatyLimitsurplusDC(fm.formatter(data1.getRskTreatySurpLimitDc() == null ? "" : data1.getRskTreatySurpLimitDc().toString().equalsIgnoreCase("0") ? "0" : data1.getRskTreatySurpLimitDc().toString() == null ? "" : data1.getRskTreatySurpLimitDc().toString()));
					beanObj.setTreatyLimitsurplusOCOSOC(getShareVal(beanObj.getTreatyLimitsurplusOC().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setTreatyLimitsurplusOCOSDC(getDesginationCountry(beanObj.getTreatyLimitsurplusOCOSOC().replaceAll(",",""),beanObj.getExchRate()).toString());
			}
			List<TtrnMndInstallments> instalmentList = 	mndRepo.findByProposalNoAndLayerNoAndEndorsementNoAndEndorsementNoNotNullOrderByInstallmentNoAsc(req.getProposalNo(),new BigDecimal(req.getLayerNo()),new BigDecimal(req.getAmendId()));	
			List<InstalmentListRes1> instalment1 = new ArrayList<InstalmentListRes1>();
			if (instalmentList != null) {
			
				for (int number = 0; number < instalmentList.size(); number++) {
					InstalmentListRes1 instalment = new 	InstalmentListRes1();
					TtrnMndInstallments insMap = instalmentList.get(number);
					instalment.setDateList(insMap.getInstallmentDate()==null?"":insMap.getInstallmentDate().toString());
					instalment.setPremiumList(insMap.getMndPremiumOc()==null?"":insMap.getMndPremiumOc().toString());
					instalment1.add(instalment);
						}
			}
			beanObj.setInstalmentList(instalment1);	
			args = new String[5];
			args[0] = req.getBranchCode();
			args[1] = req.getProposalNo();
			args[2] = req.getAmendId();
			args[3] = req.getProposalNo();
			args[4] = req.getAmendId();
			selectQry = "risk.select.getThirdPageData";
			List<Map<String, Object>> res3 =  queryImpl.selectList(selectQry,args);
			Map<String, Object> thirdViewDataMap = null;
			if(res3!=null && res3.size()>0)
				thirdViewDataMap = (Map<String, Object>)res3.get(0);
			if (thirdViewDataMap != null) {
				for (int k = 0; k < thirdViewDataMap.size(); k++) {
					beanObj.setBrokerage(thirdViewDataMap.get("RSK_BROKERAGE")==null?"":thirdViewDataMap.get("RSK_BROKERAGE").toString());
					beanObj.setTax(fm.formatter(thirdViewDataMap.get("RSK_TAX")==null?"":thirdViewDataMap.get("RSK_TAX").toString()));
					if (thirdViewDataMap.get("RSK_PROFIT_COMM") != null) {
						if (thirdViewDataMap.get("RSK_PROFIT_COMM").toString().equalsIgnoreCase("1")) {
							beanObj.setProfitCommission("YES");
						} else {
							beanObj.setProfitCommission("NO");
						}
					}
					beanObj.setPremiumQuotaShare(fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE")==null?"":thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE").toString()));
					beanObj.setPremiumSurplus(fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_SURPULS")==null?"":thirdViewDataMap.get("RSK_PREMIUM_SURPULS").toString()));
					beanObj.setPremiumQuotaShareDC(fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE_DC")==null?"":thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE_DC").toString()));
					beanObj.setPremiumSurplusDC(fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_SURPLUS_DC")==null?"":thirdViewDataMap.get("RSK_PREMIUM_SURPLUS_DC").toString()));
					beanObj.setPremiumQuotaShareOSOC(getShareVal(beanObj.getPremiumQuotaShare().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setPremiumSurplusOSOC(getShareVal(beanObj.getPremiumSurplus().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setPremiumQuotaShareOSDC(fm.formatter(getDesginationCountry(beanObj.getPremiumQuotaShareOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setPremiumSurplusOSDC(fm.formatter(getDesginationCountry(beanObj.getPremiumSurplusOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					
					beanObj.setAcquisitionCost(fm.formatter(thirdViewDataMap.get("RSK_ACQUISTION_COST_OC")==null?"":thirdViewDataMap.get("RSK_ACQUISTION_COST_OC").toString()));
					beanObj.setAcquisitionCostDc(thirdViewDataMap.get("RSK_ACQUISTION_COST_DC")==null?"":thirdViewDataMap.get("RSK_ACQUISTION_COST_DC").toString());
					beanObj.setCommissionQS(fm.formatter(thirdViewDataMap.get("RSK_COMM_QUOTASHARE")==null?"":thirdViewDataMap.get("RSK_COMM_QUOTASHARE").toString()));
					beanObj.setCommissionsurp(fm.formatter(thirdViewDataMap.get("RSK_COMM_SURPLUS")==null?"":thirdViewDataMap.get("RSK_COMM_SURPLUS").toString()));
					beanObj.setOverRidder(fm.formatter(thirdViewDataMap.get("RSK_OVERRIDER_PERC")==null?"":thirdViewDataMap.get("RSK_OVERRIDER_PERC").toString()));
					beanObj.setManagementExpenses(thirdViewDataMap.get("RSK_MANAGEMENT_EXPENSES")==null?"":thirdViewDataMap.get("RSK_MANAGEMENT_EXPENSES").toString());
					beanObj.setLossCF(thirdViewDataMap.get("RSK_LOSS_CARRYFORWARD")==null?"":thirdViewDataMap.get("RSK_LOSS_CARRYFORWARD").toString());
					beanObj.setPremiumReserve(fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_RESERVE")==null?"":thirdViewDataMap.get("RSK_PREMIUM_RESERVE").toString()));
					beanObj.setLossreserve(fm.formatter(thirdViewDataMap.get("RSK_LOSS_RESERVE")==null?"":thirdViewDataMap.get("RSK_LOSS_RESERVE").toString()));
					beanObj.setInterest(fm.formatter(thirdViewDataMap.get("RSK_INTEREST")==null?"":thirdViewDataMap.get("RSK_INTEREST").toString()));
					beanObj.setPortfolioinoutPremium(fm.formatter(thirdViewDataMap.get("RSK_PF_INOUT_PREM")==null?"":thirdViewDataMap.get("RSK_PF_INOUT_PREM").toString()));
					beanObj.setPortfolioinoutLoss(fm.formatter(thirdViewDataMap.get("RSK_PF_INOUT_LOSS")==null?"":thirdViewDataMap.get("RSK_PF_INOUT_LOSS").toString()));
					beanObj.setLossAdvise(fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE")==null?"":thirdViewDataMap.get("RSK_LOSSADVICE").toString()));
					beanObj.setCashLossLimit(fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC")==null?"":thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC").toString()));
					beanObj.setCashLossLimitDc(fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC")==null?"":thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC").toString()));
					beanObj.setAnualAggregateLiability(fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC")==null?"":thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC").toString()));
					beanObj.setAnualAggregateLiabilityDc(fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC")==null?"":thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC").toString()));
					beanObj.setReinstNo(fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_NO")==null?"":thirdViewDataMap.get("RSK_REINSTATE_NO").toString()));
					beanObj.setReinstAditionalPremiumpercent(fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC")==null?"":thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString()));
					beanObj.setReinstAditionalPremiumpercentDc(fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC")==null?"":thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC").toString()));
					beanObj.setLeaderUnderwriter(thirdViewDataMap.get("RSK_LEAD_UW")==null?"":thirdViewDataMap.get("RSK_LEAD_UW").toString());
					beanObj.setLeaderUnderwritershare(fm.formatter(thirdViewDataMap.get("RSK_LEAD_UW_SHARE")==null?"":thirdViewDataMap.get("RSK_LEAD_UW_SHARE").toString()));
					beanObj.setAccounts(thirdViewDataMap.get("RSK_ACCOUNTS")==null?"":thirdViewDataMap.get("RSK_ACCOUNTS").toString());
					beanObj.setExclusion(thirdViewDataMap.get("RSK_EXCLUSION")==null?"":thirdViewDataMap.get("RSK_EXCLUSION").toString());
					beanObj.setRemarks(thirdViewDataMap.get("RSK_REMARKS")==null?"":thirdViewDataMap.get("RSK_REMARKS").toString());
					beanObj.setUnderwriterRecommendations(thirdViewDataMap.get("RSK_UW_RECOMM")==null?"":thirdViewDataMap.get("RSK_UW_RECOMM").toString());
					beanObj.setGmsApproval(thirdViewDataMap.get("RSK_GM_APPROVAL")==null?"":thirdViewDataMap.get("RSK_GM_APPROVAL").toString());
					beanObj.setDecision(thirdViewDataMap.get("RSK_DECISION") == null ? "" : thirdViewDataMap.get("RSK_DECISION").toString());
					beanObj.setOthercost(fm.formatter(thirdViewDataMap.get("RSK_OTHER_COST")==null?"":thirdViewDataMap.get("RSK_OTHER_COST").toString()));
					beanObj.setReinstAdditionalPremium(fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString()));
					beanObj.setBurningCost(fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					beanObj.setCommissionQSAmt(fm.formatter(thirdViewDataMap.get("COMM_QS_AMT")==null?"":thirdViewDataMap.get("COMM_QS_AMT").toString()));
					beanObj.setCommissionsurpAmt(fm.formatter(thirdViewDataMap.get("COMM_SURPLUS_AMT")==null?"":thirdViewDataMap.get("COMM_SURPLUS_AMT").toString()));
					beanObj.setAcqCostPer(fm.formatter(thirdViewDataMap.get("OTHER_ACQ_COST_AMT")==null?"":thirdViewDataMap.get("OTHER_ACQ_COST_AMT").toString()));
					beanObj.setReinstAditionalPremiumpercentDc(fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString()));
					beanObj.setBurningCost(fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					beanObj.setBrokerage((thirdViewDataMap.get("RSK_BROKERAGE")==null?"":thirdViewDataMap.get("RSK_BROKERAGE").toString()));
					beanObj.setLimitPerVesselOC(fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_OC")==null?"":thirdViewDataMap.get("LIMIT_PER_VESSEL_OC").toString()));
					beanObj.setLimitPerVesselDC(fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_DC")==null?"":thirdViewDataMap.get("LIMIT_PER_VESSEL_DC").toString()));
					beanObj.setLimitPerLocationOC(fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_OC")==null?"":thirdViewDataMap.get("LIMIT_PER_LOCATION_OC").toString()));
					beanObj.setLimitPerLocationDC(fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_DC")==null?"":thirdViewDataMap.get("LIMIT_PER_LOCATION_DC").toString()));
					beanObj.setProfitCommission(thirdViewDataMap.get("RSK_PRO_NOTES")==null?"":thirdViewDataMap.get("RSK_PRO_NOTES").toString());
					beanObj.setManagementExpenses(thirdViewDataMap.get("RSK_PRO_MANAGEMENT_EXP")==null?"":thirdViewDataMap.get("RSK_PRO_MANAGEMENT_EXP").toString());
					beanObj.setCommissionType(thirdViewDataMap.get("RSK_PRO_COMM_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_COMM_TYPE").toString());
					beanObj.setProfitCommissionPer(thirdViewDataMap.get("RSK_PRO_COMM_PER")==null?"":thirdViewDataMap.get("RSK_PRO_COMM_PER").toString());
					beanObj.setSetup(thirdViewDataMap.get("RSK_PRO_SET_UP")==null?"":thirdViewDataMap.get("RSK_PRO_SET_UP").toString());
					beanObj.setSuperProfitCommission(thirdViewDataMap.get("RSK_PRO_SUP_PRO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_SUP_PRO_COM").toString());
					beanObj.setLossCarried(thirdViewDataMap.get("RSK_PRO_LOSS_CARY_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_LOSS_CARY_TYPE").toString());
					beanObj.setLossyear(thirdViewDataMap.get("RSK_PRO_LOSS_CARY_YEAR")==null?"":thirdViewDataMap.get("RSK_PRO_LOSS_CARY_YEAR").toString());
					beanObj.setProfitCarried(thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_TYPE")==null?"":thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_TYPE").toString());
					beanObj.setProfitCarriedForYear(thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_YEAR")==null?"":thirdViewDataMap.get("RSK_PRO_PROFIT_CARY_YEAR").toString());
					beanObj.setFistpc(thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM").toString());
					beanObj.setProfitMont(thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM_PRD")==null?"":thirdViewDataMap.get("RSK_PRO_FIRST_PFO_COM_PRD").toString());
					beanObj.setSubProfitMonth(thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM_PRD")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM_PRD").toString());
					beanObj.setSubpc(thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_PFO_COM").toString());
					beanObj.setSubSeqCalculation(thirdViewDataMap.get("RSK_PRO_SUB_SEQ_CAL")==null?"":thirdViewDataMap.get("RSK_PRO_SUB_SEQ_CAL").toString());
					beanObj.setCrestaStatus(thirdViewDataMap.get("RSK_CREASTA_STATUS")==null?"":thirdViewDataMap.get("RSK_CREASTA_STATUS").toString());
					beanObj.setSlideScaleCommission(thirdViewDataMap.get("RSK_SLADSCALE_COMM")==null?"":thirdViewDataMap.get("RSK_SLADSCALE_COMM").toString());
					beanObj.setLossParticipants(thirdViewDataMap.get("RSK_LOSS_PART_CARRIDOR")==null?"":thirdViewDataMap.get("RSK_LOSS_PART_CARRIDOR").toString());
					beanObj.setShareProfitCommission(thirdViewDataMap.get("RSK_PROFIT_COMM")==null?"":thirdViewDataMap.get("RSK_PROFIT_COMM").toString());
					beanObj.setLocRate(thirdViewDataMap.get("RSK_RATE")==null?"":thirdViewDataMap.get("RSK_RATE").toString());
					beanObj.setRetroCommissionType(thirdViewDataMap.get("RSK_COMMISSION_TYPE")==null?"":thirdViewDataMap.get("RSK_COMMISSION_TYPE").toString());
					beanObj.setLeaderUnderwritercountry(thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
					beanObj.setLossAdvise(fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE")==null?"":thirdViewDataMap.get("RSK_LOSSADVICE").toString()));
					beanObj.setLossAdviseDc(fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE_DC")==null?"":thirdViewDataMap.get("RSK_LOSSADVICE_DC").toString()));
					beanObj.setLossAdviseOSOC(getShareVal(beanObj.getLossAdvise().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setLossAdviseOSDC(fm.formatter(getDesginationCountry(beanObj.getLossAdviseOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setCashLossLimit(fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC")==null?"":thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC").toString()));
					beanObj.setCashLossLimitDc(fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC")==null?"":thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC").toString()));
					beanObj.setCashLossLimitOSOC(getShareVal(beanObj.getCashLossLimit().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setCashLossLimitOSDC(fm.formatter(getDesginationCountry(beanObj.getCashLossLimitOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setEventlimit(fm.formatter(thirdViewDataMap.get("RSK_EVENT_LIMIT_OC")==null?"0":thirdViewDataMap.get("RSK_EVENT_LIMIT_OC").toString()));
					beanObj.setEventlimitDC(fm.formatter(thirdViewDataMap.get("RSK_EVENT_LIMIT_DC")==null?"0":thirdViewDataMap.get("RSK_EVENT_LIMIT_DC").toString()));
					beanObj.setEventlimitOSOC(getShareVal(beanObj.getEventlimit().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setEventlimitOSDC(fm.formatter(getDesginationCountry(beanObj.getEventlimitOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setAggregateLimit(fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_OC")==null?"0":thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_OC").toString()));
					beanObj.setAggregateLimitDC(fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_DC")==null?"0":thirdViewDataMap.get("RSK_AGGREGATE_LIMIT_DC").toString()));
					beanObj.setAggregateLimitOSOC(getShareVal(beanObj.getAggregateLimit().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setAggregateLimitOSDC(fm.formatter(getDesginationCountry(beanObj.getAggregateLimitOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setOccurrentLimit(fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC")==null?"0":thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC").toString()));
					beanObj.setOccurrentLimitDC(fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC")==null?"0":thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC").toString()));
					beanObj.setOccurrentLimitOSOC(getShareVal(beanObj.getOccurrentLimit().replaceAll(",", ""),beanObj.getSharSign(),"share"));
					beanObj.setOccurrentLimitOSDC(fm.formatter(getDesginationCountry(beanObj.getOccurrentLimitOSOC().replaceAll(",",""),beanObj.getExchRate()).toString()));
					beanObj.setCommissionQSYN(thirdViewDataMap.get("RSK_COMMISSION_QS_YN") == null ? "" : thirdViewDataMap.get("RSK_COMMISSION_QS_YN").toString());
					beanObj.setCommissionsurpYN(thirdViewDataMap.get("RSK_COMMISSION_SUR_YN") == null ? "" : thirdViewDataMap.get("RSK_COMMISSION_SUR_YN").toString());
					beanObj.setOverRidderYN(thirdViewDataMap.get("RSK_OVERRIDE_YN") == null ? "" : thirdViewDataMap.get("RSK_OVERRIDE_YN").toString());
					beanObj.setBrokerageYN(thirdViewDataMap.get("RSK_BROKARAGE_YN") == null ? "" : thirdViewDataMap.get("RSK_BROKARAGE_YN").toString());
					beanObj.setTaxYN(thirdViewDataMap.get("RSK_TAX_YN") == null ? "" : thirdViewDataMap.get("RSK_TAX_YN").toString());
					beanObj.setOthercostYN(thirdViewDataMap.get("RSK_OTHER_COST_YN") == null ? "" : thirdViewDataMap.get("RSK_OTHER_COST_YN").toString());
					beanObj.setCeedODIYN(thirdViewDataMap.get("RSK_CEED_ODI_YN") == null ? "" : thirdViewDataMap.get("RSK_CEED_ODI_YN").toString());
					
					if("NR".equalsIgnoreCase(beanObj.getLocRate())){
						beanObj.setLocRate("On Net Rate");
					}
					else if("GR".equalsIgnoreCase(beanObj.getLocRate())){
						beanObj.setLocRate("On Gross Rate");
					}
				}
			}
//			showRetroContracts(beanObj,pid,true);
//			GetRemarksDetails(beanObj);
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
		e.printStackTrace();
	}
	return res;
}
@Override
public GetRemarksDetailsRes getRemarksDetails(String proposalNo) {
	GetRemarksDetailsRes response = new GetRemarksDetailsRes();
	GetRemarksDetailsRes1 res2 = new GetRemarksDetailsRes1();
	List<RemarksRes> remarksRes = new ArrayList<RemarksRes>();
	try {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		// like table name
		Root<TtrnRiskRemarks> pm = query.from(TtrnRiskRemarks.class);

		// Select
		query.multiselect(pm.get("rskSNo").alias("RSK_SNO"),pm.get("rskDescription").alias("RSK_DESCRIPTION"),pm.get("rskRemark1").alias("RSK_REMARK1"),pm.get("rskRemark2").alias("RSK_REMARK2")); 

		// MAXAmend ID
		Subquery<Long> amend = query.subquery(Long.class); 
		Root<TtrnRiskRemarks> pms = amend.from(TtrnRiskRemarks.class);
		amend.select(cb.max(pms.get("amendId")));
		Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
		amend.where(a1);

		//Order By
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(pm.get("rskSNo")));

		// Where
		Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
		Predicate n2 = cb.equal(pm.get("layerNo"), "0");
		Predicate n3 = cb.equal(pm.get("amendId"), amend);
		query.where(n1,n2,n3).orderBy(orderList);
		
		// Get Result
		TypedQuery<Tuple> res = em.createQuery(query);
		List<Tuple> result = res.getResultList();
		
		if(result!=null && result.size()>0){
			for (int i = 0; i < result.size(); i++) {
				RemarksRes res1 = new RemarksRes();
				Tuple insMap = result.get(i);
				res1.setRemarkSNo(insMap.get("RSK_SNO")==null?"Remarks":insMap.get("RSK_SNO").toString());
				res1.setDescription(insMap.get("RSK_DESCRIPTION")==null?"Remarks":insMap.get("RSK_DESCRIPTION").toString());	
				res1.setRemark1(insMap.get("RSK_REMARK1")==null?" ":insMap.get("RSK_REMARK1").toString());	
				res1.setRemark2(insMap.get("RSK_REMARK2")==null?"":insMap.get("RSK_REMARK2").toString());	
				remarksRes.add(res1)	;
				}
			res2.setRemarksRes(remarksRes);
			res2.setRemarkCount(Integer.toString(result.size()));
		}
//		else{
//			Map<String,Object> doubleMap = new HashMap<String,Object>();
//			 doubleMap.put("one",new Double(1.0));
//			 result.add(doubleMap);
//			 beanObj.setRemarkList(result);
//		}
		response.setCommonResponse(remarksRes);
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
public ShowRetroCess1Res showRetroCess(ShowRetroCess1Req req) {
	ShowRetroCess1Res response = new ShowRetroCess1Res();
	List<RetroCessListRes> retriCessResList = new ArrayList<RetroCessListRes>();
	try{
		if(req.getMode()==1||req.getMode()==3){
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			if(req.getMode()==1){
				
				Root<TtrnRetroCessionary> pm = query.from(TtrnRetroCessionary.class);

				// Select
				query.multiselect(pm.get("sno").alias("SNO"),pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),pm.get("brokerId").alias("BROKER_ID"),pm.get("shareAccepted").alias("SHARE_ACCEPTED"),pm.get("shareSigned").alias("SHARE_SIGNED"),pm.get("comission").alias("COMISSION"),pm.get("proposalStatus").alias("PROPOSAL_STATUS")); 

				// MAXAmend ID
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnRetroCessionary> pms = amend.from(TtrnRetroCessionary.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				//Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("sno")));

				// Where
				Predicate n1 = cb.equal(pm.get("proposalNo"), req.getProposalNo());
				Predicate n2 = cb.between(pm.get("sno"), BigDecimal.ZERO, new BigDecimal(String.valueOf(Integer.parseInt(req.getNoRetroCess())-1)));
				Predicate n3 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2,n3).orderBy(orderList);
				
			}else if(req.getMode()==3){
				
				Root<TtrnRetroCessionary> pm = query.from(TtrnRetroCessionary.class);

				// Select
				query.multiselect(pm.get("sno").alias("SNO"),pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),pm.get("brokerId").alias("BROKER_ID"),pm.get("shareAccepted").alias("SHARE_ACCEPTED"),pm.get("shareSigned").alias("SHARE_SIGNED"),pm.get("comission").alias("COMISSION"),pm.get("proposalStatus").alias("PROPOSAL_STATUS")); 

				//Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("sno")));

				// Where
				Predicate n1 = cb.equal(pm.get("proposalNo"), req.getProposalNo());
				Predicate n2 = cb.between(pm.get("sno"), BigDecimal.ZERO, new BigDecimal(String.valueOf(Integer.parseInt(req.getNoRetroCess())-1)));
				Predicate n3 = cb.equal(pm.get("amendId"), new BigDecimal(req.getAmendId()));
				query.where(n1,n2,n3).orderBy(orderList);
			}
			// Get Result
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> result = res1.getResultList();
			
			if(result.size()!=0){
				for(int i = 0; i < result.size(); i++) {
					RetroCessListRes retro = new RetroCessListRes();
				Tuple	resMap = result.get(i);
				retro.setCedingCompany(resMap.get("CEDING_COMPANY_ID").toString());
					retro.setRetroBroker(resMap.get("BROKER_ID").toString());
					retro.setProposalStatus(resMap.get("PROPOSAL_STATUS").toString());
					retro.setShareAccepted(resMap.get("SHARE_ACCEPTED").toString());
					retro.setShareSigned(resMap.get("SHARE_SIGNED").toString());	
					retro.setCommission(resMap.get("COMISSION").toString());	
					retriCessResList.add(retro);
				}
			}else{
				
				int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
				
				for(int i=0;i<NoRetroCess;i++){
				RetroCessListRes retriCessRes = new RetroCessListRes();
				RetroCessListReq req2 =	req.getRetroCessReq().get(i);
				retriCessRes.setCedingCompany(req2.getCedingId());
				retriCessRes.setRetroBroker(req2.getBrokerId());
				retriCessRes.setProposalStatus(req2.getProStatus());
				retriCessRes.setShareAccepted("");
				retriCessRes.setShareSigned("");
				
				retriCessResList.add(retriCessRes);
			}
			}
		}else{
			int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
			
			for(int i=0;i<NoRetroCess;i++){
				RetroCessListReq req3 =	req.getRetroCessReq().get(i);
				RetroCessListRes retriCessRes = new RetroCessListRes();
				retriCessRes.setCedingCompany(req3.getCedingId());
				retriCessRes.setRetroBroker(req3.getBrokerId());
				retriCessRes.setProposalStatus(req3.getProStatus());
				retriCessRes.setShareAccepted(req3.getShareWritt());
				retriCessRes.setShareSigned(req3.getSharSign());
				retriCessRes.setCommission(req3.getCommission());
				retriCessResList.add(retriCessRes);
			}	
		}
		response.setCommonResponse(retriCessResList);
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
public ShowSecondPageData1Res showSecondPageData1(ShowSecondPageData1Req req) {
	ShowSecondPageData1Res response = new ShowSecondPageData1Res();
	ShowSecondPageData1Res1 res1 = new ShowSecondPageData1Res1();
	try{
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiskDetails> rk = query.from(TtrnRiskDetails.class);
		Root<TmasPfcMaster> pfc = query.from(TmasPfcMaster.class);
		Root<PersonalInfo> personal = query.from(PersonalInfo.class);
		Root<PersonalInfo> pi = query.from(PersonalInfo.class);
		Root<TmasPolicyBranch> bran = query.from(TmasPolicyBranch.class);
		Root<TmasDepartmentMaster> tdm = query.from(TmasDepartmentMaster.class);
		
		//TMAS_SPFC_NAME
//		CriteriaQuery<String> spfcName = cb.createQuery(String.class); 
//		Root<TmasSpfcMaster> SPFC = spfcName.from(TmasSpfcMaster.class);
//		spfcName.select(SPFC.get("tmasSpfcName"));
//		
//		//spfcId	
//		Subquery<String> spfcId = spfcName.subquery(String.class); 
//		Root<TtrnRiskDetails> id = spfcId.from(TtrnRiskDetails.class);
//		spfcId.select(id.get("rskSpfcid"));
//		
//		Predicate d1 = cb.equal(SPFC.get("tmasSpfcId"),spfcId);
//		Predicate d2 = cb.equal(SPFC.get("tmasProductId"),tdm.get("tmasProductId"));	
//		Predicate d3 = cb.equal(SPFC.get("branchCode"),tdm.get("branchCode"));	
//		spfcName.where(d1,d2,d3);
//		TypedQuery<String> ress = em.createQuery(spfcName);
//		List<String> result1 = ress.getResultList();

		query.multiselect(rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),pfc.get("tmasPfcName").alias("TMAS_PFC_NAME"),
				personal.get("companyName").alias("COMPANY_NAME"),
				pi.get("firstName").alias("FIRST_NAME"),pi.get("lastName").alias("LAST_NAME"),rk.get("rskMonth").alias("MONTH"),
				bran.get("tmasPolBranchName").alias("TMAS_POL_BRANCH_NAME"),rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				rk.get("rskUwyear").alias("RSK_UWYEAR"),tdm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
				rk.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE")); 

		// MAXAmend1
		Subquery<Long> perAmend1 = query.subquery(Long.class); 
		Root<PersonalInfo> s1 = perAmend1.from(PersonalInfo.class);
		perAmend1.select(cb.max(s1.get("amendId")));
		Predicate a1 = cb.equal(s1.get("customerId"), personal.get("customerId"));
		Predicate a2 = cb.equal(s1.get("customerType"), personal.get("customerType"));
		Predicate a3 = cb.equal(s1.get("branchCode"), personal.get("branchCode"));
		perAmend1.where(a1,a2,a3);
		
		// MAXAmend2
		Subquery<Long> perAmend2 = query.subquery(Long.class); 
		Root<PersonalInfo> s2 = perAmend2.from(PersonalInfo.class);
		perAmend2.select(cb.max(s2.get("amendId")));
		Predicate b1 = cb.equal(s2.get("customerId"), pi.get("customerId"));
		Predicate b2 = cb.equal(s2.get("customerType"), pi.get("customerType"));
		Predicate b3 = cb.equal(s2.get("branchCode"), pi.get("branchCode"));
		perAmend2.where(b1,b2,b3);
		
		// maxEnd
		Subquery<Long> rkEnd = query.subquery(Long.class); 
		Root<TtrnRiskDetails> s3 = rkEnd.from(TtrnRiskDetails.class);
		rkEnd.select(cb.max(s3.get("rskEndorsementNo")));
		Predicate c1 = cb.equal(s3.get("rskProposalNumber"), rk.get("rskProposalNumber"));
		rkEnd.where(c1);

		// Where
		Predicate n1 = cb.equal(rk.get("rskProposalNumber"), req.getProposal());
		Predicate n2 = cb.equal(pfc.get("tmasPfcId"), rk.get("rskPfcid"));
		Predicate n3 = cb.equal(pfc.get("branchCode"), req.getBranchCode());
		Predicate n4 = cb.equal(tdm.get("branchCode"), req.getBranchCode());
		Predicate n5 = cb.equal(tdm.get("tmasProductId"), new BigDecimal(req.getProductId()));
		Predicate n6 = cb.equal(tdm.get("tmasDepartmentId"), rk.get("rskDeptid"));
		Predicate n7 = cb.equal(rk.get("rskCedingid"), personal.get("customerId"));
		Predicate n8 = cb.equal(personal.get("customerType"), "C");
		Predicate n9 = cb.equal(personal.get("branchCode"), req.getBranchCode());
		Predicate n10 = cb.equal(personal.get("amendId"), perAmend1);
		Predicate n11 = cb.equal(rk.get("rskBrokerid"), pi.get("customerId"));
		Predicate n12 = cb.equal(pi.get("customerType"), "B");
		Predicate n13 = cb.equal(pi.get("branchCode"), req.getBranchCode());
		Predicate n14 = cb.equal(pi.get("amendId"), perAmend2);
		Predicate n15 = cb.equal(rk.get("rskEndorsementNo"), rkEnd);
		Predicate n16 = cb.equal(rk.get("rskPolbranch"), bran.get("tmasPolBranchId"));
		Predicate n17 = cb.equal(bran.get("branchCode"), req.getBranchCode());
		query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17);
		
		// Get Result
		TypedQuery<Tuple> res = em.createQuery(query);
		List<Tuple> result = res.getResultList();
	
		if(result!=null && result.size()>0)
		{
			Tuple	resMap = result.get(0);
			res1.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
			
			//need to be add
//			(select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER
//					SPFC where SPFC.TMAS_SPFC_ID in(select * from table(SPLIT_TEXT_FN(replace(RK.RSK_SPFCID,' ', '')))) AND SPFC.TMAS_PRODUCT_ID = TDM.TMAS_PRODUCT_ID AND 
//					TDM.BRANCH_CODE = SPFC.BRANCH_CODE)TMAS_SPFC_NAME

			//res1.setSubProfitCenter(resMap.get("TMAS_SPFC_NAME") == null ? "" : resMap.get("TMAS_SPFC_NAME").toString());
			res1.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
			res1.setBroker((resMap.get("FIRST_NAME")==null?"":resMap.get("FIRST_NAME").toString()) + " "+ (resMap.get("LAST_NAME")==null?"":resMap.get("LAST_NAME").toString()));
			res1.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
			res1.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
			res1.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
		}
		//GetRemarksDetails(beanObj);
		response.setCommonResponse(res1);
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
public ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req) {
	ShowSecondPageDataRes response = new ShowSecondPageDataRes();
	ShowSecondPageDataRes1 res1 = new ShowSecondPageDataRes1();
	try{
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiskDetails> rk = query.from(TtrnRiskDetails.class);
		Root<TmasPfcMaster> pfc = query.from(TmasPfcMaster.class);
		Root<PersonalInfo> personal = query.from(PersonalInfo.class);
		Root<PersonalInfo> pi = query.from(PersonalInfo.class);
		Root<TmasPolicyBranch> bran = query.from(TmasPolicyBranch.class);
		Root<TmasDepartmentMaster> tdm = query.from(TmasDepartmentMaster.class);
		
		query.multiselect(rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),pfc.get("tmasPfcName").alias("TMAS_PFC_NAME"),
				personal.get("companyName").alias("COMPANY_NAME"),
				pi.get("firstName").alias("FIRST_NAME"),pi.get("lastName").alias("LAST_NAME"),rk.get("rskMonth").alias("MONTH"),
				bran.get("tmasPolBranchName").alias("TMAS_POL_BRANCH_NAME"),rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				rk.get("rskUwyear").alias("RSK_UWYEAR"),tdm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME"),
				rk.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE")); 

		// MAXAmend1
		Subquery<Long> perAmend1 = query.subquery(Long.class); 
		Root<PersonalInfo> s1 = perAmend1.from(PersonalInfo.class);
		perAmend1.select(cb.max(s1.get("amendId")));
		Predicate a1 = cb.equal(s1.get("customerId"), personal.get("customerId"));
		Predicate a2 = cb.equal(s1.get("customerType"), personal.get("customerType"));
		Predicate a3 = cb.equal(s1.get("branchCode"), personal.get("branchCode"));
		perAmend1.where(a1,a2,a3);
		
		// MAXAmend2
		Subquery<Long> perAmend2 = query.subquery(Long.class); 
		Root<PersonalInfo> s2 = perAmend2.from(PersonalInfo.class);
		perAmend2.select(cb.max(s2.get("amendId")));
		Predicate b1 = cb.equal(s2.get("customerId"), pi.get("customerId"));
		Predicate b2 = cb.equal(s2.get("customerType"), pi.get("customerType"));
		Predicate b3 = cb.equal(s2.get("branchCode"), pi.get("branchCode"));
		perAmend2.where(b1,b2,b3);
		
		// maxEnd
		Subquery<Long> rkEnd = query.subquery(Long.class); 
		Root<TtrnRiskDetails> s3 = rkEnd.from(TtrnRiskDetails.class);
		rkEnd.select(cb.max(s3.get("rskEndorsementNo")));
		Predicate c1 = cb.equal(s3.get("rskProposalNumber"), rk.get("rskProposalNumber"));
		rkEnd.where(c1);

		// Where
		Predicate n1 = cb.equal(rk.get("rskProposalNumber"), req.getProposal());
		Predicate n2 = cb.equal(pfc.get("tmasPfcId"), rk.get("rskPfcid"));
		Predicate n3 = cb.equal(pfc.get("branchCode"), req.getBranchCode());
		Predicate n4 = cb.equal(tdm.get("branchCode"), req.getBranchCode());
		Predicate n5 = cb.equal(tdm.get("tmasProductId"), new BigDecimal(req.getProductId()));
		Predicate n6 = cb.equal(tdm.get("tmasDepartmentId"), rk.get("rskDeptid"));
		Predicate n7 = cb.equal(rk.get("rskCedingid"), personal.get("customerId"));
		Predicate n8 = cb.equal(personal.get("customerType"), "C");
		Predicate n9 = cb.equal(personal.get("branchCode"), req.getBranchCode());
		Predicate n10 = cb.equal(personal.get("amendId"), perAmend1);
		Predicate n11 = cb.equal(rk.get("rskBrokerid"), pi.get("customerId"));
		Predicate n12 = cb.equal(pi.get("customerType"), "B");
		Predicate n13 = cb.equal(pi.get("branchCode"), req.getBranchCode());
		Predicate n14 = cb.equal(pi.get("amendId"), perAmend2);
		Predicate n15 = cb.equal(rk.get("rskEndorsementNo"), rkEnd);
		Predicate n16 = cb.equal(rk.get("rskPolbranch"), bran.get("tmasPolBranchId"));
		Predicate n17 = cb.equal(bran.get("branchCode"), req.getBranchCode());
		query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17);
		
		// Get Result
		TypedQuery<Tuple> res = em.createQuery(query);
		List<Tuple> result = res.getResultList();
	
		if(result!=null && result.size()>0)
		{
			Tuple	resMap = result.get(0);
			res1.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
			if( !"Renewal".equalsIgnoreCase(req.getReMode())){
			res1.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
			}
		//	res1.setSubProfitCenter(resMap.get("TMAS_SPFC_NAME") == null ? "" : resMap.get("TMAS_SPFC_NAME").toString());
			res1.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
			res1.setBroker((resMap.get("FIRST_NAME")==null?"":resMap.get("FIRST_NAME").toString()) + " "+ (resMap.get("LAST_NAME")==null?"":resMap.get("LAST_NAME").toString()));
			res1.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
			res1.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
			res1.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME") == null ? "" : resMap.get("TMAS_POL_BRANCH_NAME").toString());
			res1.setEndttypename(resMap.get("RS_ENDORSEMENT_TYPE")==null?"":resMap.get("RS_ENDORSEMENT_TYPE").toString());
		}
		CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiskCommission> pm = query1.from(TtrnRiskCommission.class);

		query1.multiselect(pm.get("rskReinstateNo").alias("RSK_REINSTATE_NO"),
				pm.get("rskReinstateAddlPremPct").alias("RSK_REINSTATE_ADDL_PREM_PCT"),pm.get("rskBurningCostPct").alias("RSK_BURNING_COST_PCT"),
				pm.get("rskRemarks").alias("RSK_REMARKS")); 

		// MAXAmend ID
		Subquery<Long> maxEnd = query1.subquery(Long.class); 
		Root<TtrnRiskCommission> pms = maxEnd.from(TtrnRiskCommission.class);
		maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
		Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
		maxEnd.where(y1);

		// Where
		Predicate x1 = cb.equal(pm.get("rskProposalNumber"), req.getProposal());
		Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
		query1.where(x1,x2);
		
		// Get Result
		TypedQuery<Tuple> res2 = em.createQuery(query1);
		List<Tuple> commList = res2.getResultList();
		
		if(commList!=null && commList.size()>0){
			Tuple commMap=commList.get(0);
			res1.setReinstNo(commMap.get("RSK_REINSTATE_NO")==null?"":commMap.get("RSK_REINSTATE_NO").toString());
			res1.setReinstAditionalPremiumpercent(commMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":commMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString());
			res1.setBurningCost(commMap.get("RSK_BURNING_COST_PCT")==null?"":commMap.get("RSK_BURNING_COST_PCT").toString());
			res1.setRemarks(commMap.get("RSK_REMARKS")==null?"":commMap.get("RSK_REMARKS").toString());
		}
		response.setCommonResponse(res1);
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
public CommonSaveRes getEditMode(String proposalNo) {
	CommonSaveRes response= new CommonSaveRes();
	int mode=0;
	try {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<String> query1 = cb.createQuery(String.class); 
		
		Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);

		query1.select(pm.get("rskContractNo"));
				

		// MAXAmend ID
		Subquery<Long> maxEnd = query1.subquery(Long.class); 
		Root<TtrnRiskDetails> pms = maxEnd.from(TtrnRiskDetails.class);
		maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
		Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
		maxEnd.where(y1);

		// Where
		Predicate x1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
		Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
		query1.where(x1,x2);
		
		// Get Result
		TypedQuery<String> res2 = em.createQuery(query1);
		List<String> list = res2.getResultList();
		String string = "";
		if(list.size()>0) {
			string = list.get(0)==null?"0":list.get(0);
		}
	
		if ("0".equalsIgnoreCase(string)) {
			mode = 1;
		} else {
			mode = 2;
		}
		response.setResponse(String.valueOf(mode));	
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
public SaveSecondPageRes saveSecondPage(SaveSecondPageReq req) {
	SaveSecondPageRes response = new SaveSecondPageRes();
	SaveSecondPageRes1 res1 = new SaveSecondPageRes1();
	try{
		int ChkSecPagMod = checkSecondPageMode(req.getProposalNo());
		int ContractEditMode = contractEditMode(req.getProposalNo());
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		if (ChkSecPagMod == 2) {
		int	res = saveUpdateRiskDetailsSecondForm(req, req.getProductId(), getMaxAmednId(req.getProposalNo()));
			String GetProposalStatus = "";
			
			CriteriaQuery<String> query1 = cb.createQuery(String.class); 
			
			Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);

			query1.select(pm.get("rskStatus"));
				
			// MAXAmend ID
			Subquery<Long> maxEnd = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> pms = maxEnd.from(TtrnRiskDetails.class);
			maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
			Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
			maxEnd.where(y1);

			// Where
			Predicate x1 = cb.equal(pm.get("rskProposalNumber"),req.getProposalNo());
			Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
			query1.where(x1,x2);
			
			// Get Result
			TypedQuery<String> res2 = em.createQuery(query1);
			List<String> list = res2.getResultList();
			if(list.size()>0) {
				GetProposalStatus = list.get(0)==null?"0":list.get(0);
			}
			res1.setProStatus(GetProposalStatus);
			if (StringUtils.isNotBlank(req.getContractNo())) {
				res1.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : " + req.getProposalNo());
			} else if ("A".equalsIgnoreCase(GetProposalStatus) || "P".equalsIgnoreCase(GetProposalStatus)) {
				res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : " + req.getProposalNo());
			} else if ("N".equalsIgnoreCase(GetProposalStatus)) {
				res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : " + req.getProposalNo());
			}
			res = savemodeUpdateRiskDetailsSecondFormSecondTable(req, req.getProductId(), getMaxAmednId(req.getProposalNo()));
			
		} else {
			int	res = secondPageFirstTableSaveAruguments(req, req.getProductId(), getMaxAmednId(req.getProposalNo()));
			res = secondPageCommissionSaveAruguments(req, req.getProductId());
			
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> a = query1.from(TtrnRiskProposal.class);
			Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

			query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),a.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO"));			
			// maxEndRp
			Subquery<Long> maxEndRp = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> rps = maxEndRp.from(TtrnRiskProposal.class);
			maxEndRp.select(cb.max(rps.get("rskEndorsementNo")));
			Predicate y1 = cb.equal( rps.get("rskProposalNumber"), req.getProposalNo());
			maxEndRp.where(y1);
			// maxEndRd
			Subquery<Long> maxEndRd = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> rds = maxEndRd.from(TtrnRiskDetails.class);
			maxEndRd.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate z1 = cb.equal( rds.get("rskProposalNumber"), req.getProposalNo());
			maxEndRd.where(z1);
			

			// Where
			Predicate x1 = cb.equal(a.get("rskProposalNumber"),b.get("rskProposalNumber"));
			Predicate x2 = cb.equal(a.get("rskProposalNumber"), req.getProposalNo());
			Predicate x3 = cb.equal(a.get("rskEndorsementNo"), maxEndRp);
			Predicate x4 = cb.equal(b.get("rskEndorsementNo"), maxEndRd);
			query1.where(x1,x2,x3,x4);
			
			// Get Result
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			List<Tuple> list = res2.getResultList();
			
			if(list.size()>0) {		
				Tuple resMap = list.get(0);
				res1.setProStatus(resMap.get("RSK_STATUS") == null ? "" : resMap.get("RSK_STATUS").toString());
				res1.setSharSign(resMap.get("RSK_SHARE_SIGNED") == null ? "" : resMap.get("RSK_SHARE_SIGNED").toString());
				res1.setContractNo(resMap.get("RSK_CONTRACT_NO") == null ? "" : resMap.get("RSK_CONTRACT_NO").toString());
			}
			final String HomePosition = getproposalStatus(req.getProposalNo());
			res1.setProStatus(HomePosition);
			if (HomePosition.equalsIgnoreCase("P")) {
				res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : " + req.getProposalNo());
			} else if (HomePosition.equalsIgnoreCase("A")) {
				res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : " + req.getProposalNo());
			} else if (HomePosition.equalsIgnoreCase("R")) {
				res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : " + req.getProposalNo());
			} else if (HomePosition.equalsIgnoreCase("N")) {
				res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : " +req.getProposalNo());
			} else if (HomePosition.equalsIgnoreCase("0")) {
				res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : " + req.getProposalNo());
			}
		}
//		insertRetroCess(beanObj);
//		InsertRemarkDetails(beanObj);
//		insertCrestaMaintable(beanObj);
//		beanObj.setProduct_id(productId);
//		insertBonusDetails(beanObj,"scale");
//		insertBonusDetails(beanObj,"lossparticipate");
//		insertProfitCommissionMain(beanObj,"main");
//		if ("5".equalsIgnoreCase(productId)) {
//			instalMentPremium(beanObj);
//		}
		response.setCommonResponse(res1);	
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
}
public int checkSecondPageMode(final String proposalNo) {
	int mode=0;
	try{
		Long selectCount = cessRepo.countByProposalNo(proposalNo);
		if (selectCount == 0) {
			mode = 1;
		} else if (selectCount != 0) {
			mode = 2;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return mode;
}
public int contractEditMode(final String proposalNo) {
	int mode=0;
	try {
	CriteriaBuilder cb = em.getCriteriaBuilder(); 
	CriteriaQuery<String> query1 = cb.createQuery(String.class); 
	
	Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);

	query1.select(pm.get("rskContractNo"));
			

	// MAXAmend ID
	Subquery<Long> maxEnd = query1.subquery(Long.class); 
	Root<TtrnRiskDetails> pms = maxEnd.from(TtrnRiskDetails.class);
	maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
	Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
	maxEnd.where(y1);

	// Where
	Predicate x1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
	Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
	query1.where(x1,x2);
	
	// Get Result
	TypedQuery<String> res2 = em.createQuery(query1);
	List<String> list = res2.getResultList();
	String string = "";
	if(list.size()>0) {
		string = list.get(0)==null?"0":list.get(0);
	}
	if ("0".equalsIgnoreCase(string)) {
		mode = 1;
	} else {
		mode = 2;
	}
	}catch(Exception e){
	e.printStackTrace();
	}
	return mode;
}
public int saveUpdateRiskDetailsSecondForm(final SaveSecondPageReq beanObj, final String productId, final String endNo) {
		TtrnRiskProposal entity = rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		try {
	if ("4".equalsIgnoreCase(productId)) {
		entity.setRskLimitOsOc(new BigDecimal(beanObj.getLimitOurShare()));
		entity.setRskLimitOsDc(new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
		entity.setRskEpiOsofOc(new BigDecimal(beanObj.getEpiAsPerOffer()));	
		entity.setRskEpiOsofDc( new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
				.getExchangeRate())));	
		entity.setRskEpiOsoeOc(StringUtils.isBlank(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerShare()));
		entity.setRskEpiOsoeDc(new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate())));
		entity.setRskXlcostOsOc(new BigDecimal(beanObj.getXlcostOurShare()));	
		entity.setRskXlcostOsDc(new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumQuotaShare(StringUtils.isBlank(beanObj.getPremiumQuotaShare())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumQuotaShare()));	
		entity.setRskPremiumSurpuls(StringUtils.isBlank(beanObj.getPremiumSurplus())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumSurplus()));	
		entity.setRskPremiumQuotaShareDc(StringUtils.isEmpty(beanObj.getPremiumQuotaShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumSurplusDc(StringUtils.isEmpty(beanObj.getPremiumSurplus())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumSurplus(), beanObj	.getExchangeRate())));	
		entity.setCommQsAmt(StringUtils.isBlank(beanObj.getCommissionQSAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQSAmt()));	
		entity.setCommSurplusAmt(StringUtils.isBlank(beanObj.getCommissionsurpAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurpAmt()));
		entity.setCommQsAmtDc(StringUtils.isEmpty(beanObj.getCommissionQSAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj.getExchangeRate())));	
		entity.setCommSurplusAmtDc(StringUtils.isEmpty(beanObj.getCommissionsurpAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj.getExchangeRate())));
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));	
		count = 1;
		} else if ("5".equalsIgnoreCase(productId)) {
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getMdpremiumourservice()));
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate())))	;
//			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//			entity.setRskEndorsementNo(new BigDecimal(endNo));	
			count = 1;
	}
	rpRepo.save(entity);
}catch(Exception e)
{
	e.printStackTrace();
	}
	return count;
}
public int secondPageFirstTableSaveAruguments(final SaveSecondPageReq beanObj, final String productId, final String endNo) {
	TtrnRiskProposal entity = rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
	int count = 0;
	try {
if ("4".equalsIgnoreCase(productId)) {
	entity.setRskLimitOsOc(new BigDecimal(beanObj.getLimitOurShare()));
	entity.setRskLimitOsDc(new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
	entity.setRskEpiOsofOc(new BigDecimal(beanObj.getEpiAsPerOffer()));	
	entity.setRskEpiOsofDc( new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
			.getExchangeRate())));	
	entity.setRskEpiOsoeOc(StringUtils.isBlank(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerShare()));
	entity.setRskEpiOsoeDc(new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate())));
	entity.setRskXlcostOsOc(new BigDecimal(beanObj.getXlcostOurShare()));	
	entity.setRskXlcostOsDc(new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate())));	
	entity.setRskPremiumQuotaShare(StringUtils.isBlank(beanObj.getPremiumQuotaShare())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumQuotaShare()));	
	entity.setRskPremiumSurpuls(StringUtils.isBlank(beanObj.getPremiumSurplus())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumSurplus()));	
	entity.setRskPremiumQuotaShareDc(StringUtils.isEmpty(beanObj.getPremiumQuotaShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj.getExchangeRate())));	
	entity.setRskPremiumSurplusDc(StringUtils.isEmpty(beanObj.getPremiumSurplus())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumSurplus(), beanObj	.getExchangeRate())));	
	entity.setCommQsAmt(StringUtils.isBlank(beanObj.getCommissionQSAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQSAmt()));	
	entity.setCommSurplusAmt(StringUtils.isBlank(beanObj.getCommissionsurpAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurpAmt()));
	entity.setCommQsAmtDc(StringUtils.isEmpty(beanObj.getCommissionQSAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj.getExchangeRate())));	
	entity.setCommSurplusAmtDc(StringUtils.isEmpty(beanObj.getCommissionsurpAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj.getExchangeRate())));
//	entity.setRskProposalNumber(beanObj.getProposalNo());
//	entity.setRskEndorsementNo(new BigDecimal(endNo));	
	
	count = 1;
	} else if ("5".equalsIgnoreCase(productId)) {
		entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLimitOurShare()));
		entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
		entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerOffer()));
		entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));
		entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getMdpremiumourservice()));
		entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate())))	;
//		entity.setRskProposalNumber(beanObj.getProposalNo());
		entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//		entity.setRskEndorsementNo(new BigDecimal(endNo));	
		
		count = 1;
		}
rpRepo.save(entity);
	}catch(Exception e){
		e.printStackTrace();
	}
return count;
}
private String getMaxproposalStatus(String proposalNo) {
	String result="";
	try{
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<String> query1 = cb.createQuery(String.class); 
		
		Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);

		query1.select(pm.get("rskStatus"));
			
		// MAXAmend ID
		Subquery<Long> maxEnd = query1.subquery(Long.class); 
		Root<TtrnRiskDetails> pms = maxEnd.from(TtrnRiskDetails.class);
		maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
		Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
		maxEnd.where(y1);

		// Where
		Predicate x1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
		Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
		query1.where(x1,x2);
		
		// Get Result
		TypedQuery<String> res2 = em.createQuery(query1);
		List<String> list = res2.getResultList();
		if(list.size()>0) {
			result = list.get(0)==null?"0":list.get(0);
		}
	}catch(Exception e){
		e.printStackTrace();
		}
	return result;
}
	public int savemodeUpdateRiskDetailsSecondFormSecondTable(final SaveSecondPageReq beanObj, final String productId, final String endNo) {
		TtrnRiskCommission entity = rcRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count =0;
		try {
		if (productId.equalsIgnoreCase("4")) {
			entity.setRskBrokerage(StringUtils.isEmpty(beanObj.getBrokerage()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getBrokerage()));		
			entity.setRskTax(new BigDecimal(beanObj.getTax()));			
			entity.setRskShareProfitCommission(beanObj.getShareProfitCommission());
			entity.setRskAcquistionCostOc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getAcquisitionCost()));
			entity.setRskAcquistionCostDc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getAcquisitionCost(), beanObj.getExchangeRate())));
			entity.setRskCommQuotashare(StringUtils.isEmpty(beanObj.getCommissionQS()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQS()));	
			entity.setRskCommSurplus(StringUtils.isEmpty(beanObj.getCommissionsurp()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurp()));		
			entity.setRskOverriderPerc(new BigDecimal(beanObj.getOverRidder()));	
			entity.setRskPremiumReserve(new BigDecimal(beanObj.getPremiumReserve()));	
			entity.setRskLossReserve(new BigDecimal(beanObj.getLossreserve()));	
			entity.setRskInterest(new BigDecimal(beanObj.getInterest()));	
			entity.setRskPfInoutPrem(StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutPremium()));	
			entity.setRskPfInoutLoss(StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutLoss()));	
			entity.setRskLossadvice(beanObj.getLossAdvise());
			entity.setRskCashlossLmtOc(new BigDecimal(beanObj.getCashLossLimit()));		
			entity.setRskCashlossLmtDc(new BigDecimal(getDesginationCountry(beanObj.getCashLossLimit(), beanObj.getExchangeRate())));	
			entity.setRskLeadUw(beanObj.getLeaderUnderwriter());
			entity.setRskLeadUwShare(new BigDecimal(beanObj.getLeaderUnderwritershare()));	
			entity.setRskAccounts(beanObj.getAccounts());
			entity.setRskExclusion(beanObj.getExclusion());
			entity.setRskRemarks(beanObj.getRemarks());		
			entity.setRskUwRecomm(beanObj.getUnderwriterRecommendations());		
			entity.setRskGmApproval(beanObj.getGmsApproval());		
			entity.setRskOtherCost(StringUtils.isEmpty(beanObj.getOthercost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOthercost()));	
			entity.setRskCreastaStatus(beanObj.getCrestaStatus());	
			entity.setRskEventLimitOc(new BigDecimal(beanObj.getEventlimit()));
			entity.setRskEventLimitDc(new BigDecimal(getDesginationCountry(beanObj.getEventlimit(), beanObj.getExchangeRate())));
			entity.setRskAggregateLimitOc(new BigDecimal(beanObj.getAggregateLimit()));
			entity.setRskAggregateLimitDc(new BigDecimal(getDesginationCountry(beanObj.getAggregateLimit(), beanObj.getExchangeRate())));
			entity.setRskOccurrentLimitOc(new BigDecimal(beanObj.getOccurrentLimit()));	
			entity.setRskOccurrentLimitDc(new BigDecimal(getDesginationCountry(beanObj.getOccurrentLimit(), beanObj.getExchangeRate())));	
			entity.setRskSladscaleComm(beanObj.getSlideScaleCommission());
			entity.setRskLossPartCarridor(beanObj.getLossParticipants());	
			entity.setRskCombinSubClass(StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "" : beanObj.getCommissionSubClass());
			entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));	
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskLeadUnderwriterCountry(StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "" : beanObj.getLeaderUnderwritercountry());
			entity.setRskIncludeAcqCost(StringUtils.isEmpty(beanObj.getOrginalacqcost()) ? "" : beanObj.getOrginalacqcost());
			entity.setRskOurAssAcqCost(StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost()) ? "" : beanObj.getOurassessmentorginalacqcost());
			entity.setRskOurAcqOurShareOc(StringUtils.isEmpty(beanObj.getOuracqCost()) ? "" : beanObj.getOuracqCost());	
			entity.setRskLossCombinSubClass(StringUtils.isEmpty(beanObj.getLosscommissionSubClass()) ? "" : beanObj.getLosscommissionSubClass());		
			entity.setRskSlideCombinSubClass(StringUtils.isEmpty(beanObj.getSlidecommissionSubClass()) ? "" : beanObj.getSlidecommissionSubClass());		
			entity.setRskCrestaCombinSubClass(StringUtils.isEmpty(beanObj.getCrestacommissionSubClass()) ? "" : beanObj.getCrestacommissionSubClass());
	
		if ("1".equalsIgnoreCase(beanObj.getShareProfitCommission())) {
			entity.setRskManagementExpenses(StringUtils.isEmpty(beanObj.getManagementExpenses()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getManagementExpenses()));
			entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getCommissionType()) ? "" : beanObj.getCommissionType());
			entity.setRskProCommPer(StringUtils.isEmpty(beanObj.getProfitCommissionPer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getProfitCommissionPer()));
			entity.setRskProSetUp(StringUtils.isEmpty(beanObj.getSetup()) ? "" : beanObj.getSetup());
			entity.setRskProSupProCom(StringUtils.isEmpty(beanObj.getSuperProfitCommission()) ? "" : beanObj.getSuperProfitCommission());			
			entity.setRskProLossCaryType(StringUtils.isEmpty(beanObj.getLossCarried()) ? "" : beanObj.getLossCarried());
			entity.setRskProLossCaryYear(StringUtils.isEmpty(beanObj.getLossyear()) ? "" : beanObj.getLossyear());
			entity.setRskProProfitCaryType(StringUtils.isEmpty(beanObj.getProfitCarried()) ? "" : beanObj.getProfitCarried());	
			entity.setRskProProfitCaryYear(StringUtils.isEmpty(beanObj.getProfitCarriedForYear()) ? "" : beanObj.getProfitCarriedForYear());	
			entity.setRskProFirstPfoCom(StringUtils.isEmpty(beanObj.getFistpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getFistpc()));
			entity.setRskProFirstPfoComPrd(StringUtils.isEmpty(beanObj.getProfitMont()) ? "" : beanObj.getProfitMont());
			entity.setRskProSubPfoComPrd(StringUtils.isEmpty(beanObj.getSubProfitMonth()) ? "" : beanObj.getSubProfitMonth());
			entity.setRskProSubPfoCom(StringUtils.isEmpty(beanObj.getSubpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getSubpc()));	
			entity.setRskProSubSeqCal(StringUtils.isEmpty(beanObj.getSubSeqCalculation()) ? "" : beanObj.getSubSeqCalculation());
			entity.setRskProNotes(StringUtils.isEmpty(beanObj.getProfitCommission()) ? "" : beanObj.getProfitCommission());
		} else {
			entity.setRskManagementExpenses(BigDecimal.ZERO );
			entity.setRskCommissionType("");
			entity.setRskProCommPer(BigDecimal.ZERO );
			entity.setRskProSetUp("");
			entity.setRskProSupProCom("");
			entity.setRskProLossCaryType("");
			entity.setRskProLossCaryYear("");
			entity.setRskProProfitCaryType("");
			entity.setRskProProfitCaryYear("");
			entity.setRskProFirstPfoCom(BigDecimal.ZERO );
			entity.setRskProFirstPfoComPrd("");
			entity.setRskProSubPfoComPrd("");
			entity.setRskProSubPfoCom(BigDecimal.ZERO );
			entity.setRskProSubSeqCal("");
			entity.setRskProNotes("");
		}
		entity.setRskDocStatus(StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus());	
		entity.setRskCommissionQsYn(StringUtils.isEmpty(beanObj.getCommissionQSYN())?"":beanObj.getCommissionQSYN());	
		entity.setRskCommissionSurYn(StringUtils.isEmpty(beanObj.getCommissionsurpYN())?"":beanObj.getCommissionsurpYN());
		entity.setRskOverrideYn(StringUtils.isEmpty(beanObj.getOverRidderYN())?"":beanObj.getOverRidderYN());
		entity.setRskBrokarageYn(StringUtils.isEmpty(beanObj.getBrokerageYN())?"":beanObj.getBrokerageYN());
		entity.setRskTaxYn(StringUtils.isEmpty(beanObj.getTaxYN())?"":beanObj.getTaxYN());
		entity.setRskOtherCostYn(StringUtils.isEmpty(beanObj.getOthercostYN())?"":beanObj.getOthercostYN());	
		entity.setRskCeedOdiYn(StringUtils.isEmpty(beanObj.getCeedODIYN())?"":beanObj.getCeedODIYN());
		entity.setRskRate(StringUtils.isEmpty(beanObj.getLocRate())?"":beanObj.getLocRate());
		entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getRetroCommissionType())?"":beanObj.getRetroCommissionType());	
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));
	
	}else if (productId.equalsIgnoreCase("5")) {
		entity.setRskReinstateNo(StringUtils.isEmpty(beanObj.getReinstNo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstNo()));		
		entity.setRskReinstateAddlPremPct(StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstAditionalPremiumpercent()));
		entity.setRskBurningCostPct(StringUtils.isEmpty(beanObj.getBurningCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getBurningCost()));
		entity.setRskRemarks(beanObj.getRemarks());
//		entity.setRskProposalNumber(beanObj.getProposalNo());
		entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//		entity.setRskEndorsementNo(new BigDecimal(endNo));
	}
		rcRepo.save(entity);
		count = 1;
		}catch(Exception e){
		e.printStackTrace();
		}
	return count;
}
	public int secondPageCommissionSaveAruguments(final SaveSecondPageReq beanObj, final String productId) {
		int count = 0;
		TtrnRiskCommission entity = new TtrnRiskCommission();
		if ("4".equalsIgnoreCase(productId)) {
			entity.setRskProposalNumber(beanObj.getProposalNo());		
			entity.setRskEndorsementNo(BigDecimal.ZERO);
			entity.setRskLayerNo(BigDecimal.ZERO);
			entity.setRskBrokerage(StringUtils.isEmpty(beanObj.getBrokerage()) ? BigDecimal.ZERO :new BigDecimal( beanObj.getBrokerage()));
			entity.setRskTax(StringUtils.isEmpty(beanObj.getTax()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getTax()));
			entity.setRskProfitComm(StringUtils.isEmpty(beanObj.getShareProfitCommission()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getShareProfitCommission()));
			entity.setRskReserveOnLoss(BigDecimal.ZERO);
			entity.setRskAcquistionCostOc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getAcquisitionCost()));	
			entity.setRskAcquistionCostDc(StringUtils.isEmpty(beanObj.getAcquisitionCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getAcquisitionCost(),beanObj.getExchangeRate())));	
			entity.setRskCommQuotashare(StringUtils.isEmpty(beanObj.getCommissionQS()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQS()));
			entity.setRskCommSurplus(StringUtils.isEmpty(beanObj.getCommissionsurp()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurp()));
			entity.setRskOverriderPerc(StringUtils.isEmpty(beanObj.getOverRidder()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOverRidder()));
			entity.setRskPremiumReserve(StringUtils.isEmpty(beanObj.getPremiumReserve()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumReserve()));
			entity.setRskLossReserve(StringUtils.isEmpty(beanObj.getLossreserve()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLossreserve()));
			entity.setRskInterest(StringUtils.isEmpty(beanObj.getInterest()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getInterest()));
			entity.setRskPfInoutPrem(StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutPremium()));		
			entity.setRskPfInoutLoss(StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutLoss()));
			entity.setRskLossadvice(StringUtils.isEmpty(beanObj.getLossAdvise()) ?"":(beanObj.getLossAdvise()));
			entity.setRskCashlossLmtOc(StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getCashLossLimit()));
			entity.setRskCashlossLmtDc(StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCashLossLimit(),beanObj.getExchangeRate())));	
			entity.setRskLeadUw(StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "":(beanObj.getLeaderUnderwriter()));
			entity.setRskLeadUwShare(StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLeaderUnderwritershare()));
			entity.setRskAccounts(StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts());	
			entity.setRskExclusion(StringUtils.isEmpty(beanObj.getExclusion()) ? "": beanObj.getExclusion());
			entity.setRskRemarks(StringUtils.isEmpty(beanObj.getRemarks()) ? "" : beanObj.getRemarks());
			entity.setRskUwRecommendation(StringUtils.isEmpty(beanObj.getUnderwriterRecommendations()) ? "" : beanObj.getUnderwriterRecommendations());	
			entity.setRskGmApproval(StringUtils.isEmpty(beanObj.getGmsApproval()) ? "": beanObj.getGmsApproval());
			entity.setRskDecision("");
			entity.setRskStatus("");	
			entity.setRskOtherCost(StringUtils.isEmpty(beanObj.getOthercost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOthercost()));
			entity.setRskCreastaStatus(beanObj.getCrestaStatus());
			entity.setRskEventLimitOc(new BigDecimal(beanObj.getEventlimit()));	
			entity.setRskEventLimitDc(new BigDecimal(getDesginationCountry(beanObj.getEventlimit(),beanObj.getExchangeRate())));
			entity.setRskAggregateLimitOc(new BigDecimal(beanObj.getAggregateLimit()));
			entity.setRskAggregateLimitDc(new BigDecimal(getDesginationCountry(beanObj.getAggregateLimit(),beanObj.getExchangeRate())));
			entity.setRskOccurrentLimitOc(new BigDecimal(beanObj.getOccurrentLimit()));
			entity.setRskOccurrentLimitDc(new BigDecimal(getDesginationCountry(beanObj.getOccurrentLimit(),beanObj.getExchangeRate())));
			entity.setRskSladscaleComm(beanObj.getSlideScaleCommission());
			entity.setRskLossPartCarridor(beanObj.getLossParticipants());
			entity.setRskCombinSubClass(StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "": beanObj.getCommissionSubClass());	
			entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));	
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskLeadUnderwriterCountry(StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "" : beanObj.getLeaderUnderwritercountry());
			entity.setRskIncludeAcqCost(StringUtils.isEmpty(beanObj.getOrginalacqcost()) ? "" : beanObj.getOrginalacqcost());
			entity.setRskOurAssAcqCost(StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost()) ? "" : beanObj.getOurassessmentorginalacqcost());
			entity.setRskOurAcqOurShareOc(StringUtils.isEmpty(beanObj.getOuracqCost()) ? "" : beanObj.getOuracqCost());	
			entity.setRskLossCombinSubClass(StringUtils.isEmpty(beanObj.getLosscommissionSubClass()) ? "" : beanObj.getLosscommissionSubClass());		
			entity.setRskSlideCombinSubClass(StringUtils.isEmpty(beanObj.getSlidecommissionSubClass()) ? "" : beanObj.getSlidecommissionSubClass());		
			entity.setRskCrestaCombinSubClass(StringUtils.isEmpty(beanObj.getCrestacommissionSubClass()) ? "" : beanObj.getCrestacommissionSubClass());
	
			if("1".equalsIgnoreCase(beanObj.getShareProfitCommission())){
				entity.setRskManagementExpenses(StringUtils.isEmpty(beanObj.getManagementExpenses()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getManagementExpenses()));
				entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getCommissionType()) ? "" : beanObj.getCommissionType());
				entity.setRskProCommPer(StringUtils.isEmpty(beanObj.getProfitCommissionPer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getProfitCommissionPer()));
				entity.setRskProSetUp(StringUtils.isEmpty(beanObj.getSetup()) ? "" : beanObj.getSetup());
				entity.setRskProSupProCom(StringUtils.isEmpty(beanObj.getSuperProfitCommission()) ? "" : beanObj.getSuperProfitCommission());			
				entity.setRskProLossCaryType(StringUtils.isEmpty(beanObj.getLossCarried()) ? "" : beanObj.getLossCarried());
				entity.setRskProLossCaryYear(StringUtils.isEmpty(beanObj.getLossyear()) ? "" : beanObj.getLossyear());
				entity.setRskProProfitCaryType(StringUtils.isEmpty(beanObj.getProfitCarried()) ? "" : beanObj.getProfitCarried());	
				entity.setRskProProfitCaryYear(StringUtils.isEmpty(beanObj.getProfitCarriedForYear()) ? "" : beanObj.getProfitCarriedForYear());	
				entity.setRskProFirstPfoCom(StringUtils.isEmpty(beanObj.getFistpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getFistpc()));
				entity.setRskProFirstPfoComPrd(StringUtils.isEmpty(beanObj.getProfitMont()) ? "" : beanObj.getProfitMont());
				entity.setRskProSubPfoComPrd(StringUtils.isEmpty(beanObj.getSubProfitMonth()) ? "" : beanObj.getSubProfitMonth());
				entity.setRskProSubPfoCom(StringUtils.isEmpty(beanObj.getSubpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getSubpc()));	
				entity.setRskProSubSeqCal(StringUtils.isEmpty(beanObj.getSubSeqCalculation()) ? "" : beanObj.getSubSeqCalculation());
				entity.setRskProNotes(StringUtils.isEmpty(beanObj.getProfitCommission()) ? "" : beanObj.getProfitCommission());
			
			}
			else{
				entity.setRskManagementExpenses(BigDecimal.ZERO );
				entity.setRskCommissionType("");
				entity.setRskProCommPer(BigDecimal.ZERO );
				entity.setRskProSetUp("");
				entity.setRskProSupProCom("");
				entity.setRskProLossCaryType("");
				entity.setRskProLossCaryYear("");
				entity.setRskProProfitCaryType("");
				entity.setRskProProfitCaryYear("");
				entity.setRskProFirstPfoCom(BigDecimal.ZERO );
				entity.setRskProFirstPfoComPrd("");
				entity.setRskProSubPfoComPrd("");
				entity.setRskProSubPfoCom(BigDecimal.ZERO );
				entity.setRskProSubSeqCal("");
				entity.setRskProNotes("");
			}
			entity.setRskDocStatus(StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus());	
			entity.setRskCommissionQsYn(StringUtils.isEmpty(beanObj.getCommissionQSYN())?"":beanObj.getCommissionQSYN());	
			entity.setRskCommissionSurYn(StringUtils.isEmpty(beanObj.getCommissionsurpYN())?"":beanObj.getCommissionsurpYN());
			entity.setRskOverrideYn(StringUtils.isEmpty(beanObj.getOverRidderYN())?"":beanObj.getOverRidderYN());
			entity.setRskBrokarageYn(StringUtils.isEmpty(beanObj.getBrokerageYN())?"":beanObj.getBrokerageYN());
			entity.setRskTaxYn(StringUtils.isEmpty(beanObj.getTaxYN())?"":beanObj.getTaxYN());
			entity.setRskOtherCostYn(StringUtils.isEmpty(beanObj.getOthercostYN())?"":beanObj.getOthercostYN());	
			entity.setRskCeedOdiYn(StringUtils.isEmpty(beanObj.getCeedODIYN())?"":beanObj.getCeedODIYN());
			entity.setRskRate(StringUtils.isEmpty(beanObj.getLocRate())?"":beanObj.getLocRate());
			entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getRetroCommissionType())?"":beanObj.getRetroCommissionType());	
			count = 1;
			
		}else if ("5".equalsIgnoreCase(productId)) {
			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskEndorsementNo( BigDecimal.ZERO);
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));			
			entity.setRskReinstateNo(StringUtils.isEmpty(beanObj.getReinstNo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstNo()));		
			entity.setRskReinstateAddlPremPct(StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstAditionalPremiumpercent()));
			entity.setRskBurningCostPct(StringUtils.isEmpty(beanObj.getBurningCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getBurningCost()));
			entity.setRskRemarks(beanObj.getRemarks());
			count = 1;
		}
	rcRepo.save(entity);
	return count;
	}
	private String getproposalStatus(final String proposalNo) {
		String result="";
		try{
			TtrnRiskDetails list = rdRepo.findByRskProposalNumber(proposalNo);
		if(list != null) {
			result =	list.getRskStatus();
		}
		}catch(Exception e){
			e.printStackTrace();
			}
		return result;
	}
	public int getCrestaCount(String proposal, String amend, String branchCode) {
		int count = 0;
		try {
			count = cresRepo.countByProposalNoAndAmendIdAndBranchCode(new BigDecimal(proposal),amend,branchCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public int getBonusListCount(SaveSecondPageReq bean, String type) {
		int result = 0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			
			// like table name
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);

			// Select
			query.select(pm.get("lcbFrom")); 

			// Where
			Predicate n1 = cb.equal(pm.get("proposalNo"), new BigDecimal(bean.getProposalNo()));
			Predicate n2 = cb.equal(pm.get("branch"), bean.getBranchCode());
			Predicate n3 = cb.equal(pm.get("type"), type);
			Predicate n4 = cb.equal(pm.get("endorsementNo"), new BigDecimal(bean.getEndorsementNo()));
			Predicate n5 = cb.equal(pm.get("layerNo"), bean.getLayerNo());
			query.where(n1,n2,n3,n4,n5);
			
			// Get Result
			TypedQuery<Long> res = em.createQuery(query);
			List<Long> result1 = res.getResultList();
			
			if(result1.size()>0) {
				for(int i=0;i<result1.size();i++) {
					result = result1.get(i) != null? (result+1) : result;
						}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int CommissionTypeCount( String proposal, String branchCode, String commitionType) {
		int count = 0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			
			// like table name
			Root<TtrnCommissionDetails> pm = query.from(TtrnCommissionDetails.class);

			// Select
			query.multiselect(cb.count(pm)); 
			
			// maxEnd
			Subquery<Long> maxEnd = query.subquery(Long.class); 
			Root<TtrnCommissionDetails> pms = maxEnd.from(TtrnCommissionDetails.class);
			maxEnd.select(cb.max(pms.get("endorsementNo")));
			Predicate y1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate y2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			maxEnd.where(y1,y2);

			// Where
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposal);
			Predicate n2 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(pm.get("commissionType"), commitionType);
			Predicate n4 = cb.equal(pm.get("endorsementNo"),maxEnd);
			query.where(n1,n2,n3,n4);
			
			// Get Result
			TypedQuery<Long> res = em.createQuery(query);
			List<Long> result1 = res.getResultList();
			count =	result1.get(0).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Transactional
	@Override
	public CommonResponse insertRetroCess(InsertRetroCessReq req) {
		CommonResponse response = new CommonResponse();
		try{
			int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
			String endtNo= dropDowmImpl.getRiskComMaxAmendId(req.getProposalNo());
			
			//Delete data
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnRetroCessionary> delete = cb.createCriteriaDelete(TtrnRetroCessionary.class);
			
			Root<TtrnRetroCessionary> rp = delete.from(TtrnRetroCessionary.class);
			
			//Where
			Predicate n1 = cb.equal(rp.get("proposalNo"), req.getProposalNo());
			Predicate n2 = cb.equal(rp.get("amendId"), endtNo);
			delete.where(n1,n2);
			em.createQuery(delete).executeUpdate();
			
			for(int i=0;i<NoRetroCess;i++){
				getRetroCessArgs(req,i,endtNo,true);
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
	public void getRetroCessArgs(InsertRetroCessReq beanObj,int i,String endtNo,boolean mode) {
		TtrnRetroCessionary entity = new TtrnRetroCessionary();
		InsertRetroCessReq1 req = beanObj.getInsertRetroCessReq1().get(i);
		try{
				entity.setSno(new BigDecimal(i));	
				entity.setProposalNo(beanObj.getProposalNo());		
				entity.setContractNo(StringUtils.isBlank(beanObj.getContNo())?"0":beanObj.getContNo());
				entity.setCedingCompanyId(StringUtils.isEmpty(req.getCedingCompany())?BigDecimal.ZERO :new BigDecimal(req.getCedingCompany()));
				entity.setBrokerId(StringUtils.isEmpty(req.getRetroBroker())?BigDecimal.ZERO :new BigDecimal(req.getRetroBroker()));
				entity.setShareAccepted(StringUtils.isEmpty(req.getShareAccepted())?BigDecimal.ZERO :new BigDecimal(req.getShareAccepted()));
				entity.setShareSigned(StringUtils.isEmpty(req.getShareSigned())?BigDecimal.ZERO :new BigDecimal(req.getShareSigned()));
				entity.setComission(StringUtils.isEmpty(req.getCommission())?BigDecimal.ZERO :new BigDecimal(req.getCommission()));
				entity.setProposalStatus(StringUtils.isEmpty(req.getProposalStatus())?"0":req.getProposalStatus().equalsIgnoreCase("0")?"P":req.getProposalStatus());
				entity.setAmendId(new BigDecimal(endtNo));
				entity.setLayerNo(StringUtils.isEmpty(beanObj.getLayerNo())?BigDecimal.ZERO :new BigDecimal(beanObj.getLayerNo()));
				entity.setLoginId(beanObj.getLoginId());
				entity.setBranchCode(beanObj.getBranchCode());
				cessRepo.save(entity);			
		}catch(Exception e){
			e.printStackTrace();
			}
		
	}
	@Override
	public CommonResponse insertCrestaMaintable(InsertCrestaMaintableReq bean) {
		CommonResponse response = new CommonResponse();
		TtrnCrestazoneDetails entity = new TtrnCrestazoneDetails();
		try {
			if(StringUtils.isBlank(bean.getAmendId())){
				bean.setAmendId("0");
			}
			int count=getCrestaCount(bean.getProposalNo(),bean.getAmendId(),bean.getBranchCode());
					if(count<=0){
						entity.setContractNo(new BigDecimal(bean.getContractNo()));
						entity.setProposalNo(new BigDecimal(bean.getProposalNo()));
						entity.setAmendId(bean.getAmendId());
						entity.setSubClass(bean.getDepartmentId());
						entity.setEntryDate(new Date());
						entity.setStatus("Y");
						entity.setBranchCode(bean.getBranchCode());
						entity.setTerritoryCode(bean.getBranchCode());
						cresRepo.save(entity);	
					}
					TtrnCrestazoneDetails uEntity = cresRepo.findByProposalNoAndAmendIdAndBranchCode(new BigDecimal(bean.getProposalNo()),bean.getAmendId(),bean.getBranchCode());
					uEntity.setContractNo(new BigDecimal(bean.getContractNo()));
					uEntity.setProposalNo(new BigDecimal(bean.getProposalNo()));
					uEntity.setAmendId(bean.getAmendId());
					uEntity.setTerritoryCode(bean.getBranchCode());
					cresRepo.save(uEntity);			
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
	public CommonResponse insertBonusDetails(InsertBonusDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
//		if(StringUtils.isBlank(beanObj.getContractNo()) && StringUtils.isNotBlank(beanObj.getContNo()) ){
//			beanObj.setContractNo(beanObj.getContNo());
//		}
		try{
			beanObj.setPageFor(beanObj.getType());
					if("scale".equalsIgnoreCase(beanObj.getPageFor()) && "Y".equalsIgnoreCase(beanObj.getSlideScaleCommission()) ){
						updateContractDetails(beanObj,"scale");
					}
					else if("Y".equalsIgnoreCase(beanObj.getLossParticipants()) && "lossparticipate".equalsIgnoreCase(beanObj.getPageFor())){
						updateContractDetails(beanObj,"lossparticipate");
					}
					else{
						moveBonusEmptyData(beanObj);
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
	private void updateContractDetails(InsertBonusDetailsReq bean, String string) {
		TtrnCrestazoneDetails entity = cresRepo.findByProposalNoAndAmendIdAndBranchCode(new BigDecimal(bean.getProposalNo()),bean.getAmendId(),bean.getBranchCode());
		try{
			entity.setContractNo(new BigDecimal(bean.getContractNo()));
//			entity.setProposalNo(new BigDecimal(bean.getProposalNo()));
//			entity.setBranchCode(bean.getBranchCode());
			cresRepo.save(entity);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Transactional
	private void moveBonusEmptyData(InsertBonusDetailsReq bean) {
		TtrnBonus entity = new TtrnBonus();
		try{
			if(StringUtils.isBlank(bean.getAmendId())){
				bean.setAmendId("0");
			}
			deleteMaintable(bean);
			entity.setContractNo(new BigDecimal(bean.getContractNo()));
			entity.setProposalNo(new BigDecimal(bean.getProposalNo()));
			entity.setProductId(bean.getProductId());
			entity.setUserId(bean.getLoginId());
			entity.setBranch(bean.getBranchCode());
			entity.setSysDate(new Date());
			if("scale".equalsIgnoreCase(bean.getPageFor())){
		        	   entity.setType("SSC");
		           }
		           else{
		        	   entity.setType("LPC");
		           }
		           entity.setEndorsementNo(new BigDecimal(bean.getAmendId()));   
		           entity.setSubClass(bean.getDepartmentId());	
		           entity.setLayerNo(StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo());	
		           bonusRepo.save(entity);	
	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Transactional
	private void deleteMaintable(InsertBonusDetailsReq bean) {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			if("".equalsIgnoreCase(bean.getEndorsmentno())){
				 if("scale".equalsIgnoreCase(bean.getPageFor())){
					 bean.setType("SSC");
		           }
		           else{
		        	   bean.setType("LPC");
		           }
				 bean.setLayerNo(StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo());	
				//Delete data
					
					CriteriaDelete<TtrnBonus> delete = cb.createCriteriaDelete(TtrnBonus.class);
					
					Root<TtrnBonus> rp = delete.from(TtrnBonus.class);
					
					//Where
					Predicate n1 = cb.equal(rp.get("proposalNo"), bean.getProposalNo());
					Predicate n2 = cb.equal(rp.get("branch"), bean.getBranchCode());
					Predicate n3 = cb.equal(rp.get("type"), bean.getType());
					Predicate n4 = cb.equal(rp.get("layerNo"), bean.getLayerNo());
					delete.where(n1,n2,n3,n4);
					em.createQuery(delete).executeUpdate();
			//	 bonusRepo.deleteByProposalNoAndBranchAndTypeAndLayerNo(new BigDecimal(bean.getProposalNo()),bean.getBranchCode(),bean.getType(),bean.getLayerNo());			
			}
			else{
				 if("scale".equalsIgnoreCase(bean.getPageFor())){
					 bean.setType("SSC");
		           }
		           else{
		        	   bean.setType("LPC");
		           }
				 bean.setLayerNo(StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo());	
				 CriteriaDelete<TtrnBonus> delete = cb.createCriteriaDelete(TtrnBonus.class);
					
					Root<TtrnBonus> rp = delete.from(TtrnBonus.class);
					
					//Where
					Predicate n1 = cb.equal(rp.get("proposalNo"), bean.getProposalNo());
					Predicate n5 = cb.equal(rp.get("endorsementNo"), bean.getAmendId());
					Predicate n2 = cb.equal(rp.get("branch"), bean.getBranchCode());
					Predicate n3 = cb.equal(rp.get("type"), bean.getType());
					Predicate n4 = cb.equal(rp.get("layerNo"), bean.getLayerNo());
					delete.where(n1,n2,n3,n4,n5);
					em.createQuery(delete).executeUpdate();
			//	 bonusRepo.deleteByProposalNoAndEndorsementNoAndBranchAndTypeAndLayerNo(new BigDecimal(bean.getProposalNo()),new BigDecimal(bean.getAmendId()),bean.getBranchCode(),bean.getType(),bean.getLayerNo());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public CommonResponse insertProfitCommissionMain(InsertProfitCommissionMainReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
		if(!"1".equalsIgnoreCase(beanObj.getShareProfitCommission())){
			mainDelete(beanObj);
			profitMainEmptyInsert(beanObj);
		}
	profitUpdate(beanObj);
		
	response.setMessage("Success");
	response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	}
	return response;
	}
	
	private void mainDelete(InsertProfitCommissionMainReq bean) {
		try{
		if(StringUtils.isNotBlank(bean.getContractNo())){
			 commRepo.deleteByProposalNoAndBranchCodeAndEndorsementNoAndContractNo(new BigDecimal(bean.getProposalNo()),bean.getBranchCode(),new BigDecimal(bean.getAmendId()),new BigDecimal(bean.getContractNo()));
		}
		else{
			TtrnCommissionDetails list = commRepo.findByProposalNoAndBranchCodeAndEndorsementNo(new BigDecimal(bean.getProposalNo()),bean.getBranchCode(),new BigDecimal(bean.getAmendId()));	
			
		//	commRepo.deleteByProposalNoAndBranchCodeAndEndorsementNo(new BigDecimal(bean.getProposalNo()),bean.getBranchCode(),new BigDecimal(bean.getAmendId()));
			commRepo.delete(list);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void profitMainEmptyInsert(InsertProfitCommissionMainReq bean) {
		TtrnCommissionDetails entity = new TtrnCommissionDetails();
		try{
			entity.setProposalNo(new BigDecimal(bean.getProposalNo()));	
			entity.setContractNo(new BigDecimal(bean.getContractNo()));
			entity.setEndorsementNo(new BigDecimal(bean.getAmendId()));
			entity.setProductId(bean.getProductId());
			entity.setBranchCode(bean.getBranchCode());
			entity.setSubClass(bean.getDepartmentId());
			entity.setCommissionType(bean.getCommissionType());
			entity.setLoginId(bean.getLoginId());		
			commRepo.save(entity);
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	private void profitUpdate(InsertProfitCommissionMainReq bean) {
		TtrnCommissionDetails entity =commRepo.findByProposalNoAndBranchCodeAndEndorsementNo(new BigDecimal(bean.getProposalNo()),bean.getBranchCode(),new BigDecimal(bean.getAmendId()));	
		try{
		//entity.setProposalNo(new BigDecimal(bean.getProposalNo()));	
			entity.setContractNo(new BigDecimal(bean.getContractNo()));
		//	entity.setEndorsementNo(new BigDecimal(bean.getAmendId()));
		//	entity.setBranchCode(bean.getBranchCode());
			entity.setProfitComStatus(bean.getShareProfitCommission());	
			commRepo.save(entity);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public CommonResponse instalMentPremium(InstalMentPremiumReq beanObj) {
		CommonResponse response = new CommonResponse();
		TtrnMndInstallments entity = new TtrnMndInstallments();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try{
			final String InstallmentPeriod = beanObj.getMdInstalmentNumber();
			String endtNo= dropDowmImpl.getRiskComMaxAmendId(beanObj.getProposalNo());
			int number=Integer.parseInt(InstallmentPeriod);
			for (int i = 0; i < number; i++) {
				InstalmentperiodReq req = beanObj.getInstalmentperiodReq().get(i);
				boolean modeOfInsertion = dropDowmImpl.getMode(beanObj.getProposalNo(),i+1,endtNo,1);
				if (modeOfInsertion) {
					entity.setInstallmentNo(new BigDecimal(i + 1));
					entity.setProposalNo( beanObj.getProposalNo());
					entity.setContractNo(StringUtils.isEmpty(beanObj.getContNo()) ? "0"	: beanObj.getContNo());
					entity.setLayerNo(new BigDecimal(beanObj.getLayerNo()));				
					entity.setEndorsementNo(new BigDecimal(endtNo));
					entity.setInstallmentDate(StringUtils.isEmpty(req.getInstalmentDateList()) ? null : sdf.parse(req.getInstalmentDateList()));
					entity.setMndPremiumOc(StringUtils.isEmpty(req.getInstallmentPremium()) ? BigDecimal.ZERO: new BigDecimal(req.getInstallmentPremium()));
					entity.setMndPremiumDc(StringUtils.isEmpty(req.getInstallmentPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO: new BigDecimal(getDesginationCountry(req.getInstallmentPremium(), beanObj.getExchangeRate())));
					entity.setStatus("Y");
					entity.setEntryDate(new Date());
					mndRepo.save(entity);				
				}else {
					TtrnMndInstallments UEntity = mndRepo.findByInstallmentNoAndProposalNoAndEndorsementNo(new BigDecimal(i + 1),beanObj.getProposalNo(),new BigDecimal(endtNo));
					UEntity.setContractNo(StringUtils.isEmpty(beanObj.getContNo()) ? "0"	: beanObj.getContNo());
					UEntity.setLayerNo(new BigDecimal(beanObj.getLayerNo()));
					UEntity.setInstallmentDate(StringUtils.isEmpty(req.getInstalmentDateList()) ? null : sdf.parse(req.getInstalmentDateList()));
					UEntity.setMndPremiumOc(StringUtils.isEmpty(req.getInstallmentPremium()) ? BigDecimal.ZERO: new BigDecimal(req.getInstallmentPremium()));
					UEntity.setMndPremiumDc(StringUtils.isEmpty(req.getInstallmentPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO: new BigDecimal(getDesginationCountry(req.getInstallmentPremium(), beanObj.getExchangeRate())));
//					UEntity.setInstallmentNo();
//					UEntity.setProposalNo( beanObj.getProposalNo());
//					UEntity.setEndorsementNo(new BigDecimal(endtNo));
					mndRepo.save(UEntity);		
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
	@Override
	public SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(SaveSecondPageReq beanObj) {
		SaveRiskDeatilsSecondFormRes response = new SaveRiskDeatilsSecondFormRes();
		SaveRiskDeatilsSecondFormRes1 res1 = new SaveRiskDeatilsSecondFormRes1();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			int out=0;
			int chkSecPageMode = checkSecondPageMode(beanObj.getProposalNo());
			int ContractEditMode = contractEditMode(beanObj.getProposalNo());
			if (ContractEditMode == 1) {
				if (chkSecPageMode == 1) {
					out = secondPageFirstTableAruguments(beanObj, beanObj.getProductId(),getMaxAmednId(beanObj.getProposalNo()));
				
					out = secondPageCommissionAruguments(beanObj,beanObj.getProductId());
				
					CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
					Root<TtrnRiskProposal> a = query1.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

					query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),a.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO"));			
					// maxEndRp
					Subquery<Long> maxEndRp = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> rps = maxEndRp.from(TtrnRiskProposal.class);
					maxEndRp.select(cb.max(rps.get("rskEndorsementNo")));
					Predicate y1 = cb.equal( rps.get("rskProposalNumber"), beanObj.getProposalNo());
					maxEndRp.where(y1);
					// maxEndRd
					Subquery<Long> maxEndRd = query1.subquery(Long.class); 
					Root<TtrnRiskDetails> rds = maxEndRd.from(TtrnRiskDetails.class);
					maxEndRd.select(cb.max(rds.get("rskEndorsementNo")));
					Predicate z1 = cb.equal( rds.get("rskProposalNumber"), beanObj.getProposalNo());
					maxEndRd.where(z1);

					// Where
					Predicate x1 = cb.equal(a.get("rskProposalNumber"),b.get("rskProposalNumber"));
					Predicate x2 = cb.equal(a.get("rskProposalNumber"), beanObj.getProposalNo());
					Predicate x3 = cb.equal(a.get("rskEndorsementNo"), maxEndRp);
					Predicate x4 = cb.equal(b.get("rskEndorsementNo"), maxEndRd);
					query1.where(x1,x2,x3,x4);
					
					// Get Result
					TypedQuery<Tuple> res2 = em.createQuery(query1);
					List<Tuple> list = res2.getResultList();
					
					if(list.size()>0) {		
						Tuple resMap = list.get(0);
						res1.setProStatus(resMap.get("RSK_STATUS") == null ? "" : resMap.get("RSK_STATUS").toString());
						res1.setSharSign(resMap.get("RSK_SHARE_SIGNED") == null ? "" : resMap.get("RSK_SHARE_SIGNED").toString());
						res1.setContNo(resMap.get("RSK_CONTRACT_NO") == null ? "" : resMap.get("RSK_CONTRACT_NO").toString());
					
						if (res1.getProStatus().matches("A")&& !res1.getSharSign().matches("0")) {
							String maxContarctNo = null;
							String prodid=beanObj.getProductId();
							if (beanObj.getLayerNo().equalsIgnoreCase("layer")) {
								maxContarctNo = beanObj.getContractNo();
							} else {
								
								String catagoryId="";
								if("SR".equalsIgnoreCase(beanObj.getRetroType())){
									catagoryId="9";//Fac Spc Retro
								}else{
									catagoryId="10";//Treaty Retro
								}
								List<ConstantDetail> repo = 	cdRepo.findByCategoryId(new BigDecimal(catagoryId));
							if(repo.size()>0) {
								prodid = repo.get(0).getType();
								}
							
								if(!"".equals(beanObj.getRenewalcontractno())&&!"0".equals(beanObj.getRenewalcontractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									maxContarctNo=beanObj.getRenewalcontractno();
								}else{
									
									maxContarctNo = fm.getSequence("Contract", "SR".equalsIgnoreCase(beanObj.getRetroType()) ? "5" : "4", beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear());
								}
							}
							//RISK_UPDATE_CONTNO
							TtrnRiskDetails rdentity = rdRepo.findByRskProposalNumber(beanObj.getProposalNo());
							rdentity.setRskContractNo(maxContarctNo);
							//rdentity.setRskProposalNumber(beanObj.getProposalNo());			
							rdRepo.save(rdentity);
							//RISK_UPDATE_HOMECONTNO
							PositionMaster pmEntity = pmRepo.findByProposalNo(new BigDecimal(beanObj.getProposalNo()));
							pmEntity.setContractNo(new BigDecimal(maxContarctNo));
							pmEntity.setProposalStatus("A");
							pmEntity.setContractStatus("A");
						//	pmEntity.setProposalNo(new BigDecimal(beanObj.getProposalNo()));	
							pmRepo.save(pmEntity);
							
							if("".equals(beanObj.getRenewalcontractno())||"0".equals(beanObj.getRenewalcontractno())||"NEWCONTNO".equals(beanObj.getRenewalFlag())){
								res1.setContractGendration("Your Proposal is converted to Contract with Proposal No : "+beanObj.getProposalNo() +" and Contract No : "+maxContarctNo+".");
							}else{
								res1.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
							}
						} else {
							String proposalStatus = getproposalStatus(beanObj.getProposalNo());
								if (proposalStatus.equalsIgnoreCase("P")) {
									if (beanObj.getProductId().equalsIgnoreCase("4")) {
										res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
									}
									if ("5".equalsIgnoreCase(beanObj.getProductId())) {
										res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No. :"+beanObj.getLayerNo());
									}
								}	if (proposalStatus.equalsIgnoreCase("N")) {
									if (beanObj.getProductId().equalsIgnoreCase("2")||beanObj.getProductId().equalsIgnoreCase("4")) {
										res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
									}
									if ("5".equalsIgnoreCase(beanObj.getProductId())) {
										res1.setContractGendration("Your Proposal is saved in  Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No. :"+beanObj.getLayerNo());
									}
								}  else if (proposalStatus.equalsIgnoreCase("A")) {
									if ("4".equalsIgnoreCase(beanObj.getProductId())) {
										res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+beanObj.getContractNo()+".");
									}
									if ("5".equalsIgnoreCase(beanObj.getProductId())) {
										res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+beanObj.getContractNo()+" and Layer No : "+beanObj.getLayerNo()+".");
									}
								} else if (proposalStatus.equalsIgnoreCase("R")) {
									res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
								}
							PositionMaster pmEntity = pmRepo.findByProposalNo(new BigDecimal(beanObj.getProposalNo()));
							pmEntity.setContractNo(new BigDecimal(beanObj.getContractNo()));
							pmEntity.setProposalStatus(proposalStatus);
							pmEntity.setContractStatus(proposalStatus);
						//	pmEntity.setProposalNo(new BigDecimal(beanObj.getProposalNo()));	
							pmRepo.save(pmEntity);
						}
					}
				}else if (chkSecPageMode == 2) {
					out =updateRiskDetailsSecondForm(beanObj, beanObj.getProductId(),getMaxAmednId(beanObj.getProposalNo()));
					out = updateRiskDetailsSecondFormSecondTable(beanObj, beanObj.getProductId(),getMaxAmednId(beanObj.getProposalNo()));
					
					CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
					Root<TtrnRiskProposal> a = query1.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);

					query1.multiselect(b.get("rskStatus").alias("RSK_STATUS"),a.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),b.get("rskContractNo").alias("RSK_CONTRACT_NO"));			
					// maxEndRp
					Subquery<Long> maxEndRp = query1.subquery(Long.class); 
					Root<TtrnRiskProposal> rps = maxEndRp.from(TtrnRiskProposal.class);
					maxEndRp.select(cb.max(rps.get("rskEndorsementNo")));
					Predicate y1 = cb.equal( rps.get("rskProposalNumber"), beanObj.getProposalNo());
					maxEndRp.where(y1);
					// maxEndRd
					Subquery<Long> maxEndRd = query1.subquery(Long.class); 
					Root<TtrnRiskDetails> rds = maxEndRd.from(TtrnRiskDetails.class);
					maxEndRd.select(cb.max(rds.get("rskEndorsementNo")));
					Predicate z1 = cb.equal( rds.get("rskProposalNumber"), beanObj.getProposalNo());
					maxEndRd.where(z1);
					

					// Where
					Predicate x1 = cb.equal(a.get("rskProposalNumber"),b.get("rskProposalNumber"));
					Predicate x2 = cb.equal(a.get("rskProposalNumber"), beanObj.getProposalNo());
					Predicate x3 = cb.equal(a.get("rskEndorsementNo"), maxEndRp);
					Predicate x4 = cb.equal(b.get("rskEndorsementNo"), maxEndRd);
					query1.where(x1,x2,x3,x4);
					
					// Get Result
					TypedQuery<Tuple> res2 = em.createQuery(query1);
					List<Tuple> list = res2.getResultList();
					
					if(list.size()>0) {		
						Tuple resMap = list.get(0);
						res1.setProStatus(resMap.get("RSK_STATUS") == null ? "" : resMap.get("RSK_STATUS").toString());
						res1.setSharSign(resMap.get("RSK_SHARE_SIGNED") == null ? "" : resMap.get("RSK_SHARE_SIGNED").toString());
						res1.setContNo(resMap.get("RSK_CONTRACT_NO") == null ? "" : resMap.get("RSK_CONTRACT_NO").toString());
					}

					if (res1.getProStatus().matches("A")	&& !res1.getSharSign().matches("0")) {
						String prodid=beanObj.getProductId();
						
						String catagoryId="";
						if("SR".equalsIgnoreCase(beanObj.getRetroType())){
							catagoryId = "9";
						}else{
							catagoryId="10";
						}
						//RISK_SELECT_POLICYNOPRODCODE
						List<ConstantDetail> repo = cdRepo.findByCategoryId(new BigDecimal(catagoryId));
						if(repo.size()>0) {
							prodid = repo.get(0).getType();
							}
					
						String maxContarctNo = "";
						if(!"".equals(beanObj.getRenewalcontractno())&&!"0".equals(beanObj.getRenewalcontractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
							maxContarctNo = beanObj.getRenewalcontractno();
						}else{
						
							maxContarctNo = fm.getSequence("Contract", "SR".equalsIgnoreCase(beanObj.getRetroType()) ? "5" : "4", beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear());
						
						}
					
						//RISK_UPDATE_CONTNO
						TtrnRiskDetails rdentity = rdRepo.findByRskProposalNumber(beanObj.getProposalNo());
						rdentity.setRskContractNo(maxContarctNo);
					//	rdentity.setRskProposalNumber(beanObj.getProposalNo());			
						rdRepo.save(rdentity);
						//RISK_UPDATE_HOMECONTNO
						PositionMaster pmEntity = pmRepo.findByProposalNo(new BigDecimal(beanObj.getProposalNo()));
						pmEntity.setContractNo(new BigDecimal(maxContarctNo));
						pmEntity.setProposalStatus("A");
						pmEntity.setContractStatus("A");
					//	pmEntity.setProposalNo(new BigDecimal(beanObj.getProposalNo()));	
						pmRepo.save(pmEntity);
					
						if("".equals(beanObj.getRenewalcontractno())||"0".equals(beanObj.getRenewalcontractno())||"NEWCONTNO".equals(beanObj.getRenewalFlag())){
							if (beanObj.getProductId().equalsIgnoreCase("4")) {
								res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+maxContarctNo+".");
							}
							if ("5".equalsIgnoreCase(beanObj.getProductId())) {
								res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+maxContarctNo+" and Layer No.: "+beanObj.getLayerNo()+".");
							}
						}else	{
							if (beanObj.getProductId().equalsIgnoreCase("4")) {
								res1.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
							}
							if ("5".equalsIgnoreCase(beanObj.getProductId())) {
								res1.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+",New Contract No : "+maxContarctNo+" and Layer No.: "+beanObj.getLayerNo()+".");
							}
						}
						//RISK_UPDATE_MNDINSTALLMENTS
						TtrnMndInstallments entity = mndRepo.findByProposalNo(beanObj.getProposalNo());
						entity.setContractNo(maxContarctNo);			
					//	entity.setProposalNo(beanObj.getProposalNo());				
						mndRepo.save(entity);		
						
						beanObj.setContractNo(maxContarctNo);
					} else if (res1.getProStatus().matches("P")) {
						if (beanObj.getProductId().equalsIgnoreCase("4")) {
							res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo());
						}
						if ("5".equalsIgnoreCase(beanObj.getProductId())) {
							res1.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");
						}
					}else if (res1.getProStatus().matches("N")) {
						if (beanObj.getProductId().equalsIgnoreCase("4")) {
							res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo());
						}
						if ("5".equalsIgnoreCase(beanObj.getProductId())) {
							res1.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");
						}
					}
					else if (res1.getProStatus().matches("R")) {
						res1.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");
					}
				}
			}
			else if (ContractEditMode == 2) {
				
				if(StringUtils.isBlank(beanObj.getContractNo())&&"Renewal".equals(beanObj.getReMode())){
					beanObj.setContractNo(fm.getSequence("Contract", "SR".equalsIgnoreCase(beanObj.getRetroType()) ? "5" : "4", beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear()));
					//RISK_UPDATE_CONTNO
					TtrnRiskDetails rdentity = rdRepo.findByRskProposalNumber(beanObj.getProposalNo());
					rdentity.setRskContractNo(beanObj.getContractNo());
				//	rdentity.setRskProposalNumber(beanObj.getProposalNo());				
					rdRepo.save(rdentity);
				
					//RISK_UPDATE_HOMECONTNO
					PositionMaster pmEntity = pmRepo.findByProposalNo(new BigDecimal(beanObj.getProposalNo()));
					pmEntity.setContractNo(new BigDecimal(beanObj.getContractNo()));
					pmEntity.setProposalStatus(getMaxproposalStatus(beanObj.getProposalNo()));
					pmEntity.setContractStatus(getMaxproposalStatus(beanObj.getProposalNo()));
					pmRepo.save(pmEntity);
			
				}
				//RISK_SELECT_ENDO
				TtrnRiskProposal  res= rpRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(beanObj.getProposalNo());
				String endtNo="";
				 if (res != null) {
					 endtNo =String.valueOf(res.getRskEndorsementNo());
					} 
				out = updateContractRiskDetailsSecondForm(beanObj, beanObj.getProductId(),endtNo);
				
				
			
				if (beanObj.getProductId().equalsIgnoreCase("4")) {
					res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+beanObj.getContractNo()+".");
				} else if ("5".equalsIgnoreCase(beanObj.getProductId())) {
					res1.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+beanObj.getContractNo()+" and Layer No : "+beanObj.getLayerNo()+".");
				}
				//RISK_UPDATE_PRO4SECCOMM, RISK_UPDATE_PRO5SECCOMM
				out = updateRiskDetailsSecondFormSecondTable(beanObj, beanObj.getProductId(),getMaxAmednId(beanObj.getProposalNo()));
				res1.setProStatus("A");
			}
//			if("4".equalsIgnoreCase(beanObj.getProductId())){
//				insertRetroCess(beanObj);
//			}
//			if("5".equalsIgnoreCase(beanObj.getProductId())) {
//				instalMentPremium(beanObj);
//			}
//			insertCrestaMaintable(beanObj);
//			beanObj.setProduct_id(beanObj.getProductId());
//			insertBonusDetails(beanObj,"scale");
//			insertBonusDetails(beanObj,"lossparticipate");
//			insertProfitCommissionMain(beanObj,"main");
//			InsertRemarkDetails(beanObj);
//			DropDownControllor dropDownControllor = new DropDownControllor();
//			dropDowmImpl.updatepositionMasterEndtStatus(beanObj.getProposalNo(),beanObj.getProductId(),beanObj.getEndorsementDate(),"");
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
			return response;
	}

	public int secondPageFirstTableAruguments(final SaveSecondPageReq beanObj, final String productId , final String endNo) {
		TtrnRiskProposal entity =  rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		try {
	if ("4".equalsIgnoreCase(productId) || "2".equalsIgnoreCase(productId)) {
		entity.setRskLimitOsOc(new BigDecimal(beanObj.getLimitOurShare()));
		entity.setRskLimitOsDc(new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
		entity.setRskEpiOsofOc(new BigDecimal(beanObj.getEpiAsPerOffer()));	
		entity.setRskEpiOsofDc( new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));	
		entity.setRskEpiOsoeOc(StringUtils.isBlank(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerShare()));
		entity.setRskEpiOsoeDc(new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate())));
		entity.setRskXlcostOsOc(new BigDecimal(beanObj.getXlcostOurShare()));	
		entity.setRskXlcostOsDc(new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumQuotaShare(StringUtils.isBlank(beanObj.getPremiumQuotaShare())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumQuotaShare()));	
		entity.setRskPremiumSurpuls(StringUtils.isBlank(beanObj.getPremiumSurplus())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumSurplus()));	
		entity.setRskPremiumQuotaShareDc(StringUtils.isEmpty(beanObj.getPremiumQuotaShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumSurplusDc(StringUtils.isEmpty(beanObj.getPremiumSurplus())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumSurplus(), beanObj	.getExchangeRate())));	
		entity.setCommQsAmt(StringUtils.isBlank(beanObj.getCommissionQSAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQSAmt()));	
		entity.setCommSurplusAmt(StringUtils.isBlank(beanObj.getCommissionsurpAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurpAmt()));
		entity.setCommQsAmtDc(StringUtils.isEmpty(beanObj.getCommissionQSAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj.getExchangeRate())));	
		entity.setCommSurplusAmtDc(StringUtils.isEmpty(beanObj.getCommissionsurpAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj.getExchangeRate())));
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));	
		count = 1;
		} else if ("5".equalsIgnoreCase(productId) || "3".equalsIgnoreCase(productId)) {
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getMdpremiumourservice()));
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate())))	;
//			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//			entity.setRskEndorsementNo(new BigDecimal(endNo));	
			count = 1;
	}
	rpRepo.save(entity);
}catch(Exception e)
{
	e.printStackTrace();
	}
	return count;
}
	public int secondPageCommissionAruguments(final SaveSecondPageReq beanObj, final String productId) {
		int count = 0;
		TtrnRiskCommission entity = rcRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),BigDecimal.ZERO);
		if ("4".equalsIgnoreCase(productId)) {
//			entity.setRskProposalNumber(beanObj.getProposalNo());		
//			entity.setRskEndorsementNo(BigDecimal.ZERO);
			entity.setRskLayerNo(BigDecimal.ZERO);
			entity.setRskBrokerage(StringUtils.isEmpty(beanObj.getBrokerage()) ? BigDecimal.ZERO :new BigDecimal( beanObj.getBrokerage()));
			entity.setRskTax(StringUtils.isEmpty(beanObj.getTax()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getTax()));
			entity.setRskProfitComm(StringUtils.isEmpty(beanObj.getShareProfitCommission()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getShareProfitCommission()));
			entity.setRskReserveOnLoss(BigDecimal.ZERO);
			entity.setRskAcquistionCostOc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getAcquisitionCost()));	
			entity.setRskAcquistionCostDc(StringUtils.isEmpty(beanObj.getAcquisitionCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getAcquisitionCost(),beanObj.getExchangeRate())));	
			entity.setRskCommQuotashare(StringUtils.isEmpty(beanObj.getCommissionQS()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQS()));
			entity.setRskCommSurplus(StringUtils.isEmpty(beanObj.getCommissionsurp()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurp()));
			entity.setRskOverriderPerc(StringUtils.isEmpty(beanObj.getOverRidder()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOverRidder()));
			entity.setRskPremiumReserve(StringUtils.isEmpty(beanObj.getPremiumReserve()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumReserve()));
			entity.setRskLossReserve(StringUtils.isEmpty(beanObj.getLossreserve()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLossreserve()));
			entity.setRskInterest(StringUtils.isEmpty(beanObj.getInterest()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getInterest()));
			entity.setRskPfInoutPrem(StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutPremium()));		
			entity.setRskPfInoutLoss(StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutLoss()));
			entity.setRskLossadvice(StringUtils.isEmpty(beanObj.getLossAdvise()) ?"":(beanObj.getLossAdvise()));
			entity.setRskCashlossLmtOc(StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getCashLossLimit()));
			entity.setRskCashlossLmtDc(StringUtils.isEmpty(beanObj.getCashLossLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCashLossLimit(),beanObj.getExchangeRate())));	
			entity.setRskLeadUw(StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "":(beanObj.getLeaderUnderwriter()));
			entity.setRskLeadUwShare(StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLeaderUnderwritershare()));
			entity.setRskAccounts(StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts());	
			entity.setRskExclusion(StringUtils.isEmpty(beanObj.getExclusion()) ? "": beanObj.getExclusion());
			entity.setRskRemarks(StringUtils.isEmpty(beanObj.getRemarks()) ? "" : beanObj.getRemarks());
			entity.setRskUwRecommendation(StringUtils.isEmpty(beanObj.getUnderwriterRecommendations()) ? "" : beanObj.getUnderwriterRecommendations());	
			entity.setRskGmApproval(StringUtils.isEmpty(beanObj.getGmsApproval()) ? "": beanObj.getGmsApproval());
			entity.setRskDecision("");
			entity.setRskStatus("");	
			entity.setRskOtherCost(StringUtils.isEmpty(beanObj.getOthercost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOthercost()));
			entity.setRskCreastaStatus(beanObj.getCrestaStatus());
			entity.setRskEventLimitOc(new BigDecimal(beanObj.getEventlimit()));	
			entity.setRskEventLimitDc(new BigDecimal(getDesginationCountry(beanObj.getEventlimit(),beanObj.getExchangeRate())));
			entity.setRskAggregateLimitOc(new BigDecimal(beanObj.getAggregateLimit()));
			entity.setRskAggregateLimitDc(new BigDecimal(getDesginationCountry(beanObj.getAggregateLimit(),beanObj.getExchangeRate())));
			entity.setRskOccurrentLimitOc(new BigDecimal(beanObj.getOccurrentLimit()));
			entity.setRskOccurrentLimitDc(new BigDecimal(getDesginationCountry(beanObj.getOccurrentLimit(),beanObj.getExchangeRate())));
			entity.setRskSladscaleComm(beanObj.getSlideScaleCommission());
			entity.setRskLossPartCarridor(beanObj.getLossParticipants());
			entity.setRskCombinSubClass(StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "": beanObj.getCommissionSubClass());	
			entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));	
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskLeadUnderwriterCountry(StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "" : beanObj.getLeaderUnderwritercountry());
			entity.setRskIncludeAcqCost(StringUtils.isEmpty(beanObj.getOrginalacqcost()) ? "" : beanObj.getOrginalacqcost());
			entity.setRskOurAssAcqCost(StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost()) ? "" : beanObj.getOurassessmentorginalacqcost());
			entity.setRskOurAcqOurShareOc(StringUtils.isEmpty(beanObj.getOuracqCost()) ? "" : beanObj.getOuracqCost());	
			entity.setRskLossCombinSubClass(StringUtils.isEmpty(beanObj.getLosscommissionSubClass()) ? "" : beanObj.getLosscommissionSubClass());		
			entity.setRskSlideCombinSubClass(StringUtils.isEmpty(beanObj.getSlidecommissionSubClass()) ? "" : beanObj.getSlidecommissionSubClass());		
			entity.setRskCrestaCombinSubClass(StringUtils.isEmpty(beanObj.getCrestacommissionSubClass()) ? "" : beanObj.getCrestacommissionSubClass());
	
			if("1".equalsIgnoreCase(beanObj.getShareProfitCommission())){
				entity.setRskManagementExpenses(StringUtils.isEmpty(beanObj.getManagementExpenses()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getManagementExpenses()));
				entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getCommissionType()) ? "" : beanObj.getCommissionType());
				entity.setRskProCommPer(StringUtils.isEmpty(beanObj.getProfitCommissionPer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getProfitCommissionPer()));
				entity.setRskProSetUp(StringUtils.isEmpty(beanObj.getSetup()) ? "" : beanObj.getSetup());
				entity.setRskProSupProCom(StringUtils.isEmpty(beanObj.getSuperProfitCommission()) ? "" : beanObj.getSuperProfitCommission());			
				entity.setRskProLossCaryType(StringUtils.isEmpty(beanObj.getLossCarried()) ? "" : beanObj.getLossCarried());
				entity.setRskProLossCaryYear(StringUtils.isEmpty(beanObj.getLossyear()) ? "" : beanObj.getLossyear());
				entity.setRskProProfitCaryType(StringUtils.isEmpty(beanObj.getProfitCarried()) ? "" : beanObj.getProfitCarried());	
				entity.setRskProProfitCaryYear(StringUtils.isEmpty(beanObj.getProfitCarriedForYear()) ? "" : beanObj.getProfitCarriedForYear());	
				entity.setRskProFirstPfoCom(StringUtils.isEmpty(beanObj.getFistpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getFistpc()));
				entity.setRskProFirstPfoComPrd(StringUtils.isEmpty(beanObj.getProfitMont()) ? "" : beanObj.getProfitMont());
				entity.setRskProSubPfoComPrd(StringUtils.isEmpty(beanObj.getSubProfitMonth()) ? "" : beanObj.getSubProfitMonth());
				entity.setRskProSubPfoCom(StringUtils.isEmpty(beanObj.getSubpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getSubpc()));	
				entity.setRskProSubSeqCal(StringUtils.isEmpty(beanObj.getSubSeqCalculation()) ? "" : beanObj.getSubSeqCalculation());
				entity.setRskProNotes(StringUtils.isEmpty(beanObj.getProfitCommission()) ? "" : beanObj.getProfitCommission());
			
			}
			else{
				entity.setRskManagementExpenses(BigDecimal.ZERO );
				entity.setRskCommissionType("");
				entity.setRskProCommPer(BigDecimal.ZERO );
				entity.setRskProSetUp("");
				entity.setRskProSupProCom("");
				entity.setRskProLossCaryType("");
				entity.setRskProLossCaryYear("");
				entity.setRskProProfitCaryType("");
				entity.setRskProProfitCaryYear("");
				entity.setRskProFirstPfoCom(BigDecimal.ZERO );
				entity.setRskProFirstPfoComPrd("");
				entity.setRskProSubPfoComPrd("");
				entity.setRskProSubPfoCom(BigDecimal.ZERO );
				entity.setRskProSubSeqCal("");
				entity.setRskProNotes("");
			}
			entity.setRskDocStatus(StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus());	
			entity.setRskCommissionQsYn(StringUtils.isEmpty(beanObj.getCommissionQSYN())?"":beanObj.getCommissionQSYN());	
			entity.setRskCommissionSurYn(StringUtils.isEmpty(beanObj.getCommissionsurpYN())?"":beanObj.getCommissionsurpYN());
			entity.setRskOverrideYn(StringUtils.isEmpty(beanObj.getOverRidderYN())?"":beanObj.getOverRidderYN());
			entity.setRskBrokarageYn(StringUtils.isEmpty(beanObj.getBrokerageYN())?"":beanObj.getBrokerageYN());
			entity.setRskTaxYn(StringUtils.isEmpty(beanObj.getTaxYN())?"":beanObj.getTaxYN());
			entity.setRskOtherCostYn(StringUtils.isEmpty(beanObj.getOthercostYN())?"":beanObj.getOthercostYN());	
			entity.setRskCeedOdiYn(StringUtils.isEmpty(beanObj.getCeedODIYN())?"":beanObj.getCeedODIYN());
			entity.setRskRate(StringUtils.isEmpty(beanObj.getLocRate())?"":beanObj.getLocRate());
			entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getRetroCommissionType())?"":beanObj.getRetroCommissionType());	
			count = 1;
			
		}else if ("5".equalsIgnoreCase(productId)) {
//			entity.setRskProposalNumber(beanObj.getProposalNo());
//			entity.setRskEndorsementNo( BigDecimal.ZERO);
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));			
			entity.setRskReinstateNo(StringUtils.isEmpty(beanObj.getReinstNo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstNo()));		
			entity.setRskReinstateAddlPremPct(StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstAditionalPremiumpercent()));
			entity.setRskBurningCostPct(StringUtils.isEmpty(beanObj.getBurningCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getBurningCost()));
			entity.setRskRemarks(beanObj.getRemarks());
			count = 1;
		}
	rcRepo.save(entity);
	return count;
	}
	public int updateRiskDetailsSecondForm(final SaveSecondPageReq beanObj, final String productId , final String endNo) {
		TtrnRiskProposal entity =  rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		try {
	if ("4".equalsIgnoreCase(productId) ) {
		entity.setRskLimitOsOc(new BigDecimal(beanObj.getLimitOurShare()));
		entity.setRskLimitOsDc(new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
		entity.setRskEpiOsofOc(new BigDecimal(beanObj.getEpiAsPerOffer()));	
		entity.setRskEpiOsofDc( new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
				.getExchangeRate())));	
		entity.setRskEpiOsoeOc(StringUtils.isBlank(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerShare()));
		entity.setRskEpiOsoeDc(new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate())));
		entity.setRskXlcostOsOc(new BigDecimal(beanObj.getXlcostOurShare()));	
		entity.setRskXlcostOsDc(new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumQuotaShare(StringUtils.isBlank(beanObj.getPremiumQuotaShare())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumQuotaShare()));	
		entity.setRskPremiumSurpuls(StringUtils.isBlank(beanObj.getPremiumSurplus())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumSurplus()));	
		entity.setRskPremiumQuotaShareDc(StringUtils.isEmpty(beanObj.getPremiumQuotaShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumSurplusDc(StringUtils.isEmpty(beanObj.getPremiumSurplus())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumSurplus(), beanObj	.getExchangeRate())));	
		entity.setCommQsAmt(StringUtils.isBlank(beanObj.getCommissionQSAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQSAmt()));	
		entity.setCommSurplusAmt(StringUtils.isBlank(beanObj.getCommissionsurpAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurpAmt()));
		entity.setCommQsAmtDc(StringUtils.isEmpty(beanObj.getCommissionQSAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj.getExchangeRate())));	
		entity.setCommSurplusAmtDc(StringUtils.isEmpty(beanObj.getCommissionsurpAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj.getExchangeRate())));
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));	
		count = 1;
		} else if ("5".equalsIgnoreCase(productId) ) {
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getMdpremiumourservice()));
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate())))	;
//			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//			entity.setRskEndorsementNo(new BigDecimal(endNo));	
			count = 1;
	}
	rpRepo.save(entity);
		}catch(Exception e){
	e.printStackTrace();
	}
	return count;
}
	public int updateRiskDetailsSecondFormSecondTable(final SaveSecondPageReq beanObj, final String productId,final String endNo) {
		TtrnRiskCommission entity = rcRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count =0;	
		try {
		if (productId.equalsIgnoreCase("4")) {
			entity.setRskBrokerage(StringUtils.isEmpty(beanObj.getBrokerage()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getBrokerage()));		
			entity.setRskTax(new BigDecimal(beanObj.getTax()));			
			entity.setRskShareProfitCommission(beanObj.getShareProfitCommission());
			entity.setRskAcquistionCostOc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getAcquisitionCost()));
			entity.setRskAcquistionCostDc(StringUtils.isEmpty(beanObj.getAcquisitionCost()) || StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getAcquisitionCost(), beanObj.getExchangeRate())));
			entity.setRskCommQuotashare(StringUtils.isEmpty(beanObj.getCommissionQS()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQS()));	
			entity.setRskCommSurplus(StringUtils.isEmpty(beanObj.getCommissionsurp()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurp()));		
			entity.setRskOverriderPerc(new BigDecimal(beanObj.getOverRidder()));	
			entity.setRskPremiumReserve(new BigDecimal(beanObj.getPremiumReserve()));	
			entity.setRskLossReserve(new BigDecimal(beanObj.getLossreserve()));	
			entity.setRskInterest(new BigDecimal(beanObj.getInterest()));	
			entity.setRskPfInoutPrem(StringUtils.isEmpty(beanObj.getPortfolioinoutPremium()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutPremium()));	
			entity.setRskPfInoutLoss(StringUtils.isEmpty(beanObj.getPortfolioinoutLoss()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPortfolioinoutLoss()));	
			entity.setRskLossadvice(beanObj.getLossAdvise());
			entity.setRskCashlossLmtOc(new BigDecimal(beanObj.getCashLossLimit()));		
			entity.setRskCashlossLmtDc(new BigDecimal(getDesginationCountry(beanObj.getCashLossLimit(), beanObj.getExchangeRate())));	
			entity.setRskLeadUw(beanObj.getLeaderUnderwriter());
			entity.setRskLeadUwShare(new BigDecimal(beanObj.getLeaderUnderwritershare()));	
			entity.setRskAccounts(beanObj.getAccounts());
			entity.setRskExclusion(beanObj.getExclusion());
			entity.setRskRemarks(beanObj.getRemarks());		
			entity.setRskUwRecomm(beanObj.getUnderwriterRecommendations());		
			entity.setRskGmApproval(beanObj.getGmsApproval());		
			entity.setRskOtherCost(StringUtils.isEmpty(beanObj.getOthercost()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getOthercost()));	
			entity.setRskCreastaStatus(beanObj.getCrestaStatus());	
			entity.setRskEventLimitOc(new BigDecimal(beanObj.getEventlimit()));
			entity.setRskEventLimitDc(new BigDecimal(getDesginationCountry(beanObj.getEventlimit(), beanObj.getExchangeRate())));
			entity.setRskAggregateLimitOc(new BigDecimal(beanObj.getAggregateLimit()));
			entity.setRskAggregateLimitDc(new BigDecimal(getDesginationCountry(beanObj.getAggregateLimit(), beanObj.getExchangeRate())));
			entity.setRskOccurrentLimitOc(new BigDecimal(beanObj.getOccurrentLimit()));	
			entity.setRskOccurrentLimitDc(new BigDecimal(getDesginationCountry(beanObj.getOccurrentLimit(), beanObj.getExchangeRate())));	
			entity.setRskSladscaleComm(beanObj.getSlideScaleCommission());
			entity.setRskLossPartCarridor(beanObj.getLossParticipants());	
			entity.setRskCombinSubClass(StringUtils.isEmpty(beanObj.getCommissionSubClass()) ? "" : beanObj.getCommissionSubClass());
			entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));	
			entity.setLoginId(beanObj.getLoginId());
			entity.setBranchCode(beanObj.getBranchCode());
			entity.setRskLeadUnderwriterCountry(StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "" : beanObj.getLeaderUnderwritercountry());
			entity.setRskIncludeAcqCost(StringUtils.isEmpty(beanObj.getOrginalacqcost()) ? "" : beanObj.getOrginalacqcost());
			entity.setRskOurAssAcqCost(StringUtils.isEmpty(beanObj.getOurassessmentorginalacqcost()) ? "" : beanObj.getOurassessmentorginalacqcost());
			entity.setRskOurAcqOurShareOc(StringUtils.isEmpty(beanObj.getOuracqCost()) ? "" : beanObj.getOuracqCost());	
			entity.setRskLossCombinSubClass(StringUtils.isEmpty(beanObj.getLosscommissionSubClass()) ? "" : beanObj.getLosscommissionSubClass());		
			entity.setRskSlideCombinSubClass(StringUtils.isEmpty(beanObj.getSlidecommissionSubClass()) ? "" : beanObj.getSlidecommissionSubClass());		
			entity.setRskCrestaCombinSubClass(StringUtils.isEmpty(beanObj.getCrestacommissionSubClass()) ? "" : beanObj.getCrestacommissionSubClass());
	
		if ("1".equalsIgnoreCase(beanObj.getShareProfitCommission())) {
			entity.setRskManagementExpenses(StringUtils.isEmpty(beanObj.getManagementExpenses()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getManagementExpenses()));
			entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getCommissionType()) ? "" : beanObj.getCommissionType());
			entity.setRskProCommPer(StringUtils.isEmpty(beanObj.getProfitCommissionPer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getProfitCommissionPer()));
			entity.setRskProSetUp(StringUtils.isEmpty(beanObj.getSetup()) ? "" : beanObj.getSetup());
			entity.setRskProSupProCom(StringUtils.isEmpty(beanObj.getSuperProfitCommission()) ? "" : beanObj.getSuperProfitCommission());			
			entity.setRskProLossCaryType(StringUtils.isEmpty(beanObj.getLossCarried()) ? "" : beanObj.getLossCarried());
			entity.setRskProLossCaryYear(StringUtils.isEmpty(beanObj.getLossyear()) ? "" : beanObj.getLossyear());
			entity.setRskProProfitCaryType(StringUtils.isEmpty(beanObj.getProfitCarried()) ? "" : beanObj.getProfitCarried());	
			entity.setRskProProfitCaryYear(StringUtils.isEmpty(beanObj.getProfitCarriedForYear()) ? "" : beanObj.getProfitCarriedForYear());	
			entity.setRskProFirstPfoCom(StringUtils.isEmpty(beanObj.getFistpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getFistpc()));
			entity.setRskProFirstPfoComPrd(StringUtils.isEmpty(beanObj.getProfitMont()) ? "" : beanObj.getProfitMont());
			entity.setRskProSubPfoComPrd(StringUtils.isEmpty(beanObj.getSubProfitMonth()) ? "" : beanObj.getSubProfitMonth());
			entity.setRskProSubPfoCom(StringUtils.isEmpty(beanObj.getSubpc()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getSubpc()));	
			entity.setRskProSubSeqCal(StringUtils.isEmpty(beanObj.getSubSeqCalculation()) ? "" : beanObj.getSubSeqCalculation());
			entity.setRskProNotes(StringUtils.isEmpty(beanObj.getProfitCommission()) ? "" : beanObj.getProfitCommission());
		} else {
			entity.setRskManagementExpenses(BigDecimal.ZERO );
			entity.setRskCommissionType("");
			entity.setRskProCommPer(BigDecimal.ZERO );
			entity.setRskProSetUp("");
			entity.setRskProSupProCom("");
			entity.setRskProLossCaryType("");
			entity.setRskProLossCaryYear("");
			entity.setRskProProfitCaryType("");
			entity.setRskProProfitCaryYear("");
			entity.setRskProFirstPfoCom(BigDecimal.ZERO );
			entity.setRskProFirstPfoComPrd("");
			entity.setRskProSubPfoComPrd("");
			entity.setRskProSubPfoCom(BigDecimal.ZERO );
			entity.setRskProSubSeqCal("");
			entity.setRskProNotes("");
		}
		entity.setRskDocStatus(StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus());	
		entity.setRskCommissionQsYn(StringUtils.isEmpty(beanObj.getCommissionQSYN())?"":beanObj.getCommissionQSYN());	
		entity.setRskCommissionSurYn(StringUtils.isEmpty(beanObj.getCommissionsurpYN())?"":beanObj.getCommissionsurpYN());
		entity.setRskOverrideYn(StringUtils.isEmpty(beanObj.getOverRidderYN())?"":beanObj.getOverRidderYN());
		entity.setRskBrokarageYn(StringUtils.isEmpty(beanObj.getBrokerageYN())?"":beanObj.getBrokerageYN());
		entity.setRskTaxYn(StringUtils.isEmpty(beanObj.getTaxYN())?"":beanObj.getTaxYN());
		entity.setRskOtherCostYn(StringUtils.isEmpty(beanObj.getOthercostYN())?"":beanObj.getOthercostYN());	
		entity.setRskCeedOdiYn(StringUtils.isEmpty(beanObj.getCeedODIYN())?"":beanObj.getCeedODIYN());
		entity.setRskRate(StringUtils.isEmpty(beanObj.getLocRate())?"":beanObj.getLocRate());
		entity.setRskCommissionType(StringUtils.isEmpty(beanObj.getRetroCommissionType())?"":beanObj.getRetroCommissionType());	
	//	entity.setRskProposalNumber(beanObj.getProposalNo());
	//	entity.setRskEndorsementNo(new BigDecimal(endNo));
	
	}else if (productId.equalsIgnoreCase("5")) {
		entity.setRskReinstateNo(StringUtils.isEmpty(beanObj.getReinstNo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstNo()));		
		entity.setRskReinstateAddlPremPct(StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReinstAditionalPremiumpercent()));
		entity.setRskBurningCostPct(StringUtils.isEmpty(beanObj.getBurningCost()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getBurningCost()));
		entity.setRskRemarks(beanObj.getRemarks());
	//	entity.setRskProposalNumber(beanObj.getProposalNo());
		entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
	//	entity.setRskEndorsementNo(new BigDecimal(endNo));
	}
		rcRepo.save(entity);
		count = 1;
		}catch(Exception e){
		e.printStackTrace();
		}
	return count;
}
	public int updateContractRiskDetailsSecondForm(final SaveSecondPageReq beanObj, final String productId,final String endNo) {
		TtrnRiskProposal entity =  rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		try {
	if ("4".equalsIgnoreCase(productId)) {
		entity.setRskLimitOsOc(new BigDecimal(beanObj.getLimitOurShare()));
		entity.setRskLimitOsDc(new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
		entity.setRskEpiOsofOc(new BigDecimal(beanObj.getEpiAsPerOffer()));	
		entity.setRskEpiOsofDc( new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj
				.getExchangeRate())));	
		entity.setRskEpiOsoeOc(StringUtils.isBlank(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerShare()));
		entity.setRskEpiOsoeDc(new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchangeRate())));
		entity.setRskXlcostOsOc(new BigDecimal(beanObj.getXlcostOurShare()));	
		entity.setRskXlcostOsDc(new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumQuotaShare(StringUtils.isBlank(beanObj.getPremiumQuotaShare())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumQuotaShare()));	
		entity.setRskPremiumSurpuls(StringUtils.isBlank(beanObj.getPremiumSurplus())?BigDecimal.ZERO :new BigDecimal(beanObj.getPremiumSurplus()));	
		entity.setRskPremiumQuotaShareDc(StringUtils.isEmpty(beanObj.getPremiumQuotaShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumQuotaShare(), beanObj.getExchangeRate())));	
		entity.setRskPremiumSurplusDc(StringUtils.isEmpty(beanObj.getPremiumSurplus())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getPremiumSurplus(), beanObj	.getExchangeRate())));	
		entity.setCommQsAmt(StringUtils.isBlank(beanObj.getCommissionQSAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionQSAmt()));	
		entity.setCommSurplusAmt(StringUtils.isBlank(beanObj.getCommissionsurpAmt())?BigDecimal.ZERO :new BigDecimal(beanObj.getCommissionsurpAmt()));
		entity.setCommQsAmtDc(StringUtils.isEmpty(beanObj.getCommissionQSAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionQSAmt(), beanObj.getExchangeRate())));	
		entity.setCommSurplusAmtDc(StringUtils.isEmpty(beanObj.getCommissionsurpAmt())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getCommissionsurpAmt(), beanObj.getExchangeRate())));
//		entity.setRskProposalNumber(beanObj.getProposalNo());
//		entity.setRskEndorsementNo(new BigDecimal(endNo));	
		count = 1;
		} else if ("5".equalsIgnoreCase(productId)) {
			entity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getLimitOurShare()));
			entity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate())));
			entity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getEpiAsPerOffer()));
			entity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate())));
			entity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ?BigDecimal.ZERO :new BigDecimal(beanObj.getMdpremiumourservice()));
			entity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ?BigDecimal.ZERO :new BigDecimal(getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate())))	;
//			entity.setRskProposalNumber(beanObj.getProposalNo());
			entity.setRskLayerNo(new BigDecimal(beanObj.getLayerNo()));
//			entity.setRskEndorsementNo(new BigDecimal(endNo));	
			count = 1;
	}
	rpRepo.save(entity);
		}catch(Exception e){
	e.printStackTrace();
	}
	return count;
}
	@Override
	public ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo) {
		ShowLayerBrokerageRes response = new ShowLayerBrokerageRes();
		ShowLayerBrokerageRes1 res = new ShowLayerBrokerageRes1();
		try {
			if(StringUtils.isNotBlank(layerProposalNo)){
				//RISK_SELECT_GETBROKERAGE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				Root<TtrnRiskCommission> pm = query.from(TtrnRiskCommission.class);

				// Select
				query.multiselect(pm.get("rskBrokerage").alias("RSK_BROKERAGE"), pm.get("rskTax").alias("RSK_TAX")); 

				// maxEnd
				Subquery<Long> maxEnd = query.subquery(Long.class); 
				Root<TtrnRiskCommission> pms = maxEnd.from(TtrnRiskCommission.class);
				maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( pms.get("rskProposalNumber"), layerProposalNo);
				maxEnd.where(a1);

				// Where
				Predicate n1 = cb.equal(pm.get("rskProposalNumber"), layerProposalNo);
				Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
				query.where(n1,n2);

				// Get Result
				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				
				if(list!=null && list.size()>0) {
					Tuple	resMap = list.get(0);
				if(resMap!=null){
					res.setBrokerage(resMap.get("RSK_BROKERAGE")==null?"":resMap.get("RSK_BROKERAGE").toString());
					res.setTax(resMap.get("RSK_TAX")==null?"":resMap.get("RSK_TAX").toString());
				}
			} }
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
	public CommonSaveRes updateProportionalTreaty(UpdateProportionalTreatyReq beanObj) {
		CommonSaveRes response = new CommonSaveRes();
		boolean savFlg = false;
		try {
			String updateQry = "";
			int updateCount =  getFirstPageSaveModeAruguments(beanObj, beanObj.getProductId(),getMaxAmednId(beanObj.getProposalNo()));
		
			if (updateCount > 0) {
			//	this.updateRiskProposal(beanObj, pid);
//				InsertRemarkDetails(beanObj);
//				GetRemarksDetails(beanObj);
//				this.showSecondpageEditItems(beanObj, pid, beanObj.getProposal_no());
				savFlg = true;
			}
			response.setResponse("SaveFlag: " + String.valueOf(savFlg));
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	//RISK_UPDATE_RSKDTLS
	public int getFirstPageSaveModeAruguments(final UpdateProportionalTreatyReq beanObj, final String pid,String endNo) throws ParseException {
		TtrnRiskDetails entity = null;
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		int count =0;
		if (pid.equalsIgnoreCase("4")) {
			entity = rdRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
			
		} else if ("5".equalsIgnoreCase(pid)) {
			entity = rdRepo.findByRskProposalNumberAndRskEndorsementNoAndRskLayerNo(beanObj.getProposalNo(),new BigDecimal(endNo),new BigDecimal(beanObj.getLayerNo()));
		}
		entity.setRskDeptid(StringUtils.isEmpty(beanObj.getDepartmentId()) ? BigDecimal.ZERO :new BigDecimal( beanObj.getDepartmentId()));
		entity.setRskPfcid(StringUtils.isEmpty(beanObj.getProfitCenter()) ? "0" : beanObj.getProfitCenter());
		entity.setRskSpfcid(StringUtils.isEmpty(beanObj.getSubProfitCenter()) ? "0" : beanObj.getSubProfitCenter());
		entity.setRskPolbranch(StringUtils.isEmpty(beanObj.getPolicyBranch()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getPolicyBranch()));
		entity.setRskCedingid(StringUtils.isEmpty(beanObj.getCedingCo()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getCedingCo()));
		entity.setRskBrokerid(StringUtils.isEmpty(beanObj.getBroker()) ?  BigDecimal.ZERO :new BigDecimal(beanObj.getBroker()));
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyNameType()) ? "" : beanObj.getTreatyNameType());
		entity.setRskMonth(StringUtils.isEmpty(beanObj.getMonth()) ? null : sdf.parse(beanObj.getMonth()));
		entity.setRskUwyear(StringUtils.isEmpty(beanObj.getUwYear()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getUwYear()));
		entity.setRskUnderwritter(StringUtils.isEmpty(beanObj.getUnderwriter()) ? "" : beanObj.getUnderwriter());
		entity.setRskInceptionDate(StringUtils.isEmpty(beanObj.getIncepDate()) ?  null :  sdf.parse(beanObj.getIncepDate()));
		entity.setRskExpiryDate(StringUtils.isEmpty(beanObj.getExpDate()) ?null :  sdf.parse(beanObj.getExpDate()));
		entity.setRskAccountDate( StringUtils.isEmpty(beanObj.getAccDate()) ?  null :  sdf.parse(beanObj.getAccDate()));
		entity.setRskOriginalCurr(StringUtils.isEmpty(beanObj.getOrginalCurrency()) ? "0" : beanObj.getOrginalCurrency());
		entity.setRskExchangeRate(StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getExchRate()));
		
		if (pid.equalsIgnoreCase("4")) {
			entity.setRskBasis(StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis());
			entity.setRskPeriodOfNotice(StringUtils.isEmpty(beanObj.getPnoc()) ? "" : beanObj.getPnoc());
			entity.setRskRiskCovered(StringUtils.isEmpty(beanObj.getRiskCovered()) ? "" : beanObj.getRiskCovered());
		
		} else if ("5".equalsIgnoreCase(pid)) {
			entity.setRskBasis(StringUtils.isEmpty(beanObj.getBasis()) ? "0" : beanObj.getBasis());
			
		}
		entity.setRskTerritoryScope(StringUtils.isEmpty(beanObj.getTerritoryscope()) ? "" : beanObj.getTerritoryscope());
		entity.setRskTerritory(StringUtils.isEmpty(beanObj.getTerritory()) ? "" : beanObj.getTerritory());
		entity.setRskStatus(StringUtils.isEmpty(beanObj.getProStatus()) ? "" : beanObj.getProStatus());

		if (pid.equalsIgnoreCase("4")) {
			entity.setRskProposalType(StringUtils.isEmpty(beanObj.getProposalType()) ? "0"	: beanObj.getProposalType());
			entity.setRskAccountingPeriod(StringUtils.isEmpty(beanObj.getAccountingPeriod()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getAccountingPeriod()));
			entity.setRskReceiptStatement(StringUtils.isEmpty(beanObj.getReceiptofStatements()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReceiptofStatements()));
			entity.setRskReceiptPayement(StringUtils.isEmpty(beanObj.getReceiptofPayment()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getReceiptofPayment()));
			
		} else if ("5".equalsIgnoreCase(pid)) {
		
			entity.setRskAccountingPeriod( BigDecimal.ZERO );
			entity.setRskReceiptStatement(BigDecimal.ZERO);
			entity.setRskReceiptPayement(BigDecimal.ZERO);
		}
		entity.setMndInstallments(StringUtils.isEmpty(beanObj.getMdInstalmentNumber()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getMdInstalmentNumber()));
		entity.setRetroCessionaries(StringUtils.isEmpty(beanObj.getNoRetroCess()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getNoRetroCess()));
		entity.setRskRetroType(StringUtils.isEmpty(beanObj.getRetroType()) ? "0" : beanObj.getRetroType());
		entity.setRskInsuredName(StringUtils.isEmpty(beanObj.getInsuredName()) ? "" : beanObj.getInsuredName());
		entity.setInwardBusType(StringUtils.isEmpty(beanObj.getInwardType()) ? "0"	: beanObj.getInwardType());
		entity.setTreatytype(StringUtils.isEmpty(beanObj.getTreatyType()) ? "0"	: beanObj.getTreatyType());
		entity.setRskBusinessType(StringUtils.isEmpty(beanObj.getBusinessType()) ? ""	: beanObj.getBusinessType());
		entity.setRskExchangeType(StringUtils.isEmpty(beanObj.getExchangeType()) ? ""	: beanObj.getExchangeType());
		entity.setRskPerilsCovered(StringUtils.isEmpty(beanObj.getPerilCovered()) ? ""	: beanObj.getPerilCovered());
		entity.setRskLocIssued(StringUtils.isEmpty(beanObj.getLOCIssued()) ? ""	: beanObj.getLOCIssued());
		entity.setRskUmbrellaXl(StringUtils.isEmpty(beanObj.getUmbrellaXL()) ? ""	: beanObj.getUmbrellaXL());
		entity.setLoginId(beanObj.getLoginId());
		entity.setBranchCode(beanObj.getBranchCode());
		entity.setCountriesInclude(StringUtils.isEmpty(beanObj.getCountryIncludedList()) ? ""	:beanObj.getCountryIncludedList());
		entity.setCountriesExclude(StringUtils.isEmpty(beanObj.getCountryExcludedList()) ? ""	: beanObj.getCountryExcludedList());
		entity.setRskNoOfLine(StringUtils.isEmpty(beanObj.getTreatynoofLine()) ? ""	:beanObj.getTreatynoofLine().replaceAll(",", ""));
		entity.setRsEndorsementType(StringUtils.isEmpty(beanObj.getEndorsmenttype()) ? ""	:beanObj.getEndorsmenttype());
		entity.setRskRunOffYear(StringUtils.isEmpty(beanObj.getRunoffYear()) ? BigDecimal.ZERO :new BigDecimal(beanObj.getRunoffYear()));
		entity.setRskLocBnkName(StringUtils.isEmpty(beanObj.getLocBankName()) ? ""	:beanObj.getLocBankName());
		entity.setRskLocCrdtAmt(StringUtils.isEmpty(beanObj.getLocCreditAmt()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditAmt().replaceAll(",", "")));
		entity.setRskLocCrdtPrd(StringUtils.isEmpty(beanObj.getLocCreditPrd()) ? new BigDecimal("")	:new BigDecimal(beanObj.getLocCreditPrd()));
		entity.setRskLocBenfcreName(StringUtils.isEmpty(beanObj.getLocBeneficerName()) ? ""	:beanObj.getLocBeneficerName());
		entity.setRskCessionExgRate(StringUtils.isEmpty(beanObj.getCessionExgRate()) ? ""	:beanObj.getCessionExgRate());
		entity.setRskFixedRate(StringUtils.isEmpty(beanObj.getFixedRate()) ? new BigDecimal("")	:new BigDecimal(beanObj.getFixedRate()));
		entity.setRetentionyn(StringUtils.isEmpty(beanObj.getRetentionYN()) ? ""	:beanObj.getRetentionYN());
		count =1;
		rdRepo.save(entity);
		return count;
		}
	@Override
	public CommonSaveRes updateRiskProposal(FirstInsertReq beanObj) {
		CommonSaveRes response = new CommonSaveRes();
		boolean saveFlag = false;
		try {
			int res=0;
			String endtNo="";
			//RISK_SELECT_MAXENDOM  
			TtrnRiskProposal list =	rpRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(beanObj.getProposalNo());
			 if (list != null) {
				 endtNo =String.valueOf(list.getRskEndorsementNo());
				} 
			res= updateRiskProposalArgs(beanObj, beanObj.getProductId(),endtNo);
		
			if (res> 0) {
				saveFlag = true;
			}
			updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
			res = updateHomePostion(beanObj, beanObj.getProductId(),true);
			if (res > 0) {
				saveFlag = true;
			}
			 response.setResponse("SaveFlag: " + String.valueOf(saveFlag));
				response.setMessage("Success");
				response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	//RISK_UPDATE_PRO24FIRPAGERSKPRO, RISK_UPDATE_PRO35FIRPAGERSKPRO
	public int updateRiskProposalArgs(final FirstInsertReq beanObj,final String pid,String endNo) {
		TtrnRiskProposal rpEntity = rpRepo.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
		int count = 0;
		if (pid.equalsIgnoreCase("4")) {
			if("TR".equalsIgnoreCase(beanObj.getRetroType())){
			rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ?  BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ?  BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			}
			else{
				rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getFaclimitOrigCur()));
				rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getFaclimitOrigCur(), beanObj.getExchRate())));
				}
			rpEntity.setRskEpiOfferOc(StringUtils.isEmpty(beanObj.getEpiorigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiorigCur()));
			rpEntity.setRskEpiOfferDc(StringUtils.isEmpty(beanObj.getEpiorigCur()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiorigCur(), beanObj.getExchRate())));
			rpEntity.setRskEpiEstimate(StringUtils.isEmpty(beanObj.getOurEstimate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurEstimate()));
			rpEntity.setRskXlcostOc(StringUtils.isEmpty(beanObj.getXlCost()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlCost()));
			rpEntity.setRskXlcostDc(StringUtils.isEmpty(beanObj.getXlCost()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlCost(), beanObj.getExchRate())));
			rpEntity.setRskCedantRetention(StringUtils.isEmpty(beanObj.getCedReten()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getCedReten()));
			rpEntity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			rpEntity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			rpEntity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				rpEntity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			}
			rpEntity.setRskCedretType(StringUtils.isBlank(beanObj.getCedRetenType())?"":beanObj.getCedRetenType());
			rpEntity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			rpEntity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			rpEntity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMaxLimitProduct()));
			rpEntity.setRskEventLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			rpEntity.setRskEventLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerOffer()));
			rpEntity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsoeOc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpiAsPerShare()));
			rpEntity.setRskEpiOsoeDc(StringUtils.isEmpty(beanObj.getEpiAsPerShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerShare(), beanObj.getExchRate())));
			rpEntity.setRskXlcostOsOc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getXlcostOurShare()));
			rpEntity.setRskXlcostOsDc(StringUtils.isEmpty(beanObj.getXlcostOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlcostOurShare(), beanObj.getExchRate())));
			rpEntity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerVesselOC()));
			rpEntity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			rpEntity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			rpEntity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			rpEntity.setRskTreatySurpLimitOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOC()));
			rpEntity.setRskTreatySurpLimitDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOC())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOC(), beanObj.getExchRate())));
			rpEntity.setRskTreatySurpLimitOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getTreatyLimitsurplusOurShare()));
			rpEntity.setRskTreatySurpLimitOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOurShare())|| StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getTreatyLimitsurplusOurShare(), beanObj.getExchRate())));
			if("TR".equalsIgnoreCase(beanObj.getRetroType())){
				rpEntity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml()) ? "0": beanObj.getLimitOrigCurPml());
				rpEntity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPml(), beanObj.getExchRate()));
			}
			else{
				rpEntity.setRskTrtyLmtPmlOc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml()) ? "0": beanObj.getFaclimitOrigCurPml());
				rpEntity.setRskTrtyLmtPmlDc(StringUtils.isEmpty(beanObj.getFaclimitOrigCurPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getFaclimitOrigCurPml(), beanObj.getExchRate()));
				}
			rpEntity.setRskTrtyLmtPmlOsOc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS()) ? "0": beanObj.getLimitOrigCurPmlOS());
			rpEntity.setRskTrtyLmtPmlOsDc(StringUtils.isEmpty(beanObj.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getLimitOrigCurPmlOS(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtSurPmlOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml()) ? "0": beanObj.getTreatyLimitsurplusOCPml());
			rpEntity.setRskTrtyLmtSurPmlDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPml(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtSurPmlOsOc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS()) ? "0": beanObj.getTreatyLimitsurplusOCPmlOS());		
			rpEntity.setRskTrtyLmtSurPmlOsDc(StringUtils.isEmpty(beanObj.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getTreatyLimitsurplusOCPmlOS(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtOurassPmlOc(StringUtils.isEmpty(beanObj.getEpipml()) ? "0": beanObj.getEpipml());
			rpEntity.setRskTrtyLmtOurassPmlDc(StringUtils.isEmpty(beanObj.getEpipml())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getEpipml(), beanObj.getExchRate()));
			rpEntity.setRskTrtyLmtOurassPmlOsOc	(StringUtils.isEmpty(beanObj.getEpipmlOS()) ? "0": beanObj.getEpipmlOS());
			rpEntity.setRskTrtyLmtourAssPmlOsDc(StringUtils.isEmpty(beanObj.getEpipmlOS())|| StringUtils.isEmpty(beanObj.getExchRate()) ? "0": getDesginationCountry(beanObj.getEpipmlOS(), beanObj.getExchRate()));
			rpEntity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));
			rpEntity.setLoginId(beanObj.getLoginId());
			rpEntity.setBranchCode(beanObj.getBranchCode());
			rpEntity.setRskPml(StringUtils.isEmpty(beanObj.getPml()) ? "" : beanObj.getPml());
			rpEntity.setRskPmlPercent(StringUtils.isEmpty(beanObj.getPmlPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getPmlPercent()));

			rpRepo.save(rpEntity);
			count=1;
		}
		if ("5".equalsIgnoreCase(pid)) {
			rpEntity.setRskLimitOc(StringUtils.isEmpty(beanObj.getLimitOrigCur()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOrigCur()));
			rpEntity.setRskLimitDc(StringUtils.isEmpty(beanObj.getLimitOrigCur())	|| StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOrigCur(), beanObj.getExchRate())));
			rpEntity.setRskEpiEstOc(StringUtils.isEmpty(beanObj.getEpi()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getEpi()));
			rpEntity.setRskEpiEstDc(StringUtils.isEmpty(beanObj.getEpi()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpi(), beanObj.getExchRate())));
			rpEntity.setRskShareWritten(StringUtils.isEmpty(beanObj.getShareWritt()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getShareWritt()));
			if (beanObj.getProStatus().equalsIgnoreCase("P")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
				rpEntity.setRskShareSigned(StringUtils.isEmpty(beanObj.getSharSign()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getSharSign()));
			} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			} else {
				rpEntity.setRskShareSigned(BigDecimal.ZERO);
			}
			rpEntity.setRskMaxLmtCover(StringUtils.isEmpty(beanObj.getMaxLimitProduct()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getMaxLimitProduct()));
			rpEntity.setRskSubjPremiumOc(StringUtils.isEmpty(beanObj.getSubPremium()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getSubPremium()));
			rpEntity.setRskSubjPremiumDc(StringUtils.isEmpty(beanObj.getSubPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getSubPremium(), beanObj.getExchRate())));
			rpEntity.setRskXlpremOc(StringUtils.isEmpty(beanObj.getXlPremium()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getXlPremium()));
			rpEntity.setRskXlpremDc(StringUtils.isEmpty(beanObj.getXlPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getXlPremium(), beanObj.getExchRate())));
			rpEntity.setRskPfCovered(StringUtils.isEmpty(beanObj.getPortfoloCovered()) ? "": beanObj.getPortfoloCovered());
			rpEntity.setRskDeducOc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getDeduchunPercent()));
			rpEntity.setRskDeducDc(StringUtils.isEmpty(beanObj.getDeduchunPercent()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getDeduchunPercent(),beanObj.getExchRate())));
			rpEntity.setRskMdPremOc(StringUtils.isEmpty(beanObj.getMdPremium()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdPremium()));
			rpEntity.setRskMdPremDc(StringUtils.isEmpty(beanObj.getMdPremium()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getMdPremium(), beanObj.getExchRate())));
			rpEntity.setRskAdjrate(StringUtils.isEmpty(beanObj.getAdjRate()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getAdjRate()));
			rpEntity.setRskSpRetro(StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro());
			rpEntity.setRskNoOfInsurers(StringUtils.isEmpty(beanObj.getNoInsurer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getNoInsurer()));
			rpEntity.setRskLimitOsOc(StringUtils.isEmpty(beanObj.getLimitOurShare()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitOurShare()));
			rpEntity.setRskLimitOsDc(StringUtils.isEmpty(beanObj.getLimitOurShare()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchRate())));
			rpEntity.setRskEpiOsofOc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getEpiAsPerOffer()));
			rpEntity.setRskEpiOsofDc(StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchRate())));
			rpEntity.setRskMdPremOsOc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getMdpremiumourservice()));
			rpEntity.setRskMdPremOsDc(StringUtils.isEmpty(beanObj.getMdpremiumourservice()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchRate())));
			rpEntity.setLimitPerVesselOc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? BigDecimal.ZERO : new BigDecimal( beanObj.getLimitPerVesselOC()));
			rpEntity.setLimitPerVesselDc(StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal(getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchRate())));
			rpEntity.setLimitPerLocationOc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLimitPerLocationOC()));
			rpEntity.setLimitPerLocationDc(StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) || StringUtils.isEmpty(beanObj.getExchRate()) ? BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchRate())));
			rpEntity.setEgpniAsOffer(StringUtils.isEmpty(beanObj.getEgnpiOffer()) ?BigDecimal.ZERO : new BigDecimal(beanObj.getEgnpiOffer()));
			rpEntity.setOurassessment(StringUtils.isEmpty(beanObj.getOurAssessment()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getOurAssessment()));
			rpEntity.setEgpniAsOfferDc(StringUtils.isEmpty(beanObj.getEgnpiOffer()) || StringUtils.isEmpty(beanObj.getExchRate()) ?BigDecimal.ZERO : new BigDecimal( getDesginationCountry(beanObj.getEgnpiOffer(), beanObj.getExchRate())));
			rpEntity.setLoginId(beanObj.getLoginId());
			rpEntity.setBranchCode(beanObj.getBranchCode());

			rpRepo.save(rpEntity);
			count=1;
		}
		return count;
	}
	//RISK_UPDATE_POSITIONMASTER
	public int updateHomePostion(final FirstInsertReq beanObj,final String pid, final boolean bool) throws ParseException {
		int count = 0;
		PositionMaster entity = pmRepo.findByProposalNoAndAmendId(new BigDecimal(beanObj.getProposalNo()),new BigDecimal(beanObj.getEndorsmentNo()));
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		entity.setLayerNo(StringUtils.isEmpty(beanObj.getLayerNo()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getLayerNo()));
		entity.setReinsuranceId("");
		entity.setProductId( new BigDecimal(pid));
		entity.setDeptId(beanObj.getDepartId());
		entity.setCedingCompanyId(beanObj.getCedingCo());
		entity.setUwYear(beanObj.getUwYear());
		entity.setUwMonth(sdf.parse(beanObj.getMonth()));
		entity.setAccountDate(sdf.parse(beanObj.getAccDate()));
		entity.setInceptionDate(sdf.parse(beanObj.getIncepDate()));
		entity.setExpiryDate(sdf.parse(beanObj.getExpDate()));
		entity.setProposalStatus(beanObj.getProStatus());
		if (beanObj.getContNo() == null || beanObj.getContNo().equalsIgnoreCase("") ){
			if(beanObj.getProStatus().equalsIgnoreCase("P") || beanObj.getProStatus().equalsIgnoreCase("A")){
				entity.setContractStatus("P");
				} else if ("R".equalsIgnoreCase(beanObj.getProStatus())) {
			entity.setContractStatus("R");
		} else if("N".equalsIgnoreCase(beanObj.getProStatus())) {
			entity.setContractStatus("N");
		}else{
			entity.setContractStatus(beanObj.getProStatus().trim());
		}
		}
		entity.setBrokerId(beanObj.getBroker());
		entity.setRetroType(StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType());
		entity.setLoginId(beanObj.getLoginId());
		entity.setRskDummyContract(StringUtils.isBlank(beanObj.getDummyCon())?"":beanObj.getDummyCon());
		entity.setDataMapContNo(StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal());
		pmRepo.save(entity);
		count = 1;
		return count;
	}
	@Override
	public CommonSaveRes previouRetroTypeChect(String proposalNo, String branchCode) {
		CommonSaveRes response = new CommonSaveRes();
		String result="";
		try{
			//GET_RETRO_TYPE
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class);

			// Select
			query.select(rd.get("rskRetroType")).distinct(true); 

			// maxEnd
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> rds = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			Predicate a2 = cb.equal( rds.get("branchCode"), rd.get("branchCode"));
			end.where(a1,a2);

			// Where
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
			Predicate n3 = cb.equal(rd.get("branchCode"), branchCode);
			query.where(n1,n2,n3);

			// Get Result
			TypedQuery<String> res = em.createQuery(query);
			List<String> list = res.getResultList();
			if(list.size()>0) {
				result = list.get(0);	
				}
			 response.setResponse(result);
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
		RiskDetailsEditModeRes1 beanObj = new RiskDetailsEditModeRes1();
		boolean saveFlag = false;
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		try {
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
		
			if (req.getProductId().equalsIgnoreCase("4")) {
				//RISK_SELECT_GETEDITMODEDATA,RISK_SELECT_GETEDITMODECONTCOND,RISK_SELECT_GETEDITMODEPROCOND
				query.multiselect(de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
				de.get("rskLayerNo").alias("RSK_LAYER_NO"),	de.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
				de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
				de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
				de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
				de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
				de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
				de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
				de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"), 	de.get("rskLocIssued").alias("RSK_LOC_ISSUED"), 
				de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
				de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
				de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
				de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),de.get("rskRemarks").alias("RSK_REMARKS"),
				de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),
				de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),
				de.get("rskInsuredName").alias("RSK_INSURED_NAME"),de.get("rskLocation").alias("RSK_LOCATION"),
				de.get("rskCity").alias("RSK_CITY"),de.get("rskBaseLayer").alias("RSK_BASE_LAYER"),
				de.get("mndInstallments").alias("MND_INSTALLMENTS"),de.get("oldContractno").alias("OLD_CONTRACTNO"),
				de.get("rskPremiumRate").alias("RSK_PREMIUM_RATE"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
				de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("inwardBusType").alias("INWARD_BUS_TYPE"),
				de.get("treatytype").alias("TREATYTYPE"),de.get("rskBusinessType").alias("RSK_BUSINESS_TYPE"),
				de.get("rskExchangeType").alias("RSK_EXCHANGE_TYPE"),de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),
				de.get("branchCode").alias("BRANCH_CODE"), de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
				de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),
				de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),de.get("rskLatitude").alias("RSK_LATITUDE"),
				de.get("rskLongitude").alias("RSK_LONGITUDE"),de.get("rskVessalTonnage").alias("RSK_VESSAL_TONNAGE"),
				de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskDocStatus").alias("RSK_DOC_STATUS"),
				de.get("rskUmbrellaXl").alias("RSK_LOC_BNK_NAME"),
				de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),
				de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
				de.get("rskFixedRate").alias("RSK_FIXED_RATE"),de.get("rskDummyContract").alias("RSK_UMBRELLA_XL"),
				de.get("retentionyn").alias("RETENTIONYN"),de.get("xolLayerNo").alias("XOL_LAYER_NO"),
				de.get("rskRunOffYear").alias("RSK_RUN_OFF_YEAR"),
				pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),
				pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),
				pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),
				pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
				pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
				pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
				pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
				pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
				pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
				pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
				pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),pr.get("rskPml").alias("RSK_PML"),
				pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),pm.get("loginId").alias("LOGIN_ID"),pm.get("baseLayer").alias("BASE_LAYER"),
				pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"));
				
					if ("true".equalsIgnoreCase(req.getContractMode())) {
						// maxEndDe
						Subquery<Long> maxEndDe = query.subquery(Long.class); 
						Root<TtrnRiskDetails> des = maxEndDe.from(TtrnRiskDetails.class);
						maxEndDe.select(cb.max(des.get("rskEndorsementNo")));
						Predicate a1 = cb.equal(des.get("rskContractNo"), req.getContractNo());
						maxEndDe.where(a1);
						
						// maxEndPr
						Subquery<Long> maxEndPr = query.subquery(Long.class); 
						Root<TtrnRiskProposal> b = maxEndPr.from(TtrnRiskProposal.class);
						Root<TtrnRiskDetails> a = maxEndPr.from(TtrnRiskDetails.class);
						maxEndPr.select(cb.max(b.get("rskEndorsementNo")));
						Predicate b1 = cb.equal( a.get("rskContractNo"), req.getContractNo());
						Predicate b2 = cb.equal(a.get("rskProposalNumber"), b.get("rskProposalNumber"));
						maxEndPr.where(b1,b2);
						
						// maxAmendPm
						Subquery<Long> maxAmendPm = query.subquery(Long.class); 
						Root<PositionMaster> pms = maxAmendPm.from(PositionMaster.class);
						maxAmendPm.select(cb.max(pms.get("amendId")));
						Predicate c1 = cb.equal( pms.get("proposalNo"), pm.get("proposalNo"));
						maxAmendPm.where(c1);

						// Where
						Predicate n1 = cb.equal(de.get("rskContractNo"), req.getContractNo());
						Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
						Predicate n3 = cb.equal(de.get("rskEndorsementNo"), maxEndDe);
						Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), maxEndPr);
						Predicate n5 = cb.equal(de.get("rskProposalNumber"), pm.get("proposalNo"));
						Predicate n6 = cb.equal(pm.get("amendId"), maxAmendPm);
						query.where(n1,n2,n3,n4,n5,n6);
						
					}else{
						// maxEndDe
						Subquery<Long> maxEndDe = query.subquery(Long.class); 
						Root<TtrnRiskDetails> des = maxEndDe.from(TtrnRiskDetails.class);
						maxEndDe.select(cb.max(des.get("rskEndorsementNo")));
						Predicate a1 = cb.equal( des.get("rskProposalNumber"), req.getProposalNo());
						maxEndDe.where(a1);
						
						// maxEndPr
						Subquery<Long> maxEndPr = query.subquery(Long.class); 
						Root<TtrnRiskProposal> b = maxEndPr.from(TtrnRiskProposal.class);
						maxEndPr.select(cb.max(b.get("rskEndorsementNo")));
						Predicate b2 = cb.equal(b.get("rskProposalNumber"), req.getProposalNo());
						maxEndPr.where(b2);
						
						// maxAmendPm
						Subquery<Long> maxAmendPm = query.subquery(Long.class); 
						Root<PositionMaster> pms = maxAmendPm.from(PositionMaster.class);
						maxAmendPm.select(cb.max(pms.get("amendId")));
						Predicate c1 = cb.equal( pms.get("proposalNo"), pm.get("proposalNo"));
						maxAmendPm.where(c1);

						// Where
						Predicate n1 = cb.equal(de.get("rskProposalNumber"), req.getProposalNo());
						Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
						Predicate n3 = cb.equal(de.get("rskEndorsementNo"), maxEndDe);
						Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), maxEndPr);
						Predicate n5 = cb.equal(de.get("rskProposalNumber"), pm.get("proposalNo"));
						Predicate n6 = cb.equal(pm.get("amendId"), maxAmendPm);
						query.where(n1,n2,n3,n4,n5,n6);
						
					}
					// Get Result
					TypedQuery<Tuple> res = em.createQuery(query);
					List<Tuple> list = res.getResultList();

				if(list!=null && list.size()>0) {
					Tuple	resMap = list.get(0);
					if (resMap!=null) {
					beanObj.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
					beanObj.setEndorsmentNo(resMap.get("RSK_ENDORSEMENT_NO")==null?"":resMap.get("RSK_ENDORSEMENT_NO").toString());
					if(!"Renewal".equalsIgnoreCase(req.getReMode())) {
					beanObj.setContractNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
					}
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
					beanObj.setInceptionDate(resMap.get("RSK_INCEPTION_DATE")==null?"":(resMap.get("RSK_INCEPTION_DATE").toString()));
					beanObj.setExpiryDate(resMap.get("RSK_EXPIRY_DATE")==null?"":(resMap.get("RSK_EXPIRY_DATE").toString()));
					beanObj.setAcceptanceDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":(resMap.get("RSK_ACCOUNT_DATE").toString()));
					beanObj.setOrginalCurrency(resMap.get("RSK_ORIGINAL_CURR")==null?"":resMap.get("RSK_ORIGINAL_CURR").toString());
					beanObj.setExchangeRate(resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString());
					beanObj.setBasis(resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_BASIS")==null?"":resMap.get("RSK_BASIS").toString());
					beanObj.setPnoc(resMap.get("RSK_PERIOD_OF_NOTICE")==null?"":resMap.get("RSK_PERIOD_OF_NOTICE").toString());
					beanObj.setRiskCovered(resMap.get("RSK_RISK_COVERED")==null?"":resMap.get("RSK_RISK_COVERED").toString());
					beanObj.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
					beanObj.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString()); //24
					beanObj.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
					beanObj.setRetroType(resMap.get("RSK_RETRO_TYPE")==null?"0":resMap.get("RSK_RETRO_TYPE").toString());
					if("TR".equalsIgnoreCase(beanObj.getRetroType())){
						beanObj.setLimitOrigCur(resMap.get("RSK_LIMIT_OC")==null?"":resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OC").toString()==null?"":resMap.get("RSK_LIMIT_OC").toString());
					}
					else{
						beanObj.setFaclimitOrigCur(resMap.get("RSK_LIMIT_OC")==null?"":resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OC").toString()==null?"":resMap.get("RSK_LIMIT_OC").toString());
					}
					beanObj.setEpiorigCur(resMap.get("RSK_EPI_OFFER_OC")==null?"":resMap.get("RSK_EPI_OFFER_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OFFER_OC").toString()==null?"":resMap.get("RSK_EPI_OFFER_OC").toString());
					beanObj.setXlCost(resMap.get("RSK_XLCOST_OC")==null?"":resMap.get("RSK_XLCOST_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_XLCOST_OC").toString()==null?"":resMap.get("RSK_XLCOST_OC").toString());
					beanObj.setShareWritten(resMap.get("RSK_SHARE_WRITTEN")==null?"":resMap.get("RSK_SHARE_WRITTEN").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_SHARE_WRITTEN").toString()==null?"":resMap.get("RSK_SHARE_WRITTEN").toString());
					beanObj.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString().equalsIgnoreCase("0") ? "": resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
					beanObj.setProposalType(resMap.get("RSK_PROPOSAL_TYPE")==null?"":resMap.get("RSK_PROPOSAL_TYPE").toString());
					beanObj.setAccountingPeriod(resMap.get("RSK_ACCOUNTING_PERIOD")==null?"":resMap.get("RSK_ACCOUNTING_PERIOD").toString());
					beanObj.setReceiptofStatements(resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString());
					beanObj.setReceiptofPayment(resMap.get("RSK_RECEIPT_PAYEMENT")==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_RECEIPT_PAYEMENT").toString()==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString());
					beanObj.setNoRetroCess(resMap.get("RETRO_CESSIONARIES")==null?"":resMap.get("RETRO_CESSIONARIES").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RETRO_CESSIONARIES").toString()==null?"":resMap.get("RETRO_CESSIONARIES").toString());
					beanObj.setInsuredName(resMap.get("RSK_INSURED_NAME")==null?"":resMap.get("RSK_INSURED_NAME").toString());
					beanObj.setMaxLimitProduct(resMap.get("RSK_MAX_LMT_COVER")==null?"":resMap.get("RSK_MAX_LMT_COVER").toString()==null ? "0" : resMap.get("RSK_MAX_LMT_COVER").toString()==null?"":resMap.get("RSK_MAX_LMT_COVER").toString());
					beanObj.setRenewalcontractNo(resMap.get("OLD_CONTRACTNO")==null?"":resMap.get("OLD_CONTRACTNO").toString());
					beanObj.setBaseLoginId(resMap.get("LOGIN_ID")==null?"":resMap.get("LOGIN_ID").toString());
					beanObj.setCedRetenType(resMap.get("RSK_CEDRET_TYPE") == null ? "" : resMap.get("RSK_CEDRET_TYPE").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_CEDRET_TYPE") == null ? "" : resMap.get("RSK_CEDRET_TYPE").toString());
					beanObj.setCedRetent(resMap.get("RSK_CEDANT_RETENTION") == null ? "" : resMap.get("RSK_CEDANT_RETENTION").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_CEDANT_RETENTION").toString() == null ? "" : resMap.get("RSK_CEDANT_RETENTION").toString());
					beanObj.setPml(resMap.get("RSK_PML") == null ? "" : resMap.get("RSK_PML").toString());
					beanObj.setPmlPercent(resMap.get("RSK_PML_PERCENT") == null ? "" : resMap.get("RSK_PML_PERCENT").toString());
					beanObj.setTreatyType(resMap.get("TREATYTYPE") == null ? "" : resMap.get("TREATYTYPE").toString());
					beanObj.setLOCIssued(resMap.get("RSK_LOC_ISSUED") == null ? "" : resMap.get("RSK_LOC_ISSUED").toString());
					beanObj.setPerilCovered(resMap.get("RSK_PERILS_COVERED") == null ? "" : resMap.get("RSK_PERILS_COVERED").toString());
					beanObj.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE") == null ? "" : resMap.get("COUNTRIES_INCLUDE").toString());
					beanObj.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE") == null ? "" : resMap.get("COUNTRIES_EXCLUDE").toString());
					beanObj.setTreatynoofLine(resMap.get("RSK_NO_OF_LINE") == null ? "0" : resMap.get("RSK_NO_OF_LINE").toString());
					if("TR".equalsIgnoreCase(beanObj.getRetroType())){
					beanObj.setLimitOrigCurPml(resMap.get("RSK_TRTY_LMT_PML_OC") == null ? "0" : resMap.get("RSK_TRTY_LMT_PML_OC").toString());
					}
					else{
						beanObj.setFaclimitOrigCurPml(resMap.get("RSK_TRTY_LMT_PML_OC") == null ? "0" : resMap.get("RSK_TRTY_LMT_PML_OC").toString());
					}
					beanObj.setTreatyLimitsurplusOCPml(resMap.get("RSK_TRTY_LMT_SUR_PML_OC") == null ? "" : resMap.get("RSK_TRTY_LMT_SUR_PML_OC").toString());
					beanObj.setEpipml(resMap.get("RSK_TRTY_LMT_OURASS_PML_OC") == null ? "" : resMap.get("RSK_TRTY_LMT_OURASS_PML_OC").toString());
					beanObj.setEpi(resMap.get("RSK_TRTY_LMT_OURASS_PML_OC") == null ? "" : resMap.get("RSK_TRTY_LMT_OURASS_PML_OC").toString());
					beanObj.setEpi(resMap.get("RSK_EPI_EST_OC") == null ? "" : resMap.get("RSK_EPI_EST_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_EST_OC").toString() == null ? "" : resMap.get("RSK_EPI_EST_OC").toString());
					beanObj.setTreatyLimitsurplusOC(resMap.get("RSK_TREATY_SURP_LIMIT_OC") == null ? "" : resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString() == null ? "" : resMap.get("RSK_TREATY_SURP_LIMIT_OC").toString());
					beanObj.setRunoffYear(resMap.get("RSK_RUN_OFF_YEAR")==null?"":resMap.get("RSK_RUN_OFF_YEAR").toString());
					beanObj.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
					beanObj.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
					beanObj.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
					beanObj.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
					beanObj.setCessionExgRate(resMap.get("RSK_CESSION_EXG_RATE")==null?"":resMap.get("RSK_CESSION_EXG_RATE").toString());
					beanObj.setFixedRate(resMap.get("RSK_FIXED_RATE")==null?"":resMap.get("RSK_FIXED_RATE").toString());
					beanObj.setDummyCon(resMap.get("RSK_DUMMY_CONTRACT")==null?"":resMap.get("RSK_DUMMY_CONTRACT").toString());
					saveFlag = true;
				}
			} }
			if ("5".equalsIgnoreCase(req.getProductId())) {
				//RISK_SELECT_GETEDITMODEDATAPRO3,RISK_SELECT_GETEDITMODEDATAPRO3CONTCOND,RISK_SELECT_GETEDITMODEDATAPRO3PROCOND
				query.multiselect(de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
						de.get("rskLayerNo").alias("RSK_LAYER_NO"),de.get("rskContractNo").alias("RSK_CONTRACT_NO"),
						de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
						de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
						de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
						de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
						de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
						de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
						de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
						de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),  de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
						de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
						de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
						de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
						de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),de.get("rskRemarks").alias("RSK_REMARKS"),
						de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),
						de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),
						de.get("rskInsuredName").alias("RSK_INSURED_NAME"),de.get("rskLocation").alias("RSK_LOCATION"),
						de.get("rskCity").alias("RSK_CITY"),de.get("rskBaseLayer").alias("RSK_BASE_LAYER"),
						de.get("mndInstallments").alias("MND_INSTALLMENTS"),de.get("oldContractno").alias("OLD_CONTRACTNO"),
						de.get("rskPremiumRate").alias("RSK_PREMIUM_RATE"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
						de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("inwardBusType").alias("INWARD_BUS_TYPE"),
						de.get("treatytype").alias("TREATYTYPE"),de.get("rskBusinessType").alias("RSK_BUSINESS_TYPE"),
						de.get("rskExchangeType").alias("RSK_EXCHANGE_TYPE"),de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),
						de.get("branchCode").alias("BRANCH_CODE"), de.get("rskLocIssued").alias("RSK_LOC_ISSUED"), 
						de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),
						de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),de.get("rskLatitude").alias("RSK_LATITUDE"),
						de.get("rskLongitude").alias("RSK_LONGITUDE"),de.get("rskVessalTonnage").alias("RSK_VESSAL_TONNAGE"),
						de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskDocStatus").alias("RSK_DOC_STATUS"),
						de.get("rskUmbrellaXl").alias("RSK_LOC_BNK_NAME"),
						de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),
						de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
						de.get("rskFixedRate").alias("RSK_FIXED_RATE"),de.get("rskDummyContract").alias("RSK_UMBRELLA_XL"),
						de.get("retentionyn").alias("RETENTIONYN"),de.get("xolLayerNo").alias("XOL_LAYER_NO"),
						pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),
						pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),
						pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),
						pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
						pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
						pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
						pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
						pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
						pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
						pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
						pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),pr.get("rskPml").alias("RSK_PML"),
						pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),pm.get("loginId").alias("LOGIN_ID"),pm.get("baseLayer").alias("BASE_LAYER"),
						pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"),
						pr.get("rskMdPremOc").alias("RSK_MD_PREM_OC"),pr.get("rskAdjrate").alias("RSK_ADJRATE"),
						pr.get("rskPfCovered").alias("RSK_PF_COVERED"),
				pr.get("rskSubjPremiumOc").alias("RSK_SUBJ_PREMIUM_OC"),pr.get("rskXlpremOc").alias("RSK_XLPREM_OC"),
				pr.get("rskDeducOc").alias("RSK_DEDUC_OC"),pr.get("rskEventLimitOc").alias("RSK_EVENT_LIMIT_OC"),
				pr.get("rskCoverLimitUxlOc").alias("RSK_COVER_LIMIT_UXL_OC"),pr.get("rskDeductableUxlOc").alias("RSK_DEDUCTABLE_UXL_OC"),
				pr.get("rskEgnpiPmlOc").alias("RSK_EGNPI_PML_OC"),pr.get("rskPremiumBasis").alias("RSK_PREMIUM_BASIS"),
				pr.get("rskMinimumRate").alias("RSK_MINIMUM_RATE"),pr.get("rskMaxiimumRate").alias("RSK_MAXIIMUM_RATE"),
				pr.get("rskBurningCostLf").alias("RSK_BURNING_COST_LF"),pr.get("rskMinimumPremiumOc").alias("RSK_MINIMUM_PREMIUM_OC"),
				pr.get("rskPaymentDueDays").alias("RSK_PAYMENT_DUE_DAYS"),pr.get("rskMinimumPremiumDc").alias("RSK_MINIMUM_PREMIUM_DC"),
				pr.get("rskMinimumPremiumOsOc").alias("RSK_MINIMUM_PREMIUM_OS_OC"),pr.get("rskMinimumPremiumOsDc").alias("RSK_MINIMUM_PREMIUM_OS_DC"),
				pr.get("rskCoverLimitUxlDc").alias("RSK_COVER_LIMIT_UXL_DC"),pr.get("rskCoverLimitUxlOsOc").alias("RSK_COVER_LIMIT_UXL_OS_OC"),
				pr.get("rskCoverLimitUxlOsDc").alias("RSK_COVER_LIMIT_UXL_OS_DC"),pr.get("rskDeductableUxlDc").alias("RSK_DEDUCTABLE_UXL_DC"),
				pr.get("rskDeductableUxlOsOc").alias("RSK_DEDUCTABLE_UXL_OS_OC"),pr.get("rskDeductableUxlOsDc").alias("RSK_DEDUCTABLE_UXL_OS_DC"));
				
				if ("true".equalsIgnoreCase(req.getContractMode())) {
					// maxEndDe
					Subquery<Long> maxEndDe = query.subquery(Long.class); 
					Root<TtrnRiskDetails> des = maxEndDe.from(TtrnRiskDetails.class);
					maxEndDe.select(cb.max(des.get("rskEndorsementNo")));
					Predicate a1 = cb.equal( des.get("rskContractNo"), req.getContractNo());
					maxEndDe.where(a1);
					
					// maxEndPr
					Subquery<Long> maxEndPr = query.subquery(Long.class); 
					Root<TtrnRiskProposal> b = maxEndPr.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> a = maxEndPr.from(TtrnRiskDetails.class);
					maxEndPr.select(cb.max(b.get("rskEndorsementNo")));
					Predicate b1 = cb.equal( a.get("rskContractNo"), req.getContractNo());
					Predicate b2 = cb.equal(a.get("rskProposalNumber"), b.get("rskProposalNumber"));
					maxEndPr.where(b1,b2);
					
					// maxAmendPm
					Subquery<Long> maxAmendPm = query.subquery(Long.class); 
					Root<PositionMaster> pms = maxAmendPm.from(PositionMaster.class);
					maxAmendPm.select(cb.max(pms.get("amendId")));
					Predicate c1 = cb.equal( pms.get("proposalNo"), pm.get("proposalNo"));
					maxAmendPm.where(c1);

					// Where
					Predicate n1 = cb.equal(de.get("rskContractNo"), req.getContractNo());
					Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
					Predicate n3 = cb.equal(de.get("rskEndorsementNo"), maxEndDe);
					Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), maxEndPr);
					Predicate n5 = cb.equal(de.get("rskProposalNumber"), pm.get("proposalNo"));
					Predicate n6 = cb.equal(pm.get("amendId"), maxAmendPm);
					query.where(n1,n2,n3,n4,n5,n6);
					
				}else{
					// maxEndDe
					Subquery<Long> maxEndDe = query.subquery(Long.class); 
					Root<TtrnRiskDetails> des = maxEndDe.from(TtrnRiskDetails.class);
					maxEndDe.select(cb.max(des.get("rskEndorsementNo")));
					Predicate a1 = cb.equal( des.get("rskProposalNumber"), req.getProposalNo());
					maxEndDe.where(a1);
					
					// maxEndPr
					Subquery<Long> maxEndPr = query.subquery(Long.class); 
					Root<TtrnRiskProposal> b = maxEndPr.from(TtrnRiskProposal.class);
					maxEndPr.select(cb.max(b.get("rskEndorsementNo")));
					Predicate b2 = cb.equal(b.get("rskProposalNumber"), req.getProposalNo());
					maxEndPr.where(b2);
					
					// maxAmendPm
					Subquery<Long> maxAmendPm = query.subquery(Long.class); 
					Root<PositionMaster> pms = maxAmendPm.from(PositionMaster.class);
					maxAmendPm.select(cb.max(pms.get("amendId")));
					Predicate c1 = cb.equal( pms.get("proposalNo"), pm.get("proposalNo"));
					maxAmendPm.where(c1);

					// Where
					Predicate n1 = cb.equal(de.get("rskProposalNumber"), req.getProposalNo());
					Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
					Predicate n3 = cb.equal(de.get("rskEndorsementNo"), maxEndDe);
					Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), maxEndPr);
					Predicate n5 = cb.equal(de.get("rskProposalNumber"), pm.get("proposalNo"));
					Predicate n6 = cb.equal(pm.get("amendId"), maxAmendPm);
					query.where(n1,n2,n3,n4,n5,n6);
					
				}
				// Get Result
				TypedQuery<Tuple> res = em.createQuery(query);
				List<Tuple> list = res.getResultList();
				
				
				if(list!=null && list.size()>0) {
					Tuple	resMap = list.get(0);
					if (resMap!=null) {
					beanObj.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
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
					if (resMap.get("RSK_EXCHANGE_RATE") != null) {
						beanObj.setExchangeRate(resMap.get("RSK_EXCHANGE_RATE").toString().equalsIgnoreCase("0") ? "0":resMap.get("RSK_EXCHANGE_RATE").toString());
					}
					if (resMap.get("RSK_BASIS") != null) {
						beanObj.setBasis(resMap.get("RSK_BASIS").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_BASIS").toString());
					}
					beanObj.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
					beanObj.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString());
					beanObj.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
					if (resMap.get("RSK_LIMIT_OC") != null) {
						if("TR".equalsIgnoreCase(beanObj.getRetroType())){
							beanObj.setLimitOrigCur(resMap.get("RSK_LIMIT_OC")==null?"":resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OC").toString()==null?"":resMap.get("RSK_LIMIT_OC").toString());
						}
						else{
							beanObj.setFaclimitOrigCur(resMap.get("RSK_LIMIT_OC")==null?"":resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OC").toString()==null?"":resMap.get("RSK_LIMIT_OC").toString());
						}
						
					}
					if (resMap.get("RSK_EPI_EST_OC") != null) {
						beanObj.setEpi(resMap.get("RSK_EPI_EST_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_EPI_EST_OC").toString());
					}
					if (resMap.get("RSK_SHARE_WRITTEN") != null) {
						beanObj.setShareWritten(resMap.get("RSK_SHARE_WRITTEN").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_SHARE_WRITTEN").toString());
					}
					if (resMap.get("RSK_SHARE_SIGNED") != null) {
						beanObj.setSharSign(resMap.get("RSK_SHARE_SIGNED").toString().equalsIgnoreCase("0") ? "" :resMap.get("RSK_SHARE_SIGNED").toString());
					}
					if (resMap.get("RSK_MAX_LMT_COVER") != null) {
						beanObj.setMaxLimitProduct(resMap.get("RSK_MAX_LMT_COVER").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_MAX_LMT_COVER").toString());
					}
					if (resMap.get("RSK_SUBJ_PREMIUM_OC") != null) {
						beanObj.setSubPremium(resMap.get("RSK_SUBJ_PREMIUM_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_SUBJ_PREMIUM_OC").toString());
					}
					if (resMap.get("RSK_XLPREM_OC") != null) {
						beanObj.setXlPremium(resMap.get("RSK_XLPREM_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_XLPREM_OC").toString());
					}
					if (resMap.get("RSK_DEDUC_OC") != null) {
						beanObj.setDeduchunPercent(resMap.get("RSK_DEDUC_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_DEDUC_OC").toString());
					}
					if (resMap.get("RSK_MD_PREM_OC") != null) {
						beanObj.setMdPremium(resMap.get("RSK_MD_PREM_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_MD_PREM_OC").toString());
					}
					if (resMap.get("RSK_ADJRATE") != null) {
						beanObj.setAdjRate(resMap.get("RSK_ADJRATE").toString().equalsIgnoreCase("0") ? "0"	: resMap.get("RSK_ADJRATE").toString());
					}
					if (resMap.get("RSK_PF_COVERED") != null) {
						beanObj.setPortfoloCovered(resMap.get("RSK_PF_COVERED").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_PF_COVERED").toString());
					}
					beanObj.setMdInstalmentNumber(resMap.get("MND_INSTALLMENTS")==null?"":resMap.get("MND_INSTALLMENTS").toString());
					if("5".equalsIgnoreCase(beanObj.getProductId())){
						beanObj.setNoRetroCess(resMap.get("RETRO_CESSIONARIES").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RETRO_CESSIONARIES").toString());
					}
					beanObj.setRenewalcontractNo(resMap.get("OLD_CONTRACTNO")==null?"":resMap.get("OLD_CONTRACTNO").toString());
					beanObj.setBaseLoginId(resMap.get("LOGIN_ID")==null?"":resMap.get("LOGIN_ID").toString());
					saveFlag = true;
				}
			}	}
			beanObj.setSaveFlag(String.valueOf(saveFlag));
			//GetRemarksDetails(beanObj);
			beanObj.setAmendId(dropDowmImpl.getRiskComMaxAmendId(beanObj.getProposalNo()));
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
	@Override
	public CommonSaveRes getEndDate(GetEndDateReq req) {
		CommonSaveRes response = new CommonSaveRes();
		String result="";
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
			//GET_END_DATE
			Date date = sdf.parse(req.getEndDate());
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int month = localDate.getMonthValue();
			Long year = (long) localDate.getYear();
			int day = localDate.getDayOfMonth()	;
			if(1 <= month && month<=3) {
				result = "01/01/" +  String.valueOf(year);
				} else if(4 <= month && month<=6) {
					result = "01/04/" +  String.valueOf(year);
				}else if(7 <= month && month<=9) {
					result = "01/07/" +  String.valueOf(year);
				}else if (10 <= month && month<=12) {
					result = "01/10/" +  String.valueOf(year);
				}
			response.setResponse(result);
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
	public CommonResponse insertRetroDetails(InsertRetroDetailsReq bean) {
		CommonResponse response = new CommonResponse();
		try {
			//GET_RETRO_PROCESSING
	//		int result=this.mytemplate.update(query,obj);
				  StoredProcedureQuery integration = null;
				  integration = em.createStoredProcedureQuery("GET_RETRO_PROCESSING")
				  .registerStoredProcedureParameter("pvstartdate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvenddate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvbranch", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvType", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("lvSeq", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvtransno", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvexchange", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvSeq1", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvtransno1", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvCount", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvCount1", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("pvType", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvsoaconstant", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("pvType", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvsoaarchconstant", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvArchSeq", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvmainSeq", String.class, ParameterMode.OUT)
				  .setParameter("pvstartdate", bean.getStartDate())
				  .setParameter("pvenddate", bean.getEndDate())
				  .setParameter("pvbranch", bean.getBranchCode())
				  .setParameter("pvType", "I");
				  
				  integration.execute();
				// output=(String) integration.getOutputParameterValue("pvQuoteNo");

			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
}
