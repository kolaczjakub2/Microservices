package com.kolaczjakub.photoapp.api.users.services;

import com.kolaczjakub.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserDetailsByEmail(String email);
    UserDto getUserByUserId(String userId);


}
