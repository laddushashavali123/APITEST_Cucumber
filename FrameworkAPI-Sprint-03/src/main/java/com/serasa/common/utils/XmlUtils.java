package com.serasa.common.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlUtils {
	
	private String xml;
	
	public String converterObjetoParaXml(Object obj)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper mapper = new XmlMapper();
		mapper.setDateFormat(df);
		try {
			xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(xml);		
			return xml;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String converterArrayParaXml(List<?>objs)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ObjectMapper mapper = new XmlMapper();
		mapper.setDateFormat(df);
		try {
			xml = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objs);
			System.out.println(xml);		
			return xml;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

}
