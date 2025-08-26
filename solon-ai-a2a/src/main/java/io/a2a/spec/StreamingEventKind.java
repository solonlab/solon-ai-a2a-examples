package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static io.a2a.spec.Message.MESSAGE;
import static io.a2a.spec.Task.TASK;
import static io.a2a.spec.TaskArtifactUpdateEvent.ARTIFACT_UPDATE;
import static io.a2a.spec.TaskStatusUpdateEvent.STATUS_UPDATE;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Task.class, name = TASK),
        @JsonSubTypes.Type(value = Message.class, name = MESSAGE),
        @JsonSubTypes.Type(value = TaskStatusUpdateEvent.class, name = STATUS_UPDATE),
        @JsonSubTypes.Type(value = TaskArtifactUpdateEvent.class, name = ARTIFACT_UPDATE)
})
public interface StreamingEventKind extends Event {

    String getKind();
}
