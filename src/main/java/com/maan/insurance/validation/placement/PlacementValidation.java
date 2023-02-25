package com.maan.insurance.validation.placement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.error.ErrorCheck;
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
import com.maan.insurance.model.req.placement.InsertMailDetailsReq;
import com.maan.insurance.model.req.placement.ReinsListReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdateMailDetailsReq;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq;
import com.maan.insurance.model.req.placement.proposalInfoReq;
import com.maan.insurance.service.impl.placement.PlacementCustomRepository;
import com.maan.insurance.model.req.placement.PlacementSummaryReq;

@Service
public class PlacementValidation {
	private Logger log = LogManager.getLogger(PlacementValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private PlacementCustomRepository placementCustomRepository;

	
 public PlacementValidation() {
		
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("application_field_names.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			log.info(e);
		}
	}

public List<ErrorCheck> getMailToListVali(GetMailToListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBrokerId())) {
		//list.add(new ErrorCheck("Please Enter BrokerCompany", "BrokerCompany", "1"));
	}
	if (StringUtils.isBlank(req.getCedingId())) {
		//list.add(new ErrorCheck("Please Enter CedingCompany", "CedingCompany", "3"));
	}
	if (StringUtils.isBlank(req.getCurrentStatus())) {
		//list.add(new ErrorCheck("Please Enter CurrentStatus", "CurrentStatus", "4"));
	}
	if (StringUtils.isBlank(req.getNewStatus())) {
		//list.add(new ErrorCheck("Please Enter NewStatus", "NewStatus", "5"));
	}
	if (StringUtils.isBlank(req.getReinsurerId())) {
		//list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "6"));
	}
	return list;
}

public List<ErrorCheck> getExistingReinsurerListVali(GetExistingReinsurerListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	
	if(StringUtils.isNotBlank(req.getBouquetNo())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getBouquetNo())) {
			list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "2"));
		}
	}else if(StringUtils.isNotBlank(req.getBaseProposalNo())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(req.getBaseProposalNo())) {
			list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "1"));
		}
	}else {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		if (StringUtils.isBlank(StringUtils.isBlank(req.getEproposalNo())?req.getProposalNo():req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "4"));
		}
	}
	
	return list;
}

public List<ErrorCheck> getExistingAttachListVali(GetExistingAttachListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getEproposalNo())) {
		list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
	}
	if (StringUtils.isBlank(req.getProposalNo())) {
		list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
	}
	if (StringUtils.isBlank(req.getBrokerId())) {
		list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "4"));
	}
	if (StringUtils.isBlank(req.getCorresId())) {
		list.add(new ErrorCheck("Please Enter CorresId", "CorresId", "5"));
	}
	if (StringUtils.isBlank(req.getReinsurerId())) {
		list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "6"));
	}
	return list;
}

public List<ErrorCheck> getReinsurerInfoVali(GetReinsurerInfoReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getEproposalNo())) {
		list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
	}
	if(StringUtils.isNotBlank(req.getBouquetNo())) {
		if (StringUtils.isBlank(req.getBouquetNo())) {
			list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "3"));
		}
	}
	return list;
}

public List<ErrorCheck> getPlacementInfoListVali(GetPlacementInfoListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	
	if(StringUtils.isNotBlank(req.getBouquetNo())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBouquetNo())) {
			list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "5"));
		}
	}else if(StringUtils.isNotBlank(req.getBaseProposalNo())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBaseProposalNo())) {
			list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "4"));
		}
		
	}else {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(StringUtils.isBlank(req.getEproposalNo())?req.getProposalNo():req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
		}
	}
	
	return list;
}

