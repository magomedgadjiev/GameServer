package application.controller;

import application.config.ResponseMessage;
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

    private static final Logger LOGGER = Logger.getLogger("UserControllerWithDB");

    private UserProfileJDBCTemplate userProfileJDBCTemplate;

    @RequestMapping(value = "/auth/signOut", method = RequestMethod.GET)
    public ResponseEntity<?> signOut(HttpSession session) throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            session.invalidate();
        }
        LOGGER.debug(ResponseMessage.SUCCESS);
        return ResponseEntity.ok(new Resp(0, ResponseMessage.SUCCESS));
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (userProfile.isEmpty()) {
                LOGGER.debug(ResponseMessage.BAD_REQUEST);
                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            userProfileJDBCTemplate.getUserProfile(userProfile.getEmail());
            if (session.getAttribute(LOGIN) == null) {
                session.setAttribute(LOGIN, true);
                session.setAttribute(EMAIL, userProfile.getEmail());
            }
            LOGGER.debug(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(new RespWithUser(0, userProfileJDBCTemplate.getUserProfile(userProfile.getEmail())));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug(ResponseMessage.REGISTRATION);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.REGISTRATION), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ignored) {
            LOGGER.debug(ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser(HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                LOGGER.debug(ResponseMessage.SUCCESS);
                return ResponseEntity.ok(new RespWithUser(0, (userProfileJDBCTemplate.getUserProfile((String) (session.getAttribute(EMAIL))))));
            }
            LOGGER.debug(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ignored) {
            LOGGER.debug(ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/user/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> setInfoUser(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                if (!userProfile.isEmpty()) {
                    userProfileJDBCTemplate.updateUserProfile(userProfile);
                    LOGGER.debug(ResponseMessage.SUCCESS);
                    return ResponseEntity.ok(new Resp(0, ResponseMessage.SUCCESS));
                }
                LOGGER.debug(ResponseMessage.BAD_REQUEST);
                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            LOGGER.debug(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException e) {
            LOGGER.debug(ResponseMessage.CONFLICT);
            return new ResponseEntity<>(new Resp(3, ResponseMessage.CONFLICT), HttpStatus.CONFLICT);
        } catch (RuntimeException ignored) {
            LOGGER.debug(ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> signUp(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (userProfile.isEmpty()) {
                LOGGER.debug(ResponseMessage.BAD_REQUEST);
                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            userProfileJDBCTemplate.create(userProfile.getUsername(), userProfile.getPassword(), userProfile.getEmail());
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            LOGGER.debug(ResponseMessage.SUCCESS + session.getId());
            return new ResponseEntity<>(new RespWithUser(0, userProfile), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            LOGGER.debug(ResponseMessage.CONFLICT);
            return new ResponseEntity<>(new Resp(3, ResponseMessage.CONFLICT), HttpStatus.CONFLICT);
        } catch (RuntimeException ignored) {
            LOGGER.debug(ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
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
            for (int i = 0; i < count; ++i) {
                respWithUsers.addUser(userProfiles.get(i));
            }
            respWithUsers.setKey(0);
            LOGGER.debug(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(respWithUsers);
        } catch (RuntimeException ignored) {
            LOGGER.debug(ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public UserControllerWithDB(UserProfileJDBCTemplate userProfileJDBCTemplate) {
        this.userProfileJDBCTemplate = userProfileJDBCTemplate;
    }
}
