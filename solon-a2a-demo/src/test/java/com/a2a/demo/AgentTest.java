package com.a2a.demo;

import org.junit.jupiter.api.Test;
import io.a2a_preview.client.HostAgent;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.test.SolonTest;

@SolonTest
public class AgentTest {

    @Test
    public void test1() throws Exception {
        HostAgent agent = new HostAgent();

        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
                .provider("ollama")
                .model("qwen2.5:latest")
                .build();

        agent.addChatModel(chatModel);

        agent.register("http://localhost:10001");
        agent.register("http://localhost:10002");

        ChatResponse chatResponse = agent.chatCall("杭州今天的天气适合去哪里玩？");

        System.err.println(chatResponse.getMessage().getResultContent());
    }
}
