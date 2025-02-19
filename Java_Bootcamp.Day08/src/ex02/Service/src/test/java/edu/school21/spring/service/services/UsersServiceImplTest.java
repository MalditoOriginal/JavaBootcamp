package edu.school21.spring.service.services;

import edu.school21.spring.service.config.TestApplicationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestApplicationConfig.class)
public class UsersServiceImplTest {

    @Autowired
    private UsersService usersService;

    @Test
    void testSignUpSuccess() {
        String password = usersService.signUp("test@example.com");
        assertTrue(password != null && password.length() > 0);
    }

    @Test
    void testSignUpDuplicateEmail() {
        String email = "duplicate@example.com";
        String password = usersService.signUp(email);
        assertNotNull(password);
        
        assertThrows(RuntimeException.class, () -> 
            usersService.signUp(email)
        );
    }
}
