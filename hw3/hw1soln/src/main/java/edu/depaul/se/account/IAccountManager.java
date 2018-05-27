package edu.depaul.se.account;

import java.util.List;

/**
 * Essence of what the homework asks to be implemented
 */
public interface IAccountManager {

	/**
	 * Create an account 
	 * @param name - name of the person whose account it is being created
	 * @param initialBalance - initial balance to create the account
	 * @return newly created account number
     * @throws InvalidAmountException - if negative amount is passed
	 */
    int createAccount(String name, float initialBalance) throws InvalidAmountException;

    /**
     * Deposit into account
     * @param accountNbr - account number to deposit into
     * @param amount - deposit amount
     * @return new balance
     * @throws AccountNotFoundException - if account is not found
     * @throws InvalidAmountException - if negative amount is passed
     */
    float deposit(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException;

    /**
     * List of open accounts
     * @return
     */
    List<IAccount> getAllAccounts();

    /**
     * Withdraw amount
     * @param accountNbr - account number to withdraw from
     * @param amount - deposit amount
     * @return new balance
     * @throws AccountNotFoundException - if account is not found
     * @throws InvalidAmountException - if negative amount is passed
     * @throws InsufficientFundsException - if amount trying to withdraw is larger than the remaining balance
     */
    float withdraw(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException;

    /**
     * Close account
     * @param accountNbr - account number to close out
     * @return remaining balance on the account
     * @throws AccountNotFoundException - if account is not found
     */
    float closeAccount(int accountNbr) throws AccountNotFoundException;
    
    /**
     * Find the account
     * @param accountNbr - account number to close out
     * @return Account
     * @throws AccountNotFoundException - if account is not found
     */
    IAccount findAccount(int accountNbr) throws AccountNotFoundException;
}
