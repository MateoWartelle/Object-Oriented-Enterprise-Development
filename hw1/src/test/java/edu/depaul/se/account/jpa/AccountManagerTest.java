/**
 *
 */
package edu.depaul.se.account.jpa;

import edu.depaul.se.account.AccountNotFoundException;
import edu.depaul.se.account.IAccount;
import edu.depaul.se.account.IAccountManager;
import edu.depaul.se.account.InsufficientFundsException;
import edu.depaul.se.account.InvalidAmountException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountManagerTest {

    @BeforeEach
    public void setup() {
        setAccountManager(new AccountManager());
    }
    
    /**
     * Initial account number where we start with auto create at this value
     */
    private static final int PAULS_ACCOUNT_NUMBER = 100;

    private static final int JOHNS_ACCOUNT_NUMBER = 101;

    private static final int GEORGES_ACCOUNT_NUMBER = 102;

    private static final int INVALID_ACCOUNT_NUMBER = -1;

    private IAccountManager accountManager;

    protected void setAccountManager(IAccountManager am) {
        accountManager = am;
    }

    /**
     * @throws java.lang.Exception
     */
    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @BeforeEach
    public void setupTable() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("create table accounts(id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 100) PRIMARY KEY, name varchar(50), balance float)");
            statement.executeUpdate("insert into accounts(name, balance) values ('Paul', 100)");
            statement.executeUpdate("insert into accounts(name, balance) values ('John', 100)");
            statement.executeUpdate("insert into accounts(name, balance) values ('George', 100)");
            statement.executeUpdate("insert into accounts(name, balance) values ('Ringo', 100)");
        } catch (SQLException sql) {
            System.out.println(sql.toString());
        }
    }

    @AfterEach
    public void removeTable() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("drop table accounts");
        } catch (SQLException sql) {
            System.out.println(sql.toString());
        }
    }

    private final String connectionUrl = "jdbc:hsqldb:mem:.";
    private final String userName = "sa";
    private final String password = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, userName, password);
    }

    /**
     * Test method for
     * {@link edu.depaul.se.account.IAccountManager#createAccount(java.lang.String, float)}
     * .
     *
     * @throws InvalidAmountException
     */
    @Test
    public void testCreateAccount() throws InvalidAmountException {
        int createdAccountNumber = accountManager.createAccount("Dave", 100);
        assertTrue(createdAccountNumber > 0);
    }

    @Test
    public void testCreateAccountWithNegativeBalance() {
        assertThrows(InvalidAmountException.class, () -> {
            accountManager.createAccount("Dave", -100);
        });
    }

    @Test
    public void findAccountWithInvalidAccount() {
        assertThrows(AccountNotFoundException.class, () -> {
            accountManager.findAccount(INVALID_ACCOUNT_NUMBER);
        });
    }

    @Test
    public void findAccount() throws Exception {
        IAccount account = accountManager.findAccount(PAULS_ACCOUNT_NUMBER);
        assertEquals("Paul", account.getName());
    }

    /**
     * Test method for
     * {@link edu.depaul.se.account.IAccountManager#getAllAccounts()}.
     */
    @Test
    public void testGetAllAccounts() {
        List<IAccount> accounts = accountManager.getAllAccounts();
        assertTrue(accounts.size() >= 4);
    }

    /**
     * Test method for
     * {@link edu.depaul.se.account.IAccountManager#deposit(int, float)}.
     *
     * @throws InvalidAmountException
     */
    @Test
    public void testDeposit() throws Exception {
        float newBalance = accountManager.deposit(PAULS_ACCOUNT_NUMBER, 10);
        assertEquals(110, newBalance);
    }

    @Test
    public void testDepositNegativeAmount() throws Exception {
        assertThrows(InvalidAmountException.class, () -> {
            accountManager.deposit(PAULS_ACCOUNT_NUMBER, -10);
        });
    }

    @Test
    public void testDepositAccountNotFound() throws Exception {
        assertThrows(AccountNotFoundException.class, () -> {
            accountManager.deposit(INVALID_ACCOUNT_NUMBER, 10);
        });
    }

    /**
     * Test method for
     * {@link edu.depaul.se.account.IAccountManager#withdraw(int, float)}.
     *
     * @throws InvalidAmountException
     */
    @Test
    public void testWithdraw() throws Exception {
        float newBalance = accountManager.withdraw(JOHNS_ACCOUNT_NUMBER, 10);
        assertEquals(90, newBalance);
    }

    @Test
    public void testWithdrawAccountNotFound() throws Exception {
        assertThrows(AccountNotFoundException.class, () -> {
            accountManager.withdraw(INVALID_ACCOUNT_NUMBER, 10);
        });
    }

    @Test
    public void testWithdrawInsufficientFund() throws Exception {
        assertThrows(InsufficientFundsException.class, () -> {
            accountManager.withdraw(JOHNS_ACCOUNT_NUMBER, 10000);
        });
    }

    @Test
    public void testWithdrawNegativeAmount() throws Exception {

        assertThrows(InvalidAmountException.class, () -> {
            accountManager.withdraw(JOHNS_ACCOUNT_NUMBER, -10);
        });
    }

    /**
     * Test method for
     * {@link edu.depaul.se.account.IAccountManager#closeAccount(int)}.
     */
    @Test
    public void testCloseAccount() throws AccountNotFoundException {
        float closingBalance = accountManager.closeAccount(GEORGES_ACCOUNT_NUMBER);
        assertEquals(100, closingBalance);
    }

    @Test
    public void testCloseAccountNotFound() throws Exception {
        assertThrows(AccountNotFoundException.class, () -> {
            accountManager.closeAccount(INVALID_ACCOUNT_NUMBER);
        });
    }

}
