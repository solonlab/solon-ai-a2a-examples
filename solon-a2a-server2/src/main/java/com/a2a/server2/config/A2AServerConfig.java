package com.a2a.server2.config;

import com.a2a.server2.tool.Server2Tools;
import org.noear.solon.ai.a2a.model.*;
import org.noear.solon.ai.a2a.server.A2AServer;
import org.noear.solon.ai.a2a.server.TaskHandler;
import org.noear.solon.ai.chat.ChatConfig;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author by HaiTao.Wang on 2025/8/21.
 */
@Configuration
public class A2AServerConfig {

    @Bean
    public ChatModel build(@Inject("${solon.ai.demo}") ChatConfig config) {
        return ChatModel.of(config).defaultToolsAdd(new Server2Tools()).build();
    }

    @Bean
    public A2AServer a2aServer(@Inject ChatModel chatModel) {
        // Create agent card
        AgentCard agentCard = createAgentCard();

        // Create task handler
        TaskHandler taskHandler = createTaskHandler(chatModel);

        return new A2AServer(agentCard, taskHandler);
    }

    /**
     * Create translation agent card
     */
    private AgentCard createAgentCard() {
        return AgentCard.builder()
                .name("spot_agent")
                .description("A professional scenic spot recommendation assistant. It can accurately recommend attractions and places to visit.")
                .url("http://localhost:10002/a2a")
                .documentationUrl("http://localhost:10002/docs")
                .version("1.0.0")
                .provider(AgentProvider.builder()
                        .organization("ollama")
                        .url("http://127.0.0.1:11434/api/chat")
                        .build())
                .capabilities(AgentCapabilities.builder()
                        .streaming(false)
                        .pushNotifications(false)
                        .stateTransitionHistory(false)
                        .build())
                .defaultInputModes(Arrays.asList("text"))
                .defaultOutputModes(Arrays.asList("text"))
                .skills(Arrays.asList(AgentSkill.builder()
                        .id("ai-spot")
                        .name("景区推荐")
                        .description("专业的景区推荐助手。主要任务是推荐景点信息")
                        .tags(Arrays.asList("旅游", "景区", "景点", "游玩"))
                        .examples(Arrays.asList("示例：有哪些景区", "示例：有哪些玩的地方", "推荐一些景点"))
                        .inputModes(Arrays.asList("text"))
                        .outputModes(Arrays.asList("text"))
                        .build()))
                .build();
    }


    private TaskHandler createTaskHandler(ChatModel chatModel) {
        return (task, message) -> {
            try {
                // Extract text content from message parts
                String textToTranslate = extractTextFromMessage(message);

                if (textToTranslate == null || textToTranslate.trim().isEmpty()) {
                    return createErrorTask(task, "No text content found in the message");
                }

                String resultContent = chatModel.prompt(textToTranslate).call().getMessage().getResultContent();
                System.err.println("resultContent:" + resultContent);

                // Create response message with translation
                TextPart responsePart = new TextPart(resultContent);
                Message responseMessage = new Message(
                        UUID.randomUUID().toString(),
                        "message",
                        "assistant",
                        Arrays.asList(responsePart),
                        message.getContextId(),
                        task.getId(),
                        Arrays.asList(message.getMessageId()),
                        null
                );

                // Create completed status
                TaskStatus completedStatus = new TaskStatus(
                        TaskState.COMPLETED,
                        null,  // No status message
                        Instant.now().toString()
                );

                // Add response to history
                List<Message> updatedHistory = task.getHistory() != null ?
                        Arrays.asList(task.getHistory().toArray(new Message[0])) :
                        Arrays.asList();

                updatedHistory = Arrays.asList(
                        java.util.stream.Stream.concat(
                                updatedHistory.stream(),
                                java.util.stream.Stream.of(message, responseMessage)
                        ).toArray(Message[]::new)
                );

                return new Task(
                        task.getId(),
                        task.getContextId(),
                        task.getKind(),
                        completedStatus,
                        task.getArtifacts(),
                        updatedHistory,
                        task.getMetadata()
                );

            } catch (Exception e) {
                e.printStackTrace();
                return createErrorTask(task, "task failed: " + e.getMessage());
            }
        };
    }

    /**
     * Extract text content from message parts
     */
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

    /**
     * Create error task for translation failures
     */
    private Task createErrorTask(Task originalTask, String errorMessage) {
        TaskStatus errorStatus = new TaskStatus(
                TaskState.FAILED,
                null,
                Instant.now().toString()
        );

        return new Task(
                originalTask.getId(),
                originalTask.getContextId(),
                originalTask.getKind(),
                errorStatus,
                originalTask.getArtifacts(),
                originalTask.getHistory(),
                originalTask.getMetadata()
        );
    }
}