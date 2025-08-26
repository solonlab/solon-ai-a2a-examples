package io.a2a.spec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.noear.solon.Utils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class JSONRPCErrorSerializationTest {
    @Test
    public void shouldDeserializeToCorrectJSONRPCErrorSubclass() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTemplate = "{\"code\": %s, \"message\": \"error\", \"data\": \"anything\"}";

        class ErrorCase {
            int code;
            Class<? extends JSONRPCError> clazz;

            public ErrorCase(int code, Class<? extends JSONRPCError> clazz) {
                this.code = code;
                this.clazz = clazz;
            }

            public int code() {
                return code;
            }

            public Class<? extends JSONRPCError> clazz() {
                return clazz;
            }
        }

        List<ErrorCase> cases = Arrays.asList(
                new ErrorCase(JSONParseError.DEFAULT_CODE, JSONParseError.class),
                new ErrorCase(InvalidRequestError.DEFAULT_CODE, InvalidRequestError.class),
                new ErrorCase(MethodNotFoundError.DEFAULT_CODE, MethodNotFoundError.class),
                new ErrorCase(InvalidParamsError.DEFAULT_CODE, InvalidParamsError.class),
                new ErrorCase(InternalError.DEFAULT_CODE, InternalError.class),
                new ErrorCase(PushNotificationNotSupportedError.DEFAULT_CODE, PushNotificationNotSupportedError.class),
                new ErrorCase(UnsupportedOperationError.DEFAULT_CODE, UnsupportedOperationError.class),
                new ErrorCase(ContentTypeNotSupportedError.DEFAULT_CODE, ContentTypeNotSupportedError.class),
                new ErrorCase(InvalidAgentResponseError.DEFAULT_CODE, InvalidAgentResponseError.class),
                new ErrorCase(TaskNotCancelableError.DEFAULT_CODE, TaskNotCancelableError.class),
                new ErrorCase(TaskNotFoundError.DEFAULT_CODE, TaskNotFoundError.class),
                new ErrorCase(Integer.MAX_VALUE, JSONRPCError.class) // Any unknown code will be treated as JSONRPCError
        );

        for (ErrorCase errorCase : cases) {
            String json = String.format(jsonTemplate, errorCase.code());
            JSONRPCError error;
            try {
                error = objectMapper.readValue(json, JSONRPCError.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            assertInstanceOf(errorCase.clazz(), error);
            assertEquals("error", error.getMessage());
            assertEquals("anything", error.getData().toString());
        }
    }
}