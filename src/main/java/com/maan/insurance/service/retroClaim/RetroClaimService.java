package com.maan.insurance.service.retroClaim;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.retroClaim.ContractDetailsMode1Res;

@Service
public interface RetroClaimService {

	CommonSaveRes getProposalNo(String departmentId, String contractNo, String layerNo, String productId);

	CommonSaveRes getShortname(String branchCode);

	ContractDetailsMode1Res contractDetailsMode1(ContractDetailsMode1Req req);

}
