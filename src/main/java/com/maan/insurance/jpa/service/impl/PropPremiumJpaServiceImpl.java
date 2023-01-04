package com.maan.insurance.jpa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.propPremium.TtrnDepositRelease;
import com.maan.insurance.jpa.mapper.RskPremiumDetailsMapper;
import com.maan.insurance.jpa.mapper.RskPremiumDetailsTempMapper;
import com.maan.insurance.jpa.mapper.TtrnDepositReleaseMapper;
import com.maan.insurance.jpa.repository.propPremium.PropPremiumCustomRepository;
import com.maan.insurance.jpa.repository.propPremium.TtrnDepositReleaseRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnAllocatedTransactionRepository;
import com.maan.insurance.jpa.repository.xolpremium.XolPremiumCustomRepository;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsRi;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.repository.RskPremiumDetailsRIRepository;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.RskPremiumDetailsTempRepository;
import com.maan.insurance.model.repository.TmasBranchMasterRepository;
import com.maan.insurance.model.repository.TmasDepartmentMasterRepository;
import com.maan.insurance.model.repository.TtrnClaimDetailsRepository;
import com.maan.insurance.model.repository.TtrnClaimPaymentRepository;
import com.maan.insurance.model.req.premium.CashLossmailTriggerReq;
import com.maan.insurance.model.req.premium.ClaimTableListReq;
import com.maan.insurance.model.req.premium.ContractDetailsReq;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq1;
import com.maan.insurance.model.req.premium.GetCassLossCreditReq;
import com.maan.insurance.model.req.premium.GetConstantPeriodDropDownReq;
import com.maan.insurance.model.req.premium.GetPreListReq;
import com.maan.insurance.model.req.premium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.premium.GetSPRetroListReq;
import com.maan.insurance.model.req.premium.GetVatInfoReq;
import com.maan.insurance.model.req.premium.InsertLossReserved;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.premium.InsertReverseCashLossCreditReq;
import com.maan.insurance.model.req.premium.PremiumEditReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq1;
import com.maan.insurance.model.res.ClaimlistRes;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.SettlementstatusRes;
import com.maan.insurance.model.res.premium.ClaimNosListRes;
import com.maan.insurance.model.res.premium.ClaimTableListMode1Res;
import com.maan.insurance.model.res.premium.ContractDetailsRes;
import com.maan.insurance.model.res.premium.ContractDetailsRes1;
import com.maan.insurance.model.res.premium.ContractDetailsRes2;
import com.maan.insurance.model.res.premium.ContractDetailsResponse;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes1;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes1;
import com.maan.insurance.model.res.premium.GetAllocatedListRes;
import com.maan.insurance.model.res.premium.GetAllocatedListRes1;
import com.maan.insurance.model.res.premium.GetAllocatedListRes2;
import com.maan.insurance.model.res.premium.GetAllocatedTransListRes;
import com.maan.insurance.model.res.premium.GetAllocatedTransListRes1;
import com.maan.insurance.model.res.premium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.premium.GetBrokerAndCedingNameRes1;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes1;
import com.maan.insurance.model.res.premium.GetClaimNosDropDownRes;
import com.maan.insurance.model.res.premium.GetConstantPeriodDropDownRes;
import com.maan.insurance.model.res.premium.GetConstantPeriodDropDownRes1;
import com.maan.insurance.model.res.premium.GetContractPremiumRes;
import com.maan.insurance.model.res.premium.GetCountCleanCUTRes;
import com.maan.insurance.model.res.premium.GetDepartmentNoRes;
import com.maan.insurance.model.res.premium.GetDepositReleaseCountRes;
import com.maan.insurance.model.res.premium.GetOSBListRes;
import com.maan.insurance.model.res.premium.GetPreListRes;
import com.maan.insurance.model.res.premium.GetPreListRes1;
import com.maan.insurance.model.res.premium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes1;
import com.maan.insurance.model.res.premium.GetPremiumedListRes;
import com.maan.insurance.model.res.premium.GetPremiumedListRes1;
import com.maan.insurance.model.res.premium.GetPreviousPremiumRes;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes;
import com.maan.insurance.model.res.premium.GetRetroContractsRes;
import com.maan.insurance.model.res.premium.GetRetroContractsRes1;
import com.maan.insurance.model.res.premium.GetSPRetroListRes;
import com.maan.insurance.model.res.premium.GetSPRetroListRes1;
import com.maan.insurance.model.res.premium.GetSumOfShareSignRes;
import com.maan.insurance.model.res.premium.GetVatInfoRes;
import com.maan.insurance.model.res.premium.GetVatInfoRes1;
import com.maan.insurance.model.res.premium.InsertPremiumRes;
import com.maan.insurance.model.res.premium.InsertPremiumRes1;
import com.maan.insurance.model.res.premium.PremiumEditRes;
import com.maan.insurance.model.res.premium.PremiumEditRes1;
import com.maan.insurance.model.res.premium.SubmitPremiumReservedRes;
import com.maan.insurance.model.res.premium.SubmitPremiumReservedRes1;
import com.maan.insurance.model.res.premium.SubmitPremiumReservedResponse;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIReq;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIRes;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIRes1;
import com.maan.insurance.model.res.premium.ViewRIPremiumListRes;
import com.maan.insurance.model.res.premium.ViewRIPremiumListRes1;
import com.maan.insurance.model.res.premium.getCurrencyShortNameRes;
import com.maan.insurance.model.res.premium.getReverseCassLossCreditRes;
import com.maan.insurance.model.res.premium.premiumUpdateMethodRes;
import com.maan.insurance.model.res.premium.premiumUpdateMethodRes1;
import com.maan.insurance.model.res.proportionality.CommonSaveRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.premium.GetPremiumDetailsRes1;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.premium.PropPremiumService;
import com.maan.insurance.validation.Formatters;

@Component
public class PropPremiumJpaServiceImpl implements PropPremiumService{
	private Logger log = LogManager.getLogger(PropPremiumJpaServiceImpl.class);

	@Autowired
	PropPremiumCustomRepository propPremiumCustomRepository;
	
	@Autowired
	private TtrnClaimDetailsRepository claimDetailsRepo;
	
	@Autowired
	private TtrnClaimPaymentRepository claimPayRepo;
	
	@Autowired
	private DropDownServiceImple  dropDownImple;
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	RskPremiumDetailsMapper rskPremiumDetailsMapper;
	
	@Autowired
	RskPremiumDetailsRepository rskPremiumDetailsRepository;
	
	@Autowired
	RskPremiumDetailsTempMapper rskPremiumDetailsTempMapper;
	
	@Autowired
	RskPremiumDetailsTempRepository rskPremiumDetailsTempRepository;
	
	@Autowired
	TtrnDepositReleaseMapper ttrnDepositReleaseMapper;
	
	@Autowired
	TtrnDepositReleaseRepository ttrnDepositReleaseRepository;
	
	@Autowired
	private TmasDepartmentMasterRepository dmRepo;
	@Autowired
	private RskPremiumDetailsTempRepository pdTempRepo;
	@Autowired
	
	private XolPremiumCustomRepository xolPremiumCustomRepository;
	@Autowired
	private  TtrnAllocatedTransactionRepository ttrnallocateRepo;
	@Autowired
	private RskPremiumDetailsRepository pdRepo;
	@Autowired
	private QueryImplemention queryImpl;
	@Autowired
	private RskPremiumDetailsRIRepository pdRIRepo;
	@Autowired
	private TmasBranchMasterRepository bmRepo;
	
