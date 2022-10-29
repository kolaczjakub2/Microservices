package com.kolaczjakub.photoapp.api.users.services;


import com.kolaczjakub.photoapp.api.users.data.AlbumServiceClient;
import com.kolaczjakub.photoapp.api.users.data.UserEntity;
import com.kolaczjakub.photoapp.api.users.data.UsersRepository;
import com.kolaczjakub.photoapp.api.users.model.AlbumResponseModel;
import com.kolaczjakub.photoapp.api.users.shared.UserDto;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //    private final RestTemplate restTemplate;
    private final Environment environment;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AlbumServiceClient albumServiceClient;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
//                           RestTemplate restTemplate,
                           Environment environment, AlbumServiceClient albumServiceClient) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.restTemplate = restTemplate;
        this.environment = environment;
        this.albumServiceClient = albumServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        UserEntity savedUser = usersRepository.save(userEntity);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        Optional<UserEntity> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(email);
        UserEntity userEntity = optionalUser.get();
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        Optional<UserEntity> optionalUser = usersRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Use not found");
        UserEntity userEntity = optionalUser.get();

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);

//        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
//        ResponseEntity<List<AlbumResponseModel>> response = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//        });

        List<AlbumResponseModel> albums = null;
//        try {
            albums = albumServiceClient.getAlbums(userId);
//        } catch (FeignException e) {
//            logger.error(e.getLocalizedMessage());
//        }
        userDto.setAlbums(albums);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = usersRepository.findByEmail(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(username);
        UserEntity userEntity = optionalUser.get();
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }


}
