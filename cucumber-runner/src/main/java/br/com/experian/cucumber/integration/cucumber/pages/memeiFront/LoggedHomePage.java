package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class LoggedHomePage extends BasePage<LoggedHomePage> {

    //persona
    @Name("Discover Profile Button")
    @FindBy(id="descobrirPerfil")
    private WebElement discoverProfileBtn;

    //videos
    @Name("Discover Free Video Trail Button")
    @FindBy(xpath="//a[@id='conhecerTrilhaGratuita' or @id='conhecerTrilhaPersonalizada']")
    private WebElement discoverFreeVideoTrailBtn;

    @Name("Videoteca")
    @FindBy(xpath="//a[@class='simple-link']//span[contains(text(),'videoteca')]")
    private WebElement allVideosLink;

    //credita
    @Name("Start Credita")
    @FindBy(xpath="//span[contains(text(),'Comece agora')]")
    private WebElement startCredita;

    @Name("Credit Text Box")
    @FindBy(xpath="//input[@type='text']")
    private WebElement creditTextBox;

    @Name("Next Steo")
    @FindBy(xpath = "//span[text()='Próxima']")
    private WebElement nextStepCredit;

    @Name("Credit Need Fluxo de caixa")
    @FindBy(xpath = "//*[@for='ckb01']")
    private WebElement moneyFlowChk;

    @Name("Credit important Valor total do empréstimo")
    @FindBy(xpath = "//*[@for='ckb06']")
    private WebElement importantTotalChk;

    @Name("Accept Terms")
    @FindBy(xpath = "//*[@for='ckb13']")
    private WebElement acceptTermsChk;

    @Name("Final Credit")
    @FindBy(id = "questionarioCreditaStepFinal")
    private WebElement sendCreditProposalBtn;

    @Name("Ok Button")
    @FindBy(xpath = "//button//span[text()='Ok']")
    private WebElement okBtn;

    @Name("Profile")
    @FindBy(xpath="//div[contains(@class, 'user-perfil-info')]//h1")
    private WebElement profileLabel;



    public LoggedHomePage clickDiscoverProfileBtn() throws Throwable{
        Assert.assertTrue("Discover Profile button not found", clickElement(discoverProfileBtn));
        waitPageLoadEvents();
        return this;
    }

    //videoteca
    public LoggedHomePage clickDiscoverFreeVideoTrail() throws Throwable{
        Assert.assertTrue("Discover Free Video Trail button not found", clickElement(discoverFreeVideoTrailBtn));
        waitPageLoadEvents();
        return this;
    }

    public LoggedHomePage clickAllVideos() throws Throwable{
        Assert.assertTrue("Videoteca link not found", clickElement(allVideosLink));
        return this;
    }


    //credita

    public LoggedHomePage clickStartCredita() throws Throwable{
        Assert.assertTrue("Start Credit line link not found", clickElement(startCredita));
        return this;
    }

    public LoggedHomePage clickNextStepCredit() throws Throwable{
        Assert.assertTrue("Next Step Credit not found", clickElement(nextStepCredit));
        return this;
    }


    public LoggedHomePage setMonthlyRevenue(String monthlyRevenue) throws Throwable{
        Assert.assertTrue("Monthly Revenue field not found", sendKeysElement(this.creditTextBox, monthlyRevenue));
        return this;
    }

    public LoggedHomePage clickMoneyFlowChk() throws Throwable{
        Assert.assertTrue("Money Flow option not found", clickElement(moneyFlowChk));
        return this;
    }

    public LoggedHomePage setNeededValue(String neededValue) throws Throwable{
        Assert.assertTrue("Monthly Revenue field not found", sendKeysElement(this.creditTextBox, neededValue));
        return this;
    }

    public LoggedHomePage setMonthlyPortion(String monthlyPortion) throws Throwable{
        Assert.assertTrue("Monthly Revenue field not found", sendKeysElement(this.creditTextBox, monthlyPortion));
        return this;
    }

    public LoggedHomePage clickImportantTotalChk() throws Throwable{
        Assert.assertTrue("Important on Credit time option not found", clickElement(importantTotalChk));
        return this;
    }

    public LoggedHomePage clickAcceptTermsChk() throws Throwable{
        Assert.assertTrue("Accept Terms Check not found", clickElement(acceptTermsChk));
        return this;
    }

    public LoggedHomePage clickSendCreditProposalBtn() throws Throwable{
        Assert.assertTrue("Send proposal Button not found", clickElement(sendCreditProposalBtn));
        return this;
    }

    public LoggedHomePage clickOkBtn() throws Throwable{
        Assert.assertTrue("Ok Button not found", clickElement(okBtn));
        return this;
    }

    //credita
    public LoggedHomePage requestCreditProposal(String monthlyRevenue, String neededValue, String monthlyPortion) throws Throwable{
        this.clickStartCredita()
                .setMonthlyRevenue(monthlyRevenue)
                .clickNextStepCredit()
                .clickMoneyFlowChk()
                .clickNextStepCredit()
                .setNeededValue(neededValue)
                .clickNextStepCredit()
                .setMonthlyPortion(monthlyPortion)
                .clickNextStepCredit()
                .clickImportantTotalChk()
                .clickAcceptTermsChk()
                .screenShot();
        this.clickSendCreditProposalBtn()
                //.clickOkBtn()
                .waitPageLoadEvents();

        this.screenShot();
        return this;
    }

    public LoggedHomePage validateProfileLabel(String expectedProfile) throws Throwable {
        Assert.assertTrue("Unable to verify profile " + expectedProfile,validateElementText(this.profileLabel,expectedProfile));
        return this;
    }


}
