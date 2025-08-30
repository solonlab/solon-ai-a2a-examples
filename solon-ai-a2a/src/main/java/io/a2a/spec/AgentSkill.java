package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

/**
 * The set of skills, or distinct capabilities, that the agent can perform.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class AgentSkill {
    String id;
    String name;
    String description;
    List<String> tags;
    List<String> examples;
    List<String> inputModes;
    List<String> outputModes;
    List<Map<String, List<String>>> security;

    public AgentSkill(String id, String name, String description, List<String> tags,
                      List<String> examples, List<String> inputModes, List<String> outputModes,
                      List<Map<String, List<String>>> security) {
        Assert.checkNotNullParam("description", description);
        Assert.checkNotNullParam("id", id);
        Assert.checkNotNullParam("name", name);
        Assert.checkNotNullParam("tags", tags);

        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.examples = examples;
        this.inputModes = inputModes;
        this.outputModes = outputModes;
        this.security = security;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public List<String> tags() {
        return tags;
    }

    public List<String> examples() {
        return examples;
    }

    public List<String> inputModes() {
        return inputModes;
    }

    public List<String> outputModes() {
        return outputModes;
    }

    public List<Map<String, List<String>>> security() {
        return security;
    }

    public static class Builder {

        private String id;
        private String name;
        private String description;
        private List<String> tags;
        private List<String> examples;
        private List<String> inputModes;
        private List<String> outputModes;
        private List<Map<String, List<String>>> security;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder examples(List<String> examples) {
            this.examples = examples;
            return this;
        }

        public Builder inputModes(List<String> inputModes) {
            this.inputModes = inputModes;
            return this;
        }

        public Builder outputModes(List<String> outputModes) {
            this.outputModes = outputModes;
            return this;
        }

        public Builder security(List<Map<String, List<String>>> security) {
            this.security = security;
            return this;
        }

        public AgentSkill build() {
            return new AgentSkill(id, name, description, tags, examples, inputModes, outputModes, security);
        }
    }
}
