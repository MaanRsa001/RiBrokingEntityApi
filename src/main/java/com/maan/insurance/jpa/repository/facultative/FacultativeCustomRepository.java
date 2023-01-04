package com.maan.insurance.jpa.repository.facultative;

import java.sql.Date;
import java.util.List;

import javax.persistence.Tuple;

import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;

public interface FacultativeCustomRepository {

	public String getShortName(String branchcode);

	public Integer updateUWShare(String shSd, String proposalNo);

	public List<TtrnBonus> bonusMainSelect(String proposalNo, String branchCode, String acqBonus);

	public List<Tuple> facSelectUwYear(String productId, Date incepDate, String branchCode);

	public List<Tuple> getRemarksDetails(String proposalNo, String layerNo);

	public List<Tuple> getLossDetails(String proposalNo, String layerNo);

	public String bonusPreviousTypeCheck(String proposalNo, String branch, String layerno);

	public Integer bonusMainDelete(String proposalNo, String branchCode, String acqBonus, String layerno);

	public Integer bonusMainDelete2(String proposalNo, String endoNo, String branchCode, String acqBonus,
			String layerno);
	
	public Integer riskComUpdate(UpdateSecondPageReq beanObj);
	
	public Integer bonusCountMain(String proposalNo, String branchCode, String acqBonus, String endorsmentno,
			String layerNo);
	
	public String getSignShareProduct1(String proposalNo);
	
	public List<Tuple> getXolcoverDeductableDetails(String proposalNo, String layerNo);
	
	public List<Tuple> getCoverDeductableDetails(String proposalNo, String layerNo);
	
	public Integer deleteRemarksDetails(String proposalNo, String layerNo);
	
	public String ttrnRiskRemarksAmendId(String proposalNo);
	
	public Integer deleteCoverdeductableDetails(String proposalNo, String layerNo);
	
	public String ttrnFacSiAmendId(String proposalNo);
	
	public List<Tuple> facSelectRetroContDetTR(String productId, String year, Date incepDate, String branchCode);
	
	public List<Tuple> facSelectRetroContDet(String productId, String type, String year, Date incepDate, String branchCode);
	
	public List<Tuple> facSelectRetroDupContract(String productId, String type, String year, Date incepDate, String branchCode);
	
	public String getCrestaDetailCount(String proposalNo, String amendId, String branchCode);
	
	public Integer creataContractUpdate(String contractNo, String proposalNo, String amendId, String branchCode);
	
	public Integer updateContractDetails(InsertBonusDetailsReq beanObj);
	
	public String selectAmendId(String proposalNo);
	
	public Integer deleteFaculData(String proposalNo, String endorsmentno);
	
	public Integer facUpdateFacContractNo(String facContractNo, String contractNo);
	
	public Integer deleteTtrnMndInstakkments(String proposalNo, String endorsmentno);
	
	public Integer lossDelete(String proposalNo, String layerNo); 
	
	public List<Tuple> facSelectViewInsDetails(String endorsmentno, String proposalNo, Double noOfInsurer);
	
	public List<Tuple> facSelectInsDetails(String proposalNo, Double noOfInsurer);
	
	public List<Tuple> facSelectSecondPageDet(String proposalNo, String branchCode);
	
	public String premiumSelectCeaseStatus(String proposalNo);
	
	public List<Tuple> riskSelectGetInstalmentData(String proposalNo, String layerNo);
	
	public List<Tuple> facSelectShowSecondData(String proposalNo, String productId, String branchCode);
	
	public Integer updateRiskDetails(FirstPageInsertReq beanObj);
	
	public Integer updateFacRiskProposal(FirstPageInsertReq beanObj);
	
	public Integer facSpFacultativepage1(String[] args);
	
	public Integer facSpFacultativepage2(String[] args);
	
	public List<Tuple> facSelectContGen(String proposalNo);
	
	public Integer commonUpdateRiskDetContNo(String contractNo, String proposalNo);
	
	public Integer commonUpdatePosMasDetContNo(String contractNo, String proposalNo);
	
	
}
