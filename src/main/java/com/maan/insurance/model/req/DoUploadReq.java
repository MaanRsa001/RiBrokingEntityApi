package com.maan.insurance.model.req;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.upload.DoDeleteDocDetailsReq;

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
	@JsonProperty("OurFileName")
	private String ourFileName; 
	@JsonProperty("TranNo")
	private String tranNo; 
	@JsonProperty("FilePath")
	private String filePath; 
	@JsonProperty("StartIndex")
	private String startIndex; 
	@JsonProperty("EndIndex")
	private String endIndex;  
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("DocId")
	private String docId;
	@JsonProperty("DocTypeId")
	private String docTypeId; 
//	@JsonProperty("Upload")
//	private List<File> upload;  
	@JsonProperty("UploadFileName")
	private String uploadFileName;
	@JsonProperty("DocDesc")
	private String docDesc; 
	

}
