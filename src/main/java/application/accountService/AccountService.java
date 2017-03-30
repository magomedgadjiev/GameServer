package application.accountService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import application.user.UserProfile;

import java.util.*;


@Service
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

    public boolean isSignUp(String email, String password) {
        Set<Map.Entry<String, UserProfile>> set = profiles.entrySet();
        for (Map.Entry<String, UserProfile> profile : set) {
            if (profile.getValue().getEmail().equals(email) && profile.getValue().getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public List<UserProfile> sort() {
        List<UserProfile> userProfiles = (ArrayList<UserProfile>) profiles.values();
        Collections.sort(userProfiles, new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile o1, UserProfile o2) {
                if (o2.getRating() == o1.getRating()) {
                    return 0;
                } else if (o2.getRating() > o1.getRating()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return userProfiles;
    }
}
