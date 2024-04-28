package org.vlasevsky.gym.exceptions;

public class TrainerNotFoundException extends NotFoundException {
    public TrainerNotFoundException(Long id) {
        super("Trainer with id" + id + " hasn't been found");

    }

    public TrainerNotFoundException(String username) {
        super("Trainer with username" + username + " hasn't been found");
    }
}
