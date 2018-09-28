package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import br.com.experian.cucumber.integration.cucumber.steps.Hooks;
import br.com.experian.cucumber.integration.cucumber.common.utils.js.JavaScriptWait;

import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;

public abstract class BasePage<T extends BasePage<T>> {

    // force check in
    protected WebDriver driver;
    protected Wait<WebDriver> wait;
    protected JavaScriptWait javascriptWait;
    private int globalTimeout = 10;

    @Name("Link to remove certificate error for IE")
    @FindBy(css = "#overridelink")
    private WebElement certificate;

    @Name("Home Serasa Logo")
    @FindBy(xpath="//img[@alt='Serasa Experian']")
    private WebElement homeLogo;

    @Name("Username")
    @FindBy(name="username")
    private WebElement username;

    @Name("Password")
    @FindBy(name="password")
    private WebElement password;

    @Name("Access Login Button")
    @FindBy(xpath="//input[@value='Acessar']")
    private WebElement accessBtn;

    @Name("Lost Password")
    @FindBy(xpath="//a[@class='lost-pass']")
    private WebElement lostPass;

    @Name("email")
    @FindBy(xpath="//span[@class='user-email text-truncate']")
    private WebElement email;

    @Name("Menu")
    @FindBy(xpath="//a[@title='menu']")
    private WebElement menuLink;

    @Name("Change my data menu item")
    @FindBy(xpath="//a[@title='Meus dados']")
    private WebElement changeMyDataMenuLink;

    @Name("Change email menu item")
    @FindBy(xpath="//a[@title='Trocar e-mail']")
    private WebElement changeEmailMenuLink;

    @Name("Change email menu item")
    @FindBy(xpath="//a[@title='Trocar número de celular']")
    private WebElement changePhoneNumberMenuLink;

    @Name("Change email menu item")
    @FindBy(xpath="//a[@title='Trocar minha senha']")
    private WebElement changePasswordMenuLink;

    @Name("Logout menu item")
    @FindBy(xpath="//a[@title='Sair']")
    private WebElement logoutMenuLink;

    @Name("Wait frame")
    @FindBy(xpath="//*[contains(text(), 'Aguarde um instante...')]")
    private WebElement waitFrame;

    public BasePage() {
        this.driver = Hooks.getDriver();
        this.wait = Hooks.getWait();
        this.javascriptWait = new JavaScriptWait(driver, wait);
    }

    @SuppressWarnings("unchecked")
    public T then() {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T with() {
        return (T) this;
    }

    public T get(String url) {
        driver.get(url);
        return get();
    }

    @SuppressWarnings("unchecked")
    public T get() {
        // cria a page object
        HtmlElementLoader.populatePageObject(this, driver);
        javascriptWait.waitJsEventOnLoad();
        return (T) this;
    }

    public void closeBrowser(){
        Hooks.closeBrowser();
    }

    public void clickCertificate() {
        try {
            if (certificate.isDisplayed()) {
                certificate.click();
                clickCertificateDialog();
            }
        } catch (Exception e) {
        }
    }

    public void clickCertificateDialog() {
        try {
            Thread.sleep(3000);
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert is present");
            alert.accept();
        } catch (Exception e) {
            System.out.println("alert is not present");
        }
    }

    public BasePage openMemei(String url) throws Throwable {
        try{
            driver.get(url);
            waitPageLoadEvents();
        }catch(WebDriverException e){
        }

        return this;
    }

    public BasePage clickHomeLogo() throws Throwable{
        Assert.assertTrue("Home button not found", clickElement(homeLogo));
        waitPageLoadEvents();
        return this;
    }

    public BasePage setUsername(String user) throws Throwable{
        Assert.assertTrue("Username field not found", sendKeysElement(username, user));
        return this;
    }

    public BasePage setPassword(String pwd) throws Throwable{
        Assert.assertTrue("Password field not found", sendKeysElement(password, pwd));
        return this;
    }

    public BasePage clickAccessBtn() throws Throwable{
        Assert.assertTrue("Access button not found", clickElement(accessBtn));
        return this;
    }

    public BasePage clickLostPassword() throws Throwable{
        Assert.assertTrue("Lost Password link not found", clickElement(lostPass));
        return this;
    }

    public BasePage clickOpenMenu() throws Throwable{
        Assert.assertTrue("Menu not found", clickElement(menuLink));
        return this;
    }

    public BasePage clickChangeEmail() throws Throwable{
        Assert.assertTrue("Change email link not found", clickElement(changeEmailMenuLink));
        waitPageLoadEvents();
        return this;
    }

    public BasePage clickChangePhoneNumber() throws Throwable{
        Assert.assertTrue("Change cell phone number link not found", clickElement(changePhoneNumberMenuLink));
        waitPageLoadEvents();
        return this;
    }

    public BasePage clickChangePassword() throws Throwable{
        Assert.assertTrue("Change Password link not found", clickElement(changePasswordMenuLink));
        waitPageLoadEvents();
        return this;
    }

    public BasePage clickLogoutMenuLink() throws Throwable{
        Assert.assertTrue("Change Password link not found", clickElement(logoutMenuLink));
        waitPageLoadEvents();
        return this;
    }

    public BasePage clickChangeMyData() throws Throwable{
        Assert.assertTrue("Change my Data link not found", clickElement(changeMyDataMenuLink));
        waitPageLoadEvents();
        return this;
    }

    public BasePage waitFrameToVanish() {
        try{
            waitElementVanish(waitFrame);
        }catch (Throwable t){
        }
        return this;
    }

    public void changeFrame(WebElement frameWebElement) {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameWebElement);
    }

