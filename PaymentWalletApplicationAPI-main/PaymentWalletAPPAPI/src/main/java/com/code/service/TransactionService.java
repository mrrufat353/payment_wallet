package com.code.service;

import java.util.*;

import com.code.exception.TransactionNotFoundException;
import com.code.exception.UserNotLogedinException;
import com.code.model.Transaction;
import com.code.model.TransactionType;

public interface TransactionService {
	
	public List<Transaction> viewAlltransaction(String  uniqueId)throws UserNotLogedinException, TransactionNotFoundException ;
	
	public List<Transaction> viewTranscationByDate(String from, String to , String uniqueId)  throws UserNotLogedinException,TransactionNotFoundException ;
		
	public List<Transaction> viewAllTransactionbyTransactionType(String uniqueId,TransactionType type) throws UserNotLogedinException, TransactionNotFoundException;


}
