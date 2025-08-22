package com.wht.a2a;

import cn.hutool.core.map.MapUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.noear.solon.ai.mcp.McpChannel;
import org.noear.solon.ai.mcp.client.McpClientProvider;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;

import java.time.Duration;
import java.util.Map;

@SolonTest(value = Server2App.class)
public class agentTest extends HttpTester {

    @SneakyThrows
    @Test
    public void test1() {

        McpClientProvider clientProvider = McpClientProvider.builder()
                .channel(McpChannel.STREAMABLE)
                .apiUrl("http://localhost:9002/mcp/sse")
                .requestTimeout(Duration.ofSeconds(60))
                .build();

        Map<String, Object> map = MapUtil.newHashMap();
        map.put("weather", "晴朗");

        String content = clientProvider.callToolAsText("recommendTourist", map).getContent();

        System.err.println(content);

//        ChatModel chatModel = ChatModel.of("http://127.0.0.1:11434/api/chat")
//                .provider("ollama")
//                .model("qwen2.5:latest")
//                .defaultToolsAdd(clientProvider)
//                .build();
//
//        String content = chatModel.prompt("合肥天气怎么样").call().getMessage().getResultContent();
//        System.err.println( content);

    }





}
