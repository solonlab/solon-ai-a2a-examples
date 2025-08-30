package io.a2a.spec;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A fundamental unit with a Message or Artifact.
 * @param <T> the type of unit
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPart.class, name = "text"),
        @JsonSubTypes.Type(value = FilePart.class, name = "file"),
        @JsonSubTypes.Type(value = DataPart.class, name = "data")
})
@NoArgsConstructor
@Data
public abstract class Part<T> {
    public enum Kind {
        TEXT("text"),
        FILE("file"),
        DATA("data");

        private String kind;

        Kind(String kind) {
            this.kind = kind;
        }

        @JsonValue
        public String asString() {
            return this.kind;
        }
    }

    public abstract Kind getKind();

    public abstract Map<String, Object> getMetadata();

}