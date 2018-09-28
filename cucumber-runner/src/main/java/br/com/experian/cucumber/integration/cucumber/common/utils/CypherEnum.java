package br.com.experian.cucumber.integration.cucumber.common.utils;

public enum CypherEnum {
	LENGTH_1("HOBBITS"), 
	LENGTH_6("MAIAR");

	private final String value;

	CypherEnum(String optionValue) {
		value = optionValue;
	}

	public String getValue() {
		return value;
	}
}
