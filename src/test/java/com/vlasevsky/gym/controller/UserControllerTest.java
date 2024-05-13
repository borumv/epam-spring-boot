package com.vlasevsky.gym.controller;

import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void loginSuccess() throws Exception {
        // Given
        String username = "user";
        String password = "pass";
        CredentialsDto credentials = new CredentialsDto(username, password);
        Mockito.when(userService.login(credentials)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(get("/user/login")
                .param("username", username)
                .param("password", password));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));
    }

    @Test
    void loginUnauthorized() throws Exception {
        // Given
        String username = "user";
        String password = "wrong_pass";
        CredentialsDto credentials = new CredentialsDto(username, password);
        Mockito.when(userService.login(credentials)).thenReturn(false);

        // When
        ResultActions resultActions = mockMvc.perform(get("/user/login")
                .param("username", username)
                .param("password", password));

        // Then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    void changePasswordSuccess() throws Exception {
        // Given
        String username = "user";
        String password = "pass";
        String newPassword = "new_pass";
        CredentialsDto credentials = new CredentialsDto(username, password);
        Mockito.when(userService.changePassword(credentials, newPassword)).thenReturn(true);

        // When
        ResultActions resultActions = mockMvc.perform(put("/user/change-password")
                .param("username", username)
                .param("password", password)
                .param("newPassword", newPassword));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("200 OK"));
    }

    @Test
    void changePasswordUnauthorized() throws Exception {
        // Given
        String username = "user";
        String password = "wrong_pass";
        String newPassword = "new_pass";
        CredentialsDto credentials = new CredentialsDto(username, password);
        Mockito.when(userService.changePassword(credentials, newPassword)).thenReturn(false);

        // When
        ResultActions resultActions = mockMvc.perform(put("/user/change-password")
                .param("username", username)
                .param("password", password)
                .param("newPassword", newPassword));

        // Then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }
}