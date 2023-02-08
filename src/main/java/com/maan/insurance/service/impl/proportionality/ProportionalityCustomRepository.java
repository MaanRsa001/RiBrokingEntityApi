package com.maan.insurance.service.impl.proportionality;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCedentRet;
import com.maan.insurance.model.entity.TtrnCommissionDetails;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnPttySection;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;

public interface ProportionalityCustomRepository {

	TtrnRiskDetails ttrnRiskDetailsUpdate(String[] args) throws ParseException;

	TtrnRiskDetails ttrnRiskDetailsInsert(String[] args) throws ParseException;

	TtrnRiskProposal ttrnRiskProposalUpdate(String[] obj) throws ParseException;

	PositionMaster positionMasterUpdate(String[] obj);

	TtrnRiskProposal ttrnRiskProposalInsert(String[] obj) throws ParseException;

	PositionMaster positionMasterInsert(String[] obj) throws ParseException;

	TtrnRiskProposal updateFirstPageFields(String[] obj) throws ParseException;

	TtrnRiskRemarks ttrnRiskRemarksInsert(String[] obj) throws ParseException;

	TtrnCedentRet ttrnCedentRetInsert(String[] obj) throws ParseException;

	TtrnRiskProposal ttrnRiskProposalSecondPageUpdate(String[] obj) throws ParseException;

	TtrnRiskCommission ttrnRiskCommissionSecondPageUpdate(String[] obj1)  throws ParseException;

	TtrnRiskCommission ttrnRiskCommissionSecondPageInsert(String[] obj1) throws ParseException;

	int facUpdateInsDetails(String proposalNo, String string);

	TtrnInsurerDetails facInsertInsDetails(String[] obj) throws ParseException;

	TtrnCrestazoneDetails moveToCrestaMainTable(String[] obj) throws ParseException;

	TtrnBonus bonusMainInsertPtty(String[] args) throws ParseException;

	TtrnCommissionDetails commissionInsert(String[] args);

	List<Tuple> riskSelectGetEditModeData1(String[] args) throws ParseException;

	List<Tuple> riskSelectGetEditModeData2(String[] args) throws ParseException;

	List<Tuple> getRetDetails(String[] args);

	List<Tuple> getBaseLayerDetails(String productId, String branchCode, String proposalNo);

	List<Tuple> riskSelectGetEditModeSecPageData(String proposalNo);

	List<Tuple> riskSelectGetQuotaShare(String proposalNo);

	List<Tuple> riskSelectCommGetquotashare(String proposalNo);

	List<Map<String, Object>>  riskSelectCommGetquotashare(String icepDate, String contId, String deptId);

	String getBaseProposalNo(String proposalNo);

	List<Tuple> profitCommissionEnable(String baseLayer);

	List<Tuple> profitCommissionEnableLoss(String baseLayer);

	List<Tuple> profitCommissionEnableCresta(String baseLayer);

	List<Tuple> profitCommissionEnableSlide(String baseLayer);

	int commissionTypeCount(String[] args);

	String getSignShareProduct23(String proposalNo);

	List<Tuple> riskSelectGetBrokerage(String layerProposalNo);

	List<Tuple> facSelectViewInsDetails(String[] args);

	List<Tuple> facSelectInsDetails(String[] args);

	String getMaxSectionNoDet(String proposalNo);

	List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId);

	String riskSelectCeaseStatus(String proposalNo);

	TtrnRiskProposal riskUpdatePro24ContSecPage(String[] obj);

	String riskSelectGetRskContractNo(String proposalNo);

	TtrnPttySection insertSectionDetails(String[] args);

	int getCountRetention(String proposalNo, String productId);

	void updateRetenContno(String contNo, String productId, String proposalNo, String departmentId);

	List<Tuple> bonusMainSelect(String proposalNo, String branchCode);

	List<Tuple> bonusMainSelectReference(String referenceNo, String branchCode);

	List<Tuple> bonusMainSelectLpc(String proposalNo, String branchCode);

	List<Tuple> bonusMainSelectReferenceLpc(String referenceNo, String branchCode);

	List<Tuple> selectSlidingScaleMethodInfo(String proposalNo, String branchCode);

	List<Tuple> selectSlidingScaleMethodInfoRef(String referenceNo, String branchCode);

	void insertScMethodInfo(String[] args);

	List<Tuple> getRiskDetailsEditQuery(boolean contractMode, String proposalNo);

	List<Tuple> riskSelectChechProposalStatus(String proposalNo);

	void insertRiDetails(String[] obj);


	int updateBonus(String requestNumber, String proposalNo);

	int updateRip(String requestNumber, String proposalNo);

	void riskUpdateHomeContNo(String[] args);

	List<Tuple> commissionTypeList(String proposalno, String branchCode, String commissionType, String contractNo);

	List<Tuple> commissionTypeListReference(String referenceNo, String branchCode, String commissionType,
			String contractNo);


}
