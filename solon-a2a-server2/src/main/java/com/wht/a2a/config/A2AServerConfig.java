package com.wht.a2a.config;

import cn.hutool.core.collection.CollUtil;
import com.wht.a2a.A2AServer;
import com.wht.a2a.TaskHandler;
import com.wht.a2a.model.*;
import org.noear.solon.ai.chat.ChatConfig;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author by HaiTao.Wang on 2025/8/21.
 */
@Configuration
public class A2AServerConfig {

    @Bean
    public ChatModel build(@Inject("${solon.ai.demo}") ChatConfig config) {

        McpClientProvider clientProvider = McpClientProvider.builder()
                .channel(McpChannel.SSE)
                .apiUrl("http://localhost:9002/mcp/sse")
                .requestTimeout(Duration.ofSeconds(60))
                .build();

        return ChatModel.of(config).defaultToolsAdd(clientProvider).build();
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

        AgentProvider provider = new AgentProvider("ollama", "http://127.0.0.1:11434/api/chat");

        AgentCapabilities capabilities = new AgentCapabilities(false, false, false);

        //鉴权
        AgentAuthentication authentication = new AgentAuthentication(CollUtil.newArrayList("bearer"), null);

        AgentSkill skill = new AgentSkill(
                "ai-spot",
                "景区推荐",
                "专业的景区推荐助手。主要任务是推荐景点信息",
                CollUtil.newArrayList("旅游", "景区", "景点", "游玩"),
                CollUtil.newArrayList("示例：有哪些景区", "示例：有哪些玩的地方", "推荐一些景点"),
                CollUtil.newArrayList("text"),
                CollUtil.newArrayList("text"));

        return new AgentCard("spot_agent",
                "A professional scenic spot recommendation assistant. It can accurately recommend attractions and places to visit.",
                "http://localhost:10002/a2a",
                provider,
                "1.0.0",
                "http://localhost:10002/docs",
                capabilities,
                authentication,
                CollUtil.newArrayList("text"),
                CollUtil.newArrayList("text"),
                CollUtil.newArrayList(skill));
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
                        CollUtil.newArrayList(responsePart),
                        message.getContextId(),
                        task.getId(),
                        CollUtil.newArrayList(message.getMessageId()),
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
                        CollUtil.newArrayList(task.getHistory().toArray(new Message[0])) :
                        CollUtil.newArrayList();

                updatedHistory = CollUtil.newArrayList(
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
