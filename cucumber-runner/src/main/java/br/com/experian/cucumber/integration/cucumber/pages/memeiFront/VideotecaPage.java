package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class VideotecaPage extends BasePage<VideotecaPage> {

    @Name("Watch First Video")
    @FindBy(xpath = "(//a[@class='btn btn-assistir'])[1]")
    private WebElement watchFirstVideoBtn;

    //video page
    @Name("Play Video")
    @FindBy(xpath = "//button[@title='Play']")
    private WebElement playVideoBtn;

    @Name("Video time bar")
    @FindBy(xpath = "//div[@class='cuepoints']")
    private WebElement videoBar;

    @Name("Rating Start")
    @FindBy(xpath = "//*[contains(@class,'bs-rating-star')][1]")
    private WebElement ratingStar;


    public VideotecaPage clickWatchFirstVideoBtn() throws Throwable{
        Assert.assertTrue("Watch First Video Button not found", clickElement(watchFirstVideoBtn));
        waitPageLoadEvents();
        return this;
    }

    public VideotecaPage clickPlayVideoBtn() throws Throwable{
        switchToVideoFrame();
        Assert.assertTrue("Play Video Button not found", clickElement(playVideoBtn, 30));
        //switchToDefaultFrame();
        return this;
    }

    public VideotecaPage clickVideoBarEnding() throws Throwable {
        switchToVideoFrame();
        Actions builder = new Actions(driver);
        builder.moveToElement(videoBar, 1180, 4).click().build().perform();
        switchToDefaultFrame();
        return this;
    }


    public VideotecaPage watchMovie() throws Throwable {
        this.clickPlayVideoBtn()
                .clickVideoBarEnding();
        return this;
    }

    public VideotecaPage switchToVideoFrame() throws Throwable {
        switchToDefaultFrame();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@class='embed-responsive-item']")));
        waitPageLoadEvents();
        return this;
    }

    public VideotecaPage switchToDefaultFrame() throws Throwable {
        driver.switchTo().defaultContent();
        waitPageLoadEvents();
        return this;
    }

    public VideotecaPage rateVideo(String nStars) throws Throwable {
        //wait element to appear
        Assert.assertTrue("Rating popup not found", waitElement(ratingStar));
        String xpathExpression = "//*[contains(@class,'bs-rating-star')][" + nStars + "]";
        WebElement responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("Star " + nStars + " button not found", clickElement(responseBtn));
        //waitPageLoadEvents();
        //screenShot();
        xpathExpression = "//a[text()='OK']";
        responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("OK button not found", clickElement(responseBtn));
        waitPageLoadEvents();
        screenShot();
        xpathExpression = "//span[text()='Ok']";
        responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("OK button not found", clickElement(responseBtn));
        return this;
    }


}
