package io.exberry.e2e;

import io.exberry.exchange.auth.service.api.CreateSessionRequest;
import io.scalecube.services.api.ServiceMessage;

public final class TestUtils {

  public static final String OM_2_EXCHANGE_AUTH_CREATE_SESSION = "/om2.exchange.auth/createSession";

  public static ServiceMessage buildCreateSessionMessage(CreateSessionRequest data) {
    return ServiceMessage.builder()
        .qualifier(OM_2_EXCHANGE_AUTH_CREATE_SESSION)
        .data(data)
        .build();
  }
}
