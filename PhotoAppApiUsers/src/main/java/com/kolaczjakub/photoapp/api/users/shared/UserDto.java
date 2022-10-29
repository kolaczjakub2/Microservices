package com.kolaczjakub.photoapp.api.users.shared;

import com.kolaczjakub.photoapp.api.users.model.AlbumResponseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {
    private String password;
    private String encryptedPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private List<AlbumResponseModel> albums;
}

