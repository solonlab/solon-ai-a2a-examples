package com.wht.a2a.config;

import com.wht.a2a.HostAgent;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

/**
 * @author by HaiTao.Wang on 2025/8/21.
 */
@Configuration
public class AiConfig {

//    @Bean
//    public ChatModel build(@Inject("${solon.ai.demo}") ChatConfig config) {
//
//        McpClientProvider clientProvider  = McpClientProvider.builder()
//                .channel(McpChannel.SSE)
//                .apiUrl("http://localhost:8080/mcp/sse")
//                .build();
//
//        return ChatModel.of(config).defaultToolsAdd(clientProvider).build();
//    }

    @Bean
    public HostAgent build() {

        return new HostAgent();
    }
}