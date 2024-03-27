package org.vlasevsky.gym.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class User implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

//    public void createProfile() {
//        this.username = generateUsername(firstName, lastName);
//        this.password = generateRandomPassword(10);
//    }

//    private String generateUsername(String firstName, String lastName) {
//        String baseUsername = firstName + "." + lastName;
//        String finalUsername = baseUsername;
//        int suffix = 1;
//
//        while (existingUsernames.contains(finalUsername)) {
//            finalUsername = baseUsername + suffix;
//            suffix++;
//        }
//
//        existingUsernames.add(finalUsername);
//
//        return finalUsername;
//    }
//
//    private String generateRandomPassword(int length) {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuilder password = new StringBuilder(length);
//
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            password.append(characters.charAt(index));
//        }
//
//        return password.toString();
//    }

}
