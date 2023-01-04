package com.maan.insurance.service.statistics;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.statistics.GetColumnHeaderlistReq;
import com.maan.insurance.model.req.statistics.GetRenewalStatisticsListReq;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.statistics.GetColumnHeaderlistRes;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.GetRenewalStatisticsListRes;
import com.maan.insurance.model.res.statistics.SlideScenarioRes;

@Service
public interface StatisticsService {

	SlideScenarioRes slideScenario(SlideScenarioReq req);

	GetCurrencyNameRes getCurrencyName(String proposalNo, String contractNo, String productId);

	GetColumnHeaderlistRes getColumnHeaderlist(GetColumnHeaderlistReq req);

	GetRenewalStatisticsListRes getRenewalStatisticsList(GetRenewalStatisticsListReq req);

	GetRenewalStatisticsListRes getRenewalCalStatisticsList(GetRenewalStatisticsListReq req);

}
