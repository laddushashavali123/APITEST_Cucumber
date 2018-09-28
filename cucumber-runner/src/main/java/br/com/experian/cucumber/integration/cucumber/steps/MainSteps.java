package br.com.experian.cucumber.integration.cucumber.steps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;
import org.apache.http.Header;

public abstract class MainSteps {

	public void init() {
		RestApi.setPath("");
		RestApi.setHeaders(new ArrayList<>());
		RestApi.setParameters(new ArrayList<>());
		RestApi.setJsonBodyString("");
		RestApi.setEntity(null);
		RestApi.setUserParameters(new LinkedHashMap<String, String>());
	}
	
	public void extractJsonResponse() throws Throwable {
        Integer statusCode = RestApi.getResponse().getStatusLine().getStatusCode();

        if (RestApi.getResponse().getEntity() != null && RestApi.getResponse().getEntity().getContent() != null) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(RestApi.getResponse().getEntity().getContent(),"UTF-8"));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            RestApi.setResponseJson(result.toString());
        } else {
			RestApi.setResponseJson("");
        }

    }

	public Object generateObjectValue(String value) throws Exception {

		if (isNumericValue(value)) {
			return Long.parseLong(value);
		}

		if (isDecimalValue(value)) {
			return Double.parseDouble(value);
		}

		if (isISODateValue(value)) {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(value);
		}

		if (isBooleanValue(value)){
			return Boolean.valueOf(value);
		}

		return value;
	}

	private boolean isBooleanValue(String value) {
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
	}

	private boolean isNumericValue(String value) {

		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	private boolean isDecimalValue(String value) {

		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	private boolean isISODateValue(String val) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			sdf.parse(val);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public void removeHeader(String headerKey) {

		for (Iterator<Header> i = RestApi.getHeaders().iterator(); i.hasNext();) {
			Header header = i.next();
			if (header.getName().equals(headerKey)) {
				i.remove();
			}
		}
	}

}
