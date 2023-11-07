package com.code.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.code.exception.BeneficiaryDetailException;
import com.code.model.BeneficiaryDetail;
import com.code.model.CurrentSessionUser;
import com.code.model.Customer;
import com.code.model.Wallet;
import com.code.repository.BeneficiaryDetailDao;
import com.code.repository.CustomerDAO;
import com.code.repository.SessionDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeneficiaryDetailServicesImpl implements BeneficiaryDetailServices {
	
	private final BeneficiaryDetailDao bDao;
	
	private final SessionDAO sDao;
	
	private final CustomerDAO customerDao;
	

	@Override
	public BeneficiaryDetail addBeneficiary(String uniqueId,BeneficiaryDetail beneficiaryDetail) throws BeneficiaryDetailException {
		Optional<CurrentSessionUser> currentSessionUser= sDao.findByUuid(uniqueId);
		if(currentSessionUser.isPresent()) {
			Optional<Customer> userOptional=customerDao.findById(currentSessionUser.get().getUserId());
			if(userOptional.isPresent()) {
				Wallet wallet = userOptional.get().getWallet();
				beneficiaryDetail.setWalletId(wallet.getWalletId());
				List<BeneficiaryDetail> list = wallet.getBeneficiaryDetails();
				list.add(beneficiaryDetail);
				BeneficiaryDetail saved =bDao.save(beneficiaryDetail);
				return saved;
			}else {
				throw new BeneficiaryDetailException("No Customer found with id "+currentSessionUser.get().getUserId());
			}
		}
		
		else {
			throw new BeneficiaryDetailException("You need to login first!");
		}
	}

	@Override
	public BeneficiaryDetail deleteBeneficiary(String uniqueId,String benficiaryMobile) throws BeneficiaryDetailException {
		Optional<CurrentSessionUser> currentSessionUser= sDao.findByUuid(uniqueId);
		if(currentSessionUser.isPresent()) {
			Optional<Customer> userOptional=customerDao.findById(currentSessionUser.get().getUserId());
			if(userOptional.isPresent()) {
				Customer customer = userOptional.get();
				Wallet wallet = customer.getWallet();
				List<BeneficiaryDetail> list=wallet.getBeneficiaryDetails();
				if(!list.isEmpty()) {
					int index=-1;
					for(int i=0;i<list.size();i++) {
						if(list.get(i).getBeneficiaryMobileNo().equals(benficiaryMobile)) {
							index=i;
							break;
						}
					}
					if(index==-1) throw new BeneficiaryDetailException("Beneficiary Not found");
					BeneficiaryDetail beneficiaryDetail=list.get(index);
					BeneficiaryDetail updated=list.remove(index);
					wallet.setBeneficiaryDetails(list);
					bDao.delete(updated);
					return beneficiaryDetail;
					
				}else {
					throw new BeneficiaryDetailException("There is no beneficiary found in the list");
				}
				
			}else {
				throw new BeneficiaryDetailException("No Customer found with id "+currentSessionUser.get().getUserId());
			}
		}
		
		else {
			throw new BeneficiaryDetailException("You need to login first!");
		}
		
	}

	@Override
	public List<BeneficiaryDetail> viewBeneficiaryByMobileNo(String beneficiaryMobileNo) throws BeneficiaryDetailException {
		List<BeneficiaryDetail> beneficiaryDetail=bDao.findBybeneficiaryMobileNo(beneficiaryMobileNo);
		if(beneficiaryDetail.isEmpty()) {
			throw new BeneficiaryDetailException("No Beneficiary found with Mobile No : "+beneficiaryMobileNo);
		}else {
			return beneficiaryDetail;
		}
	}

	@Override
	public List<BeneficiaryDetail> viewAllBeneficiary(String uniqueId) throws BeneficiaryDetailException {
		Optional<CurrentSessionUser> currentSessionUser= sDao.findByUuid(uniqueId);
		if(currentSessionUser.isPresent()) {
			CurrentSessionUser currUser =  currentSessionUser.get();
			int userid=currUser.getUserId();
			Optional<Customer> customerOpt = customerDao.findById(userid);
			if(customerOpt.isPresent()) {
				Customer customer = customerOpt.get();
				List<BeneficiaryDetail>list =customer.getWallet().getBeneficiaryDetails();
				if(list.isEmpty()) {
					throw new BeneficiaryDetailException("No Beneficiary found in the list");
				}else {
					return list;
				}
			}else {
				throw new BeneficiaryDetailException("You need to login first");
			}
			
		}
		else {
			throw new BeneficiaryDetailException("You need to login first");	
		}
	}



}
