package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Parameters for fetching a pushNotificationConfiguration associated with a Task.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class GetTaskPushNotificationConfigParams {
    String id;
    String pushNotificationConfigId;
    Map<String, Object> metadata;

    public GetTaskPushNotificationConfigParams(String id, String pushNotificationConfigId, Map<String, Object> metadata) {
        Assert.checkNotNullParam("id", id);

        this.id = id;
        this.pushNotificationConfigId = pushNotificationConfigId;
        this.metadata = metadata;
    }

    public GetTaskPushNotificationConfigParams(String id) {
        this(id, null, null);
    }

    public GetTaskPushNotificationConfigParams(String id, String pushNotificationConfigId) {
        this(id, pushNotificationConfigId, null);
    }

    public String id() {
        return id;
    }

    public String pushNotificationConfigId() {
        return pushNotificationConfigId;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public static class Builder {
        String id;
        String pushNotificationConfigId;
        Map<String, Object> metadata;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder pushNotificationConfigId(String pushNotificationConfigId) {
            this.pushNotificationConfigId = pushNotificationConfigId;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public GetTaskPushNotificationConfigParams build() {
            return new GetTaskPushNotificationConfigParams(id, pushNotificationConfigId, metadata);
        }
    }
}
