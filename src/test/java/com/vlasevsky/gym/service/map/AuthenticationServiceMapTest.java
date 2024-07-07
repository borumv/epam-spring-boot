package com.vlasevsky.gym.service;

import com.vlasevsky.gym.dto.*;
import com.vlasevsky.gym.model.Role;
import com.vlasevsky.gym.model.Trainee;
import com.vlasevsky.gym.model.User;
import com.vlasevsky.gym.repository.UserRepository;
import com.vlasevsky.gym.security.config.JwtService;
import com.vlasevsky.gym.service.map.AuthenticationServiceMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock(lenient = true)
    private PasswordEncoder passwordEncoder;

    @Mock(lenient = true)
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceMap authenticationService;

    private static final TraineeRegistrationDto TRAINEE_REGISTRATION_DTO = new TraineeRegistrationDto("John", "Doe", null, "123 Street");
    private static final AuthenticationRequest AUTHENTICATION_REQUEST = new AuthenticationRequest("john.doe", "password");
    private static final String GENERATED_USERNAME = "john.doe";
    private static final String GENERATED_PASSWORD = "encodedPassword";
    private static final String JWT_TOKEN = "jwtToken";

    private static final User USER = new User();
    static {
        USER.setUsername(GENERATED_USERNAME);
        USER.setPassword(GENERATED_PASSWORD);
        USER.setRole(Role.USER);
    }

    @BeforeEach
    void setUp() {
        doReturn(GENERATED_PASSWORD).when(passwordEncoder).encode(anyString());
        doReturn(JWT_TOKEN).when(jwtService).generateToken(any(User.class));
    }

    @SneakyThrows
    @Test
    void testRegisterTrainee() {
        doReturn(false).when(userRepository).existsByUsername(anyString());

        RegistrationResponse response = authenticationService.registerTrainee(TRAINEE_REGISTRATION_DTO);

        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.token());
        assertEquals(10, response.password().length());
        verify(userRepository, times(1)).save(any(Trainee.class));
    }

    @SneakyThrows
    @Test
    void testAuthenticate() {
        doReturn(Optional.of(USER)).when(userRepository).findByUsername(anyString());

        AuthenticationResponse response = authenticationService.authenticate(AUTHENTICATION_REQUEST);

        assertNotNull(response);
        assertEquals(JWT_TOKEN, response.token());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testGenerateRandomPassword() {
        String password = authenticationService.generateRandomPassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }
}
