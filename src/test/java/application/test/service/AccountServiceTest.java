package application.test.service;

import application.accountService.AccountService;
import org.junit.Before;
import org.junit.Test;
import application.user.UserProfile;
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

}
