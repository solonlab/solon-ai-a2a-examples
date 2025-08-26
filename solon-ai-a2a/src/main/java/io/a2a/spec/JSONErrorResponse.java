package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONErrorResponse {
    String error;

    public JSONErrorResponse(String error) {
        this.error = error;
    }

    public String error() {
        return error;
    }
}
