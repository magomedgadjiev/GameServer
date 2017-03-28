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
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMeRequiresLogin() {
        UserProfile userProfile = new UserProfile("login", "password", "email");
        ResponseEntity<UserProfile> meResp = restTemplate.postForEntity("/api/auth/regirstration", userProfile, UserProfile.class);
        assertEquals(HttpStatus.CREATED, meResp.getStatusCode());



        UserProfile userProfile1 = new UserProfile("aa", "dd", "email");


        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/auth/signOut", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//        userProfile1.setLogin("");
//        responseEntity = restTemplate.postForEntity("/api/auth/login", userProfile1, String.class);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        UserProfile userProfile2 = new UserProfile();
        userProfile2.setLogin("a");
        userProfile2.setPassword("a");
        responseEntity = restTemplate.postForEntity("/api/auth/regirstration", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = restTemplate.postForEntity("/api/auth/login", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        responseEntity = restTemplate.postForEntity("/api/user/setInfoUser", userProfile2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());


        responseEntity = restTemplate.getForEntity("/api/user/getInfoUser", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

//
//
//    @Test
//    public void testMe() {
//        List<String> coockies = login();
//
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.put(HttpHeaders.COOKIE, coockies);
//        HttpEntity requestEntity = new HttpEntity(requestHeaders);
//
//        when(usersService.getUser(anyString())).thenReturn(new User("foo"));
//        ResponseEntity<User> meResp = restTemplate.exchange("/me", HttpMethod.GET, requestEntity, User.class);
//
//        assertEquals(HttpStatus.OK, meResp.getStatusCode());
//        User application.user = meResp.getBody();
//        assertNotNull(application.user);
//        assertEquals("foo", application.user.getName());
//    }
}