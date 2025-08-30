package io.a2a.solon.integration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonNode;
import io.a2a.server.ExtendedAgentCard;
import io.a2a.server.requesthandlers.JSONRPCHandler;
import io.a2a.server.util.async.Internal;
import io.a2a.spec.*;
import io.a2a.spec.InternalError;
import io.a2a.util.Utils;
import io.smallrye.mutiny.Multi;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.MimeType;
import org.noear.solon.server.handle.HeaderNames;
import org.noear.solon.web.sse.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;


public class A2AServerEndpoint {

    @Inject
    JSONRPCHandler jsonRpcHandler;

    @ExtendedAgentCard
    AgentCard extendedAgentCard;

    @Internal
    Executor executor;

    @Consumes(MimeType.APPLICATION_JSON_VALUE)
    @Post
    @Mapping("/")
    public void invokeJSONRPCHandler(@Body String body, Context rc) throws Throwable {
        boolean streaming = false;
        JSONRPCResponse<?> nonStreamingResponse = null;
        Multi<? extends JSONRPCResponse<?>> streamingResponse = null;
        JSONRPCErrorResponse error = null;

        try {
            if (isStreamingRequest(body)) {
                streaming = true;
                StreamingJSONRPCRequest<?> request = Utils.OBJECT_MAPPER.readValue(body, StreamingJSONRPCRequest.class);
                streamingResponse = processStreamingRequest(request);
            } else {
                NonStreamingJSONRPCRequest<?> request = Utils.OBJECT_MAPPER.readValue(body, NonStreamingJSONRPCRequest.class);
                nonStreamingResponse = processNonStreamingRequest(request);
            }
        } catch (JsonProcessingException e) {
            error = handleError(e);
        } catch (Throwable t) {
            error = new JSONRPCErrorResponse(new InternalError(t.getMessage()));
        } finally {
            if (error != null) {
                rc.headerSet(HeaderNames.HEADER_CONTENT_TYPE, MimeType.APPLICATION_JSON_VALUE);
                rc.output(Utils.OBJECT_MAPPER.writeValueAsBytes(error));
            } else if (streaming) {
                final Multi<? extends JSONRPCResponse<?>> finalStreamingResponse = streamingResponse;
                SseEmitter sseEmitter = new SseEmitter(-1);

                sseEmitter.onInited(emitter -> {
                    finalStreamingResponse.subscribe(new Flow.Subscriber<JSONRPCResponse<?>>() {
                        private Flow.Subscription subscription;
                        @Override
                        public void onSubscribe(Flow.Subscription subscription) {
                            this.subscription = subscription;
                            subscription.request(1);
                        }

                        @Override
                        public void onNext(JSONRPCResponse<?> item) {
                            try {
                                sseEmitter.send(Utils.OBJECT_MAPPER.writeValueAsString(item));
                                subscription.request(1);
                            } catch (IOException e) {
                                sseEmitter.error(e);
                                subscription.cancel();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            sseEmitter.error(e);
                        }

                        @Override
                        public void onComplete() {
                            sseEmitter.complete();
                        }
                    });
                });

               rc.returnValue(sseEmitter);
            } else {
                rc.headerSet(HeaderNames.HEADER_CONTENT_TYPE, MimeType.APPLICATION_JSON_VALUE);
                rc.output(Utils.OBJECT_MAPPER.writeValueAsBytes(nonStreamingResponse));
            }
        }
    }

    private JSONRPCErrorResponse handleError(JsonProcessingException exception) {
        Object id = null;
        JSONRPCError jsonRpcError = null;
        if (exception.getCause() instanceof JsonParseException) {
            jsonRpcError = new JSONParseError();
        } else if (exception instanceof JsonEOFException) {
            jsonRpcError = new JSONParseError(exception.getMessage());
        } else if (exception instanceof MethodNotFoundJsonMappingException) {
            MethodNotFoundJsonMappingException err = (MethodNotFoundJsonMappingException) exception;
            id = err.getId();
            jsonRpcError = new MethodNotFoundError();
        } else if (exception instanceof InvalidParamsJsonMappingException) {
            InvalidParamsJsonMappingException err = (InvalidParamsJsonMappingException) exception;
            id = err.getId();
            jsonRpcError = new InvalidParamsError();
        } else if (exception instanceof IdJsonMappingException) {
            IdJsonMappingException err =  (IdJsonMappingException) exception;
            id = err.getId();
            jsonRpcError = new InvalidRequestError();
        } else {
            jsonRpcError = new InvalidRequestError();
        }
        return new JSONRPCErrorResponse(id, jsonRpcError);
    }

    /**
    /**
     * Handles incoming GET requests to the agent card endpoint.
     * Returns the agent card in JSON format.
     *
     * @return the agent card
     */
    @Produces(MimeType.APPLICATION_JSON_VALUE)
    @Get
    @Mapping("/.well-known/agent-card.json")
    public AgentCard getAgentCard() {
        return jsonRpcHandler.getAgentCard();
    }

    /**
     * Handles incoming GET requests to the authenticated extended agent card endpoint.
     * Returns the agent card in JSON format.
     *
     */
    @Get
    @Mapping("/agent/authenticatedExtendedCard")
    public void getAuthenticatedExtendedAgentCard(Context re) {
        // TODO need to add authentication for this endpoint
        // https://github.com/a2aproject/a2a-java/issues/77
        try {
            if (! jsonRpcHandler.getAgentCard().supportsAuthenticatedExtendedCard()) {
                JSONErrorResponse errorResponse = new JSONErrorResponse("Extended agent card not supported or not enabled.");
                re.status(404);
                re.output(Utils.OBJECT_MAPPER.writeValueAsString(errorResponse));
                return;
            }
            if (extendedAgentCard == null) {
                JSONErrorResponse errorResponse = new JSONErrorResponse("Authenticated extended agent card is supported but not configured on the server.");
                re.status(404);
                re.output(Utils.OBJECT_MAPPER.writeValueAsString(errorResponse));
                return;
            }

            re.output(Utils.OBJECT_MAPPER.writeValueAsString(extendedAgentCard));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONRPCResponse<?> processNonStreamingRequest(NonStreamingJSONRPCRequest<?> request) {
        if (request instanceof GetTaskRequest) {
            return jsonRpcHandler.onGetTask((GetTaskRequest) request);
        } else if (request instanceof CancelTaskRequest) {
            return jsonRpcHandler.onCancelTask((CancelTaskRequest) request);
        } else if (request instanceof SetTaskPushNotificationConfigRequest) {
            return jsonRpcHandler.setPushNotificationConfig((SetTaskPushNotificationConfigRequest) request);
        } else if (request instanceof GetTaskPushNotificationConfigRequest) {
            return jsonRpcHandler.getPushNotificationConfig((GetTaskPushNotificationConfigRequest) request);
        } else if (request instanceof SendMessageRequest) {
            return jsonRpcHandler.onMessageSend((SendMessageRequest) request);
        } else if (request instanceof ListTaskPushNotificationConfigRequest) {
            return jsonRpcHandler.listPushNotificationConfig((ListTaskPushNotificationConfigRequest) request);
        } else if (request instanceof DeleteTaskPushNotificationConfigRequest) {
            return jsonRpcHandler.deletePushNotificationConfig((DeleteTaskPushNotificationConfigRequest) request);
        } else {
            return generateErrorResponse(request, new UnsupportedOperationError());
        }
    }

    private Multi<? extends JSONRPCResponse<?>> processStreamingRequest(JSONRPCRequest<?> request) {
        Flow.Publisher<? extends JSONRPCResponse<?>> publisher;
        if (request instanceof SendStreamingMessageRequest) {
            publisher = jsonRpcHandler.onMessageSendStream((SendStreamingMessageRequest) request);
        } else if (request instanceof TaskResubscriptionRequest) {
            publisher = jsonRpcHandler.onResubscribeToTask((TaskResubscriptionRequest) request);
        } else {
            return Multi.createFrom().item(generateErrorResponse(request, new UnsupportedOperationError()));
        }
        return Multi.createFrom().publisher(publisher);
    }

    private JSONRPCResponse<?> generateErrorResponse(JSONRPCRequest<?> request, JSONRPCError error) {
        return new JSONRPCErrorResponse(request.getId(), error);
    }

    private static boolean isStreamingRequest(String requestBody) {
        try {
            JsonNode node = Utils.OBJECT_MAPPER.readTree(requestBody);
            JsonNode method = node != null ? node.get("method") : null;
            return method != null && (SendStreamingMessageRequest.METHOD.equals(method.asText())
                    || TaskResubscriptionRequest.METHOD.equals(method.asText()));
        } catch (Exception e) {
            return false;
        }
    }
}

