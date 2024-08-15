package com.vlasevsky.gym.actuator;

import com.vlasevsky.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UserHealthIndicator implements HealthIndicator {

    @Autowired
    protected UserRepository userRepository;

    @Override
    public Health health() {
        if(userRepository.findAll().size() > 4)
            return new Health.Builder().up().withDetail("users", "we have more than 4 users in db").build();
        else
            return new Health.Builder().down().withDetail("users", "we have less than 4 users in db").build();
    }
}
