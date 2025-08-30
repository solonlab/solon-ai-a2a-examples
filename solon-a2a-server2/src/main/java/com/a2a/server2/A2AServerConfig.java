package com.a2a.server2;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.apps.solon.ChatModelAgentExecutor;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import org.noear.solon.ai.chat.ChatConfig;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

import java.util.Arrays;
import java.util.Collections;

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
    public AgentExecutor agentExecutor(@Inject ChatModel chatModel) {
        return new ChatModelAgentExecutor(chatModel);
    }

    @Bean
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                .name("spot_agent")
                .description("A professional scenic spot recommendation assistant. It can accurately recommend attractions and places to visit.")
                .url("http://localhost:9999")
                .version("1.0.0")
                //.provider(new AgentProvider("ollama", "http://127.0.0.1:11434/api/chat"))
                .documentationUrl("http://example.com/docs")
                .capabilities(new AgentCapabilities.Builder()
                        .streaming(false)
                        .pushNotifications(false)
                        .stateTransitionHistory(false)
                        .build())
                .defaultInputModes(Collections.singletonList("text"))
                .defaultOutputModes(Collections.singletonList("text"))
                .skills(Collections.singletonList(new AgentSkill.Builder()
                        .id("ai-spot")
                        .name("景区推荐")
                        .description("专业的景区推荐助手。主要任务是推荐景点信息")
                        .tags(Arrays.asList("旅游", "景区", "景点", "游玩"))
                        .examples(Arrays.asList("示例：有哪些景区", "示例：有哪些玩的地方", "推荐一些景点"))
                        .build()))
                .protocolVersion("0.2.5")
                .build();
    }
}