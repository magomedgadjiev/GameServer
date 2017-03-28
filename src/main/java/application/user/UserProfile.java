package application.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {
    private String login;
    private String password;
    private String email;

    @JsonIgnore
    private int rating = 0;

    @JsonCreator
    public UserProfile(@JsonProperty("login") String login, @JsonProperty("password")String password, @JsonProperty("email")String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public UserProfile() {
        ;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public boolean isEmpty() {
        return email == null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}