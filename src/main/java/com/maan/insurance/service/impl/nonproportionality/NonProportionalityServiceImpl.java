package com.maan.insurance.service.impl.nonproportionality;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnRetroCessionary;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnIeModule;
import com.maan.insurance.model.entity.TtrnRip;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.entity.TtrnRskClassLimits;
import com.maan.insurance.model.repository.ConstantDetailRepository;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.TtrnBonusRepository;
import com.maan.insurance.model.repository.TtrnCrestazoneDetailsRepository;
import com.maan.insurance.model.repository.TtrnIeModuleRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnMndInstallmentsRepository;
import com.maan.insurance.model.repository.TtrnRetroCessionaryRepository;
import com.maan.insurance.model.repository.TtrnRipRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskProposalRepository;
import com.maan.insurance.model.repository.TtrnRiskRemarksRepository;
import com.maan.insurance.model.repository.TtrnRskClassLimitsRepository;
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.CoverLimitAmount;
import com.maan.insurance.model.req.nonproportionality.CoverList;
import com.maan.insurance.model.req.nonproportionality.CoverdepartIdList;
import com.maan.insurance.model.req.nonproportionality.CrestaSaveReq;
import com.maan.insurance.model.req.nonproportionality.GetLayerInfoReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertBonusDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertIEModuleReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq1;
import com.maan.insurance.model.req.nonproportionality.InsertRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;
import com.maan.insurance.model.req.nonproportionality.MoveReinstatementMainReq;
import com.maan.insurance.model.req.nonproportionality.ReInstatementMainInsertReq;
import com.maan.insurance.model.req.nonproportionality.ReinstatementNoList;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.req.nonproportionality.RetroCessListReq;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.SaveSecondPageReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.nonproportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageData1Req;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.nonproportionality.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.nonproportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.nonproportionality.getReInstatementDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.insertClassLimitReq;
import com.maan.insurance.model.req.nonproportionality.insertProportionalTreatyReq;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;
import com.maan.insurance.model.req.proportionality.GetTreatyNameDuplicationCheckReq;
import com.maan.insurance.model.req.proportionality.RetroDetailReq;
import com.maan.insurance.model.res.nonproportionality.BonusRes;
import com.maan.insurance.model.res.nonproportionality.CheckAvialabilityRes;
import com.maan.insurance.model.res.nonproportionality.CommonResponse;
import com.maan.insurance.model.res.nonproportionality.CommonSaveRes;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes1;
import com.maan.insurance.model.res.nonproportionality.GetCommonValueRes;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes1;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes1;
import com.maan.insurance.model.res.nonproportionality.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.nonproportionality.GetLowClaimBonusListRes1;
import com.maan.insurance.model.res.nonproportionality.GetReInstatementDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes1;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;
import com.maan.insurance.model.res.nonproportionality.InstalmentListRes;
import com.maan.insurance.model.res.nonproportionality.InstalmentListRes1;
import com.maan.insurance.model.res.nonproportionality.MappingProposalRes;
import com.maan.insurance.model.res.nonproportionality.ReInStatementRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;
import com.maan.insurance.model.res.nonproportionality.RetroCessListRes;
import com.maan.insurance.model.res.nonproportionality.RetroListRes;
import com.maan.insurance.model.res.nonproportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.nonproportionality.RiskDetailsEditModeRes1;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes1;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes1;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes1;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes1;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res1;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes1;
import com.maan.insurance.model.res.nonproportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondpageEditItemsRes1;
import com.maan.insurance.model.res.nonproportionality.UpdateProportionalTreatyRes;
import com.maan.insurance.model.res.nonproportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.nonproportionality.ViewRiskDetailsRes1;
import com.maan.insurance.model.res.nonproportionality.insertProportionalTreatyRes;
import com.maan.insurance.model.res.xolPremium.GetClassLimitDetailsResponse;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.proportionality.ProportionalityCustomRepository;
import com.maan.insurance.service.nonproportionality.NonProportionalityService;
import com.maan.insurance.validation.Formatters;


@Service
public class NonProportionalityServiceImpl implements NonProportionalityService{
	private Logger logger = LogManager.getLogger(NonProportionalityServiceImpl.class);
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	@Autowired
	private ProportionalityCustomRepository proportionalityCustomRepository;
	@Autowired
	private NonProportionalityCustomRepository nonProportCustomRepository;
	@Autowired
	private TtrnRiskDetailsRepository ttrnRiskDetailsRepository;
	@Autowired
	private PositionMasterRepository positionMasterRepository;
	@Autowired
	private TtrnRskClassLimitsRepository ttrnRskClassLimitsRepository;
	@Autowired
	private TtrnRiskProposalRepository ttrnRiskProposalRepository;
	@Autowired
	private ConstantDetailRepository constantDetailRepository;
	@Autowired
	private  TtrnMndInstallmentsRepository ttrnMndInstallmentsRepository;
	@Autowired
	private  TtrnRiskCommissionRepository ttrnRiskCommissionRepository;
	@Autowired
	private  TtrnRetroCessionaryRepository ttrnRetroCessionaryRepository;
	@Autowired
	private TtrnBonusRepository ttrnBonusRepository;
	@Autowired
	private TtrnCrestazoneDetailsRepository ttrnCrestazoneDetailsRepository;
	@Autowired
	private TtrnRipRepository ttrnRipRepository;
	@Autowired
	private TtrnIeModuleRepository ttrnIeModuleRepository;
	@Autowired
	private TtrnRiskRemarksRepository ttrnRiskRemarksRepository;
	@Autowired
	private TtrnInsurerDetailsRepository ttrnInsurerDetailsRepository;
	
	private String DateFormat(Object input) {
		return new SimpleDateFormat("dd/MM/yyyy").format(input).toString();
		}

