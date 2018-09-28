package com.serasa.common.utils.gerador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cnpj {

	private static String numeroCNPJ;

	public static String getNumeroCNPJ() {
		numeroCNPJ = null;
		gerarNumero();
		while(validarCNPJ(numeroCNPJ) == false)
		{
			gerarNumero();
		}
		return numeroCNPJ;
	}

	public static void setNumeroCNPJ(String numeroCNPJ) {
		Cnpj.numeroCNPJ = numeroCNPJ;
	}
	
	private static void gerarNumero()
	{
		String raiz = "";
		Random gerador = new Random();
		Integer numero;
		for (int i = 0; i < 8; i++)
		{			
			numero =  gerador.nextInt(10);
			raiz = raiz + numero.toString();
		}
		raiz = raiz + "0001";
		raiz = raiz + calcularDigito(raiz);
		setNumeroCNPJ(raiz);
	}

	private static String calcularDigito(String numero)
	{
		Integer primeiroDigito, segundoDigito;
		int soma = 0;
		List<String> numerosPrimeiroDigito = new ArrayList<String>();
		List<String> numerosSegundoDigito = new ArrayList<String>();
		numerosPrimeiroDigito.add("5");
		numerosPrimeiroDigito.add("4");
		numerosPrimeiroDigito.add("3");
		numerosPrimeiroDigito.add("2");
		numerosPrimeiroDigito.add("9");
		numerosPrimeiroDigito.add("8");
		numerosPrimeiroDigito.add("7");
		numerosPrimeiroDigito.add("6");
		numerosPrimeiroDigito.add("5");
		numerosPrimeiroDigito.add("4");
		numerosPrimeiroDigito.add("3");
		numerosPrimeiroDigito.add("2");
		
		for (int i = 0; i < numero.length(); i++)
		{
			soma += Integer.parseInt(numero.substring(i, i + 1)) * Integer.parseInt(numerosPrimeiroDigito.get(i));
		}
		
		if (soma % 11 == 0 || soma % 11 == 1)
		{
			primeiroDigito = 0;
		}else
		{
			primeiroDigito = 11 - (soma % 11);
		}
		soma = 0;
		numero = numero + primeiroDigito.toString();
		
		numerosSegundoDigito.add("6");
		numerosSegundoDigito.add("5");
		numerosSegundoDigito.add("4");
		numerosSegundoDigito.add("3");
		numerosSegundoDigito.add("2");
		numerosSegundoDigito.add("9");
		numerosSegundoDigito.add("8");
		numerosSegundoDigito.add("7");
		numerosSegundoDigito.add("6");
		numerosSegundoDigito.add("5");
		numerosSegundoDigito.add("4");
		numerosSegundoDigito.add("3");
		numerosSegundoDigito.add("2");
		
		for (int i = 0; i < numero.length(); i++)
		{
			soma += Integer.parseInt(numero.substring(i, i + 1)) * Integer.parseInt(numerosSegundoDigito.get(i));
		}
		
		if (soma % 11 == 0 || soma % 11 == 1)
		{
			segundoDigito = 0;
		}else
		{
			segundoDigito = 11 - (soma % 11);
		}
		
		return primeiroDigito.toString() + segundoDigito.toString();
	}
	
	private static boolean validarCNPJ(String numero)
	{
		if (numero.length() != 14)
		{
			return false;
		}
		String numDig = numero.substring(0, 12);
		String numDigAssert = numero.substring(12,14);
		return calcularDigito(numDig).equals(numDigAssert);
	}
}
