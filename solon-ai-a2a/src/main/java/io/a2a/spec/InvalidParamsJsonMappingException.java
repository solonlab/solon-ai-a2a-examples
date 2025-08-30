package io.a2a.spec;

import lombok.Value;

@Value
public class InvalidParamsJsonMappingException extends IdJsonMappingException {

    public InvalidParamsJsonMappingException(String msg, Object id) {
        super(msg, id);
    }

    public InvalidParamsJsonMappingException(String msg, Throwable cause, Object id) {
        super(msg, cause, id);
    }
}
