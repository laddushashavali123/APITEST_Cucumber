package br.com.experian.cucumber.integration.cucumber.common.utils;

public class DateUtils {
    public static String extractDate(String from) {
        return from.substring(0,10);
    }
}