    public BasePage waitPageLoadEvents() {
        javascriptWait.waitForDocumentCompleteLoad();
        javascriptWait.waitJsEventOnLoad();
        this.waitFrameToVanish();
        return this;
    }

    protected String getTitle() {
        return driver.getTitle();
    }

    protected Object executeJS(String javascript) {
        Object returnObj = null;
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        try {
            returnObj = jsExecutor.executeScript(javascript);
        } catch (Exception e) {
            System.err.println("Erro ao executar o javascript");
        }
        return returnObj;
    }

    public boolean validateSelectedElement(WebElement element) throws Throwable {

        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                if (element.isSelected())
                    return true;
                return false;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        throw new ElementNotVisibleException("element " + element + " nao encontrado");
    }

    public Boolean waitElement(WebElement element) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                result = true;
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }

    public Boolean waitElementVanish(WebElement element) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                Thread.sleep(1000);
            } catch (Exception e) {
                result = true;
                break;
            }
        }
        return result;
    }


    public Boolean validateElementText(WebElement element, String text) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                String elementText = element.getText();
                Assert.assertEquals(text, elementText);
                result = true;
                break;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }


    public Boolean waitElementXpath(String xpath) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                driver.findElement(By.xpath(xpath)).isDisplayed();
                result = true;
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }

    public Boolean clickElement(WebElement element) throws Throwable {
        return clickElement(element, globalTimeout);
    }

    public Boolean clickElement(WebElement element, int timeout) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < timeout; i++) {
            try {
                element.isEnabled();
                element.click();
                result = true;
                break;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }


    public Boolean clickXpath(String xpath) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                driver.findElement(By.xpath(xpath)).isEnabled();
                driver.findElement(By.xpath(xpath)).click();
                result = true;
                break;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }

    public BasePage<T> confirmAlertMsg(String msg) throws Throwable {

        int i = 0;
        while (i++ < 200) {
            try {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                Assert.assertEquals(msg, alertText);
                Hooks.screenShotAlert();
                alert.accept();
                break;
            } catch (NoAlertPresentException e) {
                Thread.sleep(50);
                continue;
            }
        }
        return this;
    }

    public Boolean selectElementByText(WebElement element, String texto) throws Throwable {

        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                if (element.isDisplayed()) {
                    Select select = new Select(element);
                    select.selectByVisibleText(texto);
                    javascriptWait.waitForDocumentCompleteLoad();
                    result = true;
                    break;
                }

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }


    public Boolean selectElementByValue(WebElement element, String valor) throws Throwable {

        Boolean result = false;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                if (element.isDisplayed()) {
                    Select select = new Select(element);
                    select.selectByValue(valor);
                    javascriptWait.waitForDocumentCompleteLoad();
                    result = true;
                    break;
                }

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;
    }

    public Boolean clearText(WebElement element) throws Throwable {
        Boolean result = false;
        for (int i = 0; i < 60; i++) {
            try {
                element.isDisplayed();
                element.clear();
                result = true;
                break;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return result;

    }

    public BasePage<T> screenShot() {
        Hooks.screenShot();
        return this;
    }


    public Boolean validateElementNotDisplayed(WebElement element) throws Throwable {
        int counter = 0;
        for (int i = 0; i < 120; i++) {
            try {
                if (counter == 5) {
                    return true;
                }
                element.isDisplayed();
                Thread.sleep(1000);

            } catch (Exception e) {
                counter++;
            }
        }
        return false;

    }

    public String extractTextFromElement(WebElement element) throws Throwable {
        String text;
        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                text = element.getText();
                if (text.equals("") || text.equals(null)) {
                    return "";
                }
                return text;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        throw new ElementNotVisibleException("element " + element + " nao encontrado");
    }


    public Boolean validateAlertMessage(String msg) throws Throwable {

        int i = 0;
        while (i < 5) {
            try {
                Alert alert = driver.switchTo().alert();
                String alertText = alert.getText();
                if (alertText.equalsIgnoreCase(msg)) {
                    return true;
                } else {
                    return false;
                }
            } catch (NoAlertPresentException e) {
                Thread.sleep(500);
                i++;
                continue;
            }
        }
        return false;
    }


    public BasePage<T> scrollDown() throws Throwable {
        for (int i = 0; i < globalTimeout; i++) {
            try {
                WebElement html = driver.findElement(By.tagName("html"));
                html.sendKeys(Keys.PAGE_DOWN);

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        return this;
    }


    public Boolean clickElementByXpath(String xpath) throws Throwable {
        for (int i = 0; i < globalTimeout; i++) {
            try {
                driver.findElement(By.xpath(xpath)).isEnabled();
                driver.findElement(By.xpath(xpath)).click();
                return true;

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        throw new ElementNotVisibleException("Element with the xpath: " + xpath + " not found.");
    }


    public Boolean sendKeysElement(WebElement element, String text) throws Throwable {

        for (int i = 0; i < globalTimeout; i++) {
            try {
                element.isDisplayed();
                if (element.isEnabled()) {
                    element.clear();
                    element.sendKeys(text);
                    return true;
                }

            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        throw new ElementNotVisibleException(element + " not found.");
    }

    public BasePage verifyEmail(String email) throws Throwable{
        Assert.assertTrue("Unable to verify the email " + email,validateElementText(this.email,email));
        return this;
    }

    public BasePage verifyUrlContains(String text) throws Throwable{
        Assert.assertTrue(text + " not found on " + driver.getCurrentUrl(), driver.getCurrentUrl().contains(text));
        return this;
    }

}