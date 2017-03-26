package accountService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import user.UserProfile;

import java.util.*;


@Service(value = "singleton")
@Component
public class AccountService {
    private Map<String, UserProfile> profiles = new HashMap<String, UserProfile>();


    public void addUser(UserProfile userProfile) {
        profiles.put(userProfile.getEmail(), userProfile);
    }

    public int getSize() {
        return profiles.size();
    }

    public UserProfile getUser(String id) {
        return profiles.get(id);
    }

    public boolean isSignUp(String email) {
        Set<Map.Entry<String, UserProfile>> set = profiles.entrySet();
        for (Map.Entry<String, UserProfile> profile : set) {
            if (profile.getValue().getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public List<UserProfile> sort() {
        List<UserProfile> userProfiles = (ArrayList<UserProfile>) profiles.values();
        Collections.sort(userProfiles);
        return userProfiles;
    }
}
