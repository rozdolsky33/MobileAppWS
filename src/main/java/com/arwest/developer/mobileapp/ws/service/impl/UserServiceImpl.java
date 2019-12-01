package com.arwest.developer.mobileapp.ws.service.impl;

import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import com.arwest.developer.mobileapp.ws.repository.UserRepository;
import com.arwest.developer.mobileapp.ws.service.UserService;
import com.arwest.developer.mobileapp.ws.shared.Utils;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;


    @Override
    public UserDto createUser(UserDto user) {


        if(userRepository.findUserByEmail(user.getEmail()) != null){
            throw new RuntimeException("Record already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setEncryptedPassword("tst1");
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }
}
