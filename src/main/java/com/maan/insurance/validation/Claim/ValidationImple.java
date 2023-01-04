package com.maan.insurance.validation.Claim;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.TreasuryServiceImpl;
@Service
public class ValidationImple {
	
	@Autowired
	private QueryImplemention queryImpl;
	private Logger log = LogManager.getLogger(TreasuryServiceImpl.class);
	public int Validatethree(String branchCode, String accountDate) {
		int result = 0;
		String[] args = null;
		try {
			String OpstartDate = "";
			String OpendDate = "";
			args = new String[1];
			args[0] = branchCode;
			List<Map<String, Object>> list = queryImpl.selectList("GET_OPEN_PERIOD_DATE", args);

			if (!CollectionUtils.isEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
					OpstartDate = tempMap.get("START_DATE") == null ? "" : tempMap.get("START_DATE").toString();
					OpendDate = tempMap.get("END_DATE") == null ? "" : tempMap.get("END_DATE").toString();

					args = new String[3];
					args[0] = accountDate;
					args[1] = OpstartDate;
					args[2] = OpendDate;
					List<Map<String, Object>> count = queryImpl.selectList("GET_OPEN_PERIOD_VALIDATE", args);
					if (!CollectionUtils.isEmpty(count)) {
						result = count.get(0).get("COUNT") == null ? 0
								: Integer.parseInt(count.get(0).get("COUNT").toString());
					}

					if (result > 0)
						break;
				}
			}

		} catch (Exception e) {
			log.debug("Exception @ {" + e + "}");
		}
		return result;
	}
}
