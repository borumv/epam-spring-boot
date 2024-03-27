package org.vlasevsky.gym.exceptions;

public class UserNotFoundException  extends NotFoundException{
    public UserNotFoundException(String id) {
        super("User with id " + id + " hasn't been found");
    }
}
