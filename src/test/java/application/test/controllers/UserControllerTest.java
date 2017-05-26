package application.test.controllers;

import application.controller.UserControllerWithDB;
import application.user.UserProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.jws.soap.SOAPBinding;

import java.sql.Connection;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by isopov on 29.09.16.
 */

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserProfile userProfile = new UserProfile("username", "password", "email");

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session = new MockHttpSession();

    @Before
    public void init(){
        String Sql = "truncate table user_project";
        jdbcTemplate.execute(Sql);
    }

    @Test
    public void testRegirstration() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        userProfile.setEmail(",");
        userProfile.setUsername("a");
        userProfile.setPassword("a");
        mockMvc.perform(post("/api/DB/auth/regirstration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        userProfile.setEmail("b@");
        userProfile.setPassword("b");
        userProfile.setUsername("b");
        mockMvc.perform(post("/api/DB/auth/regirstration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))).andExpect(status().isCreated());
        session.setAttribute(UserControllerWithDB.EMAIL, userProfile.getEmail());
        session.setAttribute(UserControllerWithDB.LOGIN, true);
        mockMvc.perform(post("/api/DB/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetInfoUser() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        userProfile.setUsername("c");
        userProfile.setPassword("c");
        userProfile.setEmail("c@");
        mockMvc.perform(post("/api/DB/auth/regirstration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))).andExpect(status().isCreated());
        session.setAttribute(UserControllerWithDB.EMAIL, userProfile.getEmail());
        session.setAttribute(UserControllerWithDB.LOGIN, true);

        mockMvc.perform(get("/api/DB/user/getInfoUser")
                .session(session)
        ).andExpect(status().isOk());
    }

    @Test
    public void testSignOut() throws Exception {
        mockMvc.perform(get("/api/DB/auth/signOut")
        ).andExpect(status().isOk());
    }
}