package org.vlasevsky.gym.service.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vlasevsky.gym.dao.UserRepository;
import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.dto.UserReadDto;
import org.vlasevsky.gym.mapper.mapstruct.UserMapper;
import org.vlasevsky.gym.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialsService userCredentialsService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");
        UserReadDto userReadDto = new UserReadDto(userId, "Boris", "Vlasevsky", "boris.vlasevsky");

        when(userCredentialsService.checkCredentials(credentialsDto)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userReadDto);

        UserReadDto result = userService.findById(userId, credentialsDto);

        assertNotNull(result);
        assertEquals(userId, result.id());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDelete() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        CredentialsDto credentialsDto = new CredentialsDto("boris.vlasevsky", "password");

        when(userCredentialsService.checkCredentials(credentialsDto)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        boolean result = userService.delete(userId, credentialsDto);

        assertTrue(result);

        verify(userRepository, times(1)).delete(userId);
    }
}
