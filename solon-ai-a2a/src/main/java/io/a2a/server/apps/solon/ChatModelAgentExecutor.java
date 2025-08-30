package io.a2a.server.apps.solon;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import org.noear.solon.ai.chat.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author noear 2025/8/30 created
 *
 */
public class ChatModelAgentExecutor implements AgentExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ChatModelAgentExecutor.class);

    private final ChatModel chatModel;
    public ChatModelAgentExecutor(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
        try {
            // Extract text content from message parts
            String textToTranslate = extractTextFromMessage(context.getMessage());

            if (textToTranslate == null || textToTranslate.trim().isEmpty()) {
                eventQueue.enqueueEvent(createErrorTask(context.getTask(), "No text content found in the message"));
            } else {
                String resultContent = chatModel.prompt(textToTranslate)
                        .call()
                        .getMessage()
                        .getResultContent();

                if(logger.isDebugEnabled()) {
                    logger.debug("The chatModel resultContent:" + resultContent);
                }

                // Create response message with translation
                TextPart responsePart = new TextPart(resultContent);
                Message responseMessage =  new Message.Builder()
                        .messageId(UUID.randomUUID().toString())
                        .role(Message.Role.AGENT)
                        .parts(responsePart)
                        .contextId(context.getContextId())
                        .taskId(context.getTaskId())
                        .referenceTaskIds(context.getMessage().getReferenceTaskIds())
                                .build();

                // Create completed status
                TaskStatus completedStatus = new TaskStatus(
                        TaskState.COMPLETED,
                        null,  // No status message
                        LocalDateTime.now()
                );

                // Add response to history
                List<Message> updatedHistory = context.getTask().getHistory() != null ?
                        Arrays.asList(context.getTask().getHistory().toArray(new Message[0])) :
                        Arrays.asList();

                updatedHistory = Arrays.asList(
                        java.util.stream.Stream.concat(
                                updatedHistory.stream(),
                                java.util.stream.Stream.of(context.getMessage(), responseMessage)
                        ).toArray(Message[]::new)
                );

                eventQueue.enqueueEvent(new Task.Builder()
                        .id(context.getTaskId())
                        .contextId(context.getContextId())
                        .artifacts(context.getTask().getArtifacts())
                        .history(updatedHistory)
                        .metadata(context.getTask().getMetadata())
                        .build());
            }


        } catch (Exception e) {
            logger.warn("Task failed: {}", e.getMessage(), e);
            eventQueue.enqueueEvent(createErrorTask(context.getTask(), "Task failed: " + e.getMessage()));
        }
    }

    @Override
    public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {

    }


    private String extractTextFromMessage(Message message) {
        if (message.getParts() == null || message.getParts().isEmpty()) {
            return null;
        }

        StringBuilder textBuilder = new StringBuilder();
        for (Part part : message.getParts()) {
            if (part instanceof TextPart) {
                if (textBuilder.length() > 0) {
                    textBuilder.append("\n");
                }
                textBuilder.append(((TextPart) part).getText());
            }
        }

        return textBuilder.toString();
    }

    private Task createErrorTask(Task originalTask, String errorMessage) {
        TaskStatus errorStatus = new TaskStatus(
                TaskState.FAILED,
                null,
                LocalDateTime.now()
        );

        return new Task.Builder()
                .id(originalTask.getId())
                .contextId(originalTask.getContextId())
                .artifacts(originalTask.getArtifacts())
                .history(originalTask.getHistory())
                .metadata(originalTask.getMetadata())
                .status(errorStatus)
                .build();
    }
}
