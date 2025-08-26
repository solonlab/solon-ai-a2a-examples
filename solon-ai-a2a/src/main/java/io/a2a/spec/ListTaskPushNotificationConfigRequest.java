package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.a2a.util.Assert;
import io.a2a.util.Utils;

import java.util.UUID;

/**
 * A list task push notification config request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ListTaskPushNotificationConfigRequest extends NonStreamingJSONRPCRequest<ListTaskPushNotificationConfigParams> {

    public static final String METHOD = "tasks/pushNotificationConfig/list";

    @JsonCreator
    public ListTaskPushNotificationConfigRequest(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("id") Object id,
                                                 @JsonProperty("method") String method,
                                                 @JsonProperty("params") ListTaskPushNotificationConfigParams params) {
        if (jsonrpc != null && ! jsonrpc.equals(JSONRPC_VERSION)) {
            throw new IllegalArgumentException("Invalid JSON-RPC protocol version");
        }
        Assert.checkNotNullParam("method", method);
        if (! method.equals(METHOD)) {
            throw new IllegalArgumentException("Invalid ListTaskPushNotificationConfigRequest method");
        }
        Assert.isNullOrStringOrInteger(id);
        this.jsonrpc = Utils.defaultIfNull(jsonrpc, JSONRPC_VERSION);
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public ListTaskPushNotificationConfigRequest(String id, ListTaskPushNotificationConfigParams params) {
        this(null, id, METHOD, params);
    }

    public static class Builder {
        private String jsonrpc;
        private Object id;
        private String method;
        private ListTaskPushNotificationConfigParams params;

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

        public Builder params(ListTaskPushNotificationConfigParams params) {
            this.params = params;
            return this;
        }

        public ListTaskPushNotificationConfigRequest build() {
            if (id == null) {
                id = UUID.randomUUID().toString();
            }
            return new ListTaskPushNotificationConfigRequest(jsonrpc, id, method, params);
        }
    }
}
