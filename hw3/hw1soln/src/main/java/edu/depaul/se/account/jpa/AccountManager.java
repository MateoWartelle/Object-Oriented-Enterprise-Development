package edu.depaul.se.account.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import edu.depaul.se.account.AccountNotFoundException;
import edu.depaul.se.account.IAccount;
import edu.depaul.se.account.IAccountManager;
import edu.depaul.se.account.InsufficientFundsException;
import edu.depaul.se.account.InvalidAmountException;

/**
 * Implementation of requirements using JPA
 * TODO:  em in static initializer
 */
public class AccountManager implements IAccountManager {
    private EntityManager em;

    public AccountManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountsPU");
        em = emf.createEntityManager();
    }
   
    /**
     * Check to see if the amount is valid amount
     * @param initialBalance
     * @throws InvalidAmountException
     */
    private void validateAmount(float initialBalance) throws InvalidAmountException {
    	if (initialBalance < 0) {
    		throw new InvalidAmountException();
    	}
    }
    
    public int createAccount(String name, float initialBalance) throws InvalidAmountException {
    	validateAmount(initialBalance);
    	
        Account account = new Account();
        account.setName(name);
        account.setBalance(initialBalance);
        
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        return account.getId();
    }

    public float withdraw(int accountNbr, float amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
    	validateAmount(amount);

        IAccount account = findAccount(accountNbr);
        if (account.getBalance() < amount) {
            em.getTransaction().rollback();
                throw new InsufficientFundsException();
            }
            float newBalance = account.getBalance() - amount;
            account.setBalance(newBalance);
            em.persist(account);

        em.getTransaction().commit();

        return newBalance;
    }

    public float deposit(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException {
    	validateAmount(amount);

        IAccount account = findAccount(accountNbr);
        float newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        em.persist(account);

        em.getTransaction().commit();

        return newBalance;
    }

    public Account findAccount(int accountNbr) throws AccountNotFoundException {
        em.getTransaction().begin();
        TypedQuery<Account> accountQuery = em.createNamedQuery("Account.findById", Account.class);
        accountQuery.setParameter("id", accountNbr);
        Account account = null;
        
        try {
            account = accountQuery.getSingleResult();
        } catch (NoResultException nre) {
            em.getTransaction().rollback();
        	throw new AccountNotFoundException("Account number: " + accountNbr + " was not found");
        }

        return account;
    }

    public List<IAccount> getAllAccounts() {
        TypedQuery<Account> accountQuery = em.createNamedQuery("Account.findAll", Account.class);
        List<IAccount> accounts = new ArrayList<IAccount>();
        accounts.addAll(accountQuery.getResultList());

        return accounts;
    }

    public float closeAccount(int accountNbr) throws AccountNotFoundException {
        Account account = findAccount(accountNbr);

        float newBalance = account.getBalance();
        
        account.setBalance(newBalance);
        em.remove(account);
        em.getTransaction().commit();

        return newBalance;
    }

}
