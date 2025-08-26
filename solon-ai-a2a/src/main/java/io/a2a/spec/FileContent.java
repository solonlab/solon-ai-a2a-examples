package io.a2a.spec;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = FileContentDeserializer.class)
public interface FileContent {

    String mimeType();

    String name();
}
