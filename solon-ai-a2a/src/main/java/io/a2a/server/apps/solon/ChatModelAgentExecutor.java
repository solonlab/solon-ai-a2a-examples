package io.a2a.server.apps.solon;

import io.a2a.A2A;
import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.spec.*;
import org.noear.solon.ai.chat.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                eventQueue.enqueueEvent(createError(context, "No text content found in the message"));
            } else {
                String resultContent = chatModel.prompt(textToTranslate)
                        .call()
                        .getMessage()
                        .getResultContent();

                if (logger.isDebugEnabled()) {
                    logger.debug("The chatModel resultContent:" + resultContent);
                }

                eventQueue.enqueueEvent(A2A.toAgentMessage(resultContent));
            }

        } catch (Exception e) {
            logger.warn("Task failed: {}", e.getMessage(), e);
            eventQueue.enqueueEvent(createError(context, "Task failed: " + e.getMessage()));
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

    private Event createError(RequestContext context, String errorMessage) {
        return A2A.toAgentMessage(errorMessage);
    }
}
