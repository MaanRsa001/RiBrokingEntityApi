package com.maan.insurance.service.impl.proportionality;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCedentRet;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnPttySection;
import com.maan.insurance.model.entity.TtrnRi;
import com.maan.insurance.model.entity.TtrnRip;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TtrnBonusRepository;
import com.maan.insurance.model.repository.TtrnRiRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;
import com.maan.insurance.validation.Formatters;

@Repository
public class ProportionalityCustomRepositoryImple implements ProportionalityCustomRepository{
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	@Autowired
	EntityManager em;
	@Autowired
	private TtrnRiskDetailsRepository ttrnRiskDetailsRepository;
	@Autowired
	private TtrnRiskProposalRepository ttrnRiskProposalRepository;
	@Autowired
	private PositionMasterRepository positionMasterRepository;
	@Autowired
	private TtrnRiskCommissionRepository ttrnRiskCommissionRepository;
	@Autowired
	private Formatters fm;
	
	@Autowired
	private TtrnBonusRepository ttrnBonusRepository;
	@Autowired
	private  TtrnRiRepository ttrnRiRepository;

	@Override
	public TtrnRiskDetails ttrnRiskDetailsUpdate(String[] args) throws ParseException {
		TtrnRiskDetails ttrnRiskDetails = null;
		//risk.update.rskDtls
		//args--getFirstPageEditSaveModeAruguments
		try {
		if(args != null) {
			ttrnRiskDetails = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[52],fm.formatBigDecimal(args[53]));
			if(ttrnRiskDetails!=null) {
			ttrnRiskDetails.setSysDate(new Date());
			ttrnRiskDetails.setRskDeptid(StringUtils.isBlank(args[0])? null :fm.formatBigDecimal(args[0]));
			ttrnRiskDetails.setRskPfcid(args[1]);
			ttrnRiskDetails.setRskSpfcid(args[2]);
			ttrnRiskDetails.setRskPolbranch(StringUtils.isBlank(args[3])? null :fm.formatBigDecimal(args[3]));
			ttrnRiskDetails.setRskCedingid(StringUtils.isBlank(args[4])? null :fm.formatBigDecimal(args[4]));
			ttrnRiskDetails.setRskBrokerid(StringUtils.isBlank(args[5])? null :fm.formatBigDecimal(args[5]));
			ttrnRiskDetails.setRskTreatyid(args[6]);
			ttrnRiskDetails.setRskMonth(StringUtils.isBlank(args[7])? null : sdf.parse(args[7]));
			ttrnRiskDetails.setRskUwyear(StringUtils.isBlank(args[8])? null :fm.formatBigDecimal(args[8]));
			ttrnRiskDetails.setRskUnderwritter(args[9]);
			ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[10]));
			ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[11]));
			ttrnRiskDetails.setRskAccountDate(StringUtils.isBlank(args[12])? null : sdf.parse(args[12]));
			ttrnRiskDetails.setRskOriginalCurr(args[13]);
			ttrnRiskDetails.setRskExchangeRate(StringUtils.isBlank(args[14])? null :fm.formatBigDecimal(args[14]));
			ttrnRiskDetails.setRskBasis(args[15]);
			ttrnRiskDetails.setRskPeriodOfNotice(args[16]);
			ttrnRiskDetails.setRskRiskCovered(args[17]);
			ttrnRiskDetails.setRskTerritoryScope(args[18]);
			ttrnRiskDetails.setRskTerritory(args[19]);
			ttrnRiskDetails.setRskStatus(args[20]);
			ttrnRiskDetails.setRskProposalType(args[21]);
			ttrnRiskDetails.setRskAccountingPeriod(StringUtils.isBlank(args[22])? null :fm.formatBigDecimal(args[22]));
			ttrnRiskDetails.setRskReceiptStatement(StringUtils.isBlank(args[23])? null :fm.formatBigDecimal(args[23]));
			ttrnRiskDetails.setRskReceiptPayement(StringUtils.isBlank(args[24])? null :fm.formatBigDecimal(args[24]));
			ttrnRiskDetails.setMndInstallments(StringUtils.isBlank(args[25])? null :fm.formatBigDecimal(args[25]));
			ttrnRiskDetails.setRetroCessionaries(StringUtils.isBlank(args[26])? null :fm.formatBigDecimal(args[26]));
			ttrnRiskDetails.setRskRetroType(args[27]);
			ttrnRiskDetails.setRskInsuredName(args[28]);
			ttrnRiskDetails.setInwardBusType(args[29]);
			ttrnRiskDetails.setTreatytype(args[30]);
			ttrnRiskDetails.setRskBusinessType(args[31]);
			ttrnRiskDetails.setRskExchangeType(args[32]);
			ttrnRiskDetails.setRskPerilsCovered(args[33]);
			ttrnRiskDetails.setRskLocIssued(args[34]);
			ttrnRiskDetails.setRskUmbrellaXl(args[35]);
			ttrnRiskDetails.setLoginId(args[36]);
			ttrnRiskDetails.setBranchCode(args[37]);
			ttrnRiskDetails.setCountriesInclude(args[38]);
			ttrnRiskDetails.setCountriesExclude(args[39]);
			ttrnRiskDetails.setRskNoOfLine(args[40]);
			ttrnRiskDetails.setRsEndorsementType(args[41]);
			ttrnRiskDetails.setRskRunOffYear(StringUtils.isBlank(args[42])? null :fm.formatBigDecimal(args[42]));
			ttrnRiskDetails.setRskLocBnkName(args[43]);
			ttrnRiskDetails.setRskLocCrdtPrd(StringUtils.isBlank(args[44])? null : fm.formatBigDecimal(args[44]));
			ttrnRiskDetails.setRskLocCrdtAmt(StringUtils.isBlank(args[45])? null : fm.formatBigDecimal(args[45]));
			ttrnRiskDetails.setRskLocBenfcreName(args[46]);
			ttrnRiskDetails.setRskCessionExgRate(args[47]);
			if(StringUtils.isNotEmpty(args[48]))
			ttrnRiskDetails.setRskFixedRate(StringUtils.isBlank(args[48])? null :fm.formatBigDecimal(args[48]));
			ttrnRiskDetails.setRetentionyn(args[49]);
			ttrnRiskDetails.setRskAccountPeriodNotice(args[50]);;
			ttrnRiskDetails.setRskStatementConfirm(args[51]);;
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnRiskDetails;
	}

	@Override
	public TtrnRiskDetails ttrnRiskDetailsInsert(String[] args) throws ParseException {
		TtrnRiskDetails ttrnRiskDetails = null;
		//risk.insert.isAmendIDProTreaty
		//args--getFirstPageInsertAruguments
		try {
			if(args != null) {
				ttrnRiskDetails = new TtrnRiskDetails();
				ttrnRiskDetails.setRskProposalNumber(args[0]);
				ttrnRiskDetails.setRskEndorsementNo(fm.formatBigDecimal(args[1]));
				ttrnRiskDetails.setRskLayerNo(fm.formatBigDecimal(args[2]));
				ttrnRiskDetails.setRskProductid(fm.formatBigDecimal(args[3]));
				ttrnRiskDetails.setRskDeptid(fm.formatBigDecimal(args[4]));
				ttrnRiskDetails.setRskPfcid(args[5]);
				ttrnRiskDetails.setRskSpfcid(args[6]);
				ttrnRiskDetails.setRskPolbranch(fm.formatBigDecimal(args[7]));
				ttrnRiskDetails.setRskCedingid(fm.formatBigDecimal(args[8]));
				ttrnRiskDetails.setRskBrokerid(fm.formatBigDecimal(args[9]));
				ttrnRiskDetails.setRskTreatyid(args[10]);
				ttrnRiskDetails.setRskMonth(StringUtils.isBlank(args[11])?null : sdf.parse(args[11]));
				ttrnRiskDetails.setRskUwyear(fm.formatBigDecimal(args[12]));
				ttrnRiskDetails.setRskUnderwritter(args[13]);
				ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[14]));
				ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[15]));
				ttrnRiskDetails.setRskAccountDate(StringUtils.isBlank(args[16])?null : sdf.parse(args[16]));
				ttrnRiskDetails.setRskOriginalCurr(args[17]);
				ttrnRiskDetails.setRskExchangeRate(fm.formatBigDecimal(args[18]));
				ttrnRiskDetails.setRskBasis(args[19]);
				ttrnRiskDetails.setRskPeriodOfNotice(args[20]);
				ttrnRiskDetails.setRskRiskCovered(args[21]);
				ttrnRiskDetails.setRskTerritoryScope(args[22]);
				ttrnRiskDetails.setRskTerritory(args[23]);
				ttrnRiskDetails.setRskEntryDate(new Date());
				ttrnRiskDetails.setRskEndDate(new Date());
				ttrnRiskDetails.setRskStatus(args[24]);
				ttrnRiskDetails.setRskRemarks(args[25]);
				ttrnRiskDetails.setRskContractNo(args[26]);
				ttrnRiskDetails.setRskProposalType(args[27]);
				ttrnRiskDetails.setRskAccountingPeriod(fm.formatBigDecimal(args[28]));
				ttrnRiskDetails.setRskReceiptStatement(fm.formatBigDecimal(args[29]));
				ttrnRiskDetails.setRskReceiptPayement(fm.formatBigDecimal(args[30]));
				ttrnRiskDetails.setMndInstallments(fm.formatBigDecimal(args[31]));
				ttrnRiskDetails.setRetroCessionaries(fm.formatBigDecimal(args[32]));
				ttrnRiskDetails.setRskRetroType(args[33]);
				ttrnRiskDetails.setRskInsuredName(args[34]);
				ttrnRiskDetails.setOldContractno(args[35]);
				ttrnRiskDetails.setInwardBusType(args[36]);
				ttrnRiskDetails.setTreatytype(args[37]);
				ttrnRiskDetails.setRskBusinessType(args[38]);
				ttrnRiskDetails.setRskExchangeType(args[39]);
				ttrnRiskDetails.setRskPerilsCovered(args[40]);
				ttrnRiskDetails.setRskLocIssued(args[41]);
				ttrnRiskDetails.setRskUmbrellaXl(args[42]);
				ttrnRiskDetails.setLoginId(args[43]);
				ttrnRiskDetails.setBranchCode(args[44]);
				ttrnRiskDetails.setSysDate(new Date());
				ttrnRiskDetails.setCountriesInclude(args[45]);
				ttrnRiskDetails.setCountriesExclude(args[46]);
				ttrnRiskDetails.setRskNoOfLine(args[47]);
				ttrnRiskDetails.setRsEndorsementType(args[48]);
				ttrnRiskDetails.setRskRunOffYear(fm.formatBigDecimal(args[49]));
				ttrnRiskDetails.setRskLocBnkName(args[50]);
				ttrnRiskDetails.setRskLocCrdtPrd(StringUtils.isBlank(args[51])? null : fm.formatBigDecimal(args[51]));
				ttrnRiskDetails.setRskLocCrdtAmt(StringUtils.isBlank(args[52])? null : fm.formatBigDecimal(args[52]));
				ttrnRiskDetails.setRskLocBenfcreName(args[53]);
				ttrnRiskDetails.setRskCessionExgRate(args[54]);
				if(StringUtils.isNotEmpty(args[55]))
				ttrnRiskDetails.setRskFixedRate(fm.formatBigDecimal(args[55]));
				ttrnRiskDetails.setRetentionyn(args[56]);
				ttrnRiskDetails.setRskAccountPeriodNotice(args[57]);
				ttrnRiskDetails.setRskStatementConfirm(args[58]);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ttrnRiskDetails;
	}

	@Override
	public TtrnRiskProposal ttrnRiskProposalUpdate(String[] args) throws ParseException {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.update.pro24FirPageRskPro
		//args--getProposalSaveEditModeQuery
		try {
		if(args != null) {
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[49],fm.formatBigDecimal(args[50]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setSysDate(new Date());
				ttrnRiskProposal.setRskLimitOc(fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOfferOc(fm.formatBigDecimal(args[2]));
				ttrnRiskProposal.setRskEpiOfferDc(fm.formatBigDecimal(args[3]));
				ttrnRiskProposal.setRskEpiEstimate(fm.formatBigDecimal(args[4]));
				ttrnRiskProposal.setRskEpiEstOc(fm.formatBigDecimal(args[5]));
				ttrnRiskProposal.setRskEpiEstDc(fm.formatBigDecimal(args[6]));
				ttrnRiskProposal.setRskXlcostOc(StringUtils.isBlank(args[7])? null : fm.formatBigDecimal(args[7]));
				ttrnRiskProposal.setRskXlcostDc(fm.formatBigDecimal(args[8]));
				ttrnRiskProposal.setRskCedantRetention(fm.formatBigDecimal(args[9]));
				ttrnRiskProposal.setRskShareWritten(fm.formatBigDecimal(args[10]));
				ttrnRiskProposal.setRskShareSigned(StringUtils.isBlank(args[11])? null : fm.formatBigDecimal(args[11]));
				ttrnRiskProposal.setRskCedretType(args[12]);
				ttrnRiskProposal.setRskSpRetro(args[13]);
				ttrnRiskProposal.setRskNoOfInsurers(fm.formatBigDecimal(args[14]));	
				ttrnRiskProposal.setRskMaxLmtCover(fm.formatBigDecimal(args[15]));
				ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(args[16]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[17]));
				ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(args[18]));
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[19]));
				ttrnRiskProposal.setRskEpiOsoeOc(fm.formatBigDecimal(args[20]));
				ttrnRiskProposal.setRskEpiOsoeDc(fm.formatBigDecimal(args[21]));
				ttrnRiskProposal.setRskXlcostOsOc(fm.formatBigDecimal(args[22]));
				ttrnRiskProposal.setRskXlcostOsDc(fm.formatBigDecimal(args[23]));
				ttrnRiskProposal.setLimitPerVesselOc(fm.formatBigDecimal(args[24]));
				ttrnRiskProposal.setLimitPerVesselDc(fm.formatBigDecimal(args[25]));
				ttrnRiskProposal.setLimitPerLocationOc(fm.formatBigDecimal(args[26]));
				ttrnRiskProposal.setLimitPerLocationDc(fm.formatBigDecimal(args[27]));
				ttrnRiskProposal.setRskTreatySurpLimitOc(fm.formatBigDecimal(args[28]));
				ttrnRiskProposal.setRskTreatySurpLimitDc(fm.formatBigDecimal(args[29]));
				ttrnRiskProposal.setRskTreatySurpLimitOsOc(fm.formatBigDecimal(args[30]));
				ttrnRiskProposal.setRskTreatySurpLimitOsDc(fm.formatBigDecimal(args[31]));
				ttrnRiskProposal.setRskTrtyLmtPmlOc(args[32]);
				ttrnRiskProposal.setRskTrtyLmtPmlDc(args[33]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsOc(args[34]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsDc(args[35]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOc(args[36]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlDc(args[37]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsOc(args[38]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsDc(args[39]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOc(args[40]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlDc(args[41]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOsOc(args[42]);
				ttrnRiskProposal.setRskTrtyLmtourAssPmlOsDc(args[43]);
				ttrnRiskProposal.setSubClass(fm.formatBigDecimal(args[44]));
				ttrnRiskProposal.setLoginId(args[45]);
				ttrnRiskProposal.setBranchCode(args[46]);
				ttrnRiskProposal.setRskPml(args[47]);
				ttrnRiskProposal.setRskPmlPercent(fm.formatBigDecimal(args[48]));
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	
	}

	@Override
	public PositionMaster positionMasterUpdate(String[] input) {
		PositionMaster positionMaster = null;
		//risk.update.positionMaster
		//args--updateHomePositionMasterAruguments
		try {
		if(input != null) {
			positionMaster = positionMasterRepository.findByProposalNoAndAmendId(fm.formatBigDecimal(input[21]),fm.formatBigDecimal(input[22]));
			if(positionMaster!=null) {
				positionMaster.setLayerNo(fm.formatBigDecimal(input[0]));
				positionMaster.setReinsuranceId(input[1]);
				positionMaster.setProductId(fm.formatBigDecimal(input[2]));			
				positionMaster.setDeptId(input[3]);
				positionMaster.setCedingCompanyId(input[4]);
				positionMaster.setUwYear(input[5]);			
				positionMaster.setUwMonth(StringUtils.isBlank(input[6])? null : sdf.parse(input[6]));				
				positionMaster.setAccountDate(StringUtils.isBlank(input[7])? null : sdf.parse(input[7]));
				positionMaster.setInceptionDate(sdf.parse(input[8]));				
				positionMaster.setExpiryDate(sdf.parse(input[9]));
				positionMaster.setProposalStatus(input[10]);
				positionMaster.setEntryDate(new Date());
				positionMaster.setContractStatus(input[11]);
				positionMaster.setBrokerId(input[12]);
				positionMaster.setRetroType(input[13]);
				positionMaster.setUpdateLoginId(input[14]);
				positionMaster.setRskDummyContract(input[15]);
				positionMaster.setDataMapContNo(input[16]);
				positionMaster.setBouquetModeYn(input[17]); //Ri
				positionMaster.setBouquetNo(StringUtils.isBlank(input[18])? null : fm.formatBigDecimal(input[18]));
				positionMaster.setUwYearTo(fm.formatBigDecimal(input[19]));
				positionMaster.setSectionNo(fm.formatBigDecimal(input[20]));				
				
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return positionMaster;
	}

	@Override
	public TtrnRiskProposal ttrnRiskProposalInsert(String[] input) throws ParseException {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.insert.pro24RskProposal
		//args--getFirstPageSecondTableAruguments,getFirstPageSecondTableInsertAruguments
		try {
		if(input != null) {
			ttrnRiskProposal = new TtrnRiskProposal();
				ttrnRiskProposal.setSysDate(new Date());
				ttrnRiskProposal.setRskProposalNumber(input[0]);
				ttrnRiskProposal.setRskEndorsementNo(fm.formatBigDecimal(input[1]));
				ttrnRiskProposal.setRskLayerNo(fm.formatBigDecimal(input[2]));
				ttrnRiskProposal.setRskLimitOc(fm.formatBigDecimal(input[3]));
				ttrnRiskProposal.setRskLimitDc(fm.formatBigDecimal(input[4]));
				ttrnRiskProposal.setRskEpiOfferOc(fm.formatBigDecimal(input[5]));
				ttrnRiskProposal.setRskEpiOfferDc(fm.formatBigDecimal(input[6]));
				ttrnRiskProposal.setRskEpiEstimate(fm.formatBigDecimal(input[7]));
				ttrnRiskProposal.setRskEpiEstOc(fm.formatBigDecimal(input[8]));
				ttrnRiskProposal.setRskEpiEstDc(fm.formatBigDecimal(input[9]));
				ttrnRiskProposal.setRskXlcostOc(StringUtils.isBlank(input[10])?null :fm.formatBigDecimal(input[10]));
				ttrnRiskProposal.setRskXlcostDc(fm.formatBigDecimal(input[11]));
				ttrnRiskProposal.setRskCedantRetention(fm.formatBigDecimal(input[12]));
				ttrnRiskProposal.setRskShareWritten(fm.formatBigDecimal(input[13]));
				ttrnRiskProposal.setRskShareSigned(fm.formatBigDecimal(input[14]));
				ttrnRiskProposal.setRskCedretType(input[15]);
				ttrnRiskProposal.setRskSpRetro(input[16]);
				ttrnRiskProposal.setRskNoOfInsurers(fm.formatBigDecimal(input[17]));
				ttrnRiskProposal.setRskMaxLmtCover(fm.formatBigDecimal(input[18]));
				ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(input[19]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(input[20]));
				ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(input[21]));		
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(input[22]));	
				ttrnRiskProposal.setRskEpiOsoeOc(fm.formatBigDecimal(input[23]));		
				ttrnRiskProposal.setRskEpiOsoeDc(fm.formatBigDecimal(input[24]));		
				ttrnRiskProposal.setRskXlcostOsOc(fm.formatBigDecimal(input[25]));
				ttrnRiskProposal.setRskXlcostOsDc(fm.formatBigDecimal(input[26]));	
				ttrnRiskProposal.setLimitPerVesselOc(fm.formatBigDecimal(input[27]));
				ttrnRiskProposal.setLimitPerVesselDc(fm.formatBigDecimal(input[28]));
				ttrnRiskProposal.setLimitPerLocationOc(fm.formatBigDecimal(input[29]));
				ttrnRiskProposal.setLimitPerLocationDc(fm.formatBigDecimal(input[30]));
				ttrnRiskProposal.setRskTreatySurpLimitOc(fm.formatBigDecimal(input[31]));
				ttrnRiskProposal.setRskTreatySurpLimitDc(fm.formatBigDecimal(input[32]));
				ttrnRiskProposal.setRskTreatySurpLimitOsOc(fm.formatBigDecimal(input[33]));
				ttrnRiskProposal.setRskTreatySurpLimitOsDc(fm.formatBigDecimal(input[34]));
				ttrnRiskProposal.setSubClass(StringUtils.isBlank(input[35])?null :fm.formatBigDecimal(input[35]));;
				ttrnRiskProposal.setLoginId(input[36]);
				ttrnRiskProposal.setBranchCode(input[37]);
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}

	@Override
	public PositionMaster positionMasterInsert(String[] input) throws ParseException {
		PositionMaster positionMaster = null;
		//risk.insert.positionMaster
		//args--insertHomePositionMasterAruguments
		try {
		if(input != null) {
			positionMaster = new PositionMaster();
				positionMaster.setProposalNo(fm.formatBigDecimal(input[0]));
				positionMaster.setContractNo(fm.formatBigDecimal(input[1]));
				positionMaster.setAmendId(fm.formatBigDecimal(input[2]));
				positionMaster.setLayerNo(fm.formatBigDecimal(input[3]));
				positionMaster.setReinsuranceId(input[4]);
				positionMaster.setProductId(fm.formatBigDecimal(input[5]));				
				positionMaster.setDeptId(input[6]);
				positionMaster.setCedingCompanyId(input[7]);
				positionMaster.setUwYear(input[8]);
				positionMaster.setUwMonth(StringUtils.isBlank(input[9])?null :sdf.parse(input[9]));
				positionMaster.setAccountDate(StringUtils.isBlank(input[10])?null :sdf.parse(input[10]));
				positionMaster.setInceptionDate(sdf.parse(input[11]));
				positionMaster.setExpiryDate(sdf.parse(input[12]));
				positionMaster.setProposalStatus(input[13]);
				positionMaster.setEntryDate(new Date());
				positionMaster.setContractStatus(input[14]);
				positionMaster.setLoginId(input[15]);
				positionMaster.setBaseLayer(input[16]);
				positionMaster.setOldContractno(input[17]);
				positionMaster.setRenewalStatus(input[18]);
				positionMaster.setBrokerId(input[19]);
				positionMaster.setBranchCode(input[20]);
				positionMaster.setRetroType(input[21]);
				positionMaster.setUpdateLoginId(input[22]);
				positionMaster.setEndtStatus(input[23]);
				positionMaster.setRskDummyContract(input[24]);
				positionMaster.setDataMapContNo(input[25]);	
				positionMaster.setBouquetModeYn(input[26]);
				positionMaster.setBouquetNo(StringUtils.isBlank(input[27])? null : fm.formatBigDecimal(input[27]));
				positionMaster.setUwYearTo(fm.formatBigDecimal(input[28]));
				positionMaster.setSectionNo(fm.formatBigDecimal(input[29]));
				positionMaster.setOfferNo(input[30]);				
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return positionMaster;
}

	@Override
	public TtrnRiskProposal updateFirstPageFields(String[] args) throws ParseException {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.update.pro24FirPageRskPro
		//args--getProposalSaveEditModeQuery
		try {
		if(args != null) {
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[53],fm.formatBigDecimal(args[54]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskEventLimitOc(StringUtils.isBlank(args[0])? null : fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskEventLimitDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEventLimitOsOc(fm.formatBigDecimal(args[2]));
				ttrnRiskProposal.setRskEventLimitOsDc(fm.formatBigDecimal(args[3]));
				ttrnRiskProposal.setRskCoverLimitUxlOc(StringUtils.isBlank(args[4])? null:fm.formatBigDecimal(args[4]));
				ttrnRiskProposal.setRskCoverLimitUxlDc(fm.formatBigDecimal(args[5]));
				ttrnRiskProposal.setRskCoverLimitUxlOsOc(fm.formatBigDecimal(args[6]));
				ttrnRiskProposal.setRskCoverLimitUxlOsDc(fm.formatBigDecimal(args[7]));
				ttrnRiskProposal.setRskDeductableUxlOc(StringUtils.isBlank(args[8])? null:fm.formatBigDecimal(args[8]));
				ttrnRiskProposal.setRskDeductableUxlDc(fm.formatBigDecimal(args[9]));
				ttrnRiskProposal.setRskDeductableUxlOsOc(fm.formatBigDecimal(args[10]));
				ttrnRiskProposal.setRskDeductableUxlOsDc(fm.formatBigDecimal(args[11]));
				ttrnRiskProposal.setRskPml(args[12]);
				if(args[13]!="")
				ttrnRiskProposal.setRskPmlPercent(fm.formatBigDecimal(args[13]));
				if(args[14]!="")
				ttrnRiskProposal.setRskEgnpiPmlOc(fm.formatBigDecimal(args[14]));
				if(args[15]!="")
				ttrnRiskProposal.setRskEgnpiPmlDc(fm.formatBigDecimal(args[15]));
				if(args[16]!="")
				ttrnRiskProposal.setRskEgnpiPmlOsOc(fm.formatBigDecimal(args[16]));
				ttrnRiskProposal.setRskEgnpiPmlOsDc(fm.formatBigDecimal(args[17]));
				ttrnRiskProposal.setRskPremiumBasis(args[18]);
				ttrnRiskProposal.setRskMinimumRate(fm.formatBigDecimal(args[19]));
				ttrnRiskProposal.setRskMaxiimumRate(fm.formatBigDecimal(args[20]));	
				ttrnRiskProposal.setRskBurningCostLf(args[21]);
				ttrnRiskProposal.setRskMinimumPremiumOc(StringUtils.isBlank(args[22])? null:fm.formatBigDecimal(args[22]));
				ttrnRiskProposal.setRskMinimumPremiumDc(fm.formatBigDecimal(args[23]));
				ttrnRiskProposal.setRskMinimumPremiumOsOc(fm.formatBigDecimal(args[24]));
				ttrnRiskProposal.setRskMinimumPremiumOsDc(fm.formatBigDecimal(args[25]));
				ttrnRiskProposal.setRskPaymentDueDays(args[26]);
				ttrnRiskProposal.setRskTrtyLmtPmlOc(args[27]);
				ttrnRiskProposal.setRskTrtyLmtPmlDc(args[28]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsOc(args[29]);
				ttrnRiskProposal.setRskTrtyLmtPmlOsDc(args[30]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOc(args[31]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlDc(args[32]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsOc(args[33]);
				ttrnRiskProposal.setRskTrtyLmtSurPmlOsDc(args[34]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOc(args[35]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlDc(args[36]);
				ttrnRiskProposal.setRskTrtyLmtOurassPmlOsOc(args[37]);
				ttrnRiskProposal.setRskTrtyLmtourAssPmlOsDc(args[38]);
				ttrnRiskProposal.setRiskDetYn(args[39]);;
				ttrnRiskProposal.setBrokerDetYn(args[40]);
				ttrnRiskProposal.setCoverDetYn(args[41]);
				ttrnRiskProposal.setPremiumDetYn(args[42]);
				ttrnRiskProposal.setAcqcostDetYn(args[43]);
				ttrnRiskProposal.setCommDetYn(args[44]);
				ttrnRiskProposal.setDepositDetYn(args[45]);
				ttrnRiskProposal.setLossDetYn(args[46]);
				ttrnRiskProposal.setDocDetYn(args[47]);
				ttrnRiskProposal.setPaymentPartner(args[48]);
				ttrnRiskProposal.setIntallDetYn(args[49]);
				ttrnRiskProposal.setReinstDetYn(args[50]);
				ttrnRiskProposal.setRateOnLine(StringUtils.isBlank(args[51])? null:fm.formatBigDecimal(args[51]));
				ttrnRiskProposal.setQuotesharePercent(fm.formatBigDecimal(args[52]));;
				
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}

	@Override
	public TtrnRiskRemarks ttrnRiskRemarksInsert(String[] input) throws ParseException {
		TtrnRiskRemarks ttrnRiskRemarks = null;
		//INSERT_REMARKS_DETAILS
		try {
		if(input != null) {
			ttrnRiskRemarks = new TtrnRiskRemarks();
				ttrnRiskRemarks.setProposalNo(fm.formatBigDecimal(input[0]));
				ttrnRiskRemarks.setContractNo(StringUtils.isBlank(input[1])? null : fm.formatBigDecimal(input[1]));
				ttrnRiskRemarks.setLayerNo(fm.formatBigDecimal(input[2]));
				ttrnRiskRemarks.setDeptId(input[3]);
				ttrnRiskRemarks.setProductId(input[4]);
				ttrnRiskRemarks.setAmendId(input[5]);
				ttrnRiskRemarks.setRskSNo(input[6]);
				ttrnRiskRemarks.setRskDescription(input[7]);
				ttrnRiskRemarks.setRskRemark1(input[8]);
				ttrnRiskRemarks.setRskRemark2(input[9]);
				ttrnRiskRemarks.setLoginId(input[10]);
				ttrnRiskRemarks.setBranchCode(input[11]);
				ttrnRiskRemarks.setSysDate(new Date());;
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskRemarks;
	}

	@Override
	public TtrnCedentRet ttrnCedentRetInsert(String[] input) throws ParseException {
		TtrnCedentRet ttrnCedentRet = null;
		//INSERT_RET_DETAILS
		try {
		if(input != null) {
			ttrnCedentRet = new TtrnCedentRet();
				ttrnCedentRet.setProposalNo(input[0]);
				ttrnCedentRet.setContractNo(input[1]);
				ttrnCedentRet.setLayerNo(fm.formatBigDecimal(input[2]));
				ttrnCedentRet.setDeptId(input[3]);
				ttrnCedentRet.setProductId(input[4]);
				ttrnCedentRet.setAmendId(input[5]);
				ttrnCedentRet.setRskSno(input[6]);
				ttrnCedentRet.setRskClass(input[7]);
				ttrnCedentRet.setRskSubclass(input[8]);
				ttrnCedentRet.setRskType(input[9]);
				ttrnCedentRet.setRskRettype(input[10]);
				ttrnCedentRet.setRskBasistype(input[11]);
				ttrnCedentRet.setRskFirstRetOc(fm.formatBigDecimal(input[12]));
				ttrnCedentRet.setRskSecondRetOc(fm.formatBigDecimal(input[13]));
				ttrnCedentRet.setRskRetTlFstOc(fm.formatBigDecimal(input[14]));
				ttrnCedentRet.setRskRetTlSstOc(fm.formatBigDecimal(input[15]));
				ttrnCedentRet.setRskRetElFstOc(fm.formatBigDecimal(input[16]));
				ttrnCedentRet.setRskRetElSstOc(fm.formatBigDecimal(input[17]));
				ttrnCedentRet.setLoginId(input[18]);
				ttrnCedentRet.setBranchCode(input[19]);
				ttrnCedentRet.setSysDate(new Date());			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnCedentRet;
	}

	@Override
	public TtrnRiskProposal ttrnRiskProposalSecondPageUpdate(String[] args) throws ParseException {
		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.update.pro24RskProposal
		try {
		if(args != null) {
			
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[16],fm.formatBigDecimal(args[17]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskLimitOsOc(StringUtils.isBlank(args[0])? null : fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOsofOc(StringUtils.isBlank(args[2])? null : fm.formatBigDecimal(args[2]));		
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[3]));		
				ttrnRiskProposal.setRskEpiOsoeOc(fm.formatBigDecimal(args[4]));		
				ttrnRiskProposal.setRskEpiOsoeDc(fm.formatBigDecimal(args[5]));	
				if(StringUtils.isNotEmpty(args[6]))
				ttrnRiskProposal.setRskXlcostOsOc(fm.formatBigDecimal(args[6]));	
				if(StringUtils.isNotEmpty(args[7]))
				ttrnRiskProposal.setRskXlcostOsDc(fm.formatBigDecimal(args[7]));	
				ttrnRiskProposal.setRskPremiumQuotaShare(fm.formatBigDecimal(args[8]));
				ttrnRiskProposal.setRskPremiumSurpuls(fm.formatBigDecimal(args[9]));
				ttrnRiskProposal.setRskPremiumQuotaShareDc(fm.formatBigDecimal(args[10]));
				ttrnRiskProposal.setRskPremiumSurplusDc(fm.formatBigDecimal(args[11]));
				ttrnRiskProposal.setCommQsAmt(fm.formatBigDecimal(args[12]));
				ttrnRiskProposal.setCommSurplusAmt(fm.formatBigDecimal(args[13]));
				ttrnRiskProposal.setCommQsAmtDc(fm.formatBigDecimal(args[14]));
				ttrnRiskProposal.setCommSurplusAmtDc(fm.formatBigDecimal(args[15]));
			}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	}

	@Override
	public TtrnRiskCommission ttrnRiskCommissionSecondPageUpdate(String[] args) throws ParseException {
		TtrnRiskCommission ttrnRiskCommission = null;
		//risk.update.pro2SecComm
		try {
		if(args != null) {
			ttrnRiskCommission = ttrnRiskCommissionRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[65],fm.formatBigDecimal(args[66]));
			if(ttrnRiskCommission!=null) {
				ttrnRiskCommission.setRskBrokerage(fm.formatBigDecimal(args[0]));
				ttrnRiskCommission.setRskTax(fm.formatBigDecimal(args[1]));
				ttrnRiskCommission.setRskProfitComm(fm.formatBigDecimal(args[2]));
				ttrnRiskCommission.setRskAcquistionCostOc(fm.formatBigDecimal(args[3]));	
				ttrnRiskCommission.setRskAcquistionCostDc(fm.formatBigDecimal(args[4]));	
				ttrnRiskCommission.setRskCommQuotashare(fm.formatBigDecimal(args[5]));
				ttrnRiskCommission.setRskCommSurplus(fm.formatBigDecimal(args[6]));
				ttrnRiskCommission.setRskOverriderPerc(fm.formatBigDecimal(args[7]));
				ttrnRiskCommission.setRskPremiumReserve(fm.formatBigDecimal(args[8]));
				ttrnRiskCommission.setRskLossReserve(fm.formatBigDecimal(args[9]));
				ttrnRiskCommission.setRskInterest(fm.formatBigDecimal(args[10]));
				ttrnRiskCommission.setRskPfInoutPrem(fm.formatBigDecimal(args[11]));
				ttrnRiskCommission.setRskPfInoutLoss(fm.formatBigDecimal(args[12]));
				ttrnRiskCommission.setRskLossadvice(args[13]);
				ttrnRiskCommission.setRskCashlossLmtOc(fm.formatBigDecimal(args[14]));
				ttrnRiskCommission.setRskCashlossLmtDc(fm.formatBigDecimal(args[15]));
				ttrnRiskCommission.setRskLeadUw(args[16]);
				ttrnRiskCommission.setRskLeadUwShare(fm.formatBigDecimal(args[17]));
				ttrnRiskCommission.setRskAccounts(args[18]);
				ttrnRiskCommission.setRskExclusion(args[19]);
				ttrnRiskCommission.setRskRemarks(args[20]);
				ttrnRiskCommission.setRskUwRecomm(args[21]);
				ttrnRiskCommission.setRskGmApproval(args[22]);
				ttrnRiskCommission.setRskOtherCost(fm.formatBigDecimal(args[23]));
				ttrnRiskCommission.setRskCreastaStatus(args[24]);
				ttrnRiskCommission.setRskEventLimitOc(StringUtils.isBlank(args[25])? null : fm.formatBigDecimal(args[25]));	
				ttrnRiskCommission.setRskEventLimitDc(fm.formatBigDecimal(args[26]));	
				ttrnRiskCommission.setRskAggregateLimitOc(StringUtils.isBlank(args[27])? null : fm.formatBigDecimal(args[27]));
				ttrnRiskCommission.setRskAggregateLimitDc(fm.formatBigDecimal(args[28]));
				ttrnRiskCommission.setRskOccurrentLimitOc(StringUtils.isBlank(args[29])? null : fm.formatBigDecimal(args[29]));
				ttrnRiskCommission.setRskOccurrentLimitDc(fm.formatBigDecimal(args[30]));
				ttrnRiskCommission.setRskSladscaleComm(args[31]);
				ttrnRiskCommission.setRskLossPartCarridor(args[32]);
				ttrnRiskCommission.setRskCombinSubClass(args[33]);
				ttrnRiskCommission.setSubClass(fm.formatBigDecimal(args[34]));
				ttrnRiskCommission.setLoginId(args[35]);
				ttrnRiskCommission.setBranchCode(args[36]);
				ttrnRiskCommission.setSysDate(new Date());
				ttrnRiskCommission.setRskLeadUnderwriterCountry(args[37]);
				ttrnRiskCommission.setRskIncludeAcqCost(args[38]);
				ttrnRiskCommission.setRskOurAssAcqCost(args[39]);
				ttrnRiskCommission.setRskOurAcqOurShareOc(args[40]);
				ttrnRiskCommission.setRskLossCombinSubClass(args[41]);
				ttrnRiskCommission.setRskSlideCombinSubClass(args[42]);
				ttrnRiskCommission.setRskCrestaCombinSubClass(args[43]);
				ttrnRiskCommission.setRskProManagementExp(StringUtils.isBlank(args[44])? null : fm.formatBigDecimal(args[44]));
				ttrnRiskCommission.setRskProCommType(args[45]);
				ttrnRiskCommission.setRskProCommPer(fm.formatBigDecimal(args[46]));
				ttrnRiskCommission.setRskProSetUp(args[47]);
				ttrnRiskCommission.setRskShareProfitCommission(args[48]);
				ttrnRiskCommission.setRskProLossCaryType(args[49]);
				ttrnRiskCommission.setRskProLossCaryYear(args[50]);
				ttrnRiskCommission.setRskProProfitCaryType(args[51]);
				ttrnRiskCommission.setRskProProfitCaryYear(args[52]);
				ttrnRiskCommission.setRskProFirstPfoCom(StringUtils.isBlank(args[53])? null : fm.formatBigDecimal(args[53]));
				ttrnRiskCommission.setRskProFirstPfoComPrd(args[54]);
				ttrnRiskCommission.setRskProSubPfoComPrd(args[55]);
				ttrnRiskCommission.setRskProSubPfoCom(StringUtils.isBlank(args[56])? null : fm.formatBigDecimal(args[56]));
				ttrnRiskCommission.setRskProSubSeqCal(args[57]);
				ttrnRiskCommission.setRskProNotes(args[58]);
				ttrnRiskCommission.setRskDocStatus(args[59]);
				ttrnRiskCommission.setRskRate(args[60]);
				
				ttrnRiskCommission.setRskPremiumResType(args[61]); //Ri
				ttrnRiskCommission.setFpcType(args[62]);
				ttrnRiskCommission.setFpcFixedDate(StringUtils.isBlank(args[63])? null : sdf.parse(args[63]));
				ttrnRiskCommission.setRskPortfolioType(args[64]);
				}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskCommission;
	
}

	@Override
	public TtrnRiskCommission ttrnRiskCommissionSecondPageInsert(String[] args) throws ParseException {
		TtrnRiskCommission ttrnRiskCommission = null;
		//risk.insert.pro2SecComm
		try {
		if(args != null) {
			ttrnRiskCommission = new TtrnRiskCommission();
				ttrnRiskCommission.setRskProposalNumber(args[0]);
				ttrnRiskCommission.setRskEndorsementNo(fm.formatBigDecimal(args[1]));
				ttrnRiskCommission.setRskLayerNo(fm.formatBigDecimal(args[2]));
				ttrnRiskCommission.setRskBrokerage(fm.formatBigDecimal(args[3]));	
				ttrnRiskCommission.setRskTax(fm.formatBigDecimal(args[4]));
				ttrnRiskCommission.setRskProfitComm(fm.formatBigDecimal(args[5]));
				ttrnRiskCommission.setRskReserveOnLoss(fm.formatBigDecimal(args[6]));
				ttrnRiskCommission.setRskAcquistionCostOc(fm.formatBigDecimal(args[7]));				
				ttrnRiskCommission.setRskAcquistionCostDc(fm.formatBigDecimal(args[8]));	
				ttrnRiskCommission.setRskCommQuotashare(fm.formatBigDecimal(args[9]));
				ttrnRiskCommission.setRskCommSurplus(fm.formatBigDecimal(args[10]));
				ttrnRiskCommission.setRskOverriderPerc(fm.formatBigDecimal(args[11]));
				ttrnRiskCommission.setRskPremiumReserve(fm.formatBigDecimal(args[12]));
				ttrnRiskCommission.setRskLossReserve(fm.formatBigDecimal(args[13]));
				ttrnRiskCommission.setRskInterest(fm.formatBigDecimal(args[14]));
				ttrnRiskCommission.setRskPfInoutPrem(fm.formatBigDecimal(args[15]));
				ttrnRiskCommission.setRskPfInoutLoss(fm.formatBigDecimal(args[16]));
				ttrnRiskCommission.setRskLossadvice(args[17]);
				ttrnRiskCommission.setRskCashlossLmtOc(fm.formatBigDecimal(args[18]));
				ttrnRiskCommission.setRskCashlossLmtDc(fm.formatBigDecimal(args[19]));
				ttrnRiskCommission.setRskLeadUw(args[20]);
				ttrnRiskCommission.setRskLeadUwShare(fm.formatBigDecimal(args[21]));
				ttrnRiskCommission.setRskAccounts(args[22]);
				ttrnRiskCommission.setRskExclusion(args[23]);
				ttrnRiskCommission.setRskRemarks(args[24]);
				ttrnRiskCommission.setRskUwRecomm(args[25]);
				ttrnRiskCommission.setRskGmApproval(args[26]);
				ttrnRiskCommission.setRskDecision(args[27]);
				ttrnRiskCommission.setRskEntryDate(new Date());
				ttrnRiskCommission.setRskEndDate(new Date());
				ttrnRiskCommission.setRskStatus(args[28]);		
				ttrnRiskCommission.setRskOtherCost(fm.formatBigDecimal(args[29]));
				ttrnRiskCommission.setRskCreastaStatus(StringUtils.isBlank(args[30])?null:args[30]);
				ttrnRiskCommission.setRskEventLimitOc(fm.formatBigDecimal(args[31]));
				ttrnRiskCommission.setRskEventLimitDc(fm.formatBigDecimal(args[32]));
				ttrnRiskCommission.setRskAggregateLimitOc(fm.formatBigDecimal(args[33]));
				ttrnRiskCommission.setRskAggregateLimitDc(fm.formatBigDecimal(args[34]));
				ttrnRiskCommission.setRskOccurrentLimitOc(fm.formatBigDecimal(args[35]));
				ttrnRiskCommission.setRskOccurrentLimitDc(fm.formatBigDecimal(args[36]));			
				ttrnRiskCommission.setRskSladscaleComm(args[37]);
				ttrnRiskCommission.setRskLossPartCarridor(args[38]);
				ttrnRiskCommission.setRskCombinSubClass(args[39]);
				ttrnRiskCommission.setSubClass(fm.formatBigDecimal(args[40]));
				ttrnRiskCommission.setLoginId(args[41]);
				ttrnRiskCommission.setBranchCode(args[42]);
				ttrnRiskCommission.setSysDate(new Date());
				ttrnRiskCommission.setRskLeadUnderwriterCountry(args[43]);
				ttrnRiskCommission.setRskIncludeAcqCost(args[44]);
				ttrnRiskCommission.setRskOurAssAcqCost(args[45]);
				ttrnRiskCommission.setRskOurAcqOurShareOc(args[46]);
				ttrnRiskCommission.setRskLossCombinSubClass(args[47]);
				ttrnRiskCommission.setRskSlideCombinSubClass(args[48]);
				ttrnRiskCommission.setRskCrestaCombinSubClass(args[49]);
				ttrnRiskCommission.setRskProManagementExp(StringUtils.isBlank(args[50])? null :fm.formatBigDecimal(args[50]));
				ttrnRiskCommission.setRskProCommType(args[51]);	
				ttrnRiskCommission.setRskProCommPer(StringUtils.isBlank(args[52])? null :fm.formatBigDecimal(args[52]));
				ttrnRiskCommission.setRskProSetUp(args[53]);
				ttrnRiskCommission.setRskProSupProCom(args[54]);
				ttrnRiskCommission.setRskProLossCaryType(args[55]);
				ttrnRiskCommission.setRskProLossCaryYear(args[56]);
				ttrnRiskCommission.setRskProProfitCaryType(args[57]);
				ttrnRiskCommission.setRskProProfitCaryYear(args[58]);
				ttrnRiskCommission.setRskProFirstPfoCom(StringUtils.isBlank(args[59])? null :fm.formatBigDecimal(args[59]));
				ttrnRiskCommission.setRskProFirstPfoComPrd(args[60]);	
				ttrnRiskCommission.setRskProSubPfoComPrd(args[61]);	
				ttrnRiskCommission.setRskProSubPfoCom(StringUtils.isBlank(args[62])? null :fm.formatBigDecimal(args[62]));		
				ttrnRiskCommission.setRskProSubSeqCal(args[63]);
				ttrnRiskCommission.setRskProNotes(args[64]);
				ttrnRiskCommission.setRskDocStatus(args[65]);
				ttrnRiskCommission.setRskRate(args[66]);
				ttrnRiskCommission.setRskPremiumResType(args[67]);
				ttrnRiskCommission.setFpcType(args[68]);
				ttrnRiskCommission.setFpcFixedDate(StringUtils.isBlank(args[69])? null :sdf.parse(args[69]));
				ttrnRiskCommission.setRskPortfolioType(args[70]);
				
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskCommission;
	}

	@Override
	public int facUpdateInsDetails(String proposalNo, String string) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<TtrnInsurerDetails> update = cb.createCriteriaUpdate(TtrnInsurerDetails.class);
		Root<TtrnInsurerDetails> m = update.from(TtrnInsurerDetails.class);
		update.set("endDate", new Date());
		//end
		Subquery<Long> end = update.subquery(Long.class); 
		Root<TtrnInsurerDetails> rds = end.from(TtrnInsurerDetails.class);
		end.select(cb.max(rds.get("endorsementNo")));
		Predicate a1 = cb.equal( rds.get("proposalNo"), m.get("proposalNo"));
		Predicate a2 = cb.equal( rds.get("insurerNo"), m.get("insurerNo"));
		end.where(a1,a2);
		
		Predicate n1 = cb.equal(m.get("proposalNo"),proposalNo);
		Predicate n2 = cb.equal(m.get("insurerNo"),string);
		Predicate n3 = cb.equal(m.get("endorsementNo"),end);
		update.where(n1 ,n2,n3);
		 return em.createQuery(update).executeUpdate();
	}

	@Override
	public TtrnInsurerDetails facInsertInsDetails(String[] obj) throws ParseException {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		//fac.insert.insDetails
		try {
		if(obj != null) {
			ttrnInsurerDetails = new TtrnInsurerDetails();
			ttrnInsurerDetails.setInsurerNo(fm.formatBigDecimal(obj[0]));
			ttrnInsurerDetails.setProposalNo(obj[1]);
			ttrnInsurerDetails.setContractNo(obj[2]);
			ttrnInsurerDetails.setEndorsementNo(fm.formatBigDecimal(obj[3]));
			ttrnInsurerDetails.setType(obj[4]);
			ttrnInsurerDetails.setRetroPercentage(fm.formatBigDecimal(obj[5]));
			ttrnInsurerDetails.setStatus(obj[6]);
			ttrnInsurerDetails.setUwYear(obj[7]);
			ttrnInsurerDetails.setRetroType(obj[8]);
			ttrnInsurerDetails.setEntryDate(new Date());
			ttrnInsurerDetails.setSubClass(fm.formatBigDecimal(obj[9]));
			ttrnInsurerDetails.setLoginId(obj[10]);
			ttrnInsurerDetails.setBranchCode(obj[11]);			
			}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnInsurerDetails;
	}

	@Override
	public TtrnCrestazoneDetails moveToCrestaMainTable(String[] input) throws ParseException {
		TtrnCrestazoneDetails ttrnCrestazoneDetails = null;
		//MOVE_TO_CRESTA_MAIN_TABLE
		try {
		if(input != null) {
			ttrnCrestazoneDetails = new TtrnCrestazoneDetails();
			ttrnCrestazoneDetails.setContractNo(fm.formatBigDecimal(input[0]));
			ttrnCrestazoneDetails.setProposalNo(fm.formatBigDecimal(input[1]));
			ttrnCrestazoneDetails.setAmendId(input[2]);
			ttrnCrestazoneDetails.setSubClass(input[3]);
			ttrnCrestazoneDetails.setCrestaId(input[4]);
			ttrnCrestazoneDetails.setCrestaName(input[5]);
			ttrnCrestazoneDetails.setCurrency(input[6]);
			ttrnCrestazoneDetails.setAccRisk(input[7]);
			if(StringUtils.isNotEmpty(input[8]))
			ttrnCrestazoneDetails.setAccumDate(sdf.parse(input[8]));
			ttrnCrestazoneDetails.setEntryDate(new Date());		
			ttrnCrestazoneDetails.setStatus("Y");
			ttrnCrestazoneDetails.setBranchCode(input[9]);
			ttrnCrestazoneDetails.setTerritoryCode(input[10]);
			ttrnCrestazoneDetails.setSno(input[11]);			
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnCrestazoneDetails;
		}

	@Override
	public TtrnBonus bonusMainInsertPtty(String[] input) throws ParseException {
		TtrnBonus ttrnBonus = new TtrnBonus();
		//BONUS_MAIN_INSERT_PTTY
		try {
		if(input != null) {
			ttrnBonus = new TtrnBonus();
			ttrnBonus.setProposalNo(fm.formatBigDecimal(input[0]));
			ttrnBonus.setContractNo(fm.formatBigDecimal(input[1]));
			ttrnBonus.setProductId(input[2]);
			ttrnBonus.setLcbType(input[3]);
			ttrnBonus.setLcbFrom(input[4]);
			ttrnBonus.setLcbTo(input[5]);
			ttrnBonus.setLcbPercentage(input[6]);
			ttrnBonus.setUserId(input[7]);
			ttrnBonus.setBranch(input[8]);
			ttrnBonus.setLcbId(input[9]);
			ttrnBonus.setType(input[10]);
			ttrnBonus.setEndorsementNo(fm.formatBigDecimal(input[11]));
			ttrnBonus.setSubClass(input[12]);
			ttrnBonus.setSysDate(new Date());
			ttrnBonus.setLayerNo(input[13]);
			ttrnBonus.setQuotaShare(input[14]);
			ttrnBonus.setRemarks(input[15]);
			ttrnBonus.setFirstProfitComm(input[16]);
			ttrnBonus.setFpcDurationType(input[17]);
			ttrnBonus.setSubProfitComm(input[18]);
			ttrnBonus.setSpcDurationType(input[19]);
			ttrnBonus.setSubSecCal(input[20]);	
			ttrnBonus.setReferenceNo(fm.formatBigDecimal(input[21]));
			ttrnBonus.setScaleMaxPartPercent(fm.formatBigDecimal(input[22]));
			ttrnBonus.setFpcType(input[23]);
			ttrnBonus.setFpcFixedDate(StringUtils.isBlank(input[24])?null:sdf.parse(input[24]));
			ttrnBonus.setSno(fm.formatBigDecimal(input[25]));	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnBonus;
		}

	@Override
	public TtrnCommissionDetails commissionInsert(String[] input) {
		TtrnCommissionDetails ttrnCommissionDetails = null;
		//COMMISSION_INSERT
		try {
		if(input != null) {
			ttrnCommissionDetails = new TtrnCommissionDetails();
			ttrnCommissionDetails.setSNo(input[0]);
			ttrnCommissionDetails.setCommFrom(input[1]);	
			ttrnCommissionDetails.setCommTo(input[2]);
			ttrnCommissionDetails.setProfitComm(input[3]);
			ttrnCommissionDetails.setProposalNo(fm.formatBigDecimal(input[4]));
			ttrnCommissionDetails.setContractNo(fm.formatBigDecimal(input[5]));
			ttrnCommissionDetails.setEndorsementNo(fm.formatBigDecimal(input[6]));
			ttrnCommissionDetails.setProductId(input[7]);
			ttrnCommissionDetails.setBranchCode(input[8]);
			ttrnCommissionDetails.setSubClass(input[9]);
			ttrnCommissionDetails.setCommissionType(input[10]);
			ttrnCommissionDetails.setLoginId(input[11]);
			ttrnCommissionDetails.setEntryDate(new Date());	
			ttrnCommissionDetails.setReferenceNo(fm.formatBigDecimal(input[12]));
			ttrnCommissionDetails.setSerialNo(fm.formatBigDecimal(input[13]));			;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ttrnCommissionDetails;
	}

	@Override
	public List<Tuple> riskSelectGetEditModeData1(String[] args) throws ParseException {
		List<Tuple> list = new ArrayList<>();
	try {
		if(args!=null) {
			//risk.select.getEditModeData1
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			query.multiselect(de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					de.get("rskContractNo").alias("RSK_CONTRACT_NO"),de.get("rskLayerNo").alias("RSK_LAYER_NO"),
					de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
					de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
					de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
					de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
					de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
					de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
					de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
					de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
					de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
					de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
					de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
					de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),
					de.get("rskRemarks").alias("RSK_REMARKS"),pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),
					pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),
					pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),
					pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
					pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
					de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),
					de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
					pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
					pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
					de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("rskInsuredName").alias("RSK_INSURED_NAME"),
					de.get("oldContractno").alias("OLD_CONTRACTNO"),pm.get("loginId").alias("LOGIN_ID"),
					pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
					pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
					de.get("inwardBusType").alias("INWARD_BUS_TYPE"),de.get("treatytype").alias("TREATYTYPE"),
					pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
					pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
					pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),
					de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),
					pr.get("rskPml").alias("RSK_PML"),pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),
					de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),de.get("rskLocIssued").alias("RSK_LOC_ISSUED"),
					de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskRunOffYear").alias("RSK_RUN_OFF_YEAR"),
					de.get("rskLocBnkName").alias("RSK_LOC_BNK_NAME"),de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),
					de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),
					pm.get("baseLayer").alias("BASE_LAYER"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
					de.get("rskFixedRate").alias("RSK_FIXED_RATE"),pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),
					de.get("retentionyn").alias("RETENTIONYN"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"), //RI
					pm.get("bouquetNo").alias("BOUQUET_NO"),	pm.get("offerNo").alias("OFFER_NO"),
					pr.get("riskDetYn").alias("RISK_DET_YN"),	pr.get("brokerDetYn").alias("BROKER_DET_YN"), 
					pr.get("coverDetYn").alias("COVER_DET_YN"),	pr.get("premiumDetYn").alias("PREMIUM_DET_YN"), 
					pr.get("acqcostDetYn").alias("ACQCOST_DET_YN"),	pr.get("commDetYn").alias("COMM_DET_YN"), 
					pr.get("depositDetYn").alias("DEPOSIT_DET_YN"),	pr.get("lossDetYn").alias("LOSS_DET_YN"), 
					pr.get("docDetYn").alias("DOC_DET_YN"),	pr.get("paymentPartner").alias("PAYMENT_PARTNER"),
					pm.get("sectionNo").alias("SECTION_NO"),	pr.get("quotesharePercent").alias("QUOTESHARE_PERCENT"), 
					de.get("rskAccountPeriodNotice").alias("RSK_ACCOUNT_PERIOD_NOTICE"),de.get("rskStatementConfirm").alias("RSK_STATEMENT_CONFIRM")
					); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);
			//endDe
			Subquery<Long> endDe = query.subquery(Long.class); 
			Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
			endDe.select(cb.max(des.get("rskEndorsementNo")));
			Predicate b1 = cb.equal( des.get("rskContractNo"),args[1]);
			endDe.where(b1);
			//endPr
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskDetails> a = endPr.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(b.get("rskEndorsementNo")));
			Predicate c1 = cb.equal( a.get("rskContractNo"),args[2]);
			Predicate c2 = cb.equal( a.get("rskProposalNumber"), b.get("rskProposalNumber"));
			endPr.where(c1,c2);

			Predicate n1 = cb.equal(de.get("rskContractNo"), args[0] );
			Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
			Predicate n5 = cb.equal(de.get("rskEndorsementNo"), endDe);
			Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n4 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
			Predicate n6 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n4,n2,n5,n6);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
		}
		
	}catch(Exception e) {
		e.printStackTrace();
	}
		return list;
	}

	@Override
	public List<Tuple> riskSelectGetEditModeData2(String[] args) throws ParseException {
		List<Tuple> list = new ArrayList<>();
		try {
			if(args!=null) {
				//risk.select.getEditModeData2
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
				Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				
				query.multiselect(de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
						de.get("rskContractNo").alias("RSK_CONTRACT_NO"),de.get("rskLayerNo").alias("RSK_LAYER_NO"),
						de.get("rskProductid").alias("RSK_PRODUCTID"),de.get("rskDeptid").alias("RSK_DEPTID"),
						de.get("rskPfcid").alias("RSK_PFCID"),de.get("rskSpfcid").alias("RSK_SPFCID"),
						de.get("rskPolbranch").alias("RSK_POLBRANCH"),de.get("rskCedingid").alias("RSK_CEDINGID"),
						de.get("rskBrokerid").alias("RSK_BROKERID"),de.get("rskTreatyid").alias("RSK_TREATYID"),
						de.get("rskMonth").alias("RSK_MONTH"),de.get("rskUwyear").alias("RSK_UWYEAR"),
						de.get("rskUnderwritter").alias("RSK_UNDERWRITTER"),de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
						de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),de.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
						de.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"),de.get("rskExchangeRate").alias("RSK_EXCHANGE_RATE"),
						de.get("rskBasis").alias("RSK_BASIS"),de.get("rskPeriodOfNotice").alias("RSK_PERIOD_OF_NOTICE"),
						de.get("rskRiskCovered").alias("RSK_RISK_COVERED"),de.get("rskTerritoryScope").alias("RSK_TERRITORY_SCOPE"),
						de.get("rskTerritory").alias("RSK_TERRITORY"),de.get("rskEntryDate").alias("RSK_ENTRY_DATE"),
						de.get("rskEndDate").alias("RSK_END_DATE"),de.get("rskStatus").alias("RSK_STATUS"),
						de.get("rskRemarks").alias("RSK_REMARKS"),pr.get("rskLimitOc").alias("RSK_LIMIT_OC"),
						pr.get("rskEpiOfferOc").alias("RSK_EPI_OFFER_OC"),pr.get("rskEpiEstimate").alias("RSK_EPI_ESTIMATE"),
						pr.get("rskEpiEstOc").alias("RSK_EPI_EST_OC"),pr.get("rskXlcostOc").alias("RSK_XLCOST_OC"),
						pr.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),pr.get("rskShareWritten").alias("RSK_SHARE_WRITTEN"),
						pr.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),de.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
						de.get("rskAccountingPeriod").alias("RSK_ACCOUNTING_PERIOD"),de.get("rskReceiptStatement").alias("RSK_RECEIPT_STATEMENT"),
						de.get("rskReceiptPayement").alias("RSK_RECEIPT_PAYEMENT"),de.get("retroCessionaries").alias("RETRO_CESSIONARIES"),
						pr.get("rskCedretType").alias("RSK_CEDRET_TYPE"),pr.get("rskSpRetro").alias("RSK_SP_RETRO"),
						pr.get("rskNoOfInsurers").alias("RSK_NO_OF_INSURERS"),pr.get("rskMaxLmtCover").alias("RSK_MAX_LMT_COVER"),
						de.get("rskRetroType").alias("RSK_RETRO_TYPE"),de.get("rskInsuredName").alias("RSK_INSURED_NAME"),
						de.get("oldContractno").alias("OLD_CONTRACTNO"),pm.get("loginId").alias("LOGIN_ID"),
						pr.get("limitPerVesselOc").alias("LIMIT_PER_VESSEL_OC"),pr.get("limitPerVesselDc").alias("LIMIT_PER_VESSEL_DC"),
						pr.get("limitPerLocationOc").alias("LIMIT_PER_LOCATION_OC"),pr.get("limitPerLocationDc").alias("LIMIT_PER_LOCATION_DC"),
						de.get("inwardBusType").alias("INWARD_BUS_TYPE"),de.get("treatytype").alias("TREATYTYPE"),
						pr.get("rskTreatySurpLimitOc").alias("RSK_TREATY_SURP_LIMIT_OC"),pr.get("rskTrtyLmtPmlOc").alias("RSK_TRTY_LMT_PML_OC"),
						pr.get("rskTrtyLmtSurPmlOc").alias("RSK_TRTY_LMT_SUR_PML_OC"),pr.get("rskTrtyLmtOurassPmlOc").alias("RSK_TRTY_LMT_OURASS_PML_OC"),
						pr.get("rskTreatySurpLimitDc").alias("RSK_TREATY_SURP_LIMIT_DC"),de.get("countriesInclude").alias("COUNTRIES_INCLUDE"),
						de.get("countriesExclude").alias("COUNTRIES_EXCLUDE"),de.get("rskNoOfLine").alias("RSK_NO_OF_LINE"),
						pr.get("rskPml").alias("RSK_PML"),pr.get("rskPmlPercent").alias("RSK_PML_PERCENT"),
						de.get("rskPerilsCovered").alias("RSK_PERILS_COVERED"),de.get("rskLocIssued").alias("RSK_LOC_ISSUED"),
						de.get("rsEndorsementType").alias("RS_ENDORSEMENT_TYPE"),de.get("rskRunOffYear").alias("RSK_RUN_OFF_YEAR"),
						de.get("rskLocBnkName").alias("RSK_LOC_BNK_NAME"),de.get("rskLocCrdtPrd").alias("RSK_LOC_CRDT_PRD"),
						de.get("rskLocCrdtAmt").alias("RSK_LOC_CRDT_AMT"),de.get("rskLocBenfcreName").alias("RSK_LOC_BENFCRE_NAME"),
						pm.get("baseLayer").alias("BASE_LAYER"),de.get("rskCessionExgRate").alias("RSK_CESSION_EXG_RATE"),
						de.get("rskFixedRate").alias("RSK_FIXED_RATE"),pm.get("rskDummyContract").alias("RSK_DUMMY_CONTRACT"),
						de.get("retentionyn").alias("RETENTIONYN"),pm.get("dataMapContNo").alias("DATA_MAP_CONT_NO"),
						pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"), //RI
						pm.get("bouquetNo").alias("BOUQUET_NO"),	pm.get("offerNo").alias("OFFER_NO"),
						pr.get("riskDetYn").alias("RISK_DET_YN"),	pr.get("brokerDetYn").alias("BROKER_DET_YN"), 
						pr.get("coverDetYn").alias("COVER_DET_YN"),	pr.get("premiumDetYn").alias("PREMIUM_DET_YN"), 
						pr.get("acqcostDetYn").alias("ACQCOST_DET_YN"),	pr.get("commDetYn").alias("COMM_DET_YN"), 
						pr.get("depositDetYn").alias("DEPOSIT_DET_YN"),	pr.get("lossDetYn").alias("LOSS_DET_YN"), 
						pr.get("docDetYn").alias("DOC_DET_YN"),	pr.get("paymentPartner").alias("PAYMENT_PARTNER"),
						pm.get("sectionNo").alias("SECTION_NO"),	pr.get("quotesharePercent").alias("QUOTESHARE_PERCENT"), 
						de.get("rskAccountPeriodNotice").alias("RSK_ACCOUNT_PERIOD_NOTICE"),de.get("rskStatementConfirm").alias("RSK_STATEMENT_CONFIRM")); 
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);
				//endDe
				Subquery<Long> endDe = query.subquery(Long.class); 
				Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
				endDe.select(cb.max(des.get("rskEndorsementNo")));
				Predicate b1 = cb.equal( des.get("rskProposalNumber"),args[1]);
				endDe.where(b1);
				//endPr
				Subquery<Long> endPr = query.subquery(Long.class); 
				Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
				endPr.select(cb.max(b.get("rskEndorsementNo")));
				Predicate c1 = cb.equal( b.get("rskProposalNumber"),args[2]);
				endPr.where(c1);

				Predicate n1 = cb.equal(de.get("rskProposalNumber"), args[0] );
				Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
				Predicate n5 = cb.equal(de.get("rskEndorsementNo"), endDe);
				Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
				Predicate n4 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
				Predicate n6 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n3,n4,n2,n5,n6);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
	}

	@Override
	public List<Tuple> getRetDetails(String[] args) {
		List<Tuple> list = new ArrayList<>();
		try {
			if(args!=null) {
				////GET_RET_DETAILS
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TtrnCedentRet> pm = query.from(TtrnCedentRet.class);
				query.multiselect(pm.get("rskSno").alias("RSK_SNO"),pm.get("rskClass").alias("RSK_CLASS"),
						pm.get("rskSubclass").alias("RSK_SUBCLASS"),pm.get("rskType").alias("RSK_TYPE"),
						pm.get("rskRettype").alias("RSK_RETTYPE"),pm.get("rskBasistype").alias("RSK_BASISTYPE"),
						pm.get("rskFirstRetOc").alias("RSK_FIRST_RET_OC"),pm.get("rskSecondRetOc").alias("RSK_SECOND_RET_OC"),
						pm.get("rskRetTlFstOc").alias("RSK_RET_TL_FST_OC"),pm.get("rskRetTlSstOc").alias("RSK_RET_TL_SST_OC"),
						pm.get("rskRetElFstOc").alias("RSK_RET_EL_FST_OC"),pm.get("rskRetElSstOc").alias("RSK_RET_EL_SST_OC"));		
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnCedentRet> e = amend.from(TtrnCedentRet.class);
				amend.select(cb.max(e.get("amendId")));
				Predicate c1 = cb.equal( e.get("proposalNo"), pm.get("proposalNo"));
				amend.where(c1);
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("rskSno")));
				
				Predicate n1 = cb.equal(pm.get("proposalNo"),args[0]);
				Predicate n2 = cb.equal(pm.get("layerNo"),args[1]);
				Predicate n4 = cb.equal(pm.get("amendId"),amend);
				query.where(n1,n2,n4);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
	}

	@Override
	public List<Tuple> getBaseLayerDetails(String productId, String branchCode, String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_BASE_LAYER_DETAILS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> rd = query.from(PositionMaster.class);

			query.multiselect(rd.get("proposalNo").alias("PROPOSAL_NO"),rd.get("baseLayer").alias("BASE_LAYER"),
					rd.get("contractNo").alias("CONTRACT_NO"),rd.get("contractStatus").alias("CONTRACT_STATUS")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> rds = amend.from(PositionMaster.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal( rds.get("proposalNo"), rd.get("proposalNo"));
			amend.where(a1);

			Predicate n1 = cb.equal(rd.get("productId"), productId);
			Predicate n2 = cb.equal(rd.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(rd.get("proposalNo"), proposalNo);
			Predicate n4 = cb.equal(rd.get("amendId"), amend);
			query.where(n1,n2,n3,n4);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
		}

	@Override
	public List<Tuple> riskSelectGetEditModeSecPageData(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
		 //risk.select.getEditModeSecPageData
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<TtrnRiskCommission> com = query.from(TtrnRiskCommission.class);
			
			query.multiselect(pr.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),pr.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
					pr.get("rskEpiOsofOc").alias("RSK_EPI_OSOF_OC"),pr.get("rskEpiOsofDc").alias("RSK_EPI_OSOF_DC"),
					pr.get("rskEpiOsoeOc").alias("RSK_EPI_OSOE_OC"),pr.get("rskEpiOsoeDc").alias("RSK_EPI_OSOE_DC"),
					pr.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC"),pr.get("rskXlcostOsDc").alias("RSK_XLCOST_OS_DC"),
					com.get("rskCommQuotashare").alias("RSK_COMM_QUOTASHARE"),com.get("rskCommSurplus").alias("RSK_COMM_SURPLUS"),
					com.get("rskOverriderPerc").alias("RSK_OVERRIDER_PERC"),com.get("rskBrokerage").alias("RSK_BROKERAGE"),
					com.get("rskTax").alias("RSK_TAX"),com.get("rskAcquistionCostOc").alias("RSK_ACQUISTION_COST_OC"),
					com.get("rskProfitComm").alias("RSK_PROFIT_COMM"),com.get("rskCombinSubClass").alias("RSK_COMBIN_SUB_CLASS"),
					com.get("rskPremiumReserve").alias("RSK_PREMIUM_RESERVE"),com.get("rskLossReserve").alias("RSK_LOSS_RESERVE"),
					com.get("rskInterest").alias("RSK_INTEREST"),com.get("rskCashlossLmtOc").alias("RSK_CASHLOSS_LMT_OC"),
					com.get("rskPfInoutPrem").alias("RSK_PF_INOUT_PREM"),com.get("rskPfInoutLoss").alias("RSK_PF_INOUT_LOSS"),
					com.get("rskLossadvice").alias("RSK_LOSSADVICE"),com.get("rskLeadUw").alias("RSK_LEAD_UW"),
					com.get("rskLeadUwShare").alias("RSK_LEAD_UW_SHARE"),com.get("rskAccounts").alias("RSK_ACCOUNTS"),
					com.get("rskExclusion").alias("RSK_EXCLUSION"),com.get("rskRemarks").alias("RSK_REMARKS"),
					com.get("rskUwRecomm").alias("RSK_UW_RECOMM"),com.get("rskGmApproval").alias("RSK_GM_APPROVAL"),
					com.get("rskSladscaleComm").alias("RSK_SLADSCALE_COMM"),com.get("rskLossPartCarridor").alias("RSK_LOSS_PART_CARRIDOR"),
					pr.get("rskPremiumQuotaShare").alias("RSK_PREMIUM_QUOTA_SHARE"),pr.get("rskPremiumSurpuls").alias("RSK_PREMIUM_SURPULS"),
					com.get("rskOtherCost").alias("RSK_OTHER_COST"),com.get("rskCreastaStatus").alias("RSK_CREASTA_STATUS"),
					com.get("rskEventLimitOc").alias("RSK_EVENT_LIMIT_OC"),com.get("rskEventLimitDc").alias("RSK_EVENT_LIMIT_DC"),
					com.get("rskAggregateLimitOc").alias("RSK_AGGREGATE_LIMIT_OC"),com.get("rskAggregateLimitDc").alias("RSK_AGGREGATE_LIMIT_DC"),
					com.get("rskOccurrentLimitOc").alias("RSK_OCCURRENT_LIMIT_OC"),com.get("rskOccurrentLimitDc").alias("RSK_OCCURRENT_LIMIT_DC"),
					com.get("rskLeadUnderwriterCountry").alias("RSK_LEAD_UNDERWRITER_COUNTRY"),com.get("rskIncludeAcqCost").alias("RSK_INCLUDE_ACQ_COST"),
					com.get("rskOurAssAcqCost").alias("RSK_OUR_ASS_ACQ_COST"),com.get("rskOurAcqOurShareOc").alias("RSK_OUR_ACQ_OUR_SHARE_OC"),
					com.get("rskLossCombinSubClass").alias("RSK_LOSS_COMBIN_SUB_CLASS"),com.get("rskSlideCombinSubClass").alias("RSK_SLIDE_COMBIN_SUB_CLASS"),
					com.get("rskCrestaCombinSubClass").alias("RSK_CRESTA_COMBIN_SUB_CLASS"),com.get("rskProManagementExp").alias("RSK_PRO_MANAGEMENT_EXP"),
					com.get("rskProCommType").alias("RSK_PRO_COMM_TYPE"),com.get("rskProCommPer").alias("RSK_PRO_COMM_PER"),
					com.get("rskProSetUp").alias("RSK_PRO_SET_UP"),com.get("rskProSupProCom").alias("RSK_PRO_SUP_PRO_COM"),
					com.get("rskProLossCaryType").alias("RSK_PRO_LOSS_CARY_TYPE"),com.get("rskProLossCaryYear").alias("RSK_PRO_LOSS_CARY_YEAR"),
					com.get("rskProProfitCaryType").alias("RSK_PRO_PROFIT_CARY_TYPE"),com.get("rskProProfitCaryYear").alias("RSK_PRO_PROFIT_CARY_YEAR"),
					com.get("rskProFirstPfoCom").alias("RSK_PRO_FIRST_PFO_COM"),com.get("rskProFirstPfoComPrd").alias("RSK_PRO_FIRST_PFO_COM_PRD"),
					com.get("rskProSubPfoComPrd").alias("RSK_PRO_SUB_PFO_COM_PRD"),com.get("rskProSubPfoCom").alias("RSK_PRO_SUB_PFO_COM"),
					com.get("rskProSubSeqCal").alias("RSK_PRO_SUB_SEQ_CAL"),com.get("rskProNotes").alias("RSK_PRO_NOTES"),
					com.get("rskCommissionQsYn").alias("RSK_COMMISSION_QS_YN"),com.get("rskNoclaimbonusPrcent").alias("RSK_NOCLAIMBONUS_PRCENT"),
					com.get("rskBonusId").alias("RSK_BONUS_ID"),com.get("rskCommissionSurYn").alias("RSK_COMMISSION_SUR_YN"),
					com.get("rskOverrideYn").alias("RSK_OVERRIDE_YN"),com.get("rskBrokarageYn").alias("RSK_BROKARAGE_YN"),
					com.get("rskTaxYn").alias("RSK_TAX_YN"),com.get("rskOtherCostYn").alias("RSK_OTHER_COST_YN"),
					com.get("rskCeedOdiYn").alias("RSK_CEED_ODI_YN"),com.get("rskRate").alias("RSK_RATE"),
					com.get("rskCommissionType").alias("RSK_COMMISSION_TYPE"),com.get("rskPremiumResType").alias("RSK_PREMIUM_RES_TYPE"),
					com.get("rskPortfolioType").alias("RSK_PORTFOLIO_TYPE"),com.get("fpcType").alias("FPC_TYPE"),
					com.get("fpcFixedDate").alias("FPC_FIXED_DATE")); 
			
			//endPr
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskProposal> prs = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(prs.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( prs.get("rskProposalNumber"), proposalNo);
			endPr.where(a1);
			//endCom
			Subquery<Long> endCom = query.subquery(Long.class); 
			Root<TtrnRiskProposal> coms = endCom.from(TtrnRiskProposal.class);
			endCom.select(cb.max(coms.get("rskEndorsementNo")));
			Predicate b1 = cb.equal( coms.get("rskProposalNumber"), proposalNo);
			endCom.where(b1);

			Predicate n1 = cb.equal(pr.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(pr.get("rskProposalNumber"), com.get("rskProposalNumber"));
			Predicate n3 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n4 = cb.equal(com.get("rskEndorsementNo"), endCom);
			query.where(n1,n2,n3,n4);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
			return list;
		}

	@Override
	public List<Tuple> riskSelectGetQuotaShare(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			 //risk.select.getQuotaShare
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);

			query1.multiselect(rd.get("rskPremiumQuotaShare").alias("RSK_PREMIUM_QUOTA_SHARE"),rd.get("rskPremiumSurpuls").alias("RSK_PREMIUM_SURPULS"),rd.get("rskXlcostOsOc").alias("RSK_XLCOST_OS_OC")); 

			Subquery<Long> endA = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
			endA.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			Predicate a2 = cb.isNotNull(rds.get("rskPremiumQuotaShare"));
			endA.where(a1,a2);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			Predicate n3 = cb.isNotNull(rd.get("rskPremiumQuotaShare"));
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
			query1.where(n1,n2,n3);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
				return list;
		}

	@Override
	public List<Tuple> riskSelectCommGetquotashare(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.getQuotaShare
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);

			query1.multiselect(rd.get("commQsAmt").alias("COMM_QS_AMT"),rd.get("commSurplusAmt").alias("COMM_SURPLUS_AMT")); 

			Subquery<Long> endA = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> rds = endA.from(TtrnRiskProposal.class);
			endA.select(cb.max(rds.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			endA.where(a1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), endA);
			query1.where(n1,n2);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Map<String, Object>>  riskSelectCommGetquotashare(String icepDate, String contId, String deptId) {
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
			Predicate a3 = cb.equal( pm.get("deptId"), pms.get("deptId"));
			amend.where(a3,a1,a2);

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(pm.get("uwYear")));

			// Where
			Predicate n1 = cb.equal(pm.get("contractNo"), contId);
			Predicate n2 = cb.equal(pm.get("renewalStatus"), "0");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			Predicate n4 = cb.equal(pm.get("deptId"), deptId);
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();

			for(Tuple data :  list ) {
			values.put("UW_YEAR" , data.get("uwYear"));
			values.put("EXPIRY_DATE" , dbf.format(data.get("expiryDate")));
			LocalDate d1 = LocalDate.parse(dbf.format( sdf.parse( icepDate)), DateTimeFormatter.ISO_LOCAL_DATE);
			LocalDate d2 = LocalDate.parse(  dbf.format(data.get("expiryDate")), DateTimeFormatter.ISO_LOCAL_DATE);
			Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());
			long diffDays = diff.toDays();	
			values.put("DIFF" , diffDays);
			response.add(values);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return response;	
	}

	@Override
	public String getBaseProposalNo(String proposalNo) {
		List<Long> list = null;
		String propNo= "";
		try {
			//GET_BASE_PROPOSAL_NO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query1 = cb.createQuery(Long.class); 
			Root<PositionMaster> rd = query1.from(PositionMaster.class);

			query1.multiselect(cb.selectCase().when(cb.isNull(rd.get("proposalNo")), 0l)
					.otherwise(rd.get("proposalNo"))).distinct(true); 
			//contractNo
			Subquery<Long> contNo = query1.subquery(Long.class); 
			Root<PositionMaster> rds = contNo.from(PositionMaster.class);
			contNo.select(rds.get("oldContractno")).distinct(true);
			//amend
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PositionMaster> e = amend.from(PositionMaster.class);
			amend.select(cb.max(e.get("amendId")));
			Predicate c1 = cb.equal(e.get("proposalNo"), rds.get("proposalNo"));
			amend.where(c1);
			Predicate a1 = cb.equal( rds.get("proposalNo"), proposalNo);
			Predicate a2 = cb.equal( rds.get("amendId"), amend);
			contNo.where(a1,a2);
			
			//amendId
			Subquery<Long> amen = query1.subquery(Long.class); 
			Root<PositionMaster> p = amen.from(PositionMaster.class);
			amen.select(cb.max(p.get("amendId")));
			Predicate d1 = cb.equal(p.get("proposalNo"), rd.get("proposalNo"));
			amen.where(d1);
			
			Predicate n1 = cb.isNull(rd.get("baseLayer"));
			Predicate n2 = cb.equal(rd.get("contractNo"), contNo);
			Predicate n3 = cb.equal(rd.get("amendId"), amen);
			query1.where(n1,n2,n3);

			TypedQuery<Long> result = em.createQuery(query1);
			list = result.getResultList();
			if(!CollectionUtils.isEmpty(list)) {
				propNo=list.get(0)==null?"0":list.get(0).toString();
			}else {
				propNo="0";
			}
			
			}catch(Exception e) {
				e.printStackTrace();
				}
			return propNo;
	}

	@Override
	public List<Tuple> profitCommissionEnable(String baseLayer) {
		List<Tuple> list = new ArrayList<>();
		try {
			//PROFIT_COMMISSION_ENABLE
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskCommission> rd = query1.from(TtrnRiskCommission.class);

			query1.multiselect(rd.get("rskProfitComm").alias("RSK_PROFIT_COMM"),rd.get("rskCombinSubClass").alias("RSK_COMBIN_SUB_CLASS")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), baseLayer);
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),baseLayer);
			Predicate n3 = cb.equal(rd.get("rskEndorsementNo"), end);
			query1.where(n1,n3);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
				return null;
				}
			return list;
	}

	@Override
	public List<Tuple> profitCommissionEnableLoss(String baseLayer) {
		List<Tuple> list = new ArrayList<>();
		try {
			//PROFIT_COMMISSION_ENABLE_LOSS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskCommission> rd = query1.from(TtrnRiskCommission.class);

			query1.multiselect(rd.get("rskLossPartCarridor").alias("RSK_LOSS_PART_CARRIDOR"),rd.get("rskLossCombinSubClass").alias("RSK_LOSS_COMBIN_SUB_CLASS")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), baseLayer);
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),baseLayer);
			Predicate n3 = cb.equal(rd.get("rskEndorsementNo"), end);
			query1.where(n1,n3);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> profitCommissionEnableCresta(String baseLayer) {
		List<Tuple> list = new ArrayList<>();
		try {
			//PROFIT_COMMISSION_ENABLE_CRESTA
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskCommission> rd = query1.from(TtrnRiskCommission.class);

			query1.multiselect(rd.get("rskCreastaStatus").alias("RSK_CREASTA_STATUS"),rd.get("rskCrestaCombinSubClass").alias("RSK_CRESTA_COMBIN_SUB_CLASS")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), baseLayer);
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),baseLayer);
			Predicate n3 = cb.equal(rd.get("rskEndorsementNo"), end);
			query1.where(n1,n3);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> profitCommissionEnableSlide(String baseLayer) {
		List<Tuple> list = new ArrayList<>();
		try {
			//PROFIT_COMMISSION_ENABLE_SLIDE
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskCommission> rd = query1.from(TtrnRiskCommission.class);

			query1.multiselect(rd.get("rskSladscaleComm").alias("RSK_SLADSCALE_COMM"),rd.get("rskSlideCombinSubClass").alias("RSK_SLIDE_COMBIN_SUB_CLASS")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), baseLayer);
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),baseLayer);
			Predicate n3 = cb.equal(rd.get("rskEndorsementNo"), end);
			query1.where(n1,n3);

			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public int commissionTypeCount(String[] args) {
		int count =0;
		String val = "0";
		try {
			//COMMISSION_TYPE_COUNT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query1 = cb.createQuery(Long.class); 
			Root<TtrnCommissionDetails> rd = query1.from(TtrnCommissionDetails.class);

			query1.multiselect(cb.count(rd)); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnCommissionDetails> p = end.from(TtrnCommissionDetails.class);
			end.select(cb.max(p.get("endorsementNo")));
			Predicate d1 = cb.equal(p.get("proposalNo"), rd.get("proposalNo"));
			Predicate d2 = cb.equal(p.get("branchCode"), rd.get("branchCode"));
			end.where(d1,d2);
			
			Predicate n1 = cb.equal(rd.get("proposalNo"),args[0]);
			Predicate n2 = cb.equal(rd.get("branchCode"), args[1]); 
			Predicate n3 = cb.equal(rd.get("commissionType"), args[2]); 
			Predicate n4 = cb.equal(rd.get("endorsementNo"), end); 
			query1.where(n1,n3,n2,n4);

			TypedQuery<Long> result = em.createQuery(query1);
			List<Long>list = result.getResultList();
			
			if (!CollectionUtils.isEmpty(list)) {
				val = list.get(0) == null ?"0": list.get(0).toString();
			}
			count = Integer.valueOf(val);
			}catch(Exception e) {
				e.printStackTrace();
				}
			return count;
	}

	@Override
	public String getSignShareProduct23(String proposalNo) {
		String val = "0";
		try {
			//COMMISSION_TYPE_COUNT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<BigDecimal> query1 = cb.createQuery(BigDecimal.class); 
			Root<TtrnRiskProposal> rd = query1.from(TtrnRiskProposal.class);

			query1.select(rd.get("rskShareSigned")); 
			
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskProposal> p = end.from(TtrnRiskProposal.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), rd.get("rskProposalNumber"));
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);

			TypedQuery<BigDecimal> result = em.createQuery(query1);
			List<BigDecimal>list = result.getResultList();
			if (!CollectionUtils.isEmpty(list)) {
				val = list.get(0) == null ?"0": list.get(0).toString();
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return val;
	}

	@Override
	public List<Tuple> riskSelectGetBrokerage(String layerProposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//COMMISSION_TYPE_COUNT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRiskCommission> rd = query1.from(TtrnRiskCommission.class);
			query1.multiselect(rd.get("rskBrokerage").alias("RSK_BROKERAGE"),rd.get("rskTax").alias("RSK_TAX")); 
			//amendId
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskCommission> p = end.from(TtrnRiskCommission.class);
			end.select(cb.max(p.get("rskEndorsementNo")));
			Predicate d1 = cb.equal(p.get("rskProposalNumber"), layerProposalNo);
			end.where(d1);
			
			Predicate n1 = cb.equal(rd.get("rskProposalNumber"),layerProposalNo);
			Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> facSelectViewInsDetails(String[] args) {
		List<Tuple> list = new ArrayList<>();
		try {
			//fac.select.viewInsDetails
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnInsurerDetails> rd = query1.from(TtrnInsurerDetails.class);
			query1.multiselect(rd.get("retroPercentage").alias("RETRO_PER"),rd.get("type").alias("TYPE"),
					rd.get("uwYear").alias("UW_YEAR"),rd.get("retroType").alias("RETRO_TYPE"),
					cb.selectCase().when(cb.equal(rd.get("contractNo"),"0"), "").otherwise(rd.get("contractNo")).alias("CONTRACTNO")); 
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("insurerNo")));
			
			Predicate n1 = cb.equal(rd.get("endorsementNo"),args[0]);
			Predicate n2 = cb.equal(rd.get("proposalNo"), args[1]); 
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, fm.formatBigDecimal(args[2]));
			query1.where(n1,n2,n3).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> facSelectInsDetails(String[] args) {
		List<Tuple> list = new ArrayList<>();
		try {
			//fac.select.viewInsDetails
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnInsurerDetails> rd = query1.from(TtrnInsurerDetails.class);
			query1.multiselect(rd.get("retroPercentage").alias("RETRO_PER"),rd.get("type").alias("TYPE"),
					rd.get("uwYear").alias("UW_YEAR"),rd.get("retroType").alias("RETRO_TYPE"),
					cb.selectCase().when(cb.equal(rd.get("contractNo"),"0"), "").otherwise(rd.get("contractNo")).alias("CONTRACTNO")); 
			//end
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnInsurerDetails> coms = end.from(TtrnInsurerDetails.class);
			end.select(cb.max(coms.get("endorsementNo")));
			Predicate b1 = cb.equal( coms.get("proposalNo"), rd.get("proposalNo"));
			Predicate b2 = cb.equal( coms.get("insurerNo"), rd.get("insurerNo"));
			end.where(b1,b2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("insurerNo")));
			
			Predicate n1 = cb.equal(rd.get("endorsementNo"),end);
			Predicate n2 = cb.equal(rd.get("proposalNo"), args[0]); 
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, fm.formatBigDecimal(args[1]));
			query1.where(n1,n2,n3).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public String getMaxSectionNoDet(String proposalNo) {
		String secNo = "";
		//GET_MAX_SECTION_NO_DET
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<BigDecimal> query1 = cb.createQuery(BigDecimal.class); 
		Root<PositionMaster> rd = query1.from(PositionMaster.class);
		query1.select(cb.max(rd.get("sectionNo")));  
		
		Predicate n1 = cb.equal(cb.coalesce(rd.get("baseLayer"), rd.get("proposalNo")),proposalNo);
		query1.where(n1);

		TypedQuery<BigDecimal> result = em.createQuery(query1);
		List<BigDecimal>list = result.getResultList();
		if(list!=null) {
			secNo = String.valueOf((list.get(0)==null?"1":(Integer.valueOf(list.get(0).toString())+1)));
		}
		return secNo;
	}

	//	 (select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select *
	//	 from table(SPLIT_TEXT_FN(replace(RK.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = RK.RSK_PRODUCTID AND PERSONAL.BRANCH_CODE = 
	//	 SPFC.BRANCH_CODE)TMAS_SPFC_NAME
	@Override
	public List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId) {
	//risk.select.getSecPageData //TMAS_SPFC_NAME pending
	List<Tuple> list = new ArrayList<>();
	try {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiskDetails> rk = query1.from(TtrnRiskDetails.class);
		Root<PersonalInfo> personal = query1.from(PersonalInfo.class);
		Root<TtrnRiskProposal> rp = query1.from(TtrnRiskProposal.class);
		Root<PersonalInfo> pi = query1.from(PersonalInfo.class);
		Root<TtrnRiskCommission> rc = query1.from(TtrnRiskCommission.class);
	
		//deptName
		Subquery<String> deptName = query1.subquery(String.class); 
		Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
		deptName.select(coms.get("tmasDepartmentName"));
		Predicate a1 = cb.equal( coms.get("tmasDepartmentId"), rk.get("rskDeptid"));
		Predicate a2 = cb.equal( coms.get("tmasProductId"), rk.get("rskProductid"));
		Predicate a3 = cb.equal( coms.get("branchCode"), personal.get("branchCode"));
		Predicate a4 = cb.equal( coms.get("tmasStatus"), "Y");
		deptName.where(a1,a2,a3,a4);
		
		Expression<String> e0 = cb.concat(pi.get("firstName"), " ");
		
		query1.multiselect(rk.get("rskContractNo").alias("RSK_CONTRACT_NO"),
				rk.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
				personal.get("companyName").alias("CEDING_COMPANY"),
				rk.get("rskCedingid").alias("RSK_CEDINGID"),
				rk.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
				rp.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
				rp.get("rskLimitOc").alias("RSK_LIMIT_OC"),
				rp.get("rskLimitDc").alias("RSK_LIMIT_DC"),
				rp.get("rskLimitOsOc").alias("RSK_LIMIT_OS_OC"),
				rp.get("rskLimitOsDc").alias("RSK_LIMIT_OS_DC"),
				rp.get("rskCedantRetention").alias("RSK_CEDANT_RETENTION"),
				rk.get("rskInceptionDate").alias("INCP_DATE"),
				rk.get("rskAccountDate").alias("RSK_ACCOUNT_DATE"),
				rk.get("rskExpiryDate").alias("EXP_DATE"),
				rk.get("rskTreatyid").alias("RSK_TREATYID"),
				rp.get("rskPfCovered").alias("RSK_PF_COVERED"),
				rk.get("rskInsuredName").alias("RSK_INSURED_NAME"),
				rk.get("rskRiskCovered").alias("RSK_RISK_COVERED"),
				cb.concat(e0, pi.get("lastName")).alias("BROKER_NAME"),
				rk.get("rskBrokerid").alias("RSK_BROKERID"),
				rk.get("rskProposalType").alias("RSK_PROPOSAL_TYPE"),
				rk.get("rskBasis").alias("RSK_BASIS"),
				rc.get("rskCashlossLmtOc").alias("RSK_CASHLOSS_LMT_OC"),
				rc.get("rskCashlossLmtDc").alias("RSK_CASHLOSS_LMT_DC"),
				deptName.alias("TMAS_DEPARTMENT_NAME"),
				rk.get("rskSpfcid").alias("RSK_SPFCID"),
				rk.get("rskDeptid").alias("RSK_DEPTID"),
				rk.get("rskUwyear").alias("RSK_UWYEAR"),
				rc.get("rskReinstatementPremium").alias("RSK_REINSTATEMENT_PREMIUM")
				); 
		//amend
		Subquery<Long> amend = query1.subquery(Long.class); 
		Root<PersonalInfo> pis = amend.from(PersonalInfo.class);
		amend.select(cb.max(pis.get("amendId")));
		Predicate b1 = cb.equal( pis.get("customerId"), personal.get("customerId"));
		Predicate b2 = cb.equal( pis.get("branchCode"), personal.get("branchCode"));
		Predicate b3 = cb.equal( pis.get("customerType"), personal.get("customerType"));
		amend.where(b1,b2,b3);
		
		//amend
		Subquery<Long> amendPi = query1.subquery(Long.class); 
		Root<PersonalInfo> pi1 = amendPi.from(PersonalInfo.class);
		amendPi.select(cb.max(pi1.get("amendId")));
		Predicate c1 = cb.equal( pi1.get("customerId"), pi.get("customerId"));
		Predicate c2 = cb.equal( pi1.get("branchCode"), pi.get("branchCode"));
		Predicate c3 = cb.equal( pi1.get("customerType"), pi.get("customerType"));
		amendPi.where(c1,c2,c3);
		
		//end
		Subquery<Long> end = query1.subquery(Long.class); 
		Root<TtrnRiskDetails> rks = end.from(TtrnRiskDetails.class);
		end.select(cb.max(rks.get("rskEndorsementNo")));
		Predicate d1 = cb.equal( rks.get("rskProposalNumber"), proposalNo);
		end.where(d1);
		
		//endRp
		Subquery<Long> endRp = query1.subquery(Long.class); 
		Root<TtrnRiskProposal> rps = endRp.from(TtrnRiskProposal.class);
		endRp.select(cb.max(rps.get("rskEndorsementNo")));
		Predicate e1 = cb.equal( rps.get("rskProposalNumber"),  rk.get("rskProposalNumber"));
		endRp.where(e1);
		
		//endRp
		Subquery<Long> endRc = query1.subquery(Long.class); 
		Root<TtrnRiskCommission> rcs = endRc.from(TtrnRiskCommission.class);
		endRc.select(cb.max(rcs.get("rskEndorsementNo")));
		Predicate f1 = cb.equal( rcs.get("rskProposalNumber"),  rc.get("rskProposalNumber"));
		endRc.where(f1);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(rk.get("rskProposalNumber"),proposalNo));
		predicates.add(cb.equal(rk.get("rskProductid"),productId));
		predicates.add(cb.equal(rk.get("rskCedingid"),personal.get("customerId")));
		predicates.add(cb.equal(personal.get("branchCode"),branchCode));
		predicates.add(cb.equal(personal.get("customerType"),"C"));
		predicates.add(cb.equal(personal.get("amendId"),amend));
		predicates.add(cb.equal(rk.get("rskBrokerid"),pi.get("customerId")));
		predicates.add(cb.equal(pi.get("branchCode"),branchCode));
		predicates.add(cb.equal(pi.get("customerType"),"B"));
		predicates.add(cb.equal(pi.get("amendId"),amendPi));
		predicates.add(cb.equal(rp.get("rskProposalNumber"),rk.get("rskProposalNumber")));
		predicates.add(cb.equal(personal.get("customerId"),rk.get("rskCedingid")));
		predicates.add(cb.equal(rk.get("rskEndorsementNo"),end));
		predicates.add(cb.equal(rp.get("rskEndorsementNo"),endRp));
		predicates.add(cb.equal(rc.get("rskProposalNumber"),rp.get("rskProposalNumber")));
		predicates.add(cb.equal(rc.get("rskEndorsementNo"),endRc));
	
		query1.where(predicates.toArray(new Predicate[0]));
	
		TypedQuery<Tuple> result = em.createQuery(query1);
		list = result.getResultList();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String riskSelectCeaseStatus(String proposalNo) {
		String ceaseStatus = "";
		try {
			//risk.select.CEASE_STATUS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query1 = cb.createQuery(String.class); 
			Root<PositionMaster> pm = query1.from(PositionMaster.class);
			
			query1.select(pm.get("ceaseStatus")); 
			
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<PositionMaster> rcs = amend.from(PositionMaster.class);
			amend.select(cb.max(rcs.get("amendId")));
			Predicate f1 = cb.equal( rcs.get("proposalNo"),  pm.get("proposalNo"));
			amend.where(f1);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"),proposalNo);
			Predicate n2 = cb.equal(pm.get("amendId"), amend); 
			query1.where(n1,n2);
	
			TypedQuery<String> result = em.createQuery(query1);
			List<String> list = result.getResultList();
			
			if(list!=null) {
				ceaseStatus = list.get(0)==null?"":list.get(0);
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ceaseStatus;
	}

	@Override
	public TtrnRiskProposal riskUpdatePro24ContSecPage(String[] args) {

		TtrnRiskProposal ttrnRiskProposal = null;
		//risk.update.pro24ContSecPage
		try {
		if(args != null) {
			ttrnRiskProposal = ttrnRiskProposalRepository.findByRskEndorsementNoAndRskProposalNumber(
					fm.formatBigDecimal(args[16]),args[17]);
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskLimitOsOc(fm.formatBigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitOsDc(fm.formatBigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOsofOc(fm.formatBigDecimal(args[2]));
				ttrnRiskProposal.setRskEpiOsofDc(fm.formatBigDecimal(args[3]));
				ttrnRiskProposal.setRskEpiOsoeOc(fm.formatBigDecimal(args[4]));
				ttrnRiskProposal.setRskEpiOsoeDc(fm.formatBigDecimal(args[5]));
				ttrnRiskProposal.setRskXlcostOsOc(fm.formatBigDecimal(args[6]));	
				ttrnRiskProposal.setRskXlcostOsDc(fm.formatBigDecimal(args[7]));
				ttrnRiskProposal.setRskPremiumQuotaShare(fm.formatBigDecimal(args[8]));
				ttrnRiskProposal.setRskPremiumSurpuls(fm.formatBigDecimal(args[9]));	
				ttrnRiskProposal.setRskPremiumQuotaShareDc(fm.formatBigDecimal(args[10]));
				ttrnRiskProposal.setRskPremiumSurplusDc(fm.formatBigDecimal(args[11]));
				ttrnRiskProposal.setCommQsAmt(fm.formatBigDecimal(args[12]));
				ttrnRiskProposal.setCommSurplusAmt(fm.formatBigDecimal(args[13]));
				ttrnRiskProposal.setCommQsAmtDc(fm.formatBigDecimal(args[14]));	
				ttrnRiskProposal.setCommSurplusAmtDc(fm.formatBigDecimal(args[15]));
					}
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnRiskProposal;
	
	}

	@Override
	public String riskSelectGetRskContractNo(String proposalNo) {
		String contNo = "";
		try {
			//risk.select.getRskContractNo
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query1 = cb.createQuery(String.class); 
			Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);
			
			query1.select(cb.coalesce(pm.get("rskContractNo"), "0")); 
			
			Subquery<Long> end = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> rcs = end.from(TtrnRiskDetails.class);
			end.select(cb.max(rcs.get("rskEndorsementNo")));
			Predicate f1 = cb.equal( rcs.get("rskProposalNumber"),  pm.get("rskProposalNumber"));
			end.where(f1);
			
			Predicate n1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
			Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), end); 
			query1.where(n1,n2);
	
			TypedQuery<String> result = em.createQuery(query1);
			List<String> list = result.getResultList();
			
			if(list!=null) {
				contNo = list.get(0)==null?"":list.get(0);
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return contNo;
	}

	@Override
	public TtrnPttySection insertSectionDetails(String[] args) {
		TtrnPttySection ttrnPttySection = new TtrnPttySection();;
		//INSERT_SECTION_DETAILS
		try {
		if(args != null) {
			ttrnPttySection.setSectionNo(args[0]);
			ttrnPttySection.setContractNo(args[1]);
			ttrnPttySection.setDeptId(args[2]);
			ttrnPttySection.setSectionName(args[3]);
			ttrnPttySection.setAmendId("0");
			ttrnPttySection.setBranchCode(args[4]);
			ttrnPttySection.setLoginId(args[5]);			
			ttrnPttySection.setEntry_date(new Date());
			
		}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return ttrnPttySection;
	}

	@Override
	public int getCountRetention(String proposalNo, String productId) {
		int count = 0;
		try {
			//GET_COUNT_RETENTION
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Integer> query1 = cb.createQuery(Integer.class); 
			Root<TtrnCedentRet> pm = query1.from(TtrnCedentRet.class);
			
			query1.multiselect(cb.count(pm)); 
			
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnCedentRet> rcs = amend.from(TtrnCedentRet.class);
			amend.select(cb.max(rcs.get("amendId")));
			Predicate f1 = cb.equal( rcs.get("proposalNo"),  pm.get("proposalNo"));
			amend.where(f1);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"),proposalNo);
			Predicate n2 = cb.equal(pm.get("productId"), productId); 
			Predicate n3 = cb.equal(pm.get("amendId"), amend); 
			query1.where(n1,n2,n3);
	
			TypedQuery<Integer> result = em.createQuery(query1);
			List<Integer> list = result.getResultList();
			
			if(list!=null) {
				count = list.get(0)==null?0:list.get(0);
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return count;
	}

	@Override
	public void updateRetenContno(String contNo, String productId, String proposalNo, String departmentId) {
		try{
			//UPDATE_RETEN_CONTNO
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnCedentRet> update = cb.createCriteriaUpdate(TtrnCedentRet.class);
			Root<TtrnCedentRet> m = update.from(TtrnCedentRet.class);
			
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<TtrnCedentRet> rcs = amend.from(TtrnCedentRet.class);
			amend.select(cb.max(rcs.get("amendId")));
			Predicate f1 = cb.equal( rcs.get("proposalNo"),  m.get("proposalNo"));
			amend.where(f1);
		
			update.set("contractNo", contNo);
			update.set("deptId", departmentId);
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(m.get("productId"), productId);
			Predicate n3 = cb.equal(m.get("amendId"), amend); 
			update.where(n1,n2,n3 );
			
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
			}
	}

	@Override
	public List<Tuple> bonusMainSelect(String proposalNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try{
			//BONUS_MAIN_SELECT
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("lcbId").alias("LCB_ID"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("quotaShare").alias("QUOTA_SHARE"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
					pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
					pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
					pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
					pm.get("subSecCal").alias("SUB_SEC_CAL"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
		 			pm.get("fpcType").alias("FPC_TYPE"),
					pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
					
		
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), pm.get("proposalNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			Predicate a4 = cb.equal(rds.get("lcbType"), pm.get("lcbType"));
			end.where(a1,a2,a3,a4);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), "SSC");
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n5 = cb.equal(pm.get("lcbType"), "SSC2");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> bonusMainSelectReference(String referenceNo, String branchCode) {
		
		List<Tuple> list = new ArrayList<>();
		try{
			// bonus_Main_Select_Reference
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("lcbId").alias("LCB_ID"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("quotaShare").alias("QUOTA_SHARE"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
					pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
					pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
					pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
					pm.get("subSecCal").alias("SUB_SEC_CAL"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
		 			pm.get("fpcType").alias("FPC_TYPE"),
					pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
		
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("referenceNo"), pm.get("referenceNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			Predicate a4 = cb.equal(rds.get("lcbType"), pm.get("lcbType"));
			end.where(a1,a2,a3,a4);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("referenceNo"), referenceNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), "SSC");
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n5 = cb.equal(pm.get("lcbType"), "SSC2");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> bonusMainSelectLpc(String proposalNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<TtrnBonus> pm = query.from(TtrnBonus.class);
		query.multiselect(
				pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("contractNo").alias("CONTRACT_NO"),
				pm.get("productId").alias("PRODUCT_ID"),
				pm.get("lcbType").alias("LCB_TYPE"),
				pm.get("lcbFrom").alias("LCB_FROM"),
				pm.get("lcbTo").alias("LCB_TO"),
				pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
				pm.get("userId").alias("USERID"),
				pm.get("branch").alias("BRANCH"),
				pm.get("lcbId").alias("LCB_ID"),
				pm.get("type").alias("TYPE"),
				pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
				pm.get("subClass").alias("SUB_CLASS"),
				pm.get("sysDate").alias("SYS_DATE"),
				pm.get("layerNo").alias("LAYERNO"),
				pm.get("quotaShare").alias("QUOTA_SHARE"),
				pm.get("remarks").alias("REMARKS"),
				pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
				pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
				pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
				pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
				pm.get("subSecCal").alias("SUB_SEC_CAL"),
				pm.get("referenceNo").alias("REFERENCE_NO"),
				pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
	 			pm.get("fpcType").alias("FPC_TYPE"),
				pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
				
	
		//end
		Subquery<Long> end = query.subquery(Long.class);
		Root<TtrnBonus> rds = end.from(TtrnBonus.class);
		end.select(cb.max(rds.get("endorsementNo")));
		Predicate a1 = cb.equal(rds.get("proposalNo"), pm.get("proposalNo"));
		Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
		Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
		end.where(a1,a2,a3);
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.asc(pm.get("lcbId")));

		Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
		Predicate n2 = cb.equal(pm.get("branch"), branchCode);
		Predicate n3 = cb.equal(pm.get("type"), "LPC");
		Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
		query.where(n1,n2,n3,n4).orderBy(orderList);
		
		TypedQuery<Tuple> res1 = em.createQuery(query);
		list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
			}
		return list;
	}

	@Override
	public List<Tuple> bonusMainSelectReferenceLpc(String referenceNo, String branchCode) {

		List<Tuple> list = new ArrayList<>();
		try{
			// bonus_Main_Select_Reference
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("lcbId").alias("LCB_ID"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("quotaShare").alias("QUOTA_SHARE"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("firstProfitComm").alias("FIRST_PROFIT_COMM"),
					pm.get("fpcDurationType").alias("FPC_DURATION_TYPE"),
					pm.get("subProfitComm").alias("SUB_PROFIT_COMM"),
					pm.get("spcDurationType").alias("SPC_DURATION_TYPE"),
					pm.get("subSecCal").alias("SUB_SEC_CAL"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("scaleMaxPartPercent").alias("SCALE_MAX_PART_PERCENT"),
		 			pm.get("fpcType").alias("FPC_TYPE"),
					pm.get("fpcFixedDate").alias("FPC_FIXED_DATE"));
		
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("referenceNo"), pm.get("referenceNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			end.where(a1,a2,a3);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("referenceNo"), referenceNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), "LPC");
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> selectSlidingScaleMethodInfo(String proposalNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try{
			//SELECT_SLIDING_SCALE_METHOD_INFO
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = query.from(TtrnBonus.class);
			query.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("provisionalCommisiion").alias("PROVISIONAL_COMMISIION"),
					pm.get("scMethodType").alias("SC_METHOD_TYPE"),
					pm.get("scMinLossRatio").alias("SC_MIN_LOSS_RATIO"),
					pm.get("scMaxLossRatio").alias("SC_MAX_LOSS_RATIO"),
					pm.get("scCombineLossRatio").alias("SC_COMBINE_LOSS_RATIO"),
					pm.get("scBandingStep").alias("SC_BANDING_STEP"),
					pm.get("scNoOfDigit").alias("SC_NO_OF_DIGIT"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("deltaLossRatio").alias("DELTA_LOSS_RATIO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"));
				
			//end
			Subquery<Long> end = query.subquery(Long.class);
			Root<TtrnBonus> rds = end.from(TtrnBonus.class);
			end.select(cb.max(rds.get("endorsementNo")));
			Predicate a1 = cb.equal(rds.get("proposalNo"), pm.get("proposalNo"));
			Predicate a2 = cb.equal(rds.get("branch"), pm.get("branch"));
			Predicate a3 = cb.equal(rds.get("type"), pm.get("type"));
			Predicate a4 = cb.equal(rds.get("lcbType"), pm.get("lcbType"));
			end.where(a1,a2,a3,a4);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("lcbId")));
	
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("branch"), branchCode);
			Predicate n3 = cb.equal(pm.get("type"), "SSC");
			Predicate n4 = cb.equal(pm.get("endorsementNo"), end);
			Predicate n5 = cb.equal(pm.get("lcbType"), "SSC1");
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public List<Tuple> selectSlidingScaleMethodInfoRef(String referenceNo, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try{
			//SELECT_SLIDING_SCALE_METHOD_INFO_REF
		 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class); 
			Root<TtrnBonus> pm = cq.from(TtrnBonus.class);
			cq.multiselect(
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("productId").alias("PRODUCT_ID"),
					pm.get("lcbType").alias("LCB_TYPE"),
					pm.get("provisionalCommisiion").alias("PROVISIONAL_COMMISIION"),
					pm.get("scMethodType").alias("SC_METHOD_TYPE"),
					pm.get("scMinLossRatio").alias("SC_MIN_LOSS_RATIO"),
					pm.get("scMaxLossRatio").alias("SC_MAX_LOSS_RATIO"),
					pm.get("scCombineLossRatio").alias("SC_COMBINE_LOSS_RATIO"),
					pm.get("scBandingStep").alias("SC_BANDING_STEP"),
					pm.get("scNoOfDigit").alias("SC_NO_OF_DIGIT"),
					pm.get("userId").alias("USERID"),
					pm.get("branch").alias("BRANCH"),
					pm.get("type").alias("TYPE"),
					pm.get("endorsementNo").alias("ENDORSEMENT_NO"),
					pm.get("subClass").alias("SUB_CLASS"),
					pm.get("sysDate").alias("SYS_DATE"),
					pm.get("layerNo").alias("LAYERNO"),
					pm.get("remarks").alias("REMARKS"),
					pm.get("referenceNo").alias("REFERENCE_NO"),
					pm.get("lcbFrom").alias("LCB_FROM"),
					pm.get("lcbTo").alias("LCB_TO"),
					pm.get("deltaLossRatio").alias("DELTA_LOSS_RATIO"),
					pm.get("lcbPercentage").alias("LCB_PERCENTAGE"));
				
			Subquery<Integer> end = cq.subquery(Integer.class);
			Root<TtrnBonus> tbRoot = end.from(TtrnBonus.class);
			
			end.select(cb.max(tbRoot.get("endorsementNo")))
			.where(cb.equal(pm.get("referenceNo"), tbRoot.get("referenceNo")),
					cb.equal(pm.get("branch"), tbRoot.get("branch")),
					cb.equal(pm.get("type"), tbRoot.get("type")),
					cb.equal(pm.get("lcbType"), tbRoot.get("lcbType")));
			
			cq.where(cb.equal(pm.get("referenceNo"), referenceNo),
					cb.equal(pm.get("branch"), branchCode),
					cb.equal(pm.get("type"), "SSC"),
					cb.equal(pm.get("lcbType"), "SSC1"),
					cb.equal(pm.get("endorsementNo"), end));
			
			cq.orderBy(cb.asc(pm.get("lcbId")));
			
			list = em.createQuery(cq).getResultList();

			}catch(Exception e) {
				e.printStackTrace();
				}
			return list;
	}

	@Override
	public void insertScMethodInfo(String[] args) {
		TtrnBonus ttrnBonus = new TtrnBonus() ;
		//INSERT_SECTION_DETAILS
		try {
		if(args != null) {
			ttrnBonus.setProposalNo(StringUtils.isBlank(args[0])? null : fm.formatBigDecimal(args[0]));	
			ttrnBonus.setContractNo(StringUtils.isBlank(args[1])? null : fm.formatBigDecimal(args[1]));
			ttrnBonus.setProductId(args[2]);
			ttrnBonus.setLcbType(args[3]);
			ttrnBonus.setProvisionalCommisiion(fm.formatBigDecimal(args[4]));
			ttrnBonus.setScMethodType(args[5]);
			ttrnBonus.setScMinLossRatio(fm.formatBigDecimal(args[6]));	
			ttrnBonus.setScMaxLossRatio(fm.formatBigDecimal(args[7]));
			ttrnBonus.setScCombineLossRatio(fm.formatBigDecimal(args[8]));
			ttrnBonus.setScBandingStep(fm.formatBigDecimal(args[9]));
			ttrnBonus.setScNoOfDigit(fm.formatBigDecimal(args[10]));
			ttrnBonus.setUserId(args[11]);
			ttrnBonus.setBranch(args[12]);
			ttrnBonus.setType(args[13]);	
			ttrnBonus.setEndorsementNo(fm.formatBigDecimal(args[14]));	
			ttrnBonus.setSubClass(args[15]);
			ttrnBonus.setSysDate(new Date());
			ttrnBonus.setLayerNo(args[16]);
			ttrnBonus.setRemarks(args[17]);
			ttrnBonus.setReferenceNo(fm.formatBigDecimal(args[18]));
			ttrnBonus.setLcbFrom(args[19]);
			ttrnBonus.setLcbTo(args[20]);
			ttrnBonus.setDeltaLossRatio(StringUtils.isBlank(args[21])? null : fm.formatBigDecimal(args[21]));
			ttrnBonus.setLcbPercentage(args[22]);
			ttrnBonus.setSno(fm.formatBigDecimal(args[23]));
			ttrnBonusRepository.saveAndFlush(ttrnBonus);
			}
	}catch(Exception e) {
		e.printStackTrace();
	}
		
	}

	@Override
	public List<Tuple> getRiskDetailsEditQuery(boolean contractMode, String proposalNo) {
		//risk.select.getEditModeData1,risk.select.getEditModeData2
		List<Tuple> list = new ArrayList<>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			query.multiselect(
					de.get("rskCedingid").alias("RSK_CEDINGID"),
					de.get("rskInceptionDate").alias("RSK_INCEPTION_DATE"),
					de.get("rskExpiryDate").alias("RSK_EXPIRY_DATE"),
					de.get("rskUwyear").alias("RSK_UWYEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),
					de.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),
					pm.get("baseLayer").alias("BASE_LAYER"),
					de.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					de.get("rskContractNo").alias("RSK_CONTRACT_NO"),
					de.get("rskLayerNo").alias("RSK_LAYER_NO"),
					de.get("rskProductid").alias("RSK_PRODUCTID"),
					de.get("rskDeptid").alias("RSK_DEPTID")
					);
				if(contractMode) {
					//endpr
					Subquery<Long> endde = query.subquery(Long.class);
					Root<TtrnRiskDetails> des = endde.from(TtrnRiskDetails.class);
					endde.select(cb.max(des.get("rskEndorsementNo")));
					Predicate a1 = cb.equal(des.get("rskContractNo"), proposalNo);
					endde.where(a1);
					
					Subquery<Long> endpr = query.subquery(Long.class);
					Root<TtrnRiskProposal> b = endpr.from(TtrnRiskProposal.class);
					Root<TtrnRiskDetails> a = endde.from(TtrnRiskDetails.class);
					endpr.select(cb.max(b.get("rskEndorsementNo")));
					Predicate b1 = cb.equal(a.get("rskContractNo"), proposalNo);
					Predicate b2 = cb.equal(a.get("rskProposalNumber"), b.get("rskProposalNumber"));
					endpr.where(b1,b2);
					
					Subquery<Long> amend = query.subquery(Long.class);
					Root<PositionMaster> pms = amend.from(PositionMaster.class);
					amend.select(cb.max(pms.get("amendId")));
					Predicate c1 = cb.equal(pms.get("proposalNo"), pm.get("proposalNo"));
					amend.where(c1);
					
					Predicate n1 = cb.equal(de.get("rskContractNo"), proposalNo);
					Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
					Predicate n3 = cb.equal(de.get("rskEndorsementNo"), endde);
					Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), endpr);
					Predicate n5 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
					Predicate n6 = cb.equal(pm.get("amendId"), amend);
					query.where(n1,n2,n3,n4,n5,n6);
				}else {
					//endpr
					Subquery<Long> endde = query.subquery(Long.class);
					Root<TtrnRiskDetails> des = endde.from(TtrnRiskDetails.class);
					endde.select(cb.max(des.get("rskEndorsementNo")));
					Predicate a1 = cb.equal(des.get("rskProposalNumber"), proposalNo);
					endde.where(a1);
					
					Subquery<Long> endpr = query.subquery(Long.class);
					Root<TtrnRiskProposal> b = endpr.from(TtrnRiskProposal.class);
					endpr.select(cb.max(b.get("rskEndorsementNo")));
					Predicate b1 = cb.equal(b.get("rskProposalNumber"), proposalNo);
					endpr.where(b1);
					
					Subquery<Long> amend = query.subquery(Long.class);
					Root<PositionMaster> pms = amend.from(PositionMaster.class);
					amend.select(cb.max(pms.get("amendId")));
					Predicate c1 = cb.equal(pms.get("proposalNo"), pm.get("proposalNo"));
					amend.where(c1);
					
					Predicate n1 = cb.equal(de.get("rskProposalNumber"), proposalNo);
					Predicate n2 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
					Predicate n3 = cb.equal(de.get("rskEndorsementNo"), endde);
					Predicate n4 = cb.equal(pr.get("rskEndorsementNo"), endpr);
					Predicate n5 = cb.equal(pm.get("proposalNo"), de.get("rskProposalNumber"));
					Predicate n6 = cb.equal(pm.get("amendId"), amend);
					query.where(n1,n2,n3,n4,n5,n6);
				}
			TypedQuery<Tuple> res1 = em.createQuery(query);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> riskSelectChechProposalStatus(String proposalNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//risk.select.chechProposalStatus
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskProposal> a = query1.from(TtrnRiskProposal.class);
			Root<TtrnRiskDetails> b = query1.from(TtrnRiskDetails.class);
			
			query1.multiselect(
					b.get("rskStatus").alias("RSK_STATUS"),
					a.get("rskShareSigned").alias("RSK_SHARE_SIGNED"),
					b.get("rskContractNo").alias("RSK_CONTRACT_NO")
					); 
			
			Subquery<Long> endA = query1.subquery(Long.class);
			Root<TtrnRiskProposal> des = endA.from(TtrnRiskProposal.class);
			endA.select(cb.max(des.get("rskEndorsementNo")));
			Predicate a1 = cb.equal(des.get("rskProposalNumber"), proposalNo);
			endA.where(a1);
			
			Subquery<Long> endB = query1.subquery(Long.class);
			Root<TtrnRiskDetails> rd = endB.from(TtrnRiskDetails.class);
			endB.select(cb.max(rd.get("rskEndorsementNo")));
			Predicate b1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
			endB.where(b1);
			
			Predicate n1 = cb.equal(a.get("rskProposalNumber"),b.get("rskProposalNumber"));
			Predicate n2 = cb.equal(a.get("rskProposalNumber"),proposalNo);
			Predicate n3 = cb.equal(a.get("rskEndorsementNo"), endA); 
			Predicate n4 = cb.equal(b.get("rskEndorsementNo"), endB); 
			query1.where(n1,n2,n3,n4);	
	
			TypedQuery<Tuple> res1 = em.createQuery(query1);
			list = res1.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void insertRiDetails(String[] args) {
		TtrnRi ttrnRi = new TtrnRi();
		//INSERT_SECTION_DETAILS
		try {
			
		if(args != null) {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<BigDecimal> query1 = cb.createQuery(BigDecimal.class); 
			Root<TtrnRi> rd = query1.from(TtrnRi.class);
			query1.multiselect(cb.max(rd.get("riNo"))); 
			//amendId
			
			TypedQuery<BigDecimal> result = em.createQuery(query1);
			BigDecimal riNo = result.getResultList().get(0);
			int a = riNo==null?1:(riNo.intValue()+1);
			
			ttrnRi.setRiNo(fm.formatBigDecimal(String.valueOf(a)));
			ttrnRi.setStatusNo(fm.formatBigDecimal(args[0]));
			ttrnRi.setSno(fm.formatBigDecimal(args[1]));
			ttrnRi.setBouquetNo(fm.formatBigDecimal(args[2]));
			ttrnRi.setBaseProposalNo(fm.formatBigDecimal(args[3]));
			ttrnRi.setProposalNo(fm.formatBigDecimal(args[4]));
			ttrnRi.setContractNo(fm.formatBigDecimal(args[5]));
			ttrnRi.setSubContractNo(fm.formatBigDecimal(args[6]));
			ttrnRi.setLayerNo(fm.formatBigDecimal(args[7]));
			ttrnRi.setSectionNo(fm.formatBigDecimal(args[8]));
			ttrnRi.setAmendId(fm.formatBigDecimal(args[9]));
			ttrnRi.setReinsurerId(args[10]);
			ttrnRi.setBrokerId(args[11]);	
			ttrnRi.setShareOffered(fm.formatBigDecimal(args[12]));
			ttrnRi.setShareWritten(fm.formatBigDecimal(args[13]));
			ttrnRi.setShareProposalWritten(fm.formatBigDecimal(args[14]));
			ttrnRi.setShareSigned(fm.formatBigDecimal(args[15]));
			ttrnRi.setShareProposedSigned(fm.formatBigDecimal(args[16]));
			ttrnRi.setBrokerage(fm.formatBigDecimal(args[17]));
			ttrnRi.setCurrentStatus(args[18]);
			ttrnRi.setNewStatus(args[19]);
			ttrnRi.setApproveStatus(args[20]);
			ttrnRi.setUserId(args[21]);
			ttrnRi.setBranchCode(args[22]);
			ttrnRi.setSysDate(new Date());	
			
			ttrnRiRepository.saveAndFlush(ttrnRi);
			}
	}catch(Exception e) {
		e.printStackTrace();
	}
	}


	@Override
	public int updateBonus(String requestNumber, String proposalNo) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<TtrnBonus> update = cb.createCriteriaUpdate(TtrnBonus.class);
		Root<TtrnBonus> m = update.from(TtrnBonus.class);
		update.set("proposalNo", fm.formatBigDecimal(proposalNo));
		
		Predicate n1 = cb.equal(m.get("referenceNo"),fm.formatBigDecimal(requestNumber));
	
		update.where(n1 );
		return em.createQuery(update).executeUpdate();
	}

	@Override
	public int updateRip(String requestNumber, String proposalNo) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRip> update = cb.createCriteriaUpdate(TtrnRip.class);
		Root<TtrnRip> m = update.from(TtrnRip.class);
		update.set("proposalNo", fm.formatBigDecimal(proposalNo));
		
		Predicate n1 = cb.equal(m.get("referenceNo"),fm.formatBigDecimal(requestNumber));
	
		update.where(n1 );
		return em.createQuery(update).executeUpdate();
	}
	@Override
	public void riskUpdateHomeContNo(String[] args) {
		try {
			//GET_RETRO_CON_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
		
			update.set("contractNo", fm.formatBigDecimal(args[0]));
			update.set("proposalStatus", args[1]);
			update.set("contractStatus", args[2]);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), args[3]);
			update.where(n1);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Tuple> commissionTypeList(String proposalno, String branchCode, String commissionType,
			String contractNo) {
		List<Tuple> list = new ArrayList<>();
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

			Predicate n1 = cb.equal(c.get("proposalNo"), proposalno);
			Predicate n2 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(c.get("commissionType"), commissionType);
			Predicate n4 = cb.equal(c.get("endorsementNo"), end);
			
			
		 if(StringUtils.isNotBlank(contractNo)){
			//COMMISSION_TYPE_LIST1
				Predicate n5 = cb.equal(c.get("contractNo"), contractNo);
				query.where(n1,n2,n3,n4,n5).orderBy(orderList);
				}
		 else{
			 query.where(n1,n2,n3,n4).orderBy(orderList);
		 }
		 TypedQuery<Tuple> res1 = em.createQuery(query);
		 list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> commissionTypeListReference(String referenceNo, String branchCode, String commissionType,
			String contractNo) {
		List<Tuple> list = new ArrayList<>();
		try {
			//COMMISSION_TYPE_LIST_Ref
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnCommissionDetails> c = query.from(TtrnCommissionDetails.class);
			query.multiselect(c.get("sNo").alias("SNO"),c.get("commFrom").alias("COMM_FROM"),
					c.get("commTo").alias("COMM_TO"),
					c.get("profitComm").alias("PROFIT_COMM"));
					
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnCommissionDetails> pms = end.from(TtrnCommissionDetails.class);
			end.select(cb.max(pms.get("endorsementNo")));
			Predicate a1 = cb.equal(c.get("referenceNo"), pms.get("referenceNo"));
			Predicate a2 = cb.equal(c.get("branchCode"), pms.get("branchCode"));
			end.where(a1,a2);
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("sNo")));

			Predicate n1 = cb.equal(c.get("referenceNo"), referenceNo);
			Predicate n2 = cb.equal(c.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(c.get("commissionType"), commissionType);
			Predicate n4 = cb.equal(c.get("endorsementNo"), end);
			
			
		 if(StringUtils.isNotBlank(contractNo)){
			//COMMISSION_TYPE_LIST1
				Predicate n5 = cb.equal(c.get("contractNo"), contractNo);
				query.where(n1,n2,n3,n4,n5).orderBy(orderList);
				}
		 else{
			 query.where(n1,n2,n3,n4).orderBy(orderList);
		 }
		 TypedQuery<Tuple> res1 = em.createQuery(query);
		 list = res1.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	@Transactional
	public void UpdateInstallmentContNo(String proposalNo, String maxContarctNo) {
		try {
			//GET_RETRO_CON_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnMndInstallments> update = cb.createCriteriaUpdate(TtrnMndInstallments.class);
			Root<TtrnMndInstallments> m = update.from(TtrnMndInstallments.class);
		
			update.set("contractNo", maxContarctNo);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			update.where(n1);
			em.createQuery(update).executeUpdate();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	}
