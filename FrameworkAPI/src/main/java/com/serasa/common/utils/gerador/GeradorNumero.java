package com.serasa.common.utils.gerador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class GeradorNumero {

	private static BigDecimal valor;
	
	public static String gerarNumero(int min, int max)
	{
		Random rand = new Random();
		Integer numero = rand.nextInt((max - min) + 1) + min;
		return numero.toString();
	}
	
	public static Integer gerarNumeroInteger(int min, int max)
	{
		Random rand = new Random();
		return rand.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
	}
	
	public static Long gerarNumeroLong(Long min, Long max)
	{
		Long generatedLong = min + (long) (Math.random() * (max - min));
		return generatedLong;
	}
	
	public static BigDecimal gerarValor()
	{
		valor = null;
		BigDecimal min = new BigDecimal(1.0);
		BigDecimal max = new BigDecimal(1000.0);
		GeradorNumero.valor = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
		GeradorNumero.valor = GeradorNumero.valor.setScale(2, RoundingMode.FLOOR);
		GeradorNumero.valor = GeradorNumero.valor.stripTrailingZeros();
		return GeradorNumero.valor;
	}
	
}
