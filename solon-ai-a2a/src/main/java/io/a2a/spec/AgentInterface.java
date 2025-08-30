package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Declares a combination of a target URL and a transport protocol for interacting with the agent.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AgentInterface {
    String transport;
    String url;

    public AgentInterface(String transport, String url) {
        Assert.checkNotNullParam("transport", transport);
        Assert.checkNotNullParam("url", url);

        this.transport = transport;
        this.url = url;
    }

    public String transport() {
        return transport;
    }

    public String url() {
        return url;
    }
}
