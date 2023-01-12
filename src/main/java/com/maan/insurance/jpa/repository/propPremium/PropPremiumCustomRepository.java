package com.maan.insurance.jpa.repository.propPremium;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.req.premium.ContractidetifierlistReq;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.premium.PremiumListReq;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes;

public interface PropPremiumCustomRepository {

	public List<Tuple> premiumSelectPremiumedList(GetPremiumedListReq req);

	public List<Tuple> pityPremiumListTemp(GetPremiumedListReq req);

	public List<Tuple> premiumSelectFacTreatyPreList(String contNo, String deptId);

	public String getAccPeriod(String proposalNo);

	public List<Tuple> commonSelectGetconstdetPity(String categoryId, String status, String accPeriod);

	public List<Tuple> getBaseLayer(String contractNo, String deptId);

	public List<Tuple> getSlideCommValue(String proposalNo, String subClass);

	public String getDeptId(String proposalNo);

	public List<Tuple> getSlideCommValue2(String proposalNo, String subClass);

	public List<BigDecimal> selectPremiumSum(String contractNo);

	public List<Tuple> getContPrem(String contractNo, String departmentId, String branchCode);

	public List<Tuple> claimSelectClaimTableList(BigDecimal contractNo, String layerNo, String subClass);

	public String getCountCashlossPrem(String contractNo, String branchCode, String tableMoveStatus);

	public String getCountDepositPrem(String contractNo, String branchCode, String tableMoveStatus, String releaseType);

	public List<Tuple> getPremiumReservedDetails(GetPremiumReservedReq req);
	
	public List<Tuple> getLossReservedDetails(GetPremiumReservedReq req);
	
	public String getCurrencyName(String branchCode, String currencyId);
	
	public List<Tuple> getAllocatedCasLoss(String proposalNo);
	
	public List<Tuple> getAllocatedTransList(String proposalNo);
	
	public List<Tuple> getCashLossCreadt(String contNo, String departmentId, String claimPayNo);
	
	public String getExcessRatePercent();
	
	public List<Map<String, Object>> currencyList(String branchCode);
	
	public List<Tuple> premiumSelectGetAlloTransaction(String contNo, String transactionNo);
	
	public List<Tuple> brokerCedingName(String contNo, String branchCode);
	
	public String getDepartmentNo(String contractNo);
	
	public String getSumOfShareSign(String retroContractNo);
	
	public List<Tuple> premiumSelectInsDetails(String proposalNo, String noOfRetro);
	
	public Integer getCleanCutContCount(String contractNo);
	
	public List<Map<String, Object>> getOSBList(String transaction, String contractNo, String branchCode);
	
	public List<Tuple> premiumSelectGetTreatySPRetro(String contNo); 
	
	public String premiumSelectRLNO(String branchCode);
	
	public Integer updateLossReserve(String contractNo,String requestNo ,String transaction, String tableMoveStatus);
	
	public Integer updatePremiumReserve(String contractNo,String requestNo ,String transaction, String tableMoveStatus);
	
	public void premiumSpRetroSplit(InsertPremiumReq input);
	
	public RskPremiumDetailsTemp facPremiumTempToMain(String requestNo, String branchCode);
	
	public String getSeqName(String branchCode);
	
	public String getLpad(String name);
	
	public Integer facTempStatusUpdate(InsertPremiumReq req);
	
	public List<Tuple> premiumSelectTreatyContDet(com.maan.insurance.model.req.premium.ContractDetailsReq req);
	
	public List<Tuple> selectCommissionDetails(String proposalNo);
	
	public List<Tuple> premiumSelectTreatyProposalDetails(String proposalNo);
	
	public List<Tuple> premiumSelectCashLossCreditUpdate(String contNo, String transactionNo);
	
	public String selectCurrecyName(String branchCode);
	
	public String premiumSelectSumOfPaidPremium(String contractNo);
	
	public List<Tuple> getSettlementStatus(String contNo);

	public List<Tuple> getPremiumTempView(String branchCode,String productId, String contractNo, String requestNo);

	public List<Tuple> getPremiumView(String branchCode,String productId, String contractNo, String transactionNo);

	public List<Tuple> getPremiumTempEdit(String contNo, String requestNo);

	public List<Tuple> getPremiumEdit(String contNo, String transactionNo);

	public Integer updateDepositRelease(String contNo, String requestNo, String transactionNo, String string);

	public Integer updateCashLossStatus(String contNo, String requestNo, String transactionNo, String string);

	public Integer updateClaimPayment(String contNo, String branchCode, String requestNo, String string,
			String claimNumber, String claimPaymentNo, String contNo2, String claimNumber2, String claimPaymentNo2);

	public void premiumarchive(String contNo, String layerNo, String transactionNo, String currencyId, String exchRate,
			String netDueOc, String departmentId, String productId);

	public void premiumRiSplit(InsertPremiumReq req);

	public List<Tuple> getPremiumFullList(PremiumListReq bean);

	public List<Tuple> getPremiumTempList(PremiumListReq bean);

	public List<Tuple> getPremiumRiFullList(PremiumListReq bean);

	public List<Tuple> contractIdentifierList(ContractidetifierlistReq bean);

	public List<Tuple> getPremiumEditRi(String contNo, String transactionNo);

	public List<Tuple> getPremiumViewRi(String branchCode, String productId, String contractNo, String transactionNo);


	
}
