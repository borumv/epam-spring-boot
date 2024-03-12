package org.vlasevsky.gym.exceptions;

public class TraineeNotFoundException extends NotFoundException{
    public TraineeNotFoundException(Long id) {
        super("Trainee with id" + id + " hasn't been found");
    }
}
