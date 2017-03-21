package controller;

import java.io.IOException;

import accountService.AccountService;
import Models.Response;
import Models.RespWithUser;
import Models.Resp;
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
    HttpSession session;

    @RequestMapping(value = "/auth/signOut", method = RequestMethod.POST)
    public ResponseEntity<Response> signOut() throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new Response<>(new Resp(200, SUCCESS)));
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody UserProfile userProfile) throws IOException {
        if (userProfile.isEmpty()) {
            Response<Resp> response= new Response<>(new Resp(200, "Not all required parameters provided"));
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            if (session.getAttribute(LOGIN) ==  null) {
                session.setAttribute(LOGIN, true);
            }
            return ResponseEntity.ok(new Response<>(new RespWithUser(200, accountService.getUser(userProfile.getEmail()), "Logged in succesfully")));
        } else {
            Response<Resp> response= new Response<>(new Resp(200, "You did't registration"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/getInfoUser", method = RequestMethod.GET)
    public ResponseEntity<?> getInfoUser() throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            Response<RespWithUser> response = new Response<>(new RespWithUser(200, (accountService.getUser((String)(session.getAttribute(EMAIL)))), "User created successfully"));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            Response<Resp> response= new Response<>(new Resp(200, "You don't login"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<?> setInfoUser(@RequestBody UserProfile userProfile) throws IOException {
        if (session.getAttribute(LOGIN) != null) {
            accountService.getUser(userProfile.getEmail()).setLogin(userProfile.getLogin());
            accountService.getUser(userProfile.getEmail()).setPassword(userProfile.getPassword());
            return ResponseEntity.ok(new Response<>(new Resp(200, "User data succesfully updated")));
        } else {
            Response<Resp> response= new Response<>(new Resp(200, "Not all required parameters provided"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/auth/regirstration", method = RequestMethod.POST)
    public ResponseEntity<Response> signUp(@RequestBody UserProfile userProfile) throws IOException {
        if (userProfile.isEmpty()) {
            Response<Resp> response= new Response<>(new Resp(200, "Not all required parameters provided"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            Response<Resp> response = new Response<>(new Resp(200, "This user alredy exist"));
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            session.setAttribute(LOGIN, true);
            session.setAttribute(EMAIL, userProfile.getEmail());
            accountService.addUser(userProfile);
            Response<RespWithUser> response = new Response<>(new RespWithUser(200, userProfile, "User created successfully"));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }


    @Autowired
    public UserAuthController(AccountService accountService) {
        this.accountService = accountService;
    }
}