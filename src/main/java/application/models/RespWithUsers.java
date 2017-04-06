package application.models;

import application.user.UserProfile;

import java.util.List;

public class RespWithUsers {
    private int key;
    
    private List<UserProfile> userProfiles;

    public List<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void addUser(UserProfile u) {
        this.userProfiles.add(u);
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
