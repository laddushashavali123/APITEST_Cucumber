package br.com.experian.cucumber.integration.cucumber.common.utils.js.framework;

import br.com.experian.cucumber.integration.cucumber.common.utils.js.AbstractFramework;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.JavascriptExecutor;

public class AngularTwo implements AbstractFramework{

	public boolean isPresent(JavascriptExecutor javascriptExecutor) {
		
		return (Boolean) javascriptExecutor.executeScript(
				"return typeof ng == 'object';"
				);
	}

	public void waitToBeready(JavascriptExecutor javascriptExecutor) {
		new NgWebDriver(javascriptExecutor).waitForAngular2RequestsToFinish();
			
	}
	
	
}
