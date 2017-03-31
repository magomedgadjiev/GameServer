package application.test.domain;

import org.junit.Test;
import application.user.UserProfile;

import static org.junit.Assert.assertEquals;

/**
 * Created by magomed on 26.03.17.
 */
public class UserTest {
    @Test
    public void test(){
        final UserProfile userProfile = new UserProfile();
        userProfile.setEmail("avenger@mail.ru");
        assertEquals("avenger@mail.ru", userProfile.getEmail());
    }
}
