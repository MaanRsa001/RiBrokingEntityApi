package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.model.req.facultative.InsertInsurarerTableInsertReq;
import com.maan.insurance.model.req.facultative.RetroDetails;

@Component
public class TtrnInsurerDetailsMapper extends AbstractEntityMapper<TtrnInsurerDetails, InsertInsurarerTableInsertReq> {

	@Override
	public InsertInsurarerTableInsertReq fromEntity(TtrnInsurerDetails input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnInsurerDetails toEntity(InsertInsurarerTableInsertReq input) throws ParseException {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		if (input != null) {
			ttrnInsurerDetails = new TtrnInsurerDetails();

			ttrnInsurerDetails.setInsurerNo(formatBigDecimal("0"));
			ttrnInsurerDetails.setProposalNo(input.getProposalNo());
			ttrnInsurerDetails.setContractNo("");
			ttrnInsurerDetails.setEndorsementNo(null);
			ttrnInsurerDetails.setType("R");
			ttrnInsurerDetails.setRetroPercentage(formatBigDecimal(StringUtils.isEmpty(input.getRetper())? "0" :input.getRetper()));
			ttrnInsurerDetails.setStatus("Y");
			ttrnInsurerDetails.setUwYear("");
			ttrnInsurerDetails.setRetroType("");
			ttrnInsurerDetails.setEntryDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnInsurerDetails.setSubClass(null);
			ttrnInsurerDetails.setLoginId(input.getLoginid());
			ttrnInsurerDetails.setBranchCode(input.getBranchCode());

		}
		return ttrnInsurerDetails;
	}
	
	public TtrnInsurerDetails toCustomEntity(InsertInsurarerTableInsertReq input) throws ParseException {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		if (input != null) {
			ttrnInsurerDetails = new TtrnInsurerDetails();
			
			ttrnInsurerDetails.setInsurerNo(formatBigDecimal("1"));
			ttrnInsurerDetails.setProposalNo(input.getProposalNo());
			ttrnInsurerDetails.setContractNo(StringUtils.isEmpty(input.getRetroDupContract()) ? "0" :input.getRetroDupContract());
			ttrnInsurerDetails.setEndorsementNo(null);
			ttrnInsurerDetails.setType("C");
			ttrnInsurerDetails.setRetroPercentage(formatBigDecimal(StringUtils.isEmpty(input.getRetper())? "0" :input.getRetper()));
			ttrnInsurerDetails.setStatus("Y");
			ttrnInsurerDetails.setUwYear(StringUtils.isEmpty(input.getRetroDupYerar())? "0" :input.getRetroDupYerar());
			ttrnInsurerDetails.setRetroType("TR");
			ttrnInsurerDetails.setEntryDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnInsurerDetails.setSubClass(null);
			ttrnInsurerDetails.setLoginId(input.getLoginid());
			ttrnInsurerDetails.setBranchCode(input.getBranchCode());

		}
		return ttrnInsurerDetails;
	}
	
	public TtrnInsurerDetails toCustomEntity1(InsertInsurarerTableInsertReq input, RetroDetails req) throws ParseException {
		TtrnInsurerDetails ttrnInsurerDetails = null;
		if (input != null) {
			ttrnInsurerDetails = new TtrnInsurerDetails();
			
			ttrnInsurerDetails.setProposalNo(input.getProposalNo());
			ttrnInsurerDetails.setContractNo(StringUtils.isEmpty(req.getCedingCompanyValList()) ? "0" :req.getCedingCompanyValList());
			ttrnInsurerDetails.setEndorsementNo(null);
			ttrnInsurerDetails.setType("C");
			ttrnInsurerDetails.setRetroPercentage(formatBigDecimal(StringUtils.isEmpty(req.getRetroPercentage())? "0" :req.getRetroPercentage()));
			ttrnInsurerDetails.setStatus("Y");
			ttrnInsurerDetails.setUwYear(StringUtils.isEmpty(req.getUwYearValList())? "0" :req.getUwYearValList());
			ttrnInsurerDetails.setRetroType(StringUtils.isEmpty(req.getRetroTypeValList())? "" :req.getRetroTypeValList());
			ttrnInsurerDetails.setEntryDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnInsurerDetails.setSubClass(null);
			ttrnInsurerDetails.setLoginId(input.getLoginid());
			ttrnInsurerDetails.setBranchCode(input.getBranchCode());

		}
		return ttrnInsurerDetails;
	}
}
