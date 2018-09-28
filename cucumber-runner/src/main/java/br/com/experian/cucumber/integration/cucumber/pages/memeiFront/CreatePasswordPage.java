package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class CreatePasswordPage  extends BasePage<CreatePasswordPage> {

    //Cadastrar Senha
    @Name("Insert Token")
    @FindBy(id="fieldToken")
    private WebElement fieldToken;

    @Name("Insert Token Ok button")
    @FindBy(xpath = "//button[@id='ok' or @id='validaToken']")
    private WebElement okBtn;

    @Name("Resend token by email")
    @FindBy(xpath="//a[@class='btn-border-dark-blue']")
    private WebElement resendTokenBtn;

    @Name("Password")
    @FindBy(xpath="//*[@id='define-password']//input[@name='password']")
    private WebElement firstPassword;

    @Name("Password Confirmation")
    @FindBy(xpath="//*[@id='define-password']//input[@name='confirmation']")
    private WebElement passwordConfirmation;

    @Name("Remember Me")
    @FindBy(xpath="//label[@for='remerberMe']")
    private WebElement rememberMeChk;

    @Name("Accept terms")
    @FindBy(xpath="//label[@for='accept']")
    private WebElement acceptTermsChk;

    @Name("Close terms")
    @FindBy(xpath = "//button[@class='close']")
    private WebElement closeTermsBtn;

    @Name("Save data Button")
    @FindBy(id="salvarDadosIniciarQuestironario")
    private WebElement saveDataBtn;

    public CreatePasswordPage setFieldToken(String token) throws Throwable{
        Assert.assertTrue("Password field not found", sendKeysElement(fieldToken, token));
        return this;
    }

    public CreatePasswordPage clickOkBtn() throws Throwable{
        Assert.assertTrue("Ok button not found", clickElement(okBtn));
        waitPageLoadEvents();
        return this;
    }

    public CreatePasswordPage clickResendTokenBtn() throws Throwable{
        Assert.assertTrue("Resend Token Button not found", clickElement(resendTokenBtn));
        return this;
    }

    public CreatePasswordPage setFirstPassword(String pwd) throws Throwable{
        Assert.assertTrue("Password field not found", sendKeysElement(firstPassword, pwd));
        return this;
    }

    public CreatePasswordPage setFirstPasswordConfirmation(String pwd) throws Throwable{
        Assert.assertTrue("Password Confirmation field not found", sendKeysElement(passwordConfirmation, pwd));
        return this;
    }

    public CreatePasswordPage clickRememberMe() throws Throwable{
        Assert.assertTrue("Remember me checkbox not found", clickElement(rememberMeChk));
        return this;
    }

    public CreatePasswordPage clickAcceptTerms() throws Throwable{
        Assert.assertTrue("Accept Terms checkbox not found", clickElement(acceptTermsChk));
        return this;
    }

    public CreatePasswordPage clickCloseTermsBtn() throws Throwable{
        Assert.assertTrue("Accept Terms checkbox not found", clickElement(closeTermsBtn));
        return this;
    }

    public CreatePasswordPage clickSaveData() throws Throwable{
        Assert.assertTrue("Save Data button not found", clickElement(saveDataBtn));
        return this;
    }

    public CreatePasswordPage sendValidateEmailToken(String token) throws Throwable {
        this.setFieldToken(token)
                .screenShot();
        this.clickOkBtn();
        return this;
    }

    public CreatePasswordPage createUserPassword(String pwd, Boolean rememberMe) throws Throwable {

        this.clickAcceptTerms();
        if(closeTermsBtn.isEnabled()) this.clickCloseTermsBtn();
        if(rememberMe) this.clickRememberMe();

        this.setFirstPassword(pwd)
            .setFirstPasswordConfirmation(pwd)
            .screenShot();

        this.clickSaveData()
            .waitPageLoadEvents()
            .screenShot();

        //this.waitElementXpath("//input[@id='descobrirPerfil']");
        return this;
    }

}

