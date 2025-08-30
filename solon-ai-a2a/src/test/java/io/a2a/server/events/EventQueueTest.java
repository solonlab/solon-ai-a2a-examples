package io.a2a.server.events;

import io.a2a.spec.*;
import io.a2a.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class EventQueueTest {

    private EventQueue eventQueue;

    private static final String MINIMAL_TASK = """
            {
                "id": "123",
                "contextId": "session-xyz",
                "status": {"state": "submitted"},
                "kind": "task"
            }
            """;

    private static final String MESSAGE_PAYLOAD = """
            {
                "role": "agent",
                "parts": [{"kind": "text", "text": "test message"}],
                "messageId": "111",
                "kind": "message"
            }
            """;


    @BeforeEach
    public void init() {
        eventQueue = EventQueue.create();

    }

    @Test
    public void testEnqueueAndDequeueEvent() throws Exception {
        Event event = Utils.unmarshalFrom(MESSAGE_PAYLOAD, Message.TYPE_REFERENCE);
        eventQueue.enqueueEvent(event);
        Event dequeuedEvent = eventQueue.dequeueEvent(200);
        assertSame(event, dequeuedEvent);
    }

    @Test
    public void testDequeueEventNoWait() throws Exception {
        Event event = Utils.unmarshalFrom(MINIMAL_TASK, Task.TYPE_REFERENCE);
        eventQueue.enqueueEvent(event);
        Event dequeuedEvent = eventQueue.dequeueEvent(-1);
        assertSame(event, dequeuedEvent);
    }

    @Test
    public void testDequeueEventEmptyQueueNoWait() throws Exception {
        Event dequeuedEvent = eventQueue.dequeueEvent(-1);
        assertNull(dequeuedEvent);
    }

    @Test
    public void testDequeueEventWait() throws Exception {
        Event event = new TaskStatusUpdateEvent.Builder()
                .taskId("task-123")
                .contextId("session-xyz")
                .status(new TaskStatus(TaskState.WORKING))
                .isFinal(true)
                .build();

        eventQueue.enqueueEvent(event);
        Event dequeuedEvent = eventQueue.dequeueEvent(1000);
        assertSame(event, dequeuedEvent);
    }

    @Test
    public void testTaskDone() throws Exception {
        Event event = new TaskArtifactUpdateEvent.Builder()
                .taskId("task-123")
                .contextId("session-xyz")
                .artifact(new Artifact.Builder()
                        .artifactId("11")
                        .parts(new TextPart("text"))
                        .build())
                .build();
        eventQueue.enqueueEvent(event);
        Event dequeuedEvent = eventQueue.dequeueEvent(1000);
        assertSame(event, dequeuedEvent);
        eventQueue.taskDone();
    }

    @Test
    public void testEnqueueDifferentEventTypes() throws Exception {
        List<Event> events = List.of(
                new TaskNotFoundError(),
                new JSONRPCError(111, "rpc error", null));

        for (Event event : events) {
            eventQueue.enqueueEvent(event);
            Event dequeuedEvent = eventQueue.dequeueEvent(100);
            assertSame(event, dequeuedEvent);
        }
    }
}
