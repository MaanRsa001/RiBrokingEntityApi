package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.req.facultative.MoveBonusReq;
import com.maan.insurance.model.req.nonproportionality.BonusReq;

@Component
public class TtrnBonusMapper extends AbstractEntityMapper<TtrnBonus, MoveBonusReq> {

	@Override
	public MoveBonusReq fromEntity(TtrnBonus input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnBonus toEntity(MoveBonusReq input) throws ParseException {
		TtrnBonus ttrnBonus = null;
		if (input != null) {
			ttrnBonus = new TtrnBonus();
		}
		return ttrnBonus;
	}

	public TtrnBonus toCustomEntity(MoveBonusReq bean, BonusReq req) {

		TtrnBonus ttrnBonus = new TtrnBonus();
		ttrnBonus.setProposalNo(formatBigDecimal(bean.getProposalNo()));
		ttrnBonus.setContractNo(formatBigDecimal(bean.getContractNo()));
		ttrnBonus.setProductId(bean.getProductId());
		ttrnBonus.setLcbType(bean.getBonusTypeId());
		ttrnBonus.setLcbFrom(req.getBonusFrom().replace(",", ""));
		ttrnBonus.setLcbTo(req.getBonusTo().replace(",", ""));
		ttrnBonus.setLcbPercentage(req.getBonusLowClaimBonus().replace(",", ""));
		ttrnBonus.setUserId(bean.getLoginid());
		ttrnBonus.setBranch(bean.getBranchCode());
		ttrnBonus.setLcbId(req.getBonusSNo().replace(",", ""));
		ttrnBonus.setType(bean.getAcqBonus());
		ttrnBonus.setEndorsementNo(formatBigDecimal(bean.getEndorsmentno()));
		ttrnBonus.setSubClass(bean.getDepartmentId());
		ttrnBonus.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		ttrnBonus.setLayerNo("0");

		return ttrnBonus;
	}

	public TtrnBonus toBonusCustomEntity(com.maan.insurance.model.req.facultative.InsertBonusDetailsReq bean) {

		TtrnBonus ttrnBonus = new TtrnBonus();
		ttrnBonus.setProposalNo(formatBigDecimal(bean.getProposalNo()));
		ttrnBonus.setContractNo(formatBigDecimal(bean.getContractNo()));
		ttrnBonus.setProductId(bean.getProductId());
		ttrnBonus.setLcbType("");
		ttrnBonus.setLcbFrom("");
		ttrnBonus.setLcbTo("");
		ttrnBonus.setLcbPercentage("");
		ttrnBonus.setUserId(bean.getLoginid());
		ttrnBonus.setBranch(bean.getBranchCode());
		ttrnBonus.setLcbId("1");
		ttrnBonus.setType(bean.getAcqBonus());
		ttrnBonus.setEndorsementNo(formatBigDecimal(bean.getEndorsmentno()));
		ttrnBonus.setSubClass(bean.getDepartmentId());
		ttrnBonus.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		ttrnBonus.setLayerNo("0");

		return ttrnBonus;
	}

}
