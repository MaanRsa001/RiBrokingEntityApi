package com.maan.insurance.jpa.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.facultative.TtrnFacSi;
import com.maan.insurance.jpa.entity.facultative.TtrnLossDetails;
import com.maan.insurance.jpa.entity.propPremium.TtrnInsurerDetails;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.jpa.mapper.TtrnBonusMapper;
import com.maan.insurance.jpa.mapper.TtrnCrestazoneDetailsMapper;
import com.maan.insurance.jpa.mapper.TtrnFacSiMapper;
import com.maan.insurance.jpa.mapper.TtrnInsurerDetailsMapper;
import com.maan.insurance.jpa.mapper.TtrnLossDetailsMapper;
import com.maan.insurance.jpa.mapper.TtrnMndInstallmentsMapper;
import com.maan.insurance.jpa.mapper.TtrnRiskRemarksMapper;
import com.maan.insurance.jpa.repository.facultative.FacultativeCustomRepository;
import com.maan.insurance.jpa.repository.facultative.TtrnFacSiRepository;
import com.maan.insurance.jpa.repository.facultative.TtrnLossDetailsRepository;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnCrestazoneDetails;
import com.maan.insurance.model.entity.TtrnRiskRemarks;
import com.maan.insurance.model.repository.TtrnBonusRepository;
import com.maan.insurance.model.repository.TtrnCrestazoneDetailsRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnMndInstallmentsRepository;
import com.maan.insurance.model.repository.TtrnRiskRemarksRepository;
import com.maan.insurance.model.req.facultative.CoverSNoReq;
import com.maan.insurance.model.req.facultative.DeleteMaintableReq;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.GetInsurarerDetailsReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.facultative.InserLossRecordReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.facultative.InsertInsurarerTableInsertReq;
import com.maan.insurance.model.req.facultative.InsertXolCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InstalMentPremiumReq;
import com.maan.insurance.model.req.facultative.LossDetailsReq;
import com.maan.insurance.model.req.facultative.MoveBonusReq;
import com.maan.insurance.model.req.facultative.RetroDetails;
import com.maan.insurance.model.req.facultative.SecondPageInsertReq;
import com.maan.insurance.model.req.facultative.ShowSecondPagedataReq;
import com.maan.insurance.model.req.facultative.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;
import com.maan.insurance.model.req.facultative.XolcoverSNoReq;
import com.maan.insurance.model.req.nonproportionality.BonusReq;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.req.nonproportionality.RemarksReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.facultative.CommonResponse;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes1;
import com.maan.insurance.model.res.facultative.GetCommonValueRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes1;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes1;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes1;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes1;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes1;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes1;
import com.maan.insurance.model.res.facultative.InstalmentListRes;
import com.maan.insurance.model.res.facultative.RetroDupListRes;
import com.maan.insurance.model.res.facultative.RetroListRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes1;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes1;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes1;
import com.maan.insurance.model.res.facultative.UwList;
import com.maan.insurance.model.res.nonproportionality.BonusRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;
import com.maan.insurance.model.res.retro.GetRemarksDetailsRes1;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.facultative.Validation;

@Component
public class FacultativeJpaServiceImpl {
	private Logger log = LogManager.getLogger(FacultativeJpaServiceImpl.class);
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	FacultativeCustomRepository facultativeCustomRepository;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private TtrnRiskRemarksMapper ttrnRiskRemarksMapper;
	
	@Autowired
	private TtrnRiskRemarksRepository ttrnRiskRemarksRepository;
	
	@Autowired
	private TtrnFacSiMapper ttrnFacSiMapper;
	
	@Autowired
	private TtrnFacSiRepository ttrnFacSiRepository;
	
	@Autowired
	private TtrnBonusMapper ttrnBonusMapper;
	
	@Autowired
	private TtrnBonusRepository ttrnBonusRepository;
	
	@Autowired
	private TtrnCrestazoneDetailsMapper ttrnCrestazoneDetailsMapper;
	
	@Autowired
	private TtrnCrestazoneDetailsRepository ttrnCrestazoneDetailsRepository;
	
	@Autowired
	private TtrnInsurerDetailsMapper ttrnInsurerDetailsMapper;
	
	@Autowired
	private TtrnInsurerDetailsRepository ttrnInsurerDetailsRepository;
	
	@Autowired
	private TtrnMndInstallmentsMapper ttrnMndInstallmentsMapper;
	
	@Autowired
	private TtrnMndInstallmentsRepository ttrnMndInstallmentsRepository;
	
	@Autowired
	private TtrnLossDetailsMapper ttrnLossDetailsMapper;
	
	@Autowired
	private TtrnLossDetailsRepository ttrnLossDetailsRepository;
	
	
	private String formatDate(Object input) {
		return new SimpleDateFormat("dd/MM/yyyy").format(input).toString();
	}

