package application.websocket;

import application.WebSocketConfig;
import application.user.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Solovyev on 02/11/2016.
 */
@Service
public class RemotePointService {
    private Map<String, WebSocketSession> profiles = new ConcurrentHashMap<>();

    public void registerUser(WebSocketSession webSocketSession){
        profiles.put(webSocketSession.getId(), webSocketSession);
    }

    public void removeUser(String key){
        profiles.remove(key);
    }

    public boolean isRegister(String key){
        return profiles.get(key) == null;
    }

}
