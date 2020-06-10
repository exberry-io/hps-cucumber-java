package io.exberry.e2e;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import java.util.Map;
import org.junit.Assert;

public class StepDefinitions {

  Actionwords actionwords;
  public static final String HOST = "exchange-gateway-master.demo.exberry.io";
  public static final int PORT = 443;

  @Before
  public void setup() {
    actionwords = new Actionwords(HOST, PORT);
  }

  @Given("^there is a broker that got$")
  public void there_is_a_broker_that_got(DataTable data) {
    List<Map<String, String>> list = data.asMaps(String.class, String.class);
    actionwords.setupApiKeyAndSecret(list.get(0).get("apiKey"), list.get(0).get("secret"));
  }

  @When("^a createSession request sent with previous params$")
  public void a_createSession_request_sent_with_fprevious_params() throws Throwable {
    actionwords.createSession();
  }

  @Then("^session created$")
  public void session_created() throws Throwable {
    Assert.assertTrue(actionwords.sessionCreated());
  }

}