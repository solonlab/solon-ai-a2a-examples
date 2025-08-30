package demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.server.App;
import io.a2a.A2A;
import io.a2a.client.A2AClient;
import io.a2a.spec.AgentCard;
import io.a2a.spec.Message;
import io.a2a.spec.MessageSendParams;
import io.a2a.spec.SendMessageResponse;
import org.junit.jupiter.api.Test;
import org.noear.solon.test.SolonTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple example of using the A2A Java SDK to communicate with an A2A server.
 * This example is equivalent to the Python example provided in the A2A Python SDK.
 */
@SolonTest(App.class)
public class HelloWorldTest {
    static final Logger log = LoggerFactory.getLogger(HelloWorldTest.class);

    private static final String SERVER_URL = "http://localhost:9999";
    private static final String MESSAGE_TEXT = "how much is 10 USD in INR?";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void case1() throws Exception {
        try {
            AgentCard finalAgentCard = null;
            AgentCard publicAgentCard = A2A.getAgentCard("http://localhost:9999");
            log.info("Successfully fetched public agent card:");
            log.info(OBJECT_MAPPER.writeValueAsString(publicAgentCard));
            log.info("Using public agent card for client initialization (default).");
            finalAgentCard = publicAgentCard;

            if (publicAgentCard.supportsAuthenticatedExtendedCard()) {
                log.info("Public card supports authenticated extended card. Attempting to fetch from: " + SERVER_URL + "/agent/authenticatedExtendedCard");
                Map<String, String> authHeaders = new HashMap<>();
                authHeaders.put("Authorization", "Bearer dummy-token-for-extended-card");
                AgentCard extendedAgentCard = A2A.getAgentCard(SERVER_URL, "/agent/authenticatedExtendedCard", authHeaders);
                log.info("Successfully fetched authenticated extended agent card:");
                log.info(OBJECT_MAPPER.writeValueAsString(extendedAgentCard));
                log.info("Using AUTHENTICATED EXTENDED agent card for client initialization.");
                finalAgentCard = extendedAgentCard;
            } else {
                log.info("Public card does not indicate support for an extended card. Using public card.");
            }

            A2AClient client = new A2AClient(finalAgentCard);
            Message message = A2A.toUserMessage(MESSAGE_TEXT); // the message ID will be automatically generated for you
            MessageSendParams params = new MessageSendParams.Builder()
                    .message(message)
                    .build();
            SendMessageResponse response = client.sendMessage(params);
            log.info("Message sent with ID: " + response.getId());
            log.info("Response: " + response.toString());
        } catch (Exception e) {
            log.error("An error occurred: " + e.getMessage());
            throw e;
        }
    }
}