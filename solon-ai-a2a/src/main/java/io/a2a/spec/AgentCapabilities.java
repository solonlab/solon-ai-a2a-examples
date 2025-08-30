package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * Defines optional capabilities supported by an agent.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AgentCapabilities {
    boolean streaming;
    boolean pushNotifications;
    boolean stateTransitionHistory;
    List<AgentExtension> extensions;

    public AgentCapabilities(boolean streaming, boolean pushNotifications, boolean stateTransitionHistory,
                             List<AgentExtension> extensions) {
        this.streaming = streaming;
        this.pushNotifications = pushNotifications;
        this.stateTransitionHistory = stateTransitionHistory;
        this.extensions = extensions;
    }

    public boolean streaming() {
        return streaming;
    }

    public boolean pushNotifications() {
        return pushNotifications;
    }

    public boolean stateTransitionHistory() {
        return stateTransitionHistory;
    }

    public List<AgentExtension> getExtensions() {
        return extensions;
    }


    public void setStreaming(boolean streaming) {}

    public static class Builder {

        private boolean streaming;
        private boolean pushNotifications;
        private boolean stateTransitionHistory;
        private List<AgentExtension> extensions;

        public Builder streaming(boolean streaming) {
            this.streaming = streaming;
            return this;
        }

        public Builder pushNotifications(boolean pushNotifications) {
            this.pushNotifications = pushNotifications;
            return this;
        }

        public Builder stateTransitionHistory(boolean stateTransitionHistory) {
            this.stateTransitionHistory = stateTransitionHistory;
            return this;
        }

        public Builder extensions(List<AgentExtension> extensions) {
            this.extensions = extensions;
            return this;
        }

        public AgentCapabilities build() {
            return new AgentCapabilities(streaming, pushNotifications, stateTransitionHistory, extensions);
        }
    }
}
