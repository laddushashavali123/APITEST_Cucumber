package br.com.experian.cucumber.integration.cucumber.common.utils.js;

import org.openqa.selenium.JavascriptExecutor;

public interface AbstractFramework {
	
	 boolean isPresent(JavascriptExecutor javascriptExecutor);
	
	 void waitToBeready(JavascriptExecutor javascriptExecutor);
	
}
