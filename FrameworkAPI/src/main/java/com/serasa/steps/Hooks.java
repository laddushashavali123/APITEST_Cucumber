package com.serasa.steps;

import java.util.Collection;

import cucumber.api.Scenario;
import cucumber.api.java.Before;

public class Hooks {
	// public static WebDriver driver;
	private static Collection<String> taggs;
	public static Scenario scenario;
	public static String hostname = "dev-br-api.experian.com";
	public static String responseJson;
	
	public static  void setHostname(String hostname)
	{
		Hooks.hostname = hostname;
	}
	

	/**
	 * @throws Throwable
	 * 
	 * 
	 */
	@Before
	public void runBeforeWithOrder(Scenario scenario) throws Throwable {
		Hooks.scenario = scenario;
		keepScenarion(scenario);
		//configurarBrowser();
	}

	public void keepScenarion(Scenario scenario) {
		setTaggs(scenario.getSourceTagNames());
	}

	public static Collection<String> getTaggs() {
		return taggs;
	}

	public static void setTaggs(Collection<String> taggs) {
		Hooks.taggs = taggs;
	}
}

