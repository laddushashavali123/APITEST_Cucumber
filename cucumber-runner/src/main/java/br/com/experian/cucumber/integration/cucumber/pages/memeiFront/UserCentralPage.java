package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class UserCentralPage extends BasePage<UserCentralPage> {

    //change name
    @Name("menu change name")
    @FindBy(xpath="//a[@title='Meus dados cadastrais']")
    private WebElement menuChangeName;

    @Name(" name field")
    @FindBy(id="nomeCompleto")
    private WebElement nameField;

    @Name(" send button")
    @FindBy(xpath="//input[@value='Enviar']")
    private WebElement sendBtn;

    @Name("Successfully changed data confirmation popup close button")
    @FindBy(xpath="//p[text()='Dados alterados com sucesso.']//following::span[text()='Fechar']")
    private WebElement closeBtnDataChangedConfirmation;


    //change email
    @Name("menu change email")
    @FindBy(xpath="//a[@title='Trocar endereço de e-mail']")
    private WebElement menuChangeEmail;

    @Name("current email field")
    @FindBy(id="email")
    private WebElement currentEmail;

    @Name("send token by sms Button")
    @FindBy(xpath="//*[text()='Enviar Token por SMS']")
    private WebElement sendTokenBySMSBtn;

    @Name(" token code field")
    @FindBy(id="token")
    private WebElement tokenCodeField;

    @Name(" ok button")
    @FindBy(xpath="//input[@value='OK']")
    private WebElement okBtn;

    @Name("Token confirmation popup close button")
    @FindBy(xpath="//p[text()='Token enviado com sucesso!']//following::span[text()='Fechar']")
    private WebElement closeBtnTokenConfirmation;

    @Name(" new email field")
    @FindBy(name="email")
    private WebElement newEmail;

    @Name(" new email confirmation field")
    @FindBy(name="confirmation")
    private WebElement newEmailConfirmation;

    @Name(" Update data button")
    @FindBy(xpath="//input[@value='Atualizar dados']")
    private WebElement UpdateDataBtn;

    @Name("change email confirmation popup close button")
    @FindBy(xpath="//p[text()='Email alterado com sucesso.']//following::span[text()='Fechar']")
    private WebElement closeBtnChangeEmailConfirmation;

    //change telefone
    @Name("menu change telephone")
    @FindBy(xpath="//a[@title='Trocar número de celular']")
    private WebElement menuChangeTelephone;

    @Name("send token by email Button")
    @FindBy(xpath="//*[text()='Enviar Token por e-mail']")
    private WebElement sendTokenByEmailBtn;

    @Name(" new telephone  field")
    @FindBy(id="novoCelular")
    private WebElement newTelephone;

    @Name(" new telephone confirmation field")
    @FindBy(id="confirmNovoCelular")
    private WebElement newTelephoneConfirmation;

    @Name("change email confirmation popup close button")
    @FindBy(xpath="//p[text()='Celular alterado com sucesso.']//following::span[text()='Fechar']")
    private WebElement closeBtnChangeTelephoneConfirmation;

    //change password
    @Name("menu change email")
    @FindBy(xpath="//a[@title='Trocar minha senha']")
    private WebElement menuChangePassword;

    @Name("current password field")
    @FindBy(name="oldPassword")
    private WebElement CurrentPassword;

    @Name("new password field")
    @FindBy(name="password")
    private WebElement newPassword;

    @Name("new password confirmation field")
    @FindBy(name="confirmation")
    private WebElement newPasswordConfirmation;

    @Name("change email confirmation popup close button")
    @FindBy(xpath="//p[text()='Senha alterada com sucesso.']//following::span[text()='Fechar']")
    private WebElement closeBtnChangePasswordConfirmation;

    public UserCentralPage clickSendTokenBySms() throws Throwable{
        Assert.assertTrue("Unable to click on send token by sms button", clickElement(sendTokenBySMSBtn));
        waitPageLoadEvents();
        return this;
    }

    public UserCentralPage clickTokenSentConfirmationPopup() throws Throwable{
        Assert.assertTrue("Unable to click on Token Sent Confirmation Popup close button", clickElement(closeBtnTokenConfirmation));
        return this;
    }

    public UserCentralPage setTokenCode(String token) throws Throwable{
        Assert.assertTrue("token field not found", sendKeysElement(this.tokenCodeField,token));
        return this;
    }

    public UserCentralPage clickOkButton() throws Throwable{
        Assert.assertTrue("Unable to click on ok button", clickElement(okBtn));
        waitPageLoadEvents();
        return this;
    }

    public UserCentralPage setEmail(String newEmail) throws Throwable{
        Assert.assertTrue("email field not found", sendKeysElement(this.newEmail,newEmail));
        return this;
    }
    public UserCentralPage setEmailConfirmation(String newEmail) throws Throwable{
        Assert.assertTrue("email confirmation field not found", sendKeysElement(this.newEmailConfirmation,newEmail));
        return this;
    }

    public UserCentralPage clickUpdateData() throws Throwable {
        Assert.assertTrue("Unable to click on update data button", clickElement(UpdateDataBtn));
        waitPageLoadEvents();
        return this;
    }

    public UserCentralPage clickEmailChangeConfirmationPopup() throws Throwable {
        Assert.assertTrue("Unable to click on Token Sent Confirmation Popup close button", clickElement(closeBtnChangeEmailConfirmation));
        return this;
    }

    public UserCentralPage clickSendTokenByEmail() throws Throwable{
        Assert.assertTrue("Unable to click on send token by email button", clickElement(sendTokenByEmailBtn));
        waitPageLoadEvents();
        return this;
    }

    public UserCentralPage setPassword(String password) throws Throwable{
        Assert.assertTrue("password field not found", sendKeysElement(this.CurrentPassword,password));
        return this;
    }

    public UserCentralPage setNewPassword(String newPassword) throws Throwable{
        Assert.assertTrue("new password field not found", sendKeysElement(this.newPassword,newPassword));
        return this;
    }
    public UserCentralPage setNewPasswordConfirmation(String newPasswordConfirmation) throws Throwable{
        Assert.assertTrue("new password confirmation field not found", sendKeysElement(this.newPasswordConfirmation,newPasswordConfirmation));
        return this;
    }

    public UserCentralPage clickPasswordChangeConfirmationPopup() throws Throwable {
        Assert.assertTrue("Unable to click on Token Sent Confirmation Popup close button", clickElement(closeBtnChangePasswordConfirmation));
        return this;
    }

    public UserCentralPage setNewTelephoneNumber(String newTelephone) throws Throwable{
        Assert.assertTrue("new telephone number field not found", sendKeysElement(this.newTelephone,newTelephone));
        return this;
    }

    public UserCentralPage setNewTelephoneNumberConfirmation(String newTelephoneConfirmation) throws Throwable{
        Assert.assertTrue("new telephone confirmation field not found", sendKeysElement(this.newTelephoneConfirmation,newTelephoneConfirmation));
        return this;
    }
    public UserCentralPage clickTelephoneChangeConfirmationPopup() throws Throwable {
        Assert.assertTrue("Unable to click on Token Sent Confirmation Popup close button", clickElement(closeBtnChangeTelephoneConfirmation));
        return this;
    }

    public UserCentralPage clearNameField() throws Throwable {
        Assert.assertTrue("Unable to clear the name field", clearText(nameField));
        return this;
    }
    public UserCentralPage SetName(String Name) throws Throwable {
        Assert.assertTrue("name field not found", sendKeysElement(this.nameField,Name));
        return this;
    }

    public UserCentralPage ClickSend() throws Throwable {
        Assert.assertTrue("Unable to click on send button", clickElement(sendBtn));
        return this;
    }

    public UserCentralPage clickNameChangeConfirmationPopup() throws Throwable {
        Assert.assertTrue("Unable to click on  Name change Confirmation Popup close button", clickElement(closeBtnDataChangedConfirmation));
        return this;
    }



    public UserCentralPage changeName(String name)throws Throwable{

        this.SetName(name)
                .ClickSend()
                .clickNameChangeConfirmationPopup();
        return this;

    }

    public UserCentralPage updateEmail(String newEmail, String tokenCode)throws Throwable{
        this.clickSendTokenBySms()
                .clickTokenSentConfirmationPopup()
                .setTokenCode(tokenCode)
                .clickOkButton();

        this.setEmail(newEmail)
                .setEmailConfirmation(newEmail)
                .clickUpdateData()
                .clickEmailChangeConfirmationPopup();

        return this;

    }

    public UserCentralPage updateTelephone(String tokenCode,String newNumber)throws Throwable{
        this.clickSendTokenByEmail()
                .clickTokenSentConfirmationPopup()
                .setTokenCode(tokenCode)
                .clickOkButton();

        this.setNewTelephoneNumber(newNumber)
                .setNewTelephoneNumberConfirmation(newNumber)
                .clickUpdateData()
                .clickTelephoneChangeConfirmationPopup();
        return this;

    }

    public UserCentralPage updatePassword(String tokenCode,String oldPassword,String newPassword)throws Throwable{
        this.clickSendTokenBySms()
                .clickTokenSentConfirmationPopup()
                .setTokenCode(tokenCode)
                .clickOkButton();

        this.setPassword(oldPassword)
            .setNewPassword(newPassword)
                .setNewPasswordConfirmation(newPassword)
                .clickUpdateData()
                .clickPasswordChangeConfirmationPopup();
        return this;

    }

}