	@PersistenceContext
	private EntityManager em;
	private Query query=null;
	private String formatDate(Object input) {
		return new SimpleDateFormat("dd/MM/yyyy").format(input).toString();
	}
	@Override
	public GetPremiumedListRes getPremiumedList(GetPremiumedListReq req) {
		GetPremiumedListRes response = new GetPremiumedListRes();
		List<GetPremiumedListRes1> finalList = new ArrayList<GetPremiumedListRes1>();
		List<Tuple> list = new ArrayList<>();
		try {
			if ("Main".equalsIgnoreCase(req.getType())) {
				list = propPremiumCustomRepository.premiumSelectPremiumedList(req);

			} else {
				list = propPremiumCustomRepository.pityPremiumListTemp(req);
			}

			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				GetPremiumedListRes1 tempreq = new GetPremiumedListRes1();
				if ("Main".equalsIgnoreCase(req.getType())) {
					tempreq.setTransactionNo(tempMap.get("TRANSACTION_NO") == null ? "" : tempMap.get("TRANSACTION_NO").toString());
				}else {
					tempreq.setRequestNo(tempMap.get("REQUEST_NO")==null?"":tempMap.get("REQUEST_NO").toString());
				}
				tempreq.setContNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
				tempreq.setAccountPeriod(tempMap.get("ACC_PER") == null ? "" : tempMap.get("ACC_PER").toString());
				tempreq.setAccountPeriodDate(tempMap.get("ACCOUNTING_PERIOD_DATE") == null ? ""
						: formatDate(tempMap.get("ACCOUNTING_PERIOD_DATE")).toString());
				tempreq.setStatementDate(tempMap.get("STATEMENT_DATE") == null ? ""
						: formatDate(tempMap.get("STATEMENT_DATE")).toString());
				tempreq.setTransDate(tempMap.get("TRANSACTION_DATE") == null ? ""
						: formatDate(tempMap.get("TRANSACTION_DATE")).toString());
				if((StringUtils.isNotBlank(req.getOpstartDate()))&& (StringUtils.isNotBlank(req.getOpendDate()))){
					if(dropDownImple.Validatethree(req.getBranchCode(), tempreq.getTransDate())==0){
						tempreq.setTransOpenperiodStatus("N");
					}else
					{
						tempreq.setTransOpenperiodStatus("Y");
					}
				}
				if(StringUtils.isNotBlank(tempreq.getTransactionNo())) {
					int count=ttrnallocateRepo.countByContractNoAndTransactionNoAndLayerNoAndTypeAndStatus(tempreq.getContNo(),new BigDecimal(tempreq.getTransactionNo()),"0","P","Y");
					tempreq.setAllocatedYN(count==0?"Y":"N");
				}
				tempreq.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
				tempreq.setSectionNo(tempMap.get("SECTION_NO")==null?"":tempMap.get("SECTION_NO").toString());
				finalList.add(tempreq);

			}
			response.setCommonResponse(finalList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;

	}
	@Override
	public GetPreListRes getPreList(GetPreListReq req) {
		GetPreListRes response = new GetPreListRes();
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- premium.select.facTreatyPreList
			list = propPremiumCustomRepository.premiumSelectFacTreatyPreList(req.getContNo(), req.getDepartmentId());

			if (list != null && list.size() > 0) {
				Tuple tempMap = list.get(0);
				GetPreListRes1 res = new GetPreListRes1();
				res.setContNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
				res.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? ""
						: tempMap.get("TMAS_DEPARTMENT_NAME").toString());
				res.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
				res.setCedingCompanyName(
						tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
				res.setBrokerName(tempMap.get("Broker_name") == null ? "" : tempMap.get("Broker_name").toString());

				response.setCommonResponse(res);
				response.setMessage("Success");
				response.setIsError(false);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetConstantPeriodDropDownRes getConstantPeriodDropDown(GetConstantPeriodDropDownReq req) {
		GetConstantPeriodDropDownRes response = new GetConstantPeriodDropDownRes();
		
		List<GetConstantPeriodDropDownRes1> res1List = new ArrayList<GetConstantPeriodDropDownRes1>();
		List<Tuple> constantList=new ArrayList<>();
		List<Map<String,Object>> res=new ArrayList<Map<String,Object>>();
		List<Tuple> result = new ArrayList<>();
		boolean val = false;
		boolean preval=false;
		boolean lossval=false;
		String slide  = "";
		String combine ="";
		String premium ="";
		String preCombine="";
		String loss ="";
		String lossCombine="";
		String DeptNo ="";
		String proposalNo = "";
		String base="";
		
		String accPeriod="";
		String output = null;
		List<Map<String,Object>> mapObjectList = new ArrayList<Map<String,Object>>();
		
		try{
			
			// query  -- GET_ACC_PERIOD
			output = propPremiumCustomRepository.getAccPeriod(req.getProposalNo());
			accPeriod = output == null ? ""	: output;
			
			// query -- COMMON_SELECT_GETCONSTDET_PTTY
			constantList = propPremiumCustomRepository.commonSelectGetconstdetPity(req.getCategoryId(), "Y", accPeriod);
			
			//query -- GET_BASE_LAYER
			result = propPremiumCustomRepository.getBaseLayer(req.getContractNo(), req.getSectionNo());
			
            for(int i=0;i<result.size();i++) {
                Tuple map = result.get(i);
              
                proposalNo = map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
                base = map.get("BASE_LAYER")==null?"":map.get("BASE_LAYER").toString();
            }
			if(base.equalsIgnoreCase("0")){
				
				// query  -- GET_SLIDE_COMM_VALUE
                result = propPremiumCustomRepository.getSlideCommValue(proposalNo, req.getDepartmentId());
				for(int i=0;i<result.size();i++){
					Tuple map = result.get(i);
					 slide  = map.get("RSK_SLADSCALE_COMM")==null?"":map.get("RSK_SLADSCALE_COMM").toString();
					 combine  = map.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"":map.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString();
					 premium = map.get("RSK_PROFIT_COMM")==null?"":map.get("RSK_PROFIT_COMM").toString();
					 preCombine =map.get("RSK_COMBIN_SUB_CLASS")==null?"":map.get("RSK_COMBIN_SUB_CLASS").toString();
					 loss = map.get("RSK_LOSS_PART_CARRIDOR")==null?"":map.get("RSK_LOSS_PART_CARRIDOR").toString();
					 lossCombine =map.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":map.get("RSK_LOSS_COMBIN_SUB_CLASS").toString();
					if(slide.equalsIgnoreCase("Y")){
						val = true ;
                        if(combine.equalsIgnoreCase("1")){
                        	response.setSlideScenario("one");
                        }
                        else if(combine.equalsIgnoreCase("2")){
                        	response.setSlideScenario("two");
                        }
					}
					if(premium.equalsIgnoreCase("1")){
						preval=true;
						if(preCombine.equalsIgnoreCase("1")){
							response.setSlideScenario("one");
						}
						else if(preCombine.equalsIgnoreCase("2")){
							response.setSlideScenario("two");
                        }
					}
					if(loss.equalsIgnoreCase("Y")){
						lossval=true;
						if(lossCombine.equalsIgnoreCase("1")){
							response.setSlideScenario("one");
						}
						else if(lossCombine.equalsIgnoreCase("2")){
							response.setSlideScenario("two");
                        }
					}
				}
			}
			else{
				
				// query  -- GET_DEPT_ID
				if(!"0".equals(base)) {
					output = propPremiumCustomRepository.getDeptId(base);
	                DeptNo  = output ==null?"":output;
	                
	                result = propPremiumCustomRepository.getSlideCommValue(base, DeptNo);
				}
				for(int i=0;i<result.size();i++){
                    Tuple map = result.get(i);
					 slide  = map.get("RSK_SLADSCALE_COMM")==null?"":map.get("RSK_SLADSCALE_COMM").toString();
					 combine  = map.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"":map.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString();
					 premium = map.get("RSK_PROFIT_COMM")==null?"":map.get("RSK_PROFIT_COMM").toString();
					 preCombine =map.get("RSK_COMBIN_SUB_CLASS")==null?"":map.get("RSK_COMBIN_SUB_CLASS").toString();
					 loss = map.get("RSK_LOSS_PART_CARRIDOR")==null?"":map.get("RSK_LOSS_PART_CARRIDOR").toString();
					 lossCombine =map.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":map.get("RSK_LOSS_COMBIN_SUB_CLASS").toString();
				}
				if(combine.equalsIgnoreCase("2") || preCombine.equalsIgnoreCase("2")|| lossCombine.equalsIgnoreCase("2")) {
                  
					// query  -- GET_SLIDE_COMM_VALUE2
                    propPremiumCustomRepository.getSlideCommValue2(proposalNo, req.getDepartmentId());
					for (int i = 0; i < result.size(); i++) {
                        Tuple map =result.get(i);
						 slide  = map.get("RSK_SLADSCALE_COMM")==null?"":map.get("RSK_SLADSCALE_COMM").toString();
						 premium = map.get("RSK_PROFIT_COMM")==null?"":map.get("RSK_PROFIT_COMM").toString();
						 loss = map.get("RSK_LOSS_PART_CARRIDOR")==null?"":map.get("RSK_LOSS_PART_CARRIDOR").toString();
						if(slide.equalsIgnoreCase("Y") ){
							val = true ;
							response.setSlideScenario("three");
						}
						if(premium.equalsIgnoreCase("1")){
							preval= true;
							response.setSlideScenario("three");
						}
						if(loss.equalsIgnoreCase("Y") ){
							lossval = true ;
							response.setSlideScenario("three");
						}
					}
				}
			}
			if(!val) {
				for(int i=0; i< constantList.size(); i++) {
					Map<String, Object> mapObj = new HashMap<>(); 
					mapObj.put("TYPE", constantList.get(i).get("TYPE"));
					mapObj.put("DETAIL_NAME", constantList.get(i).get("DETAIL_NAME"));
					mapObjectList.add(mapObj);
				}
				
                for (int i = 0; i < mapObjectList.size(); i++) {
                    Map<String, Object> val1 = mapObjectList.get(i);
                    String type = val1.get("TYPE")==null?"":val1.get("TYPE").toString();
                    if (type.equalsIgnoreCase("8")) {
                        val1.remove(i);
                    } else {
                        res.add(val1);
                    }
                }
                mapObjectList = res;
            }
			if(!preval){
				res=new ArrayList<Map<String,Object>>();
				 for (int i = 0; i < mapObjectList.size(); i++) {
	                    Map<String, Object> val1 = mapObjectList.get(i);
	                    String type = val1.get("TYPE")==null?"":val1.get("TYPE").toString();
	                    if (type.equalsIgnoreCase("7")) {
	                        val1.remove(i);
	                    } else {
	                        res.add(val1);
	                    }
	                }
				 mapObjectList = res;
			}
			if(!lossval){
				res=new ArrayList<Map<String,Object>>();
				 for (int i = 0; i < mapObjectList.size(); i++) {
	                    Map<String, Object> val1 = mapObjectList.get(i);
	                    String type = val1.get("TYPE")==null?"":val1.get("TYPE").toString();
	                    if (type.equalsIgnoreCase("9")) {
	                        val1.remove(i);
	                    } else {
	                        res.add(val1);
	                    }
	                }
				 mapObjectList = res;
			}
			for (int i = 0; i < mapObjectList.size(); i++) {
				Map<String,Object> tempMap = (Map<String,Object>) mapObjectList.get(i);
				GetConstantPeriodDropDownRes1 res2 = new GetConstantPeriodDropDownRes1();
				res2.setPremiumType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());
				res2.setDetailName(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
				res1List.add(res2);
			}
			response.setCommonResponse(res1List);
			response.setMessage("Success");
			response.setIsError(false);
	    }catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	
	}
	@Override
	public GetPreviousPremiumRes getPreviousPremium(String contractNo) {
		GetPreviousPremiumRes response = new GetPreviousPremiumRes();
		String premium = "";
		try {
			//query -- select.premium.sum
			List<BigDecimal> list1 = propPremiumCustomRepository.selectPremiumSum(contractNo);
			//Double premiumSOC = list1.stream().filter(o -> o != null).mapToDouble(o -> o.doubleValue()).sum(); // loop
			BigDecimal premiumSOC = list1.stream().filter(o -> o !=null).reduce(BigDecimal.ZERO, BigDecimal::add);
			premium = String.valueOf(premiumSOC);
			response.setCommonResponse(premium);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetContractPremiumRes getContractPremium(String contractNo, String departmentId, String branchCode) {
		GetContractPremiumRes response = new GetContractPremiumRes();
		try {
			// query -- GET_CONT_PREM
			List<Tuple> list1 = propPremiumCustomRepository.getContPrem(contractNo, departmentId, branchCode);
			if(list1.size() > 0) {
			response.setCommonResponse(
					list1.get(0).get("rskEpiOsoeOc") == null ? "" : list1.get(0).get("rskEpiOsoeOc").toString());
			}
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetClaimNosDropDownRes getClaimNosDropDown(String contractNo) {
		GetClaimNosDropDownRes response = new GetClaimNosDropDownRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		List<ClaimNosListRes> resList = new ArrayList<ClaimNosListRes>();
		try {
			List<TtrnClaimDetails> list1 = claimDetailsRepo.findByContractNoOrderByClaimNo(contractNo);

			for (TtrnClaimDetails data : list1) {
				ClaimNosListRes claimRes = new ClaimNosListRes();
				dozerMapper.map(data, claimRes);
				resList.add(claimRes);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;

	}
	@Override
	public ClaimTableListMode1Res claimTableListMode1(ClaimTableListReq req) {
		log.info("CliamBusinessImpl cliamTableList || Enter");
		List<ClaimlistRes> cliamlists = new ArrayList<ClaimlistRes>();
		List<Tuple> list;
		ClaimTableListMode1Res res = new ClaimTableListMode1Res();
		try {
			
			// query  -- claim.select.claimTableList
			list = propPremiumCustomRepository.claimSelectClaimTableList(
					ttrnDepositReleaseMapper.formatBigDecimal(req.getPolicyContractNo()), req.getLayerNo(),
					req.getDepartmentId());
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				ClaimlistRes cliam = new ClaimlistRes();
				cliam.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
				cliam.setDateOfLoss(tempMap.get("DATE_OF_LOSS") == null ? "" : tempMap.get("DATE_OF_LOSS").toString());
				cliam.setCreatedDate(tempMap.get("CREATED_DATE") == null ? "" : tempMap.get("CREATED_DATE").toString());
				cliam.setStatusOfClaim(
						tempMap.get("STATUS_OF_CLAIM") == null ? "" : tempMap.get("STATUS_OF_CLAIM").toString());
				cliam.setPolicyContractNo(
						tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
				cliam.setEditMode(tempMap.get("EDITVIEW") == null ? "" : tempMap.get("EDITVIEW").toString());
				int count = Integer.valueOf(dropDownImple.Validatethree(req.getBranchCode(), cliam.getCreatedDate()));
				int claimpaymentcount = Integer
						.valueOf(getCliampaymnetCount(cliam.getClaimNo(), cliam.getPolicyContractNo()));
		if (count != 0 && claimpaymentcount == 0) {
				cliam.setDeleteStatus("Y");
				}
				cliamlists.add(cliam);
			}
			res.setCommonResponse(cliamlists);
			res.setMessage("Success");
			res.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
	
	}
	public String getCliampaymnetCount(String claimNo, String contNo) {
		String result = "";
		try {
			Long list = claimPayRepo.countByClaimNoAndContractNo(BigDecimal.valueOf(Double.valueOf(claimNo)), contNo);
			result = String.valueOf(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception @ {" + e + "}");
		}
		return result;
	}
	@Override
	public GetRetroContractsRes getRetroContracts(String proposalNo, String noOfRetro) {
		GetRetroContractsRes response = new GetRetroContractsRes();
		List<GetRetroContractsRes1> resList = new ArrayList<GetRetroContractsRes1>();
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- premium.select.insDetails
			list = propPremiumCustomRepository.premiumSelectFacTreatyPreList(proposalNo, noOfRetro);
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);

				GetRetroContractsRes1 res = new GetRetroContractsRes1();
				res.setContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
				res.setRetroPercentage(
						tempMap.get("RETRO_PERCENTAGE") == null ? "" : tempMap.get("RETRO_PERCENTAGE").toString());
				res.setType(tempMap.get("TYPE") == null ? "" : tempMap.get("TYPE").toString());
				res.setUwyear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
				res.setRetroType(tempMap.get("RETRO_TYPE") == null ? "" : tempMap.get("RETRO_TYPE").toString());
				resList.add(res);
			}

			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetSumOfShareSignRes getSumOfShareSign(String retroContractNo) {
		GetSumOfShareSignRes response = new GetSumOfShareSignRes();
		String sumShareSigned = "0";
		try {
			sumShareSigned = propPremiumCustomRepository.getSumOfShareSign(retroContractNo);
			response.setCommonResponse(sumShareSigned == null ? "0" : sumShareSigned);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetDepartmentNoRes getDepartmentNo(String contractNo) {
		GetDepartmentNoRes response = new GetDepartmentNoRes();
		String deptNo = "";
		try {
			// Criteria
			deptNo = propPremiumCustomRepository.getDepartmentNo(contractNo);
			response.setCommonResponse(deptNo);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetBrokerAndCedingNameRes getBrokerAndCedingName(String contNo, String branchCode) {
		GetBrokerAndCedingNameRes response = new GetBrokerAndCedingNameRes();
		List<GetBrokerAndCedingNameRes1> resList = new ArrayList<GetBrokerAndCedingNameRes1>();
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- broker.ceding.name
			list = propPremiumCustomRepository.brokerCedingName(contNo, branchCode);
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				GetBrokerAndCedingNameRes1 temp = new GetBrokerAndCedingNameRes1();
				temp.setCutomerId(tempMap.get("CUSTOMER_ID") == null ? "" : tempMap.get("CUSTOMER_ID").toString());
				temp.setCompanyName(tempMap.get("BROKER") == null ? "" : tempMap.get("BROKER").toString());
				temp.setAddress(tempMap.get("ADDRESS") == null ? "" : tempMap.get("ADDRESS").toString());
				resList.add(temp);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetAllocatedListRes getAllocatedList(String contNo, String transactionNo) {
		GetAllocatedListRes response = new GetAllocatedListRes();
		List<GetAllocatedListRes1> resList = new ArrayList<GetAllocatedListRes1>();
		GetAllocatedListRes2 res2 = new GetAllocatedListRes2();
		
		Double a=0.0;
		Integer count = 1;
		try{
			
			// query -- payment.select.getAlloTransaction
			List<Tuple> list = propPremiumCustomRepository.premiumSelectGetAlloTransaction(contNo, transactionNo);
			
			if (list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					
					Tuple tempMap = list.get(i);
					GetAllocatedListRes1 tempreq = new GetAllocatedListRes1();
					tempreq.setSerialNo((count++).toString());
					tempreq.setAllocatedDate(tempMap.get("INCEPTION_DATE")==null?"":formatDate(tempMap.get("INCEPTION_DATE")).toString());
					tempreq.setProductName(tempMap.get("PRODUCT_NAME")==null?"":tempMap.get("PRODUCT_NAME").toString());
					tempreq.setType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());
					tempreq.setPayAmount(tempMap.get("PAID_AMOUNT")==null?"":tempMap.get("PAID_AMOUNT").toString());
					tempreq.setCurrencyValue(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					tempreq.setAlloccurrencyId(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					tempreq.setAllocateType(tempMap.get("ADJUSTMENT_TYPE")==null?"":tempMap.get("ADJUSTMENT_TYPE").toString());
					tempreq.setStatus((tempMap.get("STATUS")==null?"":tempMap.get("STATUS").toString()));
					tempreq.setPayRecNo(tempMap.get("RECEIPT_NO")==null?"":tempMap.get("RECEIPT_NO").toString());
					tempreq.setSettlementType(tempMap.get("TRANS_TYPE")==null?"":tempMap.get("TRANS_TYPE").toString());
					if(tempreq.getPayRecNo()!=""){
						tempreq.setAllocateType(tempMap.get("ALLOCATE_TYPE")==null?"":tempMap.get("ALLOCATE_TYPE").toString());
				
					}
					a=a+Double.parseDouble(tempMap.get("PAID_AMOUNT")==null?"":tempMap.get("PAID_AMOUNT").toString());
					resList.add(tempreq);
				}
			}
			if (a > 0) {
				res2.setTotalAmount(fm.formatter(Double.toString(a)));
			} else {
				res2.setTotalAmount("");
			}
			res2.setCommonResponse(resList);
			response.setCommonResponse(res2);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		    return response;

	}
	@Override
	public CurrencyListRes currencyList(String branchCode) {
		CurrencyListRes response = new CurrencyListRes();
		List<CurrencyListRes1> resList = new ArrayList<CurrencyListRes1>();
		List<Map<String, Object>> list = null;
		try {
			// query -- currency.list
			list = propPremiumCustomRepository.currencyList(branchCode);
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
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;

	}
	@Override
	public GetCashLossCreditRes getCassLossCredit(GetCassLossCreditReq req) {
		GetCashLossCreditRes response = new GetCashLossCreditRes();
		List<GetCashLossCreditRes1> cashLossList = new ArrayList<GetCashLossCreditRes1>();
		Double a = 0.0;
		Double b = 0.0;
		List<Tuple> list = null;
		String currency = null;
		String excessRatePercent = "";

		List<GetCashLossCreditReq1> req1 = req.getGetCashLossCreditReq1();
		try {

			// query --GET_CASH_LOSS_CREADIT
			list = propPremiumCustomRepository.getCashLossCreadt(req.getContNo(), req.getDepartmentId(),
					req.getClaimPayNo());

			// Query -- GET_EXCESS_RATE_PERCENT
			excessRatePercent = propPremiumCustomRepository.getExcessRatePercent();
			excessRatePercent = excessRatePercent == null ? "" : excessRatePercent;

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Tuple tempMap = list.get(i);
					GetCashLossCreditRes1 tempreq = new GetCashLossCreditRes1();
					tempreq.setSerialNo(tempMap.get("SNO") == null ? "" : tempMap.get("SNO").toString());
					tempreq.setContNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
					tempreq.setPaidDate(tempMap.get("INCEPTION_DATE") == null ? ""
							: formatDate(tempMap.get("INCEPTION_DATE")).toString());
					tempreq.setClaimNumber(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
					tempreq.setClaimPaymentNo(
							tempMap.get("CLAIM_PAYMENT_NO") == null ? "" : tempMap.get("CLAIM_PAYMENT_NO").toString());
					tempreq.setPayAmount(fm.formatter(
							tempMap.get("PAID_AMOUNT_OC") == null ? "" : tempMap.get("PAID_AMOUNT_OC").toString()));
					tempreq.setCurrencyValue(
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempreq.setExcessRatePercent(excessRatePercent);
					tempreq.setCurrencyId(req.getCurrencyId());

					// query -- GET_CURRENCY_NAME
					currency = propPremiumCustomRepository.getCurrencyName(req.getBranchCode(),
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempreq.setCurrencyValueName(currency == null ? "" : currency);

					// query -- GET_CURRENCY_NAME
					currency = propPremiumCustomRepository.getCurrencyName(req.getBranchCode(), req.getCurrencyId());
					tempreq.setCurrencyIdName(currency == null ? "" : currency);

					if (req1.size() > i) {
						if (StringUtils.isNotBlank(req1.get(i).getMainclaimPaymentNos()) && req1.get(i)
								.getMainclaimPaymentNos().contains(tempMap.get("CLAIM_PAYMENT_NO") == null ? ""
										: tempMap.get("CLAIM_PAYMENT_NO").toString())) {
							if (tempreq.getCurrencyValue().equalsIgnoreCase(tempreq.getCurrencyId())) {
								tempreq.setStatus("true");
							}
							tempreq.setCreditAmountCLC(fm.formatter(req1.get(i).getMaincreditAmountCLClist()));
							tempreq.setCreditAmountCLD(fm.formatter(req1.get(i).getMaincreditAmountCLDlist()));
							tempreq.setCLCsettlementRate(req1.get(i).getMainCLCsettlementRatelist());
							tempreq.setCheck("true");
						}
					} else if (StringUtils.isNotBlank(req.getMode()) && "error".equals(req.getMode())) {
						if (tempreq.getCurrencyValue().equalsIgnoreCase(tempreq.getCurrencyId())) {
							tempreq.setStatus("true");
						}
						if (req1.get(i).getCreditAmountCLClist() != null
								&& req1.get(i).getCreditAmountCLClist().length() > 0
								&& StringUtils.isNotBlank(req1.get(i).getCreditAmountCLClist())) {
							tempreq.setCreditAmountCLC((req1.get(i).getCreditAmountCLClist()));
						} else {
							tempreq.setCreditAmountCLC("");
						}
						if (req1.get(i).getCreditAmountCLDlist() != null
								&& req1.get(i).getCreditAmountCLDlist().length() > 0
								&& StringUtils.isNotBlank(req1.get(i).getCreditAmountCLDlist())) {
							tempreq.setCreditAmountCLD((req1.get(i).getCreditAmountCLDlist()));
						} else {
							tempreq.setCreditAmountCLD("");
						}
						if (req1.get(i).getCLCsettlementRatelist() != null
								&& req1.get(i).getCLCsettlementRatelist().length() > 0
								&& StringUtils.isNotBlank(req1.get(i).getCLCsettlementRatelist())) {
							tempreq.setCLCsettlementRate(req1.get(i).getCLCsettlementRatelist());
						} else {
							tempreq.setCLCsettlementRate("");
						}
					} else {
						if (tempreq.getCurrencyValue().equalsIgnoreCase(tempreq.getCurrencyId())) {
							tempreq.setStatus("true");
							tempreq.setCreditAmountCLCTemp(fm.formatter(tempMap.get("PAID_AMOUNT_OC") == null ? ""
									: tempMap.get("PAID_AMOUNT_OC").toString()));
							tempreq.setCreditAmountCLDTemp(tempreq.getCreditAmountCLCTemp());
							a = Double.parseDouble(tempMap.get("PAID_AMOUNT_OC") == null ? "0"
									: tempMap.get("PAID_AMOUNT_OC").toString());
							b = Double.parseDouble(tempreq.getCreditAmountCLDTemp().replace(",", ""));
							String c = Double.toString(a / b);
							tempreq.setCLCsettlementRateTemp(c);
							tempreq.setCreditAmountCLC("");
							tempreq.setCreditAmountCLD("");
							tempreq.setCLCsettlementRate("");
						} else {
							tempreq.setCreditAmountCLC("");
							tempreq.setCreditAmountCLD("");
							tempreq.setCLCsettlementRate("");
						}
					}
					if ((req1.get(i).getChkbox() != null) && req1.get(i).getChkbox().equalsIgnoreCase("true")) {
						tempreq.setCheck("true");
					}
					cashLossList.add(tempreq);
				}
			}
			response.setCommonResponse(cashLossList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetAllocatedTransListRes getAllocatedTransList(String proposalNo) {
		GetAllocatedTransListRes response = new GetAllocatedTransListRes();
		List<GetAllocatedTransListRes1> result = new ArrayList<GetAllocatedTransListRes1>();
		List<Tuple> list = new ArrayList<>();
		try {
			// query -- GET_ALLOCATED_TRANS_LIST
			list = propPremiumCustomRepository.getAllocatedTransList(proposalNo);

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					GetAllocatedTransListRes1 res = new GetAllocatedTransListRes1();
					Tuple tempMap = list.get(i);

					res.setCreditTrxnNO(
							tempMap.get("CREDITTRXNNO") == null ? "" : tempMap.get("CREDITTRXNNO").toString());
					res.setContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
					result.add(res);

				}
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetAllocatedCassLossCreditRes getAllocatedCassLossCredit(String proposalNo, String branchCode) {
		GetAllocatedCassLossCreditRes response = new GetAllocatedCassLossCreditRes();
		List<GetAllocatedCassLossCreditRes1> result = new ArrayList<GetAllocatedCassLossCreditRes1>();

		List<Tuple> list = new ArrayList<>();
		String currency = null;

		try {
			// query -- GET_ALLOCATED_CASH_LOSS
			list = propPremiumCustomRepository.getAllocatedCasLoss(proposalNo);

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					GetAllocatedCassLossCreditRes1 res = new GetAllocatedCassLossCreditRes1();
					Tuple tempMap = list.get(i);

					res.setCreditTrxnNo(
							tempMap.get("CREDITTRXNNO") == null ? "" : tempMap.get("CREDITTRXNNO").toString());
					res.setContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
					res.setClaimNo(tempMap.get("CLAIM_NO") == null ? "" : tempMap.get("CLAIM_NO").toString());
					res.setClaimPaymentNo(
							tempMap.get("CLAIMPAYMENT_NO") == null ? "" : tempMap.get("CLAIMPAYMENT_NO").toString());
					res.setCreditDate(
							tempMap.get("CREDITDATE") == null ? "" : formatDate(tempMap.get("CREDITDATE")).toString());
					res.setCldAmount(tempMap.get("CLD_AMOUNT") == null ? "" : tempMap.get("CLD_AMOUNT").toString());

					// query -- GET_CURRENCY_NAME
					currency = propPremiumCustomRepository.getCurrencyName(branchCode,
							tempMap.get("CLDCURRENCY_ID") == null ? "" : tempMap.get("CLDCURRENCY_ID").toString());
					res.setCldCurrencyId(currency == null ? "" : currency);

					// query -- GET_CURRENCY_NAME
					currency = propPremiumCustomRepository.getCurrencyName(branchCode,
							tempMap.get("CLCCURRENCY_ID") == null ? "" : tempMap.get("CLCCURRENCY_ID").toString());
					res.setCldCurrencyId(currency == null ? "" : currency);
					res.setClcCurrencyId(currency == null ? "" : currency);

					res.setCreditAmountClc(
							tempMap.get("CREDITAMOUNTCLC") == null ? "" : tempMap.get("CREDITAMOUNTCLC").toString());
					res.setCreditAmountCld(
							tempMap.get("CREDITAMOUNTCLD") == null ? "" : tempMap.get("CREDITAMOUNTCLD").toString());
					res.setExchangeRate(
							tempMap.get("EXCHANGE_RATE") == null ? "" : tempMap.get("EXCHANGE_RATE").toString());

					result.add(res);
				}
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public SubmitPremiumReservedRes submitPremiumReserved(SubmitPremiumReservedReq req) {
		SubmitPremiumReservedRes response = new SubmitPremiumReservedRes();
		List<SubmitPremiumReservedRes1> resList = new ArrayList<SubmitPremiumReservedRes1>();
		SubmitPremiumReservedResponse res1 = new SubmitPremiumReservedResponse();
		Map<String, String> cashLossCreditMap = new HashMap<String, String>();
		try {
			double credit = 0.00;

			if (req.getReqList() != null && req.getReqList().size() > 0) {
				for (int i = 0; i < req.getReqList().size(); i++) {
					SubmitPremiumReservedReq1 req1 = req.getReqList().get(i);
					if ("true".equalsIgnoreCase(req1.getChkbox())) {
						List<SubmitPremiumReservedReq1> filterTrack = req.getReqList().stream()
								.filter(o -> req1.getClaimPaymentNos().equalsIgnoreCase(o.getClaimPaymentNos()))
								.collect(Collectors.toList());
						if (!CollectionUtils.isEmpty(filterTrack)) {
							filterTrack.remove(0).getClaimPaymentNos();
							cashLossCreditMap.put(req1.getClaimPaymentNos(),
									req1.getCreditAmountCLC().replace(",", "") + "~"
											+ req1.getCreditAmountCLD().replace(",", "") + "~"
											+ req1.getCLCsettlementRate().replace(",", ""));
							credit = credit + Double.parseDouble(req1.getCreditAmountCLC().replace(",", ""));

						}

						if (!CollectionUtils.isEmpty(filterTrack)) {
							cashLossCreditMap.remove(req1.getClaimPaymentNos());
							cashLossCreditMap.put(req1.getClaimPaymentNos(),
									req1.getCreditAmountCLC().replace(",", "") + "~"
											+ req1.getCreditAmountCLD().replace(",", "") + "~"
											+ req1.getCLCsettlementRate().replace(",", ""));
							credit = credit + Double.parseDouble(req1.getCreditAmountCLC().replace(",", ""));
						} else {
							cashLossCreditMap.put(req1.getClaimPaymentNos(),
									req1.getCreditAmountCLC().replace(",", "") + "~"
											+ req1.getCreditAmountCLD().replace(",", "") + "~"
											+ req1.getCLCsettlementRate().replace(",", ""));
							credit = credit + Double.parseDouble(req1.getCreditAmountCLC().replace(",", ""));
						}
					}
				}
			}
			String value1 = "";
			String value2 = "";
			String value3 = "";
			String value4 = "";
			GetPremiumReservedReq req1 = new GetPremiumReservedReq();
			GetPremiumReservedReq req2 = req.getPremiumReservedReq();
			req1.setBranchCode(req2.getBranchCode());
			req1.setContNo(req2.getContNo());
			req1.setCurrencyId(req2.getCurrencyId());
			req1.setDepartmentId(req2.getDepartmentId());
			req1.setPrTransNo(req2.getPrTransNo());
			req1.setTransaction(req2.getTransaction());
			req1.setType(req2.getType());

			GetPremiumReservedRes1 cashLossList = getPremiumReserved(req1);

			for (int i = 0; i < cashLossList.getCommonResponse().size(); i++) {
				GetPremiumReservedRes form = cashLossList.getCommonResponse().get(i);
				SubmitPremiumReservedRes1 res = new SubmitPremiumReservedRes1();
				if ((cashLossCreditMap).containsKey(form.getTransactionNo())) {
					String string3 = cashLossCreditMap.get(form.getTransactionNo());
					String[] cashloss = string3.split("~");
					res.setValue1(value1 + form.getTransactionNo() + ",");
					res.setValue2(value2 + cashloss[0] + ",");
					res.setValue3(value3 + cashloss[1] + ",");
					res.setValue4(value4 + cashloss[2] + ",");

				} else {
					res.setValue1(value1 + ",");
					res.setValue2(value2 + ",");
					res.setValue3(value3 + ",");
					res.setValue4(value4 + ",");
				}
				resList.add(res);
			}
			res1.setCommonResponse1(resList);
			res1.setCreditVal(fm.formatter(Double.toString(credit)));

			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetPremiumReservedRes1 getPremiumReserved(GetPremiumReservedReq req) {
		GetPremiumReservedRes1 response = new GetPremiumReservedRes1();
		List<GetPremiumReservedRes> cashLossList = new ArrayList<GetPremiumReservedRes>();
		List<Tuple> list = null;
		String shortName = null;
		try {
			String[] args = new String[3];
			args[0] = req.getContNo();
			args[1] = req.getDepartmentId();
			args[2] = req.getTransaction();
			if ("PRR".equals(req.getType())) {
				// query -- GET_PREMIUM_RESERVED_DETAILS
				list = propPremiumCustomRepository.getPremiumReservedDetails(req);
			} else if ("LRR".equals(req.getType())) {
				// query -- selectQry = "GET_LOSS_RESERVED_DETAILS";
				list = propPremiumCustomRepository.getLossReservedDetails(req);
			}

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Tuple tempMap = list.get(i);
					GetPremiumReservedRes tempreq = new GetPremiumReservedRes();
					tempreq.setSerialno(String.valueOf(i+1));
					tempreq.setContNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
					tempreq.setTransactionNo(
							tempMap.get("TRANSACTION_NO") == null ? "" : tempMap.get("TRANSACTION_NO").toString());
					tempreq.setPaidDate(tempMap.get("TRANSACTION_MONTH_YEAR") == null ? ""
							: formatDate(tempMap.get("TRANSACTION_MONTH_YEAR")).toString());
					tempreq.setPayamount(fm.formatter(tempMap.get("PREMIUMRESERVE_QUOTASHARE_OC") == null ? ""
							: tempMap.get("PREMIUMRESERVE_QUOTASHARE_OC").toString()));
					tempreq.setCurrencyValue(
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempreq.setCurrencyId(req.getCurrencyId());
					tempreq.setPrallocatedTillDate(tempMap.get("ALLOCATE_TILLDATE") == null ? ""
							: tempMap.get("ALLOCATE_TILLDATE").toString());

					// query -- GET_CURRENCY_NAME
					shortName = propPremiumCustomRepository.getCurrencyName(req.getBranchCode(),
							tempMap.get("CURRENCY_ID") == null ? "" : tempMap.get("CURRENCY_ID").toString());
					tempreq.setCurrencyValueName(shortName == null ? "" : shortName);

					// query -- GET_CURRENCY_NAME
					shortName = propPremiumCustomRepository.getCurrencyName(req.getBranchCode(), req.getCurrencyId());
					tempreq.setCurrencyIdName(shortName == null ? "" : shortName);

					cashLossList.add(tempreq);
				}

			}

			response.setCommonResponse(cashLossList);

			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetDepositReleaseCountRes getDepositReleaseCount(String dropDown, String contractNo, String branchCode,
			String type) {
		GetDepositReleaseCountRes response = new GetDepositReleaseCountRes();
		int res = 0;
		String count = null;
		try {
			if ("cashcountStatus".equalsIgnoreCase(dropDown)) {
				// query -- GET_COUNT_CASHLOSS_PREM
				count = propPremiumCustomRepository.getCountCashlossPrem(contractNo, branchCode, "P");
			} else {
				// query -- GET_COUNT_DEPOSIT_PREM
				count = propPremiumCustomRepository.getCountDepositPrem(contractNo, branchCode, "P", type);
			}
			res = Integer.valueOf(count == null ? "" : count);
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetCountCleanCUTRes getCountCleanCUT(String contractNo) {
		GetCountCleanCUTRes response = new GetCountCleanCUTRes();
		int count = 0;
		try {
			// query -- GET_CLEAN_CUT_CONT_COUNT
			count = propPremiumCustomRepository.getCleanCutContCount(contractNo);
			response.setCommonResponse(String.valueOf(count));
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetOSBListRes getOSBList(String transaction, String contractNo, String branchCode) {
		GetOSBListRes response = new GetOSBListRes();
		List<Map<String, Object>> list = null;
		double sum = 0.00;
		try {
			// Query -- GET_OBS_LIST
			list = propPremiumCustomRepository.getOSBList(transaction, contractNo, branchCode);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> tempMap = list.get(i);
					sum += Double.parseDouble(tempMap.get("OSCLAIM_LOSSUPDATE_OC") == null ? "0"
							: tempMap.get("OSCLAIM_LOSSUPDATE_OC").toString());
				}
			}
			response.setCommonResponse(Double.toString(sum));
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	public GetSPRetroListRes getSPRetroList(GetSPRetroListReq req) {
		GetSPRetroListRes response = new GetSPRetroListRes();
		DozerBeanMapper dozerMapper = new DozerBeanMapper();
		List<Tuple> resList = new ArrayList<>();
		List<GetSPRetroListRes1> responseList = new ArrayList<>();
		try {
			// query -- premium.select.getTreatySPRetro
			resList = propPremiumCustomRepository.premiumSelectGetTreatySPRetro(req.getContNo());
			if (resList.size() > 0) {
				for (int i = 0; i < resList.size(); i++) {
					Tuple tempMap = resList.get(i);
					GetSPRetroListRes1 res = new GetSPRetroListRes1();
					res.setNoOfInsurers(tempMap.get("RSK_NO_OF_INSURERS") == null ? ""
							: tempMap.get("RSK_NO_OF_INSURERS").toString());
					res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER") == null ? ""
							: tempMap.get("RSK_PROPOSAL_NUMBER").toString());
					res.setSpRetro(tempMap.get("RSK_SP_RETRO") == null ? "" : tempMap.get("RSK_SP_RETRO").toString());
					responseList.add(res);

				}
			}

			response.setCommonResponse(responseList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Override
	@Transactional
	public InsertPremiumRes insertPremium(InsertPremiumReq req) {
		InsertPremiumRes response = new InsertPremiumRes();
		InsertPremiumRes1 res = new InsertPremiumRes1();
		try {
				String[] args = insertArguments(req);
			 	
			 	// query -- PREMIUM_INSERT_TREATYPREMIUM_TEMP
			 	RskPremiumDetailsTemp entity = rskPremiumDetailsTempMapper.toTempEntity(args);
			 	res.setRequestNo(req.getRequestNo());
			 	rskPremiumDetailsTempRepository.save(entity);
			 	
			 	if("submit".equalsIgnoreCase(req.getButtonStatus())){
			 		req.setTransactionNo(fm.getSequence("Premium",req.getProductId(),req.getDepartmentId(), req.getBranchCode(),"",req.getTransaction()));
					// query -- FAC_TEMP_STATUS_UPDATE
			 		propPremiumCustomRepository.facTempStatusUpdate(req);
			 		res.setTransactionNo(req.getTransactionNo());
			 		getTempToMainMove(req);
			 	}
				InsertPremiumReserved(req);
				InsertLossReserved(req);
				response.setCommonResponse(res);
				response.setMessage("Success");
				response.setIsError(false);
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	private String[] insertArguments(InsertPremiumReq req) {

		String[] args=null;
		args=new String[100];
		double premiumsurpInsert=0.0;
		double premiumInsert=0.0;
	    args[0]=req.getContNo();
	    args[1] = getRequestNo(req);
		
		args[2]=req.getTransaction();
		args[3]=req.getAccountPeriod();
		args[4]=req.getAccountPeriodyear();
		args[5]=req.getCurrencyId();
		args[6]=req.getExchRate();
		args[7]=req.getBrokerageview();
		args[8]=getModeOfTransaction(req.getBrokerage(),req);
		args[35]=dropDownImple.GetDesginationCountry(args[8], req.getExchRate());
		args[9]=req.getTaxview();
		args[10]=getModeOfTransaction(req.getTax(),req);
		args[36]=dropDownImple.GetDesginationCountry(args[10], req.getExchRate());
		args[67]=getModeOfTransaction(req.getOverrider(),req);
		args[68]=dropDownImple.GetDesginationCountry(args[67],req.getExchRate());
		args[69]=req.getOverRiderview();
        args[70]=getModeOfTransaction(req.getWithHoldingTaxOC(),req);
        args[71]=dropDownImple.GetDesginationCountry(args[70], req.getExchRate());
		args[11]=StringUtils.isEmpty(req.getInceptionDate()) ?"" :req.getInceptionDate();
		args[12]=getModeOfTransaction(req.getPremiumQuotaShare(),req);
		args[37]=dropDownImple.GetDesginationCountry(args[12], req.getExchRate());
		args[13]=getModeOfTransaction(req.getCommissionQuotaShare(),req);
		args[38]=dropDownImple.GetDesginationCountry(args[13], req.getExchRate());
		args[14]=getModeOfTransaction(req.getPremiumSurplus(),req);
		args[39]=dropDownImple.GetDesginationCountry(args[14], req.getExchRate());
		args[15]=getModeOfTransaction(req.getCommissionSurplus(),req);
		args[40]=dropDownImple.GetDesginationCountry(args[15], req.getExchRate());
		args[16]=getModeOfTransaction(req.getPremiumportifolioIn(),req);
		args[41]=dropDownImple.GetDesginationCountry(args[16], req.getExchRate());
		args[72]=req.getRiCession();
	
		args[73]= req.getLoginId();
		args[74]=req.getBranchCode();
		args[75]=req.getDepartmentId();
		args[76] = getModeOfTransaction(req.getTaxDedectSource(),req);
		args[77] = dropDownImple.GetDesginationCountry(args[76], req.getExchRate());
		args[78] = getModeOfTransaction(req.getVatPremium(),req);
		args[79] = dropDownImple.GetDesginationCountry(args[78], req.getExchRate());
		args[80] = getModeOfTransaction(req.getSlideScaleCom(),req);
		args[81] = dropDownImple.GetDesginationCountry(args[80], req.getExchRate());
		args[82] = req.getPredepartment();
		args[83] = req.getSubProfitId().replace(" ", "");
		args[84] = req.getAccountPeriodDate();
		args[85] = req.getStatementDate();
		args[86] = req.getOsbYN();
		args[87] = getModeOfTransaction(req.getLossParticipation(),req);
		args[88] = dropDownImple.GetDesginationCountry(args[87], req.getExchRate());
		args[89] = req.getSectionName();
		args[90] = req.getProposalNo();
		args[91] = req.getProductId();
		if("submit".equalsIgnoreCase(req.getButtonStatus())){
			args[92] = "A";
		}else{
			args[92] = "P";
		}
		args[93] = req.getMode();
		

		if(!StringUtils.isEmpty(req.getPremiumQuotaShare())||!StringUtils.isEmpty(req.getPremiumSurplus()))
		{
			

			if(!StringUtils.isEmpty(req.getPremiumQuotaShare()))
			{
				premiumInsert=Double.parseDouble(req.getPremiumQuotaShare());
			}
			if(StringUtils.isEmpty(req.getCommissionQuotaShare()))
			{
				final double commission=premiumInsert*(Double.parseDouble(req.getCommissionview())/100);
				
				args[13]=getModeOfTransaction(commission+" ",req);
				args[38]=dropDownImple.GetDesginationCountry(args[13], req.getExchRate());
			}
			if(!StringUtils.isEmpty(req.getPremiumSurplus()))
			{
				premiumsurpInsert=(Double.parseDouble(req.getPremiumSurplus()));
			}
			if(StringUtils.isEmpty(req.getCommissionSurplus()))
			{
				
				final double comsurp=premiumsurpInsert*(Double.parseDouble(req.getCommssionSurp())/100);
				
				args[15]=getModeOfTransaction(comsurp+" ",req);
				args[40]=dropDownImple.GetDesginationCountry(args[15], req.getExchRate());
			}
			if(StringUtils.isEmpty(req.getBrokerage()))
			{
				final double brokerage=(premiumInsert+premiumsurpInsert)*(Double.parseDouble(req.getBrokerageview())/100);
				args[8]=getModeOfTransaction(brokerage+" ",req);
				args[35]=dropDownImple.GetDesginationCountry(args[8], req.getExchRate());
				
			}
			if(StringUtils.isEmpty(req.getTax()))
			{
				final double tax=(premiumInsert+premiumsurpInsert)*(Double.parseDouble(req.getTaxview())/100);
				args[10]=getModeOfTransaction(tax+" ",req);
				args[36]=dropDownImple.GetDesginationCountry(args[10], req.getExchRate());
				
			}
			if(StringUtils.isEmpty(req.getOverrider()))
			{
				double overrider=(premiumInsert+premiumsurpInsert)*(Double.parseDouble(req.getOverRiderview())/100);
				args[67]=getModeOfTransaction(overrider+" ",req);
				args[68]=dropDownImple.GetDesginationCountry(args[67], req.getExchRate());
				
			}

		}
		
		args[17]=getModeOfTransaction(req.getCliamPortfolioin(),req);
		args[42]=dropDownImple.GetDesginationCountry(args[17], req.getExchRate());
		args[18]=getModeOfTransaction(req.getPremiumportifolioout(),req);
		args[43]=dropDownImple.GetDesginationCountry(args[18], req.getExchRate());
		args[19]=getModeOfTransaction(req.getLossReserveReleased(),req);
		args[44]=dropDownImple.GetDesginationCountry(args[19], req.getExchRate());
		args[20]=getModeOfTransaction(req.getPremiumReserveQuotaShare(),req);
		args[45]=dropDownImple.GetDesginationCountry(args[20], req.getExchRate());
		args[21]=getModeOfTransaction(req.getCashLossCredit(),req);
		args[46]=dropDownImple.GetDesginationCountry(args[21], req.getExchRate());
		args[22]=getModeOfTransaction(req.getLossReserveRetained(),req);
		args[47]=dropDownImple.GetDesginationCountry(args[22], req.getExchRate());
		args[23]=getModeOfTransaction(StringUtils.isBlank(req.getProfitCommission()) ? "0" : req.getProfitCommission(),req);
		args[48]=dropDownImple.GetDesginationCountry(args[23], req.getExchRate());
		args[24]=getModeOfTransaction(req.getCashLossPaid(),req);
		args[49]=dropDownImple.GetDesginationCountry(args[24], req.getExchRate());
		args[25]="Y";
		args[26]=req.getEnteringMode();
		args[27]=StringUtils.isBlank(req.getReceiptno())?"":req.getReceiptno();
		args[28]=getModeOfTransaction(req.getClaimspaid(),req);
		args[50]=dropDownImple.GetDesginationCountry(args[28], req.getExchRate());
		args[29]=StringUtils.isBlank(req.getSettlementstatus())?"":req.getSettlementstatus();
		args[30]=getModeOfTransaction(req.getXlCost(),req);
		args[51]=dropDownImple.GetDesginationCountry(args[30], req.getExchRate());
		args[31]=getModeOfTransaction(req.getCliamportfolioout(),req);
		args[52]=dropDownImple.GetDesginationCountry(args[31], req.getExchRate());
		args[32]=getModeOfTransaction(req.getPremiumReserveReleased(),req);
		args[53]=dropDownImple.GetDesginationCountry(args[32], req.getExchRate());
		args[34]=getModeOfTransaction(req.getOtherCost(),req);
		args[55]=dropDownImple.GetDesginationCountry(args[34], req.getExchRate());
		
		args[56]=req.getCommissionview();
		args[57]=StringUtils.isBlank(req.getCedentRef())?"":req.getCedentRef();
		args[58]=req.getRemarks();
		args[59]=getModeOfTransaction(req.getTotalCredit(),req);
		args[60]=dropDownImple.GetDesginationCountry(args[59],req.getExchRate());
		args[61]=getModeOfTransaction(req.getTotalDebit(),req);
		args[62]=dropDownImple.GetDesginationCountry(args[61],req.getExchRate());
		args[63]=getModeOfTransaction(req.getInterest(),req);
		args[64]=dropDownImple.GetDesginationCountry(args[63],req.getExchRate());
		
		args[65]=StringUtils.isEmpty(req.getOsClaimsLossUpdateOC())?"0":getModeOfTransaction(req.getOsClaimsLossUpdateOC(),req);
		args[66]=dropDownImple.GetDesginationCountry(args[65], req.getExchRate());
		args[97] = StringUtils.isBlank(req.getM1oc())?"0":req.getM1oc();
		args[98] = StringUtils.isBlank(req.getM2oc())?"0":req.getM2oc();
		args[99] = StringUtils.isBlank(req.getM3oc())?"0":req.getM3oc();			
		args[94] = getModeOfTransaction(req.getBrokerageVat(),req);
		args[95] = dropDownImple.GetDesginationCountry(args[94], req.getExchRate());
		args[96] = req.getDocumentType();
		args[33]=getNetDueAmount(args,getModeOfTransaction(req.getClaimspaid(),req));
		args[54]=dropDownImple.GetDesginationCountry(args[33], req.getExchRate());
		req.setRequestNo(args[1]);
	
	final String[] copiedArray = new String[args.length];
	System.arraycopy(args, 0, copiedArray, 0, args.length);
	return copiedArray;
	}
	
	private String getRequestNo(InsertPremiumReq req) {
		String reqNo = "";
		String name = "";
		try {
			// query -- GET_SEQ_NAME
			name = propPremiumCustomRepository.getSeqName(req.getBranchCode());
			name = name == null ? "" : name;

			// query -- SELECT LPAD("+name+".nextval,6,0) REQ_NO FROM DUAL
			String sql="SELECT LPAD("+name+".nextval,6,0) REQ_NO FROM DUAL";
			query = em.createNativeQuery(sql);
		
			query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String,Object>> list = query.getResultList();
			if(!CollectionUtils.isEmpty(list)) {
				reqNo = list.get(0).get("REQ_NO")==null?"":list.get(0).get("REQ_NO").toString();
			}
			reqNo = "92" + reqNo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqNo;
	}
	
	private void getTempToMainMove(InsertPremiumReq req) {

		try {
			if (!"Main".equalsIgnoreCase(req.getTableType())) {

				// query -- FAC_PREMIUM_TEMP_TO_MAIN
				RskPremiumDetailsTemp rskTemp = rskPremiumDetailsTempRepository.findByRequestNoAndBranchCode(new BigDecimal(req.getRequestNo()),
						req.getBranchCode());
				if (rskTemp != null) {
					RskPremiumDetails detailsEntity = rskPremiumDetailsMapper.toProEntity(rskTemp);
					detailsEntity.setTransactionNo(new BigDecimal(req.getTransactionNo()));
					rskPremiumDetailsRepository.save(detailsEntity);
				}

				// query -- premium.sp.retroSplit
				propPremiumCustomRepository.premiumSpRetroSplit(req);
				propPremiumCustomRepository.premiumRiSplit(req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void InsertPremiumReserved(InsertPremiumReq req) {
		String rLNo = "";
		try {
			if (req.getInsertPremiumReserved() != null) {
				if (StringUtils.isNotBlank(req.getPRTransNo())) {
					req.setType("PRR");
					// Query -- premium.select.MAXRLNo
					rLNo = propPremiumCustomRepository.premiumSelectRLNO(req.getBranchCode());
					rLNo = rLNo == null ? "" : rLNo;

					for (int i = 0; i < req.getInsertPremiumReserved().size(); i++) {
						InsertLossReserved request = req.getInsertLossReserved().get(i);
						if (StringUtils.isNotBlank(request.getPRTransNo())) {
							GetPremiumReservedReq req1 = new GetPremiumReservedReq();
							req1.setBranchCode(req.getBranchCode());
							req1.setContNo(req.getContNo());
							req1.setCurrencyId(req.getCurrencyId());
							req1.setDepartmentId(req.getDepartmentId());
							req1.setPrTransNo(req.getPRTransNo());
							req1.setTransaction(req.getTransaction());
							req1.setType(req.getType());
							GetPremiumReservedRes1 cashLossList = getPremiumReserved(req1);
							GetPremiumReservedRes form = cashLossList.getCommonResponse().get(0);
							if (request.getPRTransNo().equals(form.getTransactionNo())) {
								String[] obj = new String[18];
								obj[0] = "";
								obj[1] = form.getContNo();
								obj[2] = req.getDepartmentId();
								obj[3] = "PRR";
								obj[4] = rLNo;
								obj[5] = form.getTransactionNo();
								obj[6] = req.getTransaction();
								obj[7] = request.getPRTransNo();
								obj[8] = form.getPaidDate();
								obj[9] = form.getCurrencyValue();
								obj[10] = req.getCurrency();
								obj[11] = request.getPRAmount();
								obj[12] = request.getPREAmount();
								obj[13] = request.getPRERate();
								obj[14] = req.getLoginId();
								obj[15] = req.getBranchCode();
								if ("submit".equalsIgnoreCase(req.getButtonStatus())) {
									obj[16] = "A";
								} else {
									obj[16] = "P";
								}
								obj[17] = req.getRequestNo();
								// query -- INSERT_PREMIUM_RESERVE
								TtrnDepositRelease entity = ttrnDepositReleaseMapper.toEntity(obj);
								ttrnDepositReleaseRepository.save(entity);

								if ("submit".equalsIgnoreCase(req.getButtonStatus())) {
									// query -- UPDATE_PREMIUM_RESERVE
									propPremiumCustomRepository.updatePremiumReserve(form.getContNo(),form.getRequestNo(),form.getTransactionNo(),"A");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void InsertLossReserved(InsertPremiumReq req) {
		String rLNo = "";
		try {
			if (req.getInsertLossReserved() != null) {
				req.setType("LRR");
				// Query -- premium.select.RL_NO
				rLNo = propPremiumCustomRepository.premiumSelectRLNO(req.getBranchCode());
				rLNo = rLNo == null ? "" : rLNo;

				for (int i = 0; i < req.getInsertLossReserved().size(); i++) {
					InsertLossReserved request = req.getInsertLossReserved().get(i);

					if (StringUtils.isNotBlank(request.getPRTransNo())) {
						GetPremiumReservedReq req1 = new GetPremiumReservedReq();
						req1.setBranchCode(req.getBranchCode());
						req1.setContNo(req.getContNo());
						req1.setCurrencyId(req.getCurrencyId());
						req1.setDepartmentId(req.getDepartmentId());
						req1.setPrTransNo(req.getPRTransNo());
						req1.setTransaction(req.getTransaction());
						req1.setType(req.getType());
						GetPremiumReservedRes1 cashLossList = getPremiumReserved(req1);
						GetPremiumReservedRes form = cashLossList.getCommonResponse().get(0);
						if (request.getPRTransNo().equals(form.getTransactionNo())) {
							String[] obj = new String[18];
							obj[0] = "";
							obj[1] = form.getContNo();
							obj[2] = req.getDepartmentId();
							obj[3] = "LRR";
							obj[4] = rLNo;
							obj[5] = req.getTransactionNo();
							obj[6] = req.getTransaction();
							obj[7] = request.getPRTransNo();
							obj[8] = form.getPaidDate();
							obj[9] = form.getCurrencyValue();
							obj[10] = req.getCurrency();
							obj[11] = request.getPRAmount();
							obj[12] = request.getPREAmount();
							obj[13] = request.getPRERate();
							obj[14] = req.getLoginId();
							obj[15] = req.getBranchCode();
							if ("submit".equalsIgnoreCase(req.getButtonStatus())) {
								obj[16] = "A";
							} else {
								obj[16] = "P";
							}
							obj[17] = req.getRequestNo();
							// query -- INSERT_PREMIUM_RESERVE
							TtrnDepositRelease entity = ttrnDepositReleaseMapper.toEntity(obj);
							ttrnDepositReleaseRepository.save(entity);

							if ("submit".equalsIgnoreCase(req.getButtonStatus())) {
								// query -- UPDATE_LOSS_RESERVE
								propPremiumCustomRepository.updateLossReserve(form.getContNo(),form.getRequestNo(),form.getTransactionNo(),"A");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getModeOfTransaction(final String Value, final InsertPremiumReq req) {

		String result = "0";
		double shareSigned = 0.0;
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		if (req.getEnteringMode() != null) {
			if ("1".equalsIgnoreCase(req.getEnteringMode())) {
				shareSigned = Double.parseDouble(req.getShareSigned());
			} else if ("2".equalsIgnoreCase(req.getEnteringMode())) {
				shareSigned = 100;
			}

			if (!"".equalsIgnoreCase(Value)) {
				double finalValue = Double.parseDouble(Value) * shareSigned / 100;

				result = String.valueOf(Double.valueOf(twoDForm.format(finalValue)));
			}
		}

		return result;
	}
	
	private static String getNetDueAmount(final String[] args, final String CliamPaid) {

		double Abt = 0;
		double Bbt = 0;
		if (StringUtils.isNotEmpty(args[12])) {
			Abt += Double.parseDouble(args[12]);
		}
		if (StringUtils.isNotEmpty(args[14])) {
			Abt += Double.parseDouble(args[14]);
		}
		if (StringUtils.isNotEmpty(args[16])) {
			Abt += Double.parseDouble(args[16]);
		}
		if (StringUtils.isNotEmpty(args[17])) {
			Abt += Double.parseDouble(args[17]);
		}
		if (StringUtils.isNotEmpty(args[19])) {
			Abt += Double.parseDouble(args[19]);
		}
		if (StringUtils.isNotEmpty(args[21])) {
			Abt += Double.parseDouble(args[21]);
		}
		if (StringUtils.isNotEmpty(args[32])) {
			Abt += Double.parseDouble(args[32]);
		}
		if (StringUtils.isNotEmpty(args[63])) {
			Abt += Double.parseDouble(args[63]);
		}
		if (StringUtils.isNotEmpty(args[76])) {
			Abt += Double.parseDouble(args[76]);
		}
		if (StringUtils.isNotEmpty(args[78])) {
			Abt += Double.parseDouble(args[78]);
		}

		if (StringUtils.isNotEmpty(args[87])) {
			Abt += Double.parseDouble(args[87]);
		}
		if (StringUtils.isNotEmpty(args[13])) {
			Bbt += Double.parseDouble(args[13]);
		}
		if (StringUtils.isNotEmpty(args[15])) {
			Bbt += Double.parseDouble(args[15]);
		}
		if (StringUtils.isNotEmpty(args[8])) {
			Bbt += Double.parseDouble(args[8]);
		}
		if (StringUtils.isNotEmpty(args[10])) {
			Bbt += Double.parseDouble(args[10]);
		}
		if (StringUtils.isNotEmpty(args[18])) {
			Bbt += Double.parseDouble(args[18]);
		}
		if (StringUtils.isNotEmpty(args[20])) {
			Bbt += Double.parseDouble(args[20]);
		}
		if (StringUtils.isNotEmpty(args[22])) {
			Bbt += Double.parseDouble(args[22]);
		}
		if (StringUtils.isNotEmpty(args[23])) {
			Bbt += Double.parseDouble(args[23]);
		}
		if (StringUtils.isNotEmpty(args[24])) {
			Bbt += Double.parseDouble(args[24]);
		}
		if (StringUtils.isNotEmpty(CliamPaid)) {
			Bbt += Double.parseDouble(CliamPaid);
		}
		if (StringUtils.isNotEmpty(args[31])) {
			Bbt += Double.parseDouble(args[31]);
		}
		if (StringUtils.isNotEmpty(args[30])) {
			Bbt += Double.parseDouble(args[30]);
		}
		if (StringUtils.isNotEmpty(args[34])) {
			Bbt += Double.parseDouble(args[34]);
		}
		if (StringUtils.isNotEmpty(args[67])) {
			Bbt += Double.parseDouble(args[67]);
		}
		if (StringUtils.isNotEmpty(args[70])) {
			Bbt += Double.parseDouble(args[70]);
		}
		if (StringUtils.isNotEmpty(args[80])) {
			Bbt += Double.parseDouble(args[80]);
		}

		final double cbt = Abt - Bbt;

		return String.valueOf(cbt);
	}
	
	public ContractDetailsRes contractDetails(ContractDetailsReq req) {
		ContractDetailsRes response = new ContractDetailsRes();
		List<Tuple> list = new ArrayList<>();
		ContractDetailsRes1 res = new ContractDetailsRes1();
		List<ContractDetailsRes2> res2List = new ArrayList<ContractDetailsRes2>();
		ContractDetailsResponse response1 = new ContractDetailsResponse();
		try {
			// query -- premium.select.treatyContDet
			list = propPremiumCustomRepository.premiumSelectTreatyContDet(req);

			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				res.setContNo(tempMap.get("RSK_CONTRACT_NO") == null ? "" : tempMap.get("RSK_CONTRACT_NO").toString());
				res.setAmendId(
						tempMap.get("RSK_ENDORSEMENT_NO") == null ? "" : tempMap.get("RSK_ENDORSEMENT_NO").toString());
				res.setProfitCenter(
						tempMap.get("TMAS_PFC_NAME") == null ? "" : tempMap.get("TMAS_PFC_NAME").toString());
				res.setSubProfitCenter(tempMap.get("RSK_SPFCID") == null ? "" : tempMap.get("RSK_SPFCID").toString());
				if (!"ALL".equalsIgnoreCase(res.getSubProfitCenter())) {
					res.setSubProfitCenter(
							tempMap.get("TMAS_SPFC_NAME") == null ? "" : tempMap.get("TMAS_SPFC_NAME").toString());
				}
				res.setCedingCo(tempMap.get("COMPANY") == null ? "" : tempMap.get("COMPANY").toString());
				res.setBroker(tempMap.get("BROKER") == null ? "" : tempMap.get("BROKER").toString());
				res.setTreatyNameType(
						tempMap.get("RSK_TREATYID") == null ? "" : tempMap.get("RSK_TREATYID").toString());
				res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER") == null ? ""
						: tempMap.get("RSK_PROPOSAL_NUMBER").toString());
				res.setUwYear(tempMap.get("RSK_UWYEAR") == null ? "" : tempMap.get("RSK_UWYEAR").toString());
				res.setLayerNo(tempMap.get("RSK_LAYER_NO") == null ? "" : tempMap.get("RSK_LAYER_NO").toString());
				res.setInsDate(tempMap.get("INS_DATE") == null ? "" : formatDate(tempMap.get("INS_DATE")).toString());
				res.setExpDate(tempMap.get("EXP_DATE") == null ? "" : formatDate(tempMap.get("EXP_DATE")).toString());
				res.setMonth(tempMap.get("MONTH") == null ? "" : tempMap.get("MONTH").toString());
				res.setBaseCurrencyId(
						tempMap.get("RSK_ORIGINAL_CURR") == null ? "" : tempMap.get("RSK_ORIGINAL_CURR").toString());
				res.setBaseCurrencyName(
						tempMap.get("CURRENCY_NAME") == null ? "" : tempMap.get("CURRENCY_NAME").toString());
				res.setPolicyBranch(tempMap.get("TMAS_POL_BRANCH_NAME") == null ? ""
						: tempMap.get("TMAS_POL_BRANCH_NAME").toString());
				res.setAddress(tempMap.get("Address") == null ? "" : tempMap.get("Address").toString());
				res.setDepartmentId(tempMap.get("RSK_DEPTID") == null ? "" : tempMap.get("RSK_DEPTID").toString());
				String count = "";
				if ("2".equals(req.getProductId())) {
					//count = getCombinedClass(req.getBranchCode(), req.getProductId(), req.getSectionNo());
				}
				if (StringUtils.isBlank(count)) {
					res.setPreDepartment(tempMap.get("RSK_DEPTID") == null ? "" : tempMap.get("RSK_DEPTID").toString());
					res.setConsubProfitId(
							tempMap.get("RSK_SPFCID") == null ? "" : tempMap.get("RSK_SPFCID").toString());
				}
				res.setTreatyType(tempMap.get("TREATYTYPE") == null ? "" : tempMap.get("TREATYTYPE").toString());
				res.setBusinessType(
						tempMap.get("INWARD_BUS_TYPE") == null ? "" : tempMap.get("INWARD_BUS_TYPE").toString());
				res.setAcceptenceDate(
						tempMap.get("RSK_ACCOUNT_DATE") == null ? "" : tempMap.get("RSK_ACCOUNT_DATE").toString());
			}
			if (list != null && list.size() > 0)

				// query -- premium.select.commissionDetails
				list = propPremiumCustomRepository.selectCommissionDetails(req.getProposalNo());
			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);

				res.setCommissionView(fm.formatter(tempMap.get("RSK_COMM_QUOTASHARE") == null ? ""
						: tempMap.get("RSK_COMM_QUOTASHARE").toString()));
				res.setPremiumReserveView(fm.formatter(tempMap.get("RSK_PREMIUM_RESERVE") == null ? ""
						: tempMap.get("RSK_PREMIUM_RESERVE").toString()));
				res.setLossReserveView(fm.formatter(
						tempMap.get("RSK_LOSS_RESERVE") == null ? "" : tempMap.get("RSK_LOSS_RESERVE").toString()));
				res.setProfitCommYN(fm.formatter(
						tempMap.get("RSK_PROFIT_COMM") == null ? "" : tempMap.get("RSK_PROFIT_COMM").toString()));

				res.setCommissionSurbView(fm.formatter(
						tempMap.get("RSK_COMM_SURPLUS") == null ? "" : tempMap.get("RSK_COMM_SURPLUS").toString()));
				res.setOverRiderView(fm.formatter(
						tempMap.get("RSK_OVERRIDER_PERC") == null ? "" : tempMap.get("RSK_OVERRIDER_PERC").toString()));
				res.setBrokerageView(fm.formatter(
						tempMap.get("RSK_BROKERAGE") == null ? "" : tempMap.get("RSK_BROKERAGE").toString()));
				res.setBrokerageView(
						tempMap.get("RSK_BROKERAGE") == null ? "" : tempMap.get("RSK_BROKERAGE").toString());
				res.setTaxView(fm.formatter(tempMap.get("RSK_TAX") == null ? "" : tempMap.get("RSK_TAX").toString()));
				res.setOtherCostView(fm.formatter(
						tempMap.get("RSK_OTHER_COST") == null ? "" : tempMap.get("RSK_OTHER_COST").toString()));
				res.setOurAssessmentOfOrginal(tempMap.get("RSK_OUR_ASS_ACQ_COST") == null ? "0.00"
						: tempMap.get("RSK_OUR_ASS_ACQ_COST").toString());
				res.setPremiumReserve(tempMap.get("RSK_PREMIUM_RESERVE") == null ? ""
						: tempMap.get("RSK_PREMIUM_RESERVE").toString());

			}

			// query -- premium.select.treatyProposalDetails
			list = propPremiumCustomRepository.premiumSelectTreatyProposalDetails(req.getProposalNo());

			for (int i = 0; i < list.size(); i++) {
				Tuple tempMap = list.get(i);
				res.setShareSigned(
						tempMap.get("RSK_SHARE_SIGNED") == null ? "" : tempMap.get("RSK_SHARE_SIGNED").toString());
				res.setPremiumQuotaView(fm.formatter(tempMap.get("RSK_PREMIUM_QUOTA_SHARE") == null ? ""
						: tempMap.get("RSK_PREMIUM_QUOTA_SHARE").toString()));
				res.setPremiumsurpView(fm.formatter(tempMap.get("RSK_PREMIUM_SURPULS") == null ? ""
						: tempMap.get("RSK_PREMIUM_SURPULS").toString()));
				res.setXlCostView(fm.formatter(
						tempMap.get("RSK_XLCOST_OS_OC") == null ? "" : tempMap.get("RSK_XLCOST_OS_OC").toString()));
				String eps = (tempMap.get("RSK_EPI_OSOE_OC") == null ? "" : tempMap.get("RSK_EPI_OSOE_OC").toString());
				res.setRdsExchageRate(
						tempMap.get("RSK_EXCHANGE_RATE") == null ? "" : tempMap.get("RSK_EXCHANGE_RATE").toString());
				double val = Double.parseDouble(eps) / Double.parseDouble(res.getRdsExchageRate());
				res.setEpioc(fm.formatter(Double.toString(val)));
			}
			if (StringUtils.isNotBlank(req.getTransactionNo())) {

				// query -- premium.select.cashLossCreditUpdate
				List<Tuple> claimlist = propPremiumCustomRepository.premiumSelectCashLossCreditUpdate(req.getContNo(),
						req.getTransactionNo());

				if (claimlist != null) {
					for (int k = 0; k < claimlist.size(); k++) {
						Tuple temp = claimlist.get(k);
						ContractDetailsRes2 res2 = new ContractDetailsRes2();
						res2.setClaimNo(temp.get("CLAIM_NO") == null ? "" : temp.get("CLAIM_NO").toString());
						res2.setCurrencyId(temp.get("CURRENCY_ID") == null ? "" : temp.get("CURRENCY_ID").toString());
						res2.setCashoc(temp.get("CASH_LOSS_CREDIT_OC") == null ? ""
								: temp.get("CASH_LOSS_CREDIT_OC").toString());
						res2.setCashdc(temp.get("CASH_LOSS_CREDIT_DC") == null ? ""
								: temp.get("CASH_LOSS_CREDIT_DC").toString());
						res2List.add(res2);
					}
				}
			}

			// query -- premium.select.currecy.name
			String output = propPremiumCustomRepository.selectCurrecyName(req.getBranchCode());
			res.setCurrencyName(output == null ? "" : output);

			// query -- premium.select.sumOfPaidPremium
			output = propPremiumCustomRepository.premiumSelectSumOfPaidPremium(req.getContNo());
			res.setSumofPaidPremium(output == null ? "" : output);

			// query -- GETSETTLEMET_STATUS
			List<Tuple> premlist = propPremiumCustomRepository.getSettlementStatus(req.getContNo());

			if (premlist.size() > 0) {
				for (int j = 0; j < premlist.size(); j++) {
					Tuple map = premlist.get(j);
					String allocate = map.get("ALLOCATED_TILL_DATE") == null ? "0"
							: map.get("ALLOCATED_TILL_DATE").toString();
					String net = map.get("NETDUE_OC").toString();
					if ("0".equalsIgnoreCase(allocate)) {
						res.setSettlementStatus("Pending");
					} else if (Double.parseDouble(allocate) == Double.parseDouble(net)) {
						res.setSettlementStatus("Allocated");
					} else {
						res.setSettlementStatus("Partially Allocated");
					}
				}
			}
			response1.setCommonResponse2(res2List);
			response1.setCommonResponse1(res);
			response.setCommonResponse(response1);
			response.setMessage("Success");
			response.setIsError(false);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public String getCombinedClass(String branchCode, String productId, String departId) {
		String count = "";
		try {

			List<BigDecimal> departIdList = new ArrayList<BigDecimal>();
			departIdList.add(BigDecimal.valueOf(Long.valueOf(departId)));
			List<TmasDepartmentMaster> list1 = dmRepo
					.findByBranchCodeAndTmasProductIdAndTmasStatusAndCoreCompanyCodeIsNotNullAndTmasDepartmentIdIn(
							branchCode, BigDecimal.valueOf(Long.valueOf(productId)), "Y", departIdList);
			if(list1!=null && "".equals(list1)) {
			count = list1.get(0).getCoreCompanyCode() == null ? "" : list1.get(0).getCoreCompanyCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	
	public GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req) {
		List<Tuple>list = null;
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		List<GetPremiumDetailsRes1> resList = new ArrayList<GetPremiumDetailsRes1>();
		try{
			
		   	if("Temp".equalsIgnoreCase(req.getTableType())){
		   		list=propPremiumCustomRepository.getPremiumTempView(req.getBranchCode(),req.getProductId(),req.getContractNo(),req.getRequestNo());
		   	}else{
		   		list=propPremiumCustomRepository.getPremiumView(req.getBranchCode(),req.getProductId(),req.getContractNo(),req.getTransactionNo());
		   	}
		   	for (int i = 0; i < list.size(); i++) {
		   		Tuple tempMap=list.get(i);
				GetPremiumDetailsRes1 res = new GetPremiumDetailsRes1();
				res.setContNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				res.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
				res.setTransaction(tempMap.get("TRANS_DATE")==null?"":formatDate(tempMap.get("TRANS_DATE")));
				res.setBrokerage(tempMap.get("BROKERAGE_AMT_OC")==null?"":fm.formatter(tempMap.get("BROKERAGE_AMT_OC").toString()));
				res.setTax(tempMap.get("TAX_AMT_OC")==null?"":fm.formatter(tempMap.get("TAX_AMT_OC").toString()));
				res.setPremiumQuotaShare(tempMap.get("PREMIUM_QUOTASHARE_OC")==null?"":fm.formatter(tempMap.get("PREMIUM_QUOTASHARE_OC").toString()));
				res.setCommissionQuotaShare(tempMap.get("COMMISSION_QUOTASHARE_OC")==null?"":fm.formatter(tempMap.get("COMMISSION_QUOTASHARE_OC").toString()));
				res.setPremiumSurplus(tempMap.get("PREMIUM_SURPLUS_OC")==null?"":fm.formatter(tempMap.get("PREMIUM_SURPLUS_OC").toString()));
				res.setCommissionSurplus(tempMap.get("COMMISSION_SURPLUS_OC")==null?"":fm.formatter(tempMap.get("COMMISSION_SURPLUS_OC").toString()));
				res.setPremiumportifolioIn(tempMap.get("PREMIUM_PORTFOLIOIN_OC")==null?"":fm.formatter(tempMap.get("PREMIUM_PORTFOLIOIN_OC").toString()));
				res.setCliamPortfolioin(tempMap.get("CLAIM_PORTFOLIOIN_OC")==null?"":fm.formatter(tempMap.get("CLAIM_PORTFOLIOIN_OC").toString()));
				res.setPremiumportifolioout(tempMap.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":fm.formatter(tempMap.get("PREMIUM_PORTFOLIOOUT_OC").toString()));
					
		
					res.setLossReserveReleased(tempMap.get("LOSS_RESERVE_RELEASED_OC")==null?"":fm.formatter(tempMap.get("LOSS_RESERVE_RELEASED_OC").toString()));
					res.setPremiumReserveQuotaShare(tempMap.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":fm.formatter(tempMap.get("PREMIUMRESERVE_QUOTASHARE_OC").toString()));
					res.setCashLossCredit(tempMap.get("CASH_LOSS_CREDIT_OC")==null?"":fm.formatter(tempMap.get("CASH_LOSS_CREDIT_OC").toString()));
					res.setLossReserveRetained(tempMap.get("LOSS_RESERVERETAINED_OC")==null?"":fm.formatter(tempMap.get("LOSS_RESERVERETAINED_OC").toString()));
					
					res.setProfitCommission(tempMap.get("PROFIT_COMMISSION_OC")==null?"":fm.formatter(tempMap.get("PROFIT_COMMISSION_OC").toString()));
					res.setCashLossPaid(tempMap.get("CASH_LOSSPAID_OC")==null?"":fm.formatter(tempMap.get("CASH_LOSSPAID_OC").toString()));
					res.setNetDue(tempMap.get("NETDUE_OC")==null?"":fm.formatter(tempMap.get("NETDUE_OC").toString()));
					res.setReceiptno(tempMap.get("RECEIPT_NO")==null?"":tempMap.get("RECEIPT_NO").toString());
				
					res.setClaimsPaid(tempMap.get("CLAIMS_PAID_OC")==null?"":fm.formatter(tempMap.get("CLAIMS_PAID_OC").toString()));
					res.setInceptionDate(tempMap.get("ENTRY_DATE")==null?"":formatDate(tempMap.get("ENTRY_DATE")));
					res.setXlCost(tempMap.get("XL_COST_OC")==null?"":fm.formatter(fm.formatter(tempMap.get("XL_COST_OC").toString())));
					res.setCliamPortfolioOut(tempMap.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":fm.formatter(tempMap.get("CLAIM_PORTFOLIO_OUT_OC").toString()));
					res.setPremiumReserveReleased(tempMap.get("PREMIUM_RESERVE_REALSED_OC")==null?"":fm.formatter(tempMap.get("PREMIUM_RESERVE_REALSED_OC").toString()));
					res.setAccountPeriod(tempMap.get("ACCOUNT_PERIOD_QTR")==null?"":tempMap.get("ACCOUNT_PERIOD_QTR").toString());
					res.setAccountPeriodYear(tempMap.get("ACCOUNT_PERIOD_YEAR")==null?"":tempMap.get("ACCOUNT_PERIOD_YEAR").toString());
					res.setCurrencyId(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					res.setOtherCost(tempMap.get("OTHER_COST_OC")==null?"":fm.formatter(tempMap.get("OTHER_COST_OC").toString()));
					res.setBrokerageUsd(tempMap.get("BROKERAGE_AMT_DC")==null?"":fm.formatter(tempMap.get("BROKERAGE_AMT_DC").toString()));
					res.setTaxUsd(tempMap.get("TAX_AMT_DC")==null?"":fm.formatter(tempMap.get("TAX_AMT_DC").toString()));
					res.setPremiumQuotaShareUsd(tempMap.get("PREMIUM_QUOTASHARE_DC")==null?"":fm.formatter(tempMap.get("PREMIUM_QUOTASHARE_DC").toString()));
					res.setCommsissionQuotaShareUsd(tempMap.get("COMMISSION_QUOTASHARE_DC")==null?"":fm.formatter(tempMap.get("COMMISSION_QUOTASHARE_DC").toString()));
					res.setPremiumSurplusUsd(tempMap.get("PREMIUM_SURPLUS_DC")==null?"":fm.formatter(tempMap.get("PREMIUM_SURPLUS_DC").toString()));
					res.setComissionSurplusUsd(tempMap.get("COMMISSION_SURPLUS_DC")==null?"":fm.formatter(tempMap.get("COMMISSION_SURPLUS_DC").toString()));
					res.setPremiumPortfolioInUsd(tempMap.get("PREMIUM_PORTFOLIOIN_DC")==null?"":fm.formatter(tempMap.get("PREMIUM_PORTFOLIOIN_DC").toString()));
					res.setCliamPortfolioUsd(tempMap.get("CLAIM_PORTFOLIOIN_DC")==null?"":fm.formatter(tempMap.get("CLAIM_PORTFOLIOIN_DC").toString()));
					res.setPremiumPortfolioOutUsd(tempMap.get("PREMIUM_PORTFOLIOOUT_DC")==null?"":fm.formatter(tempMap.get("PREMIUM_PORTFOLIOOUT_DC").toString()));
					res.setLossReserveReleasedUsd(tempMap.get("LOSS_RESERVE_RELEASED_DC")==null?"":fm.formatter(tempMap.get("LOSS_RESERVE_RELEASED_DC").toString()));
					res.setPremiumReserveQuotaShareUsd(tempMap.get("PREMIUMRESERVE_QUOTASHARE_DC")==null?"":fm.formatter(tempMap.get("PREMIUMRESERVE_QUOTASHARE_DC").toString()));
					res.setCashLossCreditUsd(tempMap.get("CASH_LOSS_CREDIT_DC")==null?"":fm.formatter(tempMap.get("CASH_LOSS_CREDIT_DC").toString()));
					res.setLossReserveRetainedUsd(tempMap.get("LOSS_RESERVERETAINED_DC")==null?"":fm.formatter(tempMap.get("LOSS_RESERVERETAINED_DC").toString()));
					res.setProfitCommissionUsd(tempMap.get("PROFIT_COMMISSION_DC")==null?"":fm.formatter(tempMap.get("PROFIT_COMMISSION_DC").toString()));
					res.setCashLossPaidUsd(tempMap.get("CASH_LOSSPAID_DC")==null?"":fm.formatter(tempMap.get("CASH_LOSSPAID_DC").toString()));
					res.setClamsPaidUsd(tempMap.get("CLAIMS_PAID_DC")==null?"":fm.formatter(tempMap.get("CLAIMS_PAID_DC").toString()));
					res.setXlCostUsd(tempMap.get("XL_COST_DC")==null?"":fm.formatter(tempMap.get("XL_COST_DC").toString()));
					res.setCliamPortfolioOutUsd(tempMap.get("CLAIM_PORTFOLIO_OUT_DC")==null?"":fm.formatter(tempMap.get("CLAIM_PORTFOLIO_OUT_DC").toString()));
					res.setPremiumReserveReleasedUsd(tempMap.get("PREMIUM_RESERVE_REALSED_DC")==null?"":fm.formatter(tempMap.get("PREMIUM_RESERVE_REALSED_DC").toString()));
					res.setNetDueUsd(tempMap.get("NETDUE_DC")==null?"":fm.formatter(tempMap.get("NETDUE_DC").toString()));
					res.setOtherCostUSD(tempMap.get("OTHER_COST_DC")==null?"":fm.formatter(tempMap.get("OTHER_COST_DC").toString()));
					res.setCedentRef(tempMap.get("CEDANT_REFERENCE")==null?"":tempMap.get("CEDANT_REFERENCE").toString());
					res.setRemarks(tempMap.get("REMARKS")==null?"":tempMap.get("REMARKS").toString());
					res.setTotalCredit(tempMap.get("TOTAL_CR_OC")==null?"":fm.formatter(tempMap.get("TOTAL_CR_OC").toString()));
					res.setTotalCreditDC(tempMap.get("TOTAL_CR_DC")==null?"":fm.formatter(tempMap.get("TOTAL_CR_DC").toString()));
					res.setTotalDebit(tempMap.get("TOTAL_DR_OC")==null?"":fm.formatter(tempMap.get("TOTAL_DR_OC").toString()));
					res.setTotalDebitDC(tempMap.get("TOTAL_DR_DC")==null?"":fm.formatter(tempMap.get("TOTAL_DR_DC").toString()));
					res.setInterest(tempMap.get("INTEREST_OC")==null?"":fm.formatter(tempMap.get("INTEREST_OC").toString()));
					res.setInterestDC(tempMap.get("INTEREST_DC")==null?"":fm.formatter(tempMap.get("INTEREST_DC").toString()));
					res.setOsClaimsLossUpdateOC(tempMap.get("OSCLAIM_LOSSUPDATE_OC")==null?"":fm.formatter(tempMap.get("OSCLAIM_LOSSUPDATE_OC").toString()));
					res.setOsClaimsLossUpdateDC(tempMap.get("OSCLAIM_LOSSUPDATE_DC")==null?"":fm.formatter(tempMap.get("OSCLAIM_LOSSUPDATE_DC").toString()));
					res.setOverrider(tempMap.get("OVERRIDER_AMT_OC")==null?"":fm.formatter(tempMap.get("OVERRIDER_AMT_OC").toString()));
					res.setOverriderUSD(tempMap.get("OVERRIDER_AMT_DC")==null?"":fm.formatter(tempMap.get("OVERRIDER_AMT_DC").toString()));
					res.setAmendmentDate(tempMap.get("AMENDMENT_DATE")==null?"":formatDate(tempMap.get("AMENDMENT_DATE")));
	                res.setWithHoldingTaxOC(tempMap.get("WITH_HOLDING_TAX_OC")==null?"":fm.formatter(tempMap.get("WITH_HOLDING_TAX_OC").toString()));
	                res.setWithHoldingTaxDC(tempMap.get("WITH_HOLDING_TAX_DC")==null?"":fm.formatter(tempMap.get("WITH_HOLDING_TAX_DC").toString()));
	                //res.setDueDate(tempMap.get("DUE_DATE")==null?"":tempMap.get("DUE_DATE").toString());
	                res.setCreditsign(tempMap.get("NETDUE_OC")==null?"":tempMap.get("NETDUE_OC").toString());
	                res.setRiCession(tempMap.get("RI_CESSION")==null?"":tempMap.get("RI_CESSION").toString());
	                res.setTaxDedectSource(tempMap.get("TDS_OC")==null?"":fm.formatter(tempMap.get("TDS_OC").toString()));
					res.setTaxDedectSourceDc(tempMap.get("TDS_DC")==null?"":fm.formatter(tempMap.get("TDS_DC").toString()));
					res.setServiceTax(tempMap.get("ST_OC")==null?"":fm.formatter(tempMap.get("ST_OC").toString()));
					res.setServiceTaxDc(tempMap.get("ST_DC")==null?"":fm.formatter(tempMap.get("ST_DC").toString()));
					res.setLossParticipation(tempMap.get("LPC_OC")==null?"":fm.formatter(tempMap.get("LPC_OC").toString()));
					res.setLossParticipationDC(tempMap.get("LPC_DC")==null?"":fm.formatter(tempMap.get("LPC_DC").toString()));
					res.setSlideScaleCom(tempMap.get("SC_COMM_OC")==null?"":fm.formatter(tempMap.get("SC_COMM_OC").toString()));
					res.setSlideScaleComDC(tempMap.get("SC_COMM_DC")==null?"":fm.formatter(tempMap.get("SC_COMM_DC").toString()));
					/*res.setSubProfitId(tempMap.get("PREMIUM_SUBCLASS")==null?"":tempMap.get("PREMIUM_SUBCLASS").toString());
					if(!"ALL".equalsIgnoreCase(res.getSubProfitId())){
					res.setSubProfitId(tempMap.get("PREMIUM_SUBCLASS")==null?"":tempMap.get("PREMIUM_SUBCLASS").toString());
					}*/
					res.setExchRate(tempMap.get("EXCHANGE_RATE")==null?"":fm.formatter(tempMap.get("EXCHANGE_RATE").toString()));
					res.setStatementDate(tempMap.get("STATEMENT_DATE")==null?"":formatDate(tempMap.get("STATEMENT_DATE")));
					 res.setPremiumClass(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
		                /*res.setPremiumSubClass(tempMap.get("SUB")==null?"":tempMap.get("SUB").toString());
		                if(!"ALL".equalsIgnoreCase(res.getPremiumSubClass())){
		                	res.setPremiumSubClass(tempMap.get("PREMIUM_SUBCLASS")==null?"":tempMap.get("PREMIUM_SUBCLASS").toString());
		                }*/
		                res.setOsbYN(tempMap.get("OSBYN")==null?"":tempMap.get("OSBYN").toString());
		                //res.setSectionName(tempMap.get("SECTION_NAME")==null?"":tempMap.get("SECTION_NAME").toString());
		                res.setAccDate(tempMap.get("ACCOUNTING_PERIOD_DATE")==null?"":formatDate(tempMap.get("ACCOUNTING_PERIOD_DATE"))) ;
			
				
				String output="";
				output=propPremiumCustomRepository.premiumSelectSumOfPaidPremium(req.getContractNo());
				res.setSumOfPaidPremium(output);
				if(StringUtils.isNotBlank(res.getCurrencyId())){
					// query -- premium.select.currency
			   		output = xolPremiumCustomRepository.premiumSelectCurrency(res.getCurrencyId(),req.getBranchCode());
					res.setCurrency(output == null ? "" : output);
			   	}
			   	
			   	//query -- premium.select.currecy.name
			   	output = xolPremiumCustomRepository.selectCurrecyName(req.getBranchCode());
				res.setCurrencyName(output == null ? "" : output);
			resList.add(res);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
	}
	catch (Exception e) {
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	
	return response;

	}

	public PremiumEditRes premiumEdit(PremiumEditReq req) {
		PremiumEditRes response = new PremiumEditRes();
		PremiumEditRes1 res = new PremiumEditRes1();
		List<Tuple>list = null;
		try {
			if("Temp".equalsIgnoreCase(req.getTableType())){
		   		list=propPremiumCustomRepository.getPremiumTempEdit(req.getContNo(),req.getRequestNo());
		   	}else{
		   		list=propPremiumCustomRepository.getPremiumEdit(req.getContNo(),req.getTransactionNo());
		   	}
			for(int i=0;i<list.size();i++) {
				Tuple editpremium = list.get(i);
					res.setTransaction(editpremium.get("TRANS_DATE")==null?"":formatDate(editpremium.get("TRANS_DATE")));
					res.setAccountPeriod(editpremium.get("ACCOUNT_PERIOD_QTR")==null?"":editpremium.get("ACCOUNT_PERIOD_QTR").toString());
					res.setAccountPeriodyear(editpremium.get("ACCOUNT_PERIOD_YEAR")==null?"":editpremium.get("ACCOUNT_PERIOD_YEAR").toString());
					res.setCurrencyId(editpremium.get("CURRENCY_ID")==null?"":editpremium.get("CURRENCY_ID").toString());
					res.setCurrency(editpremium.get("CURRENCY_ID")==null?"":editpremium.get("CURRENCY_ID").toString());
					if(null==editpremium.get("EXCHANGE_RATE")){
						GetCommonValueRes res1 = dropDownImple.GetExchangeRate(req.getCurrencyId(),req.getTransaction(),req.getCountryId(),req.getBranchCode());
					res.setExchRate(res1.getCommonResponse());
					}
					else{
						res.setExchRate(dropDownImple.exchRateFormat(editpremium.get("EXCHANGE_RATE")==null?"":editpremium.get("EXCHANGE_RATE").toString()));
					}
					res.setBrokerage(editpremium.get("BROKERAGE_AMT_OC")==null?"":editpremium.get("BROKERAGE_AMT_OC").toString());
					res.setTax(editpremium.get("TAX_AMT_OC")==null?"":editpremium.get("TAX_AMT_OC").toString());
					res.setPremiumQuotaShare(editpremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editpremium.get("PREMIUM_QUOTASHARE_OC").toString());
					res.setCommissionQuotaShare(editpremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editpremium.get("COMMISSION_QUOTASHARE_OC").toString());
					res.setPremiumSurplus(editpremium.get("PREMIUM_SURPLUS_OC")==null?"":editpremium.get("PREMIUM_SURPLUS_OC").toString());
					res.setCommissionSurplus(editpremium.get("COMMISSION_SURPLUS_OC")==null?"":editpremium.get("COMMISSION_SURPLUS_OC").toString());
					res.setPremiumportifolioIn(editpremium.get("PREMIUM_PORTFOLIOIN_OC")==null?"":editpremium.get("PREMIUM_PORTFOLIOIN_OC").toString());
					res.setCliamPortfolioin(editpremium.get("CLAIM_PORTFOLIOIN_OC")==null?"":editpremium.get("CLAIM_PORTFOLIOIN_OC").toString());
					res.setPremiumportifolioout(editpremium.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":editpremium.get("PREMIUM_PORTFOLIOOUT_OC").toString());
					res.setLossReserveReleased(editpremium.get("LOSS_RESERVE_RELEASED_OC")==null?"":editpremium.get("LOSS_RESERVE_RELEASED_OC").toString());
					res.setPremiumReserveQuotaShare(editpremium.get("PREMIUMRESERVE_QUOTASHARE_OC")==null?"":editpremium.get("PREMIUMRESERVE_QUOTASHARE_OC").toString());
					res.setCashLossCredit(editpremium.get("CASH_LOSS_CREDIT_OC")==null?"":editpremium.get("CASH_LOSS_CREDIT_OC").toString());
					res.setLossReserveRetained(editpremium.get("LOSS_RESERVERETAINED_OC")==null?"":editpremium.get("LOSS_RESERVERETAINED_OC").toString());
					res.setProfitCommission(editpremium.get("PROFIT_COMMISSION_OC")==null?"":editpremium.get("PROFIT_COMMISSION_OC").toString());
					res.setCashLossPaid(editpremium.get("CASH_LOSSPAID_OC")==null?"":editpremium.get("CASH_LOSSPAID_OC").toString());
					res.setStatus(editpremium.get("STATUS")==null?"":editpremium.get("STATUS").toString());
					res.setNetDue(editpremium.get("NETDUE_OC")==null?"":editpremium.get("NETDUE_OC").toString());
					res.setEnteringMode(editpremium.get("ENTERING_MODE")==null?"":editpremium.get("ENTERING_MODE").toString());
					res.setReceiptno(editpremium.get("RECEIPT_NO")==null?"":editpremium.get("RECEIPT_NO").toString());
					res.setClaimspaid(editpremium.get("CLAIMS_PAID_OC")==null?"":editpremium.get("CLAIMS_PAID_OC").toString());
					//bean.setSettlement_status(editPremium.get("SETTLEMENT_STATUS"));
					res.setMdpremium(editpremium.get("M_DPREMIUM_OC")==null?"":editpremium.get("M_DPREMIUM_OC").toString());
					res.setAdjustmentpremium(editpremium.get("ADJUSTMENT_PREMIUM_OC")==null?"":editpremium.get("ADJUSTMENT_PREMIUM_OC").toString());
					res.setRecuirementpremium(editpremium.get("REC_PREMIUM_OC")==null?"":editpremium.get("REC_PREMIUM_OC").toString());
					res.setCommission(editpremium.get("COMMISSION")==null?"":editpremium.get("COMMISSION").toString());
					res.setInstlmentNo(editpremium.get("INSTALMENT_NUMBER")==null?"":editpremium.get("INSTALMENT_NUMBER").toString());
					res.setInceptionDate(editpremium.get("ENTRY_DATE")==null?"":formatDate(editpremium.get("ENTRY_DATE")));
					res.setXlCost(editpremium.get("XL_COST_OC")==null?"":editpremium.get("XL_COST_OC").toString());
					res.setCliamportfolioout(editpremium.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":editpremium.get("CLAIM_PORTFOLIO_OUT_OC").toString());
					res.setPremiumReserveReleased(editpremium.get("PREMIUM_RESERVE_REALSED_OC")==null?"":editpremium.get("PREMIUM_RESERVE_REALSED_OC").toString());
					res.setOtherCost(editpremium.get("OTHER_COST_OC")==null?"":editpremium.get("OTHER_COST_OC").toString());
					res.setCedentRef(editpremium.get("CEDANT_REFERENCE")==null?"":editpremium.get("CEDANT_REFERENCE").toString());
					res.setRemarks(editpremium.get("REMARKS")==null?"":editpremium.get("REMARKS").toString());
					res.setInterest(fm.formatter(editpremium.get("INTEREST_OC")==null?"":editpremium.get("INTEREST_OC").toString()));
					res.setOsClaimsLossUpdateOC(fm.formatter(editpremium.get("OSCLAIM_LOSSUPDATE_OC")==null?"":editpremium.get("OSCLAIM_LOSSUPDATE_OC").toString()));
					res.setOverrider(editpremium.get("OVERRIDER_AMT_OC")==null?"":editpremium.get("OVERRIDER_AMT_OC").toString());
					res.setOverriderUSD(editpremium.get("OVERRIDER_AMT_DC")==null?"":editpremium.get("OVERRIDER_AMT_DC").toString());
					res.setAmendmentDate(editpremium.get("AMENDMENT_DATE")==null?"":formatDate(editpremium.get("AMENDMENT_DATE")));
					res.setWithHoldingTaxOC(fm.formatter(editpremium.get("WITH_HOLDING_TAX_OC")==null?"":editpremium.get("WITH_HOLDING_TAX_OC").toString()));
			        res.setWithHoldingTaxDC(fm.formatter(editpremium.get("WITH_HOLDING_TAX_DC")==null?"":editpremium.get("WITH_HOLDING_TAX_DC").toString()));
			        res.setRicession(editpremium.get("RI_CESSION")==null?"":editpremium.get("RI_CESSION").toString());
			        res.setTaxDedectSource(fm.formatter(editpremium.get("TDS_OC")==null?"":editpremium.get("TDS_OC").toString()));
					res.setTaxDedectSourceDc(fm.formatter(editpremium.get("TDS_DC")==null?"":editpremium.get("TDS_DC").toString()));
					res.setServiceTax(fm.formatter(editpremium.get("ST_OC")==null?"":editpremium.get("ST_OC").toString()));
					res.setServiceTaxDc(fm.formatter(editpremium.get("ST_DC")==null?"":editpremium.get("ST_DC").toString()));
					res.setLossParticipation(fm.formatter(editpremium.get("LPC_OC")==null?"":editpremium.get("LPC_OC").toString()));
					res.setLossParticipationDC(fm.formatter(editpremium.get("LPC_DC")==null?"":editpremium.get("LPC_DC").toString()));
					res.setSlideScaleCom(fm.formatter(editpremium.get("SC_COMM_OC")==null?"":editpremium.get("SC_COMM_OC").toString()));
					res.setSlideScaleComDC(fm.formatter(editpremium.get("SC_COMM_DC")==null?"":editpremium.get("SC_COMM_DC").toString()));
					res.setSubProfitId(editpremium.get("PREMIUM_SUBCLASS")==null?"":editpremium.get("PREMIUM_SUBCLASS").toString());
					res.setPrAllocatedAmount(editpremium.get("PRD_ALLOCATED_TILL_DATE")==null?"":editpremium.get("PRD_ALLOCATED_TILL_DATE").toString());
					res.setLrAllocatedAmount(editpremium.get("LRD_ALLOCATED_TILL_DATE")==null?"":editpremium.get("LRD_ALLOCATED_TILL_DATE").toString());
					res.setStatementDate(editpremium.get("STATEMENT_DATE")==null?"":formatDate(editpremium.get("STATEMENT_DATE")));
					res.setOsbYN(editpremium.get("OSBYN")==null?"":editpremium.get("OSBYN").toString());
					res.setSectionName(editpremium.get("SECTION_NAME")==null?"":editpremium.get("SECTION_NAME").toString());
	//							res.setSectionType(editpremium.get("2")==null?"":editpremium.get("TRANS_DATE").toString());
					res.setAccountPeriodDate(editpremium.get("ACCOUNTING_PERIOD_DATE")==null?"":formatDate(editpremium.get("ACCOUNTING_PERIOD_DATE")));
					res.setPredepartment(editpremium.get("PREMIUM_CLASS")==null?"":editpremium.get("PREMIUM_CLASS").toString());
				}
			
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		  }catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		  }
		return response;
	}

	public premiumUpdateMethodRes premiumUpdateMethod(InsertPremiumReq beanObj) {
		premiumUpdateMethodRes response = new premiumUpdateMethodRes();
		premiumUpdateMethodRes1 res = new premiumUpdateMethodRes1();
		String query="";
		boolean saveFlag = false;
		try {
			updateAruguments(beanObj);
			String netDueOc="0";
			RskPremiumDetails entity = null;
			if("Temp".equalsIgnoreCase(beanObj.getTableType())){
				//PREMIUM_UPDATE_TREATYUPDATEPRE_TEMP
				 entity = pdRepo.findByContractNoAndRequestNo(new BigDecimal(beanObj.getContNo()),new BigDecimal(beanObj.getRequestNo()));	
			}else{
				//PREMIUM_UPDATE_TREATYUPDATEPRE
				 entity = pdRepo.findByContractNoAndTransactionNo(new BigDecimal(beanObj.getContNo()),new BigDecimal(beanObj.getTransactionNo()));
			}
			netDueOc= entity.getNetdueOc()==null?"0":entity.getNetdueOc().toString();
			
			if("Submit".equalsIgnoreCase(beanObj.getButtonStatus()) && "Temp".equalsIgnoreCase(beanObj.getTableType())){
				propPremiumCustomRepository.facTempStatusUpdate(beanObj);
				beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
		 		
		 		getTempToMainMove(beanObj);
		 		
		 		propPremiumCustomRepository.updateDepositRelease(beanObj.getContNo(),beanObj.getRequestNo(),beanObj.getTransactionNo(),"A");
		 		
				propPremiumCustomRepository.updatePremiumReserve(beanObj.getContNo(),beanObj.getRequestNo(),beanObj.getTransactionNo(),"A");

			 	propPremiumCustomRepository.updateLossReserve(beanObj.getContNo(),beanObj.getRequestNo(),beanObj.getTransactionNo(),"A");
		 	
			 	propPremiumCustomRepository.updateCashLossStatus(beanObj.getContNo(),beanObj.getRequestNo(),beanObj.getTransactionNo(),"A");
			 	
				GetCassLossCreditReq req = new GetCassLossCreditReq();
			 	List<GetCashLossCreditReq1> reqList = new ArrayList<GetCashLossCreditReq1>();
			 	req.setBranchCode(beanObj.getBranchCode());
			 	req.setClaimPayNo(beanObj.getClaimPayNo());
			 	req.setContNo(beanObj.getContNo());
			 	req.setCurrencyId(beanObj.getCurrencyId());
			 	req.setDepartmentId(beanObj.getDepartmentId());
			 	req.setMode(beanObj.getMode());
			 	for(int i=0; i<beanObj.getGetCashLossCreditReq1().size();i++) {
			 		GetCashLossCreditReq1 req1 = beanObj.getGetCashLossCreditReq1().get(i);
			 		GetCashLossCreditReq1 res1 = new GetCashLossCreditReq1();
			 		res1.setCLCsettlementRatelist(req1.getCLCsettlementRatelist());
			 		res1.setChkbox(req1.getChkbox());
			 		res1.setCreditAmountCLClist(req1.getCreditAmountCLClist());
			 		res1.setCreditAmountCLDlist(req1.getCreditAmountCLDlist());
			 		res1.setMainclaimPaymentNos(req1.getMainclaimPaymentNos());
			 		res1.setMainCLCsettlementRatelist(req1.getMainCLCsettlementRatelist());
			 		res1.setMaincreditAmountCLClist(req1.getMaincreditAmountCLClist());
			 		res1.setMaincreditAmountCLDlist(req1.getMaincreditAmountCLDlist());
			 		reqList.add(res1);			 	
			 	}		 					 	
			 	req.setGetCashLossCreditReq1(reqList);	
			 	
			 	GetCashLossCreditRes cash = getCassLossCredit(req);
			 	
			 	
			 	//GetCashLossCreditRes cash = getCassLossCredit(beanObj);
			 	 List<GetCashLossCreditRes1> cashLossList=cash.getCommonResponse();
			 	 for(int i=0;i<cashLossList.size();i++){
			 		GetCashLossCreditRes1 form= cashLossList.get(0);
			 		propPremiumCustomRepository.updateClaimPayment(form.getContNo(),beanObj.getBranchCode(),beanObj.getRequestNo(),"A",form.getClaimNumber(),form.getClaimPaymentNo(),form.getContNo(),form.getClaimNumber(),form.getClaimPaymentNo());
			 	 }
			 	propPremiumCustomRepository.premiumarchive(beanObj.getContNo(),(StringUtils.isBlank(beanObj.getLayerno())?"0":beanObj.getLayerno()),beanObj.getTransactionNo(),beanObj.getCurrencyId(),beanObj.getExchRate(),netDueOc,beanObj.getDepartmentId(),beanObj.getProductId());
		
			saveFlag=true;
			}
		 res.setTransactionNo(beanObj.getTransactionNo());
		 response.setResponse(res);
		 response.setMessage("Success");
		 response.setIsError(false);
		 }catch (Exception e) {
			log.error(e);
			saveFlag=false;
			e.printStackTrace();
			response.setResponse(res);
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	@Transactional
	public void updateAruguments(InsertPremiumReq beanObj) throws ParseException {
		
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		if("Temp".equalsIgnoreCase(beanObj.getTableType())){
			//PREMIUM_UPDATE_TREATYUPDATEPRE_TEMP
			RskPremiumDetailsTemp entity = pdTempRepo.findByContractNoAndRequestNo(new BigDecimal(beanObj.getContNo()),new BigDecimal(beanObj.getRequestNo()));	
			if(entity != null) {
				double premiumsurp=0.0;
				double premium=0.0; 
				entity.setTransactionMonthYear(sdf.parse(beanObj.getTransaction()));		
				entity.setAccountPeriodQtr(beanObj.getAccountPeriod());	
				entity.setAccountPeriodYear(new BigDecimal(beanObj.getAccountPeriodyear()));;
				entity.setCurrencyId(new BigDecimal(beanObj.getCurrencyId()));;
				entity.setExchangeRate(new BigDecimal(beanObj.getExchRate()));;
				entity.setBrokerage(new BigDecimal(beanObj.getBrokerageview()));;
				entity.setBrokerageAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getBrokerage(),beanObj)));;
				entity.setTax(new BigDecimal(beanObj.getTaxview()));;
				entity.setBrokerageAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageAmtOc().toString(), beanObj.getExchRate())));	
			
				entity.setTaxAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getTax(),beanObj)));	
				entity.setEntryDateTime(StringUtils.isEmpty(beanObj.getInceptionDate()) ?null :sdf.parse(beanObj.getInceptionDate()));	
				entity.setPremiumQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumQuotaShare(),beanObj)));	
				entity.setCommissionQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getCommissionQuotaShare(),beanObj)));;
				entity.setPremiumSurplusOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumSurplus(),beanObj)));	
				entity.setCommissionSurplusOc(new BigDecimal(getModeOfTransaction(beanObj.getCommissionSurplus(),beanObj)));;
				entity.setPremiumPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioIn(),beanObj)));;
				entity.setClaimPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamPortfolioin(),beanObj)));;
				entity.setPremiumPortfoliooutOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioout(),beanObj)));;
			
				entity.setLossReserveReleasedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveReleased(),beanObj)));;
				entity.setPremiumreserveQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveQuotaShare(),beanObj)));	
				entity.setCashLossCreditOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossCredit(),beanObj)));			
				entity.setLossReserveretainedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveRetained(),beanObj)));;
				entity.setProfitCommissionOc(new BigDecimal(getModeOfTransaction((StringUtils.isEmpty(beanObj.getProfitCommission()) ? "0" : beanObj.getProfitCommission()),beanObj)));;
				entity.setCashLosspaidOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossPaid(),beanObj)));;
				entity.setEnteringMode(beanObj.getEnteringMode());
				BigDecimal receiptno = StringUtils.isBlank(beanObj.getReceiptno())? null: new BigDecimal(beanObj.getReceiptno());
				entity.setReceiptNo(receiptno);
				entity.setClaimsPaidOc(new BigDecimal(getModeOfTransaction(beanObj.getClaimspaid(),beanObj)));		
				entity.setPremiumPortfolioinDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumPortfolioinOc().toString(),beanObj.getExchRate())));;
				entity.setClaimPortfolioinDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimPortfolioinOc().toString(), beanObj.getExchRate())));;
				entity.setPremiumPortfoliooutDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumPortfoliooutOc().toString(), beanObj.getExchRate())));
				entity.setLossReserveReleasedDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLossReserveReleasedOc().toString(), beanObj.getExchRate())));	
				entity.setPremiumreserveQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumreserveQuotashareOc().toString(), beanObj.getExchRate())));	
				entity.setCashLossCreditDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCashLossCreditOc().toString(), beanObj.getExchRate())));	
				if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare())||!StringUtils.isEmpty(beanObj.getPremiumSurplus()))
				{
					

					if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare()))
					{
						premium=Double.parseDouble(beanObj.getPremiumQuotaShare());
					}
					if(StringUtils.isEmpty(beanObj.getCommissionQuotaShare()))
					{
						final double commission=premium*(Double.parseDouble(beanObj.getCommissionview())/100);
						entity.setCommissionQuotashareOc(new BigDecimal(getModeOfTransaction(commission+" ",beanObj)));
						entity.setCommissionQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionQuotashareOc().toString(),beanObj.getExchRate())));
						
					}
					if(!StringUtils.isEmpty(beanObj.getPremiumSurplus()))
					{
						premiumsurp=(Double.parseDouble(beanObj.getPremiumSurplus()));
					}
					if(StringUtils.isEmpty(beanObj.getCommissionSurplus()))
					{
					
						double comsurp=premiumsurp*(Double.parseDouble(beanObj.getCommssionSurp())/100);
						entity.setCommissionSurplusOc(new BigDecimal(getModeOfTransaction(comsurp+" ",beanObj)));
						entity.setCommissionSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionSurplusOc().toString(), beanObj.getExchRate())));
						

					}
					if(StringUtils.isEmpty(beanObj.getBrokerage()))
					{
						double brokerage=(premium+premiumsurp)*(Double.parseDouble(beanObj.getBrokerageview())/100);
						entity.setBrokerageAmtOc(new BigDecimal(getModeOfTransaction(brokerage+" ",beanObj)));
						entity.setBrokerageAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageAmtOc().toString(), beanObj.getExchRate())));
					
					}
					if(StringUtils.isEmpty(beanObj.getTax()))
					{
						double tax=(premium+premiumsurp)*(Double.parseDouble(beanObj.getTaxview())/100);
						entity.setTaxAmtOc(new BigDecimal(getModeOfTransaction(tax+" ",beanObj)));	
						entity.setTaxAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())))	;
					
					
					}
					if(StringUtils.isEmpty(beanObj.getOverrider()))
					{
						double overrider=(premium+premiumsurp)*(Double.parseDouble(beanObj.getOverRiderview())/100);
						entity.setOverriderAmtOc(new BigDecimal(getModeOfTransaction(overrider+" ",beanObj)));
						entity.setOverriderAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())));
						
						
					}
				}
				
				entity.setLossReserveretainedDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLossReserveretainedOc().toString(), beanObj.getExchRate())));
				entity.setProfitCommissionDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getProfitCommissionOc().toString(), beanObj.getExchRate())));			
				entity.setCashLosspaidDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCashLosspaidOc().toString(), beanObj.getExchRate())));
				entity.setCashLosspaidDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimsPaidOc().toString(), beanObj.getExchRate())));	
			
				
				entity.setSettlementStatus(beanObj.getSettlementstatus());;
				entity.setXlCostOc(new BigDecimal(getModeOfTransaction(beanObj.getXlCost(),beanObj)));	
				entity.setClaimPortfolioOutOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamportfolioout(),beanObj)));
				entity.setPremiumReserveRealsedOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveReleased(),beanObj)));;
			
				entity.setOtherCostOc(new BigDecimal(getModeOfTransaction(beanObj.getOtherCost().toString(),beanObj)));;
				entity.setXlCostDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getXlCostOc().toString(), beanObj.getExchRate())));
				entity.setClaimPortfolioOutDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimPortfolioOutOc().toString(), beanObj.getExchRate())));	
				entity.setPremiumReserveReleaseDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumReserveRealsedOc().toString(), beanObj.getExchRate())));	
				;
				entity.setOtherCostDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOtherCostOc().toString(), beanObj.getExchRate())));;
				entity.setCedantReference(beanObj.getCedentRef());
				entity.setRemarks(beanObj.getRemarks());	
				entity.setTotalCrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalCredit(),beanObj)));
				entity.setTotalCrDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTotalCrOc().toString(),beanObj.getExchRate())));;
				entity.setTotalDrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalDebit(),beanObj)));	
				entity.setTotalDrDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTotalDrOc().toString(),beanObj.getExchRate())));;
				entity.setInterestOc(new BigDecimal(getModeOfTransaction(beanObj.getInterest(),beanObj)));
				entity.setInterestDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getInterestOc().toString(),beanObj.getExchRate())));
				entity.setOsclaimLossupdateOc(StringUtils.isEmpty(beanObj.getOsClaimsLossUpdateOC())?BigDecimal.ZERO:new BigDecimal(getModeOfTransaction(beanObj.getOsClaimsLossUpdateOC().toString(),beanObj)));
				entity.setOsclaimLossupdateDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOsclaimLossupdateOc().toString(),beanObj.getExchRate())));;
				Date amendmentdate = StringUtils.isBlank(beanObj.getAmendmentDate())? null: (sdf.parse(beanObj.getAmendmentDate()));
				entity.setAmendmentDate(amendmentdate);	
				entity.setWithHoldingTaxOc(StringUtils.isEmpty(beanObj.getWithHoldingTaxOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getWithHoldingTaxOC()));
				entity.setWithHoldingTaxDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getWithHoldingTaxOc().toString(), beanObj.getExchRate())));	
				entity.setRiCession(beanObj.getRiCession());		
				entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));
				entity.setTdsOc(new BigDecimal(getModeOfTransaction(beanObj.getTaxDedectSource(),beanObj)));
				entity.setTdsDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTdsOc().toString(), beanObj.getExchRate())));
				entity.setVatPremiumOc(new BigDecimal(getModeOfTransaction(beanObj.getVatPremium(),beanObj)));
				entity.setVatPremiumDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getVatPremiumOc().toString(), beanObj.getExchRate())));
				entity.setScCommOc(new BigDecimal(getModeOfTransaction(beanObj.getSlideScaleCom(),beanObj)));;
				entity.setScCommDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getScCommOc().toString(), beanObj.getExchRate())));			
				entity.setPremiumClass(beanObj.getPredepartment());	
				entity.setPremiumSubclass(beanObj.getSubProfitId().replace(" ", ""));
				entity.setAccountingPeriodDate(sdf.parse(beanObj.getAccountPeriodDate()));
				entity.setStatementDate(sdf.parse(beanObj.getStatementDate()));
				entity.setOsbyn(beanObj.getOsbYN());	
				entity.setLpcOc(new BigDecimal(getModeOfTransaction(beanObj.getLossParticipation().toString(),beanObj)));
				entity.setLpcDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLpcOc().toString(), beanObj.getExchRate())));
				entity.setSectionName(beanObj.getSectionName());		
				
		
				
				entity.setTaxAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())))	;	
				entity.setOverriderAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getOverrider(),beanObj)));
				entity.setOverriderAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOverriderAmtOc().toString(),beanObj.getExchRate())));;
				entity.setOverrider(new BigDecimal(beanObj.getOverRiderview()));;
				
				entity.setPremiumQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumQuotashareOc().toString(), beanObj.getExchRate())));;
				entity.setCommissionQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionQuotashareOc().toString(), beanObj.getExchRate())));	
				entity.setPremiumSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumSurplusOc().toString(), beanObj.getExchRate())));;
				entity.setCommissionSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionSurplusOc().toString(), beanObj.getExchRate())));;
				entity.setBrokerageVatOc(new BigDecimal(getModeOfTransaction(beanObj.getBrokerageVat(),beanObj)));
				entity.setBrokerageVatDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageVatOc().toString(), beanObj.getExchRate())));
				entity.setDocumentType(beanObj.getDocumentType());
				entity.setNetdueOc(new BigDecimal(updateNetDue(entity,getModeOfTransaction(beanObj.getClaimspaid(),beanObj))));
				entity.setNetdueDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getNetdueOc().toString(), beanObj.getExchRate())));
				 entity.setMovementYn(null);
				 entity.setEntryDate(new Date());
				 entity.setM1Oc(new BigDecimal(StringUtils.isBlank(beanObj.getM1oc())?"0":beanObj.getM1oc()));	
				 entity.setM2Oc(new BigDecimal(StringUtils.isBlank(beanObj.getM2oc())?"0":beanObj.getM2oc()));	
				 entity.setM3Oc(new BigDecimal(StringUtils.isBlank(beanObj.getM3oc())?"0":beanObj.getM3oc()));	
				 pdTempRepo.save(entity);

			} 
			}else{
			//PREMIUM_UPDATE_TREATYUPDATEPRE
			RskPremiumDetails	 entity = pdRepo.findByContractNoAndTransactionNo(new BigDecimal(beanObj.getContNo()),new BigDecimal(beanObj.getTransactionNo()));
			 if(entity != null) {
					double premiumsurp=0.0;
					double premium=0.0; 
					entity.setTransactionMonthYear(sdf.parse(beanObj.getTransaction()));		
					entity.setAccountPeriodQtr(beanObj.getAccountPeriod());	
					entity.setAccountPeriodYear(new BigDecimal(beanObj.getAccountPeriodyear()));;
					entity.setCurrencyId(new BigDecimal(beanObj.getCurrencyId()));;
					entity.setExchangeRate(new BigDecimal(beanObj.getExchRate()));;
					entity.setBrokerage(new BigDecimal(beanObj.getBrokerageview()));;
					entity.setBrokerageAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getBrokerage(),beanObj)));;
					entity.setTax(new BigDecimal(beanObj.getTaxview()));;
					entity.setBrokerageAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageAmtOc().toString(), beanObj.getExchRate())));	
				
					entity.setTaxAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getTax(),beanObj)));	
					entity.setEntryDateTime(StringUtils.isEmpty(beanObj.getInceptionDate()) ?null :sdf.parse(beanObj.getInceptionDate()));	
					entity.setPremiumQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumQuotaShare(),beanObj)));	
					entity.setCommissionQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getCommissionQuotaShare(),beanObj)));;
					entity.setPremiumSurplusOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumSurplus(),beanObj)));	
					entity.setCommissionSurplusOc(new BigDecimal(getModeOfTransaction(beanObj.getCommissionSurplus(),beanObj)));;
					entity.setPremiumPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioIn(),beanObj)));;
					entity.setClaimPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamPortfolioin(),beanObj)));;
					entity.setPremiumPortfoliooutOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioout(),beanObj)));;
				
					entity.setLossReserveReleasedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveReleased(),beanObj)));;
					entity.setPremiumreserveQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveQuotaShare(),beanObj)));	
					entity.setCashLossCreditOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossCredit(),beanObj)));			
					entity.setLossReserveretainedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveRetained(),beanObj)));;
					entity.setProfitCommissionOc(new BigDecimal(getModeOfTransaction((StringUtils.isEmpty(beanObj.getProfitCommission()) ? "0" : beanObj.getProfitCommission()),beanObj)));;
					entity.setCashLosspaidOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossPaid(),beanObj)));;
					entity.setEnteringMode(beanObj.getEnteringMode());
					entity.setReceiptNo(StringUtils.isBlank(beanObj.getReceiptno())?null:new BigDecimal(beanObj.getReceiptno()));	
					entity.setClaimsPaidOc(new BigDecimal(getModeOfTransaction(beanObj.getClaimspaid(),beanObj)));		
					entity.setPremiumPortfolioinDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumPortfolioinOc().toString(),beanObj.getExchRate())));;
					entity.setClaimPortfolioinDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimPortfolioinOc().toString(), beanObj.getExchRate())));;
					entity.setPremiumPortfoliooutDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumPortfoliooutOc().toString(), beanObj.getExchRate())));
					entity.setLossReserveReleasedDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLossReserveReleasedOc().toString(), beanObj.getExchRate())));	
					entity.setPremiumreserveQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumreserveQuotashareOc().toString(), beanObj.getExchRate())));	
					entity.setCashLossCreditDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCashLossCreditOc().toString(), beanObj.getExchRate())));	
					if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare())||!StringUtils.isEmpty(beanObj.getPremiumSurplus()))
					{
						

						if(!StringUtils.isEmpty(beanObj.getPremiumQuotaShare()))
						{
							premium=Double.parseDouble(beanObj.getPremiumQuotaShare());
						}
						if(StringUtils.isEmpty(beanObj.getCommissionQuotaShare()))
						{
							final double commission=premium*(Double.parseDouble(beanObj.getCommissionview())/100);
							entity.setCommissionQuotashareOc(new BigDecimal(getModeOfTransaction(commission+" ",beanObj)));
							entity.setCommissionQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionQuotashareOc().toString(),beanObj.getExchRate())));
							
						}
						if(!StringUtils.isEmpty(beanObj.getPremiumSurplus()))
						{
							premiumsurp=(Double.parseDouble(beanObj.getPremiumSurplus()));
						}
						if(StringUtils.isEmpty(beanObj.getCommissionSurplus()))
						{
						
							double comsurp=premiumsurp*(Double.parseDouble(beanObj.getCommssionSurp())/100);
							entity.setCommissionSurplusOc(new BigDecimal(getModeOfTransaction(comsurp+" ",beanObj)));
							entity.setCommissionSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionSurplusOc().toString(), beanObj.getExchRate())));
							

						}
						if(StringUtils.isEmpty(beanObj.getBrokerage()))
						{
							double brokerage=(premium+premiumsurp)*(Double.parseDouble(beanObj.getBrokerageview())/100);
							entity.setBrokerageAmtOc(new BigDecimal(getModeOfTransaction(brokerage+" ",beanObj)));
							entity.setBrokerageAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageAmtOc().toString(), beanObj.getExchRate())));
						
						}
						if(StringUtils.isEmpty(beanObj.getTax()))
						{
							double tax=(premium+premiumsurp)*(Double.parseDouble(beanObj.getTaxview())/100);
							entity.setTaxAmtOc(new BigDecimal(getModeOfTransaction(tax+" ",beanObj)));	
							entity.setTaxAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())))	;
						
						
						}
						if(StringUtils.isEmpty(beanObj.getOverrider()))
						{
							double overrider=(premium+premiumsurp)*(Double.parseDouble(beanObj.getOverRiderview())/100);
							entity.setOverriderAmtOc(new BigDecimal(getModeOfTransaction(overrider+" ",beanObj)));
							entity.setOverriderAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())));
							
							
						}
					}
					
					entity.setLossReserveretainedDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLossReserveretainedOc().toString(), beanObj.getExchRate())));
					entity.setProfitCommissionDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getProfitCommissionOc().toString(), beanObj.getExchRate())));			
					entity.setCashLosspaidDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCashLosspaidOc().toString(), beanObj.getExchRate())));
					entity.setCashLosspaidDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimsPaidOc().toString(), beanObj.getExchRate())));	
				
					
					entity.setSettlementStatus(beanObj.getSettlementstatus());;
					entity.setXlCostOc(new BigDecimal(getModeOfTransaction(beanObj.getXlCost(),beanObj)));	
					entity.setClaimPortfolioOutOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamportfolioout(),beanObj)));
					entity.setPremiumReserveRealsedOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveReleased(),beanObj)));;
				
					entity.setOtherCostOc(new BigDecimal(getModeOfTransaction(beanObj.getOtherCost().toString(),beanObj)));;
					entity.setXlCostDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getXlCostOc().toString(), beanObj.getExchRate())));
					entity.setClaimPortfolioOutDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getClaimPortfolioOutOc().toString(), beanObj.getExchRate())));	
					entity.setPremiumReserveReleaseDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumReserveRealsedOc().toString(), beanObj.getExchRate())));	
					;
					entity.setOtherCostDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOtherCostOc().toString(), beanObj.getExchRate())));;
					entity.setCedantReference(beanObj.getCedentRef());
					entity.setRemarks(beanObj.getRemarks());	
					entity.setTotalCrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalCredit(),beanObj)));
					entity.setTotalCrDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTotalCrOc().toString(),beanObj.getExchRate())));;
					entity.setTotalDrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalDebit(),beanObj)));	
					entity.setTotalDrDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTotalDrOc().toString(),beanObj.getExchRate())));;
					entity.setInterestOc(new BigDecimal(getModeOfTransaction(beanObj.getInterest(),beanObj)));
					entity.setInterestDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getInterestOc().toString(),beanObj.getExchRate())));
					entity.setOsclaimLossupdateOc(StringUtils.isEmpty(beanObj.getOsClaimsLossUpdateOC())?BigDecimal.ZERO:new BigDecimal(getModeOfTransaction(beanObj.getOsClaimsLossUpdateOC().toString(),beanObj)));
					entity.setOsclaimLossupdateDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOsclaimLossupdateOc().toString(),beanObj.getExchRate())));;
					entity.setAmendmentDate(sdf.parse(beanObj.getAmendmentDate()));	
					entity.setWithHoldingTaxOc(StringUtils.isEmpty(beanObj.getWithHoldingTaxOC()) ? BigDecimal.ZERO : new BigDecimal(beanObj.getWithHoldingTaxOC()));
					entity.setWithHoldingTaxDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getWithHoldingTaxOc().toString(), beanObj.getExchRate())));	
					entity.setRiCession(beanObj.getRiCession());		
					entity.setSubClass(new BigDecimal(beanObj.getDepartmentId()));
					entity.setTdsOc(new BigDecimal(getModeOfTransaction(beanObj.getTaxDedectSource(),beanObj)));
					entity.setTdsDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTdsOc().toString(), beanObj.getExchRate())));
					entity.setVatPremiumOc(new BigDecimal(getModeOfTransaction(beanObj.getVatPremium(),beanObj)));
					entity.setVatPremiumDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getVatPremiumOc().toString(), beanObj.getExchRate())));
					entity.setScCommOc(new BigDecimal(getModeOfTransaction(beanObj.getSlideScaleCom(),beanObj)));;
					entity.setScCommDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getScCommOc().toString(), beanObj.getExchRate())));			
					entity.setPremiumClass(beanObj.getPredepartment());	
					entity.setPremiumSubclass(beanObj.getSubProfitId().replace(" ", ""));
					entity.setAccountingPeriodDate(sdf.parse(beanObj.getAccountPeriodDate()));
					entity.setStatementDate(sdf.parse(beanObj.getStatementDate()));
					entity.setOsbyn(beanObj.getOsbYN());	
					entity.setLpcOc(new BigDecimal(getModeOfTransaction(beanObj.getLossParticipation().toString(),beanObj)));
					entity.setLpcDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getLpcOc().toString(), beanObj.getExchRate())));
					entity.setSectionName(beanObj.getSectionName());		
			
			
					
					entity.setTaxAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())))	;	
					entity.setOverriderAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getOverrider(),beanObj)));
					entity.setOverriderAmtDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getOverriderAmtOc().toString(),beanObj.getExchRate())));;
					entity.setOverrider(new BigDecimal(beanObj.getOverRiderview()));;
					
					entity.setPremiumQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumQuotashareOc().toString(), beanObj.getExchRate())));;
					entity.setCommissionQuotashareDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionQuotashareOc().toString(), beanObj.getExchRate())));	
					entity.setPremiumSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getPremiumSurplusOc().toString(), beanObj.getExchRate())));;
					entity.setCommissionSurplusDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getCommissionSurplusOc().toString(), beanObj.getExchRate())));;
					entity.setBrokerageVatOc(new BigDecimal(getModeOfTransaction(beanObj.getBrokerageVat(),beanObj)));
					entity.setBrokerageVatDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getBrokerageVatOc().toString(), beanObj.getExchRate())));
					entity.setDocumentType(beanObj.getDocumentType());
					entity.setNetdueOc(new BigDecimal(updateNetDue(entity,getModeOfTransaction(beanObj.getClaimspaid(),beanObj))));
					entity.setNetdueDc(new BigDecimal(dropDownImple.GetDesginationCountry(entity.getNetdueOc().toString(), beanObj.getExchRate())));
					 entity.setMovementYn(null);
					 entity.setEntryDate(new Date());
					 entity.setM1Oc(new BigDecimal(beanObj.getM1oc()));	
					 entity.setM2Oc(new BigDecimal(beanObj.getM2oc()));	
					 entity.setM3Oc(new BigDecimal(beanObj.getM3oc()));	
					 pdRepo.save(entity);

		}
		}
		
	}
	private static String updateNetDue(RskPremiumDetailsTemp entity, String modeOfTransaction) {
		double Aut=0;
		double But=0;
		if(StringUtils.isNotEmpty(entity.getPremiumQuotashareOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumSurplusOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfolioinOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioinOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getClaimPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveReleasedOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getLossReserveReleasedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLossCreditOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getCashLossCreditOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumReserveRealsedOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getPremiumReserveRealsedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getInterestOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getInterestOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTdsOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getTdsOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getVatPremiumOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getVatPremiumOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLpcOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getLpcOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionQuotashareOc().toString()));
		{
			But+=Double.parseDouble(entity.getCommissionQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionSurplusOc().toString()));
		{
			But+=Double.parseDouble(entity.getCommissionSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getBrokerageAmtOc().toString()))
		{
			But+=Double.parseDouble(entity.getBrokerageAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTaxAmtOc().toString()));
		{
			But+=Double.parseDouble(entity.getTaxAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfoliooutOc().toString()));
		{
			But+=Double.parseDouble(entity.getPremiumPortfoliooutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumreserveQuotashareOc().toString()));
		{
			But+=Double.parseDouble(entity.getPremiumreserveQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveretainedOc().toString()));
		{
			But+=Double.parseDouble(entity.getLossReserveretainedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getProfitCommissionOc().toString()))
		{
			But+=Double.parseDouble(entity.getProfitCommissionOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLosspaidOc().toString()));
		{
			But+=Double.parseDouble(entity.getCashLosspaidOc().toString());
		}
		if(StringUtils.isNotEmpty(modeOfTransaction))
		{
			But+=Double.parseDouble(modeOfTransaction);
		}
		if(StringUtils.isNotEmpty(entity.getOtherCostOc().toString()));
		{
			But+=Double.parseDouble(entity.getOtherCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getXlCostOc().toString()));
		{
			But+=Double.parseDouble(entity.getXlCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioOutOc().toString()));
		{
			But+=Double.parseDouble(entity.getClaimPortfolioOutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getOverriderAmtOc().toString()));
		{
			But+=Double.parseDouble(entity.getOverriderAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getWithHoldingTaxOc().toString()));
		{
			But+=Double.parseDouble(entity.getWithHoldingTaxOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getScCommOc().toString()));
		{
			But+=Double.parseDouble(entity.getScCommOc().toString());
		}if(StringUtils.isNotEmpty(entity.getBrokerageVatOc().toString()));
		{
			But+=Double.parseDouble(entity.getBrokerageVatOc().toString());
		}
		
	    double cut=Aut-But;
	 
		return String.valueOf(cut);
	}
	private static String updateNetDue(RskPremiumDetails entity,final String claimpaid) {
		double Aut=0;
		double But=0;
		if(StringUtils.isNotEmpty(entity.getPremiumQuotashareOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumSurplusOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfolioinOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getPremiumPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioinOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getClaimPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveReleasedOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getLossReserveReleasedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLossCreditOc().toString()));
		{
		Aut+=Double.parseDouble(entity.getCashLossCreditOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumReserveRealsedOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getPremiumReserveRealsedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getInterestOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getInterestOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTdsOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getTdsOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getVatPremiumOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getVatPremiumOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLpcOc().toString()));
		{
			Aut+=Double.parseDouble(entity.getLpcOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionQuotashareOc().toString()));
		{
			But+=Double.parseDouble(entity.getCommissionQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionSurplusOc().toString()));
		{
			But+=Double.parseDouble(entity.getCommissionSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getBrokerageAmtOc().toString()))
		{
			But+=Double.parseDouble(entity.getBrokerageAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTaxAmtOc().toString()));
		{
			But+=Double.parseDouble(entity.getTaxAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfoliooutOc().toString()));
		{
			But+=Double.parseDouble(entity.getPremiumPortfoliooutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumreserveQuotashareOc().toString()));
		{
			But+=Double.parseDouble(entity.getPremiumreserveQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveretainedOc().toString()));
		{
			But+=Double.parseDouble(entity.getLossReserveretainedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getProfitCommissionOc().toString()))
		{
			But+=Double.parseDouble(entity.getProfitCommissionOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLosspaidOc().toString()));
		{
			But+=Double.parseDouble(entity.getCashLosspaidOc().toString());
		}
		if(StringUtils.isNotEmpty(claimpaid))
		{
			But+=Double.parseDouble(claimpaid);
		}
		if(StringUtils.isNotEmpty(entity.getOtherCostOc().toString()));
		{
			But+=Double.parseDouble(entity.getOtherCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getXlCostOc().toString()));
		{
			But+=Double.parseDouble(entity.getXlCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioOutOc().toString()));
		{
			But+=Double.parseDouble(entity.getClaimPortfolioOutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getOverriderAmtOc().toString()));
		{
			But+=Double.parseDouble(entity.getOverriderAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getWithHoldingTaxOc().toString()));
		{
			But+=Double.parseDouble(entity.getWithHoldingTaxOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getScCommOc().toString()));
		{
			But+=Double.parseDouble(entity.getScCommOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getBrokerageVatOc().toString()));
		{
			But+=Double.parseDouble(entity.getBrokerageVatOc().toString());
		}
		
	    double cut=Aut-But;
	 
		return String.valueOf(cut);
	}
	public GetVatInfoRes getVatInfo(GetVatInfoReq req) {
		GetVatInfoRes response = new GetVatInfoRes();
		GetVatInfoRes1 res = new GetVatInfoRes1();
		try {
			List<Map<String,Object>> list = queryImpl.selectList("Select.premium.vatInfo",new String[] {req.getPremiumAmount(),req.getProposalNo(),req.getBranchCode()});			
			if(list.size()>0){
				Map<String,Object> tempMap = (Map<String,Object>) list.get(0);
				res.setBrokerageAmt(tempMap.get("BROKERAGE_AMT")==null?"":tempMap.get("BROKERAGE_AMT").toString());
				res.setBrokerageVat(tempMap.get("BROKERAGE_VAT")==null?"":tempMap.get("BROKERAGE_VAT").toString());		
				res.setPremiumVat(tempMap.get("PREMIUM_VAT")==null?"":tempMap.get("PREMIUM_VAT").toString());
				res.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
				response.setCommonResponse(res);	
			}			
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		    return response;
	}

	 
	public ViewRIPremiumListRes viewRIPremiumList(GetRIPremiumListReq req) { //getPremiumDetails response
		ViewRIPremiumListRes response = new ViewRIPremiumListRes();
		List<ViewRIPremiumListRes1> resList = new ArrayList<ViewRIPremiumListRes1>();
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
			
			//ACCOUNT_PERIOD_QTR
			Subquery<String> act = query.subquery(String.class); 
			Root<ConstantDetail> name = act.from(ConstantDetail.class);
			act.select(name.get("detailName"));
			Predicate j1 = cb.equal( name.get("categoryId"), "49");
			Predicate j2 = cb.equal( name.get("type"), pm.get("accountPeriodQtr")); //
			act.where(j1,j2);
			
			//TMAS_DEPARTMENT_NAME
			Subquery<String> dept = query.subquery(String.class); 
			Root<TmasDepartmentMaster> dname = dept.from(TmasDepartmentMaster.class);
			dept.select(dname.get("tmasDepartmentName"));
			Predicate k1 = cb.equal( dname.get("tmasDepartmentId"), pm.get("premiumClass"));
			Predicate k2 = cb.equal( dname.get("branchCode"), pm.get("branchCode"));
			Predicate k3 = cb.equal( dname.get("tmasProductId"), req.getProductId());
			Predicate k4 = cb.equal( dname.get("tmasStatus"), "Y");
			dept.where(k1,k2,k3,k4);

			query.multiselect(reInsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("brokerId").alias("BROKER_ID"),pm.get("brokerage").alias("BROKERAGE"),
					pm.get("signShared").alias("SIGN_SHARED"),pm.get("reinsurerId").alias("REINSURER_ID"),
					act.alias("ACCOUNT_PERIOD_QTR"),dept.alias("TMAS_DEPARTMENT_NAME"),pm.alias("table")
					); 
			
			Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
			Predicate n2 = cb.equal(pm.get("transactionNo"), req.getTransactionNo());
			Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple data1: list) {
					ViewRIPremiumListRes1 res = new ViewRIPremiumListRes1();
					RskPremiumDetailsRi data = (RskPremiumDetailsRi) data1.get("table");	
					
					res.setBrokerage(data1.get("BROKERAGE")==null?"":data1.get("BROKERAGE").toString());
					res.setBrokerId(data1.get("BROKER_ID")==null?"":data1.get("BROKER_ID").toString());
					res.setBrokerName(data1.get("BROKER_NAME")==null?"":data1.get("BROKER_NAME").toString());
					res.setReinsurerId(data1.get("REINSURER_ID")==null?"":data1.get("REINSURER_ID").toString());
					res.setReInsurerName(data1.get("REINSURER_NAME")==null?"":data1.get("REINSURER_NAME").toString());
					res.setSignShared(data1.get("SIGN_SHARED")==null?"":data1.get("SIGN_SHARED").toString());	
					res.setAccountPeriod(data1.get("ACCOUNT_PERIOD_QTR")==null?"":data1.get("ACCOUNT_PERIOD_QTR").toString());
					res.setPremiumClass(data1.get("TMAS_DEPARTMENT_NAME")==null?"":data1.get("TMAS_DEPARTMENT_NAME").toString());
					
					res.setContNo(data.getContractNo()==null?"":data.getContractNo().toString());
					res.setTransactionNo(data.getTransactionNo()==null?"":data.getTransactionNo().toString());
					res.setTransaction(data.getTransactionMonthYear()==null?"":sdf.format(data.getTransactionMonthYear()));
					res.setBrokerage(data.getBrokerageAmtOc()==null?"":fm.formatter(data.getBrokerageAmtOc().toString()));
					res.setTax(data.getTaxAmtOc()==null?"":fm.formatter(data.getTaxAmtOc().toString()));
					res.setPremiumQuotaShare(data.getPremiumQuotashareOc()==null?"":fm.formatter(data.getPremiumQuotashareOc().toString()));
					res.setCommissionQuotaShare(data.getCommissionQuotashareOc()==null?"":fm.formatter(data.getCommissionQuotashareOc().toString()));
					res.setPremiumSurplus(data.getPremiumSurplusOc()==null?"":fm.formatter(data.getPremiumSurplusOc().toString()));
					res.setCommissionSurplus(data.getCommissionSurplusOc()==null?"":fm.formatter(data.getCommissionSurplusOc().toString()));
					res.setPremiumportifolioIn(data.getPremiumPortfolioinOc()==null?"":fm.formatter(data.getPremiumPortfolioinOc().toString()));
					res.setCliamPortfolioin(data.getClaimPortfolioinOc()==null?"":fm.formatter(data.getClaimPortfolioinOc().toString()));
					res.setPremiumportifolioout(data.getPremiumPortfoliooutOc()==null?"":fm.formatter(data.getPremiumPortfoliooutOc().toString()));
						
			
						res.setLossReserveReleased(data.getLossReserveReleasedOc()==null?"":fm.formatter(data.getLossReserveReleasedOc().toString()));
						res.setPremiumReserveQuotaShare(data.getPremiumreserveQuotashareOc()==null?"":fm.formatter(data.getPremiumreserveQuotashareOc().toString()));
						res.setCashLossCredit(data.getCashLossCreditOc()==null?"":fm.formatter(data.getCashLossCreditOc().toString()));
						res.setLossReserveRetained(data.getLossReserveReleasedOc()==null?"":fm.formatter(data.getLossReserveReleasedOc().toString()));
						
						res.setProfitCommission(data.getProfitCommissionOc()==null?"":fm.formatter(data.getProfitCommissionOc().toString()));
						res.setCashLossPaid(data.getCashLosspaidOc()==null?"":fm.formatter(data.getCashLosspaidOc().toString()));
						res.setNetDue(data.getNetdueOc()==null?"":fm.formatter(data.getNetdueOc().toString()));
						res.setReceiptno(data.getReceiptNo()==null?"":data.getReceiptNo().toString());
					
						res.setClaimsPaid(data.getClaimsPaidOc()==null?"":fm.formatter(data.getClaimsPaidOc().toString()));
						res.setInceptionDate(data.getEntryDate()==null?"":sdf.format(data.getEntryDate()));

						res.setXlCost(data.getXlCostOc()==null?"":fm.formatter(fm.formatter(data.getXlCostOc().toString())));
						res.setCliamPortfolioOut(data.getClaimPortfolioOutOc()==null?"":fm.formatter(data.getClaimPortfolioOutOc().toString()));
						res.setPremiumReserveReleased(data.getPremiumReserveRealsedOc()==null?"":fm.formatter(data.getPremiumReserveRealsedOc().toString()));
						res.setAccountPeriod(data.getAccountPeriodQtr()==null?"":data.getAccountPeriodQtr().toString());

						res.setXlCost(data.getXlCostOc()==null?"":dropDownImple.formatter(dropDownImple.formatter(data.getXlCostOc().toString())));
						res.setCliamPortfolioOut(data.getClaimPortfolioOutOc()==null?"":dropDownImple.formatter(data.getClaimPortfolioOutOc().toString()));
						res.setPremiumReserveReleased(data.getPremiumReserveRealsedOc()==null?"":dropDownImple.formatter(data.getPremiumReserveRealsedOc().toString()));

						res.setAccountPeriodYear(data.getAccountPeriodYear()==null?"":data.getAccountPeriodYear().toString());
						res.setCurrencyId(data.getCurrencyId()==null?"":data.getCurrencyId().toString());
						res.setOtherCost(data.getOtherCostOc()==null?"":fm.formatter(data.getOtherCostOc().toString()));
						res.setBrokerageUsd(data.getBrokerageAmtDc()==null?"":fm.formatter(data.getBrokerageAmtDc().toString()));
						res.setTaxUsd(data.getTaxAmtDc()==null?"":fm.formatter(data.getTaxAmtDc().toString()));
						res.setPremiumQuotaShareUsd(data.getPremiumreserveQuotashareDc()==null?"":fm.formatter(data.getPremiumreserveQuotashareDc().toString()));
						res.setCommsissionQuotaShareUsd(data.getCommissionQuotashareDc()==null?"":fm.formatter(data.getCommissionQuotashareDc().toString()));
						res.setPremiumSurplusUsd(data.getPremiumSurplusDc()==null?"":fm.formatter(data.getPremiumSurplusDc().toString()));
						res.setComissionSurplusUsd(data.getCommissionSurplusDc()==null?"":fm.formatter(data.getCommissionSurplusDc().toString()));
						res.setPremiumPortfolioInUsd(data.getPremiumPortfolioinDc()==null?"":fm.formatter(data.getPremiumPortfolioinDc().toString()));
						res.setCliamPortfolioUsd(data.getClaimPortfolioinDc()==null?"":fm.formatter(data.getClaimPortfolioinDc().toString()));
						res.setPremiumPortfolioOutUsd(data.getPremiumPortfoliooutDc()==null?"":fm.formatter(data.getPremiumPortfoliooutDc().toString()));
						res.setLossReserveReleasedUsd(data.getLossReserveReleasedDc()==null?"":fm.formatter(data.getLossReserveReleasedDc().toString()));
						res.setPremiumReserveQuotaShareUsd(data.getPremiumreserveQuotashareDc()==null?"":fm.formatter(data.getPremiumreserveQuotashareDc().toString()));
						res.setCashLossCreditUsd(data.getCashLossCreditDc()==null?"":fm.formatter(data.getCashLossCreditDc().toString()));
						res.setLossReserveRetainedUsd(data.getLossReserveretainedDc()==null?"":fm.formatter(data.getLossReserveretainedDc().toString()));
						res.setProfitCommissionUsd(data.getProfitCommissionDc()==null?"":fm.formatter(data.getProfitCommissionDc().toString()));
						res.setCashLossPaidUsd(data.getCashLosspaidDc()==null?"":fm.formatter(data.getCashLosspaidDc().toString()));
						res.setClamsPaidUsd(data.getClaimsPaidDc()==null?"":fm.formatter(data.getClaimsPaidDc().toString()));
						res.setXlCostUsd(data.getXlCostDc()==null?"":fm.formatter(data.getXlCostDc().toString()));
						res.setCliamPortfolioOutUsd(data.getClaimPortfolioOutDc()==null?"":fm.formatter(data.getClaimPortfolioOutDc().toString()));
						res.setPremiumReserveReleasedUsd(data.getPremiumReserveRealsedDc()==null?"":fm.formatter(data.getPremiumReserveRealsedDc().toString()));
						res.setNetDueUsd(data.getNetdueDc()==null?"":fm.formatter(data.getNetdueDc().toString()));
						res.setOtherCostUSD(data.getOtherCostDc()==null?"":fm.formatter(data.getOtherCostDc().toString()));
						res.setCedentRef(data.getCedantReference()==null?"":data.getCedantReference().toString());
						res.setRemarks(data.getRemarks()==null?"":data.getRemarks().toString());
						res.setTotalCredit(data.getTotalCrOc()==null?"":fm.formatter(data.getTotalCrOc().toString()));
						res.setTotalCreditDC(data.getTotalCrDc()==null?"":fm.formatter(data.getTotalCrDc().toString()));
						res.setTotalDebit(data.getTotalDrOc()==null?"":fm.formatter(data.getTotalDrOc().toString()));
						res.setTotalDebitDC(data.getTotalDrDc()==null?"":fm.formatter(data.getTotalDrDc().toString()));
						res.setInterest(data.getInterestOc()==null?"":fm.formatter(data.getInterestOc().toString()));
						res.setInterestDC(data.getInterestDc()==null?"":fm.formatter(data.getInterestDc().toString()));
						res.setOsClaimsLossUpdateOC(data.getOsclaimLossupdateOc()==null?"":fm.formatter(data.getOsclaimLossupdateOc().toString()));
						res.setOsClaimsLossUpdateDC(data.getOsclaimLossupdateDc()==null?"":fm.formatter(data.getOsclaimLossupdateDc().toString()));
						res.setOverrider(data.getOverriderAmtOc()==null?"":fm.formatter(data.getOverriderAmtOc().toString()));
						res.setOverriderUSD(data.getOverriderAmtDc()==null?"":fm.formatter(data.getOverriderAmtDc().toString()));
						res.setAmendmentDate(data.getAmendmentDate()==null?"":sdf.format(data.getAmendmentDate()));
		                res.setWithHoldingTaxOC(data.getWithHoldingTaxOc()==null?"":fm.formatter(data.getWithHoldingTaxOc().toString()));
		                res.setWithHoldingTaxDC(data.getWithHoldingTaxDc()==null?"":fm.formatter(data.getWithHoldingTaxDc().toString()));
		               // res.setDueDate(data.getdutempMap.get("DUE_DATE")==null?"":tempMap.get("DUE_DATE").toString());
		                res.setCreditsign(data.getNetdueOc()==null?"":data.getNetdueOc().toString());
		                res.setRiCession(data.getRiCession()==null?"":data.getRiCession().toString());
		                res.setTaxDedectSource(data.getTdsOc()==null?"":fm.formatter(data.getTdsOc().toString()));
						res.setTaxDedectSourceDc(data.getTdsDc()==null?"":fm.formatter(data.getTdsDc().toString()));
						res.setVatPremiumOc(data.getVatPremiumOc()==null?"":fm.formatter(data.getVatPremiumOc().toString()));
						res.setVatPremiumDc(data.getVatPremiumDc()==null?"":fm.formatter(data.getVatPremiumDc().toString()));
						res.setBrokerageVatOc(data.getBrokerageVatOc()==null?"":fm.formatter(data.getBrokerageVatOc().toString()));
						res.setBrokerageVatDc(data.getBrokerageVatDc()==null?"":fm.formatter(data.getBrokerageVatDc().toString()));
						res.setDocumentType(data.getDocumentType()==null?"":data.getDocumentType().toString());
						res.setLossParticipation(data.getLpcOc()==null?"":fm.formatter(data.getLpcOc().toString()));
						res.setLossParticipationDC(data.getLpcDc()==null?"":fm.formatter(data.getLpcDc().toString()));
						res.setSlideScaleCom(data.getScCommOc()==null?"":fm.formatter(data.getScCommOc().toString()));
						res.setSlideScaleComDC(data.getScCommDc()==null?"":fm.formatter(data.getScCommDc().toString()));
					//	res.setSubProfitId(data.gettempMap.get("SUB")==null?"":tempMap.get("SUB").toString());
//						if(!"ALL".equalsIgnoreCase(res.getSubProfitId())){
//						res.setSubProfitId(data.getPremiumSubclass()==null?"":data.getPremiumSubclass().toString());
//						}

						res.setExchRate(dropDownImple.exchRateFormat(data.getExchangeRate()==null?"":fm.formatter(data.getExchangeRate().toString())));
						res.setStatementDate(data.getStatementDate()==null?"":data.getStatementDate().toString());
						// res.setPremiumClass(data.getdeptempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());

						res.setExchRate(dropDownImple.exchRateFormat(data.getExchangeRate()==null?"":dropDownImple.formatter(data.getExchangeRate().toString())));
						res.setStatementDate(data.getStatementDate()==null?"":sdf.format(data.getStatementDate()));
						

//			                res.setPremiumSubClass(tempMap.get("SUB")==null?"":tempMap.get("SUB").toString());
//			                if(!"ALL".equalsIgnoreCase(res.getPremiumSubClass())){
//			                	res.setPremiumSubClass(tempMap.get("PREMIUM_SUBCLASS")==null?"":tempMap.get("PREMIUM_SUBCLASS").toString());
//			                }
			                res.setOsbYN(data.getOsbyn()==null?"":data.getOsbyn().toString());
			                res.setSectionName(data.getSectionName()==null?"":data.getSectionName().toString());
			                res.setAccDate(data.getAccountingPeriodDate()==null?"":sdf.format(data.getAccountingPeriodDate())) ;
				
				 	//premium.select.sumOfPaidPremium
				 	List<RskPremiumDetailsRi> list1 =	pdRIRepo.findByContractNo(new BigDecimal(req.getContractNo()));
				 	int sum =0;
				 	if(list1.size()>0) {
				 		for(RskPremiumDetailsRi data2: list1) {
				 		int	temp = data2.getPremiumQuotashareOc()==null?0:data2.getPremiumQuotashareOc().intValue();
				 		sum = sum+temp;
				 		}
				 		res.setSumOfPaidPremium(String.valueOf(sum));
				 	}				 	
				 	


			   	if(StringUtils.isNotBlank(res.getCurrencyId())){
			   		//premium.select.currency
					CriteriaQuery<String> query1 = cb.createQuery(String.class); 
					Root<CurrencyMaster> cm = query1.from(CurrencyMaster.class);
					query1.select(cm.get("shortName")); 

					// MAXAmend ID
					Subquery<Long> amend = query1.subquery(Long.class); 
					Root<CurrencyMaster> pms = amend.from(CurrencyMaster.class);
					amend.select(cb.max(pms.get("amendId")));
					Predicate p1 = cb.equal( cm.get("branchCode"), pms.get("branchCode"));
					Predicate p2 = cb.equal( cm.get("countryId"), pms.get("countryId"));
					Predicate p3 = cb.equal( cm.get("currencyId"), pms.get("currencyId"));
					Predicate p4 = cb.equal( cm.get("status"), pms.get("status"));
					amend.where(p1,p2,p3,p4);

					Predicate m1 = cb.equal(cm.get("currencyId"), res.getCurrencyId());
					Predicate m2 = cb.equal(cm.get("branchCode"), req.getBranchCode());
					Predicate m3 = cb.equal(cm.get("amendId"), amend);
					Predicate m4 = cb.equal(cm.get("status"), "Y");
					query1.where(m1,m2,m3,m4);
					TypedQuery<String> res1 = em.createQuery(query1);
					List<String> list12 = res1.getResultList();
					if (!CollectionUtils.isEmpty(list12)) {
						res.setCurrency(list12.get(0) == null ? ""
								: list12.get(0).toString());
					}
			   	}
			   //premium.select.currecy.name
			   	TmasBranchMaster bm = bmRepo.findByBranchCode(req.getBranchCode());				
			   	if (bm != null) {
					res.setCurrencyName(bm.getCountryShortName() == null ? ""
							: bm.getCountryShortName().toString());
				}
					
					resList.add(res);
				}
			}		
			response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		    return response;
	}

	@Override
	public CommonResponse updateRIStatus(GetRIPremiumListReq req) {
		CommonResponse response = new CommonResponse();
		try {
			List<RskPremiumDetailsRi> list =	pdRIRepo.findByContractNoAndTransactionNoAndBranchCode
					(new BigDecimal(req.getContractNo()),new BigDecimal(req.getTransactionNo()),req.getBranchCode());
			if(list.size()>0) {
				for(RskPremiumDetailsRi data: list) {
					data.setTransStatus("Approved");
					pdRIRepo.saveAndFlush(data);
				}
			}			
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				log.error(e);
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
	
	public CommonResponse InsertCashLossCredit(InsertPremiumReq req) {
		CommonResponse response = new CommonResponse();
			try {
				/*String query = "";
				if(StringUtils.isNotBlank(req.getClaimPaymentNo())){
				String[] ClaimPayNo=req.getClaimPaymentNo().split(",");
				String[] creditAmountCLC=req.getCreditAmountCLC().split(",");
				String[] creditAmountCLD=req.getCreditAmountCLD().split(",");
				String[] CLCsettlementRate=req.getCLCsettlementRate().split(",");
				String[] cldAmount=req.getCLDAmount().split(",");
				for(int i=0;i<ClaimPayNo.length;i++) {
				if(StringUtils.isNotBlank(ClaimPayNo[i])){
				GetCashLossCreditRes cashLossList=getCassLossCredit(req);
				GetCashLossCreditRes1 form= cashLossList.getCommonResponse().get(0);
				if(ClaimPayNo[i].contains(form.getClaimPaymentNo())) {
				String[] args=new String[17];
				args[0]=req.getBranchCode();
				args[1]=form.getContNo();
				args[2]=form.getClaimNumber();
				args[3]=form.getClaimPaymentNo();
				args[4]=form.getCurrencyId();
				args[5]=creditAmountCLC[i];
				args[6]=form.getCurrencyValue();
				args[7]=CLCsettlementRate[i];
				args[8]=creditAmountCLD[i];
				args[9]=req.getTransactionNo();
				args[10]=req.getTransaction();
				args[11]=req.getBranchCode();
				args[12] = req.getRequestNo();
				if("submit".equalsIgnoreCase(req.getButtonStatus())){
					args[13] = "A";
				}else{
					args[13] = "P";
				}
				args[14] = req.getProposalNo();
				args[15] = cldAmount[i];
				args[16] = req.getCashlossType();
				query = "insert.cash.loss.credit";
				queryImpl.selectList(query, args);
				if("submit".equalsIgnoreCase(req.getButtonStatus())){
					query = "update.claim.payment";
				 	String[] arg=new String[]{form.getContNo(),req.getBranchCode(),req.getRequestNo(),"A",form.getClaimNumber(),form.getClaimPaymentNo(),form.getContNo(),form.getClaimNumber(),form.getClaimPaymentNo()};
				 	queryImpl.selectList(query, arg);
					}
				}
			}
		}
	}
			response.setMessage("Success");
			response.setIsError(false);*/
		    }catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}
	
	public CommonResponse InsertReverseCashLossCredit(InsertReverseCashLossCreditReq req) {
		CommonResponse response = new CommonResponse();
		 try {
			String query="";
			 List<Map<String, Object>>result=new ArrayList<>();
			 String clc="",cld="",paymentNo="",claimNo="";
			if(StringUtils.isNotBlank(req.getCashlosstranId())){
				query="GET_REVERSE_TRANS_LIST";
				String [] obj = new String[2];
				obj[0]= req.getProposalNo();
				obj[1]= req.getCashlosstranId();
				result = queryImpl.selectList(query, obj);
				for(int i=0;i<result.size();i++) {
					Map<String,Object>map=result.get(i);
					clc=map.get("CREDITAMOUNTCLC")==null?"0":map.get("CREDITAMOUNTCLC").toString();
					cld=map.get("CREDITAMOUNTCLD")==null?"0":map.get("CREDITAMOUNTCLD").toString();
					paymentNo=map.get("CLAIMPAYMENT_NO")==null?"0":map.get("CLAIMPAYMENT_NO").toString();
					claimNo=map.get("CLAIM_NO")==null?"0":map.get("CLAIM_NO").toString();
					String[] args=new String[7];
					args[0]=req.getCashlossType();
					args[1]=clc;
					args[2]=cld;
					args[3]="0";
					args[4]="0";
					args[5]=map.get("CREDITTRXNNO")==null?"0.0":map.get("CREDITTRXNNO").toString();
					args[6]=map.get("CLC_NO")==null?"0.0":map.get("CLC_NO").toString();
					query="UPDATE_REVERSE_CASH_LOSS";
					queryImpl.selectList(query, args);
					query = "UPDATE_CASH_LOSS_PAYMENT";
				 	String[] arg=new String[]{clc,req.getContNo(),claimNo,paymentNo};
				 	queryImpl.selectList(query, arg);
				}
			}
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}
	
	public CommonResponse CashLossmailTrigger(CashLossmailTriggerReq req) {
		CommonResponse response = new CommonResponse();
		try {
			String result="";
			String[] args = new String[3];
			args[0] = req.getContNo();
			args[1] = req.getContNo();
			args[2] = req.getDepartmentId();
			String selectQry = "GET_CASH_LOSS_CREADIT_MAIL";
			List<Map<String,Object>> list = queryImpl.selectList(selectQry, args);
			if(list!=null && list.size()>0) {
				String query = "GET_CASSLOSS_ALLOCATE_COUNT";
				int count = queryImpl.updateQuery(query, new String[] {req.getProposalNo()});
				if(count==0) {
					result = sendCashLossMail(list,req);
				}else {
					String query1="GET_CASSLOSS_ALLOCATE_AMOUNT_COUNT";
					int count1 = queryImpl.updateQuery(query1,new String[] {req.getProposalNo(),req.getTransactionNo()});
					if(count1>0) {
						selectQry = "GET_REVERSE_TRANS_LIST";
						list = queryImpl.selectList(selectQry,new String[] {req.getProposalNo(),req.getTransactionNo()});
						result = sendCashLossMail(list,req);
					}
				} 
			}
			response.setMessage("Success");
			response.setIsError(false);
		    }catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}
	
	public String sendCashLossMail(List<Map<String, Object>> list, CashLossmailTriggerReq req) {
		try {
			List<Map<String, Object>>listinfo=null;
			Map<String, String> mapt=getMailDetails("51");
			listinfo=getMailCCInfo("CASH_LOSS",req.getBranchCode());
			Map<String, String> details=new HashMap<String, String>();
			if(mapt != null && listinfo!=null && listinfo.size()>0){
				Map<String,Object>map=listinfo.get(0);
				String hostName=(String)mapt.get("SMTP_HOST");
				String user = mapt.get("SMTP_USER");
				String pwd = mapt.get("SMTP_PWD");
				String port=mapt.get("SMTP_PORT");
				String mailform = mapt.get("SMTP_ADDRESS");
				String shortAddress = mapt.get("SMTP_SHORT_ADDRESS");
				String subject = "Cash Loss Credit Mail";
				String toAddress = (String) map.get("EMAIL_TO");
				String ccAddress = (String) map.get("EMAIL_CC");
				
				details.put("TRANSACTION_NO", req.getTransactionNo());
				details.put("LOGIN_ID", req.getLoginId());
				details.put("CONTRACT_NO", req.getContNo());
				details.put("TRANSACTION_DATE", req.getTransaction());
				details.put("BRANCH_CODE", req.getBranchCode());
				details.put("CLC_CURRENCY", req.getCurrencyId());
				if(toAddress!=null && !"".equals(toAddress)){
					String[] toAddresses = (toAddress.indexOf(",")!=-1)?toAddress.split(","):new String[]{toAddress};
					String[] ccAddresses = new String[0];
					if(ccAddress!=null && !"".equals(ccAddress)){
						ccAddresses = (ccAddress.indexOf(",")!=-1)?ccAddress.split(","):new String[]{ccAddress};
					}
				
				String msg=frameTemplate(details,list);
				sendResponseMail(hostName, user, pwd, mailform, subject, msg, toAddresses, ccAddresses, shortAddress,port);
				}
}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, String> getMailDetails(String appId){
    	Map<String, String> details = new HashMap<String, String>();
    	try {
    		String  query= "GET_MAIL_DETAILS";
        	List<Map<String, Object>> list = queryImpl.selectList(query, new String[]{appId});
        	if(list != null && list.size()>0){
	        	details = (Map)list.get(0);
        	}
        }catch(Exception e){
        }
    	return details;
  }
	
	private String frameTemplate(Map<String, String> details, List<Map<String, Object>> list) {
		DecimalFormat d = new DecimalFormat("#,###");
		String messageContent="";
		try {
			String query = "GET_CURRENCY_NAME";
			String branchcode=details.get("BRANCH_CODE")==null?"":details.get("BRANCH_CODE").toString();
			messageContent="Dear Management,<br/><br/><br/>\r\n" + 
			"\r\n" + 
			"A transaction "+details.get("TRANSACTION_NO")+" has been processed by "+details.get("LOGIN_ID")+" on "+details.get("TRANSACTION_DATE")+" under contract number "+details.get("CONTRACT_NO")+".<br/>\r\n" + 
			"Below were the list of pending allocations in Cash Loss Credit module on the transaction date for contract "+details.get("CONTRACT_NO")+" (including previous risk's pending credits).<br/>\r\n" + 
			"<!DOCTYPE html>\r\n" + 
					"<html lang=\"en\">\r\n" + 
					"<body>\r\n" + 
					"    <div style=\"width: 80%;\">\r\n" + 
					"        <p style=\"color: #003286;font: small/ 1.5 Arial,Helvetica,sans-serif;font-size: 25px;font-weight: bold;text-decoration: underline;\">Below were the list of pending allocations in Cash Loss Credit module on the transaction date for contract "+details.get("CONTRACT_NO")+" (including previous risk's pending credits).</p>\r\n" + 
					"        <table style=\"width:100%;border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">\r\n" + 
					"            <tr>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">S No</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Contract No</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Claim No</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Claim Payment No</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Paid Date</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">CLD Currency</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">CLD Amount</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">CLC Currency</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Credit Amount in CLC Currency</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Credit Amount in CLD Currency</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">CLC Settlement rate</th>\r\n" + 
					"                <th style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;background-color: #003286;color: white;font-weight: bold;\">Status</th>\r\n" + 
					"            </tr>\r\n" ;
			
			
			for(int i=0;i<list.size();i++) {
				Map<String,Object>map=list.get(i);
				String cldcurrencyName = queryImpl.selectList(query, new String[]{branchcode,map.get("CLDCURRENCY_ID")==null?"":map.get("CLDCURRENCY_ID").toString()}).toString();
				String clccurrencyName = queryImpl.selectList(query, new String[]{branchcode,details.get("CLC_CURRENCY")==null?"":details.get("CLC_CURRENCY").toString()}).toString();
				String status="";
				Double cldamount=map.get("CLD_AMOUNT")==null?0d:Double.parseDouble(map.get("CLD_AMOUNT").toString());
				Double creditcldamount=map.get("CREDITAMOUNTCLD")==null?0d:Double.parseDouble(map.get("CREDITAMOUNTCLD").toString());
				if(cldamount==creditcldamount) {
					status="Fully Credited";
				}else if(cldamount<creditcldamount) {
					status="Partially Credit";
				}else if(creditcldamount<=0) {
					status="Unallocated";
				}
					messageContent+="<tr>\r\n"+
				"				 <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(i+1)+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString())+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CLAIM_NO")==null?"":map.get("CLAIM_NO").toString())+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CLAIMPAYMENT_NO")==null?"":map.get("CLAIMPAYMENT_NO").toString())+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CREDITDATE")==null?"":map.get("CREDITDATE").toString())+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(cldcurrencyName)+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CLD_AMOUNT")==null?"":d.format(Double.parseDouble(map.get("CLD_AMOUNT").toString())))+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(clccurrencyName)+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CREDITAMOUNTCLC")==null?"":d.format(Double.parseDouble(map.get("CREDITAMOUNTCLC").toString())))+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("CREDITAMOUNTCLD")==null?"":d.format(Double.parseDouble(map.get("CREDITAMOUNTCLD").toString())))+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(map.get("EXCHANGE_RATE")==null?"":map.get("EXCHANGE_RATE").toString())+"</td>\r\n" + 
				"                <td style=\"border: 1px solid #003286; font: small/ 1.5 Arial,Helvetica,sans-serif;\">"+(status)+"</td>\r\n" + 
				"</tr>\r\n";
			}
			messageContent+="        </table>\r\n" + 
				"    </div>\r\n" + 
				"</body>\r\n" + 
				"\r\n" + 
				"</html>";
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	return messageContent.toString();
	}
	
	private List<Map<String, Object>> getMailCCInfo(String type, String branchCode) {
    	List<Map<String, Object>>list=null;
    	try {
    		String  query= "GET_MAILCC_INFO";
        	 list = queryImpl.selectList(query,new String[]{type,branchCode});
        	
        }catch(Exception e){
        }
    	return list;
	}
	
	 public void sendResponseMail(final String SMTP_HOST_NAME, final String user,  final String pwd, final String SMTP_MAIL_FROM, final String subject,
	    		final String message, final String[] toAddress, final String[] ccAddress, final String SMTP_SHORT_ADDRESS,final String SMTP_PORT){
		  	String SMTP_AUTH_USER; 
			String SMTP_AUTH_PWD;
	    	SMTP_AUTH_USER = user;
	    	SMTP_AUTH_PWD = pwd;
	    	try{
		    	Properties props = new Properties();
		    	props.setProperty("mail.transport.protocol", "smtp");
				props.put("mail.smtp.host", SMTP_HOST_NAME);
				props.put("mail.smtp.port", SMTP_PORT);
				props.put("mail.smtp.starttls.enable", "true");
				 props.put("mail.debug", "true");
				Session session = null; 
				if(SMTP_AUTH_PWD != null && !"".equals(SMTP_AUTH_PWD.trim())){
					props.put("mail.smtp.auth", "true");
					Authenticator auth = new SMTPAuthenticator();
					session = Session.getInstance(props, auth);
				}else{
					props.put("mail.smtp.auth", "false");
					session = Session.getInstance(props);
				}
				session.setDebug(false);
				Message msg1 = new MimeMessage(session);
				InternetAddress addressFrom = new InternetAddress(SMTP_MAIL_FROM, SMTP_SHORT_ADDRESS);
				msg1.setFrom(addressFrom);
				if(toAddress != null && toAddress.length>0){
					InternetAddress[] addressTo = new InternetAddress[toAddress.length];			
					for (int i = 0; i < toAddress.length; i++){
						addressTo[i] = new InternetAddress(toAddress[i]);
						msg1.addRecipient(Message.RecipientType.TO, addressTo[i]);
					}
				}
				if(ccAddress != null && ccAddress.length>0){
					InternetAddress[] addressToCC = new InternetAddress[ccAddress.length];			
					for(int i=0;i<ccAddress.length;i++){
						addressToCC[i] = new InternetAddress(ccAddress[i]);
						msg1.addRecipient(Message.RecipientType.CC, addressToCC[i]);
					}
				}
				msg1.setSubject(subject);
				msg1.setContent(message, "text/html");
				System.out.println(msg1);
				Transport.send(msg1);
			}catch(Exception e){
				System.out.println(e);
			}
	    }
	  
	  private class SMTPAuthenticator extends javax.mail.Authenticator{
		  	String SMTP_AUTH_USER = null; 
			String SMTP_AUTH_PWD = null;
			public PasswordAuthentication getPasswordAuthentication(){
				String username = SMTP_AUTH_USER;
				String password = SMTP_AUTH_PWD;
				return new PasswordAuthentication(username, password);
			}
		}
	
	  public getReverseCassLossCreditRes getReverseCassLossCredit(String proposalNo, String cashlosstranId) {
			getReverseCassLossCreditRes res = new getReverseCassLossCreditRes();
		     double value1=0.0;
		     try {
				List<Map<String, Object>>result=new ArrayList<>();
					String query = "GET_REVERSE_TRANS_LIST";
					result = queryImpl.selectList(query,new String[] {proposalNo,cashlosstranId});
					for(int i=0;i<result.size();i++) {
						Map<String,Object>map=result.get(i);
						value1=value1+(map.get("CREDITAMOUNTCLC")==null?0.0:Double.parseDouble(map.get("CREDITAMOUNTCLC").toString()));
								
						}
					res.setResponse(String.valueOf(value1));
					res.setMessage("Success");
					res.setIsError(false);
				    }catch (NumberFormatException e) {
						log.error(e);
						e.printStackTrace();
						res.setMessage("Failed");
						res.setIsError(true);
					}
					return res;
			}

		public getCurrencyShortNameRes getCurrencyShortName(String currencyId, String branchCode) {
			getCurrencyShortNameRes response = new getCurrencyShortNameRes();
			String res = "";
			try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					String query = "GET_SHRT_NAME";
					String args[] = new String[2];
					args[0] = currencyId;
					args[1] = branchCode;
					
					list = queryImpl.selectList(query, args);
					if(!CollectionUtils.isEmpty(list)){
						res = list.get(0).get("SHORT_NAME")==null?"":list.get(0).get("SHORT_NAME").toString();
						}
					
					response.setResponse(String.valueOf(res));
					response.setMessage("Success");
					response.setIsError(false);
				    }catch (Exception e) {
						log.error(e);
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
					return response;
			}
		@Override
		public ViewPremiumDetailsRIRes viewPremiumDetailsRI(ViewPremiumDetailsRIReq req) {
			ViewPremiumDetailsRIRes response = new ViewPremiumDetailsRIRes();
			ViewPremiumDetailsRIRes1 res = new ViewPremiumDetailsRIRes1();
			try {
				RskPremiumDetailsRi data =	pdRIRepo.findByTransactionNoAndContractNoAndReinsurerIdAndBranchCode
						(new BigDecimal(req.getTransactionNo()),new BigDecimal(req.getContractNo()),req.getReinsurerId(),req.getBranchCode()); 
						if(data!=null) { //CON,BRA,REINID
						res.setAccountPeriod(data.getAccountPeriodQtr()==null?"":data.getAccountPeriodQtr().toString());	
						res.setAccountPeriodDate(data.getAccountingPeriodDate()==null?"":data.getAccountingPeriodDate().toString());	
						res.setAccountPeriodyear(data.getAccountPeriodYear()==null?"":data.getAccountPeriodYear().toString());	
						res.setAdjustmentpremium(data.getAdjustmentPremiumOc()==null?"":data.getAdjustmentPremiumOc().toString());
						res.setAmendmentDate(data.getAmendmentDate()==null?"":data.getAmendmentDate().toString());
						res.setBrokerage(data.getBrokerage()==null?"":data.getBrokerage().toString());
						res.setCashLossCredit(data.getCashLossCreditOc()==null?"":data.getCashLossCreditOc().toString());
						res.setCashLossPaid(data.getCashLosspaidOc()==null?"":data.getCashLosspaidOc().toString());;
						res.setCedentRef(data.getCedantReference()==null?"":data.getCedantReference ().toString());
						res.setClaimspaid(data.getClaimsPaidOc()==null?"":data.getClaimsPaidOc().toString());
						res.setCliamPortfolioin(data.getClaimPortfolioinOc()==null?"":data.getClaimPortfolioinOc().toString());
						res.setCliamportfolioout(data.getClaimPortfolioOutOc()==null?"":data.getClaimPortfolioOutOc().toString());					
						res.setCommission(data.getCommission()==null?"":data.getCommission().toString());	
						res.setCommissionQuotaShare(data.getCommissionQuotashareOc()==null?"":data.getCommissionQuotashareOc().toString());	
						res.setCommissionSurplus(data.getCommissionSurplusOc()==null?"":data.getCommissionSurplusOc().toString());	
						res.setCurrency(data.getCurrencyId()==null?"":data.getCurrencyId().toString());	
						res.setCurrencyId(data.getCurrencyId()==null?"":data.getCurrencyId().toString());	
						res.setEnteringMode(data.getEnteringMode()==null?"":data.getEnteringMode().toString());
						res.setExchRate(data.getExchangeRate()==null?"":data.getExchangeRate().toString());
						res.setInceptionDate(data.getEntryDate()==null?"":data.getEntryDate().toString());
						res.setInstlmentNo(data.getInstalmentNumber()==null?"":data.getInstalmentNumber().toString());
						res.setInterest(data.getInterestOc()==null?"":data.getInterestOc().toString());
						res.setLossParticipation(data.getLpcOc()==null?"":data.getLpcOc().toString());
						res.setLossParticipationDC(data.getLpcDc()==null?"":data.getLpcDc().toString());
						res.setLossReserveReleased(data.getLossReserveReleasedOc()==null?"":data.getLossReserveReleasedOc().toString());
						res.setLossReserveRetained(data.getLossReserveretainedOc()==null?"":data.getLossReserveretainedOc().toString());
						res.setLrAllocatedAmount(data.getLrdAllocatedTillDate()==null?"":data.getLrdAllocatedTillDate().toString());
						res.setMdpremium(data.getMDpremiumOc()==null?"":data.getMDpremiumOc().toString());
						res.setNetDue(data.getNetdueOc()==null?"":data.getNetdueOc().toString());	
						res.setOsbYN(data.getOsbyn()==null?"":data.getOsbyn().toString());
						res.setOsClaimsLossUpdateOC(data.getOsclaimLossupdateOc()==null?"":data.getOsclaimLossupdateOc().toString());
						res.setOtherCost(data.getOtherCostOc()==null?"":data.getOtherCostOc().toString());
						res.setOverriderUSD(data.getOverriderAmtDc()==null?"":data.getOverriderAmtDc().toString());
						res.setOverrider(data.getOverrider()==null?"":data.getOverrider().toString());
						res.setPrAllocatedAmount(data.getPrdAllocatedTillDate()==null?"":data.getPrdAllocatedTillDate().toString());
						res.setPredepartment(data.getPremiumClass()==null?"":data.getPremiumClass().toString());
						res.setPremiumportifolioIn(data.getPremiumPortfolioinOc()==null?"":data.getPremiumPortfolioinOc().toString());
						res.setPremiumportifolioout(data.getPremiumPortfoliooutOc()==null?"":data.getPremiumPortfoliooutOc().toString());
						res.setPremiumQuotaShare(data.getPremiumQuotashareOc()==null?"":data.getPremiumQuotashareOc().toString());
						res.setPremiumReserveQuotaShare(data.getPremiumreserveQuotashareOc()==null?"":data.getPremiumreserveQuotashareOc().toString());
						res.setPremiumReserveReleased(data.getPremiumReserveRealsedOc()==null?"":data.getPremiumReserveRealsedOc().toString());
						res.setPremiumSurplus(data.getPremiumSubclass()==null?"":data.getPremiumSubclass().toString());
						res.setProfitCommission(data.getProfitCommissionOc()==null?"":data.getProfitCommissionOc().toString());
						res.setReceiptno(data.getReceiptNo()==null?"":data.getReceiptNo().toString());
						res.setRecuirementpremium(data.getRecPremiumOc()==null?"":data.getRecPremiumOc().toString());
						res.setRemarks(data.getRemarks()==null?"":data.getRemarks().toString());
						res.setRicession(data.getRiCession()==null?"":data.getRiCession().toString());
						res.setSectionName(data.getSectionName()==null?"":data.getSectionName().toString());
		//				res.setSectionType(data.gets);
						res.setVatPremium(data.getVatPremiumOc()==null?"":data.getVatPremiumOc().toString());
						res.setVatPremiumDc(data.getVatPremiumDc()==null?"":data.getVatPremiumDc().toString());
						res.setSlideScaleCom(data.getScCommOc()==null?"":data.getScCommOc().toString());
						res.setSlideScaleComDC(data.getScCommDc()==null?"":data.getScCommDc().toString());
						res.setStatementDate(data.getStatementDate()==null?"":data.getStatementDate().toString());
						res.setStatus(data.getStatus()==null?"":data.getStatus().toString());
						res.setSubProfitId(data.getPremiumSubclass()==null?"":data.getPremiumSubclass().toString());
						res.setTax(data.getTax()==null?"":data.getTax().toString());
						res.setTaxDedectSource(data.getTdsOc()==null?"":data.getTdsOc().toString());
						res.setTaxDedectSourceDc(data.getTdsDc()==null?"":data.getTdsDc().toString());
						res.setTransaction(data.getTransactionNo()==null?"":data.getTransactionNo().toString());
						res.setWithHoldingTaxDC(data.getWithHoldingTaxDc()==null?"":data.getWithHoldingTaxDc().toString());
						res.setWithHoldingTaxOC(data.getWithHoldingTaxOc()==null?"":data.getWithHoldingTaxOc().toString());
						res.setXlCost(data.getXlCostOc()==null?"":data.getXlCostOc().toString());
						response.setCommonResponse(res);		
						}
				response.setMessage("Success");
				response.setIsError(false);
			    }catch (Exception e) {
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			    }
			return response;
		}
		public int getCountAccountPeriod(InsertPremiumReq req) {
			List<Map<String,Object>> list = null;
			int count=0;
			int count1=1;
			try {
				String query="GET_ACCOUNT_PERIOD_COUNT";
				list = queryImpl.selectList(query,new String[]{req.getBranchCode(),req.getAccountPeriodDate(),req.getContNo(),req.getDepartmentId(),req.getContNo(),req.getDepartmentId()});
				if (!CollectionUtils.isEmpty(list)) {
					count= Integer.valueOf(list.get(0).get("COUNT") == null ? ""
							: list.get(0).get("COUNT").toString());
				}
				
				
				if(!(count>=1)){
				query="GET_ACCOUNT_PERIOD_COUNT1";
				list = queryImpl.selectList(query,new String[]{req.getAccountPeriodDate(),req.getContNo(),req.getDepartmentId()});
				if (!CollectionUtils.isEmpty(list)) {
					count1= Integer.valueOf(list.get(0).get("ACCOUNT_PERIOD_COUNT1") == null ? ""
							: list.get(0).get("ACCOUNT_PERIOD_COUNT1").toString());
				}
				
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return count1;
		}
		
	
}
