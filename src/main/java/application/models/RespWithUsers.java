package application.models;

import application.user.UserProfile;

import java.util.List;

public class RespWithUsers {
    private int key;
    private List<UserProfile> userProfiles;

    public RespWithUsers(int key, List<UserProfile> userProfiles) {
        this.key = key;
        this.userProfiles = userProfiles;
    }

    public RespWithUsers() {

    }

    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void addUser(UserProfile userProf) {
        this.userProfiles.add(userProf);
    }

    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
