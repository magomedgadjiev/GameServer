package application.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class UserProfile implements Comparable<UserProfile> {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String email;
    @JsonProperty
    private int wins;
    @JsonProperty
    private int losses;
    @JsonProperty
    private int draws;

    public UserProfile(String username, String password, String email, int wins, int losses, int draws) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
    }

    @JsonCreator
    public UserProfile(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("email") String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserProfile() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(@NotNull UserProfile o) {
        if (wins - losses == o.wins - o.losses) {
            return 0;
        } else if (wins - losses > o.wins - o.losses) {
            return 1;
        } else {
            return -1;
        }
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }
}