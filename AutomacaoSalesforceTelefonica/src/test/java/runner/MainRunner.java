package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		dryRun = false,
		monochrome = true,
		features = {
				
		"src/test/features/Login.feature",				

				},
		glue = {"stepsDefinition"},
		
		tags = "@Regressao",
		plugin = {
				"pretty",
				})

public class MainRunner {

}