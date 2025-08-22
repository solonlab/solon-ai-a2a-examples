package com.wht.a2a;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.wht.a2a.model.AgentCard;
import lombok.Data;
import lombok.SneakyThrows;
import org.noear.solon.ai.chat.ChatModel;
import org.noear.solon.ai.chat.ChatResponse;
import org.noear.solon.ai.chat.message.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by HaiTao.Wang on 2025/8/21.
 */
@Data
public class HostAgent {

    private ChatModel chatModel = null;
    private final List<AgentCard> agentCards;

    // name - description
    private final List<Map<String, String>> agentInfo;

    public final Map<String, A2AClient> a2AClientMap;

    public HostAgent() {
        this.agentCards = CollUtil.newArrayList();
        this.agentInfo = CollUtil.newArrayList();
        this.a2AClientMap = MapUtil.newHashMap();
    }

    public void addChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public void register(String remoteAddress) {

        if (remoteAddress.isEmpty()) {
            System.err.println("No remote address provided. Skipping agent registration.");
            return;
        }

        AgentCard agentCard = new A2AClient(remoteAddress).getAgentCard();
        agentCards.add(agentCard);

        a2AClientMap.put(agentCard.getName(), new A2AClient(remoteAddress));

        agentInfo.add(new HashMap<String, String>() {
            {
                put("agentName", agentCard.getName());
                put("description", agentCard.getDescription());
            }
        });
    }

    @SneakyThrows
    public ChatResponse chat(String userMessage) {

        String systemPrompt = StrUtil.format(
                "您是一位擅长分配任务的专家，负责将用户请求分解为子代理可以执行的任务。能够将用户请求分配给合适的远程代理。\n" +
                        "\n" +
                        "发现：\n" +
                        "- 你可以使用工具 `list_remote_agents` 列出可用于分配任务的远程代理。\n" +
                        "\n" +
                        "执行：\n" +
                        "- 对于可操作的请求，您可以使用工具 `send_message` 与远程代理交互以获取结果。\n" +
                        " \n" +
                        "请在回复用户时包含远程代理的名称。\n" +
                        "\n" +
                        "请依靠工具来处理请求，不要编造回复。如果您不确定，请向用户询问更多细节。\n" +
                        "主要关注对话的最新部分。将子任务的答案代入回答每个子任务的结果。\n" +
                        "\n" +
                        "代理:\n" +
                        "{}",
                StrUtil.join("\n", agentInfo)
        );

        return chatModel.prompt(ChatMessage.ofSystem(systemPrompt), ChatMessage.ofUser(userMessage)).call();

    }


}
