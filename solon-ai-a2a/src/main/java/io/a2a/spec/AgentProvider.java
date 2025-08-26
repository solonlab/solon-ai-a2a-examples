package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;

/**
 * Represents the service provider of an agent.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentProvider {
    String organization;
    String url;

    public AgentProvider(String organization, String url) {
        Assert.checkNotNullParam("organization", organization);
        Assert.checkNotNullParam("url", url);

        this.organization = organization;
        this.url = url;
    }

    public String organization() {
        return organization;
    }

    public String url() {
        return url;
    }
}
