package com.wht.a2a;


import cn.hutool.json.JSONUtil;
import com.wht.a2a.model.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.noear.solon.net.http.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A2A protocol client implementation
 */
public class A2AClient {

    private final String baseUrl;

    /**
     * Create a new A2A client
     *
     * @param baseUrl the base URL of the A2A server
     */
    public A2AClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;

    }

    /**
     * Send a task message to the agent
     *
     * @param params task send parameters
     * @return JSON-RPC response containing the task
     */
    public JSONRPCResponse sendTask(TaskSendParams params) throws Exception {
        JSONRPCRequest request = new JSONRPCRequest(
                generateRequestId(),
                "2.0",
                "message/send",
                params
        );

        return doRequest(request);
    }

    /**
     * Get the status of a task
     *
     * @param params task query parameters
     * @return JSON-RPC response containing the task
     */
    public JSONRPCResponse getTask(TaskQueryParams params) throws Exception {
        JSONRPCRequest request = new JSONRPCRequest(
                generateRequestId(),
                "2.0",
                "tasks/get",
                params
        );

        return doRequest(request);
    }

    /**
     * Cancel a task
     *
     * @param params task ID parameters
     * @return JSON-RPC response containing the task
     */
    public JSONRPCResponse cancelTask(TaskIDParams params) throws Exception {
        JSONRPCRequest request = new JSONRPCRequest(
                generateRequestId(),
                "2.0",
                "tasks/cancel",
                params
        );

        return doRequest(request);
    }

    /**
     * Send a task with streaming response
     *
     * @param params   task send parameters
     * @param listener event listener for streaming updates
     * @return CompletableFuture that completes when streaming ends
     */
    public CompletableFuture<Void> sendTaskStreaming(TaskSendParams params, StreamingEventListener listener) {
        return CompletableFuture.runAsync(() -> {
            try {

                JSONRPCRequest request = new JSONRPCRequest(
                        generateRequestId(),
                        "2.0",
                        "message/send",
                        params
                );

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .build();

                String requestBody = JSONUtil.toJsonStr(request);

                RequestBody body = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        requestBody);
                // 请求对象
                Request httpRequest = new Request.Builder()
                        .url(baseUrl + "/a2a/stream")
                        .header("Content-Type", "application/json")
                        .header("Accept", "text/event-stream")
                        .post(body)
                        .build();

                try (Response response = client.newCall(httpRequest).execute()) {
                    if (response.code() != 200) {
                        listener.onError(new Exception("HTTP " + response.code() + ": " + response.body()));
                        return;
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }

                        try {
                            SendTaskStreamingResponse streamingResponse = JSONUtil.toBean(line, SendTaskStreamingResponse.class);

                            if (streamingResponse.getError() != null) {
                                A2AError error = streamingResponse.getError();
                                Integer errorCode = error.getCode() != null ? error.getCode().getValue() : null;
                                listener.onError(new Exception(
                                        error.getMessage()
                                ));
                                return;
                            }

                            if (streamingResponse.getResult() != null) {
                                listener.onEvent(streamingResponse.getResult());
                            }
                        } catch (Exception e) {
                            listener.onError(new Exception("Failed to parse streaming response", e));
                            return;
                        }


                    }

                    listener.onComplete();

                }

            } catch (Exception e) {
                listener.onError(new Exception("Streaming request failed", e));
            }
        });
    }

    /**
     * Get agent card information
     *
     * @return the agent card
     */
    public AgentCard getAgentCard() {

        return HttpUtils.http(baseUrl + "/.well-known/agent.json")
                .header("Accept", "application/json")
                .getAs(AgentCard.class);

    }

    /**
     * Perform HTTP request and handle response
     */
    private JSONRPCResponse doRequest(JSONRPCRequest request) throws Exception {

        try {

            JSONRPCResponse response = HttpUtils.http(baseUrl + "/a2a")
                    .header("Content-Type", "application/json")
                    .bodyOfBean(request)
                    .postAs(JSONRPCResponse.class);

            return response;

        } catch (Exception e) {
            throw new Exception("Request failed", e);
        }
    }

    /**
     * Generate a unique request ID
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
} 