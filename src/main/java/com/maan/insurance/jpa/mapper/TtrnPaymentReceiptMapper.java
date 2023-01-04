package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.model.req.PaymentRecieptReq;

@Component
public class TtrnPaymentReceiptMapper extends AbstractEntityMapper<TtrnPaymentReceipt, PaymentRecieptReq>{

	@Override
	public PaymentRecieptReq fromEntity(TtrnPaymentReceipt input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnPaymentReceipt toEntity(PaymentRecieptReq input) throws ParseException {
		TtrnPaymentReceipt ttrnPaymentReceipt = null;
		if(input != null) {
			ttrnPaymentReceipt = new TtrnPaymentReceipt();
			ttrnPaymentReceipt.setPaymentReceiptNo(formatLong(input.getSerialno()));
			ttrnPaymentReceipt.setBrokerId(format(input.getBroker()));
			ttrnPaymentReceipt.setCedingId(format(input.getCedingCompany() == null ? "0" : input.getCedingCompany().trim()));
			ttrnPaymentReceipt.setCurrencyId(format(input.getCurrency()));
			ttrnPaymentReceipt.setPaidAmt(formatDouble(input.getPaymentAmount()));
			ttrnPaymentReceipt.setExchangeRate(formatDouble(input.getExchangeRate()));
			ttrnPaymentReceipt.setReceiptBank(format(input.getReceiptBankId()));
			ttrnPaymentReceipt.setProductId(format(input.getProductId()));
			ttrnPaymentReceipt.setDeptId(format(input.getDepartmentNo()));
			ttrnPaymentReceipt.setStatus("Y");
			ttrnPaymentReceipt.setTransDate(formatDate(input.getTransactionDate()));
			ttrnPaymentReceipt.setTransType(input.getTransType());
			ttrnPaymentReceipt.setBranchCode(input.getBranchCode());
			ttrnPaymentReceipt.setBankCharges(formatDouble(input.getBankCharges().replaceAll(",", "")));
			ttrnPaymentReceipt.setNetAmt(formatDouble(input.getNetAmount().replaceAll(",", "")));
			ttrnPaymentReceipt.setRemarks(input.getRemarks());
			ttrnPaymentReceipt.setWhTax(formatDouble(input.getWithHoldingTaxOC().replaceAll(",", "")));
			ttrnPaymentReceipt.setAmendId(0);
			ttrnPaymentReceipt.setLoginId(input.getLoginId());
			ttrnPaymentReceipt.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnPaymentReceipt.setPremiumLavy(formatDouble(input.getPremiumLavy()));
			ttrnPaymentReceipt.setTransactionType(input.getTransactionType());
			
		}
		return ttrnPaymentReceipt;
	}

}
