package br.com.experian.cucumber.integration.cucumber.steps;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import javax.imageio.ImageIO;

public class Hooks {

	private static Collection<String> tags;
	public static Scenario scenario;
	
	private static boolean expectException;
    private static List<Exception> exceptions = new ArrayList<>();

    //Selenium
    private static ThreadLocal<WebDriver> driver;
    private static ThreadLocal<Wait<WebDriver>> wait;

    public static Wait<WebDriver> getWait() {
        return wait.get();
    }
    
	/**
	 * @throws Throwable
	 */
	@Before
	public void runBeforeWithOrder(Scenario scenario) throws Throwable {
		Hooks.scenario = scenario;
		keepScenarion(scenario);
	}

    @After
    public void runAfterWithOrder(Scenario scenario) throws Throwable {
        if (driver != null) {
            embedScreenshot(scenario);
            closeBrowser();
        }
    }

	public void keepScenarion(Scenario scenario) {
		Hooks.tags = scenario.getSourceTagNames();
	}
	
	public static void expectException() {
        expectException = true;
    }

    public static void add(Exception e) throws Exception {
        if (!expectException) {
            throw e;
        }
        exceptions.add(e);
    }

    public static List<Exception> getExceptions() {
        return exceptions;
    }


    // Selenium methods
    public static WebDriver getDriver() {
        if (driver == null){
            configureBrowser();
        }
        return driver.get();
    }

    public static void configureBrowser() {
        //System.setProperty("webdriver.gecko.driver", "/home/user/bin");
        //System.setProperty("webdriver.chrome.driver", "C:/webdrivers/chromedriver.exe");

        driver = ThreadLocal.withInitial(() -> {
            //return new FirefoxDriver();
            try {
                return new ChromeDriver();
            }catch (Exception e) {
                return new ChromeDriver();
            }
        });

        wait = ThreadLocal.withInitial(() -> {
            return new FluentWait<>(driver.get()).withTimeout(40, TimeUnit.SECONDS).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
        });
        //System.out.println("Called openBrowser");
        //driver.get().manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        driver.get().manage().deleteAllCookies();
        driver.get().manage().window().maximize();
    }

    public void closeUnusedTabs() {
        try {
            ArrayList<String> windows = new ArrayList<String>(driver.get().getWindowHandles());
            while (windows.size() > 1) {
                driver.get().switchTo().window(windows.get(1));
                driver.get().close();
                windows = new ArrayList<>(driver.get().getWindowHandles());
            }
            driver.get().switchTo().window(windows.get(0));
        } catch (Exception e) {

        }
    }

    public static void closeBrowser() {
        try {
            driver.get().quit();
            driver = null;
        } catch (Exception e) {

        }
    }

    public static void screenShot() {
        byte[] sShot = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
        scenario.embed(sShot, "image/png");
    }

    public static void screenShotAlert() throws Throwable {

        // Print screen of two monitors
        java.awt.Rectangle screenRect = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
        }
        BufferedImage image = new Robot().createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        scenario.embed(imageInByte, "image/png");
    }

    public void embedScreenshot(Scenario scenario) throws Throwable {

        if (scenario.isFailed()) {
            //screenShotAlert();
            screenShot();
            try {
                scenario.write("Current Page URL is " + driver.get().getCurrentUrl());
                //closeBrowser();
            } catch (Exception e) {
                //System.err.println(e.getMessage());
            }
        }
    }

}

