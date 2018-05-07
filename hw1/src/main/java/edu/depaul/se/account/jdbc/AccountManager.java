package edu.depaul.se.account.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.depaul.se.account.AccountNotFoundException;
import edu.depaul.se.account.IAccount;
import edu.depaul.se.account.IAccountManager;
import edu.depaul.se.account.InsufficientFundsException;
import edu.depaul.se.account.InvalidAmountException;

/**
 * Implementation of solution using JDBC query 1) to create an account (insert
 * into account table) 2) to close an account (delete from account table) 3) to
 * withdraw or deposit to an account (update an account table) 4) to list all
 * accounts (get all)
 */
public class AccountManager implements IAccountManager {

	@Override
	public int createAccount(String name, float initialBalance) throws InvalidAmountException {
		int id = 0;
		Connection dbConnection = null;
		if (initialBalance < 0) {
			throw new InvalidAmountException();
		}
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String insertTableSQL = "INSERT INTO accounts" + "(name, balance) VALUES" + "(?,?)";
			PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, name);
			preparedStatement.setFloat(2, initialBalance);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			String accountNumber = "SELECT id from accounts WHERE name = ?";
			PreparedStatement newAccount = dbConnection.prepareStatement(accountNumber);
			newAccount.setString(1, name);
			ResultSet resultSet = newAccount.executeQuery();
			while (resultSet.next()) {
				id = resultSet.getInt(1);
			}
			newAccount.close();
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}

		}
		return id;
	}

	@Override
	public float deposit(int accountNbr, float amount) throws AccountNotFoundException, InvalidAmountException {
		float initialAmount = 0;
		float combinedAmount = 0;
		Connection dbConnection = null;
		if (amount < 0) {
			throw new InvalidAmountException();
		}
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String getExistingAmount = "SELECT BALANCE from accounts WHERE id = ?";
			PreparedStatement preparedStatement = dbConnection.prepareStatement(getExistingAmount);
			preparedStatement.setInt(1, accountNbr);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new AccountNotFoundException(String.valueOf(accountNbr));
			} else {
				initialAmount = resultSet.getFloat(1);
				preparedStatement.close();
			}
			combinedAmount = initialAmount + amount;
			String depositTableSQL = "UPDATE accounts SET BALANCE = ? WHERE id = ?";
			PreparedStatement preparedStatement2 = dbConnection.prepareStatement(depositTableSQL);
			preparedStatement2.setFloat(1, combinedAmount);
			preparedStatement2.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}

		}
		return combinedAmount;
	}

	@Override
	public List<IAccount> getAllAccounts() {
		List<IAccount> listOfAccounts = new ArrayList<IAccount>();
		int id = 0;
		String name = "";
		float balance = 0.0f;
		Connection dbConnection = null;
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String accountNumber = "SELECT ID, NAME, BALANCE from accounts";
			PreparedStatement newAccount = dbConnection.prepareStatement(accountNumber);
			ResultSet resultSet = newAccount.executeQuery();
			while (resultSet.next()) {
				IAccount UserAccount = new Account();
				id = resultSet.getInt(1);
				name = resultSet.getString(2);
				balance = resultSet.getFloat(3);
				UserAccount.setBalance(balance);
				UserAccount.setId(id);
				UserAccount.setName(name);
				listOfAccounts.add(UserAccount);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}

		}
		return listOfAccounts;
	}

	@Override
	public float withdraw(int accountNbr, float amount)
			throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
		float initialAmount = 0;
		float combinedAmount = 0;
		Connection dbConnection = null;
		if (amount < 0) {
			throw new InvalidAmountException();
		}
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String getExistingAmount = "SELECT BALANCE from accounts WHERE id = ?";
			PreparedStatement preparedStatement = dbConnection.prepareStatement(getExistingAmount);
			preparedStatement.setInt(1, accountNbr);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new AccountNotFoundException(String.valueOf(accountNbr));
			} else {
				initialAmount = resultSet.getFloat(1);
			}
			combinedAmount = initialAmount - amount;
			if (combinedAmount < 0) {
				throw new InsufficientFundsException();
			}
			String depositTableSQL = "UPDATE accounts SET BALANCE = ? WHERE id = ?";
			PreparedStatement preparedStatement2 = dbConnection.prepareStatement(depositTableSQL);
			preparedStatement2.setFloat(1, combinedAmount);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}

		}
		return combinedAmount;
	}

	public float closeAccount(int accountNbr) throws AccountNotFoundException {
		float closeOutAmount = 0;
		Connection dbConnection = null;
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String getAccountNbr = "SELECT BALANCE from accounts WHERE id = ?";
			PreparedStatement preparedStatement = dbConnection.prepareStatement(getAccountNbr);
			preparedStatement.setInt(1, accountNbr);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new AccountNotFoundException(String.valueOf(accountNbr));
			} else {
				closeOutAmount = resultSet.getFloat(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}
		}

		return closeOutAmount;
	}

	public IAccount findAccount(int accountNbr) throws AccountNotFoundException {
		IAccount userAccount = new Account();
		int id = 0;
		String name = "";
		float balance = 0.0f;
		Connection dbConnection = null;
		try {
			dbConnection = HSQLDBMemDriverHelper.getConnection();
			String accountNumber = "SELECT ID, NAME, BALANCE from accounts WHERE id = ?";
			PreparedStatement newAccount = dbConnection.prepareStatement(accountNumber);
			newAccount.setInt(1, accountNbr);
			ResultSet resultSet = newAccount.executeQuery();
			if (!resultSet.next()) {
				throw new AccountNotFoundException(String.valueOf(accountNbr));
			}

			id = resultSet.getInt(1);
			name = resultSet.getString(2);
			balance = resultSet.getFloat(3);

			userAccount.setBalance(balance);
			userAccount.setId(id);
			userAccount.setName(name);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			if (dbConnection.equals(null)) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					e.getStackTrace();
				}
			}
		}

		return userAccount;
	}

}
