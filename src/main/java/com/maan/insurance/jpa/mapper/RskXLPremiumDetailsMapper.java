package com.maan.insurance.jpa.mapper;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.xolpremium.RskXLPremiumDetails;

@Component
public class RskXLPremiumDetailsMapper extends AbstractEntityMapper<RskXLPremiumDetails, String[]>{

	@Override
	public String[] fromEntity(RskXLPremiumDetails input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RskXLPremiumDetails toEntity(String[] input) throws ParseException {
		RskXLPremiumDetails rskXLPremiumDetails = null;
		if(input != null) {
			rskXLPremiumDetails = new RskXLPremiumDetails();
			rskXLPremiumDetails.setContractNo(formatLong(input[0]));
			rskXLPremiumDetails.setTransactionNo(format(input[1]));
			rskXLPremiumDetails.setTransactionMonthYear(getTimestamp(input[2]));
			rskXLPremiumDetails.setAccountPeriodQtr(input[3]);
			rskXLPremiumDetails.setAccountPeriodYear(formatDouble(input[4]));
			rskXLPremiumDetails.setCurrencyId(formatDouble(input[5]));
			rskXLPremiumDetails.setExchangeRate(formatDouble(input[6]));
			rskXLPremiumDetails.setBrokerage(formatDouble(input[7]));
			rskXLPremiumDetails.setBrokerageAmtOc(formatDouble(input[8]));
			rskXLPremiumDetails.setTax(formatDouble(input[9]));
			rskXLPremiumDetails.setTaxAmtOc(formatDouble(input[10]));
			rskXLPremiumDetails.setEntryDateTime(getTimestamp(input[11]));
			rskXLPremiumDetails.setCommission(formatDouble(input[12]));
			rskXLPremiumDetails.setMDpremiumOc(formatDouble(input[13]));
			rskXLPremiumDetails.setAdjustmentPremiumOc(formatDouble(input[14]));
			rskXLPremiumDetails.setRecPremiumOc(formatDouble(input[15]));
			rskXLPremiumDetails.setStatus(input[16]);
			rskXLPremiumDetails.setNetdueOc(formatDouble(input[17]));
			rskXLPremiumDetails.setLayerNo(format(input[18]));
			rskXLPremiumDetails.setEnteringMode(input[19]);
			rskXLPremiumDetails.setReceiptNo(format(input[20]));
			rskXLPremiumDetails.setInstalmentNumber(input[21]);
			rskXLPremiumDetails.setSettlementStatus(input[22]);
			rskXLPremiumDetails.setOtherCostOc(formatDouble(input[23]));
			rskXLPremiumDetails.setBrokerageAmtDc(formatDouble(input[24]));
			rskXLPremiumDetails.setTaxAmtDc(formatDouble(input[25]));
			rskXLPremiumDetails.setMDpremiumDc(formatDouble(input[26]));
			rskXLPremiumDetails.setAdjustmentPremiumDc(formatDouble(input[27]));
			rskXLPremiumDetails.setRecPremiumDc(formatDouble(input[28]));
			rskXLPremiumDetails.setNetdueDc(formatDouble(input[29]));
			rskXLPremiumDetails.setOtherCostDc(formatDouble(input[30]));
			rskXLPremiumDetails.setCedantReference(input[31]);
			rskXLPremiumDetails.setRemarks(input[32]);
			rskXLPremiumDetails.setTotalCrOc(formatDouble(input[33]));
			rskXLPremiumDetails.setTotalCrDc(formatDouble(input[34]));
			rskXLPremiumDetails.setTotalDrOc(formatDouble(input[35]));
			rskXLPremiumDetails.setTotalDrDc(formatDouble(input[36]));
			rskXLPremiumDetails.setEntryDate(getTimestamp(input[37]));
			rskXLPremiumDetails.setWithHoldingTaxOc(formatDouble(input[37]));
			rskXLPremiumDetails.setWithHoldingTaxDc(formatDouble(input[38]));
			rskXLPremiumDetails.setRiCession(input[39]);
			rskXLPremiumDetails.setLoginId(input[40]);
			rskXLPremiumDetails.setBranchCode(input[41]);
			rskXLPremiumDetails.setSubClass(format(input[42]));
			rskXLPremiumDetails.setTdsOc(formatDouble(input[43]));
			rskXLPremiumDetails.setTdsDc(formatDouble(input[44]));
			rskXLPremiumDetails.setStOc(formatDouble(input[45]));
			rskXLPremiumDetails.setStDc(formatDouble(input[46]));
			rskXLPremiumDetails.setBonusOc(formatDouble(input[47]));
			rskXLPremiumDetails.setBonusDc(formatDouble(input[48]));
			rskXLPremiumDetails.setGnpiEndtNo(input[49]);
			rskXLPremiumDetails.setPremiumSubclass(input[50]);
			rskXLPremiumDetails.setPremiumClass(input[51]);
			rskXLPremiumDetails.setStatementDate(formatDate(input[52]));
			rskXLPremiumDetails.setProposalNo(format(input[53]));
			rskXLPremiumDetails.setProductId(format(input[54]));
			rskXLPremiumDetails.setReverselStatus(input[55]);
		}
		return rskXLPremiumDetails;
	}

	

}
