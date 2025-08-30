package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Defines parameters containing a task ID, used for simple task operations.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class TaskIdParams {
    String id;
    Map<String, Object> metadata;

    public TaskIdParams(String id, Map<String, Object> metadata) {
        Assert.checkNotNullParam("id", id);

        this.id = id;
        this.metadata = metadata;
    }

    public TaskIdParams(String id) {
        this(id, null);
    }

    public String id() {
        return id;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }
}