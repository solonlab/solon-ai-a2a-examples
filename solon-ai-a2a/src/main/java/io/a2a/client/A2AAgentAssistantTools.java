package io.a2a.client;

import io.a2a.spec.Message;
import io.a2a.spec.MessageSendParams;
import io.a2a.spec.TextPart;
import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author haiTao.Wang on 2025/8/21.
 */
public class A2AAgentAssistantTools {
    private A2AAgent hostAgent;

    public A2AAgentAssistantTools(A2AAgent hostAgent) {
        this.hostAgent = hostAgent;
    }

    @ToolMapping(description = "List the available remote agents you can use to delegate the task.")
    public List<Map<String, String>> list_remote_agents() {

        System.err.println("list_remote_agents:" + hostAgent.getAgentInfo());
        return hostAgent.getAgentInfo();
    }

    @ToolMapping(description = "发送一个任务，要么以流式传输（如果支持的话），要么以非流式传输。这将向名为 agent_name 的远程代理发送一条消息。")
    public String send_message(@Param(description = "要将任务发送给的代理的名称") String agentName,
                               @Param(description = "需要发送给执行该任务的代理的信息") String message) throws Exception {

        System.err.println("send_message:" + agentName + ":" + message);

        Message taskMessage = new Message.Builder().messageId("1").role(Message.Role.USER).parts( Arrays.asList(new TextPart(message))).build();
        MessageSendParams taskSendParams = new MessageSendParams.Builder()
                .message(taskMessage)
                .build();

        return hostAgent.getA2AClientMap().get(agentName).sendMessage(taskSendParams).getResult().toString();
    }
}