package com.wht.server.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.wht.server.A2AServer;
import com.wht.server.model.*;
import org.noear.solon.annotation.*;
import org.noear.solon.web.sse.SseEmitter;
import org.noear.solon.web.sse.SseEvent;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * A2A REST controller for handling JSON-RPC requests
 */
@Controller
public class A2AController {

    @Inject
    A2AServer server;

    /**
     * Handle JSON-RPC requests
     */
    @Post
    @Mapping("a2a")
    public JSONRPCResponse handleJsonRpcRequest(@Body JSONRPCRequest request) {

        System.err.println("handleJsonRpcRequest(), request = " + request);

        if (!"2.0".equals(request.getJsonrpc())) {
            JSONRPCError error = new JSONRPCError(
                    ErrorCode.INVALID_REQUEST.getValue(),
                    "Invalid JSON-RPC version",
                    null
            );
            JSONRPCResponse response = new JSONRPCResponse(
                    request.getId(),
                    "2.0",
                    null,
                    error
            );
            return response;
        }

        if ("message/send".equals(request.getMethod())) {
            return server.handleTaskSend(request);
        }

        if ("tasks/get".equals(request.getMethod())) {
            return server.handleTaskGet(request);
        }

        if ("tasks/cancel".equals(request.getMethod())) {
            return server.handleTaskCancel(request);
        }

       return new JSONRPCResponse(
                request.getId(),
                "2.0",
                null,
                new JSONRPCError(
                        ErrorCode.METHOD_NOT_FOUND.getValue(),
                        "Method not found",
                        null
                )
        );

    }

    /**
     * Handle streaming task requests (Server-Sent Events)
     */
    @Post
    @Mapping("a2a/streaming")
    public SseEmitter handleStreamingTask(@Body JSONRPCRequest request) {

        System.err.println("handleStreamingTask(), request = " + request);

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Process task asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                if (!"message/send".equals(request.getMethod())) {
                    sendErrorEvent(emitter, request.getId(), ErrorCode.METHOD_NOT_FOUND, "Method not found");
                    return;
                }

                TaskSendParams params = BeanUtil.copyProperties(request.getParams(), TaskSendParams.class);

                // Create initial status with timestamp
                TaskStatus initialStatus = new TaskStatus(
                    TaskState.WORKING,
                    null,  // No message initially
                    Instant.now().toString()
                );

                // Send initial status update
                TaskStatusUpdateEvent initialEvent = new TaskStatusUpdateEvent(
                        params.getId(),
                        initialStatus,
                        false,  // final
                        null    // metadata
                );

                SendTaskStreamingResponse initialResponse = new SendTaskStreamingResponse(
                        request.getId(),
                        "2.0",
                        initialEvent,
                        null
                );

                emitter.send(new SseEvent().name("task-complete").data(JSONUtil.toJsonStr(initialResponse)));

                // Process task
                JSONRPCResponse taskResponse = server.handleTaskSend(request);

                if (taskResponse.getError() != null) {
                    sendErrorEvent(emitter, request.getId(), ErrorCode.INTERNAL_ERROR, taskResponse.getError().getMessage());
                    return;
                }

                // Send final status update
                Task completedTask = (Task) taskResponse.getResult();
                TaskStatusUpdateEvent finalEvent = new TaskStatusUpdateEvent(
                        completedTask.getId(),
                        completedTask.getStatus(),
                        true,   // final
                        null    // metadata
                );

                SendTaskStreamingResponse finalResponse = new SendTaskStreamingResponse(
                        request.getId(),
                        "2.0",
                        finalEvent,
                        null
                );

                emitter.send(new SseEvent().name("task-update").data(JSONUtil.toJsonStr(finalResponse)));

                emitter.complete();

            } catch (Exception e) {
                sendErrorEvent(emitter, request.getId(), ErrorCode.INTERNAL_ERROR, e.getMessage());
            }
        });

        return emitter;
    }

    /**
     * Get agent card information
     */
    @Mapping("/.well-known/agent.json")
    public AgentCard getAgentCard() {

        System.err.println("getAgentCard()");
        AgentCard agentCard = server.getAgentCard();
        return agentCard;
    }

    /**
     * Send error event
     */
    private void sendErrorEvent(SseEmitter emitter, Object requestId, ErrorCode code, String message) {
        try {
            A2AError error = new A2AError(code, message, null);
            SendTaskStreamingResponse errorResponse = new SendTaskStreamingResponse(
                    requestId,
                    "2.0",
                    null,
                    error
            );

            emitter.send(new SseEvent().name("error").data(JSONUtil.toJsonStr(errorResponse)));

            emitter.error(new RuntimeException(message));

        } catch (IOException e) {
            emitter.error(e);
        }
    }
}
