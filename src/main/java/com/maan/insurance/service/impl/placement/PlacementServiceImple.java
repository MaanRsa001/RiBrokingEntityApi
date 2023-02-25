package com.maan.insurance.service.impl.placement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.entity.MailNotificationDetail;
import com.maan.insurance.model.entity.MailTemplateMaster;
import com.maan.insurance.model.entity.NotificationAttachmentDetail;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiPlacementStatus;
import com.maan.insurance.model.repository.MailNotificationDetailRepository;
import com.maan.insurance.model.repository.MailTemplateMasterRepository;
import com.maan.insurance.model.repository.NotificationAttachmentDetailRepository;
import com.maan.insurance.model.repository.TtrnRiPlacementRepository;
import com.maan.insurance.model.repository.TtrnRiPlacementStatusRepository;
import com.maan.insurance.model.req.placement.AttachFileReq;
import com.maan.insurance.model.req.placement.DeleteFileReq;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;
import com.maan.insurance.model.req.placement.GetApprovalPendingListReq;
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
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.req.placement.ReinsListReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UpdateRiplacementReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq1;
import com.maan.insurance.model.req.placement.proposalInfoReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.placement.AttachFileRes;
import com.maan.insurance.model.res.placement.AttachFileRes1;
import com.maan.insurance.model.res.placement.CommonSaveResList;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes1;
import com.maan.insurance.model.res.placement.EditPlacingDetailsResponse;
import com.maan.insurance.model.res.placement.GetApprovalPendingListRes;
import com.maan.insurance.model.res.placement.GetApprovalPendingListRes1;
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
import com.maan.insurance.model.res.placement.SendMailRes;
import com.maan.insurance.model.res.placement.UpdatePlacementRes;
import com.maan.insurance.model.res.placement.UpdatePlacementRes1;
import com.maan.insurance.model.res.placement.UpdateRiplacementRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes1;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.notification.entity.MailMaster;
import com.maan.insurance.notification.repo.MailMasterRepository;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.upload.UploadCustomRepository;
import com.maan.insurance.service.placement.PlacementService;
import com.maan.insurance.validation.Formatters;

@Service
public class PlacementServiceImple implements PlacementService {
	private Logger log = LogManager.getLogger(PlacementServiceImple.class);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	String commonPath = (PlacementServiceImple.class).getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
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
	
	@Autowired
	private PlacementCustomRepository placementCustomRepository;
	
	@Autowired
	private UploadCustomRepository uploadCustomRepository;
	
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
	  
		private String formatdate(Object output) {
			return new SimpleDateFormat("dd/MM/yyyy").format(output).toString();
		}
	
