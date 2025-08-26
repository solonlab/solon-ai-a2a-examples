package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.a2a.util.Assert;

import java.util.UUID;

import static io.a2a.util.Utils.defaultIfNull;

/**
 * Used to set a task push notification request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SetTaskPushNotificationConfigRequest extends NonStreamingJSONRPCRequest<TaskPushNotificationConfig> {

    public static final String METHOD = "tasks/pushNotificationConfig/set";

    @JsonCreator
    public SetTaskPushNotificationConfigRequest(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("id") Object id,
                                                @JsonProperty("method") String method, @JsonProperty("params") TaskPushNotificationConfig params) {
        if (jsonrpc != null && ! jsonrpc.equals(JSONRPC_VERSION)) {
            throw new IllegalArgumentException("Invalid JSON-RPC protocol version");
        }
        Assert.checkNotNullParam("method", method);
        if (! method.equals(METHOD)) {
            throw new IllegalArgumentException("Invalid SetTaskPushNotificationRequest method");
        }
        Assert.checkNotNullParam("params", params);
        Assert.isNullOrStringOrInteger(id);
        this.jsonrpc = defaultIfNull(jsonrpc, JSONRPC_VERSION);
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public SetTaskPushNotificationConfigRequest(String id, TaskPushNotificationConfig taskPushConfig) {
        this(null, id, METHOD, taskPushConfig);
    }

    public static class Builder {
        private String jsonrpc;
        private Object id;
        private String method = METHOD;
        private TaskPushNotificationConfig params;

        public Builder jsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
            return this;
        }

        public Builder id(Object id) {
            this.id = id;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder params(TaskPushNotificationConfig params) {
            this.params = params;
            return this;
        }

        public SetTaskPushNotificationConfigRequest build() {
            if (id == null) {
                id = UUID.randomUUID().toString();
            }
            return new SetTaskPushNotificationConfigRequest(jsonrpc, id, method, params);
        }
    }
}
