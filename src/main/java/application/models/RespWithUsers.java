package application.models;

import application.user.UserProfile;

import java.util.List;

public class RespWithUsers {
    private int key;
    private List<UserProfile> userProfile;

    public List<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void addUser(UserProfile userProfile) {
        this.userProfile.add(userProfile);
    }

    public void setUserProfile(List<UserProfile> userProfile) {
        this.userProfile = userProfile;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
