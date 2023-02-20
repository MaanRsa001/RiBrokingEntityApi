package com.maan.insurance.model.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.InsertDocdetailsReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoUploadReq {
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ModuleType")
	private String moduleType;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("TranNo")
	private String tranNo; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("InsertDocdetailsReq")
	private List<InsertDocdetailsReq> insertDocdetailsReq; 
	

}
