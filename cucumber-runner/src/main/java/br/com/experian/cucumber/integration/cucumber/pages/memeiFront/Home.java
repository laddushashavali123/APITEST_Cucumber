package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class Home extends BasePage<Home> {
    @Name("Full Name")
    @FindBy(id="nomeCompleto")
    private WebElement fullName;

    @Name("email")
    @FindBy(id="email")
    private WebElement email;

    @Name("cpf")
    @FindBy(id="cpf")
    private WebElement cpf;

    @Name("phoneNumber")
    @FindBy(id="celular")
    private WebElement phoneNumber;


    @Name("Has no CNPJ check button")
    @FindBy(xpath="(//input[@name='temCnpj'])[2]")
    private WebElement hasNoCnpjChk;

    @Name("cnpj")
    @FindBy(id="cnpj")
    private WebElement cnpj;

    @Name("Register Button")
    @FindBy(xpath="//button[@id='cadastreSeHome' or @id ='continuarCadatroTamoJunto']")
    private WebElement regiterBtn;

    @Name("Cadastrar Senha")
    @FindBy(id="ok")
    private WebElement okBtn;

    @Name("User already registered")
    @FindBy(xpath="//p[text()='Usuário já cadastrado!']")
    private WebElement userRegisteredLabel;


    public Home setFullName(String fullName) throws Throwable{
        Assert.assertTrue("fullName field not found", sendKeysElement(this.fullName, fullName));
        return this;
    }

    public Home setEmail(String email) throws Throwable{
        Assert.assertTrue("Email field not found", sendKeysElement(this.email, email));
        return this;
    }

    public Home setCpf(String cpf) throws Throwable{
        Assert.assertTrue("fullName field not found", sendKeysElement(this.cpf, cpf));
        return this;
    }

    public Home setPhoneNumber(String phoneNumber) throws Throwable{
        Assert.assertTrue("phoneNumber field not found", sendKeysElement(this.phoneNumber, phoneNumber));
        return this;
    }

    public Home clickHasNoCnpjChk() throws Throwable{
        Assert.assertTrue("No CNPJ option not found", clickElement(hasNoCnpjChk));
        return this;
    }


    public Home setCNPJ(String cnpj) throws Throwable{
        Assert.assertTrue("CNPJ field not found", sendKeysElement(this.cnpj, cnpj));
        return this;
    }

    public Home clickRegisterBtn() throws Throwable{
        Assert.assertTrue("Register button not found", clickElement(regiterBtn));
        return this;
    }

    public Home clickRegisterPaswordOkBtn() throws Throwable {
        clickElement(okBtn, 5);
        return this;
    }


    public Home loginMemei(String user, String password) throws Throwable {
        this.setUsername(user)
                .setPassword(password)
                .screenShot()
                .clickAccessBtn()
                .waitPageLoadEvents();
        return this;
    }

    public Home registerNewUser(String fullName, String email, String cpf, String phone, String cnpj)  throws Throwable {
        this.setFullName(fullName)
                .setEmail(email)
                .setCpf(cpf)
                .setPhoneNumber(phone);

        if ("no".equals(cnpj.toLowerCase())) {
            clickHasNoCnpjChk();
        }else{
            this.setCNPJ(cnpj);
        }

        screenShot();

        this.clickRegisterBtn();

        this.waitPageLoadEvents();
        screenShot();

        return this;
    }

    public Home validateUserAlreadyRegisteredLabel() throws Throwable {
        Assert.assertTrue("User Already Registered Label not Found", waitElement(userRegisteredLabel));
        screenShot();
        return this;
    }

}
