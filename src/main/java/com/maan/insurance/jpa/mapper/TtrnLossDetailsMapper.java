package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.facultative.TtrnLossDetails;
import com.maan.insurance.model.req.facultative.InserLossRecordReq;
import com.maan.insurance.model.req.facultative.LossDetailsReq;

@Component
public class TtrnLossDetailsMapper extends AbstractEntityMapper<TtrnLossDetails, InserLossRecordReq>{

	@Override
	public InserLossRecordReq fromEntity(TtrnLossDetails input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnLossDetails toEntity(InserLossRecordReq input) throws ParseException {
		TtrnLossDetails ttrnLossDetails = null;
		if(input != null) {
			ttrnLossDetails = new TtrnLossDetails();
		}
		return ttrnLossDetails;
	}
	
	public TtrnLossDetails toCustomEntity(InserLossRecordReq beanObj, LossDetailsReq req) throws ParseException {
		
		TtrnLossDetails ttrnLossDetails = new TtrnLossDetails();
		ttrnLossDetails.setProposalNo(beanObj.getProposalNo());
		ttrnLossDetails.setContractNo(StringUtils.isBlank(beanObj.getContractNo())?"":beanObj.getContractNo());
		ttrnLossDetails.setEndorsementNo(null);
		ttrnLossDetails.setProductId(beanObj.getProductId());
		ttrnLossDetails.setBranch(beanObj.getBranchCode());
		ttrnLossDetails.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		ttrnLossDetails.setLayerNo(format("0"));
		ttrnLossDetails.setYear(StringUtils.isBlank(req.getLossYear())?"0":req.getLossYear());
		ttrnLossDetails.setLossNo(StringUtils.isBlank(req.getLossNo())?"0":req.getLossNo());
		ttrnLossDetails.setInsuredName(StringUtils.isBlank(req.getLossinsuredName())?"0":req.getLossinsuredName());
		ttrnLossDetails.setInceptionDate(formatDate(StringUtils.isBlank(req.getLossInceptionDate())?"":req.getLossInceptionDate()));
		ttrnLossDetails.setExpirydate(formatDate(StringUtils.isBlank(req.getLossExpiryDate())?"":req.getLossExpiryDate()));
		ttrnLossDetails.setDateOfLoss(formatDate(StringUtils.isBlank(req.getLossDateOfLoss())?"0":req.getLossDateOfLoss()));
		ttrnLossDetails.setCauseOfLoss(StringUtils.isBlank(req.getLossCauseOfLoss())?"0":req.getLossCauseOfLoss().replaceAll(",", ""));
		ttrnLossDetails.setInsuredClaim(formatBigDecimal(StringUtils.isBlank(req.getLossInsuredClaim())?"0":req.getLossInsuredClaim().replaceAll(",", "")));
		ttrnLossDetails.setPremium(formatBigDecimal(StringUtils.isBlank(req.getLossPremium())?"0":req.getLossPremium().replaceAll(",", "")));
		ttrnLossDetails.setLossRatio(formatBigDecimal(StringUtils.isBlank(req.getLossRatio())?"0":req.getLossRatio().replaceAll(",", "")));
		ttrnLossDetails.setLeader(StringUtils.isBlank(req.getLossLeader())?"0":req.getLossLeader());
		ttrnLossDetails.setItiReShare(formatBigDecimal(StringUtils.isBlank(req.getLossITIReShare())?"0":req.getLossITIReShare().replaceAll(",", "")));
		 
		return ttrnLossDetails;
	}

}
