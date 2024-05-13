package com.vlasevsky.gym.service.map;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.exceptions.UserNotFoundException;
import com.vlasevsky.gym.model.User;
import com.vlasevsky.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCredentialsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCredentialsService userCredentialsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUsernameWithUniqueName() {

        String firstName = "John";
        String lastName = "Doe";
        when(userRepository.existsByUsername("John.Doe")).thenReturn(false);

        String result = userCredentialsService.generateUsername(firstName, lastName);

        assertEquals("John.Doe", result);
    }

    @Test
    void generateUsernameWithExistingName() {

        String firstName = "John";
        String lastName = "Doe";
        when(userRepository.existsByUsername("John.Doe")).thenReturn(true);
        when(userRepository.existsByUsername("John.Doe1")).thenReturn(false);

        String result = userCredentialsService.generateUsername(firstName, lastName);

        assertEquals("John.Doe1", result);
    }

    @Test
    void generateRandomPassword() {

        String password = userCredentialsService.generateRandomPassword();

        assertEquals(10, password.length());
    }

    @Test
    void changePasswordWithValidUser() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPassword("oldPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userCredentialsService.changePassword(userId, "newPassword");

        assertEquals("newPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePasswordWithInvalidUser() {

        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userCredentialsService.changePassword(userId, "newPassword"));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void checkCredentialsValid() {

        CredentialsDto credentials = new CredentialsDto("testUser", "password");
        when(userRepository.login(credentials)).thenReturn(true);

        boolean result = userCredentialsService.checkCredentials(credentials);

        assertTrue(result);
        verify(userRepository, times(1)).login(credentials);
    }

    @Test
    void checkCredentialsInvalid() {
        CredentialsDto credentials = new CredentialsDto("testUser", "wrongPassword");
        when(userRepository.login(credentials)).thenReturn(false);

        boolean result = userCredentialsService.checkCredentials(credentials);

        assertFalse(result);
        verify(userRepository, times(1)).login(credentials);
    }

    @Test
    void loginValid() {

        CredentialsDto credentials = new CredentialsDto("testUser", "password");
        when(userRepository.login(credentials)).thenReturn(true);

        boolean result = userCredentialsService.login(credentials);

        assertTrue(result);
        verify(userRepository, times(1)).login(credentials);
    }

    @Test
    void loginInvalid() {

        CredentialsDto credentials = new CredentialsDto("testUser", "wrongPassword");
        when(userRepository.login(credentials)).thenReturn(false);

        boolean result = userCredentialsService.login(credentials);

        assertFalse(result);
        verify(userRepository, times(1)).login(credentials);
    }

    @Test
    void changePasswordWithValidCredentials() {

        CredentialsDto credentials = new CredentialsDto("testUser", "password");
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.login(credentials)).thenReturn(true);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        boolean result = userCredentialsService.changePassword(credentials, "newPassword");

        assertTrue(result);
        assertEquals("newPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePasswordWithInvalidCredentials() {

        CredentialsDto credentials = new CredentialsDto("testUser", "wrongPassword");
        when(userRepository.login(credentials)).thenReturn(false);

        boolean result = userCredentialsService.changePassword(credentials, "newPassword");

        assertFalse(result);
        verify(userRepository, times(0)).save(any());
    }
}