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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasDocTypeMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCedentRet;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;

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
	private TtrnInsurerDetailsRepository ttrnInsurerDetailsRepository;

	@Override
	public TtrnRiskDetails ttrnRiskDetailsUpdate(String[] args) throws ParseException {
		TtrnRiskDetails ttrnRiskDetails = null;
		//risk.update.rskDtls
		//args--getFirstPageEditSaveModeAruguments
		try {
		if(args != null) {
			ttrnRiskDetails = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNo(
					args[52],new BigDecimal(args[53]));
			if(ttrnRiskDetails!=null) {
			ttrnRiskDetails.setSysDate(new Date());
			ttrnRiskDetails.setRskDeptid(new BigDecimal(args[0]));
			ttrnRiskDetails.setRskPfcid(args[1]);
			ttrnRiskDetails.setRskSpfcid(args[2]);
			ttrnRiskDetails.setRskPolbranch(new BigDecimal(args[3]));
			ttrnRiskDetails.setRskCedingid(new BigDecimal(args[4]));
			ttrnRiskDetails.setRskBrokerid(new BigDecimal(args[5]));
			ttrnRiskDetails.setRskTreatyid(args[6]);
			ttrnRiskDetails.setRskMonth(sdf.parse(args[7]));
			ttrnRiskDetails.setRskUwyear(new BigDecimal(args[8]));
			ttrnRiskDetails.setRskUnderwritter(args[9]);
			ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[10]));
			ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[11]));
			ttrnRiskDetails.setRskAccountDate(sdf.parse(args[12]));
			ttrnRiskDetails.setRskOriginalCurr(args[13]);
			ttrnRiskDetails.setRskExchangeRate(new BigDecimal(args[14]));
			ttrnRiskDetails.setRskBasis(args[15]);
			ttrnRiskDetails.setRskPeriodOfNotice(args[16]);
			ttrnRiskDetails.setRskRiskCovered(args[17]);
			ttrnRiskDetails.setRskTerritoryScope(args[18]);
			ttrnRiskDetails.setRskTerritory(args[19]);
			ttrnRiskDetails.setRskStatus(args[20]);
			ttrnRiskDetails.setRskProposalType(args[21]);
			ttrnRiskDetails.setRskAccountingPeriod(new BigDecimal(args[22]));
			ttrnRiskDetails.setRskReceiptStatement(new BigDecimal(args[23]));
			ttrnRiskDetails.setRskReceiptPayement(new BigDecimal(args[24]));
			ttrnRiskDetails.setMndInstallments(new BigDecimal(args[25]));
			ttrnRiskDetails.setRetroCessionaries(new BigDecimal(args[26]));
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
			ttrnRiskDetails.setRskRunOffYear(new BigDecimal(args[42]));
			ttrnRiskDetails.setRskLocBnkName(args[43]);
			ttrnRiskDetails.setRskLocCrdtPrd(new BigDecimal(args[44]));
			ttrnRiskDetails.setRskLocCrdtAmt(new BigDecimal(args[45]));
			ttrnRiskDetails.setRskLocBenfcreName(args[46]);
			ttrnRiskDetails.setRskCessionExgRate(args[47]);
			if(StringUtils.isNotEmpty(args[48]))
			ttrnRiskDetails.setRskFixedRate(new BigDecimal(args[48]));
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
				ttrnRiskDetails.setRskEndorsementNo(new BigDecimal(args[1]));
				ttrnRiskDetails.setRskLayerNo(new BigDecimal(args[2]));
				ttrnRiskDetails.setRskProductid(new BigDecimal(args[3]));
				ttrnRiskDetails.setRskDeptid(new BigDecimal(args[4]));
				ttrnRiskDetails.setRskPfcid(args[5]);
				ttrnRiskDetails.setRskSpfcid(args[6]);
				ttrnRiskDetails.setRskPolbranch(new BigDecimal(args[7]));
				ttrnRiskDetails.setRskCedingid(new BigDecimal(args[8]));
				ttrnRiskDetails.setRskBrokerid(new BigDecimal(args[9]));
				ttrnRiskDetails.setRskTreatyid(args[10]);
				ttrnRiskDetails.setRskMonth(sdf.parse(args[11]));
				ttrnRiskDetails.setRskUwyear(new BigDecimal(args[12]));
				ttrnRiskDetails.setRskUnderwritter(args[13]);
				ttrnRiskDetails.setRskInceptionDate(sdf.parse(args[14]));
				ttrnRiskDetails.setRskExpiryDate(sdf.parse(args[15]));
				ttrnRiskDetails.setRskAccountDate(sdf.parse(args[16]));
				ttrnRiskDetails.setRskOriginalCurr(args[17]);
				ttrnRiskDetails.setRskExchangeRate(new BigDecimal(args[18]));
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
				ttrnRiskDetails.setRskAccountingPeriod(new BigDecimal(args[28]));
				ttrnRiskDetails.setRskReceiptStatement(new BigDecimal(args[29]));
				ttrnRiskDetails.setRskReceiptPayement(new BigDecimal(args[30]));
				ttrnRiskDetails.setMndInstallments(new BigDecimal(args[31]));
				ttrnRiskDetails.setRetroCessionaries(new BigDecimal(args[32]));
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
				ttrnRiskDetails.setRskRunOffYear(new BigDecimal(args[49]));
				ttrnRiskDetails.setRskLocBnkName(args[50]);
				ttrnRiskDetails.setRskLocCrdtPrd(new BigDecimal(args[51]));
				ttrnRiskDetails.setRskLocCrdtAmt(new BigDecimal(args[52]));
				ttrnRiskDetails.setRskLocBenfcreName(args[53]);
				ttrnRiskDetails.setRskCessionExgRate(args[54]);
				if(StringUtils.isNotEmpty(args[55]))
				ttrnRiskDetails.setRskFixedRate(new BigDecimal(args[55]));
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
					args[49],new BigDecimal(args[50]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setSysDate(new Date());
				ttrnRiskProposal.setRskLimitOc(new BigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitDc(new BigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOfferOc(new BigDecimal(args[2]));
				ttrnRiskProposal.setRskEpiOfferDc(new BigDecimal(args[3]));
				ttrnRiskProposal.setRskEpiEstimate(new BigDecimal(args[4]));
				ttrnRiskProposal.setRskEpiEstOc(new BigDecimal(args[5]));
				ttrnRiskProposal.setRskEpiEstDc(new BigDecimal(args[6]));
				ttrnRiskProposal.setRskXlcostOc(new BigDecimal(args[7]));
				ttrnRiskProposal.setRskXlcostDc(new BigDecimal(args[8]));
				ttrnRiskProposal.setRskCedantRetention(new BigDecimal(args[9]));
				ttrnRiskProposal.setRskShareWritten(new BigDecimal(args[10]));
				ttrnRiskProposal.setRskShareSigned(new BigDecimal(args[11]));
				ttrnRiskProposal.setRskCedretType(args[12]);
				ttrnRiskProposal.setRskSpRetro(args[13]);
				ttrnRiskProposal.setRskNoOfInsurers(new BigDecimal(args[14]));	
				ttrnRiskProposal.setRskMaxLmtCover(new BigDecimal(args[15]));
				ttrnRiskProposal.setRskLimitOsOc(new BigDecimal(args[16]));
				ttrnRiskProposal.setRskLimitOsDc(new BigDecimal(args[17]));
				ttrnRiskProposal.setRskEpiOsofOc(new BigDecimal(args[18]));
				ttrnRiskProposal.setRskEpiOsofDc(new BigDecimal(args[19]));
				ttrnRiskProposal.setRskEpiOsoeOc(new BigDecimal(args[20]));
				ttrnRiskProposal.setRskEpiOsoeDc(new BigDecimal(args[21]));
				ttrnRiskProposal.setRskXlcostOsOc(new BigDecimal(args[22]));
				ttrnRiskProposal.setRskXlcostOsDc(new BigDecimal(args[23]));
				ttrnRiskProposal.setLimitPerVesselOc(new BigDecimal(args[24]));
				ttrnRiskProposal.setLimitPerVesselDc(new BigDecimal(args[25]));
				ttrnRiskProposal.setLimitPerLocationOc(new BigDecimal(args[26]));
				ttrnRiskProposal.setLimitPerLocationDc(new BigDecimal(args[27]));
				ttrnRiskProposal.setRskTreatySurpLimitOc(new BigDecimal(args[28]));
				ttrnRiskProposal.setRskTreatySurpLimitDc(new BigDecimal(args[29]));
				ttrnRiskProposal.setRskTreatySurpLimitOsOc(new BigDecimal(args[30]));
				ttrnRiskProposal.setRskTreatySurpLimitOsDc(new BigDecimal(args[31]));
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
				ttrnRiskProposal.setSubClass(new BigDecimal(args[44]));
				ttrnRiskProposal.setLoginId(args[45]);
				ttrnRiskProposal.setBranchCode(args[46]);
				ttrnRiskProposal.setRskPml(args[47]);
				ttrnRiskProposal.setRskPmlPercent(new BigDecimal(args[48]));
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
			positionMaster = positionMasterRepository.findByProposalNoAndAmendId(new BigDecimal(input[21]),new BigDecimal(input[22]));
			if(positionMaster!=null) {
				positionMaster.setLayerNo(new BigDecimal(input[0]));
				positionMaster.setReinsuranceId(input[1]);
				positionMaster.setProductId(new BigDecimal(input[2]));			
				positionMaster.setDeptId(input[3]);
				positionMaster.setCedingCompanyId(input[4]);
				positionMaster.setUwYear(input[5]);			
				positionMaster.setUwMonth(sdf.parse(input[6]));				
				positionMaster.setAccountDate(sdf.parse(input[7]));
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
				positionMaster.setBouquetNo(new BigDecimal(input[18]));
				positionMaster.setUwYearTo(new BigDecimal(input[19]));
				positionMaster.setSectionNo(new BigDecimal(input[20]));				
				
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
				ttrnRiskProposal.setRskEndorsementNo(new BigDecimal(input[1]));
				ttrnRiskProposal.setRskLayerNo(new BigDecimal(input[2]));
				ttrnRiskProposal.setRskLimitOc(new BigDecimal(input[3]));
				ttrnRiskProposal.setRskLimitDc(new BigDecimal(input[4]));
				ttrnRiskProposal.setRskEpiOfferOc(new BigDecimal(input[5]));
				ttrnRiskProposal.setRskEpiOfferDc(new BigDecimal(input[6]));
				ttrnRiskProposal.setRskEpiEstimate(new BigDecimal(input[7]));
				ttrnRiskProposal.setRskEpiEstOc(new BigDecimal(input[8]));
				ttrnRiskProposal.setRskEpiEstDc(new BigDecimal(input[9]));
				ttrnRiskProposal.setRskXlcostOc(new BigDecimal(input[10]));
				ttrnRiskProposal.setRskXlcostDc(new BigDecimal(input[11]));
				ttrnRiskProposal.setRskCedantRetention(new BigDecimal(input[12]));
				ttrnRiskProposal.setRskShareWritten(new BigDecimal(input[13]));
				ttrnRiskProposal.setRskShareSigned(new BigDecimal(input[14]));
				ttrnRiskProposal.setRskCedretType(input[15]);
				ttrnRiskProposal.setRskSpRetro(input[16]);
				ttrnRiskProposal.setRskNoOfInsurers(new BigDecimal(input[17]));
				ttrnRiskProposal.setRskMaxLmtCover(new BigDecimal(input[18]));
				ttrnRiskProposal.setRskLimitOsOc(new BigDecimal(input[19]));
				ttrnRiskProposal.setRskLimitOsDc(new BigDecimal(input[20]));
				ttrnRiskProposal.setRskEpiOsofOc(new BigDecimal(input[21]));		
				ttrnRiskProposal.setRskEpiOsofDc(new BigDecimal(input[22]));	
				ttrnRiskProposal.setRskEpiOsoeOc(new BigDecimal(input[23]));		
				ttrnRiskProposal.setRskEpiOsoeDc(new BigDecimal(input[24]));		
				ttrnRiskProposal.setRskXlcostOsOc(new BigDecimal(input[25]));
				ttrnRiskProposal.setRskXlcostOsDc(new BigDecimal(input[26]));	
				ttrnRiskProposal.setLimitPerVesselOc(new BigDecimal(input[27]));
				ttrnRiskProposal.setLimitPerVesselDc(new BigDecimal(input[28]));
				ttrnRiskProposal.setLimitPerLocationOc(new BigDecimal(input[29]));
				ttrnRiskProposal.setLimitPerLocationDc(new BigDecimal(input[30]));
				ttrnRiskProposal.setRskTreatySurpLimitOc(new BigDecimal(input[31]));
				ttrnRiskProposal.setRskTreatySurpLimitDc(new BigDecimal(input[32]));
				ttrnRiskProposal.setRskTreatySurpLimitOsOc(new BigDecimal(input[33]));
				ttrnRiskProposal.setRskTreatySurpLimitOsDc(new BigDecimal(input[34]));
				ttrnRiskProposal.setSubClass(new BigDecimal(input[35]));;
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
				positionMaster.setProposalNo(new BigDecimal(input[0]));
				positionMaster.setContractNo(new BigDecimal(input[1]));
				positionMaster.setAmendId(new BigDecimal(input[2]));
				positionMaster.setLayerNo(new BigDecimal(input[3]));
				positionMaster.setReinsuranceId(input[4]);
				positionMaster.setProductId(new BigDecimal(input[5]));				
				positionMaster.setDeptId(input[6]);
				positionMaster.setCedingCompanyId(input[7]);
				positionMaster.setUwYear(input[8]);
				positionMaster.setUwMonth(sdf.parse(input[9]));
				positionMaster.setAccountDate(sdf.parse(input[10]));
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
				positionMaster.setBouquetNo(new BigDecimal(input[27]));
				positionMaster.setUwYearTo(new BigDecimal(input[28]));
				positionMaster.setSectionNo(new BigDecimal(input[29]));
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
					args[53],new BigDecimal(args[54]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskEventLimitOc(new BigDecimal(args[0]));
				ttrnRiskProposal.setRskEventLimitDc(new BigDecimal(args[1]));
				ttrnRiskProposal.setRskEventLimitOsOc(new BigDecimal(args[2]));
				ttrnRiskProposal.setRskEventLimitOsDc(new BigDecimal(args[3]));
				ttrnRiskProposal.setRskCoverLimitUxlOc(new BigDecimal(args[4]));
				ttrnRiskProposal.setRskCoverLimitUxlDc(new BigDecimal(args[5]));
				ttrnRiskProposal.setRskCoverLimitUxlOsOc(new BigDecimal(args[6]));
				ttrnRiskProposal.setRskCoverLimitUxlOsDc(new BigDecimal(args[7]));
				ttrnRiskProposal.setRskDeductableUxlOc(new BigDecimal(args[8]));
				ttrnRiskProposal.setRskDeductableUxlDc(new BigDecimal(args[9]));
				ttrnRiskProposal.setRskDeductableUxlOsOc(new BigDecimal(args[10]));
				ttrnRiskProposal.setRskDeductableUxlOsDc(new BigDecimal(args[11]));
				ttrnRiskProposal.setRskPml(args[12]);
				if(args[13]!="")
				ttrnRiskProposal.setRskPmlPercent(new BigDecimal(args[13]));
				if(args[14]!="")
				ttrnRiskProposal.setRskEgnpiPmlOc(new BigDecimal(args[14]));
				if(args[15]!="")
				ttrnRiskProposal.setRskEgnpiPmlDc(new BigDecimal(args[15]));
				if(args[16]!="")
				ttrnRiskProposal.setRskEgnpiPmlOsOc(new BigDecimal(args[16]));
				ttrnRiskProposal.setRskEgnpiPmlOsDc(new BigDecimal(args[17]));
				ttrnRiskProposal.setRskPremiumBasis(args[18]);
				ttrnRiskProposal.setRskMinimumRate(new BigDecimal(args[19]));
				ttrnRiskProposal.setRskMaxiimumRate(new BigDecimal(args[20]));	
				ttrnRiskProposal.setRskBurningCostLf(args[21]);
				ttrnRiskProposal.setRskMinimumPremiumOc(new BigDecimal(args[22]));
				ttrnRiskProposal.setRskMinimumPremiumDc(new BigDecimal(args[23]));
				ttrnRiskProposal.setRskMinimumPremiumOsOc(new BigDecimal(args[24]));
				ttrnRiskProposal.setRskMinimumPremiumOsDc(new BigDecimal(args[25]));
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
				ttrnRiskProposal.setRateOnLine(new BigDecimal(args[51]));
				ttrnRiskProposal.setQuotesharePercent(new BigDecimal(args[52]));;
				
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
				ttrnRiskRemarks.setProposalNo(new BigDecimal(input[0]));
				ttrnRiskRemarks.setContractNo(new BigDecimal(input[1]));
				ttrnRiskRemarks.setLayerNo(new BigDecimal(input[2]));
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
				ttrnCedentRet.setLayerNo(new BigDecimal(input[2]));
				ttrnCedentRet.setDeptId(input[3]);
				ttrnCedentRet.setProductId(input[4]);
				ttrnCedentRet.setAmendId(input[5]);
				ttrnCedentRet.setRskSno(input[6]);
				ttrnCedentRet.setRskClass(input[7]);
				ttrnCedentRet.setRskSubclass(input[8]);
				ttrnCedentRet.setRskType(input[9]);
				ttrnCedentRet.setRskRettype(input[10]);
				ttrnCedentRet.setRskBasistype(input[11]);
				ttrnCedentRet.setRskFirstRetOc(new BigDecimal(input[12]));
				ttrnCedentRet.setRskSecondRetOc(new BigDecimal(input[13]));
				ttrnCedentRet.setRskRetTlFstOc(new BigDecimal(input[14]));
				ttrnCedentRet.setRskRetTlSstOc(new BigDecimal(input[15]));
				ttrnCedentRet.setRskRetElFstOc(new BigDecimal(input[16]));
				ttrnCedentRet.setRskRetElSstOc(new BigDecimal(input[17]));
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
					args[16],new BigDecimal(args[17]));
			if(ttrnRiskProposal!=null) {
				ttrnRiskProposal.setRskLimitOsOc(new BigDecimal(args[0]));
				ttrnRiskProposal.setRskLimitOsDc(new BigDecimal(args[1]));
				ttrnRiskProposal.setRskEpiOsofOc(new BigDecimal(args[2]));		
				ttrnRiskProposal.setRskEpiOsofDc(new BigDecimal(args[3]));		
				ttrnRiskProposal.setRskEpiOsoeOc(new BigDecimal(args[4]));		
				ttrnRiskProposal.setRskEpiOsoeDc(new BigDecimal(args[5]));	
				if(StringUtils.isNotEmpty(args[6]))
				ttrnRiskProposal.setRskXlcostOsOc(new BigDecimal(args[6]));	
				if(StringUtils.isNotEmpty(args[7]))
				ttrnRiskProposal.setRskXlcostOsDc(new BigDecimal(args[7]));	
				ttrnRiskProposal.setRskPremiumQuotaShare(new BigDecimal(args[8]));
				ttrnRiskProposal.setRskPremiumSurpuls(new BigDecimal(args[9]));
				ttrnRiskProposal.setRskPremiumQuotaShareDc(new BigDecimal(args[10]));
				ttrnRiskProposal.setRskPremiumSurplusDc(new BigDecimal(args[11]));
				ttrnRiskProposal.setCommQsAmt(new BigDecimal(args[12]));
				ttrnRiskProposal.setCommSurplusAmt(new BigDecimal(args[13]));
				ttrnRiskProposal.setCommQsAmtDc(new BigDecimal(args[14]));
				ttrnRiskProposal.setCommSurplusAmtDc(new BigDecimal(args[15]));
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
					args[65],new BigDecimal(args[66]));
			if(ttrnRiskCommission!=null) {
				ttrnRiskCommission.setRskBrokerage(new BigDecimal(args[0]));
				ttrnRiskCommission.setRskTax(new BigDecimal(args[1]));
				ttrnRiskCommission.setRskProfitComm(new BigDecimal(args[2]));
				ttrnRiskCommission.setRskAcquistionCostOc(new BigDecimal(args[3]));	
				ttrnRiskCommission.setRskAcquistionCostDc(new BigDecimal(args[4]));	
				ttrnRiskCommission.setRskCommQuotashare(new BigDecimal(args[5]));
				ttrnRiskCommission.setRskCommSurplus(new BigDecimal(args[6]));
				ttrnRiskCommission.setRskOverriderPerc(new BigDecimal(args[7]));
				ttrnRiskCommission.setRskPremiumReserve(new BigDecimal(args[8]));
				ttrnRiskCommission.setRskLossReserve(new BigDecimal(args[9]));
				ttrnRiskCommission.setRskInterest(new BigDecimal(args[10]));
				ttrnRiskCommission.setRskPfInoutPrem(new BigDecimal(args[11]));
				ttrnRiskCommission.setRskPfInoutLoss(new BigDecimal(args[12]));
				ttrnRiskCommission.setRskLossadvice(args[13]);
				ttrnRiskCommission.setRskCashlossLmtOc(new BigDecimal(args[14]));
				ttrnRiskCommission.setRskCashlossLmtDc(new BigDecimal(args[15]));
				ttrnRiskCommission.setRskLeadUw(args[16]);
				ttrnRiskCommission.setRskLeadUwShare(new BigDecimal(args[17]));
				ttrnRiskCommission.setRskAccounts(args[18]);
				ttrnRiskCommission.setRskExclusion(args[19]);
				ttrnRiskCommission.setRskRemarks(args[20]);
				ttrnRiskCommission.setRskUwRecomm(args[21]);
				ttrnRiskCommission.setRskGmApproval(args[22]);
				ttrnRiskCommission.setRskOtherCost(new BigDecimal(args[23]));
				ttrnRiskCommission.setRskCreastaStatus(args[24]);
				ttrnRiskCommission.setRskEventLimitOc(new BigDecimal(args[25]));	
				ttrnRiskCommission.setRskEventLimitDc(new BigDecimal(args[26]));	
				ttrnRiskCommission.setRskAggregateLimitOc(new BigDecimal(args[27]));
				ttrnRiskCommission.setRskAggregateLimitDc(new BigDecimal(args[28]));
				ttrnRiskCommission.setRskOccurrentLimitOc(new BigDecimal(args[29]));
				ttrnRiskCommission.setRskOccurrentLimitDc(new BigDecimal(args[30]));
				ttrnRiskCommission.setRskSladscaleComm(args[31]);
				ttrnRiskCommission.setRskLossPartCarridor(args[32]);
				ttrnRiskCommission.setRskCombinSubClass(args[33]);
				ttrnRiskCommission.setSubClass(new BigDecimal(args[34]));
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
				ttrnRiskCommission.setRskProManagementExp(new BigDecimal(args[44]));
				ttrnRiskCommission.setRskProCommType(args[45]);
				ttrnRiskCommission.setRskProCommPer(new BigDecimal(args[46]));
				ttrnRiskCommission.setRskProSetUp(args[47]);
				ttrnRiskCommission.setRskProSubPfoCom(new BigDecimal(args[48]));
				ttrnRiskCommission.setRskProLossCaryType(args[49]);
				ttrnRiskCommission.setRskProLossCaryYear(args[50]);
				ttrnRiskCommission.setRskProProfitCaryType(args[51]);
				ttrnRiskCommission.setRskProProfitCaryYear(args[52]);
				ttrnRiskCommission.setRskProFirstPfoCom(new BigDecimal(args[53]));
				ttrnRiskCommission.setRskProFirstPfoComPrd(args[54]);
				ttrnRiskCommission.setRskProSubPfoComPrd(args[55]);
				ttrnRiskCommission.setRskProSubPfoCom(new BigDecimal(args[56]));
				ttrnRiskCommission.setRskProSubSeqCal(args[57]);
				ttrnRiskCommission.setRskProNotes(args[58]);
				ttrnRiskCommission.setRskDocStatus(args[59]);
				ttrnRiskCommission.setRskRate(args[60]);
				
				ttrnRiskCommission.setRskPremiumResType(args[61]); //Ri
				ttrnRiskCommission.setFpcType(args[62]);
				ttrnRiskCommission.setFpcFixedDate(sdf.parse(args[63]));
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
				ttrnRiskCommission.setRskEndorsementNo(new BigDecimal(args[1]));
				ttrnRiskCommission.setRskLayerNo(new BigDecimal(args[2]));
				ttrnRiskCommission.setRskBrokerage(new BigDecimal(args[3]));	
				ttrnRiskCommission.setRskTax(new BigDecimal(args[4]));
				ttrnRiskCommission.setRskProfitComm(new BigDecimal(args[5]));
				ttrnRiskCommission.setRskReserveOnLoss(new BigDecimal(args[6]));
				ttrnRiskCommission.setRskAcquistionCostOc(new BigDecimal(args[7]));				
				ttrnRiskCommission.setRskAcquistionCostDc(new BigDecimal(args[8]));	
				ttrnRiskCommission.setRskCommQuotashare(new BigDecimal(args[9]));
				ttrnRiskCommission.setRskCommSurplus(new BigDecimal(args[10]));
				ttrnRiskCommission.setRskOverriderPerc(new BigDecimal(args[11]));
				ttrnRiskCommission.setRskPremiumReserve(new BigDecimal(args[12]));
				ttrnRiskCommission.setRskLossReserve(new BigDecimal(args[13]));
				ttrnRiskCommission.setRskInterest(new BigDecimal(args[14]));
				ttrnRiskCommission.setRskPfInoutPrem(new BigDecimal(args[15]));
				ttrnRiskCommission.setRskPfInoutLoss(new BigDecimal(args[16]));
				ttrnRiskCommission.setRskLossadvice(args[17]);
				ttrnRiskCommission.setRskCashlossLmtOc(new BigDecimal(args[18]));
				ttrnRiskCommission.setRskCashlossLmtDc(new BigDecimal(args[19]));
				ttrnRiskCommission.setRskLeadUw(args[20]);
				ttrnRiskCommission.setRskLeadUwShare(new BigDecimal(args[21]));
				ttrnRiskCommission.setRskAccounts(args[22]);
				ttrnRiskCommission.setRskExclusion(args[23]);
				ttrnRiskCommission.setRskRemarks(args[24]);
				ttrnRiskCommission.setRskUwRecomm(args[25]);
				ttrnRiskCommission.setRskGmApproval(args[26]);
				ttrnRiskCommission.setRskDecision(args[27]);
				ttrnRiskCommission.setRskEntryDate(new Date());
				ttrnRiskCommission.setRskEndDate(new Date());
				ttrnRiskCommission.setRskStatus(args[28]);		
				ttrnRiskCommission.setRskOtherCost(new BigDecimal(args[29]));
				ttrnRiskCommission.setRskCreastaStatus(args[30]);
				ttrnRiskCommission.setRskEventLimitOc(new BigDecimal(args[31]));
				ttrnRiskCommission.setRskEventLimitDc(new BigDecimal(args[32]));
				ttrnRiskCommission.setRskAggregateLimitOc(new BigDecimal(args[33]));
				ttrnRiskCommission.setRskAggregateLimitDc(new BigDecimal(args[34]));
				ttrnRiskCommission.setRskOccurrentLimitOc(new BigDecimal(args[35]));
				ttrnRiskCommission.setRskOccurrentLimitDc(new BigDecimal(args[36]));			
				ttrnRiskCommission.setRskSladscaleComm(args[37]);
				ttrnRiskCommission.setRskLossPartCarridor(args[38]);
				ttrnRiskCommission.setRskCombinSubClass(args[39]);
				ttrnRiskCommission.setSubClass(new BigDecimal(args[40]));
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
				ttrnRiskCommission.setRskProManagementExp(new BigDecimal(args[50]));
				ttrnRiskCommission.setRskProCommType(args[51]);	
				ttrnRiskCommission.setRskProCommPer(new BigDecimal(args[52]));
				ttrnRiskCommission.setRskProSetUp(args[53]);
				ttrnRiskCommission.setRskProSupProCom(args[54]);
				ttrnRiskCommission.setRskProLossCaryType(args[55]);
				ttrnRiskCommission.setRskProLossCaryYear(args[56]);
				ttrnRiskCommission.setRskProProfitCaryType(args[57]);
				ttrnRiskCommission.setRskProProfitCaryYear(args[58]);
				ttrnRiskCommission.setRskProFirstPfoCom(new BigDecimal(args[59]));
				ttrnRiskCommission.setRskProFirstPfoComPrd(args[60]);	
				ttrnRiskCommission.setRskProSubPfoComPrd(args[61]);	
				ttrnRiskCommission.setRskProSubPfoCom(new BigDecimal(args[62]));		
				ttrnRiskCommission.setRskProSubSeqCal(args[63]);
				ttrnRiskCommission.setRskProNotes(args[64]);
				ttrnRiskCommission.setRskDocStatus(args[65]);
				ttrnRiskCommission.setRskRate(args[66]);
				ttrnRiskCommission.setRskPremiumResType(args[67]);
				ttrnRiskCommission.setFpcType(args[68]);
				ttrnRiskCommission.setFpcFixedDate(sdf.parse(args[69]));
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
			ttrnInsurerDetails.setInsurerNo(new BigDecimal(obj[0]));
			ttrnInsurerDetails.setProposalNo(obj[1]);
			ttrnInsurerDetails.setContractNo(obj[2]);
			ttrnInsurerDetails.setEndorsementNo(new BigDecimal(obj[3]));
			ttrnInsurerDetails.setType(obj[4]);
			ttrnInsurerDetails.setRetroPercentage(new BigDecimal(obj[5]));
			ttrnInsurerDetails.setStatus(obj[6]);
			ttrnInsurerDetails.setUwYear(obj[7]);
			ttrnInsurerDetails.setRetroType(obj[8]);
			ttrnInsurerDetails.setEntryDate(new Date());
			ttrnInsurerDetails.setSubClass(new BigDecimal(obj[9]));
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
			ttrnCrestazoneDetails.setContractNo(new BigDecimal(input[0]));
			ttrnCrestazoneDetails.setProposalNo(new BigDecimal(input[1]));
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
		TtrnBonus ttrnBonus = null;
		//MOVE_TO_CRESTA_MAIN_TABLE
		try {
		if(input != null) {
			ttrnBonus = new TtrnBonus();
			ttrnBonus.setProposalNo(new BigDecimal(input[0]));
			ttrnBonus.setContractNo(new BigDecimal(input[1]));
			ttrnBonus.setProductId(input[2]);
			ttrnBonus.setLcbType(input[3]);
			ttrnBonus.setLcbFrom(input[4]);
			ttrnBonus.setLcbTo(input[5]);
			ttrnBonus.setLcbPercentage(input[6]);
			ttrnBonus.setUserId(input[7]);
			ttrnBonus.setBranch(input[8]);
			ttrnBonus.setLcbId(input[9]);
			ttrnBonus.setType(input[10]);
			ttrnBonus.setEndorsementNo(new BigDecimal(input[11]));
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
			ttrnBonus.setReferenceNo(new BigDecimal(input[21]));
			ttrnBonus.setScaleMaxPartPercent(new BigDecimal(input[22]));
			ttrnBonus.setFpcType(input[23]);
			ttrnBonus.setFpcFixedDate(sdf.parse(input[24]));
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
			ttrnCommissionDetails.setProposalNo(new BigDecimal(input[4]));
			ttrnCommissionDetails.setContractNo(new BigDecimal(input[5]));
			ttrnCommissionDetails.setEndorsementNo(new BigDecimal(input[6]));
			ttrnCommissionDetails.setProductId(input[7]);
			ttrnCommissionDetails.setBranchCode(input[8]);
			ttrnCommissionDetails.setSubClass(input[9]);
			ttrnCommissionDetails.setCommissionType(input[10]);
			ttrnCommissionDetails.setLoginId(input[11]);
			ttrnCommissionDetails.setEntryDate(new Date());	
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
			values.put("UWYear" , data.get("uwYear"));
			values.put("expiryDate" , dbf.format(data.get("expiryDate")));
			LocalDate d1 = LocalDate.parse(dbf.format( sdf.parse( icepDate)), DateTimeFormatter.ISO_LOCAL_DATE);
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
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, new BigDecimal(args[2]));
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
			Predicate n3 = cb.between(rd.get("insurerNo"), BigDecimal.ZERO, new BigDecimal(args[1]));
			query1.where(n1,n2,n3).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query1);
			list = result.getResultList();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}
	}
