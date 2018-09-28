package br.com.experian.cucumber.integration.cucumber.steps.account;

import br.com.experian.cucumber.integration.cucumber.common.utils.DocumentsBrazil;
import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;
import br.com.experian.cucumber.integration.cucumber.definitions.BusinessAccountJson;
import br.com.experian.cucumber.integration.cucumber.steps.MainSteps;
import br.com.experian.cucumber.integration.cucumber.steps.RestAPISteps;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

import java.util.List;

public class BusinessAccountsSteps extends MainSteps {

    RestAPISteps restAPISteps = new RestAPISteps();

    @And("^user adds a company and user account with client \"(.*)\"$")
    public void userAddsACompanyAndUserAccount(String clientId) throws Throwable {
        restAPISteps.saveValue("client_id", clientId);
        restAPISteps.setRoute("/business-accounts/register-email?clientId=" + clientId);
        restAPISteps.pathToReport();
    }

    @And("^user adds a company and user account with predefined client$")
    public void userAddsACompanyAndUserAccountWithPredClient() throws Throwable {
        restAPISteps.setRoute("/business-accounts/register-email?clientId=" + RestApi.loadVariable("client_id"));
        restAPISteps.pathToReport();
    }

    @And("^user informs the following data for company to register email:$")
    public void userInformsTheFollowingDataForCompanyToRegisterEmail(List<List<String>> dataTable) throws Throwable {
        restAPISteps.fillVariablesAndSetBody(dataTable,BusinessAccountJson.jsonRegisterEmail());
    }

    @And("^user saves businessId and userId created$")
    public void userSavesBusinessIdAndUserIdCreated() throws Throwable {
        restAPISteps.saveJsonValue("/businessId" , "business_id");
        restAPISteps.saveJsonValue("/userId" , "user_id");
    }

    @When("^user deletes last business$")
    public void userDeletesLastBusiness() throws Throwable {
        restAPISteps.setRoute("/business-accounts/${business_id}");
        restAPISteps.deleteRequest();
    }

    @When("^user deletes last user$")
    public void userDeletesLastUser() throws Throwable {
        restAPISteps.setRoute("/user-accounts/${user_id}");
        restAPISteps.deleteRequest();
    }

    @And("^user informs the following data to invite user mobile:$")
    public void userInformsTheFollowingDataToInviteUserMobile(List<List<String>> dataTable) throws Throwable {
        restAPISteps.fillVariablesAndSetBody(dataTable,BusinessAccountJson.jsonInviteUserMobile());
    }

    @And("^user informs the following data to accept invitations:$")
    public void userInformsTheFollowingDataToAcceptInvitations(List<List<String>> dataTable) throws Throwable {
        restAPISteps.fillVariablesAndSetBody(dataTable,BusinessAccountJson.jsonAcceptInvitation());
    }

    @And("^user informs aleatory email$")
    public void userInformsAleatoryEmail() throws Throwable {
        restAPISteps.saveValue("email", "email" + DocumentsBrazil.aleatoryNumber(8)+"@teste.com");
    }

    @And("^user adds a company and user account$")
    public void userAddsACompanyAndUserAccount() throws Throwable {
        restAPISteps.setRoute("/business-accounts/register-email?clientId=" + RestApi.loadVariable("client_id"));
        restAPISteps.pathToReport();
    }
}
