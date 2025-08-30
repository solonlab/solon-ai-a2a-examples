package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a file with its content located at a specific URI.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class FileWithUri implements FileContent {
    String mimeType;
    String name;
    String uri;

    public FileWithUri(String mimeType, String name, String uri) {
        this.mimeType = mimeType;
        this.name = name;
        this.uri = uri;
    }

    @Override
    public String mimeType() {
        return mimeType;
    }

    @Override
    public String name() {
        return name;
    }

    public String uri() {
        return uri;
    }
}

