package com.maan.insurance.jpa.mapper;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.facultative.TtrnFacSi;
import com.maan.insurance.model.req.facultative.CoverSNoReq;
import com.maan.insurance.model.req.facultative.InsertCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InsertXolCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.XolcoverSNoReq;

@Component
public class TtrnFacSiMapper extends AbstractEntityMapper<TtrnFacSi, InsertCoverDeductableDetailsReq>{

	@Override
	public InsertCoverDeductableDetailsReq fromEntity(TtrnFacSi input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnFacSi toEntity(InsertCoverDeductableDetailsReq input) throws ParseException {
		TtrnFacSi ttrnFacSi = null;
		if(input != null) {
			ttrnFacSi = new TtrnFacSi();
		}
		return ttrnFacSi;
	}
	
	public TtrnFacSi toCustomEntity(InsertCoverDeductableDetailsReq beanObj, CoverSNoReq req, int i) {
		
		TtrnFacSi ttrnFacSi = new TtrnFacSi();
		ttrnFacSi.setProposalNo(formatBigDecimal(beanObj.getProposalNo()));
		ttrnFacSi.setContractNo(formatBigDecimal(beanObj.getContractNo()));
		ttrnFacSi.setLayerNo(formatBigDecimal("0"));
		ttrnFacSi.setDeptId(formatBigDecimal(beanObj.getDepartmentId()));
		ttrnFacSi.setProductId(formatBigDecimal(beanObj.getProductId()));
		ttrnFacSi.setAmendId(null);
		ttrnFacSi.setRskSno(formatBigDecimal(String.valueOf(i+1)));
		ttrnFacSi.setRskClass(req.getCoverdepartId());
		ttrnFacSi.setRskSubclass(req.getCoversubdepartId());
		ttrnFacSi.setRskType(req.getCoverTypeId());
		ttrnFacSi.setRskCoverlimitOc(formatBigDecimal(req.getCoverLimitOC().replace(",", "")));
		ttrnFacSi.setRskDeductablelimitOc(formatBigDecimal(req.getDeductableLimitOC().replace(",", "")));
		ttrnFacSi.setRskCoverageDays(formatBigDecimal(req.getCoverageDays().replace(",", "")));
		ttrnFacSi.setRskDeductableDays(formatBigDecimal(req.getDeductableDays()));
		ttrnFacSi.setRskPremiumRate(formatBigDecimal(req.getPremiumRateList()));
		ttrnFacSi.setRskGwpiOc(formatBigDecimal(req.getEgnpiAsPerOff().replace(",", "")));
		ttrnFacSi.setRskCoverRemarks(req.getCoverRemark());
		ttrnFacSi.setLoginId(formatBigDecimal(beanObj.getLoginid()));
		ttrnFacSi.setBranchCode(formatBigDecimal(beanObj.getBranchCode()));
		ttrnFacSi.setSysDate(null);
		ttrnFacSi.setPmlPercentage(formatBigDecimal(StringUtils.isEmpty(req.getPmlPerList())? "" :req.getPmlPerList().replace(",", "")));
		ttrnFacSi.setPmlHunPerOc(formatBigDecimal(StringUtils.isEmpty(req.getPmlHundredPer())? "" :req.getPmlHundredPer().replace(",", "")));
		ttrnFacSi.setRskBusinessType(beanObj.getType());
		 
		return ttrnFacSi;
	}
	
public TtrnFacSi toCustomXolEntity(InsertXolCoverDeductableDetailsReq beanObj, XolcoverSNoReq req, int i) {
		
		TtrnFacSi ttrnFacSi = new TtrnFacSi();
		ttrnFacSi.setProposalNo(formatBigDecimal(beanObj.getProposalNo()));
		ttrnFacSi.setContractNo(formatBigDecimal(beanObj.getContractNo()));
		ttrnFacSi.setLayerNo(formatBigDecimal("0"));
		ttrnFacSi.setDeptId(formatBigDecimal(beanObj.getDepartmentId()));
		ttrnFacSi.setProductId(formatBigDecimal(beanObj.getProductId()));
		ttrnFacSi.setAmendId(null);
		ttrnFacSi.setRskSno(formatBigDecimal(String.valueOf(i+1)));
		ttrnFacSi.setRskClass(req.getXolcoverdepartId());
		ttrnFacSi.setRskSubclass(req.getXolcoversubdepartId());
		ttrnFacSi.setRskCoverlimitOc(formatBigDecimal(req.getXolcoverLimitOC().replace(",", "")));
		ttrnFacSi.setRskDeductablelimitOc(formatBigDecimal(req.getXoldeductableLimitOC().replace(",", "")));
		ttrnFacSi.setRskPremiumRate(formatBigDecimal(req.getXolpremiumRateList()));
		ttrnFacSi.setRskGwpiOc(formatBigDecimal(req.getXolgwpiOC().replace(",", "")));
		ttrnFacSi.setLoginId(formatBigDecimal(beanObj.getLoginid()));
		ttrnFacSi.setBranchCode(formatBigDecimal(beanObj.getBranchCode()));
		ttrnFacSi.setSysDate(null);
		ttrnFacSi.setRskBusinessType(beanObj.getType());
		 
		return ttrnFacSi;
	}

	

}
