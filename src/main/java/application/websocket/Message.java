package application.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 06/04/16.
 */
@SuppressWarnings("NullableProblems")
public class Message {
//    private @NotNull String type;
    private @NotNull String content;


    public @NotNull String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(@NotNull String content) {
        this.content = content;
    }
}
