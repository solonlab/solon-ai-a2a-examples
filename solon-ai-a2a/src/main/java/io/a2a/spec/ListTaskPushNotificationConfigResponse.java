package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A response for a list task push notification config request.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public final class ListTaskPushNotificationConfigResponse extends JSONRPCResponse<List<TaskPushNotificationConfig>> {

    @JsonCreator
    public ListTaskPushNotificationConfigResponse(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("id") Object id,
                                                  @JsonProperty("result") List<TaskPushNotificationConfig> result,
                                                  @JsonProperty("error") JSONRPCError error) {
        super(jsonrpc, id, result, error, (Class<List<TaskPushNotificationConfig>>) (Class<?>) List.class);
    }

    public ListTaskPushNotificationConfigResponse(Object id, JSONRPCError error) {
        this(null, id, null, error);
    }

    public ListTaskPushNotificationConfigResponse(Object id, List<TaskPushNotificationConfig> result) {
        this(null, id,  result, null);
    }

}
