package controller;

import java.io.BufferedReader;
import java.io.IOException;

import accountService.AccountService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jsonModels.JsonResponse;
import jsonModels.JsonResponseWithUser;
import jsonModels.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import user.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Component
@RequestMapping(value = "/auth")
public class UserAuthController {

    @Autowired
    private final AccountService accountService;

    @RequestMapping(value = "/signOut", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> signOut(RequestEntity<String> req, HttpSession session) throws IOException {
        if (accountService.isLogIn(session.getId())) {
            accountService.deleteSession(session.getId());
        }
        JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> signIn(RequestEntity<String> req, HttpSession session) throws IOException {
        String reqBody = req.getBody();
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(reqBody, UserProfile.class);
        if (userProfile.isEmpty()) {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.CONFLICT);
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            if (!accountService.isLogIn(session.getId())) {
                accountService.addSession(session.getId(), userProfile);
            }
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } else {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getInfoUser", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseWithUser> getInfoUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        if (accountService.isLogIn(req.getSession().getId())) {
            JsonResponseWithUser jsonResponseWithUser = new JsonResponseWithUser(new Resp(200, "succes"), accountService.getUserOfSession(req.getSession().getId()) );
            return new ResponseEntity<>(jsonResponseWithUser, HttpStatus.OK);
        } else {
            JsonResponseWithUser jsonResponseWithUser = new JsonResponseWithUser(new Resp(200, "succes"), null);
            return new ResponseEntity<>(jsonResponseWithUser, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/setInfoUser", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> setInfoUser(RequestEntity<String> req, HttpSession session) throws IOException {
        String reqBody = req.getBody();
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(reqBody, UserProfile.class);
        if (accountService.isLogIn(session.getId())) {
            accountService.getUserOfSession(session.getId()).setEmail(userProfile.getEmail());
            accountService.getUserOfSession(session.getId()).setLogin(userProfile.getLogin());
            accountService.getUserOfSession(session.getId()).setPassword(userProfile.getPassword());
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } else {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> signUp(RequestEntity<String> req, HttpSession session) throws IOException {
        String reqBody = req.getBody();
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(reqBody, UserProfile.class);
        if (userProfile.isEmpty()) {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.CONFLICT);
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        } else {
            accountService.addSession(session.getId(), userProfile);
            accountService.addUser(userProfile);
            JsonResponse jsonResponse = new JsonResponse(new Resp(200, "succes"));
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }
    }

    @Autowired
    public UserAuthController(AccountService accountService) {
        this.accountService = accountService;
    }
}