	@Override
	public GetCommonDropDownRes getMailToList(GetMailToListReq bean) { 
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			List<Tuple> list=placementCustomRepository.getMailToList(bean);
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
			List<Tuple> list=placementCustomRepository.getExistingReinsurerList(bean);
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
			List<Tuple> list=placementCustomRepository.getExistingBrokerList(bean);
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
			List<Tuple> list=placementCustomRepository.getExistingAttachList(bean);
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
	public ProposalInfoRes proposalInfo(proposalInfoReq req) {
		ProposalInfoRes response = new ProposalInfoRes();
		ProposalInfoRes1 bean = new ProposalInfoRes1();
		try {
			String proposal=StringUtils.isBlank(req.getEProposalNo())?req.getProposalNo():req.getEProposalNo();
			
			List<Tuple> list = placementCustomRepository.getExistingProposal(proposal, req.getBranchCode(),"0");
			if(list.size()>0) {
				Tuple map=list.get(0);
				bean.setCedingCompanyName(map.get("COMPANY_NAME")==null?"":map.get("COMPANY_NAME").toString());
				bean.setCedingCompany(map.get("RSK_CEDINGID")==null?"":map.get("RSK_CEDINGID").toString());
				bean.setBrokerCompany(map.get("RSK_BROKERID")==null?"":map.get("RSK_BROKERID").toString());
				bean.setTreatyName(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString());
				bean.setInceptionDate(map.get("INS_DATE")==null?"":formatdate(map.get("INS_DATE")));
				bean.setExpiryDate(map.get("EXP_DATE")==null?"":formatdate(map.get("EXP_DATE")));
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
				if(StringUtils.isNotBlank(req.getProposalNo()))
				bean.setEproposalNo(req.getProposalNo());
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

	

//	@Override
//	public GetReinsurerInfoRes getReinsurerInfo(GetReinsurerInfoReq bean) {
//		GetReinsurerInfoRes response = new GetReinsurerInfoRes();
//		List<GetReinsurerInfoRes1> resList = new ArrayList<GetReinsurerInfoRes1>();
//		GetReinsurerInfoResponse res1 = new GetReinsurerInfoResponse();
//		List<Map<String,Object>>list=null;
//		int mailcount=0;
//		
//		try {
//			//GET_REINSURER_INFO_NOTIN --LEFT JOIN--
//			list = queryImpl.selectList("GET_REINSURER_INFO_NOTIN",new String[] {bean.getBranchCode(),bean.getEproposalNo()});
//			if(!CollectionUtils.isEmpty(list)) {
//				res1.setPlacementMode(list.get(0).get("PLACEMENT_MODE")==null?"":list.get(0).get("PLACEMENT_MODE").toString());
//				res1.setPlacementDisabled("Y");
//			}
//			//GET_PROPOSAL_MAIL_COUNT
//			mailcount = ripRepo.countByBranchCodeAndProposalNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getEproposalNo()),"0");
//			if(StringUtils.isNotBlank(bean.getBouquetNo()) && "C".equals(res1.getPlacementMode())) {	
//				//get_Reinsurer_Info_Bouquet_Notin --LEFT JOIN--
//				list=queryImpl.selectList("GET_REINSURER_INFO_BOUQUET_NOTIN",new String[] {bean.getBranchCode(),bean.getBouquetNo()});
//				//GET_BOUQUET_MAIL_COUNT
//				mailcount=ripRepo.countByBranchCodeAndBouquetNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getBouquetNo()),"0");
//			}
//			if(!CollectionUtils.isEmpty(list)) {
//				for(int i=0;i<list.size();i++) {
//					Map<String,Object>map=list.get(i);
//					GetReinsurerInfoRes1 res = new GetReinsurerInfoRes1();
//					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString());
//					res.setPlacingBroker(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
//					res.setProposalNos(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
//					res.setReinsSNo(map.get("SNO")==null?"":map.get("SNO").toString());
//					res.setReinsureName(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
//					res.setShareOffer(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()));;
//		
//					if(mailcount>0) {
//						res.setDeleteStatus("N");
//					}else {
//						res.setDeleteStatus("N");
//					}
//					res.setChangeStatus("N");
//					resList.add(res);
//					}
//				res1.setReinsurerInfoList(resList);
//			}
//			response.setCommonResponse(res1);	
//			response.setMessage("Success");
//			response.setIsError(false);
//		}catch(Exception e){
//				log.error(e);
//				e.printStackTrace();
//				response.setMessage("Failed");
//				response.setIsError(true);
//			}
//		return response;
//	}
	@Override
	public GetReinsurerInfoRes getReinsurerInfo(GetReinsurerInfoReq bean) {
		GetReinsurerInfoRes response = new GetReinsurerInfoRes();
		List<GetReinsurerInfoRes1> resList = new ArrayList<GetReinsurerInfoRes1>();
		GetReinsurerInfoResponse res1 = new GetReinsurerInfoResponse();
		List<Tuple> list = new ArrayList<>();
		int mailcount=0;
		
		try {
			//GET_REINSURER_INFO_NOTIN --LEFT JOIN
			list = placementCustomRepository.getReinsurerInfoNotin(bean.getBranchCode(),bean.getEproposalNo());
			if(!CollectionUtils.isEmpty(list)) {
				res1.setPlacementMode(list.get(0).get("PLACEMENT_MODE")==null?"":list.get(0).get("PLACEMENT_MODE").toString());
				res1.setPlacementDisabled("Y");
			}
			//GET_PROPOSAL_MAIL_COUNT
			mailcount = ripRepo.countByBranchCodeAndProposalNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getEproposalNo()),"0");
			if(StringUtils.isNotBlank(bean.getBouquetNo()) && "C".equals(res1.getPlacementMode())) {	
				//get_Reinsurer_Info_Bouquet_Notin --LEFT JOIN 
				list=placementCustomRepository.getReinsurerInfoBouquetNotin(bean.getBranchCode(),bean.getBouquetNo());
				//GET_BOUQUET_MAIL_COUNT
				mailcount=ripRepo.countByBranchCodeAndBouquetNoAndStatusNot(bean.getBranchCode(),new BigDecimal(bean.getBouquetNo()),"0");
			}
			if(!CollectionUtils.isEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					Tuple map=list.get(i);
					GetReinsurerInfoRes1 res = new GetReinsurerInfoRes1();
					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString());
					res.setPlacingBroker(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					//res.setProposalNos(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					res.setReinsSNo(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setReinsureName(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setShareOffer(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()));;
		
					if(mailcount>0) {
						res.setDeleteStatus("N");
					}else {
						res.setDeleteStatus("N");
					}
					res.setChangeStatus("N");
					resList.add(res);
					}
				res1.setReinsurerInfoList(resList);
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

//	@Override
//	public GetPlacementInfoListRes getPlacementInfoList(GetPlacementInfoListReq bean) {
//		GetPlacementInfoListRes response = new GetPlacementInfoListRes();
//		List<Map<String,Object>>list=null;
//		String query="";
//		String qutext ="";
//		List<GetPlacementInfoListRes1> resList = new ArrayList<GetPlacementInfoListRes1>();
//		try {
//			String[] obj=new String[2];
//			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
//				query="GET_PLACEMENT_BOUQUET_LIST"; //LEFT JOIN
//				  qutext = prop.getProperty(query);
//				obj[0]=bean.getBranchCode();
//				obj[1]=bean.getBouquetNo();
//			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
//				query="GET_PLACEMENT_BASE_LIST";
//				 qutext = prop.getProperty(query);
//				obj[0]=bean.getBranchCode();
//				obj[1]=bean.getBaseProposalNo();
//			}else {
//				query="GET_PLACEMENT_LIST";
//				 qutext = prop.getProperty(query);
//				obj[0]=bean.getBranchCode();
//				obj[1]=StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo();
//			}
//			if(StringUtils.isNotBlank(bean.getSearchType())) {
//				qutext=qutext+" AND P.REINSURER_ID='"+bean.getSearchReinsurerId()+"' AND P.BROKER_ID='"+bean.getSearchBrokerId()+"' AND P.STATUS='"+bean.getSearchStatus()+"'";
//			}
//			qutext=qutext+" ORDER BY OFFER_NO,P.BASE_PROPOSAL_NO,P.PROPOSAL_NO,P.SNO";
//			query1 =queryImpl.setQueryProp(qutext, obj);
//    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//    		try {
//    			 list = query1.getResultList();
//    		} catch(Exception e) {
//    			e.printStackTrace();
//    		} 
//			
//			if(list.size()>0) {
//				for(int i=0;i<list.size();i++) {
//					Map<String,Object>map=list.get(i);
//					GetPlacementInfoListRes1 res = new GetPlacementInfoListRes1();
//					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString()); 
//					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString()); 
//					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString()); 
//					res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString()); 
//					res.setCedingCompanyId(map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString()); 
//					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString()); 
//					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString()); 
//					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString()); 
//					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString()); 
//					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":map.get("SHARE_OFFERED").toString()); 
//					res.setShareWritten(map.get("SHARE_WRITTEN")==null?"":map.get("SHARE_WRITTEN").toString()); 
//					res.setShareProposalWritten(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":map.get("SHARE_PROPOSAL_WRITTEN").toString()); 
//					res.setShareSigned(map.get("SHARE_SIGNED")==null?"":map.get("SHARE_SIGNED").toString()); 
//					res.setBrokeragePer(map.get("BROKERAGE_PER")==null?"":map.get("BROKERAGE_PER").toString()); 
//					res.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString()); 
//					res.setWrittenLineValidity(map.get("WRITTEN_LINE_VALIDITY")==null?"":map.get("WRITTEN_LINE_VALIDITY").toString()); 
//					res.setWrittenLineRemarks(map.get("WRITTEN_LINE_REMARKS")==null?"":map.get("WRITTEN_LINE_REMARKS").toString()); 
//					res.setShareLineValidity(map.get("SHARE_LINE_VALIDITY")==null?"":map.get("SHARE_LINE_VALIDITY").toString()); 
//					res.setShareLineRemarks(map.get("SHARE_LINE_REMARKS")==null?"":map.get("SHARE_LINE_REMARKS").toString()); 
//					res.setShareProposedSigned(map.get("SHARE_PROPOSED_SIGNED")==null?"":map.get("SHARE_PROPOSED_SIGNED").toString()); 
//					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString()); 
//					res.setOfferNo(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString());
//					resList.add(res);
//					}
//			}
//			response.setCommonResponse(resList);	
//			response.setMessage("Success");
//			response.setIsError(false);
//		}catch(Exception e){
//				log.error(e);
//				e.printStackTrace();
//				response.setMessage("Failed");
//				response.setIsError(true);
//			}
//		return response;
//	}
	@Override
	public GetPlacementInfoListRes getPlacementInfoList(GetPlacementInfoListReq bean) {
		GetPlacementInfoListRes response = new GetPlacementInfoListRes();
		List<Tuple> list=new ArrayList<>();
		List<GetPlacementInfoListRes1> resList = new ArrayList<GetPlacementInfoListRes1>();
		try {
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_PLACEMENT_BOUQUET_LIST--LEFT JOIN
				list = placementCustomRepository.getPlacementBouquetList(bean); 
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_PLACEMENT_BASE_LIST--LEFT JOIN
				list = placementCustomRepository.getPlacementBaseList(bean); 
				 
			}else {
				//GET_PLACEMENT_LIST--LEFT JOIN
				String prop=StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo();
				list = placementCustomRepository.getPlacementList(bean, prop); 
			}

			DecimalFormat formatter = new DecimalFormat("#0.00000000");     
			if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Tuple map=list.get(i);
					GetPlacementInfoListRes1 res = new GetPlacementInfoListRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString()); 
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString()); 
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString()); 
					res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString()); 
					res.setCedingCompanyId(map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString()); 
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString()); 
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString()); 
					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString()); 
					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString()); 
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":formatter.format(map.get("SHARE_OFFERED"))); 
					res.setShareWritten(map.get("SHARE_WRITTEN")==null?"":formatter.format(map.get("SHARE_WRITTEN"))); 
					res.setShareProposalWritten(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":formatter.format(map.get("SHARE_PROPOSAL_WRITTEN"))); 
					res.setShareSigned(map.get("SHARE_SIGNED")==null?"":formatter.format(map.get("SHARE_SIGNED"))); 
					res.setBrokeragePer(map.get("BROKERAGE_PER")==null?"":formatter.format(map.get("BROKERAGE_PER"))); 
					res.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString()); 
					res.setWrittenLineValidity(map.get("WRITTEN_LINE_VALIDITY")==null?"":sdf.format(map.get("WRITTEN_LINE_VALIDITY"))); 
					res.setWrittenLineRemarks(map.get("WRITTEN_LINE_REMARKS")==null?"":map.get("WRITTEN_LINE_REMARKS").toString()); 
					res.setShareLineValidity(map.get("SHARE_LINE_VALIDITY")==null?"":sdf.format(map.get("SHARE_LINE_VALIDITY"))); 
					res.setShareLineRemarks(map.get("SHARE_LINE_REMARKS")==null?"":map.get("SHARE_LINE_REMARKS").toString()); 
					res.setShareProposedSigned(map.get("SHARE_PROPOSED_SIGNED")==null?"":map.get("SHARE_PROPOSED_SIGNED").toString()); 
					res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString()); 
					res.setOfferNo(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString());
					res.setApproverStatus(map.get("APPROVER_STATUS")==null?"":map.get("APPROVER_STATUS").toString());
					resList.add(res);
					}
					resList.sort(Comparator.comparing(GetPlacementInfoListRes1 :: getOfferNo)
							.thenComparing(Comparator.comparing(GetPlacementInfoListRes1 :: getBaseProposalNo))
							.thenComparing(Comparator.comparing(GetPlacementInfoListRes1 :: getProposalNo)
							.thenComparing(Comparator.comparing(GetPlacementInfoListRes1 :: getSno)) )); //asc multiple orderby
							
			//		resList.sort(Comparator.comparing(GetPlacementInfoListRes1 :: getBaseProposalNo));	
			//		resList.sort(Comparator.comparing(GetPlacementInfoListRes1 :: getProposalNo));	
			//		resList.sort(Comparator.comparing(GetPlacementInfoListRes1 :: getSno));//asc single orderby
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
			GetPlacementNoRes1 res1 = 	placementCustomRepository.getPlacementNo(bean).getCommonResponse();
			
			if("C".equalsIgnoreCase(bean.getPlacementMode())) {
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				list =	dropDownImple.getBouquetExistingList(bean.getBranchCode(),bean.getBouquetNo(),bean.getBouquetModeYN()).getCommonResponse();
				}else {
					list = dropDownImple.getBaseLayerExistingList(bean.getBranchCode(),bean.getBaseProposalNo()).getCommonResponse();
				}
				for(int i=0;i<list.size();i++) {
					bean.setEproposalNo(list.get(i).getProposalNo()==null?"":list.get(i).getProposalNo().toString());
					insertPlacing(bean,res1);
				}
			}else {
				insertPlacing(bean,res1);
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

	
	//@Override
	public InsertPlacingRes insertPlacing(SavePlacingReq bean, GetPlacementNoRes1 res1) {
		InsertPlacingRes response = new InsertPlacingRes();
		String plamendId="0",currentStatus="O";
		Tuple result=null;
		TtrnRiPlacement entity = new TtrnRiPlacement();
		List<InsertPlacingRes1> resList = new ArrayList<InsertPlacingRes1>();
		try {
			proposalInfoReq req1 = new proposalInfoReq();
			req1.setBranchCode(bean.getBranchCode());
			req1.setProposalNo(bean.getProposalNo());
			req1.setEProposalNo(bean.getEproposalNo());
			ProposalInfoRes resp=proposalInfo(req1);
			ProposalInfoRes1 resp1=resp.getCommonResponse();
			//DeletePlacement(bean);
			//INSERT_PLACEMENT_INFO
			for(int i=0;i<bean.getReinsListReq().size();i++) {
				InsertPlacingRes1 res = new InsertPlacingRes1();
				ReinsListReq req =	bean.getReinsListReq().get(i);
				res.setReinsurerId(req.getReinsureName());
				res.setBrokerId(req.getPlacingBroker());
				//bean.setEproposalNo(bean.getProposalNo());
				plamendId=placementCustomRepository.getMaxAmendId(bean.getBranchCode(),bean.getEproposalNo(),res.getReinsurerId(),res.getBrokerId());
				if(StringUtils.isBlank(plamendId) || "null".equalsIgnoreCase(plamendId)) {
					plamendId="0";
				}
				result=placementCustomRepository.getPlacementDetails(bean.getEproposalNo(),res.getReinsurerId(),res.getBrokerId(),bean.getBranchCode());
				if(result!=null) {
					currentStatus=result.get("STATUS")==null?"O":result.get("STATUS").toString();
				}
				entity.setPlacementNo(new BigDecimal(res1.getPlacementNo()));
				entity.setSno(new BigDecimal(req.getReinsSNo()));
				entity.setBouquetNo(StringUtils.isBlank(resp1.getBouquetNo())? BigDecimal.ZERO :new BigDecimal(resp1.getBouquetNo()));
				entity.setBaseProposalNo(StringUtils.isBlank(resp1.getBaseProposalNo())?BigDecimal.ZERO :new BigDecimal(resp1.getBaseProposalNo()));
				entity.setProposalNo(bean.getEproposalNo()==null?BigDecimal.ZERO :new BigDecimal(bean.getEproposalNo()));
				entity.setContractNo(StringUtils.isBlank(resp1.getContractNo())? BigDecimal.ZERO :new BigDecimal(resp1.getContractNo()));
				entity.setLayerNo(StringUtils.isBlank(resp1.getLayerNo())? BigDecimal.ZERO :new BigDecimal(resp1.getLayerNo()));
				entity.setSectionNo(StringUtils.isBlank(resp1.getSectionNo())? BigDecimal.ZERO :new BigDecimal(resp1.getSectionNo()));
				entity.setAmendId(StringUtils.isBlank(resp1.getAmendId())? BigDecimal.ZERO :new BigDecimal(resp1.getAmendId()));
				entity.setReinsurerId(req.getReinsureName()==null?"":req.getReinsureName());
				entity.setBrokerId(req.getPlacingBroker()==null?"":req.getPlacingBroker());
				entity.setShareOffered(req.getShareOffer()==null?BigDecimal.ZERO :new BigDecimal(req.getShareOffer()));
				entity.setBranchCode(bean.getBranchCode()==null?"":bean.getBranchCode());
				entity.setSysDate(new Date());
				entity.setCedingCompanyId(StringUtils.isBlank(resp1.getCedingCompany())?"":resp1.getCedingCompany());
				entity.setPlacementMode(bean.getPlacementMode()==null?"":bean.getPlacementMode());
				entity.setStatus(currentStatus==null?"":currentStatus);
				entity.setPlacementAmendId(StringUtils.isBlank(plamendId)? BigDecimal.ZERO  :new BigDecimal(plamendId));
				entity.setStatusNo(res1.getStatusNo()==null?BigDecimal.ZERO :new BigDecimal(res1.getStatusNo()));
				entity.setApproverStatus("Y");
				entity.setUserId(bean.getUserId()==null?"":bean.getUserId());
				ripRepo.saveAndFlush(entity);		
				resList.add(res);
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
	
	
	@Override
	public GetPlacingInfoRes getPlacingInfo(GetPlacingInfoReq bean) {
		GetPlacingInfoRes response = new GetPlacingInfoRes();
		List<GetPlacingInfoRes1> resList = new ArrayList<GetPlacingInfoRes1>();
		List<Tuple> result = new ArrayList<Tuple>();
		try {
			if(StringUtils.isNotBlank(bean.getBouquetNo()) && !"0".equals(bean.getBouquetNo())) {
				//GET_PLACING_BOUQUET_LIST  //left join  
				result = placementCustomRepository.getPlacingBouquetList(bean);
				 
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo()) && !"0".equals(bean.getBaseProposalNo())) {
				//GET_PLACING_BASELAYER_LIST //left join  
				result = placementCustomRepository.getPlacingBaselayerList(bean);
				
			}else {
				//GET_PLACING_LIST //left join  
				String prop = StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo();
				result = placementCustomRepository.getPlacingList(bean, prop);
			}
			
    		if(result.size()>0) {
				for(int i=0;i<result.size();i++) {
					Tuple map=result.get(i);
					GetPlacingInfoRes1 res = new GetPlacingInfoRes1();
					  res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());  
					 // res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());  
					  res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());  
					  res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());  
					  res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());  
					  res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString());  
					  res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString());  
					  res.setShareOffered(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()));  
					  res.setOfferStatus(map.get("OFFER_STATUS")==null?"":map.get("OFFER_STATUS").toString());
					  res.setMailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString()); 
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
				result=placementCustomRepository.GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId());
			}else {
				result=placementCustomRepository.GetPlacementSearchEdit(bean);
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
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()));
					res.setWrittenLine(map.get("SHARE_WRITTEN")==null?"":fm.formattereight(map.get("SHARE_WRITTEN").toString()));
					res.setBrokerage(map.get("BROKERAGE_PER")==null?"":map.get("BROKERAGE_PER").toString());
					res.setWrittenvaliditydate(map.get("WRITTEN_LINE_VALIDITY")==null?"":formatdate(map.get("WRITTEN_LINE_VALIDITY")));
					res.setWrittenvalidityRemarks(map.get("WRITTEN_LINE_REMARKS")==null?"":map.get("WRITTEN_LINE_REMARKS").toString());
					res.setProposedWL(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":fm.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()));
					res.setSignedLine(map.get("SHARE_SIGNED")==null?"":fm.formattereight(map.get("SHARE_SIGNED").toString()));
					res.setProposedSL(map.get("SHARE_PROPOSED_SIGNED")==null?"":fm.formattereight(map.get("SHARE_PROPOSED_SIGNED").toString()));
					res.setReoffer(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()));
					Double epi=map.get("EPI_AMOUNT")==null?0d:Double.parseDouble(map.get("EPI_AMOUNT").toString());
					Double exRate=map.get("EXCAHNGE_RATE")==null?0d:Double.parseDouble(map.get("EXCAHNGE_RATE").toString());
					res.setEpi(fm.formatter(String.valueOf(epi/exRate)));
					if("CSL".equalsIgnoreCase(bean.getNewStatus())) {
						res.setTqrBrokerageAmt(fm.formatter(String.valueOf((epi/exRate)*Double.parseDouble(res.getSignedLine())/100*Double.parseDouble(res.getBrokerage())/100)));
					}
					
					res.setSignedLineValidity(map.get("SHARE_LINE_VALIDITY")==null?"":formatdate(map.get("SHARE_LINE_VALIDITY")));
					res.setSignedLineRemarks(map.get("SHARE_LINE_REMARKS")==null?"":map.get("SHARE_LINE_REMARKS").toString());
					res.setEmailStatus(map.get("MAIL_STATUS")==null?"":map.get("MAIL_STATUS").toString());
					res.setPsignedLine(map.get("SHARE_SIGNED")==null?"":fm.formattereight(map.get("SHARE_SIGNED").toString()));
					resList.add(res);
					}
				res1.setPlacingDetails(resList);
				}
			if(StringUtils.isBlank(bean.getSearchType())) {
				List<Tuple>list=placementCustomRepository.getStatusInfo(bean);
			 
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
			res1.setStatusUpdateDate(formatdate(new Date()));
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
	public UpdatePlacementRes1 updatePlacement(UpdatePlacementReq bean) {
		UpdatePlacementRes res=new UpdatePlacementRes();
		UpdatePlacementRes1 response = new UpdatePlacementRes1();
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
				plamendId=placementCustomRepository.getMaxAmendId(bean.getBranchCode(),bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId());
				bean.setPlacementamendId(StringUtils.isBlank(plamendId)?"0":plamendId);
				
				result=placementCustomRepository.getPlacementDetails(bean.getEproposalNo(),bean.getReinsurerId(),bean.getBrokerId(),bean.getBranchCode());
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
					entity.setShareWritten(StringUtils.isBlank(req.getWrittenLine())? null : new BigDecimal(req.getWrittenLine()));
					entity.setShareProposalWritten(StringUtils.isBlank(req.getProposedWL())? null : new BigDecimal(req.getProposedWL()));
					entity.setWrittenLineValidity(StringUtils.isBlank(req.getWrittenvaliditydate())? null :sdf.parse(req.getWrittenvaliditydate()));
					entity.setWrittenLineRemarks(StringUtils.isBlank(req.getWrittenvalidityRemarks())? null : req.getWrittenvalidityRemarks());
					entity.setShareSigned(StringUtils.isBlank(req.getSignedLine())? null : new BigDecimal(req.getSignedLine()));
					entity.setShareLineValidity(StringUtils.isBlank(req.getSignedLineValidity())? null :sdf.parse(req.getSignedLineValidity()));
					entity.setShareLineRemarks(StringUtils.isBlank(req.getSignedLineRemarks())? null :req.getSignedLineRemarks());
					entity.setShareProposedSigned(StringUtils.isBlank(req.getProposedSL())? null :new BigDecimal(req.getProposedSL()));
					entity.setBrokerage(StringUtils.isBlank(req.getBrokerage())? null :new BigDecimal(req.getBrokerage()));
					entity.setReinsurerContractNo(StringUtils.isBlank(req.getRenewalcontractNo())? null :req.getRenewalcontractNo());
				}
				entity.setPlacementAmendId(StringUtils.isBlank(bean.getPlacementamendId())? BigDecimal.ZERO:new BigDecimal(bean.getPlacementamendId()));
				entity.setStatusNo(StringUtils.isBlank(bean.getStatusNo())? null: new BigDecimal(bean.getStatusNo()));
				entity.setApproverStatus(bean.getApproverStatus());
				entity.setUserId(bean.getUserId());
				entity.setBranchCode(bean.getBranchCode());
				entity.setSysDate(new Date());
				ripRepo.saveAndFlush(entity);
			}
			updateStatus(bean);
			res.setCorrespondentId(bean.getCorresId());
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
	public CommonResponse updateStatus(UpdatePlacementReq bean) {
		CommonResponse response = new CommonResponse();
		String corresId="",statusNo="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if(StringUtils.isBlank(bean.getCorresId())) {
			List<Map<String,Object>> list  = queryImpl.selectSingle("SELECT  CORRESPONDENT_SEQ.NEXTVAL seqval FROM DUAL", new String[] {});
			if (!CollectionUtils.isEmpty(list)) {
				corresId = list.get(0).get("SEQVAL") == null ? "" : list.get(0).get("SEQVAL").toString();
			}
			bean.setCorresId(corresId);
			}
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
				entity.setUpdateDate(StringUtils.isBlank(bean.getUpdateDate())?new Date():sdf.parse(bean.getUpdateDate()));
				entity.setStatus("Y");
				entity.setBranchCode(bean.getBranchCode());
				entity.setSysDate(new Date());
				entity.setCorrespondentId(new BigDecimal(bean.getCorresId()));
				entity.setStatusNo(new BigDecimal(bean.getStatusNo()));
				entity.setUserId(bean.getUserId());
				entity.setApproverStatus(bean.getApproverStatus());				
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
			insertDocdetails(bean,res);
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
	public InsertDocdetailsRes insertDocdetails(UploadDocumentReq bean, UploadDocumentRes1 resp) {
		InsertDocdetailsRes res = new InsertDocdetailsRes();
		 try {
			 if(bean.getInsertDocdetailsReq()!=null) {
				 String filePath=commonPath+"documents/"+"PL/";
				
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
				
				encodeBase64ToFile( req.getUpload(),copyFile);
				//res.setFileName(fileName);
				
				//INSET_NOTIFY_ATTACHEMENT
				NotificationAttachmentDetail entity = new NotificationAttachmentDetail();	
				entity.setDocId(StringUtils.isBlank(docId)?new BigDecimal("1"):new BigDecimal(docId));
				entity.setDocType(req.getDocTypeId());
				entity.setSno(new BigDecimal(resp.getSno()));
				entity.setBouquetNo(fm.formatBigDecimal(resp.getBouquetNo()));
				entity.setBaseProposalNo(fm.formatBigDecimal(resp.getBaseproposalNo()));
				entity.setProposalNo(fm.formatBigDecimal(resp.getProposalNo()));
				entity.setReinsurerId(resp.getReinsurerId());
				entity.setBrokerId(resp.getBrokerId());
				entity.setOrgFileName(req.getUploadFileName());
				entity.setOurFileName(fileName);
				entity.setFileLocation(filePath);
				entity.setBranchCode(bean.getBranchCode());
				entity.setUserId(bean.getUserId());
				entity.setEntryDate(new Date());
				entity.setCorrespondentId(fm.formatBigDecimal(bean.getCorrespondentId()));
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
		List<Tuple> list=null;String corresId="";
		try {
			List<Map<String,Object>> list1  = queryImpl.selectSingle("SELECT  CORRESPONDENT_SEQ.NEXTVAL seqval FROM DUAL", new String[] {});
			if (!CollectionUtils.isEmpty(list1)) {
				corresId = list1.get(0).get("SEQVAL") == null ? "" : list1.get(0).get("SEQVAL").toString();
			}
			if("C".equals(bean.getPlacementMode())) {
				list=placementCustomRepository.GetPlacementBouquet(bean.getBranchCode(),bean.getBrokerId(),bean.getBouquetNo(),bean.getReinsurerId(),bean.getBaseProposalNo());
			}
			else {
				list=placementCustomRepository.GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId())	;					
			}
			
			if(!CollectionUtils.isEmpty(list)) {
				for(int  i=0;i<list.size();i++) {
					Tuple  map=list.get(i);
					AttachFileRes1 res = new AttachFileRes1();
					res.setCorrespondentId(corresId);
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setEproposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString());
					resList.add(res);
					insertDocdetails(bean,res,corresId);
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
	private void insertDocdetails(AttachFileReq bean, AttachFileRes1 res, String corresId) {
		 try {
			 if(bean.getInsertDocdetailsReq()!=null) {
				 String filePath=commonPath+"documents/"+"PL/";
				
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
				
				encodeBase64ToFile( req.getUpload(),copyFile);
				//res.setFileName(fileName);
				
				//INSET_NOTIFY_ATTACHEMENT
				NotificationAttachmentDetail entity = new NotificationAttachmentDetail();	
				entity.setDocId(StringUtils.isBlank(docId)?new BigDecimal("1"):new BigDecimal(docId));
				entity.setDocType(req.getDocTypeId());
				entity.setSno(new BigDecimal(res.getSno()));
				entity.setBouquetNo(fm.formatBigDecimal(bean.getBouquetNo()));
				entity.setBaseProposalNo(fm.formatBigDecimal(res.getBaseProposalNo()));
				entity.setProposalNo(fm.formatBigDecimal(res.getEproposalNo()));
				entity.setReinsurerId(res.getReinsurerId());
				entity.setBrokerId(res.getBrokerId());
				entity.setOrgFileName(req.getUploadFileName());
				entity.setOurFileName(fileName);
				entity.setFileLocation(filePath);
				entity.setBranchCode(bean.getBranchCode());
				entity.setUserId(bean.getUserId());
				entity.setEntryDate(new Date());
				entity.setCorrespondentId(fm.formatBigDecimal(corresId));
				entity.setDocDescription(req.getDocDesc());
				notiRepo.saveAndFlush(entity);
				}
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	

	@Override
	@Transactional
	public SendMailRes sendMail(SendMailReq bean) {
		SendMailRes response = new SendMailRes();
		String status=null;
		UpdatePlacementReq req=new UpdatePlacementReq();
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
			String mailBody= bean.getMailBody()+"<br/>"+(StringUtils.isBlank(bean.getMailRemarks())?"":bean.getMailRemarks())+""+bean.getMailRegards();
			bean.setMailBody(mailBody);
			if(toAddress!=null && !"".equals(toAddress)){
				String[] toAddresses = (toAddress.indexOf(",")!=-1)?toAddress.split(","):new String[]{toAddress};
				String[] ccAddresses = new String[0];
				if(ccAddress!=null && !"".equals(ccAddress)){
					ccAddresses = (ccAddress.indexOf(",")!=-1)?ccAddress.split(","):new String[]{ccAddress};
				}
			if(!"PC".equalsIgnoreCase(bean.getMailType())) {	
				InsertMailDetailsRes res=insertMailDetails(bean);
				
				req.setBranchCode(bean.getBranchCode());
				req.setCorresId(bean.getCorresId());
				req.setStatusNo(bean.getStatusNo());
				req.setApproverStatus("Y");
				List<UpdatePlacementListReq> placementListReq=new ArrayList<UpdatePlacementListReq>();
				List<InsertMailDetailsRes1> resp=res.getCommonResponse();
				for(int i=0;i<resp.size();i++) {
					UpdatePlacementListReq ureq=new UpdatePlacementListReq();
					ureq.setSno(resp.get(i).getSno());
					ureq.setProposalNo(resp.get(i).getEproposalNo());
					ureq.setBouquetNo(resp.get(i).getBouquetNo());
					ureq.setReinsurerId(resp.get(i).getReinsurerId());
					ureq.setBrokerId(resp.get(i).getBrokerId());
					ureq.setBaseproposalNo(resp.get(i).getBaseProposalNo());
					placementListReq.add(ureq);
				}
				req.setPlacementListReq(placementListReq);
			}
			Multipart multipart=GetMailAttachment(bean);
			status=sendResponseMail(hostName, user, pwd, mailform, subject, multipart, toAddresses, ccAddresses, shortAddress,port);
			if(!"PC".equalsIgnoreCase(bean.getMailType())) {	
				if("Success".equals(status) && "P".equals(bean.getMailType())) {
					req.setStatus("P");
					updateStatus(req); 
				}
				updateMailDetails(req,status,bean.getMailType());
				} 
			}
			response.setResponse(String.valueOf(status));
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
			
			List<GetExistingAttachListRes1>  list=getMailAttachList(bean);
			if(!CollectionUtils.isEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					GetExistingAttachListRes1 map=list.get(i);
					BodyPart messageBodyPart = new MimeBodyPart();
					String filePath="";
					if("PC".equalsIgnoreCase(bean.getMailType())) {	
						filePath=commonPath+"documents/"+"PC/";
					}else {
						filePath=commonPath+"documents/"+"PL/";
					}
					String fileName=map.getOrgFileName()==null?"":map.getOrgFileName().toString();
					String orgfileName=map.getOurFileName()==null?"":map.getOurFileName().toString();
					if(fileName!=null ){
						DataSource source = new FileDataSource(filePath+orgfileName);
						messageBodyPart.setDataHandler(new DataHandler(source));
						messageBodyPart.setFileName(fileName);
						multipart.addBodyPart(messageBodyPart);
					 }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return multipart;
	}

	private List<GetExistingAttachListRes1> getMailAttachList(SendMailReq bean) {
		List<GetExistingAttachListRes1> resList = new ArrayList<GetExistingAttachListRes1>();
		List<Tuple> list=null;
		try {
			if("PC".equalsIgnoreCase(bean.getMailType())) {	
				list=uploadCustomRepository.getMailAttachList(bean);
			}else {
				list=placementCustomRepository.getMailAttachList(bean);
			}
				if(list.size()>0) {
					for(Tuple data: list) {
						GetExistingAttachListRes1 res = new GetExistingAttachListRes1();
						res.setDocDescription(data.get("DOC_DESCRIPTION")==null?"":data.get("DOC_DESCRIPTION").toString());
						res.setDocId(data.get("DOC_ID")==null?"":data.get("DOC_ID").toString());
						res.setOrgFileName(data.get("ORG_FILE_NAME")==null?"":data.get("ORG_FILE_NAME").toString());
						res.setOurFileName(data.get("OUR_FILE_NAME")==null?"":data.get("OUR_FILE_NAME").toString());						
	      				resList.add(res);
	      			}
	      		}
				
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
			}
		return resList;
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
	//@Override
	public InsertMailDetailsRes insertMailDetails(SendMailReq bean) {
		InsertMailDetailsRes response = new InsertMailDetailsRes();
		List<InsertMailDetailsRes1> resList = new ArrayList<InsertMailDetailsRes1>();
		List<Tuple>list=null;
		try {
			//INSERT_MAIL_NOTIFICATION
			if("C".equals(bean.getPlacementMode())) {
				list=placementCustomRepository.GetPlacementBouquet(bean.getBranchCode(),bean.getBrokerId(),bean.getBouquetNo(),bean.getReinsurerId(),bean.getBaseProposalNo());
			}
			else {
				list=placementCustomRepository.GetPlacementEdit(bean.getBranchCode(),bean.getBrokerId(),bean.getEproposalNo(),bean.getReinsurerId());
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
	public CommonResponse updateMailDetails(UpdatePlacementReq bean, String status, String mailType) {
		CommonResponse response = new CommonResponse();
		try {
		for(int i=0;i<bean.getPlacementListReq().size();i++) {
			UpdatePlacementListReq resp =bean.getPlacementListReq().get(i);
			
			placementCustomRepository.updateNotificationStatus(resp,bean,status);
			placementCustomRepository.updatePlacement(resp,bean,mailType);
			placementCustomRepository.updateAttachementStatus(resp,bean);
			placementCustomRepository.updatePlacementStatus(resp,bean);
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
			 String filePath=commonPath+"documents/"+"PL/";
			//DOWNLOAD_ATTACHED_FILE
			NotificationAttachmentDetail list =  notiRepo.findByDocIdAndOrgFileName(new BigDecimal(req.getDocId()), req.getFileName());
			if(list!=null) {
				result = list.getOurFileName();
			}
			final File copyFile = new File(filePath+result);
			String file=encodeFileToBase64(copyFile);
			response.setResponse(file);
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
			List<Tuple> list=placementCustomRepository.getPlacementViewList(bean);
			
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
					res.setUpdateDate(map.get("UPDATE_DATE")==null?"":formatdate(map.get("UPDATE_DATE")).toString());
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
			List<Tuple> list=placementCustomRepository.getPlacementView(bean);
			
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

//	@Override
//	public PlacementSummaryRes placementSummary(PlacementSummaryReq bean) {
//		PlacementSummaryRes response = new PlacementSummaryRes();
//		List<Map<String,Object>>list=null;
//		String query="";
//		try {
//			String[] obj=new String[2];
//			query= "NEW_CONTRACT_PL_SUMMARY"; //left outer join
//			 String qutext = prop.getProperty(query);
//			obj[0]=bean.getBranchCode();
//			obj[1]=bean.getProposalNo();
//			
//			if(StringUtils.isNotBlank(bean.getSearchType()) && null !=bean.getSearchType()){
//            	
//        		if(StringUtils.isNotBlank(bean.getCompanyNameSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getCompanyNameSearch() + "%")});
//            		qutext += " " + " AND UPPER((SELECT CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME ELSE PI.FIRST_NAME || ' ' || PI.LAST_NAME END NAME FROM PERSONAL_INFO PI WHERE CUSTOMER_TYPE='R'  AND CUSTOMER_ID=TRP.REINSURER_ID AND BRANCH_CODE=TRP.BRANCH_CODE  AND AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO WHERE CUSTOMER_ID=PI.CUSTOMER_ID))) LIKE UPPER(?)";
//            	}
//        		if(StringUtils.isNotBlank(bean.getBrokerNameSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getBrokerNameSearch() + "%")});
//            		qutext += " " + " AND UPPER((SELECT CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME ELSE PI.FIRST_NAME || ' ' || PI.LAST_NAME END NAME FROM PERSONAL_INFO PI WHERE CUSTOMER_TYPE='B'  AND CUSTOMER_ID=TRP.BROKER_ID AND BRANCH_CODE=TRP.BRANCH_CODE AND AMEND_ID=(SELECT MAX(AMEND_ID) FROM PERSONAL_INFO WHERE CUSTOMER_ID=PI.CUSTOMER_ID))) LIKE UPPER(?)";
//            	}
//            	if(StringUtils.isNotBlank(bean.getUwYearSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getUwYearSearch() + "%")});
//            		qutext += " " +" AND UPPER(UW_YEAR) LIKE UPPER(?)";
//            	}
//            	if(StringUtils.isNotBlank(bean.getUwYearToSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getUwYearToSearch() + "%")});
//            		qutext += " " +" AND UPPER(UW_YEAR_TO) LIKE UPPER(?)";
//            	}
//            	if(StringUtils.isNotBlank(bean.getIncepDateSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getIncepDateSearch() + "%")});
//            		qutext += " "  +" AND TO_CHAR(INCEPTION_DATE,'DD/MM/YYYY') LIKE ?";;
//            	}
//            	if(StringUtils.isNotBlank(bean.getExpDateSearch())){
//            		obj = dropDownImple.getIncObjectArray(obj, new String[]{("%" + bean.getExpDateSearch() + "%")});
//            		qutext += " " +" AND TO_CHAR(EXPIRY_DATE,'DD/MM/YYYY') LIKE ?";;
//            	}
//            	qutext += " " + "ORDER BY PM.PROPOSAL_NO, TRP.SNO";
//            	
//            }else{
////            	bean.setCompanyNameSearch("");
////            	bean.setBrokerNameSearch("");
////            	bean.setUwYearSearch("");
////            	bean.setUwYearToSearch("");
////            	bean.setIncepDateSearch("");
////            	bean.setExpDateSearch("");
//            }
//	        	query1 =queryImpl.setQueryProp(qutext, obj);
//	    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//	    		try {
//	    			 list = query1.getResultList();
//	    		} catch(Exception e) {
//	    			e.printStackTrace();
//	    		} 
//	    		 List<PlacementSummaryRes1> resList =new ArrayList<PlacementSummaryRes1>();
//	             if(list!=null && list.size()>0){
//	             for (int i = 0; i < list.size(); i++) {
//	                 Map<String, Object> tempMap = list.get(i);
//	                 PlacementSummaryRes1 tempBean = new PlacementSummaryRes1();
//	                 tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString()); 
//	                 tempBean.setBaseProposal(tempMap.get("BASE_PROPOSAL") == null ? "" : tempMap.get("BASE_PROPOSAL").toString()); 
//	                 tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString()); 
//	                 tempBean.setRskTreatyid(tempMap.get("TREATY_NAME") == null ? "" : tempMap.get("TREATY_NAME").toString()); 
//	                 tempBean.setLayerSection(tempMap.get("LAYER_SECTION") == null ? "" : tempMap.get("LAYER_SECTION").toString()); 
//	                 tempBean.setSno(tempMap.get("SNO") == null ? "" : tempMap.get("SNO").toString()); 
//	                 tempBean.setReinsurerName(tempMap.get("REINSURER_NAME") == null ? "" : tempMap.get("REINSURER_NAME").toString()); 
//	                 tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString()); 
//	                 tempBean.setCurrency(tempMap.get("CURRENCY") == null ? "" : tempMap.get("CURRENCY").toString()); 
//	                 tempBean.setEpi100Oc(tempMap.get("EPI_100_OC") == null ? "" : fm.formatter(tempMap.get("EPI_100_OC").toString())); 
//	                 tempBean.setEpi100Dc(tempMap.get("EPI_100_DC") == null ? "" : fm.formatter(tempMap.get("EPI_100_DC").toString())); 
//	                 tempBean.setPlacingStatus(tempMap.get("PLACING_STATUS") == null ? "" : tempMap.get("PLACING_STATUS").toString()); 
//	                 tempBean.setShareSigned(tempMap.get("SHARE_SIGNED") == null ? "" : fm.formattereight(tempMap.get("SHARE_SIGNED").toString())); 
//	                tempBean.setBrokerage(tempMap.get("BROKERAGE") == null ? "" : fm.formattereight(tempMap.get("BROKERAGE").toString())); 
//	                tempBean.setBrokerageAmt(tempMap.get("BROKERAGE_AMT") == null ? "" :  fm.formatter(tempMap.get("BROKERAGE_AMT").toString()));
//	                resList.add(tempBean);
//	             }
//	             }
//	 			 response.setCommonResponse(resList);
//	 			 response.setMessage("Success");
//	 			 response.setIsError(false);
//	 			}catch(Exception e){
//	 					log.error(e);
//	 					e.printStackTrace();
//	 					response.setMessage("Failed");
//	 					response.setIsError(true);
//	 				}
//	 			return response;
//	}
	@Override
	public PlacementSummaryRes placementSummary(PlacementSummaryReq bean) {
		PlacementSummaryRes response = new PlacementSummaryRes();
		try {
			//NEW_CONTRACT_PL_SUMMARY
			List<Tuple> list = placementCustomRepository.newContractPlSummary(bean); //need to check query single row
			
	    		 List<PlacementSummaryRes1> resList =new ArrayList<PlacementSummaryRes1>();
	             if(list!=null && list.size()>0){
	             for (int i = 0; i < list.size(); i++) {
	            	 Tuple tempMap = list.get(i);
	                 PlacementSummaryRes1 tempBean = new PlacementSummaryRes1();
	                 tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString()); 
	                 tempBean.setBaseProposal(tempMap.get("BASE_PROPOSAL") == null ? "" : tempMap.get("BASE_PROPOSAL").toString()); 
	                 tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString()); 
	                 tempBean.setRskTreatyid(tempMap.get("TREATY_NAME") == null ? "" : tempMap.get("TREATY_NAME").toString()); 
	                 tempBean.setLayerSection(tempMap.get("LAYER_SECTION") == null ? "" : tempMap.get("LAYER_SECTION").toString()); 
	                 tempBean.setSno(tempMap.get("SNO") == null ? "" : tempMap.get("SNO").toString()); 
	                 tempBean.setReinsurerName(tempMap.get("REINSURER_NAME") == null ? "" : tempMap.get("REINSURER_NAME").toString()); 
	                 tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString()); 
	                 tempBean.setCurrency(tempMap.get("CURRENCY") == null ? "" : tempMap.get("CURRENCY").toString()); 
	                 tempBean.setEpi100Oc(tempMap.get("EPI_100_OC") == null ? "" : fm.formatter(tempMap.get("EPI_100_OC").toString())); 
	                 tempBean.setEpi100Dc(tempMap.get("EPI_100_DC") == null ? "" : fm.formatter(tempMap.get("EPI_100_DC").toString())); 
	                 tempBean.setPlacingStatus(tempMap.get("PLACING_STATUS") == null ? "" : tempMap.get("PLACING_STATUS").toString()); 
	                 tempBean.setShareSigned(tempMap.get("SHARE_SIGNED") == null ? "" : fm.formattereight(tempMap.get("SHARE_SIGNED").toString())); 
	                 tempBean.setBrokerage(tempMap.get("BROKERAGE") == null ? "" : fm.formattereight(tempMap.get("BROKERAGE").toString())); 
	                
	                 DecimalFormat decfor = new DecimalFormat("0.00");
					  //(EPI_100_DC)*(BROKERAGE/100)*(SHARE_SIGNED/100) = BROKERAGE_AMT
					  String epi100Dc = tempMap.get("EPI_100_DC")==null?"0": tempMap.get("EPI_100_DC").toString();
					  String brokerage = tempMap.get("BROKERAGE").toString();
					  String sharesigned = decfor.format(tempMap.get("SHARE_SIGNED"));
					 
					  double brokerageAmt = Math.round(Double.valueOf(epi100Dc) * (Double.valueOf(brokerage)/100) * (Double.valueOf(sharesigned)/100));
					  
					  tempBean.setBrokerageAmt(String.valueOf(brokerageAmt)); 
	                
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
		Map<String,Object>  values = new HashMap<String,Object>();
		try {
		
			List<MailTemplateMaster> list = mailTemplateMasterRepository.findByMailType(req.getMailType());
		
			if(!CollectionUtils.isEmpty(list)) {
				MailTemplateMaster map=list.get(0);
				bean.setMailSubject(map.getMailSubject()==null?"":map.getMailSubject().toString());
				bean.setMailBody(map.getMailBody()==null?"":map.getMailBody().toString());
				bean.setMailTo(map.getEmailTo()==null?"":map.getEmailTo().toString());
				bean.setMailCC(map.getEmailCc()==null?"":map.getEmailCc().toString());
				bean.setMailRegards(map.getMailRegards()==null?"":map.getMailRegards().toString());
			}
			String mailbody=bean.getMailBody(),mailsub=bean.getMailSubject();
			if(!"PC".equalsIgnoreCase(req.getMailType())) {
			String proposal=StringUtils.isBlank(req.getEproposalNo())?req.getProposalNo():req.getEproposalNo();
			List<Tuple> list1 = placementCustomRepository.getExistingProposal(proposal, req.getBranchCode(),req.getReinsurerId());
		
			if(!CollectionUtils.isEmpty(list1)) {
				Tuple map=list1.get(0);
				String bouquetNo=map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString();
				//String proposalNo=map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
				//String baseproposalNo=map.get("BASE_LAYER")==null?"":map.get("BASE_LAYER").toString();
				String offerNo=map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString();
				if(StringUtils.isNotBlank(bouquetNo)) {
					mailsub=mailsub+" "+bouquetNo+"";
				}else if(StringUtils.isNotBlank(offerNo)) {
					mailsub=mailsub+" "+offerNo+" ";
				}
				 values.put("COMPANY_NAME", map.get("COMPANY_NAME") );
				 values.put("TransactionNo", req.getTransactionNo());
				 
				 if("PWL".equalsIgnoreCase(req.getNewStatus())) {
					if(mailbody.contains("COMPANY_NAME") == true) {
						mailbody = mailbody.replace("{COMPANY_NAME}", map.get("REINSURER_NAME").toString());
					}
				 }
				
				
			}
				mailbody+=BodyTableFrame(req);
			}else {
			
				 values.put("TransactionNo", req.getTransactionNo());
				 
			}
			for (Map.Entry entry: values.entrySet()) {
				
				if(mailbody.contains(entry.getKey().toString()) == true) {
					mailbody = mailbody.replace("{"+entry.getKey().toString()+"}", entry.getValue()==null?"":entry.getValue().toString());
				}
				
				if(mailsub.contains(entry.getKey().toString()) == true) {
					mailsub = mailsub.replace("{"+entry.getKey().toString()+"}", entry.getValue()==null?"":entry.getValue().toString());
				}
			}
			
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
		List<Tuple> list=placementCustomRepository.MailproposalInfo(bean);
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
			String subclass=map.get("SUB_CLASS")==null?"":map.get("SUB_CLASS").toString();
			String productId=map.get("PRODUCT_ID")==null?"":map.get("PRODUCT_ID").toString();
			if(!"ALL".equals(subclass)) {
				subclass=dropDownImple.getSubClass(subclass, bean.getBranchCode(), productId);
			}
			messageContent+="<tr>"+
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("BUSINESS_TYPE")==null?"":map.get("BUSINESS_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("CLASS")==null?"":map.get("CLASS").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+subclass+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("TREATY_TYPE")==null?"":map.get("TREATY_TYPE").toString())+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("RSK_TREATYID")==null?"":map.get("RSK_TREATYID").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("SECTION_NO")==null?"":map.get("SECTION_NO").toString())+"</td>" +
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":formatdate(map.get("INS_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":formatdate(map.get("EXP_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" + 
			"</tr>";
		}
		messageContent+=	"</table>" +
				"    </div>" + 
				"</body>" + 
				"" + 
				"</html>";
	
	return messageContent.toString();
	}
	public String getPropsalWrittenMsg(List<Tuple> agentWiseReport, GetMailTemplateReq bean) {
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
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":formatdate(map.get("INS_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":formatdate(map.get("EXP_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":fm.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()))+"</td>" +
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
			"<td style=\"border: 1px solid #000000;\">"+(map.get("INS_DATE")==null?"":formatdate(map.get("INS_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;\">"+(map.get("EXP_DATE")==null?"":formatdate(map.get("EXP_DATE")))+"</td>" + 
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_OFFERED")==null?"":fm.formattereight(map.get("SHARE_OFFERED").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":fm.formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()))+"</td>" +
			"<td style=\"border: 1px solid #000000;text-align: right;\">"+(map.get("SHARE_PROPOSED_SIGNED")==null?"":fm.formattereight(map.get("SHARE_PROPOSED_SIGNED").toString()))+"</td>" +
			"</tr>";
		} 
		messageContent+=	"</table>" +
				"    </div>" + 
				"</body>" + 
				"" + 
				"</html>";
	
	return messageContent.toString();
	}
	private void  encodeBase64ToFile(String file, File copyFile) {
		byte[] data = Base64.getDecoder().decode(file);

		try( OutputStream stream = new FileOutputStream(copyFile) ) 
		{
		   stream.write(data);
		}
		catch (Exception e) 
		{
		   System.err.println("Couldn't write to file...");
		   e.printStackTrace();
		   
		}
	}
	private static String encodeFileToBase64(File file) {
	    try {
	        byte[] fileContent = Files.readAllBytes(file.toPath());
	        return Base64.getEncoder().encodeToString(fileContent);
	    } catch (Exception e) {
	        throw new IllegalStateException("could not read file " + file, e);
	    }
	}
	@Override
	public GetPlacementNoRes getPlacementNo(SavePlacingReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetApprovalPendingListRes getApprovalPendingList(GetApprovalPendingListReq bean,String status) {
		GetApprovalPendingListRes response = new GetApprovalPendingListRes();
		List<Tuple> list=new ArrayList<>();
		List<GetApprovalPendingListRes1> resList = new ArrayList<GetApprovalPendingListRes1>();
		try {
				list = placementCustomRepository.getApprovalPendingList(bean, status); 
			
			DecimalFormat formatter = new DecimalFormat("#0.00000000");     
			if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Tuple map=list.get(i);
					GetApprovalPendingListRes1 res = new GetApprovalPendingListRes1();
					res.setSno(map.get("SNO")==null?"":map.get("SNO").toString()); 
					res.setStatusNo(map.get("STATUS_NO")==null?"":map.get("STATUS_NO").toString());		
					res.setBouquetNo(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString()); 
					res.setBaseProposalNo(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString()); 
					res.setProposalNo(map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString()); 
					res.setCedingCompany(map.get("CEDING_COMPANY_NAME")==null?"":map.get("CEDING_COMPANY_NAME").toString());
					res.setReinsurerId(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString()); 
					res.setBrokerId(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setReinsurerName(map.get("REINSURER_NAME")==null?"":map.get("REINSURER_NAME").toString()); 
					res.setBrokerName(map.get("BROKER_NAME")==null?"":map.get("BROKER_NAME").toString()); 
					res.setProposalWrittenLine(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":formatter.format(map.get("SHARE_PROPOSAL_WRITTEN")));		
					res.setBrokeragePer(map.get("BROKERAGE_PER")==null?"":formatter.format(map.get("BROKERAGE_PER"))); 
					res.setWrittenLine(map.get("SHARE_WRITTEN")==null?"":sdf.format(map.get("SHARE_WRITTEN"))); 
					res.setOfferNo(map.get("OFFER_NO")==null?"":map.get("OFFER_NO").toString());
					res.setConfirmedSignedLine(map.get("SHARE_SIGNED")==null?"":map.get("SHARE_SIGNED").toString());
					res.setProposedSignedLine(map.get("SHARE_PROPOSED_SIGNED")==null?"":map.get("SHARE_PROPOSED_SIGNED").toString());
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":map.get("SHARE_OFFERED").toString());
					res.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString());
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
	public CommonResponse updateRiplacement(UpdateRiplacementReq req) {
		CommonResponse response = new CommonResponse();
		try {
				placementCustomRepository.updateRiplacement(req);
				placementCustomRepository.updateRiplacementStatus(req);
				
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
	}
