package edu.depaul.se.account;

public class AccountNotFoundException extends Exception{
	public AccountNotFoundException(String info) {
		super(info);
	}
}
