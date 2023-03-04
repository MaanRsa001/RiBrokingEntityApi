package com.maan.insurance.jpa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.xolpremium.RskXLPremiumDetails;
import com.maan.insurance.jpa.mapper.RskPremiumDetailsMapper;
import com.maan.insurance.jpa.mapper.RskPremiumDetailsTempMapper;
import com.maan.insurance.jpa.mapper.RskXLPremiumDetailsMapper;
import com.maan.insurance.jpa.repository.xolpremium.RskXLPremiumDetailsRepository;
import com.maan.insurance.jpa.repository.xolpremium.XolPremiumCustomRepository;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.RskPremiumDetailsTempRepository;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.MdInstallmentDatesReq;
import com.maan.insurance.model.req.xolPremium.PremiumEditReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes1;
import com.maan.insurance.model.res.facPremium.GetAllocatedListResponse;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes1;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes1;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes1;
import com.maan.insurance.model.res.xolPremium.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes1;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes1;
import com.maan.insurance.model.res.xolPremium.GetPreListRes;
import com.maan.insurance.model.res.xolPremium.GetPreListRes1;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes1;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes1;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes1;
import com.maan.insurance.model.res.xolPremium.PremiumEditResponse;
import com.maan.insurance.model.res.xolPremium.PremiumInsertRes;
import com.maan.insurance.model.res.xolPremium.premiumInsertMethodRes;
import com.maan.insurance.service.XolPremium.XolPremiumService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;

@Component
public class XolPremiumJpaServiceImpl implements XolPremiumService{
	@Autowired
	private QueryImplemention queryImpl;
	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	XolPremiumCustomRepository xolPremiumCustomRepository;
	
	@Autowired
	RskPremiumDetailsTempMapper rskPremiumDetailsTempMapper;
	
	@Autowired
	RskXLPremiumDetailsMapper rskXLPremiumDetailsMapper;
	
	@Autowired
	RskPremiumDetailsMapper rskPremiumDetailsMapper;
	
	@Autowired
	RskXLPremiumDetailsRepository rskXLPremiumDetailsRepository;
	
	@Autowired
	RskPremiumDetailsTempRepository rskPremiumDetailsTempRepository;
	
	@Autowired
	RskPremiumDetailsRepository rskPremiumDetailsRepository;
	
	@PersistenceContext
	private EntityManager em;
	private Query query=null;
	
	/*public CommonSaveRes getRPPremiumOC(String contractNo, String layerNo, String productId) {
		CommonSaveRes response = new CommonSaveRes();
		String premiumOC = "";
		try {
			if ("3".equalsIgnoreCase(productId))
				// query -- premium.select.RPPremiumOC
				premiumOC = xolPremiumCustomRepository.selectRPPremiumOC(contractNo, layerNo);
			else
				// query -- XOL_PREMIUM_SELECT_RPPREMIUMOC
				premiumOC = xolPremiumCustomRepository.xolSelectRPPremiumOC(contractNo, layerNo);

			premiumOC = premiumOC == null ? "" : premiumOC;
			response.setResponse(premiumOC);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public GetSPRetroListRes getSPRetroList(String contNo) {
		GetSPRetroListRes response = new GetSPRetroListRes();
		GetSPRetroListRes2 res = new GetSPRetroListRes2();

		List<Tuple> list = new ArrayList<>();
		try {
			// query -- premium.select.getTreatySPRetro
			list = xolPremiumCustomRepository.selectGetTreatySPRetro(contNo);
			for (int x = 0; x < list.size(); x++) {
				Tuple resMap = list.get(0);
				if (resMap != null) {
					res.setSpretro(resMap.get("RSK_SP_RETRO") == null ? "" : resMap.get("RSK_SP_RETRO").toString());
					res.setNoofinsurers(resMap.get("RSK_NO_OF_INSURERS") == null ? ""
							: resMap.get("RSK_NO_OF_INSURERS").toString());
					res.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER") == null ? ""
							: resMap.get("RSK_PROPOSAL_NUMBER").toString());
				}
			} // Query For List
			response.setResponse(res);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}

	public GetRetroContractsRes getRetroContracts(GetRetroContractsReq req) {
		GetRetroContractsRes response = new GetRetroContractsRes();
		List<GetRetroContractsRes1> relist = new ArrayList<GetRetroContractsRes1>();
		List<Tuple> list = new ArrayList<>();

		try {
			// query -- premium.select.insDetails
			list = xolPremiumCustomRepository.selectInsDetails(req.getProposalNo(),
					Integer.parseInt(req.getNoOfRetro()));
			if (list.size() > 0 && list != null) {
				for (int r = 0; r < list.size(); r++) {
					GetRetroContractsRes1 res = new GetRetroContractsRes1();
					Tuple resMap = list.get(r);
					res.setContractNo(resMap.get("CONTRACT_NO") == null ? "" : resMap.get("CONTRACT_NO").toString());
					res.setRetroPercentage(
							resMap.get("RETRO_PERCENTAGE") == null ? "" : resMap.get("RETRO_PERCENTAGE").toString());
					res.setType(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString());
					res.setUwyear(resMap.get("UW_YEAR") == null ? "" : resMap.get("UW_YEAR").toString());
					res.setRetroType(resMap.get("RETRO_TYPE") == null ? "" : resMap.get("RETRO_TYPE").toString());
					relist.add(res);
				}
			}
			response.setCommonResponse(relist);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}

	public GetSumOfShareSignRes getSumOfShareSign(String contractNo) {
		GetSumOfShareSignRes response = new GetSumOfShareSignRes();
		int shareSign = 0;
		String sumOfShareSign = "";
		String output = null;
		try {
			// query -- premium.select.getNoRetroCess
			output = xolPremiumCustomRepository.selectGetNoRetroCess(contractNo);
			shareSign = Integer.valueOf(output == null ? "" : output) - 1;

			// query -- premium.select.getSumOfShareSign
			sumOfShareSign = xolPremiumCustomRepository.selectGetSumOfShareSign(contractNo, shareSign);
			sumOfShareSign = sumOfShareSign == null ? "" : sumOfShareSign;
			response.setCommonResponse(sumOfShareSign);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}*/
	
	private String formatDate(Object input) {
		return new SimpleDateFormat("dd/MM/yyyy").format(input).toString();
	}
	
