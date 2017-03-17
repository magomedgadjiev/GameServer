package jsonModels;

import user.UserProfile;

/**
 * Created by magomed on 17.03.17.
 */
public class JsonResponseWithUser {
    private Resp resp;
    private UserProfile userProfile;

    public JsonResponseWithUser(Resp resp, UserProfile userProfile) {
        this.resp = resp;
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }
}