	@Override
	public GetCommonValueRes getShortname(String branchCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		String Short="";
		try {
			//GET_SHORT_NAME
			Short = nonProportCustomRepository.getShortName(branchCode);
			response.setCommonResponse(Short);
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
	public ShowSecondPageData1Res showSecondPageData1(ShowSecondPageData1Req req) {
		ShowSecondPageData1Res response = new ShowSecondPageData1Res();
		ShowSecondPageData1Res1 res = new ShowSecondPageData1Res1();
		try{
			//risk.select.getSecPageData 
			List<Tuple> list =	nonProportCustomRepository.riskSelectGetSecPageData(req.getProposalNo(),req.getBranchCode(),req.getProductId());
			Tuple resMap = null;
			if(list!=null && list.size()>0) {
				resMap = list.get(0);
				if(resMap!=null) {
					res.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
					String id = resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString();
					String branch = resMap.get("BRANCH_CODE")==null?"":resMap.get("BRANCH_CODE").toString();
					String proId = resMap.get("TMAS_PRODUCT_ID")==null?"":resMap.get("TMAS_PRODUCT_ID").toString();
					//select RTRIM(XMLAGG(XMLELEMEN 
					res.setSubProfitCenter(dropDowmImpl.getSubClass(id, branch, proId));
					
					res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
					res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
					res.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
					res.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
					res.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
					res.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
				}
			}
			if(StringUtils.isNotBlank(req.getMdInstalmentNumber()) && Integer.parseInt(req.getMdInstalmentNumber())>0){
			List<InstalListRes> instalList = new ArrayList<InstalListRes>();
				for(int i=0;i<Integer.parseInt(req.getMdInstalmentNumber());i++){
					InstalListRes res1 = new InstalListRes();
				//	InstalListRes installRes = res.getInstalList().get(i);
					res1.setInstall(String.valueOf(i));
					instalList.add(res1);
				}
				res.setInstalList(instalList);
			}
			//GetRemarksDetails(req);
			response.setCommonResponse(res);
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
	public GetRemarksDetailsRes GetRemarksDetails(String proposalNo, String layerNo) {
		GetRemarksDetailsRes resp=new GetRemarksDetailsRes();
		List<RemarksRes> remarksres=new ArrayList<RemarksRes>();
		try {
			//GET_REMARKS_DETAILS
			List<Tuple> list =	nonProportCustomRepository.getRemarksDetails(proposalNo,layerNo);
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					Tuple insMap = list.get(i);
					RemarksRes res=new RemarksRes();
					res.setDescription(insMap.get("RSK_DESCRIPTION")==null?"Remarks":insMap.get("RSK_DESCRIPTION").toString());
					res.setRemark1(insMap.get("RSK_REMARK1")==null?" ":insMap.get("RSK_REMARK1").toString());
					res.setRemark2(insMap.get("RSK_REMARK2")==null?"":insMap.get("RSK_REMARK2").toString());
					res.setRemarkSNo(insMap.get("RSK_SNO")==null?"":insMap.get("RSK_SNO").toString());
					remarksres.add(res);
					
				}
			}else{
				RemarksRes res=new RemarksRes();
				remarksres.add(res);
			}
			resp.setCommonResponse(remarksres);
			resp.setIsError(false);
			resp.setMessage("Success");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setIsError(true);
			resp.setMessage("Failed");
		}
		return resp;
	}

	@Override
	public GetCommonValueRes getSumOfCover(String proposalNo, String branchCode, String productId,String layerNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result="";
		try{
			String query="SUM_COVER_LIMT"; //WITH T1  AS
			String args[]=new String[7];
			args[0] = proposalNo;
			args[1] = branchCode;
			args[2] = StringUtils.isEmpty(layerNo)?"0":layerNo;
			args[3] = productId;
			args[4] = branchCode;
			args[5] = proposalNo;
			args[6] = productId;
			List<Map<String, Object>> list  = queryImpl.selectList(query,args);
			if(!CollectionUtils.isEmpty(list)) {
				result=list.get(0).get("SUM_VAL")==null?"":list.get(0).get("SUM_VAL").toString();
			}
			response.setCommonResponse(result);
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
	public ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo) {
		ShowLayerBrokerageRes response = new ShowLayerBrokerageRes();
		ShowLayerBrokerageRes1 res = new ShowLayerBrokerageRes1();
		try {
			if(StringUtils.isNotBlank(layerProposalNo)){
				//RISK_SELECT_GETBROKERAGE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				Root<TtrnRiskCommission> pm = query.from(TtrnRiskCommission.class);

				query.multiselect(pm.get("rskBrokerage").alias("RSK_BROKERAGE"), pm.get("rskTax").alias("RSK_TAX")); 

				Subquery<Long> maxEnd = query.subquery(Long.class); 
				Root<TtrnRiskCommission> pms = maxEnd.from(TtrnRiskCommission.class);
				maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( pms.get("rskProposalNumber"), layerProposalNo);
				maxEnd.where(a1);

				Predicate n1 = cb.equal(pm.get("rskProposalNumber"), layerProposalNo);
				Predicate n2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
				query.where(n1,n2);

				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				
				if(list!=null && list.size()>0) {
					Tuple	resMap = list.get(0);
				if(resMap!=null){
					res.setBrokerage(resMap.get("RSK_BROKERAGE")==null?"":fm.formattereight(resMap.get("RSK_BROKERAGE").toString()));
					res.setTax(resMap.get("RSK_TAX")==null?"":fm.formattereight(resMap.get("RSK_TAX").toString()));
				}
			} }
			response.setCommonResponse(res);
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
	public ShowRetroContractsRes showRetroContracts(ShowRetroContractsReq req) {
		ShowRetroContractsRes response = new ShowRetroContractsRes();
		ShowRetroContractsRes1 res = new ShowRetroContractsRes1();
		GetRetroContractDetailsListReq req1 = new GetRetroContractDetailsListReq();
		req1.setBranchCode(req.getBranchCode());
		req1.setIncepDate(req.getIncepDate());
		req1.setProductid(req.getProductId());
		List<Tuple> insDetailsList = new ArrayList<>();
		try{
			String[] args=null;
			int noofInsurar=0;
			if(StringUtils.isNotBlank(req.getNoInsurer())){
				 noofInsurar = Integer.parseInt(req.getNoInsurer());
				 noofInsurar = noofInsurar+1;
			}
			if(req.getView()){
				args=new String[3];
				args[0]=req.getAmendId();
				args[1]=req.getProposalNo();
				args[2]=Integer.toString(noofInsurar);
				//fac.select.viewInsDetails
				insDetailsList = nonProportCustomRepository.facSelectViewInsDetails(args);
			}else{
				args=new String[2];
				args[0]=req.getProposalNo();
				args[1]=Integer.toString(noofInsurar);
				//fac.select.insDetails
				insDetailsList = nonProportCustomRepository.facSelectInsDetails(args);
			}
			
			List<RetroListRes> retroListRes = new ArrayList<RetroListRes>();
		//	List<List<Map<String,Object>>> retroFinalList=new ArrayList<List<Map<String,Object>>>();
			if(insDetailsList!=null&&insDetailsList.size()>0){
				
				for(int j=0;j<insDetailsList.size();j++){
					RetroListRes retroRes = new RetroListRes();
					Tuple insDetailsMap=insDetailsList.get(j);
					if("R".equalsIgnoreCase((String)insDetailsMap.get("TYPE"))){
						res.setRetentionPercentage(insDetailsMap.get("RETRO_PER")==null?"":insDetailsMap.get("RETRO_PER").toString());
					}else{
						
						res.setBranchCode(req.getBranchCode());
						res.setIncepDate(req.getIncepDate());
						if("2".equals(req.getProductId())){
							res.setProductId("4");	
							res.setRetroType("TR");
						}else if("3".equals(req.getProductId())){
							res.setProductId("4");	
							res.setRetroType("TR");	
						}
						if(j==1){
							if(req.getUwYear().equalsIgnoreCase(insDetailsMap.get("UW_YEAR").toString())){
								res.setRetroDupYerar(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
								}else{
									res.setRetroDupYerar(req.getUwYear());
								}
							res.setRetroDupContract(insDetailsMap.get("CONTRACTNO")==null?"":insDetailsMap.get("CONTRACTNO").toString());
						}
						else if(j>1){
							retroRes.setUWYear(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
							retroRes.setCedingCompany(insDetailsMap.get("CONTRACTNO")==null?"":insDetailsMap.get("CONTRACTNO").toString());
							retroRes.setRetroPercentage(insDetailsMap.get("RETRO_PER")==null?"":insDetailsMap.get("RETRO_PER").toString());
							GetRetroContractDetailsListRes commonRes=getRetroContractDetailsList(req1,2,insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
							//retroFinalList.add(getRetroContractDetailsList(bean,2,insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString()));
							
							retroRes.setRetroFinalList(commonRes.getCommonResponse());
							retroRes.setRetroList(String.valueOf(j));
							
						}
				//		res.setRetroUwyear(getRetroContractDetailsList(bean,1,""));
						res.setUwYear(insDetailsMap.get("UW_YEAR")==null?"":insDetailsMap.get("UW_YEAR").toString());
					}
					retroListRes.add(retroRes);
				} //--------------------------------------doubt below code need to update
//				res.setPercentRetro(retroPercentage);
//				res.setRetroCeding(cedingCompany);
//				res.setRetroYear(UWYear);
//				if(req.getUwYear().size()==0){
//					res.setRetroUwyear(getRetroContractDetailsList(req,1,""));
//				}
//				if(retroFinalList.size()!=Integer.parseInt(req.getNoInsurer())){
//					for(int i=0;i<Integer.parseInt(req.getNoInsurer())-retroList.size();i++){
//						retroFinalList.add(new ArrayList<Map<String,Object>>());
//					}
//				}
//				res.setRetroFinalList(retroFinalList);
//				
//			}else{
//				for(int i=0;i<Integer.parseInt(StringUtils.isBlank(req.getNoInsurer())?"0":req.getNoInsurer());i++){
//					RiskDetailsBean bean=new RiskDetailsBean();
//					if("2".equals(req.getProductId())){
//						res.setProductId("4");	
//						res.setRetroType("TR");
//						res.setUwYear(req.getUwYear());
//						res.setIncepDate(req.getIncepDate());
//						res.setBranchCode(req.getBranchCode());
//					}else if("3".equals(req.getProductId())){
//						res.setProductId("4");	
//						res.setRetroType("TR");
//						res.setUwYear(req.getUwYear());
//						res.setIncepDate(req.getIncepDate());
//						res.setBranchCode(req.getBranchCode());
//					}
//					if(StringUtils.isNotBlank(req.getNoInsurer())){
//						for (int z=0;z<Integer.parseInt(req.getNoInsurer());z++){
//							res.setRetroDupYerar(req.getUwYear());
//							List<Map<String,Object>> list = getRetroContractDetailsList(req,3,req.getUwYear());
//							for (int j=0;j<list.size();j++){
//								Map<String,Object> map = list.get(j);
//								res.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
//							}
//						}
//					}
//					res.setPercentRetro(retroPercentage);
//					res.setRetroYear(UWYear);
//					res.setBranchCode(req.getBranchCode());
//					res.setIncepDate(req.getIncepDate());
//					retroFinalList.add(new ArrayList<Map<String,Object>>());
//					req.setRetroUwyear(getRetroContractDetailsList(bean,1,""));
//				}
//				res.setRetroFinalList(retroFinalList);
//			}
//			if(req.getNoInsurer()!=null&&Integer.parseInt(req.getNoInsurer())==0){
//				res.setRetroDupYerar(req.getUwYear());
//				List<Map<String,Object>> list = getRetroContractDetailsList(req,3,req.getUwYear());
//				for (int j=0;j<list.size();j++){
//					Map<String,Object> map = list.get(j);
//					res.setRetroDupContract(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
//				}
			}
			response.setCommonResponse(res);
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
	public GetCommonValueRes getRetroContractDetails(GetRetroContractDetailsReq req) {
		GetCommonValueRes response = new GetCommonValueRes();
		String  Cedingco="";
		try{
			//FAC_SELECT_RETROCONTDET_23
			List<Tuple> list = nonProportCustomRepository.facSelectRetrocontdet23(req.getProductId(),req.getUwYear(),req.getIncepDate(),req.getBranchCode());
			
		//	beanObj.setRetroContractList(list);
		
			if(list!=null && list.size()>0){
				
				Tuple resMap;
				for(int i=0;i<list.size();i++) 
				{
					resMap = list.get(i); 
					if(i==(list.size()-1))
					{
						Cedingco+=resMap.get("CONTDET1").toString()+"~"+resMap.get("CONTDET1").toString();
					}else
					{
						Cedingco+=resMap.get("CONTDET1").toString()+"~"+resMap.get("CONTDET1").toString()+"~";	
					}
				}
			}
			response.setCommonResponse(Cedingco);
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
	public CheckAvialabilityRes checkAvialability(String proposalNo, String productId) {
		CheckAvialabilityRes response = new CheckAvialabilityRes();
		boolean saveFlag = false;
		String result="";
		try{
			//risk.select.getRskProIdByProNo
			List<Tuple> list = nonProportCustomRepository.riskSelectGetRskProIdByProNo(proposalNo);
		
			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0).get("RSK_PRODUCTID")== null ? "": list.get(0).get("RSK_PRODUCTID").toString();
			}
			if (result.equals(productId)) {
				saveFlag = true;
			} else {
				saveFlag = false;
			}
			response.setSaveFlag(saveFlag);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public insertProportionalTreatyRes insertProportionalTreaty(insertProportionalTreatyReq req) {
		insertClassLimitReq req2 = new insertClassLimitReq();
		insertProportionalTreatyRes response = new insertProportionalTreatyRes();
		RemarksSaveReq req3 = new RemarksSaveReq();
		boolean savFlg = false, ChkSavFlg=false;
		int updateCount = 0;
		if("Renewal".equalsIgnoreCase(req.getRenewalEditMode())){
		ChkSavFlg = true;
		}else {
			ChkSavFlg = checkEditSaveModeMethod(req);
		}
		try { 
			String [] args=null;
			if (req.isSaveFlag()) {	
				if (ChkSavFlg){
					String maxAmendID= getMaxAmednId(req.getProposalNo());
					args = getFirstPageEditSaveModeAruguments(req,maxAmendID);
				
					//UpdateProportionalTreatyQuery
					TtrnRiskDetails list = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNo(req.getProposalNo(), maxAmendID);
					if(list!=null) {
						list.setRskLayerNo(StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()))	;	
						ttrnRiskDetailsRepository.saveAndFlush(list);
						}
					
					//risk.update.rskDtls
					TtrnRiskDetails	 update = nonProportCustomRepository.ttrnRiskDetailsUpdate(args);
					if(update!=null) {
					ttrnRiskDetailsRepository.saveAndFlush(update);
					updateCount = 1;
					}
					args[1]=(Integer.parseInt(maxAmendID)+1)+"";
					if (updateCount > 0) {
						savFlg = true;
					} else {
						savFlg= false;
					}
				} else {
					args = getFirstPageInsertAruguments(req);
					//risk.insert.isAmendIDProTreaty
					TtrnRiskDetails	 insert = nonProportCustomRepository.ttrnRiskDetailsInsert(args);
					ttrnRiskDetailsRepository.saveAndFlush(insert);	
					response.setContractGendration("Your Proposal Number :"+ req.getProposalNo());
				}
			} 
			else {
				String maxAmendID = getMaxAmednId(req.getProposalNo());
				if( maxAmendID.equalsIgnoreCase(req.getAmendId())) {
					args = getFirstPageInsertAruguments(req);
					//risk.insert.isAmendIDProTreaty
					TtrnRiskDetails	 insert = nonProportCustomRepository.ttrnRiskDetailsInsert(args);
					ttrnRiskDetailsRepository.saveAndFlush(insert);	
				}
				else{
					//UpdateProportionalTreatyQuery
					TtrnRiskDetails list = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNo(req.getProposalNo(), maxAmendID);
					if(list!=null) {
						list.setRskLayerNo(StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()))	;	
						ttrnRiskDetailsRepository.saveAndFlush(list);
						}
					args = getFirstPageEditSaveModeAruguments(req,getMaxAmednId(req.getProposalNo()));
					//risk.update.rskDtls
					TtrnRiskDetails	 update = nonProportCustomRepository.ttrnRiskDetailsUpdate(args);
					if(update!=null) {
					ttrnRiskDetailsRepository.saveAndFlush(update);
					updateCount = 1;
					}
					args[1]=(Integer.parseInt(maxAmendID)+1)+"";
				}
			}
		
			
			insertClassLimit(req);
//			insertRemarkDetails(req3);
			response = insertRiskProposal(req,ChkSavFlg);
			SaveSecondPageRes secRes = 	saveSecondPage(req);
			instalMentPremium(req);
			if(StringUtils.isNotBlank(req.getRequestNumber()) && !"0".equals(req.getRequestNumber())) {
				proportionalityCustomRepository.updateBonus(req.getRequestNumber(),req.getProposalNo());
				proportionalityCustomRepository.updateRip(req.getRequestNumber(),req.getProposalNo());
			}
			response.setContractGendration(secRes.getCommonResponse().getContractGendration());
			response.setMessage("Success");
			response.setIsError(false);
			}
		catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public ShowRetroCess1Res showRetroCess1(ShowRetroCess1Req req) {
		ShowRetroCess1Res response = new ShowRetroCess1Res();
		List<RetroCessListRes> retriCessResList = new ArrayList<RetroCessListRes>();
		List<Tuple> list = new ArrayList<>();
		try{
			
			if((req.getMode()==1)||(req.getMode()==3)){
				String[] selectArgs = null;
				if(req.getMode()==1){
					selectArgs = new String[2];
					selectArgs[0] = req.getProposalNo();
					selectArgs[1] = String.valueOf(Integer.parseInt(req.getNoRetroCess())-1);
					//risk.select.getRetroCessDtls
					list = nonProportCustomRepository.riskSelectGetRetroCessDtls(selectArgs);
				}else if(req.getMode()==3){
					selectArgs = new String[3];
					selectArgs[0] = req.getAmendId();
					selectArgs[1] = req.getProposalNo();
					selectArgs[2] = String.valueOf(Integer.parseInt(req.getNoRetroCess())-1);
					//risk.select.viewRetroCessDtls
					list = nonProportCustomRepository.riskSelectViewRetroCessDtls(selectArgs);
				}
			
				if(list.size()!=0){
					for(int i = 0; i < list.size(); i++) {
						RetroCessListRes retriCessRes = new RetroCessListRes();
						Tuple resMap = list.get(i);
						retriCessRes.setCedingCompany(resMap.get("CEDING_COMPANY_ID")==null?"":resMap.get("CEDING_COMPANY_ID").toString());
						retriCessRes.setRetroBroker(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
						retriCessRes.setProposalStatus(resMap.get("PROPOSAL_STATUS")==null?"":resMap.get("PROPOSAL_STATUS").toString());
						retriCessRes.setShareAccepted(resMap.get("SHARE_ACCEPTED")==null?"":resMap.get("SHARE_ACCEPTED").toString());
						retriCessRes.setShareSigned(resMap.get("SHARE_SIGNED")==null?"":resMap.get("SHARE_SIGNED").toString());
						retriCessRes.setCommission(resMap.get("COMISSION")==null?"":resMap.get("COMISSION").toString());
						retriCessResList.add(retriCessRes);
					}
				}else{
					int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
					
					for(int i=0;i<NoRetroCess;i++){
					RetroCessListRes retriCessRes = new RetroCessListRes();
					RetroCessListReq req2 =	req.getRetroCessReq().get(i);
					retriCessRes.setCedingCompany(req2.getCedingId());
					retriCessRes.setRetroBroker(req2.getBrokerId());
					retriCessRes.setProposalStatus(req2.getProStatus());
					retriCessRes.setShareAccepted(req2.getShareWritt());
					retriCessRes.setShareSigned(req2.getSharSign());
					retriCessRes.setCommission(req2.getCommission());
					retriCessResList.add(retriCessRes);
				}
				}	
			}else{
				int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
				
				for(int i=0;i<NoRetroCess;i++){
					RetroCessListReq req3 =	req.getRetroCessReq().get(i);
					RetroCessListRes retriCessRes = new RetroCessListRes();
					retriCessRes.setCedingCompany(req3.getCedingId());
					retriCessRes.setRetroBroker(req3.getBrokerId());
					retriCessRes.setProposalStatus(req3.getProStatus());
					retriCessRes.setShareAccepted(req3.getShareWritt());
					retriCessRes.setShareSigned(req3.getSharSign());
					retriCessRes.setCommission(req3.getCommission());
					retriCessResList.add(retriCessRes);
				}	
			}
			response.setCommonResponse(retriCessResList);
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
	public UpdateProportionalTreatyRes updateProportionalTreaty(insertProportionalTreatyReq req) {
		UpdateProportionalTreatyRes response = new UpdateProportionalTreatyRes();
		boolean savFlg = false;
		int updateCount = 0;
		try {
			//update.RSK_LAYER_NO
			TtrnRiskDetails list = ttrnRiskDetailsRepository.findByRskProposalNumberAndRskEndorsementNo(req.getProposalNo(),new BigDecimal(getMaxAmednId(req.getProposalNo())));
			if(list!=null) {
				list.setRskLayerNo(StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()));
				ttrnRiskDetailsRepository.saveAndFlush(list);
				}
			
		//	String[] args = getFirstPageSaveModeAruguments(beanObj, req.getProductId(),getMaxAmednId(req.getProposalNo()));
			String[] args=null;
			args = new String[55]; //RI
			args[0] = req.getDepartmentId();
			args[1] = req.getProfitCenter();
			args[2] = req.getSubProfitcenter();
			args[3] = StringUtils.isBlank(req.getPolicyBranch())? "":req.getPolicyBranch();
			args[4] = req.getCedingCo();
			args[5] = StringUtils.isBlank(req.getBroker())?"63":req.getBroker();
			args[6] = req.getTreatyNametype();
			args[7] = StringUtils.isBlank(req.getMonth())? "":req.getMonth();
			args[8] = req.getUwYear();
			args[9] = StringUtils.isBlank(req.getUnderwriter())? "":req.getUnderwriter();
			args[10] = req.getIncepDate();
			args[11] = req.getExpDate();
			args[12] = StringUtils.isBlank(req.getAccDate())? "":req.getAccDate();
			args[13] = req.getOrginalCurrency();
			args[14] = req.getExchangeRate();
			args[15] = req.getBasis();
			args[16] = "";
			args[17] = "";
			args[18] = StringUtils.isBlank(req.getTerritoryscope())? "":req.getTerritoryscope();
			args[19] = StringUtils.isBlank(req.getTerritory())?"":req.getTerritory();
			args[20] = req.getProStatus();
			args[21] = "";
			args[22] = "0";
			args[23] = "0";
			args[24] = "0";
			
			args[25] = StringUtils.isEmpty(req.getMdInstalmentNumber()) ? "0"	: req.getMdInstalmentNumber();
			args[26] = StringUtils.isEmpty(req.getNoRetroCess()) ? "0": req.getNoRetroCess();
			args[27] = StringUtils.isEmpty(req.getRetroType()) ? "0": req.getRetroType();
			args[28] = StringUtils.isEmpty(req.getInsuredName()) ? "": req.getInsuredName();
			args[29] = StringUtils.isEmpty(req.getInwardType()) ? "": req.getInwardType();
			args[30] = StringUtils.isEmpty(req.getTreatyType()) ? "": req.getTreatyType();
			
			args[31]=StringUtils.isEmpty(req.getBusinessType()) ? ""	: req.getBusinessType();
			args[32]=StringUtils.isEmpty(req.getExchangeType()) ? ""	: req.getExchangeType();
			args[33]=StringUtils.isEmpty(req.getPerilCovered()) ? ""	: req.getPerilCovered();
			args[34]=StringUtils.isEmpty(req.getLOCIssued()) ? ""	: req.getLOCIssued();
			args[35]=StringUtils.isEmpty(req.getUmbrellaXL()) ? ""	: req.getUmbrellaXL();
			
			args[36]=req.getLoginId();
			args[37]=req.getBranchCode();
			args[38] = StringUtils.isEmpty(req.getCountryIncludedList()) ? ""	:req.getCountryIncludedList();
			args[39] = StringUtils.isEmpty(req.getCountryExcludedList()) ? ""	:req.getCountryExcludedList();
			args[40] = "";
			args[41] =StringUtils.isEmpty(req.getEndorsmenttype()) ? ""	:req.getEndorsmenttype();
			args[42] ="0";
			args[43] =StringUtils.isEmpty(req.getLocBankName()) ? ""	:req.getLocBankName();
			args[44] =StringUtils.isEmpty(req.getLocCreditPrd()) ? ""	:req.getLocCreditPrd();
			args[45] =StringUtils.isEmpty(req.getLocCreditAmt()) ? ""	:req.getLocCreditAmt().replaceAll(",", "");
			args[46] =StringUtils.isEmpty(req.getLocBeneficerName()) ? ""	:req.getLocBeneficerName();
			args[47] = "";
			args[48]="";
			args[49]=StringUtils.isEmpty(req.getRetentionYN()) ? ""	:req.getRetentionYN();
			args[50] = StringUtils.isEmpty(req.getAccountingPeriodNotes())?"":req.getAccountingPeriodNotes(); //ri
			args[51] =StringUtils.isEmpty(req.getStatementConfirm())?"":req.getStatementConfirm();
			args[52] = req.getProposalNo();
			args[53]=req.getEndNo();
			args[54] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			
			//risk.update.rskDtls
			TtrnRiskDetails	 update = nonProportCustomRepository.ttrnRiskDetailsUpdate(args);
			if(update!=null) {
			ttrnRiskDetailsRepository.saveAndFlush(update);
			updateCount = 1;
			}
			
		//	int updateCount = this.mytemplate.update(updateQry, args);
			
			String proposalno="";
			if (StringUtils.isNotEmpty(req.getLayerProposalNo())) {
				proposalno = req.getLayerProposalNo();
			} else {
				proposalno = req.getProposalNo();
			}
			if (updateCount > 0) {
			
				insertClassLimit(req);
				//InsertRemarkDetails(req); //already prop
//				GetRemarksDetails(req); 
				updateOfferNo(req.getOfferNo(),req.getBranchCode(),req.getProposalNo(),req.getProductId());
				updateRiskProposal(req);
//				this.showSecondpageEditItems(beanObj, pid, proposalno);
				savFlg = true;
			}
			
			instalMentPremium(req);
			SaveSecondPageRes secRes=saveSecondPage(req);
		
			response.setStatus(secRes.getCommonResponse().getContractGendration());
			//response.setSaveFlag(savFlg);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

		private void updateOfferNo(String offerNo, String branchCode, String proposalNo, String productId) {
		try {
			if(StringUtils.isBlank(offerNo)) {
				offerNo=fm.getSequence("Offer",productId,"0", branchCode,"","");
				
				PositionMaster pm = positionMasterRepository.findByProposalNo(new BigDecimal(proposalNo));
				if(pm!=null) {
					pm.setOfferNo(offerNo);
					positionMasterRepository.saveAndFlush(pm);
				}
				//UPDATE POSITION_MASTER SET OFFER_NO=? WHERE PROPOSAL_NO=?
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String getMaxAmednId(String  proposalNo) {
		String result = "0";
		try{
			//risk.select.getMaxEndorseNo
			TtrnRiskDetails list = ttrnRiskDetailsRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if(list!=null) {
				result=list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString();
			}
		}catch(Exception e){
		e.printStackTrace();
		}
		return result;
		}

//	@Override
//	public CommonResponse insertClassLimit(insertClassLimitReq req) {
//		CommonResponse response = new CommonResponse();
//		try {
//			if("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())){
//			String  deleteQuery="DELETE_CLASS_LIMIT";
//			String[] dobj= new String[1];
//			dobj[0]=req.getProposalNo();
//			queryImpl.updateQuery(deleteQuery, dobj);
//			
//			String query="INSERT_CLASS_LIMIT";
//			if(!"5".equalsIgnoreCase(req.getBusinessType())){
//			for(int i=0;i<req.getCoverLimitOCReq().size();i++){
//				String[] obj= new String[14];
//				obj[0]=req.getProposalNo();
//				obj[1]=(getMaxAmednId(req.getProposalNo()))+"";
//				obj[2]=req.getContNo();
//				obj[3]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
//				obj[4]=req.getProductId();
//				obj[5]=req.getCoverLimitOCReq().get(i).getCoverdepartId();
//				obj[6]=req.getCoverLimitOCReq().get(i).getCoverLimitOC().replace(",", "");
//				obj[7]="";
//				obj[8]=req.getCoverLimitOCReq().get(i).getDeductableLimitOC().replace(",", "");
//				obj[9]="";
//				obj[10]=req.getBranchCode();
//				obj[11]=String.valueOf(i+1);
//				obj[12]=req.getCoverLimitOCReq().get(i).getEgnpiAsPerOff().replace(",", "");
//				obj[13]=req.getCoverLimitOCReq().get(i).getGnpiAsPO()==null?"0":req.getCoverLimitOCReq().get(i).getGnpiAsPO().replace(",", "");
//				queryImpl.updateQuery(query, obj);
//			
//		//		String val = req.getCoverLimitOCReq().get(i).getCoverLimitOC().replace(",", "");
//			}
//			}
//			else{
//				for(int i=0;i<req.getCoverLimitAmountReq().size();i++){
//					String[] obj= new String[14];
//					obj[0]=req.getProposalNo();
//					obj[1]=(getMaxAmednId(req.getProposalNo()))+"";
//					obj[2]=req.getContNo();
//					obj[3]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
//					obj[4]=req.getProductId();
//					obj[5]=req.getCoverLimitAmountReq().get(i).getCoverdepartIdS();
//					obj[6]=req.getCoverLimitAmountReq().get(i).getCoverLimitAmount().replace(",", "");
//					obj[7]=req.getCoverLimitAmountReq().get(i).getCoverLimitPercent().replace(",", "");
//					obj[8]=req.getCoverLimitAmountReq().get(i).getDeductableLimitAmount().replace(",", "");
//					obj[9]=req.getCoverLimitAmountReq().get(i).getDeductableLimitPercent().replace(",", "");
//					obj[10]=req.getBranchCode();
//					obj[11]=String.valueOf(i+1);
//					obj[12]=req.getCoverLimitAmountReq().get(i).getEgnpiAsPerOffSlide().replace(",", "");
//					obj[13]=req.getCoverLimitAmountReq().get(i).getGnpiAsPOSlide().replace(",", "");
//					queryImpl.updateQuery(query, obj);
//		}
//			}
//			
//			}
//			response.setMessage("Success");
//			response.setIsError(false);
//			}catch(Exception e){
//				e.printStackTrace();
//				response.setMessage("Failed");
//				response.setIsError(true);
//			}
//		return response;
//	}
	private boolean updateRiskProposal(final insertProportionalTreatyReq beanObj) {
		boolean saveFlag = false;
		String endtNo="";
		try {
			int res=0;
			String[] obj=null;
			//risk.select.maxEndom
			TtrnRiskDetails list = ttrnRiskDetailsRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(beanObj.getProposalNo());
			if(list!=null) {
				endtNo=list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString();
			}
		
			obj = updateRiskProposalArgs(beanObj,beanObj.getProductId() ,endtNo);
			//risk.update.pro35FirPageRskPro
			res = nonProportCustomRepository.riskUpdatePro35FirPageRskPro(obj);
		
			if (res> 0) {
				saveFlag = true;
			}
			updateFirstPageFields(beanObj, getMaxAmednIdPro(beanObj.getProposalNo()));
			//if(StringUtils.isBlank(beanObj.getBaseLayer())) {

			obj = updateHomePostion(beanObj, beanObj.getProductId(),true);
			//risk.update.positionMaster
			PositionMaster update1 = nonProportCustomRepository.positionMasterUpdate(obj);
			if(update1!=null) {
				positionMasterRepository.saveAndFlush(update1);	
				res=1;
			}
			
			if (res > 0) {
				saveFlag = true;
			}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveFlag;
	}
	public String[] updateRiskProposalArgs(final insertProportionalTreatyReq beanObj,final String pid,String endNo) {
		String[] obj=null;
		obj = new String[38];
		obj[0] = StringUtils.isBlank(beanObj.getLimitOrigCur())?"":beanObj.getLimitOrigCur().replaceAll(",", "");
		obj[1] = getDesginationCountry(StringUtils.isBlank(beanObj.getLimitOrigCur())?"":beanObj.getLimitOrigCur().replaceAll(",", ""), beanObj.getExchangeRate());
		obj[2] = beanObj.getEpi();
		obj[3] = getDesginationCountry(beanObj.getEpi(), beanObj.getExchangeRate());
		obj[4] = StringUtils.isBlank(beanObj.getShareWritt())? "":beanObj.getShareWritt();
		if (beanObj.getProStatus().equalsIgnoreCase("P")) {
			obj[5] = "0";
		} else if (beanObj.getProStatus().equalsIgnoreCase("A")) {
			obj[5] = StringUtils.isBlank(beanObj.getSharSign())?"":beanObj.getSharSign();
		} else if (beanObj.getProStatus().equalsIgnoreCase("R")) {
			obj[5] = "0";
		} else {
			obj[5] = "0";
		}
		obj[6] = StringUtils.isBlank(beanObj.getMaxLimitProduct())?"":beanObj.getMaxLimitProduct();
		obj[7] = beanObj.getSubPremium();
		obj[8] = getDesginationCountry(beanObj.getSubPremium(), beanObj.getExchangeRate());
		obj[9] = beanObj.getXlPremium();
		obj[10] = getDesginationCountry(beanObj.getXlPremium(), beanObj.getExchangeRate());
		obj[11] = StringUtils.isBlank(beanObj.getPortfoloCovered())? "":beanObj.getPortfoloCovered();
		obj[12] = beanObj.getDeduchunPercent();
		obj[13] = getDesginationCountry(beanObj.getDeduchunPercent(),beanObj.getExchangeRate());
		obj[14] = beanObj.getMdPremium();
		obj[15] = getDesginationCountry(beanObj.getMdPremium(), beanObj.getExchangeRate());
		obj[16] = StringUtils.isBlank(beanObj.getAdjRate())?"":beanObj.getAdjRate();
		obj[17] = StringUtils.isEmpty(beanObj.getSpRetro()) ? "" : beanObj.getSpRetro();
		obj[18] = StringUtils.isEmpty(beanObj.getNoInsurer()) ? "0" : beanObj.getNoInsurer();
		obj[19] = StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[20] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[21] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[22] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[23] =StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? "0"	: beanObj.getMdpremiumourservice();
		obj[24] = StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate());
		obj[25] =StringUtils.isEmpty(beanObj.getLimitPerVesselOC()) ? "0": beanObj.getLimitPerVesselOC();
		obj[26] = StringUtils.isEmpty(beanObj.getLimitPerVesselOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitPerVesselOC(), beanObj.getExchangeRate());
		obj[27] =StringUtils.isEmpty(beanObj.getLimitPerLocationOC()) ? "0"	: beanObj.getLimitPerLocationOC();
		obj[28] = StringUtils.isEmpty(beanObj.getLimitPerLocationOC())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getLimitPerLocationOC(), beanObj.getExchangeRate());
		obj[29] =StringUtils.isEmpty(beanObj.getEgnpiOffer()) ? "0"	: beanObj.getEgnpiOffer();
		obj[31] = StringUtils.isEmpty(beanObj.getEgnpiOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getEgnpiOffer(), beanObj.getExchangeRate());
		obj[30] =StringUtils.isEmpty(beanObj.getOurAssessment()) ? "0"	: beanObj.getOurAssessment();
		obj[32] = beanObj.getLoginId();
		obj[33] = beanObj.getBranchCode();
		obj[34] = beanObj.getProposalNo();
		obj[35]=endNo;
		obj[36] = beanObj.getMinimumpremiumPercent();
		obj[37] = beanObj.getGnpiCapPercent();
		
		layerNoUpdate(beanObj,endNo);
		
		return obj;
	}
	
	public CommonResponse insertClassLimit(insertProportionalTreatyReq req) {
		CommonResponse response = new CommonResponse();
		CoverList reqlist = new CoverList();
		List<CoverList> reqlist2 = new ArrayList<CoverList>();
			try {
				String query = "";
				if("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())){
			
				//DELETE_CLASS_LIMIT
				nonProportCustomRepository.deleteClassLimit(req.getProposalNo());
			
				//INSERT_CLASS_LIMIT
				if(!"5".equalsIgnoreCase(req.getBusinessType())){
				for(int i=0;i<req.getCoverList().size();i++){
					CoverList reqlist3 = req.getCoverList().get(i);
					String[] args1= new String[15]; //ri
					args1[0]=req.getProposalNo();
					args1[1]=(getMaxAmednId(req.getProposalNo()))+"";
					args1[2]=req.getContNo();
					args1[3]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
					args1[4]=req.getProductId();
					args1[5]=reqlist3.getCoverdepartId();
					args1[6]=reqlist3.getCoverLimitOC()==null?"0":reqlist3.getCoverLimitOC().replace(",", "");
					args1[7]="";
					args1[8]=reqlist3.getDeductableLimitOC()==null?"0":reqlist3.getDeductableLimitOC().replace(",", "");
					args1[9]="";
					args1[10]=req.getBranchCode();
					args1[11]=String.valueOf(i+1);
					args1[12]=reqlist3.getEgnpiAsPerOff()==null?"0":reqlist3.getEgnpiAsPerOff().replace(",", "");
					args1[13]=reqlist3.getGnpiAsPO()==null?"0":reqlist3.getGnpiAsPO().replace(",", "");
					args1[14]=reqlist3.getNetMaxRetentPer()==null?"0":reqlist3.getNetMaxRetentPer().replace(",", ""); //ri

					TtrnRskClassLimits	list = nonProportCustomRepository.insertClassLimit(args1);
					if(list!=null) {
						ttrnRskClassLimitsRepository.saveAndFlush(list)	;
						}
					//String val = reqlist3.getCoverLimitOC().replace(",", "");
				}
				}
				}
				else{
					for(int i=0;i<req.getCoverLimitAmount().size();i++){
						CoverLimitAmount covreq = req.getCoverLimitAmount().get(i);
						String [] args2= new String[15];
						args2[0]=req.getProposalNo();
						args2[1]=(getMaxAmednId(req.getProposalNo()))+"";
						args2[2]=req.getContNo();
						args2[3]=StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
						args2[4]=req.getProductId();
						args2[5]=covreq.getCoverdepartIdS();
						args2[6]=covreq.getCoverLimitAmount()==null?"0":covreq.getCoverLimitAmount().replace(",", "");
						args2[7]=covreq.getCoverLimitPercent()==null?"0":covreq.getCoverLimitPercent().replace(",", "");
						args2[8]=covreq.getDeductableLimitAmount()==null?"0":covreq.getDeductableLimitAmount().replace(",", "");
						args2[9]=covreq.getDeductableLimitPercent()==null?"0":covreq.getDeductableLimitPercent().replace(",", "");
						args2[10]=req.getBranchCode();
						args2[11]=String.valueOf(i+1);
						args2[12]=covreq.getEgnpiAsPerOffSlide()==null?"0":covreq.getEgnpiAsPerOffSlide().replace(",", "");
						args2[13]=covreq.getGnpiAsPOSlide()==null?"0":covreq.getGnpiAsPOSlide().replace(",", "");
						args2[14]="";
						TtrnRskClassLimits	list = nonProportCustomRepository.insertClassLimit(args2);
						if(list!=null) {
							ttrnRskClassLimitsRepository.saveAndFlush(list)	;
							}
			}
				}
				response.setMessage("Success");
				response.setIsError(false);
					}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
			}
	
	private String[] getFirstPageInsertAruguments(insertProportionalTreatyReq req) { 

		String[] args= new String[59]; //ri
		if (req.getAmendStatus()) {
			args[0] = req.getProposalNo();
			args[1] =(Integer.parseInt(getMaxAmednId(req.getProposalNo()))+1)+"";
			args[26] = req.getContNo();
		}else{
			args[26] = "";
			if(StringUtils.isBlank(req.getProposalNo())){
			req.setProposalNo(getMaxProposanlno(req.getPid(),req.getBranchCode(),req.getDepartmentId()));
			}
			args[0] = req.getProposalNo();
			args[1] = "0";
		}
		args[2] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
		args[27] = "";
		args[28] = "0";
		args[29] = "0";
		args[30] = "0";
		args[3] = req.getPid();
		args[4] = StringUtils.isEmpty(req.getDepartmentId()) ? "0" : req.getDepartmentId();
		args[5] = StringUtils.isEmpty(req.getProfitCenter()) ? "0" : req.getProfitCenter();
		args[6] = StringUtils.isEmpty(req.getSubProfitcenter()) ? "0" : req.getSubProfitcenter();
		args[7] = StringUtils.isEmpty(req.getPolicyBranch()) ? "0" : req.getPolicyBranch();
		args[8] = StringUtils.isEmpty(req.getCedingCo()) ? "0" : req.getCedingCo();
		args[9] = StringUtils.isEmpty(req.getBroker()) ? "63" : req.getBroker();
		args[10] = StringUtils.isEmpty(req.getTreatyNametype()) ? "" : req.getTreatyNametype();
		args[11] = StringUtils.isEmpty(req.getMonth()) ? "" : req.getMonth();
		args[12] = StringUtils.isEmpty(req.getUwYear()) ? "0" : req.getUwYear();
		args[13] = StringUtils.isEmpty(req.getUnderwriter()) ? "" : req.getUnderwriter();
		args[14] = StringUtils.isEmpty(req.getIncepDate()) ? "" : req.getIncepDate();
		args[15] = StringUtils.isEmpty(req.getExpDate()) ? "" : req.getExpDate();
		args[16] = StringUtils.isEmpty(req.getAccDate()) ? "" : req.getAccDate();
		args[17] = StringUtils.isEmpty(req.getOrginalCurrency()) ? "0" : req.getOrginalCurrency();
		args[18] = StringUtils.isEmpty(req.getExchangeRate()) ? "0" : req.getExchangeRate();
		args[19] = StringUtils.isEmpty(req.getBasis()) ? "0" : req.getBasis();
		args[20] = StringUtils.isEmpty(req.getPnoc()) ? "" : req.getPnoc();
		args[21] = StringUtils.isEmpty(req.getRiskCovered()) ? "0"	: req.getRiskCovered();
		args[22] = StringUtils.isEmpty(req.getTerritoryscope()) ? "" : req.getTerritoryscope();
		args[23] = StringUtils.isBlank(req.getTerritory())?"" :req.getTerritory();
		args[24] = StringUtils.isEmpty(req.getProStatus()) ? "0" : req.getProStatus();
		args[25] = "";
		args[31] = StringUtils.isEmpty(req.getMdInstalmentNumber()) ? "0": req.getMdInstalmentNumber();
		args[32] = StringUtils.isEmpty(req.getNoRetroCess()) ? "0"	: req.getNoRetroCess();
		args[33] = StringUtils.isEmpty(req.getRetroType()) ? "0" : req.getRetroType();
		args[34] = StringUtils.isEmpty(req.getInsuredName()) ? "" : req.getInsuredName();
		args[35] = StringUtils.isEmpty(req.getRenewalcontractno()) ? "": req.getRenewalcontractno();
		args[36]=StringUtils.isEmpty(req.getInwardType()) ? "0"	: req.getInwardType();
		args[37]=StringUtils.isEmpty(req.getTreatyType()) ? ""	: req.getTreatyType();
		args[38]=StringUtils.isEmpty(req.getBusinessType()) ? ""	: req.getBusinessType();
		args[39]=StringUtils.isEmpty(req.getExchangeType()) ? ""	: req.getExchangeType();
		args[40]=StringUtils.isEmpty(req.getPerilCovered()) ? ""	: req.getPerilCovered();
		args[41]=StringUtils.isEmpty(req.getLOCIssued()) ? ""	: req.getLOCIssued();
		args[42]=StringUtils.isEmpty(req.getUmbrellaXL()) ? ""	: req.getUmbrellaXL();
		args[43] = req.getLoginId();
		args[44] = req.getBranchCode();
		args[45] = StringUtils.isEmpty(req.getCountryIncludedList()) ? ""	:req.getCountryIncludedList();
		args[46] = StringUtils.isEmpty(req.getCountryExcludedList()) ? ""	:req.getCountryExcludedList();
		args[47] ="";
		args[48] =StringUtils.isEmpty(req.getEndorsmenttype()) ? ""	:req.getEndorsmenttype();
		args[49] = "0";
		args[50] =StringUtils.isEmpty(req.getLocBankName()) ? ""	:req.getLocBankName();
		args[51] =StringUtils.isEmpty(req.getLocCreditPrd()) ? ""	:req.getLocCreditPrd();
		args[52] =StringUtils.isEmpty(req.getLocCreditAmt()) ? ""	:req.getLocCreditAmt().replaceAll(",", "");
		args[53] =StringUtils.isEmpty(req.getLocBeneficerName()) ? ""	:req.getLocBeneficerName();
		args[54]="";
		args[55]="";
		args[56]=StringUtils.isEmpty(req.getRetentionYN()) ? ""	:req.getRetentionYN();
		args[57] = req.getAccountingPeriodNotes();
		args[58] = req.getStatementConfirm();
		return args;	
	}
		
		public String getMaxProposanlno(String pid,String branchCode, String deptId) {
			String result="";
			try{
				result=fm.getSequence("Proposal","3".equalsIgnoreCase(pid)?pid:"6","", branchCode,"","");
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}

	public boolean updateFirstPageFields(UpdateProportionalTreatyReq req, String endNo){
		boolean updateStatus = true;
		int res=0;
		
		String[] args= new String[57]; //ri
		try {
		args[0] = StringUtils.isEmpty(req.getEventlimit()) ? "": req.getEventlimit();
		args[1] = StringUtils.isEmpty(req.getEventlimit())	|| StringUtils.isEmpty(req.getExchRate()) ? "0"	: getDesginationCountry(req.getEventlimit(), req.getExchRate());
		args[2] = StringUtils.isEmpty(req.getEventLimitOurShare()) ? "0" : req.getEventLimitOurShare();
		args[3] = StringUtils.isEmpty(req.getEventLimitOurShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0" : getDesginationCountry(req.getEventLimitOurShare(), req.getExchRate());

		args[4] = StringUtils.isEmpty(req.getCoverLimitXL()) ? "": req.getCoverLimitXL();
		args[5] = StringUtils.isEmpty(req.getCoverLimitXL())	|| StringUtils.isEmpty(req.getExchRate()) ? "0"	: getDesginationCountry(req.getCoverLimitXL(), req.getExchRate());
		args[6] = StringUtils.isEmpty(req.getCoverLimitXLOurShare()) ? "0" : req.getCoverLimitXLOurShare();
		args[7] = StringUtils.isEmpty(req.getCoverLimitXLOurShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0" : getDesginationCountry(req.getCoverLimitXLOurShare(), req.getExchRate());

		args[8] = StringUtils.isEmpty(req.getDeductLimitXL()) ? "": req.getDeductLimitXL();
		args[9] = StringUtils.isEmpty(req.getDeductLimitXL())	|| StringUtils.isEmpty(req.getExchRate()) ? "0"	: getDesginationCountry(req.getDeductLimitXL(), req.getExchRate());
		args[10] = StringUtils.isEmpty(req.getDeductLimitXLOurShare()) ? "0" : req.getDeductLimitXLOurShare();
		args[11] = StringUtils.isEmpty(req.getDeductLimitXLOurShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0" : getDesginationCountry(req.getDeductLimitXLOurShare(), req.getExchRate());

		args[12] = StringUtils.isEmpty(req.getPml()) ? "" : req.getPml();
		args[13] = StringUtils.isEmpty(req.getPmlPercent()) ? "0" : req.getPmlPercent();

		args[14] = StringUtils.isEmpty(req.getEgnpipml()) ? "": req.getEgnpipml();
		args[15] = StringUtils.isEmpty(req.getEgnpipml())	|| StringUtils.isEmpty(req.getExchRate()) ? "0"	: getDesginationCountry(req.getEgnpipml(), req.getExchRate());
		args[16] = StringUtils.isEmpty(req.getEgnpipmlOurShare()) ? "0" : req.getEgnpipmlOurShare();
		args[17] = StringUtils.isEmpty(req.getEgnpipmlOurShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0" : getDesginationCountry(req.getEgnpipmlOurShare(), req.getExchRate());

		args[18] = StringUtils.isEmpty(req.getPremiumbasis()) ? "" : req.getPremiumbasis();
		args[19] = StringUtils.isEmpty(req.getMinimumRate()) ? "0" : req.getMinimumRate();
		args[20] = StringUtils.isEmpty(req.getMaximumRate()) ? "0" : req.getMaximumRate();
		args[21] = StringUtils.isEmpty(req.getBurningCostLF()) ? "0" : req.getBurningCostLF();

		args[22] = StringUtils.isEmpty(req.getMinPremium()) ? "": req.getMinPremium();
		args[23] = StringUtils.isEmpty(req.getMinPremium())	|| StringUtils.isEmpty(req.getExchRate()) ? "0"	: getDesginationCountry(req.getMinPremium(), req.getExchRate());
		args[24] = StringUtils.isEmpty(req.getMinPremiumOurShare()) ? "0" : req.getMinPremiumOurShare();
		args[25] = StringUtils.isEmpty(req.getMinPremiumOurShare()) || StringUtils.isEmpty(req.getExchRate()) ? "0" : getDesginationCountry(req.getMinPremiumOurShare(), req.getExchRate());
		args[26] = StringUtils.isEmpty(req.getPaymentDuedays()) ? "0" : req.getPaymentDuedays();

		args[27] =StringUtils.isEmpty(req.getLimitOrigCurPml()) ? "0": req.getLimitOrigCurPml();
		args[28] = StringUtils.isEmpty(req.getLimitOrigCurPml())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getLimitOrigCurPml(), req.getExchRate());
		args[29] =StringUtils.isEmpty(req.getLimitOrigCurPmlOS()) ? "0": req.getLimitOrigCurPmlOS();
		args[30] = StringUtils.isEmpty(req.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getLimitOrigCurPmlOS(), req.getExchRate());
		args[31] =StringUtils.isEmpty(req.getTreatyLimitsurplusOCPml()) ? "0": req.getTreatyLimitsurplusOCPml();
		args[32] = StringUtils.isEmpty(req.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getTreatyLimitsurplusOCPml(), req.getExchRate());
		args[33] =StringUtils.isEmpty(req.getTreatyLimitsurplusOCPmlOS()) ? "0": req.getTreatyLimitsurplusOCPmlOS();
		args[34] = StringUtils.isEmpty(req.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getTreatyLimitsurplusOCPmlOS(), req.getExchRate());
		args[35] =StringUtils.isEmpty(req.getEpipml()) ? "0": req.getEpipml();
		args[36] = StringUtils.isEmpty(req.getEpipml())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getEpipml(), req.getExchRate());
		args[37] =StringUtils.isEmpty(req.getEpipmlOS()) ? "0": req.getEpipmlOS();
		args[38] = StringUtils.isEmpty(req.getEpipmlOS())|| StringUtils.isEmpty(req.getExchRate()) ? "0": getDesginationCountry(req.getEpipmlOS(), req.getExchRate());
		args[39]=req.getRiskdetailYN();
		args[40]=req.getBrokerdetYN();
		args[41]=StringUtils.isBlank(req.getCoverdetYN())? "":req.getCoverdetYN();
		args[42]=req.getPremiumdetailYN();
		args[43]=req.getAcqdetailYN();
		args[44]=StringUtils.isBlank(req.getCommissiondetailYN())? "":req.getCommissiondetailYN();
		args[45]=StringUtils.isBlank(req.getDepositdetailYN())? "":req.getDepositdetailYN();
		args[46]=StringUtils.isBlank(req.getLossdetailYN())? "":req.getLossdetailYN();
		args[47]=StringUtils.isBlank(req.getDocdetailYN())? "":req.getDocdetailYN();
		args[48] = req.getPaymentPartner();
		args[49] = req.getInstallYN();
		args[50] = req.getReinstdetailYN();
		args[51] = req.getRateOnLine();
		args[52] =StringUtils.isEmpty(req.getQuotesharePercent()) ? "0": req.getQuotesharePercent();
		args[53] = req.getProposalNo();
		args[54]=endNo;
		args[55]=req.getMinimumpremiumPercent();
		args[56]=req.getGnpiCapPercent();
		
		//UPDATE_RISK_PROPOSAL_DETAILS
		TtrnRiskProposal update = nonProportCustomRepository.updateFirstPageFields(args);
		if(update!=null) {
		ttrnRiskProposalRepository.saveAndFlush(update);	
		res=1;
		}
		if (res> 0) {
		updateStatus = true;
		}
		} catch (Exception e) {
		e.printStackTrace();
		}
		return updateStatus;

		}
	public String getDesginationCountry(final String limitOrigCur, final String ExchangeRate) {
		String output="0.0";
		try{
		double origCountryVal=0.0;
		if(limitOrigCur!=null){
		if (!("".equalsIgnoreCase(limitOrigCur)) && Double.parseDouble(limitOrigCur) != 0) {
		origCountryVal = Double.parseDouble(limitOrigCur) / Double.parseDouble(ExchangeRate);
		output = fm.formatter(Double.toString(origCountryVal).toString()).replaceAll(",", "");
		}
		}
		}catch(Exception e){
		e.printStackTrace();
		}
		return output;
		}
	public String[] updateHomePostion(final insertProportionalTreatyReq beanObj,final String pid, final boolean bool) {

		String[] obj = new String[24]; //RI
		obj[0] = StringUtils.isEmpty(beanObj.getLayerNo()) ? "0" : beanObj.getLayerNo();
		obj[1] = "";
		obj[2] = pid;
		obj[3] = beanObj.getDepartmentId();
		obj[4] = beanObj.getCedingCo();
		obj[5] = beanObj.getUwYear();
		obj[6] = StringUtils.isBlank(beanObj.getMonth())? "":beanObj.getMonth();
		obj[7] = StringUtils.isBlank(beanObj.getAccDate())? "":beanObj.getAccDate();
		obj[8] = beanObj.getIncepDate();
		obj[9] = beanObj.getExpDate();
		obj[10] = beanObj.getProStatus().trim();
		if(beanObj.getContractNo()==null || beanObj.getContractNo().equalsIgnoreCase(""))
		{
			
			if(beanObj.getProStatus().equalsIgnoreCase("P"))
			{
				obj[11] ="P"; 
			}
			else if(beanObj.getProStatus().equalsIgnoreCase("A"))
			{
				obj[11] ="P";
			}
			else if(beanObj.getProStatus().equalsIgnoreCase("R"))
			{
				obj[11] ="R";
			}else if(beanObj.getProStatus().equalsIgnoreCase("N"))
			{
				obj[11] ="N";
			}
		}
		else
		{
			obj[11] =beanObj.getProStatus().trim();
		}
		obj[12] = StringUtils.isBlank(beanObj.getBroker())?"63":beanObj.getBroker();
		obj[13] = StringUtils.isBlank(beanObj.getRetroType())?"":beanObj.getRetroType();
		obj[14] = beanObj.getLoginId();
		obj[15] = "";
		obj[16] =  StringUtils.isEmpty(beanObj.getContractListVal())?"":beanObj.getContractListVal();
		obj[17] = beanObj.getBouquetModeYN();
		obj[18] = StringUtils.isBlank(beanObj.getBouquetNo())? "":beanObj.getBouquetNo();
		obj[19] = beanObj.getUwYearTo();
		obj[20] = "";
		obj[21] = beanObj.getProposalNo();
		obj[22] = StringUtils.isBlank(beanObj.getAmendId())? "":beanObj.getAmendId();
		obj[23] = beanObj.getNewLayerNo();
		
		return obj;
	}
	private void layerNoUpdate(insertProportionalTreatyReq beanObj, String endNo) {
		try{
			//RISK_LAYER_UPDATE
			TtrnRiskProposal list  = ttrnRiskProposalRepository.findByRskProposalNumberAndRskEndorsementNo(beanObj.getProposalNo(),new BigDecimal(endNo));
			if(list!=null) {
				list.setRskLayerNo(StringUtils.isEmpty(beanObj.getLayerNo())?BigDecimal.ZERO:new BigDecimal(beanObj.getLayerNo()));	
				ttrnRiskProposalRepository.saveAndFlush(list);
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public CommonSaveRes getLayerDuplicationCheck(String proposalNo, String layerNo, String baseLayer) {
		CommonSaveRes response = new CommonSaveRes();
		boolean result=false;
		List<Tuple>  list=new ArrayList<>();
		try{
			if(StringUtils.isNotBlank(proposalNo)&&StringUtils.isNotBlank(layerNo) ){ //ri
				//risk.select.getLayerDupcheckByProNo
				String proposalOrBase = StringUtils.isBlank(baseLayer)?proposalNo:baseLayer;
				 list = nonProportCustomRepository.riskSelectGetLayerDupcheckByProNo(layerNo,proposalNo,proposalOrBase);
				
				 if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Tuple map = list.get(i);
						String res=map.get("NEW_LAYER_NO")==null?"":map.get("NEW_LAYER_NO").toString();
						if(res.equalsIgnoreCase(layerNo)){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(baseLayer) && StringUtils.isNotBlank(layerNo) /* && "Yes".equalsIgnoreCase(formObj.getLayerMode()) */){
				//risk.select.getLayerDupcheckByBaseLayer
				list = 	nonProportCustomRepository.riskSelectGetLayerDupcheckByBaseLayer(layerNo,baseLayer);
			  
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Tuple map = list.get(i);
						String res=map.get("NEW_LAYER_NO")==null?"":map.get("NEW_LAYER_NO").toString();
						if(res.equalsIgnoreCase(layerNo)){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(proposalNo) && StringUtils.isNotBlank(layerNo)){
				//risk.select.getLayerDupcheckByBaseLayer
				list = 	nonProportCustomRepository.riskSelectGetLayerDupcheckByBaseLayer(layerNo,proposalNo);
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Tuple map = list.get(i);
						String res=map.get("NEW_LAYER_NO")==null?"":map.get("NEW_LAYER_NO").toString();
						if(res.equalsIgnoreCase(layerNo)){
							result=true;
						}
					}
				}
			}
			response.setResponse(String.valueOf(result));
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public List<Map<String, Object>> getValidation(String incepDate, String renewalContractNo)  {
		List<Map<String, Object>>  list = new ArrayList<>();
		try{
			//fac.select.getRenewalValidation
			list = nonProportCustomRepository.facSelectGetRenewalValidation(incepDate,renewalContractNo);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ViewRiskDetailsRes viewRiskDetails(ViewRiskDetailsReq req) {
		ViewRiskDetailsRes response = new ViewRiskDetailsRes();
		ViewRiskDetailsRes1 res = new ViewRiskDetailsRes1();
		//List<ViewRiskDetailsRes1> resList = new ArrayList<ViewRiskDetailsRes1>();
		try {
			String[] args=new String[10];
			args[0] = req.getBranchCode();
			args[1] = req.getBranchCode();
			args[2] = req.getBranchCode();
			args[3] = req.getBranchCode();
			args[4] = req.getBranchCode();
			args[5] = req.getBranchCode();
			args[6] = req.getBranchCode();
			args[7] = req.getBranchCode();
			args[8] = req.getProposalNo();
			args[9] = req.getAmendId();
			String selectQry = "risk.select.getCommonData"; //LEFT OUTER JOIN ,within groupby
			List<Map<String, Object>> list = queryImpl.selectList(selectQry,args);
			Map<String, Object> resMap = null;
			if(res!=null && list.size()>0)
				resMap = (Map<String, Object>)list.get(0);
			if (resMap != null) {
				res.setDepartId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				res.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
				res.setProposalType(resMap.get("PROPOSAL_TYPE")==null?"":resMap.get("PROPOSAL_TYPE").toString());
				res.setAccountingPeriod(resMap.get("ACCOUNTING_PERIOD")==null?"":resMap.get("ACCOUNTING_PERIOD").toString());
				res.setReceiptofPayment(resMap.get("RSK_RECEIPT_PAYEMENT")==null?"":resMap.get("RSK_RECEIPT_PAYEMENT").toString());
				res.setReceiptofStatements(resMap.get("RSK_RECEIPT_STATEMENT")==null?"":resMap.get("RSK_RECEIPT_STATEMENT").toString());
				res.setProfitCenter(resMap.get("TMAS_PFC_NAME")==null?"":resMap.get("TMAS_PFC_NAME").toString());
				res.setSubProfitCenter(resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString());
				if(!"ALL".equalsIgnoreCase(req.getSubProfitCenter())){
				res.setSubProfitCenter(resMap.get("TMAS_SPFC_NAME")==null?"":resMap.get("TMAS_SPFC_NAME").toString().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
				
				}
				
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setTreatyNametype(resMap.get("RSK_TREATYID")==null?"":resMap.get("RSK_TREATYID").toString());
				res.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
				res.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
				res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				res.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				res.setUnderwriter(resMap.get("UNDERWRITTER")==null?"":resMap.get("UNDERWRITTER").toString());
				res.setIncepDate(resMap.get("RSK_INCEPTION_DATE")==null?"":resMap.get("RSK_INCEPTION_DATE").toString());
				res.setExpDate(resMap.get("RSK_EXPIRY_DATE")==null?"":resMap.get("RSK_EXPIRY_DATE").toString());
				res.setAccDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":resMap.get("RSK_ACCOUNT_DATE").toString());
				res.setOrginalCurrency(resMap.get("SHORT_NAME")==null?"":resMap.get("SHORT_NAME").toString());
				res.setExchRate(resMap.get("RSK_EXCHANGE_RATE")==null?"":resMap.get("RSK_EXCHANGE_RATE").toString());
				res.setMdInstalmentNumber(resMap.get("MND_INSTALLMENTS")==null?"0":resMap.get("MND_INSTALLMENTS").toString());
				res.setRetroType(resMap.get("RSK_RETRO_TYPE")==null?"0":resMap.get("RSK_RETRO_TYPE").toString());
				res.setNoRetroCess(resMap.get("RETRO_CESSIONARIES")==null?"0":resMap.get("RETRO_CESSIONARIES").toString());
				if (resMap.get("RSK_BASIS") != null && !"0".equals(resMap.get("RSK_BASIS"))) {
					//risk.select.getDtlName
					List<ConstantDetail> cd = constantDetailRepository.findByCategoryIdAndStatusAndCategoryDetailId(
							new BigDecimal(6),"Y",new BigDecimal(resMap.get("RSK_BASIS").toString()));
					
					if(!CollectionUtils.isEmpty(cd)) {
						res.setBasis(cd.get(0).getDetailName()==null?"":cd.get(0).getDetailName().toString());
					}
				}
				res.setPnoc(resMap.get("RSK_PERIOD_OF_NOTICE")==null?"":resMap.get("RSK_PERIOD_OF_NOTICE").toString());
				res.setRiskCovered(resMap.get("RSK_RISK_COVERED")==null?"":resMap.get("RSK_RISK_COVERED").toString());
				res.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
				res.setTerritory(resMap.get("TERRITORY_DESC")==null?"":resMap.get("TERRITORY_DESC").toString());
				res.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString());
				res.setEndorsmenttype(resMap.get("RS_ENDORSEMENT_TYPE")==null?"":resMap.get("RS_ENDORSEMENT_TYPE").toString());
				
				//String qry = "SELECT   RTRIM(XMLAGG(XMLELEMENT(E,TERRITORY_NAME,',')).EXTRACT('//text()'),',') TERRITORY_NAME FROM   TMAS_TERRITORY  WHERE  TERRITORY_ID in("+res.getTerritory()+") and BRANCH_CODE="+req.getBranchCode();
				String qry ="select_TERRITORY_NAME";
				if(StringUtils.isNotBlank(res.getTerritory())){
					List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getTerritory(),req.getBranchCode()});
					if(!CollectionUtils.isEmpty(result)) {
						res.setTerritoryName(result.get(0).get("TERRITORY_NAME")==null?"":result.get(0).get("TERRITORY_NAME").toString());
					}
			
				}
				res.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE")==null?"":resMap.get("COUNTRIES_INCLUDE").toString());
//				if(StringUtils.isNotBlank(res.getCountryIncludedList())){
//			//	qry ="SELECT   RTRIM(XMLAGG(XMLELEMENT(E,COUNTRY_NAME,',')).EXTRACT('//text()'),',') COUNTRY_NAME FROM   country_master  WHERE  COUNTRY_ID in("+res.getCountryIncludedList()+") and BRANCH_CODE="+req.getBranchCode();
//				qry="select_COUNTRY_NAME";
//				List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getCountryIncludedList(),req.getBranchCode()});
//				if(!CollectionUtils.isEmpty(result)) {
//					res.setCountryIncludedName(result.get(0).get("COUNTRY_NAME")==null?"":result.get(0).get("COUNTRY_NAME").toString());
//				}
//				
//				res.setCountryIncludedName(res.getCountryIncludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
//				
//				}
				res.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE")==null?"":resMap.get("COUNTRIES_EXCLUDE").toString());
//				if(StringUtils.isNotBlank(res.getCountryExcludedList())){
//				//qry ="SELECT   RTRIM(XMLAGG(XMLELEMENT(E,COUNTRY_NAME,',')).EXTRACT('//text()'),',')  FROM   country_master  WHERE  COUNTRY_ID in("+beanObj.getCountryExcludedList()+") and BRANCH_CODE="+beanObj.getBranchCode();
//					qry ="select_COUNTRY_NAME1";
//				List<Map<String, Object>> result = queryImpl.selectList(qry,new String[]{res.getCountryExcludedList(),req.getBranchCode()});
//				if(!CollectionUtils.isEmpty(result)) {
//					res.setCountryExcludedName(result.get(0).get("COUNTRY_NAME")==null?"":result.get(0).get("COUNTRY_NAME").toString());
//				}
//				
//				res.setCountryExcludedName(res.getCountryExcludedName().replaceAll("&amp;", "&").replaceAll("&apos;", "'"));
//				}
				
				res.setBusinessType(resMap.get("RSK_BUSINESS_TYPE_Con")==null?"":resMap.get("RSK_BUSINESS_TYPE_Con").toString());
				res.setInwardType(resMap.get("INWARD_BUS_TYPE")==null?"":resMap.get("INWARD_BUS_TYPE").toString());
				res.setExchangeType(resMap.get("RSK_EXCHANGE_TYPE")==null?"":resMap.get("RSK_EXCHANGE_TYPE").toString());
				res.setPerilCovered(resMap.get("RSK_PERILS_COVERED")==null?"":resMap.get("RSK_PERILS_COVERED").toString());
				if("0".equalsIgnoreCase(res.getPerilCovered())){
					res.setPerilCovered("None");
				}else{
				res.setPerilCovered(resMap.get("RSK_PERILS_COVERED_Con")==null?"":resMap.get("RSK_PERILS_COVERED_Con").toString());
				}
				res.setLOCIssued(resMap.get("RSK_LOC_ISSUED")==null?"":resMap.get("RSK_LOC_ISSUED").toString());
				res.setUmbrellaXL(resMap.get("RSK_UMBRELLA_XL")==null?"":resMap.get("RSK_UMBRELLA_XL").toString());
				res.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
				res.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
				res.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
				res.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
				
				if (resMap.get("RSK_STATUS") != null) {
					if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("P")||"0".equalsIgnoreCase(resMap.get("RSK_STATUS").toString())) {
						res.setStatus("Pending");
					}else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("N")) {
						res.setStatus("Not Taken Up");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("A")) {
						res.setStatus("Accepted");
					} else if (resMap.get("RSK_STATUS").toString().equalsIgnoreCase("R")) {
						res.setStatus("Rejected");
					} else {
						res.setStatus("Pending");
					}
				}
				res.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
			}
			//risk.select.getSecondViewData
			List<Tuple> res1 = nonProportCustomRepository.riskSelectGetSecondViewData(req.getProposalNo(),req.getAmendId());
			
			Tuple secViewDataMap = null;
			if(res1!=null && res1.size()>0)
				secViewDataMap = res1.get(0);
			if (secViewDataMap != null) {
				res.setLimitOrigCur(secViewDataMap.get("RSK_LIMIT_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_OC").toString()));
				res.setLimitOrigCurDc(secViewDataMap.get("RSK_LIMIT_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_DC").toString()));
				res.setEpiorigCur(secViewDataMap.get("RSK_EPI_OFFER_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OFFER_OC").toString()));
				res.setEpiorigCurDc(secViewDataMap.get("RSK_EPI_OFFER_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OFFER_DC").toString()));
				res.setOurEstimate(secViewDataMap.get("RSK_EPI_ESTIMATE")==null?"":secViewDataMap.get("RSK_EPI_ESTIMATE").toString());
				res.setXlPremium(secViewDataMap.get("RSK_XLPREM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLPREM_OC").toString()));
				res.setXlPremiumDc(secViewDataMap.get("RSK_XLPREM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLPREM_DC").toString()));
				res.setDeduchunPercent(secViewDataMap.get("RSK_DEDUC_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUC_OC").toString()));
				res.setEpiEstmate(secViewDataMap.get("RSK_EPI_EST_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_EST_OC").toString()));
				res.setEpiEstmateDc(secViewDataMap.get("RSK_EPI_EST_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_EST_DC").toString()));
				res.setXlCost(secViewDataMap.get("RSK_XLCOST_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OC").toString()));
				res.setXlCostDc(secViewDataMap.get("RSK_XLCOST_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_DC").toString()));
				res.setCedReten(secViewDataMap.get("RSK_CEDANT_RETENTION")==null?"":fm.formatter(secViewDataMap.get("RSK_CEDANT_RETENTION").toString()));
				
				res.setShareWritt(secViewDataMap.get("RSK_SHARE_WRITTEN")==null?"":dropDowmImpl.formatterpercentage(secViewDataMap.get("RSK_SHARE_WRITTEN").toString()));
				res.setSharSign(secViewDataMap.get("RSK_SHARE_SIGNED")==null?"":dropDowmImpl.formatterpercentage(secViewDataMap.get("RSK_SHARE_SIGNED").toString()));
				res.setMdPremium(secViewDataMap.get("RSK_MD_PREM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OC").toString()));
				res.setMdpremiumDc(secViewDataMap.get("RSK_MD_PREM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_DC").toString()));
				res.setAdjRate(secViewDataMap.get("RSK_ADJRATE")==null?"":fm.formattereight(secViewDataMap.get("RSK_ADJRATE").toString()));
				res.setPortfoloCovered(secViewDataMap.get("RSK_PF_COVERED")==null?"":secViewDataMap.get("RSK_PF_COVERED").toString());
				res.setSubPremium(secViewDataMap.get("RSK_SUBJ_PREMIUM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_SUBJ_PREMIUM_OC").toString()));
				
				res.setSubPremiumOSOC(getShareVal(res.getSubPremium().replaceAll(",", ""),res.getSharSign(),"share"));
				res.setDeduchunPercentOSOC(getShareVal(res.getDeduchunPercent().replaceAll(",", ""),res.getSharSign(),"share"));
				res.setDeduchunPercentOSDC(fm.formatter(getDesginationCountry(res.getDeduchunPercentOSOC().replaceAll(",",""),res.getExchRate()).toString()));
				res.setSubPremiumOSDC(fm.formatter(getDesginationCountry(res.getSubPremiumOSOC().replaceAll(",",""),res.getExchRate()).toString()));
				res.setSubPremiumDc(secViewDataMap.get("RSK_SUBJ_PREMIUM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_SUBJ_PREMIUM_DC").toString()));
				res.setMaxLimitProduct(secViewDataMap.get("RSK_MAX_LMT_COVER")==null?"":fm.formatter(secViewDataMap.get("RSK_MAX_LMT_COVER").toString()));
				res.setDeduchunPercentDc(secViewDataMap.get("RSK_DEDUC_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUC_DC").toString()));
				if("3".equalsIgnoreCase(req.getProductId())){
					res.setLimitOurShare(secViewDataMap.get("RSK_LIMIT_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_OS_OC").toString()));
					res.setLimitOurShareDc(secViewDataMap.get("RSK_LIMIT_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_LIMIT_OS_DC").toString()));
				}else{
					res.setLimitOurShare(getShareVal(res.getLimitOrigCur().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLimitOurShareDc(fm.formatter(getDesginationCountry(res.getLimitOurShare().replaceAll(",",""),res.getExchRate()).toString()));
				}
				if("3".equalsIgnoreCase(req.getProductId())){
					res.setEpiAsPerOffer(secViewDataMap.get("RSK_EPI_OSOF_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOF_OC").toString()));
					res.setEpiAsPerOfferDc(secViewDataMap.get("RSK_EPI_OSOF_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOF_DC").toString()));
				}else{
					res.setEpiAsPerOffer(getShareVal(res.getEpiEstmate().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setEpiAsPerOfferDc(fm.formatter(getDesginationCountry(res.getEpiAsPerOffer().replaceAll(",",""),res.getExchRate()).toString()));
				}
				
				res.setEpiOurShareEs(secViewDataMap.get("RSK_EPI_OSOE_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOE_OC").toString()));
				res.setEpiOurShareEsDc(secViewDataMap.get("RSK_EPI_OSOE_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_EPI_OSOE_DC").toString()));
				res.setXlcostOurShare(secViewDataMap.get("RSK_XLCOST_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OS_OC").toString()));
				res.setXlcostOurShareDc(secViewDataMap.get("RSK_XLCOST_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_XLCOST_OS_DC").toString()));
				res.setMdpremiumourservice(secViewDataMap.get("RSK_MD_PREM_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OS_OC").toString()));
				res.setMdpremiumourserviceDc(secViewDataMap.get("RSK_MD_PREM_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MD_PREM_OS_DC").toString()));
				res.setSpRetro(secViewDataMap.get("RSK_SP_RETRO")==null?"0":secViewDataMap.get("RSK_SP_RETRO").toString());
				res.setNoInsurer(secViewDataMap.get("RSK_NO_OF_INSURERS")==null?"0":secViewDataMap.get("RSK_NO_OF_INSURERS").toString());
				res.setMaxLimitProduct(secViewDataMap.get("RSK_MAX_LMT_COVER")==null?"0.00":fm.formatter(secViewDataMap.get("RSK_MAX_LMT_COVER").toString()));
				res.setCedRetenType(secViewDataMap.get("CEDRET_TYPE")==null?"0":secViewDataMap.get("CEDRET_TYPE").toString());
				res.setOurAssessment(secViewDataMap.get("OURASSESSMENT")==null?"0":secViewDataMap.get("OURASSESSMENT").toString());
				res.setEgnpiOffer(secViewDataMap.get("EGPNI_AS_OFFER")==null?"":fm.formatter(secViewDataMap.get("EGPNI_AS_OFFER").toString()));
				res.setEgnpiOfferOSOC(getShareVal(res.getEgnpiOffer().replaceAll(",", ""),res.getSharSign(),"share"));
				res.setEgnpiOfferOSDC(fm.formatter(getDesginationCountry(res.getEgnpiOfferOSOC().replaceAll(",",""),res.getExchRate()).toString()));
				res.setEgnpiOfferDC(secViewDataMap.get("EGPNI_AS_OFFER_DC")==null?"":fm.formatter(secViewDataMap.get("EGPNI_AS_OFFER_DC").toString()));
				res.setPml(secViewDataMap.get("RSK_PML")==null ? "" : secViewDataMap.get("RSK_PML").toString());
				res.setPmlPercent(secViewDataMap.get("RSK_PML_PERCENT")==null ? "" : secViewDataMap.get("RSK_PML_PERCENT").toString());
				res.setPremiumbasis(secViewDataMap.get("RSK_PREMIUM_BASIS_Con")==null ? "" : secViewDataMap.get("RSK_PREMIUM_BASIS_Con").toString());
				res.setMinimumRate(secViewDataMap.get("RSK_MINIMUM_RATE")==null ? "" :fm.formattereight(secViewDataMap.get("RSK_MINIMUM_RATE").toString()));
				res.setMaximumRate(secViewDataMap.get("RSK_MAXIIMUM_RATE")==null ? "" : fm.formattereight(secViewDataMap.get("RSK_MAXIIMUM_RATE").toString()));
				res.setBurningCostLF(secViewDataMap.get("RSK_BURNING_COST_LF")==null ? "" : fm.formatter(secViewDataMap.get("RSK_BURNING_COST_LF").toString()));
				res.setPaymentDuedays(secViewDataMap.get("RSK_PAYMENT_DUE_DAYS")==null ? "" : secViewDataMap.get("RSK_PAYMENT_DUE_DAYS").toString());
				res.setMinPremium(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OC").toString()));
				res.setMinPremiumDC(secViewDataMap.get("RSK_MINIMUM_PREMIUM_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MINIMUM_PREMIUM_DC").toString()));
				res.setMinPremiumOurShare(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OS_OC").toString()));
				res.setMinPremiumOurShareDC(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_MINIMUM_PREMIUM_OS_DC").toString()));
				res.setCoverLimitXL(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OC").toString()));
				res.setCoverLimitXLDC(secViewDataMap.get("RSK_COVER_LIMIT_UXL_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_COVER_LIMIT_UXL_DC").toString()));
				res.setCoverLimitXLOurShare(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OS_OC").toString()));
				res.setCoverLimitXLOurShareDC(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_COVER_LIMIT_UXL_OS_DC").toString()));
				res.setDeductLimitXL(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OC").toString()));
				res.setDeductLimitXLDC(secViewDataMap.get("RSK_DEDUCTABLE_UXL_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUCTABLE_UXL_DC").toString()));
				res.setDeductLimitXLOurShare(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OS_OC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OS_OC").toString()));
				res.setDeductLimitXLOurShareDC(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OS_DC")==null?"":fm.formatter(secViewDataMap.get("RSK_DEDUCTABLE_UXL_OS_DC").toString()));
			}

			int number = 0;
			//risk.select.viewInstalmentData
			List<TtrnMndInstallments> instalmentList = ttrnMndInstallmentsRepository.findByProposalNoAndLayerNoAndEndorsementNoAndEndorsementNoNotNullOrderByInstallmentNo(
					req.getProposalNo(),StringUtils.isEmpty(res.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()),new BigDecimal(req.getAmendId()));
	
			List<InstalmentListRes1> instalmentResList = new ArrayList<InstalmentListRes1>();
			if (instalmentList != null  && instalmentList.size()>0) {
				for (number = 0; number < instalmentList.size(); number++) {
					TtrnMndInstallments insMap = instalmentList.get(number);
					InstalmentListRes1 instalmentRes = new InstalmentListRes1();
					instalmentRes.setDateList(insMap.getInstallmentDate()==null?"":insMap.getInstallmentDate().toString());
					instalmentRes.setPremiumList(insMap.getMndPremiumOc()==null?"":fm.formatter(insMap.getMndPremiumOc().toString()));
					instalmentRes.setPaymentdays(insMap.getPayementDueDay()==null?"":insMap.getPayementDueDay().toString());	
					instalmentResList.add(instalmentRes);
				}
							
							
				}else{
			
				for (int k = 0; k <Integer.parseInt(res.getNoInsurer()); k++) {
					InstalmentListRes1 instalmentRes = new InstalmentListRes1();
					instalmentRes.setPaymentdays(res.getPaymentDuedays());
					instalmentResList.add(instalmentRes);
				}
				
			}
			res.setInstalmentList(instalmentResList);
			args = new String[5];
			args[0] = req.getBranchCode();
			args[1] = req.getProposalNo();
			args[2] = req.getAmendId();
			args[3] = req.getProposalNo();
			args[4] = req.getAmendId();
			selectQry = "risk.select.getThirdPageData"; // LEFT OUTER JOIN 
			
			List<Map<String, Object>> res3 = queryImpl.selectList(selectQry,args);
			
			Map<String, Object> thirdViewDataMap = null;
			if(res3!=null && res3.size()>0)
				thirdViewDataMap = (Map<String, Object>)res3.get(0);
			if (thirdViewDataMap != null) {
				for (int k = 0; k < res3.size(); k++) {
					res.setBrokerage(thirdViewDataMap.get("RSK_BROKERAGE")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_BROKERAGE").toString()));
					res.setTax(thirdViewDataMap.get("RSK_TAX")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_TAX").toString()));
					if (thirdViewDataMap.get("RSK_PROFIT_COMM") != null) {
						if (thirdViewDataMap.get("RSK_PROFIT_COMM").toString().equalsIgnoreCase("1")) {
							res.setShareProfitCommission("YES");
						} else {
							res.setShareProfitCommission("NO");
						}
					}
					res.setPremiumQuotaShare(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_QUOTA_SHARE").toString()));
					res.setPremiumSurplus(thirdViewDataMap.get("RSK_PREMIUM_SURPULS")==null?"":fm.formatter(thirdViewDataMap.get("RSK_PREMIUM_SURPULS").toString()));
					res.setAcquisitionCost(thirdViewDataMap.get("RSK_ACQUISTION_COST_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_ACQUISTION_COST_OC").toString()));
					res.setAcquisitionCostDc(thirdViewDataMap.get("RSK_ACQUISTION_COST_DC")==null?"":thirdViewDataMap.get("RSK_ACQUISTION_COST_DC").toString());
					res.setAcquisitionCostOSOC(getShareVal(res.getAcquisitionCost().replaceAll(",", ""),req.getSharSign(),"share"));
					res.setAcquisitionCostOSDC(fm.formatter(getDesginationCountry(res.getAcquisitionCostOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					
					res.setCommissionQS(thirdViewDataMap.get("RSK_COMM_QUOTASHARE")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_COMM_QUOTASHARE").toString()));
					res.setCommissionsurp(thirdViewDataMap.get("RSK_COMM_SURPLUS")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_COMM_SURPLUS").toString()));
					res.setOverRidder(thirdViewDataMap.get("RSK_OVERRIDER_PERC")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_OVERRIDER_PERC").toString()));
					res.setManagementExpenses(thirdViewDataMap.get("RSK_MANAGEMENT_EXPENSES")==null?"":thirdViewDataMap.get("RSK_MANAGEMENT_EXPENSES").toString());
					res.setLossCF(thirdViewDataMap.get("RSK_LOSS_CARRYFORWARD")==null?"":thirdViewDataMap.get("RSK_LOSS_CARRYFORWARD").toString());
					res.setPremiumReserve(thirdViewDataMap.get("RSK_PREMIUM_RESERVE")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_PREMIUM_RESERVE").toString()));
					res.setLossreserve(thirdViewDataMap.get("RSK_LOSS_RESERVE")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_LOSS_RESERVE").toString()));
					res.setInterest(thirdViewDataMap.get("RSK_INTEREST")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_INTEREST").toString()));
					res.setPortfolioinoutPremium(thirdViewDataMap.get("RSK_PF_INOUT_PREM")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_PF_INOUT_PREM").toString()));
					res.setPortfolioinoutLoss(thirdViewDataMap.get("RSK_PF_INOUT_LOSS")==null?"":dropDowmImpl.formatterpercentage(thirdViewDataMap.get("RSK_PF_INOUT_LOSS").toString()));
					res.setLossAdvise(thirdViewDataMap.get("RSK_LOSSADVICE")==null?"":fm.formatter(thirdViewDataMap.get("RSK_LOSSADVICE").toString()));
					res.setCashLossLimit(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_OC").toString()));
					res.setCashLossLimitDc(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_CASHLOSS_LMT_DC").toString()));
					res.setAnualAggregateLiability(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_OC").toString()));
					res.setAnualAggregateLiabilityOSOC(getShareVal(res.getAnualAggregateLiability().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setAnualAggregateLiabilityOSDC(fm.formatter(getDesginationCountry(res.getAnualAggregateLiabilityOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setAnualAggregateLiabilityDc(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_LIAB_DC").toString()));
					res.setAnualAggregateDeduct(thirdViewDataMap.get("RSK_AGGREGATE_DEDUCT_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_DEDUCT_OC").toString()));
					res.setAnualAggregateDeductOSOC(getShareVal(res.getAnualAggregateDeduct().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setAnualAggregateDeductOSDC(fm.formatter(getDesginationCountry(res.getAnualAggregateDeductOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setAnualAggregateDeductDC(thirdViewDataMap.get("RSK_AGGREGATE_DEDUCT_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_AGGREGATE_DEDUCT_DC").toString()));
					res.setOcclimit(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_OC").toString()));
					res.setOcclimitOSOC(getShareVal(res.getOcclimit().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setOcclimitOSDC(fm.formatter(getDesginationCountry(res.getOcclimitOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setOcclimitDc(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_OCCURRENT_LIMIT_DC").toString()));
					res.setReinstNo(thirdViewDataMap.get("RSK_REINSTATE_NO")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_NO").toString()));
					res.setReinstAditionalPremiumpercent(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString()));
					res.setReinstAditionalPremiumpercentDc(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_DC").toString()));
					res.setLeaderUnderwriter(thirdViewDataMap.get("RSK_LEAD_UWID")==null?"":thirdViewDataMap.get("RSK_LEAD_UWID").toString());
					if(!"64".equalsIgnoreCase(res.getLeaderUnderwriter())){
					res.setLeaderUnderwriter(thirdViewDataMap.get("RSK_LEAD_UW")==null?"":thirdViewDataMap.get("RSK_LEAD_UW").toString());
					}else if("64".equalsIgnoreCase(res.getLeaderUnderwriter())){
						res.setLeaderUnderwriter("ITI Reinsurance Ltd");	
					}
					res.setLeaderUnderwritercountry(thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":thirdViewDataMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
					res.setLeaderUnderwritershare(thirdViewDataMap.get("RSK_LEAD_UW_SHARE")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_LEAD_UW_SHARE").toString()));					res.setAccounts(thirdViewDataMap.get("RSK_ACCOUNTS")==null?"":thirdViewDataMap.get("RSK_ACCOUNTS").toString());
					res.setExclusion(thirdViewDataMap.get("RSK_EXCLUSION")==null?"":thirdViewDataMap.get("RSK_EXCLUSION").toString());
					res.setRemarks(thirdViewDataMap.get("RSK_REMARKS")==null?"":thirdViewDataMap.get("RSK_REMARKS").toString());
					res.setUnderwriterRecommendations(thirdViewDataMap.get("RSK_UW_RECOMM")==null?"":thirdViewDataMap.get("RSK_UW_RECOMM").toString());
					res.setGmsApproval(thirdViewDataMap.get("RSK_GM_APPROVAL")==null?"":thirdViewDataMap.get("RSK_GM_APPROVAL").toString());
					res.setDecision(thirdViewDataMap.get("RSK_DECISION")==null?"":thirdViewDataMap.get("RSK_DECISION").toString()); 
					res.setOthercost(thirdViewDataMap.get("RSK_OTHER_COST")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_OTHER_COST").toString()));
					res.setReinstAdditionalPremium(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString()));
					res.setBurningCost(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					res.setCommissionQSAmt(thirdViewDataMap.get("COMM_QS_AMT")==null?"":fm.formatter(thirdViewDataMap.get("COMM_QS_AMT").toString()));
					res.setCommissionsurpAmt(thirdViewDataMap.get("COMM_SURPLUS_AMT")==null?"":fm.formatter(thirdViewDataMap.get("COMM_SURPLUS_AMT").toString()));
					res.setAcqCostPer(thirdViewDataMap.get("OTHER_ACQ_COST_AMT")==null?"":fm.formatter(thirdViewDataMap.get("OTHER_ACQ_COST_AMT").toString()));
					res.setProfitCommission(thirdViewDataMap.get("RSK_SHARE_PROFIT_COMMISSION")==null?"":thirdViewDataMap.get("RSK_SHARE_PROFIT_COMMISSION").toString());
					res.setReinstAditionalPremiumpercent(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC")==null?"":fm.formatter(thirdViewDataMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString()));
					res.setBurningCost(thirdViewDataMap.get("RSK_BURNING_COST_PCT")==null?"":fm.formatter(thirdViewDataMap.get("RSK_BURNING_COST_PCT").toString()));
					res.setBrokerage((thirdViewDataMap.get("RSK_BROKERAGE")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_BROKERAGE").toString())));
					res.setLimitPerVesselOC(thirdViewDataMap.get("LIMIT_PER_VESSEL_OC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_OC").toString()));
					res.setLimitPerVesselDC(thirdViewDataMap.get("LIMIT_PER_VESSEL_DC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_VESSEL_DC").toString()));
					res.setLimitPerVesselOSOC(getShareVal(res.getLimitPerVesselOC().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLimitPerVesselOSDC(fm.formatter(getDesginationCountry(res.getLimitPerVesselOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setLimitPerLocationOC(thirdViewDataMap.get("LIMIT_PER_LOCATION_OC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_OC").toString()));
					res.setLimitPerLocationDC(thirdViewDataMap.get("LIMIT_PER_LOCATION_DC")==null?"":fm.formatter(thirdViewDataMap.get("LIMIT_PER_LOCATION_DC").toString()));
					res.setLimitPerLocationOSOC(getShareVal(res.getLimitPerLocationOC().replaceAll(",", ""),res.getSharSign(),"share"));
					res.setLimitPerLocationOSDC(fm.formatter(getDesginationCountry(res.getLimitPerLocationOSOC().replaceAll(",",""),res.getExchRate()).toString()));
					res.setEndorsementDate(thirdViewDataMap.get("ENDT_DATE")==null?"":thirdViewDataMap.get("ENDT_DATE").toString());
					res.setReInstatementPremium(thirdViewDataMap.get("RSK_REINSTATEMENT_PREMIUM")==null?"":thirdViewDataMap.get("RSK_REINSTATEMENT_PREMIUM").toString());
					res.setCrestaStatus(thirdViewDataMap.get("RSK_CREASTA_STATUS")==null?"":thirdViewDataMap.get("RSK_CREASTA_STATUS").toString());
					res.setAcqBonus(thirdViewDataMap.get("RSK_BONUS_ID")==null?"":thirdViewDataMap.get("RSK_BONUS_ID").toString());
					res.setAcqBonusPercentage(thirdViewDataMap.get("RSK_NOCLAIMBONUS_PRCENT")==null?"":fm.formattereight(thirdViewDataMap.get("RSK_NOCLAIMBONUS_PRCENT").toString()));
					if("LCB".equalsIgnoreCase(res.getAcqBonus())){
						res.setAcqBonusName("Low Claim Bonus");
					}
					else if("NCB".equalsIgnoreCase(res.getAcqBonus())){
						res.setAcqBonusName("No Claim Bonus");
					}
				}
			}
			//GET_POSITION_MASTER_CON_MAP
			PositionMaster result = positionMasterRepository.findByProposalNoAndAmendId(new BigDecimal(req.getProposalNo()),new BigDecimal(req.getAmendId()));
			if(result!=null) {
				res.setContractListVal(result.getDataMapContNo()==null?"":result.getDataMapContNo().toString());
			}
			
			if(StringUtils.isNotBlank(res.getContractListVal()) && !"None".equalsIgnoreCase(res.getContractListVal())){
				//GET_MAPPING_PROPOSAL_NO
				List<Tuple> list1 =	nonProportCustomRepository.getMappingProposalNo(res.getContractListVal(),res.getLayerNo(),res.getDepartId(),res.getUwYear());
				List<MappingProposalRes> mapResList = new ArrayList<MappingProposalRes>();
				if(list1.size()>0){
					for(int i=0;i<list1.size();i++){
						Tuple map =list1.get(i);
						MappingProposalRes mapRes = new MappingProposalRes();
						mapRes.setMappingProposal(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
						mapRes.setMapingAmendId(map.get("AMEND_ID")==null?"":map.get("AMEND_ID").toString());
						mapResList.add(mapRes);
					}
					res.setMappingProposal(mapResList);
				}
				}
//			showRetroContracts(beanObj,pid,true);
//			getClassLimitDetails(beanObj);
//			GetRemarksDetails(beanObj);
		//	resList.add(res);
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	private String getShareVal(String lossAdvise, String signedShare, String type) {
		String res="";
		try{
			if(StringUtils.isBlank(lossAdvise)){
				lossAdvise="0";
			}
			if(StringUtils.isBlank(signedShare)){
				signedShare="0";
			}
			if("share".equalsIgnoreCase(type)){
			res=Double.toString((Double.parseDouble(lossAdvise)*Double.parseDouble(signedShare))/100);
			}
			res=fm.formatter(res);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public RiskDetailsEditModeRes riskDetailsEditMode(RiskDetailsEditModeReq req) {
		RiskDetailsEditModeRes response = new RiskDetailsEditModeRes();
		List<Tuple> list = new ArrayList<>();
		try {
			if (req.getContractMode()) {
				//risk.select.getEditModeDataPro3ContCond1
				list = nonProportCustomRepository.riskSelectGetEditModeDataPro3ContCond1(req.getContractNo());
			} else {
				//risk.select.getEditModeDataPro3ProCond1
				list = nonProportCustomRepository.riskSelectGetEditModeDataPro3ProCond1(req.getProposalNo());
			}
			RiskDetailsEditModeRes1 beanObj= new RiskDetailsEditModeRes1();
			Tuple resMap = null;
			if(list!=null && list.size()>0)
				resMap = list.get(0);
			if (resMap!=null) {
				String id=resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString();
				String productId=resMap.get("RSK_PRODUCTID")==null?"":resMap.get("RSK_PRODUCTID").toString();
				String branchcode=resMap.get("BRANCH_CODE")==null?"":resMap.get("BRANCH_CODE").toString();
				beanObj.setSubClass(dropDowmImpl.getSubClass(id,branchcode , productId));
				beanObj.setContractListVal(resMap.get("DATA_MAP_CONT_NO")==null?"":resMap.get("DATA_MAP_CONT_NO").toString());
				beanObj.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
			//	beanObj.setBaseLayer(resMap.get("BASE_LAYER")==null?"":resMap.get("BASE_LAYER").toString());
				
				beanObj.setEndorsmentNo(resMap.get("RSK_ENDORSEMENT_NO")==null?"":resMap.get("RSK_ENDORSEMENT_NO").toString());
				beanObj.setContractNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				beanObj.setLayerNo(resMap.get("RSK_LAYER_NO")==null?"":resMap.get("RSK_LAYER_NO").toString());
				beanObj.setProductId(resMap.get("RSK_PRODUCTID")==null?"":resMap.get("RSK_PRODUCTID").toString());
				beanObj.setDepartmentId(resMap.get("RSK_DEPTID")==null?"":resMap.get("RSK_DEPTID").toString());
				beanObj.setProfitCenter(resMap.get("RSK_PFCID")==null?"":resMap.get("RSK_PFCID").toString());
				beanObj.setSubProfitcenter(resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString());
				beanObj.setPolicyBranch(resMap.get("RSK_POLBRANCH")==null?"":resMap.get("RSK_POLBRANCH").toString());
				beanObj.setCedingCo(resMap.get("RSK_CEDINGID")==null?"":resMap.get("RSK_CEDINGID").toString());
				beanObj.setBroker(resMap.get("RSK_BROKERID")==null?"":resMap.get("RSK_BROKERID").toString());
				beanObj.setTreatyNametype(resMap.get("RSK_TREATYID")==null?"":resMap.get("RSK_TREATYID").toString());
				beanObj.setMonth(resMap.get("RSK_MONTH")==null?"":resMap.get("RSK_MONTH").toString());
				beanObj.setUwYear(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				//RI
				beanObj.setUwYearTo(resMap.get("UW_YEAR_TO")==null?"":resMap.get("UW_YEAR_TO").toString());
				beanObj.setBouquetModeYN(resMap.get("BOUQUET_MODE_YN")==null?"N":resMap.get("BOUQUET_MODE_YN").toString());
				beanObj.setBouquetNo(resMap.get("BOUQUET_NO")==null?"":resMap.get("BOUQUET_NO").toString());
				beanObj.setOfferNo(resMap.get("OFFER_NO")==null?"":resMap.get("OFFER_NO").toString());
				beanObj.setUnderwriter(resMap.get("RSK_UNDERWRITTER")==null?"":resMap.get("RSK_UNDERWRITTER").toString());
				if(!"Layer".equalsIgnoreCase(req.getProposalReference())){
					if(StringUtils.isBlank(beanObj.getBaseLayer()))
				beanObj.setBaseLayer(resMap.get("BASE_LAYER")==null?"":resMap.get("BASE_LAYER").toString());
				}
				beanObj.setInceptionDate(resMap.get("RSK_INCEPTION_DATE")==null?"":DateFormat(resMap.get("RSK_INCEPTION_DATE")));
				beanObj.setExpiryDate(resMap.get("RSK_EXPIRY_DATE")==null?"":DateFormat(resMap.get("RSK_EXPIRY_DATE")));
				beanObj.setAcceptanceDate(resMap.get("RSK_ACCOUNT_DATE")==null?"":resMap.get("RSK_ACCOUNT_DATE").toString());
				
				beanObj.setOrginalCurrency(resMap.get("RSK_ORIGINAL_CURR")==null?"":resMap.get("RSK_ORIGINAL_CURR").toString());

				if (resMap.get("RSK_EXCHANGE_RATE") != null) {
					beanObj.setExchangeRate(resMap.get("RSK_EXCHANGE_RATE").toString().equalsIgnoreCase("0") ? "0":resMap.get("RSK_EXCHANGE_RATE").toString());
				}
				if (resMap.get("RSK_BASIS") != null) {
					beanObj.setBasis(resMap.get("RSK_BASIS").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_BASIS").toString());
				}
				beanObj.setTerritoryscope(resMap.get("RSK_TERRITORY_SCOPE")==null?"":resMap.get("RSK_TERRITORY_SCOPE").toString());
				beanObj.setTerritory(resMap.get("RSK_TERRITORY")==null?"":resMap.get("RSK_TERRITORY").toString());
				beanObj.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
				if (resMap.get("RSK_LIMIT_OC") != null) {
					beanObj.setLimitOrigCur(resMap.get("RSK_LIMIT_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_LIMIT_OC").toString());
				}
				if (resMap.get("RSK_EPI_EST_OC") != null) {
					beanObj.setEpi(resMap.get("RSK_EPI_EST_OC").toString().equalsIgnoreCase("0") ? "0.00":fm.formatter(resMap.get("RSK_EPI_EST_OC").toString()));
				}
				if (resMap.get("RSK_SHARE_WRITTEN") != null) {
					beanObj.setShareWritten(resMap.get("RSK_SHARE_WRITTEN").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_SHARE_WRITTEN").toString());
				}
				if (resMap.get("RSK_SHARE_SIGNED") != null) {
					beanObj.setSharSign(resMap.get("RSK_SHARE_SIGNED").toString().equalsIgnoreCase("0") ? "" :resMap.get("RSK_SHARE_SIGNED").toString());
				}
				if (resMap.get("RSK_MAX_LMT_COVER") != null) {
					beanObj.setMaxLimitProduct(resMap.get("RSK_MAX_LMT_COVER").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_MAX_LMT_COVER").toString());
				}
				if (resMap.get("RSK_SUBJ_PREMIUM_OC") != null) {
					beanObj.setSubPremium(resMap.get("RSK_SUBJ_PREMIUM_OC").toString().equalsIgnoreCase("0") ? "0.00":fm.formatter(resMap.get("RSK_SUBJ_PREMIUM_OC").toString()));
				}
				if (resMap.get("RSK_XLPREM_OC") != null) {
					beanObj.setXlPremium(resMap.get("RSK_XLPREM_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_XLPREM_OC").toString());
				}
				if (resMap.get("RSK_DEDUC_OC") != null) {
					beanObj.setDeduchunPercent(resMap.get("RSK_DEDUC_OC").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_DEDUC_OC").toString());
				}
				if (resMap.get("RSK_MD_PREM_OC") != null) {
					beanObj.setMdPremium(resMap.get("RSK_MD_PREM_OC").toString().equalsIgnoreCase("0") ? "0.00":fm.formatter(resMap.get("RSK_MD_PREM_OC").toString()));
				}
				if (resMap.get("RSK_ADJRATE") != null) {
					beanObj.setAdjRate(resMap.get("RSK_ADJRATE").toString().equalsIgnoreCase("0") ? "0.00000000": fm.formattereight(resMap.get("RSK_ADJRATE").toString()));
				}
				if (resMap.get("RSK_PF_COVERED") != null) {
					beanObj.setPortfoloCovered(resMap.get("RSK_PF_COVERED").toString().equalsIgnoreCase("0") ? "0": resMap.get("RSK_PF_COVERED").toString());
				}
				beanObj.setMdInstalmentNumber(resMap.get("MND_INSTALLMENTS")==null?"":resMap.get("MND_INSTALLMENTS").toString());
				if("5".equalsIgnoreCase(beanObj.getProductId())){
					beanObj.setNoRetroCess(resMap.get("RETRO_CESSIONARIES").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RETRO_CESSIONARIES").toString());
				}
					beanObj.setSpRetro(resMap.get("RSK_SP_RETRO")==null ? "0" : resMap.get("RSK_SP_RETRO").toString());
					beanObj.setNoInsurer(resMap.get("RSK_NO_OF_INSURERS")==null ? "" : resMap.get("RSK_NO_OF_INSURERS").toString());
					beanObj.setLimitPerVesselOC((resMap.get("LIMIT_PER_VESSEL_OC")==null||"0".equals(resMap.get("LIMIT_PER_VESSEL_OC"))?"":resMap.get("LIMIT_PER_VESSEL_OC").toString()));
					beanObj.setLimitPerLocationOC((resMap.get("LIMIT_PER_LOCATION_OC")==null||"0".equals(resMap.get("LIMIT_PER_LOCATION_OC"))?"":resMap.get("LIMIT_PER_LOCATION_OC").toString()));
					beanObj.setEgnpiOffer((resMap.get("EGPNI_AS_OFFER")==null||"0".equals(resMap.get("EGPNI_AS_OFFER"))?"":resMap.get("EGPNI_AS_OFFER").toString()));
					beanObj.setEgnpiOfferDC((resMap.get("EGPNI_AS_OFFER_DC")==null||"0".equals(resMap.get("EGPNI_AS_OFFER_DC"))?"":resMap.get("EGPNI_AS_OFFER_DC").toString()));
					beanObj.setOurAssessment((resMap.get("OURASSESSMENT")==null||"0".equals(resMap.get("OURASSESSMENT"))?"":resMap.get("OURASSESSMENT").toString()));
					
					beanObj.setInwardType(resMap.get("INWARD_BUS_TYPE")==null ? "" : resMap.get("INWARD_BUS_TYPE").toString());
					beanObj.setBusinessType(resMap.get("RSK_BUSINESS_TYPE")==null ? "" : resMap.get("RSK_BUSINESS_TYPE").toString());
					beanObj.setExchangeType(resMap.get("RSK_EXCHANGE_TYPE")==null ? "" : resMap.get("RSK_EXCHANGE_TYPE").toString());
					beanObj.setPerilCovered(resMap.get("RSK_PERILS_COVERED")==null ? "" : resMap.get("RSK_PERILS_COVERED").toString());
					beanObj.setLOCIssued(resMap.get("RSK_LOC_ISSUED")==null ? "" : resMap.get("RSK_LOC_ISSUED").toString());
					beanObj.setUmbrellaXL(resMap.get("RSK_UMBRELLA_XL")==null ? "" : resMap.get("RSK_UMBRELLA_XL").toString());
					beanObj.setEventlimit((resMap.get("RSK_EVENT_LIMIT_OC")==null||"0".equals(resMap.get("RSK_EVENT_LIMIT_OC"))?"":resMap.get("RSK_EVENT_LIMIT_OC").toString()));
					beanObj.setCoverLimitXL((resMap.get("RSK_COVER_LIMIT_UXL_OC")==null||"0".equals(resMap.get("RSK_COVER_LIMIT_UXL_OC"))?"":resMap.get("RSK_COVER_LIMIT_UXL_OC").toString()));
					beanObj.setDeductLimitXL((resMap.get("RSK_DEDUCTABLE_UXL_OC")==null||"0".equals(resMap.get("RSK_DEDUCTABLE_UXL_OC"))?"":resMap.get("RSK_DEDUCTABLE_UXL_OC").toString()));
					beanObj.setPml(resMap.get("RSK_PML")==null ? "" : resMap.get("RSK_PML").toString());
					beanObj.setPmlPercent(resMap.get("RSK_PML_PERCENT")==null ? "" : resMap.get("RSK_PML_PERCENT").toString());
					beanObj.setEgnpipml((resMap.get("RSK_EGNPI_PML_OC")==null||"0".equals(resMap.get("RSK_EGNPI_PML_OC"))?"":resMap.get("RSK_EGNPI_PML_OC").toString()));
					beanObj.setPremiumbasis(resMap.get("RSK_PREMIUM_BASIS")==null ? "" : resMap.get("RSK_PREMIUM_BASIS").toString());
					beanObj.setMinimumRate(resMap.get("RSK_MINIMUM_RATE")==null ? "" :fm.formattereight(resMap.get("RSK_MINIMUM_RATE").toString()));
					beanObj.setMaximumRate(resMap.get("RSK_MAXIIMUM_RATE")==null ? "" : fm.formattereight(resMap.get("RSK_MAXIIMUM_RATE").toString()));
					beanObj.setBurningCostLF(resMap.get("RSK_BURNING_COST_LF")==null ? "" : resMap.get("RSK_BURNING_COST_LF").toString());
					beanObj.setMinPremium((resMap.get("RSK_MINIMUM_PREMIUM_OC")==null ||"0".equals(resMap.get("RSK_MINIMUM_PREMIUM_OC"))?"":fm.formatter(resMap.get("RSK_MINIMUM_PREMIUM_OC").toString())));
					beanObj.setPaymentDuedays(resMap.get("RSK_PAYMENT_DUE_DAYS")==null ? "" : resMap.get("RSK_PAYMENT_DUE_DAYS").toString());
					beanObj.setCountryIncludedList(resMap.get("COUNTRIES_INCLUDE")==null ? "" : resMap.get("COUNTRIES_INCLUDE").toString());
					beanObj.setCountryExcludedList(resMap.get("COUNTRIES_EXCLUDE")==null ? "" : resMap.get("COUNTRIES_EXCLUDE").toString());
					beanObj.setLocBankName(resMap.get("RSK_LOC_BNK_NAME")==null?"":resMap.get("RSK_LOC_BNK_NAME").toString());
					beanObj.setLocCreditPrd(resMap.get("RSK_LOC_CRDT_PRD")==null?"":resMap.get("RSK_LOC_CRDT_PRD").toString());
					beanObj.setLocCreditAmt(resMap.get("RSK_LOC_CRDT_AMT")==null?"":fm.formatter(resMap.get("RSK_LOC_CRDT_AMT").toString()));
					beanObj.setLocBeneficerName(resMap.get("RSK_LOC_BENFCRE_NAME")==null?"":resMap.get("RSK_LOC_BENFCRE_NAME").toString());
					
				beanObj.setEndorsmenttype(resMap.get("RS_ENDORSEMENT_TYPE")==null?"":resMap.get("RS_ENDORSEMENT_TYPE").toString());
				beanObj.setRenewalcontractNo(resMap.get("OLD_CONTRACTNO")==null?"":resMap.get("OLD_CONTRACTNO").toString());
				beanObj.setBaseLoginId(resMap.get("LOGIN_ID")==null?"":resMap.get("LOGIN_ID").toString());
				beanObj.setPaymentPartner(resMap.get("PAYMENT_PARTNER")==null?"":resMap.get("PAYMENT_PARTNER").toString());
				beanObj.setRateOnLine(resMap.get("RATE_ON_LINE")==null?"":fm.formattereight(resMap.get("RATE_ON_LINE").toString()));
				beanObj.setRiskdetailYN(resMap.get("RISK_DET_YN")==null?"N":resMap.get("RISK_DET_YN").toString());
				beanObj.setBrokerdetYN(resMap.get("BROKER_DET_YN")==null?"N":resMap.get("BROKER_DET_YN").toString());
				beanObj.setPremiumdetailYN(resMap.get("PREMIUM_DET_YN")==null?"N":resMap.get("PREMIUM_DET_YN").toString());
				beanObj.setInstallYN(resMap.get("INTALL_DET_YN")==null?"N":resMap.get("INTALL_DET_YN").toString());
				beanObj.setAcqdetailYN(resMap.get("ACQCOST_DET_YN")==null?"N":resMap.get("ACQCOST_DET_YN").toString());
				beanObj.setReinstdetailYN(resMap.get("REINST_DET_YN")==null?"N":resMap.get("REINST_DET_YN").toString());
				
				beanObj.setNewLayerNo(resMap.get("NEW_LAYER_NO")==null?"":resMap.get("NEW_LAYER_NO").toString());
				beanObj.setMinimumpremiumPercent(resMap.get("RSK_MINIMUMPREMIUM_PERCENT")==null?"0.0000":fm.formatterfourNoComma(resMap.get("RSK_MINIMUMPREMIUM_PERCENT").toString()));
				beanObj.setGnpiCapPercent(resMap.get("RSK_GNPI_CAP_PERCENT")==null?"0.0000":fm.formatterfourNoComma(resMap.get("RSK_GNPI_CAP_PERCENT").toString()));	
			}

			if ("3".equalsIgnoreCase(req.getProductId())){
				if(StringUtils.isNotBlank(req.getContractNo())&&!"0".equals(req.getContractNo())){
					beanObj.setPrclFlag(dropDowmImpl.getPLCLCountStatus(req.getContractNo(), req.getLayerNo()));
				}else{
					beanObj.setPrclFlag(false);
				}
			}
			beanObj.setAmendId(dropDowmImpl.getRiskComMaxAmendId(req.getProposalNo()));
			//getClassLimitDetails(beanObj);
		//	GetRemarksDetails(beanObj);
			String proposalno=""; //RI
			if (StringUtils.isNotEmpty(req.getLayerProposalNo())) {
				proposalno = req.getLayerProposalNo();
			} else {
				proposalno = beanObj.getProposalNo();
			}
			
			int count = ttrnRipRepository.countByProposalNo(req.getProposalNo());
			beanObj.setTotalNoOfRows(String.valueOf(count));
			
			
	//		this.showSecondpageEditItems(beanObj, beanObj.getProductId(), proposalno);
			response.setCommonResponse(beanObj);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
//	public String GetRiskDetailsEditQueryProduct3(boolean contractMode) {
//		String selectQry ="risk.select.getEditModeDataPro3";
//		if(contractMode)
//			selectQry = "risk.select.getEditModeDataPro3ContCond1";
//		else
//			selectQry ="risk.select.getEditModeDataPro3ProCond1";
//
//		return selectQry;
//	}

	@Override
	public ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req) {
		ShowSecondPageDataRes response = new ShowSecondPageDataRes();
		ShowSecondPageDataRes1 res = new ShowSecondPageDataRes1();
		try{
			//risk.select.getSecPageData 
			List<Tuple> list =	nonProportCustomRepository.riskSelectGetSecPageData(req.getProposalNo(),req.getBranchCode(),req.getProductId());
			Tuple resMap = null;
			if(list!=null && list.size()>0)
				resMap = list.get(0);
			if(resMap!=null){
				res.setProposalNo(resMap.get("RSK_PROPOSAL_NUMBER")==null?"":resMap.get("RSK_PROPOSAL_NUMBER").toString());
				//select RTRIM(XMLAGG(XMLELEMEN 
				String id = resMap.get("RSK_SPFCID")==null?"":resMap.get("RSK_SPFCID").toString();
				res.setSubProfitcenter(dropDowmImpl.getSubClass(id, req.getBranchCode(), req.getProductId()));
			
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setMonth(resMap.get("MONTH")==null?"":resMap.get("MONTH").toString());
				res.setUnderwriter(resMap.get("RSK_UWYEAR")==null?"":resMap.get("RSK_UWYEAR").toString());
				res.setPolicyBranch(resMap.get("TMAS_POL_BRANCH_NAME")==null?"":resMap.get("TMAS_POL_BRANCH_NAME").toString());
				res.setDepartClass(resMap.get("TMAS_DEPARTMENT_NAME")==null?"":resMap.get("TMAS_DEPARTMENT_NAME").toString());
			
				//risk.select.CEASE_STATUS
				String ceaseStatus = nonProportCustomRepository.riskSelectCeaseStatus(req.getProposalNo());
				res.setCeaseStatus(ceaseStatus==null?"":ceaseStatus);
				res.setEndttypename(resMap.get("DETAIL_NAME")==null?"":resMap.get("DETAIL_NAME").toString());
			}
			response.setCommonResponse(res);
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
	public GetRetroContractDetailsListRes getRetroContractDetailsList(GetRetroContractDetailsListReq req, int flag, String UWYear) {
		GetRetroContractDetailsListRes response  = new GetRetroContractDetailsListRes();
		List<GetRetroContractDetailsListRes1> resList = new ArrayList<GetRetroContractDetailsListRes1>();
		List<Tuple> list= new ArrayList<>();
		try{
			if(flag==1)	{
				if("4".equals(req.getProductid())){
					//fac.select.uwYear
					list= nonProportCustomRepository.facSelectUwYear(req.getProductid(),req.getIncepDate(),req.getBranchCode());
				}else{
					//risk.select.uwYear
					list= nonProportCustomRepository.riskSelectUwYear(req.getProductid(),req.getIncepDate(),req.getBranchCode());
				}
			}
			else if(StringUtils.isNotEmpty(UWYear)&&flag==2){
				if(StringUtils.isNotBlank(req.getRetroType()) && "TR".equals(req.getRetroType()) && "4".equals(req.getProductid())){
					//fac.select.retroContDetTR
					list= nonProportCustomRepository.facSelectRetroContDetTR(req.getProductid(),UWYear,req.getIncepDate(),req.getBranchCode());
				}else{
					//fac.select.retroContDet
					list= nonProportCustomRepository.facSelectRetroContDet(req.getProductid(),req.getRetroType(),UWYear,req.getIncepDate(),req.getBranchCode());
				}
			}
			else if(StringUtils.isNotEmpty(UWYear)&&flag==3){
				//FAC_SELECT_RETRO_DUP_CONTRACT
				list= nonProportCustomRepository.facSelectRetroDupContract(UWYear,req.getIncepDate(),req.getBranchCode());
			}
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					GetRetroContractDetailsListRes1 res = new GetRetroContractDetailsListRes1();
					Tuple insMap = list.get(i);
					res.setCONTDET1(insMap.get("CONTDET1")==null?"":insMap.get("CONTDET1").toString());
					res.setCONTDET2(insMap.get("CONTDET1")==null?"":insMap.get("CONTDET1").toString());
					resList.add(res);					
					}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
	 }	}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	
	public SaveSecondPageRes saveSecondPage(insertProportionalTreatyReq req) {
		SaveSecondPageRes response = new SaveSecondPageRes();
		SaveSecondPageRes1 beanObj = new SaveSecondPageRes1();
		try{
			int ChkSecPagMod = checkSecondPageMode(req.getProductId(), req.getProposalNo());
			String[] obj=null,obj1=null;
			if (ChkSecPagMod == 2) {
				obj = saveUpdateRiskDetailsSecondForm(req,getMaxAmednId(req.getProposalNo()));
				int res=0;
				//risk.update.pro35RskProposal
				TtrnRiskProposal update = nonProportCustomRepository.riskUpdatePro35RskProposal(obj);
				if(update!=null) {
				ttrnRiskProposalRepository.saveAndFlush(update);	
				res=1;
				}
				//risk.select.maxRskStatus
				 String GetProposalStatus = nonProportCustomRepository.riskSelectMaxRskStatus(obj[6]);
			
				beanObj.setProStatus(GetProposalStatus);
				if(StringUtils.isNotBlank(req.getContractNo())) {
					beanObj.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ req.getProposalNo());
				}
				else if ("A".equalsIgnoreCase(GetProposalStatus)||"P".equalsIgnoreCase(GetProposalStatus)) {
					beanObj.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ obj[6]  + " and New Layer No : "	+ req.getNewLayerNo());
				}else if ("N".equalsIgnoreCase(GetProposalStatus)) {
					beanObj.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ obj[6]  + " and New Layer No : "	+ req.getNewLayerNo());
				}
				obj1 = savemodeUpdateRiskDetailsSecondFormSecondTable(req);
				if("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())) {
					//risk.update.pro3SecComm
					TtrnRiskCommission update1 = nonProportCustomRepository.riskUpdatePro3SecComm(obj1);
					if(update1!=null) {
						ttrnRiskCommissionRepository.saveAndFlush(update1);	
					}
				}
//				else if("5".equalsIgnoreCase(req.getProductId())) {
//					//risk.update.pro5SecComm
//					TtrnRiskCommission update1 = nonProportCustomRepository.riskUpdatePro5SecComm(obj1);
//					if(update1!=null) {
//						ttrnRiskCommissionRepository.saveAndFlush(update1);	
//					} }
			
				
			}else {
				obj = secondPageFirstTableSaveAruguments(req,  getMaxAmednId(req.getProposalNo()));
				int res=0;
				//risk.update.pro35RskProposal
				TtrnRiskProposal update = nonProportCustomRepository.riskUpdatePro35RskProposal(obj);
				if(update!=null) {
				ttrnRiskProposalRepository.saveAndFlush(update);	
				res=1;
				}
				
				obj1 = secondPageCommissionSaveAruguments(req);
				if("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())) {
					//risk.insert.pro3SecComm
					TtrnRiskCommission insert = nonProportCustomRepository.riskInsertPro3SecComm(obj1);
					if(insert!=null) {
						ttrnRiskCommissionRepository.saveAndFlush(insert);	
					}
				}
//				else if("5".equalsIgnoreCase(req.getProductId())) {
//					//risk.insert.pro5SecComm
//					TtrnRiskCommission insert = nonProportCustomRepository.riskInsertPro5SecComm(obj1);
//					if(insert!=null) {
//						ttrnRiskCommissionRepository.saveAndFlush(insert);	
//					}
//				}
				
				//risk.select.chechProposalStatus
				List<Tuple> list1 = nonProportCustomRepository.riskSelectChechProposalStatus(req.getProposalNo());
			
				if(!CollectionUtils.isEmpty(list1)) {
					Tuple resMap=list1.get(0);
					beanObj.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
					beanObj.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
					beanObj.setContractNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
				}
				final String HomePosition = getproposalStatus(req.getProposalNo());
				beanObj.setProStatus(HomePosition);
				if (HomePosition.equalsIgnoreCase("P")) {
					beanObj.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo() + " and New Layer No : "	+ req.getNewLayerNo());

				} else if (HomePosition.equalsIgnoreCase("A")) {						
					beanObj.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo() + " and New Layer No : "	+ req.getNewLayerNo());

				} else if (HomePosition.equalsIgnoreCase("R")) {
					beanObj.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalNo() + " and New Layer No : "	+ req.getNewLayerNo());
				}else if (HomePosition.equalsIgnoreCase("N")) {
					beanObj.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalNo() + " and New Layer No : "	+ req.getNewLayerNo());
				} else if (HomePosition.equalsIgnoreCase("0")) {
					beanObj.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "	+ req.getProposalNo() + " and New Layer No : " + req.getNewLayerNo());
				}
			}
//			instalMentPremium(req);
//			if("3".equalsIgnoreCase(productId)){
//				insertRetroContracts(beanObj,productId);
//			}
//			if  ("5".equals(productId)){
//				insertRetroCess(beanObj);
//			}
//			reInstatementMainInsert(req);
//			insertCrestaMaintable(beanObj);
//			insertBonusDetails(beanObj);
//			InsertRemarkDetails(beanObj);
			response.setCommonResponse(beanObj);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public int checkSecondPageMode(String productId,String proposalNo) {
		int mode=0;
		int selectCount =0;
		try{
			if("5".equalsIgnoreCase(productId)){
				//risk.select.getRetroCessCount
				selectCount = ttrnRetroCessionaryRepository.countByProposalNo(proposalNo);
			}else{
				//risk.select.getRiskCommCount
				 selectCount = ttrnRiskCommissionRepository.countByRskProposalNumber(proposalNo);
			}
			
			if (selectCount == 0) {
				mode = 1;
			} else if (selectCount != 0) {
				mode = 2;
			}
		}catch(Exception e){
			e.printStackTrace();;
		}
	return mode;
	}

	private insertProportionalTreatyRes insertRiskProposal (insertProportionalTreatyReq req,boolean ChkSavFlg) {
		insertProportionalTreatyRes response = new insertProportionalTreatyRes();
		boolean InsertFlag = false;
		try {
			String[] args = null;
			String maxAmendId="0";
			if(!"0".endsWith(req.getAmendId()))
				maxAmendId=(Integer.parseInt(req.getAmendId())-1)+"";
			if (req.isSaveFlag()) {
				if (ChkSavFlg) {
					//risk.select.maxEndom
					TtrnRiskDetails list = ttrnRiskDetailsRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(req.getProposalNo());
					if(list!=null) {
					req.setEndNo(list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString());
					}
					args = getProposalSaveEditModeQuery(req);
					//risk.update.pro35FirPageRskPro
					int	res = nonProportCustomRepository.riskUpdatePro35FirPageRskPro(args);
					if (res > 0) {
						/*
						 * if(StringUtils.isNotBlank(req.getContNo())) { response.
						 * setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "
						 * + req.getProposalno()); } else if (req.getProStatus().equalsIgnoreCase("A")
						 * || req.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(req.
						 * getProStatus())) { response.
						 * setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "
						 * + req.getProposalno()); }else if(req.getProStatus().equalsIgnoreCase("N")){
						 * response.
						 * setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "
						 * + req.getProposalno()); }else if(req.getProStatus().equalsIgnoreCase("R")){
						 * response.
						 * setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "
						 * + req.getProposalno()); }
						 */
					}
					int count =0;
					updateFirstPageFields(req, getMaxAmednIdPro(req.getProposalNo()));
					args = updateHomePositionMasterAruguments(req,maxAmendId);
					//risk.update.positionMaster
					PositionMaster update1 = nonProportCustomRepository.positionMasterUpdate(args);
					if(update1!=null) {
						positionMasterRepository.saveAndFlush(update1);	
						count=1;
					}
					if (count > 0) {
						if(StringUtils.isNotBlank(req.getContNo())) {
							response.setContractGendration("Your Proposal is saved in Endorsement with Proposal No : "+ req.getProposalNo());
						}
						else if (req.getProStatus().equalsIgnoreCase("A") || req.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(req.getProStatus())) {
							response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ req.getProposalNo()+" and New Layer No : "+req.getNewLayerNo());
						}else if(req.getProStatus().equalsIgnoreCase("N")){
							response.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalNo()+" and New Layer No : "+req.getNewLayerNo());
						}else if(req.getProStatus().equalsIgnoreCase("R")){
							response.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalNo() +" and New Layer No : "+req.getNewLayerNo());
						}
					}
				} else {
					int res = 1;
					args = getFirstPageSecondTableAruguments(req);
					//risk.insert.pro35RskProposal
					TtrnRiskProposal insert = nonProportCustomRepository.ttrnRiskProposalInsert(args);
					if(insert!=null) {
						ttrnRiskProposalRepository.saveAndFlush(insert);	
						res = 1;
					}
					updateFirstPageFields(req, getMaxAmednIdPro(req.getProposalNo()));
					String renewalStatus = getRenewalStatus(req);
					args = insertHomePositionMasterAruguments(req);
					//risk.insert.positionMaster
					PositionMaster insert1 = nonProportCustomRepository.positionMasterInsert(args);
					if(insert1!=null) {
						positionMasterRepository.saveAndFlush(insert1);	
						res=1;
					}
					if (req.getProStatus().equalsIgnoreCase("A") || req.getProStatus().equalsIgnoreCase("P")||"0".equalsIgnoreCase(req.getProStatus())) {
						response.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ req.getProposalNo()+" and New Layer No : "+req.getNewLayerNo());
					}else if(req.getProStatus().equalsIgnoreCase("N")){
						response.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ req.getProposalNo()+" and New Layer No : "+req.getNewLayerNo());
					}else if(req.getProStatus().equalsIgnoreCase("R")){
						response.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalNo() +" and New Layer No : "+req.getNewLayerNo());
				}
			}
		}
			 else {
				 int res = 0;
				 if (!ChkSavFlg) {
					if(maxAmendId.equalsIgnoreCase(req.getAmendId())){
						
						args = getFirstPageSecondTableInsertAruguments(req);
						//risk.insert.pro35RskProposal
						TtrnRiskProposal insert = nonProportCustomRepository.ttrnRiskProposalInsert(args);
						if(insert!=null) {
							ttrnRiskProposalRepository.saveAndFlush(insert);	
							res = 1;
						}
					}else{
						//risk.update.pro35FirPageRskPro
						res = nonProportCustomRepository.riskUpdatePro35FirPageRskPro(args);
					}
					if (res > 0) {
						//InsertFlag = true;
					}
					
					updateFirstPageFields(req, getMaxAmednIdPro(req.getProposalNo()));
					int insertCount = 0;
					if(maxAmendId.equalsIgnoreCase(req.getAmendId())){
						String renewalStatus = getRenewalStatus(req);
						args = insertHomePositionMasterAruguments(req);
						//risk.insert.positionMaster
						PositionMaster insert1 = nonProportCustomRepository.positionMasterInsert(args);
						if(insert1!=null) {
							positionMasterRepository.saveAndFlush(insert1);	
							insertCount=1;
						}
						
					}	else{
						
						args = updateHomePositionMasterAruguments(req,maxAmendId);
						//risk.update.positionMaster
						PositionMaster update1 = nonProportCustomRepository.positionMasterUpdate(args);
						if(update1!=null) {
							positionMasterRepository.saveAndFlush(update1);	
							insertCount=1;
						}
					}
				//	int insertCount = queryImpl.updateQuery(query, args);
					if (insertCount > 0){
						//InsertFlag = true;
					}
					if (req.getProStatus().equalsIgnoreCase("R")) {
						response.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ req.getProposalNo());
					}
					String proposalno="";
					if (StringUtils.isNotEmpty(req.getLayerProposalNo())) {
						proposalno = req.getLayerProposalNo();
					} else {
						proposalno = req.getProposalNo();
						}
//					this.showSecondpageEditItems(beanObj, pid, proposalno);
				}
			}
		response.setProposalno(req.getProposalNo());
		response.setMessage("Success");
		response.setIsError(false);
	} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		response.setMessage("Failed");
		response.setIsError(true);
	}
		return response;
	}
	
	private String[] getFirstPageSecondTableInsertAruguments(insertProportionalTreatyReq req) {

		String[] arg=null;
		String arg2 = null;
		arg = new String[39];
		if (req.getAmendStatus()) {
			arg[0] = req.getProposalNo();
			arg[1] = arg2;
		} else {
			arg[0] = req.getProposalNo();
			arg[1] = "0";
		}
		arg[3] = StringUtils.isEmpty(req.getLimitOrigCur()) ? "0" : req.getLimitOrigCur().replaceAll(",", "");
		arg[4] = StringUtils.isEmpty(req.getLimitOrigCur())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getLimitOrigCur().replaceAll(",", ""), req.getExchangeRate());
		arg[2] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
		arg[5] = StringUtils.isEmpty(req.getXlPremium()) ? "0" : req.getXlPremium();
		arg[6] = StringUtils.isEmpty(req.getXlPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getXlPremium(), req.getExchangeRate());
		arg[7] = StringUtils.isEmpty(req.getDeduchunPercent()) ? "0" : req.getDeduchunPercent();
		arg[10] = StringUtils.isEmpty(req.getMdPremium()) ? "0" : req.getMdPremium();
		arg[11] = StringUtils.isEmpty(req.getMdPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getMdPremium(), req.getExchangeRate());
		arg[14] = StringUtils.isEmpty(req.getAdjRate()) ? "0" : req.getAdjRate();
		arg[15] = StringUtils.isEmpty(req.getPortfoloCovered()) ? "0" : req.getPortfoloCovered();
		arg[16] = StringUtils.isEmpty(req.getSubPremium()) ? "0" : req.getSubPremium();
		arg[17] = StringUtils.isEmpty(req.getSubPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getSubPremium(), req.getExchangeRate());
		arg[18] = StringUtils.isEmpty(req.getMaxLimitProduct()) ? "0" : req.getMaxLimitProduct();
		arg[19] = StringUtils.isEmpty(req.getDeduchunPercent()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getDeduchunPercent(), req.getExchangeRate());
		arg[20] = StringUtils.isEmpty(req.getSpRetro()) ? "" : req.getSpRetro();
		arg[21] = StringUtils.isEmpty(req.getNoInsurer()) ? "0" : req.getNoInsurer();
		arg[8] = StringUtils.isEmpty(req.getEpi()) ? "0" : req.getEpi();
		arg[9] = StringUtils.isEmpty(req.getEpi()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEpi(), req.getExchangeRate());
		arg[12] = StringUtils.isEmpty(req.getShareWritt()) ? "0" : req.getShareWritt();
		if (req.getProStatus().equalsIgnoreCase("P")) {
			arg[13] = "0";
		} else if (req.getProStatus().equalsIgnoreCase("A")) {
			arg[13] = StringUtils.isEmpty(req.getSharSign()) ? "0" : req.getSharSign();
		} else if (req.getProStatus().equalsIgnoreCase("R")) {
			arg[13] = "0";
		} else {
			arg[13] = "0";
		}
		arg[22] = StringUtils.isEmpty(req.getLimitOurShare()) ? "0" : req.getLimitOurShare();
		arg[23] = StringUtils.isEmpty(req.getLimitOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitOurShare(), req.getExchangeRate());
		arg[24] = StringUtils.isEmpty(req.getEpiAsPerShare()) ? "0" : req.getEpiAsPerShare();
		arg[25] = StringUtils.isEmpty(req.getEpiAsPerShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEpiAsPerShare(), req.getExchangeRate());
		arg[26] = StringUtils.isEmpty(req.getMdpremiumourservice()) ? "0" : req.getMdpremiumourservice();
		arg[27] = StringUtils.isEmpty(req.getMdpremiumourservice()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getMdpremiumourservice(), req.getExchangeRate());
		arg[28] = StringUtils.isEmpty(req.getLimitPerVesselOC()) ? "0" : req.getLimitPerVesselOC();
		arg[29] = StringUtils.isEmpty(req.getLimitPerVesselOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerVesselOC(), req.getExchangeRate());
		arg[30] = StringUtils.isEmpty(req.getLimitPerLocationOC()) ? "0"	: req.getLimitPerLocationOC();
		arg[31] = StringUtils.isEmpty(req.getLimitPerLocationOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerLocationOC(), req.getExchangeRate());
		arg[32] = StringUtils.isEmpty(req.getEgnpiOffer()) ? "": req.getEgnpiOffer();
		arg[34] = StringUtils.isEmpty(req.getEgnpiOffer()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEgnpiOffer(), req.getExchangeRate());
		arg[33] = StringUtils.isEmpty(req.getOurAssessment()) ? "": req.getOurAssessment();
		arg[35] =req.getLoginId();
		arg[36] = req.getBranchCode();
		arg[37] = req.getMinimumpremiumPercent();
		arg[38] = req.getGnpiCapPercent();
		return arg;
	}


	private String[] insertHomePositionMasterAruguments(insertProportionalTreatyReq req) {
		String offerNo="",bouquetno="";
		if(StringUtils.isBlank(req.getBouquetNo()) && "Y".equals(req.getBouquetModeYN())) {
			bouquetno=fm.getSequence("Bouquet","9","0", req.getBranchCode(),"","");
			req.setBouquetNo(bouquetno);
		}
		if(StringUtils.isBlank(req.getOfferNo())) {
			offerNo=fm.getSequence("Offer",req.getProductId(),"0", req.getBranchCode(),"","");
			req.setOfferNo(offerNo);
		}
		
		String renewalStatus = getRenewalStatus(req);
		String [] argus = new String[32]; //ri
		String args2 = null;
		if (req.getAmendStatus()) {
			argus[1] = req.getContNo();
			argus[2] = args2;
			argus[16] = req.getBaseLayer();
		} else {
			argus[1] = "0";
			argus[2] = "0";
			argus[16] = StringUtils.isBlank(req.getBaseLayer())?StringUtils.isBlank(req.getLayerProposalNo())? "": req.getLayerProposalNo():req.getBaseLayer();
		}
		argus[0] = req.getProposalNo();
		argus[3] = StringUtils.isEmpty(req.getLayerNo()) ? "0" : req.getLayerNo();
		argus[4] = "";
		argus[5] = req.getPid();
		argus[6] = req.getDepartmentId();
		argus[7] = req.getCedingCo();
		argus[8] = req.getUwYear();
		argus[9] = req.getMonth();
		argus[10] = req.getAccDate();
		argus[11] = req.getIncepDate();
		argus[12] = req.getExpDate();
		argus[13] = req.getProStatus();
		if (req.getAmendStatus()) {
			argus[14] = "A";
			argus[18] = renewalStatus;
		} else {
			argus[14] = "P";
			argus[18] = StringUtils.isEmpty(req.getLayerProposalNo()) ? "1" : "0";
		}
		argus[15] = StringUtils.isNotBlank(req.getBaseLoginID())?req.getBaseLoginID():req.getLoginId();
		argus[17] = StringUtils.isEmpty(req.getLayerProposalNo()) ?StringUtils.isEmpty(req.getRenewalcontractno()) ? "" : req.getRenewalcontractno():"";
		argus[19] = StringUtils.isBlank(req.getBroker())?"63":req.getBroker();
		argus[20] = req.getBranchCode();
		argus[21] = StringUtils.isBlank(req.getRetroType())?"":req.getRetroType();
		argus[22] = req.getLoginId();
		argus[23] = "N";
		argus[24] = "";
		argus[25] =  StringUtils.isEmpty(req.getContractListVal())?"":req.getContractListVal();		
		argus[26] = req.getBouquetModeYN();
		argus[27] = StringUtils.isBlank(req.getBouquetNo())? "": req.getBouquetNo();
		argus[28] = req.getUwYearTo();
		argus[29] = StringUtils.isBlank(req.getSectionNo())? "": req.getSectionNo();
		argus[30] = req.getOfferNo();
		argus[31] = req.getNewLayerNo();
		return argus;
	}


	private String[] getFirstPageSecondTableAruguments(insertProportionalTreatyReq req) {

		String[] args =null;
		String args2 = null;
		args = new String[39];
		if (req.getAmendStatus()){
			args[0] = req.getProposalNo();
			args[1] = args2;
		} else {
			args[0] = req.getProposalNo();
			args[1] = "0";
		}
		args[3] = StringUtils.isEmpty(req.getLimitOrigCur()) ? "0" : req.getLimitOrigCur().replaceAll(",", "");
		args[4] = StringUtils.isEmpty(req.getLimitOrigCur())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getLimitOrigCur().replaceAll(",", ""), req.getExchangeRate());
		args[2] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
		args[5] = StringUtils.isEmpty(req.getXlPremium()) ? "0" : req.getXlPremium();
		args[6] = StringUtils.isEmpty(req.getXlPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getXlPremium(), req.getExchangeRate());
		args[7] = StringUtils.isEmpty(req.getDeduchunPercent()) ? "0" : req.getDeduchunPercent();
		args[10] = StringUtils.isEmpty(req.getMdPremium()) ? "0" : req.getMdPremium();
		args[11] = StringUtils.isEmpty(req.getMdPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getMdPremium(), req.getExchangeRate());
		args[14] = StringUtils.isEmpty(req.getAdjRate()) ? "0" : req.getAdjRate();
		args[15] = StringUtils.isEmpty(req.getPortfoloCovered()) ? "0" : req.getPortfoloCovered();
		args[16] = StringUtils.isEmpty(req.getSubPremium()) ? "0" : req.getSubPremium();
		args[17] = StringUtils.isEmpty(req.getSubPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getSubPremium(), req.getExchangeRate());
		args[18] = StringUtils.isEmpty(req.getMaxLimitProduct()) ? "0" : req.getMaxLimitProduct();
		args[19] = StringUtils.isEmpty(req.getDeduchunPercent()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getDeduchunPercent(), req.getExchangeRate());
		args[20] = StringUtils.isEmpty(req.getSpRetro()) ? "" : req.getSpRetro();
		args[21] = StringUtils.isEmpty(req.getNoInsurer()) ? "0" : req.getNoInsurer();
		args[8] = StringUtils.isEmpty(req.getEpi()) ? "0" : req.getEpi();
		args[9] = StringUtils.isEmpty(req.getEpi()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEpi(), req.getExchangeRate());
		args[12] = StringUtils.isEmpty(req.getShareWritt()) ? "0" : req.getShareWritt();
		if (req.getProStatus().equalsIgnoreCase("P")) {
			args[13] = "0";
		} else if (req.getProStatus().equalsIgnoreCase("A")) {
			args[13] = StringUtils.isEmpty(req.getSharSign()) ? "0" : req.getSharSign();
		} else if (req.getProStatus().equalsIgnoreCase("R")) {
			args[13] = "0";
		} else {
			args[13] = "0";
		}
		args[22] = StringUtils.isEmpty(req.getLimitOurShare()) ? "0" : req.getLimitOurShare();
		args[23] = StringUtils.isEmpty(req.getLimitOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitOurShare(), req.getExchangeRate());
		args[24] = StringUtils.isEmpty(req.getEpiAsPerShare()) ? "0" : req.getEpiAsPerShare();
		args[25] = StringUtils.isEmpty(req.getEpiAsPerShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEpiAsPerShare(), req.getExchangeRate());
		args[26] = StringUtils.isEmpty(req.getMdpremiumourservice()) ? "0" : req.getMdpremiumourservice();
		args[27] = StringUtils.isEmpty(req.getMdpremiumourservice()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getMdpremiumourservice(), req.getExchangeRate());
		args[28] = StringUtils.isEmpty(req.getLimitPerVesselOC()) ? "0" : req.getLimitPerVesselOC();
		args[29] = StringUtils.isEmpty(req.getLimitPerVesselOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerVesselOC(), req.getExchangeRate());
		args[30] = StringUtils.isEmpty(req.getLimitPerLocationOC()) ? "0"	: req.getLimitPerLocationOC();
		args[31] = StringUtils.isEmpty(req.getLimitPerLocationOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerLocationOC(), req.getExchangeRate());
		args[32] = StringUtils.isEmpty(req.getEgnpiOffer()) ? "": req.getEgnpiOffer();
		args[34] = StringUtils.isEmpty(req.getEgnpiOffer()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEgnpiOffer(), req.getExchangeRate());
		args[33] = StringUtils.isEmpty(req.getOurAssessment()) ? "": req.getOurAssessment();
		args[35] =req.getLoginId();
		args[36] = req.getBranchCode();
		args[37] = req.getMinimumpremiumPercent();
		args[38] = req.getGnpiCapPercent();
		return args;
	}


	private String[] updateHomePositionMasterAruguments(insertProportionalTreatyReq req,String maxAmendId) {
		
		String args [] = null; 
		args = new String[24]; //Ri
		args[0] = StringUtils.isEmpty(req.getLayerNo()) ? "0" : req.getLayerNo();
		args[1] = "";
		args[2] = req.getPid();
		args[3] = req.getDepartmentId();
		args[4] = req.getCedingCo();
		args[5] = req.getUwYear();
		args[6] = req.getMonth();
		args[7] = req.getAccDate();
		args[8] = req.getIncepDate();
		args[9] = req.getExpDate();
		args[10] = req.getProStatus();
		if(req.getContNo()!=null&&!"0".equals(req.getContNo())&&!"".equals(req.getContNo()))
			args[11] = "A";
		else if (req.getProStatus().equalsIgnoreCase("A") || req.getProStatus().equalsIgnoreCase("P")) {
			args[11] = "P";
		} else if (req.getProStatus().equalsIgnoreCase("R")) {
			args[11] = "R";
		} else if("N".equalsIgnoreCase(req.getProStatus())) {
			args[11] = "N";
		}else{
			args[11] = "P";
		}
		args[12] = StringUtils.isBlank(req.getBroker())?"63":req.getBroker();

		args[13] = StringUtils.isBlank(req.getRetroType())?"":req.getRetroType();
		args[14] = req.getLoginId();
		args[15] = "";
		args[16] =  StringUtils.isEmpty(req.getContractListVal())?"":req.getContractListVal();
		args[17] = req.getBouquetModeYN();
		args[18] = req.getBouquetNo();
		args[19] = req.getUwYearTo();
		args[20] = req.getSectionNo();
		args[21] = req.getProposalNo();
		args[22] = maxAmendId;	
		args[23] = req.getNewLayerNo();
		return args;
	}


	private String[] getProposalSaveEditModeQuery(insertProportionalTreatyReq req) {

		String args [] = null;
		args = new String[36];
		args[0] = StringUtils.isEmpty(req.getLimitOrigCur()) ? "0" : req.getLimitOrigCur().replaceAll(",", "");
		args[1] = StringUtils.isEmpty(req.getLimitOrigCur())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getLimitOrigCur().replaceAll(",", ""), req.getExchangeRate());
		args[2] = StringUtils.isEmpty(req.getEpi()) ? "0" : req.getEpi();
		args[3] = StringUtils.isEmpty(req.getEpi()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getEpi(), req.getExchangeRate());
		args[4] = StringUtils.isEmpty(req.getShareWritt()) ? "0" : req.getShareWritt();
		args[5] = StringUtils.isEmpty(req.getSharSign()) ? "0" : req.getSharSign();
		args[6] = StringUtils.isEmpty(req.getMaxLimitProduct()) ? "0" : req.getMaxLimitProduct();
		args[7] = StringUtils.isEmpty(req.getSubPremium()) ? "0"	: req.getSubPremium();
		args[8] = StringUtils.isEmpty(req.getSubPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getSubPremium(), req.getExchangeRate());
		args[9] = StringUtils.isEmpty(req.getXlPremium()) ? "0"	: req.getXlPremium();
		args[10] = StringUtils.isEmpty(req.getXlPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getXlPremium(), req.getExchangeRate());
		args[11] = StringUtils.isEmpty(req.getPortfoloCovered()) ? "0" : req.getPortfoloCovered();
		args[12] = StringUtils.isEmpty(req.getDeduchunPercent()) ? "0" : req.getDeduchunPercent();
		args[13] = StringUtils.isEmpty(req.getDeduchunPercent()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getDeduchunPercent(),req.getExchangeRate());
		args[14] = StringUtils.isEmpty(req.getMdPremium()) ? "0" : req.getMdPremium();
		args[15] = StringUtils.isEmpty(req.getMdPremium()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getMdPremium(), req.getExchangeRate());
		args[16] = StringUtils.isEmpty(req.getAdjRate()) ? "0" : req.getAdjRate();
		args[17] = StringUtils.isEmpty(req.getSpRetro()) ? "" : req.getSpRetro();
		args[18] = StringUtils.isEmpty(req.getNoInsurer()) ? "0" : req.getNoInsurer();
		args[19] =StringUtils.isEmpty(req.getLimitOurShare()) ? "0" : req.getLimitOurShare();
		args[20] = StringUtils.isEmpty(req.getLimitOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitOurShare(), req.getExchangeRate());
		args[21] =StringUtils.isEmpty(req.getEpiAsPerOffer()) ? "0"	: req.getEpiAsPerOffer();
		args[22] = StringUtils.isEmpty(req.getEpiAsPerOffer()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEpiAsPerOffer(), req.getExchangeRate());
		args[23] =StringUtils.isEmpty(req.getMdpremiumourservice()) ? "0"	: req.getMdpremiumourservice();
		args[24] = StringUtils.isEmpty(req.getMdpremiumourservice()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getMdpremiumourservice(), req.getExchangeRate());
		args[25] =StringUtils.isEmpty(req.getLimitPerVesselOC()) ? "0" : req.getLimitPerVesselOC();
		args[26] = StringUtils.isEmpty(req.getLimitPerVesselOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerVesselOC(), req.getExchangeRate());
		args[27] =StringUtils.isEmpty(req.getLimitPerLocationOC()) ? "0" : req.getLimitPerLocationOC();
		args[28] = StringUtils.isEmpty(req.getLimitPerLocationOC()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getLimitPerLocationOC(), req.getExchangeRate());
		args[29] =StringUtils.isEmpty(req.getEgnpiOffer()) ? "0"	: req.getEgnpiOffer();
		args[31] = StringUtils.isEmpty(req.getEgnpiOffer()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEgnpiOffer(), req.getExchangeRate());
		args[30] =StringUtils.isEmpty(req.getOurAssessment()) ? "0"	: req.getOurAssessment();
		args[32] = req.getLoginId();
		args[33] = req.getBranchCode();
		args[34] = req.getProposalNo();
		args[35]=req.getEndNo()==null?"0":req.getEndNo();
		return args;
	}

	public String[] saveUpdateRiskDetailsSecondForm(
			final insertProportionalTreatyReq beanObj, final String endNo) {
		String[] obj=null;
		obj = new String[9];
		obj[0] = StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[1] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[2] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[3] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[4] = StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? "0": beanObj.getMdpremiumourservice();
		obj[5] = StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate());
		obj[6] = beanObj.getProposalNo();
		obj[7] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
		obj[8] = endNo;
		return obj;
	}
	public String[] savemodeUpdateRiskDetailsSecondFormSecondTable(
			final insertProportionalTreatyReq beanObj) {
		String[] obj=new String[0];
		if ("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())) {
			obj = new String[35];
			obj[0] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0"
					: beanObj.getBrokerage();
			obj[1] = beanObj.getTax();
			obj[2] = beanObj.getShareProfitCommission();
			obj[3] = beanObj.getAcquisitionCost();
			obj[4] = getDesginationCountry(beanObj.getAcquisitionCost(),
					beanObj.getExchangeRate());
			obj[5] = beanObj.getAnualAggregateLiability();
			obj[6] = getDesginationCountry(beanObj.getAnualAggregateLiability(),
					beanObj.getExchangeRate());
			obj[7] =StringUtils.isEmpty(beanObj.getReinstNo()) ? "0": beanObj.getReinstNo();
			obj[8] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium()) ? "0" :beanObj.getReinstAdditionalPremium();
			obj[9] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getReinstAdditionalPremium(), beanObj.getExchangeRate());
			obj[10] =StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "0" :beanObj.getLeaderUnderwriter();
			obj[11] = StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0" :beanObj.getLeaderUnderwritershare();
			obj[12] =StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj
					.getAccounts();
			obj[13] = beanObj.getExclusion();
			obj[14] = StringUtils.isBlank(beanObj.getRemarks())? "":beanObj.getRemarks();
			obj[15] = StringUtils.isBlank(beanObj.getUnderwriterRecommendations())? "":beanObj.getUnderwriterRecommendations();
			obj[16] = StringUtils.isBlank(beanObj.getGmsApproval())? "":beanObj.getGmsApproval();
			obj[17] = beanObj.getOthercost();
			obj[18] = StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? "0" :beanObj.getReinstAditionalPremiumpercent();
			obj[19] = StringUtils.isEmpty(beanObj.getBurningCost()) ? "0"
					: beanObj.getBurningCost();
			obj[20] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct()) ? "0" : beanObj.getAnualAggregateDeduct();
			obj[21] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct())
			|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
					: getDesginationCountry(beanObj.getAnualAggregateDeduct(), beanObj.getExchangeRate());
			obj[22] = StringUtils.isEmpty(beanObj.getOccurrentLimit()) ? "0" : beanObj.getOccurrentLimit();
			obj[23] = StringUtils.isEmpty(beanObj.getOccurrentLimit())
			|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
					: getDesginationCountry(beanObj.getOccurrentLimit(), beanObj.getExchangeRate());
			
			obj[24] = beanObj.getReInstatementPremium();
			obj[25] = StringUtils.isBlank(beanObj.getCrestaStatus())? "":beanObj.getCrestaStatus();
			obj[26] =StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "0" :beanObj.getLeaderUnderwritercountry();
			obj[27] = beanObj.getLoginId();
			obj[28] = beanObj.getBranchCode();
			obj[29] = StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
			if(StringUtils.isNotBlank(beanObj.getAcqBonus())&&"NCB".equalsIgnoreCase(beanObj.getAcqBonus())){
				obj[30]=StringUtils.isEmpty(beanObj.getAcqBonusPercentage())? "": beanObj.getAcqBonusPercentage();
				}
				else{
					obj[30]="";
				}
			obj[31]=StringUtils.isEmpty(beanObj.getAcqBonus())? "": beanObj.getAcqBonus();
			obj[33] = beanObj.getProposalNo();
			obj[32] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
			obj[34] = StringUtils.isBlank(beanObj.getAmendId())? "":beanObj.getAmendId();
		}
		return obj;
	}
	public String[] secondPageFirstTableSaveAruguments(final insertProportionalTreatyReq beanObj, final String endNo) {
		String[] obj=null;
		obj = new String[9];
		obj[0] = StringUtils.isEmpty(beanObj.getLimitOurShare()) ? "0": beanObj.getLimitOurShare();
		obj[1] = StringUtils.isEmpty(beanObj.getLimitOurShare())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[2] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer()) ? "0": beanObj.getEpiAsPerOffer();
		obj[3] = StringUtils.isEmpty(beanObj.getEpiAsPerOffer())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"	: getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[4] = StringUtils.isEmpty(beanObj.getMdpremiumourservice()) ? "0"	: beanObj.getMdpremiumourservice();
		obj[5] = StringUtils.isEmpty(beanObj.getMdpremiumourservice())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate());
		obj[6] = beanObj.getProposalNo();
		obj[7] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
		obj[8] = endNo;
		return obj;
	}
	public String[] secondPageCommissionSaveAruguments(
			final insertProportionalTreatyReq beanObj) {
		String[] obj=null;
		if ("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())) {

			obj = new String[38];
			obj[0] =  beanObj.getProposalNo();
			obj[1] = "0";
			obj[2] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
			obj[3] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0"
					: beanObj.getBrokerage();
			obj[4] = StringUtils.isEmpty(beanObj.getTax()) ? "0" : beanObj.getTax();
			obj[5] = StringUtils.isEmpty(beanObj.getShareProfitCommission()) ? "0": beanObj.getShareProfitCommission();
			obj[6] = "0";
			obj[7] = StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? "0": beanObj.getAcquisitionCost();
			obj[8] = StringUtils.isEmpty(beanObj.getAcquisitionCost())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAcquisitionCost(),beanObj.getExchangeRate());
			obj[9] = StringUtils.isEmpty(beanObj.getAnualAggregateLiability()) ? "0": beanObj.getAnualAggregateLiability();
			obj[10] = StringUtils.isEmpty(beanObj.getAnualAggregateLiability())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAnualAggregateLiability(), beanObj.getExchangeRate());
			obj[11] = StringUtils.isEmpty(beanObj.getReinstNo()) ? "0": beanObj.getReinstNo();
			obj[12] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium()) ? "0": beanObj.getReinstAdditionalPremium();
			obj[13] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getReinstAdditionalPremium(), beanObj.getExchangeRate());
			obj[14] = StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "0": beanObj.getLeaderUnderwriter();
			obj[15] = StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0": beanObj.getLeaderUnderwritershare();
			obj[16] = StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts();
			obj[17] = StringUtils.isEmpty(beanObj.getExclusion()) ? "": beanObj.getExclusion();
			obj[18] = StringUtils.isEmpty(beanObj.getRemarks()) ? "" : beanObj.getRemarks();
			obj[19] = StringUtils.isEmpty(beanObj .getUnderwriterRecommendations()) ? "" : beanObj .getUnderwriterRecommendations();
			obj[20] = StringUtils.isEmpty(beanObj.getGmsApproval()) ? "" : beanObj.getGmsApproval();
			obj[21] = "";
			obj[22] = "";
			obj[23] = StringUtils.isEmpty(beanObj.getOthercost()) ? "0": beanObj.getOthercost();
			obj[24] = StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? "0" : beanObj.getReinstAditionalPremiumpercent();
			obj[25] = StringUtils.isEmpty(beanObj.getBurningCost()) ? "0" : beanObj.getBurningCost();
			obj[26] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct()) ? "0" : beanObj.getAnualAggregateDeduct();
			obj[27] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAnualAggregateDeduct(), beanObj.getExchangeRate());
			obj[28] = StringUtils.isEmpty(beanObj.getOccurrentLimit()) ? "0" : beanObj.getOccurrentLimit().replaceAll(",", "");
			obj[29] = StringUtils.isEmpty(beanObj.getOccurrentLimit())
			|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0"
					: getDesginationCountry(beanObj.getOccurrentLimit().replaceAll(",", ""), beanObj.getExchangeRate());
			
			obj[30] = beanObj.getReInstatementPremium();
			obj[31] = beanObj.getCrestaStatus();
			obj[32] = StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "0": beanObj.getLeaderUnderwritercountry();
			obj[33] = beanObj.getLoginId();
			obj[34] = beanObj.getBranchCode();
			obj[35] =StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
			if(StringUtils.isNotBlank(beanObj.getAcqBonus())&&"NCB".equalsIgnoreCase(beanObj.getAcqBonus())){
				obj[36]=StringUtils.isEmpty(beanObj.getAcqBonusPercentage())? "": beanObj.getAcqBonusPercentage();
				}
				else{
					obj[36]="";
				}
			obj[37]=StringUtils.isEmpty(beanObj.getAcqBonus())? "": beanObj.getAcqBonus();

			
		}
		return obj;
	}
	private String getproposalStatus(final String proposalNo) {
		String result="";
		try{
			//risk.select.getRskStatus
			List<TtrnRiskDetails> list = ttrnRiskDetailsRepository.findByRskProposalNumber(proposalNo);
			
				if(!CollectionUtils.isEmpty(list)) {
					result=list.get(0).getRskStatus()==null?"":list.get(0).getRskStatus().toString();
				}
		}catch(Exception e){
				e.printStackTrace();
			}
		return result;
	}
	
	private String getMaxAmednIdPro(String proposalNo) {
		String result ="";
		try{
			//GET_MAX_AMEND_RISK_PROPOSAL
			TtrnRiskProposal list = ttrnRiskProposalRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if(list!=null) {
				result=list.getRskEndorsementNo()==null?"0":list.getRskEndorsementNo().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public int getReInstatementCount(String amendId, String proposalNo,String branchCode,String referenceNo) {
		int result=0;
		try{
				if(StringUtils.isBlank(amendId)){
					amendId ="0";
				}
				//REINSTATEMENT_COUNT_MAIN
				result = nonProportCustomRepository.reinstatementCountMain(proposalNo,branchCode,amendId);
				
				if(result==0) { //ri
					//REINSTATEMENT_COUNT_MAIN_REFERENCE
					result = nonProportCustomRepository.reinstatementCountMainReference(referenceNo,branchCode,amendId);
				}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}	
	public int getBonusListCount(insertProportionalTreatyReq bean) {
		String args[]=null;
		int result=0;
		try{
			if(StringUtils.isBlank(bean.getAmendId())){
				bean.setAmendId("0");
			}
			args = new String[5];
			args[0] = bean.getProposalNo();
			args[1] = bean.getBranchCode();
			args[2] = bean.getAcqBonus();
			args[3] = bean.getAmendId();
			args[4] = StringUtils.isEmpty(bean.getLayerNo())?"0":bean.getLayerNo();
		
			//BONUS_COUNT_MAIN
			result = ttrnBonusRepository.countByProposalNoAndBranchAndTypeAndEndorsementNoAndLayerNoAndLcbFromNotNull(
					new BigDecimal(args[0]),args[1],args[2],new BigDecimal(args[3]),args[4]);
		
			if(result==0) {
				//BONUS_COUNT_MAIN_REFERENCE             //ri
				args[0] = bean.getReferenceNo();
				result = ttrnBonusRepository.countByReferenceNoAndBranchAndTypeAndEndorsementNoAndLayerNoAndLcbFromNotNull(
						new BigDecimal(args[0]),args[1],args[2],new BigDecimal(args[3]),args[4]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	return result;
}
	
	public boolean GetShareValidation(String proposalNo, String leaderUnderwritershare) {
		boolean result=false;
		String out="";
		try {
			//GET_SIGN_SHARE_PRODUCT23
			out =	nonProportCustomRepository.getSignShareProduct23(proposalNo);
		
			if(Double.parseDouble(out)+Double.parseDouble(leaderUnderwritershare)>100){
				result=true;
			}
		}catch (Exception e) {
				e.printStackTrace();
			}
				return result;
			}
			public int getCrestaCount(String amendId, String proposalNo,String branchCode) {
				int count=0;
				try {
					//GET_CRESTA_DETAIL_COUNT
					count = ttrnCrestazoneDetailsRepository.countByProposalNoAndAmendIdAndBranchCode(new BigDecimal(proposalNo), amendId, branchCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return count;
			}
	
	public boolean updateFirstPageFields(insertProportionalTreatyReq req, String endNo){
		boolean updateStatus = true;
		int res=0;
		
		String[] args= new String[57];
		try {
			args[0] = StringUtils.isEmpty(req.getEventlimit()) ? "": req.getEventlimit();
			args[1] = StringUtils.isEmpty(req.getEventlimit())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getEventlimit(), req.getExchangeRate());
			args[2] = StringUtils.isEmpty(req.getEventLimitOurShare()) ? "0" : req.getEventLimitOurShare();
			args[3] = StringUtils.isEmpty(req.getEventLimitOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEventLimitOurShare(), req.getExchangeRate());
			
			args[4] = StringUtils.isEmpty(req.getCoverLimitXL()) ? "": req.getCoverLimitXL();
			args[5] = StringUtils.isEmpty(req.getCoverLimitXL())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getCoverLimitXL(), req.getExchangeRate());
			args[6] = StringUtils.isEmpty(req.getCoverLimitXLOurShare()) ? "0" : req.getCoverLimitXLOurShare();
			args[7] = StringUtils.isEmpty(req.getCoverLimitXLOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getCoverLimitXLOurShare(), req.getExchangeRate());
			
			args[8] = StringUtils.isEmpty(req.getDeductLimitXL()) ? "": req.getDeductLimitXL();
			args[9] = StringUtils.isEmpty(req.getDeductLimitXL())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getDeductLimitXL(), req.getExchangeRate());
			args[10] = StringUtils.isEmpty(req.getDeductLimitXLOurShare()) ? "0" : req.getDeductLimitXLOurShare();
			args[11] = StringUtils.isEmpty(req.getDeductLimitXLOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getDeductLimitXLOurShare(), req.getExchangeRate());
			
			args[12] = StringUtils.isEmpty(req.getPml()) ? "" : req.getPml();
			args[13] = StringUtils.isEmpty(req.getPmlPercent()) ? "0" : req.getPmlPercent();
			
			args[14] = StringUtils.isEmpty(req.getEgnpipml()) ? "": req.getEgnpipml();
			args[15] = StringUtils.isEmpty(req.getEgnpipml())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getEgnpipml(), req.getExchangeRate());
			args[16] = StringUtils.isEmpty(req.getEgnpipmlOurShare()) ? "0" : req.getEgnpipmlOurShare();
			args[17] = StringUtils.isEmpty(req.getEgnpipmlOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getEgnpipmlOurShare(), req.getExchangeRate());
			
			args[18] = StringUtils.isEmpty(req.getPremiumbasis()) ? "" : req.getPremiumbasis();
			args[19] = StringUtils.isEmpty(req.getMinimumRate()) ? "0" : req.getMinimumRate();
			args[20] = StringUtils.isEmpty(req.getMaximumRate()) ? "0" : req.getMaximumRate();
			args[21] = StringUtils.isEmpty(req.getBurningCostLF()) ? "0" : req.getBurningCostLF();
			
			args[22] = StringUtils.isEmpty(req.getMinPremium()) ? "": req.getMinPremium();
			args[23] = StringUtils.isEmpty(req.getMinPremium())	|| StringUtils.isEmpty(req.getExchangeRate()) ? "0"	: getDesginationCountry(req.getMinPremium(), req.getExchangeRate());
			args[24] = StringUtils.isEmpty(req.getMinPremiumOurShare()) ? "0" : req.getMinPremiumOurShare();
			args[25] = StringUtils.isEmpty(req.getMinPremiumOurShare()) || StringUtils.isEmpty(req.getExchangeRate()) ? "0" : getDesginationCountry(req.getMinPremiumOurShare(), req.getExchangeRate());
			args[26] = StringUtils.isEmpty(req.getPaymentDuedays()) ? "0" : req.getPaymentDuedays();
			
			args[27] =StringUtils.isEmpty(req.getLimitOrigCurPml()) ? "0": req.getLimitOrigCurPml();
			args[28] = StringUtils.isEmpty(req.getLimitOrigCurPml())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getLimitOrigCurPml(), req.getExchangeRate());
			args[29] =StringUtils.isEmpty(req.getLimitOrigCurPmlOS()) ? "0": req.getLimitOrigCurPmlOS();
			args[30] = StringUtils.isEmpty(req.getLimitOrigCurPmlOS())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getLimitOrigCurPmlOS(), req.getExchangeRate());
			args[31] =StringUtils.isEmpty(req.getTreatyLimitsurplusOCPml()) ? "0": req.getTreatyLimitsurplusOCPml();
			args[32] = StringUtils.isEmpty(req.getTreatyLimitsurplusOCPml())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getTreatyLimitsurplusOCPml(), req.getExchangeRate());
			args[33] =StringUtils.isEmpty(req.getTreatyLimitsurplusOCPmlOS()) ? "0": req.getTreatyLimitsurplusOCPmlOS();
			args[34] = StringUtils.isEmpty(req.getTreatyLimitsurplusOCPmlOS())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getTreatyLimitsurplusOCPmlOS(), req.getExchangeRate());
			args[35] =StringUtils.isEmpty(req.getEpipml()) ? "0": req.getEpipml();
			args[36] = StringUtils.isEmpty(req.getEpipml())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getEpipml(), req.getExchangeRate());
			args[37] =StringUtils.isEmpty(req.getEpipmlOS()) ? "0": req.getEpipmlOS();
			args[38] = StringUtils.isEmpty(req.getEpipmlOS())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req.getEpipmlOS(), req.getExchangeRate());
			args[39]=req.getRiskdetailYN();
			args[40]=req.getBrokerdetYN();
			args[41]=StringUtils.isBlank(req.getCoverdetYN())? "":req.getCoverdetYN();
			args[42]=req.getPremiumdetailYN();
			args[43]=req.getAcqdetailYN();
			args[44]=StringUtils.isBlank(req.getCommissiondetailYN())? "":req.getCommissiondetailYN();
			args[45]=StringUtils.isBlank(req.getDepositdetailYN())? "":req.getDepositdetailYN();
			args[46]=StringUtils.isBlank(req.getLossdetailYN())? "":req.getLossdetailYN();
			args[47]=StringUtils.isBlank(req.getDocdetailYN())? "":req.getDocdetailYN();
			args[48] = StringUtils.isBlank(req.getPaymentPartner())? "":req.getPaymentPartner();
			args[49] = req.getInstallYN();
			args[50] = req.getReinstdetailYN();
			args[51] = StringUtils.isBlank(req.getRateOnLine())? "":req.getRateOnLine();
			args[52] =StringUtils.isEmpty(req.getQuotesharePercent()) ? "0": req.getQuotesharePercent();
			args[53] = req.getProposalNo();
			args[54]=endNo;
			args[55] = req.getMinimumpremiumPercent();
			args[56] = req.getGnpiCapPercent();
			
			//UPDATE_RISK_PROPOSAL_DETAILS
			TtrnRiskProposal update = nonProportCustomRepository.updateFirstPageFields(args);
			if(update!=null) {
			ttrnRiskProposalRepository.saveAndFlush(update);	
			res=1;
			}
			if (res> 0) {
				updateStatus = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return updateStatus;
		
	}
	
	
	private String getRenewalStatus(insertProportionalTreatyReq req) {
		String result="";
	
		try{
			if(StringUtils.isNotBlank(req.getContNo())){
				//risk.select.getRenewalStatus
				 result = nonProportCustomRepository.riskSelectGetRenewalStatus(req.getProposalNo());
			}
		}
			
	catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String[] getFirstPageEditSaveModeAruguments(insertProportionalTreatyReq req, String EndNo) {
		
		String[] args=null;
		args = new String[55];//ri
		args[0] = StringUtils.isEmpty(req.getDepartmentId()) ? "0" : req.getDepartmentId();
		args[1] = StringUtils.isEmpty(req.getProfitCenter()) ? "0" : req.getProfitCenter();
		args[2] = StringUtils.isEmpty(req.getSubProfitcenter()) ? "0" : req.getSubProfitcenter();
		args[3] = StringUtils.isEmpty(req.getPolicyBranch()) ? "0" : req.getPolicyBranch();
		args[4] = StringUtils.isEmpty(req.getCedingCo()) ? "0" : req.getCedingCo();
		args[5] = StringUtils.isEmpty(req.getBroker()) ? "63" : req.getBroker();
		args[6] = StringUtils.isEmpty(req.getTreatyNametype()) ? "" : req.getTreatyNametype();
		args[7] = StringUtils.isEmpty(req.getMonth()) ? "" : req.getMonth();
		args[8] = StringUtils.isEmpty(req.getUwYear()) ? "0" : req.getUwYear();
		args[9] = StringUtils.isEmpty(req.getUnderwriter()) ? "" : req.getUnderwriter();
		args[10] = StringUtils.isEmpty(req.getIncepDate()) ? "" : req.getIncepDate();
		args[11] = StringUtils.isEmpty(req.getExpDate()) ? "" : req.getExpDate();
		args[12] = StringUtils.isEmpty(req.getAccDate()) ? "" : req.getAccDate();
		args[13] = StringUtils.isEmpty(req.getOrginalCurrency()) ? "0" : req.getOrginalCurrency();
		args[14] = StringUtils.isEmpty(req.getExchangeRate()) ? "0" : req.getExchangeRate();
		args[15] = StringUtils.isEmpty(req.getBasis()) ? "0" : req.getBasis();
		args[16] = StringUtils.isEmpty(req.getPnoc()) ? "" : req.getPnoc();
		args[17] = StringUtils.isEmpty(req.getRiskCovered()) ? "" : req.getRiskCovered();
		args[18] = StringUtils.isEmpty(req.getTerritoryscope()) ? "" : req.getTerritoryscope();
		args[19] = StringUtils.isEmpty(req.getTerritory()) ? "" : req.getTerritory();
		args[20] = StringUtils.isEmpty(req.getProStatus()) ? ""	: req.getProStatus();
		args[21] = StringUtils.isEmpty(req.getProposalType()) ? "0"	: req.getProposalType();
		args[22] = "0";
		args[23] = "0";
		args[24] = "0";
		
		args[25] = StringUtils.isEmpty(req.getMdInstalmentNumber()) ? "0"	: req.getMdInstalmentNumber();
		args[26] = StringUtils.isEmpty(req.getNoRetroCess()) ? "0"	: req.getNoRetroCess();
		args[27] = StringUtils.isEmpty(req.getRetroType()) ? "0" : req.getRetroType();
		args[28] = StringUtils.isEmpty(req.getInsuredName()) ? "" : req.getInsuredName();
		args[29]=StringUtils.isEmpty(req.getInwardType()) ? "0"	: req.getInwardType();
		args[30]=StringUtils.isEmpty(req.getTreatyType()) ? ""	: req.getTreatyType();
		args[31]=StringUtils.isEmpty(req.getBusinessType()) ? ""	: req.getBusinessType();
		args[32]=StringUtils.isEmpty(req.getExchangeType()) ? ""	: req.getExchangeType();
		args[33]=StringUtils.isEmpty(req.getPerilCovered()) ? ""	: req.getPerilCovered();
		args[34]=StringUtils.isEmpty(req.getLOCIssued()) ? ""	: req.getLOCIssued();
		args[35]=StringUtils.isEmpty(req.getUmbrellaXL()) ? ""	: req.getUmbrellaXL();
		
		args[36] = req.getLoginId();
		args[37] = req.getBranchCode();
		args[38] = StringUtils.isEmpty(req.getCountryIncludedList()) ? ""	:req.getCountryIncludedList();
		args[39] = StringUtils.isEmpty(req.getCountryExcludedList()) ? ""	:req.getCountryExcludedList();
		args[40] = "";
		args[41] =StringUtils.isEmpty(req.getEndorsmenttype()) ? ""	:req.getEndorsmenttype();
		args[42] ="0";
		args[43] =StringUtils.isEmpty(req.getLocBankName()) ? ""	:req.getLocBankName();
		args[44] =StringUtils.isEmpty(req.getLocCreditPrd()) ? ""	:req.getLocCreditPrd();
		args[45] =StringUtils.isEmpty(req.getLocCreditAmt()) ? ""	:req.getLocCreditAmt().replaceAll(",", "");
		args[46] =StringUtils.isEmpty(req.getLocBeneficerName()) ? ""	:req.getLocBeneficerName();
		args[47] = "";
		args[48]="";
		args[49]=StringUtils.isEmpty(req.getRetentionYN()) ? ""	:req.getRetentionYN();
		args[50] = req.getAccountingPeriodNotes();
		args[51] =req.getStatementConfirm();
		args[52] = req.getProposalNo();
		
		args[53]= EndNo;
		args[54] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();

		return args;
		
	}

	@Override
	public GetReInstatementDetailsListRes getReInstatementDetailsList(getReInstatementDetailsListReq req) {
		GetReInstatementDetailsListRes response = new GetReInstatementDetailsListRes();
		List<ReInStatementRes> reInstResList=new ArrayList<ReInStatementRes>();
		try{

			//REINSTATEMENT_MAIN_SELECT_A
			List<Tuple> result = null; 
					//nonProportCustomRepository.reinstatementMainSelectA(req.getProposalNo(),req.getBranchCode());
				
			if(CollectionUtils.isEmpty(result) && !"0".equals(req.getReferenceNo())){ //Ri
						//REINSTATEMENT_MAIN_SELECT_A_REFERENCE
						result =  nonProportCustomRepository.reinstatementMainSelectAReference(req.getReferenceNo(),req.getBranchCode());

					}
			if(CollectionUtils.isEmpty(result)){
				result =  nonProportCustomRepository.reinstatementMainSelectA(req.getProposalNo(),req.getBranchCode());
			}
				for(int i=0;i<result.size();i++){
					   Tuple tempMap = result.get(i);
		               ReInStatementRes reInstRes = new ReInStatementRes();
		               reInstRes.setSno(tempMap.get("REINST_NO")==null?"":tempMap.get("REINST_NO").toString());
		               reInstRes.setType(tempMap.get("REINST_TYPE")==null?"":tempMap.get("REINST_TYPE").toString());
		               reInstRes.setAmount(tempMap.get("AMOUNT_PERCENT")==null?"":fm.formattereight(tempMap.get("AMOUNT_PERCENT").toString()));
		               reInstRes.setMinamount(tempMap.get("MIN_AMOUNT_PERCENT")==null?"":fm.formattereight(tempMap.get("MIN_AMOUNT_PERCENT").toString()));
		               reInstRes.setMinTime(tempMap.get("MIN_TIME_PERCENT")==null?"":fm.formattereight(tempMap.get("MIN_TIME_PERCENT").toString()));
		               reInstRes.setReinstatementOption(tempMap.get("REINSTATEMENT")==null?"":tempMap.get("REINSTATEMENT").toString());
		               reInstResList.add(reInstRes);
		               }
				
//					query ="REINSTATEMENT_MAIN_SELECT_B";
//	               list = queryImpl.selectList(query,args);
//	              List <CoverListInstate> coverListRes = new ArrayList<CoverListInstate>();
//	               for(int i=0;i<list.size();i++){
//	            	   CoverListInstate coverList = new CoverListInstate();
//		               Map<String,Object> tempMap = list.get(i);
//		               coverList.setCoverdepartId(tempMap.get("DEPARTMENT_CLASS")==null?"":tempMap.get("DEPARTMENT_CLASS").toString());
//		               coverList.setCoverLimitOC(tempMap.get("ANNUAL_AGGRE_LAIBLE")==null?"":fm.formatter(tempMap.get("ANNUAL_AGGRE_LAIBLE").toString()));
//		               coverListRes.add(coverList);
//		               res.setCoverList(coverListRes);
//		               resList.add(res);
//		               }
//	             
//	               if(!"U".equalsIgnoreCase(res.getReinstatementOption())){
//	               res.setBusinessType("1");
//	         //      getClassLimitDetails(req);
//	               if(result!=null && result.size()>0){
//	            	   double coverLimit=0.00;
//	            	   List<CoverLimitAmountInstate> CoverLimitAmountList = new ArrayList<CoverLimitAmountInstate>();
//	            	   CoverLimitAmountInstate coverLimitAmount = new CoverLimitAmountInstate();
//	            	   if(res.getCoverList()!=null && res.getCoverList().size()>0){
//	            		   for(int i=0;i<res.getCoverList().size();i++){
//	            			   if(StringUtils.isNotBlank(res.getReinstatementOption()) && "S".equalsIgnoreCase(res.getReinstatementOption())){
//		            			   coverLimit= Double.parseDouble(res.getCoverList().get(i).getCoverLimitOC().replace(",", "")) *(double)(result.size()+1);
//		            			   coverLimitAmount.setCoverLimitAmount(Double.toString(coverLimit));
//	            			   }else{
//	            				  if(StringUtils.isNotBlank(res.getCoverList().get(i).getCoverLimitOC())) {
//	            				   coverLimit= Double.parseDouble(res.getCoverList().get(i).getCoverLimitOC().replace(",", "")) *1;
//	            				   coverLimitAmount.setCoverLimitAmount(Double.toString(coverLimit));
//	            				  }
//	            			   }
//	            			   CoverLimitAmountList.add(coverLimitAmount);
//	            			   res.setCoverLimitAmount(CoverLimitAmountList);
//	            			   resList.add(res);
//	            		   }
//	            		  
//	            	   }
//	               }
//	              }else{
//	            	  res.setBusinessType("1");
//	            	//  getClassLimitDetails(req);
//	              }


	               response.setCommonResponse(reInstResList);

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
	public CommonSaveRes moveReinstatementMain(MoveReinstatementMainReq req) {
		CommonSaveRes response = new CommonSaveRes();
		try{
		if(StringUtils.isBlank(req.getAmendId())){
			req.setAmendId("0");
		}
		deleteMainTable(req.getProposalNo(),req.getAmendId(),req.getBranchCode(),req.getReferenceNo());
		if(StringUtils.isBlank(req.getProposalNo()) && (StringUtils.isBlank(req.getReferenceNo()) || "0".equals(req.getReferenceNo()))) { //ri
        	String referenceNo="";
        	
        	List<Map<String, Object>> list  = queryImpl.selectSingle("SELECT  REFERENCENO_SEQ.NEXTVAL REFERENCENO FROM DUAL",new String[]{});

        	if (!CollectionUtils.isEmpty(list)) {
        		referenceNo = list.get(0).get("REFERENCENO") == null ? ""
        				: list.get(0).get("REFERENCENO").toString();
        	}
        	req.setReferenceNo(referenceNo);
        }


	    String args[] = new String[16];
			for(int i=0;i<req.getReinstatementNo().size();i++){
			ReinstatementNoList req1 = req.getReinstatementNo().get(i);
			args[0] = req1.getReinstatementNo();
			args[1] = req1.getReinstatementTypeId();
			args[2] = req1.getReinstatementAmount();
			args[3] = StringUtils.isEmpty(req1.getReinstatementMinAmount())?"":req1.getReinstatementMinAmount();
			args[4] = StringUtils.isEmpty(req1.getReinstatementMinTime())?"":req1.getReinstatementMinTime();
			args[5] = StringUtils.isBlank(req.getProposalNo())?"":req.getProposalNo();
			args[6] = req.getBranchCode();
			args[7] = req.getAmendId();
			args[8] = StringUtils.isBlank(req.getDepartmentId())? "":req.getDepartmentId();
			args[9] = "";
			args[10] = req.getProductId();
			args[11] = StringUtils.isEmpty(req.getLayerNo()) ? "0"	: req.getLayerNo();
			args[12] = req.getReinstatementOption();
			args[13] = "A";
			args[14]=StringUtils.isBlank(req.getReferenceNo())?"":req.getReferenceNo();

	           String sno = "";
	           TtrnRip list = ttrnRipRepository.findTop1ByBranchCodeOrderBySnoDesc(req.getBranchCode());
	           if(list!=null) {
	        	   sno =   list.getSno()==null?"0": String.valueOf(list.getSno().intValue()+1);
	           }
			args[15] = sno;

			 //INSERT_REINSTATEMENT_MAIN
			TtrnRip insert =  nonProportCustomRepository.insertReinstatementMain(args);
			if(insert!=null) {
				ttrnRipRepository.saveAndFlush(insert);
				}
			}
//			query = "INSERT_REINSTATEMENT_MAIN_B";
//			 String args1[] = new String[11];
//			for(int i=0;i<req.getCoverdepartIdList().size();i++){
//				CoverdepartIdList req2 = req.getCoverdepartIdList().get(i)	;
//				args1[0] = req2.getCoverdepartId();
//				args1[1] = req2.getCoverLimitOC().replaceAll(",", "");
//				args1[2] = req.getProposalNo();
//				args1[3] = req.getBranchCode();
//				args1[4] = req.getAmendId();
//				args1[5] = req.getDepartmentId();
//				args1[6] ="";
//				args1[7] = req.getProductId();
//				args1[8] = StringUtils.isEmpty(req.getLayerNo()) ? "0"	: req.getLayerNo();
//				args1[9] = "B";
//				args[10]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();
//				 queryImpl.updateQuery(query, args1);		
//			}
				response.setResponse(req.getReferenceNo());
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
	public CommonResponse deleteMainTable(String proposalNo, String amendId, String branchCode, String referenceNo) {
		CommonResponse response = new CommonResponse();
		try{
			if("".equalsIgnoreCase(amendId)){
				//REINSTATEMENT_MAIN_DELETE
				 ttrnRipRepository.deleteByProposalNoAndBranchCode(proposalNo,branchCode);
			}else if(StringUtils.isBlank(proposalNo)) {
				//REINSTATEMENT_MAIN_DELETE
				if(StringUtils.isNotBlank(referenceNo) && !"0".equals(referenceNo))
				 ttrnRipRepository.deleteByReferenceNoAndBranchCode(new BigDecimal(referenceNo),branchCode);
			}
			else{
				//REINSTATEMENT_MAIN_DELETE2
				ttrnRipRepository.deleteByProposalNoAndAmendIdAndBranchCode(proposalNo,new BigDecimal(amendId),branchCode);
			}
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
	public GetLowClaimBonusListRes getLowClaimBonusList(String proposalNo, String branchCode, String acqBonus,String referenceNo) {
		GetLowClaimBonusListRes response = new GetLowClaimBonusListRes();
		List<Tuple> result = new ArrayList<>();
         GetLowClaimBonusListRes1 res = new GetLowClaimBonusListRes1();
         List<GetLowClaimBonusListRes1> resList= new ArrayList<GetLowClaimBonusListRes1>();
		try{
			
				//BONUS_MAIN_SELECT //ri
				result =  nonProportCustomRepository.bonusMainSelect(proposalNo,branchCode, acqBonus);
					if(CollectionUtils.isEmpty(result) && !"0".equals(referenceNo)) {
						//BONUS_MAIN_SELECT_REFERENCE
						result =  nonProportCustomRepository.bonusMainSelectReference(referenceNo,branchCode, acqBonus);
					} 
					List<BonusRes> bonusResList = new ArrayList<BonusRes>();
					for(int i=0;i<result.size();i++){
					BonusRes bonusRes = new BonusRes();
					Tuple tempMap = result.get(i);
		               res.setBonusTypeId(tempMap.get("LCB_TYPE")==null?"":tempMap.get("LCB_TYPE").toString());
		               bonusRes.setBonusSNo(tempMap.get("LCB_ID")==null?"":tempMap.get("LCB_ID").toString());
		               bonusRes.setBonusFrom(tempMap.get("LCB_FROM")==null?"":fm.formatter(tempMap.get("LCB_FROM").toString()));	  
		               bonusRes.setBonusTo(tempMap.get("LCB_TO")==null?"":fm.formatter(tempMap.get("LCB_TO").toString()));
		               bonusRes.setBonusLowClaim(tempMap.get("LCB_PERCENTAGE")==null?"":fm.formatter(tempMap.get("LCB_PERCENTAGE").toString()));		  
		               bonusResList.add(bonusRes);
		               res.setBonusRes(bonusResList);
		               resList.add(res);
		           }
//	               if("RD".equalsIgnoreCase(bean.getFlag()) && count<=0){
//	               LowClaimBonusInser(bean);
//	               }
					response.setCommonResponse(resList);
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
	@Transactional
	public CommonSaveRes lowClaimBonusInser(LowClaimBonusInserReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		try{
			if(StringUtils.isBlank(bean.getEndorsmentNo()) && StringUtils.isNotBlank(bean.getProposalNo())){
				//GET_AMEND_ID
				PositionMaster list  =	positionMasterRepository.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(bean.getProposalNo()));
	        	if(list!=null) {
				bean.setEndorsmentNo(list.getAmendId()==null?"":list.getAmendId().toString());
				}
	           
			}
			if(StringUtils.isBlank(bean.getEndorsmentNo())) {
	        	   bean.setEndorsmentNo("0");
	           }
	        deleteMaintable(bean);
			 if(StringUtils.isBlank(bean.getProposalNo()) && (StringUtils.isBlank(bean.getReferenceNo())|| "0".equals(bean.getReferenceNo()))) {
		        	String referenceNo="";
		        
		        	List<Map<String, Object>> list  = queryImpl.selectSingle("SELECT  REFERENCENO_SEQ.NEXTVAL REFERENCENO FROM DUAL",new String[]{});
		        	if (!CollectionUtils.isEmpty(list)) {
		        		referenceNo = list.get(0).get("REFERENCENO") == null ? "": list.get(0).get("REFERENCENO").toString();
		        	}
		        	bean.setReferenceNo(referenceNo);
		        }
	       
			String args[]=new String[16];
			for(int i=0;i<bean.getBonusReq().size();i++){
				BonusReq req = bean.getBonusReq().get(i);
				if(StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo()) &&StringUtils.isNotBlank(req.getBonusLowClaimBonus()) ){
			           args[0] =bean.getProposalNo();
			           args[1] = bean.getContractNo();
			           args[2] = bean.getProductId();
			           args[3] = bean.getBonusTypeId();
			           args[4] = req.getBonusFrom().replace(",", "");
			           args[5] = req.getBonusTo().replace(",", "");
			           args[6] =req.getBonusLowClaimBonus().replace(",", "");
			           args[7] = bean.getLoginId();
			           args[8] = bean.getBranchCode();
			           args[9] = req.getBonusSNo().replace(",", "");
			           args[10] =bean.getAcqBonus();
			           args[11] = bean.getEndorsmentNo();
			           args[12] =bean.getDepartmentId();
			           args[13] =StringUtils.isEmpty(bean.getLayerNo())?"0":bean.getLayerNo();
			           args[14]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();
			           
			           String sno = "";
			           TtrnBonus list = ttrnBonusRepository.findTop1ByBranchOrderBySnoDesc(bean.getBranchCode());
			           if(list!=null) {
			        	   sno =   list.getSno()==null?"0": String.valueOf(list.getSno().intValue()+1);
			           }
			           args[15] =sno;
			         //BONUS_MAIN_INSERT
			           TtrnBonus insert = nonProportCustomRepository.bonusMainInsert(args);
			           if(insert!=null) {
			        	   ttrnBonusRepository.saveAndFlush(insert);
			           }
				}
			}
			response.setResponse(bean.getReferenceNo());
			response.setMessage("Success");
	   		response.setIsError(false);
   			}catch(Exception e){
   				e.printStackTrace();
   				response.setMessage("Failed");
   				response.setIsError(true);
   			}
	   		return response;
	}

	@Transactional
	private void deleteMaintable(LowClaimBonusInserReq bean) {
		String arg[]=null;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaDelete<TtrnBonus> delete = cb.createCriteriaDelete(TtrnBonus.class);
			Root<TtrnBonus> rp = delete.from(TtrnBonus.class);
			
			if("".equalsIgnoreCase(bean.getEndorsmentNo())){
				//BONUS_MAIN_DELETE
				 arg = new String[4];
				 arg[0] = bean.getProposalNo();
				 arg[1] = bean.getBranchCode();
				 arg[2] ="LCB";
		         arg[3]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
				  	
				  	Predicate n1 = cb.equal(rp.get("proposalNo"), arg[0]);
					Predicate n3 = cb.equal(rp.get("branch"), arg[1]);
					Predicate n4 = cb.equal(rp.get("type"), arg[2]);
					Predicate n5 = cb.equal(rp.get("layerNo"), arg[3]);
					delete.where(n1,n3,n4,n5);
					em.createQuery(delete).executeUpdate();
					
			}else if(StringUtils.isBlank(bean.getProposalNo())) {
				//BONUS_MAIN_DELETE3
				 arg = new String[4];
				 arg[0] = bean.getReferenceNo();
				 arg[1] = bean.getBranchCode();
				 arg[2] ="LCB";
				 arg[3]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
				  
				  	Predicate n1 = cb.equal(rp.get("referenceNo"), arg[0]);
					Predicate n3 = cb.equal(rp.get("branch"), arg[1]);
					Predicate n4 = cb.equal(rp.get("type"), arg[2]);
					Predicate n5 = cb.equal(rp.get("layerNo"), arg[3]);
					Predicate n6 = cb.or(cb.isNull(rp.get("proposalNo")),cb.equal(rp.get("proposalNo"), 0));
					delete.where(n1,n3,n4,n5,n6);
					em.createQuery(delete).executeUpdate();
					
			}	else{
			 //BONUS_MAIN_DELETE2
			 arg = new String[5];
			 arg[0] = bean.getProposalNo();
			 arg[1] = bean.getEndorsmentNo();
			 arg[2] = bean.getBranchCode();
			 arg[3] ="LCB";
			  arg[4]=StringUtils.isEmpty(bean.getLayerNo()) ? "0" : bean.getLayerNo();
				
				//Where
				Predicate n1 = cb.equal(rp.get("proposalNo"), arg[0]);
				Predicate n2 = cb.equal(rp.get("endorsementNo"), arg[1]);
				Predicate n3 = cb.equal(rp.get("branch"), arg[2]);
				Predicate n4 = cb.equal(rp.get("type"), arg[3]);
				Predicate n5 = cb.equal(rp.get("layerNo"), arg[4]);
				delete.where(n1,n2,n3,n4,n5);
				em.createQuery(delete).executeUpdate();

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public GetInclusionExListRes getInclusionExList(String proposalNo, String branchCode) {
		GetInclusionExListRes response = new GetInclusionExListRes();
		List<GetInclusionExListRes1> resList = new ArrayList<GetInclusionExListRes1>();
		try {
			//GET_INCLUSIONEX_LIST
			List<Tuple>	list = nonProportCustomRepository.getInclusionexList(proposalNo,branchCode);
			if (list != null  && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Tuple insMap = list.get(i);
					GetInclusionExListRes1 res = new GetInclusionExListRes1();
					res.setContractNo(insMap.get("CONTRACT_NO")==null?"":insMap.get("CONTRACT_NO").toString());
					res.setEffectiveDate(insMap.get("EEFECTIVE_DATE")==null?"":insMap.get("EEFECTIVE_DATE").toString());
					res.setLayerNo(insMap.get("LAYER_NO")==null?"":insMap.get("LAYER_NO").toString());
					res.setTransactionNo(insMap.get("TRANSACTION_NO")==null?"":insMap.get("TRANSACTION_NO").toString());
					res.setProposalNo(insMap.get("PROPOSAL_NO")==null?"":insMap.get("PROPOSAL_NO").toString());
					resList.add(res);			
				} }
				response.setCommonResponse(resList);	
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
	public CommonResponse insertIEModule(InsertIEModuleReq bean) {
		CommonResponse response = new CommonResponse();
		try{
			String tranId="";
			String amendId="";
			if(StringUtils.isBlank(bean.getTransactionNo())){
			List<Map<String, Object>> list = queryImpl.selectList("SELECT_transaction_no",new String[] {}); //dual
			if(!CollectionUtils.isEmpty(list)) {
				tranId = (list.get(0).get("TRANS_NO")==null?"":list.get(0).get("TRANS_NO").toString());
			}
			bean.setTransactionNo(tranId);
			amendId="0";
			}else{
				//SELECT_AMEND_ID
				PositionMaster list  =	positionMasterRepository.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(bean.getProposalNo()));
	        	if(list!=null) {
	        		amendId = list.getAmendId()==null?"":list.getAmendId().toString();
	        		}
			}
			//INCLUSION_EXCLUSION_INSERT
			if(!(CollectionUtils.isEmpty(bean.getIncludedList()))){
				for(int i=0;i<bean.getIncludedList().size();i++){
					String args[]=new String[11];
					args[0] =bean.getProposalNo();
					args[1] =bean.getContractNo();
					args[2] =bean.getLayerNo();
					args[3] = tranId;
					args[4] = bean.getIncludedList().get(i).getValue1();
					args[5] = bean.getIncludedList().get(i).getValue2();
					args[6] = "I";
					args[7] = amendId;
					args[8] = bean.getBranchCode();
					args[9] = bean.getEffectiveDate();
					args[10] = bean.getLoginId();
					TtrnIeModule insert = nonProportCustomRepository.inclusionExclusionInsert(args);
					if(insert!=null) {
						ttrnIeModuleRepository.saveAndFlush(insert)		;
						}
				}
			}
			if(!(CollectionUtils.isEmpty(bean.getExcludedList()))){
				for(int i=0;i<bean.getExcludedList().size();i++){
					String args[]=new String[11];
					args[0] =bean.getProposalNo();
					args[1] =bean.getContractNo();
					args[2] =bean.getLayerNo();
					args[3] = tranId;
					args[4] =bean.getExcludedList().get(i).getValue1();
					args[5] = bean.getExcludedList().get(i).getValue2();
					args[6] = "E";
					args[7] = amendId;
					args[8] = bean.getBranchCode();
					args[9] = bean.getEffectiveDate();
					args[10] = bean.getLoginId();
					TtrnIeModule insert = nonProportCustomRepository.inclusionExclusionInsert(args);
					if(insert!=null) {
						ttrnIeModuleRepository.saveAndFlush(insert)		;
						}
				}
			}
			if(StringUtils.isNotBlank(bean.getExcludeProposalNo())){
				String[] value=bean.getExcludeProposalNo().split(",");
				for(int i=0;i<value.length;i++){
					String args[]=new String[11];
					args[0] =bean.getProposalNo();
					args[1] =bean.getContractNo();
					args[2] =bean.getLayerNo();
					args[3] = tranId;
					args[4] = "7";
					args[5] = value[i];
					args[6] = "E";
					args[7] = amendId;
					args[8] = bean.getBranchCode();
					args[9] = bean.getEffectiveDate();
					args[10] = bean.getLoginId();
					TtrnIeModule insert = nonProportCustomRepository.inclusionExclusionInsert(args);
					if(insert!=null) {
						ttrnIeModuleRepository.saveAndFlush(insert)		;
						}
					 }
			}
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
	public ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req) {
		ShowSecondpageEditItemsRes response = new ShowSecondpageEditItemsRes();
		ShowSecondpageEditItemsRes1 res = new ShowSecondpageEditItemsRes1();
		GetRetroContractDetailsListReq req1 = new GetRetroContractDetailsListReq();
		req1.setBranchCode(req.getBranchCode());
		req1.setIncepDate(req.getIncepDate());
		req1.setProductid(req.getProductId());
		try{
			
			if ("3".equalsIgnoreCase(req.getProductId())||"5".equalsIgnoreCase(req.getProductId())) {
			
				//UPDATE_LAYER_NO
				TtrnRiskCommission list = ttrnRiskCommissionRepository.findByRskProposalNumberAndRskEndorsementNo(req.getProposalNo(), new BigDecimal(getMaxAmednId(req.getProposalNo())));	
				if(list!=null) {
					list.setRskLayerNo(StringUtils.isBlank(req.getLayerLayerNo())?new BigDecimal(req.getLayerNo()):new BigDecimal(req.getLayerLayerNo()));
				}
				String layerNo = StringUtils.isBlank(req.getLayerLayerNo())?req.getLayerNo():req.getLayerLayerNo();
				//risk.select.getEditModeSecPagePro3Data
				List<Tuple> list1 = nonProportCustomRepository.riskSelectGetEditModeSecPagePro3Data(req.getProposalNo(),layerNo);
		
				Tuple resMap = null;
				if(list1!=null && list1.size()>0) {
					resMap = list1.get(0);
				
							if (resMap.get("RSK_LIMIT_OS_OC") != null) {
								res.setLimitOurShare(resMap.get("RSK_LIMIT_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LIMIT_OS_OC").toString());
								res.setLimitOSViewOC(resMap.get("RSK_LIMIT_OS_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_LIMIT_OS_OC").toString()));
							}
							if (resMap.get("RSK_LIMIT_OS_DC") != null) {
								res.setLimitOSViewDC(resMap.get("RSK_LIMIT_OS_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_LIMIT_OS_DC").toString()));
							}
							if (resMap.get("RSK_EPI_OSOF_OC") != null) {
								res.setEpiAsPerOffer(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOF_OC").toString());
								res.setEpiOSViewOC(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_EPI_OSOF_OC").toString()));
							}
							if (resMap.get("RSK_EPI_OSOF_DC") != null) {
								res.setEpiOSViewDC(resMap.get("RSK_EPI_OSOF_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_EPI_OSOF_DC").toString()));
							}
							if (resMap.get("RSK_BROKERAGE") != null) {
								res.setBrokerage(resMap.get("RSK_BROKERAGE").toString().equalsIgnoreCase("0") ? "0.00000000" : fm.formattereight(resMap.get("RSK_BROKERAGE").toString()));
							}
							if (resMap.get("RSK_TAX") != null) {
								res.setTax(resMap.get("RSK_TAX").toString().equalsIgnoreCase("0") ? "0.00000000"	: fm.formattereight(resMap.get("RSK_TAX").toString()));
							}
							if (resMap.get("RSK_ACQUISTION_COST_OC") != null) {
								res.setAcquisitionCost(resMap.get("RSK_ACQUISTION_COST_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_ACQUISTION_COST_OC").toString());
							}
							if (resMap.get("RSK_PROFIT_COMM") != null) {
								res.setShareProfitCommission(resMap.get("RSK_PROFIT_COMM").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PROFIT_COMM").toString());
							}
							if (resMap.get("RSK_MD_PREM_OS_OC") != null) {
								res.setMdpremiumourservice(resMap.get("RSK_MD_PREM_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_MD_PREM_OS_OC").toString());
								res.setMandDpreViewOC(resMap.get("RSK_MD_PREM_OS_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_MD_PREM_OS_OC").toString()));
							}
							if (resMap.get("RSK_MD_PREM_OS_DC") != null) {
								res.setMandDpreViewDC(resMap.get("RSK_MD_PREM_OS_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_MD_PREM_OS_DC").toString()));
							}
							if(StringUtils.isBlank(req.getAnualAggregateLiability())  ) {
								if (resMap.get("RSK_AGGREGATE_LIAB_OC") != null) {
									res.setAnualAggregateLiability(resMap.get("RSK_AGGREGATE_LIAB_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_AGGREGATE_LIAB_OC").toString()));
								}
							}
							if (resMap.get("RSK_REINSTATE_NO") != null) {
								res.setReinstNo(resMap.get("RSK_REINSTATE_NO").toString().equalsIgnoreCase("0") ? "" : resMap.get("RSK_REINSTATE_NO").toString());
							}
							if (resMap.get("RSK_REINSTATE_ADDL_PREM_OC") != null) {
								res.setReinstAdditionalPremium(resMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString());
							}
							if (resMap.get("RSK_LEAD_UW") != null) {
								res.setLeaderUnderwriter(resMap.get("RSK_LEAD_UW")==null ? "0" : resMap.get("RSK_LEAD_UW").toString());
							}
							if (resMap.get("RSK_LEAD_UW_SHARE") != null) {
								res.setLeaderUnderwritershare(resMap.get("RSK_LEAD_UW_SHARE")==null ? "0.00000000" :fm.formattereight(resMap.get("RSK_LEAD_UW_SHARE").toString()));
							}
							if (resMap.get("RSK_ACCOUNTS") != null) {
								res.setAccounts(resMap.get("RSK_ACCOUNTS").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_ACCOUNTS").toString());
							}
							if (resMap.get("RSK_EXCLUSION") != null) {
								res.setExclusion(resMap.get("RSK_EXCLUSION").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EXCLUSION").toString());
							}
							res.setReInstatementPremium(resMap.get("RSK_REINSTATEMENT_PREMIUM")==null?"":resMap.get("RSK_REINSTATEMENT_PREMIUM").toString());
							res.setRemarks(resMap.get("RSK_REMARKS")==null?"":resMap.get("RSK_REMARKS").toString());
							res.setUnderwriterRecommendations(resMap.get("RSK_UW_RECOMM")==null?"":resMap.get("RSK_UW_RECOMM").toString());
							res.setGmsApproval(resMap.get("RSK_GM_APPROVAL")==null?"":resMap.get("RSK_GM_APPROVAL").toString());
							if (resMap.get("RSK_OTHER_COST") != null) {
								res.setOthercost(resMap.get("RSK_OTHER_COST").toString().equalsIgnoreCase("0") ? "0.00000000" : fm.formattereight(resMap.get("RSK_OTHER_COST").toString()));
							}
							if (resMap.get("RSK_REINSTATE_ADDL_PREM_PCT") != null) {
								res.setReinstAditionalPremiumpercent(resMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString());
							}
							if (resMap.get("RSK_BURNING_COST_PCT") != null) {
								res.setBurningCost(resMap.get("RSK_BURNING_COST_PCT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_BURNING_COST_PCT").toString());
							}
					//	}
						if (resMap.get("RSK_LIMIT_OS_DC") != null) {
							res.setLimitOSViewDC(resMap.get("RSK_LIMIT_OS_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_LIMIT_OS_DC").toString()));
						}
						if (resMap.get("RSK_EPI_OSOF_OC") != null) {
							res.setEpiAsPerOffer(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EPI_OSOF_OC").toString());
							res.setEpiOSViewOC(resMap.get("RSK_EPI_OSOF_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_EPI_OSOF_OC").toString()));
						}
						if (resMap.get("RSK_EPI_OSOF_DC") != null) {
							res.setEpiOSViewDC(resMap.get("RSK_EPI_OSOF_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_EPI_OSOF_DC").toString()));
						}
						if (resMap.get("RSK_BROKERAGE") != null) {
							res.setBrokerage(resMap.get("RSK_BROKERAGE").toString().equalsIgnoreCase("0") ? "0.00000000" :fm.formattereight(resMap.get("RSK_BROKERAGE").toString()));
						}
						if (resMap.get("RSK_TAX") != null) {
							res.setTax(resMap.get("RSK_TAX").toString().equalsIgnoreCase("0") ? "0.00000000"	: fm.formattereight(resMap.get("RSK_TAX").toString()));
						}
						if (resMap.get("RSK_ACQUISTION_COST_OC") != null) {
							res.setAcquisitionCost(resMap.get("RSK_ACQUISTION_COST_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_ACQUISTION_COST_OC").toString());
						}
						if (resMap.get("RSK_PROFIT_COMM") != null) {
							res.setShareProfitCommission(resMap.get("RSK_PROFIT_COMM").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_PROFIT_COMM").toString());
						}
						if (resMap.get("RSK_MD_PREM_OS_OC") != null) {
							res.setMdpremiumourservice(resMap.get("RSK_MD_PREM_OS_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_MD_PREM_OS_OC").toString());
							res.setMandDpreViewOC(resMap.get("RSK_MD_PREM_OS_OC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_MD_PREM_OS_OC").toString()));
						}
						if (resMap.get("RSK_MD_PREM_OS_DC") != null) {
							res.setMandDpreViewDC(resMap.get("RSK_MD_PREM_OS_DC").toString().equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_MD_PREM_OS_DC").toString()));
						}
						if (resMap.get("RSK_AGGREGATE_LIAB_OC") != null) {
							res.setAnualAggregateLiability(resMap.get("RSK_AGGREGATE_LIAB_OC").toString().equalsIgnoreCase("0") ? "0.00" :fm.formatter(resMap.get("RSK_AGGREGATE_LIAB_OC").toString()));
						}
						if (resMap.get("RSK_AGGREGATE_DEDUCT_OC") != null) {
							res.setAnualAggregateDeduct(resMap.get("RSK_AGGREGATE_DEDUCT_OC").toString()
									.equalsIgnoreCase("0") ? "0.00" : fm.formatter(resMap.get("RSK_AGGREGATE_DEDUCT_OC").toString()));
						}
						if (resMap.get("RSK_REINSTATE_NO") != null) {
							res.setReinstNo(resMap.get("RSK_REINSTATE_NO").toString().equalsIgnoreCase("0") ? "" : resMap.get("RSK_REINSTATE_NO").toString());
						}
						if (resMap.get("RSK_REINSTATE_ADDL_PREM_OC") != null) {
							res.setReinstAdditionalPremium(resMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_REINSTATE_ADDL_PREM_OC").toString());
						}
						if (resMap.get("RSK_LEAD_UW") != null) {
							res.setLeaderUnderwriter(resMap.get("RSK_LEAD_UW").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_LEAD_UW").toString());
						}
						res.setLeaderUnderwritercountry(resMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":resMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
						res.setCrestaStatus(resMap.get("RSK_CREASTA_STATUS")==null?"":resMap.get("RSK_CREASTA_STATUS").toString());
						if (resMap.get("RSK_LEAD_UW_SHARE") != null) {
							res.setLeaderUnderwritershare(resMap.get("RSK_LEAD_UW_SHARE").toString().equalsIgnoreCase("0") ? "0.00000000" : fm.formattereight(resMap.get("RSK_LEAD_UW_SHARE").toString()));
						}
						if (resMap.get("RSK_ACCOUNTS") != null) {
							res.setAccounts(resMap.get("RSK_ACCOUNTS").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_ACCOUNTS").toString());
						}
						if (resMap.get("RSK_EXCLUSION") != null) {
							res.setExclusion(resMap.get("RSK_EXCLUSION").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_EXCLUSION").toString());
						}
						res.setReInstatementPremium(resMap.get("RSK_REINSTATEMENT_PREMIUM")==null?"":resMap.get("RSK_REINSTATEMENT_PREMIUM").toString());
						res.setRemarks(resMap.get("RSK_REMARKS")==null?"":resMap.get("RSK_REMARKS").toString());
						res.setUnderwriterRecommendations(resMap.get("RSK_UW_RECOMM")==null?"":resMap.get("RSK_UW_RECOMM").toString());
						res.setGmsApproval(resMap.get("RSK_GM_APPROVAL")==null?"":resMap.get("RSK_GM_APPROVAL").toString());
						if (resMap.get("RSK_OTHER_COST") != null) {
							res.setOthercost(resMap.get("RSK_OTHER_COST").toString().equalsIgnoreCase("0") ? "0.00000000" :fm.formattereight(resMap.get("RSK_OTHER_COST").toString()));
						}
						if (resMap.get("RSK_REINSTATE_ADDL_PREM_PCT") != null) {
							res.setReinstAditionalPremiumpercent(resMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_REINSTATE_ADDL_PREM_PCT").toString());
						}
						if (resMap.get("RSK_BURNING_COST_PCT") != null) {
							res.setBurningCost(resMap.get("RSK_BURNING_COST_PCT").toString().equalsIgnoreCase("0") ? "0" : resMap.get("RSK_BURNING_COST_PCT").toString());
						}
						if (resMap.get("RSK_BONUS_ID") != null) {
							res.setAcqBonus(resMap.get("RSK_BONUS_ID").toString().equalsIgnoreCase("") ? "" : resMap.get("RSK_BONUS_ID").toString());
						}
						if (resMap.get("RSK_NOCLAIMBONUS_PRCENT") != null) {
							res.setAcqBonusPercentage(resMap.get("RSK_NOCLAIMBONUS_PRCENT").toString().equalsIgnoreCase("") ? "0.00000000" : fm.formattereight(resMap.get("RSK_NOCLAIMBONUS_PRCENT").toString()));
						}
						if (resMap.get("RSK_OCCURRENT_LIMIT_OC") != null) {
							res.setOccurrentLimit(resMap.get("RSK_OCCURRENT_LIMIT_OC").toString().equalsIgnoreCase("") ? "" : resMap.get("RSK_OCCURRENT_LIMIT_OC").toString());
						}
					
				}
				int count = 0;
				//GET_INSTALMENT_COUNT
				count = nonProportCustomRepository.getInstalmentCount(req.getProposalNo(),req.getLayerNo());
				String[] args = null;
				if(req.getMdInstalmentNumber().equalsIgnoreCase(Integer.toString(count))){
					args = new String[4];
					args[0] = req.getProposalNo();
					args[1] = StringUtils.isBlank(req.getLayerLayerNo())? req.getLayerNo():req.getLayerLayerNo();
					args[2] = req.getProposalNo();
					args[3] = StringUtils.isBlank(req.getLayerLayerNo())? req.getLayerNo():req.getLayerLayerNo();
				}else{
				args = new String[4];
				args[0] = req.getProposalNo();
				args[1] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
				args[2] = req.getProposalNo();
				args[3] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
				}
				//risk.select.getInstalmentData
				List<Tuple> instalmentList = nonProportCustomRepository.riskSelectGetInstalmentData(args);
				
				List<InstalmentListRes> instalmentResList= new ArrayList<InstalmentListRes>();
				if (instalmentList != null && instalmentList.size()>0) {
					for (int k = 0; k < instalmentList.size(); k++) {
						InstalmentListRes instalmentRes = new InstalmentListRes();
						Tuple insMap = instalmentList.get(k);
						instalmentRes.setInstalmentDateList(insMap.get("INSTALLMENT_DATE")==null?"":DateFormat(insMap.get("INSTALLMENT_DATE")));
						instalmentRes.setPaymentDueDays((insMap.get("PAYEMENT_DUE_DAY")==null?"":insMap.get("PAYEMENT_DUE_DAY").toString()));
						instalmentRes.setInstalList(insMap.get("MND_PREMIUM_OC")==null?"":fm.formatter(insMap.get("MND_PREMIUM_OC").toString()));
						instalmentResList.add(instalmentRes);
						}
					res.setInstalmentList(instalmentResList);					
				}else{
					if(StringUtils.isNotBlank(req.getMdInstalmentNumber())) {
					for (int k = 0; k <Integer.parseInt(req.getMdInstalmentNumber()); k++) {
						InstalmentListRes instalmentRes = new InstalmentListRes();
						instalmentRes.setPaymentDueDays(req.getPaymentDuedays());
						instalmentResList.add(instalmentRes);
					}
					res.setInstalmentList(instalmentResList);		
				}
			}
//			List<NoInsurerRes> noInsurerResList = new ArrayList<NoInsurerRes>();
//			if ("3".equalsIgnoreCase(req.getProductId())){
//				for(int i=0;i<Integer.parseInt(req.getNoInsurer());i++){
//					NoInsurerRes insurerRes = new NoInsurerRes();
//					if("3".equals(req.getProductId())){
//						insurerRes.setProductid("4");
//						insurerRes.setRetroType("TR");
//					}
//					insurerRes.setBranchCode(req.getBranchCode());
//					insurerRes.setIncepDate(req.getIncepDate());
//					GetRetroContractDetailsListRes commonRes=getRetroContractDetailsList(req1,1,"");
//					insurerRes.setRetroUwyear(commonRes.getCommonResponse());	
//					noInsurerResList.add(insurerRes);
//					}
//				res.setNoInsurerRes(noInsurerResList);	
//				}
			}
			response.setCommonResponse(res);	
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
	   				e.printStackTrace();
	   				response.setMessage("Failed");
	   				response.setIsError(true);
	   			}
	   		return response;
	}

	@Transactional
	@Override
	public CommonSaveRes insertRemarkDetails(RemarksSaveReq beanObj) {
		CommonSaveRes resp=new CommonSaveRes();
		int amendId=0;
		try {
			//DELETE_REMARKS_DETAILS
			nonProportCustomRepository.deleteRemarksDetails(beanObj.getProposalNo(),beanObj.getLayerNo());
			//GET_AMEND_REMARKS
			List<TtrnRiskRemarks> list = ttrnRiskRemarksRepository.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(beanObj.getProposalNo()));
			
			if(!CollectionUtils.isEmpty(list)) {
				amendId=list.get(0).getAmendId()==null?0:Integer.valueOf(list.get(0).getAmendId()+1);
			}
			if(!CollectionUtils.isEmpty(beanObj.getRemarksReq())) {
				
			for(int i=0;i<beanObj.getRemarksReq().size();i++){
				RemarksReq req=beanObj.getRemarksReq().get(i);
			
				String[] obj= new String[12];
				obj[0]=beanObj.getProposalNo();
				obj[1]=beanObj.getContractNo();
				obj[2]=beanObj.getLayerNo();
				obj[3]=beanObj.getDepartmentId();
				obj[4]=beanObj.getProductid();
				obj[5]=String.valueOf(amendId);
				obj[6]=String.valueOf(i+1);
				obj[7]=req.getDescription();
				obj[8]=req.getRemark1();
				obj[9]=req.getRemark2();
				obj[10]=beanObj.getLoginId();
				obj[11]=beanObj.getBranchCode();
				//INSERT_REMARKS_DETAILS
				TtrnRiskRemarks insert = nonProportCustomRepository.ttrnRiskRemarksInsert(obj);
				if(insert!=null) {
					ttrnRiskRemarksRepository.saveAndFlush(insert);	
				}
			}
			resp.setResponse("Success");
			resp.setErroCode(0);
			resp.setIsError(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setResponse("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}

	@Override
	public GetClassLimitDetailsRes getClassLimitDetails(GetClassLimitDetailsReq req) {
		GetClassLimitDetailsRes response = new GetClassLimitDetailsRes();
		GetClassLimitDetailsResponse com = new GetClassLimitDetailsResponse();
		List<GetClassLimitDetailsRes1> resList = new ArrayList<GetClassLimitDetailsRes1>();
		try {
			String layerNo = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
			//GET_CLASS_LIMIT_DETAILS
			List<Tuple> result = nonProportCustomRepository.getClassLimitDetails(req.getProposalNo(),layerNo); //RI
			if(StringUtils.isNotBlank(req.getBusinessType())) {
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					Tuple insMap = result.get(i);
					GetClassLimitDetailsRes1 res = new GetClassLimitDetailsRes1();
					if(!req.getBusinessType().equalsIgnoreCase("5") && !req.getBusinessType().equalsIgnoreCase("Stop Loss")){
						res.setCoverdepartId(insMap.get("RSK_COVER_CLASS")==null?"":insMap.get("RSK_COVER_CLASS").toString());
						res.setCoverdepartIdRe(insMap.get("RSK_COVER_CLASS")==null?"":insMap.get("RSK_COVER_CLASS").toString());
						 if(StringUtils.isNotBlank(req.getReinstatementOption()) && "U".equalsIgnoreCase(req.getReinstatementOption())){
							 res.setHcoverLimitOC(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
							 res.setHcoverLimitOCRe(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
						 }else{
							 res.setCoverLimitAmount(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
							 res.setHcoverLimitOC(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
							 res.setCoverLimitAmountRe(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
							 res.setHcoverLimitOCRe(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
						 }
						 	res.setDeductableLimitPercent(insMap.get("RSK_DEDUCTABLE_PERCENTAGE")==null?"0.00":insMap.get("RSK_DEDUCTABLE_PERCENTAGE").toString());
							res.setEgnpi(insMap.get("RSK_EGNPI_AS_OFF")==null?"0.00":fm.formatter(insMap.get("RSK_EGNPI_AS_OFF").toString()));
							res.setGnpi(insMap.get("RSK_GNPI_AS_OFF")==null?"0.00":fm.formatter(insMap.get("RSK_GNPI_AS_OFF").toString()));	
							res.setNetMaxRetentPer(insMap.get("RSK_NET_MAX_RETENT_PERCENT")==null?"0.00":fm.formatter(insMap.get("RSK_NET_MAX_RETENT_PERCENT").toString()));
							res.setDeductableLimitAmount(insMap.get("RSK_DEDUCTABLE_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_DEDUCTABLE_LIMT").toString()));
							res.setTotalLoopCount(String.valueOf(i+1));
					}else{
						res.setCoverdepartId(insMap.get("RSK_COVER_CLASS")==null?"":insMap.get("RSK_COVER_CLASS").toString());	
						res.setCoverLimitAmount(insMap.get("RSK_COVER_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_COVER_LIMT").toString()));
						res.setCoverLimitPercent(insMap.get("RSK_COVER_LIMT_PERCENTAGE")==null?"0.00":insMap.get("RSK_COVER_LIMT_PERCENTAGE").toString());
						res.setDeductableLimitAmount(insMap.get("RSK_DEDUCTABLE_LIMT")==null?"0.00":fm.formatter(insMap.get("RSK_DEDUCTABLE_LIMT").toString()));
						res.setDeductableLimitPercent(insMap.get("RSK_DEDUCTABLE_PERCENTAGE")==null?"0.00":insMap.get("RSK_DEDUCTABLE_PERCENTAGE").toString());
						res.setEgnpi(insMap.get("RSK_EGNPI_AS_OFF")==null?"0.00":fm.formatter(insMap.get("RSK_EGNPI_AS_OFF").toString()));
						res.setGnpi(insMap.get("RSK_GNPI_AS_OFF")==null?"0.00":fm.formatter(insMap.get("RSK_GNPI_AS_OFF").toString()));	
						res.setNetMaxRetentPer(insMap.get("RSK_NET_MAX_RETENT_PERCENT")==null?"0.00":fm.formatter(insMap.get("RSK_NET_MAX_RETENT_PERCENT").toString()));		
						res.setGnpi(insMap.get("RSK_GNPI_AS_OFF")==null?"0.00":fm.formatter(insMap.get("RSK_GNPI_AS_OFF").toString()));
						res.setTotalLoopCount(String.valueOf(i+1));
					}
					
					if("copy".equals(req.getFlag())) {
						
						String proposalno=StringUtils.isBlank(req.getBaseLayer())?req.getProposalNo():req.getBaseLayer();
						int layermax=0;
						//GET_MAX_LAYER_NO
						layermax = 	nonProportCustomRepository.getMaxLayerNo(proposalno);
						if(Integer.parseInt(req.getLayerNo())>=layermax) {
								
								String coverAmount=insMap.get("RSK_COVER_LIMT")==null?"0.00":insMap.get("RSK_COVER_LIMT").toString();
								String coverAmount1=insMap.get("RSK_DEDUCTABLE_LIMT")==null?"0.00":insMap.get("RSK_DEDUCTABLE_LIMT").toString();
								String deducttotal=String.valueOf(Double.parseDouble(coverAmount1)+Double.parseDouble(coverAmount));
								res.setDeductableLimitAmount(fm.formatter(deducttotal));
								res.setCoverLimitAmount("");
						}else {
							res.setDeductableLimitAmount("");
							res.setCoverLimitAmount("");
							
						}
						
					}
					
					resList.add(res);
					}
			}}
			else {
				resList.add(new GetClassLimitDetailsRes1());

			}
			
			com.setClassLimitDetails(resList);
			response.setCommonResponse(com);	
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
	public SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(SaveSecondPageReq beanObj) {
		SaveRiskDeatilsSecondFormRes response = new SaveRiskDeatilsSecondFormRes();
		SaveRiskDeatilsSecondFormRes1 res = new SaveRiskDeatilsSecondFormRes1();
		try {
			String[] obj=null,obj1=null;
			String[] args=null;
			int out=0;
			int chkSecPageMode = checkSecondPageMode(beanObj.getProductId(), beanObj.getProposalNo());
			int ContractEditMode = contractEditMode(beanObj.getProposalNo());
			//if(!"Renewal".equalsIgnoreCase(beanObj.getProposalReference())&& !"Layer".equalsIgnoreCase(beanObj.getProposalReference())){
			
			if (ContractEditMode == 1) {
				if (chkSecPageMode == 1) {
					obj = secondPageFirstTableAruguments(beanObj, beanObj.getProductId(), getMaxAmednId(beanObj.getProposalNo()));
					
					//risk.update.pro35RskProposal
					TtrnRiskProposal update = nonProportCustomRepository.riskUpdatePro35RskProposal(obj);
					if(update!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update);	
					out=1;
					}
					
					obj = secondPageCommissionAruguments(beanObj,beanObj.getProductId());
					if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())) {
						//risk.insert.pro3SecComm //risk.insert.pro5SecComm
						TtrnRiskCommission insert = nonProportCustomRepository.riskInsertPro3SecComm(obj1);
						if(insert!=null) {
							ttrnRiskCommissionRepository.saveAndFlush(insert);	
							out = 1;
						}
					}
//					else if("5".equalsIgnoreCase(beanObj.getProductId())) {
//						//risk.insert.pro5SecComm
//						TtrnRiskCommission insert = nonProportCustomRepository.riskInsertPro5SecComm(obj1);
//						if(insert!=null) {
//							ttrnRiskCommissionRepository.saveAndFlush(insert);	
//						}
					//risk.select.chechProposalStatus
					List<Tuple> list = nonProportCustomRepository.riskSelectChechProposalStatus(beanObj.getProposalNo());
					Tuple resMap = null;
					if(list!=null && list.size()>0)
						resMap = list.get(0);
					if(resMap!=null){
						res.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
						res.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
						res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
						if (res.getProStatus().matches("A")	&& !res.getSharSign().matches("0")) {
							String maxContarctNo = null;
							String prodid=beanObj.getProductId();
							if ("layer".equalsIgnoreCase(beanObj.getLayerNo())) {
								maxContarctNo = res.getContNo();
							} else {
								if(!"".equals(beanObj.getRenewalcontractno())&&!"0".equals(beanObj.getRenewalcontractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									maxContarctNo=beanObj.getRenewalcontractno();
								}else{
										maxContarctNo=fm.getSequence("Contract","3".equalsIgnoreCase(prodid)?prodid:"6",beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear());
									
								}
							}
							//risk.update.contNo
							List<TtrnRiskDetails> update1 = ttrnRiskDetailsRepository.findByRskProposalNumber(beanObj.getProposalNo());	
							if(update1.size()>0) {
								TtrnRiskDetails u =update1.get(0);		
								u.setRskContractNo(maxContarctNo);				
								ttrnRiskDetailsRepository.saveAndFlush(u);
							}	
							if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())){
								//CLASS_LIMIT_UPDATE_CONTNO
								List<TtrnRskClassLimits> updat = ttrnRskClassLimitsRepository.findByRskProposalNumber(new BigDecimal(beanObj.getProposalNo()));	
								if(updat.size()>0) {
									TtrnRskClassLimits u =updat.get(0);		
									u.setRskContractNo(new BigDecimal(maxContarctNo));				
									ttrnRskClassLimitsRepository.saveAndFlush(u);
								}	
							}
							if("5".equalsIgnoreCase(beanObj.getProductId())){
								//GET_RETRO_CON_UPDATE
								nonProportCustomRepository.getRetroConUpdate(maxContarctNo,beanObj.getProposalNo(),beanObj.getLayerNo());
								}
							
							args = new String[4];
							args[0] = maxContarctNo;
							res.setContNo((String)args[0]);
							args[1] = "A";
							args[2] = "A";
							args[3] = beanObj.getProposalNo();
							
							//risk.update.homeContNo
							nonProportCustomRepository.riskUpdateHomeContNo(args);
							beanObj.setContractNo(maxContarctNo);
							
							
							if(StringUtils.isBlank(beanObj.getRenewalcontractno())||"0".equals(beanObj.getRenewalcontractno())||"NEWCONTNO".equals(beanObj.getRenewalFlag())){
								res.setContractGendration("Your Proposal is converted to Contract with Proposal No : "+beanObj.getProposalNo() +" and Contract No : "+maxContarctNo+".");
							}else{
								res.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+" and New Contract No : "+maxContarctNo+".");
							}
						} else {
							args = new String[4];
							args[0] = beanObj.getContractNo();
							args[1] = getproposalStatus(beanObj.getProposalNo());
							args[2] = args[1];
							if (args != null) {

								if (((String) args[1]).equalsIgnoreCase("P")) {
									res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No. :"+beanObj.getLayerNo());
								}	if (((String) args[1]).equalsIgnoreCase("N")) {
									res.setContractGendration("Your Proposal is saved in  Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No. :"+beanObj.getLayerNo());

								}  else if (((String) args[1]).equalsIgnoreCase("A")) {

									res.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+beanObj.getContractNo()+" and Layer No : "+beanObj.getLayerNo()+".");

								} else if (((String) args[1]).equalsIgnoreCase("R")) {
									res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo());
								}
							}
							args[3] = beanObj.getProposalNo();
							
							int k=0;
							for(Object str:args) {//risk.update.homeContNo
								nonProportCustomRepository.riskUpdateHomeContNo(args);}
						}
					}
				}
				else if (chkSecPageMode == 2 ) {
					obj =updateRiskDetailsSecondForm(beanObj, getMaxAmednId(beanObj.getProposalNo()));
					//risk.update.pro35RskProposal
					TtrnRiskProposal update = nonProportCustomRepository.riskUpdatePro35RskProposal(obj);
					if(update!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update);	
					out=1;
					}
					obj1 = updateRiskDetailsSecondFormSecondTable(beanObj, beanObj.getProductId());
					if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())) {
						//risk.update.pro3SecComm
						TtrnRiskCommission update1 = nonProportCustomRepository.riskUpdatePro3SecComm(obj1);
						if(update1!=null) {
							ttrnRiskCommissionRepository.saveAndFlush(update1);	
						}
					}
//					else if("5".equalsIgnoreCase(req.getProductId())) {
//						//risk.update.pro5SecComm
//						TtrnRiskCommission update1 = nonProportCustomRepository.riskUpdatePro5SecComm(obj1);
//						if(update1!=null) {
//							ttrnRiskCommissionRepository.saveAndFlush(update1);	
//						} }
					//risk.select.chechProposalStatus
					List<Tuple> list = nonProportCustomRepository.riskSelectChechProposalStatus(beanObj.getProposalNo());
					Tuple resMap = null;
					if(list!=null && list.size()>0)
						resMap =list.get(0);
					if (resMap != null) {
						for (int i = 0; i < list.size(); i++) {
							res.setProStatus(resMap.get("RSK_STATUS")==null?"":resMap.get("RSK_STATUS").toString());
							res.setSharSign(resMap.get("RSK_SHARE_SIGNED")==null?"":resMap.get("RSK_SHARE_SIGNED").toString());
							res.setContNo(resMap.get("RSK_CONTRACT_NO")==null?"":resMap.get("RSK_CONTRACT_NO").toString());
						}
					}
					if (res.getProStatus().matches("A")	&& !res.getSharSign().matches("0")) {
						String prodid=beanObj.getProductId();

						String maxContarctNo=""; 
						if ("layer".equalsIgnoreCase(beanObj.getLayerNo())) {
							maxContarctNo = beanObj.getContractNo();
						} else {
						if(!"".equals(beanObj.getRenewalcontractno())&&!"0".equals(beanObj.getRenewalcontractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
							maxContarctNo=beanObj.getRenewalcontractno(); 
						}else{
								maxContarctNo=fm.getSequence("Contract","3".equalsIgnoreCase(prodid)?prodid:"6",beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getUwYear());
							
						}
						}
						//risk.update.contNo
						List<TtrnRiskDetails> update1 = ttrnRiskDetailsRepository.findByRskProposalNumber(beanObj.getProposalNo());	
						if(update1.size()>0) {
							TtrnRiskDetails u =update1.get(0);		
							u.setRskContractNo(maxContarctNo);				
							ttrnRiskDetailsRepository.saveAndFlush(u);
						}	
						if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())){
							//CLASS_LIMIT_UPDATE_CONTNO
							List<TtrnRskClassLimits> updat = ttrnRskClassLimitsRepository.findByRskProposalNumber(new BigDecimal(beanObj.getProposalNo()));	
							if(updat.size()>0) {
								TtrnRskClassLimits u =updat.get(0);		
								u.setRskContractNo(new BigDecimal(maxContarctNo));				
								ttrnRskClassLimitsRepository.saveAndFlush(u);
							}	
						}
						if("5".equalsIgnoreCase(beanObj.getProductId())){
							//GET_RETRO_CON_UPDATE
							nonProportCustomRepository.getRetroConUpdate(maxContarctNo,beanObj.getProposalNo(),beanObj.getLayerNo());
						}
						args = new String[4];
						args[0] = maxContarctNo;
						args[1] = getMaxproposalStatus(beanObj.getProposalNo());
						args[2] = args[1];
						args[3] = beanObj.getProposalNo();
						//risk.update.homeContNo
						nonProportCustomRepository.riskUpdateHomeContNo(args);
						if("".equals(beanObj.getRenewalcontractno())||"0".equals(beanObj.getRenewalcontractno())||"NEWCONTNO".equals(beanObj.getRenewalFlag())){

							res.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+maxContarctNo+" and Layer No.: "+beanObj.getLayerNo()+".");
						}else{

							res.setContractGendration("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No:"+maxContarctNo+",New Contract No : "+maxContarctNo+" and Layer No.: "+beanObj.getLayerNo()+".");

						}
						//risk.update.mndInstallments
						TtrnMndInstallments list4 = ttrnMndInstallmentsRepository.findByProposalNo(beanObj.getProposalNo());
						if(list4!=null) {
							list4.setContractNo(maxContarctNo);	
							ttrnMndInstallmentsRepository.saveAndFlush(list4);
							}
						res.setContNo(maxContarctNo);
						beanObj.setContractNo(maxContarctNo);
					} else if (beanObj.getProStatus().matches("P")) {

						res.setContractGendration("Your Proposal is saved in Pending Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");

					}else if (beanObj.getProStatus().matches("N")) {

						res.setContractGendration("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");
					}
					else if (beanObj.getProStatus().matches("R")) {
						res.setContractGendration("Your Proposal is saved in Rejected Stage with Proposal No : "+ beanObj.getProposalNo()+" and Layer No.:"+beanObj.getLayerNo()+".");
					}
				}
			}
			else if (ContractEditMode == 2 ) {
				String endtNo = "";
				//risk.select.endo
				TtrnRiskProposal rp = ttrnRiskProposalRepository.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(beanObj.getProposalNo());
				if(rp!=null) {
					endtNo = rp.getRskEndorsementNo()==null?"":	rp.getRskEndorsementNo().toString();
					}
				obj = updateContractRiskDetailsSecondForm(beanObj, endtNo);
				//risk.update.pro35ContSecPage
				TtrnRiskProposal update2 = nonProportCustomRepository.riskUpdatePro35ContSecPage(obj);
				if(update2!=null) {
					ttrnRiskProposalRepository.saveAndFlush(update2);	
				}
				res.setContractGendration("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+", Contract No : "+beanObj.getContractNo()+" and Layer No : "+beanObj.getLayerNo()+".");

				obj1 = updateRiskDetailsSecondFormSecondTable(beanObj, beanObj.getProductId());
				if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())) {
					//risk.update.pro3SecComm
					TtrnRiskCommission update1 = nonProportCustomRepository.riskUpdatePro3SecComm(obj1);
					if(update1!=null) {
						ttrnRiskCommissionRepository.saveAndFlush(update1);	
					}
				}
				
				
				beanObj.setProStatus("A");
			}
//			if("5".equalsIgnoreCase(beanObj.getProductId())){
//				insertRetroCess(beanObj);
//			}
//			instalMentPremium(beanObj);
//			if("3".equalsIgnoreCase(productId)){
//				insertRetroContracts(beanObj,productId);
//			}
//			reInstatementMainInsert(beanObj);
//			insertCrestaMaintable(beanObj);
//			insertBonusDetails(beanObj);
//			InsertRemarkDetails(beanObj);
//			DropDownControllor dropDownController = new DropDownControllor();
//			dropDownController.updatepositionMasterEndtStatus(beanObj.getProposal_no(),productId,beanObj.getEndorsementDate(),"");
			response.setCommonResponse(res);	
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
	   				e.printStackTrace();
	   				response.setMessage("Failed");
	   				response.setIsError(true);
	   			}
	   		return response;
	}
	
	public int contractEditMode(String proposalNo) {
		int mode=0;
		String result = null;
		try {
			//risk.select.getRskContractNo
			result=nonProportCustomRepository.riskSelectGetRskContractNo(proposalNo);
			if ("0".equalsIgnoreCase(result)) {
				mode = 1;
			} else {
				mode = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mode;
	}
	public String[] secondPageFirstTableAruguments(
			final SaveSecondPageReq beanObj, final String productId, final String endNo) {
		String[] obj=null;
		obj = new String[9];
		obj[0] = beanObj.getLimitOurShare();
		obj[1] = getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[2] = beanObj.getEpiAsPerOffer();
		obj[3] = getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[4] = beanObj.getMdpremiumourservice();
		obj[5] = getDesginationCountry(beanObj.getMdpremiumourservice(),	beanObj.getExchangeRate());
		obj[6] = beanObj.getProposalNo();
		obj[7] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
		obj[8] = endNo;
		return obj;
	}
	public String[] secondPageCommissionAruguments(final SaveSecondPageReq beanObj, final String productId) {
		String[] obj=null;
		if ("3".equalsIgnoreCase(productId) || "5".equalsIgnoreCase(productId)) {
			obj = new String[38];
			obj[0] =  beanObj.getProposalNo();
			obj[1] = "0";
			obj[2] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
			obj[3] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0"
					: beanObj.getBrokerage();
			obj[4] = beanObj.getTax();
			obj[5] = beanObj.getShareProfitCommission();
			obj[6] = "0";
			obj[7] = StringUtils.isEmpty(beanObj.getAcquisitionCost())?"0":beanObj.getAcquisitionCost().replaceAll(",", "");
			obj[8] = getDesginationCountry(StringUtils.isEmpty(beanObj.getAcquisitionCost())?"0":beanObj.getAcquisitionCost().replaceAll(",", ""),beanObj.getExchangeRate());
			obj[9] = beanObj.getAnualAggregateLiability().replaceAll(",", "");
			obj[10] = getDesginationCountry(beanObj.getAnualAggregateLiability().replaceAll(",", ""), beanObj.getExchangeRate());
			obj[11] = StringUtils.isEmpty(beanObj.getReinstNo()) ? "0": beanObj.getReinstNo();
			obj[12] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium()) ? "0" :beanObj.getReinstAdditionalPremium().replaceAll(",", "");
			obj[13] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getReinstAdditionalPremium().replaceAll(",", ""), beanObj.getExchangeRate());
			obj[14] = StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "0" : beanObj.getLeaderUnderwriter();
			obj[15] = StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0" : beanObj.getLeaderUnderwritershare();
			obj[16] = StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts();
			obj[17] = beanObj.getExclusion();
			obj[18] = beanObj.getRemarks();
			obj[19] = beanObj.getUnderwriterRecommendations();
			obj[20] = beanObj.getGmsApproval();
			obj[21] = "";
			obj[22] = "";
			obj[23] = beanObj.getOthercost();
			obj[24] = StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? "0" :beanObj.getReinstAditionalPremiumpercent();
			obj[25] = "";
			obj[26] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct()) ? "0" : beanObj.getAnualAggregateDeduct();
			obj[27] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAnualAggregateDeduct(), beanObj.getExchangeRate());
			obj[28] = StringUtils.isEmpty(beanObj.getOccurrentLimit()) ? "0" : beanObj.getOccurrentLimit().replaceAll(",", "");
			obj[29] = StringUtils.isEmpty(beanObj.getOccurrentLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getOccurrentLimit().replaceAll(",", ""), beanObj.getExchangeRate());
			obj[30] = beanObj.getReInstatementPremium();
			obj[31] = beanObj.getCrestaStatus();
			obj[32] = StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "0" : beanObj.getLeaderUnderwritercountry();
			obj[33] = beanObj.getLoginId();
			obj[34] = beanObj.getBranchCode();
			obj[35] =StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
			if(StringUtils.isNotBlank(beanObj.getAcqBonus())&&"NCB".equalsIgnoreCase(beanObj.getAcqBonus())){
				obj[36]=StringUtils.isEmpty(beanObj.getAcqBonusPercentage())? "": beanObj.getAcqBonusPercentage();
				}
				else{
					obj[36]="";
				}
			obj[37]=StringUtils.isEmpty(beanObj.getAcqBonus())? "": beanObj.getAcqBonus();

		}
		return obj;
	}
	public String[] updateRiskDetailsSecondForm(final SaveSecondPageReq beanObj, final String endNo) {
		String[] obj=null;
		obj = new String[9];
		obj[0] = beanObj.getLimitOurShare();
		obj[1] = getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[2] = beanObj.getEpiAsPerOffer();
		obj[3] = getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[4] = beanObj.getMdpremiumourservice();
		obj[5] = getDesginationCountry(beanObj.getMdpremiumourservice(),beanObj.getExchangeRate());
		obj[6] = beanObj.getProposalNo();
		obj[7] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
		obj[8] = endNo;
		return obj;
	}
	public String[] updateRiskDetailsSecondFormSecondTable(final SaveSecondPageReq beanObj, final String productId) {

		String[] obj=null;
		if ("3".equalsIgnoreCase(productId) || "5".equalsIgnoreCase(productId)) {
			obj = new String[35];
			obj[0] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0": beanObj.getBrokerage();
			obj[1] = beanObj.getTax();
			obj[2] = beanObj.getShareProfitCommission();
			obj[3] = StringUtils.isEmpty(beanObj.getAcquisitionCost()) ? "0": beanObj.getAcquisitionCost().replaceAll(",", "");
			obj[4] = getDesginationCountry(beanObj.getAcquisitionCost().replaceAll(",", ""),beanObj.getExchangeRate());
			obj[5] = beanObj.getAnualAggregateLiability().replaceAll(",", "");
			obj[6] = getDesginationCountry(beanObj.getAnualAggregateLiability().replaceAll(",", ""),beanObj.getExchangeRate());
			obj[7] = StringUtils.isEmpty(beanObj.getReinstNo()) ? "0": beanObj.getReinstNo();
			obj[8] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium()) ? "0" :beanObj.getReinstAdditionalPremium();
			obj[9] = StringUtils.isEmpty(beanObj.getReinstAdditionalPremium())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getReinstAdditionalPremium(), beanObj.getExchangeRate());
			obj[10] = StringUtils.isEmpty(beanObj.getLeaderUnderwriter()) ? "0" : beanObj.getLeaderUnderwriter();
			obj[11] =StringUtils.isEmpty(beanObj.getLeaderUnderwritershare()) ? "0" : beanObj.getLeaderUnderwritershare();
			obj[12] =StringUtils.isEmpty(beanObj.getAccounts()) ? "" : beanObj.getAccounts();
			obj[13] = beanObj.getExclusion();
			obj[14] = beanObj.getRemarks();
			obj[15] = beanObj.getUnderwriterRecommendations();
			obj[16] = beanObj.getGmsApproval();
			obj[17] = beanObj.getOthercost();
			obj[18] = StringUtils.isEmpty(beanObj.getReinstAditionalPremiumpercent()) ? "0" :beanObj.getReinstAditionalPremiumpercent();
			obj[19] = StringUtils.isEmpty(beanObj.getBurningCost()) ? "0": beanObj.getBurningCost();
			obj[20] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct()) ? "0" : beanObj.getAnualAggregateDeduct();
			obj[21] = StringUtils.isEmpty(beanObj.getAnualAggregateDeduct())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getAnualAggregateDeduct(), beanObj.getExchangeRate());
			obj[22] = StringUtils.isEmpty(beanObj.getOccurrentLimit()) ? "0" : beanObj.getOccurrentLimit().replaceAll(",", "");
			obj[23] = StringUtils.isEmpty(beanObj.getOccurrentLimit())|| StringUtils.isEmpty(beanObj.getExchangeRate()) ? "0": getDesginationCountry(beanObj.getOccurrentLimit().replaceAll(",", ""), beanObj.getExchangeRate());
			obj[24] = beanObj.getReInstatementPremium();
			obj[25] = beanObj.getCrestaStatus();
			obj[26] =StringUtils.isEmpty(beanObj.getLeaderUnderwritercountry()) ? "0" : beanObj.getLeaderUnderwritercountry();
			obj[27] = beanObj.getLoginId();
			obj[28] = beanObj.getBranchCode();
			obj[29] = StringUtils.isEmpty(beanObj.getDocStatus())? "" :beanObj.getDocStatus();
			if(StringUtils.isNotBlank(beanObj.getAcqBonus())&&"NCB".equalsIgnoreCase(beanObj.getAcqBonus())){
				obj[30]=StringUtils.isEmpty(beanObj.getAcqBonusPercentage())? "": beanObj.getAcqBonusPercentage();
				}
				else{
					obj[30]="";
				}
			obj[31]=StringUtils.isEmpty(beanObj.getAcqBonus())? "": beanObj.getAcqBonus();
			obj[33] = beanObj.getProposalNo();
			obj[32] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
			obj[34] = beanObj.getAmendId();
		}
		return obj;
	}
	
	private String getMaxproposalStatus(String proposalNo) {
		String result="";
		try{
			//risk.select.maxRskStatus
			result = nonProportCustomRepository.riskSelectMaxRskStatus(proposalNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public String[] updateContractRiskDetailsSecondForm(final SaveSecondPageReq beanObj,final String endNo) {
		String[] obj=null;
		obj = new String[9];
		obj[0] = beanObj.getLimitOurShare();
		obj[1] = getDesginationCountry(beanObj.getLimitOurShare(), beanObj.getExchangeRate());
		obj[2] = beanObj.getEpiAsPerOffer();
		obj[3] = getDesginationCountry(beanObj.getEpiAsPerOffer(), beanObj.getExchangeRate());
		obj[4] = beanObj.getMdpremiumourservice();
		obj[5] = getDesginationCountry(beanObj.getMdpremiumourservice(), beanObj.getExchangeRate());
		obj[6] = endNo;
		obj[7] = beanObj.getProposalNo();
		obj[8] = StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
		return obj;
	}
	@Transactional
	@Override
	public CommonResponse insertRetroCess(InsertRetroCessReq req) {
		CommonResponse response = new CommonResponse();
		try{
			int NoRetroCess=Integer.parseInt(req.getNoRetroCess()==null?"0":req.getNoRetroCess());
			String[] obj=null;
			String endtNo = dropDowmImpl.getRiskComMaxAmendId(req.getProposalNo());
			//delete_TTRN_RETRO_CESSIONARY
			nonProportCustomRepository.deleteTtrnRetroCessionary(req.getProposalNo(),endtNo);
			for(int i=0;i<NoRetroCess;i++){
				//risk.insert.retroCessDtls
				obj=getRetroCessArgs(req,i,endtNo,true);
				TtrnRetroCessionary insert = nonProportCustomRepository.riskInsertRetroCessDtls(obj);
				if(insert != null) {
					ttrnRetroCessionaryRepository.saveAndFlush(insert);
					}
			}
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
	   				e.printStackTrace();
	   				response.setMessage("Failed");
	   				response.setIsError(true);
	   			}
	   		return response;
	}
	public String[] getRetroCessArgs(InsertRetroCessReq beanObj,int i,String endtNo,boolean mode) {
		String[] obj = null;
		InsertRetroCessReq1 req = beanObj.getInsertRetroCessReq1().get(i);
		try{
			if(mode){	
				obj = new String[13];
				obj[0]=String.valueOf(i);
				obj[1]=beanObj.getProposalNo();
				obj[2]=StringUtils.isBlank(beanObj.getContNo())?"0":beanObj.getContNo();
				obj[3]=StringUtils.isEmpty(req.getCedingCompany())?"0":req.getCedingCompany();
				obj[4]=StringUtils.isEmpty(req.getRetroBroker())?"0":req.getRetroBroker();
				obj[5]=StringUtils.isEmpty(req.getShareAccepted())?"0":req.getShareAccepted();
				obj[6]=StringUtils.isEmpty(req.getShareSigned())?"0":req.getShareSigned();
				obj[7]="";
				obj[8]=StringUtils.isEmpty(req.getProposalStatus())?"0":req.getProposalStatus().equalsIgnoreCase("0")?"P":req.getProposalStatus();
				obj[9]=endtNo;
				obj[10]=StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
				obj[11]=beanObj.getLoginId();
				obj[12]=beanObj.getBranchCode();
			}else{
				obj = new String[11];
				obj[0]=StringUtils.isBlank(beanObj.getContNo())?"0":beanObj.getContNo();
				obj[1]=StringUtils.isEmpty(req.getCedingCompany())?"0":req.getCedingCompany();
				obj[2]=StringUtils.isEmpty(req.getRetroBroker())?"0":req.getRetroBroker();
				obj[3]=StringUtils.isEmpty(req.getShareAccepted())?"0":req.getShareAccepted();
				obj[4]=StringUtils.isEmpty(req.getShareSigned())?"0":req.getShareSigned();
				obj[5]="";
				obj[6]=StringUtils.isEmpty(req.getProposalStatus())?"0":req.getProposalStatus().equalsIgnoreCase("0")?"P":req.getProposalStatus();
				obj[7]=StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
				obj[8]=String.valueOf(i);
				obj[9]=beanObj.getProposalNo();
				obj[10]=endtNo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}

	public CommonResponse instalMentPremium(insertProportionalTreatyReq req) {
		CommonResponse response = new CommonResponse();
		try{
			String endtNo= dropDowmImpl.getRiskComMaxAmendId(req.getProposalNo());
			//delete.TTRN_MND_INSTALLMENTS
			nonProportCustomRepository.deleteTtrnMndInstallments(req.getProposalNo(),endtNo);
		
			final String InstallmentPeriod = (StringUtils.isBlank(req.getMdInstalmentNumber())||"0".equals(req.getMdInstalmentNumber()))?"1":req.getMdInstalmentNumber();
			int number=Integer.parseInt(InstallmentPeriod);
			String[] obj = new String[11];
			for (int i = 0; i < number; i++) {
				InstalmentperiodReq req1 = req.getInstalmentperiodReq().get(i);
				obj = new String[11];
				obj[0] = String.valueOf(i + 1);
				obj[1] = req.getProposalNo();
				obj[2] = StringUtils.isEmpty(req.getContNo()) ? "0"	: req.getContNo();
				obj[3] = StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo();
				obj[4] = endtNo;
				obj[5] = StringUtils.isEmpty(req1.getInstalmentDateList()) ? "" : req1.getInstalmentDateList();
				obj[6] = StringUtils.isEmpty(req1.getInstallmentPremium()) ? "" : req1.getInstallmentPremium().replaceAll(",", "");
				obj[7] = StringUtils.isEmpty(req1.getInstallmentPremium())|| StringUtils.isEmpty(req.getExchangeRate()) ? "0": getDesginationCountry(req1.getInstallmentPremium().replaceAll(",", ""), req.getExchangeRate());
				obj[8] =(req1.getPaymentDueDays()==null)?"":StringUtils.isEmpty(req1.getPaymentDueDays()) ? "" : req1.getPaymentDueDays();
				obj[9] = req.getLoginId();
				obj[10] = req.getBranchCode();
				//risk.insert.instalPrem
				TtrnMndInstallments insert = nonProportCustomRepository.riskInsertInstalPrem(obj);
				if(insert!=null) {
					ttrnMndInstallmentsRepository.saveAndFlush(insert);
					}
			}
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
	public CommonResponse insertRetroContracts(InsertRetroContractsReq beanObj) {
		CommonResponse response = new CommonResponse();
		try{
			final int LoopCount = Integer.parseInt(beanObj.getNoInsurer());
			String endtNo= dropDowmImpl.getRiskComMaxAmendId(beanObj.getProposalNo());
			String[] obj = new String[12];
			
			int j=2;
			if(LoopCount==0){
				beanObj.setRetentionPercentage("100");
			}
			//RETRO_DELETE_QUERY
			nonProportCustomRepository.retroDeleteQuery(beanObj.getProposalNo(),endtNo);
		
			for (int i = 0; i < LoopCount; i++) {
				RetroDetailReq req = beanObj.getRetroDetailReq().get(i);
				boolean mode = dropDowmImpl.getMode(beanObj.getProposalNo(),j,endtNo,2);
				if(mode){
					if(Integer.parseInt(endtNo)>0){
						//fac.update.insDetails
						nonProportCustomRepository.facUpdateInsDetails(beanObj.getProposalNo(),String.valueOf(i+1));
					}
					obj = new String[12];
					obj[0] = String.valueOf(j);
					obj[1] = beanObj.getProposalNo();
					obj[2] = StringUtils.isEmpty(req.getRetroCeding()) ? "0" :req.getRetroCeding();
					obj[3] = endtNo;
					obj[4] = "C";
					obj[5] = StringUtils.isEmpty(req.getPercentRetro())? "0" :req.getPercentRetro();
					obj[6] = "Y";
					obj[7] = StringUtils.isEmpty(req.getRetroYear())? "0" :req.getRetroYear();
					obj[8] = StringUtils.isEmpty(beanObj.getRetroType())? "" :beanObj.getRetroType();
					obj[9] = beanObj.getDepartmentId();
					obj[10] = beanObj.getLoginId();
					obj[11] = beanObj.getBranchCode();
					
					//fac.insert.insDetails
					TtrnInsurerDetails insert = nonProportCustomRepository.facInsertInsDetails(obj);
					if(insert!=null) {
						ttrnInsurerDetailsRepository.saveAndFlush(insert)	;
						}
					j++;
					
				}else
				{
					obj = new String[11];
					obj[0] = StringUtils.isEmpty(req.getRetroCeding()) ? "0" :req.getRetroCeding();
					obj[1] = "C";
					obj[2] = StringUtils.isEmpty(req.getPercentRetro())? "0" :req.getPercentRetro();
					obj[3] = "Y";
					obj[4] = StringUtils.isEmpty(req.getRetroYear())? "0" :req.getRetroYear();
					obj[5] = StringUtils.isEmpty(beanObj.getRetroType())? "" :beanObj.getRetroType();
					obj[6] = beanObj.getLoginId();
					obj[7] = beanObj.getBranchCode();
					obj[8] = beanObj.getProposalNo();
					obj[9] = String.valueOf(i+1);
					obj[10] = endtNo;
					
					//fac.upddate.updDetails
					TtrnInsurerDetails update = nonProportCustomRepository.facUpddateUpdDetails(obj);
					if(update!=null) {
						ttrnInsurerDetailsRepository.saveAndFlush(update)	;
						}
				}
			}
			boolean mode = dropDowmImpl.getMode(beanObj.getProposalNo(),0,endtNo,2);
			if(mode){
				if(Integer.parseInt(endtNo)>0){
					
					//fac.update.insDetails
					nonProportCustomRepository.facUpdateInsDetails(beanObj.getProposalNo(),"0");
				}
				obj = new String[12];
				obj[0] = String.valueOf(0);
				obj[1] = beanObj.getProposalNo();
				obj[2] = "";
				obj[3] = endtNo;
				obj[4] = "R";
				obj[5] = StringUtils.isEmpty(beanObj.getRetentionPercentage())? "0" :beanObj.getRetentionPercentage();
				obj[6] = "Y";
				obj[7] = "";
				obj[8] = "";
				obj[9] = beanObj.getDepartmentId();
				obj[10] = beanObj.getLoginId();
				obj[11] = beanObj.getBranchCode();
				
				//fac.insert.insDetails
				TtrnInsurerDetails insert = nonProportCustomRepository.facInsertInsDetails(obj);
				if(insert!=null) {
					ttrnInsurerDetailsRepository.saveAndFlush(insert)	;
					}
				obj = new String[12];
				obj[0] = String.valueOf(1);
				obj[1] = beanObj.getProposalNo();
				obj[2] =  StringUtils.isEmpty(beanObj.getRetroDupContract()) ? "0" :beanObj.getRetroDupContract();
				obj[3] = endtNo;
				obj[4] = "C";
				obj[5] = StringUtils.isEmpty(beanObj.getRetentionPercentage())? "0" :beanObj.getRetentionPercentage();
				obj[6] = "Y";
				obj[7] = StringUtils.isEmpty(beanObj.getRetroDupYerar())? "0" :beanObj.getRetroDupYerar();
				obj[8] = StringUtils.isEmpty(beanObj.getRetroType())? "" :beanObj.getRetroType();
				obj[9] = beanObj.getDepartmentId();
				obj[10] = beanObj.getLoginId();
				obj[11] = beanObj.getBranchCode();
				//fac.insert.insDetails
				TtrnInsurerDetails insert1 = nonProportCustomRepository.facInsertInsDetails(obj);
				if(insert1!=null) {
					ttrnInsurerDetailsRepository.saveAndFlush(insert1)	;
					}
			}else
			{
				obj = new String[11];
				obj[0] = "";
				obj[1] = "R";
				obj[2] = StringUtils.isEmpty(beanObj.getRetentionPercentage())? "0" :beanObj.getRetentionPercentage();
				obj[3] = "Y";
				obj[4] = "";
				obj[5] = "";
				obj[6] = beanObj.getLoginId();
				obj[7] = beanObj.getBranchCode();
				obj[8] = beanObj.getProposalNo();
				obj[9] = "0";
				obj[10] = endtNo;
				//fac.upddate.updDetails
				TtrnInsurerDetails update = nonProportCustomRepository.facUpddateUpdDetails(obj);
				if(update!=null) {
					ttrnInsurerDetailsRepository.saveAndFlush(update);
					}
			}
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
	   				e.printStackTrace();
	   				response.setMessage("Failed");
	   				response.setIsError(true);
	   			}
	   		return response;
	}
	@Transactional
	@Override
	public CommonResponse reInstatementMainInsert(ReInstatementMainInsertReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
		if("Y".equalsIgnoreCase(beanObj.getReInstatementPremium())){
			updateTotalReinstatement(beanObj);
		}
		else{
			//deleteMainTable(beanObj.getProposalNo(),beanObj.getAmendId(),beanObj.getBranchCode(),beanObj.getReferenceNo());
			MoveReinstatementEmptyData(beanObj);
		}
		updateContractNumber(beanObj);
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

private void updateTotalReinstatement(ReInstatementMainInsertReq bean) {
	try{
		if(bean.getReinstatementNo()!=null){
			//UPDATE_REINSTATEMENT_TOTAL_NO
			nonProportCustomRepository.updateReinstatementTotalNo(bean.getProposalNo(),String.valueOf(bean.getReinstatementNo().size()));
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
}
public void MoveReinstatementEmptyData(ReInstatementMainInsertReq bean) {
	try{
	if(StringUtils.isBlank(bean.getAmendId())){
		bean.setAmendId("0");
	}
	if(StringUtils.isBlank(bean.getProposalNo()) && (StringUtils.isBlank(bean.getReferenceNo()) || "0".equals(bean.getReferenceNo()))) { //ri
    	String referenceNo="";
    	
    	List<Map<String, Object>> list  = queryImpl.selectSingle("SELECT  REFERENCENO_SEQ.NEXTVAL REFERENCENO FROM DUAL",new String[]{}); //dual
    	if (!CollectionUtils.isEmpty(list)) {
    		referenceNo = list.get(0).get("REFERENCENO") == null ? ""
    				: list.get(0).get("REFERENCENO").toString();
    	}
    	
    	bean.setReferenceNo(referenceNo);
    }
	
	 String args[] = new String[15]; //ri
		args[0] = "";
		args[1] = "";
		args[2] = "";
		args[3] = "";
		args[4] = "";
		args[5] = bean.getProposalNo();
		args[6] = bean.getBranchCode();
		args[7] = bean.getAmendId();
		args[8] = bean.getDepartmentId();
		args[9] = bean.getContractNo();
		args[10] = bean.getProductId();
		args[11] = StringUtils.isEmpty(bean.getLayerNo()) ? ""	: bean.getLayerNo();
		args[12] ="";
		args[13] = "A";
		args[14]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();
		 //INSERT_REINSTATEMENT_MAIN
		TtrnRip insert =  nonProportCustomRepository.insertReinstatementMain(args);
		if(insert!=null) {
			ttrnRipRepository.saveAndFlush(insert);
			}

		//getClassLimitDetails(bean);
	
		String args1[] = new String[11];
		for(int i=0;i<bean.getCoverdepartIdList().size();i++){
			CoverdepartIdList req =bean.getCoverdepartIdList().get(i);
			args1[0] = req.getCoverdepartId();
			args1[1] = req.getCoverLimitOC().replaceAll(",", "");
			args1[2] = bean.getProposalNo();
			args1[3] = bean.getBranchCode();
			args1[4] = bean.getAmendId();
			args1[5] = bean.getDepartmentId();
			args1[6] ="";
			args1[7] = bean.getProductId();
			args1[8] = StringUtils.isEmpty(bean.getLayerNo()) ? "0"	: bean.getLayerNo();
			args1[9] = "B";
			args[10]=StringUtils.isBlank(bean.getReferenceNo())?"":bean.getReferenceNo();

			//INSERT_REINSTATEMENT_MAIN_B
			TtrnRip insert1 =  nonProportCustomRepository.insertReinstatementMainB(args1);
			if(insert1!=null) {
				ttrnRipRepository.saveAndFlush(insert1);
				}
		}
	}
	catch (Exception e){
		e.printStackTrace();
	}
}
@Transactional
private void updateContractNumber(ReInstatementMainInsertReq bean) {
	try{
		
		String args[]= new String[5];
		args[0] = bean.getContractNo();
		args[1] = StringUtils.isEmpty(bean.getLayerNo())?"0":bean.getLayerNo();
		args[2] = bean.getProposalNo();
		args[3] = bean.getBranchCode();
		args[4] = bean.getAmendId();
		//RE_INSTATEMENT_UPDATE
		nonProportCustomRepository.reInstatementUpdate(args);
	}
	catch(Exception e){
		e.printStackTrace();
	}
}


@Override
public CommonResponse insertCrestaMaintable(CrestaSaveReq bean) {
	CommonResponse response = new CommonResponse();
	String obj[] =null;
	try {
		int count=getCrestaCount(bean.getAmendId(), bean.getProposalNo(), bean.getBranchCode());
				if(count<=0){
			 
			    obj = new String[12];
				obj[0]=bean.getContractNo();
				obj[1]=bean.getProposalNo();
				obj[2]=bean.getAmendId();
				obj[3]=bean.getDepartmentId();
				obj[4]="";
				obj[5]="";
				obj[6]="";
				obj[7]="";
				obj[8]="";
				obj[9]=bean.getBranchCode();
				obj[10]="";
				obj[11]="";
				//MOVE_TO_CRESTA_MAIN_TABLE
				TtrnCrestazoneDetails insert = nonProportCustomRepository.moveToCrestaMainTable(obj);
				if(insert!=null) {
					ttrnCrestazoneDetailsRepository.saveAndFlush(insert);
					}
		}
		 
		 obj = new String[4];
		 obj[0]=bean.getContractNo();
		 obj[1]=bean.getProposalNo();
		 obj[2]=bean.getAmendId();
		 obj[3]=bean.getBranchCode();
		//CREATA_CONTRACT_

		 TtrnCrestazoneDetails list = ttrnCrestazoneDetailsRepository.findByProposalNoAndAmendIdAndBranchCode(new BigDecimal(bean.getProposalNo()),bean.getAmendId(),bean.getBranchCode());
		 if(list!= null) {
			 list.setContractNo(new BigDecimal(bean.getContractNo()));	
			 ttrnCrestazoneDetailsRepository.saveAndFlush(list);
			 }
		 response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
}

@Transactional
@Override
public CommonResponse insertBonusDetails(InsertBonusDetailsReq beanObj) {
	CommonResponse response = new CommonResponse();
	try {
	if(!"LCB".equalsIgnoreCase(beanObj.getAcqBonus())){
			insetNOClaimBonusMainTable(beanObj);
		}
			String args[]=new String[6];
			args[1] =beanObj.getProposalNo();
			args[0] = beanObj.getContractNo();
			args[2] = beanObj.getBranchCode();
			args[3] = beanObj.getAcqBonus();
			args[4] =  beanObj.getAmendId();
			args[5] =  StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo();
			//UPDATE_CONTRACT_DETAILS
			nonProportCustomRepository.updateContractDetails(args);
			 response.setMessage("Success");
				response.setIsError(false);
				}catch(Exception e){
						e.printStackTrace();
						response.setMessage("Failed");
						response.setIsError(true);
					}
		return response;
}
public void insetNOClaimBonusMainTable(InsertBonusDetailsReq bean) {
	try{
		if(StringUtils.isBlank(bean.getEndorsmentno())){
			//GET_AMEND_ID
			PositionMaster list  =	positionMasterRepository.findTop1ByProposalNoOrderByAmendIdDesc(new BigDecimal(bean.getProposalNo()));
        	if(list!=null) {
			bean.setEndorsmentno(list.getAmendId()==null?"":list.getAmendId().toString());
			}
		}
		deleteMainTable(bean.getProposalNo(),bean.getAmendId(),bean.getBranchCode(),bean.getReferenceNo());
		if(StringUtils.isBlank(bean.getProposalNo()) && (StringUtils.isBlank(bean.getReferenceNo()) || "0".equals(bean.getReferenceNo()))) {
        	String referenceNo="";
        	String query="GET_REFERENCE_NO_SEQ";  //ri
        	
        	List<Map<String, Object>> list  = queryImpl.selectSingle("SELECT  REFERENCENO_SEQ.NEXTVAL REFERENCENO FROM DUAL",new String[]{});
        	if (!CollectionUtils.isEmpty(list)) {
        		referenceNo = list.get(0).get("REFERENCENO") == null ? ""
        				: list.get(0).get("REFERENCENO").toString();
        	}
        	bean.setReferenceNo(referenceNo);
        }
		
		String args[]=new String[16]; //ri
	           args[0] =bean.getProposalNo();
	           args[1] = bean.getContractNo();	
	           args[2] = bean.getProductId();
	           args[3] = "";
	           args[4] = "";
	           args[5] = "";
	           args[6] ="";
	           args[7] = bean.getLoginId();
	           args[8] = bean.getBranchCode();
	           args[9] = "1";
	           args[10] =bean.getAcqBonus();
	           args[11] = bean.getAmendId();
	           args[12] = bean.getDepartmentId();
	           args[13] = StringUtils.isEmpty(bean.getLayerNo())?"0":bean.getLayerNo();
	           args[14] = StringUtils.isEmpty(bean.getReferenceNo())?"0":bean.getReferenceNo();

	           String sno = "";
	           TtrnBonus list = ttrnBonusRepository.findTop1ByBranchOrderBySnoDesc(bean.getBranchCode());
	           if(list!=null) {
	        	   sno =   list.getSno()==null?"0": String.valueOf(list.getSno().intValue()+1);
	           }
	           args[15] =sno;
	         //BONUS_MAIN_INSERT
	           TtrnBonus insert = nonProportCustomRepository.bonusMainInsert(args);
	           if(insert!=null) {
	        	   ttrnBonusRepository.saveAndFlush(insert);
	        	   }
}
	catch(Exception e){
		e.printStackTrace();
	}
}

private boolean checkEditSaveModeMethod(final insertProportionalTreatyReq req) {
	boolean editSaveMode = false;
	try {
		//risk.select.getRskProNO
		List<TtrnRiskDetails> list = ttrnRiskDetailsRepository.findByRskProposalNumber(req.getProposalNo());
		if (list.size() == 0) {
			editSaveMode = false;
		} else {
			editSaveMode = true;
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return editSaveMode;
}

	@Override
	public GetLayerInfoRes getLayerInfo(GetLayerInfoReq bean) {
		GetLayerInfoRes response = new GetLayerInfoRes();
		List<GetLayerInfoRes1> resList = new ArrayList<GetLayerInfoRes1>();
		List<Tuple> result=new ArrayList<>();
		String[] obj=null;
		try {
			if(StringUtils.isBlank(bean.getContNo())) {
			
				obj= new String[2];
				if("Y".equals(bean.getContractMode())  && StringUtils.isNotBlank(bean.getBaseLayer())) {
					obj[0]=bean.getBaseLayer();
					obj[1]=bean.getBaseLayer();
				}else if(StringUtils.isNotBlank(bean.getBaseLayer())){
					obj[0]=bean.getBaseLayer();
					obj[1]=bean.getBaseLayer();
				}else {
					obj[0]=bean.getProposalNo();
					obj[1]=bean.getProposalNo();
				}
				//GET_LAYER_INFO
				result = nonProportCustomRepository.getLayerInfo(obj);
			}else {
				
				obj= new String[2];
				if("Y".equals(bean.getContractMode())  && StringUtils.isNotBlank(bean.getBaseLayer())) {
					obj[0]=bean.getBaseLayer();
					obj[1]=bean.getBaseLayer();
				}else if(StringUtils.isNotBlank(bean.getBaseLayer())){
					obj[0]=bean.getBaseLayer();
					obj[1]=bean.getBaseLayer();
				}else {
					obj[0]=bean.getProposalNo();
					obj[1]=bean.getProposalNo();
				}
				//GET_LAYER_CONTRACT_INFO
				result = nonProportCustomRepository.getLayerContractInfo(obj);
			}
			
			if(result!=null && result.size()>0){
				for (int i = 0; i < result.size(); i++) {
					Tuple insMap = result.get(i);
					GetLayerInfoRes1 res = new GetLayerInfoRes1();
					 res.setOfferNo(insMap.get("OFFER_NO")==null?"":insMap.get("OFFER_NO").toString());	 
					 res.setLayerNo(insMap.get("LAYER_NO")==null?"":insMap.get("LAYER_NO").toString());	 
					  res.setNewLayerNo(insMap.get("NEW_LAYER_NO")==null?"":insMap.get("NEW_LAYER_NO").toString());	 
					  res.setProposalNo(insMap.get("PROPOSAL_NO")==null?"":insMap.get("PROPOSAL_NO").toString());	 
					  res.setCedingCompanyId(insMap.get("CEDING_COMPANY_ID")==null?"":insMap.get("CEDING_COMPANY_ID").toString());	 
					  res.setBaseLayer(insMap.get("BASE_LAYER")==null?"":insMap.get("BASE_LAYER").toString());	 
					  res.setContractNo(insMap.get("CONTRACT_NO")==null?"":insMap.get("CONTRACT_NO").toString());	 
					  res.setDeptId(insMap.get("DEPT_ID")==null?"":insMap.get("DEPT_ID").toString());	 
					  res.setProductId(insMap.get("PRODUCT_ID")==null?"":insMap.get("PRODUCT_ID").toString());	 
					  res.setSectionNo(insMap.get("SECTION_NO")==null?"":insMap.get("SECTION_NO").toString());	 
					  res.setTmasDepartmentName(insMap.get("TMAS_DEPARTMENT_NAME")==null?"":insMap.get("TMAS_DEPARTMENT_NAME").toString());	 
					  res.setTreatyType(insMap.get("TREATY_TYPE")==null?"":insMap.get("TREATY_TYPE").toString());	 
					  res.setRskTreatyid(insMap.get("RSK_TREATYID")==null?"":insMap.get("RSK_TREATYID").toString());	
					  resList.add(res);
					}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Transactional
	@Override
	public CommonResponse cancelProposal(String proposalNo, String proposalReference, String newProposal) {
		CommonResponse response = new CommonResponse();
		try{
			if(!"Layer".equalsIgnoreCase(proposalReference)){
				//CANCEL_OLD_PROPOSAL
				nonProportCustomRepository.cancelOldProposal(proposalNo);
			
			}
				//CANCEL_PROPOSAL
				nonProportCustomRepository.cancelProposal(newProposal);
			
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public CommonSaveRes getTreatYNameDuplicationCheck(GetTreatyNameDuplicationCheckReq formObj) {
		CommonSaveRes response = new CommonSaveRes();
		boolean result=false;
		try{
			if (StringUtils.isNotBlank(formObj.getProposalNo()) && StringUtils.isNotBlank(formObj.getTreatyNameType()) ) {
				//risk.select.getSectionDupcheckByProNo
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query1 = cb.createQuery(String.class); 
				Root<PositionMaster> pm = query1.from(PositionMaster.class);
				Root<TtrnRiskDetails> trd = query1.from(TtrnRiskDetails.class);
				
				query1.select(trd.get("rskTreatyid")); 
				
				Subquery<Long> amend = query1.subquery(Long.class); 
				Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
				amend.select(cb.max(pm1.get("amendId")));
				Predicate f1 = cb.equal( pm1.get("proposalNo"),  pm.get("proposalNo"));
				amend.where(f1);
				
				Subquery<Long> amend1 = query1.subquery(Long.class); 
				Root<TtrnRiskDetails> trd1 = amend1.from(TtrnRiskDetails.class);
				amend1.select(cb.max(trd1.get("rskEndorsementNo")));
				Predicate fr1 = cb.equal( trd1.get("rskProposalNumber"),  trd.get("rskProposalNumber"));
				amend1.where(fr1);
				
				Predicate n1 = cb.equal(trd.get("rskTreatyid"),formObj.getTreatyNameType());
				Predicate n2 = cb.notEqual(trd.get("rskProposalNumber"), formObj.getProposalNo()); 
				Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo")),StringUtils.isBlank(formObj.getBaseLayer())?formObj.getProposalNo():formObj.getBaseLayer());
				Predicate n4 = cb.equal(pm.get("contractStatus"), "P"); 
				Predicate n5 = cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")); 
				Predicate n6 = cb.equal(pm.get("amendId"), amend); 
				Predicate n7 = cb.equal(trd.get("rskEndorsementNo"), amend1); 
				query1.where(n1,n2,n3,n4,n5,n6,n7);	
		
				TypedQuery<String> res1 = em.createQuery(query1);
				List<String> list = res1.getResultList();
				
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						String res=list.get(i)==null?"":list.get(i).toString();
						if(res.equalsIgnoreCase(formObj.getTreatyNameType())){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(formObj.getBaseLayer()) && StringUtils.isNotBlank(formObj.getTreatyNameType()) ){
				//risk.select.getSectionDupcheckByBaseLayer
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query1 = cb.createQuery(String.class); 
				Root<PositionMaster> pm = query1.from(PositionMaster.class);
				Root<TtrnRiskDetails> trd = query1.from(TtrnRiskDetails.class);
				
				query1.select(trd.get("rskTreatyid")); 
				
				Subquery<Long> amend = query1.subquery(Long.class); 
				Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
				amend.select(cb.max(pm1.get("amendId")));
				Predicate f1 = cb.equal( pm1.get("proposalNo"),  pm.get("proposalNo"));
				amend.where(f1);
				
				Subquery<Long> amend1 = query1.subquery(Long.class); 
				Root<TtrnRiskDetails> trd1 = amend1.from(TtrnRiskDetails.class);
				amend1.select(cb.max(trd1.get("rskEndorsementNo")));
				Predicate fr1 = cb.equal( trd1.get("rskProposalNumber"),  trd.get("rskProposalNumber"));
				amend1.where(fr1);
				
				Predicate n1 = cb.equal(trd.get("rskTreatyid"),formObj.getTreatyNameType());
				Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo")),formObj.getBaseLayer());
				Predicate n4 = cb.equal(pm.get("contractStatus"), "P"); 
				Predicate n5 = cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")); 
				Predicate n6 = cb.equal(pm.get("amendId"), amend); 
				Predicate n7 = cb.equal(trd.get("rskEndorsementNo"), amend1); 
				query1.where(n1,n3,n4,n5,n6,n7);	
		
		
				TypedQuery<String> res1 = em.createQuery(query1);
				List<String> list = res1.getResultList();
				
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						String res=list.get(i)==null?"":list.get(i).toString();
						if(res.equalsIgnoreCase(formObj.getTreatyNameType())){
							result=true;
						}
					}
				}
			}else if (StringUtils.isNotBlank(formObj.getProposalNo()) && StringUtils.isNotBlank(formObj.getTreatyNameType())){
				//risk.select.getSectionDupcheckByBaseLayer
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<String> query1 = cb.createQuery(String.class); 
				Root<PositionMaster> pm = query1.from(PositionMaster.class);
				Root<TtrnRiskDetails> trd = query1.from(TtrnRiskDetails.class);
				
				query1.select(trd.get("rskTreatyid")); 
				
				Subquery<Long> amend = query1.subquery(Long.class); 
				Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
				amend.select(cb.max(pm1.get("amendId")));
				Predicate f1 = cb.equal( pm1.get("proposalNo"),  pm.get("proposalNo"));
				amend.where(f1);
				
				Subquery<Long> amend1 = query1.subquery(Long.class); 
				Root<TtrnRiskDetails> trd1 = amend1.from(TtrnRiskDetails.class);
				amend1.select(cb.max(trd1.get("rskEndorsementNo")));
				Predicate fr1 = cb.equal( trd1.get("rskProposalNumber"),  trd.get("rskProposalNumber"));
				amend1.where(fr1);
				
				Predicate n1 = cb.equal(trd.get("rskTreatyid"),formObj.getTreatyNameType());
				Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo")),formObj.getBaseLayer());
				Predicate n4 = cb.equal(pm.get("contractStatus"), "P"); 
				Predicate n5 = cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")); 
				Predicate n6 = cb.equal(pm.get("amendId"), amend); 
				Predicate n7 = cb.equal(trd.get("rskEndorsementNo"), amend1); 
				query1.where(n1,n3,n4,n5,n6,n7);	
		
		
				TypedQuery<String> res1 = em.createQuery(query1);
				List<String> list = res1.getResultList();
				
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						String res=list.get(i)==null?"":list.get(i).toString();
						if(res.equalsIgnoreCase(formObj.getTreatyNameType())){
							result=true;
						}
					}
				}
			}
			response.setResponse(String.valueOf(result));
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