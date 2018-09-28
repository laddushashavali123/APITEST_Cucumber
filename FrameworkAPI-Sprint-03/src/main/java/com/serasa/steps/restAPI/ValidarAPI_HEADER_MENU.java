package com.serasa.steps.restAPI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.common.utils.model.athenas.AuthenticationInfo;
import com.serasa.common.utils.model.athenas.MenuItem;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPI_HEADER_MENU {
	
	@Then("^I validate api Header Menu$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		List<MenuItem> Menu = new ArrayList<>();
		Menu = mapper.readValue(Hooks.responseJson, new TypeReference<List<MenuItem>>(){});
		for (MenuItem Menus : Menu)
		{
			Hooks.scenario.write("DocumentBase: " + Menus.getItem());
			assertThat(Menus.getItem().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("Logon: " + Menus.getLink());
			assertThat(Menus.getLink().toString().trim(), is(not(equalTo(""))));

		}
	}
}