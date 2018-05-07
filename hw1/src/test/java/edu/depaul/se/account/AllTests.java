package edu.depaul.se.account;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@SelectClasses({edu.depaul.se.account.jdbc.AccountManagerTest.class, edu.depaul.se.account.jdbc.AccountManagerTest.class})
public class AllTests {

}
