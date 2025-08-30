package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Defines parameters for querying a task, with an option to limit history length.
 *
 * @param id the ID for the task to be queried
 * @param historyLength the maximum number of items of history for the task to include in the response
 * @param metadata additional properties
 */

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class TaskQueryParams {
    String id;
    Integer historyLength;
    Map<String, Object> metadata;

    public TaskQueryParams(String id, Integer historyLength, Map<String, Object> metadata) {
        Assert.checkNotNullParam("id", id);
        if (historyLength != null && historyLength < 0) {
            throw new IllegalArgumentException("Invalid history length");
        }

        this.id = id;
        this.historyLength = historyLength;
        this.metadata = metadata;
    }

    public String id() {
        return id;
    }

    public Integer historyLength() {
        return historyLength;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public TaskQueryParams(String id) {
        this(id, null, null);
    }

    public TaskQueryParams(String id, Integer historyLength) {
        this(id, historyLength, null);
    }
}
