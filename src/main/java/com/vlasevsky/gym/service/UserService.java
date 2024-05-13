package com.vlasevsky.gym.service;


import com.vlasevsky.gym.dto.CredentialsDto;
import com.vlasevsky.gym.model.User;

public interface UserService extends BaseService<User, Long>{

    boolean login(CredentialsDto credentialsDto);

    boolean changePassword(CredentialsDto credentialsDto, String newPassword);
}