	public GetCommonValueRes getShortname(String branchCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		String Short = "";
		try {
			// query -- GET_SHORT_NAME
			Short = facultativeCustomRepository.getShortName(branchCode);
			Short = Short == null ? "" : Short;
			response.setCommonResponse(Short);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public CommonResponse UpadateUWShare(String shSd, String proposalNo) {
		CommonResponse response = new CommonResponse();
		try {
			// query -- UPDATE_UW_SHARE
			facultativeCustomRepository.updateUWShare(shSd, proposalNo);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonResponse insertXolCoverDeductableDetails(InsertXolCoverDeductableDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		String amendId = "";
		try {
			// Query -- DELETE_COVERDEDUCTABLE_DETAILS
			facultativeCustomRepository.deleteCoverdeductableDetails(beanObj.getProposalNo(), "0");

			// Query -- TTRN_FAC_SI_AMEND_ID
			amendId = facultativeCustomRepository.ttrnFacSiAmendId(beanObj.getProposalNo());
			amendId = amendId == null ? "" : amendId;

			if (StringUtils.isNotBlank(beanObj.getType())) {
				for (int i = 0; i < beanObj.getXolcoverSNoReq().size(); i++) {

					XolcoverSNoReq req = beanObj.getXolcoverSNoReq().get(i);
					TtrnFacSi entity = ttrnFacSiMapper.toCustomXolEntity(beanObj, req, i);
					entity.setAmendId(ttrnFacSiMapper.formatBigDecimal(amendId));
					// query -- INSERT_XOLCOVERDEDUCTABLE_DETAILS
					ttrnFacSiRepository.save(entity);
				}
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
	
	public CommonResponse insertCoverDeductableDetails(InsertCoverDeductableDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		String amendId = "";
		try {
			// Query -- DELETE_COVERDEDUCTABLE_DETAILS
			facultativeCustomRepository.deleteCoverdeductableDetails(beanObj.getProposalNo(), "0");

			// Query -- TTRN_FAC_SI_AMEND_ID
			amendId = facultativeCustomRepository.ttrnFacSiAmendId(beanObj.getProposalNo());
			amendId = amendId == null ? "" : amendId;
			if (StringUtils.isNotBlank(beanObj.getType()) && "1".equalsIgnoreCase(beanObj.getType())) {
				for (int i = 0; i < beanObj.getCoverSNoReq().size(); i++) {
					CoverSNoReq req = beanObj.getCoverSNoReq().get(i);
					TtrnFacSi entity = ttrnFacSiMapper.toCustomEntity(beanObj, req, i);
					entity.setAmendId(ttrnFacSiMapper.formatBigDecimal(amendId));
					// query-- INSERT_COVERDEDUCTABLE_DETAILS
					ttrnFacSiRepository.save(entity);
				}
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
	
	public GetRetroContractDetailsListRes getRetroContractDetailsList(GetRetroContractDetailsListReq beanObj,
			String flag) {
		GetRetroContractDetailsListRes response = new GetRetroContractDetailsListRes();
		List<GetRetroContractDetailsListRes1> resList = new ArrayList<GetRetroContractDetailsListRes1>();
		List<Tuple> list = new ArrayList<>();
		int flag1 = Integer.valueOf(flag);
		try {
			if (StringUtils.isEmpty(beanObj.getYear()) && flag1 == 1) {
				// query -- fac.select.uwYear
				list = facultativeCustomRepository.facSelectUwYear(beanObj.getProductId(),
						ttrnFacSiMapper.formatDate(beanObj.getIncepDate()), beanObj.getBranchCode());
			} else if (StringUtils.isNotEmpty(beanObj.getYear()) && flag1 == 2) {
				if (StringUtils.isNotBlank(beanObj.getRetroType()) && "TR".equals(beanObj.getRetroType())
						&& "4".equals(beanObj.getProductId())) {
					// query -- fac.select.retroContDetTR
					facultativeCustomRepository.facSelectRetroContDetTR(beanObj.getProductId(), beanObj.getYear(),
							ttrnFacSiMapper.formatDate(beanObj.getIncepDate()), beanObj.getBranchCode());
				} else {
					// query -- fac.select.retroContDet
					list = facultativeCustomRepository.facSelectRetroContDet(beanObj.getProductId(),
							(StringUtils.isBlank(beanObj.getRetroType()) ? "" : beanObj.getRetroType()),
							beanObj.getYear(), ttrnFacSiMapper.formatDate(beanObj.getIncepDate()),
							beanObj.getBranchCode());
				}
			} else if (StringUtils.isNotEmpty(beanObj.getYear()) && flag1 == 3) {
				// query -- FAC_SELECT_RETRO_DUP_CONTRACT";
				list = facultativeCustomRepository.facSelectRetroDupContract(beanObj.getProductId(), "TR",
						beanObj.getYear(), ttrnFacSiMapper.formatDate(beanObj.getIncepDate()), beanObj.getBranchCode());
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					GetRetroContractDetailsListRes1 res = new GetRetroContractDetailsListRes1();
					Tuple insMap = list.get(i);
					res.setCONTDET1(insMap.get("CONTDET1") == null ? "" : insMap.get("CONTDET1").toString());
					res.setCONTDET2(insMap.get("CONTDET2") == null ? "" : insMap.get("CONTDET2").toString());
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
	
	public GetLowClaimBonusListRes getLowClaimBonusList(String proposalNo, String branchCode, String acqBonus) {
		GetLowClaimBonusListRes response = new GetLowClaimBonusListRes();
		List<TtrnBonus> result = new ArrayList<>();
		GetLowClaimBonusListRes1 res = new GetLowClaimBonusListRes1();
		List<GetLowClaimBonusListRes1> resList = new ArrayList<GetLowClaimBonusListRes1>();
		try {
			// query = "BONUS_MAIN_SELECT";
			result = facultativeCustomRepository.bonusMainSelect(proposalNo, branchCode, acqBonus);
			List<BonusRes> bonusResList = new ArrayList<BonusRes>();
			for (int i = 0; i < result.size(); i++) {
				BonusRes bonusRes = new BonusRes();
				TtrnBonus tempMap = result.get(i);
				res.setBonusTypeId(tempMap.getLcbType() == null ? "" : tempMap.getLcbType().toString());
				bonusRes.setBonusSNo(tempMap.getLcbId() == null ? "" : tempMap.getLcbId().toString());
				bonusRes.setBonusFrom(tempMap.getLcbFrom() == null ? "" : fm.formatter(tempMap.getLcbFrom().toString()));
				bonusRes.setBonusTo(tempMap.getLcbTo() == null ? "" : fm.formatter(tempMap.getLcbTo().toString()));
				bonusRes.setBonusLowClaim(tempMap.getLcbPercentage() == null ? "" : fm.formatter(tempMap.getLcbPercentage().toString()));
				bonusResList.add(bonusRes);
				res.setBonusRes(bonusResList);
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
	
	public GetRemarksDetailsRes getRemarksDetails(String proposalNo) {
		GetRemarksDetailsRes response = new GetRemarksDetailsRes();
		GetRemarksDetailsRes1 res1 = new GetRemarksDetailsRes1();
		List<RemarksRes> remarksres = new ArrayList<RemarksRes>();
		try {
			List<Tuple> result = new ArrayList<>();
			// query -- GET_REMARKS_DETAILS
			result = facultativeCustomRepository.getRemarksDetails(proposalNo, "0");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					RemarksRes res = new RemarksRes();
					Tuple insMap = result.get(i);
					res.setDescription(insMap.get("RSK_DESCRIPTION") == null ? "Remarks" : insMap.get("RSK_DESCRIPTION").toString());
					res.setRemark1(insMap.get("RSK_REMARK1") == null ? " " : insMap.get("RSK_REMARK1").toString());
					res.setRemark2(insMap.get("RSK_REMARK2") == null ? "" : insMap.get("RSK_REMARK2").toString());
					res.setRemarkSNo(Integer.toString(result.size()));
					remarksres.add(res);
				}

			}
			res1.setRemarkCount(String.valueOf(result.size()));
			res1.setRemarksRes(remarksres);
			response.setCommonResponse(remarksres);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonResponse instalMentPremium(InstalMentPremiumReq beanObj) {
		CommonResponse response = new CommonResponse();
		String endtNo = "";
		try {
			// query -- SELECT_AMEND_ID
			endtNo = facultativeCustomRepository.selectAmendId(beanObj.getProposalNo());
			endtNo = endtNo == null ? "" : endtNo;

			// query -- delete.TTRN_MND_INSTALLMENTS
			facultativeCustomRepository.deleteTtrnMndInstakkments(beanObj.getProposalNo(), endtNo);

			for (int i = 0; i < Integer.parseInt(beanObj.getNoOfInst()); i++) {
				InstalmentperiodReq req = beanObj.getInstalmentDetails().get(i);

				// Query -- risk.insert.instalPrem
				TtrnMndInstallments entity = ttrnMndInstallmentsMapper.toCustomEntity(beanObj, req);
				entity.setInstallmentNo(ttrnMndInstallmentsMapper.formatBigDecimal(String.valueOf(i + 1)));
				entity.setEndorsementNo(ttrnMndInstallmentsMapper.formatBigDecimal(endtNo));
				ttrnMndInstallmentsRepository.save(entity);

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
	
	public CommonResponse insertInsurarerTableInsert(InsertInsurarerTableInsertReq beanObj) {
		CommonResponse response = new CommonResponse();
		String endtNo = "";
		try {
			final int LoopCount = beanObj.getNoInsurer() == "" ? 0 : Integer.parseInt(beanObj.getNoInsurer());
			// query -- SELECT_AMEND_ID
			endtNo = facultativeCustomRepository.selectAmendId(beanObj.getProposalNo());
			endtNo = endtNo == null ? "" : endtNo;

			// query -- delete.facul.data
			facultativeCustomRepository.deleteFaculData(beanObj.getProposalNo(), endtNo);
			if (LoopCount == 0) {
				beanObj.setRetper("100");
			}
			// query -- fac.insert.insDetails
			TtrnInsurerDetails entity = ttrnInsurerDetailsMapper.toEntity(beanObj);
			entity.setEndorsementNo(ttrnInsurerDetailsMapper.formatBigDecimal(endtNo));
			ttrnInsurerDetailsRepository.save(entity);

			// query -- fac.insert.insDetails
			TtrnInsurerDetails entity1 = ttrnInsurerDetailsMapper.toCustomEntity(beanObj);
			entity1.setEndorsementNo(ttrnInsurerDetailsMapper.formatBigDecimal(endtNo));
			ttrnInsurerDetailsRepository.save(entity1);

			int j = 2;
			for (int i = 0; i < LoopCount; i++) {
				RetroDetails req = beanObj.getRetroDetails().get(i);

				// query -- fac.insert.insDetails
				TtrnInsurerDetails entity2 = ttrnInsurerDetailsMapper.toCustomEntity1(beanObj, req);
				entity2.setInsurerNo(ttrnInsurerDetailsMapper.formatBigDecimal(String.valueOf(j)));
				;
				entity2.setEndorsementNo(ttrnInsurerDetailsMapper.formatBigDecimal(endtNo));
				ttrnInsurerDetailsRepository.save(entity1);

				if (i > 0) {
					if (StringUtils.isNotBlank(beanObj.getContractNo()) && !"0".equals(beanObj.getContractNo())
							&& "SR".equalsIgnoreCase(req.getRetroTypeValList())) {
						// query -- fac.update.fac.contractNo
						facultativeCustomRepository.facUpdateFacContractNo(beanObj.getContractNo(),
								req.getCedingCompanyValList());
					}
				}
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
	
	public CommonResponse insertBonusDetails(InsertBonusDetailsReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
			if (!"LCB".equalsIgnoreCase(beanObj.getAcqBonus())) {
				insetNOClaimBonusMainTable(beanObj);
			}
			// query -- UPDATE_CONTRACT_DETAILS
			facultativeCustomRepository.updateContractDetails(beanObj);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public void insetNOClaimBonusMainTable(InsertBonusDetailsReq bean) {
		try {
			if (StringUtils.isBlank(bean.getEndorsmentno())) {
				bean.setEndorsmentno("0");
			}
			DeleteMaintableReq req = new DeleteMaintableReq();
			req.setAcqBonus(bean.getAcqBonus());
			req.setBranchCode(bean.getBranchCode());
			req.setEndorsmentno(bean.getEndorsmentno());
			req.setProposalNo(bean.getProposalNo());
			deleteMaintable(req);

			// query -- BONUS_MAIN_INSERT
			TtrnBonus entity = ttrnBonusMapper.toBonusCustomEntity(bean);
			ttrnBonusRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CommonResponse insertCrestaMaintable(InsertCrestaMaintableReq bean) {
		CommonResponse response = new CommonResponse();
		try {
			int count = getCrestaCount(bean.getEndorsmentno(), bean.getProposalNo(), bean.getBranchCode());
			if (count <= 0) {
				TtrnCrestazoneDetails entity = ttrnCrestazoneDetailsMapper.toEntity(bean);
				// query -- MOVE_TO_CRESTA_MAIN_TABLE
				ttrnCrestazoneDetailsRepository.save(entity);
			}
			// query -- CREATA_CONTRACT_UPDATE
			facultativeCustomRepository.creataContractUpdate(bean.getContractNo(), bean.getProposalNo(),
					StringUtils.isEmpty(bean.getEndorsmentno()) ? "0" : bean.getEndorsmentno(), bean.getBranchCode());
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public int getCrestaCount(String endorsmentno, String proposalNo, String branchCode) {
		int count = 0;
		try {
			// query -- GET_CRESTA_DETAIL_COUNT
			String output = facultativeCustomRepository.getCrestaDetailCount(proposalNo,
					StringUtils.isEmpty(endorsmentno) ? "0" : endorsmentno, branchCode);
			count = output == null ? 0 : Integer.parseInt(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	public GetInsurarerDetailsRes getInsurarerDetails(GetInsurarerDetailsReq formObj) {
		GetInsurarerDetailsRes response = new GetInsurarerDetailsRes();
		List<GetInsurarerDetailsRes1> resList = new ArrayList<GetInsurarerDetailsRes1>();
		int noofInsurar = 0;
		List<UwList> listUw = new ArrayList<UwList>();
		try {
			List<Tuple> retroCedList = new ArrayList<>();
			if (StringUtils.isNotBlank(formObj.getNoInsurer())) {
				noofInsurar = Integer.parseInt(formObj.getNoInsurer());
				noofInsurar = noofInsurar + 1;
			}
			List<Tuple> insDetailsList = null;
			if ("true".equalsIgnoreCase(formObj.getView())) {
				// query -- fac.select.viewInsDetails
				insDetailsList = facultativeCustomRepository.facSelectViewInsDetails(formObj.getAmendId(),
						formObj.getProposalNo(), ttrnBonusMapper.formatDouble(String.valueOf(noofInsurar)));

			} else {
				// query -- fac.select.insDetails
				insDetailsList = facultativeCustomRepository.facSelectInsDetails(formObj.getProposalNo(),
						ttrnBonusMapper.formatDouble(String.valueOf(noofInsurar)));
			}
			if (insDetailsList != null && insDetailsList.size() > 0) {
				for (int j = 0; j < insDetailsList.size(); j++) {
					GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
					Tuple insDetailsMap = insDetailsList.get(j);
					if ("R".equalsIgnoreCase(insDetailsMap.get("TYPE").toString())) {
						res.setRetper(insDetailsMap.get("RETRO_PER") == null ? ""
								: insDetailsMap.get("RETRO_PER").toString());
						if (StringUtils.isNotBlank(formObj.getNoInsurer())) {
							if ("0".equalsIgnoreCase(res.getRetper())) {
								for (int z = 0; z < Integer.valueOf(formObj.getNoInsurer()); z++) {
									res.setRetroTypeValList("");
									res.setUwYearValList("");
									res.setCedingCompanyValList("");
									res.setRetroPercentage("");
									resList.add(res);

								}
							}
						}
					} else {
						if (j <= noofInsurar) {
							if (1 == j) {
								String uwYear = "";
								if (formObj.getYear().equalsIgnoreCase(insDetailsMap.get("UW_YEAR").toString())) {
									res.setRetroDupYerar(insDetailsMap.get("UW_YEAR") == null ? ""
											: insDetailsMap.get("UW_YEAR").toString());
									uwYear = insDetailsMap.get("UW_YEAR") == null ? ""
											: insDetailsMap.get("UW_YEAR").toString();
								} else {
									res.setRetroDupYerar(formObj.getYear());
									uwYear = formObj.getYear();
								}
								res.setRetroDupType(insDetailsMap.get("RETRO_TYPE") == null ? ""
										: insDetailsMap.get("RETRO_TYPE").toString());
								String retroType = insDetailsMap.get("RETRO_TYPE") == null ? ""
										: insDetailsMap.get("RETRO_TYPE").toString();
								// query -- FAC_SELECT_RETRO_DUP_CONTRACT
								List<Tuple> list = facultativeCustomRepository.facSelectRetroDupContract("4",
										(StringUtils.isBlank(retroType) ? "" : retroType), uwYear,
										ttrnBonusMapper.formatDate(formObj.getInceptionDate()),
										formObj.getBranchCode());

								for (int k = 0; k < list.size(); k++) {
									Tuple map = list.get(k);
									res.setRetroDupContract(
											map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
								}
							} else {
								res.setRetroTypeValList(insDetailsMap.get("RETRO_TYPE") == null ? ""
										: insDetailsMap.get("RETRO_TYPE").toString());
								res.setUwYearValList(insDetailsMap.get("UW_YEAR") == null ? ""
										: insDetailsMap.get("UW_YEAR").toString());
								res.setCedingCompanyValList(insDetailsMap.get("CONTRACTNO") == null ? ""
										: insDetailsMap.get("CONTRACTNO").toString());
								res.setRetroPercentage(insDetailsMap.get("RETRO_PER") == null ? ""
										: insDetailsMap.get("RETRO_PER").toString());

								// query -- fac.select.uwYear
								List<Tuple> uwList = facultativeCustomRepository.facSelectUwYear("4",
										ttrnBonusMapper.formatDate(formObj.getInceptionDate()),
										formObj.getBranchCode());
								if (uwList.size() > 0 && uwList != null) {
									for (int i = 0; i < uwList.size(); i++) {
										UwList uw = new UwList();
										Tuple uwList1 = uwList.get(i);
										uw.setContdet1(uwList1.get("CONTDET1") == null ? ""
												: uwList1.get("CONTDET1").toString());
										uw.setContdet2(uwList1.get("CONTDET2") == null ? ""
												: uwList1.get("CONTDET2").toString());
										listUw.add(uw);
									}
									res.setUwList(listUw);
								}
								String retroType = insDetailsMap.get("RETRO_TYPE") == null ? ""
										: insDetailsMap.get("RETRO_TYPE").toString();

								String uwYear = insDetailsMap.get("UW_YEAR") == null ? ""
										: insDetailsMap.get("UW_YEAR").toString();

								if (StringUtils.isNotBlank(retroType) && "TR".equals(retroType)) {
									// query -- fac.select.retroContDetTR
									retroCedList = facultativeCustomRepository.facSelectRetroContDetTR("4", uwYear,
											ttrnBonusMapper.formatDate(formObj.getInceptionDate()),
											formObj.getBranchCode());
								} else {
									// query -- fac.select.retroContDet
									retroCedList = facultativeCustomRepository.facSelectRetroContDet("4",
											(StringUtils.isBlank(retroType) ? "" : retroType), uwYear,
											ttrnBonusMapper.formatDate(formObj.getInceptionDate()),
											formObj.getBranchCode());
								}
							}
							if (1 == j) {

								if (retroCedList.size() > 0 && retroCedList != null) {
									for (int i = 0; i < retroCedList.size(); i++) {
										UwList uw = new UwList();
										Tuple retroCedList1 = retroCedList.get(i);
										uw.setContdet1(retroCedList1.get("CONTDET1") == null ? ""
												: retroCedList1.get("CONTDET1").toString());
										uw.setContdet2(retroCedList1.get("CONTDET2") == null ? ""
												: retroCedList1.get("CONTDET2").toString());
										listUw.add(uw);
									}
									res.setRetroDupList(listUw);
								}
							} else {
								if (retroCedList.size() > 0 && retroCedList != null) {
									for (int i = 0; i < retroCedList.size(); i++) {
										UwList uw = new UwList();
										Tuple retroCedList1 = retroCedList.get(i);
										;
										uw.setContdet1(retroCedList1.get("CONTDET1") == null ? ""
												: retroCedList1.get("CONTDET1").toString());
										uw.setContdet2(retroCedList1.get("CONTDET2") == null ? ""
												: retroCedList1.get("CONTDET2").toString());
										listUw.add(uw);
									}
									res.setRetrolList(listUw);
								}
							}
						}
					}
					if (StringUtils.isNotBlank(formObj.getNoInsurer())) {
						int num = noofInsurar - (insDetailsList.size() - 1);
						for (int z = 0; z < num; z++) {
							res.setUwYearValList("");
							res.setCedingCompanyValList("");
							res.setRetroPercentage("");

						}
					}
					resList.add(res);
				}
			} else if (StringUtils.isNotBlank(formObj.getNoInsurer())) {
				for (int z = 0; z < Integer.valueOf(formObj.getNoInsurer()) + 1; z++) {
					GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
					res.setRetroTypeValList("");
					if (0 == z) {
						// query -- fac.select.uwYear
						List<Tuple> uwList = facultativeCustomRepository.facSelectUwYear("4",
								ttrnBonusMapper.formatDate(formObj.getInceptionDate()), formObj.getBranchCode());
						if (uwList.size() > 0 && uwList != null) {
							for (int i = 0; i < uwList.size(); i++) {
								UwList uw = new UwList();
								Tuple uwList1 = uwList.get(i);
								uw.setContdet1(
										uwList1.get("CONTDET1") == null ? "" : uwList1.get("CONTDET1").toString());
								uw.setContdet2(
										uwList1.get("CONTDET2") == null ? "" : uwList1.get("CONTDET2").toString());
								listUw.add(uw);
							}
							res.setUwList(listUw);
						}
						res.setRetroDupYerar(formObj.getYear());
						res.setRetroDupType("TR");
						// query -- FAC_SELECT_RETRO_DUP_CONTRACT
						List<Tuple> list = facultativeCustomRepository.facSelectRetroDupContract("4", "TR",
								formObj.getYear(), ttrnBonusMapper.formatDate(formObj.getInceptionDate()),
								formObj.getBranchCode());
						for (int k = 0; k < list.size(); k++) {
							Tuple map = list.get(k);
							res.setRetroDupContract(
									map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
						}
						res.setRetroDupMode("Duplicate");
					} else {
						res.setRetroPercentage("");
						res.setUwYearValList("");
					}
					res.setCedingCompanyValList("");
					resList.add(res);
				}
			}
			if (StringUtils.isNotBlank(formObj.getNoInsurer()) && "0".equalsIgnoreCase(formObj.getNoInsurer())) {
				GetInsurarerDetailsRes1 res = new GetInsurarerDetailsRes1();
				res.setRetroDupYerar(formObj.getYear());
				res.setRetroDupType("TR");
				// query -- FAC_SELECT_RETRO_DUP_CONTRACT
				List<Tuple> list = facultativeCustomRepository.facSelectRetroDupContract("4", "TR", formObj.getYear(),
						ttrnBonusMapper.formatDate(formObj.getInceptionDate()), formObj.getBranchCode());
				for (int k = 0; k < list.size(); k++) {
					GetInsurarerDetailsRes1 res1 = new GetInsurarerDetailsRes1();
					Tuple map = list.get(k);
					res1.setRetroDupContract(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
					resList.add(res1);
				}
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
	
	public ShowSecondpageEditItemsRes ShowSecondPageEditItems(ShowSecondpageEditItemsReq req) {
		ShowSecondpageEditItemsRes response = new ShowSecondpageEditItemsRes();
		ShowSecondpageEditItemsRes1 formObj = new ShowSecondpageEditItemsRes1();
		try{
			String[] args=new String[4];
			args[1]=req.getProposalNo();
			args[2]=req.getProposalNo();
			args[3]=req.getProposalNo();
			args[0]=req.getBranchCode();
			// query  -- fac.select.secondPageDet
			List<Tuple> list = facultativeCustomRepository.facSelectSecondPageDet(req.getProposalNo(), req.getBranchCode());
			if(list!=null && list.size()>0){
				Tuple tempMap = list.get(0);
				formObj.setRiskGrade(tempMap.get("RISK_GRADE")==null?"":tempMap.get("RISK_GRADE").toString());
				formObj.setOccCode(tempMap.get("OCCUPATION_CODE")==null?"":tempMap.get("OCCUPATION_CODE").toString());
				formObj.setRiskDetail(tempMap.get("RISK_DETAILS")==null?"":tempMap.get("RISK_DETAILS").toString());
				formObj.setFireProt(StringUtils.isBlank(tempMap.get("FIRE_PORT")==null?"":tempMap.get("FIRE_PORT").toString())?"N":tempMap.get("FIRE_PORT").toString());
				formObj.setScope(tempMap.get("SCOPE")==null?"":tempMap.get("SCOPE").toString());
				formObj.setMbind(StringUtils.isBlank(tempMap.get("MB_IND")==null?"":tempMap.get("MB_IND").toString())?"N":tempMap.get("MB_IND").toString());
				formObj.setCategoryZone(tempMap.get("CATEGORY_ZONE")==null?"":tempMap.get("CATEGORY_ZONE").toString());
				formObj.setEqwsInd(tempMap.get("EARTHQUAKE_WS_IND")==null?"":tempMap.get("EARTHQUAKE_WS_IND").toString());
				formObj.setWsThreat(tempMap.get("WS_THREAT_IND")==null?"":tempMap.get("WS_THREAT_IND").toString());
				formObj.setEqThreat(tempMap.get("EQ_THREAT")==null?"":tempMap.get("EQ_THREAT").toString());
				formObj.setCommn(StringUtils.isBlank(req.getContractNo()==null?"":req.getContractNo().toString())&&(StringUtils.isBlank(tempMap.get("RSK_COMM")==null?"":tempMap.get("RSK_COMM").toString()))?"":tempMap.get("RSK_COMM").toString());
				formObj.setBrokerage(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_BROKERAGE")==null?"":tempMap.get("RSK_BROKERAGE").toString()))?"":tempMap.get("RSK_BROKERAGE").toString());
				formObj.setAcqBonus(tempMap.get("RSK_BONUS_ID")==null?"":tempMap.get("RSK_BONUS_ID").toString());
				formObj.setAcqBonusPercentage(dropDowmImpl.exchRateFormat(tempMap.get("RSK_NOCLAIMBONUS_PRCENT")==null?"":tempMap.get("RSK_NOCLAIMBONUS_PRCENT").toString()));
				formObj.setTax(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_TAX")==null?"":tempMap.get("RSK_TAX").toString()))?"":tempMap.get("RSK_TAX").toString());
				formObj.setLossRecord(StringUtils.isBlank(tempMap.get("RSK_LOSS_RECORD")==null?"":tempMap.get("RSK_LOSS_RECORD").toString())?"N":tempMap.get("RSK_LOSS_RECORD").toString());
				formObj.setDgmsApproval(tempMap.get("RSK_DGM_APPROVAL")==null?"":tempMap.get("RSK_DGM_APPROVAL").toString());
				formObj.setUnderwriterCode(tempMap.get("RSK_UNDERWRITTER_CODE")==null?"":tempMap.get("RSK_UNDERWRITTER_CODE").toString());
				formObj.setUwRecommendation(tempMap.get("RSK_UW_RECOMMENDATION")==null?"":tempMap.get("RSK_UW_RECOMMENDATION").toString());
				formObj.setRemarks(tempMap.get("RSK_REMARKS")==null?"":tempMap.get("RSK_REMARKS").toString());
				formObj.setOthAccep(tempMap.get("RSK_OTH_ACCEP")==null?"":tempMap.get("RSK_OTH_ACCEP").toString());
				formObj.setReftoHO(StringUtils.isBlank(tempMap.get("RSK_REF_TO_HO")==null?"":tempMap.get("RSK_REF_TO_HO").toString())?"N":tempMap.get("RSK_REF_TO_HO").toString());
				formObj.setAcqCost(StringUtils.isBlank(req.getContractNo())&&(StringUtils.isBlank(tempMap.get("RSK_ACQUISTION_COST_OC")==null?"":tempMap.get("RSK_ACQUISTION_COST_OC").toString()))?"":tempMap.get("RSK_ACQUISTION_COST_OC").toString());
				formObj.setAccusd(tempMap.get("RSK_ACQUISTION_COST_DC")==null?"":tempMap.get("RSK_ACQUISTION_COST_DC").toString());
				formObj.setCuRsn(tempMap.get("CU_RSN")==null?"":tempMap.get("CU_RSN").toString());
				formObj.setOthercost(tempMap.get("RSK_OTHER_COST")==null?"":tempMap.get("RSK_OTHER_COST").toString());
				formObj.setAcqCostPer(dropDowmImpl.GetACC(Double.parseDouble(StringUtils.isBlank(formObj.getCommn())?"0":formObj.getCommn())+Double.parseDouble(StringUtils.isBlank(formObj.getBrokerage())?"0":formObj.getBrokerage())+Double.parseDouble(StringUtils.isBlank(formObj.getTax())?"0":formObj.getTax())+Double.parseDouble(StringUtils.isBlank(formObj.getOthercost())?"0":formObj.getOthercost()))+"");
				String mLop = tempMap.get("M_LOP")==null?"":tempMap.get("M_LOP").toString();
				formObj.setMlopYN(StringUtils.isBlank(mLop)?"N":mLop);
				String aLop = tempMap.get("A_LOP")==null?"":tempMap.get("A_LOP").toString();
				formObj.setMlopYN(StringUtils.isBlank(aLop)?"N":aLop);
				formObj.setAlopYN(tempMap.get("A_LOP")==null?"N":tempMap.get("A_LOP").toString());
				formObj.setLeaderUnderwritercountry(tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY")==null?"":tempMap.get("RSK_LEAD_UNDERWRITER_COUNTRY").toString());
				formObj.setLeaderUnderwriter(tempMap.get("RSK_LEAD_UW")==null ? "0" : tempMap.get("RSK_LEAD_UW").toString());
				formObj.setLeaderUnderwritershare(tempMap.get("RSK_LEAD_UW_SHARE")==null ? "0" : tempMap.get("RSK_LEAD_UW_SHARE").toString());
				formObj.setExclusion(tempMap.get("RSK_EXCLUSION")==null?"":tempMap.get("RSK_EXCLUSION").toString());
				formObj.setCrestaStatus(tempMap.get("RSK_CREASTA_STATUS")==null?"":tempMap.get("RSK_CREASTA_STATUS").toString());
				
				//Query -- premium.select.CEASE_STATUS
				String output = facultativeCustomRepository.premiumSelectCeaseStatus(req.getProposalNo());
				formObj.setCeaseStatus(output==null?"":output);
				
			}
			// query -- risk.select.getInstalmentData
			List<Tuple> instalmentList = facultativeCustomRepository.riskSelectGetInstalmentData(req.getProposalNo(),
					"0");

			List<InstalmentListRes> instalResList = new ArrayList<InstalmentListRes>();
			if (instalmentList != null) {

				for (int k = 0; k < instalmentList.size(); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					Tuple insMap = instalmentList.get(k);
					instalRes.setInstalmentDateList(insMap.get("INSTALLMENT_DATE") == null ? ""
							: formatDate(insMap.get("INSTALLMENT_DATE").toString()));
					instalRes.setPaymentDueDays(
							insMap.get("PAYEMENT_DUE_DAY") == null ? "" : insMap.get("PAYEMENT_DUE_DAY").toString());
					instalRes.setTransactionList(
							insMap.get("TRANSACTION_NO") == null ? "" : insMap.get("TRANSACTION_NO").toString());
					instalResList.add(instalRes);
				}

				for (int k = 0; k < Integer.parseInt(req.getNoOfInst()); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					instalRes.setPaymentDueDays(req.getReceiptofPayment());
					instalResList.add(instalRes);
				}
				formObj.setInstalmentList(instalResList);
			} else {
				for (int k = 0; k < Integer.parseInt(req.getNoOfInst()); k++) {
					InstalmentListRes instalRes = new InstalmentListRes();
					instalRes.setPaymentDueDays(req.getReceiptofPayment());
					instalResList.add(instalRes);

				}
				formObj.setInstalmentList(instalResList);
			}

			response.setCommonResponse(formObj);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetCommonValueRes getRetroContractDetails(GetRetroContractDetailsReq beanObj) {
		GetCommonValueRes response = new GetCommonValueRes();
		String Cedingco = "";
		try {
			List<Tuple> list = null;
			if ("uwYear".equalsIgnoreCase(beanObj.getDropDown())) {
				// query -- fac.select.uwYear
				list = facultativeCustomRepository.facSelectUwYear(beanObj.getProductId(),
						ttrnBonusMapper.formatDate(beanObj.getInceptionDate()), beanObj.getBranchCode());
				// beanObj.setUwYearList(list);
			} else if (StringUtils.isNotEmpty(beanObj.getYear())
					&& "Duplicate".equalsIgnoreCase(beanObj.getRetroDupMode())) {
				// query -- FAC_SELECT_RETRO_DUP_CONTRACT
				list = facultativeCustomRepository.facSelectRetroDupContract(beanObj.getProductId(),
						(StringUtils.isBlank(beanObj.getRetroType()) ? "" : beanObj.getRetroType()), beanObj.getYear(),
						ttrnBonusMapper.formatDate(beanObj.getInceptionDate()), beanObj.getBranchCode());
			} else {
				if (StringUtils.isNotBlank(beanObj.getRetroType()) && "TR".equals(beanObj.getRetroType())
						&& "4".equals(beanObj.getProductId())) {
					// query -- fac.select.retroContDetTR
					list = facultativeCustomRepository.facSelectRetroContDetTR(beanObj.getProductId(),
							beanObj.getYear(), ttrnBonusMapper.formatDate(beanObj.getInceptionDate()),
							beanObj.getBranchCode());
					// beanObj.setCedingCompanyList(list);
				} else {
					// query -- fac.select.retroContDet
					list = facultativeCustomRepository.facSelectRetroContDet(beanObj.getProductId(),
							(StringUtils.isBlank(beanObj.getRetroType()) ? "" : beanObj.getRetroType()),
							beanObj.getYear(), ttrnBonusMapper.formatDate(beanObj.getInceptionDate()),
							beanObj.getBranchCode());
					// beanObj.setCedingCompanyList(list);
				}
			}
			if (list != null && list.size() > 0) {
				Tuple resMap;
				for (int i = 0; i < list.size(); i++) {
					resMap = list.get(i);
					if (i == (list.size() - 1)) {
						Cedingco += resMap.get("CONTDET1").toString() + "~" + resMap.get("CONTDET2").toString();
					} else {
						Cedingco += resMap.get("CONTDET1").toString() + "~" + resMap.get("CONTDET2").toString() + "~";
					}
				}
			}
			response.setCommonResponse(Cedingco);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public CommonResponse inserLossRecord(InserLossRecordReq beanObj) {
		CommonResponse response = new CommonResponse();
		String endtNo = "";
		try {
			// query -- LOSS_DELETE
			facultativeCustomRepository.lossDelete(beanObj.getProposalNo(), "0");

			// query -- SELECT_AMEND_ID
			endtNo = facultativeCustomRepository.selectAmendId(beanObj.getProposalNo());
			endtNo = endtNo == null ? "" : endtNo;

			for (int i = 0; i < beanObj.getLossDetails().size(); i++) {
				LossDetailsReq req = beanObj.getLossDetails().get(i);
				if (StringUtils.isNotBlank(req.getLossYear())) {
					// query -- INSET_LOSS_REC
					TtrnLossDetails entity = ttrnLossDetailsMapper.toCustomEntity(beanObj, req);
					entity.setEndorsementNo(endtNo);
					ttrnLossDetailsRepository.save(entity);
				}
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
	
	public CommonResponse moveBonus(MoveBonusReq bean) {
		CommonResponse response = new CommonResponse();
		try {
			if (StringUtils.isBlank(bean.getEndorsmentno())) {
				bean.setEndorsmentno("0");
			}

			for (int i = 0; i < bean.getBonusReq().size(); i++) {
				BonusReq req = bean.getBonusReq().get(i);
				if (StringUtils.isNotBlank(req.getBonusFrom()) && StringUtils.isNotBlank(req.getBonusTo())
						&& StringUtils.isNotBlank(req.getBonusLowClaimBonus())) {
					// query -- BONUS_MAIN_INSERT
					TtrnBonus entity = ttrnBonusMapper.toCustomEntity(bean, req);
					ttrnBonusRepository.save(entity);
				}
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
	
	public GetLossDEtailsRes getLossDEtails(String proposalNo) {
		GetLossDEtailsRes response = new GetLossDEtailsRes();
		
		List<GetLossDEtailsRes1> resList = new ArrayList<GetLossDEtailsRes1>();
		List<Tuple> list = new ArrayList<>();
		try{
			// query -- GET_LOSS_DETIALS
			list = facultativeCustomRepository.getLossDetails(proposalNo, "0");
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					GetLossDEtailsRes1 res = new GetLossDEtailsRes1();
					Tuple insMap = list.get(i);
					res.setLossYear(insMap.get("YEAR")==null?"":insMap.get("YEAR").toString());
					res.setLossNo(insMap.get("LOSS_NO")==null?"":insMap.get("LOSS_NO").toString());
					res.setLossinsuredName(insMap.get("INSURED_NAME")==null?"":insMap.get("INSURED_NAME").toString());
					res.setLossInceptionDate(insMap.get("INCEPTION_DATE")==null?"":formatDate(insMap.get("INCEPTION_DATE")).toString());
					res.setLossExpiryDate(insMap.get("EXPIRYDATE")==null?"":formatDate(insMap.get("EXPIRYDATE")).toString());
					res.setLossDateOfLoss(insMap.get("DATE_OF_LOSS")==null?"":insMap.get("DATE_OF_LOSS").toString());
					res.setLossCauseOfLoss(insMap.get("CAUSE_OF_LOSS")==null?"":insMap.get("CAUSE_OF_LOSS").toString());
					res.setLossInsuredClaim(insMap.get("INSURED_CLAIM")==null?"":fm.formatter(insMap.get("INSURED_CLAIM").toString()));
					res.setLossPremium(insMap.get("PREMIUM")==null?"":dropDowmImpl.formatterpercentage(insMap.get("PREMIUM").toString()));
					res.setLossRatio(insMap.get("LOSS_RATIO")==null?"":fm.formatter(insMap.get("LOSS_RATIO").toString()));
					res.setLossLeader(insMap.get("LEADER")==null?"":insMap.get("LEADER").toString());
					res.setLossITIReShare(insMap.get("ITI_RE_SHARE")==null?"":fm.formatter(insMap.get("ITI_RE_SHARE").toString()));
					res.setLossCount(Integer.toString(list.size()));
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
	
	public CommonResponse deleteMaintable(DeleteMaintableReq bean) {
		CommonResponse response = new CommonResponse();
		String acqBonus = bean.getAcqBonus();
		String type = "";
		try {
			// query -- BONUS_PREVIOUS_TYPE_CHECK
			type = facultativeCustomRepository.bonusPreviousTypeCheck(bean.getProposalNo(), bean.getBranchCode(), "0");
			type = type == null ? "" : type;

			if (!type.equalsIgnoreCase(acqBonus)) {
				acqBonus = type;
			}
			if ("".equalsIgnoreCase(bean.getEndorsmentno())) {
				// query -- BONUS_MAIN_DELETE
				facultativeCustomRepository.bonusMainDelete(bean.getProposalNo(), bean.getBranchCode(), acqBonus, "0");
			} else {
				// query -- BONUS_MAIN_DELETE2
				facultativeCustomRepository.bonusMainDelete2(bean.getProposalNo(), bean.getEndorsmentno(),
						bean.getBranchCode(), acqBonus, "0");
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
	
	public CommonResponse updateSecondPage(UpdateSecondPageReq beanObj) {
		CommonResponse response = new CommonResponse();
		try{
			// query -- RISK_COM_UPDATE
			facultativeCustomRepository.riskComUpdate(beanObj);
			response.setMessage("Success");
			response.setIsError(false);
				}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
			}
		return response;
	}
	
	public GetCoverDeductableDetailsRes getCoverDeductableDetails(String proposalNo, String branchCode,
			String productId) {
		GetCoverDeductableDetailsRes response = new GetCoverDeductableDetailsRes();

		List<GetCoverDeductableDetailsRes1> resList = new ArrayList<GetCoverDeductableDetailsRes1>();
		try {
			List<Tuple> result = new ArrayList<>();
			// query -- GET_COVER_DEDUCTABLE_DETAILS
			result = facultativeCustomRepository.getCoverDeductableDetails(proposalNo, "0");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					GetCoverDeductableDetailsRes1 res = new GetCoverDeductableDetailsRes1();
					Tuple insMap = result.get(i);
					res.setCoverdepartId(insMap.get("RSK_CLASS") == null ? "" : insMap.get("RSK_CLASS").toString());
					res.setCoverageDays(
							insMap.get("RSK_COVERAGE_DAYS") == null ? "" : insMap.get("RSK_COVERAGE_DAYS").toString());
					res.setCoverLimitOC(insMap.get("RSK_COVERLIMIT_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_COVERLIMIT_OC").toString()));
					res.setCoverRemark(
							insMap.get("RSK_COVER_REMARKS") == null ? "" : insMap.get("RSK_COVER_REMARKS").toString());
					res.setCoversubdepartId(
							insMap.get("RSK_SUBCLASS") == null ? "" : insMap.get("RSK_SUBCLASS").toString());
					res.setCoverTypeId(insMap.get("RSK_TYPE") == null ? "" : insMap.get("RSK_TYPE").toString());
					res.setDeductableDays(insMap.get("RSK_DEDUCTABLE_DAYS") == null ? ""
							: insMap.get("RSK_DEDUCTABLE_DAYS").toString());
					res.setDeductableLimitOC(insMap.get("RSK_DEDUCTABLELIMIT_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_DEDUCTABLELIMIT_OC").toString()));
					res.setEgnpiAsPerOff(insMap.get("RSK_GWPI_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_GWPI_OC").toString()));
					res.setPmlHundredPer(fm.formatter(
							insMap.get("PML_HUN_PER_OC") == null ? "0" : insMap.get("PML_HUN_PER_OC").toString()));
					res.setPmlPerList(fm.formatter(
							insMap.get("PML_PERCENTAGE") == null ? "0" : insMap.get("PML_PERCENTAGE").toString()));
					res.setPremiumRateList(insMap.get("RSK_PREMIUM_RATE") == null ? ""
							: fm.formatter(insMap.get("RSK_PREMIUM_RATE").toString()));
					GetCommonDropDownRes dropDownRes = dropDowmImpl.getSubProfitCentreDropDown(
							insMap.get("RSK_CLASS") == null ? "" : insMap.get("RSK_CLASS").toString(), branchCode,
							productId);
					res.setCoversubdeptList(dropDownRes.getCommonResponse());
					res.setLoopcount(Integer.toString(result.size()));
					;
					resList.add(res);
				}
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
	
	public GetXolCoverDeductableDetailsRes GetXolCoverDeductableDetails(String proposalNo, String branchCode) {
		GetXolCoverDeductableDetailsRes response = new GetXolCoverDeductableDetailsRes();

		List<GetXolCoverDeductableDetailsRes1> resList = new ArrayList<GetXolCoverDeductableDetailsRes1>();
		try {
			List<Tuple> result = new ArrayList<>();
			// query -- GET_XOLCOVER_DEDUCTABLE_DETAILS
			result = facultativeCustomRepository.getXolcoverDeductableDetails(proposalNo, "0");
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					GetXolCoverDeductableDetailsRes1 res = new GetXolCoverDeductableDetailsRes1();
					Tuple insMap = result.get(i);
					res.setXolcoverdepartId(insMap.get("RSK_CLASS") == null ? "" : insMap.get("RSK_CLASS").toString());
					GetCommonDropDownRes dropDownRes = dropDowmImpl.getSubProfitCentreDropDown(
							insMap.get("RSK_CLASS") == null ? "" : insMap.get("RSK_CLASS").toString(), branchCode, "1");
					res.setCoversubdeptList(dropDownRes.getCommonResponse());
					res.setXolcoverLimitOC(insMap.get("RSK_COVERLIMIT_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_COVERLIMIT_OC").toString()));
					res.setXolcoversubdepartId(
							insMap.get("RSK_SUBCLASS") == null ? "" : insMap.get("RSK_SUBCLASS").toString());
					res.setXoldeductableLimitOC(insMap.get("RSK_DEDUCTABLELIMIT_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_DEDUCTABLELIMIT_OC").toString()));
					res.setXolgwpiOC(insMap.get("RSK_GWPI_OC") == null ? ""
							: fm.formatter(insMap.get("RSK_GWPI_OC").toString()));
					res.setXolpremiumRateList(insMap.get("RSK_PREMIUM_RATE") == null ? ""
							: fm.formatter(insMap.get("RSK_PREMIUM_RATE").toString()));
					res.setXolLoopcount(Integer.toString(result.size()));
					resList.add(res);
				}
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
	
	public CommonResponse insertRemarkDetails(RemarksSaveReq beanObj) {
		CommonResponse resp = new CommonResponse();
		String amendId = "";
		try {
			// Query = DELETE_REMARKS_DETAILS
			facultativeCustomRepository.deleteRemarksDetails(beanObj.getProposalNo(), "0");

			// Query = TTRN_RISK_REMARKS_AMEND_ID
			amendId = facultativeCustomRepository.ttrnRiskRemarksAmendId(beanObj.getProposalNo());
			amendId = amendId == null ? "" : amendId;
			if (!CollectionUtils.isEmpty(beanObj.getRemarksReq())) {
				for (int i = 0; i < beanObj.getRemarksReq().size(); i++) {
					RemarksReq req = beanObj.getRemarksReq().get(i);
					TtrnRiskRemarks entity = ttrnRiskRemarksMapper.toEntity(beanObj);
					entity.setAmendId(amendId);
					entity.setRskSNo(String.valueOf(i + 1));
					entity.setRskDescription(req.getDescription());
					entity.setRskRemark1(req.getRemark1());
					entity.setRskRemark2(req.getRemark2());

					// query -- INSERT_REMARKS_DETAILS
					ttrnRiskRemarksRepository.save(entity);
					resp.setMessage("Success");
					resp.setErroCode(0);
					resp.setIsError(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("Failed");
			resp.setErroCode(1);
			resp.setIsError(true);
		}
		return resp;
	}
	
	public GetCommonValueRes getShareValidation(String proposalNo, String leaderUWShare) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result = "false";
		String out = "";
		try {
			// query -- GET_SIGN_SHARE_PRODUCT1
			out = facultativeCustomRepository.getSignShareProduct1(proposalNo);
			out = out == null ? "" : out;

			if (Double.parseDouble(out) + Double.parseDouble(leaderUWShare) > 100) {
				result = "true";
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public GetCommonValueRes getBonusListCount(String proposalNo, String branchCode, String acqBonus,
			String endorsmentno) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result = "";
		try {
			if (StringUtils.isBlank(endorsmentno)) {
				endorsmentno = "0";
			}
			// query -- BONUS_COUNT_MAIN
			Integer output = facultativeCustomRepository.bonusCountMain(proposalNo, branchCode, acqBonus, endorsmentno,
					"0");
			result = output == null ? "" : output.toString();
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public ShowSecondPagedataRes showSecondPagedata(ShowSecondPagedataReq beanObj) {
		ShowSecondPagedataRes response = new ShowSecondPagedataRes();
		ShowSecondPagedataRes1 formObj = new ShowSecondPagedataRes1();
		try{
			List<String> days=new ArrayList<String>();
			// query -- fac.select.showSecondData
			List<Tuple> list =  facultativeCustomRepository.facSelectShowSecondData(beanObj.getProposalNo(), beanObj.getProductId(), beanObj.getBranchCode());
			if(list!=null && list.size()>0) {
				Tuple tempMap = list.get(0);
				formObj.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
				formObj.setSubProfitCenter(tempMap.get("TMAS_SPFC_NAME")==null?"":tempMap.get("TMAS_SPFC_NAME").toString()); 
				formObj.setCedingCompany(tempMap.get("COMPANY")==null?"":tempMap.get("COMPANY").toString());
				formObj.setBroker(tempMap.get("BROKER")==null?"":tempMap.get("BROKER").toString());
				formObj.setUnderwriter(tempMap.get("RSK_UWYEAR")==null?"":tempMap.get("RSK_UWYEAR").toString());
				formObj.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
				formObj.setDepartClass(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
				formObj.setEndttypename(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
			}
			if (StringUtils.isNotBlank(beanObj.getNoOfInst()) && Integer.parseInt(beanObj.getNoOfInst()) > 0) {
				List<String> instalList = new ArrayList<String>();
				for (int i = 0; i < Integer.parseInt(beanObj.getNoOfInst()); i++) {
					instalList.add(String.valueOf(i));
				}
				formObj.setInstalList(instalList);
			}
			if (formObj.getInstalList().size() > 0) {
				for (int i = 0; i < formObj.getInstalList().size(); i++) {
					if (null == beanObj.getPaymentDueDays() || beanObj.getPaymentDueDays().size() <= i) {
						days.add(beanObj.getReceiptofPayment().get(i));
					} else {
						days.add(beanObj.getPaymentDueDays().get(i));
					}
				}
				formObj.setPaymentDueDays(days);
			} else {
				List<String> paymentdays = new ArrayList<String>();
				for (int k = 0; k < Integer.parseInt(beanObj.getNoOfInst()); k++) {
					paymentdays.add(beanObj.getReceiptofPayment().get(k));
				}
				formObj.setPaymentDueDays(paymentdays);
			}
			if (StringUtils.isNotBlank(beanObj.getNoInsurer()) && Integer.parseInt(beanObj.getNoInsurer()) > 0
					&& (beanObj.getRetrolList() == null || beanObj.getRetrolList().size() == 0)) {
				GetRetroContractDetailsListReq req = new GetRetroContractDetailsListReq();
				req.setBranchCode(beanObj.getBranchCode());
				req.setIncepDate(beanObj.getIncepDate());
				req.setRetroType(beanObj.getRetroType());
				req.setYear(beanObj.getYear());
				req.setProductId(beanObj.getProductId());

				List<RetroListRes> retroList1 = new ArrayList<RetroListRes>();
				List<RetroDupListRes> retroDupList1 = new ArrayList<RetroDupListRes>();
				for (int i = 1; i < Integer.parseInt(beanObj.getNoInsurer()); i++) {
					RetroListRes retroListRes = new RetroListRes();
					GetRetroContractDetailsListRes retroList = getRetroContractDetailsList(req, "2");
					if (!(retroList.getCommonResponse() == null)) {
						retroListRes.setCONTDET1(retroList.getCommonResponse().get(i).getCONTDET1());
						retroListRes.setCONTDET2(retroList.getCommonResponse().get(i).getCONTDET2());
						retroList1.add(retroListRes);
					}

				}
				formObj.setRetroListRes(retroList1);
				GetRetroContractDetailsListRes retroDupList = getRetroContractDetailsList(req, "3");
				RetroDupListRes retroDup = new RetroDupListRes();
				if (!(retroDupList.getCommonResponse() == null)) {
					retroDup.setCONTDET1(retroDupList.getCommonResponse().get(0).getCONTDET1());
					retroDup.setCONTDET2(retroDupList.getCommonResponse().get(0).getCONTDET2());
					retroDupList1.add(retroDup);
				}
				formObj.setRetroDupList(retroDupList1);
			}

			response.setCommonResponse(formObj);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	public FirstPageInsertRes firstPageInsert(FirstPageInsertReq beanObj) {
		FirstPageInsertRes response = new FirstPageInsertRes();
		FirstPageInsertRes1 res = new FirstPageInsertRes1();
		try {
			String[] args = firstPageInsertAruguments(beanObj, Boolean.valueOf(beanObj.getFlag()),
					Boolean.valueOf(beanObj.getContract()));
			// query -- fac.sp.facultativepage1
			int result = facultativeCustomRepository.facSpFacultativepage1(args);
			updatependingFields(beanObj);
			if (result == 1)
				res.setSaveFlag("true");
			else
				res.setSaveFlag("false");
			beanObj.setProposalNo(args[0].toString());
			if (StringUtils.isNotBlank(beanObj.getContractNo())) {
				res.setBackmode("Con");
				res.setStatus(
						"Your Proposal is saved in Endorsement with Proposal No : " + beanObj.getProposalNo() + ".");
			} else if ("0".equalsIgnoreCase(beanObj.getProStatus()) || "P".equalsIgnoreCase(beanObj.getProStatus())
					|| "A".equalsIgnoreCase(beanObj.getProStatus())) {
				res.setBackmode("Pro");
				res.setStatus(
						"Your Proposal is saved in Pending Stage with Proposal No : " + beanObj.getProposalNo() + ".");
			} else if ("N".equalsIgnoreCase(beanObj.getProStatus())) {
				res.setBackmode("NTU");
				res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "
						+ beanObj.getProposalNo() + ".");
			} else if ("R".equalsIgnoreCase(beanObj.getProStatus())) {
				res.setBackmode("Reje");
				res.setStatus(
						"Your Proposal is saved in Rejected Stage with Proposal No : " + beanObj.getProposalNo() + ".");
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
	
	public String[] firstPageInsertAruguments(FirstPageInsertReq beanObj,final boolean flag,final boolean contract) throws ParseException {
		final Validation val=new Validation();
		String[] args=new String[64];
		if(beanObj.getProposalNo().equalsIgnoreCase("")){
				args[0]= fm.getSequence("Proposal", beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),"");
		}
		else
		args[0]=beanObj.getProposalNo();	
		beanObj.setProposalNo(args[0].toString());
		args[1]="0";
		args[2]=beanObj.getContractNo()==null?"":beanObj.getContractNo();
		args[3]=StringUtils.isEmpty(beanObj.getUsCurrencyRate())? "0": beanObj.getUsCurrencyRate();
		args[4]=StringUtils.isEmpty(beanObj.getProfitCenterCode())? "0": beanObj.getProfitCenterCode();
		args[5]=StringUtils.isEmpty(beanObj.getSubProfitCenter())? "0": beanObj.getSubProfitCenter();
		args[6]=StringUtils.isEmpty(beanObj.getMonth())? "": val.GetProcedureDate(beanObj.getMonth());
		args[7]=StringUtils.isEmpty(beanObj.getUnderwriter())? "": beanObj.getUnderwriter();
		args[8]=StringUtils.isEmpty(beanObj.getFacultativeDepartment())? "0": beanObj.getFacultativeDepartment();
		args[9]=StringUtils.isEmpty(beanObj.getCedingCompany())? "0":beanObj.getCedingCompany();
		args[10]=StringUtils.isEmpty(beanObj.getBroker())? "0":beanObj.getBroker();
		args[11]=StringUtils.isEmpty(beanObj.getInceptionDate())? "": val.GetProcedureDate(beanObj.getInceptionDate());
		args[12]=StringUtils.isEmpty(beanObj.getExpiryDate())? "":val.GetProcedureDate(beanObj.getExpiryDate());
		args[13]=StringUtils.isEmpty(beanObj.getAccountDate())?"":val.GetProcedureDate(beanObj.getAccountDate());
		args[14]=StringUtils.isEmpty(beanObj.getOriginalCurrency())? "0":beanObj.getOriginalCurrency();
		args[15]=StringUtils.isEmpty(beanObj.getTerritory())? "0":beanObj.getTerritory();
		args[16]=StringUtils.isEmpty(beanObj.getInsuredName())? "":beanObj.getInsuredName();
		args[17]=StringUtils.isEmpty(beanObj.getLocation())? "":beanObj.getLocation();
		args[18]=StringUtils.isEmpty(beanObj.getCity())? "":beanObj.getCity();
		args[19]=StringUtils.isEmpty(beanObj.getCedantsRet())? "0":beanObj.getCedantsRet();
		args[20]=StringUtils.isEmpty(beanObj.getNr())? "0":beanObj.getNr();
		args[21]=StringUtils.isEmpty(beanObj.getMaxiumlimit())? "0":beanObj.getMaxiumlimit();
		args[22]=StringUtils.isEmpty(beanObj.getDeductible())? "0":beanObj.getDeductible();
		args[23]=StringUtils.isEmpty(beanObj.getInterest())? "":beanObj.getInterest();
		args[24]=StringUtils.isEmpty(beanObj.getSpRetro())? "0":beanObj.getSpRetro();
		args[25]=StringUtils.isEmpty(beanObj.getPml())? "0":beanObj.getPml();
		args[26]=StringUtils.isEmpty(beanObj.getSipml())? "0":beanObj.getSipml();
		args[27]=StringUtils.isEmpty(beanObj.getSumInsured())? "0":beanObj.getSumInsured();
		args[28]=StringUtils.isEmpty(beanObj.getGwpi())? "0": beanObj.getGwpi();
		args[29]=StringUtils.isEmpty(beanObj.getPmll())? "0":beanObj.getPmll();
		args[30]=StringUtils.isEmpty(beanObj.getTpl())? "0":beanObj.getTpl();  
		args[31]=StringUtils.isEmpty(beanObj.getShWt())? "0": beanObj.getShWt();
		if(!beanObj.getProStatus().equalsIgnoreCase("A")){
			args[32]="0";
		}
		else if(beanObj.getProStatus().equalsIgnoreCase("A")){
			args[32]=StringUtils.isEmpty(beanObj.getShSd())? "0": beanObj.getShSd();
		}
		args[33]=StringUtils.isEmpty(beanObj.getProductId()) ? "0":   beanObj.getProductId();
		args[34]=StringUtils.isEmpty(beanObj.getYear())? "0":   beanObj.getYear();
		if(flag){
			args[35]="P";
			args[36]="P";
		}
		else {
			if(beanObj.getProStatus().equalsIgnoreCase("0") ||  beanObj.getProStatus().equalsIgnoreCase("P") ){
				args[35]="P";
				args[36]="P";
			}else{
				args[35]=beanObj.getProStatus();
				args[36]=beanObj.getProStatus();
			}	
		}
		args[37]=StringUtils.isNotBlank(beanObj.getBaseLoginID())?beanObj.getBaseLoginID():beanObj.getLoginid();
		args[38]=StringUtils.isEmpty(beanObj.getNoInsurer())?"0":beanObj.getNoInsurer();
		args[39]=StringUtils.isEmpty(beanObj.getRenewalContractno())?"":beanObj.getRenewalContractno();
		args[40]=beanObj.getRenewalStatus();
		args[41]=StringUtils.isEmpty(beanObj.getPremiumrate())?"0":beanObj.getPremiumrate();
		args[42]=beanObj.getBranchCode();
		args[43]=beanObj.getPolicyBranch();
		args[44]=StringUtils.isEmpty(beanObj.getCedRetenType())?"":beanObj.getCedRetenType();
		args[45]=StringUtils.isEmpty(beanObj.getSumInsuredOurShare())?"0": beanObj.getSumInsuredOurShare();
		args[46]=StringUtils.isEmpty(beanObj.getGwpiOurShare())? "0": beanObj.getGwpiOurShare();
		args[47]=StringUtils.isEmpty(beanObj.getPmlOurShare())? "0": beanObj.getPmlOurShare();
		args[48]=StringUtils.isEmpty(beanObj.getTplOurShare())? "0": beanObj.getTplOurShare();
		args[49]=beanObj.getLoginid();
		args[50]=StringUtils.isEmpty(beanObj.getDeductibleFacXol())?"0": beanObj.getDeductibleFacXol();
		args[51]=StringUtils.isEmpty(beanObj.getXolOC())?"0": beanObj.getXolOC();
		args[52]=StringUtils.isEmpty(beanObj.getXolOSOC())?"0": beanObj.getXolOSOC();
		args[53]=StringUtils.isEmpty(beanObj.getNoOfInst())?"0": beanObj.getNoOfInst();
		args[54]=StringUtils.isEmpty(beanObj.getModeOfTransport())?"0": beanObj.getModeOfTransport();
		args[55]=StringUtils.isEmpty(beanObj.getVesselName())?"": beanObj.getVesselName();
		args[56]=StringUtils.isEmpty(beanObj.getVesselAge())?"0": beanObj.getVesselAge();
		args[57]=StringUtils.isEmpty(beanObj.getLimitPerVesselOC())?"0": beanObj.getLimitPerVesselOC();
		args[58]=StringUtils.isEmpty(beanObj.getLimitPerLocationOC())?"0": beanObj.getLimitPerLocationOC();
		args[59]=StringUtils.isEmpty(beanObj.getType())?"": beanObj.getType();
		args[60]=StringUtils.isEmpty(beanObj.getCountryIncludedList())?"": beanObj.getCountryIncludedList();
		args[61]=StringUtils.isEmpty(beanObj.getCountryExcludedList())?"": beanObj.getCountryExcludedList();
		args[62]=StringUtils.isEmpty(beanObj.getContractListVal())?"": beanObj.getContractListVal();
		args[63]=StringUtils.isEmpty(beanObj.getXollayerNo())?"": beanObj.getXollayerNo();
		return args;
	}
	
	
	private void updatependingFields(FirstPageInsertReq beanObj) {
		try {
			// query -- UPDATE_RISK_DETAILS
			facultativeCustomRepository.updateRiskDetails(beanObj);

			// query -- UPDATE_FAC_RISK_PROPOSAL
			facultativeCustomRepository.updateFacRiskProposal(beanObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public SecondPageInsertRes secondPageInsert(SecondPageInsertReq beanObj) {
		SecondPageInsertRes response = new SecondPageInsertRes();
		SecondPageInsertRes1 res = new SecondPageInsertRes1();
		boolean saveFlag=false;
		try{
			final String[] args = secondPageInsertArg(beanObj);
			// query -- fac.sp.facultativepage2
			int result = facultativeCustomRepository.facSpFacultativepage2(args);
			if(result==1)
				saveFlag=true;
			else
				saveFlag=false;
			if("".equalsIgnoreCase(beanObj.getContractNo())||"0".equalsIgnoreCase(beanObj.getContractNo())){
				// query -- fac.select.contGen
				List<Tuple>  list = facultativeCustomRepository.facSelectContGen(beanObj.getProposalNo());
				if(list!=null&&list.size()>0){	
					Tuple contractMap = list.get(0);
					res.setProStatus(contractMap.get("RSK_STATUS")==null?"":contractMap.get("RSK_STATUS").toString());
					res.setContractNo(contractMap.get("RSK_CONTRACT_NO")==null?"":contractMap.get("RSK_CONTRACT_NO").toString());
					if("true".equalsIgnoreCase(beanObj.getFlag())){
						if("A".equalsIgnoreCase(res.getProStatus()) && ! "0".equalsIgnoreCase(beanObj.getShSd())){
							if(res.getContractNo()==null||"0".equalsIgnoreCase(res.getContractNo())||"".equalsIgnoreCase(res.getContractNo())){
								if(!"".equals(beanObj.getRenewalContractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									res.setContractNo(beanObj.getRenewalContractno());
								}else {
										res.setContractNo(fm.getSequence("Contract",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),beanObj.getProposalNo(),beanObj.getYear()));
								}
								// query  -- common.update.riskDetContNo
								facultativeCustomRepository.commonUpdateRiskDetContNo(res.getContractNo(),beanObj.getProposalNo());
								
								//query -- common.update.posMasDetContNo
								facultativeCustomRepository.commonUpdatePosMasDetContNo(res.getContractNo(),beanObj.getProposalNo());								
								if(!"".equals(beanObj.getRenewalContractno())&&"OLDCONTNO".equals(beanObj.getRenewalFlag())){
									res.setStatus("Your Proposal is Renewaled with Proposal No : "+beanObj.getProposalNo() +", Old Contract No: "+beanObj.getContractNo()+" and New Contract No : "+beanObj.getContractNo()+".");
								}else{
									res.setStatus("Your Proposal is converted to Contract with Proposal No : "+beanObj.getProposalNo() +" and Contract No : "+beanObj.getContractNo()+".");
								}
								res.setBackmode("Con");
							}
							else{
								res.setStatus("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+beanObj.getContractNo()+".");
								res.setBackmode("Con");
							}
						}else if("0".equalsIgnoreCase(res.getProStatus())||"P".equalsIgnoreCase(res.getProStatus())||"A".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("Pro");
						}else if("N".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("NTU");
						}	
					}else{
						if("0".equalsIgnoreCase(res.getProStatus())||"P".equalsIgnoreCase(res.getProStatus())||"A".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("Pro");
						}else if("N".equalsIgnoreCase(res.getProStatus())){
							res.setStatus("Your Proposal is saved in Not Taken Up Stage with Proposal No : "+beanObj.getProposalNo()+".");
							res.setBackmode("NTU");
						}
					}
				}else {
					res.setStatus("Your Proposal is saved in Pending Stage with Proposal No : ==>"+beanObj.getProposalNo());
					res.setBackmode("Pro");
				}		
			}else{
				res.setStatus("Your Contract is updated with Proposal No : "+beanObj.getProposalNo()+" and Contract No : "+beanObj.getContractNo()+".");
				res.setBackmode("Con");
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
	
	public String[] secondPageInsertArg(final SecondPageInsertReq beanObj) {
		String[] args = new String[34];
		args[0] = beanObj.getProposalNo() == null ? "" : beanObj.getProposalNo();
		args[1] = beanObj.getContractNo() == null ? "" : beanObj.getContractNo();
		args[2] = StringUtils.isEmpty(beanObj.getSumInsuredOurShare()) ? "0" : beanObj.getSumInsuredOurShare();
		args[3] = StringUtils.isEmpty(beanObj.getGwpiOurShare()) ? "0" : beanObj.getGwpiOurShare();
		args[4] = StringUtils.isEmpty(beanObj.getPmlOurShare()) ? "0" : beanObj.getPmlOurShare();
		args[5] = StringUtils.isEmpty(beanObj.getTplOurShare()) ? "0" : beanObj.getTplOurShare();
		args[6] = StringUtils.isEmpty(beanObj.getRiskGrade()) ? "" : beanObj.getRiskGrade();
		args[7] = StringUtils.isEmpty(beanObj.getOccCode()) ? "" : beanObj.getOccCode();
		args[8] = StringUtils.isEmpty(beanObj.getRiskDetail()) ? "" : beanObj.getRiskDetail();
		args[9] = StringUtils.isEmpty(beanObj.getFireProt()) ? "" : beanObj.getFireProt();
		args[10] = StringUtils.isEmpty(beanObj.getScope()) ? "" : beanObj.getScope();
		args[11] = StringUtils.isEmpty(beanObj.getMbind()) ? "" : beanObj.getMbind();
		args[12] = StringUtils.isEmpty(beanObj.getCategoryZone()) ? "" : beanObj.getCategoryZone();
		args[13] = StringUtils.isEmpty(beanObj.getEqwsInd()) ? "0" : beanObj.getEqwsInd();
		args[14] = StringUtils.isEmpty(beanObj.getWsThreat()) ? "0" : beanObj.getWsThreat();
		args[15] = StringUtils.isEmpty(beanObj.getEqThreat()) ? "0" : beanObj.getEqThreat();
		args[16] = StringUtils.isEmpty(beanObj.getCommn()) ? "0" : beanObj.getCommn();
		args[17] = StringUtils.isEmpty(beanObj.getBrokerage()) ? "0" : beanObj.getBrokerage();
		args[18] = StringUtils.isEmpty(beanObj.getTax()) ? "0" : beanObj.getTax();
		args[19] = StringUtils.isEmpty(beanObj.getLossRecord()) ? "" : beanObj.getLossRecord();
		args[20] = StringUtils.isEmpty(beanObj.getDgmsApproval()) ? "" : beanObj.getDgmsApproval();
		args[21] = StringUtils.isEmpty(beanObj.getUnderwriterCode()) ? "" : beanObj.getUnderwriterCode();
		args[22] = StringUtils.isEmpty(beanObj.getUwRecommendation()) ? "" : beanObj.getUwRecommendation();
		args[23] = StringUtils.isEmpty(beanObj.getRemarks()) ? "" : beanObj.getRemarks();
		args[24] = StringUtils.isEmpty(beanObj.getOthAccep()) ? "" : beanObj.getOthAccep();
		args[25] = StringUtils.isEmpty(beanObj.getReftoHO()) ? "0" : beanObj.getReftoHO();
		args[26] = StringUtils.isEmpty(beanObj.getAcqCost()) ? "0" : beanObj.getAcqCost();
		args[27] = StringUtils.isEmpty(beanObj.getCu()) ? "0" : beanObj.getCu();
		args[28] = StringUtils.isEmpty(beanObj.getCuRsn()) ? "" : beanObj.getCuRsn();
		args[29] = StringUtils.isEmpty(beanObj.getOthercost()) ? "" : beanObj.getOthercost();
		args[30] = StringUtils.isEmpty(beanObj.getMlopYN()) ? "" : beanObj.getMlopYN();
		args[31] = StringUtils.isEmpty(beanObj.getAlopYN()) ? "" : beanObj.getAlopYN();
		if ("NCB".equalsIgnoreCase(beanObj.getAcqBonus())) {
			args[32] = StringUtils.isEmpty(beanObj.getAcqBonusPercentage()) ? "" : beanObj.getAcqBonusPercentage();
		} else {
			args[32] = "";
		}
		args[33] = StringUtils.isEmpty(beanObj.getAcqBonus()) ? "" : beanObj.getAcqBonus();
		return args;
	}
}
