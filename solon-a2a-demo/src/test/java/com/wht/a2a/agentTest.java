package com.wht.a2a;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.time.Duration;

@SolonTest(value = ClientApp.class)
public class agentTest extends HttpTester {

    @Inject
    HostAgent hostAgent;

    @SneakyThrows
    @Test
    public void test1() {

        McpClientProvider clientProvider = McpClientProvider.builder()
                .channel(McpChannel.SSE)
                .apiUrl("http://localhost:8080/mcp/sse")
                .requestTimeout(Duration.ofSeconds(60))
                .build();

        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
                .provider("ollama")
                .model("qwen2.5:latest")
                .defaultToolsAdd(clientProvider)
                .build();

        hostAgent.addChatModel(chatModel);

        hostAgent.register("http://localhost:10001");
        hostAgent.register("http://localhost:10002");

        ChatResponse chatResponse = hostAgent.chat("杭州今天的天气适合去哪里玩？");

        System.err.println(chatResponse.getMessage().getResultContent());

    }





}
