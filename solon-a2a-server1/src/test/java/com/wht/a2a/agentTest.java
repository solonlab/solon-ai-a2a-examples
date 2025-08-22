package com.wht.a2a;

import com.wht.server.Server1App;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.time.Duration;

@SolonTest(value = Server1App.class)
public class agentTest extends HttpTester {

    @SneakyThrows
    @Test
    public void test1() {

        McpClientProvider clientProvider = McpClientProvider.builder()
                .channel(McpChannel.SSE)
                .apiUrl("http://localhost:9001/mcp/sse")
                .requestTimeout(Duration.ofSeconds(60))
                .build();

//        Map<String, Object> map = MapUtil.newHashMap();
//        map.put("location", "合肥");
//
//        String content = clientProvider.callToolAsText("getWeather", map).getContent();
//
//        System.err.println(content);

        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
                .provider("ollama")
                .model("qwen2.5:latest")
                .defaultToolsAdd(clientProvider)
                .build();

        String content = chatModel.prompt("合肥天气怎么样").call().getMessage().getResultContent();
        System.err.println( content);

    }





}
