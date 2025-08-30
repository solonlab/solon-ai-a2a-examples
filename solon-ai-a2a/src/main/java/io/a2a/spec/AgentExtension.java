package io.a2a.spec;

import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A declaration of a protocol extension supported by an Agent.
 */
@NoArgsConstructor
@Data
public class AgentExtension{
    String description;
    Map<String, Object> params;
    boolean required;
    String uri;

    public AgentExtension (String description, Map<String, Object> params, boolean required, String uri)  {
        Assert.checkNotNullParam("uri", uri);

        this.description = description;
        this.params = params;
        this.required = required;
        this.uri = uri;
    }

    public String description() {
        return description;
    }

    public Map<String, Object> params() {
        return params;
    }

    public boolean required() {
        return required;
    }

    public String uri() {
        return uri;
    }

    public static class Builder {
        String description;
        Map<String, Object> params;
        boolean required;
        String uri;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public AgentExtension build() {
            return new AgentExtension(description, params, required, uri);
        }
    }

}
