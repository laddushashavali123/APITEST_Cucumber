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
import com.serasa.common.utils.model.athenas.NumberOfActiveContract;
import com.serasa.common.utils.model.athenas.VCMValue;
import com.serasa.common.utils.model.athenas.ValueByMonth;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPInfofaturaeboleto {
	
	@Then("^I validate api Informacoes de faturas e boletos$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		NumberOfActiveContract Quantidade = new NumberOfActiveContract();
		Quantidade = mapper.readValue(Hooks.responseJson, NumberOfActiveContract.class);
		
		Hooks.scenario.write("Quantidadecontratos: " + Quantidade.getNumberOfActiveContract());
		assertThat(Quantidade.getNumberOfActiveContract().toString().trim(), is(not(equalTo(""))));
		
	}
}