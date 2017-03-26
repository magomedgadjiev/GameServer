package Models;

import user.UserProfile;

import java.util.List;

public class RespWithUsers {
    private int key;
    private List<UserProfile> userProfile;

    public RespWithUsers(int key, List<UserProfile> userProfile, String message) {
        this.key = key;
        this.userProfile = userProfile;
    }

    public RespWithUsers() {
        ;
    }

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
