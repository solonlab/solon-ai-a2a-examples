package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Defines configuration details for the OAuth 2.0 Authorization Code flow.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AuthorizationCodeOAuthFlow {
    String authorizationUrl;
    String refreshUrl;
    Map<String, String> scopes;
    String tokenUrl;

    public AuthorizationCodeOAuthFlow(String authorizationUrl, String refreshUrl, Map<String, String> scopes,
                                      String tokenUrl) {
        Assert.checkNotNullParam("authorizationUrl", authorizationUrl);
        Assert.checkNotNullParam("scopes", scopes);
        Assert.checkNotNullParam("tokenUrl", tokenUrl);

        this.authorizationUrl = authorizationUrl;
        this.refreshUrl = refreshUrl;
        this.scopes = scopes;
        this.tokenUrl = tokenUrl;
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

    public String tokenUrl() {
        return tokenUrl;
    }
}
