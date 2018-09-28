package br.com.experian.cucumber.integration.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static br.com.experian.cucumber.integration.cucumber.common.utils.CypherUtil.decryptAES;
import static br.com.experian.cucumber.integration.cucumber.common.utils.CypherUtil.encryptAES;
import static br.com.experian.cucumber.integration.cucumber.common.utils.ObfuscateDataUtil.obfucateName;
import static org.junit.Assert.assertEquals;


public class EncryptoSteps {

    private String key = "5ebb10f16146b47f842b251f";
    private String encrypted;
    private String obfuscated;
    private String dencrypted;

    @Given("^I use the key \"([^\"]*)\"$")
    public void setKey(String key) {
        this.key = key;
    }

    @Then("^I obfuscate the value \"([^\"]*)\"$")
    private void obfuscate(String value) {
        this.obfuscated = obfucateName(value);
        Hooks.scenario.write(this.obfuscated);
    }

    @Then("^I encrypt the value \"([^\"]*)\"$")
    private void encrypt(String value) {
        this.encrypted = encryptAES(value, this.key);
        Hooks.scenario.write(this.encrypted);
    }

    @Then("^I decrypt the value \"([^\"]*)\"$")
    void decrypt(String value) {
        this.dencrypted = decryptAES(value, this.key);
        Hooks.scenario.write(this.dencrypted);
    }

    @Then("^The value \"([^\"]*)\" should be equals the encrypted value$")
    public void validateResponseCrypto(String value) {
        this.encrypted = encryptAES(value, this.key);
        Hooks.scenario.write(this.encrypted);
        this.dencrypted = decryptAES(encrypted, this.key);
        Hooks.scenario.write(this.dencrypted);
        assertEquals(value, dencrypted);
    }
}
