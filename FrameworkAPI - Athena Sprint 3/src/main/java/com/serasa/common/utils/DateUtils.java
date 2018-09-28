package com.serasa.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static Date parse(String data, String formato) throws ParseException
	{
		DateFormat format = new SimpleDateFormat(formato);
		return format.parse(data);
	}
	
	public static String format(Date data, String formato)
	{
		DateFormat format = new SimpleDateFormat(formato);
		return format.format(data);
	}

}
