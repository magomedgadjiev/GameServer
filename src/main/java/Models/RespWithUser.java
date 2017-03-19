package Models;

import user.UserProfile;

/**
 * Created by magomed on 17.03.17.
 */
public class RespWithUser {
    private int key;
    private UserProfile userProfile;
    private String message;

    public RespWithUser(int key, UserProfile userProfile, String message) {
        this.key = key;
        this.userProfile = userProfile;
        this.message = message;
    }

    public RespWithUser(){
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
