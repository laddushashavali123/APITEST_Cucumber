package br.com.experian.cucumber.integration.cucumber.steps.account;

import br.com.experian.cucumber.integration.cucumber.common.utils.DocumentsBrazil;
import br.com.experian.cucumber.integration.cucumber.definitions.UserAccountJson;
import br.com.experian.cucumber.integration.cucumber.steps.MainSteps;
import br.com.experian.cucumber.integration.cucumber.steps.RestAPISteps;
import cucumber.api.java.en.And;

import java.util.List;

public class UserAccountsSteps extends MainSteps {

    RestAPISteps restAPISteps = new RestAPISteps();

    @And("^user informs new phone number$")
    public void userInformsNewPhoneNumber() throws Throwable {
        restAPISteps.saveValue("phoneNumber", "9"+DocumentsBrazil.aleatoryNumber(8));
    }

    @And("^user informs the following data for user to register email:$")
    public void userInformsTheFollowingDataForUserToRegisterEmail(List<List<String>> dataTable) throws Throwable {
        restAPISteps.fillVariablesAndSetBody(dataTable, UserAccountJson.jsonRegisterEmail());
    }
}
