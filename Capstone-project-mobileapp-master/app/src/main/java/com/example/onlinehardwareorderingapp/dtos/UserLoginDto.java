package com.example.onlinehardwareorderingapp.dtos;

public class UserLoginDto {
    private String userName;
    private String password;

    public UserLoginDto(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
    public UserLoginDto(){}
    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
