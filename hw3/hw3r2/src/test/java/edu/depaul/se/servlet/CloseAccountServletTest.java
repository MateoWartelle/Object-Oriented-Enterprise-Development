package edu.depaul.se.servlet;

import edu.depaul.se.account.jpa.AccountManager;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

public class CloseAccountServletTest extends MockServletInitializer {
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private MockServletConfig config = new MockServletConfig();
	private CloseAccountServlet servlet = new CloseAccountServlet();

	@Before
	public void setup() throws Exception {
		servlet.init(config);
	}

	@Test
	public void invalidEntryError() throws Exception {
        request.setParameter("account", "hey jude");
		servlet.doGet(request, response);
		String result = response.getForwardedUrl();
		assertEquals("/InvalidDataEntry.jsp", result);
	}

	@Test
	public void invalidAccountError() throws Exception {
        request.setParameter("account", "-100");
		servlet.doGet(request, response);
		String result = response.getForwardedUrl();
		assertEquals("/AccountNotFound.jsp", result);
	}
	
	@Test
	public void validCloseAccount() throws Exception {
        AccountManager manager = new AccountManager();
        manager.createAccount("George", 100);
        manager.createAccount("Paul", 100);
        manager.createAccount("John", 100);
		// Validate that there are only 3 accounts
		int initialAccountCount = manager.getAllAccounts().size();
        
        request.setParameter("account", "100");
		servlet.doGet(request, response);
		String result = response.getForwardedUrl();
		assertEquals("/ShowAllAccounts.jsp", result);
		
		// Validate that after the results that there is one less
		assertEquals(initialAccountCount - 1, manager.getAllAccounts().size());
	}
}
