package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {

    private UsersRepository usersRepository;
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    void testAuthenticateWithCorrectLoginAndPassword() {
        User user = new User(1L, "user", "password", false);

        when(usersRepository.findByLogin("user")).thenReturn(user);

        boolean result = usersService.authenticate("user", "password");

        assertTrue(result);
        assertTrue(user.isAuthenticated());
        verify(usersRepository, times(1)).update(user);
    }

    @Test
    void testAuthenticateWithAlreadyAuthenticatedUser() {
        User user = new User(1L, "user", "password", true);

        when(usersRepository.findByLogin("user")).thenReturn(user);

        assertThrows(AlreadyAuthenticatedException.class, () -> usersService.authenticate("user", "password"));
        verify(usersRepository, never()).update(user);
    }

    @Test
    void testAuthenticateWithWrongLogin() {
        when(usersRepository.findByLogin("user")).thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () -> usersService.authenticate("user", "password"));
    }

    @Test
    void testAuthenticateWithWrongPassword() {
        User user = new User(1L, "user", "password", false);

        when(usersRepository.findByLogin("user")).thenReturn(user);

        boolean result = usersService.authenticate("user", "wrongpassword");

        assertFalse(result);
        assertFalse(user.isAuthenticated());
        verify(usersRepository, never()).update(user);
    }
}
