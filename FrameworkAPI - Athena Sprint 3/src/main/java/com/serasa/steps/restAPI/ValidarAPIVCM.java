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
import com.serasa.common.utils.model.athenas.VCMValue;
import com.serasa.steps.Hooks;

import cucumber.api.java.en.Then;

public class ValidarAPIVCM {
	
	@Then("^I validate api historico de consumo VCM$")
	public void validarAthenas() throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		VCMValue VCMatual = new VCMValue();
		VCMatual = mapper.readValue(Hooks.responseJson, VCMValue.class);
		
		Hooks.scenario.write("VCM: " + VCMatual.getVcmValue());
		assertThat(VCMatual.getVcmValue().toString().trim(), is(not(equalTo(""))));
		
	}
}