package com.maan.insurance.jpa.mapper;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.req.facultative.InsertCrestaMaintableReq;

@Component
public class TtrnCrestazoneDetailsMapper extends AbstractEntityMapper<TtrnCrestazoneDetails, InsertCrestaMaintableReq> {

	@Override
	public InsertCrestaMaintableReq fromEntity(TtrnCrestazoneDetails input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnCrestazoneDetails toEntity(InsertCrestaMaintableReq input) throws ParseException {
		TtrnCrestazoneDetails ttrnCrestazoneDetails = null;
		if (input != null) {
			ttrnCrestazoneDetails = new TtrnCrestazoneDetails();
			ttrnCrestazoneDetails.setContractNo(formatBigDecimal(input.getContractNo()));
			ttrnCrestazoneDetails.setProposalNo(formatBigDecimal(input.getProposalNo()));
			ttrnCrestazoneDetails
					.setAmendId(StringUtils.isEmpty(input.getEndorsmentno()) ? "0" : input.getEndorsmentno());
			ttrnCrestazoneDetails.setSubClass(input.getDepartmentId());
			ttrnCrestazoneDetails.setCrestaId("");
			ttrnCrestazoneDetails.setCrestaName("");
			ttrnCrestazoneDetails.setCurrency("");
			ttrnCrestazoneDetails.setAccRisk("");
			ttrnCrestazoneDetails.setAccumDate(null);
			ttrnCrestazoneDetails.setEntryDate(null);
			ttrnCrestazoneDetails.setStatus("");
			ttrnCrestazoneDetails.setBranchCode(input.getBranchCode());
			ttrnCrestazoneDetails.setTerritoryCode("");
			ttrnCrestazoneDetails.setSno("");

		}
		return ttrnCrestazoneDetails;
	}
}
