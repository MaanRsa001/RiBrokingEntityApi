package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.propPremium.TtrnDepositRelease;

@Component
public class TtrnDepositReleaseMapper extends AbstractEntityMapper<TtrnDepositRelease, String[]> {

	@Override
	public String[] fromEntity(TtrnDepositRelease input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnDepositRelease toEntity(String[] input) throws ParseException {
		TtrnDepositRelease ttrnDepositRelease = null;
		if (input != null) {
			ttrnDepositRelease = new TtrnDepositRelease();
			ttrnDepositRelease.setProposalNo(input[0]);
			ttrnDepositRelease.setContractNo(input[0]);
			ttrnDepositRelease.setDeptId(input[0]);
			ttrnDepositRelease.setReleaseType(input[0]);
			ttrnDepositRelease.setRlNo(input[0]);
			ttrnDepositRelease.setRlTransactionNo(input[0]);
			ttrnDepositRelease.setRlTransactionDate(formatDate(input[0]));
			ttrnDepositRelease.setRtTransactionNo(input[0]);
			ttrnDepositRelease.setRtTransactionDate(formatDate(input[0]));
			ttrnDepositRelease.setRlCurrencyId(formatLong(input[0]));
			ttrnDepositRelease.setRtCurrencyId(formatLong(input[0]));
			ttrnDepositRelease.setRlAmountInRlCurr(formatLong(input[0]));
			ttrnDepositRelease.setRlAmountInRtCurr(formatLong(input[0]));
			ttrnDepositRelease.setExchangeRate(formatLong(input[0]));
			ttrnDepositRelease.setLoginId(input[0]);
			ttrnDepositRelease.setBranchCode(input[0]);
			ttrnDepositRelease.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnDepositRelease.setTableMoveStatus(input[0]);
			ttrnDepositRelease.setTempRequestNo(formatLong(input[0]));

		}
		return ttrnDepositRelease;
	}

}
