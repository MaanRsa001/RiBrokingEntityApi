package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SecondPageInsertReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("SumInsuredOurShare")
	private String sumInsuredOurShare;
	
	@JsonProperty("GwpiOurShare")
	private String gwpiOurShare;
	
	@JsonProperty("PmlOurShare")
	private String pmlOurShare;
	
	@JsonProperty("TplOurShare")
	private String tplOurShare;
	
	@JsonProperty("RiskGrade")
	private String riskGrade;
	
	@JsonProperty("OccCode")
	private String occCode;
	
	@JsonProperty("RiskDetail")
	private String riskDetail;
	
	@JsonProperty("FireProt")
	private String fireProt;
	
	@JsonProperty("Scope")
	private String scope;
	
	@JsonProperty("Mbind")
	private String mbind;
	
	@JsonProperty("CategoryZone")
	private String categoryZone;
	
	@JsonProperty("EqwsInd")
	private String eqwsInd;
	
	@JsonProperty("WsThreat")
	private String wsThreat;
	
	@JsonProperty("EqThreat")
	private String eqThreat;
	
	@JsonProperty("Commn")
	private String commn;
	
	@JsonProperty("Brokerage")
	private String brokerage;
	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("LossRecord")
	private String lossRecord;
	
	@JsonProperty("DgmsApproval")
	private String dgmsApproval;
	
	@JsonProperty("UnderwriterCode")
	private String underwriterCode;
	
	@JsonProperty("UwRecommendation")
	private String uwRecommendation;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("OthAccep")
	private String othAccep;
	
	@JsonProperty("ReftoHO")
	private String reftoHO;
	
	@JsonProperty("AcqCost")
	private String acqCost;
	
	@JsonProperty("CuRsn")
	private String cuRsn;
	
	@JsonProperty("Cu")
	private String cu;
	
	@JsonProperty("Othercost")
	private String othercost;
	
	@JsonProperty("MlopYN")
	private String mlopYN;
	
	@JsonProperty("AlopYN")
	private String alopYN;
	
	@JsonProperty("AcqBonus")
	private String acqBonus;
	
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;
	
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("ShSd")
	private String shSd; 
	
	@JsonProperty("RenewalContractno")
	private String renewalContractno;
	
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Year")
	private String year;
	
	@JsonProperty("ProStatus")
	private String proStatus;
}
