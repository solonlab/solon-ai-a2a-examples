package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a non-streaming JSON-RPC request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = NonStreamingJSONRPCRequestDeserializer.class)
@NoArgsConstructor
@Data
public abstract class NonStreamingJSONRPCRequest<T> extends JSONRPCRequest<T> {
}
