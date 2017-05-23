package application.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;

/**
 * Created by magomed on 17.05.17.
 */
@Service
public class GameSession {
    @JsonProperty
    private String loginFirst;
    @JsonIgnore
    private String first;
    @JsonProperty
    private String loginSecond;
    @JsonIgnore
    private String second;
    @JsonProperty
    private String field;

    public GameSession(){
    }

    public GameSession(String loginFirst, String loginSecond, String field) {
        this.loginFirst = loginFirst;
        this.loginSecond = loginSecond;
        this.field = field;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isEmpty(){
        return first == null;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GameSession gameSession = (GameSession) o;
        return loginFirst.equals(gameSession.loginFirst) || loginSecond.equals(gameSession.loginSecond);
    }

    public boolean isEnd(){
        switch (field) {
            case "KREST":{
                return true;
            }
            case "ZERO":{
                return true;
            }
            case "NICH":{
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public String getLoginFirst() {
        return loginFirst;
    }

    public void setLoginFirst(String loginFirst) {
        this.loginFirst = loginFirst;
    }

    public String getLoginSecond() {
        return loginSecond;
    }

    public void setLoginSecond(String loginSecond) {
        this.loginSecond = loginSecond;
    }
}
