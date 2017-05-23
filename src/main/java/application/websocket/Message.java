package application.websocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 06/04/16.
 */
@SuppressWarnings("NullableProblems")
public class Message<T> {
    private @NotNull int type;

    private @NotNull T content;

    @JsonCreator
    public Message(@JsonProperty("type") int type, @JsonProperty("content") T content) {
        this.content = content;
        this.type = type;
    }

    public @NotNull T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Message(@NotNull T content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(@NotNull int type) {
        this.type = type;
    }
}
