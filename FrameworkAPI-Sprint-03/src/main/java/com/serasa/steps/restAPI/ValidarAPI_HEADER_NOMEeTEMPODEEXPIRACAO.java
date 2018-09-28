 package com.serasa.steps.restAPI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.common.utils.model.athenas.AuthenticationInfo;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPI_HEADER_NOMEeTEMPODEEXPIRACAO {
	
	@Then("^I validate api AuthenticationInfo \"([^\"]*)\" \"([^\"]*)\"$$")
	public void validarAthenas(String Logon, String Expiration) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		AuthenticationInfo Nome = new AuthenticationInfo();
		Nome = mapper.readValue(Hooks.responseJson, AuthenticationInfo.class);
		
		Hooks.scenario.write("DocumentBase: " + Nome.getDocumentBase());
		assertThat(Nome.getDocumentBase().toString().trim(), is(not(equalTo(""))));
		
		Hooks.scenario.write("Logon: " + Nome.getLogon());
		assertThat(Nome.getLogon().toString().trim(), is(not(equalTo(""))));
		
		
		Hooks.scenario.write("Expiration: " + Nome.getExpiration());
		assertThat(Nome.getExpiration().toString().trim(), is(not(equalTo(""))));
		
		
	}
}