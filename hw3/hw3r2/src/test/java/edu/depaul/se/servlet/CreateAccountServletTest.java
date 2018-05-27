package edu.depaul.se.servlet;

import edu.depaul.se.account.IAccount;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import edu.depaul.se.account.jpa.AccountManager;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CreateAccountServletTest extends MockServletInitializer {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();
    private MockServletConfig config = new MockServletConfig();
    private CreateAccountServlet servlet = new CreateAccountServlet();

    @Before
    public void setup() throws Exception {
        servlet.init(config);
    }

    @Test
    public void invalidEntryError() throws Exception {
        request.setParameter("amount", "hey jude");
        servlet.doGet(request, response);
        String result = response.getForwardedUrl();
        assertEquals("/InvalidDataEntry.jsp", result);
    }

    @Test
    public void validCreateAccount() throws Exception {
        AccountManager manager = new AccountManager();
        manager.createAccount("George", 100);
        manager.createAccount("Paul", 100);
        manager.createAccount("John", 100);

        // Validate that there are only 3 accounts
        List<IAccount> initialAccounts = manager.getAllAccounts();
        int initialAccountCount = initialAccounts.size();

        String newAccountName = "Ringo";
        String initialAccountBalance = "100";

        request.setParameter("account", newAccountName);
        request.setParameter("amount", initialAccountBalance);
        servlet.doGet(request, response);
        String result = response.getForwardedUrl();
        assertEquals("/ShowAllAccounts.jsp", result);

        // Validate that after the results that there is one less
        List<IAccount> allAccounts = manager.getAllAccounts();
        assertEquals(initialAccountCount + 1, allAccounts.size());
        allAccounts.removeAll(initialAccounts);
        IAccount account = allAccounts.get(0); // There should only be one account left
        assertEquals(newAccountName, account.getName());
        assertEquals(Float.parseFloat(initialAccountBalance), account.getBalance(), 0.0);

    }
}
