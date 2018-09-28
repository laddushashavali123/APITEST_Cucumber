package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.annotations.Name;

public class VideoTrailPage extends BasePage<VideoTrailPage> {

    @Name("Watch Next Video")
    @FindBy(xpath = "(//a[text()='Assistir'])[1]")
    private WebElement watchNextVideoBtn;

    @Name("Play Video")
    @FindBy(xpath = "//button[@title='Play']")
    private WebElement playVideoBtn;

    @Name("Video time bar")
    @FindBy(xpath = "//div[@class='cuepoints']")
    private WebElement videoBar;

    @Name("Rating Start")
    @FindBy(xpath = "//*[contains(@class,'bs-rating-star')][1]")
    private WebElement ratingStar;



    public VideoTrailPage clickWatchNextVideoBtn() throws Throwable{
        Assert.assertTrue("Watch Next Video Button not found", clickElement(watchNextVideoBtn));
        waitPageLoadEvents();
        return this;
    }

    public VideoTrailPage clickPlayVideoBtn() throws Throwable{
        switchToVideoFrame();
        Assert.assertTrue("Play Video Button not found", clickElement(playVideoBtn, 30));
        //switchToDefaultFrame();
        return this;
    }

    public VideoTrailPage clickVideoBarEnding() throws Throwable {
        switchToVideoFrame();
        Actions builder = new Actions(driver);
        builder.moveToElement(videoBar, 498, 4).click().build().perform();
        switchToDefaultFrame();
        return this;
    }

    public VideoTrailPage watchMovie() throws Throwable {
        this.clickPlayVideoBtn()
            .clickVideoBarEnding();
        return this;
    }

    //Rating at the end of the video
    public VideoTrailPage rateVideo(String nStars) throws Throwable {
        //wait element to appear
        Assert.assertTrue("Rating popup not found", waitElement(ratingStar));
        String xpathExpression = "//*[contains(@class,'bs-rating-star')][" + nStars + "]";
        WebElement responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("Star " + nStars + " button not found", clickElement(responseBtn));
        waitPageLoadEvents();
        screenShot();
        xpathExpression = "//button[@type='button']";
        responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("Next Video button not found", clickElement(responseBtn));
        waitPageLoadEvents();
        //screenShot();
        return this;
    }

    public VideoTrailPage clickTrailVideo(String videoNum) throws Throwable {

        String xpathExpression = "//ul[contains(@class, 'trilha-personalizada')]//li['" + videoNum + "']";
        WebElement responseBtn = driver.findElement(By.xpath(xpathExpression));
        Assert.assertTrue("Video " + videoNum + " button not found", clickElement(responseBtn));
        waitPageLoadEvents();
        screenShot();

        return this;
    }


    public VideoTrailPage switchToVideoFrame() throws Throwable {
        switchToDefaultFrame();
        driver.switchTo().defaultContent();
        //driver.switchTo().frame(driver.findElement(By.xpath("//*[title='Abertura Trilha Capacitação']")));
        driver.switchTo().frame(driver.findElement(By.xpath("//*[@class='embed-responsive-item']")));
        waitPageLoadEvents();
        return this;
    }

    public VideoTrailPage switchToDefaultFrame() throws Throwable {
        driver.switchTo().defaultContent();
        waitPageLoadEvents();
        return this;
    }


}
