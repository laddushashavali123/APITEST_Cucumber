package br.com.experian.cucumber.integration.cucumber.steps;

import static br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil.getProperty;
import static java.lang.Integer.parseInt;

import java.util.Objects;

import br.com.experian.cucumber.integration.cucumber.common.utils.RestApi;
import org.junit.Assert;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class RabbitMQSteps extends MainSteps {

	private Channel channel;
	private Connection connection;
	
	public void init() {
		channel = null;
		connection = null;
	}

	@Given("^I connect with the rabbitMQ$")
	public void connectAmqp() throws Throwable {
		if (Objects.nonNull(channel) && channel.isOpen())
			return;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(getProperty("rabbitmq.username"));
		factory.setPassword(getProperty("rabbitmq.password"));
		factory.setVirtualHost(getProperty("rabbitmq.virtualhost"));
		factory.setHost(getProperty("rabbitmq.host"));
		factory.setPort(parseInt(getProperty("rabbitmq.port")));
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(getProperty("rabbitmq.queue"), true, false, false, null);
		} catch (Exception e) {
			Assert.fail("Error connecting RabbitMQ");
		}
	};

	@Given("^I close rabbitMQ$")
	public void closeAmqp() throws Throwable {
		if (Objects.nonNull(connection) && connection.isOpen()) {
			connection.close();
		}
		if (Objects.nonNull(channel) && channel.isOpen()) {
			channel.close();
		}
	};

	@Then("^I send messages on the queue \"([^\"]*)\":$")
	public void a(String queueName, DataTable table) throws Throwable {
		for (String body : table.asList(String.class)) {
			body = RestApi.replaceVariablesValues(body);
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
		}
	}

}