	public GetPremiumedListRes getPremiumedList(GetPremiumedListReq beanObj) {
		GetPremiumedListRes response = new GetPremiumedListRes();
		List<GetPremiumedListRes1> finalList = new ArrayList<GetPremiumedListRes1>();
		int allocationstatus = 0;
		int retroPrclStatus = 0;
		List<Tuple> list = null;
		String output = null;
		try {

			if ("Main".equalsIgnoreCase(beanObj.getType())) {
				if ("3".equalsIgnoreCase(beanObj.getProductId())) {
					// query -- premium.select.PremiumedList1_added
					list = xolPremiumCustomRepository.selectPremiumedList1(beanObj);
				} else {
					// query -- premium.select.retroPremiumedList1_added
					list = xolPremiumCustomRepository.selectRetroPremiumedList1(beanObj);
				}
			} else {
				// query -- XOL_PREMIUM_LIST_TEMP
				list = xolPremiumCustomRepository.xolPremiumListTemp(beanObj);

			}

			if (list.size() > 0 && list != null)
				for (int i = 0; i < list.size(); i++) {
					Tuple tempMap = list.get(i);
					GetPremiumedListRes1 tempBean = new GetPremiumedListRes1();
					tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER") == null ? "": tempMap.get("RSK_PROPOSAL_NUMBER").toString());
					tempBean.setContNo(tempMap.get("RSK_CONTRACT_NO") == null ? "" : tempMap.get("RSK_CONTRACT_NO").toString());
					tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
					tempBean.setBroker(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
					tempBean.setLayerno(tempMap.get("RSK_LAYER_NO") == null ? "" : tempMap.get("RSK_LAYER_NO").toString());
					tempBean.setAccountPeriod(tempMap.get("ACC_PER") == null ? "" : tempMap.get("ACC_PER").toString());
					tempBean.setAccountPeriod(tempMap.get("INS_DETAIL") == null ? "" : tempMap.get("INS_DETAIL").toString());
					tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO") == null ? "": tempMap.get("REVERSE_TRANSACTION_NO").toString());
					if ("Main".equalsIgnoreCase(beanObj.getType())) {
						tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO") == null ? "" : tempMap.get("TRANSACTION_NO").toString());
					}else {
						tempBean.setRequestNo(tempMap.get("REQUEST_NO") == null ? "" : tempMap.get("REQUEST_NO").toString());
					}
					if (i == 0)
						tempBean.setEndtYN("No");
					else
						tempBean.setEndtYN("Yes");
					if (Double.parseDouble(tempMap.get("ALLOC_AMT").toString()) != 0)
						tempBean.setEndtYN("Yes");
					tempBean.setInceptionDate(tempMap.get("INS_DATE") == null ? "" : formatDate(tempMap.get("INS_DATE")));
					tempBean.setStatementDate(tempMap.get("STATEMENT_DATE") == null ? "" : formatDate(tempMap.get("STATEMENT_DATE")));
					tempBean.setMovementYN(tempMap.get("MOVEMENT_YN") == null ? "" : tempMap.get("MOVEMENT_YN").toString());
					tempBean.setTransDate(tempMap.get("TRANSACTION_DATE") == null ? "" : formatDate(tempMap.get("TRANSACTION_DATE")));
					if ((StringUtils.isNotBlank(beanObj.getOpstartDate()))
							&& (StringUtils.isNotBlank(beanObj.getOpendDate()))) {
						if (dropDowmImpl.Validatethree(beanObj.getBranchCode(), tempBean.getTransDate()) == 0) {
							tempBean.setTransOpenperiodStatus("N");
						} else {
							tempBean.setTransOpenperiodStatus("Y");
						}
					}
					// query -- premium.select.allocatedYN
					output = xolPremiumCustomRepository.selectAllocatedYN(tempBean.getContNo(),
							tempBean.getTransactionNo(), tempBean.getLayerno());
					tempBean.setAllocatedYN(output == null ? "" : output);
					tempBean.setProductId("3");
					int count = dropDowmImpl.Validatethree(beanObj.getBranchCode(), tempBean.getTransDate());
					// query -- allocation.status
					output = xolPremiumCustomRepository.getAllocationStatus(tempBean.getTransactionNo());
					allocationstatus = Integer.valueOf(output == null ? "" : output);

					// query -- retro.status
					output = xolPremiumCustomRepository.getRetroStatus(tempBean.getTransactionNo());
					retroPrclStatus = Integer.valueOf(output == null ? "" : output);

					int retroPrclStatus1 = 0;
					if (retroPrclStatus != 0) {

						// query -- retro.status1
						output = xolPremiumCustomRepository.getRetroStatus1(tempBean.getTransactionNo());
						retroPrclStatus1 = Integer.valueOf(output == null ? "" : output);
					}

					if (count != 0 && allocationstatus == 0 && retroPrclStatus1 == 0) {
						tempBean.setDeleteStatus("Y");
					}
					finalList.add(tempBean);
				}
			response.setCommonResponse(finalList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonSaveRes getContractPremium(String contractNo, String layerNo) {
		CommonSaveRes response = new CommonSaveRes();
		String premium = "";
		try {
			// query -- SELECT_RSK_SUBJ_PREMIUM_OC
			premium = xolPremiumCustomRepository.selectRskSubjPremiumOc(contractNo, layerNo);
			premium = premium == null ? "" : premium;
			response.setResponse(premium);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public CommonSaveRes getPreviousPremium(String contractNo) {
		CommonSaveRes response = new CommonSaveRes();
		String premium = "";
		try {
			int count=rskPremiumDetailsRepository.countByContractNo(new BigDecimal(contractNo));
			// query -- select_Previous_premium
			if(count>0) {
				premium = xolPremiumCustomRepository.selectPreviousPremium(contractNo);
			}
			premium = premium == null ? "" : premium;
			response.setResponse(premium);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	
	public MdInstallmentDatesRes mdInstallmentDates(MdInstallmentDatesReq req) {
		MdInstallmentDatesRes response = new MdInstallmentDatesRes();
		List<MdInstallmentDatesRes1> resList = new ArrayList<MdInstallmentDatesRes1>();
		List<Tuple> result = new ArrayList<>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {

			if ("3".equalsIgnoreCase(req.getProductId())) //mode:transedit----instal no 1
				// query -- PREMIUM_MND_INS_LIST
				result = xolPremiumCustomRepository.getPremiumMndInsList(req.getContNo(), req.getLayerno(), req.getMode());
			else
				// query -- premium.select.mdInstalmentList
				result = xolPremiumCustomRepository.selectmdInstalmentList(req.getContNo(), req.getLayerno());

			for (Tuple element : result) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("KEY1", element.get("INSTALLMENT_NO") + "_"
						+ new SimpleDateFormat("dd/MM/yyyy").format(element.get("INSTALLMENT_DATE")).toString());
				tempMap.put("VALUE", element.get("INSTALLMENT_NO") + ":"
						+ new SimpleDateFormat("dd/MM/yyyy").format(element.get("INSTALLMENT_DATE")).toString());
				tempMap.put("INSTALLMENT_NO", element.get("INSTALLMENT_NO"));
				list.add(tempMap);
				
			}
			
			Map<String, Object> tempMap1 = new HashMap<String, Object>();
			Map<String, Object> tempMap2 = new HashMap<String, Object>();
			Map<String, Object> tempMap3 = new HashMap<String, Object>();
			Map<String, Object> tempMap4 = new HashMap<String, Object>();
			if (list.size() == 0) {
				String count = "";
				if ("RI02".equalsIgnoreCase(req.getSourceId())) {
					// query -- GET_GNPI_COUNT_PRE
					count = xolPremiumCustomRepository.getGnpiCountPre(req.getContNo(), req.getLayerno(), "GNPI");
					count = count == null ? "" : count;
				}
				tempMap1.put("KEY1", "RP");
				tempMap1.put("VALUE", "Reinstatement Premium");
				list.add(tempMap1);
				if ("RI02".equalsIgnoreCase(req.getSourceId()) && !"0".equalsIgnoreCase(count)) {
					tempMap2.put("KEY1", "AP");
					tempMap2.put("VALUE", "Adjustment Premium");
					list.add(tempMap2);
				} else if ("RI01".equalsIgnoreCase(req.getSourceId())) {
					tempMap2.put("KEY1", "AP");
					tempMap2.put("VALUE", "Adjustment Premium");
					list.add(tempMap2);
				}
				tempMap3.put("KEY1", "RTP");
				tempMap3.put("VALUE", "Return Premium");
				list.add(tempMap3);
				//tempMap4.put("KEY1", "RVP");
				//tempMap4.put("VALUE", "Reversal Premium");
				//list.add(tempMap4);
			} else {
				tempMap1.put("KEY1", "RP");
				tempMap1.put("VALUE", "Reinstatement Premium");
				list.add(tempMap1);
				tempMap3.put("KEY1", "RTP");
				tempMap3.put("VALUE", "Return Premium");
				list.add(tempMap3);
				//tempMap4.put("KEY1", "RVP");
				//tempMap4.put("VALUE", "Reversal Premium");
				//list.add(tempMap4);
			}
			if (list.size() > 0)
				for (int i = 0; i < list.size(); i++) {
					MdInstallmentDatesRes1 res = new MdInstallmentDatesRes1();
					Map<String, Object> map = (Map<String, Object>) list.get(i);
					res.setKey1(map.get("KEY1") == null ? "" : map.get("KEY1").toString());
					res.setValue(map.get("VALUE") == null ? "" : map.get("VALUE").toString());
					res.setInstallmentNo(map.get("INSTALLMENT_NO") == null ? "" : map.get("INSTALLMENT_NO").toString());
					resList.add(res);
				}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetPreListRes getPreList(String contNo, String layerNo) {
		GetPreListRes response = new GetPreListRes();
		GetPreListRes1 bean = new GetPreListRes1();
		String ceaseStatus = null;
		try {
			// query -- premium.select.xolPreList
			List<Tuple> list = xolPremiumCustomRepository.selectXolPreList(contNo, layerNo);
			if (list != null && list.size() > 0) {
				Tuple preList = list.get(0);
				bean.setContNo(preList.get("CONTRACT_NO") == null ? "" : preList.get("CONTRACT_NO").toString());
				bean.setDepartmentName(preList.get("TMAS_DEPARTMENT_NAME") == null ? "": preList.get("TMAS_DEPARTMENT_NAME").toString());
				bean.setUwYear(preList.get("UW_YEAR") == null ? "" : preList.get("UW_YEAR").toString());
				bean.setCedingCompanyName(preList.get("COMPANY_NAME") == null ? "" : preList.get("COMPANY_NAME").toString());
				bean.setLayerno(preList.get("LAYER_NO") == null ? "" : preList.get("LAYER_NO").toString());
				bean.setBrokername(preList.get("First_name") == null ? "" : preList.get("First_name").toString());
			}
			bean.setSaveFlag("true");
			if (StringUtils.isNotBlank(contNo)) {
				// query -- select_CEASE_STATUS
				ceaseStatus = xolPremiumCustomRepository.selectCeaseStatus(contNo, layerNo);
				bean.setCeaseStatus(ceaseStatus == null ? "" : ceaseStatus);
			}
			response.setCommonResponse(bean);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetDepartmentIdRes getDepartmentId(String contNo, String productId) {
		GetDepartmentIdRes response = new GetDepartmentIdRes();
		GetDepartmentIdRes1 res = new GetDepartmentIdRes1();
		try {
			// query -- SELECT_DEPT_ID
			List<Tuple> list = xolPremiumCustomRepository.selectDeptId(contNo, productId);
			if (list != null && list.size() > 0) {
				Tuple map = list.get(0);
				res.setDepartmentId(map.get("DEPT_ID") == null ? "" : map.get("DEPT_ID").toString());
				res.setProposalNo(map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString());
			}
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonSaveRes getInstalmentAmount(String contNo, String layerNo, String instalmentno) {
		CommonSaveRes response = new CommonSaveRes();
		String string = null;
		try {
			// query -- premium.select.mndPremiumOC
			final String[] Instalmentno=instalmentno.split("_");
			string = xolPremiumCustomRepository.selectMndPremiumOC(contNo, layerNo, Instalmentno[0]);
			string = string == null ? "" : string;
			response.setResponse(string);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public GetBrokerAndCedingNameRes getBrokerAndCedingName(String contNo, String branchCode) {
		GetBrokerAndCedingNameRes response = new GetBrokerAndCedingNameRes();
		List<GetBrokerAndCedingNameRes1> resList = new ArrayList<GetBrokerAndCedingNameRes1>();
		try {
			// query -- broker.ceding.name
			List<Tuple> list = xolPremiumCustomRepository.getBrokerCedingName(contNo, branchCode);
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				GetBrokerAndCedingNameRes1 temp = new GetBrokerAndCedingNameRes1();
				temp.setCutomerId(tempMap.get("CUSTOMER_ID") == null ? "" : tempMap.get("CUSTOMER_ID").toString());
				temp.setCompanyName(tempMap.get("BROKER") == null ? "" : tempMap.get("BROKER").toString());
				temp.setAddress(tempMap.get("Address") == null ? "" : tempMap.get("Address").toString());
				resList.add(temp);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetAllocatedListRes getAllocatedList(String contNo, String transactionNo) {
		GetAllocatedListRes response = new GetAllocatedListRes();
		GetAllocatedListResponse res1 = new GetAllocatedListResponse();
		double a = 0;
		List<GetAllocatedListRes1> resList = new ArrayList<GetAllocatedListRes1>();
		try {
			// query -- payment.select.getAlloTransaction
			List<Tuple> list = xolPremiumCustomRepository.selectGetAlloTransaction(contNo, transactionNo);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Tuple tempMap = list.get(i);
					GetAllocatedListRes1 tempBean = new GetAllocatedListRes1();
					tempBean.setSerialno(tempMap.get("SNO") == null ? "" : tempMap.get("SNO").toString());
					tempBean.setAllocateddate(tempMap.get("INCEPTION_DATE") == null ? ""
							: new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get("INCEPTION_DATE")).toString());
					tempBean.setProductname(
							tempMap.get("PRODUCT_NAME") == null ? "" : tempMap.get("PRODUCT_NAME").toString());
					tempBean.setType(tempMap.get("TYPE") == null ? "" : tempMap.get("TYPE").toString());
					tempBean.setPayamount(
							tempMap.get("PAID_AMOUNT") == null ? "" : tempMap.get("PAID_AMOUNT").toString());
					tempBean.setCurrencyValue(
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempBean.setAlloccurrencyid(
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempBean.setStatus(
							("R".equals(tempMap.get("STATUS") == null ? "" : tempMap.get("STATUS").toString())
									? "Reverted"
									: "Allocated"));
					tempBean.setPayrecno(tempMap.get("RECEIPT_NO") == null ? "" : tempMap.get("RECEIPT_NO").toString());
					tempBean.setSettlementType(
							tempMap.get("TRANS_TYPE") == null ? "" : tempMap.get("TRANS_TYPE").toString());
					if (tempBean.getSettlementType().equalsIgnoreCase("PAYMENT")
							|| tempBean.getSettlementType().equalsIgnoreCase("RECEIPT")) {
						tempBean.setAllocateType(
								tempMap.get("ALLOCATE_TYPE") == null ? "" : tempMap.get("ALLOCATE_TYPE").toString());
					}
					resList.add(tempBean);
					a = a + Double.parseDouble(
							tempMap.get("PAID_AMOUNT") == null ? "" : tempMap.get("PAID_AMOUNT").toString());
				}
				res1.setAllocatedList(resList);
				if (a > 0) {
					res1.setTotalAmount(fm.formatter(Double.toString(a)));
				} else {
					res1.setTotalAmount("");
				}
				response.setCommonResponse(res1);
			}
			
			
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	@SuppressWarnings("static-access")
	public PremiumEditRes premiumEdit(PremiumEditReq req) {
		PremiumEditRes response = new PremiumEditRes();
		PremiumEditResponse res1 = new PremiumEditResponse();
		 List<PremiumEditRes1> resList = new ArrayList<PremiumEditRes1>();
		 List<Tuple> transList=new ArrayList<>();
		 try {
			 String input = null;
			 	if( "transEdit".equalsIgnoreCase(req.getMode())) {
					input = req.getTransDropDownVal();
					transList = xolPremiumCustomRepository.selectTreetyXOLPremiumEdit(req.getContNo(),input);
			 	}else if("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId())){
					input = req.getRequestNo();
					transList = xolPremiumCustomRepository.getXolPremiumEditRTemp(req.getContNo(), input);
				}else{
					input = req.getTransactionNo();					
					if("3".equalsIgnoreCase(req.getProductId())){
						transList = xolPremiumCustomRepository.selectTreetyXOLPremiumEdit(req.getContNo(),input);
					}
					else{
						transList = xolPremiumCustomRepository.selectRetrotreetyXOLPremiumEdit(req.getContNo(),input);
					}
				}
				if( "transEdit".equalsIgnoreCase(req.getMode())){
					 if(transList.size()>0)
					 {
						 for(int i=0;i<transList.size();i++){
							 PremiumEditRes1 bean = new PremiumEditRes1();
							 Tuple	editPremium=transList.get(i);
							 	bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
								bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
								bean.setAccountPeriod(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString());
								if("RP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())||"AP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
						    	{
						    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
						    	}else
						    	{
						    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")+"_"+editPremium.get("ACCOUNT_PERIOD_QTR"));
						    	}
								if(null==editPremium.get("EXCHANGE_RATE")){
									GetCommonValueRes common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
									bean.setExchRate(common.getCommonResponse());
								}
								else{
								bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString()));
								}
								bean.setBrokerage((editPremium.get("BROKERAGE_AMT_OC")==null?"":editPremium.get("BROKERAGE_AMT_OC").toString()));
								bean.setBrokerage((getMultipleVal(bean.getBrokerage())));
								bean.setTax((editPremium.get("TAX_AMT_OC")==null?"":editPremium.get("TAX_AMT_OC").toString()));
								bean.setTax((getMultipleVal(bean.getTax())));
								bean.setPremiumQuotaShare(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString());
								bean.setPremiumQuotaShare((getMultipleVal(bean.getPremiumQuotaShare())));
								bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
								bean.setCommissionQuotaShare((getMultipleVal(bean.getCommissionQuotaShare())));
								bean.setPremiumSurplus(editPremium.get("PREMIUM_SURPLUS_OC")==null?"":editPremium.get("PREMIUM_SURPLUS_OC").toString());
								bean.setPremiumSurplus((getMultipleVal(bean.getPremiumSurplus())));
								bean.setCommissionSurplus(editPremium.get("COMMISSION_SURPLUS_OC")==null?"":editPremium.get("COMMISSION_SURPLUS_OC").toString());
								bean.setCommissionSurplus((getMultipleVal(bean.getCommissionSurplus())));
								bean.setPremiumportifolioIn(editPremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
								bean.setPremiumportifolioIn((getMultipleVal(bean.getPremiumportifolioIn())));
								bean.setCliamPortfolioin(editPremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editPremium.get("CLAIM_PORTFOLIOIN_OC").toString());
								bean.setCliamPortfolioin((getMultipleVal(bean.getCliamPortfolioin())));
								bean.setPremiumportifolioout(editPremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
								bean.setPremiumportifolioout((getMultipleVal(bean.getPremiumportifolioout())));
								bean.setLossReserveReleased(editPremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editPremium.get("LOSS_RESERVE_RELEASED_OC").toString());
								bean.setLossReserveReleased((getMultipleVal(bean.getLossReserveReleased())));
								bean.setPremiumReserveQuotaShare(editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
								bean.setPremiumReserveQuotaShare((getMultipleVal(bean.getPremiumReserveQuotaShare())));
								bean.setCashLossCredit(editPremium.get("CASH_LOSS_CREDIT_OC")==null?"":editPremium.get("CASH_LOSS_CREDIT_OC").toString());
								bean.setCashLossCredit((getMultipleVal(bean.getCashLossCredit())));
								bean.setLossReserveRetained(editPremium.get("LOSS_RESERVERETAINED_OC")==null?"":editPremium.get("LOSS_RESERVERETAINED_OC").toString());
								bean.setLossReserveRetained((getMultipleVal(bean.getLossReserveRetained())));
								bean.setProfitCommission(editPremium.get("PROFIT_COMMISSION_OC")==null?"":editPremium.get("PROFIT_COMMISSION_OC").toString());
								bean.setProfitCommission((getMultipleVal(bean.getProfitCommission())));
								bean.setCashLossPaid(editPremium.get("CASH_LOSSPAID_OC")==null?"":editPremium.get("CASH_LOSSPAID_OC").toString());
								bean.setCashLossPaid((getMultipleVal(bean.getCashLossPaid())));
								bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
								bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
								bean.setNetDue((getMultipleVal(bean.getNetDue())));
								bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());
								bean.setClaimspaid(editPremium.get("CLAIMS_PAID_OC")==null?"":editPremium.get("CLAIMS_PAID_OC").toString());	
								bean.setClaimspaid((getMultipleVal(bean.getClaimspaid())));
							    bean.setMdpremium(fm.formatter(editPremium.get("M_DPREMIUM_OC")==null?"":editPremium.get("M_DPREMIUM_OC").toString()));
							    bean.setMdpremium((getMultipleVal(bean.getMdpremium())));
							    bean.setAdjustmentpremium(editPremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editPremium.get("ADJUSTMENT_PREMIUM_OC").toString());
							    bean.setAdjustmentpremium((getMultipleVal(bean.getAdjustmentpremium())));
							    bean.setRecuirementpremium(editPremium.get("REC_PREMIUM_OC")==null?"":editPremium.get("REC_PREMIUM_OC").toString());
							    bean.setRecuirementpremium((getMultipleVal(bean.getRecuirementpremium())));
							    bean.setCommission(editPremium.get("COMMISSION")==null?"":editPremium.get("COMMISSION").toString());
							    bean.setCommission((getMultipleVal(bean.getCommission())));
							    bean.setXlCost(editPremium.get("XL_COST_OC")==null?"":editPremium.get("XL_COST_OC").toString());
							    bean.setXlCost((getMultipleVal(bean.getXlCost())));
							    bean.setCliamportfolioout(editPremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editPremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
							    bean.setCliamportfolioout((getMultipleVal(bean.getCliamportfolioout())));
							    bean.setPremiumReserveReleased(editPremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editPremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
							    bean.setPremiumReserveReleased((getMultipleVal(bean.getPremiumReserveReleased())));
							    bean.setOtherCost((editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString()));
							    bean.setOtherCost((getMultipleVal(bean.getOtherCost())));
								bean.setInterest((editPremium.get("INTEREST_OC")==null?"":editPremium.get("INTEREST_OC").toString()));
								bean.setInterest((getMultipleVal(bean.getInterest())));
								bean.setOsClaimsLossUpdateOC(editPremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editPremium.get("OSCLAIM_LOSSUPDATE_OC").toString());
								bean.setOsClaimsLossUpdateOC((getMultipleVal(bean.getOsClaimsLossUpdateOC())));
								bean.setOverrider(editPremium.get("OVERRIDER_AMT_OC")==null?"":editPremium.get("OVERRIDER_AMT_OC").toString());
								bean.setOverrider((getMultipleVal(bean.getOverrider())));
								bean.setOverriderUSD(editPremium.get("OVERRIDER_AMT_DC")==null?"":editPremium.get("OVERRIDER_AMT_DC").toString());	
								bean.setOverriderUSD((getMultipleVal(bean.getOverriderUSD())));
		                        bean.setWithHoldingTaxOC((editPremium.get("WITH_HOLDING_TAX_OC")==null?"":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
		                        bean.setWithHoldingTaxOC((getMultipleVal(bean.getWithHoldingTaxOC())));
		                        bean.setPredepartment(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
		                        bean.setDepartmentId(editPremium.get("SUB_CLASS")==null?"":editPremium.get("SUB_CLASS").toString());
		                        bean.setTaxDedectSource((editPremium.get("TDS_OC")==null?"":editPremium.get("TDS_OC").toString()));
		                        bean.setTaxDedectSource((getMultipleVal(bean.getTaxDedectSource())));
		                        bean.setVatPremium(editPremium.get("VAT_PREMIUM_OC")==null?"":fm.formatter(editPremium.get("VAT_PREMIUM_OC").toString()));
		                        bean.setVatPremium((getMultipleVal(bean.getVatPremium())));
	                            bean.setBrokerageVat(editPremium.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(editPremium.get("BROKERAGE_VAT_OC").toString()));
	                            bean.setBrokerageVat((getMultipleVal(bean.getBrokerageVat())));
		                        bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"":editPremium.get("BONUS_OC").toString()));
		                        bean.setBonus((getMultipleVal(bean.getBonus())));
		                        bean.setTotalCredit((editPremium.get("TOTAL_CR_OC")==null?"":editPremium.get("TOTAL_CR_OC").toString()));
		                        bean.setTotalCredit((getMultipleVal(bean.getTotalCredit())));
		    					bean.setTotalDebit((editPremium.get("TOTAL_DR_OC")==null?"":editPremium.get("TOTAL_DR_OC").toString()));
		    					bean.setTotalDebit((getMultipleVal(bean.getTotalDebit())));
		    					resList.add(bean);
						}
						 res1.setPremiumEditRes1(resList);
					 }
				}
				else{
				if("3".equalsIgnoreCase(req.getProductId())){
					 if(!CollectionUtils.isEmpty(transList)){
						for(int i=0;i<transList.size();i++){
							PremiumEditRes1 bean = new PremiumEditRes1();
							Tuple	 editPremium=transList.get(i);
							bean.setTransaction(editPremium.get("TRANS_DATE")==null?"":formatDate(editPremium.get("TRANS_DATE")).toString()); 
							bean.setAccountPeriod(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString());
							bean.setAccountPeriodyear(editPremium.get("ACCOUNT_PERIOD_YEAR")==null?"":editPremium.get("ACCOUNT_PERIOD_YEAR").toString());
							bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
							bean.setCurrency(editPremium.get("CURRENCY_ID").toString()==null?"":editPremium.get("CURRENCY_ID").toString());
							if(null==editPremium.get("EXCHANGE_RATE")){
								GetCommonValueRes common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
								bean.setExchRate(common.getCommonResponse());
							}
							else{
							bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"0":editPremium.get("EXCHANGE_RATE").toString()));
							}
							bean.setBrokerage(fm.formatter(editPremium.get("BROKERAGE_AMT_OC")==null?"0":editPremium.get("BROKERAGE_AMT_OC").toString()));
							bean.setTax(fm.formatter(editPremium.get("TAX_AMT_OC")==null?"0":editPremium.get("TAX_AMT_OC").toString()));
							bean.setPremiumQuotaShare(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString());
							bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
							bean.setPremiumSurplus(editPremium.get("PREMIUM_SURPLUS_OC")==null?"":editPremium.get("PREMIUM_SURPLUS_OC").toString());
							bean.setCommissionSurplus(editPremium.get("COMMISSION_SURPLUS_OC")==null?"":editPremium.get("COMMISSION_SURPLUS_OC").toString());
							bean.setPremiumportifolioIn(editPremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
							bean.setCliamPortfolioin(editPremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editPremium.get("CLAIM_PORTFOLIOIN_OC").toString());
							bean.setPremiumportifolioout(editPremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
							bean.setLossReserveReleased(editPremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editPremium.get("LOSS_RESERVE_RELEASED_OC").toString());
							bean.setPremiumReserveQuotaShare(editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
							bean.setCashLossCredit(editPremium.get("CASH_LOSS_CREDIT_OC")==null?"":editPremium.get("CASH_LOSS_CREDIT_OC").toString());
							bean.setLossReserveRetained(editPremium.get("LOSS_RESERVERETAINED_OC")==null?"":editPremium.get("LOSS_RESERVERETAINED_OC").toString());
							bean.setProfitCommission(editPremium.get("PROFIT_COMMISSION_OC")==null?"":editPremium.get("PROFIT_COMMISSION_OC").toString());
							bean.setCashLossPaid(editPremium.get("CASH_LOSSPAID_OC")==null?"":editPremium.get("CASH_LOSSPAID_OC").toString());
							bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
							bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
							bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
							bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());
							bean.setClaimspaid(editPremium.get("CLAIMS_PAID_OC")==null?"":editPremium.get("CLAIMS_PAID_OC").toString());				 
						    bean.setMdpremium(fm.formatter(editPremium.get("M_DPREMIUM_OC")==null?"":editPremium.get("M_DPREMIUM_OC").toString()));
						    bean.setAdjustmentpremium(editPremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editPremium.get("ADJUSTMENT_PREMIUM_OC").toString());
						    bean.setRecuirementpremium(editPremium.get("REC_PREMIUM_OC")==null?"":editPremium.get("REC_PREMIUM_OC").toString());
						    bean.setCommission(editPremium.get("COMMISSION")==null?"":editPremium.get("COMMISSION").toString());
						    bean.setInstlmentNo(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
					    	if("RP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())||"AP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
					    	{
					    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
					    	}else
					    	{
					    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")+"_"+editPremium.get("ACCOUNT_PERIOD_QTR"));
					    	}					
						    bean.setInceptionDate(editPremium.get("ENTRY_DATE_TIME")==null?"":formatDate(editPremium.get("ENTRY_DATE_TIME")).toString());
						    bean.setXlCost(editPremium.get("XL_COST_OC")==null?"":editPremium.get("XL_COST_OC").toString());
						    bean.setCliamportfolioout(editPremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editPremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
						    bean.setPremiumReserveReleased(editPremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editPremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
						    bean.setOtherCost(fm.formatter(editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString()));
						    bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
							bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
							bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
							bean.setInterest(fm.formatter(editPremium.get("INTEREST_OC")==null?"0":editPremium.get("INTEREST_OC").toString()));
							bean.setOsClaimsLossUpdateOC(editPremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editPremium.get("OSCLAIM_LOSSUPDATE_OC").toString());
							bean.setOverrider(editPremium.get("OVERRIDER_AMT_OC")==null?"":editPremium.get("OVERRIDER_AMT_OC").toString());
							bean.setOverriderUSD(editPremium.get("OVERRIDER_AMT_DC")==null?"":editPremium.get("OVERRIDER_AMT_DC").toString());	
							bean.setAmendmentDate(editPremium.get("AMENDMENT_DATE")==null?"":editPremium.get("AMENDMENT_DATE").toString());	
	                        bean.setWithHoldingTaxOC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC")==null?"":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
	                        bean.setWithHoldingTaxDC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_DC")==null?"":editPremium.get("WITH_HOLDING_TAX_DC").toString()));
	                        bean.setRicession(editPremium.get("RI_CESSION")==null?"":editPremium.get("RI_CESSION").toString());
	                        bean.setPredepartment(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
	                        bean.setDepartmentId(editPremium.get("SUB_CLASS")==null?"":editPremium.get("SUB_CLASS").toString());
	                        bean.setTaxDedectSource(fm.formatter(editPremium.get("TDS_OC")==null?"":editPremium.get("TDS_OC").toString()));
	                        bean.setTaxDedectSourceDc(fm.formatter(editPremium.get("TDS_DC")==null?"":editPremium.get("TDS_DC").toString()));
	                        bean.setVatPremium(editPremium.get("VAT_PREMIUM_OC")==null?"":fm.formatter(editPremium.get("VAT_PREMIUM_OC").toString()));
	                        bean.setVatPremiumDc(editPremium.get("VAT_PREMIUM_DC")==null?"":fm.formatter(editPremium.get("VAT_PREMIUM_DC").toString()));
	                        bean.setBrokerageVat(editPremium.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(editPremium.get("BROKERAGE_VAT_OC").toString()));
	                        bean.setBrokerageVatDc(editPremium.get("BROKERAGE_VAT_DC")==null?"":fm.formatter(editPremium.get("BROKERAGE_VAT_DC").toString()));
	                        bean.setDocumentType(editPremium.get("DOCUMENT_TYPE")==null?"":editPremium.get("DOCUMENT_TYPE").toString());
	                        bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"":editPremium.get("BONUS_OC").toString()));
	                        bean.setBonusDc(fm.formatter(editPremium.get("BONUS_DC")==null?"":editPremium.get("BONUS_DC").toString()));
	                        bean.setGnpiDate((editPremium.get("GNPI_ENDT_NO")==null?"":editPremium.get("GNPI_ENDT_NO").toString()));
	                        bean.setStatementDate(editPremium.get("STATEMENT_DATE")==null?"":formatDate(editPremium.get("STATEMENT_DATE")).toString());
	                        bean.setChooseTransaction(editPremium.get("REVERSEL_STATUS")==null?"":editPremium.get("REVERSEL_STATUS").toString() );
		       	            bean.setTransDropDownVal(editPremium.get("REVERSE_TRANSACTION_NO")==null?"":editPremium.get("REVERSE_TRANSACTION_NO").toString() );
							resList.add(bean);
						}
						res1.setPremiumEditRes1(resList);
					 }
				}
				else{
					 if(!CollectionUtils.isEmpty(transList)){
						 for(int i=0;i<transList.size();i++){
							 PremiumEditRes1 bean = new PremiumEditRes1();
							 Tuple	 xolView=transList.get(i);
								bean.setContNo(xolView.get("CONTRACT_NO")==null?"":xolView.get("CONTRACT_NO").toString());
								bean.setTransactionNo(xolView.get("TRANSACTION_NO")==null?"":xolView.get("TRANSACTION_NO").toString());
								bean.setTransaction(xolView.get("TRANS_DATE")==null?"":formatDate(xolView.get("TRANS_DATE")).toString()); 
								bean.setBrokerage(fm.formatter(xolView.get("BROKERAGE_AMT_OC")==null?"0":xolView.get("BROKERAGE_AMT_OC").toString()));
								bean.setTax(fm.formatter(xolView.get("TAX_AMT_OC")==null?"0":xolView.get("TAX_AMT_OC").toString()));
								bean.setMdpremium(fm.formatter(xolView.get("M_DPREMIUM_OC")==null?"0":xolView.get("M_DPREMIUM_OC").toString()));
								bean.setAdjustmentpremium(fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_OC")==null?"0":xolView.get("ADJUSTMENT_PREMIUM_OC").toString()));							
								bean.setRecuirementpremium(fm.formatter(xolView.get("REC_PREMIUM_OC")==null?"0":xolView.get("REC_PREMIUM_OC").toString()));
								bean.setNetDue(fm.formatter(xolView.get("NETDUE_OC")==null?"0":xolView.get("NETDUE_OC").toString()));
								bean.setLayerno(xolView.get("LAYER_NO")==null?"":xolView.get("LAYER_NO").toString());
								bean.setEnteringMode(xolView.get("ENTERING_MODE")==null?"":xolView.get("ENTERING_MODE").toString());
								bean.setAccountPeriod(xolView.get("INSTALMENT_NUMBER")+(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":("_"+xolView.get("ACCOUNT_PERIOD_QTR"))));
								bean.setCurrency(xolView.get("CURRENCY_ID")==null?"":xolView.get("CURRENCY_ID").toString());
								bean.setOtherCost(fm.formatter(xolView.get("OTHER_COST_OC")==null?"0":xolView.get("OTHER_COST_OC").toString()));
								bean.setBrokerageusd(fm.formatter(xolView.get("BROKERAGE_AMT_DC")==null?"0":xolView.get("BROKERAGE_AMT_DC").toString()));
								bean.setTaxusd(fm.formatter(xolView.get("TAX_AMT_DC")==null?"0":xolView.get("TAX_AMT_DC").toString()));
								bean.setMdpremiumusd(fm.formatter(xolView.get("M_DPREMIUM_DC")==null?"0":xolView.get("M_DPREMIUM_DC").toString()));
								bean.setAdjustmentpremiumusd(fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_DC")==null?"0":xolView.get("ADJUSTMENT_PREMIUM_DC").toString()));
								bean.setRecuirementpremiumusd(fm.formatter(xolView.get("REC_PREMIUM_DC")==null?"0":xolView.get("REC_PREMIUM_DC").toString()));
								bean.setNetdueusd(fm.formatter(xolView.get("NETDUE_DC")==null?"0":xolView.get("NETDUE_DC").toString()));
								bean.setOtherCostUSD(fm.formatter(xolView.get("OTHER_COST_DC")==null?"0":xolView.get("OTHER_COST_DC").toString()));
								bean.setInceptionDate(xolView.get("ENTRY_DATE")==null?"":formatDate(xolView.get("ENTRY_DATE")).toString());
								bean.setCedentRef(xolView.get("CEDANT_REFERENCE")==null?"":xolView.get("CEDANT_REFERENCE").toString());
								bean.setRemarks(xolView.get("REMARKS")==null?"":xolView.get("REMARKS").toString());
								bean.setTotalCredit(fm.formatter(xolView.get("TOTAL_CR_OC")==null?"":xolView.get("TOTAL_CR_OC").toString()));
								bean.setTotalCreditDC(fm.formatter(xolView.get("TOTAL_CR_DC")==null?"":xolView.get("TOTAL_CR_DC").toString()));
								bean.setTotalDebit(fm.formatter(xolView.get("TOTAL_DR_OC")==null?"":xolView.get("TOTAL_DR_OC").toString()));
								bean.setTotalDebitDC(fm.formatter(xolView.get("TOTAL_DR_DC")==null?"":xolView.get("TOTAL_DR_DC").toString()));
								bean.setAmendmentDate(xolView.get("AMENDMENT_DATE")==null?"":xolView.get("AMENDMENT_DATE").toString());
								bean.setWithHoldingTaxOC(fm.formatter(xolView.get("WITH_HOLDING_TAX_OC")==null?"":xolView.get("WITH_HOLDING_TAX_OC").toString()));
								bean.setWithHoldingTaxDC(fm.formatter(xolView.get("WITH_HOLDING_TAX_DC")==null?"":xolView.get("WITH_HOLDING_TAX_DC").toString()));
								bean.setDueDate(xolView.get("DUE_DATE")==null?"":xolView.get("DUE_DATE").toString());
								bean.setCreditsign(xolView.get("NETDUE_OC")==null?"":xolView.get("NETDUE_OC").toString());
								bean.setRicession(xolView.get("RI_CESSION")==null?"":xolView.get("RI_CESSION").toString());
								bean.setPredepartment(xolView.get("PREMIUM_CLASS")==null?"":xolView.get("PREMIUM_CLASS").toString());
								bean.setDepartmentId(xolView.get("SUB_CLASS")==null?"":xolView.get("SUB_CLASS").toString());
								bean.setTaxDedectSource(fm.formatter(xolView.get("TDS_OC")==null?"0":xolView.get("TDS_OC").toString()));
								bean.setTaxDedectSourceDc(fm.formatter(xolView.get("TDS_DC")==null?"0":xolView.get("TDS_DC").toString()));
								bean.setVatPremium(xolView.get("VAT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_OC").toString()));
								bean.setVatPremiumDc(xolView.get("VAT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_DC").toString()));
								bean.setBrokerageVat(xolView.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_OC").toString()));
								bean.setBrokerageVatDc(xolView.get("BROKERAGE_VAT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_DC").toString()));
								bean.setDocumentType(xolView.get("DOCUMENT_TYPE")==null?"":xolView.get("DOCUMENT_TYPE").toString());
								bean.setBonus(fm.formatter(xolView.get("BONUS_OC")==null?"0":xolView.get("BONUS_OC").toString()));
								bean.setBonusDc(fm.formatter(xolView.get("BONUS_DC")==null?"0":xolView.get("BONUS_DC").toString()));
								bean.setExchRate(dropDowmImpl.exchRateFormat(xolView.get("EXCHANGE_RATE")==null?"0":xolView.get("EXCHANGE_RATE").toString()));
								bean.setGnpiDate(xolView.get("GNPI_ENDT_NO")==null?"":xolView.get("GNPI_ENDT_NO").toString());
								bean.setStatementDate(xolView.get("STATEMENT_DATE")==null?"":formatDate(xolView.get("STATEMENT_DATE")).toString());
								bean.setChooseTransaction(xolView.get("REVERSEL_STATUS")==null?"":xolView.get("REVERSEL_STATUS").toString() );
								bean.setTransDropDownVal(xolView.get("REVERSE_TRANSACTION_NO")==null?"":xolView.get("REVERSE_TRANSACTION_NO").toString() );
								resList.add(bean);
						 }	
						 res1.setPremiumEditRes1(resList);
					 }
				}
				
				if(transList!=null && transList.size()>0)
					res1.setSaveFlag("true");
				}
				response.setCommonResponse(res1);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}
	
	private String getMultipleVal(String premiumQuotaShare) {
		String res="";double val =0;
		try{
			if(premiumQuotaShare==""){
				premiumQuotaShare="0";
			}
				 val = Double.parseDouble(premiumQuotaShare.replaceAll(",", ""))*-1;
			 if(val==-0){
				 val = 0; 
			 }
			res = fm.formatter(Double.toString(val));
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	public CurrencyListRes currencyList(String branchCode) {
		CurrencyListRes response = new CurrencyListRes();
		List<CurrencyListRes1> resList = new ArrayList<CurrencyListRes1>();
		List<Map<String, Object>> list = null;
		try {
			//query  -- currency.list
			list = xolPremiumCustomRepository.getCurrencyList(branchCode);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
					CurrencyListRes1 res = new CurrencyListRes1();
					res.setCurrencyId(tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					res.setShortName(tempMap.get("SHORT_NAME") == null ? "" : tempMap.get("SHORT_NAME").toString());
					resList.add(res);
				}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonSaveRes getAdjPremium(GetAdjPremiumReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		List<Double>list=null;
		String premium="0";
		String dppremium="0";
		String adjpremium="0";
		try {
			// query = "GET_GNPI_PREMIUM";
			list= xolPremiumCustomRepository.getGnpiPremium(bean);
			if(list!=null && list.size()>0){ 
				Double map =list.get(0);
				premium= map ==null?"0": map.toString();
			}
			//query = "GET_DEPOSIT_PREMIUM";
			list = xolPremiumCustomRepository.getDepositPremium(bean);
			if(list!=null && list.size()>0){
				Double map=list.get(0);
				dppremium= map ==null?"0":map.toString();
			}
			adjpremium=Double.toString((Double.parseDouble(premium))-(Double.parseDouble(dppremium)));
			response.setResponse(adjpremium);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	
	public GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();
		String output="";
		List<Tuple> list = null;
		try{
			if ("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId())
					|| "".equalsIgnoreCase(req.getTransactionNo())) {
				list = xolPremiumCustomRepository.xolPremiumViewDetailsTemp(req.getBranchCode(), req.getContNo(),
						req.getProductId(), req.getRequestNo());
			} else {
				if ("3".equalsIgnoreCase(req.getProductId())) {
					list = xolPremiumCustomRepository.premiumSelectXolPremiumView(req.getBranchCode(),
							req.getContNo(), req.getProductId(), req.getTransactionNo());
				} else {
					list = xolPremiumCustomRepository.premiumSelectRetroXolPremiumView(req.getBranchCode(),
							req.getContNo(), req.getProductId(), req.getTransactionNo());
				}
			}
		    if(list!=null && list.size()>0) {
				Tuple xolView= list.get(0);
							bean.setContNo(xolView.get("CONTRACT_NO")==null?"":xolView.get("CONTRACT_NO").toString());
							bean.setTransactionNo(xolView.get("TRANSACTION_NO")==null?"":xolView.get("TRANSACTION_NO").toString());
							bean.setTransaction(xolView.get("TRANS_DATE")==null?"":formatDate(xolView.get("TRANS_DATE")).toString()); 
							bean.setBrokerage(xolView.get("BROKERAGE_AMT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_OC").toString()));
							bean.setTax(xolView.get("TAX_AMT_OC")==null?"":fm.formatter(xolView.get("TAX_AMT_OC").toString()));
							bean.setMdpremium(xolView.get("M_DPREMIUM_OC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_OC").toString()));
							bean.setAdjustmentpremium(xolView.get("ADJUSTMENT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_OC").toString()));							
							bean.setRecuirementpremium(xolView.get("REC_PREMIUM_OC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_OC").toString()));
							bean.setNetDue(xolView.get("NETDUE_OC")==null?"":fm.formatter(xolView.get("NETDUE_OC").toString()));
							bean.setLayerno(xolView.get("LAYER_NO")==null?"":xolView.get("LAYER_NO").toString());
							bean.setEnteringMode(xolView.get("ENTERING_MODE")==null?"":xolView.get("ENTERING_MODE").toString());
							bean.setAccountPeriod(xolView.get("INSTALMENT_NUMBER")+(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":("_"+xolView.get("ACCOUNT_PERIOD_QTR"))));
							bean.setCurrencyId(xolView.get("CURRENCY_ID")==null?"":xolView.get("CURRENCY_ID").toString());
							bean.setOtherCost(xolView.get("OTHER_COST_OC")==null?"":fm.formatter(xolView.get("OTHER_COST_OC").toString()));
							bean.setBrokerageusd(xolView.get("BROKERAGE_AMT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_DC").toString()));
							bean.setTaxusd(xolView.get("TAX_AMT_DC")==null?"":fm.formatter(xolView.get("TAX_AMT_DC").toString()));
							bean.setMdpremiumusd(xolView.get("M_DPREMIUM_DC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_DC").toString()));
							bean.setAdjustmentpremiumusd(xolView.get("ADJUSTMENT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_DC").toString()));
							bean.setRecuirementpremiumusd(xolView.get("REC_PREMIUM_DC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_DC").toString()));
							bean.setNetdueusd(xolView.get("NETDUE_DC")==null?"":fm.formatter(xolView.get("NETDUE_DC").toString()));
							bean.setOtherCostUSD(xolView.get("OTHER_COST_DC")==null?"":fm.formatter(xolView.get("OTHER_COST_DC").toString()));
							bean.setInceptionDate(xolView.get("ENTRY_DATE_TIME")==null?"":formatDate(xolView.get("ENTRY_DATE_TIME")).toString());
							bean.setCedentRef(xolView.get("CEDANT_REFERENCE")==null?"":xolView.get("CEDANT_REFERENCE").toString());
							bean.setRemarks(xolView.get("REMARKS")==null?"":xolView.get("REMARKS").toString());
							bean.setTotalCredit(xolView.get("TOTAL_CR_OC")==null?"":fm.formatter(xolView.get("TOTAL_CR_OC").toString()));
							bean.setTotalCreditDC(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_CR_DC").toString()));
							bean.setTotalDebit(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_OC").toString()));
							bean.setTotalDebitDC(xolView.get("TOTAL_DR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_DC").toString()));
							bean.setAmendmentDate(xolView.get("AMENDMENT_DATE")==null?"":formatDate(xolView.get("AMENDMENT_DATE")).toString());
                            bean.setWithHoldingTaxOC(xolView.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_OC").toString()));
                            bean.setWithHoldingTaxDC(xolView.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_DC").toString()));
		                   
                            if ("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId())|| "".equalsIgnoreCase(req.getTransactionNo())) {
		                            bean.setDueDate(xolView.get("TRANS_DATE")==null?"":fm.Transadded(xolView.get("TRANS_DATE")).toString());
		                    }else {
		                    	bean.setDueDate(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":(xolView.get("ACCOUNT_PERIOD_QTR")).toString());
		                    }
                            
                            bean.setCreditsign(xolView.get("NETDUE_OC")==null?"":xolView.get("NETDUE_OC").toString());
                            bean.setRicession(xolView.get("RI_CESSION")==null?"":xolView.get("RI_CESSION").toString());
                            bean.setPredepartment(xolView.get("PREMIUM_CLASS")==null?"":xolView.get("PREMIUM_CLASS").toString());
                            bean.setDepartmentId(xolView.get("SUB_CLASS")==null?"":xolView.get("SUB_CLASS").toString());
                            bean.setTaxDedectSource(xolView.get("TDS_OC")==null?"":fm.formatter(xolView.get("TDS_OC").toString()));
                            bean.setTaxDedectSourceDc(xolView.get("TDS_DC")==null?"":fm.formatter(xolView.get("TDS_DC").toString()));
                            bean.setVatPremium(xolView.get("VAT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_OC").toString()));
                            bean.setVatPremiumDc(xolView.get("VAT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_DC").toString()));
                            bean.setBrokerageVat(xolView.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_OC").toString()));
                            bean.setBrokerageVatDc(xolView.get("BROKERAGE_VAT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_DC").toString()));
                            bean.setDocumentType(xolView.get("DOCUMENT_TYPE")==null?"":xolView.get("DOCUMENT_TYPE").toString());
                            bean.setBonus(xolView.get("BONUS_OC")==null?"":fm.formatter(xolView.get("BONUS_OC").toString()));
                            bean.setBonusDc(xolView.get("BONUS_DC")==null?"":fm.formatter(xolView.get("BONUS_DC").toString()));
            				bean.setExchRate(dropDowmImpl.exchRateFormat(xolView.get("EXCHANGE_RATE")==null?"":xolView.get("EXCHANGE_RATE").toString()));
                            bean.setGnpiDate(xolView.get("GNPI_ENDT_NO")==null?"":xolView.get("GNPI_ENDT_NO").toString());
                            bean.setStatementDate(xolView.get("STATEMENT_DATE")==null?"":formatDate(xolView.get("STATEMENT_DATE")).toString());
                            bean.setChooseTransaction(xolView.get("REVERSEL_STATUS")==null?"":xolView.get("REVERSEL_STATUS").toString());
                            bean.setTransDropDownVal(xolView.get("REVERSE_TRANSACTION_NO")==null?"":xolView.get("REVERSE_TRANSACTION_NO").toString());
						}				
		   	if(StringUtils.isNotBlank(bean.getCurrencyId())){
				// query -- premium.select.currency
		   		output = xolPremiumCustomRepository.premiumSelectCurrency(bean.getCurrencyId(),req.getBranchCode());
				bean.setCurrency(output == null ? "" : output);
		   	}
		   	
		   	//query -- premium.select.currecy.name
		   	output = xolPremiumCustomRepository.selectCurrecyName(req.getBranchCode());
			bean.setCurrencyName(output == null ? "" : output);

			if ("3".equalsIgnoreCase(req.getProductId())) {
				// query -- GETSETTLEMET_STATUS
				List<Tuple> premlist = xolPremiumCustomRepository.getSettlementStatus(req.getContNo());
				List<SettlementstatusRes> res1List = new ArrayList<SettlementstatusRes>();
				if (premlist.size() > 0) {
					for (int i = 0; i < premlist.size(); i++) {
						Tuple map = premlist.get(i);
						SettlementstatusRes res1 = new SettlementstatusRes();
						String allocate = map.get("ALLOCATED_TILL_DATE") == null ? "0"
								: map.get("ALLOCATED_TILL_DATE").toString();
						String net = map.get("NETDUE_OC").toString();
						if ("0".equalsIgnoreCase(allocate)) {
							res1.setSettlementstatus("Pending");
						} else if (Double.parseDouble(allocate) == Double.parseDouble(net)) {
							res1.setSettlementstatus("Allocated");
						} else {
							res1.setSettlementstatus("Partially Allocated");
						}
						res1List.add(res1);
					}
					bean.setSettlementstatusRes(res1List);
				}
			}
		   	response.setCommonResponse(bean);
		   	response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	public premiumInsertMethodRes premiumUpdateMethod(PremiumInsertMethodReq beanObj) {
		premiumInsertMethodRes response = new premiumInsertMethodRes();
		PremiumInsertRes res = new PremiumInsertRes();
		try {
			String[] args = updateAruguments(beanObj);
			String netDueOc = "0";
			netDueOc = args[12];
			xolPremiumCustomRepository.premiumDetailArchive(beanObj, netDueOc);
			if ("Temp".equalsIgnoreCase(beanObj.getTableType()) && "3".equalsIgnoreCase(beanObj.getProductId())) {
				xolPremiumCustomRepository.xolPremiumUpdateUpdateTemp(args);
				beanObj.setRequestNo(args[47]);
			} else {
				if ("3".equalsIgnoreCase(beanObj.getProductId())) {
					xolPremiumCustomRepository.premiumUpdateXolUpdatePre(args);
				} else {
					xolPremiumCustomRepository.premiumUpdateRetroxolUpdatePre(args);
				}
				beanObj.setTransactionNo(args[47]);
			}
			
			//if ("Submit".equalsIgnoreCase(beanObj.getButtonStatus()) && "Temp".equalsIgnoreCase(beanObj.getTableType())	&& "3".equalsIgnoreCase(beanObj.getProductId())) {
				if ("3".equalsIgnoreCase(beanObj.getProductId())) {
					//beanObj.setTransactionNo(fm.getSequence("Premium", beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(), "", beanObj.getTransaction()));
					xolPremiumCustomRepository.facTempStatusUpdate(beanObj);
					getTempToMainMove(beanObj, netDueOc);
				}
				

				xolPremiumCustomRepository.facTempStatusUpdate(beanObj);
			//}
			res.setRequestNo(beanObj.getRequestNo());
			res.setTransactionNo(beanObj.getTransactionNo());
			response.setInsertRes(res);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public  String[] updateAruguments(final PremiumInsertMethodReq beanObj) {
		
		if("RP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "AP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "RTP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "RVP".equalsIgnoreCase(beanObj.getAccountPeriod()))
    	{
			beanObj.setInstlmentNo(beanObj.getAccountPeriod());	        		
    	}else if(!beanObj.getAccountPeriod().equalsIgnoreCase(""))// && StringUtils.isBlank(bean.getTransactionNo()))
    	{
        	final String[] InstalmentNo=beanObj.getAccountPeriod().split("_");
        	beanObj.setInstlmentNo(InstalmentNo[0]);
        	beanObj.setInstalmentdate(InstalmentNo[1]);
    	}
		
		String[] args=null;				
		args=new String[48];
		args[0]=beanObj.getTransaction();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args[1]=beanObj.getCurrencyId();
			}
			else{
				args[1]=beanObj.getCurrency();
			}
		args[2]=beanObj.getExchRate();
		args[3]=beanObj.getBrokerageview();
		args[4]=getModeOfTransaction(beanObj.getBrokerage(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
		args[5]=beanObj.getTaxview();
		args[6]=getModeOfTransaction(beanObj.getTax(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
		args[7]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
		args[8]=getModeOfTransaction(beanObj.getCommission(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[9]=getModeOfTransaction(beanObj.getMdpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
		args[10]=getModeOfTransaction(beanObj.getAdjustmentpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
		args[11]=getModeOfTransaction(beanObj.getRecuirementpremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
		args[13]="2";
		args[14]=beanObj.getReceiptno();
		
		if(beanObj.getInstlmentNo().equalsIgnoreCase("RP"))
		{
			args[9]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
			args[10]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getRecuirementpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getRecuirementpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
				}
			}
			
		}else if(beanObj.getInstlmentNo().equalsIgnoreCase("AP"))
		{
			args[9]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[18]= dropDowmImpl.GetDesginationCountry(args[9], beanObj.getExchRate());
			args[11]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getAdjustmentpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getAdjustmentpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
			}
			}
		}else
		{
			args[10]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[19]= dropDowmImpl.GetDesginationCountry(args[10], beanObj.getExchRate());
			args[11]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[20]= dropDowmImpl.GetDesginationCountry(args[11], beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getMdpremium()))
			{
			double premium=Double.parseDouble(beanObj.getMdpremium());
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				double brokerage=premium*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[4]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[16]= dropDowmImpl.GetDesginationCountry(args[4], beanObj.getExchRate());
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				double tax=premium*(Double.parseDouble(beanObj.getTaxview())/100);
				args[6]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[17]= dropDowmImpl.GetDesginationCountry(args[6], beanObj.getExchRate());
			}
		}
		}
		args[15]=getModeOfTransaction(beanObj.getOtherCost(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[30]=getModeOfTransaction(beanObj.getWithHoldingTaxOC(), beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[22]= dropDowmImpl.GetDesginationCountry(args[15], beanObj.getExchRate());
		args[23]=beanObj.getCedentRef();
		args[24]=beanObj.getRemarks();
		args[25]=getModeOfTransaction(beanObj.getTotalCredit(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[26]= dropDowmImpl.GetDesginationCountry(args[25],beanObj.getExchRate());
		args[27]=getModeOfTransaction(beanObj.getTotalDebit(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[28]= dropDowmImpl.GetDesginationCountry(args[27],beanObj.getExchRate());
		args[29]=beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate();
        args[31]= dropDowmImpl.GetDesginationCountry(args[30], beanObj.getExchRate());
        args[32]=beanObj.getRicession();
        args[33]=beanObj.getDepartmentId();
        args[34] = getModeOfTransaction(beanObj.getTaxDedectSource()==null?"0":beanObj.getTaxDedectSource(),beanObj.getEnteringMode(), beanObj.getShareSigned());
        args[35] =  dropDowmImpl.GetDesginationCountry(args[34], beanObj.getExchRate());
        args[36] = getModeOfTransaction(beanObj.getVatPremium()==null?"0":beanObj.getVatPremium(),beanObj.getEnteringMode(), beanObj.getShareSigned());
        args[37] =  dropDowmImpl.GetDesginationCountry(args[36], beanObj.getExchRate());
		args[38] = getModeOfTransaction(beanObj.getBonus(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[39] =  dropDowmImpl.GetDesginationCountry(args[38], beanObj.getExchRate());
		args[46]=beanObj.getContNo();
		
			args[47]=beanObj.getTransactionNo();
		
		args[40]=StringUtils.isEmpty(beanObj.getGnpiDate()) ?"" :beanObj.getGnpiDate();
		args[41]=beanObj.getPredepartment();
		args[42]= beanObj.getStatementDate();
		args[43] = getModeOfTransaction(beanObj.getBrokerageVat(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[44] = dropDowmImpl.GetDesginationCountry(args[43], beanObj.getExchRate());
		args[45] = beanObj.getDocumentType();
		args[12]=getNetDueXolUpdate(args,beanObj.getProductId());
		args[21]= dropDowmImpl.GetDesginationCountry(args[12], beanObj.getExchRate());
		final String[] copiedArray = new String[args.length];
		System.arraycopy(args, 0, copiedArray, 0, args.length);
		return copiedArray;
	}
	
	private static String getNetDueXolUpdate(final String[] args, String id) {
		double Ant=StringUtils.isEmpty(args[9]) ? 0 :Double.parseDouble(args[9]) ;
		double Bnt=StringUtils.isEmpty(args[10]) ? 0 :Double.parseDouble(args[10]) ;
		double Cnt=StringUtils.isEmpty(args[11]) ? 0 :Double.parseDouble(args[11]) ;
		double Dnt=StringUtils.isEmpty(args[4]) ? 0 :Double.parseDouble(args[4]) ;
		double Ent=StringUtils.isEmpty(args[6]) ? 0 :Double.parseDouble(args[6]) ;
		double Fnt=StringUtils.isEmpty(args[15]) ? 0 :Double.parseDouble(args[15]) ;
		double Gnt=StringUtils.isEmpty(args[30]) ? 0 :Double.parseDouble(args[30]) ;
		double Int=StringUtils.isEmpty(args[34]) ? 0 :Double.parseDouble(args[34]) ;
		double Jnt=StringUtils.isEmpty(args[36]) ? 0 :Double.parseDouble(args[36]) ;
		double Knt =StringUtils.isEmpty(args[38]) ? 0 :Double.parseDouble(args[38]) ;
		double Lnt =StringUtils.isEmpty(args[43]) ? 0 :Double.parseDouble(args[43]) ;
		double c=(Ant+Bnt+Cnt+Int+Jnt)-(Dnt+Ent+Fnt+Gnt+Knt+Lnt);
		return String.valueOf(c);
	}
	
	
	public premiumInsertMethodRes premiumInsertMethod(PremiumInsertMethodReq beanObj) {
		premiumInsertMethodRes response = new premiumInsertMethodRes();
		PremiumInsertRes res = new PremiumInsertRes();
		try {
			int result;
			String[] args = insertArguments(beanObj);
			String netDueOc = "0";
			Object dbResponse;
			if ("3".equalsIgnoreCase(beanObj.getProductId())) {
				// query= "PREMIUM_INSERT_XOLPREMIUM_TEMP";
				RskPremiumDetailsTemp entity = rskPremiumDetailsTempMapper.toEntity(args);
				dbResponse = rskPremiumDetailsTempRepository.save(entity);
				beanObj.setRequestNo(args[1]);
			} else {
				RskXLPremiumDetails entity = rskXLPremiumDetailsMapper.toEntity(args);
				dbResponse = rskXLPremiumDetailsRepository.save(entity);
				beanObj.setTransactionNo(args[1]);
			}
			netDueOc = args[17];

			result = Objects.nonNull(dbResponse) ? 1 : 0;
			if ("submit".equalsIgnoreCase(beanObj.getButtonStatus())) {
				if ("3".equalsIgnoreCase(beanObj.getProductId())) {
					beanObj.setTransactionNo(fm.getSequence("Premium", beanObj.getProductId(),
							beanObj.getDepartmentId(), beanObj.getBranchCode(), "", beanObj.getTransaction()));
					// query -- FAC_TEMP_STATUS_UPDATE
					xolPremiumCustomRepository.facTempStatusUpdate(beanObj);
					res.setTransactionNo(beanObj.getTransactionNo());
					getTempToMainMove(beanObj, netDueOc);
				}

				if (result == 1) {
					if (!("RP".equalsIgnoreCase(beanObj.getInstlmentNo())
							|| "RTP".equalsIgnoreCase(beanObj.getInstlmentNo())
							|| "RVP".equalsIgnoreCase(beanObj.getInstlmentNo())
							|| "AP".equalsIgnoreCase(beanObj.getInstlmentNo()))) {
						// query -- UPDATE_MND_INSTALLMENT
						xolPremiumCustomRepository.updateMndInstallment(beanObj);
					}
				}
				if ("transEdit".equalsIgnoreCase(beanObj.getMode())) {
					if ("3".equalsIgnoreCase(beanObj.getProductId())) {
						// query -- UPDATE_REV_TRANSACTION_NO
						xolPremiumCustomRepository.updateRevTransactionNo(beanObj.getTransactionNo(),
								beanObj.getTransDropDownVal());
						xolPremiumCustomRepository.updateRevTransactionNo(beanObj.getTransDropDownVal(),
								beanObj.getTransactionNo());
					} else {
						// query -- UPDATE_REV_TRANSACTION_NO_RETRO
						xolPremiumCustomRepository.updateRevTransactionNoRetro(beanObj.getTransactionNo(),
								beanObj.getTransDropDownVal());

						xolPremiumCustomRepository.updateRevTransactionNoRetro(beanObj.getTransDropDownVal(),
								beanObj.getTransactionNo());
					}
					// query -- UPDATE_REV_TRANSACTION_NO_RETRO
					xolPremiumCustomRepository.updateRevTransactionNoRetro(beanObj.getTransactionNo(),
							beanObj.getTransDropDownVal());

					xolPremiumCustomRepository.updateRevTransactionNoRetro(beanObj.getTransDropDownVal(),
							beanObj.getTransactionNo());

					// query -- UPDATE_MND_INSTAL
					xolPremiumCustomRepository.updateMndInstal(beanObj.getProposalNo(), beanObj.getTransDropDownVal());
				}
			}
			res.setRequestNo(beanObj.getRequestNo());
			res.setTransactionNo(beanObj.getTransactionNo());
			response.setInsertRes(res);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	private String[] insertArguments(final PremiumInsertMethodReq beanObj){
		
		if("RP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "AP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "RTP".equalsIgnoreCase(beanObj.getAccountPeriod()) || "RVP".equalsIgnoreCase(beanObj.getAccountPeriod()))
    	{
			beanObj.setInstlmentNo(beanObj.getAccountPeriod());	        		
    	}else if(!beanObj.getAccountPeriod().equalsIgnoreCase(""))// && StringUtils.isBlank(bean.getTransactionNo()))
    	{
        	final String[] InstalmentNo=beanObj.getAccountPeriod().split("_");
        	beanObj.setInstlmentNo(InstalmentNo[0]);
        	beanObj.setInstalmentdate(InstalmentNo[1]);
    	}
		String[] args=null;	
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args=new String[61];
		}else{
			args=new String[56];
		}
		args[0]=beanObj.getContNo();
		
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			args[1] = getRequestNo(beanObj.getBranchCode());
		}else{
			args[1] = fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction());
			beanObj.setTransactionNo(args[1]);
		}
		args[2]=beanObj.getTransaction();
		args[3]=beanObj.getInstalmentdate();
		args[4]=StringUtils.isBlank(beanObj.getAccountPeriodyear())?"":beanObj.getAccountPeriodyear();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
		args[5]=beanObj.getCurrencyId();
		}
		else{
			args[5]=beanObj.getCurrency();
		}
		args[6]=beanObj.getExchRate();
		args[7]=beanObj.getBrokerageview();
		args[8] = getModeOfTransaction(beanObj.getBrokerage().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
		args[9]=beanObj.getTaxview();
		args[10]=getModeOfTransaction(beanObj.getTax().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
		args[11]=StringUtils.isEmpty(beanObj.getInceptionDate()) ?"" :beanObj.getInceptionDate();
		args[12]=getModeOfTransaction(beanObj.getCommission().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[13]=getModeOfTransaction(beanObj.getMdpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
		args[14]=getModeOfTransaction(StringUtils.isBlank(beanObj.getAdjustmentpremium())?"0":beanObj.getAdjustmentpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
		args[15]=getModeOfTransaction(StringUtils.isBlank(beanObj.getRecuirementpremium())?"0":beanObj.getRecuirementpremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
		if("RP".equalsIgnoreCase(beanObj.getInstlmentNo()))
		{
			args[13]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
			args[14]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getRecuirementpremium()))
			{
				double premiums=Double.parseDouble(beanObj.getRecuirementpremium());
				if(StringUtils.isEmpty(beanObj.getBrokerage()))
				{
					final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
					args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
					
				}
				if(StringUtils.isEmpty(beanObj.getTax()))
				{
					final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
					args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
					args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
				}
			}
		}else if("AP".equalsIgnoreCase(beanObj.getInstlmentNo()))
		{
			args[13]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[26]=dropDowmImpl.GetDesginationCountry(args[13],beanObj.getExchRate());
			args[15]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getAdjustmentpremium()))
			{
			double premiums=Double.parseDouble(beanObj.getAdjustmentpremium().replaceAll(",", ""));
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				final double brokerage=premiums*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				final double tax=premiums*(Double.parseDouble(beanObj.getTaxview())/100);
				args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
			}
		}
		}else
		{
			args[14]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[27]=dropDowmImpl.GetDesginationCountry(args[14],beanObj.getExchRate());
			args[15]=getModeOfTransaction(0+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
			args[28]=dropDowmImpl.GetDesginationCountry(args[15],beanObj.getExchRate());
			if(!StringUtils.isEmpty(beanObj.getMdpremium()))
			{
			final double premium=Double.parseDouble(beanObj.getMdpremium());
			if(StringUtils.isEmpty(beanObj.getBrokerage()))
			{
				final double brokerage=premium*(Double.parseDouble(beanObj.getBrokerageview())/100);
				args[8]=getModeOfTransaction(brokerage+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[24]=dropDowmImpl.GetDesginationCountry(args[8],beanObj.getExchRate());
				
			}
			if(StringUtils.isEmpty(beanObj.getTax()))
			{
				final double tax=premium*(Double.parseDouble(beanObj.getTaxview())/100);
				args[10]=getModeOfTransaction(tax+" ",beanObj.getEnteringMode(), beanObj.getShareSigned());
				args[25]=dropDowmImpl.GetDesginationCountry(args[10],beanObj.getExchRate());
			}
		}
		}
		args[16]="Y";
		args[18]=beanObj.getLayerno();
		args[19]="2";
		args[20]=StringUtils.isBlank(beanObj.getReceiptno())?"":beanObj.getReceiptno();
		args[21]=StringUtils.isBlank(beanObj.getInstlmentNo())?"":beanObj.getInstlmentNo();
		args[22]=StringUtils.isBlank(beanObj.getSettlementstatus())?"":beanObj.getSettlementstatus();
		args[23]=getModeOfTransaction(beanObj.getOtherCost().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[30]=dropDowmImpl.GetDesginationCountry(args[23],beanObj.getExchRate());
		args[31]=beanObj.getCedentRef();
		args[32]=beanObj.getRemarks();
		args[33]=getModeOfTransaction(beanObj.getTotalCredit().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[34]=dropDowmImpl.GetDesginationCountry(args[33],beanObj.getExchRate());
		args[35]=getModeOfTransaction(beanObj.getTotalDebit().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[36]=dropDowmImpl.GetDesginationCountry(args[35],beanObj.getExchRate());
		args[37]=getModeOfTransaction(beanObj.getWithHoldingTaxOC().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());		
		args[38]=dropDowmImpl.GetDesginationCountry(args[37],beanObj.getExchRate());
		args[39]=beanObj.getRicession();
		args[40] = beanObj.getLoginId();
		args[41] = beanObj.getBranchCode();
		args[42]=beanObj.getDepartmentId();
		args[43] = getModeOfTransaction(beanObj.getTaxDedectSource()==null?"0":beanObj.getTaxDedectSource().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[44] = dropDowmImpl.GetDesginationCountry(args[43], beanObj.getExchRate());
		args[45] = getModeOfTransaction(beanObj.getVatPremium()==null?"0":beanObj.getVatPremium().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[46] = dropDowmImpl.GetDesginationCountry(args[45], beanObj.getExchRate());
		args[47] = getModeOfTransaction(beanObj.getBonus()==null?"0":beanObj.getBonus().replaceAll(",", ""),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[48] = dropDowmImpl.GetDesginationCountry(args[47], beanObj.getExchRate());
		
		args[49] = StringUtils.isEmpty(beanObj.getGnpiDate()) ?"" :beanObj.getGnpiDate();
		args[50] ="D";
		args[51]=beanObj.getPredepartment();
		args[52]=beanObj.getStatementDate();
		args[53]=beanObj.getProposalNo();
		args[54]=beanObj.getProductId();
		args[55]=beanObj.getChooseTransaction()==null?"":beanObj.getChooseTransaction();
		if("3".equalsIgnoreCase(beanObj.getProductId())){
			if("submit".equalsIgnoreCase(beanObj.getButtonStatus())){
				args[56] = "A";
			}else{
				args[56] = "P";
			}
			args[57] = beanObj.getMode();
		}
		args[58] = getModeOfTransaction(beanObj.getBrokerageVat(),beanObj.getEnteringMode(), beanObj.getShareSigned());
		args[59] = dropDowmImpl.GetDesginationCountry(args[58], beanObj.getExchRate());
		args[60] = beanObj.getDocumentType();
		args[17] = getNetDueXol(args,beanObj.getProductId());
		args[29]=dropDowmImpl.GetDesginationCountry(args[17],beanObj.getExchRate());
		final String[] copiedArray = new String[args.length];
		System.arraycopy(args, 0, copiedArray, 0, args.length);
		
		return copiedArray;
	}
	
	private static String getModeOfTransaction(final String Value, final String enteringMode,
			final String shareSigned) {
		String result = "0";
		double shareSign = 0.0;
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		if (enteringMode != null) {
			if ("1".equalsIgnoreCase(enteringMode)) {
				shareSign = Double.valueOf(shareSigned);
			} else if ("2".equalsIgnoreCase(enteringMode)) {
				shareSign = 100;
			}
			if (!"".equalsIgnoreCase(Value)) {
				double finalValue = Double.parseDouble(Value) * shareSign / 100;
				result = String.valueOf(Double.valueOf(twoDForm.format(finalValue)));
			}
		}
		return result;
	}
	
	private void getTempToMainMove(PremiumInsertMethodReq beanObj, String netDueOc) {
		try {
			if (!"Main".equalsIgnoreCase(beanObj.getTableType())) {

				// query -- FAC_PREMIUM_TEMP_TO_MAIN
				
				RskPremiumDetailsTemp rskTemp = rskPremiumDetailsTempRepository.findByRequestNoAndBranchCode(new BigDecimal(beanObj.getRequestNo()),
						beanObj.getBranchCode());
				
				
				if (rskTemp != null) {
					RskPremiumDetails detailsEntity = rskPremiumDetailsMapper.toEntity(rskTemp);
					detailsEntity.setTransactionNo(new BigDecimal(beanObj.getTransactionNo()));
					rskPremiumDetailsRepository.save(detailsEntity);
				}

			}
			if ("3".equalsIgnoreCase(beanObj.getProductId())) {
				// query -- premium.sp.retroSplit
				xolPremiumCustomRepository.premiumSpRetroSplit(beanObj);
				xolPremiumCustomRepository.premiumRiSplit(beanObj);
				
			} else {
				// query -- PRCL_DELETE
				xolPremiumCustomRepository.prclDelete(beanObj);
			}
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}
	
	private String getRequestNo(String branchCode) {
		String reqNo = "";
		String name= "";
		try{
			// query -- GET_SEQ_NAME
			name = xolPremiumCustomRepository.getSeqName(branchCode);
			name = name == null ? "" : name;
			
			//query -- SELECT LPAD("+name+".nextval,6,0) FROM DUAL";
//			reqNo = xolPremiumCustomRepository.getLpad(name);
//			reqNo = reqNo == null ? "" : reqNo;
			String sql="SELECT LPAD("+name+".nextval,6,0) REQ_NO FROM DUAL";
			query = em.createNativeQuery(sql);
		
			query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String,Object>> list = query.getResultList();
			if(!CollectionUtils.isEmpty(list)) {
				reqNo = list.get(0).get("REQ_NO")==null?"":list.get(0).get("REQ_NO").toString();
			}
			
			reqNo ="92"+reqNo;
		}catch(Exception e){
			e.printStackTrace();
		}
		return reqNo;
	}
	
	private static String getNetDueXol(final String[] args, String id) {
		final double Ant=StringUtils.isEmpty(args[13]) ? 0 :Double.parseDouble(args[13]) ;
		final double Bnt=StringUtils.isEmpty(args[14]) ? 0 :Double.parseDouble(args[14]) ;
		final double Cnt=StringUtils.isEmpty(args[15]) ? 0 :Double.parseDouble(args[15]) ;
		final double Dnt=StringUtils.isEmpty(args[8]) ? 0 :Double.parseDouble(args[8]) ;
		final double Ent=StringUtils.isEmpty(args[10]) ? 0 :Double.parseDouble(args[10]) ;
		final double Fnt=StringUtils.isEmpty(args[12]) ? 0 :Double.parseDouble(args[12]) ;
		final double Gnt=StringUtils.isEmpty(args[23]) ? 0 :Double.parseDouble(args[23]) ;
		final double Hnt=StringUtils.isEmpty(args[37]) ? 0 :Double.parseDouble(args[37]) ;
		final double Int=StringUtils.isEmpty(args[43]) ? 0 :Double.parseDouble(args[43]) ;
		final double Jnt=StringUtils.isEmpty(args[45]) ? 0 :Double.parseDouble(args[45]) ;
		final double Knt=StringUtils.isEmpty(args[47]) ? 0 :Double.parseDouble(args[47]) ;
		final double lnt=StringUtils.isEmpty(args[58]) ? 0 :Double.parseDouble(args[58]) ;
	    final double cnt=(Ant+Bnt+Cnt+Int+Jnt)-(Dnt+Ent+Fnt+Gnt+Hnt+Knt+lnt);
		return String.valueOf(cnt);
	}
	
	
	public ContractDetailsRes contractDetails(ContractDetailsReq req) {
		ContractDetailsRes response = new ContractDetailsRes();
		ContractDetailsRes1 bean = new ContractDetailsRes1();
 		String[] args = null;
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- premium.select.treatyContDet1_xolLayerNo2
			list = xolPremiumCustomRepository.selectTreatyContDet1XolLayerNo2(req);
			if (list != null && list.size() > 0) {
				Tuple contDet = list.get(0);
				bean.setContNo(contDet.get("RSK_CONTRACT_NO") == null ? "" : contDet.get("RSK_CONTRACT_NO").toString());
				bean.setAmendId(
						contDet.get("RSK_ENDORSEMENT_NO") == null ? "" : contDet.get("RSK_ENDORSEMENT_NO").toString());
				bean.setProfitCenter(
						contDet.get("TMAS_PFC_NAME") == null ? "" : contDet.get("TMAS_PFC_NAME").toString());
				bean.setSubProfitcenter(
						contDet.get("TMAS_SPFC_NAME") == null ? "" : contDet.get("TMAS_SPFC_NAME").toString());
				bean.setCedingCo(contDet.get("COMPANY") == null ? "" : contDet.get("COMPANY").toString());
				bean.setBroker(contDet.get("BROKER") == null ? "" : contDet.get("BROKER").toString());
				bean.setTreatyNametype(
						contDet.get("RSK_TREATYID") == null ? "" : contDet.get("RSK_TREATYID").toString());
				bean.setProposalNo(contDet.get("RSK_PROPOSAL_NUMBER") == null ? ""
						: contDet.get("RSK_PROPOSAL_NUMBER").toString());
				bean.setUwYear(contDet.get("RSK_UWYEAR") == null ? "" : contDet.get("RSK_UWYEAR").toString());
				bean.setLayerno(contDet.get("RSK_LAYER_NO") == null ? "" : contDet.get("RSK_LAYER_NO").toString());
				bean.setInsDate(contDet.get("INS_DATE") == null ? "" : formatDate(contDet.get("INS_DATE")).toString());
				bean.setExpDate(contDet.get("EXP_DATE") == null ? "" : formatDate(contDet.get("EXP_DATE")).toString());
				bean.setMonth(contDet.get("MONTH") == null ? "" : contDet.get("MONTH").toString());
				bean.setBaseCurrencyId(
						contDet.get("RSK_ORIGINAL_CURR") == null ? "" : contDet.get("RSK_ORIGINAL_CURR").toString());
				bean.setBaseCurrencyName(
						contDet.get("CURRENCY_NAME") == null ? "" : contDet.get("CURRENCY_NAME").toString());
				//bean.setPolicyBranch(contDet.get("TMAS_POL_BRANCH_NAME") == null ? "": contDet.get("TMAS_POL_BRANCH_NAME").toString());
				bean.setAddress(contDet.get("Address") == null ? "" : contDet.get("Address").toString());
				bean.setCedingId(contDet.get("CEDING_ID") == null ? "" : contDet.get("CEDING_ID").toString());
				bean.setBrokerId(contDet.get("BROKER_ID") == null ? "" : contDet.get("BROKER_ID").toString());
				bean.setDepartmentId(contDet.get("RSK_DEPTID") == null ? "" : contDet.get("RSK_DEPTID").toString());
				bean.setDepartmentName(contDet.get("TMAS_DEPARTMENT_NAME") == null ? ""
						: contDet.get("TMAS_DEPARTMENT_NAME").toString());
				bean.setAcceptenceDate(contDet.get("RSK_ACCOUNT_DATE") == null ? ""
						: formatDate(contDet.get("RSK_ACCOUNT_DATE")).toString());
				bean.setVatRate(contDet.get("VAT_RATE") == null ? "0": fm.formattereight(contDet.get("VAT_RATE").toString()));
			}
			if (list != null && list.size() > 0)
				bean.setSaveFlag("true");
			args = new String[2];
			args[0] = bean.getProposalNo();
			args[1] = bean.getProposalNo();
			// query -- premium.select.commissionDetails
			list = xolPremiumCustomRepository.selectCommissionDetails(bean.getProposalNo());
			if (list != null && list.size() > 0) {
				Tuple commission = list.get(0);
				bean.setCommissionview(commission.get("RSK_COMM_QUOTASHARE") == null ? "": fm.formattereight(commission.get("RSK_COMM_QUOTASHARE").toString()));
				bean.setPremiumReserveview(commission.get("RSK_PREMIUM_RESERVE") == null ? "": fm.formattereight(commission.get("RSK_PREMIUM_RESERVE").toString()));
				bean.setLossreserveview(commission.get("RSK_LOSS_RESERVE") == null ? "": (commission.get("RSK_LOSS_RESERVE").toString()));
				bean.setProfitCommYN(commission.get("RSK_PROFIT_COMM") == null ? "" : (commission.get("RSK_PROFIT_COMM").toString()));
				bean.setCommissionSurbview(commission.get("RSK_COMM_SURPLUS") == null ? "": fm.formattereight(commission.get("RSK_COMM_SURPLUS").toString()));
				bean.setOverRiderview(commission.get("RSK_OVERRIDER_PERC") == null ? "": fm.formattereight(commission.get("RSK_OVERRIDER_PERC").toString()));
				bean.setBrokerageview(commission.get("RSK_BROKERAGE") == null ? "" : fm.formattereight(commission.get("RSK_BROKERAGE").toString()));
				bean.setTaxview(commission.get("RSK_TAX") == null ? "" : fm.formattereight(commission.get("RSK_TAX").toString()));
				bean.setOtherCostView(commission.get("RSK_OTHER_COST") == null ? "": fm.formattereight(commission.get("RSK_OTHER_COST").toString()));
			}
			args[0] = bean.getProposalNo();
			args[1] = bean.getProposalNo();

			// query -- premium.select.treatyXOLfacProposalDetails
			list = xolPremiumCustomRepository.selectTreatyXOLfacProposalDetails(bean.getProposalNo());
			if (list != null && list.size() > 0) {
				Tuple proposalDetails = list.get(0);
				bean.setShareSigned(proposalDetails.get("RSK_SHARE_SIGNED") == null ? "0": fm.formattereight(proposalDetails.get("RSK_SHARE_SIGNED").toString()));
				String mnd = proposalDetails.get("RSK_MD_PREM_OS_OC") == null ? "0": proposalDetails.get("RSK_MD_PREM_OS_OC").toString();
				String eps = (proposalDetails.get("RSK_EPI_OSOF_OC") == null ? "0": proposalDetails.get("RSK_EPI_OSOF_OC").toString());
				bean.setRdsExchageRate(proposalDetails.get("RSK_EXCHANGE_RATE") == null ? "": proposalDetails.get("RSK_EXCHANGE_RATE").toString());
				if (bean.getRdsExchageRate() != "0" && bean.getRdsExchageRate() != null) {
					double val = Double.parseDouble(eps) / Double.parseDouble(bean.getRdsExchageRate());
					double mndval = Double.parseDouble(mnd) / Double.parseDouble(bean.getRdsExchageRate());
					bean.setEPIourshareview(fm.formatter(Double.toString(val)));
					bean.setMdpremiumview(fm.formatter(Double.toString(mndval)));
				}
				bean.setAdjustmentpremiumtemp(proposalDetails.get("ADJ_PRE") == null ? "" : proposalDetails.get("ADJ_PRE").toString());
			}
			// query -- premium.select.currecy.name
			String currency = xolPremiumCustomRepository.selectCurrecyName(req.getBranchCode());
			bean.setCurrencyName(currency == null ? "" : currency);
			response.setCommonResponse(bean);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetRIPremiumListRes getRIPremiumList(GetRIPremiumListReq req) {
		GetRIPremiumListRes response = new GetRIPremiumListRes();
		List<GetRIPremiumListRes1> resList = new ArrayList<GetRIPremiumListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<RskPremiumDetailsRi> pm = query.from(RskPremiumDetailsRi.class);
						
			//reInsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(b1);
			
			Predicate a1 = cb.equal( pi.get("customerType"), "R");
			Predicate a2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(a1,a2,a3,a4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
			
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pis1.get("amendId")));
			Predicate c1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			maxAmend1.where(c1);
			
			Predicate d1 = cb.equal( pi1.get("customerType"), "B");
			Predicate d2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate d3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi1.get("amendId"), maxAmend1);
			brokerName.where(d1,d2,d3,d4);

			query.multiselect(reInsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("brokerId").alias("BROKER_ID"),pm.get("brokerage").alias("BROKERAGE"),
					pm.get("signShared").alias("SIGN_SHARED"),pm.get("reinsurerId").alias("REINSURER_ID"),pm.alias("table")); 
			
			Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
			Predicate n2 = cb.equal(pm.get("transactionNo"), req.getTransactionNo());
			Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple data1: list) {
					GetRIPremiumListRes1 res = new GetRIPremiumListRes1();
					RskPremiumDetailsRi data = (RskPremiumDetailsRi) data1.get("table");	
					
					res.setBrokerage(data1.get("BROKERAGE")==null?"":fm.formatter(data1.get("BROKERAGE").toString()));
					res.setBrokerId(data1.get("BROKER_ID")==null?"":data1.get("BROKER_ID").toString());
					res.setBrokerName(data1.get("BROKER_NAME")==null?"":data1.get("BROKER_NAME").toString());
					res.setReinsurerId(data1.get("REINSURER_ID")==null?"":data1.get("REINSURER_ID").toString());
					res.setReInsurerName(data1.get("REINSURER_NAME")==null?"":data1.get("REINSURER_NAME").toString());
					res.setSignShared(data1.get("SIGN_SHARED")==null?"":fm.formatter(data1.get("SIGN_SHARED").toString()));	
					
					res.setContNo(data.getContractNo()==null?"":data.getContractNo().toString());
					res.setTransactionNo(data.getTransactionNo()==null?"":data.getTransactionNo().toString());
					res.setTransaction(data.getTransactionMonthYear()==null?"":sdf.format(data.getTransactionMonthYear()));
					res.setBrokerage(data.getBrokerageAmtOc()==null?"":fm.formatter(data.getBrokerageAmtOc().toString()));
					res.setTax(data.getTax()==null?"":fm.formatter(data.getTax().toString()));
					res.setMdpremium(data.getMDpremiumOc()==null?"":fm.formatter(data.getMDpremiumOc().toString()));
					res.setAdjustmentpremium(data.getAdjustmentPremiumOc()	==null?"":fm.formatter(data.getAdjustmentPremiumOc().toString()));					
					res.setRecuirementpremium(data.getRecPremiumOc()==null?"":fm.formatter(data.getRecPremiumOc().toString()));
					res.setNetDue(data.getNetdueOc()==null?"":fm.formatter(data.getNetdueOc().toString()));
					res.setLayerno(data.getLayerNo()==null?"":data.getLayerNo().toString());
					res.setEnteringMode(data.getEnteringMode()==null?"":data.getEnteringMode().toString());
					res.setAccountPeriod(data.getInstalmentNumber()+(data.getAccountPeriodQtr()==null?"":("_"+data.getAccountPeriodQtr())));
					res.setCurrencyId(data.getCurrencyId()==null?"":data.getCurrencyId().toString());
					res.setOtherCost(data.getOtherCostOc()==null?"":fm.formatter(data.getOtherCostOc().toString()));
					res.setBrokerageusd(data.getBrokerageAmtDc()==null?"":fm.formatter(data.getBrokerageAmtDc().toString()));
					res.setTaxusd(data.getTaxAmtDc()==null?"":fm.formatter(data.getTaxAmtDc().toString()));
					res.setTaxOc(data.getTaxAmtOc()==null?"":fm.formatter(data.getTaxAmtOc().toString()));
					res.setMdpremiumusd(data.getMDpremiumDc()==null?"":fm.formatter(data.getMDpremiumDc().toString()));
					res.setAdjustmentpremiumusd(data.getAdjustmentPremiumDc()==null?"":fm.formatter(data.getAdjustmentPremiumDc().toString()));
					res.setRecuirementpremiumusd(data.getRecPremiumDc()==null?"":fm.formatter(data.getRecPremiumDc().toString()));
					res.setNetdueusd(data.getNetdueDc()==null?"":fm.formatter(data.getNetdueDc().toString()));
					res.setOtherCostUSD(data.getOtherCostDc()==null?"":fm.formatter(data.getOtherCostDc().toString()));
					res.setInceptionDate(data.getEntryDate()==null?"":sdf.format(data.getEntryDate()));
					res.setCedentRef(data.getCedantReference()==null?"":data.getCedantReference().toString());
					res.setRemarks(data.getRemarks()==null?"":data.getRemarks().toString());
					res.setTotalCredit(data.getTotalCrOc()==null?"":fm.formatter(data.getTotalCrOc().toString()));
					res.setTotalCreditDC(data.getTotalCrDc()==null?"":fm.formatter(data.getTotalCrDc().toString()));
					res.setTotalDebit(data.getTotalDrOc()==null?"":fm.formatter(data.getTotalDrOc().toString()));
					res.setTotalDebitDC(data.getTotalDrDc()==null?"":fm.formatter(data.getTotalDrDc().toString()));
					res.setAmendmentDate(data.getAmendmentDate()==null?"":sdf.format(data.getAmendmentDate()));
                    res.setWithHoldingTaxOC(data.getWithHoldingTaxOc()==null?"":fm.formatter(data.getWithHoldingTaxOc().toString()));
                    res.setWithHoldingTaxDC(data.getWithHoldingTaxDc()==null?"":fm.formatter(data.getWithHoldingTaxDc().toString()));
                 //   res.setDueDate(xolView.get("DUE_DATE")==null?"":xolView.get("DUE_DATE").toString());
                    res.setCreditsign(data.getNetdueOc()==null?"":fm.formatter(data.getNetdueOc().toString()));
                    res.setRicession(data.getRiCession()==null?"":data.getRiCession().toString());
                    res.setPredepartment(data.getPremiumClass()==null?"":data.getPremiumClass().toString());
                    res.setDepartmentId(data.getSubClass()==null?"":data.getSubClass().toString());
                    res.setTaxDedectSource(data.getTdsOc()==null?"":fm.formatter(data.getTdsOc().toString()));
                    res.setTaxDedectSourceDc(data.getTdsDc()==null?"":fm.formatter(data.getTdsDc().toString()));
                    res.setVatPremiumOc(data.getVatPremiumOc()==null?"":fm.formatter(data.getVatPremiumOc().toString()));
                    res.setDocumentType(data.getDocumentType()==null?"":data.getDocumentType().toString());
                    res.setVatPremiumDc(data.getVatPremiumDc()==null?"": fm.formatter(data.getVatPremiumDc().toString()));
                    res.setBonus(data.getBonusOc()==null?"":fm.formatter(data.getBonusOc().toString()));
                    res.setBonusDc(data.getBonusDc()==null?"":fm.formatter(data.getBonusDc().toString()));
    				res.setExchRate(dropDowmImpl.exchRateFormat(data.getExchangeRate()==null?"":data.getExchangeRate().toString()));
                    res.setGnpiDate(data.getGnpiEndtNo()==null?"":sdf.format(data.getGnpiEndtNo()));
                    res.setStatementDate(data.getStatementDate()==null?"":sdf.format(data.getStatementDate()));
                    res.setChooseTransaction(data.getReverselStatus()==null?"":data.getReverselStatus().toString());
                    res.setTransDropDownVal(data.getReverseTransactionNo()==null?"":data.getReverseTransactionNo().toString());
                    res.setBrokerageVatOc(data.getBrokerageVatOc()==null?"":fm.formatter(data.getBrokerageVatOc().toString()));
					res.setBrokerageVatDc(data.getBrokerageVatDc()==null?"":fm.formatter(data.getBrokerageVatDc().toString()));
					resList.add(res);
				}
			}		
			response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		    return response;
	}

	public List<Map<String, Object>> getSPRetroList(String contNo){
     	List<Map<String, Object>> list= new ArrayList<Map<String,Object>>();
     	try{
					String query="premium.select.getTreatySPRetro";
					list=queryImpl.selectList(query,new String[] {contNo});	
				}catch(Exception e){
					e.printStackTrace();
				}
				return list;
	}

	public List<Map<String, Object>> getRetroContracts(String proposalNo, String noOfRetro) {
		List<Map<String, Object>> list= new ArrayList<Map<String,Object>>();
		
		try{
			String query="premium.select.insDetails";
			String[] args = new String [2];
			args[0] = proposalNo;
			args[1] = noOfRetro;
			list=queryImpl.selectList(query,args);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	public String getSumOfShareSign(String contractNo){
		List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		int shareSign = 0;
		String sumOfShareSign = "";
		try {
			String query ="premium.select.getNoRetroCess";
			 list = queryImpl.selectList(query, new String [] {contractNo});
			 if (!CollectionUtils.isEmpty(list)) {
				 shareSign = Integer.valueOf(list.get(0).get("RETRO_CESSIONARIES") == null ? "": list.get(0).get("RETRO_CESSIONARIES").toString())-1;
				} 
			 query="premium.select.getSumOfShareSign";
			list = queryImpl.selectList(query, new String [] {contractNo,String.valueOf(shareSign)});
			 if (!CollectionUtils.isEmpty(list)) {
				 sumOfShareSign = list.get(0).get("SHARE_SIGNED") == null ? "": list.get(0).get("SHARE_SIGNED").toString();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return sumOfShareSign;
	}

	public String getMovementReportMaxDate(String branchCode) {
		String maxDate = "";
		String query = "premium.mov.rep.max.date";
		try {
			List<Map<String,Object>> list = queryImpl.selectList(query, new String[] {branchCode});
			if (!CollectionUtils.isEmpty(list)) {
				maxDate = list.get(0).get("MAX_DATE") == null ? "" : list.get(0).get("MAX_DATE").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxDate;
	}

	public String getRPPremiumOC(String contractNo, String layerNo,String productId){
		List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		String premiumOC ="";
		try {
			String query="";
			if("3".equalsIgnoreCase(productId)){
				query="premium.select.RPPremiumOC";
			}else{
			 query="XOL_PREMIUM_SELECT_RPPREMIUMOC";
			}
			 list = queryImpl.selectList(query, new String [] {contractNo,layerNo});
			 if (!CollectionUtils.isEmpty(list)) {
					premiumOC = list.get(0).get("REC_PREMIUM_OC") == null ? "": list.get(0).get("REC_PREMIUM_OC").toString();
				} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return premiumOC;
	}

	@Override
	public GetPremiumDetailsRes getPremiumDetailsRi(GetPremiumDetailsReq req) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();
		String output="";
		List<Tuple> list = null;
		try{
			
					list = xolPremiumCustomRepository.premiumSelectXolPremiumViewRi(req.getBranchCode(),
							req.getContNo(), req.getProductId(), req.getTransactionNo());
			
		    if(list!=null && list.size()>0) {
				Tuple xolView= list.get(0);
				bean.setRiBroker(xolView.get("RI_BROKER_NAME")==null?"":xolView.get("RI_BROKER_NAME").toString());
				bean.setReinsuranceName(xolView.get("RI_COMPANY_NAME")==null?"":xolView.get("RI_COMPANY_NAME").toString());
							bean.setContNo(xolView.get("CONTRACT_NO")==null?"":xolView.get("CONTRACT_NO").toString());
							bean.setTransactionNo(xolView.get("RI_TRANSACTION_NO")==null?"":xolView.get("RI_TRANSACTION_NO").toString());
							bean.setTransaction(xolView.get("TRANS_DATE")==null?"":formatDate(xolView.get("TRANS_DATE")).toString()); 
							bean.setBrokerage(xolView.get("BROKERAGE_AMT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_OC").toString()));
							bean.setTax(xolView.get("TAX_AMT_OC")==null?"":fm.formatter(xolView.get("TAX_AMT_OC").toString()));
							bean.setMdpremium(xolView.get("M_DPREMIUM_OC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_OC").toString()));
							bean.setAdjustmentpremium(xolView.get("ADJUSTMENT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_OC").toString()));							
							bean.setRecuirementpremium(xolView.get("REC_PREMIUM_OC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_OC").toString()));
							bean.setNetDue(xolView.get("NETDUE_OC")==null?"":fm.formatter(xolView.get("NETDUE_OC").toString()));
							bean.setLayerno(xolView.get("LAYER_NO")==null?"":xolView.get("LAYER_NO").toString());
							bean.setEnteringMode(xolView.get("ENTERING_MODE")==null?"":xolView.get("ENTERING_MODE").toString());
							bean.setAccountPeriod(xolView.get("INSTALMENT_NUMBER")+(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":("_"+xolView.get("ACCOUNT_PERIOD_QTR"))));
							bean.setCurrencyId(xolView.get("CURRENCY_ID")==null?"":xolView.get("CURRENCY_ID").toString());
							bean.setOtherCost(xolView.get("OTHER_COST_OC")==null?"":fm.formatter(xolView.get("OTHER_COST_OC").toString()));
							bean.setBrokerageusd(xolView.get("BROKERAGE_AMT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_AMT_DC").toString()));
							bean.setTaxusd(xolView.get("TAX_AMT_DC")==null?"":fm.formatter(xolView.get("TAX_AMT_DC").toString()));
							bean.setMdpremiumusd(xolView.get("M_DPREMIUM_DC")==null?"":fm.formatter(xolView.get("M_DPREMIUM_DC").toString()));
							bean.setAdjustmentpremiumusd(xolView.get("ADJUSTMENT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("ADJUSTMENT_PREMIUM_DC").toString()));
							bean.setRecuirementpremiumusd(xolView.get("REC_PREMIUM_DC")==null?"":fm.formatter(xolView.get("REC_PREMIUM_DC").toString()));
							bean.setNetdueusd(xolView.get("NETDUE_DC")==null?"":fm.formatter(xolView.get("NETDUE_DC").toString()));
							bean.setOtherCostUSD(xolView.get("OTHER_COST_DC")==null?"":fm.formatter(xolView.get("OTHER_COST_DC").toString()));
							bean.setInceptionDate(xolView.get("ENTRY_DATE_TIME")==null?"":formatDate(xolView.get("ENTRY_DATE_TIME")).toString());
							bean.setCedentRef(xolView.get("CEDANT_REFERENCE")==null?"":xolView.get("CEDANT_REFERENCE").toString());
							bean.setRemarks(xolView.get("REMARKS")==null?"":xolView.get("REMARKS").toString());
							bean.setTotalCredit(xolView.get("TOTAL_CR_OC")==null?"":fm.formatter(xolView.get("TOTAL_CR_OC").toString()));
							bean.setTotalCreditDC(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_CR_DC").toString()));
							bean.setTotalDebit(xolView.get("TOTAL_CR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_OC").toString()));
							bean.setTotalDebitDC(xolView.get("TOTAL_DR_DC")==null?"":fm.formatter(xolView.get("TOTAL_DR_DC").toString()));
							bean.setAmendmentDate(xolView.get("AMENDMENT_DATE")==null?"":formatDate(xolView.get("AMENDMENT_DATE")).toString());
                            bean.setWithHoldingTaxOC(xolView.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_OC").toString()));
                            bean.setWithHoldingTaxDC(xolView.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(xolView.get("WITH_HOLDING_TAX_DC").toString()));
		                   
                            if ("Temp".equalsIgnoreCase(req.getTableType()) && "3".equalsIgnoreCase(req.getProductId())|| "".equalsIgnoreCase(req.getTransactionNo())) {
		                            bean.setDueDate(xolView.get("TRANS_DATE")==null?"":fm.Transadded(xolView.get("TRANS_DATE")).toString());
		                    }else {
		                    	bean.setDueDate(xolView.get("ACCOUNT_PERIOD_QTR")==null?"":(xolView.get("ACCOUNT_PERIOD_QTR")).toString());
		                    }
                            
                            bean.setCreditsign(xolView.get("NETDUE_OC")==null?"":xolView.get("NETDUE_OC").toString());
                            bean.setRicession(xolView.get("RI_CESSION")==null?"":xolView.get("RI_CESSION").toString());
                            bean.setPredepartment(xolView.get("PREMIUM_CLASS")==null?"":xolView.get("PREMIUM_CLASS").toString());
                            bean.setDepartmentId(xolView.get("SUB_CLASS")==null?"":xolView.get("SUB_CLASS").toString());
                            bean.setTaxDedectSource(xolView.get("TDS_OC")==null?"":fm.formatter(xolView.get("TDS_OC").toString()));
                            bean.setTaxDedectSourceDc(xolView.get("TDS_DC")==null?"":fm.formatter(xolView.get("TDS_DC").toString()));
                            bean.setVatPremium(xolView.get("VAT_PREMIUM_OC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_OC").toString()));
                            bean.setVatPremiumDc(xolView.get("VAT_PREMIUM_DC")==null?"":fm.formatter(xolView.get("VAT_PREMIUM_DC").toString()));
                            bean.setBrokerageVat(xolView.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_OC").toString()));
                            bean.setBrokerageVatDc(xolView.get("BROKERAGE_VAT_DC")==null?"":fm.formatter(xolView.get("BROKERAGE_VAT_DC").toString()));
                            bean.setDocumentType(xolView.get("DOCUMENT_TYPE")==null?"":xolView.get("DOCUMENT_TYPE").toString());
                            bean.setBonus(xolView.get("BONUS_OC")==null?"":fm.formatter(xolView.get("BONUS_OC").toString()));
                            bean.setBonusDc(xolView.get("BONUS_DC")==null?"":fm.formatter(xolView.get("BONUS_DC").toString()));
            				bean.setExchRate(dropDowmImpl.exchRateFormat(xolView.get("EXCHANGE_RATE")==null?"":xolView.get("EXCHANGE_RATE").toString()));
                            bean.setGnpiDate(xolView.get("GNPI_ENDT_NO")==null?"":xolView.get("GNPI_ENDT_NO").toString());
                            bean.setStatementDate(xolView.get("STATEMENT_DATE")==null?"":formatDate(xolView.get("STATEMENT_DATE")).toString());
                            bean.setChooseTransaction(xolView.get("REVERSEL_STATUS")==null?"":xolView.get("REVERSEL_STATUS").toString());
                            bean.setTransDropDownVal(xolView.get("REVERSE_TRANSACTION_NO")==null?"":xolView.get("REVERSE_TRANSACTION_NO").toString());
						}				
		   	if(StringUtils.isNotBlank(bean.getCurrencyId())){
				// query -- premium.select.currency
		   		output = xolPremiumCustomRepository.premiumSelectCurrency(bean.getCurrencyId(),req.getBranchCode());
				bean.setCurrency(output == null ? "" : output);
		   	}
		   	
		   	//query -- premium.select.currecy.name
		   	output = xolPremiumCustomRepository.selectCurrecyName(req.getBranchCode());
			bean.setCurrencyName(output == null ? "" : output);

			if ("3".equalsIgnoreCase(req.getProductId())) {
				// query -- GETSETTLEMET_STATUS
				List<Tuple> premlist = xolPremiumCustomRepository.getSettlementStatus(req.getContNo());
				List<SettlementstatusRes> res1List = new ArrayList<SettlementstatusRes>();
				if (premlist.size() > 0) {
					for (int i = 0; i < premlist.size(); i++) {
						Tuple map = premlist.get(i);
						SettlementstatusRes res1 = new SettlementstatusRes();
						String allocate = map.get("ALLOCATED_TILL_DATE") == null ? "0"
								: map.get("ALLOCATED_TILL_DATE").toString();
						String net = map.get("NETDUE_OC").toString();
						if ("0".equalsIgnoreCase(allocate)) {
							res1.setSettlementstatus("Pending");
						} else if (Double.parseDouble(allocate) == Double.parseDouble(net)) {
							res1.setSettlementstatus("Allocated");
						} else {
							res1.setSettlementstatus("Partially Allocated");
						}
						res1List.add(res1);
					}
					bean.setSettlementstatusRes(res1List);
				}
			}
		   	response.setCommonResponse(bean);
		   	response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public PremiumEditRes premiumEditRi(PremiumEditReq req) {
		PremiumEditRes response = new PremiumEditRes();
		PremiumEditResponse res1 = new PremiumEditResponse();
		 List<PremiumEditRes1> resList = new ArrayList<PremiumEditRes1>();
		 List<Tuple> transList=new ArrayList<>();
		 try {
					transList = xolPremiumCustomRepository.selectTreetyXOLPremiumEditRi(req.getContNo(),req.getTransactionNo());
					 if(!CollectionUtils.isEmpty(transList)){
						for(int i=0;i<transList.size();i++){
							PremiumEditRes1 bean = new PremiumEditRes1();
							Tuple	 editPremium=transList.get(i);
							bean.setTransaction(editPremium.get("TRANS_DATE")==null?"":formatDate(editPremium.get("TRANS_DATE")).toString()); 
							bean.setAccountPeriod(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString());
							bean.setAccountPeriodyear(editPremium.get("ACCOUNT_PERIOD_YEAR")==null?"":editPremium.get("ACCOUNT_PERIOD_YEAR").toString());
							bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
							bean.setCurrency(editPremium.get("CURRENCY_ID").toString()==null?"":editPremium.get("CURRENCY_ID").toString());
							if(null==editPremium.get("EXCHANGE_RATE")){
								GetCommonValueRes common = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
								bean.setExchRate(common.getCommonResponse());
							}
							else{
							bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"0":editPremium.get("EXCHANGE_RATE").toString()));
							}
							bean.setBrokerage(editPremium.get("BROKERAGE_AMT_OC")==null?"0":fm.formatter(editPremium.get("BROKERAGE_AMT_OC").toString()));
							bean.setTax(editPremium.get("TAX_AMT_OC")==null?"0":fm.formatter(editPremium.get("TAX_AMT_OC").toString()));
							bean.setPremiumQuotaShare(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString());
							bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
							bean.setPremiumSurplus(editPremium.get("PREMIUM_SURPLUS_OC")==null?"":editPremium.get("PREMIUM_SURPLUS_OC").toString());
							bean.setCommissionSurplus(editPremium.get("COMMISSION_SURPLUS_OC")==null?"":editPremium.get("COMMISSION_SURPLUS_OC").toString());
							bean.setPremiumportifolioIn(editPremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
							bean.setCliamPortfolioin(editPremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editPremium.get("CLAIM_PORTFOLIOIN_OC").toString());
							bean.setPremiumportifolioout(editPremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editPremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
							bean.setLossReserveReleased(editPremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editPremium.get("LOSS_RESERVE_RELEASED_OC").toString());
							bean.setPremiumReserveQuotaShare(editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
							bean.setCashLossCredit(editPremium.get("CASH_LOSS_CREDIT_OC")==null?"":editPremium.get("CASH_LOSS_CREDIT_OC").toString());
							bean.setLossReserveRetained(editPremium.get("LOSS_RESERVERETAINED_OC")==null?"":editPremium.get("LOSS_RESERVERETAINED_OC").toString());
							bean.setProfitCommission(editPremium.get("PROFIT_COMMISSION_OC")==null?"":editPremium.get("PROFIT_COMMISSION_OC").toString());
							bean.setCashLossPaid(editPremium.get("CASH_LOSSPAID_OC")==null?"":editPremium.get("CASH_LOSSPAID_OC").toString());
							bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
							bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
							bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
							bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());
							bean.setClaimspaid(editPremium.get("CLAIMS_PAID_OC")==null?"":editPremium.get("CLAIMS_PAID_OC").toString());				 
						    bean.setMdpremium(fm.formatter(editPremium.get("M_DPREMIUM_OC")==null?"":editPremium.get("M_DPREMIUM_OC").toString()));
						    bean.setAdjustmentpremium(editPremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editPremium.get("ADJUSTMENT_PREMIUM_OC").toString());
						    bean.setRecuirementpremium(editPremium.get("REC_PREMIUM_OC")==null?"":editPremium.get("REC_PREMIUM_OC").toString());
						    bean.setCommission(editPremium.get("COMMISSION")==null?"":editPremium.get("COMMISSION").toString());
						    bean.setInstlmentNo(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
					    	if("RP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())||"AP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()) ||"RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
					    	{
					    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
					    	}else
					    	{
					    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")+"_"+editPremium.get("ACCOUNT_PERIOD_QTR"));
					    	}					
						    bean.setInceptionDate(editPremium.get("ENTRY_DATE_TIME")==null?"":formatDate(editPremium.get("ENTRY_DATE_TIME")).toString());
						    bean.setXlCost(editPremium.get("XL_COST_OC")==null?"":editPremium.get("XL_COST_OC").toString());
						    bean.setCliamportfolioout(editPremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editPremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
						    bean.setPremiumReserveReleased(editPremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editPremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
						    bean.setOtherCost(editPremium.get("OTHER_COST_OC")==null?"":fm.formatter(editPremium.get("OTHER_COST_OC").toString()));
						    bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
							bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
							bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
							bean.setInterest(editPremium.get("INTEREST_OC")==null?"0":fm.formatter(editPremium.get("INTEREST_OC").toString()));
							bean.setOsClaimsLossUpdateOC(editPremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editPremium.get("OSCLAIM_LOSSUPDATE_OC").toString());
							bean.setOverrider(editPremium.get("OVERRIDER_AMT_OC")==null?"":editPremium.get("OVERRIDER_AMT_OC").toString());
							bean.setOverriderUSD(editPremium.get("OVERRIDER_AMT_DC")==null?"":editPremium.get("OVERRIDER_AMT_DC").toString());	
							bean.setAmendmentDate(editPremium.get("AMENDMENT_DATE")==null?"":formatDate(editPremium.get("AMENDMENT_DATE")));	
	                        bean.setWithHoldingTaxOC(editPremium.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC").toString()));
	                        bean.setWithHoldingTaxDC(editPremium.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(editPremium.get("WITH_HOLDING_TAX_DC").toString()));
	                        bean.setRicession(editPremium.get("RI_CESSION")==null?"":editPremium.get("RI_CESSION").toString());
	                        bean.setPredepartment(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
	                        bean.setDepartmentId(editPremium.get("SUB_CLASS")==null?"":editPremium.get("SUB_CLASS").toString());
	                        bean.setTaxDedectSource(editPremium.get("TDS_OC")==null?"":fm.formatter(editPremium.get("TDS_OC").toString()));
	                        bean.setTaxDedectSourceDc(editPremium.get("TDS_DC")==null?"":fm.formatter(editPremium.get("TDS_DC").toString()));
	                        bean.setVatPremium(editPremium.get("VAT_PREMIUM_OC")==null?"":fm.formatter(editPremium.get("VAT_PREMIUM_OC").toString()));
	                        bean.setVatPremiumDc(editPremium.get("VAT_PREMIUM_DC")==null?"":fm.formatter(editPremium.get("VAT_PREMIUM_DC").toString()));
	                        bean.setBrokerageVat(editPremium.get("BROKERAGE_VAT_OC")==null?"":fm.formatter(editPremium.get("BROKERAGE_VAT_OC").toString()));
	                        bean.setBrokerageVatDc(editPremium.get("BROKERAGE_VAT_DC")==null?"":fm.formatter(editPremium.get("BROKERAGE_VAT_DC").toString()));
	                        bean.setDocumentType(editPremium.get("DOCUMENT_TYPE")==null?"":editPremium.get("DOCUMENT_TYPE").toString());
	                        bean.setBonus(editPremium.get("BONUS_OC")==null?"":fm.formatter(editPremium.get("BONUS_OC").toString()));
	                        bean.setBonusDc(editPremium.get("BONUS_DC")==null?"":fm.formatter(editPremium.get("BONUS_DC").toString()));
	                        bean.setGnpiDate((editPremium.get("GNPI_ENDT_NO")==null?"":editPremium.get("GNPI_ENDT_NO").toString()));
	                        bean.setStatementDate(editPremium.get("STATEMENT_DATE")==null?"":formatDate(editPremium.get("STATEMENT_DATE")).toString());
	                        bean.setChooseTransaction(editPremium.get("REVERSEL_STATUS")==null?"":editPremium.get("REVERSEL_STATUS").toString() );
		       	            bean.setTransDropDownVal(editPremium.get("REVERSE_TRANSACTION_NO")==null?"":editPremium.get("REVERSE_TRANSACTION_NO").toString() );
							resList.add(bean);
						}
						res1.setPremiumEditRes1(resList);
					 }
			
				
				if(transList!=null && transList.size()>0)
					res1.setSaveFlag("true");
				response.setCommonResponse(res1);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;

	}

	@Override
	public CommonResponse premiumUpdateMethodRi(PremiumInsertMethodReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
			String[] args = updateAruguments(beanObj);
					xolPremiumCustomRepository.premiumUpdateXolUpdatePreRi(args);
			
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
}
