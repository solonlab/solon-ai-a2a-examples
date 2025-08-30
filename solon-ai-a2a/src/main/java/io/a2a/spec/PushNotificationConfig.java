package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the configuration for setting up push notifications for task updates.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class PushNotificationConfig {
    String url;
    String token;
    PushNotificationAuthenticationInfo authentication;
    String id;

    public PushNotificationConfig(String url, String token, PushNotificationAuthenticationInfo authentication, String id) {
        Assert.checkNotNullParam("url", url);

        this.url = url;
        this.token = token;
        this.authentication = authentication;
        this.id = id;
    }

    public String url() {
        return url;
    }

    public String token() {
        return token;
    }

    public PushNotificationAuthenticationInfo authentication() {
        return authentication;
    }

    public String id() {
        return id;
    }

    public static class Builder {
        private String url;
        private String token;
        private PushNotificationAuthenticationInfo authentication;
        private String id;

        public Builder() {
        }

        public Builder(PushNotificationConfig notificationConfig) {
            this.url = notificationConfig.url;
            this.token = notificationConfig.token;
            this.authentication = notificationConfig.authentication;
            this.id = notificationConfig.id;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder authenticationInfo(PushNotificationAuthenticationInfo authenticationInfo) {
            this.authentication = authenticationInfo;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public PushNotificationConfig build() {
            return new PushNotificationConfig(url, token, authentication, id);
        }
    }
}
