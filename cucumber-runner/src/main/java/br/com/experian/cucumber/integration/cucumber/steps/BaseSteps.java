package br.com.experian.cucumber.integration.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class BaseSteps extends MainSteps {

    RestAPISteps restAPISteps = new RestAPISteps();

    @When("^user saves data$")
    public void userSavesData() throws Throwable {
        restAPISteps.postRequest();
    }

    @Given("^user has permission$")
    public void userHasPermission() throws Throwable {
        restAPISteps.setBearerAuthHeader();
    }
}
