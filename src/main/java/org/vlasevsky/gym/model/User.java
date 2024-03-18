package org.vlasevsky.gym.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Data
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private static Set<String> existingUsernames = new HashSet<>();

    public void createProfile() {
        this.username = generateUsername(firstName, lastName);
        this.password = generateRandomPassword(10);
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String finalUsername = baseUsername;
        int suffix = 1;

        while (existingUsernames.contains(finalUsername)) {
            finalUsername = baseUsername + suffix;
            suffix++;
        }

        existingUsernames.add(finalUsername);

        return finalUsername;
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

}
