package com.kolaczjakub.photoapp.api.users.controllers;

import com.kolaczjakub.photoapp.api.users.model.CreateUserRequestModel;
import com.kolaczjakub.photoapp.api.users.model.CreateUserResponseModel;
import com.kolaczjakub.photoapp.api.users.model.UserResponseModel;
import com.kolaczjakub.photoapp.api.users.services.UserService;
import com.kolaczjakub.photoapp.api.users.shared.UserDto;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Environment environment;

    private final UserService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(Environment environment, UserService usersService, ModelMapper modelMapper) {
        this.environment = environment;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("status/check")
    public String status() {
        return "Working on port" + environment.getProperty("local.server.port") + ", with token = " + environment.getProperty("token.secret");
    }


    @PostMapping()
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid CreateUserRequestModel userDetails) {
        UserDto user = usersService.createUser(modelMapper.map(userDetails, UserDto.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, CreateUserResponseModel.class));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUser(@PathVariable String userId) {
        UserDto returnValue = usersService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(returnValue, UserResponseModel.class));
    }
}
