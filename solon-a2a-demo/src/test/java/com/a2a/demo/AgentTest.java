package com.a2a.demo;

import com.a2a.demo.tool.DemoTools;

import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.annotation.Inject;
import org.noear.solon.test.SolonTest;

@SolonTest(value = ClientApp.class)
public class AgentTest {
    @Inject
    HostAgent hostAgent;

    @Inject
    DemoTools demoTools;

    @SneakyThrows
    @Test
    public void test1() {
        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
                .provider("ollama")
                .model("qwen2.5:latest")
                .defaultToolsAdd(demoTools)
                .build();

        hostAgent.addChatModel(chatModel);

        hostAgent.register("http://localhost:10001");
        hostAgent.register("http://localhost:10002");

        ChatResponse chatResponse = hostAgent.chat("杭州今天的天气适合去哪里玩？");

        System.err.println(chatResponse.getMessage().getResultContent());
    }
}
