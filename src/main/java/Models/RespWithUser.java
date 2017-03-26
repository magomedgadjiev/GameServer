package Models;

import user.UserProfile;

public class RespWithUser {
    private int key;
    private UserProfile userProfile;

    public RespWithUser(int key, UserProfile userProfile) {
        this.key = key;
        this.userProfile = userProfile;
    }

    public RespWithUser() {
        ;
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
