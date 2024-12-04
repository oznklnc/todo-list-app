package com.demo.todo.list.app.test.suite;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.demo.todo.list.app")
@IncludeClassNamePatterns(".*IT")
public class IntegrationTestSuite {
}
