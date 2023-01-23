package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.claim.TtrnClaimAcc;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;

@Component
public class TtrnClaimAccMapper {

	public TtrnClaimAcc toEntity(InsertCliamDetailsMode2Req req) {
		TtrnClaimAcc ttrnClaimAcc = new TtrnClaimAcc();
		
		ttrnClaimAcc.setClaimNo(new BigDecimal(req.getClaimNo()));
		ttrnClaimAcc.setContractNo(req.getPolicyContractNo());
		ttrnClaimAcc.setLayerNo(StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()));
		ttrnClaimAcc.setSubClass(new BigDecimal(req.getSectionNo()));
		ttrnClaimAcc.setRiskCode(req.getRiskCode());
		ttrnClaimAcc.setAggregateCode(req.getAccumulationCode());
		ttrnClaimAcc.setEventCode(req.getEventCode());
		ttrnClaimAcc.setCreatedDate(new Date());
		ttrnClaimAcc.setBranchCode(req.getBranchCode());
		ttrnClaimAcc.setLoginId(req.getLoginId());
		
		return ttrnClaimAcc;
	}

}
