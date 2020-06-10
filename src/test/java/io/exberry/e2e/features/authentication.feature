@Exchange-Integration-Tests

Feature: Authentication is done on broker level only.
  Each broker can have multiple API keys. Each key should be permission to:
  1. Read (all read only streams)
  2. Write (place order and cancel order)
  Flow:
  Broker get :
  apiKey from exchange (with permissions and brokerId)
  Secret key
  Broker execute “createSession” using the apiKey and signature (generated using secret).
  Exchange verify (vs exchange source of data ):
  brokerId exists
  apiKey belong to that broker and has the read or write permission
  signature was created correct with the right secret
  If all verification are OK exchange return empty response and session is created and apply for a specific websocket connection.
  Any placeOrder and cancelOrder request done on that websocket  connection is allowed. (no need to use token, system doesn’t need this as long as connection is open).
  Once connection closed - the session is lost and need to create a new session.


  #1 - DONE
  Scenario: Creating session successfully
    Given there is a broker that got
      | apiKey                               | secret                                                           |
      | 2ebc4532-9768-4943-be75-1a7818303a3e | 016901ae9bae341c62c11ab16df8ad4ad8ce687ea7caf8a3d9dcdd8518d26453 |
    When a createSession request sent with previous params
    Then session created
