package application.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by magomed on 25.05.17.
 */
public class RatingUpdate {
    @JsonProperty
    private String username;
    @JsonProperty

    private String gameResult;

    @JsonCreator
    public RatingUpdate(@JsonProperty("username")String username,@JsonProperty("gameResult")String gameResult) {
        this.username = username;
        this.gameResult = gameResult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }
}
