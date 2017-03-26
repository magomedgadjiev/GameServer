package controller;

import java.io.IOException;
import java.util.List;

import Models.RespWithUsers;
import accountService.AccountService;
import Models.Response;
import Models.RespWithUser;
import Models.Resp;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import user.UserProfile;

import javax.servlet.http.HttpSession;

@RestController
@Component
@RequestMapping(value = "/api")
public class UserAuthController {
    public static final String EMAIL = "userEmail";
    public static final String LOGIN = "logIn";
    public static final String SUCCESS = "success";

    @Autowired
    private final AccountService accountService;

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/auth/signOut", method = RequestMethod.GET)
    public ResponseEntity<Response> signOut() throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new Response<>(new Resp(0, SUCCESS)));
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile) throws IOException {
        try {
            if (userProfile.isEmpty()) {
                Response<Resp> response = new Response<>(new Resp(2, "Not all required parameters provided"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (accountService.isSignUp(userProfile.getEmail())) {
                if (session.getAttribute(LOGIN) == null) {
                    session.setAttribute(LOGIN, true);
                    session.setAttribute(EMAIL, userProfile.getEmail());
                }
                return ResponseEntity.ok(new Response<>(new RespWithUser(0, accountService.getUser(userProfile.getEmail()))));
            } else {
                Response<Resp> response = new Response<>(new Resp(1, "You did't registration"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored) {
            return new ResponseEntity<Response<Resp>>(new Response<Resp>(new Resp(4, "Iternal server error")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser() throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                return ResponseEntity.ok(new Response<>(new RespWithUser(0, (accountService.getUser((String) (session.getAttribute(EMAIL)))))));
            } else {
                return new ResponseEntity<>(new Response<>(new Resp(1, "You don't login")), HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored) {
            return new ResponseEntity<Response<Resp>>(new Response<Resp>(new Resp(4, "Iternal server error")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> setInfoUser(@RequestBody UserProfile userProfile) throws IOException {
        try {
            if (session.getAttribute(LOGIN) != null) {
                if (userProfile.isEmpty()) {
                    accountService.getUser(session.getAttribute(EMAIL).toString()).setLogin(userProfile.getLogin());
                    accountService.getUser(session.getAttribute(EMAIL).toString()).setPassword(userProfile.getPassword());
                    return ResponseEntity.ok(new Response<>(new Resp(0, "User data succesfully updated")));
                } else {
                    return new ResponseEntity<>(new Response<>(new Resp(2, "Not all required parameters provided")), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new Response<>(new Resp(1, "you don't login")), HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored) {
            return new ResponseEntity<Response<Resp>>(new Response<Resp>(new Resp(4, "Iternal server error")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserProfile userProfile) throws IOException, JsonParseException {
        try {
            if (userProfile.isEmpty()) {
                Response<Resp> response = new Response<>(new Resp(2, "Not all required parameters provided"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (accountService.isSignUp(userProfile.getEmail())) {
                Response<Resp> response = new Response<>(new Resp(3, "This user alredy exist"));
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                session.setAttribute(LOGIN, true);
                session.setAttribute(EMAIL, userProfile.getEmail());
                accountService.addUser(userProfile);
                return new ResponseEntity<>(new Response<>(new RespWithUser(0, userProfile)), HttpStatus.CREATED);
            }
        } catch (RuntimeException ignored) {
            return new ResponseEntity<Response<Resp>>(new Response<Resp>(new Resp(4, "Iternal server error")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/stats/{count}", method = RequestMethod.POST)
    public ResponseEntity<?> getMMR(@PathVariable(value = "count") int count, @RequestBody UserProfile userProfile) throws IOException {
        try {
            if (count > accountService.getSize()) {
                Resp resp = new Resp(1, "count > countUser");
                return new ResponseEntity<Response<Resp>>(new Response<Resp>(resp), HttpStatus.BAD_REQUEST);
            } else {
                List<UserProfile> userProfiles = accountService.sort();
                RespWithUsers respWithUsers = new RespWithUsers();
                for (int i = 0; i < count; ++i) {
                    respWithUsers.addUser(userProfiles.get(i));
                }
                respWithUsers.setKey(0);
                return ResponseEntity.ok(respWithUsers);
            }
        } catch (RuntimeException ignored) {
            return new ResponseEntity<Response<Resp>>(new Response<Resp>(new Resp(4, "Iternal server error")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public UserAuthController(AccountService accountService) {
        this.accountService = accountService;
    }
}