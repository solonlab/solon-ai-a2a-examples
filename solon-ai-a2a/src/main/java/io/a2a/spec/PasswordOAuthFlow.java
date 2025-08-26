package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;

import java.util.Map;

/**
 * Defines configuration details for the OAuth 2.0 Resource Owner Password flow.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordOAuthFlow {
    String refreshUrl;
    Map<String, String> scopes;
    String tokenUrl;

    public PasswordOAuthFlow(String refreshUrl, Map<String, String> scopes, String tokenUrl) {
        Assert.checkNotNullParam("scopes", scopes);
        Assert.checkNotNullParam("tokenUrl", tokenUrl);

        this.refreshUrl = refreshUrl;
        this.scopes = scopes;
        this.tokenUrl = tokenUrl;
    }

    public String refreshUrl() {
        return refreshUrl;
    }

    public Map<String, String> scopes() {
        return scopes;
    }

    public String tokenUrl() {
        return tokenUrl;
    }
}
