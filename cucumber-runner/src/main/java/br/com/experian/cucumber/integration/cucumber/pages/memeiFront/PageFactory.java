package br.com.experian.cucumber.integration.cucumber.pages.memeiFront;

public class PageFactory {

    //force check in
    public static <T extends BasePage<T>> T newInstance(Class<T> clazz) {
        return instantiatePageObject(clazz).get();
    }



    public static <T extends BasePage<T>> T newInstance(
            Class<T> clazz, String url) {
        return instantiatePageObject(clazz).get(url);
    }


    private static <T extends BasePage<T>> T instantiatePageObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate PageObject", e);
        }
    }

}
