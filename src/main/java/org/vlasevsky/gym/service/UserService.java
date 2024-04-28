package org.vlasevsky.gym.service;

import org.vlasevsky.gym.dto.CredentialsDto;
import org.vlasevsky.gym.model.User;

public interface UserService extends BaseService<User, Long>{

    boolean login(CredentialsDto credentialsDto);

    boolean changePassword(CredentialsDto credentialsDto, String newPassword);
}
