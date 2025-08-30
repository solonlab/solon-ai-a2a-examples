package io.a2a.client.apps.solon;

import io.a2a.A2A;
import io.a2a.client.A2AClient;
import io.a2a.spec.*;
import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author haiTao.Wang on 2025/8/21.
 */
public class HostAgentAssistantTools {
    static final Logger log = LoggerFactory.getLogger(HostAgentAssistantTools.class);

    private HostAgent hostAgent;

    public HostAgentAssistantTools(HostAgent hostAgent) {
        this.hostAgent = hostAgent;
    }

    @ToolMapping(description = "List the available remote agents you can use to delegate the task.")
    public List<Map<String, String>> list_remote_agents() {
        if (log.isDebugEnabled()) {
            log.debug("list_remote_agents:" + hostAgent.getAgentInfo());
        }

        return hostAgent.getAgentInfo();
    }

    @ToolMapping(description = "发送一个任务，要么以流式传输（如果支持的话），要么以非流式传输。这将向名为 agent_name 的远程代理发送一条消息。")
    public String send_message(@Param(description = "要将任务发送给的代理的名称") String agentName,
                               @Param(description = "需要发送给执行该任务的代理的信息") String message) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("send_message:" + agentName + ":" + message);
        }


        Message taskMessage = A2A.toUserMessage(message);
        MessageSendParams taskSendParams = new MessageSendParams.Builder()
                .message(taskMessage)
                .build();

        A2AClient a2AClient = hostAgent.getA2AClientMap().get(agentName);

        SendMessageResponse messageResponse = a2AClient.sendMessage(taskSendParams);
        if (messageResponse.getResult() instanceof Message) {
            Message message1 = (Message) messageResponse.getResult();
            if (message1.getParts().size() > 0) {
                Part part1 = message1.getParts().get(0);
                if (part1 instanceof TextPart) {
                    return ((TextPart) part1).getText();
                }
            }
        }

        throw new IllegalStateException("Invalid response");
    }
}