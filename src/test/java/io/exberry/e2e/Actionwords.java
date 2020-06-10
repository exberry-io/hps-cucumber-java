package io.exberry.e2e;

import static io.scalecube.services.gateway.transport.GatewayClientTransports.WEBSOCKET_CLIENT_CODEC;

import io.exberry.exchange.auth.service.api.BrokerAuthenticationService;
import io.exberry.exchange.auth.service.api.CreateSessionRequest;
import io.exberry.exchange.auth.service.utils.HexUtils;
import io.exberry.exchange.order.service.api.OrderService;
import io.scalecube.net.Address;
import io.scalecube.services.ServiceCall;
import io.scalecube.services.gateway.transport.GatewayClient;
import io.scalecube.services.gateway.transport.GatewayClientSettings;
import io.scalecube.services.gateway.transport.GatewayClientTransport;
import io.scalecube.services.gateway.transport.StaticAddressRouter;
import io.scalecube.services.gateway.transport.websocket.WebsocketGatewayClient;
import java.time.Duration;
import java.util.StringJoiner;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Actionwords {

  private static final String HMAC_SHA256 = "HmacSHA256";
  // TODO: init service call on construction time
  private ServiceCall client;
  private BrokerAuthenticationService brokerService;
  private OrderService orderService;
  private String apiKey;
  private String secret;

  private boolean sessionCreated = false;

  private static final Duration TIMEOUT = Duration.ofSeconds(5);
  private long timestamp;

  public Actionwords(String host, int port) {
    client = createClient(host, port);
    brokerService = client.api(BrokerAuthenticationService.class);
    orderService = client.api(OrderService.class);
  }

  public void setupApiKeyAndSecret(String apiKey, String secret) {
    this.apiKey = apiKey;
    this.secret = secret;
    this.timestamp = System.currentTimeMillis();
  }

  public void createSession() throws Exception {
    SecretKey key = new SecretKeySpec(this.secret.getBytes(), HMAC_SHA256);
    Mac mac = Mac.getInstance(HMAC_SHA256);
    mac.init(key);
    String data =
        new StringJoiner(",")
            .add(quote("apiKey") + ":" + quote(apiKey))
            .add(quote("timestamp") + ":" + quote("" + timestamp))
            .toString();
    String signature = HexUtils.toHex(mac.doFinal(data.getBytes()));
    brokerService
        .createSession(TestUtils
            .buildCreateSessionMessage(
                new CreateSessionRequest(apiKey, timestamp, signature)))
        .block(TIMEOUT);
    this.sessionCreated = true;
  }

  public boolean sessionCreated() {
    return this.sessionCreated;
  }

  private ServiceCall createClient(String host, int port) {
    final Address address = Address.create(host, port);
    GatewayClient gatewayClient =
        new WebsocketGatewayClient(
            GatewayClientSettings.builder().address(address)
                .secure()
                .build(),
            WEBSOCKET_CLIENT_CODEC);

    return new ServiceCall()
        .transport(new GatewayClientTransport(gatewayClient))
        .router(new StaticAddressRouter(address));
  }

  public static String quote(String s) {
    return new StringJoiner("", "\"", "\"").add(s).toString();
  }

}