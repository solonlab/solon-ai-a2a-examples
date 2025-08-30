package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The response after receiving a send message request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public final class SendMessageResponse extends JSONRPCResponse<EventKind> {

    @JsonCreator
    public SendMessageResponse(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("id") Object id,
                               @JsonProperty("result") EventKind result, @JsonProperty("error") JSONRPCError error) {
        super(jsonrpc, id, result, error, EventKind.class);
    }

    public SendMessageResponse(Object id, EventKind result) {
        this(null, id, result, null);
    }

    public SendMessageResponse(Object id, JSONRPCError error) {
        this(null, id, null, error);
    }
}
