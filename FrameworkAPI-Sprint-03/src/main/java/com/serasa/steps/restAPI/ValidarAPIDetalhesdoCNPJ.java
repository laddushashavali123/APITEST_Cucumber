package com.serasa.steps.restAPI;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serasa.common.utils.model.athenas.Customer;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPIDetalhesdoCNPJ {
	
	@Then("^I validate api Detalhes do CNPJ \"([^\"]*)\" \"([^\"]*)\"$$")
	public void validarAthenas(String nome, String cnpj) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		List<Customer> Empresa = new ArrayList<>();
		Empresa = mapper.readValue(Hooks.responseJson, new TypeReference<List<Customer>>(){});
		for (Customer Empresas : Empresa)
		{
			Hooks.scenario.write("Nome: " + Empresas.getTradingName());
			assertThat(Empresas.getTradingName().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("CNPJ: " + Empresas.getDocument());
			assertThat(Empresas.getDocument().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("Nome: " + Empresas.getTradingName());
			assertThat(Empresas.getTradingName().toString().trim(), is(not(equalTo(""))));
			
			Hooks.scenario.write("CNPJ: " + Empresas.getDocument());
			assertThat(Empresas.getDocument().toString().trim(), is(not(equalTo(""))));
		}
	}
}