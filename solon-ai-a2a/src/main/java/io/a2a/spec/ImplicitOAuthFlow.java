package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Defines configuration details for the OAuth 2.0 Implicit flow.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class ImplicitOAuthFlow {
    String authorizationUrl;
    String refreshUrl;
    Map<String, String> scopes;

    public ImplicitOAuthFlow(String authorizationUrl, String refreshUrl, Map<String, String> scopes) {
        Assert.checkNotNullParam("authorizationUrl", authorizationUrl);
        Assert.checkNotNullParam("scopes", scopes);

        this.authorizationUrl = authorizationUrl;
        this.refreshUrl = refreshUrl;
        this.scopes = scopes;
    }

    public String authorizationUrl() {
        return authorizationUrl;
    }

    public String refreshUrl() {
        return refreshUrl;
    }

    public Map<String, String> scopes() {
        return scopes;
    }
}
