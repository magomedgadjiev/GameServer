package application.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile implements Comparable<UserProfile> {
    private String username;
    private String password;
    private String email;

    @JsonIgnore
    private int rating = 0;

    @JsonCreator
    public UserProfile(@JsonProperty("username") String username, @JsonProperty("password")String password, @JsonProperty("email")String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserProfile() {
        ;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public int compareTo(UserProfile o) {
        if (rating == o.rating) {
            return 0;
        } else if (rating > o.rating) {
            return 1;
        } else {
            return -1;
        }
    }
}