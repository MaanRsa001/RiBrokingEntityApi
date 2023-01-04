package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.req.facultative.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;

@Component
public class TtrnMndInstallmentsMapper extends AbstractEntityMapper<TtrnMndInstallments, InstalMentPremiumReq> {

	@Override
	public InstalMentPremiumReq fromEntity(TtrnMndInstallments input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnMndInstallments toEntity(InstalMentPremiumReq input) throws ParseException {
		TtrnMndInstallments ttrnMndInstallments = null;
		if (input != null) {
			ttrnMndInstallments = new TtrnMndInstallments();
		}
		return ttrnMndInstallments;
	}
	//
	public TtrnMndInstallments toCustomEntity(InstalMentPremiumReq input, InstalmentperiodReq req)
			throws ParseException {
		TtrnMndInstallments ttrnMndInstallments = null;
		if (input != null) {
			ttrnMndInstallments = new TtrnMndInstallments();

			ttrnMndInstallments.setInstallmentNo(null);
			ttrnMndInstallments.setProposalNo(input.getProposalNo());
			ttrnMndInstallments.setContractNo(StringUtils.isEmpty(input.getContractNo()) ? "0" : input.getContractNo());
			ttrnMndInstallments.setLayerNo(formatBigDecimal("0"));
			ttrnMndInstallments.setEndorsementNo(null);
			ttrnMndInstallments.setInstallmentDate(
					formatDate(StringUtils.isEmpty(req.getInstalmentDateList()) ? "" : req.getInstalmentDateList()));
			ttrnMndInstallments.setMndPremiumOc(formatBigDecimal(StringUtils.isEmpty(req.getInstallmentPremium()) ? ""
					: req.getInstallmentPremium().replaceAll(",", "")));
			ttrnMndInstallments.setMndPremiumDc(formatBigDecimal(
					StringUtils.isEmpty(req.getInstallmentPremium()) || StringUtils.isEmpty(input.getUsCurrencyRate())
							? "0"
							: String.valueOf(Double.parseDouble(req.getInstallmentPremium().replaceAll(",", ""))
									/ Double.parseDouble(input.getUsCurrencyRate()))));
			ttrnMndInstallments.setStatus("Y");
			ttrnMndInstallments.setEntryDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnMndInstallments.setPayementDueDay((req.getPaymentDueDays() == null) ? ""
					: StringUtils.isEmpty(req.getPaymentDueDays()) ? "" : req.getPaymentDueDays());
			ttrnMndInstallments.setLoginId(input.getLoginid());
			ttrnMndInstallments.setBranchCode(input.getBranchCode());

		}
		return ttrnMndInstallments;
	}
}
