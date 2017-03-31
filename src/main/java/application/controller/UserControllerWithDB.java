package application.controller;

import application.models.Resp;
import application.models.RespWithUser;
import application.models.RespWithUsers;
import application.user.UserProfile;
import application.user.UserProfileJDBCTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by magomed on 29.03.17.
 */
@RestController
@Component
@RequestMapping(value = "/api/DB")
public class UserControllerWithDB {
    public static final String EMAIL = "userEmail";
    public static final String LOGIN = "logIn";
    public static final String SUCCESS = "success";

    private static final Logger LOGGER = Logger.getLogger("UserControllerWithDB");

    private UserProfileJDBCTemplate userProfileJDBCTemplate;

    @RequestMapping(value = "/auth/signOut", method = RequestMethod.GET)
    public ResponseEntity<?> signOut(HttpSession session) throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            session.invalidate();
        }
        LOGGER.debug("Success signOut");
        return ResponseEntity.ok(new Resp(0, SUCCESS));
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (userProfile.isEmpty()) {
                LOGGER.debug("Not all required parameters provided");
                return new ResponseEntity<>(new Resp(2, "Not all required parameters provided"), HttpStatus.BAD_REQUEST);
            }
            userProfileJDBCTemplate.getUserProfile(userProfile.getEmail());
            if (session.getAttribute(LOGIN) == null) {
                session.setAttribute(LOGIN, true);
                session.setAttribute(EMAIL, userProfile.getEmail());
            }
            LOGGER.debug("Success login");
            return ResponseEntity.ok(new RespWithUser(0, userProfileJDBCTemplate.getUserProfile(userProfile.getEmail())));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug("did't registration");
            return new ResponseEntity<>(new Resp(1, "You did't registration"), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser(HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                LOGGER.debug("Success get info user");
                return ResponseEntity.ok(new RespWithUser(0, (userProfileJDBCTemplate.getUserProfile((String) (session.getAttribute(EMAIL))))));
            }
            LOGGER.debug("don't login");
            return new ResponseEntity<>(new Resp(1, "You don't login"), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/user/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> setInfoUser(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                if (!userProfile.isEmpty()) {
                    userProfileJDBCTemplate.update(userProfile.getLogin(), userProfile.getPassword(), userProfile.getEmail(), userProfile.getRating());
                    if (userProfile.getLogin() != null){
                        userProfileJDBCTemplate.updateNickname(userProfile.getLogin(), userProfile.getEmail());
                    }
                    if(userProfile.getPassword() != null){
                        userProfileJDBCTemplate.updatePassword(userProfile.getPassword(), userProfile.getEmail());
                    }
                    LOGGER.debug("User data succesfully updated");
                    return ResponseEntity.ok(new Resp(0, "User data succesfully updated"));
                }
                LOGGER.debug("Not all required parameters provided");
                return new ResponseEntity<>(new Resp(2, "Not all required parameters provided"), HttpStatus.BAD_REQUEST);
            }
            LOGGER.debug("don't login");
            return new ResponseEntity<>(new Resp(1, "you don't login"), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException e) {
            LOGGER.debug("This user alredy exist");
            return new ResponseEntity<>(new Resp(3, "This user alredy exist"), HttpStatus.CONFLICT);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (userProfile.isEmpty()) {
                LOGGER.debug("Not all required parameters provided");
                return new ResponseEntity<>(new Resp(2, "Not all required parameters provided"), HttpStatus.BAD_REQUEST);
            }
            userProfileJDBCTemplate.create(userProfile.getLogin(), userProfile.getPassword(), userProfile.getEmail());
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            LOGGER.debug("Success registration" + session.getId());
            return new ResponseEntity<>(new RespWithUser(0, userProfile), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            LOGGER.debug("This user alredy exist");
            return new ResponseEntity<>(new Resp(3, "This user alredy exist"), HttpStatus.CONFLICT);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/stats/{count}", method = RequestMethod.GET)
    public ResponseEntity<?> getMMR(@PathVariable(value = "count") int count) throws IOException {
        try {
            if (count > userProfileJDBCTemplate.getCount()) {
                return new ResponseEntity<>(new Resp(1, "count > countUser"), HttpStatus.BAD_REQUEST);
            }
            final List<UserProfile> userProfiles = userProfileJDBCTemplate.getUsers();
            final RespWithUsers respWithUsers = new RespWithUsers();
            userProfiles.sort((o1, o2) -> {
                if (o2.getRating() == o1.getRating()) {
                    return 0;
                } else if (o2.getRating() > o1.getRating()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (int i = 0; i < count; ++i) {
                respWithUsers.addUser(userProfiles.get(i));
            }
            respWithUsers.setKey(0);
            LOGGER.debug("Success get users");
            return ResponseEntity.ok(respWithUsers);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public UserControllerWithDB(UserProfileJDBCTemplate userProfileJDBCTemplate) {
        this.userProfileJDBCTemplate = userProfileJDBCTemplate;
        userProfileJDBCTemplate.dropTable();
        userProfileJDBCTemplate.createTable();
    }
}
