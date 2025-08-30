package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.a2a.util.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A container associating a push notification configuration with a specific task.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class TaskPushNotificationConfig {
    String taskId;
    PushNotificationConfig pushNotificationConfig;

    public TaskPushNotificationConfig(String taskId, PushNotificationConfig pushNotificationConfig) {
        Assert.checkNotNullParam("taskId", taskId);
        Assert.checkNotNullParam("pushNotificationConfig", pushNotificationConfig);

        this.taskId = taskId;
        this.pushNotificationConfig = pushNotificationConfig;
    }

    public String taskId() {
        return taskId;
    }

    public PushNotificationConfig pushNotificationConfig() {
        return pushNotificationConfig;
    }
}
