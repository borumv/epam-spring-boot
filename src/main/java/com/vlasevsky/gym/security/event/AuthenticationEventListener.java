package com.vlasevsky.gym.security.event;

import com.vlasevsky.gym.security.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener implements ApplicationListener<org.springframework.context.ApplicationEvent> {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(org.springframework.context.ApplicationEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            Authentication authentication = ((AuthenticationFailureBadCredentialsEvent) event).getAuthentication();
            String username = getUsername(authentication);
            loginAttemptService.loginFailed(username);
        } else if (event instanceof AuthenticationSuccessEvent) {
            Authentication authentication = ((AuthenticationSuccessEvent) event).getAuthentication();
            String username = getUsername(authentication);
            loginAttemptService.loginSucceeded(username);
        }
    }

    private String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}