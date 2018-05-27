package edu.depaul.se.account;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@SelectPackages("edu.depaul.se.account.jdbc")
public class AllTests {

}
