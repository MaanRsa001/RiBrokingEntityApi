package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.model.req.GenerationReq;

@Component
public class TtrnPaymentReceiptDetailsMapper extends AbstractEntityMapper<TtrnPaymentReceiptDetails, GenerationReq>{

	@Override
	public GenerationReq fromEntity(TtrnPaymentReceiptDetails input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnPaymentReceiptDetails toEntity(GenerationReq input) throws ParseException {
		TtrnPaymentReceiptDetails ttrnPaymentReceiptDetails = null;
		if(input != null) {
			ttrnPaymentReceiptDetails = new TtrnPaymentReceiptDetails();
			ttrnPaymentReceiptDetails.setCurrencyId(new BigDecimal(StringUtils.isEmpty(input.getCurrencyValList()) ? "0" :input.getCurrencyValList()));
			ttrnPaymentReceiptDetails.setAmount(new BigDecimal(StringUtils.isEmpty(input.getPayamountValList()) ? "0" :input.getPayamountValList().trim().replaceAll(",", "")));
			ttrnPaymentReceiptDetails.setExchangeRate(new BigDecimal(StringUtils.isEmpty(input.getExachangeValList()) ? "0" :input.getExachangeValList().replaceAll(",", "")));
			ttrnPaymentReceiptDetails.setStatus("Y");
			ttrnPaymentReceiptDetails.setTotAmt(new BigDecimal(StringUtils.isEmpty(input.getRowamountValList()) ? "0" :input.getRowamountValList().replaceAll(",", "")));
			ttrnPaymentReceiptDetails.setSettledExcrate(new BigDecimal(StringUtils.isEmpty(input.getSetExcRateValList()) ? "0" :input.getSetExcRateValList().replaceAll(",", "")));
			ttrnPaymentReceiptDetails.setConvertedReccur(new BigDecimal(StringUtils.isEmpty(input.getConRecCurValList()) ? "0" :input.getConRecCurValList().replaceAll(",", "")));
		}
		return ttrnPaymentReceiptDetails;
	}

	

}
