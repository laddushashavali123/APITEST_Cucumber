package com.serasa.common.utils;

import org.apache.commons.lang3.StringUtils;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;
import br.com.caelum.stella.format.Formatter;

public class Formatador {

	private static String documento;
	private static Formatter formatter = null;
	private static String tipoDocumento = null;

	public static String removerPontuacao(String documento)
	{
		Formatador.documento = null;
		Formatador.formatter = null;
		Formatador.tipoDocumento = descobrirTipoDocumento(documento);

		switch (tipoDocumento.toUpperCase())
		{
		case "CPF":
			formatter = new CPFFormatter();
			Formatador.documento = formatter.unformat(documento);
			break;
		case "CNPJ":
			formatter = new CNPJFormatter();
			Formatador.documento = formatter.unformat(documento);
			break;
		}

		return Formatador.documento;	
	}

	public static String colocarPontuacao (String documento)
	{
		Formatador.documento = null;
		Formatador.formatter = null;
		tipoDocumento = descobrirTipoDocumento(documento);


		switch (tipoDocumento.toUpperCase())
		{
		case "CPF":
			formatter = new CPFFormatter();
			Formatador.documento = formatter.format(documento);
			break;
		case "CNPJ":
			formatter = new CNPJFormatter();
			Formatador.documento = formatter.format(documento);
			break;
		}

		return Formatador.documento;
	}

	private static String descobrirTipoDocumento(String documento)
	{
		Integer documentoSize = documento.length();
		boolean temPontuacao = StringUtils.contains(documento, "-");

		if (documentoSize == 11 || (temPontuacao && documentoSize == 14)) 
		{
			return "CPF";
		}else if (documentoSize == 18 || documentoSize == 14)
		{
			return "CNPJ";
		}else
		{
			return null;
		}
	}
}
