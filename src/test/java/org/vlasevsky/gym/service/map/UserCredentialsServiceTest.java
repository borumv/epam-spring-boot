package org.vlasevsky.gym.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.model.User;

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
    void testGenerateUsername() {
        String firstName = "Boris";
        String lastName = "Vlasevsky";
        String expectedUsername = "Boris.Vlasevsky";

        when(userRepository.existsByUsername(expectedUsername)).thenReturn(false);

        String username = userCredentialsService.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, username);
        verify(userRepository, times(1)).existsByUsername(expectedUsername);
    }

    @Test
    void testGenerateRandomPassword() {
        String password = userCredentialsService.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void testChangePassword() {
        Long userId = 1L;
        String newPassword = "newPassword";
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userCredentialsService.changePassword(userId, newPassword);

        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void testCheckCredentials() {
        String username = "Boris.Vlasevsky";
        String password = "password";
        CredentialsDto credentialsDto = new CredentialsDto(username, password);

        when(userRepository.checkCredentials(username, password)).thenReturn(true);

        boolean result = userCredentialsService.checkCredentials(credentialsDto);

        assertTrue(result);
        verify(userRepository, times(1)).checkCredentials(username, password);
    }
}
