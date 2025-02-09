package com.code.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;

import com.code.exception.TransactionNotFoundException;
import com.code.exception.UserNotLogedinException;
import com.code.model.*;
import com.code.repository.CustomerDAO;
import com.code.repository.SessionDAO;
import com.code.repository.TransactionDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TranscationServiceImpl implements TransactionService{
	
	private final CustomerDAO customerDAO;
	
	private final TransactionDao transactiondao;
	
	private final SessionDAO sessionDao;

	@Override
	public List<Transaction> viewAlltransaction(String  uniqueId) throws UserNotLogedinException, TransactionNotFoundException {
		
		Optional<CurrentSessionUser> optional = sessionDao.findByUuid(uniqueId);
		
		if(!optional.isPresent()) {
			throw new UserNotLogedinException("User is not Logged in");
		}
		
		Optional<Customer> customer=  customerDAO.findById(optional.get().getUserId());
		
		
		Wallet wallet = customer.get().getWallet();
		
		List<Transaction> transactions  = wallet.getTransaction();
		
		if(transactions.size()>0) {
			return transactions;
		}else {
			throw new TransactionNotFoundException("Not found any transaction with wallet");
		}
		
	}

	@Override
	public List<Transaction> viewTranscationByDate(String from, String  to,String uniqueId) throws UserNotLogedinException,TransactionNotFoundException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		LocalDate start = LocalDate.parse(from, formatter);
		LocalDate end = LocalDate.parse(to, formatter);
		
		Optional<CurrentSessionUser> optional = sessionDao.findByUuid(uniqueId);
		
		if(!optional.isPresent()) {
			throw new UserNotLogedinException("User not logged in");
		}
		
		Optional<Customer> customer=  customerDAO.findById(optional.get().getUserId());
		
		Wallet wallet = customer.get().getWallet();
		
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<Transaction> transaction = transactiondao.findByWalletId(wallet.getWalletId());
		List<Transaction> trans = new ArrayList<>();
		
		for(int i = 0 ; i<transaction.size() ; i++) {
			String str1 =  transaction.get(i).getTransactionDate().format(formatter2);
			LocalDate temp = LocalDate.parse(str1, formatter);
			if ((temp.isAfter(start) && temp.isBefore(end)) || temp.equals(start) || temp.equals(end)) {
				trans.add(transaction.get(i));
			}
		}
		return trans;

	}


	@Override
	public List<Transaction> viewAllTransactionbyTransactionType(String uniqueId,TransactionType type) throws UserNotLogedinException,TransactionNotFoundException  {

		Optional<CurrentSessionUser> optional = sessionDao.findByUuid(uniqueId);
		
		if(!optional.isPresent()) {
			throw new UserNotLogedinException("User not logged In");
		}
		
		Optional<Customer> customer=  customerDAO.findById(optional.get().getUserId());
		Wallet wallet = customer.get().getWallet();
		wallet.getTransaction();
		List<Transaction> transactionslist = transactiondao.getTransactionByTransactionType(type);
		
		List<Transaction> transactionLists= new ArrayList<>();
		for(Transaction tr: transactionslist) {
			if(tr.getWalletId()==wallet.getWalletId()) {
				transactionLists.add(tr);
			}
		}
		
		if(transactionLists.size()>0) {
			return transactionLists;
		}else {
			throw new TransactionNotFoundException("Transaction not found");
		}
	}



}
