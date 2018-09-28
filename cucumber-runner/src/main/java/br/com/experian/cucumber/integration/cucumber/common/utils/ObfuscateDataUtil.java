package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.apache.commons.lang3.StringUtils;

import static java.util.Objects.isNull;

/**
 * Created by Kaio Gonzaga on 19/01/2018.
 */
public abstract class ObfuscateDataUtil {
	
	private static final int CPF_SIZE = 11;
	private static final int CNPJ_SIZE = 14;
	
	private ObfuscateDataUtil() {}
	
    public static String obfucateCnpj(String cnpj) {
        if (StringUtils.isBlank(cnpj) || cnpj.length() != CNPJ_SIZE)
            throw new IllegalArgumentException("CNPJ is not valid");

        StringBuilder sb = new StringBuilder();
        sb.append(cnpj.substring(0, 5));
        sb.append("***");
        sb.append(cnpj.substring(cnpj.length() - 6, cnpj.length()));
        return sb.toString().trim();
    }

    public static String obfucateCpf(String cpf) {
        if (StringUtils.isBlank(cpf) || cpf.length() != CPF_SIZE)
            throw new IllegalArgumentException("CPF is not valid");

        StringBuilder sb = new StringBuilder();
        sb.append(cpf.substring(0, 3));
        sb.append("********");
        sb.append(cpf.substring(cpf.length() - 1, cpf.length()));
        return sb.toString().trim();
    }

    public static String obfucateName(String name) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("Name is not valid");

        StringBuilder sb = new StringBuilder();
        String[] split = name.split(" ");
        sb.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            sb.append(" ");
            String eachName = split[i];
            sb.append(eachName.substring(0, 1));
            sb.append(eachName.substring(1, eachName.length()).replaceAll("\\w", "*"));
        }
        return sb.toString().trim();
    }

    public static String obfucateBirthDay(String localDate) {
        if (isNull(localDate))
            throw new IllegalArgumentException("Date can not be null");

        StringBuilder sb = new StringBuilder();
        sb.append(localDate.substring(0, 8));
        sb.append("**");
        return sb.toString().trim();
    }
}