package com.kolaczjakub.photoapp.api.users.model;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;
}
