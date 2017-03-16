package controller;

import java.io.BufferedReader;
import java.io.IOException;

import accountService.AccountService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.UserProfile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/auth")
public class UserAuthController {

    private final AccountService accountService;

    @RequestMapping(value = "/signOut", method = RequestMethod.POST)
    public void signOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (accountService.isLogIn(req.getSession().getId())){
            accountService.deleteSession(req.getSession().getId());
        }
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject.addProperty("message", "succes");
        jsonObject.addProperty("key",  "200");
        jsonObject1.add("response", jsonObject.getAsJsonObject());
        resp.getWriter().write(jsonObject1.toString());
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public void signIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        UserProfile userProfile = gson.fromJson(jb.toString(), UserProfile.class);

        if(userProfile.isEmpty()){
            jsonObject.addProperty("message", "You have incorrect data");
            jsonObject.addProperty("key", "401");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
            return;
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            jsonObject.addProperty("message", "succes");
            jsonObject.addProperty("User", gson.toJson(accountService.getUserOfSession(req.getSession().getId())));
            jsonObject.addProperty("key",  "200");
            if (!accountService.isLogIn(req.getSession().getId())) {
                accountService.addSession(req.getSession().getId(), userProfile);
            }
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
        } else {
            jsonObject.addProperty("message", "you don't signUp");
            jsonObject.addProperty("key",  "400");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());        }
    }


    @RequestMapping(value = "/getInfoUser", method = RequestMethod.POST)
    public void getInfoUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();

        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        UserProfile userProfile = gson.fromJson(jb.toString(), UserProfile.class);
        if (accountService.isLogIn(req.getSession().getId())){

            jsonObject.addProperty("key", "200");
            jsonObject.addProperty("User", gson.toJson(accountService.getUserOfSession(req.getSession().getId())));
            jsonObject.addProperty("message", "succes");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
        } else {
            jsonObject.addProperty("message", "you don't is login");
            jsonObject.addProperty("key", "400");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
        }
    }

    @RequestMapping(value = "/setInfoUser", method = RequestMethod.POST)
    public void setInfoUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(jb.toString(), UserProfile.class);
        JsonObject jsonObject1 = new JsonObject();
        if(accountService.isLogIn(req.getSession().getId())) {
            JsonObject jsonObject = new JsonObject();
            accountService.getUserOfSession(req.getSession().getId()).setEmail(userProfile.getEmail());
            accountService.getUserOfSession(req.getSession().getId()).setLogin(userProfile.getLogin());
            accountService.getUserOfSession(req.getSession().getId()).setPassword(userProfile.getPassword());

            jsonObject.addProperty("key", "200");
            jsonObject.addProperty("User", gson.toJson(userProfile));
            jsonObject.addProperty("message", "succes");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", "you don't is login");
            jsonObject.addProperty("key", "400");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());        }
    }


    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public void signUp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        UserProfile userProfile = gson.fromJson(jb.toString(), UserProfile.class);
        if(userProfile.isEmpty()){
            jsonObject.addProperty("message", "You have incorrect data");
            jsonObject.addProperty("key", "401");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
            return;
        }
        if (accountService.isSignUp(userProfile.getEmail())) {
            jsonObject.addProperty("message", "you already signUp, enter other data");
            jsonObject.addProperty("key", "400");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());
        } else {
            accountService.addSession(req.getSession().getId(), userProfile);
            accountService.addUser(userProfile);

            jsonObject.addProperty("key", "200");
            jsonObject.addProperty("User", gson.toJson(userProfile));
            jsonObject.addProperty("message", "success");
            jsonObject1.add("response", jsonObject.getAsJsonObject());
            resp.getWriter().write(jsonObject1.toString());

        }
    }

    @Autowired
    public UserAuthController(AccountService accountService) {
        this.accountService = accountService;
    }
}