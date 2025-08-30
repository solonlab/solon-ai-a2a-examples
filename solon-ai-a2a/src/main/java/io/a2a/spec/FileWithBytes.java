package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a file with its content provided directly as a base64-encoded string.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class FileWithBytes implements FileContent {
    String mimeType;
    String name;
    String bytes;

    public FileWithBytes(String mimeType, String name, String bytes) {
        this.mimeType = mimeType;
        this.name = name;
        this.bytes = bytes;
    }

    @Override
    public String mimeType() {
        return mimeType;
    }

    @Override
    public String name() {
        return name;
    }

    public String bytes() {
        return bytes;
    }
}
