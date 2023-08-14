# Real-time communication for Antragsgrün


## Authentication

- The central Antragsgrün system authenticates users through traditional means (cookie-based sessions generated during username/password- or SAML-based login).
- It creates a JWT containing information about the site, the consultation and the ID of the user and is signed using a private key (RS256). If the user has specific admin privileges (like to administer speech queues), a role is added to the JWT.
- If the user is not logged in, instead of the regular user-id-pattern (`login-123`) a session-token like (`anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN`) will be set as Subject of the JWT.
- We web browser connects to the websocket / STOMP server of this Live Server. The authentication and authorization is checked at the following places:
  - When connecting, the validity of the JWT is checked on a protocol level (as part of [WebsocketChannelInterceptor](src/main/java/de/antragsgruen/live/websocket/WebsocketChannelInterceptor.java)).
  - The site and consultation association is checked when subscribing to topics - the site subdomain and consultation path has to be in the topic name and equal to information provided in the JWT.
  - When subscribing to the speech admin topic, the SPEECH_ADMIN role is checked in the JWT.


## RabbitMQ Setup

The central Antragsgrün system publishes all its messages to one central exchange (by default: `antragsgruen-exchange`). Messages to all subdomains and consultations within a subdomain are published through that exchange, but are classified by a routing key pattern.

The following routing key patterns are fixed, while its associated queues can be configured:
- `user.[site].[consultation].[userid]`, e.g. `user.stdparteitag.std-parteitag.1` contains messages directed to one particular user, by default being bound to the queue `antragsgruen-user-queue` and using the [UserEvent](src/main/java/de/antragsgruen/live/rabbitmq/dto/UserEvent.java)-DTO for deserialization.
- `speech.[site].[consultation]`, e.g. `speech.stdparteitag.std-parteitag` contains messages updating a speech queue, by default being bound to the queue `antragsgruen-speech-queue` and using the [SpeechQueue](src/main/java/de/antragsgruen/live/rabbitmq/dto/SpeechQueue.java)-DTO for deserialization. All users in the consultation receive this event, but in a personalized version.

## Exposed Websocket STOMP Topics

- `/user/[subdomain]/[consultation]/[userid]/speech`
- `/topic/[subdomain]/[consultation]/[...]` (currently not used)


## Installing, Running, Configuration

Docker needs to be installed, as for now, Spring automatically starts a RabbitMQ-container and connects to it. 

```shell
npm install
./mvnw spring-boot:run
```

## Testing

### Running the tests

Currently there is one integration test that tests the RabbitMQ-receiver, the data mapping and the WS/STOMP-Server by connecting to the STOMP-Server using a self-signed JWT for authentication, then sends a message to RabbitMQ and tests what message gets delivered through the STOMP-Connection.

The test case is located in [LiveApplicationTests.java](src/test/java/de/antragsgruen/live/LiveApplicationTests.java), some helper classes in [utils](src/test/java/de/antragsgruen/live/utils) and the test fixtures (JSON Payloads) in [resources](src/test/resources).

To run the tests, call:
```shell
npm ci # Only needed once
./mvnw test
```

Note that Docker needs to be installed for this, too, as the integration test makes use of RabbitMQ.

### JWTs for tests

The public / private keys used for the test cases were created using the following commands:
```shell
ssh-keygen -t rsa -b 4096 -m PEM -f bundle.pem
openssl rsa -in bundle.pem -pubout -outform PEM -out jwt-test-public.key
openssl pkcs8 -topk8 -inform PEM -outform PEM -in bundle.pem -out jwt-test-private.key -nocrypt
```
