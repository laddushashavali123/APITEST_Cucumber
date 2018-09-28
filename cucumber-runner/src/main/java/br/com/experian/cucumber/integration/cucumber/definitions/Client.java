package br.com.experian.cucumber.integration.cucumber.definitions;

import br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil;
import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;
import org.apache.commons.lang.StringUtils;

public class Client {

    public static void setId(String id) {
        RestApi.getUserParameters().put("client_id",id);
    }

    public static void setPropertyId() {
      RestApi.saveVariable("client_id",PropertiesUtil.getProperty("api.iam.client.id"));
    }

    public static void setAppId(String id) {
        RestApi.getUserParameters().put("app_id",id);
    }

    public static void setPropertyAppId() {
        if (!StringUtils.isEmpty(PropertiesUtil.getProperty("api.iam.client.appid"))) {
            RestApi.saveVariable("app_id", PropertiesUtil.getProperty("api.iam.client.appid"));
        }
    }
}
