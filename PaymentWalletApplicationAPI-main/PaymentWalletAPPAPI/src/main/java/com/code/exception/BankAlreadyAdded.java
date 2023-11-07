package com.code.exception;

public class BankAlreadyAdded extends Exception {

	public BankAlreadyAdded() {
		super();
	}
	public BankAlreadyAdded(String message) {
		super(message);
	}
}
