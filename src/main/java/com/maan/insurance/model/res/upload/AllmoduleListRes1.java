package com.maan.insurance.model.res.upload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes1;

import lombok.Data;

@Data
public class AllmoduleListRes1 {
	@JsonProperty("ModuleType")
	private String moduleType;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("DocId")
	private String docId;
	
	@JsonProperty("DocDesc")
	private String docDesc;
	
	@JsonProperty("OrgFileName")
	private String orgFileName;
	
	@JsonProperty("tranNo")
	private String TranNo;
	
	@JsonProperty("OurFileName")
	private String ourFileName;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DocType")
	private String docType;
	
	@JsonProperty("DocName")
	private String docName;
}
