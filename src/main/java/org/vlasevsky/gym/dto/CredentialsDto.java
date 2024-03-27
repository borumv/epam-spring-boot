package org.vlasevsky.gym.dto;

public class CredentialsDto {
    private String username;
    private String password;

    public CredentialsDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
