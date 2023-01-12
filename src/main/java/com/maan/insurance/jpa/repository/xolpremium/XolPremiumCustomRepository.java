package com.maan.insurance.jpa.repository.xolpremium;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;

public interface XolPremiumCustomRepository {
	/* old code
	public String selectRPPremiumOC(String contractNo, String layerNo);
	
	public String xolSelectRPPremiumOC(String contractNo, String layerNo);
	
	public List<Tuple> selectGetTreatySPRetro(String contractNo);
	
	public List<Tuple> selectInsDetails(String proposalNo, Integer noOfRetro);
	
	public String selectGetNoRetroCess(String contractNo);
	
	public String selectGetSumOfShareSign(String contractNo, Integer shareSign); */
	
	public List<Tuple> selectPremiumedList1(GetPremiumedListReq beanObj);
	
	public List<Tuple> selectRetroPremiumedList1(GetPremiumedListReq beanObj);
	
	public List<Tuple> xolPremiumListTemp(GetPremiumedListReq beanObj);
	
	public String selectAllocatedYN(String contractNo, String transactionNo, String layerNo);
	
	public String getAllocationStatus(String transactionNo);
	
	public String getRetroStatus(String transactionNo);
	
	public String getRetroStatus1(String transactionNo);
		
	public String selectRskSubjPremiumOc(String contractNo, String layerNo);
	
	public String selectPreviousPremium(String contractNo);
	
	public List<Tuple> getPremiumMndInsList(String contractNo, String layerNo);
	
	public List<Tuple> selectmdInstalmentList(String contractNo, String layerNo);
	
	public String getGnpiCountPre(String contractNo, String layerNo, String endoType);
	
	public List<Tuple> selectXolPreList(String contNo, String layerNo);
	
	public String selectCeaseStatus(String contNo, String layerNo);
	
	public List<Tuple> selectDeptId(String contNo, String productId);
	
	public String selectMndPremiumOC(String contNo, String layerNo, String instalmentno);
	
	public List<Tuple> getBrokerCedingName(String contNo, String branchCode);
	
	public List<Map<String, Object>> getCurrencyList(String branchCode);
	
	public List<Tuple> selectGetAlloTransaction(String contNo, String transactionNo);
	
	public List<Tuple> selectTreatyContDet1XolLayerNo2(ContractDetailsReq req);
	
	public List<Tuple> selectCommissionDetails(String proposalNo);
	
	public List<Tuple> selectTreatyXOLfacProposalDetails(String proposalNo);
	
	public String selectCurrecyName(String branchCode);
	
	public List<Tuple> getXolPremiumEditRTemp(String contNo, String reqNo);
	
	public List<Tuple> selectTreetyXOLPremiumEdit(String contNo, String transactionNo);
	
	public List<Tuple> selectRetrotreetyXOLPremiumEdit(String contNo, String transactionNo);
	
	public List<Double> getGnpiPremium(GetAdjPremiumReq bean);
	
	public List<Double> getDepositPremium(GetAdjPremiumReq bean);
	
	public List<Tuple>  xolPremiumViewDetailsTemp(String branchCode, String contNo, String productId, String requestNo);
	
	public List<Tuple>  premiumSelectXolPremiumView(String branchCode, String contNo, String productId, String requestNo);
	
	public List<Tuple>  premiumSelectRetroXolPremiumView(String branchCode, String contNo, String productId, String requestNo);
	
	public String premiumSelectCurrency(String currencyId, String branchCode);
	
	public List<Tuple> getSettlementStatus(String contNo);
	
	public String getSeqName(String branchCode);
	
	public String getLpad(String name);
	
	public Integer facTempStatusUpdate(PremiumInsertMethodReq beanObj);
	
	public Integer updateMndInstallment(PremiumInsertMethodReq beanObj);
	
	public Integer updateRevTransactionNo(String input1, String input2);
	
	public Integer updateRevTransactionNoRetro(String input1, String input2);
	
	public Integer updateMndInstal(String input1, String input2);
	
	public Integer xolPremiumUpdateUpdateTemp(String[] args) throws ParseException;
	
	public Integer premiumUpdateXolUpdatePre(String[] args) throws ParseException, ParseException;
	
	public Integer premiumUpdateRetroxolUpdatePre(String[] args) throws ParseException;
	
	public void premiumDetailArchive(PremiumInsertMethodReq beanObj, String netdueOc);
	
	public RskPremiumDetailsTemp facPremiumTempToMain(String requestNo, String branchCode);
	
	public void premiumSpRetroSplit(PremiumInsertMethodReq beanObj);
	
	public Integer prclDelete(PremiumInsertMethodReq beanObj);

	public void premiumRiSplit(PremiumInsertMethodReq beanObj);

	public List<Tuple> premiumSelectXolPremiumViewRi(String branchCode, String contNo, String productId,
			String transactionNo);

	public List<Tuple> selectTreetyXOLPremiumEditRi(String contNo, String transactionNo);
	
	
}
