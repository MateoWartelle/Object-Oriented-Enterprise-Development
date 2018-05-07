package edu.depaul.se.account.jpa;

import java.util.ArrayList;
import java.util.List;

import edu.depaul.se.account.AccountNotFoundException;
import edu.depaul.se.account.IAccount;
import edu.depaul.se.account.IAccountManager;
import edu.depaul.se.account.InsufficientFundsException;
import edu.depaul.se.account.InvalidAmountException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * Implementation of requirements using JPA
 */
public class AccountManager implements IAccountManager {

	private EntityManager em;

	public AccountManager() {
		em = Persistence.createEntityManagerFactory("AccountsPU").createEntityManager();
	}

	@Override
	public int createAccount(String name, float initialBalance) throws InvalidAmountException {
		if (initialBalance < 0) {
			throw new InvalidAmountException();
		}
		em.getTransaction().begin();
		Account account = new Account();
		account.setBalance(initialBalance);
		account.setName(name);
		em.persist(account);
		em.getTransaction().commit();
		Account temp = em.find(Account.class, account.getId());
		return temp.getId();
	}

	@Override
	public float deposit(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException {
		float initialAmount = 0;
		float combinedAmount = 0;
		if (amount < 0) {
			throw new InvalidAmountException();
		}
		em.getTransaction().begin();
		Account temp = em.find(Account.class, accountNbr);
		if (temp == null) {
			throw new AccountNotFoundException(String.valueOf(accountNbr));
		}
		initialAmount = temp.getBalance();
		combinedAmount = initialAmount + amount;
		temp.setBalance(combinedAmount);
		em.getTransaction().commit();
		em.close();
		return temp.getBalance();
	}

	@Override
	public List<IAccount> getAllAccounts() {
		List<IAccount> listOfAccounts = new ArrayList<IAccount>();
		em.getTransaction().begin();
		List<Account> Resultset = em.createNamedQuery("findAllAccounts", Account.class).getResultList();
		for (Account account : Resultset) {
			listOfAccounts.add(account);
		}
		return listOfAccounts;
	}

	@Override
	public float withdraw(int accountNbr, float amount)
			throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
		float initialAmount = 0;
		float combinedAmount = 0;
		if (amount < 0) {
			throw new InvalidAmountException();
		}
		em.getTransaction().begin();
		Account temp = em.find(Account.class, accountNbr);
		if (temp == null) {
			throw new AccountNotFoundException(String.valueOf(accountNbr));
		}
		initialAmount = temp.getBalance();
		combinedAmount = initialAmount - amount;
		if (combinedAmount < 0) {
			throw new InsufficientFundsException();
		}
		temp.setBalance(combinedAmount);
		em.getTransaction().commit();
		em.close();
		return temp.getBalance();
	}

	@Override
	public float closeAccount(int accountNbr) throws AccountNotFoundException {
		em.getTransaction().begin();
		Account temp = em.find(Account.class, accountNbr);
		if (temp == null) {
			throw new AccountNotFoundException(String.valueOf(accountNbr));
		}
		em.getTransaction().commit();
		em.close();
		return temp.getBalance();
	}

	@Override
	public IAccount findAccount(int accountNbr) throws AccountNotFoundException {
		em.getTransaction().begin();
		Account temp = em.find(Account.class, accountNbr);
		if (temp == null) {
			throw new AccountNotFoundException(String.valueOf(accountNbr));
		}
		em.getTransaction().commit();
		em.close();
		return temp;
	}

}
