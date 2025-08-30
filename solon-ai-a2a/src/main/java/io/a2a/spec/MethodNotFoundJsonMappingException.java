package io.a2a.spec;

import lombok.Value;

@Value
public class MethodNotFoundJsonMappingException extends IdJsonMappingException {

    public MethodNotFoundJsonMappingException(String msg, Object id) {
        super(msg, id);
    }

    public MethodNotFoundJsonMappingException(String msg, Throwable cause, Object id) {
        super(msg, cause, id);
    }
}
