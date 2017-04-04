package application.models;

import application.user.UserProfile;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RespWithUser {
    private int key;
    private UserProfile userProfile;

    @JsonCreator
    public RespWithUser(@JsonProperty("key") int key,@JsonProperty("user") UserProfile userProfile) {
        this.key = key;
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
