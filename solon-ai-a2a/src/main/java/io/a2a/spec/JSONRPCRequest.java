package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;

import static io.a2a.util.Utils.defaultIfNull;

/**
 * Represents a JSONRPC request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class JSONRPCRequest<T> implements JSONRPCMessage {

    protected String jsonrpc;
    protected Object id;
    protected String method;
    protected T params;

    public JSONRPCRequest() {
    }

    public JSONRPCRequest(String jsonrpc, Object id, String method, T params) {
        Assert.checkNotNullParam("jsonrpc", jsonrpc);
        Assert.checkNotNullParam("method", method);
        Assert.isNullOrStringOrInteger(id);
        this.jsonrpc = defaultIfNull(jsonrpc, JSONRPC_VERSION);
        this.id = id;
        this.method = method;
        this.params = params;
    }

    @Override
    public String getJsonrpc() {
        return this.jsonrpc;
    }

    @Override
    public Object getId() {
        return this.id;
    }

    public String getMethod() {
        return this.method;
    }

    public T getParams() {
        return this.params;
    }
}
