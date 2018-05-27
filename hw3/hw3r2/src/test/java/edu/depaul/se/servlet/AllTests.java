package edu.depaul.se.servlet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CloseAccountServletTest.class, CreateAccountServletTest.class,
		DepositAccountServletTest.class, WithdrawAccountServletTest.class })
public class AllTests {

}