public List<ErrorCheck> validatePlacing(SavePlacingReq bean) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	try {
	if(StringUtils.isNotBlank(bean.getBouquetNo()) || StringUtils.isNotBlank(bean.getBaseProposalNo())) {
		if(StringUtils.isBlank(bean.getPlacementMode())) {
			list.add(new ErrorCheck(prop.getProperty("error.placementmode.required"),"placementmode","01"));
		}
		else if("S".equals(bean.getPlacementMode())) {
			if(StringUtils.isBlank(bean.getNotplacedProposal()) && StringUtils.isBlank(bean.getPlacedProposal())) {
				list.add(new ErrorCheck(prop.getProperty("error.placednotpalced.required"),"placednotpalced","01"));
			}
		}
	}
	List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
	if(!CollectionUtils.isEmpty(bean.getReinsListReq())) {
		List<String>rebrlist=new ArrayList<String>();
	for(int i=0;i<bean.getReinsListReq().size();i++) {
		ReinsListReq req =bean.getReinsListReq().get(i);
		if(StringUtils.isBlank(req.getReinsureName())) {
			list.add(new ErrorCheck(prop.getProperty("error.reinsuere.required")+" "+String.valueOf(i+1),"reinsuere","01"));
		}
		if(StringUtils.isBlank(req.getPlacingBroker())) {
			list.add(new ErrorCheck(prop.getProperty("error.placingbroker.required")+" "+String.valueOf(i+1),"placingbroker","01"));
		}
		
		if(StringUtils.isNotBlank(req.getReinsureName()) && StringUtils.isNotBlank(req.getPlacingBroker())) {
			rebrlist.add(req.getReinsureName()+"~"+req.getPlacingBroker());
		}
		Map<String,Object> string = new HashMap<String,Object>();
		string.put("1","1");
		list1.add(string);
		
	}
//	bean.setReinsurerInfoList(list1); doubt
	int count=0;
	for(int i=0;i<bean.getReinsListReq().size();i++) {
		ReinsListReq req =bean.getReinsListReq().get(i);
		String rebr=req.getReinsureName()+"~"+req.getPlacingBroker();
		if (StringUtils.isNotEmpty(rebr)) {
			if (java.util.Collections.frequency(rebrlist, rebr) > 1) {
				if(count==0) {
				list.add(new ErrorCheck(prop.getProperty("error.placingbroker.duplicate"),"placingbroker","01"));
				count=1;
				}
			}
		}
	} 
}
	}catch(Exception e) {
		e.printStackTrace();
	}
	return list;
}

public List<ErrorCheck> getPlacingInfoVali(GetPlacingInfoReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getEproposalNo())) {
		list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
	}
	if (StringUtils.isBlank(req.getProposalNo())) {
		//list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
	}
	if (StringUtils.isBlank(req.getBaseProposalNo())) {
		//list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "4"));
	}
	if (StringUtils.isBlank(req.getBouquetNo())) {
		//list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "5"));
	}
	if (StringUtils.isBlank(req.getSearchBrokerId())) {
		//list.add(new ErrorCheck("Please Enter SearchBrokerId", "SearchBrokerId", "6"));
	}
	if (StringUtils.isBlank(req.getSearchReinsurerId())) {
		//list.add(new ErrorCheck("Please Enter SearchReinsurerId", "SearchReinsurerId", "7"));
	}
	if (StringUtils.isBlank(req.getSearchStatus())) {
		//list.add(new ErrorCheck("Please Enter SearchStatus", "SearchStatus", "8"));
	}
	if (StringUtils.isBlank(req.getSearchType())) {
		//list.add(new ErrorCheck("Please Enter SearchType", "SearchType", "9"));
	}
	return list;
}

public List<ErrorCheck> editPlacingDetailsVali(EditPlacingDetailsReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	
	if(StringUtils.isBlank(req.getSearchType())) {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "11"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
	}else {
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getSearchReinsurerId())) {
			list.add(new ErrorCheck("Please Enter SearchReinsurerId", "SearchReinsurerId", "7"));
		}
		if (StringUtils.isBlank(req.getSearchBrokerId())) {
			list.add(new ErrorCheck("Please Enter SearchBrokerId", "SearchBrokerId", "6"));
		}
		if (StringUtils.isBlank(req.getSearchStatus())) {
			list.add(new ErrorCheck("Please Enter SearchStatus", "SearchStatus", "8"));
		}
		if(StringUtils.isNotBlank(req.getBouquetNo())) {
			if (StringUtils.isBlank(req.getBouquetNo())) {
				list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "5"));
			}
		}else if(StringUtils.isNotBlank(req.getBaseProposalNo())) {
			if (StringUtils.isBlank(req.getBaseProposalNo())) {
				list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "4"));
			}
		}
	}
	
	return list;
}

