package com.maan.insurance.jpa.mapper;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;

@Component
public class TtrnRiskRemarksMapper extends AbstractEntityMapper<TtrnRiskRemarks, RemarksSaveReq>{

	@Override
	public RemarksSaveReq fromEntity(TtrnRiskRemarks input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnRiskRemarks toEntity(RemarksSaveReq input) throws ParseException {
		TtrnRiskRemarks ttrnRiskRemarks = null;
		if(input != null) {
			
			ttrnRiskRemarks = new TtrnRiskRemarks();
			ttrnRiskRemarks.setProposalNo(formatBigDecimal(input.getProposalNo()));
			ttrnRiskRemarks.setContractNo(formatBigDecimal(input.getContractNo()));
			ttrnRiskRemarks.setLayerNo(formatBigDecimal(input.getLayerNo()));
			ttrnRiskRemarks.setDeptId(input.getDepartmentId());
			ttrnRiskRemarks.setProductId(input.getProductid());
			ttrnRiskRemarks.setAmendId(null);
			ttrnRiskRemarks.setRskSNo(null);
			ttrnRiskRemarks.setRskDescription(null);
			ttrnRiskRemarks.setRskRemark1(null);
			ttrnRiskRemarks.setRskRemark2(null);
			ttrnRiskRemarks.setLoginId(input.getLoginId());
			ttrnRiskRemarks.setBranchCode(input.getBranchCode());
			ttrnRiskRemarks.setSysDate(getCurrentTimestamp());
			
		}
		return ttrnRiskRemarks;
	}

	

}
