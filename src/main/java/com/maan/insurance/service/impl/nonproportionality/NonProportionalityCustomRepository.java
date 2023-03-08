package com.maan.insurance.service.impl.nonproportionality;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnRetroCessionary;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnIeModule;
import com.maan.insurance.model.entity.TtrnRip;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.entity.TtrnRskClassLimits;

public interface NonProportionalityCustomRepository {

	String getShortName(String branchCode);

	List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId);

	List<Tuple> getRemarksDetails(String proposalNo, String layerNo);

	List<Tuple> facSelectViewInsDetails(String[] args);

	List<Tuple> facSelectInsDetails(String[] args);

	List<Tuple> facSelectRetrocontdet23(String productId, String uwYear, String incepDate, String branchCode);

	List<Tuple> riskSelectGetRskProIdByProNo(String proposalNo);

	TtrnRiskDetails ttrnRiskDetailsUpdate(String[] args);

	TtrnRiskDetails ttrnRiskDetailsInsert(String[] args);

	List<Tuple> riskSelectGetRetroCessDtls(String[] selectArgs);

	List<Tuple> riskSelectViewRetroCessDtls(String[] selectArgs);

	int riskUpdatePro35FirPageRskPro(String[] obj);

	PositionMaster positionMasterUpdate(String[] args);

	void deleteClassLimit(String proposalno);

	TtrnRskClassLimits insertClassLimit(String[] args1);

	TtrnRiskProposal updateFirstPageFields(String[] args);

	String riskSelectGetRenewalStatus(String proposalno);

	List<Tuple> riskSelectGetLayerDupcheckByBaseLayer(String layerNo, String baseLayer);

	List<Tuple> riskSelectGetLayerDupcheckByProNo(String layerNo, String proposalNo, String proposalOrBase);

	List<Map<String, Object>> facSelectGetRenewalValidation(String incepDate, String renewalContractNo);

	List<Tuple> riskSelectGetSecondViewData(String proposalNo, String amendId);

	List<Tuple> getMappingProposalNo(String contractListVal, String layerNo, String departId, String uwYear);

	List<Tuple> riskSelectGetEditModeDataPro3ContCond1(String contractNo);

	List<Tuple> riskSelectGetEditModeDataPro3ProCond1(String proposalNo);

	String riskSelectCeaseStatus(String proposalNo);
	
	List<Tuple> facSelectUwYear(String productid, String incepDate, String branchCode);

	List<Tuple> riskSelectUwYear(String productid, String incepDate, String branchCode);

	List<Tuple> facSelectRetroContDetTR(String productid, String uWYear, String incepDate, String branchCode);

	List<Tuple> facSelectRetroContDet(String productid, String retroType, String uWYear, String incepDate,
			String branchCode);

	List<Tuple> facSelectRetroDupContract(String UWYear, String incepDate,	String branchCode);

	TtrnRiskProposal riskUpdatePro35RskProposal(String[] obj);

	String riskSelectMaxRskStatus(String string);

	TtrnRiskCommission riskInsertPro3SecComm(String[] obj1);

	TtrnRiskCommission riskUpdatePro3SecComm(String[] obj1);

	List<Tuple> riskSelectChechProposalStatus(String proposalNo);

	TtrnRiskProposal ttrnRiskProposalInsert(String[] args);

	PositionMaster positionMasterInsert(String[] args);

	int reinstatementCountMain(String proposalNo, String branchCode, String amendId);

	int reinstatementCountMainReference(String referenceNo, String branchCode, String amendId);

	String getSignShareProduct23(String proposalNo);

	List<Tuple> reinstatementMainSelectA(String proposalNo, String branchCode);

	List<Tuple> reinstatementMainSelectAReference(String referenceNo, String branchCode);

	TtrnRip insertReinstatementMain(String[] args);

	TtrnRip insertReinstatementMainB(String[] args1);

	List<Tuple> bonusMainSelect(String proposalNo, String branchCode, String acqBonus);

	List<Tuple> bonusMainSelectReference(String referenceNo, String branchCode, String acqBonus);

	TtrnBonus bonusMainInsert(String[] args);

	List<Tuple> getInclusionexList(String proposalNo, String branchCode);

	TtrnIeModule inclusionExclusionInsert(String[] args);

	List<Tuple> riskSelectGetEditModeSecPagePro3Data(String proposalNo, String layerNo);

	int getInstalmentCount(String proposalNo, String layerNo);

	List<Tuple> riskSelectGetInstalmentData(String[] args);

	void deleteRemarksDetails(String proposalNo, String layerNo);

	TtrnRiskRemarks ttrnRiskRemarksInsert(String[] obj);

	List<Tuple> getClassLimitDetails(String proposalNo, String layerNo);

	int getMaxLayerNo(String proposalno);

	void getRetroConUpdate(String maxContarctNo, String proposalNo, String layerNo);

	TtrnRiskProposal riskUpdatePro35ContSecPage(String[] obj);

	void riskUpdateHomeContNo(String[] args);

	String riskSelectGetRskContractNo(String proposalNo);

	void deleteTtrnRetroCessionary(String proposalNo, String endtNo);

	TtrnRetroCessionary riskInsertRetroCessDtls(String[] obj);

	void deleteTtrnMndInstallments(String proposalNo, String endtNo);

	TtrnMndInstallments riskInsertInstalPrem(String[] obj);

	void retroDeleteQuery(String proposalNo, String endtNo);

	void facUpdateInsDetails(String proposalNo, String valueOf);

	TtrnInsurerDetails facInsertInsDetails(String[] obj);

	TtrnInsurerDetails facUpddateUpdDetails(String[] obj);

	void updateReinstatementTotalNo(String proposalNo, String valueOf);

	void reInstatementUpdate(String[] args);

	TtrnCrestazoneDetails moveToCrestaMainTable(String[] obj);

	void updateContractDetails(String[] args);

	List<Tuple> getLayerContractInfo(String[] obj);

	List<Tuple> getLayerInfo(String[] obj);

	void cancelOldProposal(String proposalNo);

	void cancelProposal(String newProposal);

	int getCountOfInstallmentNumber(String proposalNo);

	BigDecimal getRipSno();

}
