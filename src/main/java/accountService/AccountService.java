package accountService;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import user.UserProfile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.function.Consumer;


@Service(value = "singleton")
@Component
public class AccountService {
    private Map<String, UserProfile> profiles = new HashMap<String, UserProfile>();
    private Map<String, UserProfile> sessionMap = new HashMap<String, UserProfile>();

    public void addUser(UserProfile userProfile){
        profiles.put(userProfile.getEmail(), userProfile);
    }
    public void addSession(String id, UserProfile userProfile){
        sessionMap.put(id, userProfile);
    }

    public UserProfile getUser(String id){
        return profiles.get(id);
    }

    public UserProfile getUserOfSession(String id)   { return sessionMap.get(id); }

    public boolean isSignUp(String email){
        Set<Map.Entry<String, UserProfile>> set = profiles.entrySet();
        for (Map.Entry<String, UserProfile> profile: set) {
            if (profile.getValue().getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
    public boolean isLogIn(String id){
        return sessionMap.containsKey(id);
    }

    public void deleteSession(String id){
        sessionMap.remove(id);
    }
}
