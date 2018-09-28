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
import com.serasa.common.utils.model.athenas.Invoice;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPIInfofaturaeboleto {
	
	@Then("^I validate api Informacoes de faturas e boletos$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Invoice> Datas = new ArrayList<>();
		Datas = mapper.readValue(Hooks.responseJson, new TypeReference<List<Invoice>>(){});
		for (Invoice Data : Datas)
		{
			Hooks.scenario.write("Data: " + Data.getDueDate());
			assertThat(Data.getDueDate().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("Value: " + Data.getValue());
			assertThat(Data.getValue().toString().trim(), is(not(equalTo(""))));
		}
	}
}