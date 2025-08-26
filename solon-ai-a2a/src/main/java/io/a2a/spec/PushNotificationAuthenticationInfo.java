package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;

import java.util.List;

/**
 * Defines authentication details for a push notification endpoint.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushNotificationAuthenticationInfo {
    List<String> schemes;
    String credentials;

    public PushNotificationAuthenticationInfo(List<String> schemes, String credentials) {
        Assert.checkNotNullParam("schemes", schemes);

        this.schemes = schemes;
        this.credentials = credentials;
    }

    public List<String> schemes() {
        return schemes;
    }

    public String credentials() {
        return credentials;
    }
}
