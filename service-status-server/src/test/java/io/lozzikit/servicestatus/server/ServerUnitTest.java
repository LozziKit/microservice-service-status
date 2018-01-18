package io.lozzikit.servicestatus.server;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/scenarios/unit/", plugin = {"pretty", "html:target/cucumber"})
public class ServerUnitTest {

}