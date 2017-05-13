package application.test.service;

import application.accountservice.AccountService;
import application.user.UserProfile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by magomed on 26.03.17.
 */
public class AccountServiceTest {
    private AccountService usersService;

    @Before
    public void setup(){
        usersService = new AccountService();
    }


    @Test
    public void ensureExists(){
        assertNotNull(usersService);
    }

    @Test
    public void getUser(){
        final UserProfile userProfile = new UserProfile("a", "a", "a");
        usersService.addUser(userProfile);
        assert usersService.isSignUp("a", "a");
    }

    @Test
    public void getUserNotFound(){
        final UserProfile userProfile = new UserProfile("a", "a", "a");
        usersService.addUser(userProfile);
        assert !usersService.isSignUp("aaa", "aaaa");
    }





}
