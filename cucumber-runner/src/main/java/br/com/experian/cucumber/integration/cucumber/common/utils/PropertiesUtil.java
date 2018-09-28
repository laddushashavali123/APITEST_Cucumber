package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PropertiesUtil {

	private PropertiesUtil() {
	}

	private static final String CUCUMBER_PROPERTIES = "cucumber";
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
	public static final String SANDBOX = "sandbox";
	public static final String VARIABLE_PROFILE = "cucumber.profiles.active";

	public static String getProperty(final String key) {

		String value = null;

		try {
			ResourceBundle bundle = null;
			String profile = System.getProperty(VARIABLE_PROFILE);
			if (profile != null) {
				bundle = ResourceBundle.getBundle(CUCUMBER_PROPERTIES + "-" + profile);
			} else {
				bundle = ResourceBundle.getBundle(CUCUMBER_PROPERTIES);
			}
			value = resolveValueWithEnvVars(bundle.getString(key));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return value;
	}

	private static String resolveValueWithEnvVars(String value) {
		if (null == value) {
			return null;
		}

		Pattern p = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");
		Matcher m = p.matcher(value);
		String envVarValue = value;
		while (m.find()) {
			String envVarName = null == m.group(1) ? m.group(2) : m.group(1);
			envVarValue = System.getenv(envVarName);
			if (envVarValue == null || envVarValue.isEmpty()) {
				envVarValue = System.getProperty(envVarName);
			}
		}
		return envVarValue;
	}
}