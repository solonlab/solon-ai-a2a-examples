package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Defines configuration options for a `message/send` or `message/stream` request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class MessageSendConfiguration {
    List<String> acceptedOutputModes;
    Integer historyLength;
    PushNotificationConfig pushNotification;
    boolean blocking;

    public MessageSendConfiguration(List<String> acceptedOutputModes, Integer historyLength,
                                    PushNotificationConfig pushNotification, boolean blocking) {
        if (historyLength != null && historyLength < 0) {
            throw new IllegalArgumentException("Invalid history length");
        }

        this.acceptedOutputModes = acceptedOutputModes;
        this.historyLength = historyLength;
        this.pushNotification = pushNotification;
        this.blocking = blocking;
    }

    public List<String> acceptedOutputModes() {
        return acceptedOutputModes;
    }

    public Integer historyLength() {
        return historyLength;
    }

    public PushNotificationConfig pushNotification() {
        return pushNotification;
    }

    public boolean blocking() {
        return blocking;
    }

    public static class Builder {
        List<String> acceptedOutputModes;
        Integer historyLength;
        PushNotificationConfig pushNotification;
        boolean blocking;

        public Builder acceptedOutputModes(List<String> acceptedOutputModes) {
            this.acceptedOutputModes = acceptedOutputModes;
            return this;
        }

        public Builder pushNotification(PushNotificationConfig pushNotification) {
            this.pushNotification = pushNotification;
            return this;
        }

        public Builder historyLength(Integer historyLength) {
            this.historyLength = historyLength;
            return this;
        }

        public Builder blocking(boolean blocking) {
            this.blocking = blocking;
            return this;
        }

        public MessageSendConfiguration build() {
            return new MessageSendConfiguration(acceptedOutputModes, historyLength, pushNotification, blocking);
        }
    }
}
