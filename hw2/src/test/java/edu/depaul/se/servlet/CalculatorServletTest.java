package edu.depaul.se.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CalculatorServletTest {
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private CalculatorServlet servlet = new CalculatorServlet();

	@Test
	public void testInvalidLHS() throws Exception {
        request.setParameter("lhs", "Foo");
        request.setParameter("rhs", "2");
        request.setParameter("operator", "+");
		servlet.doGet(request, response);
		String errorMessage="please enter only numeric values";
		
		Assert.assertTrue("LHS non-numeric", response.getContentAsString().contains(errorMessage));
	}

	@Test
	public void testInvalidRHS() throws Exception {
        request.setParameter("lhs", "2");
        request.setParameter("rhs", "RHS");
        request.setParameter("operator", "+");
		servlet.doGet(request, response);
		String errorMessage="please enter only numeric values";
		
		Assert.assertTrue("RHS non-numeric", response.getContentAsString().contains(errorMessage));
	}
	
	@Test
	public void testAddition() throws Exception {
        request.setParameter("lhs", "2");
        request.setParameter("rhs", "2");
        request.setParameter("operator", "+");
		servlet.doGet(request, response);
		String result="=4";
		
		Assert.assertTrue("Addition", response.getContentAsString().contains(result));
	}

	@Test
	public void testSubtraction() throws Exception {
        request.setParameter("lhs", "4");
        request.setParameter("rhs", "2");
        request.setParameter("operator", "-");
		servlet.doGet(request, response);
		String result="=2";
		
		Assert.assertTrue("Subtraction", response.getContentAsString().contains(result));
	}

	@Test
	public void testMultiplication() throws Exception {
        request.setParameter("lhs", "4");
        request.setParameter("rhs", "2");
        request.setParameter("operator", "*");
		servlet.doGet(request, response);
		String result="=8";
		
		Assert.assertTrue("Multiplication", response.getContentAsString().contains(result));
	}

	@Test
	public void testDivision() throws Exception {
        request.setParameter("lhs", "4");
        request.setParameter("rhs", "2");
        request.setParameter("operator", "/");
		servlet.doGet(request, response);
		String result="=2";
		
		Assert.assertTrue("Division", response.getContentAsString().contains(result));
	}
	
}