public List<ErrorCheck> validationStatus(UpdatePlacementReq bean) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	try {
	if(StringUtils.isBlank(bean.getEmailBy())) {
		list.add(new ErrorCheck(prop.getProperty("error.emailBy.required"),"emailBy","01"));
	}if(StringUtils.isBlank(bean.getUpdateDate())) {
		list.add(new ErrorCheck(prop.getProperty("error.updatedate.required"),"updatedate","01"));
	}if(StringUtils.isBlank(bean.getNewStatus())) {
		list.add(new ErrorCheck(prop.getProperty("error.newstatus.required"),"newstatus","01"));
	}else {
		if(!CollectionUtils.isEmpty(bean.getPlacementListReq())) {
			for(int i=0;i<bean.getPlacementListReq().size();i++) {
				UpdatePlacementListReq req =bean.getPlacementListReq().get(i);
				if("P".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getShareOffered())) {
						list.add(new ErrorCheck(prop.getProperty("error.shareoffer.required")+" "+String.valueOf(i+1),"shareoffer","01"));
					}
				}else if("A".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getWrittenLine())) {
						list.add(new ErrorCheck(prop.getProperty("error.writtenLine.required")+" "+String.valueOf(i+1),"writtenLine","01"));
					}else {
						if(StringUtils.isNotBlank(req.getShareOffered())) {
							if(Double.parseDouble(req.getWrittenLine())>Double.parseDouble(req.getShareOffered())) {
								list.add(new ErrorCheck(prop.getProperty("error.writtenline.valid")+" "+String.valueOf(i+1),"proposedWL","01"));	
							}
						}
					}
					if(StringUtils.isBlank(req.getWrittenvaliditydate())) {
						//list.add(new ErrorCheck(prop.getProperty("error.writtenvaliditydate.required")+" "+String.valueOf(i+1),"writtenvaliditydate","01"));
					}if(StringUtils.isBlank(req.getWrittenvalidityRemarks())) {
						//list.add(new ErrorCheck(prop.getProperty("error.writtenvalidityRemarks.required")+" "+String.valueOf(i+1),"writtenvalidityRemarks","01"));
					}if(StringUtils.isBlank(req.getBrokerage())) {
						list.add(new ErrorCheck(prop.getProperty("error.brokeragep.required")+" "+String.valueOf(i+1),"brokeragep","01"));
					}
				}else if("RO".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getReoffer())) {
						list.add(new ErrorCheck(prop.getProperty("error.reoffer.required")+" "+String.valueOf(i+1),"reoffer","01"));
					}
				}else if("PWL".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getProposedWL())) {
						list.add(new ErrorCheck(prop.getProperty("error.proposedWL.required")+" "+String.valueOf(i+1),"proposedWL","01"));
					}else {
						if(StringUtils.isNotBlank(req.getWrittenLine())) {
							if(Double.parseDouble(req.getProposedWL())>Double.parseDouble(req.getWrittenLine())) {
								list.add(new ErrorCheck(prop.getProperty("error.proposedWL.valid")+" "+String.valueOf(i+1),"proposedWL","01"));	
							}
						}
					}
				}else if("SL".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getSignedLine())) {
						list.add(new ErrorCheck(prop.getProperty("error.signedLine.required")+" "+String.valueOf(i+1),"signedLine","01"));
					}else {
						if(StringUtils.isNotBlank(req.getWrittenLine())) {
							if(Double.parseDouble(req.getSignedLine())>Double.parseDouble(req.getWrittenLine())) {
								list.add(new ErrorCheck(prop.getProperty("error.signedLine.valid")+" "+String.valueOf(i+1),"signedLine","01"));	
							}
						}
					}if(StringUtils.isBlank(req.getSignedLineValidity())) {
						//list.add(new ErrorCheck(prop.getProperty("error.signedLineValidity.required")+" "+String.valueOf(i+1),"signedLineValidity","01"));
					}if(StringUtils.isBlank(req.getSignedLineRemarks())) {
						//list.add(new ErrorCheck(prop.getProperty("error.signedLineRemarks.required")+" "+String.valueOf(i+1),"signedLineRemarks","01"));
					}
				}else if("PSL".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getProposedSL())) {
						list.add(new ErrorCheck(prop.getProperty("error.proposedSL.required")+" "+String.valueOf(i+1),"proposedSL","01"));
					}if(StringUtils.isNotBlank(req.getSignedLine())) {
						if(Double.parseDouble(req.getProposedSL())>Double.parseDouble(req.getSignedLine())) {
							list.add(new ErrorCheck(prop.getProperty("error.signedLinePro.valid")+" "+String.valueOf(i+1),"psignedLine","01"));
						
						}
					}
				}else if("CSL".equals(bean.getNewStatus())) {
					if(StringUtils.isBlank(req.getSignedLine())) {
						list.add(new ErrorCheck(prop.getProperty("error.signedLine.required")+" "+String.valueOf(i+1),"signedLine","01"));
					}if(StringUtils.isNotBlank(req.getProposedSL())) {
						if(Double.parseDouble(req.getSignedLine())>Double.parseDouble(req.getProposedSL())) {
							list.add(new ErrorCheck(prop.getProperty("error.psignedLine.valid")+" "+String.valueOf(i+1),"psignedLine","01"));
						
						}
					}
					double sumOfShareSign = placementCustomRepository.sumOfShareSigned(req.getProposalNo());
					if(sumOfShareSign + Double.parseDouble(req.getSignedLine())>100) {
						list.add(new ErrorCheck("Confirmed signed line cannot exceed 100% for this proposal "+req.getProposalNo(), "SignedLine", "01"));
					}
				}
	
			}
	}
	}
	
	
	}catch(Exception e) {
		e.printStackTrace();
		}
	return list;
	}

	public List<ErrorCheck> uploadDocumentVali(UploadDocumentReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getCorrespondentId())) {
			list.add(new ErrorCheck("Please Enter CorrespondentId", "CorresId", "2"));
		}
		if (StringUtils.isBlank(req.getUserId())) {
			list.add(new ErrorCheck("Please Enter UserId", "UserId", "12"));
		}
		if (CollectionUtils.isEmpty(req.getInsertDocdetailsReq())) {
			list.add(new ErrorCheck("Please Enter Docdetails", "Docdetails", "13"));
		}
		if (CollectionUtils.isEmpty(req.getUploadDocumentReq1())) {
			list.add(new ErrorCheck("Please Enter Docdetails", "Docdetails", "14"));
		}
		return list;
	}

	public List<ErrorCheck> attachFileVali(AttachFileReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBaseProposalNo())) {
			//list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getBouquetNo())) {
			//list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "5"));
		}
		if (StringUtils.isBlank(req.getPlacementMode())) {
			//list.add(new ErrorCheck("Please Enter PlacementMode", "PlacementMode", "6"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "7"));
		}
		return list;
	}

	public List<ErrorCheck> sendMailVali(SendMailReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			//list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			//list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			//list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "4"));
		}
		if (StringUtils.isBlank(req.getCorresId())) {
			//list.add(new ErrorCheck("Please Enter CorresId", "CorresId", "5"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			//list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "6"));
		}
		if (StringUtils.isBlank(req.getMailBody())) {
			list.add(new ErrorCheck("Please Enter MailBody", "MailBody", "7"));
		}
		if (StringUtils.isBlank(req.getMailCC())) {
			list.add(new ErrorCheck("Please Enter MailCC", "MailCC", "8"));
		}
		if (StringUtils.isBlank(req.getMailRegards())) {
			list.add(new ErrorCheck("Please Enter MailRegards", "MailRegards", "9"));
		}
		if (StringUtils.isBlank(req.getMailRemarks())) {
			//list.add(new ErrorCheck("Please Enter MailRemarks", "MailRemarks", "10"));
		}
		if (StringUtils.isBlank(req.getMailSubject())) {
			list.add(new ErrorCheck("Please Enter MailSubject", "MailSubject", "11"));
		}
		if (StringUtils.isBlank(req.getMailTo())) {
			list.add(new ErrorCheck("Please Enter MailTo", "MailTo", "12"));
		}
		if (StringUtils.isBlank(req.getMailType())) {
			list.add(new ErrorCheck("Please Enter MailType", "MailType", "13"));
		}
		return list;
	}

	public List<ErrorCheck> insertMailDetailsVali(InsertMailDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBaseProposalNo())) {
			list.add(new ErrorCheck("Please Enter BaseProposalNo", "BaseProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "4"));
		}
		if (StringUtils.isBlank(req.getBouquetNo())) {
			list.add(new ErrorCheck("Please Enter BouquetNo", "BouquetNo", "5"));
		}
		if (StringUtils.isBlank(req.getPlacementMode())) {
			list.add(new ErrorCheck("Please Enter PlacementMode", "PlacementMode", "6"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "7"));
		}
		if (StringUtils.isBlank(req.getMailBody())) {
			list.add(new ErrorCheck("Please Enter MailBody", "MailBody", "8"));
		}
		if (StringUtils.isBlank(req.getMailCC())) {
			list.add(new ErrorCheck("Please Enter MailCC", "MailCC", "9"));
		}
		if (StringUtils.isBlank(req.getUserId())) {
			list.add(new ErrorCheck("Please Enter UserId", "UserId", "10"));
		}
		if (StringUtils.isBlank(req.getMailSubject())) {
			list.add(new ErrorCheck("Please Enter MailSubject", "MailSubject", "11"));
		}
		if (StringUtils.isBlank(req.getMailTo())) {
			list.add(new ErrorCheck("Please Enter MailTo", "MailTo", "12"));
		}
		if (StringUtils.isBlank(req.getMailType())) {
			list.add(new ErrorCheck("Please Enter MailType", "MailType", "13"));
		}
		return list;
	}

	public List<ErrorCheck> updateMailDetailsVali(UpdateMailDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getMailType())) {
			list.add(new ErrorCheck("Please Enter MailType", "MailType", "2"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "3"));
		}
		if (StringUtils.isBlank(req.getStatusNo())) {
			list.add(new ErrorCheck("Please Enter StatusNo", "StatusNo", "4"));
		}
		if (CollectionUtils.isEmpty(req.getUpdateMailDetailsReqList())) {
			list.add(new ErrorCheck("Please Enter MailDetails", "MailDetails", "5"));
		}
		return list;
	}

	public List<ErrorCheck> deleteFileVali(DeleteFileReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDocId())) {
			list.add(new ErrorCheck("Please Enter DocId", "DocId", "1"));
		}
		if (StringUtils.isBlank(req.getFileName())) {
			list.add(new ErrorCheck("Please Enter FileName", "FileName", "2"));
		}
		return list;
	}

	public List<ErrorCheck> getPlacementViewListVali(GetPlacementViewListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "3"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "4"));
		}
		return list;
	}

	public List<ErrorCheck> getPlacementViewVali(GetPlacementViewReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
		}
		if (StringUtils.isBlank(req.getEproposalNo())) {
			list.add(new ErrorCheck("Please Enter EproposalNo", "EproposalNo", "3"));
		}
		if (StringUtils.isBlank(req.getReinsurerId())) {
			list.add(new ErrorCheck("Please Enter ReinsurerId", "ReinsurerId", "4"));
		}
		if (StringUtils.isBlank(req.getNewStatus())) {
			list.add(new ErrorCheck("Please Enter NewStatus", "NewStatus", "5"));
		}
		return list;
	}

	public List<ErrorCheck> placementSummaryVali(PlacementSummaryReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		return list;
	}
	public List<ErrorCheck> getMailTemplateVali(GetMailTemplateReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getMailType())) {
			list.add(new ErrorCheck("Please Enter MailType", "MailType", "1"));
		}
		return list;
	}

	public List<ErrorCheck> proposalInfoVali(proposalInfoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		return list;
	}
	}
