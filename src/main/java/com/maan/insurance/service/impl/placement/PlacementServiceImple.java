package com.maan.insurance.service.impl.placement;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.MailNotificationDetail;
import com.maan.insurance.model.entity.MailTemplateMaster;
import com.maan.insurance.model.entity.NotificationAttachmentDetail;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PersonalInfoContact;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.StatusMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasDocTypeMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiPlacementStatus;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.repository.MailNotificationDetailRepository;
import com.maan.insurance.model.repository.MailTemplateMasterRepository;
import com.maan.insurance.model.repository.NotificationAttachmentDetailRepository;
import com.maan.insurance.model.repository.TtrnRiPlacementRepository;
import com.maan.insurance.model.repository.TtrnRiPlacementStatusRepository;
import com.maan.insurance.model.req.placement.AttachFileReq;
import com.maan.insurance.model.req.placement.DeleteFileReq;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;
import com.maan.insurance.model.req.placement.GetExistingAttachListReq;
import com.maan.insurance.model.req.placement.GetExistingReinsurerListReq;
import com.maan.insurance.model.req.placement.GetMailTemplateReq;
import com.maan.insurance.model.req.placement.GetMailToListReq;
import com.maan.insurance.model.req.placement.GetPlacementInfoListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewReq;
import com.maan.insurance.model.req.placement.GetPlacingInfoReq;
import com.maan.insurance.model.req.placement.GetReinsurerInfoReq;
import com.maan.insurance.model.req.placement.InsertDocdetailsReq;
import com.maan.insurance.model.req.placement.InsertMailDetailsReq;
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.req.placement.ReinsListReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdateMailDetailsReq;
import com.maan.insurance.model.req.placement.UpdateMailDetailsReq1;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq1;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.placement.AttachFileRes;
import com.maan.insurance.model.res.placement.AttachFileRes1;
import com.maan.insurance.model.res.placement.CommonSaveResList;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes1;
import com.maan.insurance.model.res.placement.EditPlacingDetailsResponse;
import com.maan.insurance.model.res.placement.GetExistingAttachListRes;
import com.maan.insurance.model.res.placement.GetExistingAttachListRes1;
import com.maan.insurance.model.res.placement.GetMailTemplateRes;
import com.maan.insurance.model.res.placement.GetMailTemplateRes1;
import com.maan.insurance.model.res.placement.GetPlacementInfoListRes;
import com.maan.insurance.model.res.placement.GetPlacementInfoListRes1;
import com.maan.insurance.model.res.placement.GetPlacementNoRes;
import com.maan.insurance.model.res.placement.GetPlacementNoRes1;
import com.maan.insurance.model.res.placement.GetPlacementViewListRes;
import com.maan.insurance.model.res.placement.GetPlacementViewListRes1;
import com.maan.insurance.model.res.placement.GetPlacementViewRes;
import com.maan.insurance.model.res.placement.GetPlacementViewRes1;
import com.maan.insurance.model.res.placement.GetPlacingInfoRes;
import com.maan.insurance.model.res.placement.GetPlacingInfoRes1;
import com.maan.insurance.model.res.placement.GetReinsurerInfoRes;
import com.maan.insurance.model.res.placement.GetReinsurerInfoRes1;
import com.maan.insurance.model.res.placement.GetReinsurerInfoResponse;
import com.maan.insurance.model.res.placement.InsertDocdetailsRes;
import com.maan.insurance.model.res.placement.InsertMailDetailsRes;
import com.maan.insurance.model.res.placement.InsertMailDetailsRes1;
import com.maan.insurance.model.res.placement.InsertPlacingRes;
import com.maan.insurance.model.res.placement.InsertPlacingRes1;
import com.maan.insurance.model.res.placement.PlacementSummaryRes;
import com.maan.insurance.model.res.placement.PlacementSummaryRes1;
import com.maan.insurance.model.res.placement.ProposalInfoRes;
import com.maan.insurance.model.res.placement.ProposalInfoRes1;
import com.maan.insurance.model.res.placement.SavePlacingRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes1;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.notification.entity.MailMaster;
import com.maan.insurance.notification.repo.MailMasterRepository;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.placement.PlacementService;
import com.maan.insurance.validation.Formatters;

@Service
public class PlacementServiceImple implements PlacementService {
	private Logger log = LogManager.getLogger(PlacementServiceImple.class);
	
	
	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple  dropDownImple;
	
	@Autowired
	private TtrnRiPlacementRepository ripRepo;
	@Autowired
	private TtrnRiPlacementStatusRepository placeStatusRepo;
	@Autowired
	private NotificationAttachmentDetailRepository notiRepo;
	@Autowired
	private MailMasterRepository mmRepo;
	@Autowired
	private MailNotificationDetailRepository  mailnotiRepo;
	@Autowired
	private MailTemplateMasterRepository mailTemplateMasterRepository;
	
	@PersistenceContext
	private EntityManager em;

	private Properties prop = new Properties();
	private Query query1=null;
	
	  public PlacementServiceImple() {
	        try {
	        	InputStream  inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
	        	if (inputStream != null) {
					prop.load(inputStream);
				}
	        	
	        } catch (Exception e) {
	            log.info(e);
	        }
	    }
	  
	  String SMTP_AUTH_USER; 
		String SMTP_AUTH_PWD;
	  
