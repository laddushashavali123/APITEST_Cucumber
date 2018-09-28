package br.com.experian.cucumber.integration.cucumber.common.utils.js;

import br.com.experian.cucumber.integration.cucumber.common.utils.js.framework.Angular;
import br.com.experian.cucumber.integration.cucumber.common.utils.js.framework.AngularTwo;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.function.Function;


public class JavaScriptWait {
	
    private final Wait<WebDriver> wait;
    private final JavascriptExecutor javascriptExecutor;

    private AbstractFramework frameworkDetectado;

    public JavaScriptWait(WebDriver driver, Wait<WebDriver> wait) {
        this.wait = wait;
        this.javascriptExecutor = (JavascriptExecutor) driver;
    }
	
	/*
	 * The following actions expect the document state to be ready
	 * If the pace is using a known framework it will detect and wait for it.
	 *
	 * */
	
	public void waitJsEventOnLoad(){
		waitForDocumentCompleteLoad();
		detectFramework();
		waitForJavaScriptFramework();
	}
	
    private void detectFramework() {
    	frameworkDetectado = Arrays.stream(SupportedFramework.values())
                .map(SupportedFramework::getInstance)
                .filter(framework -> framework.isPresent(javascriptExecutor))
                .findFirst()
                .orElse(null);
	}
	
	private enum SupportedFramework{
		
		ANGULAR(Angular.class),
		ANGULAR_TWO(AngularTwo.class);
			
		private AbstractFramework frameworkInstance;
		
		SupportedFramework(Class<? extends AbstractFramework> frameworkClass){
			try{
			 this.frameworkInstance = frameworkClass.newInstance();
			}
			catch(InstantiationException | IllegalAccessException e){
			 throw new RuntimeException(e);
			}
		}
		public AbstractFramework getInstance(){
			return frameworkInstance;
		}
	}

	
	public void waitForJavaScriptFramework(){
		if(frameworkDetectado != null){
			frameworkDetectado.waitToBeready(javascriptExecutor);
		}
	}
	
	public void waitForDocumentCompleteLoad(){
		wait.until(javascriptExpectedCondition(
				"return document.readyState == 'complete';",
				"o estado do documento ser 'complete'"));
	}

	private static ExpectedCondition<Boolean> javascriptExpectedCondition(String query, String message) {
		return expectedCondition(
				driver -> (Boolean) ((JavascriptExecutor) driver).executeScript(query),
				message);
	}

	private static <T> ExpectedCondition<T> expectedCondition(
			Function<WebDriver, T> function, String string) {

		return new ExpectedCondition<T>() {
			@Override
			public T apply(WebDriver driver) {
				return function.apply(driver);
			}

			@Override
			public String toString() {
				return string;
			}
		};
	}
	

}
