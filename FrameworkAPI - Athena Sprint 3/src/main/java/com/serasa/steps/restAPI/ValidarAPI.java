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
import com.serasa.common.utils.model.athenas.ActiveContract;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;


public class ValidarAPI {

	@Then("^I validate api fields$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		List<ActiveContract> contratos = new ArrayList<>();
		contratos = mapper.readValue(Hooks.responseJson, new TypeReference<List<ActiveContract>>(){});
		for (ActiveContract contrato : contratos)
		{
			Hooks.scenario.write("Contrato: " + contrato.getContractNumber());
			assertThat(contrato.getContractNumber(), is(not(equalTo(""))));
		}
	}
	
}
