package com.serasa.steps.restAPI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.common.utils.model.athenas.VCMValue;
import com.serasa.common.utils.model.athenas.ValueByMonth;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPI2 {
	
	@Then("^I validate api historico de consumo$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		List<ValueByMonth> valores = new ArrayList<>();
		valores = mapper.readValue(Hooks.responseJson, new TypeReference<List<ValueByMonth>>(){});
		for (ValueByMonth valor : valores)
		{
			Hooks.scenario.write("Valor: " + valor.getValue());
			assertThat(valor.getValue().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("Ano: " + valor.getYear());
			assertThat(valor.getYear().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("Mes: " + valor.getMonth());
			assertThat(valor.getMonth().toString().trim(), is(not(equalTo(""))));
		}
	}
}