package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {

    @Test
    public void format() {
        Assert.assertEquals("2018-05-10", DateUtils.extractDate("2018-05-10T13:12:51.183-03:00"));
        Assert.assertEquals("2018-05-10", DateUtils.extractDate("2018-05-10"));
    }
}