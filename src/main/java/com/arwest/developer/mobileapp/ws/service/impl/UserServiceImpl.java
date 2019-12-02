package com.arwest.developer.mobileapp.ws.service.impl;

import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import com.arwest.developer.mobileapp.ws.io.repositories.UserRepository;
import com.arwest.developer.mobileapp.ws.service.UserService;
import com.arwest.developer.mobileapp.ws.shared.Utils;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto user) {


        if(userRepository.findUserByEmail(user.getEmail()) != null){
            throw new RuntimeException("Record already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepository.findUserByEmail(email);

        if(userEntity == null)throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)throw new UsernameNotFoundException(userId);

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    /*
    UserServiceImpl implements UserService interface that extends org.springframework.security.core.userdetails.UserDetailsService;
    helper method to load a user in the process of sign in.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByEmail(email);

        if(userEntity == null)throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>()); // Uses SpringFramework Object "User" to look for a user with email/password in the DB
    }
}
