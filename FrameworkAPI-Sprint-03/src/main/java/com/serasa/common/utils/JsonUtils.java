package com.serasa.common.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static String converterObjetoParaJson(Object obj)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(df);
		try {
			String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(jsonBody);
			return jsonBody;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String converterArrayParaJson(List<?> objs)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(df);
		
		try {
			String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objs);
			System.out.println(jsonBody);
			return jsonBody;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