	@Override
	public GetCommonDropDownRes getMailToList(GetMailToListReq bean) { 
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		String cedeingId="";
		try{
			if(("A".equals(bean.getCurrentStatus())  && "PWL".equals(bean.getNewStatus())) || StringUtils.isBlank(bean.getReinsurerId())) {
				cedeingId="63".equals(bean.getBrokerId())?bean.getCedingId():bean.getBrokerId();
				
			}else {
				cedeingId="63".equals(bean.getBrokerId())?bean.getReinsurerId():bean.getBrokerId();
			}
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PersonalInfoContact> pm = query.from(PersonalInfoContact.class);

			query.multiselect(pm.get("email").alias("CODE")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfoContact> pms = amend.from(PersonalInfoContact.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("customerId"), pms.get("customerId"));
			amend.where(a1);

			// Where
			Predicate n1 = cb.equal(pm.get("customerId"), cedeingId);
			Predicate n2 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
      			for(Tuple data: list) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
      				res.setCodeDescription(data.get("CODE")==null?"": data.get("CODE").toString());;
      				resList.add(res);
      			}
      		}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonDropDownRes getExistingReinsurerList(GetExistingReinsurerListReq bean) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
						
			//reinsurerName
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
			
			query.multiselect(pm.get("reinsurerId").alias("REINSURER_ID"),reInsurerName.alias("REINSURER_NAME")).distinct(true); 
			
			
			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_EX_REINSURER_BOUQUET_LIST
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
				query.where(n1,n2);
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_EX_REINSURER_BASE_LIST
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
				query.where(n1,n2);
			}else {
				//GET_EX_REINSURER_PRO_LIST
				Predicate n2 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
				query.where(n1,n2);
			}
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			
			if(list.size()>0) {
				for(Tuple data: list) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.get("REINSURER_ID")==null?"":data.get("REINSURER_ID").toString());
      				res.setCodeDescription(data.get("REINSURER_NAME")==null?"": data.get("REINSURER_NAME").toString());;
      				resList.add(res);
      			}
      		}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonDropDownRes getExistingBrokerList(GetExistingReinsurerListReq bean) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
						
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			brokerName.select(cb.concat(firstName1, pi.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(b1);
			
			Predicate a1 = cb.equal( pi.get("customerType"), "B");
			Predicate a2 = cb.equal( pi.get("customerId"), pm.get("brokerId"));
			Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( pi.get("amendId"), maxAmend);
			brokerName.where(a1,a2,a3,a4);
			
			query.multiselect(pm.get("brokerId").alias("BROKER_ID"),brokerName.alias("BROKER_NAME")).distinct(true); 
			
			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_EX_BROKER_BOUQUET_LIST
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
				query.where(n1,n2);
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_EX_BROKER_BASE_LIST
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
				query.where(n1,n2);
			}else {
				//GET_EX_BROKER_PRO_LIST
				Predicate n2 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
				query.where(n1,n2);
			}
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			
			if(list.size()>0) {
				for(Tuple data: list) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.get("BROKER_ID")==null?"":data.get("BROKER_ID").toString());
      				res.setCodeDescription(data.get("BROKER_NAME")==null?"": data.get("BROKER_NAME").toString());;
      				resList.add(res);
      			}
      		}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetExistingAttachListRes getExistingAttachList(GetExistingAttachListReq bean) {
		GetExistingAttachListRes response = new GetExistingAttachListRes();
		List<GetExistingAttachListRes1> resList = new ArrayList<GetExistingAttachListRes1>();
		try {
				//GET_EX_DOC_PRO_LIST
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<NotificationAttachmentDetail> pm = query.from(NotificationAttachmentDetail.class);
							
				//DOC_TYPE
				Subquery<String> docName = query.subquery(String.class); 
				Root<TmasDocTypeMaster> pi = docName.from(TmasDocTypeMaster.class);
			
				docName.select( pi.get("docName"));
				
				//maxAmend
				Subquery<Long> maxAmend = query.subquery(Long.class); 
				Root<TmasDocTypeMaster> pis = maxAmend.from(TmasDocTypeMaster.class);
				maxAmend.select(cb.max(pis.get("amendId")));
				Predicate b1 = cb.equal( pis.get("docType"), pi.get("docType"));
				Predicate b2 = cb.equal( pis.get("branchCode"), pi.get("branchCode"));
				Predicate b3 = cb.equal( pis.get("moduleType"), pi.get("moduleType"));
				Predicate b4 = cb.equal( pis.get("productId"), pi.get("productId"));
				maxAmend.where(b1,b2,b3,b4);
				
				Predicate a1 = cb.equal( pi.get("productId"), "12");
				Predicate a2 = cb.equal( pi.get("moduleType"), "PL");
				Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
				Predicate a4 = cb.equal( pi.get("status"), "Y");
				Predicate a5 = cb.equal( pi.get("amendId"), maxAmend);
				Predicate a6 = cb.equal( pi.get("docType"), pm.get("docType"));
				docName.where(a1,a2,a3,a4,a5,a6);
				
				query.multiselect(pm.get("docId").alias("DOC_ID"),docName.alias("DOC_TYPE"),
						pm.get("docDescription").alias("DOC_DESCRIPTION"),pm.get("orgFileName").alias("ORG_FILE_NAME")
						,pm.get("ourFileName").alias("OUR_FILE_NAME")).distinct(true); 
				
				Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				
				Expression<String> e0 = pm.get("docType");
				Predicate n2 = e0.in("MA","MA2").not();

				Predicate n3 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
				Predicate n4 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
				Predicate n5 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
				Predicate n6 = cb.equal(pm.get("correspondentId"), bean.getCorresId());
				query.where(n1,n2,n3,n4,n5,n6);
				
				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
		
				if(list.size()>0) {
					for(Tuple data: list) {
						GetExistingAttachListRes1 res = new GetExistingAttachListRes1();
						res.setDocDescription(data.get("DOC_DESCRIPTION")==null?"":data.get("DOC_DESCRIPTION").toString());
						res.setDocId(data.get("DOC_ID")==null?"":data.get("DOC_ID").toString());
						res.setDocType(data.get("DOC_TYPE")==null?"":data.get("DOC_TYPE").toString());
						res.setOrgFileName(data.get("ORG_FILE_NAME")==null?"":data.get("ORG_FILE_NAME").toString());
						res.setOurFileName(data.get("OUR_FILE_NAME")==null?"":data.get("OUR_FILE_NAME").toString());						
	      				resList.add(res);
	      			}
	      		}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}


	@Override
	public ProposalInfoRes proposalInfo(String branchCode, String proposalNo, String eProposalNo) {
		ProposalInfoRes response = new ProposalInfoRes();
		ProposalInfoRes1 bean = new ProposalInfoRes1();
		try {
			String proposal=StringUtils.isBlank(eProposalNo)?proposalNo:eProposalNo;
			
			List<Tuple> list = getExistingProposal(proposal, branchCode);
			if(list.size()>0) {
				Tuple map=list.get(0);
				bean.setCedingCompanyName(map.get("COMPANY_NAME")==null?"":map.get("COMPANY_NAME").toString());
				bean.setCedingCompany(map.get("RSK_CEDINGID")==null?"":map.get("RSK_CEDINGID").toString());
				bean.setBrokerCompany(map.get("RSK_BROKERID")==null?"":map.get("RSK_BROKERID").toString());
				bean.setTreatyName(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString());
				bean.setInceptionDate(map.get("INS_DATE")==null?"":map.get("INS_DATE").toString());
				bean.setExpiryDate(map.get("EXP_DATE")==null?"":map.get("EXP_DATE").toString());
				bean.setUwYear(map.get("UW_YEAR")==null?"":map.get("UW_YEAR").toString());
				bean.setUwYearTo(map.get("UW_YEAR_TO")==null?"":map.get("UW_YEAR_TO").toString());
				bean.setBouquetModeYN(map.get("BOUQUET_MODE_YN")==null?"":map.get("BOUQUET_MODE_YN").toString());
				bean.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
				bean.setBaseProposalNo(map.get("BASE_LAYER")==null?"":map.get("BASE_LAYER").toString());
				bean.setContractNo(map.get("CONTRACT_NO")==null?"":map.get("CONTRACT_NO").toString());
				bean.setLayerNo(map.get("LAYER_NO")==null?"0":map.get("LAYER_NO").toString());
				bean.setSectionNo(map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString());
				bean.setOfferNo(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString());
				bean.setAmendId(map.get("AMEND_ID")==null?"":map.get("AMEND_ID").toString());
				if(StringUtils.isBlank(eProposalNo))
				bean.setEproposalNo(proposalNo);
				response.setCommonResponse(bean);
			}
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	private List<Tuple> getExistingProposal(String proposal, String branchCode) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_EXISTING_PROPOSAL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); //'New' POLICY_STATUS,''EXISTING_SHARE
			
			// MAXAmend ID
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( rd.get("rskCedingid"), pms.get("customerId"));
			Predicate a2 = cb.equal( pms.get("customerType"), "C");
			Predicate a3 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			companyName.where(a1,a2,a3);
			
			//baseLayer
			Subquery<String> baseLayer = query.subquery(String.class); 
			Root<PositionMaster> bs = baseLayer.from(PositionMaster.class);
			baseLayer.select(bs.get("baseLayer")).distinct(true);
			Predicate b1 = cb.equal( bs.get("baseLayer"),  pm.get("baseLayer")==null?pm.get("proposalNo"):pm.get("baseLayer"));
			baseLayer.where(b1);
			
			
			//treatyType
			Subquery<String> treatyType = query.subquery(String.class); 
			Root<ConstantDetail> cd = treatyType.from(ConstantDetail.class);
			treatyType.select(cd.get("detailName"));
			Predicate c1 =  cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,new BigDecimal(43) ).otherwise(new BigDecimal(29)) ,
					  cd.get("categoryId"));
			Predicate c2 = cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,rd.get("treatytype") ).otherwise(rd.get("rskBusinessType")) ,
					  cd.get("type"));	
			treatyType.where(c1,c2);
	
			query.multiselect(rd.get("rskInceptionDate").alias("INS_DATE"),rd.get("rskExpiryDate").alias("EXP_DATE"),
					companyName.alias("COMPANY_NAME"),pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("contractNo").alias("CONTRACT_NO"),
					baseLayer.alias("BASE_LAYER"),pm.get("layerNo").alias("LAYER_NO"),pm.get("sectionNo").alias("SECTION_NO"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"),treatyType.alias("TREATY_TYPE"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"),
					pm.get("offerNo").alias("OFFER_NO"),rd.get("rskCedingid").alias("RSK_CEDINGID"),
					rd.get("rskBrokerid").alias("RSK_BROKERID"),pm.get("amendId").alias("AMEND_ID")); 
	
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("offerNo")));
			orderList.add(cb.asc(pm.get("proposalNo")));
	
			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n3 = cb.equal(pm.get("proposalNo"), proposal);
			Predicate n4 = cb.equal(pm.get("contractStatus"), "P");  //P
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			
		}
	return list;
	}

	@Override
	public GetReinsurerInfoRes getReinsurerInfo(GetReinsurerInfoReq bean) {
		GetReinsurerInfoRes response = new GetReinsurerInfoRes();
		List<GetReinsurerInfoRes1> resList = new ArrayList<GetReinsurerInfoRes1>();
		GetReinsurerInfoResponse res1 = new GetReinsurerInfoResponse();
		List<Map<String,Object>>list=null;
		int mailcount=0;
		
		try {
			list = queryImpl.selectList("GET_REINSURER_INFO_NOTIN",new String[] {bean.getBranchCode(),bean.getEproposalNo()});
			if(!CollectionUtils.isEmpty(list)) {
				res1.setPlacementMode(list.get(0).get("PLACEMENT_MODE")==null?"":list.get(0).get("PLACEMENT_MODE").toString());
				res1.setPlacementDisabled("Y");
			}
			//GET_PROPOSAL_MAIL_COUNT
			mailcount = ripRepo.countByBranchCodeAndProposalNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getEproposalNo()),"0");
			if(StringUtils.isNotBlank(bean.getBouquetNo()) && "C".equals(res1.getPlacementMode())) {
				list=queryImpl.selectList("GET_REINSURER_INFO_BOUQUET_NOTIN",new String[] {bean.getBranchCode(),bean.getBouquetNo()});
				//GET_BOUQUET_MAIL_COUNT
				mailcount=ripRepo.countByBranchCodeAndBouquetNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getBouquetNo()),"0");
			}
			if(!CollectionUtils.isEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					Map<String,Object>map=list.get(i);
					GetReinsurerInfoRes1 res = new GetReinsurerInfoRes1();
					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString());
					res.setPlacingBroker(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setProposalNos(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					res.setReinsSNo(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setReinsureName(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setShareOffer(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()));;
		
					if(mailcount>0) {
						res.setDeleteStatus("N");
					}else {
						res.setDeleteStatus("N");
					}
					res.setChangeStatus("N");
					resList.add(res);
					}
				res1.setReinsurerInfoList(resList);;
			}
			response.setCommonResponse(res1);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetPlacementInfoListRes getPlacementInfoList(GetPlacementInfoListReq bean) {
		GetPlacementInfoListRes response = new GetPlacementInfoListRes();
		List<Map<String,Object>>list=null;
		String query="";
		String qutext ="";
		List<GetPlacementInfoListRes1> resList = new ArrayList<GetPlacementInfoListRes1>();
		try {
			String[] obj=new String[2];
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				query="GET_PLACEMENT_BOUQUET_LIST";
				  qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getBouquetNo();
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				query="GET_PLACEMENT_BASE_LIST";
				 qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getBaseProposalNo();
			}else {
				query="GET_PLACEMENT_LIST";
				 qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo();
			}
			if(StringUtils.isNotBlank(bean.getSearchType())) {
				qutext=qutext+" AND P.REINSURER_ID='"+bean.getSearchReinsurerId()+"' AND P.BROKER_ID='"+bean.getSearchBrokerId()+"' AND P.STATUS='"+bean.getSearchStatus()+"'";
			}
			qutext=qutext+" ORDER BY OFFER_NO,P.BASE_PROPOSAL_NO,P.PROPOSAL_NO,P.SNO";
			query1 =queryImpl.setQueryProp(qutext, obj);
    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
    		try {
    			 list = query1.getResultList();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} 
			
			if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Map<String,Object>map=list.get(i);
					GetPlacementInfoListRes1 res = new GetPlacementInfoListRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString()); 
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString()); 
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString()); 
					res.setProposalNo(map.get("ROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString()); 
					res.setCedingCompanyId(map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString()); 
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString()); 
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString()); 
					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString()); 
					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString()); 
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":map.get("SHARE_OFFERED").toString()); 
					res.setShareWritten(map.get("SHARE_WRITTEN")==null?"":map.get("SHARE_WRITTEN").toString()); 
					res.setShareProposalWritten(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":map.get("SHARE_PROPOSAL_WRITTEN").toString()); 
					res.setShareSigned(map.get("SHARE_SIGNED")==null?"":map.get("SHARE_SIGNED").toString()); 
					res.setBrokeragePer(map.get("BROKERAGE_PER")==null?"":map.get("BROKERAGE_PER").toString()); 
					res.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString()); 
					res.setWrittenLineValidity(map.get("WRITTEN_LINE_VALIDITY")==null?"":map.get("WRITTEN_LINE_VALIDITY").toString()); 
					res.setWrittenLineRemarks(map.get("WRITTEN_LINE_REMARKS")==null?"":map.get("WRITTEN_LINE_REMARKS").toString()); 
					res.setShareLineValidity(map.get("SHARE_LINE_VALIDITY")==null?"":map.get("SHARE_LINE_VALIDITY").toString()); 
					res.setShareLineRemarks(map.get("SHARE_LINE_REMARKS")==null?"":map.get("SHARE_LINE_REMARKS").toString()); 
					res.setShareProposedSigned(map.get("SHARE_PROPOSED_SIGNED")==null?"":map.get("SHARE_PROPOSED_SIGNED").toString()); 
					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString()); 
					res.setOfferNo(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString());
					resList.add(res);
					}
			}
			response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonSaveResList savePlacing(SavePlacingReq bean) {
		CommonSaveResList response = new CommonSaveResList();
		List<SavePlacingRes> resList = new ArrayList<SavePlacingRes>();
		List<GetBouquetExistingListRes1> list = null;
		try {
			getPlacementNo(bean);
			if("C".equalsIgnoreCase(bean.getPlacementMode())) {
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				list =	dropDownImple.getBouquetExistingList(bean.getBranchCode(),bean.getBouquetNo(),bean.getBouquetModeYN()).getCommonResponse();
				}else {
					list = dropDownImple.getBaseLayerExistingList(bean.getBranchCode(),bean.getBaseProposalNo()).getCommonResponse();
				}
				for(int i=0;i<list.size();i++) {
					SavePlacingRes res = new SavePlacingRes();
					res.setEproposalNo(list.get(i).getProposalNo()==null?"":list.get(i).getProposalNo().toString());
					resList.add(res);
					insertPlacing(bean);
				}
			}else {
				insertPlacing(bean);
			}
			response.setResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Override
	public GetPlacementNoRes getPlacementNo(SavePlacingReq bean) {
		GetPlacementNoRes response = new GetPlacementNoRes();
		GetPlacementNoRes1 res = new GetPlacementNoRes1();
		String placementNo="",statusNo;
		TtrnRiPlacement list= new TtrnRiPlacement();
		try {
			if("C".equalsIgnoreCase(bean.getPlacementMode())) {
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
					//GET_PLACEMENT_NO_BOUQUET
					list = ripRepo.findDistinctByBouquetNo(new BigDecimal(bean.getBouquetNo()));
				}else {
					//GET_PLACEMENT_NO_BASELAYER
					list = ripRepo.findDistinctByBaseProposalNo(new BigDecimal(bean.getBaseProposalNo()));
				}
			}else {
				//GET_PLACEMENT_NO_PROPOSAL
				list = ripRepo.findDistinctByProposalNo(new BigDecimal(bean.getEproposalNo()));
			}
			if(list != null) {
				placementNo = list.getPlacementNo()==null?"":list.getPlacementNo().toString();
			}
			if(StringUtils.isBlank(placementNo)) {
			 	placementNo= fm.getSequence("PlacementNo","0","0", bean.getBranchCode(),"","");
			}
			statusNo= fm.getSequence("StatusNo","0","0", bean.getBranchCode(),"","");
			res.setStatusNo(statusNo);
			res.setPlacementNo(placementNo);
			res.setStatusNo(statusNo);
			response.setCommonResponse(res);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Override
	public InsertPlacingRes insertPlacing(SavePlacingReq bean) {
		InsertPlacingRes response = new InsertPlacingRes();
		String plamendId="0",currentStatus="O";
		Tuple result=null;
		TtrnRiPlacement entity = new TtrnRiPlacement();
		List<InsertPlacingRes1> resList = new ArrayList<InsertPlacingRes1>();
		try {
			proposalInfo(bean.getBranchCode(),bean.getProposalNo(),bean.getEproposalNo());
			//DeletePlacement(bean);
			//INSERT_PLACEMENT_INFO
			for(int i=0;i<bean.getReinsListReq().size();i++) {
				InsertPlacingRes1 res = new InsertPlacingRes1();
				ReinsListReq req =	bean.getReinsListReq().get(i);
				res.setReinsurerId(req.getReinsureName());
				res.setBrokerId(req.getPlacingBroker());
				plamendId=getMaxAmendId(bean.getBranchCode(),bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId());
				res.setPlacementamendId(StringUtils.isBlank(plamendId)?"0":plamendId);
				result=getPlacementDetails(bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId(),bean.getBranchCode());
				if(result!=null) {
					currentStatus=result.get("STATUS")==null?"O":result.get("STATUS").toString();
				}
				entity.setPlacementNo(new BigDecimal(bean.getPlacementNo()));
				entity.setSno(new BigDecimal(req.getReinsSNo()));
				entity.setBouquetNo(new BigDecimal(bean.getBouquetNo()));
				entity.setBaseProposalNo(new BigDecimal(bean.getBaseProposalNo()));
				entity.setProposalNo(new BigDecimal(bean.getEproposalNo()));
				entity.setContractNo(new BigDecimal(bean.getContractNo()));
				entity.setLayerNo(new BigDecimal(bean.getLayerNo()));
				entity.setSectionNo(new BigDecimal(bean.getSectionNo()));
				entity.setAmendId(new BigDecimal(bean.getAmendId()));
				entity.setReinsurerId(req.getReinsureName());
				entity.setBrokerId(req.getPlacingBroker());
				entity.setShareOffered(new BigDecimal(req.getShareOffer()));
				entity.setBranchCode(bean.getBranchCode());
				entity.setSysDate(new Date());
				entity.setCedingCompanyId(bean.getCedingCompany());
				entity.setPlacementMode(bean.getPlacementMode());
				entity.setStatus(currentStatus);
				entity.setPlacementAmendId(new BigDecimal(bean.getPlacementamendId()));
				entity.setStatusNo(new BigDecimal(bean.getStatusNo()));
				entity.setApproveStatus("Y");
				entity.setUserId(bean.getUserId());
				ripRepo.saveAndFlush(entity);		
				resList.add(res);
				}
			response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	private String getMaxAmendId(String branchCode, String eproposalNo, String reinsurerId, String brokerId) {
		String plamendId="0";
		try {
			//GET_PLACEMENT_MAX_AMENDID
			TtrnRiPlacement  list = ripRepo.findTop1ByBranchCodeAndProposalNoAndReinsurerIdAndBrokerIdOrderByPlacementAmendIdDesc(
					branchCode,new BigDecimal(eproposalNo),reinsurerId,brokerId);
			if(list != null) {
			plamendId= String.valueOf(list.getPlacementAmendId().intValue()+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plamendId;
	}
	private Tuple getPlacementDetails(String proposalNo, String reinsuerId, String brokerId,String branchCode) {
		Tuple map=null;
		try {
			//GET_PLACEMENT_DETAIL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);

			query.multiselect(pm.get("placementNo").alias("PLACEMENT_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),
					pm.get("sectionNo").alias("SECTION_NO"),pm.get("amendId").alias("AMEND_ID"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),pm.get("reinsurerId").alias("REINSURER_ID"),
					pm.get("brokerId").alias("BROKER_ID"),pm.get("shareOffered").alias("SHARE_OFFERED"),
					pm.get("placementMode").alias("PLACEMENT_MODE"),pm.get("status").alias("STATUS")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsuerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();

			if(!CollectionUtils.isEmpty(list)) {
					map=list.get(0);
			}
		}
		catch (Exception e) {
				e.printStackTrace();
		}	
		return map;
	}

	@Override
	public GetPlacingInfoRes getPlacingInfo(GetPlacingInfoReq bean) {
		GetPlacingInfoRes response = new GetPlacingInfoRes();
		List<Map<String,Object>>list=null;
		String query="";
		String qutext ="";
		List<GetPlacingInfoRes1> resList = new ArrayList<GetPlacingInfoRes1>();
		try {
			String[] obj=new String[2];
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				query="GET_PLACING_BOUQUET_LIST";
				  qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getBouquetNo();
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				query="GET_PLACING_BASELAYER_LIST";
				  qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getBaseProposalNo();
			}else {
				query="GET_PLACING_LIST";
				  qutext = prop.getProperty(query);
				obj[0]=bean.getBranchCode();
				obj[1]=StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo();
			}
			if(StringUtils.isNotBlank(bean.getSearchType())) {
				qutext=qutext+" AND P.REINSURER_ID='"+bean.getSearchReinsurerId()+"' AND P.BROKER_ID='"+bean.getSearchBrokerId()+"' AND P.STATUS='"+bean.getSearchStatus()+"'";
			}
			//query=query+" ORDER BY P.BASE_PROPOSAL_NO,P.PROPOSAL_NO,P.SNO";
			query1 =queryImpl.setQueryProp(qutext, obj);
    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
    		try {
    			 list = query1.getResultList();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} 
    		if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Map<String,Object>map=list.get(i);
					GetPlacingInfoRes1 res = new GetPlacingInfoRes1();
					  res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());  
					  res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());  
					  res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());  
					  res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());  
					  res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString());  
					  res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString());  
					  res.setShareOffered(map.get("SHARE_OFFERED")==null?"":map.get("SHARE_OFFERED").toString());  
					  res.setOfferStatus(map.get("OFFER_STATUS")==null?"":map.get("OFFER_STATUS").toString()); 
					  resList.add(res);
					  }
				}
			
    		response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
}

	@Override
	public EditPlacingDetailsRes editPlacingDetails(EditPlacingDetailsReq bean) {
		EditPlacingDetailsRes response = new EditPlacingDetailsRes();
		List<EditPlacingDetailsRes1> resList = new ArrayList<EditPlacingDetailsRes1>();
		EditPlacingDetailsResponse res1 = new EditPlacingDetailsResponse();
		List<Tuple> result=null;
		try {
			if(StringUtils.isBlank(bean.getSearchType())) {
				result=GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId());
			}else {
				result=GetPlacementSearchEdit(bean);
			}
			if(!CollectionUtils.isEmpty(result)) {
				for(int i=0;i<result.size();i++) {
					Tuple map=result.get(i);
					EditPlacingDetailsRes1 res = new EditPlacingDetailsRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setBaseproposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString());
					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString());
					res.setCedingCompany(map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString());
					res.setCedingCompanyName(map.get("CEDING_COMPANY_NAME")==null?"":map.get("CEDING_COMPANY_NAME").toString());
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()));
					res.setWrittenLine(map.get("SHARE_WRITTEN")==null?"":dropDownImple.formattereight(map.get("SHARE_WRITTEN").toString()));
					res.setBrokerage(map.get("BROKERAGE_PER")==null?"":map.get("BROKERAGE_PER").toString());
					res.setWrittenvaliditydate(map.get("WRITTEN_LINE_VALIDITY")==null?"":map.get("WRITTEN_LINE_VALIDITY").toString());
					res.setWrittenvalidityRemarks(map.get("WRITTEN_LINE_REMARKS")==null?"":map.get("WRITTEN_LINE_REMARKS").toString());
					res.setProposedWL(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":dropDownImple.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()));
					res.setSignedLine(map.get("SHARE_SIGNED")==null?"":dropDownImple.formattereight(map.get("SHARE_SIGNED").toString()));
					res.setProposedSL(map.get("SHARE_PROPOSED_SIGNED")==null?"":dropDownImple.formattereight(map.get("SHARE_PROPOSED_SIGNED").toString()));
					res.setReoffer(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()));
					// doubt not in table TQR_BROKERAGE_AMT
					//	res.setTqrBrokerageAmt(map.get("TQR_BROKERAGE_AMT")==null?"":dropDownImple.formattereight(map.get("TQR_BROKERAGE_AMT").toString()));
					res.setSignedLineValidity(map.get("SHARE_LINE_VALIDITY")==null?"":map.get("SHARE_LINE_VALIDITY").toString());
					res.setSignedLineRemarks(map.get("SHARE_LINE_REMARKS")==null?"":map.get("SHARE_LINE_REMARKS").toString());
					res.setEmailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString());
					res.setPsignedLine(map.get("SHARE_SIGNED")==null?"":dropDownImple.formattereight(map.get("SHARE_SIGNED").toString()));
					resList.add(res);
					}
				res1.setPlacingDetails(resList);
				}
			if(StringUtils.isBlank(bean.getSearchType())) {
				//GET_PLACEMENT_STATUS_EDIT
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);

				query.multiselect(pm.get("newStatus").alias("NEW_STATUS"),pm.get("currentStatus").alias("CURRENT_STATUS")); 

				//status
				Subquery<Long> status = query.subquery(Long.class); 
				Root<TtrnRiPlacementStatus> pms = status.from(TtrnRiPlacementStatus.class);
				status.select(cb.max(pms.get("statusNo")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
				Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
				Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
				status.where(a1,a2,a3,a4);

				Predicate n1 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
				Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
				Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
				Predicate n5 = cb.equal(pm.get("statusNo"), status);
				query.where(n1,n2,n3,n4,n5);
				
				TypedQuery<Tuple> res = em.createQuery(query);
				List<Tuple> list = res.getResultList();
			 
			if(!CollectionUtils.isEmpty(list)) {
				Tuple map=list.get(0);
				res1.setCurrentStatus(map.get("NEW_STATUS")==null?"":map.get("NEW_STATUS").toString());
				
			}else {
				if(StringUtils.isBlank(bean.getNewStatus())) {
					res1.setCurrentStatus(StringUtils.isBlank(bean.getSearchStatus())?"O":bean.getSearchStatus());
				}
			}
			}else {
				res1.setCurrentStatus(StringUtils.isBlank(bean.getSearchStatus())?"O":bean.getSearchStatus());
			}
			response.setCommonResponse(res1);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	private List<Tuple> GetPlacementEdit(String branchCode, String brokerId, String eproposalNo, String reinsurerId) {
		List<Tuple> list =null;
		try {
			//GET_PLACEMENT_EDIT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			cedingCompanyName.where(b1,b2,b3);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			mailStatus.where(g1,g2,g3,g4);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS"),
					pm.get("placementAmendId").alias("PLACEMENT_AMEND_ID"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),
					pm.get("sectionNo").alias("SECTION_NO")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), eproposalNo);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsurerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			 list = res.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private List<Tuple> GetPlacementSearchEdit(EditPlacingDetailsReq bean) {
		List<Tuple> list=null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			cedingCompanyName.where(b1,b2,b3);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			mailStatus.where(g1,g2,g3,g4);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS")); 
					
			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getSearchReinsurerId());
			Predicate n4 = cb.equal(pm.get("brokerId"), bean.getSearchBrokerId());
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			Predicate n6 = cb.equal(pm.get("status"), bean.getSearchStatus());
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_PLACEMENT_SEARCHBQ_EDIT
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
				query.where(n1,n2,n3,n4,n5,n6);
				
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_PLACEMENT_SEARCHBP_EDIT
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
				query.where(n1,n2,n3,n4,n5,n6);
			}else {
				//GET_PLACEMENT_SEARCH_EDIT
				Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
				query.where(n1,n2,n3,n4,n5,n6);
			}
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public CommonResponse updatePlacement(UpdatePlacementReq bean) {
		CommonResponse response = new CommonResponse();
		String plamendId="",statusNo="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Tuple result=null;
			if(StringUtils.isBlank(bean.getStatusNo())) {
			statusNo=fm.getSequence("StatusNo","0","0", bean.getBranchCode(),"","");
			bean.setStatusNo(statusNo);
			}
			//INSERT_PLACEMENT_DETAIL
			for(int i=0;i<bean.getPlacementListReq().size();i++) {
				TtrnRiPlacement entity = new TtrnRiPlacement();
				UpdatePlacementListReq req =bean.getPlacementListReq().get(i);
				bean.setReinsurerId(req.getReinsurerId());
				bean.setBrokerId(req.getBrokerId());
				bean.setEproposalNo(req.getProposalNo());
				plamendId=getMaxAmendId(bean.getBranchCode(),bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId());
				bean.setPlacementamendId(StringUtils.isBlank(plamendId)?"0":plamendId);
				
				result=getPlacementDetails(bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId(),bean.getBranchCode());
				entity.setPlacementNo(result.get("PLACEMENT_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("PLACEMENT_NO").toString()));
				entity.setSno(result.get("SNO")==null?BigDecimal.ZERO:new BigDecimal(result.get("SNO").toString()));
				entity.setBouquetNo(result.get("BOUQUET_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("BOUQUET_NO").toString()));
				entity.setBaseProposalNo(result.get("BASE_PROPOSAL_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("BASE_PROPOSAL_NO").toString()));
				entity.setProposalNo(result.get("PROPOSAL_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("PROPOSAL_NO").toString()));
				entity.setContractNo(result.get("CONTRACT_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("CONTRACT_NO").toString()));
				entity.setLayerNo(result.get("LAYER_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("LAYER_NO").toString()));
				entity.setSectionNo(result.get("SECTION_NO")==null?BigDecimal.ZERO:new BigDecimal(result.get("SECTION_NO").toString()));
				entity.setAmendId(result.get("AMEND_ID")==null?BigDecimal.ZERO:new BigDecimal(result.get("AMEND_ID").toString()));
				entity.setCedingCompanyId(result.get("CEDING_COMPANY_ID")==null?"":result.get("CEDING_COMPANY_ID").toString());
				entity.setReinsurerId(result.get("REINSURER_ID")==null?"":result.get("REINSURER_ID").toString());
				entity.setBrokerId(result.get("BROKER_ID")==null?"":result.get("BROKER_ID").toString());
				entity.setPlacementMode(result.get("PLACEMENT_MODE")==null?"":result.get("PLACEMENT_MODE").toString());
				entity.setStatus(bean.getNewStatus());;
				
				if("RO".equalsIgnoreCase(bean.getNewStatus())) {
					entity.setShareOffered(new BigDecimal(req.getReoffer()));
					
				}else {
					entity.setShareOffered(new BigDecimal(req.getShareOffered()));
					entity.setShareWritten(new BigDecimal(req.getWrittenLine()));
					entity.setShareProposalWritten(new BigDecimal(req.getProposedWL()));
					entity.setWrittenLineValidity(sdf.parse(req.getWrittenvaliditydate()));
					entity.setWrittenLineRemarks(req.getWrittenvalidityRemarks());
					entity.setShareSigned(new BigDecimal(req.getSignedLine()));
					entity.setShareLineValidity(sdf.parse(req.getSignedLineValidity()));
					entity.setShareLineRemarks(req.getSignedLineRemarks());
					entity.setShareProposedSigned(new BigDecimal(req.getProposedSL()));
					entity.setBrokerage(new BigDecimal(req.getBrokerage()));
				}
				entity.setPlacementAmendId(new BigDecimal(bean.getPlacementamendId()));
				entity.setStatusNo(new BigDecimal(bean.getStatusNo()));
				entity.setApproveStatus("Y");
				entity.setUserId(bean.getUserId());
				entity.setBranchCode(bean.getBranchCode());
				entity.setSysDate(new Date());
				ripRepo.saveAndFlush(entity);
			}
	//		updateStatus(bean,""); status =""
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Override
	public CommonResponse updateStatus(UpdatePlacementReq bean) {
		CommonResponse response = new CommonResponse();
		String corresId="",statusNo="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			List<Map<String,Object>> list  = queryImpl.selectSingle("SELECT  CORRESPONDENT_SEQ.NEXTVAL seqval FROM DUAL", new String[] {});
			if (!CollectionUtils.isEmpty(list)) {
				corresId = list.get(0).get("SEQVAL") == null ? "" : list.get(0).get("SEQVAL").toString();
			}
			bean.setCorresId(corresId);
			if(StringUtils.isBlank(bean.getStatusNo())) {
				statusNo= fm.getSequence("StatusNo","0","0", bean.getBranchCode(),"","");
				bean.setStatusNo(statusNo);
			}
			//INSERT_PLACEMENT_STATUS
			for(int i=0;i<bean.getPlacementListReq().size();i++) {
				UpdatePlacementListReq req =bean.getPlacementListReq().get(i);
				TtrnRiPlacementStatus entity = new TtrnRiPlacementStatus();
				entity.setSno(new BigDecimal(req.getSno()));
				entity.setBouquetNo(new BigDecimal(req.getBouquetNo()));
				entity.setBaseProposalNo(new BigDecimal(req.getBaseproposalNo()));
				entity.setProposalNo(new BigDecimal(req.getProposalNo()));
				TtrnRiPlacementStatus list1 =  placeStatusRepo.findTop1ByProposalNoAndReinsurerIdAndBrokerIdOrderByAmendIdDesc
				(new BigDecimal(req.getProposalNo()),req.getReinsurerId(),req.getBrokerId());
				int amend = 0;
				if(list1 != null) {
					 amend = list1.getAmendId()==null?0:(list1.getAmendId().intValue()+1);
				}
				entity.setAmendId(new BigDecimal(amend));;
				entity.setReinsurerId(req.getReinsurerId());
				entity.setBrokerId(req.getBrokerId());
				entity.setEmailBy(bean.getEmailBy());
				entity.setCurrentStatus(StringUtils.isBlank(bean.getCurrentStatus())?"O":bean.getCurrentStatus());
				entity.setNewStatus(StringUtils.isBlank(bean.getNewStatus())?bean.getStatus():bean.getNewStatus());
				entity.setCedentCorrespondence(bean.getCedentCorrespondent());
				entity.setReinsurerCorrespondence(bean.getReinsurerCorrespondent());
				entity.setTqrCorrespondence(bean.getTqrCorrespondent());
				entity.setUpdateDate(sdf.parse(bean.getUpdateDate()));
				entity.setStatus("Y");
				entity.setBranchCode(bean.getBranchCode());
				entity.setSysDate(new Date());
				entity.setCorrespondentId(new BigDecimal(bean.getCorresId()));
				entity.setStatusNo(new BigDecimal(bean.getStatusNo()));
				entity.setUserId(new BigDecimal(bean.getUserId()));
				entity.setApproverStatus("Y");				
				placeStatusRepo.saveAndFlush(entity);		
			}
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public UploadDocumentRes uploadDocument(UploadDocumentReq bean) {
		UploadDocumentRes response = new UploadDocumentRes();
		List<UploadDocumentRes1> resList = new ArrayList<UploadDocumentRes1>();
		try {
		for(int i=0;i<bean.getUploadDocumentReq1().size();i++) {
			UploadDocumentReq1 req = bean.getUploadDocumentReq1().get(i);
			UploadDocumentRes1 res = new UploadDocumentRes1();
			res.setSno(req.getSno());
			res.setBouquetNo(req.getBouquetNo());
			res.setBaseproposalNo(req.getBaseproposalNo());
			res.setBrokerId(req.getBrokerId());
			res.setReinsurerId(req.getReinsurerId());
			res.setProposalNo(req.getProposalNo());
			resList.add(res);
			insertDocdetails(bean);
		}
		response.setCommonResponse(resList);
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}
	public InsertDocdetailsRes insertDocdetails(UploadDocumentReq bean) {
		InsertDocdetailsRes res = new InsertDocdetailsRes();
		 try {
			 if(bean.getUpload()!=null) {
				 String filePath=bean.getFilePath();
				
				File tmpFile = new File(filePath);
				if(!tmpFile.exists()){
					tmpFile.mkdir();
				}
				//GET_DOC_SEQUENCE
				NotificationAttachmentDetail list = notiRepo.findTop1ByOrderByDocIdDesc();
				String docId="";
				if(list!=null) {
					docId =	list.getDocId()==null?"0":String.valueOf(list.getDocId().intValue()+1);
				}
				for(int i=0;i<bean.getInsertDocdetailsReq().size();i++) {
					InsertDocdetailsReq req =  bean.getInsertDocdetailsReq().get(i);
					final String orgFileName=req.getUploadFileName();
				Calendar cal = Calendar.getInstance();
				String time = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"
				+cal.get(Calendar.YEAR)+"_"+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND);
				String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
				String fileName = orgFileName.substring(0, orgFileName.lastIndexOf("."))+"_"+time;
				fileName = fileName + ext;
				final File copyFile = new File(filePath+fileName);
				
				FileUtils.copyFile((File) req.getUpload(), copyFile);
				res.setFileName(fileName);
				
				//INSET_NOTIFY_ATTACHEMENT
				NotificationAttachmentDetail entity = new NotificationAttachmentDetail();	
				entity.setDocId(StringUtils.isBlank(docId)?new BigDecimal("1"):new BigDecimal(docId));
				entity.setDocType(req.getDocTypeId());
				entity.setSno(new BigDecimal(bean.getSno()));
				entity.setBouquetNo(new BigDecimal(bean.getBouquetNo()));
				entity.setBaseProposalNo(new BigDecimal(bean.getBaseproposalNo()));
				entity.setProposalNo(new BigDecimal(bean.getProposalNo()));
				entity.setReinsurerId(bean.getReinsurerId());
				entity.setBrokerId(bean.getBrokerId());
				entity.setOrgFileName(req.getUploadFileName());
				entity.setOurFileName(res.getFileName());
				entity.setFileLocation(bean.getFilePath());
				entity.setBranchCode(bean.getBranchCode());
				entity.setUserId(bean.getUserId());
				entity.setEntryDate(new Date());
				entity.setCorrespondentId(StringUtils.isBlank(bean.getCorresId())?BigDecimal.ZERO:new BigDecimal(bean.getCorresId()));
				entity.setDocDescription(req.getDocDesc());
				notiRepo.saveAndFlush(entity);
				}
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return res; 
	 }

	@Override
	public AttachFileRes attachFile(AttachFileReq bean) {
		AttachFileRes response = new AttachFileRes();
		List<AttachFileRes1> resList = new ArrayList<AttachFileRes1>(); 
		List<Tuple> list=null;
		try {
			
			if("C".equals(bean.getPlacementMode())) {
				list=GetPlacementBouquet(bean.getBranchCode(),bean.getBrokerId(),bean.getBouquetNo(),bean.getReinsurerId(),bean.getBaseProposalNo());
			}
			else {
				list=GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId())	;					
			}
			
			if(!CollectionUtils.isEmpty(list)) {
				for(int  i=0;i<list.size();i++) {
					Tuple  map=list.get(i);
					AttachFileRes1 res = new AttachFileRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setEproposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					resList.add(res);
					//	insertDocdetails(bean);
					}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	private List<Tuple> GetPlacementBouquet(String branchCode, String brokerId, String bouquetNo, String reinsurerId,String baseProposalNo ) {
		List<Tuple> list=null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			cedingCompanyName.where(b1,b2,b3);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			mailStatus.where(g1,g2,g3,g4);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),
					pm.get("sectionNo").alias("SECTION_NO")); 
					

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsurerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);

			if(StringUtils.isNotBlank(bouquetNo)) {
				//GET_PLACEMENT_BOUQUET
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bouquetNo);
				query.where(n1,n2,n3,n4,n5);
			}else {
				//GET_PLACEMENT_BASELAYER
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), baseProposalNo);
				query.where(n1,n2,n3,n4,n5);
			}
			
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public CommonResponse sendMail(SendMailReq bean) {
		CommonResponse response = new CommonResponse();
		try {
			MailMaster mapt= getMailDetails("51"); //new CommonDAO()
			String hostName=mapt.getSmtpHost();
			String user = mapt.getSmtpUser();
			String pwd = mapt.getSmtpPwd();
			String port=mapt.getSmtpPort();
			String mailform = mapt.getSmtpAddress();
			String shortAddress = mapt.getSmtpShortAddress();
			String subject = bean.getMailSubject();
			String toAddress = bean.getMailTo();
			String ccAddress = bean.getMailCC();
			String mailBody= bean.getMailBody()+"<br/>"+bean.getMailRemarks()+""+bean.getMailRegards();
			bean.setMailBody(mailBody);
			if(toAddress!=null && !"".equals(toAddress)){
				String[] toAddresses = (toAddress.indexOf(",")!=-1)?toAddress.split(","):new String[]{toAddress};
				String[] ccAddresses = new String[0];
				if(ccAddress!=null && !"".equals(ccAddress)){
					ccAddresses = (ccAddress.indexOf(",")!=-1)?ccAddress.split(","):new String[]{ccAddress};
				}
		//	insertMailDetails(bean);
			Multipart multipart=GetMailAttachment(bean);
			String status=sendResponseMail(hostName, user, pwd, mailform, subject, multipart, toAddresses, ccAddresses, shortAddress,port);
			if("Success".equals(status) && "P".equals(bean.getMailType())) {
			//	updateStatus(bean,"P"); status = "P"
			}
	//		updateMailDetails(bean,status);
			} 
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public MailMaster getMailDetails(String appId){
		MailMaster details =new MailMaster();
    	try {
    		//GET_MAIL_DETAILS
    		List<MailMaster> list = mmRepo.findByAppId(appId);
        	if(list != null && list.size()>0){
	        	details = list.get(0);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        	}
    
    	return details;
  }
	
	private Multipart GetMailAttachment(SendMailReq bean) {
		Multipart multipart = new MimeMultipart();
		try {
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(bean.getMailBody(),"text/html;charset=UTF-8");
			multipart.addBodyPart(messageBodyPart1);
			GetExistingAttachListReq req = new GetExistingAttachListReq();
			req.setBranchCode(bean.getBranchCode());
			req.setBrokerId(bean.getBrokerId());
			req.setCorresId(bean.getCorresId());
			req.setEproposalNo(bean.getEproposalNo());
			req.setProposalNo(bean.getProposalNo());
			req.setReinsurerId(bean.getReinsurerId());
			
			List<GetExistingAttachListRes1>  list=getExistingAttachList(req).getCommonResponse();
			if(!CollectionUtils.isEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					GetExistingAttachListRes1 map=list.get(i);
					BodyPart messageBodyPart = new MimeBodyPart();
//					String filePath=ServletActionContext.getServletContext().getRealPath("/")+"documents/";
//					String fileName=map.getOrgFileName()==null?"":map.getOrgFileName().toString();
//					String orgfileName=map.getOurFileName()==null?"":map.getOurFileName().toString();
//					if(fileName!=null ){
//						DataSource source = new FileDataSource(filePath+orgfileName);
//						messageBodyPart.setDataHandler(new DataHandler(source));
//						messageBodyPart.setFileName(fileName);
//						multipart.addBodyPart(messageBodyPart);
//					 }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return multipart;
	}

	public String sendResponseMail(final String SMTP_HOST_NAME, final String user,  final String pwd, final String SMTP_MAIL_FROM, final String subject,
    		final Multipart message, final String[] toAddress, final String[] ccAddress, final String SMTP_SHORT_ADDRESS,final String SMTP_PORT){
    	
    	 String status="";
    	SMTP_AUTH_USER = user;
    	SMTP_AUTH_PWD = pwd;
    	try{
    		
    		 Properties props = System.getProperties();
    		 props.setProperty("mail.smtp.host", SMTP_HOST_NAME);
    		 props.put("mail.smtp.port", SMTP_PORT);
    		 props.put("mail.smtp.starttls.enable", "true");
    		 props.put("mail.smtp.ssl.protocols", "TLSv1.2");
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
				
			msg1.setContent(message);
			
			System.out.println(msg1);
			Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST_NAME, Integer.parseInt(SMTP_PORT),SMTP_AUTH_USER, SMTP_AUTH_PWD);
            transport.sendMessage(msg1, msg1.getAllRecipients());
			status="Success";
			System.out.println("Mail successfully sent");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.out.println("Mail successfully Not Senrsent");
			status="Failed";
		}
    	return status;
    }
	 private class SMTPAuthenticator extends javax.mail.Authenticator{
			public PasswordAuthentication getPasswordAuthentication(){
				String username = SMTP_AUTH_USER;
				String password = SMTP_AUTH_PWD;
				return new PasswordAuthentication(username, password);
			}
	}
	@Override
	public InsertMailDetailsRes insertMailDetails(InsertMailDetailsReq bean) {
		InsertMailDetailsRes response = new InsertMailDetailsRes();
		List<InsertMailDetailsRes1> resList = new ArrayList<InsertMailDetailsRes1>();
		List<Tuple>list=null;
		try {
			//INSERT_MAIL_NOTIFICATION
			if("C".equals(bean.getPlacementMode())) {
				list=GetPlacementBouquet(bean.getBranchCode(),bean.getBrokerId(),bean.getBouquetNo(),bean.getReinsurerId(),bean.getBaseProposalNo());
			}
			else {
				list=GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId());
			}
			
			if(!CollectionUtils.isEmpty(list)) {
				for(int  i=0;i<list.size();i++) {
					Tuple map=list.get(i);
					InsertMailDetailsRes1 res = new InsertMailDetailsRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setEproposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					resList.add(res);
					String recNo ="";
					List<Map<String,Object>> list1  = queryImpl.selectSingle("select MAIL_RECORD_SEQ.NEXTVAL NEXTVAL from dual", new String[] {});
					if (!CollectionUtils.isEmpty(list1)) {
						recNo = list1.get(0).get("NEXTVAL") == null ? "" : list1.get(0).get("NEXTVAL").toString();
					}
					
					MailNotificationDetail entity = new MailNotificationDetail();
					entity.setMailRecordNo(new BigDecimal(recNo));
					entity.setSno(new BigDecimal(res.getSno()));
					entity.setBouquetNo(new BigDecimal(res.getBouquetNo()));
					entity.setBaseProposalNo(new BigDecimal(res.getBaseProposalNo()));
					entity.setProposalNo(new BigDecimal(res.getEproposalNo()));
					entity.setContractNo(map.get("CONTRACT_NO")==null?BigDecimal.ZERO:new BigDecimal(map.get("CONTRACT_NO").toString()));;
					entity.setLayerNo(map.get("LAYER_NO")==null?BigDecimal.ZERO:new BigDecimal(map.get("LAYER_NO").toString()));
					entity.setSectionNo(map.get("SECTION_NO")==null?BigDecimal.ZERO:new BigDecimal(map.get("SECTION_NO").toString()));
					entity.setCedingCompanyId(map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString());
					entity.setReinsurerId(res.getReinsurerId());
					entity.setBrokerId(res.getBrokerId());
					entity.setClientId("63".equals(res.getBrokerId())?res.getReinsurerId():res.getBrokerId());
					entity.setMailType(bean.getMailType());
					entity.setMailTo(bean.getMailTo());
					entity.setMailCc(bean.getMailCC());
					entity.setMailSubject(bean.getMailSubject());
					entity.setMailBody(bean.getMailBody());
					entity.setBranchCode(bean.getBranchCode());
					entity.setUserId(bean.getUserId());
					entity.setEntryDate(new Date());
					entity.setMailStatus("Pending");;
					mailnotiRepo.saveAndFlush(entity);
					}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
   
	@Transactional
	@Override
	public CommonResponse updateMailDetails(UpdateMailDetailsReq bean) {
		CommonResponse response = new CommonResponse();
		try {
		for(int i=0;i<bean.getUpdateMailDetailsReqList().size();i++) {
			UpdateMailDetailsReq1 req = bean.getUpdateMailDetailsReqList().get(i);
			//UPDATE_MAIL_NOTIFICATION
			MailNotificationDetail entity = mailnotiRepo.findByProposalNoAndReinsurerIdAndBrokerIdAndBranchCode(
					new BigDecimal(req.getProposalNo()),req.getReinsurerId(),req.getBrokerId(),bean.getBranchCode());
			entity.setMailStatus(bean.getStatus());
			entity.setUpdateDate(new Date());;
			entity.setStatusNo(bean.getStatusNo());
			mailnotiRepo.saveAndFlush(entity);
			
			//UPDATE_PLACEMENT_STATUS
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			// create update
			CriteriaUpdate<TtrnRiPlacement> update = cb.createCriteriaUpdate(TtrnRiPlacement.class);
			// set the root class
			Root<TtrnRiPlacement> m = update.from(TtrnRiPlacement.class);
			// set update and where clause
			update.set("status", bean.getMailType());
			update.set("statusNo",new BigDecimal(bean.getStatusNo()));
			
			// MAXAmend ID
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( m.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( m.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( m.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( m.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), req.getProposalNo());
			Predicate n2 = cb.equal(m.get("reinsurerId"), req.getReinsurerId());
			Predicate n3 = cb.equal(m.get("brokerId"),req.getBrokerId());
			Predicate n4 = cb.equal(m.get("branchCode"), bean.getBranchCode());
			Predicate n5 = cb.equal(m.get("placementAmendId"), amend);
			update.where(n1,n2,n3,n4,n5);
			// perform update
			em.createQuery(update).executeUpdate();

			//UPDATE_ATTACHMENT_MAIL	
			CriteriaUpdate<NotificationAttachmentDetail> update1 = cb.createCriteriaUpdate(NotificationAttachmentDetail.class);
			Root<NotificationAttachmentDetail> pm = update1.from(NotificationAttachmentDetail.class);
			
			// MAXAmend ID
			Subquery<Long> mailRecordNo = update1.subquery(Long.class); 
			Root<MailNotificationDetail> mr = mailRecordNo.from(MailNotificationDetail.class);
			mailRecordNo.select(cb.max(mr.get("mailRecordNo")));
			Predicate b2 = cb.equal( pm.get("proposalNo"), mr.get("proposalNo"));
			Predicate b3 = cb.equal( pm.get("reinsurerId"), mr.get("reinsurerId"));
			Predicate b4 = cb.equal( pm.get("brokerId"), mr.get("brokerId"));
			mailRecordNo.where(b2,b3,b4);
			
			update1.set("mailRecordNo",mailRecordNo==null?null:mailRecordNo);
			update1.set("statusNo", new BigDecimal( bean.getStatusNo()));
			
			Predicate m1 = cb.equal(pm.get("proposalNo"), req.getProposalNo());
			Predicate m2 = cb.equal(pm.get("reinsurerId"), req.getReinsurerId());
			Predicate m3 = cb.equal(pm.get("brokerId"),req.getBrokerId());
			Predicate m4 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			update1.where(m1,m2,m3,m4);
			em.createQuery(update1).executeUpdate();
			
			//UPDATE_PLACEMENT_STATUS_MAIL
			CriteriaUpdate<TtrnRiPlacementStatus> update2 = cb.createCriteriaUpdate(TtrnRiPlacementStatus.class);
			Root<TtrnRiPlacementStatus> ri = update2.from(TtrnRiPlacementStatus.class);
			
			// mailRecordNo
			Subquery<Long> mailRecordNo1 = update2.subquery(Long.class); 
			Root<MailNotificationDetail> mr1 = mailRecordNo1.from(MailNotificationDetail.class);
			mailRecordNo1.select(cb.max(mr1.get("mailRecordNo")));
			Predicate c2 = cb.equal( ri.get("proposalNo"), mr1.get("proposalNo"));
			Predicate c3 = cb.equal( ri.get("reinsurerId"), mr1.get("reinsurerId"));
			Predicate c4 = cb.equal( ri.get("brokerId"), mr1.get("brokerId"));
			mailRecordNo1.where(c2,c3,c4);
			
			update2.set("emailRecordid", mailRecordNo1==null?null:mailRecordNo1);
			
			//statusNo
			Subquery<Long> statusNo = update2.subquery(Long.class); 
			Root<TtrnRiPlacementStatus> ris = statusNo.from(TtrnRiPlacementStatus.class);
			statusNo.select(cb.max(ris.get("statusNo")));
			Predicate e1 = cb.equal( ri.get("branchCode"), ris.get("branchCode"));
			Predicate e2 = cb.equal( ri.get("proposalNo"), ris.get("proposalNo"));
			Predicate e3 = cb.equal( ri.get("reinsurerId"), ris.get("reinsurerId"));
			Predicate e4 = cb.equal( ri.get("brokerId"), ris.get("brokerId"));
			statusNo.where(e1,e2,e3,e4);
			
			Predicate d1 = cb.equal(ri.get("proposalNo"), req.getProposalNo());
			Predicate d2 = cb.equal(ri.get("reinsurerId"), req.getReinsurerId());
			Predicate d3 = cb.equal(ri.get("brokerId"),req.getBrokerId());
			Predicate d4 = cb.equal(ri.get("branchCode"), bean.getBranchCode());
			Predicate d5 = cb.equal(ri.get("statusNo"), statusNo);
			update2.where(d1,d2,d3,d4,d5);
			em.createQuery(update2).executeUpdate();
			
		}
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Transactional
	@Override
	public CommonSaveRes deleteFile(DeleteFileReq req) {
		CommonSaveRes response = new CommonSaveRes();
		try {
		//DELETE_ATTACHED_FILE
		 notiRepo.deleteByDocIdAndOrgFileName(new BigDecimal(req.getDocId()), req.getFileName());
		    response.setResponse("");
		    response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonSaveRes downloadFile(DeleteFileReq req) {
		CommonSaveRes response = new CommonSaveRes();
		String result="";
		try {
			//DOWNLOAD_ATTACHED_FILE
			NotificationAttachmentDetail list =  notiRepo.findByDocIdAndOrgFileName(new BigDecimal(req.getDocId()), req.getFileName());
			if(list!=null) {
				result = list.getOurFileName();
			}
			response.setResponse(result);
		    response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetPlacementViewListRes getPlacementViewList(GetPlacementViewListReq bean) {
		GetPlacementViewListRes response = new GetPlacementViewListRes();
		List<GetPlacementViewListRes1> resList = new ArrayList<GetPlacementViewListRes1>();
		try {
			//GET_PLACEMENT_VIEW
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//currentStatus
			Subquery<String> currentStatus = query.subquery(String.class); 
			Root<StatusMaster> mail = currentStatus.from(StatusMaster.class);
			currentStatus.select(mail.get("statusName"));
			Predicate g1 = cb.equal( pm.get("currentStatus"), mail.get("statusCode"));
			Predicate g2 = cb.equal( pm.get("branchCode"), mail.get("branchCode"));
			currentStatus.where(g1,g2);
			
			//newStatus
			Subquery<String> newStatus = query.subquery(String.class); 
			Root<StatusMaster> mail1 = newStatus.from(StatusMaster.class);
			newStatus.select(mail1.get("statusName"));
			Predicate h1 = cb.equal( pm.get("newStatus"), mail1.get("statusCode"));
			Predicate h2 = cb.equal( pm.get("branchCode"), mail1.get("branchCode"));
			newStatus.where(h1,h2);
			
			//emailBy
			Subquery<String> emailBy = query.subquery(String.class); 
			Root<ConstantDetail> email = emailBy.from(ConstantDetail.class);
			emailBy.select(email.get("detailName"));
			Predicate j1 = cb.equal( email.get("categoryId"), "55");
			Predicate j2 = cb.equal( email.get("type"), pm.get("emailBy"));
			emailBy.where(j1,j2);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"), currentStatus.alias("CURRENT_STATUS"),
					newStatus.alias("NEW_STATUS"),pm.get("updateDate").alias("UPDATE_DATE"),
					emailBy.alias("EMAIL_BY"),pm.get("newStatus").alias("STATUS"),
					pm.get("correspondentId").alias("CORRESPONDENT_ID")); 

			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(pm.get("amendId")));

			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
			Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
			Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple map: list) {
					GetPlacementViewListRes1 res = new GetPlacementViewListRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString());
					res.setCorrespondentId(map.get("CORRESPONDENT_ID")==null?"":map.get("CORRESPONDENT_ID").toString());
					res.setCurrentStatus(map.get("CURRENT_STATUS")==null?"":map.get("CURRENT_STATUS").toString());
					res.setEmailBy(map.get("EMAIL_BY")==null?"":map.get("EMAIL_BY").toString());
					res.setNewStatus(map.get("NEW_STATUS")==null?"":map.get("NEW_STATUS").toString());
					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString());
					res.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString());
					res.setUpdateDate(map.get("UPDATE_DATE")==null?"":map.get("UPDATE_DATE").toString());
					resList.add(res);
				}
			}
			response.setCommonResponse(resList);
		    response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetPlacementViewRes getPlacementView(GetPlacementViewReq bean) {
		GetPlacementViewRes response = new GetPlacementViewRes();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			//GET_PLACEMENT_STATUS_VIEW
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);
			query.multiselect(pm.get("amendId").alias("AMEND_ID"),pm.get("emailBy").alias("EMAIL_BY"),
					pm.get("currentStatus").alias("CURRENT_STATUS"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					pm.get("cedentCorrespondence").alias("CEDENT_CORRESPONDENCE"),pm.get("updateDate").alias("UPDATE_DATE"),
					pm.get("newStatus").alias("NEW_STATUS"),pm.get("tqrCorrespondence").alias("TQR_CORRESPONDENCE"),
					pm.get("reinsurerCorrespondence").alias("REINSURER_CORRESPONDENCE")); 
		
			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
			Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
			Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
			Predicate n5 = cb.equal(pm.get("newStatus"), bean.getNewStatus());
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			
			if(!CollectionUtils.isEmpty(list)) {
				Tuple map=list.get(0);
				GetPlacementViewRes1 res = new GetPlacementViewRes1();
				res.setEmailBy(map.get("EMAIL_BY")==null?"":map.get("EMAIL_BY").toString());
				res.setCurrentStatus(map.get("NEW_STATUS")==null?"":map.get("NEW_STATUS").toString());
				res.setCedentCorrespondent(map.get("CEDENT_CORRESPONDENCE")==null?"":map.get("CEDENT_CORRESPONDENCE").toString());
				res.setReinsurerCorrespondent(map.get("REINSURER_CORRESPONDENCE")==null?"":map.get("REINSURER_CORRESPONDENCE").toString());
				res.setTqrCorrespondent(map.get("TQR_CORRESPONDENCE")==null?"":map.get("TQR_CORRESPONDENCE").toString());
				res.setUpdateDate(map.get("UPDATE_DATE")==null?"":sdf.format(map.get("UPDATE_DATE")));
				response.setCommonResponse(res);
			}
			
		    response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public PlacementSummaryRes placementSummary(PlacementSummaryReq bean) {
		PlacementSummaryRes response = new PlacementSummaryRes();
		List<Map<String,Object>>list=null;
		String query="";
		try {
			String[] obj=new String[2];
			query= "NEW_CONTRACT_PL_SUMMARY";
			 String qutext = prop.getProperty(query);
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProposalNo();
			
			if(StringUtils.isNotBlank(bean.getSearchType()) && null !=bean.getSearchType()){
            	
        		if(StringUtils.isNotBlank(bean.getCompanyNameSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getCompanyNameSearch() + "%")});
            		qutext += " " + " AND UPPER((SELECT CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME ELSE PI.FIRST_NAME || ' ' || PI.LAST_NAME END NAME FROM PERSONAL_INFO PI WHERE CUSTOMER_TYPE='R'  AND CUSTOMER_ID=TRP.REINSURER_ID AND BRANCH_CODE=TRP.BRANCH_CODE  AND AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO WHERE CUSTOMER_ID=PI.CUSTOMER_ID))) LIKE UPPER(?)";
            	}
        		if(StringUtils.isNotBlank(bean.getBrokerNameSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getBrokerNameSearch() + "%")});
            		qutext += " " + " AND UPPER((SELECT CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME ELSE PI.FIRST_NAME || ' ' || PI.LAST_NAME END NAME FROM PERSONAL_INFO PI WHERE CUSTOMER_TYPE='B'  AND CUSTOMER_ID=TRP.BROKER_ID AND BRANCH_CODE=TRP.BRANCH_CODE AND AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO WHERE CUSTOMER_ID=PI.CUSTOMER_ID))) LIKE UPPER(?)";
            	}
            	if(StringUtils.isNotBlank(bean.getUwYearSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getUwYearSearch() + "%")});
            		qutext += " " +" AND UPPER(UW_YEAR) LIKE UPPER(?)";
            	}
            	if(StringUtils.isNotBlank(bean.getUwYearToSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getUwYearToSearch() + "%")});
            		qutext += " " +" AND UPPER(UW_YEAR_TO) LIKE UPPER(?)";
            	}
            	if(StringUtils.isNotBlank(bean.getIncepDateSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getIncepDateSearch() + "%")});
            		qutext += " "  +" AND TO_CHAR(INCEPTION_DATE,'DD/MM/YYYY') LIKE ?";;
            	}
            	if(StringUtils.isNotBlank(bean.getExpDateSearch())){
            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getExpDateSearch() + "%")});
            		qutext += " " +" AND TO_CHAR(EXPIRY_DATE,'DD/MM/YYYY') LIKE ?";;
            	}
            	qutext += " " + "ORDER BY PM.PROPOSAL_NO, TRP.SNO";
            	
            }else{
//            	bean.setCompanyNameSearch("");
//            	bean.setBrokerNameSearch("");
//            	bean.setUwYearSearch("");
//            	bean.setUwYearToSearch("");
//            	bean.setIncepDateSearch("");
//            	bean.setExpDateSearch("");
            }
	        	query1 =queryImpl.setQueryProp(qutext, obj);
	    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    		try {
	    			 list = query1.getResultList();
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		} 
	    		 List<PlacementSummaryRes1> resList =new ArrayList<PlacementSummaryRes1>();
	             if(list!=null && list.size()>0){
	             for (int i = 0; i < list.size(); i++) {
	                 Map<String, Object> tempMap = list.get(i);
	                 PlacementSummaryRes1 tempBean = new PlacementSummaryRes1();
	                 tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString()); 
	                 tempBean.setBaseProposal(tempMap.get("BASE_PROPOSAL") == null ? "" : tempMap.get("BASE_PROPOSAL").toString()); 
	                 tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString()); 
	                 tempBean.setRskTreatyid(tempMap.get("RSK_TREATYID") == null ? "" : tempMap.get("TREATY_NAME").toString()); 
	                 tempBean.setLayerSection(tempMap.get("LAYER_SECTION") == null ? "" : tempMap.get("LAYER_SECTION").toString()); 
	                 tempBean.setSno(tempMap.get("SNO") == null ? "" : tempMap.get("SNO").toString()); 
	                 tempBean.setReinsurerName(tempMap.get("REINSURER_NAME") == null ? "" : tempMap.get("REINSURER_NAME").toString()); 
	                 tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString()); 
	                 tempBean.setCurrency(tempMap.get("CURRENCY") == null ? "" : tempMap.get("CURRENCY").toString()); 
	                 tempBean.setEpi100Oc(tempMap.get("EPI_100_OC") == null ? "" : tempMap.get("EPI_100_OC").toString()); 
	                 tempBean.setEpi100Dc(tempMap.get("EPI_100_DC") == null ? "" : tempMap.get("EPI_100_DC").toString()); 
	                 tempBean.setPlacingStatus(tempMap.get("PLACING_STATUS") == null ? "" : tempMap.get("PLACING_STATUS").toString()); 
	                 tempBean.setShareSigned(tempMap.get("SHARE_SIGNED") == null ? "" : tempMap.get("SHARE_SIGNED").toString()); 
	                tempBean.setBrokerage(tempMap.get("BROKERAGE") == null ? "" : tempMap.get("BROKERAGE").toString()); 
	                tempBean.setBrokerageAmt(tempMap.get("BROKERAGE_AMT") == null ? "" : tempMap.get("BROKERAGE_AMT").toString());
	                resList.add(tempBean);
	             }
	             }
	 			 response.setCommonResponse(resList);
	 			 response.setMessage("Success");
	 			 response.setIsError(false);
	 			}catch(Exception e){
	 					log.error(e);
	 					e.printStackTrace();
	 					response.setMessage("Failed");
	 					response.setIsError(true);
	 				}
	 			return response;
	}
	@Override
	public GetMailTemplateRes getMailTemplate(GetMailTemplateReq req) {
		GetMailTemplateRes response = new GetMailTemplateRes();
		GetMailTemplateRes1 bean =new GetMailTemplateRes1();
		try {
			MailproposalInfo(req);
			//GET_MAIL_TEMPLATE
			List<MailTemplateMaster> list = mailTemplateMasterRepository.findByMailType(req.getMailType());
		
			if(!CollectionUtils.isEmpty(list)) {
				MailTemplateMaster map=list.get(0);
				bean.setMailSubject(map.getMailSubject()==null?"":map.getMailSubject().toString());
				bean.setMailBody(map.getMailBody()==null?"":map.getMailBody().toString());
				bean.setMailTo(map.getEmailTo()==null?"":map.getEmailTo().toString());
				bean.setMailCC(map.getEmailCc()==null?"":map.getEmailCc().toString());
				bean.setMailRegards(map.getMailRegards()==null?"":map.getMailRegards().toString());
			}
			//GetMailBodyFrame(bean);
			String mailbody=bean.getMailBody(),mailsub=bean.getMailSubject();
			
			//proposalInfo
			String proposal=StringUtils.isBlank(req.getEproposalNo())?req.getProposalNo():req.getEproposalNo();
			List<Tuple> list1 = getExistingProposal(proposal, req.getBranchCode());
		
			if(!CollectionUtils.isEmpty(list1)) {
				Tuple map=list1.get(0);
				String bouquetNo=map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString();
				String proposalNo=map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
				String baseproposalNo=map.get("BASE_LAYER")==null?"":map.get("BASE_LAYER").toString();
				String offerNo=map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString();
				if(StringUtils.isNotBlank(bouquetNo)) {
					mailsub=mailsub+" "+bouquetNo+"";
				}else if(StringUtils.isNotBlank(offerNo)) {
					mailsub=mailsub+" "+offerNo+" ";
				}
			//	GetPalcementInfo(bean);
				 Map<String,Object>  values = new HashMap<String,Object>();
				 values.put("COMPANY_NAME", map.get("COMPANY_NAME") );
				 
				 if("PWL".equalsIgnoreCase(req.getNewStatus())) {
					if(mailbody.contains("COMPANY_NAME") == true) {
						mailbody = mailbody.replace("{COMPANY_NAME}", req.getReinsurerName());
					}
				 }
				
				for (Map.Entry entry: values.entrySet()) {
					
					if(mailbody.contains(entry.getKey().toString()) == true) {
						mailbody = mailbody.replace("{"+entry.getKey().toString()+"}", entry.getValue()==null?"":entry.getValue().toString());
					}
					
					if(mailsub.contains(entry.getKey().toString()) == true) {
						mailsub = mailsub.replace("{"+entry.getKey().toString()+"}", entry.getValue()==null?"":entry.getValue().toString());
					}
				}
			}
			
			//mailbody+=BodyTableFrame(req);
			bean.setMailBody(mailbody);
			bean.setMailSubject(mailsub);
	
			response.setCommonResponse(bean);
			 response.setMessage("Success");
			 response.setIsError(false);
			}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	private String BodyTableFrame(GetMailTemplateReq bean) {
		List<Tuple> list=MailproposalInfo(bean);
		String msg="";
		if(StringUtils.isBlank(bean.getNewStatus())) {
			msg=getOfferMsg(list,bean);
		}else if("PWL".equalsIgnoreCase(bean.getNewStatus())) {
			msg=getPropsalWrittenMsg(list,bean);
		}else if("PSL".equalsIgnoreCase(bean.getNewStatus())) {
			msg=getPropsalSignedMsg(list,bean);
		}
		return msg;
	}
	private List<Tuple> MailproposalInfo(GetMailTemplateReq bean) {
		List<Tuple> list=null;
		try {
//			CASE WHEN RD.RSK_SPFCID='ALL' THEN 'ALL' ELSE (select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).
//					EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from table(SPLIT_TEXT_FN(replace(RD.RSK_SPFCID,' ', '')))) 
//					AND  SPFC.TMAS_PRODUCT_ID = RD.RSK_PRODUCTID AND SPFC.BRANCH_CODE = RD.BRANCH_CODE) END SUB_CLASS
			
			
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); //'New' POLICY_STATUS,''EXISTING_SHARE
			Root<TtrnRiPlacement> trp = query.from(TtrnRiPlacement.class);
			// companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( rd.get("rskCedingid"), pms.get("customerId"));
			Predicate a2 = cb.equal( pms.get("customerType"), "C");
			Predicate a3 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			companyName.where(a1,a2,a3);
			
			//BUSINESS_TYPE
			Subquery<String> businessType = query.subquery(String.class); 
			Root<TmasProductMaster> pd = businessType.from(TmasProductMaster.class);
			businessType.select(pd.get("tmasProductName"));
			Predicate b1 = cb.equal(pd.get("tmasProductId"),  pm.get("productId"));
			Predicate b2 = cb.equal(pd.get("branchCode"),  pm.get("branchCode"));
			businessType.where(b1,b2);
			
			//treatyType
			Subquery<String> treatyType = query.subquery(String.class); 
			Root<ConstantDetail> cd = treatyType.from(ConstantDetail.class);
			treatyType.select(cd.get("detailName"));
			Predicate c1 =  cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,new BigDecimal(43) ).otherwise(new BigDecimal(29)) ,
					  cd.get("categoryId"));
			Predicate c2 = cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,rd.get("treatytype") ).otherwise(rd.get("rskBusinessType")) ,
					  cd.get("type"));	
			treatyType.where(c1,c2);
			
			//CLASS
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> dm = deptName.from(TmasDepartmentMaster.class);
			deptName.select(dm.get("tmasDepartmentName"));
			Predicate d1 = cb.equal(dm.get("tmasDepartmentId"),  rd.get("rskDeptid"));
			Predicate d2 = cb.equal(dm.get("branchCode"),  rd.get("branchCode"));
			Predicate d3 = cb.equal(dm.get("tmasProductId"),  rd.get("rskProductid"));
			Predicate d4 = cb.equal(dm.get("tmasStatus"), "Y");
			deptName.where(d1,d2,d3,d4);
			
			//status
			Subquery<Long> status = query.subquery(Long.class); 
			Root<TtrnRiPlacement> TRP1 = status.from(TtrnRiPlacement.class);
			status.select(cb.max(TRP1.get("statusNo")));
			Predicate e1 = cb.equal( TRP1.get("proposalNo"), trp.get("proposalNo"));
			Predicate e2 = cb.equal( TRP1.get("sno"), trp.get("sno"));
			Predicate e3 = cb.equal( TRP1.get("branchCode"), trp.get("branchCode"));
			status.where(e1,e2,e3);
	
			query.multiselect(rd.get("rskInceptionDate").alias("INS_DATE"),rd.get("rskExpiryDate").alias("EXP_DATE"),
					companyName.alias("COMPANY_NAME"),pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("baseLayer").alias("BASE_LAYER"),pm.get("layerNo").alias("LAYER_NO"),businessType.alias("BUSINESS_TYPE"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),treatyType.alias("TREATY_TYPE"),rd.get("rskTreatyid").alias("RSK_TREATYID"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"),
					deptName.alias("CLASS"),                    //subclass pending
					cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,pm.get("sectionNo"))
					.otherwise(pm.get("layerNo")).alias("SECTION_NO"), 	pm.get("offerNo").alias("OFFER_NO"),
					pm.get("productId").alias("PRODUCT_ID"), trp.get("shareOffered").alias("SHARE_OFFERED"),
					trp.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),
					trp.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED")).distinct(true); 
	
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(pm.get("proposalNo")));
			orderList.add(cb.asc(pm.get("bouquetNo")));
	
			
			if(StringUtils.isBlank(bean.getSearchType())) {
				// Where
				Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			
				Predicate n4 = cb.equal(trp.get("reinsurerId"), bean.getReinsurerId()); 
				Predicate n5 = cb.equal(trp.get("brokerId"), bean.getBrokerId()); 
				Predicate n6 = cb.equal(pm.get("contractStatus"), "P");
				Predicate n7 = cb.equal(trp.get("proposalNo"), pm.get("proposalNo")); 
				
				Expression<String> e0 = trp.get("statusNo");
	      		Predicate n8 = e0.in(status);
				
				if("C".equals(bean.getPlacementMode())) {
					if(StringUtils.isNotBlank(bean.getBouquetNo())) {
						//GET_MAILTEPLATE_BOUQUET
						Predicate n3 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
						query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
					}else {
						//GET_MAILTEPLATE_BASELAYER
						Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), bean.getBaseProposalNo());
						query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
					}
				}else {
					//GET_MAILTEPLATE_PROPOSAL
					Predicate n3 = cb.equal(pm.get("proposalNo"), bean.getProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
				}
			}else {
				// Where
				Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
				Predicate n4 = cb.equal(trp.get("reinsurerId"), bean.getSearchReinsurerId()); 
				Predicate n5 = cb.equal(trp.get("brokerId"), bean.getSearchBrokerId()); 
				Predicate n6 = cb.equal(pm.get("contractStatus"), "P");
				Predicate n7 = cb.equal(trp.get("proposalNo"), pm.get("proposalNo")); 
				Expression<String> e0 = trp.get("statusNo");
	      		Predicate n8 = e0.in(status);
				Predicate n9 = cb.equal(trp.get("status"), bean.getNewStatus()); 
				
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
					//GET_MAILTEPLATE_BOUQUET_SEARCH
					Predicate n3 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
					//GET_MAILTEPLATE_BASELAYER_SEARCH
					Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), bean.getBaseProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}else {
					//GET_MAILTEPLATE_PROPOSAL_SEARCH
					Predicate n3 = cb.equal(pm.get("proposalNo"), bean.getProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}
			}
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public String getOfferMsg(List<Tuple> agentWiseReport, GetMailTemplateReq bean) {
		String messageContent ="";
		messageContent="<!DOCTYPE html>" + 
				"<html lang=\"en\">" + 
				"<body>" + 
				"    <div style=\"width: 80%;\">" + 
				"<table style=\"width:100%;border: 1px solid #000000;\">" +
				"<thead> <tr> <th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Bouquet Ref </th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Offer Ref</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Business Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Main Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Sub Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Name</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Layer / Section No</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Inception Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Expiry Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Max Share Offer %</th>" + 
				"</tr> </thead> <tbody>" ;
		
		
		for(int i=0;i<agentWiseReport.size();i++) {
			Tuple map=agentWiseReport.get(i);
				messageContent+="<tr>"+
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BUSINESS_TYPE")==null?"":map.get("BUSINESS_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("CLASS")==null?"":map.get("CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SUB_CLASS")==null?"":map.get("SUB_CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("RSK_TREATYID")==null?"":map.get("RSK_TREATYID").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":map.get("INS_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":map.get("EXP_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" + 
			"</tr>";
		}
		messageContent+=	"</table>" +
				"    </div>" + 
				"</body>" + 
				"" + 
				"</html>";
	
	return messageContent.toString();
	}
	public String getPropsalWrittenMsg(List<Tuple>agentWiseReport, GetMailTemplateReq bean) {
		String messageContent ="";
		messageContent="<!DOCTYPE html>" + 
				"<html lang=\"en\">" + 
				"<body>" + 
				"    <div style=\"width: 80%;\">" + 
				"<table style=\"width:100%;border: 1px solid #000000;\">" +
				"<thead> <tr> <th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Bouquet Ref </th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Offer Ref</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Business Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Main Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Sub Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Name</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Layer / Section No</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Inception Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Expiry Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Max Share Offer %</th>" +
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Proposed Written Line %</th>" + 
				"</tr> </thead> <tbody>" ;
		
		for(int i=0;i<agentWiseReport.size();i++) {
			Tuple map=agentWiseReport.get(i);
				messageContent+="<tr>"+
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BUSINESS_TYPE")==null?"":map.get("BUSINESS_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("CLASS")==null?"":map.get("CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SUB_CLASS")==null?"":map.get("SUB_CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("RSK_TREATYID")==null?"":map.get("RSK_TREATYID").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":map.get("INS_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":map.get("EXP_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":dropDownImple.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()))+"</td>" +
			"</tr>";
		}
		messageContent+=	"</table>" +
				"    </div>" + 
				"</body>" + 
				"" + 
				"</html>";
	
	return messageContent.toString();
	}
	public String getPropsalSignedMsg(List<Tuple> agentWiseReport, GetMailTemplateReq bean) {
		String messageContent ="";
		messageContent="<!DOCTYPE html>" + 
				"<html lang=\"en\">" + 
				"<body>" + 
				"    <div style=\"width: 80%;\">" + 
				"<table style=\"width:100%;border: 1px solid #000000;\">" +
				"<thead> <tr> <th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Bouquet Ref </th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">TQR Offer Ref</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Business Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Main Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Sub Class</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Type</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Treaty Name</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Layer / Section No</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Inception Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Expiry Date</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Max Share Offer %</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Proposed Written Line %</th>" + 
				"<th width=\"10%\" style=\"border: 1px solid #000000;\">Proposed Signed Line %</th>" + 
				"</tr> </thead> <tbody>" ;
		
		
		for(int i=0;i<agentWiseReport.size();i++) {
			Tuple map=agentWiseReport.get(i);
				messageContent+="<tr>"+
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BUSINESS_TYPE")==null?"":map.get("BUSINESS_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("CLASS")==null?"":map.get("CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SUB_CLASS")==null?"":map.get("SUB_CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("RSK_TREATYID")==null?"":map.get("RSK_TREATYID").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":map.get("INS_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":map.get("EXP_DATE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":dropDownImple.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":dropDownImple.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSED_SIGNED")==null?"":dropDownImple.formattereight(map.get("SHARE_PROPOSED_SIGNED").toString()))+"</td>" +
			"</tr>";
		} 
		messageContent+=	"</table>" +
				"    </div>" + 
				"</body>" + 
				"" + 
				"</html>";
	
	return messageContent.toString();
	}

	}
