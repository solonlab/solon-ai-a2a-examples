package com.a2a.demo;

import io.a2a.client.apps.solon.HostAgent;
import org.junit.jupiter.api.Test;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.test.SolonTest;

import java.time.Duration;

@SolonTest(ClientApp.class)
public class AgentTest {

    @Test
    public void test1() throws Exception {
        HostAgent agent = new HostAgent();

        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
                .provider("ollama")
                .model("qwen3:4b")
                .timeout(Duration.ofSeconds(600))
                .build();

        agent.addChatModel(chatModel);

        agent.register("http://localhost:10001");
        agent.register("http://localhost:10002");

        ChatResponse chatResponse = agent.chatCall("杭州今天的天气适合去哪里玩？");

        System.err.println(chatResponse.getMessage().getResultContent());
    }
}
