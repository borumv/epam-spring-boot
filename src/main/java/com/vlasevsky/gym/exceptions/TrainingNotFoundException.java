package com.vlasevsky.gym.exceptions;

public class TrainingNotFoundException extends NotFoundException {
    public TrainingNotFoundException(Long id) {
        super("Training with id" + id + " hasn't been found");
    }
}
