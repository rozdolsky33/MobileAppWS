package com.arwest.developer.mobileapp.ws.service.impl;

import com.arwest.developer.mobileapp.ws.io.entity.UserEntity;
import com.arwest.developer.mobileapp.ws.io.repositories.UserRepository;
import com.arwest.developer.mobileapp.ws.shared.Utils;
import com.arwest.developer.mobileapp.ws.shared.dto.AddressDTO;
import com.arwest.developer.mobileapp.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "87ghjk989ddf";
    String encryptedPassword = "ghefjgr34895895t9hegdDDS54g";

    UserEntity userEntity;

    @BeforeEach
   final void setUp() {
        MockitoAnnotations.initMocks(this);
        //Stub Object
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("John");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("tets@test.com");
        userEntity.setEmailVerificationToken("utrif004rtgfjd44345434re");
    }

    @Test
    final void testGetUser() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                ()->{
                    userService.getUser("test@test.com");
        });
    }

    @Test
    final void testCreateUser(){

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("fghjkjhg9893");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addresses);

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());


    }

}