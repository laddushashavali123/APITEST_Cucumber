package br.com.experian.cucumber.integration.cucumber.steps.selenium.memeiFront;

import br.com.experian.cucumber.integration.cucumber.pages.memeiFront.*;
import br.com.experian.cucumber.integration.cucumber.steps.Hooks;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Map;

public class MemeiFrontSteps {
    public Scenario scenario;

    private Home homePage;
    private CreatePasswordPage createPasswordPage;
    private LoggedHomePage loggedHomePage;
    private PersonaQuestionsPage personaQuestionsPage;
    private VideoTrailPage videoTrailPage;
    private VideotecaPage videotecaPage;
    private UserCentralPage userCentralPage;

    public MemeiFrontSteps() {
        scenario = Hooks.scenario;
        homePage = PageFactory.newInstance(Home.class);
        createPasswordPage = PageFactory.newInstance(CreatePasswordPage.class);
        loggedHomePage = PageFactory.newInstance(LoggedHomePage.class);
        personaQuestionsPage = PageFactory.newInstance(PersonaQuestionsPage.class);
        videoTrailPage = PageFactory.newInstance(VideoTrailPage.class);
        videotecaPage = PageFactory.newInstance(VideotecaPage.class);
        userCentralPage = PageFactory.newInstance(UserCentralPage.class);
    }

    @Given("^I open MEMEI$")
    public void openMemei() throws Throwable {
        homePage.openMemei("https://memei-memei-development.apps.appcanvas.net");
    }

    @Given("^I login on MEMEI with user \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void loginBPMAmbiente(String user, String password) throws Throwable {
        homePage.loginMemei(user, password);
    }

    @Given("^I logout from MEMEI$")
    public void logout() throws Throwable {
        loggedHomePage.clickOpenMenu()
                .clickLogoutMenuLink();
    }


    @When ("^I register a new user with$")
    public void registerUser(DataTable table) throws Throwable {
        for (Map<String, String> item : table.asMaps(String.class, String.class)) {
            homePage.registerNewUser(item.get("fullName"), item.get("email"), item.get("cpf"),
                    item.get("phone"), item.get("cnpj"));
        }
    }

    @Given ("^I set the validate email token as \"([^\"]*)\"$")
    public void setValidateEmailToken(String token) throws Throwable {
        createPasswordPage.sendValidateEmailToken(token);
    }

    @Given ("^I confirm the Register Password email popup$")
    public void confirmRegisterPasswordPopup() throws Throwable {
        createPasswordPage.clickOkBtn();
    }

    @Given ("^I set the user password as \"([^\"]*)\"$")
    public void setUserPassword(String password) throws Throwable {
        createPasswordPage.createUserPassword(password, true);
    }

    @When ("^I request a credit proposal$")
    public void requestCreditProposal() throws Throwable {
        loggedHomePage.requestCreditProposal("5000000", "1500000", "150000");
    }

    @When ("^I set the persona nickname as \"([^\"]*)\" and answer the questions with$")
    public void answerPersonaQuestions(String nickname, DataTable table) throws Throwable {
        loggedHomePage.clickDiscoverProfileBtn();
        personaQuestionsPage.startPersonaQuestionary(nickname);
        for (Map<String, String> item : table.asMaps(String.class, String.class)) {
            personaQuestionsPage.answerQuestion("0", item.get("answer_1"))
                .answerQuestion("1", item.get("answer_2"))
                .answerQuestion("2", item.get("answer_3"))
                .answerQuestion("3", item.get("answer_4"))
                .answerQuestion("4", item.get("answer_5"))
                .answerQuestion("5", item.get("answer_6"))
                .answerQuestion("6", item.get("answer_7"))
                .answerQuestion("7", item.get("answer_8"))
                .answerQuestion("8", item.get("answer_9"))
                .answerQuestion("9", item.get("answer_10"))
                .answerQuestion("10", item.get("answer_11"))
                .answerQuestion("11", item.get("answer_12"))
                .answerQuestion("12", item.get("answer_13"))
                .answerQuestion("13", item.get("answer_14"))
                .answerQuestion("14", item.get("answer_15"))
                .answerQuestion("15", item.get("answer_16"))
                .clickFinishQuestionary();
        }
    }

    @When ("^I watch the first trail video$")
    public void watchFirstTrailVideo() throws Throwable {
        loggedHomePage.clickDiscoverFreeVideoTrail();

        videoTrailPage.watchMovie()
                .rateVideo("4");
    }

    @Given ("^I go to the Discover Free Video Trail Page$")
    public void goToDiscoverFreeVideoTrail() throws Throwable {
        loggedHomePage.clickDiscoverFreeVideoTrail();
    }

    @When ("^I watch the trail video number \"([^\"]*)\" and rate it with \"([^\"]*)\" stars$")
    public void watchTrailVideo(String videoNum, String stars) throws Throwable {
        videoTrailPage.clickTrailVideo(videoNum)
                .watchMovie()
                .rateVideo(stars);
    }

    @When ("^I watch the first videoteca video$")
    public void watchFirstVideotecaMovie() throws Throwable {
        loggedHomePage.clickAllVideos();

        videotecaPage.clickWatchFirstVideoBtn()
                .watchMovie()
                .rateVideo("5");

    }

    @When("^I go to the home page$")
    public void goToHomePage() throws Throwable {
        homePage.clickHomeLogo();
    }

    @When("^I change my email to \"([^\"]*)\"$")
    public void changeEmail(String newEmail) throws Throwable {
        loggedHomePage.clickOpenMenu()
                .clickChangeEmail();
        userCentralPage.updateEmail(newEmail, "123456");

    }

    @When("^I change my Cell Phone number to \"([^\"]*)\"$")
    public void changeCellNumber(String newNumber) throws Throwable {
        loggedHomePage.clickOpenMenu()
                .clickChangePhoneNumber();
        userCentralPage.updateTelephone("123456",newNumber);
    }

    @When("^I change my Password from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void changePassword(String oldPassword, String newPassword) throws Throwable {
        loggedHomePage.clickOpenMenu()
                .clickChangePassword();
        userCentralPage.updatePassword("123456",oldPassword,newPassword);
    }

    @When("^I change my name to \"([^\"]*)\"$")
    public void changeName(String name)throws Throwable {
        loggedHomePage.clickOpenMenu()
                .clickChangeMyData();
        userCentralPage.changeName(name);
    }


    //Assertions
    @Then  ("^I validate the user is already registered message appears$")
    public void validateUserAlreadyRegisteredLabel() throws Throwable {
        homePage.validateUserAlreadyRegisteredLabel();
    }

    @Then("^I assert that the email is \"([^\"]*)\"$")
    public void validateEmail(String email) throws Throwable{
        loggedHomePage.verifyEmail(email);
    }

    @Then("^I assert that the URL contains \"([^\"]*)\"$")
    public void verifyUrlContains(String text) throws Throwable{
        homePage.verifyUrlContains(text);
    }

    @Then("^I assert that the profile is \"([^\"]*)\"$")
    public void validateProfile(String profile) throws Throwable{
        loggedHomePage.validateProfileLabel(profile);
    }


}

