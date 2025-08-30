package com.a2a.server1;

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
        return ChatModel.of(config).defaultToolsAdd(new Server1Tools()).build();
    }

    @Bean
    public AgentExecutor agentExecutor(@Inject ChatModel chatModel) {
        return new ChatModelAgentExecutor(chatModel);
    }

    @Bean
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                .name("weather_agent")
                .description("A professional weather forecast assistant. It can accurately predict the weather and temperature.")
                .url("http://localhost:10001")
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
                        .id("ai-weather")
                        .name("天气服务")
                        .description("专业的天气预报助手。主要任务是利用所提供的工具获取并传递天气信息")
                        .tags(Arrays.asList("天气", "天气预报", "温度", "气温"))
                        .examples(Arrays.asList("示例：今天的天气", "示例：今天的气温", "近三天的天气预报"))
                        .build()))
                .protocolVersion("0.2.5")
                .build();
    }
}
