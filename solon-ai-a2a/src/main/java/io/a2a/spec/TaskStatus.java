package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the status of a task at a specific point in time.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class TaskStatus {
    TaskState state;
    Message message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    LocalDateTime timestamp;

    public TaskStatus(TaskState state, Message message, LocalDateTime timestamp) {
        Assert.checkNotNullParam("state", state);
        timestamp = timestamp == null ? LocalDateTime.now() : timestamp;

        this.state = state;
        this.message = message;
        this.timestamp = timestamp;
    }

    public TaskStatus(TaskState state) {
        this(state, null, null);
    }

    public TaskState state() {
        return state;
    }

    public Message message() {
        return message;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }
}
