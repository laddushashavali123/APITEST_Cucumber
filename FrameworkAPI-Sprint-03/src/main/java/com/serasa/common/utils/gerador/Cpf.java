package com.serasa.common.utils.gerador;

import java.util.Random;

public class Cpf {

	private static String numeroCPF;

	public static String getNumeroCPF() {
		numeroCPF = null;
		gerarNumero();
		while (validaCPF(numeroCPF) == false) {
			gerarNumero();
		}
		return numeroCPF;
	}

	public static void setNumeroCPF(String numeroCPF) {
		Cpf.numeroCPF = numeroCPF;
	}
	
	private static void gerarNumero()
	{
		String raiz = "";
		Random gerador = new Random();
		Integer numero;
		for (int i = 0; i < 9; i++)
		{			
			numero =  gerador.nextInt(10);
			raiz = raiz + numero.toString();
		}
		
		raiz = raiz + calcDigVerif(raiz);
		setNumeroCPF(raiz);
	}
	
	private static String calcDigVerif(String num) {  
        Integer primDig, segDig;  
        int soma = 0, peso = 10;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            primDig = 0;  
        else  
            primDig = 11 - (soma % 11);  
        soma = 0;  
        peso = 11;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
        soma += primDig.intValue() * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            segDig = 0;  
        else  
            segDig = 11 - (soma % 11);  
        return primDig.toString() + segDig.toString();  
    } 
	
	private static boolean validaCPF(String cpf)
	{  
        if (cpf.length() != 11)  
            return false;  
        String numDig = cpf.substring(0, 9);  
        return calcDigVerif(numDig).equals(cpf.substring(9, 11));  
    } 
}
