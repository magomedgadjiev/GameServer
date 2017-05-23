package application.controller;

import java.io.IOException;
import java.util.List;

import application.config.ResponseMessage;
import application.models.RespWithUsers;
import application.accountservice.AccountService;
import application.models.RespWithUser;
import application.models.Resp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import application.user.UserProfile;

import javax.servlet.http.HttpSession;

@RestController
@Component
@RequestMapping(value = "/api")
public class UserController {
    public static final String EMAIL = "userEmail";
    public static final String LOGIN = "logIn";

    private static final Logger LOGGER = Logger.getLogger("maga");

    private final AccountService accountService;

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
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
//            if (userProfile.isEmpty()) {
//                LOGGER.warn(ResponseMessage.BAD_REQUEST);
//                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
//            }
            if (accountService.isSignUp(userProfile.getEmail(), userProfile.getPassword())) {
                if (session.getAttribute(LOGIN) == null) {
                    session.setAttribute(LOGIN, true);
                    session.setAttribute(EMAIL, userProfile.getEmail());
                }
                LOGGER.info(ResponseMessage.SUCCESS);
                return ResponseEntity.ok(new RespWithUser(0, accountService.getUser(userProfile.getEmail())));
            }
            LOGGER.warn(ResponseMessage.REGISTRATION);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.REGISTRATION), HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser(HttpSession session) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                LOGGER.info(ResponseMessage.SUCCESS);
                return ResponseEntity.ok(new RespWithUser(0, (accountService.getUser((String) (session.getAttribute(EMAIL))))));
            }
            LOGGER.warn(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.BAD_REQUEST);
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
//                if (userProfile.isEmpty()) {
//                    accountService.getUser(session.getAttribute(EMAIL).toString()).setUsername(userProfile.getUsername());
//                    accountService.getUser(session.getAttribute(EMAIL).toString()).setPassword(userProfile.getPassword());
//                    LOGGER.info(ResponseMessage.SUCCESS);
//                    return ResponseEntity.ok(new Resp(0, ResponseMessage.SUCCESS));
//                }
                LOGGER.warn(ResponseMessage.BAD_REQUEST);
                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
            LOGGER.warn(ResponseMessage.LOGIN);
            return new ResponseEntity<>(new Resp(1, ResponseMessage.LOGIN), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserProfile userProfile, HttpSession session) throws IOException {
        try {
//            if (userProfile.isEmpty()) {
//                LOGGER.warn(ResponseMessage.BAD_REQUEST);
//                return new ResponseEntity<>(new Resp(2, ResponseMessage.BAD_REQUEST), HttpStatus.BAD_REQUEST);
//            }
            if (accountService.isSignUp(userProfile.getEmail(), userProfile.getPassword())) {
                LOGGER.warn(ResponseMessage.CONFLICT);
                return new ResponseEntity<>(new Resp(3, ResponseMessage.CONFLICT), HttpStatus.CONFLICT);
            }
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            accountService.addUser(userProfile);
            LOGGER.info(ResponseMessage.SUCCESS + session.getId());
            return new ResponseEntity<>(new RespWithUser(0, userProfile), HttpStatus.CREATED);
        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/stats/{count}", method = RequestMethod.GET)
    public ResponseEntity<?> getMMR(@PathVariable(value = "count") int count) throws IOException {
        try {
            final List<UserProfile> userProfiles = accountService.sort();
            final RespWithUsers respWithUsers = new RespWithUsers();
            for (int i = 0; i < count; ++i) {
                respWithUsers.addUser(userProfiles.get(i));
            }
            respWithUsers.setKey(0);
            LOGGER.info(ResponseMessage.SUCCESS);
            return ResponseEntity.ok(respWithUsers);

        } catch (RuntimeException ignored) {
            LOGGER.error(ignored.getMessage());
            return new ResponseEntity<>(new Resp(4, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }
}