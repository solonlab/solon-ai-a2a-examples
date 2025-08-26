package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;

import java.util.Map;

/**
 * Parameters for removing pushNotificationConfiguration associated with a Task.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteTaskPushNotificationConfigParams {
    String id;
    String pushNotificationConfigId;
    Map<String, Object> metadata;

    public DeleteTaskPushNotificationConfigParams(String id, String pushNotificationConfigId, Map<String, Object> metadata) {
        Assert.checkNotNullParam("id", id);
        Assert.checkNotNullParam("pushNotificationConfigId", pushNotificationConfigId);

        this.id = id;
        this.pushNotificationConfigId = pushNotificationConfigId;
        this.metadata = metadata;
    }

    public DeleteTaskPushNotificationConfigParams(String id, String pushNotificationConfigId) {
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

        public DeleteTaskPushNotificationConfigParams build() {
            return new DeleteTaskPushNotificationConfigParams(id, pushNotificationConfigId, metadata);
        }
    }
}
