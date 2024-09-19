package com.vlasevsky.gym.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com.vlasevsky.gym.features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.vlasevsky.gym.bdd.steps")
public class RunCucumberTest {
}
