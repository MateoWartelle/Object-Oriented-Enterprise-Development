package edu.depaul.se.account.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.depaul.se.account.AccountNotFoundException;
import edu.depaul.se.account.IAccount;
import edu.depaul.se.account.IAccountManager;
import edu.depaul.se.account.InsufficientFundsException;
import edu.depaul.se.account.InvalidAmountException;

/**
 * Implementation of solution using JDBC
 */
public class AccountManager implements IAccountManager {

    public AccountManager(){
    }
    
    public int createAccount(String name, float initialBalance) throws InvalidAmountException {
    	validateAmount(initialBalance);
    	
    	int accountNumber = -1;
    	
        openConnection();
        try {
            PreparedStatement statement = con.prepareStatement("insert into accounts(name, balance) values (?, ?)",  Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setFloat(2, initialBalance);
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                accountNumber = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        

        return accountNumber;
    }

    public float deposit(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException {
    	validateAmount(amount);
    	
        float balance = getAccountBalance(accountNbr);        
        
        openConnection();
        try {
            PreparedStatement statement = con.prepareStatement("update accounts set balance = balance + ? where id = ?");
            statement.setFloat(1, amount);
            statement.setInt(2, accountNbr);
            statement.execute();

            balance += amount;
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return balance;
    }

    public List<IAccount> getAllAccounts() {
        List<IAccount> accounts = new ArrayList<IAccount>();
        
        openConnection();
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select id, name, balance from accounts");
            while (rs.next()) {
                accounts.add(map(rs));
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            closeConnection();
        }

        return accounts;
    }

    public float withdraw(int accountNbr, float amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
    	validateAmount(amount);
    	
        float balance = getAccountBalance(accountNbr);

        if (balance < amount) {
            throw new InsufficientFundsException();
        }

        openConnection();
        try {
            PreparedStatement statement = con.prepareStatement("update accounts set balance = balance - ? where id = ?");
            statement.setFloat(1, amount);
            statement.setInt(2, accountNbr);
            statement.execute();

            balance -= amount;
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return balance;
    }


    private Connection con;

    private void openConnection() {
        try {
            con = DriverManager.getConnection("jdbc:hsqldb:mem:.", "sa", "");
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    private void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float getAccountBalance(int accountNumber) throws AccountNotFoundException {
        float balance = 0;
        
        openConnection();
        try {
            PreparedStatement statement = con.prepareStatement("select balance from accounts where id=?");
            statement.setInt(1, accountNumber);
            ResultSet rs = statement.executeQuery();

            boolean isValid = rs.next();
            if (isValid) {
                balance = rs.getFloat(1);
            } else {
            	throw new AccountNotFoundException("Account number: " + accountNumber + " was not found");
            }

            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            closeConnection();
        }

        return balance;
    }



    private IAccount map(ResultSet rs) {
        IAccount account = null;

        try {
            account = new Account(rs.getInt(1), rs.getString(2), rs.getFloat(3));
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return account;
    }

    public float closeAccount(int accountNbr) throws AccountNotFoundException {
        float balance = getAccountBalance(accountNbr);

        openConnection();
        try {
            PreparedStatement statement = con.prepareStatement("delete from accounts where id=?");
            statement.setInt(1, accountNbr);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            closeConnection();
        }


        return balance;
    }

	public IAccount findAccount(int accountNbr) throws AccountNotFoundException {
        IAccount account = null;
        
        openConnection();
        try {
        	PreparedStatement statement = con.prepareStatement("select id, name, balance from accounts where id=?");
        	statement.setInt(1, accountNbr);
        	
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                account = map(rs);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            closeConnection();
        }
        
        if (account == null) {
        	throw new AccountNotFoundException("Account number: " + accountNbr + " was not found");
        }

        return account;
	}
}