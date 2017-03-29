package application.test.controllers;

import application.accountService.AccountService;
import application.user.UserProfile;
import org.eclipse.jetty.server.Authentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


import javax.jws.soap.SOAPBinding;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by isopov on 29.09.16.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @MockBean
    private AccountService accountServic;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMeRequiresLogin() {
        UserProfile userProfile = new UserProfile("login", "password", "email");
        ResponseEntity<UserProfile> meResp = restTemplate.postForEntity("/api/DB/auth/regirstration", userProfile, UserProfile.class);
        assertEquals(HttpStatus.CREATED, meResp.getStatusCode());

        UserProfile userProfile1 = new UserProfile("aa", "dd", "email");

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/DB/auth/signOut", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserProfile userProfile2 = new UserProfile();
        userProfile2.setLogin("a");
        userProfile2.setPassword("a");
        responseEntity = restTemplate.postForEntity("/api/DB/auth/regirstration", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = restTemplate.postForEntity("/api/DB/auth/login", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = restTemplate.postForEntity("/api/DB/user/setInfoUser", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = restTemplate.getForEntity("/api/DB/user/getInfoUser", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}