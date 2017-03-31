package application.controller;

import java.io.IOException;
import java.util.List;

import application.models.RespWithUsers;
import application.accountService.AccountService;
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
    public static final String SUCCESS = "success";

    private static final Logger LOGGER = Logger.getLogger("maga");

    private final AccountService accountService;

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
            if (accountService.isSignUp(userProfile.getEmail(), userProfile.getPassword())) {
                if (session.getAttribute(LOGIN) == null) {
                    session.setAttribute(LOGIN, true);
                    session.setAttribute(EMAIL, userProfile.getEmail());
                }
                LOGGER.debug("Success login");
                return ResponseEntity.ok(new RespWithUser(0, accountService.getUser(userProfile.getEmail())));
            }
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
                return ResponseEntity.ok(new RespWithUser(0, (accountService.getUser((String) (session.getAttribute(EMAIL))))));
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
                if (userProfile.isEmpty()) {
                    accountService.getUser(session.getAttribute(EMAIL).toString()).setLogin(userProfile.getLogin());
                    accountService.getUser(session.getAttribute(EMAIL).toString()).setPassword(userProfile.getPassword());
                    LOGGER.debug("User data succesfully updated");
                    return ResponseEntity.ok(new Resp(0, "User data succesfully updated"));
                }
                LOGGER.debug("Not all required parameters provided");
                return new ResponseEntity<>(new Resp(2, "Not all required parameters provided"), HttpStatus.BAD_REQUEST);
            }
            LOGGER.debug("don't login");
            return new ResponseEntity<>(new Resp(1, "you don't login"), HttpStatus.BAD_REQUEST);
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
            if (accountService.isSignUp(userProfile.getEmail(), userProfile.getPassword())) {
                LOGGER.debug("This application.user alredy exist");
                return new ResponseEntity<>(new Resp(3, "This application.user alredy exist"), HttpStatus.CONFLICT);
            }
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            accountService.addUser(userProfile);
            LOGGER.debug("Success registration" + session.getId());
            return new ResponseEntity<>(new RespWithUser(0, userProfile), HttpStatus.CREATED);
        } catch (RuntimeException ignored) {
            LOGGER.debug("Iternal server error");
            return new ResponseEntity<>(new Resp(4, "Iternal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/stats/{count}", method = RequestMethod.GET)
    public ResponseEntity<?> getMMR(@PathVariable(value = "count") int count) throws IOException {
        try {
            if (count > accountService.getSize()) {
                return new ResponseEntity<>(new Resp(1, "count > countUser"), HttpStatus.BAD_REQUEST);
            }
            final List<UserProfile> userProfiles = accountService.sort();
            final RespWithUsers respWithUsers = new RespWithUsers();
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
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }
}