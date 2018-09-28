package com.serasa.common.utils.gerador;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

public class GeradorData {

	private static String fullDate;
	
	public static String dataHoje(String divisor)
	{
		fullDate = null;
		Calendar data = Calendar.getInstance();
		String ano = String.valueOf(data.get(Calendar.YEAR));
		String mes = String.valueOf(data.get(Calendar.MONTH)+ 1);
		String dia = String.valueOf(data.get(Calendar.DAY_OF_MONTH));
		
		mes = StringUtils.leftPad(mes.toString(), 2, "0");
		dia = StringUtils.leftPad(dia.toString(), 2, "0");
	
		GeradorData.fullDate = 
				ano + divisor
				+ mes + divisor
				+ dia;
		
		return GeradorData.fullDate;
	}
	
	public static String dataAleatoria(String divisor)
	{
		Calendar datas = new GregorianCalendar();
		
		Integer mes = GeradorNumero.gerarNumeroInteger(1, 12);
		datas.set(Calendar.MONTH, mes -1);
		
		String ano = GeradorNumero.gerarNumero(1900, Integer.valueOf(datas.get(Calendar.YEAR)));
		Integer quantidadeDias = datas.getActualMaximum(Calendar.DAY_OF_MONTH);		
		String dia = GeradorNumero.gerarNumero(1, quantidadeDias);
		
		GeradorData.fullDate = ano + divisor
					+ StringUtils.leftPad(mes.toString(), 2, "0") 
					+ StringUtils.leftPad(dia, 2, "0");
		
		return GeradorData.fullDate;
	}
	
}
