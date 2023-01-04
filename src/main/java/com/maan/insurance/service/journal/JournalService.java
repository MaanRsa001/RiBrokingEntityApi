package com.maan.insurance.service.journal;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.journal.GetEndDateStatusReq;
import com.maan.insurance.model.req.journal.GetJournalViewsReq;
import com.maan.insurance.model.req.journal.GetLedgerEntryListReq;
import com.maan.insurance.model.req.journal.GetQuaterEndValidationReq;
import com.maan.insurance.model.req.journal.GetSpcCurrencyListReq;
import com.maan.insurance.model.req.journal.GetStartDateStatusReq;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.journal.InsertManualJVReq;
import com.maan.insurance.model.req.journal.InsertRetroProcessReq;
import com.maan.insurance.model.res.journal.GetEndDateListRes;
import com.maan.insurance.model.res.journal.GetJournalViewsRes;
import com.maan.insurance.model.res.journal.GetLedgerEntryListRes;
import com.maan.insurance.model.res.journal.GetOpenPeriodListRes;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes;
import com.maan.insurance.model.res.journal.GetStartDateListRes;
import com.maan.insurance.model.res.journal.GetUserDetailsRes;
import com.maan.insurance.model.res.journal.GetViewLedgerDetailsRes;
import com.maan.insurance.model.res.journal.GetjounalListRes;
import com.maan.insurance.model.res.journal.InsertManualJVRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface JournalService {

	GetjounalListRes getjounalList();

	GetOpenPeriodListRes getOpenPeriodList(String branchCode) ;

	GetStartDateListRes getStartDateList(String branchCode);

	GetEndDateListRes getEndDateList(String branchCode);

	CommonSaveRes getForExDiffName(String branchCode);

	CommonSaveRes insertInActiveOpenPeriod(InsertInActiveOpenPeriodReq req);

	CommonSaveRes insertActiveOpenPeriod(InsertInActiveOpenPeriodReq req);

	CommonSaveRes getShortname(String branchCode);

	GetSpcCurrencyListRes getSpcCurrencyList(GetSpcCurrencyListReq req);

	GetJournalViewsRes getJournalViews(GetSpcCurrencyListReq req);

	CommonSaveRes getCountOpenPeriod(String branchCode, String sNo);

	GetUserDetailsRes getUserDetails(String branchCode, List<String>  loginId);


	CommonSaveRes getQuaterEndValidation(GetQuaterEndValidationReq req);

	CommonResponse activateInActivateLoginUsers(String branchCode, String loginId, String status);

	CommonSaveRes insertRetroProcess(InsertRetroProcessReq req);

	GetLedgerEntryListRes getLedgerEntryList(GetLedgerEntryListReq req);

	CommonResponse insertManualJV(InsertManualJVReq req);

	GetViewLedgerDetailsRes getViewLedgerDetails(String branchCode, String transId, String reversalStatus);

	GetViewLedgerDetailsRes getEditLedgerDetails(String branchCode, String transId, String mode);

	CommonSaveRes getStartDateStatus(GetStartDateStatusReq req);

	CommonSaveRes getEndDateStatus(GetEndDateStatusReq req);

}
