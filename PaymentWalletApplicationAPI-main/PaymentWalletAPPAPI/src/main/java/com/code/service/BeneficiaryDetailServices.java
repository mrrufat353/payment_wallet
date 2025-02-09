package com.code.service;

import java.util.List;

import com.code.exception.BeneficiaryDetailException;
import com.code.model.BeneficiaryDetail;

public interface BeneficiaryDetailServices {
	public BeneficiaryDetail addBeneficiary(String uniqueId, BeneficiaryDetail beneficiaryDetail)
			throws BeneficiaryDetailException;

	public BeneficiaryDetail deleteBeneficiary(String uniqueId, String benficiaryMobileNo)
			throws BeneficiaryDetailException;

	public List<BeneficiaryDetail> viewBeneficiaryByMobileNo(String beneficiaryMobileNo)
			throws BeneficiaryDetailException;

	public List<BeneficiaryDetail> viewAllBeneficiary(String uniqueId) throws BeneficiaryDetailException;
}
