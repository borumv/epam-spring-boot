package org.vlasevsky.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User trainee;
    private User trainer;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainer = new Trainer();
    }

//    @Test
//    void testCreateProfile() {
//        trainee.setFirstName("John");
//        trainee.setLastName("Doe");
//        trainee.createProfile();
//
//        assertEquals("John.Doe", trainee.getUsername());
//        assertNotNull(trainee.getPassword());
//        assertEquals(10, trainee.getPassword().length());
//
//        User anotherTrainee = new Trainee();
//        anotherTrainee.setFirstName("John");
//        anotherTrainee.setLastName("Doe");
//        anotherTrainee.createProfile();
//
//        assertEquals("John.Doe1", anotherTrainee.getUsername());
//    }

    @Test
    void testCreateProfileWithSpecialCharacters() {
        trainee.setFirstName("John");
        trainee.setLastName("Doe-Smith");
        trainee.createProfile();

        assertEquals("John.Doe-Smith", trainee.getUsername());
    }

    @Test
    void testCreateProfileWithDuplicateNames() {
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.createProfile();

        assertEquals("John.Doe", trainee.getUsername());

        // Create another Trainee with the same name
        User duplicateTrainee = new Trainee();
        duplicateTrainee.setFirstName("John");
        duplicateTrainee.setLastName("Doe");
        duplicateTrainee.createProfile();

        // Check that a serial number is added as a suffix to the username
        assertEquals("John.Doe1", duplicateTrainee.getUsername());

        // Create a Trainer with the same name
        User duplicateTrainer = new Trainer();
        duplicateTrainer.setFirstName("John");
        duplicateTrainer.setLastName("Doe");
        duplicateTrainer.createProfile();

        // Check that a serial number is incremented for a different user type
        assertEquals("John.Doe2", duplicateTrainer.getUsername());
    }

    @Test
    void testRandomPasswordGeneration() {
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        trainer.createProfile();

        String password = trainer.getPassword();
        assertNotNull(password);
        assertEquals(10, password.length());

        // Check if the password contains only alphanumeric characters
        assertTrue(password.matches("[A-Za-z0-9]+"));
    }
}