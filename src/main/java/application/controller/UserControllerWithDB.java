package application.controller;

import application.config.ResponseMessage;
import application.models.*;
import application.user.UserProfile;
import application.user.UserProfileJDBCTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by magomed on 29.03.17.
 */
@RestController
@Component
@RequestMapping(value = "/api/DB")
public class UserControllerWithDB {
    public static final String EMAIL = "userEmail";
    public static final String LOGIN = "logIn";

    private static final Logger LOGGER = Logger.getLogger(UserControllerWithDB.class);

    private UserProfileJDBCTemplate userProfileJDBCTemplate;

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/auth/signOut", method = RequestMethod.GET)
    public ResponseEntity<?> signOut(HttpSession session) throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            session.invalidate();
        }
        LOGGER.info(ResponseMessage.SUCCESS);
        return ResponseEntity.ok(new Resp(0, ResponseMessage.SUCCESS));
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() throws IOException {
        final RespWithUsers respWithUsers = new RespWithUsers(0, userProfileJDBCTemplate.getUsers(userProfileJDBCTemplate.getCount()));
        LOGGER.info("get all info db success");
        return ResponseEntity.ok(respWithUsers);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUser(@RequestBody UserProfile userProfile) throws IOException {
        userProfileJDBCTemplate.deleteUser(userProfile.getUsername());
        LOGGER.info("delete user success");
        return ResponseEntity.ok(null);
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUser(@RequestBody RatingUpdate ratingUpdate, HttpSession session) throws IOException {
        try {
            userProfileJDBCTemplate.updateRating(ratingUpdate.getUsername(), ratingUpdate.getGameResult());
            LOGGER.info(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(new Messager("ok"));
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            int key = 0;
            if (userProfile.getEmail().isEmpty()) {
                key = 10;
            }
            if (userProfile.getPassword().isEmpty()) {
                key += 1;
            }
            if (userProfile.ff(userProfile.getEmail())) {
                key = 30 + key % 10;
            }
            if (userProfile.ff(userProfile.getPassword())) {
                key = key / 10 + 3;
            }
            if (key != 0) {
                if (key / 10 == 0) {
                    key += 70;
                }
                return new ResponseEntity<>(new Resp(key, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            String passwoord = userProfile.getPassword();
            userProfile = userProfileJDBCTemplate.getUserProfileByEmail(userProfile.getEmail());
            if (userProfile == null) {
                LOGGER.warn(ResponseMessage.REGISTRATION);
                return new ResponseEntity<>(new Resp(2, ResponseMessage.REGISTRATION), HttpStatus.BAD_REQUEST);
            }
            if (!userProfile.getPassword().equals(passwoord)) {
                return new ResponseEntity<>(new Resp(2, ResponseMessage.REGISTRATION), HttpStatus.BAD_REQUEST);
            }
            if (session.getAttribute(LOGIN) == null) {
                session.setAttribute(LOGIN, true);
                session.setAttribute(EMAIL, userProfile.getEmail());
            }
            LOGGER.info(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(new RespWithUser(777, userProfile));
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser(HttpSession session) throws IOException {
        try {
            LOGGER.info(session.getId());
            if (session.getAttribute(LOGIN) != null) {
                LOGGER.info(ResponseMessage.SUCCESS);
                return ResponseEntity.ok(new RespWithUser(0, (userProfileJDBCTemplate.getUserProfileByEmail((String) (session.getAttribute(EMAIL))))));
            }
            LOGGER.warn(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.NOT_FOUND);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/user/user", method = RequestMethod.POST)
    public ResponseEntity<?> gg(@RequestBody RatingUpdate ratingUpdate, HttpSession session) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            final UserProfile userProfile = userProfileJDBCTemplate.getUserProfileByUsername(ratingUpdate.getUsername());
            return ResponseEntity.ok(objectMapper.writeValueAsString(userProfile));
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/user/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> setInfoUser(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                userProfileJDBCTemplate.updateUserProfile(userProfile);
                LOGGER.info(ResponseMessage.SUCCESS);
                return ResponseEntity.ok(new Resp(0, ResponseMessage.SUCCESS));
            }
            LOGGER.warn(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException e) {
            LOGGER.warn(ResponseMessage.CONFLICT);
            return new ResponseEntity<>(new Resp(3, ResponseMessage.CONFLICT), HttpStatus.CONFLICT);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
            int key = userProfile.isGoodInf();
            key = isDuplicate(userProfile.getUsername(), userProfile.getEmail(), key);
            if (key != 0) {
                if (key / 10 == 0) {
                    key += 70;
                }
                if (key / 100 == 0) {
                    key += 700;
                }
                return new ResponseEntity<>(new Resp(key, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            userProfileJDBCTemplate.create(userProfile.getUsername(), userProfile.getPassword(), userProfile.getEmail());
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            LOGGER.info(ResponseMessage.SUCCESS + session.getId());
            return new ResponseEntity<>(new RespWithUser(777, userProfile), HttpStatus.CREATED);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/stats/{count}", method = RequestMethod.GET)
    public ResponseEntity<?> getMMR(@PathVariable(value = "count", required = false) int count) throws IOException {
        try {
            final RespWithUsers respWithUsers = new RespWithUsers(0, userProfileJDBCTemplate.getUsers(count));
            LOGGER.info(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(respWithUsers);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(null);
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(null);
    }

    public int isDuplicate(String username, String email, int key) {
        if (key / 100 == 0) {
            if (userProfileJDBCTemplate.getUserProfileByEmail(email) != null) {
                key = 200 + (key % 100);
            }
        }
        if (key / 10 % 10 == 0) {
            if (userProfileJDBCTemplate.getUserProfileByUsername(username) != null) {
                int a = key % 100;
                int b = key / 100;
                key = b * 100 + 2 * 10 + a;
            }
        }
        return key;
    }

    @RequestMapping(
            value = "/{slug}/create",
            method = RequestMethod.POST
    )
    public final ResponseEntity<?> createThread(
            @PathVariable(value = "slug") final String slug
    ) {
        return ResponseEntity.ok(null);
    }

    @Autowired
    public UserControllerWithDB(UserProfileJDBCTemplate userProfileJDBCTemplate) {
        this.userProfileJDBCTemplate = userProfileJDBCTemplate;
//        userProfileJDBCTemplate.dropTable();
        userProfileJDBCTemplate.createTable();
    }
}
